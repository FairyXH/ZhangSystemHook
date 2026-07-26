package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public final class JobStore {
    private static final int ALL_UIDS = -1;
    static final int INVALID_UID = -2;
    private static final int JOBS_FILE_VERSION = 1;
    static final java.lang.String JOB_FILE_SPLIT_PREFIX = "jobs_";
    private static final long JOB_PERSIST_DELAY = 2000;
    private static final long SCHEDULED_JOB_HIGH_WATER_MARK_PERIOD_MS = 1800000;
    private static final java.lang.String TAG = "JobStore";
    private static final java.lang.String XML_TAG_DEBUG_INFO = "debug-info";
    private static final java.lang.String XML_TAG_DEBUG_TAG = "debug-tag";
    private static final java.lang.String XML_TAG_EXTRAS = "extras";
    private static final java.lang.String XML_TAG_JOB = "job";
    private static final java.lang.String XML_TAG_JOB_INFO = "job-info";
    private static final java.lang.String XML_TAG_JOB_WORK_ITEM = "job-work-item";
    private static final java.lang.String XML_TAG_ONEOFF = "one-off";
    private static final java.lang.String XML_TAG_PARAMS_CONSTRAINTS = "constraints";
    private static final java.lang.String XML_TAG_PERIODIC = "periodic";
    private static com.android.server.job.JobStore sSingleton;
    final android.content.Context mContext;
    private final android.util.SystemConfigFileCommitEventLogger mEventLogger;
    private final java.io.File mJobFileDirectory;
    final com.android.server.job.JobStore.JobSet mJobSet;
    private final android.util.AtomicFile mJobsFile;
    final java.lang.Object mLock;
    private boolean mRtcGood;
    private boolean mSplitFileMigrationNeeded;
    private boolean mWriteInProgress;
    private boolean mWriteScheduled;
    private final long mXmlTimestamp;
    private static final boolean DEBUG = com.android.server.job.JobSchedulerService.DEBUG;
    private static final java.util.regex.Pattern SPLIT_FILE_PATTERN = java.util.regex.Pattern.compile("^jobs_\\d+.xml$");
    private static final java.lang.Object sSingletonLock = new java.lang.Object();
    private static final com.android.modules.expresslog.Histogram sScheduledJob30MinHighWaterMarkLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_scheduled_job_30_min_high_water_mark", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(15, 1, 99.0f, 1.5f));
    private final android.util.SparseBooleanArray mPendingJobWriteUids = new android.util.SparseBooleanArray();
    private final android.os.Handler mIoHandler = com.android.server.IoThread.getHandler();
    private boolean mUseSplitFiles = true;
    private com.android.server.job.JobSchedulerInternal.JobStorePersistStats mPersistInfo = new com.android.server.job.JobSchedulerInternal.JobStorePersistStats();
    private int mCurrentJobSetSize = 0;
    private int mScheduledJob30MinHighWaterMark = 0;
    private final java.lang.Runnable mScheduledJobHighWaterMarkLoggingRunnable = new java.lang.Runnable() { // from class: com.android.server.job.JobStore.1
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.AppSchedulingModuleThread.getHandler().removeCallbacks(this);
            synchronized (com.android.server.job.JobStore.this.mLock) {
                com.android.server.job.JobStore.sScheduledJob30MinHighWaterMarkLogger.logSample(com.android.server.job.JobStore.this.mScheduledJob30MinHighWaterMark);
                com.android.server.job.JobStore.this.mScheduledJob30MinHighWaterMark = com.android.server.job.JobStore.this.mJobSet.size();
            }
            com.android.server.AppSchedulingModuleThread.getHandler().postDelayed(this, 1800000L);
        }
    };
    private final java.lang.Runnable mWriteRunnable = new java.lang.Runnable() { // from class: com.android.server.job.JobStore.2
        private final android.util.SparseArray<android.util.AtomicFile> mJobFiles = new android.util.SparseArray<>();
        private final com.android.server.job.JobStore.AnonymousClass2.CopyConsumer mPersistedJobCopier = new com.android.server.job.JobStore.AnonymousClass2.CopyConsumer();

        /* JADX INFO: renamed from: com.android.server.job.JobStore$2$CopyConsumer */
        class CopyConsumer implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
            private boolean mCopyAllJobs;
            private final android.util.SparseArray<java.util.List<com.android.server.job.controllers.JobStatus>> mJobStoreCopy = new android.util.SparseArray<>();

            CopyConsumer() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void prepare() {
                this.mCopyAllJobs = !com.android.server.job.JobStore.this.mUseSplitFiles || com.android.server.job.JobStore.this.mPendingJobWriteUids.get(-1);
                if (com.android.server.job.JobStore.this.mUseSplitFiles) {
                    if (com.android.server.job.JobStore.this.mPendingJobWriteUids.get(-1)) {
                        try {
                            java.io.File[] files = com.android.server.job.JobStore.this.mJobFileDirectory.listFiles();
                            if (files == null) {
                                android.util.Slog.wtfStack(com.android.server.job.JobStore.TAG, "Couldn't get job file list");
                                return;
                            }
                            for (java.io.File file : files) {
                                int uid = com.android.server.job.JobStore.extractUidFromJobFileName(file);
                                if (uid != -2) {
                                    this.mJobStoreCopy.put(uid, new java.util.ArrayList());
                                }
                            }
                            return;
                        } catch (java.lang.SecurityException e) {
                            android.util.Slog.wtf(com.android.server.job.JobStore.TAG, "Not allowed to read job file directory", e);
                            return;
                        }
                    }
                    for (int i = 0; i < com.android.server.job.JobStore.this.mPendingJobWriteUids.size(); i++) {
                        this.mJobStoreCopy.put(com.android.server.job.JobStore.this.mPendingJobWriteUids.keyAt(i), new java.util.ArrayList());
                    }
                    return;
                }
                this.mJobStoreCopy.put(-1, new java.util.ArrayList());
            }

            @Override // java.util.function.Consumer
            public void accept(com.android.server.job.controllers.JobStatus jobStatus) {
                int uid = com.android.server.job.JobStore.this.mUseSplitFiles ? jobStatus.getUid() : -1;
                if (jobStatus.isPersisted()) {
                    if (this.mCopyAllJobs || com.android.server.job.JobStore.this.mPendingJobWriteUids.get(uid)) {
                        java.util.List<com.android.server.job.controllers.JobStatus> uidJobList = this.mJobStoreCopy.get(uid);
                        if (uidJobList == null) {
                            uidJobList = new java.util.ArrayList();
                            this.mJobStoreCopy.put(uid, uidJobList);
                        }
                        uidJobList.add(new com.android.server.job.controllers.JobStatus(jobStatus));
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void reset() {
                this.mJobStoreCopy.clear();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean useSplitFiles;
            android.util.AtomicFile file;
            long startElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            synchronized (com.android.server.job.JobStore.this.mWriteScheduleLock) {
                com.android.server.job.JobStore.this.mWriteScheduled = false;
                if (com.android.server.job.JobStore.this.mWriteInProgress) {
                    com.android.server.job.JobStore.this.maybeWriteStatusToDiskAsync();
                    return;
                }
                com.android.server.job.JobStore.this.mWriteInProgress = true;
                synchronized (com.android.server.job.JobStore.this.mLock) {
                    useSplitFiles = com.android.server.job.JobStore.this.mUseSplitFiles;
                    this.mPersistedJobCopier.prepare();
                    com.android.server.job.JobStore.this.mJobSet.forEachJob((java.util.function.Predicate<com.android.server.job.controllers.JobStatus>) null, this.mPersistedJobCopier);
                    com.android.server.job.JobStore.this.mPendingJobWriteUids.clear();
                }
                com.android.server.job.JobStore.this.mPersistInfo.countAllJobsSaved = 0;
                com.android.server.job.JobStore.this.mPersistInfo.countSystemServerJobsSaved = 0;
                com.android.server.job.JobStore.this.mPersistInfo.countSystemSyncManagerJobsSaved = 0;
                for (int i = this.mPersistedJobCopier.mJobStoreCopy.size() - 1; i >= 0; i--) {
                    if (useSplitFiles) {
                        int uid = this.mPersistedJobCopier.mJobStoreCopy.keyAt(i);
                        file = this.mJobFiles.get(uid);
                        if (file == null) {
                            file = com.android.server.job.JobStore.this.createJobFile(com.android.server.job.JobStore.JOB_FILE_SPLIT_PREFIX + uid);
                            this.mJobFiles.put(uid, file);
                        }
                    } else {
                        file = com.android.server.job.JobStore.this.mJobsFile;
                    }
                    if (com.android.server.job.JobStore.DEBUG) {
                        android.util.Slog.d(com.android.server.job.JobStore.TAG, "Writing for " + this.mPersistedJobCopier.mJobStoreCopy.keyAt(i) + " to " + file.getBaseFile().getName() + ": " + ((java.util.List) this.mPersistedJobCopier.mJobStoreCopy.valueAt(i)).size() + " jobs");
                    }
                    writeJobsMapImpl(file, (java.util.List) this.mPersistedJobCopier.mJobStoreCopy.valueAt(i));
                }
                if (com.android.server.job.JobStore.DEBUG) {
                    android.util.Slog.v(com.android.server.job.JobStore.TAG, "Finished writing, took " + (com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() - startElapsed) + "ms");
                }
                this.mPersistedJobCopier.reset();
                if (!useSplitFiles) {
                    this.mJobFiles.clear();
                }
                com.android.server.job.JobStore.this.mJobFileDirectory.setLastModified(com.android.server.job.JobSchedulerService.sSystemClock.millis());
                synchronized (com.android.server.job.JobStore.this.mWriteScheduleLock) {
                    if (com.android.server.job.JobStore.this.mSplitFileMigrationNeeded) {
                        java.io.File[] files = com.android.server.job.JobStore.this.mJobFileDirectory.listFiles();
                        for (java.io.File file2 : files) {
                            if (useSplitFiles) {
                                if (!file2.getName().startsWith(com.android.server.job.JobStore.JOB_FILE_SPLIT_PREFIX)) {
                                    file2.delete();
                                }
                            } else if (file2.getName().startsWith(com.android.server.job.JobStore.JOB_FILE_SPLIT_PREFIX)) {
                                file2.delete();
                            }
                        }
                    }
                    com.android.server.job.JobStore.this.mWriteInProgress = false;
                    com.android.server.job.JobStore.this.mWriteScheduleLock.notifyAll();
                }
            }
        }

        private void writeJobsMapImpl(android.util.AtomicFile file, java.util.List<com.android.server.job.controllers.JobStatus> jobList) {
            int numJobs = 0;
            int numSystemJobs = 0;
            int numSyncJobs = 0;
            com.android.server.job.JobStore.this.mEventLogger.setStartTime(android.os.SystemClock.uptimeMillis());
            try {
                try {
                    java.io.FileOutputStream fos = file.startWrite();
                    try {
                        com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
                        out.startDocument((java.lang.String) null, true);
                        out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                        out.startTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_JOB_INFO);
                        out.attribute((java.lang.String) null, "version", java.lang.Integer.toString(1));
                        for (int i = 0; i < jobList.size(); i++) {
                            com.android.server.job.controllers.JobStatus jobStatus = jobList.get(i);
                            if (com.android.server.job.JobStore.DEBUG) {
                                android.util.Slog.d(com.android.server.job.JobStore.TAG, "Saving job " + jobStatus.getJobId());
                            }
                            out.startTag((java.lang.String) null, "job");
                            addAttributesToJobTag(out, jobStatus);
                            writeConstraintsToXml(out, jobStatus);
                            writeExecutionCriteriaToXml(out, jobStatus);
                            writeBundleToXml(jobStatus.getJob().getExtras(), out);
                            writeJobWorkItemsToXml(out, jobStatus);
                            writeDebugInfoToXml(out, jobStatus);
                            out.endTag((java.lang.String) null, "job");
                            numJobs++;
                            if (jobStatus.getUid() == 1000) {
                                numSystemJobs++;
                                if (com.android.server.job.JobStore.isSyncJob(jobStatus)) {
                                    numSyncJobs++;
                                }
                            }
                        }
                        out.endTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_JOB_INFO);
                        out.endDocument();
                        file.finishWrite(fos);
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (java.lang.Throwable th) {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } finally {
                    com.android.server.job.JobStore.this.mPersistInfo.countAllJobsSaved += 0;
                    com.android.server.job.JobStore.this.mPersistInfo.countSystemServerJobsSaved += 0;
                    com.android.server.job.JobStore.this.mPersistInfo.countSystemSyncManagerJobsSaved += 0;
                }
            } catch (java.io.IOException e) {
                if (com.android.server.job.JobStore.DEBUG) {
                    android.util.Slog.v(com.android.server.job.JobStore.TAG, "Error writing out job data.", e);
                }
            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                if (com.android.server.job.JobStore.DEBUG) {
                    android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error persisting bundle.", e2);
                }
            }
        }

        private void addAttributesToJobTag(com.android.modules.utils.TypedXmlSerializer out, com.android.server.job.controllers.JobStatus jobStatus) throws java.io.IOException {
            out.attribute((java.lang.String) null, "jobid", java.lang.Integer.toString(jobStatus.getJobId()));
            out.attribute((java.lang.String) null, "package", jobStatus.getServiceComponent().getPackageName());
            out.attribute((java.lang.String) null, "class", jobStatus.getServiceComponent().getClassName());
            if (jobStatus.getSourcePackageName() != null) {
                out.attribute((java.lang.String) null, "sourcePackageName", jobStatus.getSourcePackageName());
            }
            if (jobStatus.getNamespace() != null) {
                out.attribute((java.lang.String) null, "namespace", jobStatus.getNamespace());
            }
            if (jobStatus.getSourceTag() != null) {
                out.attribute((java.lang.String) null, "sourceTag", jobStatus.getSourceTag());
            }
            out.attribute((java.lang.String) null, "sourceUserId", java.lang.String.valueOf(jobStatus.getSourceUserId()));
            out.attribute((java.lang.String) null, "uid", java.lang.Integer.toString(jobStatus.getUid()));
            out.attribute((java.lang.String) null, "bias", java.lang.String.valueOf(jobStatus.getBias()));
            out.attribute((java.lang.String) null, "priority", java.lang.String.valueOf(jobStatus.getJob().getPriority()));
            out.attribute((java.lang.String) null, "flags", java.lang.String.valueOf(jobStatus.getFlags()));
            if (jobStatus.getInternalFlags() != 0) {
                out.attribute((java.lang.String) null, "internalFlags", java.lang.String.valueOf(jobStatus.getInternalFlags()));
            }
            out.attribute((java.lang.String) null, "lastSuccessfulRunTime", java.lang.String.valueOf(jobStatus.getLastSuccessfulRunTime()));
            out.attribute((java.lang.String) null, "lastFailedRunTime", java.lang.String.valueOf(jobStatus.getLastFailedRunTime()));
            out.attributeLong((java.lang.String) null, "cumulativeExecutionTime", jobStatus.getCumulativeExecutionTimeMs());
        }

        private void writeBundleToXml(android.os.PersistableBundle extras, org.xmlpull.v1.XmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            out.startTag(null, com.android.server.job.JobStore.XML_TAG_EXTRAS);
            android.os.PersistableBundle extrasCopy = deepCopyBundle(extras, 10);
            extrasCopy.saveToXml(out);
            out.endTag(null, com.android.server.job.JobStore.XML_TAG_EXTRAS);
        }

        private android.os.PersistableBundle deepCopyBundle(android.os.PersistableBundle bundle, int maxDepth) {
            if (maxDepth <= 0) {
                return null;
            }
            android.os.PersistableBundle copy = (android.os.PersistableBundle) bundle.clone();
            java.util.Set<java.lang.String> keySet = bundle.keySet();
            for (java.lang.String key : keySet) {
                java.lang.Object o = copy.get(key);
                if (o instanceof android.os.PersistableBundle) {
                    android.os.PersistableBundle bCopy = deepCopyBundle((android.os.PersistableBundle) o, maxDepth - 1);
                    copy.putPersistableBundle(key, bCopy);
                }
            }
            return copy;
        }

        private void writeConstraintsToXml(com.android.modules.utils.TypedXmlSerializer out, com.android.server.job.controllers.JobStatus jobStatus) throws java.io.IOException {
            out.startTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_PARAMS_CONSTRAINTS);
            android.app.job.JobInfo job = jobStatus.getJob();
            if (jobStatus.hasConnectivityConstraint()) {
                android.net.NetworkRequest network = jobStatus.getJob().getRequiredNetwork();
                out.attribute((java.lang.String) null, "net-capabilities-csv", com.android.server.job.JobStore.intArrayToString(network.getCapabilities()));
                out.attribute((java.lang.String) null, "net-forbidden-capabilities-csv", com.android.server.job.JobStore.intArrayToString(network.getForbiddenCapabilities()));
                out.attribute((java.lang.String) null, "net-transport-types-csv", com.android.server.job.JobStore.intArrayToString(network.getTransportTypes()));
                if (job.getEstimatedNetworkDownloadBytes() != -1) {
                    out.attributeLong((java.lang.String) null, "estimated-download-bytes", job.getEstimatedNetworkDownloadBytes());
                }
                if (job.getEstimatedNetworkUploadBytes() != -1) {
                    out.attributeLong((java.lang.String) null, "estimated-upload-bytes", job.getEstimatedNetworkUploadBytes());
                }
                if (job.getMinimumNetworkChunkBytes() != -1) {
                    out.attributeLong((java.lang.String) null, "minimum-network-chunk-bytes", job.getMinimumNetworkChunkBytes());
                }
            }
            if (job.isRequireDeviceIdle()) {
                out.attribute((java.lang.String) null, "idle", java.lang.Boolean.toString(true));
            }
            if (job.isRequireCharging()) {
                out.attribute((java.lang.String) null, "charging", java.lang.Boolean.toString(true));
            }
            if (job.isRequireBatteryNotLow()) {
                out.attribute((java.lang.String) null, "battery-not-low", java.lang.Boolean.toString(true));
            }
            if (job.isRequireStorageNotLow()) {
                out.attribute((java.lang.String) null, "storage-not-low", java.lang.Boolean.toString(true));
            }
            if (job.mJobInfoExt != null) {
                android.app.job.IJobInfoExt jobInfoExt = job.mJobInfoExt;
                if (jobInfoExt.getBooleanConstraint("requireBattIdle", false)) {
                    out.attribute((java.lang.String) null, "requireBattIdle", java.lang.Boolean.toString(true));
                }
                if (jobInfoExt.getBooleanConstraint("requireProtectFore", false)) {
                    out.attribute((java.lang.String) null, "requireProtectFore", java.lang.Integer.toString(jobInfoExt.getIntConstraint("protectForeType", 0)));
                }
                if (jobInfoExt.getBooleanConstraint("hasCpuConstraint", false)) {
                    out.attribute((java.lang.String) null, "hasCpuConstraint", java.lang.Boolean.toString(true));
                }
                if (jobInfoExt.getBooleanConstraint("hasTemperatureConstraint", false)) {
                    out.attribute((java.lang.String) null, "hasTemperatureConstraint", java.lang.Boolean.toString(true));
                }
                if (jobInfoExt.getBooleanConstraint("hasProtectSceneConstraint", false)) {
                    out.attribute((java.lang.String) null, "hasProtectSceneConstraint", java.lang.Integer.toString(jobInfoExt.getIntConstraint("protectScene", 0)));
                }
                if (jobInfoExt.getBooleanConstraint("isOplusJob", false)) {
                    out.attribute((java.lang.String) null, "isOplusJob", java.lang.Boolean.toString(true));
                }
                if (jobInfoExt.getBooleanConstraint("isFastIdle", false)) {
                    out.attribute((java.lang.String) null, "isFastIdle", java.lang.Boolean.toString(true));
                }
            }
            out.endTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_PARAMS_CONSTRAINTS);
        }

        private void writeExecutionCriteriaToXml(org.xmlpull.v1.XmlSerializer out, com.android.server.job.controllers.JobStatus jobStatus) throws java.io.IOException {
            long delayWallclock;
            long deadlineWallclock;
            android.app.job.JobInfo job = jobStatus.getJob();
            if (jobStatus.getJob().isPeriodic()) {
                out.startTag(null, com.android.server.job.JobStore.XML_TAG_PERIODIC);
                out.attribute(null, "period", java.lang.Long.toString(job.getIntervalMillis()));
                out.attribute(null, "flex", java.lang.Long.toString(job.getFlexMillis()));
            } else {
                out.startTag(null, com.android.server.job.JobStore.XML_TAG_ONEOFF);
            }
            android.util.Pair<java.lang.Long, java.lang.Long> utcJobTimes = jobStatus.getPersistedUtcTimes();
            if (com.android.server.job.JobStore.DEBUG && utcJobTimes != null) {
                android.util.Slog.i(com.android.server.job.JobStore.TAG, "storing original UTC timestamps for " + jobStatus);
            }
            long nowRTC = com.android.server.job.JobSchedulerService.sSystemClock.millis();
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (jobStatus.hasDeadlineConstraint()) {
                if (utcJobTimes == null) {
                    deadlineWallclock = (jobStatus.getLatestRunTimeElapsed() - nowElapsed) + nowRTC;
                } else {
                    deadlineWallclock = ((java.lang.Long) utcJobTimes.second).longValue();
                }
                out.attribute(null, "deadline", java.lang.Long.toString(deadlineWallclock));
            }
            if (jobStatus.hasTimingDelayConstraint()) {
                if (utcJobTimes == null) {
                    delayWallclock = (jobStatus.getEarliestRunTime() - nowElapsed) + nowRTC;
                } else {
                    delayWallclock = ((java.lang.Long) utcJobTimes.first).longValue();
                }
                out.attribute(null, "delay", java.lang.Long.toString(delayWallclock));
            }
            if (jobStatus.getJob().getInitialBackoffMillis() != 30000 || jobStatus.getJob().getBackoffPolicy() != 1) {
                out.attribute(null, "backoff-policy", java.lang.Integer.toString(job.getBackoffPolicy()));
                out.attribute(null, "initial-backoff", java.lang.Long.toString(job.getInitialBackoffMillis()));
            }
            if (job.isPeriodic()) {
                out.endTag(null, com.android.server.job.JobStore.XML_TAG_PERIODIC);
            } else {
                out.endTag(null, com.android.server.job.JobStore.XML_TAG_ONEOFF);
            }
        }

        private void writeDebugInfoToXml(com.android.modules.utils.TypedXmlSerializer out, com.android.server.job.controllers.JobStatus jobStatus) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            android.util.ArraySet<java.lang.String> debugTags = jobStatus.getJob().getDebugTagsArraySet();
            int numTags = debugTags.size();
            java.lang.String traceTag = jobStatus.getJob().getTraceTag();
            if (traceTag == null && numTags == 0) {
                return;
            }
            out.startTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_DEBUG_INFO);
            if (traceTag != null) {
                out.attribute((java.lang.String) null, "trace-tag", traceTag);
            }
            for (int i = 0; i < numTags; i++) {
                out.startTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_DEBUG_TAG);
                out.attribute((java.lang.String) null, "tag", debugTags.valueAt(i));
                out.endTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_DEBUG_TAG);
            }
            out.endTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_DEBUG_INFO);
        }

        private void writeJobWorkItemsToXml(com.android.modules.utils.TypedXmlSerializer out, com.android.server.job.controllers.JobStatus jobStatus) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            writeJobWorkItemListToXml(out, jobStatus.executingWork);
            writeJobWorkItemListToXml(out, jobStatus.pendingWork);
        }

        private void writeJobWorkItemListToXml(com.android.modules.utils.TypedXmlSerializer out, java.util.List<android.app.job.JobWorkItem> jobWorkItems) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            if (jobWorkItems == null) {
                return;
            }
            int size = jobWorkItems.size();
            for (int i = 0; i < size; i++) {
                android.app.job.JobWorkItem item = jobWorkItems.get(i);
                if (item.getGrants() == null) {
                    if (item.getIntent() != null) {
                        android.util.Slog.wtf(com.android.server.job.JobStore.TAG, "Encountered JobWorkItem with Intent in persisting list");
                    } else {
                        out.startTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_JOB_WORK_ITEM);
                        out.attributeInt((java.lang.String) null, "delivery-count", item.getDeliveryCount());
                        if (item.getEstimatedNetworkDownloadBytes() != -1) {
                            out.attributeLong((java.lang.String) null, "estimated-download-bytes", item.getEstimatedNetworkDownloadBytes());
                        }
                        if (item.getEstimatedNetworkUploadBytes() != -1) {
                            out.attributeLong((java.lang.String) null, "estimated-upload-bytes", item.getEstimatedNetworkUploadBytes());
                        }
                        if (item.getMinimumNetworkChunkBytes() != -1) {
                            out.attributeLong((java.lang.String) null, "minimum-network-chunk-bytes", item.getMinimumNetworkChunkBytes());
                        }
                        writeBundleToXml(item.getExtras(), out);
                        out.endTag((java.lang.String) null, com.android.server.job.JobStore.XML_TAG_JOB_WORK_ITEM);
                    }
                }
            }
        }
    };
    final java.lang.Object mWriteScheduleLock = new java.lang.Object();

    static com.android.server.job.JobStore get(com.android.server.job.JobSchedulerService jobManagerService) {
        com.android.server.job.JobStore jobStore;
        synchronized (sSingletonLock) {
            if (sSingleton == null) {
                sSingleton = new com.android.server.job.JobStore(jobManagerService.getContext(), jobManagerService.getLock(), android.os.Environment.getDataDirectory());
            }
            jobStore = sSingleton;
        }
        return jobStore;
    }

    public static com.android.server.job.JobStore initAndGetForTesting(android.content.Context context, java.io.File dataDir) throws java.lang.Throwable {
        com.android.server.job.JobStore jobStoreUnderTest = new com.android.server.job.JobStore(context, new java.lang.Object(), dataDir);
        jobStoreUnderTest.init();
        jobStoreUnderTest.clearForTesting();
        return jobStoreUnderTest;
    }

    private JobStore(android.content.Context context, java.lang.Object lock, java.io.File dataDir) {
        this.mLock = lock;
        this.mContext = context;
        java.io.File systemDir = new java.io.File(dataDir, "system");
        this.mJobFileDirectory = new java.io.File(systemDir, "job");
        this.mJobFileDirectory.mkdirs();
        this.mEventLogger = new android.util.SystemConfigFileCommitEventLogger("jobs");
        if (!android.os.SystemProperties.getBoolean("persist.sys.brand.oplus", false)) {
            java.io.File file = new java.io.File(this.mJobFileDirectory, "jobs.xml");
            if (file.exists()) {
                boolean sucess = file.delete();
                android.util.Slog.d(TAG, "delete jobs file for oplus brand : " + sucess);
            } else {
                android.util.Slog.d(TAG, "try to delete jobs file for oplus brand but not exist");
            }
            android.os.SystemProperties.set("persist.sys.brand.oplus", "true");
        }
        this.mJobsFile = createJobFile(new java.io.File(this.mJobFileDirectory, "jobs.xml"));
        this.mJobSet = new com.android.server.job.JobStore.JobSet();
        this.mXmlTimestamp = this.mJobsFile.exists() ? this.mJobsFile.getLastModifiedTime() : this.mJobFileDirectory.lastModified();
        this.mRtcGood = com.android.server.job.JobSchedulerService.sSystemClock.millis() > this.mXmlTimestamp;
        com.android.server.AppSchedulingModuleThread.getHandler().postDelayed(this.mScheduledJobHighWaterMarkLoggingRunnable, 1800000L);
    }

    private void init() throws java.lang.Throwable {
        readJobMapFromDisk(this.mJobSet, this.mRtcGood);
    }

    void initAsync(java.util.concurrent.CountDownLatch completionLatch) {
        this.mIoHandler.post(new com.android.server.job.JobStore.ReadJobMapFromDiskRunnable(this.mJobSet, this.mRtcGood, completionLatch));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.AtomicFile createJobFile(java.lang.String baseName) {
        return createJobFile(new java.io.File(this.mJobFileDirectory, baseName + ".xml"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.AtomicFile createJobFile(java.io.File file) {
        return new android.util.AtomicFile(file, this.mEventLogger);
    }

    public boolean jobTimesInflatedValid() {
        return this.mRtcGood;
    }

    public boolean clockNowValidToInflate(long now) {
        return now >= this.mXmlTimestamp;
    }

    void runWorkAsync(java.lang.Runnable r) {
        this.mIoHandler.post(r);
    }

    public void getRtcCorrectedJobsLocked(final java.util.ArrayList<com.android.server.job.controllers.JobStatus> toAdd, final java.util.ArrayList<com.android.server.job.controllers.JobStatus> toRemove) {
        final long elapsedNow = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        forEachJob(new java.util.function.Consumer() { // from class: com.android.server.job.JobStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.job.JobStore.lambda$getRtcCorrectedJobsLocked$0(elapsedNow, toAdd, toRemove, (com.android.server.job.controllers.JobStatus) obj);
            }
        });
    }

    static /* synthetic */ void lambda$getRtcCorrectedJobsLocked$0(long elapsedNow, java.util.ArrayList toAdd, java.util.ArrayList toRemove, com.android.server.job.controllers.JobStatus job) {
        android.util.Pair<java.lang.Long, java.lang.Long> utcTimes = job.getPersistedUtcTimes();
        if (utcTimes != null) {
            android.util.Pair<java.lang.Long, java.lang.Long> elapsedRuntimes = convertRtcBoundsToElapsed(utcTimes, elapsedNow);
            com.android.server.job.controllers.JobStatus newJob = new com.android.server.job.controllers.JobStatus(job, ((java.lang.Long) elapsedRuntimes.first).longValue(), ((java.lang.Long) elapsedRuntimes.second).longValue(), 0, 0, job.getLastSuccessfulRunTime(), job.getLastFailedRunTime(), job.getCumulativeExecutionTimeMs());
            newJob.prepareLocked();
            toAdd.add(newJob);
            toRemove.add(job);
        }
    }

    public void add(com.android.server.job.controllers.JobStatus jobStatus) {
        if (this.mJobSet.add(jobStatus)) {
            this.mCurrentJobSetSize++;
            maybeUpdateHighWaterMark();
        }
        if (jobStatus.isPersisted()) {
            this.mPendingJobWriteUids.put(jobStatus.getUid(), true);
            maybeWriteStatusToDiskAsync();
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Added job status to store: " + jobStatus);
        }
    }

    public void addForTesting(com.android.server.job.controllers.JobStatus jobStatus) {
        if (this.mJobSet.add(jobStatus)) {
            this.mCurrentJobSetSize++;
            maybeUpdateHighWaterMark();
        }
        if (jobStatus.isPersisted()) {
            this.mPendingJobWriteUids.put(jobStatus.getUid(), true);
        }
    }

    boolean containsJob(com.android.server.job.controllers.JobStatus jobStatus) {
        return this.mJobSet.contains(jobStatus);
    }

    public int size() {
        return this.mJobSet.size();
    }

    public com.android.server.job.JobSchedulerInternal.JobStorePersistStats getPersistStats() {
        return this.mPersistInfo;
    }

    public int countJobsForUid(int uid) {
        return this.mJobSet.countJobsForUid(uid);
    }

    public boolean remove(com.android.server.job.controllers.JobStatus jobStatus, boolean removeFromPersisted) {
        boolean removed = this.mJobSet.remove(jobStatus);
        if (!removed) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Couldn't remove job: didn't exist: " + jobStatus);
                return false;
            }
            return false;
        }
        this.mCurrentJobSetSize--;
        if (removeFromPersisted && jobStatus.isPersisted()) {
            this.mPendingJobWriteUids.put(jobStatus.getUid(), true);
            maybeWriteStatusToDiskAsync();
        }
        return removed;
    }

    public void removeForTesting(com.android.server.job.controllers.JobStatus jobStatus) {
        if (this.mJobSet.remove(jobStatus)) {
            this.mCurrentJobSetSize--;
        }
        if (jobStatus.isPersisted()) {
            this.mPendingJobWriteUids.put(jobStatus.getUid(), true);
        }
    }

    public void removeJobsOfUnlistedUsers(int[] keepUserIds) {
        this.mJobSet.removeJobsOfUnlistedUsers(keepUserIds);
        this.mCurrentJobSetSize = this.mJobSet.size();
    }

    void touchJob(com.android.server.job.controllers.JobStatus jobStatus) {
        if (!jobStatus.isPersisted()) {
            return;
        }
        this.mPendingJobWriteUids.put(jobStatus.getUid(), true);
        maybeWriteStatusToDiskAsync();
    }

    public void clear() {
        this.mJobSet.clear();
        this.mPendingJobWriteUids.put(-1, true);
        this.mCurrentJobSetSize = 0;
        maybeWriteStatusToDiskAsync();
    }

    public void clearForTesting() {
        this.mJobSet.clear();
        this.mPendingJobWriteUids.put(-1, true);
        this.mCurrentJobSetSize = 0;
    }

    void setUseSplitFiles(boolean useSplitFiles) {
        synchronized (this.mLock) {
            if (this.mUseSplitFiles != useSplitFiles) {
                this.mUseSplitFiles = useSplitFiles;
                migrateJobFilesAsync();
            }
        }
    }

    public void setUseSplitFilesForTesting(boolean useSplitFiles) {
        boolean changed;
        synchronized (this.mLock) {
            changed = this.mUseSplitFiles != useSplitFiles;
            if (changed) {
                this.mUseSplitFiles = useSplitFiles;
                this.mPendingJobWriteUids.put(-1, true);
            }
        }
        if (changed) {
            synchronized (this.mWriteScheduleLock) {
                this.mSplitFileMigrationNeeded = true;
            }
        }
    }

    public android.util.ArraySet<com.android.server.job.controllers.JobStatus> getJobsBySourceUid(int sourceUid) {
        return this.mJobSet.getJobsBySourceUid(sourceUid);
    }

    public void getJobsBySourceUid(int sourceUid, java.util.Set<com.android.server.job.controllers.JobStatus> insertInto) {
        this.mJobSet.getJobsBySourceUid(sourceUid, insertInto);
    }

    public android.util.ArraySet<com.android.server.job.controllers.JobStatus> getJobsByUid(int uid) {
        return this.mJobSet.getJobsByUid(uid);
    }

    public void getJobsByUid(int uid, java.util.Set<com.android.server.job.controllers.JobStatus> insertInto) {
        this.mJobSet.getJobsByUid(uid, insertInto);
    }

    public com.android.server.job.controllers.JobStatus getJobByUidAndJobId(int uid, java.lang.String namespace, int jobId) {
        return this.mJobSet.get(uid, namespace, jobId);
    }

    public void forEachJob(java.util.function.Consumer<com.android.server.job.controllers.JobStatus> functor) {
        this.mJobSet.forEachJob((java.util.function.Predicate<com.android.server.job.controllers.JobStatus>) null, functor);
    }

    public void forEachJob(java.util.function.Predicate<com.android.server.job.controllers.JobStatus> filterPredicate, java.util.function.Consumer<com.android.server.job.controllers.JobStatus> functor) {
        this.mJobSet.forEachJob(filterPredicate, functor);
    }

    public void forEachJob(int uid, java.util.function.Consumer<com.android.server.job.controllers.JobStatus> functor) {
        this.mJobSet.forEachJob(uid, functor);
    }

    public void forEachJobForSourceUid(int sourceUid, java.util.function.Consumer<com.android.server.job.controllers.JobStatus> functor) {
        this.mJobSet.forEachJobForSourceUid(sourceUid, functor);
    }

    private void maybeUpdateHighWaterMark() {
        if (this.mScheduledJob30MinHighWaterMark < this.mCurrentJobSetSize) {
            this.mScheduledJob30MinHighWaterMark = this.mCurrentJobSetSize;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void migrateJobFilesAsync() {
        synchronized (this.mLock) {
            this.mPendingJobWriteUids.put(-1, true);
        }
        synchronized (this.mWriteScheduleLock) {
            this.mSplitFileMigrationNeeded = true;
            maybeWriteStatusToDiskAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeWriteStatusToDiskAsync() {
        synchronized (this.mWriteScheduleLock) {
            if (!this.mWriteScheduled) {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Scheduling persist of jobs to disk.");
                }
                this.mIoHandler.postDelayed(this.mWriteRunnable, JOB_PERSIST_DELAY);
                this.mWriteScheduled = true;
            }
        }
    }

    public void readJobMapFromDisk(com.android.server.job.JobStore.JobSet jobSet, boolean rtcGood) throws java.lang.Throwable {
        new com.android.server.job.JobStore.ReadJobMapFromDiskRunnable(this, jobSet, rtcGood).run();
    }

    public void writeStatusToDiskForTesting() {
        synchronized (this.mWriteScheduleLock) {
            if (this.mWriteScheduled) {
                throw new java.lang.IllegalStateException("An asynchronous write is already scheduled.");
            }
            this.mWriteScheduled = true;
            this.mWriteRunnable.run();
        }
    }

    public boolean waitForWriteToCompleteForTesting(long maxWaitMillis) {
        long start = android.os.SystemClock.uptimeMillis();
        long end = start + maxWaitMillis;
        synchronized (this.mWriteScheduleLock) {
            while (true) {
                if (!this.mWriteScheduled && !this.mWriteInProgress) {
                    break;
                }
                long now = android.os.SystemClock.uptimeMillis();
                if (now >= end) {
                    return false;
                }
                try {
                    this.mWriteScheduleLock.wait((now - start) + maxWaitMillis);
                } catch (java.lang.InterruptedException e) {
                    return true;
                }
            }
        }
    }

    static java.lang.String intArrayToString(int[] values) {
        java.util.StringJoiner sj = new java.util.StringJoiner(",");
        for (int value : values) {
            sj.add(java.lang.String.valueOf(value));
        }
        return sj.toString();
    }

    static int[] stringToIntArray(java.lang.String str) {
        if (android.text.TextUtils.isEmpty(str)) {
            return new int[0];
        }
        java.lang.String[] arr = str.split(",");
        int[] values = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            values[i] = java.lang.Integer.parseInt(arr[i]);
        }
        return values;
    }

    static int extractUidFromJobFileName(java.io.File file) {
        java.lang.String fileName = file.getName();
        if (fileName.startsWith(JOB_FILE_SPLIT_PREFIX)) {
            try {
                int subEnd = fileName.length() - 4;
                int uid = java.lang.Integer.parseInt(fileName.substring(JOB_FILE_SPLIT_PREFIX.length(), subEnd));
                if (uid < 0) {
                    return -2;
                }
                return uid;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Unexpected file name format", e);
            }
        }
        return -2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.util.Pair<java.lang.Long, java.lang.Long> convertRtcBoundsToElapsed(android.util.Pair<java.lang.Long, java.lang.Long> rtcTimes, long nowElapsed) {
        long earliest;
        long nowWallclock = com.android.server.job.JobSchedulerService.sSystemClock.millis();
        if (((java.lang.Long) rtcTimes.first).longValue() > 0) {
            earliest = java.lang.Math.max(((java.lang.Long) rtcTimes.first).longValue() - nowWallclock, 0L) + nowElapsed;
        } else {
            earliest = 0;
        }
        long latest = ((java.lang.Long) rtcTimes.second).longValue() < Long.MAX_VALUE ? nowElapsed + java.lang.Math.max(((java.lang.Long) rtcTimes.second).longValue() - nowWallclock, 0L) : Long.MAX_VALUE;
        return android.util.Pair.create(java.lang.Long.valueOf(earliest), java.lang.Long.valueOf(latest));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSyncJob(com.android.server.job.controllers.JobStatus status) {
        return com.android.server.content.SyncJobService.class.getName().equals(status.getServiceComponent().getClassName());
    }

    private final class ReadJobMapFromDiskRunnable implements java.lang.Runnable {
        private final com.android.server.job.JobStore.JobSet jobSet;
        private final java.util.concurrent.CountDownLatch mCompletionLatch;
        private final boolean rtcGood;

        ReadJobMapFromDiskRunnable(com.android.server.job.JobStore jobStore, com.android.server.job.JobStore.JobSet jobSet, boolean rtcIsGood) {
            this(jobSet, rtcIsGood, null);
        }

        ReadJobMapFromDiskRunnable(com.android.server.job.JobStore.JobSet jobSet, boolean rtcIsGood, java.util.concurrent.CountDownLatch completionLatch) {
            this.jobSet = jobSet;
            this.rtcGood = rtcIsGood;
            this.mCompletionLatch = completionLatch;
        }

        /* JADX WARN: Can't wrap try/catch for region: R(15:141|17|(4:147|19|(2:21|158)|96)|24|25|153|26|27|128|28|(3:30|(4:31|151|32|(12:34|35|132|36|37|145|38|39|143|40|(2:42|163)(2:43|(2:45|(2:47|162)(1:165))(1:164))|48)(1:161))|56)(1:59)|(2:134|61)|87|(2:89|(2:91|159)(2:95|157))(2:92|(2:94|160)(0))|96) */
        /* JADX WARN: Code restructure failed: missing block: B:78:0x0164, code lost:
        
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:79:0x0165, code lost:
        
            r21 = r2;
            r16 = r5;
            r20 = r9;
            r17 = r11;
            r2 = r3;
            r3 = r4;
            r4 = r6;
            r9 = r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:81:0x017b, code lost:
        
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:82:0x017c, code lost:
        
            r21 = r2;
            r16 = r5;
            r20 = r9;
            r17 = r11;
            r2 = r3;
            r3 = r4;
            r4 = r6;
            r9 = r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:85:0x01a9, code lost:
        
            r21 = r2;
            r16 = r5;
            r20 = r9;
            r17 = r11;
            r2 = r3;
            r3 = r4;
            r4 = r6;
            r9 = r12;
         */
        /* JADX WARN: Removed duplicated region for block: B:89:0x01dc A[Catch: all -> 0x0212, TryCatch #7 {all -> 0x0212, blocks: (B:71:0x015c, B:70:0x0159, B:61:0x013c, B:87:0x01d4, B:89:0x01dc, B:92:0x01f0, B:80:0x0173, B:83:0x018a, B:86:0x01b7), top: B:136:0x0159 }] */
        /* JADX WARN: Removed duplicated region for block: B:92:0x01f0 A[Catch: all -> 0x0212, TRY_LEAVE, TryCatch #7 {all -> 0x0212, blocks: (B:71:0x015c, B:70:0x0159, B:61:0x013c, B:87:0x01d4, B:89:0x01dc, B:92:0x01f0, B:80:0x0173, B:83:0x018a, B:86:0x01b7), top: B:136:0x0159 }] */
        /* JADX WARN: Removed duplicated region for block: B:95:0x0204  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 715
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobStore.ReadJobMapFromDiskRunnable.run():void");
        }

        private static java.lang.String intern(java.lang.String val) {
            if (val == null) {
                return null;
            }
            return val.intern();
        }

        private java.util.List<com.android.server.job.controllers.JobStatus> readJobMapImpl(java.io.InputStream fis, boolean rtcIsGood, long nowElapsed) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(fis);
            int eventType = parser.getEventType();
            while (eventType != 2 && eventType != 1) {
                eventType = parser.next();
                android.util.Slog.d(com.android.server.job.JobStore.TAG, "Start tag: " + parser.getName());
            }
            if (eventType == 1) {
                if (com.android.server.job.JobStore.DEBUG) {
                    android.util.Slog.d(com.android.server.job.JobStore.TAG, "No persisted jobs.");
                }
                return null;
            }
            java.lang.String tagName = parser.getName();
            if (!com.android.server.job.JobStore.XML_TAG_JOB_INFO.equals(tagName)) {
                return null;
            }
            java.util.List<com.android.server.job.controllers.JobStatus> jobs = new java.util.ArrayList<>();
            int version = parser.getAttributeInt((java.lang.String) null, "version");
            if (version > 1 || version < 0) {
                android.util.Slog.d(com.android.server.job.JobStore.TAG, "Invalid version number, aborting jobs file read.");
                return null;
            }
            int eventType2 = parser.next();
            int eventType3 = eventType2;
            do {
                if (eventType3 == 2) {
                    java.lang.String tagName2 = parser.getName();
                    if ("job".equals(tagName2)) {
                        com.android.server.job.controllers.JobStatus persistedJob = restoreJobFromXml(rtcIsGood, parser, version, nowElapsed);
                        if (persistedJob != null) {
                            if (com.android.server.job.JobStore.DEBUG) {
                                android.util.Slog.d(com.android.server.job.JobStore.TAG, "Read out " + persistedJob);
                            }
                            jobs.add(persistedJob);
                        } else {
                            android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error reading job from file.");
                        }
                    }
                }
                eventType3 = parser.next();
            } while (eventType3 != 1);
            return jobs;
        }

        private com.android.server.job.controllers.JobStatus restoreJobFromXml(boolean rtcIsGood, com.android.modules.utils.TypedXmlPullParser parser, int schemaVersion, long nowElapsed) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            android.app.job.JobInfo.Builder jobBuilder;
            int uid;
            int internalFlags;
            int eventType;
            int eventType2;
            long jLongValue;
            int internalFlags2;
            android.util.Pair<java.lang.Long, java.lang.Long> rtcRuntimes;
            int sourceUserId;
            int eventType3;
            android.util.Pair<java.lang.Long, java.lang.Long> elapsedRuntimes;
            int eventType4;
            java.util.List<android.app.job.JobWorkItem> jobWorkItems;
            com.android.server.job.JobStore.ReadJobMapFromDiskRunnable readJobMapFromDiskRunnable = this;
            com.android.server.job.controllers.JobStatus jobStatus = null;
            try {
                jobBuilder = readJobMapFromDiskRunnable.buildBuilderFromXml(parser);
                jobBuilder.setPersisted(true);
                uid = java.lang.Integer.parseInt(parser.getAttributeValue((java.lang.String) null, "uid"));
                if (schemaVersion == 0) {
                    java.lang.String val = parser.getAttributeValue((java.lang.String) null, "priority");
                    if (val != null) {
                        jobBuilder.setBias(java.lang.Integer.parseInt(val));
                    }
                } else if (schemaVersion >= 1) {
                    java.lang.String val2 = parser.getAttributeValue((java.lang.String) null, "bias");
                    if (val2 != null) {
                        jobBuilder.setBias(java.lang.Integer.parseInt(val2));
                    }
                    java.lang.String val3 = parser.getAttributeValue((java.lang.String) null, "priority");
                    if (val3 != null) {
                        jobBuilder.setPriority(java.lang.Integer.parseInt(val3));
                    }
                }
                java.lang.String val4 = parser.getAttributeValue((java.lang.String) null, "flags");
                if (val4 != null) {
                    jobBuilder.setFlags(java.lang.Integer.parseInt(val4));
                }
                java.lang.String val5 = parser.getAttributeValue((java.lang.String) null, "internalFlags");
                internalFlags = val5 != null ? java.lang.Integer.parseInt(val5) : 0;
            } catch (java.lang.NumberFormatException e) {
            }
            try {
                java.lang.String val6 = parser.getAttributeValue((java.lang.String) null, "sourceUserId");
                int sourceUserId2 = val6 == null ? -1 : java.lang.Integer.parseInt(val6);
                java.lang.String val7 = parser.getAttributeValue((java.lang.String) null, "lastSuccessfulRunTime");
                long j = 0;
                long lastSuccessfulRunTime = val7 == null ? 0L : java.lang.Long.parseLong(val7);
                java.lang.String val8 = parser.getAttributeValue((java.lang.String) null, "lastFailedRunTime");
                long lastFailedRunTime = val8 == null ? 0L : java.lang.Long.parseLong(val8);
                long cumulativeExecutionTime = parser.getAttributeLong((java.lang.String) null, "cumulativeExecutionTime", 0L);
                java.lang.String sourcePackageName = parser.getAttributeValue((java.lang.String) null, "sourcePackageName");
                java.lang.String namespace = intern(parser.getAttributeValue((java.lang.String) null, "namespace"));
                java.lang.String sourceTag = intern(parser.getAttributeValue((java.lang.String) null, "sourceTag"));
                while (true) {
                    eventType = parser.next();
                    if (eventType != 4) {
                        break;
                    }
                    j = j;
                    jobStatus = null;
                    sourceUserId2 = sourceUserId2;
                    readJobMapFromDiskRunnable = this;
                }
                int i = 2;
                if (eventType != 2) {
                    return jobStatus;
                }
                if (!com.android.server.job.JobStore.XML_TAG_PARAMS_CONSTRAINTS.equals(parser.getName())) {
                    return null;
                }
                try {
                    readJobMapFromDiskRunnable.buildConstraintsFromXml(jobBuilder, parser);
                    parser.next();
                    while (true) {
                        eventType2 = parser.next();
                        if (eventType2 != 4) {
                            break;
                        }
                        sourceUserId2 = sourceUserId2;
                        internalFlags = internalFlags;
                        i = i;
                        readJobMapFromDiskRunnable = this;
                    }
                    if (eventType2 != i) {
                        return null;
                    }
                    android.util.Pair<java.lang.Long, java.lang.Long> rtcRuntimes2 = readJobMapFromDiskRunnable.buildRtcExecutionTimesFromXml(parser);
                    android.util.Pair<java.lang.Long, java.lang.Long> elapsedRuntimes2 = com.android.server.job.JobStore.convertRtcBoundsToElapsed(rtcRuntimes2, nowElapsed);
                    if (com.android.server.job.JobStore.XML_TAG_PERIODIC.equals(parser.getName())) {
                        try {
                            long periodMillis = java.lang.Long.parseLong(parser.getAttributeValue((java.lang.String) null, "period"));
                            java.lang.String val9 = parser.getAttributeValue((java.lang.String) null, "flex");
                            if (val9 == null) {
                                jLongValue = periodMillis;
                            } else {
                                try {
                                    jLongValue = java.lang.Long.valueOf(val9).longValue();
                                } catch (java.lang.NumberFormatException e2) {
                                    android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error reading periodic execution criteria, skipping.");
                                    return null;
                                }
                            }
                            long flexMillis = jLongValue;
                            internalFlags2 = internalFlags;
                            rtcRuntimes = rtcRuntimes2;
                            sourceUserId = sourceUserId2;
                            try {
                                jobBuilder.setPeriodic(periodMillis, flexMillis);
                                if (((java.lang.Long) elapsedRuntimes2.second).longValue() > nowElapsed + periodMillis + flexMillis) {
                                    long clampedLateRuntimeElapsed = nowElapsed + flexMillis + periodMillis;
                                    long clampedEarlyRuntimeElapsed = clampedLateRuntimeElapsed - flexMillis;
                                    eventType3 = eventType2;
                                    try {
                                        android.util.Slog.w(com.android.server.job.JobStore.TAG, java.lang.String.format("Periodic job for uid='%d' persisted run-time is too big [%s, %s]. Clamping to [%s,%s]", java.lang.Integer.valueOf(uid), android.text.format.DateUtils.formatElapsedTime(((java.lang.Long) elapsedRuntimes2.first).longValue() / 1000), android.text.format.DateUtils.formatElapsedTime(((java.lang.Long) elapsedRuntimes2.second).longValue() / 1000), android.text.format.DateUtils.formatElapsedTime(clampedEarlyRuntimeElapsed / 1000), android.text.format.DateUtils.formatElapsedTime(clampedLateRuntimeElapsed / 1000)));
                                        elapsedRuntimes2 = android.util.Pair.create(java.lang.Long.valueOf(clampedEarlyRuntimeElapsed), java.lang.Long.valueOf(clampedLateRuntimeElapsed));
                                    } catch (java.lang.NumberFormatException e3) {
                                        android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error reading periodic execution criteria, skipping.");
                                        return null;
                                    }
                                } else {
                                    eventType3 = eventType2;
                                }
                                elapsedRuntimes = elapsedRuntimes2;
                            } catch (java.lang.NumberFormatException e4) {
                            }
                        } catch (java.lang.NumberFormatException e5) {
                        }
                    } else {
                        internalFlags2 = internalFlags;
                        rtcRuntimes = rtcRuntimes2;
                        sourceUserId = sourceUserId2;
                        eventType3 = eventType2;
                        if (!com.android.server.job.JobStore.XML_TAG_ONEOFF.equals(parser.getName())) {
                            if (com.android.server.job.JobStore.DEBUG) {
                                android.util.Slog.d(com.android.server.job.JobStore.TAG, "Invalid parameter tag, skipping - " + parser.getName());
                                return null;
                            }
                            return null;
                        }
                        try {
                            if (((java.lang.Long) elapsedRuntimes2.first).longValue() != 0) {
                                try {
                                    jobBuilder.setMinimumLatency(((java.lang.Long) elapsedRuntimes2.first).longValue() - nowElapsed);
                                } catch (java.lang.NumberFormatException e6) {
                                    android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error reading job execution criteria, skipping.");
                                    return null;
                                }
                            }
                            if (((java.lang.Long) elapsedRuntimes2.second).longValue() != Long.MAX_VALUE) {
                                jobBuilder.setOverrideDeadline(((java.lang.Long) elapsedRuntimes2.second).longValue() - nowElapsed);
                            }
                            elapsedRuntimes = elapsedRuntimes2;
                        } catch (java.lang.NumberFormatException e7) {
                        }
                    }
                    readJobMapFromDiskRunnable.maybeBuildBackoffPolicyFromXml(jobBuilder, parser);
                    parser.nextTag();
                    while (true) {
                        eventType4 = parser.next();
                        if (eventType4 != 4) {
                            break;
                        }
                        readJobMapFromDiskRunnable = this;
                    }
                    if (eventType4 != 2 || !com.android.server.job.JobStore.XML_TAG_EXTRAS.equals(parser.getName())) {
                        if (com.android.server.job.JobStore.DEBUG) {
                            android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error reading extras, skipping.");
                            return null;
                        }
                        return null;
                    }
                    try {
                        android.os.PersistableBundle extras = android.os.PersistableBundle.restoreFromXml(parser);
                        jobBuilder.setExtras(extras);
                        int eventType5 = parser.nextTag();
                        if (eventType5 == 2 && com.android.server.job.JobStore.XML_TAG_JOB_WORK_ITEM.equals(parser.getName())) {
                            java.util.List<android.app.job.JobWorkItem> jobWorkItems2 = readJobMapFromDiskRunnable.readJobWorkItemsFromXml(parser);
                            jobWorkItems = jobWorkItems2;
                        } else {
                            jobWorkItems = null;
                        }
                        if (eventType5 == 2 && com.android.server.job.JobStore.XML_TAG_DEBUG_INFO.equals(parser.getName())) {
                            try {
                                jobBuilder.setTraceTag(parser.getAttributeValue((java.lang.String) null, "trace-tag"));
                            } catch (java.lang.Exception e8) {
                                android.util.Slog.wtf(com.android.server.job.JobStore.TAG, "Invalid trace tag persisted to disk", e8);
                            }
                            parser.next();
                            jobBuilder.addDebugTags(readJobMapFromDiskRunnable.readDebugTagsFromXml(parser));
                            parser.nextTag();
                        }
                        try {
                            android.app.job.JobInfo builtJob = jobBuilder.build(false, false, false, false);
                            if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(sourcePackageName) && extras != null && extras.getBoolean("SyncManagerJob", false)) {
                                sourcePackageName = extras.getString("owningPackage", sourcePackageName);
                                if (com.android.server.job.JobStore.DEBUG) {
                                    android.util.Slog.i(com.android.server.job.JobStore.TAG, "Fixing up sync job source package name from 'android' to '" + sourcePackageName + "'");
                                }
                            }
                            java.lang.String sourcePackageName2 = sourcePackageName;
                            int sourceUserId3 = sourceUserId;
                            int appBucket = com.android.server.job.JobSchedulerService.standbyBucketForPackage(sourcePackageName2, sourceUserId3, nowElapsed);
                            java.util.List<android.app.job.JobWorkItem> jobWorkItems3 = jobWorkItems;
                            com.android.server.job.controllers.JobStatus js = new com.android.server.job.controllers.JobStatus(builtJob, uid, intern(sourcePackageName2), sourceUserId3, appBucket, namespace, sourceTag, ((java.lang.Long) elapsedRuntimes.first).longValue(), ((java.lang.Long) elapsedRuntimes.second).longValue(), lastSuccessfulRunTime, lastFailedRunTime, cumulativeExecutionTime, rtcIsGood ? null : rtcRuntimes, internalFlags2, 0);
                            if (jobWorkItems3 != null) {
                                for (int i2 = 0; i2 < jobWorkItems3.size(); i2++) {
                                    js.enqueueWorkLocked(jobWorkItems3.get(i2));
                                }
                            }
                            if (js.getWrapper().getExtImpl().getBooleanValue("getSyncJobAbnormal", null, false)) {
                                return null;
                            }
                            return js;
                        } catch (java.lang.Exception e9) {
                            android.util.Slog.w(com.android.server.job.JobStore.TAG, "Unable to build job from XML, ignoring: " + jobBuilder.summarize(), e9);
                            return null;
                        }
                    } catch (java.lang.IllegalArgumentException e10) {
                        android.util.Slog.e(com.android.server.job.JobStore.TAG, "Persisted extras contained invalid data", e10);
                        return null;
                    }
                } catch (java.io.IOException e11) {
                    android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error I/O Exception.", e11);
                    return null;
                } catch (java.lang.NumberFormatException e12) {
                    android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error reading constraints, skipping.");
                    return null;
                } catch (java.lang.IllegalArgumentException e13) {
                    android.util.Slog.e(com.android.server.job.JobStore.TAG, "Constraints contained invalid data", e13);
                    return null;
                } catch (org.xmlpull.v1.XmlPullParserException e14) {
                    android.util.Slog.d(com.android.server.job.JobStore.TAG, "Error Parser Exception.", e14);
                    return null;
                }
            } catch (java.lang.NumberFormatException e15) {
                android.util.Slog.e(com.android.server.job.JobStore.TAG, "Error parsing job's required fields, skipping");
                return null;
            }
        }

        private android.app.job.JobInfo.Builder buildBuilderFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
            int jobId = parser.getAttributeInt((java.lang.String) null, "jobid");
            java.lang.String packageName = intern(parser.getAttributeValue((java.lang.String) null, "package"));
            java.lang.String className = intern(parser.getAttributeValue((java.lang.String) null, "class"));
            android.content.ComponentName cname = new android.content.ComponentName(packageName, className);
            return new android.app.job.JobInfo.Builder(jobId, cname);
        }

        private void buildConstraintsFromXml(android.app.job.JobInfo.Builder jobBuilder, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            boolean z;
            boolean z2;
            boolean z3;
            java.lang.String netCapabilitiesLong = null;
            java.lang.String netForbiddenCapabilitiesLong = null;
            java.lang.String netTransportTypesLong = null;
            java.lang.String netCapabilitiesIntArray = parser.getAttributeValue((java.lang.String) null, "net-capabilities-csv");
            java.lang.String netForbiddenCapabilitiesIntArray = parser.getAttributeValue((java.lang.String) null, "net-forbidden-capabilities-csv");
            java.lang.String netTransportTypesIntArray = parser.getAttributeValue((java.lang.String) null, "net-transport-types-csv");
            if (netCapabilitiesIntArray == null || netTransportTypesIntArray == null) {
                netCapabilitiesLong = parser.getAttributeValue((java.lang.String) null, "net-capabilities");
                netForbiddenCapabilitiesLong = parser.getAttributeValue((java.lang.String) null, "net-unwanted-capabilities");
                netTransportTypesLong = parser.getAttributeValue((java.lang.String) null, "net-transport-types");
            }
            if (netCapabilitiesIntArray != null && netTransportTypesIntArray != null) {
                android.net.NetworkRequest.Builder builder = new android.net.NetworkRequest.Builder().clearCapabilities();
                for (int i : com.android.server.job.JobStore.stringToIntArray(netCapabilitiesIntArray)) {
                    builder.addCapability(i);
                }
                for (int i2 : com.android.server.job.JobStore.stringToIntArray(netForbiddenCapabilitiesIntArray)) {
                    builder.addForbiddenCapability(i2);
                }
                for (int i3 : com.android.server.job.JobStore.stringToIntArray(netTransportTypesIntArray)) {
                    builder.addTransportType(i3);
                }
                jobBuilder.setRequiredNetwork(builder.build()).setEstimatedNetworkBytes(parser.getAttributeLong((java.lang.String) null, "estimated-download-bytes", -1L), parser.getAttributeLong((java.lang.String) null, "estimated-upload-bytes", -1L)).setMinimumNetworkChunkBytes(parser.getAttributeLong((java.lang.String) null, "minimum-network-chunk-bytes", -1L));
            } else if (netCapabilitiesLong != null && netTransportTypesLong != null) {
                android.net.NetworkRequest.Builder builder2 = new android.net.NetworkRequest.Builder().clearCapabilities();
                for (int capability : com.android.internal.util.BitUtils.unpackBits(java.lang.Long.parseLong(netCapabilitiesLong))) {
                    if (capability <= 25) {
                        builder2.addCapability(capability);
                    }
                }
                for (int forbiddenCapability : com.android.internal.util.BitUtils.unpackBits(java.lang.Long.parseLong(netForbiddenCapabilitiesLong))) {
                    if (forbiddenCapability <= 25) {
                        builder2.addForbiddenCapability(forbiddenCapability);
                    }
                }
                for (int transport : com.android.internal.util.BitUtils.unpackBits(java.lang.Long.parseLong(netTransportTypesLong))) {
                    if (transport <= 7) {
                        builder2.addTransportType(transport);
                    }
                }
                jobBuilder.setRequiredNetwork(builder2.build());
            } else {
                java.lang.String val = parser.getAttributeValue((java.lang.String) null, "connectivity");
                if (val != null) {
                    jobBuilder.setRequiredNetworkType(1);
                }
                java.lang.String val2 = parser.getAttributeValue((java.lang.String) null, "metered");
                if (val2 != null) {
                    jobBuilder.setRequiredNetworkType(4);
                }
                java.lang.String val3 = parser.getAttributeValue((java.lang.String) null, "unmetered");
                if (val3 != null) {
                    jobBuilder.setRequiredNetworkType(2);
                }
                java.lang.String val4 = parser.getAttributeValue((java.lang.String) null, "not-roaming");
                if (val4 != null) {
                    jobBuilder.setRequiredNetworkType(3);
                }
            }
            java.lang.String val5 = parser.getAttributeValue((java.lang.String) null, "idle");
            if (val5 == null) {
                z = true;
            } else {
                z = true;
                jobBuilder.setRequiresDeviceIdle(true);
            }
            java.lang.String val6 = parser.getAttributeValue((java.lang.String) null, "charging");
            if (val6 != null) {
                jobBuilder.setRequiresCharging(z);
            }
            java.lang.String val7 = parser.getAttributeValue((java.lang.String) null, "battery-not-low");
            if (val7 != null) {
                jobBuilder.setRequiresBatteryNotLow(z);
            }
            java.lang.String val8 = parser.getAttributeValue((java.lang.String) null, "storage-not-low");
            if (val8 != null) {
                jobBuilder.setRequiresStorageNotLow(z);
            }
            java.lang.String val9 = parser.getAttributeValue((java.lang.String) null, "requireBattIdle");
            if (val9 != null) {
                jobBuilder.makeBuilderExt().setRequiresBattIdle(z, 0);
            }
            java.lang.String val10 = parser.getAttributeValue((java.lang.String) null, "requireProtectFore");
            if (val10 == null) {
                z2 = true;
            } else {
                z2 = true;
                jobBuilder.makeBuilderExt().setRequiresProtectFore(true, java.lang.Integer.parseInt(val10));
            }
            java.lang.String val11 = parser.getAttributeValue((java.lang.String) null, "hasCpuConstraint");
            if (val11 != null) {
                jobBuilder.makeBuilderExt().setHasCpuConstraint(z2);
            }
            java.lang.String val12 = parser.getAttributeValue((java.lang.String) null, "hasTemperatureConstraint");
            if (val12 != null) {
                jobBuilder.makeBuilderExt().setHasTemperatureConstraint(z2);
            }
            java.lang.String val13 = parser.getAttributeValue((java.lang.String) null, "hasProtectSceneConstraint");
            if (val13 == null) {
                z3 = true;
            } else {
                z3 = true;
                jobBuilder.makeBuilderExt().setRequiresProtectScene(true, java.lang.Integer.parseInt(val13));
            }
            java.lang.String val14 = parser.getAttributeValue((java.lang.String) null, "isOplusJob");
            if (val14 != null) {
                jobBuilder.makeBuilderExt().setOplusJob(z3);
            }
            java.lang.String val15 = parser.getAttributeValue((java.lang.String) null, "isFastIdle");
            if (val15 != null) {
                jobBuilder.makeBuilderExt().setFastIdle(z3);
            }
        }

        private void maybeBuildBackoffPolicyFromXml(android.app.job.JobInfo.Builder jobBuilder, org.xmlpull.v1.XmlPullParser parser) {
            java.lang.String val = parser.getAttributeValue(null, "initial-backoff");
            if (val != null) {
                long initialBackoff = java.lang.Long.parseLong(val);
                int backoffPolicy = java.lang.Integer.parseInt(parser.getAttributeValue(null, "backoff-policy"));
                jobBuilder.setBackoffCriteria(initialBackoff, backoffPolicy);
            }
        }

        private android.util.Pair<java.lang.Long, java.lang.Long> buildRtcExecutionTimesFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
            long earliestRunTimeRtc = parser.getAttributeLong((java.lang.String) null, "delay", 0L);
            long latestRunTimeRtc = parser.getAttributeLong((java.lang.String) null, "deadline", Long.MAX_VALUE);
            return android.util.Pair.create(java.lang.Long.valueOf(earliestRunTimeRtc), java.lang.Long.valueOf(latestRunTimeRtc));
        }

        private java.util.List<android.app.job.JobWorkItem> readJobWorkItemsFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.util.List<android.app.job.JobWorkItem> jobWorkItems = new java.util.ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != 1) {
                java.lang.String tagName = parser.getName();
                if (!com.android.server.job.JobStore.XML_TAG_JOB_WORK_ITEM.equals(tagName)) {
                    break;
                }
                try {
                    android.app.job.JobWorkItem jwi = readJobWorkItemFromXml(parser);
                    if (jwi != null) {
                        jobWorkItems.add(jwi);
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.job.JobStore.TAG, "Problem with persisted JobWorkItem", e);
                }
                eventType = parser.next();
            }
            return jobWorkItems;
        }

        private android.app.job.JobWorkItem readJobWorkItemFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            android.app.job.JobWorkItem.Builder jwiBuilder = new android.app.job.JobWorkItem.Builder();
            jwiBuilder.setDeliveryCount(parser.getAttributeInt((java.lang.String) null, "delivery-count")).setEstimatedNetworkBytes(parser.getAttributeLong((java.lang.String) null, "estimated-download-bytes", -1L), parser.getAttributeLong((java.lang.String) null, "estimated-upload-bytes", -1L)).setMinimumNetworkChunkBytes(parser.getAttributeLong((java.lang.String) null, "minimum-network-chunk-bytes", -1L));
            parser.next();
            try {
                android.os.PersistableBundle extras = android.os.PersistableBundle.restoreFromXml(parser);
                jwiBuilder.setExtras(extras);
                try {
                    return jwiBuilder.build();
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.job.JobStore.TAG, "Invalid JobWorkItem", e);
                    return null;
                }
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.e(com.android.server.job.JobStore.TAG, "Persisted extras contained invalid data", e2);
                return null;
            }
        }

        private java.util.Set<java.lang.String> readDebugTagsFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.util.Set<java.lang.String> debugTags = new android.util.ArraySet<>();
            int eventType = parser.getEventType();
            while (eventType != 1) {
                java.lang.String tagName = parser.getName();
                if (!com.android.server.job.JobStore.XML_TAG_DEBUG_TAG.equals(tagName)) {
                    break;
                }
                if (debugTags.size() < 32) {
                    try {
                        java.lang.String debugTag = android.app.job.JobInfo.validateDebugTag(parser.getAttributeValue((java.lang.String) null, "tag"));
                        debugTags.add(debugTag);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.wtf(com.android.server.job.JobStore.TAG, "Invalid debug tag persisted to disk", e);
                    }
                }
                eventType = parser.next();
            }
            return debugTags;
        }
    }

    public static final class JobSet {
        final android.util.SparseArray<android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mJobs = new android.util.SparseArray<>();
        final android.util.SparseArray<android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mJobsPerSourceUid = new android.util.SparseArray<>();

        public android.util.ArraySet<com.android.server.job.controllers.JobStatus> getJobsByUid(int uid) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> matchingJobs = new android.util.ArraySet<>();
            getJobsByUid(uid, matchingJobs);
            return matchingJobs;
        }

        public void getJobsByUid(int uid, java.util.Set<com.android.server.job.controllers.JobStatus> insertInto) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.get(uid);
            if (jobs != null) {
                insertInto.addAll(jobs);
            }
        }

        public android.util.ArraySet<com.android.server.job.controllers.JobStatus> getJobsBySourceUid(int sourceUid) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> result = new android.util.ArraySet<>();
            getJobsBySourceUid(sourceUid, result);
            return result;
        }

        public void getJobsBySourceUid(int sourceUid, java.util.Set<com.android.server.job.controllers.JobStatus> insertInto) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobsPerSourceUid.get(sourceUid);
            if (jobs != null) {
                insertInto.addAll(jobs);
            }
        }

        public boolean add(com.android.server.job.controllers.JobStatus job) {
            int uid = job.getUid();
            int sourceUid = job.getSourceUid();
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.get(uid);
            if (jobs == null) {
                jobs = new android.util.ArraySet<>();
                this.mJobs.put(uid, jobs);
            }
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobsForSourceUid = this.mJobsPerSourceUid.get(sourceUid);
            if (jobsForSourceUid == null) {
                jobsForSourceUid = new android.util.ArraySet<>();
                this.mJobsPerSourceUid.put(sourceUid, jobsForSourceUid);
            }
            boolean added = jobs.add(job);
            boolean addedInSource = jobsForSourceUid.add(job);
            if (added != addedInSource) {
                android.util.Slog.wtf(com.android.server.job.JobStore.TAG, "mJobs and mJobsPerSourceUid mismatch; caller= " + added + " source= " + addedInSource);
            }
            return added || addedInSource;
        }

        public boolean remove(com.android.server.job.controllers.JobStatus job) {
            int uid = job.getUid();
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.get(uid);
            int sourceUid = job.getSourceUid();
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobsForSourceUid = this.mJobsPerSourceUid.get(sourceUid);
            boolean didRemove = jobs != null && jobs.remove(job);
            boolean sourceRemove = jobsForSourceUid != null && jobsForSourceUid.remove(job);
            if (didRemove != sourceRemove) {
                android.util.Slog.wtf(com.android.server.job.JobStore.TAG, "Job presence mismatch; caller=" + didRemove + " source=" + sourceRemove);
            }
            if (!didRemove && !sourceRemove) {
                return false;
            }
            if (jobs != null && jobs.size() == 0) {
                this.mJobs.remove(uid);
            }
            if (jobsForSourceUid != null && jobsForSourceUid.size() == 0) {
                this.mJobsPerSourceUid.remove(sourceUid);
            }
            return true;
        }

        public void removeJobsOfUnlistedUsers(final int[] keepUserIds) {
            java.util.function.Predicate<com.android.server.job.controllers.JobStatus> noSourceUser = new java.util.function.Predicate() { // from class: com.android.server.job.JobStore$JobSet$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.job.JobStore.JobSet.lambda$removeJobsOfUnlistedUsers$0(keepUserIds, (com.android.server.job.controllers.JobStatus) obj);
                }
            };
            java.util.function.Predicate<com.android.server.job.controllers.JobStatus> noCallingUser = new java.util.function.Predicate() { // from class: com.android.server.job.JobStore$JobSet$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.job.JobStore.JobSet.lambda$removeJobsOfUnlistedUsers$1(keepUserIds, (com.android.server.job.controllers.JobStatus) obj);
                }
            };
            removeAll(noSourceUser.or(noCallingUser));
        }

        static /* synthetic */ boolean lambda$removeJobsOfUnlistedUsers$0(int[] keepUserIds, com.android.server.job.controllers.JobStatus job) {
            return !com.android.internal.util.ArrayUtils.contains(keepUserIds, job.getSourceUserId());
        }

        static /* synthetic */ boolean lambda$removeJobsOfUnlistedUsers$1(int[] keepUserIds, com.android.server.job.controllers.JobStatus job) {
            return !com.android.internal.util.ArrayUtils.contains(keepUserIds, job.getUserId());
        }

        private void removeAll(java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
            for (int jobSetIndex = this.mJobs.size() - 1; jobSetIndex >= 0; jobSetIndex--) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.valueAt(jobSetIndex);
                jobs.removeIf(predicate);
                if (jobs.size() == 0) {
                    this.mJobs.removeAt(jobSetIndex);
                }
            }
            for (int jobSetIndex2 = this.mJobsPerSourceUid.size() - 1; jobSetIndex2 >= 0; jobSetIndex2--) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs2 = this.mJobsPerSourceUid.valueAt(jobSetIndex2);
                jobs2.removeIf(predicate);
                if (jobs2.size() == 0) {
                    this.mJobsPerSourceUid.removeAt(jobSetIndex2);
                }
            }
        }

        public boolean contains(com.android.server.job.controllers.JobStatus job) {
            int uid = job.getUid();
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.get(uid);
            return jobs != null && jobs.contains(job);
        }

        public com.android.server.job.controllers.JobStatus get(int uid, java.lang.String namespace, int jobId) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.get(uid);
            if (jobs != null) {
                for (int i = jobs.size() - 1; i >= 0; i--) {
                    com.android.server.job.controllers.JobStatus job = jobs.valueAt(i);
                    if (job.getJobId() == jobId && java.util.Objects.equals(namespace, job.getNamespace())) {
                        return job;
                    }
                }
                return null;
            }
            return null;
        }

        public java.util.List<com.android.server.job.controllers.JobStatus> getAllJobs() {
            java.util.ArrayList<com.android.server.job.controllers.JobStatus> allJobs = new java.util.ArrayList<>(size());
            for (int i = this.mJobs.size() - 1; i >= 0; i--) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.valueAt(i);
                if (jobs != null) {
                    for (int j = jobs.size() - 1; j >= 0; j--) {
                        allJobs.add(jobs.valueAt(j));
                    }
                }
            }
            return allJobs;
        }

        public void clear() {
            this.mJobs.clear();
            this.mJobsPerSourceUid.clear();
        }

        public int size() {
            int total = 0;
            for (int i = this.mJobs.size() - 1; i >= 0; i--) {
                total += this.mJobs.valueAt(i).size();
            }
            return total;
        }

        public int countJobsForUid(int uid) {
            int total = 0;
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.get(uid);
            if (jobs != null) {
                for (int i = jobs.size() - 1; i >= 0; i--) {
                    com.android.server.job.controllers.JobStatus job = jobs.valueAt(i);
                    if (job.getUid() == job.getSourceUid()) {
                        total++;
                    }
                }
            }
            return total;
        }

        public void forEachJob(java.util.function.Predicate<com.android.server.job.controllers.JobStatus> filterPredicate, java.util.function.Consumer<com.android.server.job.controllers.JobStatus> functor) {
            for (int uidIndex = this.mJobs.size() - 1; uidIndex >= 0; uidIndex--) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.valueAt(uidIndex);
                if (jobs != null) {
                    for (int i = jobs.size() - 1; i >= 0; i--) {
                        com.android.server.job.controllers.JobStatus jobStatus = jobs.valueAt(i);
                        if (filterPredicate == null || filterPredicate.test(jobStatus)) {
                            functor.accept(jobStatus);
                        }
                    }
                }
            }
        }

        public void forEachJob(int callingUid, java.util.function.Consumer<com.android.server.job.controllers.JobStatus> functor) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.get(callingUid);
            if (jobs != null) {
                for (int i = jobs.size() - 1; i >= 0; i--) {
                    functor.accept(jobs.valueAt(i));
                }
            }
        }

        public void forEachJobForSourceUid(int sourceUid, java.util.function.Consumer<com.android.server.job.controllers.JobStatus> functor) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobsPerSourceUid.get(sourceUid);
            if (jobs != null) {
                for (int i = jobs.size() - 1; i >= 0; i--) {
                    functor.accept(jobs.valueAt(i));
                }
            }
        }
    }
}
