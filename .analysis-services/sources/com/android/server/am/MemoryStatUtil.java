package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class MemoryStatUtil {
    private static final java.lang.String MEMORY_STAT_FILE_FMT = "/dev/memcg/apps/uid_%d/pid_%d/memory.stat";
    private static final int PGFAULT_INDEX = 9;
    private static final int PGMAJFAULT_INDEX = 11;
    private static final java.lang.String PROC_STAT_FILE_FMT = "/proc/%d/stat";
    private static final int RSS_IN_PAGES_INDEX = 23;
    private static final java.lang.String TAG = "ActivityManager";
    static final int PAGE_SIZE = (int) android.system.Os.sysconf(android.system.OsConstants._SC_PAGESIZE);
    private static final boolean DEVICE_HAS_PER_APP_MEMCG = android.os.SystemProperties.getBoolean("ro.config.per_app_memcg", false);
    private static final java.util.regex.Pattern PGFAULT = java.util.regex.Pattern.compile("total_pgfault (\\d+)");
    private static final java.util.regex.Pattern PGMAJFAULT = java.util.regex.Pattern.compile("total_pgmajfault (\\d+)");
    private static final java.util.regex.Pattern RSS_IN_BYTES = java.util.regex.Pattern.compile("total_rss (\\d+)");
    private static final java.util.regex.Pattern CACHE_IN_BYTES = java.util.regex.Pattern.compile("total_cache (\\d+)");
    private static final java.util.regex.Pattern SWAP_IN_BYTES = java.util.regex.Pattern.compile("total_swap (\\d+)");

    public static final class MemoryStat {
        public long cacheInBytes;
        public long pgfault;
        public long pgmajfault;
        public long rssInBytes;
        public long swapInBytes;
    }

    private MemoryStatUtil() {
    }

    public static com.android.server.am.MemoryStatUtil.MemoryStat readMemoryStatFromFilesystem(int uid, int pid) {
        return hasMemcg() ? readMemoryStatFromMemcg(uid, pid) : readMemoryStatFromProcfs(pid);
    }

    static com.android.server.am.MemoryStatUtil.MemoryStat readMemoryStatFromMemcg(int uid, int pid) {
        java.lang.String statPath = java.lang.String.format(java.util.Locale.US, MEMORY_STAT_FILE_FMT, java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid));
        return parseMemoryStatFromMemcg(readFileContents(statPath));
    }

    public static com.android.server.am.MemoryStatUtil.MemoryStat readMemoryStatFromProcfs(int pid) {
        java.lang.String statPath = java.lang.String.format(java.util.Locale.US, PROC_STAT_FILE_FMT, java.lang.Integer.valueOf(pid));
        return parseMemoryStatFromProcfs(readFileContents(statPath));
    }

    private static java.lang.String readFileContents(java.lang.String path) {
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
                android.util.Slog.i("ActivityManager", path + " not found");
            }
            return null;
        }
        try {
            return android.os.FileUtils.readTextFile(file, 0, null);
        } catch (java.io.IOException e) {
            android.util.Slog.e("ActivityManager", "Failed to read file:", e);
            return null;
        }
    }

    static com.android.server.am.MemoryStatUtil.MemoryStat parseMemoryStatFromMemcg(java.lang.String memoryStatContents) {
        if (memoryStatContents == null || memoryStatContents.isEmpty()) {
            return null;
        }
        com.android.server.am.MemoryStatUtil.MemoryStat memoryStat = new com.android.server.am.MemoryStatUtil.MemoryStat();
        memoryStat.pgfault = tryParseLong(PGFAULT, memoryStatContents);
        memoryStat.pgmajfault = tryParseLong(PGMAJFAULT, memoryStatContents);
        memoryStat.rssInBytes = tryParseLong(RSS_IN_BYTES, memoryStatContents);
        memoryStat.cacheInBytes = tryParseLong(CACHE_IN_BYTES, memoryStatContents);
        memoryStat.swapInBytes = tryParseLong(SWAP_IN_BYTES, memoryStatContents);
        return memoryStat;
    }

    static com.android.server.am.MemoryStatUtil.MemoryStat parseMemoryStatFromProcfs(java.lang.String procStatContents) {
        if (procStatContents == null || procStatContents.isEmpty()) {
            return null;
        }
        java.lang.String[] splits = procStatContents.split(" ");
        if (splits.length < 24) {
            return null;
        }
        try {
            com.android.server.am.MemoryStatUtil.MemoryStat memoryStat = new com.android.server.am.MemoryStatUtil.MemoryStat();
            memoryStat.pgfault = java.lang.Long.parseLong(splits[9]);
            memoryStat.pgmajfault = java.lang.Long.parseLong(splits[11]);
            memoryStat.rssInBytes = java.lang.Long.parseLong(splits[23]) * ((long) PAGE_SIZE);
            return memoryStat;
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e("ActivityManager", "Failed to parse value", e);
            return null;
        }
    }

    static boolean hasMemcg() {
        return DEVICE_HAS_PER_APP_MEMCG;
    }

    private static long tryParseLong(java.util.regex.Pattern pattern, java.lang.String input) {
        java.util.regex.Matcher m = pattern.matcher(input);
        try {
            if (m.find()) {
                return java.lang.Long.parseLong(m.group(1));
            }
            return 0L;
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e("ActivityManager", "Failed to parse value", e);
            return 0L;
        }
    }
}
