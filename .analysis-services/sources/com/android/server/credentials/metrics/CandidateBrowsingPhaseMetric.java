package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class CandidateBrowsingPhaseMetric {
    private int mEntryEnum = com.android.server.credentials.metrics.EntryEnum.UNKNOWN.getMetricCode();
    private int mProviderUid = -1;

    public void setEntryEnum(int entryEnum) {
        this.mEntryEnum = entryEnum;
    }

    public int getEntryEnum() {
        return this.mEntryEnum;
    }

    public void setProviderUid(int providerUid) {
        this.mProviderUid = providerUid;
    }

    public int getProviderUid() {
        return this.mProviderUid;
    }
}
