package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class WatchlistLoggingHandler extends android.os.Handler {
    private static final boolean DEBUG = false;
    private static final java.lang.String DROPBOX_TAG = "network_watchlist_report";
    static final int FORCE_REPORT_RECORDS_NOW_FOR_TEST_MSG = 3;
    static final int LOG_WATCHLIST_EVENT_MSG = 1;
    static final int REPORT_RECORDS_IF_NECESSARY_MSG = 2;
    private final com.android.server.net.watchlist.FileHashCache mApkHashCache;
    private final java.util.concurrent.ConcurrentHashMap<java.lang.Integer, byte[]> mCachedUidDigestMap;
    private final com.android.server.net.watchlist.WatchlistConfig mConfig;
    private final android.content.Context mContext;
    private final com.android.server.net.watchlist.WatchlistReportDbHelper mDbHelper;
    private final android.os.DropBoxManager mDropBoxManager;
    private final android.content.pm.PackageManager mPm;
    private int mPrimaryUserId;
    private final android.content.ContentResolver mResolver;
    private final com.android.server.net.watchlist.WatchlistSettings mSettings;
    private static final java.lang.String TAG = com.android.server.net.watchlist.WatchlistLoggingHandler.class.getSimpleName();
    private static final long ONE_DAY_MS = java.util.concurrent.TimeUnit.DAYS.toMillis(1);

    private interface WatchlistEventKeys {
        public static final java.lang.String HOST = "host";
        public static final java.lang.String IP_ADDRESSES = "ipAddresses";
        public static final java.lang.String TIMESTAMP = "timestamp";
        public static final java.lang.String UID = "uid";
    }

    WatchlistLoggingHandler(android.content.Context context, android.os.Looper looper) {
        super(looper);
        this.mPrimaryUserId = -1;
        this.mCachedUidDigestMap = new java.util.concurrent.ConcurrentHashMap<>();
        this.mContext = context;
        this.mPm = this.mContext.getPackageManager();
        this.mResolver = this.mContext.getContentResolver();
        this.mDbHelper = com.android.server.net.watchlist.WatchlistReportDbHelper.getInstance(context);
        this.mConfig = com.android.server.net.watchlist.WatchlistConfig.getInstance();
        this.mSettings = com.android.server.net.watchlist.WatchlistSettings.getInstance();
        this.mDropBoxManager = (android.os.DropBoxManager) this.mContext.getSystemService(android.os.DropBoxManager.class);
        this.mPrimaryUserId = getPrimaryUserId();
        if (context.getResources().getBoolean(android.R.bool.config_useCurrentRotationOnRotationLockChange)) {
            this.mApkHashCache = new com.android.server.net.watchlist.FileHashCache(this);
            android.util.Slog.i(TAG, "Using file hashes cache.");
        } else {
            this.mApkHashCache = null;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                android.os.Bundle data = msg.getData();
                handleNetworkEvent(data.getString("host"), data.getStringArray(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.IP_ADDRESSES), data.getInt("uid"), data.getLong(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP));
                break;
            case 2:
                tryAggregateRecords(getLastMidnightTime());
                break;
            case 3:
                if (msg.obj instanceof java.lang.Long) {
                    long lastRecordTime = ((java.lang.Long) msg.obj).longValue();
                    tryAggregateRecords(lastRecordTime);
                } else {
                    android.util.Slog.e(TAG, "Msg.obj needs to be a Long object.");
                }
                break;
            default:
                android.util.Slog.d(TAG, "WatchlistLoggingHandler received an unknown of message.");
                break;
        }
    }

    private int getPrimaryUserId() {
        android.content.pm.UserInfo primaryUserInfo = ((android.os.UserManager) this.mContext.getSystemService("user")).getPrimaryUser();
        if (primaryUserInfo != null) {
            return primaryUserInfo.id;
        }
        return -1;
    }

    private boolean isPackageTestOnly(int uid) {
        try {
            java.lang.String[] packageNames = this.mPm.getPackagesForUid(uid);
            if (packageNames != null && packageNames.length != 0) {
                android.content.pm.ApplicationInfo ai = this.mPm.getApplicationInfo(packageNames[0], 0);
                return (ai.flags & 256) != 0;
            }
            android.util.Slog.e(TAG, "Couldn't find package: " + java.util.Arrays.toString(packageNames));
            return false;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void reportWatchlistIfNecessary() {
        android.os.Message msg = obtainMessage(2);
        sendMessage(msg);
    }

    public void forceReportWatchlistForTest(long lastReportTime) {
        android.os.Message msg = obtainMessage(3);
        msg.obj = java.lang.Long.valueOf(lastReportTime);
        sendMessage(msg);
    }

    public void asyncNetworkEvent(java.lang.String host, java.lang.String[] ipAddresses, int uid) {
        android.os.Message msg = obtainMessage(1);
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString("host", host);
        bundle.putStringArray(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.IP_ADDRESSES, ipAddresses);
        bundle.putInt("uid", uid);
        bundle.putLong(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, java.lang.System.currentTimeMillis());
        msg.setData(bundle);
        sendMessage(msg);
    }

    private void handleNetworkEvent(java.lang.String hostname, java.lang.String[] ipAddresses, int uid, long timestamp) {
        if (this.mPrimaryUserId == -1) {
            this.mPrimaryUserId = getPrimaryUserId();
        }
        if (android.os.UserHandle.getUserId(uid) != this.mPrimaryUserId) {
            return;
        }
        java.lang.String cncDomain = searchAllSubDomainsInWatchlist(hostname);
        if (cncDomain != null) {
            insertRecord(uid, cncDomain, timestamp);
            return;
        }
        java.lang.String cncIp = searchIpInWatchlist(ipAddresses);
        if (cncIp != null) {
            insertRecord(uid, cncIp, timestamp);
        }
    }

    private void insertRecord(int uid, java.lang.String cncHost, long timestamp) {
        byte[] digest;
        if ((this.mConfig.isConfigSecure() || isPackageTestOnly(uid)) && (digest = getDigestFromUid(uid)) != null && this.mDbHelper.insertNewRecord(digest, cncHost, timestamp)) {
            android.util.Slog.w(TAG, "Unable to insert record for uid: " + uid);
        }
    }

    private boolean shouldReportNetworkWatchlist(long lastRecordTime) {
        long lastReportTime = android.provider.Settings.Global.getLong(this.mResolver, "network_watchlist_last_report_time", 0L);
        if (lastRecordTime >= lastReportTime) {
            return lastRecordTime >= ONE_DAY_MS + lastReportTime;
        }
        android.util.Slog.i(TAG, "Last report time is larger than current time, reset report");
        this.mDbHelper.cleanup(lastReportTime);
        return false;
    }

    private void tryAggregateRecords(long lastRecordTime) {
        long endTime;
        java.lang.String str;
        java.lang.StringBuilder sb;
        long startTime = java.lang.System.currentTimeMillis();
        try {
            try {
            } catch (android.database.sqlite.SQLiteDatabaseCorruptException e) {
                android.util.Slog.w(TAG, "Database exception", e);
                endTime = java.lang.System.currentTimeMillis();
                str = TAG;
                sb = new java.lang.StringBuilder();
            }
            if (!shouldReportNetworkWatchlist(lastRecordTime)) {
                android.util.Slog.i(TAG, "No need to aggregate record yet.");
                return;
            }
            android.util.Slog.i(TAG, "Start aggregating watchlist records.");
            if (this.mDropBoxManager == null || !this.mDropBoxManager.isTagEnabled(DROPBOX_TAG)) {
                android.util.Slog.w(TAG, "Network Watchlist dropbox tag is not enabled");
            } else {
                android.provider.Settings.Global.putLong(this.mResolver, "network_watchlist_last_report_time", lastRecordTime);
                com.android.server.net.watchlist.WatchlistReportDbHelper.AggregatedResult aggregatedResult = this.mDbHelper.getAggregatedRecords(lastRecordTime);
                if (aggregatedResult == null) {
                    android.util.Slog.i(TAG, "Cannot get result from database");
                    return;
                }
                java.util.List<java.lang.String> digestsForReport = getAllDigestsForReport(aggregatedResult);
                byte[] secretKey = this.mSettings.getPrivacySecretKey();
                byte[] encodedResult = com.android.server.net.watchlist.ReportEncoder.encodeWatchlistReport(this.mConfig, secretKey, digestsForReport, aggregatedResult);
                if (encodedResult != null) {
                    addEncodedReportToDropBox(encodedResult);
                }
            }
            this.mDbHelper.cleanup(lastRecordTime);
            endTime = java.lang.System.currentTimeMillis();
            str = TAG;
            sb = new java.lang.StringBuilder();
            android.util.Slog.i(str, sb.append("Milliseconds spent on tryAggregateRecords(): ").append(endTime - startTime).toString());
        } finally {
            long endTime2 = java.lang.System.currentTimeMillis();
            android.util.Slog.i(TAG, "Milliseconds spent on tryAggregateRecords(): " + (endTime2 - startTime));
        }
    }

    java.util.List<java.lang.String> getAllDigestsForReport(com.android.server.net.watchlist.WatchlistReportDbHelper.AggregatedResult record) {
        java.util.List<android.content.pm.ApplicationInfo> apps = this.mContext.getPackageManager().getInstalledApplications(131072);
        java.util.HashSet<java.lang.String> result = new java.util.HashSet<>(apps.size() + record.appDigestCNCList.size());
        int size = apps.size();
        for (int i = 0; i < size; i++) {
            byte[] digest = getDigestFromUid(apps.get(i).uid);
            if (digest != null) {
                result.add(com.android.internal.util.HexDump.toHexString(digest));
            }
        }
        result.addAll(record.appDigestCNCList.keySet());
        return new java.util.ArrayList(result);
    }

    private void addEncodedReportToDropBox(byte[] encodedReport) {
        this.mDropBoxManager.addData(DROPBOX_TAG, encodedReport, 0);
    }

    private byte[] getDigestFromUid(final int uid) {
        return this.mCachedUidDigestMap.computeIfAbsent(java.lang.Integer.valueOf(uid), new java.util.function.Function() { // from class: com.android.server.net.watchlist.WatchlistLoggingHandler$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$getDigestFromUid$0(uid, (java.lang.Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ byte[] lambda$getDigestFromUid$0(int uid, java.lang.Integer key) {
        java.lang.String[] packageNames = this.mPm.getPackagesForUid(key.intValue());
        int userId = android.os.UserHandle.getUserId(uid);
        if (!com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
            for (java.lang.String packageName : packageNames) {
                try {
                    java.lang.String apkPath = this.mPm.getPackageInfoAsUser(packageName, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, userId).applicationInfo.publicSourceDir;
                    if (android.text.TextUtils.isEmpty(apkPath)) {
                        android.util.Slog.w(TAG, "Cannot find apkPath for " + packageName);
                    } else if (android.os.incremental.IncrementalManager.isIncrementalPath(apkPath)) {
                        android.util.Slog.i(TAG, "Skipping incremental path: " + packageName);
                    } else {
                        if (this.mApkHashCache != null) {
                            return this.mApkHashCache.getSha256Hash(new java.io.File(apkPath));
                        }
                        return com.android.server.net.watchlist.DigestUtils.getSha256Hash(new java.io.File(apkPath));
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException | java.io.IOException | java.security.NoSuchAlgorithmException e) {
                    android.util.Slog.e(TAG, "Cannot get digest from uid: " + key + ",pkg: " + packageName, e);
                    return null;
                }
            }
        }
        return null;
    }

    private java.lang.String searchIpInWatchlist(java.lang.String[] ipAddresses) {
        for (java.lang.String ipAddress : ipAddresses) {
            if (isIpInWatchlist(ipAddress)) {
                return ipAddress;
            }
        }
        return null;
    }

    private boolean isIpInWatchlist(java.lang.String ipAddr) {
        if (ipAddr == null) {
            return false;
        }
        return this.mConfig.containsIp(ipAddr);
    }

    private boolean isHostInWatchlist(java.lang.String host) {
        if (host == null) {
            return false;
        }
        return this.mConfig.containsDomain(host);
    }

    private java.lang.String searchAllSubDomainsInWatchlist(java.lang.String host) {
        if (host == null) {
            return null;
        }
        java.lang.String[] subDomains = getAllSubDomains(host);
        for (java.lang.String subDomain : subDomains) {
            if (isHostInWatchlist(subDomain)) {
                return subDomain;
            }
        }
        return null;
    }

    static java.lang.String[] getAllSubDomains(java.lang.String host) {
        if (host == null) {
            return null;
        }
        java.util.ArrayList<java.lang.String> subDomainList = new java.util.ArrayList<>();
        subDomainList.add(host);
        int index = host.indexOf(".");
        while (index != -1) {
            host = host.substring(index + 1);
            if (!android.text.TextUtils.isEmpty(host)) {
                subDomainList.add(host);
            }
            index = host.indexOf(".");
        }
        return (java.lang.String[]) subDomainList.toArray(new java.lang.String[0]);
    }

    static long getLastMidnightTime() {
        return getMidnightTimestamp(0);
    }

    static long getMidnightTimestamp(int daysBefore) {
        java.util.Calendar date = new java.util.GregorianCalendar();
        date.set(11, 0);
        date.set(12, 0);
        date.set(13, 0);
        date.set(14, 0);
        date.add(5, -daysBefore);
        return date.getTimeInMillis();
    }
}
