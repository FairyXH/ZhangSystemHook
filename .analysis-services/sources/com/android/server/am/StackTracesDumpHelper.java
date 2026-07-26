package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class StackTracesDumpHelper {
    static final java.lang.String ANR_FILE_PREFIX = "anr_";
    static final java.lang.String ANR_TEMP_FILE_PREFIX = "temp_anr_";
    public static final java.lang.String ANR_TRACE_DIR = "/data/anr";
    private static final int JAVA_DUMP_MINIMUM_SIZE = 100;
    static final java.lang.String TAG = "ActivityManager";
    public com.android.server.am.IStackTracesDumpHelperExt mExt = (com.android.server.am.IStackTracesDumpHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IStackTracesDumpHelperExt.class).create();
    private static final java.text.SimpleDateFormat ANR_FILE_DATE_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
    private static final int NATIVE_DUMP_TIMEOUT_MS = android.os.Build.HW_TIMEOUT_MULTIPLIER * 2000;
    private static final int TEMP_DUMP_TIME_LIMIT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 10000;
    public static com.android.server.am.IStackTracesDumpHelperExt.IStaticExt mStaticExt = (com.android.server.am.IStackTracesDumpHelperExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IStackTracesDumpHelperExt.IStaticExt.class).create();

    public static java.io.File dumpStackTraces(java.util.ArrayList<java.lang.Integer> firstPids, com.android.internal.os.ProcessCpuTracker processCpuTracker, android.util.SparseBooleanArray lastPids, java.util.concurrent.Future<java.util.ArrayList<java.lang.Integer>> nativePidsFuture, java.io.StringWriter logExceptionCreatingFile, java.util.concurrent.Executor auxiliaryTaskExecutor, com.android.internal.os.anr.AnrLatencyTracker latencyTracker) {
        return dumpStackTraces(firstPids, processCpuTracker, lastPids, nativePidsFuture, logExceptionCreatingFile, null, null, null, null, auxiliaryTaskExecutor, null, latencyTracker);
    }

    public static java.io.File dumpStackTraces(java.util.ArrayList<java.lang.Integer> firstPids, com.android.internal.os.ProcessCpuTracker processCpuTracker, android.util.SparseBooleanArray lastPids, java.util.concurrent.Future<java.util.ArrayList<java.lang.Integer>> nativePidsFuture, java.io.StringWriter logExceptionCreatingFile, java.lang.String subject, java.lang.String criticalEventSection, java.util.concurrent.Executor auxiliaryTaskExecutor, com.android.internal.os.anr.AnrLatencyTracker latencyTracker) {
        return dumpStackTraces(firstPids, processCpuTracker, lastPids, nativePidsFuture, logExceptionCreatingFile, null, subject, criticalEventSection, null, auxiliaryTaskExecutor, null, latencyTracker);
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x0121  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.io.File dumpStackTraces(java.util.ArrayList<java.lang.Integer> r20, final com.android.internal.os.ProcessCpuTracker r21, final android.util.SparseBooleanArray r22, java.util.concurrent.Future<java.util.ArrayList<java.lang.Integer>> r23, java.io.StringWriter r24, java.util.concurrent.atomic.AtomicLong r25, java.lang.String r26, java.lang.String r27, java.lang.String r28, java.util.concurrent.Executor r29, java.util.concurrent.Future<java.io.File> r30, final com.android.internal.os.anr.AnrLatencyTracker r31) throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 293
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.StackTracesDumpHelper.dumpStackTraces(java.util.ArrayList, com.android.internal.os.ProcessCpuTracker, android.util.SparseBooleanArray, java.util.concurrent.Future, java.io.StringWriter, java.util.concurrent.atomic.AtomicLong, java.lang.String, java.lang.String, java.lang.String, java.util.concurrent.Executor, java.util.concurrent.Future, com.android.internal.os.anr.AnrLatencyTracker):java.io.File");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0116 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0131  */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v5, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static long dumpStackTraces(java.lang.String r25, java.util.ArrayList<java.lang.Integer> r26, java.util.concurrent.Future<java.util.ArrayList<java.lang.Integer>> r27, java.util.concurrent.Future<java.util.ArrayList<java.lang.Integer>> r28, java.util.concurrent.Future<java.io.File> r29, com.android.internal.os.anr.AnrLatencyTracker r30) throws java.util.concurrent.ExecutionException, java.lang.InterruptedException {
        /*
            Method dump skipped, instruction units count: 874
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.StackTracesDumpHelper.dumpStackTraces(java.lang.String, java.util.ArrayList, java.util.concurrent.Future, java.util.concurrent.Future, java.util.concurrent.Future, com.android.internal.os.anr.AnrLatencyTracker):long");
    }

    public static java.io.File dumpStackTracesTempFile(int pid, com.android.internal.os.anr.AnrLatencyTracker latencyTracker) {
        if (latencyTracker != null) {
            try {
                latencyTracker.dumpStackTracesTempFileStarted();
            } catch (java.lang.Throwable th) {
                if (latencyTracker != null) {
                    latencyTracker.dumpStackTracesTempFileEnded();
                }
                throw th;
            }
        }
        try {
            java.io.File tmpTracesFile = java.io.File.createTempFile(ANR_TEMP_FILE_PREFIX, ".txt", new java.io.File(ANR_TRACE_DIR));
            android.util.Slog.d("ActivityManager", "created ANR temporary file:" + tmpTracesFile.getAbsolutePath());
            android.util.Slog.i("ActivityManager", "Collecting stacks for pid " + pid + " into temporary file " + tmpTracesFile.getName());
            if (latencyTracker != null) {
                latencyTracker.dumpingPidStarted(pid);
            }
            long timeTaken = dumpJavaTracesTombstoned(pid, tmpTracesFile.getAbsolutePath(), TEMP_DUMP_TIME_LIMIT);
            if (latencyTracker != null) {
                latencyTracker.dumpingPidEnded();
            }
            if (TEMP_DUMP_TIME_LIMIT <= timeTaken) {
                android.util.Slog.e("ActivityManager", "Aborted stack trace dump (current primary pid=" + pid + "); deadline exceeded.");
                if (latencyTracker != null) {
                    latencyTracker.dumpStackTracesTempFileTimedOut();
                }
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ANR) {
                android.util.Slog.d("ActivityManager", "Done with primary pid " + pid + " in " + timeTaken + "ms dumped into temporary file " + tmpTracesFile.getName());
            }
            if (latencyTracker != null) {
                latencyTracker.dumpStackTracesTempFileEnded();
            }
            return tmpTracesFile;
        } catch (java.io.IOException e) {
            android.util.Slog.w("ActivityManager", "Exception creating temporary ANR dump file:", e);
            if (latencyTracker != null) {
                latencyTracker.dumpStackTracesTempFileCreationFailed();
            }
            if (latencyTracker == null) {
                return null;
            }
            latencyTracker.dumpStackTracesTempFileEnded();
            return null;
        }
    }

    private static boolean copyFirstPidTempDump(java.lang.String tracesFile, java.util.concurrent.Future<java.io.File> firstPidFilePromise, long timeLimitMs, com.android.internal.os.anr.AnrLatencyTracker latencyTracker) {
        try {
            try {
                try {
                    try {
                        try {
                            java.io.FileOutputStream fos = new java.io.FileOutputStream(tracesFile, true);
                            if (latencyTracker != null) {
                                try {
                                    latencyTracker.copyingFirstPidStarted();
                                } catch (java.lang.Throwable th) {
                                    try {
                                        fos.close();
                                    } catch (java.lang.Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                    throw th;
                                }
                            }
                            java.io.File tempfile = firstPidFilePromise.get(timeLimitMs, java.util.concurrent.TimeUnit.MILLISECONDS);
                            if (tempfile == null) {
                                fos.close();
                                if (latencyTracker != null) {
                                    latencyTracker.copyingFirstPidEnded(false);
                                }
                                return false;
                            }
                            java.nio.file.Files.copy(tempfile.toPath(), fos);
                            tempfile.delete();
                            fos.close();
                            if (latencyTracker != null) {
                                latencyTracker.copyingFirstPidEnded(true);
                            }
                            return true;
                        } catch (java.util.concurrent.ExecutionException e) {
                            android.util.Slog.w("ActivityManager", "Failed to collect the first pid's predump to the main ANR file", e.getCause());
                            if (latencyTracker != null) {
                                latencyTracker.copyingFirstPidEnded(false);
                            }
                            return false;
                        }
                    } catch (java.util.concurrent.TimeoutException e2) {
                        android.util.Slog.w("ActivityManager", "Copying the first pid timed out", e2);
                        if (latencyTracker != null) {
                            latencyTracker.copyingFirstPidEnded(false);
                        }
                        return false;
                    }
                } catch (java.lang.InterruptedException e3) {
                    android.util.Slog.w("ActivityManager", "Interrupted while collecting the first pid's predump to the main ANR file", e3);
                    if (latencyTracker != null) {
                        latencyTracker.copyingFirstPidEnded(false);
                    }
                    return false;
                }
            } catch (java.io.IOException e4) {
                android.util.Slog.w("ActivityManager", "Failed to read the first pid's predump file", e4);
                if (latencyTracker != null) {
                    latencyTracker.copyingFirstPidEnded(false);
                }
                return false;
            }
        } catch (java.lang.Throwable th3) {
            if (latencyTracker != null) {
                latencyTracker.copyingFirstPidEnded(false);
            }
            throw th3;
        }
    }

    private static synchronized java.io.File createAnrDumpFile(java.io.File tracesDir, int anrPid) throws java.io.IOException {
        java.io.File anrFile;
        java.lang.String formattedDate = ANR_FILE_DATE_FORMAT.format(new java.util.Date());
        java.lang.String anrPidString = java.lang.Integer.toString(anrPid);
        anrFile = new java.io.File(tracesDir, ANR_FILE_PREFIX + anrPidString + "_" + formattedDate);
        if (anrFile.createNewFile()) {
            android.os.FileUtils.setPermissions(anrFile.getAbsolutePath(), com.android.internal.util.FrameworkStatsLog.NON_A11Y_TOOL_SERVICE_WARNING_REPORT, -1, -1);
        } else {
            throw new java.io.IOException("Unable to create ANR dump file: createNewFile failed");
        }
        return anrFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.util.ArrayList<java.lang.Integer> getExtraPids(com.android.internal.os.ProcessCpuTracker processCpuTracker, android.util.SparseBooleanArray lastPids, com.android.internal.os.anr.AnrLatencyTracker latencyTracker) {
        if (latencyTracker != null) {
            latencyTracker.processCpuTrackerMethodsCalled();
        }
        java.util.ArrayList<java.lang.Integer> extraPids = new java.util.ArrayList<>();
        synchronized (processCpuTracker) {
            processCpuTracker.init();
        }
        try {
            java.lang.Thread.sleep(200L);
        } catch (java.lang.InterruptedException e) {
        }
        synchronized (processCpuTracker) {
            processCpuTracker.update();
            int workingStatsNumber = processCpuTracker.countWorkingStats();
            for (int i = 0; i < workingStatsNumber && extraPids.size() < 2; i++) {
                com.android.internal.os.ProcessCpuTracker.Stats stats = processCpuTracker.getWorkingStats(i);
                if (lastPids.indexOfKey(stats.pid) >= 0) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ANR) {
                        android.util.Slog.d("ActivityManager", "Collecting stacks for extra pid " + stats.pid);
                    }
                    extraPids.add(java.lang.Integer.valueOf(stats.pid));
                } else {
                    android.util.Slog.i("ActivityManager", "Skipping next CPU consuming process, not a java proc: " + stats.pid);
                }
            }
        }
        if (latencyTracker != null) {
            latencyTracker.processCpuTrackerMethodsReturned();
        }
        return extraPids;
    }

    private static void maybePruneOldTraces(java.io.File tracesDir) {
        java.io.File[] files = tracesDir.listFiles();
        if (files == null) {
            return;
        }
        int max = android.os.SystemProperties.getInt("tombstoned.max_anr_count", 64);
        long now = java.lang.System.currentTimeMillis();
        try {
            java.util.Arrays.sort(files, java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.am.StackTracesDumpHelper$$ExternalSyntheticLambda0
                @Override // java.util.function.ToLongFunction
                public final long applyAsLong(java.lang.Object obj) {
                    return ((java.io.File) obj).lastModified();
                }
            }).reversed());
        } catch (java.lang.Exception e) {
            android.util.Slog.w("ActivityManager", "Unexpected exception when sorting anr trace files", e);
        }
        for (int i = 0; i < files.length; i++) {
            try {
                if ((i > max || now - files[i].lastModified() > 86400000) && !files[i].delete()) {
                    android.util.Slog.w("ActivityManager", "Unable to prune stale trace file: " + files[i]);
                }
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.w("ActivityManager", "tombstone modification times changed while sorting; not pruning", e2);
                return;
            }
        }
    }

    private static long dumpJavaTracesTombstoned(int pid, java.lang.String fileName, long timeoutMs, com.android.internal.os.anr.AnrLatencyTracker latencyTracker) {
        if (latencyTracker != null) {
            try {
                latencyTracker.dumpingPidStarted(pid);
            } finally {
                if (latencyTracker != null) {
                    latencyTracker.dumpingPidEnded();
                }
            }
        }
        return dumpJavaTracesTombstoned(pid, fileName, timeoutMs);
    }

    private static long dumpJavaTracesTombstoned(int pid, java.lang.String fileName, long timeoutMs) {
        if (android.os.Process.getThreadGroupLeader(pid) != pid) {
            android.util.Slog.w("ActivityManager", pid + " is reused by others, skip dump trace");
            return 0L;
        }
        long timeStart = android.os.SystemClock.elapsedRealtime();
        int headerSize = writeUptimeStartHeaderForPid(pid, fileName);
        boolean javaSuccess = android.os.Debug.dumpJavaBacktraceToFileTimeout(pid, fileName, (int) (timeoutMs / 1000));
        if (javaSuccess) {
            try {
                long size = new java.io.File(fileName).length();
                if (size - ((long) headerSize) < 100) {
                    android.util.Slog.w("ActivityManager", "Successfully created Java ANR file is empty!");
                    javaSuccess = false;
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.w("ActivityManager", "Unable to get ANR file size", e);
                javaSuccess = false;
            }
        }
        if (!javaSuccess) {
            android.util.Slog.w("ActivityManager", "Dumping Java threads failed, initiating native stack dump.");
            if (!android.os.Debug.dumpNativeBacktraceToFileTimeout(pid, fileName, NATIVE_DUMP_TIMEOUT_MS / 1000)) {
                android.util.Slog.w("ActivityManager", "Native stack dump failed!");
            }
        }
        return android.os.SystemClock.elapsedRealtime() - timeStart;
    }

    private static int appendtoANRFile(java.lang.String fileName, java.lang.String text) {
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(fileName, true);
            try {
                byte[] header = text.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                fos.write(header);
                int length = header.length;
                fos.close();
                return length;
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w("ActivityManager", "Exception writing to ANR dump file:", e);
            return 0;
        }
    }

    private static int writeUptimeStartHeaderForPid(int pid, java.lang.String fileName) {
        return appendtoANRFile(fileName, "----- dumping pid: " + pid + " at " + android.os.SystemClock.uptimeMillis() + "\n");
    }

    private static java.util.ArrayList<java.lang.Integer> collectPids(java.util.concurrent.Future<java.util.ArrayList<java.lang.Integer>> pidsFuture, java.lang.String logName) {
        if (pidsFuture == null) {
            return null;
        }
        try {
            java.util.ArrayList<java.lang.Integer> pids = pidsFuture.get();
            return pids;
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.w("ActivityManager", "Interrupted while collecting " + logName, e);
            return null;
        } catch (java.util.concurrent.ExecutionException e2) {
            android.util.Slog.w("ActivityManager", "Failed to collect " + logName, e2.getCause());
            return null;
        }
    }
}
