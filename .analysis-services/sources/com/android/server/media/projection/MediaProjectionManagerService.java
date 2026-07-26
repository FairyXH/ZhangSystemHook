package com.android.server.media.projection;

/* JADX INFO: loaded from: classes2.dex */
public final class MediaProjectionManagerService extends com.android.server.SystemService implements com.android.server.Watchdog.Monitor {
    static final long MEDIA_PROJECTION_PREVENTS_REUSING_CONSENT = 266201607;
    private static final boolean REQUIRE_FG_SERVICE_FOR_PROJECTION = true;
    private static final java.lang.String TAG = "MediaProjectionManagerService";
    private final java.lang.String OPLUSSCREENRECORDER;
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.app.AppOpsManager mAppOps;
    private final com.android.server.media.projection.MediaProjectionManagerService.CallbackDelegate mCallbackDelegate;
    private final com.android.server.media.projection.MediaProjectionManagerService.Clock mClock;
    private final android.content.Context mContext;
    private final java.util.Map<android.os.IBinder, android.os.IBinder.DeathRecipient> mDeathEaters;
    private final android.os.Handler mHandler;
    private final com.android.server.media.projection.MediaProjectionManagerService.Injector mInjector;
    private final java.lang.Object mLock;
    private android.media.projection.IMediaProjectionManagerServiceExt mMediaProjectionManagerServiceExt;
    private final com.android.server.media.projection.MediaProjectionMetricsLogger mMediaProjectionMetricsLogger;
    private android.media.MediaRouter.RouteInfo mMediaRouteInfo;
    private final android.media.MediaRouter mMediaRouter;
    private final com.android.server.media.projection.MediaProjectionManagerService.MediaRouterCallback mMediaRouterCallback;
    private final com.android.server.media.projection.MediaProjectionManagerService.CallbackDelegate mOplusCallbackDelegate;
    private com.android.server.media.projection.MediaProjectionManagerService.MediaProjection mOplusProjectionGrant;
    private android.os.IBinder mOplusProjectionToken;
    private final android.content.pm.PackageManager mPackageManager;
    private com.android.server.media.projection.MediaProjectionManagerService.MediaProjection mProjectionGrant;
    private android.os.IBinder mProjectionToken;
    private final com.android.server.wm.WindowManagerInternal mWmInternal;

    interface Clock {
        long uptimeMillis();
    }

    public MediaProjectionManagerService(android.content.Context context) {
        this(context, new com.android.server.media.projection.MediaProjectionManagerService.Injector());
    }

    MediaProjectionManagerService(android.content.Context context, com.android.server.media.projection.MediaProjectionManagerService.Injector injector) {
        super(context);
        this.mLock = new java.lang.Object();
        this.OPLUSSCREENRECORDER = "com.oplus.screenrecorder";
        this.mContext = context;
        this.mInjector = injector;
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        this.mClock = injector.createClock();
        this.mDeathEaters = new android.util.ArrayMap();
        this.mCallbackDelegate = new com.android.server.media.projection.MediaProjectionManagerService.CallbackDelegate(injector.createCallbackLooper());
        this.mOplusCallbackDelegate = new com.android.server.media.projection.MediaProjectionManagerService.CallbackDelegate(injector.createCallbackLooper());
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService("appops");
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mPackageManager = this.mContext.getPackageManager();
        this.mWmInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        this.mMediaRouter = (android.media.MediaRouter) this.mContext.getSystemService("media_router");
        this.mMediaRouterCallback = new com.android.server.media.projection.MediaProjectionManagerService.MediaRouterCallback();
        this.mMediaProjectionMetricsLogger = injector.mediaProjectionMetricsLogger(context);
        com.android.server.Watchdog.getInstance().addMonitor(this);
        this.mMediaProjectionManagerServiceExt = (android.media.projection.IMediaProjectionManagerServiceExt) system.ext.loader.core.ExtLoader.type(android.media.projection.IMediaProjectionManagerServiceExt.class).create();
    }

    static class Injector {
        Injector() {
        }

        boolean shouldMediaProjectionPreventReusingConsent(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection) {
            return android.app.compat.CompatChanges.isChangeEnabled(com.android.server.media.projection.MediaProjectionManagerService.MEDIA_PROJECTION_PREVENTS_REUSING_CONSENT, projection.packageName, android.os.UserHandle.getUserHandleForUid(projection.uid));
        }

        com.android.server.media.projection.MediaProjectionManagerService.Clock createClock() {
            return new com.android.server.media.projection.MediaProjectionManagerService.Clock() { // from class: com.android.server.media.projection.MediaProjectionManagerService$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.media.projection.MediaProjectionManagerService.Clock
                public final long uptimeMillis() {
                    return android.os.SystemClock.uptimeMillis();
                }
            };
        }

        android.os.Looper createCallbackLooper() {
            return android.os.Looper.getMainLooper();
        }

