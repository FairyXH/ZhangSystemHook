package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class CandidateAggregateMetric {
    private static final java.lang.String TAG = "CandidateTotalMetric";
    private final int mSessionIdProvider;
    private boolean mQueryReturned = false;
    private long mServiceBeganTimeNanoseconds = -1;
    private int mNumProviders = 0;
    private boolean mAuthReturned = false;
    private int mNumAuthEntriesTapped = 0;
    private com.android.server.credentials.metrics.shared.ResponseCollective mAggregateCollectiveQuery = new com.android.server.credentials.metrics.shared.ResponseCollective(java.util.Map.of(), java.util.Map.of());
    private com.android.server.credentials.metrics.shared.ResponseCollective mAggregateCollectiveAuth = new com.android.server.credentials.metrics.shared.ResponseCollective(java.util.Map.of(), java.util.Map.of());
    private long mMinProviderTimestampNanoseconds = -1;
    private long mMaxProviderTimestampNanoseconds = -1;
    private int mTotalQueryFailures = 0;
    private java.util.Map<java.lang.String, java.lang.Integer> mExceptionCountQuery = new java.util.LinkedHashMap();
    private int mTotalAuthFailures = 0;
    private java.util.Map<java.lang.String, java.lang.Integer> mExceptionCountAuth = new java.util.LinkedHashMap();

    public CandidateAggregateMetric(int sessionIdTrackOne) {
        this.mSessionIdProvider = sessionIdTrackOne;
    }

    public int getSessionIdProvider() {
        return this.mSessionIdProvider;
    }

    public void collectAverages(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> providers) {
        collectQueryAggregates(providers);
        collectAuthAggregates(providers);
    }

    private void collectQueryAggregates(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> map) {
        java.util.Collection<com.android.server.credentials.ProviderSession> collection;
        long j;
        this.mNumProviders = map.size();
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        java.util.LinkedHashMap linkedHashMap2 = new java.util.LinkedHashMap();
        java.util.Collection<com.android.server.credentials.ProviderSession> collectionValues = map.values();
        long j2 = Long.MAX_VALUE;
        long jMax = Long.MIN_VALUE;
        java.util.Iterator<com.android.server.credentials.ProviderSession> it = collectionValues.iterator();
        while (it.hasNext()) {
            com.android.server.credentials.metrics.CandidatePhaseMetric candidatePhasePerProviderMetric = it.next().getProviderSessionMetric().getCandidatePhasePerProviderMetric();
            if (candidatePhasePerProviderMetric.getCandidateUid() == -1) {
                this.mNumProviders--;
            } else {
                if (this.mServiceBeganTimeNanoseconds == -1) {
                    this.mServiceBeganTimeNanoseconds = candidatePhasePerProviderMetric.getServiceBeganTimeNanoseconds();
                }
                this.mQueryReturned = this.mQueryReturned || candidatePhasePerProviderMetric.isQueryReturned();
                com.android.server.credentials.metrics.shared.ResponseCollective responseCollective = candidatePhasePerProviderMetric.getResponseCollective();
                com.android.server.credentials.metrics.shared.ResponseCollective.combineTypeCountMaps(linkedHashMap, responseCollective.getResponseCountsMap());
                com.android.server.credentials.metrics.shared.ResponseCollective.combineTypeCountMaps(linkedHashMap2, responseCollective.getEntryCountsMap());
                long jMin = java.lang.Math.min(j2, candidatePhasePerProviderMetric.getStartQueryTimeNanoseconds());
                jMax = java.lang.Math.max(jMax, candidatePhasePerProviderMetric.getQueryFinishTimeNanoseconds());
                this.mTotalQueryFailures += candidatePhasePerProviderMetric.isHasException() ? 1 : 0;
                if (candidatePhasePerProviderMetric.getFrameworkException().isEmpty()) {
                    collection = collectionValues;
                    j = jMin;
                } else {
                    collection = collectionValues;
                    j = jMin;
                    this.mExceptionCountQuery.put(candidatePhasePerProviderMetric.getFrameworkException(), java.lang.Integer.valueOf(this.mExceptionCountQuery.getOrDefault(candidatePhasePerProviderMetric.getFrameworkException(), 0).intValue() + 1));
                }
                collectionValues = collection;
                j2 = j;
            }
        }
        this.mMinProviderTimestampNanoseconds = j2;
        this.mMaxProviderTimestampNanoseconds = jMax;
        this.mAggregateCollectiveQuery = new com.android.server.credentials.metrics.shared.ResponseCollective(linkedHashMap, linkedHashMap2);
    }

    private void collectAuthAggregates(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> map) {
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        java.util.LinkedHashMap linkedHashMap2 = new java.util.LinkedHashMap();
        java.util.Iterator<com.android.server.credentials.ProviderSession> it = map.values().iterator();
        while (it.hasNext()) {
            for (com.android.server.credentials.metrics.BrowsedAuthenticationMetric browsedAuthenticationMetric : it.next().getProviderSessionMetric().getBrowsedAuthenticationMetric()) {
                if (browsedAuthenticationMetric.getProviderUid() != -1) {
                    this.mNumAuthEntriesTapped++;
                    this.mAuthReturned = this.mAuthReturned || browsedAuthenticationMetric.isAuthReturned();
                    com.android.server.credentials.metrics.shared.ResponseCollective authEntryCollective = browsedAuthenticationMetric.getAuthEntryCollective();
                    com.android.server.credentials.metrics.shared.ResponseCollective.combineTypeCountMaps(linkedHashMap, authEntryCollective.getResponseCountsMap());
                    com.android.server.credentials.metrics.shared.ResponseCollective.combineTypeCountMaps(linkedHashMap2, authEntryCollective.getEntryCountsMap());
                    this.mTotalQueryFailures += browsedAuthenticationMetric.isHasException() ? 1 : 0;
                    if (!browsedAuthenticationMetric.getFrameworkException().isEmpty()) {
                        this.mExceptionCountQuery.put(browsedAuthenticationMetric.getFrameworkException(), java.lang.Integer.valueOf(this.mExceptionCountQuery.getOrDefault(browsedAuthenticationMetric.getFrameworkException(), 0).intValue() + 1));
                    }
                }
            }
        }
        this.mAggregateCollectiveAuth = new com.android.server.credentials.metrics.shared.ResponseCollective(linkedHashMap, linkedHashMap2);
    }

    public int getNumProviders() {
        return this.mNumProviders;
    }

    public boolean isQueryReturned() {
        return this.mQueryReturned;
    }

    public int getNumAuthEntriesTapped() {
        return this.mNumAuthEntriesTapped;
    }

    public com.android.server.credentials.metrics.shared.ResponseCollective getAggregateCollectiveQuery() {
        return this.mAggregateCollectiveQuery;
    }

    public com.android.server.credentials.metrics.shared.ResponseCollective getAggregateCollectiveAuth() {
        return this.mAggregateCollectiveAuth;
    }

    public boolean isAuthReturned() {
        return this.mAuthReturned;
    }

    public long getMaxProviderTimestampNanoseconds() {
        return this.mMaxProviderTimestampNanoseconds;
    }

    public long getMinProviderTimestampNanoseconds() {
        return this.mMinProviderTimestampNanoseconds;
    }

    public int getTotalQueryFailures() {
        return this.mTotalQueryFailures;
    }

    public java.lang.String[] getUniqueExceptionStringsQuery() {
        java.lang.String[] result = new java.lang.String[this.mExceptionCountQuery.keySet().size()];
        this.mExceptionCountQuery.keySet().toArray(result);
        return result;
    }

    public int[] getUniqueExceptionCountsQuery() {
        return this.mExceptionCountQuery.values().stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
    }

    public java.lang.String[] getUniqueExceptionStringsAuth() {
        java.lang.String[] result = new java.lang.String[this.mExceptionCountAuth.keySet().size()];
        this.mExceptionCountAuth.keySet().toArray(result);
        return result;
    }

    public int[] getUniqueExceptionCountsAuth() {
        return this.mExceptionCountAuth.values().stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
    }

    public long getServiceBeganTimeNanoseconds() {
        return this.mServiceBeganTimeNanoseconds;
    }

    public int getTotalAuthFailures() {
        return this.mTotalAuthFailures;
    }
}
