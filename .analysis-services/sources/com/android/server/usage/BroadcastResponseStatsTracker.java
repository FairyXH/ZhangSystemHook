package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
class BroadcastResponseStatsTracker {
    static final int NOTIFICATION_EVENT_TYPE_CANCELLED = 2;
    static final int NOTIFICATION_EVENT_TYPE_POSTED = 0;
    static final int NOTIFICATION_EVENT_TYPE_UPDATED = 1;
    static final java.lang.String TAG = "ResponseStatsTracker";
    private com.android.server.usage.AppStandbyInternal mAppStandby;
    private final android.content.Context mContext;
    private android.app.role.RoleManager mRoleManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private android.util.SparseArray<com.android.server.usage.UserBroadcastEvents> mUserBroadcastEvents = new android.util.SparseArray<>();
    private android.util.SparseArray<android.util.SparseArray<com.android.server.usage.UserBroadcastResponseStats>> mUserResponseStats = new android.util.SparseArray<>();
    private android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>>> mExemptedRoleHoldersCache = new android.util.SparseArray<>();
    private final android.app.role.OnRoleHoldersChangedListener mRoleHoldersChangedListener = new android.app.role.OnRoleHoldersChangedListener() { // from class: com.android.server.usage.BroadcastResponseStatsTracker$$ExternalSyntheticLambda0
        public final void onRoleHoldersChanged(java.lang.String str, android.os.UserHandle userHandle) {
            this.f$0.onRoleHoldersChanged(str, userHandle);
        }
    };
    private com.android.server.usage.BroadcastResponseStatsLogger mLogger = new com.android.server.usage.BroadcastResponseStatsLogger();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface NotificationEventType {
    }

    BroadcastResponseStatsTracker(com.android.server.usage.AppStandbyInternal appStandby, android.content.Context context) {
        this.mAppStandby = appStandby;
        this.mContext = context;
    }

