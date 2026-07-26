package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class StagingManager {
    private static final java.lang.String TAG = "StagingManager";
    private final com.android.server.pm.ApexManager mApexManager;
    private final java.util.concurrent.CompletableFuture<java.lang.Void> mBootCompleted;
    private final android.content.Context mContext;
    private final java.util.List<java.lang.String> mFailedPackageNames;
    private java.lang.String mFailureReason;
    private final java.io.File mFailureReasonFile;
    private java.lang.String mNativeFailureReason;
    private final android.os.PowerManager mPowerManager;
    private final java.util.List<android.content.pm.IStagedApexObserver> mStagedApexObservers;
    private final android.util.SparseArray<com.android.server.pm.StagingManager.StagedSession> mStagedSessions;
    public final com.android.server.pm.IStagingManagerExt mStagingManageExt;
    private final java.util.List<java.lang.Integer> mSuccessfulStagedSessionIds;

    interface StagedSession {
        void abandon();

        boolean containsApexSession();

        boolean containsApkSession();

        java.util.List<com.android.server.pm.StagingManager.StagedSession> getChildSessions();

        long getCommittedMillis();

        java.lang.String getPackageName();

        int getParentSessionId();

        boolean hasParentSessionId();

        java.util.concurrent.CompletableFuture<java.lang.Void> installSession();

        boolean isApexSession();

        boolean isCommitted();

        boolean isDestroyed();

        boolean isInTerminalState();

        boolean isMultiPackage();

        boolean isSessionApplied();

        boolean isSessionFailed();

        boolean isSessionReady();

        boolean sessionContains(java.util.function.Predicate<com.android.server.pm.StagingManager.StagedSession> predicate);

        int sessionId();

        android.content.pm.PackageInstaller.SessionParams sessionParams();

        void setSessionApplied();

        void setSessionFailed(int i, java.lang.String str);

        void setSessionReady();

        void verifySession();
    }

    StagingManager(android.content.Context context) {
        this(context, com.android.server.pm.ApexManager.getInstance());
    }

    StagingManager(android.content.Context context, com.android.server.pm.ApexManager apexManager) {
        this.mFailureReasonFile = new java.io.File("/metadata/staged-install/failure_reason.txt");
        this.mStagedSessions = new android.util.SparseArray<>();
        this.mFailedPackageNames = new java.util.ArrayList();
        this.mSuccessfulStagedSessionIds = new java.util.ArrayList();
        this.mStagedApexObservers = new java.util.ArrayList();
        this.mBootCompleted = new java.util.concurrent.CompletableFuture<>();
        this.mStagingManageExt = (com.android.server.pm.IStagingManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IStagingManagerExt.class).base(this).create();
        this.mContext = context;
        this.mApexManager = apexManager;
        this.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
        if (this.mFailureReasonFile.exists()) {
            try {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(this.mFailureReasonFile));
                try {
                    this.mFailureReason = reader.readLine();
                    reader.close();
                } finally {
                }
            } catch (java.lang.Exception e) {
            }
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private static com.android.server.pm.StagingManager sStagingManager;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        void startService(com.android.server.pm.StagingManager stagingManager) {
            sStagingManager = stagingManager;
            ((com.android.server.SystemServiceManager) com.android.server.LocalServices.getService(com.android.server.SystemServiceManager.class)).startService(this);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 1000 && sStagingManager != null) {
                sStagingManager.markStagedSessionsAsSuccessful();
                sStagingManager.markBootCompleted();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markBootCompleted() {
        this.mApexManager.markBootCompleted();
    }

    void registerStagedApexObserver(final android.content.pm.IStagedApexObserver observer) {
        if (observer == null) {
            return;
        }
        if (observer.asBinder() != null) {
            try {
                observer.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.pm.StagingManager.1
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        synchronized (com.android.server.pm.StagingManager.this.mStagedApexObservers) {
                            com.android.server.pm.StagingManager.this.mStagedApexObservers.remove(observer);
                        }
                    }
                }, 0);
            } catch (android.os.RemoteException re) {
                android.util.Slog.w(TAG, re.getMessage());
            }
        }
        synchronized (this.mStagedApexObservers) {
            this.mStagedApexObservers.add(observer);
        }
    }

    void unregisterStagedApexObserver(android.content.pm.IStagedApexObserver observer) {
        synchronized (this.mStagedApexObservers) {
            this.mStagedApexObservers.remove(observer);
        }
    }

    private void abortCheckpoint(java.lang.String failureReason, boolean supportsCheckpoint, boolean needsCheckpoint) {
        java.io.BufferedWriter writer;
        android.util.Slog.e(TAG, failureReason);
        if (supportsCheckpoint) {
            try {
                if (needsCheckpoint) {
                    try {
                        writer = new java.io.BufferedWriter(new java.io.FileWriter(this.mFailureReasonFile));
                    } catch (java.lang.Exception e) {
                        android.util.Slog.w(TAG, "Failed to save failure reason: ", e);
                    }
                    try {
                        writer.write(failureReason);
                        writer.close();
                        if (this.mApexManager.isApexSupported()) {
                            this.mApexManager.revertActiveSessions();
                        }
                        com.android.internal.content.InstallLocationUtils.getStorageManager().abortChanges("abort-staged-install", false);
                    } finally {
                    }
                }
            } catch (java.lang.Exception e2) {
                android.util.Slog.wtf(TAG, "Failed to abort checkpoint", e2);
                if (this.mApexManager.isApexSupported()) {
                    this.mApexManager.revertActiveSessions();
                }
                this.mPowerManager.reboot(null);
            }
        }
    }

    private java.util.List<com.android.server.pm.StagingManager.StagedSession> extractApexSessions(com.android.server.pm.StagingManager.StagedSession session) {
        java.util.List<com.android.server.pm.StagingManager.StagedSession> apexSessions = new java.util.ArrayList<>();
        if (session.isMultiPackage()) {
            for (com.android.server.pm.StagingManager.StagedSession s : session.getChildSessions()) {
                if (s.containsApexSession()) {
                    apexSessions.add(s);
                }
            }
        } else {
            apexSessions.add(session);
        }
        return apexSessions;
    }

    private void checkInstallationOfApkInApexSuccessful(com.android.server.pm.StagingManager.StagedSession session) throws com.android.server.pm.PackageManagerException {
        java.util.List<com.android.server.pm.StagingManager.StagedSession> apexSessions = extractApexSessions(session);
        if (apexSessions.isEmpty()) {
            return;
        }
        for (com.android.server.pm.StagingManager.StagedSession apexSession : apexSessions) {
            java.lang.String packageName = apexSession.getPackageName();
            java.lang.String errorMsg = this.mApexManager.getApkInApexInstallError(packageName);
            if (errorMsg != null) {
                throw new com.android.server.pm.PackageManagerException(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Failed to install apk-in-apex of " + packageName + " : " + errorMsg);
            }
        }
    }

    private void snapshotAndRestoreForApexSession(com.android.server.pm.StagingManager.StagedSession session) {
        boolean doSnapshotOrRestore = (session.sessionParams().installFlags & 262144) != 0 || session.sessionParams().installReason == 5;
        if (!doSnapshotOrRestore) {
            return;
        }
        java.util.List<com.android.server.pm.StagingManager.StagedSession> apexSessions = extractApexSessions(session);
        if (apexSessions.isEmpty()) {
            return;
        }
        com.android.server.pm.UserManagerInternal um = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        int[] allUsers = um.getUserIds();
        com.android.server.rollback.RollbackManagerInternal rm = (com.android.server.rollback.RollbackManagerInternal) com.android.server.LocalServices.getService(com.android.server.rollback.RollbackManagerInternal.class);
        int sessionsSize = apexSessions.size();
        for (int i = 0; i < sessionsSize; i++) {
            java.lang.String packageName = apexSessions.get(i).getPackageName();
            snapshotAndRestoreApexUserData(packageName, allUsers, rm);
            java.util.List<java.lang.String> apksInApex = this.mApexManager.getApksInApex(packageName);
            int apksSize = apksInApex.size();
            for (int j = 0; j < apksSize; j++) {
                snapshotAndRestoreApkInApexUserData(apksInApex.get(j), allUsers, rm);
            }
        }
    }

    private void snapshotAndRestoreApexUserData(java.lang.String packageName, int[] allUsers, com.android.server.rollback.RollbackManagerInternal rm) {
        rm.snapshotAndRestoreUserData(packageName, android.os.UserHandle.toUserHandles(allUsers), 0, 0L, null, 0);
    }

    private void snapshotAndRestoreApkInApexUserData(java.lang.String packageName, int[] allUsers, com.android.server.rollback.RollbackManagerInternal rm) {
        android.content.pm.PackageManagerInternal mPmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        com.android.server.pm.pkg.AndroidPackage pkg = mPmi.getPackage(packageName);
        if (pkg == null) {
            android.util.Slog.e(TAG, "Could not find package: " + packageName + "for snapshotting/restoring user data.");
            return;
        }
        com.android.server.pm.pkg.PackageStateInternal ps = mPmi.getPackageStateInternal(packageName);
        if (ps != null) {
            int appId = ps.getAppId();
            long ceDataInode = ps.getUserStateOrDefault(0).getCeDataInode();
            int[] installedUsers = com.android.server.pm.pkg.PackageStateUtils.queryInstalledUsers(ps, allUsers, true);
            java.lang.String seInfo = ps.getSeInfo();
            rm.snapshotAndRestoreUserData(packageName, android.os.UserHandle.toUserHandles(installedUsers), appId, ceDataInode, seInfo, 0);
        }
    }

    private void prepareForLoggingApexdRevert(com.android.server.pm.StagingManager.StagedSession session, java.lang.String nativeFailureReason) {
        synchronized (this.mFailedPackageNames) {
            this.mNativeFailureReason = nativeFailureReason;
            if (session.getPackageName() != null) {
                this.mFailedPackageNames.add(session.getPackageName());
            }
        }
    }

    private void resumeSession(com.android.server.pm.StagingManager.StagedSession session, boolean supportsCheckpoint, boolean needsCheckpoint) throws com.android.server.pm.PackageManagerException {
        android.util.Slog.d(TAG, "Resuming session " + session.sessionId());
        boolean hasApex = session.containsApexSession();
        boolean isSotaApp = false;
        android.content.pm.PackageInstaller.SessionParams params = session.sessionParams();
        if (params != null && this.mStagingManageExt.isSotaAppSession(session)) {
            android.util.Slog.d(TAG, "sota app install from sau ,appPackageName is " + params.appPackageName + " ,needsCheckpoint is " + needsCheckpoint + " ,force set isSotaApp true to jump revert.");
            isSotaApp = true;
        }
        if (supportsCheckpoint && !needsCheckpoint && !isSotaApp) {
            java.lang.String revertMsg = "Reverting back to safe state. Marking " + session.sessionId() + " as failed.";
            java.lang.String reasonForRevert = getReasonForRevert();
            if (!android.text.TextUtils.isEmpty(reasonForRevert)) {
                revertMsg = revertMsg + " Reason for revert: " + reasonForRevert;
            }
            android.util.Slog.d(TAG, revertMsg);
            session.setSessionFailed(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, revertMsg);
            return;
        }
        if (hasApex) {
            checkInstallationOfApkInApexSuccessful(session);
            checkDuplicateApkInApex(session);
            snapshotAndRestoreForApexSession(session);
            android.util.Slog.i(TAG, "APEX packages in session " + session.sessionId() + " were successfully activated. Proceeding with APK packages, if any");
        }
        android.util.Slog.d(TAG, "Installing APK packages in session " + session.sessionId());
        android.util.TimingsTraceLog t = new android.util.TimingsTraceLog("StagingManagerTiming", 262144L);
        t.traceBegin("installApksInSession");
        installApksInSession(session);
        t.traceEnd();
        if (hasApex) {
            if (supportsCheckpoint) {
                synchronized (this.mSuccessfulStagedSessionIds) {
                    this.mSuccessfulStagedSessionIds.add(java.lang.Integer.valueOf(session.sessionId()));
                }
                return;
            }
            this.mApexManager.markStagedSessionSuccessful(session.sessionId());
        }
    }

    void onInstallationFailure(com.android.server.pm.StagingManager.StagedSession session, com.android.server.pm.PackageManagerException e, boolean supportsCheckpoint, boolean needsCheckpoint) {
        session.setSessionFailed(e.error, e.getMessage());
        if (this.mStagingManageExt.isSotaAppSession(session)) {
            android.util.Slog.d(TAG, "Sota session do not abortCheckpoint and other rollback/reboot, return. Failed to install sessionId: " + session.sessionId() + " Error: " + e.getMessage());
            return;
        }
        abortCheckpoint("Failed to install sessionId: " + session.sessionId() + " Error: " + e.getMessage(), supportsCheckpoint, needsCheckpoint);
        if (!session.containsApexSession()) {
            return;
        }
        if (!this.mApexManager.revertActiveSessions()) {
            android.util.Slog.e(TAG, "Failed to abort APEXd session");
        } else {
            android.util.Slog.e(TAG, "Successfully aborted apexd session. Rebooting device in order to revert to the previous state of APEXd.");
            this.mPowerManager.reboot(null);
        }
    }

    private java.lang.String getReasonForRevert() {
        if (!android.text.TextUtils.isEmpty(this.mFailureReason)) {
            return this.mFailureReason;
        }
        if (!android.text.TextUtils.isEmpty(this.mNativeFailureReason)) {
            return "Session reverted due to crashing native process: " + this.mNativeFailureReason;
        }
        return "";
    }

    private void checkDuplicateApkInApex(com.android.server.pm.StagingManager.StagedSession session) throws com.android.server.pm.PackageManagerException {
        if (!session.isMultiPackage()) {
            return;
        }
        java.util.Set<java.lang.String> apkNames = new android.util.ArraySet<>();
        for (com.android.server.pm.StagingManager.StagedSession s : session.getChildSessions()) {
            if (!s.isApexSession()) {
                apkNames.add(s.getPackageName());
            }
        }
        java.util.List<com.android.server.pm.StagingManager.StagedSession> apexSessions = extractApexSessions(session);
        for (com.android.server.pm.StagingManager.StagedSession apexSession : apexSessions) {
            java.lang.String packageName = apexSession.getPackageName();
            for (java.lang.String apkInApex : this.mApexManager.getApksInApex(packageName)) {
                if (!apkNames.add(apkInApex)) {
                    throw new com.android.server.pm.PackageManagerException(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Package: " + packageName + " in session: " + apexSession.sessionId() + " has duplicate apk-in-apex: " + apkInApex, (java.lang.Throwable) null);
                }
            }
        }
    }

    private void installApksInSession(com.android.server.pm.StagingManager.StagedSession session) throws com.android.server.pm.PackageManagerException {
        try {
            session.installSession().get();
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        } catch (java.util.concurrent.ExecutionException ee) {
            throw ((com.android.server.pm.PackageManagerException) ee.getCause());
        }
    }

    void commitSession(com.android.server.pm.StagingManager.StagedSession session) {
        createSession(session);
        handleCommittedSession(session);
    }

    private void handleCommittedSession(com.android.server.pm.StagingManager.StagedSession session) {
        if (session.isSessionReady() && session.containsApexSession()) {
            notifyStagedApexObservers();
        }
    }

    void createSession(com.android.server.pm.StagingManager.StagedSession sessionInfo) {
        synchronized (this.mStagedSessions) {
            this.mStagedSessions.append(sessionInfo.sessionId(), sessionInfo);
        }
    }

    void abortSession(com.android.server.pm.StagingManager.StagedSession session) {
        synchronized (this.mStagedSessions) {
            this.mStagedSessions.remove(session.sessionId());
        }
    }

    void abortCommittedSession(com.android.server.pm.StagingManager.StagedSession session) {
        int sessionId = session.sessionId();
        if (session.isInTerminalState()) {
            android.util.Slog.w(TAG, "Cannot abort session in final state: " + sessionId);
            return;
        }
        if (!session.isDestroyed()) {
            throw new java.lang.IllegalStateException("Committed session must be destroyed before aborting it from StagingManager");
        }
        if (getStagedSession(sessionId) == null) {
            android.util.Slog.w(TAG, "Session " + sessionId + " has been abandoned already");
            return;
        }
        if (session.isSessionReady()) {
            if (!ensureActiveApexSessionIsAborted(session)) {
                android.util.Slog.e(TAG, "Failed to abort apex session " + session.sessionId());
            }
            if (session.containsApexSession()) {
                notifyStagedApexObservers();
            }
        }
        abortSession(session);
    }

    private boolean ensureActiveApexSessionIsAborted(com.android.server.pm.StagingManager.StagedSession session) {
        android.apex.ApexSessionInfo apexSession;
        if (!session.containsApexSession() || (apexSession = this.mApexManager.getStagedSessionInfo(session.sessionId())) == null || isApexSessionFinalized(apexSession)) {
            return true;
        }
        return this.mApexManager.abortStagedSession(session.sessionId());
    }

    private boolean isApexSessionFinalized(android.apex.ApexSessionInfo session) {
        return session.isUnknown || session.isActivationFailed || session.isSuccess || session.isReverted;
    }

    private static boolean isApexSessionFailed(android.apex.ApexSessionInfo apexSessionInfo) {
        return apexSessionInfo.isActivationFailed || apexSessionInfo.isUnknown || apexSessionInfo.isReverted || apexSessionInfo.isRevertInProgress || apexSessionInfo.isRevertFailed;
    }

    private void handleNonReadyAndDestroyedSessions(java.util.List<com.android.server.pm.StagingManager.StagedSession> sessions) {
        int j = sessions.size();
        int i = 0;
        while (i < j) {
            final com.android.server.pm.StagingManager.StagedSession session = sessions.get(i);
            if (session.isDestroyed()) {
                session.abandon();
                com.android.server.pm.StagingManager.StagedSession session2 = sessions.set(j - 1, session);
                sessions.set(i, session2);
                j--;
            } else if (!session.isSessionReady()) {
                android.util.Slog.i(TAG, "Restart verification for session=" + session.sessionId());
                this.mBootCompleted.thenRun(new java.lang.Runnable() { // from class: com.android.server.pm.StagingManager$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        session.verifySession();
                    }
                });
                com.android.server.pm.StagingManager.StagedSession session22 = sessions.set(j - 1, session);
                sessions.set(i, session22);
                j--;
            } else {
                i++;
            }
        }
        int i2 = sessions.size();
        sessions.subList(j, i2).clear();
    }

    void restoreSessions(java.util.List<com.android.server.pm.StagingManager.StagedSession> sessions, boolean isDeviceUpgrading) {
        java.util.List<com.android.server.pm.StagingManager.StagedSession> sotaAppStageSessionList;
        android.util.TimingsTraceLog t = new android.util.TimingsTraceLog("StagingManagerTiming", 262144L);
        t.traceBegin("restoreSessions");
        if (android.os.SystemProperties.getBoolean("sys.boot_completed", false)) {
            return;
        }
        for (int i = 0; i < sessions.size(); i++) {
            com.android.server.pm.StagingManager.StagedSession session = sessions.get(i);
            com.android.internal.util.Preconditions.checkArgument(!session.hasParentSessionId(), session.sessionId() + " is a child session");
            com.android.internal.util.Preconditions.checkArgument(session.isCommitted(), session.sessionId() + " is not committed");
            com.android.internal.util.Preconditions.checkArgument(true ^ session.isInTerminalState(), session.sessionId() + " is in terminal state");
            createSession(session);
        }
        if (!isDeviceUpgrading) {
            sotaAppStageSessionList = sessions;
        } else if (this.mStagingManageExt.isBootFromSotaAppUpdate()) {
            java.util.List<com.android.server.pm.StagingManager.StagedSession> sotaAppStageSessionList2 = new java.util.ArrayList<>();
            for (com.android.server.pm.StagingManager.StagedSession session2 : sessions) {
                if (this.mStagingManageExt.isSotaAppSession(session2)) {
                    sotaAppStageSessionList2.add(session2);
                } else {
                    session2.setSessionFailed(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Build fingerprint has changed");
                }
            }
            if (sotaAppStageSessionList2.isEmpty()) {
                return;
            } else {
                sotaAppStageSessionList = sotaAppStageSessionList2;
            }
        } else {
            for (int i2 = 0; i2 < sessions.size(); i2++) {
                sessions.get(i2).setSessionFailed(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Build fingerprint has changed");
            }
            return;
        }
        try {
            boolean supportsCheckpoint = com.android.internal.content.InstallLocationUtils.getStorageManager().supportsCheckpoint();
            boolean needsCheckpoint = com.android.internal.content.InstallLocationUtils.getStorageManager().needsCheckpoint();
            if (sotaAppStageSessionList.size() > 1 && !supportsCheckpoint) {
                throw new java.lang.IllegalStateException("Detected multiple staged sessions on a device without fs-checkpoint support");
            }
            handleNonReadyAndDestroyedSessions(sotaAppStageSessionList);
            android.util.SparseArray<android.apex.ApexSessionInfo> apexSessions = this.mApexManager.getSessions();
            boolean hasFailedApexSession = false;
            boolean hasAppliedApexSession = false;
            for (int i3 = 0; i3 < sotaAppStageSessionList.size(); i3++) {
                com.android.server.pm.StagingManager.StagedSession session3 = sotaAppStageSessionList.get(i3);
                if (session3.containsApexSession()) {
                    android.apex.ApexSessionInfo apexSession = apexSessions.get(session3.sessionId());
                    if (apexSession == null || apexSession.isUnknown) {
                        session3.setSessionFailed(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "apexd did not know anything about a staged session supposed to be activated");
                        hasFailedApexSession = true;
                    } else if (isApexSessionFailed(apexSession)) {
                        hasFailedApexSession = true;
                        if (!android.text.TextUtils.isEmpty(apexSession.crashingNativeProcess)) {
                            prepareForLoggingApexdRevert(session3, apexSession.crashingNativeProcess);
                        }
                        java.lang.String errorMsg = "APEX activation failed.";
                        java.lang.String reasonForRevert = getReasonForRevert();
                        if (!android.text.TextUtils.isEmpty(reasonForRevert)) {
                            errorMsg = "APEX activation failed. Reason: " + reasonForRevert;
                        } else if (!android.text.TextUtils.isEmpty(apexSession.errorMessage)) {
                            errorMsg = "APEX activation failed. Error: " + apexSession.errorMessage;
                        }
                        android.util.Slog.d(TAG, errorMsg);
                        session3.setSessionFailed(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, errorMsg);
                    } else if (apexSession.isActivated || apexSession.isSuccess) {
                        hasAppliedApexSession = true;
                    } else if (apexSession.isStaged) {
                        session3.setSessionFailed(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Staged session " + session3.sessionId() + " at boot didn't activate nor fail. Marking it as failed anyway.");
                        hasFailedApexSession = true;
                    } else {
                        android.util.Slog.w(TAG, "Apex session " + session3.sessionId() + " is in impossible state");
                        session3.setSessionFailed(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Impossible state");
                        hasFailedApexSession = true;
                    }
                }
            }
            if (hasAppliedApexSession && hasFailedApexSession) {
                abortCheckpoint("Found both applied and failed apex sessions", supportsCheckpoint, needsCheckpoint);
                return;
            }
            if (hasFailedApexSession) {
                for (int i4 = 0; i4 < sotaAppStageSessionList.size(); i4++) {
                    com.android.server.pm.StagingManager.StagedSession session4 = sotaAppStageSessionList.get(i4);
                    if (!session4.isSessionFailed()) {
                        session4.setSessionFailed(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Another apex session failed");
                    }
                }
                return;
            }
            for (int i5 = 0; i5 < sotaAppStageSessionList.size(); i5++) {
                com.android.server.pm.StagingManager.StagedSession session5 = sotaAppStageSessionList.get(i5);
                try {
                    resumeSession(session5, supportsCheckpoint, needsCheckpoint);
                } catch (com.android.server.pm.PackageManagerException e) {
                    onInstallationFailure(session5, e, supportsCheckpoint, needsCheckpoint);
                } catch (java.lang.Exception e2) {
                    android.util.Slog.e(TAG, "Staged install failed due to unhandled exception", e2);
                    onInstallationFailure(session5, new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Staged install failed due to unhandled exception: " + e2), supportsCheckpoint, needsCheckpoint);
                }
            }
            t.traceEnd();
        } catch (android.os.RemoteException e3) {
            throw new java.lang.IllegalStateException("Failed to get checkpoint status", e3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: logFailedApexSessionsIfNecessary, reason: merged with bridge method [inline-methods] */
    public void lambda$onBootCompletedBroadcastReceived$1() {
        synchronized (this.mFailedPackageNames) {
            if (!this.mFailedPackageNames.isEmpty()) {
                com.android.server.rollback.WatchdogRollbackLogger.logApexdRevert(this.mContext, this.mFailedPackageNames, this.mNativeFailureReason);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markStagedSessionsAsSuccessful() {
        synchronized (this.mSuccessfulStagedSessionIds) {
            for (int i = 0; i < this.mSuccessfulStagedSessionIds.size(); i++) {
                this.mApexManager.markStagedSessionSuccessful(this.mSuccessfulStagedSessionIds.get(i).intValue());
            }
        }
    }

    void systemReady() {
        new com.android.server.pm.StagingManager.Lifecycle(this.mContext).startService(this);
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.pm.StagingManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context ctx, android.content.Intent intent) {
                com.android.server.pm.StagingManager.this.onBootCompletedBroadcastReceived();
                ctx.unregisterReceiver(this);
            }
        }, new android.content.IntentFilter("android.intent.action.BOOT_COMPLETED"));
        this.mFailureReasonFile.delete();
    }

    void onBootCompletedBroadcastReceived() {
        this.mBootCompleted.complete(null);
        com.android.internal.os.BackgroundThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.pm.StagingManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBootCompletedBroadcastReceived$1();
            }
        });
    }

    private com.android.server.pm.StagingManager.StagedSession getStagedSession(int sessionId) {
        com.android.server.pm.StagingManager.StagedSession session;
        synchronized (this.mStagedSessions) {
            session = this.mStagedSessions.get(sessionId);
        }
        return session;
    }

    java.util.Map<java.lang.String, android.apex.ApexInfo> getStagedApexInfos(com.android.server.pm.StagingManager.StagedSession session) {
        com.android.internal.util.Preconditions.checkArgument(session != null, "Session is null");
        com.android.internal.util.Preconditions.checkArgument(true ^ session.hasParentSessionId(), session.sessionId() + " session has parent session");
        com.android.internal.util.Preconditions.checkArgument(session.containsApexSession(), session.sessionId() + " session does not contain apex");
        if (!session.isSessionReady() || session.isDestroyed()) {
            return java.util.Collections.emptyMap();
        }
        android.apex.ApexSessionParams params = new android.apex.ApexSessionParams();
        params.sessionId = session.sessionId();
        android.util.IntArray childSessionIds = new android.util.IntArray();
        if (session.isMultiPackage()) {
            for (com.android.server.pm.StagingManager.StagedSession s : session.getChildSessions()) {
                if (s.isApexSession()) {
                    childSessionIds.add(s.sessionId());
                }
            }
        }
        params.childSessionIds = childSessionIds.toArray();
        android.apex.ApexInfo[] infos = this.mApexManager.getStagedApexInfos(params);
        java.util.Map<java.lang.String, android.apex.ApexInfo> result = new android.util.ArrayMap<>();
        for (android.apex.ApexInfo info : infos) {
            result.put(info.moduleName, info);
        }
        return result;
    }

    java.util.List<java.lang.String> getStagedApexModuleNames() {
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        synchronized (this.mStagedSessions) {
            for (int i = 0; i < this.mStagedSessions.size(); i++) {
                com.android.server.pm.StagingManager.StagedSession session = this.mStagedSessions.valueAt(i);
                if (session.isSessionReady() && !session.isDestroyed() && !session.hasParentSessionId() && session.containsApexSession()) {
                    result.addAll(getStagedApexInfos(session).keySet());
                }
            }
        }
        return result;
    }

    android.content.pm.StagedApexInfo getStagedApexInfo(java.lang.String moduleName) {
        android.apex.ApexInfo ai;
        synchronized (this.mStagedSessions) {
            for (int i = 0; i < this.mStagedSessions.size(); i++) {
                com.android.server.pm.StagingManager.StagedSession session = this.mStagedSessions.valueAt(i);
                if (session.isSessionReady() && !session.isDestroyed() && !session.hasParentSessionId() && session.containsApexSession() && (ai = getStagedApexInfos(session).get(moduleName)) != null) {
                    android.content.pm.StagedApexInfo info = new android.content.pm.StagedApexInfo();
                    info.moduleName = ai.moduleName;
                    info.diskImagePath = ai.modulePath;
                    info.versionCode = ai.versionCode;
                    info.versionName = ai.versionName;
                    info.hasClassPathJars = ai.hasClassPathJars;
                    return info;
                }
            }
            return null;
        }
    }

    private void notifyStagedApexObservers() {
        synchronized (this.mStagedApexObservers) {
            for (android.content.pm.IStagedApexObserver observer : this.mStagedApexObservers) {
                android.content.pm.ApexStagedEvent event = new android.content.pm.ApexStagedEvent();
                event.stagedApexModuleNames = (java.lang.String[]) getStagedApexModuleNames().toArray(new java.lang.String[0]);
                try {
                    observer.onApexStaged(event);
                } catch (android.os.RemoteException re) {
                    android.util.Slog.w(TAG, "Failed to contact the observer " + re.getMessage());
                }
            }
        }
    }
}
