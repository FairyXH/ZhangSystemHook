package com.android.server.ondeviceintelligence;

/* JADX INFO: loaded from: classes2.dex */
public class InferenceInfoStore {
    private static final java.lang.String TAG = "InferenceInfoStore";
    private final java.util.TreeSet<android.app.ondeviceintelligence.InferenceInfo> inferenceInfos = new java.util.TreeSet<>(java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.ondeviceintelligence.InferenceInfoStore$$ExternalSyntheticLambda1
        @Override // java.util.function.ToLongFunction
        public final long applyAsLong(java.lang.Object obj) {
            return ((android.app.ondeviceintelligence.InferenceInfo) obj).getStartTimeMs();
        }
    }));
    private final long maxAgeMs;

    public InferenceInfoStore(long maxAgeMs) {
        this.maxAgeMs = maxAgeMs;
    }

    public java.util.List<android.app.ondeviceintelligence.InferenceInfo> getLatestInferenceInfo(final long startTimeEpochMillis) {
        return this.inferenceInfos.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.ondeviceintelligence.InferenceInfoStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.ondeviceintelligence.InferenceInfoStore.lambda$getLatestInferenceInfo$0(startTimeEpochMillis, (android.app.ondeviceintelligence.InferenceInfo) obj);
            }
        }).toList();
    }

    static /* synthetic */ boolean lambda$getLatestInferenceInfo$0(long startTimeEpochMillis, android.app.ondeviceintelligence.InferenceInfo info) {
        return info.getStartTimeMs() > startTimeEpochMillis;
    }

    public void addInferenceInfoFromBundle(android.os.PersistableBundle pb) {
        if (!pb.containsKey("inference_info")) {
            return;
        }
        try {
            java.lang.String infoBytesBase64String = pb.getString("inference_info");
            if (infoBytesBase64String != null) {
                byte[] infoBytes = android.util.Base64.decode(infoBytesBase64String, 0);
                com.android.server.ondeviceintelligence.nano.InferenceInfo inferenceInfo = com.android.server.ondeviceintelligence.nano.InferenceInfo.parseFrom(infoBytes);
                add(inferenceInfo);
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Unable to parse InferenceInfo from the received bytes.");
        }
    }

    public void addInferenceInfoFromBundle(android.os.Bundle b) {
        if (!b.containsKey("inference_info")) {
            return;
        }
        try {
            byte[] infoBytes = b.getByteArray("inference_info");
            if (infoBytes != null) {
                com.android.server.ondeviceintelligence.nano.InferenceInfo inferenceInfo = com.android.server.ondeviceintelligence.nano.InferenceInfo.parseFrom(infoBytes);
                add(inferenceInfo);
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Unable to parse InferenceInfo from the received bytes.");
        }
    }

    private synchronized void add(com.android.server.ondeviceintelligence.nano.InferenceInfo info) {
        while (!this.inferenceInfos.isEmpty() && java.lang.System.currentTimeMillis() - this.inferenceInfos.first().getStartTimeMs() > this.maxAgeMs) {
            this.inferenceInfos.pollFirst();
        }
        this.inferenceInfos.add(toInferenceInfo(info));
    }

    private static android.app.ondeviceintelligence.InferenceInfo toInferenceInfo(com.android.server.ondeviceintelligence.nano.InferenceInfo info) {
        return new android.app.ondeviceintelligence.InferenceInfo.Builder().setUid(info.uid).setStartTimeMs(info.startTimeMs).setEndTimeMs(info.endTimeMs).setSuspendedTimeMs(info.suspendedTimeMs).build();
    }
}
