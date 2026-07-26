package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
final class PreRebootLogger {
    private static final java.lang.String PREREBOOT_DIR = "prereboot";
    private static final java.lang.String TAG = "PreRebootLogger";
    private static final java.lang.String[] BUFFERS_TO_DUMP = {"system"};
    private static final java.lang.String[] SERVICES_TO_DUMP = {"rollback", "package"};
    private static final java.lang.Object sLock = new java.lang.Object();
    private static final long MAX_DUMP_TIME = java.util.concurrent.TimeUnit.SECONDS.toMillis(20);

    PreRebootLogger() {
    }

    static void log(android.content.Context context) {
        log(context, getDumpDir());
    }

    static void log(android.content.Context context, java.io.File dumpDir) {
        if (!needDump(context)) {
            wipe(dumpDir);
        }
    }

    private static boolean needDump(android.content.Context context) {
        return android.provider.Settings.Global.getInt(context.getContentResolver(), "adb_enabled", 0) == 1 && !context.getPackageManager().getPackageInstaller().getActiveStagedSessions().isEmpty();
    }

    static void dump(final java.io.File dumpDir, long maxWaitTime) {
        android.util.Slog.d(TAG, "Dumping pre-reboot information...");
        final java.util.concurrent.atomic.AtomicBoolean done = new java.util.concurrent.atomic.AtomicBoolean(false);
        java.lang.Thread t = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.power.PreRebootLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.power.PreRebootLogger.lambda$dump$0(dumpDir, done);
            }
        });
        t.start();
        try {
            t.join(maxWaitTime);
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.e(TAG, "Failed to dump pre-reboot information due to interrupted", e);
        }
        if (!done.get()) {
            android.util.Slog.w(TAG, "Failed to dump pre-reboot information due to timeout");
        }
    }

    static /* synthetic */ void lambda$dump$0(java.io.File dumpDir, java.util.concurrent.atomic.AtomicBoolean done) {
        synchronized (sLock) {
            for (java.lang.String buffer : BUFFERS_TO_DUMP) {
                dumpLogsLocked(dumpDir, buffer);
            }
            for (java.lang.String service : SERVICES_TO_DUMP) {
                dumpServiceLocked(dumpDir, service);
            }
        }
        done.set(true);
    }

    private static void wipe(java.io.File dumpDir) {
        android.util.Slog.d(TAG, "Wiping pre-reboot information...");
        synchronized (sLock) {
            for (java.io.File file : dumpDir.listFiles()) {
                file.delete();
            }
        }
    }

    private static java.io.File getDumpDir() {
        java.io.File dumpDir = new java.io.File(android.os.Environment.getDataMiscDirectory(), PREREBOOT_DIR);
        if (!dumpDir.exists() || !dumpDir.isDirectory()) {
            throw new java.lang.UnsupportedOperationException("Pre-reboot dump directory not found");
        }
        return dumpDir;
    }

    private static void dumpLogsLocked(java.io.File dumpDir, java.lang.String buffer) {
        try {
            java.io.File dumpFile = new java.io.File(dumpDir, buffer);
            if (dumpFile.createNewFile()) {
                dumpFile.setWritable(true, true);
            } else {
                new java.io.FileWriter(dumpFile, false).flush();
            }
            java.lang.String[] cmdline = {"logcat", "-d", "-b", buffer, "-f", dumpFile.getAbsolutePath()};
            java.lang.Runtime.getRuntime().exec(cmdline).waitFor();
        } catch (java.io.IOException | java.lang.InterruptedException e) {
            android.util.Slog.e(TAG, "Failed to dump system log buffer before reboot", e);
        }
    }

    private static void dumpServiceLocked(java.io.File dumpDir, java.lang.String serviceName) {
        android.os.IBinder binder = android.os.ServiceManager.checkService(serviceName);
        if (binder == null) {
            return;
        }
        try {
            java.io.File dumpFile = new java.io.File(dumpDir, serviceName);
            android.os.ParcelFileDescriptor fd = android.os.ParcelFileDescriptor.open(dumpFile, 738197504);
            binder.dump(fd.getFileDescriptor(), (java.lang.String[]) com.android.internal.util.ArrayUtils.emptyArray(java.lang.String.class));
        } catch (android.os.RemoteException | java.io.FileNotFoundException e) {
            android.util.Slog.e(TAG, java.lang.String.format("Failed to dump %s service before reboot", serviceName), e);
        }
    }
}
