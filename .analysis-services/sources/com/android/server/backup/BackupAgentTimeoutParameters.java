package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class BackupAgentTimeoutParameters extends android.util.KeyValueSettingObserver {
    public static final long DEFAULT_FULL_BACKUP_AGENT_TIMEOUT_MILLIS = 300000;
    public static final long DEFAULT_KV_BACKUP_AGENT_TIMEOUT_MILLIS = 30000;
    public static final long DEFAULT_QUOTA_EXCEEDED_TIMEOUT_MILLIS = 3000;
    public static final long DEFAULT_RESTORE_AGENT_FINISHED_TIMEOUT_MILLIS = 30000;
    public static final long DEFAULT_RESTORE_AGENT_TIMEOUT_MILLIS = 60000;
    public static final long DEFAULT_RESTORE_SESSION_TIMEOUT_MILLIS = 60000;
    public static final long DEFAULT_RESTORE_SYSTEM_AGENT_TIMEOUT_MILLIS = 180000;
    public static final long DEFAULT_SHARED_BACKUP_AGENT_TIMEOUT_MILLIS = 1800000;
    public static final java.lang.String SETTING = "backup_agent_timeout_parameters";
    public static final java.lang.String SETTING_FULL_BACKUP_AGENT_TIMEOUT_MILLIS = "full_backup_agent_timeout_millis";
    public static final java.lang.String SETTING_KV_BACKUP_AGENT_TIMEOUT_MILLIS = "kv_backup_agent_timeout_millis";
    public static final java.lang.String SETTING_QUOTA_EXCEEDED_TIMEOUT_MILLIS = "quota_exceeded_timeout_millis";
    public static final java.lang.String SETTING_RESTORE_AGENT_FINISHED_TIMEOUT_MILLIS = "restore_agent_finished_timeout_millis";
    public static final java.lang.String SETTING_RESTORE_AGENT_TIMEOUT_MILLIS = "restore_agent_timeout_millis";
    public static final java.lang.String SETTING_RESTORE_SESSION_TIMEOUT_MILLIS = "restore_session_timeout_millis";
    public static final java.lang.String SETTING_RESTORE_SYSTEM_AGENT_TIMEOUT_MILLIS = "restore_system_agent_timeout_millis";
    public static final java.lang.String SETTING_SHARED_BACKUP_AGENT_TIMEOUT_MILLIS = "shared_backup_agent_timeout_millis";
    private long mFullBackupAgentTimeoutMillis;
    private long mKvBackupAgentTimeoutMillis;
    private final java.lang.Object mLock;
    private long mQuotaExceededTimeoutMillis;
    private long mRestoreAgentFinishedTimeoutMillis;
    private long mRestoreAgentTimeoutMillis;
    private long mRestoreSessionTimeoutMillis;
    private long mRestoreSystemAgentTimeoutMillis;
    private long mSharedBackupAgentTimeoutMillis;

    public BackupAgentTimeoutParameters(android.os.Handler handler, android.content.ContentResolver resolver) {
        super(handler, resolver, android.provider.Settings.Global.getUriFor(SETTING));
        this.mLock = new java.lang.Object();
    }

    public java.lang.String getSettingValue(android.content.ContentResolver resolver) {
        return android.provider.Settings.Global.getString(resolver, SETTING);
    }

    public void update(android.util.KeyValueListParser parser) {
        synchronized (this.mLock) {
            this.mKvBackupAgentTimeoutMillis = parser.getLong(SETTING_KV_BACKUP_AGENT_TIMEOUT_MILLIS, 30000L);
            this.mFullBackupAgentTimeoutMillis = parser.getLong(SETTING_FULL_BACKUP_AGENT_TIMEOUT_MILLIS, 300000L);
            this.mSharedBackupAgentTimeoutMillis = parser.getLong(SETTING_SHARED_BACKUP_AGENT_TIMEOUT_MILLIS, 1800000L);
            this.mRestoreAgentTimeoutMillis = parser.getLong(SETTING_RESTORE_AGENT_TIMEOUT_MILLIS, 60000L);
            this.mRestoreSystemAgentTimeoutMillis = parser.getLong(SETTING_RESTORE_SYSTEM_AGENT_TIMEOUT_MILLIS, 180000L);
            this.mRestoreAgentFinishedTimeoutMillis = parser.getLong(SETTING_RESTORE_AGENT_FINISHED_TIMEOUT_MILLIS, 30000L);
            this.mRestoreSessionTimeoutMillis = parser.getLong(SETTING_RESTORE_SESSION_TIMEOUT_MILLIS, 60000L);
            this.mQuotaExceededTimeoutMillis = parser.getLong(SETTING_QUOTA_EXCEEDED_TIMEOUT_MILLIS, 3000L);
        }
    }

    public long getKvBackupAgentTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mKvBackupAgentTimeoutMillis;
        }
        return j;
    }

    public long getFullBackupAgentTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mFullBackupAgentTimeoutMillis;
        }
        return j;
    }

    public long getSharedBackupAgentTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mSharedBackupAgentTimeoutMillis;
        }
        return j;
    }

    public long getRestoreAgentTimeoutMillis(int applicationUid) {
        long j;
        synchronized (this.mLock) {
            j = android.os.UserHandle.isCore(applicationUid) ? this.mRestoreSystemAgentTimeoutMillis : this.mRestoreAgentTimeoutMillis;
        }
        return j;
    }

    public long getRestoreSessionTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mRestoreSessionTimeoutMillis;
        }
        return j;
    }

    public long getRestoreAgentFinishedTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mRestoreAgentFinishedTimeoutMillis;
        }
        return j;
    }

    public long getQuotaExceededTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mQuotaExceededTimeoutMillis;
        }
        return j;
    }
}
