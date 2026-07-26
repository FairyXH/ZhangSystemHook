package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public class SyncStorageEngine {
    private static final int ACCOUNTS_VERSION = 3;
    private static final java.lang.String ACCOUNT_INFO_FILE_NAME = "accounts.xml";
    private static final double DEFAULT_FLEX_PERCENT_SYNC = 0.04d;
    private static final long DEFAULT_MIN_FLEX_ALLOWED_SECS = 5;
    private static final long DEFAULT_POLL_FREQUENCY_SECONDS = 86400;
    private static final boolean DELETE_LEGACY_PARCEL_FILES = true;
    public static final int EVENT_START = 0;
    public static final int EVENT_STOP = 1;
    private static final java.lang.String LEGACY_STATISTICS_FILE_NAME = "stats.bin";
    private static final java.lang.String LEGACY_STATUS_FILE_NAME = "status.bin";
    public static final int MAX_HISTORY = 100;
    public static final java.lang.String MESG_CANCELED = "canceled";
    public static final java.lang.String MESG_SUCCESS = "success";
    static final long MILLIS_IN_4WEEKS = 2419200000L;
    private static final int MSG_WRITE_STATISTICS = 2;
    private static final int MSG_WRITE_STATUS = 1;
    public static final long NOT_IN_BACKOFF_MODE = -1;
    public static final int SOURCE_FEED = 5;
    public static final int SOURCE_LOCAL = 1;
    public static final int SOURCE_OTHER = 0;
    public static final int SOURCE_PERIODIC = 4;
    public static final int SOURCE_POLL = 2;
    public static final int SOURCE_USER = 3;
    public static final int STATISTICS_FILE_END = 0;
    public static final int STATISTICS_FILE_ITEM = 101;
    public static final int STATISTICS_FILE_ITEM_OLD = 100;
    private static final java.lang.String STATISTICS_FILE_NAME = "stats";
    public static final int STATUS_FILE_END = 0;
    public static final int STATUS_FILE_ITEM = 100;
    private static final java.lang.String STATUS_FILE_NAME = "status";
    private static final java.lang.String SYNC_DIR_NAME = "sync";
    private static final boolean SYNC_ENABLED_DEFAULT = false;
    private static final java.lang.String TAG = "SyncManager";
    private static final java.lang.String TAG_FILE = "SyncManagerFile";
    private static final long WRITE_STATISTICS_DELAY = 1800000;
    private static final long WRITE_STATUS_DELAY = 600000;
    private static final java.lang.String XML_ATTR_ENABLED = "enabled";
    private static final java.lang.String XML_ATTR_LISTEN_FOR_TICKLES = "listen-for-tickles";
    private static final java.lang.String XML_ATTR_NEXT_AUTHORITY_ID = "nextAuthorityId";
    private static final java.lang.String XML_ATTR_SYNC_RANDOM_OFFSET = "offsetInSeconds";
    private static final java.lang.String XML_ATTR_USER = "user";
    private static final java.lang.String XML_TAG_LISTEN_FOR_TICKLES = "listenForTickles";
    private static com.android.server.content.SyncStorageEngine.PeriodicSyncAddedListener mPeriodicSyncAddedListener;
    private static volatile com.android.server.content.SyncStorageEngine sSyncStorageEngine;
    private final android.util.AtomicFile mAccountInfoFile;
    private com.android.server.content.SyncStorageEngine.OnAuthorityRemovedListener mAuthorityRemovedListener;
    private final java.util.Calendar mCal;
    private final android.content.Context mContext;
    private boolean mDefaultMasterSyncAutomatically;
    private boolean mGrantSyncAdaptersAccountAccess;
    private final com.android.server.content.SyncStorageEngine.MyHandler mHandler;
    private volatile boolean mIsClockValid;
    private volatile boolean mIsJobAttributionFixed;
    private volatile boolean mIsJobNamespaceMigrated;
    private final com.android.server.content.SyncLogger mLogger;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final android.util.AtomicFile mStatisticsFile;
    private final android.util.AtomicFile mStatusFile;
    private java.io.File mSyncDir;
    private int mSyncRandomOffset;
    private com.android.server.content.SyncStorageEngine.OnSyncRequestListener mSyncRequestListener;
    private com.android.server.content.ISyncStorageEngineExt mSyncStorageEngineExt;
    private int mYear;
    private int mYearInDays;
    public static final java.lang.String[] SOURCES = {"OTHER", "LOCAL", "POLL", "USER", "PERIODIC", "FEED"};
    private static java.util.HashMap<java.lang.String, java.lang.String> sAuthorityRenames = new java.util.HashMap<>();
    final android.util.SparseArray<com.android.server.content.SyncStorageEngine.AuthorityInfo> mAuthorities = new android.util.SparseArray<>();
    private final java.util.HashMap<android.accounts.AccountAndUser, com.android.server.content.SyncStorageEngine.AccountInfo> mAccounts = new java.util.HashMap<>();
    private final android.util.SparseArray<java.util.ArrayList<android.content.SyncInfo>> mCurrentSyncs = new android.util.SparseArray<>();
    final android.util.SparseArray<android.content.SyncStatusInfo> mSyncStatus = new android.util.SparseArray<>();
    private final java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> mSyncHistory = new java.util.ArrayList<>();
    private final android.os.RemoteCallbackList<android.content.ISyncStatusObserver> mChangeListeners = new android.os.RemoteCallbackList<>();
    private final android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<com.android.server.content.SyncStorageEngine.AuthorityInfo>> mServices = new android.util.ArrayMap<>();
    private int mNextAuthorityId = 0;
    final com.android.server.content.SyncStorageEngine.DayStats[] mDayStats = new com.android.server.content.SyncStorageEngine.DayStats[28];
    private int mNextHistoryId = 0;
    private android.util.SparseArray<java.lang.Boolean> mMasterSyncAutomatically = new android.util.SparseArray<>();

    interface OnAuthorityRemovedListener {
        void onAuthorityRemoved(com.android.server.content.SyncStorageEngine.EndPoint endPoint);
    }

    interface OnSyncRequestListener {
        void onSyncRequest(com.android.server.content.SyncStorageEngine.EndPoint endPoint, int i, android.os.Bundle bundle, int i2, int i3, int i4);
    }

    interface PeriodicSyncAddedListener {
        void onPeriodicSyncAdded(com.android.server.content.SyncStorageEngine.EndPoint endPoint, android.os.Bundle bundle, long j, long j2);
    }

    public static class SyncHistoryItem {
        int authorityId;
        long downstreamActivity;
        long elapsedTime;
        int event;
        long eventTime;
        android.os.Bundle extras;
        int historyId;
        boolean initialization;
        java.lang.String mesg;
        int reason;
        int source;
        int syncExemptionFlag;
        long upstreamActivity;
    }

    static {
        sAuthorityRenames.put("contacts", "com.android.contacts");
        sAuthorityRenames.put("calendar", "com.android.calendar");
        sSyncStorageEngine = null;
    }

    static class AccountInfo {
        final android.accounts.AccountAndUser accountAndUser;
        final java.util.HashMap<java.lang.String, com.android.server.content.SyncStorageEngine.AuthorityInfo> authorities = new java.util.HashMap<>();

        AccountInfo(android.accounts.AccountAndUser accountAndUser) {
            this.accountAndUser = accountAndUser;
        }
    }

    public static class EndPoint {
        public static final com.android.server.content.SyncStorageEngine.EndPoint USER_ALL_PROVIDER_ALL_ACCOUNTS_ALL = new com.android.server.content.SyncStorageEngine.EndPoint(null, null, -1);
        final android.accounts.Account account;
        final java.lang.String provider;
        final int userId;

        public EndPoint(android.accounts.Account account, java.lang.String provider, int userId) {
            this.account = account;
            this.provider = provider;
            this.userId = userId;
        }

        public boolean matchesSpec(com.android.server.content.SyncStorageEngine.EndPoint spec) {
            boolean accountsMatch;
            boolean providersMatch;
            if (this.userId != spec.userId && this.userId != -1 && spec.userId != -1) {
                return false;
            }
            if (spec.account == null) {
                accountsMatch = true;
            } else {
                accountsMatch = this.account.equals(spec.account);
            }
            if (spec.provider == null) {
                providersMatch = true;
            } else {
                providersMatch = this.provider.equals(spec.provider);
            }
            return accountsMatch && providersMatch;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(this.account == null ? "ALL ACCS" : this.account.name).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(this.provider == null ? "ALL PDRS" : this.provider);
            sb.append(":u" + this.userId);
            return sb.toString();
        }

        public java.lang.String toSafeString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(this.account == null ? "ALL ACCS" : com.android.server.content.SyncLogger.logSafe(this.account)).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(this.provider == null ? "ALL PDRS" : this.provider);
            sb.append(":u" + this.userId);
            return sb.toString();
        }
    }

    public static class AuthorityInfo {
        public static final int NOT_INITIALIZED = -1;
        public static final int NOT_SYNCABLE = 0;
        public static final int SYNCABLE = 1;
        public static final int SYNCABLE_NOT_INITIALIZED = 2;
        public static final int SYNCABLE_NO_ACCOUNT_ACCESS = 3;
        public static final int UNDEFINED = -2;
        long backoffDelay;
        long backoffTime;
        long delayUntil;
        boolean enabled;
        final int ident;
        final java.util.ArrayList<android.content.PeriodicSync> periodicSyncs;
        int syncable;
        final com.android.server.content.SyncStorageEngine.EndPoint target;

        AuthorityInfo(com.android.server.content.SyncStorageEngine.AuthorityInfo toCopy) {
            this.target = toCopy.target;
            this.ident = toCopy.ident;
            this.enabled = toCopy.enabled;
            this.syncable = toCopy.syncable;
            this.backoffTime = toCopy.backoffTime;
            this.backoffDelay = toCopy.backoffDelay;
            this.delayUntil = toCopy.delayUntil;
            this.periodicSyncs = new java.util.ArrayList<>();
            for (android.content.PeriodicSync sync : toCopy.periodicSyncs) {
                this.periodicSyncs.add(new android.content.PeriodicSync(sync));
            }
        }

        AuthorityInfo(com.android.server.content.SyncStorageEngine.EndPoint info, int id) {
            this.target = info;
            this.ident = id;
            this.enabled = false;
            this.periodicSyncs = new java.util.ArrayList<>();
            defaultInitialisation();
        }

        private void defaultInitialisation() {
            this.syncable = -1;
            this.backoffTime = -1L;
            this.backoffDelay = -1L;
            if (com.android.server.content.SyncStorageEngine.mPeriodicSyncAddedListener != null) {
                com.android.server.content.SyncStorageEngine.mPeriodicSyncAddedListener.onPeriodicSyncAdded(this.target, new android.os.Bundle(), com.android.server.content.SyncStorageEngine.DEFAULT_POLL_FREQUENCY_SECONDS, com.android.server.content.SyncStorageEngine.calculateDefaultFlexTime(com.android.server.content.SyncStorageEngine.DEFAULT_POLL_FREQUENCY_SECONDS));
            }
        }

        public java.lang.String toString() {
            return this.target + ", enabled=" + this.enabled + ", syncable=" + this.syncable + ", backoff=" + this.backoffTime + ", delay=" + this.delayUntil;
        }

        public java.lang.String toSafeString() {
            return this.target.toSafeString() + ", enabled=" + this.enabled + ", syncable=" + this.syncable + ", backoff=" + this.backoffTime + ", delay=" + this.delayUntil;
        }
    }

    public static class DayStats {
        public final int day;
        public int failureCount;
        public long failureTime;
        public int successCount;
        public long successTime;

        public DayStats(int day) {
            this.day = day;
        }
    }

    private static class AccountAuthorityValidator {
        private final android.accounts.AccountManager mAccountManager;
        private final android.content.pm.PackageManager mPackageManager;
        private final android.util.SparseArray<android.accounts.Account[]> mAccountsCache = new android.util.SparseArray<>();
        private final android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mProvidersPerUserCache = new android.util.SparseArray<>();

        AccountAuthorityValidator(android.content.Context context) {
            this.mAccountManager = (android.accounts.AccountManager) context.getSystemService(android.accounts.AccountManager.class);
            this.mPackageManager = context.getPackageManager();
        }

        boolean isAccountValid(android.accounts.Account account, int userId) {
            android.accounts.Account[] accountsForUser = this.mAccountsCache.get(userId);
            if (accountsForUser == null) {
                accountsForUser = this.mAccountManager.getAccountsAsUser(userId);
                this.mAccountsCache.put(userId, accountsForUser);
            }
            return com.android.internal.util.ArrayUtils.contains(accountsForUser, account);
        }

        boolean isAuthorityValid(java.lang.String authority, int userId) {
            android.util.ArrayMap<java.lang.String, java.lang.Boolean> authorityMap = this.mProvidersPerUserCache.get(userId);
            if (authorityMap == null) {
                authorityMap = new android.util.ArrayMap<>();
                this.mProvidersPerUserCache.put(userId, authorityMap);
            }
            if (!authorityMap.containsKey(authority)) {
                authorityMap.put(authority, java.lang.Boolean.valueOf(this.mPackageManager.resolveContentProviderAsUser(authority, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, userId) != null));
            }
            return authorityMap.get(authority).booleanValue();
        }
    }

    private SyncStorageEngine(android.content.Context context, java.io.File dataDir, android.os.Looper looper) throws java.lang.Exception {
        this.mSyncStorageEngineExt = null;
        this.mHandler = new com.android.server.content.SyncStorageEngine.MyHandler(looper);
        this.mContext = context;
        sSyncStorageEngine = this;
        this.mSyncStorageEngineExt = (com.android.server.content.ISyncStorageEngineExt) system.ext.loader.core.ExtLoader.type(com.android.server.content.ISyncStorageEngineExt.class).base(this).create();
        this.mSyncStorageEngineExt.init(this.mContext);
        this.mLogger = com.android.server.content.SyncLogger.getInstance();
        this.mCal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+0"));
        this.mDefaultMasterSyncAutomatically = this.mContext.getResources().getBoolean(android.R.bool.config_subscription_database_async_update);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        java.io.File systemDir = new java.io.File(dataDir, "system");
        this.mSyncDir = new java.io.File(systemDir, SYNC_DIR_NAME);
        this.mSyncDir.mkdirs();
        maybeDeleteLegacyPendingInfoLocked(this.mSyncDir);
        this.mAccountInfoFile = new android.util.AtomicFile(new java.io.File(this.mSyncDir, ACCOUNT_INFO_FILE_NAME), "sync-accounts");
        this.mStatusFile = new android.util.AtomicFile(new java.io.File(this.mSyncDir, STATUS_FILE_NAME), "sync-status");
        this.mStatisticsFile = new android.util.AtomicFile(new java.io.File(this.mSyncDir, STATISTICS_FILE_NAME), "sync-stats");
        readAccountInfoLocked();
        readStatusLocked();
        readStatisticsLocked();
        if (this.mLogger.enabled()) {
            int size = this.mAuthorities.size();
            this.mLogger.log("Loaded ", java.lang.Integer.valueOf(size), " items");
            for (int i = 0; i < size; i++) {
                this.mLogger.log(this.mAuthorities.valueAt(i).toSafeString());
            }
        }
    }

    public static com.android.server.content.SyncStorageEngine newTestInstance(android.content.Context context) {
        return new com.android.server.content.SyncStorageEngine(context, context.getFilesDir(), android.os.Looper.getMainLooper());
    }

    public static void init(android.content.Context context, android.os.Looper looper) {
        if (sSyncStorageEngine != null) {
            return;
        }
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        sSyncStorageEngine = new com.android.server.content.SyncStorageEngine(context, dataDir, looper);
    }

    public static com.android.server.content.SyncStorageEngine getSingleton() {
        if (sSyncStorageEngine == null) {
            throw new java.lang.IllegalStateException("not initialized");
        }
        return sSyncStorageEngine;
    }

    protected void setOnSyncRequestListener(com.android.server.content.SyncStorageEngine.OnSyncRequestListener listener) {
        if (this.mSyncRequestListener == null) {
            this.mSyncRequestListener = listener;
        }
    }

    protected void setOnAuthorityRemovedListener(com.android.server.content.SyncStorageEngine.OnAuthorityRemovedListener listener) {
        if (this.mAuthorityRemovedListener == null) {
            this.mAuthorityRemovedListener = listener;
        }
    }

    protected void setPeriodicSyncAddedListener(com.android.server.content.SyncStorageEngine.PeriodicSyncAddedListener listener) {
        if (mPeriodicSyncAddedListener == null) {
            mPeriodicSyncAddedListener = listener;
        }
    }

    private class MyHandler extends android.os.Handler {
        public MyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                synchronized (com.android.server.content.SyncStorageEngine.this.mAuthorities) {
                    com.android.server.content.SyncStorageEngine.this.writeStatusLocked();
                }
            } else if (msg.what == 2) {
                synchronized (com.android.server.content.SyncStorageEngine.this.mAuthorities) {
                    com.android.server.content.SyncStorageEngine.this.writeStatisticsLocked();
                }
            }
        }
    }

    public int getSyncRandomOffset() {
        return this.mSyncRandomOffset;
    }

    public void addStatusChangeListener(int mask, int callingUid, android.content.ISyncStatusObserver callback) {
        synchronized (this.mAuthorities) {
            long cookie = com.android.internal.util.IntPair.of(callingUid, mask);
            this.mChangeListeners.register(callback, java.lang.Long.valueOf(cookie));
        }
    }

    public void removeStatusChangeListener(android.content.ISyncStatusObserver callback) {
        synchronized (this.mAuthorities) {
            this.mChangeListeners.unregister(callback);
        }
    }

    public static long calculateDefaultFlexTime(long syncTimeSeconds) {
        if (syncTimeSeconds < 5) {
            return 0L;
        }
        if (syncTimeSeconds < DEFAULT_POLL_FREQUENCY_SECONDS) {
            return (long) (syncTimeSeconds * DEFAULT_FLEX_PERCENT_SYNC);
        }
        return 3456L;
    }

    void reportChange(int which, com.android.server.content.SyncStorageEngine.EndPoint target) {
        java.lang.String syncAdapterPackageName;
        if (target.account == null || target.provider == null) {
            syncAdapterPackageName = null;
        } else {
            syncAdapterPackageName = android.content.ContentResolver.getSyncAdapterPackageAsUser(target.account.type, target.provider, target.userId);
        }
        reportChange(which, syncAdapterPackageName, target.userId);
    }

    void reportChange(int which, java.lang.String callingPackageName, int callingUserId) {
        java.util.ArrayList<android.content.ISyncStatusObserver> reports = null;
        synchronized (this.mAuthorities) {
            int i = this.mChangeListeners.beginBroadcast();
            while (i > 0) {
                i--;
                long cookie = ((java.lang.Long) this.mChangeListeners.getBroadcastCookie(i)).longValue();
                int registerUid = com.android.internal.util.IntPair.first(cookie);
                int registerUserId = android.os.UserHandle.getUserId(registerUid);
                int mask = com.android.internal.util.IntPair.second(cookie);
                if ((which & mask) != 0 && callingUserId == registerUserId && (callingPackageName == null || !this.mPackageManagerInternal.filterAppAccess(callingPackageName, registerUid, callingUserId))) {
                    if (reports == null) {
                        reports = new java.util.ArrayList<>(i);
                    }
                    reports.add(this.mChangeListeners.getBroadcastItem(i));
                }
            }
            this.mChangeListeners.finishBroadcast();
        }
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "reportChange " + which + " to: " + reports);
        }
        if (reports != null) {
            int i2 = reports.size();
            while (i2 > 0) {
                i2--;
                try {
                    reports.get(i2).onStatusChanged(which);
                } catch (android.os.RemoteException e) {
                }
            }
        }
    }

    public boolean getSyncAutomatically(android.accounts.Account account, int userId, java.lang.String providerName) {
        synchronized (this.mAuthorities) {
            boolean z = true;
            if (account != null) {
                com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getAuthorityLocked(new com.android.server.content.SyncStorageEngine.EndPoint(account, providerName, userId), "getSyncAutomatically");
                if (authority == null || !authority.enabled) {
                    z = false;
                }
                return z;
            }
            int i = this.mAuthorities.size();
            while (i > 0) {
                i--;
                com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo = this.mAuthorities.valueAt(i);
                if (authorityInfo.target.matchesSpec(new com.android.server.content.SyncStorageEngine.EndPoint(account, providerName, userId)) && authorityInfo.enabled) {
                    return true;
                }
            }
            return false;
        }
    }

    public void setSyncAutomatically(android.accounts.Account account, int userId, java.lang.String providerName, boolean sync, int syncExemptionFlag, int callingUid, int callingPid) throws java.lang.Throwable {
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.d("SyncManager", "setSyncAutomatically:  provider " + providerName + ", user " + userId + " -> " + sync);
        }
        this.mLogger.log("Set sync auto account=", account.toSafeString(), " user=", java.lang.Integer.valueOf(userId), " authority=", providerName, " value=", java.lang.Boolean.toString(sync), " cuid=", java.lang.Integer.valueOf(callingUid), " cpid=", java.lang.Integer.valueOf(callingPid));
        synchronized (this.mAuthorities) {
            try {
                try {
                    com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getOrCreateAuthorityLocked(new com.android.server.content.SyncStorageEngine.EndPoint(account, providerName, userId), -1, false);
                    if (authority.enabled == sync) {
                        if (android.util.Log.isLoggable("SyncManager", 2)) {
                            android.util.Slog.d("SyncManager", "setSyncAutomatically: already set to " + sync + ", doing nothing");
                        }
                        return;
                    }
                    if (sync && authority.syncable == 2) {
                        authority.syncable = -1;
                    }
                    authority.enabled = sync;
                    writeAccountInfoLocked();
                    if (sync) {
                        requestSync(account, userId, -6, providerName, new android.os.Bundle(), syncExemptionFlag, callingUid, callingPid);
                    }
                    reportChange(1, authority.target);
                    queueBackup();
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public int getIsSyncable(android.accounts.Account account, int userId, java.lang.String providerName) {
        synchronized (this.mAuthorities) {
            if (account != null) {
                com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getAuthorityLocked(new com.android.server.content.SyncStorageEngine.EndPoint(account, providerName, userId), "get authority syncable");
                if (authority == null) {
                    return -1;
                }
                return authority.syncable;
            }
            int i = this.mAuthorities.size();
            while (i > 0) {
                i--;
                com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo = this.mAuthorities.valueAt(i);
                if (authorityInfo.target != null && authorityInfo.target.provider.equals(providerName)) {
                    return authorityInfo.syncable;
                }
            }
            return -1;
        }
    }

    public void setIsSyncable(android.accounts.Account account, int userId, java.lang.String providerName, int syncable, int callingUid, int callingPid) {
        setSyncableStateForEndPoint(new com.android.server.content.SyncStorageEngine.EndPoint(account, providerName, userId), syncable, callingUid, callingPid);
    }

    private void setSyncableStateForEndPoint(com.android.server.content.SyncStorageEngine.EndPoint target, int syncable, int callingUid, int callingPid) {
        this.mLogger.log("Set syncable ", target.toSafeString(), " value=", java.lang.Integer.toString(syncable), " cuid=", java.lang.Integer.valueOf(callingUid), " cpid=", java.lang.Integer.valueOf(callingPid));
        synchronized (this.mAuthorities) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo aInfo = getOrCreateAuthorityLocked(target, -1, false);
            if (syncable < -1) {
                syncable = -1;
            }
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.d("SyncManager", "setIsSyncable: " + aInfo.toString() + " -> " + syncable);
            }
            if (aInfo.syncable == syncable) {
                if (android.util.Log.isLoggable("SyncManager", 2)) {
                    android.util.Slog.d("SyncManager", "setIsSyncable: already set to " + syncable + ", doing nothing");
                }
                return;
            }
            aInfo.syncable = syncable;
            writeAccountInfoLocked();
            if (syncable == 1) {
                requestSync(aInfo, -5, new android.os.Bundle(), 0, callingUid, callingPid);
            }
            reportChange(1, target);
        }
    }

    void setJobNamespaceMigrated(boolean migrated) {
        if (this.mIsJobNamespaceMigrated == migrated) {
            return;
        }
        this.mIsJobNamespaceMigrated = migrated;
        this.mHandler.sendEmptyMessageDelayed(1, 600000L);
    }

    boolean isJobNamespaceMigrated() {
        return this.mIsJobNamespaceMigrated;
    }

    void setJobAttributionFixed(boolean fixed) {
        if (this.mIsJobAttributionFixed == fixed) {
            return;
        }
        this.mIsJobAttributionFixed = fixed;
        this.mHandler.sendEmptyMessageDelayed(1, 600000L);
    }

    boolean isJobAttributionFixed() {
        return this.mIsJobAttributionFixed;
    }

    public android.util.Pair<java.lang.Long, java.lang.Long> getBackoff(com.android.server.content.SyncStorageEngine.EndPoint info) {
        synchronized (this.mAuthorities) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getAuthorityLocked(info, "getBackoff");
            if (authority == null) {
                return null;
            }
            return android.util.Pair.create(java.lang.Long.valueOf(authority.backoffTime), java.lang.Long.valueOf(authority.backoffDelay));
        }
    }

    public void setBackoff(com.android.server.content.SyncStorageEngine.EndPoint info, long nextSyncTime, long nextDelay) {
        int i;
        boolean changed;
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "setBackoff: " + info + " -> nextSyncTime " + nextSyncTime + ", nextDelay " + nextDelay);
        }
        synchronized (this.mAuthorities) {
            if (info.account != null && info.provider != null) {
                com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo = getOrCreateAuthorityLocked(info, -1, true);
                if (authorityInfo.backoffTime == nextSyncTime && authorityInfo.backoffDelay == nextDelay) {
                    changed = false;
                    i = 1;
                } else {
                    authorityInfo.backoffTime = nextSyncTime;
                    authorityInfo.backoffDelay = nextDelay;
                    changed = true;
                    i = 1;
                }
            } else {
                i = 1;
                changed = setBackoffLocked(info.account, info.userId, info.provider, nextSyncTime, nextDelay);
            }
        }
        if (changed) {
            reportChange(i, info);
        }
    }

    private boolean setBackoffLocked(android.accounts.Account account, int userId, java.lang.String providerName, long nextSyncTime, long nextDelay) {
        boolean changed = false;
        for (com.android.server.content.SyncStorageEngine.AccountInfo accountInfo : this.mAccounts.values()) {
            if (account == null || account.equals(accountInfo.accountAndUser.account) || userId == accountInfo.accountAndUser.userId) {
                for (com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo : accountInfo.authorities.values()) {
                    if (providerName == null || providerName.equals(authorityInfo.target.provider)) {
                        if (authorityInfo.backoffTime != nextSyncTime || authorityInfo.backoffDelay != nextDelay) {
                            authorityInfo.backoffTime = nextSyncTime;
                            authorityInfo.backoffDelay = nextDelay;
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }

    public void clearAllBackoffsLocked() {
        android.util.ArraySet<java.lang.Integer> changedUserIds = new android.util.ArraySet<>();
        synchronized (this.mAuthorities) {
            for (com.android.server.content.SyncStorageEngine.AccountInfo accountInfo : this.mAccounts.values()) {
                for (com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo : accountInfo.authorities.values()) {
                    if (authorityInfo.backoffTime != -1 || authorityInfo.backoffDelay != -1) {
                        if (android.util.Log.isLoggable("SyncManager", 2)) {
                            android.util.Slog.v("SyncManager", "clearAllBackoffsLocked: authority:" + authorityInfo.target + " account:" + accountInfo.accountAndUser.account.name + " user:" + accountInfo.accountAndUser.userId + " backoffTime was: " + authorityInfo.backoffTime + " backoffDelay was: " + authorityInfo.backoffDelay);
                        }
                        authorityInfo.backoffTime = -1L;
                        authorityInfo.backoffDelay = -1L;
                        changedUserIds.add(java.lang.Integer.valueOf(accountInfo.accountAndUser.userId));
                    }
                }
            }
        }
        for (int i = changedUserIds.size() - 1; i > 0; i--) {
            reportChange(1, null, changedUserIds.valueAt(i).intValue());
        }
    }

    public long getDelayUntilTime(com.android.server.content.SyncStorageEngine.EndPoint info) {
        synchronized (this.mAuthorities) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getAuthorityLocked(info, "getDelayUntil");
            if (authority == null) {
                return 0L;
            }
            return authority.delayUntil;
        }
    }

    public void setDelayUntilTime(com.android.server.content.SyncStorageEngine.EndPoint info, long delayUntil) {
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "setDelayUntil: " + info + " -> delayUntil " + delayUntil);
        }
        synchronized (this.mAuthorities) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getOrCreateAuthorityLocked(info, -1, true);
            if (authority.delayUntil == delayUntil) {
                return;
            }
            authority.delayUntil = delayUntil;
            reportChange(1, info);
        }
    }

    boolean restoreAllPeriodicSyncs() {
        if (mPeriodicSyncAddedListener == null) {
            return false;
        }
        synchronized (this.mAuthorities) {
            for (int i = 0; i < this.mAuthorities.size(); i++) {
                com.android.server.content.SyncStorageEngine.AuthorityInfo authority = this.mAuthorities.valueAt(i);
                for (android.content.PeriodicSync periodicSync : authority.periodicSyncs) {
                    mPeriodicSyncAddedListener.onPeriodicSyncAdded(authority.target, periodicSync.extras, periodicSync.period, periodicSync.flexTime);
                }
                authority.periodicSyncs.clear();
            }
            writeAccountInfoLocked();
        }
        return true;
    }

    public void setMasterSyncAutomatically(boolean flag, int userId, int syncExemptionFlag, int callingUid, int callingPid) {
        this.mLogger.log("Set master enabled=", java.lang.Boolean.valueOf(flag), " user=", java.lang.Integer.valueOf(userId), " cuid=", java.lang.Integer.valueOf(callingUid), " cpid=", java.lang.Integer.valueOf(callingPid));
        if (this.mSyncStorageEngineExt.isDataSyncDisabled()) {
            return;
        }
        synchronized (this.mAuthorities) {
            java.lang.Boolean auto = this.mMasterSyncAutomatically.get(userId);
            if (auto == null || !auto.equals(java.lang.Boolean.valueOf(flag))) {
                this.mMasterSyncAutomatically.put(userId, java.lang.Boolean.valueOf(flag));
                writeAccountInfoLocked();
                if (flag) {
                    requestSync(null, userId, -7, null, new android.os.Bundle(), syncExemptionFlag, callingUid, callingPid);
                }
                reportChange(1, null, userId);
                this.mContext.sendBroadcast(android.content.ContentResolver.ACTION_SYNC_CONN_STATUS_CHANGED);
                queueBackup();
            }
        }
    }

    public boolean getMasterSyncAutomatically(int userId) {
        synchronized (this.mAuthorities) {
            if (this.mSyncStorageEngineExt.isDataSyncDisabled()) {
                return false;
            }
            java.lang.Boolean auto = this.mMasterSyncAutomatically.get(userId);
            return auto == null ? this.mDefaultMasterSyncAutomatically : auto.booleanValue();
        }
    }

    public int getAuthorityCount() {
        int size;
        synchronized (this.mAuthorities) {
            size = this.mAuthorities.size();
        }
        return size;
    }

    public com.android.server.content.SyncStorageEngine.AuthorityInfo getAuthority(int authorityId) {
        com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo;
        synchronized (this.mAuthorities) {
            authorityInfo = this.mAuthorities.get(authorityId);
        }
        return authorityInfo;
    }

    public boolean isSyncActive(com.android.server.content.SyncStorageEngine.EndPoint info) {
        synchronized (this.mAuthorities) {
            for (android.content.SyncInfo syncInfo : getCurrentSyncs(info.userId)) {
                com.android.server.content.SyncStorageEngine.AuthorityInfo ainfo = getAuthority(syncInfo.authorityId);
                if (ainfo != null && ainfo.target.matchesSpec(info)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void markPending(com.android.server.content.SyncStorageEngine.EndPoint info, boolean pendingValue) {
        synchronized (this.mAuthorities) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getOrCreateAuthorityLocked(info, -1, true);
            if (authority == null) {
                return;
            }
            android.content.SyncStatusInfo status = getOrCreateSyncStatusLocked(authority.ident);
            status.pending = pendingValue;
            reportChange(2, info);
        }
    }

    public void removeStaleAccounts(android.accounts.Account[] currentAccounts, int userId) {
        synchronized (this.mAuthorities) {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "Updating for new accounts...");
            }
            android.util.SparseArray<com.android.server.content.SyncStorageEngine.AuthorityInfo> removing = new android.util.SparseArray<>();
            java.util.Iterator<com.android.server.content.SyncStorageEngine.AccountInfo> accIt = this.mAccounts.values().iterator();
            while (accIt.hasNext()) {
                com.android.server.content.SyncStorageEngine.AccountInfo acc = accIt.next();
                if (acc.accountAndUser.userId == userId) {
                    if (currentAccounts == null || !com.android.internal.util.ArrayUtils.contains(currentAccounts, acc.accountAndUser.account)) {
                        if (android.util.Log.isLoggable("SyncManager", 2)) {
                            android.util.Slog.v("SyncManager", "Account removed: " + acc.accountAndUser);
                        }
                        for (com.android.server.content.SyncStorageEngine.AuthorityInfo auth : acc.authorities.values()) {
                            removing.put(auth.ident, auth);
                        }
                        accIt.remove();
                    }
                }
            }
            int i = removing.size();
            if (i > 0) {
                while (i > 0) {
                    i--;
                    int ident = removing.keyAt(i);
                    com.android.server.content.SyncStorageEngine.AuthorityInfo auth2 = removing.valueAt(i);
                    if (this.mAuthorityRemovedListener != null) {
                        this.mAuthorityRemovedListener.onAuthorityRemoved(auth2.target);
                    }
                    this.mAuthorities.remove(ident);
                    int j = this.mSyncStatus.size();
                    while (j > 0) {
                        j--;
                        if (this.mSyncStatus.keyAt(j) == ident) {
                            this.mSyncStatus.remove(this.mSyncStatus.keyAt(j));
                        }
                    }
                    int j2 = this.mSyncHistory.size();
                    while (j2 > 0) {
                        j2--;
                        if (this.mSyncHistory.get(j2).authorityId == ident) {
                            this.mSyncHistory.remove(j2);
                        }
                    }
                }
                writeAccountInfoLocked();
                writeStatusLocked();
                writeStatisticsLocked();
            }
        }
    }

    public android.content.SyncInfo addActiveSync(com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext) {
        android.content.SyncInfo syncInfo;
        synchronized (this.mAuthorities) {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "setActiveSync: account= auth=" + activeSyncContext.mSyncOperation.target + " src=" + activeSyncContext.mSyncOperation.syncSource + " extras=" + activeSyncContext.mSyncOperation.getExtrasAsString());
            }
            com.android.server.content.SyncStorageEngine.EndPoint info = activeSyncContext.mSyncOperation.target;
            com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo = getOrCreateAuthorityLocked(info, -1, true);
            syncInfo = new android.content.SyncInfo(authorityInfo.ident, authorityInfo.target.account, authorityInfo.target.provider, activeSyncContext.mStartTime);
            getCurrentSyncs(authorityInfo.target.userId).add(syncInfo);
        }
        reportActiveChange(activeSyncContext.mSyncOperation.target);
        return syncInfo;
    }

    public void removeActiveSync(android.content.SyncInfo syncInfo, int userId) {
        synchronized (this.mAuthorities) {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "removeActiveSync: account=" + syncInfo.account + " user=" + userId + " auth=" + syncInfo.authority);
            }
            getCurrentSyncs(userId).remove(syncInfo);
        }
        reportActiveChange(new com.android.server.content.SyncStorageEngine.EndPoint(syncInfo.account, syncInfo.authority, userId));
    }

    public void reportActiveChange(com.android.server.content.SyncStorageEngine.EndPoint target) {
        reportChange(4, target);
    }

    public long insertStartSyncEvent(com.android.server.content.SyncOperation op, long now) {
        synchronized (this.mAuthorities) {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "insertStartSyncEvent: " + op);
            }
            com.android.server.content.SyncStorageEngine.AuthorityInfo authority = getAuthorityLocked(op.target, "insertStartSyncEvent");
            if (authority == null) {
                return -1L;
            }
            com.android.server.content.SyncStorageEngine.SyncHistoryItem item = new com.android.server.content.SyncStorageEngine.SyncHistoryItem();
            item.initialization = op.isInitialization();
            item.authorityId = authority.ident;
            int i = this.mNextHistoryId;
            this.mNextHistoryId = i + 1;
            item.historyId = i;
            if (this.mNextHistoryId < 0) {
                this.mNextHistoryId = 0;
            }
            item.eventTime = now;
            item.source = op.syncSource;
            item.reason = op.reason;
            item.extras = op.getClonedExtras();
            item.event = 0;
            item.syncExemptionFlag = op.syncExemptionFlag;
            this.mSyncHistory.add(0, item);
            while (this.mSyncHistory.size() > 100) {
                this.mSyncHistory.remove(this.mSyncHistory.size() - 1);
            }
            long id = item.historyId;
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "returning historyId " + id);
            }
            reportChange(8, op.owningPackage, op.target.userId);
            return id;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x016e A[Catch: all -> 0x02a9, TryCatch #1 {all -> 0x02a9, blocks: (B:19:0x0075, B:20:0x00a8, B:28:0x010c, B:30:0x0118, B:36:0x015b, B:38:0x016e, B:40:0x0176, B:45:0x0181, B:54:0x01eb, B:56:0x0232, B:57:0x0239, B:58:0x023c, B:61:0x024c, B:59:0x023f, B:60:0x0246, B:62:0x024f, B:64:0x0262, B:70:0x0283, B:74:0x029e, B:71:0x0287, B:73:0x0290, B:65:0x0267, B:67:0x0270, B:46:0x019c, B:48:0x01a6, B:52:0x01b0, B:53:0x01d7, B:31:0x0129, B:33:0x0132, B:34:0x0150, B:22:0x00ac, B:23:0x00bc, B:24:0x00cc, B:25:0x00dc, B:26:0x00ec, B:27:0x00fc), top: B:88:0x0075 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x019c A[Catch: all -> 0x02a9, TryCatch #1 {all -> 0x02a9, blocks: (B:19:0x0075, B:20:0x00a8, B:28:0x010c, B:30:0x0118, B:36:0x015b, B:38:0x016e, B:40:0x0176, B:45:0x0181, B:54:0x01eb, B:56:0x0232, B:57:0x0239, B:58:0x023c, B:61:0x024c, B:59:0x023f, B:60:0x0246, B:62:0x024f, B:64:0x0262, B:70:0x0283, B:74:0x029e, B:71:0x0287, B:73:0x0290, B:65:0x0267, B:67:0x0270, B:46:0x019c, B:48:0x01a6, B:52:0x01b0, B:53:0x01d7, B:31:0x0129, B:33:0x0132, B:34:0x0150, B:22:0x00ac, B:23:0x00bc, B:24:0x00cc, B:25:0x00dc, B:26:0x00ec, B:27:0x00fc), top: B:88:0x0075 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0232 A[Catch: all -> 0x02a9, TryCatch #1 {all -> 0x02a9, blocks: (B:19:0x0075, B:20:0x00a8, B:28:0x010c, B:30:0x0118, B:36:0x015b, B:38:0x016e, B:40:0x0176, B:45:0x0181, B:54:0x01eb, B:56:0x0232, B:57:0x0239, B:58:0x023c, B:61:0x024c, B:59:0x023f, B:60:0x0246, B:62:0x024f, B:64:0x0262, B:70:0x0283, B:74:0x029e, B:71:0x0287, B:73:0x0290, B:65:0x0267, B:67:0x0270, B:46:0x019c, B:48:0x01a6, B:52:0x01b0, B:53:0x01d7, B:31:0x0129, B:33:0x0132, B:34:0x0150, B:22:0x00ac, B:23:0x00bc, B:24:0x00cc, B:25:0x00dc, B:26:0x00ec, B:27:0x00fc), top: B:88:0x0075 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0262 A[Catch: all -> 0x02a9, TryCatch #1 {all -> 0x02a9, blocks: (B:19:0x0075, B:20:0x00a8, B:28:0x010c, B:30:0x0118, B:36:0x015b, B:38:0x016e, B:40:0x0176, B:45:0x0181, B:54:0x01eb, B:56:0x0232, B:57:0x0239, B:58:0x023c, B:61:0x024c, B:59:0x023f, B:60:0x0246, B:62:0x024f, B:64:0x0262, B:70:0x0283, B:74:0x029e, B:71:0x0287, B:73:0x0290, B:65:0x0267, B:67:0x0270, B:46:0x019c, B:48:0x01a6, B:52:0x01b0, B:53:0x01d7, B:31:0x0129, B:33:0x0132, B:34:0x0150, B:22:0x00ac, B:23:0x00bc, B:24:0x00cc, B:25:0x00dc, B:26:0x00ec, B:27:0x00fc), top: B:88:0x0075 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0267 A[Catch: all -> 0x02a9, TryCatch #1 {all -> 0x02a9, blocks: (B:19:0x0075, B:20:0x00a8, B:28:0x010c, B:30:0x0118, B:36:0x015b, B:38:0x016e, B:40:0x0176, B:45:0x0181, B:54:0x01eb, B:56:0x0232, B:57:0x0239, B:58:0x023c, B:61:0x024c, B:59:0x023f, B:60:0x0246, B:62:0x024f, B:64:0x0262, B:70:0x0283, B:74:0x029e, B:71:0x0287, B:73:0x0290, B:65:0x0267, B:67:0x0270, B:46:0x019c, B:48:0x01a6, B:52:0x01b0, B:53:0x01d7, B:31:0x0129, B:33:0x0132, B:34:0x0150, B:22:0x00ac, B:23:0x00bc, B:24:0x00cc, B:25:0x00dc, B:26:0x00ec, B:27:0x00fc), top: B:88:0x0075 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0283 A[Catch: all -> 0x02a9, TryCatch #1 {all -> 0x02a9, blocks: (B:19:0x0075, B:20:0x00a8, B:28:0x010c, B:30:0x0118, B:36:0x015b, B:38:0x016e, B:40:0x0176, B:45:0x0181, B:54:0x01eb, B:56:0x0232, B:57:0x0239, B:58:0x023c, B:61:0x024c, B:59:0x023f, B:60:0x0246, B:62:0x024f, B:64:0x0262, B:70:0x0283, B:74:0x029e, B:71:0x0287, B:73:0x0290, B:65:0x0267, B:67:0x0270, B:46:0x019c, B:48:0x01a6, B:52:0x01b0, B:53:0x01d7, B:31:0x0129, B:33:0x0132, B:34:0x0150, B:22:0x00ac, B:23:0x00bc, B:24:0x00cc, B:25:0x00dc, B:26:0x00ec, B:27:0x00fc), top: B:88:0x0075 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0287 A[Catch: all -> 0x02a9, TryCatch #1 {all -> 0x02a9, blocks: (B:19:0x0075, B:20:0x00a8, B:28:0x010c, B:30:0x0118, B:36:0x015b, B:38:0x016e, B:40:0x0176, B:45:0x0181, B:54:0x01eb, B:56:0x0232, B:57:0x0239, B:58:0x023c, B:61:0x024c, B:59:0x023f, B:60:0x0246, B:62:0x024f, B:64:0x0262, B:70:0x0283, B:74:0x029e, B:71:0x0287, B:73:0x0290, B:65:0x0267, B:67:0x0270, B:46:0x019c, B:48:0x01a6, B:52:0x01b0, B:53:0x01d7, B:31:0x0129, B:33:0x0132, B:34:0x0150, B:22:0x00ac, B:23:0x00bc, B:24:0x00cc, B:25:0x00dc, B:26:0x00ec, B:27:0x00fc), top: B:88:0x0075 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void stopSyncEvent(long r24, long r26, java.lang.String r28, long r29, long r31, java.lang.String r33, int r34) {
        /*
            Method dump skipped, instruction units count: 718
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.content.SyncStorageEngine.stopSyncEvent(long, long, java.lang.String, long, long, java.lang.String, int):void");
    }

    private java.util.List<android.content.SyncInfo> getCurrentSyncs(int userId) {
        java.util.List<android.content.SyncInfo> currentSyncsLocked;
        synchronized (this.mAuthorities) {
            currentSyncsLocked = getCurrentSyncsLocked(userId);
        }
        return currentSyncsLocked;
    }

    public java.util.List<android.content.SyncInfo> getCurrentSyncsCopy(int userId, boolean canAccessAccounts) {
        java.util.List<android.content.SyncInfo> syncsCopy;
        android.content.SyncInfo copy;
        synchronized (this.mAuthorities) {
            java.util.List<android.content.SyncInfo> syncs = getCurrentSyncsLocked(userId);
            syncsCopy = new java.util.ArrayList<>();
            for (android.content.SyncInfo sync : syncs) {
                if (!canAccessAccounts) {
                    copy = android.content.SyncInfo.createAccountRedacted(sync.authorityId, sync.authority, sync.startTime);
                } else {
                    copy = new android.content.SyncInfo(sync);
                }
                syncsCopy.add(copy);
            }
        }
        return syncsCopy;
    }

    private java.util.List<android.content.SyncInfo> getCurrentSyncsLocked(int userId) {
        java.util.ArrayList<android.content.SyncInfo> syncs = this.mCurrentSyncs.get(userId);
        if (syncs == null) {
            java.util.ArrayList<android.content.SyncInfo> syncs2 = new java.util.ArrayList<>();
            this.mCurrentSyncs.put(userId, syncs2);
            return syncs2;
        }
        return syncs;
    }

    public android.util.Pair<com.android.server.content.SyncStorageEngine.AuthorityInfo, android.content.SyncStatusInfo> getCopyOfAuthorityWithSyncStatus(com.android.server.content.SyncStorageEngine.EndPoint info) {
        android.util.Pair<com.android.server.content.SyncStorageEngine.AuthorityInfo, android.content.SyncStatusInfo> pairCreateCopyPairOfAuthorityWithSyncStatusLocked;
        synchronized (this.mAuthorities) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo = getOrCreateAuthorityLocked(info, -1, true);
            pairCreateCopyPairOfAuthorityWithSyncStatusLocked = createCopyPairOfAuthorityWithSyncStatusLocked(authorityInfo);
        }
        return pairCreateCopyPairOfAuthorityWithSyncStatusLocked;
    }

    public android.content.SyncStatusInfo getStatusByAuthority(com.android.server.content.SyncStorageEngine.EndPoint info) {
        if (info.account == null || info.provider == null) {
            return null;
        }
        synchronized (this.mAuthorities) {
            int N = this.mSyncStatus.size();
            for (int i = 0; i < N; i++) {
                android.content.SyncStatusInfo cur = this.mSyncStatus.valueAt(i);
                com.android.server.content.SyncStorageEngine.AuthorityInfo ainfo = this.mAuthorities.get(cur.authorityId);
                if (ainfo != null && ainfo.target.matchesSpec(info)) {
                    return cur;
                }
            }
            return null;
        }
    }

    public boolean isSyncPending(com.android.server.content.SyncStorageEngine.EndPoint info) {
        synchronized (this.mAuthorities) {
            int N = this.mSyncStatus.size();
            for (int i = 0; i < N; i++) {
                android.content.SyncStatusInfo cur = this.mSyncStatus.valueAt(i);
                com.android.server.content.SyncStorageEngine.AuthorityInfo ainfo = this.mAuthorities.get(cur.authorityId);
                if (ainfo != null && ainfo.target.matchesSpec(info) && cur.pending) {
                    return true;
                }
            }
            return false;
        }
    }

    public java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> getSyncHistory() {
        java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> items;
        synchronized (this.mAuthorities) {
            int N = this.mSyncHistory.size();
            items = new java.util.ArrayList<>(N);
            for (int i = 0; i < N; i++) {
                items.add(this.mSyncHistory.get(i));
            }
        }
        return items;
    }

    public com.android.server.content.SyncStorageEngine.DayStats[] getDayStatistics() {
        com.android.server.content.SyncStorageEngine.DayStats[] ds;
        synchronized (this.mAuthorities) {
            ds = new com.android.server.content.SyncStorageEngine.DayStats[this.mDayStats.length];
            java.lang.System.arraycopy(this.mDayStats, 0, ds, 0, ds.length);
        }
        return ds;
    }

    private android.util.Pair<com.android.server.content.SyncStorageEngine.AuthorityInfo, android.content.SyncStatusInfo> createCopyPairOfAuthorityWithSyncStatusLocked(com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo) {
        android.content.SyncStatusInfo syncStatusInfo = getOrCreateSyncStatusLocked(authorityInfo.ident);
        return android.util.Pair.create(new com.android.server.content.SyncStorageEngine.AuthorityInfo(authorityInfo), new android.content.SyncStatusInfo(syncStatusInfo));
    }

    private int getCurrentDayLocked() {
        this.mCal.setTimeInMillis(java.lang.System.currentTimeMillis());
        int dayOfYear = this.mCal.get(6);
        if (this.mYear != this.mCal.get(1)) {
            this.mYear = this.mCal.get(1);
            this.mCal.clear();
            this.mCal.set(1, this.mYear);
            this.mYearInDays = (int) (this.mCal.getTimeInMillis() / 86400000);
        }
        return this.mYearInDays + dayOfYear;
    }

    private com.android.server.content.SyncStorageEngine.AuthorityInfo getAuthorityLocked(com.android.server.content.SyncStorageEngine.EndPoint info, java.lang.String tag) {
        android.accounts.AccountAndUser au = new android.accounts.AccountAndUser(info.account, info.userId);
        com.android.server.content.SyncStorageEngine.AccountInfo accountInfo = this.mAccounts.get(au);
        if (accountInfo == null) {
            if (tag != null && android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", tag + ": unknown account " + au);
            }
            return null;
        }
        com.android.server.content.SyncStorageEngine.AuthorityInfo authority = accountInfo.authorities.get(info.provider);
        if (authority == null) {
            if (tag != null && android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", tag + ": unknown provider " + info.provider);
            }
            return null;
        }
        return authority;
    }

    private com.android.server.content.SyncStorageEngine.AuthorityInfo getOrCreateAuthorityLocked(com.android.server.content.SyncStorageEngine.EndPoint info, int ident, boolean doWrite) {
        android.accounts.AccountAndUser au = new android.accounts.AccountAndUser(info.account, info.userId);
        com.android.server.content.SyncStorageEngine.AccountInfo account = this.mAccounts.get(au);
        if (account == null) {
            account = new com.android.server.content.SyncStorageEngine.AccountInfo(au);
            this.mAccounts.put(au, account);
        }
        com.android.server.content.SyncStorageEngine.AuthorityInfo authority = account.authorities.get(info.provider);
        if (authority == null) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo authority2 = createAuthorityLocked(info, ident, doWrite);
            account.authorities.put(info.provider, authority2);
            return authority2;
        }
        return authority;
    }

    private com.android.server.content.SyncStorageEngine.AuthorityInfo createAuthorityLocked(com.android.server.content.SyncStorageEngine.EndPoint info, int ident, boolean doWrite) {
        if (ident < 0) {
            ident = this.mNextAuthorityId;
            this.mNextAuthorityId++;
            doWrite = true;
        }
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "created a new AuthorityInfo for " + info);
        }
        com.android.server.content.SyncStorageEngine.AuthorityInfo authority = new com.android.server.content.SyncStorageEngine.AuthorityInfo(info, ident);
        this.mAuthorities.put(ident, authority);
        if (doWrite) {
            writeAccountInfoLocked();
        }
        return authority;
    }

    public void removeAuthority(com.android.server.content.SyncStorageEngine.EndPoint info) {
        synchronized (this.mAuthorities) {
            removeAuthorityLocked(info.account, info.userId, info.provider, true);
        }
    }

    private void removeAuthorityLocked(android.accounts.Account account, int userId, java.lang.String authorityName, boolean doWrite) {
        com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo;
        com.android.server.content.SyncStorageEngine.AccountInfo accountInfo = this.mAccounts.get(new android.accounts.AccountAndUser(account, userId));
        if (accountInfo != null && (authorityInfo = accountInfo.authorities.remove(authorityName)) != null) {
            if (this.mAuthorityRemovedListener != null) {
                this.mAuthorityRemovedListener.onAuthorityRemoved(authorityInfo.target);
            }
            this.mAuthorities.remove(authorityInfo.ident);
            if (doWrite) {
                writeAccountInfoLocked();
            }
        }
    }

    private android.content.SyncStatusInfo getOrCreateSyncStatusLocked(int authorityId) {
        android.content.SyncStatusInfo status = this.mSyncStatus.get(authorityId);
        if (status == null) {
            android.content.SyncStatusInfo status2 = new android.content.SyncStatusInfo(authorityId);
            this.mSyncStatus.put(authorityId, status2);
            return status2;
        }
        return status;
    }

    public void writeAllState() {
        synchronized (this.mAuthorities) {
            writeStatusLocked();
            writeStatisticsLocked();
        }
    }

    public boolean shouldGrantSyncAdaptersAccountAccess() {
        return this.mGrantSyncAdaptersAccountAccess;
    }

    public void clearAndReadState() {
        synchronized (this.mAuthorities) {
            this.mAuthorities.clear();
            this.mAccounts.clear();
            this.mServices.clear();
            this.mSyncStatus.clear();
            this.mSyncHistory.clear();
            readAccountInfoLocked();
            readStatusLocked();
            readStatisticsLocked();
            writeAccountInfoLocked();
            writeStatusLocked();
            writeStatisticsLocked();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x01d6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x01e8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01bf A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:138:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:140:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:142:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01ac A[Catch: all -> 0x01a5, TRY_ENTER, TryCatch #2 {all -> 0x01a5, blocks: (B:3:0x000c, B:5:0x001a, B:6:0x0036, B:10:0x0045, B:12:0x004d, B:19:0x0062, B:21:0x006e, B:23:0x0081, B:24:0x0083, B:26:0x009f, B:28:0x00b5, B:38:0x00ee, B:40:0x00f2, B:95:0x01ac, B:96:0x01b0, B:105:0x01c7), top: B:120:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01b0 A[Catch: all -> 0x01a5, TRY_LEAVE, TryCatch #2 {all -> 0x01a5, blocks: (B:3:0x000c, B:5:0x001a, B:6:0x0036, B:10:0x0045, B:12:0x004d, B:19:0x0062, B:21:0x006e, B:23:0x0081, B:24:0x0083, B:26:0x009f, B:28:0x00b5, B:38:0x00ee, B:40:0x00f2, B:95:0x01ac, B:96:0x01b0, B:105:0x01c7), top: B:120:0x000c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readAccountInfoLocked() throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.content.SyncStorageEngine.readAccountInfoLocked():void");
    }

    private void maybeDeleteLegacyPendingInfoLocked(java.io.File syncDir) {
        java.io.File file = new java.io.File(syncDir, "pending.bin");
        if (!file.exists()) {
            return;
        }
        file.delete();
    }

    private boolean maybeMigrateSettingsForRenamedAuthorities() {
        boolean writeNeeded = false;
        java.util.ArrayList<com.android.server.content.SyncStorageEngine.AuthorityInfo> authoritiesToRemove = new java.util.ArrayList<>();
        int N = this.mAuthorities.size();
        for (int i = 0; i < N; i++) {
            com.android.server.content.SyncStorageEngine.AuthorityInfo authority = this.mAuthorities.valueAt(i);
            java.lang.String newAuthorityName = sAuthorityRenames.get(authority.target.provider);
            if (newAuthorityName != null) {
                authoritiesToRemove.add(authority);
                if (authority.enabled) {
                    com.android.server.content.SyncStorageEngine.EndPoint newInfo = new com.android.server.content.SyncStorageEngine.EndPoint(authority.target.account, newAuthorityName, authority.target.userId);
                    if (getAuthorityLocked(newInfo, "cleanup") == null) {
                        com.android.server.content.SyncStorageEngine.AuthorityInfo newAuthority = getOrCreateAuthorityLocked(newInfo, -1, false);
                        newAuthority.enabled = true;
                        writeNeeded = true;
                    }
                }
            }
        }
        for (com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo : authoritiesToRemove) {
            removeAuthorityLocked(authorityInfo.target.account, authorityInfo.target.userId, authorityInfo.target.provider, false);
            writeNeeded = true;
        }
        return writeNeeded;
    }

    private void parseListenForTickles(com.android.modules.utils.TypedXmlPullParser parser) {
        int userId = 0;
        try {
            userId = parser.getAttributeInt((java.lang.String) null, XML_ATTR_USER);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e("SyncManager", "error parsing the user for listen-for-tickles", e);
        }
        boolean listen = parser.getAttributeBoolean((java.lang.String) null, "enabled", true);
        this.mMasterSyncAutomatically.put(userId, java.lang.Boolean.valueOf(listen));
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0184  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.content.SyncStorageEngine.AuthorityInfo parseAuthority(com.android.modules.utils.TypedXmlPullParser r19, int r20, com.android.server.content.SyncStorageEngine.AccountAuthorityValidator r21) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 431
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.content.SyncStorageEngine.parseAuthority(com.android.modules.utils.TypedXmlPullParser, int, com.android.server.content.SyncStorageEngine$AccountAuthorityValidator):com.android.server.content.SyncStorageEngine$AuthorityInfo");
    }

    private android.content.PeriodicSync parsePeriodicSync(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo) {
        long flextime;
        android.os.Bundle extras = new android.os.Bundle();
        try {
            long period = parser.getAttributeLong((java.lang.String) null, "period");
            try {
                flextime = parser.getAttributeLong((java.lang.String) null, "flex");
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                long flextime2 = calculateDefaultFlexTime(period);
                android.util.Slog.e("SyncManager", "Error formatting value parsed for periodic sync flex, using default: " + flextime2, e);
                flextime = flextime2;
            }
            android.content.PeriodicSync periodicSync = new android.content.PeriodicSync(authorityInfo.target.account, authorityInfo.target.provider, extras, period, flextime);
            authorityInfo.periodicSyncs.add(periodicSync);
            return periodicSync;
        } catch (org.xmlpull.v1.XmlPullParserException e2) {
            android.util.Slog.e("SyncManager", "error parsing the period of a periodic sync", e2);
            return null;
        }
    }

    private void parseExtra(com.android.modules.utils.TypedXmlPullParser parser, android.os.Bundle extras) {
        java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
        java.lang.String type = parser.getAttributeValue((java.lang.String) null, "type");
        try {
            if ("long".equals(type)) {
                extras.putLong(name, parser.getAttributeLong((java.lang.String) null, "value1"));
            } else if ("integer".equals(type)) {
                extras.putInt(name, parser.getAttributeInt((java.lang.String) null, "value1"));
            } else if ("double".equals(type)) {
                extras.putDouble(name, parser.getAttributeDouble((java.lang.String) null, "value1"));
            } else if ("float".equals(type)) {
                extras.putFloat(name, parser.getAttributeFloat((java.lang.String) null, "value1"));
            } else if ("boolean".equals(type)) {
                extras.putBoolean(name, parser.getAttributeBoolean((java.lang.String) null, "value1"));
            } else if ("string".equals(type)) {
                extras.putString(name, parser.getAttributeValue((java.lang.String) null, "value1"));
            } else if ("account".equals(type)) {
                java.lang.String value1 = parser.getAttributeValue((java.lang.String) null, "value1");
                java.lang.String value2 = parser.getAttributeValue((java.lang.String) null, "value2");
                extras.putParcelable(name, new android.accounts.Account(value1, value2));
            }
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e("SyncManager", "error parsing bundle value", e);
        }
    }

    private void writeAccountInfoLocked() {
        if (android.util.Log.isLoggable(TAG_FILE, 2)) {
            android.util.Slog.v(TAG_FILE, "Writing new " + this.mAccountInfoFile.getBaseFile());
        }
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mAccountInfoFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            out.startDocument((java.lang.String) null, true);
            out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            out.startTag((java.lang.String) null, "accounts");
            out.attributeInt((java.lang.String) null, "version", 3);
            out.attributeInt((java.lang.String) null, XML_ATTR_NEXT_AUTHORITY_ID, this.mNextAuthorityId);
            out.attributeInt((java.lang.String) null, XML_ATTR_SYNC_RANDOM_OFFSET, this.mSyncRandomOffset);
            int M = this.mMasterSyncAutomatically.size();
            for (int m = 0; m < M; m++) {
                int userId = this.mMasterSyncAutomatically.keyAt(m);
                java.lang.Boolean listen = this.mMasterSyncAutomatically.valueAt(m);
                out.startTag((java.lang.String) null, XML_TAG_LISTEN_FOR_TICKLES);
                out.attributeInt((java.lang.String) null, XML_ATTR_USER, userId);
                out.attributeBoolean((java.lang.String) null, "enabled", listen.booleanValue());
                out.endTag((java.lang.String) null, XML_TAG_LISTEN_FOR_TICKLES);
            }
            int N = this.mAuthorities.size();
            for (int i = 0; i < N; i++) {
                com.android.server.content.SyncStorageEngine.AuthorityInfo authority = this.mAuthorities.valueAt(i);
                com.android.server.content.SyncStorageEngine.EndPoint info = authority.target;
                out.startTag((java.lang.String) null, "authority");
                out.attributeInt((java.lang.String) null, "id", authority.ident);
                out.attributeInt((java.lang.String) null, XML_ATTR_USER, info.userId);
                out.attributeBoolean((java.lang.String) null, "enabled", authority.enabled);
                out.attribute((java.lang.String) null, "account", info.account.name);
                out.attribute((java.lang.String) null, "type", info.account.type);
                out.attribute((java.lang.String) null, "authority", info.provider);
                out.attributeInt((java.lang.String) null, "syncable", authority.syncable);
                out.endTag((java.lang.String) null, "authority");
            }
            out.endTag((java.lang.String) null, "accounts");
            out.endDocument();
            this.mAccountInfoFile.finishWrite(fos);
        } catch (java.io.IOException e1) {
            android.util.Slog.w("SyncManager", "Error writing accounts", e1);
            if (fos != null) {
                this.mAccountInfoFile.failWrite(fos);
            }
        }
    }

    private void readStatusParcelLocked(java.io.File parcel) {
        android.os.Parcel in;
        try {
            android.util.AtomicFile parcelFile = new android.util.AtomicFile(parcel);
            byte[] data = parcelFile.readFully();
            in = android.os.Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
        } catch (java.io.IOException e) {
            android.util.Slog.i("SyncManager", "No initial status");
            return;
        }
        while (true) {
            int token = in.readInt();
            if (token != 0) {
                if (token != 100) {
                    android.util.Slog.w("SyncManager", "Unknown status token: " + token);
                    return;
                }
                try {
                    android.content.SyncStatusInfo status = new android.content.SyncStatusInfo(in);
                    if (this.mAuthorities.indexOfKey(status.authorityId) >= 0) {
                        status.pending = false;
                        this.mSyncStatus.put(status.authorityId, status);
                    }
                } catch (java.lang.Exception e2) {
                    android.util.Slog.e("SyncManager", "Unable to parse some sync status.", e2);
                }
                android.util.Slog.i("SyncManager", "No initial status");
                return;
            }
            return;
        }
    }

    private void upgradeStatusIfNeededLocked() {
        java.io.File parcelStatus = new java.io.File(this.mSyncDir, LEGACY_STATUS_FILE_NAME);
        if (parcelStatus.exists() && !this.mStatusFile.exists()) {
            readStatusParcelLocked(parcelStatus);
            writeStatusLocked();
        }
        if (parcelStatus.exists() && this.mStatusFile.exists()) {
            parcelStatus.delete();
        }
    }

    void readStatusLocked() {
        upgradeStatusIfNeededLocked();
        if (!this.mStatusFile.exists()) {
            return;
        }
        try {
            java.io.FileInputStream in = this.mStatusFile.openRead();
            try {
                readStatusInfoLocked(in);
                if (in != null) {
                    in.close();
                }
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e("SyncManager", "Unable to read status info file.", e);
        }
    }

    private void readStatusInfoLocked(java.io.InputStream in) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 1:
                    long token = proto.start(2246267895809L);
                    android.content.SyncStatusInfo status = readSyncStatusInfoLocked(proto);
                    proto.end(token);
                    if (this.mAuthorities.indexOfKey(status.authorityId) >= 0) {
                        status.pending = false;
                        this.mSyncStatus.put(status.authorityId, status);
                    }
                    break;
                case 2:
                    this.mIsJobNamespaceMigrated = proto.readBoolean(1133871366146L);
                    break;
                case 3:
                    this.mIsJobAttributionFixed = proto.readBoolean(1133871366147L);
                    break;
            }
        }
    }

    private android.content.SyncStatusInfo readSyncStatusInfoLocked(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        android.content.SyncStatusInfo status;
        if (proto.nextField(1120986464258L)) {
            status = new android.content.SyncStatusInfo(proto.readInt(1120986464258L));
        } else {
            status = new android.content.SyncStatusInfo(0);
        }
        int successTimesCount = 0;
        int failureTimesCount = 0;
        java.util.ArrayList<android.util.Pair<java.lang.Long, java.lang.String>> lastEventInformation = new java.util.ArrayList<>();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    status.populateLastEventsInformation(lastEventInformation);
                    return status;
                case 2:
                    android.util.Slog.w("SyncManager", "Failed to read the authority id via fast-path; some data might not have been read.");
                    status = new android.content.SyncStatusInfo(proto.readInt(1120986464258L), status);
                    break;
                case 3:
                    status.lastSuccessTime = proto.readLong(1112396529667L);
                    break;
                case 4:
                    status.lastSuccessSource = proto.readInt(1120986464260L);
                    break;
                case 5:
                    status.lastFailureTime = proto.readLong(1112396529669L);
                    break;
                case 6:
                    status.lastFailureSource = proto.readInt(1120986464262L);
                    break;
                case 7:
                    status.lastFailureMesg = proto.readString(1138166333447L);
                    break;
                case 8:
                    status.initialFailureTime = proto.readLong(1112396529672L);
                    break;
                case 9:
                    status.pending = proto.readBoolean(1133871366153L);
                    break;
                case 10:
                    status.initialize = proto.readBoolean(1133871366154L);
                    break;
                case 11:
                    status.addPeriodicSyncTime(proto.readLong(2211908157451L));
                    break;
                case 12:
                    long eventToken = proto.start(2246267895820L);
                    android.util.Pair<java.lang.Long, java.lang.String> lastEventInfo = parseLastEventInfoLocked(proto);
                    if (lastEventInfo != null) {
                        lastEventInformation.add(lastEventInfo);
                    }
                    proto.end(eventToken);
                    break;
                case 13:
                    status.lastTodayResetTime = proto.readLong(1112396529677L);
                    break;
                case 14:
                    long totalStatsToken = proto.start(1146756268046L);
                    readSyncStatusStatsLocked(proto, status.totalStats);
                    proto.end(totalStatsToken);
                    break;
                case 15:
                    long todayStatsToken = proto.start(1146756268047L);
                    readSyncStatusStatsLocked(proto, status.todayStats);
                    proto.end(todayStatsToken);
                    break;
                case 16:
                    long yesterdayStatsToken = proto.start(1146756268048L);
                    readSyncStatusStatsLocked(proto, status.yesterdayStats);
                    proto.end(yesterdayStatsToken);
                    break;
                case 17:
                    long successTime = proto.readLong(2211908157457L);
                    if (successTimesCount == status.perSourceLastSuccessTimes.length) {
                        android.util.Slog.w("SyncManager", "Attempted to read more per source last success times than expected; data might be corrupted.");
                    } else {
                        status.perSourceLastSuccessTimes[successTimesCount] = successTime;
                        successTimesCount++;
                    }
                    break;
                case 18:
                    long failureTime = proto.readLong(2211908157458L);
                    if (failureTimesCount == status.perSourceLastFailureTimes.length) {
                        android.util.Slog.w("SyncManager", "Attempted to read more per source last failure times than expected; data might be corrupted.");
                    } else {
                        status.perSourceLastFailureTimes[failureTimesCount] = failureTime;
                        failureTimesCount++;
                    }
                    break;
            }
        }
    }

    private android.util.Pair<java.lang.Long, java.lang.String> parseLastEventInfoLocked(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        long time = 0;
        java.lang.String message = null;
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (message == null) {
                        return null;
                    }
                    return new android.util.Pair<>(java.lang.Long.valueOf(time), message);
                case 1:
                    time = proto.readLong(1112396529665L);
                    break;
                case 2:
                    message = proto.readString(1138166333442L);
                    break;
            }
        }
    }

    private void readSyncStatusStatsLocked(android.util.proto.ProtoInputStream proto, android.content.SyncStatusInfo.Stats stats) throws java.io.IOException {
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 1:
                    stats.totalElapsedTime = proto.readLong(1112396529665L);
                    break;
                case 2:
                    stats.numSyncs = proto.readInt(1120986464258L);
                    break;
                case 3:
                    stats.numFailures = proto.readInt(1120986464259L);
                    break;
                case 4:
                    stats.numCancels = proto.readInt(1120986464260L);
                    break;
                case 5:
                    stats.numSourceOther = proto.readInt(1120986464261L);
                    break;
                case 6:
                    stats.numSourceLocal = proto.readInt(1120986464262L);
                    break;
                case 7:
                    stats.numSourcePoll = proto.readInt(1120986464263L);
                    break;
                case 8:
                    stats.numSourceUser = proto.readInt(1120986464264L);
                    break;
                case 9:
                    stats.numSourcePeriodic = proto.readInt(1120986464265L);
                    break;
                case 10:
                    stats.numSourceFeed = proto.readInt(1120986464266L);
                    break;
            }
        }
    }

    void writeStatusLocked() {
        if (android.util.Log.isLoggable(TAG_FILE, 2)) {
            android.util.Slog.v(TAG_FILE, "Writing new " + this.mStatusFile.getBaseFile());
        }
        this.mHandler.removeMessages(1);
        java.io.FileOutputStream fos = null;
        try {
            try {
                fos = this.mStatusFile.startWrite();
                writeStatusInfoLocked(fos);
                this.mStatusFile.finishWrite(fos);
                fos = null;
            } catch (java.io.IOException | java.lang.IllegalArgumentException e) {
                android.util.Slog.e("SyncManager", "Unable to write sync status to proto.", e);
            }
        } finally {
            this.mStatusFile.failWrite(fos);
        }
    }

    private void writeStatusInfoLocked(java.io.OutputStream out) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        int size = this.mSyncStatus.size();
        int i = 0;
        while (i < size) {
            android.content.SyncStatusInfo info = this.mSyncStatus.valueAt(i);
            long token = proto.start(2246267895809L);
            proto.write(1120986464258L, info.authorityId);
            proto.write(1112396529667L, info.lastSuccessTime);
            proto.write(1120986464260L, info.lastSuccessSource);
            proto.write(1112396529669L, info.lastFailureTime);
            proto.write(1120986464262L, info.lastFailureSource);
            proto.write(1138166333447L, info.lastFailureMesg);
            proto.write(1112396529672L, info.initialFailureTime);
            proto.write(1133871366153L, info.pending);
            proto.write(1133871366154L, info.initialize);
            int periodicSyncTimesSize = info.getPeriodicSyncTimesSize();
            for (int j = 0; j < periodicSyncTimesSize; j++) {
                proto.write(2211908157451L, info.getPeriodicSyncTime(j));
            }
            int lastEventsSize = info.getEventCount();
            int j2 = 0;
            while (j2 < lastEventsSize) {
                long eventToken = proto.start(2246267895820L);
                proto.write(1112396529665L, info.getEventTime(j2));
                proto.write(1138166333442L, info.getEvent(j2));
                proto.end(eventToken);
                j2++;
                size = size;
            }
            int size2 = size;
            proto.write(1112396529677L, info.lastTodayResetTime);
            long totalStatsToken = proto.start(1146756268046L);
            writeStatusStatsLocked(proto, info.totalStats);
            proto.end(totalStatsToken);
            long todayStatsToken = proto.start(1146756268047L);
            writeStatusStatsLocked(proto, info.todayStats);
            proto.end(todayStatsToken);
            long yesterdayStatsToken = proto.start(1146756268048L);
            writeStatusStatsLocked(proto, info.yesterdayStats);
            proto.end(yesterdayStatsToken);
            int lastSuccessTimesSize = info.perSourceLastSuccessTimes.length;
            int j3 = 0;
            while (j3 < lastSuccessTimesSize) {
                proto.write(2211908157457L, info.perSourceLastSuccessTimes[j3]);
                j3++;
                periodicSyncTimesSize = periodicSyncTimesSize;
                lastEventsSize = lastEventsSize;
                todayStatsToken = todayStatsToken;
            }
            int lastFailureTimesSize = info.perSourceLastFailureTimes.length;
            for (int j4 = 0; j4 < lastFailureTimesSize; j4++) {
                proto.write(2211908157458L, info.perSourceLastFailureTimes[j4]);
            }
            proto.end(token);
            i++;
            size = size2;
        }
        proto.write(1133871366146L, this.mIsJobNamespaceMigrated);
        proto.write(1133871366147L, this.mIsJobAttributionFixed);
        proto.flush();
    }

    private void writeStatusStatsLocked(android.util.proto.ProtoOutputStream proto, android.content.SyncStatusInfo.Stats stats) {
        proto.write(1112396529665L, stats.totalElapsedTime);
        proto.write(1120986464258L, stats.numSyncs);
        proto.write(1120986464259L, stats.numFailures);
        proto.write(1120986464260L, stats.numCancels);
        proto.write(1120986464261L, stats.numSourceOther);
        proto.write(1120986464262L, stats.numSourceLocal);
        proto.write(1120986464263L, stats.numSourcePoll);
        proto.write(1120986464264L, stats.numSourceUser);
        proto.write(1120986464265L, stats.numSourcePeriodic);
        proto.write(1120986464266L, stats.numSourceFeed);
    }

    private void requestSync(com.android.server.content.SyncStorageEngine.AuthorityInfo authorityInfo, int reason, android.os.Bundle extras, int syncExemptionFlag, int callingUid, int callingPid) {
        if (android.os.Process.myUid() == 1000 && this.mSyncRequestListener != null) {
            this.mSyncRequestListener.onSyncRequest(authorityInfo.target, reason, extras, syncExemptionFlag, callingUid, callingPid);
            return;
        }
        android.content.SyncRequest.Builder req = new android.content.SyncRequest.Builder().syncOnce().setExtras(extras);
        req.setSyncAdapter(authorityInfo.target.account, authorityInfo.target.provider);
        android.content.ContentResolver.requestSync(req.build());
    }

    private void requestSync(android.accounts.Account account, int userId, int reason, java.lang.String authority, android.os.Bundle extras, int syncExemptionFlag, int callingUid, int callingPid) {
        if (android.os.Process.myUid() == 1000 && this.mSyncRequestListener != null) {
            this.mSyncRequestListener.onSyncRequest(new com.android.server.content.SyncStorageEngine.EndPoint(account, authority, userId), reason, extras, syncExemptionFlag, callingUid, callingPid);
        } else {
            android.content.ContentResolver.requestSync(account, authority, extras);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0028, code lost:
    
        android.util.Slog.w("SyncManager", "Unknown stats token: " + r5);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readStatsParcelLocked(java.io.File r11) {
        /*
            r10 = this;
            java.lang.String r0 = "SyncManager"
            android.os.Parcel r1 = android.os.Parcel.obtain()
            android.util.AtomicFile r2 = new android.util.AtomicFile     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r2.<init>(r11)     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            byte[] r3 = r2.readFully()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            int r4 = r3.length     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r5 = 0
            r1.unmarshall(r3, r5, r4)     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r1.setDataPosition(r5)     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r4 = 0
        L18:
            int r5 = r1.readInt()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r6 = r5
            if (r5 == 0) goto L72
            r5 = 101(0x65, float:1.42E-43)
            r7 = 100
            if (r6 == r5) goto L3f
            if (r6 != r7) goto L28
            goto L3f
        L28:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r5.<init>()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            java.lang.String r7 = "Unknown stats token: "
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            android.util.Slog.w(r0, r5)     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            goto L72
        L3f:
            int r5 = r1.readInt()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            if (r6 != r7) goto L49
            int r7 = r5 + (-2009)
            int r5 = r7 + 14245
        L49:
            com.android.server.content.SyncStorageEngine$DayStats r7 = new com.android.server.content.SyncStorageEngine$DayStats     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r7.<init>(r5)     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            int r8 = r1.readInt()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r7.successCount = r8     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            long r8 = r1.readLong()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r7.successTime = r8     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            int r8 = r1.readInt()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r7.failureCount = r8     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            long r8 = r1.readLong()     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r7.failureTime = r8     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            com.android.server.content.SyncStorageEngine$DayStats[] r8 = r10.mDayStats     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            int r8 = r8.length     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            if (r4 >= r8) goto L71
            com.android.server.content.SyncStorageEngine$DayStats[] r8 = r10.mDayStats     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            r8[r4] = r7     // Catch: java.lang.Throwable -> L73 java.io.IOException -> L75
            int r4 = r4 + 1
        L71:
            goto L18
        L72:
            goto L7c
        L73:
            r0 = move-exception
            goto L81
        L75:
            r2 = move-exception
            java.lang.String r3 = "No initial statistics"
            android.util.Slog.i(r0, r3)     // Catch: java.lang.Throwable -> L73
        L7c:
            r1.recycle()
            return
        L81:
            r1.recycle()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.content.SyncStorageEngine.readStatsParcelLocked(java.io.File):void");
    }

    private void upgradeStatisticsIfNeededLocked() {
        java.io.File parcelStats = new java.io.File(this.mSyncDir, LEGACY_STATISTICS_FILE_NAME);
        if (parcelStats.exists() && !this.mStatisticsFile.exists()) {
            readStatsParcelLocked(parcelStats);
            writeStatisticsLocked();
        }
        if (parcelStats.exists() && this.mStatisticsFile.exists()) {
            parcelStats.delete();
        }
    }

    private void readStatisticsLocked() {
        upgradeStatisticsIfNeededLocked();
        if (!this.mStatisticsFile.exists()) {
            return;
        }
        try {
            java.io.FileInputStream in = this.mStatisticsFile.openRead();
            try {
                readDayStatsLocked(in);
                if (in != null) {
                    in.close();
                }
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e("SyncManager", "Unable to read day stats file.", e);
        }
    }

    private void readDayStatsLocked(java.io.InputStream in) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        int statsCount = 0;
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 1:
                    long token = proto.start(2246267895809L);
                    com.android.server.content.SyncStorageEngine.DayStats stats = readIndividualDayStatsLocked(proto);
                    proto.end(token);
                    this.mDayStats[statsCount] = stats;
                    statsCount++;
                    if (statsCount == this.mDayStats.length) {
                        return;
                    }
                    break;
            }
        }
    }

    private com.android.server.content.SyncStorageEngine.DayStats readIndividualDayStatsLocked(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        com.android.server.content.SyncStorageEngine.DayStats stats;
        if (proto.nextField(1120986464257L)) {
            stats = new com.android.server.content.SyncStorageEngine.DayStats(proto.readInt(1120986464257L));
        } else {
            stats = new com.android.server.content.SyncStorageEngine.DayStats(0);
        }
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return stats;
                case 1:
                    android.util.Slog.w("SyncManager", "Failed to read the day via fast-path; some data might not have been read.");
                    com.android.server.content.SyncStorageEngine.DayStats temp = new com.android.server.content.SyncStorageEngine.DayStats(proto.readInt(1120986464257L));
                    temp.successCount = stats.successCount;
                    temp.successTime = stats.successTime;
                    temp.failureCount = stats.failureCount;
                    temp.failureTime = stats.failureTime;
                    stats = temp;
                    break;
                case 2:
                    stats.successCount = proto.readInt(1120986464258L);
                    break;
                case 3:
                    stats.successTime = proto.readLong(1112396529667L);
                    break;
                case 4:
                    stats.failureCount = proto.readInt(1120986464260L);
                    break;
                case 5:
                    stats.failureTime = proto.readLong(1112396529669L);
                    break;
            }
        }
    }

    void writeStatisticsLocked() {
        if (android.util.Log.isLoggable(TAG_FILE, 2)) {
            android.util.Slog.v("SyncManager", "Writing new " + this.mStatisticsFile.getBaseFile());
        }
        this.mHandler.removeMessages(2);
        java.io.FileOutputStream fos = null;
        try {
            try {
                fos = this.mStatisticsFile.startWrite();
                writeDayStatsLocked(fos);
                this.mStatisticsFile.finishWrite(fos);
                fos = null;
            } catch (java.io.IOException | java.lang.IllegalArgumentException e) {
                android.util.Slog.e("SyncManager", "Unable to write day stats to proto.", e);
            }
        } finally {
            this.mStatisticsFile.failWrite(fos);
        }
    }

    private void writeDayStatsLocked(java.io.OutputStream out) throws java.io.IOException, java.lang.IllegalArgumentException {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        int size = this.mDayStats.length;
        for (int i = 0; i < size; i++) {
            com.android.server.content.SyncStorageEngine.DayStats stats = this.mDayStats[i];
            if (stats == null) {
                break;
            }
            long token = proto.start(2246267895809L);
            proto.write(1120986464257L, stats.day);
            proto.write(1120986464258L, stats.successCount);
            proto.write(1112396529667L, stats.successTime);
            proto.write(1120986464260L, stats.failureCount);
            proto.write(1112396529669L, stats.failureTime);
            proto.end(token);
        }
        proto.flush();
    }

    public void queueBackup() {
        android.app.backup.BackupManager.dataChanged(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
    }

    public void setClockValid() {
        if (!this.mIsClockValid) {
            this.mIsClockValid = true;
            android.util.Slog.w("SyncManager", "Clock is valid now.");
        }
    }

    public boolean isClockValid() {
        return this.mIsClockValid;
    }

    public void resetTodayStats(boolean force) {
        if (force) {
            android.util.Log.w("SyncManager", "Force resetting today stats.");
        }
        synchronized (this.mAuthorities) {
            int N = this.mSyncStatus.size();
            for (int i = 0; i < N; i++) {
                android.content.SyncStatusInfo cur = this.mSyncStatus.valueAt(i);
                cur.maybeResetTodayStats(isClockValid(), force);
            }
            writeStatusLocked();
        }
    }
}
