package com.android.server.credentials.metrics.shared;

/* JADX INFO: loaded from: classes.dex */
public class ResponseCollective {
    private static final java.lang.String TAG = "ResponseCollective";
    private final java.util.Map<com.android.server.credentials.metrics.EntryEnum, java.lang.Integer> mEntryCounts;
    private final java.util.Map<java.lang.String, java.lang.Integer> mResponseCounts;

    public ResponseCollective(java.util.Map<java.lang.String, java.lang.Integer> responseCounts, java.util.Map<com.android.server.credentials.metrics.EntryEnum, java.lang.Integer> entryCounts) {
        this.mResponseCounts = responseCounts == null ? new java.util.LinkedHashMap() : new java.util.LinkedHashMap(responseCounts);
        this.mEntryCounts = entryCounts == null ? new java.util.LinkedHashMap() : new java.util.LinkedHashMap(entryCounts);
    }

    public java.lang.String[] getUniqueResponseStrings() {
        java.lang.String[] result = new java.lang.String[this.mResponseCounts.keySet().size()];
        this.mResponseCounts.keySet().toArray(result);
        return result;
    }

    public java.util.Map<com.android.server.credentials.metrics.EntryEnum, java.lang.Integer> getEntryCountsMap() {
        return java.util.Collections.unmodifiableMap(this.mEntryCounts);
    }

    public java.util.Map<java.lang.String, java.lang.Integer> getResponseCountsMap() {
        return java.util.Collections.unmodifiableMap(this.mResponseCounts);
    }

    public int[] getUniqueResponseCounts() {
        return this.mResponseCounts.values().stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
    }

    public int[] getUniqueEntries() {
        return this.mEntryCounts.keySet().stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.credentials.metrics.shared.ResponseCollective$$ExternalSyntheticLambda0
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((com.android.server.credentials.metrics.EntryEnum) obj).ordinal();
            }
        }).toArray();
    }

    public int[] getUniqueEntryCounts() {
        return this.mEntryCounts.values().stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
    }

    public int getCountForEntry(com.android.server.credentials.metrics.EntryEnum e) {
        return this.mEntryCounts.getOrDefault(e, 0).intValue();
    }

    public int getNumEntriesTotal() {
        return this.mEntryCounts.values().stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).sum();
    }

    public com.android.server.credentials.metrics.shared.ResponseCollective combineCollectives(com.android.server.credentials.metrics.shared.ResponseCollective other) {
        if (this == other) {
            return this;
        }
        java.util.Map<java.lang.String, java.lang.Integer> responseCounts = new java.util.LinkedHashMap<>(other.mResponseCounts);
        for (java.lang.String response : this.mResponseCounts.keySet()) {
            responseCounts.merge(response, this.mResponseCounts.get(response), new java.util.function.BiFunction() { // from class: com.android.server.credentials.metrics.shared.ResponseCollective$$ExternalSyntheticLambda1
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Integer.valueOf(java.lang.Integer.sum(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue()));
                }
            });
        }
        java.util.Map<com.android.server.credentials.metrics.EntryEnum, java.lang.Integer> entryCounts = new java.util.LinkedHashMap<>(other.mEntryCounts);
        for (com.android.server.credentials.metrics.EntryEnum entry : this.mEntryCounts.keySet()) {
            entryCounts.merge(entry, this.mEntryCounts.get(entry), new java.util.function.BiFunction() { // from class: com.android.server.credentials.metrics.shared.ResponseCollective$$ExternalSyntheticLambda1
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Integer.valueOf(java.lang.Integer.sum(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue()));
                }
            });
        }
        return new com.android.server.credentials.metrics.shared.ResponseCollective(responseCounts, entryCounts);
    }

    public static <T> java.util.Map<T, java.lang.Integer> combineTypeCountMaps(java.util.Map<T, java.lang.Integer> first, java.util.Map<T, java.lang.Integer> second) {
        for (T response : second.keySet()) {
            first.put(response, java.lang.Integer.valueOf(first.getOrDefault(response, 0).intValue() + second.get(response).intValue()));
        }
        return first;
    }
}
