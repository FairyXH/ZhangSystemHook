package com.android.server.media.projection;

/* JADX INFO: loaded from: classes2.dex */
public class MediaProjectionTimestampStore {
    private static final java.lang.String PREFERENCES_FILE_NAME = "media_projection_timestamp";
    private static final java.lang.String TIMESTAMP_PREF_KEY = "media_projection_timestamp_key";
    private static com.android.server.media.projection.MediaProjectionTimestampStore sInstance;
    private static final java.lang.Object sInstanceLock = new java.lang.Object();
    private final java.time.InstantSource mInstantSource;
    private final android.content.SharedPreferences mSharedPreferences;
    private final java.lang.Object mTimestampLock = new java.lang.Object();

    public MediaProjectionTimestampStore(android.content.SharedPreferences sharedPreferences, java.time.InstantSource instantSource) {
        this.mSharedPreferences = sharedPreferences;
        this.mInstantSource = instantSource;
    }

    public static com.android.server.media.projection.MediaProjectionTimestampStore getInstance(android.content.Context context) {
        com.android.server.media.projection.MediaProjectionTimestampStore mediaProjectionTimestampStore;
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                java.io.File preferencesFile = new java.io.File(android.os.Environment.getDataSystemDirectory(), PREFERENCES_FILE_NAME);
                android.content.Context directBootContext = context.createDeviceProtectedStorageContext();
                android.content.SharedPreferences preferences = directBootContext.getSharedPreferences(preferencesFile, 0);
                sInstance = new com.android.server.media.projection.MediaProjectionTimestampStore(preferences, java.time.InstantSource.system());
            }
            mediaProjectionTimestampStore = sInstance;
        }
        return mediaProjectionTimestampStore;
    }

    public java.time.Duration timeSinceLastActiveSession() {
        synchronized (this.mTimestampLock) {
            java.time.Instant lastActiveSessionTimestamp = getLastActiveSessionTimestamp();
            if (lastActiveSessionTimestamp == null) {
                return null;
            }
            java.time.Instant now = this.mInstantSource.instant();
            return java.time.Duration.between(lastActiveSessionTimestamp, now);
        }
    }

    public void registerActiveSessionEnded() {
        synchronized (this.mTimestampLock) {
            java.time.Instant now = this.mInstantSource.instant();
            setLastActiveSessionTimestamp(now);
        }
    }

    private java.time.Instant getLastActiveSessionTimestamp() {
        long lastActiveSessionEpochMilli = this.mSharedPreferences.getLong(TIMESTAMP_PREF_KEY, -1L);
        if (lastActiveSessionEpochMilli == -1) {
            return null;
        }
        return java.time.Instant.ofEpochMilli(lastActiveSessionEpochMilli);
    }

    private void setLastActiveSessionTimestamp(java.time.Instant timestamp) {
        this.mSharedPreferences.edit().putLong(TIMESTAMP_PREF_KEY, timestamp.toEpochMilli()).apply();
    }
}
