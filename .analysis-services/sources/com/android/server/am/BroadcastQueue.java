package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public abstract class BroadcastQueue {
    public static final java.lang.String TAG = "BroadcastQueue";
    public static final java.lang.String TAG_DUMP = "broadcast_queue_dump";
    final android.os.Handler mHandler;
    final com.android.server.am.BroadcastHistory mHistory;
    final java.lang.String mQueueName;
    final com.android.server.am.ActivityManagerService mService;
    final com.android.server.am.BroadcastSkipPolicy mSkipPolicy;

    public abstract void backgroundServicesFinishedLocked(int i);

    public abstract boolean cleanupDisabledPackageReceiversLocked(java.lang.String str, java.util.Set<java.lang.String> set, int i);

    public abstract java.lang.String describeStateLocked();

    public abstract void dumpDebug(android.util.proto.ProtoOutputStream protoOutputStream, long j);

    public abstract boolean dumpLocked(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String[] strArr, int i, boolean z, boolean z2, boolean z3, java.lang.String str, boolean z4);

    public abstract void enqueueBroadcastLocked(com.android.server.am.BroadcastRecord broadcastRecord);

    public abstract boolean finishReceiverLocked(com.android.server.am.ProcessRecord processRecord, int i, java.lang.String str, android.os.Bundle bundle, boolean z, boolean z2);

    public abstract int getPreferredSchedulingGroupLocked(com.android.server.am.ProcessRecord processRecord);

    public abstract boolean isBeyondBarrierLocked(long j);

    public abstract boolean isDispatchedLocked(android.content.Intent intent);

    public abstract boolean isIdleLocked();

    public abstract boolean onApplicationAttachedLocked(com.android.server.am.ProcessRecord processRecord) throws com.android.server.am.BroadcastDeliveryFailedException;

    public abstract void onApplicationCleanupLocked(com.android.server.am.ProcessRecord processRecord);

    public abstract void onApplicationProblemLocked(com.android.server.am.ProcessRecord processRecord);

    public abstract void onApplicationTimeoutLocked(com.android.server.am.ProcessRecord processRecord);

    public abstract void onProcessFreezableChangedLocked(com.android.server.am.ProcessRecord processRecord);

    public abstract void start(android.content.ContentResolver contentResolver);

    public abstract void waitForBarrier(java.io.PrintWriter printWriter);

    public abstract void waitForDispatched(android.content.Intent intent, java.io.PrintWriter printWriter);

    public abstract void waitForIdle(java.io.PrintWriter printWriter);

    BroadcastQueue(com.android.server.am.ActivityManagerService service, android.os.Handler handler, java.lang.String name, com.android.server.am.BroadcastSkipPolicy skipPolicy, com.android.server.am.BroadcastHistory history) {
        this.mService = (com.android.server.am.ActivityManagerService) java.util.Objects.requireNonNull(service);
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
        this.mQueueName = (java.lang.String) java.util.Objects.requireNonNull(name);
        this.mSkipPolicy = (com.android.server.am.BroadcastSkipPolicy) java.util.Objects.requireNonNull(skipPolicy);
        this.mHistory = (com.android.server.am.BroadcastHistory) java.util.Objects.requireNonNull(history);
    }

    static void logw(java.lang.String msg) {
        android.util.Slog.w(TAG, msg);
    }

    static void logv(java.lang.String msg) {
        android.util.Slog.v(TAG, msg);
    }

    static void checkState(boolean expression, java.lang.String msg) {
        if (!expression) {
            throw new java.lang.IllegalStateException(msg);
        }
    }

    static int traceBegin(java.lang.String methodName) {
        int cookie = methodName.hashCode();
        android.os.Trace.asyncTraceForTrackBegin(64L, TAG, methodName, cookie);
        return cookie;
    }

    static void traceEnd(int cookie) {
        android.os.Trace.asyncTraceForTrackEnd(64L, TAG, cookie);
    }

    public java.lang.String toString() {
        return this.mQueueName;
    }

    public void forceDelayBroadcastDelivery(java.lang.String targetPackage, long delayedDurationMs) {
    }

    public void dumpToDropBoxLocked(final java.lang.String msg) {
        ((com.android.server.DropBoxManagerInternal) com.android.server.LocalServices.getService(com.android.server.DropBoxManagerInternal.class)).addEntry(TAG_DUMP, new com.android.server.DropBoxManagerInternal.EntrySource() { // from class: com.android.server.am.BroadcastQueue$$ExternalSyntheticLambda0
            @Override // com.android.server.DropBoxManagerInternal.EntrySource
            public final void writeTo(java.io.FileDescriptor fileDescriptor) throws java.io.IOException {
                this.f$0.lambda$dumpToDropBoxLocked$0(msg, fileDescriptor);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpToDropBoxLocked$0(java.lang.String msg, java.io.FileDescriptor fd) throws java.io.IOException {
        java.io.FileOutputStream out = new java.io.FileOutputStream(fd);
        try {
            java.io.PrintWriter pw = new java.io.PrintWriter(out);
            try {
                pw.print("Message: ");
                pw.println(msg);
                dumpLocked(fd, pw, null, 0, false, false, false, null, false);
                pw.flush();
                pw.close();
                out.close();
            } finally {
            }
        } catch (java.lang.Throwable th) {
            try {
                out.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
