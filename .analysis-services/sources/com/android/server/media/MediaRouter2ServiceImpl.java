package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class MediaRouter2ServiceImpl {
    private static final long DUMMY_REQUEST_ID = -1;
    private static final int REQUIRED_PACKAGE_IMPORTANCE_FOR_SCANNING = 100;
    final android.app.ActivityManager mActivityManager;
    private final android.app.AppOpsManager mAppOpsManager;
    private final android.content.Context mContext;
    private final android.os.Looper mLooper;
    final android.os.PowerManager mPowerManager;
    private final com.android.server.statusbar.StatusBarManagerInternal mStatusBarManagerInternal;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private static final java.lang.String TAG = "MR2ServiceImpl";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.lang.String[] BLUETOOTH_PERMISSIONS_FOR_SYSTEM_ROUTING = {"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"};
    private final java.lang.Object mLock = new java.lang.Object();
    final java.util.concurrent.atomic.AtomicInteger mNextRouterOrManagerId = new java.util.concurrent.atomic.AtomicInteger(1);
    private final android.util.SparseArray<com.android.server.media.MediaRouter2ServiceImpl.UserRecord> mUserRecords = new android.util.SparseArray<>();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> mAllRouterRecords = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord> mAllManagerRecords = new android.util.ArrayMap<>();
    private int mCurrentActiveUserId = -1;
    private final android.app.ActivityManager.OnUidImportanceListener mOnUidImportanceListener = new android.app.ActivityManager.OnUidImportanceListener() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda9
        public final void onUidImportance(int i, int i2) {
            this.f$0.lambda$new$0(i, i2);
        }
    };
    private final android.content.BroadcastReceiver mScreenOnOffReceiver = new com.android.server.media.MediaRouter2ServiceImpl.AnonymousClass1();
    private final android.app.AppOpsManager.OnOpChangedListener mOnOpChangedListener = new android.app.AppOpsManager.OnOpChangedListener() { // from class: com.android.server.media.MediaRouter2ServiceImpl.2
        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(java.lang.String op, java.lang.String packageName) {
        }

        public void onOpChanged(java.lang.String op, java.lang.String packageName, int userId) {
            if (!android.text.TextUtils.equals(op, "android:media_routing_control")) {
                return;
            }
            synchronized (com.android.server.media.MediaRouter2ServiceImpl.this.mLock) {
                com.android.server.media.MediaRouter2ServiceImpl.this.revokeManagerRecordAccessIfNeededLocked(packageName, userId);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int uid, int importance) {
        synchronized (this.mLock) {
            int count = this.mUserRecords.size();
            for (int i = 0; i < count; i++) {
                this.mUserRecords.valueAt(i).mHandler.maybeUpdateDiscoveryPreferenceForUid(uid);
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.media.MediaRouter2ServiceImpl$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            synchronized (com.android.server.media.MediaRouter2ServiceImpl.this.mLock) {
                int count = com.android.server.media.MediaRouter2ServiceImpl.this.mUserRecords.size();
                for (int i = 0; i < count; i++) {
                    com.android.server.media.MediaRouter2ServiceImpl.UserHandler userHandler = ((com.android.server.media.MediaRouter2ServiceImpl.UserRecord) com.android.server.media.MediaRouter2ServiceImpl.this.mUserRecords.valueAt(i)).mHandler;
                    userHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).updateDiscoveryPreferenceOnHandler();
                        }
                    }, userHandler));
                }
            }
        }
    }

    MediaRouter2ServiceImpl(android.content.Context context, android.os.Looper looper) {
        this.mContext = context;
        this.mLooper = looper;
        this.mActivityManager = (android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class);
        this.mActivityManager.addOnUidImportanceListener(this.mOnUidImportanceListener, 100);
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mStatusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
        android.content.IntentFilter screenOnOffIntentFilter = new android.content.IntentFilter();
        screenOnOffIntentFilter.addAction("android.intent.action.SCREEN_ON");
        screenOnOffIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mContext.registerReceiver(this.mScreenOnOffReceiver, screenOnOffIntentFilter);
        this.mAppOpsManager.startWatchingMode(139, (java.lang.String) null, this.mOnOpChangedListener);
        this.mContext.getPackageManager().addOnPermissionsChangeListener(new android.content.pm.PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda10
            public final void onPermissionsChanged(int i) {
                this.f$0.onPermissionsChanged(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPermissionsChanged(final int uid) {
        synchronized (this.mLock) {
            java.util.Optional<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> affectedRouter = this.mAllRouterRecords.values().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda22
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.media.MediaRouter2ServiceImpl.lambda$onPermissionsChanged$1(uid, (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj);
                }
            }).findFirst();
            if (affectedRouter.isPresent()) {
                affectedRouter.get().maybeUpdateSystemRoutingPermissionLocked();
            }
        }
    }

    static /* synthetic */ boolean lambda$onPermissionsChanged$1(int uid, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord it) {
        return it.mUid == uid;
    }

    public java.util.List<android.media.MediaRoute2Info> getSystemRoutes(java.lang.String callerPackageName, boolean isProxyRouter) {
        boolean hasSystemRoutingPermissions;
        java.util.Collection<android.media.MediaRoute2Info> systemRoutes;
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
        if (!isProxyRouter) {
            hasSystemRoutingPermissions = checkCallerHasSystemRoutingPermissions(pid, uid);
        } else {
            hasSystemRoutingPermissions = checkCallerHasPrivilegedRoutingPermissions(pid, uid, callerPackageName);
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = getOrCreateUserRecordLocked(userId);
                if (hasSystemRoutingPermissions) {
                    android.media.MediaRoute2ProviderInfo providerInfo = userRecord.mHandler.mSystemProvider.getProviderInfo();
                    if (providerInfo != null) {
                        systemRoutes = providerInfo.getRoutes();
                    } else {
                        systemRoutes = java.util.Collections.emptyList();
                    }
                } else {
                    systemRoutes = new java.util.ArrayList<>();
                    systemRoutes.add(userRecord.mHandler.mSystemProvider.getDefaultRoute());
                }
            }
            return new java.util.ArrayList(systemRoutes);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean showMediaOutputSwitcherWithRouter2(java.lang.String packageName) {
        android.os.UserHandle userHandle = android.os.Binder.getCallingUserHandle();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return showOutputSwitcher(packageName, userHandle);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void registerRouter2(android.media.IMediaRouter2 router, java.lang.String packageName) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        if (android.text.TextUtils.isEmpty(packageName)) {
            throw new java.lang.IllegalArgumentException("packageName must not be empty");
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
        boolean hasConfigureWifiDisplayPermission = this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY") == 0;
        boolean hasModifyAudioRoutingPermission = checkCallerHasModifyAudioRoutingPermission(pid, uid);
        boolean hasMediaContentControlPermission = checkMediaContentControlPermission(uid, pid);
        boolean hasMediaRoutingControlPermission = checkMediaRoutingControlPermission(uid, pid, packageName);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerRouter2Locked(router, uid, pid, packageName, userId, hasConfigureWifiDisplayPermission, hasModifyAudioRoutingPermission, hasMediaContentControlPermission, hasMediaRoutingControlPermission);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void unregisterRouter2(android.media.IMediaRouter2 router) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                unregisterRouter2Locked(router, false);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void updateScanningState(android.media.IMediaRouter2 router, int scanningState) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        validateScanningStateValue(scanningState);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                updateScanningStateLocked(router, scanningState);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setDiscoveryRequestWithRouter2(android.media.IMediaRouter2 router, android.media.RouteDiscoveryPreference preference) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        java.util.Objects.requireNonNull(preference, "preference must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(router.asBinder());
                if (routerRecord == null) {
                    android.util.Slog.w(TAG, "Ignoring updating discoveryRequest of null routerRecord.");
                } else {
                    setDiscoveryRequestWithRouter2Locked(routerRecord, preference);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setRouteListingPreference(android.media.IMediaRouter2 router, android.media.RouteListingPreference routeListingPreference) {
        android.content.ComponentName linkedItemLandingComponent;
        if (routeListingPreference != null) {
            linkedItemLandingComponent = routeListingPreference.getLinkedItemComponentName();
        } else {
            linkedItemLandingComponent = null;
        }
        if (linkedItemLandingComponent != null) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.media.MediaServerUtils.enforcePackageName(this.mContext, linkedItemLandingComponent.getPackageName(), callingUid);
            if (!com.android.server.media.MediaServerUtils.isValidActivityComponentName(this.mContext, linkedItemLandingComponent, "android.media.action.TRANSFER_MEDIA", android.os.Binder.getCallingUserHandle())) {
                throw new java.lang.IllegalArgumentException("Unable to resolve " + linkedItemLandingComponent + " to a valid activity for android.media.action.TRANSFER_MEDIA");
            }
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(router.asBinder());
                if (routerRecord == null) {
                    android.util.Slog.w(TAG, "Ignoring updating route listing of null routerRecord.");
                } else {
                    setRouteListingPreferenceLocked(routerRecord, routeListingPreference);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setRouteVolumeWithRouter2(android.media.IMediaRouter2 router, android.media.MediaRoute2Info route, int volume) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        java.util.Objects.requireNonNull(route, "route must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setRouteVolumeWithRouter2Locked(router, route, volume);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void requestCreateSessionWithRouter2(android.media.IMediaRouter2 router, int requestId, long managerRequestId, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.Bundle sessionHints) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        java.util.Objects.requireNonNull(oldSession, "oldSession must not be null");
        java.util.Objects.requireNonNull(route, "route must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestCreateSessionWithRouter2Locked(requestId, managerRequestId, router, oldSession, route, sessionHints);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void selectRouteWithRouter2(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        java.util.Objects.requireNonNull(route, "route must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                selectRouteWithRouter2Locked(router, uniqueSessionId, route);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void deselectRouteWithRouter2(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        java.util.Objects.requireNonNull(route, "route must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                deselectRouteWithRouter2Locked(router, uniqueSessionId, route);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void transferToRouteWithRouter2(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        java.util.Objects.requireNonNull(route, "route must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        android.os.UserHandle userHandle = android.os.Binder.getCallingUserHandle();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                transferToRouteWithRouter2Locked(router, userHandle, uniqueSessionId, route);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setSessionVolumeWithRouter2(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId, int volume) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        java.util.Objects.requireNonNull(uniqueSessionId, "uniqueSessionId must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setSessionVolumeWithRouter2Locked(router, uniqueSessionId, volume);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void releaseSessionWithRouter2(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId) {
        java.util.Objects.requireNonNull(router, "router must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                releaseSessionWithRouter2Locked(router, uniqueSessionId);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public java.util.List<android.media.RoutingSessionInfo> getRemoteSessions(android.media.IMediaRouter2Manager manager) {
        java.util.List<android.media.RoutingSessionInfo> remoteSessionsLocked;
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                remoteSessionsLocked = getRemoteSessionsLocked(manager);
            }
            return remoteSessionsLocked;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void registerManager(android.media.IMediaRouter2Manager manager, java.lang.String callerPackageName) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        if (android.text.TextUtils.isEmpty(callerPackageName)) {
            throw new java.lang.IllegalArgumentException("callerPackageName must not be empty");
        }
        int callerUid = android.os.Binder.getCallingUid();
        int callerPid = android.os.Binder.getCallingPid();
        android.os.UserHandle callerUser = android.os.Binder.getCallingUserHandle();
        enforcePrivilegedRoutingPermissions(callerUid, callerPid, callerPackageName);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerManagerLocked(manager, callerUid, callerPid, callerPackageName, null, callerUser);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void registerProxyRouter(android.media.IMediaRouter2Manager manager, java.lang.String callerPackageName, java.lang.String targetPackageName, android.os.UserHandle targetUser) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        java.util.Objects.requireNonNull(targetUser, "targetUser must not be null");
        if (android.text.TextUtils.isEmpty(targetPackageName)) {
            throw new java.lang.IllegalArgumentException("targetPackageName must not be empty");
        }
        int callerUid = android.os.Binder.getCallingUid();
        int callerPid = android.os.Binder.getCallingPid();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            enforcePrivilegedRoutingPermissions(callerUid, callerPid, callerPackageName);
            enforceCrossUserPermissions(callerUid, callerPid, targetUser);
            if (!verifyPackageExistsForUser(targetPackageName, targetUser)) {
                throw new java.lang.IllegalArgumentException("targetPackageName does not exist: " + targetPackageName);
            }
            synchronized (this.mLock) {
                registerManagerLocked(manager, callerUid, callerPid, callerPackageName, targetPackageName, targetUser);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void unregisterManager(android.media.IMediaRouter2Manager manager) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                unregisterManagerLocked(manager, false);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void updateScanningState(android.media.IMediaRouter2Manager manager, int scanningState) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        validateScanningStateValue(scanningState);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                updateScanningStateLocked(manager, scanningState);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setRouteVolumeWithManager(android.media.IMediaRouter2Manager manager, int requestId, android.media.MediaRoute2Info route, int volume) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        java.util.Objects.requireNonNull(route, "route must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setRouteVolumeWithManagerLocked(requestId, manager, route, volume);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void requestCreateSessionWithManager(android.media.IMediaRouter2Manager manager, int requestId, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        java.util.Objects.requireNonNull(oldSession, "oldSession must not be null");
        java.util.Objects.requireNonNull(route, "route must not be null");
        java.util.Objects.requireNonNull(transferInitiatorUserHandle);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestCreateSessionWithManagerLocked(requestId, manager, oldSession, route, transferInitiatorUserHandle, transferInitiatorPackageName);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void selectRouteWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        java.util.Objects.requireNonNull(route, "route must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                selectRouteWithManagerLocked(requestId, manager, uniqueSessionId, route);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void deselectRouteWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        java.util.Objects.requireNonNull(route, "route must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                deselectRouteWithManagerLocked(requestId, manager, uniqueSessionId, route);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void transferToRouteWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        java.util.Objects.requireNonNull(route, "route must not be null");
        java.util.Objects.requireNonNull(transferInitiatorUserHandle);
        java.util.Objects.requireNonNull(transferInitiatorPackageName);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                transferToRouteWithManagerLocked(requestId, manager, uniqueSessionId, route, 1, transferInitiatorUserHandle, transferInitiatorPackageName);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setSessionVolumeWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String uniqueSessionId, int volume) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setSessionVolumeWithManagerLocked(requestId, manager, uniqueSessionId, volume);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void releaseSessionWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String uniqueSessionId) {
        java.util.Objects.requireNonNull(manager, "manager must not be null");
        if (android.text.TextUtils.isEmpty(uniqueSessionId)) {
            throw new java.lang.IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                releaseSessionWithManagerLocked(requestId, manager, uniqueSessionId);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean showMediaOutputSwitcherWithProxyRouter(android.media.IMediaRouter2Manager proxyRouter) {
        boolean zShowOutputSwitcher;
        java.util.Objects.requireNonNull(proxyRouter, "Proxy router must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                android.os.IBinder binder = proxyRouter.asBinder();
                com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord proxyRouterRecord = this.mAllManagerRecords.get(binder);
                if (proxyRouterRecord.mTargetPackageName == null) {
                    throw new java.lang.UnsupportedOperationException("Only proxy routers can show the Output Switcher.");
                }
                zShowOutputSwitcher = showOutputSwitcher(proxyRouterRecord.mTargetPackageName, android.os.UserHandle.of(proxyRouterRecord.mUserRecord.mUserId));
            }
            return zShowOutputSwitcher;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public android.media.RoutingSessionInfo getSystemSessionInfo(java.lang.String callerPackageName, java.lang.String targetPackageName, boolean setDeviceRouteSelected) {
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
        boolean hasSystemRoutingPermissions = targetPackageName == null ? checkCallerHasSystemRoutingPermissions(pid, uid) : checkCallerHasPrivilegedRoutingPermissions(pid, uid, callerPackageName);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = getOrCreateUserRecordLocked(userId);
                if (!hasSystemRoutingPermissions) {
                    return new android.media.RoutingSessionInfo.Builder(userRecord.mHandler.mSystemProvider.getDefaultSessionInfo()).setClientPackageName(targetPackageName).build();
                }
                if (setDeviceRouteSelected) {
                    return userRecord.mHandler.mSystemProvider.generateDeviceRouteSelectedSessionInfo(targetPackageName);
                }
                java.util.List<android.media.RoutingSessionInfo> sessionInfos = userRecord.mHandler.mSystemProvider.getSessionInfos();
                if (!sessionInfos.isEmpty()) {
                    return new android.media.RoutingSessionInfo.Builder(sessionInfos.get(0)).setClientPackageName(targetPackageName).build();
                }
                android.util.Slog.w(TAG, "System provider does not have any session info.");
                android.os.Binder.restoreCallingIdentity(token);
                return null;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean checkCallerHasSystemRoutingPermissions(int pid, int uid) {
        return checkCallerHasModifyAudioRoutingPermission(pid, uid) || checkCallerHasBluetoothPermissions(pid, uid);
    }

    private boolean checkCallerHasPrivilegedRoutingPermissions(int pid, int uid, java.lang.String callerPackageName) {
        return checkMediaContentControlPermission(uid, pid) || checkMediaRoutingControlPermission(uid, pid, callerPackageName);
    }

    private boolean checkCallerHasModifyAudioRoutingPermission(int pid, int uid) {
        return this.mContext.checkPermission("android.permission.MODIFY_AUDIO_ROUTING", pid, uid) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkCallerHasBluetoothPermissions(int pid, int uid) {
        boolean hasBluetoothRoutingPermission = true;
        for (java.lang.String permission : BLUETOOTH_PERMISSIONS_FOR_SYSTEM_ROUTING) {
            hasBluetoothRoutingPermission &= this.mContext.checkPermission(permission, pid, uid) == 0;
        }
        return hasBluetoothRoutingPermission;
    }

    private void enforcePrivilegedRoutingPermissions(int callerUid, int callerPid, java.lang.String callerPackageName) {
        if (!checkMediaContentControlPermission(callerUid, callerPid) && !checkMediaRoutingControlPermission(callerUid, callerPid, callerPackageName)) {
            throw new java.lang.SecurityException("Must hold MEDIA_CONTENT_CONTROL or MEDIA_ROUTING_CONTROL permissions.");
        }
    }

    private boolean checkMediaContentControlPermission(int callerUid, int callerPid) {
        return this.mContext.checkPermission("android.permission.MEDIA_CONTENT_CONTROL", callerPid, callerUid) == 0;
    }

    private boolean checkMediaRoutingControlPermission(int callerUid, int callerPid, java.lang.String callerPackageName) {
        return com.android.media.flags.Flags.enablePrivilegedRoutingForMediaRoutingControl() && android.content.PermissionChecker.checkPermissionForDataDelivery(this.mContext, "android.permission.MEDIA_ROUTING_CONTROL", callerPid, callerUid, callerPackageName, (java.lang.String) null, "Checking permissions for registering manager in MediaRouter2ServiceImpl.") == 0;
    }

    private boolean verifyPackageExistsForUser(java.lang.String clientPackageName, android.os.UserHandle user) {
        try {
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            pm.getPackageInfoAsUser(clientPackageName, android.content.pm.PackageManager.PackageInfoFlags.of(0L), user.getIdentifier());
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void enforceCrossUserPermissions(int callerUid, int callerPid, android.os.UserHandle targetUser) {
        int callerUserId = android.os.UserHandle.getUserId(callerUid);
        if (targetUser.getIdentifier() != callerUserId) {
            this.mContext.enforcePermission("android.permission.INTERACT_ACROSS_USERS_FULL", callerPid, callerUid, "Must hold INTERACT_ACROSS_USERS_FULL to control an app in a different userId.");
        }
    }

    private boolean showOutputSwitcher(java.lang.String packageName, android.os.UserHandle userHandle) {
        if (this.mActivityManager.getPackageImportance(packageName) > 100) {
            android.util.Slog.w(TAG, "showMediaOutputSwitcher only works when called from foreground");
            return false;
        }
        synchronized (this.mLock) {
            this.mStatusBarManagerInternal.showMediaOutputSwitcher(packageName, userHandle);
        }
        return true;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "MediaRouter2ServiceImpl");
        java.lang.String indent = prefix + "  ";
        synchronized (this.mLock) {
            pw.println(indent + "mNextRouterOrManagerId=" + this.mNextRouterOrManagerId.get());
            pw.println(indent + "mCurrentActiveUserId=" + this.mCurrentActiveUserId);
            pw.println(indent + "UserRecords:");
            if (this.mUserRecords.size() > 0) {
                for (int i = 0; i < this.mUserRecords.size(); i++) {
                    this.mUserRecords.valueAt(i).dump(pw, indent + "  ");
                }
            } else {
                pw.println(indent + "  <no user records>");
            }
        }
    }

    void updateRunningUserAndProfiles(int newActiveUserId) {
        synchronized (this.mLock) {
            if (this.mCurrentActiveUserId != newActiveUserId) {
                android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("switchUser | user: %d", new java.lang.Object[]{java.lang.Integer.valueOf(newActiveUserId)}));
                this.mCurrentActiveUserId = newActiveUserId;
                android.util.SparseArray<com.android.server.media.MediaRouter2ServiceImpl.UserRecord> userRecords = this.mUserRecords.clone();
                for (int i = 0; i < userRecords.size(); i++) {
                    int userId = userRecords.keyAt(i);
                    com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = userRecords.valueAt(i);
                    if (isUserActiveLocked(userId)) {
                        userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda21(), userRecord.mHandler));
                    } else {
                        userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda20(), userRecord.mHandler));
                        disposeUserIfNeededLocked(userRecord);
                    }
                }
            }
        }
    }

    void routerDied(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord) {
        synchronized (this.mLock) {
            unregisterRouter2Locked(routerRecord.mRouter, true);
        }
    }

    void managerDied(com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord) {
        synchronized (this.mLock) {
            unregisterManagerLocked(managerRecord.mManager, true);
        }
    }

    private boolean isUserActiveLocked(int userId) {
        return this.mUserManagerInternal.getProfileParentId(userId) == this.mCurrentActiveUserId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeManagerRecordAccessIfNeededLocked(final java.lang.String packageName, int userId) {
        com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = this.mUserRecords.get(userId);
        if (userRecord == null) {
            return;
        }
        java.util.List<com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord> managers = (java.util.List) userRecord.mManagerRecords.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.media.MediaRouter2ServiceImpl.lambda$revokeManagerRecordAccessIfNeededLocked$2((com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord) obj);
            }
        }).filter(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return android.text.TextUtils.equals(((com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord) obj).mOwnerPackageName, packageName);
            }
        }).collect(java.util.stream.Collectors.toList());
        if (managers.isEmpty()) {
            return;
        }
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord record = (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord) managers.getFirst();
        boolean isAppOpAllowed = this.mAppOpsManager.unsafeCheckOpNoThrow("android:media_routing_control", record.mOwnerUid, record.mOwnerPackageName) == 0;
        if (isAppOpAllowed) {
            return;
        }
        for (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager : managers) {
            boolean isRegularPermission = this.mContext.checkPermission("android.permission.MEDIA_ROUTING_CONTROL", manager.mOwnerPid, manager.mOwnerUid) == 0;
            if (!isRegularPermission) {
                android.util.Log.w(TAG, android.text.TextUtils.formatSimple("Revoking access to manager record id: %d, package: %s, userId: %d", new java.lang.Object[]{java.lang.Integer.valueOf(manager.mManagerId), manager.mOwnerPackageName, java.lang.Integer.valueOf(userRecord.mUserId)}));
                unregisterManagerLocked(manager.mManager, false);
                try {
                    manager.mManager.invalidateInstance();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to notify manager= " + manager + " of permission revocation.");
                }
            }
        }
    }

    static /* synthetic */ boolean lambda$revokeManagerRecordAccessIfNeededLocked$2(com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord r) {
        return !r.mHasMediaContentControl;
    }

    private void registerRouter2Locked(android.media.IMediaRouter2 router, int uid, int pid, java.lang.String packageName, int userId, boolean hasConfigureWifiDisplayPermission, boolean hasModifyAudioRoutingPermission, boolean hasMediaContentControlPermission, boolean hasMediaRoutingControlPermission) {
        android.os.IBinder binder = router.asBinder();
        if (this.mAllRouterRecords.get(binder) == null) {
            com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = getOrCreateUserRecordLocked(userId);
            com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = new com.android.server.media.MediaRouter2ServiceImpl.RouterRecord(userRecord, router, uid, pid, packageName, hasConfigureWifiDisplayPermission, hasModifyAudioRoutingPermission, hasMediaContentControlPermission, hasMediaRoutingControlPermission);
            try {
                binder.linkToDeath(routerRecord, 0);
                userRecord.mRouterRecords.add(routerRecord);
                this.mAllRouterRecords.put(binder, routerRecord);
                userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda7
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).notifyRouterRegistered((com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj2);
                    }
                }, userRecord.mHandler, routerRecord));
                android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("registerRouter2 | package: %s, uid: %d, pid: %d, router id: %d, hasMediaRoutingControl: %b", new java.lang.Object[]{packageName, java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(routerRecord.mRouterId), java.lang.Boolean.valueOf(hasMediaRoutingControlPermission)}));
                return;
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException("MediaRouter2 died prematurely.", ex);
            }
        }
        android.util.Slog.w(TAG, "registerRouter2Locked: Same router already exists. packageName=" + packageName);
    }

    private void unregisterRouter2Locked(android.media.IMediaRouter2 router, boolean died) {
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.remove(router.asBinder());
        if (routerRecord == null) {
            android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Ignoring unregistering unknown router: %s, died: %b", new java.lang.Object[]{router, java.lang.Boolean.valueOf(died)}));
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("unregisterRouter2 | package: %s, router id: %d, died: %b", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), java.lang.Boolean.valueOf(died)}));
        com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = routerRecord.mUserRecord;
        userRecord.mRouterRecords.remove(routerRecord);
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda3(), routerRecord.mUserRecord.mHandler, routerRecord.mPackageName, (java.lang.Object) null));
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda4(), routerRecord.mUserRecord.mHandler, routerRecord.mPackageName, (java.lang.Object) null));
        userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda5(), userRecord.mHandler));
        routerRecord.dispose();
        disposeUserIfNeededLocked(userRecord);
    }

    private void updateScanningStateLocked(android.media.IMediaRouter2 router, int scanningState) {
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord == null) {
            android.util.Slog.w(TAG, "Router record not found. Ignoring updateScanningState call.");
            return;
        }
        boolean enableScanViaMediaContentControl = com.android.media.flags.Flags.enableFullScanWithMediaContentControl() && routerRecord.mHasMediaContentControlPermission;
        if (scanningState == 2 && !enableScanViaMediaContentControl && !routerRecord.mHasMediaRoutingControl) {
            throw new java.lang.SecurityException("Screen off scan requires MEDIA_ROUTING_CONTROL");
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("updateScanningStateLocked | router: %d, packageName: %s, scanningState: %d", new java.lang.Object[]{java.lang.Integer.valueOf(routerRecord.mRouterId), routerRecord.mPackageName, getScanningStateString(scanningState)}));
        routerRecord.updateScanningState(scanningState);
    }

    private void setDiscoveryRequestWithRouter2Locked(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, android.media.RouteDiscoveryPreference discoveryRequest) {
        if (routerRecord.mDiscoveryPreference.equals(discoveryRequest)) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("setDiscoveryRequestWithRouter2 | router: %s(id: %d), discovery request: %s", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), discoveryRequest.toString()}));
        routerRecord.mDiscoveryPreference = discoveryRequest;
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda3(), routerRecord.mUserRecord.mHandler, routerRecord.mPackageName, routerRecord.mDiscoveryPreference));
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda5(), routerRecord.mUserRecord.mHandler));
    }

    private void setRouteListingPreferenceLocked(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, android.media.RouteListingPreference routeListingPreference) {
        java.lang.String routeListingAsString;
        routerRecord.mRouteListingPreference = routeListingPreference;
        if (routeListingPreference != null) {
            routeListingAsString = (java.lang.String) routeListingPreference.getItems().stream().map(new java.util.function.Function() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda14
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.media.RouteListingPreference.Item) obj).getRouteId();
                }
            }).collect(java.util.stream.Collectors.joining(","));
        } else {
            routeListingAsString = null;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("setRouteListingPreference | router: %s(id: %d), route listing preference: [%s]", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), routeListingAsString}));
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda4(), routerRecord.mUserRecord.mHandler, routerRecord.mPackageName, routeListingPreference));
    }

    private void setRouteVolumeWithRouter2Locked(android.media.IMediaRouter2 router, android.media.MediaRoute2Info route, int volume) {
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord != null) {
            android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("setRouteVolumeWithRouter2 | router: %s(id: %d), volume: %d", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), java.lang.Integer.valueOf(volume)}));
            routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda1(), routerRecord.mUserRecord.mHandler, -1L, route, java.lang.Integer.valueOf(volume)));
        }
    }

    private void requestCreateSessionWithRouter2Locked(int requestId, long managerRequestId, android.media.IMediaRouter2 router, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.Bundle sessionHints) {
        android.media.MediaRoute2Info route2;
        android.media.MediaRoute2Info route3;
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("requestCreateSessionWithRouter2 | router: %s(id: %d), old session id: %s, new session's route id: %s, request id: %d", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), oldSession.getId(), route.getId(), java.lang.Integer.valueOf(requestId)}));
        com.android.server.media.MediaRouter2ServiceImpl.UserHandler userHandler = routerRecord.mUserRecord.mHandler;
        if (managerRequestId != 0) {
            com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager = userHandler.findManagerWithId(toRequesterId(managerRequestId));
            if (manager == null || manager.mLastSessionCreationRequest == null) {
                android.util.Slog.w(TAG, "requestCreateSessionWithRouter2Locked: Ignoring unknown request.");
                userHandler.notifySessionCreationFailedToRouter(routerRecord, requestId);
                return;
            }
            if (!android.text.TextUtils.equals(manager.mLastSessionCreationRequest.mOldSession.getId(), oldSession.getId())) {
                android.util.Slog.w(TAG, "requestCreateSessionWithRouter2Locked: Ignoring unmatched routing session.");
                userHandler.notifySessionCreationFailedToRouter(routerRecord, requestId);
                return;
            }
            if (android.text.TextUtils.equals(manager.mLastSessionCreationRequest.mRoute.getId(), route.getId())) {
                route3 = route;
            } else {
                if (routerRecord.hasSystemRoutingPermission() || !manager.mLastSessionCreationRequest.mRoute.isSystemRoute() || !route.isSystemRoute()) {
                    android.util.Slog.w(TAG, "requestCreateSessionWithRouter2Locked: Ignoring unmatched route.");
                    userHandler.notifySessionCreationFailedToRouter(routerRecord, requestId);
                    return;
                }
                route3 = manager.mLastSessionCreationRequest.mRoute;
            }
            manager.mLastSessionCreationRequest = null;
            route2 = route3;
        } else {
            java.lang.String defaultRouteId = userHandler.mSystemProvider.getDefaultRoute().getId();
            if (route.isSystemRoute() && !routerRecord.hasSystemRoutingPermission() && !android.text.TextUtils.equals(route.getId(), defaultRouteId)) {
                android.util.Slog.w(TAG, "MODIFY_AUDIO_ROUTING permission is required to transfer to" + route);
                userHandler.notifySessionCreationFailedToRouter(routerRecord, requestId);
                return;
            } else {
                android.media.MediaRoute2Info mediaRoute2Info = route;
                route2 = mediaRoute2Info;
            }
        }
        long uniqueRequestId = toUniqueRequestId(routerRecord.mRouterId, requestId);
        userHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HeptConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda13
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7) {
                ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).requestCreateSessionWithRouter2OnHandler(((java.lang.Long) obj2).longValue(), ((java.lang.Long) obj3).longValue(), (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj4, (android.media.RoutingSessionInfo) obj5, (android.media.MediaRoute2Info) obj6, (android.os.Bundle) obj7);
            }
        }, userHandler, java.lang.Long.valueOf(uniqueRequestId), java.lang.Long.valueOf(managerRequestId), routerRecord, oldSession, route2, sessionHints));
    }

    private void selectRouteWithRouter2Locked(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("selectRouteWithRouter2 | router: %s(id: %d), route: %s", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), route.getId()}));
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda0(), routerRecord.mUserRecord.mHandler, -1L, routerRecord, uniqueSessionId, route));
    }

    private void deselectRouteWithRouter2Locked(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("deselectRouteWithRouter2 | router: %s(id: %d), route: %s", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), route.getId()}));
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda2(), routerRecord.mUserRecord.mHandler, -1L, routerRecord, uniqueSessionId, route));
    }

    private void transferToRouteWithRouter2Locked(android.media.IMediaRouter2 router, android.os.UserHandle transferInitiatorUserHandle, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("transferToRouteWithRouter2 | router: %s(id: %d), route: %s", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), route.getId()}));
        com.android.server.media.MediaRouter2ServiceImpl.UserHandler userHandler = routerRecord.mUserRecord.mHandler;
        java.lang.String defaultRouteId = userHandler.mSystemProvider.getDefaultRoute().getId();
        if (route.isSystemRoute() && !routerRecord.hasSystemRoutingPermission() && !android.text.TextUtils.equals(route.getId(), defaultRouteId)) {
            userHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda11
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).notifySessionCreationFailedToRouter((com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj2, ((java.lang.Integer) obj3).intValue());
                }
            }, userHandler, routerRecord, java.lang.Integer.valueOf(toOriginalRequestId(-1L))));
        } else {
            userHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda12(), userHandler, -1L, transferInitiatorUserHandle, routerRecord.mPackageName, routerRecord, uniqueSessionId, route, 2));
        }
    }

    private void setSessionVolumeWithRouter2Locked(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId, int volume) {
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("setSessionVolumeWithRouter2 | router: %s(id: %d), session: %s, volume: %d", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), uniqueSessionId, java.lang.Integer.valueOf(volume)}));
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda8(), routerRecord.mUserRecord.mHandler, -1L, uniqueSessionId, java.lang.Integer.valueOf(volume)));
    }

    private void releaseSessionWithRouter2Locked(android.media.IMediaRouter2 router, java.lang.String uniqueSessionId) {
        android.os.IBinder binder = router.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mAllRouterRecords.get(binder);
        if (routerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("releaseSessionWithRouter2 | router: %s(id: %d), session: %s", new java.lang.Object[]{routerRecord.mPackageName, java.lang.Integer.valueOf(routerRecord.mRouterId), uniqueSessionId}));
        routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda6(), routerRecord.mUserRecord.mHandler, -1L, routerRecord, uniqueSessionId));
    }

    private java.util.List<android.media.RoutingSessionInfo> getRemoteSessionsLocked(android.media.IMediaRouter2Manager manager) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            android.util.Slog.w(TAG, "getRemoteSessionLocked: Ignoring unknown manager");
            return java.util.Collections.emptyList();
        }
        java.util.List<android.media.RoutingSessionInfo> sessionInfos = new java.util.ArrayList<>();
        for (com.android.server.media.MediaRoute2Provider provider : managerRecord.mUserRecord.mHandler.mRouteProviders) {
            if (!provider.mIsSystemRouteProvider) {
                sessionInfos.addAll(provider.getSessionInfos());
            }
        }
        return sessionInfos;
    }

    private void registerManagerLocked(android.media.IMediaRouter2Manager manager, int callerUid, int callerPid, java.lang.String callerPackageName, java.lang.String targetPackageName, android.os.UserHandle targetUser) {
        android.os.IBinder binder = manager.asBinder();
        if (this.mAllManagerRecords.get(binder) != null) {
            android.util.Slog.w(TAG, "registerManagerLocked: Same manager already exists. callerPackageName=" + callerPackageName);
            return;
        }
        boolean hasMediaRoutingControl = checkMediaRoutingControlPermission(callerUid, callerPid, callerPackageName);
        boolean hasMediaContentControl = checkMediaContentControlPermission(callerUid, callerPid);
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("registerManager | callerUid: %d, callerPid: %d, callerPackage: %s, targetPackageName: %s, targetUserId: %d, hasMediaRoutingControl: %b", new java.lang.Object[]{java.lang.Integer.valueOf(callerUid), java.lang.Integer.valueOf(callerPid), callerPackageName, targetPackageName, targetUser, java.lang.Boolean.valueOf(hasMediaRoutingControl)}));
        com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = getOrCreateUserRecordLocked(targetUser.getIdentifier());
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = new com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord(userRecord, manager, callerUid, callerPid, callerPackageName, targetPackageName, hasMediaRoutingControl, hasMediaContentControl);
        try {
            binder.linkToDeath(managerRecord, 0);
            userRecord.mManagerRecords.add(managerRecord);
            this.mAllManagerRecords.put(binder, managerRecord);
            for (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord : userRecord.mRouterRecords) {
                userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda4(), routerRecord.mUserRecord.mHandler, routerRecord.mPackageName, routerRecord.mRouteListingPreference));
                routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda17
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).notifyDiscoveryPreferenceChangedToManager((com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj2, (android.media.IMediaRouter2Manager) obj3);
                    }
                }, routerRecord.mUserRecord.mHandler, routerRecord, manager));
            }
            userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda18
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).notifyInitialRoutesToManager((android.media.IMediaRouter2Manager) obj2);
                }
            }, userRecord.mHandler, manager));
        } catch (android.os.RemoteException ex) {
            throw new java.lang.RuntimeException("Media router manager died prematurely.", ex);
        }
    }

    private void unregisterManagerLocked(android.media.IMediaRouter2Manager manager, boolean died) {
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.remove(manager.asBinder());
        if (managerRecord == null) {
            android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Ignoring unregistering unknown manager: %s, died: %b", new java.lang.Object[]{manager, java.lang.Boolean.valueOf(died)}));
            return;
        }
        com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = managerRecord.mUserRecord;
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("unregisterManager | package: %s, user: %d, manager: %d, died: %b", new java.lang.Object[]{managerRecord.mOwnerPackageName, java.lang.Integer.valueOf(userRecord.mUserId), java.lang.Integer.valueOf(managerRecord.mManagerId), java.lang.Boolean.valueOf(died)}));
        userRecord.mManagerRecords.remove(managerRecord);
        managerRecord.dispose();
        disposeUserIfNeededLocked(userRecord);
    }

    private void updateScanningStateLocked(android.media.IMediaRouter2Manager manager, int scanningState) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            android.util.Slog.w(TAG, "Manager record not found. Ignoring updateScanningState call.");
            return;
        }
        boolean enableScanViaMediaContentControl = com.android.media.flags.Flags.enableFullScanWithMediaContentControl() && managerRecord.mHasMediaContentControl;
        if (!managerRecord.mHasMediaRoutingControl && !enableScanViaMediaContentControl && scanningState == 2) {
            throw new java.lang.SecurityException("Screen off scan requires MEDIA_ROUTING_CONTROL");
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("updateScanningState | manager: %d, ownerPackageName: %s, targetPackageName: %s, scanningState: %d", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), managerRecord.mOwnerPackageName, managerRecord.mTargetPackageName, getScanningStateString(scanningState)}));
        managerRecord.updateScanningState(scanningState);
    }

    private void setRouteVolumeWithManagerLocked(int requestId, android.media.IMediaRouter2Manager manager, android.media.MediaRoute2Info route, int volume) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("setRouteVolumeWithManager | manager: %d, route: %s, volume: %d", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), route.getId(), java.lang.Integer.valueOf(volume)}));
        long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, requestId);
        managerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda1(), managerRecord.mUserRecord.mHandler, java.lang.Long.valueOf(uniqueRequestId), route, java.lang.Integer.valueOf(volume)));
    }

    private void requestCreateSessionWithManagerLocked(int requestId, android.media.IMediaRouter2Manager manager, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("requestCreateSessionWithManager | manager: %d, route: %s", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), route.getId()}));
        java.lang.String packageName = oldSession.getClientPackageName();
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = managerRecord.mUserRecord.findRouterRecordLocked(packageName);
        if (routerRecord != null) {
            long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, requestId);
            com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest lastRequest = managerRecord.mLastSessionCreationRequest;
            if (lastRequest != null) {
                android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("requestCreateSessionWithManagerLocked: Notifying failure for pending session creation request - oldSession: %s, route: %s", new java.lang.Object[]{lastRequest.mOldSession, lastRequest.mRoute}));
                managerRecord.mUserRecord.mHandler.notifyRequestFailedToManager(managerRecord.mManager, toOriginalRequestId(lastRequest.mManagerRequestId), 0);
            }
            managerRecord.mLastSessionCreationRequest = new com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest(routerRecord, 0L, uniqueRequestId, oldSession, route);
            routerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.OctConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda19
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).requestRouterCreateSessionOnHandler(((java.lang.Long) obj2).longValue(), (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj3, (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord) obj4, (android.media.RoutingSessionInfo) obj5, (android.media.MediaRoute2Info) obj6, (android.os.UserHandle) obj7, (java.lang.String) obj8);
                }
            }, routerRecord.mUserRecord.mHandler, java.lang.Long.valueOf(uniqueRequestId), routerRecord, managerRecord, oldSession, route, transferInitiatorUserHandle, transferInitiatorPackageName));
            return;
        }
        android.util.Slog.w(TAG, "requestCreateSessionWithManagerLocked: Ignoring session creation for unknown router.");
        try {
            managerRecord.mManager.notifyRequestFailed(requestId, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "requestCreateSessionWithManagerLocked: Failed to notify failure. Manager probably died.");
        }
    }

    private void selectRouteWithManagerLocked(int requestId, android.media.IMediaRouter2Manager manager, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("selectRouteWithManager | manager: %d, session: %s, route: %s", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), uniqueSessionId, route.getId()}));
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(uniqueSessionId);
        long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, requestId);
        managerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda0(), managerRecord.mUserRecord.mHandler, java.lang.Long.valueOf(uniqueRequestId), routerRecord, uniqueSessionId, route));
    }

    private void deselectRouteWithManagerLocked(int requestId, android.media.IMediaRouter2Manager manager, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("deselectRouteWithManager | manager: %d, session: %s, route: %s", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), uniqueSessionId, route.getId()}));
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(uniqueSessionId);
        long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, requestId);
        managerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda2(), managerRecord.mUserRecord.mHandler, java.lang.Long.valueOf(uniqueRequestId), routerRecord, uniqueSessionId, route));
    }

    private void transferToRouteWithManagerLocked(int requestId, android.media.IMediaRouter2Manager manager, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route, int transferReason, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("transferToRouteWithManager | manager: %d, session: %s, route: %s", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), uniqueSessionId, route.getId()}));
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(uniqueSessionId);
        long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, requestId);
        managerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda12(), managerRecord.mUserRecord.mHandler, java.lang.Long.valueOf(uniqueRequestId), transferInitiatorUserHandle, transferInitiatorPackageName, routerRecord, uniqueSessionId, route, java.lang.Integer.valueOf(transferReason)));
    }

    private void setSessionVolumeWithManagerLocked(int requestId, android.media.IMediaRouter2Manager manager, java.lang.String uniqueSessionId, int volume) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("setSessionVolumeWithManager | manager: %d, session: %s, volume: %d", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), uniqueSessionId, java.lang.Integer.valueOf(volume)}));
        long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, requestId);
        managerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda8(), managerRecord.mUserRecord.mHandler, java.lang.Long.valueOf(uniqueRequestId), uniqueSessionId, java.lang.Integer.valueOf(volume)));
    }

    private void releaseSessionWithManagerLocked(int requestId, android.media.IMediaRouter2Manager manager, java.lang.String uniqueSessionId) {
        android.os.IBinder binder = manager.asBinder();
        com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord = this.mAllManagerRecords.get(binder);
        if (managerRecord == null) {
            return;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("releaseSessionWithManager | manager: %d, session: %s", new java.lang.Object[]{java.lang.Integer.valueOf(managerRecord.mManagerId), uniqueSessionId}));
        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(uniqueSessionId);
        long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, requestId);
        managerRecord.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda6(), managerRecord.mUserRecord.mHandler, java.lang.Long.valueOf(uniqueRequestId), routerRecord, uniqueSessionId));
    }

    private com.android.server.media.MediaRouter2ServiceImpl.UserRecord getOrCreateUserRecordLocked(int userId) {
        com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord = this.mUserRecords.get(userId);
        if (userRecord == null) {
            userRecord = new com.android.server.media.MediaRouter2ServiceImpl.UserRecord(userId, this.mLooper);
            this.mUserRecords.put(userId, userRecord);
            userRecord.init();
            if (isUserActiveLocked(userId)) {
                userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda21(), userRecord.mHandler));
            }
        }
        return userRecord;
    }

    private void disposeUserIfNeededLocked(com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord) {
        if (!isUserActiveLocked(userRecord.mUserId) && userRecord.mRouterRecords.isEmpty() && userRecord.mManagerRecords.isEmpty()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, userRecord + ": Disposed");
            }
            userRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda20(), userRecord.mHandler));
            this.mUserRecords.remove(userRecord.mUserId);
        }
    }

    static long toUniqueRequestId(int requesterId, int originalRequestId) {
        return (((long) requesterId) << 32) | ((long) originalRequestId);
    }

    static int toRequesterId(long uniqueRequestId) {
        return (int) (uniqueRequestId >> 32);
    }

    static int toOriginalRequestId(long uniqueRequestId) {
        return (int) uniqueRequestId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getScanningStateString(int scanningState) {
        switch (scanningState) {
            case 0:
                return "NOT_SCANNING";
            case 1:
                return "SCREEN_ON_ONLY";
            case 2:
                return "FULL";
            default:
                return "Invalid scanning state: " + scanningState;
        }
    }

    private static void validateScanningStateValue(int scanningState) {
        if (scanningState != 0 && scanningState != 1 && scanningState != 2) {
            throw new java.lang.IllegalArgumentException(android.text.TextUtils.formatSimple("Scanning state %d is not valid.", new java.lang.Object[]{java.lang.Integer.valueOf(scanningState)}));
        }
    }

    final class UserRecord {
        final com.android.server.media.MediaRouter2ServiceImpl.UserHandler mHandler;
        public final int mUserId;
        final java.util.ArrayList<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> mRouterRecords = new java.util.ArrayList<>();
        final java.util.ArrayList<com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord> mManagerRecords = new java.util.ArrayList<>();
        android.media.RouteDiscoveryPreference mCompositeDiscoveryPreference = android.media.RouteDiscoveryPreference.EMPTY;
        java.util.Set<java.lang.String> mActivelyScanningPackages = java.util.Set.of();

        UserRecord(int userId, android.os.Looper looper) {
            this.mUserId = userId;
            this.mHandler = new com.android.server.media.MediaRouter2ServiceImpl.UserHandler(com.android.server.media.MediaRouter2ServiceImpl.this, this, looper);
        }

        void init() {
            this.mHandler.init();
        }

        com.android.server.media.MediaRouter2ServiceImpl.RouterRecord findRouterRecordLocked(java.lang.String packageName) {
            for (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord : this.mRouterRecords) {
                if (android.text.TextUtils.equals(routerRecord.mPackageName, packageName)) {
                    return routerRecord;
                }
            }
            return null;
        }

        public void dump(final java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "UserRecord");
            final java.lang.String indent = prefix + "  ";
            pw.println(indent + "mUserId=" + this.mUserId);
            pw.println(indent + "Router Records:");
            if (!this.mRouterRecords.isEmpty()) {
                for (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord : this.mRouterRecords) {
                    routerRecord.dump(pw, indent + "  ");
                }
            } else {
                pw.println(indent + "<no router records>");
            }
            pw.println(indent + "Manager Records:");
            if (!this.mManagerRecords.isEmpty()) {
                for (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord : this.mManagerRecords) {
                    managerRecord.dump(pw, indent + "  ");
                }
            } else {
                pw.println(indent + "<no manager records>");
            }
            pw.println(indent + "Composite discovery preference:");
            this.mCompositeDiscoveryPreference.dump(pw, indent + "  ");
            pw.println(indent + "Packages actively scanning: " + java.lang.String.join(", ", this.mActivelyScanningPackages));
            if (!this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$dump$0(pw, indent);
                }
            }, 1000L)) {
                pw.println(indent + "<could not dump handler state>");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dump$0(java.io.PrintWriter pw, java.lang.String indent) {
            this.mHandler.dump(pw, indent);
        }
    }

    final class RouterRecord implements android.os.IBinder.DeathRecipient {
        public final java.util.concurrent.atomic.AtomicBoolean mHasBluetoothRoutingPermission;
        public final boolean mHasConfigureWifiDisplayPermission;
        public final boolean mHasMediaContentControlPermission;
        public final boolean mHasMediaRoutingControl;
        public final boolean mHasModifyAudioRoutingPermission;
        public final java.lang.String mPackageName;
        public final int mPid;
        public android.media.RouteListingPreference mRouteListingPreference;
        public final android.media.IMediaRouter2 mRouter;
        public final int mRouterId;
        public final int mUid;
        public final com.android.server.media.MediaRouter2ServiceImpl.UserRecord mUserRecord;
        public int mScanningState = 0;
        public final java.util.List<java.lang.Integer> mSelectRouteSequenceNumbers = new java.util.ArrayList();
        public android.media.RouteDiscoveryPreference mDiscoveryPreference = android.media.RouteDiscoveryPreference.EMPTY;

        RouterRecord(com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord, android.media.IMediaRouter2 router, int uid, int pid, java.lang.String packageName, boolean hasConfigureWifiDisplayPermission, boolean hasModifyAudioRoutingPermission, boolean hasMediaContentControlPermission, boolean hasMediaRoutingControl) {
            this.mUserRecord = userRecord;
            this.mPackageName = packageName;
            this.mRouter = router;
            this.mUid = uid;
            this.mPid = pid;
            this.mHasConfigureWifiDisplayPermission = hasConfigureWifiDisplayPermission;
            this.mHasModifyAudioRoutingPermission = hasModifyAudioRoutingPermission;
            this.mHasMediaContentControlPermission = hasMediaContentControlPermission;
            this.mHasMediaRoutingControl = hasMediaRoutingControl;
            this.mHasBluetoothRoutingPermission = new java.util.concurrent.atomic.AtomicBoolean(com.android.server.media.MediaRouter2ServiceImpl.this.checkCallerHasBluetoothPermissions(this.mPid, this.mUid));
            this.mRouterId = com.android.server.media.MediaRouter2ServiceImpl.this.mNextRouterOrManagerId.getAndIncrement();
        }

        public boolean hasSystemRoutingPermission() {
            return this.mHasModifyAudioRoutingPermission || this.mHasBluetoothRoutingPermission.get();
        }

        public boolean isActivelyScanning() {
            return this.mScanningState == 1 || this.mScanningState == 2 || this.mDiscoveryPreference.shouldPerformActiveScan();
        }

        public void maybeUpdateSystemRoutingPermissionLocked() {
            java.util.Map<java.lang.String, android.media.MediaRoute2Info> routesToReport;
            android.media.RoutingSessionInfo systemSessionToReport;
            boolean oldSystemRoutingPermissionValue = hasSystemRoutingPermission();
            this.mHasBluetoothRoutingPermission.set(com.android.server.media.MediaRouter2ServiceImpl.this.checkCallerHasBluetoothPermissions(this.mPid, this.mUid));
            boolean newSystemRoutingPermissionValue = hasSystemRoutingPermission();
            if (oldSystemRoutingPermissionValue != newSystemRoutingPermissionValue) {
                if (newSystemRoutingPermissionValue) {
                    routesToReport = this.mUserRecord.mHandler.mLastNotifiedRoutesToPrivilegedRouters;
                } else {
                    routesToReport = this.mUserRecord.mHandler.mLastNotifiedRoutesToNonPrivilegedRouters;
                }
                notifyRoutesUpdated(routesToReport.values().stream().toList());
                java.util.List<android.media.RoutingSessionInfo> sessionInfos = this.mUserRecord.mHandler.mSystemProvider.getSessionInfos();
                if (newSystemRoutingPermissionValue && !sessionInfos.isEmpty()) {
                    systemSessionToReport = sessionInfos.get(0);
                } else {
                    systemSessionToReport = this.mUserRecord.mHandler.mSystemProvider.getDefaultSessionInfo();
                }
                notifySessionInfoChanged(systemSessionToReport);
            }
        }

        public void dispose() {
            this.mRouter.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.media.MediaRouter2ServiceImpl.this.routerDied(this);
        }

        public void updateScanningState(int scanningState) {
            if (this.mScanningState == scanningState) {
                return;
            }
            this.mScanningState = scanningState;
            this.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$RouterRecord$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).updateDiscoveryPreferenceOnHandler();
                }
            }, this.mUserRecord.mHandler));
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "RouterRecord");
            java.lang.String indent = prefix + "  ";
            pw.println(indent + "mPackageName=" + this.mPackageName);
            pw.println(indent + "mSelectRouteSequenceNumbers=" + this.mSelectRouteSequenceNumbers);
            pw.println(indent + "mUid=" + this.mUid);
            pw.println(indent + "mPid=" + this.mPid);
            pw.println(indent + "mHasConfigureWifiDisplayPermission=" + this.mHasConfigureWifiDisplayPermission);
            pw.println(indent + "mHasModifyAudioRoutingPermission=" + this.mHasModifyAudioRoutingPermission);
            pw.println(indent + "mHasBluetoothRoutingPermission=" + this.mHasBluetoothRoutingPermission.get());
            pw.println(indent + "hasSystemRoutingPermission=" + hasSystemRoutingPermission());
            pw.println(indent + "mRouterId=" + this.mRouterId);
            this.mDiscoveryPreference.dump(pw, indent);
        }

        public void notifyRegistered(java.util.List<android.media.MediaRoute2Info> currentRoutes, android.media.RoutingSessionInfo currentSystemSessionInfo) {
            try {
                this.mRouter.notifyRouterRegistered(getVisibleRoutes(currentRoutes), currentSystemSessionInfo);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify router registered. Router probably died.", ex);
            }
        }

        public void notifyRoutesUpdated(java.util.List<android.media.MediaRoute2Info> routes) {
            try {
                this.mRouter.notifyRoutesUpdated(getVisibleRoutes(routes));
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify routes updated. Router probably died.", ex);
            }
        }

        public void notifySessionCreated(int requestId, android.media.RoutingSessionInfo sessionInfo) {
            try {
                this.mRouter.notifySessionCreated(requestId, maybeClearTransferInitiatorIdentity(sessionInfo));
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify router of the session creation. Router probably died.", ex);
            }
        }

        public void notifySessionInfoChanged(android.media.RoutingSessionInfo sessionInfo) {
            try {
                this.mRouter.notifySessionInfoChanged(maybeClearTransferInitiatorIdentity(sessionInfo));
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify session info changed. Router probably died.", ex);
            }
        }

        private android.media.RoutingSessionInfo maybeClearTransferInitiatorIdentity(android.media.RoutingSessionInfo sessionInfo) {
            android.os.UserHandle transferInitiatorUserHandle = sessionInfo.getTransferInitiatorUserHandle();
            java.lang.String transferInitiatorPackageName = sessionInfo.getTransferInitiatorPackageName();
            if (!java.util.Objects.equals(android.os.UserHandle.of(this.mUserRecord.mUserId), transferInitiatorUserHandle) || !java.util.Objects.equals(this.mPackageName, transferInitiatorPackageName)) {
                return new android.media.RoutingSessionInfo.Builder(sessionInfo).setTransferInitiator(null, null).build();
            }
            return sessionInfo;
        }

        private java.util.List<android.media.MediaRoute2Info> getVisibleRoutes(java.util.List<android.media.MediaRoute2Info> routes) {
            java.util.List<android.media.MediaRoute2Info> filteredRoutes = new java.util.ArrayList<>();
            for (android.media.MediaRoute2Info route : routes) {
                if (route.isVisibleTo(this.mPackageName)) {
                    filteredRoutes.add(route);
                }
            }
            return filteredRoutes;
        }
    }

    final class ManagerRecord implements android.os.IBinder.DeathRecipient {
        public final boolean mHasMediaContentControl;
        public final boolean mHasMediaRoutingControl;
        public com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest mLastSessionCreationRequest;
        public final android.media.IMediaRouter2Manager mManager;
        public final int mManagerId;
        public final java.lang.String mOwnerPackageName;
        public final int mOwnerPid;
        public final int mOwnerUid;
        public int mScanningState = 0;
        public final java.lang.String mTargetPackageName;
        public final com.android.server.media.MediaRouter2ServiceImpl.UserRecord mUserRecord;

        ManagerRecord(com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord, android.media.IMediaRouter2Manager manager, int ownerUid, int ownerPid, java.lang.String ownerPackageName, java.lang.String targetPackageName, boolean hasMediaRoutingControl, boolean hasMediaContentControl) {
            this.mUserRecord = userRecord;
            this.mManager = manager;
            this.mOwnerUid = ownerUid;
            this.mOwnerPid = ownerPid;
            this.mOwnerPackageName = ownerPackageName;
            this.mTargetPackageName = targetPackageName;
            this.mManagerId = com.android.server.media.MediaRouter2ServiceImpl.this.mNextRouterOrManagerId.getAndIncrement();
            this.mHasMediaRoutingControl = hasMediaRoutingControl;
            this.mHasMediaContentControl = hasMediaContentControl;
        }

        public void dispose() {
            this.mManager.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.media.MediaRouter2ServiceImpl.this.managerDied(this);
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "ManagerRecord");
            java.lang.String indent = prefix + "  ";
            pw.println(indent + "mOwnerPackageName=" + this.mOwnerPackageName);
            pw.println(indent + "mTargetPackageName=" + this.mTargetPackageName);
            pw.println(indent + "mManagerId=" + this.mManagerId);
            pw.println(indent + "mOwnerUid=" + this.mOwnerUid);
            pw.println(indent + "mOwnerPid=" + this.mOwnerPid);
            pw.println(indent + "mScanningState=" + com.android.server.media.MediaRouter2ServiceImpl.getScanningStateString(this.mScanningState));
            if (this.mLastSessionCreationRequest != null) {
                this.mLastSessionCreationRequest.dump(pw, indent);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateScanningState(int scanningState) {
            if (this.mScanningState == scanningState) {
                return;
            }
            this.mScanningState = scanningState;
            this.mUserRecord.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$ManagerRecord$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).updateDiscoveryPreferenceOnHandler();
                }
            }, this.mUserRecord.mHandler));
        }

        public java.lang.String toString() {
            return "Manager " + this.mOwnerPackageName + " (pid " + this.mOwnerPid + ")";
        }
    }

    static final class UserHandler extends android.os.Handler implements com.android.server.media.MediaRoute2ProviderWatcher.Callback, com.android.server.media.MediaRoute2Provider.Callback {
        private final java.util.Map<java.lang.String, android.media.MediaRoute2Info> mLastNotifiedRoutesToNonPrivilegedRouters;
        private final java.util.Map<java.lang.String, android.media.MediaRoute2Info> mLastNotifiedRoutesToPrivilegedRouters;
        private final java.util.List<android.media.MediaRoute2ProviderInfo> mLastProviderInfos;
        private final java.util.ArrayList<com.android.server.media.MediaRoute2Provider> mRouteProviders;
        private boolean mRunning;
        private final java.lang.ref.WeakReference<com.android.server.media.MediaRouter2ServiceImpl> mServiceRef;
        private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest> mSessionCreationRequests;
        private final java.util.Map<java.lang.String, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> mSessionToRouterMap;
        private final com.android.server.media.SystemMediaRoute2Provider mSystemProvider;
        private final com.android.server.media.MediaRouter2ServiceImpl.UserRecord mUserRecord;
        private final com.android.server.media.MediaRoute2ProviderWatcher mWatcher;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX INFO: renamed from: -$$Nest$mdeselectRouteOnHandler, reason: not valid java name */
        public static /* bridge */ /* synthetic */ void m5231$$Nest$mdeselectRouteOnHandler(com.android.server.media.MediaRouter2ServiceImpl.UserHandler userHandler, long j, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, java.lang.String str, android.media.MediaRoute2Info mediaRoute2Info) {
            userHandler.deselectRouteOnHandler(j, routerRecord, str, mediaRoute2Info);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX INFO: renamed from: -$$Nest$mstop, reason: not valid java name */
        public static /* bridge */ /* synthetic */ void m5246$$Nest$mstop(com.android.server.media.MediaRouter2ServiceImpl.UserHandler userHandler) {
            userHandler.stop();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX INFO: renamed from: -$$Nest$mtransferToRouteOnHandler, reason: not valid java name */
        public static /* bridge */ /* synthetic */ void m5247$$Nest$mtransferToRouteOnHandler(com.android.server.media.MediaRouter2ServiceImpl.UserHandler userHandler, long j, android.os.UserHandle userHandle, java.lang.String str, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, java.lang.String str2, android.media.MediaRoute2Info mediaRoute2Info, int i) {
            userHandler.transferToRouteOnHandler(j, userHandle, str, routerRecord, str2, mediaRoute2Info, i);
        }

        UserHandler(com.android.server.media.MediaRouter2ServiceImpl service, com.android.server.media.MediaRouter2ServiceImpl.UserRecord userRecord, android.os.Looper looper) {
            super(looper, null, true);
            this.mRouteProviders = new java.util.ArrayList<>();
            this.mLastProviderInfos = new java.util.ArrayList();
            this.mSessionCreationRequests = new java.util.concurrent.CopyOnWriteArrayList<>();
            this.mSessionToRouterMap = new android.util.ArrayMap();
            this.mLastNotifiedRoutesToPrivilegedRouters = new android.util.ArrayMap();
            this.mLastNotifiedRoutesToNonPrivilegedRouters = new android.util.ArrayMap();
            this.mServiceRef = new java.lang.ref.WeakReference<>(service);
            this.mUserRecord = userRecord;
            this.mSystemProvider = new com.android.server.media.SystemMediaRoute2Provider(service.mContext, android.os.UserHandle.of(userRecord.mUserId), looper);
            this.mRouteProviders.add(this.mSystemProvider);
            this.mWatcher = new com.android.server.media.MediaRoute2ProviderWatcher(service.mContext, this, this, this.mUserRecord.mUserId);
        }

        void init() {
            this.mSystemProvider.setCallback(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void start() {
            if (!this.mRunning) {
                this.mRunning = true;
                this.mSystemProvider.start();
                this.mWatcher.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stop() {
            if (this.mRunning) {
                this.mRunning = false;
                this.mWatcher.stop();
                this.mSystemProvider.stop();
            }
        }

        @Override // com.android.server.media.MediaRoute2ProviderWatcher.Callback
        public void onAddProviderService(com.android.server.media.MediaRoute2ProviderServiceProxy proxy) {
            proxy.setCallback(this);
            this.mRouteProviders.add(proxy);
            proxy.updateDiscoveryPreference(this.mUserRecord.mActivelyScanningPackages, this.mUserRecord.mCompositeDiscoveryPreference);
        }

        @Override // com.android.server.media.MediaRoute2ProviderWatcher.Callback
        public void onRemoveProviderService(com.android.server.media.MediaRoute2ProviderServiceProxy proxy) {
            this.mRouteProviders.remove(proxy);
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onProviderStateChanged(com.android.server.media.MediaRoute2Provider provider) {
            sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda6
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).onProviderStateChangedOnHandler((com.android.server.media.MediaRoute2Provider) obj2);
                }
            }, this, provider));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onSessionCreated(com.android.server.media.MediaRoute2Provider provider, long uniqueRequestId, android.media.RoutingSessionInfo sessionInfo) {
            sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda1
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).onSessionCreatedOnHandler((com.android.server.media.MediaRoute2Provider) obj2, ((java.lang.Long) obj3).longValue(), (android.media.RoutingSessionInfo) obj4);
                }
            }, this, provider, java.lang.Long.valueOf(uniqueRequestId), sessionInfo));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onSessionUpdated(com.android.server.media.MediaRoute2Provider provider, android.media.RoutingSessionInfo sessionInfo) {
            sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda3
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).onSessionInfoChangedOnHandler((com.android.server.media.MediaRoute2Provider) obj2, (android.media.RoutingSessionInfo) obj3);
                }
            }, this, provider, sessionInfo));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onSessionReleased(com.android.server.media.MediaRoute2Provider provider, android.media.RoutingSessionInfo sessionInfo) {
            sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda5
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).onSessionReleasedOnHandler((com.android.server.media.MediaRoute2Provider) obj2, (android.media.RoutingSessionInfo) obj3);
                }
            }, this, provider, sessionInfo));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onRequestFailed(com.android.server.media.MediaRoute2Provider provider, long uniqueRequestId, int reason) {
            sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda10
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                    ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).onRequestFailedOnHandler((com.android.server.media.MediaRoute2Provider) obj2, ((java.lang.Long) obj3).longValue(), ((java.lang.Integer) obj4).intValue());
                }
            }, this, provider, java.lang.Long.valueOf(uniqueRequestId), java.lang.Integer.valueOf(reason)));
        }

        public com.android.server.media.MediaRouter2ServiceImpl.RouterRecord findRouterWithSessionLocked(java.lang.String uniqueSessionId) {
            return this.mSessionToRouterMap.get(uniqueSessionId);
        }

        public com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord findManagerWithId(int managerId) {
            for (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager : getManagerRecords()) {
                if (manager.mManagerId == managerId) {
                    return manager;
                }
            }
            return null;
        }

        public void maybeUpdateDiscoveryPreferenceForUid(final int uid) {
            boolean isUidRelevant;
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            if (service == null) {
                return;
            }
            synchronized (service.mLock) {
                isUidRelevant = this.mUserRecord.mRouterRecords.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda7
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.media.MediaRouter2ServiceImpl.UserHandler.lambda$maybeUpdateDiscoveryPreferenceForUid$0(uid, (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj);
                    }
                }) | this.mUserRecord.mManagerRecords.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda8
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.media.MediaRouter2ServiceImpl.UserHandler.lambda$maybeUpdateDiscoveryPreferenceForUid$1(uid, (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord) obj);
                    }
                });
            }
            if (isUidRelevant) {
                sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda9
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.media.MediaRouter2ServiceImpl.UserHandler) obj).updateDiscoveryPreferenceOnHandler();
                    }
                }, this));
            }
        }

        static /* synthetic */ boolean lambda$maybeUpdateDiscoveryPreferenceForUid$0(int uid, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord router) {
            return router.mUid == uid;
        }

        static /* synthetic */ boolean lambda$maybeUpdateDiscoveryPreferenceForUid$1(int uid, com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager) {
            return manager.mOwnerUid == uid;
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "UserHandler");
            java.lang.String indent = prefix + "  ";
            pw.println(indent + "mRunning=" + this.mRunning);
            this.mSystemProvider.dump(pw, prefix);
            this.mWatcher.dump(pw, prefix);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onProviderStateChangedOnHandler(com.android.server.media.MediaRoute2Provider provider) {
            java.util.Set<java.lang.String> newRouteIds;
            java.util.Collection<android.media.MediaRoute2Info> newRoutes;
            int providerInfoIndex;
            android.media.MediaRoute2ProviderInfo newInfo = provider.getProviderInfo();
            int providerInfoIndex2 = indexOfRouteProviderInfoByUniqueId(provider.getUniqueId(), this.mLastProviderInfos);
            android.media.MediaRoute2ProviderInfo oldInfo = providerInfoIndex2 == -1 ? null : this.mLastProviderInfos.get(providerInfoIndex2);
            if (oldInfo == newInfo) {
                return;
            }
            if (newInfo != null) {
                newRoutes = newInfo.getRoutes();
                newRouteIds = (java.util.Set) newRoutes.stream().map(new java.util.function.Function() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return ((android.media.MediaRoute2Info) obj).getId();
                    }
                }).collect(java.util.stream.Collectors.toSet());
                if (providerInfoIndex2 >= 0) {
                    this.mLastProviderInfos.set(providerInfoIndex2, newInfo);
                } else {
                    this.mLastProviderInfos.add(newInfo);
                }
            } else {
                this.mLastProviderInfos.remove(oldInfo);
                newRouteIds = java.util.Collections.emptySet();
                newRoutes = java.util.Collections.emptySet();
            }
            java.util.ArrayList<android.media.MediaRoute2Info> addedRoutes = new java.util.ArrayList<>();
            boolean hasAddedOrModifiedRoutes = false;
            for (android.media.MediaRoute2Info newRouteInfo : newRoutes) {
                if (!newRouteInfo.isValid()) {
                    android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "onProviderStateChangedOnHandler: Ignoring invalid route : " + newRouteInfo);
                } else {
                    if (!provider.mIsSystemRouteProvider) {
                        this.mLastNotifiedRoutesToNonPrivilegedRouters.put(newRouteInfo.getId(), newRouteInfo);
                    }
                    android.media.MediaRoute2Info oldRouteInfo = this.mLastNotifiedRoutesToPrivilegedRouters.put(newRouteInfo.getId(), newRouteInfo);
                    hasAddedOrModifiedRoutes |= !newRouteInfo.equals(oldRouteInfo);
                    if (oldRouteInfo == null) {
                        addedRoutes.add(newRouteInfo);
                    }
                }
            }
            java.util.ArrayList<android.media.MediaRoute2Info> removedRoutes = new java.util.ArrayList<>();
            java.util.Collection<android.media.MediaRoute2Info> oldRoutes = oldInfo == null ? java.util.Collections.emptyList() : oldInfo.getRoutes();
            boolean hasRemovedRoutes = false;
            for (android.media.MediaRoute2Info oldRoute : oldRoutes) {
                java.lang.String oldRouteId = oldRoute.getId();
                if (newRouteIds.contains(oldRouteId)) {
                    providerInfoIndex = providerInfoIndex2;
                } else {
                    hasRemovedRoutes = true;
                    providerInfoIndex = providerInfoIndex2;
                    this.mLastNotifiedRoutesToPrivilegedRouters.remove(oldRouteId);
                    this.mLastNotifiedRoutesToNonPrivilegedRouters.remove(oldRouteId);
                    removedRoutes.add(oldRoute);
                }
                providerInfoIndex2 = providerInfoIndex;
            }
            if (!addedRoutes.isEmpty()) {
                android.util.Slog.i(com.android.server.media.MediaRouter2ServiceImpl.TAG, toLoggingMessage("addProviderRoutes", newInfo.getUniqueId(), addedRoutes));
            }
            if (!removedRoutes.isEmpty()) {
                android.util.Slog.i(com.android.server.media.MediaRouter2ServiceImpl.TAG, toLoggingMessage("removeProviderRoutes", oldInfo.getUniqueId(), removedRoutes));
            }
            dispatchUpdates(hasAddedOrModifiedRoutes, hasRemovedRoutes, provider.mIsSystemRouteProvider, this.mSystemProvider.getDefaultRoute());
        }

        private static java.lang.String getPackageNameFromNullableRecord(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord) {
            return routerRecord != null ? routerRecord.mPackageName : "<null router record>";
        }

        private static java.lang.String toLoggingMessage(java.lang.String source, java.lang.String providerId, java.util.ArrayList<android.media.MediaRoute2Info> routes) {
            java.lang.String routesString = (java.lang.String) routes.stream().map(new java.util.function.Function() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    android.media.MediaRoute2Info mediaRoute2Info = (android.media.MediaRoute2Info) obj;
                    return java.lang.String.format("%s | %s", mediaRoute2Info.getOriginalId(), mediaRoute2Info.getName());
                }
            }).collect(java.util.stream.Collectors.joining(", "));
            return android.text.TextUtils.formatSimple("%s | provider: %s, routes: [%s]", new java.lang.Object[]{source, providerId, routesString});
        }

        private void dispatchUpdates(boolean hasAddedOrModifiedRoutes, boolean hasRemovedRoutes, boolean isSystemProvider, android.media.MediaRoute2Info defaultRoute) {
            if (!hasAddedOrModifiedRoutes && !hasRemovedRoutes) {
                return;
            }
            java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> routerRecordsWithSystemRoutingPermission = getRouterRecords(true);
            java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> routerRecordsWithoutSystemRoutingPermission = getRouterRecords(false);
            java.util.List<android.media.IMediaRouter2Manager> managers = getManagers();
            notifyRoutesUpdatedToManagers(managers, new java.util.ArrayList(this.mLastNotifiedRoutesToPrivilegedRouters.values()));
            notifyRoutesUpdatedToRouterRecords(routerRecordsWithSystemRoutingPermission, new java.util.ArrayList(this.mLastNotifiedRoutesToPrivilegedRouters.values()));
            if (!isSystemProvider) {
                notifyRoutesUpdatedToRouterRecords(routerRecordsWithoutSystemRoutingPermission, new java.util.ArrayList(this.mLastNotifiedRoutesToNonPrivilegedRouters.values()));
            } else if (hasAddedOrModifiedRoutes) {
                this.mLastNotifiedRoutesToNonPrivilegedRouters.put(defaultRoute.getId(), defaultRoute);
                notifyRoutesUpdatedToRouterRecords(routerRecordsWithoutSystemRoutingPermission, new java.util.ArrayList(this.mLastNotifiedRoutesToNonPrivilegedRouters.values()));
            }
        }

        private static int indexOfRouteProviderInfoByUniqueId(java.lang.String uniqueId, java.util.List<android.media.MediaRoute2ProviderInfo> lastProviderInfos) {
            for (int i = 0; i < lastProviderInfos.size(); i++) {
                android.media.MediaRoute2ProviderInfo providerInfo = lastProviderInfos.get(i);
                if (android.text.TextUtils.equals(providerInfo.getUniqueId(), uniqueId)) {
                    return i;
                }
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestRouterCreateSessionOnHandler(long uniqueRequestId, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
            try {
                if (route.isSystemRoute() && !routerRecord.hasSystemRoutingPermission()) {
                    route = this.mSystemProvider.getDefaultRoute();
                }
                routerRecord.mRouter.requestCreateSessionByManager(uniqueRequestId, oldSession, route);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "getSessionHintsForCreatingSessionOnHandler: Failed to request. Router probably died.", ex);
                notifyRequestFailedToManager(managerRecord.mManager, com.android.server.media.MediaRouter2ServiceImpl.toOriginalRequestId(uniqueRequestId), 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestCreateSessionWithRouter2OnHandler(long uniqueRequestId, long managerRequestId, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.Bundle sessionHints) {
            int transferReason;
            com.android.server.media.MediaRoute2Provider provider = findProvider(route.getProviderId());
            if (provider == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "requestCreateSessionWithRouter2OnHandler: Ignoring session creation request since no provider found for given route=" + route);
                notifySessionCreationFailedToRouter(routerRecord, com.android.server.media.MediaRouter2ServiceImpl.toOriginalRequestId(uniqueRequestId));
                return;
            }
            com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest request = new com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest(routerRecord, uniqueRequestId, managerRequestId, oldSession, route);
            this.mSessionCreationRequests.add(request);
            if (managerRequestId != 0) {
                transferReason = 1;
            } else {
                transferReason = 2;
            }
            provider.requestCreateSession(uniqueRequestId, routerRecord.mPackageName, route.getOriginalId(), sessionHints, transferReason, android.os.UserHandle.of(routerRecord.mUserRecord.mUserId), routerRecord.mPackageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void selectRouteOnHandler(long uniqueRequestId, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
            if (!checkArgumentsForSessionControl(routerRecord, uniqueSessionId, route, "selecting")) {
                return;
            }
            java.lang.String providerId = route.getProviderId();
            com.android.server.media.MediaRoute2Provider provider = findProvider(providerId);
            if (provider == null) {
                return;
            }
            provider.selectRoute(uniqueRequestId, android.media.MediaRouter2Utils.getOriginalId(uniqueSessionId), route.getOriginalId());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void deselectRouteOnHandler(long uniqueRequestId, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route) {
            if (!checkArgumentsForSessionControl(routerRecord, uniqueSessionId, route, "deselecting")) {
                return;
            }
            java.lang.String providerId = route.getProviderId();
            com.android.server.media.MediaRoute2Provider provider = findProvider(providerId);
            if (provider == null) {
                return;
            }
            provider.deselectRoute(uniqueRequestId, android.media.MediaRouter2Utils.getOriginalId(uniqueSessionId), route.getOriginalId());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void transferToRouteOnHandler(long uniqueRequestId, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route, int transferReason) {
            if (!checkArgumentsForSessionControl(routerRecord, uniqueSessionId, route, "transferring to")) {
                return;
            }
            java.lang.String providerId = route.getProviderId();
            com.android.server.media.MediaRoute2Provider provider = findProvider(providerId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring transferToRoute due to lack of matching provider for target: " + route);
            } else {
                provider.transferToRoute(uniqueRequestId, transferInitiatorUserHandle, transferInitiatorPackageName, android.media.MediaRouter2Utils.getOriginalId(uniqueSessionId), route.getOriginalId(), transferReason);
            }
        }

        private boolean checkArgumentsForSessionControl(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, java.lang.String uniqueSessionId, android.media.MediaRoute2Info route, java.lang.String description) {
            java.lang.String providerId = route.getProviderId();
            com.android.server.media.MediaRoute2Provider provider = findProvider(providerId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring " + description + " route since no provider found for given route=" + route);
                return false;
            }
            if (android.text.TextUtils.equals(android.media.MediaRouter2Utils.getProviderId(uniqueSessionId), this.mSystemProvider.getUniqueId())) {
                return true;
            }
            com.android.server.media.MediaRouter2ServiceImpl.RouterRecord matchingRecord = this.mSessionToRouterMap.get(uniqueSessionId);
            if (matchingRecord != routerRecord) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring " + description + " route from non-matching router. routerRecordPackageName=" + getPackageNameFromNullableRecord(routerRecord) + " matchingRecordPackageName=" + getPackageNameFromNullableRecord(matchingRecord) + " route=" + route);
                return false;
            }
            java.lang.String sessionId = android.media.MediaRouter2Utils.getOriginalId(uniqueSessionId);
            if (sessionId != null) {
                return true;
            }
            android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to get original session id from unique session id. uniqueSessionId=" + uniqueSessionId);
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRouteVolumeOnHandler(long uniqueRequestId, android.media.MediaRoute2Info route, int volume) {
            com.android.server.media.MediaRoute2Provider provider = findProvider(route.getProviderId());
            if (provider == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "setRouteVolumeOnHandler: Couldn't find provider for route=" + route);
            } else {
                provider.setRouteVolume(uniqueRequestId, route.getOriginalId(), volume);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSessionVolumeOnHandler(long uniqueRequestId, java.lang.String uniqueSessionId, int volume) {
            com.android.server.media.MediaRoute2Provider provider = findProvider(android.media.MediaRouter2Utils.getProviderId(uniqueSessionId));
            if (provider == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "setSessionVolumeOnHandler: Couldn't find provider for session id=" + uniqueSessionId);
            } else {
                provider.setSessionVolume(uniqueRequestId, android.media.MediaRouter2Utils.getOriginalId(uniqueSessionId), volume);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void releaseSessionOnHandler(long uniqueRequestId, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, java.lang.String uniqueSessionId) {
            com.android.server.media.MediaRouter2ServiceImpl.RouterRecord matchingRecord = this.mSessionToRouterMap.get(uniqueSessionId);
            if (matchingRecord != routerRecord) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring releasing session from non-matching router. routerRecordPackageName=" + getPackageNameFromNullableRecord(routerRecord) + " matchingRecordPackageName=" + getPackageNameFromNullableRecord(matchingRecord) + " uniqueSessionId=" + uniqueSessionId);
                return;
            }
            java.lang.String providerId = android.media.MediaRouter2Utils.getProviderId(uniqueSessionId);
            if (providerId == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring releasing session with invalid unique session ID. uniqueSessionId=" + uniqueSessionId);
                return;
            }
            java.lang.String sessionId = android.media.MediaRouter2Utils.getOriginalId(uniqueSessionId);
            if (sessionId == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring releasing session with invalid unique session ID. uniqueSessionId=" + uniqueSessionId + " providerId=" + providerId);
                return;
            }
            com.android.server.media.MediaRoute2Provider provider = findProvider(providerId);
            if (provider == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring releasing session since no provider found for given providerId=" + providerId);
            } else {
                provider.releaseSession(uniqueRequestId, sessionId);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSessionCreatedOnHandler(com.android.server.media.MediaRoute2Provider provider, long uniqueRequestId, android.media.RoutingSessionInfo sessionInfo) {
            long managerRequestId;
            com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest matchingRequest = null;
            java.util.Iterator<com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest> it = this.mSessionCreationRequests.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest request = it.next();
                if (request.mUniqueRequestId == uniqueRequestId && android.text.TextUtils.equals(request.mRoute.getProviderId(), provider.getUniqueId())) {
                    matchingRequest = request;
                    break;
                }
            }
            if (matchingRequest == null) {
                managerRequestId = 0;
            } else {
                managerRequestId = matchingRequest.mManagerRequestId;
            }
            notifySessionCreatedToManagers(managerRequestId, sessionInfo);
            if (matchingRequest == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Ignoring session creation result for unknown request. uniqueRequestId=" + uniqueRequestId + ", sessionInfo=" + sessionInfo);
                return;
            }
            this.mSessionCreationRequests.remove(matchingRequest);
            com.android.server.media.MediaRoute2Provider oldProvider = findProvider(matchingRequest.mOldSession.getProviderId());
            if (oldProvider == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "onSessionCreatedOnHandler: Can't find provider for an old session. session=" + matchingRequest.mOldSession);
            } else {
                oldProvider.prepareReleaseSession(matchingRequest.mOldSession.getId());
            }
            this.mSessionToRouterMap.put(sessionInfo.getId(), matchingRequest.mRouterRecord);
            if (sessionInfo.isSystemSession() && !matchingRequest.mRouterRecord.hasSystemRoutingPermission()) {
                sessionInfo = this.mSystemProvider.getDefaultSessionInfo();
            }
            matchingRequest.mRouterRecord.notifySessionCreated(com.android.server.media.MediaRouter2ServiceImpl.toOriginalRequestId(uniqueRequestId), sessionInfo);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSessionInfoChangedOnHandler(com.android.server.media.MediaRoute2Provider provider, android.media.RoutingSessionInfo sessionInfo) {
            java.util.List<android.media.IMediaRouter2Manager> managers = getManagers();
            notifySessionUpdatedToManagers(managers, sessionInfo);
            if (provider == this.mSystemProvider) {
                if (this.mServiceRef.get() == null) {
                    return;
                }
                notifySessionInfoChangedToRouters(getRouterRecords(true), sessionInfo);
                notifySessionInfoChangedToRouters(getRouterRecords(false), this.mSystemProvider.getDefaultSessionInfo());
                return;
            }
            com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mSessionToRouterMap.get(sessionInfo.getId());
            if (routerRecord == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "onSessionInfoChangedOnHandler: No matching router found for session=" + sessionInfo);
            } else {
                notifySessionInfoChangedToRouters(java.util.Arrays.asList(routerRecord), sessionInfo);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSessionReleasedOnHandler(com.android.server.media.MediaRoute2Provider provider, android.media.RoutingSessionInfo sessionInfo) {
            java.util.List<android.media.IMediaRouter2Manager> managers = getManagers();
            notifySessionReleasedToManagers(managers, sessionInfo);
            com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord = this.mSessionToRouterMap.get(sessionInfo.getId());
            if (routerRecord == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "onSessionReleasedOnHandler: No matching router found for session=" + sessionInfo);
            } else {
                notifySessionReleasedToRouter(routerRecord, sessionInfo);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onRequestFailedOnHandler(com.android.server.media.MediaRoute2Provider provider, long uniqueRequestId, int reason) {
            if (handleSessionCreationRequestFailed(provider, uniqueRequestId, reason)) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, android.text.TextUtils.formatSimple("onRequestFailedOnHandler | Finished handling session creation request failed for provider: %s, uniqueRequestId: %d, reason: %d", new java.lang.Object[]{provider.getUniqueId(), java.lang.Long.valueOf(uniqueRequestId), java.lang.Integer.valueOf(reason)}));
                return;
            }
            int requesterId = com.android.server.media.MediaRouter2ServiceImpl.toRequesterId(uniqueRequestId);
            com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager = findManagerWithId(requesterId);
            if (manager != null) {
                notifyRequestFailedToManager(manager.mManager, com.android.server.media.MediaRouter2ServiceImpl.toOriginalRequestId(uniqueRequestId), reason);
            }
        }

        private boolean handleSessionCreationRequestFailed(com.android.server.media.MediaRoute2Provider provider, long uniqueRequestId, int reason) {
            com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest matchingRequest = null;
            java.util.Iterator<com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest> it = this.mSessionCreationRequests.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.media.MediaRouter2ServiceImpl.SessionCreationRequest request = it.next();
                if (request.mUniqueRequestId == uniqueRequestId && android.text.TextUtils.equals(request.mRoute.getProviderId(), provider.getUniqueId())) {
                    matchingRequest = request;
                    break;
                }
            }
            if (matchingRequest == null) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, android.text.TextUtils.formatSimple("handleSessionCreationRequestFailed | No matching request found for provider: %s, uniqueRequestId: %d, reason: %d", new java.lang.Object[]{provider.getUniqueId(), java.lang.Long.valueOf(uniqueRequestId), java.lang.Integer.valueOf(reason)}));
                return false;
            }
            this.mSessionCreationRequests.remove(matchingRequest);
            if (matchingRequest.mManagerRequestId == 0) {
                notifySessionCreationFailedToRouter(matchingRequest.mRouterRecord, com.android.server.media.MediaRouter2ServiceImpl.toOriginalRequestId(uniqueRequestId));
                return true;
            }
            int requesterId = com.android.server.media.MediaRouter2ServiceImpl.toRequesterId(matchingRequest.mManagerRequestId);
            com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager = findManagerWithId(requesterId);
            if (manager != null) {
                notifyRequestFailedToManager(manager.mManager, com.android.server.media.MediaRouter2ServiceImpl.toOriginalRequestId(matchingRequest.mManagerRequestId), reason);
                return true;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionCreationFailedToRouter(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, int requestId) {
            try {
                routerRecord.mRouter.notifySessionCreated(requestId, (android.media.RoutingSessionInfo) null);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify router of the session creation failure. Router probably died.", ex);
            }
        }

        private void notifySessionReleasedToRouter(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, android.media.RoutingSessionInfo sessionInfo) {
            try {
                routerRecord.mRouter.notifySessionReleased(sessionInfo);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify router of the session release. Router probably died.", ex);
            }
        }

        private java.util.List<android.media.IMediaRouter2Manager> getManagers() {
            java.util.List<android.media.IMediaRouter2Manager> managers = new java.util.ArrayList<>();
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            if (service == null) {
                return managers;
            }
            synchronized (service.mLock) {
                for (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord : this.mUserRecord.mManagerRecords) {
                    managers.add(managerRecord.mManager);
                }
            }
            return managers;
        }

        private java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> getRouterRecords() {
            java.util.ArrayList arrayList;
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            if (service == null) {
                return java.util.Collections.emptyList();
            }
            synchronized (service.mLock) {
                arrayList = new java.util.ArrayList(this.mUserRecord.mRouterRecords);
            }
            return arrayList;
        }

        private java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> getRouterRecords(boolean hasSystemRoutingPermission) {
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> routerRecords = new java.util.ArrayList<>();
            if (service == null) {
                return routerRecords;
            }
            synchronized (service.mLock) {
                for (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord : this.mUserRecord.mRouterRecords) {
                    if (hasSystemRoutingPermission == routerRecord.hasSystemRoutingPermission()) {
                        routerRecords.add(routerRecord);
                    }
                }
            }
            return routerRecords;
        }

        private java.util.List<com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord> getManagerRecords() {
            java.util.ArrayList arrayList;
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            if (service == null) {
                return java.util.Collections.emptyList();
            }
            synchronized (service.mLock) {
                arrayList = new java.util.ArrayList(this.mUserRecord.mManagerRecords);
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyRouterRegistered(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord) {
            android.media.RoutingSessionInfo currentSystemSessionInfo;
            java.util.List<android.media.MediaRoute2Info> currentRoutes = new java.util.ArrayList<>();
            android.media.MediaRoute2ProviderInfo systemProviderInfo = null;
            for (android.media.MediaRoute2ProviderInfo providerInfo : this.mLastProviderInfos) {
                if (android.text.TextUtils.equals(providerInfo.getUniqueId(), this.mSystemProvider.getUniqueId())) {
                    systemProviderInfo = providerInfo;
                } else {
                    currentRoutes.addAll(providerInfo.getRoutes());
                }
            }
            if (routerRecord.hasSystemRoutingPermission()) {
                if (systemProviderInfo != null) {
                    currentRoutes.addAll(systemProviderInfo.getRoutes());
                } else {
                    android.util.Slog.wtf(com.android.server.media.MediaRouter2ServiceImpl.TAG, "System route provider not found.");
                }
                currentSystemSessionInfo = this.mSystemProvider.getSessionInfos().get(0);
            } else {
                currentRoutes.add(this.mSystemProvider.getDefaultRoute());
                currentSystemSessionInfo = this.mSystemProvider.getDefaultSessionInfo();
            }
            if (!currentRoutes.isEmpty()) {
                routerRecord.notifyRegistered(currentRoutes, currentSystemSessionInfo);
            }
        }

        private static void notifyRoutesUpdatedToRouterRecords(java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> routerRecords, java.util.List<android.media.MediaRoute2Info> routes) {
            for (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord : routerRecords) {
                routerRecord.notifyRoutesUpdated(routes);
            }
        }

        private void notifySessionInfoChangedToRouters(java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> routerRecords, android.media.RoutingSessionInfo sessionInfo) {
            for (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord : routerRecords) {
                routerRecord.notifySessionInfoChanged(sessionInfo);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyInitialRoutesToManager(android.media.IMediaRouter2Manager manager) {
            if (this.mLastNotifiedRoutesToPrivilegedRouters.isEmpty()) {
                return;
            }
            try {
                manager.notifyRoutesUpdated(new java.util.ArrayList(this.mLastNotifiedRoutesToPrivilegedRouters.values()));
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify all routes. Manager probably died.", ex);
            }
        }

        private void notifyRoutesUpdatedToManagers(java.util.List<android.media.IMediaRouter2Manager> managers, java.util.List<android.media.MediaRoute2Info> routes) {
            for (android.media.IMediaRouter2Manager manager : managers) {
                try {
                    manager.notifyRoutesUpdated(routes);
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify routes changed. Manager probably died.", ex);
                }
            }
        }

        private void notifySessionCreatedToManagers(long managerRequestId, android.media.RoutingSessionInfo session) {
            int requesterId = com.android.server.media.MediaRouter2ServiceImpl.toRequesterId(managerRequestId);
            int originalRequestId = com.android.server.media.MediaRouter2ServiceImpl.toOriginalRequestId(managerRequestId);
            for (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager : getManagerRecords()) {
                try {
                    manager.mManager.notifySessionCreated(manager.mManagerId == requesterId ? originalRequestId : 0, session);
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "notifySessionCreatedToManagers: Failed to notify. Manager probably died.", ex);
                }
            }
        }

        private void notifySessionUpdatedToManagers(java.util.List<android.media.IMediaRouter2Manager> managers, android.media.RoutingSessionInfo sessionInfo) {
            for (android.media.IMediaRouter2Manager manager : managers) {
                try {
                    manager.notifySessionUpdated(sessionInfo);
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "notifySessionUpdatedToManagers: Failed to notify. Manager probably died.", ex);
                }
            }
        }

        private void notifySessionReleasedToManagers(java.util.List<android.media.IMediaRouter2Manager> managers, android.media.RoutingSessionInfo sessionInfo) {
            for (android.media.IMediaRouter2Manager manager : managers) {
                try {
                    manager.notifySessionReleased(sessionInfo);
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "notifySessionReleasedToManagers: Failed to notify. Manager probably died.", ex);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyDiscoveryPreferenceChangedToManager(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, android.media.IMediaRouter2Manager manager) {
            try {
                manager.notifyDiscoveryPreferenceChanged(routerRecord.mPackageName, routerRecord.mDiscoveryPreference);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify preferred features changed. Manager probably died.", ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyDiscoveryPreferenceChangedToManagers(java.lang.String routerPackageName, android.media.RouteDiscoveryPreference discoveryPreference) {
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            if (service == null) {
                return;
            }
            java.util.List<android.media.IMediaRouter2Manager> managers = new java.util.ArrayList<>();
            synchronized (service.mLock) {
                for (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord : this.mUserRecord.mManagerRecords) {
                    managers.add(managerRecord.mManager);
                }
            }
            for (android.media.IMediaRouter2Manager manager : managers) {
                try {
                    manager.notifyDiscoveryPreferenceChanged(routerPackageName, discoveryPreference);
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify preferred features changed. Manager probably died.", ex);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyRouteListingPreferenceChangeToManagers(java.lang.String routerPackageName, android.media.RouteListingPreference routeListingPreference) {
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            if (service == null) {
                return;
            }
            java.util.List<android.media.IMediaRouter2Manager> managers = new java.util.ArrayList<>();
            synchronized (service.mLock) {
                for (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord managerRecord : this.mUserRecord.mManagerRecords) {
                    managers.add(managerRecord.mManager);
                }
            }
            for (android.media.IMediaRouter2Manager manager : managers) {
                try {
                    manager.notifyRouteListingPreferenceChange(routerPackageName, routeListingPreference);
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify preferred features changed. Manager probably died.", ex);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyRequestFailedToManager(android.media.IMediaRouter2Manager manager, int requestId, int reason) {
            try {
                manager.notifyRequestFailed(requestId, reason);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.media.MediaRouter2ServiceImpl.TAG, "Failed to notify manager of the request failure. Manager probably died.", ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDiscoveryPreferenceOnHandler() {
            java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> activeRouterRecords;
            com.android.server.media.MediaRouter2ServiceImpl service = this.mServiceRef.get();
            if (service == null) {
                return;
            }
            java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> allRouterRecords = getRouterRecords();
            boolean areManagersScanning = areManagersScanning(service, getManagerRecords());
            if (areManagersScanning) {
                activeRouterRecords = allRouterRecords;
            } else {
                activeRouterRecords = getIndividuallyActiveRouters(service, allRouterRecords);
            }
            updateManagerScanningForProviders(areManagersScanning);
            java.util.Set<java.lang.String> activelyScanningPackages = new java.util.HashSet<>();
            android.media.RouteDiscoveryPreference newPreference = buildCompositeDiscoveryPreference(activeRouterRecords, areManagersScanning, activelyScanningPackages);
            android.util.Slog.i(com.android.server.media.MediaRouter2ServiceImpl.TAG, android.text.TextUtils.formatSimple("Updating composite discovery preference | preference: %s, active routers: %s", new java.lang.Object[]{newPreference, activelyScanningPackages}));
            if (updateScanningOnUserRecord(service, activelyScanningPackages, newPreference)) {
                updateDiscoveryPreferenceForProviders(activelyScanningPackages);
            }
        }

        private void updateDiscoveryPreferenceForProviders(java.util.Set<java.lang.String> activelyScanningPackages) {
            for (com.android.server.media.MediaRoute2Provider provider : this.mRouteProviders) {
                provider.updateDiscoveryPreference(activelyScanningPackages, this.mUserRecord.mCompositeDiscoveryPreference);
            }
        }

        private boolean updateScanningOnUserRecord(com.android.server.media.MediaRouter2ServiceImpl service, java.util.Set<java.lang.String> activelyScanningPackages, android.media.RouteDiscoveryPreference newPreference) {
            synchronized (service.mLock) {
                if (newPreference.equals(this.mUserRecord.mCompositeDiscoveryPreference) && activelyScanningPackages.equals(this.mUserRecord.mActivelyScanningPackages)) {
                    return false;
                }
                this.mUserRecord.mCompositeDiscoveryPreference = newPreference;
                this.mUserRecord.mActivelyScanningPackages = activelyScanningPackages;
                return true;
            }
        }

        private static android.media.RouteDiscoveryPreference buildCompositeDiscoveryPreference(java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> activeRouterRecords, boolean shouldForceActiveScan, java.util.Set<java.lang.String> activelyScanningPackages) {
            boolean isRouterRecordActivelyScanning;
            java.util.Set<java.lang.String> preferredFeatures = new java.util.HashSet<>();
            boolean activeScan = false;
            java.util.Iterator<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> it = activeRouterRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.media.MediaRouter2ServiceImpl.RouterRecord activeRouterRecord = it.next();
                android.media.RouteDiscoveryPreference preference = activeRouterRecord.mDiscoveryPreference;
                preferredFeatures.addAll(preference.getPreferredFeatures());
                if (com.android.media.flags.Flags.enablePreventionOfManagerScansWhenNoAppsScan()) {
                    if ((activeRouterRecord.isActivelyScanning() || shouldForceActiveScan) && !preference.getPreferredFeatures().isEmpty()) {
                        isRouterRecordActivelyScanning = true;
                    }
                } else {
                    isRouterRecordActivelyScanning = activeRouterRecord.isActivelyScanning();
                }
                if (isRouterRecordActivelyScanning) {
                    activeScan = true;
                    activelyScanningPackages.add(activeRouterRecord.mPackageName);
                }
            }
            java.util.List listCopyOf = java.util.List.copyOf(preferredFeatures);
            isRouterRecordActivelyScanning = activeScan || shouldForceActiveScan;
            return new android.media.RouteDiscoveryPreference.Builder(listCopyOf, isRouterRecordActivelyScanning).build();
        }

        private void updateManagerScanningForProviders(boolean isManagerScanning) {
            for (com.android.server.media.MediaRoute2Provider provider : this.mRouteProviders) {
                if (provider instanceof com.android.server.media.MediaRoute2ProviderServiceProxy) {
                    ((com.android.server.media.MediaRoute2ProviderServiceProxy) provider).setManagerScanning(isManagerScanning);
                }
            }
        }

        private static java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> getIndividuallyActiveRouters(final com.android.server.media.MediaRouter2ServiceImpl service, java.util.List<com.android.server.media.MediaRouter2ServiceImpl.RouterRecord> allRouterRecords) {
            if (!service.mPowerManager.isInteractive() && !com.android.media.flags.Flags.enableScreenOffScanning()) {
                return java.util.Collections.emptyList();
            }
            return (java.util.List) allRouterRecords.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda11
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.media.MediaRouter2ServiceImpl.UserHandler.lambda$getIndividuallyActiveRouters$3(service, (com.android.server.media.MediaRouter2ServiceImpl.RouterRecord) obj);
                }
            }).collect(java.util.stream.Collectors.toList());
        }

        static /* synthetic */ boolean lambda$getIndividuallyActiveRouters$3(com.android.server.media.MediaRouter2ServiceImpl service, com.android.server.media.MediaRouter2ServiceImpl.RouterRecord record) {
            return isPackageImportanceSufficientForScanning(service, record.mPackageName) || record.mScanningState == 2;
        }

        private static boolean areManagersScanning(final com.android.server.media.MediaRouter2ServiceImpl service, java.util.List<com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord> managerRecords) {
            if (!service.mPowerManager.isInteractive() && !com.android.media.flags.Flags.enableScreenOffScanning()) {
                return false;
            }
            return managerRecords.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.media.MediaRouter2ServiceImpl.UserHandler.lambda$areManagersScanning$4(service, (com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord) obj);
                }
            });
        }

        static /* synthetic */ boolean lambda$areManagersScanning$4(com.android.server.media.MediaRouter2ServiceImpl service, com.android.server.media.MediaRouter2ServiceImpl.ManagerRecord manager) {
            return (manager.mScanningState == 1 && isPackageImportanceSufficientForScanning(service, manager.mOwnerPackageName)) || manager.mScanningState == 2;
        }

        private static boolean isPackageImportanceSufficientForScanning(com.android.server.media.MediaRouter2ServiceImpl service, java.lang.String packageName) {
            return service.mActivityManager.getPackageImportance(packageName) <= 100;
        }

        private com.android.server.media.MediaRoute2Provider findProvider(java.lang.String providerId) {
            for (com.android.server.media.MediaRoute2Provider provider : this.mRouteProviders) {
                if (android.text.TextUtils.equals(provider.getUniqueId(), providerId)) {
                    return provider;
                }
            }
            return null;
        }
    }

    static final class SessionCreationRequest {
        public final long mManagerRequestId;
        public final android.media.RoutingSessionInfo mOldSession;
        public final android.media.MediaRoute2Info mRoute;
        public final com.android.server.media.MediaRouter2ServiceImpl.RouterRecord mRouterRecord;
        public final long mUniqueRequestId;

        SessionCreationRequest(com.android.server.media.MediaRouter2ServiceImpl.RouterRecord routerRecord, long uniqueRequestId, long managerRequestId, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route) {
            this.mRouterRecord = routerRecord;
            this.mUniqueRequestId = uniqueRequestId;
            this.mManagerRequestId = managerRequestId;
            this.mOldSession = oldSession;
            this.mRoute = route;
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "SessionCreationRequest");
            java.lang.String indent = prefix + "  ";
            pw.println(indent + "mUniqueRequestId=" + this.mUniqueRequestId);
            pw.println(indent + "mManagerRequestId=" + this.mManagerRequestId);
            this.mOldSession.dump(pw, indent);
            this.mRoute.dump(pw, prefix);
        }
    }
}
