package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
public final class TvInputManagerService extends com.android.server.SystemService {
    private static final int APP_TAG_SELF = 0;
    private static final boolean DEBUG = false;
    private static final java.lang.String DVB_DIRECTORY = "/dev/dvb";
    private static final java.lang.String PERMISSION_ACCESS_WATCHED_PROGRAMS = "com.android.providers.tv.permission.ACCESS_WATCHED_PROGRAMS";
    private static final java.lang.String TAG = "TvInputManagerService";
    private static final long UPDATE_HARDWARE_TIS_BINDING_DELAY_IN_MILLIS = 10000;
    private final android.app.ActivityManager mActivityManager;
    private final android.content.Context mContext;
    private int mCurrentUserId;
    private final java.util.List<java.lang.String> mExternalInputLoggingDeviceBrandNames;
    private final java.util.HashSet<java.lang.String> mExternalInputLoggingDeviceOnScreenDisplayNames;
    private boolean mExternalInputLoggingDisplayNameFilterEnabled;
    private final java.lang.Object mLock;
    private final com.android.server.tv.TvInputManagerService.MessageHandler mMessageHandler;
    private java.lang.String mOnScreenInputId;
    private com.android.server.tv.TvInputManagerService.SessionState mOnScreenSessionState;
    private final java.util.Set<java.lang.Integer> mRunningProfiles;
    private final java.util.Map<java.lang.String, com.android.server.tv.TvInputManagerService.SessionState> mSessionIdToSessionStateMap;
    private final com.android.server.tv.TvInputHardwareManager mTvInputHardwareManager;
    private final android.os.UserManager mUserManager;
    private final android.util.SparseArray<com.android.server.tv.TvInputManagerService.UserState> mUserStates;
    private static final java.util.regex.Pattern sFrontEndDevicePattern = java.util.regex.Pattern.compile("^dvb([0-9]+)\\.frontend([0-9]+)$");
    private static final java.util.regex.Pattern sAdapterDirPattern = java.util.regex.Pattern.compile("^adapter([0-9]+)$");
    private static final java.util.regex.Pattern sFrontEndInAdapterDirPattern = java.util.regex.Pattern.compile("^frontend([0-9]+)$");

