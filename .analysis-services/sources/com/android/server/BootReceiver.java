package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class BootReceiver extends android.content.BroadcastReceiver {
    private static final java.lang.String E2FSCK_FS_MODIFIED = "FILE SYSTEM WAS MODIFIED";
    private static final java.lang.String ERROR_REPORT_TRACE_PIPE = "/sys/kernel/tracing/instances/bootreceiver/trace_pipe";
    private static final java.lang.String F2FS_FSCK_FS_MODIFIED = "[FSCK] Unreachable";
    private static final java.lang.String FSCK_PASS_PATTERN = "Pass ([1-9]E?):";
    private static final java.lang.String FSCK_TREE_OPTIMIZATION_PATTERN = "Inode [0-9]+ extent tree.*could be shorter";
    private static final int FS_STAT_FSCK_FS_FIXED = 1024;
    private static final java.lang.String FS_STAT_PATTERN = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)";
    private static final int GMSCORE_LASTK_LOG_SIZE = 196608;
    private static final int LASTK_LOG_SIZE;
    private static final java.lang.String LAST_HEADER_FILE = "last-header.txt";
    private static final java.lang.String[] LAST_KMSG_FILES;
    private static final java.lang.String LAST_SHUTDOWN_TIME_PATTERN = "powerctl_shutdown_time_ms:([0-9]+):([0-9]+)";
    private static final java.lang.String LOG_FILES_FILE = "log-files.xml";
    private static final int LOG_SIZE = 4194304;
    private static final int MAX_ERROR_REPORTS = 8;
    private static final long MAX_TOMBSTONE_SIZE_BYTES;
    private static final java.lang.String METRIC_SHUTDOWN_TIME_START = "begin_shutdown";
    private static final java.lang.String METRIC_SYSTEM_SERVER = "shutdown_system_server";
    private static final java.lang.String[] MOUNT_DURATION_PROPS_POSTFIX;
    private static final java.lang.String OLD_UPDATER_CLASS = "com.google.android.systemupdater.SystemUpdateReceiver";
    private static final java.lang.String OLD_UPDATER_PACKAGE = "com.google.android.systemupdater";
    private static final java.lang.String SHUTDOWN_METRICS_FILE = "/data/system/shutdown-metrics.txt";
    private static final java.lang.String SHUTDOWN_TRON_METRICS_PREFIX = "shutdown_";
    private static final java.lang.String TAG = "BootReceiver";
    private static final java.lang.String TAG_TOMBSTONE = "SYSTEM_TOMBSTONE";
    private static final java.lang.String TAG_TOMBSTONE_PROTO = "SYSTEM_TOMBSTONE_PROTO";
    private static final java.lang.String TAG_TOMBSTONE_PROTO_WITH_HEADERS = "SYSTEM_TOMBSTONE_PROTO_WITH_HEADERS";
    private static final java.lang.String TAG_TRUNCATED = "[[TRUNCATED]]\n";
    private static final java.io.File TOMBSTONE_TMP_DIR;
    private static final int UMOUNT_STATUS_NOT_AVAILABLE = 4;
    private static final java.io.File lastHeaderFile;
    public static com.android.server.IBootReceiverExt mBootReceiverExt;
    private static com.android.server.BootReceiver.BootReceiverWrapper mBootReceiverWrapper;
    private static final com.android.server.am.DropboxRateLimiter sDropboxRateLimiter;
    private static final android.util.AtomicFile sFile;
    private static int sSentReports;

    static {
        LASTK_LOG_SIZE = android.os.SystemProperties.getInt("ro.debuggable", 0) == 1 ? GMSCORE_LASTK_LOG_SIZE : 65536;
        TOMBSTONE_TMP_DIR = new java.io.File("/data/tombstones");
        sFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), LOG_FILES_FILE), "log-files");
        lastHeaderFile = new java.io.File(android.os.Environment.getDataSystemDirectory(), LAST_HEADER_FILE);
        MOUNT_DURATION_PROPS_POSTFIX = new java.lang.String[]{"early", "default", "late"};
        LAST_KMSG_FILES = new java.lang.String[]{"/sys/fs/pstore/console-ramoops", "/proc/last_kmsg"};
        sSentReports = 0;
        mBootReceiverExt = (com.android.server.IBootReceiverExt) system.ext.loader.core.ExtLoader.type(com.android.server.IBootReceiverExt.class).create();
        MAX_TOMBSTONE_SIZE_BYTES = com.android.server.DropBoxManagerService.DEFAULT_QUOTA_KB * 1024;
        sDropboxRateLimiter = new com.android.server.am.DropboxRateLimiter();
        mBootReceiverWrapper = new com.android.server.BootReceiver.BootReceiverWrapper();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.server.BootReceiver$1] */
    @Override // android.content.BroadcastReceiver
    public void onReceive(final android.content.Context context, android.content.Intent intent) {
        mBootReceiverExt.init(context);
        new java.lang.Thread() { // from class: com.android.server.BootReceiver.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                com.android.server.BootReceiver.mBootReceiverExt.incrementCriticalDataAndRecordRebootBlocked();
                com.android.server.BootReceiver.mBootReceiverExt.notifyOTAUpdateResult(context);
                try {
                    com.android.server.BootReceiver.this.logBootEvents(context);
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.BootReceiver.TAG, "Can't log boot events", e);
                }
                try {
                    com.android.server.BootReceiver.this.removeOldUpdatePackages(context);
                } catch (java.lang.Exception e2) {
                    android.util.Slog.e(com.android.server.BootReceiver.TAG, "Can't remove old update packages", e2);
                }
                com.android.server.BootReceiver.mBootReceiverExt.syncCacheToEmmc();
                com.android.server.BootReceiver.mBootReceiverExt.initPowerkeyMonitor();
            }
        }.start();
        try {
            java.io.FileDescriptor tracefd = android.system.Os.open(ERROR_REPORT_TRACE_PIPE, android.system.OsConstants.O_RDONLY, com.android.internal.util.FrameworkStatsLog.NON_A11Y_TOOL_SERVICE_WARNING_REPORT);
            android.os.MessageQueue.OnFileDescriptorEventListener traceCallback = new android.os.MessageQueue.OnFileDescriptorEventListener() { // from class: com.android.server.BootReceiver.2
                final int mBufferSize = 1024;
                byte[] mTraceBuffer = new byte[1024];

                @Override // android.os.MessageQueue.OnFileDescriptorEventListener
                public int onFileDescriptorEvents(java.io.FileDescriptor fd, int events) {
                    try {
                        int nbytes = android.system.Os.read(fd, this.mTraceBuffer, 0, 1024);
                        if (nbytes > 0) {
                            java.lang.String readStr = new java.lang.String(this.mTraceBuffer);
                            if (readStr.indexOf("\n") != -1 && com.android.server.BootReceiver.sSentReports < 8) {
                                android.os.SystemProperties.set("dmesgd.start", "1");
                                com.android.server.BootReceiver.sSentReports++;
                            }
                        }
                        return 1;
                    } catch (java.lang.Exception e) {
                        android.util.Slog.wtf(com.android.server.BootReceiver.TAG, "Error watching for trace events", e);
                        return 0;
                    }
                }
            };
            com.android.server.IoThread.get().getLooper().getQueue().addOnFileDescriptorEventListener(tracefd, 1, traceCallback);
        } catch (android.system.ErrnoException e) {
            android.util.Slog.wtf(TAG, "Could not open /sys/kernel/tracing/instances/bootreceiver/trace_pipe", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOldUpdatePackages(android.content.Context context) {
        android.provider.Downloads.removeAllDownloadsByPackage(context, OLD_UPDATER_PACKAGE, OLD_UPDATER_CLASS);
    }

    private static java.lang.String getPreviousBootHeaders() {
        try {
            return android.os.FileUtils.readTextFile(lastHeaderFile, 0, null);
        } catch (java.io.IOException e) {
            return null;
        }
    }

    private static java.lang.String getCurrentBootHeaders() throws java.io.IOException {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(512).append("Build: ").append(android.os.Build.FINGERPRINT).append("\n").append("Hardware: ").append(android.os.Build.BOARD).append("\n").append("Revision: ").append(android.os.SystemProperties.get("ro.revision", "")).append("\n").append("Bootloader: ").append(android.os.Build.BOOTLOADER).append("\n").append("Radio: ").append(android.os.Build.getRadioVersion()).append("\n").append("Kernel: ").append(android.os.FileUtils.readTextFile(new java.io.File("/proc/version"), 1024, "...\n"));
        long pageSize = android.system.Os.sysconf(android.system.OsConstants._SC_PAGESIZE);
        if (pageSize != 4096) {
            builder.append("PageSize: ").append(pageSize).append("\n");
        }
        builder.append("\n");
        return builder.toString();
    }

    private static java.lang.String getBootHeadersToLogAndUpdate() throws java.io.IOException {
        java.lang.String oldHeaders = getPreviousBootHeaders();
        java.lang.String newHeaders = getCurrentBootHeaders();
        try {
            android.os.FileUtils.stringToFile(lastHeaderFile, newHeaders);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error writing " + lastHeaderFile, e);
        }
        if (oldHeaders == null) {
            return "isPrevious: false\n" + newHeaders;
        }
        return "isPrevious: true\n" + oldHeaders;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logBootEvents(android.content.Context ctx) throws java.io.IOException {
        android.os.DropBoxManager db = (android.os.DropBoxManager) ctx.getSystemService("dropbox");
        java.lang.String headers = getBootHeadersToLogAndUpdate();
        java.lang.String bootReason = android.os.SystemProperties.get("ro.boot.bootreason", (java.lang.String) null);
        java.lang.String recovery = android.os.RecoverySystem.handleAftermath(ctx);
        if (recovery != null && db != null) {
            db.addText("SYSTEM_RECOVERY_LOG", headers + recovery);
        }
        if (bootReason != null) {
            new java.lang.StringBuilder(512).append("\n").append("Boot info:\n").append("Last boot reason: ").append(bootReason).append("\n").toString();
        }
        java.util.HashMap<java.lang.String, java.lang.Long> timestamps = readTimestamps();
        if (android.os.SystemProperties.getLong("ro.runtime.firstboot", 0L) == 0) {
            java.lang.String now = java.lang.Long.toString(java.lang.System.currentTimeMillis());
            android.os.SystemProperties.set("ro.runtime.firstboot", now);
            if (db != null) {
                db.addText("SYSTEM_BOOT", headers);
            }
            mBootReceiverExt.recordAbnormalRestart(db);
            addFileToDropBox(db, timestamps, headers, "/cache/recovery/last_kmsg", -4194304, "SYSTEM_RECOVERY_KMSG");
            addAuditErrorsToDropBox(db, timestamps, headers, -4194304, "SYSTEM_AUDIT");
        } else {
            if (db != null) {
                db.addText("SYSTEM_RESTART", headers);
            }
            mBootReceiverExt.addFile(db, timestamps, headers, ctx);
        }
        logFsShutdownTime();
        logFsMountTime();
        addFsckErrorsToDropBoxAndLogFsStat(db, timestamps, headers, -4194304, "SYSTEM_FSCK");
        logSystemServerShutdownTimeMetrics();
        writeTimestamps(timestamps);
    }

    public static void initDropboxRateLimiter() {
        sDropboxRateLimiter.init();
    }

    public static void resetDropboxRateLimiter() {
        sDropboxRateLimiter.reset();
    }

    public static void addTombstoneToDropBox(android.content.Context ctx, java.io.File tombstone, boolean proto, java.lang.String processName, java.util.concurrent.locks.ReentrantLock tmpFileLock) {
        android.os.DropBoxManager db = (android.os.DropBoxManager) ctx.getSystemService(android.os.DropBoxManager.class);
        if (db == null) {
            android.util.Slog.e(TAG, "Can't log tombstone: DropBoxManager not available");
            return;
        }
        com.android.server.am.DropboxRateLimiter.RateLimitResult rateLimitResult = sDropboxRateLimiter.shouldRateLimit(proto ? TAG_TOMBSTONE_PROTO_WITH_HEADERS : TAG_TOMBSTONE, processName);
        if (rateLimitResult.shouldRateLimit()) {
            return;
        }
        java.util.HashMap<java.lang.String, java.lang.Long> timestamps = readTimestamps();
        try {
            if (proto) {
                if (recordFileTimestamp(tombstone, timestamps)) {
                    tmpFileLock.lock();
                    try {
                        addAugmentedProtoToDropbox(tombstone, db, rateLimitResult);
                        tmpFileLock.unlock();
                    } catch (java.lang.Throwable th) {
                        tmpFileLock.unlock();
                        throw th;
                    }
                }
            } else {
                java.lang.String headers = getBootHeadersToLogAndUpdate() + rateLimitResult.createHeader();
                addFileToDropBox(db, timestamps, headers, tombstone.getPath(), 4194304, TAG_TOMBSTONE);
            }
            mBootReceiverExt.hookAddTombstoneToDropBox(tombstone.getPath());
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Can't log tombstone", e);
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG, "Can't log tombstone", e2);
        }
        writeTimestamps(timestamps);
    }

    private static void addAugmentedProtoToDropbox(java.io.File tombstone, android.os.DropBoxManager db, com.android.server.am.DropboxRateLimiter.RateLimitResult rateLimitResult) throws java.io.IOException {
        if (tombstone.length() > MAX_TOMBSTONE_SIZE_BYTES) {
            android.util.Slog.w(TAG, "Tombstone too large to add to DropBox: " + tombstone.toPath());
            return;
        }
        byte[] tombstoneBytes = java.nio.file.Files.readAllBytes(tombstone.toPath());
        java.io.File tombstoneProtoWithHeaders = java.io.File.createTempFile(tombstone.getName(), ".tmp", TOMBSTONE_TMP_DIR);
        java.nio.file.Files.setPosixFilePermissions(tombstoneProtoWithHeaders.toPath(), java.nio.file.attribute.PosixFilePermissions.fromString("rw-rw----"));
        try {
            try {
                android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(tombstoneProtoWithHeaders, 805306368);
                try {
                    android.util.proto.ProtoOutputStream protoStream = new android.util.proto.ProtoOutputStream(pfd.getFileDescriptor());
                    protoStream.write(1151051235329L, tombstoneBytes);
                    protoStream.write(1120986464258L, rateLimitResult.droppedCountSinceRateLimitActivated());
                    protoStream.flush();
                    db.addFile(TAG_TOMBSTONE_PROTO_WITH_HEADERS, tombstoneProtoWithHeaders, 0);
                    if (pfd != null) {
                        pfd.close();
                    }
                    if (tombstoneProtoWithHeaders == null) {
                        return;
                    }
                } catch (java.lang.Throwable th) {
                    if (pfd != null) {
                        try {
                            pfd.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.io.FileNotFoundException ex) {
                android.util.Slog.e(TAG, "failed to open for write: " + tombstoneProtoWithHeaders, ex);
                throw ex;
            } catch (java.io.IOException ex2) {
                android.util.Slog.e(TAG, "IO exception during write: " + tombstoneProtoWithHeaders, ex2);
                if (tombstoneProtoWithHeaders == null) {
                    return;
                }
            }
            tombstoneProtoWithHeaders.delete();
        } catch (java.lang.Throwable th3) {
            if (tombstoneProtoWithHeaders != null) {
                tombstoneProtoWithHeaders.delete();
            }
            throw th3;
        }
    }

    private static void addLastkToDropBox(android.os.DropBoxManager db, java.util.HashMap<java.lang.String, java.lang.Long> timestamps, java.lang.String headers, java.lang.String footers, java.lang.String filename, int maxSize, java.lang.String tag) throws java.io.IOException {
        int extraSize = headers.length() + TAG_TRUNCATED.length() + footers.length();
        if (LASTK_LOG_SIZE + extraSize > GMSCORE_LASTK_LOG_SIZE) {
            if (GMSCORE_LASTK_LOG_SIZE > extraSize) {
                maxSize = -(GMSCORE_LASTK_LOG_SIZE - extraSize);
            } else {
                maxSize = 0;
            }
        }
        addFileWithFootersToDropBox(db, timestamps, headers, footers, filename, maxSize, tag);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addFileToDropBox(android.os.DropBoxManager db, java.util.HashMap<java.lang.String, java.lang.Long> timestamps, java.lang.String headers, java.lang.String filename, int maxSize, java.lang.String tag) throws java.io.IOException {
        addFileWithFootersToDropBox(db, timestamps, headers, "", filename, maxSize, tag);
    }

    private static void addFileWithFootersToDropBox(android.os.DropBoxManager db, java.util.HashMap<java.lang.String, java.lang.Long> timestamps, java.lang.String headers, java.lang.String footers, java.lang.String filename, int maxSize, java.lang.String tag) throws java.io.IOException {
        if (db == null || !db.isTagEnabled(tag)) {
            return;
        }
        java.io.File file = new java.io.File(filename);
        long fileTime = file.lastModified();
        if (fileTime <= 0) {
            return;
        }
        if (timestamps.containsKey(filename) && timestamps.get(filename).longValue() == fileTime && !tag.equals("SYSTEM_TOMBSTONE_CRASH")) {
            return;
        }
        if (!recordFileTimestamp(file, timestamps) && (!tag.equals("SYSTEM_TOMBSTONE_CRASH") || file.lastModified() <= 0)) {
            return;
        }
        java.lang.String fileContents = android.os.FileUtils.readTextFile(file, maxSize, TAG_TRUNCATED);
        java.lang.String text = headers + fileContents + footers;
        if (tag.equals(TAG_TOMBSTONE) && fileContents.contains(">>> system_server <<<")) {
            addTextToDropBox(db, "system_server_native_crash", text, filename, maxSize);
        }
        if (tag.equals(TAG_TOMBSTONE)) {
            com.android.internal.util.FrameworkStatsLog.write(186);
        }
        addTextToDropBox(db, tag, text, filename, maxSize);
    }

    private static boolean recordFileTimestamp(java.io.File file, java.util.HashMap<java.lang.String, java.lang.Long> timestamps) {
        long fileTime = file.lastModified();
        if (fileTime <= 0) {
            return false;
        }
        java.lang.String filename = file.getPath();
        if (timestamps.containsKey(filename) && timestamps.get(filename).longValue() == fileTime) {
            return false;
        }
        timestamps.put(filename, java.lang.Long.valueOf(fileTime));
        return true;
    }

    private static void addTextToDropBox(android.os.DropBoxManager db, java.lang.String tag, java.lang.String text, java.lang.String filename, int maxSize) {
        android.util.Slog.i(TAG, "Copying " + filename + " to DropBox (" + tag + ")");
        db.addText(tag, text);
        android.util.EventLog.writeEvent(81002, filename, java.lang.Integer.valueOf(maxSize), tag);
    }

    private static void addAuditErrorsToDropBox(android.os.DropBoxManager db, java.util.HashMap<java.lang.String, java.lang.Long> timestamps, java.lang.String headers, int maxSize, java.lang.String tag) throws java.io.IOException {
        if (db != null && db.isTagEnabled(tag)) {
            android.util.Slog.i(TAG, "Copying audit failures to DropBox");
            java.io.File file = new java.io.File("/proc/last_kmsg");
            long fileTime = file.lastModified();
            if (fileTime <= 0) {
                file = new java.io.File("/sys/fs/pstore/console-ramoops");
                fileTime = file.lastModified();
                if (fileTime <= 0) {
                    file = new java.io.File("/sys/fs/pstore/console-ramoops-0");
                    fileTime = file.lastModified();
                }
            }
            if (fileTime <= 0) {
                return;
            }
            if (timestamps.containsKey(tag) && timestamps.get(tag).longValue() == fileTime) {
                return;
            }
            timestamps.put(tag, java.lang.Long.valueOf(fileTime));
            java.lang.String log = android.os.FileUtils.readTextFile(file, maxSize, TAG_TRUNCATED);
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (java.lang.String line : log.split("\n")) {
                if (line.contains("audit")) {
                    sb.append(line + "\n");
                }
            }
            android.util.Slog.i(TAG, "Copied " + sb.toString().length() + " worth of audits to DropBox");
            db.addText(tag, headers + sb.toString());
        }
    }

    private static void addFsckErrorsToDropBoxAndLogFsStat(android.os.DropBoxManager db, java.util.HashMap<java.lang.String, java.lang.Long> timestamps, java.lang.String headers, int maxSize, java.lang.String tag) throws java.io.IOException {
        int lastFsStatLineNumber;
        boolean uploadEnabled = db != null && db.isTagEnabled(tag);
        android.util.Slog.i(TAG, "Checking for fsck errors");
        java.io.File file = new java.io.File("/dev/fscklogs/log");
        long fileTime = file.lastModified();
        if (fileTime <= 0) {
            return;
        }
        java.lang.String log = android.os.FileUtils.readTextFile(file, maxSize, TAG_TRUNCATED);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(FS_STAT_PATTERN);
        java.lang.String[] lines = log.split("\n");
        int lastFsStatLineNumber2 = 0;
        int length = lines.length;
        boolean uploadNeeded = false;
        int i = 0;
        int lineNumber = 0;
        while (i < length) {
            java.lang.String line = lines[i];
            int i2 = length;
            if (line.contains(E2FSCK_FS_MODIFIED) || line.contains(F2FS_FSCK_FS_MODIFIED)) {
                int lastFsStatLineNumber3 = lastFsStatLineNumber2;
                uploadNeeded = true;
                lastFsStatLineNumber2 = lastFsStatLineNumber3;
                lineNumber++;
                i++;
                length = i2;
            } else {
                if (!line.contains("fs_stat")) {
                    lastFsStatLineNumber = lastFsStatLineNumber2;
                } else {
                    java.util.regex.Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        handleFsckFsStat(matcher, lines, lastFsStatLineNumber2, lineNumber);
                        lastFsStatLineNumber2 = lineNumber;
                        lineNumber++;
                        i++;
                        length = i2;
                    } else {
                        lastFsStatLineNumber = lastFsStatLineNumber2;
                        android.util.Slog.w(TAG, "cannot parse fs_stat:" + line);
                    }
                }
                lastFsStatLineNumber2 = lastFsStatLineNumber;
                lineNumber++;
                i++;
                length = i2;
            }
        }
        if (uploadEnabled && uploadNeeded) {
            addFileToDropBox(db, timestamps, headers, "/dev/fscklogs/log", maxSize, tag);
        }
        java.io.File pfile = new java.io.File("/dev/fscklogs/fsck");
        file.renameTo(pfile);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void logFsMountTime() {
        /*
            java.lang.String[] r0 = com.android.server.BootReceiver.MOUNT_DURATION_PROPS_POSTFIX
            int r1 = r0.length
            r2 = 0
            r3 = r2
        L5:
            if (r3 >= r1) goto L60
            r4 = r0[r3]
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "ro.boottime.init.mount_all."
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r4)
            java.lang.String r5 = r5.toString()
            int r5 = android.os.SystemProperties.getInt(r5, r2)
            if (r5 == 0) goto L5d
            int r6 = r4.hashCode()
            switch(r6) {
                case 3314342: goto L3f;
                case 96278371: goto L35;
                case 1544803905: goto L2b;
                default: goto L2a;
            }
        L2a:
            goto L4a
        L2b:
            java.lang.String r6 = "default"
            boolean r6 = r4.equals(r6)
            if (r6 == 0) goto L2a
            r6 = 1
            goto L4b
        L35:
            java.lang.String r6 = "early"
            boolean r6 = r4.equals(r6)
            if (r6 == 0) goto L2a
            r6 = r2
            goto L4b
        L3f:
            java.lang.String r6 = "late"
            boolean r6 = r4.equals(r6)
            if (r6 == 0) goto L2a
            r6 = 2
            goto L4b
        L4a:
            r6 = -1
        L4b:
            switch(r6) {
                case 0: goto L55;
                case 1: goto L52;
                case 2: goto L4f;
                default: goto L4e;
            }
        L4e:
            goto L5d
        L4f:
            r6 = 12
            goto L58
        L52:
            r6 = 10
            goto L58
        L55:
            r6 = 11
        L58:
            r7 = 239(0xef, float:3.35E-43)
            com.android.internal.util.FrameworkStatsLog.write(r7, r6, r5)
        L5d:
            int r3 = r3 + 1
            goto L5
        L60:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.logFsMountTime():void");
    }

    private static void logSystemServerShutdownTimeMetrics() {
        java.io.File metricsFile = new java.io.File(SHUTDOWN_METRICS_FILE);
        java.lang.String metricsStr = null;
        if (metricsFile.exists()) {
            try {
                metricsStr = android.os.FileUtils.readTextFile(metricsFile, 0, null);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Problem reading " + metricsFile, e);
            }
        }
        if (!android.text.TextUtils.isEmpty(metricsStr)) {
            java.lang.String reboot = null;
            java.lang.String reason = null;
            java.lang.String start_time = null;
            java.lang.String duration = null;
            java.lang.String[] array = metricsStr.split(",");
            for (java.lang.String keyValueStr : array) {
                java.lang.String[] keyValue = keyValueStr.split(":");
                if (keyValue.length != 2) {
                    android.util.Slog.e(TAG, "Wrong format of shutdown metrics - " + metricsStr);
                } else {
                    if (keyValue[0].startsWith(SHUTDOWN_TRON_METRICS_PREFIX)) {
                        logTronShutdownMetric(keyValue[0], keyValue[1]);
                        if (keyValue[0].equals(METRIC_SYSTEM_SERVER)) {
                            duration = keyValue[1];
                        }
                    }
                    if (keyValue[0].equals("reboot")) {
                        reboot = keyValue[1];
                    } else if (keyValue[0].equals(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY)) {
                        reason = keyValue[1];
                    } else if (keyValue[0].equals(METRIC_SHUTDOWN_TIME_START)) {
                        start_time = keyValue[1];
                    }
                }
            }
            logStatsdShutdownAtom(reboot, reason, start_time, duration);
        }
        metricsFile.delete();
    }

    private static void logTronShutdownMetric(java.lang.String metricName, java.lang.String valueStr) {
        try {
            int value = java.lang.Integer.parseInt(valueStr);
            if (value >= 0) {
                com.android.internal.logging.MetricsLogger.histogram((android.content.Context) null, metricName, value);
            }
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Cannot parse metric " + metricName + " int value - " + valueStr);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x004d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0074 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void logStatsdShutdownAtom(java.lang.String r17, java.lang.String r18, java.lang.String r19, java.lang.String r20) {
        /*
            r1 = r17
            r2 = r19
            r0 = 0
            java.lang.String r3 = "<EMPTY>"
            r4 = 0
            r6 = 0
            java.lang.String r8 = "BootReceiver"
            if (r1 == 0) goto L3b
            java.lang.String r9 = "y"
            boolean r9 = r1.equals(r9)
            if (r9 == 0) goto L1b
            r0 = 1
            r9 = r0
            goto L41
        L1b:
            java.lang.String r9 = "n"
            boolean r9 = r1.equals(r9)
            if (r9 != 0) goto L40
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Unexpected value for reboot : "
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.StringBuilder r9 = r9.append(r1)
            java.lang.String r9 = r9.toString()
            android.util.Slog.e(r8, r9)
            goto L40
        L3b:
            java.lang.String r9 = "No value received for reboot"
            android.util.Slog.e(r8, r9)
        L40:
            r9 = r0
        L41:
            if (r18 == 0) goto L46
            r3 = r18
            goto L4b
        L46:
            java.lang.String r0 = "No value received for shutdown reason"
            android.util.Slog.e(r8, r0)
        L4b:
            if (r2 == 0) goto L6d
            long r10 = java.lang.Long.parseLong(r19)     // Catch: java.lang.NumberFormatException -> L53
            r4 = r10
        L52:
            goto L72
        L53:
            r0 = move-exception
            r10 = r0
            r0 = r10
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Cannot parse shutdown start time: "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.String r10 = r10.toString()
            android.util.Slog.e(r8, r10)
            goto L52
        L6d:
            java.lang.String r0 = "No value received for shutdown start time"
            android.util.Slog.e(r8, r0)
        L72:
            if (r20 == 0) goto L94
            long r10 = java.lang.Long.parseLong(r20)     // Catch: java.lang.NumberFormatException -> L7a
            r6 = r10
        L79:
            goto L99
        L7a:
            r0 = move-exception
            r10 = r0
            r0 = r10
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Cannot parse shutdown duration: "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.String r10 = r10.toString()
            android.util.Slog.e(r8, r10)
            goto L79
        L94:
            java.lang.String r0 = "No value received for shutdown duration"
            android.util.Slog.e(r8, r0)
        L99:
            r10 = 56
            r11 = r9
            r12 = r3
            r13 = r4
            r15 = r6
            com.android.internal.util.FrameworkStatsLog.write(r10, r11, r12, r13, r15)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.logStatsdShutdownAtom(java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    private static void logFsShutdownTime() {
        java.io.File f = null;
        java.lang.String[] strArr = LAST_KMSG_FILES;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            java.lang.String fileName = strArr[i];
            java.io.File file = new java.io.File(fileName);
            if (!file.exists()) {
                i++;
            } else {
                f = file;
                break;
            }
        }
        if (f == null) {
            return;
        }
        try {
            java.lang.String lines = android.os.FileUtils.readTextFile(f, -16384, null);
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(LAST_SHUTDOWN_TIME_PATTERN, 8);
            java.util.regex.Matcher matcher = pattern.matcher(lines);
            if (matcher.find()) {
                com.android.internal.util.FrameworkStatsLog.write(239, 9, java.lang.Integer.parseInt(matcher.group(1)));
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ERROR_CODE_REPORTED, 2, java.lang.Integer.parseInt(matcher.group(2)));
                android.util.Slog.i(TAG, "boot_fs_shutdown," + matcher.group(1) + "," + matcher.group(2));
            } else {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ERROR_CODE_REPORTED, 2, 4);
                android.util.Slog.w(TAG, "boot_fs_shutdown, string not found");
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "cannot read last msg", e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x009c, code lost:
    
        r8 = true;
        r9 = r14;
        r6 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0156, code lost:
    
        r6 = r16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int fixFsckFsStat(java.lang.String r18, int r19, java.lang.String[] r20, int r21, int r22) {
        /*
            Method dump skipped, instruction units count: 445
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.fixFsckFsStat(java.lang.String, int, java.lang.String[], int, int):int");
    }

    private static void handleFsckFsStat(java.util.regex.Matcher match, java.lang.String[] lines, int startLineNumber, int endLineNumber) {
        java.lang.String partition = match.group(1);
        try {
            int stat = fixFsckFsStat(partition, java.lang.Integer.decode(match.group(2)).intValue(), lines, startLineNumber, endLineNumber);
            if ("userdata".equals(partition) || "data".equals(partition)) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ERROR_CODE_REPORTED, 3, stat);
            }
            android.util.Slog.i(TAG, "fs_stat, partition:" + partition + " stat:0x" + java.lang.Integer.toHexString(stat));
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.w(TAG, "cannot parse fs_stat: partition:" + partition + " stat:" + match.group(2));
        }
    }

    private static java.util.HashMap<java.lang.String, java.lang.Long> readTimestamps() {
        java.util.HashMap<java.lang.String, java.lang.Long> timestamps;
        java.io.FileInputStream stream;
        com.android.modules.utils.TypedXmlPullParser parser;
        int type;
        synchronized (sFile) {
            timestamps = new java.util.HashMap<>();
            boolean success = false;
            try {
                try {
                    try {
                        try {
                            try {
                                try {
                                    stream = sFile.openRead();
                                    try {
                                        parser = android.util.Xml.resolvePullParser(stream);
                                        do {
                                            type = parser.next();
                                            if (type == 2) {
                                                break;
                                            }
                                        } while (type != 1);
                                    } catch (java.lang.Throwable th) {
                                        if (stream != null) {
                                            try {
                                                stream.close();
                                            } catch (java.lang.Throwable th2) {
                                                th.addSuppressed(th2);
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (java.io.IOException e) {
                                    android.util.Slog.w(TAG, "Failed parsing " + e);
                                    if (!success) {
                                    }
                                }
                            } catch (java.io.FileNotFoundException e2) {
                                android.util.Slog.i(TAG, "No existing last log timestamp file " + sFile.getBaseFile() + "; starting empty");
                                if (!success) {
                                }
                            }
                        } catch (java.lang.NullPointerException e3) {
                            android.util.Slog.w(TAG, "Failed parsing " + e3);
                            if (!success) {
                            }
                        }
                    } catch (org.xmlpull.v1.XmlPullParserException e4) {
                        android.util.Slog.w(TAG, "Failed parsing " + e4);
                        if (!success) {
                        }
                    }
                } finally {
                    if (!success) {
                        timestamps.clear();
                    }
                }
            } catch (java.lang.IllegalStateException e5) {
                android.util.Slog.w(TAG, "Failed parsing " + e5);
                if (!success) {
                }
            }
            if (type != 2) {
                throw new java.lang.IllegalStateException("no start tag found");
            }
            int outerDepth = parser.getDepth();
            while (true) {
                int type2 = parser.next();
                if (type2 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                    break;
                }
                if (type2 != 3 && type2 != 4) {
                    java.lang.String tagName = parser.getName();
                    if (tagName.equals("log")) {
                        java.lang.String filename = parser.getAttributeValue((java.lang.String) null, "filename");
                        long timestamp = parser.getAttributeLong((java.lang.String) null, com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP);
                        timestamps.put(filename, java.lang.Long.valueOf(timestamp));
                    } else {
                        android.util.Slog.w(TAG, "Unknown tag: " + parser.getName());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
            success = true;
            if (stream != null) {
                stream.close();
            }
        }
        return timestamps;
    }

    private static void writeTimestamps(java.util.HashMap<java.lang.String, java.lang.Long> timestamps) {
        synchronized (sFile) {
            try {
                try {
                    java.io.FileOutputStream stream = sFile.startWrite();
                    try {
                        com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
                        out.startDocument((java.lang.String) null, true);
                        out.startTag((java.lang.String) null, "log-files");
                        for (java.lang.String filename : timestamps.keySet()) {
                            out.startTag((java.lang.String) null, "log");
                            out.attribute((java.lang.String) null, "filename", filename);
                            out.attributeLong((java.lang.String) null, com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, timestamps.get(filename).longValue());
                            out.endTag((java.lang.String) null, "log");
                        }
                        out.endTag((java.lang.String) null, "log-files");
                        out.endDocument();
                        sFile.finishWrite(stream);
                    } catch (java.io.IOException e) {
                        android.util.Slog.w(TAG, "Failed to write timestamp file, using the backup: " + e);
                        sFile.failWrite(stream);
                    }
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(TAG, "Failed to write timestamp file: " + e2);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    public static com.android.server.BootReceiver.BootReceiverWrapper getWrapper() {
        return mBootReceiverWrapper;
    }

    public static class BootReceiverWrapper implements com.android.server.IBootReceiverWrapper {
        @Override // com.android.server.IBootReceiverWrapper
        public void addFileToDropBox(android.os.DropBoxManager db, java.util.HashMap<java.lang.String, java.lang.Long> timestamps, java.lang.String headers, java.lang.String filename, int maxSize, java.lang.String tag) {
            try {
                com.android.server.BootReceiver.addFileToDropBox(db, timestamps, headers, filename, maxSize, tag);
            } catch (java.io.IOException e) {
                android.util.Slog.w(com.android.server.BootReceiver.TAG, "Failed to add file to dropBox: " + e);
            }
        }
    }
}