        com.android.server.media.projection.MediaProjectionMetricsLogger mediaProjectionMetricsLogger(android.content.Context context) {
            return com.android.server.media.projection.MediaProjectionMetricsLogger.getInstance(context);
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("media_projection", new com.android.server.media.projection.MediaProjectionManagerService.BinderService(this.mContext), false);
        this.mMediaRouter.addCallback(4, this.mMediaRouterCallback, 8);
        this.mActivityManagerInternal.registerProcessObserver(new android.app.IProcessObserver.Stub() { // from class: com.android.server.media.projection.MediaProjectionManagerService.1
            public void onForegroundActivitiesChanged(int pid, int uid, boolean fg) {
            }

            public void onProcessStarted(int pid, int processUid, int packageUid, java.lang.String packageName, java.lang.String processName) {
            }

            public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) {
                com.android.server.media.projection.MediaProjectionManagerService.this.handleForegroundServicesChanged(pid, uid, serviceTypes);
            }

            public void onProcessDied(int pid, int uid) {
            }
        });
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        this.mMediaRouter.rebindAsUser(to.getUserIdentifier());
        synchronized (this.mLock) {
            if (this.mProjectionGrant != null) {
                android.util.Slog.d(TAG, "Content Recording: Stopped MediaProjection due to user switching");
                this.mProjectionGrant.stop();
            }
            if (this.mOplusProjectionGrant != null) {
                this.mOplusProjectionGrant.stop();
            }
        }
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundServicesChanged(int pid, int uid, int serviceTypes) {
        synchronized (this.mLock) {
            if (this.mProjectionGrant != null && this.mProjectionGrant.uid == uid) {
                if (this.mProjectionGrant.requiresForegroundService()) {
                    if (this.mOplusProjectionGrant == null || this.mOplusProjectionGrant.uid != uid || !this.mOplusProjectionGrant.requiresForegroundService() || this.mActivityManagerInternal.hasRunningForegroundService(uid, 32)) {
                        return;
                    }
                    synchronized (this.mLock) {
                        android.util.Slog.d(TAG, "Content Recording: Stopped MediaProjection due to foreground service change");
                        if (this.mProjectionGrant != null) {
                            this.mProjectionGrant.stop();
                        }
                        this.mOplusProjectionGrant.stop();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startProjectionLocked(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection) {
        if (this.mProjectionGrant != null && !projection.packageName.equals("com.oplus.screenrecorder")) {
            android.util.Slog.d(TAG, "Content Recording: Stopped MediaProjection to start new incoming projection");
            this.mProjectionGrant.stop();
        }
        if (this.mOplusProjectionGrant != null && projection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusProjectionGrant.stop();
        }
        if (this.mMediaRouteInfo != null) {
            android.util.Slog.d(TAG, "connect status is " + ((java.lang.Object) this.mMediaRouteInfo.getStatus()));
            if (this.mMediaRouteInfo.getStatusCode() != 6) {
                this.mMediaRouter.getFallbackRoute().select();
            }
        }
        if (projection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusProjectionGrant = projection;
            this.mOplusProjectionToken = projection.asBinder();
        } else {
            this.mProjectionGrant = projection;
            this.mProjectionToken = projection.asBinder();
        }
        dispatchStart(projection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopProjectionLocked(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection) {
        int targetUid;
        android.util.Slog.d(TAG, "Content Recording: Stopped active MediaProjection and dispatching stop to callbacks");
        android.view.ContentRecordingSession session = projection.mSession;
        if (session != null) {
            targetUid = session.getTargetUid();
        } else {
            targetUid = -2;
        }
        this.mMediaProjectionMetricsLogger.logStopped(projection.uid, targetUid);
        if (projection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusProjectionGrant = null;
            this.mOplusProjectionToken = null;
        } else {
            this.mProjectionToken = null;
            this.mProjectionGrant = null;
        }
        dispatchStop(projection);
    }

    android.media.projection.MediaProjectionInfo addCallback(final android.media.projection.IMediaProjectionWatcherCallback callback) {
        android.media.projection.MediaProjectionInfo projectionInfo;
        android.os.IBinder.DeathRecipient deathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.media.projection.MediaProjectionManagerService.2
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                com.android.server.media.projection.MediaProjectionManagerService.this.removeCallback(callback);
            }
        };
        synchronized (this.mLock) {
            this.mCallbackDelegate.add(callback);
            linkDeathRecipientLocked(callback, deathRecipient);
            projectionInfo = this.mProjectionGrant != null ? this.mProjectionGrant.getProjectionInfo() : null;
        }
        return projectionInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCallback(android.media.projection.IMediaProjectionWatcherCallback callback) {
        synchronized (this.mLock) {
            unlinkDeathRecipientLocked(callback);
            this.mCallbackDelegate.remove(callback);
        }
    }

    private void linkDeathRecipientLocked(android.media.projection.IMediaProjectionWatcherCallback callback, android.os.IBinder.DeathRecipient deathRecipient) {
        try {
            android.os.IBinder token = callback.asBinder();
            token.linkToDeath(deathRecipient, 0);
            this.mDeathEaters.put(token, deathRecipient);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to link to death for media projection monitoring callback", e);
        }
    }

    private void unlinkDeathRecipientLocked(android.media.projection.IMediaProjectionWatcherCallback callback) {
        android.os.IBinder token = callback.asBinder();
        android.os.IBinder.DeathRecipient deathRecipient = this.mDeathEaters.remove(token);
        if (deathRecipient != null) {
            token.unlinkToDeath(deathRecipient, 0);
        }
    }

    private void dispatchStart(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection) {
        if (projection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusCallbackDelegate.dispatchStart(projection);
        } else {
            this.mCallbackDelegate.dispatchStart(projection);
        }
        this.mMediaProjectionManagerServiceExt.start(this.mContext, projection.packageName, com.android.server.media.projection.MediaProjectionManagerService.MediaProjection.getCallingUserHandle());
    }

    private void dispatchStop(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection) {
        if (projection.packageName.equals("com.oplus.screenrecorder")) {
            this.mOplusCallbackDelegate.dispatchStop(projection);
        } else {
            this.mCallbackDelegate.dispatchStop(projection);
        }
        this.mMediaProjectionManagerServiceExt.stop(this.mContext);
    }

    private void dispatchSessionSet(android.media.projection.MediaProjectionInfo projectionInfo, android.view.ContentRecordingSession session) {
        this.mCallbackDelegate.dispatchSession(projectionInfo, session);
    }

    boolean setContentRecordingSession(android.view.ContentRecordingSession incomingSession) {
        java.lang.String projectionType;
        boolean setSessionSucceeded = this.mWmInternal.setContentRecordingSession(incomingSession);
        synchronized (this.mLock) {
            if (!setSessionSucceeded) {
                if (this.mProjectionGrant != null) {
                    if (incomingSession != null) {
                        projectionType = android.view.ContentRecordingSession.recordContentToString(incomingSession.getContentToRecord());
                    } else {
                        projectionType = "none";
                    }
                    android.util.Slog.w(TAG, "Content Recording: Stopped MediaProjection due to failing to set ContentRecordingSession - id= " + this.mProjectionGrant.getVirtualDisplayId() + "type=" + projectionType);
                    this.mProjectionGrant.stop();
                }
                return false;
            }
            if (this.mProjectionGrant != null) {
                this.mProjectionGrant.mSession = incomingSession;
                if (incomingSession != null) {
                    this.mMediaProjectionMetricsLogger.logInProgress(this.mProjectionGrant.uid, incomingSession.getTargetUid());
                }
                dispatchSessionSet(this.mProjectionGrant.getProjectionInfo(), incomingSession);
            }
            return true;
        }
    }

    boolean isCurrentProjection(android.os.IBinder token) {
        synchronized (this.mLock) {
            if (this.mProjectionToken != null && this.mOplusProjectionToken != null) {
                return this.mProjectionToken.equals(token) || this.mOplusProjectionToken.equals(token);
            }
            if (this.mOplusProjectionToken != null) {
                return this.mOplusProjectionToken.equals(token);
            }
            if (this.mProjectionToken != null) {
                return this.mProjectionToken.equals(token);
            }
            return false;
        }
    }

    void requestConsentForInvalidProjection() {
        android.content.Intent reviewConsentIntent;
        int uid;
        synchronized (this.mLock) {
            reviewConsentIntent = buildReviewGrantedConsentIntentLocked();
            uid = this.mProjectionGrant.uid;
        }
        android.util.Slog.v(TAG, "Reusing token: Reshow dialog for due to invalid projection.");
        this.mContext.startActivityAsUser(reviewConsentIntent, android.os.UserHandle.getUserHandleForUid(uid));
    }

    private android.content.Intent buildReviewGrantedConsentIntentLocked() {
        java.lang.String permissionDialogString = this.mContext.getResources().getString(android.R.string.config_notificationAccessConfirmationActivity);
        android.content.ComponentName mediaProjectionPermissionDialogComponent = android.content.ComponentName.unflattenFromString(permissionDialogString);
        return new android.content.Intent().setComponent(mediaProjectionPermissionDialogComponent).putExtra("extra_media_projection_user_consent_required", true).putExtra("extra_media_projection_package_reusing_consent", this.mProjectionGrant.packageName).setFlags(276824064);
    }

    void notifyPermissionRequestInitiated(int hostUid, int sessionCreationSource) {
        this.mMediaProjectionMetricsLogger.logInitiated(hostUid, sessionCreationSource);
    }

    void notifyPermissionRequestDisplayed(int hostUid) {
        this.mMediaProjectionMetricsLogger.logPermissionRequestDisplayed(hostUid);
    }

    void notifyPermissionRequestCancelled(int hostUid) {
        this.mMediaProjectionMetricsLogger.logProjectionPermissionRequestCancelled(hostUid);
    }

    void notifyAppSelectorDisplayed(int hostUid) {
        this.mMediaProjectionMetricsLogger.logAppSelectorDisplayed(hostUid);
    }

    void notifyWindowingModeChanged(int contentToRecord, int targetUid, int windowingMode) {
        synchronized (this.mLock) {
            if (this.mProjectionGrant == null) {
                android.util.Slog.i(TAG, "Cannot log MediaProjectionTargetChanged atom due to null projection");
            } else {
                this.mMediaProjectionMetricsLogger.logChangedWindowingMode(contentToRecord, this.mProjectionGrant.uid, targetUid, windowingMode);
            }
        }
    }

    void setUserReviewGrantedConsentResult(int consentResult, android.media.projection.IMediaProjection projection) {
        synchronized (this.mLock) {
            boolean consentGranted = true;
            if (consentResult != 1 && consentResult != 2) {
                consentGranted = false;
            }
            android.os.IBinder iBinder = null;
            if (consentGranted) {
                if (!isCurrentProjection(projection == null ? null : projection.asBinder())) {
                    android.util.Slog.v(TAG, "Reusing token: Ignore consent result of " + consentResult + " for a token that isn't current");
                    return;
                }
            }
            if (this.mProjectionGrant == null) {
                android.util.Slog.w(TAG, "Reusing token: Can't review consent with no ongoing projection.");
                return;
            }
            if (this.mProjectionGrant.mSession != null && this.mProjectionGrant.mSession.isWaitingForConsent()) {
                android.util.Slog.v(TAG, "Reusing token: Handling user consent result " + consentResult);
                switch (consentResult) {
                    case -1:
                    case 0:
                        setReviewedConsentSessionLocked(null);
                        if (this.mProjectionGrant != null) {
                            android.util.Slog.w(TAG, "Content Recording: Stopped MediaProjection due to user consent result of CANCEL - id= " + this.mProjectionGrant.getVirtualDisplayId());
                            this.mProjectionGrant.stop();
                        }
                        break;
                    case 1:
                        setReviewedConsentSessionLocked(android.view.ContentRecordingSession.createDisplaySession(0));
                        break;
                    case 2:
                        if (this.mProjectionGrant.getLaunchCookie() != null) {
                            iBinder = this.mProjectionGrant.getLaunchCookie().binder;
                        }
                        android.os.IBinder taskWindowContainerToken = iBinder;
                        setReviewedConsentSessionLocked(android.view.ContentRecordingSession.createTaskSession(taskWindowContainerToken, this.mProjectionGrant.mTaskId));
                        break;
                }
                return;
            }
            android.util.Slog.w(TAG, "Reusing token: Ignore consent result " + consentResult + " if not waiting for the result.");
        }
    }

    private void setReviewedConsentSessionLocked(android.view.ContentRecordingSession session) {
        if (session != null) {
            session.setWaitingForConsent(false);
            session.setVirtualDisplayId(this.mProjectionGrant.mVirtualDisplayId);
        }
        android.util.Slog.v(TAG, "Reusing token: Processed consent so set the session " + session);
        if (!setContentRecordingSession(session)) {
            android.util.Slog.e(TAG, "Reusing token: Failed to set session for reused consent, so stop");
        }
    }

    com.android.server.media.projection.MediaProjectionManagerService.MediaProjection createProjectionInternal(int uid, java.lang.String packageName, int type, boolean isPermanentGrant, android.os.UserHandle callingUser) {
        try {
            android.content.pm.ApplicationInfo ai = this.mPackageManager.getApplicationInfoAsUser(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(0L), callingUser);
            long callingToken = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection = new com.android.server.media.projection.MediaProjectionManagerService.MediaProjection(type, uid, packageName, ai.targetSdkVersion, ai.isPrivilegedApp());
                if (isPermanentGrant) {
                    this.mAppOps.setMode(46, projection.uid, projection.packageName, 0);
                }
                return projection;
            } finally {
                android.os.Binder.restoreCallingIdentity(callingToken);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.IllegalArgumentException("No package matching :" + packageName);
        }
    }

    com.android.server.media.projection.MediaProjectionManagerService.MediaProjection getProjectionInternal(int uid, java.lang.String packageName) {
        long callingToken = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (this.mProjectionGrant != null && this.mProjectionGrant.mSession != null && this.mProjectionGrant.mSession.isWaitingForConsent()) {
                    if (this.mProjectionGrant.uid != uid || !java.util.Objects.equals(this.mProjectionGrant.packageName, packageName)) {
                        android.util.Slog.e(TAG, "Reusing token: Not possible to reuse the current projection instance due to package details mismatching");
                        return null;
                    }
                    android.util.Slog.v(TAG, "Reusing token: getProjection can reuse the current projection");
                    return this.mProjectionGrant;
                }
                android.util.Slog.e(TAG, "Reusing token: Not possible to reuse the current projection instance");
                return null;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingToken);
        }
    }

    android.media.projection.MediaProjectionInfo getActiveProjectionInfo() {
        synchronized (this.mLock) {
            if (this.mProjectionGrant != null) {
                return this.mProjectionGrant.getProjectionInfo();
            }
            if (this.mOplusProjectionGrant == null) {
                return null;
            }
            return this.mOplusProjectionGrant.getProjectionInfo();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dump(java.io.PrintWriter pw) {
        pw.println("MEDIA PROJECTION MANAGER (dumpsys media_projection)");
        synchronized (this.mLock) {
            pw.println("Media Projection: ");
            if (this.mProjectionGrant != null) {
                this.mProjectionGrant.dump(pw);
            } else if (this.mOplusProjectionGrant != null) {
                this.mOplusProjectionGrant.dump(pw);
            } else {
                pw.println("null");
            }
        }
    }

    private final class BinderService extends android.media.projection.IMediaProjectionManager.Stub {
        BinderService(android.content.Context context) {
            super(android.os.PermissionEnforcer.fromContext(context));
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x001e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean hasProjectionPermission(int r6, java.lang.String r7) {
            /*
                r5 = this;
                long r0 = android.os.Binder.clearCallingIdentity()
                r2 = 0
                java.lang.String r3 = "android.permission.CAPTURE_VIDEO_OUTPUT"
                boolean r3 = r5.checkPermission(r7, r3)     // Catch: java.lang.Throwable -> L25
                if (r3 != 0) goto L1e
                com.android.server.media.projection.MediaProjectionManagerService r3 = com.android.server.media.projection.MediaProjectionManagerService.this     // Catch: java.lang.Throwable -> L25
                android.app.AppOpsManager r3 = com.android.server.media.projection.MediaProjectionManagerService.m5440$$Nest$fgetmAppOps(r3)     // Catch: java.lang.Throwable -> L25
                r4 = 46
                int r3 = r3.noteOpNoThrow(r4, r6, r7)     // Catch: java.lang.Throwable -> L25
                if (r3 != 0) goto L1c
                goto L1e
            L1c:
                r3 = 0
                goto L1f
            L1e:
                r3 = 1
            L1f:
                r2 = r2 | r3
                android.os.Binder.restoreCallingIdentity(r0)
                return r2
            L25:
                r3 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.projection.MediaProjectionManagerService.BinderService.hasProjectionPermission(int, java.lang.String):boolean");
        }

        public android.media.projection.IMediaProjection createProjection(int processUid, java.lang.String packageName, int type, boolean isPermanentGrant) {
            if (com.android.server.media.projection.MediaProjectionManagerService.this.mContext.checkCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new java.lang.SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to grant projection permission");
            }
            if (packageName == null || packageName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("package name must not be empty");
            }
            android.os.UserHandle callingUser = android.os.Binder.getCallingUserHandle();
            return com.android.server.media.projection.MediaProjectionManagerService.this.createProjectionInternal(processUid, packageName, type, isPermanentGrant, callingUser);
        }

        public android.media.projection.IMediaProjection getProjection(int processUid, java.lang.String packageName) {
            getProjection_enforcePermission();
            if (packageName == null || packageName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("package name must not be empty");
            }
            long callingToken = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection = com.android.server.media.projection.MediaProjectionManagerService.this.getProjectionInternal(processUid, packageName);
                return projection;
            } finally {
                android.os.Binder.restoreCallingIdentity(callingToken);
            }
        }

        public boolean isCurrentProjection(android.media.projection.IMediaProjection projection) {
            isCurrentProjection_enforcePermission();
            return com.android.server.media.projection.MediaProjectionManagerService.this.isCurrentProjection(projection == null ? null : projection.asBinder());
        }

        public android.media.projection.MediaProjectionInfo getActiveProjectionInfo() {
            if (com.android.server.media.projection.MediaProjectionManagerService.this.mContext.checkCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new java.lang.SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to get active projection info");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.media.projection.MediaProjectionManagerService.this.getActiveProjectionInfo();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void stopActiveProjection() {
            stopActiveProjection_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                    if (com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant != null) {
                        android.util.Slog.d(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Content Recording: Stopping active projection");
                        com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant.stop();
                    }
                }
                if (com.android.server.media.projection.MediaProjectionManagerService.this.mOplusProjectionGrant != null) {
                    com.android.server.media.projection.MediaProjectionManagerService.this.mOplusProjectionGrant.stop();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyActiveProjectionCapturedContentResized(int width, int height) {
            notifyActiveProjectionCapturedContentResized_enforcePermission();
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                if (isCurrentProjection(com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant)) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                            if (com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant != null && com.android.server.media.projection.MediaProjectionManagerService.this.mCallbackDelegate != null) {
                                com.android.server.media.projection.MediaProjectionManagerService.this.mCallbackDelegate.dispatchResize(com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant, width, height);
                            }
                        }
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
            }
        }

        public void notifyActiveProjectionCapturedContentVisibilityChanged(boolean isVisible) {
            notifyActiveProjectionCapturedContentVisibilityChanged_enforcePermission();
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                if (isCurrentProjection(com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant)) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                            if (com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant != null && com.android.server.media.projection.MediaProjectionManagerService.this.mCallbackDelegate != null) {
                                com.android.server.media.projection.MediaProjectionManagerService.this.mCallbackDelegate.dispatchVisibilityChanged(com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant, isVisible);
                            }
                        }
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
            }
        }

        public android.media.projection.MediaProjectionInfo addCallback(android.media.projection.IMediaProjectionWatcherCallback callback) {
            addCallback_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.media.projection.MediaProjectionManagerService.this.addCallback(callback);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void removeCallback(android.media.projection.IMediaProjectionWatcherCallback callback) {
            if (com.android.server.media.projection.MediaProjectionManagerService.this.mContext.checkCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION") != 0) {
                throw new java.lang.SecurityException("Requires MANAGE_MEDIA_PROJECTION in order to remove projection callbacks");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.this.removeCallback(callback);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setContentRecordingSession(android.view.ContentRecordingSession incomingSession, android.media.projection.IMediaProjection projection) {
            setContentRecordingSession_enforcePermission();
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                if (!isCurrentProjection(projection)) {
                    throw new java.lang.SecurityException("Unable to set ContentRecordingSession on non-current MediaProjection");
                }
            }
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.media.projection.MediaProjectionManagerService.this.setContentRecordingSession(incomingSession);
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }

        public void requestConsentForInvalidProjection(android.media.projection.IMediaProjection projection) {
            requestConsentForInvalidProjection_enforcePermission();
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                if (!isCurrentProjection(projection)) {
                    android.util.Slog.v(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Reusing token: Won't request consent again for a token that isn't current");
                    return;
                }
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.media.projection.MediaProjectionManagerService.this.requestConsentForInvalidProjection();
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }

        public void setUserReviewGrantedConsentResult(int consentResult, android.media.projection.IMediaProjection projection) {
            setUserReviewGrantedConsentResult_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.this.setUserReviewGrantedConsentResult(consentResult, projection);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyPermissionRequestInitiated(int hostProcessUid, int sessionCreationSource) {
            notifyPermissionRequestInitiated_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.this.notifyPermissionRequestInitiated(hostProcessUid, sessionCreationSource);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyPermissionRequestDisplayed(int hostProcessUid) {
            notifyPermissionRequestDisplayed_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.this.notifyPermissionRequestDisplayed(hostProcessUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyPermissionRequestCancelled(int hostProcessUid) {
            notifyPermissionRequestCancelled_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.this.notifyPermissionRequestCancelled(hostProcessUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyAppSelectorDisplayed(int hostProcessUid) {
            notifyAppSelectorDisplayed_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.this.notifyAppSelectorDisplayed(hostProcessUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyWindowingModeChanged(int contentToRecord, int targetProcessUid, int windowingMode) {
            notifyWindowingModeChanged_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.projection.MediaProjectionManagerService.this.notifyWindowingModeChanged(contentToRecord, targetProcessUid, windowingMode);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.media.projection.MediaProjectionManagerService.this.mContext, com.android.server.media.projection.MediaProjectionManagerService.TAG, pw)) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.media.projection.MediaProjectionManagerService.this.dump(pw);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }

        private boolean checkPermission(java.lang.String packageName, java.lang.String permission) {
            return com.android.server.media.projection.MediaProjectionManagerService.this.mContext.getPackageManager().checkPermission(permission, packageName) == 0;
        }
    }

    final class MediaProjection extends android.media.projection.IMediaProjection.Stub {
        private android.media.projection.IMediaProjectionCallback mCallback;
        private final long mCreateTimeMs;
        private android.os.IBinder.DeathRecipient mDeathEater;
        private final boolean mIsPrivileged;
        private boolean mRestoreSystemAlertWindow;
        private android.view.ContentRecordingSession mSession;
        private final int mTargetSdkVersion;
        private android.os.IBinder mToken;
        private final int mType;
        public final java.lang.String packageName;
        public final int uid;
        public final android.os.UserHandle userHandle;
        final long mDefaultTimeoutMs = java.time.Duration.ofMinutes(5).toMillis();
        private int mTaskId = -1;
        private android.app.ActivityOptions.LaunchCookie mLaunchCookie = null;
        private long mTimeoutMs = this.mDefaultTimeoutMs;
        private int mCountStarts = 0;
        private int mVirtualDisplayId = -1;

        MediaProjection(int type, int uid, java.lang.String packageName, int targetSdkVersion, boolean isPrivileged) {
            this.mType = type;
            this.uid = uid;
            this.packageName = packageName;
            this.userHandle = new android.os.UserHandle(android.os.UserHandle.getUserId(uid));
            this.mTargetSdkVersion = targetSdkVersion;
            this.mIsPrivileged = isPrivileged;
            this.mCreateTimeMs = com.android.server.media.projection.MediaProjectionManagerService.this.mClock.uptimeMillis();
            com.android.server.media.projection.MediaProjectionManagerService.this.mActivityManagerInternal.notifyMediaProjectionEvent(uid, asBinder(), 0);
        }

        int getVirtualDisplayId() {
            return this.mVirtualDisplayId;
        }

        public boolean canProjectVideo() {
            return this.mType == 1 || this.mType == 0;
        }

        public boolean canProjectSecureVideo() {
            return false;
        }

        public boolean canProjectAudio() {
            return this.mType == 1 || this.mType == 2 || this.mType == 0;
        }

        public int applyVirtualDisplayFlags(int flags) {
            applyVirtualDisplayFlags_enforcePermission();
            if (this.mType == 0) {
                return (flags & (-9)) | 18;
            }
            if (this.mType == 1) {
                return (flags & (-18)) | 10;
            }
            if (this.mType == 2) {
                return (flags & (-9)) | 19;
            }
            throw new java.lang.RuntimeException("Unknown MediaProjection type");
        }

        public void start(final android.media.projection.IMediaProjectionCallback callback) {
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("callback must not be null");
            }
            android.util.Slog.v(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Start the token instance " + this);
            if (this.mType == 0) {
                ((com.oplus.permission.IOplusPermissionCheckInjectorExt) system.ext.loader.core.ExtLoader.type(com.oplus.permission.IOplusPermissionCheckInjectorExt.class).create()).checkPermission("capture_or_mirror_screen", getCallingPid(), this.uid, "recordMirrorScreenBehavior");
                android.util.Slog.d(com.android.server.media.projection.MediaProjectionManagerService.TAG, "start capture_or_mirror_screen uid:" + this.uid + " pid:" + getCallingPid());
            }
            boolean hasFGS = com.android.server.media.projection.MediaProjectionManagerService.this.mActivityManagerInternal.hasRunningForegroundService(this.uid, 32);
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                if (com.android.server.media.projection.MediaProjectionManagerService.this.isCurrentProjection(asBinder())) {
                    android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "UID " + android.os.Binder.getCallingUid() + " attempted to start already started MediaProjection");
                    this.mCountStarts++;
                    return;
                }
                if (requiresForegroundService() && !hasFGS) {
                    throw new java.lang.SecurityException("Media projections require a foreground service of type ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION");
                }
                try {
                    this.mToken = callback.asBinder();
                    this.mDeathEater = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.media.projection.MediaProjectionManagerService$MediaProjection$$ExternalSyntheticLambda1
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            this.f$0.lambda$start$0(callback);
                        }
                    };
                    this.mToken.linkToDeath(this.mDeathEater, 0);
                    if (this.mType == 0) {
                        long token = android.os.Binder.clearCallingIdentity();
                        try {
                            android.content.pm.PackageInfo packageInfo = com.android.server.media.projection.MediaProjectionManagerService.this.mPackageManager.getPackageInfoAsUser(this.packageName, 4096, android.os.UserHandle.getUserId(this.uid));
                            if (com.android.internal.util.ArrayUtils.contains(packageInfo.requestedPermissions, "android.permission.SYSTEM_ALERT_WINDOW")) {
                                int currentMode = com.android.server.media.projection.MediaProjectionManagerService.this.mAppOps.unsafeCheckOpRawNoThrow(24, this.uid, this.packageName);
                                if (currentMode == 3) {
                                    com.android.server.media.projection.MediaProjectionManagerService.this.mAppOps.setMode(24, this.uid, this.packageName, 0);
                                    this.mRestoreSystemAlertWindow = true;
                                }
                            }
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Package not found, aborting MediaProjection", e);
                            return;
                        } finally {
                            android.os.Binder.restoreCallingIdentity(token);
                        }
                    }
                    com.android.server.media.projection.MediaProjectionManagerService.this.startProjectionLocked(this);
                    this.mCallback = callback;
                    registerCallback(this.mCallback);
                    this.mCountStarts++;
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "MediaProjectionCallbacks must be valid, aborting MediaProjection", e2);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$0(android.media.projection.IMediaProjectionCallback callback) {
            android.util.Slog.d(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Content Recording: MediaProjection stopped by Binder death - id= " + this.mVirtualDisplayId);
            unregisterCallback(callback);
            stop();
        }

        public void stop() {
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                if (!com.android.server.media.projection.MediaProjectionManagerService.this.isCurrentProjection(asBinder())) {
                    android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Attempted to stop inactive MediaProjection (uid=" + android.os.Binder.getCallingUid() + ", pid=" + android.os.Binder.getCallingPid() + ")");
                    return;
                }
                if (this.mRestoreSystemAlertWindow) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        int currentMode = com.android.server.media.projection.MediaProjectionManagerService.this.mAppOps.unsafeCheckOpRawNoThrow(24, this.uid, this.packageName);
                        if (currentMode == 0) {
                            com.android.server.media.projection.MediaProjectionManagerService.this.mAppOps.setMode(24, this.uid, this.packageName, 3);
                        }
                        this.mRestoreSystemAlertWindow = false;
                        android.os.Binder.restoreCallingIdentity(token);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                }
                android.util.Slog.d(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Content Recording: handling stopping this projection token createTime= " + this.mCreateTimeMs + " countStarts= " + this.mCountStarts);
                com.android.server.media.projection.MediaProjectionManagerService.this.stopProjectionLocked(this);
                this.mToken.unlinkToDeath(this.mDeathEater, 0);
                this.mToken = null;
                unregisterCallback(this.mCallback);
                this.mCallback = null;
                com.android.server.media.projection.MediaProjectionManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.projection.MediaProjectionManagerService$MediaProjection$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$stop$1();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$stop$1() {
            com.android.server.media.projection.MediaProjectionManagerService.this.mActivityManagerInternal.notifyMediaProjectionEvent(this.uid, asBinder(), 1);
        }

        public void registerCallback(android.media.projection.IMediaProjectionCallback callback) {
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("callback must not be null");
            }
            if (this.packageName.equals("com.oplus.screenrecorder")) {
                com.android.server.media.projection.MediaProjectionManagerService.this.mOplusCallbackDelegate.add(callback);
            } else {
                com.android.server.media.projection.MediaProjectionManagerService.this.mCallbackDelegate.add(callback);
            }
        }

        public void unregisterCallback(android.media.projection.IMediaProjectionCallback callback) {
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("callback must not be null");
            }
            if (this.packageName.equals("com.oplus.screenrecorder")) {
                com.android.server.media.projection.MediaProjectionManagerService.this.mOplusCallbackDelegate.remove(callback);
            } else {
                com.android.server.media.projection.MediaProjectionManagerService.this.mCallbackDelegate.remove(callback);
            }
        }

        public void setLaunchCookie(android.app.ActivityOptions.LaunchCookie launchCookie) {
            setLaunchCookie_enforcePermission();
            this.mLaunchCookie = launchCookie;
        }

        public void setTaskId(int taskId) {
            setTaskId_enforcePermission();
            this.mTaskId = taskId;
        }

        public android.app.ActivityOptions.LaunchCookie getLaunchCookie() {
            getLaunchCookie_enforcePermission();
            return this.mLaunchCookie;
        }

        public int getTaskId() {
            getTaskId_enforcePermission();
            return this.mTaskId;
        }

        public boolean isValid() {
            isValid_enforcePermission();
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                long curMs = com.android.server.media.projection.MediaProjectionManagerService.this.mClock.uptimeMillis();
                boolean hasTimedOut = curMs - this.mCreateTimeMs > this.mTimeoutMs;
                boolean virtualDisplayCreated = this.mVirtualDisplayId != -1;
                boolean isValid = (hasTimedOut || this.mCountStarts > 1 || virtualDisplayCreated) ? false : true;
                if (isValid) {
                    return true;
                }
                if (!com.android.server.media.projection.MediaProjectionManagerService.this.mInjector.shouldMediaProjectionPreventReusingConsent(com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant)) {
                    return false;
                }
                if (com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant.packageName.equals("com.oplus.cast")) {
                    android.util.Slog.i(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Reusing token: heycast is allways valid projection.");
                    return true;
                }
                android.util.Slog.v(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Reusing token: Throw exception due to invalid projection.");
                com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant.stop();
                throw new java.lang.SecurityException("Don't re-use the resultData to retrieve the same projection instance, and don't use a token that has timed out. Don't take multiple captures by invoking MediaProjection#createVirtualDisplay multiple times on the same instance.");
            }
        }

        public void notifyVirtualDisplayCreated(int displayId) {
            notifyVirtualDisplayCreated_enforcePermission();
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                this.mVirtualDisplayId = displayId;
                if (this.mSession != null && this.mSession.getVirtualDisplayId() == -1) {
                    android.util.Slog.v(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Virtual display now created, so update session with the virtual display id");
                    this.mSession.setVirtualDisplayId(this.mVirtualDisplayId);
                    if (!com.android.server.media.projection.MediaProjectionManagerService.this.setContentRecordingSession(this.mSession)) {
                        android.util.Slog.e(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Failed to set session for virtual display id");
                    }
                }
            }
        }

        public android.media.projection.MediaProjectionInfo getProjectionInfo() {
            return new android.media.projection.MediaProjectionInfo(this.packageName, this.userHandle, this.mLaunchCookie);
        }

        boolean requiresForegroundService() {
            return this.mTargetSdkVersion >= 29 && !this.mIsPrivileged;
        }

        public void dump(java.io.PrintWriter pw) {
            pw.println("(" + this.packageName + ", uid=" + this.uid + "): " + com.android.server.media.projection.MediaProjectionManagerService.typeToString(this.mType));
        }
    }

    private class MediaRouterCallback extends android.media.MediaRouter.SimpleCallback {
        private MediaRouterCallback() {
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteSelected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo info) {
            synchronized (com.android.server.media.projection.MediaProjectionManagerService.this.mLock) {
                if ((type & 4) != 0) {
                    com.android.server.media.projection.MediaProjectionManagerService.this.mMediaRouteInfo = info;
                    if (com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant != null) {
                        android.util.Slog.d(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Content Recording: Stopped MediaProjection due to route type of REMOTE_DISPLAY not selected");
                        com.android.server.media.projection.MediaProjectionManagerService.this.mProjectionGrant.stop();
                    }
                    if (com.android.server.media.projection.MediaProjectionManagerService.this.mOplusProjectionGrant != null) {
                        com.android.server.media.projection.MediaProjectionManagerService.this.mOplusProjectionGrant.stop();
                    }
                }
            }
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteUnselected(android.media.MediaRouter route, int type, android.media.MediaRouter.RouteInfo info) {
            if (com.android.server.media.projection.MediaProjectionManagerService.this.mMediaRouteInfo == info) {
                com.android.server.media.projection.MediaProjectionManagerService.this.mMediaRouteInfo = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class CallbackDelegate {
        private android.os.Handler mHandler;
        private final java.lang.Object mLock = new java.lang.Object();
        private java.util.Map<android.os.IBinder, android.media.projection.IMediaProjectionCallback> mClientCallbacks = new android.util.ArrayMap();
        private java.util.Map<android.os.IBinder, android.media.projection.IMediaProjectionWatcherCallback> mWatcherCallbacks = new android.util.ArrayMap();

        CallbackDelegate(android.os.Looper callbackLooper) {
            this.mHandler = new android.os.Handler(callbackLooper, null, true);
        }

        public void add(android.media.projection.IMediaProjectionCallback callback) {
            synchronized (this.mLock) {
                this.mClientCallbacks.put(callback.asBinder(), callback);
            }
        }

        public void add(android.media.projection.IMediaProjectionWatcherCallback callback) {
            synchronized (this.mLock) {
                this.mWatcherCallbacks.put(callback.asBinder(), callback);
            }
        }

        public void remove(android.media.projection.IMediaProjectionCallback callback) {
            synchronized (this.mLock) {
                this.mClientCallbacks.remove(callback.asBinder());
            }
        }

        public void remove(android.media.projection.IMediaProjectionWatcherCallback callback) {
            synchronized (this.mLock) {
                this.mWatcherCallbacks.remove(callback.asBinder());
            }
        }

        public void dispatchStart(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection) {
            if (projection == null) {
                android.util.Slog.e(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Tried to dispatch start notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                for (android.media.projection.IMediaProjectionWatcherCallback callback : this.mWatcherCallbacks.values()) {
                    android.media.projection.MediaProjectionInfo info = projection.getProjectionInfo();
                    this.mHandler.post(new com.android.server.media.projection.MediaProjectionManagerService.WatcherStartCallback(info, callback));
                }
            }
        }

        public void dispatchStop(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection) {
            if (projection == null) {
                android.util.Slog.e(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Tried to dispatch stop notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                for (android.media.projection.IMediaProjectionCallback callback : this.mClientCallbacks.values()) {
                    this.mHandler.post(new com.android.server.media.projection.MediaProjectionManagerService.ClientStopCallback(callback));
                }
                for (android.media.projection.IMediaProjectionWatcherCallback callback2 : this.mWatcherCallbacks.values()) {
                    android.media.projection.MediaProjectionInfo info = projection.getProjectionInfo();
                    this.mHandler.post(new com.android.server.media.projection.MediaProjectionManagerService.WatcherStopCallback(info, callback2));
                }
            }
        }

        public void dispatchSession(android.media.projection.MediaProjectionInfo projectionInfo, android.view.ContentRecordingSession session) {
            synchronized (this.mLock) {
                for (android.media.projection.IMediaProjectionWatcherCallback callback : this.mWatcherCallbacks.values()) {
                    this.mHandler.post(new com.android.server.media.projection.MediaProjectionManagerService.WatcherSessionCallback(callback, projectionInfo, session));
                }
            }
        }

        public void dispatchResize(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection, final int width, final int height) {
            if (projection == null) {
                android.util.Slog.e(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Tried to dispatch resize notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                for (final android.media.projection.IMediaProjectionCallback callback : this.mClientCallbacks.values()) {
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.projection.MediaProjectionManagerService$CallbackDelegate$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.media.projection.MediaProjectionManagerService.CallbackDelegate.lambda$dispatchResize$0(callback, width, height);
                        }
                    });
                }
            }
        }

        static /* synthetic */ void lambda$dispatchResize$0(android.media.projection.IMediaProjectionCallback callback, int width, int height) {
            try {
                callback.onCapturedContentResize(width, height);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Failed to notify media projection has resized to " + width + " x " + height, e);
            }
        }

        public void dispatchVisibilityChanged(com.android.server.media.projection.MediaProjectionManagerService.MediaProjection projection, final boolean isVisible) {
            if (projection == null) {
                android.util.Slog.e(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Tried to dispatch visibility changed notification for a null media projection. Ignoring!");
                return;
            }
            synchronized (this.mLock) {
                for (final android.media.projection.IMediaProjectionCallback callback : this.mClientCallbacks.values()) {
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.projection.MediaProjectionManagerService$CallbackDelegate$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.media.projection.MediaProjectionManagerService.CallbackDelegate.lambda$dispatchVisibilityChanged$1(callback, isVisible);
                        }
                    });
                }
            }
        }

        static /* synthetic */ void lambda$dispatchVisibilityChanged$1(android.media.projection.IMediaProjectionCallback callback, boolean isVisible) {
            try {
                callback.onCapturedContentVisibilityChanged(isVisible);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Failed to notify media projection has captured content visibility change to " + isVisible, e);
            }
        }
    }

    private static final class WatcherStartCallback implements java.lang.Runnable {
        private android.media.projection.IMediaProjectionWatcherCallback mCallback;
        private android.media.projection.MediaProjectionInfo mInfo;

        public WatcherStartCallback(android.media.projection.MediaProjectionInfo info, android.media.projection.IMediaProjectionWatcherCallback callback) {
            this.mInfo = info;
            this.mCallback = callback;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mCallback.onStart(this.mInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Failed to notify media projection has started", e);
            }
        }
    }

    private static final class WatcherStopCallback implements java.lang.Runnable {
        private android.media.projection.IMediaProjectionWatcherCallback mCallback;
        private android.media.projection.MediaProjectionInfo mInfo;

        public WatcherStopCallback(android.media.projection.MediaProjectionInfo info, android.media.projection.IMediaProjectionWatcherCallback callback) {
            this.mInfo = info;
            this.mCallback = callback;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mCallback.onStop(this.mInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Failed to notify media projection has stopped", e);
            }
        }
    }

    private static final class ClientStopCallback implements java.lang.Runnable {
        private android.media.projection.IMediaProjectionCallback mCallback;

        public ClientStopCallback(android.media.projection.IMediaProjectionCallback callback) {
            this.mCallback = callback;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mCallback.onStop();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Failed to notify media projection has stopped", e);
            }
        }
    }

    private static final class WatcherSessionCallback implements java.lang.Runnable {
        private final android.media.projection.IMediaProjectionWatcherCallback mCallback;
        private final android.media.projection.MediaProjectionInfo mProjectionInfo;
        private final android.view.ContentRecordingSession mSession;

        WatcherSessionCallback(android.media.projection.IMediaProjectionWatcherCallback callback, android.media.projection.MediaProjectionInfo projectionInfo, android.view.ContentRecordingSession session) {
            this.mCallback = callback;
            this.mProjectionInfo = projectionInfo;
            this.mSession = session;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mCallback.onRecordingSessionSet(this.mProjectionInfo, this.mSession);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.media.projection.MediaProjectionManagerService.TAG, "Failed to notify content recording session changed", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String typeToString(int type) {
        switch (type) {
            case 0:
                return "TYPE_SCREEN_CAPTURE";
            case 1:
                return "TYPE_MIRRORING";
            case 2:
                return "TYPE_PRESENTATION";
            default:
                return java.lang.Integer.toString(type);
        }
    }
}
