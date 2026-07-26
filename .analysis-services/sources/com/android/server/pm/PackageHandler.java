package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PackageHandler extends android.os.Handler {
    private final com.android.server.pm.PackageManagerService mPm;

    PackageHandler(android.os.Looper looper, com.android.server.pm.PackageManagerService pm) {
        super(looper);
        this.mPm = pm;
    }

    @Override // android.os.Handler
    public void handleMessage(android.os.Message msg) {
        try {
            doHandleMessage(msg);
        } finally {
            android.os.Process.setThreadPriority(0);
        }
    }

    void doHandleMessage(android.os.Message msg) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "doHandleMessage: " + msg.what);
        }
        switch (msg.what) {
            case 1:
                this.mPm.sendPendingBroadcasts();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 14:
            case 17:
            case 18:
            default:
                this.mPm.mPackageManagerServiceExt.customHandleMsgInPackageHandler(msg);
                break;
            case 9:
                if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                    android.util.Log.v("PackageManager", "Handling post-install for " + msg.arg1);
                }
                com.android.server.pm.InstallRequest request = this.mPm.mRunningInstalls.get(msg.arg1);
                boolean didRestore = msg.arg2 != 0;
                this.mPm.mRunningInstalls.delete(msg.arg1);
                if (request == null) {
                    if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                        android.util.Slog.i("PackageManager", "InstallRequest is null. Nothing to do for post-install token " + msg.arg1);
                    }
                } else {
                    request.closeFreezer();
                    request.onInstallCompleted();
                    request.runPostInstallRunnable();
                    if (!request.isInstallExistingForUser()) {
                        this.mPm.handlePackagePostInstall(request, didRestore);
                        this.mPm.mPackageManagerServiceExt.writeMdmLog("005", request.getReturnCode() == 1 ? "1" : "0", request.getPkg() != null ? request.getPkg().getPackageName() : request.getName());
                        this.mPm.mPackageManagerServiceExt.afterHandlePackagePostInstallInCasePostInstall(request);
                    } else if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                        android.util.Slog.i("PackageManager", "Nothing to do for post-install token " + msg.arg1);
                    }
                    android.os.Trace.asyncTraceEnd(262144L, "postInstall", msg.arg1);
                }
                break;
            case 13:
                this.mPm.writeSettings(false);
                break;
            case 15:
                int verificationId = msg.arg1;
                com.android.server.pm.PackageVerificationState state = this.mPm.mPendingVerification.get(verificationId);
                if (state == null) {
                    android.util.Slog.w("PackageManager", "Verification with id " + verificationId + " not found. It may be invalid or overridden by integrity verification");
                } else if (state.isVerificationComplete()) {
                    android.util.Slog.w("PackageManager", "Verification with id " + verificationId + " already complete.");
                } else {
                    com.android.server.pm.VerificationUtils.processVerificationResponse(verificationId, state, (com.android.server.pm.PackageVerificationResponse) msg.obj, this.mPm);
                }
                break;
            case 16:
                int verificationId2 = msg.arg1;
                boolean streaming = msg.arg2 != 0;
                com.android.server.pm.PackageVerificationState state2 = this.mPm.mPendingVerification.get(verificationId2);
                if (state2 != null && !state2.isVerificationComplete()) {
                    com.android.server.pm.PackageVerificationResponse response = (com.android.server.pm.PackageVerificationResponse) msg.obj;
                    if (streaming || !state2.timeoutExtended(response.callerUid)) {
                        com.android.server.pm.VerificationUtils.processVerificationResponseOnTimeout(verificationId2, state2, response, this.mPm);
                    }
                    break;
                }
                break;
            case 19:
                this.mPm.writePackageList(msg.arg1);
                break;
            case 20:
                com.android.server.pm.InstantAppResolver.doInstantAppResolutionPhaseTwo(this.mPm.mContext, this.mPm.snapshotComputer(), this.mPm.mUserManager, this.mPm.mInstantAppResolverConnection, (android.content.pm.InstantAppRequest) msg.obj, this.mPm.mInstantAppInstallerActivity, this.mPm.mHandler);
                break;
            case 21:
                int enableRollbackToken = msg.arg1;
                int enableRollbackCode = msg.arg2;
                com.android.server.pm.VerifyingSession params = this.mPm.mPendingEnableRollback.get(enableRollbackToken);
                if (params == null) {
                    android.util.Slog.w("PackageManager", "Invalid rollback enabled token " + enableRollbackToken + " received");
                } else {
                    this.mPm.mPendingEnableRollback.remove(enableRollbackToken);
                    if (enableRollbackCode != 1) {
                        android.net.Uri originUri = android.net.Uri.fromFile(params.mOriginInfo.mResolvedFile);
                        android.util.Slog.w("PackageManager", "Failed to enable rollback for " + originUri);
                        android.util.Slog.w("PackageManager", "Continuing with installation of " + originUri);
                    }
                    android.os.Trace.asyncTraceEnd(262144L, "enable_rollback", enableRollbackToken);
                    params.handleRollbackEnabled();
                }
                break;
            case 22:
                int enableRollbackToken2 = msg.arg1;
                int sessionId = msg.arg2;
                com.android.server.pm.VerifyingSession params2 = this.mPm.mPendingEnableRollback.get(enableRollbackToken2);
                if (params2 != null) {
                    android.net.Uri originUri2 = android.net.Uri.fromFile(params2.mOriginInfo.mResolvedFile);
                    android.util.Slog.w("PackageManager", "Enable rollback timed out for " + originUri2);
                    this.mPm.mPendingEnableRollback.remove(enableRollbackToken2);
                    android.util.Slog.w("PackageManager", "Continuing with installation of " + originUri2);
                    android.os.Trace.asyncTraceEnd(262144L, "enable_rollback", enableRollbackToken2);
                    params2.handleRollbackEnabled();
                    android.content.Intent rollbackTimeoutIntent = new android.content.Intent("android.intent.action.CANCEL_ENABLE_ROLLBACK");
                    rollbackTimeoutIntent.putExtra(android.content.pm.PackageManagerInternal.EXTRA_ENABLE_ROLLBACK_SESSION_ID, sessionId);
                    rollbackTimeoutIntent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
                    this.mPm.mContext.sendBroadcastAsUser(rollbackTimeoutIntent, android.os.UserHandle.SYSTEM, "android.permission.PACKAGE_ROLLBACK_AGENT");
                }
                break;
            case 23:
                com.android.server.pm.CleanUpArgs args = (com.android.server.pm.CleanUpArgs) msg.obj;
                if (args != null) {
                    this.mPm.cleanUpResources(args.getPackageName(), args.getCodeFile(), args.getInstructionSets());
                }
                break;
            case 24:
            case 29:
                java.lang.String packageName = (java.lang.String) msg.obj;
                if (packageName != null) {
                    boolean killApp = msg.what == 29;
                    this.mPm.notifyInstallObserver(packageName, killApp);
                }
                break;
            case 25:
                int verificationId3 = msg.arg1;
                com.android.server.pm.PackageVerificationState state3 = this.mPm.mPendingVerification.get(verificationId3);
                if (state3 == null) {
                    android.util.Slog.w("PackageManager", "Integrity verification with id " + verificationId3 + " not found. It may be invalid or overridden by verifier");
                } else {
                    int response2 = ((java.lang.Integer) msg.obj).intValue();
                    com.android.server.pm.VerifyingSession verifyingSession = state3.getVerifyingSession();
                    android.net.Uri originUri3 = android.net.Uri.fromFile(verifyingSession.mOriginInfo.mResolvedFile);
                    state3.setIntegrityVerificationResult(response2);
                    if (response2 == 1) {
                        android.util.Slog.i("PackageManager", "Integrity check passed for " + originUri3);
                    } else {
                        verifyingSession.setReturnCode(-22, "Integrity check failed for " + originUri3);
                    }
                    if (state3.areAllVerificationsComplete()) {
                        this.mPm.mPendingVerification.remove(verificationId3);
                    }
                    android.os.Trace.asyncTraceEnd(262144L, "integrity_verification", verificationId3);
                    verifyingSession.handleIntegrityVerificationFinished();
                }
                break;
            case 26:
                int messageCode = msg.arg1;
                com.android.server.pm.PackageVerificationState state4 = this.mPm.mPendingVerification.get(messageCode);
                if (state4 != null && !state4.isIntegrityVerificationComplete()) {
                    com.android.server.pm.VerifyingSession verifyingSession2 = state4.getVerifyingSession();
                    android.net.Uri originUri4 = android.net.Uri.fromFile(verifyingSession2.mOriginInfo.mResolvedFile);
                    java.lang.String errorMsg = "Integrity verification timed out for " + originUri4;
                    android.util.Slog.i("PackageManager", errorMsg);
                    state4.setIntegrityVerificationResult(getDefaultIntegrityVerificationResponse());
                    if (getDefaultIntegrityVerificationResponse() == 1) {
                        android.util.Slog.i("PackageManager", "Integrity check times out, continuing with " + originUri4);
                    } else {
                        verifyingSession2.setReturnCode(-22, errorMsg);
                    }
                    if (state4.areAllVerificationsComplete()) {
                        this.mPm.mPendingVerification.remove(messageCode);
                    }
                    android.os.Trace.asyncTraceEnd(262144L, "integrity_verification", messageCode);
                    verifyingSession2.handleIntegrityVerificationFinished();
                    break;
                }
                break;
            case 27:
                int messageCode2 = msg.arg1;
                java.lang.Object object = msg.obj;
                this.mPm.mDomainVerificationManager.runMessage(messageCode2, object);
                break;
            case 28:
                try {
                    this.mPm.mInjector.getSharedLibrariesImpl().pruneUnusedStaticSharedLibraries(this.mPm.snapshotComputer(), Long.MAX_VALUE, android.provider.Settings.Global.getLong(this.mPm.mContext.getContentResolver(), "unused_static_shared_lib_min_cache_period", com.android.server.pm.PackageManagerService.DEFAULT_UNUSED_STATIC_SHARED_LIB_MIN_CACHE_PERIOD));
                } catch (java.io.IOException e) {
                    android.util.Log.w("PackageManager", "Failed to prune unused static shared libraries :" + e.getMessage());
                    return;
                }
                break;
        }
    }

    private int getDefaultIntegrityVerificationResponse() {
        return -1;
    }
}
