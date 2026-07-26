package com.android.server.grammaticalinflection;

/* JADX INFO: loaded from: classes2.dex */
public class GrammaticalInflectionBackupHelper {
    private static final java.lang.String SYSTEM_BACKUP_PACKAGE_KEY = "android";
    private final android.content.AttributionSource mAttributionSource;
    private final android.util.SparseArray<com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper.StagedData> mCache = new android.util.SparseArray<>();
    private final java.lang.Object mCacheLock = new java.lang.Object();
    private final java.time.Clock mClock = java.time.Clock.systemUTC();
    private final com.android.server.grammaticalinflection.GrammaticalInflectionService mGrammaticalGenderService;
    private final android.content.pm.PackageManager mPackageManager;
    private static final java.lang.String TAG = com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper.class.getSimpleName();
    private static final java.time.Duration STAGE_DATA_RETENTION_PERIOD = java.time.Duration.ofDays(3);

    static class StagedData {
        final long mCreationTimeMillis;
        final java.util.HashMap<java.lang.String, java.lang.Integer> mPackageStates = new java.util.HashMap<>();

        StagedData(long creationTimeMillis) {
            this.mCreationTimeMillis = creationTimeMillis;
        }
    }

    public GrammaticalInflectionBackupHelper(android.content.AttributionSource attributionSource, com.android.server.grammaticalinflection.GrammaticalInflectionService grammaticalGenderService, android.content.pm.PackageManager packageManager) {
        this.mAttributionSource = attributionSource;
        this.mGrammaticalGenderService = grammaticalGenderService;
        this.mPackageManager = packageManager;
    }

    public byte[] getBackupPayload(int userId) {
        synchronized (this.mCacheLock) {
            cleanStagedDataForOldEntries();
        }
        java.util.HashMap<java.lang.String, java.lang.Integer> pkgGenderInfo = new java.util.HashMap<>();
        for (android.content.pm.ApplicationInfo appInfo : this.mPackageManager.getInstalledApplicationsAsUser(android.content.pm.PackageManager.ApplicationInfoFlags.of(0L), userId)) {
            int gender = this.mGrammaticalGenderService.getApplicationGrammaticalGender(appInfo.packageName, userId);
            if (gender != 0) {
                pkgGenderInfo.put(appInfo.packageName, java.lang.Integer.valueOf(gender));
            }
        }
        if (!pkgGenderInfo.isEmpty()) {
            return convertToByteArray(pkgGenderInfo);
        }
        return null;
    }

    public void stageAndApplyRestoredPayload(byte[] payload, int userId) {
        synchronized (this.mCacheLock) {
            cleanStagedDataForOldEntries();
            java.util.HashMap<java.lang.String, java.lang.Integer> pkgInfo = readFromByteArray(payload);
            if (pkgInfo.isEmpty()) {
                return;
            }
            com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper.StagedData stagedData = new com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper.StagedData(this.mClock.millis());
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> info : pkgInfo.entrySet()) {
                if (isPackageInstalledForUser(info.getKey(), userId)) {
                    if (!hasSetBeforeRestoring(info.getKey(), userId)) {
                        this.mGrammaticalGenderService.setRequestedApplicationGrammaticalGender(info.getKey(), userId, info.getValue().intValue());
                    }
                } else if (info.getValue().intValue() != 0) {
                    stagedData.mPackageStates.put(info.getKey(), info.getValue());
                }
            }
            this.mCache.append(userId, stagedData);
        }
    }

    public byte[] getSystemBackupPayload(int userId) {
        int gender = this.mGrammaticalGenderService.getSystemGrammaticalGender(userId);
        return intToByteArray(gender);
    }

    public void applyRestoredSystemPayload(byte[] payload, int userId) {
        int gender = convertByteArrayToInt(payload);
        this.mGrammaticalGenderService.setSystemWideGrammaticalGender(gender, userId);
    }

    private boolean hasSetBeforeRestoring(java.lang.String pkgName, int userId) {
        return this.mGrammaticalGenderService.getApplicationGrammaticalGender(pkgName, userId) != 0;
    }

    public void onPackageAdded(java.lang.String packageName, int uid) {
        int grammaticalGender;
        synchronized (this.mCacheLock) {
            int userId = android.os.UserHandle.getUserId(uid);
            com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper.StagedData cache = this.mCache.get(userId);
            if (cache != null && cache.mPackageStates.containsKey(packageName) && (grammaticalGender = cache.mPackageStates.get(packageName).intValue()) != 0) {
                this.mGrammaticalGenderService.setRequestedApplicationGrammaticalGender(packageName, userId, grammaticalGender);
            }
        }
    }

    public void onPackageDataCleared() {
        notifyBackupManager();
    }

    public void onPackageRemoved() {
        notifyBackupManager();
    }

    public static void notifyBackupManager() {
        android.app.backup.BackupManager.dataChanged("android");
    }

    private static byte[] convertToByteArray(java.util.HashMap<java.lang.String, java.lang.Integer> pkgGenderInfo) {
        try {
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            try {
                java.io.ObjectOutputStream objStream = new java.io.ObjectOutputStream(out);
                try {
                    objStream.writeObject(pkgGenderInfo);
                    byte[] byteArray = out.toByteArray();
                    objStream.close();
                    out.close();
                    return byteArray;
                } finally {
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(TAG, "cannot convert payload to byte array.", e);
            return null;
        }
    }

    private static byte[] intToByteArray(int gender) {
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(4);
        bb.putInt(gender);
        return bb.array();
    }

    private static int convertByteArrayToInt(byte[] intBytes) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(intBytes);
        return byteBuffer.getInt();
    }

    private static java.util.HashMap<java.lang.String, java.lang.Integer> readFromByteArray(byte[] payload) {
        java.io.ByteArrayInputStream byteIn;
        java.util.HashMap<java.lang.String, java.lang.Integer> data = new java.util.HashMap<>();
        try {
            byteIn = new java.io.ByteArrayInputStream(payload);
        } catch (java.io.IOException | java.lang.ClassNotFoundException e) {
            android.util.Log.e(TAG, "cannot convert payload to HashMap.", e);
            e.printStackTrace();
        }
        try {
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(byteIn);
            try {
                data = (java.util.HashMap) in.readObject();
                in.close();
                byteIn.close();
                return data;
            } finally {
            }
        } catch (java.lang.Throwable th) {
            try {
                byteIn.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void cleanStagedDataForOldEntries() {
        int i = 0;
        while (i < this.mCache.size()) {
            int userId = this.mCache.keyAt(i);
            com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper.StagedData stagedData = this.mCache.valueAt(userId);
            if (stagedData.mCreationTimeMillis < this.mClock.millis() - STAGE_DATA_RETENTION_PERIOD.toMillis()) {
                this.mCache.removeAt(i);
                i--;
            }
            i++;
        }
    }

    private boolean isPackageInstalledForUser(java.lang.String packageName, int userId) {
        android.content.pm.PackageInfo pkgInfo = null;
        try {
            pkgInfo = this.mPackageManager.getPackageInfoAsUser(packageName, 0, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        return pkgInfo != null;
    }
}
