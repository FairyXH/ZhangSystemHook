package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public class MediaSessionService extends com.android.server.SystemService implements com.android.server.Watchdog.Monitor {
    static final boolean DEBUG_KEY_EVENT = true;
    private static final int MAX_BUTTON_RECEIVER_SIZE = 10;
    private static final java.lang.String MEDIA_BUTTON_RECEIVER = "media_button_receiver";
    private static final java.lang.String MEDIA_ID_DELIM = "-";
    private static final int MEDIA_KEY_LISTENER_TIMEOUT = 1000;
    private static final int SESSION_CREATION_LIMIT_PER_UID = 100;
    private static final java.lang.String USAGE_STATS_ACTION_START = "start";
    private static final java.lang.String USAGE_STATS_ACTION_STOP = "stop";
    private static final java.lang.String USAGE_STATS_CATEGORY = "android.media";
    private static final int WAKELOCK_TIMEOUT = 5000;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private android.media.AudioManager mAudioManager;
    private com.android.server.media.AudioPlayerStateMonitor mAudioPlayerStateMonitor;
    private android.media.MediaCommunicationManager mCommunicationManager;
    private final android.content.Context mContext;
    private com.android.server.media.MediaSessionService.FullUserRecord mCurrentFullUserRecord;
    private com.android.server.media.MediaKeyDispatcher mCustomMediaKeyDispatcher;
    private com.android.server.media.MediaSessionPolicyProvider mCustomMediaSessionPolicyProvider;
    private final java.util.Set<com.android.server.media.MediaSessionRecordImpl> mFgsAllowedMediaSessionRecords;
    private final android.util.SparseIntArray mFullUserIds;
    private com.android.server.media.MediaSessionRecord mGlobalPrioritySession;
    private final com.android.server.media.MediaSessionService.MessageHandler mHandler;
    private boolean mHasFeatureLeanback;
    private android.app.KeyguardManager mKeyguardManager;
    private final java.lang.Object mLock;
    private final android.os.PowerManager.WakeLock mMediaEventWakeLock;
    private final java.util.Map<java.lang.Integer, java.util.Set<android.app.Notification>> mMediaNotifications;
    private com.android.server.media.MediaSessionService.MediaSessionServiceWrapper mMediaSSWrapper;
    private com.android.server.media.IMediaSessionServiceExt mMediaSessionServiceExt;
    private com.android.server.media.MediaSessionService.NotificationListener mNotificationListener;
    private final android.content.BroadcastReceiver mNotificationListenerEnabledChangedReceiver;
    private final android.app.NotificationManager mNotificationManager;
    private final android.os.HandlerThread mRecordThread;
    final android.os.RemoteCallbackList<android.media.IRemoteSessionCallback> mRemoteVolumeControllers;
    private final android.media.MediaCommunicationManager.SessionCallback mSession2TokenCallback;
    private final java.util.List<com.android.server.media.MediaSessionService.Session2TokensListenerRecord> mSession2TokensListenerRecords;
    private final com.android.server.media.MediaSessionService.SessionManagerImpl mSessionManagerImpl;
    private final java.util.ArrayList<com.android.server.media.MediaSessionService.SessionsListenerRecord> mSessionsListeners;
    private android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal;
    private final java.util.Map<java.lang.Integer, java.util.Set<com.android.server.media.MediaSessionRecordImpl>> mUserEngagedSessionsForFgs;
    private final android.util.SparseArray<java.util.Set<com.android.server.media.MediaSessionRecordImpl>> mUserEngagedSessionsForUsageLogging;
    private final android.util.SparseArray<com.android.server.media.MediaSessionService.FullUserRecord> mUserRecords;
    private static final java.lang.String TAG = "MediaSessionService";
    static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final int LONG_PRESS_TIMEOUT = android.view.ViewConfiguration.getLongPressTimeout() + 50;
    private static final int MULTI_TAP_TIMEOUT = android.view.ViewConfiguration.getMultiPressTimeout();

    /* JADX WARN: Multi-variable type inference failed */
    public MediaSessionService(android.content.Context context) {
        super(context);
        this.mHandler = new com.android.server.media.MediaSessionService.MessageHandler();
        this.mLock = new java.lang.Object();
        this.mRecordThread = new android.os.HandlerThread("SessionRecordThread");
        this.mFullUserIds = new android.util.SparseIntArray();
        this.mUserRecords = new android.util.SparseArray<>();
        this.mSessionsListeners = new java.util.ArrayList<>();
        this.mSession2TokensListenerRecords = new java.util.ArrayList();
        this.mUserEngagedSessionsForUsageLogging = new android.util.SparseArray<>();
        this.mUserEngagedSessionsForFgs = new java.util.HashMap();
        this.mMediaNotifications = new java.util.HashMap();
        this.mFgsAllowedMediaSessionRecords = new java.util.HashSet();
        this.mRemoteVolumeControllers = new android.os.RemoteCallbackList<>();
        this.mSession2TokenCallback = new android.media.MediaCommunicationManager.SessionCallback() { // from class: com.android.server.media.MediaSessionService.1
            public void onSession2TokenCreated(android.media.Session2Token token, int pid) {
                addSession(token, pid);
            }

            private void addSession(android.media.Session2Token token, int pid) {
                if (com.android.server.media.MediaSessionService.DEBUG) {
                    android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Session2 is created " + token);
                }
                com.android.server.media.MediaSession2Record record = new com.android.server.media.MediaSession2Record(token, com.android.server.media.MediaSessionService.this, com.android.server.media.MediaSessionService.this.mRecordThread.getLooper(), pid, 0);
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(record.getUserId());
                    if (user != null) {
                        user.mPriorityStack.addSession(record);
                    }
                }
            }
        };
        this.mMediaSSWrapper = new com.android.server.media.MediaSessionService.MediaSessionServiceWrapper();
        this.mMediaSessionServiceExt = (com.android.server.media.IMediaSessionServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.media.IMediaSessionServiceExt.class).base(this).create();
        this.mNotificationListenerEnabledChangedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.media.MediaSessionService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.media.MediaSessionService.this.updateActiveSessionListeners();
            }
        };
        this.mContext = context;
        this.mSessionManagerImpl = new com.android.server.media.MediaSessionService.SessionManagerImpl();
        this.mMediaSessionServiceExt.init(context);
        this.mMediaEventWakeLock = ((android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class)).newWakeLock(1, "handleMediaEvent");
        this.mNotificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
        this.mNotificationListener = new com.android.server.media.MediaSessionService.NotificationListener();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("media_session", this.mSessionManagerImpl);
        com.android.server.Watchdog.getInstance().addMonitor(this);
        this.mKeyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService("keyguard");
        this.mAudioPlayerStateMonitor = com.android.server.media.AudioPlayerStateMonitor.getInstance(this.mContext);
        this.mAudioPlayerStateMonitor.registerListener(new com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener() { // from class: com.android.server.media.MediaSessionService$$ExternalSyntheticLambda0
            @Override // com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener
            public final void onAudioPlayerActiveStateChanged(android.media.AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z) {
                this.f$0.lambda$onStart$0(audioPlaybackConfiguration, z);
            }
        }, null);
        this.mHasFeatureLeanback = this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        updateUser();
        instantiateCustomProvider(this.mContext.getResources().getString(android.R.string.config_customMediaSessionPolicyProvider));
        instantiateCustomDispatcher(this.mContext.getResources().getString(android.R.string.config_customMediaKeyDispatcher));
        this.mRecordThread.start();
        android.content.IntentFilter filter = new android.content.IntentFilter("android.app.action.NOTIFICATION_LISTENER_ENABLED_CHANGED");
        this.mContext.registerReceiver(this.mNotificationListenerEnabledChangedReceiver, filter);
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(android.media.AudioPlaybackConfiguration config, boolean isRemoved) {
        if (DEBUG) {
            android.util.Log.d(TAG, "Audio playback is changed, config=" + config + ", removed=" + isRemoved);
        }
        if (config.getPlayerType() == 3) {
            return;
        }
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(android.os.UserHandle.getUserHandleForUid(config.getClientUid()).getIdentifier());
            if (user != null) {
                user.mPriorityStack.updateMediaButtonSessionIfNeeded();
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        super.onBootPhase(phase);
        switch (phase) {
            case 550:
                com.android.server.media.MediaSessionDeviceConfig.initialize(this.mContext);
                break;
            case 1000:
                this.mCommunicationManager = (android.media.MediaCommunicationManager) this.mContext.getSystemService(android.media.MediaCommunicationManager.class);
                this.mCommunicationManager.registerSessionCallback(new com.android.server.media.HandlerExecutor(this.mHandler), this.mSession2TokenCallback);
                if (com.android.media.flags.Flags.enableNotifyingActivityManagerWithMediaSessionStatusChange()) {
                    try {
                        this.mNotificationListener.registerAsSystemService(this.mContext, new android.content.ComponentName(this.mContext, (java.lang.Class<?>) com.android.server.media.MediaSessionService.NotificationListener.class), -1);
                    } catch (android.os.RemoteException e) {
                        return;
                    }
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGlobalPriorityActiveLocked() {
        return this.mGlobalPrioritySession != null && this.mGlobalPrioritySession.isActive();
    }

    void onSessionActiveStateChanged(com.android.server.media.MediaSessionRecordImpl record, android.media.session.PlaybackState playbackState) {
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(record.getUserId());
            if (user == null) {
                android.util.Log.w(TAG, "Unknown session updated. Ignoring.");
                return;
            }
            if (record.isSystemPriority()) {
                android.util.Log.d(TAG, "Global priority session updated - user id=" + record.getUserId() + " package=" + record.getPackageName() + " active=" + record.isActive());
                user.pushAddressedPlayerChangedLocked();
            } else {
                if (!user.mPriorityStack.contains(record)) {
                    android.util.Log.w(TAG, "Unknown session updated. Ignoring.");
                    return;
                }
                user.mPriorityStack.onSessionActiveStateChanged(record);
            }
            boolean isUserEngaged = isUserEngaged(record, playbackState);
            android.util.Log.d(TAG, "onSessionActiveStateChanged: record=" + record + " playbackState=" + playbackState);
            reportMediaInteractionEvent(record, isUserEngaged);
            this.mHandler.postSessionsChanged(record);
        }
    }

    private boolean isUserEngaged(com.android.server.media.MediaSessionRecordImpl record, android.media.session.PlaybackState playbackState) {
        if (playbackState == null) {
            return record.checkPlaybackActiveState(true);
        }
        return playbackState.isActive() && record.isActive();
    }

    void setGlobalPrioritySession(com.android.server.media.MediaSessionRecord record) {
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(record.getUserId());
            if (this.mGlobalPrioritySession != record) {
                android.util.Log.d(TAG, "Global priority session is changed from " + this.mGlobalPrioritySession + " to " + record);
                this.mGlobalPrioritySession = record;
                if (user != null && user.mPriorityStack.contains(record)) {
                    user.mPriorityStack.removeSession(record);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<com.android.server.media.MediaSessionRecord> getActiveSessionsLocked(int userId) {
        java.util.List<com.android.server.media.MediaSessionRecord> records = new java.util.ArrayList<>();
        if (userId == android.os.UserHandle.ALL.getIdentifier()) {
            int size = this.mUserRecords.size();
            for (int i = 0; i < size; i++) {
                records.addAll(this.mUserRecords.valueAt(i).mPriorityStack.getActiveSessions(userId));
            }
        } else {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(userId);
            if (user == null) {
                android.util.Log.w(TAG, "getSessions failed. Unknown user " + userId);
                return records;
            }
            records.addAll(user.mPriorityStack.getActiveSessions(userId));
        }
        if (isGlobalPriorityActiveLocked() && (userId == android.os.UserHandle.ALL.getIdentifier() || userId == this.mGlobalPrioritySession.getUserId())) {
            records.add(0, this.mGlobalPrioritySession);
        }
        return records;
    }

    java.util.List<android.media.Session2Token> getSession2TokensLocked(int userId) {
        java.util.List<android.media.Session2Token> list = new java.util.ArrayList<>();
        if (userId == android.os.UserHandle.ALL.getIdentifier()) {
            int size = this.mUserRecords.size();
            for (int i = 0; i < size; i++) {
                list.addAll(this.mUserRecords.valueAt(i).mPriorityStack.getSession2Tokens(userId));
            }
        } else {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(userId);
            list.addAll(user.mPriorityStack.getSession2Tokens(userId));
        }
        return list;
    }

    public void notifyRemoteVolumeChanged(int flags, com.android.server.media.MediaSessionRecord session) {
        if (!session.isActive()) {
            return;
        }
        synchronized (this.mLock) {
            int size = this.mRemoteVolumeControllers.beginBroadcast();
            android.media.session.MediaSession.Token token = session.getSessionToken();
            for (int i = size - 1; i >= 0; i--) {
                try {
                    android.media.IRemoteSessionCallback cb = this.mRemoteVolumeControllers.getBroadcastItem(i);
                    cb.onVolumeChanged(token, flags);
                } catch (java.lang.Exception e) {
                    android.util.Log.w(TAG, "Error sending volume change.", e);
                }
            }
            this.mRemoteVolumeControllers.finishBroadcast();
        }
    }

    void onSessionPlaybackStateChanged(com.android.server.media.MediaSessionRecordImpl record, boolean shouldUpdatePriority, android.media.session.PlaybackState playbackState) {
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(record.getUserId());
            if (user != null && user.mPriorityStack.contains(record)) {
                user.mPriorityStack.onPlaybackStateChanged(record, shouldUpdatePriority);
                boolean isUserEngaged = isUserEngaged(record, playbackState);
                android.util.Log.d(TAG, "onSessionPlaybackStateChanged: record=" + record + " playbackState=" + playbackState);
                reportMediaInteractionEvent(record, isUserEngaged);
                return;
            }
            android.util.Log.d(TAG, "Unknown session changed playback state. Ignoring.");
        }
    }

    void onSessionPlaybackTypeChanged(com.android.server.media.MediaSessionRecord record) {
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(record.getUserId());
            if (user != null && user.mPriorityStack.contains(record)) {
                pushRemoteVolumeUpdateLocked(record.getUserId());
                return;
            }
            android.util.Log.d(TAG, "Unknown session changed playback type. Ignoring.");
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        if (DEBUG) {
            android.util.Log.d(TAG, "onStartUser: " + user);
        }
        updateUser();
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        if (DEBUG) {
            android.util.Log.d(TAG, "onSwitchUser: " + to);
        }
        updateUser();
    }

    @Override // com.android.server.SystemService
    public void onUserStopped(com.android.server.SystemService.TargetUser targetUser) {
        int userId = targetUser.getUserIdentifier();
        if (DEBUG) {
            android.util.Log.d(TAG, "onCleanupUser: " + userId);
        }
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(userId);
            if (user != null) {
                if (user.mFullUserId == userId) {
                    user.destroySessionsForUserLocked(android.os.UserHandle.ALL.getIdentifier());
                    this.mUserRecords.remove(userId);
                } else {
                    user.destroySessionsForUserLocked(userId);
                }
            }
            updateUser();
        }
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    protected void enforcePhoneStatePermission(int pid, int uid) {
        if (this.mContext.checkPermission("android.permission.MODIFY_PHONE_STATE", pid, uid) != 0) {
            throw new java.lang.SecurityException("Must hold the MODIFY_PHONE_STATE permission.");
        }
    }

    void onSessionDied(com.android.server.media.MediaSessionRecordImpl session) {
        synchronized (this.mLock) {
            destroySessionLocked(session);
        }
    }

    private void updateUser() {
        synchronized (this.mLock) {
            android.os.UserManager manager = (android.os.UserManager) this.mContext.getSystemService("user");
            this.mFullUserIds.clear();
            java.util.List<android.os.UserHandle> allUsers = manager.getUserHandles(false);
            if (allUsers != null) {
                for (android.os.UserHandle user : allUsers) {
                    android.os.UserHandle parent = manager.getProfileParent(user);
                    if (parent != null) {
                        this.mFullUserIds.put(user.getIdentifier(), parent.getIdentifier());
                    } else {
                        this.mFullUserIds.put(user.getIdentifier(), user.getIdentifier());
                        if (this.mUserRecords.get(user.getIdentifier()) == null) {
                            this.mUserRecords.put(user.getIdentifier(), new com.android.server.media.MediaSessionService.FullUserRecord(user.getIdentifier()));
                        }
                    }
                }
            }
            int currentFullUserId = android.app.ActivityManager.getCurrentUser();
            this.mCurrentFullUserRecord = this.mUserRecords.get(currentFullUserId);
            if (this.mCurrentFullUserRecord == null) {
                android.util.Log.w(TAG, "Cannot find FullUserInfo for the current user " + currentFullUserId);
                this.mCurrentFullUserRecord = new com.android.server.media.MediaSessionService.FullUserRecord(currentFullUserId);
                this.mUserRecords.put(currentFullUserId, this.mCurrentFullUserRecord);
            }
            this.mFullUserIds.put(currentFullUserId, currentFullUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveSessionListeners() {
        synchronized (this.mLock) {
            for (int i = this.mSessionsListeners.size() - 1; i >= 0; i--) {
                com.android.server.media.MediaSessionService.SessionsListenerRecord listener = this.mSessionsListeners.get(i);
                try {
                    java.lang.String packageName = listener.componentName == null ? null : listener.componentName.getPackageName();
                    enforceMediaPermissions(packageName, listener.pid, listener.uid, listener.userId);
                } catch (java.lang.SecurityException e) {
                    android.util.Log.i(TAG, "ActiveSessionsListener " + listener.componentName + " is no longer authorized. Disconnecting.");
                    this.mSessionsListeners.remove(i);
                    try {
                        listener.listener.onActiveSessionsChanged(new java.util.ArrayList());
                    } catch (java.lang.Exception e2) {
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroySessionLocked(com.android.server.media.MediaSessionRecordImpl session) {
        if (DEBUG) {
            android.util.Log.d(TAG, "Destroying " + session);
        }
        if (session.isClosed()) {
            android.util.Log.w(TAG, "Destroying already destroyed session. Ignoring.");
            return;
        }
        com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(session.getUserId());
        if (user != null && (session instanceof com.android.server.media.MediaSessionRecord)) {
            int uid = session.getUid();
            int sessionCount = user.mUidToSessionCount.get(uid, 0);
            if (sessionCount <= 0) {
                android.util.Log.w(TAG, "destroySessionLocked: sessionCount should be positive. sessionCount=" + sessionCount);
            } else {
                user.mUidToSessionCount.put(uid, sessionCount - 1);
            }
        }
        if (this.mGlobalPrioritySession == session) {
            this.mGlobalPrioritySession = null;
            if (session.isActive() && user != null) {
                user.pushAddressedPlayerChangedLocked();
            }
        } else if (user != null) {
            user.mPriorityStack.removeSession(session);
        }
        session.close();
        android.util.Log.d(TAG, "destroySessionLocked: record=" + session);
        reportMediaInteractionEvent(session, false);
        this.mHandler.postSessionsChanged(session);
    }

    void onSessionUserEngagementStateChange(com.android.server.media.MediaSessionRecordImpl mediaSessionRecord, boolean isUserEngaged) {
        if (isUserEngaged) {
            addUserEngagedSession(mediaSessionRecord);
            startFgsIfSessionIsLinkedToNotification(mediaSessionRecord);
        } else {
            removeUserEngagedSession(mediaSessionRecord);
            stopFgsIfNoSessionIsLinkedToNotification(mediaSessionRecord);
        }
    }

    private void addUserEngagedSession(com.android.server.media.MediaSessionRecordImpl mediaSessionRecord) {
        if (!com.android.media.flags.Flags.enableNotifyingActivityManagerWithMediaSessionStatusChange()) {
            return;
        }
        synchronized (this.mLock) {
            int uid = mediaSessionRecord.getUid();
            this.mUserEngagedSessionsForFgs.putIfAbsent(java.lang.Integer.valueOf(uid), new java.util.HashSet());
            this.mUserEngagedSessionsForFgs.get(java.lang.Integer.valueOf(uid)).add(mediaSessionRecord);
        }
    }

    private void removeUserEngagedSession(com.android.server.media.MediaSessionRecordImpl mediaSessionRecord) {
        if (!com.android.media.flags.Flags.enableNotifyingActivityManagerWithMediaSessionStatusChange()) {
            return;
        }
        synchronized (this.mLock) {
            int uid = mediaSessionRecord.getUid();
            java.util.Set<com.android.server.media.MediaSessionRecordImpl> mUidUserEngagedSessionsForFgs = this.mUserEngagedSessionsForFgs.get(java.lang.Integer.valueOf(uid));
            if (mUidUserEngagedSessionsForFgs == null) {
                return;
            }
            mUidUserEngagedSessionsForFgs.remove(mediaSessionRecord);
            if (mUidUserEngagedSessionsForFgs.isEmpty()) {
                this.mUserEngagedSessionsForFgs.remove(java.lang.Integer.valueOf(uid));
            }
        }
    }

    private void startFgsIfSessionIsLinkedToNotification(com.android.server.media.MediaSessionRecordImpl mediaSessionRecord) {
        android.util.Log.d(TAG, "startFgsIfSessionIsLinkedToNotification: record=" + mediaSessionRecord);
        if (!com.android.media.flags.Flags.enableNotifyingActivityManagerWithMediaSessionStatusChange()) {
            return;
        }
        synchronized (this.mLock) {
            int uid = mediaSessionRecord.getUid();
            for (android.app.Notification mediaNotification : this.mMediaNotifications.getOrDefault(java.lang.Integer.valueOf(uid), java.util.Set.of())) {
                if (mediaSessionRecord.isLinkedToNotification(mediaNotification)) {
                    startFgsDelegateLocked(mediaSessionRecord);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFgsDelegateLocked(com.android.server.media.MediaSessionRecordImpl mediaSessionRecord) {
        android.app.ForegroundServiceDelegationOptions foregroundServiceDelegationOptions = mediaSessionRecord.getForegroundServiceDelegationOptions();
        if (foregroundServiceDelegationOptions == null || !this.mFgsAllowedMediaSessionRecords.add(mediaSessionRecord)) {
            return;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.util.Log.i(TAG, android.text.TextUtils.formatSimple("startFgsDelegate: pkg=%s uid=%d", new java.lang.Object[]{foregroundServiceDelegationOptions.mClientPackageName, java.lang.Integer.valueOf(foregroundServiceDelegationOptions.mClientUid)}));
            this.mActivityManagerInternal.startForegroundServiceDelegate(foregroundServiceDelegationOptions, (android.content.ServiceConnection) null);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopFgsIfNoSessionIsLinkedToNotification(com.android.server.media.MediaSessionRecordImpl mediaSessionRecord) {
        android.util.Log.d(TAG, "stopFgsIfNoSessionIsLinkedToNotification: record=" + mediaSessionRecord);
        if (!com.android.media.flags.Flags.enableNotifyingActivityManagerWithMediaSessionStatusChange()) {
            return;
        }
        synchronized (this.mLock) {
            int uid = mediaSessionRecord.getUid();
            android.app.ForegroundServiceDelegationOptions foregroundServiceDelegationOptions = mediaSessionRecord.getForegroundServiceDelegationOptions();
            if (foregroundServiceDelegationOptions == null) {
                return;
            }
            for (com.android.server.media.MediaSessionRecordImpl record : this.mUserEngagedSessionsForFgs.getOrDefault(java.lang.Integer.valueOf(uid), java.util.Set.of())) {
                for (android.app.Notification mediaNotification : this.mMediaNotifications.getOrDefault(java.lang.Integer.valueOf(uid), java.util.Set.of())) {
                    if (record.isLinkedToNotification(mediaNotification)) {
                        return;
                    }
                }
            }
            stopFgsDelegateLocked(mediaSessionRecord);
        }
    }

    private void stopFgsDelegateLocked(com.android.server.media.MediaSessionRecordImpl mediaSessionRecord) {
        android.app.ForegroundServiceDelegationOptions foregroundServiceDelegationOptions = mediaSessionRecord.getForegroundServiceDelegationOptions();
        if (foregroundServiceDelegationOptions == null || !this.mFgsAllowedMediaSessionRecords.remove(mediaSessionRecord)) {
            return;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.util.Log.i(TAG, android.text.TextUtils.formatSimple("stopFgsDelegate: pkg=%s uid=%d", new java.lang.Object[]{foregroundServiceDelegationOptions.mClientPackageName, java.lang.Integer.valueOf(foregroundServiceDelegationOptions.mClientUid)}));
            this.mActivityManagerInternal.stopForegroundServiceDelegate(foregroundServiceDelegationOptions);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void reportMediaInteractionEvent(com.android.server.media.MediaSessionRecordImpl record, boolean userEngaged) {
        if (!android.app.usage.Flags.userInteractionTypeApi()) {
            return;
        }
        java.lang.String packageName = record.getPackageName();
        int sessionUid = record.getUid();
        if (userEngaged) {
            if (!this.mUserEngagedSessionsForUsageLogging.contains(sessionUid)) {
                this.mUserEngagedSessionsForUsageLogging.put(sessionUid, new java.util.HashSet());
                reportUserInteractionEvent(USAGE_STATS_ACTION_START, record.getUserId(), packageName);
            }
            this.mUserEngagedSessionsForUsageLogging.get(sessionUid).add(record);
            return;
        }
        if (this.mUserEngagedSessionsForUsageLogging.contains(sessionUid)) {
            this.mUserEngagedSessionsForUsageLogging.get(sessionUid).remove(record);
            if (this.mUserEngagedSessionsForUsageLogging.get(sessionUid).isEmpty()) {
                reportUserInteractionEvent(USAGE_STATS_ACTION_STOP, record.getUserId(), packageName);
                this.mUserEngagedSessionsForUsageLogging.remove(sessionUid);
            }
        }
    }

    private void reportUserInteractionEvent(java.lang.String action, int userId, java.lang.String packageName) {
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        extras.putString("android.app.usage.extra.EVENT_CATEGORY", USAGE_STATS_CATEGORY);
        extras.putString("android.app.usage.extra.EVENT_ACTION", action);
        this.mUsageStatsManagerInternal.reportUserInteractionEvent(packageName, userId, extras);
    }

    void tempAllowlistTargetPkgIfPossible(int targetUid, java.lang.String targetPackage, int callingPid, int callingUid, java.lang.String callingPackage, java.lang.String reason) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.media.MediaServerUtils.enforcePackageName(this.mContext, callingPackage, callingUid);
            if (targetUid != callingUid) {
                boolean canAllowWhileInUse = this.mActivityManagerInternal.canAllowWhileInUsePermissionInFgs(callingPid, callingUid, callingPackage);
                boolean canStartFgs = canAllowWhileInUse || this.mActivityManagerInternal.canStartForegroundService(callingPid, callingUid, callingPackage);
                android.util.Log.i(TAG, "tempAllowlistTargetPkgIfPossible callingPackage:" + callingPackage + " targetPackage:" + targetPackage + " reason:" + reason + (canAllowWhileInUse ? " [WIU]" : "") + (canStartFgs ? " [FGS]" : ""));
                if (canAllowWhileInUse) {
                    this.mActivityManagerInternal.tempAllowWhileInUsePermissionInFgs(targetUid, com.android.server.media.MediaSessionDeviceConfig.getMediaSessionCallbackFgsWhileInUseTempAllowDurationMs());
                }
                if (canStartFgs) {
                    android.content.Context userContext = this.mContext.createContextAsUser(android.os.UserHandle.of(android.os.UserHandle.getUserId(targetUid)), 0);
                    android.os.PowerExemptionManager powerExemptionManager = (android.os.PowerExemptionManager) userContext.getSystemService(android.os.PowerExemptionManager.class);
                    powerExemptionManager.addToTemporaryAllowList(targetPackage, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_SESSION_CALLBACK, reason, com.android.server.media.MediaSessionDeviceConfig.getMediaSessionCallbackFgsAllowlistDurationMs());
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceMediaPermissions(java.lang.String packageName, int pid, int uid, int resolvedUserId) {
        if (hasStatusBarServicePermission(pid, uid) || hasMediaControlPermission(pid, uid)) {
            return;
        }
        if (packageName == null || !hasEnabledNotificationListener(packageName, android.os.UserHandle.getUserHandleForUid(uid), resolvedUserId)) {
            throw new java.lang.SecurityException("Missing permission to control media.");
        }
    }

    private boolean hasStatusBarServicePermission(int pid, int uid) {
        return this.mContext.checkPermission("android.permission.STATUS_BAR_SERVICE", pid, uid) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceStatusBarServicePermission(java.lang.String action, int pid, int uid) {
        if (!hasStatusBarServicePermission(pid, uid)) {
            throw new java.lang.SecurityException("Only System UI and Settings may " + action);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasMediaControlPermission(int pid, int uid) {
        if (uid == 1000 || this.mContext.checkPermission("android.permission.MEDIA_CONTENT_CONTROL", pid, uid) == 0) {
            return true;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "uid(" + uid + ") hasn't granted MEDIA_CONTENT_CONTROL");
            return false;
        }
        return false;
    }

    private boolean hasEnabledNotificationListener(java.lang.String packageName, android.os.UserHandle userHandle, int forUserId) {
        if (userHandle.getIdentifier() != forUserId) {
            return false;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "Checking whether the package " + packageName + " has an enabled notification listener.");
        }
        return this.mNotificationManager.hasEnabledNotificationListener(packageName, userHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.media.MediaSessionRecord createSessionInternal(int callerPid, int callerUid, int userId, java.lang.String callerPackageName, android.media.session.ISessionCallback cb, java.lang.String tag, android.os.Bundle sessionInfo) throws java.lang.Throwable {
        java.lang.Object obj;
        int policies;
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                try {
                    if (this.mCustomMediaSessionPolicyProvider == null) {
                        policies = 0;
                    } else {
                        int policies2 = this.mCustomMediaSessionPolicyProvider.getSessionPoliciesForApplication(callerUid, callerPackageName);
                        policies = policies2;
                    }
                    com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(userId);
                    if (user == null) {
                        android.util.Log.w(TAG, "Request from invalid user: " + userId + ", pkg=" + callerPackageName);
                        throw new java.lang.RuntimeException("Session request from invalid user.");
                    }
                    int sessionCount = user.mUidToSessionCount.get(callerUid, 0);
                    if (sessionCount >= 100 && !hasMediaControlPermission(callerPid, callerUid)) {
                        throw new java.lang.RuntimeException("Created too many sessions. count=" + sessionCount + ")");
                    }
                    try {
                        obj = obj2;
                        try {
                            try {
                                com.android.server.media.MediaSessionRecord session = new com.android.server.media.MediaSessionRecord(callerPid, callerUid, userId, callerPackageName, cb, tag, sessionInfo, this, this.mRecordThread.getLooper(), policies);
                                user.mUidToSessionCount.put(callerUid, sessionCount + 1);
                                user.mPriorityStack.addSession(session);
                                this.mHandler.postSessionsChanged(session);
                                if (DEBUG) {
                                    android.util.Log.d(TAG, "Created session for " + callerPackageName + " with tag " + tag);
                                }
                                return session;
                            } catch (android.os.RemoteException e) {
                                e = e;
                                throw new java.lang.RuntimeException("Media Session owner died prematurely.", e);
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (android.os.RemoteException e2) {
                        e = e2;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    obj = obj2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findIndexOfSessionsListenerLocked(android.media.session.IActiveSessionsListener listener) {
        for (int i = this.mSessionsListeners.size() - 1; i >= 0; i--) {
            if (this.mSessionsListeners.get(i).listener.asBinder() == listener.asBinder()) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findIndexOfSession2TokensListenerLocked(android.media.session.ISession2TokensListener listener) {
        for (int i = this.mSession2TokensListenerRecords.size() - 1; i >= 0; i--) {
            if (this.mSession2TokensListenerRecords.get(i).listener.asBinder() == listener.asBinder()) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushSession1Changed(int userId) {
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(userId);
            if (user == null) {
                android.util.Log.w(TAG, "pushSession1ChangedOnHandler failed. No user with id=" + userId);
                return;
            }
            java.util.List<com.android.server.media.MediaSessionRecord> records = getActiveSessionsLocked(userId);
            int size = records.size();
            java.util.ArrayList<android.media.session.MediaSession.Token> tokens = new java.util.ArrayList<>();
            for (int i = 0; i < size; i++) {
                tokens.add(records.get(i).getSessionToken());
            }
            pushRemoteVolumeUpdateLocked(userId);
            for (int i2 = this.mSessionsListeners.size() - 1; i2 >= 0; i2--) {
                com.android.server.media.MediaSessionService.SessionsListenerRecord record = this.mSessionsListeners.get(i2);
                if (record.userId == android.os.UserHandle.ALL.getIdentifier() || record.userId == userId) {
                    try {
                        record.listener.onActiveSessionsChanged(tokens);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.w(TAG, "Dead ActiveSessionsListener in pushSessionsChanged, removing", e);
                        this.mSessionsListeners.remove(i2);
                    }
                }
            }
        }
    }

    void pushSession2Changed(int userId) {
        synchronized (this.mLock) {
            java.util.List<android.media.Session2Token> allSession2Tokens = getSession2TokensLocked(android.os.UserHandle.ALL.getIdentifier());
            java.util.List<android.media.Session2Token> session2Tokens = getSession2TokensLocked(userId);
            for (int i = this.mSession2TokensListenerRecords.size() - 1; i >= 0; i--) {
                com.android.server.media.MediaSessionService.Session2TokensListenerRecord listenerRecord = this.mSession2TokensListenerRecords.get(i);
                try {
                    if (listenerRecord.userId == android.os.UserHandle.ALL.getIdentifier()) {
                        listenerRecord.listener.onSession2TokensChanged(allSession2Tokens);
                    } else if (listenerRecord.userId == userId) {
                        listenerRecord.listener.onSession2TokensChanged(session2Tokens);
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(TAG, "Failed to notify Session2Token change. Removing listener.", e);
                    this.mSession2TokensListenerRecords.remove(i);
                }
            }
        }
    }

    private void pushRemoteVolumeUpdateLocked(int userId) {
        com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(userId);
        if (user == null) {
            android.util.Log.w(TAG, "pushRemoteVolumeUpdateLocked failed. No user with id=" + userId);
            return;
        }
        synchronized (this.mLock) {
            int size = this.mRemoteVolumeControllers.beginBroadcast();
            com.android.server.media.MediaSessionRecordImpl record = user.mPriorityStack.getDefaultRemoteSession(userId);
            if (record instanceof com.android.server.media.MediaSession2Record) {
                return;
            }
            android.media.session.MediaSession.Token token = record == null ? null : ((com.android.server.media.MediaSessionRecord) record).getSessionToken();
            for (int i = size - 1; i >= 0; i--) {
                try {
                    android.media.IRemoteSessionCallback cb = this.mRemoteVolumeControllers.getBroadcastItem(i);
                    cb.onSessionChanged(token);
                } catch (java.lang.Exception e) {
                    android.util.Log.w(TAG, "Error sending default remote volume.", e);
                }
            }
            this.mRemoteVolumeControllers.finishBroadcast();
        }
    }

    public void onMediaButtonReceiverChanged(com.android.server.media.MediaSessionRecordImpl record) {
        synchronized (this.mLock) {
            com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(record.getUserId());
            com.android.server.media.MediaSessionRecordImpl mediaButtonSession = user.mPriorityStack.getMediaButtonSession();
            if (record == mediaButtonSession) {
                user.rememberMediaButtonReceiverLocked(mediaButtonSession);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getCallingPackageName(int uid) {
        java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(uid);
        if (packages != null && packages.length > 0) {
            return packages[0];
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchVolumeKeyLongPressLocked(android.view.KeyEvent keyEvent) {
        if (this.mCurrentFullUserRecord.mOnVolumeKeyLongPressListener == null) {
            return;
        }
        try {
            this.mCurrentFullUserRecord.mOnVolumeKeyLongPressListener.onVolumeKeyLongPress(keyEvent);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(TAG, "Failed to send " + keyEvent + " to volume key long-press listener");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.media.MediaSessionService.FullUserRecord getFullUserRecordLocked(int userId) {
        int fullUserId = this.mFullUserIds.get(userId, -1);
        if (fullUserId < 0) {
            return null;
        }
        return this.mUserRecords.get(fullUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.media.MediaSessionRecord getMediaSessionRecordLocked(android.media.session.MediaSession.Token sessionToken) {
        com.android.server.media.MediaSessionService.FullUserRecord user = getFullUserRecordLocked(android.os.UserHandle.getUserHandleForUid(sessionToken.getUid()).getIdentifier());
        if (user != null) {
            return user.mPriorityStack.getMediaSessionRecord(sessionToken);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0039 A[Catch: all -> 0x003b, DONT_GENERATE, TryCatch #1 {, blocks: (B:5:0x0004, B:7:0x0008, B:9:0x000e, B:14:0x0039, B:12:0x002e), top: B:21:0x0004, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void instantiateCustomDispatcher(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.Object r0 = r6.mLock
            monitor-enter(r0)
            r1 = 0
            r6.mCustomMediaKeyDispatcher = r1     // Catch: java.lang.Throwable -> L3b
            if (r7 == 0) goto L38
            boolean r2 = android.text.TextUtils.isEmpty(r7)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            if (r2 != 0) goto L38
            java.lang.Class r2 = java.lang.Class.forName(r7)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            r3 = 1
            java.lang.Class[] r3 = new java.lang.Class[r3]     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            java.lang.Class<android.content.Context> r4 = android.content.Context.class
            r5 = 0
            r3[r5] = r4     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            java.lang.reflect.Constructor r3 = r2.getDeclaredConstructor(r3)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            android.content.Context r4 = r6.mContext     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            java.lang.Object[] r4 = new java.lang.Object[]{r4}     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            java.lang.Object r4 = r3.newInstance(r4)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            com.android.server.media.MediaKeyDispatcher r4 = (com.android.server.media.MediaKeyDispatcher) r4     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            r6.mCustomMediaKeyDispatcher = r4     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3b
            goto L38
        L2d:
            r2 = move-exception
            r6.mCustomMediaKeyDispatcher = r1     // Catch: java.lang.Throwable -> L3b
            java.lang.String r1 = "MediaSessionService"
            java.lang.String r3 = "Encountered problem while using reflection"
            android.util.Log.w(r1, r3, r2)     // Catch: java.lang.Throwable -> L3b
            goto L39
        L38:
        L39:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L3b
            return
        L3b:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L3b
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.MediaSessionService.instantiateCustomDispatcher(java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0037 A[Catch: all -> 0x0039, DONT_GENERATE, TryCatch #1 {, blocks: (B:5:0x0004, B:7:0x0008, B:9:0x000e, B:14:0x0037, B:12:0x002e), top: B:21:0x0004, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void instantiateCustomProvider(java.lang.String r6) {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mLock
            monitor-enter(r0)
            r1 = 0
            r5.mCustomMediaSessionPolicyProvider = r1     // Catch: java.lang.Throwable -> L39
            if (r6 == 0) goto L36
            boolean r1 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            if (r1 != 0) goto L36
            java.lang.Class r1 = java.lang.Class.forName(r6)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            r2 = 1
            java.lang.Class[] r2 = new java.lang.Class[r2]     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            java.lang.Class<android.content.Context> r3 = android.content.Context.class
            r4 = 0
            r2[r4] = r3     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            java.lang.reflect.Constructor r2 = r1.getDeclaredConstructor(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            android.content.Context r3 = r5.mContext     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            java.lang.Object[] r3 = new java.lang.Object[]{r3}     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            java.lang.Object r3 = r2.newInstance(r3)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            com.android.server.media.MediaSessionPolicyProvider r3 = (com.android.server.media.MediaSessionPolicyProvider) r3     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            r5.mCustomMediaSessionPolicyProvider = r3     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L39
            goto L36
        L2d:
            r1 = move-exception
            java.lang.String r2 = "MediaSessionService"
            java.lang.String r3 = "Encountered problem while using reflection"
            android.util.Log.w(r2, r3, r1)     // Catch: java.lang.Throwable -> L39
            goto L37
        L36:
        L37:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L39
            return
        L39:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L39
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.MediaSessionService.instantiateCustomProvider(java.lang.String):void");
    }

    final class FullUserRecord implements com.android.server.media.MediaSessionStack.OnMediaButtonSessionChangedListener {
        private final android.content.ContentResolver mContentResolver;
        private final int mFullUserId;
        private com.android.server.media.MediaButtonReceiverHolder mLastMediaButtonReceiverHolder;
        private android.media.session.IOnMediaKeyListener mOnMediaKeyListener;
        private int mOnMediaKeyListenerUid;
        private android.media.session.IOnVolumeKeyLongPressListener mOnVolumeKeyLongPressListener;
        private int mOnVolumeKeyLongPressListenerUid;
        private final com.android.server.media.MediaSessionStack mPriorityStack;
        private final java.util.HashMap<android.os.IBinder, com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventDispatchedListenerRecord> mOnMediaKeyEventDispatchedListeners = new java.util.HashMap<>();
        private final java.util.HashMap<android.os.IBinder, com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventSessionChangedListenerRecord> mOnMediaKeyEventSessionChangedListeners = new java.util.HashMap<>();
        private final android.util.SparseIntArray mUidToSessionCount = new android.util.SparseIntArray();

        FullUserRecord(int fullUserId) {
            this.mFullUserId = fullUserId;
            this.mContentResolver = com.android.server.media.MediaSessionService.this.mContext.createContextAsUser(android.os.UserHandle.of(this.mFullUserId), 0).getContentResolver();
            this.mPriorityStack = new com.android.server.media.MediaSessionStack(com.android.server.media.MediaSessionService.this.mAudioPlayerStateMonitor, this);
            java.lang.String mediaButtonReceiverInfo = android.provider.Settings.Secure.getString(this.mContentResolver, com.android.server.media.MediaSessionService.MEDIA_BUTTON_RECEIVER);
            this.mLastMediaButtonReceiverHolder = com.android.server.media.MediaButtonReceiverHolder.unflattenFromString(com.android.server.media.MediaSessionService.this.mContext, com.android.server.media.MediaSessionService.this.mMediaSessionServiceExt.isMediaControlSupported() ? com.android.server.media.MediaSessionService.this.mMediaSessionServiceExt.checkAndResetReceiverInfo(mediaButtonReceiverInfo) : mediaButtonReceiverInfo);
        }

        public void destroySessionsForUserLocked(int userId) {
            java.util.List<com.android.server.media.MediaSessionRecord> sessions = this.mPriorityStack.getPriorityList(false, userId);
            for (com.android.server.media.MediaSessionRecord session : sessions) {
                com.android.server.media.MediaSessionService.this.destroySessionLocked(session);
            }
        }

        public void addOnMediaKeyEventDispatchedListenerLocked(android.media.session.IOnMediaKeyEventDispatchedListener listener, int uid) {
            android.os.IBinder cbBinder = listener.asBinder();
            com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventDispatchedListenerRecord cr = new com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventDispatchedListenerRecord(listener, uid);
            this.mOnMediaKeyEventDispatchedListeners.put(cbBinder, cr);
            try {
                cbBinder.linkToDeath(cr, 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to add listener", e);
                this.mOnMediaKeyEventDispatchedListeners.remove(cbBinder);
            }
        }

        public void removeOnMediaKeyEventDispatchedListenerLocked(android.media.session.IOnMediaKeyEventDispatchedListener listener) {
            android.os.IBinder cbBinder = listener.asBinder();
            com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventDispatchedListenerRecord cr = this.mOnMediaKeyEventDispatchedListeners.remove(cbBinder);
            cbBinder.unlinkToDeath(cr, 0);
        }

        public void addOnMediaKeyEventSessionChangedListenerLocked(android.media.session.IOnMediaKeyEventSessionChangedListener listener, int uid) {
            android.os.IBinder cbBinder = listener.asBinder();
            com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventSessionChangedListenerRecord cr = new com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventSessionChangedListenerRecord(listener, uid);
            this.mOnMediaKeyEventSessionChangedListeners.put(cbBinder, cr);
            try {
                cbBinder.linkToDeath(cr, 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to add listener", e);
                this.mOnMediaKeyEventSessionChangedListeners.remove(cbBinder);
            }
        }

        public void removeOnMediaKeyEventSessionChangedListener(android.media.session.IOnMediaKeyEventSessionChangedListener listener) {
            android.os.IBinder cbBinder = listener.asBinder();
            com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventSessionChangedListenerRecord cr = this.mOnMediaKeyEventSessionChangedListeners.remove(cbBinder);
            cbBinder.unlinkToDeath(cr, 0);
        }

        public void dumpLocked(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix + "Record for full_user=" + this.mFullUserId);
            int size = com.android.server.media.MediaSessionService.this.mFullUserIds.size();
            for (int i = 0; i < size; i++) {
                if (com.android.server.media.MediaSessionService.this.mFullUserIds.keyAt(i) != com.android.server.media.MediaSessionService.this.mFullUserIds.valueAt(i) && com.android.server.media.MediaSessionService.this.mFullUserIds.valueAt(i) == this.mFullUserId) {
                    pw.print(", profile_user=" + com.android.server.media.MediaSessionService.this.mFullUserIds.keyAt(i));
                }
            }
            pw.println();
            java.lang.String indent = prefix + "  ";
            pw.println(indent + "Volume key long-press listener: " + this.mOnVolumeKeyLongPressListener);
            pw.println(indent + "Volume key long-press listener package: " + com.android.server.media.MediaSessionService.this.getCallingPackageName(this.mOnVolumeKeyLongPressListenerUid));
            pw.println(indent + "Media key listener: " + this.mOnMediaKeyListener);
            pw.println(indent + "Media key listener package: " + com.android.server.media.MediaSessionService.this.getCallingPackageName(this.mOnMediaKeyListenerUid));
            pw.println(indent + "OnMediaKeyEventDispatchedListener: added " + this.mOnMediaKeyEventDispatchedListeners.size() + " listener(s)");
            for (com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventDispatchedListenerRecord cr : this.mOnMediaKeyEventDispatchedListeners.values()) {
                pw.println(indent + "  from " + com.android.server.media.MediaSessionService.this.getCallingPackageName(cr.uid));
            }
            pw.println(indent + "OnMediaKeyEventSessionChangedListener: added " + this.mOnMediaKeyEventSessionChangedListeners.size() + " listener(s)");
            for (com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventSessionChangedListenerRecord cr2 : this.mOnMediaKeyEventSessionChangedListeners.values()) {
                pw.println(indent + "  from " + com.android.server.media.MediaSessionService.this.getCallingPackageName(cr2.uid));
            }
            pw.println(indent + "Last MediaButtonReceiver: " + this.mLastMediaButtonReceiverHolder);
            this.mPriorityStack.dump(pw, indent);
        }

        @Override // com.android.server.media.MediaSessionStack.OnMediaButtonSessionChangedListener
        public void onMediaButtonSessionChanged(com.android.server.media.MediaSessionRecordImpl oldMediaButtonSession, com.android.server.media.MediaSessionRecordImpl newMediaButtonSession) {
            android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Media button session is changed to " + newMediaButtonSession);
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                if (oldMediaButtonSession != null) {
                    try {
                        com.android.server.media.MediaSessionService.this.mHandler.postSessionsChanged(oldMediaButtonSession);
                    } catch (java.lang.Throwable th) {
                        throw th;
                    }
                }
                if (newMediaButtonSession != null) {
                    rememberMediaButtonReceiverLocked(newMediaButtonSession);
                    com.android.server.media.MediaSessionService.this.mHandler.postSessionsChanged(newMediaButtonSession);
                }
                pushAddressedPlayerChangedLocked();
            }
        }

        public void rememberMediaButtonReceiverLocked(com.android.server.media.MediaSessionRecordImpl record) {
            if (record instanceof com.android.server.media.MediaSession2Record) {
                return;
            }
            com.android.server.media.MediaSessionRecord sessionRecord = (com.android.server.media.MediaSessionRecord) record;
            this.mLastMediaButtonReceiverHolder = sessionRecord.getMediaButtonReceiver();
            if (com.android.server.media.MediaSessionService.this.mMediaSessionServiceExt.isMediaControlSupported()) {
                com.android.server.media.MediaSessionService.this.mMediaSSWrapper.updateMediaButtonReceiverInfo(this.mContentResolver, this.mLastMediaButtonReceiverHolder, this.mFullUserId);
                com.android.server.media.MediaSessionService.this.mMediaSessionServiceExt.setLastMediaButtonReceiver(this.mLastMediaButtonReceiverHolder, this.mFullUserId);
            } else {
                java.lang.String mediaButtonReceiverInfo = this.mLastMediaButtonReceiverHolder == null ? "" : this.mLastMediaButtonReceiverHolder.flattenToString();
                android.provider.Settings.Secure.putString(this.mContentResolver, com.android.server.media.MediaSessionService.MEDIA_BUTTON_RECEIVER, mediaButtonReceiverInfo);
            }
        }

        private void pushAddressedPlayerChangedLocked(android.media.session.IOnMediaKeyEventSessionChangedListener callback) {
            try {
                com.android.server.media.MediaSessionRecordImpl mediaButtonSession = getMediaButtonSessionLocked();
                if (mediaButtonSession != null) {
                    if (mediaButtonSession instanceof com.android.server.media.MediaSessionRecord) {
                        com.android.server.media.MediaSessionRecord session1 = (com.android.server.media.MediaSessionRecord) mediaButtonSession;
                        callback.onMediaKeyEventSessionChanged(session1.getPackageName(), session1.getSessionToken());
                    }
                } else if (com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mLastMediaButtonReceiverHolder != null) {
                    java.lang.String packageName = com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mLastMediaButtonReceiverHolder.getPackageName();
                    callback.onMediaKeyEventSessionChanged(packageName, (android.media.session.MediaSession.Token) null);
                } else {
                    callback.onMediaKeyEventSessionChanged("", (android.media.session.MediaSession.Token) null);
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to pushAddressedPlayerChangedLocked", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pushAddressedPlayerChangedLocked() {
            for (com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventSessionChangedListenerRecord cr : this.mOnMediaKeyEventSessionChangedListeners.values()) {
                pushAddressedPlayerChangedLocked(cr.callback);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.media.MediaSessionRecordImpl getMediaButtonSessionLocked() {
            return com.android.server.media.MediaSessionService.this.isGlobalPriorityActiveLocked() ? com.android.server.media.MediaSessionService.this.mGlobalPrioritySession : this.mPriorityStack.getMediaButtonSession();
        }

        final class OnMediaKeyEventDispatchedListenerRecord implements android.os.IBinder.DeathRecipient {
            public final android.media.session.IOnMediaKeyEventDispatchedListener callback;
            public final int uid;

            OnMediaKeyEventDispatchedListenerRecord(android.media.session.IOnMediaKeyEventDispatchedListener callback, int uid) {
                this.callback = callback;
                this.uid = uid;
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord.this.mOnMediaKeyEventDispatchedListeners.remove(this.callback.asBinder());
                }
            }
        }

        final class OnMediaKeyEventSessionChangedListenerRecord implements android.os.IBinder.DeathRecipient {
            public final android.media.session.IOnMediaKeyEventSessionChangedListener callback;
            public final int uid;

            OnMediaKeyEventSessionChangedListenerRecord(android.media.session.IOnMediaKeyEventSessionChangedListener callback, int uid) {
                this.callback = callback;
                this.uid = uid;
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord.this.mOnMediaKeyEventSessionChangedListeners.remove(this.callback.asBinder());
                }
            }
        }
    }

    final class SessionsListenerRecord implements android.os.IBinder.DeathRecipient {
        public final android.content.ComponentName componentName;
        public final android.media.session.IActiveSessionsListener listener;
        public final int pid;
        public final int uid;
        public final int userId;

        SessionsListenerRecord(android.media.session.IActiveSessionsListener listener, android.content.ComponentName componentName, int userId, int pid, int uid) {
            this.listener = listener;
            this.componentName = componentName;
            this.userId = userId;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                com.android.server.media.MediaSessionService.this.mSessionsListeners.remove(this);
            }
        }
    }

    final class Session2TokensListenerRecord implements android.os.IBinder.DeathRecipient {
        public final android.media.session.ISession2TokensListener listener;
        public final int userId;

        Session2TokensListenerRecord(android.media.session.ISession2TokensListener listener, int userId) {
            this.listener = listener;
            this.userId = userId;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                com.android.server.media.MediaSessionService.this.mSession2TokensListenerRecords.remove(this);
            }
        }
    }

    class SessionManagerImpl extends android.media.session.ISessionManager.Stub {
        private static final java.lang.String EXTRA_WAKELOCK_ACQUIRED = "android.media.AudioService.WAKELOCK_ACQUIRED";
        private static final int WAKELOCK_RELEASE_ON_FINISHED = 1980;
        private com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventWakeLockReceiver mKeyEventReceiver;
        private com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler mMediaKeyEventHandler = new com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler(0);
        private com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler mVolumeKeyEventHandler = new com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler(1);

        SessionManagerImpl() {
            this.mKeyEventReceiver = new com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventWakeLockReceiver(com.android.server.media.MediaSessionService.this.mHandler);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            java.lang.String str;
            java.lang.String[] packageNames = com.android.server.media.MediaSessionService.this.mContext.getPackageManager().getPackagesForUid(android.os.Binder.getCallingUid());
            if (packageNames != null && packageNames.length > 0) {
                str = packageNames[0];
            } else {
                str = "com.android.shell";
            }
            java.lang.String packageName = str;
            new com.android.server.media.MediaShellCommand(packageName).exec(this, in, out, err, args, callback, resultReceiver);
        }

        public android.media.session.ISession createSession(java.lang.String packageName, android.media.session.ISessionCallback cb, java.lang.String tag, android.os.Bundle sessionInfo, int userId) throws java.lang.Exception {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    com.android.server.media.MediaServerUtils.enforcePackageName(com.android.server.media.MediaSessionService.this.mContext, packageName, uid);
                    try {
                        int resolvedUserId = handleIncomingUser(pid, uid, userId, packageName);
                        if (cb == null) {
                            throw new java.lang.IllegalArgumentException("Controller callback cannot be null");
                        }
                        com.android.server.media.MediaSessionRecord session = com.android.server.media.MediaSessionService.this.createSessionInternal(pid, uid, resolvedUserId, packageName, cb, tag, sessionInfo);
                        if (session == null) {
                            throw new java.lang.IllegalStateException("Failed to create a new session record");
                        }
                        android.media.session.ISession sessionBinder = session.getSessionBinder();
                        if (sessionBinder == null) {
                            throw new java.lang.IllegalStateException("Invalid session record");
                        }
                        android.os.Binder.restoreCallingIdentity(token);
                        return sessionBinder;
                    } catch (java.lang.Exception e) {
                        e = e;
                        android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Exception in creating a new session", e);
                        throw e;
                    }
                } catch (java.lang.Throwable th) {
                    e = th;
                    android.os.Binder.restoreCallingIdentity(token);
                    throw e;
                }
            } catch (java.lang.Exception e2) {
                e = e2;
            } catch (java.lang.Throwable th2) {
                e = th2;
                android.os.Binder.restoreCallingIdentity(token);
                throw e;
            }
        }

        public java.util.List<android.media.session.MediaSession.Token> getSessions(android.content.ComponentName componentName, int userId) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                int resolvedUserId = verifySessionsRequest(componentName, userId, pid, uid);
                java.util.ArrayList<android.media.session.MediaSession.Token> tokens = new java.util.ArrayList<>();
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    java.util.List<com.android.server.media.MediaSessionRecord> records = com.android.server.media.MediaSessionService.this.getActiveSessionsLocked(resolvedUserId);
                    for (com.android.server.media.MediaSessionRecord record : records) {
                        tokens.add(record.getSessionToken());
                    }
                }
                return tokens;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.media.session.MediaSession.Token getMediaKeyEventSession(java.lang.String packageName) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            android.os.UserHandle userHandle = android.os.UserHandle.getUserHandleForUid(uid);
            int userId = userHandle.getIdentifier();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaServerUtils.enforcePackageName(com.android.server.media.MediaSessionService.this.mContext, packageName, uid);
                com.android.server.media.MediaSessionService.this.enforceMediaPermissions(packageName, pid, uid, userId);
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user == null) {
                        android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "No matching user record to get the media key event session, userId=" + userId);
                        return null;
                    }
                    com.android.server.media.MediaSessionRecordImpl record = user.getMediaButtonSessionLocked();
                    if (record instanceof com.android.server.media.MediaSessionRecord) {
                        return ((com.android.server.media.MediaSessionRecord) record).getSessionToken();
                    }
                    return null;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public java.lang.String getMediaKeyEventSessionPackageName(java.lang.String packageName) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            android.os.UserHandle userHandle = android.os.UserHandle.getUserHandleForUid(uid);
            int userId = userHandle.getIdentifier();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaServerUtils.enforcePackageName(com.android.server.media.MediaSessionService.this.mContext, packageName, uid);
                com.android.server.media.MediaSessionService.this.enforceMediaPermissions(packageName, pid, uid, userId);
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user == null) {
                        android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "No matching user record to get the media key event session package , userId=" + userId);
                        return "";
                    }
                    com.android.server.media.MediaSessionRecordImpl record = user.getMediaButtonSessionLocked();
                    if (record instanceof com.android.server.media.MediaSessionRecord) {
                        return record.getPackageName();
                    }
                    if (user.mLastMediaButtonReceiverHolder == null) {
                        return "";
                    }
                    return user.mLastMediaButtonReceiverHolder.getPackageName();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void addSessionsListener(android.media.session.IActiveSessionsListener listener, android.content.ComponentName componentName, int userId) throws android.os.RemoteException {
            if (listener == null) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "addSessionsListener: listener is null, ignoring");
                return;
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                int resolvedUserId = verifySessionsRequest(componentName, userId, pid, uid);
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    int index = com.android.server.media.MediaSessionService.this.findIndexOfSessionsListenerLocked(listener);
                    if (index != -1) {
                        android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "ActiveSessionsListener is already added, ignoring");
                        return;
                    }
                    com.android.server.media.MediaSessionService.SessionsListenerRecord record = com.android.server.media.MediaSessionService.this.new SessionsListenerRecord(listener, componentName, resolvedUserId, pid, uid);
                    try {
                        listener.asBinder().linkToDeath(record, 0);
                        com.android.server.media.MediaSessionService.this.mSessionsListeners.add(record);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(com.android.server.media.MediaSessionService.TAG, "ActiveSessionsListener is dead, ignoring it", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void removeSessionsListener(android.media.session.IActiveSessionsListener listener) throws android.os.RemoteException {
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                int index = com.android.server.media.MediaSessionService.this.findIndexOfSessionsListenerLocked(listener);
                if (index != -1) {
                    com.android.server.media.MediaSessionService.SessionsListenerRecord record = (com.android.server.media.MediaSessionService.SessionsListenerRecord) com.android.server.media.MediaSessionService.this.mSessionsListeners.remove(index);
                    try {
                        record.listener.asBinder().unlinkToDeath(record, 0);
                    } catch (java.lang.Exception e) {
                    }
                }
            }
        }

        public void addSession2TokensListener(android.media.session.ISession2TokensListener listener, int userId) {
            if (listener == null) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "addSession2TokensListener: listener is null, ignoring");
                return;
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                int resolvedUserId = handleIncomingUser(pid, uid, userId, null);
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    int index = com.android.server.media.MediaSessionService.this.findIndexOfSession2TokensListenerLocked(listener);
                    if (index >= 0) {
                        android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "addSession2TokensListener: listener is already added, ignoring");
                    } else {
                        com.android.server.media.MediaSessionService.this.mSession2TokensListenerRecords.add(com.android.server.media.MediaSessionService.this.new Session2TokensListenerRecord(listener, resolvedUserId));
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void removeSession2TokensListener(android.media.session.ISession2TokensListener listener) {
            android.os.Binder.getCallingPid();
            android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    int index = com.android.server.media.MediaSessionService.this.findIndexOfSession2TokensListenerLocked(listener);
                    if (index >= 0) {
                        com.android.server.media.MediaSessionService.Session2TokensListenerRecord listenerRecord = (com.android.server.media.MediaSessionService.Session2TokensListenerRecord) com.android.server.media.MediaSessionService.this.mSession2TokensListenerRecords.remove(index);
                        try {
                            listenerRecord.listener.asBinder().unlinkToDeath(listenerRecord, 0);
                        } catch (java.lang.Exception e) {
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void dispatchMediaKeyEvent(java.lang.String packageName, boolean asSystemService, android.view.KeyEvent keyEvent, boolean needWakeLock) throws java.lang.Throwable {
            if (keyEvent == null || !android.view.KeyEvent.isMediaSessionKey(keyEvent.getKeyCode())) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Attempted to dispatch null or non-media key event.");
                return;
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.media.MediaSessionService.DEBUG) {
                    try {
                        try {
                            android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "dispatchMediaKeyEvent, pkg=" + packageName + " pid=" + pid + ", uid=" + uid + ", asSystem=" + asSystemService + ", event=" + keyEvent);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(token);
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
                if (!isUserSetupComplete()) {
                    android.util.Log.i(com.android.server.media.MediaSessionService.TAG, "Not dispatching media key event because user setup is in progress.");
                    android.os.Binder.restoreCallingIdentity(token);
                    return;
                }
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    boolean isGlobalPriorityActive = com.android.server.media.MediaSessionService.this.isGlobalPriorityActiveLocked();
                    if (isGlobalPriorityActive && uid != 1000) {
                        android.util.Log.i(com.android.server.media.MediaSessionService.TAG, "Only the system can dispatch media key event to the global priority session.");
                        android.os.Binder.restoreCallingIdentity(token);
                        return;
                    }
                    if (!isGlobalPriorityActive && com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mOnMediaKeyListener != null) {
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Send " + keyEvent + " to the media key listener");
                        try {
                            com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mOnMediaKeyListener.onMediaKey(keyEvent, new com.android.server.media.MediaSessionService.SessionManagerImpl.MediaKeyListenerResultReceiver(packageName, pid, uid, asSystemService, keyEvent, needWakeLock));
                            android.os.Binder.restoreCallingIdentity(token);
                            return;
                        } catch (android.os.RemoteException e) {
                            android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to send " + keyEvent + " to the media key listener");
                        }
                    }
                    if (isGlobalPriorityActive) {
                        dispatchMediaKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock);
                    } else {
                        this.mMediaKeyEventHandler.handleMediaKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock);
                    }
                    android.os.Binder.restoreCallingIdentity(token);
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }

        public boolean dispatchMediaKeyEventToSessionAsSystemService(java.lang.String packageName, android.view.KeyEvent keyEvent, android.media.session.MediaSession.Token sessionToken) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionRecord record = com.android.server.media.MediaSessionService.this.getMediaSessionRecordLocked(sessionToken);
                    android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "dispatchMediaKeyEventToSessionAsSystemService, pkg=" + packageName + ", pid=" + pid + ", uid=" + uid + ", sessionToken=" + sessionToken + ", event=" + keyEvent + ", session=" + record);
                    if (record == null) {
                        android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to find session to dispatch key event.");
                        android.os.Binder.restoreCallingIdentity(token);
                        return false;
                    }
                    return record.sendMediaButton(packageName, pid, uid, true, keyEvent, 0, null);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void addOnMediaKeyEventDispatchedListener(android.media.session.IOnMediaKeyEventDispatchedListener listener) {
            if (listener == null) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "addOnMediaKeyEventDispatchedListener: listener is null, ignoring");
                return;
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (!com.android.server.media.MediaSessionService.this.hasMediaControlPermission(pid, uid)) {
                    throw new java.lang.SecurityException("MEDIA_CONTENT_CONTROL permission is required to  add MediaKeyEventDispatchedListener");
                }
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user != null && user.mFullUserId == userId) {
                        user.addOnMediaKeyEventDispatchedListenerLocked(listener, uid);
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "The MediaKeyEventDispatchedListener (" + listener.asBinder() + ") is added by " + com.android.server.media.MediaSessionService.this.getCallingPackageName(uid));
                        return;
                    }
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Only the full user can add the listener, userId=" + userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void removeOnMediaKeyEventDispatchedListener(android.media.session.IOnMediaKeyEventDispatchedListener listener) {
            if (listener == null) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "removeOnMediaKeyEventDispatchedListener: listener is null, ignoring");
                return;
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (!com.android.server.media.MediaSessionService.this.hasMediaControlPermission(pid, uid)) {
                    throw new java.lang.SecurityException("MEDIA_CONTENT_CONTROL permission is required to  remove MediaKeyEventDispatchedListener");
                }
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user != null && user.mFullUserId == userId) {
                        user.removeOnMediaKeyEventDispatchedListenerLocked(listener);
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "The MediaKeyEventDispatchedListener (" + listener.asBinder() + ") is removed by " + com.android.server.media.MediaSessionService.this.getCallingPackageName(uid));
                        return;
                    }
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Only the full user can remove the listener, userId=" + userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void addOnMediaKeyEventSessionChangedListener(android.media.session.IOnMediaKeyEventSessionChangedListener listener, java.lang.String packageName) {
            if (listener == null) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "addOnMediaKeyEventSessionChangedListener: listener is null, ignoring");
                return;
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            android.os.UserHandle userHandle = android.os.UserHandle.getUserHandleForUid(uid);
            int userId = userHandle.getIdentifier();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaServerUtils.enforcePackageName(com.android.server.media.MediaSessionService.this.mContext, packageName, uid);
                com.android.server.media.MediaSessionService.this.enforceMediaPermissions(packageName, pid, uid, userId);
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user != null && user.mFullUserId == userId) {
                        user.addOnMediaKeyEventSessionChangedListenerLocked(listener, uid);
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "The MediaKeyEventSessionChangedListener (" + listener.asBinder() + ") is added by " + packageName);
                        return;
                    }
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Only the full user can add the listener, userId=" + userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void removeOnMediaKeyEventSessionChangedListener(android.media.session.IOnMediaKeyEventSessionChangedListener listener) {
            if (listener == null) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "removeOnMediaKeyEventSessionChangedListener: listener is null, ignoring");
                return;
            }
            android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user != null && user.mFullUserId == userId) {
                        user.removeOnMediaKeyEventSessionChangedListener(listener);
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "The MediaKeyEventSessionChangedListener (" + listener.asBinder() + ") is removed by " + com.android.server.media.MediaSessionService.this.getCallingPackageName(uid));
                        return;
                    }
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Only the full user can remove the listener, userId=" + userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setOnVolumeKeyLongPressListener(android.media.session.IOnVolumeKeyLongPressListener listener) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.media.MediaSessionService.this.mContext.checkPermission("android.permission.SET_VOLUME_KEY_LONG_PRESS_LISTENER", pid, uid) != 0) {
                    throw new java.lang.SecurityException("Must hold the SET_VOLUME_KEY_LONG_PRESS_LISTENER permission.");
                }
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
                    final com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user != null && user.mFullUserId == userId) {
                        if (user.mOnVolumeKeyLongPressListener != null && user.mOnVolumeKeyLongPressListenerUid != uid) {
                            android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "The volume key long-press listener cannot be reset by another app , mOnVolumeKeyLongPressListener=" + user.mOnVolumeKeyLongPressListenerUid + ", uid=" + uid);
                            return;
                        }
                        user.mOnVolumeKeyLongPressListener = listener;
                        user.mOnVolumeKeyLongPressListenerUid = uid;
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "The volume key long-press listener " + listener + " is set by " + com.android.server.media.MediaSessionService.this.getCallingPackageName(uid));
                        if (user.mOnVolumeKeyLongPressListener != null) {
                            try {
                                user.mOnVolumeKeyLongPressListener.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.media.MediaSessionService.SessionManagerImpl.1
                                    @Override // android.os.IBinder.DeathRecipient
                                    public void binderDied() {
                                        synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                                            user.mOnVolumeKeyLongPressListener = null;
                                        }
                                    }
                                }, 0);
                            } catch (android.os.RemoteException e) {
                                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to set death recipient " + user.mOnVolumeKeyLongPressListener);
                                user.mOnVolumeKeyLongPressListener = null;
                            }
                        }
                        return;
                    }
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Only the full user can set the volume key long-press listener, userId=" + userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setOnMediaKeyListener(android.media.session.IOnMediaKeyListener listener) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.media.MediaSessionService.this.mContext.checkPermission("android.permission.SET_MEDIA_KEY_LISTENER", pid, uid) != 0) {
                    throw new java.lang.SecurityException("Must hold the SET_MEDIA_KEY_LISTENER permission.");
                }
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    int userId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
                    final com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(userId);
                    if (user != null && user.mFullUserId == userId) {
                        if (user.mOnMediaKeyListener != null && user.mOnMediaKeyListenerUid != uid) {
                            android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "The media key listener cannot be reset by another app. , mOnMediaKeyListenerUid=" + user.mOnMediaKeyListenerUid + ", uid=" + uid);
                            return;
                        }
                        user.mOnMediaKeyListener = listener;
                        user.mOnMediaKeyListenerUid = uid;
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "The media key listener " + user.mOnMediaKeyListener + " is set by " + com.android.server.media.MediaSessionService.this.getCallingPackageName(uid));
                        if (user.mOnMediaKeyListener != null) {
                            try {
                                user.mOnMediaKeyListener.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.media.MediaSessionService.SessionManagerImpl.2
                                    @Override // android.os.IBinder.DeathRecipient
                                    public void binderDied() {
                                        synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                                            user.mOnMediaKeyListener = null;
                                        }
                                    }
                                }, 0);
                            } catch (android.os.RemoteException e) {
                                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to set death recipient " + user.mOnMediaKeyListener);
                                user.mOnMediaKeyListener = null;
                            }
                        }
                        return;
                    }
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Only the full user can set the media key listener, userId=" + userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void dispatchVolumeKeyEvent(java.lang.String packageName, java.lang.String opPackageName, boolean asSystemService, android.view.KeyEvent keyEvent, int stream, boolean musicOnly) {
            if (keyEvent == null || (keyEvent.getKeyCode() != 24 && keyEvent.getKeyCode() != 25 && keyEvent.getKeyCode() != 164)) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Attempted to dispatch null or non-volume key event.");
                return;
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "dispatchVolumeKeyEvent, pkg=" + packageName + ", opPkg=" + opPackageName + ", pid=" + pid + ", uid=" + uid + ", asSystem=" + asSystemService + ", event=" + keyEvent + ", stream=" + stream + ", musicOnly=" + musicOnly);
            try {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    if (com.android.server.media.MediaSessionService.this.isGlobalPriorityActiveLocked()) {
                        dispatchVolumeKeyEventLocked(packageName, opPackageName, pid, uid, asSystemService, keyEvent, stream, musicOnly);
                    } else {
                        this.mVolumeKeyEventHandler.handleVolumeKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, opPackageName, stream, musicOnly);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatchVolumeKeyEventLocked(java.lang.String packageName, java.lang.String opPackageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, int stream, boolean musicOnly) {
            boolean down = keyEvent.getAction() == 0;
            boolean up = keyEvent.getAction() == 1;
            int direction = 0;
            boolean isMute = false;
            switch (keyEvent.getKeyCode()) {
                case 24:
                    direction = 1;
                    break;
                case 25:
                    direction = -1;
                    break;
                case 164:
                    isMute = true;
                    break;
            }
            if (down || up) {
                int flags = 4096;
                if (!musicOnly) {
                    if (up) {
                        flags = 4096 | 20;
                    } else {
                        flags = 4096 | 17;
                    }
                }
                if (direction != 0) {
                    if (up) {
                        direction = 0;
                    }
                    dispatchAdjustVolumeLocked(packageName, opPackageName, pid, uid, asSystemService, stream, direction, flags, musicOnly);
                } else if (isMute && down && keyEvent.getRepeatCount() == 0) {
                    dispatchAdjustVolumeLocked(packageName, opPackageName, pid, uid, asSystemService, stream, 101, flags, musicOnly);
                }
            }
        }

        public void dispatchVolumeKeyEventToSessionAsSystemService(java.lang.String packageName, java.lang.String opPackageName, android.view.KeyEvent keyEvent, android.media.session.MediaSession.Token sessionToken) {
            int direction;
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionRecord record = com.android.server.media.MediaSessionService.this.getMediaSessionRecordLocked(sessionToken);
                    android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "dispatchVolumeKeyEventToSessionAsSystemService, pkg=" + packageName + ", opPkg=" + opPackageName + ", pid=" + pid + ", uid=" + uid + ", sessionToken=" + sessionToken + ", event=" + keyEvent + ", session=" + record);
                    if (record == null) {
                        android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to find session to dispatch key event, token=" + sessionToken + ". Fallbacks to the default handling.");
                        dispatchVolumeKeyEventLocked(packageName, opPackageName, pid, uid, true, keyEvent, Integer.MIN_VALUE, false);
                        return;
                    }
                    if (com.android.media.flags.Flags.fallbackToDefaultHandlingWhenMediaSessionHasFixedVolumeHandling() && !record.canHandleVolumeKey()) {
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Session with packageName=" + record.getPackageName() + " doesn't support volume adjustment. Fallbacks to the default handling.");
                        dispatchVolumeKeyEventLocked(packageName, opPackageName, pid, uid, true, keyEvent, Integer.MIN_VALUE, false);
                        return;
                    }
                    switch (keyEvent.getAction()) {
                        case 0:
                            switch (keyEvent.getKeyCode()) {
                                case 24:
                                    direction = 1;
                                    break;
                                case 25:
                                    direction = -1;
                                    break;
                                case 164:
                                    direction = 101;
                                    break;
                                default:
                                    direction = 0;
                                    break;
                            }
                            record.adjustVolume(packageName, opPackageName, pid, uid, true, direction, 1, false);
                            break;
                        case 1:
                            record.adjustVolume(packageName, opPackageName, pid, uid, true, 0, 4116, false);
                            break;
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void dispatchAdjustVolume(java.lang.String packageName, java.lang.String opPackageName, int suggestedStream, int delta, int flags) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    dispatchAdjustVolumeLocked(packageName, opPackageName, pid, uid, false, suggestedStream, delta, flags, false);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void registerRemoteSessionCallback(android.media.IRemoteSessionCallback rvc) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                try {
                    com.android.server.media.MediaSessionService.this.enforceStatusBarServicePermission("listen for volume changes", pid, uid);
                    com.android.server.media.MediaSessionService.this.mRemoteVolumeControllers.register(rvc);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }

        public void unregisterRemoteSessionCallback(android.media.IRemoteSessionCallback rvc) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                try {
                    com.android.server.media.MediaSessionService.this.enforceStatusBarServicePermission("listen for volume changes", pid, uid);
                    com.android.server.media.MediaSessionService.this.mRemoteVolumeControllers.unregister(rvc);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }

        public boolean isGlobalPriorityActive() {
            boolean zIsGlobalPriorityActiveLocked;
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                zIsGlobalPriorityActiveLocked = com.android.server.media.MediaSessionService.this.isGlobalPriorityActiveLocked();
            }
            return zIsGlobalPriorityActiveLocked;
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.server.media.MediaServerUtils.checkDumpPermission(com.android.server.media.MediaSessionService.this.mContext, com.android.server.media.MediaSessionService.TAG, pw)) {
                pw.println("MEDIA SESSION SERVICE (dumpsys media_session)");
                pw.println();
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    pw.println(com.android.server.media.MediaSessionService.this.mSessionsListeners.size() + " sessions listeners.");
                    pw.println("Global priority session is " + com.android.server.media.MediaSessionService.this.mGlobalPrioritySession);
                    if (com.android.server.media.MediaSessionService.this.mGlobalPrioritySession != null) {
                        com.android.server.media.MediaSessionService.this.mGlobalPrioritySession.dump(pw, "  ");
                    }
                    pw.println("User Records:");
                    int count = com.android.server.media.MediaSessionService.this.mUserRecords.size();
                    for (int i = 0; i < count; i++) {
                        ((com.android.server.media.MediaSessionService.FullUserRecord) com.android.server.media.MediaSessionService.this.mUserRecords.valueAt(i)).dumpLocked(pw, "");
                    }
                    com.android.server.media.MediaSessionService.this.mAudioPlayerStateMonitor.dump(com.android.server.media.MediaSessionService.this.mContext, pw, "");
                }
                com.android.server.media.MediaSessionDeviceConfig.dump(pw, "");
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x002e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean isTrusted(java.lang.String r7, int r8, int r9) {
            /*
                r6 = this;
                int r0 = android.os.Binder.getCallingUid()
                android.os.UserHandle r1 = android.os.UserHandle.getUserHandleForUid(r0)
                int r1 = r1.getIdentifier()
                java.lang.Class<android.content.pm.PackageManagerInternal> r2 = android.content.pm.PackageManagerInternal.class
                java.lang.Object r2 = com.android.server.LocalServices.getService(r2)
                android.content.pm.PackageManagerInternal r2 = (android.content.pm.PackageManagerInternal) r2
                boolean r2 = r2.filterAppAccess(r7, r0, r1)
                r3 = 0
                if (r2 == 0) goto L1c
                return r3
            L1c:
                long r4 = android.os.Binder.clearCallingIdentity()
                com.android.server.media.MediaSessionService r2 = com.android.server.media.MediaSessionService.this     // Catch: java.lang.Throwable -> L33
                boolean r2 = com.android.server.media.MediaSessionService.m5361$$Nest$mhasMediaControlPermission(r2, r8, r9)     // Catch: java.lang.Throwable -> L33
                if (r2 != 0) goto L2e
                boolean r2 = r6.hasEnabledNotificationListener(r1, r7, r9)     // Catch: java.lang.Throwable -> L33
                if (r2 == 0) goto L2f
            L2e:
                r3 = 1
            L2f:
                android.os.Binder.restoreCallingIdentity(r4)
                return r3
            L33:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r4)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.MediaSessionService.SessionManagerImpl.isTrusted(java.lang.String, int, int):boolean");
        }

        public void setCustomMediaKeyDispatcher(java.lang.String name) {
            com.android.server.media.MediaSessionService.this.instantiateCustomDispatcher(name);
        }

        public void setCustomMediaSessionPolicyProvider(java.lang.String name) {
            com.android.server.media.MediaSessionService.this.instantiateCustomProvider(name);
        }

        public boolean hasCustomMediaKeyDispatcher(java.lang.String componentName) {
            if (com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher == null) {
                return false;
            }
            return android.text.TextUtils.equals(componentName, com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.getClass().getName());
        }

        public boolean hasCustomMediaSessionPolicyProvider(java.lang.String componentName) {
            if (com.android.server.media.MediaSessionService.this.mCustomMediaSessionPolicyProvider == null) {
                return false;
            }
            return android.text.TextUtils.equals(componentName, com.android.server.media.MediaSessionService.this.mCustomMediaSessionPolicyProvider.getClass().getName());
        }

        public int getSessionPolicies(android.media.session.MediaSession.Token token) {
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                com.android.server.media.MediaSessionRecord record = com.android.server.media.MediaSessionService.this.getMediaSessionRecordLocked(token);
                if (record != null) {
                    return record.getSessionPolicies();
                }
                return 0;
            }
        }

        public void setSessionPolicies(android.media.session.MediaSession.Token token, int policies) {
            long callingIdentityToken = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    com.android.server.media.MediaSessionRecord record = com.android.server.media.MediaSessionService.this.getMediaSessionRecordLocked(token);
                    com.android.server.media.MediaSessionService.FullUserRecord user = com.android.server.media.MediaSessionService.this.getFullUserRecordLocked(record.getUserId());
                    if (record != null && user != null) {
                        record.setSessionPolicies(policies);
                        user.mPriorityStack.updateMediaButtonSessionBySessionPolicyChange(record);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentityToken);
            }
        }

        private int verifySessionsRequest(android.content.ComponentName componentName, int userId, int pid, int uid) {
            java.lang.String packageName = null;
            if (componentName != null) {
                packageName = componentName.getPackageName();
                com.android.server.media.MediaServerUtils.enforcePackageName(com.android.server.media.MediaSessionService.this.mContext, packageName, uid);
            }
            int resolvedUserId = handleIncomingUser(pid, uid, userId, packageName);
            com.android.server.media.MediaSessionService.this.enforceMediaPermissions(packageName, pid, uid, resolvedUserId);
            return resolvedUserId;
        }

        private int handleIncomingUser(int pid, int uid, int userId, java.lang.String packageName) {
            int callingUserId = android.os.UserHandle.getUserHandleForUid(uid).getIdentifier();
            if (userId == callingUserId) {
                return userId;
            }
            boolean canInteractAcrossUsersFull = com.android.server.media.MediaSessionService.this.mContext.checkPermission("android.permission.INTERACT_ACROSS_USERS_FULL", pid, uid) == 0;
            if (canInteractAcrossUsersFull) {
                if (userId == android.os.UserHandle.CURRENT.getIdentifier()) {
                    return android.app.ActivityManager.getCurrentUser();
                }
                return userId;
            }
            throw new java.lang.SecurityException("Permission denied while calling from " + packageName + " with user id: " + userId + "; Need to run as either the calling user id (" + callingUserId + "), or with android.permission.INTERACT_ACROSS_USERS_FULL permission");
        }

        private boolean hasEnabledNotificationListener(int callingUserId, java.lang.String controllerPackageName, int controllerUid) {
            int controllerUserId = android.os.UserHandle.getUserHandleForUid(controllerUid).getIdentifier();
            if (callingUserId != controllerUserId) {
                return false;
            }
            try {
                int actualControllerUid = com.android.server.media.MediaSessionService.this.mContext.getPackageManager().getPackageUidAsUser(controllerPackageName, android.os.UserHandle.getUserId(controllerUid));
                if (controllerUid != actualControllerUid) {
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to check enabled notification listener. Package name and UID doesn't match");
                    return false;
                }
                if (com.android.server.media.MediaSessionService.this.mNotificationManager.hasEnabledNotificationListener(controllerPackageName, android.os.UserHandle.getUserHandleForUid(controllerUid))) {
                    return true;
                }
                if (com.android.server.media.MediaSessionService.DEBUG) {
                    android.util.Log.d(com.android.server.media.MediaSessionService.TAG, controllerPackageName + " (uid=" + controllerUid + ") doesn't have an enabled notification listener");
                }
                return false;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to check enabled notification listener. Package name doesn't exist");
                return false;
            }
        }

        private void dispatchAdjustVolumeLocked(final java.lang.String packageName, final java.lang.String opPackageName, final int pid, final int uid, final boolean asSystemService, final int suggestedStream, final int direction, final int flags, boolean musicOnly) {
            com.android.server.media.MediaSessionRecordImpl session;
            com.android.server.media.MediaSessionRecordImpl session2;
            if (com.android.server.media.MediaSessionService.this.isGlobalPriorityActiveLocked()) {
                session = com.android.server.media.MediaSessionService.this.mGlobalPrioritySession;
            } else {
                session = com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mPriorityStack.getDefaultVolumeSession();
            }
            boolean preferSuggestedStream = isValidLocalStreamType(suggestedStream) && android.media.AudioSystem.isStreamActive(suggestedStream, 0);
            if (session != null && session.getUid() != uid && com.android.server.media.MediaSessionService.this.mAudioPlayerStateMonitor.hasUidPlayedAudioLast(uid)) {
                if (com.android.media.flags.Flags.adjustVolumeForForegroundAppPlayingAudioWithoutMediaSession()) {
                    android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Ignoring session=" + session + " and adjusting suggestedStream=" + suggestedStream + " instead");
                    session2 = null;
                } else {
                    android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Session=" + session + " will not be not ignored and will receive the volume adjustment event");
                    session2 = session;
                }
            } else {
                session2 = session;
            }
            if (session2 == null || preferSuggestedStream) {
                com.android.server.media.MediaSessionRecordImpl session3 = session2;
                android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Adjusting suggestedStream=" + suggestedStream + " by " + direction + ". flags=" + flags + ", preferSuggestedStream=" + preferSuggestedStream + ", session=" + session3 + ", musicOnly " + musicOnly);
                if (musicOnly && !android.media.AudioSystem.isStreamActive(3, 0)) {
                    android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Nothing is playing on the music stream. Skipping volume event, flags=" + flags);
                    return;
                } else {
                    com.android.server.media.MediaSessionService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionService.SessionManagerImpl.3
                        @Override // java.lang.Runnable
                        public void run() {
                            java.lang.String callingOpPackageName;
                            int callingUid;
                            int callingPid;
                            if (asSystemService) {
                                callingOpPackageName = com.android.server.media.MediaSessionService.this.mContext.getOpPackageName();
                                callingUid = android.os.Process.myUid();
                                callingPid = android.os.Process.myPid();
                            } else {
                                callingOpPackageName = opPackageName;
                                callingUid = uid;
                                callingPid = pid;
                            }
                            try {
                                com.android.server.media.MediaSessionService.this.mAudioManager.adjustSuggestedStreamVolumeForUid(suggestedStream, direction, flags, callingOpPackageName, callingUid, callingPid, com.android.server.media.MediaSessionService.this.getContext().getApplicationInfo().targetSdkVersion);
                            } catch (java.lang.IllegalArgumentException | java.lang.SecurityException e) {
                                android.util.Log.e(com.android.server.media.MediaSessionService.TAG, "Cannot adjust volume: direction=" + direction + ", suggestedStream=" + suggestedStream + ", flags=" + flags + ", packageName=" + packageName + ", uid=" + uid + ", asSystemService=" + asSystemService, e);
                            }
                        }
                    });
                    return;
                }
            }
            if (!musicOnly || android.media.AudioSystem.isStreamActive(3, 0)) {
                android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Adjusting " + session2 + " by " + direction + ". flags=" + flags + ", suggestedStream=" + suggestedStream + ", preferSuggestedStream=" + preferSuggestedStream);
                session2.adjustVolume(packageName, opPackageName, pid, uid, asSystemService, direction, flags, true);
            } else {
                android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Nothing is playing on the music stream. Skipping volume event,session " + session2 + " flags=" + flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatchMediaKeyEventLocked(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, boolean needWakeLock) {
            com.android.server.media.MediaSessionRecord session;
            com.android.server.media.MediaButtonReceiverHolder mediaButtonReceiverHolder;
            android.app.PendingIntent pi;
            if (com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.getMediaButtonSessionLocked() instanceof com.android.server.media.MediaSession2Record) {
                return;
            }
            com.android.server.media.MediaSessionRecord session2 = null;
            com.android.server.media.MediaButtonReceiverHolder mediaButtonReceiverHolder2 = null;
            if (com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher != null) {
                android.media.session.MediaSession.Token token = com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.getMediaSession(keyEvent, uid, asSystemService);
                if (token != null) {
                    session2 = com.android.server.media.MediaSessionService.this.getMediaSessionRecordLocked(token);
                }
                if (session2 == null && (pi = com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.getMediaButtonReceiver(keyEvent, uid, asSystemService)) != null) {
                    mediaButtonReceiverHolder2 = com.android.server.media.MediaButtonReceiverHolder.create(com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mFullUserId, pi, "");
                }
            }
            if (session2 == null && mediaButtonReceiverHolder2 == null) {
                com.android.server.media.MediaSessionRecord session3 = (com.android.server.media.MediaSessionRecord) com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.getMediaButtonSessionLocked();
                if (session3 != null) {
                    session = session3;
                    mediaButtonReceiverHolder = mediaButtonReceiverHolder2;
                } else {
                    session = session3;
                    mediaButtonReceiverHolder = com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mLastMediaButtonReceiverHolder;
                }
            } else {
                session = session2;
                mediaButtonReceiverHolder = mediaButtonReceiverHolder2;
            }
            if (session != null) {
                android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "Sending " + keyEvent + " to " + session);
                if (needWakeLock) {
                    this.mKeyEventReceiver.acquireWakeLockLocked();
                }
                session.sendMediaButton(packageName, pid, uid, asSystemService, keyEvent, needWakeLock ? this.mKeyEventReceiver.mLastTimeoutId : -1, this.mKeyEventReceiver);
                try {
                    java.util.Iterator it = com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mOnMediaKeyEventDispatchedListeners.values().iterator();
                    while (it.hasNext()) {
                        ((com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventDispatchedListenerRecord) it.next()).callback.onMediaKeyEventDispatched(keyEvent, session.getPackageName(), session.getSessionToken());
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed to send callback", e);
                }
                return;
            }
            if (mediaButtonReceiverHolder != null) {
                if (needWakeLock) {
                    this.mKeyEventReceiver.acquireWakeLockLocked();
                }
                java.lang.String callingPackageName = asSystemService ? com.android.server.media.MediaSessionService.this.mContext.getPackageName() : packageName;
                boolean sent = mediaButtonReceiverHolder.send(com.android.server.media.MediaSessionService.this.mContext, keyEvent, callingPackageName, needWakeLock ? this.mKeyEventReceiver.mLastTimeoutId : -1, this.mKeyEventReceiver, com.android.server.media.MediaSessionService.this.mHandler, com.android.server.media.MediaSessionDeviceConfig.getMediaButtonReceiverFgsAllowlistDurationMs());
                if (sent) {
                    java.lang.String pkgName = mediaButtonReceiverHolder.getPackageName();
                    for (com.android.server.media.MediaSessionService.FullUserRecord.OnMediaKeyEventDispatchedListenerRecord cr : com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mOnMediaKeyEventDispatchedListeners.values()) {
                        try {
                            cr.callback.onMediaKeyEventDispatched(keyEvent, pkgName, (android.media.session.MediaSession.Token) null);
                        } catch (android.os.RemoteException e2) {
                            android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "Failed notify key event dispatch, uid=" + cr.uid, e2);
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startVoiceInput(boolean needWakeLock) {
            android.content.Intent voiceIntent;
            android.os.PowerManager pm = (android.os.PowerManager) com.android.server.media.MediaSessionService.this.mContext.getSystemService("power");
            boolean isLocked = com.android.server.media.MediaSessionService.this.mKeyguardManager != null && com.android.server.media.MediaSessionService.this.mKeyguardManager.isKeyguardLocked();
            if (isLocked || !pm.isScreenOn()) {
                voiceIntent = new android.content.Intent("android.speech.action.VOICE_SEARCH_HANDS_FREE");
                voiceIntent.putExtra("android.speech.extras.EXTRA_SECURE", isLocked && com.android.server.media.MediaSessionService.this.mKeyguardManager.isKeyguardSecure());
                android.util.Log.i(com.android.server.media.MediaSessionService.TAG, "voice-based interactions: about to use ACTION_VOICE_SEARCH_HANDS_FREE");
            } else {
                voiceIntent = new android.content.Intent("android.speech.action.WEB_SEARCH");
                android.util.Log.i(com.android.server.media.MediaSessionService.TAG, "voice-based interactions: about to use ACTION_WEB_SEARCH");
            }
            if (needWakeLock) {
                com.android.server.media.MediaSessionService.this.mMediaEventWakeLock.acquire();
            }
            try {
                try {
                    voiceIntent.setFlags(276824064);
                    if (com.android.server.media.MediaSessionService.DEBUG) {
                        android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "voiceIntent: " + voiceIntent);
                    }
                    com.android.server.media.MediaSessionService.this.mContext.startActivityAsUser(voiceIntent, android.os.UserHandle.CURRENT);
                    if (!needWakeLock) {
                        return;
                    }
                } catch (android.content.ActivityNotFoundException e) {
                    android.util.Log.w(com.android.server.media.MediaSessionService.TAG, "No activity for search: " + e);
                    if (!needWakeLock) {
                        return;
                    }
                }
                com.android.server.media.MediaSessionService.this.mMediaEventWakeLock.release();
            } catch (java.lang.Throwable th) {
                if (needWakeLock) {
                    com.android.server.media.MediaSessionService.this.mMediaEventWakeLock.release();
                }
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isVoiceKey(int keyCode) {
            return keyCode == 79 || (!com.android.server.media.MediaSessionService.this.mHasFeatureLeanback && keyCode == 85);
        }

        private boolean isUserSetupComplete() {
            return android.provider.Settings.Secure.getIntForUser(com.android.server.media.MediaSessionService.this.mContext.getContentResolver(), "user_setup_complete", 0, android.os.UserHandle.CURRENT.getIdentifier()) != 0;
        }

        private boolean isValidLocalStreamType(int streamType) {
            return streamType >= 0 && streamType <= 5;
        }

        public void expireTempEngagedSessions() {
            if (!com.android.media.flags.Flags.enableNotifyingActivityManagerWithMediaSessionStatusChange()) {
                return;
            }
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                for (java.util.Set<com.android.server.media.MediaSessionRecordImpl> uidSessions : com.android.server.media.MediaSessionService.this.mUserEngagedSessionsForFgs.values()) {
                    for (com.android.server.media.MediaSessionRecordImpl sessionRecord : uidSessions) {
                        sessionRecord.expireTempEngaged();
                    }
                }
            }
        }

        private class MediaKeyListenerResultReceiver extends android.os.ResultReceiver implements java.lang.Runnable {
            private final boolean mAsSystemService;
            private boolean mHandled;
            private final android.view.KeyEvent mKeyEvent;
            private final boolean mNeedWakeLock;
            private final java.lang.String mPackageName;
            private final int mPid;
            private final int mUid;

            private MediaKeyListenerResultReceiver(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, boolean needWakeLock) {
                super(com.android.server.media.MediaSessionService.this.mHandler);
                com.android.server.media.MediaSessionService.this.mHandler.postDelayed(this, 1000L);
                this.mPackageName = packageName;
                this.mPid = pid;
                this.mUid = uid;
                this.mAsSystemService = asSystemService;
                this.mKeyEvent = keyEvent;
                this.mNeedWakeLock = needWakeLock;
            }

            @Override // java.lang.Runnable
            public void run() {
                android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "The media key listener is timed-out for " + this.mKeyEvent);
                dispatchMediaKeyEvent();
            }

            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
                if (resultCode == 1) {
                    this.mHandled = true;
                    com.android.server.media.MediaSessionService.this.mHandler.removeCallbacks(this);
                } else {
                    dispatchMediaKeyEvent();
                }
            }

            private void dispatchMediaKeyEvent() {
                if (this.mHandled) {
                    return;
                }
                this.mHandled = true;
                com.android.server.media.MediaSessionService.this.mHandler.removeCallbacks(this);
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    if (com.android.server.media.MediaSessionService.this.isGlobalPriorityActiveLocked()) {
                        com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchMediaKeyEventLocked(this.mPackageName, this.mPid, this.mUid, this.mAsSystemService, this.mKeyEvent, this.mNeedWakeLock);
                    } else {
                        com.android.server.media.MediaSessionService.SessionManagerImpl.this.mMediaKeyEventHandler.handleMediaKeyEventLocked(this.mPackageName, this.mPid, this.mUid, this.mAsSystemService, this.mKeyEvent, this.mNeedWakeLock);
                    }
                }
            }
        }

        class KeyEventWakeLockReceiver extends android.os.ResultReceiver implements java.lang.Runnable, android.app.PendingIntent.OnFinished {
            private final android.os.Handler mHandler;
            private int mLastTimeoutId;
            private int mRefCount;

            KeyEventWakeLockReceiver(android.os.Handler handler) {
                super(handler);
                this.mRefCount = 0;
                this.mLastTimeoutId = 0;
                this.mHandler = handler;
            }

            public void onTimeout() {
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    if (this.mRefCount == 0) {
                        return;
                    }
                    this.mLastTimeoutId++;
                    this.mRefCount = 0;
                    releaseWakeLockLocked();
                }
            }

            public void acquireWakeLockLocked() {
                if (this.mRefCount == 0) {
                    com.android.server.media.MediaSessionService.this.mMediaEventWakeLock.acquire();
                }
                this.mRefCount++;
                this.mHandler.removeCallbacks(this);
                this.mHandler.postDelayed(this, 5000L);
            }

            @Override // java.lang.Runnable
            public void run() {
                onTimeout();
            }

            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
                if (resultCode < this.mLastTimeoutId) {
                    return;
                }
                synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                    if (this.mRefCount > 0) {
                        this.mRefCount--;
                        if (this.mRefCount == 0) {
                            releaseWakeLockLocked();
                        }
                    }
                }
            }

            private void releaseWakeLockLocked() {
                com.android.server.media.MediaSessionService.this.mMediaEventWakeLock.release();
                this.mHandler.removeCallbacks(this);
            }

            @Override // android.app.PendingIntent.OnFinished
            public void onSendFinished(android.app.PendingIntent pendingIntent, android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras) {
                onReceiveResult(resultCode, null);
            }
        }

        class KeyEventHandler {
            private static final int KEY_TYPE_MEDIA = 0;
            private static final int KEY_TYPE_VOLUME = 1;
            private boolean mIsLongPressing;
            private int mKeyType;
            private java.lang.Runnable mLongPressTimeoutRunnable;
            private int mMultiTapCount;
            private int mMultiTapKeyCode;
            private java.lang.Runnable mMultiTapTimeoutRunnable;
            private android.view.KeyEvent mTrackingFirstDownKeyEvent;

            KeyEventHandler(int keyType) {
                this.mKeyType = keyType;
            }

            void handleMediaKeyEventLocked(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, boolean needWakeLock) {
                handleKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, null, 0, false);
            }

            void handleVolumeKeyEventLocked(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, java.lang.String opPackageName, int stream, boolean musicOnly) {
                handleKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, false, opPackageName, stream, musicOnly);
            }

            void handleKeyEventLocked(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, boolean needWakeLock, java.lang.String opPackageName, int stream, boolean musicOnly) {
                int overriddenKeyEvents;
                if (keyEvent.isCanceled()) {
                    return;
                }
                if (com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher != null && com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.getOverriddenKeyEvents() != null) {
                    int overriddenKeyEvents2 = com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.getOverriddenKeyEvents().get(java.lang.Integer.valueOf(keyEvent.getKeyCode())).intValue();
                    overriddenKeyEvents = overriddenKeyEvents2;
                } else {
                    overriddenKeyEvents = 0;
                }
                cancelTrackingIfNeeded(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, opPackageName, stream, musicOnly, overriddenKeyEvents);
                if (!needTracking(keyEvent, overriddenKeyEvents)) {
                    if (this.mKeyType == 1) {
                        com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchVolumeKeyEventLocked(packageName, opPackageName, pid, uid, asSystemService, keyEvent, stream, musicOnly);
                        return;
                    } else {
                        com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchMediaKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock);
                        return;
                    }
                }
                if (isFirstDownKeyEvent(keyEvent)) {
                    this.mTrackingFirstDownKeyEvent = keyEvent;
                    this.mIsLongPressing = false;
                    return;
                }
                if (isFirstLongPressKeyEvent(keyEvent)) {
                    this.mIsLongPressing = true;
                }
                if (this.mIsLongPressing) {
                    handleLongPressLocked(keyEvent, needWakeLock, overriddenKeyEvents);
                    return;
                }
                if (keyEvent.getAction() == 1) {
                    this.mTrackingFirstDownKeyEvent = null;
                    if (shouldTrackForMultipleTapsLocked(overriddenKeyEvents)) {
                        if (this.mMultiTapCount == 0) {
                            this.mMultiTapTimeoutRunnable = createSingleTapRunnable(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, opPackageName, stream, musicOnly, com.android.server.media.MediaKeyDispatcher.isSingleTapOverridden(overriddenKeyEvents));
                            if (!com.android.server.media.MediaKeyDispatcher.isSingleTapOverridden(overriddenKeyEvents) || com.android.server.media.MediaKeyDispatcher.isDoubleTapOverridden(overriddenKeyEvents) || com.android.server.media.MediaKeyDispatcher.isTripleTapOverridden(overriddenKeyEvents)) {
                                com.android.server.media.MediaSessionService.this.mHandler.postDelayed(this.mMultiTapTimeoutRunnable, com.android.server.media.MediaSessionService.MULTI_TAP_TIMEOUT);
                                this.mMultiTapCount = 1;
                                this.mMultiTapKeyCode = keyEvent.getKeyCode();
                                return;
                            }
                            this.mMultiTapTimeoutRunnable.run();
                            return;
                        }
                        if (this.mMultiTapCount == 1) {
                            com.android.server.media.MediaSessionService.this.mHandler.removeCallbacks(this.mMultiTapTimeoutRunnable);
                            this.mMultiTapTimeoutRunnable = createDoubleTapRunnable(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, opPackageName, stream, musicOnly, com.android.server.media.MediaKeyDispatcher.isSingleTapOverridden(overriddenKeyEvents), com.android.server.media.MediaKeyDispatcher.isDoubleTapOverridden(overriddenKeyEvents));
                            if (com.android.server.media.MediaKeyDispatcher.isTripleTapOverridden(overriddenKeyEvents)) {
                                com.android.server.media.MediaSessionService.this.mHandler.postDelayed(this.mMultiTapTimeoutRunnable, com.android.server.media.MediaSessionService.MULTI_TAP_TIMEOUT);
                                this.mMultiTapCount = 2;
                                return;
                            } else {
                                this.mMultiTapTimeoutRunnable.run();
                                return;
                            }
                        }
                        if (this.mMultiTapCount == 2) {
                            com.android.server.media.MediaSessionService.this.mHandler.removeCallbacks(this.mMultiTapTimeoutRunnable);
                            onTripleTap(keyEvent);
                            return;
                        }
                        return;
                    }
                    dispatchDownAndUpKeyEventsLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, opPackageName, stream, musicOnly);
                }
            }

            private boolean shouldTrackForMultipleTapsLocked(int overriddenKeyEvents) {
                return com.android.server.media.MediaKeyDispatcher.isSingleTapOverridden(overriddenKeyEvents) || com.android.server.media.MediaKeyDispatcher.isDoubleTapOverridden(overriddenKeyEvents) || com.android.server.media.MediaKeyDispatcher.isTripleTapOverridden(overriddenKeyEvents);
            }

            private void cancelTrackingIfNeeded(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, boolean needWakeLock, java.lang.String opPackageName, int stream, boolean musicOnly, int overriddenKeyEvents) {
                if (this.mTrackingFirstDownKeyEvent == null && this.mMultiTapTimeoutRunnable == null) {
                    return;
                }
                if (isFirstDownKeyEvent(keyEvent)) {
                    if (this.mLongPressTimeoutRunnable != null) {
                        com.android.server.media.MediaSessionService.this.mHandler.removeCallbacks(this.mLongPressTimeoutRunnable);
                        this.mLongPressTimeoutRunnable.run();
                    }
                    if (this.mMultiTapTimeoutRunnable != null && keyEvent.getKeyCode() != this.mMultiTapKeyCode) {
                        runExistingMultiTapRunnableLocked();
                    }
                    resetLongPressTracking();
                    return;
                }
                if (this.mTrackingFirstDownKeyEvent != null && this.mTrackingFirstDownKeyEvent.getDownTime() == keyEvent.getDownTime() && this.mTrackingFirstDownKeyEvent.getKeyCode() == keyEvent.getKeyCode() && keyEvent.getAction() == 0) {
                    if (isFirstLongPressKeyEvent(keyEvent)) {
                        if (this.mMultiTapTimeoutRunnable != null) {
                            runExistingMultiTapRunnableLocked();
                        }
                        if ((overriddenKeyEvents & 8) == 0) {
                            if (this.mKeyType == 1) {
                                if (com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mOnVolumeKeyLongPressListener == null) {
                                    com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchVolumeKeyEventLocked(packageName, opPackageName, pid, uid, asSystemService, keyEvent, stream, musicOnly);
                                    this.mTrackingFirstDownKeyEvent = null;
                                    return;
                                }
                                return;
                            }
                            if (!com.android.server.media.MediaSessionService.SessionManagerImpl.this.isVoiceKey(keyEvent.getKeyCode())) {
                                com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchMediaKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock);
                                this.mTrackingFirstDownKeyEvent = null;
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (keyEvent.getRepeatCount() > 1 && !this.mIsLongPressing) {
                        resetLongPressTracking();
                    }
                }
            }

            private boolean needTracking(android.view.KeyEvent keyEvent, int overriddenKeyEvents) {
                if (!isFirstDownKeyEvent(keyEvent) && (this.mTrackingFirstDownKeyEvent == null || this.mTrackingFirstDownKeyEvent.getDownTime() != keyEvent.getDownTime() || this.mTrackingFirstDownKeyEvent.getKeyCode() != keyEvent.getKeyCode())) {
                    return false;
                }
                if (overriddenKeyEvents == 0) {
                    if (this.mKeyType == 1) {
                        if (com.android.server.media.MediaSessionService.this.mCurrentFullUserRecord.mOnVolumeKeyLongPressListener == null) {
                            return false;
                        }
                    } else if (!com.android.server.media.MediaSessionService.SessionManagerImpl.this.isVoiceKey(keyEvent.getKeyCode())) {
                        return false;
                    }
                }
                return true;
            }

            private void runExistingMultiTapRunnableLocked() {
                com.android.server.media.MediaSessionService.this.mHandler.removeCallbacks(this.mMultiTapTimeoutRunnable);
                this.mMultiTapTimeoutRunnable.run();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void resetMultiTapTrackingLocked() {
                this.mMultiTapCount = 0;
                this.mMultiTapTimeoutRunnable = null;
                this.mMultiTapKeyCode = 0;
            }

            private void handleLongPressLocked(android.view.KeyEvent keyEvent, boolean needWakeLock, int overriddenKeyEvents) {
                if (com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher != null && com.android.server.media.MediaKeyDispatcher.isLongPressOverridden(overriddenKeyEvents)) {
                    com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.onLongPress(keyEvent);
                    if (this.mLongPressTimeoutRunnable != null) {
                        com.android.server.media.MediaSessionService.this.mHandler.removeCallbacks(this.mLongPressTimeoutRunnable);
                    }
                    if (keyEvent.getAction() == 0) {
                        if (this.mLongPressTimeoutRunnable == null) {
                            this.mLongPressTimeoutRunnable = createLongPressTimeoutRunnable(keyEvent);
                        }
                        com.android.server.media.MediaSessionService.this.mHandler.postDelayed(this.mLongPressTimeoutRunnable, com.android.server.media.MediaSessionService.LONG_PRESS_TIMEOUT);
                        return;
                    }
                    resetLongPressTracking();
                    return;
                }
                if (this.mKeyType == 1) {
                    if (isFirstLongPressKeyEvent(keyEvent)) {
                        com.android.server.media.MediaSessionService.this.dispatchVolumeKeyLongPressLocked(this.mTrackingFirstDownKeyEvent);
                    }
                    com.android.server.media.MediaSessionService.this.dispatchVolumeKeyLongPressLocked(keyEvent);
                } else if (isFirstLongPressKeyEvent(keyEvent) && com.android.server.media.MediaSessionService.SessionManagerImpl.this.isVoiceKey(keyEvent.getKeyCode())) {
                    com.android.server.media.MediaSessionService.SessionManagerImpl.this.startVoiceInput(needWakeLock);
                    resetLongPressTracking();
                }
            }

            private java.lang.Runnable createLongPressTimeoutRunnable(final android.view.KeyEvent keyEvent) {
                return new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher != null) {
                            com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.onLongPress(com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.this.createCanceledKeyEvent(keyEvent));
                        }
                        com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.this.resetLongPressTracking();
                    }
                };
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void resetLongPressTracking() {
                this.mTrackingFirstDownKeyEvent = null;
                this.mIsLongPressing = false;
                this.mLongPressTimeoutRunnable = null;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public android.view.KeyEvent createCanceledKeyEvent(android.view.KeyEvent keyEvent) {
                android.view.KeyEvent upEvent = android.view.KeyEvent.changeAction(keyEvent, 1);
                return android.view.KeyEvent.changeTimeRepeat(upEvent, java.lang.System.currentTimeMillis(), 0, 32);
            }

            private boolean isFirstLongPressKeyEvent(android.view.KeyEvent keyEvent) {
                return (keyEvent.getFlags() & 128) != 0 && keyEvent.getRepeatCount() == 1;
            }

            private boolean isFirstDownKeyEvent(android.view.KeyEvent keyEvent) {
                return keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void dispatchDownAndUpKeyEventsLocked(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, boolean needWakeLock, java.lang.String opPackageName, int stream, boolean musicOnly) {
                android.view.KeyEvent downEvent = android.view.KeyEvent.changeAction(keyEvent, 0);
                if (this.mKeyType == 1) {
                    com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchVolumeKeyEventLocked(packageName, opPackageName, pid, uid, asSystemService, downEvent, stream, musicOnly);
                    com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchVolumeKeyEventLocked(packageName, opPackageName, pid, uid, asSystemService, keyEvent, stream, musicOnly);
                } else {
                    com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchMediaKeyEventLocked(packageName, pid, uid, asSystemService, downEvent, needWakeLock);
                    com.android.server.media.MediaSessionService.SessionManagerImpl.this.dispatchMediaKeyEventLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock);
                }
            }

            java.lang.Runnable createSingleTapRunnable(final java.lang.String packageName, final int pid, final int uid, final boolean asSystemService, final android.view.KeyEvent keyEvent, final boolean needWakeLock, final java.lang.String opPackageName, final int stream, final boolean musicOnly, final boolean overridden) {
                return new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.2
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.this.resetMultiTapTrackingLocked();
                        if (overridden) {
                            com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.onSingleTap(keyEvent);
                        } else {
                            com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.this.dispatchDownAndUpKeyEventsLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, opPackageName, stream, musicOnly);
                        }
                    }
                };
            }

            java.lang.Runnable createDoubleTapRunnable(final java.lang.String packageName, final int pid, final int uid, final boolean asSystemService, final android.view.KeyEvent keyEvent, final boolean needWakeLock, final java.lang.String opPackageName, final int stream, final boolean musicOnly, final boolean singleTapOverridden, final boolean doubleTapOverridden) {
                return new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.3
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.this.resetMultiTapTrackingLocked();
                        if (doubleTapOverridden) {
                            com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.onDoubleTap(keyEvent);
                        } else if (singleTapOverridden) {
                            com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.onSingleTap(keyEvent);
                            com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.onSingleTap(keyEvent);
                        } else {
                            com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.this.dispatchDownAndUpKeyEventsLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, opPackageName, stream, musicOnly);
                            com.android.server.media.MediaSessionService.SessionManagerImpl.KeyEventHandler.this.dispatchDownAndUpKeyEventsLocked(packageName, pid, uid, asSystemService, keyEvent, needWakeLock, opPackageName, stream, musicOnly);
                        }
                    }
                };
            }

            private void onTripleTap(android.view.KeyEvent keyEvent) {
                resetMultiTapTrackingLocked();
                com.android.server.media.MediaSessionService.this.mCustomMediaKeyDispatcher.onTripleTap(keyEvent);
            }
        }
    }

    final class MessageHandler extends android.os.Handler {
        private static final int MSG_SESSIONS_1_CHANGED = 1;
        private static final int MSG_SESSIONS_2_CHANGED = 2;
        private final android.util.SparseArray<java.lang.Integer> mIntegerCache = new android.util.SparseArray<>();

        MessageHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.media.MediaSessionService.this.pushSession1Changed(((java.lang.Integer) msg.obj).intValue());
                    break;
                case 2:
                    com.android.server.media.MediaSessionService.this.pushSession2Changed(((java.lang.Integer) msg.obj).intValue());
                    break;
            }
        }

        public void postSessionsChanged(com.android.server.media.MediaSessionRecordImpl record) {
            java.lang.Integer userIdInteger = this.mIntegerCache.get(record.getUserId());
            if (userIdInteger == null) {
                userIdInteger = java.lang.Integer.valueOf(record.getUserId());
                this.mIntegerCache.put(record.getUserId(), userIdInteger);
            }
            int msg = record instanceof com.android.server.media.MediaSessionRecord ? 1 : 2;
            removeMessages(msg, userIdInteger);
            obtainMessage(msg, userIdInteger).sendToTarget();
        }
    }

    private final class NotificationListener extends android.service.notification.NotificationListenerService {
        private NotificationListener() {
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationPosted(android.service.notification.StatusBarNotification sbn) {
            super.onNotificationPosted(sbn);
            android.app.Notification postedNotification = sbn.getNotification();
            int uid = sbn.getUid();
            if (!postedNotification.isMediaNotification()) {
                return;
            }
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                com.android.server.media.MediaSessionService.this.mMediaNotifications.putIfAbsent(java.lang.Integer.valueOf(uid), new java.util.HashSet());
                ((java.util.Set) com.android.server.media.MediaSessionService.this.mMediaNotifications.get(java.lang.Integer.valueOf(uid))).add(postedNotification);
                for (com.android.server.media.MediaSessionRecordImpl mediaSessionRecord : (java.util.Set) com.android.server.media.MediaSessionService.this.mUserEngagedSessionsForFgs.getOrDefault(java.lang.Integer.valueOf(uid), java.util.Set.of())) {
                    if (mediaSessionRecord.isLinkedToNotification(postedNotification)) {
                        com.android.server.media.MediaSessionService.this.startFgsDelegateLocked(mediaSessionRecord);
                        return;
                    }
                }
            }
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationRemoved(android.service.notification.StatusBarNotification sbn) {
            super.onNotificationRemoved(sbn);
            android.app.Notification removedNotification = sbn.getNotification();
            int uid = sbn.getUid();
            if (!removedNotification.isMediaNotification()) {
                return;
            }
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                java.util.Set<android.app.Notification> uidMediaNotifications = (java.util.Set) com.android.server.media.MediaSessionService.this.mMediaNotifications.get(java.lang.Integer.valueOf(uid));
                if (uidMediaNotifications != null) {
                    uidMediaNotifications.remove(removedNotification);
                    if (uidMediaNotifications.isEmpty()) {
                        com.android.server.media.MediaSessionService.this.mMediaNotifications.remove(java.lang.Integer.valueOf(uid));
                    }
                }
                com.android.server.media.MediaSessionRecordImpl notificationRecord = getLinkedMediaSessionRecord(uid, removedNotification);
                if (notificationRecord == null) {
                    return;
                }
                com.android.server.media.MediaSessionService.this.stopFgsIfNoSessionIsLinkedToNotification(notificationRecord);
            }
        }

        private com.android.server.media.MediaSessionRecordImpl getLinkedMediaSessionRecord(int uid, android.app.Notification notification) {
            synchronized (com.android.server.media.MediaSessionService.this.mLock) {
                for (com.android.server.media.MediaSessionRecordImpl mediaSessionRecord : (java.util.Set) com.android.server.media.MediaSessionService.this.mUserEngagedSessionsForFgs.getOrDefault(java.lang.Integer.valueOf(uid), java.util.Set.of())) {
                    if (mediaSessionRecord.isLinkedToNotification(notification)) {
                        return mediaSessionRecord;
                    }
                }
                return null;
            }
        }
    }

    public com.android.server.media.IMediaSessionServiceWrapper getWrapper() {
        return this.mMediaSSWrapper;
    }

    private class MediaSessionServiceWrapper implements com.android.server.media.IMediaSessionServiceWrapper {
        private MediaSessionServiceWrapper() {
        }

        @Override // com.android.server.media.IMediaSessionServiceWrapper
        public com.android.server.media.IMediaSessionServiceExt getExtImpl() {
            return com.android.server.media.MediaSessionService.this.mMediaSessionServiceExt;
        }

        @Override // com.android.server.media.IMediaSessionServiceWrapper
        public void updateMediaButtonReceiverInfo(android.content.ContentResolver contentResolver, com.android.server.media.MediaButtonReceiverHolder receiverHolder, int fullUserId) {
            java.lang.String[] mediaInfos;
            java.util.Stack<java.lang.String> infoStack = new java.util.Stack<>();
            if (receiverHolder != null) {
                java.lang.String info = receiverHolder.flattenToString();
                java.lang.String mediaButtonReceiverInfo = android.provider.Settings.Secure.getStringForUser(contentResolver, com.android.server.media.MediaSessionService.MEDIA_BUTTON_RECEIVER, fullUserId);
                if (mediaButtonReceiverInfo != null && (mediaInfos = mediaButtonReceiverInfo.split(com.android.server.media.MediaSessionService.MEDIA_ID_DELIM)) != null && mediaInfos.length != 0) {
                    for (int i = mediaInfos.length - 1; i >= 0; i--) {
                        if (mediaInfos[i].trim().length() == 0) {
                            android.util.Log.d(com.android.server.media.MediaSessionService.TAG, "updateMediaButtonReceiverInfo not add invalid info data");
                        } else {
                            infoStack.add(mediaInfos[i]);
                        }
                    }
                }
                if (!info.equals("") && getExtImpl().isInHistoryPlayInfoWhiteList(receiverHolder.getPackageName()) && !getExtImpl().isInMediaBlackList(receiverHolder.getPackageName())) {
                    infoStack.remove(info);
                    while (infoStack.size() >= 10) {
                        infoStack.remove(0);
                    }
                    while (!infoStack.isEmpty()) {
                        info = java.lang.String.join(com.android.server.media.MediaSessionService.MEDIA_ID_DELIM, info, infoStack.pop());
                    }
                    android.util.Log.i(com.android.server.media.MediaSessionService.TAG, "updateMediaButtonReceiverInfo info: " + info);
                    android.provider.Settings.Secure.putStringForUser(contentResolver, com.android.server.media.MediaSessionService.MEDIA_BUTTON_RECEIVER, info, fullUserId);
                }
            }
        }
    }
}
