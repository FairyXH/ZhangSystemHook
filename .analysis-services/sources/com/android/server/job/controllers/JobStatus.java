package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class JobStatus {
    static final int CONSTRAINTS_OF_INTEREST = -1801421809;
    static final int CONSTRAINT_BACKGROUND_NOT_RESTRICTED = 4194304;
    public static final int CONSTRAINT_BATTERY_NOT_LOW = 2;
    public static final int CONSTRAINT_CHARGING = 1;
    public static final int CONSTRAINT_CONNECTIVITY = 268435456;
    public static final int CONSTRAINT_CONTENT_TRIGGER = 67108864;
    public static final int CONSTRAINT_DEADLINE = 1073741824;
    static final int CONSTRAINT_DEVICE_NOT_DOZING = 33554432;
    public static final int CONSTRAINT_FLEXIBLE = 2097152;
    public static final int CONSTRAINT_IDLE = 4;
    static final int CONSTRAINT_PREFETCH = 8388608;
    public static final int CONSTRAINT_STORAGE_NOT_LOW = 8;
    public static final int CONSTRAINT_TIMING_DELAY = Integer.MIN_VALUE;
    static final int CONSTRAINT_WITHIN_QUOTA = 16777216;
    static final boolean DEBUG_PREPARE = true;
    public static final long DEFAULT_TRIGGER_MAX_DELAY = 120000;
    public static final long DEFAULT_TRIGGER_UPDATE_DELAY = 10000;
    private static final int DYNAMIC_EXPEDITED_DEFERRAL_CONSTRAINTS = 37748736;
    private static final int DYNAMIC_RESTRICTED_CONSTRAINTS = 268435463;
    private static final int IMPLICIT_CONSTRAINTS = 56623104;
    public static final int INTERNAL_FLAG_DEMOTED_BY_SYSTEM_UIJ = 4;
    public static final int INTERNAL_FLAG_DEMOTED_BY_USER = 2;
    public static final int INTERNAL_FLAG_HAS_FOREGROUND_EXEMPTION = 1;
    private static final int MAX_NAMESPACE_CACHE_SIZE = 128;
    public static final long MIN_TRIGGER_MAX_DELAY = 1000;
    public static final long MIN_TRIGGER_UPDATE_DELAY = 500;
    public static final long NO_EARLIEST_RUNTIME = 0;
    public static final long NO_LATEST_RUNTIME = Long.MAX_VALUE;
    private static final int NUM_CONSTRAINT_CHANGE_HISTORY = 10;
    public static final int OVERRIDE_FULL = 3;
    public static final int OVERRIDE_NONE = 0;
    public static final int OVERRIDE_SOFT = 2;
    public static final int OVERRIDE_SORTING = 1;
    static final int SOFT_OVERRIDE_CONSTRAINTS = -2136997873;
    private static final int STATSD_CONSTRAINTS_TO_LOG = -981467136;
    private static final boolean STATS_LOG_ENABLED = false;
    private static final java.lang.String TAG = "JobScheduler.JobStatus";
    public static final int TRACKING_BATTERY = 1;
    public static final int TRACKING_CONNECTIVITY = 2;
    public static final int TRACKING_CONTENT = 4;
    public static final int TRACKING_FLEXIBILITY = 128;
    public static final int TRACKING_IDLE = 8;
    public static final int TRACKING_QUOTA = 64;
    public static final int TRACKING_STORAGE = 16;
    public static final int TRACKING_TIME = 32;
    private static java.security.MessageDigest sMessageDigest;
    public boolean appHasDozeExemption;
    final java.lang.String batteryName;
    final int callingUid;
    public android.util.ArraySet<java.lang.String> changedAuthorities;
    public android.util.ArraySet<android.net.Uri> changedUris;
    com.android.server.job.controllers.ContentObserverController.JobInstance contentObserverJobInstance;
    private final long earliestRunTimeElapsedMillis;
    public long enqueueTime;
    public java.util.ArrayList<android.app.job.JobWorkItem> executingWork;
    final android.app.job.JobInfo job;
    public int lastEvaluatedBias;
    private final long latestRunTimeElapsedMillis;
    private final boolean mCanApplyTransportAffinities;
    private int mConstraintChangeHistoryIndex;
    private final int[] mConstraintStatusHistory;
    private final long[] mConstraintUpdatedTimesElapsed;
    private long mCumulativeExecutionTimeMs;
    private int mDynamicConstraints;
    private boolean mExpeditedQuotaApproved;
    private java.lang.String[] mFilteredDebugTags;
    private java.lang.String mFilteredTraceTag;
    private long mFirstForceBatchedTimeElapsed;
    private final boolean mHasExemptedMediaUrisOnly;
    private boolean mHasMediaBackupExemption;
    private int mInternalFlags;
    private boolean mIsDowngradedDueToBuggyApp;
    final boolean mIsProxyJob;
    private boolean mIsUserBgRestricted;
    private com.android.server.job.JobSchedulerInternal mJobSchedulerInternal;
    private com.android.server.job.controllers.IJobStatusExt mJobStatusExt;
    private com.android.server.job.controllers.JobStatus.JobStatusWrapper mJobStatusWrapper;
    private long mLastFailedRunTime;
    private long mLastSuccessfulRunTime;
    private boolean mLoggedBucketMismatch;
    private final long mLoggingJobId;
    private long mMinimumNetworkChunkBytes;
    private final java.lang.String mNamespace;
    private final java.lang.String mNamespaceHash;
    private int mNumAppliedFlexibleConstraints;
    private int mNumDroppedFlexibleConstraints;
    private final int mNumSystemStops;
    private long mOriginalLatestRunTimeElapsedMillis;
    private android.util.Pair<java.lang.Long, java.lang.Long> mPersistedUtcTimes;
    private boolean mReadyDeadlineSatisfied;
    private boolean mReadyDynamicSatisfied;
    private boolean mReadyNotDozing;
    private boolean mReadyNotRestrictedInBg;
    private boolean mReadyWithinQuota;
    private int mReasonReadyToUnready;
    private final int mRequiredConstraintsOfInterest;
    private int mSatisfiedConstraintsOfInterest;
    private java.lang.String mSystemTraceTag;
    private long mTotalNetworkDownloadBytes;
    private long mTotalNetworkUploadBytes;
    private boolean mTransportAffinitiesSatisfied;
    private android.app.job.UserVisibleJobSummary mUserVisibleJobSummary;
    private java.lang.String mWakelockTag;
    public long madeActive;
    public long madePending;
    public android.net.Network network;
    public int nextPendingWorkId;
    private final int numFailures;
    public int overrideState;
    public java.util.ArrayList<android.app.job.JobWorkItem> pendingWork;
    private boolean prepared;
    final int requiredConstraints;
    int satisfiedConstraints;
    public java.lang.String serviceProcessName;
    final java.lang.String sourcePackageName;
    final java.lang.String sourceTag;
    final int sourceUid;
    final int sourceUserId;
    private int standbyBucket;
    public boolean startedAsExpeditedJob;
    public boolean startedAsUserInitiatedJob;
    public boolean startedWithForegroundFlag;
    public boolean startedWithImmediacyPrivilege;
    private int trackingControllers;
    public boolean uidActive;
    private java.lang.Throwable unpreparedPoint;
    private com.android.server.job.GrantedUriPermissions uriPerms;
    private long whenStandbyDeferred;
    static final boolean DEBUG = com.android.server.job.JobSchedulerService.DEBUG;
    private static final android.util.ArrayMap<java.lang.String, java.lang.String> sNamespaceHashCache = new android.util.ArrayMap<>();
    private static final android.net.Uri[] MEDIA_URIS_FOR_STANDBY_EXEMPTION = {android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI};
    private static final android.util.ArrayMap<java.util.regex.Pattern, java.lang.String> BASIC_PII_FILTERS = new android.util.ArrayMap<>();

    static {
        BASIC_PII_FILTERS.put(android.util.Patterns.EMAIL_ADDRESS, "[EMAIL]");
        BASIC_PII_FILTERS.put(android.util.Patterns.PHONE, "[PHONE]");
    }

    private JobStatus(android.app.job.JobInfo job, int callingUid, java.lang.String sourcePackageName, int sourceUserId, int standbyBucket, java.lang.String namespace, java.lang.String tag, int numFailures, int numSystemStops, long earliestRunTimeElapsedMillis, long latestRunTimeElapsedMillis, long lastSuccessfulRunTime, long lastFailedRunTime, long cumulativeExecutionTimeMs, int internalFlags, int dynamicConstraints) {
        android.app.job.JobInfo job2;
        java.lang.String str;
        boolean exemptedMediaUrisOnly;
        this.unpreparedPoint = null;
        this.satisfiedConstraints = 0;
        this.mSatisfiedConstraintsOfInterest = 0;
        this.mDynamicConstraints = 0;
        this.startedAsExpeditedJob = false;
        this.startedAsUserInitiatedJob = false;
        this.startedWithForegroundFlag = false;
        this.startedWithImmediacyPrivilege = false;
        this.nextPendingWorkId = 1;
        this.overrideState = 0;
        this.mConstraintChangeHistoryIndex = 0;
        this.mConstraintUpdatedTimesElapsed = new long[10];
        this.mConstraintStatusHistory = new int[10];
        this.mTotalNetworkDownloadBytes = -1L;
        this.mTotalNetworkUploadBytes = -1L;
        this.mMinimumNetworkChunkBytes = -1L;
        this.mReasonReadyToUnready = 0;
        this.mJobStatusWrapper = new com.android.server.job.controllers.JobStatus.JobStatusWrapper();
        this.mJobStatusExt = (com.android.server.job.controllers.IJobStatusExt) system.ext.loader.core.ExtLoader.type(com.android.server.job.controllers.IJobStatusExt.class).base(this).create();
        this.callingUid = callingUid;
        this.standbyBucket = standbyBucket;
        this.mNamespace = namespace;
        this.mNamespaceHash = generateNamespaceHash(namespace);
        this.mLoggingJobId = generateLoggingId(namespace, job.getId());
        int tempSourceUid = -1;
        if (sourceUserId != -1 && sourcePackageName != null) {
            try {
                tempSourceUid = android.app.AppGlobals.getPackageManager().getPackageUid(sourcePackageName, 0L, sourceUserId);
            } catch (android.os.RemoteException e) {
            }
        }
        if (tempSourceUid == -1) {
            this.sourceUid = callingUid;
            this.sourceUserId = android.os.UserHandle.getUserId(callingUid);
            this.sourcePackageName = job.getService().getPackageName();
            this.sourceTag = null;
            this.mJobStatusExt.setSyncJobAbnormal(job);
        } else {
            this.sourceUid = tempSourceUid;
            this.sourceUserId = sourceUserId;
            this.sourcePackageName = sourcePackageName;
            this.sourceTag = tag;
        }
        if (job.getRequiredNetwork() == null) {
            job2 = job;
        } else {
            android.app.job.JobInfo.Builder builder = new android.app.job.JobInfo.Builder(job);
            builder.setRequiredNetwork(new android.net.NetworkRequest.Builder(job.getRequiredNetwork()).setUids(java.util.Collections.singleton(new android.util.Range(java.lang.Integer.valueOf(this.sourceUid), java.lang.Integer.valueOf(this.sourceUid)))).build());
            job2 = builder.build(false, false, false, false);
        }
        this.job = job2;
        java.lang.String bnNamespace = namespace == null ? "" : "@" + namespace + "@";
        if (this.sourceTag != null) {
            str = bnNamespace + this.sourceTag + ":" + job2.getService().getPackageName();
        } else {
            str = bnNamespace + job2.getService().flattenToShortString();
        }
        this.batteryName = str;
        java.lang.String componentPackage = job2.getService().getPackageName();
        this.mIsProxyJob = !this.sourcePackageName.equals(componentPackage);
        this.earliestRunTimeElapsedMillis = earliestRunTimeElapsedMillis;
        this.latestRunTimeElapsedMillis = latestRunTimeElapsedMillis;
        this.mOriginalLatestRunTimeElapsedMillis = latestRunTimeElapsedMillis;
        this.numFailures = numFailures;
        this.mNumSystemStops = numSystemStops;
        int requiredConstraints = job2.getConstraintFlags();
        requiredConstraints = job2.getRequiredNetwork() != null ? requiredConstraints | 268435456 : requiredConstraints;
        requiredConstraints = earliestRunTimeElapsedMillis != 0 ? requiredConstraints | Integer.MIN_VALUE : requiredConstraints;
        requiredConstraints = latestRunTimeElapsedMillis != Long.MAX_VALUE ? requiredConstraints | 1073741824 : requiredConstraints;
        requiredConstraints = job2.isPrefetch() ? requiredConstraints | 8388608 : requiredConstraints;
        if (job2.getTriggerContentUris() == null) {
            exemptedMediaUrisOnly = false;
        } else {
            requiredConstraints |= 67108864;
            android.app.job.JobInfo.TriggerContentUri[] triggerContentUris = job2.getTriggerContentUris();
            int length = triggerContentUris.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    exemptedMediaUrisOnly = true;
                    break;
                }
                android.app.job.JobInfo.TriggerContentUri uri = triggerContentUris[i];
                android.app.job.JobInfo.TriggerContentUri[] triggerContentUriArr = triggerContentUris;
                int i2 = length;
                if (com.android.internal.util.ArrayUtils.contains(MEDIA_URIS_FOR_STANDBY_EXEMPTION, uri.getUri())) {
                    i++;
                    triggerContentUris = triggerContentUriArr;
                    length = i2;
                } else {
                    exemptedMediaUrisOnly = false;
                    break;
                }
            }
        }
        int requiredConstraints2 = requiredConstraints | this.mJobStatusExt.initRequiredConstraints(job2);
        this.mHasExemptedMediaUrisOnly = exemptedMediaUrisOnly;
        this.mCanApplyTransportAffinities = job2.getRequiredNetwork() != null && job2.getRequiredNetwork().getTransportTypes().length == 0;
        boolean lacksSomeFlexibleConstraints = ((~requiredConstraints2) & 7) != 0 || this.mCanApplyTransportAffinities;
        if (!isRequestedExpeditedJob() && !job2.isUserInitiated()) {
            if (numFailures + numSystemStops != 1 && lacksSomeFlexibleConstraints) {
                requiredConstraints2 |= 2097152;
            }
        }
        this.requiredConstraints = requiredConstraints2;
        this.mRequiredConstraintsOfInterest = CONSTRAINTS_OF_INTEREST & requiredConstraints2;
        addDynamicConstraints(dynamicConstraints);
        this.mReadyNotDozing = canRunInDoze();
        if (standbyBucket == 5) {
            addDynamicConstraints(DYNAMIC_RESTRICTED_CONSTRAINTS);
        } else {
            this.mReadyDynamicSatisfied = false;
        }
        this.mCumulativeExecutionTimeMs = cumulativeExecutionTimeMs;
        this.mLastSuccessfulRunTime = lastSuccessfulRunTime;
        this.mLastFailedRunTime = lastFailedRunTime;
        this.mInternalFlags = internalFlags;
        updateNetworkBytesLocked();
        updateMediaBackupExemptionStatus();
    }

    public JobStatus(com.android.server.job.controllers.JobStatus jobStatus) {
        this(jobStatus.getJob(), jobStatus.getUid(), jobStatus.getSourcePackageName(), jobStatus.getSourceUserId(), jobStatus.getStandbyBucket(), jobStatus.getNamespace(), jobStatus.getSourceTag(), jobStatus.getNumFailures(), jobStatus.getNumSystemStops(), jobStatus.getEarliestRunTime(), jobStatus.getLatestRunTimeElapsed(), jobStatus.getLastSuccessfulRunTime(), jobStatus.getLastFailedRunTime(), jobStatus.getCumulativeExecutionTimeMs(), jobStatus.getInternalFlags(), jobStatus.mDynamicConstraints);
        this.mPersistedUtcTimes = jobStatus.mPersistedUtcTimes;
        if (jobStatus.mPersistedUtcTimes != null && DEBUG) {
            android.util.Slog.i(TAG, "Cloning job with persisted run times", new java.lang.RuntimeException("here"));
        }
        if (jobStatus.executingWork != null && jobStatus.executingWork.size() > 0) {
            this.executingWork = new java.util.ArrayList<>(jobStatus.executingWork);
        }
        if (jobStatus.pendingWork != null && jobStatus.pendingWork.size() > 0) {
            this.pendingWork = new java.util.ArrayList<>(jobStatus.pendingWork);
        }
    }

    public JobStatus(android.app.job.JobInfo job, int callingUid, java.lang.String sourcePkgName, int sourceUserId, int standbyBucket, java.lang.String namespace, java.lang.String sourceTag, long earliestRunTimeElapsedMillis, long latestRunTimeElapsedMillis, long lastSuccessfulRunTime, long lastFailedRunTime, long cumulativeExecutionTimeMs, android.util.Pair<java.lang.Long, java.lang.Long> persistedExecutionTimesUTC, int innerFlags, int dynamicConstraints) {
        this(job, callingUid, sourcePkgName, sourceUserId, standbyBucket, namespace, sourceTag, 0, 0, earliestRunTimeElapsedMillis, latestRunTimeElapsedMillis, lastSuccessfulRunTime, lastFailedRunTime, cumulativeExecutionTimeMs, innerFlags, dynamicConstraints);
        this.mPersistedUtcTimes = persistedExecutionTimesUTC;
        if (persistedExecutionTimesUTC != null && DEBUG) {
            android.util.Slog.i(TAG, "+ restored job with RTC times because of bad boot clock");
        }
    }

    public JobStatus(com.android.server.job.controllers.JobStatus rescheduling, long newEarliestRuntimeElapsedMillis, long newLatestRuntimeElapsedMillis, int numFailures, int numSystemStops, long lastSuccessfulRunTime, long lastFailedRunTime, long cumulativeExecutionTimeMs) {
        this(rescheduling.job, rescheduling.getUid(), rescheduling.getSourcePackageName(), rescheduling.getSourceUserId(), rescheduling.getStandbyBucket(), rescheduling.getNamespace(), rescheduling.getSourceTag(), numFailures, numSystemStops, newEarliestRuntimeElapsedMillis, newLatestRuntimeElapsedMillis, lastSuccessfulRunTime, lastFailedRunTime, cumulativeExecutionTimeMs, rescheduling.getInternalFlags(), rescheduling.mDynamicConstraints);
    }

    public static com.android.server.job.controllers.JobStatus createFromJobInfo(android.app.job.JobInfo job, int callingUid, java.lang.String sourcePkg, int sourceUserId, java.lang.String namespace, java.lang.String tag) {
        long period;
        long latestRunTimeElapsedMillis;
        long elapsedNow = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (job.isPeriodic()) {
            long period2 = java.lang.Math.max(android.app.job.JobInfo.getMinPeriodMillis(), java.lang.Math.min(31536000000L, job.getIntervalMillis()));
            latestRunTimeElapsedMillis = elapsedNow + period2;
            period = latestRunTimeElapsedMillis - java.lang.Math.max(android.app.job.JobInfo.getMinFlexMillis(), java.lang.Math.min(period2, job.getFlexMillis()));
        } else {
            period = job.hasEarlyConstraint() ? job.getMinLatencyMillis() + elapsedNow : 0L;
            latestRunTimeElapsedMillis = job.hasLateConstraint() ? job.getMaxExecutionDelayMillis() + elapsedNow : Long.MAX_VALUE;
        }
        java.lang.String jobPackage = sourcePkg != null ? sourcePkg : job.getService().getPackageName();
        int standbyBucket = com.android.server.job.JobSchedulerService.standbyBucketForPackage(jobPackage, sourceUserId, elapsedNow);
        return new com.android.server.job.controllers.JobStatus(job, callingUid, sourcePkg, sourceUserId, standbyBucket, namespace, tag, 0, 0, period, latestRunTimeElapsedMillis, 0L, 0L, 0L, 0, 0);
    }

    private long generateLoggingId(java.lang.String namespace, int jobId) {
        if (namespace == null) {
            return jobId;
        }
        return (((long) namespace.hashCode()) << 31) | ((long) jobId);
    }

    private static java.lang.String generateNamespaceHash(java.lang.String namespace) {
        if (namespace == null) {
            return null;
        }
        if (namespace.trim().isEmpty()) {
            return namespace;
        }
        synchronized (sNamespaceHashCache) {
            int idx = sNamespaceHashCache.indexOfKey(namespace);
            if (idx >= 0) {
                return sNamespaceHashCache.valueAt(idx);
            }
            java.lang.String hash = null;
            try {
                if (sMessageDigest == null) {
                    sMessageDigest = java.security.MessageDigest.getInstance("SHA-256");
                }
                byte[] digest = sMessageDigest.digest(namespace.getBytes());
                java.lang.StringBuilder hexBuilder = new java.lang.StringBuilder(digest.length);
                for (byte byteChar : digest) {
                    hexBuilder.append(java.lang.String.format("%02X", java.lang.Byte.valueOf(byteChar)));
                }
                hash = hexBuilder.toString();
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(TAG, "Couldn't hash input", e);
            }
            if (hash == null) {
                return "failed_namespace_hash";
            }
            java.lang.String hash2 = hash.intern();
            synchronized (sNamespaceHashCache) {
                if (sNamespaceHashCache.size() >= 128) {
                    sNamespaceHashCache.removeAt(new java.util.Random().nextInt(128));
                }
                sNamespaceHashCache.put(namespace, hash2);
            }
            return hash2;
        }
    }

    public void enqueueWorkLocked(android.app.job.JobWorkItem work) {
        if (this.pendingWork == null) {
            this.pendingWork = new java.util.ArrayList<>();
        }
        work.setWorkId(this.nextPendingWorkId);
        this.nextPendingWorkId++;
        if (work.getIntent() != null && com.android.server.job.GrantedUriPermissions.checkGrantFlags(work.getIntent().getFlags())) {
            work.setGrants(com.android.server.job.GrantedUriPermissions.createFromIntent(work.getIntent(), this.sourceUid, this.sourcePackageName, this.sourceUserId, toShortString()));
        }
        this.pendingWork.add(work);
        updateNetworkBytesLocked();
    }

    public android.app.job.JobWorkItem dequeueWorkLocked() {
        if (this.pendingWork != null && this.pendingWork.size() > 0) {
            android.app.job.JobWorkItem work = this.pendingWork.remove(0);
            if (work != null) {
                if (this.executingWork == null) {
                    this.executingWork = new java.util.ArrayList<>();
                }
                this.executingWork.add(work);
                work.bumpDeliveryCount();
            }
            return work;
        }
        return null;
    }

    public int getWorkCount() {
        int pendingCount = this.pendingWork == null ? 0 : this.pendingWork.size();
        int executingCount = this.executingWork != null ? this.executingWork.size() : 0;
        return pendingCount + executingCount;
    }

    public boolean hasWorkLocked() {
        return (this.pendingWork != null && this.pendingWork.size() > 0) || hasExecutingWorkLocked();
    }

    public boolean hasExecutingWorkLocked() {
        return this.executingWork != null && this.executingWork.size() > 0;
    }

    private static void ungrantWorkItem(android.app.job.JobWorkItem work) {
        if (work.getGrants() != null) {
            ((com.android.server.job.GrantedUriPermissions) work.getGrants()).revoke();
        }
    }

    public boolean completeWorkLocked(int workId) {
        if (this.executingWork != null) {
            int N = this.executingWork.size();
            for (int i = 0; i < N; i++) {
                android.app.job.JobWorkItem work = this.executingWork.get(i);
                if (work.getWorkId() == workId) {
                    this.executingWork.remove(i);
                    ungrantWorkItem(work);
                    updateNetworkBytesLocked();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static void ungrantWorkList(java.util.ArrayList<android.app.job.JobWorkItem> list) {
        if (list != null) {
            int N = list.size();
            for (int i = 0; i < N; i++) {
                ungrantWorkItem(list.get(i));
            }
        }
    }

    public void stopTrackingJobLocked(com.android.server.job.controllers.JobStatus incomingJob) {
        if (incomingJob != null) {
            if (this.executingWork != null && this.executingWork.size() > 0) {
                incomingJob.pendingWork = this.executingWork;
            }
            if (incomingJob.pendingWork == null) {
                incomingJob.pendingWork = this.pendingWork;
            } else if (this.pendingWork != null && this.pendingWork.size() > 0) {
                incomingJob.pendingWork.addAll(this.pendingWork);
            }
            this.pendingWork = null;
            this.executingWork = null;
            incomingJob.nextPendingWorkId = this.nextPendingWorkId;
            incomingJob.updateNetworkBytesLocked();
        } else {
            ungrantWorkList(this.pendingWork);
            this.pendingWork = null;
            ungrantWorkList(this.executingWork);
            this.executingWork = null;
        }
        updateNetworkBytesLocked();
    }

    public void prepareLocked() {
        if (this.prepared) {
            android.util.Slog.wtf(TAG, "Already prepared: " + this);
            return;
        }
        this.prepared = true;
        this.unpreparedPoint = null;
        android.content.ClipData clip = this.job.getClipData();
        if (clip != null) {
            this.uriPerms = com.android.server.job.GrantedUriPermissions.createFromClip(clip, this.sourceUid, this.sourcePackageName, this.sourceUserId, this.job.getClipGrantFlags(), toShortString());
        }
    }

    public void unprepareLocked() {
        if (!this.prepared) {
            android.util.Slog.wtf(TAG, "Hasn't been prepared: " + this);
            if (this.unpreparedPoint != null) {
                android.util.Slog.e(TAG, "Was already unprepared at ", this.unpreparedPoint);
                return;
            }
            return;
        }
        this.prepared = false;
        this.unpreparedPoint = new java.lang.Throwable().fillInStackTrace();
        if (this.uriPerms != null) {
            this.uriPerms.revoke();
            this.uriPerms = null;
        }
    }

    public boolean isPreparedLocked() {
        return this.prepared;
    }

    public android.app.job.JobInfo getJob() {
        return this.job;
    }

    public int getJobId() {
        return this.job.getId();
    }

    public long getLoggingJobId() {
        return this.mLoggingJobId;
    }

    public java.lang.String getAppTraceTag() {
        return this.job.getTraceTag();
    }

    public java.lang.String computeSystemTraceTag() {
        if (this.mSystemTraceTag != null) {
            return this.mSystemTraceTag;
        }
        this.mSystemTraceTag = computeSystemTraceTagInner();
        return this.mSystemTraceTag;
    }

    private java.lang.String computeSystemTraceTagInner() {
        java.lang.String componentPackage = getServiceComponent().getPackageName();
        java.lang.StringBuilder traceTag = new java.lang.StringBuilder(128);
        traceTag.append("*job*<").append(this.sourceUid).append(">").append(this.sourcePackageName);
        if (!this.sourcePackageName.equals(componentPackage)) {
            traceTag.append(":").append(componentPackage);
        }
        traceTag.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(getServiceComponent().getShortClassName());
        if (!componentPackage.equals(this.serviceProcessName)) {
            traceTag.append("$").append(this.serviceProcessName);
        }
        if (this.mNamespace != null && !this.mNamespace.trim().isEmpty()) {
            traceTag.append("@").append(this.mNamespace);
        }
        traceTag.append("#").append(getJobId());
        return traceTag.toString();
    }

    public boolean isProxyJob() {
        return this.mIsProxyJob;
    }

    public void printUniqueId(java.io.PrintWriter pw) {
        if (this.mNamespace != null) {
            pw.print(this.mNamespace);
            pw.print(":");
        } else {
            pw.print("#");
        }
        android.os.UserHandle.formatUid(pw, this.callingUid);
        pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
        pw.print(this.job.getId());
    }

    public int getNumFailures() {
        return this.numFailures;
    }

    public int getNumSystemStops() {
        return this.mNumSystemStops;
    }

    public int getNumPreviousAttempts() {
        return this.numFailures + this.mNumSystemStops;
    }

    public android.content.ComponentName getServiceComponent() {
        return this.job.getService();
    }

    public java.lang.String getCallingPackageName() {
        return this.job.getService().getPackageName();
    }

    public java.lang.String getSourcePackageName() {
        return this.sourcePackageName;
    }

    public int getSourceUid() {
        return this.sourceUid;
    }

    public int getSourceUserId() {
        return this.sourceUserId;
    }

    public int getUserId() {
        return android.os.UserHandle.getUserId(this.callingUid);
    }

    private boolean shouldBlameSourceForTimeout() {
        return android.os.UserHandle.isCore(this.callingUid);
    }

    public java.lang.String getTimeoutBlamePackageName() {
        if (shouldBlameSourceForTimeout()) {
            return this.sourcePackageName;
        }
        return getServiceComponent().getPackageName();
    }

    public int getTimeoutBlameUid() {
        if (shouldBlameSourceForTimeout()) {
            return this.sourceUid;
        }
        return this.callingUid;
    }

    public int getTimeoutBlameUserId() {
        if (shouldBlameSourceForTimeout()) {
            return this.sourceUserId;
        }
        return android.os.UserHandle.getUserId(this.callingUid);
    }

    public int getEffectiveStandbyBucket() {
        int bucketWithBackupExemption;
        java.lang.String pkg;
        if (this.mJobSchedulerInternal == null) {
            this.mJobSchedulerInternal = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
        }
        boolean isBuggy = this.mJobSchedulerInternal.isAppConsideredBuggy(getUserId(), getServiceComponent().getPackageName(), getTimeoutBlameUserId(), getTimeoutBlamePackageName());
        int actualBucket = getStandbyBucket();
        if (actualBucket == 6) {
            if (isBuggy && DEBUG) {
                if (getServiceComponent().getPackageName().equals(this.sourcePackageName)) {
                    pkg = this.sourcePackageName;
                } else {
                    pkg = getServiceComponent().getPackageName() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.sourcePackageName;
                }
                android.util.Slog.w(TAG, "Exempted app " + pkg + " considered buggy");
            }
            return actualBucket;
        }
        if (this.uidActive || getJob().isExemptedFromAppStandby()) {
            return 0;
        }
        if (actualBucket != 5 && actualBucket != 4 && this.mHasMediaBackupExemption) {
            bucketWithBackupExemption = java.lang.Math.min(1, actualBucket);
        } else {
            bucketWithBackupExemption = actualBucket;
        }
        if (isBuggy && bucketWithBackupExemption < 1) {
            if (!this.mIsDowngradedDueToBuggyApp) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_job_quota_reduced_due_to_buggy_uid", getTimeoutBlameUid());
                this.mIsDowngradedDueToBuggyApp = true;
            }
            return 1;
        }
        return bucketWithBackupExemption;
    }

    public int getStandbyBucket() {
        return this.standbyBucket;
    }

    public void setStandbyBucket(int newBucket) {
        if (newBucket == 5) {
            addDynamicConstraints(DYNAMIC_RESTRICTED_CONSTRAINTS);
        } else if (this.standbyBucket == 5) {
            removeDynamicConstraints(DYNAMIC_RESTRICTED_CONSTRAINTS);
        }
        this.standbyBucket = newBucket;
        this.mLoggedBucketMismatch = false;
    }

    public void maybeLogBucketMismatch() {
        if (!this.mLoggedBucketMismatch) {
            android.util.Slog.wtf(TAG, "App " + getSourcePackageName() + " became active but still in NEVER bucket");
            this.mLoggedBucketMismatch = true;
        }
    }

    public long getWhenStandbyDeferred() {
        return this.whenStandbyDeferred;
    }

    public void setWhenStandbyDeferred(long now) {
        this.whenStandbyDeferred = now;
    }

    public long getFirstForceBatchedTimeElapsed() {
        return this.mFirstForceBatchedTimeElapsed;
    }

    public void setFirstForceBatchedTimeElapsed(long now) {
        this.mFirstForceBatchedTimeElapsed = now;
    }

    public boolean updateMediaBackupExemptionStatus() {
        if (this.mJobSchedulerInternal == null) {
            this.mJobSchedulerInternal = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
        }
        boolean hasMediaExemption = this.mHasExemptedMediaUrisOnly && !this.job.hasLateConstraint() && this.job.getRequiredNetwork() != null && getEffectivePriority() >= 300 && this.sourcePackageName.equals(this.mJobSchedulerInternal.getCloudMediaProviderPackage(this.sourceUserId));
        if (this.mHasMediaBackupExemption == hasMediaExemption) {
            return false;
        }
        this.mHasMediaBackupExemption = hasMediaExemption;
        return true;
    }

    public java.lang.String getNamespace() {
        return this.mNamespace;
    }

    public java.lang.String getNamespaceHash() {
        return this.mNamespaceHash;
    }

    public java.lang.String getSourceTag() {
        return this.sourceTag;
    }

    public int getUid() {
        return this.callingUid;
    }

    public java.lang.String getBatteryName() {
        return this.batteryName;
    }

    static java.lang.String applyBasicPiiFilters(java.lang.String val) {
        for (int i = BASIC_PII_FILTERS.size() - 1; i >= 0; i--) {
            val = BASIC_PII_FILTERS.keyAt(i).matcher(val).replaceAll(BASIC_PII_FILTERS.valueAt(i));
        }
        return val;
    }

    public java.lang.String[] getFilteredDebugTags() {
        if (this.mFilteredDebugTags != null) {
            return this.mFilteredDebugTags;
        }
        android.util.ArraySet<java.lang.String> debugTags = this.job.getDebugTagsArraySet();
        this.mFilteredDebugTags = new java.lang.String[debugTags.size()];
        for (int i = 0; i < this.mFilteredDebugTags.length; i++) {
            this.mFilteredDebugTags[i] = applyBasicPiiFilters(debugTags.valueAt(i));
        }
        return this.mFilteredDebugTags;
    }

    public java.lang.String getFilteredTraceTag() {
        if (this.mFilteredTraceTag != null) {
            return this.mFilteredTraceTag;
        }
        java.lang.String rawTag = this.job.getTraceTag();
        if (rawTag == null) {
            return null;
        }
        this.mFilteredTraceTag = applyBasicPiiFilters(rawTag);
        return this.mFilteredTraceTag;
    }

    public java.lang.String getWakelockTag() {
        if (this.mWakelockTag == null) {
            this.mWakelockTag = "*job*/" + this.batteryName;
        }
        return this.mWakelockTag;
    }

    public int getBias() {
        return this.job.getBias();
    }

    public int getEffectivePriority() {
        int maxPriority;
        boolean isDemoted = (getInternalFlags() & 2) != 0 || (this.job.isUserInitiated() && (getInternalFlags() & 4) != 0);
        if (isDemoted) {
            maxPriority = 400;
        } else {
            maxPriority = 500;
        }
        int rawPriority = java.lang.Math.min(maxPriority, this.job.getPriority());
        if (this.numFailures < 2 || shouldTreatAsUserInitiatedJob()) {
            return rawPriority;
        }
        if (isRequestedExpeditedJob()) {
            return 400;
        }
        int dropPower = this.numFailures / 2;
        switch (dropPower) {
        }
        return rawPriority;
    }

    public int getFlags() {
        return this.job.getFlags();
    }

    public int getInternalFlags() {
        return this.mInternalFlags;
    }

    public void addInternalFlags(int flags) {
        this.mInternalFlags |= flags;
    }

    public void removeInternalFlags(int flags) {
        this.mInternalFlags &= ~flags;
    }

    public int getSatisfiedConstraintFlags() {
        return this.satisfiedConstraints;
    }

    public void maybeAddForegroundExemption(java.util.function.Predicate<java.lang.Integer> uidForegroundChecker) {
        if (!this.job.hasEarlyConstraint() && !this.job.hasLateConstraint() && (this.mInternalFlags & 1) == 0 && uidForegroundChecker.test(java.lang.Integer.valueOf(getSourceUid()))) {
            addInternalFlags(1);
        }
    }

    private void updateNetworkBytesLocked() {
        this.mTotalNetworkDownloadBytes = this.job.getEstimatedNetworkDownloadBytes();
        if (this.mTotalNetworkDownloadBytes < 0) {
            this.mTotalNetworkDownloadBytes = -1L;
        }
        this.mTotalNetworkUploadBytes = this.job.getEstimatedNetworkUploadBytes();
        if (this.mTotalNetworkUploadBytes < 0) {
            this.mTotalNetworkUploadBytes = -1L;
        }
        this.mMinimumNetworkChunkBytes = this.job.getMinimumNetworkChunkBytes();
        if (this.pendingWork != null) {
            for (int i = 0; i < this.pendingWork.size(); i++) {
                long downloadBytes = this.pendingWork.get(i).getEstimatedNetworkDownloadBytes();
                if (downloadBytes != -1 && downloadBytes > 0) {
                    if (this.mTotalNetworkDownloadBytes != -1) {
                        this.mTotalNetworkDownloadBytes += downloadBytes;
                    } else {
                        this.mTotalNetworkDownloadBytes = downloadBytes;
                    }
                }
                long uploadBytes = this.pendingWork.get(i).getEstimatedNetworkUploadBytes();
                if (uploadBytes != -1 && uploadBytes > 0) {
                    if (this.mTotalNetworkUploadBytes != -1) {
                        this.mTotalNetworkUploadBytes += uploadBytes;
                    } else {
                        this.mTotalNetworkUploadBytes = uploadBytes;
                    }
                }
                long chunkBytes = this.pendingWork.get(i).getMinimumNetworkChunkBytes();
                if (this.mMinimumNetworkChunkBytes == -1) {
                    this.mMinimumNetworkChunkBytes = chunkBytes;
                } else if (chunkBytes != -1) {
                    this.mMinimumNetworkChunkBytes = java.lang.Math.min(this.mMinimumNetworkChunkBytes, chunkBytes);
                }
            }
        }
    }

    public long getEstimatedNetworkDownloadBytes() {
        return this.mTotalNetworkDownloadBytes;
    }

    public long getEstimatedNetworkUploadBytes() {
        return this.mTotalNetworkUploadBytes;
    }

    public long getMinimumNetworkChunkBytes() {
        return this.mMinimumNetworkChunkBytes;
    }

    public boolean hasConnectivityConstraint() {
        return (this.requiredConstraints & 268435456) != 0;
    }

    public boolean hasChargingConstraint() {
        return hasConstraint(1);
    }

    public boolean hasBatteryNotLowConstraint() {
        return hasConstraint(2);
    }

    boolean hasPowerConstraint() {
        return hasConstraint(3);
    }

    public boolean hasStorageNotLowConstraint() {
        return hasConstraint(8);
    }

    public boolean hasTimingDelayConstraint() {
        return hasConstraint(Integer.MIN_VALUE);
    }

    public boolean hasDeadlineConstraint() {
        return hasConstraint(1073741824);
    }

    public boolean hasIdleConstraint() {
        return hasConstraint(4);
    }

    public boolean hasContentTriggerConstraint() {
        return (this.requiredConstraints & 67108864) != 0;
    }

    public boolean hasFlexibilityConstraint() {
        return (this.requiredConstraints & 2097152) != 0;
    }

    public int getNumAppliedFlexibleConstraints() {
        return this.mNumAppliedFlexibleConstraints;
    }

    public int getNumRequiredFlexibleConstraints() {
        return this.mNumAppliedFlexibleConstraints - this.mNumDroppedFlexibleConstraints;
    }

    public int getNumDroppedFlexibleConstraints() {
        return this.mNumDroppedFlexibleConstraints;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasConstraint(int constraint) {
        return ((this.requiredConstraints & constraint) == 0 && (this.mDynamicConstraints & constraint) == 0) ? false : true;
    }

    public long getTriggerContentUpdateDelay() {
        long time = this.job.getTriggerContentUpdateDelay();
        if (time < 0) {
            return 10000L;
        }
        return java.lang.Math.max(time, 500L);
    }

    public long getTriggerContentMaxDelay() {
        long time = this.job.getTriggerContentMaxDelay();
        if (time < 0) {
            return 120000L;
        }
        return java.lang.Math.max(time, 1000L);
    }

    public boolean isPersisted() {
        return this.job.isPersisted();
    }

    public long getCumulativeExecutionTimeMs() {
        return this.mCumulativeExecutionTimeMs;
    }

    public void incrementCumulativeExecutionTime(long incrementMs) {
        this.mCumulativeExecutionTimeMs += incrementMs;
    }

    public long getEarliestRunTime() {
        return this.earliestRunTimeElapsedMillis;
    }

    public long getLatestRunTimeElapsed() {
        return this.latestRunTimeElapsedMillis;
    }

    public long getOriginalLatestRunTimeElapsed() {
        return this.mOriginalLatestRunTimeElapsedMillis;
    }

    public void setOriginalLatestRunTimeElapsed(long latestRunTimeElapsed) {
        this.mOriginalLatestRunTimeElapsedMillis = latestRunTimeElapsed;
    }

    boolean areTransportAffinitiesSatisfied() {
        return this.mTransportAffinitiesSatisfied;
    }

    void setTransportAffinitiesSatisfied(boolean isSatisfied) {
        this.mTransportAffinitiesSatisfied = isSatisfied;
    }

    public boolean canApplyTransportAffinities() {
        return this.mCanApplyTransportAffinities;
    }

    public int getStopReason() {
        return this.mReasonReadyToUnready;
    }

    public float getFractionRunTime() {
        long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (this.earliestRunTimeElapsedMillis == 0 && this.latestRunTimeElapsedMillis == Long.MAX_VALUE) {
            return 1.0f;
        }
        if (this.earliestRunTimeElapsedMillis == 0) {
            return now >= this.latestRunTimeElapsedMillis ? 1.0f : 0.0f;
        }
        if (this.latestRunTimeElapsedMillis == Long.MAX_VALUE) {
            return now >= this.earliestRunTimeElapsedMillis ? 1.0f : 0.0f;
        }
        if (now <= this.earliestRunTimeElapsedMillis) {
            return 0.0f;
        }
        if (now >= this.latestRunTimeElapsedMillis) {
            return 1.0f;
        }
        return (now - this.earliestRunTimeElapsedMillis) / (this.latestRunTimeElapsedMillis - this.earliestRunTimeElapsedMillis);
    }

    public android.util.Pair<java.lang.Long, java.lang.Long> getPersistedUtcTimes() {
        return this.mPersistedUtcTimes;
    }

    public void clearPersistedUtcTimes() {
        this.mPersistedUtcTimes = null;
    }

    public boolean isRequestedExpeditedJob() {
        return (getFlags() & 16) != 0;
    }

    public boolean shouldTreatAsExpeditedJob() {
        return this.mExpeditedQuotaApproved && isRequestedExpeditedJob();
    }

    public boolean shouldTreatAsUserInitiatedJob() {
        return getJob().isUserInitiated() && (getInternalFlags() & 2) == 0 && (getInternalFlags() & 4) == 0;
    }

    public android.app.job.UserVisibleJobSummary getUserVisibleJobSummary() {
        if (this.mUserVisibleJobSummary == null) {
            this.mUserVisibleJobSummary = new android.app.job.UserVisibleJobSummary(this.callingUid, getServiceComponent().getPackageName(), getSourceUserId(), getSourcePackageName(), getNamespace(), getJobId());
        }
        return this.mUserVisibleJobSummary;
    }

    public boolean isUserVisibleJob() {
        return shouldTreatAsUserInitiatedJob() || this.startedAsUserInitiatedJob;
    }

    public boolean canRunInDoze() {
        if (this.appHasDozeExemption || (getFlags() & 1) != 0 || shouldTreatAsUserInitiatedJob()) {
            return true;
        }
        return (shouldTreatAsExpeditedJob() || this.startedAsExpeditedJob) && (this.mDynamicConstraints & 33554432) == 0;
    }

    boolean canRunInBatterySaver() {
        if ((getInternalFlags() & 1) != 0 || shouldTreatAsUserInitiatedJob()) {
            return true;
        }
        return (shouldTreatAsExpeditedJob() || this.startedAsExpeditedJob) && (this.mDynamicConstraints & 4194304) == 0;
    }

    public boolean isUserBgRestricted() {
        return this.mIsUserBgRestricted;
    }

    boolean setChargingConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(1, nowElapsed, state);
    }

    boolean setBatteryNotLowConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(2, nowElapsed, state);
    }

    boolean setStorageNotLowConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(8, nowElapsed, state);
    }

    boolean setPrefetchConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(8388608, nowElapsed, state);
    }

    boolean setTimingDelayConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(Integer.MIN_VALUE, nowElapsed, state);
    }

    boolean setDeadlineConstraintSatisfied(long nowElapsed, boolean state) {
        boolean z = false;
        if (!setConstraintSatisfied(1073741824, nowElapsed, state)) {
            return false;
        }
        if (!this.job.isPeriodic() && hasDeadlineConstraint() && state) {
            z = true;
        }
        this.mReadyDeadlineSatisfied = z;
        return true;
    }

    boolean setIdleConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(4, nowElapsed, state);
    }

    boolean setConnectivityConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(268435456, nowElapsed, state);
    }

    boolean setContentTriggerConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(67108864, nowElapsed, state);
    }

    boolean setDeviceNotDozingConstraintSatisfied(long nowElapsed, boolean state, boolean whitelisted) {
        this.appHasDozeExemption = whitelisted;
        if (!setConstraintSatisfied(33554432, nowElapsed, state)) {
            return false;
        }
        this.mReadyNotDozing = state || canRunInDoze();
        return true;
    }

    boolean setBackgroundNotRestrictedConstraintSatisfied(long nowElapsed, boolean state, boolean isUserBgRestricted) {
        this.mIsUserBgRestricted = isUserBgRestricted;
        if (setConstraintSatisfied(4194304, nowElapsed, state)) {
            this.mReadyNotRestrictedInBg = state;
            return true;
        }
        return false;
    }

    boolean setQuotaConstraintSatisfied(long nowElapsed, boolean state) {
        if (setConstraintSatisfied(16777216, nowElapsed, state)) {
            this.mReadyWithinQuota = state;
            return true;
        }
        return false;
    }

    boolean setFlexibilityConstraintSatisfied(long nowElapsed, boolean state) {
        return setConstraintSatisfied(2097152, nowElapsed, state);
    }

    boolean setExpeditedJobQuotaApproved(long nowElapsed, boolean state) {
        if (this.mExpeditedQuotaApproved == state) {
            return false;
        }
        boolean wasReady = !state && isReady();
        this.mExpeditedQuotaApproved = state;
        updateExpeditedDependencies();
        boolean isReady = isReady();
        if (wasReady && !isReady) {
            this.mReasonReadyToUnready = 10;
        } else if (!wasReady && isReady) {
            this.mReasonReadyToUnready = 0;
        }
        return true;
    }

    private void updateExpeditedDependencies() {
        this.mReadyNotDozing = isConstraintSatisfied(33554432) || canRunInDoze();
    }

    boolean setUidActive(boolean newActiveState) {
        if (newActiveState != this.uidActive) {
            this.uidActive = newActiveState;
            return true;
        }
        return false;
    }

    boolean setConstraintSatisfied(int constraint, long nowElapsed, boolean state) {
        boolean old = (this.satisfiedConstraints & constraint) != 0;
        if (old == state) {
            return false;
        }
        if (DEBUG) {
            android.util.Slog.v(TAG, "Constraint " + constraint + " is " + (!state ? "NOT " : "") + "satisfied for " + toShortString());
        }
        boolean wasReady = !state && isReady();
        this.satisfiedConstraints = (this.satisfiedConstraints & (~constraint)) | (state ? constraint : 0);
        this.mSatisfiedConstraintsOfInterest = this.satisfiedConstraints & CONSTRAINTS_OF_INTEREST;
        this.mReadyDynamicSatisfied = this.mDynamicConstraints != 0 && this.mDynamicConstraints == (this.satisfiedConstraints & this.mDynamicConstraints);
        this.mConstraintUpdatedTimesElapsed[this.mConstraintChangeHistoryIndex] = nowElapsed;
        this.mConstraintStatusHistory[this.mConstraintChangeHistoryIndex] = this.satisfiedConstraints;
        this.mConstraintChangeHistoryIndex = (this.mConstraintChangeHistoryIndex + 1) % 10;
        boolean isReady = readinessStatusWithConstraint(constraint, state);
        if (wasReady && !isReady) {
            this.mReasonReadyToUnready = constraintToStopReason(constraint);
        } else if (!wasReady && isReady) {
            this.mReasonReadyToUnready = 0;
        }
        return true;
    }

    private int constraintToStopReason(int constraint) {
        switch (constraint) {
            case 1:
                return (this.requiredConstraints & constraint) != 0 ? 6 : 12;
            case 2:
                return (this.requiredConstraints & constraint) != 0 ? 5 : 12;
            case 4:
                return (this.requiredConstraints & constraint) != 0 ? 8 : 12;
            case 8:
                return 9;
            case 2097152:
                return 0;
            case 4194304:
                if (!this.mIsUserBgRestricted) {
                    return 4;
                }
                return 11;
            case 8388608:
                return 15;
            case 16777216:
                return 10;
            case 33554432:
                return 4;
            case 268435456:
                return 7;
            default:
                android.util.Slog.wtf(TAG, "Unsupported constraint (" + constraint + ") --stop reason mapping");
                return 0;
        }
    }

    public int getPendingJobReason() {
        int unsatisfiedConstraints = (~this.satisfiedConstraints) & (this.requiredConstraints | this.mDynamicConstraints | IMPLICIT_CONSTRAINTS);
        if ((4194304 & unsatisfiedConstraints) != 0) {
            return this.mIsUserBgRestricted ? 3 : 12;
        }
        if ((unsatisfiedConstraints & 2) != 0) {
            return (this.requiredConstraints & 2) != 0 ? 4 : 2;
        }
        if ((unsatisfiedConstraints & 1) != 0) {
            return (this.requiredConstraints & 1) != 0 ? 5 : 2;
        }
        if ((268435456 & unsatisfiedConstraints) != 0) {
            return 6;
        }
        if ((67108864 & unsatisfiedConstraints) != 0) {
            return 7;
        }
        if ((33554432 & unsatisfiedConstraints) != 0) {
            return 12;
        }
        if ((2097152 & unsatisfiedConstraints) != 0) {
            return 13;
        }
        if ((unsatisfiedConstraints & 4) != 0) {
            return (this.requiredConstraints & 4) != 0 ? 8 : 2;
        }
        if ((8388608 & unsatisfiedConstraints) != 0) {
            return 10;
        }
        if ((unsatisfiedConstraints & 8) != 0) {
            return 11;
        }
        if ((Integer.MIN_VALUE & unsatisfiedConstraints) != 0) {
            return 9;
        }
        if ((16777216 & unsatisfiedConstraints) != 0) {
            return 14;
        }
        if (getEffectiveStandbyBucket() == 4) {
            android.util.Slog.wtf(TAG, "App in NEVER bucket querying pending job reason");
            return 15;
        }
        if (this.serviceProcessName != null) {
            return 1;
        }
        if (!isReady()) {
            android.util.Slog.wtf(TAG, "Unknown reason job isn't ready");
            return 0;
        }
        return 0;
    }

    public boolean isConstraintSatisfied(int constraint) {
        return (this.satisfiedConstraints & constraint) != 0;
    }

    boolean isExpeditedQuotaApproved() {
        return this.mExpeditedQuotaApproved;
    }

    boolean clearTrackingController(int which) {
        if ((this.trackingControllers & which) != 0) {
            this.trackingControllers &= ~which;
            return true;
        }
        return false;
    }

    void setTrackingController(int which) {
        this.trackingControllers |= which;
    }

    public void setNumAppliedFlexibleConstraints(int count) {
        this.mNumAppliedFlexibleConstraints = count;
    }

    public void setNumDroppedFlexibleConstraints(int count) {
        this.mNumDroppedFlexibleConstraints = java.lang.Math.max(0, java.lang.Math.min(this.mNumAppliedFlexibleConstraints, count));
    }

    public void disallowRunInBatterySaverAndDoze() {
        addDynamicConstraints(DYNAMIC_EXPEDITED_DEFERRAL_CONSTRAINTS);
    }

    public void addDynamicConstraints(int constraints) {
        if ((16777216 & constraints) != 0) {
            android.util.Slog.wtf(TAG, "Tried to set quota as a dynamic constraint");
            constraints &= -16777217;
        }
        if (!hasConnectivityConstraint()) {
            constraints &= -268435457;
        }
        if (!hasContentTriggerConstraint()) {
            constraints &= -67108865;
        }
        this.mDynamicConstraints |= constraints;
        this.mReadyDynamicSatisfied = this.mDynamicConstraints != 0 && this.mDynamicConstraints == (this.satisfiedConstraints & this.mDynamicConstraints);
    }

    private void removeDynamicConstraints(int constraints) {
        this.mDynamicConstraints &= ~constraints;
        this.mReadyDynamicSatisfied = this.mDynamicConstraints != 0 && this.mDynamicConstraints == (this.satisfiedConstraints & this.mDynamicConstraints);
    }

    public long getLastSuccessfulRunTime() {
        return this.mLastSuccessfulRunTime;
    }

    public long getLastFailedRunTime() {
        return this.mLastFailedRunTime;
    }

    public boolean isReady() {
        return isReady(this.mSatisfiedConstraintsOfInterest);
    }

    public boolean wouldBeReadyWithConstraint(int constraint) {
        return readinessStatusWithConstraint(constraint, true);
    }

    boolean readinessStatusWithConstraint(int constraint, boolean value) {
        boolean oldValue = false;
        int satisfied = this.mSatisfiedConstraintsOfInterest;
        boolean z = true;
        switch (constraint) {
            case 4194304:
                oldValue = this.mReadyNotRestrictedInBg;
                this.mReadyNotRestrictedInBg = value;
                break;
            case 16777216:
                oldValue = this.mReadyWithinQuota;
                this.mReadyWithinQuota = value;
                break;
            case 33554432:
                oldValue = this.mReadyNotDozing;
                this.mReadyNotDozing = value;
                break;
            case 1073741824:
                oldValue = this.mReadyDeadlineSatisfied;
                this.mReadyDeadlineSatisfied = value;
                break;
            default:
                if (value) {
                    satisfied |= constraint;
                } else {
                    satisfied &= ~constraint;
                }
                this.mReadyDynamicSatisfied = this.mDynamicConstraints != 0 && this.mDynamicConstraints == (this.mDynamicConstraints & satisfied);
                break;
        }
        if (constraint != 2097152) {
            satisfied |= 2097152;
        }
        boolean toReturn = isReady(satisfied);
        switch (constraint) {
            case 4194304:
                this.mReadyNotRestrictedInBg = oldValue;
                return toReturn;
            case 16777216:
                this.mReadyWithinQuota = oldValue;
                return toReturn;
            case 33554432:
                this.mReadyNotDozing = oldValue;
                return toReturn;
            case 1073741824:
                this.mReadyDeadlineSatisfied = oldValue;
                return toReturn;
            default:
                if (this.mDynamicConstraints == 0 || this.mDynamicConstraints != (this.satisfiedConstraints & this.mDynamicConstraints)) {
                    z = false;
                }
                this.mReadyDynamicSatisfied = z;
                return toReturn;
        }
    }

    private boolean isReady(int satisfiedConstraints) {
        if ((this.mReadyWithinQuota || this.mReadyDynamicSatisfied || shouldTreatAsExpeditedJob()) && getEffectiveStandbyBucket() != 4 && this.mJobStatusExt.isReady(this.mReadyDeadlineSatisfied, this.mReadyNotDozing, this, this.requiredConstraints, satisfiedConstraints, 268435456, CONSTRAINTS_OF_INTEREST, SOFT_OVERRIDE_CONSTRAINTS) && this.mReadyNotDozing && this.mReadyNotRestrictedInBg && this.serviceProcessName != null) {
            return this.mReadyDeadlineSatisfied || isConstraintsSatisfied(satisfiedConstraints);
        }
        return false;
    }

    public boolean areDynamicConstraintsSatisfied() {
        return this.mReadyDynamicSatisfied;
    }

    public boolean isConstraintsSatisfied() {
        return isConstraintsSatisfied(this.mSatisfiedConstraintsOfInterest);
    }

    private boolean isConstraintsSatisfied(int satisfiedConstraints) {
        if (this.overrideState == 3) {
            return true;
        }
        int sat = satisfiedConstraints;
        if (this.overrideState == 2) {
            sat |= this.requiredConstraints & SOFT_OVERRIDE_CONSTRAINTS;
        }
        return (this.mRequiredConstraintsOfInterest & sat) == this.mRequiredConstraintsOfInterest;
    }

    public boolean matches(int uid, java.lang.String namespace, int jobId) {
        return this.job.getId() == jobId && this.callingUid == uid && java.util.Objects.equals(this.mNamespace, namespace);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("JobStatus{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        if (this.mNamespace != null) {
            sb.append(" ");
            sb.append(this.mNamespace);
            sb.append(":");
        } else {
            sb.append(" #");
        }
        android.os.UserHandle.formatUid(sb, this.callingUid);
        sb.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
        sb.append(this.job.getId());
        sb.append(' ');
        sb.append(this.batteryName);
        sb.append(" u=");
        sb.append(getUserId());
        sb.append(" s=");
        sb.append(getSourceUid());
        if (this.earliestRunTimeElapsedMillis != 0 || this.latestRunTimeElapsedMillis != Long.MAX_VALUE) {
            long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            sb.append(" TIME=");
            formatRunTime(sb, this.earliestRunTimeElapsedMillis, 0L, now);
            sb.append(":");
            formatRunTime(sb, this.latestRunTimeElapsedMillis, Long.MAX_VALUE, now);
        }
        if (this.job.getRequiredNetwork() != null) {
            sb.append(" NET");
        }
        if (this.job.isRequireCharging()) {
            sb.append(" CHARGING");
        }
        if (this.job.isRequireBatteryNotLow()) {
            sb.append(" BATNOTLOW");
        }
        if (this.job.isRequireStorageNotLow()) {
            sb.append(" STORENOTLOW");
        }
        if (this.job.isRequireDeviceIdle()) {
            sb.append(" IDLE");
        }
        if (this.job.isPeriodic()) {
            sb.append(" PERIODIC");
        }
        if (this.job.isPersisted()) {
            sb.append(" PERSISTED");
        }
        if ((this.satisfiedConstraints & 33554432) == 0) {
            sb.append(" WAIT:DEV_NOT_DOZING");
        }
        if (this.job.getTriggerContentUris() != null) {
            sb.append(" URIS=");
            sb.append(java.util.Arrays.toString(this.job.getTriggerContentUris()));
        }
        if (this.numFailures != 0) {
            sb.append(" failures=");
            sb.append(this.numFailures);
        }
        if (this.mNumSystemStops != 0) {
            sb.append(" system stops=");
            sb.append(this.mNumSystemStops);
        }
        if (isReady()) {
            sb.append(" READY");
        } else {
            sb.append(" satisfied:0x").append(java.lang.Integer.toHexString(this.satisfiedConstraints));
            int requiredConstraints = this.mRequiredConstraintsOfInterest | IMPLICIT_CONSTRAINTS;
            sb.append(" unsatisfied:0x").append(java.lang.Integer.toHexString((this.satisfiedConstraints & requiredConstraints) ^ requiredConstraints));
        }
        sb.append("}");
        return sb.toString();
    }

    private void formatRunTime(java.io.PrintWriter pw, long runtime, long defaultValue, long now) {
        if (runtime == defaultValue) {
            pw.print("none");
        } else {
            android.util.TimeUtils.formatDuration(runtime - now, pw);
        }
    }

    private void formatRunTime(java.lang.StringBuilder sb, long runtime, long defaultValue, long now) {
        if (runtime == defaultValue) {
            sb.append("none");
        } else {
            android.util.TimeUtils.formatDuration(runtime - now, sb);
        }
    }

    public java.lang.String toShortString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        if (this.mNamespace != null) {
            sb.append(" {").append(this.mNamespace).append("}");
        }
        sb.append(" #");
        android.os.UserHandle.formatUid(sb, this.callingUid);
        sb.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
        sb.append(this.job.getId());
        sb.append(' ');
        sb.append(this.batteryName);
        return sb.toString();
    }

    public java.lang.String toShortStringExceptUniqueId() {
        return java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + ' ' + this.batteryName;
    }

    public void writeToShortProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.callingUid);
        proto.write(1120986464258L, this.job.getId());
        proto.write(1138166333443L, this.batteryName);
        proto.end(token);
    }

    static void dumpConstraints(java.io.PrintWriter pw, int constraints) {
        if ((constraints & 1) != 0) {
            pw.print(" CHARGING");
        }
        if ((constraints & 2) != 0) {
            pw.print(" BATTERY_NOT_LOW");
        }
        if ((constraints & 8) != 0) {
            pw.print(" STORAGE_NOT_LOW");
        }
        if ((Integer.MIN_VALUE & constraints) != 0) {
            pw.print(" TIMING_DELAY");
        }
        if ((1073741824 & constraints) != 0) {
            pw.print(" DEADLINE");
        }
        if ((constraints & 4) != 0) {
            pw.print(" IDLE");
        }
        if ((268435456 & constraints) != 0) {
            pw.print(" CONNECTIVITY");
        }
        if ((2097152 & constraints) != 0) {
            pw.print(" FLEXIBILITY");
        }
        if ((67108864 & constraints) != 0) {
            pw.print(" CONTENT_TRIGGER");
        }
        if ((33554432 & constraints) != 0) {
            pw.print(" DEVICE_NOT_DOZING");
        }
        if ((4194304 & constraints) != 0) {
            pw.print(" BACKGROUND_NOT_RESTRICTED");
        }
        if ((8388608 & constraints) != 0) {
            pw.print(" PREFETCH");
        }
        if ((16777216 & constraints) != 0) {
            pw.print(" WITHIN_QUOTA");
        }
        com.android.server.job.controllers.IJobStatusExt.dumpConstraints(pw, constraints);
        if (constraints != 0) {
            pw.print(" [0x");
            pw.print(java.lang.Integer.toHexString(constraints));
            pw.print("]");
        }
    }

    static int getProtoConstraint(int constraint) {
        switch (constraint) {
            case Integer.MIN_VALUE:
                return 4;
            case 1:
                return 1;
            case 2:
                return 2;
            case 4:
                return 6;
            case 8:
                return 3;
            case 2097152:
                return 15;
            case 4194304:
                return 11;
            case 8388608:
                return 14;
            case 16777216:
                return 10;
            case 33554432:
                return 9;
            case 67108864:
                return 8;
            case 268435456:
                return 7;
            case 1073741824:
                return 5;
            default:
                return 0;
        }
    }

    void dumpConstraints(android.util.proto.ProtoOutputStream proto, long fieldId, int constraints) {
        if ((constraints & 1) != 0) {
            proto.write(fieldId, 1);
        }
        if ((constraints & 2) != 0) {
            proto.write(fieldId, 2);
        }
        if ((constraints & 8) != 0) {
            proto.write(fieldId, 3);
        }
        if ((Integer.MIN_VALUE & constraints) != 0) {
            proto.write(fieldId, 4);
        }
        if ((1073741824 & constraints) != 0) {
            proto.write(fieldId, 5);
        }
        if ((constraints & 4) != 0) {
            proto.write(fieldId, 6);
        }
        if ((268435456 & constraints) != 0) {
            proto.write(fieldId, 7);
        }
        if ((67108864 & constraints) != 0) {
            proto.write(fieldId, 8);
        }
        if ((33554432 & constraints) != 0) {
            proto.write(fieldId, 9);
        }
        if ((16777216 & constraints) != 0) {
            proto.write(fieldId, 10);
        }
        if ((4194304 & constraints) != 0) {
            proto.write(fieldId, 11);
        }
    }

    private void dumpJobWorkItem(android.util.IndentingPrintWriter pw, android.app.job.JobWorkItem work, int index) {
        pw.increaseIndent();
        pw.print("#");
        pw.print(index);
        pw.print(": #");
        pw.print(work.getWorkId());
        pw.print(" ");
        pw.print(work.getDeliveryCount());
        pw.print("x ");
        pw.println(work.getIntent());
        if (work.getGrants() != null) {
            pw.println("URI grants:");
            pw.increaseIndent();
            ((com.android.server.job.GrantedUriPermissions) work.getGrants()).dump(pw);
            pw.decreaseIndent();
        }
        pw.decreaseIndent();
    }

    private void dumpJobWorkItem(android.util.proto.ProtoOutputStream proto, long fieldId, android.app.job.JobWorkItem work) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, work.getWorkId());
        proto.write(1120986464258L, work.getDeliveryCount());
        if (work.getIntent() != null) {
            work.getIntent().dumpDebug(proto, 1146756268035L);
        }
        java.lang.Object grants = work.getGrants();
        if (grants != null) {
            ((com.android.server.job.GrantedUriPermissions) grants).dump(proto, 1146756268036L);
        }
        proto.end(token);
    }

    java.lang.String getBucketName() {
        return bucketName(this.standbyBucket);
    }

    static java.lang.String bucketName(int standbyBucket) {
        switch (standbyBucket) {
            case 0:
                return "ACTIVE";
            case 1:
                return "WORKING_SET";
            case 2:
                return "FREQUENT";
            case 3:
                return "RARE";
            case 4:
                return "NEVER";
            case 5:
                return "RESTRICTED";
            case 6:
                return "EXEMPTED";
            default:
                return "Unknown: " + standbyBucket;
        }
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dump(android.util.IndentingPrintWriter pw, boolean full, long nowElapsed) {
        android.os.UserHandle.formatUid(pw, this.callingUid);
        pw.print(" tag=");
        pw.println(getWakelockTag());
        pw.print("Source: uid=");
        android.os.UserHandle.formatUid(pw, getSourceUid());
        pw.print(" user=");
        pw.print(getSourceUserId());
        pw.print(" pkg=");
        pw.println(getSourcePackageName());
        pw.print("Namespace=");
        pw.println(getNamespace());
        pw.print("OsenseMode=");
        pw.println(this.mJobStatusExt.getOsenseRestrictMode());
        if (full) {
            pw.println("JobInfo:");
            pw.increaseIndent();
            pw.print("Service: ");
            pw.println(this.job.getService().flattenToShortString());
            if (this.job.isPeriodic()) {
                pw.print("PERIODIC: interval=");
                android.util.TimeUtils.formatDuration(this.job.getIntervalMillis(), pw);
                pw.print(" flex=");
                android.util.TimeUtils.formatDuration(this.job.getFlexMillis(), pw);
                pw.println();
            }
            if (this.job.isPersisted()) {
                pw.println("PERSISTED");
            }
            if (this.job.getBias() != 0) {
                pw.print("Bias: ");
                pw.println(android.app.job.JobInfo.getBiasString(this.job.getBias()));
            }
            pw.print("Priority: ");
            pw.print(android.app.job.JobInfo.getPriorityString(this.job.getPriority()));
            int effectivePriority = getEffectivePriority();
            if (effectivePriority != this.job.getPriority()) {
                pw.print(" effective=");
                pw.print(android.app.job.JobInfo.getPriorityString(effectivePriority));
            }
            pw.println();
            if (this.job.getFlags() != 0) {
                pw.print("Flags: ");
                pw.println(java.lang.Integer.toHexString(this.job.getFlags()));
            }
            if (getInternalFlags() != 0) {
                pw.print("Internal flags: ");
                pw.print(java.lang.Integer.toHexString(getInternalFlags()));
                if ((getInternalFlags() & 1) != 0) {
                    pw.print(" HAS_FOREGROUND_EXEMPTION");
                }
                pw.println();
            }
            pw.print("Requires: charging=");
            pw.print(this.job.isRequireCharging());
            pw.print(" batteryNotLow=");
            pw.print(this.job.isRequireBatteryNotLow());
            pw.print(" deviceIdle=");
            pw.println(this.job.isRequireDeviceIdle());
            if (this.job.getTriggerContentUris() != null) {
                pw.println("Trigger content URIs:");
                pw.increaseIndent();
                for (int i = 0; i < this.job.getTriggerContentUris().length; i++) {
                    android.app.job.JobInfo.TriggerContentUri trig = this.job.getTriggerContentUris()[i];
                    pw.print(java.lang.Integer.toHexString(trig.getFlags()));
                    pw.print(' ');
                    pw.println(trig.getUri());
                }
                pw.decreaseIndent();
                if (this.job.getTriggerContentUpdateDelay() >= 0) {
                    pw.print("Trigger update delay: ");
                    android.util.TimeUtils.formatDuration(this.job.getTriggerContentUpdateDelay(), pw);
                    pw.println();
                }
                if (this.job.getTriggerContentMaxDelay() >= 0) {
                    pw.print("Trigger max delay: ");
                    android.util.TimeUtils.formatDuration(this.job.getTriggerContentMaxDelay(), pw);
                    pw.println();
                }
                pw.print("Has media backup exemption", java.lang.Boolean.valueOf(this.mHasMediaBackupExemption)).println();
            }
            if (this.job.getExtras() != null && !this.job.getExtras().isDefinitelyEmpty()) {
                pw.print("Extras: ");
                pw.println(this.job.getExtras().toShortString());
            }
            if (this.job.getTransientExtras() != null && !this.job.getTransientExtras().isDefinitelyEmpty()) {
                pw.print("Transient extras: ");
                pw.println(this.job.getTransientExtras().toShortString());
            }
            if (this.job.getClipData() != null) {
                pw.print("Clip data: ");
                java.lang.StringBuilder b = new java.lang.StringBuilder(128);
                b.append(this.job.getClipData());
                pw.println(b);
            }
            if (this.uriPerms != null) {
                pw.println("Granted URI permissions:");
                this.uriPerms.dump(pw);
            }
            if (this.job.getRequiredNetwork() != null) {
                pw.print("Network type: ");
                pw.println(this.job.getRequiredNetwork());
            }
            if (this.mTotalNetworkDownloadBytes != -1) {
                pw.print("Network download bytes: ");
                pw.println(this.mTotalNetworkDownloadBytes);
            }
            if (this.mTotalNetworkUploadBytes != -1) {
                pw.print("Network upload bytes: ");
                pw.println(this.mTotalNetworkUploadBytes);
            }
            if (this.mMinimumNetworkChunkBytes != -1) {
                pw.print("Minimum network chunk bytes: ");
                pw.println(this.mMinimumNetworkChunkBytes);
            }
            if (this.job.getMinLatencyMillis() != 0) {
                pw.print("Minimum latency: ");
                android.util.TimeUtils.formatDuration(this.job.getMinLatencyMillis(), pw);
                pw.println();
            }
            if (this.job.getMaxExecutionDelayMillis() != 0) {
                pw.print("Max execution delay: ");
                android.util.TimeUtils.formatDuration(this.job.getMaxExecutionDelayMillis(), pw);
                pw.println();
            }
            pw.print("Backoff: policy=");
            pw.print(this.job.getBackoffPolicy());
            pw.print(" initial=");
            android.util.TimeUtils.formatDuration(this.job.getInitialBackoffMillis(), pw);
            pw.println();
            if (this.job.hasEarlyConstraint()) {
                pw.println("Has early constraint");
            }
            if (this.job.hasLateConstraint()) {
                pw.println("Has late constraint");
            }
            if (this.job.getTraceTag() != null) {
                pw.print("Trace tag: ");
                pw.println(this.job.getTraceTag());
            }
            if (this.job.getDebugTags().size() > 0) {
                pw.print("Debug tags: ");
                pw.println(this.job.getDebugTags());
            }
            pw.decreaseIndent();
        }
        pw.print("Required constraints:");
        dumpConstraints(pw, this.requiredConstraints);
        pw.println();
        pw.print("Dynamic constraints:");
        dumpConstraints(pw, this.mDynamicConstraints);
        pw.println();
        if (full) {
            pw.print("Satisfied constraints:");
            dumpConstraints(pw, this.satisfiedConstraints);
            pw.println();
            pw.print("Unsatisfied constraints:");
            dumpConstraints(pw, (this.requiredConstraints | 16777216) & (~this.satisfiedConstraints));
            pw.println();
            if (hasFlexibilityConstraint()) {
                pw.print("Num Required Flexible constraints: ");
                pw.print(getNumRequiredFlexibleConstraints());
                pw.println();
                pw.print("Num Dropped Flexible constraints: ");
                pw.print(getNumDroppedFlexibleConstraints());
                pw.println();
            }
            pw.println("Constraint history:");
            pw.increaseIndent();
            for (int h = 0; h < 10; h++) {
                int idx = (this.mConstraintChangeHistoryIndex + h) % 10;
                if (this.mConstraintUpdatedTimesElapsed[idx] != 0) {
                    android.util.TimeUtils.formatDuration(this.mConstraintUpdatedTimesElapsed[idx], nowElapsed, pw);
                    pw.print(" =");
                    dumpConstraints(pw, this.mConstraintStatusHistory[idx]);
                    pw.println();
                }
            }
            pw.decreaseIndent();
            if (this.appHasDozeExemption) {
                pw.println("Doze whitelisted: true");
            }
            if (this.uidActive) {
                pw.println("Uid: active");
            }
            if (this.job.isExemptedFromAppStandby()) {
                pw.println("Is exempted from app standby");
            }
        }
        if (this.trackingControllers != 0) {
            pw.print("Tracking:");
            if ((this.trackingControllers & 1) != 0) {
                pw.print(" BATTERY");
            }
            if ((this.trackingControllers & 2) != 0) {
                pw.print(" CONNECTIVITY");
            }
            if ((this.trackingControllers & 4) != 0) {
                pw.print(" CONTENT");
            }
            if ((this.trackingControllers & 8) != 0) {
                pw.print(" IDLE");
            }
            if ((this.trackingControllers & 16) != 0) {
                pw.print(" STORAGE");
            }
            if ((this.trackingControllers & 32) != 0) {
                pw.print(" TIME");
            }
            if ((this.trackingControllers & 64) != 0) {
                pw.print(" QUOTA");
            }
            pw.println();
        }
        pw.println("Implicit constraints:");
        pw.increaseIndent();
        pw.print("readyNotDozing: ");
        pw.println(this.mReadyNotDozing);
        pw.print("readyNotRestrictedInBg: ");
        pw.println(this.mReadyNotRestrictedInBg);
        if (!this.job.isPeriodic() && hasDeadlineConstraint()) {
            pw.print("readyDeadlineSatisfied: ");
            pw.println(this.mReadyDeadlineSatisfied);
        }
        if (this.mDynamicConstraints != 0) {
            pw.print("readyDynamicSatisfied: ");
            pw.println(this.mReadyDynamicSatisfied);
        }
        pw.print("readyComponentEnabled: ");
        pw.println(this.serviceProcessName != null);
        if ((getFlags() & 16) != 0) {
            pw.print("expeditedQuotaApproved: ");
            pw.print(this.mExpeditedQuotaApproved);
            pw.print(" (started as EJ: ");
            pw.print(this.startedAsExpeditedJob);
            pw.println(")");
        }
        if ((32 & getFlags()) != 0) {
            pw.print("userInitiatedApproved: ");
            pw.print(shouldTreatAsUserInitiatedJob());
            pw.print(" (started as UIJ: ");
            pw.print(this.startedAsUserInitiatedJob);
            pw.println(")");
        }
        pw.decreaseIndent();
        pw.print("Started with foreground flag: ");
        pw.println(this.startedWithForegroundFlag);
        if (this.mIsUserBgRestricted) {
            pw.println("User BG restricted");
        }
        if (this.changedAuthorities != null) {
            pw.println("Changed authorities:");
            pw.increaseIndent();
            for (int i2 = 0; i2 < this.changedAuthorities.size(); i2++) {
                pw.println(this.changedAuthorities.valueAt(i2));
            }
            pw.decreaseIndent();
        }
        if (this.changedUris != null) {
            pw.println("Changed URIs:");
            pw.increaseIndent();
            for (int i3 = 0; i3 < this.changedUris.size(); i3++) {
                pw.println(this.changedUris.valueAt(i3));
            }
            pw.decreaseIndent();
        }
        if (this.network != null) {
            pw.print("Network: ");
            pw.println(this.network);
        }
        if (this.pendingWork != null && this.pendingWork.size() > 0) {
            pw.println("Pending work:");
            for (int i4 = 0; i4 < this.pendingWork.size(); i4++) {
                dumpJobWorkItem(pw, this.pendingWork.get(i4), i4);
            }
        }
        if (this.executingWork != null && this.executingWork.size() > 0) {
            pw.println("Executing work:");
            for (int i5 = 0; i5 < this.executingWork.size(); i5++) {
                dumpJobWorkItem(pw, this.executingWork.get(i5), i5);
            }
        }
        pw.print("Standby bucket: ");
        pw.println(getBucketName());
        pw.increaseIndent();
        if (this.whenStandbyDeferred != 0) {
            pw.print("Deferred since: ");
            android.util.TimeUtils.formatDuration(this.whenStandbyDeferred, nowElapsed, pw);
            pw.println();
        }
        if (this.mFirstForceBatchedTimeElapsed != 0) {
            pw.print("Time since first force batch attempt: ");
            android.util.TimeUtils.formatDuration(this.mFirstForceBatchedTimeElapsed, nowElapsed, pw);
            pw.println();
        }
        pw.decreaseIndent();
        pw.print("Enqueue time: ");
        android.util.TimeUtils.formatDuration(this.enqueueTime, nowElapsed, pw);
        pw.println();
        pw.print("Run time: earliest=");
        formatRunTime((java.io.PrintWriter) pw, this.earliestRunTimeElapsedMillis, 0L, nowElapsed);
        pw.print(", latest=");
        formatRunTime((java.io.PrintWriter) pw, this.latestRunTimeElapsedMillis, Long.MAX_VALUE, nowElapsed);
        pw.print(", original latest=");
        formatRunTime((java.io.PrintWriter) pw, this.mOriginalLatestRunTimeElapsedMillis, Long.MAX_VALUE, nowElapsed);
        pw.println();
        if (this.mCumulativeExecutionTimeMs != 0) {
            pw.print("Cumulative execution time=");
            android.util.TimeUtils.formatDuration(this.mCumulativeExecutionTimeMs, pw);
            pw.println();
        }
        if (this.numFailures != 0) {
            pw.print("Num failures: ");
            pw.println(this.numFailures);
        }
        if (this.mNumSystemStops != 0) {
            pw.print("Num system stops: ");
            pw.println(this.mNumSystemStops);
        }
        if (this.mLastSuccessfulRunTime != 0) {
            pw.print("Last successful run: ");
            pw.println(formatTime(this.mLastSuccessfulRunTime));
        }
        if (this.mLastFailedRunTime != 0) {
            pw.print("Last failed run: ");
            pw.println(formatTime(this.mLastFailedRunTime));
        }
    }

    private static java.lang.CharSequence formatTime(long time) {
        return android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", time);
    }

    public void dump(android.util.proto.ProtoOutputStream proto, long fieldId, boolean full, long elapsedRealtimeMillis) {
        long j;
        long token = proto.start(fieldId);
        long j2 = 1120986464257L;
        proto.write(1120986464257L, this.callingUid);
        proto.write(1138166333442L, getWakelockTag());
        proto.write(1120986464259L, getSourceUid());
        proto.write(1120986464260L, getSourceUserId());
        proto.write(1138166333445L, getSourcePackageName());
        if (full) {
            long jiToken = proto.start(1146756268038L);
            this.job.getService().dumpDebug(proto, 1146756268033L);
            proto.write(1133871366146L, this.job.isPeriodic());
            proto.write(1112396529667L, this.job.getIntervalMillis());
            proto.write(1112396529668L, this.job.getFlexMillis());
            proto.write(1133871366149L, this.job.isPersisted());
            proto.write(1172526071814L, this.job.getBias());
            proto.write(1120986464263L, this.job.getFlags());
            proto.write(1112396529688L, getInternalFlags());
            proto.write(1133871366152L, this.job.isRequireCharging());
            proto.write(1133871366153L, this.job.isRequireBatteryNotLow());
            proto.write(1133871366154L, this.job.isRequireDeviceIdle());
            if (this.job.getTriggerContentUris() != null) {
                int i = 0;
                while (i < this.job.getTriggerContentUris().length) {
                    long tcuToken = proto.start(2246267895819L);
                    android.app.job.JobInfo.TriggerContentUri trig = this.job.getTriggerContentUris()[i];
                    proto.write(j2, trig.getFlags());
                    android.net.Uri u = trig.getUri();
                    if (u != null) {
                        proto.write(1138166333442L, u.toString());
                    }
                    proto.end(tcuToken);
                    i++;
                    j2 = 1120986464257L;
                }
                if (this.job.getTriggerContentUpdateDelay() >= 0) {
                    proto.write(1112396529676L, this.job.getTriggerContentUpdateDelay());
                }
                if (this.job.getTriggerContentMaxDelay() >= 0) {
                    proto.write(1112396529677L, this.job.getTriggerContentMaxDelay());
                }
            }
            if (this.job.getExtras() != null && !this.job.getExtras().isDefinitelyEmpty()) {
                this.job.getExtras().dumpDebug(proto, 1146756268046L);
            }
            if (this.job.getTransientExtras() != null && !this.job.getTransientExtras().isDefinitelyEmpty()) {
                this.job.getTransientExtras().dumpDebug(proto, 1146756268047L);
            }
            if (this.job.getClipData() != null) {
                this.job.getClipData().dumpDebug(proto, 1146756268048L);
            }
            if (this.uriPerms != null) {
                this.uriPerms.dump(proto, 1146756268049L);
            }
            if (this.mTotalNetworkDownloadBytes != -1) {
                proto.write(1112396529689L, this.mTotalNetworkDownloadBytes);
            }
            if (this.mTotalNetworkUploadBytes != -1) {
                proto.write(1112396529690L, this.mTotalNetworkUploadBytes);
            }
            proto.write(1112396529684L, this.job.getMinLatencyMillis());
            proto.write(1112396529685L, this.job.getMaxExecutionDelayMillis());
            long bpToken = proto.start(1146756268054L);
            proto.write(1159641169921L, this.job.getBackoffPolicy());
            proto.write(1112396529666L, this.job.getInitialBackoffMillis());
            proto.end(bpToken);
            proto.write(1133871366167L, this.job.hasEarlyConstraint());
            proto.write(1133871366168L, this.job.hasLateConstraint());
            proto.end(jiToken);
        }
        dumpConstraints(proto, 2259152797703L, this.requiredConstraints);
        dumpConstraints(proto, 2259152797727L, this.mDynamicConstraints);
        if (full) {
            dumpConstraints(proto, 2259152797704L, this.satisfiedConstraints);
            dumpConstraints(proto, 2259152797705L, (this.requiredConstraints | 16777216) & (~this.satisfiedConstraints));
            proto.write(1133871366154L, this.appHasDozeExemption);
            proto.write(1133871366170L, this.uidActive);
            proto.write(1133871366171L, this.job.isExemptedFromAppStandby());
        }
        if ((this.trackingControllers & 1) != 0) {
            proto.write(2259152797707L, 0);
        }
        if ((this.trackingControllers & 2) != 0) {
            proto.write(2259152797707L, 1);
        }
        if ((this.trackingControllers & 4) != 0) {
            proto.write(2259152797707L, 2);
        }
        if ((this.trackingControllers & 8) != 0) {
            proto.write(2259152797707L, 3);
        }
        if ((this.trackingControllers & 16) != 0) {
            proto.write(2259152797707L, 4);
        }
        if ((this.trackingControllers & 32) != 0) {
            proto.write(2259152797707L, 5);
        }
        if ((this.trackingControllers & 64) != 0) {
            proto.write(2259152797707L, 6);
        }
        long icToken = proto.start(1146756268057L);
        proto.write(1133871366145L, this.mReadyNotDozing);
        proto.write(1133871366146L, this.mReadyNotRestrictedInBg);
        proto.write(1133871366147L, this.mReadyDynamicSatisfied);
        proto.end(icToken);
        if (this.changedAuthorities != null) {
            for (int k = 0; k < this.changedAuthorities.size(); k++) {
                proto.write(2237677961228L, this.changedAuthorities.valueAt(k));
            }
        }
        if (this.changedUris != null) {
            for (int i2 = 0; i2 < this.changedUris.size(); i2++) {
                proto.write(2237677961229L, this.changedUris.valueAt(i2).toString());
            }
        }
        if (this.pendingWork != null) {
            for (int i3 = 0; i3 < this.pendingWork.size(); i3++) {
                dumpJobWorkItem(proto, 2246267895823L, this.pendingWork.get(i3));
            }
        }
        if (this.executingWork != null) {
            for (int i4 = 0; i4 < this.executingWork.size(); i4++) {
                dumpJobWorkItem(proto, 2246267895824L, this.executingWork.get(i4));
            }
        }
        proto.write(1159641169937L, this.standbyBucket);
        proto.write(1112396529682L, elapsedRealtimeMillis - this.enqueueTime);
        proto.write(1112396529692L, this.whenStandbyDeferred == 0 ? 0L : elapsedRealtimeMillis - this.whenStandbyDeferred);
        if (this.mFirstForceBatchedTimeElapsed != 0) {
            j = elapsedRealtimeMillis - this.mFirstForceBatchedTimeElapsed;
        } else {
            j = 0;
        }
        proto.write(1112396529693L, j);
        if (this.earliestRunTimeElapsedMillis == 0) {
            proto.write(1176821039123L, 0);
        } else {
            proto.write(1176821039123L, this.earliestRunTimeElapsedMillis - elapsedRealtimeMillis);
        }
        if (this.latestRunTimeElapsedMillis == Long.MAX_VALUE) {
            proto.write(1176821039124L, 0);
        } else {
            proto.write(1176821039124L, this.latestRunTimeElapsedMillis - elapsedRealtimeMillis);
        }
        proto.write(1116691496990L, this.mOriginalLatestRunTimeElapsedMillis);
        proto.write(1120986464277L, this.numFailures + this.mNumSystemStops);
        proto.write(1112396529686L, this.mLastSuccessfulRunTime);
        proto.write(1112396529687L, this.mLastFailedRunTime);
        proto.end(token);
    }

    public com.android.server.job.controllers.IJobStatusWrapper getWrapper() {
        return this.mJobStatusWrapper;
    }

    private class JobStatusWrapper implements com.android.server.job.controllers.IJobStatusWrapper {
        private JobStatusWrapper() {
        }

        @Override // com.android.server.job.controllers.IJobStatusWrapper
        public com.android.server.job.controllers.IJobStatusExt getExtImpl() {
            return com.android.server.job.controllers.JobStatus.this.mJobStatusExt;
        }

        @Override // com.android.server.job.controllers.IJobStatusWrapper
        public boolean hasConstraint(int constraint) {
            return com.android.server.job.controllers.JobStatus.this.hasConstraint(constraint);
        }
    }
}
