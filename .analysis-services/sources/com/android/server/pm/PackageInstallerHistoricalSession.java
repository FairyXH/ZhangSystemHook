package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class PackageInstallerHistoricalSession {
    private final int mBridges;
    private final int[] mChildSessionIds;
    private final float mClientProgress;
    private final boolean mCommitted;
    private final long mCommittedMillis;
    private final long mCreatedMillis;
    private final boolean mDestroyed;
    private final int mFds;
    private final java.lang.String mFinalMessage;
    private final int mFinalStatus;
    private final com.android.server.pm.InstallSource mInstallSource;
    private final int mInstallerUid;
    private final java.lang.String mOriginalInstallerPackageName;
    private final int mOriginalInstallerUid;
    private final java.lang.String mPackageName;
    private final java.lang.String mParams;
    private final int mParentSessionId;
    private final boolean mPermissionsManuallyAccepted;
    private final java.lang.String mPreVerifiedDomains;
    private final java.lang.String mPreapprovalDetails;
    private final boolean mPreapprovalRequested;
    private final float mProgress;
    private final boolean mSealed;
    private final boolean mSessionApplied;
    private final int mSessionErrorCode;
    private final java.lang.String mSessionErrorMessage;
    private final boolean mSessionFailed;
    private final boolean mSessionReady;
    private final java.lang.String mStageCid;
    private final java.io.File mStageDir;
    private final boolean mStageDirInUse;
    private final long mUpdatedMillis;
    public final int sessionId;
    public final int userId;

    PackageInstallerHistoricalSession(int sessionId, int userId, int originalInstallerUid, java.lang.String originalInstallerPackageName, com.android.server.pm.InstallSource installSource, int installerUid, long createdMillis, long updatedMillis, long committedMillis, java.io.File stageDir, java.lang.String stageCid, float clientProgress, float progress, boolean committed, boolean preapprovalRequested, boolean sealed, boolean permissionsManuallyAccepted, boolean stageDirInUse, boolean destroyed, int fds, int bridges, int finalStatus, java.lang.String finalMessage, android.content.pm.PackageInstaller.SessionParams params, int parentSessionId, int[] childSessionIds, boolean sessionApplied, boolean sessionFailed, boolean sessionReady, int sessionErrorCode, java.lang.String sessionErrorMessage, android.content.pm.PackageInstaller.PreapprovalDetails preapprovalDetails, android.content.pm.verify.domain.DomainSet preVerifiedDomains, java.lang.String packageNameFromApk) {
        java.lang.String str;
        java.lang.String packageName;
        this.sessionId = sessionId;
        this.userId = userId;
        this.mOriginalInstallerUid = originalInstallerUid;
        this.mOriginalInstallerPackageName = originalInstallerPackageName;
        this.mInstallSource = installSource;
        this.mInstallerUid = installerUid;
        this.mCreatedMillis = createdMillis;
        this.mUpdatedMillis = updatedMillis;
        this.mCommittedMillis = committedMillis;
        this.mStageDir = stageDir;
        this.mStageCid = stageCid;
        this.mClientProgress = clientProgress;
        this.mProgress = progress;
        this.mCommitted = committed;
        this.mPreapprovalRequested = preapprovalRequested;
        this.mSealed = sealed;
        this.mPermissionsManuallyAccepted = permissionsManuallyAccepted;
        this.mStageDirInUse = stageDirInUse;
        this.mDestroyed = destroyed;
        this.mFds = fds;
        this.mBridges = bridges;
        this.mFinalStatus = finalStatus;
        this.mFinalMessage = finalMessage;
        java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
        com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "    ");
        params.dump(pw);
        this.mParams = writer.toString();
        this.mParentSessionId = parentSessionId;
        this.mChildSessionIds = childSessionIds;
        this.mSessionApplied = sessionApplied;
        this.mSessionFailed = sessionFailed;
        this.mSessionReady = sessionReady;
        this.mSessionErrorCode = sessionErrorCode;
        this.mSessionErrorMessage = sessionErrorMessage;
        if (preapprovalDetails != null) {
            this.mPreapprovalDetails = preapprovalDetails.toString();
            str = null;
        } else {
            str = null;
            this.mPreapprovalDetails = null;
        }
        if (preVerifiedDomains != null) {
            this.mPreVerifiedDomains = java.lang.String.join(",", preVerifiedDomains.getDomains());
        } else {
            this.mPreVerifiedDomains = str;
        }
        if (preapprovalDetails != null) {
            packageName = preapprovalDetails.getPackageName();
        } else {
            packageName = packageNameFromApk != null ? packageNameFromApk : params.appPackageName;
        }
        this.mPackageName = packageName;
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("Session " + this.sessionId + ":");
        pw.increaseIndent();
        pw.printPair("userId", java.lang.Integer.valueOf(this.userId));
        pw.printPair("mOriginalInstallerUid", java.lang.Integer.valueOf(this.mOriginalInstallerUid));
        pw.printPair("mOriginalInstallerPackageName", this.mOriginalInstallerPackageName);
        pw.printPair("installerPackageName", this.mInstallSource.mInstallerPackageName);
        pw.printPair("installInitiatingPackageName", this.mInstallSource.mInitiatingPackageName);
        pw.printPair("installOriginatingPackageName", this.mInstallSource.mOriginatingPackageName);
        pw.printPair("mInstallerUid", java.lang.Integer.valueOf(this.mInstallerUid));
        pw.printPair("createdMillis", java.lang.Long.valueOf(this.mCreatedMillis));
        pw.printPair("updatedMillis", java.lang.Long.valueOf(this.mUpdatedMillis));
        pw.printPair("committedMillis", java.lang.Long.valueOf(this.mCommittedMillis));
        pw.printPair("stageDir", this.mStageDir);
        pw.printPair("stageCid", this.mStageCid);
        pw.println();
        pw.print(this.mParams);
        pw.printPair("mClientProgress", java.lang.Float.valueOf(this.mClientProgress));
        pw.printPair("mProgress", java.lang.Float.valueOf(this.mProgress));
        pw.printPair("mCommitted", java.lang.Boolean.valueOf(this.mCommitted));
        pw.printPair("mPreapprovalRequested", java.lang.Boolean.valueOf(this.mPreapprovalRequested));
        pw.printPair("mSealed", java.lang.Boolean.valueOf(this.mSealed));
        pw.printPair("mPermissionsManuallyAccepted", java.lang.Boolean.valueOf(this.mPermissionsManuallyAccepted));
        pw.printPair("mStageDirInUse", java.lang.Boolean.valueOf(this.mStageDirInUse));
        pw.printPair("mDestroyed", java.lang.Boolean.valueOf(this.mDestroyed));
        pw.printPair("mFds", java.lang.Integer.valueOf(this.mFds));
        pw.printPair("mBridges", java.lang.Integer.valueOf(this.mBridges));
        pw.printPair("mFinalStatus", java.lang.Integer.valueOf(this.mFinalStatus));
        pw.printPair("mFinalMessage", this.mFinalMessage);
        pw.printPair("mParentSessionId", java.lang.Integer.valueOf(this.mParentSessionId));
        pw.printPair("mChildSessionIds", this.mChildSessionIds);
        pw.printPair("mSessionApplied", java.lang.Boolean.valueOf(this.mSessionApplied));
        pw.printPair("mSessionFailed", java.lang.Boolean.valueOf(this.mSessionFailed));
        pw.printPair("mSessionReady", java.lang.Boolean.valueOf(this.mSessionReady));
        pw.printPair("mSessionErrorCode", java.lang.Integer.valueOf(this.mSessionErrorCode));
        pw.printPair("mSessionErrorMessage", this.mSessionErrorMessage);
        pw.printPair("mPreapprovalDetails", this.mPreapprovalDetails);
        pw.printPair("mPreVerifiedDomains", this.mPreVerifiedDomains);
        pw.printPair("mAppPackageName", this.mPackageName);
        pw.println();
        pw.decreaseIndent();
    }

    public android.content.pm.PackageInstaller.SessionInfo generateInfo() {
        android.content.pm.PackageInstaller.SessionInfo info = new android.content.pm.PackageInstaller.SessionInfo();
        info.sessionId = this.sessionId;
        info.userId = this.userId;
        info.installerPackageName = this.mInstallSource.mInstallerPackageName;
        info.installerAttributionTag = this.mInstallSource.mInstallerAttributionTag;
        info.progress = this.mProgress;
        info.sealed = this.mSealed;
        info.isCommitted = this.mCommitted;
        info.isPreapprovalRequested = this.mPreapprovalRequested;
        info.parentSessionId = this.mParentSessionId;
        info.childSessionIds = this.mChildSessionIds;
        info.isSessionApplied = this.mSessionApplied;
        info.isSessionReady = this.mSessionReady;
        info.isSessionFailed = this.mSessionFailed;
        info.setSessionErrorCode(this.mSessionErrorCode, this.mSessionErrorMessage);
        info.createdMillis = this.mCreatedMillis;
        info.updatedMillis = this.mUpdatedMillis;
        info.installerUid = this.mInstallerUid;
        info.appPackageName = this.mPackageName;
        return info;
    }
}
