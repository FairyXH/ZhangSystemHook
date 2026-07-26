package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class ConnectivityController extends com.android.server.job.controllers.RestrictingController implements android.net.ConnectivityManager.OnNetworkActiveListener {
    private static final boolean DEBUG;
    private static final int MAX_NETWORK_CALLBACKS = 125;
    private static final long MIN_ADJUST_CALLBACK_INTERVAL_MS = 1000;
    private static final long MIN_STATS_UPDATE_INTERVAL_MS = 30000;
    private static final int MSG_ADJUST_CALLBACKS = 0;
    private static final int MSG_DATA_SAVER_TOGGLED = 2;
    private static final int MSG_PROCESS_ACTIVE_NETWORK = 4;
    private static final int MSG_UID_POLICIES_CHANGED = 3;
    private static final int MSG_UPDATE_ALL_TRACKED_JOBS = 1;
    private static final java.lang.String TAG = "JobScheduler.Connectivity";
    static final int TRANSPORT_AFFINITY_AVOID = 2;
    static final int TRANSPORT_AFFINITY_PREFER = 1;
    static final int TRANSPORT_AFFINITY_UNDEFINED = 0;
    private static final int UNBYPASSABLE_BG_BLOCKED_REASONS = -65;
    private static final int UNBYPASSABLE_EJ_BLOCKED_REASONS = -72;
    private static final int UNBYPASSABLE_FOREGROUND_BLOCKED_REASONS = -196680;
    private static final int UNBYPASSABLE_UI_BLOCKED_REASONS = -196680;
    public static final long UNKNOWN_TIME = -1;
    static final android.util.SparseIntArray sNetworkTransportAffinities;
    private final android.util.ArrayMap<android.net.Network, com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata> mAvailableNetworks;
    private final android.util.SparseBooleanArray mBackgroundMeteredAllowed;
    private final com.android.server.job.controllers.ConnectivityController.CcConfig mCcConfig;
    private final android.net.ConnectivityManager mConnManager;
    private final android.util.SparseArray<com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback> mCurrentDefaultNetworkCallbacks;
    private final android.net.ConnectivityManager.NetworkCallback mDefaultNetworkCallback;
    private final android.util.Pools.Pool<com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback> mDefaultNetworkCallbackPool;
    private final com.android.server.job.controllers.FlexibilityController mFlexibilityController;
    private final android.os.Handler mHandler;
    private long mLastAllJobUpdateTimeElapsed;
    private long mLastCallbackAdjustmentTimeElapsed;
    private final android.net.INetworkPolicyListener mNetPolicyListener;
    private final android.net.NetworkPolicyManager mNetPolicyManager;
    private final com.android.server.net.NetworkPolicyManagerInternal mNetPolicyManagerInternal;
    private final android.net.ConnectivityManager.NetworkCallback mNetworkCallback;
    private final android.util.SparseArray<android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mRequestedWhitelistJobs;
    private final android.util.SparseArray<com.android.server.job.controllers.ConnectivityController.CellSignalStrengthCallback> mSignalStrengths;
    private final java.util.List<com.android.server.job.controllers.ConnectivityController.UidStats> mSortedStats;
    private android.net.Network mSystemDefaultNetwork;
    private final android.util.SparseArray<android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mTrackedJobs;
    private final android.util.SparseArray<com.android.server.job.controllers.ConnectivityController.UidStats> mUidStats;
    private final java.util.Comparator<com.android.server.job.controllers.ConnectivityController.UidStats> mUidStatsComparator;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
        sNetworkTransportAffinities = new android.util.SparseIntArray();
        sNetworkTransportAffinities.put(0, 2);
        sNetworkTransportAffinities.put(3, 1);
        sNetworkTransportAffinities.put(10, 2);
        sNetworkTransportAffinities.put(1, 1);
    }

    public ConnectivityController(com.android.server.job.JobSchedulerService service, com.android.server.job.controllers.FlexibilityController flexibilityController) {
        super(service);
        this.mTrackedJobs = new android.util.SparseArray<>();
        this.mRequestedWhitelistJobs = new android.util.SparseArray<>();
        this.mAvailableNetworks = new android.util.ArrayMap<>();
        this.mCurrentDefaultNetworkCallbacks = new android.util.SparseArray<>();
        this.mUidStatsComparator = new java.util.Comparator<com.android.server.job.controllers.ConnectivityController.UidStats>() { // from class: com.android.server.job.controllers.ConnectivityController.1
            private int prioritizeExistenceOver(int threshold, int v1, int v2) {
                if (v1 > threshold && v2 > threshold) {
                    return 0;
                }
                if (v1 <= threshold && v2 <= threshold) {
                    return 0;
                }
                if (v1 > threshold) {
                    return -1;
                }
                return 1;
            }

            @Override // java.util.Comparator
            public int compare(com.android.server.job.controllers.ConnectivityController.UidStats us1, com.android.server.job.controllers.ConnectivityController.UidStats us2) {
                int runningPriority = prioritizeExistenceOver(0, us1.runningJobs.size(), us2.runningJobs.size());
                if (runningPriority == 0) {
                    int readyWithConnPriority = prioritizeExistenceOver(0, us1.numReadyWithConnectivity, us2.numReadyWithConnectivity);
                    if (readyWithConnPriority == 0) {
                        int reqAvailPriority = prioritizeExistenceOver(0, us1.numRequestedNetworkAvailable, us2.numRequestedNetworkAvailable);
                        if (reqAvailPriority == 0) {
                            int topPriority = prioritizeExistenceOver(39, us1.baseBias, us2.baseBias);
                            if (topPriority == 0) {
                                int uijPriority = prioritizeExistenceOver(0, us1.numUIJs, us2.numUIJs);
                                if (uijPriority == 0) {
                                    int ejPriority = prioritizeExistenceOver(0, us1.numEJs, us2.numEJs);
                                    if (ejPriority == 0) {
                                        int fgsPriority = prioritizeExistenceOver(34, us1.baseBias, us2.baseBias);
                                        if (fgsPriority != 0) {
                                            return fgsPriority;
                                        }
                                        if (us1.earliestUIJEnqueueTime < us2.earliestUIJEnqueueTime) {
                                            return -1;
                                        }
                                        if (us1.earliestUIJEnqueueTime > us2.earliestUIJEnqueueTime) {
                                            return 1;
                                        }
                                        if (us1.earliestEJEnqueueTime < us2.earliestEJEnqueueTime) {
                                            return -1;
                                        }
                                        if (us1.earliestEJEnqueueTime > us2.earliestEJEnqueueTime) {
                                            return 1;
                                        }
                                        if (us1.baseBias != us2.baseBias) {
                                            return us2.baseBias - us1.baseBias;
                                        }
                                        if (us1.earliestEnqueueTime < us2.earliestEnqueueTime) {
                                            return -1;
                                        }
                                        return us1.earliestEnqueueTime > us2.earliestEnqueueTime ? 1 : 0;
                                    }
                                    return ejPriority;
                                }
                                return uijPriority;
                            }
                            return topPriority;
                        }
                        return reqAvailPriority;
                    }
                    return readyWithConnPriority;
                }
                return runningPriority;
            }
        };
        this.mUidStats = new android.util.SparseArray<>();
        this.mDefaultNetworkCallbackPool = new android.util.Pools.SimplePool(125);
        this.mSortedStats = new java.util.ArrayList();
        this.mBackgroundMeteredAllowed = new android.util.SparseBooleanArray();
        this.mSignalStrengths = new android.util.SparseArray<>();
        this.mNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.job.controllers.ConnectivityController.2
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(android.net.Network network) {
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "onAvailable: " + network);
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities capabilities) {
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "onCapabilitiesChanged: " + network);
                }
                synchronized (com.android.server.job.controllers.ConnectivityController.this.mLock) {
                    com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cnm = (com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata) com.android.server.job.controllers.ConnectivityController.this.mAvailableNetworks.get(network);
                    if (cnm == null) {
                        cnm = new com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata();
                        cnm.capabilitiesFirstAcquiredTimeElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                        com.android.server.job.controllers.ConnectivityController.this.mAvailableNetworks.put(network, cnm);
                    } else {
                        android.net.NetworkCapabilities oldCaps = cnm.networkCapabilities;
                        if (oldCaps != null) {
                            maybeUnregisterSignalStrengthCallbackLocked(oldCaps);
                        }
                    }
                    cnm.networkCapabilities = capabilities;
                    if (com.android.server.job.controllers.ConnectivityController.this.updateTransportAffinitySatisfaction(cnm)) {
                        maybeUpdateFlexConstraintLocked(cnm);
                    }
                    maybeRegisterSignalStrengthCallbackLocked(capabilities);
                    com.android.server.job.controllers.ConnectivityController.this.updateTrackedJobsLocked(-1, network);
                    com.android.server.job.controllers.ConnectivityController.this.postAdjustCallbacks();
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(android.net.Network network) {
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "onLost: " + network);
                }
                synchronized (com.android.server.job.controllers.ConnectivityController.this.mLock) {
                    com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cnm = (com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata) com.android.server.job.controllers.ConnectivityController.this.mAvailableNetworks.remove(network);
                    if (cnm != null) {
                        if (cnm.networkCapabilities != null) {
                            maybeUnregisterSignalStrengthCallbackLocked(cnm.networkCapabilities);
                        }
                        if (cnm.satisfiesTransportAffinities) {
                            maybeUpdateFlexConstraintLocked(null);
                        }
                    }
                    for (int u = 0; u < com.android.server.job.controllers.ConnectivityController.this.mCurrentDefaultNetworkCallbacks.size(); u++) {
                        com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback callback = (com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback) com.android.server.job.controllers.ConnectivityController.this.mCurrentDefaultNetworkCallbacks.valueAt(u);
                        if (java.util.Objects.equals(callback.mDefaultNetwork, network)) {
                            callback.mDefaultNetwork = null;
                        }
                    }
                    com.android.server.job.controllers.ConnectivityController.this.updateTrackedJobsLocked(-1, network);
                    com.android.server.job.controllers.ConnectivityController.this.postAdjustCallbacks();
                }
            }

            private void maybeRegisterSignalStrengthCallbackLocked(android.net.NetworkCapabilities capabilities) {
                if (!capabilities.hasTransport(0)) {
                    return;
                }
                android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) com.android.server.job.controllers.ConnectivityController.this.mContext.getSystemService(android.telephony.TelephonyManager.class);
                java.util.Set<java.lang.Integer> subscriptionIds = capabilities.getSubscriptionIds();
                java.util.Iterator<java.lang.Integer> it = subscriptionIds.iterator();
                while (it.hasNext()) {
                    int subId = it.next().intValue();
                    if (com.android.server.job.controllers.ConnectivityController.this.mSignalStrengths.indexOfKey(subId) < 0) {
                        android.telephony.TelephonyManager idTm = telephonyManager.createForSubscriptionId(subId);
                        com.android.server.job.controllers.ConnectivityController.CellSignalStrengthCallback callback = new com.android.server.job.controllers.ConnectivityController.CellSignalStrengthCallback();
                        idTm.registerTelephonyCallback(com.android.server.AppSchedulingModuleThread.getExecutor(), callback);
                        com.android.server.job.controllers.ConnectivityController.this.mSignalStrengths.put(subId, callback);
                        android.telephony.SignalStrength signalStrength = idTm.getSignalStrength();
                        if (signalStrength != null) {
                            callback.signalStrength = signalStrength.getLevel();
                        }
                    }
                }
            }

            private void maybeUnregisterSignalStrengthCallbackLocked(android.net.NetworkCapabilities capabilities) {
                if (!capabilities.hasTransport(0)) {
                    return;
                }
                android.util.ArraySet<java.lang.Integer> activeIds = new android.util.ArraySet<>();
                int size = com.android.server.job.controllers.ConnectivityController.this.mAvailableNetworks.size();
                for (int i = 0; i < size; i++) {
                    com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata metadata = (com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata) com.android.server.job.controllers.ConnectivityController.this.mAvailableNetworks.valueAt(i);
                    if (metadata != null && metadata.networkCapabilities != null && metadata.networkCapabilities.hasTransport(0)) {
                        activeIds.addAll(metadata.networkCapabilities.getSubscriptionIds());
                    }
                }
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.ConnectivityController.TAG, "Active subscription IDs: " + activeIds);
                }
                android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) com.android.server.job.controllers.ConnectivityController.this.mContext.getSystemService(android.telephony.TelephonyManager.class);
                java.util.Set<java.lang.Integer> subscriptionIds = capabilities.getSubscriptionIds();
                java.util.Iterator<java.lang.Integer> it = subscriptionIds.iterator();
                while (it.hasNext()) {
                    int subId = it.next().intValue();
                    if (!activeIds.contains(java.lang.Integer.valueOf(subId))) {
                        android.telephony.TelephonyManager idTm = telephonyManager.createForSubscriptionId(subId);
                        com.android.server.job.controllers.ConnectivityController.CellSignalStrengthCallback callback = (com.android.server.job.controllers.ConnectivityController.CellSignalStrengthCallback) com.android.server.job.controllers.ConnectivityController.this.mSignalStrengths.removeReturnOld(subId);
                        if (callback != null) {
                            idTm.unregisterTelephonyCallback(callback);
                        } else {
                            android.util.Slog.wtf(com.android.server.job.controllers.ConnectivityController.TAG, "Callback for sub " + subId + " didn't exist?!?!");
                        }
                    }
                }
            }

            private void maybeUpdateFlexConstraintLocked(com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cachedNetworkMetadata) {
                if (cachedNetworkMetadata != null && cachedNetworkMetadata.satisfiesTransportAffinities) {
                    com.android.server.job.controllers.ConnectivityController.this.mFlexibilityController.setConstraintSatisfied(268435456, true, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    return;
                }
                boolean isTransportAffinitySatisfied = false;
                int i = com.android.server.job.controllers.ConnectivityController.this.mAvailableNetworks.size() - 1;
                while (true) {
                    if (i >= 0) {
                        com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cnm = (com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata) com.android.server.job.controllers.ConnectivityController.this.mAvailableNetworks.valueAt(i);
                        if (cnm == null || !cnm.satisfiesTransportAffinities) {
                            i--;
                        } else {
                            isTransportAffinitySatisfied = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (!isTransportAffinitySatisfied) {
                    com.android.server.job.controllers.ConnectivityController.this.mFlexibilityController.setConstraintSatisfied(268435456, false, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                }
            }
        };
        this.mDefaultNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.job.controllers.ConnectivityController.3
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(android.net.Network network) {
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "systemDefault-onAvailable: " + network);
                }
                synchronized (com.android.server.job.controllers.ConnectivityController.this.mLock) {
                    com.android.server.job.controllers.ConnectivityController.this.mSystemDefaultNetwork = network;
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(android.net.Network network) {
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "systemDefault-onLost: " + network);
                }
                synchronized (com.android.server.job.controllers.ConnectivityController.this.mLock) {
                    if (network.equals(com.android.server.job.controllers.ConnectivityController.this.mSystemDefaultNetwork)) {
                        com.android.server.job.controllers.ConnectivityController.this.mSystemDefaultNetwork = null;
                    }
                }
            }
        };
        this.mNetPolicyListener = new android.net.NetworkPolicyManager.Listener() { // from class: com.android.server.job.controllers.ConnectivityController.4
            public void onRestrictBackgroundChanged(boolean restrictBackground) {
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "onRestrictBackgroundChanged: " + restrictBackground);
                }
                com.android.server.job.controllers.ConnectivityController.this.mHandler.obtainMessage(2).sendToTarget();
            }

            public void onUidPoliciesChanged(int uid, int uidPolicies) {
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "onUidPoliciesChanged: " + uid);
                }
                com.android.server.job.controllers.ConnectivityController.this.mHandler.obtainMessage(3, uid, com.android.server.job.controllers.ConnectivityController.this.mNetPolicyManager.getRestrictBackgroundStatus(uid)).sendToTarget();
            }
        };
        this.mHandler = new com.android.server.job.controllers.ConnectivityController.CcHandler(com.android.server.AppSchedulingModuleThread.get().getLooper());
        this.mCcConfig = new com.android.server.job.controllers.ConnectivityController.CcConfig();
        this.mConnManager = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
        this.mNetPolicyManager = (android.net.NetworkPolicyManager) this.mContext.getSystemService(android.net.NetworkPolicyManager.class);
        this.mNetPolicyManagerInternal = (com.android.server.net.NetworkPolicyManagerInternal) com.android.server.LocalServices.getService(com.android.server.net.NetworkPolicyManagerInternal.class);
        this.mFlexibilityController = flexibilityController;
        android.net.NetworkRequest request = new android.net.NetworkRequest.Builder().clearCapabilities().build();
        this.mConnManager.registerNetworkCallback(request, this.mNetworkCallback);
        this.mNetPolicyManager.registerListener(this.mNetPolicyListener);
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
            sNetworkTransportAffinities.clear();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void startTrackingLocked() {
        if (com.android.server.job.Flags.batchConnectivityJobsPerNetwork()) {
            this.mConnManager.registerSystemDefaultNetworkCallback(this.mDefaultNetworkCallback, this.mHandler);
            this.mConnManager.addDefaultNetworkActiveListener(this);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus lastJob) {
        if (jobStatus.hasConnectivityConstraint()) {
            com.android.server.job.controllers.ConnectivityController.UidStats uidStats = getUidStats(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), false);
            if (wouldBeReadyWithConstraintLocked(jobStatus, 268435456)) {
                uidStats.numReadyWithConnectivity++;
            }
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.get(jobStatus.getSourceUid());
            if (jobs == null) {
                jobs = new android.util.ArraySet<>();
                this.mTrackedJobs.put(jobStatus.getSourceUid(), jobs);
            }
            jobs.add(jobStatus);
            jobStatus.setTrackingController(2);
            updateConstraintsSatisfied(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.hasConnectivityConstraint()) {
            com.android.server.job.controllers.ConnectivityController.UidStats uidStats = getUidStats(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), true);
            uidStats.runningJobs.add(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void unprepareFromExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.hasConnectivityConstraint()) {
            com.android.server.job.controllers.ConnectivityController.UidStats uidStats = getUidStats(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), true);
            uidStats.runningJobs.remove(jobStatus);
            postAdjustCallbacks();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        if (jobStatus.clearTrackingController(2)) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.get(jobStatus.getSourceUid());
            if (jobs != null) {
                jobs.remove(jobStatus);
            }
            com.android.server.job.controllers.ConnectivityController.UidStats uidStats = getUidStats(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), true);
            uidStats.numReadyWithConnectivity--;
            uidStats.runningJobs.remove(jobStatus);
            maybeRevokeStandbyExceptionLocked(jobStatus);
            postAdjustCallbacks();
        }
    }

    @Override // com.android.server.job.controllers.RestrictingController
    public void startTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.hasConnectivityConstraint()) {
            updateConstraintsSatisfied(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.RestrictingController
    public void stopTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.hasConnectivityConstraint()) {
            updateConstraintsSatisfied(jobStatus);
        }
    }

    private com.android.server.job.controllers.ConnectivityController.UidStats getUidStats(int uid, java.lang.String packageName, boolean shouldExist) {
        com.android.server.job.controllers.ConnectivityController.UidStats us = this.mUidStats.get(uid);
        if (us == null) {
            if (shouldExist) {
                android.util.Slog.wtfStack(TAG, "UidStats was null after job for " + packageName + " was registered");
            }
            com.android.server.job.controllers.ConnectivityController.UidStats us2 = new com.android.server.job.controllers.ConnectivityController.UidStats(uid);
            this.mUidStats.append(uid, us2);
            return us2;
        }
        return us;
    }

    public boolean isNetworkAvailable(com.android.server.job.controllers.JobStatus job) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mAvailableNetworks.size(); i++) {
                android.net.Network network = this.mAvailableNetworks.keyAt(i);
                com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata metadata = this.mAvailableNetworks.valueAt(i);
                android.net.NetworkCapabilities capabilities = metadata == null ? null : metadata.networkCapabilities;
                boolean satisfied = isSatisfied(job, network, capabilities, this.mConstants);
                if (DEBUG) {
                    android.util.Slog.v(TAG, "isNetworkAvailable(" + job + ") with network " + network + " and capabilities " + capabilities + ". Satisfied=" + satisfied);
                }
                if (satisfied) {
                    return true;
                }
            }
            return false;
        }
    }

    void requestStandbyExceptionLocked(com.android.server.job.controllers.JobStatus job) {
        int uid = job.getSourceUid();
        boolean isExceptionRequested = isStandbyExceptionRequestedLocked(uid);
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mRequestedWhitelistJobs.get(uid);
        if (jobs == null) {
            jobs = new android.util.ArraySet<>();
            this.mRequestedWhitelistJobs.put(uid, jobs);
        }
        if (!jobs.add(job) || isExceptionRequested) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "requestStandbyExceptionLocked found exception already requested.");
            }
        } else {
            if (DEBUG) {
                android.util.Slog.i(TAG, "Requesting standby exception for UID: " + uid);
            }
            this.mNetPolicyManagerInternal.setAppIdleWhitelist(uid, true);
        }
    }

    boolean isStandbyExceptionRequestedLocked(int uid) {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> arraySet = this.mRequestedWhitelistJobs.get(uid);
        return arraySet != null && arraySet.size() > 0;
    }

    @Override // com.android.server.job.controllers.StateController
    public void evaluateStateLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (!jobStatus.hasConnectivityConstraint()) {
            return;
        }
        com.android.server.job.controllers.ConnectivityController.UidStats uidStats = getUidStats(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), true);
        if (jobStatus.shouldTreatAsExpeditedJob() || jobStatus.shouldTreatAsUserInitiatedJob()) {
            if (!jobStatus.isConstraintSatisfied(268435456)) {
                updateConstraintsSatisfied(jobStatus);
            }
        } else if (((jobStatus.isRequestedExpeditedJob() && !jobStatus.shouldTreatAsExpeditedJob()) || (jobStatus.getJob().isUserInitiated() && !jobStatus.shouldTreatAsUserInitiatedJob())) && jobStatus.isConstraintSatisfied(268435456)) {
            updateConstraintsSatisfied(jobStatus);
        }
        if (wouldBeReadyWithConstraintLocked(jobStatus, 268435456) && isNetworkAvailable(jobStatus)) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "evaluateStateLocked finds job " + jobStatus + " would be ready.");
            }
            uidStats.numReadyWithConnectivity++;
            requestStandbyExceptionLocked(jobStatus);
            return;
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "evaluateStateLocked finds job " + jobStatus + " would not be ready.");
        }
        maybeRevokeStandbyExceptionLocked(jobStatus);
    }

    @Override // com.android.server.job.controllers.StateController
    public void reevaluateStateLocked(int uid) {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.get(uid);
        if (jobs == null) {
            return;
        }
        for (int i = jobs.size() - 1; i >= 0; i--) {
            evaluateStateLocked(jobs.valueAt(i));
        }
    }

    void maybeRevokeStandbyExceptionLocked(com.android.server.job.controllers.JobStatus job) {
        int uid = job.getSourceUid();
        if (!isStandbyExceptionRequestedLocked(uid)) {
            return;
        }
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mRequestedWhitelistJobs.get(uid);
        if (jobs == null) {
            android.util.Slog.wtf(TAG, "maybeRevokeStandbyExceptionLocked found null jobs array even though a standby exception has been requested.");
            return;
        }
        if (!jobs.remove(job) || jobs.size() > 0) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "maybeRevokeStandbyExceptionLocked not revoking because there are still " + jobs.size() + " jobs left.");
                return;
            }
            return;
        }
        revokeStandbyExceptionLocked(uid);
    }

    private void revokeStandbyExceptionLocked(int uid) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "Revoking standby exception for UID: " + uid);
        }
        this.mNetPolicyManagerInternal.setAppIdleWhitelist(uid, false);
        this.mRequestedWhitelistJobs.remove(uid);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onAppRemovedLocked(java.lang.String pkgName, int uid) {
        if (this.mService.getPackagesForUidLocked(uid) == null) {
            this.mTrackedJobs.delete(uid);
            this.mBackgroundMeteredAllowed.delete(uid);
            com.android.server.job.controllers.ConnectivityController.UidStats uidStats = (com.android.server.job.controllers.ConnectivityController.UidStats) this.mUidStats.removeReturnOld(uid);
            unregisterDefaultNetworkCallbackLocked(uid, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
            this.mSortedStats.remove(uidStats);
            registerPendingUidCallbacksLocked();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserRemovedLocked(int userId) {
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        for (int u = this.mUidStats.size() - 1; u >= 0; u--) {
            com.android.server.job.controllers.ConnectivityController.UidStats uidStats = this.mUidStats.valueAt(u);
            if (android.os.UserHandle.getUserId(uidStats.uid) == userId) {
                unregisterDefaultNetworkCallbackLocked(uidStats.uid, nowElapsed);
                this.mSortedStats.remove(uidStats);
                this.mUidStats.removeAt(u);
            }
        }
        for (int u2 = this.mBackgroundMeteredAllowed.size() - 1; u2 >= 0; u2--) {
            int uid = this.mBackgroundMeteredAllowed.keyAt(u2);
            if (android.os.UserHandle.getUserId(uid) == userId) {
                this.mBackgroundMeteredAllowed.removeAt(u2);
            }
        }
        postAdjustCallbacks();
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUidBiasChangedLocked(int uid, int prevBias, int newBias) {
        com.android.server.job.controllers.ConnectivityController.UidStats uidStats = this.mUidStats.get(uid);
        if (uidStats != null && uidStats.baseBias != newBias) {
            uidStats.baseBias = newBias;
            postAdjustCallbacks();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onBatteryStateChangedLocked() {
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForUpdatedConstantsLocked() {
        this.mCcConfig.mShouldReprocessNetworkCapabilities = false;
        this.mCcConfig.mFlexIsEnabled = this.mFlexibilityController.isEnabled();
    }

    @Override // com.android.server.job.controllers.StateController
    public void processConstantLocked(android.provider.DeviceConfig.Properties properties, java.lang.String key) {
        this.mCcConfig.processConstantLocked(properties, key);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onConstantsUpdatedLocked() {
        if (this.mCcConfig.mShouldReprocessNetworkCapabilities || this.mFlexibilityController.isEnabled() != this.mCcConfig.mFlexIsEnabled) {
            com.android.server.AppSchedulingModuleThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.ConnectivityController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onConstantsUpdatedLocked$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConstantsUpdatedLocked$0() {
        boolean flexAffinitiesChanged = false;
        boolean flexAffinitiesSatisfied = false;
        synchronized (this.mLock) {
            for (int i = 0; i < this.mAvailableNetworks.size(); i++) {
                com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata metadata = this.mAvailableNetworks.valueAt(i);
                if (metadata != null) {
                    if (updateTransportAffinitySatisfaction(metadata)) {
                        flexAffinitiesChanged = true;
                    }
                    flexAffinitiesSatisfied |= metadata.satisfiesTransportAffinities;
                }
            }
            if (flexAffinitiesChanged) {
                this.mFlexibilityController.setConstraintSatisfied(268435456, flexAffinitiesSatisfied, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                updateAllTrackedJobsLocked(false);
            }
        }
    }

    private boolean isUsable(android.net.NetworkCapabilities capabilities) {
        return capabilities != null && capabilities.hasCapability(21);
    }

    private boolean isInsane(com.android.server.job.controllers.JobStatus jobStatus, android.net.Network network, android.net.NetworkCapabilities capabilities, com.android.server.job.JobSchedulerService.Constants constants) {
        java.lang.String str;
        long maxJobExecutionTimeMs = this.mService.getMaxJobExecutionTimeMs(jobStatus);
        long minimumChunkBytes = jobStatus.getMinimumNetworkChunkBytes();
        if (minimumChunkBytes != -1) {
            long bandwidthDown = capabilities.getLinkDownstreamBandwidthKbps();
            if (bandwidthDown > 0) {
                long estimatedMillis = calculateTransferTimeMs(minimumChunkBytes, bandwidthDown);
                if (estimatedMillis > maxJobExecutionTimeMs) {
                    android.util.Slog.w(TAG, "Minimum chunk " + minimumChunkBytes + " bytes over " + bandwidthDown + " kbps network would take " + estimatedMillis + "ms and job has " + maxJobExecutionTimeMs + "ms to run; that's insane!");
                    return true;
                }
                str = "ms to run; that's insane!";
            } else {
                str = "ms to run; that's insane!";
            }
            long bandwidthUp = capabilities.getLinkUpstreamBandwidthKbps();
            if (bandwidthUp <= 0) {
                return false;
            }
            long estimatedMillis2 = calculateTransferTimeMs(minimumChunkBytes, bandwidthUp);
            if (estimatedMillis2 > maxJobExecutionTimeMs) {
                android.util.Slog.w(TAG, "Minimum chunk " + minimumChunkBytes + " bytes over " + bandwidthUp + " kbps network would take " + estimatedMillis2 + "ms and job has " + maxJobExecutionTimeMs + str);
                return true;
            }
            return false;
        }
        if (capabilities.hasCapability(11) && this.mService.isBatteryCharging()) {
            return false;
        }
        long downloadBytes = jobStatus.getEstimatedNetworkDownloadBytes();
        if (downloadBytes != -1) {
            long bandwidth = capabilities.getLinkDownstreamBandwidthKbps();
            if (bandwidth > 0) {
                long estimatedMillis3 = calculateTransferTimeMs(downloadBytes, bandwidth);
                if (estimatedMillis3 > maxJobExecutionTimeMs) {
                    android.util.Slog.w(TAG, "Estimated " + downloadBytes + " download bytes over " + bandwidth + " kbps network would take " + estimatedMillis3 + "ms and job has " + maxJobExecutionTimeMs + "ms to run; that's insane!");
                    return true;
                }
            }
        }
        long uploadBytes = jobStatus.getEstimatedNetworkUploadBytes();
        if (uploadBytes != -1) {
            long bandwidth2 = capabilities.getLinkUpstreamBandwidthKbps();
            if (bandwidth2 <= 0) {
                return false;
            }
            long estimatedMillis4 = calculateTransferTimeMs(uploadBytes, bandwidth2);
            if (estimatedMillis4 > maxJobExecutionTimeMs) {
                android.util.Slog.w(TAG, "Estimated " + uploadBytes + " upload bytes over " + bandwidth2 + " kbps network would take " + estimatedMillis4 + "ms and job has " + maxJobExecutionTimeMs + "ms to run; that's insane!");
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isMeteredAllowed(com.android.server.job.controllers.JobStatus jobStatus, android.net.NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.hasCapability(11) || networkCapabilities.hasCapability(25)) {
            return true;
        }
        int uid = jobStatus.getSourceUid();
        int procState = this.mService.getUidProcState(uid);
        int capabilities = this.mService.getUidCapabilities(uid);
        boolean currentStateAllows = procState != -1 && procState < 6 && android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(procState, capabilities);
        if (DEBUG) {
            android.util.Slog.d(TAG, "UID " + uid + " current state allows metered network=" + currentStateAllows + " procState=" + android.app.ActivityManager.procStateToString(procState) + " capabilities=" + android.app.ActivityManager.getCapabilitiesSummary(capabilities));
        }
        if (currentStateAllows) {
            return true;
        }
        if ((jobStatus.getFlags() & 1) != 0) {
            int mergedCapabilities = android.net.NetworkPolicyManager.getDefaultProcessNetworkCapabilities(4) | capabilities;
            boolean wouldBeAllowed = android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(4, mergedCapabilities);
            if (DEBUG) {
                android.util.Slog.d(TAG, "UID " + uid + " willBeForeground flag allows metered network=" + wouldBeAllowed + " capabilities=" + android.app.ActivityManager.getCapabilitiesSummary(mergedCapabilities));
            }
            if (wouldBeAllowed) {
                return true;
            }
        }
        if (jobStatus.shouldTreatAsUserInitiatedJob()) {
            int mergedCapabilities2 = capabilities | 32 | android.net.NetworkPolicyManager.getDefaultProcessNetworkCapabilities(6);
            boolean wouldBeAllowed2 = android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(6, mergedCapabilities2);
            if (DEBUG) {
                android.util.Slog.d(TAG, "UID " + uid + " UI job state allows metered network=" + wouldBeAllowed2 + " capabilities=" + mergedCapabilities2);
            }
            if (wouldBeAllowed2) {
                return true;
            }
        }
        if (this.mBackgroundMeteredAllowed.indexOfKey(uid) >= 0) {
            return this.mBackgroundMeteredAllowed.get(uid);
        }
        boolean allowed = this.mNetPolicyManager.getRestrictBackgroundStatus(uid) != 3;
        if (DEBUG) {
            android.util.Slog.d(TAG, "UID " + uid + " allowed in data saver=" + allowed);
        }
        this.mBackgroundMeteredAllowed.put(uid, allowed);
        return allowed;
    }

    public long getEstimatedTransferTimeMs(com.android.server.job.controllers.JobStatus jobStatus) {
        android.net.NetworkCapabilities capabilities;
        long downloadBytes = jobStatus.getEstimatedNetworkDownloadBytes();
        long uploadBytes = jobStatus.getEstimatedNetworkUploadBytes();
        if ((downloadBytes == -1 && uploadBytes == -1) || jobStatus.network == null || (capabilities = getNetworkCapabilities(jobStatus.network)) == null) {
            return -1L;
        }
        long estimatedDownloadTimeMs = calculateTransferTimeMs(downloadBytes, capabilities.getLinkDownstreamBandwidthKbps());
        long estimatedUploadTimeMs = calculateTransferTimeMs(uploadBytes, capabilities.getLinkUpstreamBandwidthKbps());
        if (estimatedDownloadTimeMs == -1) {
            return estimatedUploadTimeMs;
        }
        if (estimatedUploadTimeMs == -1) {
            return estimatedDownloadTimeMs;
        }
        return estimatedDownloadTimeMs + estimatedUploadTimeMs;
    }

    static long calculateTransferTimeMs(long transferBytes, long bandwidthKbps) {
        if (transferBytes == -1 || bandwidthKbps <= 0) {
            return -1L;
        }
        return (transferBytes * 1000) / ((1000 * bandwidthKbps) / 8);
    }

    private static boolean isCongestionDelayed(com.android.server.job.controllers.JobStatus jobStatus, android.net.Network network, android.net.NetworkCapabilities capabilities, com.android.server.job.JobSchedulerService.Constants constants) {
        return !capabilities.hasCapability(20) && jobStatus.getFractionRunTime() < constants.CONN_CONGESTION_DELAY_FRAC;
    }

    private boolean isStrongEnough(com.android.server.job.controllers.JobStatus jobStatus, android.net.NetworkCapabilities capabilities, com.android.server.job.JobSchedulerService.Constants constants) {
        int priority = jobStatus.getEffectivePriority();
        if (priority >= 400 || !constants.CONN_USE_CELL_SIGNAL_STRENGTH || !capabilities.hasTransport(0) || capabilities.hasTransport(4)) {
            return true;
        }
        int signalStrength = 0;
        java.util.Set<java.lang.Integer> subscriptionIds = capabilities.getSubscriptionIds();
        java.util.Iterator<java.lang.Integer> it = subscriptionIds.iterator();
        while (it.hasNext()) {
            int subId = it.next().intValue();
            com.android.server.job.controllers.ConnectivityController.CellSignalStrengthCallback callback = this.mSignalStrengths.get(subId);
            if (callback != null) {
                signalStrength = java.lang.Math.max(signalStrength, callback.signalStrength);
            } else {
                android.util.Slog.wtf(TAG, "Subscription ID " + subId + " doesn't have a registered callback");
            }
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Cell signal strength for job=" + signalStrength);
        }
        if (signalStrength <= 1) {
            if (priority > 300) {
                return true;
            }
            if (priority < 300) {
                return false;
            }
            return (this.mService.isBatteryCharging() && this.mService.isBatteryNotLow()) || jobStatus.getFractionRunTime() > constants.CONN_PREFETCH_RELAX_FRAC;
        }
        if (signalStrength > 2 || priority >= 200) {
            return true;
        }
        if (this.mService.isBatteryCharging() && this.mService.isBatteryNotLow()) {
            return true;
        }
        com.android.server.job.controllers.ConnectivityController.UidStats uidStats = getUidStats(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), true);
        return uidStats.runningJobs.contains(jobStatus);
    }

    private static android.net.NetworkCapabilities.Builder copyCapabilities(android.net.NetworkRequest request) {
        android.net.NetworkCapabilities.Builder builder = new android.net.NetworkCapabilities.Builder();
        for (int transport : request.getTransportTypes()) {
            builder.addTransportType(transport);
        }
        for (int capability : request.getCapabilities()) {
            builder.addCapability(capability);
        }
        return builder;
    }

    private static boolean isStrictSatisfied(com.android.server.job.controllers.JobStatus jobStatus, android.net.Network network, android.net.NetworkCapabilities capabilities, com.android.server.job.JobSchedulerService.Constants constants) {
        if (jobStatus.getEffectiveStandbyBucket() == 5 && !jobStatus.isConstraintSatisfied(16777216)) {
            android.net.NetworkCapabilities.Builder builder = copyCapabilities(jobStatus.getJob().getRequiredNetwork());
            builder.addCapability(11);
            return builder.build().satisfiedByNetworkCapabilities(capabilities);
        }
        return jobStatus.getJob().getRequiredNetwork().canBeSatisfiedBy(capabilities);
    }

    private boolean isRelaxedSatisfied(com.android.server.job.controllers.JobStatus jobStatus, android.net.Network network, android.net.NetworkCapabilities capabilities, com.android.server.job.JobSchedulerService.Constants constants) {
        if (!jobStatus.getJob().isPrefetch() || jobStatus.getStandbyBucket() == 5) {
            return false;
        }
        long estDownloadBytes = jobStatus.getEstimatedNetworkDownloadBytes();
        if (estDownloadBytes <= 0) {
            return false;
        }
        if (com.android.server.job.Flags.relaxPrefetchConnectivityConstraintOnlyOnCharger() && (!this.mService.isBatteryCharging() || !this.mService.isBatteryNotLow())) {
            return false;
        }
        android.net.NetworkCapabilities.Builder builder = copyCapabilities(jobStatus.getJob().getRequiredNetwork());
        builder.removeCapability(11);
        if (!builder.build().satisfiedByNetworkCapabilities(capabilities) || jobStatus.getFractionRunTime() <= constants.CONN_PREFETCH_RELAX_FRAC) {
            return false;
        }
        long opportunisticQuotaBytes = this.mNetPolicyManagerInternal.getSubscriptionOpportunisticQuota(network, 1);
        long estUploadBytes = jobStatus.getEstimatedNetworkUploadBytes();
        long estimatedBytes = (estUploadBytes != -1 ? estUploadBytes : 0L) + estDownloadBytes;
        return opportunisticQuotaBytes >= estimatedBytes;
    }

    boolean isSatisfied(com.android.server.job.controllers.JobStatus jobStatus, android.net.Network network, android.net.NetworkCapabilities capabilities, com.android.server.job.JobSchedulerService.Constants constants) {
        if (network == null || capabilities == null || !isUsable(capabilities) || isInsane(jobStatus, network, capabilities, constants) || !isMeteredAllowed(jobStatus, capabilities) || isCongestionDelayed(jobStatus, network, capabilities, constants) || !isStrongEnough(jobStatus, capabilities, constants)) {
            return false;
        }
        return isStrictSatisfied(jobStatus, network, capabilities, constants) || isRelaxedSatisfied(jobStatus, network, capabilities, constants);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateTransportAffinitySatisfaction(com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cachedNetworkMetadata) {
        boolean satisfiesAffinities = satisfiesTransportAffinities(cachedNetworkMetadata.networkCapabilities);
        if (cachedNetworkMetadata.satisfiesTransportAffinities != satisfiesAffinities) {
            cachedNetworkMetadata.satisfiesTransportAffinities = satisfiesAffinities;
            return true;
        }
        return false;
    }

    private boolean satisfiesTransportAffinities(android.net.NetworkCapabilities capabilities) {
        if (!this.mFlexibilityController.isEnabled()) {
            return true;
        }
        if (capabilities == null) {
            android.util.Slog.wtf(TAG, "Network constraint satisfied with null capabilities");
            return !this.mCcConfig.AVOID_UNDEFINED_TRANSPORT_AFFINITY;
        }
        if (sNetworkTransportAffinities.size() == 0) {
            return !this.mCcConfig.AVOID_UNDEFINED_TRANSPORT_AFFINITY;
        }
        int[] transports = capabilities.getTransportTypes();
        if (transports.length == 0) {
            return !this.mCcConfig.AVOID_UNDEFINED_TRANSPORT_AFFINITY;
        }
        for (int t : transports) {
            int affinity = sNetworkTransportAffinities.get(t, 0);
            if (DEBUG) {
                android.util.Slog.d(TAG, "satisfiesTransportAffinities transport=" + t + " aff=" + affinity);
            }
            switch (affinity) {
                case 0:
                    if (this.mCcConfig.AVOID_UNDEFINED_TRANSPORT_AFFINITY) {
                        return false;
                    }
                    break;
                    break;
                case 2:
                    return false;
            }
        }
        return true;
    }

    private void maybeRegisterDefaultNetworkCallbackLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        int sourceUid = jobStatus.getSourceUid();
        if (this.mCurrentDefaultNetworkCallbacks.contains(sourceUid)) {
            return;
        }
        com.android.server.job.controllers.ConnectivityController.UidStats uidStats = getUidStats(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), true);
        if (!this.mSortedStats.contains(uidStats)) {
            this.mSortedStats.add(uidStats);
        }
        if (this.mCurrentDefaultNetworkCallbacks.size() >= 125) {
            postAdjustCallbacks();
        } else {
            registerPendingUidCallbacksLocked();
        }
    }

    private void registerPendingUidCallbacksLocked() {
        int numCallbacks = this.mCurrentDefaultNetworkCallbacks.size();
        int numPending = this.mSortedStats.size();
        if (numPending < numCallbacks) {
            android.util.Slog.wtf(TAG, "There are more registered callbacks than sorted UIDs: " + numCallbacks + " vs " + numPending);
        }
        for (int i = numCallbacks; i < numPending && i < 125; i++) {
            com.android.server.job.controllers.ConnectivityController.UidStats uidStats = this.mSortedStats.get(i);
            com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback callback = (com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback) this.mDefaultNetworkCallbackPool.acquire();
            if (callback == null) {
                callback = new com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback();
            }
            callback.setUid(uidStats.uid);
            this.mCurrentDefaultNetworkCallbacks.append(uidStats.uid, callback);
            this.mConnManager.registerDefaultNetworkCallbackForUid(uidStats.uid, callback, this.mHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postAdjustCallbacks() {
        postAdjustCallbacks(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postAdjustCallbacks(long delayMs) {
        this.mHandler.sendEmptyMessageDelayed(0, delayMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeAdjustRegisteredCallbacksLocked() {
        this.mHandler.removeMessages(0);
        int count = this.mUidStats.size();
        if (count == this.mCurrentDefaultNetworkCallbacks.size()) {
            return;
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (nowElapsed - this.mLastCallbackAdjustmentTimeElapsed < 1000) {
            postAdjustCallbacks(1000L);
            return;
        }
        this.mLastCallbackAdjustmentTimeElapsed = nowElapsed;
        this.mSortedStats.clear();
        for (int u = 0; u < this.mUidStats.size(); u++) {
            com.android.server.job.controllers.ConnectivityController.UidStats us = this.mUidStats.valueAt(u);
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.get(us.uid);
            if (jobs == null || jobs.size() == 0) {
                unregisterDefaultNetworkCallbackLocked(us.uid, nowElapsed);
            } else {
                if (us.lastUpdatedElapsed + 30000 < nowElapsed) {
                    us.earliestEnqueueTime = Long.MAX_VALUE;
                    us.earliestEJEnqueueTime = Long.MAX_VALUE;
                    us.earliestUIJEnqueueTime = Long.MAX_VALUE;
                    us.numReadyWithConnectivity = 0;
                    us.numRequestedNetworkAvailable = 0;
                    us.numRegular = 0;
                    us.numEJs = 0;
                    us.numUIJs = 0;
                    for (int j = 0; j < jobs.size(); j++) {
                        com.android.server.job.controllers.JobStatus job = jobs.valueAt(j);
                        if (wouldBeReadyWithConstraintLocked(job, 268435456)) {
                            us.numReadyWithConnectivity++;
                            if (isNetworkAvailable(job)) {
                                us.numRequestedNetworkAvailable++;
                            }
                            us.earliestEnqueueTime = java.lang.Math.min(us.earliestEnqueueTime, job.enqueueTime);
                            if (job.shouldTreatAsExpeditedJob() || job.startedAsExpeditedJob) {
                                us.earliestEJEnqueueTime = java.lang.Math.min(us.earliestEJEnqueueTime, job.enqueueTime);
                            } else if (job.shouldTreatAsUserInitiatedJob()) {
                                us.earliestUIJEnqueueTime = java.lang.Math.min(us.earliestUIJEnqueueTime, job.enqueueTime);
                            }
                        }
                        if (job.shouldTreatAsExpeditedJob() || job.startedAsExpeditedJob) {
                            us.numEJs++;
                        } else if (job.shouldTreatAsUserInitiatedJob()) {
                            us.numUIJs++;
                        } else {
                            us.numRegular++;
                        }
                    }
                    us.lastUpdatedElapsed = nowElapsed;
                }
                this.mSortedStats.add(us);
            }
        }
        this.mSortedStats.sort(this.mUidStatsComparator);
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
        for (int i = this.mSortedStats.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.ConnectivityController.UidStats us2 = this.mSortedStats.get(i);
            if (i >= 125) {
                if (unregisterDefaultNetworkCallbackLocked(us2.uid, nowElapsed)) {
                    changedJobs.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) this.mTrackedJobs.get(us2.uid));
                }
            } else if (this.mCurrentDefaultNetworkCallbacks.get(us2.uid) == null) {
                com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback defaultNetworkCallback = (com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback) this.mDefaultNetworkCallbackPool.acquire();
                if (defaultNetworkCallback == null) {
                    defaultNetworkCallback = new com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback();
                }
                defaultNetworkCallback.setUid(us2.uid);
                this.mCurrentDefaultNetworkCallbacks.append(us2.uid, defaultNetworkCallback);
                this.mConnManager.registerDefaultNetworkCallbackForUid(us2.uid, defaultNetworkCallback, this.mHandler);
            }
        }
        int i2 = changedJobs.size();
        if (i2 > 0) {
            this.mStateChangedListener.onControllerStateChanged(changedJobs);
        }
    }

    private boolean unregisterDefaultNetworkCallbackLocked(int uid, long nowElapsed) {
        com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback defaultNetworkCallback = this.mCurrentDefaultNetworkCallbacks.get(uid);
        if (defaultNetworkCallback == null) {
            return false;
        }
        this.mCurrentDefaultNetworkCallbacks.remove(uid);
        this.mConnManager.unregisterNetworkCallback(defaultNetworkCallback);
        this.mDefaultNetworkCallbackPool.release(defaultNetworkCallback);
        defaultNetworkCallback.clear();
        boolean changed = false;
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.get(uid);
        if (jobs != null) {
            for (int j = jobs.size() - 1; j >= 0; j--) {
                changed |= updateConstraintsSatisfied(jobs.valueAt(j), nowElapsed, null, null);
            }
        }
        return changed;
    }

    public android.net.NetworkCapabilities getNetworkCapabilities(android.net.Network network) {
        com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata metadata = getNetworkMetadata(network);
        if (metadata == null) {
            return null;
        }
        return metadata.networkCapabilities;
    }

    private com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata getNetworkMetadata(android.net.Network network) {
        com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cachedNetworkMetadata;
        if (network == null) {
            return null;
        }
        synchronized (this.mLock) {
            cachedNetworkMetadata = this.mAvailableNetworks.get(network);
        }
        return cachedNetworkMetadata;
    }

    private android.net.Network getNetworkLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        int unbypassableBlockedReasons;
        com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback defaultNetworkCallback = this.mCurrentDefaultNetworkCallbacks.get(jobStatus.getSourceUid());
        if (defaultNetworkCallback == null) {
            return null;
        }
        com.android.server.job.controllers.ConnectivityController.UidStats uidStats = this.mUidStats.get(jobStatus.getSourceUid());
        if (uidStats.baseBias >= 30 || (jobStatus.getFlags() & 1) != 0) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Using FG bypass for " + jobStatus.getSourceUid());
            }
            unbypassableBlockedReasons = -196680;
        } else if (jobStatus.shouldTreatAsUserInitiatedJob()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Using UI bypass for " + jobStatus.getSourceUid());
            }
            unbypassableBlockedReasons = -196680;
        } else if (jobStatus.shouldTreatAsExpeditedJob() || jobStatus.startedAsExpeditedJob) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Using EJ bypass for " + jobStatus.getSourceUid());
            }
            unbypassableBlockedReasons = UNBYPASSABLE_EJ_BLOCKED_REASONS;
        } else {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Using BG bypass for " + jobStatus.getSourceUid());
            }
            unbypassableBlockedReasons = UNBYPASSABLE_BG_BLOCKED_REASONS;
        }
        if ((defaultNetworkCallback.mBlockedReasons & unbypassableBlockedReasons) != 0) {
            return null;
        }
        return defaultNetworkCallback.mDefaultNetwork;
    }

    private boolean updateConstraintsSatisfied(com.android.server.job.controllers.JobStatus jobStatus) {
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback defaultNetworkCallback = this.mCurrentDefaultNetworkCallbacks.get(jobStatus.getSourceUid());
        if (defaultNetworkCallback == null) {
            maybeRegisterDefaultNetworkCallbackLocked(jobStatus);
            return updateConstraintsSatisfied(jobStatus, nowElapsed, null, null);
        }
        android.net.Network network = getNetworkLocked(jobStatus);
        com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata networkMetadata = getNetworkMetadata(network);
        return updateConstraintsSatisfied(jobStatus, nowElapsed, network, networkMetadata);
    }

    private boolean updateConstraintsSatisfied(com.android.server.job.controllers.JobStatus jobStatus, long nowElapsed, android.net.Network network, com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata networkMetadata) {
        android.net.NetworkCapabilities capabilities = networkMetadata == null ? null : networkMetadata.networkCapabilities;
        boolean satisfied = isSatisfied(jobStatus, network, capabilities, this.mConstants);
        boolean z = false;
        if (!satisfied && jobStatus.network != null && this.mService.isCurrentlyRunningLocked(jobStatus) && isSatisfied(jobStatus, jobStatus.network, getNetworkCapabilities(jobStatus.network), this.mConstants)) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "Not reassigning network from " + jobStatus.network + " to " + network + " for running job " + jobStatus);
            }
            return false;
        }
        boolean changed = jobStatus.setConnectivityConstraintSatisfied(nowElapsed, satisfied);
        if (satisfied && networkMetadata != null && networkMetadata.satisfiesTransportAffinities) {
            z = true;
        }
        jobStatus.setTransportAffinitiesSatisfied(z);
        if (jobStatus.canApplyTransportAffinities()) {
            jobStatus.setFlexibilityConstraintSatisfied(nowElapsed, this.mFlexibilityController.isFlexibilitySatisfiedLocked(jobStatus));
        }
        if (!changed && satisfied && jobStatus.network != null && this.mService.isCurrentlyRunningLocked(jobStatus)) {
            this.mStateChangedListener.onNetworkChanged(jobStatus, network);
        }
        jobStatus.network = network;
        if (DEBUG) {
            android.util.Slog.i(TAG, "Connectivity " + (changed ? "CHANGED" : "unchanged") + " for " + jobStatus + ": usable=" + isUsable(capabilities) + " satisfied=" + satisfied);
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllTrackedJobsLocked(boolean allowThrottle) {
        if (allowThrottle) {
            long throttleTimeLeftMs = (this.mLastAllJobUpdateTimeElapsed + this.mConstants.CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS) - com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (throttleTimeLeftMs > 0) {
                android.os.Message msg = this.mHandler.obtainMessage(1, 1, 0);
                this.mHandler.sendMessageDelayed(msg, throttleTimeLeftMs);
                return;
            }
        }
        this.mHandler.removeMessages(1);
        updateTrackedJobsLocked(-1, (android.net.Network) null);
        this.mLastAllJobUpdateTimeElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTrackedJobsLocked(int filterUid, android.net.Network filterNetwork) {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs;
        if (filterUid == -1) {
            changedJobs = new android.util.ArraySet<>();
            for (int i = this.mTrackedJobs.size() - 1; i >= 0; i--) {
                if (updateTrackedJobsLocked(this.mTrackedJobs.valueAt(i), filterNetwork)) {
                    changedJobs.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) this.mTrackedJobs.valueAt(i));
                }
            }
        } else if (updateTrackedJobsLocked(this.mTrackedJobs.get(filterUid), filterNetwork)) {
            changedJobs = this.mTrackedJobs.get(filterUid);
        } else {
            changedJobs = null;
        }
        if (changedJobs != null && changedJobs.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(changedJobs);
        }
    }

    private boolean updateTrackedJobsLocked(android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs, android.net.Network filterNetwork) {
        if (jobs == null || jobs.size() == 0) {
            return false;
        }
        com.android.server.job.controllers.ConnectivityController.UidDefaultNetworkCallback defaultNetworkCallback = this.mCurrentDefaultNetworkCallbacks.get(jobs.valueAt(0).getSourceUid());
        if (defaultNetworkCallback == null) {
            return false;
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        boolean changed = false;
        for (int i = jobs.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus js = jobs.valueAt(i);
            android.net.Network net = getNetworkLocked(js);
            boolean match = filterNetwork == null || java.util.Objects.equals(filterNetwork, net);
            if (match || !java.util.Objects.equals(js.network, net)) {
                changed = updateConstraintsSatisfied(js, nowElapsed, net, getNetworkMetadata(net)) | changed;
            }
        }
        return changed;
    }

    public boolean isNetworkInStateForJobRunLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.network == null) {
            return false;
        }
        if (jobStatus.shouldTreatAsExpeditedJob() || jobStatus.shouldTreatAsUserInitiatedJob() || this.mService.getUidProcState(jobStatus.getSourceUid()) <= 5) {
            return true;
        }
        return isNetworkInStateForJobRunLocked(jobStatus.network);
    }

    boolean isNetworkInStateForJobRunLocked(android.net.Network network) {
        java.util.List<android.net.Network> underlyingNetworks;
        if (!com.android.server.job.Flags.batchConnectivityJobsPerNetwork()) {
            return true;
        }
        com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cachedNetworkMetadata = this.mAvailableNetworks.get(network);
        if (cachedNetworkMetadata == null) {
            return false;
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (cachedNetworkMetadata.defaultNetworkActivationLastConfirmedTimeElapsed + this.mCcConfig.NETWORK_ACTIVATION_EXPIRATION_MS > nowElapsed) {
            return true;
        }
        boolean inactiveForTooLong = cachedNetworkMetadata.capabilitiesFirstAcquiredTimeElapsed < nowElapsed - this.mCcConfig.NETWORK_ACTIVATION_MAX_WAIT_TIME_MS && cachedNetworkMetadata.defaultNetworkActivationLastConfirmedTimeElapsed < nowElapsed - this.mCcConfig.NETWORK_ACTIVATION_MAX_WAIT_TIME_MS;
        if (this.mSystemDefaultNetwork == null) {
            return inactiveForTooLong;
        }
        if (this.mSystemDefaultNetwork.equals(network)) {
            if (cachedNetworkMetadata.defaultNetworkActivationLastCheckTimeElapsed + this.mCcConfig.NETWORK_ACTIVATION_EXPIRATION_MS >= nowElapsed) {
                return false;
            }
            if (cachedNetworkMetadata.defaultNetworkActivationLastCheckTimeElapsed > cachedNetworkMetadata.defaultNetworkActivationLastConfirmedTimeElapsed) {
                return inactiveForTooLong;
            }
            cachedNetworkMetadata.defaultNetworkActivationLastCheckTimeElapsed = nowElapsed;
            boolean isActive = this.mConnManager.isDefaultNetworkActive();
            if (isActive) {
                cachedNetworkMetadata.defaultNetworkActivationLastConfirmedTimeElapsed = nowElapsed;
                return true;
            }
            return inactiveForTooLong;
        }
        android.net.NetworkCapabilities capabilities = cachedNetworkMetadata.networkCapabilities;
        if (capabilities == null || !capabilities.hasTransport(4) || (underlyingNetworks = capabilities.getUnderlyingNetworks()) == null) {
            return inactiveForTooLong;
        }
        if (underlyingNetworks.contains(this.mSystemDefaultNetwork)) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "Substituting system default network " + this.mSystemDefaultNetwork + " for VPN " + network);
            }
            return isNetworkInStateForJobRunLocked(this.mSystemDefaultNetwork);
        }
        for (int i = underlyingNetworks.size() - 1; i >= 0; i--) {
            if (isNetworkInStateForJobRunLocked(underlyingNetworks.get(i))) {
                return true;
            }
        }
        return inactiveForTooLong;
    }

    @Override // android.net.ConnectivityManager.OnNetworkActiveListener
    public void onNetworkActive() {
        synchronized (this.mLock) {
            if (this.mSystemDefaultNetwork == null) {
                android.util.Slog.wtf(TAG, "System default network is unknown but active");
                return;
            }
            com.android.server.job.controllers.ConnectivityController.CachedNetworkMetadata cachedNetworkMetadata = this.mAvailableNetworks.get(this.mSystemDefaultNetwork);
            if (cachedNetworkMetadata == null) {
                android.util.Slog.wtf(TAG, "System default network capabilities are unknown but active");
                return;
            }
            long jMillis = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            cachedNetworkMetadata.defaultNetworkActivationLastCheckTimeElapsed = jMillis;
            cachedNetworkMetadata.defaultNetworkActivationLastConfirmedTimeElapsed = jMillis;
            this.mHandler.sendEmptyMessage(4);
        }
    }

    private class CcHandler extends android.os.Handler {
        CcHandler(android.os.Looper looper) {
            super(looper);
        }

        /* JADX WARN: Removed duplicated region for block: B:47:0x00cc A[Catch: all -> 0x010a, TryCatch #4 {, blocks: (B:9:0x001c, B:11:0x0024, B:13:0x0027, B:15:0x002d, B:17:0x0030, B:19:0x003e, B:21:0x0041, B:23:0x0053, B:25:0x0066, B:27:0x007a, B:29:0x0084, B:32:0x008b, B:34:0x0093, B:53:0x00fd, B:37:0x00a2, B:39:0x00a9, B:43:0x00b8, B:46:0x00c4, B:47:0x00cc, B:49:0x00d2, B:51:0x00d8, B:52:0x00f6, B:54:0x0102, B:55:0x0107), top: B:113:0x001c, outer: #5 }] */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void handleMessage(android.os.Message r17) {
            /*
                Method dump skipped, instruction units count: 398
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.ConnectivityController.CcHandler.handleMessage(android.os.Message):void");
        }
    }

    class CcConfig {
        private static final java.lang.String CC_CONFIG_PREFIX = "conn_";
        private static final boolean DEFAULT_AVOID_UNDEFINED_TRANSPORT_AFFINITY = false;
        private static final long DEFAULT_NETWORK_ACTIVATION_EXPIRATION_MS = 10000;
        private static final long DEFAULT_NETWORK_ACTIVATION_MAX_WAIT_TIME_MS = 1860000;
        static final java.lang.String KEY_AVOID_UNDEFINED_TRANSPORT_AFFINITY = "conn_avoid_undefined_transport_affinity";
        private static final java.lang.String KEY_NETWORK_ACTIVATION_EXPIRATION_MS = "conn_network_activation_expiration_ms";
        private static final java.lang.String KEY_NETWORK_ACTIVATION_MAX_WAIT_TIME_MS = "conn_network_activation_max_wait_time_ms";
        private boolean mFlexIsEnabled = false;
        private boolean mShouldReprocessNetworkCapabilities = false;
        public boolean AVOID_UNDEFINED_TRANSPORT_AFFINITY = false;
        public long NETWORK_ACTIVATION_EXPIRATION_MS = 10000;
        public long NETWORK_ACTIVATION_MAX_WAIT_TIME_MS = DEFAULT_NETWORK_ACTIVATION_MAX_WAIT_TIME_MS;

        CcConfig() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0028  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void processConstantLocked(android.provider.DeviceConfig.Properties r6, java.lang.String r7) {
            /*
                r5 = this;
                int r0 = r7.hashCode()
                r1 = 0
                r2 = 1
                switch(r0) {
                    case -1221182095: goto L1e;
                    case 524268654: goto L14;
                    case 1011552586: goto La;
                    default: goto L9;
                }
            L9:
                goto L28
            La:
                java.lang.String r0 = "conn_avoid_undefined_transport_affinity"
                boolean r0 = r7.equals(r0)
                if (r0 == 0) goto L9
                r0 = r1
                goto L29
            L14:
                java.lang.String r0 = "conn_network_activation_max_wait_time_ms"
                boolean r0 = r7.equals(r0)
                if (r0 == 0) goto L9
                r0 = 2
                goto L29
            L1e:
                java.lang.String r0 = "conn_network_activation_expiration_ms"
                boolean r0 = r7.equals(r0)
                if (r0 == 0) goto L9
                r0 = r2
                goto L29
            L28:
                r0 = -1
            L29:
                switch(r0) {
                    case 0: goto L4e;
                    case 1: goto L3f;
                    case 2: goto L2d;
                    default: goto L2c;
                }
            L2c:
                goto L5a
            L2d:
                r0 = 1860000(0x1c61a0, double:9.18962E-318)
                long r0 = r6.getLong(r7, r0)
                long r3 = r5.NETWORK_ACTIVATION_MAX_WAIT_TIME_MS
                int r3 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
                if (r3 == 0) goto L5a
                r5.NETWORK_ACTIVATION_MAX_WAIT_TIME_MS = r0
                r5.mShouldReprocessNetworkCapabilities = r2
                goto L5a
            L3f:
                r0 = 10000(0x2710, double:4.9407E-320)
                long r0 = r6.getLong(r7, r0)
                long r2 = r5.NETWORK_ACTIVATION_EXPIRATION_MS
                int r2 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
                if (r2 == 0) goto L5a
                r5.NETWORK_ACTIVATION_EXPIRATION_MS = r0
                goto L5a
            L4e:
                boolean r0 = r6.getBoolean(r7, r1)
                boolean r1 = r5.AVOID_UNDEFINED_TRANSPORT_AFFINITY
                if (r1 == r0) goto L5a
                r5.AVOID_UNDEFINED_TRANSPORT_AFFINITY = r0
                r5.mShouldReprocessNetworkCapabilities = r2
            L5a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.ConnectivityController.CcConfig.processConstantLocked(android.provider.DeviceConfig$Properties, java.lang.String):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(android.util.IndentingPrintWriter pw) {
            pw.println();
            pw.print(com.android.server.job.controllers.ConnectivityController.class.getSimpleName());
            pw.println(":");
            pw.increaseIndent();
            pw.print(KEY_AVOID_UNDEFINED_TRANSPORT_AFFINITY, java.lang.Boolean.valueOf(this.AVOID_UNDEFINED_TRANSPORT_AFFINITY)).println();
            pw.print(KEY_NETWORK_ACTIVATION_EXPIRATION_MS, java.lang.Long.valueOf(this.NETWORK_ACTIVATION_EXPIRATION_MS)).println();
            pw.print(KEY_NETWORK_ACTIVATION_MAX_WAIT_TIME_MS, java.lang.Long.valueOf(this.NETWORK_ACTIVATION_MAX_WAIT_TIME_MS)).println();
            pw.decreaseIndent();
        }
    }

    private class UidDefaultNetworkCallback extends android.net.ConnectivityManager.NetworkCallback {
        private int mBlockedReasons;
        private android.net.Network mDefaultNetwork;
        private int mUid;

        private UidDefaultNetworkCallback() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setUid(int uid) {
            this.mUid = uid;
            this.mDefaultNetwork = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.mDefaultNetwork = null;
            this.mUid = -10000;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(android.net.Network network) {
            if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "default-onAvailable(" + this.mUid + "): " + network);
            }
        }

        public void onBlockedStatusChanged(android.net.Network network, int blockedReasons) {
            if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "default-onBlockedStatusChanged(" + this.mUid + "): " + network + " -> " + blockedReasons);
            }
            if (this.mUid == -10000) {
                return;
            }
            synchronized (com.android.server.job.controllers.ConnectivityController.this.mLock) {
                this.mDefaultNetwork = network;
                this.mBlockedReasons = blockedReasons;
                com.android.server.job.controllers.ConnectivityController.this.updateTrackedJobsLocked(this.mUid, network);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                android.util.Slog.v(com.android.server.job.controllers.ConnectivityController.TAG, "default-onLost(" + this.mUid + "): " + network);
            }
            if (this.mUid == -10000) {
                return;
            }
            synchronized (com.android.server.job.controllers.ConnectivityController.this.mLock) {
                if (java.util.Objects.equals(this.mDefaultNetwork, network)) {
                    this.mDefaultNetwork = null;
                    com.android.server.job.controllers.ConnectivityController.this.updateTrackedJobsLocked(this.mUid, network);
                    com.android.server.job.controllers.ConnectivityController.this.postAdjustCallbacks(1000L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpLocked(android.util.IndentingPrintWriter pw) {
            pw.print("UID: ");
            pw.print(this.mUid);
            pw.print("; ");
            if (this.mDefaultNetwork == null) {
                pw.print("No network");
            } else {
                pw.print("Network: ");
                pw.print(this.mDefaultNetwork);
                pw.print(" (blocked=");
                pw.print(android.net.NetworkPolicyManager.blockedReasonsToString(this.mBlockedReasons));
                pw.print(")");
            }
            pw.println();
        }
    }

    private static class CachedNetworkMetadata {
        public long capabilitiesFirstAcquiredTimeElapsed;
        public long defaultNetworkActivationLastCheckTimeElapsed;
        public long defaultNetworkActivationLastConfirmedTimeElapsed;
        public android.net.NetworkCapabilities networkCapabilities;
        public boolean satisfiesTransportAffinities;

        private CachedNetworkMetadata() {
        }

        public java.lang.String toString() {
            return "CNM{" + this.networkCapabilities.toString() + ", satisfiesTransportAffinities=" + this.satisfiesTransportAffinities + ", capabilitiesFirstAcquiredTimeElapsed=" + this.capabilitiesFirstAcquiredTimeElapsed + ", defaultNetworkActivationLastCheckTimeElapsed=" + this.defaultNetworkActivationLastCheckTimeElapsed + ", defaultNetworkActivationLastConfirmedTimeElapsed=" + this.defaultNetworkActivationLastConfirmedTimeElapsed + "}";
        }
    }

    private static class UidStats {
        public int baseBias;
        public long earliestEJEnqueueTime;
        public long earliestEnqueueTime;
        public long earliestUIJEnqueueTime;
        public long lastUpdatedElapsed;
        public int numEJs;
        public int numReadyWithConnectivity;
        public int numRegular;
        public int numRequestedNetworkAvailable;
        public int numUIJs;
        public final android.util.ArraySet<com.android.server.job.controllers.JobStatus> runningJobs;
        public final int uid;

        private UidStats(int uid) {
            this.runningJobs = new android.util.ArraySet<>();
            this.uid = uid;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpLocked(android.util.IndentingPrintWriter pw, long nowElapsed) {
            pw.print("UidStats{");
            pw.print("uid", java.lang.Integer.valueOf(this.uid));
            pw.print("pri", java.lang.Integer.valueOf(this.baseBias));
            pw.print("#run", java.lang.Integer.valueOf(this.runningJobs.size()));
            pw.print("#readyWithConn", java.lang.Integer.valueOf(this.numReadyWithConnectivity));
            pw.print("#netAvail", java.lang.Integer.valueOf(this.numRequestedNetworkAvailable));
            pw.print("#EJs", java.lang.Integer.valueOf(this.numEJs));
            pw.print("#reg", java.lang.Integer.valueOf(this.numRegular));
            pw.print("earliestEnqueue", java.lang.Long.valueOf(this.earliestEnqueueTime));
            pw.print("earliestEJEnqueue", java.lang.Long.valueOf(this.earliestEJEnqueueTime));
            pw.print("earliestUIJEnqueue", java.lang.Long.valueOf(this.earliestUIJEnqueueTime));
            pw.print("updated=");
            android.util.TimeUtils.formatDuration(this.lastUpdatedElapsed - nowElapsed, pw);
            pw.println("}");
        }
    }

    private class CellSignalStrengthCallback extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.SignalStrengthsListener {
        public int signalStrength;

        private CellSignalStrengthCallback() {
            this.signalStrength = 4;
        }

        @Override // android.telephony.TelephonyCallback.SignalStrengthsListener
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            synchronized (com.android.server.job.controllers.ConnectivityController.this.mLock) {
                int newSignalStrength = signalStrength.getLevel();
                if (com.android.server.job.controllers.ConnectivityController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.ConnectivityController.TAG, "Signal strength changing from " + this.signalStrength + " to " + newSignalStrength);
                    for (android.telephony.CellSignalStrength css : signalStrength.getCellSignalStrengths()) {
                        android.util.Slog.d(com.android.server.job.controllers.ConnectivityController.TAG, "CSS: " + css.getLevel() + " " + css);
                    }
                }
                if (this.signalStrength == newSignalStrength) {
                    return;
                }
                this.signalStrength = newSignalStrength;
                com.android.server.job.controllers.ConnectivityController.this.mHandler.obtainMessage(1, 1, 0).sendToTarget();
            }
        }
    }

    com.android.server.job.controllers.ConnectivityController.CcConfig getCcConfig() {
        return this.mCcConfig;
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
        this.mCcConfig.dump(pw);
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        pw.println("Aconfig flags:");
        pw.increaseIndent();
        pw.print(com.android.server.job.Flags.FLAG_RELAX_PREFETCH_CONNECTIVITY_CONSTRAINT_ONLY_ON_CHARGER, java.lang.Boolean.valueOf(com.android.server.job.Flags.relaxPrefetchConnectivityConstraintOnlyOnCharger()));
        pw.println();
        pw.decreaseIndent();
        pw.println();
        if (this.mRequestedWhitelistJobs.size() > 0) {
            pw.print("Requested standby exceptions:");
            for (int i = 0; i < this.mRequestedWhitelistJobs.size(); i++) {
                pw.print(" ");
                pw.print(this.mRequestedWhitelistJobs.keyAt(i));
                pw.print(" (");
                pw.print(this.mRequestedWhitelistJobs.valueAt(i).size());
                pw.print(" jobs)");
            }
            pw.println();
        }
        if (this.mAvailableNetworks.size() > 0) {
            pw.println("Available networks:");
            pw.increaseIndent();
            for (int i2 = 0; i2 < this.mAvailableNetworks.size(); i2++) {
                pw.print(this.mAvailableNetworks.keyAt(i2));
                pw.print(": ");
                pw.println(this.mAvailableNetworks.valueAt(i2));
            }
            pw.decreaseIndent();
        } else {
            pw.println("No available networks");
        }
        pw.println();
        if (this.mSignalStrengths.size() > 0) {
            pw.println("Subscription ID signal strengths:");
            pw.increaseIndent();
            for (int i3 = 0; i3 < this.mSignalStrengths.size(); i3++) {
                pw.print(this.mSignalStrengths.keyAt(i3));
                pw.print(": ");
                pw.println(this.mSignalStrengths.valueAt(i3).signalStrength);
            }
            pw.decreaseIndent();
        } else {
            pw.println("No cached signal strengths");
        }
        pw.println();
        if (this.mBackgroundMeteredAllowed.size() > 0) {
            pw.print("Background metered allowed: ");
            pw.println(this.mBackgroundMeteredAllowed);
            pw.println();
        }
        pw.println("Current default network callbacks:");
        pw.increaseIndent();
        for (int i4 = 0; i4 < this.mCurrentDefaultNetworkCallbacks.size(); i4++) {
            this.mCurrentDefaultNetworkCallbacks.valueAt(i4).dumpLocked(pw);
        }
        pw.decreaseIndent();
        pw.println();
        pw.println("UID Pecking Order:");
        pw.increaseIndent();
        for (int i5 = 0; i5 < this.mSortedStats.size(); i5++) {
            pw.print(i5);
            pw.print(": ");
            this.mSortedStats.get(i5).dumpLocked(pw, nowElapsed);
        }
        pw.decreaseIndent();
        pw.println();
        for (int i6 = 0; i6 < this.mTrackedJobs.size(); i6++) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.valueAt(i6);
            for (int j = 0; j < jobs.size(); j++) {
                com.android.server.job.controllers.JobStatus js = jobs.valueAt(j);
                if (predicate.test(js)) {
                    pw.print("#");
                    js.printUniqueId(pw);
                    pw.print(" from ");
                    android.os.UserHandle.formatUid(pw, js.getSourceUid());
                    pw.print(": ");
                    pw.print(js.getJob().getRequiredNetwork());
                    pw.println();
                }
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long token = proto.start(fieldId);
        long mToken = proto.start(1146756268035L);
        for (int i = 0; i < this.mRequestedWhitelistJobs.size(); i++) {
            proto.write(2220498092035L, this.mRequestedWhitelistJobs.keyAt(i));
        }
        for (int i2 = 0; i2 < this.mTrackedJobs.size(); i2++) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.valueAt(i2);
            for (int j = 0; j < jobs.size(); j++) {
                com.android.server.job.controllers.JobStatus js = jobs.valueAt(j);
                if (predicate.test(js)) {
                    long jsToken = proto.start(2246267895810L);
                    js.writeToShortProto(proto, 1146756268033L);
                    proto.write(1120986464258L, js.getSourceUid());
                    proto.end(jsToken);
                }
            }
        }
        proto.end(mToken);
        proto.end(token);
    }
}
