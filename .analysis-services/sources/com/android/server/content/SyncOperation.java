package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public class SyncOperation {
    public static final int NO_JOB_ID = -1;
    public static final int REASON_ACCOUNTS_UPDATED = -2;
    public static final int REASON_BACKGROUND_DATA_SETTINGS_CHANGED = -1;
    public static final int REASON_IS_SYNCABLE = -5;
    public static final int REASON_MASTER_SYNC_AUTO = -7;
    private static java.lang.String[] REASON_NAMES = {"DataSettingsChanged", "AccountsUpdated", "ServiceChanged", "Periodic", "IsSyncable", "AutoSync", "MasterSyncAuto", "UserStart"};
    public static final int REASON_PERIODIC = -4;
    public static final int REASON_SERVICE_CHANGED = -3;
    public static final int REASON_SYNC_AUTO = -6;
    public static final int REASON_USER_START = -8;
    public static final java.lang.String TAG = "SyncManager";
    public final boolean allowParallelSyncs;
    public long expectedRuntime;
    public final long flexMillis;
    public final boolean isPeriodic;
    public int jobId;
    public final java.lang.String key;
    private volatile android.os.Bundle mImmutableExtras;
    public final java.lang.String owningPackage;
    public final int owningUid;
    public final long periodMillis;
    public final int reason;
    int retries;
    boolean scheduleEjAsRegularJob;
    public final int sourcePeriodicId;
    public int syncExemptionFlag;
    public final int syncSource;
    public final com.android.server.content.SyncStorageEngine.EndPoint target;
    public java.lang.String wakeLockName;

    public SyncOperation(android.accounts.Account account, int userId, int owningUid, java.lang.String owningPackage, int reason, int source, java.lang.String provider, android.os.Bundle extras, boolean allowParallelSyncs, int syncExemptionFlag) {
        this(new com.android.server.content.SyncStorageEngine.EndPoint(account, provider, userId), owningUid, owningPackage, reason, source, extras, allowParallelSyncs, syncExemptionFlag);
    }

    private SyncOperation(com.android.server.content.SyncStorageEngine.EndPoint info, int owningUid, java.lang.String owningPackage, int reason, int source, android.os.Bundle extras, boolean allowParallelSyncs, int syncExemptionFlag) {
        this(info, owningUid, owningPackage, reason, source, extras, allowParallelSyncs, false, -1, 0L, 0L, syncExemptionFlag);
    }

    public SyncOperation(com.android.server.content.SyncOperation op, long periodMillis, long flexMillis) {
        this(op.target, op.owningUid, op.owningPackage, op.reason, op.syncSource, op.mImmutableExtras, op.allowParallelSyncs, op.isPeriodic, op.sourcePeriodicId, periodMillis, flexMillis, 0);
    }

    public SyncOperation(com.android.server.content.SyncStorageEngine.EndPoint info, int owningUid, java.lang.String owningPackage, int reason, int source, android.os.Bundle extras, boolean allowParallelSyncs, boolean isPeriodic, int sourcePeriodicId, long periodMillis, long flexMillis, int syncExemptionFlag) {
        this.target = info;
        this.owningUid = owningUid;
        this.owningPackage = owningPackage;
        this.reason = reason;
        this.syncSource = source;
        this.mImmutableExtras = new android.os.Bundle(extras);
        this.allowParallelSyncs = allowParallelSyncs;
        this.isPeriodic = isPeriodic;
        this.sourcePeriodicId = sourcePeriodicId;
        this.periodMillis = periodMillis;
        this.flexMillis = flexMillis;
        this.jobId = -1;
        this.key = toKey();
        this.syncExemptionFlag = syncExemptionFlag;
    }

    public com.android.server.content.SyncOperation createOneTimeSyncOperation() {
        if (!this.isPeriodic) {
            return null;
        }
        com.android.server.content.SyncOperation op = new com.android.server.content.SyncOperation(this.target, this.owningUid, this.owningPackage, this.reason, this.syncSource, this.mImmutableExtras, this.allowParallelSyncs, false, this.jobId, this.periodMillis, this.flexMillis, 0);
        return op;
    }

    public SyncOperation(com.android.server.content.SyncOperation other) {
        this.target = other.target;
        this.owningUid = other.owningUid;
        this.owningPackage = other.owningPackage;
        this.reason = other.reason;
        this.syncSource = other.syncSource;
        this.allowParallelSyncs = other.allowParallelSyncs;
        this.mImmutableExtras = other.mImmutableExtras;
        this.wakeLockName = other.wakeLockName();
        this.isPeriodic = other.isPeriodic;
        this.sourcePeriodicId = other.sourcePeriodicId;
        this.periodMillis = other.periodMillis;
        this.flexMillis = other.flexMillis;
        this.key = other.key;
        this.syncExemptionFlag = other.syncExemptionFlag;
    }

    android.os.PersistableBundle toJobInfoExtras() {
        android.os.PersistableBundle jobInfoExtras = new android.os.PersistableBundle();
        android.os.PersistableBundle syncExtrasBundle = new android.os.PersistableBundle();
        android.os.Bundle extras = this.mImmutableExtras;
        for (java.lang.String key : extras.keySet()) {
            java.lang.Object value = extras.get(key);
            if (value instanceof android.accounts.Account) {
                android.accounts.Account account = (android.accounts.Account) value;
                android.os.PersistableBundle accountBundle = new android.os.PersistableBundle();
                accountBundle.putString("accountName", account.name);
                accountBundle.putString("accountType", account.type);
                jobInfoExtras.putPersistableBundle("ACCOUNT:" + key, accountBundle);
            } else if (value instanceof java.lang.Long) {
                syncExtrasBundle.putLong(key, ((java.lang.Long) value).longValue());
            } else if (value instanceof java.lang.Integer) {
                syncExtrasBundle.putInt(key, ((java.lang.Integer) value).intValue());
            } else if (value instanceof java.lang.Boolean) {
                syncExtrasBundle.putBoolean(key, ((java.lang.Boolean) value).booleanValue());
            } else if (value instanceof java.lang.Float) {
                syncExtrasBundle.putDouble(key, ((java.lang.Float) value).floatValue());
            } else if (value instanceof java.lang.Double) {
                syncExtrasBundle.putDouble(key, ((java.lang.Double) value).doubleValue());
            } else if (value instanceof java.lang.String) {
                syncExtrasBundle.putString(key, (java.lang.String) value);
            } else if (value == null) {
                syncExtrasBundle.putString(key, null);
            } else {
                android.util.Slog.e(TAG, "Unknown extra type.");
            }
        }
        jobInfoExtras.putPersistableBundle("syncExtras", syncExtrasBundle);
        jobInfoExtras.putBoolean("SyncManagerJob", true);
        jobInfoExtras.putString("provider", this.target.provider);
        jobInfoExtras.putString("accountName", this.target.account.name);
        jobInfoExtras.putString("accountType", this.target.account.type);
        jobInfoExtras.putInt("userId", this.target.userId);
        jobInfoExtras.putInt("owningUid", this.owningUid);
        jobInfoExtras.putString("owningPackage", this.owningPackage);
        jobInfoExtras.putInt(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, this.reason);
        jobInfoExtras.putInt("source", this.syncSource);
        jobInfoExtras.putBoolean("allowParallelSyncs", this.allowParallelSyncs);
        jobInfoExtras.putInt("jobId", this.jobId);
        jobInfoExtras.putBoolean("isPeriodic", this.isPeriodic);
        jobInfoExtras.putInt("sourcePeriodicId", this.sourcePeriodicId);
        jobInfoExtras.putLong("periodMillis", this.periodMillis);
        jobInfoExtras.putLong("flexMillis", this.flexMillis);
        jobInfoExtras.putLong("expectedRuntime", this.expectedRuntime);
        jobInfoExtras.putInt("retries", this.retries);
        jobInfoExtras.putInt("syncExemptionFlag", this.syncExemptionFlag);
        jobInfoExtras.putBoolean("ejDowngradedToRegular", this.scheduleEjAsRegularJob);
        return jobInfoExtras;
    }

    static com.android.server.content.SyncOperation maybeCreateFromJobExtras(android.os.PersistableBundle jobExtras) {
        java.lang.String str;
        java.util.Iterator<java.lang.String> it;
        if (jobExtras == null || !jobExtras.getBoolean("SyncManagerJob", false)) {
            return null;
        }
        java.lang.String str2 = "accountName";
        java.lang.String accountName = jobExtras.getString("accountName");
        java.lang.String accountType = jobExtras.getString("accountType");
        java.lang.String provider = jobExtras.getString("provider");
        int userId = jobExtras.getInt("userId", Integer.MAX_VALUE);
        int owningUid = jobExtras.getInt("owningUid");
        java.lang.String owningPackage = jobExtras.getString("owningPackage");
        int reason = jobExtras.getInt(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, Integer.MAX_VALUE);
        int source = jobExtras.getInt("source", Integer.MAX_VALUE);
        boolean allowParallelSyncs = jobExtras.getBoolean("allowParallelSyncs", false);
        boolean isPeriodic = jobExtras.getBoolean("isPeriodic", false);
        int initiatedBy = jobExtras.getInt("sourcePeriodicId", -1);
        long periodMillis = jobExtras.getLong("periodMillis");
        long flexMillis = jobExtras.getLong("flexMillis");
        int syncExemptionFlag = jobExtras.getInt("syncExemptionFlag", 0);
        android.os.Bundle extras = new android.os.Bundle();
        android.os.PersistableBundle syncExtras = jobExtras.getPersistableBundle("syncExtras");
        if (syncExtras != null) {
            extras.putAll(syncExtras);
        }
        java.util.Iterator<java.lang.String> it2 = jobExtras.keySet().iterator();
        while (it2.hasNext()) {
            java.lang.String key = it2.next();
            if (key == null || !key.startsWith("ACCOUNT:")) {
                str = str2;
                it = it2;
            } else {
                java.lang.String newKey = key.substring(8);
                android.os.PersistableBundle accountsBundle = jobExtras.getPersistableBundle(key);
                it = it2;
                str = str2;
                android.accounts.Account account = new android.accounts.Account(accountsBundle.getString(str2), accountsBundle.getString("accountType"));
                extras.putParcelable(newKey, account);
            }
            it2 = it;
            str2 = str;
        }
        android.accounts.Account account2 = new android.accounts.Account(accountName, accountType);
        com.android.server.content.SyncStorageEngine.EndPoint target = new com.android.server.content.SyncStorageEngine.EndPoint(account2, provider, userId);
        com.android.server.content.SyncOperation op = new com.android.server.content.SyncOperation(target, owningUid, owningPackage, reason, source, extras, allowParallelSyncs, isPeriodic, initiatedBy, periodMillis, flexMillis, syncExemptionFlag);
        op.jobId = jobExtras.getInt("jobId");
        op.expectedRuntime = jobExtras.getLong("expectedRuntime");
        op.retries = jobExtras.getInt("retries");
        op.scheduleEjAsRegularJob = jobExtras.getBoolean("ejDowngradedToRegular");
        return op;
    }

    boolean isConflict(com.android.server.content.SyncOperation toRun) {
        com.android.server.content.SyncStorageEngine.EndPoint other = toRun.target;
        return this.target.account.type.equals(other.account.type) && this.target.provider.equals(other.provider) && this.target.userId == other.userId && (!this.allowParallelSyncs || this.target.account.name.equals(other.account.name));
    }

    boolean isReasonPeriodic() {
        return this.reason == -4;
    }

    boolean matchesPeriodicOperation(com.android.server.content.SyncOperation other) {
        return this.target.matchesSpec(other.target) && com.android.server.content.SyncManager.syncExtrasEquals(this.mImmutableExtras, other.mImmutableExtras, true) && this.periodMillis == other.periodMillis && this.flexMillis == other.flexMillis;
    }

    boolean isDerivedFromFailedPeriodicSync() {
        return this.sourcePeriodicId != -1;
    }

    int getJobBias() {
        if (isInitialization()) {
            return 20;
        }
        if (isExpedited()) {
            return 10;
        }
        return 0;
    }

    private java.lang.String toKey() {
        android.os.Bundle extras = this.mImmutableExtras;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("provider: ").append(this.target.provider);
        sb.append(" account {name=" + this.target.account.name + ", user=" + this.target.userId + ", type=" + this.target.account.type + "}");
        sb.append(" isPeriodic: ").append(this.isPeriodic);
        sb.append(" period: ").append(this.periodMillis);
        sb.append(" flex: ").append(this.flexMillis);
        sb.append(" extras: ");
        extrasToStringBuilder(extras, sb);
        return sb.toString();
    }

    public java.lang.String toString() {
        return dump(null, true, null, false);
    }

    public java.lang.String toSafeString() {
        return dump(null, true, null, true);
    }

    java.lang.String dump(android.content.pm.PackageManager pm, boolean shorter, com.android.server.content.SyncAdapterStateFetcher appStates, boolean logSafe) {
        android.os.Bundle extras = this.mImmutableExtras;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("JobId=").append(this.jobId).append(" ").append(logSafe ? "***" : this.target.account.name).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(this.target.account.type).append(" u").append(this.target.userId).append(" [").append(this.target.provider).append("] ");
        sb.append(com.android.server.content.SyncStorageEngine.SOURCES[this.syncSource]);
        if (this.expectedRuntime != 0) {
            sb.append(" ExpectedIn=");
            com.android.server.content.SyncManager.formatDurationHMS(sb, this.expectedRuntime - android.os.SystemClock.elapsedRealtime());
        }
        if (extras.getBoolean("expedited", false)) {
            sb.append(" EXPEDITED");
        }
        if (extras.getBoolean("schedule_as_expedited_job", false)) {
            sb.append(" EXPEDITED-JOB");
            if (this.scheduleEjAsRegularJob) {
                sb.append("(scheduled-as-regular)");
            }
        }
        switch (this.syncExemptionFlag) {
            case 0:
                break;
            case 1:
                sb.append(" STANDBY-EXEMPTED");
                break;
            case 2:
                sb.append(" STANDBY-EXEMPTED(TOP)");
                break;
            default:
                sb.append(" ExemptionFlag=" + this.syncExemptionFlag);
                break;
        }
        sb.append(" Reason=");
        sb.append(reasonToString(pm, this.reason));
        if (this.isPeriodic) {
            sb.append(" (period=");
            com.android.server.content.SyncManager.formatDurationHMS(sb, this.periodMillis);
            sb.append(" flex=");
            com.android.server.content.SyncManager.formatDurationHMS(sb, this.flexMillis);
            sb.append(")");
        }
        if (this.retries > 0) {
            sb.append(" Retries=");
            sb.append(this.retries);
        }
        if (!shorter) {
            sb.append(" Owner={");
            android.os.UserHandle.formatUid(sb, this.owningUid);
            sb.append(" ");
            sb.append(this.owningPackage);
            if (appStates != null) {
                sb.append(" [");
                sb.append(appStates.getStandbyBucket(android.os.UserHandle.getUserId(this.owningUid), this.owningPackage));
                sb.append("]");
                if (appStates.isAppActive(this.owningUid)) {
                    sb.append(" [ACTIVE]");
                }
            }
            sb.append("}");
            if (!extras.keySet().isEmpty()) {
                sb.append(" ");
                extrasToStringBuilder(extras, sb);
            }
        }
        return sb.toString();
    }

    static java.lang.String reasonToString(android.content.pm.PackageManager pm, int reason) {
        if (reason < 0) {
            int index = (-reason) - 1;
            if (index >= REASON_NAMES.length) {
                return java.lang.String.valueOf(reason);
            }
            return REASON_NAMES[index];
        }
        if (pm != null) {
            java.lang.String[] packages = pm.getPackagesForUid(reason);
            if (packages != null && packages.length == 1) {
                return packages[0];
            }
            java.lang.String name = pm.getNameForUid(reason);
            if (name != null) {
                return name;
            }
            return java.lang.String.valueOf(reason);
        }
        return java.lang.String.valueOf(reason);
    }

    boolean isInitialization() {
        return this.mImmutableExtras.getBoolean("initialize", false);
    }

    boolean isExpedited() {
        return this.mImmutableExtras.getBoolean("expedited", false);
    }

    boolean isUpload() {
        return this.mImmutableExtras.getBoolean("upload", false);
    }

    void enableTwoWaySync() {
        removeExtra("upload");
    }

    boolean hasIgnoreBackoff() {
        return this.mImmutableExtras.getBoolean("ignore_backoff", false);
    }

    void enableBackoff() {
        removeExtra("ignore_backoff");
    }

    boolean hasDoNotRetry() {
        return this.mImmutableExtras.getBoolean("do_not_retry", false);
    }

    boolean isNotAllowedOnMetered() {
        return this.mImmutableExtras.getBoolean("allow_metered", false);
    }

    boolean isManual() {
        return this.mImmutableExtras.getBoolean("force", false);
    }

    boolean isIgnoreSettings() {
        return this.mImmutableExtras.getBoolean("ignore_settings", false);
    }

    boolean hasRequireCharging() {
        return this.mImmutableExtras.getBoolean("require_charging", false);
    }

    boolean isScheduledAsExpeditedJob() {
        return this.mImmutableExtras.getBoolean("schedule_as_expedited_job", false);
    }

    boolean isAppStandbyExempted() {
        return this.syncExemptionFlag != 0;
    }

    boolean areExtrasEqual(android.os.Bundle other, boolean includeSyncSettings) {
        return com.android.server.content.SyncManager.syncExtrasEquals(this.mImmutableExtras, other, includeSyncSettings);
    }

    static void extrasToStringBuilder(android.os.Bundle bundle, java.lang.StringBuilder sb) {
        if (bundle == null) {
            sb.append("null");
            return;
        }
        sb.append("[");
        for (java.lang.String key : bundle.keySet()) {
            sb.append(key).append("=").append(bundle.get(key)).append(" ");
        }
        sb.append("]");
    }

    private static java.lang.String extrasToString(android.os.Bundle bundle) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        extrasToStringBuilder(bundle, sb);
        return sb.toString();
    }

    java.lang.String wakeLockName() {
        if (this.wakeLockName != null) {
            return this.wakeLockName;
        }
        java.lang.String str = this.target.provider + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.target.account.type;
        this.wakeLockName = str;
        return str;
    }

    public java.lang.Object[] toEventLog(int event) {
        java.lang.Object[] logArray = {this.target.provider, java.lang.Integer.valueOf(event), java.lang.Integer.valueOf(this.syncSource), java.lang.Integer.valueOf(this.target.account.name.hashCode())};
        return logArray;
    }

    private void removeExtra(java.lang.String key) {
        android.os.Bundle b = this.mImmutableExtras;
        if (!b.containsKey(key)) {
            return;
        }
        android.os.Bundle clone = new android.os.Bundle(b);
        clone.remove(key);
        this.mImmutableExtras = clone;
    }

    public android.os.Bundle getClonedExtras() {
        return new android.os.Bundle(this.mImmutableExtras);
    }

    public java.lang.String getExtrasAsString() {
        return extrasToString(this.mImmutableExtras);
    }
}
