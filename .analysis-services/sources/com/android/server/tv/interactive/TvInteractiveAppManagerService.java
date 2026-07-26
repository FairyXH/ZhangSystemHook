package com.android.server.tv.interactive;

/* JADX INFO: loaded from: classes3.dex */
public class TvInteractiveAppManagerService extends com.android.server.SystemService {
    private static final boolean DEBUG = false;
    private static final java.lang.String METADATA_CLASS_NAME = "android.media.tv.interactive.AppLinkInfo.ClassName";
    private static final java.lang.String METADATA_URI = "android.media.tv.interactive.AppLinkInfo.Uri";
    private static final java.lang.String TAG = "TvInteractiveAppManagerService";
    private final android.content.Context mContext;
    private int mCurrentUserId;
    private boolean mGetAdServiceListCalled;
    private boolean mGetAppLinkInfoListCalled;
    private boolean mGetServiceListCalled;
    private final java.lang.Object mLock;
    private final java.util.Set<java.lang.Integer> mRunningProfiles;
    private final android.os.UserManager mUserManager;
    private final android.util.SparseArray<com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState> mUserStates;

    public TvInteractiveAppManagerService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mCurrentUserId = 0;
        this.mRunningProfiles = new java.util.HashSet();
        this.mUserStates = new android.util.SparseArray<>();
        this.mGetServiceListCalled = false;
        this.mGetAdServiceListCalled = false;
        this.mGetAppLinkInfoListCalled = false;
        this.mContext = context;
        this.mUserManager = (android.os.UserManager) getContext().getSystemService("user");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildAppLinkInfoLocked(int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.util.List<android.content.pm.ApplicationInfo> appInfos = pm.getInstalledApplicationsAsUser(android.content.pm.PackageManager.ApplicationInfoFlags.of(128L), userId);
        java.util.ArrayList arrayList = new java.util.ArrayList();
        for (android.content.pm.ApplicationInfo appInfo : appInfos) {
            android.media.tv.interactive.AppLinkInfo info = buildAppLinkInfoLocked(appInfo);
            if (info != null) {
                arrayList.add(info);
            }
        }
        java.util.Collections.sort(arrayList, java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.tv.interactive.TvInteractiveAppManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.media.tv.interactive.AppLinkInfo) obj).getComponentName();
            }
        }));
        userState.mAppLinkInfoList.clear();
        userState.mAppLinkInfoList.addAll(arrayList);
    }

    private android.media.tv.interactive.AppLinkInfo buildAppLinkInfoLocked(android.content.pm.ApplicationInfo appInfo) {
        if (appInfo.metaData == null || appInfo.packageName == null) {
            return null;
        }
        java.lang.String className = appInfo.metaData.getString(METADATA_CLASS_NAME, null);
        java.lang.String uri = appInfo.metaData.getString(METADATA_URI, null);
        if (className == null || uri == null) {
            return null;
        }
        return new android.media.tv.interactive.AppLinkInfo(appInfo.packageName, className, uri);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildTvInteractiveAppServiceListLocked(int userId, java.lang.String[] updatedPackages) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        userState.mPackageSet.clear();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> services = pm.queryIntentServicesAsUser(new android.content.Intent("android.media.tv.interactive.TvInteractiveAppService"), 132, userId);
        java.util.List<android.media.tv.interactive.TvInteractiveAppServiceInfo> iAppList = new java.util.ArrayList<>();
        for (android.content.pm.ResolveInfo ri : services) {
            android.content.pm.ServiceInfo si = ri.serviceInfo;
            if (!"android.permission.BIND_TV_INTERACTIVE_APP".equals(si.permission)) {
                android.util.Slog.w(TAG, "Skipping TV interactiva app service " + si.name + ": it does not require the permission android.permission.BIND_TV_INTERACTIVE_APP");
            } else {
                try {
                    iAppList.add(new android.media.tv.interactive.TvInteractiveAppServiceInfo(this.mContext, new android.content.ComponentName(si.packageName, si.name)));
                    userState.mPackageSet.add(si.packageName);
                } catch (java.lang.Exception e) {
                    com.android.server.utils.Slogf.e(TAG, "failed to load TV Interactive App service " + si.name, e);
                }
            }
        }
        java.util.Collections.sort(iAppList, java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.tv.interactive.TvInteractiveAppManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.media.tv.interactive.TvInteractiveAppServiceInfo) obj).getId();
            }
        }));
        java.util.Map<java.lang.String, com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState> iAppMap = new java.util.HashMap<>();
        android.util.ArrayMap<java.lang.String, java.lang.Integer> tiasAppCount = new android.util.ArrayMap<>(iAppMap.size());
        for (android.media.tv.interactive.TvInteractiveAppServiceInfo info : iAppList) {
            java.lang.String iAppServiceId = info.getId();
            java.lang.Integer count = tiasAppCount.get(iAppServiceId);
            java.lang.Integer count2 = java.lang.Integer.valueOf(count != null ? 1 + count.intValue() : 1);
            tiasAppCount.put(iAppServiceId, count2);
            com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState iAppState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState) userState.mIAppMap.get(iAppServiceId);
            if (iAppState == null) {
                iAppState = new com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState();
            }
            iAppState.mInfo = info;
            iAppState.mUid = getInteractiveAppUid(info);
            iAppState.mComponentName = info.getComponent();
            iAppMap.put(iAppServiceId, iAppState);
            iAppState.mIAppNumber = count2.intValue();
        }
        for (java.lang.String iAppServiceId2 : iAppMap.keySet()) {
            if (!userState.mIAppMap.containsKey(iAppServiceId2)) {
                notifyInteractiveAppServiceAddedLocked(userState, iAppServiceId2);
            } else if (updatedPackages != null) {
                android.content.ComponentName component = iAppMap.get(iAppServiceId2).mInfo.getComponent();
                int length = updatedPackages.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        java.lang.String updatedPackage = updatedPackages[i];
                        if (!component.getPackageName().equals(updatedPackage)) {
                            i++;
                        } else {
                            updateServiceConnectionLocked(component, userId);
                            notifyInteractiveAppServiceUpdatedLocked(userState, iAppServiceId2);
                            break;
                        }
                    }
                }
            }
        }
        for (java.lang.String iAppServiceId3 : userState.mIAppMap.keySet()) {
            if (!iAppMap.containsKey(iAppServiceId3)) {
                com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(((com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState) userState.mIAppMap.get(iAppServiceId3)).mInfo.getComponent());
                if (serviceState != null) {
                    abortPendingCreateSessionRequestsLocked(serviceState, iAppServiceId3, userId);
                }
                notifyInteractiveAppServiceRemovedLocked(userState, iAppServiceId3);
            }
        }
        userState.mIAppMap.clear();
        userState.mIAppMap = iAppMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildTvAdServiceListLocked(int userId, java.lang.String[] updatedPackages) {
        if (!com.android.internal.hidden_from_bootclasspath.android.media.tv.flags.Flags.enableAdServiceFw()) {
            return;
        }
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        userState.mPackageSet.clear();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> services = pm.queryIntentServicesAsUser(new android.content.Intent("android.media.tv.ad.TvAdService"), 132, userId);
        java.util.List<android.media.tv.ad.TvAdServiceInfo> serviceList = new java.util.ArrayList<>();
        for (android.content.pm.ResolveInfo ri : services) {
            android.content.pm.ServiceInfo si = ri.serviceInfo;
            if (!"android.permission.BIND_TV_AD_SERVICE".equals(si.permission)) {
                android.util.Slog.w(TAG, "Skipping TV AD service " + si.name + ": it does not require the permission android.permission.BIND_TV_AD_SERVICE");
            } else {
                try {
                    serviceList.add(new android.media.tv.ad.TvAdServiceInfo(this.mContext, new android.content.ComponentName(si.packageName, si.name)));
                    userState.mPackageSet.add(si.packageName);
                } catch (java.lang.Exception e) {
                    com.android.server.utils.Slogf.e(TAG, "failed to load TV AD service " + si.name, e);
                }
            }
        }
        java.util.Collections.sort(serviceList, java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.tv.interactive.TvInteractiveAppManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.media.tv.ad.TvAdServiceInfo) obj).getId();
            }
        }));
        java.util.Map<java.lang.String, com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState> adServiceMap = new java.util.HashMap<>();
        for (android.media.tv.ad.TvAdServiceInfo info : serviceList) {
            java.lang.String serviceId = info.getId();
            com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState adServiceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState) userState.mAdServiceMap.get(serviceId);
            if (adServiceState == null) {
                adServiceState = new com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState();
            }
            adServiceState.mInfo = info;
            adServiceState.mUid = getAdServiceUid(info);
            adServiceState.mComponentName = info.getComponent();
            adServiceMap.put(serviceId, adServiceState);
        }
        for (java.lang.String serviceId2 : adServiceMap.keySet()) {
            if (!userState.mAdServiceMap.containsKey(serviceId2)) {
                notifyAdServiceAddedLocked(userState, serviceId2);
            } else if (updatedPackages != null) {
                android.content.ComponentName component = adServiceMap.get(serviceId2).mInfo.getComponent();
                int length = updatedPackages.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        java.lang.String updatedPackage = updatedPackages[i];
                        if (!component.getPackageName().equals(updatedPackage)) {
                            i++;
                        } else {
                            updateAdServiceConnectionLocked(component, userId);
                            notifyAdServiceUpdatedLocked(userState, serviceId2);
                            break;
                        }
                    }
                }
            }
        }
        for (java.lang.String serviceId3 : userState.mAdServiceMap.keySet()) {
            if (!adServiceMap.containsKey(serviceId3)) {
                com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState) userState.mAdServiceStateMap.get(((com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState) userState.mAdServiceMap.get(serviceId3)).mInfo.getComponent());
                if (serviceState != null) {
                    abortPendingCreateAdSessionRequestsLocked(serviceState, serviceId3, userId);
                }
                notifyAdServiceRemovedLocked(userState, serviceId3);
            }
        }
        userState.mAdServiceMap.clear();
        userState.mAdServiceMap = adServiceMap;
    }

    private void notifyAdServiceAddedLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState, java.lang.String serviceId) {
        int n = userState.mAdCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mAdCallbacks.getBroadcastItem(i).onAdServiceAdded(serviceId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report added AD service to callback", e);
            }
        }
        userState.mAdCallbacks.finishBroadcast();
    }

    private void notifyAdServiceRemovedLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState, java.lang.String serviceId) {
        int n = userState.mAdCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mAdCallbacks.getBroadcastItem(i).onAdServiceRemoved(serviceId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report removed AD service to callback", e);
            }
        }
        userState.mAdCallbacks.finishBroadcast();
    }

    private void notifyAdServiceUpdatedLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState, java.lang.String serviceId) {
        int n = userState.mAdCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mAdCallbacks.getBroadcastItem(i).onAdServiceUpdated(serviceId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report updated AD service to callback", e);
            }
        }
        userState.mAdCallbacks.finishBroadcast();
    }

    private void notifyInteractiveAppServiceAddedLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState, java.lang.String iAppServiceId) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onInteractiveAppServiceAdded(iAppServiceId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report added Interactive App service to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    private void notifyInteractiveAppServiceRemovedLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState, java.lang.String iAppServiceId) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onInteractiveAppServiceRemoved(iAppServiceId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report removed Interactive App service to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    private void notifyInteractiveAppServiceUpdatedLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState, java.lang.String iAppServiceId) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onInteractiveAppServiceUpdated(iAppServiceId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report updated Interactive App service to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStateChangedLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState, java.lang.String iAppServiceId, int type, int state, int err) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onStateChanged(iAppServiceId, type, state, err);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report RTE state changed", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    private int getInteractiveAppUid(android.media.tv.interactive.TvInteractiveAppServiceInfo info) {
        try {
            return getContext().getPackageManager().getApplicationInfo(info.getServiceInfo().packageName, 0).uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            com.android.server.utils.Slogf.w(TAG, "Unable to get UID for  " + info, e);
            return -1;
        }
    }

    private int getAdServiceUid(android.media.tv.ad.TvAdServiceInfo info) {
        try {
            return getContext().getPackageManager().getApplicationInfo(info.getServiceInfo().packageName, 0).uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            com.android.server.utils.Slogf.w(TAG, "Unable to get UID for  " + info, e);
            return -1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("tv_interactive_app", new com.android.server.tv.interactive.TvInteractiveAppManagerService.BinderService());
        publishBinderService("tv_ad", new com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdBinderService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            registerBroadcastReceivers();
            return;
        }
        if (phase == 600) {
            synchronized (this.mLock) {
                buildTvInteractiveAppServiceListLocked(this.mCurrentUserId, null);
                buildAppLinkInfoLocked(this.mCurrentUserId);
                buildTvAdServiceListLocked(this.mCurrentUserId, null);
            }
        }
    }

    private void registerBroadcastReceivers() {
        com.android.internal.content.PackageMonitor monitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.tv.interactive.TvInteractiveAppManagerService.1
            private void buildTvInteractiveAppServiceList(java.lang.String[] packages) {
                int userId = getChangingUserId();
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    if (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mCurrentUserId == userId || com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mRunningProfiles.contains(java.lang.Integer.valueOf(userId))) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.buildTvInteractiveAppServiceListLocked(userId, packages);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.buildAppLinkInfoLocked(userId);
                    }
                }
            }

            private void buildTvAdServiceList(java.lang.String[] packages) {
                int userId = getChangingUserId();
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    if (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mCurrentUserId == userId || com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mRunningProfiles.contains(java.lang.Integer.valueOf(userId))) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.buildTvAdServiceListLocked(userId, packages);
                    }
                }
            }

            public void onPackageUpdateFinished(java.lang.String packageName, int uid) {
                buildTvInteractiveAppServiceList(new java.lang.String[]{packageName});
                buildTvAdServiceList(new java.lang.String[]{packageName});
            }

            public void onPackagesAvailable(java.lang.String[] packages) {
                if (isReplacing()) {
                    buildTvInteractiveAppServiceList(packages);
                    buildTvAdServiceList(packages);
                }
            }

            public void onPackagesUnavailable(java.lang.String[] packages) {
                if (isReplacing()) {
                    buildTvInteractiveAppServiceList(packages);
                    buildTvAdServiceList(packages);
                }
            }

            public void onSomePackagesChanged() {
                if (isReplacing()) {
                    return;
                }
                buildTvInteractiveAppServiceList(null);
                buildTvAdServiceList(null);
            }

            public boolean onPackageChanged(java.lang.String packageName, int uid, java.lang.String[] components) {
                return true;
            }
        };
        monitor.register(this.mContext, (android.os.Looper) null, android.os.UserHandle.ALL, true);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_STARTED");
        intentFilter.addAction("android.intent.action.USER_STOPPED");
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.tv.interactive.TvInteractiveAppManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if ("android.intent.action.USER_SWITCHED".equals(action)) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.switchUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                    return;
                }
                if ("android.intent.action.USER_REMOVED".equals(action)) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.removeUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                    return;
                }
                if ("android.intent.action.USER_STARTED".equals(action)) {
                    int userId = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.startUser(userId);
                } else if ("android.intent.action.USER_STOPPED".equals(action)) {
                    int userId2 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.stopUser(userId2);
                }
            }
        }, android.os.UserHandle.ALL, intentFilter, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchUser(int userId) {
        synchronized (this.mLock) {
            if (this.mCurrentUserId == userId) {
                return;
            }
            android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
            if (userInfo.isProfile()) {
                android.util.Slog.w(TAG, "cannot switch to a profile!");
                return;
            }
            java.util.Iterator<java.lang.Integer> it = this.mRunningProfiles.iterator();
            while (it.hasNext()) {
                int runningId = it.next().intValue();
                releaseSessionOfUserLocked(runningId);
                unbindServiceOfUserLocked(runningId);
            }
            this.mRunningProfiles.clear();
            releaseSessionOfUserLocked(this.mCurrentUserId);
            unbindServiceOfUserLocked(this.mCurrentUserId);
            this.mCurrentUserId = userId;
            buildTvInteractiveAppServiceListLocked(userId, null);
            buildAppLinkInfoLocked(userId);
            buildTvAdServiceListLocked(userId, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUser(int userId) {
        synchronized (this.mLock) {
            com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getUserStateLocked(userId);
            if (userState == null) {
                return;
            }
            for (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState state : userState.mSessionStateMap.values()) {
                if (state.mSession != null) {
                    try {
                        state.mSession.release();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "error in release", e);
                    }
                }
            }
            userState.mSessionStateMap.clear();
            for (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState : userState.mServiceStateMap.values()) {
                if (serviceState.mService != null) {
                    if (serviceState.mCallback != null) {
                        try {
                            serviceState.mService.unregisterCallback(serviceState.mCallback);
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.e(TAG, "error in unregisterCallback", e2);
                        }
                    }
                    this.mContext.unbindService(serviceState.mConnection);
                }
            }
            userState.mServiceStateMap.clear();
            userState.mIAppMap.clear();
            userState.mPackageSet.clear();
            userState.mClientStateMap.clear();
            userState.mCallbacks.kill();
            this.mRunningProfiles.remove(java.lang.Integer.valueOf(userId));
            this.mUserStates.remove(userId);
            if (userId == this.mCurrentUserId) {
                switchUser(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startUser(int userId) {
        synchronized (this.mLock) {
            if (userId != this.mCurrentUserId && !this.mRunningProfiles.contains(java.lang.Integer.valueOf(userId))) {
                android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
                android.content.pm.UserInfo parentInfo = this.mUserManager.getProfileParent(userId);
                if (userInfo.isProfile() && parentInfo != null && parentInfo.id == this.mCurrentUserId) {
                    startProfileLocked(userId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopUser(int userId) {
        synchronized (this.mLock) {
            if (userId == this.mCurrentUserId) {
                switchUser(android.app.ActivityManager.getCurrentUser());
                return;
            }
            releaseSessionOfUserLocked(userId);
            unbindServiceOfUserLocked(userId);
            this.mRunningProfiles.remove(java.lang.Integer.valueOf(userId));
        }
    }

    private void startProfileLocked(int userId) {
        this.mRunningProfiles.add(java.lang.Integer.valueOf(userId));
        buildTvInteractiveAppServiceListLocked(userId, null);
        buildAppLinkInfoLocked(userId);
        buildTvAdServiceListLocked(userId, null);
    }

    private void releaseSessionOfUserLocked(int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getUserStateLocked(userId);
        if (userState == null) {
            return;
        }
        java.util.List<com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState> sessionStatesToRelease = new java.util.ArrayList<>();
        for (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState : userState.mSessionStateMap.values()) {
            if (sessionState.mSession != null) {
                sessionStatesToRelease.add(sessionState);
            }
        }
        for (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState2 : sessionStatesToRelease) {
            try {
                sessionState2.mSession.release();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "error in release", e);
            }
            clearSessionAndNotifyClientLocked(sessionState2);
        }
    }

    private void unbindServiceOfUserLocked(int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getUserStateLocked(userId);
        if (userState == null) {
            return;
        }
        java.util.Iterator<android.content.ComponentName> it = userState.mServiceStateMap.keySet().iterator();
        while (it.hasNext()) {
            android.content.ComponentName component = it.next();
            com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(component);
            if (serviceState != null && serviceState.mSessionTokens.isEmpty()) {
                if (serviceState.mCallback != null) {
                    try {
                        serviceState.mService.unregisterCallback(serviceState.mCallback);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "error in unregisterCallback", e);
                    }
                }
                this.mContext.unbindService(serviceState.mConnection);
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSessionAndNotifyClientLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState state) {
        if (state.mClient != null) {
            try {
                state.mClient.onSessionReleased(state.mSeq);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "error in onSessionReleased", e);
            }
        }
        removeAdSessionStateLocked(state.mSessionToken, state.mUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAdSessionAndNotifyClientLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState state) {
        if (state.mClient != null) {
            try {
                state.mClient.onSessionReleased(state.mSeq);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "error in onSessionReleased", e);
            }
        }
        removeAdSessionStateLocked(state.mSessionToken, state.mUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int resolveCallingUserId(int callingPid, int callingUid, int requestedUserId, java.lang.String methodName) {
        return android.app.ActivityManager.handleIncomingUser(callingPid, callingUid, requestedUserId, false, false, methodName, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState getOrCreateUserStateLocked(int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getUserStateLocked(userId);
        if (userState == null) {
            com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState2 = new com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState(userId);
            this.mUserStates.put(userId, userState2);
            return userState2;
        }
        return userState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState getUserStateLocked(int userId) {
        return this.mUserStates.get(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState getServiceStateLocked(android.content.ComponentName component, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(component);
        if (serviceState == null) {
            throw new java.lang.IllegalStateException("Service state not found for " + component + " (userId=" + userId + ")");
        }
        return serviceState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState getAdSessionStateLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        return getAdSessionStateLocked(sessionToken, callingUid, userState);
    }

    private com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState getAdSessionStateLocked(android.os.IBinder sessionToken, int callingUid, com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState) userState.mAdSessionStateMap.get(sessionToken);
        if (sessionState == null) {
            throw new com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException("Session state not found for token " + sessionToken);
        }
        if (callingUid != 1000 && callingUid != sessionState.mCallingUid) {
            throw new java.lang.SecurityException("Illegal access to the session with token " + sessionToken + " from uid " + callingUid);
        }
        return sessionState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.tv.ad.ITvAdSession getAdSessionLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        return getAdSessionLocked(getAdSessionStateLocked(sessionToken, callingUid, userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.tv.ad.ITvAdSession getAdSessionLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState) {
        android.media.tv.ad.ITvAdSession session = sessionState.mSession;
        if (session == null) {
            throw new java.lang.IllegalStateException("Session not yet created for token " + sessionState.mSessionToken);
        }
        return session;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState getSessionStateLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        return getSessionStateLocked(sessionToken, callingUid, userState);
    }

    private com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState getSessionStateLocked(android.os.IBinder sessionToken, int callingUid, com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState) userState.mSessionStateMap.get(sessionToken);
        if (sessionState == null) {
            throw new com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException("Session state not found for token " + sessionToken);
        }
        if (callingUid != 1000 && callingUid != sessionState.mCallingUid) {
            throw new java.lang.SecurityException("Illegal access to the session with token " + sessionToken + " from uid " + callingUid);
        }
        return sessionState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.tv.interactive.ITvInteractiveAppSession getSessionLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        return getSessionLocked(getSessionStateLocked(sessionToken, callingUid, userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.tv.interactive.ITvInteractiveAppSession getSessionLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState) {
        android.media.tv.interactive.ITvInteractiveAppSession session = sessionState.mSession;
        if (session == null) {
            throw new java.lang.IllegalStateException("Session not yet created for token " + sessionState.mSessionToken);
        }
        return session;
    }

    private final class TvAdBinderService extends android.media.tv.ad.ITvAdManager.Stub {
        private TvAdBinderService() {
        }

        public java.util.List<android.media.tv.ad.TvAdServiceInfo> getTvAdServiceList(int userId) {
            java.util.List<android.media.tv.ad.TvAdServiceInfo> adServiceList;
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getTvAdServiceList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    if (!com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mGetAdServiceListCalled) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.buildTvAdServiceListLocked(userId, null);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mGetAdServiceListCalled = true;
                    }
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    adServiceList = new java.util.ArrayList<>();
                    for (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState state : userState.mAdServiceMap.values()) {
                        adServiceList.add(state.mInfo);
                    }
                }
                return adServiceList;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendAppLinkCommand(java.lang.String serviceId, android.os.Bundle command, int userId) {
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "sendAppLinkCommand");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendAppLinkCommand", e);
                }
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState adServiceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState) userState.mAdServiceMap.get(serviceId);
                    if (adServiceState == null) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "failed to sendAppLinkCommand - unknown service id " + serviceId);
                        return;
                    }
                    android.content.ComponentName componentName = adServiceState.mInfo.getComponent();
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState) userState.mAdServiceStateMap.get(componentName);
                    if (serviceState == null) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState serviceState2 = new com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState(componentName, serviceId, resolvedUserId);
                        serviceState2.addPendingAppLinkCommand(command);
                        userState.mAdServiceStateMap.put(componentName, serviceState2);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateAdServiceConnectionLocked(componentName, resolvedUserId);
                    } else if (serviceState.mService != null) {
                        serviceState.mService.sendAppLinkCommand(command);
                    } else {
                        serviceState.addPendingAppLinkCommand(command);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateAdServiceConnectionLocked(componentName, resolvedUserId);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0067 A[Catch: all -> 0x004b, TRY_ENTER, TryCatch #4 {all -> 0x004b, blocks: (B:7:0x0029, B:9:0x0039, B:10:0x0046, B:18:0x0067, B:19:0x008c, B:25:0x00a2, B:30:0x00da, B:31:0x00e7), top: B:68:0x0029 }] */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0091 A[Catch: all -> 0x0156, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x0156, blocks: (B:5:0x0021, B:15:0x0053, B:22:0x0091, B:28:0x00d4, B:34:0x00ec), top: B:66:0x0021 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void createSession(android.media.tv.ad.ITvAdClient r27, java.lang.String r28, java.lang.String r29, int r30, int r31) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 364
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdBinderService.createSession(android.media.tv.ad.ITvAdClient, java.lang.String, java.lang.String, int, int):void");
        }

        public void releaseSession(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "releaseSession");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.releaseSessionLocked(sessionToken, callingUid, resolvedUserId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setSurface(android.os.IBinder sessionToken, android.view.Surface surface, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setSurface");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).setSurface(surface);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in setSurface", e);
                    }
                }
            } finally {
                if (surface != null) {
                    surface.release();
                }
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void dispatchSurfaceChanged(android.os.IBinder sessionToken, int format, int width, int height, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "dispatchSurfaceChanged");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).dispatchSurfaceChanged(format, width, height);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in dispatchSurfaceChanged", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void startAdService(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "startAdService");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).startAdService();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in start", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void stopAdService(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "stopAdService");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).stopAdService();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in stop", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void resetAdService(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "resetAdService");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).resetAdService();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in reset", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCurrentVideoBounds(android.os.IBinder sessionToken, android.graphics.Rect bounds, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCurrentVideoBounds");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).sendCurrentVideoBounds(bounds);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCurrentVideoBounds", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCurrentChannelUri(android.os.IBinder sessionToken, android.net.Uri channelUri, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCurrentChannelUri");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).sendCurrentChannelUri(channelUri);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCurrentChannelUri", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendTrackInfoList(android.os.IBinder sessionToken, java.util.List<android.media.tv.TvTrackInfo> tracks, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendTrackInfoList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).sendTrackInfoList(tracks);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendTrackInfoList", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCurrentTvInputId(android.os.IBinder sessionToken, java.lang.String inputId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCurrentTvInputId");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).sendCurrentTvInputId(inputId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCurrentTvInputId", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendSigningResult(android.os.IBinder sessionToken, java.lang.String signingId, byte[] result, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendSigningResult");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).sendSigningResult(signingId, result);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendSigningResult", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyError(android.os.IBinder sessionToken, java.lang.String errMsg, android.os.Bundle params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyError");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).notifyError(errMsg, params);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyError", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTvMessage(android.os.IBinder sessionToken, int type, android.os.Bundle data, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyTvMessage");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).notifyTvMessage(type, data);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTvMessage", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTvInputSessionData(android.os.IBinder sessionToken, java.lang.String type, android.os.Bundle data, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyTvInputSessionData");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionState).notifyTvInputSessionData(type, data);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTvInputSessionData", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void registerCallback(android.media.tv.ad.ITvAdManagerCallback callback, int userId) {
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "registerCallback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    if (!userState.mAdCallbacks.register(callback)) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "client process has already died");
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unregisterCallback(android.media.tv.ad.ITvAdManagerCallback callback, int userId) {
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "unregisterCallback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    userState.mAdCallbacks.unregister(callback);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void createMediaView(android.os.IBinder sessionToken, android.os.IBinder windowToken, android.graphics.Rect frame, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "createMediaView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionToken, callingUid, resolvedUserId).createMediaView(windowToken, frame);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in createMediaView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void relayoutMediaView(android.os.IBinder sessionToken, android.graphics.Rect frame, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "relayoutMediaView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionToken, callingUid, resolvedUserId).relayoutMediaView(frame);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in relayoutMediaView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void removeMediaView(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "removeMediaView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getAdSessionLocked(sessionToken, callingUid, resolvedUserId).removeMediaView();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in removeMediaView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private final class BinderService extends android.media.tv.interactive.ITvInteractiveAppManager.Stub {
        private BinderService() {
        }

        public java.util.List<android.media.tv.interactive.TvInteractiveAppServiceInfo> getTvInteractiveAppServiceList(int userId) {
            java.util.List<android.media.tv.interactive.TvInteractiveAppServiceInfo> iAppList;
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getTvInteractiveAppServiceList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    if (!com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mGetServiceListCalled) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.buildTvInteractiveAppServiceListLocked(userId, null);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mGetServiceListCalled = true;
                    }
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    iAppList = new java.util.ArrayList<>();
                    for (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState state : userState.mIAppMap.values()) {
                        iAppList.add(state.mInfo);
                    }
                }
                return iAppList;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<android.media.tv.interactive.AppLinkInfo> getAppLinkInfoList(int userId) {
            java.util.List<android.media.tv.interactive.AppLinkInfo> appLinkInfos;
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getAppLinkInfoList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    if (!com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mGetAppLinkInfoListCalled) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.buildAppLinkInfoLocked(userId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mGetAppLinkInfoListCalled = true;
                    }
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    appLinkInfos = new java.util.ArrayList<>(userState.mAppLinkInfoList);
                }
                return appLinkInfos;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void registerAppLinkInfo(java.lang.String tiasId, android.media.tv.interactive.AppLinkInfo appLinkInfo, int userId) {
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "registerAppLinkInfo: " + appLinkInfo);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in registerAppLinkInfo", e);
                }
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState iAppState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState) userState.mIAppMap.get(tiasId);
                    if (iAppState == null) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "failed to registerAppLinkInfo - unknown TIAS id " + tiasId);
                        return;
                    }
                    android.content.ComponentName componentName = iAppState.mInfo.getComponent();
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(componentName);
                    if (serviceState == null) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState2 = new com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState(componentName, tiasId, resolvedUserId);
                        serviceState2.addPendingAppLink(appLinkInfo, true);
                        userState.mServiceStateMap.put(componentName, serviceState2);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateServiceConnectionLocked(componentName, resolvedUserId);
                    } else if (serviceState.mService != null) {
                        serviceState.mService.registerAppLinkInfo(appLinkInfo);
                    } else {
                        serviceState.addPendingAppLink(appLinkInfo, true);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateServiceConnectionLocked(componentName, resolvedUserId);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unregisterAppLinkInfo(java.lang.String tiasId, android.media.tv.interactive.AppLinkInfo appLinkInfo, int userId) {
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "unregisterAppLinkInfo: " + appLinkInfo);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in unregisterAppLinkInfo", e);
                }
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState iAppState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState) userState.mIAppMap.get(tiasId);
                    if (iAppState == null) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "failed to unregisterAppLinkInfo - unknown TIAS id " + tiasId);
                        return;
                    }
                    android.content.ComponentName componentName = iAppState.mInfo.getComponent();
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(componentName);
                    if (serviceState == null) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState2 = new com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState(componentName, tiasId, resolvedUserId);
                        serviceState2.addPendingAppLink(appLinkInfo, false);
                        userState.mServiceStateMap.put(componentName, serviceState2);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateServiceConnectionLocked(componentName, resolvedUserId);
                    } else if (serviceState.mService != null) {
                        serviceState.mService.unregisterAppLinkInfo(appLinkInfo);
                    } else {
                        serviceState.addPendingAppLink(appLinkInfo, false);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateServiceConnectionLocked(componentName, resolvedUserId);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendAppLinkCommand(java.lang.String tiasId, android.os.Bundle command, int userId) {
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "sendAppLinkCommand");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendAppLinkCommand", e);
                }
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState iAppState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState) userState.mIAppMap.get(tiasId);
                    if (iAppState == null) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "failed to sendAppLinkCommand - unknown TIAS id " + tiasId);
                        return;
                    }
                    android.content.ComponentName componentName = iAppState.mInfo.getComponent();
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(componentName);
                    if (serviceState == null) {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState2 = new com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState(componentName, tiasId, resolvedUserId);
                        serviceState2.addPendingAppLinkCommand(command);
                        userState.mServiceStateMap.put(componentName, serviceState2);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateServiceConnectionLocked(componentName, resolvedUserId);
                    } else if (serviceState.mService != null) {
                        serviceState.mService.sendAppLinkCommand(command);
                    } else {
                        serviceState.addPendingAppLinkCommand(command);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.updateServiceConnectionLocked(componentName, resolvedUserId);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0067 A[Catch: all -> 0x004b, TRY_ENTER, TryCatch #4 {all -> 0x004b, blocks: (B:7:0x0029, B:9:0x0039, B:10:0x0046, B:18:0x0067, B:19:0x008c, B:25:0x00a2, B:30:0x00da, B:31:0x00e7), top: B:68:0x0029 }] */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0091 A[Catch: all -> 0x0156, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x0156, blocks: (B:5:0x0021, B:15:0x0053, B:22:0x0091, B:28:0x00d4, B:34:0x00ec), top: B:66:0x0021 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void createSession(android.media.tv.interactive.ITvInteractiveAppClient r27, java.lang.String r28, int r29, int r30, int r31) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 364
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.interactive.TvInteractiveAppManagerService.BinderService.createSession(android.media.tv.interactive.ITvInteractiveAppClient, java.lang.String, int, int, int):void");
        }

        public void releaseSession(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "releaseSession");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.releaseAdSessionLocked(sessionToken, callingUid, resolvedUserId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTuned(android.os.IBinder sessionToken, android.net.Uri channelUri, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTuned");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTuned(channelUri);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTuned", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTrackSelected(android.os.IBinder sessionToken, int type, java.lang.String trackId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTrackSelected");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTrackSelected(type, trackId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTrackSelected", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTracksChanged(android.os.IBinder sessionToken, java.util.List<android.media.tv.TvTrackInfo> tracks, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTracksChanged");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTracksChanged(tracks);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTracksChanged", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyVideoAvailable(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyVideoAvailable");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyVideoAvailable();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyVideoAvailable", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyVideoUnavailable(android.os.IBinder sessionToken, int reason, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyVideoUnavailable");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyVideoUnavailable(reason);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyVideoUnavailable", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyVideoFreezeUpdated(android.os.IBinder sessionToken, boolean isFrozen, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyVideoFreezeUpdated");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyVideoFreezeUpdated(isFrozen);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyVideoFreezeUpdated", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyContentAllowed(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyContentAllowed");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyContentAllowed();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyContentAllowed", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyContentBlocked(android.os.IBinder sessionToken, java.lang.String rating, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyContentBlocked");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyContentBlocked(rating);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyContentBlocked", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifySignalStrength(android.os.IBinder sessionToken, int strength, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifySignalStrength");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifySignalStrength(strength);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifySignalStrength", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTvMessage(android.os.IBinder sessionToken, int type, android.os.Bundle data, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyTvMessage");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTvMessage(type, data);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTvMessage", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyRecordingStarted(android.os.IBinder sessionToken, java.lang.String recordingId, java.lang.String requestId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyRecordingStarted");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyRecordingStarted(recordingId, requestId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyRecordingStarted", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyRecordingStopped(android.os.IBinder sessionToken, java.lang.String recordingId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyRecordingStopped");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyRecordingStopped(recordingId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyRecordingStopped", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void startInteractiveApp(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "startInteractiveApp");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).startInteractiveApp();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in start", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void stopInteractiveApp(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "stopInteractiveApp");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).stopInteractiveApp();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in stop", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void resetInteractiveApp(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "resetInteractiveApp");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).resetInteractiveApp();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in reset", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void createBiInteractiveApp(android.os.IBinder sessionToken, android.net.Uri biIAppUri, android.os.Bundle params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "createBiInteractiveApp");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).createBiInteractiveApp(biIAppUri, params);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in createBiInteractiveApp", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void destroyBiInteractiveApp(android.os.IBinder sessionToken, java.lang.String biIAppId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "destroyBiInteractiveApp");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).destroyBiInteractiveApp(biIAppId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in destroyBiInteractiveApp", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setTeletextAppEnabled(android.os.IBinder sessionToken, boolean enable, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setTeletextAppEnabled");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).setTeletextAppEnabled(enable);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in setTeletextAppEnabled", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCurrentVideoBounds(android.os.IBinder sessionToken, android.graphics.Rect bounds, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCurrentVideoBounds");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendCurrentVideoBounds(bounds);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCurrentVideoBounds", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCurrentChannelUri(android.os.IBinder sessionToken, android.net.Uri channelUri, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCurrentChannelUri");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendCurrentChannelUri(channelUri);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCurrentChannelUri", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCurrentChannelLcn(android.os.IBinder sessionToken, int lcn, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCurrentChannelLcn");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendCurrentChannelLcn(lcn);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCurrentChannelLcn", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendStreamVolume(android.os.IBinder sessionToken, float volume, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendStreamVolume");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendStreamVolume(volume);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendStreamVolume", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendTrackInfoList(android.os.IBinder sessionToken, java.util.List<android.media.tv.TvTrackInfo> tracks, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendTrackInfoList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendTrackInfoList(tracks);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendTrackInfoList", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendSelectedTrackInfo(android.os.IBinder sessionToken, java.util.List<android.media.tv.TvTrackInfo> tracks, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendSelectedTrackInfo");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendSelectedTrackInfo(tracks);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendSelectedTrackInfo", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCurrentTvInputId(android.os.IBinder sessionToken, java.lang.String inputId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCurrentTvInputId");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendCurrentTvInputId(inputId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCurrentTvInputId", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendTimeShiftMode(android.os.IBinder sessionToken, int mode, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendTimeShiftMode");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendTimeShiftMode(mode);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendTimeShiftMode", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendAvailableSpeeds(android.os.IBinder sessionToken, float[] speeds, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendAvailableSpeeds");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendAvailableSpeeds(speeds);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendAvailableSpeeds", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendTvRecordingInfo(android.os.IBinder sessionToken, android.media.tv.TvRecordingInfo recordingInfo, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendTvRecordingInfo");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendTvRecordingInfo(recordingInfo);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendTvRecordingInfo", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendTvRecordingInfoList(android.os.IBinder sessionToken, java.util.List<android.media.tv.TvRecordingInfo> recordingInfoList, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendTvRecordingInfoList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendTvRecordingInfoList(recordingInfoList);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendTvRecordingInfoList", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendSigningResult(android.os.IBinder sessionToken, java.lang.String signingId, byte[] result, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendSigningResult");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendSigningResult(signingId, result);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendSigningResult", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendCertificate(android.os.IBinder sessionToken, java.lang.String host, int port, android.os.Bundle certBundle, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendCertificate");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).sendCertificate(host, port, certBundle);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in sendCertificate", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyError(android.os.IBinder sessionToken, java.lang.String errMsg, android.os.Bundle params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyError");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyError(errMsg, params);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyError", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTimeShiftPlaybackParams(android.os.IBinder sessionToken, android.media.PlaybackParams params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTimeShiftPlaybackParams");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTimeShiftPlaybackParams(params);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTimeShiftPlaybackParams", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTimeShiftStatusChanged(android.os.IBinder sessionToken, java.lang.String inputId, int status, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTimeShiftStatusChanged");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTimeShiftStatusChanged(inputId, status);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTimeShiftStatusChanged", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTimeShiftStartPositionChanged(android.os.IBinder sessionToken, java.lang.String inputId, long timeMs, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTimeShiftStartPositionChanged");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTimeShiftStartPositionChanged(inputId, timeMs);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTimeShiftStartPositionChanged", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTimeShiftCurrentPositionChanged(android.os.IBinder sessionToken, java.lang.String inputId, long timeMs, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTimeShiftCurrentPositionChanged");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyTimeShiftCurrentPositionChanged(inputId, timeMs);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyTimeShiftCurrentPositionChanged", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyRecordingConnectionFailed(android.os.IBinder sessionToken, java.lang.String recordingId, java.lang.String inputId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyRecordingConnectionFailed");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyRecordingConnectionFailed(recordingId, inputId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyRecordingConnectionFailed", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyRecordingDisconnected(android.os.IBinder sessionToken, java.lang.String recordingId, java.lang.String inputId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyRecordingDisconnected");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyRecordingDisconnected(recordingId, inputId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyRecordingDisconnected", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyRecordingTuned(android.os.IBinder sessionToken, java.lang.String recordingId, android.net.Uri channelUri, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyRecordingTuned");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyRecordingTuned(recordingId, channelUri);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyRecordingTuned", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyRecordingError(android.os.IBinder sessionToken, java.lang.String recordingId, int err, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyRecordingError");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyRecordingError(recordingId, err);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyRecordingError", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyRecordingScheduled(android.os.IBinder sessionToken, java.lang.String recordingId, java.lang.String requestId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyRecordingScheduled");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyRecordingScheduled(recordingId, requestId);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyRecordingScheduled", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setSurface(android.os.IBinder sessionToken, android.view.Surface surface, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setSurface");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).setSurface(surface);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in setSurface", e);
                    }
                }
            } finally {
                if (surface != null) {
                    surface.release();
                }
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void dispatchSurfaceChanged(android.os.IBinder sessionToken, int format, int width, int height, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "dispatchSurfaceChanged");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).dispatchSurfaceChanged(format, width, height);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in dispatchSurfaceChanged", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyBroadcastInfoResponse(android.os.IBinder sessionToken, android.media.tv.BroadcastInfoResponse response, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyBroadcastInfoResponse");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyBroadcastInfoResponse(response);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyBroadcastInfoResponse", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyAdResponse(android.os.IBinder sessionToken, android.media.tv.AdResponse response, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyAdResponse");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyAdResponse(response);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyAdResponse", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyAdBufferConsumed(android.os.IBinder sessionToken, android.media.tv.AdBuffer buffer, int userId) {
            android.os.SharedMemory sharedMemory;
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyAdBufferConsumed");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        try {
                            com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                            com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionState).notifyAdBufferConsumed(buffer);
                        } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                            com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in notifyAdBufferConsumed", e);
                            if (buffer != null) {
                                sharedMemory = buffer.getSharedMemory();
                            }
                        }
                        if (buffer != null) {
                            sharedMemory = buffer.getSharedMemory();
                            sharedMemory.close();
                        }
                    } catch (java.lang.Throwable th) {
                        if (buffer != null) {
                            buffer.getSharedMemory().close();
                        }
                        throw th;
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void registerCallback(android.media.tv.interactive.ITvInteractiveAppManagerCallback callback, int userId) {
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "registerCallback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    if (!userState.mCallbacks.register(callback)) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "client process has already died");
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unregisterCallback(android.media.tv.interactive.ITvInteractiveAppManagerCallback callback, int userId) {
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "unregisterCallback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    userState.mCallbacks.unregister(callback);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void createMediaView(android.os.IBinder sessionToken, android.os.IBinder windowToken, android.graphics.Rect frame, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "createMediaView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).createMediaView(windowToken, frame);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in createMediaView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void relayoutMediaView(android.os.IBinder sessionToken, android.graphics.Rect frame, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "relayoutMediaView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).relayoutMediaView(frame);
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in relayoutMediaView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void removeMediaView(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "removeMediaView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    try {
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).removeMediaView();
                    } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in removeMediaView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSessionTokenToClientLocked(android.media.tv.interactive.ITvInteractiveAppClient client, java.lang.String iAppServiceId, android.os.IBinder sessionToken, android.view.InputChannel channel, int seq) {
        try {
            client.onSessionCreated(iAppServiceId, sessionToken, channel, seq);
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.e(TAG, "error in onSessionCreated", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAdSessionTokenToClientLocked(android.media.tv.ad.ITvAdClient client, java.lang.String serviceId, android.os.IBinder sessionToken, android.view.InputChannel channel, int seq) {
        try {
            client.onSessionCreated(serviceId, sessionToken, channel, seq);
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.e(TAG, "error in onSessionCreated", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean createSessionInternalLocked(android.media.tv.interactive.ITvInteractiveAppService service, android.os.IBinder sessionToken, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState) userState.mSessionStateMap.get(sessionToken);
        android.view.InputChannel[] channels = android.view.InputChannel.openInputChannelPair(sessionToken.toString());
        boolean created = true;
        try {
            try {
                service.createSession(channels[1], new com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionCallback(sessionState, channels), sessionState.mIAppServiceId, sessionState.mType);
            } catch (android.os.RemoteException e) {
                e = e;
                com.android.server.utils.Slogf.e(TAG, "error in createSession", e);
                sendSessionTokenToClientLocked(sessionState.mClient, sessionState.mIAppServiceId, null, null, sessionState.mSeq);
                created = false;
            }
        } catch (android.os.RemoteException e2) {
            e = e2;
        }
        channels[1].dispose();
        return created;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean createAdSessionInternalLocked(android.media.tv.ad.ITvAdService service, android.os.IBinder sessionToken, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState) userState.mAdSessionStateMap.get(sessionToken);
        android.view.InputChannel[] channels = android.view.InputChannel.openInputChannelPair(sessionToken.toString());
        boolean created = true;
        try {
            try {
                service.createSession(channels[1], new com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionCallback(sessionState, channels), sessionState.mAdServiceId, sessionState.mType);
            } catch (android.os.RemoteException e) {
                e = e;
                com.android.server.utils.Slogf.e(TAG, "error in createSession", e);
                sendAdSessionTokenToClientLocked(sessionState.mClient, sessionState.mAdServiceId, null, null, sessionState.mSeq);
                created = false;
            }
        } catch (android.os.RemoteException e2) {
            e = e2;
        }
        channels[1].dispose();
        return created;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState releaseAdSessionLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = null;
        try {
            try {
                sessionState = getAdSessionStateLocked(sessionToken, callingUid, userId);
                getOrCreateUserStateLocked(userId);
                if (sessionState.mSession != null) {
                    sessionState.mSession.asBinder().unlinkToDeath(sessionState, 0);
                    sessionState.mSession.release();
                }
            } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                com.android.server.utils.Slogf.e(TAG, "error in releaseSession", e);
                if (sessionState != null) {
                }
            }
            removeAdSessionStateLocked(sessionToken, userId);
            return sessionState;
        } finally {
            if (sessionState != null) {
                sessionState.mSession = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState releaseSessionLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = null;
        try {
            try {
                sessionState = getSessionStateLocked(sessionToken, callingUid, userId);
                getOrCreateUserStateLocked(userId);
                if (sessionState.mSession != null) {
                    sessionState.mSession.asBinder().unlinkToDeath(sessionState, 0);
                    sessionState.mSession.release();
                }
            } catch (android.os.RemoteException | com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionNotFoundException e) {
                com.android.server.utils.Slogf.e(TAG, "error in releaseSession", e);
                if (sessionState != null) {
                }
            }
            removeSessionStateLocked(sessionToken, userId);
            return sessionState;
        } finally {
            if (sessionState != null) {
                sessionState.mSession = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSessionStateLocked(android.os.IBinder sessionToken, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState) userState.mSessionStateMap.remove(sessionToken);
        if (sessionState == null) {
            com.android.server.utils.Slogf.e(TAG, "sessionState null, no more remove session action!");
            return;
        }
        com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState clientState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState) userState.mClientStateMap.get(sessionState.mClient.asBinder());
        if (clientState != null) {
            clientState.mSessionTokens.remove(sessionToken);
            if (clientState.isEmpty()) {
                userState.mClientStateMap.remove(sessionState.mClient.asBinder());
                sessionState.mClient.asBinder().unlinkToDeath(clientState, 0);
            }
        }
        com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(sessionState.mComponent);
        if (serviceState != null) {
            serviceState.mSessionTokens.remove(sessionToken);
        }
        updateServiceConnectionLocked(sessionState.mComponent, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAdSessionStateLocked(android.os.IBinder sessionToken, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState) userState.mAdSessionStateMap.remove(sessionToken);
        if (sessionState == null) {
            com.android.server.utils.Slogf.e(TAG, "sessionState null, no more remove session action!");
            return;
        }
        com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState clientState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState) userState.mClientStateMap.get(sessionState.mClient.asBinder());
        if (clientState != null) {
            clientState.mSessionTokens.remove(sessionToken);
            if (clientState.isEmpty()) {
                userState.mClientStateMap.remove(sessionState.mClient.asBinder());
                sessionState.mClient.asBinder().unlinkToDeath(clientState, 0);
            }
        }
        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState) userState.mAdServiceStateMap.get(sessionState.mComponent);
        if (serviceState != null) {
            serviceState.mSessionTokens.remove(sessionToken);
        }
        updateAdServiceConnectionLocked(sessionState.mComponent, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abortPendingCreateSessionRequestsLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState, java.lang.String iAppServiceId, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        java.util.List<com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState> sessionsToAbort = new java.util.ArrayList<>();
        for (android.os.IBinder sessionToken : serviceState.mSessionTokens) {
            com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState) userState.mSessionStateMap.get(sessionToken);
            if (sessionState.mSession == null && (iAppServiceId == null || sessionState.mIAppServiceId.equals(iAppServiceId))) {
                sessionsToAbort.add(sessionState);
            }
        }
        for (com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState2 : sessionsToAbort) {
            removeSessionStateLocked(sessionState2.mSessionToken, sessionState2.mUserId);
            sendSessionTokenToClientLocked(sessionState2.mClient, sessionState2.mIAppServiceId, null, null, sessionState2.mSeq);
        }
        updateServiceConnectionLocked(serviceState.mComponent, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abortPendingCreateAdSessionRequestsLocked(com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState serviceState, java.lang.String serviceId, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        java.util.List<com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState> sessionsToAbort = new java.util.ArrayList<>();
        for (android.os.IBinder sessionToken : serviceState.mSessionTokens) {
            com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState) userState.mAdSessionStateMap.get(sessionToken);
            if (sessionState.mSession == null && (serviceState == null || sessionState.mAdServiceId.equals(serviceId))) {
                sessionsToAbort.add(sessionState);
            }
        }
        for (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState2 : sessionsToAbort) {
            removeAdSessionStateLocked(sessionState2.mSessionToken, sessionState2.mUserId);
            sendAdSessionTokenToClientLocked(sessionState2.mClient, sessionState2.mAdServiceId, null, null, sessionState2.mSeq);
        }
        updateAdServiceConnectionLocked(serviceState.mComponent, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceConnectionLocked(android.content.ComponentName component, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(component);
        if (serviceState == null) {
            return;
        }
        if (serviceState.mReconnecting) {
            if (!serviceState.mSessionTokens.isEmpty()) {
                return;
            } else {
                serviceState.mReconnecting = false;
            }
        }
        boolean shouldBind = (serviceState.mSessionTokens.isEmpty() && serviceState.mPendingAppLinkInfo.isEmpty() && serviceState.mPendingAppLinkCommand.isEmpty()) ? false : true;
        if (serviceState.mService == null && shouldBind) {
            if (serviceState.mBound) {
                return;
            }
            android.content.Intent i = new android.content.Intent("android.media.tv.interactive.TvInteractiveAppService").setComponent(component);
            serviceState.mBound = this.mContext.bindServiceAsUser(i, serviceState.mConnection, 33554433, new android.os.UserHandle(userId));
            return;
        }
        if (serviceState.mService != null && !shouldBind) {
            this.mContext.unbindService(serviceState.mConnection);
            userState.mServiceStateMap.remove(component);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAdServiceConnectionLocked(android.content.ComponentName component, int userId) {
        com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState) userState.mAdServiceStateMap.get(component);
        if (serviceState == null) {
            return;
        }
        if (serviceState.mReconnecting) {
            if (!serviceState.mSessionTokens.isEmpty()) {
                return;
            } else {
                serviceState.mReconnecting = false;
            }
        }
        boolean shouldBind = (serviceState.mSessionTokens.isEmpty() && serviceState.mPendingAppLinkCommand.isEmpty()) ? false : true;
        if (serviceState.mService == null && shouldBind) {
            if (serviceState.mBound) {
                return;
            }
            android.content.Intent i = new android.content.Intent("android.media.tv.ad.TvAdService").setComponent(component);
            serviceState.mBound = this.mContext.bindServiceAsUser(i, serviceState.mConnection, 33554433, new android.os.UserHandle(userId));
            return;
        }
        if (serviceState.mService != null && !shouldBind) {
            this.mContext.unbindService(serviceState.mConnection);
            userState.mAdServiceStateMap.remove(component);
        }
    }

    private static final class UserState {
        private final android.os.RemoteCallbackList<android.media.tv.ad.ITvAdManagerCallback> mAdCallbacks;
        private java.util.Map<java.lang.String, com.android.server.tv.interactive.TvInteractiveAppManagerService.TvAdServiceState> mAdServiceMap;
        private final java.util.Map<android.content.ComponentName, com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState> mAdServiceStateMap;
        private final java.util.Map<android.os.IBinder, com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState> mAdSessionStateMap;
        private final java.util.List<android.media.tv.interactive.AppLinkInfo> mAppLinkInfoList;
        private final android.os.RemoteCallbackList<android.media.tv.interactive.ITvInteractiveAppManagerCallback> mCallbacks;
        private final java.util.Map<android.os.IBinder, com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState> mClientStateMap;
        private java.util.Map<java.lang.String, com.android.server.tv.interactive.TvInteractiveAppManagerService.TvInteractiveAppState> mIAppMap;
        private final java.util.Set<java.lang.String> mPackageSet;
        private final java.util.Map<android.content.ComponentName, com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState> mServiceStateMap;
        private final java.util.Map<android.os.IBinder, com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState> mSessionStateMap;
        private final int mUserId;

        private UserState(int userId) {
            this.mAdServiceStateMap = new java.util.HashMap();
            this.mAdSessionStateMap = new java.util.HashMap();
            this.mIAppMap = new java.util.HashMap();
            this.mAdServiceMap = new java.util.HashMap();
            this.mClientStateMap = new java.util.HashMap();
            this.mServiceStateMap = new java.util.HashMap();
            this.mSessionStateMap = new java.util.HashMap();
            this.mPackageSet = new java.util.HashSet();
            this.mAppLinkInfoList = new java.util.ArrayList();
            this.mAdCallbacks = new android.os.RemoteCallbackList<>();
            this.mCallbacks = new android.os.RemoteCallbackList<>();
            this.mUserId = userId;
        }
    }

    private static final class TvInteractiveAppState {
        private android.content.ComponentName mComponentName;
        private int mIAppNumber;
        private java.lang.String mIAppServiceId;
        private android.media.tv.interactive.TvInteractiveAppServiceInfo mInfo;
        private int mUid;

        private TvInteractiveAppState() {
        }
    }

    private static final class TvAdServiceState {
        private int mAdNumber;
        private java.lang.String mAdServiceId;
        private android.content.ComponentName mComponentName;
        private android.media.tv.ad.TvAdServiceInfo mInfo;
        private int mUid;

        private TvAdServiceState() {
        }
    }

    private final class SessionState implements android.os.IBinder.DeathRecipient {
        private final int mCallingPid;
        private final int mCallingUid;
        private final android.media.tv.interactive.ITvInteractiveAppClient mClient;
        private final android.content.ComponentName mComponent;
        private final java.lang.String mIAppServiceId;
        private final int mSeq;
        private android.media.tv.interactive.ITvInteractiveAppSession mSession;
        private final android.os.IBinder mSessionToken;
        private final int mType;
        private final int mUserId;

        private SessionState(android.os.IBinder sessionToken, java.lang.String iAppServiceId, int type, android.content.ComponentName componentName, android.media.tv.interactive.ITvInteractiveAppClient client, int seq, int callingUid, int callingPid, int userId) {
            this.mSessionToken = sessionToken;
            this.mIAppServiceId = iAppServiceId;
            this.mComponent = componentName;
            this.mType = type;
            this.mClient = client;
            this.mSeq = seq;
            this.mCallingUid = callingUid;
            this.mCallingPid = callingPid;
            this.mUserId = userId;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                this.mSession = null;
                com.android.server.tv.interactive.TvInteractiveAppManagerService.this.clearSessionAndNotifyClientLocked(this);
            }
        }
    }

    private final class AdSessionState implements android.os.IBinder.DeathRecipient {
        private final java.lang.String mAdServiceId;
        private final int mCallingPid;
        private final int mCallingUid;
        private final android.media.tv.ad.ITvAdClient mClient;
        private final android.content.ComponentName mComponent;
        private final int mSeq;
        private android.media.tv.ad.ITvAdSession mSession;
        private final android.os.IBinder mSessionToken;
        private final java.lang.String mType;
        private final int mUserId;

        private AdSessionState(android.os.IBinder sessionToken, java.lang.String serviceId, java.lang.String type, android.content.ComponentName componentName, android.media.tv.ad.ITvAdClient client, int seq, int callingUid, int callingPid, int userId) {
            this.mSessionToken = sessionToken;
            this.mAdServiceId = serviceId;
            this.mType = type;
            this.mComponent = componentName;
            this.mClient = client;
            this.mSeq = seq;
            this.mCallingUid = callingUid;
            this.mCallingPid = callingPid;
            this.mUserId = userId;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                this.mSession = null;
                com.android.server.tv.interactive.TvInteractiveAppManagerService.this.clearAdSessionAndNotifyClientLocked(this);
            }
        }
    }

    private final class ClientState implements android.os.IBinder.DeathRecipient {
        private android.os.IBinder mClientToken;
        private final java.util.List<android.os.IBinder> mSessionTokens = new java.util.ArrayList();
        private final int mUserId;

        ClientState(android.os.IBinder clientToken, int userId) {
            this.mClientToken = clientToken;
            this.mUserId = userId;
        }

        public boolean isEmpty() {
            return this.mSessionTokens.isEmpty();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(this.mUserId);
                com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState clientState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState) userState.mClientStateMap.get(this.mClientToken);
                if (clientState != null) {
                    while (clientState.mSessionTokens.size() > 0) {
                        android.os.IBinder sessionToken = clientState.mSessionTokens.get(0);
                        com.android.server.tv.interactive.TvInteractiveAppManagerService.this.releaseSessionLocked(sessionToken, 1000, this.mUserId);
                        if (clientState.mSessionTokens.contains(sessionToken)) {
                            com.android.server.utils.Slogf.d(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "remove sessionToken " + sessionToken + " for " + this.mClientToken);
                            clientState.mSessionTokens.remove(sessionToken);
                        }
                    }
                }
                this.mClientToken = null;
            }
        }
    }

    private final class ServiceState {
        private boolean mBound;
        private com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceCallback mCallback;
        private final android.content.ComponentName mComponent;
        private final android.content.ServiceConnection mConnection;
        private final java.lang.String mIAppServiceId;
        private final java.util.List<android.os.Bundle> mPendingAppLinkCommand;
        private final java.util.List<android.util.Pair<android.media.tv.interactive.AppLinkInfo, java.lang.Boolean>> mPendingAppLinkInfo;
        private boolean mReconnecting;
        private android.media.tv.interactive.ITvInteractiveAppService mService;
        private final java.util.List<android.os.IBinder> mSessionTokens;

        private ServiceState(android.content.ComponentName component, java.lang.String tias, int userId) {
            this.mSessionTokens = new java.util.ArrayList();
            this.mPendingAppLinkInfo = new java.util.ArrayList();
            this.mPendingAppLinkCommand = new java.util.ArrayList();
            this.mComponent = component;
            this.mConnection = new com.android.server.tv.interactive.TvInteractiveAppManagerService.InteractiveAppServiceConnection(component, userId);
            this.mIAppServiceId = tias;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addPendingAppLink(android.media.tv.interactive.AppLinkInfo info, boolean register) {
            this.mPendingAppLinkInfo.add(android.util.Pair.create(info, java.lang.Boolean.valueOf(register)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addPendingAppLinkCommand(android.os.Bundle command) {
            this.mPendingAppLinkCommand.add(command);
        }
    }

    private final class AdServiceState {
        private final java.lang.String mAdServiceId;
        private boolean mBound;
        private com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceCallback mCallback;
        private final android.content.ComponentName mComponent;
        private final android.content.ServiceConnection mConnection;
        private final java.util.List<android.os.Bundle> mPendingAppLinkCommand;
        private boolean mReconnecting;
        private android.media.tv.ad.ITvAdService mService;
        private final java.util.List<android.os.IBinder> mSessionTokens;

        private AdServiceState(android.content.ComponentName component, java.lang.String tasId, int userId) {
            this.mSessionTokens = new java.util.ArrayList();
            this.mPendingAppLinkCommand = new java.util.ArrayList();
            this.mComponent = component;
            this.mConnection = new com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceConnection(component, userId);
            this.mAdServiceId = tasId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addPendingAppLinkCommand(android.os.Bundle command) {
            this.mPendingAppLinkCommand.add(command);
        }
    }

    private final class InteractiveAppServiceConnection implements android.content.ServiceConnection {
        private final android.content.ComponentName mComponent;
        private final int mUserId;

        private InteractiveAppServiceConnection(android.content.ComponentName component, int userId) {
            this.mComponent = component;
            this.mUserId = userId;
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0061 A[Catch: all -> 0x0166, TryCatch #3 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:27:0x009d, B:35:0x00c4, B:36:0x00c8, B:37:0x00c9, B:39:0x00d3, B:40:0x00db, B:42:0x00e1, B:44:0x00f5, B:52:0x011c, B:53:0x0120, B:54:0x0121, B:55:0x012e, B:57:0x0134, B:59:0x0148, B:61:0x014c, B:62:0x0150, B:64:0x0156, B:65:0x0164, B:22:0x0079, B:24:0x0083, B:26:0x009a, B:25:0x008f, B:32:0x00a4, B:43:0x00eb, B:49:0x00fc), top: B:74:0x0007, inners: #0, #2, #4 }] */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00c9 A[Catch: all -> 0x0166, TryCatch #3 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:27:0x009d, B:35:0x00c4, B:36:0x00c8, B:37:0x00c9, B:39:0x00d3, B:40:0x00db, B:42:0x00e1, B:44:0x00f5, B:52:0x011c, B:53:0x0120, B:54:0x0121, B:55:0x012e, B:57:0x0134, B:59:0x0148, B:61:0x014c, B:62:0x0150, B:64:0x0156, B:65:0x0164, B:22:0x0079, B:24:0x0083, B:26:0x009a, B:25:0x008f, B:32:0x00a4, B:43:0x00eb, B:49:0x00fc), top: B:74:0x0007, inners: #0, #2, #4 }] */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00d3 A[Catch: all -> 0x0166, TryCatch #3 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:27:0x009d, B:35:0x00c4, B:36:0x00c8, B:37:0x00c9, B:39:0x00d3, B:40:0x00db, B:42:0x00e1, B:44:0x00f5, B:52:0x011c, B:53:0x0120, B:54:0x0121, B:55:0x012e, B:57:0x0134, B:59:0x0148, B:61:0x014c, B:62:0x0150, B:64:0x0156, B:65:0x0164, B:22:0x0079, B:24:0x0083, B:26:0x009a, B:25:0x008f, B:32:0x00a4, B:43:0x00eb, B:49:0x00fc), top: B:74:0x0007, inners: #0, #2, #4 }] */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0134 A[Catch: all -> 0x0166, TryCatch #3 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:27:0x009d, B:35:0x00c4, B:36:0x00c8, B:37:0x00c9, B:39:0x00d3, B:40:0x00db, B:42:0x00e1, B:44:0x00f5, B:52:0x011c, B:53:0x0120, B:54:0x0121, B:55:0x012e, B:57:0x0134, B:59:0x0148, B:61:0x014c, B:62:0x0150, B:64:0x0156, B:65:0x0164, B:22:0x0079, B:24:0x0083, B:26:0x009a, B:25:0x008f, B:32:0x00a4, B:43:0x00eb, B:49:0x00fc), top: B:74:0x0007, inners: #0, #2, #4 }] */
        /* JADX WARN: Removed duplicated region for block: B:64:0x0156 A[Catch: all -> 0x0166, LOOP:3: B:62:0x0150->B:64:0x0156, LOOP_END, TryCatch #3 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:27:0x009d, B:35:0x00c4, B:36:0x00c8, B:37:0x00c9, B:39:0x00d3, B:40:0x00db, B:42:0x00e1, B:44:0x00f5, B:52:0x011c, B:53:0x0120, B:54:0x0121, B:55:0x012e, B:57:0x0134, B:59:0x0148, B:61:0x014c, B:62:0x0150, B:64:0x0156, B:65:0x0164, B:22:0x0079, B:24:0x0083, B:26:0x009a, B:25:0x008f, B:32:0x00a4, B:43:0x00eb, B:49:0x00fc), top: B:74:0x0007, inners: #0, #2, #4 }] */
        @Override // android.content.ServiceConnection
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onServiceConnected(android.content.ComponentName r12, android.os.IBinder r13) {
            /*
                Method dump skipped, instruction units count: 361
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.interactive.TvInteractiveAppManagerService.InteractiveAppServiceConnection.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName component) {
            if (!this.mComponent.equals(component)) {
                throw new java.lang.IllegalArgumentException("Mismatched ComponentName: " + this.mComponent + " (expected), " + component + " (actual).");
            }
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(this.mUserId);
                com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState) userState.mServiceStateMap.get(this.mComponent);
                if (serviceState != null) {
                    serviceState.mReconnecting = true;
                    serviceState.mBound = false;
                    serviceState.mService = null;
                    serviceState.mCallback = null;
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.abortPendingCreateSessionRequestsLocked(serviceState, null, this.mUserId);
                }
            }
        }
    }

    private final class AdServiceConnection implements android.content.ServiceConnection {
        private final android.content.ComponentName mComponent;
        private final int mUserId;

        private AdServiceConnection(android.content.ComponentName component, int userId) {
            this.mComponent = component;
            this.mUserId = userId;
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0061 A[Catch: all -> 0x00f4, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:23:0x0083, B:31:0x00aa, B:32:0x00ae, B:33:0x00af, B:34:0x00bc, B:36:0x00c2, B:38:0x00d6, B:40:0x00da, B:41:0x00de, B:43:0x00e4, B:44:0x00f2, B:22:0x0079, B:28:0x008a), top: B:49:0x0007, inners: #1, #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:36:0x00c2 A[Catch: all -> 0x00f4, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:23:0x0083, B:31:0x00aa, B:32:0x00ae, B:33:0x00af, B:34:0x00bc, B:36:0x00c2, B:38:0x00d6, B:40:0x00da, B:41:0x00de, B:43:0x00e4, B:44:0x00f2, B:22:0x0079, B:28:0x008a), top: B:49:0x0007, inners: #1, #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:43:0x00e4 A[Catch: all -> 0x00f4, LOOP:2: B:41:0x00de->B:43:0x00e4, LOOP_END, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0035, B:12:0x0043, B:15:0x0050, B:16:0x0057, B:18:0x0061, B:19:0x0069, B:21:0x006f, B:23:0x0083, B:31:0x00aa, B:32:0x00ae, B:33:0x00af, B:34:0x00bc, B:36:0x00c2, B:38:0x00d6, B:40:0x00da, B:41:0x00de, B:43:0x00e4, B:44:0x00f2, B:22:0x0079, B:28:0x008a), top: B:49:0x0007, inners: #1, #2 }] */
        @Override // android.content.ServiceConnection
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onServiceConnected(android.content.ComponentName r12, android.os.IBinder r13) {
            /*
                Method dump skipped, instruction units count: 247
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceConnection.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName component) {
            if (!this.mComponent.equals(component)) {
                throw new java.lang.IllegalArgumentException("Mismatched ComponentName: " + this.mComponent + " (expected), " + component + " (actual).");
            }
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(this.mUserId);
                com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState serviceState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.AdServiceState) userState.mAdServiceStateMap.get(this.mComponent);
                if (serviceState != null) {
                    serviceState.mReconnecting = true;
                    serviceState.mBound = false;
                    serviceState.mService = null;
                    serviceState.mCallback = null;
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.abortPendingCreateAdSessionRequestsLocked(serviceState, null, this.mUserId);
                }
            }
        }
    }

    private final class ServiceCallback extends android.media.tv.interactive.ITvInteractiveAppServiceCallback.Stub {
        private final android.content.ComponentName mComponent;
        private final int mUserId;

        ServiceCallback(android.content.ComponentName component, int userId) {
            this.mComponent = component;
            this.mUserId = userId;
        }

        public void onStateChanged(int type, int state, int error) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.ServiceState serviceState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getServiceStateLocked(this.mComponent, this.mUserId);
                    java.lang.String iAppServiceId = serviceState.mIAppServiceId;
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getUserStateLocked(this.mUserId);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.notifyStateChangedLocked(userState, iAppServiceId, type, state, error);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private final class AdServiceCallback extends android.media.tv.ad.ITvAdServiceCallback.Stub {
        private final android.content.ComponentName mComponent;
        private final int mUserId;

        AdServiceCallback(android.content.ComponentName component, int userId) {
            this.mComponent = component;
            this.mUserId = userId;
        }
    }

    private final class SessionCallback extends android.media.tv.interactive.ITvInteractiveAppSessionCallback.Stub {
        private final android.view.InputChannel[] mInputChannels;
        private final com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState mSessionState;

        SessionCallback(com.android.server.tv.interactive.TvInteractiveAppManagerService.SessionState sessionState, android.view.InputChannel[] channels) {
            this.mSessionState = sessionState;
            this.mInputChannels = channels;
        }

        public void onSessionCreated(android.media.tv.interactive.ITvInteractiveAppSession session) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                this.mSessionState.mSession = session;
                if (session != null && addSessionTokenToClientStateLocked(session)) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.sendSessionTokenToClientLocked(this.mSessionState.mClient, this.mSessionState.mIAppServiceId, this.mSessionState.mSessionToken, this.mInputChannels[0], this.mSessionState.mSeq);
                } else {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.removeSessionStateLocked(this.mSessionState.mSessionToken, this.mSessionState.mUserId);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.sendSessionTokenToClientLocked(this.mSessionState.mClient, this.mSessionState.mIAppServiceId, null, null, this.mSessionState.mSeq);
                }
                this.mInputChannels[0].dispose();
            }
        }

        public void onLayoutSurface(int left, int top, int right, int bottom) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onLayoutSurface(left, top, right, bottom, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onLayoutSurface", e);
                }
            }
        }

        public void onBroadcastInfoRequest(android.media.tv.BroadcastInfoRequest request) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onBroadcastInfoRequest(request, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onBroadcastInfoRequest", e);
                }
            }
        }

        public void onRemoveBroadcastInfo(int requestId) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRemoveBroadcastInfo(requestId, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRemoveBroadcastInfo", e);
                }
            }
        }

        public void onCommandRequest(java.lang.String cmdType, android.os.Bundle parameters) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onCommandRequest(cmdType, parameters, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onCommandRequest", e);
                }
            }
        }

        public void onTimeShiftCommandRequest(java.lang.String cmdType, android.os.Bundle parameters) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onTimeShiftCommandRequest(cmdType, parameters, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onTimeShiftCommandRequest", e);
                }
            }
        }

        public void onSetVideoBounds(android.graphics.Rect rect) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onSetVideoBounds(rect, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onSetVideoBounds", e);
                }
            }
        }

        public void onRequestCurrentVideoBounds() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCurrentVideoBounds(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCurrentVideoBounds", e);
                }
            }
        }

        public void onRequestCurrentChannelUri() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCurrentChannelUri(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCurrentChannelUri", e);
                }
            }
        }

        public void onRequestCurrentChannelLcn() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCurrentChannelLcn(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCurrentChannelLcn", e);
                }
            }
        }

        public void onRequestStreamVolume() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestStreamVolume(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestStreamVolume", e);
                }
            }
        }

        public void onRequestTrackInfoList() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestTrackInfoList(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestTrackInfoList", e);
                }
            }
        }

        public void onRequestSelectedTrackInfo() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestSelectedTrackInfo(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestSelectedTrackInfo", e);
                }
            }
        }

        public void onRequestCurrentTvInputId() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCurrentTvInputId(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCurrentTvInputId", e);
                }
            }
        }

        public void onRequestTimeShiftMode() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestTimeShiftMode(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestTimeShiftMode", e);
                }
            }
        }

        public void onRequestAvailableSpeeds() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestAvailableSpeeds(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestAvailableSpeeds", e);
                }
            }
        }

        public void onRequestStartRecording(java.lang.String requestId, android.net.Uri programUri) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestStartRecording(requestId, programUri, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestStartRecording", e);
                }
            }
        }

        public void onRequestStopRecording(java.lang.String recordingId) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestStopRecording(recordingId, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestStopRecording", e);
                }
            }
        }

        public void onRequestScheduleRecording(java.lang.String requestId, java.lang.String inputId, android.net.Uri channelUri, android.net.Uri programUri, android.os.Bundle params) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestScheduleRecording(requestId, inputId, channelUri, programUri, params, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestScheduleRecording", e);
                }
            }
        }

        public void onRequestScheduleRecording2(java.lang.String requestId, java.lang.String inputId, android.net.Uri channelUri, long start, long duration, int repeat, android.os.Bundle params) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestScheduleRecording2(requestId, inputId, channelUri, start, duration, repeat, params, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestScheduleRecording2", e);
                }
            }
        }

        public void onSetTvRecordingInfo(java.lang.String recordingId, android.media.tv.TvRecordingInfo recordingInfo) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onSetTvRecordingInfo(recordingId, recordingInfo, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onSetTvRecordingInfo", e);
                }
            }
        }

        public void onRequestTvRecordingInfo(java.lang.String recordingId) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestTvRecordingInfo(recordingId, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestTvRecordingInfo", e);
                }
            }
        }

        public void onRequestTvRecordingInfoList(int type) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestTvRecordingInfoList(type, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestTvRecordingInfoList", e);
                }
            }
        }

        public void onRequestSigning(java.lang.String id, java.lang.String algorithm, java.lang.String alias, byte[] data) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestSigning(id, algorithm, alias, data, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestSigning", e);
                }
            }
        }

        public void onRequestSigning2(java.lang.String id, java.lang.String algorithm, java.lang.String host, int port, byte[] data) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestSigning2(id, algorithm, host, port, data, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestSigning", e);
                }
            }
        }

        public void onRequestCertificate(java.lang.String host, int port) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCertificate(host, port, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCertificate", e);
                }
            }
        }

        public void onAdRequest(android.media.tv.AdRequest request) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onAdRequest(request, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onAdRequest", e);
                }
            }
        }

        public void onSessionStateChanged(int state, int err) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onSessionStateChanged(state, err, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onSessionStateChanged", e);
                }
            }
        }

        public void onBiInteractiveAppCreated(android.net.Uri biIAppUri, java.lang.String biIAppId) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onBiInteractiveAppCreated(biIAppUri, biIAppId, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onBiInteractiveAppCreated", e);
                }
            }
        }

        public void onTeletextAppStateChanged(int state) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onTeletextAppStateChanged(state, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onTeletextAppStateChanged", e);
                }
            }
        }

        public void onAdBufferReady(android.media.tv.AdBuffer buffer) {
            android.os.SharedMemory sharedMemory;
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession != null) {
                    try {
                        if (this.mSessionState.mClient != null) {
                            try {
                                this.mSessionState.mClient.onAdBufferReady(buffer, this.mSessionState.mSeq);
                            } catch (android.os.RemoteException e) {
                                com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onAdBuffer", e);
                                if (buffer != null) {
                                    sharedMemory = buffer.getSharedMemory();
                                }
                            }
                            if (buffer != null) {
                                sharedMemory = buffer.getSharedMemory();
                                sharedMemory.close();
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        if (buffer != null) {
                            buffer.getSharedMemory().close();
                        }
                        throw th;
                    }
                }
            }
        }

        private boolean addSessionTokenToClientStateLocked(android.media.tv.interactive.ITvInteractiveAppSession session) {
            try {
                session.asBinder().linkToDeath(this.mSessionState, 0);
                android.os.IBinder clientToken = this.mSessionState.mClient.asBinder();
                com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(this.mSessionState.mUserId);
                com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState clientState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState) userState.mClientStateMap.get(clientToken);
                if (clientState == null) {
                    clientState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.new ClientState(clientToken, this.mSessionState.mUserId);
                    try {
                        clientToken.linkToDeath(clientState, 0);
                        userState.mClientStateMap.put(clientToken, clientState);
                    } catch (android.os.RemoteException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "client process has already died", e);
                        return false;
                    }
                }
                clientState.mSessionTokens.add(this.mSessionState.mSessionToken);
                return true;
            } catch (android.os.RemoteException e2) {
                com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "session process has already died", e2);
                return false;
            }
        }
    }

    private final class AdSessionCallback extends android.media.tv.ad.ITvAdSessionCallback.Stub {
        private final android.view.InputChannel[] mInputChannels;
        private final com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState mSessionState;

        AdSessionCallback(com.android.server.tv.interactive.TvInteractiveAppManagerService.AdSessionState sessionState, android.view.InputChannel[] channels) {
            this.mSessionState = sessionState;
            this.mInputChannels = channels;
        }

        public void onSessionCreated(android.media.tv.ad.ITvAdSession session) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                this.mSessionState.mSession = session;
                if (session != null && addAdSessionTokenToClientStateLocked(session)) {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.sendAdSessionTokenToClientLocked(this.mSessionState.mClient, this.mSessionState.mAdServiceId, this.mSessionState.mSessionToken, this.mInputChannels[0], this.mSessionState.mSeq);
                } else {
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.removeAdSessionStateLocked(this.mSessionState.mSessionToken, this.mSessionState.mUserId);
                    com.android.server.tv.interactive.TvInteractiveAppManagerService.this.sendAdSessionTokenToClientLocked(this.mSessionState.mClient, this.mSessionState.mAdServiceId, null, null, this.mSessionState.mSeq);
                }
                this.mInputChannels[0].dispose();
            }
        }

        public void onLayoutSurface(int left, int top, int right, int bottom) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onLayoutSurface(left, top, right, bottom, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onLayoutSurface", e);
                }
            }
        }

        public void onRequestCurrentVideoBounds() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCurrentVideoBounds(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCurrentVideoBounds", e);
                }
            }
        }

        public void onRequestCurrentChannelUri() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCurrentChannelUri(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCurrentChannelUri", e);
                }
            }
        }

        public void onRequestTrackInfoList() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestTrackInfoList(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestTrackInfoList", e);
                }
            }
        }

        public void onRequestCurrentTvInputId() {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestCurrentTvInputId(this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestCurrentTvInputId", e);
                }
            }
        }

        public void onRequestSigning(java.lang.String id, java.lang.String algorithm, java.lang.String alias, byte[] data) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onRequestSigning(id, algorithm, alias, data, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onRequestSigning", e);
                }
            }
        }

        public void onTvAdSessionData(java.lang.String type, android.os.Bundle data) {
            synchronized (com.android.server.tv.interactive.TvInteractiveAppManagerService.this.mLock) {
                if (this.mSessionState.mSession == null || this.mSessionState.mClient == null) {
                    return;
                }
                try {
                    this.mSessionState.mClient.onTvAdSessionData(type, data, this.mSessionState.mSeq);
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "error in onTvAdSessionData", e);
                }
            }
        }

        private boolean addAdSessionTokenToClientStateLocked(android.media.tv.ad.ITvAdSession session) {
            try {
                session.asBinder().linkToDeath(this.mSessionState, 0);
                android.os.IBinder clientToken = this.mSessionState.mClient.asBinder();
                com.android.server.tv.interactive.TvInteractiveAppManagerService.UserState userState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.getOrCreateUserStateLocked(this.mSessionState.mUserId);
                com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState clientState = (com.android.server.tv.interactive.TvInteractiveAppManagerService.ClientState) userState.mClientStateMap.get(clientToken);
                if (clientState == null) {
                    clientState = com.android.server.tv.interactive.TvInteractiveAppManagerService.this.new ClientState(clientToken, this.mSessionState.mUserId);
                    try {
                        clientToken.linkToDeath(clientState, 0);
                        userState.mClientStateMap.put(clientToken, clientState);
                    } catch (android.os.RemoteException e) {
                        com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "client process has already died", e);
                        return false;
                    }
                }
                clientState.mSessionTokens.add(this.mSessionState.mSessionToken);
                return true;
            } catch (android.os.RemoteException e2) {
                com.android.server.utils.Slogf.e(com.android.server.tv.interactive.TvInteractiveAppManagerService.TAG, "session process has already died", e2);
                return false;
            }
        }
    }

    private static class SessionNotFoundException extends java.lang.IllegalArgumentException {
        SessionNotFoundException(java.lang.String name) {
            super(name);
        }
    }

    private static class ClientPidNotFoundException extends java.lang.IllegalArgumentException {
        ClientPidNotFoundException(java.lang.String name) {
            super(name);
        }
    }
}
