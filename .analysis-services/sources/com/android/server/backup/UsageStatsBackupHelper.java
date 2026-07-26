package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class UsageStatsBackupHelper extends android.app.backup.BlobBackupHelper {
    static final int BLOB_VERSION = 1;
    static final boolean DEBUG = false;
    static final java.lang.String KEY_USAGE_STATS = "usage_stats";
    static final java.lang.String TAG = "UsgStatsBackupHelper";
    private final int mUserId;

    public UsageStatsBackupHelper(int userId) {
        super(1, new java.lang.String[]{KEY_USAGE_STATS});
        this.mUserId = userId;
    }

    protected byte[] getBackupPayload(java.lang.String key) {
        if (KEY_USAGE_STATS.equals(key)) {
            android.app.usage.UsageStatsManagerInternal localUsageStatsManager = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.DataOutputStream out = new java.io.DataOutputStream(baos);
            try {
                out.writeInt(0);
                out.write(localUsageStatsManager.getBackupPayload(this.mUserId, key));
            } catch (java.io.IOException e) {
                baos.reset();
            }
            return baos.toByteArray();
        }
        return null;
    }

    protected void applyRestoredPayload(java.lang.String key, byte[] payload) {
        if (KEY_USAGE_STATS.equals(key)) {
            android.app.usage.UsageStatsManagerInternal localUsageStatsManager = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
            java.io.DataInputStream in = new java.io.DataInputStream(new java.io.ByteArrayInputStream(payload));
            try {
                in.readInt();
                byte[] restoreData = new byte[payload.length - 4];
                in.read(restoreData, 0, restoreData.length);
                localUsageStatsManager.applyRestoredPayload(this.mUserId, key, restoreData);
            } catch (java.io.IOException e) {
            }
        }
    }
}
