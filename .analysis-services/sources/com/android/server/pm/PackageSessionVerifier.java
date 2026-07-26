package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PackageSessionVerifier {
    private static final java.lang.String TAG = "PackageSessionVerifier";
    private final com.android.server.pm.ApexManager mApexManager;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final java.util.function.Supplier<com.android.internal.pm.parsing.PackageParser2> mPackageParserSupplier;
    private final com.android.server.pm.PackageManagerService mPm;
    private final java.util.List<com.android.server.pm.StagingManager.StagedSession> mStagedSessions;

    interface Callback {
        void onResult(int i, java.lang.String str);
    }

    PackageSessionVerifier(android.content.Context context, com.android.server.pm.PackageManagerService pm, com.android.server.pm.ApexManager apexManager, java.util.function.Supplier<com.android.internal.pm.parsing.PackageParser2> packageParserSupplier, android.os.Looper looper) {
        this.mStagedSessions = new java.util.ArrayList();
        this.mContext = context;
        this.mPm = pm;
        this.mApexManager = apexManager;
        this.mPackageParserSupplier = packageParserSupplier;
        this.mHandler = new android.os.Handler(looper);
    }

    PackageSessionVerifier() {
        this.mStagedSessions = new java.util.ArrayList();
        this.mContext = null;
        this.mPm = null;
        this.mApexManager = null;
        this.mPackageParserSupplier = null;
        this.mHandler = null;
    }

    public void verify(final com.android.server.pm.PackageInstallerSession session, final com.android.server.pm.PackageSessionVerifier.Callback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageSessionVerifier$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$verify$0(session, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verify$0(com.android.server.pm.PackageInstallerSession session, com.android.server.pm.PackageSessionVerifier.Callback callback) {
        try {
            storeSession(session.mStagedSession);
            if (session.isMultiPackage()) {
                for (com.android.server.pm.PackageInstallerSession child : session.getChildSessions()) {
                    checkApexUpdateAllowed(child);
                    checkRebootlessApex(child);
                    checkApexSignature(child);
                }
            } else {
                checkApexUpdateAllowed(session);
                checkRebootlessApex(session);
                checkApexSignature(session);
            }
            verifyAPK(session, callback);
        } catch (com.android.server.pm.PackageManagerException e) {
            java.lang.String errorMessage = android.content.pm.PackageManager.installStatusToString(e.error, e.getMessage());
            session.setSessionFailed(e.error, errorMessage);
            callback.onResult(e.error, e.getMessage());
        }
    }

    private android.content.pm.SigningDetails getSigningDetails(android.content.pm.PackageInfo apexPkg) throws com.android.server.pm.PackageManagerException {
        java.lang.String apexPath = apexPkg.applicationInfo.sourceDir;
        int minSignatureScheme = android.util.apk.ApkSignatureVerifier.getMinimumSignatureSchemeVersionForTargetSdk(apexPkg.applicationInfo.targetSdkVersion);
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        android.content.pm.parsing.result.ParseResult<android.content.pm.SigningDetails> result = android.util.apk.ApkSignatureVerifier.verify(input, apexPath, minSignatureScheme);
        if (result.isError()) {
            throw new com.android.server.pm.PackageManagerException(-22, "Failed to verify APEX package " + apexPath + " : " + result.getException(), result.getException());
        }
        return (android.content.pm.SigningDetails) result.getResult();
    }

    private void checkApexSignature(com.android.server.pm.PackageInstallerSession session) throws com.android.server.pm.PackageManagerException {
        if (!session.isApexSession()) {
            return;
        }
        java.lang.String packageName = session.getPackageName();
        android.content.pm.PackageInfo existingApexPkg = this.mPm.snapshotComputer().getPackageInfo(session.getPackageName(), 1073741824L, 0);
        if (existingApexPkg == null) {
            throw new com.android.server.pm.PackageManagerException(-23, "Attempting to install new APEX package " + packageName);
        }
        android.content.pm.SigningDetails existingSigningDetails = getSigningDetails(existingApexPkg);
        android.content.pm.SigningDetails newSigningDetails = session.getSigningDetails();
        if (newSigningDetails.checkCapability(existingSigningDetails, 1) || existingSigningDetails.checkCapability(newSigningDetails, 8)) {
        } else {
            throw new com.android.server.pm.PackageManagerException(-22, "APK container signature of APEX package " + packageName + " is not compatible with the one currently installed on device");
        }
    }

    private void verifyAPK(final com.android.server.pm.PackageInstallerSession session, final com.android.server.pm.PackageSessionVerifier.Callback callback) throws com.android.server.pm.PackageManagerException {
        com.android.server.pm.VerifyingSession verifyingSession = createVerifyingSession(session, new android.content.pm.IPackageInstallObserver2.Stub() { // from class: com.android.server.pm.PackageSessionVerifier.1
            public void onUserActionRequired(android.content.Intent intent) {
                throw new java.lang.IllegalStateException();
            }

            public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) {
                if (session.isStaged() && returnCode == 1) {
                    com.android.server.pm.PackageSessionVerifier.this.verifyStaged(session.mStagedSession, callback);
                    return;
                }
                if (returnCode != 1) {
                    java.lang.String errorMessage = android.content.pm.PackageManager.installStatusToString(returnCode, msg);
                    session.setSessionFailed(returnCode, errorMessage);
                    callback.onResult(returnCode, msg);
                } else {
                    session.setSessionReady();
                    callback.onResult(1, null);
                }
            }
        });
        if (session.isMultiPackage()) {
            java.util.List<com.android.server.pm.PackageInstallerSession> childSessions = session.getChildSessions();
            java.util.List<com.android.server.pm.VerifyingSession> verifyingChildSessions = new java.util.ArrayList<>(childSessions.size());
            for (com.android.server.pm.PackageInstallerSession child : childSessions) {
                verifyingChildSessions.add(createVerifyingSession(child, null));
            }
            verifyingSession.verifyStage(verifyingChildSessions);
            return;
        }
        verifyingSession.verifyStage();
    }

    private com.android.server.pm.VerifyingSession createVerifyingSession(com.android.server.pm.PackageInstallerSession session, android.content.pm.IPackageInstallObserver2 observer) {
        android.os.UserHandle user;
        if ((session.params.installFlags & 64) != 0) {
            user = android.os.UserHandle.ALL;
        } else {
            user = new android.os.UserHandle(session.userId);
        }
        return new com.android.server.pm.VerifyingSession(user, session.stageDir, observer, session.params, session.getInstallSource(), session.getInstallerUid(), session.getSigningDetails(), session.sessionId, session.getPackageLite(), session.getUserActionRequired(), this.mPm);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verifyStaged(final com.android.server.pm.StagingManager.StagedSession session, final com.android.server.pm.PackageSessionVerifier.Callback callback) {
        android.util.Slog.d(TAG, "Starting preRebootVerification for session " + session.sessionId());
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageSessionVerifier$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$verifyStaged$1(session, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyStaged$1(com.android.server.pm.StagingManager.StagedSession session, com.android.server.pm.PackageSessionVerifier.Callback callback) {
        try {
            checkActiveSessions();
            checkRollbacks(session);
            if (session.isMultiPackage()) {
                for (com.android.server.pm.StagingManager.StagedSession child : session.getChildSessions()) {
                    checkOverlaps(session, child);
                }
            } else {
                checkOverlaps(session, session);
            }
            dispatchVerifyApex(session, callback);
        } catch (com.android.server.pm.PackageManagerException e) {
            onVerificationFailure(session, callback, e.error, e.getMessage());
        }
    }

    void storeSession(com.android.server.pm.StagingManager.StagedSession session) {
        if (session != null) {
            this.mStagedSessions.add(session);
        }
    }

    private void onVerificationSuccess(com.android.server.pm.StagingManager.StagedSession session, com.android.server.pm.PackageSessionVerifier.Callback callback) {
        callback.onResult(1, null);
    }

    private void onVerificationFailure(com.android.server.pm.StagingManager.StagedSession session, com.android.server.pm.PackageSessionVerifier.Callback callback, int errorCode, java.lang.String errorMessage) {
        if (!ensureActiveApexSessionIsAborted(session)) {
            android.util.Slog.e(TAG, "Failed to abort apex session " + session.sessionId());
        }
        session.setSessionFailed(errorCode, errorMessage);
        callback.onResult(-22, errorMessage);
    }

    private void dispatchVerifyApex(final com.android.server.pm.StagingManager.StagedSession session, final com.android.server.pm.PackageSessionVerifier.Callback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageSessionVerifier$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dispatchVerifyApex$2(session, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchVerifyApex$2(com.android.server.pm.StagingManager.StagedSession session, com.android.server.pm.PackageSessionVerifier.Callback callback) {
        try {
            verifyApex(session);
            dispatchEndVerification(session, callback);
        } catch (com.android.server.pm.PackageManagerException e) {
            onVerificationFailure(session, callback, e.error, e.getMessage());
        }
    }

    private void dispatchEndVerification(final com.android.server.pm.StagingManager.StagedSession session, final com.android.server.pm.PackageSessionVerifier.Callback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageSessionVerifier$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dispatchEndVerification$3(session, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchEndVerification$3(com.android.server.pm.StagingManager.StagedSession session, com.android.server.pm.PackageSessionVerifier.Callback callback) {
        try {
            endVerification(session);
            onVerificationSuccess(session, callback);
        } catch (com.android.server.pm.PackageManagerException e) {
            onVerificationFailure(session, callback, e.error, e.getMessage());
        }
    }

    private void verifyApex(com.android.server.pm.StagingManager.StagedSession session) throws com.android.server.pm.PackageManagerException {
        int rollbackId = -1;
        if ((session.sessionParams().installFlags & 262144) != 0) {
            com.android.server.rollback.RollbackManagerInternal rm = (com.android.server.rollback.RollbackManagerInternal) com.android.server.LocalServices.getService(com.android.server.rollback.RollbackManagerInternal.class);
            try {
                rollbackId = rm.notifyStagedSession(session.sessionId());
            } catch (java.lang.RuntimeException re) {
                android.util.Slog.e(TAG, "Failed to notifyStagedSession for session: " + session.sessionId(), re);
            }
        } else if (isRollback(session)) {
            rollbackId = retrieveRollbackIdForCommitSession(session.sessionId());
        }
        boolean hasApex = session.containsApexSession();
        if (hasApex) {
            submitSessionToApexService(session, rollbackId);
        }
    }

    private void endVerification(com.android.server.pm.StagingManager.StagedSession session) throws com.android.server.pm.PackageManagerException {
        try {
            if (com.android.internal.content.InstallLocationUtils.getStorageManager().supportsCheckpoint()) {
                com.android.internal.content.InstallLocationUtils.getStorageManager().startCheckpoint(2);
            }
            android.util.Slog.d(TAG, "Marking session " + session.sessionId() + " as ready");
            session.setSessionReady();
            if (session.isSessionReady()) {
                boolean hasApex = session.containsApexSession();
                if (hasApex) {
                    this.mApexManager.markStagedSessionReady(session.sessionId());
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to get hold of StorageManager", e);
            throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Failed to get hold of StorageManager");
        }
    }

    private void submitSessionToApexService(com.android.server.pm.StagingManager.StagedSession session, int rollbackId) throws com.android.server.pm.PackageManagerException {
        android.util.IntArray childSessionIds = new android.util.IntArray();
        if (session.isMultiPackage()) {
            for (com.android.server.pm.StagingManager.StagedSession s : session.getChildSessions()) {
                if (s.isApexSession()) {
                    childSessionIds.add(s.sessionId());
                }
            }
        }
        android.apex.ApexSessionParams apexSessionParams = new android.apex.ApexSessionParams();
        apexSessionParams.sessionId = session.sessionId();
        apexSessionParams.childSessionIds = childSessionIds.toArray();
        if (session.sessionParams().installReason == 5) {
            apexSessionParams.isRollback = true;
            apexSessionParams.rollbackId = rollbackId;
        } else if (rollbackId != -1) {
            apexSessionParams.hasRollbackEnabled = true;
            apexSessionParams.rollbackId = rollbackId;
        }
        android.apex.ApexInfoList apexInfoList = this.mApexManager.submitStagedSession(apexSessionParams);
        java.util.List<java.lang.String> apexPackageNames = new java.util.ArrayList<>();
        for (android.apex.ApexInfo apexInfo : apexInfoList.apexInfos) {
            try {
                com.android.internal.pm.parsing.PackageParser2 packageParser = this.mPackageParserSupplier.get();
                try {
                    java.io.File apexFile = new java.io.File(apexInfo.modulePath);
                    com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage = packageParser.parsePackage(apexFile, 1024, false);
                    if (packageParser != null) {
                        packageParser.close();
                    }
                    apexPackageNames.add(parsedPackage.getPackageName());
                } finally {
                }
            } catch (com.android.internal.pm.parsing.PackageParserException e) {
                throw new com.android.server.pm.PackageManagerException(-22, "Failed to parse APEX package " + apexInfo.modulePath + " : " + e, (java.lang.Throwable) e);
            }
        }
        android.util.Slog.d(TAG, "Session " + session.sessionId() + " has following APEX packages: " + apexPackageNames);
    }

    private int retrieveRollbackIdForCommitSession(int sessionId) throws com.android.server.pm.PackageManagerException {
        android.content.rollback.RollbackManager rm = (android.content.rollback.RollbackManager) this.mContext.getSystemService(android.content.rollback.RollbackManager.class);
        java.util.List<android.content.rollback.RollbackInfo> rollbacks = rm.getRecentlyCommittedRollbacks();
        int size = rollbacks.size();
        for (int i = 0; i < size; i++) {
            android.content.rollback.RollbackInfo rollback = rollbacks.get(i);
            if (rollback.getCommittedSessionId() == sessionId) {
                return rollback.getRollbackId();
            }
        }
        throw new com.android.server.pm.PackageManagerException(-22, "Could not find rollback id for commit session: " + sessionId);
    }

    private static boolean isRollback(com.android.server.pm.StagingManager.StagedSession session) {
        return session.sessionParams().installReason == 5;
    }

    private static boolean isApexSessionFinalized(android.apex.ApexSessionInfo info) {
        return info.isUnknown || info.isActivationFailed || info.isSuccess || info.isReverted;
    }

    private boolean ensureActiveApexSessionIsAborted(com.android.server.pm.StagingManager.StagedSession session) {
        int sessionId;
        android.apex.ApexSessionInfo apexSession;
        if (!session.containsApexSession() || (apexSession = this.mApexManager.getStagedSessionInfo((sessionId = session.sessionId()))) == null || isApexSessionFinalized(apexSession)) {
            return true;
        }
        return this.mApexManager.abortStagedSession(sessionId);
    }

    private boolean isApexUpdateAllowed(java.lang.String apexPackageName, java.lang.String installerPackageName) {
        if (this.mPm.getModuleInfo(apexPackageName, 0) != null) {
            java.lang.String modulesInstaller = com.android.server.SystemConfig.getInstance().getModulesInstallerPackageName();
            if (modulesInstaller == null) {
                android.util.Slog.w(TAG, "No modules installer defined");
                return false;
            }
            return modulesInstaller.equals(installerPackageName);
        }
        java.lang.String vendorApexInstaller = com.android.server.SystemConfig.getInstance().getAllowedVendorApexes().get(apexPackageName);
        if (vendorApexInstaller == null) {
            android.util.Slog.w(TAG, apexPackageName + " is not allowed to be updated");
            return false;
        }
        return vendorApexInstaller.equals(installerPackageName);
    }

    private void checkApexUpdateAllowed(com.android.server.pm.PackageInstallerSession session) throws com.android.server.pm.PackageManagerException {
        if (!session.isApexSession()) {
            return;
        }
        int installFlags = session.params.installFlags;
        if ((8388608 & installFlags) != 0) {
            return;
        }
        java.lang.String packageName = session.getPackageName();
        java.lang.String installerPackageName = session.getInstallSource().mInstallerPackageName;
        if (!isApexUpdateAllowed(packageName, installerPackageName)) {
            throw new com.android.server.pm.PackageManagerException(-22, "Update of APEX package " + packageName + " is not allowed for " + installerPackageName);
        }
    }

    void checkRebootlessApex(com.android.server.pm.PackageInstallerSession session) throws com.android.server.pm.PackageManagerException {
        if (session.isStaged() || !session.isApexSession()) {
            return;
        }
        final java.lang.String packageName = session.getPackageName();
        if (packageName == null) {
            throw new com.android.server.pm.PackageManagerException(-22, "Invalid session " + session.sessionId + " with package name null");
        }
        for (com.android.server.pm.StagingManager.StagedSession stagedSession : this.mStagedSessions) {
            if (!stagedSession.isDestroyed() && !stagedSession.isInTerminalState() && stagedSession.sessionContains(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageSessionVerifier$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return packageName.equals(((com.android.server.pm.StagingManager.StagedSession) obj).getPackageName());
                }
            })) {
                throw new com.android.server.pm.PackageManagerException(-22, "Staged session " + stagedSession.sessionId() + " already contains " + packageName);
            }
        }
    }

    private void checkActiveSessions() throws com.android.server.pm.PackageManagerException {
        try {
            checkActiveSessions(com.android.internal.content.InstallLocationUtils.getStorageManager().supportsCheckpoint());
        } catch (android.os.RemoteException e) {
            throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Can't query fs-checkpoint status : " + e);
        }
    }

    void checkActiveSessions(boolean supportsCheckpoint) throws com.android.server.pm.PackageManagerException {
        int activeSessions = 0;
        for (com.android.server.pm.StagingManager.StagedSession stagedSession : this.mStagedSessions) {
            if (!stagedSession.isDestroyed() && !stagedSession.isInTerminalState()) {
                activeSessions++;
            }
        }
        if (!supportsCheckpoint && activeSessions > 1) {
            throw new com.android.server.pm.PackageManagerException(-119, "Cannot stage multiple sessions without checkpoint support");
        }
    }

    void checkRollbacks(com.android.server.pm.StagingManager.StagedSession session) throws com.android.server.pm.PackageManagerException {
        if (session.isDestroyed() || session.isInTerminalState()) {
            return;
        }
        for (com.android.server.pm.StagingManager.StagedSession stagedSession : this.mStagedSessions) {
            if (!stagedSession.isDestroyed() && !stagedSession.isInTerminalState()) {
                if (isRollback(session) && !isRollback(stagedSession)) {
                    if (!ensureActiveApexSessionIsAborted(stagedSession)) {
                        android.util.Slog.e(TAG, "Failed to abort apex session " + stagedSession.sessionId());
                    }
                    stagedSession.setSessionFailed(-119, "Session was failed by rollback session: " + session.sessionId());
                    android.util.Slog.i(TAG, "Session " + stagedSession.sessionId() + " is marked failed due to rollback session: " + session.sessionId());
                } else if (!isRollback(session) && isRollback(stagedSession)) {
                    throw new com.android.server.pm.PackageManagerException(-119, "Session was failed by rollback session: " + stagedSession.sessionId());
                }
            }
        }
    }

    void checkOverlaps(com.android.server.pm.StagingManager.StagedSession parent, com.android.server.pm.StagingManager.StagedSession child) throws com.android.server.pm.PackageManagerException {
        if (parent.isDestroyed() || parent.isInTerminalState()) {
            return;
        }
        final java.lang.String packageName = child.getPackageName();
        if (packageName == null) {
            throw new com.android.server.pm.PackageManagerException(-22, "Cannot stage session " + child.sessionId() + " with package name null");
        }
        for (com.android.server.pm.StagingManager.StagedSession stagedSession : this.mStagedSessions) {
            if (!stagedSession.isDestroyed() && !stagedSession.isInTerminalState() && stagedSession.sessionId() != parent.sessionId() && stagedSession.sessionContains(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageSessionVerifier$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return packageName.equals(((com.android.server.pm.StagingManager.StagedSession) obj).getPackageName());
                }
            })) {
                if (stagedSession.getCommittedMillis() < parent.getCommittedMillis()) {
                    throw new com.android.server.pm.PackageManagerException(-119, "Package: " + packageName + " in session: " + child.sessionId() + " has been staged already by session: " + stagedSession.sessionId());
                }
                stagedSession.setSessionFailed(-119, "Package: " + packageName + " in session: " + stagedSession.sessionId() + " has been staged already by session: " + child.sessionId());
            }
        }
    }
}
