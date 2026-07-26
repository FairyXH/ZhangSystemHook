package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class LmkdStatsReporter {
    private static final int DIRECT_RECL_AND_THRASHING = 5;
    private static final int DIRECT_RECL_STUCK = 9;
    public static final int KILL_OCCURRED_MSG_SIZE = 80;
    private static final int LOW_FILECACHE_AFTER_THRASHING = 7;
    private static final int LOW_MEM = 8;
    private static final int LOW_MEM_AND_SWAP = 3;
    private static final int LOW_MEM_AND_SWAP_UTIL = 6;
    private static final int LOW_MEM_AND_THRASHING = 4;
    private static final int LOW_SWAP_AND_THRASHING = 2;
    private static final int NOT_RESPONDING = 1;
    private static final int PRESSURE_AFTER_KILL = 0;
    static final java.lang.String TAG = "ActivityManager";

    public static void logKillOccurred(java.io.DataInputStream inputData, int totalForegroundServices, int procsWithForegroundServices) {
        try {
            long pgFault = inputData.readLong();
            long pgMajFault = inputData.readLong();
            long rssInBytes = inputData.readLong();
            long cacheInBytes = inputData.readLong();
            long swapInBytes = inputData.readLong();
            long processStartTimeNS = inputData.readLong();
            int uid = inputData.readInt();
            int oomScore = inputData.readInt();
            int minOomScore = inputData.readInt();
            int freeMemKb = inputData.readInt();
            int freeSwapKb = inputData.readInt();
            int killReason = inputData.readInt();
            int thrashing = inputData.readInt();
            int maxThrashing = inputData.readInt();
            java.lang.String procName = inputData.readUTF();
            com.android.internal.util.FrameworkStatsLog.write(51, uid, procName, oomScore, pgFault, pgMajFault, rssInBytes, cacheInBytes, swapInBytes, processStartTimeNS, minOomScore, freeMemKb, freeSwapKb, mapKillReason(killReason), thrashing, maxThrashing, totalForegroundServices, procsWithForegroundServices);
        } catch (java.io.IOException e) {
            android.util.Slog.e("ActivityManager", "Invalid buffer data. Failed to log LMK_KILL_OCCURRED");
        }
    }

    private static int mapKillReason(int reason) {
        switch (reason) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            case 8:
                return 9;
            case 9:
                return 10;
            default:
                return 0;
        }
    }
}
