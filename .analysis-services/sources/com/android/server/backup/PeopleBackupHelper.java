package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
class PeopleBackupHelper extends android.app.backup.BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final java.lang.String KEY_CONVERSATIONS = "people_conversation_infos";
    private static final int STATE_VERSION = 1;
    private static final java.lang.String TAG = com.android.server.backup.PeopleBackupHelper.class.getSimpleName();
    private final int mUserId;

    PeopleBackupHelper(int userId) {
        super(1, new java.lang.String[]{KEY_CONVERSATIONS});
        this.mUserId = userId;
    }

    protected byte[] getBackupPayload(java.lang.String key) {
        if (!KEY_CONVERSATIONS.equals(key)) {
            android.util.Slog.w(TAG, "Unexpected backup key " + key);
            return new byte[0];
        }
        com.android.server.people.PeopleServiceInternal ps = (com.android.server.people.PeopleServiceInternal) com.android.server.LocalServices.getService(com.android.server.people.PeopleServiceInternal.class);
        return ps.getBackupPayload(this.mUserId);
    }

    protected void applyRestoredPayload(java.lang.String key, byte[] payload) {
        if (!KEY_CONVERSATIONS.equals(key)) {
            android.util.Slog.w(TAG, "Unexpected restore key " + key);
        } else {
            com.android.server.people.PeopleServiceInternal ps = (com.android.server.people.PeopleServiceInternal) com.android.server.LocalServices.getService(com.android.server.people.PeopleServiceInternal.class);
            ps.restore(this.mUserId, payload);
        }
    }
}
