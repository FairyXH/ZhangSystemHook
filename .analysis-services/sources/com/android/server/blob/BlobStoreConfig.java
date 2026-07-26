package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
class BlobStoreConfig {
    private static final java.lang.String BLOBS_DIR_NAME = "blobs";
    private static final java.lang.String BLOBS_INDEX_FILE_NAME = "blobs_index.xml";
    public static final int IDLE_JOB_ID = 191934935;
    public static final long INVALID_BLOB_ID = 0;
    public static final long INVALID_BLOB_SIZE = 0;
    private static final java.lang.String ROOT_DIR_NAME = "blobstore";
    private static final java.lang.String SESSIONS_INDEX_FILE_NAME = "sessions_index.xml";
    public static final int XML_VERSION_ADD_COMMIT_TIME = 4;
    public static final int XML_VERSION_ADD_DESC_RES_NAME = 3;
    public static final int XML_VERSION_ADD_SESSION_CREATION_TIME = 5;
    public static final int XML_VERSION_ADD_STRING_DESC = 2;
    public static final int XML_VERSION_ALLOW_ACCESS_ACROSS_USERS = 6;
    public static final int XML_VERSION_CURRENT = 6;
    public static final int XML_VERSION_INIT = 1;
    public static final java.lang.String TAG = "BlobStore";
    public static final boolean LOGV = android.util.Log.isLoggable(TAG, 2);

    BlobStoreConfig() {
    }

    public static class DeviceConfigProperties {
        public static final float DEFAULT_TOTAL_BYTES_PER_APP_LIMIT_FRACTION = 0.01f;
        public static final boolean DEFAULT_USE_REVOCABLE_FD_FOR_READS = false;
        public static final java.lang.String KEY_COMMIT_COOL_OFF_DURATION_MS = "commit_cool_off_duration_ms";
        public static final java.lang.String KEY_DELETE_ON_LAST_LEASE_DELAY_MS = "delete_on_last_lease_delay_ms";
        public static final java.lang.String KEY_IDLE_JOB_PERIOD_MS = "idle_job_period_ms";
        public static final java.lang.String KEY_LEASE_ACQUISITION_WAIT_DURATION_MS = "lease_acquisition_wait_time_ms";
        public static final java.lang.String KEY_LEASE_DESC_CHAR_LIMIT = "lease_desc_char_limit";
        public static final java.lang.String KEY_MAX_ACTIVE_SESSIONS = "max_active_sessions";
        public static final java.lang.String KEY_MAX_BLOB_ACCESS_PERMITTED_PACKAGES = "max_permitted_pks";
        public static final java.lang.String KEY_MAX_COMMITTED_BLOBS = "max_committed_blobs";
        public static final java.lang.String KEY_MAX_LEASED_BLOBS = "max_leased_blobs";
        public static final java.lang.String KEY_SESSION_EXPIRY_TIMEOUT_MS = "session_expiry_timeout_ms";
        public static final java.lang.String KEY_TOTAL_BYTES_PER_APP_LIMIT_FLOOR = "total_bytes_per_app_limit_floor";
        public static final java.lang.String KEY_TOTAL_BYTES_PER_APP_LIMIT_FRACTION = "total_bytes_per_app_limit_fraction";
        public static final java.lang.String KEY_USE_REVOCABLE_FD_FOR_READS = "use_revocable_fd_for_reads";
        public static final long DEFAULT_IDLE_JOB_PERIOD_MS = java.util.concurrent.TimeUnit.DAYS.toMillis(1);
        public static long IDLE_JOB_PERIOD_MS = DEFAULT_IDLE_JOB_PERIOD_MS;
        public static final long DEFAULT_SESSION_EXPIRY_TIMEOUT_MS = java.util.concurrent.TimeUnit.DAYS.toMillis(7);
        public static long SESSION_EXPIRY_TIMEOUT_MS = DEFAULT_SESSION_EXPIRY_TIMEOUT_MS;
        public static final long DEFAULT_TOTAL_BYTES_PER_APP_LIMIT_FLOOR = android.util.DataUnit.MEBIBYTES.toBytes(300);
        public static long TOTAL_BYTES_PER_APP_LIMIT_FLOOR = DEFAULT_TOTAL_BYTES_PER_APP_LIMIT_FLOOR;
        public static float TOTAL_BYTES_PER_APP_LIMIT_FRACTION = 0.01f;
        public static final long DEFAULT_LEASE_ACQUISITION_WAIT_DURATION_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(6);
        public static long LEASE_ACQUISITION_WAIT_DURATION_MS = DEFAULT_LEASE_ACQUISITION_WAIT_DURATION_MS;
        public static final long DEFAULT_COMMIT_COOL_OFF_DURATION_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(48);
        public static long COMMIT_COOL_OFF_DURATION_MS = DEFAULT_COMMIT_COOL_OFF_DURATION_MS;
        public static boolean USE_REVOCABLE_FD_FOR_READS = false;
        public static final long DEFAULT_DELETE_ON_LAST_LEASE_DELAY_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(6);
        public static long DELETE_ON_LAST_LEASE_DELAY_MS = DEFAULT_DELETE_ON_LAST_LEASE_DELAY_MS;
        public static int DEFAULT_MAX_ACTIVE_SESSIONS = 250;
        public static int MAX_ACTIVE_SESSIONS = DEFAULT_MAX_ACTIVE_SESSIONS;
        public static int DEFAULT_MAX_COMMITTED_BLOBS = 1000;
        public static int MAX_COMMITTED_BLOBS = DEFAULT_MAX_COMMITTED_BLOBS;
        public static int DEFAULT_MAX_LEASED_BLOBS = 500;
        public static int MAX_LEASED_BLOBS = DEFAULT_MAX_LEASED_BLOBS;
        public static int DEFAULT_MAX_BLOB_ACCESS_PERMITTED_PACKAGES = 300;
        public static int MAX_BLOB_ACCESS_PERMITTED_PACKAGES = DEFAULT_MAX_BLOB_ACCESS_PERMITTED_PACKAGES;
        public static int DEFAULT_LEASE_DESC_CHAR_LIMIT = 300;
        public static int LEASE_DESC_CHAR_LIMIT = DEFAULT_LEASE_DESC_CHAR_LIMIT;

