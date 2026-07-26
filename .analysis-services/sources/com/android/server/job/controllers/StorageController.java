package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class StorageController extends com.android.server.job.controllers.StateController {
    private static final boolean DEBUG;
    private static final java.lang.String TAG = "JobScheduler.Storage";
    private final com.android.server.job.controllers.StorageController.StorageTracker mStorageTracker;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mTrackedTasks;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    public com.android.server.job.controllers.StorageController.StorageTracker getTracker() {
        return this.mStorageTracker;
    }

    public StorageController(com.android.server.job.JobSchedulerService service) {
        super(service);
        this.mTrackedTasks = new android.util.ArraySet<>();
        this.mStorageTracker = new com.android.server.job.controllers.StorageController.StorageTracker();
    }

    @Override // com.android.server.job.controllers.StateController
    public void startTrackingLocked() {
        this.mStorageTracker.startTracking();
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus lastJob) {
        if (taskStatus.hasStorageNotLowConstraint()) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            this.mTrackedTasks.add(taskStatus);
            taskStatus.setTrackingController(16);
            taskStatus.setStorageNotLowConstraintSatisfied(nowElapsed, this.mStorageTracker.isStorageNotLow());
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        if (taskStatus.clearTrackingController(16)) {
            this.mTrackedTasks.remove(taskStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeReportNewStorageState() {
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        boolean storageNotLow = this.mStorageTracker.isStorageNotLow();
        boolean reportChange = false;
        synchronized (this.mLock) {
            for (int i = this.mTrackedTasks.size() - 1; i >= 0; i--) {
                com.android.server.job.controllers.JobStatus ts = this.mTrackedTasks.valueAt(i);
                reportChange |= ts.setStorageNotLowConstraintSatisfied(nowElapsed, storageNotLow);
            }
        }
        if (storageNotLow) {
            this.mStateChangedListener.onRunJobNow(null);
        } else if (reportChange) {
            this.mStateChangedListener.onControllerStateChanged(this.mTrackedTasks);
        }
    }

    public final class StorageTracker extends android.content.BroadcastReceiver {
        private int mLastStorageSeq = -1;
        private boolean mStorageLow;

        public StorageTracker() {
        }

        public void startTracking() {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
            filter.addAction("android.intent.action.DEVICE_STORAGE_OK");
            com.android.server.job.controllers.StorageController.this.mContext.registerReceiver(this, filter);
        }

        public boolean isStorageNotLow() {
            return !this.mStorageLow;
        }

        public int getSeq() {
            return this.mLastStorageSeq;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            onReceiveInternal(intent);
        }

        public void onReceiveInternal(android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            this.mLastStorageSeq = intent.getIntExtra(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mLastStorageSeq);
            if ("android.intent.action.DEVICE_STORAGE_LOW".equals(action)) {
                if (com.android.server.job.controllers.StorageController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.StorageController.TAG, "Available storage too low to do work. @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                }
                this.mStorageLow = true;
                com.android.server.job.controllers.StorageController.this.maybeReportNewStorageState();
                return;
            }
            if ("android.intent.action.DEVICE_STORAGE_OK".equals(action)) {
                if (com.android.server.job.controllers.StorageController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.StorageController.TAG, "Available storage high enough to do work. @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                }
                this.mStorageLow = false;
                com.android.server.job.controllers.StorageController.this.maybeReportNewStorageState();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        pw.println("Not low: " + this.mStorageTracker.isStorageNotLow());
        pw.println("Sequence: " + this.mStorageTracker.getSeq());
        pw.println();
        for (int i = 0; i < this.mTrackedTasks.size(); i++) {
            com.android.server.job.controllers.JobStatus js = this.mTrackedTasks.valueAt(i);
            if (predicate.test(js)) {
                pw.print("#");
                js.printUniqueId(pw);
                pw.print(" from ");
                android.os.UserHandle.formatUid(pw, js.getSourceUid());
                pw.println();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long token = proto.start(fieldId);
        long mToken = proto.start(1146756268039L);
        proto.write(1133871366145L, this.mStorageTracker.isStorageNotLow());
        proto.write(1120986464258L, this.mStorageTracker.getSeq());
        for (int i = 0; i < this.mTrackedTasks.size(); i++) {
            com.android.server.job.controllers.JobStatus js = this.mTrackedTasks.valueAt(i);
            if (predicate.test(js)) {
                long jsToken = proto.start(2246267895811L);
                js.writeToShortProto(proto, 1146756268033L);
                proto.write(1120986464258L, js.getSourceUid());
                proto.end(jsToken);
            }
        }
        proto.end(mToken);
        proto.end(token);
    }
}
