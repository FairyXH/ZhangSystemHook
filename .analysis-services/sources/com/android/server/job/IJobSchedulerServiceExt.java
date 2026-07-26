package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface IJobSchedulerServiceExt {
    default void onHookPreInit(com.android.server.job.JobSchedulerService service, android.os.Handler handler, android.content.Context context) {
    }

    default void onHookEndInit(com.android.server.job.JobSchedulerService service, java.util.List<com.android.server.job.controllers.StateController> controllers, java.util.List<com.android.server.job.restrictions.JobRestriction> restrictions) {
    }

    default void onHookSystemReady() {
    }

    default void onHookBootPhase(android.content.Context context, int phase) {
    }

    default void scheduleAsPackage(android.content.Context context, android.app.job.JobInfo job, int uId) {
    }

    default void onHookRedundantJob(android.content.Context context, com.android.server.job.JobStore jobs, int uId, int limit) {
    }

    default void jobQueueFunctorNotAccept(com.android.server.job.controllers.JobStatus job) {
    }

    default boolean ignoreJobRemoved(com.android.server.job.JobSchedulerService jss, java.lang.String pkgName, int pkgUid) {
        return false;
    }

    default void acceptForMaybeReadyJobQueueFunctor(com.android.server.job.controllers.JobStatus job) {
    }

    default boolean isReadyToBeExecuted(com.android.server.job.controllers.JobStatus job) {
        return true;
    }

    default boolean isComponentUsable(com.android.server.job.controllers.JobStatus job, boolean satisfied) {
        return satisfied;
    }

    default long translateDelayTime(android.app.job.JobInfo job, long delayMillis) {
        return delayMillis;
    }

    default void checkOplusPermission(android.app.job.JobInfo job, int callingPid, int callingUid) {
    }

    default int pendingJobs(int uid) {
        return 1;
    }

    default int restoreSpecialJobs(int uid) {
        return 2;
    }

    default boolean interceptScheduleJobLocked(com.android.server.job.controllers.JobStatus scheduleJob, android.app.job.JobWorkItem work) {
        return false;
    }

    default void dumpCacheJobs(android.util.IndentingPrintWriter pw) {
    }

    default boolean readyForPostProcess() {
        return false;
    }

    default void resetForMaybeReadyJobQueueFunctor() {
    }

    default boolean isRunningHighCpuJobs() {
        return false;
    }

    default void hookReceivePackageRemove(com.android.server.job.JobSchedulerService jss, int uidRemoved, java.lang.String pkgName) {
    }

    default void hookReceivePackageRestarted(com.android.server.job.JobSchedulerService jss, int uidRemoved, java.lang.String pkgName) {
    }

    default android.app.job.JobInfo getPendingJob(int uid, int jobId) {
        return null;
    }

    default java.util.List<android.app.job.JobInfo> getPendingJobs(int uid) {
        return null;
    }

    default boolean checkIdleJobNotUserStatus(com.android.server.job.controllers.JobStatus job) {
        return false;
    }

    default boolean isProxyJob(com.android.server.job.controllers.JobStatus job, java.lang.String methodName) {
        return false;
    }

    default void dumpProxyJob(android.util.IndentingPrintWriter pw) {
    }

    default void onRestrictionStateChanged(com.android.server.job.restrictions.JobRestriction restriction) {
    }

    default void onControllerStateChangedWithEmpty() {
    }

    default void dumpStateChanged(android.util.IndentingPrintWriter pw) {
    }

    default com.android.server.job.restrictions.JobRestriction restrictByStrictMode(com.android.server.job.controllers.JobStatus job) {
        return null;
    }

    default boolean isStrictRestriction(com.android.server.job.restrictions.JobRestriction restriction) {
        return false;
    }

    default void updateJobCheckTime(int what, boolean reset) {
    }
}
