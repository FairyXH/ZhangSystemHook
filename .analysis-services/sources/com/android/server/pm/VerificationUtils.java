package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class VerificationUtils {
    private static final long DEFAULT_STREAMING_VERIFICATION_TIMEOUT = 3000;
    private static final long DEFAULT_VERIFICATION_TIMEOUT = 10000;

    VerificationUtils() {
    }

    public static long getVerificationTimeout(android.content.Context context, boolean streaming) {
        if (streaming) {
            return getDefaultStreamingVerificationTimeout(context);
        }
        return getDefaultVerificationTimeout(context);
    }

    public static long getDefaultVerificationTimeout(android.content.Context context) {
        long timeout = android.provider.Settings.Global.getLong(context.getContentResolver(), "verifier_timeout", 10000L);
        return java.lang.Math.max(timeout, 10000L);
    }

    public static long getDefaultStreamingVerificationTimeout(android.content.Context context) {
        long timeout = android.provider.Settings.Global.getLong(context.getContentResolver(), "streaming_verifier_timeout", 3000L);
        return java.lang.Math.max(timeout, 3000L);
    }

    public static void broadcastPackageVerified(int verificationId, android.net.Uri packageUri, int verificationCode, java.lang.String rootHashString, int dataLoaderType, android.os.UserHandle user, android.content.Context context) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.PACKAGE_VERIFIED");
        intent.setDataAndType(packageUri, "application/vnd.android.package-archive");
        intent.addFlags(1);
        intent.putExtra("android.content.pm.extra.VERIFICATION_ID", verificationId);
        intent.putExtra("android.content.pm.extra.VERIFICATION_RESULT", verificationCode);
        if (rootHashString != null) {
            intent.putExtra("android.content.pm.extra.VERIFICATION_ROOT_HASH", rootHashString);
        }
        intent.putExtra("android.content.pm.extra.DATA_LOADER_TYPE", dataLoaderType);
        context.sendBroadcastAsUser(intent, user, "android.permission.PACKAGE_VERIFICATION_AGENT");
    }

    static void processVerificationResponseOnTimeout(int verificationId, com.android.server.pm.PackageVerificationState state, com.android.server.pm.PackageVerificationResponse response, com.android.server.pm.PackageManagerService pms) {
        state.setVerifierResponseOnTimeout(response.callerUid, response.code);
        processVerificationResponse(verificationId, state, response.code, "Verification timed out", pms);
    }

    static void processVerificationResponse(int verificationId, com.android.server.pm.PackageVerificationState state, com.android.server.pm.PackageVerificationResponse response, com.android.server.pm.PackageManagerService pms) {
        state.setVerifierResponse(response.callerUid, response.code);
        processVerificationResponse(verificationId, state, response.code, "Install not allowed", pms);
    }

    private static void processVerificationResponse(int verificationId, com.android.server.pm.PackageVerificationState state, int verificationResult, java.lang.String failureReason, com.android.server.pm.PackageManagerService pms) {
        if (!state.isVerificationComplete()) {
            return;
        }
        com.android.server.pm.VerifyingSession verifyingSession = state.getVerifyingSession();
        android.net.Uri originUri = verifyingSession != null ? android.net.Uri.fromFile(verifyingSession.mOriginInfo.mResolvedFile) : null;
        int verificationCode = state.isInstallAllowed() ? verificationResult : -1;
        if (pms != null && verifyingSession != null) {
            broadcastPackageVerified(verificationId, originUri, verificationCode, null, verifyingSession.getDataLoaderType(), verifyingSession.getUser(), pms.mContext);
        }
        if (state.isInstallAllowed()) {
            android.util.Slog.i("PackageManager", "Continuing with installation of " + originUri);
        } else {
            java.lang.String errorMsg = failureReason + " for " + originUri;
            android.util.Slog.i("PackageManager", errorMsg);
            if (verifyingSession != null) {
                verifyingSession.setReturnCode(-22, errorMsg);
                pms.mPackageManagerServiceExt.afterSetVerifyFailInCasePackageVerified(verifyingSession);
            }
        }
        if (pms != null && state.areAllVerificationsComplete()) {
            pms.mPendingVerification.remove(verificationId);
        }
        android.os.Trace.asyncTraceEnd(262144L, "verification", verificationId);
        if (verifyingSession != null) {
            verifyingSession.handleVerificationFinished();
        }
    }
}
