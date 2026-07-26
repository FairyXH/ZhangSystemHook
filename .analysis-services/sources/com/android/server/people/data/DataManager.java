package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class DataManager {
    private static final boolean DEBUG = false;
    static final int MAX_CACHED_RECENT_SHORTCUTS = 30;
    private static final long QUERY_EVENTS_MAX_AGE_MS = 300000;
    private static final long RECENT_NOTIFICATIONS_MAX_AGE_MS = 864000000;
    private static final java.lang.String TAG = "DataManager";
    private static final long USAGE_STATS_QUERY_INTERVAL_SEC = 120;
    private final android.util.SparseArray<android.content.BroadcastReceiver> mBroadcastReceivers;
    private android.database.ContentObserver mCallLogContentObserver;
    private final android.util.SparseArray<android.database.ContentObserver> mContactsContentObservers;
    private final android.content.Context mContext;
    private final java.util.List<com.android.server.people.PeopleService.ConversationsListener> mConversationsListeners;
    private final android.os.Handler mHandler;
    private final com.android.server.people.data.DataManager.Injector mInjector;
    private final java.lang.Object mLock;
    private android.database.ContentObserver mMmsSmsContentObserver;
    private final android.util.SparseArray<com.android.server.people.data.DataManager.NotificationListener> mNotificationListeners;
    private com.android.server.notification.NotificationManagerInternal mNotificationManagerInternal;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final android.util.SparseArray<com.android.internal.content.PackageMonitor> mPackageMonitors;
    private final java.util.concurrent.ScheduledExecutorService mScheduledExecutor;
    private android.content.pm.ShortcutServiceInternal mShortcutServiceInternal;
    private com.android.server.people.data.ConversationStatusExpirationBroadcastReceiver mStatusExpReceiver;
    private final android.util.SparseArray<java.util.concurrent.ScheduledFuture<?>> mUsageStatsQueryFutures;
    private final android.util.SparseArray<com.android.server.people.data.UserData> mUserDataArray;
    private android.os.UserManager mUserManager;

    public DataManager(android.content.Context context) {
        this(context, new com.android.server.people.data.DataManager.Injector(), com.android.internal.os.BackgroundThread.get().getLooper());
    }

    DataManager(android.content.Context context, com.android.server.people.data.DataManager.Injector injector, android.os.Looper looper) {
        this.mLock = new java.lang.Object();
        this.mUserDataArray = new android.util.SparseArray<>();
        this.mBroadcastReceivers = new android.util.SparseArray<>();
        this.mContactsContentObservers = new android.util.SparseArray<>();
        this.mUsageStatsQueryFutures = new android.util.SparseArray<>();
        this.mNotificationListeners = new android.util.SparseArray<>();
        this.mPackageMonitors = new android.util.SparseArray<>();
        this.mConversationsListeners = new java.util.ArrayList(1);
        this.mContext = context;
        this.mInjector = injector;
        this.mScheduledExecutor = this.mInjector.createScheduledExecutor();
        this.mHandler = new android.os.Handler(looper);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void initialize() {
        this.mShortcutServiceInternal = (android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mNotificationManagerInternal = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        this.mShortcutServiceInternal.addShortcutChangeCallback(new com.android.server.people.data.DataManager.ShortcutServiceCallback());
        this.mStatusExpReceiver = new com.android.server.people.data.ConversationStatusExpirationBroadcastReceiver();
        this.mContext.registerReceiver(this.mStatusExpReceiver, com.android.server.people.data.ConversationStatusExpirationBroadcastReceiver.getFilter(), 4);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN");
        this.mContext.registerReceiver(new com.android.server.people.data.DataManager.ShutdownBroadcastReceiver(), intentFilter);
    }

    public void onUserUnlocked(final int userId) {
        synchronized (this.mLock) {
            com.android.server.people.data.UserData userData = this.mUserDataArray.get(userId);
            if (userData == null) {
                userData = new com.android.server.people.data.UserData(userId, this.mScheduledExecutor);
                this.mUserDataArray.put(userId, userData);
            }
            userData.setUserUnlocked();
        }
        this.mScheduledExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserUnlocked$0(userId);
            }
        });
    }

    public void onUserStopping(final int userId) {
        synchronized (this.mLock) {
            com.android.server.people.data.UserData userData = this.mUserDataArray.get(userId);
            if (userData != null) {
                userData.setUserStopped();
            }
        }
        this.mScheduledExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserStopping$1(userId);
            }
        });
    }

    void forPackagesInProfile(int callingUserId, java.util.function.Consumer<com.android.server.people.data.PackageData> consumer) {
        java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getEnabledProfiles(callingUserId);
        for (android.content.pm.UserInfo userInfo : users) {
            com.android.server.people.data.UserData userData = getUnlockedUserData(userInfo.id);
            if (userData != null) {
                userData.forAllPackages(consumer);
            }
        }
    }

    public com.android.server.people.data.PackageData getPackage(java.lang.String packageName, int userId) {
        com.android.server.people.data.UserData userData = getUnlockedUserData(userId);
        if (userData != null) {
            return userData.getPackageData(packageName);
        }
        return null;
    }

    public android.content.pm.ShortcutInfo getShortcut(java.lang.String packageName, int userId, java.lang.String shortcutId) {
        java.util.List<android.content.pm.ShortcutInfo> shortcuts = getShortcuts(packageName, userId, java.util.Collections.singletonList(shortcutId));
        if (shortcuts != null && !shortcuts.isEmpty()) {
            return shortcuts.get(0);
        }
        return null;
    }

    public java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> getShareShortcuts(android.content.IntentFilter intentFilter, int callingUserId) {
        return this.mShortcutServiceInternal.getShareTargets(this.mContext.getPackageName(), intentFilter, callingUserId);
    }

    public android.app.people.ConversationChannel getConversation(java.lang.String packageName, int userId, java.lang.String shortcutId) {
        com.android.server.people.data.PackageData packageData;
        com.android.server.people.data.UserData userData = getUnlockedUserData(userId);
        if (userData != null && (packageData = userData.getPackageData(packageName)) != null) {
            com.android.server.people.data.ConversationInfo conversationInfo = packageData.getConversationInfo(shortcutId);
            return getConversationChannel(packageName, userId, shortcutId, conversationInfo);
        }
        return null;
    }

    com.android.server.people.data.ConversationInfo getConversationInfo(java.lang.String packageName, int userId, java.lang.String shortcutId) {
        com.android.server.people.data.PackageData packageData;
        com.android.server.people.data.UserData userData = getUnlockedUserData(userId);
        if (userData != null && (packageData = userData.getPackageData(packageName)) != null) {
            return packageData.getConversationInfo(shortcutId);
        }
        return null;
    }

    private android.app.people.ConversationChannel getConversationChannel(java.lang.String packageName, int userId, java.lang.String shortcutId, com.android.server.people.data.ConversationInfo conversationInfo) {
        android.content.pm.ShortcutInfo shortcutInfo = getShortcut(packageName, userId, shortcutId);
        return getConversationChannel(shortcutInfo, conversationInfo, packageName, userId, shortcutId);
    }

    private android.app.people.ConversationChannel getConversationChannel(android.content.pm.ShortcutInfo shortcutInfo, com.android.server.people.data.ConversationInfo conversationInfo, final java.lang.String packageName, final int userId, final java.lang.String shortcutId) {
        android.app.NotificationChannelGroup parentChannelGroup;
        if (conversationInfo == null || conversationInfo.isDemoted()) {
            return null;
        }
        if (shortcutInfo == null) {
            android.util.Slog.e(TAG, "Shortcut no longer found");
            this.mInjector.getBackgroundExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getConversationChannel$2(packageName, userId, shortcutId);
                }
            });
            return null;
        }
        int uid = this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
        android.app.NotificationChannel parentChannel = this.mNotificationManagerInternal.getNotificationChannel(packageName, uid, conversationInfo.getNotificationChannelId());
        if (parentChannel == null) {
            parentChannelGroup = null;
        } else {
            android.app.NotificationChannelGroup parentChannelGroup2 = this.mNotificationManagerInternal.getNotificationChannelGroup(packageName, uid, parentChannel.getId());
            parentChannelGroup = parentChannelGroup2;
        }
        return new android.app.people.ConversationChannel(shortcutInfo, uid, parentChannel, parentChannelGroup, conversationInfo.getLastEventTimestamp(), hasActiveNotifications(packageName, userId, shortcutId), false, getStatuses(conversationInfo));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getConversationChannel$2(java.lang.String packageName, int userId, java.lang.String shortcutId) {
        removeConversations(packageName, userId, java.util.Set.of(shortcutId));
    }

    public java.util.List<android.app.people.ConversationChannel> getRecentConversations(int callingUserId) {
        final java.util.List<android.app.people.ConversationChannel> conversationChannels = new java.util.ArrayList<>();
        forPackagesInProfile(callingUserId, new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getRecentConversations$4(conversationChannels, (com.android.server.people.data.PackageData) obj);
            }
        });
        return conversationChannels;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRecentConversations$4(final java.util.List conversationChannels, final com.android.server.people.data.PackageData packageData) {
        packageData.forAllConversations(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getRecentConversations$3(packageData, conversationChannels, (com.android.server.people.data.ConversationInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRecentConversations$3(com.android.server.people.data.PackageData packageData, java.util.List conversationChannels, com.android.server.people.data.ConversationInfo conversationInfo) {
        if (!isCachedRecentConversation(conversationInfo)) {
            return;
        }
        java.lang.String shortcutId = conversationInfo.getShortcutId();
        android.app.people.ConversationChannel channel = getConversationChannel(packageData.getPackageName(), packageData.getUserId(), shortcutId, conversationInfo);
        if (channel == null || channel.getNotificationChannel() == null) {
            return;
        }
        conversationChannels.add(channel);
    }

    public void removeRecentConversation(java.lang.String packageName, int userId, java.lang.String shortcutId, int callingUserId) {
        if (!hasActiveNotifications(packageName, userId, shortcutId)) {
            this.mShortcutServiceInternal.uncacheShortcuts(callingUserId, this.mContext.getPackageName(), packageName, java.util.Collections.singletonList(shortcutId), userId, 16384);
        }
    }

    public void removeAllRecentConversations(int callingUserId) {
        pruneOldRecentConversations(callingUserId, Long.MAX_VALUE);
    }

    public void pruneOldRecentConversations(final int callingUserId, final long currentTimeMs) {
        forPackagesInProfile(callingUserId, new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$pruneOldRecentConversations$6(currentTimeMs, callingUserId, (com.android.server.people.data.PackageData) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pruneOldRecentConversations$6(final long currentTimeMs, int callingUserId, com.android.server.people.data.PackageData packageData) {
        final java.lang.String packageName = packageData.getPackageName();
        final int userId = packageData.getUserId();
        final java.util.List<java.lang.String> idsToUncache = new java.util.ArrayList<>();
        packageData.forAllConversations(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$pruneOldRecentConversations$5(currentTimeMs, packageName, userId, idsToUncache, (com.android.server.people.data.ConversationInfo) obj);
            }
        });
        if (!idsToUncache.isEmpty()) {
            this.mShortcutServiceInternal.uncacheShortcuts(callingUserId, this.mContext.getPackageName(), packageName, idsToUncache, userId, 16384);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pruneOldRecentConversations$5(long currentTimeMs, java.lang.String packageName, int userId, java.util.List idsToUncache, com.android.server.people.data.ConversationInfo conversationInfo) {
        java.lang.String shortcutId = conversationInfo.getShortcutId();
        if (isCachedRecentConversation(conversationInfo) && currentTimeMs - conversationInfo.getLastEventTimestamp() > RECENT_NOTIFICATIONS_MAX_AGE_MS && !hasActiveNotifications(packageName, userId, shortcutId)) {
            idsToUncache.add(shortcutId);
        }
    }

    public void pruneExpiredConversationStatuses(int callingUserId, final long currentTimeMs) {
        forPackagesInProfile(callingUserId, new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$pruneExpiredConversationStatuses$8(currentTimeMs, (com.android.server.people.data.PackageData) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pruneExpiredConversationStatuses$8(final long currentTimeMs, final com.android.server.people.data.PackageData packageData) {
        if (packageData == null) {
            return;
        }
        final com.android.server.people.data.ConversationStore cs = packageData.getConversationStore();
        packageData.forAllConversations(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$pruneExpiredConversationStatuses$7(currentTimeMs, cs, packageData, (com.android.server.people.data.ConversationInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pruneExpiredConversationStatuses$7(long currentTimeMs, com.android.server.people.data.ConversationStore cs, com.android.server.people.data.PackageData packageData, com.android.server.people.data.ConversationInfo conversationInfo) {
        com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder(conversationInfo);
        java.util.List<android.app.people.ConversationStatus> newStatuses = new java.util.ArrayList<>();
        for (android.app.people.ConversationStatus status : conversationInfo.getStatuses()) {
            if (status.getEndTimeMillis() < 0 || currentTimeMs < status.getEndTimeMillis()) {
                newStatuses.add(status);
            }
        }
        builder.setStatuses(newStatuses);
        updateConversationStoreThenNotifyListeners(cs, builder.build(), packageData.getPackageName(), packageData.getUserId());
    }

    public boolean isConversation(java.lang.String packageName, int userId, java.lang.String shortcutId) {
        android.app.people.ConversationChannel channel = getConversation(packageName, userId, shortcutId);
        return (channel == null || channel.getShortcutInfo() == null || android.text.TextUtils.isEmpty(channel.getShortcutInfo().getLabel())) ? false : true;
    }

    public long getLastInteraction(java.lang.String packageName, int userId, java.lang.String shortcutId) {
        com.android.server.people.data.ConversationInfo conversationInfo;
        com.android.server.people.data.PackageData packageData = getPackage(packageName, userId);
        if (packageData != null && (conversationInfo = packageData.getConversationInfo(shortcutId)) != null) {
            return conversationInfo.getLastEventTimestamp();
        }
        return 0L;
    }

    public void addOrUpdateStatus(java.lang.String packageName, int userId, java.lang.String conversationId, android.app.people.ConversationStatus status) {
        com.android.server.people.data.ConversationStore cs = getConversationStoreOrThrow(packageName, userId);
        com.android.server.people.data.ConversationInfo convToModify = getConversationInfoOrThrow(cs, conversationId);
        com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder(convToModify);
        builder.addOrUpdateStatus(status);
        updateConversationStoreThenNotifyListeners(cs, builder.build(), packageName, userId);
        if (status.getEndTimeMillis() >= 0) {
            this.mStatusExpReceiver.scheduleExpiration(this.mContext, userId, packageName, conversationId, status);
        }
    }

    public void clearStatus(java.lang.String packageName, int userId, java.lang.String conversationId, java.lang.String statusId) {
        com.android.server.people.data.ConversationStore cs = getConversationStoreOrThrow(packageName, userId);
        com.android.server.people.data.ConversationInfo convToModify = getConversationInfoOrThrow(cs, conversationId);
        com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder(convToModify);
        builder.clearStatus(statusId);
        updateConversationStoreThenNotifyListeners(cs, builder.build(), packageName, userId);
    }

    public void clearStatuses(java.lang.String packageName, int userId, java.lang.String conversationId) {
        com.android.server.people.data.ConversationStore cs = getConversationStoreOrThrow(packageName, userId);
        com.android.server.people.data.ConversationInfo convToModify = getConversationInfoOrThrow(cs, conversationId);
        com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder(convToModify);
        builder.setStatuses(null);
        updateConversationStoreThenNotifyListeners(cs, builder.build(), packageName, userId);
    }

    public java.util.List<android.app.people.ConversationStatus> getStatuses(java.lang.String packageName, int userId, java.lang.String conversationId) {
        com.android.server.people.data.ConversationStore cs = getConversationStoreOrThrow(packageName, userId);
        com.android.server.people.data.ConversationInfo conversationInfo = getConversationInfoOrThrow(cs, conversationId);
        return getStatuses(conversationInfo);
    }

    private java.util.List<android.app.people.ConversationStatus> getStatuses(com.android.server.people.data.ConversationInfo conversationInfo) {
        java.util.Collection<? extends android.app.people.ConversationStatus> statuses = conversationInfo.getStatuses();
        if (statuses != null) {
            java.util.ArrayList<android.app.people.ConversationStatus> list = new java.util.ArrayList<>(statuses.size());
            list.addAll(statuses);
            return list;
        }
        return new java.util.ArrayList();
    }

    private com.android.server.people.data.ConversationStore getConversationStoreOrThrow(java.lang.String packageName, int userId) {
        com.android.server.people.data.PackageData packageData = getPackage(packageName, userId);
        if (packageData == null) {
            throw new java.lang.IllegalArgumentException("No settings exist for package " + packageName);
        }
        com.android.server.people.data.ConversationStore cs = packageData.getConversationStore();
        if (cs == null) {
            throw new java.lang.IllegalArgumentException("No conversations exist for package " + packageName);
        }
        return cs;
    }

    private com.android.server.people.data.ConversationInfo getConversationInfoOrThrow(com.android.server.people.data.ConversationStore cs, java.lang.String conversationId) {
        com.android.server.people.data.ConversationInfo ci = cs.getConversation(conversationId);
        if (ci == null) {
            throw new java.lang.IllegalArgumentException("Conversation does not exist");
        }
        return ci;
    }

    public void reportShareTargetEvent(android.app.prediction.AppTargetEvent event, android.content.IntentFilter intentFilter) {
        com.android.server.people.data.UserData userData;
        com.android.server.people.data.EventHistoryImpl eventHistory;
        android.app.prediction.AppTarget appTarget = event.getTarget();
        if (appTarget == null || event.getAction() != 1 || (userData = getUnlockedUserData(appTarget.getUser().getIdentifier())) == null) {
            return;
        }
        com.android.server.people.data.PackageData packageData = userData.getOrCreatePackageData(appTarget.getPackageName());
        int eventType = mimeTypeToShareEventType(intentFilter.getDataType(0));
        if ("direct_share".equals(event.getLaunchLocation())) {
            if (appTarget.getShortcutInfo() == null) {
                return;
            }
            java.lang.String shortcutId = appTarget.getShortcutInfo().getId();
            if ("chooser_target".equals(shortcutId)) {
                return;
            }
            if (packageData.getConversationStore().getConversation(shortcutId) == null) {
                addOrUpdateConversationInfo(appTarget.getShortcutInfo());
            }
            eventHistory = packageData.getEventStore().getOrCreateEventHistory(0, shortcutId);
        } else {
            eventHistory = packageData.getEventStore().getOrCreateEventHistory(4, appTarget.getClassName());
        }
        eventHistory.addEvent(new com.android.server.people.data.Event(java.lang.System.currentTimeMillis(), eventType));
    }

    public java.util.List<android.app.usage.UsageEvents.Event> queryAppMovingToForegroundEvents(int callingUserId, long startTime, long endTime) {
        return com.android.server.people.data.UsageStatsQueryHelper.queryAppMovingToForegroundEvents(callingUserId, startTime, endTime);
    }

    public java.util.Map<java.lang.String, com.android.server.people.data.AppUsageStatsData> queryAppUsageStats(int callingUserId, long startTime, long endTime, java.util.Set<java.lang.String> packageNameFilter) {
        return com.android.server.people.data.UsageStatsQueryHelper.queryAppUsageStats(callingUserId, startTime, endTime, packageNameFilter);
    }

    public void pruneDataForUser(final int userId, final android.os.CancellationSignal signal) {
        com.android.server.people.data.UserData userData = getUnlockedUserData(userId);
        if (userData == null || signal.isCanceled()) {
            return;
        }
        pruneUninstalledPackageData(userData);
        userData.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$pruneDataForUser$9(signal, userId, (com.android.server.people.data.PackageData) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pruneDataForUser$9(android.os.CancellationSignal signal, int userId, com.android.server.people.data.PackageData packageData) {
        if (signal.isCanceled()) {
            return;
        }
        packageData.getEventStore().pruneOldEvents();
        if (!packageData.isDefaultDialer()) {
            packageData.getEventStore().deleteEventHistories(2);
        }
        if (!packageData.isDefaultSmsApp()) {
            packageData.getEventStore().deleteEventHistories(3);
        }
        packageData.pruneOrphanEvents();
        pruneExpiredConversationStatuses(userId, java.lang.System.currentTimeMillis());
        pruneOldRecentConversations(userId, java.lang.System.currentTimeMillis());
        cleanupCachedShortcuts(userId, 30);
    }

    public byte[] getBackupPayload(int userId) {
        com.android.server.people.data.UserData userData = getUnlockedUserData(userId);
        if (userData == null) {
            return null;
        }
        return userData.getBackupPayload();
    }

    public void restore(int userId, byte[] payload) throws java.io.IOException {
        com.android.server.people.data.UserData userData = getUnlockedUserData(userId);
        if (userData == null) {
            return;
        }
        userData.restore(payload);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: setupUser, reason: merged with bridge method [inline-methods] */
    public void lambda$onUserUnlocked$0(int i) {
        synchronized (this.mLock) {
            com.android.server.people.data.UserData unlockedUserData = getUnlockedUserData(i);
            if (unlockedUserData == null) {
                return;
            }
            unlockedUserData.loadUserData();
            updateDefaultDialer(unlockedUserData);
            updateDefaultSmsApp(unlockedUserData);
            java.lang.Object[] objArr = 0;
            java.lang.Object[] objArr2 = 0;
            java.lang.Object[] objArr3 = 0;
            java.lang.Object[] objArr4 = 0;
            java.lang.Object[] objArr5 = 0;
            java.lang.Object[] objArr6 = 0;
            this.mUsageStatsQueryFutures.put(i, this.mScheduledExecutor.scheduleAtFixedRate(new com.android.server.people.data.DataManager.UsageStatsQueryRunnable(i), 1L, 120L, java.util.concurrent.TimeUnit.SECONDS));
            android.content.IntentFilter intentFilter = new android.content.IntentFilter();
            intentFilter.addAction("android.telecom.action.DEFAULT_DIALER_CHANGED");
            intentFilter.addAction("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL");
            if (this.mBroadcastReceivers.get(i) == null) {
                com.android.server.people.data.DataManager.PerUserBroadcastReceiver perUserBroadcastReceiver = new com.android.server.people.data.DataManager.PerUserBroadcastReceiver(i);
                this.mBroadcastReceivers.put(i, perUserBroadcastReceiver);
                this.mContext.registerReceiverAsUser(perUserBroadcastReceiver, android.os.UserHandle.of(i), intentFilter, null, null);
            }
            com.android.server.people.data.DataManager.ContactsContentObserver contactsContentObserver = new com.android.server.people.data.DataManager.ContactsContentObserver(com.android.internal.os.BackgroundThread.getHandler());
            this.mContactsContentObservers.put(i, contactsContentObserver);
            this.mContext.getContentResolver().registerContentObserver(android.provider.ContactsContract.Contacts.CONTENT_URI, true, contactsContentObserver, i);
            com.android.server.people.data.DataManager.NotificationListener notificationListener = new com.android.server.people.data.DataManager.NotificationListener(i);
            this.mNotificationListeners.put(i, notificationListener);
            try {
                notificationListener.registerAsSystemService(this.mContext, new android.content.ComponentName(this.mContext, getClass()), i);
            } catch (android.os.RemoteException e) {
            }
            if (this.mPackageMonitors.get(i) == null) {
                com.android.server.people.data.DataManager.PerUserPackageMonitor perUserPackageMonitor = new com.android.server.people.data.DataManager.PerUserPackageMonitor();
                perUserPackageMonitor.register(this.mContext, (android.os.Looper) null, android.os.UserHandle.of(i), true);
                this.mPackageMonitors.put(i, perUserPackageMonitor);
            }
            if (i == 0) {
                this.mCallLogContentObserver = new com.android.server.people.data.DataManager.CallLogContentObserver(com.android.internal.os.BackgroundThread.getHandler());
                this.mContext.getContentResolver().registerContentObserver(android.provider.CallLog.CONTENT_URI, true, this.mCallLogContentObserver, 0);
                this.mMmsSmsContentObserver = new com.android.server.people.data.DataManager.MmsSmsContentObserver(com.android.internal.os.BackgroundThread.getHandler());
                this.mContext.getContentResolver().registerContentObserver(android.provider.Telephony.MmsSms.CONTENT_URI, false, this.mMmsSmsContentObserver, 0);
            }
            com.android.server.people.data.DataMaintenanceService.scheduleJob(this.mContext, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: cleanupUser, reason: merged with bridge method [inline-methods] */
    public void lambda$onUserStopping$1(int userId) {
        synchronized (this.mLock) {
            com.android.server.people.data.UserData userData = this.mUserDataArray.get(userId);
            if (userData != null && !userData.isUnlocked()) {
                android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
                if (this.mUsageStatsQueryFutures.indexOfKey(userId) >= 0) {
                    this.mUsageStatsQueryFutures.get(userId).cancel(true);
                }
                if (this.mBroadcastReceivers.indexOfKey(userId) >= 0) {
                    this.mContext.unregisterReceiver(this.mBroadcastReceivers.get(userId));
                }
                if (this.mContactsContentObservers.indexOfKey(userId) >= 0) {
                    contentResolver.unregisterContentObserver(this.mContactsContentObservers.get(userId));
                }
                if (this.mNotificationListeners.indexOfKey(userId) >= 0) {
                    try {
                        this.mNotificationListeners.get(userId).unregisterAsSystemService();
                    } catch (android.os.RemoteException e) {
                    }
                }
                if (this.mPackageMonitors.indexOfKey(userId) >= 0) {
                    this.mPackageMonitors.get(userId).unregister();
                }
                if (userId == 0) {
                    if (this.mCallLogContentObserver != null) {
                        contentResolver.unregisterContentObserver(this.mCallLogContentObserver);
                        this.mCallLogContentObserver = null;
                    }
                    if (this.mMmsSmsContentObserver != null) {
                        contentResolver.unregisterContentObserver(this.mMmsSmsContentObserver);
                        this.mCallLogContentObserver = null;
                    }
                }
                com.android.server.people.data.DataMaintenanceService.cancelJob(this.mContext, userId);
            }
        }
    }

    public int mimeTypeToShareEventType(java.lang.String mimeType) {
        if (mimeType == null) {
            return 7;
        }
        if (mimeType.startsWith("text/")) {
            return 4;
        }
        if (mimeType.startsWith("image/")) {
            return 5;
        }
        if (!mimeType.startsWith("video/")) {
            return 7;
        }
        return 6;
    }

    private void pruneUninstalledPackageData(com.android.server.people.data.UserData userData) {
        final java.util.Set<java.lang.String> installApps = new android.util.ArraySet<>();
        this.mPackageManagerInternal.forEachInstalledPackage(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                installApps.add(((com.android.server.pm.pkg.AndroidPackage) obj).getPackageName());
            }
        }, userData.getUserId());
        final java.util.List<java.lang.String> packagesToDelete = new java.util.ArrayList<>();
        userData.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.people.data.DataManager.lambda$pruneUninstalledPackageData$11(installApps, packagesToDelete, (com.android.server.people.data.PackageData) obj);
            }
        });
        for (java.lang.String packageName : packagesToDelete) {
            userData.deletePackageData(packageName);
        }
    }

    static /* synthetic */ void lambda$pruneUninstalledPackageData$11(java.util.Set installApps, java.util.List packagesToDelete, com.android.server.people.data.PackageData packageData) {
        if (!installApps.contains(packageData.getPackageName())) {
            packagesToDelete.add(packageData.getPackageName());
        }
    }

    private java.util.List<android.content.pm.ShortcutInfo> getShortcuts(java.lang.String packageName, int userId, java.util.List<java.lang.String> shortcutIds) {
        return this.mShortcutServiceInternal.getShortcuts(0, this.mContext.getPackageName(), 0L, packageName, shortcutIds, (java.util.List) null, (android.content.ComponentName) null, 3091, userId, android.os.Process.myPid(), android.os.Process.myUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forAllUnlockedUsers(java.util.function.Consumer<com.android.server.people.data.UserData> consumer) {
        for (int i = 0; i < this.mUserDataArray.size(); i++) {
            int userId = this.mUserDataArray.keyAt(i);
            com.android.server.people.data.UserData userData = this.mUserDataArray.get(userId);
            if (userData.isUnlocked()) {
                consumer.accept(userData);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.people.data.UserData getUnlockedUserData(int userId) {
        com.android.server.people.data.UserData userData = this.mUserDataArray.get(userId);
        if (userData == null || !userData.isUnlocked()) {
            return null;
        }
        return userData;
    }

    private void updateDefaultDialer(com.android.server.people.data.UserData userData) {
        java.lang.String defaultDialer;
        android.telecom.TelecomManager telecomManager = (android.telecom.TelecomManager) this.mContext.getSystemService(android.telecom.TelecomManager.class);
        if (telecomManager != null) {
            defaultDialer = telecomManager.getDefaultDialerPackage(new android.os.UserHandle(userData.getUserId()));
        } else {
            defaultDialer = null;
        }
        userData.setDefaultDialer(defaultDialer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDefaultSmsApp(com.android.server.people.data.UserData userData) {
        android.content.ComponentName component = com.android.internal.telephony.SmsApplication.getDefaultSmsApplicationAsUser(this.mContext, false, android.os.UserHandle.of(userData.getUserId()));
        java.lang.String defaultSmsApp = component != null ? component.getPackageName() : null;
        userData.setDefaultSmsApp(defaultSmsApp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.people.data.PackageData getPackageIfConversationExists(android.service.notification.StatusBarNotification sbn, java.util.function.Consumer<com.android.server.people.data.ConversationInfo> conversationConsumer) {
        com.android.server.people.data.PackageData packageData;
        com.android.server.people.data.ConversationInfo conversationInfo;
        android.app.Notification notification = sbn.getNotification();
        java.lang.String shortcutId = notification.getShortcutId();
        if (shortcutId == null || (packageData = getPackage(sbn.getPackageName(), sbn.getUser().getIdentifier())) == null || (conversationInfo = packageData.getConversationStore().getConversation(shortcutId)) == null) {
            return null;
        }
        conversationConsumer.accept(conversationInfo);
        return packageData;
    }

    private boolean isCachedRecentConversation(com.android.server.people.data.ConversationInfo conversationInfo) {
        return isEligibleForCleanUp(conversationInfo) && conversationInfo.getLastEventTimestamp() > 0;
    }

    private boolean isEligibleForCleanUp(com.android.server.people.data.ConversationInfo conversationInfo) {
        return conversationInfo.isShortcutCachedForNotification() && java.util.Objects.equals(conversationInfo.getNotificationChannelId(), conversationInfo.getParentNotificationChannelId());
    }

    private boolean hasActiveNotifications(java.lang.String packageName, int userId, java.lang.String shortcutId) {
        com.android.server.people.data.DataManager.NotificationListener notificationListener = this.mNotificationListeners.get(userId);
        return notificationListener != null && notificationListener.hasActiveNotifications(packageName, shortcutId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupCachedShortcuts(int userId, int targetCachedCount) {
        com.android.server.people.data.UserData userData = getUnlockedUserData(userId);
        if (userData == null) {
            return;
        }
        final java.util.List<android.util.Pair<java.lang.String, com.android.server.people.data.ConversationInfo>> cachedConvos = new java.util.ArrayList<>();
        userData.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$cleanupCachedShortcuts$13(cachedConvos, (com.android.server.people.data.PackageData) obj);
            }
        });
        if (cachedConvos.size() <= targetCachedCount) {
            return;
        }
        int numToUncache = cachedConvos.size() - targetCachedCount;
        java.util.PriorityQueue<android.util.Pair<java.lang.String, com.android.server.people.data.ConversationInfo>> maxHeap = new java.util.PriorityQueue<>(numToUncache + 1, java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda12
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(java.lang.Object obj) {
                android.util.Pair pair = (android.util.Pair) obj;
                return java.lang.Math.max(((com.android.server.people.data.ConversationInfo) pair.second).getLastEventTimestamp(), ((com.android.server.people.data.ConversationInfo) pair.second).getCreationTimestamp());
            }
        }).reversed());
        for (android.util.Pair<java.lang.String, com.android.server.people.data.ConversationInfo> cached : cachedConvos) {
            if (!hasActiveNotifications((java.lang.String) cached.first, userId, ((com.android.server.people.data.ConversationInfo) cached.second).getShortcutId())) {
                maxHeap.offer(cached);
                if (maxHeap.size() > numToUncache) {
                    maxHeap.poll();
                }
            }
        }
        while (!maxHeap.isEmpty()) {
            android.util.Pair<java.lang.String, com.android.server.people.data.ConversationInfo> toUncache = maxHeap.poll();
            this.mShortcutServiceInternal.uncacheShortcuts(userId, this.mContext.getPackageName(), (java.lang.String) toUncache.first, java.util.Collections.singletonList(((com.android.server.people.data.ConversationInfo) toUncache.second).getShortcutId()), userId, 16384);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupCachedShortcuts$13(final java.util.List cachedConvos, final com.android.server.people.data.PackageData packageData) {
        packageData.forAllConversations(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$cleanupCachedShortcuts$12(cachedConvos, packageData, (com.android.server.people.data.ConversationInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupCachedShortcuts$12(java.util.List cachedConvos, com.android.server.people.data.PackageData packageData, com.android.server.people.data.ConversationInfo conversationInfo) {
        if (isEligibleForCleanUp(conversationInfo)) {
            cachedConvos.add(android.util.Pair.create(packageData.getPackageName(), conversationInfo));
        }
    }

    void addOrUpdateConversationInfo(android.content.pm.ShortcutInfo shortcutInfo) {
        com.android.server.people.data.ConversationInfo.Builder builder;
        com.android.server.people.data.UserData userData = getUnlockedUserData(shortcutInfo.getUserId());
        if (userData == null) {
            return;
        }
        com.android.server.people.data.PackageData packageData = userData.getOrCreatePackageData(shortcutInfo.getPackage());
        com.android.server.people.data.ConversationStore conversationStore = packageData.getConversationStore();
        com.android.server.people.data.ConversationInfo oldConversationInfo = conversationStore.getConversation(shortcutInfo.getId());
        if (oldConversationInfo != null) {
            builder = new com.android.server.people.data.ConversationInfo.Builder(oldConversationInfo);
        } else {
            builder = new com.android.server.people.data.ConversationInfo.Builder().setCreationTimestamp(java.lang.System.currentTimeMillis());
        }
        builder.setShortcutId(shortcutInfo.getId());
        builder.setLocusId(shortcutInfo.getLocusId());
        builder.setShortcutFlags(shortcutInfo.getFlags());
        builder.setContactUri(null);
        builder.setContactPhoneNumber(null);
        builder.setContactStarred(false);
        if (shortcutInfo.getPersons() != null && shortcutInfo.getPersons().length != 0) {
            android.app.Person person = shortcutInfo.getPersons()[0];
            builder.setPersonImportant(person.isImportant());
            builder.setPersonBot(person.isBot());
            java.lang.String contactUri = person.getUri();
            if (contactUri != null) {
                com.android.server.people.data.ContactsQueryHelper helper = this.mInjector.createContactsQueryHelper(this.mContext);
                if (helper.query(contactUri)) {
                    builder.setContactUri(helper.getContactUri());
                    builder.setContactStarred(helper.isStarred());
                    builder.setContactPhoneNumber(helper.getPhoneNumber());
                }
            }
        }
        updateConversationStoreThenNotifyListeners(conversationStore, builder.build(), shortcutInfo);
    }

    android.database.ContentObserver getContactsContentObserverForTesting(int userId) {
        return this.mContactsContentObservers.get(userId);
    }

    android.database.ContentObserver getCallLogContentObserverForTesting() {
        return this.mCallLogContentObserver;
    }

    android.database.ContentObserver getMmsSmsContentObserverForTesting() {
        return this.mMmsSmsContentObserver;
    }

    com.android.server.people.data.DataManager.NotificationListener getNotificationListenerServiceForTesting(int userId) {
        return this.mNotificationListeners.get(userId);
    }

    com.android.internal.content.PackageMonitor getPackageMonitorForTesting(int userId) {
        return this.mPackageMonitors.get(userId);
    }

    com.android.server.people.data.UserData getUserDataForTesting(int userId) {
        return this.mUserDataArray.get(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ContactsContentObserver extends android.database.ContentObserver {
        private long mLastUpdatedTimestamp;

        private ContactsContentObserver(android.os.Handler handler) {
            super(handler);
            this.mLastUpdatedTimestamp = java.lang.System.currentTimeMillis();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            com.android.server.people.data.ContactsQueryHelper helper = com.android.server.people.data.DataManager.this.mInjector.createContactsQueryHelper(com.android.server.people.data.DataManager.this.mContext);
            if (!helper.querySince(this.mLastUpdatedTimestamp)) {
                return;
            }
            final android.net.Uri contactUri = helper.getContactUri();
            final com.android.server.people.data.DataManager.ContactsContentObserver.ConversationSelector conversationSelector = new com.android.server.people.data.DataManager.ContactsContentObserver.ConversationSelector();
            com.android.server.people.data.UserData userData = com.android.server.people.data.DataManager.this.getUnlockedUserData(userId);
            if (userData == null) {
                return;
            }
            userData.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$ContactsContentObserver$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.people.data.DataManager.ContactsContentObserver.lambda$onChange$0(contactUri, conversationSelector, (com.android.server.people.data.PackageData) obj);
                }
            });
            if (conversationSelector.mConversationInfo == null) {
                return;
            }
            com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder(conversationSelector.mConversationInfo);
            builder.setContactStarred(helper.isStarred());
            builder.setContactPhoneNumber(helper.getPhoneNumber());
            com.android.server.people.data.DataManager.this.updateConversationStoreThenNotifyListeners(conversationSelector.mConversationStore, builder.build(), conversationSelector.mPackageName, userId);
            this.mLastUpdatedTimestamp = helper.getLastUpdatedTimestamp();
        }

        static /* synthetic */ void lambda$onChange$0(android.net.Uri contactUri, com.android.server.people.data.DataManager.ContactsContentObserver.ConversationSelector conversationSelector, com.android.server.people.data.PackageData packageData) {
            com.android.server.people.data.ConversationInfo ci = packageData.getConversationStore().getConversationByContactUri(contactUri);
            if (ci != null) {
                conversationSelector.mConversationStore = packageData.getConversationStore();
                conversationSelector.mConversationInfo = ci;
                conversationSelector.mPackageName = packageData.getPackageName();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        class ConversationSelector {
            private com.android.server.people.data.ConversationInfo mConversationInfo;
            private com.android.server.people.data.ConversationStore mConversationStore;
            private java.lang.String mPackageName;

            private ConversationSelector() {
                this.mConversationStore = null;
                this.mConversationInfo = null;
                this.mPackageName = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class CallLogContentObserver extends android.database.ContentObserver implements java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> {
        private final com.android.server.people.data.CallLogQueryHelper mCallLogQueryHelper;
        private long mLastCallTimestamp;

        private CallLogContentObserver(android.os.Handler handler) {
            super(handler);
            this.mCallLogQueryHelper = com.android.server.people.data.DataManager.this.mInjector.createCallLogQueryHelper(com.android.server.people.data.DataManager.this.mContext, this);
            this.mLastCallTimestamp = java.lang.System.currentTimeMillis() - 300000;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            if (this.mCallLogQueryHelper.querySince(this.mLastCallTimestamp)) {
                this.mLastCallTimestamp = this.mCallLogQueryHelper.getLastCallTimestamp();
            }
        }

        @Override // java.util.function.BiConsumer
        public void accept(final java.lang.String phoneNumber, final com.android.server.people.data.Event event) {
            com.android.server.people.data.DataManager.this.forAllUnlockedUsers(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$CallLogContentObserver$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.people.data.DataManager.CallLogContentObserver.lambda$accept$0(phoneNumber, event, (com.android.server.people.data.UserData) obj);
                }
            });
        }

        static /* synthetic */ void lambda$accept$0(java.lang.String phoneNumber, com.android.server.people.data.Event event, com.android.server.people.data.UserData userData) {
            com.android.server.people.data.PackageData defaultDialer = userData.getDefaultDialer();
            if (defaultDialer == null) {
                return;
            }
            com.android.server.people.data.ConversationStore conversationStore = defaultDialer.getConversationStore();
            if (conversationStore.getConversationByPhoneNumber(phoneNumber) == null) {
                return;
            }
            com.android.server.people.data.EventStore eventStore = defaultDialer.getEventStore();
            eventStore.getOrCreateEventHistory(2, phoneNumber).addEvent(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class MmsSmsContentObserver extends android.database.ContentObserver implements java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> {
        private boolean isAgingVersion;
        private long mLastMmsTimestamp;
        private long mLastSmsTimestamp;
        private final com.android.server.people.data.MmsQueryHelper mMmsQueryHelper;
        private final com.android.server.people.data.SmsQueryHelper mSmsQueryHelper;

        private MmsSmsContentObserver(android.os.Handler handler) {
            super(handler);
            this.isAgingVersion = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
            this.mMmsQueryHelper = com.android.server.people.data.DataManager.this.mInjector.createMmsQueryHelper(com.android.server.people.data.DataManager.this.mContext, this);
            this.mSmsQueryHelper = com.android.server.people.data.DataManager.this.mInjector.createSmsQueryHelper(com.android.server.people.data.DataManager.this.mContext, this);
            long jCurrentTimeMillis = java.lang.System.currentTimeMillis() - 300000;
            this.mLastMmsTimestamp = jCurrentTimeMillis;
            this.mLastSmsTimestamp = jCurrentTimeMillis;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            if (this.isAgingVersion) {
                return;
            }
            if (this.mMmsQueryHelper.querySince(this.mLastMmsTimestamp)) {
                this.mLastMmsTimestamp = this.mMmsQueryHelper.getLastMessageTimestamp();
            }
            if (this.mSmsQueryHelper.querySince(this.mLastSmsTimestamp)) {
                this.mLastSmsTimestamp = this.mSmsQueryHelper.getLastMessageTimestamp();
            }
        }

        @Override // java.util.function.BiConsumer
        public void accept(final java.lang.String phoneNumber, final com.android.server.people.data.Event event) {
            com.android.server.people.data.DataManager.this.forAllUnlockedUsers(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$MmsSmsContentObserver$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.people.data.DataManager.MmsSmsContentObserver.lambda$accept$0(phoneNumber, event, (com.android.server.people.data.UserData) obj);
                }
            });
        }

        static /* synthetic */ void lambda$accept$0(java.lang.String phoneNumber, com.android.server.people.data.Event event, com.android.server.people.data.UserData userData) {
            com.android.server.people.data.PackageData defaultSmsApp = userData.getDefaultSmsApp();
            if (defaultSmsApp == null) {
                return;
            }
            com.android.server.people.data.ConversationStore conversationStore = defaultSmsApp.getConversationStore();
            if (conversationStore.getConversationByPhoneNumber(phoneNumber) == null) {
                return;
            }
            com.android.server.people.data.EventStore eventStore = defaultSmsApp.getEventStore();
            eventStore.getOrCreateEventHistory(3, phoneNumber).addEvent(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ShortcutServiceCallback implements android.content.pm.LauncherApps.ShortcutChangeCallback {
        private ShortcutServiceCallback() {
        }

        public void onShortcutsAddedOrUpdated(final java.lang.String packageName, final java.util.List<android.content.pm.ShortcutInfo> shortcuts, final android.os.UserHandle user) {
            com.android.server.people.data.DataManager.this.mInjector.getBackgroundExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.people.data.DataManager$ShortcutServiceCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onShortcutsAddedOrUpdated$0(packageName, user, shortcuts);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShortcutsAddedOrUpdated$0(java.lang.String packageName, android.os.UserHandle user, java.util.List shortcuts) {
            com.android.server.people.data.PackageData packageData = com.android.server.people.data.DataManager.this.getPackage(packageName, user.getIdentifier());
            boolean hasCachedShortcut = false;
            java.util.Iterator it = shortcuts.iterator();
            while (it.hasNext()) {
                android.content.pm.ShortcutInfo shortcut = (android.content.pm.ShortcutInfo) it.next();
                if (com.android.server.notification.ShortcutHelper.isConversationShortcut(shortcut, com.android.server.people.data.DataManager.this.mShortcutServiceInternal, user.getIdentifier())) {
                    if (shortcut.isCached()) {
                        com.android.server.people.data.ConversationInfo conversationInfo = packageData != null ? packageData.getConversationInfo(shortcut.getId()) : null;
                        if (conversationInfo == null || !conversationInfo.isShortcutCachedForNotification()) {
                            hasCachedShortcut = true;
                        }
                    }
                    com.android.server.people.data.DataManager.this.addOrUpdateConversationInfo(shortcut);
                }
            }
            if (hasCachedShortcut) {
                com.android.server.people.data.DataManager.this.cleanupCachedShortcuts(user.getIdentifier(), 30);
            }
        }

        public void onShortcutsRemoved(final java.lang.String packageName, final java.util.List<android.content.pm.ShortcutInfo> shortcuts, final android.os.UserHandle user) {
            com.android.server.people.data.DataManager.this.mInjector.getBackgroundExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.people.data.DataManager$ShortcutServiceCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onShortcutsRemoved$1(shortcuts, packageName, user);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShortcutsRemoved$1(java.util.List shortcuts, java.lang.String packageName, android.os.UserHandle user) {
            java.util.HashSet<java.lang.String> shortcutIds = new java.util.HashSet<>();
            java.util.Iterator it = shortcuts.iterator();
            while (it.hasNext()) {
                android.content.pm.ShortcutInfo shortcutInfo = (android.content.pm.ShortcutInfo) it.next();
                shortcutIds.add(shortcutInfo.getId());
            }
            com.android.server.people.data.DataManager.this.removeConversations(packageName, user.getIdentifier(), shortcutIds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeConversations(java.lang.String packageName, int userId, java.util.Set<java.lang.String> shortcutIds) {
        com.android.server.people.data.PackageData packageData = getPackage(packageName, userId);
        if (packageData != null) {
            for (java.lang.String shortcutId : shortcutIds) {
                packageData.deleteDataForConversation(shortcutId);
            }
        }
        try {
            int uid = this.mContext.getPackageManager().getPackageUidAsUser(packageName, userId);
            this.mNotificationManagerInternal.onConversationRemoved(packageName, uid, shortcutIds);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Package not found when removing conversation: " + packageName, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class NotificationListener extends android.service.notification.NotificationListenerService {
        private final java.util.Map<android.util.Pair<java.lang.String, java.lang.String>, java.util.Set<java.lang.String>> mActiveNotifKeys;
        private final int mUserId;

        private NotificationListener(int userId) {
            this.mActiveNotifKeys = new android.util.ArrayMap();
            this.mUserId = userId;
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationPosted(final android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap map) {
            if (sbn.getUser().getIdentifier() != this.mUserId) {
                return;
            }
            final java.lang.String shortcutId = sbn.getNotification().getShortcutId();
            com.android.server.people.data.PackageData packageData = com.android.server.people.data.DataManager.this.getPackageIfConversationExists(sbn, new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$NotificationListener$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onNotificationPosted$1(sbn, shortcutId, (com.android.server.people.data.ConversationInfo) obj);
                }
            });
            if (packageData != null) {
                android.service.notification.NotificationListenerService.Ranking rank = new android.service.notification.NotificationListenerService.Ranking();
                map.getRanking(sbn.getKey(), rank);
                com.android.server.people.data.ConversationInfo conversationInfo = packageData.getConversationInfo(shortcutId);
                if (conversationInfo == null) {
                    return;
                }
                com.android.server.people.data.ConversationInfo.Builder updated = new com.android.server.people.data.ConversationInfo.Builder(conversationInfo).setLastEventTimestamp(sbn.getPostTime()).setNotificationChannelId(rank.getChannel().getId());
                if (!android.text.TextUtils.isEmpty(rank.getChannel().getParentChannelId())) {
                    updated.setParentNotificationChannelId(rank.getChannel().getParentChannelId());
                } else {
                    updated.setParentNotificationChannelId(sbn.getNotification().getChannelId());
                }
                packageData.getConversationStore().addOrUpdate(updated.build());
                com.android.server.people.data.EventHistoryImpl eventHistory = packageData.getEventStore().getOrCreateEventHistory(0, shortcutId);
                eventHistory.addEvent(new com.android.server.people.data.Event(sbn.getPostTime(), 2));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNotificationPosted$1(android.service.notification.StatusBarNotification sbn, java.lang.String shortcutId, com.android.server.people.data.ConversationInfo conversationInfo) {
            synchronized (this) {
                java.util.Set<java.lang.String> notificationKeys = this.mActiveNotifKeys.computeIfAbsent(android.util.Pair.create(sbn.getPackageName(), shortcutId), new java.util.function.Function() { // from class: com.android.server.people.data.DataManager$NotificationListener$$ExternalSyntheticLambda1
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.people.data.DataManager.NotificationListener.lambda$onNotificationPosted$0((android.util.Pair) obj);
                    }
                });
                notificationKeys.add(sbn.getKey());
            }
        }

        static /* synthetic */ java.util.Set lambda$onNotificationPosted$0(android.util.Pair unusedKey) {
            return new java.util.HashSet();
        }

        @Override // android.service.notification.NotificationListenerService
        public synchronized void onNotificationRemoved(final android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap rankingMap, int reason) {
            if (sbn.getUser().getIdentifier() != this.mUserId) {
                return;
            }
            final java.lang.String shortcutId = sbn.getNotification().getShortcutId();
            com.android.server.people.data.PackageData packageData = com.android.server.people.data.DataManager.this.getPackageIfConversationExists(sbn, new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$NotificationListener$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onNotificationRemoved$3(sbn, shortcutId, (com.android.server.people.data.ConversationInfo) obj);
                }
            });
            if (reason == 1 && packageData != null) {
                long currentTime = java.lang.System.currentTimeMillis();
                com.android.server.people.data.ConversationInfo conversationInfo = packageData.getConversationInfo(shortcutId);
                if (conversationInfo == null) {
                    return;
                }
                com.android.server.people.data.ConversationInfo updated = new com.android.server.people.data.ConversationInfo.Builder(conversationInfo).setLastEventTimestamp(currentTime).build();
                packageData.getConversationStore().addOrUpdate(updated);
                com.android.server.people.data.EventHistoryImpl eventHistory = packageData.getEventStore().getOrCreateEventHistory(0, shortcutId);
                eventHistory.addEvent(new com.android.server.people.data.Event(currentTime, 3));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNotificationRemoved$3(android.service.notification.StatusBarNotification sbn, java.lang.String shortcutId, com.android.server.people.data.ConversationInfo conversationInfo) {
            android.util.Pair<java.lang.String, java.lang.String> conversationKey = android.util.Pair.create(sbn.getPackageName(), shortcutId);
            synchronized (this) {
                java.util.Set<java.lang.String> notificationKeys = this.mActiveNotifKeys.computeIfAbsent(conversationKey, new java.util.function.Function() { // from class: com.android.server.people.data.DataManager$NotificationListener$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.people.data.DataManager.NotificationListener.lambda$onNotificationRemoved$2((android.util.Pair) obj);
                    }
                });
                notificationKeys.remove(sbn.getKey());
                if (notificationKeys.isEmpty()) {
                    this.mActiveNotifKeys.remove(conversationKey);
                    com.android.server.people.data.DataManager.this.cleanupCachedShortcuts(this.mUserId, 30);
                }
            }
        }

        static /* synthetic */ java.util.Set lambda$onNotificationRemoved$2(android.util.Pair unusedKey) {
            return new java.util.HashSet();
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationChannelModified(java.lang.String pkg, android.os.UserHandle user, android.app.NotificationChannel channel, int modificationType) {
            com.android.server.people.data.ConversationStore conversationStore;
            com.android.server.people.data.ConversationInfo conversationInfo;
            if (user.getIdentifier() != this.mUserId) {
                return;
            }
            com.android.server.people.data.PackageData packageData = com.android.server.people.data.DataManager.this.getPackage(pkg, user.getIdentifier());
            java.lang.String shortcutId = channel.getConversationId();
            if (packageData == null || shortcutId == null || (conversationInfo = (conversationStore = packageData.getConversationStore()).getConversation(shortcutId)) == null) {
                return;
            }
            com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder(conversationInfo);
            switch (modificationType) {
                case 1:
                case 2:
                    builder.setNotificationChannelId(channel.getId());
                    builder.setImportant(channel.isImportantConversation());
                    builder.setDemoted(channel.isDemoted());
                    builder.setNotificationSilenced(channel.getImportance() <= 2);
                    builder.setBubbled(channel.canBubble());
                    break;
                case 3:
                    builder.setNotificationChannelId(null);
                    builder.setImportant(false);
                    builder.setDemoted(false);
                    builder.setNotificationSilenced(false);
                    builder.setBubbled(false);
                    break;
            }
            com.android.server.people.data.DataManager.this.updateConversationStoreThenNotifyListeners(conversationStore, builder.build(), pkg, packageData.getUserId());
        }

        synchronized boolean hasActiveNotifications(java.lang.String packageName, java.lang.String shortcutId) {
            return this.mActiveNotifKeys.containsKey(android.util.Pair.create(packageName, shortcutId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class UsageStatsQueryRunnable implements java.lang.Runnable, com.android.server.people.data.UsageStatsQueryHelper.EventListener {
        private long mLastEventTimestamp;
        private final com.android.server.people.data.UsageStatsQueryHelper mUsageStatsQueryHelper;

        private UsageStatsQueryRunnable(final int userId) {
            this.mUsageStatsQueryHelper = com.android.server.people.data.DataManager.this.mInjector.createUsageStatsQueryHelper(userId, new java.util.function.Function() { // from class: com.android.server.people.data.DataManager$UsageStatsQueryRunnable$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$new$0(userId, (java.lang.String) obj);
                }
            }, this);
            this.mLastEventTimestamp = java.lang.System.currentTimeMillis() - 300000;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.people.data.PackageData lambda$new$0(int userId, java.lang.String packageName) {
            return com.android.server.people.data.DataManager.this.getPackage(packageName, userId);
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mUsageStatsQueryHelper.querySince(this.mLastEventTimestamp)) {
                this.mLastEventTimestamp = this.mUsageStatsQueryHelper.getLastEventTimestamp();
            }
        }

        @Override // com.android.server.people.data.UsageStatsQueryHelper.EventListener
        public void onEvent(com.android.server.people.data.PackageData packageData, com.android.server.people.data.ConversationInfo conversationInfo, com.android.server.people.data.Event event) {
            if (event.getType() == 13) {
                com.android.server.people.data.ConversationInfo updated = new com.android.server.people.data.ConversationInfo.Builder(conversationInfo).setLastEventTimestamp(event.getTimestamp()).build();
                com.android.server.people.data.DataManager.this.updateConversationStoreThenNotifyListeners(packageData.getConversationStore(), updated, packageData.getPackageName(), packageData.getUserId());
            }
        }
    }

    public void addConversationsListener(com.android.server.people.PeopleService.ConversationsListener listener) {
        synchronized (this.mLock) {
            this.mConversationsListeners.add((com.android.server.people.PeopleService.ConversationsListener) java.util.Objects.requireNonNull(listener));
        }
    }

    void updateConversationStoreThenNotifyListeners(com.android.server.people.data.ConversationStore cs, com.android.server.people.data.ConversationInfo modifiedConv, java.lang.String packageName, int userId) {
        cs.addOrUpdate(modifiedConv);
        android.app.people.ConversationChannel channel = getConversationChannel(packageName, userId, modifiedConv.getShortcutId(), modifiedConv);
        if (channel != null) {
            notifyConversationsListeners(java.util.Arrays.asList(channel));
        }
    }

    private void updateConversationStoreThenNotifyListeners(com.android.server.people.data.ConversationStore cs, com.android.server.people.data.ConversationInfo modifiedConv, android.content.pm.ShortcutInfo shortcutInfo) {
        cs.addOrUpdate(modifiedConv);
        android.app.people.ConversationChannel channel = getConversationChannel(shortcutInfo, modifiedConv, shortcutInfo.getPackage(), shortcutInfo.getUserId(), shortcutInfo.getId());
        if (channel != null) {
            notifyConversationsListeners(java.util.Arrays.asList(channel));
        }
    }

    void notifyConversationsListeners(final java.util.List<android.app.people.ConversationChannel> changedConversations) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.people.data.DataManager$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyConversationsListeners$15(changedConversations);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyConversationsListeners$15(java.util.List changedConversations) {
        java.util.List<com.android.server.people.PeopleService.ConversationsListener> copy;
        try {
            synchronized (this.mLock) {
                copy = new java.util.ArrayList<>(this.mConversationsListeners);
            }
            for (com.android.server.people.PeopleService.ConversationsListener listener : copy) {
                listener.onConversationsUpdate(changedConversations);
            }
        } catch (java.lang.Exception e) {
        }
    }

    private class PerUserBroadcastReceiver extends android.content.BroadcastReceiver {
        private final int mUserId;

        private PerUserBroadcastReceiver(int userId) {
            this.mUserId = userId;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.people.data.UserData userData = com.android.server.people.data.DataManager.this.getUnlockedUserData(this.mUserId);
            if (userData == null) {
                return;
            }
            if ("android.telecom.action.DEFAULT_DIALER_CHANGED".equals(intent.getAction())) {
                java.lang.String defaultDialer = intent.getStringExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME");
                userData.setDefaultDialer(defaultDialer);
            } else if ("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL".equals(intent.getAction())) {
                com.android.server.people.data.DataManager.this.updateDefaultSmsApp(userData);
            }
        }
    }

    private class PerUserPackageMonitor extends com.android.internal.content.PackageMonitor {
        private PerUserPackageMonitor() {
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            super.onPackageRemoved(packageName, uid);
            int userId = getChangingUserId();
            com.android.server.people.data.UserData userData = com.android.server.people.data.DataManager.this.getUnlockedUserData(userId);
            if (userData != null) {
                userData.deletePackageData(packageName);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ShutdownBroadcastReceiver extends android.content.BroadcastReceiver {
        private ShutdownBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.people.data.DataManager.this.forAllUnlockedUsers(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$ShutdownBroadcastReceiver$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.people.data.UserData) obj).forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.people.data.DataManager$ShutdownBroadcastReceiver$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj2) {
                            ((com.android.server.people.data.PackageData) obj2).saveToDisk();
                        }
                    });
                }
            });
        }
    }

    static class Injector {
        Injector() {
        }

        java.util.concurrent.ScheduledExecutorService createScheduledExecutor() {
            return java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        }

        java.util.concurrent.Executor getBackgroundExecutor() {
            return com.android.internal.os.BackgroundThread.getExecutor();
        }

        com.android.server.people.data.ContactsQueryHelper createContactsQueryHelper(android.content.Context context) {
            return new com.android.server.people.data.ContactsQueryHelper(context);
        }

        com.android.server.people.data.CallLogQueryHelper createCallLogQueryHelper(android.content.Context context, java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> eventConsumer) {
            return new com.android.server.people.data.CallLogQueryHelper(context, eventConsumer);
        }

        com.android.server.people.data.MmsQueryHelper createMmsQueryHelper(android.content.Context context, java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> eventConsumer) {
            return new com.android.server.people.data.MmsQueryHelper(context, eventConsumer);
        }

        com.android.server.people.data.SmsQueryHelper createSmsQueryHelper(android.content.Context context, java.util.function.BiConsumer<java.lang.String, com.android.server.people.data.Event> eventConsumer) {
            return new com.android.server.people.data.SmsQueryHelper(context, eventConsumer);
        }

        com.android.server.people.data.UsageStatsQueryHelper createUsageStatsQueryHelper(int userId, java.util.function.Function<java.lang.String, com.android.server.people.data.PackageData> packageDataGetter, com.android.server.people.data.UsageStatsQueryHelper.EventListener eventListener) {
            return new com.android.server.people.data.UsageStatsQueryHelper(userId, packageDataGetter, eventListener);
        }
    }
}
