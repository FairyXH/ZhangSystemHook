package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class BackupManagerMonitorDumpsysUtils {
    private static final java.lang.String BACKUP_PERSISTENT_DIR = "backup";
    private static final long BMM_FILE_SIZE_LIMIT_BYTES = 25600000;
    private static final java.lang.String INITIAL_SETUP_TIMESTAMP_KEY = "initialSetupTimestamp";
    private static final long LOGS_RETENTION_PERIOD_MILLISEC = java.util.concurrent.TimeUnit.DAYS.toMillis(1) * 60;
    private static final java.lang.String TAG = "BackupManagerMonitorDumpsysUtils";
    private boolean mIsAfterRetentionPeriod;
    private boolean mIsAfterRetentionPeriodCached = false;
    private boolean mIsFileLargerThanSizeLimit = false;

    public void parseBackupManagerMonitorRestoreEventForDumpsys(android.os.Bundle eventBundle) {
        if (isAfterRetentionPeriod() || eventBundle == null || !isOpTypeRestore(eventBundle)) {
            return;
        }
        if (!eventBundle.containsKey("android.app.backup.extra.LOG_EVENT_ID") || !eventBundle.containsKey("android.app.backup.extra.LOG_EVENT_CATEGORY")) {
            android.util.Slog.w(TAG, "Event id and category are not optional fields.");
            return;
        }
        java.io.File bmmEvents = getBMMEventsFile();
        if (bmmEvents.length() == 0) {
            recordSetUpTimestamp();
        }
        if (isFileLargerThanSizeLimit(bmmEvents)) {
            return;
        }
        try {
            java.io.FileOutputStream out = new java.io.FileOutputStream(bmmEvents, true);
            try {
                com.android.internal.util.FastPrintWriter fastPrintWriter = new com.android.internal.util.FastPrintWriter(out);
                try {
                    eventBundle.getInt("android.app.backup.extra.LOG_EVENT_CATEGORY");
                    int eventId = eventBundle.getInt("android.app.backup.extra.LOG_EVENT_ID");
                    if (eventId != 52 || hasAgentLogging(eventBundle)) {
                        fastPrintWriter.println("[" + timestamp() + "] - " + getId(eventId));
                        if (eventBundle.containsKey("android.app.backup.extra.LOG_EVENT_PACKAGE_NAME")) {
                            fastPrintWriter.println("\tPackage: " + eventBundle.getString("android.app.backup.extra.LOG_EVENT_PACKAGE_NAME"));
                        }
                        addAgentLogsIfAvailable(eventBundle, fastPrintWriter);
                        addExtrasIfAvailable(eventBundle, fastPrintWriter);
                        fastPrintWriter.close();
                        out.close();
                        return;
                    }
                    fastPrintWriter.close();
                    out.close();
                    return;
                } finally {
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "IO Exception when writing BMM events to file: " + e);
        }
    }

    private boolean hasAgentLogging(android.os.Bundle eventBundle) {
        if (eventBundle.containsKey("android.app.backup.extra.LOG_AGENT_LOGGING_RESULTS")) {
            java.util.ArrayList<android.app.backup.BackupRestoreEventLogger.DataTypeResult> agentLogs = eventBundle.getParcelableArrayList("android.app.backup.extra.LOG_AGENT_LOGGING_RESULTS");
            return !agentLogs.isEmpty();
        }
        return false;
    }

    private void addAgentLogsIfAvailable(android.os.Bundle eventBundle, java.io.PrintWriter pw) {
        if (hasAgentLogging(eventBundle)) {
            pw.println("\tAgent Logs:");
            java.util.ArrayList<android.app.backup.BackupRestoreEventLogger.DataTypeResult> agentLogs = eventBundle.getParcelableArrayList("android.app.backup.extra.LOG_AGENT_LOGGING_RESULTS");
            for (android.app.backup.BackupRestoreEventLogger.DataTypeResult result : agentLogs) {
                int totalItems = result.getFailCount() + result.getSuccessCount();
                pw.println("\t\tData Type: " + result.getDataType());
                pw.println("\t\t\tItem restored: " + result.getSuccessCount() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + totalItems);
                for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : result.getErrors().entrySet()) {
                    pw.println("\t\t\tAgent Error - Category: " + entry.getKey() + ", Count: " + entry.getValue());
                }
            }
        }
    }

    private void addExtrasIfAvailable(android.os.Bundle eventBundle, java.io.PrintWriter pw) {
        if (eventBundle.getInt("android.app.backup.extra.LOG_EVENT_ID") == 27) {
            if (eventBundle.containsKey("android.app.backup.extra.LOG_RESTORE_ANYWAY")) {
                pw.println("\t\tPackage supports RestoreAnyVersion: " + eventBundle.getBoolean("android.app.backup.extra.LOG_RESTORE_ANYWAY"));
            }
            if (eventBundle.containsKey("android.app.backup.extra.LOG_RESTORE_VERSION")) {
                pw.println("\t\tPackage version on source: " + eventBundle.getLong("android.app.backup.extra.LOG_RESTORE_VERSION"));
            }
            if (eventBundle.containsKey("android.app.backup.extra.LOG_EVENT_PACKAGE_FULL_VERSION")) {
                pw.println("\t\tPackage version on target: " + eventBundle.getLong("android.app.backup.extra.LOG_EVENT_PACKAGE_FULL_VERSION"));
            }
        }
        if (eventBundle.getInt("android.app.backup.extra.LOG_EVENT_ID") == 72) {
            if (eventBundle.containsKey("android.app.backup.extra.V_TO_U_DENYLIST")) {
                pw.println("\t\tV to U Denylist : " + eventBundle.getString("android.app.backup.extra.V_TO_U_DENYLIST"));
            }
            if (eventBundle.containsKey("android.app.backup.extra.V_TO_U_ALLOWLIST")) {
                pw.println("\t\tV to U Allowllist : " + eventBundle.getString("android.app.backup.extra.V_TO_U_ALLOWLIST"));
            }
        }
    }

    public java.io.File getBMMEventsFile() {
        java.io.File dataDir = new java.io.File(android.os.Environment.getDataDirectory(), "backup");
        java.io.File fname = new java.io.File(dataDir, "bmmevents.txt");
        return fname;
    }

    public boolean isFileLargerThanSizeLimit(java.io.File events) {
        if (!this.mIsFileLargerThanSizeLimit) {
            this.mIsFileLargerThanSizeLimit = events.length() > getBMMEventsFileSizeLimit();
        }
        return this.mIsFileLargerThanSizeLimit;
    }

    private java.lang.String timestamp() {
        long currentTime = java.lang.System.currentTimeMillis();
        java.util.Date date = new java.util.Date(currentTime);
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(date);
    }

    private java.lang.String getCategory(int code) {
        switch (code) {
            case 1:
                return "Transport";
            case 2:
                return "Agent";
            case 3:
                return "Backup Manager Policy";
            default:
                java.lang.String category = "Unknown category code: " + code;
                return category;
        }
    }

    private java.lang.String getId(int code) {
        switch (code) {
            case 4:
                return "Full backup cancel";
            case 5:
                return "Illegal key";
            case 6:
            case 8:
            case 17:
            case 20:
            case 32:
            case 33:
            default:
                java.lang.String id = "Unknown log event ID: " + code;
                return id;
            case 7:
                return "No data to send";
            case 9:
                return "Package ineligible";
            case 10:
                return "Package key-value participant";
            case 11:
                return "Package stopped";
            case 12:
                return "Package not found";
            case 13:
                return "Backup disabled";
            case 14:
                return "Device not provisioned";
            case 15:
                return "Package transport not present";
            case 16:
                return "Error preflight";
            case 18:
                return "Quota hit preflight";
            case 19:
                return "Exception full backup";
            case 21:
                return "Key-value backup cancel";
            case 22:
                return "No restore metadata available";
            case 23:
                return "No PM metadata received";
            case 24:
                return "PM agent has no metadata";
            case 25:
                return "Lost transport";
            case 26:
                return "Package not present";
            case 27:
                return "Restore version higher";
            case 28:
                return "App has no agent";
            case 29:
                return "Signature mismatch";
            case 30:
                return "Can't find agent";
            case 31:
                return "Key-value restore timeout";
            case 34:
                return "Restore any version";
            case 35:
                return "Versions match";
            case 36:
                return "Version of backup older";
            case 37:
                return "Full restore signature mismatch";
            case 38:
                return "System app no agent";
            case 39:
                return "Full restore allow backup false";
            case 40:
                return "APK not installed";
            case 41:
                return "Cannot restore without APK";
            case 42:
                return "Missing signature";
            case 43:
                return "Expected different package";
            case 44:
                return "Unknown version";
            case 45:
                return "Full restore timeout";
            case 46:
                return "Corrupt manifest";
            case 47:
                return "Widget metadata mismatch";
            case 48:
                return "Widget unknown version";
            case 49:
                return "No packages";
            case 50:
                return "Transport is null";
            case 51:
                return "Transport non-incremental backup required";
            case 52:
                return "Agent logging results";
            case 53:
                return "Start system restore";
            case 54:
                return "Start restore at install";
            case 55:
                return "Transport error during start restore";
            case 56:
                return "Cannot get next package name";
            case 57:
                return "Unknown restore type";
            case 58:
                return "KV restore";
            case 59:
                return "Full restore";
            case 60:
                return "No next restore target";
            case 61:
                return "KV agent error";
            case 62:
                return "Package restore finished";
            case 63:
                return "Transport error KV restore";
            case 64:
                return "No feeder thread";
            case 65:
                return "Full agent error";
            case 66:
                return "Transport error full restore";
            case 67:
                return "Start package restore";
            case 68:
                return "Restore complete";
            case 69:
                return "Agent failure";
            case 70:
                return "V to U restore pkg eligible";
            case 71:
                return "V to U restore pkg not eligible";
            case 72:
                return "V to U restore lists";
            case 73:
                return "Invoked restore at install";
            case 74:
                return "Skip restore at install";
            case 75:
                return "Pkg accepted for restore";
            case 76:
                return "Restore data does not belong to package";
            case 77:
                return "Unable to create Agent";
            case 78:
                return "Agent crashed before restore data is streamed";
            case 79:
                return "Failed to send data to agent";
            case 80:
                return "Agent failure during restore";
            case 81:
                return "Failed to read data from Transport";
        }
    }

    private boolean isOpTypeRestore(android.os.Bundle eventBundle) {
        switch (eventBundle.getInt("android.app.backup.extra.OPERATION_TYPE", -1)) {
            case 1:
                return true;
            default:
                return false;
        }
    }

    void recordSetUpTimestamp() {
        java.io.File setupDateFile = getSetUpDateFile();
        if (setupDateFile.length() == 0) {
            try {
                java.io.FileOutputStream out = new java.io.FileOutputStream(setupDateFile, true);
                try {
                    com.android.internal.util.FastPrintWriter fastPrintWriter = new com.android.internal.util.FastPrintWriter(out);
                    try {
                        long currentDate = java.lang.System.currentTimeMillis();
                        fastPrintWriter.println(currentDate);
                        fastPrintWriter.close();
                        out.close();
                    } finally {
                    }
                } finally {
                }
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "An error occurred while recording the setup date: " + e.getMessage());
            }
        }
    }

    java.lang.String getSetUpDate() {
        java.io.File fname = getSetUpDateFile();
        try {
            java.io.FileInputStream inputStream = new java.io.FileInputStream(fname);
            try {
                java.io.InputStreamReader reader = new java.io.InputStreamReader(inputStream);
                try {
                    java.io.BufferedReader bufferedReader = new java.io.BufferedReader(reader);
                    try {
                        java.lang.String line = bufferedReader.readLine();
                        bufferedReader.close();
                        reader.close();
                        inputStream.close();
                        return line;
                    } finally {
                    }
                } finally {
                }
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "An error occurred while reading the date: " + e.getMessage());
            return "Could not retrieve setup date";
        }
    }

    static boolean isDateAfterNMillisec(long startTimeStamp, long endTimeStamp, long millisec) {
        if (startTimeStamp > endTimeStamp) {
            return true;
        }
        long timeDifferenceMillis = endTimeStamp - startTimeStamp;
        return timeDifferenceMillis >= millisec;
    }

    boolean isAfterRetentionPeriod() {
        if (this.mIsAfterRetentionPeriodCached) {
            return this.mIsAfterRetentionPeriod;
        }
        java.io.File setUpDateFile = getSetUpDateFile();
        if (setUpDateFile.length() == 0) {
            this.mIsAfterRetentionPeriod = false;
            this.mIsAfterRetentionPeriodCached = true;
            return false;
        }
        try {
            long setupTimestamp = java.lang.Long.parseLong(getSetUpDate());
            long currentTimestamp = java.lang.System.currentTimeMillis();
            this.mIsAfterRetentionPeriod = isDateAfterNMillisec(setupTimestamp, currentTimestamp, getRetentionPeriodInMillisec());
            this.mIsAfterRetentionPeriodCached = true;
            return this.mIsAfterRetentionPeriod;
        } catch (java.lang.NumberFormatException e) {
            this.mIsAfterRetentionPeriod = true;
            this.mIsAfterRetentionPeriodCached = true;
            return true;
        }
    }

    java.io.File getSetUpDateFile() {
        java.io.File dataDir = new java.io.File(android.os.Environment.getDataDirectory(), "backup");
        java.io.File setupDateFile = new java.io.File(dataDir, "initialSetupTimestamp.txt");
        return setupDateFile;
    }

    long getRetentionPeriodInMillisec() {
        return LOGS_RETENTION_PERIOD_MILLISEC;
    }

    long getBMMEventsFileSizeLimit() {
        return BMM_FILE_SIZE_LIMIT_BYTES;
    }

    public boolean deleteExpiredBMMEvents() {
        try {
            if (isAfterRetentionPeriod()) {
                java.io.File bmmEvents = getBMMEventsFile();
                if (bmmEvents.exists()) {
                    if (bmmEvents.delete()) {
                        android.util.Slog.i(TAG, "Deleted expired BMM Events");
                    } else {
                        android.util.Slog.e(TAG, "Unable to delete expired BMM Events");
                    }
                }
                return true;
            }
            return false;
        } catch (java.lang.Exception e) {
            return true;
        }
    }
}
