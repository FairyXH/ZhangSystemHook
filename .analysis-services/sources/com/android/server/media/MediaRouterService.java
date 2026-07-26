package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public final class MediaRouterService extends android.media.IMediaRouterService.Stub implements com.android.server.Watchdog.Monitor {
    private static final long CONNECTED_TIMEOUT = 60000;
    private static final long CONNECTING_TIMEOUT = 5000;
    private static final java.lang.String WORKER_THREAD_NAME = "MediaRouterServiceThread";
    android.bluetooth.BluetoothDevice mActiveBluetoothDevice;
    private final com.android.server.media.AudioPlayerStateMonitor mAudioPlayerStateMonitor;
    private final android.media.IAudioService mAudioService;
    private final java.lang.String mBluetoothA2dpRouteId;
    private final android.content.Context mContext;
    private final java.lang.String mDefaultAudioRouteId;
    private final android.os.Handler mHandler;
    private final android.os.Looper mLooper;
    private final com.android.server.media.MediaRouter2ServiceImpl mService2;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private static final java.lang.String TAG = "MediaRouterService";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.media.MediaRouterService.UserRecord> mUserRecords = new android.util.SparseArray<>();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.media.MediaRouterService.ClientRecord> mAllClientRecords = new android.util.ArrayMap<>();
    private int mCurrentActiveUserId = -1;
    private final android.util.IntArray mActivePlayerMinPriorityQueue = new android.util.IntArray();
    private final android.util.IntArray mActivePlayerUidMinPriorityQueue = new android.util.IntArray();
    private final android.content.BroadcastReceiver mReceiver = new com.android.server.media.MediaRouterService.MediaRouterServiceBroadcastReceiver();
    int mAudioRouteMainType = 0;
    boolean mGlobalBluetoothA2dpOn = false;

    /* JADX WARN: Multi-variable type inference failed */
    public MediaRouterService(android.content.Context context) {
        if (com.android.media.flags.Flags.enableMr2ServiceNonMainBgThread()) {
            android.os.HandlerThread handlerThread = new android.os.HandlerThread(WORKER_THREAD_NAME);
            handlerThread.start();
            this.mLooper = handlerThread.getLooper();
        } else {
            this.mLooper = android.os.Looper.myLooper();
        }
        this.mHandler = new android.os.Handler(this.mLooper);
        this.mService2 = new com.android.server.media.MediaRouter2ServiceImpl(context, this.mLooper);
        this.mContext = context;
        com.android.server.Watchdog.getInstance().addMonitor(this);
        android.content.res.Resources resources = context.getResources();
        this.mDefaultAudioRouteId = resources.getString(android.R.string.default_browser);
        this.mBluetoothA2dpRouteId = resources.getString(android.R.string.bugreport_option_full_summary);
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mAudioService = android.media.IAudioService.Stub.asInterface(android.os.ServiceManager.getService("audio"));
        this.mAudioPlayerStateMonitor = com.android.server.media.AudioPlayerStateMonitor.getInstance(context);
        java.lang.Object[] objArr = 0;
        this.mAudioPlayerStateMonitor.registerListener(new com.android.server.media.MediaRouterService.AudioPlayerActiveStateChangedListenerImpl(), this.mHandler);
        try {
            this.mAudioService.startWatchingRoutes(new com.android.server.media.MediaRouterService.AudioRoutesObserverImpl());
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException in the audio service.");
        }
        context.registerReceiverAsUser(this.mReceiver, android.os.UserHandle.ALL, new android.content.IntentFilter("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED"), null, null);
    }

    public void systemRunning() throws android.os.RemoteException {
        android.app.ActivityManager.getService().registerUserSwitchObserver(new android.app.UserSwitchObserver() { // from class: com.android.server.media.MediaRouterService.1
            public void onUserSwitchComplete(int newUserId) {
                com.android.server.media.MediaRouterService.this.updateRunningUserAndProfiles(newUserId);
            }
        }, TAG);
        updateRunningUserAndProfiles(android.app.ActivityManager.getCurrentUser());
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    public void registerClientAsUser(android.media.IMediaRouterClient client, java.lang.String packageName, int userId) {
        int uid = android.os.Binder.getCallingUid();
        if (!validatePackageName(uid, packageName)) {
            throw new java.lang.SecurityException("packageName must match the calling uid");
        }
        int pid = android.os.Binder.getCallingPid();
        int resolvedUserId = android.app.ActivityManager.handleIncomingUser(pid, uid, userId, false, true, "registerClientAsUser", packageName);
        boolean trusted = this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY") == 0;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerClientLocked(client, uid, pid, packageName, resolvedUserId, trusted);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void registerClientGroupId(android.media.IMediaRouterClient client, java.lang.String groupId) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY") != 0) {
            android.util.Log.w(TAG, "Ignoring client group request because the client doesn't have the CONFIGURE_WIFI_DISPLAY permission.");
            return;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerClientGroupIdLocked(client, groupId);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void unregisterClient(android.media.IMediaRouterClient client) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                unregisterClientLocked(client, false);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public android.media.MediaRouterClientState getState(android.media.IMediaRouterClient client) {
        android.media.MediaRouterClientState stateLocked;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                stateLocked = getStateLocked(client);
            }
            return stateLocked;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean isPlaybackActive(android.media.IMediaRouterClient client) {
        com.android.server.media.MediaRouterService.ClientRecord clientRecord;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                clientRecord = this.mAllClientRecords.get(client.asBinder());
            }
            if (clientRecord != null) {
                return this.mAudioPlayerStateMonitor.isPlaybackActive(clientRecord.mUid);
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setBluetoothA2dpOn(android.media.IMediaRouterClient client, boolean on) {
        if (client == null) {
            throw new java.lang.IllegalArgumentException("client must not be null");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mAudioService.setBluetoothA2dpOn(on);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "RemoteException while calling setBluetoothA2dpOn. on=" + on);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setDiscoveryRequest(android.media.IMediaRouterClient client, int routeTypes, boolean activeScan) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setDiscoveryRequestLocked(client, routeTypes, activeScan);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setSelectedRoute(android.media.IMediaRouterClient client, java.lang.String routeId, boolean explicit) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setSelectedRouteLocked(client, routeId, explicit);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void requestSetVolume(android.media.IMediaRouterClient client, java.lang.String routeId, int volume) {
        java.util.Objects.requireNonNull(routeId, "routeId must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestSetVolumeLocked(client, routeId, volume);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void requestUpdateVolume(android.media.IMediaRouterClient client, java.lang.String routeId, int direction) {
        java.util.Objects.requireNonNull(routeId, "routeId must not be null");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestUpdateVolumeLocked(client, routeId, direction);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            pw.println("MEDIA ROUTER SERVICE (dumpsys media_router)");
            pw.println();
            pw.println("Global state");
            pw.println("  mCurrentUserId=" + this.mCurrentActiveUserId);
            synchronized (this.mLock) {
                int count = this.mUserRecords.size();
                for (int i = 0; i < count; i++) {
                    com.android.server.media.MediaRouterService.UserRecord userRecord = this.mUserRecords.valueAt(i);
                    pw.println();
                    userRecord.dump(pw, "");
                }
            }
            pw.println();
            this.mService2.dump(pw, "");
        }
    }

    public java.util.List<android.media.MediaRoute2Info> getSystemRoutes(java.lang.String callerPackageName, boolean isProxyRouter) {
        if (!validatePackageName(android.os.Binder.getCallingUid(), callerPackageName)) {
            throw new java.lang.SecurityException("callerPackageName does not match calling uid.");
        }
        return this.mService2.getSystemRoutes(callerPackageName, isProxyRouter);
    }

    public android.media.RoutingSessionInfo getSystemSessionInfo() {
        return this.mService2.getSystemSessionInfo(null, null, false);
    }

    public boolean showMediaOutputSwitcherWithRouter2(java.lang.String packageName) {
        int uid = android.os.Binder.getCallingUid();
        if (!validatePackageName(uid, packageName)) {
            throw new java.lang.SecurityException("packageName must match the calling identity");
        }
        return this.mService2.showMediaOutputSwitcherWithRouter2(packageName);
    }

    public void registerRouter2(android.media.IMediaRouter2 router, java.lang.String packageName) {
        int uid = android.os.Binder.getCallingUid();
        if (!validatePackageName(uid, packageName)) {
            throw new java.lang.SecurityException("packageName must match the calling uid");
        }
        this.mService2.registerRouter2(router, packageName);
    }

    public void unregisterRouter2(android.media.IMediaRouter2 router) {
        this.mService2.unregisterRouter2(router);
    }

    public void updateScanningStateWithRouter2(android.media.IMediaRouter2 router, int scanningState) {
        this.mService2.updateScanningState(router, scanningState);
    }

    public void setDiscoveryRequestWithRouter2(android.media.IMediaRouter2 router, android.media.RouteDiscoveryPreference request) {
        this.mService2.setDiscoveryRequestWithRouter2(router, request);
    }

    public void setRouteListingPreference(android.media.IMediaRouter2 router, android.media.RouteListingPreference routeListingPreference) {
        this.mService2.setRouteListingPreference(router, routeListingPreference);
    }

    public void setRouteVolumeWithRouter2(android.media.IMediaRouter2 router, android.media.MediaRoute2Info route, int volume) {
        this.mService2.setRouteVolumeWithRouter2(router, route, volume);
    }

    public void requestCreateSessionWithRouter2(android.media.IMediaRouter2 router, int requestId, long managerRequestId, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.Bundle sessionHints) {
        this.mService2.requestCreateSessionWithRouter2(router, requestId, managerRequestId, oldSession, route, sessionHints);
    }

    public void selectRouteWithRouter2(android.media.IMediaRouter2 router, java.lang.String sessionId, android.media.MediaRoute2Info route) {
        this.mService2.selectRouteWithRouter2(router, sessionId, route);
    }

    public void deselectRouteWithRouter2(android.media.IMediaRouter2 router, java.lang.String sessionId, android.media.MediaRoute2Info route) {
        this.mService2.deselectRouteWithRouter2(router, sessionId, route);
    }

    public void transferToRouteWithRouter2(android.media.IMediaRouter2 router, java.lang.String sessionId, android.media.MediaRoute2Info route) {
        this.mService2.transferToRouteWithRouter2(router, sessionId, route);
    }

    public void setSessionVolumeWithRouter2(android.media.IMediaRouter2 router, java.lang.String sessionId, int volume) {
        this.mService2.setSessionVolumeWithRouter2(router, sessionId, volume);
    }

    public void releaseSessionWithRouter2(android.media.IMediaRouter2 router, java.lang.String sessionId) {
        this.mService2.releaseSessionWithRouter2(router, sessionId);
    }

    public java.util.List<android.media.RoutingSessionInfo> getRemoteSessions(android.media.IMediaRouter2Manager manager) {
        return this.mService2.getRemoteSessions(manager);
    }

    public android.media.RoutingSessionInfo getSystemSessionInfoForPackage(java.lang.String callerPackageName, java.lang.String targetPackageName) {
        int uid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
        if (!validatePackageName(uid, callerPackageName)) {
            throw new java.lang.SecurityException("callerPackageName does not match calling uid.");
        }
        boolean setDeviceRouteSelected = false;
        synchronized (this.mLock) {
            com.android.server.media.MediaRouterService.UserRecord userRecord = this.mUserRecords.get(userId);
            java.util.List<com.android.server.media.MediaRouterService.ClientRecord> userClientRecords = userRecord != null ? userRecord.mClientRecords : java.util.Collections.emptyList();
            java.util.Iterator<com.android.server.media.MediaRouterService.ClientRecord> it = userClientRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.media.MediaRouterService.ClientRecord clientRecord = it.next();
                if (android.text.TextUtils.equals(clientRecord.mPackageName, targetPackageName) && this.mDefaultAudioRouteId.equals(clientRecord.mSelectedRouteId)) {
                    setDeviceRouteSelected = true;
                    break;
                }
            }
        }
        return this.mService2.getSystemSessionInfo(callerPackageName, targetPackageName, setDeviceRouteSelected);
    }

    public void registerManager(android.media.IMediaRouter2Manager manager, java.lang.String callerPackageName) {
        int uid = android.os.Binder.getCallingUid();
        if (!validatePackageName(uid, callerPackageName)) {
            throw new java.lang.SecurityException("callerPackageName must match the calling uid");
        }
        this.mService2.registerManager(manager, callerPackageName);
    }

    public void registerProxyRouter(android.media.IMediaRouter2Manager manager, java.lang.String callerPackageName, java.lang.String targetPackageName, android.os.UserHandle targetUser) {
        int uid = android.os.Binder.getCallingUid();
        if (!validatePackageName(uid, callerPackageName)) {
            throw new java.lang.SecurityException("callerPackageName must match the calling uid");
        }
        this.mService2.registerProxyRouter(manager, callerPackageName, targetPackageName, targetUser);
    }

    public void unregisterManager(android.media.IMediaRouter2Manager manager) {
        this.mService2.unregisterManager(manager);
    }

    public void updateScanningState(android.media.IMediaRouter2Manager manager, int scanningState) {
        this.mService2.updateScanningState(manager, scanningState);
    }

    public void setRouteVolumeWithManager(android.media.IMediaRouter2Manager manager, int requestId, android.media.MediaRoute2Info route, int volume) {
        this.mService2.setRouteVolumeWithManager(manager, requestId, route, volume);
    }

    public void requestCreateSessionWithManager(android.media.IMediaRouter2Manager manager, int requestId, android.media.RoutingSessionInfo oldSession, android.media.MediaRoute2Info route, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
        this.mService2.requestCreateSessionWithManager(manager, requestId, oldSession, route, transferInitiatorUserHandle, transferInitiatorPackageName);
    }

    public void selectRouteWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String sessionId, android.media.MediaRoute2Info route) {
        this.mService2.selectRouteWithManager(manager, requestId, sessionId, route);
    }

    public void deselectRouteWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String sessionId, android.media.MediaRoute2Info route) {
        this.mService2.deselectRouteWithManager(manager, requestId, sessionId, route);
    }

    public void transferToRouteWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String sessionId, android.media.MediaRoute2Info route, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
        this.mService2.transferToRouteWithManager(manager, requestId, sessionId, route, transferInitiatorUserHandle, transferInitiatorPackageName);
    }

    public void setSessionVolumeWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String sessionId, int volume) {
        this.mService2.setSessionVolumeWithManager(manager, requestId, sessionId, volume);
    }

    public void releaseSessionWithManager(android.media.IMediaRouter2Manager manager, int requestId, java.lang.String sessionId) {
        this.mService2.releaseSessionWithManager(manager, requestId, sessionId);
    }

    public boolean showMediaOutputSwitcherWithProxyRouter(android.media.IMediaRouter2Manager proxyRouter) {
        return this.mService2.showMediaOutputSwitcherWithProxyRouter(proxyRouter);
    }

    void restoreBluetoothA2dp() {
        boolean a2dpOn;
        android.bluetooth.BluetoothDevice btDevice;
        try {
            synchronized (this.mLock) {
                a2dpOn = this.mGlobalBluetoothA2dpOn;
                btDevice = this.mActiveBluetoothDevice;
            }
            if (btDevice != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "restoreBluetoothA2dp(" + a2dpOn + ")");
                }
                this.mAudioService.setBluetoothA2dpOn(a2dpOn);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException while calling setBluetoothA2dpOn.");
        }
    }

    void restoreRoute(int uid) {
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = null;
        synchronized (this.mLock) {
            com.android.server.media.MediaRouterService.UserRecord userRecord = this.mUserRecords.get(android.os.UserHandle.getUserHandleForUid(uid).getIdentifier());
            if (userRecord != null && userRecord.mClientRecords != null) {
                java.util.Iterator<com.android.server.media.MediaRouterService.ClientRecord> it = userRecord.mClientRecords.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    com.android.server.media.MediaRouterService.ClientRecord cr = it.next();
                    if (validatePackageName(uid, cr.mPackageName)) {
                        clientRecord = cr;
                        break;
                    }
                }
            }
        }
        if (clientRecord != null) {
            try {
                clientRecord.mClient.onRestoreRoute();
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to call onRestoreRoute. Client probably died.");
                return;
            }
        }
        restoreBluetoothA2dp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRunningUserAndProfiles(int newActiveUserId) {
        synchronized (this.mLock) {
            if (this.mCurrentActiveUserId != newActiveUserId) {
                this.mCurrentActiveUserId = newActiveUserId;
                android.util.SparseArray<com.android.server.media.MediaRouterService.UserRecord> userRecords = this.mUserRecords.clone();
                for (int i = 0; i < userRecords.size(); i++) {
                    int userId = userRecords.keyAt(i);
                    com.android.server.media.MediaRouterService.UserRecord userRecord = userRecords.valueAt(i);
                    if (isUserActiveLocked(userId)) {
                        userRecord.mHandler.sendEmptyMessage(1);
                    } else {
                        userRecord.mHandler.sendEmptyMessage(2);
                        disposeUserIfNeededLocked(userRecord);
                    }
                }
            }
        }
        this.mService2.updateRunningUserAndProfiles(newActiveUserId);
    }

    void clientDied(com.android.server.media.MediaRouterService.ClientRecord clientRecord) {
        synchronized (this.mLock) {
            unregisterClientLocked(clientRecord.mClient, true);
        }
    }

    private void registerClientLocked(android.media.IMediaRouterClient client, int uid, int pid, java.lang.String packageName, int userId, boolean trusted) {
        boolean newUser;
        com.android.server.media.MediaRouterService.UserRecord userRecord;
        android.os.IBinder binder = client.asBinder();
        if (this.mAllClientRecords.get(binder) == null) {
            com.android.server.media.MediaRouterService.UserRecord userRecord2 = this.mUserRecords.get(userId);
            if (userRecord2 != null) {
                newUser = false;
                userRecord = userRecord2;
            } else {
                newUser = true;
                userRecord = new com.android.server.media.MediaRouterService.UserRecord(userId);
            }
            com.android.server.media.MediaRouterService.ClientRecord clientRecord = new com.android.server.media.MediaRouterService.ClientRecord(userRecord, client, uid, pid, packageName, trusted);
            try {
                binder.linkToDeath(clientRecord, 0);
                if (newUser) {
                    this.mUserRecords.put(userId, userRecord);
                    initializeUserLocked(userRecord);
                }
                userRecord.mClientRecords.add(clientRecord);
                this.mAllClientRecords.put(binder, clientRecord);
                initializeClientLocked(clientRecord);
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException("Media router client died prematurely.", ex);
            }
        }
    }

    private void registerClientGroupIdLocked(android.media.IMediaRouterClient client, java.lang.String groupId) {
        android.os.IBinder binder = client.asBinder();
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mAllClientRecords.get(binder);
        if (clientRecord == null) {
            android.util.Log.w(TAG, "Ignoring group id register request of a unregistered client.");
            return;
        }
        if (android.text.TextUtils.equals(clientRecord.mGroupId, groupId)) {
            return;
        }
        com.android.server.media.MediaRouterService.UserRecord userRecord = clientRecord.mUserRecord;
        if (clientRecord.mGroupId != null) {
            userRecord.removeFromGroup(clientRecord.mGroupId, clientRecord);
        }
        clientRecord.mGroupId = groupId;
        if (groupId != null) {
            userRecord.addToGroup(groupId, clientRecord);
            userRecord.mHandler.obtainMessage(10, groupId).sendToTarget();
        }
    }

    private void unregisterClientLocked(android.media.IMediaRouterClient client, boolean died) {
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mAllClientRecords.remove(client.asBinder());
        if (clientRecord != null) {
            com.android.server.media.MediaRouterService.UserRecord userRecord = clientRecord.mUserRecord;
            userRecord.mClientRecords.remove(clientRecord);
            if (clientRecord.mGroupId != null) {
                userRecord.removeFromGroup(clientRecord.mGroupId, clientRecord);
                clientRecord.mGroupId = null;
            }
            disposeClientLocked(clientRecord, died);
            disposeUserIfNeededLocked(userRecord);
        }
    }

    private android.media.MediaRouterClientState getStateLocked(android.media.IMediaRouterClient client) {
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mAllClientRecords.get(client.asBinder());
        if (clientRecord != null) {
            return clientRecord.getState();
        }
        return null;
    }

    private void setDiscoveryRequestLocked(android.media.IMediaRouterClient client, int routeTypes, boolean activeScan) {
        android.os.IBinder binder = client.asBinder();
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mAllClientRecords.get(binder);
        if (clientRecord != null) {
            if (!clientRecord.mTrusted) {
                routeTypes &= -5;
            }
            if (clientRecord.mRouteTypes != routeTypes || clientRecord.mActiveScan != activeScan) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, clientRecord + ": Set discovery request, routeTypes=0x" + java.lang.Integer.toHexString(routeTypes) + ", activeScan=" + activeScan);
                }
                clientRecord.mRouteTypes = routeTypes;
                clientRecord.mActiveScan = activeScan;
                clientRecord.mUserRecord.mHandler.sendEmptyMessage(3);
            }
        }
    }

    private void setSelectedRouteLocked(android.media.IMediaRouterClient client, java.lang.String routeId, boolean explicit) {
        com.android.server.media.MediaRouterService.ClientGroup group;
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mAllClientRecords.get(client.asBinder());
        if (clientRecord != null) {
            java.lang.String oldRouteId = (this.mDefaultAudioRouteId.equals(clientRecord.mSelectedRouteId) || this.mBluetoothA2dpRouteId.equals(clientRecord.mSelectedRouteId)) ? null : clientRecord.mSelectedRouteId;
            clientRecord.mSelectedRouteId = routeId;
            if (this.mDefaultAudioRouteId.equals(routeId) || this.mBluetoothA2dpRouteId.equals(routeId)) {
                routeId = null;
            }
            if (!java.util.Objects.equals(routeId, oldRouteId)) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, clientRecord + ": Set selected route, routeId=" + routeId + ", oldRouteId=" + oldRouteId + ", explicit=" + explicit);
                }
                if (explicit && clientRecord.mTrusted) {
                    if (oldRouteId != null) {
                        clientRecord.mUserRecord.mHandler.obtainMessage(5, oldRouteId).sendToTarget();
                    }
                    if (routeId != null) {
                        clientRecord.mUserRecord.mHandler.obtainMessage(4, routeId).sendToTarget();
                    }
                    if (clientRecord.mGroupId != null && (group = (com.android.server.media.MediaRouterService.ClientGroup) clientRecord.mUserRecord.mClientGroupMap.get(clientRecord.mGroupId)) != null) {
                        group.mSelectedRouteId = routeId;
                        clientRecord.mUserRecord.mHandler.obtainMessage(10, clientRecord.mGroupId).sendToTarget();
                    }
                }
            }
        }
    }

    private void requestSetVolumeLocked(android.media.IMediaRouterClient client, java.lang.String routeId, int volume) {
        android.os.IBinder binder = client.asBinder();
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mAllClientRecords.get(binder);
        if (clientRecord != null) {
            clientRecord.mUserRecord.mHandler.obtainMessage(6, volume, 0, routeId).sendToTarget();
        }
    }

    private void requestUpdateVolumeLocked(android.media.IMediaRouterClient client, java.lang.String routeId, int direction) {
        android.os.IBinder binder = client.asBinder();
        com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mAllClientRecords.get(binder);
        if (clientRecord != null) {
            clientRecord.mUserRecord.mHandler.obtainMessage(7, direction, 0, routeId).sendToTarget();
        }
    }

    private void initializeUserLocked(com.android.server.media.MediaRouterService.UserRecord userRecord) {
        if (DEBUG) {
            android.util.Slog.d(TAG, userRecord + ": Initialized");
        }
        if (isUserActiveLocked(userRecord.mUserId)) {
            userRecord.mHandler.sendEmptyMessage(1);
        }
    }

    private void disposeUserIfNeededLocked(com.android.server.media.MediaRouterService.UserRecord userRecord) {
        if (!isUserActiveLocked(userRecord.mUserId) && userRecord.mClientRecords.isEmpty()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, userRecord + ": Disposed");
            }
            this.mUserRecords.remove(userRecord.mUserId);
        }
    }

    private boolean isUserActiveLocked(int userId) {
        return this.mUserManagerInternal.getProfileParentId(userId) == this.mCurrentActiveUserId;
    }

    private void initializeClientLocked(com.android.server.media.MediaRouterService.ClientRecord clientRecord) {
        if (DEBUG) {
            android.util.Slog.d(TAG, clientRecord + ": Registered");
        }
    }

    private void disposeClientLocked(com.android.server.media.MediaRouterService.ClientRecord clientRecord, boolean died) {
        if (DEBUG) {
            if (died) {
                android.util.Slog.d(TAG, clientRecord + ": Died!");
            } else {
                android.util.Slog.d(TAG, clientRecord + ": Unregistered");
            }
        }
        if (clientRecord.mRouteTypes != 0 || clientRecord.mActiveScan) {
            clientRecord.mUserRecord.mHandler.sendEmptyMessage(3);
        }
        clientRecord.dispose();
    }

    private boolean validatePackageName(int uid, java.lang.String packageName) {
        java.lang.String[] packageNames;
        if (packageName != null && (packageNames = this.mContext.getPackageManager().getPackagesForUid(uid)) != null) {
            for (java.lang.String n : packageNames) {
                if (n.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    final class MediaRouterServiceBroadcastReceiver extends android.content.BroadcastReceiver {
        MediaRouterServiceBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getAction().equals("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED")) {
                android.bluetooth.BluetoothDevice btDevice = (android.bluetooth.BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", android.bluetooth.BluetoothDevice.class);
                synchronized (com.android.server.media.MediaRouterService.this.mLock) {
                    com.android.server.media.MediaRouterService.this.mActiveBluetoothDevice = btDevice;
                    com.android.server.media.MediaRouterService.this.mGlobalBluetoothA2dpOn = btDevice != null;
                }
            }
        }
    }

    final class ClientRecord implements android.os.IBinder.DeathRecipient {
        public boolean mActiveScan;
        public final android.media.IMediaRouterClient mClient;
        public java.util.List<java.lang.String> mControlCategories;
        public java.lang.String mGroupId;
        public final java.lang.String mPackageName;
        public final int mPid;
        public int mRouteTypes;
        public java.lang.String mSelectedRouteId;
        public final boolean mTrusted;
        public final int mUid;
        public final com.android.server.media.MediaRouterService.UserRecord mUserRecord;

        ClientRecord(com.android.server.media.MediaRouterService.UserRecord userRecord, android.media.IMediaRouterClient client, int uid, int pid, java.lang.String packageName, boolean trusted) {
            this.mUserRecord = userRecord;
            this.mClient = client;
            this.mUid = uid;
            this.mPid = pid;
            this.mPackageName = packageName;
            this.mTrusted = trusted;
        }

        public void dispose() {
            this.mClient.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.media.MediaRouterService.this.clientDied(this);
        }

        android.media.MediaRouterClientState getState() {
            if (this.mTrusted) {
                return this.mUserRecord.mRouterState;
            }
            return null;
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + this);
            java.lang.String indent = prefix + "  ";
            pw.println(indent + "mTrusted=" + this.mTrusted);
            pw.println(indent + "mRouteTypes=0x" + java.lang.Integer.toHexString(this.mRouteTypes));
            pw.println(indent + "mActiveScan=" + this.mActiveScan);
            pw.println(indent + "mSelectedRouteId=" + this.mSelectedRouteId);
        }

        public java.lang.String toString() {
            return "Client " + this.mPackageName + " (pid " + this.mPid + ")";
        }
    }

    final class ClientGroup {
        public final java.util.List<com.android.server.media.MediaRouterService.ClientRecord> mClientRecords = new java.util.ArrayList();
        public java.lang.String mSelectedRouteId;

        ClientGroup() {
        }
    }

    final class UserRecord {
        public final com.android.server.media.MediaRouterService.UserHandler mHandler;
        public android.media.MediaRouterClientState mRouterState;
        public final int mUserId;
        public final java.util.ArrayList<com.android.server.media.MediaRouterService.ClientRecord> mClientRecords = new java.util.ArrayList<>();
        private final android.util.ArrayMap<java.lang.String, com.android.server.media.MediaRouterService.ClientGroup> mClientGroupMap = new android.util.ArrayMap<>();

        public UserRecord(int userId) {
            this.mUserId = userId;
            this.mHandler = new com.android.server.media.MediaRouterService.UserHandler(this, com.android.server.media.MediaRouterService.this.mLooper);
        }

        public void dump(final java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + this);
            final java.lang.String indent = prefix + "  ";
            int clientCount = this.mClientRecords.size();
            if (clientCount != 0) {
                for (int i = 0; i < clientCount; i++) {
                    this.mClientRecords.get(i).dump(pw, indent);
                }
            } else {
                pw.println(indent + "<no clients>");
            }
            pw.println(indent + "State");
            pw.println(indent + "mRouterState=" + this.mRouterState);
            if (!this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.media.MediaRouterService.UserRecord.1
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.media.MediaRouterService.UserRecord.this.mHandler.dump(pw, indent);
                }
            }, 1000L)) {
                pw.println(indent + "<could not dump handler state>");
            }
        }

        public void addToGroup(java.lang.String groupId, com.android.server.media.MediaRouterService.ClientRecord clientRecord) {
            com.android.server.media.MediaRouterService.ClientGroup group = this.mClientGroupMap.get(groupId);
            if (group == null) {
                group = com.android.server.media.MediaRouterService.this.new ClientGroup();
                this.mClientGroupMap.put(groupId, group);
            }
            group.mClientRecords.add(clientRecord);
        }

        public void removeFromGroup(java.lang.String groupId, com.android.server.media.MediaRouterService.ClientRecord clientRecord) {
            com.android.server.media.MediaRouterService.ClientGroup group = this.mClientGroupMap.get(groupId);
            if (group != null) {
                group.mClientRecords.remove(clientRecord);
                if (group.mClientRecords.size() == 0) {
                    this.mClientGroupMap.remove(groupId);
                }
            }
        }

        public java.lang.String toString() {
            return "User " + this.mUserId;
        }
    }

    static final class UserHandler extends android.os.Handler implements com.android.server.media.RemoteDisplayProviderWatcher.Callback, com.android.server.media.RemoteDisplayProviderProxy.Callback {
        private static final int MSG_CONNECTION_TIMED_OUT = 9;
        private static final int MSG_NOTIFY_GROUP_ROUTE_SELECTED = 10;
        public static final int MSG_REQUEST_SET_VOLUME = 6;
        public static final int MSG_REQUEST_UPDATE_VOLUME = 7;
        public static final int MSG_SELECT_ROUTE = 4;
        public static final int MSG_START = 1;
        public static final int MSG_STOP = 2;
        public static final int MSG_UNSELECT_ROUTE = 5;
        private static final int MSG_UPDATE_CLIENT_STATE = 8;
        public static final int MSG_UPDATE_DISCOVERY_REQUEST = 3;
        private static final int PHASE_CONNECTED = 2;
        private static final int PHASE_CONNECTING = 1;
        private static final int PHASE_NOT_AVAILABLE = -1;
        private static final int PHASE_NOT_CONNECTED = 0;
        private static final int TIMEOUT_REASON_CONNECTION_LOST = 2;
        private static final int TIMEOUT_REASON_NOT_AVAILABLE = 1;
        private static final int TIMEOUT_REASON_WAITING_FOR_CONNECTED = 4;
        private static final int TIMEOUT_REASON_WAITING_FOR_CONNECTING = 3;
        private boolean mClientStateUpdateScheduled;
        private int mConnectionPhase;
        private int mConnectionTimeoutReason;
        private long mConnectionTimeoutStartTime;
        private int mDiscoveryMode;
        private final java.util.ArrayList<com.android.server.media.MediaRouterService.UserHandler.ProviderRecord> mProviderRecords;
        private boolean mRunning;
        private com.android.server.media.MediaRouterService.UserHandler.RouteRecord mSelectedRouteRecord;
        private final com.android.server.media.MediaRouterService mService;
        private final java.util.ArrayList<android.media.IMediaRouterClient> mTempClients;
        private final com.android.server.media.MediaRouterService.UserRecord mUserRecord;
        private final com.android.server.media.RemoteDisplayProviderWatcher mWatcher;

        private UserHandler(com.android.server.media.MediaRouterService service, com.android.server.media.MediaRouterService.UserRecord userRecord, android.os.Looper looper) {
            super(looper, null, true);
            this.mProviderRecords = new java.util.ArrayList<>();
            this.mTempClients = new java.util.ArrayList<>();
            this.mDiscoveryMode = 0;
            this.mConnectionPhase = -1;
            this.mService = service;
            this.mUserRecord = userRecord;
            this.mWatcher = new com.android.server.media.RemoteDisplayProviderWatcher(service.mContext, this, this, this.mUserRecord.mUserId);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    start();
                    break;
                case 2:
                    stop();
                    break;
                case 3:
                    updateDiscoveryRequest();
                    break;
                case 4:
                    selectRoute((java.lang.String) msg.obj);
                    break;
                case 5:
                    unselectRoute((java.lang.String) msg.obj);
                    break;
                case 6:
                    requestSetVolume((java.lang.String) msg.obj, msg.arg1);
                    break;
                case 7:
                    requestUpdateVolume((java.lang.String) msg.obj, msg.arg1);
                    break;
                case 8:
                    updateClientState();
                    break;
                case 9:
                    connectionTimedOut();
                    break;
                case 10:
                    notifyGroupRouteSelected((java.lang.String) msg.obj);
                    break;
            }
        }

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "Handler");
            java.lang.String indent = prefix + "  ";
            pw.println(indent + "mRunning=" + this.mRunning);
            pw.println(indent + "mDiscoveryMode=" + this.mDiscoveryMode);
            pw.println(indent + "mSelectedRouteRecord=" + this.mSelectedRouteRecord);
            pw.println(indent + "mConnectionPhase=" + this.mConnectionPhase);
            pw.println(indent + "mConnectionTimeoutReason=" + this.mConnectionTimeoutReason);
            pw.println(indent + "mConnectionTimeoutStartTime=" + (this.mConnectionTimeoutReason != 0 ? android.util.TimeUtils.formatUptime(this.mConnectionTimeoutStartTime) : "<n/a>"));
            this.mWatcher.dump(pw, prefix);
            int providerCount = this.mProviderRecords.size();
            if (providerCount != 0) {
                for (int i = 0; i < providerCount; i++) {
                    this.mProviderRecords.get(i).dump(pw, prefix);
                }
                return;
            }
            pw.println(indent + "<no providers>");
        }

        private void start() {
            if (!this.mRunning) {
                this.mRunning = true;
                this.mWatcher.start();
            }
        }

        private void stop() {
            if (this.mRunning) {
                this.mRunning = false;
                unselectSelectedRoute();
                this.mWatcher.stop();
            }
        }

        private void updateDiscoveryRequest() {
            int newDiscoveryMode;
            int routeTypes = 0;
            boolean activeScan = false;
            synchronized (this.mService.mLock) {
                int count = this.mUserRecord.mClientRecords.size();
                for (int i = 0; i < count; i++) {
                    com.android.server.media.MediaRouterService.ClientRecord clientRecord = this.mUserRecord.mClientRecords.get(i);
                    routeTypes |= clientRecord.mRouteTypes;
                    activeScan |= clientRecord.mActiveScan;
                }
            }
            if ((routeTypes & 4) != 0) {
                if (activeScan) {
                    newDiscoveryMode = 2;
                } else {
                    newDiscoveryMode = 1;
                }
            } else {
                newDiscoveryMode = 0;
            }
            if (this.mDiscoveryMode != newDiscoveryMode) {
                this.mDiscoveryMode = newDiscoveryMode;
                int count2 = this.mProviderRecords.size();
                for (int i2 = 0; i2 < count2; i2++) {
                    this.mProviderRecords.get(i2).getProvider().setDiscoveryMode(this.mDiscoveryMode);
                }
            }
        }

        private void selectRoute(java.lang.String routeId) {
            com.android.server.media.MediaRouterService.UserHandler.RouteRecord routeRecord;
            if (routeId != null) {
                if ((this.mSelectedRouteRecord == null || !routeId.equals(this.mSelectedRouteRecord.getUniqueId())) && (routeRecord = findRouteRecord(routeId)) != null) {
                    unselectSelectedRoute();
                    android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Selected route:" + routeRecord);
                    this.mSelectedRouteRecord = routeRecord;
                    checkSelectedRouteState();
                    routeRecord.getProvider().setSelectedDisplay(routeRecord.getDescriptorId());
                    scheduleUpdateClientState();
                }
            }
        }

        private void unselectRoute(java.lang.String routeId) {
            if (routeId != null && this.mSelectedRouteRecord != null && routeId.equals(this.mSelectedRouteRecord.getUniqueId())) {
                unselectSelectedRoute();
            }
        }

        private void unselectSelectedRoute() {
            if (this.mSelectedRouteRecord != null) {
                android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Unselected route:" + this.mSelectedRouteRecord);
                this.mSelectedRouteRecord.getProvider().setSelectedDisplay(null);
                this.mSelectedRouteRecord = null;
                checkSelectedRouteState();
                scheduleUpdateClientState();
            }
        }

        private void requestSetVolume(java.lang.String routeId, int volume) {
            if (this.mSelectedRouteRecord != null && routeId.equals(this.mSelectedRouteRecord.getUniqueId())) {
                this.mSelectedRouteRecord.getProvider().setDisplayVolume(volume);
            }
        }

        private void requestUpdateVolume(java.lang.String routeId, int direction) {
            if (this.mSelectedRouteRecord != null && routeId.equals(this.mSelectedRouteRecord.getUniqueId())) {
                this.mSelectedRouteRecord.getProvider().adjustDisplayVolume(direction);
            }
        }

        @Override // com.android.server.media.RemoteDisplayProviderWatcher.Callback
        public void addProvider(com.android.server.media.RemoteDisplayProviderProxy provider) {
            provider.setCallback(this);
            provider.setDiscoveryMode(this.mDiscoveryMode);
            provider.setSelectedDisplay(null);
            com.android.server.media.MediaRouterService.UserHandler.ProviderRecord providerRecord = new com.android.server.media.MediaRouterService.UserHandler.ProviderRecord(provider);
            this.mProviderRecords.add(providerRecord);
            providerRecord.updateDescriptor(provider.getDisplayState());
            scheduleUpdateClientState();
        }

        @Override // com.android.server.media.RemoteDisplayProviderWatcher.Callback
        public void removeProvider(com.android.server.media.RemoteDisplayProviderProxy provider) {
            int index = findProviderRecord(provider);
            if (index >= 0) {
                com.android.server.media.MediaRouterService.UserHandler.ProviderRecord providerRecord = this.mProviderRecords.remove(index);
                providerRecord.updateDescriptor(null);
                provider.setCallback(null);
                provider.setDiscoveryMode(0);
                checkSelectedRouteState();
                scheduleUpdateClientState();
            }
        }

        @Override // com.android.server.media.RemoteDisplayProviderProxy.Callback
        public void onDisplayStateChanged(com.android.server.media.RemoteDisplayProviderProxy provider, android.media.RemoteDisplayState state) {
            updateProvider(provider, state);
        }

        private void updateProvider(com.android.server.media.RemoteDisplayProviderProxy provider, android.media.RemoteDisplayState state) {
            int index = findProviderRecord(provider);
            if (index >= 0) {
                com.android.server.media.MediaRouterService.UserHandler.ProviderRecord providerRecord = this.mProviderRecords.get(index);
                if (providerRecord.updateDescriptor(state)) {
                    checkSelectedRouteState();
                    scheduleUpdateClientState();
                }
            }
        }

        private void checkSelectedRouteState() {
            if (this.mSelectedRouteRecord == null) {
                this.mConnectionPhase = -1;
                updateConnectionTimeout(0);
            }
            if (!this.mSelectedRouteRecord.isValid() || !this.mSelectedRouteRecord.isEnabled()) {
                updateConnectionTimeout(1);
                return;
            }
            int oldPhase = this.mConnectionPhase;
            this.mConnectionPhase = getConnectionPhase(this.mSelectedRouteRecord.getStatus());
            if (oldPhase >= 1 && this.mConnectionPhase < 1) {
                updateConnectionTimeout(2);
                return;
            }
            switch (this.mConnectionPhase) {
                case 0:
                    updateConnectionTimeout(3);
                    break;
                case 1:
                    if (oldPhase != 1) {
                        android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Connecting to route: " + this.mSelectedRouteRecord);
                    }
                    updateConnectionTimeout(4);
                    break;
                case 2:
                    if (oldPhase != 2) {
                        android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Connected to route: " + this.mSelectedRouteRecord);
                    }
                    updateConnectionTimeout(0);
                    break;
                default:
                    updateConnectionTimeout(1);
                    break;
            }
        }

        private void updateConnectionTimeout(int reason) {
            if (reason != this.mConnectionTimeoutReason) {
                if (this.mConnectionTimeoutReason != 0) {
                    removeMessages(9);
                }
                this.mConnectionTimeoutReason = reason;
                this.mConnectionTimeoutStartTime = android.os.SystemClock.uptimeMillis();
                switch (reason) {
                    case 1:
                    case 2:
                        sendEmptyMessage(9);
                        break;
                    case 3:
                        sendEmptyMessageDelayed(9, com.android.server.media.MediaRouterService.CONNECTING_TIMEOUT);
                        break;
                    case 4:
                        sendEmptyMessageDelayed(9, 60000L);
                        break;
                }
            }
        }

        private void connectionTimedOut() {
            if (this.mConnectionTimeoutReason == 0 || this.mSelectedRouteRecord == null) {
                android.util.Log.wtf(com.android.server.media.MediaRouterService.TAG, "Handled connection timeout for no reason.");
                return;
            }
            switch (this.mConnectionTimeoutReason) {
                case 1:
                    android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Selected route no longer available: " + this.mSelectedRouteRecord);
                    break;
                case 2:
                    android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Selected route connection lost: " + this.mSelectedRouteRecord);
                    break;
                case 3:
                    android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Selected route timed out while waiting for connection attempt to begin after " + (android.os.SystemClock.uptimeMillis() - this.mConnectionTimeoutStartTime) + " ms: " + this.mSelectedRouteRecord);
                    break;
                case 4:
                    android.util.Slog.i(com.android.server.media.MediaRouterService.TAG, "Selected route timed out while connecting after " + (android.os.SystemClock.uptimeMillis() - this.mConnectionTimeoutStartTime) + " ms: " + this.mSelectedRouteRecord);
                    break;
            }
            this.mConnectionTimeoutReason = 0;
            unselectSelectedRoute();
        }

        private void scheduleUpdateClientState() {
            if (!this.mClientStateUpdateScheduled) {
                this.mClientStateUpdateScheduled = true;
                sendEmptyMessage(8);
            }
        }

        private void updateClientState() {
            this.mClientStateUpdateScheduled = false;
            android.media.MediaRouterClientState routerState = new android.media.MediaRouterClientState();
            int providerCount = this.mProviderRecords.size();
            for (int i = 0; i < providerCount; i++) {
                this.mProviderRecords.get(i).appendClientState(routerState);
            }
            try {
                synchronized (this.mService.mLock) {
                    this.mUserRecord.mRouterState = routerState;
                    int count = this.mUserRecord.mClientRecords.size();
                    for (int i2 = 0; i2 < count; i2++) {
                        this.mTempClients.add(this.mUserRecord.mClientRecords.get(i2).mClient);
                    }
                }
                int count2 = this.mTempClients.size();
                for (int i3 = 0; i3 < count2; i3++) {
                    try {
                        this.mTempClients.get(i3).onStateChanged();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(com.android.server.media.MediaRouterService.TAG, "Failed to call onStateChanged. Client probably died.");
                    }
                }
            } finally {
                this.mTempClients.clear();
            }
        }

        private void notifyGroupRouteSelected(java.lang.String groupId) {
            try {
                synchronized (this.mService.mLock) {
                    com.android.server.media.MediaRouterService.ClientGroup group = (com.android.server.media.MediaRouterService.ClientGroup) this.mUserRecord.mClientGroupMap.get(groupId);
                    if (group == null) {
                        return;
                    }
                    java.lang.String selectedRouteId = group.mSelectedRouteId;
                    int count = group.mClientRecords.size();
                    for (int i = 0; i < count; i++) {
                        com.android.server.media.MediaRouterService.ClientRecord clientRecord = group.mClientRecords.get(i);
                        if (!android.text.TextUtils.equals(selectedRouteId, clientRecord.mSelectedRouteId)) {
                            this.mTempClients.add(clientRecord.mClient);
                        }
                    }
                    int count2 = this.mTempClients.size();
                    for (int i2 = 0; i2 < count2; i2++) {
                        try {
                            this.mTempClients.get(i2).onGroupRouteSelected(selectedRouteId);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.w(com.android.server.media.MediaRouterService.TAG, "Failed to call onSelectedRouteChanged. Client probably died.");
                        }
                    }
                }
            } finally {
                this.mTempClients.clear();
            }
        }

        private int findProviderRecord(com.android.server.media.RemoteDisplayProviderProxy provider) {
            int count = this.mProviderRecords.size();
            for (int i = 0; i < count; i++) {
                com.android.server.media.MediaRouterService.UserHandler.ProviderRecord record = this.mProviderRecords.get(i);
                if (record.getProvider() == provider) {
                    return i;
                }
            }
            return -1;
        }

        private com.android.server.media.MediaRouterService.UserHandler.RouteRecord findRouteRecord(java.lang.String uniqueId) {
            int count = this.mProviderRecords.size();
            for (int i = 0; i < count; i++) {
                com.android.server.media.MediaRouterService.UserHandler.RouteRecord record = this.mProviderRecords.get(i).findRouteByUniqueId(uniqueId);
                if (record != null) {
                    return record;
                }
            }
            return null;
        }

        private static int getConnectionPhase(int status) {
            switch (status) {
                case 0:
                case 6:
                    return 2;
                case 1:
                case 3:
                    return 0;
                case 2:
                    return 1;
                case 4:
                case 5:
                default:
                    return -1;
            }
        }

        static final class ProviderRecord {
            private android.media.RemoteDisplayState mDescriptor;
            private final com.android.server.media.RemoteDisplayProviderProxy mProvider;
            private final java.util.ArrayList<com.android.server.media.MediaRouterService.UserHandler.RouteRecord> mRoutes = new java.util.ArrayList<>();
            private final java.lang.String mUniquePrefix;

            public ProviderRecord(com.android.server.media.RemoteDisplayProviderProxy provider) {
                this.mProvider = provider;
                this.mUniquePrefix = provider.getFlattenedComponentName() + ":";
            }

            public com.android.server.media.RemoteDisplayProviderProxy getProvider() {
                return this.mProvider;
            }

            public java.lang.String getUniquePrefix() {
                return this.mUniquePrefix;
            }

            public boolean updateDescriptor(android.media.RemoteDisplayState descriptor) {
                boolean changed = false;
                if (this.mDescriptor != descriptor) {
                    this.mDescriptor = descriptor;
                    int targetIndex = 0;
                    if (descriptor != null) {
                        if (descriptor.isValid()) {
                            java.util.List<android.media.RemoteDisplayState.RemoteDisplayInfo> routeDescriptors = descriptor.displays;
                            int routeCount = routeDescriptors.size();
                            for (int i = 0; i < routeCount; i++) {
                                android.media.RemoteDisplayState.RemoteDisplayInfo routeDescriptor = routeDescriptors.get(i);
                                java.lang.String descriptorId = routeDescriptor.id;
                                int sourceIndex = findRouteByDescriptorId(descriptorId);
                                if (sourceIndex < 0) {
                                    java.lang.String uniqueId = assignRouteUniqueId(descriptorId);
                                    com.android.server.media.MediaRouterService.UserHandler.RouteRecord route = new com.android.server.media.MediaRouterService.UserHandler.RouteRecord(this, descriptorId, uniqueId);
                                    this.mRoutes.add(targetIndex, route);
                                    route.updateDescriptor(routeDescriptor);
                                    changed = true;
                                    targetIndex++;
                                } else if (sourceIndex < targetIndex) {
                                    android.util.Slog.w(com.android.server.media.MediaRouterService.TAG, "Ignoring route descriptor with duplicate id: " + routeDescriptor);
                                } else {
                                    com.android.server.media.MediaRouterService.UserHandler.RouteRecord route2 = this.mRoutes.get(sourceIndex);
                                    java.util.Collections.swap(this.mRoutes, sourceIndex, targetIndex);
                                    changed |= route2.updateDescriptor(routeDescriptor);
                                    targetIndex++;
                                }
                            }
                        } else {
                            android.util.Slog.w(com.android.server.media.MediaRouterService.TAG, "Ignoring invalid descriptor from media route provider: " + this.mProvider.getFlattenedComponentName());
                        }
                    }
                    for (int i2 = this.mRoutes.size() - 1; i2 >= targetIndex; i2--) {
                        com.android.server.media.MediaRouterService.UserHandler.RouteRecord route3 = this.mRoutes.remove(i2);
                        route3.updateDescriptor(null);
                        changed = true;
                    }
                }
                return changed;
            }

            public void appendClientState(android.media.MediaRouterClientState state) {
                int routeCount = this.mRoutes.size();
                for (int i = 0; i < routeCount; i++) {
                    state.routes.add(this.mRoutes.get(i).getInfo());
                }
            }

            public com.android.server.media.MediaRouterService.UserHandler.RouteRecord findRouteByUniqueId(java.lang.String uniqueId) {
                int routeCount = this.mRoutes.size();
                for (int i = 0; i < routeCount; i++) {
                    com.android.server.media.MediaRouterService.UserHandler.RouteRecord route = this.mRoutes.get(i);
                    if (route.getUniqueId().equals(uniqueId)) {
                        return route;
                    }
                }
                return null;
            }

            private int findRouteByDescriptorId(java.lang.String descriptorId) {
                int routeCount = this.mRoutes.size();
                for (int i = 0; i < routeCount; i++) {
                    com.android.server.media.MediaRouterService.UserHandler.RouteRecord route = this.mRoutes.get(i);
                    if (route.getDescriptorId().equals(descriptorId)) {
                        return i;
                    }
                }
                return -1;
            }

            public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
                pw.println(prefix + this);
                java.lang.String indent = prefix + "  ";
                this.mProvider.dump(pw, indent);
                int routeCount = this.mRoutes.size();
                if (routeCount != 0) {
                    for (int i = 0; i < routeCount; i++) {
                        this.mRoutes.get(i).dump(pw, indent);
                    }
                    return;
                }
                pw.println(indent + "<no routes>");
            }

            public java.lang.String toString() {
                return "Provider " + this.mProvider.getFlattenedComponentName();
            }

            private java.lang.String assignRouteUniqueId(java.lang.String descriptorId) {
                return this.mUniquePrefix + descriptorId;
            }
        }

        static final class RouteRecord {
            private android.media.RemoteDisplayState.RemoteDisplayInfo mDescriptor;
            private final java.lang.String mDescriptorId;
            private android.media.MediaRouterClientState.RouteInfo mImmutableInfo;
            private final android.media.MediaRouterClientState.RouteInfo mMutableInfo;
            private final com.android.server.media.MediaRouterService.UserHandler.ProviderRecord mProviderRecord;

            public RouteRecord(com.android.server.media.MediaRouterService.UserHandler.ProviderRecord providerRecord, java.lang.String descriptorId, java.lang.String uniqueId) {
                this.mProviderRecord = providerRecord;
                this.mDescriptorId = descriptorId;
                this.mMutableInfo = new android.media.MediaRouterClientState.RouteInfo(uniqueId);
            }

            public com.android.server.media.RemoteDisplayProviderProxy getProvider() {
                return this.mProviderRecord.getProvider();
            }

            public com.android.server.media.MediaRouterService.UserHandler.ProviderRecord getProviderRecord() {
                return this.mProviderRecord;
            }

            public java.lang.String getDescriptorId() {
                return this.mDescriptorId;
            }

            public java.lang.String getUniqueId() {
                return this.mMutableInfo.id;
            }

            public android.media.MediaRouterClientState.RouteInfo getInfo() {
                if (this.mImmutableInfo == null) {
                    this.mImmutableInfo = new android.media.MediaRouterClientState.RouteInfo(this.mMutableInfo);
                }
                return this.mImmutableInfo;
            }

            public boolean isValid() {
                return this.mDescriptor != null;
            }

            public boolean isEnabled() {
                return this.mMutableInfo.enabled;
            }

            public int getStatus() {
                return this.mMutableInfo.statusCode;
            }

            public boolean updateDescriptor(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                boolean changed = false;
                if (this.mDescriptor != descriptor) {
                    this.mDescriptor = descriptor;
                    if (descriptor != null) {
                        java.lang.String name = computeName(descriptor);
                        if (!java.util.Objects.equals(this.mMutableInfo.name, name)) {
                            this.mMutableInfo.name = name;
                            changed = true;
                        }
                        java.lang.String description = computeDescription(descriptor);
                        if (!java.util.Objects.equals(this.mMutableInfo.description, description)) {
                            this.mMutableInfo.description = description;
                            changed = true;
                        }
                        int supportedTypes = computeSupportedTypes(descriptor);
                        if (this.mMutableInfo.supportedTypes != supportedTypes) {
                            this.mMutableInfo.supportedTypes = supportedTypes;
                            changed = true;
                        }
                        boolean enabled = computeEnabled(descriptor);
                        if (this.mMutableInfo.enabled != enabled) {
                            this.mMutableInfo.enabled = enabled;
                            changed = true;
                        }
                        int statusCode = computeStatusCode(descriptor);
                        if (this.mMutableInfo.statusCode != statusCode) {
                            this.mMutableInfo.statusCode = statusCode;
                            changed = true;
                        }
                        int playbackType = computePlaybackType(descriptor);
                        if (this.mMutableInfo.playbackType != playbackType) {
                            this.mMutableInfo.playbackType = playbackType;
                            changed = true;
                        }
                        int playbackStream = computePlaybackStream(descriptor);
                        if (this.mMutableInfo.playbackStream != playbackStream) {
                            this.mMutableInfo.playbackStream = playbackStream;
                            changed = true;
                        }
                        int volume = computeVolume(descriptor);
                        if (this.mMutableInfo.volume != volume) {
                            this.mMutableInfo.volume = volume;
                            changed = true;
                        }
                        int volumeMax = computeVolumeMax(descriptor);
                        if (this.mMutableInfo.volumeMax != volumeMax) {
                            this.mMutableInfo.volumeMax = volumeMax;
                            changed = true;
                        }
                        int volumeHandling = computeVolumeHandling(descriptor);
                        if (this.mMutableInfo.volumeHandling != volumeHandling) {
                            this.mMutableInfo.volumeHandling = volumeHandling;
                            changed = true;
                        }
                        int presentationDisplayId = computePresentationDisplayId(descriptor);
                        if (this.mMutableInfo.presentationDisplayId != presentationDisplayId) {
                            this.mMutableInfo.presentationDisplayId = presentationDisplayId;
                            changed = true;
                        }
                    }
                }
                if (changed) {
                    this.mImmutableInfo = null;
                }
                return changed;
            }

            public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
                pw.println(prefix + this);
                java.lang.String indent = prefix + "  ";
                pw.println(indent + "mMutableInfo=" + this.mMutableInfo);
                pw.println(indent + "mDescriptorId=" + this.mDescriptorId);
                pw.println(indent + "mDescriptor=" + this.mDescriptor);
            }

            public java.lang.String toString() {
                return "Route " + this.mMutableInfo.name + " (" + this.mMutableInfo.id + ")";
            }

            private static java.lang.String computeName(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                return descriptor.name;
            }

            private static java.lang.String computeDescription(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                java.lang.String description = descriptor.description;
                if (android.text.TextUtils.isEmpty(description)) {
                    return null;
                }
                return description;
            }

            private static int computeSupportedTypes(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                return 7;
            }

            private static boolean computeEnabled(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                switch (descriptor.status) {
                    case 2:
                    case 3:
                    case 4:
                        return true;
                    default:
                        return false;
                }
            }

            private static int computeStatusCode(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                switch (descriptor.status) {
                    case 0:
                        return 4;
                    case 1:
                        return 5;
                    case 2:
                        return 3;
                    case 3:
                        return 2;
                    case 4:
                        return 6;
                    default:
                        return 0;
                }
            }

            private static int computePlaybackType(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                return 1;
            }

            private static int computePlaybackStream(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                return 3;
            }

            private static int computeVolume(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                int volume = descriptor.volume;
                int volumeMax = descriptor.volumeMax;
                if (volume < 0) {
                    return 0;
                }
                if (volume > volumeMax) {
                    return volumeMax;
                }
                return volume;
            }

            private static int computeVolumeMax(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                int volumeMax = descriptor.volumeMax;
                if (volumeMax > 0) {
                    return volumeMax;
                }
                return 0;
            }

            private static int computeVolumeHandling(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                int volumeHandling = descriptor.volumeHandling;
                switch (volumeHandling) {
                    case 1:
                        return 1;
                    default:
                        return 0;
                }
            }

            private static int computePresentationDisplayId(android.media.RemoteDisplayState.RemoteDisplayInfo descriptor) {
                int displayId = descriptor.presentationDisplayId;
                if (displayId < 0) {
                    return -1;
                }
                return displayId;
            }
        }
    }

    private class AudioPlayerActiveStateChangedListenerImpl implements com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener {
        private static final long WAIT_MS = 500;
        private final java.lang.Runnable mRestoreBluetoothA2dpRunnable;

        private AudioPlayerActiveStateChangedListenerImpl() {
            final com.android.server.media.MediaRouterService mediaRouterService = com.android.server.media.MediaRouterService.this;
            this.mRestoreBluetoothA2dpRunnable = new java.lang.Runnable() { // from class: com.android.server.media.MediaRouterService$AudioPlayerActiveStateChangedListenerImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    mediaRouterService.restoreBluetoothA2dp();
                }
            };
        }

        @Override // com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener
        public void onAudioPlayerActiveStateChanged(android.media.AudioPlaybackConfiguration config, boolean isRemoved) {
            boolean active = !isRemoved && config.isActive();
            int uid = config.getClientUid();
            int idx = com.android.server.media.MediaRouterService.this.mActivePlayerMinPriorityQueue.indexOf(config.getPlayerInterfaceId());
            if (idx >= 0) {
                com.android.server.media.MediaRouterService.this.mActivePlayerMinPriorityQueue.remove(idx);
                com.android.server.media.MediaRouterService.this.mActivePlayerUidMinPriorityQueue.remove(idx);
            }
            int restoreUid = -1;
            if (active) {
                com.android.server.media.MediaRouterService.this.mActivePlayerMinPriorityQueue.add(config.getPlayerInterfaceId());
                com.android.server.media.MediaRouterService.this.mActivePlayerUidMinPriorityQueue.add(uid);
                restoreUid = uid;
            } else if (com.android.server.media.MediaRouterService.this.mActivePlayerUidMinPriorityQueue.size() > 0) {
                restoreUid = com.android.server.media.MediaRouterService.this.mActivePlayerUidMinPriorityQueue.get(com.android.server.media.MediaRouterService.this.mActivePlayerUidMinPriorityQueue.size() - 1);
            }
            com.android.server.media.MediaRouterService.this.mHandler.removeCallbacks(this.mRestoreBluetoothA2dpRunnable);
            if (restoreUid >= 0) {
                com.android.server.media.MediaRouterService.this.restoreRoute(restoreUid);
                if (com.android.server.media.MediaRouterService.DEBUG) {
                    android.util.Slog.d(com.android.server.media.MediaRouterService.TAG, "onAudioPlayerActiveStateChanged: uid=" + uid + ", active=" + active + ", restoreUid=" + restoreUid);
                    return;
                }
                return;
            }
            com.android.server.media.MediaRouterService.this.mHandler.postDelayed(this.mRestoreBluetoothA2dpRunnable, 500L);
            if (com.android.server.media.MediaRouterService.DEBUG) {
                android.util.Slog.d(com.android.server.media.MediaRouterService.TAG, "onAudioPlayerActiveStateChanged: uid=" + uid + ", active=" + active + ", delaying");
            }
        }
    }

    private class AudioRoutesObserverImpl extends android.media.IAudioRoutesObserver.Stub {
        private static final int HEADSET_FLAGS = 19;

        private AudioRoutesObserverImpl() {
        }

        public void dispatchAudioRoutesChanged(android.media.AudioRoutesInfo newRoutes) {
            synchronized (com.android.server.media.MediaRouterService.this.mLock) {
                if (newRoutes.mainType != com.android.server.media.MediaRouterService.this.mAudioRouteMainType) {
                    if ((newRoutes.mainType & 19) == 0) {
                        com.android.server.media.MediaRouterService.this.mGlobalBluetoothA2dpOn = (newRoutes.bluetoothName == null && com.android.server.media.MediaRouterService.this.mActiveBluetoothDevice == null) ? false : true;
                    } else {
                        com.android.server.media.MediaRouterService.this.mGlobalBluetoothA2dpOn = false;
                    }
                    com.android.server.media.MediaRouterService.this.mAudioRouteMainType = newRoutes.mainType;
                }
            }
        }
    }
}
