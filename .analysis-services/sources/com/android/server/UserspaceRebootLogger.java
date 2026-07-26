package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class UserspaceRebootLogger {
    private static final java.lang.String LAST_BOOT_REASON_PROPERTY = "sys.boot.reason.last";
    private static final java.lang.String TAG = "UserspaceRebootLogger";
    private static final java.lang.String USERSPACE_REBOOT_LAST_FINISHED_PROPERTY = "sys.userspace_reboot.log.last_finished";
    private static final java.lang.String USERSPACE_REBOOT_LAST_STARTED_PROPERTY = "sys.userspace_reboot.log.last_started";
    private static final java.lang.String USERSPACE_REBOOT_SHOULD_LOG_PROPERTY = "persist.sys.userspace_reboot.log.should_log";

    private UserspaceRebootLogger() {
    }

    public static void noteUserspaceRebootWasRequested() {
        if (!android.os.PowerManager.isRebootingUserspaceSupportedImpl()) {
            android.util.Slog.wtf(TAG, "noteUserspaceRebootWasRequested: Userspace reboot is not supported.");
        } else {
            android.os.SystemProperties.set(USERSPACE_REBOOT_SHOULD_LOG_PROPERTY, "1");
            android.os.SystemProperties.set(USERSPACE_REBOOT_LAST_STARTED_PROPERTY, java.lang.String.valueOf(android.os.SystemClock.elapsedRealtime()));
        }
    }

    public static void noteUserspaceRebootSuccess() {
        if (!android.os.PowerManager.isRebootingUserspaceSupportedImpl()) {
            android.util.Slog.wtf(TAG, "noteUserspaceRebootSuccess: Userspace reboot is not supported.");
        } else {
            android.os.SystemProperties.set(USERSPACE_REBOOT_LAST_FINISHED_PROPERTY, java.lang.String.valueOf(android.os.SystemClock.elapsedRealtime()));
        }
    }

    public static boolean shouldLogUserspaceRebootEvent() {
        if (android.os.PowerManager.isRebootingUserspaceSupportedImpl()) {
            return android.os.SystemProperties.getBoolean(USERSPACE_REBOOT_SHOULD_LOG_PROPERTY, false);
        }
        return false;
    }

    public static void logEventAsync(boolean userUnlocked, java.util.concurrent.Executor executor) {
        final long durationMillis;
        if (!android.os.PowerManager.isRebootingUserspaceSupportedImpl()) {
            android.util.Slog.wtf(TAG, "logEventAsync: Userspace reboot is not supported.");
            return;
        }
        final int outcome = computeOutcome();
        final int encryptionState = 1;
        if (outcome == 1) {
            durationMillis = android.os.SystemProperties.getLong(USERSPACE_REBOOT_LAST_FINISHED_PROPERTY, 0L) - android.os.SystemProperties.getLong(USERSPACE_REBOOT_LAST_STARTED_PROPERTY, 0L);
        } else {
            durationMillis = 0;
        }
        if (!userUnlocked) {
            encryptionState = 2;
        }
        executor.execute(new java.lang.Runnable() { // from class: com.android.server.UserspaceRebootLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.UserspaceRebootLogger.lambda$logEventAsync$0(outcome, durationMillis, encryptionState);
            }
        });
    }

    static /* synthetic */ void lambda$logEventAsync$0(int outcome, long durationMillis, int encryptionState) {
        android.util.Slog.i(TAG, "Logging UserspaceRebootReported atom: { outcome: " + outcome + " durationMillis: " + durationMillis + " encryptionState: " + encryptionState + " }");
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.USERSPACE_REBOOT_REPORTED, outcome, durationMillis, encryptionState);
        android.os.SystemProperties.set(USERSPACE_REBOOT_SHOULD_LOG_PROPERTY, "");
    }

    private static int computeOutcome() {
        if (android.os.SystemProperties.getLong(USERSPACE_REBOOT_LAST_STARTED_PROPERTY, -1L) != -1) {
            return 1;
        }
        java.lang.String reason = android.text.TextUtils.emptyIfNull(android.os.SystemProperties.get(LAST_BOOT_REASON_PROPERTY, ""));
        if (reason.startsWith("reboot,")) {
            reason = reason.substring("reboot".length());
        }
        if (reason.startsWith("userspace_failed,watchdog_fork") || reason.startsWith("userspace_failed,shutdown_aborted")) {
            return 2;
        }
        if (reason.startsWith("mount_userdata_failed") || reason.startsWith("userspace_failed,init_user0") || reason.startsWith("userspace_failed,enablefilecrypto")) {
            return 3;
        }
        if (reason.startsWith("userspace_failed,watchdog_triggered")) {
            return 4;
        }
        return 0;
    }
}
