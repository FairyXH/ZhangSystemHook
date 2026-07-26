package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public class SyncJobService extends android.app.job.JobService {
    private static final java.lang.String TAG = "SyncManager";
    private static com.android.server.content.SyncJobService sInstance;
    private static final java.lang.Object sLock = new java.lang.Object();
    private static final android.util.SparseArray<android.app.job.JobParameters> sJobParamsMap = new android.util.SparseArray<>();
    private static final android.util.SparseBooleanArray sStartedSyncs = new android.util.SparseBooleanArray();
    private static final android.util.SparseLongArray sJobStartUptimes = new android.util.SparseLongArray();
    private static final com.android.server.content.SyncLogger sLogger = com.android.server.content.SyncLogger.getInstance();

    private void updateInstance() {
        synchronized (com.android.server.content.SyncJobService.class) {
            sInstance = this;
        }
    }

    private static com.android.server.content.SyncJobService getInstance() {
        com.android.server.content.SyncJobService syncJobService;
        synchronized (sLock) {
            if (sInstance == null) {
                android.util.Slog.wtf("SyncManager", "sInstance == null");
            }
            syncJobService = sInstance;
        }
        return syncJobService;
    }

    public static boolean isReady() {
        boolean z;
        synchronized (sLock) {
            z = sInstance != null;
        }
        return z;
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        updateInstance();
        sLogger.purgeOldLogs();
        com.android.server.content.SyncOperation op = com.android.server.content.SyncOperation.maybeCreateFromJobExtras(params.getExtras());
        if (op == null) {
            android.util.Slog.wtf("SyncManager", "Got invalid job " + params.getJobId());
            return false;
        }
        boolean readyToSync = com.android.server.content.SyncManager.readyToSync(op.target.userId);
        sLogger.log("onStartJob() jobid=", java.lang.Integer.valueOf(params.getJobId()), " op=", op, " readyToSync", java.lang.Boolean.valueOf(readyToSync));
        if (!readyToSync) {
            boolean wantsReschedule = !op.isPeriodic;
            jobFinished(params, wantsReschedule);
            return true;
        }
        boolean isLoggable = android.util.Log.isLoggable("SyncManager", 2);
        synchronized (sLock) {
            int jobId = params.getJobId();
            sJobParamsMap.put(jobId, params);
            sStartedSyncs.delete(jobId);
            sJobStartUptimes.put(jobId, android.os.SystemClock.uptimeMillis());
        }
        android.os.Message m = android.os.Message.obtain();
        m.what = 10;
        if (isLoggable) {
            android.util.Slog.v("SyncManager", "Got start job message " + op.target);
        }
        m.obj = op;
        com.android.server.content.SyncManager.sendMessage(m);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        int i;
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "onStopJob called " + params.getJobId() + ", reason: " + params.getInternalStopReasonCode());
        }
        com.android.server.content.SyncOperation op = com.android.server.content.SyncOperation.maybeCreateFromJobExtras(params.getExtras());
        if (op == null) {
            android.util.Slog.wtf("SyncManager", "Got invalid job " + params.getJobId());
            return false;
        }
        boolean readyToSync = com.android.server.content.SyncManager.readyToSync(op.target.userId);
        sLogger.log("onStopJob() ", sLogger.jobParametersToString(params), " readyToSync=", java.lang.Boolean.valueOf(readyToSync));
        synchronized (sLock) {
            int jobId = params.getJobId();
            sJobParamsMap.remove(jobId);
            long startUptime = sJobStartUptimes.get(jobId);
            long nowUptime = android.os.SystemClock.uptimeMillis();
            long runtime = nowUptime - startUptime;
            if (runtime > 60000 && readyToSync && !sStartedSyncs.get(jobId)) {
                wtf("Job " + jobId + " didn't start:  startUptime=" + startUptime + " nowUptime=" + nowUptime + " params=" + jobParametersToString(params));
            }
            sStartedSyncs.delete(jobId);
            sJobStartUptimes.delete(jobId);
        }
        android.os.Message m = android.os.Message.obtain();
        m.what = 11;
        m.obj = op;
        if (params.getInternalStopReasonCode() == 0) {
            i = 0;
        } else {
            i = 1;
        }
        m.arg1 = i;
        m.arg2 = params.getInternalStopReasonCode() != 3 ? 0 : 1;
        com.android.server.content.SyncManager.sendMessage(m);
        return false;
    }

    public static void callJobFinished(int jobId, boolean needsReschedule, java.lang.String why) {
        com.android.server.content.SyncJobService instance = getInstance();
        if (instance != null) {
            instance.callJobFinishedInner(jobId, needsReschedule, why);
        }
    }

    public void callJobFinishedInner(int jobId, boolean needsReschedule, java.lang.String why) {
        synchronized (sLock) {
            android.app.job.JobParameters params = sJobParamsMap.get(jobId);
            sLogger.log("callJobFinished()", " jobid=", java.lang.Integer.valueOf(jobId), " needsReschedule=", java.lang.Boolean.valueOf(needsReschedule), " ", sLogger.jobParametersToString(params), " why=", why);
            if (params != null) {
                jobFinished(params, needsReschedule);
                sJobParamsMap.remove(jobId);
            } else {
                android.util.Slog.e("SyncManager", "Job params not found for " + java.lang.String.valueOf(jobId));
            }
        }
    }

    public static void markSyncStarted(int jobId) {
        synchronized (sLock) {
            sStartedSyncs.put(jobId, true);
        }
    }

    public static java.lang.String jobParametersToString(android.app.job.JobParameters params) {
        if (params == null) {
            return "job:null";
        }
        return "job:#" + params.getJobId() + ":sr=[" + params.getInternalStopReasonCode() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + params.getDebugStopReason() + "]:" + com.android.server.content.SyncOperation.maybeCreateFromJobExtras(params.getExtras());
    }

    private static void wtf(java.lang.String message) {
        sLogger.log(message);
        android.util.Slog.wtf("SyncManager", message);
    }
}
