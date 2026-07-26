package com.android.server.media.projection;

/* JADX INFO: loaded from: classes2.dex */
public class MediaProjectionSessionIdGenerator {
    private static final java.lang.String PREFERENCES_FILE_NAME = "media_projection_session_id";
    private static final int SESSION_ID_DEFAULT_VALUE = 0;
    private static final java.lang.String SESSION_ID_PREF_KEY = "media_projection_session_id_key";
    private static com.android.server.media.projection.MediaProjectionSessionIdGenerator sInstance;
    private static final java.lang.Object sInstanceLock = new java.lang.Object();
    private final java.lang.Object mSessionIdLock = new java.lang.Object();
    private final android.content.SharedPreferences mSharedPreferences;

    public static com.android.server.media.projection.MediaProjectionSessionIdGenerator getInstance(android.content.Context context) {
        com.android.server.media.projection.MediaProjectionSessionIdGenerator mediaProjectionSessionIdGenerator;
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                java.io.File preferencesFile = new java.io.File(android.os.Environment.getDataSystemDirectory(), PREFERENCES_FILE_NAME);
                android.content.Context directBootContext = context.createDeviceProtectedStorageContext();
                android.content.SharedPreferences preferences = directBootContext.getSharedPreferences(preferencesFile, 0);
                sInstance = new com.android.server.media.projection.MediaProjectionSessionIdGenerator(preferences);
            }
            mediaProjectionSessionIdGenerator = sInstance;
        }
        return mediaProjectionSessionIdGenerator;
    }

    public MediaProjectionSessionIdGenerator(android.content.SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

    public int getCurrentSessionId() {
        int currentSessionIdInternal;
        synchronized (this.mSessionIdLock) {
            currentSessionIdInternal = getCurrentSessionIdInternal();
        }
        return currentSessionIdInternal;
    }

    public int createAndGetNewSessionId() {
        int newSessionId;
        synchronized (this.mSessionIdLock) {
            newSessionId = getCurrentSessionId() + 1;
            setSessionIdInternal(newSessionId);
        }
        return newSessionId;
    }

    private void setSessionIdInternal(int value) {
        this.mSharedPreferences.edit().putInt(SESSION_ID_PREF_KEY, value).apply();
    }

    private int getCurrentSessionIdInternal() {
        return this.mSharedPreferences.getInt(SESSION_ID_PREF_KEY, 0);
    }
}
