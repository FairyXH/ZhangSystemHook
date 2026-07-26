package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
final class VirtualDeviceLog {
    private static final int MAX_ENTRIES = 16;
    private final android.content.Context mContext;
    private final java.util.ArrayDeque<com.android.server.companion.virtual.VirtualDeviceLog.LogEntry> mLogEntries = new java.util.ArrayDeque<>();
    public static int TYPE_CREATED = 0;
    public static int TYPE_CLOSED = 1;
    private static final java.time.format.DateTimeFormatter DATE_FORMAT = java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS").withZone(java.time.ZoneId.systemDefault());

    VirtualDeviceLog(android.content.Context context) {
        this.mContext = context;
    }

    void logCreated(int deviceId, int ownerUid) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (!com.android.server.companion.virtual.Flags.dumpHistory()) {
                return;
            }
            addEntry(new com.android.server.companion.virtual.VirtualDeviceLog.LogEntry(TYPE_CREATED, deviceId, java.lang.System.currentTimeMillis(), ownerUid));
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void logClosed(int deviceId, int ownerUid) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (!com.android.server.companion.virtual.Flags.dumpHistory()) {
                return;
            }
            addEntry(new com.android.server.companion.virtual.VirtualDeviceLog.LogEntry(TYPE_CLOSED, deviceId, java.lang.System.currentTimeMillis(), ownerUid));
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void addEntry(com.android.server.companion.virtual.VirtualDeviceLog.LogEntry entry) {
        this.mLogEntries.push(entry);
        if (this.mLogEntries.size() > 16) {
            this.mLogEntries.removeLast();
        }
    }

    void dump(java.io.PrintWriter pw) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (!com.android.server.companion.virtual.Flags.dumpHistory()) {
                return;
            }
            pw.println("VirtualDevice Log:");
            com.android.server.companion.virtual.VirtualDeviceLog.UidToPackageNameCache packageNameCache = new com.android.server.companion.virtual.VirtualDeviceLog.UidToPackageNameCache(this.mContext.getPackageManager());
            for (com.android.server.companion.virtual.VirtualDeviceLog.LogEntry logEntry : this.mLogEntries) {
                logEntry.dump(pw, "  ", packageNameCache);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    static class LogEntry {
        private final int mDeviceId;
        private final int mOwnerUid;
        private final long mTimestamp;
        private final int mType;

        LogEntry(int type, int deviceId, long timestamp, int ownerUid) {
            this.mType = type;
            this.mDeviceId = deviceId;
            this.mTimestamp = timestamp;
            this.mOwnerUid = ownerUid;
        }

        void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.companion.virtual.VirtualDeviceLog.UidToPackageNameCache packageNameCache) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(prefix);
            sb.append(com.android.server.companion.virtual.VirtualDeviceLog.DATE_FORMAT.format(java.time.Instant.ofEpochMilli(this.mTimestamp)));
            sb.append(" - ");
            sb.append(this.mType == com.android.server.companion.virtual.VirtualDeviceLog.TYPE_CREATED ? "START" : "CLOSE");
            sb.append(" Device ID: ");
            sb.append(this.mDeviceId);
            sb.append(" ");
            sb.append(this.mOwnerUid);
            sb.append(" (");
            sb.append(packageNameCache.getPackageName(this.mOwnerUid));
            sb.append(")");
            pw.println(sb);
        }
    }

    private static class UidToPackageNameCache {
        private final android.content.pm.PackageManager mPackageManager;
        private final android.util.SparseArray<java.lang.String> mUidToPackagesCache = new android.util.SparseArray<>();

        public UidToPackageNameCache(android.content.pm.PackageManager packageManager) {
            this.mPackageManager = packageManager;
        }

        java.lang.String getPackageName(int ownerUid) {
            if (this.mUidToPackagesCache.contains(ownerUid)) {
                return this.mUidToPackagesCache.get(ownerUid);
            }
            java.lang.String[] packages = this.mPackageManager.getPackagesForUid(ownerUid);
            java.lang.String packageName = "";
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
                if (packages.length > 1) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append(packageName).append(",...");
                    packageName = sb.toString();
                }
            }
            this.mUidToPackagesCache.put(ownerUid, packageName);
            return packageName;
        }
    }
}
