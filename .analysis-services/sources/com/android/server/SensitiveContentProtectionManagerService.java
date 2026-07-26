package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class SensitiveContentProtectionManagerService extends com.android.server.SystemService {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "SensitiveContentProtect";
    private android.util.ArraySet<java.lang.String> mExemptedPackages;
    private com.android.server.SensitiveContentProtectionManagerService.MediaProjectionSession mMediaProjectionSession;
    com.android.server.SensitiveContentProtectionManagerService.NotificationListener mNotificationListener;
    private final com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener mOnWindowRemovedListener;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> mPackagesShowingSensitiveContent;
    private boolean mProjectionActive;
    private final android.media.projection.MediaProjectionManager.Callback mProjectionCallback;
    private android.media.projection.MediaProjectionManager mProjectionManager;
    final java.lang.Object mSensitiveContentProtectionLock;
    private com.android.server.wm.WindowManagerInternal mWindowManager;

    private static class MediaProjectionSession {
        private final boolean mIsExempted;
        private final long mSessionId;
        private final int mUid;
        private final android.util.ArraySet<java.lang.String> mAllSeenNotificationKeys = new android.util.ArraySet<>();
        private final android.util.ArraySet<java.lang.String> mSeenOtpNotificationKeys = new android.util.ArraySet<>();

        MediaProjectionSession(int uid, boolean isExempted, long sessionId) {
            this.mUid = uid;
            this.mIsExempted = isExempted;
            this.mSessionId = sessionId;
        }

        public void logProjectionSessionStart() {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SENSITIVE_CONTENT_MEDIA_PROJECTION_SESSION, this.mSessionId, this.mUid, this.mIsExempted, 1, 2);
        }

        public void logProjectionSessionStop() {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SENSITIVE_CONTENT_MEDIA_PROJECTION_SESSION, this.mSessionId, this.mUid, this.mIsExempted, 2, 2);
        }

        public void logAppNotificationsProtected() {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_SESSION, this.mSessionId, this.mAllSeenNotificationKeys.size(), this.mSeenOtpNotificationKeys.size());
        }

        public void logAppBlocked(int uid) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SENSITIVE_CONTENT_APP_PROTECTION, this.mSessionId, uid, this.mUid, 1);
        }

        public void logAppUnblocked(int uid) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SENSITIVE_CONTENT_APP_PROTECTION, this.mSessionId, uid, this.mUid, 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addSeenNotificationKey(java.lang.String key) {
            this.mAllSeenNotificationKeys.add(key);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addSeenOtpNotificationKey(java.lang.String key) {
            this.mAllSeenNotificationKeys.add(key);
            this.mSeenOtpNotificationKeys.add(key);
        }

        public void addSeenNotifications(android.service.notification.StatusBarNotification[] notifications, android.service.notification.NotificationListenerService.RankingMap rankingMap) {
            for (android.service.notification.StatusBarNotification sbn : notifications) {
                if (sbn == null) {
                    android.util.Log.w(com.android.server.SensitiveContentProtectionManagerService.TAG, "Unable to parse null notification");
                } else if (com.android.server.SensitiveContentProtectionManagerService.notificationHasSensitiveContent(sbn, rankingMap)) {
                    addSeenOtpNotificationKey(sbn.getKey());
                } else {
                    addSeenNotificationKey(sbn.getKey());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final android.os.IBinder token) {
        synchronized (this.mSensitiveContentProtectionLock) {
            this.mPackagesShowingSensitiveContent.removeIf(new java.util.function.Predicate() { // from class: com.android.server.SensitiveContentProtectionManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.SensitiveContentProtectionManagerService.lambda$new$0(token, (com.android.server.wm.SensitiveContentPackages.PackageInfo) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$new$0(android.os.IBinder token, com.android.server.wm.SensitiveContentPackages.PackageInfo pkgInfo) {
        return pkgInfo.getWindowToken() == token;
    }

    public SensitiveContentProtectionManagerService(android.content.Context context) {
        super(context);
        this.mExemptedPackages = null;
        this.mSensitiveContentProtectionLock = new java.lang.Object();
        this.mPackagesShowingSensitiveContent = new android.util.ArraySet<>();
        this.mProjectionActive = false;
        this.mProjectionCallback = new android.media.projection.MediaProjectionManager.Callback() { // from class: com.android.server.SensitiveContentProtectionManagerService.1
            public void onStart(android.media.projection.MediaProjectionInfo info) {
                android.os.Trace.traceBegin(524288L, "SensitiveContentProtectionManagerService.onProjectionStart");
                try {
                    com.android.server.SensitiveContentProtectionManagerService.this.onProjectionStart(info);
                } finally {
                    android.os.Trace.traceEnd(524288L);
                }
            }

            public void onStop(android.media.projection.MediaProjectionInfo info) {
                android.os.Trace.traceBegin(524288L, "SensitiveContentProtectionManagerService.onProjectionStop");
                try {
                    com.android.server.SensitiveContentProtectionManagerService.this.onProjectionEnd();
                } finally {
                    android.os.Trace.traceEnd(524288L);
                }
            }
        };
        this.mOnWindowRemovedListener = new com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener() { // from class: com.android.server.SensitiveContentProtectionManagerService$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener
            public final void onWindowRemoved(android.os.IBinder iBinder) {
                this.f$0.lambda$new$1(iBinder);
            }
        };
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveNotificationAppProtection()) {
            this.mNotificationListener = new com.android.server.SensitiveContentProtectionManagerService.NotificationListener();
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase != 1000) {
            return;
        }
        init((android.media.projection.MediaProjectionManager) getContext().getSystemService(android.media.projection.MediaProjectionManager.class), (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class), (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class), getExemptedPackages());
        if (android.view.flags.Flags.sensitiveContentAppProtection()) {
            publishBinderService("sensitive_content_protection_service", new com.android.server.SensitiveContentProtectionManagerService.SensitiveContentProtectionManagerServiceBinder());
        }
    }

    void init(android.media.projection.MediaProjectionManager projectionManager, com.android.server.wm.WindowManagerInternal windowManager, android.content.pm.PackageManagerInternal packageManagerInternal, android.util.ArraySet<java.lang.String> exemptedPackages) {
        java.util.Objects.requireNonNull(projectionManager);
        java.util.Objects.requireNonNull(windowManager);
        this.mProjectionManager = projectionManager;
        this.mWindowManager = windowManager;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mExemptedPackages = exemptedPackages;
        this.mProjectionManager.addCallback(this.mProjectionCallback, getContext().getMainThreadHandler());
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveNotificationAppProtection()) {
            try {
                this.mNotificationListener.registerAsSystemService(getContext(), new android.content.ComponentName(getContext(), (java.lang.Class<?>) com.android.server.SensitiveContentProtectionManagerService.NotificationListener.class), -1);
            } catch (android.os.RemoteException e) {
            }
        }
        if (android.view.flags.Flags.sensitiveContentAppProtection()) {
            this.mWindowManager.registerOnWindowRemovedListener(this.mOnWindowRemovedListener);
        }
    }

    void onDestroy() {
        if (this.mProjectionManager != null) {
            this.mProjectionManager.removeCallback(this.mProjectionCallback);
        }
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveNotificationAppProtection()) {
            try {
                this.mNotificationListener.unregisterAsSystemService();
            } catch (android.os.RemoteException e) {
            }
        }
        if (this.mWindowManager != null) {
            onProjectionEnd();
        }
    }

    private boolean canRecordSensitiveContent(java.lang.String packageName) {
        return getContext().getPackageManager().checkPermission("android.permission.RECORD_SENSITIVE_CONTENT", packageName) == 0;
    }

    private android.util.ArraySet<java.lang.String> getExemptedPackages() {
        return com.android.server.SystemConfig.getInstance().getBugreportWhitelistedPackages();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProjectionStart(android.media.projection.MediaProjectionInfo projectionInfo) {
        boolean isPackageExempted = (this.mExemptedPackages != null && this.mExemptedPackages.contains(projectionInfo.getPackageName())) || canRecordSensitiveContent(projectionInfo.getPackageName()) || isAutofillServiceRecorderPackage(projectionInfo.getUserHandle().getIdentifier(), projectionInfo.getPackageName());
        boolean isFeatureDisabled = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "disable_screen_share_protections_for_apps_and_notifications", 0) != 0;
        int uid = this.mPackageManagerInternal.getPackageUid(projectionInfo.getPackageName(), 0L, projectionInfo.getUserHandle().getIdentifier());
        synchronized (this.mSensitiveContentProtectionLock) {
            this.mMediaProjectionSession = new com.android.server.SensitiveContentProtectionManagerService.MediaProjectionSession(uid, isPackageExempted || isFeatureDisabled, new java.util.Random().nextLong());
            this.mMediaProjectionSession.logProjectionSessionStart();
            if (!isPackageExempted && !isFeatureDisabled) {
                this.mProjectionActive = true;
                if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentMetricsBugfix()) {
                    this.mWindowManager.setBlockScreenCaptureForAppsSessionId(this.mMediaProjectionSession.mSessionId);
                }
                if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveNotificationAppProtection()) {
                    updateAppsThatShouldBlockScreenCapture();
                }
                if (android.view.flags.Flags.sensitiveContentAppProtection() && this.mPackagesShowingSensitiveContent.size() > 0) {
                    this.mWindowManager.addBlockScreenCaptureForApps(this.mPackagesShowingSensitiveContent);
                }
                return;
            }
            android.util.Log.w(TAG, "projection session is exempted, package =" + projectionInfo.getPackageName() + ", isFeatureDisabled=" + isFeatureDisabled);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProjectionEnd() {
        synchronized (this.mSensitiveContentProtectionLock) {
            this.mProjectionActive = false;
            if (this.mMediaProjectionSession != null) {
                this.mMediaProjectionSession.logProjectionSessionStop();
                if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentImprovements()) {
                    this.mMediaProjectionSession.logAppNotificationsProtected();
                }
                this.mMediaProjectionSession = null;
            }
            this.mWindowManager.clearBlockedApps();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAppsThatShouldBlockScreenCapture() {
        android.service.notification.NotificationListenerService.RankingMap rankingMap;
        try {
            rankingMap = this.mNotificationListener.getCurrentRanking();
        } catch (java.lang.SecurityException e) {
            android.util.Log.e(TAG, "SensitiveContentProtectionManagerService doesn't have access.", e);
            rankingMap = null;
        }
        if (rankingMap == null) {
            android.util.Log.w(TAG, "Ranking map not initialized.");
        } else {
            updateAppsThatShouldBlockScreenCapture(rankingMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAppsThatShouldBlockScreenCapture(android.service.notification.NotificationListenerService.RankingMap rankingMap) {
        android.service.notification.StatusBarNotification[] notifications;
        try {
            notifications = this.mNotificationListener.getActiveNotifications();
        } catch (java.lang.SecurityException e) {
            android.util.Log.e(TAG, "SensitiveContentProtectionManagerService doesn't have access.", e);
            notifications = new android.service.notification.StatusBarNotification[0];
        }
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentImprovements() && this.mMediaProjectionSession != null) {
            this.mMediaProjectionSession.addSeenNotifications(notifications, rankingMap);
        }
        android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> packageInfos = getSensitivePackagesFromNotifications(notifications, rankingMap);
        if (packageInfos.size() > 0) {
            this.mWindowManager.addBlockScreenCaptureForApps(packageInfos);
        }
    }

    private static android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> getSensitivePackagesFromNotifications(android.service.notification.StatusBarNotification[] notifications, android.service.notification.NotificationListenerService.RankingMap rankingMap) {
        android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> sensitivePackages = new android.util.ArraySet<>();
        for (android.service.notification.StatusBarNotification sbn : notifications) {
            if (sbn == null) {
                android.util.Log.w(TAG, "Unable to parse null notification");
            } else {
                com.android.server.wm.SensitiveContentPackages.PackageInfo info = getSensitivePackageFromNotification(sbn, rankingMap);
                if (info != null) {
                    sensitivePackages.add(info);
                }
            }
        }
        return sensitivePackages;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.wm.SensitiveContentPackages.PackageInfo getSensitivePackageFromNotification(android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap rankingMap) {
        if (notificationHasSensitiveContent(sbn, rankingMap)) {
            return new com.android.server.wm.SensitiveContentPackages.PackageInfo(sbn.getPackageName(), sbn.getUid());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean notificationHasSensitiveContent(android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap rankingMap) {
        android.service.notification.NotificationListenerService.Ranking ranking = rankingMap.getRawRankingObject(sbn.getKey());
        return ranking != null && ranking.hasSensitiveContent();
    }

    class NotificationListener extends android.service.notification.NotificationListenerService {
        NotificationListener() {
        }

        @Override // android.service.notification.NotificationListenerService
        public void onListenerConnected() {
            super.onListenerConnected();
            android.os.Trace.traceBegin(524288L, "SensitiveContentProtectionManagerService.onListenerConnected");
            try {
                synchronized (com.android.server.SensitiveContentProtectionManagerService.this.mSensitiveContentProtectionLock) {
                    if (com.android.server.SensitiveContentProtectionManagerService.this.mProjectionActive) {
                        com.android.server.SensitiveContentProtectionManagerService.this.updateAppsThatShouldBlockScreenCapture();
                    }
                }
            } finally {
                android.os.Trace.traceEnd(524288L);
            }
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationPosted(android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap rankingMap) {
            super.onNotificationPosted(sbn, rankingMap);
            android.os.Trace.traceBegin(524288L, "SensitiveContentProtectionManagerService.onNotificationPosted");
            try {
                if (sbn == null) {
                    android.util.Log.w(com.android.server.SensitiveContentProtectionManagerService.TAG, "Unable to parse null notification");
                    return;
                }
                if (rankingMap == null) {
                    android.util.Log.w(com.android.server.SensitiveContentProtectionManagerService.TAG, "Ranking map not initialized.");
                    return;
                }
                synchronized (com.android.server.SensitiveContentProtectionManagerService.this.mSensitiveContentProtectionLock) {
                    if (com.android.server.SensitiveContentProtectionManagerService.this.mProjectionActive) {
                        com.android.server.wm.SensitiveContentPackages.PackageInfo packageInfo = com.android.server.SensitiveContentProtectionManagerService.getSensitivePackageFromNotification(sbn, rankingMap);
                        if (packageInfo != null) {
                            com.android.server.SensitiveContentProtectionManagerService.this.mWindowManager.addBlockScreenCaptureForApps(new android.util.ArraySet<>(java.util.Set.of(packageInfo)));
                        }
                        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentImprovements() && com.android.server.SensitiveContentProtectionManagerService.this.mMediaProjectionSession != null) {
                            if (packageInfo != null) {
                                com.android.server.SensitiveContentProtectionManagerService.this.mMediaProjectionSession.addSeenOtpNotificationKey(sbn.getKey());
                            } else {
                                com.android.server.SensitiveContentProtectionManagerService.this.mMediaProjectionSession.addSeenNotificationKey(sbn.getKey());
                            }
                        }
                    }
                }
            } finally {
                android.os.Trace.traceEnd(524288L);
            }
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationRankingUpdate(android.service.notification.NotificationListenerService.RankingMap rankingMap) {
            super.onNotificationRankingUpdate(rankingMap);
            android.os.Trace.traceBegin(524288L, "SensitiveContentProtectionManagerService.onNotificationRankingUpdate");
            try {
                if (rankingMap == null) {
                    android.util.Log.w(com.android.server.SensitiveContentProtectionManagerService.TAG, "Ranking map not initialized.");
                    return;
                }
                synchronized (com.android.server.SensitiveContentProtectionManagerService.this.mSensitiveContentProtectionLock) {
                    if (com.android.server.SensitiveContentProtectionManagerService.this.mProjectionActive) {
                        com.android.server.SensitiveContentProtectionManagerService.this.updateAppsThatShouldBlockScreenCapture(rankingMap);
                    }
                }
            } finally {
                android.os.Trace.traceEnd(524288L);
            }
        }
    }

    void setSensitiveContentProtection(android.os.IBinder windowToken, java.lang.String packageName, int uid, boolean isShowingSensitiveContent) {
        synchronized (this.mSensitiveContentProtectionLock) {
            com.android.server.wm.SensitiveContentPackages.PackageInfo packageInfo = new com.android.server.wm.SensitiveContentPackages.PackageInfo(packageName, uid, windowToken);
            if (isShowingSensitiveContent) {
                this.mPackagesShowingSensitiveContent.add(packageInfo);
                if (this.mPackagesShowingSensitiveContent.size() > 100) {
                    android.util.Log.w(TAG, "Unexpectedly large number of sensitive windows, count: " + this.mPackagesShowingSensitiveContent.size());
                }
            } else {
                this.mPackagesShowingSensitiveContent.remove(packageInfo);
            }
            if (this.mProjectionActive) {
                android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> packageInfos = new android.util.ArraySet<>();
                packageInfos.add(packageInfo);
                if (isShowingSensitiveContent) {
                    this.mWindowManager.addBlockScreenCaptureForApps(packageInfos);
                    if (this.mMediaProjectionSession != null) {
                        this.mMediaProjectionSession.logAppBlocked(uid);
                    }
                } else {
                    this.mWindowManager.removeBlockScreenCaptureForApps(packageInfos);
                    if (this.mMediaProjectionSession != null) {
                        this.mMediaProjectionSession.logAppUnblocked(uid);
                    }
                }
            }
        }
    }

    private boolean isAutofillServiceRecorderPackage(int userId, java.lang.String projectionPackage) {
        android.content.ComponentName serviceComponent;
        java.lang.String autofillServicePackage;
        java.lang.String autofillServiceName = android.provider.Settings.Secure.getStringForUser(getContext().getContentResolver(), "autofill_service", userId);
        return (autofillServiceName == null || (serviceComponent = android.content.ComponentName.unflattenFromString(autofillServiceName)) == null || (autofillServicePackage = serviceComponent.getPackageName()) == null || !autofillServicePackage.equals(projectionPackage)) ? false : true;
    }

    private final class SensitiveContentProtectionManagerServiceBinder extends android.view.ISensitiveContentProtectionManager.Stub {
        private SensitiveContentProtectionManagerServiceBinder() {
        }

        public void setSensitiveContentProtection(android.os.IBinder windowToken, java.lang.String packageName, boolean isShowingSensitiveContent) {
            android.os.Trace.traceBegin(524288L, "SensitiveContentProtectionManagerService.setSensitiveContentProtection");
            try {
                int callingUid = android.os.Binder.getCallingUid();
                verifyCallingPackage(callingUid, packageName);
                long identity = android.os.Binder.clearCallingIdentity();
                if (isShowingSensitiveContent) {
                    try {
                        if (com.android.server.SensitiveContentProtectionManagerService.this.mWindowManager.getWindowName(windowToken) == null) {
                            android.util.Log.e(com.android.server.SensitiveContentProtectionManagerService.TAG, "window token is not know to WMS, can't apply protection, token: " + windowToken + ", package: " + packageName);
                            return;
                        }
                    } finally {
                        android.os.Binder.restoreCallingIdentity(identity);
                    }
                }
                com.android.server.SensitiveContentProtectionManagerService.this.setSensitiveContentProtection(windowToken, packageName, callingUid, isShowingSensitiveContent);
            } finally {
                android.os.Trace.traceEnd(524288L);
            }
        }

        private void verifyCallingPackage(int callingUid, java.lang.String callingPackage) {
            if (com.android.server.SensitiveContentProtectionManagerService.this.mPackageManagerInternal.getPackageUid(callingPackage, 0L, android.os.UserHandle.getUserId(callingUid)) != callingUid) {
                throw new java.lang.SecurityException("Specified calling package [" + callingPackage + "] does not match the calling uid " + callingUid);
            }
        }
    }
}
