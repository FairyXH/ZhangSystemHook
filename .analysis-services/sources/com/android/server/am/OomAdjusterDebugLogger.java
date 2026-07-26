package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class OomAdjusterDebugLogger {
    private static final int MISC_CLEAR_LAST_BG_TIME = 11;
    private static final int MISC_SCHEDULE_IDLE_UIDS_MSG_1 = 1;
    private static final int MISC_SCHEDULE_IDLE_UIDS_MSG_2 = 2;
    private static final int MISC_SCHEDULE_IDLE_UIDS_MSG_3 = 3;
    private static final int MISC_SET_LAST_BG_TIME = 10;
    private static final java.lang.String STACK_TRACE_TAG = "am_stack";
    private final com.android.server.am.ActivityManagerConstants mConstants;
    private final com.android.server.am.OomAdjuster mOomAdjuster;

    OomAdjusterDebugLogger(com.android.server.am.OomAdjuster oomAdjuster, com.android.server.am.ActivityManagerConstants constants) {
        this.mOomAdjuster = oomAdjuster;
        this.mConstants = constants;
    }

    boolean shouldLog(int uid) {
        return this.mConstants.shouldDebugUidForProcState(uid);
    }

    private void maybeLogStacktrace(java.lang.String msg) {
        if (!this.mConstants.mEnableProcStateStacktrace) {
            return;
        }
        android.util.Slog.i(STACK_TRACE_TAG, msg + ": " + com.android.server.am.OomAdjuster.oomAdjReasonToString(this.mOomAdjuster.mLastReason), new android.app.StackTrace("Called here"));
    }

    private void maybeSleep(int millis) {
        if (millis == 0) {
            return;
        }
        try {
            java.lang.Thread.sleep(millis);
        } catch (java.lang.InterruptedException e) {
        }
    }

    void logUidStateChanged(int uid, int uidstate, int olduidstate, int capability, int oldcapability, int flags) {
        com.android.server.am.EventLogTags.writeAmUidStateChanged(uid, this.mOomAdjuster.mAdjSeq, uidstate, olduidstate, capability, oldcapability, flags, com.android.server.am.OomAdjuster.oomAdjReasonToString(this.mOomAdjuster.mLastReason));
        maybeLogStacktrace("uidStateChanged");
        maybeSleep(this.mConstants.mProcStateDebugSetUidStateDelay);
    }

    void logProcStateChanged(int uid, int pid, int procstate, int oldprocstate, int oomadj, int oldoomadj) {
        com.android.server.am.EventLogTags.writeAmProcStateChanged(uid, pid, this.mOomAdjuster.mAdjSeq, procstate, oldprocstate, oomadj, oldoomadj, com.android.server.am.OomAdjuster.oomAdjReasonToString(this.mOomAdjuster.mLastReason));
        maybeLogStacktrace("procStateChanged");
        maybeSleep(this.mConstants.mProcStateDebugSetProcStateDelay);
    }

    void logScheduleUidIdle1(int uid, long delay) {
        com.android.server.am.EventLogTags.writeAmOomAdjMisc(1, uid, 0, this.mOomAdjuster.mAdjSeq, (int) delay, 0, "");
    }

    void logScheduleUidIdle2(int uid, int pid, long delay) {
        com.android.server.am.EventLogTags.writeAmOomAdjMisc(2, uid, pid, this.mOomAdjuster.mAdjSeq, (int) delay, 0, "");
    }

    void logScheduleUidIdle3(long delay) {
        com.android.server.am.EventLogTags.writeAmOomAdjMisc(3, 0, 0, this.mOomAdjuster.mAdjSeq, (int) delay, 0, "");
    }

    void logSetLastBackgroundTime(int uid, long time) {
        com.android.server.am.EventLogTags.writeAmOomAdjMisc(10, uid, 0, this.mOomAdjuster.mAdjSeq, (int) time, 0, com.android.server.am.OomAdjuster.oomAdjReasonToString(this.mOomAdjuster.mLastReason));
    }

    void logClearLastBackgroundTime(int uid) {
        com.android.server.am.EventLogTags.writeAmOomAdjMisc(11, uid, 0, this.mOomAdjuster.mAdjSeq, 0, 0, com.android.server.am.OomAdjuster.oomAdjReasonToString(this.mOomAdjuster.mLastReason));
    }
}
