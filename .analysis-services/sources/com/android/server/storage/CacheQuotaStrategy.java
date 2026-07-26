package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public class CacheQuotaStrategy implements android.os.RemoteCallback.OnResultListener {
    private static final java.lang.String ATTR_PREVIOUS_BYTES = "previousBytes";
    private static final java.lang.String ATTR_QUOTA_IN_BYTES = "bytes";
    private static final java.lang.String ATTR_UID = "uid";
    private static final java.lang.String ATTR_UUID = "uuid";
    private static final java.lang.String CACHE_INFO_TAG = "cache-info";
    private static final java.lang.String TAG = "CacheQuotaStrategy";
    private static final java.lang.String TAG_QUOTA = "quota";
    private final android.content.Context mContext;
    private final com.android.server.pm.Installer mInstaller;
    private final java.lang.Object mLock = new java.lang.Object();
    private android.util.AtomicFile mPreviousValuesFile = new android.util.AtomicFile(new java.io.File(new java.io.File(android.os.Environment.getDataDirectory(), "system"), "cachequota.xml"));
    private final android.util.ArrayMap<java.lang.String, android.util.SparseLongArray> mQuotaMap;
    private android.app.usage.ICacheQuotaService mRemoteService;
    private android.content.ServiceConnection mServiceConnection;
    private final android.app.usage.UsageStatsManagerInternal mUsageStats;

    public CacheQuotaStrategy(android.content.Context context, android.app.usage.UsageStatsManagerInternal usageStatsManager, com.android.server.pm.Installer installer, android.util.ArrayMap<java.lang.String, android.util.SparseLongArray> quotaMap) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mUsageStats = (android.app.usage.UsageStatsManagerInternal) java.util.Objects.requireNonNull(usageStatsManager);
        this.mInstaller = (com.android.server.pm.Installer) java.util.Objects.requireNonNull(installer);
        this.mQuotaMap = (android.util.ArrayMap) java.util.Objects.requireNonNull(quotaMap);
    }

    public void recalculateQuotas() {
        createServiceConnection();
        android.content.ComponentName component = getServiceComponentName();
        if (component != null) {
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(component);
            this.mContext.bindServiceAsUser(intent, this.mServiceConnection, 1, android.os.UserHandle.CURRENT);
        }
    }

    private void createServiceConnection() {
        if (this.mServiceConnection != null) {
            return;
        }
        this.mServiceConnection = new android.content.ServiceConnection() { // from class: com.android.server.storage.CacheQuotaStrategy.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName name, final android.os.IBinder service) {
                java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.storage.CacheQuotaStrategy.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (com.android.server.storage.CacheQuotaStrategy.this.mLock) {
                            com.android.server.storage.CacheQuotaStrategy.this.mRemoteService = android.app.usage.ICacheQuotaService.Stub.asInterface(service);
                            java.util.List<android.app.usage.CacheQuotaHint> requests = com.android.server.storage.CacheQuotaStrategy.this.getUnfulfilledRequests();
                            android.os.RemoteCallback remoteCallback = new android.os.RemoteCallback(com.android.server.storage.CacheQuotaStrategy.this);
                            try {
                                com.android.server.storage.CacheQuotaStrategy.this.mRemoteService.computeCacheQuotaHints(remoteCallback, requests);
                            } catch (java.lang.Exception ex) {
                                android.util.Slog.w(com.android.server.storage.CacheQuotaStrategy.TAG, "Remote exception occurred while trying to get cache quota", ex);
                            }
                        }
                    }
                };
                android.os.AsyncTask.execute(runnable);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName name) {
                synchronized (com.android.server.storage.CacheQuotaStrategy.this.mLock) {
                    com.android.server.storage.CacheQuotaStrategy.this.mRemoteService = null;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.app.usage.CacheQuotaHint> getUnfulfilledRequests() {
        com.android.server.storage.CacheQuotaStrategy cacheQuotaStrategy = this;
        long timeNow = java.lang.System.currentTimeMillis();
        long oneYearAgo = timeNow - 31449600000L;
        java.util.List<android.app.usage.CacheQuotaHint> requests = new java.util.ArrayList<>();
        android.os.UserManager um = (android.os.UserManager) cacheQuotaStrategy.mContext.getSystemService(android.os.UserManager.class);
        java.util.List<android.content.pm.UserInfo> users = um.getUsers();
        android.content.pm.PackageManager packageManager = cacheQuotaStrategy.mContext.getPackageManager();
        for (android.content.pm.UserInfo info : users) {
            android.content.pm.UserInfo info2 = info;
            android.content.pm.PackageManager packageManager2 = packageManager;
            java.util.List<android.app.usage.UsageStats> stats = cacheQuotaStrategy.mUsageStats.queryUsageStatsForUser(info.id, 4, oneYearAgo, timeNow, false);
            if (stats == null) {
                packageManager = packageManager2;
                cacheQuotaStrategy = this;
            } else {
                int i = 0;
                while (i < stats.size()) {
                    android.app.usage.UsageStats stat = stats.get(i);
                    java.lang.String packageName = stat.getPackageName();
                    android.content.pm.UserInfo info3 = info2;
                    try {
                        android.content.pm.ApplicationInfo appInfo = packageManager2.getApplicationInfoAsUser(packageName, 0, info3.id);
                        requests.add(new android.app.usage.CacheQuotaHint.Builder().setVolumeUuid(appInfo.volumeUuid).setUid(appInfo.uid).setUsageStats(stat).setQuota(-1L).build());
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    }
                    i++;
                    info2 = info3;
                }
                packageManager = packageManager2;
                cacheQuotaStrategy = this;
            }
        }
        return requests;
    }

    public void onResult(android.os.Bundle data) {
        java.util.List<android.app.usage.CacheQuotaHint> processedRequests = data.getParcelableArrayList("requests", android.app.usage.CacheQuotaHint.class);
        pushProcessedQuotas(processedRequests);
        writeXmlToFile(processedRequests);
    }

    private void pushProcessedQuotas(java.util.List<android.app.usage.CacheQuotaHint> processedRequests) {
        for (android.app.usage.CacheQuotaHint request : processedRequests) {
            long proposedQuota = request.getQuota();
            if (proposedQuota != -1) {
                try {
                    int uid = request.getUid();
                    this.mInstaller.setAppQuota(request.getVolumeUuid(), android.os.UserHandle.getUserId(uid), android.os.UserHandle.getAppId(uid), proposedQuota);
                    insertIntoQuotaMap(request.getVolumeUuid(), android.os.UserHandle.getUserId(uid), android.os.UserHandle.getAppId(uid), proposedQuota);
                } catch (com.android.server.pm.Installer.InstallerException ex) {
                    android.util.Slog.w(TAG, "Failed to set cache quota for " + request.getUid(), ex);
                }
            }
        }
        disconnectService();
    }

    private void insertIntoQuotaMap(java.lang.String volumeUuid, int userId, int appId, long quota) {
        android.util.SparseLongArray volumeMap = this.mQuotaMap.get(volumeUuid);
        if (volumeMap == null) {
            volumeMap = new android.util.SparseLongArray();
            this.mQuotaMap.put(volumeUuid, volumeMap);
        }
        volumeMap.put(android.os.UserHandle.getUid(userId, appId), quota);
    }

    private void disconnectService() {
        if (this.mServiceConnection != null) {
            this.mContext.unbindService(this.mServiceConnection);
            this.mServiceConnection = null;
        }
    }

    private android.content.ComponentName getServiceComponentName() {
        java.lang.String packageName = this.mContext.getPackageManager().getServicesSystemSharedLibraryPackageName();
        if (packageName == null) {
            android.util.Slog.w(TAG, "could not access the cache quota service: no package!");
            return null;
        }
        android.content.Intent intent = new android.content.Intent("android.app.usage.CacheQuotaService");
        intent.setPackage(packageName);
        android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveService(intent, 132);
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            android.util.Slog.w(TAG, "No valid components found.");
            return null;
        }
        android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        return new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    private void writeXmlToFile(java.util.List<android.app.usage.CacheQuotaHint> processedRequests) {
        java.io.FileOutputStream fileStream = null;
        try {
            fileStream = this.mPreviousValuesFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fileStream);
            android.os.StatFs stats = new android.os.StatFs(android.os.Environment.getDataDirectory().getAbsolutePath());
            saveToXml(out, processedRequests, stats.getAvailableBytes());
            this.mPreviousValuesFile.finishWrite(fileStream);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "An error occurred while writing the cache quota file.", e);
            this.mPreviousValuesFile.failWrite(fileStream);
        }
    }

    public long setupQuotasFromFile() throws java.io.IOException {
        try {
            java.io.FileInputStream stream = this.mPreviousValuesFile.openRead();
            try {
                try {
                    android.util.Pair<java.lang.Long, java.util.List<android.app.usage.CacheQuotaHint>> cachedValues = readFromXml(stream);
                    if (stream != null) {
                        stream.close();
                    }
                    if (cachedValues == null) {
                        android.util.Slog.e(TAG, "An error occurred while parsing the cache quota file.");
                        return -1L;
                    }
                    pushProcessedQuotas((java.util.List) cachedValues.second);
                    return ((java.lang.Long) cachedValues.first).longValue();
                } catch (org.xmlpull.v1.XmlPullParserException e) {
                    throw new java.lang.IllegalStateException(e.getMessage());
                }
            } finally {
            }
        } catch (java.io.FileNotFoundException e2) {
            return -1L;
        }
    }

    static void saveToXml(com.android.modules.utils.TypedXmlSerializer out, java.util.List<android.app.usage.CacheQuotaHint> requests, long bytesWhenCalculated) throws java.io.IOException {
        out.startDocument((java.lang.String) null, true);
        out.startTag((java.lang.String) null, CACHE_INFO_TAG);
        out.attributeLong((java.lang.String) null, ATTR_PREVIOUS_BYTES, bytesWhenCalculated);
        for (android.app.usage.CacheQuotaHint request : requests) {
            out.startTag((java.lang.String) null, TAG_QUOTA);
            java.lang.String uuid = request.getVolumeUuid();
            if (uuid != null) {
                out.attribute((java.lang.String) null, ATTR_UUID, request.getVolumeUuid());
            }
            out.attributeInt((java.lang.String) null, "uid", request.getUid());
            out.attributeLong((java.lang.String) null, ATTR_QUOTA_IN_BYTES, request.getQuota());
            out.endTag((java.lang.String) null, TAG_QUOTA);
        }
        out.endTag((java.lang.String) null, CACHE_INFO_TAG);
        out.endDocument();
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected static android.util.Pair<java.lang.Long, java.util.List<android.app.usage.CacheQuotaHint>> readFromXml(java.io.InputStream r9) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            com.android.modules.utils.TypedXmlPullParser r0 = android.util.Xml.resolvePullParser(r9)
            int r1 = r0.getEventType()
        L8:
            r2 = 2
            r3 = 1
            if (r1 == r2) goto L13
            if (r1 == r3) goto L13
            int r1 = r0.next()
            goto L8
        L13:
            r4 = 0
            if (r1 != r3) goto L1e
            java.lang.String r2 = "CacheQuotaStrategy"
            java.lang.String r3 = "No quotas found in quota file."
            android.util.Slog.d(r2, r3)
            return r4
        L1e:
            java.lang.String r5 = r0.getName()
            java.lang.String r6 = "cache-info"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L6d
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            java.lang.String r7 = "previousBytes"
            long r7 = r0.getAttributeLong(r4, r7)     // Catch: java.lang.NumberFormatException -> L64
            int r1 = r0.next()
        L3b:
            if (r1 != r2) goto L54
            java.lang.String r5 = r0.getName()
            java.lang.String r4 = "quota"
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L54
            android.app.usage.CacheQuotaHint r4 = getRequestFromXml(r0)
            if (r4 != 0) goto L51
            goto L58
        L51:
            r6.add(r4)
        L54:
            int r1 = r0.next()
        L58:
            if (r1 != r3) goto L3b
            android.util.Pair r2 = new android.util.Pair
            java.lang.Long r3 = java.lang.Long.valueOf(r7)
            r2.<init>(r3, r6)
            return r2
        L64:
            r2 = move-exception
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException
            java.lang.String r4 = "Previous bytes formatted incorrectly; aborting quota read."
            r3.<init>(r4)
            throw r3
        L6d:
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException
            java.lang.String r3 = "Invalid starting tag."
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.storage.CacheQuotaStrategy.readFromXml(java.io.InputStream):android.util.Pair");
    }

    static android.app.usage.CacheQuotaHint getRequestFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        try {
            java.lang.String uuid = parser.getAttributeValue((java.lang.String) null, ATTR_UUID);
            int uid = parser.getAttributeInt((java.lang.String) null, "uid");
            long bytes = parser.getAttributeLong((java.lang.String) null, ATTR_QUOTA_IN_BYTES);
            return new android.app.usage.CacheQuotaHint.Builder().setVolumeUuid(uuid).setUid(uid).setQuota(bytes).build();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Invalid cache quota request, skipping.");
            return null;
        }
    }
}
