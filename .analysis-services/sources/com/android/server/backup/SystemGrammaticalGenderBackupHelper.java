package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class SystemGrammaticalGenderBackupHelper extends android.app.backup.BlobBackupHelper {
    private static final int BLOB_VERSION = 1;
    private static final java.lang.String KEY_SYSTEM_GENDER = "system_gender";
    private final com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal mGrammarInflectionManagerInternal;
    private final int mUserId;

    public SystemGrammaticalGenderBackupHelper(int userId) {
        super(1, new java.lang.String[]{KEY_SYSTEM_GENDER});
        this.mUserId = userId;
        this.mGrammarInflectionManagerInternal = (com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal) com.android.server.LocalServices.getService(com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal.class);
    }

    public void performBackup(android.os.ParcelFileDescriptor oldStateFd, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newStateFd) {
        if ((data.getTransportFlags() & 1) == 0) {
            return;
        }
        super.performBackup(oldStateFd, data, newStateFd);
    }

    protected byte[] getBackupPayload(java.lang.String key) {
        if (!KEY_SYSTEM_GENDER.equals(key) || this.mGrammarInflectionManagerInternal == null) {
            return null;
        }
        return this.mGrammarInflectionManagerInternal.getSystemBackupPayload(this.mUserId);
    }

    protected void applyRestoredPayload(java.lang.String key, byte[] payload) {
        if (KEY_SYSTEM_GENDER.equals(key) && this.mGrammarInflectionManagerInternal != null) {
            this.mGrammarInflectionManagerInternal.applyRestoredSystemPayload(payload, this.mUserId);
        }
    }
}
