package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class OneTimeSafetyChecker implements android.app.admin.DevicePolicySafetyChecker {
    private static final long SELF_DESTRUCT_TIMEOUT_MS = 10000;
    private static final java.lang.String TAG = com.android.server.devicepolicy.OneTimeSafetyChecker.class.getSimpleName();
    private boolean mDone;
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final int mOperation;
    private final android.app.admin.DevicePolicySafetyChecker mRealSafetyChecker;
    private final int mReason;
    private final com.android.server.devicepolicy.DevicePolicyManagerService mService;

    OneTimeSafetyChecker(com.android.server.devicepolicy.DevicePolicyManagerService service, int operation, int reason) {
        this.mService = (com.android.server.devicepolicy.DevicePolicyManagerService) java.util.Objects.requireNonNull(service);
        this.mOperation = operation;
        this.mReason = reason;
        this.mRealSafetyChecker = service.getDevicePolicySafetyChecker();
        android.util.Slog.i(TAG, "OneTimeSafetyChecker constructor: operation=" + android.app.admin.DevicePolicyManager.operationToString(operation) + ", reason=" + android.app.admin.DevicePolicyManager.operationSafetyReasonToString(reason) + ", realChecker=" + this.mRealSafetyChecker + ", maxDuration=10000ms");
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.devicepolicy.OneTimeSafetyChecker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        }, 10000L);
    }

    public int getUnsafeOperationReason(int operation) {
        java.lang.String name = android.app.admin.DevicePolicyManager.operationToString(operation);
        android.util.Slog.i(TAG, "getUnsafeOperationReason(" + name + ")");
        int reason = -1;
        if (operation == this.mOperation) {
            reason = this.mReason;
        } else {
            android.util.Slog.wtf(TAG, "invalid call to isDevicePolicyOperationSafe(): asked for " + name + ", should be " + android.app.admin.DevicePolicyManager.operationToString(this.mOperation));
        }
        java.lang.String reasonName = android.app.admin.DevicePolicyManager.operationSafetyReasonToString(reason);
        android.app.admin.DevicePolicyManagerLiteInternal dpmi = (android.app.admin.DevicePolicyManagerLiteInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerLiteInternal.class);
        android.util.Slog.i(TAG, "notifying " + reasonName + " is UNSAFE");
        dpmi.notifyUnsafeOperationStateChanged(this, reason, false);
        android.util.Slog.i(TAG, "notifying " + reasonName + " is SAFE");
        dpmi.notifyUnsafeOperationStateChanged(this, reason, true);
        android.util.Slog.i(TAG, "returning " + reasonName);
        disableSelf();
        return reason;
    }

    public boolean isSafeOperation(int reason) {
        boolean safe = this.mReason != reason;
        android.util.Slog.i(TAG, "isSafeOperation(" + android.app.admin.DevicePolicyManager.operationSafetyReasonToString(reason) + "): " + safe);
        disableSelf();
        return safe;
    }

    public void onFactoryReset(com.android.internal.os.IResultReceiver callback) {
        throw new java.lang.UnsupportedOperationException();
    }

    private void disableSelf() {
        if (this.mDone) {
            android.util.Slog.w(TAG, "disableSelf(): already disabled");
            return;
        }
        android.util.Slog.i(TAG, "restoring DevicePolicySafetyChecker to " + this.mRealSafetyChecker);
        this.mService.setDevicePolicySafetyCheckerUnchecked(this.mRealSafetyChecker);
        this.mDone = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: selfDestruct, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        if (this.mDone) {
            return;
        }
        android.util.Slog.e(TAG, "Self destructing " + this + ", as it was not automatically disabled");
        disableSelf();
    }

    public java.lang.String toString() {
        return "OneTimeSafetyChecker[id=" + java.lang.System.identityHashCode(this) + ", reason=" + android.app.admin.DevicePolicyManager.operationSafetyReasonToString(this.mReason) + ", operation=" + android.app.admin.DevicePolicyManager.operationToString(this.mOperation) + ']';
    }
}
