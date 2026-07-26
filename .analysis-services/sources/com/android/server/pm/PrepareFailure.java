package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PrepareFailure extends com.android.server.pm.PackageManagerException {
    public java.lang.String mConflictingPackage;
    public java.lang.String mConflictingPermission;

    PrepareFailure(int error) {
        super(error, "Failed to prepare for install.");
    }

    PrepareFailure(int error, java.lang.String detailMessage) {
        super(error, detailMessage);
    }

    public static com.android.server.pm.PrepareFailure ofInternalError(java.lang.String detailMessage, int internalErrorCode) {
        return new com.android.server.pm.PrepareFailure(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, detailMessage, internalErrorCode);
    }

    private PrepareFailure(int error, java.lang.String message, int internalErrorCode) {
        super(error, message, internalErrorCode);
    }

    PrepareFailure(java.lang.String message, java.lang.Exception e) {
        super(e instanceof com.android.server.pm.PackageManagerException ? ((com.android.server.pm.PackageManagerException) e).error : android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, android.util.ExceptionUtils.getCompleteMessage(message, e));
    }

    com.android.server.pm.PrepareFailure conflictsWithExistingPermission(java.lang.String conflictingPermission, java.lang.String conflictingPackage) {
        this.mConflictingPermission = conflictingPermission;
        this.mConflictingPackage = conflictingPackage;
        return this;
    }
}
