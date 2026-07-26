package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ReconcileFailure extends com.android.server.pm.PackageManagerException {
    public static com.android.server.pm.ReconcileFailure ofInternalError(java.lang.String message, int internalErrorCode) {
        return new com.android.server.pm.ReconcileFailure(message, internalErrorCode);
    }

    private ReconcileFailure(java.lang.String message, int internalErrorCode) {
        super(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Reconcile failed: " + message, internalErrorCode);
    }

    ReconcileFailure(int reason, java.lang.String message) {
        super(reason, "Reconcile failed: " + message);
    }

    ReconcileFailure(com.android.server.pm.PackageManagerException e) {
        this(e.error, e.getMessage());
    }
}
