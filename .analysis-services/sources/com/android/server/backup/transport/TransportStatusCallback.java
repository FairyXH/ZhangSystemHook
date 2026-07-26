package com.android.server.backup.transport;

/* JADX INFO: loaded from: classes.dex */
public class TransportStatusCallback extends com.android.internal.backup.ITransportStatusCallback.Stub {
    private static final int OPERATION_STATUS_DEFAULT = 0;
    private static final java.lang.String TAG = "TransportStatusCallback";
    private boolean mHasCompletedOperation;
    private int mOperationStatus;
    private final long mOperationTimeout;

    public TransportStatusCallback() {
        this.mOperationStatus = 0;
        this.mHasCompletedOperation = false;
        this.mOperationTimeout = com.android.server.backup.BackupAndRestoreFeatureFlags.getBackupTransportCallbackTimeoutMillis();
    }

    TransportStatusCallback(int operationTimeout) {
        this.mOperationStatus = 0;
        this.mHasCompletedOperation = false;
        this.mOperationTimeout = operationTimeout;
    }

    public synchronized void onOperationCompleteWithStatus(int status) throws android.os.RemoteException {
        this.mHasCompletedOperation = true;
        this.mOperationStatus = status;
        notifyAll();
    }

    public synchronized void onOperationComplete() throws android.os.RemoteException {
        onOperationCompleteWithStatus(0);
    }

    synchronized int getOperationStatus() {
        if (this.mHasCompletedOperation) {
            return this.mOperationStatus;
        }
        long timeoutLeft = this.mOperationTimeout;
        while (!this.mHasCompletedOperation && timeoutLeft > 0) {
            try {
                long waitStartTime = java.lang.System.currentTimeMillis();
                wait(timeoutLeft);
                if (this.mHasCompletedOperation) {
                    return this.mOperationStatus;
                }
                timeoutLeft -= java.lang.System.currentTimeMillis() - waitStartTime;
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.w(TAG, "Couldn't get operation status from transport: ", e);
            }
        }
        android.util.Slog.w(TAG, "Couldn't get operation status from transport");
        return -1000;
    }

    synchronized void reset() {
        this.mHasCompletedOperation = false;
        this.mOperationStatus = 0;
    }
}
