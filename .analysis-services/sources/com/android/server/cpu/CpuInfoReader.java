package com.android.server.cpu;

/* JADX INFO: loaded from: classes.dex */
public final class CpuInfoReader {
    private static final java.lang.String AFFECTED_CPUS_FILE = "affected_cpus";
    private static final java.lang.String CPUFREQ_DIR_PATH = "/sys/devices/system/cpu/cpufreq";
    private static final java.lang.String CPUSET_BACKGROUND_DIR = "background";
    private static final java.lang.String CPUSET_DIR_PATH = "/dev/cpuset";
    private static final java.lang.String CPUSET_TOP_APP_DIR = "top-app";
    private static final java.lang.String CPUS_FILE = "cpus";
    private static final java.lang.String CUR_SCALING_FREQ_FILE = "scaling_cur_freq";
    static final int FLAG_CPUSET_CATEGORY_BACKGROUND = 2;
    static final int FLAG_CPUSET_CATEGORY_TOP_APP = 1;
    private static final java.lang.String MAX_SCALING_FREQ_FILE = "scaling_max_freq";
    private static final long MIN_READ_INTERVAL_MILLISECONDS = 500;
    private static final java.lang.String POLICY_DIR_PREFIX = "policy";
    private static final java.lang.String PROC_STAT_FILE_PATH = "/proc/stat";
    private static final java.lang.String RELATED_CPUS_FILE = "related_cpus";
    private static final java.lang.String TIME_IN_STATE_FILE = "stats/time_in_state";
    private java.io.File mCpuFreqDir;
    private final android.util.SparseArray<java.io.File> mCpuFreqPolicyDirsById;
    private final android.util.SparseIntArray mCpusetCategoriesByCpus;
    private final java.io.File mCpusetDir;
    private android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuUsageStats> mCumulativeCpuUsageStats;
    private boolean mHasTimeInStateFile;
    private boolean mIsEnabled;
    private android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuInfo> mLastReadCpuInfos;
    private long mLastReadUptimeMillis;
    private final long mMinReadIntervalMillis;
    private java.io.File mProcStatFile;
    private final android.util.SparseArray<com.android.server.cpu.CpuInfoReader.StaticPolicyInfo> mStaticPolicyInfoById;
    private final android.util.SparseArray<android.util.LongSparseLongArray> mTimeInStateByPolicyId;
    private static final java.util.regex.Pattern PROC_STAT_PATTERN = java.util.regex.Pattern.compile("cpu(?<core>[0-9]+)\\s(?<userClockTicks>[0-9]+)\\s(?<niceClockTicks>[0-9]+)\\s(?<sysClockTicks>[0-9]+)\\s(?<idleClockTicks>[0-9]+)\\s(?<iowaitClockTicks>[0-9]+)\\s(?<irqClockTicks>[0-9]+)\\s(?<softirqClockTicks>[0-9]+)\\s(?<stealClockTicks>[0-9]+)\\s(?<guestClockTicks>[0-9]+)\\s(?<guestNiceClockTicks>[0-9]+)");
    private static final java.util.regex.Pattern TIME_IN_STATE_PATTERN = java.util.regex.Pattern.compile("(?<freqKHz>[0-9]+)\\s(?<time>[0-9]+)");
    private static final long MILLIS_PER_CLOCK_TICK = 1000 / android.system.Os.sysconf(android.system.OsConstants._SC_CLK_TCK);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface CpusetCategory {
    }

    public CpuInfoReader() {
        this(new java.io.File(CPUSET_DIR_PATH), new java.io.File(CPUFREQ_DIR_PATH), new java.io.File(PROC_STAT_FILE_PATH), 500L);
    }

    CpuInfoReader(java.io.File cpusetDir, java.io.File cpuFreqDir, java.io.File procStatFile, long minReadIntervalMillis) {
        this.mCpusetCategoriesByCpus = new android.util.SparseIntArray();
        this.mCpuFreqPolicyDirsById = new android.util.SparseArray<>();
        this.mStaticPolicyInfoById = new android.util.SparseArray<>();
        this.mTimeInStateByPolicyId = new android.util.SparseArray<>();
        this.mCumulativeCpuUsageStats = new android.util.SparseArray<>();
        this.mCpusetDir = cpusetDir;
        this.mCpuFreqDir = cpuFreqDir;
        this.mProcStatFile = procStatFile;
        this.mMinReadIntervalMillis = minReadIntervalMillis;
    }

