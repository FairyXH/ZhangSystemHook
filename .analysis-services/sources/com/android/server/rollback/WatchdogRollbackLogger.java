package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
public final class WatchdogRollbackLogger {
    private static final java.lang.String LOGGING_PARENT_KEY = "android.content.pm.LOGGING_PARENT";
    private static final java.lang.String TAG = "WatchdogRollbackLogger";

    private WatchdogRollbackLogger() {
    }

    private static java.lang.String getLoggingParentName(android.content.Context context, java.lang.String packageName) {
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        try {
            android.content.pm.ApplicationInfo ai = packageManager.getPackageInfo(packageName, 1073741952).applicationInfo;
            if (ai.metaData == null) {
                return null;
            }
            return ai.metaData.getString(LOGGING_PARENT_KEY);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unable to discover logging parent package: " + packageName, e);
            return null;
        }
    }

    static android.content.pm.VersionedPackage getLogPackage(android.content.Context context, android.content.pm.VersionedPackage failingPackage) {
        java.lang.String logPackageName = getLoggingParentName(context, failingPackage.getPackageName());
        if (logPackageName == null) {
            return null;
        }
        try {
            android.content.pm.VersionedPackage loggingParent = new android.content.pm.VersionedPackage(logPackageName, context.getPackageManager().getPackageInfo(logPackageName, 0).getLongVersionCode());
            return loggingParent;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private static java.util.Set<android.content.pm.VersionedPackage> getLogPackages(android.content.Context context, java.util.List<java.lang.String> failedPackageNames) {
        java.util.Set<android.content.pm.VersionedPackage> parentPackages = new android.util.ArraySet<>();
        for (java.lang.String failedPackageName : failedPackageNames) {
            parentPackages.add(getLogPackage(context, new android.content.pm.VersionedPackage(failedPackageName, 0)));
        }
        return parentPackages;
    }

    static void logRollbackStatusOnBoot(android.content.Context context, int rollbackId, java.lang.String logPackageName, java.util.List<android.content.rollback.RollbackInfo> recentlyCommittedRollbacks) {
        android.content.pm.PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        android.content.rollback.RollbackInfo rollback = null;
        java.util.Iterator<android.content.rollback.RollbackInfo> it = recentlyCommittedRollbacks.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.content.rollback.RollbackInfo info = it.next();
            if (rollbackId == info.getRollbackId()) {
                rollback = info;
                break;
            }
        }
        if (rollback == null) {
            android.util.Slog.e(TAG, "rollback info not found for last staged rollback: " + rollbackId);
            return;
        }
        android.content.pm.VersionedPackage oldLoggingPackage = null;
        if (!android.text.TextUtils.isEmpty(logPackageName)) {
            java.util.Iterator it2 = rollback.getPackages().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                android.content.rollback.PackageRollbackInfo packageRollback = (android.content.rollback.PackageRollbackInfo) it2.next();
                if (logPackageName.equals(packageRollback.getPackageName())) {
                    oldLoggingPackage = packageRollback.getVersionRolledBackFrom();
                    break;
                }
            }
        }
        int sessionId = rollback.getCommittedSessionId();
        android.content.pm.PackageInstaller.SessionInfo sessionInfo = packageInstaller.getSessionInfo(sessionId);
        if (sessionInfo == null) {
            android.util.Slog.e(TAG, "On boot completed, could not load session id " + sessionId);
        } else if (sessionInfo.isStagedSessionApplied()) {
            logEvent(oldLoggingPackage, 2, 0, "");
        } else if (sessionInfo.isStagedSessionFailed()) {
            logEvent(oldLoggingPackage, 3, 0, "");
        }
    }

    public static void logApexdRevert(android.content.Context context, java.util.List<java.lang.String> failedPackageNames, java.lang.String failingNativeProcess) {
        java.util.Set<android.content.pm.VersionedPackage> logPackages = getLogPackages(context, failedPackageNames);
        for (android.content.pm.VersionedPackage logPackage : logPackages) {
            logEvent(logPackage, 2, 5, failingNativeProcess);
        }
    }

    public static void logEvent(android.content.pm.VersionedPackage logPackage, int type, int rollbackReason, java.lang.String failingPackageName) {
        android.util.Slog.i(TAG, "Watchdog event occurred with type: " + rollbackTypeToString(type) + " logPackage: " + logPackage + " rollbackReason: " + rollbackReasonToString(rollbackReason) + " failedPackageName: " + failingPackageName);
        if (logPackage != null) {
            com.android.server.crashrecovery.proto.CrashRecoveryStatsLog.write(147, type, logPackage.getPackageName(), logPackage.getVersionCode(), rollbackReason, failingPackageName, new byte[0]);
        } else {
            com.android.server.crashrecovery.proto.CrashRecoveryStatsLog.write(147, type, "", 0, rollbackReason, failingPackageName, new byte[0]);
        }
        logTestProperties(logPackage, type, rollbackReason, failingPackageName);
    }

    private static void logTestProperties(android.content.pm.VersionedPackage logPackage, int type, int rollbackReason, java.lang.String failingPackageName) {
        if (!android.os.SystemProperties.getBoolean("persist.sys.rollbacktest.enabled", false)) {
            return;
        }
        java.lang.String key = "persist.sys.rollbacktest." + rollbackTypeToString(type);
        android.os.SystemProperties.set(key, java.lang.String.valueOf(true));
        android.os.SystemProperties.set(key + ".logPackage", logPackage != null ? logPackage.toString() : "");
        android.os.SystemProperties.set(key + ".rollbackReason", rollbackReasonToString(rollbackReason));
        android.os.SystemProperties.set(key + ".failedPackageName", failingPackageName);
    }

    static int mapFailureReasonToMetric(int failureReason) {
        switch (failureReason) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 7;
            default:
                return 0;
        }
    }

    private static java.lang.String rollbackTypeToString(int type) {
        switch (type) {
            case 1:
                return "ROLLBACK_INITIATE";
            case 2:
                return "ROLLBACK_SUCCESS";
            case 3:
                return "ROLLBACK_FAILURE";
            case 4:
                return "ROLLBACK_BOOT_TRIGGERED";
            default:
                return "UNKNOWN";
        }
    }

    private static java.lang.String rollbackReasonToString(int reason) {
        switch (reason) {
            case 1:
                return "REASON_NATIVE_CRASH";
            case 2:
                return "REASON_EXPLICIT_HEALTH_CHECK";
            case 3:
                return "REASON_APP_CRASH";
            case 4:
                return "REASON_APP_NOT_RESPONDING";
            case 5:
                return "REASON_NATIVE_CRASH_DURING_BOOT";
            case 6:
            default:
                return "UNKNOWN";
            case 7:
                return "REASON_BOOT_LOOP";
        }
    }
}
