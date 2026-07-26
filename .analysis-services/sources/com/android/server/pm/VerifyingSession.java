package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class VerifyingSession {
    private static final long DEFAULT_ENABLE_ROLLBACK_TIMEOUT_MILLIS = 10000;
    private static final long DEFAULT_INTEGRITY_VERIFICATION_TIMEOUT = 30000;
    private static final boolean DEFAULT_INTEGRITY_VERIFY_ENABLE = true;
    private static final boolean DEFAULT_VERIFY_ENABLE = true;
    private static final java.lang.String PROPERTY_ENABLE_ROLLBACK_TIMEOUT_MILLIS = "enable_rollback_timeout";
    private final int mDataLoaderType;
    private final int mInstallFlags;
    private final int mInstallReason;
    private final com.android.server.pm.InstallSource mInstallSource;
    private final boolean mIsInherit;
    private final boolean mIsStaged;
    final android.content.pm.IPackageInstallObserver2 mObserver;
    final com.android.server.pm.OriginInfo mOriginInfo;
    private final java.lang.String mPackageAbiOverride;
    private final android.content.pm.parsing.PackageLite mPackageLite;
    com.android.server.pm.MultiPackageVerifyingSession mParentVerifyingSession;
    private final com.android.server.pm.PackageManagerService mPm;
    private final long mRequiredInstalledVersionCode;
    private final int mSessionId;
    private final android.content.pm.SigningDetails mSigningDetails;
    private final android.os.UserHandle mUser;
    private final boolean mUserActionRequired;
    private final int mUserActionRequiredType;
    private final com.android.server.pm.VerificationInfo mVerificationInfo;
    private boolean mWaitForEnableRollbackToComplete;
    private boolean mWaitForIntegrityVerificationToComplete;
    private boolean mWaitForVerificationToComplete;
    private int mRet = 1;
    private java.lang.String mErrorMessage = null;
    private final com.android.server.pm.IVerifyingSessionWrapper mWrapper = new com.android.server.pm.VerifyingSession.VerifyingSessionWrapper();

    VerifyingSession(android.os.UserHandle user, java.io.File stagedDir, android.content.pm.IPackageInstallObserver2 observer, android.content.pm.PackageInstaller.SessionParams sessionParams, com.android.server.pm.InstallSource installSource, int installerUid, android.content.pm.SigningDetails signingDetails, int sessionId, android.content.pm.parsing.PackageLite lite, boolean userActionRequired, com.android.server.pm.PackageManagerService pm) {
        this.mPm = pm;
        this.mUser = user;
        this.mOriginInfo = com.android.server.pm.OriginInfo.fromStagedFile(stagedDir);
        this.mObserver = observer;
        this.mInstallFlags = sessionParams.installFlags;
        this.mInstallSource = installSource;
        this.mPackageAbiOverride = sessionParams.abiOverride;
        this.mVerificationInfo = new com.android.server.pm.VerificationInfo(sessionParams.originatingUri, sessionParams.referrerUri, sessionParams.originatingUid, installerUid);
        this.mSigningDetails = signingDetails;
        this.mRequiredInstalledVersionCode = sessionParams.requiredInstalledVersionCode;
        this.mDataLoaderType = sessionParams.dataLoaderParams != null ? sessionParams.dataLoaderParams.getType() : 0;
        this.mSessionId = sessionId;
        this.mPackageLite = lite;
        this.mUserActionRequired = userActionRequired;
        this.mUserActionRequiredType = sessionParams.requireUserAction;
        this.mIsInherit = sessionParams.mode == 2;
        this.mIsStaged = sessionParams.isStaged;
        this.mInstallReason = sessionParams.installReason;
    }

    public java.lang.String toString() {
        return "VerifyingSession{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " file=" + this.mOriginInfo.mFile + "}";
    }

    public void handleStartVerify() {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "handleStartCopy in VerificationParams: " + (this.mPackageLite == null ? null : this.mPackageLite.getPackageName()));
        }
        android.content.pm.PackageInfoLite pkgLite = com.android.server.pm.PackageManagerServiceUtils.getMinimalPackageInfo(this.mPm.mContext, this.mPackageLite, this.mOriginInfo.mResolvedPath, this.mInstallFlags, this.mPackageAbiOverride);
        android.util.Pair<java.lang.Integer, java.lang.String> ret = this.mPm.verifyReplacingVersionCode(pkgLite, this.mRequiredInstalledVersionCode, this.mInstallFlags);
        setReturnCode(((java.lang.Integer) ret.first).intValue(), (java.lang.String) ret.second);
        this.mRet = this.mPm.mPackageManagerServiceExt.modifyRetInHandleStartCopyOfVerificationParams(this.mRet, this.mInstallSource, pkgLite, this.mObserver);
        if (this.mRet != 1) {
            this.mPm.mPackageManagerServiceExt.beforeFailReturnInHandleStartCopyOfVerificationParams(this.mRet, pkgLite, this.mInstallSource, getUser() != null ? getUser().getIdentifier() : 0);
            return;
        }
        if (!this.mOriginInfo.mExisting) {
            int i = (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection() && isARollback()) ? 0 : 1;
            if (!isApex() && !isArchivedInstallation() && i != 0) {
                sendApkVerificationRequest(pkgLite);
            }
            if ((this.mInstallFlags & 262144) != 0) {
                sendEnableRollbackRequest();
            }
        }
    }

    private boolean isARollback() {
        return this.mInstallReason == 5 && this.mInstallSource.mInitiatingPackageName.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
    }

    private void sendApkVerificationRequest(android.content.pm.PackageInfoLite pkgLite) {
        com.android.server.pm.PackageManagerService packageManagerService = this.mPm;
        int verificationId = packageManagerService.mPendingVerificationToken;
        packageManagerService.mPendingVerificationToken = verificationId + 1;
        com.android.server.pm.PackageVerificationState verificationState = new com.android.server.pm.PackageVerificationState(this);
        this.mPm.mPendingVerification.append(verificationId, verificationState);
        sendIntegrityVerificationRequest(verificationId, pkgLite, verificationState);
        sendPackageVerificationRequest(verificationId, pkgLite, verificationState);
        if (verificationState.areAllVerificationsComplete()) {
            this.mPm.mPendingVerification.remove(verificationId);
        }
    }

    void sendEnableRollbackRequest() {
        com.android.server.pm.PackageManagerService packageManagerService = this.mPm;
        int enableRollbackToken = packageManagerService.mPendingEnableRollbackToken;
        packageManagerService.mPendingEnableRollbackToken = enableRollbackToken + 1;
        android.os.Trace.asyncTraceBegin(262144L, "enable_rollback", enableRollbackToken);
        this.mPm.mPendingEnableRollback.append(enableRollbackToken, this);
        android.content.Intent enableRollbackIntent = new android.content.Intent("android.intent.action.PACKAGE_ENABLE_ROLLBACK");
        enableRollbackIntent.putExtra(android.content.pm.PackageManagerInternal.EXTRA_ENABLE_ROLLBACK_TOKEN, enableRollbackToken);
        enableRollbackIntent.putExtra(android.content.pm.PackageManagerInternal.EXTRA_ENABLE_ROLLBACK_SESSION_ID, this.mSessionId);
        enableRollbackIntent.setType("application/vnd.android.package-archive");
        enableRollbackIntent.addFlags(268435457);
        enableRollbackIntent.addFlags(67108864);
        this.mPm.mContext.sendBroadcastAsUser(enableRollbackIntent, android.os.UserHandle.SYSTEM, "android.permission.PACKAGE_ROLLBACK_AGENT");
        this.mWaitForEnableRollbackToComplete = true;
        long rollbackTimeout = android.provider.DeviceConfig.getLong("rollback", PROPERTY_ENABLE_ROLLBACK_TIMEOUT_MILLIS, 10000L);
        if (rollbackTimeout < 0) {
            rollbackTimeout = 10000;
        }
        android.os.Message msg = this.mPm.mHandler.obtainMessage(22);
        msg.arg1 = enableRollbackToken;
        msg.arg2 = this.mSessionId;
        this.mPm.mHandler.sendMessageDelayed(msg, rollbackTimeout);
    }

    void sendIntegrityVerificationRequest(final int verificationId, final android.content.pm.PackageInfoLite pkgLite, com.android.server.pm.PackageVerificationState verificationState) {
        if (!isIntegrityVerificationEnabled()) {
            verificationState.setIntegrityVerificationResult(1);
            return;
        }
        android.content.Intent integrityVerification = new android.content.Intent("android.intent.action.PACKAGE_NEEDS_INTEGRITY_VERIFICATION");
        integrityVerification.setDataAndType(android.net.Uri.fromFile(new java.io.File(this.mOriginInfo.mResolvedPath)), "application/vnd.android.package-archive");
        integrityVerification.addFlags(1342177281);
        integrityVerification.putExtra("android.content.pm.extra.VERIFICATION_ID", verificationId);
        integrityVerification.putExtra("android.intent.extra.PACKAGE_NAME", pkgLite.packageName);
        integrityVerification.putExtra("android.intent.extra.VERSION_CODE", pkgLite.versionCode);
        integrityVerification.putExtra("android.intent.extra.LONG_VERSION_CODE", pkgLite.getLongVersionCode());
        populateInstallerExtras(integrityVerification);
        integrityVerification.setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        this.mPm.mContext.sendOrderedBroadcastAsUser(integrityVerification, android.os.UserHandle.SYSTEM, null, -1, options.toBundle(), new android.content.BroadcastReceiver() { // from class: com.android.server.pm.VerifyingSession.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                    android.util.Slog.d("PackageManager", "on IntegrityVerification callback: " + (pkgLite == null ? null : pkgLite.packageName));
                }
                android.os.Message msg = com.android.server.pm.VerifyingSession.this.mPm.mHandler.obtainMessage(26);
                msg.arg1 = verificationId;
                com.android.server.pm.VerifyingSession.this.mPm.mHandler.sendMessageDelayed(msg, com.android.server.pm.VerifyingSession.this.getIntegrityVerificationTimeout());
            }
        }, null, 0, null, null);
        android.os.Trace.asyncTraceBegin(262144L, "integrity_verification", verificationId);
        this.mWaitForIntegrityVerificationToComplete = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getIntegrityVerificationTimeout() {
        long timeout = android.provider.Settings.Global.getLong(this.mPm.mContext.getContentResolver(), "app_integrity_verification_timeout", 30000L);
        return java.lang.Math.max(timeout, 30000L);
    }

    private boolean isIntegrityVerificationEnabled() {
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02c8  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02d6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void sendPackageVerificationRequest(int r49, android.content.pm.PackageInfoLite r50, com.android.server.pm.PackageVerificationState r51) {
        /*
            Method dump skipped, instruction units count: 1071
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.VerifyingSession.sendPackageVerificationRequest(int, android.content.pm.PackageInfoLite, com.android.server.pm.PackageVerificationState):void");
    }

    private void startVerificationTimeoutCountdown(int i, boolean z, com.android.server.pm.PackageVerificationResponse packageVerificationResponse, long j) {
        android.os.Message messageObtainMessage = this.mPm.mHandler.obtainMessage(16);
        messageObtainMessage.arg1 = i;
        messageObtainMessage.arg2 = z ? 1 : 0;
        messageObtainMessage.obj = packageVerificationResponse;
        this.mPm.mHandler.sendMessageDelayed(messageObtainMessage, j);
    }

    int getDefaultVerificationResponse() {
        if (this.mPm.mUserManager.hasUserRestriction("ensure_verify_apps", getUser().getIdentifier())) {
            return -1;
        }
        return android.provider.Settings.Global.getInt(this.mPm.mContext.getContentResolver(), "verifier_default_response", 1);
    }

    private boolean packageExists(java.lang.String packageName) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        return snapshot.getPackageStateInternal(packageName) != null;
    }

    private boolean isAdbVerificationEnabled(android.content.pm.PackageInfoLite pkgInfoLite, int userId, boolean requestedDisableVerification) {
        boolean verifierIncludeAdb = android.provider.Settings.Global.getInt(this.mPm.mContext.getContentResolver(), "verifier_verify_adb_installs", 1) != 0;
        if (this.mPm.isUserRestricted(userId, "ensure_verify_apps")) {
            if (!verifierIncludeAdb) {
                android.util.Slog.w("PackageManager", "Force verification of ADB install because of user restriction.");
            }
            return true;
        }
        if (!verifierIncludeAdb) {
            return false;
        }
        if (requestedDisableVerification && packageExists(pkgInfoLite.packageName)) {
            return !pkgInfoLite.debuggable;
        }
        return true;
    }

    private boolean isVerificationEnabled(android.content.pm.PackageInfoLite pkgInfoLite, int userId, java.util.List<java.lang.String> requiredVerifierPackages) {
        int installerUid = this.mVerificationInfo == null ? -1 : this.mVerificationInfo.mInstallerUid;
        boolean requestedDisableVerification = (this.mInstallFlags & 524288) != 0;
        if ((this.mInstallFlags & 32) != 0) {
            return isAdbVerificationEnabled(pkgInfoLite, userId, requestedDisableVerification);
        }
        if (requestedDisableVerification) {
            return false;
        }
        if (isInstant() && this.mPm.mInstantAppInstallerActivity != null) {
            java.lang.String installerPackage = this.mPm.mInstantAppInstallerActivity.packageName;
            for (java.lang.String requiredVerifierPackage : requiredVerifierPackages) {
                if (installerPackage.equals(requiredVerifierPackage)) {
                    try {
                        ((android.app.AppOpsManager) this.mPm.mInjector.getSystemService(android.app.AppOpsManager.class)).checkPackage(installerUid, requiredVerifierPackage);
                        if (com.android.server.pm.PackageManagerService.DEBUG_VERIFY) {
                            android.util.Slog.i("PackageManager", "disable verification for instant app");
                        }
                        return false;
                    } catch (java.lang.SecurityException e) {
                    }
                }
            }
        }
        return true;
    }

    private java.util.List<android.content.ComponentName> matchVerifiers(android.content.pm.PackageInfoLite pkgInfo, java.util.List<android.content.pm.ResolveInfo> receivers, com.android.server.pm.PackageVerificationState verificationState) {
        int verifierUid;
        if (pkgInfo.verifiers == null || pkgInfo.verifiers.length == 0) {
            return null;
        }
        int n = pkgInfo.verifiers.length;
        java.util.List<android.content.ComponentName> sufficientVerifiers = new java.util.ArrayList<>(n + 1);
        for (int i = 0; i < n; i++) {
            android.content.pm.VerifierInfo verifierInfo = pkgInfo.verifiers[i];
            android.content.ComponentName comp = matchComponentForVerifier(verifierInfo.packageName, receivers);
            if (comp != null && (verifierUid = this.mPm.getUidForVerifier(verifierInfo)) != -1) {
                if (com.android.server.pm.PackageManagerService.DEBUG_VERIFY) {
                    android.util.Slog.d("PackageManager", "Added sufficient verifier " + verifierInfo.packageName + " with the correct signature");
                }
                sufficientVerifiers.add(comp);
                verificationState.addSufficientVerifier(verifierUid);
            }
        }
        return sufficientVerifiers;
    }

    private static android.content.ComponentName matchComponentForVerifier(java.lang.String packageName, java.util.List<android.content.pm.ResolveInfo> receivers) {
        android.content.pm.ActivityInfo targetReceiver = null;
        int nr = receivers.size();
        int i = 0;
        while (true) {
            if (i >= nr) {
                break;
            }
            android.content.pm.ResolveInfo info = receivers.get(i);
            if (info.activityInfo == null || !packageName.equals(info.activityInfo.packageName)) {
                i++;
            } else {
                targetReceiver = info.activityInfo;
                break;
            }
        }
        if (targetReceiver == null) {
            return null;
        }
        return new android.content.ComponentName(targetReceiver.packageName, targetReceiver.name);
    }

    void populateInstallerExtras(android.content.Intent intent) {
        intent.putExtra("android.content.pm.extra.VERIFICATION_INSTALLER_PACKAGE", this.mInstallSource.mInitiatingPackageName);
        if (this.mVerificationInfo != null) {
            if (this.mVerificationInfo.mOriginatingUri != null) {
                intent.putExtra("android.intent.extra.ORIGINATING_URI", this.mVerificationInfo.mOriginatingUri);
            }
            if (this.mVerificationInfo.mReferrer != null) {
                intent.putExtra("android.intent.extra.REFERRER", this.mVerificationInfo.mReferrer);
            }
            if (this.mVerificationInfo.mOriginatingUid >= 0) {
                intent.putExtra("android.intent.extra.ORIGINATING_UID", this.mVerificationInfo.mOriginatingUid);
            }
            if (this.mVerificationInfo.mInstallerUid >= 0) {
                intent.putExtra("android.content.pm.extra.VERIFICATION_INSTALLER_UID", this.mVerificationInfo.mInstallerUid);
            }
        }
    }

    void setReturnCode(int ret, java.lang.String message) {
        if (this.mRet == 1) {
            this.mRet = ret;
            this.mErrorMessage = message;
        }
    }

    void handleVerificationFinished() {
        this.mWaitForVerificationToComplete = false;
        handleReturnCode();
    }

    void handleIntegrityVerificationFinished() {
        this.mWaitForIntegrityVerificationToComplete = false;
        handleReturnCode();
    }

    void handleRollbackEnabled() {
        this.mWaitForEnableRollbackToComplete = false;
        handleReturnCode();
    }

    void handleReturnCode() {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "handleReturnCode in VerificationParams: " + this.mWaitForVerificationToComplete + ", " + this.mWaitForIntegrityVerificationToComplete + ", " + this.mWaitForEnableRollbackToComplete);
        }
        if (this.mWaitForVerificationToComplete || this.mWaitForIntegrityVerificationToComplete || this.mWaitForEnableRollbackToComplete) {
            return;
        }
        sendVerificationCompleteNotification();
        if (this.mRet != 1) {
            com.android.server.pm.PackageMetrics.onVerificationFailed(this);
        }
    }

    private void sendVerificationCompleteNotification() {
        if (this.mParentVerifyingSession != null) {
            this.mParentVerifyingSession.trySendVerificationCompleteNotification(this);
            return;
        }
        try {
            this.mObserver.onPackageInstalled((java.lang.String) null, this.mRet, this.mErrorMessage, new android.os.Bundle());
        } catch (android.os.RemoteException e) {
            android.util.Slog.i("PackageManager", "Observer no longer exists.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.i("PackageManager", "start " + this.mUser + ": " + this);
        }
        android.os.Trace.asyncTraceEnd(262144L, "queueVerify", java.lang.System.identityHashCode(this));
        android.os.Trace.traceBegin(262144L, "start");
        handleStartVerify();
        handleReturnCode();
        android.os.Trace.traceEnd(262144L);
    }

    public void verifyStage() {
        android.os.Trace.asyncTraceBegin(262144L, "queueVerify", java.lang.System.identityHashCode(this));
        this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.VerifyingSession$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.start();
            }
        });
    }

    public void verifyStage(java.util.List<com.android.server.pm.VerifyingSession> children) throws com.android.server.pm.PackageManagerException {
        final com.android.server.pm.MultiPackageVerifyingSession multiPackageVerifyingSession = new com.android.server.pm.MultiPackageVerifyingSession(this, children);
        android.os.Handler handler = this.mPm.mHandler;
        java.util.Objects.requireNonNull(multiPackageVerifyingSession);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.pm.VerifyingSession$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                multiPackageVerifyingSession.start();
            }
        });
    }

    public int getRet() {
        return this.mRet;
    }

    public java.lang.String getErrorMessage() {
        return this.mErrorMessage;
    }

    public android.os.UserHandle getUser() {
        return this.mUser;
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public int getDataLoaderType() {
        return this.mDataLoaderType;
    }

    public int getUserActionRequiredType() {
        return this.mUserActionRequiredType;
    }

    public boolean isInstant() {
        return (this.mInstallFlags & 2048) != 0;
    }

    public boolean isInherit() {
        return this.mIsInherit;
    }

    public int getInstallerPackageUid() {
        return this.mInstallSource.mInstallerPackageUid;
    }

    public boolean isApex() {
        return (this.mInstallFlags & 131072) != 0;
    }

    public boolean isArchivedInstallation() {
        return (this.mInstallFlags & 134217728) != 0;
    }

    public boolean isStaged() {
        return this.mIsStaged;
    }

    public com.android.server.pm.IVerifyingSessionWrapper getWrapper() {
        return this.mWrapper;
    }

    private class VerifyingSessionWrapper implements com.android.server.pm.IVerifyingSessionWrapper {
        private VerifyingSessionWrapper() {
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public int getRet() {
            return com.android.server.pm.VerifyingSession.this.mRet;
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public android.content.pm.parsing.PackageLite getPackageLite() {
            return com.android.server.pm.VerifyingSession.this.mPackageLite;
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public com.android.server.pm.InstallSource getInstallSource() {
            return com.android.server.pm.VerifyingSession.this.mInstallSource;
        }

        @Override // com.android.server.pm.IVerifyingSessionWrapper
        public android.os.UserHandle getUser() {
            return com.android.server.pm.VerifyingSession.this.mUser;
        }
    }
}