    public boolean init() {
        if (this.mCpuFreqPolicyDirsById.size() > 0) {
            com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "Ignoring duplicate CpuInfoReader init request");
            return this.mIsEnabled;
        }
        java.io.File[] policyDirs = this.mCpuFreqDir.listFiles(new java.io.FileFilter() { // from class: com.android.server.cpu.CpuInfoReader$$ExternalSyntheticLambda1
            @Override // java.io.FileFilter
            public final boolean accept(java.io.File file) {
                return com.android.server.cpu.CpuInfoReader.lambda$init$0(file);
            }
        });
        if (policyDirs == null || policyDirs.length == 0) {
            com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "Missing CPU frequency policy directories at %s", this.mCpuFreqDir.getAbsolutePath());
            return false;
        }
        populateCpuFreqPolicyDirsById(policyDirs);
        if (this.mCpuFreqPolicyDirsById.size() == 0) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to parse CPU frequency policy directory paths: %s", java.util.Arrays.toString(policyDirs));
            return false;
        }
        readStaticPolicyInfo();
        if (this.mStaticPolicyInfoById.size() == 0) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to read static CPU frequency policy info from policy dirs: %s", java.util.Arrays.toString(policyDirs));
            return false;
        }
        if (!this.mProcStatFile.exists()) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Missing proc stat file at %s", this.mProcStatFile.getAbsolutePath());
            return false;
        }
        readCpusetCategories();
        if (this.mCpusetCategoriesByCpus.size() == 0) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to read cpuset information from %s", this.mCpusetDir.getAbsolutePath());
            return false;
        }
        for (int i = 0; i < this.mCpuFreqPolicyDirsById.size() && !this.mHasTimeInStateFile; i++) {
            this.mHasTimeInStateFile |= new java.io.File(this.mCpuFreqPolicyDirsById.valueAt(i), TIME_IN_STATE_FILE).exists();
        }
        if (!this.mHasTimeInStateFile) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Time in state file not available for any cpufreq policy");
        }
        this.mIsEnabled = true;
        return true;
    }

    static /* synthetic */ boolean lambda$init$0(java.io.File file) {
        return file.isDirectory() && file.getName().startsWith(POLICY_DIR_PREFIX);
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuInfo> readCpuInfos() {
        /*
            Method dump skipped, instruction units count: 507
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.cpu.CpuInfoReader.readCpuInfos():android.util.SparseArray");
    }

    public void dump(android.util.IndentingPrintWriter writer) {
        writer.printf("*%s*\n", new java.lang.Object[]{getClass().getSimpleName()});
        writer.increaseIndent();
        writer.printf("mCpusetDir = %s\n", new java.lang.Object[]{this.mCpusetDir.getAbsolutePath()});
        writer.printf("mCpuFreqDir = %s\n", new java.lang.Object[]{this.mCpuFreqDir.getAbsolutePath()});
        writer.printf("mProcStatFile = %s\n", new java.lang.Object[]{this.mProcStatFile.getAbsolutePath()});
        writer.printf("mIsEnabled = %s\n", new java.lang.Object[]{java.lang.Boolean.valueOf(this.mIsEnabled)});
        writer.printf("mHasTimeInStateFile = %s\n", new java.lang.Object[]{java.lang.Boolean.valueOf(this.mHasTimeInStateFile)});
        writer.printf("mLastReadUptimeMillis = %d\n", new java.lang.Object[]{java.lang.Long.valueOf(this.mLastReadUptimeMillis)});
        writer.printf("mMinReadIntervalMillis = %d\n", new java.lang.Object[]{java.lang.Long.valueOf(this.mMinReadIntervalMillis)});
        writer.printf("Cpuset categories by CPU core:\n", new java.lang.Object[0]);
        writer.increaseIndent();
        for (int i = 0; i < this.mCpusetCategoriesByCpus.size(); i++) {
            writer.printf("CPU core id = %d, %s\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mCpusetCategoriesByCpus.keyAt(i)), toCpusetCategoriesStr(this.mCpusetCategoriesByCpus.valueAt(i))});
        }
        writer.decreaseIndent();
        writer.println("Cpu frequency policy directories by policy id:");
        writer.increaseIndent();
        for (int i2 = 0; i2 < this.mCpuFreqPolicyDirsById.size(); i2++) {
            writer.printf("Policy id = %d, Dir = %s\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mCpuFreqPolicyDirsById.keyAt(i2)), this.mCpuFreqPolicyDirsById.valueAt(i2)});
        }
        writer.decreaseIndent();
        writer.println("Static cpu frequency policy infos by policy id:");
        writer.increaseIndent();
        for (int i3 = 0; i3 < this.mStaticPolicyInfoById.size(); i3++) {
            writer.printf("Policy id = %d, %s\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mStaticPolicyInfoById.keyAt(i3)), this.mStaticPolicyInfoById.valueAt(i3)});
        }
        writer.decreaseIndent();
        writer.println("Cpu time in frequency state by policy id:");
        writer.increaseIndent();
        for (int i4 = 0; i4 < this.mTimeInStateByPolicyId.size(); i4++) {
            writer.printf("Policy id = %d, Time(millis) in state by CPU frequency(KHz) = %s\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mTimeInStateByPolicyId.keyAt(i4)), this.mTimeInStateByPolicyId.valueAt(i4)});
        }
        writer.decreaseIndent();
        writer.println("Last read CPU infos:");
        writer.increaseIndent();
        for (int i5 = 0; i5 < this.mLastReadCpuInfos.size(); i5++) {
            writer.printf("%s\n", new java.lang.Object[]{this.mLastReadCpuInfos.valueAt(i5)});
        }
        writer.decreaseIndent();
        writer.println("Latest cumulative CPU usage stats by CPU core:");
        writer.increaseIndent();
        for (int i6 = 0; i6 < this.mCumulativeCpuUsageStats.size(); i6++) {
            writer.printf("CPU core id = %d, %s\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mCumulativeCpuUsageStats.keyAt(i6)), this.mCumulativeCpuUsageStats.valueAt(i6)});
        }
        writer.decreaseIndent();
        writer.decreaseIndent();
    }

    boolean setCpuFreqDir(java.io.File cpuFreqDir) {
        java.io.File[] cpuFreqPolicyDirs = cpuFreqDir.listFiles(new java.io.FileFilter() { // from class: com.android.server.cpu.CpuInfoReader$$ExternalSyntheticLambda0
            @Override // java.io.FileFilter
            public final boolean accept(java.io.File file) {
                return com.android.server.cpu.CpuInfoReader.lambda$setCpuFreqDir$1(file);
            }
        });
        if (cpuFreqPolicyDirs == null || cpuFreqPolicyDirs.length == 0) {
            com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "Failed to set CPU frequency directory. Missing policy directories at %s", cpuFreqDir.getAbsolutePath());
            return false;
        }
        populateCpuFreqPolicyDirsById(cpuFreqPolicyDirs);
        int numCpuFreqPolicyDirs = this.mCpuFreqPolicyDirsById.size();
        int numStaticPolicyInfos = this.mStaticPolicyInfoById.size();
        if (numCpuFreqPolicyDirs == 0 || numCpuFreqPolicyDirs != numStaticPolicyInfos) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to set CPU frequency directory to %s. Total CPU frequency policies (%d) under new path is either 0 or not equal to initial total CPU frequency policies. Clearing CPU frequency policy directories", cpuFreqDir.getAbsolutePath(), java.lang.Integer.valueOf(numCpuFreqPolicyDirs), java.lang.Integer.valueOf(numStaticPolicyInfos));
            this.mCpuFreqPolicyDirsById.clear();
            return false;
        }
        this.mCpuFreqDir = cpuFreqDir;
        return true;
    }

    static /* synthetic */ boolean lambda$setCpuFreqDir$1(java.io.File file) {
        return file.isDirectory() && file.getName().startsWith(POLICY_DIR_PREFIX);
    }

    boolean setProcStatFile(java.io.File procStatFile) {
        if (!procStatFile.exists()) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Missing proc stat file at %s", procStatFile.getAbsolutePath());
            return false;
        }
        this.mProcStatFile = procStatFile;
        return true;
    }

    private void populateCpuFreqPolicyDirsById(java.io.File[] policyDirs) {
        this.mCpuFreqPolicyDirsById.clear();
        for (java.io.File policyDir : policyDirs) {
            java.lang.String policyIdStr = policyDir.getName().substring(POLICY_DIR_PREFIX.length());
            if (!policyIdStr.isEmpty()) {
                this.mCpuFreqPolicyDirsById.append(java.lang.Integer.parseInt(policyIdStr), policyDir);
                if (com.android.server.cpu.CpuMonitorService.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Cached policy directory %s for policy id %s", policyDir, policyIdStr);
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readCpusetCategories() {
        /*
            r11 = this;
            java.io.File r0 = r11.mCpusetDir
            com.android.server.cpu.CpuInfoReader$$ExternalSyntheticLambda2 r1 = new com.android.server.cpu.CpuInfoReader$$ExternalSyntheticLambda2
            r1.<init>()
            java.io.File[] r0 = r0.listFiles(r1)
            if (r0 != 0) goto L1f
            java.lang.String r1 = com.android.server.cpu.CpuMonitorService.TAG
            java.io.File r2 = r11.mCpusetDir
            java.lang.String r2 = r2.getAbsolutePath()
            java.lang.Object[] r2 = new java.lang.Object[]{r2}
            java.lang.String r3 = "Missing cpuset directories at %s"
            com.android.server.utils.Slogf.e(r1, r3, r2)
            return
        L1f:
            r1 = 0
        L20:
            int r2 = r0.length
            if (r1 >= r2) goto Lb4
            r2 = r0[r1]
            java.lang.String r3 = r2.getName()
            int r4 = r3.hashCode()
            switch(r4) {
                case -1332194002: goto L3c;
                case -1141047895: goto L31;
                default: goto L30;
            }
        L30:
            goto L46
        L31:
            java.lang.String r4 = "top-app"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L30
            r3 = 0
            goto L47
        L3c:
            java.lang.String r4 = "background"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L30
            r3 = 1
            goto L47
        L46:
            r3 = -1
        L47:
            switch(r3) {
                case 0: goto L4d;
                case 1: goto L4b;
                default: goto L4a;
            }
        L4a:
            goto Lb0
        L4b:
            r3 = 2
            goto L4f
        L4d:
            r3 = 1
        L4f:
            java.io.File r4 = new java.io.File
            java.lang.String r5 = r2.getPath()
            java.lang.String r6 = "cpus"
            r4.<init>(r5, r6)
            android.util.IntArray r5 = readCpuCores(r4)
            if (r5 == 0) goto La0
            int r6 = r5.size()
            if (r6 != 0) goto L67
            goto La0
        L67:
            r6 = 0
        L68:
            int r7 = r5.size()
            if (r6 >= r7) goto Lb0
            android.util.SparseIntArray r7 = r11.mCpusetCategoriesByCpus
            int r8 = r5.get(r6)
            int r7 = r7.get(r8)
            r7 = r7 | r3
            android.util.SparseIntArray r8 = r11.mCpusetCategoriesByCpus
            int r9 = r5.get(r6)
            r8.append(r9, r7)
            boolean r8 = com.android.server.cpu.CpuMonitorService.DEBUG
            if (r8 == 0) goto L9d
            java.lang.String r8 = com.android.server.cpu.CpuMonitorService.TAG
            int r9 = r5.get(r6)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.String r10 = toCpusetCategoriesStr(r7)
            java.lang.Object[] r9 = new java.lang.Object[]{r9, r10}
            java.lang.String r10 = "Mapping CPU core id %d with cpuset categories [%s]"
            com.android.server.utils.Slogf.d(r8, r10, r9)
        L9d:
            int r6 = r6 + 1
            goto L68
        La0:
            java.lang.String r6 = com.android.server.cpu.CpuMonitorService.TAG
            java.lang.String r7 = r4.getAbsolutePath()
            java.lang.Object[] r7 = new java.lang.Object[]{r7}
            java.lang.String r8 = "Failed to read CPU cores from %s"
            com.android.server.utils.Slogf.e(r6, r8, r7)
        Lb0:
            int r1 = r1 + 1
            goto L20
        Lb4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.cpu.CpuInfoReader.readCpusetCategories():void");
    }

    private void readStaticPolicyInfo() {
        for (int i = 0; i < this.mCpuFreqPolicyDirsById.size(); i++) {
            int policyId = this.mCpuFreqPolicyDirsById.keyAt(i);
            java.io.File policyDir = this.mCpuFreqPolicyDirsById.valueAt(i);
            java.io.File cpuCoresFile = new java.io.File(policyDir, RELATED_CPUS_FILE);
            android.util.IntArray relatedCpuCores = readCpuCores(cpuCoresFile);
            if (relatedCpuCores == null || relatedCpuCores.size() == 0) {
                com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to read related CPU cores from %s", cpuCoresFile.getAbsolutePath());
            } else {
                com.android.server.cpu.CpuInfoReader.StaticPolicyInfo staticPolicyInfo = new com.android.server.cpu.CpuInfoReader.StaticPolicyInfo(relatedCpuCores);
                this.mStaticPolicyInfoById.append(policyId, staticPolicyInfo);
                if (com.android.server.cpu.CpuMonitorService.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Added static policy info %s for policy id %d", staticPolicyInfo, java.lang.Integer.valueOf(policyId));
                }
            }
        }
    }

    private android.util.SparseArray<com.android.server.cpu.CpuInfoReader.DynamicPolicyInfo> readDynamicPolicyInfo() {
        android.util.SparseArray<com.android.server.cpu.CpuInfoReader.DynamicPolicyInfo> dynamicPolicyInfoById = new android.util.SparseArray<>();
        for (int i = 0; i < this.mCpuFreqPolicyDirsById.size(); i++) {
            int policyId = this.mCpuFreqPolicyDirsById.keyAt(i);
            java.io.File policyDir = this.mCpuFreqPolicyDirsById.valueAt(i);
            long curCpuFreqKHz = readCpuFreqKHz(new java.io.File(policyDir, CUR_SCALING_FREQ_FILE));
            if (curCpuFreqKHz == 0) {
                com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "Missing current frequency information at %s", policyDir.getAbsolutePath());
            } else {
                long avgTimeInStateCpuFreqKHz = readAvgTimeInStateCpuFrequency(policyId, policyDir);
                java.io.File cpuCoresFile = new java.io.File(policyDir, AFFECTED_CPUS_FILE);
                android.util.IntArray affectedCpuCores = readCpuCores(cpuCoresFile);
                if (affectedCpuCores == null || affectedCpuCores.size() == 0) {
                    com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to read CPU cores from %s", cpuCoresFile.getAbsolutePath());
                } else {
                    long maxCpuFreqKHz = readCpuFreqKHz(new java.io.File(policyDir, MAX_SCALING_FREQ_FILE));
                    if (maxCpuFreqKHz == 0) {
                        com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "Missing max CPU frequency information at %s", policyDir.getAbsolutePath());
                    } else {
                        com.android.server.cpu.CpuInfoReader.DynamicPolicyInfo dynamicPolicyInfo = new com.android.server.cpu.CpuInfoReader.DynamicPolicyInfo(curCpuFreqKHz, maxCpuFreqKHz, avgTimeInStateCpuFreqKHz, affectedCpuCores);
                        dynamicPolicyInfoById.append(policyId, dynamicPolicyInfo);
                        if (com.android.server.cpu.CpuMonitorService.DEBUG) {
                            com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Read dynamic policy info %s for policy id %d", dynamicPolicyInfo, java.lang.Integer.valueOf(policyId));
                        }
                    }
                }
            }
        }
        return dynamicPolicyInfoById;
    }

    private long readAvgTimeInStateCpuFrequency(int policyId, java.io.File policyDir) {
        android.util.LongSparseLongArray latestTimeInState = readTimeInState(policyDir);
        if (latestTimeInState == null || latestTimeInState.size() == 0) {
            return 0L;
        }
        android.util.LongSparseLongArray prevTimeInState = this.mTimeInStateByPolicyId.get(policyId);
        if (prevTimeInState == null) {
            this.mTimeInStateByPolicyId.put(policyId, latestTimeInState);
            if (com.android.server.cpu.CpuMonitorService.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Added aggregated time in state info for policy id %d", java.lang.Integer.valueOf(policyId));
            }
            return calculateAvgCpuFreq(latestTimeInState);
        }
        android.util.LongSparseLongArray deltaTimeInState = calculateDeltaTimeInState(prevTimeInState, latestTimeInState);
        this.mTimeInStateByPolicyId.put(policyId, latestTimeInState);
        if (com.android.server.cpu.CpuMonitorService.DEBUG) {
            com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Added latest delta time in state info for policy id %d", java.lang.Integer.valueOf(policyId));
        }
        return calculateAvgCpuFreq(deltaTimeInState);
    }

    private android.util.LongSparseLongArray readTimeInState(java.io.File policyDir) {
        if (!this.mHasTimeInStateFile) {
            return null;
        }
        java.io.File timeInStateFile = new java.io.File(policyDir, TIME_IN_STATE_FILE);
        try {
            java.util.List<java.lang.String> lines = java.nio.file.Files.readAllLines(timeInStateFile.toPath());
            if (lines.isEmpty()) {
                com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "Empty time in state file at %s", timeInStateFile.getAbsolutePath());
                return null;
            }
            android.util.LongSparseLongArray cpuTimeByFrequencies = new android.util.LongSparseLongArray();
            for (int i = 0; i < lines.size(); i++) {
                java.util.regex.Matcher m = TIME_IN_STATE_PATTERN.matcher(lines.get(i).trim());
                if (m.find()) {
                    cpuTimeByFrequencies.put(java.lang.Long.parseLong(m.group("freqKHz")), clockTickStrToMillis(m.group("time")));
                }
            }
            return cpuTimeByFrequencies;
        } catch (java.lang.Exception e) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, e, "Failed to read CPU time in state from file: %s", timeInStateFile.getAbsolutePath());
            return null;
        }
    }

    private static long readCpuFreqKHz(java.io.File file) {
        if (!file.exists()) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "CPU frequency file %s doesn't exist", file.getAbsolutePath());
            return 0L;
        }
        try {
            java.util.List<java.lang.String> lines = java.nio.file.Files.readAllLines(file.toPath());
            if (!lines.isEmpty()) {
                long frequency = java.lang.Long.parseLong(lines.get(0).trim());
                if (frequency > 0) {
                    return frequency;
                }
                return 0L;
            }
        } catch (java.lang.Exception e) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, e, "Failed to read integer content from file: %s", file.getAbsolutePath());
        }
        return 0L;
    }

    private static android.util.LongSparseLongArray calculateDeltaTimeInState(android.util.LongSparseLongArray prevTimeInState, android.util.LongSparseLongArray latestTimeInState) {
        int numTimeInStateEntries = latestTimeInState.size();
        android.util.LongSparseLongArray deltaTimeInState = new android.util.LongSparseLongArray(numTimeInStateEntries);
        for (int i = 0; i < numTimeInStateEntries; i++) {
            long freq = latestTimeInState.keyAt(i);
            long durationMillis = latestTimeInState.valueAt(i);
            long prevDurationMillis = prevTimeInState.get(freq);
            deltaTimeInState.put(freq, durationMillis > prevDurationMillis ? durationMillis - prevDurationMillis : durationMillis);
        }
        return deltaTimeInState;
    }

    private static long calculateAvgCpuFreq(android.util.LongSparseLongArray timeInState) {
        double totalTimeInState = 0.0d;
        for (int i = 0; i < timeInState.size(); i++) {
            totalTimeInState += timeInState.valueAt(i);
        }
        if (totalTimeInState == 0.0d) {
            return 0L;
        }
        double avgFreqKHz = 0.0d;
        for (int i2 = 0; i2 < timeInState.size(); i2++) {
            avgFreqKHz += (timeInState.keyAt(i2) * timeInState.valueAt(i2)) / totalTimeInState;
        }
        return (long) avgFreqKHz;
    }

    private static android.util.IntArray readCpuCores(java.io.File file) {
        java.lang.String[] pairs;
        if (!file.exists()) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to read CPU cores as the file '%s' doesn't exist", file.getAbsolutePath());
            return null;
        }
        try {
            java.util.List<java.lang.String> lines = java.nio.file.Files.readAllLines(file.toPath());
            android.util.IntArray cpuCores = new android.util.IntArray(0);
            for (int i = 0; i < lines.size(); i++) {
                java.lang.String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    if (line.contains(",")) {
                        pairs = line.split(",");
                    } else {
                        pairs = line.split(" ");
                    }
                    for (int j = 0; j < pairs.length; j++) {
                        java.lang.String[] minMaxPairs = pairs[j].split("-");
                        if (minMaxPairs.length >= 2) {
                            int min = java.lang.Integer.parseInt(minMaxPairs[0]);
                            int max = java.lang.Integer.parseInt(minMaxPairs[1]);
                            if (min <= max) {
                                for (int id = min; id <= max; id++) {
                                    cpuCores.add(id);
                                }
                            }
                        } else if (minMaxPairs.length == 1) {
                            cpuCores.add(java.lang.Integer.parseInt(minMaxPairs[0]));
                        } else {
                            com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "Invalid CPU core range format %s", pairs[j]);
                        }
                    }
                }
            }
            return cpuCores;
        } catch (java.lang.NumberFormatException e) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, e, "Failed to read CPU cores from %s due to incorrect file format", file.getAbsolutePath());
            return null;
        } catch (java.lang.Exception e2) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, e2, "Failed to read CPU cores from %s", file.getAbsolutePath());
            return null;
        }
    }

    private android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuUsageStats> readLatestCpuUsageStats() {
        android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuUsageStats> cumulativeCpuUsageStats = readCumulativeCpuUsageStats();
        if (cumulativeCpuUsageStats.size() == 0) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, "Failed to read cumulative CPU usage stats");
            return null;
        }
        android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuUsageStats> deltaCpuUsageStats = new android.util.SparseArray<>();
        for (int i = 0; i < cumulativeCpuUsageStats.size(); i++) {
            int cpu = cumulativeCpuUsageStats.keyAt(i);
            com.android.server.cpu.CpuInfoReader.CpuUsageStats newStats = cumulativeCpuUsageStats.valueAt(i);
            com.android.server.cpu.CpuInfoReader.CpuUsageStats oldStats = this.mCumulativeCpuUsageStats.get(cpu);
            deltaCpuUsageStats.append(cpu, oldStats == null ? newStats : newStats.delta(oldStats));
        }
        this.mCumulativeCpuUsageStats = cumulativeCpuUsageStats;
        return deltaCpuUsageStats;
    }

    private android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuUsageStats> readCumulativeCpuUsageStats() {
        java.util.List<java.lang.String> lines;
        android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuUsageStats> cpuUsageStats = new android.util.SparseArray<>();
        try {
            java.util.List<java.lang.String> lines2 = java.nio.file.Files.readAllLines(this.mProcStatFile.toPath());
            int i = 0;
            while (i < lines2.size()) {
                java.util.regex.Matcher m = PROC_STAT_PATTERN.matcher(lines2.get(i).trim());
                if (!m.find()) {
                    lines = lines2;
                } else {
                    lines = lines2;
                    cpuUsageStats.append(java.lang.Integer.parseInt(m.group("core")), new com.android.server.cpu.CpuInfoReader.CpuUsageStats(clockTickStrToMillis(m.group("userClockTicks")), clockTickStrToMillis(m.group("niceClockTicks")), clockTickStrToMillis(m.group("sysClockTicks")), clockTickStrToMillis(m.group("idleClockTicks")), clockTickStrToMillis(m.group("iowaitClockTicks")), clockTickStrToMillis(m.group("irqClockTicks")), clockTickStrToMillis(m.group("softirqClockTicks")), clockTickStrToMillis(m.group("stealClockTicks")), clockTickStrToMillis(m.group("guestClockTicks")), clockTickStrToMillis(m.group("guestNiceClockTicks"))));
                }
                i++;
                lines2 = lines;
            }
        } catch (java.lang.Exception e) {
            com.android.server.utils.Slogf.e(com.android.server.cpu.CpuMonitorService.TAG, e, "Failed to read cpu usage stats from %s", this.mProcStatFile.getAbsolutePath());
        }
        return cpuUsageStats;
    }

    private static long clockTickStrToMillis(java.lang.String jiffyStr) {
        return java.lang.Long.parseLong(jiffyStr) * MILLIS_PER_CLOCK_TICK;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String toCpusetCategoriesStr(int cpusetCategories) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if ((cpusetCategories & 1) != 0) {
            builder.append("FLAG_CPUSET_CATEGORY_TOP_APP");
        }
        if ((cpusetCategories & 2) != 0) {
            if (builder.length() > 0) {
                builder.append('|');
            }
            builder.append("FLAG_CPUSET_CATEGORY_BACKGROUND");
        }
        return builder.toString();
    }

    public static final class CpuInfo {
        public static final long MISSING_FREQUENCY = 0;
        public final long avgTimeInStateCpuFreqKHz;
        public final int cpuCore;
        public final int cpusetCategories;
        public final long curCpuFreqKHz;
        public final boolean isOnline;
        public final com.android.server.cpu.CpuInfoReader.CpuUsageStats latestCpuUsageStats;
        private long mNormalizedAvailableCpuFreqKHz;
        public final long maxCpuFreqKHz;

        CpuInfo(int cpuCore, int cpusetCategories, boolean isOnline, long curCpuFreqKHz, long maxCpuFreqKHz, long avgTimeInStateCpuFreqKHz, com.android.server.cpu.CpuInfoReader.CpuUsageStats latestCpuUsageStats) {
            this(cpuCore, cpusetCategories, isOnline, curCpuFreqKHz, maxCpuFreqKHz, avgTimeInStateCpuFreqKHz, 0L, latestCpuUsageStats);
            this.mNormalizedAvailableCpuFreqKHz = computeNormalizedAvailableCpuFreqKHz();
        }

        CpuInfo(int cpuCore, int cpusetCategories, boolean isOnline, long curCpuFreqKHz, long maxCpuFreqKHz, long avgTimeInStateCpuFreqKHz, long normalizedAvailableCpuFreqKHz, com.android.server.cpu.CpuInfoReader.CpuUsageStats latestCpuUsageStats) {
            this.cpuCore = cpuCore;
            this.cpusetCategories = cpusetCategories;
            this.isOnline = isOnline;
            this.curCpuFreqKHz = curCpuFreqKHz;
            this.maxCpuFreqKHz = maxCpuFreqKHz;
            this.avgTimeInStateCpuFreqKHz = avgTimeInStateCpuFreqKHz;
            this.latestCpuUsageStats = latestCpuUsageStats;
            this.mNormalizedAvailableCpuFreqKHz = normalizedAvailableCpuFreqKHz;
        }

        public long getNormalizedAvailableCpuFreqKHz() {
            return this.mNormalizedAvailableCpuFreqKHz;
        }

        public java.lang.String toString() {
            return "CpuInfo{ cpuCore = " + this.cpuCore + ", cpusetCategories = [" + com.android.server.cpu.CpuInfoReader.toCpusetCategoriesStr(this.cpusetCategories) + "], isOnline = " + (this.isOnline ? "Yes" : "No") + ", curCpuFreqKHz = " + (this.curCpuFreqKHz == 0 ? "missing" : java.lang.Long.valueOf(this.curCpuFreqKHz)) + ", maxCpuFreqKHz = " + (this.maxCpuFreqKHz == 0 ? "missing" : java.lang.Long.valueOf(this.maxCpuFreqKHz)) + ", avgTimeInStateCpuFreqKHz = " + (this.avgTimeInStateCpuFreqKHz != 0 ? java.lang.Long.valueOf(this.avgTimeInStateCpuFreqKHz) : "missing") + ", latestCpuUsageStats = " + this.latestCpuUsageStats + ", mNormalizedAvailableCpuFreqKHz = " + this.mNormalizedAvailableCpuFreqKHz + " }";
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof com.android.server.cpu.CpuInfoReader.CpuInfo)) {
                return false;
            }
            com.android.server.cpu.CpuInfoReader.CpuInfo other = (com.android.server.cpu.CpuInfoReader.CpuInfo) obj;
            return this.cpuCore == other.cpuCore && this.cpusetCategories == other.cpusetCategories && this.isOnline == other.isOnline && this.curCpuFreqKHz == other.curCpuFreqKHz && this.maxCpuFreqKHz == other.maxCpuFreqKHz && this.avgTimeInStateCpuFreqKHz == other.avgTimeInStateCpuFreqKHz && this.latestCpuUsageStats.equals(other.latestCpuUsageStats) && this.mNormalizedAvailableCpuFreqKHz == other.mNormalizedAvailableCpuFreqKHz;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.cpuCore), java.lang.Integer.valueOf(this.cpusetCategories), java.lang.Boolean.valueOf(this.isOnline), java.lang.Long.valueOf(this.curCpuFreqKHz), java.lang.Long.valueOf(this.maxCpuFreqKHz), java.lang.Long.valueOf(this.avgTimeInStateCpuFreqKHz), this.latestCpuUsageStats, java.lang.Long.valueOf(this.mNormalizedAvailableCpuFreqKHz));
        }

        private long computeNormalizedAvailableCpuFreqKHz() {
            if (!this.isOnline) {
                return 0L;
            }
            long totalTimeMillis = this.latestCpuUsageStats.getTotalTimeMillis();
            if (totalTimeMillis == 0) {
                com.android.server.utils.Slogf.wtf(com.android.server.cpu.CpuMonitorService.TAG, "Total CPU time millis is 0. This shouldn't happen unless stats are polled too frequently");
                return 0L;
            }
            double nonIdlePercent = ((totalTimeMillis - this.latestCpuUsageStats.idleTimeMillis) * 100.0d) / totalTimeMillis;
            long curFreqKHz = this.avgTimeInStateCpuFreqKHz == 0 ? this.curCpuFreqKHz : this.avgTimeInStateCpuFreqKHz;
            double availablePercent = 100.0d - ((curFreqKHz * nonIdlePercent) / this.maxCpuFreqKHz);
            return (long) ((this.maxCpuFreqKHz * availablePercent) / 100.0d);
        }
    }

    public static final class CpuUsageStats {
        public final long guestNiceTimeMillis;
        public final long guestTimeMillis;
        public final long idleTimeMillis;
        public final long iowaitTimeMillis;
        public final long irqTimeMillis;
        public final long niceTimeMillis;
        public final long softirqTimeMillis;
        public final long stealTimeMillis;
        public final long systemTimeMillis;
        public final long userTimeMillis;

        public CpuUsageStats(long userTimeMillis, long niceTimeMillis, long systemTimeMillis, long idleTimeMillis, long iowaitTimeMillis, long irqTimeMillis, long softirqTimeMillis, long stealTimeMillis, long guestTimeMillis, long guestNiceTimeMillis) {
            this.userTimeMillis = userTimeMillis;
            this.niceTimeMillis = niceTimeMillis;
            this.systemTimeMillis = systemTimeMillis;
            this.idleTimeMillis = idleTimeMillis;
            this.iowaitTimeMillis = iowaitTimeMillis;
            this.irqTimeMillis = irqTimeMillis;
            this.softirqTimeMillis = softirqTimeMillis;
            this.stealTimeMillis = stealTimeMillis;
            this.guestTimeMillis = guestTimeMillis;
            this.guestNiceTimeMillis = guestNiceTimeMillis;
        }

        public long getTotalTimeMillis() {
            return this.userTimeMillis + this.niceTimeMillis + this.systemTimeMillis + this.idleTimeMillis + this.iowaitTimeMillis + this.irqTimeMillis + this.softirqTimeMillis + this.stealTimeMillis + this.guestTimeMillis + this.guestNiceTimeMillis;
        }

        public java.lang.String toString() {
            return "CpuUsageStats{ userTimeMillis = " + this.userTimeMillis + ", niceTimeMillis = " + this.niceTimeMillis + ", systemTimeMillis = " + this.systemTimeMillis + ", idleTimeMillis = " + this.idleTimeMillis + ", iowaitTimeMillis = " + this.iowaitTimeMillis + ", irqTimeMillis = " + this.irqTimeMillis + ", softirqTimeMillis = " + this.softirqTimeMillis + ", stealTimeMillis = " + this.stealTimeMillis + ", guestTimeMillis = " + this.guestTimeMillis + ", guestNiceTimeMillis = " + this.guestNiceTimeMillis + " }";
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof com.android.server.cpu.CpuInfoReader.CpuUsageStats)) {
                return false;
            }
            com.android.server.cpu.CpuInfoReader.CpuUsageStats other = (com.android.server.cpu.CpuInfoReader.CpuUsageStats) obj;
            return this.userTimeMillis == other.userTimeMillis && this.niceTimeMillis == other.niceTimeMillis && this.systemTimeMillis == other.systemTimeMillis && this.idleTimeMillis == other.idleTimeMillis && this.iowaitTimeMillis == other.iowaitTimeMillis && this.irqTimeMillis == other.irqTimeMillis && this.softirqTimeMillis == other.softirqTimeMillis && this.stealTimeMillis == other.stealTimeMillis && this.guestTimeMillis == other.guestTimeMillis && this.guestNiceTimeMillis == other.guestNiceTimeMillis;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Long.valueOf(this.userTimeMillis), java.lang.Long.valueOf(this.niceTimeMillis), java.lang.Long.valueOf(this.systemTimeMillis), java.lang.Long.valueOf(this.idleTimeMillis), java.lang.Long.valueOf(this.iowaitTimeMillis), java.lang.Long.valueOf(this.irqTimeMillis), java.lang.Long.valueOf(this.softirqTimeMillis), java.lang.Long.valueOf(this.stealTimeMillis), java.lang.Long.valueOf(this.guestTimeMillis), java.lang.Long.valueOf(this.guestNiceTimeMillis));
        }

        com.android.server.cpu.CpuInfoReader.CpuUsageStats delta(com.android.server.cpu.CpuInfoReader.CpuUsageStats rhs) {
            return new com.android.server.cpu.CpuInfoReader.CpuUsageStats(diff(this.userTimeMillis, rhs.userTimeMillis), diff(this.niceTimeMillis, rhs.niceTimeMillis), diff(this.systemTimeMillis, rhs.systemTimeMillis), diff(this.idleTimeMillis, rhs.idleTimeMillis), diff(this.iowaitTimeMillis, rhs.iowaitTimeMillis), diff(this.irqTimeMillis, rhs.irqTimeMillis), diff(this.softirqTimeMillis, rhs.softirqTimeMillis), diff(this.stealTimeMillis, rhs.stealTimeMillis), diff(this.guestTimeMillis, rhs.guestTimeMillis), diff(this.guestNiceTimeMillis, rhs.guestNiceTimeMillis));
        }

        private static long diff(long lhs, long rhs) {
            if (lhs > rhs) {
                return lhs - rhs;
            }
            return 0L;
        }
    }

    private static final class StaticPolicyInfo {
        public final android.util.IntArray relatedCpuCores;

        StaticPolicyInfo(android.util.IntArray relatedCpuCores) {
            this.relatedCpuCores = relatedCpuCores;
        }

        public java.lang.String toString() {
            return "StaticPolicyInfo{relatedCpuCores = " + this.relatedCpuCores + '}';
        }
    }

    private static final class DynamicPolicyInfo {
        public final android.util.IntArray affectedCpuCores;
        public final long avgTimeInStateCpuFreqKHz;
        public final long curCpuFreqKHz;
        public final long maxCpuFreqKHz;

        DynamicPolicyInfo(long curCpuFreqKHz, long maxCpuFreqKHz, long avgTimeInStateCpuFreqKHz, android.util.IntArray affectedCpuCores) {
            this.curCpuFreqKHz = curCpuFreqKHz;
            this.maxCpuFreqKHz = maxCpuFreqKHz;
            this.avgTimeInStateCpuFreqKHz = avgTimeInStateCpuFreqKHz;
            this.affectedCpuCores = affectedCpuCores;
        }

        public java.lang.String toString() {
            return "DynamicPolicyInfo{curCpuFreqKHz = " + this.curCpuFreqKHz + ", maxCpuFreqKHz = " + this.maxCpuFreqKHz + ", avgTimeInStateCpuFreqKHz = " + this.avgTimeInStateCpuFreqKHz + ", affectedCpuCores = " + this.affectedCpuCores + '}';
        }
    }
}