    public TvInputManagerService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mCurrentUserId = 0;
        this.mOnScreenInputId = null;
        this.mOnScreenSessionState = null;
        this.mRunningProfiles = new java.util.HashSet();
        this.mUserStates = new android.util.SparseArray<>();
        this.mSessionIdToSessionStateMap = new java.util.HashMap();
        this.mExternalInputLoggingDisplayNameFilterEnabled = false;
        this.mExternalInputLoggingDeviceOnScreenDisplayNames = new java.util.HashSet<>();
        this.mExternalInputLoggingDeviceBrandNames = new java.util.ArrayList();
        this.mContext = context;
        this.mMessageHandler = new com.android.server.tv.TvInputManagerService.MessageHandler(this.mContext.getContentResolver(), com.android.server.IoThread.get().getLooper());
        this.mTvInputHardwareManager = new com.android.server.tv.TvInputHardwareManager(context, new com.android.server.tv.TvInputManagerService.HardwareListener());
        this.mActivityManager = (android.app.ActivityManager) getContext().getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
        this.mUserManager = (android.os.UserManager) getContext().getSystemService("user");
        synchronized (this.mLock) {
            getOrCreateUserStateLocked(this.mCurrentUserId);
        }
        initExternalInputLoggingConfigs();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("tv_input", new com.android.server.tv.TvInputManagerService.BinderService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            registerBroadcastReceivers();
        } else if (phase == 600) {
            synchronized (this.mLock) {
                buildTvInputListLocked(this.mCurrentUserId, null);
                buildTvContentRatingSystemListLocked(this.mCurrentUserId);
            }
        }
        this.mTvInputHardwareManager.onBootPhase(phase);
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            if (this.mCurrentUserId != user.getUserIdentifier()) {
                return;
            }
            buildTvInputListLocked(this.mCurrentUserId, null);
            buildTvContentRatingSystemListLocked(this.mCurrentUserId);
        }
    }

    private void initExternalInputLoggingConfigs() {
        this.mExternalInputLoggingDisplayNameFilterEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_supportPreRebootSecurityLogs);
        if (!this.mExternalInputLoggingDisplayNameFilterEnabled) {
            return;
        }
        java.lang.String[] deviceOnScreenDisplayNames = this.mContext.getResources().getStringArray(android.R.array.config_tether_bluetooth_regexs);
        java.lang.String[] deviceBrandNames = this.mContext.getResources().getStringArray(android.R.array.config_testLocationProviders);
        this.mExternalInputLoggingDeviceOnScreenDisplayNames.addAll(java.util.Arrays.asList(deviceOnScreenDisplayNames));
        this.mExternalInputLoggingDeviceBrandNames.addAll(java.util.Arrays.asList(deviceBrandNames));
    }

    private void registerBroadcastReceivers() {
        com.android.internal.content.PackageMonitor monitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.tv.TvInputManagerService.1
            private void buildTvInputList(java.lang.String[] packages) {
                int userId = getChangingUserId();
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    if (com.android.server.tv.TvInputManagerService.this.mCurrentUserId == userId || com.android.server.tv.TvInputManagerService.this.mRunningProfiles.contains(java.lang.Integer.valueOf(userId))) {
                        com.android.server.tv.TvInputManagerService.this.buildTvInputListLocked(userId, packages);
                        com.android.server.tv.TvInputManagerService.this.buildTvContentRatingSystemListLocked(userId);
                    }
                }
            }

            public void onPackageUpdateFinished(java.lang.String packageName, int uid) {
                buildTvInputList(new java.lang.String[]{packageName});
            }

            public void onPackagesAvailable(java.lang.String[] packages) {
                if (isReplacing()) {
                    buildTvInputList(packages);
                }
            }

            public void onPackagesUnavailable(java.lang.String[] packages) {
                if (isReplacing()) {
                    buildTvInputList(packages);
                }
            }

            public void onSomePackagesChanged() {
                if (isReplacing()) {
                    return;
                }
                buildTvInputList(null);
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
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.tv.TvInputManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if ("android.intent.action.USER_SWITCHED".equals(action)) {
                    com.android.server.tv.TvInputManagerService.this.switchUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                    return;
                }
                if ("android.intent.action.USER_REMOVED".equals(action)) {
                    com.android.server.tv.TvInputManagerService.this.removeUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                    return;
                }
                if ("android.intent.action.USER_STARTED".equals(action)) {
                    int userId = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    com.android.server.tv.TvInputManagerService.this.startUser(userId);
                } else if ("android.intent.action.USER_STOPPED".equals(action)) {
                    int userId2 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    com.android.server.tv.TvInputManagerService.this.stopUser(userId2);
                }
            }
        }, android.os.UserHandle.ALL, intentFilter, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasHardwarePermission(android.content.pm.PackageManager pm, android.content.ComponentName component) {
        return pm.checkPermission("android.permission.TV_INPUT_HARDWARE", component.getPackageName()) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void buildTvInputListLocked(int i, java.lang.String[] strArr) {
        int iIntValue;
        com.android.server.tv.TvInputManagerService.UserState orCreateUserStateLocked = getOrCreateUserStateLocked(i);
        orCreateUserStateLocked.packageSet.clear();
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        java.util.List listQueryIntentServicesAsUser = packageManager.queryIntentServicesAsUser(new android.content.Intent("android.media.tv.TvInputService"), 132, i);
        java.util.ArrayList<android.media.tv.TvInputInfo> arrayList = new java.util.ArrayList();
        java.util.Iterator it = listQueryIntentServicesAsUser.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.content.pm.ResolveInfo resolveInfo = (android.content.pm.ResolveInfo) it.next();
            android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (!"android.permission.BIND_TV_INPUT".equals(serviceInfo.permission)) {
                android.util.Slog.w(TAG, "Skipping TV input " + serviceInfo.name + ": it does not require the permission android.permission.BIND_TV_INPUT");
            } else {
                android.content.ComponentName componentName = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
                if (hasHardwarePermission(packageManager, componentName)) {
                    com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) orCreateUserStateLocked.serviceStateMap.get(componentName);
                    if (serviceState == null) {
                        orCreateUserStateLocked.serviceStateMap.put(componentName, new com.android.server.tv.TvInputManagerService.ServiceState(componentName, i));
                        updateServiceConnectionLocked(componentName, i);
                    } else {
                        arrayList.addAll(serviceState.hardwareInputMap.values());
                    }
                } else {
                    try {
                        arrayList.add(new android.media.tv.TvInputInfo.Builder(this.mContext, resolveInfo).build());
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(TAG, "failed to load TV input " + serviceInfo.name, e);
                    }
                }
                orCreateUserStateLocked.packageSet.add(serviceInfo.packageName);
            }
        }
        java.util.Collections.sort(arrayList, java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.tv.TvInputManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.media.tv.TvInputInfo) obj).getId();
            }
        }));
        java.util.HashMap map = new java.util.HashMap();
        android.util.ArrayMap arrayMap = new android.util.ArrayMap(map.size());
        for (android.media.tv.TvInputInfo tvInputInfo : arrayList) {
            java.lang.String id = tvInputInfo.getId();
            java.lang.Integer num = (java.lang.Integer) arrayMap.get(id);
            if (num == null) {
                java.lang.Integer num2 = 1;
                iIntValue = num2.intValue();
            } else {
                iIntValue = 1 + num.intValue();
            }
            java.lang.Integer numValueOf = java.lang.Integer.valueOf(iIntValue);
            arrayMap.put(id, numValueOf);
            com.android.server.tv.TvInputManagerService.TvInputState tvInputState = (com.android.server.tv.TvInputManagerService.TvInputState) orCreateUserStateLocked.inputMap.get(id);
            if (tvInputState == null) {
                tvInputState = new com.android.server.tv.TvInputManagerService.TvInputState();
            }
            tvInputState.info = tvInputInfo;
            tvInputState.uid = getInputUid(tvInputInfo);
            map.put(id, tvInputState);
            tvInputState.inputNumber = numValueOf.intValue();
        }
        for (java.lang.String str : map.keySet()) {
            if (!orCreateUserStateLocked.inputMap.containsKey(str)) {
                notifyInputAddedLocked(orCreateUserStateLocked, str);
            } else if (strArr != null) {
                android.content.ComponentName component = ((com.android.server.tv.TvInputManagerService.TvInputState) map.get(str)).info.getComponent();
                int length = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 < length) {
                        if (!component.getPackageName().equals(strArr[i2])) {
                            i2++;
                        } else {
                            updateServiceConnectionLocked(component, i);
                            notifyInputUpdatedLocked(orCreateUserStateLocked, str);
                            break;
                        }
                    }
                }
            }
        }
        for (java.lang.String str2 : orCreateUserStateLocked.inputMap.keySet()) {
            if (!map.containsKey(str2)) {
                com.android.server.tv.TvInputManagerService.ServiceState serviceState2 = (com.android.server.tv.TvInputManagerService.ServiceState) orCreateUserStateLocked.serviceStateMap.get(((com.android.server.tv.TvInputManagerService.TvInputState) orCreateUserStateLocked.inputMap.get(str2)).info.getComponent());
                if (serviceState2 != null) {
                    abortPendingCreateSessionRequestsLocked(serviceState2, str2, i);
                }
                notifyInputRemovedLocked(orCreateUserStateLocked, str2);
            }
        }
        orCreateUserStateLocked.inputMap.clear();
        orCreateUserStateLocked.inputMap = map;
    }

    private int getInputUid(android.media.tv.TvInputInfo info) {
        try {
            return getContext().getPackageManager().getApplicationInfo(info.getServiceInfo().packageName, 0).uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, "Unable to get UID for  " + info, e);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildTvContentRatingSystemListLocked(int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        userState.contentRatingSystemList.clear();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.content.Intent intent = new android.content.Intent("android.media.tv.action.QUERY_CONTENT_RATING_SYSTEMS");
        for (android.content.pm.ResolveInfo resolveInfo : pm.queryBroadcastReceivers(intent, 128)) {
            android.content.pm.ActivityInfo receiver = resolveInfo.activityInfo;
            android.os.Bundle metaData = receiver.metaData;
            if (metaData != null) {
                int xmlResId = metaData.getInt("android.media.tv.metadata.CONTENT_RATING_SYSTEMS");
                if (xmlResId == 0) {
                    android.util.Slog.w(TAG, "Missing meta-data 'android.media.tv.metadata.CONTENT_RATING_SYSTEMS' on receiver " + receiver.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + receiver.name);
                } else {
                    userState.contentRatingSystemList.add(android.media.tv.TvContentRatingSystemInfo.createTvContentRatingSystemInfo(xmlResId, receiver.applicationInfo));
                }
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
        buildTvInputListLocked(userId, null);
        buildTvContentRatingSystemListLocked(userId);
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
            buildTvInputListLocked(userId, null);
            buildTvContentRatingSystemListLocked(userId);
            this.mMessageHandler.obtainMessage(3, getContentResolverForUser(userId)).sendToTarget();
        }
    }

    private void releaseSessionOfUserLocked(int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getUserStateLocked(userId);
        if (userState == null) {
            return;
        }
        java.util.List<com.android.server.tv.TvInputManagerService.SessionState> sessionStatesToRelease = new java.util.ArrayList<>();
        for (com.android.server.tv.TvInputManagerService.SessionState sessionState : userState.sessionStateMap.values()) {
            if (sessionState.session != null && !sessionState.isRecordingSession) {
                sessionStatesToRelease.add(sessionState);
            }
        }
        boolean notifyInfoUpdated = false;
        for (com.android.server.tv.TvInputManagerService.SessionState sessionState2 : sessionStatesToRelease) {
            try {
                try {
                    sessionState2.session.release();
                    sessionState2.currentChannel = null;
                    if (sessionState2.isCurrent) {
                        sessionState2.isCurrent = false;
                        notifyInfoUpdated = true;
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "error in release", e);
                    if (notifyInfoUpdated) {
                    }
                }
                if (notifyInfoUpdated) {
                    notifyCurrentChannelInfosUpdatedLocked(userState);
                }
                clearSessionAndNotifyClientLocked(sessionState2);
            } catch (java.lang.Throwable th) {
                if (notifyInfoUpdated) {
                    notifyCurrentChannelInfosUpdatedLocked(userState);
                }
                throw th;
            }
        }
    }

    private void unbindServiceOfUserLocked(int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getUserStateLocked(userId);
        if (userState == null) {
            return;
        }
        java.util.Iterator<android.content.ComponentName> it = userState.serviceStateMap.keySet().iterator();
        while (it.hasNext()) {
            android.content.ComponentName component = it.next();
            com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(component);
            if (serviceState != null && serviceState.sessionTokens.isEmpty()) {
                if (serviceState.callback != null) {
                    try {
                        serviceState.service.unregisterCallback(serviceState.callback);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "error in unregisterCallback", e);
                    }
                }
                unbindService(serviceState);
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSessionAndNotifyClientLocked(com.android.server.tv.TvInputManagerService.SessionState state) {
        if (state.client != null) {
            try {
                state.client.onSessionReleased(state.seq);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "error in onSessionReleased", e);
            }
        }
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(state.userId);
        for (com.android.server.tv.TvInputManagerService.SessionState sessionState : userState.sessionStateMap.values()) {
            if (state.sessionToken == sessionState.hardwareSessionToken) {
                releaseSessionLocked(sessionState.sessionToken, 1000, state.userId);
                try {
                    sessionState.client.onSessionReleased(sessionState.seq);
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(TAG, "error in onSessionReleased", e2);
                }
            }
        }
        removeSessionStateLocked(state.sessionToken, state.userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUser(int userId) {
        synchronized (this.mLock) {
            com.android.server.tv.TvInputManagerService.UserState userState = getUserStateLocked(userId);
            if (userState == null) {
                return;
            }
            boolean notifyInfoUpdated = false;
            for (com.android.server.tv.TvInputManagerService.SessionState state : userState.sessionStateMap.values()) {
                if (state.session != null) {
                    try {
                        try {
                            state.session.release();
                            state.currentChannel = null;
                            if (state.isCurrent) {
                                state.isCurrent = false;
                                notifyInfoUpdated = true;
                            }
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(TAG, "error in release", e);
                            if (notifyInfoUpdated) {
                            }
                        }
                        if (notifyInfoUpdated) {
                            notifyCurrentChannelInfosUpdatedLocked(userState);
                        }
                    } catch (java.lang.Throwable th) {
                        if (notifyInfoUpdated) {
                            notifyCurrentChannelInfosUpdatedLocked(userState);
                        }
                        throw th;
                    }
                }
            }
            userState.sessionStateMap.clear();
            for (com.android.server.tv.TvInputManagerService.ServiceState serviceState : userState.serviceStateMap.values()) {
                if (serviceState.service != null) {
                    if (serviceState.callback != null) {
                        try {
                            serviceState.service.unregisterCallback(serviceState.callback);
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.e(TAG, "error in unregisterCallback", e2);
                        }
                    }
                    unbindService(serviceState);
                }
            }
            userState.serviceStateMap.clear();
            userState.inputMap.clear();
            userState.packageSet.clear();
            userState.contentRatingSystemList.clear();
            userState.clientStateMap.clear();
            userState.mCallbacks.kill();
            userState.mainSessionToken = null;
            this.mRunningProfiles.remove(java.lang.Integer.valueOf(userId));
            this.mUserStates.remove(userId);
            if (userId == this.mCurrentUserId) {
                switchUser(0);
            }
        }
    }

    private android.content.ContentResolver getContentResolverForUser(int userId) {
        android.content.Context context;
        android.os.UserHandle user = new android.os.UserHandle(userId);
        try {
            context = this.mContext.createPackageContextAsUser(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0, user);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "failed to create package context as user " + user);
            context = this.mContext;
        }
        return context.getContentResolver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.TvInputManagerService.UserState getOrCreateUserStateLocked(int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getUserStateLocked(userId);
        if (userState == null) {
            com.android.server.tv.TvInputManagerService.UserState userState2 = new com.android.server.tv.TvInputManagerService.UserState(this.mContext, userId);
            this.mUserStates.put(userId, userState2);
            return userState2;
        }
        return userState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.TvInputManagerService.ServiceState getServiceStateLocked(android.content.ComponentName component, int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(component);
        if (serviceState == null) {
            throw new java.lang.IllegalStateException("Service state not found for " + component + " (userId=" + userId + ")");
        }
        return serviceState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.TvInputManagerService.SessionState getSessionStateLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        return getSessionStateLocked(sessionToken, callingUid, userState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.TvInputManagerService.SessionState getSessionStateLocked(android.os.IBinder sessionToken, int callingUid, com.android.server.tv.TvInputManagerService.UserState userState) {
        com.android.server.tv.TvInputManagerService.SessionState sessionState = (com.android.server.tv.TvInputManagerService.SessionState) userState.sessionStateMap.get(sessionToken);
        if (sessionState == null) {
            throw new com.android.server.tv.TvInputManagerService.SessionNotFoundException("Session state not found for token " + sessionToken);
        }
        if (callingUid != 1000 && callingUid != sessionState.callingUid) {
            throw new java.lang.SecurityException("Illegal access to the session with token " + sessionToken + " from uid " + callingUid);
        }
        return sessionState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.tv.ITvInputSession getSessionLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        return getSessionLocked(getSessionStateLocked(sessionToken, callingUid, userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.tv.ITvInputSession getSessionLocked(com.android.server.tv.TvInputManagerService.SessionState sessionState) {
        android.media.tv.ITvInputSession session = sessionState.session;
        if (session == null) {
            throw new java.lang.IllegalStateException("Session not yet created for token " + sessionState.sessionToken);
        }
        return session;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int resolveCallingUserId(int callingPid, int callingUid, int requestedUserId, java.lang.String methodName) {
        return android.app.ActivityManager.handleIncomingUser(callingPid, callingUid, requestedUserId, false, false, methodName, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceConnectionLocked(android.content.ComponentName component, int userId) {
        boolean shouldBind;
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(component);
        if (serviceState == null) {
            return;
        }
        boolean z = false;
        if (serviceState.reconnecting) {
            if (!serviceState.sessionTokens.isEmpty()) {
                return;
            } else {
                serviceState.reconnecting = false;
            }
        }
        if (userId == this.mCurrentUserId || this.mRunningProfiles.contains(java.lang.Integer.valueOf(userId))) {
            if (!serviceState.sessionTokens.isEmpty() || (serviceState.isHardware && serviceState.neverConnected)) {
                z = true;
            }
            shouldBind = z;
        } else {
            shouldBind = !serviceState.sessionTokens.isEmpty();
        }
        if (shouldBind && !serviceState.bound) {
            bindService(serviceState, userId);
            return;
        }
        if (!shouldBind && serviceState.bound) {
            unbindService(serviceState);
            if (!serviceState.isHardware) {
                userState.serviceStateMap.remove(component);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abortPendingCreateSessionRequestsLocked(com.android.server.tv.TvInputManagerService.ServiceState serviceState, java.lang.String inputId, int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        java.util.List<com.android.server.tv.TvInputManagerService.SessionState> sessionsToAbort = new java.util.ArrayList<>();
        for (android.os.IBinder sessionToken : serviceState.sessionTokens) {
            com.android.server.tv.TvInputManagerService.SessionState sessionState = (com.android.server.tv.TvInputManagerService.SessionState) userState.sessionStateMap.get(sessionToken);
            if (sessionState.session == null && (inputId == null || sessionState.inputId.equals(inputId))) {
                sessionsToAbort.add(sessionState);
            }
        }
        for (com.android.server.tv.TvInputManagerService.SessionState sessionState2 : sessionsToAbort) {
            removeSessionStateLocked(sessionState2.sessionToken, sessionState2.userId);
            sendSessionTokenToClientLocked(sessionState2.client, sessionState2.inputId, null, null, sessionState2.seq);
        }
        if (!serviceState.isHardware) {
            updateServiceConnectionLocked(serviceState.component, userId);
        } else {
            updateHardwareServiceConnectionDelayed(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean createSessionInternalLocked(android.media.tv.ITvInputService service, android.os.IBinder sessionToken, int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.TvInputManagerService.SessionState sessionState = (com.android.server.tv.TvInputManagerService.SessionState) userState.sessionStateMap.get(sessionToken);
        android.view.InputChannel[] channels = android.view.InputChannel.openInputChannelPair(sessionToken.toString());
        com.android.server.tv.TvInputManagerService.SessionCallback sessionCallback = new com.android.server.tv.TvInputManagerService.SessionCallback(sessionState, channels);
        boolean created = true;
        try {
            if (!sessionState.isRecordingSession) {
                service.createSession(channels[1], sessionCallback, sessionState.inputId, sessionState.sessionId, sessionState.tvAppAttributionSource);
            } else {
                try {
                    service.createRecordingSession(sessionCallback, sessionState.inputId, sessionState.sessionId);
                } catch (android.os.RemoteException e) {
                    e = e;
                    android.util.Slog.e(TAG, "error in createSession", e);
                    sendSessionTokenToClientLocked(sessionState.client, sessionState.inputId, null, null, sessionState.seq);
                    created = false;
                }
            }
        } catch (android.os.RemoteException e2) {
            e = e2;
        }
        channels[1].dispose();
        return created;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSessionTokenToClientLocked(android.media.tv.ITvInputClient client, java.lang.String inputId, android.os.IBinder sessionToken, android.view.InputChannel channel, int seq) {
        try {
            client.onSessionCreated(inputId, sessionToken, channel, seq);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "error in onSessionCreated", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.TvInputManagerService.SessionState releaseSessionLocked(android.os.IBinder sessionToken, int callingUid, int userId) {
        com.android.server.tv.TvInputManagerService.SessionState sessionState = null;
        try {
            try {
                sessionState = getSessionStateLocked(sessionToken, callingUid, userId);
                com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
                if (sessionState.session != null) {
                    if (sessionToken == userState.mainSessionToken) {
                        setMainLocked(sessionToken, false, callingUid, userId);
                    }
                    sessionState.session.asBinder().unlinkToDeath(sessionState, 0);
                    sessionState.session.release();
                }
                sessionState.currentChannel = null;
                if (sessionState.isCurrent) {
                    sessionState.isCurrent = false;
                    notifyCurrentChannelInfosUpdatedLocked(userState);
                }
            } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                android.util.Slog.e(TAG, "error in releaseSession", e);
                if (sessionState != null) {
                }
            }
            if (this.mOnScreenSessionState == sessionState) {
                logExternalInputEvent(2, this.mOnScreenInputId, sessionState);
                this.mOnScreenInputId = null;
                this.mOnScreenSessionState = null;
            }
            removeSessionStateLocked(sessionToken, userId);
            return sessionState;
        } finally {
            if (sessionState != null) {
                sessionState.session = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSessionStateLocked(android.os.IBinder sessionToken, int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        if (sessionToken == userState.mainSessionToken) {
            userState.mainSessionToken = null;
        }
        com.android.server.tv.TvInputManagerService.SessionState sessionState = (com.android.server.tv.TvInputManagerService.SessionState) userState.sessionStateMap.remove(sessionToken);
        if (sessionState == null) {
            android.util.Slog.e(TAG, "sessionState null, no more remove session action!");
            return;
        }
        com.android.server.tv.TvInputManagerService.ClientState clientState = (com.android.server.tv.TvInputManagerService.ClientState) userState.clientStateMap.get(sessionState.client.asBinder());
        if (clientState != null) {
            clientState.sessionTokens.remove(sessionToken);
            if (clientState.isEmpty()) {
                userState.clientStateMap.remove(sessionState.client.asBinder());
                sessionState.client.asBinder().unlinkToDeath(clientState, 0);
            }
        }
        this.mSessionIdToSessionStateMap.remove(sessionState.sessionId);
        com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(sessionState.componentName);
        if (serviceState != null) {
            serviceState.sessionTokens.remove(sessionToken);
        }
        if (!serviceState.isHardware) {
            updateServiceConnectionLocked(sessionState.componentName, userId);
        } else {
            updateHardwareServiceConnectionDelayed(userId);
        }
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = sessionToken;
        args.arg2 = java.lang.Long.valueOf(java.lang.System.currentTimeMillis());
        this.mMessageHandler.obtainMessage(2, args).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMainLocked(android.os.IBinder sessionToken, boolean isMain, int callingUid, int userId) {
        try {
            com.android.server.tv.TvInputManagerService.SessionState sessionState = getSessionStateLocked(sessionToken, callingUid, userId);
            if (sessionState.hardwareSessionToken != null) {
                sessionState = getSessionStateLocked(sessionState.hardwareSessionToken, 1000, userId);
            }
            com.android.server.tv.TvInputManagerService.ServiceState serviceState = getServiceStateLocked(sessionState.componentName, userId);
            if (!serviceState.isHardware) {
                return;
            }
            android.media.tv.ITvInputSession session = getSessionLocked(sessionState);
            session.setMain(isMain);
            if (sessionState.isMainSession != isMain) {
                com.android.server.tv.TvInputManagerService.UserState userState = getUserStateLocked(userId);
                sessionState.isMainSession = isMain;
                notifyCurrentChannelInfosUpdatedLocked(userState);
            }
        } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
            android.util.Slog.e(TAG, "error in setMain", e);
        }
    }

    private void notifyInputAddedLocked(com.android.server.tv.TvInputManagerService.UserState userState, java.lang.String inputId) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onInputAdded(inputId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report added input to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    private void notifyInputRemovedLocked(com.android.server.tv.TvInputManagerService.UserState userState, java.lang.String inputId) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onInputRemoved(inputId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report removed input to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    private void notifyInputUpdatedLocked(com.android.server.tv.TvInputManagerService.UserState userState, java.lang.String inputId) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onInputUpdated(inputId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report updated input to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInputStateChangedLocked(com.android.server.tv.TvInputManagerService.UserState userState, java.lang.String inputId, int state, android.media.tv.ITvInputManagerCallback targetCallback) {
        if (targetCallback == null) {
            int n = userState.mCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    userState.mCallbacks.getBroadcastItem(i).onInputStateChanged(inputId, state);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "failed to report state change to callback", e);
                }
            }
            userState.mCallbacks.finishBroadcast();
            return;
        }
        try {
            targetCallback.onInputStateChanged(inputId, state);
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(TAG, "failed to report state change to callback", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCurrentChannelInfosUpdatedLocked(com.android.server.tv.TvInputManagerService.UserState userState) {
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                android.media.tv.ITvInputManagerCallback callback = userState.mCallbacks.getBroadcastItem(i);
                android.util.Pair<java.lang.Integer, java.lang.Integer> pidUid = (android.util.Pair) userState.callbackPidUidMap.get(callback);
                if (this.mContext.checkPermission("android.permission.ACCESS_TUNED_INFO", ((java.lang.Integer) pidUid.first).intValue(), ((java.lang.Integer) pidUid.second).intValue()) == 0) {
                    java.util.List<android.media.tv.TunedInfo> infos = getCurrentTunedInfosInternalLocked(userState, ((java.lang.Integer) pidUid.first).intValue(), ((java.lang.Integer) pidUid.second).intValue());
                    callback.onCurrentTunedInfosUpdated(infos);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report updated current channel infos to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTvInputInfoLocked(com.android.server.tv.TvInputManagerService.UserState userState, android.media.tv.TvInputInfo inputInfo) {
        java.lang.String inputId = inputInfo.getId();
        com.android.server.tv.TvInputManagerService.TvInputState inputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId);
        if (inputState == null) {
            android.util.Slog.e(TAG, "failed to set input info - unknown input id " + inputId);
            return;
        }
        boolean currentCecTvInputInfoUpdated = isCurrentCecTvInputInfoUpdate(userState, inputInfo);
        inputState.info = inputInfo;
        inputState.uid = getInputUid(inputInfo);
        com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(inputInfo.getComponent());
        if (serviceState != null && serviceState.isHardware) {
            serviceState.hardwareInputMap.put(inputInfo.getId(), inputInfo);
            this.mTvInputHardwareManager.updateInputInfo(inputInfo);
        }
        if (currentCecTvInputInfoUpdated) {
            logExternalInputEvent(4, this.mOnScreenInputId, this.mOnScreenSessionState);
        }
        int n = userState.mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                userState.mCallbacks.getBroadcastItem(i).onTvInputInfoUpdated(inputInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "failed to report updated input info to callback", e);
            }
        }
        userState.mCallbacks.finishBroadcast();
    }

    private boolean isCurrentCecTvInputInfoUpdate(com.android.server.tv.TvInputManagerService.UserState userState, android.media.tv.TvInputInfo newInputInfo) {
        com.android.server.tv.TvInputManagerService.TvInputState inputState;
        if (newInputInfo == null || newInputInfo.getId() == null || !newInputInfo.getId().equals(this.mOnScreenInputId) || newInputInfo.getHdmiDeviceInfo() == null || !newInputInfo.getHdmiDeviceInfo().isCecDevice() || (inputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(this.mOnScreenInputId)) == null || inputState.info == null || inputState.info.getHdmiDeviceInfo() == null || !inputState.info.getHdmiDeviceInfo().isCecDevice()) {
            return false;
        }
        java.lang.String newDisplayName = newInputInfo.getHdmiDeviceInfo().getDisplayName();
        java.lang.String currentDisplayName = inputState.info.getHdmiDeviceInfo().getDisplayName();
        int newVendorId = newInputInfo.getHdmiDeviceInfo().getVendorId();
        int currentVendorId = inputState.info.getHdmiDeviceInfo().getVendorId();
        return (android.text.TextUtils.equals(newDisplayName, currentDisplayName) && newVendorId == currentVendorId) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStateLocked(java.lang.String inputId, int state, int userId) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(userId);
        com.android.server.tv.TvInputManagerService.TvInputState inputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId);
        if (inputState == null) {
            android.util.Slog.e(TAG, "failed to setStateLocked - unknown input id " + inputId);
            return;
        }
        com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(inputState.info.getComponent());
        int oldState = inputState.state;
        inputState.state = state;
        if ((serviceState == null || !serviceState.reconnecting) && oldState != state) {
            if (inputId.equals(this.mOnScreenInputId)) {
                logExternalInputEvent(3, this.mOnScreenInputId, this.mOnScreenSessionState);
            } else if (this.mOnScreenInputId != null) {
                com.android.server.tv.TvInputManagerService.TvInputState currentInputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(this.mOnScreenInputId);
                android.media.tv.TvInputInfo currentInputInfo = null;
                if (currentInputState != null) {
                    currentInputInfo = currentInputState.info;
                }
                if (currentInputInfo != null && currentInputInfo.getHdmiDeviceInfo() != null && inputId.equals(currentInputInfo.getParentId())) {
                    logExternalInputEvent(3, inputId, this.mOnScreenSessionState);
                    if (state == 1) {
                        this.mOnScreenInputId = currentInputInfo.getParentId();
                    }
                }
            }
            notifyInputStateChangedLocked(userState, inputId, state, null);
        }
    }

    private final class BinderService extends android.media.tv.ITvInputManager.Stub {
        private BinderService() {
        }

        public java.util.List<android.media.tv.TvInputInfo> getTvInputList(int userId) {
            java.util.List<android.media.tv.TvInputInfo> inputList;
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getTvInputList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    inputList = new java.util.ArrayList<>();
                    for (com.android.server.tv.TvInputManagerService.TvInputState state : userState.inputMap.values()) {
                        inputList.add(state.info);
                    }
                }
                return inputList;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.media.tv.TvInputInfo getTvInputInfo(java.lang.String inputId, int userId) {
            android.media.tv.TvInputInfo tvInputInfo;
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getTvInputInfo");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.TvInputManagerService.TvInputState state = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId);
                    tvInputInfo = state == null ? null : state.info;
                }
                return tvInputInfo;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void updateTvInputInfo(android.media.tv.TvInputInfo inputInfo, int userId) {
            java.lang.String inputInfoPackageName = inputInfo.getServiceInfo().packageName;
            java.lang.String callingPackageName = getCallingPackageName();
            if (!android.text.TextUtils.equals(inputInfoPackageName, callingPackageName) && com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
                throw new java.lang.IllegalArgumentException("calling package " + callingPackageName + " is not allowed to change TvInputInfo for " + inputInfoPackageName);
            }
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "updateTvInputInfo");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.TvInputManagerService.this.updateTvInputInfoLocked(userState, inputInfo);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private java.lang.String getCallingPackageName() {
            java.lang.String[] packages = com.android.server.tv.TvInputManagerService.this.mContext.getPackageManager().getPackagesForUid(android.os.Binder.getCallingUid());
            if (packages != null && packages.length > 0) {
                return packages[0];
            }
            return "unknown";
        }

        public int getTvInputState(java.lang.String inputId, int userId) {
            int i;
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getTvInputState");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.TvInputManagerService.TvInputState state = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId);
                    i = state == null ? 0 : state.state;
                }
                return i;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<java.lang.String> getAvailableExtensionInterfaceNames(java.lang.String inputId, int userId) {
            com.android.server.tv.TvInputManagerService.ServiceState serviceState;
            ensureTisExtensionInterfacePermission();
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "getAvailableExtensionInterfaceNames");
            long identity = android.os.Binder.clearCallingIdentity();
            android.media.tv.ITvInputService service = null;
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.TvInputManagerService.TvInputState inputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId);
                    if (inputState != null && (serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(inputState.info.getComponent())) != null && serviceState.isHardware && serviceState.service != null) {
                        service = serviceState.service;
                    }
                }
                if (service != null) {
                    try {
                        java.util.List<java.lang.String> interfaces = new java.util.ArrayList<>();
                        for (java.lang.String name : com.android.internal.util.CollectionUtils.emptyIfNull(service.getAvailableExtensionInterfaceNames())) {
                            java.lang.String permission = service.getExtensionInterfacePermission(name);
                            if (permission == null || com.android.server.tv.TvInputManagerService.this.mContext.checkPermission(permission, callingPid, callingUid) == 0) {
                                interfaces.add(name);
                            }
                        }
                        return interfaces;
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in getAvailableExtensionInterfaceNames or getExtensionInterfacePermission", e);
                    }
                }
                return new java.util.ArrayList();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.os.IBinder getExtensionInterface(java.lang.String inputId, java.lang.String name, int userId) {
            com.android.server.tv.TvInputManagerService.ServiceState serviceState;
            ensureTisExtensionInterfacePermission();
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "getExtensionInterface");
            long identity = android.os.Binder.clearCallingIdentity();
            android.media.tv.ITvInputService service = null;
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    com.android.server.tv.TvInputManagerService.TvInputState inputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId);
                    if (inputState != null && (serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(inputState.info.getComponent())) != null && serviceState.isHardware && serviceState.service != null) {
                        service = serviceState.service;
                    }
                }
                if (service != null) {
                    try {
                        java.lang.String permission = service.getExtensionInterfacePermission(name);
                        if (permission == null || com.android.server.tv.TvInputManagerService.this.mContext.checkPermission(permission, callingPid, callingUid) == 0) {
                            return service.getExtensionInterface(name);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in getExtensionInterfacePermission or getExtensionInterface", e);
                    }
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return null;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<android.media.tv.TvContentRatingSystemInfo> getTvContentRatingSystemList(int userId) {
            java.util.List<android.media.tv.TvContentRatingSystemInfo> list;
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.READ_CONTENT_RATING_SYSTEMS") != 0) {
                throw new java.lang.SecurityException("The caller does not have permission to read content rating systems");
            }
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getTvContentRatingSystemList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    list = userState.contentRatingSystemList;
                }
                return list;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0042  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void sendTvInputNotifyIntent(android.content.Intent r12, int r13) {
            /*
                Method dump skipped, instruction units count: 286
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.TvInputManagerService.BinderService.sendTvInputNotifyIntent(android.content.Intent, int):void");
        }

        public void registerCallback(android.media.tv.ITvInputManagerCallback callback, int userId) {
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "registerCallback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    if (!userState.mCallbacks.register(callback)) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "client process has already died");
                    } else {
                        userState.callbackPidUidMap.put(callback, android.util.Pair.create(java.lang.Integer.valueOf(callingPid), java.lang.Integer.valueOf(callingUid)));
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unregisterCallback(android.media.tv.ITvInputManagerCallback callback, int userId) {
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "unregisterCallback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    userState.mCallbacks.unregister(callback);
                    userState.callbackPidUidMap.remove(callback);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean isParentalControlsEnabled(int userId) {
            boolean zIsParentalControlsEnabled;
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "isParentalControlsEnabled");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    zIsParentalControlsEnabled = userState.persistentDataStore.isParentalControlsEnabled();
                }
                return zIsParentalControlsEnabled;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setParentalControlsEnabled(boolean enabled, int userId) {
            ensureParentalControlsPermission();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "setParentalControlsEnabled");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    userState.persistentDataStore.setParentalControlsEnabled(enabled);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean isRatingBlocked(java.lang.String rating, int userId) {
            boolean zIsRatingBlocked;
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "isRatingBlocked");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    zIsRatingBlocked = userState.persistentDataStore.isRatingBlocked(android.media.tv.TvContentRating.unflattenFromString(rating));
                }
                return zIsRatingBlocked;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<java.lang.String> getBlockedRatings(int userId) {
            java.util.List<java.lang.String> ratings;
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "getBlockedRatings");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    ratings = new java.util.ArrayList<>();
                    for (android.media.tv.TvContentRating rating : userState.persistentDataStore.getBlockedRatings()) {
                        ratings.add(rating.flattenToString());
                    }
                }
                return ratings;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void addBlockedRating(java.lang.String rating, int userId) {
            ensureParentalControlsPermission();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "addBlockedRating");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    userState.persistentDataStore.addBlockedRating(android.media.tv.TvContentRating.unflattenFromString(rating));
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void removeBlockedRating(java.lang.String rating, int userId) {
            ensureParentalControlsPermission();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "removeBlockedRating");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    userState.persistentDataStore.removeBlockedRating(android.media.tv.TvContentRating.unflattenFromString(rating));
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private void ensureParentalControlsPermission() {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.MODIFY_PARENTAL_CONTROLS") != 0) {
                throw new java.lang.SecurityException("The caller does not have parental controls permission");
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x006f A[Catch: all -> 0x0055, TRY_ENTER, TryCatch #1 {all -> 0x0055, blocks: (B:7:0x0031, B:10:0x0043, B:11:0x0050, B:19:0x006f, B:20:0x0094, B:25:0x00b0, B:30:0x00e3, B:31:0x00f0), top: B:64:0x0031 }] */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0099 A[Catch: all -> 0x0172, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0172, blocks: (B:5:0x0029, B:16:0x005c, B:23:0x0099, B:28:0x00dd, B:34:0x00f5), top: B:62:0x0029 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void createSession(android.media.tv.ITvInputClient r31, java.lang.String r32, android.content.AttributionSource r33, boolean r34, int r35, int r36) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 390
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.TvInputManagerService.BinderService.createSession(android.media.tv.ITvInputClient, java.lang.String, android.content.AttributionSource, boolean, int, int):void");
        }

        public void releaseSession(android.os.IBinder sessionToken, int userId) {
            com.android.server.tv.TvInputManagerService.SessionState sessionState;
            com.android.server.tv.TvInputManagerService.UserState userState;
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "releaseSession");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    sessionState = com.android.server.tv.TvInputManagerService.this.releaseSessionLocked(sessionToken, callingUid, resolvedUserId);
                    userState = com.android.server.tv.TvInputManagerService.this.getUserStateLocked(userId);
                }
                if (sessionState != null) {
                    com.android.server.tv.TvInputManagerService.TvInputState tvInputState = com.android.server.tv.TvInputManagerService.getTvInputState(sessionState, userState);
                    com.android.server.tv.TvInputManagerService.this.logTuneStateChanged(4, sessionState, tvInputState);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setMainSession(android.os.IBinder sessionToken, int userId) {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.CHANGE_HDMI_CEC_ACTIVE_SOURCE") != 0) {
                throw new java.lang.SecurityException("The caller does not have CHANGE_HDMI_CEC_ACTIVE_SOURCE permission");
            }
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setMainSession");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    if (userState.mainSessionToken == sessionToken) {
                        return;
                    }
                    android.os.IBinder oldMainSessionToken = userState.mainSessionToken;
                    userState.mainSessionToken = sessionToken;
                    if (sessionToken != null) {
                        com.android.server.tv.TvInputManagerService.this.setMainLocked(sessionToken, true, callingUid, userId);
                    }
                    if (oldMainSessionToken != null) {
                        com.android.server.tv.TvInputManagerService.this.setMainLocked(oldMainSessionToken, false, 1000, userId);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setSurface(android.os.IBinder sessionToken, android.view.Surface surface, int userId) {
            boolean isVisible;
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setSurface");
            long identity = android.os.Binder.clearCallingIdentity();
            com.android.server.tv.TvInputManagerService.SessionState sessionState = null;
            com.android.server.tv.TvInputManagerService.UserState userState = null;
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        userState = com.android.server.tv.TvInputManagerService.this.getUserStateLocked(userId);
                        sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        if (sessionState.hardwareSessionToken == null) {
                            com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).setSurface(surface);
                        } else {
                            com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState.hardwareSessionToken, 1000, resolvedUserId).setSurface(surface);
                        }
                        isVisible = surface == null;
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in setSurface", e);
                    }
                    if (sessionState.isVisible != isVisible) {
                        sessionState.isVisible = isVisible;
                        com.android.server.tv.TvInputManagerService.this.notifyCurrentChannelInfosUpdatedLocked(userState);
                    }
                }
            } finally {
                if (surface != null) {
                    surface.release();
                }
                if (sessionState != null) {
                    state = surface != null ? 2 : 3;
                    com.android.server.tv.TvInputManagerService.this.logTuneStateChanged(state, sessionState, com.android.server.tv.TvInputManagerService.getTvInputState(sessionState, userState));
                }
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void dispatchSurfaceChanged(android.os.IBinder sessionToken, int format, int width, int height, int userId) {
            com.android.server.tv.TvInputManagerService.SessionState sessionState;
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "dispatchSurfaceChanged");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).dispatchSurfaceChanged(format, width, height);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in dispatchSurfaceChanged", e);
                    }
                    if (sessionState.hardwareSessionToken != null) {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState.hardwareSessionToken, 1000, resolvedUserId).dispatchSurfaceChanged(format, width, height);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setVolume(android.os.IBinder sessionToken, float volume, int userId) {
            com.android.server.tv.TvInputManagerService.SessionState sessionState;
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setVolume");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).setVolume(volume);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in setVolume", e);
                    }
                    if (sessionState.hardwareSessionToken != null) {
                        android.media.tv.ITvInputSession sessionLocked = com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState.hardwareSessionToken, 1000, resolvedUserId);
                        float f = 0.0f;
                        if (volume > 0.0f) {
                            f = 1.0f;
                        }
                        sessionLocked.setVolume(f);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void tune(android.os.IBinder sessionToken, android.net.Uri channelUri, android.os.Bundle params, int userId) {
            com.android.server.tv.TvInputManagerService.UserState userState;
            com.android.server.tv.TvInputManagerService.SessionState sessionState;
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "tune");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).tune(channelUri, params);
                        userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                        sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, userState);
                        if (!sessionState.isCurrent || !java.util.Objects.equals(sessionState.currentChannel, channelUri)) {
                            sessionState.isCurrent = true;
                            sessionState.currentChannel = channelUri;
                            com.android.server.tv.TvInputManagerService.this.notifyCurrentChannelInfosUpdatedLocked(userState);
                            if (!sessionState.isRecordingSession) {
                                java.lang.String sessionActualInputId = com.android.server.tv.TvInputManagerService.this.getSessionActualInputId(sessionState);
                                if (!android.text.TextUtils.equals(com.android.server.tv.TvInputManagerService.this.mOnScreenInputId, sessionActualInputId)) {
                                    com.android.server.tv.TvInputManagerService.this.logExternalInputEvent(1, sessionActualInputId, sessionState);
                                }
                                com.android.server.tv.TvInputManagerService.this.mOnScreenInputId = sessionActualInputId;
                                com.android.server.tv.TvInputManagerService.this.mOnScreenSessionState = sessionState;
                            }
                        }
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in tune", e);
                    }
                    if (android.media.tv.TvContract.isChannelUriForPassthroughInput(channelUri)) {
                        return;
                    }
                    if (sessionState.isRecordingSession) {
                        return;
                    }
                    com.android.server.tv.TvInputManagerService.this.logTuneStateChanged(5, sessionState, com.android.server.tv.TvInputManagerService.getTvInputState(sessionState, userState));
                    com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                    args.arg1 = sessionState.componentName.getPackageName();
                    args.arg2 = java.lang.Long.valueOf(java.lang.System.currentTimeMillis());
                    args.arg3 = java.lang.Long.valueOf(android.content.ContentUris.parseId(channelUri));
                    args.arg4 = params;
                    args.arg5 = sessionToken;
                    com.android.server.tv.TvInputManagerService.this.mMessageHandler.obtainMessage(1, args).sendToTarget();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unblockContent(android.os.IBinder sessionToken, java.lang.String unblockedRating, int userId) {
            ensureParentalControlsPermission();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "unblockContent");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).unblockContent(unblockedRating);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in unblockContent", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setCaptionEnabled(android.os.IBinder sessionToken, boolean enabled, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setCaptionEnabled");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).setCaptionEnabled(enabled);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in setCaptionEnabled", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void selectAudioPresentation(android.os.IBinder sessionToken, int presentationId, int programId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "selectAudioPresentation");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).selectAudioPresentation(presentationId, programId);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in selectAudioPresentation", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void selectTrack(android.os.IBinder sessionToken, int type, java.lang.String trackId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "selectTrack");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).selectTrack(type, trackId);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in selectTrack", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setInteractiveAppNotificationEnabled(android.os.IBinder sessionToken, boolean enabled, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setInteractiveAppNotificationEnabled");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).setInteractiveAppNotificationEnabled(enabled);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in setInteractiveAppNotificationEnabled", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void sendAppPrivateCommand(android.os.IBinder sessionToken, java.lang.String command, android.os.Bundle data, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "sendAppPrivateCommand");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).appPrivateCommand(command, data);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in appPrivateCommand", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void createOverlayView(android.os.IBinder sessionToken, android.os.IBinder windowToken, android.graphics.Rect frame, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "createOverlayView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).createOverlayView(windowToken, frame);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in createOverlayView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void relayoutOverlayView(android.os.IBinder sessionToken, android.graphics.Rect frame, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "relayoutOverlayView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).relayoutOverlayView(frame);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in relayoutOverlayView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void removeOverlayView(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "removeOverlayView");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).removeOverlayView();
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in removeOverlayView", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void stopPlayback(android.os.IBinder sessionToken, int mode, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "stopPlayback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).stopPlayback(mode);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in stopPlayback(mode)", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void resumePlayback(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "resumePlayback");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).resumePlayback();
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in resumePlayback()", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void timeShiftPlay(android.os.IBinder sessionToken, android.net.Uri recordedProgramUri, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "timeShiftPlay");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).timeShiftPlay(recordedProgramUri);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in timeShiftPlay", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void timeShiftPause(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "timeShiftPause");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).timeShiftPause();
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in timeShiftPause", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void timeShiftResume(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "timeShiftResume");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).timeShiftResume();
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in timeShiftResume", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void timeShiftSeekTo(android.os.IBinder sessionToken, long timeMs, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "timeShiftSeekTo");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).timeShiftSeekTo(timeMs);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in timeShiftSeekTo", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void timeShiftSetPlaybackParams(android.os.IBinder sessionToken, android.media.PlaybackParams params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "timeShiftSetPlaybackParams");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).timeShiftSetPlaybackParams(params);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in timeShiftSetPlaybackParams", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void timeShiftSetMode(android.os.IBinder sessionToken, int mode, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "timeShiftSetMode");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).timeShiftSetMode(mode);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in timeShiftSetMode", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void timeShiftEnablePositionTracking(android.os.IBinder sessionToken, boolean enable, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "timeShiftEnablePositionTracking");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).timeShiftEnablePositionTracking(enable);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in timeShiftEnablePositionTracking", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTvMessage(android.os.IBinder sessionToken, int type, android.os.Bundle data, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "notifyTvmessage");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).notifyTvMessage(type, data);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyTvMessage", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setVideoFrozen(android.os.IBinder sessionToken, boolean isFrozen, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setVideoFrozen");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).setVideoFrozen(isFrozen);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in setVideoFrozen", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setTvMessageEnabled(android.os.IBinder sessionToken, int type, boolean enabled, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "setTvMessageEnabled");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        java.lang.String inputId = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, userId).inputId;
                        com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.setTvMessageEnabled(inputId, type, enabled);
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).setTvMessageEnabled(type, enabled);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in setTvMessageEnabled", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void startRecording(android.os.IBinder sessionToken, android.net.Uri programUri, android.os.Bundle params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "startRecording");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).startRecording(programUri, params);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in startRecording", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void stopRecording(android.os.IBinder sessionToken, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "stopRecording");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).stopRecording();
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in stopRecording", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void pauseRecording(android.os.IBinder sessionToken, android.os.Bundle params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "pauseRecording");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).pauseRecording(params);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in pauseRecording", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void resumeRecording(android.os.IBinder sessionToken, android.os.Bundle params, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "resumeRecording");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionToken, callingUid, resolvedUserId).resumeRecording(params);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in resumeRecording", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<android.media.tv.TvInputHardwareInfo> getHardwareList() throws android.os.RemoteException {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.TV_INPUT_HARDWARE") != 0) {
                return null;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.getHardwareList();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.media.tv.ITvInputHardware acquireTvInputHardware(int deviceId, android.media.tv.ITvInputHardwareCallback callback, android.media.tv.TvInputInfo info, int userId, java.lang.String tvInputSessionId, int priorityHint) throws android.os.RemoteException {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.TV_INPUT_HARDWARE") != 0) {
                return null;
            }
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "acquireTvInputHardware");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.acquireHardware(deviceId, callback, info, callingUid, resolvedUserId, tvInputSessionId, priorityHint);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void releaseTvInputHardware(int deviceId, android.media.tv.ITvInputHardware hardware, int userId) throws android.os.RemoteException {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.TV_INPUT_HARDWARE") != 0) {
                return;
            }
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "releaseTvInputHardware");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.releaseHardware(deviceId, hardware, callingUid, resolvedUserId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<android.media.tv.DvbDeviceInfo> getDvbDeviceList() throws android.os.RemoteException {
            int i;
            java.util.List<android.media.tv.DvbDeviceInfo> listUnmodifiableList;
            java.io.File devDirectory;
            boolean dvbDirectoryFound;
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.DVB_DEVICE") != 0) {
                throw new java.lang.SecurityException("Requires DVB_DEVICE permission");
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                java.util.ArrayList<android.media.tv.DvbDeviceInfo> deviceInfosFromPattern1 = new java.util.ArrayList<>();
                java.io.File devDirectory2 = new java.io.File("/dev");
                boolean dvbDirectoryFound2 = false;
                java.lang.String[] list = devDirectory2.list();
                int length = list.length;
                int i2 = 0;
                while (true) {
                    i = 1;
                    if (i2 >= length) {
                        break;
                    }
                    java.lang.String fileName = list[i2];
                    java.util.regex.Matcher matcher = com.android.server.tv.TvInputManagerService.sFrontEndDevicePattern.matcher(fileName);
                    if (matcher.find()) {
                        int adapterId = java.lang.Integer.parseInt(matcher.group(1));
                        int deviceId = java.lang.Integer.parseInt(matcher.group(2));
                        deviceInfosFromPattern1.add(new android.media.tv.DvbDeviceInfo(adapterId, deviceId));
                    }
                    if (android.text.TextUtils.equals("dvb", fileName)) {
                        dvbDirectoryFound2 = true;
                    }
                    i2++;
                }
                if (!dvbDirectoryFound2) {
                    return java.util.Collections.unmodifiableList(deviceInfosFromPattern1);
                }
                java.io.File dvbDirectory = new java.io.File(com.android.server.tv.TvInputManagerService.DVB_DIRECTORY);
                java.util.ArrayList<android.media.tv.DvbDeviceInfo> deviceInfosFromPattern2 = new java.util.ArrayList<>();
                java.lang.String[] list2 = dvbDirectory.list();
                int length2 = list2.length;
                int i3 = 0;
                while (i3 < length2) {
                    java.lang.String fileNameInDvb = list2[i3];
                    java.util.regex.Matcher adapterMatcher = com.android.server.tv.TvInputManagerService.sAdapterDirPattern.matcher(fileNameInDvb);
                    if (!adapterMatcher.find()) {
                        devDirectory = devDirectory2;
                        dvbDirectoryFound = dvbDirectoryFound2;
                    } else {
                        int adapterId2 = java.lang.Integer.parseInt(adapterMatcher.group(i));
                        java.io.File adapterDirectory = new java.io.File("/dev/dvb/" + fileNameInDvb);
                        java.lang.String[] list3 = adapterDirectory.list();
                        int length3 = list3.length;
                        int i4 = 0;
                        while (i4 < length3) {
                            java.lang.String fileNameInAdapter = list3[i4];
                            java.io.File devDirectory3 = devDirectory2;
                            boolean dvbDirectoryFound3 = dvbDirectoryFound2;
                            java.util.regex.Matcher frontendMatcher = com.android.server.tv.TvInputManagerService.sFrontEndInAdapterDirPattern.matcher(fileNameInAdapter);
                            if (frontendMatcher.find()) {
                                int deviceId2 = java.lang.Integer.parseInt(frontendMatcher.group(1));
                                deviceInfosFromPattern2.add(new android.media.tv.DvbDeviceInfo(adapterId2, deviceId2));
                            }
                            i4++;
                            devDirectory2 = devDirectory3;
                            dvbDirectoryFound2 = dvbDirectoryFound3;
                        }
                        devDirectory = devDirectory2;
                        dvbDirectoryFound = dvbDirectoryFound2;
                    }
                    i3++;
                    devDirectory2 = devDirectory;
                    dvbDirectoryFound2 = dvbDirectoryFound;
                    i = 1;
                }
                if (deviceInfosFromPattern2.isEmpty()) {
                    listUnmodifiableList = java.util.Collections.unmodifiableList(deviceInfosFromPattern1);
                } else {
                    listUnmodifiableList = java.util.Collections.unmodifiableList(deviceInfosFromPattern2);
                }
                return listUnmodifiableList;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.os.ParcelFileDescriptor openDvbDevice(android.media.tv.DvbDeviceInfo info, int deviceType) throws android.os.RemoteException {
            boolean dvbDeviceFound;
            java.lang.String deviceFileName;
            int i;
            java.io.File devDirectory;
            boolean dvbDeviceFound2;
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.DVB_DEVICE") != 0) {
                throw new java.lang.SecurityException("Requires DVB_DEVICE permission");
            }
            java.io.File devDirectory2 = new java.io.File("/dev");
            boolean dvbDeviceFound3 = false;
            java.lang.String[] list = devDirectory2.list();
            int length = list.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    dvbDeviceFound = dvbDeviceFound3;
                    break;
                }
                java.lang.String fileName = list[i2];
                if (!android.text.TextUtils.equals("dvb", fileName)) {
                    devDirectory = devDirectory2;
                } else {
                    java.io.File dvbDirectory = new java.io.File(com.android.server.tv.TvInputManagerService.DVB_DIRECTORY);
                    java.lang.String[] list2 = dvbDirectory.list();
                    int length2 = list2.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= length2) {
                            devDirectory = devDirectory2;
                            break;
                        }
                        java.lang.String fileNameInDvb = list2[i3];
                        java.util.regex.Matcher adapterMatcher = com.android.server.tv.TvInputManagerService.sAdapterDirPattern.matcher(fileNameInDvb);
                        if (!adapterMatcher.find()) {
                            dvbDeviceFound2 = dvbDeviceFound3;
                            devDirectory = devDirectory2;
                        } else {
                            dvbDeviceFound2 = dvbDeviceFound3;
                            java.io.File adapterDirectory = new java.io.File("/dev/dvb/" + fileNameInDvb);
                            java.lang.String[] list3 = adapterDirectory.list();
                            int length3 = list3.length;
                            int i4 = 0;
                            while (i4 < length3) {
                                java.lang.String fileNameInAdapter = list3[i4];
                                devDirectory = devDirectory2;
                                java.util.regex.Matcher frontendMatcher = com.android.server.tv.TvInputManagerService.sFrontEndInAdapterDirPattern.matcher(fileNameInAdapter);
                                if (!frontendMatcher.find()) {
                                    i4++;
                                    devDirectory2 = devDirectory;
                                } else {
                                    dvbDeviceFound3 = true;
                                    break;
                                }
                            }
                            devDirectory = devDirectory2;
                        }
                        dvbDeviceFound3 = dvbDeviceFound2;
                        if (dvbDeviceFound3) {
                            break;
                        }
                        i3++;
                        devDirectory2 = devDirectory;
                    }
                }
                if (!dvbDeviceFound3) {
                    i2++;
                    devDirectory2 = devDirectory;
                } else {
                    dvbDeviceFound = dvbDeviceFound3;
                    break;
                }
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                switch (deviceType) {
                    case 0:
                        java.lang.String deviceFileName2 = java.lang.String.format(dvbDeviceFound ? "/dev/dvb/adapter%d/demux%d" : "/dev/dvb%d.demux%d", java.lang.Integer.valueOf(info.getAdapterId()), java.lang.Integer.valueOf(info.getDeviceId()));
                        deviceFileName = deviceFileName2;
                        break;
                    case 1:
                        java.lang.String deviceFileName3 = java.lang.String.format(dvbDeviceFound ? "/dev/dvb/adapter%d/dvr%d" : "/dev/dvb%d.dvr%d", java.lang.Integer.valueOf(info.getAdapterId()), java.lang.Integer.valueOf(info.getDeviceId()));
                        deviceFileName = deviceFileName3;
                        break;
                    case 2:
                        java.lang.String deviceFileName4 = java.lang.String.format(dvbDeviceFound ? "/dev/dvb/adapter%d/frontend%d" : "/dev/dvb%d.frontend%d", java.lang.Integer.valueOf(info.getAdapterId()), java.lang.Integer.valueOf(info.getDeviceId()));
                        deviceFileName = deviceFileName4;
                        break;
                    default:
                        throw new java.lang.IllegalArgumentException("Invalid DVB device: " + deviceType);
                }
                try {
                    java.io.File file = new java.io.File(deviceFileName);
                    if (2 == deviceType) {
                        i = 805306368;
                    } else {
                        i = 268435456;
                    }
                    return android.os.ParcelFileDescriptor.open(file, i);
                } catch (java.io.FileNotFoundException e) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    return null;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<android.media.tv.TvStreamConfig> getAvailableTvStreamConfigList(java.lang.String inputId, int userId) throws android.os.RemoteException {
            ensureCaptureTvInputPermission();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "getAvailableTvStreamConfigList");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.getAvailableTvStreamConfigList(inputId, callingUid, resolvedUserId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean captureFrame(java.lang.String inputId, android.view.Surface surface, android.media.tv.TvStreamConfig config, int userId) throws android.os.RemoteException {
            java.lang.String hardwareInputId;
            ensureCaptureTvInputPermission();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "captureFrame");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                        if (userState.inputMap.get(inputId) == null) {
                            android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "input not found for " + inputId);
                            android.os.Binder.restoreCallingIdentity(identity);
                            return false;
                        }
                        java.util.Iterator it = userState.sessionStateMap.values().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                hardwareInputId = null;
                                break;
                            }
                            com.android.server.tv.TvInputManagerService.SessionState sessionState = (com.android.server.tv.TvInputManagerService.SessionState) it.next();
                            if (sessionState.inputId.equals(inputId) && sessionState.hardwareSessionToken != null) {
                                java.lang.String hardwareInputId2 = ((com.android.server.tv.TvInputManagerService.SessionState) userState.sessionStateMap.get(sessionState.hardwareSessionToken)).inputId;
                                hardwareInputId = hardwareInputId2;
                                break;
                            }
                        }
                        try {
                            return com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.captureFrame(hardwareInputId != null ? hardwareInputId : inputId, surface, config, callingUid, resolvedUserId);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean isSingleSessionActive(int userId) throws android.os.RemoteException {
            ensureCaptureTvInputPermission();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), callingUid, userId, "isSingleSessionActive");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                    boolean z = true;
                    if (userState.sessionStateMap.size() == 1) {
                        return true;
                    }
                    if (userState.sessionStateMap.size() != 2) {
                        return false;
                    }
                    com.android.server.tv.TvInputManagerService.SessionState[] sessionStates = (com.android.server.tv.TvInputManagerService.SessionState[]) userState.sessionStateMap.values().toArray(new com.android.server.tv.TvInputManagerService.SessionState[2]);
                    if (sessionStates[0].hardwareSessionToken == null && sessionStates[1].hardwareSessionToken == null) {
                        z = false;
                    }
                    return z;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private void ensureCaptureTvInputPermission() {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.CAPTURE_TV_INPUT") != 0) {
                throw new java.lang.SecurityException("Requires CAPTURE_TV_INPUT permission");
            }
        }

        public void requestChannelBrowsable(android.net.Uri channelUri, int userId) throws android.os.RemoteException {
            java.lang.String callingPackageName = getCallingPackageName();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "requestChannelBrowsable");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.content.Intent intent = new android.content.Intent("android.media.tv.action.CHANNEL_BROWSABLE_REQUESTED");
                java.util.List<android.content.pm.ResolveInfo> list = com.android.server.tv.TvInputManagerService.this.getContext().getPackageManager().queryBroadcastReceivers(intent, 0);
                if (list != null) {
                    for (android.content.pm.ResolveInfo info : list) {
                        java.lang.String receiverPackageName = info.activityInfo.packageName;
                        intent.putExtra("android.media.tv.extra.CHANNEL_ID", android.content.ContentUris.parseId(channelUri));
                        intent.putExtra("android.media.tv.extra.PACKAGE_NAME", callingPackageName);
                        intent.setPackage(receiverPackageName);
                        com.android.server.tv.TvInputManagerService.this.getContext().sendBroadcastAsUser(intent, new android.os.UserHandle(resolvedUserId));
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void requestBroadcastInfo(android.os.IBinder sessionToken, android.media.tv.BroadcastInfoRequest request, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "requestBroadcastInfo");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.SessionState sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).requestBroadcastInfo(request);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in requestBroadcastInfo", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void removeBroadcastInfo(android.os.IBinder sessionToken, int requestId, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "removeBroadcastInfo");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.SessionState sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).removeBroadcastInfo(requestId);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in removeBroadcastInfo", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void requestAd(android.os.IBinder sessionToken, android.media.tv.AdRequest request, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "requestAd");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.SessionState sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).requestAd(request);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in requestAd", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyAdBufferReady(android.os.IBinder sessionToken, android.media.tv.AdBuffer buffer, int userId) {
            android.os.SharedMemory sharedMemory;
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyAdBuffer");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        try {
                            com.android.server.tv.TvInputManagerService.SessionState sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                            com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).notifyAdBufferReady(buffer);
                        } finally {
                        }
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyAdBuffer", e);
                        if (buffer != null) {
                            sharedMemory = buffer.getSharedMemory();
                        }
                    }
                    if (buffer != null) {
                        sharedMemory = buffer.getSharedMemory();
                        sharedMemory.close();
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyTvAdSessionData(android.os.IBinder sessionToken, java.lang.String type, android.os.Bundle data, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "notifyTvAdSessionData");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        com.android.server.tv.TvInputManagerService.SessionState sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(sessionToken, callingUid, resolvedUserId);
                        com.android.server.tv.TvInputManagerService.this.getSessionLocked(sessionState).notifyTvAdSessionData(type, data);
                    } catch (android.os.RemoteException | com.android.server.tv.TvInputManagerService.SessionNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyTvAdSessionData", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public int getClientPid(java.lang.String sessionId) {
            ensureTunerResourceAccessPermission();
            long identity = android.os.Binder.clearCallingIdentity();
            int clientPid = -1;
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    try {
                        clientPid = getClientPidLocked(sessionId);
                    } catch (com.android.server.tv.TvInputManagerService.ClientPidNotFoundException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in getClientPid", e);
                    }
                }
                return clientPid;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public int getClientPriority(int useCase, java.lang.String sessionId) {
            ensureTunerResourceAccessPermission();
            int callingPid = android.os.Binder.getCallingPid();
            long identity = android.os.Binder.clearCallingIdentity();
            int clientPid = -1;
            if (sessionId != null) {
                try {
                    synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                        try {
                            clientPid = getClientPidLocked(sessionId);
                        } catch (com.android.server.tv.TvInputManagerService.ClientPidNotFoundException e) {
                            android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in getClientPriority", e);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            } else {
                clientPid = callingPid;
            }
            android.media.tv.tunerresourcemanager.TunerResourceManager trm = (android.media.tv.tunerresourcemanager.TunerResourceManager) com.android.server.tv.TvInputManagerService.this.mContext.getSystemService("tv_tuner_resource_mgr");
            int clientPriority = trm.getClientPriority(useCase, clientPid);
            android.os.Binder.restoreCallingIdentity(identity);
            return clientPriority;
        }

        public java.util.List<android.media.tv.TunedInfo> getCurrentTunedInfos(int userId) {
            java.util.List<android.media.tv.TunedInfo> currentTunedInfosInternalLocked;
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.ACCESS_TUNED_INFO") != 0) {
                throw new java.lang.SecurityException("The caller does not have access tuned info permission");
            }
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            int resolvedUserId = com.android.server.tv.TvInputManagerService.this.resolveCallingUserId(callingPid, callingUid, userId, "getTvCurrentChannelInfos");
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(resolvedUserId);
                currentTunedInfosInternalLocked = com.android.server.tv.TvInputManagerService.this.getCurrentTunedInfosInternalLocked(userState, callingPid, callingUid);
            }
            return currentTunedInfosInternalLocked;
        }

        public void addHardwareDevice(int deviceId) {
            android.media.tv.TvInputHardwareInfo info = new android.media.tv.TvInputHardwareInfo.Builder().deviceId(deviceId).type(9).audioType(0).audioAddress("0").hdmiPortId(0).build();
            android.media.tv.TvStreamConfig[] configs = {new android.media.tv.TvStreamConfig.Builder().streamId(19001).generation(1).maxHeight(600).maxWidth(800).type(1).build()};
            com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.onDeviceAvailable(info, configs);
        }

        public void removeHardwareDevice(int deviceId) {
            com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.onDeviceUnavailable(deviceId);
        }

        private int getClientPidLocked(java.lang.String sessionId) throws com.android.server.tv.TvInputManagerService.ClientPidNotFoundException {
            if (com.android.server.tv.TvInputManagerService.this.mSessionIdToSessionStateMap.get(sessionId) == null) {
                throw new com.android.server.tv.TvInputManagerService.ClientPidNotFoundException("Client Pid not found with sessionId " + sessionId);
            }
            return ((com.android.server.tv.TvInputManagerService.SessionState) com.android.server.tv.TvInputManagerService.this.mSessionIdToSessionStateMap.get(sessionId)).callingPid;
        }

        private void ensureTunerResourceAccessPermission() {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.TUNER_RESOURCE_ACCESS") != 0) {
                throw new java.lang.SecurityException("Requires TUNER_RESOURCE_ACCESS permission");
            }
        }

        private void ensureTisExtensionInterfacePermission() {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.TIS_EXTENSION_INTERFACE") != 0) {
                throw new java.lang.SecurityException("Requires TIS_EXTENSION_INTERFACE permission");
            }
        }

        @dalvik.annotation.optimization.NeverCompile
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.tv.TvInputManagerService.this.mContext, com.android.server.tv.TvInputManagerService.TAG, pw)) {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    pw.println("User Ids (Current user: " + com.android.server.tv.TvInputManagerService.this.mCurrentUserId + "):");
                    pw.increaseIndent();
                    for (int i = 0; i < com.android.server.tv.TvInputManagerService.this.mUserStates.size(); i++) {
                        pw.println(java.lang.Integer.valueOf(com.android.server.tv.TvInputManagerService.this.mUserStates.keyAt(i)));
                    }
                    pw.decreaseIndent();
                    for (int i2 = 0; i2 < com.android.server.tv.TvInputManagerService.this.mUserStates.size(); i2++) {
                        int userId = com.android.server.tv.TvInputManagerService.this.mUserStates.keyAt(i2);
                        com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(userId);
                        pw.println("UserState (" + userId + "):");
                        pw.increaseIndent();
                        pw.println("inputMap: inputId -> TvInputState");
                        pw.increaseIndent();
                        for (java.util.Map.Entry<java.lang.String, com.android.server.tv.TvInputManagerService.TvInputState> entry : userState.inputMap.entrySet()) {
                            pw.println(entry.getKey() + ": " + entry.getValue());
                        }
                        pw.decreaseIndent();
                        pw.println("packageSet:");
                        pw.increaseIndent();
                        for (java.lang.String packageName : userState.packageSet) {
                            pw.println(packageName);
                        }
                        pw.decreaseIndent();
                        pw.println("clientStateMap: ITvInputClient -> ClientState");
                        pw.increaseIndent();
                        for (java.util.Map.Entry<android.os.IBinder, com.android.server.tv.TvInputManagerService.ClientState> entry2 : userState.clientStateMap.entrySet()) {
                            com.android.server.tv.TvInputManagerService.ClientState client = entry2.getValue();
                            pw.println(entry2.getKey() + ": " + client);
                            pw.increaseIndent();
                            pw.println("sessionTokens:");
                            pw.increaseIndent();
                            for (android.os.IBinder token : client.sessionTokens) {
                                pw.println("" + token);
                            }
                            pw.decreaseIndent();
                            pw.println("clientTokens: " + client.clientToken);
                            pw.println("userId: " + client.userId);
                            pw.decreaseIndent();
                        }
                        pw.decreaseIndent();
                        pw.println("serviceStateMap: ComponentName -> ServiceState");
                        pw.increaseIndent();
                        for (java.util.Map.Entry<android.content.ComponentName, com.android.server.tv.TvInputManagerService.ServiceState> entry3 : userState.serviceStateMap.entrySet()) {
                            com.android.server.tv.TvInputManagerService.ServiceState service = entry3.getValue();
                            pw.println(entry3.getKey() + ": " + service);
                            pw.increaseIndent();
                            pw.println("sessionTokens:");
                            pw.increaseIndent();
                            for (android.os.IBinder token2 : service.sessionTokens) {
                                pw.println("" + token2);
                            }
                            pw.decreaseIndent();
                            pw.println("service: " + service.service);
                            pw.println("callback: " + service.callback);
                            pw.println("bound: " + service.bound);
                            pw.println("reconnecting: " + service.reconnecting);
                            pw.decreaseIndent();
                        }
                        pw.decreaseIndent();
                        pw.println("sessionStateMap: ITvInputSession -> SessionState");
                        pw.increaseIndent();
                        for (java.util.Map.Entry<android.os.IBinder, com.android.server.tv.TvInputManagerService.SessionState> entry4 : userState.sessionStateMap.entrySet()) {
                            com.android.server.tv.TvInputManagerService.SessionState session = entry4.getValue();
                            pw.println(entry4.getKey() + ": " + session);
                            pw.increaseIndent();
                            pw.println("inputId: " + session.inputId);
                            pw.println("sessionId: " + session.sessionId);
                            pw.println("client: " + session.client);
                            pw.println("seq: " + session.seq);
                            pw.println("callingUid: " + session.callingUid);
                            pw.println("callingPid: " + session.callingPid);
                            pw.println("userId: " + session.userId);
                            pw.println("sessionToken: " + session.sessionToken);
                            pw.println("session: " + session.session);
                            pw.println("hardwareSessionToken: " + session.hardwareSessionToken);
                            pw.decreaseIndent();
                        }
                        pw.decreaseIndent();
                        pw.println("mCallbacks:");
                        pw.increaseIndent();
                        int n = userState.mCallbacks.beginBroadcast();
                        for (int j = 0; j < n; j++) {
                            pw.println(userState.mCallbacks.getRegisteredCallbackItem(j));
                        }
                        userState.mCallbacks.finishBroadcast();
                        pw.decreaseIndent();
                        pw.println("mainSessionToken: " + userState.mainSessionToken);
                        pw.decreaseIndent();
                    }
                }
                com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.dump(fd, writer, args);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getSessionActualInputId(com.android.server.tv.TvInputManagerService.SessionState sessionState) {
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(sessionState.userId);
        com.android.server.tv.TvInputManagerService.TvInputState tvInputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(sessionState.inputId);
        if (tvInputState == null) {
            android.util.Slog.w(TAG, "No TvInputState for sessionState.inputId " + sessionState.inputId);
            return sessionState.inputId;
        }
        android.media.tv.TvInputInfo tvInputInfo = tvInputState.info;
        if (tvInputInfo == null) {
            android.util.Slog.w(TAG, "TvInputInfo is null for input id " + sessionState.inputId);
            return sessionState.inputId;
        }
        java.lang.String sessionActualInputId = sessionState.inputId;
        switch (tvInputInfo.getType()) {
            case 1007:
                java.util.Map<java.lang.String, java.util.List<java.lang.String>> hdmiParentInputMap = this.mTvInputHardwareManager.getHdmiParentInputMap();
                if (hdmiParentInputMap.containsKey(sessionState.inputId)) {
                    java.util.List<java.lang.String> parentInputList = hdmiParentInputMap.get(sessionState.inputId);
                }
                break;
        }
        return sessionState.inputId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.tv.TvInputManagerService.TvInputState getTvInputState(com.android.server.tv.TvInputManagerService.SessionState sessionState, com.android.server.tv.TvInputManagerService.UserState userState) {
        if (userState != null) {
            return (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(sessionState.inputId);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.media.tv.TunedInfo> getCurrentTunedInfosInternalLocked(com.android.server.tv.TvInputManagerService.UserState userState, int callingPid, int callingUid) {
        java.lang.Integer appTag;
        int appType;
        java.util.List<android.media.tv.TunedInfo> channelInfos = new java.util.ArrayList<>();
        boolean watchedProgramsAccess = hasAccessWatchedProgramsPermission(callingPid, callingUid);
        for (com.android.server.tv.TvInputManagerService.SessionState state : userState.sessionStateMap.values()) {
            if (state.isCurrent) {
                if (state.callingUid == callingUid) {
                    appTag = 0;
                    appType = 1;
                } else {
                    appTag = (java.lang.Integer) userState.mAppTagMap.get(java.lang.Integer.valueOf(state.callingUid));
                    if (appTag == null) {
                        int i = userState.mNextAppTag;
                        userState.mNextAppTag = i + 1;
                        appTag = java.lang.Integer.valueOf(i);
                        userState.mAppTagMap.put(java.lang.Integer.valueOf(state.callingUid), appTag);
                    }
                    if (isSystemApp(state.componentName.getPackageName())) {
                        appType = 2;
                    } else {
                        appType = 3;
                    }
                }
                channelInfos.add(new android.media.tv.TunedInfo(state.inputId, watchedProgramsAccess ? state.currentChannel : null, state.isRecordingSession, state.isVisible, state.isMainSession, appType, appTag.intValue()));
            }
        }
        return channelInfos;
    }

    private boolean hasAccessWatchedProgramsPermission(int callingPid, int callingUid) {
        return this.mContext.checkPermission(PERMISSION_ACCESS_WATCHED_PROGRAMS, callingPid, callingUid) == 0;
    }

    private boolean isSystemApp(java.lang.String pkg) {
        try {
            return (this.mContext.getPackageManager().getApplicationInfo(pkg, 0).flags & 1) != 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logTuneStateChanged(int state, com.android.server.tv.TvInputManagerService.SessionState sessionState, com.android.server.tv.TvInputManagerService.TvInputState inputState) {
        int inputType;
        int inputId;
        int hdmiPort;
        int tisUid = -1;
        if (inputState == null) {
            inputType = 0;
            inputId = 0;
            hdmiPort = 0;
        } else {
            tisUid = inputState.uid;
            int inputType2 = inputState.info.getType();
            if (inputType2 == 0) {
                inputType2 = 1;
            }
            int inputId2 = inputState.inputNumber;
            android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo = inputState.info.getHdmiDeviceInfo();
            if (hdmiDeviceInfo == null) {
                inputType = inputType2;
                inputId = inputId2;
                hdmiPort = 0;
            } else {
                int hdmiPort2 = hdmiDeviceInfo.getPortId();
                inputType = inputType2;
                inputId = inputId2;
                hdmiPort = hdmiPort2;
            }
        }
        int inputType3 = sessionState.callingUid;
        com.android.internal.util.FrameworkStatsLog.write(327, new int[]{inputType3, tisUid}, new java.lang.String[]{"tif_player", "tv_input_service"}, state, sessionState.sessionId, inputType, inputId, hdmiPort);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logExternalInputEvent(int eventType, java.lang.String inputId, com.android.server.tv.TvInputManagerService.SessionState sessionState) {
        java.lang.String displayName;
        int vendorId;
        int hdmiPort;
        android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo;
        java.lang.String inputId2 = inputId;
        com.android.server.tv.TvInputManagerService.UserState userState = getOrCreateUserStateLocked(sessionState.userId);
        com.android.server.tv.TvInputManagerService.TvInputState tvInputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId2);
        if (tvInputState == null) {
            android.util.Slog.w(TAG, "Cannot find input state for input id " + inputId2);
            inputId2 = sessionState.inputId;
            tvInputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId2);
        }
        if (tvInputState == null) {
            android.util.Slog.w(TAG, "Cannot find input state for sessionState.inputId " + inputId2);
            return;
        }
        android.media.tv.TvInputInfo tvInputInfo = tvInputState.info;
        if (tvInputInfo == null) {
            android.util.Slog.w(TAG, "TvInputInfo is null for input id " + inputId2);
            return;
        }
        int inputState = tvInputState.state;
        int inputType = tvInputInfo.getType();
        java.lang.String displayName2 = tvInputInfo.loadLabel(this.mContext).toString();
        java.lang.String tifSessionId = sessionState.sessionId;
        if (tvInputInfo.getType() == 1007 && (hdmiDeviceInfo = tvInputInfo.getHdmiDeviceInfo()) != null) {
            int hdmiPort2 = hdmiDeviceInfo.getPortId();
            if (!hdmiDeviceInfo.isCecDevice()) {
                displayName = displayName2;
                vendorId = 16777215;
                hdmiPort = hdmiPort2;
            } else {
                java.lang.String displayName3 = hdmiDeviceInfo.getDisplayName();
                if (this.mExternalInputLoggingDisplayNameFilterEnabled) {
                    displayName3 = filterExternalInputLoggingDisplayName(displayName3);
                }
                int vendorId2 = hdmiDeviceInfo.getVendorId();
                displayName = displayName3;
                vendorId = vendorId2;
                hdmiPort = hdmiPort2;
            }
        } else {
            displayName = displayName2;
            vendorId = 16777215;
            hdmiPort = -1;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.EXTERNAL_TV_INPUT_EVENT, eventType, inputState, inputType, vendorId, hdmiPort, tifSessionId, displayName);
    }

    private java.lang.String filterExternalInputLoggingDisplayName(java.lang.String displayName) {
        if (displayName == null) {
            return "NULL_DISPLAY_NAME";
        }
        if (this.mExternalInputLoggingDeviceOnScreenDisplayNames.contains(displayName)) {
            return displayName;
        }
        for (java.lang.String brandName : this.mExternalInputLoggingDeviceBrandNames) {
            if (displayName.toUpperCase().contains(brandName.toUpperCase())) {
                return brandName;
            }
        }
        return "FILTERED_DISPLAY_NAME";
    }

    private static final class UserState {
        private final java.util.Map<android.media.tv.ITvInputManagerCallback, android.util.Pair<java.lang.Integer, java.lang.Integer>> callbackPidUidMap;
        private final java.util.Map<android.os.IBinder, com.android.server.tv.TvInputManagerService.ClientState> clientStateMap;
        private final java.util.List<android.media.tv.TvContentRatingSystemInfo> contentRatingSystemList;
        private java.util.Map<java.lang.String, com.android.server.tv.TvInputManagerService.TvInputState> inputMap;
        private final java.util.Map<java.lang.Integer, java.lang.Integer> mAppTagMap;
        private final android.os.RemoteCallbackList<android.media.tv.ITvInputManagerCallback> mCallbacks;
        private int mNextAppTag;
        private android.os.IBinder mainSessionToken;
        private final java.util.Set<java.lang.String> packageSet;
        private final com.android.server.tv.PersistentDataStore persistentDataStore;
        private final java.util.Map<android.content.ComponentName, com.android.server.tv.TvInputManagerService.ServiceState> serviceStateMap;
        private final java.util.Map<android.os.IBinder, com.android.server.tv.TvInputManagerService.SessionState> sessionStateMap;

        private UserState(android.content.Context context, int userId) {
            this.inputMap = new java.util.HashMap();
            this.packageSet = new java.util.HashSet();
            this.contentRatingSystemList = new java.util.ArrayList();
            this.clientStateMap = new java.util.HashMap();
            this.serviceStateMap = new java.util.HashMap();
            this.sessionStateMap = new java.util.HashMap();
            this.mCallbacks = new android.os.RemoteCallbackList<>();
            this.callbackPidUidMap = new java.util.HashMap();
            this.mainSessionToken = null;
            this.mAppTagMap = new java.util.HashMap();
            this.mNextAppTag = 1;
            this.persistentDataStore = new com.android.server.tv.PersistentDataStore(context, userId);
        }
    }

    private final class ClientState implements android.os.IBinder.DeathRecipient {
        private android.os.IBinder clientToken;
        private final java.util.List<android.os.IBinder> sessionTokens = new java.util.ArrayList();
        private final int userId;

        ClientState(android.os.IBinder clientToken, int userId) {
            this.clientToken = clientToken;
            this.userId = userId;
        }

        public boolean isEmpty() {
            return this.sessionTokens.isEmpty();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(this.userId);
                com.android.server.tv.TvInputManagerService.ClientState clientState = (com.android.server.tv.TvInputManagerService.ClientState) userState.clientStateMap.get(this.clientToken);
                if (clientState != null) {
                    while (clientState.sessionTokens.size() > 0) {
                        android.os.IBinder sessionToken = clientState.sessionTokens.get(0);
                        com.android.server.tv.TvInputManagerService.this.releaseSessionLocked(sessionToken, 1000, this.userId);
                        if (clientState.sessionTokens.contains(sessionToken)) {
                            android.util.Slog.d(com.android.server.tv.TvInputManagerService.TAG, "remove sessionToken " + sessionToken + " for " + this.clientToken);
                            clientState.sessionTokens.remove(sessionToken);
                        }
                    }
                }
                this.clientToken = null;
            }
        }
    }

    private final class ServiceState {
        private boolean bound;
        private com.android.server.tv.TvInputManagerService.ServiceCallback callback;
        private final android.content.ComponentName component;
        private final android.content.ServiceConnection connection;
        private final java.util.List<android.media.tv.TvInputHardwareInfo> hardwareDeviceRemovedBuffer;
        private final java.util.Map<java.lang.String, android.media.tv.TvInputInfo> hardwareInputMap;
        private final java.util.List<android.hardware.hdmi.HdmiDeviceInfo> hdmiDeviceRemovedBuffer;
        private final java.util.List<android.hardware.hdmi.HdmiDeviceInfo> hdmiDeviceUpdatedBuffer;
        private final boolean isHardware;
        private boolean neverConnected;
        private boolean reconnecting;
        private android.media.tv.ITvInputService service;
        private final java.util.List<android.os.IBinder> sessionTokens;

        private ServiceState(android.content.ComponentName component, int userId) {
            this.sessionTokens = new java.util.ArrayList();
            this.hardwareInputMap = new java.util.HashMap();
            this.hardwareDeviceRemovedBuffer = new java.util.ArrayList();
            this.hdmiDeviceRemovedBuffer = new java.util.ArrayList();
            this.hdmiDeviceUpdatedBuffer = new java.util.ArrayList();
            this.component = component;
            this.connection = new com.android.server.tv.TvInputManagerService.InputServiceConnection(component, userId);
            this.isHardware = com.android.server.tv.TvInputManagerService.hasHardwarePermission(com.android.server.tv.TvInputManagerService.this.mContext.getPackageManager(), component);
            this.neverConnected = true;
        }
    }

    private static final class TvInputState {
        private android.media.tv.TvInputInfo info;
        private int inputNumber;
        private int state;
        private int uid;

        private TvInputState() {
            this.state = 0;
        }

        public java.lang.String toString() {
            return "info: " + this.info + "; state: " + this.state;
        }
    }

    private final class SessionState implements android.os.IBinder.DeathRecipient {
        private final int callingPid;
        private final int callingUid;
        private final android.media.tv.ITvInputClient client;
        private final android.content.ComponentName componentName;
        private android.net.Uri currentChannel;
        private android.os.IBinder hardwareSessionToken;
        private final java.lang.String inputId;
        private boolean isCurrent;
        private boolean isMainSession;
        private final boolean isRecordingSession;
        private boolean isVisible;
        private final int seq;
        private android.media.tv.ITvInputSession session;
        private final java.lang.String sessionId;
        private final android.os.IBinder sessionToken;
        private final android.content.AttributionSource tvAppAttributionSource;
        private final int userId;

        private SessionState(android.os.IBinder sessionToken, java.lang.String inputId, android.content.ComponentName componentName, boolean isRecordingSession, android.media.tv.ITvInputClient client, int seq, int callingUid, int callingPid, int userId, java.lang.String sessionId, android.content.AttributionSource tvAppAttributionSource) {
            this.isCurrent = false;
            this.currentChannel = null;
            this.isVisible = false;
            this.isMainSession = false;
            this.sessionToken = sessionToken;
            this.inputId = inputId;
            this.componentName = componentName;
            this.isRecordingSession = isRecordingSession;
            this.client = client;
            this.seq = seq;
            this.callingUid = callingUid;
            this.callingPid = callingPid;
            this.userId = userId;
            this.sessionId = sessionId;
            this.tvAppAttributionSource = tvAppAttributionSource;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                this.session = null;
                com.android.server.tv.TvInputManagerService.this.clearSessionAndNotifyClientLocked(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindService(com.android.server.tv.TvInputManagerService.ServiceState serviceState, int userId) {
        if (serviceState.bound) {
            if (serviceState.isHardware) {
                updateHardwareServiceConnectionDelayed(userId);
            }
        } else {
            android.content.Intent i = new android.content.Intent("android.media.tv.TvInputService").setComponent(serviceState.component);
            serviceState.bound = this.mContext.bindServiceAsUser(i, serviceState.connection, 33554433, new android.os.UserHandle(userId));
            if (!serviceState.bound) {
                android.util.Slog.e(TAG, "failed to bind " + serviceState.component + " for userId " + userId);
                this.mContext.unbindService(serviceState.connection);
            }
        }
    }

    private void unbindService(com.android.server.tv.TvInputManagerService.ServiceState serviceState) {
        if (!serviceState.bound) {
            return;
        }
        this.mContext.unbindService(serviceState.connection);
        serviceState.bound = false;
        serviceState.service = null;
        serviceState.callback = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHardwareTvInputServiceBindingLocked(int userId) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> services = pm.queryIntentServicesAsUser(new android.content.Intent("android.media.tv.TvInputService"), 132, userId);
        for (android.content.pm.ResolveInfo ri : services) {
            android.content.pm.ServiceInfo si = ri.serviceInfo;
            if ("android.permission.BIND_TV_INPUT".equals(si.permission)) {
                android.content.ComponentName component = new android.content.ComponentName(si.packageName, si.name);
                if (hasHardwarePermission(pm, component)) {
                    updateServiceConnectionLocked(component, userId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHardwareServiceConnectionDelayed(int userId) {
        this.mMessageHandler.removeMessages(4);
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = java.lang.Integer.valueOf(userId);
        android.os.Message msg = this.mMessageHandler.obtainMessage(4, args);
        this.mMessageHandler.sendMessageDelayed(msg, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addHardwareInputLocked(android.media.tv.TvInputInfo inputInfo, android.content.ComponentName component, int userId) {
        com.android.server.tv.TvInputManagerService.ServiceState serviceState = getServiceStateLocked(component, userId);
        serviceState.hardwareInputMap.put(inputInfo.getId(), inputInfo);
        buildTvInputListLocked(userId, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeHardwareInputLocked(java.lang.String inputId, int userId) {
        if (!this.mTvInputHardwareManager.getInputMap().containsKey(inputId)) {
            return;
        }
        android.content.ComponentName component = this.mTvInputHardwareManager.getInputMap().get(inputId).getComponent();
        com.android.server.tv.TvInputManagerService.ServiceState serviceState = getServiceStateLocked(component, userId);
        boolean removed = serviceState.hardwareInputMap.remove(inputId) != null;
        if (removed) {
            buildTvInputListLocked(userId, null);
            this.mTvInputHardwareManager.removeHardwareInput(inputId);
        }
    }

    private final class InputServiceConnection implements android.content.ServiceConnection {
        private final android.content.ComponentName mComponent;
        private final int mUserId;

        private InputServiceConnection(android.content.ComponentName component, int userId) {
            this.mComponent = component;
            this.mUserId = userId;
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x0073 A[Catch: all -> 0x01d2, TryCatch #2 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0039, B:13:0x003f, B:14:0x004d, B:17:0x005a, B:18:0x0061, B:19:0x006d, B:21:0x0073, B:23:0x0087, B:25:0x008d, B:27:0x00a0, B:29:0x00a6, B:30:0x00ae, B:32:0x00b4, B:33:0x00ba, B:36:0x00c3, B:38:0x00cb, B:39:0x00da, B:41:0x00e0, B:42:0x00e6, B:45:0x00ef, B:47:0x00f7, B:48:0x010c, B:50:0x0112, B:51:0x0118, B:54:0x0121, B:56:0x0129, B:57:0x0137, B:59:0x013d, B:60:0x0143, B:63:0x014c, B:65:0x0154, B:66:0x015c, B:68:0x0162, B:69:0x0168, B:72:0x0171, B:74:0x0179, B:75:0x0180, B:76:0x018d, B:78:0x0193, B:80:0x01a7, B:82:0x01ab, B:83:0x01af, B:85:0x01b5, B:86:0x01c3, B:88:0x01c9, B:89:0x01d0), top: B:98:0x0007, inners: #0, #1, #3, #4, #5, #6 }] */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00a6 A[Catch: all -> 0x01d2, TryCatch #2 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0039, B:13:0x003f, B:14:0x004d, B:17:0x005a, B:18:0x0061, B:19:0x006d, B:21:0x0073, B:23:0x0087, B:25:0x008d, B:27:0x00a0, B:29:0x00a6, B:30:0x00ae, B:32:0x00b4, B:33:0x00ba, B:36:0x00c3, B:38:0x00cb, B:39:0x00da, B:41:0x00e0, B:42:0x00e6, B:45:0x00ef, B:47:0x00f7, B:48:0x010c, B:50:0x0112, B:51:0x0118, B:54:0x0121, B:56:0x0129, B:57:0x0137, B:59:0x013d, B:60:0x0143, B:63:0x014c, B:65:0x0154, B:66:0x015c, B:68:0x0162, B:69:0x0168, B:72:0x0171, B:74:0x0179, B:75:0x0180, B:76:0x018d, B:78:0x0193, B:80:0x01a7, B:82:0x01ab, B:83:0x01af, B:85:0x01b5, B:86:0x01c3, B:88:0x01c9, B:89:0x01d0), top: B:98:0x0007, inners: #0, #1, #3, #4, #5, #6 }] */
        /* JADX WARN: Removed duplicated region for block: B:78:0x0193 A[Catch: all -> 0x01d2, TryCatch #2 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0039, B:13:0x003f, B:14:0x004d, B:17:0x005a, B:18:0x0061, B:19:0x006d, B:21:0x0073, B:23:0x0087, B:25:0x008d, B:27:0x00a0, B:29:0x00a6, B:30:0x00ae, B:32:0x00b4, B:33:0x00ba, B:36:0x00c3, B:38:0x00cb, B:39:0x00da, B:41:0x00e0, B:42:0x00e6, B:45:0x00ef, B:47:0x00f7, B:48:0x010c, B:50:0x0112, B:51:0x0118, B:54:0x0121, B:56:0x0129, B:57:0x0137, B:59:0x013d, B:60:0x0143, B:63:0x014c, B:65:0x0154, B:66:0x015c, B:68:0x0162, B:69:0x0168, B:72:0x0171, B:74:0x0179, B:75:0x0180, B:76:0x018d, B:78:0x0193, B:80:0x01a7, B:82:0x01ab, B:83:0x01af, B:85:0x01b5, B:86:0x01c3, B:88:0x01c9, B:89:0x01d0), top: B:98:0x0007, inners: #0, #1, #3, #4, #5, #6 }] */
        /* JADX WARN: Removed duplicated region for block: B:85:0x01b5 A[Catch: all -> 0x01d2, LOOP:7: B:83:0x01af->B:85:0x01b5, LOOP_END, TryCatch #2 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0039, B:13:0x003f, B:14:0x004d, B:17:0x005a, B:18:0x0061, B:19:0x006d, B:21:0x0073, B:23:0x0087, B:25:0x008d, B:27:0x00a0, B:29:0x00a6, B:30:0x00ae, B:32:0x00b4, B:33:0x00ba, B:36:0x00c3, B:38:0x00cb, B:39:0x00da, B:41:0x00e0, B:42:0x00e6, B:45:0x00ef, B:47:0x00f7, B:48:0x010c, B:50:0x0112, B:51:0x0118, B:54:0x0121, B:56:0x0129, B:57:0x0137, B:59:0x013d, B:60:0x0143, B:63:0x014c, B:65:0x0154, B:66:0x015c, B:68:0x0162, B:69:0x0168, B:72:0x0171, B:74:0x0179, B:75:0x0180, B:76:0x018d, B:78:0x0193, B:80:0x01a7, B:82:0x01ab, B:83:0x01af, B:85:0x01b5, B:86:0x01c3, B:88:0x01c9, B:89:0x01d0), top: B:98:0x0007, inners: #0, #1, #3, #4, #5, #6 }] */
        /* JADX WARN: Removed duplicated region for block: B:88:0x01c9 A[Catch: all -> 0x01d2, TryCatch #2 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x001a, B:9:0x001c, B:11:0x0039, B:13:0x003f, B:14:0x004d, B:17:0x005a, B:18:0x0061, B:19:0x006d, B:21:0x0073, B:23:0x0087, B:25:0x008d, B:27:0x00a0, B:29:0x00a6, B:30:0x00ae, B:32:0x00b4, B:33:0x00ba, B:36:0x00c3, B:38:0x00cb, B:39:0x00da, B:41:0x00e0, B:42:0x00e6, B:45:0x00ef, B:47:0x00f7, B:48:0x010c, B:50:0x0112, B:51:0x0118, B:54:0x0121, B:56:0x0129, B:57:0x0137, B:59:0x013d, B:60:0x0143, B:63:0x014c, B:65:0x0154, B:66:0x015c, B:68:0x0162, B:69:0x0168, B:72:0x0171, B:74:0x0179, B:75:0x0180, B:76:0x018d, B:78:0x0193, B:80:0x01a7, B:82:0x01ab, B:83:0x01af, B:85:0x01b5, B:86:0x01c3, B:88:0x01c9, B:89:0x01d0), top: B:98:0x0007, inners: #0, #1, #3, #4, #5, #6 }] */
        @Override // android.content.ServiceConnection
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onServiceConnected(android.content.ComponentName r10, android.os.IBinder r11) {
            /*
                Method dump skipped, instruction units count: 469
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.TvInputManagerService.InputServiceConnection.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName component) {
            if (!this.mComponent.equals(component)) {
                throw new java.lang.IllegalArgumentException("Mismatched ComponentName: " + this.mComponent + " (expected), " + component + " (actual).");
            }
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(this.mUserId);
                com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(this.mComponent);
                if (serviceState != null) {
                    serviceState.reconnecting = true;
                    serviceState.bound = false;
                    serviceState.service = null;
                    serviceState.callback = null;
                    com.android.server.tv.TvInputManagerService.this.abortPendingCreateSessionRequestsLocked(serviceState, null, this.mUserId);
                }
            }
        }
    }

    private final class ServiceCallback extends android.media.tv.ITvInputServiceCallback.Stub {
        private final android.content.ComponentName mComponent;
        private final int mUserId;

        ServiceCallback(android.content.ComponentName component, int userId) {
            this.mComponent = component;
            this.mUserId = userId;
        }

        private void ensureHardwarePermission() {
            if (com.android.server.tv.TvInputManagerService.this.mContext.checkCallingPermission("android.permission.TV_INPUT_HARDWARE") != 0) {
                throw new java.lang.SecurityException("The caller does not have hardware permission");
            }
        }

        private void ensureValidInput(android.media.tv.TvInputInfo inputInfo) {
            if (inputInfo.getId() == null || !this.mComponent.equals(inputInfo.getComponent())) {
                throw new java.lang.IllegalArgumentException("Invalid TvInputInfo");
            }
        }

        public void addHardwareInput(int deviceId, android.media.tv.TvInputInfo inputInfo) {
            ensureHardwarePermission();
            ensureValidInput(inputInfo);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.ServiceState serviceState = com.android.server.tv.TvInputManagerService.this.getServiceStateLocked(this.mComponent, this.mUserId);
                    if (serviceState.hardwareInputMap.containsKey(inputInfo.getId())) {
                        return;
                    }
                    android.util.Slog.d("ServiceCallback", "addHardwareInput: device id " + deviceId + ", " + inputInfo.toString());
                    com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.addHardwareInput(deviceId, inputInfo);
                    com.android.server.tv.TvInputManagerService.this.addHardwareInputLocked(inputInfo, this.mComponent, this.mUserId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void addHdmiInput(int id, android.media.tv.TvInputInfo inputInfo) {
            ensureHardwarePermission();
            ensureValidInput(inputInfo);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    com.android.server.tv.TvInputManagerService.ServiceState serviceState = com.android.server.tv.TvInputManagerService.this.getServiceStateLocked(this.mComponent, this.mUserId);
                    if (serviceState.hardwareInputMap.containsKey(inputInfo.getId())) {
                        return;
                    }
                    com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.addHdmiInput(id, inputInfo);
                    com.android.server.tv.TvInputManagerService.this.addHardwareInputLocked(inputInfo, this.mComponent, this.mUserId);
                    if (com.android.server.tv.TvInputManagerService.this.mOnScreenInputId != null && com.android.server.tv.TvInputManagerService.this.mOnScreenSessionState != null) {
                        if (android.text.TextUtils.equals(com.android.server.tv.TvInputManagerService.this.mOnScreenInputId, inputInfo.getParentId())) {
                            com.android.server.tv.TvInputManagerService.this.logExternalInputEvent(1, inputInfo.getId(), com.android.server.tv.TvInputManagerService.this.mOnScreenSessionState);
                            com.android.server.tv.TvInputManagerService.this.mOnScreenInputId = inputInfo.getId();
                        } else if (android.text.TextUtils.equals(com.android.server.tv.TvInputManagerService.this.mOnScreenInputId, inputInfo.getId())) {
                            com.android.server.tv.TvInputManagerService.this.logExternalInputEvent(4, com.android.server.tv.TvInputManagerService.this.mOnScreenInputId, com.android.server.tv.TvInputManagerService.this.mOnScreenSessionState);
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void removeHardwareInput(java.lang.String inputId) {
            ensureHardwarePermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                    android.util.Slog.d("ServiceCallback", "removeHardwareInput " + inputId + " by " + this.mComponent);
                    com.android.server.tv.TvInputManagerService.this.removeHardwareInputLocked(inputId, this.mUserId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private final class SessionCallback extends android.media.tv.ITvInputSessionCallback.Stub {
        private final android.view.InputChannel[] mChannels;
        private final com.android.server.tv.TvInputManagerService.SessionState mSessionState;

        SessionCallback(com.android.server.tv.TvInputManagerService.SessionState sessionState, android.view.InputChannel[] channels) {
            this.mSessionState = sessionState;
            this.mChannels = channels;
        }

        public void onSessionCreated(android.media.tv.ITvInputSession session, android.os.IBinder hardwareSessionToken) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                this.mSessionState.session = session;
                this.mSessionState.hardwareSessionToken = hardwareSessionToken;
                if (session != null && addSessionTokenToClientStateLocked(session)) {
                    com.android.server.tv.TvInputManagerService.this.sendSessionTokenToClientLocked(this.mSessionState.client, this.mSessionState.inputId, this.mSessionState.sessionToken, this.mChannels[0], this.mSessionState.seq);
                } else {
                    com.android.server.tv.TvInputManagerService.this.removeSessionStateLocked(this.mSessionState.sessionToken, this.mSessionState.userId);
                    com.android.server.tv.TvInputManagerService.this.sendSessionTokenToClientLocked(this.mSessionState.client, this.mSessionState.inputId, null, null, this.mSessionState.seq);
                }
                this.mChannels[0].dispose();
            }
        }

        private boolean addSessionTokenToClientStateLocked(android.media.tv.ITvInputSession session) {
            try {
                session.asBinder().linkToDeath(this.mSessionState, 0);
                android.os.IBinder clientToken = this.mSessionState.client.asBinder();
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(this.mSessionState.userId);
                com.android.server.tv.TvInputManagerService.ClientState clientState = (com.android.server.tv.TvInputManagerService.ClientState) userState.clientStateMap.get(clientToken);
                if (clientState == null) {
                    clientState = com.android.server.tv.TvInputManagerService.this.new ClientState(clientToken, this.mSessionState.userId);
                    try {
                        clientToken.linkToDeath(clientState, 0);
                        userState.clientStateMap.put(clientToken, clientState);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "client process has already died", e);
                        return false;
                    }
                }
                clientState.sessionTokens.add(this.mSessionState.sessionToken);
                return true;
            } catch (android.os.RemoteException e2) {
                android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "session process has already died", e2);
                return false;
            }
        }

        public void onChannelRetuned(android.net.Uri channelUri) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onChannelRetuned(channelUri, this.mSessionState.seq);
                    if (!this.mSessionState.isCurrent || !java.util.Objects.equals(this.mSessionState.currentChannel, channelUri)) {
                        com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(this.mSessionState.userId);
                        this.mSessionState.isCurrent = true;
                        this.mSessionState.currentChannel = channelUri;
                        com.android.server.tv.TvInputManagerService.this.notifyCurrentChannelInfosUpdatedLocked(userState);
                        if (!this.mSessionState.isRecordingSession) {
                            java.lang.String sessionActualInputId = com.android.server.tv.TvInputManagerService.this.getSessionActualInputId(this.mSessionState);
                            if (!android.text.TextUtils.equals(com.android.server.tv.TvInputManagerService.this.mOnScreenInputId, sessionActualInputId)) {
                                com.android.server.tv.TvInputManagerService.this.logExternalInputEvent(1, sessionActualInputId, this.mSessionState);
                            }
                            com.android.server.tv.TvInputManagerService.this.mOnScreenInputId = sessionActualInputId;
                            com.android.server.tv.TvInputManagerService.this.mOnScreenSessionState = this.mSessionState;
                        }
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onChannelRetuned", e);
                }
            }
        }

        public void onAudioPresentationsChanged(java.util.List<android.media.AudioPresentation> audioPresentations) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onAudioPresentationsChanged(audioPresentations, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onAudioPresentationsChanged", e);
                }
            }
        }

        public void onAudioPresentationSelected(int presentationId, int programId) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onAudioPresentationSelected(presentationId, programId, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onAudioPresentationSelected", e);
                }
            }
        }

        public void onTracksChanged(java.util.List<android.media.tv.TvTrackInfo> tracks) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTracksChanged(tracks, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTracksChanged", e);
                }
            }
        }

        public void onTrackSelected(int type, java.lang.String trackId) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTrackSelected(type, trackId, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTrackSelected", e);
                }
            }
        }

        public void onVideoAvailable() {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session != null && this.mSessionState.client != null) {
                    com.android.server.tv.TvInputManagerService.TvInputState tvInputState = com.android.server.tv.TvInputManagerService.getTvInputState(this.mSessionState, com.android.server.tv.TvInputManagerService.this.getUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId));
                    try {
                        this.mSessionState.client.onVideoAvailable(this.mSessionState.seq);
                        com.android.server.tv.TvInputManagerService.this.logTuneStateChanged(6, this.mSessionState, tvInputState);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onVideoAvailable", e);
                    }
                }
            }
        }

        public void onVideoUnavailable(int reason) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session != null && this.mSessionState.client != null) {
                    com.android.server.tv.TvInputManagerService.TvInputState tvInputState = com.android.server.tv.TvInputManagerService.getTvInputState(this.mSessionState, com.android.server.tv.TvInputManagerService.this.getUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId));
                    try {
                        this.mSessionState.client.onVideoUnavailable(reason, this.mSessionState.seq);
                        int loggedReason = com.android.server.tv.TvInputManagerService.getVideoUnavailableReasonForStatsd(reason);
                        com.android.server.tv.TvInputManagerService.this.logTuneStateChanged(loggedReason, this.mSessionState, tvInputState);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onVideoUnavailable", e);
                    }
                }
            }
        }

        public void onVideoFreezeUpdated(boolean isFrozen) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onVideoFreezeUpdated(isFrozen, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onVideoFreezeUpdated", e);
                }
            }
        }

        public void onContentAllowed() {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onContentAllowed(this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onContentAllowed", e);
                }
            }
        }

        public void onContentBlocked(java.lang.String rating) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onContentBlocked(rating, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onContentBlocked", e);
                }
            }
        }

        public void onLayoutSurface(int left, int top, int right, int bottom) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onLayoutSurface(left, top, right, bottom, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onLayoutSurface", e);
                }
            }
        }

        public void onSessionEvent(java.lang.String eventType, android.os.Bundle eventArgs) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onSessionEvent(eventType, eventArgs, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onSessionEvent", e);
                }
            }
        }

        public void onTimeShiftStatusChanged(int status) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTimeShiftStatusChanged(status, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTimeShiftStatusChanged", e);
                }
            }
        }

        public void onTimeShiftStartPositionChanged(long timeMs) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTimeShiftStartPositionChanged(timeMs, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTimeShiftStartPositionChanged", e);
                }
            }
        }

        public void onTimeShiftCurrentPositionChanged(long timeMs) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTimeShiftCurrentPositionChanged(timeMs, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTimeShiftCurrentPositionChanged", e);
                }
            }
        }

        public void onAitInfoUpdated(android.media.tv.AitInfo aitInfo) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onAitInfoUpdated(aitInfo, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onAitInfoUpdated", e);
                }
            }
        }

        public void onSignalStrength(int strength) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onSignalStrength(strength, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onSignalStrength", e);
                }
            }
        }

        public void onCueingMessageAvailability(boolean available) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onCueingMessageAvailability(available, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onCueingMessageAvailability", e);
                }
            }
        }

        public void onTimeShiftMode(int mode) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTimeShiftMode(mode, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTimeShiftMode", e);
                }
            }
        }

        public void onAvailableSpeeds(float[] speeds) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onAvailableSpeeds(speeds, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onAvailableSpeeds", e);
                }
            }
        }

        public void onTuned(android.net.Uri channelUri) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTuned(channelUri, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTuned", e);
                }
            }
        }

        public void onTvMessage(int type, android.os.Bundle data) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTvMessage(type, data, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTvMessage", e);
                }
            }
        }

        public void onRecordingStopped(android.net.Uri recordedProgramUri) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onRecordingStopped(recordedProgramUri, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onRecordingStopped", e);
                }
            }
        }

        public void onError(int error) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onError(error, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onError", e);
                }
            }
        }

        public void onBroadcastInfoResponse(android.media.tv.BroadcastInfoResponse response) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onBroadcastInfoResponse(response, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onBroadcastInfoResponse", e);
                }
            }
        }

        public void onAdResponse(android.media.tv.AdResponse response) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onAdResponse(response, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onAdResponse", e);
                }
            }
        }

        public void onAdBufferConsumed(android.media.tv.AdBuffer buffer) {
            android.os.SharedMemory sharedMemory;
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session != null) {
                    try {
                        if (this.mSessionState.client != null) {
                            try {
                                this.mSessionState.client.onAdBufferConsumed(buffer, this.mSessionState.seq);
                            } catch (android.os.RemoteException e) {
                                android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onAdBufferConsumed", e);
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

        public void onTvInputSessionData(java.lang.String type, android.os.Bundle data) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                if (this.mSessionState.session == null || this.mSessionState.client == null) {
                    return;
                }
                try {
                    this.mSessionState.client.onTvInputSessionData(type, data, this.mSessionState.seq);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTvInputSessionData", e);
                }
            }
        }
    }

    static int getVideoUnavailableReasonForStatsd(int reason) {
        int loggedReason = reason + 100;
        if (loggedReason < 100 || loggedReason > 118) {
            return 100;
        }
        return loggedReason;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.tv.TvInputManagerService.UserState getUserStateLocked(int userId) {
        return this.mUserStates.get(userId);
    }

    private final class MessageHandler extends android.os.Handler {
        static final int MSG_LOG_WATCH_END = 2;
        static final int MSG_LOG_WATCH_START = 1;
        static final int MSG_SWITCH_CONTENT_RESOLVER = 3;
        static final int MSG_UPDATE_HARDWARE_TIS_BINDING = 4;
        private android.content.ContentResolver mContentResolver;

        MessageHandler(android.content.ContentResolver contentResolver, android.os.Looper looper) {
            super(looper);
            this.mContentResolver = contentResolver;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    java.lang.String packageName = (java.lang.String) args.arg1;
                    long watchStartTime = ((java.lang.Long) args.arg2).longValue();
                    long channelId = ((java.lang.Long) args.arg3).longValue();
                    android.os.Bundle tuneParams = (android.os.Bundle) args.arg4;
                    android.os.IBinder sessionToken = (android.os.IBinder) args.arg5;
                    android.content.ContentValues values = new android.content.ContentValues();
                    values.put("package_name", packageName);
                    values.put("watch_start_time_utc_millis", java.lang.Long.valueOf(watchStartTime));
                    values.put("channel_id", java.lang.Long.valueOf(channelId));
                    if (tuneParams != null) {
                        values.put("tune_params", encodeTuneParams(tuneParams));
                    }
                    values.put("session_token", sessionToken.toString());
                    try {
                        this.mContentResolver.insert(android.media.tv.TvContract.WatchedPrograms.CONTENT_URI, values);
                        break;
                    } catch (java.lang.IllegalArgumentException ex) {
                        android.util.Slog.w(com.android.server.tv.TvInputManagerService.TAG, "error in insert db for MSG_LOG_WATCH_START", ex);
                    }
                    args.recycle();
                    return;
                case 2:
                    com.android.internal.os.SomeArgs args2 = (com.android.internal.os.SomeArgs) msg.obj;
                    android.os.IBinder sessionToken2 = (android.os.IBinder) args2.arg1;
                    long watchEndTime = ((java.lang.Long) args2.arg2).longValue();
                    android.content.ContentValues values2 = new android.content.ContentValues();
                    values2.put("watch_end_time_utc_millis", java.lang.Long.valueOf(watchEndTime));
                    values2.put("session_token", sessionToken2.toString());
                    try {
                        this.mContentResolver.insert(android.media.tv.TvContract.WatchedPrograms.CONTENT_URI, values2);
                        break;
                    } catch (java.lang.IllegalArgumentException ex2) {
                        android.util.Slog.w(com.android.server.tv.TvInputManagerService.TAG, "error in insert db for MSG_LOG_WATCH_END", ex2);
                    }
                    args2.recycle();
                    return;
                case 3:
                    this.mContentResolver = (android.content.ContentResolver) msg.obj;
                    return;
                case 4:
                    com.android.internal.os.SomeArgs args3 = (com.android.internal.os.SomeArgs) msg.obj;
                    int userId = ((java.lang.Integer) args3.arg1).intValue();
                    synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                        com.android.server.tv.TvInputManagerService.this.updateHardwareTvInputServiceBindingLocked(userId);
                        break;
                    }
                    args3.recycle();
                    return;
                default:
                    android.util.Slog.w(com.android.server.tv.TvInputManagerService.TAG, "unhandled message code: " + msg.what);
                    return;
            }
        }

        private java.lang.String encodeTuneParams(android.os.Bundle tuneParams) {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            java.util.Set<java.lang.String> keySet = tuneParams.keySet();
            java.util.Iterator<java.lang.String> it = keySet.iterator();
            while (it.hasNext()) {
                java.lang.String key = it.next();
                java.lang.Object value = tuneParams.get(key);
                if (value != null) {
                    builder.append(replaceEscapeCharacters(key));
                    builder.append("=");
                    builder.append(replaceEscapeCharacters(value.toString()));
                    if (it.hasNext()) {
                        builder.append(", ");
                    }
                }
            }
            return builder.toString();
        }

        private java.lang.String replaceEscapeCharacters(java.lang.String src) {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            for (char ch : src.toCharArray()) {
                if ("%=,".indexOf(ch) >= 0) {
                    builder.append('%');
                }
                builder.append(ch);
            }
            return builder.toString();
        }
    }

    private final class HardwareListener implements com.android.server.tv.TvInputHardwareManager.Listener {
        private HardwareListener() {
        }

        @Override // com.android.server.tv.TvInputHardwareManager.Listener
        public void onStateChanged(java.lang.String inputId, int state) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                com.android.server.tv.TvInputManagerService.this.setStateLocked(inputId, state, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
            }
        }

        @Override // com.android.server.tv.TvInputHardwareManager.Listener
        public void onHardwareDeviceAdded(android.media.tv.TvInputHardwareInfo info) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                for (com.android.server.tv.TvInputManagerService.ServiceState serviceState : userState.serviceStateMap.values()) {
                    if (serviceState.isHardware) {
                        try {
                            com.android.server.tv.TvInputManagerService.this.bindService(serviceState, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                            if (serviceState.service != null) {
                                serviceState.service.notifyHardwareAdded(info);
                            }
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyHardwareAdded", e);
                        }
                    }
                }
                com.android.server.tv.TvInputManagerService.this.updateHardwareServiceConnectionDelayed(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
            }
        }

        @Override // com.android.server.tv.TvInputHardwareManager.Listener
        public void onHardwareDeviceRemoved(android.media.tv.TvInputHardwareInfo info) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                java.lang.String relatedInputId = com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.getHardwareInputIdMap().get(info.getDeviceId());
                com.android.server.tv.TvInputManagerService.this.removeHardwareInputLocked(relatedInputId, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                for (com.android.server.tv.TvInputManagerService.ServiceState serviceState : userState.serviceStateMap.values()) {
                    if (serviceState.isHardware) {
                        try {
                            com.android.server.tv.TvInputManagerService.this.bindService(serviceState, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                            if (serviceState.service != null) {
                                serviceState.service.notifyHardwareRemoved(info);
                            } else {
                                serviceState.hardwareDeviceRemovedBuffer.add(info);
                            }
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyHardwareRemoved", e);
                        }
                    }
                }
                com.android.server.tv.TvInputManagerService.this.updateHardwareServiceConnectionDelayed(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
            }
        }

        @Override // com.android.server.tv.TvInputHardwareManager.Listener
        public void onHdmiDeviceAdded(android.hardware.hdmi.HdmiDeviceInfo deviceInfo) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                for (com.android.server.tv.TvInputManagerService.ServiceState serviceState : userState.serviceStateMap.values()) {
                    if (serviceState.isHardware) {
                        try {
                            com.android.server.tv.TvInputManagerService.this.bindService(serviceState, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                            if (serviceState.service != null) {
                                serviceState.service.notifyHdmiDeviceAdded(deviceInfo);
                            }
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyHdmiDeviceAdded", e);
                        }
                    }
                }
                com.android.server.tv.TvInputManagerService.this.updateHardwareServiceConnectionDelayed(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
            }
        }

        @Override // com.android.server.tv.TvInputHardwareManager.Listener
        public void onHdmiDeviceRemoved(android.hardware.hdmi.HdmiDeviceInfo deviceInfo) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                java.lang.String relatedInputId = com.android.server.tv.TvInputManagerService.this.mTvInputHardwareManager.getHdmiInputIdMap().get(deviceInfo.getId());
                com.android.server.tv.TvInputManagerService.this.removeHardwareInputLocked(relatedInputId, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                for (com.android.server.tv.TvInputManagerService.ServiceState serviceState : userState.serviceStateMap.values()) {
                    if (serviceState.isHardware) {
                        try {
                            com.android.server.tv.TvInputManagerService.this.bindService(serviceState, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                            if (serviceState.service != null) {
                                serviceState.service.notifyHdmiDeviceRemoved(deviceInfo);
                            } else {
                                serviceState.hdmiDeviceRemovedBuffer.add(deviceInfo);
                            }
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyHdmiDeviceRemoved", e);
                        }
                    }
                }
                com.android.server.tv.TvInputManagerService.this.updateHardwareServiceConnectionDelayed(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
            }
        }

        @Override // com.android.server.tv.TvInputHardwareManager.Listener
        public void onHdmiDeviceUpdated(java.lang.String inputId, android.hardware.hdmi.HdmiDeviceInfo deviceInfo) {
            java.lang.Integer state;
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                switch (deviceInfo.getDevicePowerStatus()) {
                    case 0:
                        state = 0;
                        break;
                    case 1:
                    case 2:
                    case 3:
                        state = 1;
                        break;
                    default:
                        state = null;
                        break;
                }
                if (state != null) {
                    com.android.server.tv.TvInputManagerService.this.setStateLocked(inputId, state.intValue(), com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                }
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                for (com.android.server.tv.TvInputManagerService.ServiceState serviceState : userState.serviceStateMap.values()) {
                    if (serviceState.isHardware) {
                        try {
                            com.android.server.tv.TvInputManagerService.this.bindService(serviceState, com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                            if (serviceState.service != null) {
                                serviceState.service.notifyHdmiDeviceUpdated(deviceInfo);
                            } else {
                                serviceState.hdmiDeviceUpdatedBuffer.add(deviceInfo);
                            }
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in notifyHdmiDeviceUpdated", e);
                        }
                    }
                }
                com.android.server.tv.TvInputManagerService.this.updateHardwareServiceConnectionDelayed(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
            }
        }

        @Override // com.android.server.tv.TvInputHardwareManager.Listener
        public void onTvMessage(java.lang.String inputId, int type, android.os.Bundle data) {
            synchronized (com.android.server.tv.TvInputManagerService.this.mLock) {
                com.android.server.tv.TvInputManagerService.UserState userState = com.android.server.tv.TvInputManagerService.this.getOrCreateUserStateLocked(com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                com.android.server.tv.TvInputManagerService.TvInputState inputState = (com.android.server.tv.TvInputManagerService.TvInputState) userState.inputMap.get(inputId);
                if (inputState == null) {
                    android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "failed to send TV message - unknown input id " + inputId);
                    return;
                }
                com.android.server.tv.TvInputManagerService.ServiceState serviceState = (com.android.server.tv.TvInputManagerService.ServiceState) userState.serviceStateMap.get(inputState.info.getComponent());
                for (android.os.IBinder token : serviceState.sessionTokens) {
                    try {
                        com.android.server.tv.TvInputManagerService.SessionState sessionState = com.android.server.tv.TvInputManagerService.this.getSessionStateLocked(token, android.os.Binder.getCallingUid(), com.android.server.tv.TvInputManagerService.this.mCurrentUserId);
                        if (!sessionState.isRecordingSession && sessionState.hardwareSessionToken != null) {
                            sessionState.session.notifyTvMessage(type, data);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.tv.TvInputManagerService.TAG, "error in onTvMessage", e);
                    }
                }
            }
        }
    }

    private static class SessionNotFoundException extends java.lang.IllegalArgumentException {
        public SessionNotFoundException(java.lang.String name) {
            super(name);
        }
    }

    private static class ClientPidNotFoundException extends java.lang.IllegalArgumentException {
        public ClientPidNotFoundException(java.lang.String name) {
            super(name);
        }
    }
}
