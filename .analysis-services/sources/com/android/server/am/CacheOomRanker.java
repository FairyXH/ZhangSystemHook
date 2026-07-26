package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class CacheOomRanker {
    static final float DEFAULT_OOM_RE_RANKING_LRU_WEIGHT = 0.35f;
    static final int DEFAULT_OOM_RE_RANKING_NUMBER_TO_RE_RANK = 8;
    static final float DEFAULT_OOM_RE_RANKING_RSS_WEIGHT = 0.15f;
    static final float DEFAULT_OOM_RE_RANKING_USES_WEIGHT = 0.5f;
    static final int DEFAULT_PRESERVE_TOP_N_APPS = 3;
    static final long DEFAULT_RSS_UPDATE_RATE_MS = 10000;
    static final boolean DEFAULT_USE_FREQUENT_RSS = true;
    private static final boolean DEFAULT_USE_OOM_RE_RANKING = false;
    static final java.lang.String KEY_OOM_RE_RANKING_LRU_WEIGHT = "oom_re_ranking_lru_weight";
    static final java.lang.String KEY_OOM_RE_RANKING_NUMBER_TO_RE_RANK = "oom_re_ranking_number_to_re_rank";
    static final java.lang.String KEY_OOM_RE_RANKING_PRESERVE_TOP_N_APPS = "oom_re_ranking_preserve_top_n_apps";
    static final java.lang.String KEY_OOM_RE_RANKING_RSS_UPDATE_RATE_MS = "oom_re_ranking_rss_update_rate_ms";
    static final java.lang.String KEY_OOM_RE_RANKING_RSS_WEIGHT = "oom_re_ranking_rss_weight";
    static final java.lang.String KEY_OOM_RE_RANKING_USES_WEIGHT = "oom_re_ranking_uses_weight";
    static final java.lang.String KEY_OOM_RE_RANKING_USE_FREQUENT_RSS = "oom_re_ranking_rss_use_frequent_rss";
    static final java.lang.String KEY_USE_OOM_RE_RANKING = "use_oom_re_ranking";
    private int[] mLruPositions;
    float mLruWeight;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnFlagsChangedListener;
    private final java.lang.Object mPhenotypeFlagLock;
    int mPreserveTopNApps;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final com.android.server.am.CacheOomRanker.ProcessDependencies mProcessDependencies;
    private final java.lang.Object mProfilerLock;
    long mRssUpdateRateMs;
    float mRssWeight;
    private com.android.server.am.CacheOomRanker.RankedProcessRecord[] mScoredProcessRecords;
    private final com.android.server.am.ActivityManagerService mService;
    boolean mUseFrequentRss;
    private boolean mUseOomReRanking;
    float mUsesWeight;
    private static final java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> SCORED_PROCESS_RECORD_COMPARATOR = new com.android.server.am.CacheOomRanker.ScoreComparator();
    private static final java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> CACHE_USE_COMPARATOR = new com.android.server.am.CacheOomRanker.CacheUseComparator();
    private static final java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> RSS_COMPARATOR = new com.android.server.am.CacheOomRanker.RssComparator();
    private static final java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> LAST_RSS_COMPARATOR = new com.android.server.am.CacheOomRanker.LastRssComparator();
    private static final java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> LAST_ACTIVITY_TIME_COMPARATOR = new com.android.server.am.CacheOomRanker.LastActivityTimeComparator();

    interface ProcessDependencies {
        long[] getRss(int i);
    }

    CacheOomRanker(com.android.server.am.ActivityManagerService service) {
        this(service, new com.android.server.am.CacheOomRanker.ProcessDependenciesImpl());
    }

    CacheOomRanker(com.android.server.am.ActivityManagerService service, com.android.server.am.CacheOomRanker.ProcessDependencies processDependencies) {
        this.mPhenotypeFlagLock = new java.lang.Object();
        this.mUseOomReRanking = false;
        this.mPreserveTopNApps = 3;
        this.mUseFrequentRss = true;
        this.mRssUpdateRateMs = 10000L;
        this.mLruWeight = DEFAULT_OOM_RE_RANKING_LRU_WEIGHT;
        this.mUsesWeight = 0.5f;
        this.mRssWeight = DEFAULT_OOM_RE_RANKING_RSS_WEIGHT;
        this.mOnFlagsChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.CacheOomRanker.1
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                synchronized (com.android.server.am.CacheOomRanker.this.mPhenotypeFlagLock) {
                    for (java.lang.String name : properties.getKeyset()) {
                        if (com.android.server.am.CacheOomRanker.KEY_USE_OOM_RE_RANKING.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updateUseOomReranking();
                        } else if (com.android.server.am.CacheOomRanker.KEY_OOM_RE_RANKING_NUMBER_TO_RE_RANK.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updateNumberToReRank();
                        } else if (com.android.server.am.CacheOomRanker.KEY_OOM_RE_RANKING_PRESERVE_TOP_N_APPS.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updatePreserveTopNApps();
                        } else if (com.android.server.am.CacheOomRanker.KEY_OOM_RE_RANKING_USE_FREQUENT_RSS.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updateUseFrequentRss();
                        } else if (com.android.server.am.CacheOomRanker.KEY_OOM_RE_RANKING_RSS_UPDATE_RATE_MS.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updateRssUpdateRateMs();
                        } else if (com.android.server.am.CacheOomRanker.KEY_OOM_RE_RANKING_LRU_WEIGHT.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updateLruWeight();
                        } else if (com.android.server.am.CacheOomRanker.KEY_OOM_RE_RANKING_USES_WEIGHT.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updateUsesWeight();
                        } else if (com.android.server.am.CacheOomRanker.KEY_OOM_RE_RANKING_RSS_WEIGHT.equals(name)) {
                            com.android.server.am.CacheOomRanker.this.updateRssWeight();
                        }
                    }
                }
            }
        };
        this.mService = service;
        this.mProcLock = service.mProcLock;
        this.mProfilerLock = service.mAppProfiler.mProfilerLock;
        this.mProcessDependencies = processDependencies;
    }

    public void init(java.util.concurrent.Executor executor) {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager", executor, this.mOnFlagsChangedListener);
        synchronized (this.mPhenotypeFlagLock) {
            updateUseOomReranking();
            updateNumberToReRank();
            updateLruWeight();
            updateUsesWeight();
            updateRssWeight();
        }
    }

    public boolean useOomReranking() {
        boolean z;
        synchronized (this.mPhenotypeFlagLock) {
            z = this.mUseOomReRanking;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseOomReranking() {
        this.mUseOomReRanking = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_USE_OOM_RE_RANKING, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNumberToReRank() {
        int previousNumberToReRank = getNumberToReRank();
        int numberToReRank = android.provider.DeviceConfig.getInt("activity_manager", KEY_OOM_RE_RANKING_NUMBER_TO_RE_RANK, 8);
        if (previousNumberToReRank != numberToReRank) {
            this.mScoredProcessRecords = new com.android.server.am.CacheOomRanker.RankedProcessRecord[numberToReRank];
            for (int i = 0; i < this.mScoredProcessRecords.length; i++) {
                this.mScoredProcessRecords[i] = new com.android.server.am.CacheOomRanker.RankedProcessRecord();
            }
            this.mLruPositions = new int[numberToReRank];
        }
    }

    int getNumberToReRank() {
        if (this.mScoredProcessRecords == null) {
            return 0;
        }
        return this.mScoredProcessRecords.length;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePreserveTopNApps() {
        int preserveTopNApps = android.provider.DeviceConfig.getInt("activity_manager", KEY_OOM_RE_RANKING_PRESERVE_TOP_N_APPS, 3);
        if (preserveTopNApps < 0) {
            android.util.Slog.w("OomAdjuster", "Found negative value for preserveTopNApps, setting to default: " + preserveTopNApps);
            preserveTopNApps = 3;
        }
        this.mPreserveTopNApps = preserveTopNApps;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRssUpdateRateMs() {
        this.mRssUpdateRateMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_OOM_RE_RANKING_RSS_UPDATE_RATE_MS, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseFrequentRss() {
        this.mUseFrequentRss = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_OOM_RE_RANKING_USE_FREQUENT_RSS, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLruWeight() {
        this.mLruWeight = android.provider.DeviceConfig.getFloat("activity_manager", KEY_OOM_RE_RANKING_LRU_WEIGHT, DEFAULT_OOM_RE_RANKING_LRU_WEIGHT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUsesWeight() {
        this.mUsesWeight = android.provider.DeviceConfig.getFloat("activity_manager", KEY_OOM_RE_RANKING_USES_WEIGHT, 0.5f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRssWeight() {
        this.mRssWeight = android.provider.DeviceConfig.getFloat("activity_manager", KEY_OOM_RE_RANKING_RSS_WEIGHT, DEFAULT_OOM_RE_RANKING_RSS_WEIGHT);
    }

    void reRankLruCachedAppsLSP(java.util.ArrayList<com.android.server.am.ProcessRecord> lruList, int lruProcessServiceStart) {
        float lruWeight;
        float usesWeight;
        float rssWeight;
        int preserveTopNApps;
        boolean useFrequentRss;
        long rssUpdateRateMs;
        int[] lruPositions;
        com.android.server.am.CacheOomRanker.RankedProcessRecord[] scoredProcessRecords;
        int i;
        java.util.ArrayList<com.android.server.am.ProcessRecord> arrayList;
        long rssUpdateRateMs2;
        long nowMs;
        synchronized (this.mPhenotypeFlagLock) {
            try {
                lruWeight = this.mLruWeight;
                usesWeight = this.mUsesWeight;
                rssWeight = this.mRssWeight;
                preserveTopNApps = this.mPreserveTopNApps;
                useFrequentRss = this.mUseFrequentRss;
                rssUpdateRateMs = this.mRssUpdateRateMs;
                lruPositions = this.mLruPositions;
                scoredProcessRecords = this.mScoredProcessRecords;
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        if (lruPositions != null && scoredProcessRecords != null) {
            int numProcessesEvaluated = 0;
            int numProcessesReRanked = 0;
            while (numProcessesEvaluated < lruProcessServiceStart && numProcessesReRanked < scoredProcessRecords.length) {
                com.android.server.am.ProcessRecord process = lruList.get(numProcessesEvaluated);
                if (appCanBeReRanked(process)) {
                    scoredProcessRecords[numProcessesReRanked].proc = process;
                    scoredProcessRecords[numProcessesReRanked].score = 0.0f;
                    lruPositions[numProcessesReRanked] = numProcessesEvaluated;
                    numProcessesReRanked++;
                }
                numProcessesEvaluated++;
            }
            int numProcessesNotReRanked = 0;
            int numProcessesEvaluated2 = numProcessesEvaluated;
            while (numProcessesEvaluated2 < lruProcessServiceStart && numProcessesNotReRanked < preserveTopNApps) {
                if (appCanBeReRanked(lruList.get(numProcessesEvaluated2))) {
                    numProcessesNotReRanked++;
                }
                numProcessesEvaluated2++;
            }
            if (numProcessesNotReRanked < preserveTopNApps && (numProcessesReRanked = numProcessesReRanked - (preserveTopNApps - numProcessesNotReRanked)) < 0) {
                numProcessesReRanked = 0;
            }
            if (useFrequentRss) {
                long nowMs2 = android.os.SystemClock.elapsedRealtime();
                int i2 = 0;
                while (i2 < numProcessesReRanked) {
                    int preserveTopNApps2 = preserveTopNApps;
                    com.android.server.am.CacheOomRanker.RankedProcessRecord scoredProcessRecord = scoredProcessRecords[i2];
                    int numProcessesEvaluated3 = numProcessesEvaluated2;
                    long sinceUpdateMs = nowMs2 - scoredProcessRecord.proc.mState.getCacheOomRankerRssTimeMs();
                    if (scoredProcessRecord.proc.mState.getCacheOomRankerRss() != 0 && sinceUpdateMs < rssUpdateRateMs) {
                        nowMs = nowMs2;
                        rssUpdateRateMs2 = rssUpdateRateMs;
                    } else {
                        rssUpdateRateMs2 = rssUpdateRateMs;
                        long rssUpdateRateMs3 = nowMs2;
                        long[] rss = this.mProcessDependencies.getRss(scoredProcessRecord.proc.getPid());
                        if (rss != null && rss.length != 0) {
                            scoredProcessRecord.proc.mState.setCacheOomRankerRss(rss[0], rssUpdateRateMs3);
                            com.android.server.am.ProcessProfileRecord processProfileRecord = scoredProcessRecord.proc.mProfile;
                            nowMs = rssUpdateRateMs3;
                            long nowMs3 = rss[0];
                            processProfileRecord.setLastRss(nowMs3);
                        }
                        android.util.Slog.e("OomAdjuster", "Process.getRss returned bad value, not re-ranking: " + java.util.Arrays.toString(rss));
                        return;
                    }
                    i2++;
                    preserveTopNApps = preserveTopNApps2;
                    numProcessesEvaluated2 = numProcessesEvaluated3;
                    rssUpdateRateMs = rssUpdateRateMs2;
                    nowMs2 = nowMs;
                }
            }
            if (lruWeight > 0.0f) {
                java.util.Arrays.sort(scoredProcessRecords, 0, numProcessesReRanked, LAST_ACTIVITY_TIME_COMPARATOR);
                addToScore(scoredProcessRecords, lruWeight);
            }
            if (rssWeight > 0.0f) {
                if (useFrequentRss) {
                    java.util.Arrays.sort(scoredProcessRecords, 0, numProcessesReRanked, RSS_COMPARATOR);
                } else {
                    synchronized (this.mService.mAppProfiler.mProfilerLock) {
                        java.util.Arrays.sort(scoredProcessRecords, 0, numProcessesReRanked, LAST_RSS_COMPARATOR);
                    }
                }
                addToScore(scoredProcessRecords, rssWeight);
            }
            if (usesWeight <= 0.0f) {
                i = 0;
            } else {
                i = 0;
                java.util.Arrays.sort(scoredProcessRecords, 0, numProcessesReRanked, CACHE_USE_COMPARATOR);
                addToScore(scoredProcessRecords, usesWeight);
            }
            java.util.Arrays.sort(scoredProcessRecords, i, numProcessesReRanked, SCORED_PROCESS_RECORD_COMPARATOR);
            if (!com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                arrayList = lruList;
            } else {
                boolean printedHeader = false;
                for (int i3 = 0; i3 < numProcessesReRanked; i3++) {
                    if (scoredProcessRecords[i3].proc.getPid() != lruList.get(lruPositions[i3]).getPid()) {
                        if (!printedHeader) {
                            android.util.Slog.i("OomAdjuster", "reRankLruCachedApps");
                            printedHeader = true;
                        }
                        android.util.Slog.i("OomAdjuster", "  newPos=" + lruPositions[i3] + " " + scoredProcessRecords[i3].proc);
                    }
                }
                arrayList = lruList;
            }
            for (int i4 = 0; i4 < numProcessesReRanked; i4++) {
                arrayList.set(lruPositions[i4], scoredProcessRecords[i4].proc);
                scoredProcessRecords[i4].proc = null;
            }
        }
    }

    private static boolean appCanBeReRanked(com.android.server.am.ProcessRecord process) {
        return (process.isKilledByAm() || process.getThread() == null || process.mState.getCurAdj() < 1001) ? false : true;
    }

    private static void addToScore(com.android.server.am.CacheOomRanker.RankedProcessRecord[] scores, float weight) {
        for (int i = 1; i < scores.length; i++) {
            scores[i].score += i * weight;
        }
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("CacheOomRanker settings");
        synchronized (this.mPhenotypeFlagLock) {
            pw.println("  use_oom_re_ranking=" + this.mUseOomReRanking);
            pw.println("  oom_re_ranking_number_to_re_rank=" + getNumberToReRank());
            pw.println("  oom_re_ranking_lru_weight=" + this.mLruWeight);
            pw.println("  oom_re_ranking_uses_weight=" + this.mUsesWeight);
            pw.println("  oom_re_ranking_rss_weight=" + this.mRssWeight);
        }
    }

    private static class ScoreComparator implements java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> {
        private ScoreComparator() {
        }

        @Override // java.util.Comparator
        public int compare(com.android.server.am.CacheOomRanker.RankedProcessRecord o1, com.android.server.am.CacheOomRanker.RankedProcessRecord o2) {
            return java.lang.Float.compare(o1.score, o2.score);
        }
    }

    private static class LastActivityTimeComparator implements java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> {
        private LastActivityTimeComparator() {
        }

        @Override // java.util.Comparator
        public int compare(com.android.server.am.CacheOomRanker.RankedProcessRecord o1, com.android.server.am.CacheOomRanker.RankedProcessRecord o2) {
            return java.lang.Long.compare(o1.proc.getLastActivityTime(), o2.proc.getLastActivityTime());
        }
    }

    private static class CacheUseComparator implements java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> {
        private CacheUseComparator() {
        }

        @Override // java.util.Comparator
        public int compare(com.android.server.am.CacheOomRanker.RankedProcessRecord o1, com.android.server.am.CacheOomRanker.RankedProcessRecord o2) {
            return java.lang.Long.compare(o1.proc.mState.getCacheOomRankerUseCount(), o2.proc.mState.getCacheOomRankerUseCount());
        }
    }

    private static class RssComparator implements java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> {
        private RssComparator() {
        }

        @Override // java.util.Comparator
        public int compare(com.android.server.am.CacheOomRanker.RankedProcessRecord o1, com.android.server.am.CacheOomRanker.RankedProcessRecord o2) {
            return java.lang.Long.compare(o2.proc.mState.getCacheOomRankerRss(), o1.proc.mState.getCacheOomRankerRss());
        }
    }

    private static class LastRssComparator implements java.util.Comparator<com.android.server.am.CacheOomRanker.RankedProcessRecord> {
        private LastRssComparator() {
        }

        @Override // java.util.Comparator
        public int compare(com.android.server.am.CacheOomRanker.RankedProcessRecord o1, com.android.server.am.CacheOomRanker.RankedProcessRecord o2) {
            return java.lang.Long.compare(o2.proc.mProfile.getLastRss(), o1.proc.mProfile.getLastRss());
        }
    }

    private static class RankedProcessRecord {
        public com.android.server.am.ProcessRecord proc;
        public float score;

        private RankedProcessRecord() {
        }
    }

    private static class ProcessDependenciesImpl implements com.android.server.am.CacheOomRanker.ProcessDependencies {
        private ProcessDependenciesImpl() {
        }

        @Override // com.android.server.am.CacheOomRanker.ProcessDependencies
        public long[] getRss(int pid) {
            return android.os.Process.getRss(pid);
        }
    }
}
