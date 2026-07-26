package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class MultipathPolicyTracker {
    private static final boolean DBG = false;
    private static final long MIN_THRESHOLD_BYTES = 2097152;
    private static final int OPQUOTA_USER_SETTING_DIVIDER = 20;
    private static java.lang.String TAG = com.android.server.connectivity.MultipathPolicyTracker.class.getSimpleName();
    private android.net.ConnectivityManager mCM;
    private final java.time.Clock mClock;
    private final com.android.server.connectivity.MultipathPolicyTracker.ConfigChangeReceiver mConfigChangeReceiver;
    private final android.content.Context mContext;
    private final com.android.server.connectivity.MultipathPolicyTracker.Dependencies mDeps;
    private final android.os.Handler mHandler;
    private android.net.ConnectivityManager.NetworkCallback mMobileNetworkCallback;
    private final java.util.concurrent.ConcurrentHashMap<android.net.Network, com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker> mMultipathTrackers;
    private android.net.NetworkPolicyManager mNPM;
    private android.net.NetworkPolicyManager.Listener mPolicyListener;
    private final android.content.ContentResolver mResolver;
    final android.database.ContentObserver mSettingsObserver;
    private android.app.usage.NetworkStatsManager mStatsManager;
    private final android.content.Context mUserAllContext;

    public static class Dependencies {
        public java.time.Clock getClock() {
            return new android.os.BestClock(java.time.ZoneOffset.UTC, new java.time.Clock[]{android.os.SystemClock.currentNetworkTimeClock(), java.time.Clock.systemUTC()});
        }
    }

    public MultipathPolicyTracker(android.content.Context ctx, android.os.Handler handler) {
        this(ctx, handler, new com.android.server.connectivity.MultipathPolicyTracker.Dependencies());
    }

    public MultipathPolicyTracker(android.content.Context ctx, android.os.Handler handler, com.android.server.connectivity.MultipathPolicyTracker.Dependencies deps) {
        this.mMultipathTrackers = new java.util.concurrent.ConcurrentHashMap<>();
        this.mContext = ctx;
        this.mUserAllContext = ctx.createContextAsUser(android.os.UserHandle.ALL, 0);
        this.mHandler = handler;
        this.mClock = deps.getClock();
        this.mDeps = deps;
        this.mResolver = this.mContext.getContentResolver();
        this.mSettingsObserver = new com.android.server.connectivity.MultipathPolicyTracker.SettingsObserver(this.mHandler);
        this.mConfigChangeReceiver = new com.android.server.connectivity.MultipathPolicyTracker.ConfigChangeReceiver();
    }

    public void start() {
        this.mCM = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
        this.mNPM = (android.net.NetworkPolicyManager) this.mContext.getSystemService(android.net.NetworkPolicyManager.class);
        this.mStatsManager = (android.app.usage.NetworkStatsManager) this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class);
        registerTrackMobileCallback();
        registerNetworkPolicyListener();
        android.net.Uri defaultSettingUri = android.provider.Settings.Global.getUriFor("network_default_daily_multipath_quota_bytes");
        this.mResolver.registerContentObserver(defaultSettingUri, false, this.mSettingsObserver);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        this.mUserAllContext.registerReceiver(this.mConfigChangeReceiver, intentFilter, null, this.mHandler);
    }

    public void shutdown() {
        maybeUnregisterTrackMobileCallback();
        unregisterNetworkPolicyListener();
        for (com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker t : this.mMultipathTrackers.values()) {
            t.shutdown();
        }
        this.mMultipathTrackers.clear();
        this.mResolver.unregisterContentObserver(this.mSettingsObserver);
        this.mUserAllContext.unregisterReceiver(this.mConfigChangeReceiver);
    }

    public java.lang.Integer getMultipathPreference(android.net.Network network) {
        com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker t;
        if (network == null || (t = this.mMultipathTrackers.get(network)) == null) {
            return null;
        }
        return java.lang.Integer.valueOf(t.getMultipathPreference());
    }

    class MultipathTracker {
        private volatile long mMultipathBudget;
        private android.net.NetworkCapabilities mNetworkCapabilities;
        private final android.net.NetworkTemplate mNetworkTemplate;
        private long mQuota;
        private final android.app.usage.NetworkStatsManager mStatsManager;
        private final int mSubId;
        private final android.app.usage.NetworkStatsManager.UsageCallback mUsageCallback;
        private boolean mUsageCallbackRegistered = false;
        final android.net.Network network;
        final java.lang.String subscriberId;

        public MultipathTracker(final android.net.Network network, android.net.NetworkCapabilities nc) {
            this.network = network;
            this.mNetworkCapabilities = new android.net.NetworkCapabilities(nc);
            android.net.NetworkSpecifier specifier = nc.getNetworkSpecifier();
            if (specifier instanceof android.net.TelephonyNetworkSpecifier) {
                this.mSubId = ((android.net.TelephonyNetworkSpecifier) specifier).getSubscriptionId();
                android.telephony.TelephonyManager tele = (android.telephony.TelephonyManager) com.android.server.connectivity.MultipathPolicyTracker.this.mContext.getSystemService(android.telephony.TelephonyManager.class);
                if (tele == null) {
                    throw new java.lang.IllegalStateException(java.lang.String.format("Missing TelephonyManager", new java.lang.Object[0]));
                }
                android.telephony.TelephonyManager tele2 = tele.createForSubscriptionId(this.mSubId);
                if (tele2 == null) {
                    throw new java.lang.IllegalStateException(java.lang.String.format("Can't get TelephonyManager for subId %d", java.lang.Integer.valueOf(this.mSubId)));
                }
                this.subscriberId = tele2.getSubscriberId();
                if (this.subscriberId == null) {
                    throw new java.lang.IllegalStateException("Null subscriber Id for subId " + this.mSubId);
                }
                this.mNetworkTemplate = new android.net.NetworkTemplate.Builder(1).setSubscriberIds(java.util.Set.of(this.subscriberId)).setMeteredness(1).setDefaultNetworkStatus(0).build();
                this.mUsageCallback = new android.app.usage.NetworkStatsManager.UsageCallback() { // from class: com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker.1
                    @Override // android.app.usage.NetworkStatsManager.UsageCallback
                    public void onThresholdReached(int networkType, java.lang.String subscriberId) {
                        com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker.this.updateMultipathBudget();
                    }
                };
                this.mStatsManager = (android.app.usage.NetworkStatsManager) com.android.server.connectivity.MultipathPolicyTracker.this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class);
                this.mStatsManager.setPollOnOpen(false);
                updateMultipathBudget();
                return;
            }
            throw new java.lang.IllegalStateException(java.lang.String.format("Can't get subId from mobile network %s (%s)", network, nc));
        }

        public void setNetworkCapabilities(android.net.NetworkCapabilities nc) {
            this.mNetworkCapabilities = new android.net.NetworkCapabilities(nc);
        }

        private long getDailyNonDefaultDataUsage() {
            java.time.ZonedDateTime end = java.time.ZonedDateTime.ofInstant(com.android.server.connectivity.MultipathPolicyTracker.this.mClock.instant(), java.time.ZoneId.systemDefault());
            java.time.ZonedDateTime start = end.truncatedTo(java.time.temporal.ChronoUnit.DAYS);
            long bytes = getNetworkTotalBytes(start.toInstant().toEpochMilli(), end.toInstant().toEpochMilli());
            return bytes;
        }

        private long getNetworkTotalBytes(long start, long end) {
            try {
                android.app.usage.NetworkStats.Bucket ret = this.mStatsManager.querySummaryForDevice(this.mNetworkTemplate, start, end);
                return ret.getRxBytes() + ret.getTxBytes();
            } catch (java.lang.RuntimeException e) {
                android.util.Log.w(com.android.server.connectivity.MultipathPolicyTracker.TAG, "Failed to get data usage: " + e);
                return -1L;
            }
        }

        private android.net.NetworkIdentity getTemplateMatchingNetworkIdentity(android.net.NetworkCapabilities nc) {
            return new android.net.NetworkIdentity.Builder().setType(0).setSubscriberId(this.subscriberId).setRoaming(!nc.hasCapability(18)).setMetered(!nc.hasCapability(11)).setSubId(this.mSubId).build();
        }

        private long getRemainingDailyBudget(long limitBytes, android.util.Range<java.time.ZonedDateTime> cycle) {
            long start = ((java.time.ZonedDateTime) cycle.getLower()).toInstant().toEpochMilli();
            long end = ((java.time.ZonedDateTime) cycle.getUpper()).toInstant().toEpochMilli();
            long totalBytes = getNetworkTotalBytes(start, end);
            long remainingBytes = totalBytes != -1 ? java.lang.Math.max(0L, limitBytes - totalBytes) : 0L;
            long remainingDays = (((end - com.android.server.connectivity.MultipathPolicyTracker.this.mClock.millis()) - 1) / java.util.concurrent.TimeUnit.DAYS.toMillis(1L)) + 1;
            return remainingBytes / java.lang.Math.max(1L, remainingDays);
        }

        private long getUserPolicyOpportunisticQuotaBytes() {
            long policyBytes;
            long minQuota = Long.MAX_VALUE;
            android.net.NetworkIdentity identity = getTemplateMatchingNetworkIdentity(this.mNetworkCapabilities);
            android.net.NetworkPolicy[] policies = com.android.server.connectivity.MultipathPolicyTracker.this.mNPM.getNetworkPolicies();
            for (android.net.NetworkPolicy policy : policies) {
                if (policy.hasCycle() && policy.template.matches(identity)) {
                    long cycleStart = ((java.time.ZonedDateTime) ((android.util.Range) policy.cycleIterator().next()).getLower()).toInstant().toEpochMilli();
                    long activeWarning = com.android.server.connectivity.MultipathPolicyTracker.getActiveWarning(policy, cycleStart);
                    if (activeWarning == -1) {
                        policyBytes = com.android.server.connectivity.MultipathPolicyTracker.getActiveLimit(policy, cycleStart);
                    } else {
                        policyBytes = activeWarning;
                    }
                    if (policyBytes != -1 && policyBytes != -1) {
                        long policyBudget = getRemainingDailyBudget(policyBytes, (android.util.Range) policy.cycleIterator().next());
                        minQuota = java.lang.Math.min(minQuota, policyBudget);
                    }
                }
            }
            if (minQuota == Long.MAX_VALUE) {
                return -1L;
            }
            return minQuota / 20;
        }

        void updateMultipathBudget() {
            long quota = ((com.android.server.net.NetworkPolicyManagerInternal) com.android.server.LocalServices.getService(com.android.server.net.NetworkPolicyManagerInternal.class)).getSubscriptionOpportunisticQuota(this.network, 2);
            if (quota == -1) {
                quota = getUserPolicyOpportunisticQuotaBytes();
            }
            if (quota == -1) {
                quota = com.android.server.connectivity.MultipathPolicyTracker.this.getDefaultDailyMultipathQuotaBytes();
            }
            if (haveMultipathBudget() && quota == this.mQuota) {
                return;
            }
            this.mQuota = quota;
            long usage = getDailyNonDefaultDataUsage();
            long budget = usage != -1 ? java.lang.Math.max(0L, quota - usage) : 0L;
            if (budget > com.android.server.connectivity.MultipathPolicyTracker.MIN_THRESHOLD_BYTES) {
                setMultipathBudget(budget);
            } else {
                clearMultipathBudget();
            }
        }

        public int getMultipathPreference() {
            if (haveMultipathBudget()) {
                return 3;
            }
            return 0;
        }

        public long getQuota() {
            return this.mQuota;
        }

        public long getMultipathBudget() {
            return this.mMultipathBudget;
        }

        private boolean haveMultipathBudget() {
            return this.mMultipathBudget > 0;
        }

        private void setMultipathBudget(long budget) {
            maybeUnregisterUsageCallback();
            this.mStatsManager.registerUsageCallback(this.mNetworkTemplate, budget, new java.util.concurrent.Executor() { // from class: com.android.server.connectivity.MultipathPolicyTracker$MultipathTracker$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(java.lang.Runnable runnable) {
                    this.f$0.lambda$setMultipathBudget$0(runnable);
                }
            }, this.mUsageCallback);
            this.mUsageCallbackRegistered = true;
            this.mMultipathBudget = budget;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setMultipathBudget$0(java.lang.Runnable command) {
            com.android.server.connectivity.MultipathPolicyTracker.this.mHandler.post(command);
        }

        private void maybeUnregisterUsageCallback() {
            if (this.mUsageCallbackRegistered) {
                this.mStatsManager.unregisterUsageCallback(this.mUsageCallback);
                this.mUsageCallbackRegistered = false;
            }
        }

        private void clearMultipathBudget() {
            maybeUnregisterUsageCallback();
            this.mMultipathBudget = 0L;
        }

        void shutdown() {
            clearMultipathBudget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getActiveWarning(android.net.NetworkPolicy policy, long cycleStart) {
        if (policy.lastWarningSnooze < cycleStart) {
            return policy.warningBytes;
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getActiveLimit(android.net.NetworkPolicy policy, long cycleStart) {
        if (policy.lastLimitSnooze < cycleStart) {
            return policy.limitBytes;
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getDefaultDailyMultipathQuotaBytes() {
        java.lang.String setting = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "network_default_daily_multipath_quota_bytes");
        if (setting != null) {
            try {
                return java.lang.Long.parseLong(setting);
            } catch (java.lang.NumberFormatException e) {
            }
        }
        return this.mContext.getResources().getInteger(android.R.integer.config_mdc_initial_max_retry);
    }

    private void registerTrackMobileCallback() {
        android.net.NetworkRequest request = new android.net.NetworkRequest.Builder().addCapability(12).addTransportType(0).build();
        this.mMobileNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.connectivity.MultipathPolicyTracker.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities nc) {
                com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker existing = (com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker) com.android.server.connectivity.MultipathPolicyTracker.this.mMultipathTrackers.get(network);
                if (existing != null) {
                    existing.setNetworkCapabilities(nc);
                    existing.updateMultipathBudget();
                } else {
                    try {
                        com.android.server.connectivity.MultipathPolicyTracker.this.mMultipathTrackers.put(network, com.android.server.connectivity.MultipathPolicyTracker.this.new MultipathTracker(network, nc));
                    } catch (java.lang.IllegalStateException e) {
                        android.util.Log.e(com.android.server.connectivity.MultipathPolicyTracker.TAG, "Can't track mobile network " + network + ": " + e.getMessage());
                    }
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(android.net.Network network) {
                com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker existing = (com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker) com.android.server.connectivity.MultipathPolicyTracker.this.mMultipathTrackers.get(network);
                if (existing != null) {
                    existing.shutdown();
                    com.android.server.connectivity.MultipathPolicyTracker.this.mMultipathTrackers.remove(network);
                }
            }
        };
        this.mCM.registerNetworkCallback(request, this.mMobileNetworkCallback, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllMultipathBudgets() {
        for (com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker t : this.mMultipathTrackers.values()) {
            t.updateMultipathBudget();
        }
    }

    private void maybeUnregisterTrackMobileCallback() {
        if (this.mMobileNetworkCallback != null) {
            this.mCM.unregisterNetworkCallback(this.mMobileNetworkCallback);
        }
        this.mMobileNetworkCallback = null;
    }

    /* JADX INFO: renamed from: com.android.server.connectivity.MultipathPolicyTracker$2, reason: invalid class name */
    class AnonymousClass2 extends android.net.NetworkPolicyManager.Listener {
        AnonymousClass2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeteredIfacesChanged$0() {
            com.android.server.connectivity.MultipathPolicyTracker.this.updateAllMultipathBudgets();
        }

        public void onMeteredIfacesChanged(java.lang.String[] meteredIfaces) {
            com.android.server.connectivity.MultipathPolicyTracker.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.connectivity.MultipathPolicyTracker$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onMeteredIfacesChanged$0();
                }
            });
        }
    }

    private void registerNetworkPolicyListener() {
        this.mPolicyListener = new com.android.server.connectivity.MultipathPolicyTracker.AnonymousClass2();
        this.mNPM.registerListener(this.mPolicyListener);
    }

    private void unregisterNetworkPolicyListener() {
        this.mNPM.unregisterListener(this.mPolicyListener);
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        public SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            android.util.Log.wtf(com.android.server.connectivity.MultipathPolicyTracker.TAG, "Should never be reached.");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (!android.provider.Settings.Global.getUriFor("network_default_daily_multipath_quota_bytes").equals(uri)) {
                android.util.Log.wtf(com.android.server.connectivity.MultipathPolicyTracker.TAG, "Unexpected settings observation: " + uri);
            }
            com.android.server.connectivity.MultipathPolicyTracker.this.updateAllMultipathBudgets();
        }
    }

    private final class ConfigChangeReceiver extends android.content.BroadcastReceiver {
        private ConfigChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.connectivity.MultipathPolicyTracker.this.updateAllMultipathBudgets();
        }
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("MultipathPolicyTracker:");
        pw.increaseIndent();
        for (com.android.server.connectivity.MultipathPolicyTracker.MultipathTracker t : this.mMultipathTrackers.values()) {
            pw.println(java.lang.String.format("Network %s: quota %d, budget %d. Preference: %s", t.network, java.lang.Long.valueOf(t.getQuota()), java.lang.Long.valueOf(t.getMultipathBudget()), android.util.DebugUtils.flagsToString(android.net.ConnectivityManager.class, "MULTIPATH_PREFERENCE_", t.getMultipathPreference())));
        }
        pw.decreaseIndent();
    }
}
