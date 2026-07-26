package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageInstallerService extends android.content.pm.IPackageInstaller.Stub implements com.android.server.pm.PackageSessionProvider {
    private static final int ADB_DEV_MODE = 36;
    private static final int HISTORICAL_CLEAR_SIZE = 500;
    private static final int HISTORICAL_SESSIONS_THRESHOLD = 5000;
    private static final long MAX_ACTIVE_SESSIONS_NO_PERMISSION = 50;
    private static final long MAX_ACTIVE_SESSIONS_WITH_PERMISSION = 1024;
    private static final long MAX_AGE_MILLIS = 259200000;
    private static final long MAX_HISTORICAL_SESSIONS = 1048576;
    private static final long MAX_INSTALL_CONSTRAINTS_TIMEOUT_MILLIS = 604800000;
    private static final long MAX_SESSION_AGE_ON_LOW_STORAGE_MILLIS = 28800000;
    private static final long MAX_TIME_SINCE_UPDATE_MILLIS = 1814400000;
    private static final java.lang.String TAG_SESSIONS = "sessions";
    private final com.android.server.pm.ApexManager mApexManager;
    private android.app.AppOpsManager mAppOps;
    private final com.android.server.pm.PackageInstallerService.Callbacks mCallbacks;
    private final android.content.Context mContext;
    private final com.android.server.pm.GentleUpdateHelper mGentleUpdateHelper;
    private final android.os.Handler mInstallHandler;
    final com.android.server.pm.PackageArchiver mPackageArchiver;
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.PackageSessionVerifier mSessionVerifier;
    private final java.io.File mSessionsDir;
    private final android.util.AtomicFile mSessionsFile;
    private final com.android.server.pm.StagingManager mStagingManager;
    private static final java.lang.String TAG = "PackageInstaller";
    private static final boolean LOGD = android.util.Log.isLoggable(TAG, 3);
    private static final boolean DEBUG = android.os.Build.IS_DEBUGGABLE;
    public static final java.util.Set<java.lang.String> INSTALLER_CHANGEABLE_APP_OP_PERMISSIONS = java.util.Set.of("android.permission.USE_FULL_SCREEN_INTENT");
    private static final java.io.FilenameFilter sStageFilter = new java.io.FilenameFilter() { // from class: com.android.server.pm.PackageInstallerService.1
        @Override // java.io.FilenameFilter
        public boolean accept(java.io.File dir, java.lang.String name) {
            return com.android.server.pm.PackageInstallerService.isStageName(name);
        }
    };
    private volatile boolean mOkToSendBroadcasts = false;
    private volatile boolean mBypassNextStagedInstallerCheck = false;
    private volatile boolean mBypassNextAllowedApexUpdateCheck = false;
    private volatile int mDisableVerificationForUid = -1;
    private final com.android.server.pm.PackageInstallerService.InternalCallback mInternalCallback = new com.android.server.pm.PackageInstallerService.InternalCallback();
    private final java.util.Random mRandom = new java.security.SecureRandom();
    private final android.util.SparseBooleanArray mAllocatedSessions = new android.util.SparseBooleanArray();
    private final android.util.SparseArray<com.android.server.pm.PackageInstallerSession> mSessions = new android.util.SparseArray<>();
    private final java.util.List<com.android.server.pm.PackageInstallerHistoricalSession> mHistoricalSessions = new java.util.ArrayList();
    private final android.util.SparseIntArray mHistoricalSessionsByInstaller = new android.util.SparseIntArray();
    private final android.util.SparseBooleanArray mLegacySessions = new android.util.SparseBooleanArray();
    private final com.android.server.pm.SilentUpdatePolicy mSilentUpdatePolicy = new com.android.server.pm.SilentUpdatePolicy();
    private final com.android.server.pm.utils.RequestThrottle mSettingsWriteRequest = new com.android.server.pm.utils.RequestThrottle(com.android.server.IoThread.getHandler(), new java.util.function.Supplier() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda7
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return this.f$0.lambda$new$0();
        }
    });
    public final com.android.server.pm.IPackageInstallerServiceExt mServiceExt = (com.android.server.pm.IPackageInstallerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageInstallerServiceExt.class).base(this).create();
    private final android.os.HandlerThread mInstallThread = new android.os.HandlerThread(TAG);

    private static final class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.pm.PackageInstallerService mPackageInstallerService;

        Lifecycle(android.content.Context context, com.android.server.pm.PackageInstallerService service) {
            super(context);
            this.mPackageInstallerService = service;
        }

        @Override // com.android.server.SystemService
        public void onStart() {
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                this.mPackageInstallerService.onBroadcastReady();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$new$0() {
        return java.lang.Boolean.valueOf(writeSessions());
    }

    public PackageInstallerService(android.content.Context context, com.android.server.pm.PackageManagerService pm, java.util.function.Supplier<com.android.internal.pm.parsing.PackageParser2> apexParserSupplier) {
        this.mContext = context;
        this.mPm = pm;
        this.mInstallThread.start();
        this.mInstallHandler = new android.os.Handler(this.mInstallThread.getLooper());
        this.mCallbacks = new com.android.server.pm.PackageInstallerService.Callbacks(this.mInstallThread.getLooper());
        this.mSessionsFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), "install_sessions.xml"), "package-session");
        this.mSessionsDir = new java.io.File(android.os.Environment.getDataSystemDirectory(), "install_sessions");
        this.mSessionsDir.mkdirs();
        this.mApexManager = com.android.server.pm.ApexManager.getInstance();
        this.mStagingManager = new com.android.server.pm.StagingManager(context);
        this.mSessionVerifier = new com.android.server.pm.PackageSessionVerifier(context, this.mPm, this.mApexManager, apexParserSupplier, this.mInstallThread.getLooper());
        this.mGentleUpdateHelper = new com.android.server.pm.GentleUpdateHelper(context, this.mInstallThread.getLooper(), new com.android.server.pm.AppStateHelper(context));
        this.mPackageArchiver = new com.android.server.pm.PackageArchiver(this.mContext, this.mPm);
        ((com.android.server.SystemServiceManager) com.android.server.LocalServices.getService(com.android.server.SystemServiceManager.class)).startService(new com.android.server.pm.PackageInstallerService.Lifecycle(context, this));
    }

    com.android.server.pm.StagingManager getStagingManager() {
        return this.mStagingManager;
    }

    boolean okToSendBroadcasts() {
        return this.mOkToSendBroadcasts;
    }

    public void systemReady() {
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mStagingManager.systemReady();
        this.mGentleUpdateHelper.systemReady();
        synchronized (this.mSessions) {
            readSessionsLocked();
            expireSessionsLocked();
            reconcileStagesLocked(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL);
            android.util.ArraySet<java.io.File> unclaimedIcons = newArraySet(this.mSessionsDir.listFiles());
            for (int i = 0; i < this.mSessions.size(); i++) {
                com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
                unclaimedIcons.remove(buildAppIconFile(session.sessionId));
            }
            for (java.io.File icon : unclaimedIcons) {
                android.util.Slog.w(TAG, "Deleting orphan icon " + icon);
                icon.delete();
            }
            this.mSettingsWriteRequest.runNow();
        }
        this.mContext.registerReceiver(new com.android.server.pm.PackageInstallerService.AnonymousClass2(), new android.content.IntentFilter("android.intent.action.BOOT_COMPLETED"));
    }

    /* JADX INFO: renamed from: com.android.server.pm.PackageInstallerService$2, reason: invalid class name */
    class AnonymousClass2 extends android.content.BroadcastReceiver {
        AnonymousClass2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            com.android.server.pm.PackageInstallerService.this.mServiceExt.triggerPostBootApexSessionEvent();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context ctx, android.content.Intent intent) {
            com.android.internal.os.BackgroundThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.pm.PackageInstallerService$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBroadcastReady() {
        this.mOkToSendBroadcasts = true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0098  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void restoreAndApplyStagedSessionIfNeeded() {
        /*
            Method dump skipped, instruction units count: 386
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageInstallerService.restoreAndApplyStagedSessionIfNeeded():void");
    }

    private void reconcileStagesLocked(java.lang.String volumeUuid) {
        android.util.ArraySet<java.io.File> unclaimedStages = getStagingDirsOnVolume(volumeUuid);
        for (int i = 0; i < this.mSessions.size(); i++) {
            com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
            unclaimedStages.remove(session.stageDir);
        }
        removeStagingDirs(unclaimedStages);
    }

    private android.util.ArraySet<java.io.File> getStagingDirsOnVolume(java.lang.String volumeUuid) {
        java.io.File stagingDir = getTmpSessionDir(volumeUuid);
        android.util.ArraySet<java.io.File> stagingDirs = newArraySet(stagingDir.listFiles(sStageFilter));
        java.io.File stagedSessionStagingDir = android.os.Environment.getDataStagingDirectory(volumeUuid);
        stagingDirs.addAll(newArraySet(stagedSessionStagingDir.listFiles()));
        return stagingDirs;
    }

    private void removeStagingDirs(android.util.ArraySet<java.io.File> stagingDirsToRemove) {
        for (java.io.File stage : stagingDirsToRemove) {
            android.util.Slog.w(TAG, "Deleting orphan stage " + stage);
            this.mPm.removeCodePath(stage);
        }
    }

    public void onPrivateVolumeMounted(java.lang.String volumeUuid) {
        synchronized (this.mSessions) {
            reconcileStagesLocked(volumeUuid);
        }
    }

    public void freeStageDirs(java.lang.String volumeUuid) {
        android.util.ArraySet<java.io.File> unclaimedStagingDirsOnVolume = getStagingDirsOnVolume(volumeUuid);
        long currentTimeMillis = java.lang.System.currentTimeMillis();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
                if (unclaimedStagingDirsOnVolume.contains(session.stageDir)) {
                    long age = currentTimeMillis - session.createdMillis;
                    if (age >= MAX_SESSION_AGE_ON_LOW_STORAGE_MILLIS) {
                        com.android.server.pm.PackageInstallerSession root = !session.hasParentSessionId() ? session : this.mSessions.get(session.getParentSessionId());
                        if (root == null) {
                            android.util.Slog.e(TAG, "freeStageDirs: found an orphaned session: " + session.sessionId + " parent=" + session.getParentSessionId());
                        } else if (!root.isDestroyed()) {
                            root.abandon();
                        }
                    } else {
                        unclaimedStagingDirsOnVolume.remove(session.stageDir);
                    }
                }
            }
        }
        removeStagingDirs(unclaimedStagingDirsOnVolume);
    }

    @java.lang.Deprecated
    public java.io.File allocateStageDirLegacy(java.lang.String volumeUuid, boolean isEphemeral) throws java.io.IOException {
        java.io.File sessionStageDir;
        synchronized (this.mSessions) {
            try {
                try {
                    int sessionId = allocateSessionIdLocked();
                    this.mLegacySessions.put(sessionId, true);
                    sessionStageDir = buildTmpSessionDir(sessionId, volumeUuid);
                    prepareStageDir(sessionStageDir);
                } catch (java.lang.IllegalStateException e) {
                    throw new java.io.IOException(e);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return sessionStageDir;
    }

    @java.lang.Deprecated
    public java.lang.String allocateExternalStageCidLegacy() {
        java.lang.String str;
        synchronized (this.mSessions) {
            int sessionId = allocateSessionIdLocked();
            this.mLegacySessions.put(sessionId, true);
            str = "smdl" + sessionId + ".tmp";
        }
        return str;
    }

    private void readSessionsLocked() {
        if (LOGD) {
            android.util.Slog.v(TAG, "readSessionsLocked()");
        }
        this.mSessions.clear();
        java.io.FileInputStream fis = null;
        try {
            try {
                try {
                    fis = this.mSessionsFile.openRead();
                    com.android.modules.utils.TypedXmlPullParser in = android.util.Xml.resolvePullParser(fis);
                    while (true) {
                        int type = in.next();
                        if (type == 1) {
                            break;
                        }
                        if (type == 2) {
                            java.lang.String tag = in.getName();
                            if ("session".equals(tag)) {
                                try {
                                    com.android.server.pm.PackageInstallerSession session = com.android.server.pm.PackageInstallerSession.readFromXml(in, this.mInternalCallback, this.mContext, this.mPm, this.mInstallThread.getLooper(), this.mStagingManager, this.mSessionsDir, this, this.mSilentUpdatePolicy);
                                    this.mSessions.put(session.sessionId, session);
                                    this.mAllocatedSessions.put(session.sessionId, true);
                                } catch (java.lang.Exception e) {
                                    android.util.Slog.e(TAG, "Could not read session", e);
                                }
                            }
                        }
                    }
                } catch (java.io.FileNotFoundException e2) {
                }
            } catch (java.io.IOException | java.lang.ArrayIndexOutOfBoundsException | org.xmlpull.v1.XmlPullParserException e3) {
                android.util.Slog.wtf(TAG, "Failed reading install sessions", e3);
            }
            libcore.io.IoUtils.closeQuietly(fis);
            for (int i = 0; i < this.mSessions.size(); i++) {
                this.mSessions.valueAt(i).onAfterSessionRead(this.mSessions);
            }
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly(fis);
            throw th;
        }
    }

    private void expireSessionsLocked() {
        boolean valid;
        android.util.SparseArray<com.android.server.pm.PackageInstallerSession> tmp = this.mSessions.clone();
        int n = tmp.size();
        for (int i = 0; i < n; i++) {
            com.android.server.pm.PackageInstallerSession session = tmp.valueAt(i);
            if (!session.hasParentSessionId()) {
                long age = java.lang.System.currentTimeMillis() - session.createdMillis;
                long timeSinceUpdate = java.lang.System.currentTimeMillis() - session.getUpdatedMillis();
                if (session.isStaged()) {
                    valid = !session.isStagedAndInTerminalState() || timeSinceUpdate < MAX_TIME_SINCE_UPDATE_MILLIS;
                } else if (age >= MAX_AGE_MILLIS) {
                    android.util.Slog.w(TAG, "Abandoning old session created at " + session.createdMillis);
                    valid = false;
                } else {
                    valid = true;
                }
                if (!valid) {
                    android.util.Slog.w(TAG, "Remove old session: " + session.sessionId);
                    removeActiveSession(session);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeActiveSession(com.android.server.pm.PackageInstallerSession session) {
        this.mSessions.remove(session.sessionId);
        addHistoricalSessionLocked(session);
        for (com.android.server.pm.PackageInstallerSession child : session.getChildSessions()) {
            this.mSessions.remove(child.sessionId);
            addHistoricalSessionLocked(child);
        }
    }

    private void addHistoricalSessionLocked(com.android.server.pm.PackageInstallerSession session) {
        if (this.mHistoricalSessions.size() > 5000) {
            android.util.Slog.d(TAG, "Historical sessions size reaches threshold, clear the oldest");
            this.mHistoricalSessions.subList(0, 500).clear();
        }
        this.mHistoricalSessions.add(session.createHistoricalSession());
        int installerUid = session.getInstallerUid();
        this.mHistoricalSessionsByInstaller.put(installerUid, this.mHistoricalSessionsByInstaller.get(installerUid) + 1);
    }

    private boolean writeSessions() {
        com.android.server.pm.PackageInstallerSession[] sessions;
        if (LOGD) {
            android.util.Slog.v(TAG, "writeSessions()");
        }
        synchronized (this.mSessions) {
            int size = this.mSessions.size();
            sessions = new com.android.server.pm.PackageInstallerSession[size];
            for (int i = 0; i < size; i++) {
                sessions[i] = this.mSessions.valueAt(i);
            }
        }
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mSessionsFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, TAG_SESSIONS);
            for (com.android.server.pm.PackageInstallerSession session : sessions) {
                session.write(out, this.mSessionsDir);
            }
            out.endTag((java.lang.String) null, TAG_SESSIONS);
            out.endDocument();
            this.mSessionsFile.finishWrite(fos);
            return true;
        } catch (java.io.IOException e) {
            if (fos != null) {
                this.mSessionsFile.failWrite(fos);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.io.File buildAppIconFile(int sessionId) {
        return new java.io.File(this.mSessionsDir, "app_icon." + sessionId + ".png");
    }

    public int createSession(android.content.pm.PackageInstaller.SessionParams params, java.lang.String installerPackageName, java.lang.String callingAttributionTag, int userId) {
        try {
            if (params.dataLoaderParams != null && this.mContext.checkCallingOrSelfPermission("com.android.permission.USE_INSTALLER_V2") != 0) {
                throw new java.lang.SecurityException("You need the com.android.permission.USE_INSTALLER_V2 permission to use a data loader");
            }
            params.installFlags &= -536870913;
            return createSessionInternal(params, installerPackageName, callingAttributionTag, android.os.Binder.getCallingUid(), userId);
        } catch (java.io.IOException e) {
            throw android.util.ExceptionUtils.wrap(e);
        }
    }

    int createSessionInternal(android.content.pm.PackageInstaller.SessionParams params, java.lang.String installerPackageName, java.lang.String installerAttributionTag, int callingUid, int userId) throws java.io.IOException {
        java.lang.String installerPackageName2;
        java.lang.String originatingPackageName;
        int requestedInstallerPackageUid;
        int activeCount;
        java.io.File stageDir;
        java.lang.String stageCid;
        com.android.server.pm.pkg.PackageStateInternal ps;
        java.lang.String[] packages;
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, true, "createSession");
        if (this.mPm.isUserRestricted(userId, "no_install_apps")) {
            throw new java.lang.SecurityException("User restriction prevents installing");
        }
        if (params.installReason == 5 && this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ROLLBACKS") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS") != 0) {
            throw new java.lang.SecurityException("INSTALL_REASON_ROLLBACK requires the MANAGE_ROLLBACKS permission or the TEST_MANAGE_ROLLBACKS permission");
        }
        if (params.appPackageName != null && !isValidPackageName(params.appPackageName)) {
            if (params.appPackageName.contains("sota_app-")) {
                android.util.Slog.e(TAG, "sota_app ignore ValidPackageName");
            } else {
                params.appPackageName = null;
            }
        }
        params.appLabel = (java.lang.String) android.text.TextUtils.trimToSize(params.appLabel, 1000);
        if (params.installerPackageName != null && !isValidPackageName(params.installerPackageName)) {
            params.installerPackageName = null;
        }
        java.lang.String installerPackageName3 = (installerPackageName == null || isValidPackageName(installerPackageName)) ? installerPackageName : null;
        java.lang.String requestedInstallerPackageName = params.installerPackageName != null ? params.installerPackageName : installerPackageName3;
        if (com.android.server.pm.PackageManagerServiceUtils.isRootOrShell(callingUid) || com.android.server.pm.PackageInstallerSession.isSystemDataLoaderInstallation(params) || com.android.server.pm.PackageManagerServiceUtils.isAdoptedShell(android.os.UserHandle.getAppId(callingUid), this.mContext)) {
            params.installFlags |= 32;
            installerPackageName2 = "com.android.shell";
        } else {
            if (callingUid != 1000) {
                this.mAppOps.checkPackage(callingUid, installerPackageName3);
            }
            if (!android.text.TextUtils.equals(requestedInstallerPackageName, installerPackageName3) && this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") != 0) {
                this.mAppOps.checkPackage(callingUid, requestedInstallerPackageName);
            }
            params.installFlags &= -33;
            if (!this.mServiceExt.skipRemoveInstallAllUsersFlag(params.installFlags)) {
                params.installFlags &= -65;
            }
            params.installFlags &= -134217729;
            params.installFlags |= 2;
            if ((params.installFlags & 65536) != 0 && !this.mPm.isCallerVerifier(snapshot, callingUid)) {
                params.installFlags &= -65537;
            }
            if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_TEST_ONLY_PACKAGE") != 0 && !this.mServiceExt.skipRemoveInstallAllUsersFlag(params.installFlags)) {
                params.installFlags &= -5;
            }
            params.developmentInstallFlags = 0;
            installerPackageName2 = installerPackageName3;
        }
        if (params.originatingUid == -1 || params.originatingUid == callingUid || (packages = snapshot.getPackagesForUid(params.originatingUid)) == null || packages.length <= 0) {
            originatingPackageName = null;
        } else {
            java.lang.String originatingPackageName2 = packages[0];
            originatingPackageName = originatingPackageName2;
        }
        if (android.os.Build.IS_DEBUGGABLE || com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot(callingUid)) {
            params.installFlags |= 1048576;
        } else {
            params.installFlags &= -1048577;
        }
        if (this.mDisableVerificationForUid != -1) {
            if (callingUid == this.mDisableVerificationForUid) {
                params.installFlags |= 524288;
            } else {
                params.installFlags &= -524289;
            }
            this.mDisableVerificationForUid = -1;
        } else if ((params.installFlags & 36) != 36) {
            params.installFlags &= -524289;
        }
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.rollbackLifetime()) {
            if (params.rollbackLifetimeMillis > 0) {
                if ((params.installFlags & 262144) == 0) {
                    throw new java.lang.IllegalArgumentException("Can't set rollbackLifetimeMillis when rollback is not enabled");
                }
                if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ROLLBACKS") != 0) {
                    throw new java.lang.SecurityException("Setting rollback lifetime requires the MANAGE_ROLLBACKS permission");
                }
            } else if (params.rollbackLifetimeMillis < 0) {
                throw new java.lang.IllegalArgumentException("rollbackLifetimeMillis can't be negative.");
            }
        }
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection()) {
            if (params.rollbackImpactLevel == 1 || params.rollbackImpactLevel == 2) {
                if ((262144 & params.installFlags) == 0) {
                    throw new java.lang.IllegalArgumentException("Can't set rollbackImpactLevel when rollback is not enabled");
                }
                if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ROLLBACKS") != 0) {
                    throw new java.lang.SecurityException("Setting rollbackImpactLevel requires the MANAGE_ROLLBACKS permission");
                }
            } else if (params.rollbackImpactLevel < 0) {
                throw new java.lang.IllegalArgumentException("rollbackImpactLevel can't be negative.");
            }
        }
        boolean isApex = (params.installFlags & 131072) != 0;
        if (isApex) {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGE_UPDATES") == -1 && this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == -1) {
                throw new java.lang.SecurityException("Not allowed to perform APEX updates");
            }
        } else if (params.isStaged) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INSTALL_PACKAGES", TAG);
        }
        if (isApex) {
            if (!this.mApexManager.isApexSupported()) {
                throw new java.lang.IllegalArgumentException("This device doesn't support the installation of APEX files");
            }
            if (params.isMultiPackage) {
                throw new java.lang.IllegalArgumentException("A multi-session can't be set as APEX.");
            }
            if (com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) || this.mBypassNextAllowedApexUpdateCheck) {
                params.installFlags |= 8388608;
            } else {
                params.installFlags &= -8388609;
            }
        }
        if ((params.installFlags & 16777216) != 0 && !com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && !android.os.Build.IS_DEBUGGABLE && !com.android.server.pm.PackageManagerServiceUtils.isAdoptedShell(callingUid, this.mContext)) {
            params.installFlags &= -16777217;
        }
        params.installFlags &= -1073741825;
        if (com.android.server.pm.PackageArchiver.isArchivingEnabled() && params.appPackageName != null && (ps = this.mPm.snapshotComputer().getPackageStateInternal(params.appPackageName, 1000)) != null && com.android.server.pm.PackageArchiver.isArchived(ps.getUserStateOrDefault(userId)) && com.android.server.pm.PackageArchiver.getResponsibleInstallerPackage(ps).equals(requestedInstallerPackageName)) {
            params.installFlags |= 1073741824;
        }
        if ((params.installFlags & 2048) != 0 && !com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && (snapshot.getFlagsForUid(callingUid) & 1) == 0) {
            throw new java.lang.SecurityException("Only system apps could use the PackageManager.INSTALL_INSTANT_APP flag.");
        }
        if (params.isStaged && !com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && !this.mBypassNextStagedInstallerCheck && !isStagedInstallerAllowed(requestedInstallerPackageName)) {
            throw new java.lang.SecurityException("Installer not allowed to commit staged install");
        }
        if (isApex && !com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && !this.mBypassNextStagedInstallerCheck && !isStagedInstallerAllowed(requestedInstallerPackageName)) {
            throw new java.lang.SecurityException("Installer not allowed to commit non-staged APEX install");
        }
        this.mBypassNextStagedInstallerCheck = false;
        this.mBypassNextAllowedApexUpdateCheck = false;
        if (!params.isMultiPackage) {
            boolean hasInstallGrantRuntimePermissions = this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS") == 0;
            if ((params.installFlags & 256) != 0 && !hasInstallGrantRuntimePermissions) {
                throw new java.lang.SecurityException("You need the android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS permission to use the PackageManager.INSTALL_GRANT_ALL_REQUESTED_PERMISSIONS flag");
            }
            android.util.ArrayMap<java.lang.String, java.lang.Integer> permissionStates = params.getPermissionStates();
            if (!permissionStates.isEmpty() && !hasInstallGrantRuntimePermissions) {
                for (int index = 0; index < permissionStates.size(); index++) {
                    java.lang.String permissionName = permissionStates.keyAt(index);
                    if (!INSTALLER_CHANGEABLE_APP_OP_PERMISSIONS.contains(permissionName)) {
                        throw new java.lang.SecurityException("You need the android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS permission to grant runtime permissions for a session");
                    }
                }
            }
            if (params.appIcon != null) {
                android.app.ActivityManager am = (android.app.ActivityManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
                int iconSize = am.getLauncherLargeIconSize();
                if (params.appIcon.getWidth() > iconSize * 2 || params.appIcon.getHeight() > iconSize * 2) {
                    params.appIcon = android.graphics.Bitmap.createScaledBitmap(params.appIcon, iconSize, iconSize, true);
                }
            }
            switch (params.mode) {
                case 1:
                case 2:
                    if ((params.installFlags & 16) != 0) {
                        if (!com.android.internal.content.InstallLocationUtils.fitsOnInternal(this.mContext, params)) {
                            throw new java.io.IOException("No suitable internal storage available");
                        }
                    } else if ((params.installFlags & 512) != 0) {
                        params.installFlags |= 16;
                    } else {
                        params.installFlags |= 16;
                        long ident = android.os.Binder.clearCallingIdentity();
                        try {
                            params.volumeUuid = com.android.internal.content.InstallLocationUtils.resolveInstallVolume(this.mContext, params);
                        } finally {
                            android.os.Binder.restoreCallingIdentity(ident);
                        }
                    }
                    break;
                default:
                    throw new java.lang.IllegalArgumentException("Invalid install mode: " + params.mode);
            }
        }
        if (requestedInstallerPackageName != null) {
            int requestedInstallerPackageUid2 = snapshot.getPackageUid(requestedInstallerPackageName, 0L, userId);
            requestedInstallerPackageUid = requestedInstallerPackageUid2;
        } else {
            requestedInstallerPackageUid = -1;
        }
        java.lang.String requestedInstallerPackageName2 = requestedInstallerPackageUid == -1 ? null : requestedInstallerPackageName;
        synchronized (this.mSessions) {
            try {
                try {
                    activeCount = getSessionCount(this.mSessions, callingUid);
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
            if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == 0) {
                if (activeCount >= MAX_ACTIVE_SESSIONS_WITH_PERMISSION) {
                    try {
                        throw new java.lang.IllegalStateException("Too many active sessions for UID " + callingUid);
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                }
                throw th;
            }
            if (activeCount >= MAX_ACTIVE_SESSIONS_NO_PERMISSION) {
                throw new java.lang.IllegalStateException("Too many active sessions for UID " + callingUid);
            }
            int historicalCount = this.mHistoricalSessionsByInstaller.get(callingUid);
            if (historicalCount < MAX_HISTORICAL_SESSIONS) {
                try {
                    int existingDraftSessionId = getExistingDraftSessionId(requestedInstallerPackageUid, params, userId);
                    int sessionId = existingDraftSessionId != -1 ? existingDraftSessionId : allocateSessionIdLocked();
                    long createdMillis = java.lang.System.currentTimeMillis();
                    if (params.isMultiPackage) {
                        stageDir = null;
                        stageCid = null;
                    } else if ((params.installFlags & 16) != 0) {
                        java.io.File stageDir2 = buildSessionDir(sessionId, params);
                        stageDir = stageDir2;
                        stageCid = null;
                    } else {
                        java.lang.String stageCid2 = buildExternalStageCid(sessionId);
                        stageDir = null;
                        stageCid = stageCid2;
                    }
                    if (params.forceQueryableOverride && !com.android.server.pm.PackageManagerServiceUtils.isRootOrShell(callingUid)) {
                        params.forceQueryableOverride = false;
                    }
                    android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
                    if (dpmi != null && dpmi.isUserOrganizationManaged(userId)) {
                        params.installFlags |= 67108864;
                    }
                    if (isApex || this.mContext.checkCallingOrSelfPermission("android.permission.ENFORCE_UPDATE_OWNERSHIP") == -1) {
                        params.installFlags &= -33554433;
                    }
                    com.android.server.pm.InstallSource installSource = com.android.server.pm.InstallSource.create(installerPackageName2, originatingPackageName, requestedInstallerPackageName2, requestedInstallerPackageUid, requestedInstallerPackageName2, installerAttributionTag, params.packageSource);
                    int sessionId2 = sessionId;
                    com.android.server.pm.PackageInstallerSession session = new com.android.server.pm.PackageInstallerSession(this.mInternalCallback, this.mContext, this.mPm, this, this.mSilentUpdatePolicy, this.mInstallThread.getLooper(), this.mStagingManager, sessionId2, userId, callingUid, installSource, params, createdMillis, 0L, stageDir, stageCid, null, null, false, false, false, false, null, -1, false, false, false, 0, "", null);
                    synchronized (this.mSessions) {
                        try {
                            try {
                                this.mSessions.put(sessionId2, session);
                                this.mPm.addInstallerPackageName(session.getInstallSource());
                                this.mCallbacks.notifySessionCreated(session.sessionId, session.userId);
                                this.mSettingsWriteRequest.schedule();
                                if (LOGD) {
                                    android.util.Slog.d(TAG, "Created session id=" + sessionId2 + " staged=" + params.isStaged);
                                }
                                return sessionId2;
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                while (true) {
                                    try {
                                        throw th;
                                    } catch (java.lang.Throwable th5) {
                                        th = th5;
                                    }
                                }
                            }
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                        }
                    }
                } catch (java.lang.Throwable th7) {
                    th = th7;
                }
            } else {
                try {
                    throw new java.lang.IllegalStateException("Too many historical sessions for UID " + callingUid);
                } catch (java.lang.Throwable th8) {
                    th = th8;
                }
            }
            throw th;
        }
    }

    int getExistingDraftSessionId(int installerUid, android.content.pm.PackageInstaller.SessionParams sessionParams, int userId) {
        int existingDraftSessionIdInternal;
        synchronized (this.mSessions) {
            existingDraftSessionIdInternal = getExistingDraftSessionIdInternal(installerUid, sessionParams, userId);
        }
        return existingDraftSessionIdInternal;
    }

    private int getExistingDraftSessionIdInternal(int installerUid, android.content.pm.PackageInstaller.SessionParams sessionParams, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        java.lang.String appPackageName = sessionParams.appPackageName;
        if (!com.android.server.pm.PackageArchiver.isArchivingEnabled() || installerUid == -1 || appPackageName == null || (ps = this.mPm.snapshotComputer().getPackageStateInternal(appPackageName, 1000)) == null || !com.android.server.pm.PackageArchiver.isArchived(ps.getUserStateOrDefault(userId))) {
            return -1;
        }
        if (sessionParams.unarchiveId > 0) {
            com.android.server.pm.PackageInstallerSession session = this.mSessions.get(sessionParams.unarchiveId);
            if (session == null || !isValidDraftSession(session, appPackageName, installerUid, userId)) {
                return -1;
            }
            return session.sessionId;
        }
        for (int i = 0; i < this.mSessions.size(); i++) {
            com.android.server.pm.PackageInstallerSession session2 = this.mSessions.valueAt(i);
            if (session2 != null && isValidDraftSession(session2, appPackageName, installerUid, userId)) {
                return session2.sessionId;
            }
        }
        return -1;
    }

    private boolean isValidDraftSession(com.android.server.pm.PackageInstallerSession session, java.lang.String appPackageName, int installerUid, int userId) {
        return (session.getInstallFlags() & 536870912) != 0 && appPackageName.equals(session.params.appPackageName) && session.userId == userId && installerUid == session.getInstallerUid();
    }

    void cleanupDraftIfUnclaimed(int sessionId) {
        synchronized (this.mSessions) {
            com.android.server.pm.PackageInstallerSession session = this.mPm.mInstallerService.getSession(sessionId);
            if (session != null && (session.getInstallFlags() & 536870912) != 0) {
                session.abandon();
            }
        }
    }

    private boolean isStagedInstallerAllowed(java.lang.String installerName) {
        return com.android.server.SystemConfig.getInstance().getWhitelistedStagedInstallers().contains(installerName);
    }

    public void updateSessionAppIcon(int sessionId, android.graphics.Bitmap appIcon) {
        synchronized (this.mSessions) {
            com.android.server.pm.PackageInstallerSession session = this.mSessions.get(sessionId);
            if (session == null || !isCallingUidOwner(session)) {
                throw new java.lang.SecurityException("Caller has no access to session " + sessionId);
            }
            if (appIcon != null) {
                android.app.ActivityManager am = (android.app.ActivityManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
                int iconSize = am.getLauncherLargeIconSize();
                if (appIcon.getWidth() > iconSize * 2 || appIcon.getHeight() > iconSize * 2) {
                    appIcon = android.graphics.Bitmap.createScaledBitmap(appIcon, iconSize, iconSize, true);
                }
            }
            session.params.appIcon = appIcon;
            session.params.appIconLastModified = -1L;
            this.mInternalCallback.onSessionBadgingChanged(session);
        }
    }

    public void updateSessionAppLabel(int sessionId, java.lang.String appLabel) {
        synchronized (this.mSessions) {
            com.android.server.pm.PackageInstallerSession session = this.mSessions.get(sessionId);
            if (session == null || !isCallingUidOwner(session)) {
                throw new java.lang.SecurityException("Caller has no access to session " + sessionId);
            }
            if (!appLabel.equals(session.params.appLabel)) {
                session.params.appLabel = appLabel;
                this.mInternalCallback.onSessionBadgingChanged(session);
            }
        }
    }

    public void abandonSession(int sessionId) {
        synchronized (this.mSessions) {
            com.android.server.pm.PackageInstallerSession session = this.mSessions.get(sessionId);
            if (this.mServiceExt.canForceAbandonMainlineSession(session)) {
                session.abandon();
            } else {
                if (session == null || !isCallingUidOwner(session)) {
                    throw new java.lang.SecurityException("Caller has no access to session " + sessionId);
                }
                session.abandon();
            }
        }
    }

    public android.content.pm.IPackageInstallerSession openSession(int sessionId) {
        try {
            this.mPm.mPackageManagerServiceSocExt.setInstallationBoost(true);
            return openSessionInternal(sessionId);
        } catch (java.io.IOException e) {
            throw android.util.ExceptionUtils.wrap(e);
        }
    }

    private boolean checkOpenSessionAccess(com.android.server.pm.PackageInstallerSession session) {
        if (session == null || (session.getInstallFlags() & 536870912) != 0) {
            return false;
        }
        if (isCallingUidOwner(session)) {
            return true;
        }
        return session.isSealed() && this.mContext.checkCallingOrSelfPermission("android.permission.PACKAGE_VERIFICATION_AGENT") == 0;
    }

    private com.android.server.pm.PackageInstallerSession openSessionInternal(int sessionId) throws java.io.IOException {
        com.android.server.pm.PackageInstallerSession session;
        synchronized (this.mSessions) {
            session = this.mSessions.get(sessionId);
            if (!checkOpenSessionAccess(session)) {
                throw new java.lang.SecurityException("Caller has no access to session " + sessionId);
            }
            session.open();
        }
        return session;
    }

    private int allocateSessionIdLocked() {
        int n = 0;
        while (true) {
            int sessionId = this.mRandom.nextInt(2147483646) + 1;
            if (!this.mAllocatedSessions.get(sessionId, false)) {
                this.mAllocatedSessions.put(sessionId, true);
                return sessionId;
            }
            int n2 = n + 1;
            if (n >= 32) {
                throw new java.lang.IllegalStateException("Failed to allocate session ID");
            }
            n = n2;
        }
    }

    static boolean isStageName(java.lang.String name) {
        boolean isFile = name.startsWith("vmdl") && name.endsWith(".tmp");
        boolean isContainer = name.startsWith("smdl") && name.endsWith(".tmp");
        boolean isLegacyContainer = name.startsWith("smdl2tmp");
        return isFile || isContainer || isLegacyContainer;
    }

    static int tryParseSessionId(java.lang.String tmpSessionDir) throws java.lang.IllegalArgumentException {
        if (!tmpSessionDir.startsWith("vmdl") || !tmpSessionDir.endsWith(".tmp")) {
            throw new java.lang.IllegalArgumentException("Not a temporary session directory");
        }
        java.lang.String sessionId = tmpSessionDir.substring("vmdl".length(), tmpSessionDir.length() - ".tmp".length());
        return java.lang.Integer.parseInt(sessionId);
    }

    private static boolean isValidPackageName(java.lang.String packageName) {
        if (packageName.length() > 255) {
            return false;
        }
        java.lang.String errorMessage = android.content.pm.parsing.FrameworkParsingPackageUtils.validateName(packageName, false, true);
        return errorMessage == null;
    }

    private java.io.File getTmpSessionDir(java.lang.String volumeUuid) {
        return android.os.Environment.getDataAppDirectory(volumeUuid);
    }

    private java.io.File buildTmpSessionDir(int sessionId, java.lang.String volumeUuid) {
        java.io.File sessionStagingDir = getTmpSessionDir(volumeUuid);
        return new java.io.File(sessionStagingDir, "vmdl" + sessionId + ".tmp");
    }

    private java.io.File buildSessionDir(int sessionId, android.content.pm.PackageInstaller.SessionParams params) {
        if (params.isStaged || (params.installFlags & 131072) != 0) {
            java.io.File sessionStagingDir = android.os.Environment.getDataStagingDirectory(params.volumeUuid);
            return new java.io.File(sessionStagingDir, "session_" + sessionId);
        }
        java.io.File result = buildTmpSessionDir(sessionId, params.volumeUuid);
        if (DEBUG && !java.util.Objects.equals(java.lang.Integer.valueOf(tryParseSessionId(result.getName())), java.lang.Integer.valueOf(sessionId))) {
            throw new java.lang.RuntimeException("session folder format is off: " + result.getName() + " (" + sessionId + ")");
        }
        return result;
    }

    static void prepareStageDir(java.io.File stageDir) throws java.io.IOException {
        if (stageDir.exists()) {
            throw new java.io.IOException("Session dir already exists: " + stageDir);
        }
        try {
            android.system.Os.mkdir(stageDir.getAbsolutePath(), 509);
            android.system.Os.chmod(stageDir.getAbsolutePath(), 509);
            if (!android.os.SELinux.restorecon(stageDir)) {
                java.lang.String path = stageDir.getCanonicalPath();
                java.lang.String ctx = android.os.SELinux.fileSelabelLookup(path);
                boolean success = android.os.SELinux.setFileContext(path, ctx);
                android.util.Slog.e(TAG, "Failed to SELinux.restorecon session dir, path: [" + path + "], ctx: [" + ctx + "]. Retrying via SELinux.fileSelabelLookup/SELinux.setFileContext: " + (success ? "SUCCESS" : "FAILURE"));
                if (!success) {
                    throw new java.io.IOException("Failed to restorecon session dir: " + stageDir);
                }
            }
        } catch (android.system.ErrnoException e) {
            throw new java.io.IOException("Failed to prepare session dir: " + stageDir, e);
        }
    }

    private java.lang.String buildExternalStageCid(int sessionId) {
        return "smdl" + sessionId + ".tmp";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: shouldFilterSession, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public boolean lambda$getStagedSessions$1(com.android.server.pm.Computer snapshot, int uid, android.content.pm.PackageInstaller.SessionInfo info) {
        return (info == null || uid == info.getInstallerUid() || snapshot.canQueryPackage(uid, info.getAppPackageName())) ? false : true;
    }

    public android.content.pm.PackageInstaller.SessionInfo getSessionInfo(int sessionId) {
        android.content.pm.PackageInstaller.SessionInfo sessionInfoGenerateInfoForCaller;
        android.content.pm.PackageInstaller.SessionInfo result;
        int callingUid = android.os.Binder.getCallingUid();
        synchronized (this.mSessions) {
            com.android.server.pm.PackageInstallerSession session = this.mSessions.get(sessionId);
            if (session != null && (!session.isStaged() || !session.isDestroyed())) {
                sessionInfoGenerateInfoForCaller = session.generateInfoForCaller(true, callingUid);
            } else {
                sessionInfoGenerateInfoForCaller = null;
            }
            result = sessionInfoGenerateInfoForCaller;
        }
        if (lambda$getStagedSessions$1(this.mPm.snapshotComputer(), callingUid, result)) {
            return null;
        }
        return result;
    }

    public android.content.pm.ParceledListSlice<android.content.pm.PackageInstaller.SessionInfo> getStagedSessions() {
        final int callingUid = android.os.Binder.getCallingUid();
        java.util.List<android.content.pm.PackageInstaller.SessionInfo> result = new java.util.ArrayList<>();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
                if (session.isStaged() && !session.isDestroyed()) {
                    result.add(session.generateInfoForCaller(false, callingUid));
                }
            }
        }
        final com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        result.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getStagedSessions$1(snapshot, callingUid, (android.content.pm.PackageInstaller.SessionInfo) obj);
            }
        });
        return new android.content.pm.ParceledListSlice<>(result);
    }

    public android.content.pm.ParceledListSlice<android.content.pm.PackageInstaller.SessionInfo> getAllSessions(int userId) {
        final int callingUid = android.os.Binder.getCallingUid();
        final com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        int targetUserId = this.mServiceExt.changeUserIdIfNeed(userId, callingUid, snapshot);
        snapshot.enforceCrossUserPermission(callingUid, targetUserId, true, false, "getAllSessions");
        java.util.List<android.content.pm.PackageInstaller.SessionInfo> result = new java.util.ArrayList<>();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
                if (session.userId == userId && !session.hasParentSessionId() && (!session.isStaged() || !session.isDestroyed())) {
                    result.add(session.generateInfoForCaller(false, callingUid));
                }
            }
        }
        result.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getAllSessions$2(snapshot, callingUid, (android.content.pm.PackageInstaller.SessionInfo) obj);
            }
        });
        return new android.content.pm.ParceledListSlice<>(result);
    }

    public android.content.pm.ParceledListSlice<android.content.pm.PackageInstaller.SessionInfo> getMySessions(java.lang.String installerPackageName, int userId) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        int callingUid = android.os.Binder.getCallingUid();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, false, "getMySessions");
        this.mAppOps.checkPackage(callingUid, installerPackageName);
        java.util.List<android.content.pm.PackageInstaller.SessionInfo> result = new java.util.ArrayList<>();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
                android.content.pm.PackageInstaller.SessionInfo info = session.generateInfoForCaller(false, 1000);
                if (java.util.Objects.equals(info.getInstallerPackageName(), installerPackageName) && session.userId == userId && !session.hasParentSessionId() && isCallingUidOwner(session) && (session.getInstallFlags() & 536870912) == 0) {
                    result.add(info);
                }
            }
        }
        return new android.content.pm.ParceledListSlice<>(result);
    }

    android.content.pm.ParceledListSlice<android.content.pm.PackageInstaller.SessionInfo> getHistoricalSessions(int userId) {
        final int callingUid = android.os.Binder.getCallingUid();
        final com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, false, "getAllSessions");
        java.util.List<android.content.pm.PackageInstaller.SessionInfo> result = new java.util.ArrayList<>();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mHistoricalSessions.size(); i++) {
                com.android.server.pm.PackageInstallerHistoricalSession session = this.mHistoricalSessions.get(i);
                if (userId == -1 || session.userId == userId) {
                    result.add(session.generateInfo());
                }
            }
        }
        result.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getHistoricalSessions$3(snapshot, callingUid, (android.content.pm.PackageInstaller.SessionInfo) obj);
            }
        });
        return new android.content.pm.ParceledListSlice<>(result);
    }

    public void uninstall(android.content.pm.VersionedPackage versionedPackage, java.lang.String callerPackageName, int flags, android.content.IntentSender statusReceiver, int userId) {
        uninstall(versionedPackage, callerPackageName, flags, statusReceiver, userId, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
    }

    void uninstall(android.content.pm.VersionedPackage versionedPackage, java.lang.String callerPackageName, int flags, android.content.IntentSender statusReceiver, int userId, int callingUid, int callingPid) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, true, "uninstall");
        if (!com.android.server.pm.PackageManagerServiceUtils.isRootOrShell(callingUid)) {
            this.mAppOps.checkPackage(callingUid, callerPackageName);
        }
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        boolean canSilentlyInstallPackage = (dpmi != null && dpmi.canSilentlyInstallPackage(callerPackageName, callingUid)) || com.android.server.pm.PackageInstallerSession.isEmergencyInstallerEnabled(versionedPackage.getPackageName(), snapshot, userId, callingUid);
        com.android.server.pm.PackageInstallerService.PackageDeleteObserverAdapter adapter = new com.android.server.pm.PackageInstallerService.PackageDeleteObserverAdapter(this.mContext, statusReceiver, versionedPackage.getPackageName(), canSilentlyInstallPackage, userId, this.mPackageArchiver, flags);
        if (this.mContext.checkPermission("android.permission.DELETE_PACKAGES", callingPid, callingUid) == 0) {
            this.mPm.deletePackageVersioned(versionedPackage, adapter.getBinder(), userId, flags);
            return;
        }
        if (canSilentlyInstallPackage) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                this.mPm.deletePackageVersioned(versionedPackage, adapter.getBinder(), userId, flags);
                android.os.Binder.restoreCallingIdentity(ident);
                android.app.admin.DevicePolicyEventLogger.createEvent(113).setAdmin(callerPackageName).write();
                return;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        }
        android.content.pm.ApplicationInfo appInfo = snapshot.getApplicationInfo(callerPackageName, 0L, userId);
        if (appInfo.targetSdkVersion >= 28) {
            this.mContext.enforcePermission("android.permission.REQUEST_DELETE_PACKAGES", callingPid, callingUid, null);
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.UNINSTALL_PACKAGE");
        intent.setData(android.net.Uri.fromParts("package", versionedPackage.getPackageName(), null));
        intent.putExtra("android.content.pm.extra.CALLBACK", (android.os.Parcelable) new android.content.pm.PackageManager.UninstallCompleteCallback(adapter.getBinder().asBinder()));
        if ((flags & 16) != 0) {
            intent.putExtra("android.content.pm.extra.DELETE_FLAGS", flags);
        }
        adapter.onUserActionRequired(intent);
    }

    public void uninstallExistingPackage(android.content.pm.VersionedPackage versionedPackage, java.lang.String callerPackageName, android.content.IntentSender statusReceiver, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        this.mContext.enforceCallingOrSelfPermission("android.permission.DELETE_PACKAGES", null);
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, true, "uninstall");
        if (!com.android.server.pm.PackageManagerServiceUtils.isRootOrShell(callingUid)) {
            this.mAppOps.checkPackage(callingUid, callerPackageName);
        }
        com.android.server.pm.PackageInstallerService.PackageDeleteObserverAdapter adapter = new com.android.server.pm.PackageInstallerService.PackageDeleteObserverAdapter(this.mContext, statusReceiver, versionedPackage.getPackageName(), false, userId);
        this.mPm.deleteExistingPackageAsUser(versionedPackage, adapter.getBinder(), userId);
    }

    public void installExistingPackage(java.lang.String packageName, int installFlags, int installReason, android.content.IntentSender statusReceiver, int userId, java.util.List<java.lang.String> allowListedPermissions) {
        android.util.Pair<java.lang.Integer, android.content.IntentSender> result = this.mPm.installExistingPackageAsUser(packageName, userId, installFlags, installReason, allowListedPermissions, statusReceiver);
        int returnCode = ((java.lang.Integer) result.first).intValue();
        android.content.IntentSender onCompleteSender = (android.content.IntentSender) result.second;
        if (onCompleteSender != null) {
            com.android.server.pm.InstallPackageHelper.onInstallComplete(returnCode, this.mContext, onCompleteSender);
        }
    }

    public void setPermissionsResult(int sessionId, boolean accepted) {
        setPermissionsResult_enforcePermission();
        synchronized (this.mSessions) {
            com.android.server.pm.PackageInstallerSession session = this.mSessions.get(sessionId);
            if (session != null) {
                session.setPermissionsResult(accepted);
            }
        }
    }

    private boolean isValidForInstallConstraints(com.android.server.pm.pkg.PackageStateInternal ps, java.lang.String installerPackageName, int installerUid, java.lang.String packageName) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        boolean isSelfUpdatePermissionGranted = snapshot.checkUidPermission("android.permission.INSTALL_SELF_UPDATES", installerUid) == 0;
        boolean isSelfUpdateAllowed = isSelfUpdatePermissionGranted && android.text.TextUtils.equals(packageName, installerPackageName);
        return android.text.TextUtils.equals(ps.getInstallSource().mInstallerPackageName, installerPackageName) || android.text.TextUtils.equals(ps.getInstallSource().mUpdateOwnerPackageName, installerPackageName) || isSelfUpdateAllowed;
    }

    private java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> checkInstallConstraintsInternal(java.lang.String installerPackageName, java.util.List<java.lang.String> packageNames, android.content.pm.PackageInstaller.InstallConstraints constraints, long timeoutMillis) {
        java.util.Objects.requireNonNull(packageNames);
        java.util.Objects.requireNonNull(constraints);
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        int callingUid = android.os.Binder.getCallingUid();
        java.lang.String callingPackageName = snapshot.getNameForUid(callingUid);
        if (!android.text.TextUtils.equals(callingPackageName, installerPackageName)) {
            throw new java.lang.SecurityException("The installerPackageName set by the caller doesn't match the caller's own package name.");
        }
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid)) {
            for (java.lang.String packageName : packageNames) {
                com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateInternal(packageName);
                if (ps == null || !isValidForInstallConstraints(ps, installerPackageName, callingUid, packageName)) {
                    throw new java.lang.SecurityException("Caller has no access to package " + packageName);
                }
            }
        }
        return this.mGentleUpdateHelper.checkInstallConstraints(packageNames, constraints, timeoutMillis);
    }

    public void checkInstallConstraints(java.lang.String installerPackageName, java.util.List<java.lang.String> packageNames, android.content.pm.PackageInstaller.InstallConstraints constraints, final android.os.RemoteCallback callback) {
        java.util.Objects.requireNonNull(callback);
        java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> future = checkInstallConstraintsInternal(installerPackageName, packageNames, constraints, 0L);
        future.thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.PackageInstallerService.lambda$checkInstallConstraints$4(callback, (android.content.pm.PackageInstaller.InstallConstraintsResult) obj);
            }
        });
    }

    static /* synthetic */ void lambda$checkInstallConstraints$4(android.os.RemoteCallback callback, android.content.pm.PackageInstaller.InstallConstraintsResult result) {
        android.os.Bundle b = new android.os.Bundle();
        b.putParcelable("result", result);
        callback.sendResult(b);
    }

    public void waitForInstallConstraints(java.lang.String installerPackageName, final java.util.List<java.lang.String> packageNames, final android.content.pm.PackageInstaller.InstallConstraints constraints, final android.content.IntentSender callback, long timeoutMillis) {
        java.util.Objects.requireNonNull(callback);
        if (timeoutMillis < 0 || timeoutMillis > 604800000) {
            throw new java.lang.IllegalArgumentException("Invalid timeoutMillis=" + timeoutMillis);
        }
        java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> future = checkInstallConstraintsInternal(installerPackageName, packageNames, constraints, timeoutMillis);
        future.thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$waitForInstallConstraints$5(packageNames, constraints, callback, (android.content.pm.PackageInstaller.InstallConstraintsResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$waitForInstallConstraints$5(java.util.List packageNames, android.content.pm.PackageInstaller.InstallConstraints constraints, android.content.IntentSender callback, android.content.pm.PackageInstaller.InstallConstraintsResult result) {
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.intent.extra.PACKAGES", (java.lang.String[]) packageNames.toArray(new java.lang.String[0]));
        intent.putExtra("android.content.pm.extra.INSTALL_CONSTRAINTS", constraints);
        intent.putExtra("android.content.pm.extra.INSTALL_CONSTRAINTS_RESULT", result);
        try {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setPendingIntentBackgroundActivityLaunchAllowed(false);
            callback.sendIntent(this.mContext, 0, intent, null, null, null, options.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
        }
    }

    public void registerCallback(android.content.pm.IPackageInstallerCallback callback, final int userId) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        snapshot.enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, true, false, "registerCallback");
        registerCallback(callback, new java.util.function.IntPredicate() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda5
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return com.android.server.pm.PackageInstallerService.lambda$registerCallback$6(userId, i);
            }
        });
    }

    static /* synthetic */ boolean lambda$registerCallback$6(int userId, int eventUserId) {
        return userId == eventUserId;
    }

    public void registerCallback(android.content.pm.IPackageInstallerCallback callback, java.util.function.IntPredicate userCheck) {
        this.mCallbacks.register(callback, new com.android.server.pm.PackageInstallerService.BroadcastCookie(android.os.Binder.getCallingUid(), userCheck));
    }

    public void unregisterCallback(android.content.pm.IPackageInstallerCallback callback) {
        this.mCallbacks.unregister(callback);
    }

    @Override // com.android.server.pm.PackageSessionProvider
    public com.android.server.pm.PackageInstallerSession getSession(int sessionId) {
        com.android.server.pm.PackageInstallerSession packageInstallerSession;
        synchronized (this.mSessions) {
            packageInstallerSession = this.mSessions.get(sessionId);
        }
        return packageInstallerSession;
    }

    @Override // com.android.server.pm.PackageSessionProvider
    public com.android.server.pm.PackageSessionVerifier getSessionVerifier() {
        return this.mSessionVerifier;
    }

    @Override // com.android.server.pm.PackageSessionProvider
    public com.android.server.pm.GentleUpdateHelper getGentleUpdateHelper() {
        return this.mGentleUpdateHelper;
    }

    public void bypassNextStagedInstallerCheck(boolean value) {
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Caller not allowed to bypass staged installer check");
        }
        this.mBypassNextStagedInstallerCheck = value;
    }

    public void bypassNextAllowedApexUpdateCheck(boolean value) {
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Caller not allowed to bypass allowed apex update check");
        }
        this.mBypassNextAllowedApexUpdateCheck = value;
    }

    public void disableVerificationForUid(int uid) {
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Operation not allowed for caller");
        }
        this.mDisableVerificationForUid = uid;
    }

    public void setAllowUnlimitedSilentUpdates(java.lang.String installerPackageName) {
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Caller not allowed to unlimite silent updates");
        }
        this.mSilentUpdatePolicy.setAllowUnlimitedSilentUpdates(installerPackageName);
    }

    public void setSilentUpdatesThrottleTime(long throttleTimeInSeconds) {
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Caller not allowed to set silent updates throttle time");
        }
        this.mSilentUpdatePolicy.setSilentUpdatesThrottleTime(throttleTimeInSeconds);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public void requestArchive(java.lang.String packageName, java.lang.String callerPackageName, int flags, android.content.IntentSender intentSender, android.os.UserHandle userHandle) throws android.os.ParcelableException {
        this.mPackageArchiver.requestArchive(packageName, callerPackageName, flags, intentSender, userHandle);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public void requestUnarchive(java.lang.String packageName, java.lang.String callerPackageName, android.content.IntentSender statusReceiver, android.os.UserHandle userHandle) throws android.os.ParcelableException {
        this.mPackageArchiver.requestUnarchive(packageName, callerPackageName, statusReceiver, userHandle);
    }

    public void installPackageArchived(final android.content.pm.ArchivedPackageParcel archivedPackageParcel, final android.content.pm.PackageInstaller.SessionParams params, final android.content.IntentSender statusReceiver, final java.lang.String installerPackageName, android.os.UserHandle userHandle) {
        java.util.Objects.requireNonNull(params);
        java.util.Objects.requireNonNull(archivedPackageParcel);
        java.util.Objects.requireNonNull(statusReceiver);
        java.util.Objects.requireNonNull(installerPackageName);
        java.util.Objects.requireNonNull(userHandle);
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("Requested archived install of package %s for user %s.", new java.lang.Object[]{archivedPackageParcel.packageName, java.lang.Integer.valueOf(userHandle.getIdentifier())}));
        int callingUid = android.os.Binder.getCallingUid();
        final int userId = userHandle.getIdentifier();
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, true, "installPackageArchived");
        if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") != 0) {
            throw new java.lang.SecurityException("You need the com.android.permission.INSTALL_PACKAGES permission to request archived package install");
        }
        params.installFlags |= 134217728;
        if (params.dataLoaderParams != null) {
            throw new java.lang.IllegalArgumentException("Incompatible session param: dataLoaderParams has to be null");
        }
        params.setDataLoaderParams(com.android.server.pm.PackageManagerShellCommandDataLoader.getStreamingDataLoaderParams(null));
        final com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata metadata = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forArchived(archivedPackageParcel);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$installPackageArchived$7(params, installerPackageName, userId, metadata, statusReceiver, archivedPackageParcel);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$installPackageArchived$7(android.content.pm.PackageInstaller.SessionParams r12, java.lang.String r13, int r14, com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata r15, android.content.IntentSender r16, android.content.pm.ArchivedPackageParcel r17) throws java.lang.Exception {
        /*
            r11 = this;
            r1 = 0
            int r6 = android.os.Binder.getCallingUid()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54
            r5 = 0
            r2 = r11
            r3 = r12
            r4 = r13
            r7 = r14
            int r0 = r2.createSessionInternal(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54
            r2 = r11
            com.android.server.pm.PackageInstallerSession r3 = r11.openSessionInternal(r0)     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b
            r1 = r3
            java.lang.String r6 = "base"
            byte[] r9 = r15.toByteArray()     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b
            r5 = 0
            r7 = 0
            r10 = 0
            r4 = r1
            r4.addFile(r5, r6, r7, r9, r10)     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b
            r3 = 0
            r4 = r16
            r1.commit(r4, r3)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
            java.lang.String r3 = "PackageInstaller"
            java.lang.String r5 = "Installed archived app %s."
            r6 = r17
            java.lang.String r7 = r6.packageName     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L5f
            java.lang.Object[] r7 = new java.lang.Object[]{r7}     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L5f
            java.lang.String r5 = android.text.TextUtils.formatSimple(r5, r7)     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L5f
            android.util.Slog.i(r3, r5)     // Catch: java.io.IOException -> L43 java.lang.Throwable -> L5f
            if (r1 == 0) goto L42
            r1.close()
        L42:
            return
        L43:
            r0 = move-exception
            goto L5a
        L45:
            r0 = move-exception
            goto L51
        L47:
            r0 = move-exception
            goto L58
        L49:
            r0 = move-exception
            goto L4f
        L4b:
            r0 = move-exception
            goto L56
        L4d:
            r0 = move-exception
            r2 = r11
        L4f:
            r4 = r16
        L51:
            r6 = r17
            goto L60
        L54:
            r0 = move-exception
            r2 = r11
        L56:
            r4 = r16
        L58:
            r6 = r17
        L5a:
            java.lang.RuntimeException r3 = android.util.ExceptionUtils.wrap(r0)     // Catch: java.lang.Throwable -> L5f
            throw r3     // Catch: java.lang.Throwable -> L5f
        L5f:
            r0 = move-exception
        L60:
            if (r1 == 0) goto L65
            r1.close()
        L65:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageInstallerService.lambda$installPackageArchived$7(android.content.pm.PackageInstaller$SessionParams, java.lang.String, int, com.android.server.pm.PackageManagerShellCommandDataLoader$Metadata, android.content.IntentSender, android.content.pm.ArchivedPackageParcel):void");
    }

    public void reportUnarchivalStatus(int unarchiveId, int status, long requiredStorageBytes, android.app.PendingIntent userActionIntent, android.os.UserHandle userHandle) throws java.lang.Throwable {
        verifyReportUnarchiveStatusInput(status, requiredStorageBytes, userActionIntent, userHandle);
        int userId = userHandle.getIdentifier();
        int binderUid = android.os.Binder.getCallingUid();
        synchronized (this.mSessions) {
            try {
                try {
                    com.android.server.pm.PackageInstallerSession session = this.mSessions.get(unarchiveId);
                    if (session == null || session.userId != userId || session.params.appPackageName == null) {
                        throw new android.os.ParcelableException(new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("No valid session with unarchival ID %s found for user %s.", new java.lang.Object[]{java.lang.Integer.valueOf(unarchiveId), java.lang.Integer.valueOf(userId)})));
                    }
                    if (!isCallingUidOwner(session)) {
                        throw new java.lang.SecurityException(android.text.TextUtils.formatSimple("The caller UID %s does not have access to the session with unarchiveId %d.", new java.lang.Object[]{java.lang.Integer.valueOf(binderUid), java.lang.Integer.valueOf(unarchiveId)}));
                    }
                    session.reportUnarchivalStatus(status, unarchiveId, requiredStorageBytes, userActionIntent);
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

    private static void verifyReportUnarchiveStatusInput(int status, long requiredStorageBytes, android.app.PendingIntent userActionIntent, android.os.UserHandle userHandle) {
        java.util.Objects.requireNonNull(userHandle);
        if (status == 1) {
            java.util.Objects.requireNonNull(userActionIntent);
        }
        if (status == 2 && requiredStorageBytes <= 0) {
            throw new java.lang.IllegalStateException("Insufficient storage error set, but requiredStorageBytes unspecified.");
        }
        if (status != 2 && requiredStorageBytes > 0) {
            throw new java.lang.IllegalStateException(android.text.TextUtils.formatSimple("requiredStorageBytes set, but error is %s.", new java.lang.Object[]{java.lang.Integer.valueOf(status)}));
        }
        if (!java.util.List.of(0, 1, 2, 3, 4, 5, 100).contains(java.lang.Integer.valueOf(status))) {
            throw new java.lang.IllegalStateException("Invalid status code passed " + status);
        }
    }

    private static int getSessionCount(android.util.SparseArray<com.android.server.pm.PackageInstallerSession> sessions, int installerUid) {
        int count = 0;
        int size = sessions.size();
        for (int i = 0; i < size; i++) {
            com.android.server.pm.PackageInstallerSession session = sessions.valueAt(i);
            if (session.getInstallerUid() == installerUid) {
                count++;
            }
        }
        return count;
    }

    private boolean isCallingUidOwner(com.android.server.pm.PackageInstallerSession session) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 0) {
            return true;
        }
        return session != null && callingUid == session.getInstallerUid();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldFilterSession(com.android.server.pm.Computer snapshot, int uid, int sessionId) {
        com.android.server.pm.PackageInstallerSession session = getSession(sessionId);
        return (session == null || uid == session.getInstallerUid() || snapshot.canQueryPackage(uid, session.getPackageName())) ? false : true;
    }

    static class PackageDeleteObserverAdapter extends android.app.PackageDeleteObserver {
        private final android.content.Context mContext;
        private final int mFlags;
        private final android.app.Notification mNotification;
        private final com.android.server.pm.PackageArchiver mPackageArchiver;
        private final java.lang.String mPackageName;
        private final android.content.IntentSender mTarget;
        private final int mUserId;

        PackageDeleteObserverAdapter(android.content.Context context, android.content.IntentSender target, java.lang.String packageName, boolean showNotification, int userId) {
            this(context, target, packageName, showNotification, userId, null, 0);
        }

        PackageDeleteObserverAdapter(android.content.Context context, android.content.IntentSender target, java.lang.String packageName, boolean showNotification, int userId, com.android.server.pm.PackageArchiver packageArchiver, int flags) {
            this.mContext = context;
            this.mTarget = target;
            this.mPackageName = packageName;
            if (showNotification) {
                this.mNotification = com.android.server.pm.PackageInstallerService.buildSuccessNotification(this.mContext, getDeviceOwnerDeletedPackageMsg(), packageName, userId);
            } else {
                this.mNotification = null;
            }
            this.mUserId = userId;
            this.mPackageArchiver = packageArchiver;
            this.mFlags = flags;
        }

        private java.lang.String getDeviceOwnerDeletedPackageMsg() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService(android.app.admin.DevicePolicyManager.class);
                return dpm.getResources().getString("Core.PACKAGE_DELETED_BY_DO", new java.util.function.Supplier() { // from class: com.android.server.pm.PackageInstallerService$PackageDeleteObserverAdapter$$ExternalSyntheticLambda0
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.lambda$getDeviceOwnerDeletedPackageMsg$0();
                    }
                });
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.lang.String lambda$getDeviceOwnerDeletedPackageMsg$0() {
            return this.mContext.getString(android.R.string.notification_title);
        }

        public void onUserActionRequired(android.content.Intent intent) {
            if (this.mTarget == null) {
                return;
            }
            android.content.Intent fillIn = new android.content.Intent();
            fillIn.putExtra("android.content.pm.extra.PACKAGE_NAME", this.mPackageName);
            fillIn.putExtra("android.content.pm.extra.STATUS", -1);
            fillIn.putExtra("android.intent.extra.INTENT", intent);
            try {
                android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
                options.setPendingIntentBackgroundActivityLaunchAllowed(false);
                this.mTarget.sendIntent(this.mContext, 0, fillIn, null, null, null, options.toBundle());
            } catch (android.content.IntentSender.SendIntentException e) {
            }
        }

        public void onPackageDeleted(java.lang.String basePackageName, int returnCode, java.lang.String msg) {
            if (1 == returnCode && this.mNotification != null) {
                android.app.NotificationManager notificationManager = (android.app.NotificationManager) this.mContext.getSystemService("notification");
                notificationManager.notify(basePackageName, 21, this.mNotification);
            }
            if (this.mPackageArchiver != null && 1 != returnCode && (this.mFlags & 16) != 0) {
                this.mPackageArchiver.clearArchiveState(this.mPackageName, this.mUserId);
            }
            if (this.mTarget == null) {
                return;
            }
            android.content.Intent fillIn = new android.content.Intent();
            fillIn.putExtra("android.content.pm.extra.PACKAGE_NAME", this.mPackageName);
            fillIn.putExtra("android.content.pm.extra.STATUS", android.content.pm.PackageManager.deleteStatusToPublicStatus(returnCode));
            fillIn.putExtra("android.content.pm.extra.STATUS_MESSAGE", android.content.pm.PackageManager.deleteStatusToString(returnCode, msg));
            fillIn.putExtra("android.content.pm.extra.LEGACY_STATUS", returnCode);
            try {
                android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
                options.setPendingIntentBackgroundActivityLaunchAllowed(false);
                this.mTarget.sendIntent(this.mContext, 0, fillIn, null, null, null, options.toBundle());
            } catch (android.content.IntentSender.SendIntentException e) {
            }
        }
    }

    static android.app.Notification buildSuccessNotification(android.content.Context context, java.lang.String contentText, java.lang.String basePackageName, int userId) {
        android.content.pm.PackageInfo packageInfo = null;
        try {
            packageInfo = android.app.AppGlobals.getPackageManager().getPackageInfo(basePackageName, 67108864L, userId);
        } catch (android.os.RemoteException e) {
        }
        if (packageInfo == null || packageInfo.applicationInfo == null) {
            android.util.Slog.w(TAG, "Notification not built for package: " + basePackageName);
            return null;
        }
        android.content.pm.PackageManager pm = context.getPackageManager();
        android.graphics.Bitmap packageIcon = com.android.internal.util.ImageUtils.buildScaledBitmap(packageInfo.applicationInfo.loadIcon(pm), context.getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width), context.getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height));
        java.lang.CharSequence packageLabel = packageInfo.applicationInfo.loadLabel(pm);
        return new android.app.Notification.Builder(context, com.android.internal.notification.SystemNotificationChannels.DEVICE_ADMIN).setSmallIcon(android.R.drawable.ic_accessibility_reduce_bright_colors).setColor(context.getResources().getColor(android.R.color.system_notification_accent_color)).setContentTitle(packageLabel).setContentText(contentText).setStyle(new android.app.Notification.BigTextStyle().bigText(contentText)).setLargeIcon(packageIcon).build();
    }

    public static <E> android.util.ArraySet<E> newArraySet(E... elements) {
        android.util.ArraySet<E> set = new android.util.ArraySet<>();
        if (elements != null) {
            set.ensureCapacity(elements.length);
            java.util.Collections.addAll(set, elements);
        }
        return set;
    }

    private static final class BroadcastCookie {
        public final int callingUid;
        public final java.util.function.IntPredicate userCheck;

        BroadcastCookie(int callingUid, java.util.function.IntPredicate userCheck) {
            this.callingUid = callingUid;
            this.userCheck = userCheck;
        }
    }

    private class Callbacks extends android.os.Handler {
        private static final int MSG_SESSION_ACTIVE_CHANGED = 3;
        private static final int MSG_SESSION_BADGING_CHANGED = 2;
        private static final int MSG_SESSION_CREATED = 1;
        private static final int MSG_SESSION_FINISHED = 5;
        private static final int MSG_SESSION_PROGRESS_CHANGED = 4;
        private final android.os.RemoteCallbackList<android.content.pm.IPackageInstallerCallback> mCallbacks;

        public Callbacks(android.os.Looper looper) {
            super(looper);
            this.mCallbacks = new android.os.RemoteCallbackList<>();
        }

        public void register(android.content.pm.IPackageInstallerCallback callback, com.android.server.pm.PackageInstallerService.BroadcastCookie cookie) {
            this.mCallbacks.register(callback, cookie);
        }

        public void unregister(android.content.pm.IPackageInstallerCallback callback) {
            this.mCallbacks.unregister(callback);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            int sessionId = msg.arg1;
            int userId = msg.arg2;
            int n = this.mCallbacks.beginBroadcast();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageInstallerService.this.mPm.snapshotComputer();
            for (int i = 0; i < n; i++) {
                android.content.pm.IPackageInstallerCallback callback = (android.content.pm.IPackageInstallerCallback) this.mCallbacks.getBroadcastItem(i);
                com.android.server.pm.PackageInstallerService.BroadcastCookie cookie = (com.android.server.pm.PackageInstallerService.BroadcastCookie) this.mCallbacks.getBroadcastCookie(i);
                if (cookie.userCheck.test(userId) && !com.android.server.pm.PackageInstallerService.this.shouldFilterSession(snapshot, cookie.callingUid, sessionId)) {
                    try {
                        invokeCallback(callback, msg);
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
            this.mCallbacks.finishBroadcast();
        }

        private void invokeCallback(android.content.pm.IPackageInstallerCallback callback, android.os.Message msg) throws android.os.RemoteException {
            int sessionId = msg.arg1;
            switch (msg.what) {
                case 1:
                    callback.onSessionCreated(sessionId);
                    break;
                case 2:
                    callback.onSessionBadgingChanged(sessionId);
                    break;
                case 3:
                    callback.onSessionActiveChanged(sessionId, ((java.lang.Boolean) msg.obj).booleanValue());
                    break;
                case 4:
                    callback.onSessionProgressChanged(sessionId, ((java.lang.Float) msg.obj).floatValue());
                    break;
                case 5:
                    callback.onSessionFinished(sessionId, ((java.lang.Boolean) msg.obj).booleanValue());
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionCreated(int sessionId, int userId) {
            obtainMessage(1, sessionId, userId).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionBadgingChanged(int sessionId, int userId) {
            obtainMessage(2, sessionId, userId).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionActiveChanged(int sessionId, int userId, boolean active) {
            obtainMessage(3, sessionId, userId, java.lang.Boolean.valueOf(active)).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionProgressChanged(int sessionId, int userId, float progress) {
            obtainMessage(4, sessionId, userId, java.lang.Float.valueOf(progress)).sendToTarget();
        }

        public void notifySessionFinished(int sessionId, int userId, boolean success) {
            obtainMessage(5, sessionId, userId, java.lang.Boolean.valueOf(success)).sendToTarget();
        }
    }

    static class ParentChildSessionMap {
        private final java.util.Comparator<com.android.server.pm.PackageInstallerSession> mSessionCreationComparator = java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.pm.PackageInstallerService$ParentChildSessionMap$$ExternalSyntheticLambda0
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(java.lang.Object obj) {
                return com.android.server.pm.PackageInstallerService.ParentChildSessionMap.lambda$new$0((com.android.server.pm.PackageInstallerSession) obj);
            }
        }).thenComparingInt(new java.util.function.ToIntFunction() { // from class: com.android.server.pm.PackageInstallerService$ParentChildSessionMap$$ExternalSyntheticLambda1
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return com.android.server.pm.PackageInstallerService.ParentChildSessionMap.lambda$new$1((com.android.server.pm.PackageInstallerSession) obj);
            }
        });
        private final java.util.TreeMap<com.android.server.pm.PackageInstallerSession, java.util.TreeSet<com.android.server.pm.PackageInstallerSession>> mSessionMap = new java.util.TreeMap<>(this.mSessionCreationComparator);

        static /* synthetic */ long lambda$new$0(com.android.server.pm.PackageInstallerSession sess) {
            if (sess != null) {
                return sess.createdMillis;
            }
            return -1L;
        }

        static /* synthetic */ int lambda$new$1(com.android.server.pm.PackageInstallerSession sess) {
            if (sess != null) {
                return sess.sessionId;
            }
            return -1;
        }

        ParentChildSessionMap() {
        }

        boolean containsSession() {
            return !this.mSessionMap.isEmpty();
        }

        private void addParentSession(com.android.server.pm.PackageInstallerSession session) {
            if (!this.mSessionMap.containsKey(session)) {
                this.mSessionMap.put(session, new java.util.TreeSet<>(this.mSessionCreationComparator));
            }
        }

        private void addChildSession(com.android.server.pm.PackageInstallerSession session, com.android.server.pm.PackageInstallerSession parentSession) {
            addParentSession(parentSession);
            this.mSessionMap.get(parentSession).add(session);
        }

        void addSession(com.android.server.pm.PackageInstallerSession session, com.android.server.pm.PackageInstallerSession parentSession) {
            if (session.hasParentSessionId()) {
                addChildSession(session, parentSession);
            } else {
                addParentSession(session);
            }
        }

        void dump(java.lang.String tag, com.android.internal.util.IndentingPrintWriter pw) {
            pw.println(tag + " install sessions:");
            pw.increaseIndent();
            for (java.util.Map.Entry<com.android.server.pm.PackageInstallerSession, java.util.TreeSet<com.android.server.pm.PackageInstallerSession>> entry : this.mSessionMap.entrySet()) {
                com.android.server.pm.PackageInstallerSession parentSession = entry.getKey();
                if (parentSession != null) {
                    pw.print(tag + " ");
                    parentSession.dump(pw);
                    pw.println();
                    pw.increaseIndent();
                }
                for (com.android.server.pm.PackageInstallerSession childSession : entry.getValue()) {
                    pw.print(tag + " Child ");
                    childSession.dump(pw);
                    pw.println();
                }
                pw.decreaseIndent();
            }
            pw.println();
            pw.decreaseIndent();
        }
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        com.android.server.pm.PackageInstallerSession rootSession;
        synchronized (this.mSessions) {
            com.android.server.pm.PackageInstallerService.ParentChildSessionMap activeSessionMap = new com.android.server.pm.PackageInstallerService.ParentChildSessionMap();
            com.android.server.pm.PackageInstallerService.ParentChildSessionMap orphanedChildSessionMap = new com.android.server.pm.PackageInstallerService.ParentChildSessionMap();
            com.android.server.pm.PackageInstallerService.ParentChildSessionMap finalizedSessionMap = new com.android.server.pm.PackageInstallerService.ParentChildSessionMap();
            int N = this.mSessions.size();
            for (int i = 0; i < N; i++) {
                com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
                if (session.hasParentSessionId()) {
                    rootSession = getSession(session.getParentSessionId());
                } else {
                    rootSession = session;
                }
                if (rootSession == null) {
                    orphanedChildSessionMap.addSession(session, rootSession);
                } else if (rootSession.isStagedAndInTerminalState()) {
                    finalizedSessionMap.addSession(session, rootSession);
                } else {
                    activeSessionMap.addSession(session, rootSession);
                }
            }
            activeSessionMap.dump("Active", pw);
            if (orphanedChildSessionMap.containsSession()) {
                orphanedChildSessionMap.dump("Orphaned", pw);
            }
            finalizedSessionMap.dump("Finalized", pw);
            pw.println("Historical install sessions:");
            pw.increaseIndent();
            int N2 = this.mHistoricalSessions.size();
            for (int i2 = 0; i2 < N2; i2++) {
                this.mHistoricalSessions.get(i2).dump(pw);
                pw.println();
            }
            pw.println();
            pw.decreaseIndent();
            pw.println("Legacy install sessions:");
            pw.increaseIndent();
            pw.println(this.mLegacySessions.toString());
            pw.println();
            pw.decreaseIndent();
        }
        this.mSilentUpdatePolicy.dump(pw);
        this.mGentleUpdateHelper.dump(pw);
    }

    public class InternalCallback {
        public InternalCallback() {
        }

        public void onSessionBadgingChanged(com.android.server.pm.PackageInstallerSession session) {
            com.android.server.pm.PackageInstallerService.this.mCallbacks.notifySessionBadgingChanged(session.sessionId, session.userId);
            com.android.server.pm.PackageInstallerService.this.mSettingsWriteRequest.schedule();
        }

        public void onSessionActiveChanged(com.android.server.pm.PackageInstallerSession session, boolean active) {
            com.android.server.pm.PackageInstallerService.this.mCallbacks.notifySessionActiveChanged(session.sessionId, session.userId, active);
        }

        public void onSessionProgressChanged(com.android.server.pm.PackageInstallerSession session, float progress) {
            com.android.server.pm.PackageInstallerService.this.mCallbacks.notifySessionProgressChanged(session.sessionId, session.userId, progress);
        }

        public void onSessionChanged(com.android.server.pm.PackageInstallerSession session) {
            session.markUpdated();
            com.android.server.pm.PackageInstallerService.this.mSettingsWriteRequest.schedule();
            if (com.android.server.pm.PackageInstallerService.this.mOkToSendBroadcasts && !session.isDestroyed() && session.isStaged()) {
                com.android.server.pm.PackageInstallerService.this.sendSessionUpdatedBroadcast(session.generateInfoForCaller(false, 1000), session.userId);
            }
        }

        public void onSessionFinished(final com.android.server.pm.PackageInstallerSession session, final boolean success) {
            if (success) {
                com.android.server.pm.PackageInstallerService.this.mCallbacks.notifySessionFinished(session.sessionId, session.userId, success);
            }
            com.android.server.pm.PackageInstallerService.this.mInstallHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageInstallerService.InternalCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    if (session.isStaged() && !success) {
                        com.android.server.pm.PackageInstallerService.this.mStagingManager.abortSession(session.mStagedSession);
                    }
                    synchronized (com.android.server.pm.PackageInstallerService.this.mSessions) {
                        if (!session.hasParentSessionId()) {
                            boolean shouldRemove = (session.isStaged() && !session.isDestroyed() && session.isCommitted()) ? false : true;
                            if (shouldRemove) {
                                com.android.server.pm.PackageInstallerService.this.removeActiveSession(session);
                            }
                        }
                        java.io.File appIconFile = com.android.server.pm.PackageInstallerService.this.buildAppIconFile(session.sessionId);
                        if (appIconFile.exists()) {
                            appIconFile.delete();
                        }
                        com.android.server.pm.PackageInstallerService.this.mSettingsWriteRequest.runNow();
                    }
                    if (!success) {
                        com.android.server.pm.PackageInstallerService.this.mCallbacks.notifySessionFinished(session.sessionId, session.userId, success);
                    }
                }
            });
        }

        public void onSessionPrepared(com.android.server.pm.PackageInstallerSession session) {
            com.android.server.pm.PackageInstallerService.this.mSettingsWriteRequest.schedule();
        }

        public void onSessionSealedBlocking(com.android.server.pm.PackageInstallerSession session) {
            com.android.server.pm.PackageInstallerService.this.mSettingsWriteRequest.runNow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSessionUpdatedBroadcast(android.content.pm.PackageInstaller.SessionInfo sessionInfo, int userId) {
        if (android.text.TextUtils.isEmpty(sessionInfo.installerPackageName)) {
            return;
        }
        android.content.Intent sessionUpdatedIntent = new android.content.Intent("android.content.pm.action.SESSION_UPDATED").putExtra("android.content.pm.extra.SESSION", sessionInfo).setPackage(sessionInfo.installerPackageName);
        this.mContext.sendBroadcastAsUser(sessionUpdatedIntent, android.os.UserHandle.of(userId));
    }

    void onInstallerPackageDeleted(int installerAppId, int userId) {
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                com.android.server.pm.PackageInstallerSession session = this.mSessions.valueAt(i);
                if (matchesInstaller(session, installerAppId, userId)) {
                    com.android.server.pm.PackageInstallerSession root = !session.hasParentSessionId() ? session : this.mSessions.get(session.getParentSessionId());
                    if (root != null && matchesInstaller(root, installerAppId, userId) && !root.isDestroyed()) {
                        root.abandon();
                    }
                }
            }
        }
    }

    private boolean matchesInstaller(com.android.server.pm.PackageInstallerSession session, int installerAppId, int userId) {
        int installerUid = session.getInstallerUid();
        return installerAppId == -1 ? android.os.UserHandle.getAppId(installerUid) == installerAppId : android.os.UserHandle.getUid(userId, installerAppId) == installerUid;
    }
}