    void onSystemServicesReady(android.content.Context context) {
        this.mRoleManager = (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
        this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(com.android.internal.os.BackgroundThread.getExecutor(), this.mRoleHoldersChangedListener, android.os.UserHandle.ALL);
    }

    void reportBroadcastDispatchEvent(int sourceUid, java.lang.String targetPackage, android.os.UserHandle targetUser, long idForResponseEvent, long timestampMs, int targetUidProcState) {
        this.mLogger.logBroadcastDispatchEvent(sourceUid, targetPackage, targetUser, idForResponseEvent, timestampMs, targetUidProcState);
        if (targetUidProcState <= this.mAppStandby.getBroadcastResponseFgThresholdState() || doesPackageHoldExemptedRole(targetPackage, targetUser) || doesPackageHoldExemptedPermission(targetPackage, targetUser)) {
            return;
        }
        synchronized (this.mLock) {
            try {
                try {
                    android.util.ArraySet<com.android.server.usage.BroadcastEvent> broadcastEvents = getOrCreateBroadcastEventsLocked(targetPackage, targetUser);
                    com.android.server.usage.BroadcastEvent broadcastEvent = getOrCreateBroadcastEvent(broadcastEvents, sourceUid, targetPackage, targetUser.getIdentifier(), idForResponseEvent);
                    broadcastEvent.addTimestampMs(timestampMs);
                    recordAndPruneOldBroadcastDispatchTimestamps(broadcastEvent);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    void reportNotificationPosted(java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
        reportNotificationEvent(0, packageName, user, timestampMs);
    }

    void reportNotificationUpdated(java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
        reportNotificationEvent(1, packageName, user, timestampMs);
    }

    void reportNotificationCancelled(java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
        reportNotificationEvent(2, packageName, user, timestampMs);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00b6 A[Catch: all -> 0x00c4, TryCatch #0 {, blocks: (B:5:0x0019, B:7:0x001f, B:9:0x0021, B:11:0x003b, B:12:0x004a, B:14:0x0050, B:16:0x0058, B:24:0x006f, B:25:0x007d, B:30:0x008f, B:27:0x0083, B:28:0x0087, B:29:0x008b, B:31:0x00a7, B:32:0x00b0, B:34:0x00b6, B:35:0x00b9, B:36:0x00c2), top: B:41:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00b9 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void reportNotificationEvent(int r24, java.lang.String r25, android.os.UserHandle r26, long r27) {
        /*
            Method dump skipped, instruction units count: 210
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.BroadcastResponseStatsTracker.reportNotificationEvent(int, java.lang.String, android.os.UserHandle, long):void");
    }

    private void recordAndPruneOldBroadcastDispatchTimestamps(com.android.server.usage.BroadcastEvent broadcastEvent) {
        android.util.LongArrayQueue timestampsMs = broadcastEvent.getTimestampsMs();
        long broadcastResponseWindowDurationMs = this.mAppStandby.getBroadcastResponseWindowDurationMs();
        long broadcastsSessionDurationMs = this.mAppStandby.getBroadcastSessionsDurationMs();
        long nowElapsedMs = android.os.SystemClock.elapsedRealtime();
        long broadcastsSessionEndTimestampMs = 0;
        while (timestampsMs.size() > 0 && timestampsMs.peekFirst() < nowElapsedMs - broadcastResponseWindowDurationMs) {
            long eventTimestampMs = timestampsMs.peekFirst();
            if (eventTimestampMs >= broadcastsSessionEndTimestampMs) {
                android.app.usage.BroadcastResponseStats responseStats = getOrCreateBroadcastResponseStats(broadcastEvent);
                responseStats.incrementBroadcastsDispatchedCount(1);
                broadcastsSessionEndTimestampMs = eventTimestampMs + broadcastsSessionDurationMs;
            }
            timestampsMs.removeFirst();
        }
    }

    java.util.List<android.app.usage.BroadcastResponseStats> queryBroadcastResponseStats(int callingUid, java.lang.String packageName, long id, int userId) {
        java.util.List<android.app.usage.BroadcastResponseStats> broadcastResponseStatsList = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            android.util.SparseArray<com.android.server.usage.UserBroadcastResponseStats> responseStatsForCaller = this.mUserResponseStats.get(callingUid);
            if (responseStatsForCaller == null) {
                return broadcastResponseStatsList;
            }
            com.android.server.usage.UserBroadcastResponseStats responseStatsForUser = responseStatsForCaller.get(userId);
            if (responseStatsForUser == null) {
                return broadcastResponseStatsList;
            }
            responseStatsForUser.populateAllBroadcastResponseStats(broadcastResponseStatsList, packageName, id);
            return broadcastResponseStatsList;
        }
    }

    void clearBroadcastResponseStats(int callingUid, java.lang.String packageName, long id, int userId) {
        synchronized (this.mLock) {
            android.util.SparseArray<com.android.server.usage.UserBroadcastResponseStats> responseStatsForCaller = this.mUserResponseStats.get(callingUid);
            if (responseStatsForCaller == null) {
                return;
            }
            com.android.server.usage.UserBroadcastResponseStats responseStatsForUser = responseStatsForCaller.get(userId);
            if (responseStatsForUser == null) {
                return;
            }
            responseStatsForUser.clearBroadcastResponseStats(packageName, id);
        }
    }

    void clearBroadcastEvents(int callingUid, int userId) {
        synchronized (this.mLock) {
            com.android.server.usage.UserBroadcastEvents userBroadcastEvents = this.mUserBroadcastEvents.get(userId);
            if (userBroadcastEvents == null) {
                return;
            }
            userBroadcastEvents.clear(callingUid);
        }
    }

    boolean isPackageExemptedFromBroadcastResponseStats(java.lang.String packageName, android.os.UserHandle user) {
        synchronized (this.mLock) {
            if (doesPackageHoldExemptedPermission(packageName, user)) {
                return true;
            }
            return doesPackageHoldExemptedRole(packageName, user);
        }
    }

    boolean doesPackageHoldExemptedRole(java.lang.String packageName, android.os.UserHandle user) {
        java.util.List<java.lang.String> exemptedRoles = this.mAppStandby.getBroadcastResponseExemptedRoles();
        synchronized (this.mLock) {
            for (int i = exemptedRoles.size() - 1; i >= 0; i--) {
                java.lang.String roleName = exemptedRoles.get(i);
                java.util.List<java.lang.String> roleHolders = getRoleHoldersLocked(roleName, user);
                if (com.android.internal.util.CollectionUtils.contains(roleHolders, packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    boolean doesPackageHoldExemptedPermission(java.lang.String packageName, android.os.UserHandle user) {
        try {
            int uid = this.mContext.getPackageManager().getPackageUidAsUser(packageName, user.getIdentifier());
            java.util.List<java.lang.String> exemptedPermissions = this.mAppStandby.getBroadcastResponseExemptedPermissions();
            for (int i = exemptedPermissions.size() - 1; i >= 0; i--) {
                java.lang.String permissionName = exemptedPermissions.get(i);
                if (this.mContext.checkPermission(permissionName, -1, uid) == 0) {
                    return true;
                }
            }
            return false;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private java.util.List<java.lang.String> getRoleHoldersLocked(java.lang.String roleName, android.os.UserHandle user) {
        android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> roleHoldersForUser = this.mExemptedRoleHoldersCache.get(user.getIdentifier());
        if (roleHoldersForUser == null) {
            roleHoldersForUser = new android.util.ArrayMap<>();
            this.mExemptedRoleHoldersCache.put(user.getIdentifier(), roleHoldersForUser);
        }
        java.util.List<java.lang.String> roleHolders = roleHoldersForUser.get(roleName);
        if (roleHolders == null && this.mRoleManager != null) {
            java.util.List roleHolders2 = this.mRoleManager.getRoleHoldersAsUser(roleName, user);
            roleHoldersForUser.put(roleName, roleHolders2);
            return roleHolders2;
        }
        return roleHolders;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRoleHoldersChanged(java.lang.String roleName, android.os.UserHandle user) {
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> roleHoldersForUser = this.mExemptedRoleHoldersCache.get(user.getIdentifier());
            if (roleHoldersForUser == null) {
                return;
            }
            roleHoldersForUser.remove(roleName);
        }
    }

    void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            this.mUserBroadcastEvents.remove(userId);
            for (int i = this.mUserResponseStats.size() - 1; i >= 0; i--) {
                this.mUserResponseStats.valueAt(i).remove(userId);
            }
            this.mExemptedRoleHoldersCache.remove(userId);
        }
    }

    void onPackageRemoved(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            com.android.server.usage.UserBroadcastEvents userBroadcastEvents = this.mUserBroadcastEvents.get(userId);
            if (userBroadcastEvents != null) {
                userBroadcastEvents.onPackageRemoved(packageName);
            }
            for (int i = this.mUserResponseStats.size() - 1; i >= 0; i--) {
                com.android.server.usage.UserBroadcastResponseStats userResponseStats = this.mUserResponseStats.valueAt(i).get(userId);
                if (userResponseStats != null) {
                    userResponseStats.onPackageRemoved(packageName);
                }
            }
        }
    }

    void onUidRemoved(int uid) {
        synchronized (this.mLock) {
            for (int i = this.mUserBroadcastEvents.size() - 1; i >= 0; i--) {
                this.mUserBroadcastEvents.valueAt(i).onUidRemoved(uid);
            }
            this.mUserResponseStats.remove(uid);
        }
    }

    private android.util.ArraySet<com.android.server.usage.BroadcastEvent> getBroadcastEventsLocked(java.lang.String packageName, android.os.UserHandle user) {
        com.android.server.usage.UserBroadcastEvents userBroadcastEvents = this.mUserBroadcastEvents.get(user.getIdentifier());
        if (userBroadcastEvents == null) {
            return null;
        }
        return userBroadcastEvents.getBroadcastEvents(packageName);
    }

    private android.util.ArraySet<com.android.server.usage.BroadcastEvent> getOrCreateBroadcastEventsLocked(java.lang.String packageName, android.os.UserHandle user) {
        com.android.server.usage.UserBroadcastEvents userBroadcastEvents = this.mUserBroadcastEvents.get(user.getIdentifier());
        if (userBroadcastEvents == null) {
            userBroadcastEvents = new com.android.server.usage.UserBroadcastEvents();
            this.mUserBroadcastEvents.put(user.getIdentifier(), userBroadcastEvents);
        }
        return userBroadcastEvents.getOrCreateBroadcastEvents(packageName);
    }

    private android.app.usage.BroadcastResponseStats getBroadcastResponseStats(android.util.SparseArray<com.android.server.usage.UserBroadcastResponseStats> responseStatsForUid, com.android.server.usage.BroadcastEvent broadcastEvent) {
        com.android.server.usage.UserBroadcastResponseStats userResponseStats;
        if (responseStatsForUid == null || (userResponseStats = responseStatsForUid.get(broadcastEvent.getTargetUserId())) == null) {
            return null;
        }
        return userResponseStats.getBroadcastResponseStats(broadcastEvent);
    }

    private android.app.usage.BroadcastResponseStats getOrCreateBroadcastResponseStats(com.android.server.usage.BroadcastEvent broadcastEvent) {
        int sourceUid = broadcastEvent.getSourceUid();
        android.util.SparseArray<com.android.server.usage.UserBroadcastResponseStats> userResponseStatsForUid = this.mUserResponseStats.get(sourceUid);
        if (userResponseStatsForUid == null) {
            userResponseStatsForUid = new android.util.SparseArray<>();
            this.mUserResponseStats.put(sourceUid, userResponseStatsForUid);
        }
        com.android.server.usage.UserBroadcastResponseStats userResponseStats = userResponseStatsForUid.get(broadcastEvent.getTargetUserId());
        if (userResponseStats == null) {
            userResponseStats = new com.android.server.usage.UserBroadcastResponseStats();
            userResponseStatsForUid.put(broadcastEvent.getTargetUserId(), userResponseStats);
        }
        return userResponseStats.getOrCreateBroadcastResponseStats(broadcastEvent);
    }

    private static com.android.server.usage.BroadcastEvent getOrCreateBroadcastEvent(android.util.ArraySet<com.android.server.usage.BroadcastEvent> broadcastEvents, int sourceUid, java.lang.String targetPackage, int targetUserId, long idForResponseEvent) {
        com.android.server.usage.BroadcastEvent broadcastEvent = new com.android.server.usage.BroadcastEvent(sourceUid, targetPackage, targetUserId, idForResponseEvent);
        int index = broadcastEvents.indexOf(broadcastEvent);
        if (index >= 0) {
            return broadcastEvents.valueAt(index);
        }
        broadcastEvents.add(broadcastEvent);
        return broadcastEvent;
    }

    void dump(com.android.internal.util.IndentingPrintWriter ipw) {
        ipw.println("Broadcast response stats:");
        ipw.increaseIndent();
        synchronized (this.mLock) {
            dumpBroadcastEventsLocked(ipw);
            ipw.println();
            dumpResponseStatsLocked(ipw);
            ipw.println();
            dumpRoleHoldersLocked(ipw);
            ipw.println();
            this.mLogger.dumpLogs(ipw);
        }
        ipw.decreaseIndent();
    }

    private void dumpBroadcastEventsLocked(com.android.internal.util.IndentingPrintWriter ipw) {
        ipw.println("Broadcast events:");
        ipw.increaseIndent();
        for (int i = 0; i < this.mUserBroadcastEvents.size(); i++) {
            int userId = this.mUserBroadcastEvents.keyAt(i);
            com.android.server.usage.UserBroadcastEvents userBroadcastEvents = this.mUserBroadcastEvents.valueAt(i);
            ipw.println("User " + userId + ":");
            ipw.increaseIndent();
            userBroadcastEvents.dump(ipw);
            ipw.decreaseIndent();
        }
        ipw.decreaseIndent();
    }

    private void dumpResponseStatsLocked(com.android.internal.util.IndentingPrintWriter ipw) {
        ipw.println("Response stats:");
        ipw.increaseIndent();
        for (int i = 0; i < this.mUserResponseStats.size(); i++) {
            int sourceUid = this.mUserResponseStats.keyAt(i);
            android.util.SparseArray<com.android.server.usage.UserBroadcastResponseStats> userBroadcastResponseStats = this.mUserResponseStats.valueAt(i);
            ipw.println("Uid " + sourceUid + ":");
            ipw.increaseIndent();
            for (int j = 0; j < userBroadcastResponseStats.size(); j++) {
                int userId = userBroadcastResponseStats.keyAt(j);
                com.android.server.usage.UserBroadcastResponseStats broadcastResponseStats = userBroadcastResponseStats.valueAt(j);
                ipw.println("User " + userId + ":");
                ipw.increaseIndent();
                broadcastResponseStats.dump(ipw);
                ipw.decreaseIndent();
            }
            ipw.decreaseIndent();
        }
        ipw.decreaseIndent();
    }

    private void dumpRoleHoldersLocked(com.android.internal.util.IndentingPrintWriter ipw) {
        ipw.println("Role holders:");
        ipw.increaseIndent();
        for (int userIdx = 0; userIdx < this.mExemptedRoleHoldersCache.size(); userIdx++) {
            int userId = this.mExemptedRoleHoldersCache.keyAt(userIdx);
            android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> roleHoldersForUser = this.mExemptedRoleHoldersCache.valueAt(userIdx);
            ipw.println("User " + userId + ":");
            ipw.increaseIndent();
            for (int roleIdx = 0; roleIdx < roleHoldersForUser.size(); roleIdx++) {
                java.lang.String roleName = roleHoldersForUser.keyAt(roleIdx);
                java.util.List<java.lang.String> holders = roleHoldersForUser.valueAt(roleIdx);
                ipw.print(roleName + ": ");
                for (int holderIdx = 0; holderIdx < holders.size(); holderIdx++) {
                    if (holderIdx > 0) {
                        ipw.print(", ");
                    }
                    ipw.print(holders.get(holderIdx));
                }
                ipw.println();
            }
            ipw.decreaseIndent();
        }
        ipw.decreaseIndent();
    }
}
