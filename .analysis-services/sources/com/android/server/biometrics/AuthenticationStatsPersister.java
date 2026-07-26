package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class AuthenticationStatsPersister {
    private static final java.lang.String ENROLLMENT_NOTIFICATIONS = "enrollment_notifications";
    private static final java.lang.String FACE_ATTEMPTS = "face_attempts";
    private static final java.lang.String FACE_REJECTIONS = "face_rejections";
    private static final java.lang.String FILE_NAME = "authentication_stats";
    private static final java.lang.String FINGERPRINT_ATTEMPTS = "fingerprint_attempts";
    private static final java.lang.String FINGERPRINT_REJECTIONS = "fingerprint_rejections";
    private static final java.lang.String KEY = "frr_stats";
    private static final java.lang.String TAG = "AuthenticationStatsPersister";
    private static final java.lang.String THRESHOLD_KEY = "frr_threshold";
    private static final java.lang.String USER_ID = "user_id";
    private final android.content.SharedPreferences mSharedPreferences;

    AuthenticationStatsPersister(android.content.Context context) {
        java.io.File prefsFile = new java.io.File(android.os.Environment.getDataSystemDirectory(), FILE_NAME);
        this.mSharedPreferences = context.getSharedPreferences(prefsFile, 0);
    }

    public java.util.List<com.android.server.biometrics.AuthenticationStats> getAllFrrStats(int modality) {
        java.util.List<com.android.server.biometrics.AuthenticationStats> authenticationStatsList = new java.util.ArrayList<>();
        for (java.lang.String frrStats : readFrrStats()) {
            try {
                org.json.JSONObject frrStatsJson = new org.json.JSONObject(frrStats);
                if (modality == 4) {
                    authenticationStatsList.add(new com.android.server.biometrics.AuthenticationStats(getIntValue(frrStatsJson, USER_ID, -10000), getIntValue(frrStatsJson, FACE_ATTEMPTS), getIntValue(frrStatsJson, FACE_REJECTIONS), getIntValue(frrStatsJson, ENROLLMENT_NOTIFICATIONS), modality));
                } else if (modality == 1) {
                    authenticationStatsList.add(new com.android.server.biometrics.AuthenticationStats(getIntValue(frrStatsJson, USER_ID, -10000), getIntValue(frrStatsJson, FINGERPRINT_ATTEMPTS), getIntValue(frrStatsJson, FINGERPRINT_REJECTIONS), getIntValue(frrStatsJson, ENROLLMENT_NOTIFICATIONS), modality));
                }
            } catch (org.json.JSONException e) {
                android.util.Slog.w(TAG, java.lang.String.format("Unable to resolve authentication stats JSON: %s", frrStats));
            }
        }
        return authenticationStatsList;
    }

    public void removeFrrStats(int userId) {
        try {
            java.util.Set<java.lang.String> frrStatsSet = new java.util.HashSet<>(readFrrStats());
            java.util.Iterator<java.lang.String> iterator = frrStatsSet.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    break;
                }
                java.lang.String frrStats = iterator.next();
                org.json.JSONObject frrStatJson = new org.json.JSONObject(frrStats);
                if (getValue(frrStatJson, USER_ID).equals(java.lang.String.valueOf(userId))) {
                    iterator.remove();
                    break;
                }
            }
            this.mSharedPreferences.edit().putStringSet(KEY, frrStatsSet).apply();
        } catch (org.json.JSONException e) {
        }
    }

    public void persistFrrStats(int userId, int totalAttempts, int rejectedAttempts, int enrollmentNotifications, int modality) {
        org.json.JSONObject frrStatJson;
        try {
            java.util.Set<java.lang.String> frrStatsSet = new java.util.HashSet<>(readFrrStats());
            org.json.JSONObject frrStatJson2 = null;
            java.util.Iterator<java.lang.String> iterator = frrStatsSet.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    break;
                }
                java.lang.String frrStats = iterator.next();
                frrStatJson2 = new org.json.JSONObject(frrStats);
                if (getValue(frrStatJson2, USER_ID).equals(java.lang.String.valueOf(userId))) {
                    iterator.remove();
                    break;
                }
                frrStatJson2 = null;
            }
            if (frrStatJson2 != null) {
                frrStatJson = frrStatJson2;
            } else {
                org.json.JSONObject frrStatJson3 = new org.json.JSONObject().put(USER_ID, userId);
                frrStatJson = frrStatJson3;
            }
            frrStatsSet.add(buildFrrStats(frrStatJson, totalAttempts, rejectedAttempts, enrollmentNotifications, modality));
            android.util.Slog.d(TAG, "frrStatsSet to persist: " + frrStatsSet);
            this.mSharedPreferences.edit().putStringSet(KEY, frrStatsSet).apply();
        } catch (org.json.JSONException e) {
            android.util.Slog.e(TAG, "Unable to persist authentication stats");
        }
    }

    public void persistFrrThreshold(float frrThreshold) {
        this.mSharedPreferences.edit().putFloat(THRESHOLD_KEY, frrThreshold).apply();
    }

    private java.util.Set<java.lang.String> readFrrStats() {
        return this.mSharedPreferences.getStringSet(KEY, java.util.Set.of());
    }

    private java.lang.String buildFrrStats(org.json.JSONObject frrStats, int totalAttempts, int rejectedAttempts, int enrollmentNotifications, int modality) throws org.json.JSONException {
        if (modality == 4) {
            return frrStats.put(FACE_ATTEMPTS, totalAttempts).put(FACE_REJECTIONS, rejectedAttempts).put(ENROLLMENT_NOTIFICATIONS, enrollmentNotifications).toString();
        }
        if (modality == 1) {
            return frrStats.put(FINGERPRINT_ATTEMPTS, totalAttempts).put(FINGERPRINT_REJECTIONS, rejectedAttempts).put(ENROLLMENT_NOTIFICATIONS, enrollmentNotifications).toString();
        }
        return frrStats.toString();
    }

    private java.lang.String getValue(org.json.JSONObject jsonObject, java.lang.String key) throws org.json.JSONException {
        return jsonObject.has(key) ? jsonObject.getString(key) : "";
    }

    private int getIntValue(org.json.JSONObject jsonObject, java.lang.String key) throws org.json.JSONException {
        return getIntValue(jsonObject, key, 0);
    }

    private int getIntValue(org.json.JSONObject jsonObject, java.lang.String key, int defaultValue) throws org.json.JSONException {
        return jsonObject.has(key) ? jsonObject.getInt(key) : defaultValue;
    }
}
