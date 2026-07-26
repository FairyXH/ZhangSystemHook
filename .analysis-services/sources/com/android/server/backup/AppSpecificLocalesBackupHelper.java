package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class AppSpecificLocalesBackupHelper extends android.app.backup.BlobBackupHelper {
    private static final int BLOB_VERSION = 1;
    private static final boolean DEBUG = false;
    private static final java.lang.String KEY_APP_LOCALES = "app_locales";
    private static final java.lang.String TAG = "AppLocalesBackupHelper";
    private final com.android.server.locales.LocaleManagerInternal mLocaleManagerInternal;
    private final int mUserId;

    public AppSpecificLocalesBackupHelper(int userId) {
        super(1, new java.lang.String[]{KEY_APP_LOCALES});
        this.mUserId = userId;
        this.mLocaleManagerInternal = (com.android.server.locales.LocaleManagerInternal) com.android.server.LocalServices.getService(com.android.server.locales.LocaleManagerInternal.class);
    }

    protected byte[] getBackupPayload(java.lang.String key) {
        if (KEY_APP_LOCALES.equals(key)) {
            try {
                byte[] newPayload = this.mLocaleManagerInternal.getBackupPayload(this.mUserId);
                return newPayload;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Couldn't communicate with locale manager", e);
                return null;
            }
        }
        android.util.Slog.w(TAG, "Unexpected backup key " + key);
        return null;
    }

    protected void applyRestoredPayload(java.lang.String key, byte[] payload) {
        if (KEY_APP_LOCALES.equals(key)) {
            try {
                this.mLocaleManagerInternal.stageAndApplyRestoredPayload(payload, this.mUserId);
                return;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Couldn't communicate with locale manager", e);
                return;
            }
        }
        android.util.Slog.w(TAG, "Unexpected restore key " + key);
    }
}
