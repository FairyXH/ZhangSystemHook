package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastHistory {
    private final int MAX_BROADCAST_HISTORY;
    private final int MAX_BROADCAST_SUMMARY_HISTORY;
    final com.android.server.am.BroadcastRecord[] mBroadcastHistory;
    final android.content.Intent[] mBroadcastSummaryHistory;
    final long[] mSummaryHistoryDispatchTime;
    final long[] mSummaryHistoryEnqueueTime;
    final long[] mSummaryHistoryFinishTime;
    private final java.util.ArrayList<com.android.server.am.BroadcastRecord> mFrozenBroadcasts = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.am.BroadcastRecord> mPendingBroadcasts = new java.util.ArrayList<>();
    int mHistoryNext = 0;
    int mSummaryHistoryNext = 0;

    public BroadcastHistory(com.android.server.am.BroadcastConstants constants) {
        this.MAX_BROADCAST_HISTORY = constants.MAX_HISTORY_COMPLETE_SIZE;
        this.MAX_BROADCAST_SUMMARY_HISTORY = constants.MAX_HISTORY_SUMMARY_SIZE;
        this.mBroadcastHistory = new com.android.server.am.BroadcastRecord[this.MAX_BROADCAST_HISTORY];
        this.mBroadcastSummaryHistory = new android.content.Intent[this.MAX_BROADCAST_SUMMARY_HISTORY];
        this.mSummaryHistoryEnqueueTime = new long[this.MAX_BROADCAST_SUMMARY_HISTORY];
        this.mSummaryHistoryDispatchTime = new long[this.MAX_BROADCAST_SUMMARY_HISTORY];
        this.mSummaryHistoryFinishTime = new long[this.MAX_BROADCAST_SUMMARY_HISTORY];
    }

    void onBroadcastFrozenLocked(com.android.server.am.BroadcastRecord r) {
        this.mFrozenBroadcasts.add(r);
    }

    void onBroadcastEnqueuedLocked(com.android.server.am.BroadcastRecord r) {
        this.mFrozenBroadcasts.remove(r);
        this.mPendingBroadcasts.add(r);
    }

    void onBroadcastFinishedLocked(com.android.server.am.BroadcastRecord r) {
        this.mPendingBroadcasts.remove(r);
        addBroadcastToHistoryLocked(r);
    }

    public void addBroadcastToHistoryLocked(com.android.server.am.BroadcastRecord original) {
        com.android.server.am.BroadcastRecord historyRecord = original.maybeStripForHistory();
        this.mBroadcastHistory[this.mHistoryNext] = historyRecord;
        this.mHistoryNext = ringAdvance(this.mHistoryNext, 1, this.MAX_BROADCAST_HISTORY);
        this.mBroadcastSummaryHistory[this.mSummaryHistoryNext] = historyRecord.intent;
        this.mSummaryHistoryEnqueueTime[this.mSummaryHistoryNext] = historyRecord.enqueueClockTime;
        this.mSummaryHistoryDispatchTime[this.mSummaryHistoryNext] = historyRecord.dispatchClockTime;
        this.mSummaryHistoryFinishTime[this.mSummaryHistoryNext] = java.lang.System.currentTimeMillis();
        this.mSummaryHistoryNext = ringAdvance(this.mSummaryHistoryNext, 1, this.MAX_BROADCAST_SUMMARY_HISTORY);
    }

    private int ringAdvance(int x, int increment, int ringSize) {
        int x2 = x + increment;
        if (x2 < 0) {
            return ringSize - 1;
        }
        if (x2 >= ringSize) {
            return 0;
        }
        return x2;
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
        for (int i = 0; i < this.mPendingBroadcasts.size(); i++) {
            this.mPendingBroadcasts.get(i).dumpDebug(proto, 2246267895815L);
        }
        for (int i2 = 0; i2 < this.mFrozenBroadcasts.size(); i2++) {
            this.mFrozenBroadcasts.get(i2).dumpDebug(proto, 2246267895816L);
        }
        int i3 = this.mHistoryNext;
        int ringIndex = i3;
        do {
            ringIndex = ringAdvance(ringIndex, -1, this.MAX_BROADCAST_HISTORY);
            com.android.server.am.BroadcastRecord r = this.mBroadcastHistory[ringIndex];
            if (r != null) {
                r.dumpDebug(proto, 2246267895813L);
            }
        } while (ringIndex != i3);
        int lastIndex = this.mSummaryHistoryNext;
        int ringIndex2 = lastIndex;
        do {
            ringIndex2 = ringAdvance(ringIndex2, -1, this.MAX_BROADCAST_SUMMARY_HISTORY);
            android.content.Intent intent = this.mBroadcastSummaryHistory[ringIndex2];
            if (intent != null) {
                long summaryToken = proto.start(2246267895814L);
                intent.dumpDebug(proto, 1146756268033L, false, true, true, false);
                proto.write(1112396529666L, this.mSummaryHistoryEnqueueTime[ringIndex2]);
                proto.write(1112396529667L, this.mSummaryHistoryDispatchTime[ringIndex2]);
                proto.write(1112396529668L, this.mSummaryHistoryFinishTime[ringIndex2]);
                proto.end(summaryToken);
            }
        } while (ringIndex2 != lastIndex);
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x01f6 A[LOOP:1: B:38:0x011e->B:56:0x01f6, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01f4 A[SYNTHETIC] */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean dumpLocked(java.io.PrintWriter r22, java.lang.String r23, java.lang.String r24, java.text.SimpleDateFormat r25, boolean r26, boolean r27) {
        /*
            Method dump skipped, instruction units count: 519
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.BroadcastHistory.dumpLocked(java.io.PrintWriter, java.lang.String, java.lang.String, java.text.SimpleDateFormat, boolean, boolean):boolean");
    }

    private void dumpBroadcastList(java.io.PrintWriter pw, java.text.SimpleDateFormat sdf, java.util.ArrayList<com.android.server.am.BroadcastRecord> broadcasts, java.lang.String flavor) {
        pw.print("  ");
        pw.print(flavor);
        pw.println(" broadcasts:");
        if (broadcasts.isEmpty()) {
            pw.println("    <empty>");
            return;
        }
        for (int idx = broadcasts.size() - 1; idx >= 0; idx--) {
            com.android.server.am.BroadcastRecord r = broadcasts.get(idx);
            pw.print(flavor);
            pw.print("  broadcast #");
            pw.print(idx);
            pw.println(":");
            r.dump(pw, "    ", sdf);
        }
    }
}
