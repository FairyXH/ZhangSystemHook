package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationHistoryManager {
    private static final boolean DEBUG = com.android.server.notification.NotificationManagerService.DBG;
    static final java.lang.String DIRECTORY_PER_USER = "notification_history";
    private static final java.lang.String TAG = "NotificationHistory";
    private final android.content.Context mContext;
    final com.android.server.notification.NotificationHistoryManager.SettingsObserver mSettingsObserver;
    private final android.os.UserManager mUserManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.notification.NotificationHistoryDatabase> mUserState = new android.util.SparseArray<>();
    private final android.util.SparseBooleanArray mUserUnlockedStates = new android.util.SparseBooleanArray();
    private final android.util.SparseArray<java.util.List<java.lang.String>> mUserPendingPackageRemovals = new android.util.SparseArray<>();
    private final android.util.SparseBooleanArray mHistoryEnabled = new android.util.SparseBooleanArray();
    private final android.util.SparseBooleanArray mUserPendingHistoryDisables = new android.util.SparseBooleanArray();
    private com.android.server.notification.INotificationHistoryManagerExt mNHM = (com.android.server.notification.INotificationHistoryManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.INotificationHistoryManagerExt.class).base(this).create();

    public NotificationHistoryManager(android.content.Context context, android.os.Handler handler) {
        this.mContext = context;
        this.mUserManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        this.mSettingsObserver = new com.android.server.notification.NotificationHistoryManager.SettingsObserver(handler);
    }

    void onDestroy() {
        this.mSettingsObserver.stopObserving();
    }

    void onBootPhaseAppsCanStart() {
        try {
            com.android.server.notification.NotificationHistoryJobService.scheduleJob(this.mContext);
        } catch (java.lang.Throwable e) {
            android.util.Slog.e(TAG, "Failed to schedule cleanup job", e);
        }
        this.mSettingsObserver.observe();
    }

    void onUserUnlocked(int userId) {
        synchronized (this.mLock) {
            this.mUserUnlockedStates.put(userId, true);
            com.android.server.notification.NotificationHistoryDatabase userHistory = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistory == null) {
                android.util.Slog.i(TAG, "Attempted to unlock gone/disabled user " + userId);
                return;
            }
            java.util.List<java.lang.String> pendingPackageRemovals = this.mUserPendingPackageRemovals.get(userId);
            if (pendingPackageRemovals != null) {
                for (int i = 0; i < pendingPackageRemovals.size(); i++) {
                    userHistory.onPackageRemoved(pendingPackageRemovals.get(i));
                }
                this.mUserPendingPackageRemovals.remove(userId);
            }
            if (this.mUserPendingHistoryDisables.get(userId)) {
                disableHistory(userHistory, userId);
            }
        }
    }

    public void onUserAdded(int userId) {
        this.mSettingsObserver.update(null, userId);
    }

    public void onUserStopped(int userId) {
        synchronized (this.mLock) {
            this.mUserUnlockedStates.put(userId, false);
            this.mUserState.put(userId, null);
        }
    }

    public void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            this.mUserPendingPackageRemovals.remove(userId);
            this.mHistoryEnabled.put(userId, false);
            this.mUserPendingHistoryDisables.put(userId, false);
            onUserStopped(userId);
        }
    }

    public void onPackageRemoved(int userId, java.lang.String packageName) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.get(userId, false)) {
                if (this.mHistoryEnabled.get(userId, false)) {
                    java.util.List<java.lang.String> userPendingRemovals = this.mUserPendingPackageRemovals.get(userId, new java.util.ArrayList());
                    userPendingRemovals.add(packageName);
                    this.mUserPendingPackageRemovals.put(userId, userPendingRemovals);
                }
                return;
            }
            com.android.server.notification.NotificationHistoryDatabase userHistory = this.mUserState.get(userId);
            if (userHistory == null) {
                return;
            }
            userHistory.onPackageRemoved(packageName);
        }
    }

    public void cleanupHistoryFiles() {
        com.android.server.notification.NotificationHistoryDatabase userHistory;
        synchronized (this.mLock) {
            int n = this.mUserUnlockedStates.size();
            for (int i = 0; i < n; i++) {
                if (this.mUserUnlockedStates.valueAt(i) && (userHistory = this.mUserState.get(this.mUserUnlockedStates.keyAt(i))) != null) {
                    userHistory.prune();
                }
            }
        }
    }

    public void deleteNotificationHistoryItem(java.lang.String pkg, int uid, long postedTime) {
        synchronized (this.mLock) {
            int userId = android.os.UserHandle.getUserId(uid);
            com.android.server.notification.NotificationHistoryDatabase userHistory = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistory == null) {
                android.util.Slog.w(TAG, "Attempted to remove notif for locked/gone/disabled user " + userId);
            } else {
                userHistory.deleteNotificationHistoryItem(pkg, postedTime);
            }
        }
    }

    public void deleteConversations(java.lang.String pkg, int uid, java.util.Set<java.lang.String> conversationIds) {
        synchronized (this.mLock) {
            int userId = android.os.UserHandle.getUserId(uid);
            com.android.server.notification.NotificationHistoryDatabase userHistory = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistory == null) {
                android.util.Slog.w(TAG, "Attempted to remove conversation for locked/gone/disabled user " + userId);
            } else {
                userHistory.deleteConversations(pkg, conversationIds);
            }
        }
    }

    public void deleteNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId) {
        synchronized (this.mLock) {
            int userId = android.os.UserHandle.getUserId(uid);
            com.android.server.notification.NotificationHistoryDatabase userHistory = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistory == null) {
                android.util.Slog.w(TAG, "Attempted to remove channel for locked/gone/disabled user " + userId);
            } else {
                userHistory.deleteNotificationChannel(pkg, channelId);
            }
        }
    }

    public void triggerWriteToDisk() {
        com.android.server.notification.NotificationHistoryDatabase userHistory;
        synchronized (this.mLock) {
            int userCount = this.mUserState.size();
            for (int i = 0; i < userCount; i++) {
                int userId = this.mUserState.keyAt(i);
                if (this.mUserUnlockedStates.get(userId) && (userHistory = this.mUserState.get(userId)) != null) {
                    userHistory.forceWriteToDisk();
                }
            }
        }
    }

    public void addNotification(final android.app.NotificationHistory.HistoricalNotification notification) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationHistoryManager$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$addNotification$0(notification);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addNotification$0(android.app.NotificationHistory.HistoricalNotification notification) throws java.lang.Exception {
        synchronized (this.mLock) {
            com.android.server.notification.NotificationHistoryDatabase userHistory = getUserHistoryAndInitializeIfNeededLocked(notification.getUserId());
            if (userHistory == null) {
                android.util.Slog.w(TAG, "Attempted to add notif for locked/gone/disabled user " + notification.getUserId());
            } else {
                userHistory.addNotification(notification);
            }
        }
    }

    public android.app.NotificationHistory readNotificationHistory(int[] userIds) {
        synchronized (this.mLock) {
            android.app.NotificationHistory mergedHistory = new android.app.NotificationHistory();
            if (userIds == null) {
                return mergedHistory;
            }
            for (int userId : userIds) {
                com.android.server.notification.NotificationHistoryDatabase userHistory = getUserHistoryAndInitializeIfNeededLocked(userId);
                if (userHistory == null) {
                    android.util.Slog.i(TAG, "Attempted to read history for locked/gone/disabled user " + userId);
                } else {
                    mergedHistory.addNotificationsToWrite(userHistory.readNotificationHistory());
                }
            }
            return mergedHistory;
        }
    }

    public android.app.NotificationHistory readFilteredNotificationHistory(int userId, java.lang.String packageName, java.lang.String channelId, int maxNotifications) {
        synchronized (this.mLock) {
            com.android.server.notification.NotificationHistoryDatabase userHistory = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistory == null) {
                android.util.Slog.i(TAG, "Attempted to read history for locked/gone/disabled user " + userId);
                return new android.app.NotificationHistory();
            }
            return userHistory.readNotificationHistory(packageName, channelId, maxNotifications);
        }
    }

    boolean isHistoryEnabled(int userId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mHistoryEnabled.get(userId);
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0017 A[Catch: all -> 0x0023, TryCatch #0 {, blocks: (B:5:0x0005, B:6:0x000a, B:9:0x0013, B:15:0x0021, B:10:0x0017, B:14:0x001e), top: B:20:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0011  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void onHistoryEnabledChanged(int r5, boolean r6) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mLock
            monitor-enter(r0)
            if (r6 == 0) goto La
            android.util.SparseBooleanArray r1 = r4.mHistoryEnabled     // Catch: java.lang.Throwable -> L23
            r1.put(r5, r6)     // Catch: java.lang.Throwable -> L23
        La:
            com.android.server.notification.NotificationHistoryDatabase r1 = r4.getUserHistoryAndInitializeIfNeededLocked(r5)     // Catch: java.lang.Throwable -> L23
            if (r1 == 0) goto L17
            if (r6 != 0) goto L21
            r4.disableHistory(r1, r5)     // Catch: java.lang.Throwable -> L23
            goto L21
        L17:
            android.util.SparseBooleanArray r2 = r4.mUserPendingHistoryDisables     // Catch: java.lang.Throwable -> L23
            if (r6 != 0) goto L1d
            r3 = 1
            goto L1e
        L1d:
            r3 = 0
        L1e:
            r2.put(r5, r3)     // Catch: java.lang.Throwable -> L23
        L21:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L23
            return
        L23:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L23
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationHistoryManager.onHistoryEnabledChanged(int, boolean):void");
    }

    private void disableHistory(com.android.server.notification.NotificationHistoryDatabase userHistory, int userId) {
        userHistory.disableHistory();
        this.mUserPendingHistoryDisables.put(userId, false);
        this.mHistoryEnabled.put(userId, false);
        this.mUserState.put(userId, null);
    }

    private com.android.server.notification.NotificationHistoryDatabase getUserHistoryAndInitializeIfNeededLocked(int userId) throws java.lang.Exception {
        if (!this.mHistoryEnabled.get(userId)) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "History disabled for user " + userId);
            }
            this.mUserState.put(userId, null);
            return null;
        }
        com.android.server.notification.NotificationHistoryDatabase userHistory = this.mUserState.get(userId);
        if (userHistory == null) {
            java.io.File historyDir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), DIRECTORY_PER_USER);
            userHistory = com.android.server.notification.NotificationHistoryDatabaseFactory.create(this.mContext, com.android.server.IoThread.getHandler(), historyDir);
            if (this.mUserUnlockedStates.get(userId)) {
                try {
                    userHistory.init();
                    this.mUserState.put(userId, userHistory);
                } catch (java.lang.Exception e) {
                    if (this.mUserManager.isUserUnlocked(userId)) {
                        throw e;
                    }
                    android.util.Slog.w(TAG, "Attempted to initialize service for stopped or removed user " + userId);
                    return null;
                }
            } else {
                android.util.Slog.w(TAG, "Attempted to initialize service for stopped or removed user " + userId);
                return null;
            }
        }
        return userHistory;
    }

    boolean isUserUnlocked(int userId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mUserUnlockedStates.get(userId);
        }
        return z;
    }

    boolean doesHistoryExistForUser(int userId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mUserState.get(userId) != null;
        }
        return z;
    }

    void replaceNotificationHistoryDatabase(int userId, com.android.server.notification.NotificationHistoryDatabase replacement) {
        synchronized (this.mLock) {
            if (this.mUserState.get(userId) != null) {
                this.mUserState.put(userId, replacement);
            }
        }
    }

    java.util.List<java.lang.String> getPendingPackageRemovalsForUser(int userId) {
        java.util.List<java.lang.String> list;
        synchronized (this.mLock) {
            list = this.mUserPendingPackageRemovals.get(userId);
        }
        return list;
    }

    final class SettingsObserver extends android.database.ContentObserver {
        private final android.net.Uri NOTIFICATION_HISTORY_URI;

        SettingsObserver(android.os.Handler handler) {
            super(handler);
            this.NOTIFICATION_HISTORY_URI = android.provider.Settings.Secure.getUriFor("notification_history_enabled");
        }

        void observe() {
            android.content.ContentResolver resolver = com.android.server.notification.NotificationHistoryManager.this.mContext.getContentResolver();
            resolver.registerContentObserver(this.NOTIFICATION_HISTORY_URI, false, this, -1);
            synchronized (com.android.server.notification.NotificationHistoryManager.this.mLock) {
                for (android.content.pm.UserInfo userInfo : com.android.server.notification.NotificationHistoryManager.this.mUserManager.getUsers()) {
                    update(null, userInfo.id);
                }
            }
        }

        void stopObserving() {
            android.content.ContentResolver resolver = com.android.server.notification.NotificationHistoryManager.this.mContext.getContentResolver();
            resolver.unregisterContentObserver(this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            update(uri, userId);
        }

        public void update(android.net.Uri uri, int userId) {
            android.content.ContentResolver resolver = com.android.server.notification.NotificationHistoryManager.this.mContext.getContentResolver();
            if (uri == null || this.NOTIFICATION_HISTORY_URI.equals(uri)) {
                boolean historyEnabled = android.provider.Settings.Secure.getIntForUser(resolver, "notification_history_enabled", 0, userId) != 0;
                int[] profiles = com.android.server.notification.NotificationHistoryManager.this.mUserManager.getProfileIds(userId, true);
                for (int profileId : profiles) {
                    com.android.server.notification.NotificationHistoryManager.this.mNHM.update(profileId, historyEnabled);
                }
                com.android.server.notification.NotificationHistoryManager.this.onHistoryEnabledChanged(userId, historyEnabled);
            }
        }
    }
}