        /* JADX INFO: Access modifiers changed from: package-private */
        public static void refresh(final android.provider.DeviceConfig.Properties properties) {
            if (!com.android.server.blob.BlobStoreConfig.ROOT_DIR_NAME.equals(properties.getNamespace())) {
                return;
            }
            properties.getKeyset().forEach(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreConfig$DeviceConfigProperties$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.lambda$refresh$0(properties, (java.lang.String) obj);
                }
            });
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:44:0x009f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        static /* synthetic */ void lambda$refresh$0(android.provider.DeviceConfig.Properties r2, java.lang.String r3) {
            /*
                Method dump skipped, instruction units count: 392
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.lambda$refresh$0(android.provider.DeviceConfig$Properties, java.lang.String):void");
        }

        static void dump(android.util.IndentingPrintWriter fout, android.content.Context context) {
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_IDLE_JOB_PERIOD_MS, android.util.TimeUtils.formatDuration(IDLE_JOB_PERIOD_MS), android.util.TimeUtils.formatDuration(DEFAULT_IDLE_JOB_PERIOD_MS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_SESSION_EXPIRY_TIMEOUT_MS, android.util.TimeUtils.formatDuration(SESSION_EXPIRY_TIMEOUT_MS), android.util.TimeUtils.formatDuration(DEFAULT_SESSION_EXPIRY_TIMEOUT_MS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_TOTAL_BYTES_PER_APP_LIMIT_FLOOR, android.text.format.Formatter.formatFileSize(context, TOTAL_BYTES_PER_APP_LIMIT_FLOOR, 8), android.text.format.Formatter.formatFileSize(context, DEFAULT_TOTAL_BYTES_PER_APP_LIMIT_FLOOR, 8)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_TOTAL_BYTES_PER_APP_LIMIT_FRACTION, java.lang.Float.valueOf(TOTAL_BYTES_PER_APP_LIMIT_FRACTION), java.lang.Float.valueOf(0.01f)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_LEASE_ACQUISITION_WAIT_DURATION_MS, android.util.TimeUtils.formatDuration(LEASE_ACQUISITION_WAIT_DURATION_MS), android.util.TimeUtils.formatDuration(DEFAULT_LEASE_ACQUISITION_WAIT_DURATION_MS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_COMMIT_COOL_OFF_DURATION_MS, android.util.TimeUtils.formatDuration(COMMIT_COOL_OFF_DURATION_MS), android.util.TimeUtils.formatDuration(DEFAULT_COMMIT_COOL_OFF_DURATION_MS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_USE_REVOCABLE_FD_FOR_READS, java.lang.Boolean.valueOf(USE_REVOCABLE_FD_FOR_READS), false));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_DELETE_ON_LAST_LEASE_DELAY_MS, android.util.TimeUtils.formatDuration(DELETE_ON_LAST_LEASE_DELAY_MS), android.util.TimeUtils.formatDuration(DEFAULT_DELETE_ON_LAST_LEASE_DELAY_MS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_MAX_ACTIVE_SESSIONS, java.lang.Integer.valueOf(MAX_ACTIVE_SESSIONS), java.lang.Integer.valueOf(DEFAULT_MAX_ACTIVE_SESSIONS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_MAX_COMMITTED_BLOBS, java.lang.Integer.valueOf(MAX_COMMITTED_BLOBS), java.lang.Integer.valueOf(DEFAULT_MAX_COMMITTED_BLOBS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_MAX_LEASED_BLOBS, java.lang.Integer.valueOf(MAX_LEASED_BLOBS), java.lang.Integer.valueOf(DEFAULT_MAX_LEASED_BLOBS)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_MAX_BLOB_ACCESS_PERMITTED_PACKAGES, java.lang.Integer.valueOf(MAX_BLOB_ACCESS_PERMITTED_PACKAGES), java.lang.Integer.valueOf(DEFAULT_MAX_BLOB_ACCESS_PERMITTED_PACKAGES)));
            fout.println(java.lang.String.format("%s: [cur: %s, def: %s]", KEY_LEASE_DESC_CHAR_LIMIT, java.lang.Integer.valueOf(LEASE_DESC_CHAR_LIMIT), java.lang.Integer.valueOf(DEFAULT_LEASE_DESC_CHAR_LIMIT)));
        }
    }

    public static void initialize(android.content.Context context) {
        android.provider.DeviceConfig.addOnPropertiesChangedListener(ROOT_DIR_NAME, context.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.blob.BlobStoreConfig$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.refresh(properties);
            }
        });
        com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.refresh(android.provider.DeviceConfig.getProperties(ROOT_DIR_NAME, new java.lang.String[0]));
    }

    public static long getIdleJobPeriodMs() {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.IDLE_JOB_PERIOD_MS;
    }

    public static boolean hasSessionExpired(long sessionLastModifiedMs) {
        return sessionLastModifiedMs < java.lang.System.currentTimeMillis() - com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.SESSION_EXPIRY_TIMEOUT_MS;
    }

    public static long getAppDataBytesLimit() {
        long totalBytesLimit = (long) (android.os.Environment.getDataSystemDirectory().getTotalSpace() * com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.TOTAL_BYTES_PER_APP_LIMIT_FRACTION);
        return java.lang.Math.max(com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.TOTAL_BYTES_PER_APP_LIMIT_FLOOR, totalBytesLimit);
    }

    public static boolean hasLeaseWaitTimeElapsed(long commitTimeMs) {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.LEASE_ACQUISITION_WAIT_DURATION_MS + commitTimeMs < java.lang.System.currentTimeMillis();
    }

    public static long getAdjustedCommitTimeMs(long oldCommitTimeMs, long newCommitTimeMs) {
        if (oldCommitTimeMs == 0 || hasCommitCoolOffPeriodElapsed(oldCommitTimeMs)) {
            return newCommitTimeMs;
        }
        return oldCommitTimeMs;
    }

    private static boolean hasCommitCoolOffPeriodElapsed(long commitTimeMs) {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.COMMIT_COOL_OFF_DURATION_MS + commitTimeMs < java.lang.System.currentTimeMillis();
    }

    public static boolean shouldUseRevocableFdForReads() {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.USE_REVOCABLE_FD_FOR_READS;
    }

    public static long getDeletionOnLastLeaseDelayMs() {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.DELETE_ON_LAST_LEASE_DELAY_MS;
    }

    public static int getMaxActiveSessions() {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.MAX_ACTIVE_SESSIONS;
    }

    public static int getMaxCommittedBlobs() {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.MAX_COMMITTED_BLOBS;
    }

    public static int getMaxLeasedBlobs() {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.MAX_LEASED_BLOBS;
    }

    public static int getMaxPermittedPackages() {
        return com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.MAX_BLOB_ACCESS_PERMITTED_PACKAGES;
    }

    public static java.lang.CharSequence getTruncatedLeaseDescription(java.lang.CharSequence description) {
        if (android.text.TextUtils.isEmpty(description)) {
            return description;
        }
        return android.text.TextUtils.trimToLengthWithEllipsis(description, com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.LEASE_DESC_CHAR_LIMIT);
    }

    public static java.io.File prepareBlobFile(long sessionId) {
        java.io.File blobsDir = prepareBlobsDir();
        if (blobsDir == null) {
            return null;
        }
        return getBlobFile(blobsDir, sessionId);
    }

    public static java.io.File getBlobFile(long sessionId) {
        return getBlobFile(getBlobsDir(), sessionId);
    }

    private static java.io.File getBlobFile(java.io.File blobsDir, long sessionId) {
        return new java.io.File(blobsDir, java.lang.String.valueOf(sessionId));
    }

    public static java.io.File prepareBlobsDir() {
        java.io.File blobsDir = getBlobsDir(prepareBlobStoreRootDir());
        if (!blobsDir.exists() && !blobsDir.mkdir()) {
            android.util.Slog.e(TAG, "Failed to mkdir(): " + blobsDir);
            return null;
        }
        return blobsDir;
    }

    public static java.io.File getBlobsDir() {
        return getBlobsDir(getBlobStoreRootDir());
    }

    private static java.io.File getBlobsDir(java.io.File blobsRootDir) {
        return new java.io.File(blobsRootDir, BLOBS_DIR_NAME);
    }

    public static java.io.File prepareSessionIndexFile() {
        java.io.File blobStoreRootDir = prepareBlobStoreRootDir();
        if (blobStoreRootDir == null) {
            return null;
        }
        return new java.io.File(blobStoreRootDir, SESSIONS_INDEX_FILE_NAME);
    }

    public static java.io.File prepareBlobsIndexFile() {
        java.io.File blobsStoreRootDir = prepareBlobStoreRootDir();
        if (blobsStoreRootDir == null) {
            return null;
        }
        return new java.io.File(blobsStoreRootDir, BLOBS_INDEX_FILE_NAME);
    }

    public static java.io.File prepareBlobStoreRootDir() {
        java.io.File blobStoreRootDir = getBlobStoreRootDir();
        if (!blobStoreRootDir.exists() && !blobStoreRootDir.mkdir()) {
            android.util.Slog.e(TAG, "Failed to mkdir(): " + blobStoreRootDir);
            return null;
        }
        return blobStoreRootDir;
    }

    public static java.io.File getBlobStoreRootDir() {
        return new java.io.File(android.os.Environment.getDataSystemDirectory(), ROOT_DIR_NAME);
    }

    public static void dump(android.util.IndentingPrintWriter fout, android.content.Context context) {
        fout.println("XML current version: 6");
        fout.println("Idle job ID: 191934935");
        fout.println("Total bytes per app limit: " + android.text.format.Formatter.formatFileSize(context, getAppDataBytesLimit(), 8));
        fout.println("Device config properties:");
        fout.increaseIndent();
        com.android.server.blob.BlobStoreConfig.DeviceConfigProperties.dump(fout, context);
        fout.decreaseIndent();
    }
}
