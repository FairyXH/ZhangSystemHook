package com.android.server.stats.pull;

/* JADX INFO: loaded from: classes3.dex */
class AggregatedMobileDataStatsPuller {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "AggregatedMobileDataStatsPuller";
    private static final int UID_STATS_MAX_SIZE = 3000;
    private final android.os.Handler mMobileDataStatsHandler;
    private final android.app.usage.NetworkStatsManager mNetworkStatsManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private android.net.NetworkStats mLastMobileUidStats = new android.net.NetworkStats(0, -1);
    private final com.android.server.selinux.RateLimiter mRateLimiter = new com.android.server.selinux.RateLimiter(java.time.Duration.ofSeconds(1));
    private final java.util.Map<com.android.server.stats.pull.AggregatedMobileDataStatsPuller.UidProcState, com.android.server.stats.pull.AggregatedMobileDataStatsPuller.MobileDataStats> mUidStats = new android.util.ArrayMap();
    private final android.util.SparseIntArray mUidPreviousState = new android.util.SparseIntArray();

    private static class UidProcState {
        private final int mState;
        private final int mUid;

        UidProcState(int uid, int state) {
            this.mUid = uid;
            this.mState = state;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.stats.pull.AggregatedMobileDataStatsPuller.UidProcState)) {
                return false;
            }
            com.android.server.stats.pull.AggregatedMobileDataStatsPuller.UidProcState key = (com.android.server.stats.pull.AggregatedMobileDataStatsPuller.UidProcState) o;
            return this.mUid == key.mUid && this.mState == key.mState;
        }

        public int hashCode() {
            int result = this.mUid;
            return (result * 31) + this.mState;
        }

        public int getUid() {
            return this.mUid;
        }

        public int getState() {
            return this.mState;
        }
    }

    private static class MobileDataStats {
        private long mRxBytes;
        private long mRxPackets;
        private long mTxBytes;
        private long mTxPackets;

        private MobileDataStats() {
            this.mRxPackets = 0L;
            this.mTxPackets = 0L;
            this.mRxBytes = 0L;
            this.mTxBytes = 0L;
        }

        public long getRxPackets() {
            return this.mRxPackets;
        }

        public long getTxPackets() {
            return this.mTxPackets;
        }

        public long getRxBytes() {
            return this.mRxBytes;
        }

        public long getTxBytes() {
            return this.mTxBytes;
        }

        public void addRxPackets(long rxPackets) {
            this.mRxPackets += rxPackets;
        }

        public void addTxPackets(long txPackets) {
            this.mTxPackets += txPackets;
        }

        public void addRxBytes(long rxBytes) {
            this.mRxBytes += rxBytes;
        }

        public void addTxBytes(long txBytes) {
            this.mTxBytes += txBytes;
        }

        public boolean isEmpty() {
            return this.mRxPackets == 0 && this.mTxPackets == 0 && this.mRxBytes == 0 && this.mTxBytes == 0;
        }
    }

    AggregatedMobileDataStatsPuller(android.app.usage.NetworkStatsManager networkStatsManager) {
        this.mNetworkStatsManager = networkStatsManager;
        android.os.HandlerThread mMobileDataStatsHandlerThread = new android.os.HandlerThread("MobileDataStatsHandler");
        mMobileDataStatsHandlerThread.start();
        this.mMobileDataStatsHandler = new android.os.Handler(mMobileDataStatsHandlerThread.getLooper());
        if (this.mNetworkStatsManager != null) {
            this.mMobileDataStatsHandler.post(new java.lang.Runnable() { // from class: com.android.server.stats.pull.AggregatedMobileDataStatsPuller$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateNetworkStats(this.mNetworkStatsManager);
    }

    public void noteUidProcessState(final int uid, final int state, long unusedElapsedRealtime, long unusedUptime) {
        this.mMobileDataStatsHandler.post(new java.lang.Runnable() { // from class: com.android.server.stats.pull.AggregatedMobileDataStatsPuller$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$noteUidProcessState$1(uid, state);
            }
        });
    }

    public int pullDataBytesTransfer(java.util.List<android.util.StatsEvent> data) {
        int iPullDataBytesTransferLocked;
        synchronized (this.mLock) {
            iPullDataBytesTransferLocked = pullDataBytesTransferLocked(data);
        }
        return iPullDataBytesTransferLocked;
    }

    private com.android.server.stats.pull.AggregatedMobileDataStatsPuller.MobileDataStats getUidStatsForPreviousStateLocked(int uid) {
        int previousState = this.mUidPreviousState.get(uid, -1);
        com.android.server.stats.pull.AggregatedMobileDataStatsPuller.UidProcState statsKey = new com.android.server.stats.pull.AggregatedMobileDataStatsPuller.UidProcState(uid, previousState);
        if (this.mUidStats.containsKey(statsKey)) {
            return this.mUidStats.get(statsKey);
        }
        if (this.mUidStats.size() >= 3000) {
            return null;
        }
        com.android.server.stats.pull.AggregatedMobileDataStatsPuller.MobileDataStats stats = new com.android.server.stats.pull.AggregatedMobileDataStatsPuller.MobileDataStats();
        this.mUidStats.put(statsKey, stats);
        return stats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: noteUidProcessStateImpl, reason: merged with bridge method [inline-methods] */
    public void lambda$noteUidProcessState$1(int uid, int state) {
        if (this.mRateLimiter.tryAcquire()) {
            if (this.mNetworkStatsManager != null) {
                updateNetworkStats(this.mNetworkStatsManager);
            } else {
                android.util.Slog.w(TAG, "noteUidProcessStateLocked() can not get mNetworkStatsManager");
            }
        }
        this.mUidPreviousState.put(uid, state);
    }

    private void updateNetworkStats(android.app.usage.NetworkStatsManager networkStatsManager) {
        android.net.NetworkStats latestStats = networkStatsManager.getMobileUidStats();
        if (isEmpty(latestStats)) {
            return;
        }
        android.net.NetworkStats delta = latestStats.subtract(this.mLastMobileUidStats);
        this.mLastMobileUidStats = latestStats;
        if (!isEmpty(delta)) {
            updateNetworkStatsDelta(delta);
        }
    }

    private void updateNetworkStatsDelta(android.net.NetworkStats delta) {
        synchronized (this.mLock) {
            java.util.Iterator it = delta.iterator();
            while (it.hasNext()) {
                android.net.NetworkStats.Entry entry = (android.net.NetworkStats.Entry) it.next();
                if (entry.getRxPackets() != 0 || entry.getTxPackets() != 0) {
                    com.android.server.stats.pull.AggregatedMobileDataStatsPuller.MobileDataStats stats = getUidStatsForPreviousStateLocked(entry.getUid());
                    if (stats != null) {
                        stats.addTxBytes(entry.getTxBytes());
                        stats.addRxBytes(entry.getRxBytes());
                        stats.addTxPackets(entry.getTxPackets());
                        stats.addRxPackets(entry.getRxPackets());
                    }
                }
            }
        }
    }

    private int pullDataBytesTransferLocked(java.util.List<android.util.StatsEvent> pulledData) {
        for (java.util.Map.Entry<com.android.server.stats.pull.AggregatedMobileDataStatsPuller.UidProcState, com.android.server.stats.pull.AggregatedMobileDataStatsPuller.MobileDataStats> uidStats : this.mUidStats.entrySet()) {
            if (!uidStats.getValue().isEmpty()) {
                com.android.server.stats.pull.AggregatedMobileDataStatsPuller.MobileDataStats stats = uidStats.getValue();
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.MOBILE_BYTES_TRANSFER_BY_PROC_STATE, uidStats.getKey().getUid(), android.app.ActivityManager.processStateAmToProto(uidStats.getKey().getState()), stats.getRxBytes(), stats.getRxPackets(), stats.getTxBytes(), stats.getTxPackets()));
            }
        }
        return 0;
    }

    private static boolean isEmpty(android.net.NetworkStats stats) {
        long totalRxPackets = 0;
        long totalTxPackets = 0;
        java.util.Iterator it = stats.iterator();
        while (it.hasNext()) {
            android.net.NetworkStats.Entry entry = (android.net.NetworkStats.Entry) it.next();
            if (entry.getRxPackets() != 0 || entry.getTxPackets() != 0) {
                totalRxPackets = 0 + entry.getRxPackets();
                totalTxPackets = 0 + entry.getTxPackets();
                break;
            }
        }
        long totalPackets = totalRxPackets + totalTxPackets;
        return totalPackets == 0;
    }
}
