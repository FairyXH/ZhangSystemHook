package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public class DiskStatsFileLogger {
    public static final java.lang.String APP_CACHES_KEY = "cacheSizes";
    public static final java.lang.String APP_CACHE_AGG_KEY = "cacheSize";
    public static final java.lang.String APP_DATA_KEY = "appDataSizes";
    public static final java.lang.String APP_DATA_SIZE_AGG_KEY = "appDataSize";
    public static final java.lang.String APP_SIZES_KEY = "appSizes";
    public static final java.lang.String APP_SIZE_AGG_KEY = "appSize";
    public static final java.lang.String AUDIO_KEY = "audioSize";
    public static final java.lang.String DOWNLOADS_KEY = "downloadsSize";
    public static final java.lang.String LAST_QUERY_TIMESTAMP_KEY = "queryTime";
    public static final java.lang.String MISC_KEY = "otherSize";
    public static final java.lang.String PACKAGE_NAMES_KEY = "packageNames";
    public static final java.lang.String PHOTOS_KEY = "photosSize";
    public static final java.lang.String SYSTEM_KEY = "systemSize";
    private static final java.lang.String TAG = "DiskStatsLogger";
    public static final java.lang.String VIDEOS_KEY = "videosSize";
    private long mDownloadsSize;
    private java.util.List<android.content.pm.PackageStats> mPackageStats;
    private com.android.server.storage.FileCollector.MeasurementResult mResult;
    private long mSystemSize;

    public DiskStatsFileLogger(com.android.server.storage.FileCollector.MeasurementResult result, com.android.server.storage.FileCollector.MeasurementResult downloadsResult, java.util.List<android.content.pm.PackageStats> stats, long systemSize) {
        this.mResult = result;
        this.mDownloadsSize = downloadsResult.totalAccountedSize();
        this.mSystemSize = systemSize;
        this.mPackageStats = stats;
    }

    public void dumpToFile(java.io.File file) throws java.io.FileNotFoundException {
        java.io.PrintWriter pw = new java.io.PrintWriter(file);
        org.json.JSONObject representation = getJsonRepresentation();
        if (representation != null) {
            pw.println(representation);
        }
        pw.close();
    }

    private org.json.JSONObject getJsonRepresentation() {
        org.json.JSONObject json = new org.json.JSONObject();
        try {
            json.put(LAST_QUERY_TIMESTAMP_KEY, java.lang.System.currentTimeMillis());
            json.put(PHOTOS_KEY, this.mResult.imagesSize);
            json.put(VIDEOS_KEY, this.mResult.videosSize);
            json.put(AUDIO_KEY, this.mResult.audioSize);
            json.put(DOWNLOADS_KEY, this.mDownloadsSize);
            json.put(SYSTEM_KEY, this.mSystemSize);
            json.put(MISC_KEY, this.mResult.miscSize);
            addAppsToJson(json);
            return json;
        } catch (org.json.JSONException e) {
            android.util.Log.e(TAG, e.toString());
            return null;
        }
    }

    private void addAppsToJson(org.json.JSONObject json) throws org.json.JSONException {
        long cacheSizeSum;
        org.json.JSONArray names = new org.json.JSONArray();
        org.json.JSONArray appSizeList = new org.json.JSONArray();
        org.json.JSONArray cacheSizeList = new org.json.JSONArray();
        org.json.JSONArray cacheSizeList2 = new org.json.JSONArray();
        long appSizeSum = 0;
        long appDataSizeSum = 0;
        long cacheSizeSum2 = 0;
        boolean isExternal = android.os.Environment.isExternalStorageEmulated();
        java.util.Iterator<java.util.Map.Entry<java.lang.String, android.content.pm.PackageStats>> it = filterOnlyPrimaryUser().entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<java.lang.String, android.content.pm.PackageStats> entry = it.next();
            android.content.pm.PackageStats stat = entry.getValue();
            java.util.Iterator<java.util.Map.Entry<java.lang.String, android.content.pm.PackageStats>> it2 = it;
            long appSize = stat.codeSize;
            org.json.JSONArray appDataSizeList = cacheSizeList;
            org.json.JSONArray cacheSizeList3 = cacheSizeList2;
            long appDataSize = stat.dataSize;
            org.json.JSONArray names2 = names;
            long cacheSize = stat.cacheSize;
            if (!isExternal) {
                cacheSizeSum = cacheSizeSum2;
            } else {
                cacheSizeSum = cacheSizeSum2;
                long cacheSizeSum3 = stat.externalCodeSize;
                appSize += cacheSizeSum3;
                appDataSize += stat.externalDataSize;
                cacheSize += stat.externalCacheSize;
            }
            long appSizeSum2 = appSizeSum + appSize;
            appDataSizeSum += appDataSize;
            cacheSizeSum2 = cacheSizeSum + cacheSize;
            names2.put(stat.packageName);
            appSizeList.put(appSize);
            appDataSizeList.put(appDataSize);
            cacheSizeList3.put(cacheSize);
            cacheSizeList2 = cacheSizeList3;
            cacheSizeList = appDataSizeList;
            names = names2;
            it = it2;
            appSizeSum = appSizeSum2;
        }
        org.json.JSONArray names3 = names;
        org.json.JSONArray names4 = cacheSizeList;
        json.put(PACKAGE_NAMES_KEY, names3);
        json.put(APP_SIZES_KEY, appSizeList);
        json.put(APP_CACHES_KEY, cacheSizeList2);
        json.put(APP_DATA_KEY, names4);
        json.put(APP_SIZE_AGG_KEY, appSizeSum);
        json.put(APP_CACHE_AGG_KEY, cacheSizeSum2);
        json.put(APP_DATA_SIZE_AGG_KEY, appDataSizeSum);
    }

    private android.util.ArrayMap<java.lang.String, android.content.pm.PackageStats> filterOnlyPrimaryUser() {
        android.util.ArrayMap<java.lang.String, android.content.pm.PackageStats> packageMap = new android.util.ArrayMap<>();
        for (android.content.pm.PackageStats stat : this.mPackageStats) {
            if (stat.userHandle == 0) {
                android.content.pm.PackageStats existingStats = packageMap.get(stat.packageName);
                if (existingStats != null) {
                    existingStats.cacheSize += stat.cacheSize;
                    existingStats.codeSize += stat.codeSize;
                    existingStats.dataSize += stat.dataSize;
                    existingStats.externalCacheSize += stat.externalCacheSize;
                    existingStats.externalCodeSize += stat.externalCodeSize;
                    existingStats.externalDataSize += stat.externalDataSize;
                } else {
                    packageMap.put(stat.packageName, new android.content.pm.PackageStats(stat));
                }
            }
        }
        return packageMap;
    }
}
