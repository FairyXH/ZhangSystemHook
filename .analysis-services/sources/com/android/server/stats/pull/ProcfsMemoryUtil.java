package com.android.server.stats.pull;

/* JADX INFO: loaded from: classes3.dex */
public final class ProcfsMemoryUtil {
    private static final int[] CMDLINE_OUT = {4096};
    private static final java.lang.String[] STATUS_KEYS = {"Uid:", "VmHWM:", "VmRSS:", "RssAnon:", "RssShmem:", "VmSwap:"};
    private static final java.lang.String[] VMSTAT_KEYS = {"oom_kill"};

    public static final class MemorySnapshot {
        public int anonRssInKilobytes;
        public int rssHighWaterMarkInKilobytes;
        public int rssInKilobytes;
        public int rssShmemKilobytes;
        public int swapInKilobytes;
        public int uid;
    }

    private ProcfsMemoryUtil() {
    }

    public static com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot readMemorySnapshotFromProcfs(int pid) {
        long[] output = new long[STATUS_KEYS.length];
        output[0] = -1;
        output[3] = -1;
        output[4] = -1;
        output[5] = -1;
        android.os.Process.readProcLines("/proc/" + pid + "/status", STATUS_KEYS, output);
        if (output[0] == -1 || output[3] == -1 || output[4] == -1 || output[5] == -1) {
            return null;
        }
        com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot snapshot = new com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot();
        snapshot.uid = (int) output[0];
        snapshot.rssHighWaterMarkInKilobytes = (int) output[1];
        snapshot.rssInKilobytes = (int) output[2];
        snapshot.anonRssInKilobytes = (int) output[3];
        snapshot.rssShmemKilobytes = (int) output[4];
        snapshot.swapInKilobytes = (int) output[5];
        return snapshot;
    }

    public static java.lang.String readCmdlineFromProcfs(int pid) {
        java.lang.String[] cmdline = new java.lang.String[1];
        if (!android.os.Process.readProcFile("/proc/" + pid + "/cmdline", CMDLINE_OUT, cmdline, null, null)) {
            return "";
        }
        return cmdline[0];
    }

    public static android.util.SparseArray<java.lang.String> getProcessCmdlines() {
        int[] pids = android.os.Process.getPids("/proc", new int[1024]);
        android.util.SparseArray<java.lang.String> cmdlines = new android.util.SparseArray<>(pids.length);
        for (int pid : pids) {
            if (pid < 0) {
                break;
            }
            java.lang.String cmdline = readCmdlineFromProcfs(pid);
            if (!cmdline.isEmpty()) {
                cmdlines.append(pid, cmdline);
            }
        }
        return cmdlines;
    }

    static com.android.server.stats.pull.ProcfsMemoryUtil.VmStat readVmStat() {
        long[] vmstat = new long[VMSTAT_KEYS.length];
        vmstat[0] = -1;
        android.os.Process.readProcLines("/proc/vmstat", VMSTAT_KEYS, vmstat);
        if (vmstat[0] == -1) {
            return null;
        }
        com.android.server.stats.pull.ProcfsMemoryUtil.VmStat result = new com.android.server.stats.pull.ProcfsMemoryUtil.VmStat();
        result.oomKillCount = (int) vmstat[0];
        return result;
    }

    static final class VmStat {
        public int oomKillCount;

        VmStat() {
        }
    }
}
