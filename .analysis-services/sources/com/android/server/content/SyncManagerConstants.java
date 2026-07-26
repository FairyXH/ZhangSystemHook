package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public class SyncManagerConstants extends android.database.ContentObserver {
    private static final int DEF_EXEMPTION_TEMP_ALLOWLIST_DURATION_IN_SECONDS = 600;
    private static final int DEF_INITIAL_SYNC_RETRY_TIME_IN_SECONDS = 30;
    private static final int DEF_MAX_RETRIES_WITH_APP_STANDBY_EXEMPTION = 5;
    private static final int DEF_MAX_SYNC_RETRY_TIME_IN_SECONDS = 3600;
    private static final float DEF_RETRY_TIME_INCREASE_FACTOR = 2.0f;
    private static final java.lang.String KEY_EXEMPTION_TEMP_ALLOWLIST_DURATION_IN_SECONDS = "exemption_temp_whitelist_duration_in_seconds";
    private static final java.lang.String KEY_INITIAL_SYNC_RETRY_TIME_IN_SECONDS = "initial_sync_retry_time_in_seconds";
    private static final java.lang.String KEY_MAX_RETRIES_WITH_APP_STANDBY_EXEMPTION = "max_retries_with_app_standby_exemption";
    private static final java.lang.String KEY_MAX_SYNC_RETRY_TIME_IN_SECONDS = "max_sync_retry_time_in_seconds";
    private static final java.lang.String KEY_RETRY_TIME_INCREASE_FACTOR = "retry_time_increase_factor";
    private static final java.lang.String TAG = "SyncManagerConfig";
    private final android.content.Context mContext;
    private int mInitialSyncRetryTimeInSeconds;
    private int mKeyExemptionTempWhitelistDurationInSeconds;
    private final java.lang.Object mLock;
    private int mMaxRetriesWithAppStandbyExemption;
    private int mMaxSyncRetryTimeInSeconds;
    private float mRetryTimeIncreaseFactor;

    protected SyncManagerConstants(android.content.Context context) {
        super(null);
        this.mLock = new java.lang.Object();
        this.mInitialSyncRetryTimeInSeconds = 30;
        this.mRetryTimeIncreaseFactor = DEF_RETRY_TIME_INCREASE_FACTOR;
        this.mMaxSyncRetryTimeInSeconds = DEF_MAX_SYNC_RETRY_TIME_IN_SECONDS;
        this.mMaxRetriesWithAppStandbyExemption = 5;
        this.mKeyExemptionTempWhitelistDurationInSeconds = 600;
        this.mContext = context;
    }

    public void start() {
        com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.content.SyncManagerConstants$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$start$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("sync_manager_constants"), false, this);
        refresh();
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange) {
        refresh();
    }

    private void refresh() {
        synchronized (this.mLock) {
            java.lang.String newValue = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "sync_manager_constants");
            android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
            try {
                parser.setString(newValue);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.wtf(TAG, "Bad constants: " + newValue);
            }
            this.mInitialSyncRetryTimeInSeconds = parser.getInt(KEY_INITIAL_SYNC_RETRY_TIME_IN_SECONDS, 30);
            this.mMaxSyncRetryTimeInSeconds = parser.getInt(KEY_MAX_SYNC_RETRY_TIME_IN_SECONDS, DEF_MAX_SYNC_RETRY_TIME_IN_SECONDS);
            this.mRetryTimeIncreaseFactor = parser.getFloat(KEY_RETRY_TIME_INCREASE_FACTOR, DEF_RETRY_TIME_INCREASE_FACTOR);
            this.mMaxRetriesWithAppStandbyExemption = parser.getInt(KEY_MAX_RETRIES_WITH_APP_STANDBY_EXEMPTION, 5);
            this.mKeyExemptionTempWhitelistDurationInSeconds = parser.getInt(KEY_EXEMPTION_TEMP_ALLOWLIST_DURATION_IN_SECONDS, 600);
        }
    }

    public int getInitialSyncRetryTimeInSeconds() {
        int i;
        synchronized (this.mLock) {
            i = this.mInitialSyncRetryTimeInSeconds;
        }
        return i;
    }

    public float getRetryTimeIncreaseFactor() {
        float f;
        synchronized (this.mLock) {
            f = this.mRetryTimeIncreaseFactor;
        }
        return f;
    }

    public int getMaxSyncRetryTimeInSeconds() {
        int i;
        synchronized (this.mLock) {
            i = this.mMaxSyncRetryTimeInSeconds;
        }
        return i;
    }

    public int getMaxRetriesWithAppStandbyExemption() {
        int i;
        synchronized (this.mLock) {
            i = this.mMaxRetriesWithAppStandbyExemption;
        }
        return i;
    }

    public int getKeyExemptionTempWhitelistDurationInSeconds() {
        int i;
        synchronized (this.mLock) {
            i = this.mKeyExemptionTempWhitelistDurationInSeconds;
        }
        return i;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this.mLock) {
            pw.print(prefix);
            pw.println("SyncManager Config:");
            pw.print(prefix);
            pw.print("  mInitialSyncRetryTimeInSeconds=");
            pw.println(this.mInitialSyncRetryTimeInSeconds);
            pw.print(prefix);
            pw.print("  mRetryTimeIncreaseFactor=");
            pw.println(this.mRetryTimeIncreaseFactor);
            pw.print(prefix);
            pw.print("  mMaxSyncRetryTimeInSeconds=");
            pw.println(this.mMaxSyncRetryTimeInSeconds);
            pw.print(prefix);
            pw.print("  mMaxRetriesWithAppStandbyExemption=");
            pw.println(this.mMaxRetriesWithAppStandbyExemption);
            pw.print(prefix);
            pw.print("  mKeyExemptionTempWhitelistDurationInSeconds=");
            pw.println(this.mKeyExemptionTempWhitelistDurationInSeconds);
        }
    }
}
