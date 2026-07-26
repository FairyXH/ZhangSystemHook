package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkPolicyManagerService extends android.net.INetworkPolicyManager.Stub {
    private static final java.lang.String ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED = "android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED";
    private static final java.lang.String ACTION_NERVER_REMIND = "oplus.intent.action.NERVER_REMIND";
    private static final java.lang.String ACTION_SNOOZE_RAPID = "com.android.server.net.action.SNOOZE_RAPID";
    private static final java.lang.String ACTION_SNOOZE_WARNING = "com.android.server.net.action.SNOOZE_WARNING";
    private static final java.lang.String ATTR_APP_ID = "appId";

    @java.lang.Deprecated
    private static final java.lang.String ATTR_CYCLE_DAY = "cycleDay";
    private static final java.lang.String ATTR_CYCLE_END = "cycleEnd";
    private static final java.lang.String ATTR_CYCLE_PERIOD = "cyclePeriod";
    private static final java.lang.String ATTR_CYCLE_START = "cycleStart";

    @java.lang.Deprecated
    private static final java.lang.String ATTR_CYCLE_TIMEZONE = "cycleTimezone";
    private static final java.lang.String ATTR_INFERRED = "inferred";
    private static final java.lang.String ATTR_LAST_LIMIT_SNOOZE = "lastLimitSnooze";
    private static final java.lang.String ATTR_LAST_SNOOZE = "lastSnooze";
    private static final java.lang.String ATTR_LAST_WARNING_SNOOZE = "lastWarningSnooze";
    private static final java.lang.String ATTR_LIMIT_BEHAVIOR = "limitBehavior";
    private static final java.lang.String ATTR_LIMIT_BYTES = "limitBytes";
    private static final java.lang.String ATTR_METERED = "metered";
    private static final java.lang.String ATTR_NETWORK_ID = "networkId";
    private static final java.lang.String ATTR_NETWORK_TEMPLATE = "networkTemplate";
    private static final java.lang.String ATTR_NETWORK_TYPES = "networkTypes";
    private static final java.lang.String ATTR_OWNER_PACKAGE = "ownerPackage";
    private static final java.lang.String ATTR_POLICY = "policy";
    private static final java.lang.String ATTR_RESTRICT_BACKGROUND = "restrictBackground";
    private static final java.lang.String ATTR_SUBSCRIBER_ID = "subscriberId";
    private static final java.lang.String ATTR_SUBSCRIBER_ID_MATCH_RULE = "subscriberIdMatchRule";
    private static final java.lang.String ATTR_SUB_ID = "subId";
    private static final java.lang.String ATTR_SUMMARY = "summary";
    private static final java.lang.String ATTR_TEMPLATE_METERED = "templateMetered";
    private static final java.lang.String ATTR_TITLE = "title";
    private static final java.lang.String ATTR_UID = "uid";
    private static final java.lang.String ATTR_USAGE_BYTES = "usageBytes";
    private static final java.lang.String ATTR_USAGE_TIME = "usageTime";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final java.lang.String ATTR_WARNING_BYTES = "warningBytes";
    private static final java.lang.String ATTR_XML_UTILS_NAME = "name";
    private static final int CHAIN_TOGGLE_DISABLE = 2;
    private static final int CHAIN_TOGGLE_ENABLE = 1;
    private static final int CHAIN_TOGGLE_NONE = 0;
    private static final int MSG_ADVISE_PERSIST_THRESHOLD = 7;
    private static final int MSG_CLEAR_SUBSCRIPTION_PLANS = 22;
    private static final int MSG_LIMIT_REACHED = 5;
    private static final int MSG_METERED_IFACES_CHANGED = 2;
    private static final int MSG_METERED_RESTRICTED_PACKAGES_CHANGED = 17;
    private static final int MSG_POLICIES_CHANGED = 13;
    private static final int MSG_PROCESS_BACKGROUND_TRANSITIONING_UIDS = 24;
    private static final int MSG_REMOVE_INTERFACE_QUOTAS = 11;
    private static final int MSG_RESET_FIREWALL_RULES_BY_UID = 15;
    private static final int MSG_RESTRICT_BACKGROUND_CHANGED = 6;
    private static final int MSG_RULES_CHANGED = 1;
    private static final int MSG_SET_NETWORK_TEMPLATE_ENABLED = 18;
    private static final int MSG_STATS_PROVIDER_WARNING_OR_LIMIT_REACHED = 20;
    private static final int MSG_SUBSCRIPTION_OVERRIDE = 16;
    private static final int MSG_SUBSCRIPTION_PLANS_CHANGED = 19;
    private static final int MSG_UIDS_BLOCKED_REASONS_CHANGED = 23;
    private static final int MSG_UID_BLOCKED_REASON_CHANGED = 21;
    private static final int MSG_UPDATE_INTERFACE_QUOTAS = 10;
    private static final int NOTIFICATION_LIMIT_AND_DISCONNECT = 1;
    private static final int NOT_NOTIFICATION_LIMIT = 0;
    public static final int OPPORTUNISTIC_QUOTA_UNKNOWN = -1;
    private static final java.lang.String PROP_SUB_PLAN_OWNER = "persist.sys.sub_plan_owner";
    private static final float QUOTA_FRAC_JOBS_DEFAULT = 0.5f;
    private static final float QUOTA_FRAC_MULTIPATH_DEFAULT = 0.5f;
    private static final float QUOTA_LIMITED_DEFAULT = 0.1f;
    static final java.lang.String TAG = "NetworkPolicy";
    private static final java.lang.String TAG_ALLOWLIST = "whitelist";
    private static final java.lang.String TAG_APP_POLICY = "app-policy";
    private static final java.lang.String TAG_NETWORK_POLICY = "network-policy";
    private static final java.lang.String TAG_POLICY_LIST = "policy-list";
    private static final java.lang.String TAG_RESTRICT_BACKGROUND = "restrict-background";
    private static final java.lang.String TAG_REVOKED_RESTRICT_BACKGROUND = "revoked-restrict-background";
    private static final java.lang.String TAG_UID_POLICY = "uid-policy";
    private static final java.lang.String TAG_XML_UTILS_INT_ARRAY = "int-array";
    public static final int TYPE_LIMIT = 35;
    public static final int TYPE_LIMIT_SNOOZED = 36;
    public static final int TYPE_RAPID = 45;
    public static final int TYPE_WARNING = 34;
    private static final int UID_MSG_GONE = 101;
    static final int UID_MSG_STATE_CHANGED = 100;
    private static final int VERSION_ADDED_CYCLE = 11;
    private static final int VERSION_ADDED_INFERRED = 7;
    private static final int VERSION_ADDED_METERED = 4;
    private static final int VERSION_ADDED_NETWORK_ID = 9;
    private static final int VERSION_ADDED_NETWORK_TYPES = 12;
    private static final int VERSION_ADDED_RESTRICT_BACKGROUND = 3;
    private static final int VERSION_ADDED_SNOOZE = 2;
    private static final int VERSION_ADDED_TIMEZONE = 6;
    private static final int VERSION_INIT = 1;
    private static final int VERSION_LATEST = 14;
    private static final int VERSION_REMOVED_SUBSCRIPTION_PLANS = 14;
    private static final int VERSION_SPLIT_SNOOZE = 5;
    private static final int VERSION_SUPPORTED_CARRIER_USAGE = 13;
    private static final int VERSION_SWITCH_APP_ID = 8;
    private static final int VERSION_SWITCH_UID = 10;
    private static final long WAIT_FOR_ADMIN_DATA_TIMEOUT_MS = 10000;
    private final com.android.server.net.NetworkPolicyManagerService.ActiveDataSubIdListener mActiveDataSubIdListener;
    private final android.util.ArraySet<com.android.server.net.NetworkPolicyManagerService.NotificationId> mActiveNotifs;
    private final android.app.IActivityManager mActivityManager;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private final java.util.concurrent.CountDownLatch mAdminDataAvailableLatch;
    private final android.net.INetworkManagementEventObserver mAlertObserver;
    private final android.util.SparseBooleanArray mAppIdleTempWhitelistAppIds;
    private final android.app.AppOpsManager mAppOps;
    private com.android.server.usage.AppStandbyInternal mAppStandby;
    private boolean mBackgroundNetworkRestricted;
    long mBackgroundRestrictionDelayMs;
    long mBackgroundRestrictionLongDelayMs;
    long mBackgroundRestrictionShortDelayMs;
    private final android.util.SparseLongArray mBackgroundTransitioningUids;
    private final android.telephony.CarrierConfigManager mCarrierConfigManager;
    private android.content.BroadcastReceiver mCarrierConfigReceiver;
    private final java.time.Clock mClock;
    private android.net.ConnectivityManager mConnManager;
    private android.content.BroadcastReceiver mConnReceiver;
    private final android.content.Context mContext;
    private final android.util.SparseBooleanArray mDefaultRestrictBackgroundAllowlistUids;
    private final com.android.server.net.NetworkPolicyManagerService.Dependencies mDeps;
    volatile boolean mDeviceIdleMode;
    final android.util.SparseBooleanArray mFirewallChainStates;
    final android.os.Handler mHandler;
    private final android.os.Handler.Callback mHandlerCallback;
    private final android.content.pm.IPackageManager mIPm;
    private final android.util.SparseBooleanArray mInternetPermissionMap;
    private final android.os.RemoteCallbackList<android.net.INetworkPolicyListener> mListeners;
    private boolean mLoadedRestrictBackground;
    private final com.android.server.net.NetworkPolicyLogger mLogger;
    volatile boolean mLowPowerStandbyActive;
    private final android.util.SparseBooleanArray mLowPowerStandbyAllowlistUids;
    private java.util.List<java.lang.String[]> mMergedSubscriberIds;
    private android.util.ArraySet<java.lang.String> mMeteredIfaces;
    final java.lang.Object mMeteredIfacesLock;
    private final android.util.SparseArray<java.util.Set<java.lang.Integer>> mMeteredRestrictedUids;
    private final com.android.server.connectivity.MultipathPolicyTracker mMultipathPolicyTracker;
    private final android.util.SparseIntArray mNetIdToSubId;
    private final android.net.ConnectivityManager.NetworkCallback mNetworkCallback;
    private final android.os.INetworkManagementService mNetworkManager;
    private volatile boolean mNetworkManagerReady;
    private final android.util.SparseBooleanArray mNetworkMetered;
    final java.lang.Object mNetworkPoliciesSecondLock;
    final android.util.ArrayMap<android.net.NetworkTemplate, android.net.NetworkPolicy> mNetworkPolicy;
    private int mNetworkPolicyChange;
    private final android.util.SparseBooleanArray mNetworkRoaming;
    private android.app.usage.NetworkStatsManager mNetworkStats;
    private android.util.SparseSetArray<java.lang.String> mNetworkToIfaces;
    private long mNextProcessBackgroundUidsTime;
    private com.android.server.net.IOplusNetworkPolicyManagerServiceEx mOplusNPMS;
    private final android.util.ArraySet<android.net.NetworkTemplate> mOverLimitNotified;
    private final android.content.BroadcastReceiver mPackageReceiver;
    private final android.util.AtomicFile mPolicyFile;
    private android.os.PowerExemptionManager mPowerExemptionManager;
    private android.os.PowerManagerInternal mPowerManagerInternal;
    private final android.content.BroadcastReceiver mPowerSaveAllowlistReceiver;
    private final android.util.SparseBooleanArray mPowerSaveTempWhitelistAppIds;
    private final android.util.SparseBooleanArray mPowerSaveWhitelistAppIds;
    private final android.util.SparseBooleanArray mPowerSaveWhitelistExceptIdleAppIds;
    volatile boolean mRestrictBackground;
    private final android.util.SparseBooleanArray mRestrictBackgroundAllowlistRevokedUids;
    private boolean mRestrictBackgroundBeforeBsm;
    volatile boolean mRestrictBackgroundChangedInBsm;
    private boolean mRestrictBackgroundLowPowerMode;
    volatile boolean mRestrictPower;
    private com.android.server.net.NetworkPolicyManagerService.RestrictedModeObserver mRestrictedModeObserver;
    volatile boolean mRestrictedNetworkingMode;
    int mSetSubscriptionPlansIdCounter;
    final android.util.SparseIntArray mSetSubscriptionPlansIds;
    private android.content.BroadcastReceiver mSimStateChangedReceiver;
    private final android.content.BroadcastReceiver mSnoozeReceiver;
    public final com.android.internal.util.StatLogger mStatLogger;
    private final com.android.server.net.NetworkPolicyManagerService.StatsCallback mStatsCallback;
    private final android.util.SparseArray<android.os.PersistableBundle> mSubIdToCarrierConfig;
    private final android.util.SparseArray<java.lang.String> mSubIdToSubscriberId;
    final android.util.SparseLongArray mSubscriptionOpportunisticQuota;
    final android.util.SparseArray<android.telephony.SubscriptionPlan[]> mSubscriptionPlans;
    final android.util.SparseArray<java.lang.String> mSubscriptionPlansOwner;
    private final boolean mSuppressDefaultPolicy;
    volatile boolean mSystemReady;
    private final android.util.SparseArray<com.android.server.net.NetworkPolicyManagerService.UidBlockedState> mTmpUidBlockedState;
    private final android.util.SparseArray<com.android.server.net.NetworkPolicyManagerService.UidBlockedState> mUidBlockedState;
    final android.os.Handler mUidEventHandler;
    private final android.os.Handler.Callback mUidEventHandlerCallback;
    private final com.android.server.ServiceThread mUidEventThread;
    final android.util.SparseIntArray mUidFirewallBackgroundRules;
    final android.util.SparseIntArray mUidFirewallDozableRules;
    final android.util.SparseIntArray mUidFirewallLowPowerStandbyModeRules;
    final android.util.SparseIntArray mUidFirewallPowerSaveRules;
    final android.util.SparseIntArray mUidFirewallRestrictedModeRules;
    final android.util.SparseIntArray mUidFirewallStandbyRules;
    private final android.app.IUidObserver mUidObserver;
    final android.util.SparseIntArray mUidPolicy;
    private final android.content.BroadcastReceiver mUidRemovedReceiver;
    final java.lang.Object mUidRulesFirstLock;
    private final android.util.SparseArray<android.net.NetworkPolicyManager.UidState> mUidState;
    private final android.util.SparseArray<com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo> mUidStateCallbackInfos;
    private android.app.usage.UsageStatsManagerInternal mUsageStats;
    private boolean mUseDifferentDelaysForBackgroundChain;
    private boolean mUseMeteredFirewallChains;
    private final android.os.UserManager mUserManager;
    private final android.content.BroadcastReceiver mUserReceiver;
    private final android.content.BroadcastReceiver mWifiReceiver;
    private com.android.server.net.INetworkPolicyManagerServiceWrapper mWrapper;
    private static final boolean LOGD = com.android.server.net.NetworkPolicyLogger.LOGD;
    private static final boolean LOGV = com.android.server.net.NetworkPolicyLogger.LOGV;
    private static final long QUOTA_UNLIMITED_DEFAULT = android.util.DataUnit.MEBIBYTES.toBytes(20);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ChainToggleType {
    }

    interface Stats {
        public static final int COUNT = 2;
        public static final int IS_UID_NETWORKING_BLOCKED = 1;
        public static final int UPDATE_NETWORK_ENABLED = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class RestrictedModeObserver extends android.database.ContentObserver {
        private final android.content.Context mContext;
        private final com.android.server.net.NetworkPolicyManagerService.RestrictedModeObserver.RestrictedModeListener mListener;

        public interface RestrictedModeListener {
            void onChange(boolean z);
        }

        RestrictedModeObserver(android.content.Context ctx, com.android.server.net.NetworkPolicyManagerService.RestrictedModeObserver.RestrictedModeListener listener) {
            super(null);
            this.mContext = ctx;
            this.mListener = listener;
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("restricted_networking_mode"), false, this);
        }

        public boolean isRestrictedModeEnabled() {
            return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "restricted_networking_mode", 0) != 0;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            this.mListener.onChange(isRestrictedModeEnabled());
        }
    }

    public NetworkPolicyManagerService(android.content.Context context, android.app.IActivityManager activityManager, android.os.INetworkManagementService networkManagement) {
        this(context, activityManager, networkManagement, android.app.AppGlobals.getPackageManager(), getDefaultClock(), getDefaultSystemDir(), false, new com.android.server.net.NetworkPolicyManagerService.Dependencies(context));
    }

    private static java.io.File getDefaultSystemDir() {
        return new java.io.File(android.os.Environment.getDataDirectory(), "system");
    }

    private static java.time.Clock getDefaultClock() {
        return new android.os.BestClock(java.time.ZoneOffset.UTC, new java.time.Clock[]{android.os.SystemClock.currentNetworkTimeClock(), java.time.Clock.systemUTC()});
    }

    android.net.NetworkPolicyManager.UidState getUidStateForTest(int uid) {
        android.net.NetworkPolicyManager.UidState uidState;
        synchronized (this.mUidRulesFirstLock) {
            uidState = this.mUidState.get(uid);
        }
        return uidState;
    }

    static class Dependencies {
        final android.content.Context mContext;
        final android.app.usage.NetworkStatsManager mNetworkStatsManager;

        Dependencies(android.content.Context context) {
            this.mContext = context;
            this.mNetworkStatsManager = (android.app.usage.NetworkStatsManager) this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class);
            this.mNetworkStatsManager.setPollOnOpen(false);
        }

        long getNetworkTotalBytes(android.net.NetworkTemplate template, long start, long end) {
            android.os.Trace.traceBegin(2097152L, "getNetworkTotalBytes");
            try {
                try {
                    android.app.usage.NetworkStats.Bucket ret = this.mNetworkStatsManager.querySummaryForDevice(template, start, end);
                    return ret.getRxBytes() + ret.getTxBytes();
                } catch (java.lang.RuntimeException e) {
                    android.util.Slog.w(com.android.server.net.NetworkPolicyManagerService.TAG, "Failed to read network stats: " + e);
                    android.os.Trace.traceEnd(2097152L);
                    return 0L;
                }
            } finally {
                android.os.Trace.traceEnd(2097152L);
            }
        }

        java.util.List<android.app.usage.NetworkStats.Bucket> getNetworkUidBytes(android.net.NetworkTemplate template, long start, long end) {
            android.os.Trace.traceBegin(2097152L, "getNetworkUidBytes");
            java.util.List<android.app.usage.NetworkStats.Bucket> buckets = new java.util.ArrayList<>();
            try {
                try {
                    android.app.usage.NetworkStats stats = this.mNetworkStatsManager.querySummary(template, start, end);
                    while (stats.hasNextBucket()) {
                        android.app.usage.NetworkStats.Bucket bucket = new android.app.usage.NetworkStats.Bucket();
                        stats.getNextBucket(bucket);
                        buckets.add(bucket);
                    }
                } catch (java.lang.RuntimeException e) {
                    android.util.Slog.w(com.android.server.net.NetworkPolicyManagerService.TAG, "Failed to read network stats: " + e);
                }
                return buckets;
            } finally {
                android.os.Trace.traceEnd(2097152L);
            }
        }

        int getDefaultDataSubId() {
            return android.telephony.SubscriptionManager.getDefaultDataSubscriptionId();
        }

        int getActivateDataSubId() {
            return android.telephony.SubscriptionManager.getActiveDataSubscriptionId();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public NetworkPolicyManagerService(android.content.Context context, android.app.IActivityManager iActivityManager, android.os.INetworkManagementService iNetworkManagementService, android.content.pm.IPackageManager iPackageManager, java.time.Clock clock, java.io.File file, boolean z, com.android.server.net.NetworkPolicyManagerService.Dependencies dependencies) {
        this.mUidRulesFirstLock = new java.lang.Object();
        this.mNetworkPoliciesSecondLock = new java.lang.Object();
        this.mAdminDataAvailableLatch = new java.util.concurrent.CountDownLatch(1);
        this.mBackgroundRestrictionDelayMs = java.util.concurrent.TimeUnit.SECONDS.toMillis(5L);
        this.mBackgroundRestrictionShortDelayMs = java.util.concurrent.TimeUnit.SECONDS.toMillis(2L);
        this.mBackgroundRestrictionLongDelayMs = java.util.concurrent.TimeUnit.SECONDS.toMillis(20L);
        this.mNextProcessBackgroundUidsTime = Long.MAX_VALUE;
        this.mNetworkPolicy = new android.util.ArrayMap<>();
        this.mSubscriptionPlans = new android.util.SparseArray<>();
        this.mSubscriptionPlansOwner = new android.util.SparseArray<>();
        this.mSetSubscriptionPlansIds = new android.util.SparseIntArray();
        this.mSetSubscriptionPlansIdCounter = 0;
        this.mSubscriptionOpportunisticQuota = new android.util.SparseLongArray();
        this.mUidPolicy = new android.util.SparseIntArray();
        this.mUidFirewallStandbyRules = new android.util.SparseIntArray();
        this.mUidFirewallDozableRules = new android.util.SparseIntArray();
        this.mUidFirewallPowerSaveRules = new android.util.SparseIntArray();
        this.mUidFirewallBackgroundRules = new android.util.SparseIntArray();
        this.mUidFirewallRestrictedModeRules = new android.util.SparseIntArray();
        this.mUidFirewallLowPowerStandbyModeRules = new android.util.SparseIntArray();
        this.mFirewallChainStates = new android.util.SparseBooleanArray();
        this.mPowerSaveWhitelistExceptIdleAppIds = new android.util.SparseBooleanArray();
        this.mPowerSaveWhitelistAppIds = new android.util.SparseBooleanArray();
        this.mPowerSaveTempWhitelistAppIds = new android.util.SparseBooleanArray();
        this.mLowPowerStandbyAllowlistUids = new android.util.SparseBooleanArray();
        this.mAppIdleTempWhitelistAppIds = new android.util.SparseBooleanArray();
        this.mDefaultRestrictBackgroundAllowlistUids = new android.util.SparseBooleanArray();
        this.mRestrictBackgroundAllowlistRevokedUids = new android.util.SparseBooleanArray();
        this.mMeteredIfacesLock = new java.lang.Object();
        this.mMeteredIfaces = new android.util.ArraySet<>();
        this.mOverLimitNotified = new android.util.ArraySet<>();
        this.mActiveNotifs = new android.util.ArraySet<>();
        this.mUidState = new android.util.SparseArray<>();
        this.mUidBlockedState = new android.util.SparseArray<>();
        this.mTmpUidBlockedState = new android.util.SparseArray<>();
        this.mBackgroundTransitioningUids = new android.util.SparseLongArray();
        this.mNetworkMetered = new android.util.SparseBooleanArray();
        this.mNetworkRoaming = new android.util.SparseBooleanArray();
        this.mNetworkToIfaces = new android.util.SparseSetArray<>();
        this.mNetIdToSubId = new android.util.SparseIntArray();
        this.mSubIdToSubscriberId = new android.util.SparseArray<>();
        this.mMergedSubscriberIds = new java.util.ArrayList();
        this.mSubIdToCarrierConfig = new android.util.SparseArray<>();
        this.mMeteredRestrictedUids = new android.util.SparseArray<>();
        this.mListeners = new android.os.RemoteCallbackList<>();
        this.mLogger = new com.android.server.net.NetworkPolicyLogger();
        this.mInternetPermissionMap = new android.util.SparseBooleanArray();
        this.mUidStateCallbackInfos = new android.util.SparseArray<>();
        this.mNetworkPolicyChange = 0;
        this.mOplusNPMS = null;
        this.mStatLogger = new com.android.internal.util.StatLogger(new java.lang.String[]{"updateNetworkEnabledNL()", "isUidNetworkingBlocked()"});
        this.mUidObserver = new android.app.UidObserver() { // from class: com.android.server.net.NetworkPolicyManagerService.4
            private boolean isUidStateChangeRelevant(com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo previousInfo, int newProcState, long newProcStateSeq, int newCapability) {
                if (previousInfo.procStateSeq == -1) {
                    return true;
                }
                if (newProcStateSeq <= previousInfo.procStateSeq) {
                    return false;
                }
                int previousProcState = previousInfo.procState;
                if (previousProcState <= 3 || newProcState <= 3) {
                    return true;
                }
                if ((previousProcState <= 5) != (newProcState <= 5)) {
                    return true;
                }
                if (com.android.server.net.NetworkPolicyManagerService.this.mBackgroundNetworkRestricted) {
                    if ((previousProcState >= 12) != (newProcState >= 12)) {
                        return true;
                    }
                    if (com.android.server.net.NetworkPolicyManagerService.this.mUseDifferentDelaysForBackgroundChain && newProcState >= 12 && com.android.server.net.NetworkPolicyManagerService.this.getBackgroundTransitioningDelay(newProcState) < com.android.server.net.NetworkPolicyManagerService.this.getBackgroundTransitioningDelay(previousProcState)) {
                        return true;
                    }
                }
                return (previousInfo.capability & 40) != (newCapability & 40);
            }

            public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidStateCallbackInfos) {
                    com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo callbackInfo = (com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo) com.android.server.net.NetworkPolicyManagerService.this.mUidStateCallbackInfos.get(uid);
                    if (callbackInfo == null) {
                        callbackInfo = new com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo();
                        com.android.server.net.NetworkPolicyManagerService.this.mUidStateCallbackInfos.put(uid, callbackInfo);
                    }
                    if (isUidStateChangeRelevant(callbackInfo, procState, procStateSeq, capability)) {
                        callbackInfo.update(uid, procState, procStateSeq, capability);
                        if (!callbackInfo.isPending) {
                            com.android.server.net.NetworkPolicyManagerService.this.mUidEventHandler.obtainMessage(100, uid, 0).sendToTarget();
                            callbackInfo.isPending = true;
                        }
                    }
                }
            }

            public void onUidGone(int uid, boolean disabled) {
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidStateCallbackInfos) {
                    com.android.server.net.NetworkPolicyManagerService.this.mUidStateCallbackInfos.remove(uid);
                }
                com.android.server.net.NetworkPolicyManagerService.this.mUidEventHandler.obtainMessage(101, uid, 0).sendToTarget();
            }
        };
        this.mPowerSaveAllowlistReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                    com.android.server.net.NetworkPolicyManagerService.this.updatePowerSaveAllowlistUL();
                    if (com.android.server.net.NetworkPolicyManagerService.this.mBackgroundNetworkRestricted) {
                        com.android.server.net.NetworkPolicyManagerService.this.updateRulesForBackgroundChainUL();
                    }
                    com.android.server.net.NetworkPolicyManagerService.this.updateRulesForRestrictPowerUL();
                    com.android.server.net.NetworkPolicyManagerService.this.updateRulesForAppIdleUL();
                }
            }
        };
        this.mPackageReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                if (uid != -1 && "android.intent.action.PACKAGE_ADDED".equals(action)) {
                    if (com.android.server.net.NetworkPolicyManagerService.LOGV) {
                        android.util.Slog.v(com.android.server.net.NetworkPolicyManagerService.TAG, "ACTION_PACKAGE_ADDED for uid=" + uid);
                    }
                    synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                        com.android.server.net.NetworkPolicyManagerService.this.mInternetPermissionMap.delete(uid);
                        com.android.server.net.NetworkPolicyManagerService.this.updateRestrictionRulesForUidUL(uid);
                    }
                }
            }
        };
        this.mUidRemovedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.7
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                if (uid == -1) {
                    return;
                }
                if (com.android.server.net.NetworkPolicyManagerService.LOGV) {
                    android.util.Slog.v(com.android.server.net.NetworkPolicyManagerService.TAG, "ACTION_UID_REMOVED for uid=" + uid);
                }
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                    com.android.server.net.NetworkPolicyManagerService.this.onUidDeletedUL(uid);
                    int cloneAppUid = com.android.server.net.NetworkPolicyManagerService.this.mOplusNPMS.getCloneAppUidNL(uid);
                    com.android.server.net.NetworkPolicyManagerService.this.onUidDeletedUL(cloneAppUid);
                    synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                        com.android.server.net.NetworkPolicyManagerService.this.writePolicyAL();
                    }
                }
            }
        };
        this.mUserReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.8
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                byte b = -1;
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (userId == -1) {
                    return;
                }
                switch (action.hashCode()) {
                    case -2061058799:
                        if (action.equals("android.intent.action.USER_REMOVED")) {
                            b = 0;
                        }
                        break;
                    case 1121780209:
                        if (action.equals("android.intent.action.USER_ADDED")) {
                            b = 1;
                        }
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                            com.android.server.net.NetworkPolicyManagerService.this.removeUserStateUL(userId, true, false);
                            com.android.server.net.NetworkPolicyManagerService.this.mMeteredRestrictedUids.remove(userId);
                            if (action == "android.intent.action.USER_ADDED") {
                                com.android.server.net.NetworkPolicyManagerService.this.addDefaultRestrictBackgroundAllowlistUidsUL(userId);
                            }
                            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                                com.android.server.net.NetworkPolicyManagerService.this.updateRulesForGlobalChangeAL(true);
                                break;
                            }
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mStatsCallback = new com.android.server.net.NetworkPolicyManagerService.StatsCallback();
        this.mSnoozeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.9
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                android.net.NetworkTemplate template = (android.net.NetworkTemplate) intent.getParcelableExtra("android.net.NETWORK_TEMPLATE", android.net.NetworkTemplate.class);
                if (com.android.server.net.NetworkPolicyManagerService.ACTION_SNOOZE_WARNING.equals(intent.getAction())) {
                    com.android.server.net.NetworkPolicyManagerService.this.performSnooze(template, 34);
                } else if (com.android.server.net.NetworkPolicyManagerService.ACTION_SNOOZE_RAPID.equals(intent.getAction())) {
                    com.android.server.net.NetworkPolicyManagerService.this.performSnooze(template, 45);
                }
            }
        };
        this.mWifiReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.10
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.net.NetworkPolicyManagerService.this.upgradeWifiMeteredOverride();
                com.android.server.net.NetworkPolicyManagerService.this.mContext.unregisterReceiver(this);
            }
        };
        this.mNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.net.NetworkPolicyManagerService.11
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities networkCapabilities) {
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                    boolean shouldUpdateNetworkRules = true;
                    boolean newMetered = !networkCapabilities.hasCapability(11);
                    boolean meteredChanged = com.android.server.net.NetworkPolicyManagerService.updateCapabilityChange(com.android.server.net.NetworkPolicyManagerService.this.mNetworkMetered, newMetered, network);
                    boolean newRoaming = !networkCapabilities.hasCapability(18);
                    boolean roamingChanged = com.android.server.net.NetworkPolicyManagerService.updateCapabilityChange(com.android.server.net.NetworkPolicyManagerService.this.mNetworkRoaming, newRoaming, network);
                    if (!meteredChanged && !roamingChanged) {
                        shouldUpdateNetworkRules = false;
                    }
                    if (meteredChanged) {
                        com.android.server.net.NetworkPolicyManagerService.this.mLogger.meterednessChanged(network.getNetId(), newMetered);
                    }
                    if (roamingChanged) {
                        com.android.server.net.NetworkPolicyManagerService.this.mLogger.roamingChanged(network.getNetId(), newRoaming);
                    }
                    if (shouldUpdateNetworkRules) {
                        com.android.server.net.NetworkPolicyManagerService.this.updateNetworkRulesNL();
                    }
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLinkPropertiesChanged(android.net.Network network, android.net.LinkProperties lp) {
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                    android.util.ArraySet<java.lang.String> newIfaces = new android.util.ArraySet<>(lp.getAllInterfaceNames());
                    boolean ifacesChanged = com.android.server.net.NetworkPolicyManagerService.this.updateNetworkToIfacesNL(network.getNetId(), newIfaces);
                    if (ifacesChanged) {
                        com.android.server.net.NetworkPolicyManagerService.this.mLogger.interfacesChanged(network.getNetId(), newIfaces);
                        com.android.server.net.NetworkPolicyManagerService.this.updateNetworkRulesNL();
                    }
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(android.net.Network network) {
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                    com.android.server.net.NetworkPolicyManagerService.this.mNetworkToIfaces.remove(network.getNetId());
                }
            }
        };
        this.mAlertObserver = new com.android.server.net.BaseNetworkObserver() { // from class: com.android.server.net.NetworkPolicyManagerService.12
            public void limitReached(java.lang.String limitName, java.lang.String iface) {
                android.net.NetworkStack.checkNetworkStackPermission(com.android.server.net.NetworkPolicyManagerService.this.mContext);
                if (!"globalAlert".equals(limitName)) {
                    com.android.server.net.NetworkPolicyManagerService.this.mHandler.obtainMessage(5, iface).sendToTarget();
                }
            }
        };
        this.mConnReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.13
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.net.NetworkPolicyManagerService.this.updateNetworksInternal();
            }
        };
        this.mCarrierConfigReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.14
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (!intent.hasExtra("android.telephony.extra.SUBSCRIPTION_INDEX")) {
                    return;
                }
                int subId = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                com.android.server.net.NetworkPolicyManagerService.this.updateSubscriptions();
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                    synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                        java.lang.String subscriberId = (java.lang.String) com.android.server.net.NetworkPolicyManagerService.this.mSubIdToSubscriberId.get(subId, null);
                        if (subscriberId != null) {
                            com.android.server.net.NetworkPolicyManagerService.this.ensureActiveCarrierPolicyAL(subId, subscriberId);
                            com.android.server.net.NetworkPolicyManagerService.this.maybeUpdateCarrierPolicyCycleAL(subId, subscriberId);
                        } else {
                            android.util.Slog.wtf(com.android.server.net.NetworkPolicyManagerService.TAG, "Missing subscriberId for subId " + subId);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.handleNetworkPoliciesUpdateAL(true);
                    }
                }
            }
        };
        this.mSimStateChangedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.net.NetworkPolicyManagerService.15
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String stateExtra = intent.getStringExtra("ss");
                if ("LOADED".equals(stateExtra)) {
                    android.util.Slog.i(com.android.server.net.NetworkPolicyManagerService.TAG, "INTENT_VALUE_ICC_LOADED");
                    com.android.server.net.NetworkPolicyManagerService.this.updateSubscriptions();
                }
            }
        };
        this.mHandlerCallback = new android.os.Handler.Callback() { // from class: com.android.server.net.NetworkPolicyManagerService.16
            @Override // android.os.Handler.Callback
            public boolean handleMessage(android.os.Message msg) {
                boolean enabled;
                switch (msg.what) {
                    case 1:
                        int uid = msg.arg1;
                        int uidRules = msg.arg2;
                        if (com.android.server.net.NetworkPolicyManagerService.LOGV) {
                            android.util.Slog.v(com.android.server.net.NetworkPolicyManagerService.TAG, "Dispatching rules=" + android.net.NetworkPolicyManager.uidRulesToString(uidRules) + " for uid=" + uid);
                        }
                        int length = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i = 0; i < length; i++) {
                            android.net.INetworkPolicyListener listener = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i);
                            com.android.server.net.NetworkPolicyManagerService.this.dispatchUidRulesChanged(listener, uid, uidRules);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        return true;
                    case 2:
                        java.lang.String[] meteredIfaces = (java.lang.String[]) msg.obj;
                        int length2 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i2 = 0; i2 < length2; i2++) {
                            android.net.INetworkPolicyListener listener2 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i2);
                            com.android.server.net.NetworkPolicyManagerService.this.dispatchMeteredIfacesChanged(listener2, meteredIfaces);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        return true;
                    case 3:
                    case 4:
                    case 8:
                    case 9:
                    case 12:
                    case 14:
                    default:
                        return false;
                    case 5:
                        java.lang.String iface = (java.lang.String) msg.obj;
                        synchronized (com.android.server.net.NetworkPolicyManagerService.this.mMeteredIfacesLock) {
                            if (!com.android.server.net.NetworkPolicyManagerService.this.mMeteredIfaces.contains(iface)) {
                                return true;
                            }
                            com.android.server.net.NetworkPolicyManagerService.this.mNetworkStats.forceUpdate();
                            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                                com.android.server.net.NetworkPolicyManagerService.this.updateNetworkRulesNL();
                                com.android.server.net.NetworkPolicyManagerService.this.updateNetworkEnabledNL();
                                com.android.server.net.NetworkPolicyManagerService.this.updateNotificationsNL();
                                break;
                            }
                            return true;
                        }
                    case 6:
                        enabled = msg.arg1 != 0;
                        boolean restrictBackground = enabled;
                        int length3 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i3 = 0; i3 < length3; i3++) {
                            android.net.INetworkPolicyListener listener3 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i3);
                            com.android.server.net.NetworkPolicyManagerService.this.dispatchRestrictBackgroundChanged(listener3, restrictBackground);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        android.content.Intent intent = new android.content.Intent("android.net.conn.RESTRICT_BACKGROUND_CHANGED");
                        intent.setFlags(1073741824);
                        com.android.server.net.NetworkPolicyManagerService.this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
                        return true;
                    case 7:
                        long lowestRule = ((java.lang.Long) msg.obj).longValue();
                        long persistThreshold = lowestRule / 1000;
                        com.android.server.net.NetworkPolicyManagerService.this.mNetworkStats.setDefaultGlobalAlert(persistThreshold);
                        return true;
                    case 10:
                        com.android.server.net.NetworkPolicyManagerService.IfaceQuotas val = (com.android.server.net.NetworkPolicyManagerService.IfaceQuotas) msg.obj;
                        com.android.server.net.NetworkPolicyManagerService.this.removeInterfaceLimit(val.iface);
                        com.android.server.net.NetworkPolicyManagerService.this.setInterfaceLimit(val.iface, val.limit);
                        com.android.server.net.NetworkPolicyManagerService.this.mNetworkStats.setStatsProviderWarningAndLimitAsync(val.iface, val.warning, val.limit);
                        return true;
                    case 11:
                        java.lang.String iface2 = (java.lang.String) msg.obj;
                        com.android.server.net.NetworkPolicyManagerService.this.removeInterfaceLimit(iface2);
                        com.android.server.net.NetworkPolicyManagerService.this.mNetworkStats.setStatsProviderWarningAndLimitAsync(iface2, -1L, -1L);
                        return true;
                    case 13:
                        int uid2 = msg.arg1;
                        int policy = msg.arg2;
                        java.lang.Boolean notifyApp = (java.lang.Boolean) msg.obj;
                        int length4 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i4 = 0; i4 < length4; i4++) {
                            android.net.INetworkPolicyListener listener4 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i4);
                            com.android.server.net.NetworkPolicyManagerService.this.dispatchUidPoliciesChanged(listener4, uid2, policy);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        if (notifyApp.booleanValue()) {
                            com.android.server.net.NetworkPolicyManagerService.this.broadcastRestrictBackgroundChanged(uid2, notifyApp);
                            return true;
                        }
                        return true;
                    case 15:
                        com.android.server.net.NetworkPolicyManagerService.this.resetUidFirewallRules(msg.arg1);
                        return true;
                    case 16:
                        com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                        int subId = ((java.lang.Integer) args.arg1).intValue();
                        int overrideMask = ((java.lang.Integer) args.arg2).intValue();
                        int overrideValue = ((java.lang.Integer) args.arg3).intValue();
                        int[] networkTypes = (int[]) args.arg4;
                        int length5 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i5 = 0; i5 < length5; i5++) {
                            android.net.INetworkPolicyListener listener5 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i5);
                            com.android.server.net.NetworkPolicyManagerService.this.dispatchSubscriptionOverride(listener5, subId, overrideMask, overrideValue, networkTypes);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        return true;
                    case 17:
                        int userId = msg.arg1;
                        java.util.Set<java.lang.String> packageNames = (java.util.Set) msg.obj;
                        com.android.server.net.NetworkPolicyManagerService.this.setMeteredRestrictedPackagesInternal(packageNames, userId);
                        return true;
                    case 18:
                        android.net.NetworkTemplate template = (android.net.NetworkTemplate) msg.obj;
                        enabled = msg.arg1 != 0;
                        com.android.server.net.NetworkPolicyManagerService.this.setNetworkTemplateEnabledInner(template, enabled);
                        return true;
                    case 19:
                        android.telephony.SubscriptionPlan[] plans = (android.telephony.SubscriptionPlan[]) msg.obj;
                        int subId2 = msg.arg1;
                        int length6 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i6 = 0; i6 < length6; i6++) {
                            android.net.INetworkPolicyListener listener6 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i6);
                            com.android.server.net.NetworkPolicyManagerService.this.dispatchSubscriptionPlansChanged(listener6, subId2, plans);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        return true;
                    case 20:
                        com.android.server.net.NetworkPolicyManagerService.this.mNetworkStats.forceUpdate();
                        synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                            com.android.server.net.NetworkPolicyManagerService.this.updateNetworkRulesNL();
                            com.android.server.net.NetworkPolicyManagerService.this.updateNetworkEnabledNL();
                            com.android.server.net.NetworkPolicyManagerService.this.updateNotificationsNL();
                            break;
                        }
                        return true;
                    case 21:
                        int uid3 = msg.arg1;
                        int newBlockedReasons = msg.arg2;
                        int oldBlockedReasons = ((java.lang.Integer) msg.obj).intValue();
                        int length7 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i7 = 0; i7 < length7; i7++) {
                            android.net.INetworkPolicyListener listener7 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i7);
                            com.android.server.net.NetworkPolicyManagerService.this.dispatchBlockedReasonChanged(listener7, uid3, oldBlockedReasons, newBlockedReasons);
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        return true;
                    case 22:
                        synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                                int subId3 = msg.arg1;
                                if (msg.arg2 == com.android.server.net.NetworkPolicyManagerService.this.mSetSubscriptionPlansIds.get(subId3)) {
                                    if (com.android.server.net.NetworkPolicyManagerService.LOGD) {
                                        android.util.Slog.d(com.android.server.net.NetworkPolicyManagerService.TAG, "Clearing expired subscription plans.");
                                    }
                                    com.android.server.net.NetworkPolicyManagerService.this.setSubscriptionPlansInternal(subId3, new android.telephony.SubscriptionPlan[0], 0L, (java.lang.String) msg.obj);
                                } else if (com.android.server.net.NetworkPolicyManagerService.LOGD) {
                                    android.util.Slog.d(com.android.server.net.NetworkPolicyManagerService.TAG, "Ignoring stale CLEAR_SUBSCRIPTION_PLANS.");
                                }
                                break;
                            }
                        }
                        return true;
                    case 23:
                        android.util.SparseArray<com.android.internal.os.SomeArgs> uidStateUpdates = (android.util.SparseArray) msg.obj;
                        int uidsSize = uidStateUpdates.size();
                        int listenersSize = com.android.server.net.NetworkPolicyManagerService.this.mListeners.beginBroadcast();
                        for (int i8 = 0; i8 < listenersSize; i8++) {
                            android.net.INetworkPolicyListener listener8 = com.android.server.net.NetworkPolicyManagerService.this.mListeners.getBroadcastItem(i8);
                            for (int uidIndex = 0; uidIndex < uidsSize; uidIndex++) {
                                int uid4 = uidStateUpdates.keyAt(uidIndex);
                                com.android.internal.os.SomeArgs someArgs = uidStateUpdates.valueAt(uidIndex);
                                int oldBlockedReasons2 = someArgs.argi1;
                                int newBlockedReasons2 = someArgs.argi2;
                                int uidRules2 = someArgs.argi3;
                                com.android.server.net.NetworkPolicyManagerService.this.dispatchBlockedReasonChanged(listener8, uid4, oldBlockedReasons2, newBlockedReasons2);
                                if (com.android.server.net.NetworkPolicyManagerService.LOGV) {
                                    android.util.Slog.v(com.android.server.net.NetworkPolicyManagerService.TAG, "Dispatching rules=" + android.net.NetworkPolicyManager.uidRulesToString(uidRules2) + " for uid=" + uid4);
                                }
                                com.android.server.net.NetworkPolicyManagerService.this.dispatchUidRulesChanged(listener8, uid4, uidRules2);
                            }
                        }
                        com.android.server.net.NetworkPolicyManagerService.this.mListeners.finishBroadcast();
                        for (int uidIndex2 = 0; uidIndex2 < uidsSize; uidIndex2++) {
                            uidStateUpdates.valueAt(uidIndex2).recycle();
                        }
                        return true;
                    case 24:
                        long now = android.os.SystemClock.uptimeMillis();
                        long nextCheckTime = Long.MAX_VALUE;
                        synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                            for (int i9 = com.android.server.net.NetworkPolicyManagerService.this.mBackgroundTransitioningUids.size() - 1; i9 >= 0; i9--) {
                                long completionTimeMs = com.android.server.net.NetworkPolicyManagerService.this.mBackgroundTransitioningUids.valueAt(i9);
                                if (completionTimeMs > now) {
                                    nextCheckTime = java.lang.Math.min(nextCheckTime, completionTimeMs);
                                } else {
                                    int uid5 = com.android.server.net.NetworkPolicyManagerService.this.mBackgroundTransitioningUids.keyAt(i9);
                                    com.android.server.net.NetworkPolicyManagerService.this.mBackgroundTransitioningUids.removeAt(i9);
                                    com.android.server.net.NetworkPolicyManagerService.this.updateRuleForBackgroundUL(uid5);
                                    com.android.server.net.NetworkPolicyManagerService.this.updateRulesForPowerRestrictionsUL(uid5, false);
                                }
                            }
                            com.android.server.net.NetworkPolicyManagerService.this.mNextProcessBackgroundUidsTime = nextCheckTime;
                            if (nextCheckTime < Long.MAX_VALUE) {
                                com.android.server.net.NetworkPolicyManagerService.this.mHandler.sendEmptyMessageAtTime(24, nextCheckTime);
                            }
                            break;
                        }
                        return true;
                }
            }
        };
        this.mUidEventHandlerCallback = new android.os.Handler.Callback() { // from class: com.android.server.net.NetworkPolicyManagerService.17
            @Override // android.os.Handler.Callback
            public boolean handleMessage(android.os.Message msg) {
                int uid = msg.arg1;
                switch (msg.what) {
                    case 100:
                        com.android.server.net.NetworkPolicyManagerService.this.handleUidChanged(uid);
                        break;
                    case 101:
                        com.android.server.net.NetworkPolicyManagerService.this.handleUidGone(uid);
                        break;
                }
                return true;
            }
        };
        this.mWrapper = new com.android.server.net.NetworkPolicyManagerService.NetworkPolicyManagerServiceWrapper();
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context, "missing context");
        this.mActivityManager = (android.app.IActivityManager) java.util.Objects.requireNonNull(iActivityManager, "missing activityManager");
        this.mNetworkManager = (android.os.INetworkManagementService) java.util.Objects.requireNonNull(iNetworkManagementService, "missing networkManagement");
        this.mPowerExemptionManager = (android.os.PowerExemptionManager) this.mContext.getSystemService(android.os.PowerExemptionManager.class);
        this.mClock = (java.time.Clock) java.util.Objects.requireNonNull(clock, "missing Clock");
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService("user");
        this.mCarrierConfigManager = (android.telephony.CarrierConfigManager) this.mContext.getSystemService(android.telephony.CarrierConfigManager.class);
        this.mIPm = iPackageManager;
        android.os.HandlerThread handlerThread = new android.os.HandlerThread(TAG);
        handlerThread.start();
        this.mHandler = new android.os.Handler(handlerThread.getLooper(), this.mHandlerCallback);
        this.mUidEventThread = new com.android.server.ServiceThread("NetworkPolicy.uid", -2, false);
        this.mUidEventThread.start();
        this.mUidEventHandler = new android.os.Handler(this.mUidEventThread.getLooper(), this.mUidEventHandlerCallback);
        this.mSuppressDefaultPolicy = z;
        this.mDeps = (com.android.server.net.NetworkPolicyManagerService.Dependencies) java.util.Objects.requireNonNull(dependencies, "missing Dependencies");
        this.mActiveDataSubIdListener = new com.android.server.net.NetworkPolicyManagerService.ActiveDataSubIdListener();
        this.mPolicyFile = new android.util.AtomicFile(new java.io.File(file, "netpolicy.xml"), "net-policy");
        this.mAppOps = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mNetworkStats = (android.app.usage.NetworkStatsManager) context.getSystemService(android.app.usage.NetworkStatsManager.class);
        this.mMultipathPolicyTracker = new com.android.server.connectivity.MultipathPolicyTracker(this.mContext, this.mHandler);
        com.android.server.LocalServices.addService(com.android.server.net.NetworkPolicyManagerInternal.class, new com.android.server.net.NetworkPolicyManagerService.NetworkPolicyManagerInternalImpl());
    }

    public void bindConnectivityManager() {
        this.mConnManager = (android.net.ConnectivityManager) java.util.Objects.requireNonNull((android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class), "missing ConnectivityManager");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePowerSaveAllowlistUL() {
        int[] allowlist = this.mPowerExemptionManager.getAllowListedAppIds(false);
        this.mPowerSaveWhitelistExceptIdleAppIds.clear();
        for (int uid : allowlist) {
            this.mPowerSaveWhitelistExceptIdleAppIds.put(uid, true);
        }
        int[] allowlist2 = this.mPowerExemptionManager.getAllowListedAppIds(true);
        this.mPowerSaveWhitelistAppIds.clear();
        for (int uid2 : allowlist2) {
            this.mPowerSaveWhitelistAppIds.put(uid2, true);
        }
    }

    boolean addDefaultRestrictBackgroundAllowlistUidsUL() {
        java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
        int numberUsers = users.size();
        boolean changed = false;
        for (int i = 0; i < numberUsers; i++) {
            android.content.pm.UserInfo user = users.get(i);
            changed = addDefaultRestrictBackgroundAllowlistUidsUL(user.id) || changed;
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean addDefaultRestrictBackgroundAllowlistUidsUL(int userId) {
        com.android.server.SystemConfig sysConfig = com.android.server.SystemConfig.getInstance();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.util.ArraySet<java.lang.String> allowDataUsage = sysConfig.getAllowInDataUsageSave();
        boolean changed = false;
        for (int i = 0; i < allowDataUsage.size(); i++) {
            java.lang.String pkg = allowDataUsage.valueAt(i);
            if (LOGD) {
                android.util.Slog.d(TAG, "checking restricted background exemption for package " + pkg + " and user " + userId);
            }
            try {
                android.content.pm.ApplicationInfo app = pm.getApplicationInfoAsUser(pkg, 1048576, userId);
                if (!app.isPrivilegedApp()) {
                    android.util.Slog.e(TAG, "addDefaultRestrictBackgroundAllowlistUidsUL(): skipping non-privileged app  " + pkg);
                } else {
                    int uid = android.os.UserHandle.getUid(userId, app.uid);
                    this.mDefaultRestrictBackgroundAllowlistUids.append(uid, true);
                    if (LOGD) {
                        android.util.Slog.d(TAG, "Adding uid " + uid + " (user " + userId + ") to default restricted background allowlist. Revoked status: " + this.mRestrictBackgroundAllowlistRevokedUids.get(uid));
                    }
                    if (!this.mRestrictBackgroundAllowlistRevokedUids.get(uid)) {
                        if (LOGD) {
                            android.util.Slog.d(TAG, "adding default package " + pkg + " (uid " + uid + " for user " + userId + ") to restrict background allowlist");
                        }
                        setUidPolicyUncheckedUL(uid, 4, false);
                    }
                    changed = this.mOplusNPMS.addThirdPartyRestrictBGWhitelistUidsUL(userId, this.mDefaultRestrictBackgroundAllowlistUids, this.mRestrictBackgroundAllowlistRevokedUids);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                if (LOGD) {
                    android.util.Slog.d(TAG, "No ApplicationInfo for package " + pkg);
                }
            }
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: initService, reason: merged with bridge method [inline-methods] */
    public void lambda$networkScoreAndNetworkManagementServiceReady$1(java.util.concurrent.CountDownLatch initCompleteSignal) {
        android.os.Trace.traceBegin(2097152L, "systemReady");
        int oldPriority = android.os.Process.getThreadPriority(android.os.Process.myTid());
        try {
            android.os.Process.setThreadPriority(-2);
            if (isBandwidthControlEnabled()) {
                this.mUsageStats = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
                this.mAppStandby = (com.android.server.usage.AppStandbyInternal) com.android.server.LocalServices.getService(com.android.server.usage.AppStandbyInternal.class);
                this.mOplusNPMS = (com.android.server.net.IOplusNetworkPolicyManagerServiceEx) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.net.IOplusNetworkPolicyManagerServiceEx.DEFAULT, new java.lang.Object[]{this.mContext, this, this.mHandler.getLooper()});
                this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
                this.mUseMeteredFirewallChains = com.android.server.net.Flags.useMeteredFirewallChains();
                this.mUseDifferentDelaysForBackgroundChain = com.android.server.net.Flags.useDifferentDelaysForBackgroundChain();
                synchronized (this.mUidRulesFirstLock) {
                    synchronized (this.mNetworkPoliciesSecondLock) {
                        updatePowerSaveAllowlistUL();
                        this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
                        this.mPowerManagerInternal.registerLowPowerModeObserver(new android.os.PowerManagerInternal.LowPowerModeListener() { // from class: com.android.server.net.NetworkPolicyManagerService.1
                            public int getServiceType() {
                                return 6;
                            }

                            public void onLowPowerModeChanged(android.os.PowerSaveState result) {
                                boolean enabled = result.batterySaverEnabled;
                                if (com.android.server.net.NetworkPolicyManagerService.LOGD) {
                                    android.util.Slog.d(com.android.server.net.NetworkPolicyManagerService.TAG, "onLowPowerModeChanged(" + enabled + ")");
                                }
                                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                                    if (com.android.server.net.NetworkPolicyManagerService.this.mRestrictPower != enabled) {
                                        com.android.server.net.NetworkPolicyManagerService.this.mRestrictPower = enabled;
                                        com.android.server.net.NetworkPolicyManagerService.this.updateRulesForRestrictPowerUL();
                                    }
                                }
                            }
                        });
                        this.mRestrictPower = this.mPowerManagerInternal.getLowPowerState(6).batterySaverEnabled;
                        this.mRestrictedModeObserver = new com.android.server.net.NetworkPolicyManagerService.RestrictedModeObserver(this.mContext, new com.android.server.net.NetworkPolicyManagerService.RestrictedModeObserver.RestrictedModeListener() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda2
                            @Override // com.android.server.net.NetworkPolicyManagerService.RestrictedModeObserver.RestrictedModeListener
                            public final void onChange(boolean z) {
                                this.f$0.lambda$initService$0(z);
                            }
                        });
                        this.mRestrictedNetworkingMode = this.mRestrictedModeObserver.isRestrictedModeEnabled();
                        this.mSystemReady = true;
                        waitForAdminData();
                        readPolicyAL();
                        this.mRestrictBackgroundBeforeBsm = this.mLoadedRestrictBackground;
                        this.mRestrictBackgroundLowPowerMode = this.mPowerManagerInternal.getLowPowerState(10).batterySaverEnabled;
                        if (this.mRestrictBackgroundLowPowerMode && !this.mLoadedRestrictBackground) {
                            this.mLoadedRestrictBackground = true;
                        }
                        this.mPowerManagerInternal.registerLowPowerModeObserver(new android.os.PowerManagerInternal.LowPowerModeListener() { // from class: com.android.server.net.NetworkPolicyManagerService.2
                            public int getServiceType() {
                                return 10;
                            }

                            public void onLowPowerModeChanged(android.os.PowerSaveState result) {
                                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                                    com.android.server.net.NetworkPolicyManagerService.this.updateRestrictBackgroundByLowPowerModeUL(result);
                                }
                            }
                        });
                        if (addDefaultRestrictBackgroundAllowlistUidsUL()) {
                            writePolicyAL();
                        }
                        this.mBackgroundNetworkRestricted = com.android.server.net.Flags.networkBlockedForTopSleepingAndAbove();
                        if (this.mBackgroundNetworkRestricted) {
                            enableFirewallChainUL(6, true);
                        }
                        setRestrictBackgroundUL(this.mLoadedRestrictBackground, "init_service");
                        updateRulesForGlobalChangeAL(false);
                        this.mOplusNPMS.googleRestrictInit(this.mContext, this.mHandler, this.mIPm, this.mUidRulesFirstLock);
                        updateNotificationsNL();
                    }
                }
                try {
                    int cutpoint = this.mBackgroundNetworkRestricted ? -1 : 5;
                    this.mActivityManagerInternal.registerNetworkPolicyUidObserver(this.mUidObserver, 35, cutpoint, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                    this.mNetworkManager.registerObserver(this.mAlertObserver);
                } catch (android.os.RemoteException e) {
                }
                android.content.IntentFilter allowlistFilter = new android.content.IntentFilter("android.os.action.POWER_SAVE_WHITELIST_CHANGED");
                this.mContext.registerReceiver(this.mPowerSaveAllowlistReceiver, allowlistFilter, null, this.mHandler);
                android.content.IntentFilter connFilter = new android.content.IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                this.mContext.registerReceiver(this.mConnReceiver, connFilter, "android.permission.NETWORK_STACK", this.mHandler);
                android.content.IntentFilter packageFilter = new android.content.IntentFilter();
                packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
                packageFilter.addDataScheme("package");
                this.mContext.registerReceiverForAllUsers(this.mPackageReceiver, packageFilter, null, this.mHandler);
                this.mContext.registerReceiverForAllUsers(this.mUidRemovedReceiver, new android.content.IntentFilter("android.intent.action.UID_REMOVED"), null, this.mHandler);
                android.content.IntentFilter userFilter = new android.content.IntentFilter();
                userFilter.addAction("android.intent.action.USER_ADDED");
                userFilter.addAction("android.intent.action.USER_REMOVED");
                this.mContext.registerReceiver(this.mUserReceiver, userFilter, null, this.mHandler);
                java.util.concurrent.Executor executor = new android.os.HandlerExecutor(this.mHandler);
                this.mNetworkStats.registerUsageCallback(new android.net.NetworkTemplate.Builder(1).build(), 0L, executor, this.mStatsCallback);
                this.mNetworkStats.registerUsageCallback(new android.net.NetworkTemplate.Builder(4).build(), 0L, executor, this.mStatsCallback);
                this.mContext.registerReceiver(this.mSnoozeReceiver, new android.content.IntentFilter(ACTION_SNOOZE_WARNING), "android.permission.MANAGE_NETWORK_POLICY", this.mHandler);
                this.mContext.registerReceiver(this.mSnoozeReceiver, new android.content.IntentFilter(ACTION_SNOOZE_RAPID), "android.permission.MANAGE_NETWORK_POLICY", this.mHandler);
                android.content.IntentFilter wifiFilter = new android.content.IntentFilter("android.net.wifi.CONFIGURED_NETWORKS_CHANGE");
                this.mContext.registerReceiver(this.mWifiReceiver, wifiFilter, null, this.mHandler);
                android.content.IntentFilter carrierConfigFilter = new android.content.IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED");
                this.mContext.registerReceiver(this.mCarrierConfigReceiver, carrierConfigFilter, null, this.mHandler);
                android.content.IntentFilter simStateChangedFilter = new android.content.IntentFilter("android.intent.action.SIM_STATE_CHANGED");
                this.mContext.registerReceiver(this.mSimStateChangedReceiver, simStateChangedFilter, null, this.mHandler);
                this.mConnManager.registerNetworkCallback(new android.net.NetworkRequest.Builder().build(), this.mNetworkCallback);
                this.mAppStandby.addListener(new com.android.server.net.NetworkPolicyManagerService.NetPolicyAppIdleStateChangeListener());
                synchronized (this.mUidRulesFirstLock) {
                    updateRulesForAppIdleParoleUL();
                }
                ((android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class)).addOnSubscriptionsChangedListener(new android.os.HandlerExecutor(this.mHandler), new android.telephony.SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.net.NetworkPolicyManagerService.3
                    @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
                    public void onSubscriptionsChanged() {
                        android.util.Slog.v(com.android.server.net.NetworkPolicyManagerService.TAG, "onSubscriptionsChanged");
                        com.android.server.net.NetworkPolicyManagerService.this.updateNetworksInternal();
                    }
                });
                ((android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class)).registerTelephonyCallback(executor, this.mActiveDataSubIdListener);
                if (this.mOplusNPMS != null) {
                    this.mOplusNPMS.systemReady();
                }
                initCompleteSignal.countDown();
                return;
            }
            android.util.Slog.w(TAG, "bandwidth controls disabled, unable to enforce policy");
        } finally {
            android.os.Process.setThreadPriority(oldPriority);
            android.os.Trace.traceEnd(2097152L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initService$0(boolean enabled) {
        synchronized (this.mUidRulesFirstLock) {
            this.mRestrictedNetworkingMode = enabled;
            updateRestrictedModeAllowlistUL();
        }
    }

    public java.util.concurrent.CountDownLatch networkScoreAndNetworkManagementServiceReady() {
        this.mNetworkManagerReady = true;
        final java.util.concurrent.CountDownLatch initCompleteSignal = new java.util.concurrent.CountDownLatch(1);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$networkScoreAndNetworkManagementServiceReady$1(initCompleteSignal);
            }
        });
        return initCompleteSignal;
    }

    public void systemReady(java.util.concurrent.CountDownLatch initCompleteSignal) {
        try {
            if (!initCompleteSignal.await(30L, java.util.concurrent.TimeUnit.SECONDS)) {
                throw new java.lang.IllegalStateException("Service NetworkPolicy init timeout");
            }
            this.mMultipathPolicyTracker.start();
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.IllegalStateException("Service NetworkPolicy init interrupted", e);
        }
    }

    private static final class UidStateCallbackInfo {
        public int capability;
        public boolean isPending;
        public int procState;
        public long procStateSeq;
        public int uid;

        private UidStateCallbackInfo() {
            this.procState = 20;
            this.procStateSeq = -1L;
        }

        public void update(int uid, int procState, long procStateSeq, int capability) {
            this.uid = uid;
            this.procState = procState;
            this.procStateSeq = procStateSeq;
            this.capability = capability;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("{");
            sb.append("uid=").append(this.uid).append(",");
            sb.append("proc_state=").append(android.app.ActivityManager.procStateToString(this.procState)).append(",");
            sb.append("seq=").append(this.procStateSeq).append(",");
            sb.append("cap=");
            android.app.ActivityManager.printCapabilitiesSummary(sb, this.capability);
            sb.append(",");
            sb.append("pending=").append(this.isPending);
            sb.append("}");
            return sb.toString();
        }
    }

    private class ActiveDataSubIdListener extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener {
        private int mActiveDataSubId;
        private int mDefaultDataSubId;

        private ActiveDataSubIdListener() {
            this.mDefaultDataSubId = com.android.server.net.NetworkPolicyManagerService.this.mDeps.getDefaultDataSubId();
            this.mActiveDataSubId = com.android.server.net.NetworkPolicyManagerService.this.mDeps.getActivateDataSubId();
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int subId) {
            this.mActiveDataSubId = subId;
            this.mDefaultDataSubId = com.android.server.net.NetworkPolicyManagerService.this.mDeps.getDefaultDataSubId();
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                com.android.server.net.NetworkPolicyManagerService.this.updateNotificationsNL();
            }
        }
    }

    private class StatsCallback extends android.app.usage.NetworkStatsManager.UsageCallback {
        private boolean mIsAnyCallbackReceived;

        private StatsCallback() {
            this.mIsAnyCallbackReceived = false;
        }

        @Override // android.app.usage.NetworkStatsManager.UsageCallback
        public void onThresholdReached(int networkType, java.lang.String subscriberId) {
            this.mIsAnyCallbackReceived = true;
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                com.android.server.net.NetworkPolicyManagerService.this.updateNetworkRulesNL();
                com.android.server.net.NetworkPolicyManagerService.this.updateNetworkEnabledNL();
                com.android.server.net.NetworkPolicyManagerService.this.updateNotificationsNL();
            }
        }

        public boolean isAnyCallbackReceived() {
            return this.mIsAnyCallbackReceived;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean updateCapabilityChange(android.util.SparseBooleanArray lastValues, boolean newValue, android.net.Network network) {
        boolean lastValue = lastValues.get(network.getNetId(), false);
        boolean changed = lastValue != newValue || lastValues.indexOfKey(network.getNetId()) < 0;
        if (changed) {
            lastValues.put(network.getNetId(), newValue);
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateNetworkToIfacesNL(int netId, android.util.ArraySet<java.lang.String> newIfaces) {
        android.util.ArraySet<java.lang.String> lastIfaces = this.mNetworkToIfaces.get(netId);
        boolean changed = true;
        if (lastIfaces != null && lastIfaces.equals(newIfaces)) {
            changed = false;
        }
        if (changed) {
            this.mNetworkToIfaces.remove(netId);
            for (java.lang.String iface : newIfaces) {
                this.mNetworkToIfaces.add(netId, iface);
            }
        }
        return changed;
    }

    void updateNotificationsNL() {
        android.util.Slog.v(TAG, "updateNotificationsNL not process");
    }

    private android.content.pm.ApplicationInfo findRapidBlame(android.net.NetworkTemplate template, long start, long end) {
        java.lang.String[] packageNames;
        if (!this.mStatsCallback.isAnyCallbackReceived()) {
            return null;
        }
        java.util.List<android.app.usage.NetworkStats.Bucket> stats = this.mDeps.getNetworkUidBytes(template, start, end);
        long maxBytes = 0;
        long totalBytes = 0;
        int maxUid = 0;
        for (android.app.usage.NetworkStats.Bucket entry : stats) {
            long bytes = entry.getRxBytes() + entry.getTxBytes();
            totalBytes += bytes;
            if (bytes > maxBytes) {
                maxBytes = bytes;
                maxUid = entry.getUid();
            }
        }
        if (maxBytes > 0 && maxBytes > totalBytes / 2 && (packageNames = this.mContext.getPackageManager().getPackagesForUid(maxUid)) != null && packageNames.length == 1) {
            try {
                return this.mContext.getPackageManager().getApplicationInfo(packageNames[0], 4989440);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        return null;
    }

    private int findRelevantSubIdNL(android.net.NetworkTemplate template) {
        for (int i = 0; i < this.mSubIdToSubscriberId.size(); i++) {
            int subId = this.mSubIdToSubscriberId.keyAt(i);
            java.lang.String subscriberId = this.mSubIdToSubscriberId.valueAt(i);
            android.net.NetworkIdentity probeIdent = new android.net.NetworkIdentity.Builder().setType(0).setSubscriberId(subscriberId).setMetered(true).setDefaultNetwork(true).setSubId(subId).build();
            if (template.matches(probeIdent)) {
                return subId;
            }
        }
        return -1;
    }

    private void notifyOverLimitNL(android.net.NetworkTemplate template) {
        if (!this.mOverLimitNotified.contains(template)) {
            this.mContext.startActivity(buildNetworkOverLimitIntent(this.mContext.getResources(), template));
            this.mOverLimitNotified.add(template);
        }
    }

    private void notifyUnderLimitNL(android.net.NetworkTemplate template) {
        this.mOverLimitNotified.remove(template);
    }

    private void enqueueNotification(android.net.NetworkPolicy policy, int type, long totalBytes, android.content.pm.ApplicationInfo rapidBlame) {
        java.lang.CharSequence title;
        java.lang.CharSequence body;
        java.lang.CharSequence title2;
        java.lang.CharSequence title3;
        com.android.server.net.NetworkPolicyManagerService.NotificationId notificationId = new com.android.server.net.NetworkPolicyManagerService.NotificationId(policy, type);
        android.app.Notification.Builder builder = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.NETWORK_ALERTS);
        builder.setOnlyAlertOnce(true);
        builder.setWhen(0L);
        builder.setColor(this.mContext.getColor(android.R.color.system_notification_accent_color));
        android.content.res.Resources res = this.mContext.getResources();
        switch (type) {
            case 34:
                title = res.getText(android.R.string.date_picker_decrement_day_button);
                body = res.getString(android.R.string.date_picker_day_typeface, android.text.format.Formatter.formatFileSize(this.mContext, totalBytes, 8));
                builder.setSmallIcon(android.R.drawable.stat_notify_error);
                android.content.Intent snoozeIntent = buildSnoozeWarningIntent(policy.template, this.mContext.getPackageName());
                builder.setDeleteIntent(android.app.PendingIntent.getBroadcast(this.mContext, 0, snoozeIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD));
                android.content.Intent viewIntent = buildViewDataUsageIntent(res, policy.template);
                setContentIntent(builder, viewIntent);
                break;
            case 35:
                switch (policy.template.getMatchRule()) {
                    case 1:
                    case 10:
                        title2 = res.getText(android.R.string.data_usage_warning_body);
                        break;
                    case 4:
                        title2 = res.getText(android.R.string.date_picker_decrement_year_button);
                        break;
                    default:
                        return;
                }
                body = res.getText(android.R.string.data_usage_rapid_title);
                builder.setOngoing(true);
                builder.setSmallIcon(android.R.drawable.search_dropdown_light);
                android.content.Intent intent = buildNetworkOverLimitIntent(res, policy.template);
                setContentIntent(builder, intent);
                title = title2;
                break;
            case 36:
                switch (policy.template.getMatchRule()) {
                    case 1:
                    case 10:
                        title3 = res.getText(android.R.string.data_usage_restricted_title);
                        break;
                    case 4:
                        title3 = res.getText(android.R.string.date_picker_decrement_month_button);
                        break;
                    default:
                        return;
                }
                long overBytes = totalBytes - policy.limitBytes;
                body = res.getString(android.R.string.data_usage_restricted_body, android.text.format.Formatter.formatFileSize(this.mContext, overBytes, 8));
                builder.setOngoing(true);
                builder.setSmallIcon(android.R.drawable.stat_notify_error);
                builder.setChannelId(com.android.internal.notification.SystemNotificationChannels.NETWORK_STATUS);
                android.content.Intent intent2 = buildViewDataUsageIntent(res, policy.template);
                setContentIntent(builder, intent2);
                title = title3;
                break;
            case 45:
                title = res.getText(android.R.string.data_usage_wifi_limit_title);
                if (rapidBlame != null) {
                    body = res.getString(android.R.string.data_usage_warning_title, rapidBlame.loadLabel(this.mContext.getPackageManager()));
                } else {
                    body = res.getString(android.R.string.data_usage_wifi_limit_snoozed_title);
                }
                builder.setSmallIcon(android.R.drawable.stat_notify_error);
                android.content.Intent snoozeIntent2 = buildSnoozeRapidIntent(policy.template, this.mContext.getPackageName());
                builder.setDeleteIntent(android.app.PendingIntent.getBroadcast(this.mContext, 0, snoozeIntent2, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD));
                android.content.Intent viewIntent2 = buildViewDataUsageIntent(res, policy.template);
                setContentIntent(builder, viewIntent2);
                break;
            default:
                return;
        }
        builder.setTicker(title);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setStyle(new android.app.Notification.BigTextStyle().bigText(body));
        ((android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class)).notifyAsUser(notificationId.getTag(), notificationId.getId(), builder.build(), android.os.UserHandle.ALL);
        this.mActiveNotifs.add(notificationId);
    }

    private void setContentIntent(android.app.Notification.Builder builder, android.content.Intent intent) {
        if (android.os.UserManager.isHeadlessSystemUserMode()) {
            builder.setContentIntent(android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, null, android.os.UserHandle.CURRENT));
        } else {
            builder.setContentIntent(android.app.PendingIntent.getActivity(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD));
        }
    }

    private void cancelNotification(com.android.server.net.NetworkPolicyManagerService.NotificationId notificationId) {
        ((android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class)).cancel(notificationId.getTag(), notificationId.getId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworksInternal() {
        updateSubscriptions();
        synchronized (this.mUidRulesFirstLock) {
            synchronized (this.mNetworkPoliciesSecondLock) {
                ensureActiveCarrierPolicyAL();
                normalizePoliciesNL();
                updateNetworkEnabledNL();
                updateNetworkRulesNL();
                updateNotificationsNL();
            }
        }
    }

    void updateNetworks() throws java.lang.InterruptedException {
        updateNetworksInternal();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                latch.countDown();
            }
        });
        latch.await(5L, java.util.concurrent.TimeUnit.SECONDS);
    }

    android.os.Handler getHandlerForTesting() {
        return this.mHandler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeUpdateCarrierPolicyCycleAL(int subId, java.lang.String subscriberId) {
        if (LOGV) {
            android.util.Slog.v(TAG, "maybeUpdateCarrierPolicyCycleAL()");
        }
        boolean policyUpdated = false;
        android.net.NetworkIdentity probeIdent = new android.net.NetworkIdentity.Builder().setType(0).setSubscriberId(subscriberId).setMetered(true).setDefaultNetwork(true).setSubId(subId).build();
        for (int i = this.mNetworkPolicy.size() - 1; i >= 0; i--) {
            android.net.NetworkTemplate template = this.mNetworkPolicy.keyAt(i);
            if (template.matches(probeIdent)) {
                android.net.NetworkPolicy policy = this.mNetworkPolicy.valueAt(i);
                policyUpdated |= updateDefaultCarrierPolicyAL(subId, policy);
            }
        }
        return policyUpdated;
    }

    int getCycleDayFromCarrierConfig(android.os.PersistableBundle config, int fallbackCycleDay) {
        int cycleDay;
        if (config == null || (cycleDay = config.getInt("monthly_data_cycle_day_int")) == -1) {
            return fallbackCycleDay;
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        if (cycleDay < cal.getMinimum(5) || cycleDay > cal.getMaximum(5)) {
            android.util.Slog.e(TAG, "Invalid date in CarrierConfigManager.KEY_MONTHLY_DATA_CYCLE_DAY_INT: " + cycleDay);
            return fallbackCycleDay;
        }
        return cycleDay;
    }

    long getWarningBytesFromCarrierConfig(android.os.PersistableBundle config, long fallbackWarningBytes) {
        if (config == null) {
            return fallbackWarningBytes;
        }
        long warningBytes = config.getLong("data_warning_threshold_bytes_long");
        if (warningBytes == -2) {
            return -1L;
        }
        if (warningBytes == -1) {
            return getPlatformDefaultWarningBytes();
        }
        if (warningBytes < 0) {
            android.util.Slog.e(TAG, "Invalid value in CarrierConfigManager.KEY_DATA_WARNING_THRESHOLD_BYTES_LONG; expected a non-negative value but got: " + warningBytes);
            return fallbackWarningBytes;
        }
        return warningBytes;
    }

    long getLimitBytesFromCarrierConfig(android.os.PersistableBundle config, long fallbackLimitBytes) {
        if (config == null) {
            return fallbackLimitBytes;
        }
        long limitBytes = config.getLong("data_limit_threshold_bytes_long");
        if (limitBytes == -2) {
            return -1L;
        }
        if (limitBytes == -1) {
            return getPlatformDefaultLimitBytes();
        }
        if (limitBytes < 0) {
            android.util.Slog.e(TAG, "Invalid value in CarrierConfigManager.KEY_DATA_LIMIT_THRESHOLD_BYTES_LONG; expected a non-negative value but got: " + limitBytes);
            return fallbackLimitBytes;
        }
        return limitBytes;
    }

    void handleNetworkPoliciesUpdateAL(boolean shouldNormalizePolicies) {
        if (shouldNormalizePolicies) {
            normalizePoliciesNL();
        }
        updateNetworkEnabledNL();
        updateNetworkRulesNL();
        updateNotificationsNL();
        writePolicyAL();
    }

    void updateNetworkEnabledNL() {
        android.util.Slog.v(TAG, "updateNetworkEnabledNL() return");
    }

    private void setNetworkTemplateEnabled(android.net.NetworkTemplate networkTemplate, boolean z) {
        this.mHandler.obtainMessage(18, z ? 1 : 0, 0, networkTemplate).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNetworkTemplateEnabledInner(android.net.NetworkTemplate template, boolean enabled) {
        if (template.getMatchRule() == 1 || template.getMatchRule() == 10) {
            android.util.IntArray matchingSubIds = new android.util.IntArray();
            synchronized (this.mNetworkPoliciesSecondLock) {
                for (int i = 0; i < this.mSubIdToSubscriberId.size(); i++) {
                    int subId = this.mSubIdToSubscriberId.keyAt(i);
                    java.lang.String subscriberId = this.mSubIdToSubscriberId.valueAt(i);
                    android.net.NetworkIdentity probeIdent = new android.net.NetworkIdentity.Builder().setType(0).setSubscriberId(subscriberId).setMetered(true).setDefaultNetwork(true).setSubId(subId).build();
                    if (template.matches(probeIdent)) {
                        matchingSubIds.add(subId);
                    }
                }
            }
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
            for (int i2 = 0; i2 < matchingSubIds.size(); i2++) {
                tm.createForSubscriptionId(matchingSubIds.get(i2)).setPolicyDataEnabled(enabled);
            }
        }
    }

    private static void collectIfaces(android.util.ArraySet<java.lang.String> ifaces, android.net.NetworkStateSnapshot snapshot) {
        ifaces.addAll(snapshot.getLinkProperties().getAllInterfaceNames());
    }

    void updateSubscriptions() {
        java.util.Iterator<android.telephony.SubscriptionInfo> it;
        if (LOGV) {
            android.util.Slog.v(TAG, "updateSubscriptions()");
        }
        android.os.Trace.traceBegin(2097152L, "updateSubscriptions");
        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        android.telephony.SubscriptionManager sm = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        java.util.List<android.telephony.SubscriptionInfo> subList = com.android.internal.util.CollectionUtils.emptyIfNull(sm.getActiveSubscriptionInfoList());
        java.util.List<java.lang.String[]> mergedSubscriberIdsList = new java.util.ArrayList<>();
        android.util.SparseArray<java.lang.String> subIdToSubscriberId = new android.util.SparseArray<>(subList.size());
        android.util.SparseArray<android.os.PersistableBundle> subIdToCarrierConfig = new android.util.SparseArray<>();
        java.util.Iterator<android.telephony.SubscriptionInfo> it2 = subList.iterator();
        while (it2.hasNext()) {
            android.telephony.SubscriptionInfo sub = it2.next();
            int subId = sub.getSubscriptionId();
            android.telephony.TelephonyManager tmSub = tm.createForSubscriptionId(subId);
            java.lang.String subscriberId = tmSub.getSubscriberId();
            if (!android.text.TextUtils.isEmpty(subscriberId)) {
                subIdToSubscriberId.put(tmSub.getSubscriptionId(), subscriberId);
            } else if (android.os.Build.isMtkPlatform()) {
                android.util.Slog.w(TAG, "Missing subscriberId for subId " + tmSub.getSubscriptionId());
            } else {
                android.util.Slog.wtf(TAG, "Missing subscriberId for subId " + tmSub.getSubscriptionId());
            }
            java.lang.String[] mergedSubscriberId = com.android.internal.util.ArrayUtils.defeatNullable(tmSub.getMergedImsisFromGroup());
            mergedSubscriberIdsList.add(mergedSubscriberId);
            android.os.PersistableBundle config = this.mCarrierConfigManager.getConfigForSubId(subId);
            if (config != null) {
                subIdToCarrierConfig.put(subId, config);
                it = it2;
            } else {
                it = it2;
                android.util.Slog.e(TAG, "Missing CarrierConfig for subId " + subId);
            }
            it2 = it;
        }
        synchronized (this.mNetworkPoliciesSecondLock) {
            this.mSubIdToSubscriberId.clear();
            for (int i = 0; i < subIdToSubscriberId.size(); i++) {
                this.mSubIdToSubscriberId.put(subIdToSubscriberId.keyAt(i), subIdToSubscriberId.valueAt(i));
            }
            this.mMergedSubscriberIds = mergedSubscriberIdsList;
            this.mSubIdToCarrierConfig.clear();
            for (int i2 = 0; i2 < subIdToCarrierConfig.size(); i2++) {
                this.mSubIdToCarrierConfig.put(subIdToCarrierConfig.keyAt(i2), subIdToCarrierConfig.valueAt(i2));
            }
        }
        android.os.Trace.traceEnd(2097152L);
    }

    void updateNetworkRulesNL() {
        int i;
        java.lang.String[] meteredIfaces;
        int subId;
        android.telephony.SubscriptionPlan plan;
        android.content.ContentResolver cr;
        int i2;
        int subId2;
        java.time.Instant now;
        java.util.Iterator<android.net.NetworkStateSnapshot> it;
        long quotaBytes;
        android.util.ArraySet<java.lang.String> newMeteredIfaces;
        android.net.NetworkPolicy policy;
        android.util.ArraySet<java.lang.String> newMeteredIfaces2;
        android.util.ArraySet<java.lang.String> newMeteredIfaces3;
        if (LOGV) {
            android.util.Slog.v(TAG, "updateNetworkRulesNL()");
        }
        android.os.Trace.traceBegin(2097152L, "updateNetworkRulesNL");
        java.util.List<android.net.NetworkStateSnapshot> snapshots = this.mConnManager.getAllNetworkStateSnapshots();
        this.mNetIdToSubId.clear();
        android.util.ArrayMap<android.net.NetworkStateSnapshot, android.net.NetworkIdentity> identified = new android.util.ArrayMap<>();
        java.util.Iterator<android.net.NetworkStateSnapshot> it2 = snapshots.iterator();
        while (true) {
            i = 1;
            if (!it2.hasNext()) {
                break;
            }
            android.net.NetworkStateSnapshot snapshot = it2.next();
            this.mNetIdToSubId.put(snapshot.getNetwork().getNetId(), snapshot.getSubId());
            android.net.NetworkIdentity ident = new android.net.NetworkIdentity.Builder().setNetworkStateSnapshot(snapshot).setDefaultNetwork(true).build();
            identified.put(snapshot, ident);
        }
        android.util.ArraySet<java.lang.String> newMeteredIfaces4 = new android.util.ArraySet<>();
        android.util.ArraySet<java.lang.String> matchingIfaces = new android.util.ArraySet<>();
        long lowestRule = Long.MAX_VALUE;
        int i3 = this.mNetworkPolicy.size() - 1;
        while (true) {
            if (i3 < 0) {
                break;
            }
            android.net.NetworkPolicy policy2 = this.mNetworkPolicy.valueAt(i3);
            matchingIfaces.clear();
            for (int j = identified.size() - i; j >= 0; j--) {
                if (policy2.template.matches(identified.valueAt(j))) {
                    collectIfaces(matchingIfaces, identified.keyAt(j));
                }
            }
            if (LOGD) {
                android.util.Slog.d(TAG, "Applying " + policy2 + " to ifaces " + matchingIfaces);
            }
            int i4 = policy2.warningBytes != -1 ? i : 0;
            int i5 = policy2.limitBytes != -1 ? i : 0;
            long limitBytes = Long.MAX_VALUE;
            long warningBytes = Long.MAX_VALUE;
            if ((i5 == 0 && i4 == 0) || !policy2.hasCycle()) {
                policy = policy2;
                newMeteredIfaces2 = newMeteredIfaces4;
            } else {
                android.util.Pair<java.time.ZonedDateTime, java.time.ZonedDateTime> cycle = (android.util.Pair) android.net.NetworkPolicyManager.cycleIterator(policy2).next();
                long start = ((java.time.ZonedDateTime) cycle.first).toInstant().toEpochMilli();
                long end = ((java.time.ZonedDateTime) cycle.second).toInstant().toEpochMilli();
                policy = policy2;
                newMeteredIfaces2 = newMeteredIfaces4;
                long totalBytes = getTotalBytes(policy2.template, start, end);
                if (i5 != 0 && policy.lastLimitSnooze < start) {
                    long limitBytes2 = java.lang.Math.max(1L, policy.limitBytes - totalBytes);
                    limitBytes = (limitBytes2 != 1 || needDisableMobileNetwork(findRelevantSubIdNL(policy.template))) ? limitBytes2 : Long.MAX_VALUE;
                }
                if (i4 != 0 && policy.lastWarningSnooze < start && !policy.isOverWarning(totalBytes)) {
                    warningBytes = java.lang.Math.max(1L, policy.warningBytes - totalBytes);
                }
            }
            if (i4 == 0 && i5 == 0 && !policy.metered) {
                newMeteredIfaces3 = newMeteredIfaces2;
            } else {
                if (matchingIfaces.size() > 1) {
                    android.util.Slog.w(TAG, "shared quota unsupported; generating rule for each iface");
                }
                for (int j2 = matchingIfaces.size() - 1; j2 >= 0; j2--) {
                    java.lang.String iface = matchingIfaces.valueAt(j2);
                    setInterfaceQuotasAsync(iface, warningBytes, limitBytes);
                    newMeteredIfaces2.add(iface);
                }
                newMeteredIfaces3 = newMeteredIfaces2;
            }
            if (i4 != 0 && policy.warningBytes < lowestRule) {
                lowestRule = policy.warningBytes;
            }
            if (i5 != 0 && policy.limitBytes < lowestRule) {
                lowestRule = policy.limitBytes;
            }
            i3--;
            newMeteredIfaces4 = newMeteredIfaces3;
            i = 1;
        }
        android.util.ArraySet<java.lang.String> newMeteredIfaces5 = newMeteredIfaces4;
        for (android.net.NetworkStateSnapshot snapshot2 : snapshots) {
            if (!snapshot2.getNetworkCapabilities().hasCapability(11)) {
                matchingIfaces.clear();
                collectIfaces(matchingIfaces, snapshot2);
                int j3 = matchingIfaces.size() - 1;
                while (j3 >= 0) {
                    java.lang.String iface2 = matchingIfaces.valueAt(j3);
                    if (newMeteredIfaces5.contains(iface2)) {
                        newMeteredIfaces = newMeteredIfaces5;
                    } else {
                        newMeteredIfaces = newMeteredIfaces5;
                        setInterfaceQuotasAsync(iface2, Long.MAX_VALUE, Long.MAX_VALUE);
                        newMeteredIfaces.add(iface2);
                    }
                    j3--;
                    newMeteredIfaces5 = newMeteredIfaces;
                }
            }
            newMeteredIfaces5 = newMeteredIfaces5;
        }
        android.util.ArraySet<java.lang.String> newMeteredIfaces6 = newMeteredIfaces5;
        synchronized (this.mMeteredIfacesLock) {
            for (int i6 = this.mMeteredIfaces.size() - 1; i6 >= 0; i6--) {
                java.lang.String iface3 = this.mMeteredIfaces.valueAt(i6);
                if (!newMeteredIfaces6.contains(iface3)) {
                    removeInterfaceQuotasAsync(iface3);
                }
            }
            this.mMeteredIfaces = newMeteredIfaces6;
        }
        android.content.ContentResolver cr2 = this.mContext.getContentResolver();
        int i7 = android.provider.Settings.Global.getInt(cr2, "netpolicy_quota_enabled", 1) != 0 ? 1 : 0;
        long quotaUnlimited = android.provider.Settings.Global.getLong(cr2, "netpolicy_quota_unlimited", QUOTA_UNLIMITED_DEFAULT);
        float quotaLimited = android.provider.Settings.Global.getFloat(cr2, "netpolicy_quota_limited", QUOTA_LIMITED_DEFAULT);
        this.mSubscriptionOpportunisticQuota.clear();
        java.util.Iterator<android.net.NetworkStateSnapshot> it3 = snapshots.iterator();
        while (it3.hasNext()) {
            android.net.NetworkStateSnapshot snapshot3 = it3.next();
            if (i7 != 0 && snapshot3.getNetwork() != null && (subId = getSubIdLocked(snapshot3.getNetwork())) != -1 && (plan = getPrimarySubscriptionPlanLocked(subId)) != null) {
                long limitBytes3 = plan.getDataLimitBytes();
                if (!snapshot3.getNetworkCapabilities().hasCapability(18)) {
                    quotaBytes = 0;
                    it = it3;
                    subId2 = subId;
                    cr = cr2;
                    i2 = i7;
                } else if (limitBytes3 == -1) {
                    quotaBytes = -1;
                    it = it3;
                    subId2 = subId;
                    cr = cr2;
                    i2 = i7;
                } else if (limitBytes3 == Long.MAX_VALUE) {
                    quotaBytes = quotaUnlimited;
                    it = it3;
                    subId2 = subId;
                    cr = cr2;
                    i2 = i7;
                } else {
                    android.util.Range<java.time.ZonedDateTime> cycle2 = plan.cycleIterator().next();
                    long start2 = ((java.time.ZonedDateTime) cycle2.getLower()).toInstant().toEpochMilli();
                    long end2 = ((java.time.ZonedDateTime) cycle2.getUpper()).toInstant().toEpochMilli();
                    java.time.Instant now2 = this.mClock.instant();
                    long startOfDay = java.time.ZonedDateTime.ofInstant(now2, ((java.time.ZonedDateTime) cycle2.getLower()).getZone()).truncatedTo(java.time.temporal.ChronoUnit.DAYS).toInstant().toEpochMilli();
                    java.lang.String subscriberId = snapshot3.getSubscriberId();
                    long totalBytes2 = 0;
                    if (subscriberId == null) {
                        subId2 = subId;
                        now = now2;
                        cr = cr2;
                        i2 = i7;
                    } else {
                        cr = cr2;
                        i2 = i7;
                        subId2 = subId;
                        now = now2;
                        totalBytes2 = getTotalBytes(buildTemplateCarrierMetered(subscriberId), start2, startOfDay);
                    }
                    long remainingBytes = limitBytes3 - totalBytes2;
                    it = it3;
                    long remainingDays = (((end2 - now.toEpochMilli()) - 1) / java.util.concurrent.TimeUnit.DAYS.toMillis(1L)) + 1;
                    quotaBytes = java.lang.Math.max(0L, (long) ((remainingBytes / remainingDays) * quotaLimited));
                }
                this.mSubscriptionOpportunisticQuota.put(subId2, quotaBytes);
                it3 = it;
                cr2 = cr;
                i7 = i2;
            }
        }
        synchronized (this.mMeteredIfacesLock) {
            meteredIfaces = (java.lang.String[]) this.mMeteredIfaces.toArray(new java.lang.String[this.mMeteredIfaces.size()]);
        }
        this.mHandler.obtainMessage(2, meteredIfaces).sendToTarget();
        this.mHandler.obtainMessage(7, java.lang.Long.valueOf(lowestRule)).sendToTarget();
        android.os.Trace.traceEnd(2097152L);
    }

    private void ensureActiveCarrierPolicyAL() throws java.lang.Throwable {
        if (LOGV) {
            android.util.Slog.v(TAG, "ensureActiveCarrierPolicyAL()");
        }
        if (this.mSuppressDefaultPolicy) {
            return;
        }
        for (int i = 0; i < this.mSubIdToSubscriberId.size(); i++) {
            int subId = this.mSubIdToSubscriberId.keyAt(i);
            java.lang.String subscriberId = this.mSubIdToSubscriberId.valueAt(i);
            ensureActiveCarrierPolicyAL(subId, subscriberId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r5v1, types: [java.time.ZonedDateTime] */
    public boolean ensureActiveCarrierPolicyAL(int subId, java.lang.String subscriberId) throws java.lang.Throwable {
        android.net.NetworkIdentity probeIdent = new android.net.NetworkIdentity.Builder().setType(0).setSubscriberId(subscriberId).setMetered(true).setDefaultNetwork(true).setSubId(subId).build();
        for (int i = this.mNetworkPolicy.size() - 1; i >= 0; i--) {
            android.net.NetworkTemplate template = this.mNetworkPolicy.keyAt(i);
            if (template.matches(probeIdent)) {
                if (LOGD) {
                    android.util.Slog.d(TAG, "Found template " + template + " which matches subscriber " + com.android.net.module.util.NetworkIdentityUtils.scrubSubscriberId(subscriberId));
                }
                return false;
            }
        }
        android.util.Slog.i(TAG, "No policy for subscriber " + com.android.net.module.util.NetworkIdentityUtils.scrubSubscriberId(subscriberId) + "; generating default policy");
        android.net.NetworkPolicy policy = buildDefaultCarrierPolicy(subId, subscriberId);
        java.time.Clock sClock = java.time.Clock.systemDefaultZone();
        java.time.ZoneId zone = java.time.ZoneId.systemDefault();
        java.time.ZonedDateTime start = java.time.ZonedDateTime.of(java.time.LocalDate.from((java.time.temporal.TemporalAccessor) java.time.ZonedDateTime.now(sClock).withZoneSameInstant(zone).withDayOfMonth(1).truncatedTo(java.time.temporal.ChronoUnit.DAYS)), java.time.LocalTime.MIDNIGHT, zone);
        policy.cycleRule = new android.util.RecurrenceRule(start, (java.time.ZonedDateTime) null, java.time.Period.ofMonths(1));
        addNetworkPolicyAL(policy);
        return true;
    }

    private long getPlatformDefaultWarningBytes() {
        int dataWarningConfig = this.mContext.getResources().getInteger(android.R.integer.config_metrics_pull_cooldown_millis);
        if (dataWarningConfig == -1) {
            return -1L;
        }
        return android.util.DataUnit.MEBIBYTES.toBytes(dataWarningConfig);
    }

    private long getPlatformDefaultLimitBytes() {
        return -1L;
    }

    android.net.NetworkPolicy buildDefaultCarrierPolicy(int subId, java.lang.String subscriberId) throws java.lang.Throwable {
        android.net.NetworkTemplate template = buildTemplateCarrierMetered(subscriberId);
        android.util.RecurrenceRule cycleRule = android.net.NetworkPolicy.buildRule(java.time.ZonedDateTime.now().getDayOfMonth(), java.time.ZoneId.systemDefault());
        android.net.NetworkPolicy policy = new android.net.NetworkPolicy(template, cycleRule, getPlatformDefaultWarningBytes(), getPlatformDefaultLimitBytes(), -1L, -1L, true, true);
        synchronized (this.mUidRulesFirstLock) {
            try {
                try {
                    synchronized (this.mNetworkPoliciesSecondLock) {
                        updateDefaultCarrierPolicyAL(subId, policy);
                    }
                    return policy;
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

    public static android.net.NetworkTemplate buildTemplateCarrierMetered(java.lang.String subscriberId) {
        java.util.Objects.requireNonNull(subscriberId);
        return new android.net.NetworkTemplate.Builder(10).setSubscriberIds(java.util.Set.of(subscriberId)).setMeteredness(1).build();
    }

    private boolean updateDefaultCarrierPolicyAL(int subId, android.net.NetworkPolicy policy) {
        int currentCycleDay;
        if (!policy.inferred) {
            if (LOGD) {
                android.util.Slog.d(TAG, "Ignoring user-defined policy " + policy);
            }
            return false;
        }
        android.net.NetworkPolicy original = new android.net.NetworkPolicy(policy.template, policy.cycleRule, policy.warningBytes, policy.limitBytes, policy.lastWarningSnooze, policy.lastLimitSnooze, policy.metered, policy.inferred);
        android.telephony.SubscriptionPlan[] plans = this.mSubscriptionPlans.get(subId);
        if (!com.android.internal.util.ArrayUtils.isEmpty(plans)) {
            android.telephony.SubscriptionPlan plan = plans[0];
            policy.cycleRule = plan.getCycleRule();
            long planLimitBytes = plan.getDataLimitBytes();
            if (planLimitBytes != -1) {
                if (planLimitBytes == Long.MAX_VALUE) {
                    policy.warningBytes = -1L;
                    policy.limitBytes = -1L;
                } else {
                    policy.warningBytes = (9 * planLimitBytes) / 10;
                    switch (plan.getDataLimitBehavior()) {
                        case 0:
                        case 1:
                            policy.limitBytes = planLimitBytes;
                            break;
                        default:
                            policy.limitBytes = -1L;
                            break;
                    }
                }
            } else {
                policy.warningBytes = getPlatformDefaultWarningBytes();
                policy.limitBytes = getPlatformDefaultLimitBytes();
            }
        } else {
            android.os.PersistableBundle config = this.mSubIdToCarrierConfig.get(subId);
            if (policy.cycleRule.isMonthly()) {
                currentCycleDay = policy.cycleRule.start.getDayOfMonth();
            } else {
                currentCycleDay = -1;
            }
            int cycleDay = getCycleDayFromCarrierConfig(config, currentCycleDay);
            policy.cycleRule = android.net.NetworkPolicy.buildRule(cycleDay, java.time.ZoneId.systemDefault());
            policy.warningBytes = getWarningBytesFromCarrierConfig(config, policy.warningBytes);
            policy.limitBytes = getLimitBytesFromCarrierConfig(config, policy.limitBytes);
        }
        if (policy.equals(original)) {
            return false;
        }
        android.util.Slog.d(TAG, "Updated " + original + " to " + policy);
        return true;
    }

    private void readPolicyAL() throws java.lang.Throwable {
        com.android.modules.utils.TypedXmlPullParser in;
        android.util.SparseBooleanArray restrictBackgroundAllowedUids;
        int version;
        boolean insideAllowlist;
        java.io.FileInputStream fis;
        int version2;
        int templateMeteredness;
        int subscriberIdMatchRule;
        android.util.SparseBooleanArray restrictBackgroundAllowedUids2;
        int type;
        android.util.RecurrenceRule cycleRule;
        boolean metered;
        if (LOGV) {
            android.util.Slog.v(TAG, "readPolicyAL()");
        }
        this.mNetworkPolicy.clear();
        this.mSubscriptionPlans.clear();
        this.mSubscriptionPlansOwner.clear();
        this.mUidPolicy.clear();
        java.io.FileInputStream fis2 = null;
        try {
            try {
                fis2 = this.mPolicyFile.openRead();
                try {
                    in = android.util.Xml.resolvePullParser(fis2);
                    restrictBackgroundAllowedUids = new android.util.SparseBooleanArray();
                    version = 1;
                    insideAllowlist = false;
                } catch (java.io.FileNotFoundException e) {
                } catch (java.lang.Exception e2) {
                    e = e2;
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } catch (java.io.FileNotFoundException e3) {
        } catch (java.lang.Exception e4) {
            e = e4;
        }
        while (true) {
            int type2 = in.next();
            boolean z = false;
            if (type2 == 1) {
                fis = fis2;
                int size = restrictBackgroundAllowedUids.size();
                for (int i = 0; i < size; i++) {
                    int uid = restrictBackgroundAllowedUids.keyAt(i);
                    int policy = this.mUidPolicy.get(uid, 0);
                    if ((policy & 1) != 0) {
                        android.util.Slog.w(TAG, "ignoring restrict-background-allowlist for " + uid + " because its policy is " + android.net.NetworkPolicyManager.uidPoliciesToString(policy));
                    } else if (android.os.UserHandle.isApp(uid) || isCloneUidNL(uid)) {
                        int newPolicy = policy | 4;
                        if (LOGV) {
                            android.util.Log.v(TAG, "new policy for " + uid + ": " + android.net.NetworkPolicyManager.uidPoliciesToString(newPolicy));
                        }
                        setUidPolicyUncheckedUL(uid, newPolicy, false);
                    } else {
                        android.util.Slog.w(TAG, "unable to update policy on UID " + uid);
                    }
                }
                libcore.io.IoUtils.closeQuietly(fis);
                return;
            }
            java.lang.String tag = in.getName();
            if (type2 != 2) {
                fis = fis2;
                version2 = version;
                if (type2 == 3 && TAG_ALLOWLIST.equals(tag)) {
                    insideAllowlist = false;
                    version = version2;
                } else {
                    version = version2;
                }
            } else if (TAG_POLICY_LIST.equals(tag)) {
                boolean z2 = this.mRestrictBackground;
                version = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_VERSION);
                if (version >= 3 && com.android.internal.util.XmlUtils.readBooleanAttribute(in, ATTR_RESTRICT_BACKGROUND)) {
                    z = true;
                }
                this.mLoadedRestrictBackground = z;
                fis = fis2;
            } else {
                if (TAG_NETWORK_POLICY.equals(tag)) {
                    int templateType = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_NETWORK_TEMPLATE);
                    java.lang.String subscriberId = in.getAttributeValue((java.lang.String) null, ATTR_SUBSCRIBER_ID);
                    java.lang.String networkId = version >= 9 ? in.getAttributeValue((java.lang.String) null, ATTR_NETWORK_ID) : null;
                    if (version >= 13) {
                        subscriberIdMatchRule = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_SUBSCRIBER_ID_MATCH_RULE);
                        int tMetered = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_TEMPLATE_METERED);
                        templateMeteredness = (tMetered == -1 && templateType == 10) ? 1 : tMetered;
                    } else if (templateType == 1) {
                        android.util.Log.d(TAG, "Update template match rule from mobile to carrier and force to metered");
                        templateType = 10;
                        templateMeteredness = 1;
                        subscriberIdMatchRule = 0;
                    } else {
                        templateMeteredness = -1;
                        subscriberIdMatchRule = 0;
                    }
                    if (version >= 11) {
                        java.lang.String start = com.android.internal.util.XmlUtils.readStringAttribute(in, ATTR_CYCLE_START);
                        java.lang.String end = com.android.internal.util.XmlUtils.readStringAttribute(in, ATTR_CYCLE_END);
                        fis = fis2;
                        try {
                            java.lang.String period = com.android.internal.util.XmlUtils.readStringAttribute(in, ATTR_CYCLE_PERIOD);
                            type = type2;
                            restrictBackgroundAllowedUids2 = restrictBackgroundAllowedUids;
                            cycleRule = new android.util.RecurrenceRule(android.util.RecurrenceRule.convertZonedDateTime(start), android.util.RecurrenceRule.convertZonedDateTime(end), android.util.RecurrenceRule.convertPeriod(period));
                        } catch (java.io.FileNotFoundException e5) {
                            fis2 = fis;
                        } catch (java.lang.Exception e6) {
                            e = e6;
                            fis2 = fis;
                            android.util.Log.wtf(TAG, "problem reading network policy", e);
                            libcore.io.IoUtils.closeQuietly(fis2);
                            return;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            fis2 = fis;
                            libcore.io.IoUtils.closeQuietly(fis2);
                            throw th;
                        }
                    } else {
                        fis = fis2;
                        restrictBackgroundAllowedUids2 = restrictBackgroundAllowedUids;
                        type = type2;
                        int cycleDay = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_CYCLE_DAY);
                        java.lang.String cycleTimezone = version >= 6 ? in.getAttributeValue((java.lang.String) null, ATTR_CYCLE_TIMEZONE) : "UTC";
                        cycleRule = android.net.NetworkPolicy.buildRule(cycleDay, java.time.ZoneId.of(cycleTimezone));
                    }
                    long warningBytes = com.android.internal.util.XmlUtils.readLongAttribute(in, ATTR_WARNING_BYTES);
                    long limitBytes = com.android.internal.util.XmlUtils.readLongAttribute(in, ATTR_LIMIT_BYTES);
                    long lastLimitSnooze = version >= 5 ? com.android.internal.util.XmlUtils.readLongAttribute(in, ATTR_LAST_LIMIT_SNOOZE) : version >= 2 ? com.android.internal.util.XmlUtils.readLongAttribute(in, ATTR_LAST_SNOOZE) : -1L;
                    if (version < 4) {
                        switch (templateType) {
                            case 1:
                                metered = true;
                                break;
                            default:
                                metered = false;
                                break;
                        }
                    } else {
                        metered = com.android.internal.util.XmlUtils.readBooleanAttribute(in, ATTR_METERED);
                    }
                    long lastWarningSnooze = version >= 5 ? com.android.internal.util.XmlUtils.readLongAttribute(in, ATTR_LAST_WARNING_SNOOZE) : -1L;
                    boolean inferred = version >= 7 ? com.android.internal.util.XmlUtils.readBooleanAttribute(in, ATTR_INFERRED) : false;
                    android.net.NetworkTemplate.Builder builder = new android.net.NetworkTemplate.Builder(templateType).setMeteredness(templateMeteredness);
                    if (subscriberIdMatchRule == 0) {
                        android.util.ArraySet<java.lang.String> ids = new android.util.ArraySet<>();
                        version2 = version;
                        ids.add(subscriberId);
                        builder.setSubscriberIds(ids);
                    } else {
                        version2 = version;
                    }
                    if (networkId != null) {
                        builder.setWifiNetworkKeys(java.util.Set.of(networkId));
                    }
                    android.net.NetworkTemplate template = builder.build();
                    if (android.net.NetworkPolicy.isTemplatePersistable(template)) {
                        this.mNetworkPolicy.put(template, new android.net.NetworkPolicy(template, cycleRule, warningBytes, limitBytes, lastWarningSnooze, lastLimitSnooze, metered, inferred));
                    }
                    restrictBackgroundAllowedUids = restrictBackgroundAllowedUids2;
                } else {
                    fis = fis2;
                    android.util.SparseBooleanArray restrictBackgroundAllowedUids3 = restrictBackgroundAllowedUids;
                    version2 = version;
                    if (TAG_UID_POLICY.equals(tag)) {
                        int uid2 = com.android.internal.util.XmlUtils.readIntAttribute(in, "uid");
                        int policy2 = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_POLICY);
                        if (android.os.UserHandle.isApp(uid2) || isCloneUidNL(uid2)) {
                            setUidPolicyUncheckedUL(uid2, policy2, false);
                        } else {
                            android.util.Slog.w(TAG, "unable to apply policy to UID " + uid2 + "; ignoring");
                        }
                        restrictBackgroundAllowedUids = restrictBackgroundAllowedUids3;
                    } else if (TAG_APP_POLICY.equals(tag)) {
                        int appId = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_APP_ID);
                        int policy3 = com.android.internal.util.XmlUtils.readIntAttribute(in, ATTR_POLICY);
                        int uid3 = android.os.UserHandle.getUid(0, appId);
                        if (android.os.UserHandle.isApp(uid3) || isCloneUidNL(uid3)) {
                            setUidPolicyUncheckedUL(uid3, policy3, false);
                        } else {
                            android.util.Slog.w(TAG, "unable to apply policy to UID " + uid3 + "; ignoring");
                        }
                        restrictBackgroundAllowedUids = restrictBackgroundAllowedUids3;
                    } else if (TAG_ALLOWLIST.equals(tag)) {
                        insideAllowlist = true;
                        restrictBackgroundAllowedUids = restrictBackgroundAllowedUids3;
                        version = version2;
                    } else if (TAG_RESTRICT_BACKGROUND.equals(tag) && insideAllowlist) {
                        restrictBackgroundAllowedUids = restrictBackgroundAllowedUids3;
                        restrictBackgroundAllowedUids.append(com.android.internal.util.XmlUtils.readIntAttribute(in, "uid"), true);
                    } else {
                        restrictBackgroundAllowedUids = restrictBackgroundAllowedUids3;
                        if (TAG_REVOKED_RESTRICT_BACKGROUND.equals(tag) && insideAllowlist) {
                            this.mRestrictBackgroundAllowlistRevokedUids.put(com.android.internal.util.XmlUtils.readIntAttribute(in, "uid"), true);
                        }
                    }
                }
                version = version2;
            }
            fis2 = fis;
            upgradeDefaultBackgroundDataUL();
            libcore.io.IoUtils.closeQuietly(fis2);
            return;
        }
    }

    private void upgradeDefaultBackgroundDataUL() {
        this.mLoadedRestrictBackground = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "default_restrict_background_data", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upgradeWifiMeteredOverride() {
        int i;
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> wifiNetworkKeys = new android.util.ArrayMap<>();
        synchronized (this.mNetworkPoliciesSecondLock) {
            int i2 = 0;
            while (i2 < this.mNetworkPolicy.size()) {
                android.net.NetworkPolicy policy = this.mNetworkPolicy.valueAt(i2);
                if (policy.template.getMatchRule() == 4 && !policy.inferred) {
                    this.mNetworkPolicy.removeAt(i2);
                    java.util.Set<java.lang.String> keys = policy.template.getWifiNetworkKeys();
                    wifiNetworkKeys.put(keys.isEmpty() ? null : keys.iterator().next(), java.lang.Boolean.valueOf(policy.metered));
                } else {
                    i2++;
                }
            }
        }
        if (wifiNetworkKeys.isEmpty()) {
            return;
        }
        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) this.mContext.getSystemService(android.net.wifi.WifiManager.class);
        java.util.List<android.net.wifi.WifiConfiguration> configs = wm.getConfiguredNetworks();
        for (int i3 = 0; i3 < configs.size(); i3++) {
            android.net.wifi.WifiConfiguration config = configs.get(i3);
            java.util.Iterator it = config.getAllNetworkKeys().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                java.lang.String key = (java.lang.String) it.next();
                java.lang.Boolean metered = wifiNetworkKeys.get(key);
                if (metered != null) {
                    android.util.Slog.d(TAG, "Found network " + key + "; upgrading metered hint");
                    if (metered.booleanValue()) {
                        i = 1;
                    } else {
                        i = 2;
                    }
                    config.meteredOverride = i;
                    wm.updateNetwork(config);
                }
            }
        }
        synchronized (this.mUidRulesFirstLock) {
            synchronized (this.mNetworkPoliciesSecondLock) {
                writePolicyAL();
            }
        }
    }

    void writePolicyAL() {
        int subscriberIdMatchRule;
        if (LOGV) {
            android.util.Slog.v(TAG, "writePolicyAL()");
        }
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mPolicyFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            int i = 1;
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, TAG_POLICY_LIST);
            com.android.internal.util.XmlUtils.writeIntAttribute(out, ATTR_VERSION, 14);
            com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_RESTRICT_BACKGROUND, this.mRestrictBackground);
            int i2 = 0;
            while (i2 < this.mNetworkPolicy.size()) {
                android.net.NetworkPolicy policy = this.mNetworkPolicy.valueAt(i2);
                android.net.NetworkTemplate template = policy.template;
                if (android.net.NetworkPolicy.isTemplatePersistable(template)) {
                    out.startTag((java.lang.String) null, TAG_NETWORK_POLICY);
                    com.android.internal.util.XmlUtils.writeIntAttribute(out, ATTR_NETWORK_TEMPLATE, template.getMatchRule());
                    java.lang.String subscriberId = template.getSubscriberIds().isEmpty() ? null : (java.lang.String) template.getSubscriberIds().iterator().next();
                    if (subscriberId != null) {
                        out.attribute((java.lang.String) null, ATTR_SUBSCRIBER_ID, subscriberId);
                    }
                    if (template.getSubscriberIds().isEmpty()) {
                        subscriberIdMatchRule = i;
                    } else {
                        subscriberIdMatchRule = 0;
                    }
                    com.android.internal.util.XmlUtils.writeIntAttribute(out, ATTR_SUBSCRIBER_ID_MATCH_RULE, subscriberIdMatchRule);
                    if (!template.getWifiNetworkKeys().isEmpty()) {
                        out.attribute((java.lang.String) null, ATTR_NETWORK_ID, (java.lang.String) template.getWifiNetworkKeys().iterator().next());
                    }
                    com.android.internal.util.XmlUtils.writeIntAttribute(out, ATTR_TEMPLATE_METERED, template.getMeteredness());
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_CYCLE_START, android.util.RecurrenceRule.convertZonedDateTime(policy.cycleRule.start));
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_CYCLE_END, android.util.RecurrenceRule.convertZonedDateTime(policy.cycleRule.end));
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_CYCLE_PERIOD, android.util.RecurrenceRule.convertPeriod(policy.cycleRule.period));
                    com.android.internal.util.XmlUtils.writeLongAttribute(out, ATTR_WARNING_BYTES, policy.warningBytes);
                    com.android.internal.util.XmlUtils.writeLongAttribute(out, ATTR_LIMIT_BYTES, policy.limitBytes);
                    com.android.internal.util.XmlUtils.writeLongAttribute(out, ATTR_LAST_WARNING_SNOOZE, policy.lastWarningSnooze);
                    com.android.internal.util.XmlUtils.writeLongAttribute(out, ATTR_LAST_LIMIT_SNOOZE, policy.lastLimitSnooze);
                    com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_METERED, policy.metered);
                    com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_INFERRED, policy.inferred);
                    out.endTag((java.lang.String) null, TAG_NETWORK_POLICY);
                }
                i2++;
                i = 1;
            }
            for (int i3 = 0; i3 < this.mUidPolicy.size(); i3++) {
                int uid = this.mUidPolicy.keyAt(i3);
                int policy2 = this.mUidPolicy.valueAt(i3);
                if (policy2 != 0) {
                    out.startTag((java.lang.String) null, TAG_UID_POLICY);
                    com.android.internal.util.XmlUtils.writeIntAttribute(out, "uid", uid);
                    com.android.internal.util.XmlUtils.writeIntAttribute(out, ATTR_POLICY, policy2);
                    out.endTag((java.lang.String) null, TAG_UID_POLICY);
                }
            }
            out.endTag((java.lang.String) null, TAG_POLICY_LIST);
            out.startTag((java.lang.String) null, TAG_ALLOWLIST);
            int size = this.mRestrictBackgroundAllowlistRevokedUids.size();
            for (int i4 = 0; i4 < size; i4++) {
                int uid2 = this.mRestrictBackgroundAllowlistRevokedUids.keyAt(i4);
                out.startTag((java.lang.String) null, TAG_REVOKED_RESTRICT_BACKGROUND);
                com.android.internal.util.XmlUtils.writeIntAttribute(out, "uid", uid2);
                out.endTag((java.lang.String) null, TAG_REVOKED_RESTRICT_BACKGROUND);
            }
            out.endTag((java.lang.String) null, TAG_ALLOWLIST);
            out.endDocument();
            this.mPolicyFile.finishWrite(fos);
        } catch (java.io.IOException e) {
            if (fos != null) {
                this.mPolicyFile.failWrite(fos);
            }
        }
    }

    public void setUidPolicy(int uid, int policy) {
        setUidPolicy_enforcePermission();
        if (!android.os.UserHandle.isApp(uid) && !isCloneUidNL(uid)) {
            throw new java.lang.IllegalArgumentException("cannot apply policy to UID " + uid);
        }
        synchronized (this.mUidRulesFirstLock) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                int oldPolicy = this.mUidPolicy.get(uid, 0);
                if (oldPolicy != policy) {
                    setUidPolicyUncheckedUL(uid, oldPolicy, policy, true);
                    this.mLogger.uidPolicyChanged(uid, oldPolicy, policy);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
        if (!this.mOplusNPMS.isCloneUidNL(uid)) {
            int cloneAppUid = this.mOplusNPMS.getCloneAppUidNL(uid);
            setUidPolicy(cloneAppUid, getUidPolicy(uid));
        }
    }

    public void addUidPolicy(int uid, int policy) {
        addUidPolicy_enforcePermission();
        if (!android.os.UserHandle.isApp(uid) && !isCloneUidNL(uid)) {
            throw new java.lang.IllegalArgumentException("cannot apply policy to UID " + uid);
        }
        synchronized (this.mUidRulesFirstLock) {
            int oldPolicy = this.mUidPolicy.get(uid, 0);
            int policy2 = policy | oldPolicy;
            if (oldPolicy != policy2) {
                setUidPolicyUncheckedUL(uid, oldPolicy, policy2, true);
                this.mLogger.uidPolicyChanged(uid, oldPolicy, policy2);
            }
        }
        if (!isCloneUidNL(uid)) {
            int cloneAppUid = this.mOplusNPMS.getCloneAppUidNL(uid);
            int policy3 = getUidPolicy(uid);
            addUidPolicy(cloneAppUid, policy3);
        }
    }

    public void removeUidPolicy(int uid, int policy) {
        removeUidPolicy_enforcePermission();
        if (!android.os.UserHandle.isApp(uid) && !isCloneUidNL(uid)) {
            throw new java.lang.IllegalArgumentException("cannot apply policy to UID " + uid);
        }
        synchronized (this.mUidRulesFirstLock) {
            int oldPolicy = this.mUidPolicy.get(uid, 0);
            int policy2 = oldPolicy & (~policy);
            if (oldPolicy != policy2) {
                setUidPolicyUncheckedUL(uid, oldPolicy, policy2, true);
                this.mLogger.uidPolicyChanged(uid, oldPolicy, policy2);
            }
        }
        if (!isCloneUidNL(uid)) {
            this.mOplusNPMS.removeCloneUidPolicyNL(uid);
        }
    }

    private void setUidPolicyUncheckedUL(int uid, int oldPolicy, int policy, boolean persist) {
        boolean wasDenied;
        boolean isDenied;
        boolean wasAllowed;
        boolean isAllowed;
        boolean wasBlocked;
        boolean isBlocked;
        boolean notifyApp = false;
        setUidPolicyUncheckedUL(uid, policy, false);
        if (!isUidValidForAllowlistRulesUL(uid)) {
            notifyApp = false;
        } else {
            if (oldPolicy != 1) {
                wasDenied = false;
            } else {
                wasDenied = true;
            }
            if (policy != 1) {
                isDenied = false;
            } else {
                isDenied = true;
            }
            if (oldPolicy != 4) {
                wasAllowed = false;
            } else {
                wasAllowed = true;
            }
            if (policy != 4) {
                isAllowed = false;
            } else {
                isAllowed = true;
            }
            if (!wasDenied && (!this.mRestrictBackground || wasAllowed)) {
                wasBlocked = false;
            } else {
                wasBlocked = true;
            }
            if (!isDenied && (!this.mRestrictBackground || isAllowed)) {
                isBlocked = false;
            } else {
                isBlocked = true;
            }
            if (wasAllowed && ((!isAllowed || isDenied) && this.mDefaultRestrictBackgroundAllowlistUids.get(uid) && !this.mRestrictBackgroundAllowlistRevokedUids.get(uid))) {
                if (LOGD) {
                    android.util.Slog.d(TAG, "Adding uid " + uid + " to revoked restrict background allowlist");
                }
                this.mRestrictBackgroundAllowlistRevokedUids.append(uid, true);
            }
            if (wasBlocked != isBlocked) {
                notifyApp = true;
            }
        }
        this.mHandler.obtainMessage(13, uid, policy, java.lang.Boolean.valueOf(notifyApp)).sendToTarget();
        if (persist) {
            synchronized (this.mNetworkPoliciesSecondLock) {
                writePolicyAL();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUidPolicyUncheckedUL(int uid, int policy, boolean persist) {
        if (policy == 0) {
            this.mUidPolicy.delete(uid);
        } else {
            this.mUidPolicy.put(uid, policy);
        }
        lambda$updateRulesForRestrictBackgroundUL$7(uid);
        if (persist) {
            synchronized (this.mNetworkPoliciesSecondLock) {
                writePolicyAL();
            }
        }
    }

    public int getUidPolicy(int uid) {
        int i;
        getUidPolicy_enforcePermission();
        synchronized (this.mUidRulesFirstLock) {
            i = this.mUidPolicy.get(uid, 0);
        }
        return i;
    }

    public int[] getUidsWithPolicy(int policy) {
        getUidsWithPolicy_enforcePermission();
        int[] uids = new int[0];
        synchronized (this.mUidRulesFirstLock) {
            for (int i = 0; i < this.mUidPolicy.size(); i++) {
                int uid = this.mUidPolicy.keyAt(i);
                int uidPolicy = this.mUidPolicy.valueAt(i);
                if ((policy == 0 && uidPolicy == 0) || (uidPolicy & policy) != 0) {
                    uids = com.android.internal.util.ArrayUtils.appendInt(uids, uid);
                }
            }
        }
        return uids;
    }

    boolean removeUserStateUL(int userId, boolean writePolicy, boolean updateGlobalRules) {
        this.mLogger.removingUserState(userId);
        boolean changed = false;
        for (int i = this.mRestrictBackgroundAllowlistRevokedUids.size() - 1; i >= 0; i--) {
            if (android.os.UserHandle.getUserId(this.mRestrictBackgroundAllowlistRevokedUids.keyAt(i)) == userId) {
                this.mRestrictBackgroundAllowlistRevokedUids.removeAt(i);
                changed = true;
            }
        }
        int[] uids = new int[0];
        for (int i2 = 0; i2 < this.mUidPolicy.size(); i2++) {
            int uid = this.mUidPolicy.keyAt(i2);
            if (android.os.UserHandle.getUserId(uid) == userId) {
                uids = com.android.internal.util.ArrayUtils.appendInt(uids, uid);
            }
        }
        int i3 = uids.length;
        if (i3 > 0) {
            for (int i4 : uids) {
                this.mUidPolicy.delete(i4);
            }
            changed = true;
        }
        synchronized (this.mNetworkPoliciesSecondLock) {
            if (updateGlobalRules) {
                try {
                    updateRulesForGlobalChangeAL(true);
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            if (writePolicy && changed) {
                writePolicyAL();
            }
        }
        return changed;
    }

    private boolean checkAnyPermissionOf(java.lang.String... permissions) {
        for (java.lang.String permission : permissions) {
            if (this.mContext.checkCallingOrSelfPermission(permission) == 0) {
                return true;
            }
        }
        return false;
    }

    private void enforceAnyPermissionOf(java.lang.String... permissions) {
        if (!checkAnyPermissionOf(permissions)) {
            throw new java.lang.SecurityException("Requires one of the following permissions: " + java.lang.String.join(", ", permissions) + ".");
        }
    }

    public void registerListener(android.net.INetworkPolicyListener listener) {
        java.util.Objects.requireNonNull(listener);
        enforceAnyPermissionOf("android.permission.CONNECTIVITY_INTERNAL", "android.permission.OBSERVE_NETWORK_POLICY");
        this.mListeners.register(listener);
    }

    public void unregisterListener(android.net.INetworkPolicyListener listener) {
        java.util.Objects.requireNonNull(listener);
        enforceAnyPermissionOf("android.permission.CONNECTIVITY_INTERNAL", "android.permission.OBSERVE_NETWORK_POLICY");
        this.mListeners.unregister(listener);
    }

    public void setNetworkPolicies(android.net.NetworkPolicy[] policies) {
        setNetworkPolicies_enforcePermission();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mUidRulesFirstLock) {
                synchronized (this.mNetworkPoliciesSecondLock) {
                    normalizePoliciesNL(policies);
                    handleNetworkPoliciesUpdateAL(false);
                    android.util.Log.d(TAG, "setNetworkPolicies: mNetworkPolicyChange");
                    this.mNetworkPolicyChange = 1 - this.mNetworkPolicyChange;
                    android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "networkPolicyChange", this.mNetworkPolicyChange);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void addNetworkPolicyAL(android.net.NetworkPolicy policy) {
        android.net.NetworkPolicy[] policies = getNetworkPolicies(this.mContext.getOpPackageName());
        setNetworkPolicies((android.net.NetworkPolicy[]) com.android.internal.util.ArrayUtils.appendElement(android.net.NetworkPolicy.class, policies, policy));
    }

    public android.net.NetworkPolicy[] getNetworkPolicies(java.lang.String callingPackage) {
        android.net.NetworkPolicy[] policies;
        getNetworkPolicies_enforcePermission();
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", TAG);
        } catch (java.lang.SecurityException e) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", TAG);
            if (this.mAppOps.noteOp(51, android.os.Binder.getCallingUid(), callingPackage) != 0) {
                return new android.net.NetworkPolicy[0];
            }
        }
        synchronized (this.mNetworkPoliciesSecondLock) {
            int size = this.mNetworkPolicy.size();
            policies = new android.net.NetworkPolicy[size];
            for (int i = 0; i < size; i++) {
                policies[i] = this.mNetworkPolicy.valueAt(i);
            }
        }
        return policies;
    }

    private void normalizePoliciesNL() {
        normalizePoliciesNL(getNetworkPolicies(this.mContext.getOpPackageName()));
    }

    private void normalizePoliciesNL(android.net.NetworkPolicy[] policies) {
        this.mNetworkPolicy.clear();
        for (android.net.NetworkPolicy policy : policies) {
            if (policy != null) {
                policy.template = normalizeTemplate(policy.template, this.mMergedSubscriberIds);
                android.net.NetworkPolicy existing = this.mNetworkPolicy.get(policy.template);
                if (existing == null || existing.compareTo(policy) > 0) {
                    if (existing != null) {
                        android.util.Slog.d(TAG, "Normalization replaced " + existing + " with " + policy);
                    }
                    android.util.Slog.d(TAG, "Normalization put networkpolicy" + policy.template);
                    this.mNetworkPolicy.put(policy.template, policy);
                }
            }
        }
    }

    static android.net.NetworkTemplate normalizeTemplate(android.net.NetworkTemplate template, java.util.List<java.lang.String[]> mergedList) {
        if (template.getSubscriberIds().isEmpty()) {
            return template;
        }
        for (java.lang.String[] merged : mergedList) {
            android.util.ArraySet mergedSet = new android.util.ArraySet(merged);
            if (mergedSet.size() != merged.length) {
                android.util.Log.wtf(TAG, "Duplicated merged list detected: " + java.util.Arrays.toString(merged));
            }
            for (java.lang.String subscriberId : template.getSubscriberIds()) {
                if (com.android.net.module.util.CollectionUtils.contains(merged, subscriberId)) {
                    return new android.net.NetworkTemplate.Builder(template.getMatchRule()).setWifiNetworkKeys(template.getWifiNetworkKeys()).setSubscriberIds(mergedSet).setMeteredness(template.getMeteredness()).build();
                }
            }
        }
        return template;
    }

    public void snoozeLimit(android.net.NetworkTemplate template) {
        snoozeLimit_enforcePermission();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            performSnooze(template, 35);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void performSnooze(android.net.NetworkTemplate template, int type) {
        long currentTime = this.mClock.millis();
        synchronized (this.mUidRulesFirstLock) {
            synchronized (this.mNetworkPoliciesSecondLock) {
                android.net.NetworkPolicy policy = this.mNetworkPolicy.get(template);
                if (policy == null) {
                    throw new java.lang.IllegalArgumentException("unable to find policy for " + template);
                }
                switch (type) {
                    case 34:
                        policy.lastWarningSnooze = currentTime;
                        break;
                    case 35:
                        policy.lastLimitSnooze = currentTime;
                        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$performSnooze$3();
                            }
                        });
                        break;
                    case 45:
                        policy.lastRapidSnooze = currentTime;
                        break;
                    default:
                        throw new java.lang.IllegalArgumentException("unexpected type");
                }
                handleNetworkPoliciesUpdateAL(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSnooze$3() {
        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        if (!tm.getDataEnabled()) {
            tm.setDataEnabled(true);
            android.util.Slog.d(TAG, "performSnooze setdataenable to true");
        }
    }

    public void setRestrictBackground(boolean restrictBackground) {
        android.os.Trace.traceBegin(2097152L, "setRestrictBackground");
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_NETWORK_POLICY", TAG);
            int callingUid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (this.mUidRulesFirstLock) {
                    setRestrictBackgroundUL(restrictBackground, "uid:" + callingUid);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void setRestrictBackgroundUL(boolean restrictBackground, java.lang.String reason) {
        android.os.Trace.traceBegin(2097152L, "setRestrictBackgroundUL");
        try {
            if (restrictBackground == this.mRestrictBackground) {
                android.util.Slog.w(TAG, "setRestrictBackgroundUL: already " + restrictBackground);
                return;
            }
            android.util.Slog.d(TAG, "setRestrictBackgroundUL(): " + restrictBackground + "; reason: " + reason);
            boolean oldRestrictBackground = this.mRestrictBackground;
            this.mRestrictBackground = restrictBackground;
            updateRulesForRestrictBackgroundUL();
            try {
                if (!this.mNetworkManager.setDataSaverModeEnabled(this.mRestrictBackground)) {
                    android.util.Slog.e(TAG, "Could not change Data Saver Mode on NMS to " + this.mRestrictBackground);
                    this.mRestrictBackground = oldRestrictBackground;
                    return;
                }
            } catch (android.os.RemoteException e) {
            }
            sendRestrictBackgroundChangedMsg();
            this.mLogger.restrictBackgroundChanged(oldRestrictBackground, this.mRestrictBackground);
            if (this.mRestrictBackgroundLowPowerMode) {
                this.mRestrictBackgroundChangedInBsm = true;
            }
            synchronized (this.mNetworkPoliciesSecondLock) {
                updateNotificationsNL();
                writePolicyAL();
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void sendRestrictBackgroundChangedMsg() {
        this.mHandler.removeMessages(6);
        this.mHandler.obtainMessage(6, this.mRestrictBackground ? 1 : 0, 0).sendToTarget();
    }

    public int getRestrictBackgroundByCaller() {
        getRestrictBackgroundByCaller_enforcePermission();
        return getRestrictBackgroundStatusInternal(android.os.Binder.getCallingUid());
    }

    public int getRestrictBackgroundStatus(int uid) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        return getRestrictBackgroundStatusInternal(uid);
    }

    private int getRestrictBackgroundStatusInternal(int uid) {
        synchronized (this.mUidRulesFirstLock) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                int policy = getUidPolicy(uid);
                android.os.Binder.restoreCallingIdentity(token);
                int i = 3;
                if (policy == 1) {
                    return 3;
                }
                if (!this.mRestrictBackground) {
                    return 1;
                }
                if ((this.mUidPolicy.get(uid) & 4) != 0) {
                    i = 2;
                }
                return i;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }
    }

    public boolean getRestrictBackground() {
        boolean z;
        getRestrictBackground_enforcePermission();
        synchronized (this.mUidRulesFirstLock) {
            z = this.mRestrictBackground;
        }
        return z;
    }

    public void setDeviceIdleMode(boolean enabled) {
        setDeviceIdleMode_enforcePermission();
        android.os.Trace.traceBegin(2097152L, "setDeviceIdleMode");
        try {
            synchronized (this.mUidRulesFirstLock) {
                if (this.mDeviceIdleMode == enabled) {
                    return;
                }
                this.mDeviceIdleMode = enabled;
                this.mLogger.deviceIdleModeEnabled(enabled);
                if (this.mSystemReady) {
                    handleDeviceIdleModeChangedUL(enabled);
                }
                if (enabled) {
                    com.android.server.EventLogTags.writeDeviceIdleOnPhase("net");
                } else {
                    com.android.server.EventLogTags.writeDeviceIdleOffPhase("net");
                }
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    public void setWifiMeteredOverride(java.lang.String networkId, int meteredOverride) {
        setWifiMeteredOverride_enforcePermission();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) this.mContext.getSystemService(android.net.wifi.WifiManager.class);
            java.util.List<android.net.wifi.WifiConfiguration> configs = wm.getConfiguredNetworks();
            for (android.net.wifi.WifiConfiguration config : configs) {
                if (java.util.Objects.equals(android.net.NetworkPolicyManager.resolveNetworkId(config), networkId)) {
                    config.meteredOverride = meteredOverride;
                    wm.updateNetwork(config);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void enforceSubscriptionPlanAccess(int subId, int callingUid, java.lang.String callingPackage) {
        this.mAppOps.checkPackage(callingUid, callingPackage);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.os.PersistableBundle config = this.mCarrierConfigManager.getConfigForSubId(subId);
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
            if (tm != null && tm.hasCarrierPrivileges(subId)) {
                return;
            }
            if (config != null) {
                java.lang.String overridePackage = config.getString("config_plans_package_override_string", null);
                if (!android.text.TextUtils.isEmpty(overridePackage) && java.util.Objects.equals(overridePackage, callingPackage)) {
                    return;
                }
            }
            java.lang.String defaultPackage = this.mCarrierConfigManager.getDefaultCarrierServicePackageName();
            if (android.text.TextUtils.isEmpty(defaultPackage) || !java.util.Objects.equals(defaultPackage, callingPackage)) {
                java.lang.String testPackage = android.os.SystemProperties.get("persist.sys.sub_plan_owner." + subId, (java.lang.String) null);
                if (android.text.TextUtils.isEmpty(testPackage) || !java.util.Objects.equals(testPackage, callingPackage)) {
                    java.lang.String legacyTestPackage = android.os.SystemProperties.get("fw.sub_plan_owner." + subId, (java.lang.String) null);
                    if (!android.text.TextUtils.isEmpty(legacyTestPackage) && java.util.Objects.equals(legacyTestPackage, callingPackage)) {
                        return;
                    }
                    this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_SUBSCRIPTION_PLANS", TAG);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void enforceSubscriptionPlanValidity(android.telephony.SubscriptionPlan[] plans) {
        if (plans.length == 0) {
            android.util.Log.d(TAG, "Received empty plans list. Clearing existing SubscriptionPlans.");
            return;
        }
        int[] allNetworkTypes = android.telephony.TelephonyManager.getAllNetworkTypes();
        android.util.ArraySet<java.lang.Integer> allNetworksSet = new android.util.ArraySet<>();
        addAll(allNetworksSet, allNetworkTypes);
        android.util.ArraySet<java.lang.Integer> applicableNetworkTypes = new android.util.ArraySet<>();
        boolean hasGeneralPlan = false;
        for (android.telephony.SubscriptionPlan subscriptionPlan : plans) {
            int[] planNetworkTypes = subscriptionPlan.getNetworkTypes();
            android.util.ArraySet<java.lang.Integer> planNetworksSet = new android.util.ArraySet<>();
            for (int j = 0; j < planNetworkTypes.length; j++) {
                if (allNetworksSet.contains(java.lang.Integer.valueOf(planNetworkTypes[j]))) {
                    if (!planNetworksSet.add(java.lang.Integer.valueOf(planNetworkTypes[j]))) {
                        throw new java.lang.IllegalArgumentException("Subscription plan contains duplicate network types.");
                    }
                } else {
                    throw new java.lang.IllegalArgumentException("Invalid network type: " + planNetworkTypes[j]);
                }
            }
            int j2 = planNetworkTypes.length;
            if (j2 == allNetworkTypes.length) {
                hasGeneralPlan = true;
            } else if (!addAll(applicableNetworkTypes, planNetworkTypes)) {
                throw new java.lang.IllegalArgumentException("Multiple subscription plans defined for a single network type.");
            }
        }
        if (!hasGeneralPlan) {
            throw new java.lang.IllegalArgumentException("No generic subscription plan that applies to all network types.");
        }
    }

    private static boolean addAll(android.util.ArraySet<java.lang.Integer> set, int... elements) {
        boolean result = true;
        for (int i : elements) {
            result &= set.add(java.lang.Integer.valueOf(i));
        }
        return result;
    }

    public android.telephony.SubscriptionPlan getSubscriptionPlan(android.net.NetworkTemplate template) {
        android.telephony.SubscriptionPlan primarySubscriptionPlanLocked;
        enforceAnyPermissionOf("android.permission.MAINLINE_NETWORK_STACK");
        synchronized (this.mNetworkPoliciesSecondLock) {
            int subId = findRelevantSubIdNL(template);
            primarySubscriptionPlanLocked = getPrimarySubscriptionPlanLocked(subId);
        }
        return primarySubscriptionPlanLocked;
    }

    public void notifyStatsProviderWarningOrLimitReached() {
        enforceAnyPermissionOf("android.permission.MAINLINE_NETWORK_STACK");
        synchronized (this.mNetworkPoliciesSecondLock) {
            if (this.mSystemReady) {
                this.mHandler.obtainMessage(20).sendToTarget();
            }
        }
    }

    public android.telephony.SubscriptionPlan[] getSubscriptionPlans(int subId, java.lang.String callingPackage) {
        enforceSubscriptionPlanAccess(subId, android.os.Binder.getCallingUid(), callingPackage);
        java.lang.String fake = android.os.SystemProperties.get("fw.fake_plan");
        if (!android.text.TextUtils.isEmpty(fake)) {
            java.util.List<android.telephony.SubscriptionPlan> plans = new java.util.ArrayList<>();
            if ("month_hard".equals(fake)) {
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2007-03-14T00:00:00.000Z")).setTitle("G-Mobile").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 1).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(1L), java.time.ZonedDateTime.now().minusHours(36L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2017-03-14T00:00:00.000Z")).setTitle("G-Mobile Happy").setDataLimit(Long.MAX_VALUE, 1).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(5L), java.time.ZonedDateTime.now().minusHours(36L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2017-03-14T00:00:00.000Z")).setTitle("G-Mobile, Charged after limit").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 1).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(5L), java.time.ZonedDateTime.now().minusHours(36L).toInstant().toEpochMilli()).build());
            } else if ("month_soft".equals(fake)) {
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2007-03-14T00:00:00.000Z")).setTitle("G-Mobile is the carriers name who this plan belongs to").setSummary("Crazy unlimited bandwidth plan with incredibly long title that should be cut off to prevent UI from looking terrible").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 2).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(1L), java.time.ZonedDateTime.now().minusHours(1L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2017-03-14T00:00:00.000Z")).setTitle("G-Mobile, Throttled after limit").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 2).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(5L), java.time.ZonedDateTime.now().minusHours(1L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2017-03-14T00:00:00.000Z")).setTitle("G-Mobile, No data connection after limit").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 0).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(5L), java.time.ZonedDateTime.now().minusHours(1L).toInstant().toEpochMilli()).build());
            } else if ("month_over".equals(fake)) {
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2007-03-14T00:00:00.000Z")).setTitle("G-Mobile is the carriers name who this plan belongs to").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 2).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(6L), java.time.ZonedDateTime.now().minusHours(1L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2017-03-14T00:00:00.000Z")).setTitle("G-Mobile, Throttled after limit").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 2).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(5L), java.time.ZonedDateTime.now().minusHours(1L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2017-03-14T00:00:00.000Z")).setTitle("G-Mobile, No data connection after limit").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 0).setDataUsage(android.util.DataUnit.GIBIBYTES.toBytes(5L), java.time.ZonedDateTime.now().minusHours(1L).toInstant().toEpochMilli()).build());
            } else if ("month_none".equals(fake)) {
                plans.add(android.telephony.SubscriptionPlan.Builder.createRecurringMonthly(java.time.ZonedDateTime.parse("2007-03-14T00:00:00.000Z")).setTitle("G-Mobile").build());
            } else if ("prepaid".equals(fake)) {
                plans.add(android.telephony.SubscriptionPlan.Builder.createNonrecurring(java.time.ZonedDateTime.now().minusDays(20L), java.time.ZonedDateTime.now().plusDays(10L)).setTitle("G-Mobile").setDataLimit(android.util.DataUnit.MEBIBYTES.toBytes(512L), 0).setDataUsage(android.util.DataUnit.MEBIBYTES.toBytes(100L), java.time.ZonedDateTime.now().minusHours(3L).toInstant().toEpochMilli()).build());
            } else if ("prepaid_crazy".equals(fake)) {
                plans.add(android.telephony.SubscriptionPlan.Builder.createNonrecurring(java.time.ZonedDateTime.now().minusDays(20L), java.time.ZonedDateTime.now().plusDays(10L)).setTitle("G-Mobile Anytime").setDataLimit(android.util.DataUnit.MEBIBYTES.toBytes(512L), 0).setDataUsage(android.util.DataUnit.MEBIBYTES.toBytes(100L), java.time.ZonedDateTime.now().minusHours(3L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createNonrecurring(java.time.ZonedDateTime.now().minusDays(10L), java.time.ZonedDateTime.now().plusDays(20L)).setTitle("G-Mobile Nickel Nights").setSummary("5¢/GB between 1-5AM").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(5L), 2).setDataUsage(android.util.DataUnit.MEBIBYTES.toBytes(15L), java.time.ZonedDateTime.now().minusHours(30L).toInstant().toEpochMilli()).build());
                plans.add(android.telephony.SubscriptionPlan.Builder.createNonrecurring(java.time.ZonedDateTime.now().minusDays(10L), java.time.ZonedDateTime.now().plusDays(20L)).setTitle("G-Mobile Bonus 3G").setSummary("Unlimited 3G data").setDataLimit(android.util.DataUnit.GIBIBYTES.toBytes(1L), 2).setDataUsage(android.util.DataUnit.MEBIBYTES.toBytes(300L), java.time.ZonedDateTime.now().minusHours(1L).toInstant().toEpochMilli()).build());
            } else if ("unlimited".equals(fake)) {
                plans.add(android.telephony.SubscriptionPlan.Builder.createNonrecurring(java.time.ZonedDateTime.now().minusDays(20L), java.time.ZonedDateTime.now().plusDays(10L)).setTitle("G-Mobile Awesome").setDataLimit(Long.MAX_VALUE, 2).setDataUsage(android.util.DataUnit.MEBIBYTES.toBytes(50L), java.time.ZonedDateTime.now().minusHours(3L).toInstant().toEpochMilli()).build());
            }
            return (android.telephony.SubscriptionPlan[]) plans.toArray(new android.telephony.SubscriptionPlan[plans.size()]);
        }
        synchronized (this.mNetworkPoliciesSecondLock) {
            java.lang.String ownerPackage = this.mSubscriptionPlansOwner.get(subId);
            if (!java.util.Objects.equals(ownerPackage, callingPackage) && android.os.UserHandle.getCallingAppId() != 1000 && android.os.UserHandle.getCallingAppId() != 1001) {
                android.util.Log.w(TAG, "Not returning plans because caller " + callingPackage + " doesn't match owner " + ownerPackage);
                return null;
            }
            return this.mSubscriptionPlans.get(subId);
        }
    }

    public void setSubscriptionPlans(int subId, android.telephony.SubscriptionPlan[] plans, long expirationDurationMillis, java.lang.String callingPackage) {
        enforceSubscriptionPlanAccess(subId, android.os.Binder.getCallingUid(), callingPackage);
        enforceSubscriptionPlanValidity(plans);
        for (android.telephony.SubscriptionPlan plan : plans) {
            java.util.Objects.requireNonNull(plan);
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            setSubscriptionPlansInternal(subId, plans, expirationDurationMillis, callingPackage);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSubscriptionPlansInternal(int subId, android.telephony.SubscriptionPlan[] plans, long expirationDurationMillis, java.lang.String callingPackage) {
        synchronized (this.mUidRulesFirstLock) {
            synchronized (this.mNetworkPoliciesSecondLock) {
                this.mSubscriptionPlans.put(subId, plans);
                this.mSubscriptionPlansOwner.put(subId, callingPackage);
                java.lang.String subscriberId = this.mSubIdToSubscriberId.get(subId, null);
                if (subscriberId != null) {
                    ensureActiveCarrierPolicyAL(subId, subscriberId);
                    maybeUpdateCarrierPolicyCycleAL(subId, subscriberId);
                } else {
                    android.util.Slog.wtf(TAG, "Missing subscriberId for subId " + subId);
                }
                handleNetworkPoliciesUpdateAL(true);
                android.content.Intent intent = new android.content.Intent("android.telephony.action.SUBSCRIPTION_PLANS_CHANGED");
                intent.addFlags(1073741824);
                intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
                this.mContext.sendBroadcast(intent, "android.permission.MANAGE_SUBSCRIPTION_PLANS");
                this.mHandler.sendMessage(this.mHandler.obtainMessage(19, subId, 0, plans));
                int setPlansId = this.mSetSubscriptionPlansIdCounter;
                this.mSetSubscriptionPlansIdCounter = setPlansId + 1;
                this.mSetSubscriptionPlansIds.put(subId, setPlansId);
                if (expirationDurationMillis > 0) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(22, subId, setPlansId, callingPackage), expirationDurationMillis);
                }
            }
        }
    }

    void setSubscriptionPlansOwner(int subId, java.lang.String packageName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.NETWORK_SETTINGS", TAG);
        android.os.SystemProperties.set("persist.sys.sub_plan_owner." + subId, packageName);
    }

    public java.lang.String getSubscriptionPlansOwner(int subId) {
        java.lang.String str;
        if (android.os.UserHandle.getCallingAppId() != 1000) {
            throw new java.lang.SecurityException();
        }
        synchronized (this.mNetworkPoliciesSecondLock) {
            str = this.mSubscriptionPlansOwner.get(subId);
        }
        return str;
    }

    public void setSubscriptionOverride(int subId, int overrideMask, int overrideValue, int[] networkTypes, long expirationDurationMillis, java.lang.String callingPackage) throws java.lang.Throwable {
        enforceSubscriptionPlanAccess(subId, android.os.Binder.getCallingUid(), callingPackage);
        android.util.ArraySet<java.lang.Integer> allNetworksSet = new android.util.ArraySet<>();
        addAll(allNetworksSet, android.telephony.TelephonyManager.getAllNetworkTypes());
        android.util.IntArray applicableNetworks = new android.util.IntArray();
        for (int networkType : networkTypes) {
            if (allNetworksSet.contains(java.lang.Integer.valueOf(networkType))) {
                applicableNetworks.add(networkType);
            } else {
                android.util.Log.d(TAG, "setSubscriptionOverride removing invalid network type: " + networkType);
            }
        }
        synchronized (this.mNetworkPoliciesSecondLock) {
            try {
                try {
                    android.telephony.SubscriptionPlan plan = getPrimarySubscriptionPlanLocked(subId);
                    if (overrideMask != 1 && (plan == null || plan.getDataLimitBehavior() == -1)) {
                        throw new java.lang.IllegalStateException("Must provide valid SubscriptionPlan to enable overriding");
                    }
                    boolean overrideEnabled = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "netpolicy_override_enabled", 1) != 0;
                    if (overrideEnabled || overrideValue == 0) {
                        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                        args.arg1 = java.lang.Integer.valueOf(subId);
                        args.arg2 = java.lang.Integer.valueOf(overrideMask);
                        args.arg3 = java.lang.Integer.valueOf(overrideValue);
                        args.arg4 = applicableNetworks.toArray();
                        this.mHandler.sendMessage(this.mHandler.obtainMessage(16, args));
                        if (expirationDurationMillis > 0) {
                            args.arg3 = 0;
                            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(16, args), expirationDurationMillis);
                        }
                    }
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

    public int getMultipathPreference(android.net.Network network) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        java.lang.Integer preference = this.mMultipathPolicyTracker.getMultipathPreference(network);
        if (preference != null) {
            return preference.intValue();
        }
        return 0;
    }

    @dalvik.annotation.optimization.NeverCompile
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, writer)) {
            com.android.internal.util.IndentingPrintWriter fout = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
            android.util.ArraySet<java.lang.String> argSet = new android.util.ArraySet<>(args.length);
            for (java.lang.String arg : args) {
                argSet.add(arg);
            }
            synchronized (this.mUidRulesFirstLock) {
                synchronized (this.mNetworkPoliciesSecondLock) {
                    if (argSet.contains("--unsnooze")) {
                        for (int i = this.mNetworkPolicy.size() - 1; i >= 0; i--) {
                            this.mNetworkPolicy.valueAt(i).clearSnooze();
                        }
                        handleNetworkPoliciesUpdateAL(true);
                        fout.println("Cleared snooze timestamps");
                        return;
                    }
                    fout.print("System ready: ");
                    fout.println(this.mSystemReady);
                    fout.print("Restrict background: ");
                    fout.println(this.mRestrictBackground);
                    fout.print("Restrict power: ");
                    fout.println(this.mRestrictPower);
                    fout.print("Device idle: ");
                    fout.println(this.mDeviceIdleMode);
                    fout.print("Restricted networking mode: ");
                    fout.println(this.mRestrictedNetworkingMode);
                    fout.print("Low Power Standby mode: ");
                    fout.println(this.mLowPowerStandbyActive);
                    synchronized (this.mMeteredIfacesLock) {
                        fout.print("Metered ifaces: ");
                        fout.println(this.mMeteredIfaces);
                    }
                    fout.println();
                    fout.println("Flags:");
                    fout.println("com.android.server.net.network_blocked_for_top_sleeping_and_above: " + this.mBackgroundNetworkRestricted);
                    fout.println("com.android.server.net.use_metered_firewall_chains: " + this.mUseMeteredFirewallChains);
                    fout.println("com.android.server.net.use_different_delays_for_background_chain: " + this.mUseDifferentDelaysForBackgroundChain);
                    fout.println();
                    fout.println("mRestrictBackgroundLowPowerMode: " + this.mRestrictBackgroundLowPowerMode);
                    fout.println("mRestrictBackgroundBeforeBsm: " + this.mRestrictBackgroundBeforeBsm);
                    fout.println("mLoadedRestrictBackground: " + this.mLoadedRestrictBackground);
                    fout.println("mRestrictBackgroundChangedInBsm: " + this.mRestrictBackgroundChangedInBsm);
                    fout.println();
                    fout.println("Network policies:");
                    fout.increaseIndent();
                    for (int i2 = 0; i2 < this.mNetworkPolicy.size(); i2++) {
                        fout.println(this.mNetworkPolicy.valueAt(i2).toString());
                    }
                    fout.decreaseIndent();
                    fout.println();
                    fout.println("Subscription plans:");
                    fout.increaseIndent();
                    for (int i3 = 0; i3 < this.mSubscriptionPlans.size(); i3++) {
                        int subId = this.mSubscriptionPlans.keyAt(i3);
                        fout.println("Subscriber ID " + subId + ":");
                        fout.increaseIndent();
                        android.telephony.SubscriptionPlan[] plans = this.mSubscriptionPlans.valueAt(i3);
                        if (!com.android.internal.util.ArrayUtils.isEmpty(plans)) {
                            for (android.telephony.SubscriptionPlan plan : plans) {
                                fout.println(plan);
                            }
                        }
                        fout.decreaseIndent();
                    }
                    fout.decreaseIndent();
                    fout.println();
                    fout.println("Active subscriptions:");
                    fout.increaseIndent();
                    for (int i4 = 0; i4 < this.mSubIdToSubscriberId.size(); i4++) {
                        int subId2 = this.mSubIdToSubscriberId.keyAt(i4);
                        java.lang.String subscriberId = this.mSubIdToSubscriberId.valueAt(i4);
                        fout.println(subId2 + "=" + com.android.net.module.util.NetworkIdentityUtils.scrubSubscriberId(subscriberId));
                    }
                    fout.decreaseIndent();
                    fout.println();
                    for (java.lang.String[] mergedSubscribers : this.mMergedSubscriberIds) {
                        fout.println("Merged subscriptions: " + java.util.Arrays.toString(com.android.net.module.util.NetworkIdentityUtils.scrubSubscriberIds(mergedSubscribers)));
                    }
                    fout.println();
                    fout.println("Policy for UIDs:");
                    fout.increaseIndent();
                    int size = this.mUidPolicy.size();
                    for (int i5 = 0; i5 < size; i5++) {
                        int uid = this.mUidPolicy.keyAt(i5);
                        int policy = this.mUidPolicy.valueAt(i5);
                        fout.print("UID=");
                        fout.print(uid);
                        fout.print(" policy=");
                        fout.print(android.net.NetworkPolicyManager.uidPoliciesToString(policy));
                        fout.println();
                    }
                    fout.decreaseIndent();
                    int size2 = this.mPowerSaveWhitelistExceptIdleAppIds.size();
                    if (size2 > 0) {
                        fout.println("Power save whitelist (except idle) app ids:");
                        fout.increaseIndent();
                        for (int i6 = 0; i6 < size2; i6++) {
                            fout.print("UID=");
                            fout.print(this.mPowerSaveWhitelistExceptIdleAppIds.keyAt(i6));
                            fout.print(": ");
                            fout.print(this.mPowerSaveWhitelistExceptIdleAppIds.valueAt(i6));
                            fout.println();
                        }
                        fout.decreaseIndent();
                    }
                    int size3 = this.mPowerSaveWhitelistAppIds.size();
                    if (size3 > 0) {
                        fout.println("Power save whitelist app ids:");
                        fout.increaseIndent();
                        for (int i7 = 0; i7 < size3; i7++) {
                            fout.print("UID=");
                            fout.print(this.mPowerSaveWhitelistAppIds.keyAt(i7));
                            fout.print(": ");
                            fout.print(this.mPowerSaveWhitelistAppIds.valueAt(i7));
                            fout.println();
                        }
                        fout.decreaseIndent();
                    }
                    int size4 = this.mAppIdleTempWhitelistAppIds.size();
                    if (size4 > 0) {
                        fout.println("App idle whitelist app ids:");
                        fout.increaseIndent();
                        for (int i8 = 0; i8 < size4; i8++) {
                            fout.print("UID=");
                            fout.print(this.mAppIdleTempWhitelistAppIds.keyAt(i8));
                            fout.print(": ");
                            fout.print(this.mAppIdleTempWhitelistAppIds.valueAt(i8));
                            fout.println();
                        }
                        fout.decreaseIndent();
                    }
                    int size5 = this.mDefaultRestrictBackgroundAllowlistUids.size();
                    if (size5 > 0) {
                        fout.println("Default restrict background allowlist uids:");
                        fout.increaseIndent();
                        for (int i9 = 0; i9 < size5; i9++) {
                            fout.print("UID=");
                            fout.print(this.mDefaultRestrictBackgroundAllowlistUids.keyAt(i9));
                            fout.println();
                        }
                        fout.decreaseIndent();
                    }
                    int size6 = this.mRestrictBackgroundAllowlistRevokedUids.size();
                    if (size6 > 0) {
                        fout.println("Default restrict background allowlist uids revoked by users:");
                        fout.increaseIndent();
                        for (int i10 = 0; i10 < size6; i10++) {
                            fout.print("UID=");
                            fout.print(this.mRestrictBackgroundAllowlistRevokedUids.keyAt(i10));
                            fout.println();
                        }
                        fout.decreaseIndent();
                    }
                    int size7 = this.mLowPowerStandbyAllowlistUids.size();
                    if (size7 > 0) {
                        fout.println("Low Power Standby allowlist uids:");
                        fout.increaseIndent();
                        for (int i11 = 0; i11 < size7; i11++) {
                            fout.print("UID=");
                            fout.print(this.mLowPowerStandbyAllowlistUids.keyAt(i11));
                            fout.println();
                        }
                        fout.decreaseIndent();
                    }
                    if (this.mBackgroundNetworkRestricted) {
                        fout.println();
                        if (this.mUseDifferentDelaysForBackgroundChain) {
                            fout.print("Background restrictions short delay: ");
                            android.util.TimeUtils.formatDuration(this.mBackgroundRestrictionShortDelayMs, fout);
                            fout.println();
                            fout.print("Background restrictions long delay: ");
                            android.util.TimeUtils.formatDuration(this.mBackgroundRestrictionLongDelayMs, fout);
                            fout.println();
                        }
                        int size8 = this.mBackgroundTransitioningUids.size();
                        if (size8 > 0) {
                            long nowUptime = android.os.SystemClock.uptimeMillis();
                            fout.println("Uids transitioning to background:");
                            fout.increaseIndent();
                            for (int i12 = 0; i12 < size8; i12++) {
                                fout.print("UID=");
                                fout.print(this.mBackgroundTransitioningUids.keyAt(i12));
                                fout.print(", ");
                                android.util.TimeUtils.formatDuration(this.mBackgroundTransitioningUids.valueAt(i12), nowUptime, fout);
                                fout.println();
                            }
                            fout.decreaseIndent();
                        }
                        fout.println();
                    }
                    android.util.SparseBooleanArray knownUids = new android.util.SparseBooleanArray();
                    collectKeys(this.mUidState, knownUids);
                    synchronized (this.mUidBlockedState) {
                        collectKeys(this.mUidBlockedState, knownUids);
                    }
                    synchronized (this.mUidStateCallbackInfos) {
                        collectKeys(this.mUidStateCallbackInfos, knownUids);
                    }
                    fout.println("Status for all known UIDs:");
                    fout.increaseIndent();
                    int size9 = knownUids.size();
                    for (int i13 = 0; i13 < size9; i13++) {
                        int uid2 = knownUids.keyAt(i13);
                        fout.print("UID", java.lang.Integer.valueOf(uid2));
                        android.net.NetworkPolicyManager.UidState uidState = this.mUidState.get(uid2);
                        fout.print("state", uidState);
                        synchronized (this.mUidBlockedState) {
                            com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = this.mUidBlockedState.get(uid2);
                            fout.print("blocked_state", uidBlockedState);
                        }
                        synchronized (this.mUidStateCallbackInfos) {
                            com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo callbackInfo = this.mUidStateCallbackInfos.get(uid2);
                            fout.println();
                            fout.increaseIndent();
                            fout.print("callback_info", callbackInfo);
                            fout.decreaseIndent();
                        }
                        fout.println();
                    }
                    fout.decreaseIndent();
                    fout.println();
                    fout.println("Admin restricted uids for metered data:");
                    fout.increaseIndent();
                    int size10 = this.mMeteredRestrictedUids.size();
                    for (int i14 = 0; i14 < size10; i14++) {
                        fout.print("u" + this.mMeteredRestrictedUids.keyAt(i14) + ": ");
                        fout.println(this.mMeteredRestrictedUids.valueAt(i14));
                    }
                    fout.decreaseIndent();
                    fout.println();
                    fout.println("Network to interfaces:");
                    fout.increaseIndent();
                    for (int i15 = 0; i15 < this.mNetworkToIfaces.size(); i15++) {
                        int key = this.mNetworkToIfaces.keyAt(i15);
                        fout.println(key + ": " + this.mNetworkToIfaces.get(key));
                    }
                    fout.decreaseIndent();
                    fout.println();
                    fout.print("Active notifications: ");
                    fout.println(this.mActiveNotifs);
                    fout.println();
                    this.mStatLogger.dump(fout);
                    this.mLogger.dumpLogs(fout);
                    fout.println();
                    this.mMultipathPolicyTracker.dump(fout);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
        return new com.android.server.net.NetworkPolicyManagerShellCommand(this.mContext, this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
    }

    void setDebugUid(int uid) {
        this.mLogger.setDebugUid(uid);
    }

    boolean isUidForegroundOnRestrictBackgroundUL(int uid) {
        android.net.NetworkPolicyManager.UidState uidState = this.mUidState.get(uid);
        if (android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(uidState)) {
            return true;
        }
        synchronized (this.mUidStateCallbackInfos) {
            com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo callbackInfo = this.mUidStateCallbackInfos.get(uid);
            long prevProcStateSeq = uidState != null ? uidState.procStateSeq : -1L;
            if (callbackInfo != null && callbackInfo.isPending && callbackInfo.procStateSeq >= prevProcStateSeq) {
                return android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(callbackInfo.procState, callbackInfo.capability);
            }
            return false;
        }
    }

    boolean isUidForegroundOnRestrictPowerUL(int uid) {
        android.net.NetworkPolicyManager.UidState uidState = this.mUidState.get(uid);
        if (android.net.NetworkPolicyManager.isProcStateAllowedWhileIdleOrPowerSaveMode(uidState)) {
            return true;
        }
        synchronized (this.mUidStateCallbackInfos) {
            com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo callbackInfo = this.mUidStateCallbackInfos.get(uid);
            long prevProcStateSeq = uidState != null ? uidState.procStateSeq : -1L;
            if (callbackInfo != null && callbackInfo.isPending && callbackInfo.procStateSeq >= prevProcStateSeq) {
                return android.net.NetworkPolicyManager.isProcStateAllowedWhileIdleOrPowerSaveMode(callbackInfo.procState, callbackInfo.capability);
            }
            return false;
        }
    }

    private boolean isUidTop(int uid) {
        android.net.NetworkPolicyManager.UidState uidState = this.mUidState.get(uid);
        return android.net.NetworkPolicyManager.isProcStateAllowedWhileInLowPowerStandby(uidState);
    }

    private boolean isUidExemptFromBackgroundRestrictions(int uid) {
        return this.mBackgroundTransitioningUids.indexOfKey(uid) >= 0 || android.net.NetworkPolicyManager.isProcStateAllowedNetworkWhileBackground(this.mUidState.get(uid));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getBackgroundTransitioningDelay(int procState) {
        if (this.mUseDifferentDelaysForBackgroundChain) {
            return procState <= 15 ? this.mBackgroundRestrictionLongDelayMs : this.mBackgroundRestrictionShortDelayMs;
        }
        return this.mBackgroundRestrictionDelayMs;
    }

    private boolean updateUidStateUL(int uid, int procState, long procStateSeq, int capability) {
        android.os.Trace.traceBegin(2097152L, "updateUidStateUL: " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.app.ActivityManager.procStateToString(procState) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + procStateSeq + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.app.ActivityManager.getCapabilitiesSummary(capability));
        try {
            android.net.NetworkPolicyManager.UidState oldUidState = this.mUidState.get(uid);
            if (oldUidState != null && procStateSeq < oldUidState.procStateSeq) {
                if (LOGV) {
                    android.util.Slog.v(TAG, "Ignoring older uid state updates; uid=" + uid + ",procState=" + android.app.ActivityManager.procStateToString(procState) + ",seq=" + procStateSeq + ",cap=" + capability + ",oldUidState=" + oldUidState);
                }
                android.os.Trace.traceEnd(2097152L);
                return false;
            }
            if (oldUidState != null && oldUidState.procState == procState && oldUidState.capability == capability) {
                android.os.Trace.traceEnd(2097152L);
                return false;
            }
            android.net.NetworkPolicyManager.UidState newUidState = new android.net.NetworkPolicyManager.UidState(uid, procState, procStateSeq, capability);
            this.mUidState.put(uid, newUidState);
            updateRestrictBackgroundRulesOnUidStatusChangedUL(uid, oldUidState, newUidState);
            boolean updatePowerRestrictionRules = false;
            boolean allowedWhileIdleOrPowerSaveModeChanged = android.net.NetworkPolicyManager.isProcStateAllowedWhileIdleOrPowerSaveMode(oldUidState) != android.net.NetworkPolicyManager.isProcStateAllowedWhileIdleOrPowerSaveMode(newUidState);
            if (allowedWhileIdleOrPowerSaveModeChanged) {
                updateRuleForAppIdleUL(uid, procState);
                if (this.mDeviceIdleMode) {
                    updateRuleForDeviceIdleUL(uid);
                }
                if (this.mRestrictPower) {
                    updateRuleForRestrictPowerUL(uid);
                }
                updatePowerRestrictionRules = true;
            }
            if (this.mBackgroundNetworkRestricted) {
                boolean wasAllowed = android.net.NetworkPolicyManager.isProcStateAllowedNetworkWhileBackground(oldUidState);
                boolean isAllowed = android.net.NetworkPolicyManager.isProcStateAllowedNetworkWhileBackground(newUidState);
                if (!wasAllowed && isAllowed) {
                    this.mBackgroundTransitioningUids.delete(uid);
                    updateRuleForBackgroundUL(uid);
                    updatePowerRestrictionRules = true;
                } else if (!isAllowed) {
                    int transitionIdx = this.mBackgroundTransitioningUids.indexOfKey(uid);
                    long completionTimeMs = android.os.SystemClock.uptimeMillis() + getBackgroundTransitioningDelay(procState);
                    boolean completionTimeUpdated = false;
                    if (wasAllowed) {
                        if (transitionIdx < 0 && !isUidAllowedNetworkWhileBackground(uid)) {
                            this.mBackgroundTransitioningUids.put(uid, completionTimeMs);
                            completionTimeUpdated = true;
                        }
                    } else if (this.mUseDifferentDelaysForBackgroundChain && transitionIdx >= 0 && completionTimeMs < this.mBackgroundTransitioningUids.valueAt(transitionIdx)) {
                        this.mBackgroundTransitioningUids.setValueAt(transitionIdx, completionTimeMs);
                        completionTimeUpdated = true;
                    }
                    if (completionTimeUpdated && completionTimeMs < this.mNextProcessBackgroundUidsTime) {
                        this.mHandler.removeMessages(24);
                        this.mHandler.sendEmptyMessageAtTime(24, completionTimeMs);
                        this.mNextProcessBackgroundUidsTime = completionTimeMs;
                    }
                }
            }
            if (this.mLowPowerStandbyActive) {
                boolean allowedInLpsChanged = android.net.NetworkPolicyManager.isProcStateAllowedWhileInLowPowerStandby(oldUidState) != android.net.NetworkPolicyManager.isProcStateAllowedWhileInLowPowerStandby(newUidState);
                if (allowedInLpsChanged) {
                    updateRuleForLowPowerStandbyUL(uid);
                    updatePowerRestrictionRules = true;
                }
            }
            if (updatePowerRestrictionRules) {
                updateRulesForPowerRestrictionsUL(uid, procState);
            }
            android.os.Trace.traceEnd(2097152L);
            return true;
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(2097152L);
            throw th;
        }
    }

    private boolean removeUidStateUL(int uid) {
        int index = this.mUidState.indexOfKey(uid);
        if (index >= 0) {
            android.net.NetworkPolicyManager.UidState oldUidState = this.mUidState.valueAt(index);
            this.mUidState.removeAt(index);
            if (oldUidState != null) {
                updateRestrictBackgroundRulesOnUidStatusChangedUL(uid, oldUidState, null);
                if (this.mDeviceIdleMode) {
                    updateRuleForDeviceIdleUL(uid);
                }
                if (this.mRestrictPower) {
                    updateRuleForRestrictPowerUL(uid);
                }
                if (this.mBackgroundNetworkRestricted) {
                    this.mBackgroundTransitioningUids.delete(uid);
                    updateRuleForBackgroundUL(uid);
                }
                lambda$updateRulesForRestrictPowerUL$6(uid);
                if (this.mLowPowerStandbyActive) {
                    updateRuleForLowPowerStandbyUL(uid);
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void updateNetworkStats(int uid, boolean uidForeground) {
        if (android.os.Trace.isTagEnabled(2097152L)) {
            android.os.Trace.traceBegin(2097152L, "updateNetworkStats: " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + (uidForeground ? "F" : "B"));
        }
        try {
            this.mNetworkStats.noteUidForeground(uid, uidForeground);
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void updateRestrictBackgroundRulesOnUidStatusChangedUL(int uid, android.net.NetworkPolicyManager.UidState oldUidState, android.net.NetworkPolicyManager.UidState newUidState) {
        boolean oldForeground = android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(oldUidState);
        boolean newForeground = android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(newUidState);
        if (oldForeground != newForeground) {
            lambda$updateRulesForRestrictBackgroundUL$7(uid);
        }
    }

    boolean isRestrictedModeEnabled() {
        boolean z;
        synchronized (this.mUidRulesFirstLock) {
            z = this.mRestrictedNetworkingMode;
        }
        return z;
    }

    void updateRestrictedModeAllowlistUL() {
        this.mUidFirewallRestrictedModeRules.clear();
        forEachUid("updateRestrictedModeAllowlist", new java.util.function.IntConsumer() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda7
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                this.f$0.lambda$updateRestrictedModeAllowlistUL$4(i);
            }
        });
        if (this.mRestrictedNetworkingMode) {
            setUidFirewallRulesUL(4, this.mUidFirewallRestrictedModeRules);
        }
        enableFirewallChainUL(4, this.mRestrictedNetworkingMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRestrictedModeAllowlistUL$4(int uid) {
        synchronized (this.mUidRulesFirstLock) {
            int effectiveBlockedReasons = updateBlockedReasonsForRestrictedModeUL(uid);
            int newFirewallRule = getRestrictedModeFirewallRule(effectiveBlockedReasons);
            if (newFirewallRule != 0) {
                this.mUidFirewallRestrictedModeRules.append(uid, newFirewallRule);
            }
        }
    }

    void updateRestrictedModeForUidUL(int uid) {
        int effectiveBlockedReasons = updateBlockedReasonsForRestrictedModeUL(uid);
        if (this.mRestrictedNetworkingMode) {
            setUidFirewallRuleUL(4, uid, getRestrictedModeFirewallRule(effectiveBlockedReasons));
        }
    }

    private int updateBlockedReasonsForRestrictedModeUL(int uid) {
        int oldEffectiveBlockedReasons;
        int newEffectiveBlockedReasons;
        int iDeriveUidRules;
        int uidRules;
        boolean hasRestrictedModeAccess = hasRestrictedModeAccess(uid);
        synchronized (this.mUidBlockedState) {
            com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = getOrCreateUidBlockedStateForUid(this.mUidBlockedState, uid);
            oldEffectiveBlockedReasons = uidBlockedState.effectiveBlockedReasons;
            if (this.mRestrictedNetworkingMode) {
                uidBlockedState.blockedReasons |= 8;
            } else {
                uidBlockedState.blockedReasons &= -9;
            }
            if (hasRestrictedModeAccess) {
                uidBlockedState.allowedReasons |= 16;
            } else {
                uidBlockedState.allowedReasons &= -17;
            }
            uidBlockedState.updateEffectiveBlockedReasons();
            newEffectiveBlockedReasons = uidBlockedState.effectiveBlockedReasons;
            if (oldEffectiveBlockedReasons == newEffectiveBlockedReasons) {
                iDeriveUidRules = 0;
            } else {
                iDeriveUidRules = uidBlockedState.deriveUidRules();
            }
            uidRules = iDeriveUidRules;
        }
        if (oldEffectiveBlockedReasons != newEffectiveBlockedReasons) {
            handleBlockedReasonsChanged(uid, newEffectiveBlockedReasons, oldEffectiveBlockedReasons);
            postUidRulesChangedMsg(uid, uidRules);
        }
        return newEffectiveBlockedReasons;
    }

    private static int getRestrictedModeFirewallRule(int effectiveBlockedReasons) {
        if ((effectiveBlockedReasons & 8) != 0) {
            return 0;
        }
        return 1;
    }

    private boolean hasRestrictedModeAccess(int uid) {
        try {
            if (this.mIPm.checkUidPermission("android.permission.CONNECTIVITY_USE_RESTRICTED_NETWORKS", uid) != 0 && this.mIPm.checkUidPermission("android.permission.NETWORK_STACK", uid) != 0) {
                if (this.mIPm.checkUidPermission("android.permission.MAINLINE_NETWORK_STACK", uid) != 0) {
                    return false;
                }
            }
            return true;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    void updateRulesForPowerSaveUL() {
        android.os.Trace.traceBegin(2097152L, "updateRulesForPowerSaveUL");
        try {
            updateRulesForAllowlistedPowerSaveUL(this.mRestrictPower, 3, this.mUidFirewallPowerSaveRules);
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    void updateRuleForRestrictPowerUL(int uid) {
        updateRulesForAllowlistedPowerSaveUL(uid, this.mRestrictPower, 3);
    }

    void updateRulesForDeviceIdleUL() {
        android.os.Trace.traceBegin(2097152L, "updateRulesForDeviceIdleUL");
        try {
            updateRulesForAllowlistedPowerSaveUL(this.mDeviceIdleMode, 1, this.mUidFirewallDozableRules);
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    void updateRuleForDeviceIdleUL(int uid) {
        updateRulesForAllowlistedPowerSaveUL(uid, this.mDeviceIdleMode, 1);
    }

    private void updateRulesForAllowlistedPowerSaveUL(boolean enabled, int chain, android.util.SparseIntArray rules) {
        if (enabled) {
            rules.clear();
            java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
            for (int ui = users.size() - 1; ui >= 0; ui--) {
                android.content.pm.UserInfo user = users.get(ui);
                updateRulesForAllowlistedAppIds(rules, this.mPowerSaveTempWhitelistAppIds, user.id);
                updateRulesForAllowlistedAppIds(rules, this.mPowerSaveWhitelistAppIds, user.id);
                if (chain == 3) {
                    updateRulesForAllowlistedAppIds(rules, this.mPowerSaveWhitelistExceptIdleAppIds, user.id);
                }
            }
            for (int i = this.mUidState.size() - 1; i >= 0; i--) {
                if (android.net.NetworkPolicyManager.isProcStateAllowedWhileIdleOrPowerSaveMode(this.mUidState.valueAt(i))) {
                    rules.put(this.mUidState.keyAt(i), 1);
                }
            }
            setUidFirewallRulesUL(chain, rules, 1);
            return;
        }
        setUidFirewallRulesUL(chain, null, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRulesForBackgroundChainUL() {
        android.os.Trace.traceBegin(2097152L, "updateRulesForBackgroundChainUL");
        try {
            android.util.SparseIntArray uidRules = this.mUidFirewallBackgroundRules;
            uidRules.clear();
            java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
            for (int ui = users.size() - 1; ui >= 0; ui--) {
                android.content.pm.UserInfo user = users.get(ui);
                updateRulesForAllowlistedAppIds(uidRules, this.mPowerSaveTempWhitelistAppIds, user.id);
                updateRulesForAllowlistedAppIds(uidRules, this.mPowerSaveWhitelistAppIds, user.id);
                updateRulesForAllowlistedAppIds(uidRules, this.mPowerSaveWhitelistExceptIdleAppIds, user.id);
            }
            for (int i = this.mUidState.size() - 1; i >= 0; i--) {
                if (this.mBackgroundTransitioningUids.indexOfKey(this.mUidState.keyAt(i)) >= 0 || android.net.NetworkPolicyManager.isProcStateAllowedNetworkWhileBackground(this.mUidState.valueAt(i))) {
                    uidRules.put(this.mUidState.keyAt(i), 1);
                }
            }
            setUidFirewallRulesUL(6, uidRules);
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void updateRulesForAllowlistedAppIds(android.util.SparseIntArray uidRules, android.util.SparseBooleanArray allowlistedAppIds, int userId) {
        for (int i = allowlistedAppIds.size() - 1; i >= 0; i--) {
            if (allowlistedAppIds.valueAt(i)) {
                int appId = allowlistedAppIds.keyAt(i);
                int uid = android.os.UserHandle.getUid(userId, appId);
                uidRules.put(uid, 1);
            }
        }
    }

    void updateRulesForLowPowerStandbyUL() {
        android.os.Trace.traceBegin(2097152L, "updateRulesForLowPowerStandbyUL");
        try {
            if (this.mLowPowerStandbyActive) {
                this.mUidFirewallLowPowerStandbyModeRules.clear();
                for (int i = this.mUidState.size() - 1; i >= 0; i--) {
                    int uid = this.mUidState.keyAt(i);
                    int effectiveBlockedReasons = getEffectiveBlockedReasons(uid);
                    if (hasInternetPermissionUL(uid) && (effectiveBlockedReasons & 32) == 0) {
                        this.mUidFirewallLowPowerStandbyModeRules.put(uid, 1);
                    }
                }
                setUidFirewallRulesUL(5, this.mUidFirewallLowPowerStandbyModeRules, 1);
            } else {
                setUidFirewallRulesUL(5, null, 2);
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    void updateRuleForLowPowerStandbyUL(int uid) {
        if (!hasInternetPermissionUL(uid)) {
            return;
        }
        int effectiveBlockedReasons = getEffectiveBlockedReasons(uid);
        if (this.mUidState.contains(uid) && (effectiveBlockedReasons & 32) == 0) {
            this.mUidFirewallLowPowerStandbyModeRules.put(uid, 1);
            setUidFirewallRuleUL(5, uid, 1);
        } else {
            this.mUidFirewallLowPowerStandbyModeRules.delete(uid);
            setUidFirewallRuleUL(5, uid, 0);
        }
    }

    private boolean isAllowlistedFromPowerSaveUL(int uid, boolean deviceIdleMode) {
        int appId = android.os.UserHandle.getAppId(uid);
        boolean allowlisted = this.mPowerSaveTempWhitelistAppIds.get(appId) || this.mPowerSaveWhitelistAppIds.get(appId);
        if (!deviceIdleMode) {
            return allowlisted || isAllowlistedFromPowerSaveExceptIdleUL(uid);
        }
        return allowlisted;
    }

    private boolean isAllowlistedFromPowerSaveExceptIdleUL(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        return this.mPowerSaveWhitelistExceptIdleAppIds.get(appId);
    }

    private boolean isAllowlistedFromLowPowerStandbyUL(int uid) {
        return this.mLowPowerStandbyAllowlistUids.get(uid);
    }

    private void updateRulesForAllowlistedPowerSaveUL(int uid, boolean enabled, int chain) {
        if (enabled) {
            boolean isWhitelisted = isAllowlistedFromPowerSaveUL(uid, chain == 1);
            if (isWhitelisted || isUidForegroundOnRestrictPowerUL(uid)) {
                setUidFirewallRuleUL(chain, uid, 1);
            } else {
                setUidFirewallRuleUL(chain, uid, 0);
            }
        }
    }

    void updateRulesForAppIdleUL() {
        android.os.Trace.traceBegin(2097152L, "updateRulesForAppIdleUL");
        try {
            android.util.SparseIntArray uidRules = this.mUidFirewallStandbyRules.clone();
            uidRules.clear();
            java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
            for (int ui = users.size() - 1; ui >= 0; ui--) {
                android.content.pm.UserInfo user = users.get(ui);
                int[] idleUids = this.mUsageStats.getIdleUidsForUser(user.id);
                for (int uid : idleUids) {
                    if (!this.mPowerSaveTempWhitelistAppIds.get(android.os.UserHandle.getAppId(uid), false) && hasInternetPermissionUL(uid) && !isUidForegroundOnRestrictPowerUL(uid)) {
                        uidRules.put(uid, 2);
                    }
                }
            }
            setUidFirewallRulesUL(2, uidRules, 0);
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    void updateRuleForAppIdleUL(int uid, int uidProcessState) {
        if (isUidValidForDenylistRulesUL(uid)) {
            if (android.os.Trace.isTagEnabled(2097152L)) {
                android.os.Trace.traceBegin(2097152L, "updateRuleForAppIdleUL: " + uid);
            }
            try {
                int appId = android.os.UserHandle.getAppId(uid);
                if (!this.mPowerSaveTempWhitelistAppIds.get(appId) && isUidIdle(uid, uidProcessState) && !isUidForegroundOnRestrictPowerUL(uid)) {
                    setUidFirewallRuleUL(2, uid, 2);
                    if (LOGD) {
                        android.util.Log.d(TAG, "updateRuleForAppIdleUL DENY " + uid);
                    }
                } else {
                    setUidFirewallRuleUL(2, uid, 0);
                    if (LOGD) {
                        android.util.Log.d(TAG, "updateRuleForAppIdleUL " + uid + " to DEFAULT");
                    }
                }
            } finally {
                android.os.Trace.traceEnd(2097152L);
            }
        }
    }

    void updateRuleForBackgroundUL(int uid) {
        if (!isUidValidForAllowlistRulesUL(uid)) {
            return;
        }
        android.os.Trace.traceBegin(2097152L, "updateRuleForBackgroundUL: " + uid);
        try {
            if (isAllowlistedFromPowerSaveUL(uid, false) || isUidExemptFromBackgroundRestrictions(uid) || isUidAllowedNetworkWhileBackground(uid)) {
                setUidFirewallRuleUL(6, uid, 1);
                if (LOGD) {
                    android.util.Log.d(TAG, "updateRuleForBackgroundUL ALLOW " + uid);
                }
            } else {
                setUidFirewallRuleUL(6, uid, 0);
                if (LOGD) {
                    android.util.Log.d(TAG, "updateRuleForBackgroundUL " + uid + " to DEFAULT");
                }
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRulesForAppIdleParoleUL() {
        boolean zIsInParole = this.mAppStandby.isInParole();
        boolean z = !zIsInParole;
        int size = this.mUidFirewallStandbyRules.size();
        android.util.SparseIntArray sparseIntArray = new android.util.SparseIntArray();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            int iKeyAt = this.mUidFirewallStandbyRules.keyAt(i);
            if (isUidValidForDenylistRulesUL(iKeyAt)) {
                int blockedReasons = getBlockedReasons(iKeyAt);
                if (z || (65535 & blockedReasons) != 0) {
                    boolean z2 = !zIsInParole && isUidIdle(iKeyAt);
                    if (!z2 || this.mPowerSaveTempWhitelistAppIds.get(android.os.UserHandle.getAppId(iKeyAt)) || isUidForegroundOnRestrictPowerUL(iKeyAt)) {
                        this.mUidFirewallStandbyRules.put(iKeyAt, 0);
                    } else {
                        this.mUidFirewallStandbyRules.put(iKeyAt, 2);
                        sparseIntArray.put(iKeyAt, 2);
                    }
                    updateRulesForPowerRestrictionsUL(iKeyAt, z2);
                }
            }
            i++;
        }
        setUidFirewallRulesUL(2, sparseIntArray, z ? 1 : 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRulesForGlobalChangeAL(boolean restrictedNetworksChanged) {
        if (android.os.Trace.isTagEnabled(2097152L)) {
            android.os.Trace.traceBegin(2097152L, "updateRulesForGlobalChangeAL: " + (restrictedNetworksChanged ? "R" : "-"));
        }
        try {
            if (this.mBackgroundNetworkRestricted) {
                updateRulesForBackgroundChainUL();
            }
            updateRulesForAppIdleUL();
            updateRulesForRestrictPowerUL();
            updateRulesForRestrictBackgroundUL();
            updateRestrictedModeAllowlistUL();
            if (restrictedNetworksChanged) {
                normalizePoliciesNL();
                updateNetworkRulesNL();
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void handleDeviceIdleModeChangedUL(boolean enabled) {
        android.os.Trace.traceBegin(2097152L, "updateRulesForRestrictPowerUL");
        try {
            updateRulesForDeviceIdleUL();
            if (enabled) {
                forEachUid("updateRulesForRestrictPower", new java.util.function.IntConsumer() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda8
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i) {
                        this.f$0.lambda$handleDeviceIdleModeChangedUL$5(i);
                    }
                });
            } else {
                handleDeviceIdleModeDisabledUL();
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleDeviceIdleModeChangedUL$5(int uid) {
        synchronized (this.mUidRulesFirstLock) {
            lambda$updateRulesForRestrictPowerUL$6(uid);
        }
    }

    private void handleDeviceIdleModeDisabledUL() {
        android.os.Trace.traceBegin(2097152L, "handleDeviceIdleModeDisabledUL");
        try {
            android.util.SparseArray<com.android.internal.os.SomeArgs> uidStateUpdates = new android.util.SparseArray<>();
            synchronized (this.mUidBlockedState) {
                int size = this.mUidBlockedState.size();
                for (int i = 0; i < size; i++) {
                    int uid = this.mUidBlockedState.keyAt(i);
                    com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = this.mUidBlockedState.valueAt(i);
                    if ((uidBlockedState.blockedReasons & 2) != 0) {
                        uidBlockedState.blockedReasons &= -3;
                        int oldEffectiveBlockedReasons = uidBlockedState.effectiveBlockedReasons;
                        uidBlockedState.updateEffectiveBlockedReasons();
                        if (LOGV) {
                            android.util.Log.v(TAG, "handleDeviceIdleModeDisabled(" + uid + "); newUidBlockedState=" + uidBlockedState + ", oldEffectiveBlockedReasons=" + oldEffectiveBlockedReasons);
                        }
                        if (oldEffectiveBlockedReasons != uidBlockedState.effectiveBlockedReasons) {
                            com.android.internal.os.SomeArgs someArgs = com.android.internal.os.SomeArgs.obtain();
                            someArgs.argi1 = oldEffectiveBlockedReasons;
                            someArgs.argi2 = uidBlockedState.effectiveBlockedReasons;
                            someArgs.argi3 = uidBlockedState.deriveUidRules();
                            uidStateUpdates.append(uid, someArgs);
                            this.mActivityManagerInternal.onUidBlockedReasonsChanged(uid, uidBlockedState.effectiveBlockedReasons);
                        }
                    }
                }
            }
            if (uidStateUpdates.size() != 0) {
                this.mHandler.obtainMessage(23, uidStateUpdates).sendToTarget();
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRulesForRestrictPowerUL() {
        android.os.Trace.traceBegin(2097152L, "updateRulesForRestrictPowerUL");
        try {
            updateRulesForDeviceIdleUL();
            updateRulesForPowerSaveUL();
            forEachUid("updateRulesForRestrictPower", new java.util.function.IntConsumer() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.IntConsumer
                public final void accept(int i) {
                    this.f$0.lambda$updateRulesForRestrictPowerUL$6(i);
                }
            });
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void updateRulesForRestrictBackgroundUL() {
        android.os.Trace.traceBegin(2097152L, "updateRulesForRestrictBackgroundUL");
        try {
            forEachUid("updateRulesForRestrictBackground", new java.util.function.IntConsumer() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda5
                @Override // java.util.function.IntConsumer
                public final void accept(int i) {
                    this.f$0.lambda$updateRulesForRestrictBackgroundUL$7(i);
                }
            });
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forEachUid(java.lang.String tag, final java.util.function.IntConsumer consumer) {
        if (android.os.Trace.isTagEnabled(2097152L)) {
            android.os.Trace.traceBegin(2097152L, "forEachUid-" + tag);
        }
        try {
            android.os.Trace.traceBegin(2097152L, "list-users");
            java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
            android.os.Trace.traceEnd(2097152L);
            android.os.Trace.traceBegin(2097152L, "iterate-uids");
            android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            int usersSize = users.size();
            for (int i = 0; i < usersSize; i++) {
                final int userId = users.get(i).id;
                final android.util.SparseBooleanArray sharedAppIdsHandled = new android.util.SparseBooleanArray();
                packageManagerInternal.forEachInstalledPackage(new java.util.function.Consumer() { // from class: com.android.server.net.NetworkPolicyManagerService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.net.NetworkPolicyManagerService.lambda$forEachUid$8(sharedAppIdsHandled, userId, consumer, (com.android.server.pm.pkg.AndroidPackage) obj);
                    }
                }, userId);
            }
            android.os.Trace.traceEnd(2097152L);
        } catch (java.lang.Throwable th) {
            throw th;
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    static /* synthetic */ void lambda$forEachUid$8(android.util.SparseBooleanArray sharedAppIdsHandled, int userId, java.util.function.IntConsumer consumer, com.android.server.pm.pkg.AndroidPackage androidPackage) {
        int appId = androidPackage.getUid();
        if (androidPackage.getSharedUserId() != null) {
            if (sharedAppIdsHandled.indexOfKey(appId) < 0) {
                sharedAppIdsHandled.put(appId, true);
            } else {
                return;
            }
        }
        int uid = android.os.UserHandle.getUid(userId, appId);
        consumer.accept(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRulesForTempAllowlistChangeUL(int appId) {
        java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
        int numUsers = users.size();
        for (int i = 0; i < numUsers; i++) {
            android.content.pm.UserInfo user = users.get(i);
            int uid = android.os.UserHandle.getUid(user.id, appId);
            updateRuleForAppIdleUL(uid, -1);
            updateRuleForDeviceIdleUL(uid);
            updateRuleForRestrictPowerUL(uid);
            if (this.mBackgroundNetworkRestricted) {
                updateRuleForBackgroundUL(uid);
            }
            lambda$updateRulesForRestrictPowerUL$6(uid);
        }
    }

    private boolean isUidValidForDenylistRulesUL(int uid) {
        if (uid == 1013 || uid == 1019 || isUidValidForAllowlistRulesUL(uid)) {
            return true;
        }
        return false;
    }

    private boolean isUidValidForAllowlistRulesUL(int uid) {
        return (android.os.UserHandle.isApp(uid) || isCloneUidNL(uid)) && hasInternetPermissionUL(uid);
    }

    void setAppIdleWhitelist(int uid, boolean shouldWhitelist) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_NETWORK_POLICY", TAG);
        synchronized (this.mUidRulesFirstLock) {
            if (this.mAppIdleTempWhitelistAppIds.get(uid) == shouldWhitelist) {
                return;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                this.mLogger.appIdleWlChanged(uid, shouldWhitelist);
                if (shouldWhitelist) {
                    this.mAppIdleTempWhitelistAppIds.put(uid, true);
                } else {
                    this.mAppIdleTempWhitelistAppIds.delete(uid);
                }
                updateRuleForAppIdleUL(uid, -1);
                lambda$updateRulesForRestrictPowerUL$6(uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    int[] getAppIdleWhitelist() {
        int[] uids;
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_NETWORK_POLICY", TAG);
        synchronized (this.mUidRulesFirstLock) {
            int len = this.mAppIdleTempWhitelistAppIds.size();
            uids = new int[len];
            for (int i = 0; i < len; i++) {
                uids[i] = this.mAppIdleTempWhitelistAppIds.keyAt(i);
            }
        }
        return uids;
    }

    boolean isUidIdle(int uid) {
        return isUidIdle(uid, -1);
    }

    private boolean isUidIdle(int uid, int uidProcessState) {
        synchronized (this.mUidRulesFirstLock) {
            if (uidProcessState != -1) {
                if (android.app.ActivityManager.isProcStateConsideredInteraction(uidProcessState)) {
                    return false;
                }
            }
            if (this.mAppIdleTempWhitelistAppIds.get(uid)) {
                return false;
            }
            java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(uid);
            int userId = android.os.UserHandle.getUserId(uid);
            if (packages != null) {
                for (java.lang.String packageName : packages) {
                    if (!this.mUsageStats.isAppIdle(packageName, uid, userId)) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
    }

    private boolean hasInternetPermissionUL(int uid) {
        try {
            if (this.mInternetPermissionMap.get(uid)) {
                return true;
            }
            boolean hasPermission = this.mIPm.checkUidPermission("android.permission.INTERNET", uid) == 0;
            this.mInternetPermissionMap.put(uid, hasPermission);
            return hasPermission;
        } catch (android.os.RemoteException e) {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUidDeletedUL(int uid) {
        synchronized (this.mUidBlockedState) {
            this.mUidBlockedState.delete(uid);
        }
        this.mUidState.delete(uid);
        this.mActivityManagerInternal.onUidBlockedReasonsChanged(uid, 0);
        this.mUidPolicy.delete(uid);
        this.mUidFirewallStandbyRules.delete(uid);
        this.mUidFirewallDozableRules.delete(uid);
        this.mUidFirewallPowerSaveRules.delete(uid);
        this.mUidFirewallBackgroundRules.delete(uid);
        this.mBackgroundTransitioningUids.delete(uid);
        this.mPowerSaveWhitelistExceptIdleAppIds.delete(uid);
        this.mPowerSaveWhitelistAppIds.delete(uid);
        this.mPowerSaveTempWhitelistAppIds.delete(uid);
        this.mAppIdleTempWhitelistAppIds.delete(uid);
        this.mUidFirewallRestrictedModeRules.delete(uid);
        this.mUidFirewallLowPowerStandbyModeRules.delete(uid);
        synchronized (this.mUidStateCallbackInfos) {
            this.mUidStateCallbackInfos.remove(uid);
        }
        this.mHandler.obtainMessage(15, uid, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRestrictionRulesForUidUL(int uid) {
        updateRuleForDeviceIdleUL(uid);
        updateRuleForAppIdleUL(uid, -1);
        updateRuleForRestrictPowerUL(uid);
        if (this.mBackgroundNetworkRestricted) {
            updateRuleForBackgroundUL(uid);
        }
        updateRestrictedModeForUidUL(uid);
        lambda$updateRulesForRestrictPowerUL$6(uid);
        lambda$updateRulesForRestrictBackgroundUL$7(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updateRulesForDataUsageRestrictionsUL, reason: merged with bridge method [inline-methods] */
    public void lambda$updateRulesForRestrictBackgroundUL$7(int uid) {
        if (android.os.Trace.isTagEnabled(2097152L)) {
            android.os.Trace.traceBegin(2097152L, "updateRulesForDataUsageRestrictionsUL: " + uid);
        }
        try {
            updateRulesForDataUsageRestrictionsULInner(uid);
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void updateRulesForDataUsageRestrictionsULInner(int uid) throws java.lang.Throwable {
        int oldAllowedReasons;
        int i;
        if (!isUidValidForAllowlistRulesUL(uid)) {
            if (LOGD) {
                android.util.Slog.d(TAG, "no need to update restrict data rules for uid " + uid);
                return;
            }
            return;
        }
        int uidPolicy = this.mUidPolicy.get(uid, 0);
        boolean isForeground = isUidForegroundOnRestrictBackgroundUL(uid);
        boolean isRestrictedByAdmin = isRestrictedByAdminUL(uid);
        boolean isDenied = (uidPolicy & 1) != 0;
        boolean isAllowed = (uidPolicy & 4) != 0;
        int newBlockedReasons = (isDenied ? 131072 : 0) | 0 | (isRestrictedByAdmin ? 262144 : 0) | (this.mRestrictBackground ? 65536 : 0);
        int newAllowedReasons = (isSystem(uid) ? 131072 : 0) | 0 | (isForeground ? 262144 : 0) | (isAllowed ? 65536 : 0);
        synchronized (this.mUidBlockedState) {
            try {
                try {
                    com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = getOrCreateUidBlockedStateForUid(this.mUidBlockedState, uid);
                    com.android.server.net.NetworkPolicyManagerService.UidBlockedState previousUidBlockedState = getOrCreateUidBlockedStateForUid(this.mTmpUidBlockedState, uid);
                    previousUidBlockedState.copyFrom(uidBlockedState);
                    uidBlockedState.blockedReasons = (uidBlockedState.blockedReasons & 65535) | newBlockedReasons;
                    uidBlockedState.allowedReasons = (uidBlockedState.allowedReasons & 65535) | newAllowedReasons;
                    uidBlockedState.updateEffectiveBlockedReasons();
                    int oldEffectiveBlockedReasons = previousUidBlockedState.effectiveBlockedReasons;
                    int newEffectiveBlockedReasons = uidBlockedState.effectiveBlockedReasons;
                    int oldAllowedReasons2 = previousUidBlockedState.allowedReasons;
                    int uidRules = oldEffectiveBlockedReasons == newEffectiveBlockedReasons ? 0 : uidBlockedState.deriveUidRules();
                    if (!LOGV) {
                        oldAllowedReasons = oldAllowedReasons2;
                    } else {
                        oldAllowedReasons = oldAllowedReasons2;
                        android.util.Log.v(TAG, "updateRuleForRestrictBackgroundUL(" + uid + "): isForeground=" + isForeground + ", isDenied=" + isDenied + ", isAllowed=" + isAllowed + ", isRestrictedByAdmin=" + isRestrictedByAdmin + ", oldBlockedState=" + previousUidBlockedState + ", newBlockedState=" + uidBlockedState + ", newBlockedMeteredReasons=" + android.net.NetworkPolicyManager.blockedReasonsToString(newBlockedReasons) + ", newAllowedMeteredReasons=" + android.net.NetworkPolicyManager.allowedReasonsToString(newAllowedReasons));
                    }
                    if (oldEffectiveBlockedReasons != newEffectiveBlockedReasons) {
                        handleBlockedReasonsChanged(uid, newEffectiveBlockedReasons, oldEffectiveBlockedReasons);
                        postUidRulesChangedMsg(uid, uidRules);
                    }
                    if (this.mUseMeteredFirewallChains) {
                        if ((262144 & newEffectiveBlockedReasons) != 0) {
                            setUidFirewallRuleUL(12, uid, 2);
                            i = 0;
                        } else {
                            i = 0;
                            setUidFirewallRuleUL(12, uid, 0);
                        }
                        if ((131072 & newEffectiveBlockedReasons) != 0) {
                            setUidFirewallRuleUL(11, uid, 2);
                        } else {
                            setUidFirewallRuleUL(11, uid, i);
                        }
                        if ((327680 & newAllowedReasons) != 0) {
                            setUidFirewallRuleUL(10, uid, 1);
                            return;
                        } else {
                            setUidFirewallRuleUL(10, uid, i);
                            return;
                        }
                    }
                    if ((oldEffectiveBlockedReasons & 393216) != 0 || (newEffectiveBlockedReasons & 393216) != 0) {
                        setMeteredNetworkDenylist(uid, (393216 & newEffectiveBlockedReasons) != 0);
                    }
                    if ((oldAllowedReasons & 327680) != 0 || (newAllowedReasons & 327680) != 0) {
                        setMeteredNetworkAllowlist(uid, (327680 & newAllowedReasons) != 0);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updateRulesForPowerRestrictionsUL, reason: merged with bridge method [inline-methods] */
    public void lambda$updateRulesForRestrictPowerUL$6(int uid) {
        updateRulesForPowerRestrictionsUL(uid, -1);
    }

    private void updateRulesForPowerRestrictionsUL(int uid, int uidProcState) {
        updateRulesForPowerRestrictionsUL(uid, isUidIdle(uid, uidProcState));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRulesForPowerRestrictionsUL(int uid, boolean isUidIdle) {
        if (android.os.Trace.isTagEnabled(2097152L)) {
            android.os.Trace.traceBegin(2097152L, "updateRulesForPowerRestrictionsUL: " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + (isUidIdle ? "I" : "-"));
        }
        try {
            updateRulesForPowerRestrictionsULInner(uid, isUidIdle);
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    private void updateRulesForPowerRestrictionsULInner(int i, boolean z) {
        int i2;
        int i3;
        int iDeriveUidRules;
        int i4;
        if (isUidValidForDenylistRulesUL(i)) {
            if (z || this.mRestrictPower || !this.mDeviceIdleMode) {
            }
            boolean zIsUidForegroundOnRestrictPowerUL = isUidForegroundOnRestrictPowerUL(i);
            boolean zIsUidTop = isUidTop(i);
            boolean zIsAllowlistedFromPowerSaveUL = isAllowlistedFromPowerSaveUL(i, this.mDeviceIdleMode);
            synchronized (this.mUidBlockedState) {
                com.android.server.net.NetworkPolicyManagerService.UidBlockedState orCreateUidBlockedStateForUid = getOrCreateUidBlockedStateForUid(this.mUidBlockedState, i);
                com.android.server.net.NetworkPolicyManagerService.UidBlockedState orCreateUidBlockedStateForUid2 = getOrCreateUidBlockedStateForUid(this.mTmpUidBlockedState, i);
                orCreateUidBlockedStateForUid2.copyFrom(orCreateUidBlockedStateForUid);
                int i5 = 2;
                int i6 = 32;
                int i7 = 4;
                int i8 = 8;
                int i9 = 64;
                int i10 = (orCreateUidBlockedStateForUid.blockedReasons & 8) | 0 | (this.mRestrictPower ? 1 : 0) | (this.mDeviceIdleMode ? 2 : 0) | (this.mLowPowerStandbyActive ? 32 : 0) | (z ? 4 : 0) | (this.mBackgroundNetworkRestricted ? 64 : 0);
                int i11 = (isSystem(i) ? 1 : 0) | 0;
                if (!zIsUidForegroundOnRestrictPowerUL) {
                    i5 = 0;
                }
                int i12 = i11 | i5;
                if (!zIsUidTop) {
                    i6 = 0;
                }
                int i13 = i12 | i6;
                if (!isAllowlistedFromPowerSaveUL(i, true)) {
                    i7 = 0;
                }
                int i14 = i13 | i7;
                if (!isAllowlistedFromPowerSaveExceptIdleUL(i)) {
                    i8 = 0;
                }
                int i15 = i14 | i8 | (orCreateUidBlockedStateForUid.allowedReasons & 16);
                if (!isAllowlistedFromLowPowerStandbyUL(i)) {
                    i9 = 0;
                }
                int i16 = i15 | i9 | ((this.mBackgroundNetworkRestricted && isUidExemptFromBackgroundRestrictions(i)) ? 128 : 0);
                orCreateUidBlockedStateForUid.blockedReasons = (orCreateUidBlockedStateForUid.blockedReasons & (-65536)) | i10;
                orCreateUidBlockedStateForUid.allowedReasons = (orCreateUidBlockedStateForUid.allowedReasons & (-65536)) | i16;
                orCreateUidBlockedStateForUid.updateEffectiveBlockedReasons();
                if (LOGV) {
                    android.util.Log.v(TAG, "updateRulesForPowerRestrictionsUL(" + i + "), isIdle: " + z + ", mRestrictPower: " + this.mRestrictPower + ", mDeviceIdleMode: " + this.mDeviceIdleMode + ", isForeground=" + zIsUidForegroundOnRestrictPowerUL + ", isTop=" + zIsUidTop + ", isWhitelisted=" + zIsAllowlistedFromPowerSaveUL + ", oldUidBlockedState=" + orCreateUidBlockedStateForUid2 + ", newUidBlockedState=" + orCreateUidBlockedStateForUid);
                }
                i2 = orCreateUidBlockedStateForUid2.effectiveBlockedReasons;
                i3 = orCreateUidBlockedStateForUid.effectiveBlockedReasons;
                if (i2 == i3) {
                    iDeriveUidRules = 0;
                } else {
                    iDeriveUidRules = orCreateUidBlockedStateForUid.deriveUidRules();
                }
                i4 = iDeriveUidRules;
            }
            if (i2 != i3) {
                handleBlockedReasonsChanged(i, i3, i2);
                postUidRulesChangedMsg(i, i4);
                return;
            }
            return;
        }
        if (LOGD) {
            android.util.Slog.d(TAG, "no need to update restrict power rules for uid " + i);
        }
    }

    private class NetPolicyAppIdleStateChangeListener extends com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener {
        private NetPolicyAppIdleStateChangeListener() {
        }

        public void onAppIdleStateChanged(java.lang.String packageName, int userId, boolean idle, int bucket, int reason) {
            try {
                int uid = com.android.server.net.NetworkPolicyManagerService.this.mContext.getPackageManager().getPackageUidAsUser(packageName, 8192, userId);
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                    com.android.server.net.NetworkPolicyManagerService.this.mLogger.appIdleStateChanged(uid, idle);
                    com.android.server.net.NetworkPolicyManagerService.this.updateRuleForAppIdleUL(uid, -1);
                    com.android.server.net.NetworkPolicyManagerService.this.lambda$updateRulesForRestrictPowerUL$6(uid);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }

        public void onParoleStateChanged(boolean isParoleOn) {
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                com.android.server.net.NetworkPolicyManagerService.this.mLogger.paroleStateChanged(isParoleOn);
                com.android.server.net.NetworkPolicyManagerService.this.updateRulesForAppIdleParoleUL();
            }
        }
    }

    private void handleBlockedReasonsChanged(int uid, int newEffectiveBlockedReasons, int oldEffectiveBlockedReasons) {
        this.mActivityManagerInternal.onUidBlockedReasonsChanged(uid, newEffectiveBlockedReasons);
        postBlockedReasonsChangedMsg(uid, newEffectiveBlockedReasons, oldEffectiveBlockedReasons);
    }

    private void postBlockedReasonsChangedMsg(int uid, int newEffectiveBlockedReasons, int oldEffectiveBlockedReasons) {
        this.mHandler.obtainMessage(21, uid, newEffectiveBlockedReasons, java.lang.Integer.valueOf(oldEffectiveBlockedReasons)).sendToTarget();
    }

    private void postUidRulesChangedMsg(int uid, int uidRules) {
        this.mHandler.obtainMessage(1, uid, uidRules).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUidRulesChanged(android.net.INetworkPolicyListener listener, int uid, int uidRules) {
        try {
            listener.onUidRulesChanged(uid, uidRules);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchMeteredIfacesChanged(android.net.INetworkPolicyListener listener, java.lang.String[] meteredIfaces) {
        try {
            listener.onMeteredIfacesChanged(meteredIfaces);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchRestrictBackgroundChanged(android.net.INetworkPolicyListener listener, boolean restrictBackground) {
        try {
            listener.onRestrictBackgroundChanged(restrictBackground);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUidPoliciesChanged(android.net.INetworkPolicyListener listener, int uid, int uidPolicies) {
        try {
            listener.onUidPoliciesChanged(uid, uidPolicies);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSubscriptionOverride(android.net.INetworkPolicyListener listener, int subId, int overrideMask, int overrideValue, int[] networkTypes) {
        try {
            listener.onSubscriptionOverride(subId, overrideMask, overrideValue, networkTypes);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSubscriptionPlansChanged(android.net.INetworkPolicyListener listener, int subId, android.telephony.SubscriptionPlan[] plans) {
        try {
            listener.onSubscriptionPlansChanged(subId, plans);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchBlockedReasonChanged(android.net.INetworkPolicyListener listener, int uid, int oldBlockedReasons, int newBlockedReasons) {
        try {
            listener.onBlockedReasonChanged(uid, oldBlockedReasons, newBlockedReasons);
        } catch (android.os.RemoteException e) {
        }
    }

    void handleUidChanged(int uid) {
        boolean updated;
        android.os.Trace.traceBegin(2097152L, "onUidStateChanged");
        try {
            synchronized (this.mUidStateCallbackInfos) {
                com.android.server.net.NetworkPolicyManagerService.UidStateCallbackInfo uidStateCallbackInfo = this.mUidStateCallbackInfos.get(uid);
                if (uidStateCallbackInfo == null) {
                    return;
                }
                int procState = uidStateCallbackInfo.procState;
                long procStateSeq = uidStateCallbackInfo.procStateSeq;
                int capability = uidStateCallbackInfo.capability;
                uidStateCallbackInfo.isPending = false;
                synchronized (this.mUidRulesFirstLock) {
                    this.mLogger.uidStateChanged(uid, procState, procStateSeq, capability);
                    updated = updateUidStateUL(uid, procState, procStateSeq, capability);
                    this.mActivityManagerInternal.notifyNetworkPolicyRulesUpdated(uid, procStateSeq);
                }
                if (updated) {
                    updateNetworkStats(uid, android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(procState, capability));
                }
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    void handleUidGone(int uid) {
        boolean updated;
        android.os.Trace.traceBegin(2097152L, "onUidGone");
        try {
            synchronized (this.mUidStateCallbackInfos) {
                if (this.mUidStateCallbackInfos.contains(uid)) {
                    return;
                }
                synchronized (this.mUidRulesFirstLock) {
                    updated = removeUidStateUL(uid);
                }
                if (updated) {
                    updateNetworkStats(uid, false);
                }
            }
        } finally {
            android.os.Trace.traceEnd(2097152L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastRestrictBackgroundChanged(int uid, java.lang.Boolean changed) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.lang.String[] packages = pm.getPackagesForUid(uid);
        if (packages != null) {
            int userId = android.os.UserHandle.getUserId(uid);
            for (java.lang.String packageName : packages) {
                android.content.Intent intent = new android.content.Intent("android.net.conn.RESTRICT_BACKGROUND_CHANGED");
                intent.setPackage(packageName);
                intent.setFlags(1073741824);
                this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(userId));
            }
        }
    }

    private static final class IfaceQuotas {
        public final java.lang.String iface;
        public final long limit;
        public final long warning;

        private IfaceQuotas(java.lang.String iface, long warning, long limit) {
            this.iface = iface;
            this.warning = warning;
            this.limit = limit;
        }
    }

    private void setInterfaceQuotasAsync(java.lang.String iface, long warningBytes, long limitBytes) {
        this.mHandler.obtainMessage(10, new com.android.server.net.NetworkPolicyManagerService.IfaceQuotas(iface, warningBytes, limitBytes)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInterfaceLimit(java.lang.String iface, long limitBytes) {
        try {
            this.mNetworkManager.setInterfaceQuota(iface, limitBytes);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.IllegalStateException e2) {
            android.util.Log.wtf(TAG, "problem setting interface quota", e2);
        }
    }

    private void removeInterfaceQuotasAsync(java.lang.String iface) {
        this.mHandler.obtainMessage(11, iface).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeInterfaceLimit(java.lang.String iface) {
        try {
            this.mNetworkManager.removeInterfaceQuota(iface);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.IllegalStateException e2) {
            android.util.Log.wtf(TAG, "problem removing interface quota", e2);
        }
    }

    private void setMeteredNetworkDenylist(int uid, boolean enable) {
        if (LOGV) {
            android.util.Slog.v(TAG, "setMeteredNetworkDenylist " + uid + ": " + enable);
        }
        try {
            this.mNetworkManager.setUidOnMeteredNetworkDenylist(uid, enable);
            this.mLogger.meteredDenylistChanged(uid, enable);
            if (android.os.Process.isApplicationUid(uid)) {
                int sdkSandboxUid = android.os.Process.toSdkSandboxUid(uid);
                this.mNetworkManager.setUidOnMeteredNetworkDenylist(sdkSandboxUid, enable);
                this.mLogger.meteredDenylistChanged(sdkSandboxUid, enable);
            }
        } catch (android.os.RemoteException e) {
        } catch (java.lang.IllegalStateException e2) {
            android.util.Log.wtf(TAG, "problem setting denylist (" + enable + ") rules for " + uid, e2);
        }
    }

    private void setMeteredNetworkAllowlist(int uid, boolean enable) {
        if (LOGV) {
            android.util.Slog.v(TAG, "setMeteredNetworkAllowlist " + uid + ": " + enable);
        }
        try {
            this.mNetworkManager.setUidOnMeteredNetworkAllowlist(uid, enable);
            this.mLogger.meteredAllowlistChanged(uid, enable);
            if (android.os.Process.isApplicationUid(uid)) {
                int sdkSandboxUid = android.os.Process.toSdkSandboxUid(uid);
                this.mNetworkManager.setUidOnMeteredNetworkAllowlist(sdkSandboxUid, enable);
                this.mLogger.meteredAllowlistChanged(sdkSandboxUid, enable);
            }
        } catch (android.os.RemoteException e) {
        } catch (java.lang.IllegalStateException e2) {
            android.util.Log.wtf(TAG, "problem setting allowlist (" + enable + ") rules for " + uid, e2);
        }
    }

    private void setUidFirewallRulesUL(int chain, android.util.SparseIntArray uidRules, int toggle) {
        if (uidRules != null) {
            setUidFirewallRulesUL(chain, uidRules);
        }
        if (toggle != 0) {
            enableFirewallChainUL(chain, toggle == 1);
        }
    }

    private void addSdkSandboxUidsIfNeeded(android.util.SparseIntArray uidRules) {
        int size = uidRules.size();
        android.util.SparseIntArray sdkSandboxUids = new android.util.SparseIntArray();
        for (int index = 0; index < size; index++) {
            int uid = uidRules.keyAt(index);
            int rule = uidRules.valueAt(index);
            if (android.os.Process.isApplicationUid(uid)) {
                sdkSandboxUids.put(android.os.Process.toSdkSandboxUid(uid), rule);
            }
        }
        for (int index2 = 0; index2 < sdkSandboxUids.size(); index2++) {
            int uid2 = sdkSandboxUids.keyAt(index2);
            int rule2 = sdkSandboxUids.valueAt(index2);
            uidRules.put(uid2, rule2);
        }
    }

    private void setUidFirewallRulesUL(int chain, android.util.SparseIntArray uidRules) {
        addSdkSandboxUidsIfNeeded(uidRules);
        try {
            int size = uidRules.size();
            int[] uids = new int[size];
            int[] rules = new int[size];
            for (int index = size - 1; index >= 0; index--) {
                uids[index] = uidRules.keyAt(index);
                rules[index] = uidRules.valueAt(index);
            }
            this.mNetworkManager.setFirewallUidRules(chain, uids, rules);
            this.mLogger.firewallRulesChanged(chain, uids, rules);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.IllegalStateException e2) {
            android.util.Log.wtf(TAG, "problem setting firewall uid rules", e2);
        }
    }

    private void setUidFirewallRuleUL(int chain, int uid, int rule) {
        if (android.os.Trace.isTagEnabled(2097152L)) {
            android.os.Trace.traceBegin(2097152L, "setUidFirewallRuleUL: " + chain + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + rule);
        }
        try {
            if (chain == 1) {
                this.mUidFirewallDozableRules.put(uid, rule);
            } else if (chain == 2) {
                this.mUidFirewallStandbyRules.put(uid, rule);
            } else if (chain == 3) {
                this.mUidFirewallPowerSaveRules.put(uid, rule);
            } else if (chain == 4) {
                this.mUidFirewallRestrictedModeRules.put(uid, rule);
            } else if (chain == 5) {
                this.mUidFirewallLowPowerStandbyModeRules.put(uid, rule);
            } else if (chain == 6) {
                this.mUidFirewallBackgroundRules.put(uid, rule);
            }
            try {
                this.mNetworkManager.setFirewallUidRule(chain, uid, rule);
                this.mLogger.uidFirewallRuleChanged(chain, uid, rule);
                if (android.os.Process.isApplicationUid(uid)) {
                    int sdkSandboxUid = android.os.Process.toSdkSandboxUid(uid);
                    this.mNetworkManager.setFirewallUidRule(chain, sdkSandboxUid, rule);
                    this.mLogger.uidFirewallRuleChanged(chain, sdkSandboxUid, rule);
                }
            } catch (android.os.RemoteException e) {
            } catch (java.lang.IllegalStateException e2) {
                android.util.Log.wtf(TAG, "problem setting firewall uid rules", e2);
            }
            android.os.Trace.traceEnd(2097152L);
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(2097152L);
            throw th;
        }
    }

    private void enableFirewallChainUL(int chain, boolean enable) {
        if (this.mFirewallChainStates.indexOfKey(chain) >= 0 && this.mFirewallChainStates.get(chain) == enable) {
            return;
        }
        this.mFirewallChainStates.put(chain, enable);
        try {
            this.mNetworkManager.setFirewallChainEnabled(chain, enable);
            this.mLogger.firewallChainEnabled(chain, enable);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.IllegalStateException e2) {
            android.util.Log.wtf(TAG, "problem enable firewall chain", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetUidFirewallRules(int uid) {
        try {
            this.mNetworkManager.setFirewallUidRule(1, uid, 0);
            this.mNetworkManager.setFirewallUidRule(2, uid, 0);
            this.mNetworkManager.setFirewallUidRule(3, uid, 0);
            this.mNetworkManager.setFirewallUidRule(4, uid, 0);
            this.mNetworkManager.setFirewallUidRule(5, uid, 0);
            this.mNetworkManager.setFirewallUidRule(6, uid, 0);
            if (this.mUseMeteredFirewallChains) {
                this.mNetworkManager.setFirewallUidRule(12, uid, 0);
                this.mNetworkManager.setFirewallUidRule(11, uid, 0);
                this.mNetworkManager.setFirewallUidRule(10, uid, 0);
            } else {
                this.mNetworkManager.setUidOnMeteredNetworkAllowlist(uid, false);
                this.mLogger.meteredAllowlistChanged(uid, false);
                this.mNetworkManager.setUidOnMeteredNetworkDenylist(uid, false);
                this.mLogger.meteredDenylistChanged(uid, false);
            }
        } catch (android.os.RemoteException e) {
        } catch (java.lang.IllegalStateException e2) {
            android.util.Log.wtf(TAG, "problem resetting firewall uid rules for " + uid, e2);
        }
        if (android.os.Process.isApplicationUid(uid)) {
            resetUidFirewallRules(android.os.Process.toSdkSandboxUid(uid));
        }
    }

    @java.lang.Deprecated
    private long getTotalBytes(android.net.NetworkTemplate template, long start, long end) {
        if (this.mStatsCallback.isAnyCallbackReceived()) {
            return this.mDeps.getNetworkTotalBytes(template, start, end);
        }
        return 0L;
    }

    private boolean isBandwidthControlEnabled() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            boolean zIsBandwidthControlEnabled = this.mNetworkManager.isBandwidthControlEnabled();
            android.os.Binder.restoreCallingIdentity(token);
            return zIsBandwidthControlEnabled;
        } catch (android.os.RemoteException e) {
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private static android.content.Intent buildSnoozeWarningIntent(android.net.NetworkTemplate template, java.lang.String targetPackage) {
        android.content.Intent intent = new android.content.Intent(ACTION_SNOOZE_WARNING);
        intent.addFlags(268435456);
        intent.putExtra("android.net.NETWORK_TEMPLATE", (android.os.Parcelable) template);
        intent.setPackage(targetPackage);
        return intent;
    }

    private static android.content.Intent buildSnoozeRapidIntent(android.net.NetworkTemplate template, java.lang.String targetPackage) {
        android.content.Intent intent = new android.content.Intent(ACTION_SNOOZE_RAPID);
        intent.addFlags(268435456);
        intent.putExtra("android.net.NETWORK_TEMPLATE", (android.os.Parcelable) template);
        intent.setPackage(targetPackage);
        return intent;
    }

    private static android.content.Intent buildNetworkOverLimitIntent(android.content.res.Resources res, android.net.NetworkTemplate template) {
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(android.content.ComponentName.unflattenFromString(res.getString(android.R.string.config_packagedKeyboardName)));
        intent.addFlags(268435456);
        intent.putExtra("android.net.NETWORK_TEMPLATE", (android.os.Parcelable) template);
        intent.putExtra("extra_alert_type", "extra_alert_type_month");
        return intent;
    }

    private static android.content.Intent buildViewDataUsageIntent(android.content.res.Resources res, android.net.NetworkTemplate template) {
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(android.content.ComponentName.unflattenFromString(res.getString(android.R.string.config_defaultAutofillService)));
        intent.addFlags(268435456);
        intent.putExtra("android.net.NETWORK_TEMPLATE", (android.os.Parcelable) template);
        return intent;
    }

    void addIdleHandler(android.os.MessageQueue.IdleHandler handler) {
        this.mHandler.getLooper().getQueue().addIdleHandler(handler);
    }

    void updateRestrictBackgroundByLowPowerModeUL(android.os.PowerSaveState result) {
        boolean shouldInvokeRestrictBackground;
        if (this.mRestrictBackgroundLowPowerMode == result.batterySaverEnabled) {
            return;
        }
        this.mRestrictBackgroundLowPowerMode = result.batterySaverEnabled;
        boolean restrictBackground = this.mRestrictBackgroundLowPowerMode;
        boolean localRestrictBgChangedInBsm = this.mRestrictBackgroundChangedInBsm;
        if (this.mRestrictBackgroundLowPowerMode) {
            shouldInvokeRestrictBackground = !this.mRestrictBackground;
            this.mRestrictBackgroundBeforeBsm = this.mRestrictBackground;
            localRestrictBgChangedInBsm = false;
        } else {
            boolean shouldInvokeRestrictBackground2 = this.mRestrictBackgroundChangedInBsm;
            shouldInvokeRestrictBackground = !shouldInvokeRestrictBackground2;
            restrictBackground = this.mRestrictBackgroundBeforeBsm;
        }
        if (shouldInvokeRestrictBackground) {
            setRestrictBackgroundUL(restrictBackground, "low_power");
        }
        this.mRestrictBackgroundChangedInBsm = localRestrictBgChangedInBsm;
    }

    private static void collectKeys(android.util.SparseIntArray source, android.util.SparseBooleanArray target) {
        int size = source.size();
        for (int i = 0; i < size; i++) {
            target.put(source.keyAt(i), true);
        }
    }

    private static <T> void collectKeys(android.util.SparseArray<T> source, android.util.SparseBooleanArray target) {
        int size = source.size();
        for (int i = 0; i < size; i++) {
            target.put(source.keyAt(i), true);
        }
    }

    public void factoryReset(java.lang.String subscriber) {
        android.net.NetworkTemplate templateCarrier;
        factoryReset_enforcePermission();
        if (this.mUserManager.hasUserRestriction("no_network_reset")) {
            return;
        }
        android.net.NetworkPolicy[] policies = getNetworkPolicies(this.mContext.getOpPackageName());
        android.net.NetworkTemplate templateMobile = null;
        if (subscriber == null) {
            templateCarrier = null;
        } else {
            templateCarrier = buildTemplateCarrierMetered(subscriber);
        }
        if (subscriber != null) {
            templateMobile = new android.net.NetworkTemplate.Builder(1).setSubscriberIds(java.util.Set.of(subscriber)).setMeteredness(1).build();
        }
        for (android.net.NetworkPolicy policy : policies) {
            if (policy.template.equals(templateCarrier) || policy.template.equals(templateMobile)) {
                policy.limitBytes = -1L;
                policy.inferred = false;
                policy.clearSnooze();
            }
        }
        setNetworkPolicies(policies);
        setRestrictBackground(false);
        if (!this.mUserManager.hasUserRestriction("no_control_apps")) {
            for (int uid : getUidsWithPolicy(1)) {
                setUidPolicy(uid, 0);
            }
        }
    }

    public boolean isUidNetworkingBlocked(int uid, boolean isNetworkMetered) {
        int blockedReasons;
        long startTime = this.mStatLogger.getTime();
        this.mContext.enforceCallingOrSelfPermission("android.permission.OBSERVE_NETWORK_POLICY", TAG);
        synchronized (this.mUidBlockedState) {
            com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = this.mUidBlockedState.get(uid);
            blockedReasons = uidBlockedState == null ? 0 : uidBlockedState.effectiveBlockedReasons;
            if (!isNetworkMetered) {
                blockedReasons &= 65535;
            }
            this.mLogger.networkBlocked(uid, uidBlockedState);
        }
        this.mStatLogger.logDurationStat(1, startTime);
        return blockedReasons != 0;
    }

    public boolean isUidRestrictedOnMeteredNetworks(int uid) {
        boolean z;
        isUidRestrictedOnMeteredNetworks_enforcePermission();
        synchronized (this.mUidBlockedState) {
            com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = this.mUidBlockedState.get(uid);
            int blockedReasons = uidBlockedState == null ? 0 : uidBlockedState.effectiveBlockedReasons;
            z = (blockedReasons & (-65536)) != 0;
        }
        return z;
    }

    private static boolean isSystem(int uid) {
        return uid < 10000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class NetworkPolicyManagerInternalImpl extends com.android.server.net.NetworkPolicyManagerInternal {
        private NetworkPolicyManagerInternalImpl() {
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void resetUserState(int userId) {
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                boolean z = true;
                boolean changed = com.android.server.net.NetworkPolicyManagerService.this.removeUserStateUL(userId, false, true);
                if (!com.android.server.net.NetworkPolicyManagerService.this.addDefaultRestrictBackgroundAllowlistUidsUL(userId) && !changed) {
                    z = false;
                }
                boolean changed2 = z;
                if (changed2) {
                    synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                        com.android.server.net.NetworkPolicyManagerService.this.writePolicyAL();
                    }
                }
            }
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void onTempPowerSaveWhitelistChange(int appId, boolean added, int reasonCode, java.lang.String reason) {
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                if (com.android.server.net.NetworkPolicyManagerService.this.mSystemReady) {
                    com.android.server.net.NetworkPolicyManagerService.this.mLogger.tempPowerSaveWlChanged(appId, added, reasonCode, reason);
                    if (added) {
                        com.android.server.net.NetworkPolicyManagerService.this.mPowerSaveTempWhitelistAppIds.put(appId, true);
                    } else {
                        com.android.server.net.NetworkPolicyManagerService.this.mPowerSaveTempWhitelistAppIds.delete(appId);
                    }
                    com.android.server.net.NetworkPolicyManagerService.this.updateRulesForTempAllowlistChangeUL(appId);
                }
            }
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public android.telephony.SubscriptionPlan getSubscriptionPlan(android.net.Network network) {
            android.telephony.SubscriptionPlan primarySubscriptionPlanLocked;
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                int subId = com.android.server.net.NetworkPolicyManagerService.this.getSubIdLocked(network);
                primarySubscriptionPlanLocked = com.android.server.net.NetworkPolicyManagerService.this.getPrimarySubscriptionPlanLocked(subId);
            }
            return primarySubscriptionPlanLocked;
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public long getSubscriptionOpportunisticQuota(android.net.Network network, int quotaType) {
            long quotaBytes;
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                quotaBytes = com.android.server.net.NetworkPolicyManagerService.this.mSubscriptionOpportunisticQuota.get(com.android.server.net.NetworkPolicyManagerService.this.getSubIdLocked(network), -1L);
            }
            if (quotaBytes == -1) {
                return -1L;
            }
            if (quotaType == 1) {
                return (long) (quotaBytes * android.provider.Settings.Global.getFloat(com.android.server.net.NetworkPolicyManagerService.this.mContext.getContentResolver(), "netpolicy_quota_frac_jobs", 0.5f));
            }
            if (quotaType == 2) {
                return (long) (quotaBytes * android.provider.Settings.Global.getFloat(com.android.server.net.NetworkPolicyManagerService.this.mContext.getContentResolver(), "netpolicy_quota_frac_multipath", 0.5f));
            }
            return -1L;
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void onAdminDataAvailable() {
            com.android.server.net.NetworkPolicyManagerService.this.mAdminDataAvailableLatch.countDown();
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void setAppIdleWhitelist(int uid, boolean shouldWhitelist) {
            com.android.server.net.NetworkPolicyManagerService.this.setAppIdleWhitelist(uid, shouldWhitelist);
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void setMeteredRestrictedPackages(java.util.Set<java.lang.String> packageNames, int userId) {
            com.android.server.net.NetworkPolicyManagerService.this.setMeteredRestrictedPackagesInternal(packageNames, userId);
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void setMeteredRestrictedPackagesAsync(java.util.Set<java.lang.String> packageNames, int userId) {
            com.android.server.net.NetworkPolicyManagerService.this.mHandler.obtainMessage(17, userId, 0, packageNames).sendToTarget();
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void setLowPowerStandbyActive(boolean active) {
            android.os.Trace.traceBegin(2097152L, "setLowPowerStandbyActive");
            try {
                synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                    if (com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyActive == active) {
                        return;
                    }
                    com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyActive = active;
                    synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                        if (com.android.server.net.NetworkPolicyManagerService.this.mSystemReady) {
                            com.android.server.net.NetworkPolicyManagerService.this.forEachUid("updateRulesForRestrictPower", new java.util.function.IntConsumer() { // from class: com.android.server.net.NetworkPolicyManagerService$NetworkPolicyManagerInternalImpl$$ExternalSyntheticLambda0
                                @Override // java.util.function.IntConsumer
                                public final void accept(int i) {
                                    this.f$0.lambda$setLowPowerStandbyActive$0(i);
                                }
                            });
                            com.android.server.net.NetworkPolicyManagerService.this.updateRulesForLowPowerStandbyUL();
                        }
                    }
                }
            } finally {
                android.os.Trace.traceEnd(2097152L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setLowPowerStandbyActive$0(int uid) {
            com.android.server.net.NetworkPolicyManagerService.this.lambda$updateRulesForRestrictPowerUL$6(uid);
        }

        @Override // com.android.server.net.NetworkPolicyManagerInternal
        public void setLowPowerStandbyAllowlist(int[] uids) {
            synchronized (com.android.server.net.NetworkPolicyManagerService.this.mUidRulesFirstLock) {
                android.util.SparseBooleanArray changedUids = new android.util.SparseBooleanArray();
                for (int i = 0; i < com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyAllowlistUids.size(); i++) {
                    int oldUid = com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyAllowlistUids.keyAt(i);
                    if (!com.android.internal.util.ArrayUtils.contains(uids, oldUid)) {
                        changedUids.put(oldUid, true);
                    }
                }
                for (int i2 = 0; i2 < changedUids.size(); i2++) {
                    int deletedUid = changedUids.keyAt(i2);
                    com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyAllowlistUids.delete(deletedUid);
                }
                for (int newUid : uids) {
                    if (com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyAllowlistUids.indexOfKey(newUid) < 0) {
                        changedUids.append(newUid, true);
                        com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyAllowlistUids.append(newUid, true);
                    }
                }
                if (com.android.server.net.NetworkPolicyManagerService.this.mLowPowerStandbyActive) {
                    synchronized (com.android.server.net.NetworkPolicyManagerService.this.mNetworkPoliciesSecondLock) {
                        if (com.android.server.net.NetworkPolicyManagerService.this.mSystemReady) {
                            for (int i3 = 0; i3 < changedUids.size(); i3++) {
                                int changedUid = changedUids.keyAt(i3);
                                com.android.server.net.NetworkPolicyManagerService.this.lambda$updateRulesForRestrictPowerUL$6(changedUid);
                                com.android.server.net.NetworkPolicyManagerService.this.updateRuleForLowPowerStandbyUL(changedUid);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMeteredRestrictedPackagesInternal(java.util.Set<java.lang.String> packageNames, int userId) {
        synchronized (this.mUidRulesFirstLock) {
            java.util.Set<java.lang.Integer> newRestrictedUids = new android.util.ArraySet<>();
            for (java.lang.String packageName : packageNames) {
                int uid = getUidForPackage(packageName, userId);
                if (uid >= 0) {
                    newRestrictedUids.add(java.lang.Integer.valueOf(uid));
                }
            }
            java.util.Set<java.lang.Integer> oldRestrictedUids = this.mMeteredRestrictedUids.get(userId);
            this.mMeteredRestrictedUids.put(userId, newRestrictedUids);
            handleRestrictedPackagesChangeUL(oldRestrictedUids, newRestrictedUids);
            this.mLogger.meteredRestrictedPkgsChanged(newRestrictedUids);
        }
    }

    private int getUidForPackage(java.lang.String packageName, int userId) {
        try {
            return this.mContext.getPackageManager().getPackageUidAsUser(packageName, 4202496, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubIdLocked(android.net.Network network) {
        return this.mNetIdToSubId.get(network.getNetId(), -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.telephony.SubscriptionPlan getPrimarySubscriptionPlanLocked(int subId) {
        android.telephony.SubscriptionPlan[] plans = this.mSubscriptionPlans.get(subId);
        if (!com.android.internal.util.ArrayUtils.isEmpty(plans)) {
            for (android.telephony.SubscriptionPlan plan : plans) {
                if (plan.getCycleRule().isRecurring()) {
                    return plan;
                }
                android.util.Range<java.time.ZonedDateTime> cycle = plan.cycleIterator().next();
                if (cycle.contains(java.time.ZonedDateTime.now(this.mClock))) {
                    return plan;
                }
            }
            return null;
        }
        return null;
    }

    private void waitForAdminData() {
        if (this.mContext.getPackageManager().hasSystemFeature("android.software.device_admin")) {
            com.android.internal.util.ConcurrentUtils.waitForCountDownNoInterrupt(this.mAdminDataAvailableLatch, 10000L, "Wait for admin data");
        }
    }

    private void handleRestrictedPackagesChangeUL(java.util.Set<java.lang.Integer> oldRestrictedUids, java.util.Set<java.lang.Integer> newRestrictedUids) {
        if (!this.mNetworkManagerReady) {
            return;
        }
        if (oldRestrictedUids == null) {
            java.util.Iterator<java.lang.Integer> it = newRestrictedUids.iterator();
            while (it.hasNext()) {
                lambda$updateRulesForRestrictBackgroundUL$7(it.next().intValue());
            }
            return;
        }
        java.util.Iterator<java.lang.Integer> it2 = oldRestrictedUids.iterator();
        while (it2.hasNext()) {
            int uid = it2.next().intValue();
            if (!newRestrictedUids.contains(java.lang.Integer.valueOf(uid))) {
                lambda$updateRulesForRestrictBackgroundUL$7(uid);
            }
        }
        java.util.Iterator<java.lang.Integer> it3 = newRestrictedUids.iterator();
        while (it3.hasNext()) {
            int uid2 = it3.next().intValue();
            if (!oldRestrictedUids.contains(java.lang.Integer.valueOf(uid2))) {
                lambda$updateRulesForRestrictBackgroundUL$7(uid2);
            }
        }
    }

    private boolean isRestrictedByAdminUL(int uid) {
        java.util.Set<java.lang.Integer> restrictedUids = this.mMeteredRestrictedUids.get(android.os.UserHandle.getUserId(uid));
        return restrictedUids != null && restrictedUids.contains(java.lang.Integer.valueOf(uid));
    }

    private static boolean getBooleanDefeatingNullable(android.os.PersistableBundle bundle, java.lang.String key, boolean defaultValue) {
        return bundle != null ? bundle.getBoolean(key, defaultValue) : defaultValue;
    }

    private static com.android.server.net.NetworkPolicyManagerService.UidBlockedState getOrCreateUidBlockedStateForUid(android.util.SparseArray<com.android.server.net.NetworkPolicyManagerService.UidBlockedState> uidBlockedStates, int uid) {
        com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = uidBlockedStates.get(uid);
        if (uidBlockedState == null) {
            com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState2 = new com.android.server.net.NetworkPolicyManagerService.UidBlockedState();
            uidBlockedStates.put(uid, uidBlockedState2);
            return uidBlockedState2;
        }
        return uidBlockedState;
    }

    private int getEffectiveBlockedReasons(int uid) {
        int i;
        synchronized (this.mUidBlockedState) {
            com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = this.mUidBlockedState.get(uid);
            if (uidBlockedState == null) {
                i = 0;
            } else {
                i = uidBlockedState.effectiveBlockedReasons;
            }
        }
        return i;
    }

    private int getBlockedReasons(int uid) {
        int i;
        synchronized (this.mUidBlockedState) {
            com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState = this.mUidBlockedState.get(uid);
            if (uidBlockedState == null) {
                i = 0;
            } else {
                i = uidBlockedState.blockedReasons;
            }
        }
        return i;
    }

    static final class UidBlockedState {
        public int allowedReasons;
        public int blockedReasons;
        public int effectiveBlockedReasons;
        private static final int[] BLOCKED_REASONS = {1, 2, 4, 8, 32, 64, 65536, 131072, 262144};
        private static final int[] ALLOWED_REASONS = {1, 2, 32, 4, 8, 16, 64, 128, 65536, 131072, 262144};

        private UidBlockedState(int blockedReasons, int allowedReasons, int effectiveBlockedReasons) {
            this.blockedReasons = blockedReasons;
            this.allowedReasons = allowedReasons;
            this.effectiveBlockedReasons = effectiveBlockedReasons;
        }

        UidBlockedState() {
            this(0, 0, 0);
        }

        void updateEffectiveBlockedReasons() {
            if (com.android.server.net.NetworkPolicyManagerService.LOGV && this.blockedReasons == 0) {
                android.util.Log.v(com.android.server.net.NetworkPolicyManagerService.TAG, "updateEffectiveBlockedReasons(): no blocked reasons");
            }
            this.effectiveBlockedReasons = getEffectiveBlockedReasons(this.blockedReasons, this.allowedReasons);
            if (com.android.server.net.NetworkPolicyManagerService.LOGV) {
                android.util.Log.v(com.android.server.net.NetworkPolicyManagerService.TAG, "updateEffectiveBlockedReasons(): blockedReasons=" + java.lang.Integer.toBinaryString(this.blockedReasons) + ", effectiveReasons=" + java.lang.Integer.toBinaryString(this.effectiveBlockedReasons));
            }
        }

        static int getEffectiveBlockedReasons(int blockedReasons, int allowedReasons) {
            int effectiveBlockedReasons = blockedReasons;
            if (blockedReasons == 0) {
                return effectiveBlockedReasons;
            }
            if ((allowedReasons & 1) != 0) {
                effectiveBlockedReasons &= -65536;
            }
            if ((131072 & allowedReasons) != 0) {
                effectiveBlockedReasons &= 65535;
            }
            if ((allowedReasons & 2) != 0) {
                effectiveBlockedReasons = effectiveBlockedReasons & (-2) & (-3) & (-5);
            }
            if ((262144 & allowedReasons) != 0) {
                effectiveBlockedReasons = effectiveBlockedReasons & (-65537) & (-131073);
            }
            if ((allowedReasons & 32) != 0) {
                effectiveBlockedReasons &= -33;
            }
            if ((allowedReasons & 4) != 0) {
                effectiveBlockedReasons = effectiveBlockedReasons & (-2) & (-3) & (-5) & (-65);
            }
            if ((allowedReasons & 8) != 0) {
                effectiveBlockedReasons = effectiveBlockedReasons & (-2) & (-5) & (-65);
            }
            if ((allowedReasons & 16) != 0) {
                effectiveBlockedReasons &= -9;
            }
            if ((65536 & allowedReasons) != 0) {
                effectiveBlockedReasons &= -65537;
            }
            if ((allowedReasons & 64) != 0) {
                effectiveBlockedReasons &= -33;
            }
            if ((allowedReasons & 128) != 0) {
                return effectiveBlockedReasons & (-65);
            }
            return effectiveBlockedReasons;
        }

        static int getAllowedReasonsForProcState(int procState) {
            if (procState <= 3) {
                return 262306;
            }
            if (procState <= 5) {
                return 262274;
            }
            if (procState < 12) {
                return 128;
            }
            return 0;
        }

        public java.lang.String toString() {
            return toString(this.blockedReasons, this.allowedReasons, this.effectiveBlockedReasons);
        }

        public static java.lang.String toString(int blockedReasons, int allowedReasons, int effectiveBlockedReasons) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("{");
            sb.append("blocked=").append(blockedReasonsToString(blockedReasons)).append(",");
            sb.append("allowed=").append(allowedReasonsToString(allowedReasons)).append(",");
            sb.append("effective=").append(blockedReasonsToString(effectiveBlockedReasons));
            sb.append("}");
            return sb.toString();
        }

        private static java.lang.String blockedReasonToString(int blockedReason) {
            switch (blockedReason) {
                case 0:
                    return "NONE";
                case 1:
                    return "BATTERY_SAVER";
                case 2:
                    return "DOZE";
                case 4:
                    return "APP_STANDBY";
                case 8:
                    return "RESTRICTED_MODE";
                case 32:
                    return "LOW_POWER_STANDBY";
                case 64:
                    return "APP_BACKGROUND";
                case 65536:
                    return "DATA_SAVER";
                case 131072:
                    return "METERED_USER_RESTRICTED";
                case 262144:
                    return "METERED_ADMIN_DISABLED";
                default:
                    android.util.Slog.wtfStack(com.android.server.net.NetworkPolicyManagerService.TAG, "Unknown blockedReason: " + blockedReason);
                    return java.lang.String.valueOf(blockedReason);
            }
        }

        private static java.lang.String allowedReasonToString(int allowedReason) {
            switch (allowedReason) {
                case 0:
                    return "NONE";
                case 1:
                    return "SYSTEM";
                case 2:
                    return "FOREGROUND";
                case 4:
                    return "POWER_SAVE_ALLOWLIST";
                case 8:
                    return "POWER_SAVE_EXCEPT_IDLE_ALLOWLIST";
                case 16:
                    return "RESTRICTED_MODE_PERMISSIONS";
                case 32:
                    return "TOP";
                case 64:
                    return "LOW_POWER_STANDBY_ALLOWLIST";
                case 128:
                    return "NOT_IN_BACKGROUND";
                case 65536:
                    return "METERED_USER_EXEMPTED";
                case 131072:
                    return "METERED_SYSTEM";
                case 262144:
                    return "METERED_FOREGROUND";
                default:
                    android.util.Slog.wtfStack(com.android.server.net.NetworkPolicyManagerService.TAG, "Unknown allowedReason: " + allowedReason);
                    return java.lang.String.valueOf(allowedReason);
            }
        }

        public static java.lang.String blockedReasonsToString(int blockedReasons) {
            int i = 0;
            if (blockedReasons == 0) {
                return blockedReasonToString(0);
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            int[] iArr = BLOCKED_REASONS;
            int length = iArr.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                int reason = iArr[i];
                if ((blockedReasons & reason) != 0) {
                    sb.append(sb.length() != 0 ? "|" : "");
                    sb.append(blockedReasonToString(reason));
                    blockedReasons &= ~reason;
                }
                i++;
            }
            if (blockedReasons != 0) {
                sb.append(sb.length() != 0 ? "|" : "");
                sb.append(java.lang.String.valueOf(blockedReasons));
                android.util.Slog.wtfStack(com.android.server.net.NetworkPolicyManagerService.TAG, "Unknown blockedReasons: " + blockedReasons);
            }
            return sb.toString();
        }

        public static java.lang.String allowedReasonsToString(int allowedReasons) {
            int i = 0;
            if (allowedReasons == 0) {
                return allowedReasonToString(0);
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            int[] iArr = ALLOWED_REASONS;
            int length = iArr.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                int reason = iArr[i];
                if ((allowedReasons & reason) != 0) {
                    sb.append(sb.length() != 0 ? "|" : "");
                    sb.append(allowedReasonToString(reason));
                    allowedReasons &= ~reason;
                }
                i++;
            }
            if (allowedReasons != 0) {
                sb.append(sb.length() != 0 ? "|" : "");
                sb.append(java.lang.String.valueOf(allowedReasons));
                android.util.Slog.wtfStack(com.android.server.net.NetworkPolicyManagerService.TAG, "Unknown allowedReasons: " + allowedReasons);
            }
            return sb.toString();
        }

        public void copyFrom(com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState) {
            this.blockedReasons = uidBlockedState.blockedReasons;
            this.allowedReasons = uidBlockedState.allowedReasons;
            this.effectiveBlockedReasons = uidBlockedState.effectiveBlockedReasons;
        }

        public int deriveUidRules() {
            int uidRule = 0;
            if ((this.effectiveBlockedReasons & 8) != 0) {
                uidRule = 0 | 1024;
            }
            if ((this.effectiveBlockedReasons & 103) == 0) {
                if ((this.blockedReasons & 103) != 0) {
                    uidRule |= 32;
                }
            } else {
                uidRule |= 64;
            }
            if ((this.effectiveBlockedReasons & 393216) != 0) {
                uidRule |= 4;
            } else if ((this.blockedReasons & 131072) != 0 && (this.allowedReasons & 262144) != 0) {
                uidRule |= 2;
            } else if ((this.blockedReasons & 65536) != 0) {
                if ((this.allowedReasons & 65536) != 0) {
                    uidRule |= 32;
                } else if ((this.allowedReasons & 262144) != 0) {
                    uidRule |= 2;
                }
            }
            if (com.android.server.net.NetworkPolicyManagerService.LOGV) {
                android.util.Slog.v(com.android.server.net.NetworkPolicyManagerService.TAG, "uidBlockedState=" + this + " -> uidRule=" + android.net.NetworkPolicyManager.uidRulesToString(uidRule));
            }
            return uidRule;
        }
    }

    private static class NotificationId {
        private final int mId;
        private final java.lang.String mTag;

        NotificationId(android.net.NetworkPolicy policy, int type) {
            this.mTag = buildNotificationTag(policy, type);
            this.mId = type;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.net.NetworkPolicyManagerService.NotificationId)) {
                return false;
            }
            com.android.server.net.NetworkPolicyManagerService.NotificationId that = (com.android.server.net.NetworkPolicyManagerService.NotificationId) o;
            return java.util.Objects.equals(this.mTag, that.mTag);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.mTag);
        }

        private static java.lang.String buildNotificationTag(android.net.NetworkPolicy policy, int type) {
            return "NetworkPolicy:" + policy.template.hashCode() + ":" + type;
        }

        public java.lang.String getTag() {
            return this.mTag;
        }

        public int getId() {
            return this.mId;
        }

        public java.lang.String toString() {
            return this.mTag;
        }
    }

    private boolean isCloneUidNL(int i) {
        return this.mOplusNPMS.isCloneUidNL(i);
    }

    public com.android.server.net.INetworkPolicyManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    private class NetworkPolicyManagerServiceWrapper implements com.android.server.net.INetworkPolicyManagerServiceWrapper {
        private NetworkPolicyManagerServiceWrapper() {
        }

        @Override // com.android.server.net.INetworkPolicyManagerServiceWrapper
        public void setUidPolicyUncheckedUL(int uid, int policy, boolean persist) {
            com.android.server.net.NetworkPolicyManagerService.this.setUidPolicyUncheckedUL(uid, policy, persist);
        }
    }

    private boolean needDisableMobileNetwork(int subId) {
        android.util.Slog.v(TAG, "needDisableMobileNetwork1");
        return getLimitNotificationState(subId) == 1;
    }

    private int getLimitNotificationState(int subId) {
        java.lang.String newKeyLimitNotification = "data_limit_notification_bool" + subId;
        int numLimitNotificaion = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), newKeyLimitNotification, 0);
        android.util.Slog.v(TAG, "getLimitNotification subId:" + subId + " ,numLimitNotificaion:" + numLimitNotificaion);
        return numLimitNotificaion;
    }

    private boolean isUidAllowedNetworkWhileBackground(int uid) {
        boolean isAllowedNetworkBgApp = false;
        if (this.mOplusNPMS != null) {
            isAllowedNetworkBgApp = this.mOplusNPMS.isUidAllowedNetworkWhileBackground(uid);
        }
        if (LOGD) {
            android.util.Slog.v(TAG, "isUidAllowedNetworkWhileBackground uid:" + uid + " isAllowed:" + isAllowedNetworkBgApp);
        }
        return isAllowedNetworkBgApp;
    }
}
