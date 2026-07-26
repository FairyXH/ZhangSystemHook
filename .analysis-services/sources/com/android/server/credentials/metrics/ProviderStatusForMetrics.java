package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public enum ProviderStatusForMetrics {
    UNKNOWN(0),
    FINAL_FAILURE(4),
    QUERY_FAILURE(3),
    FINAL_SUCCESS(2),
    QUERY_SUCCESS(1);

    private final int mInnerMetricCode;

    ProviderStatusForMetrics(int innerMetricCode) {
        this.mInnerMetricCode = innerMetricCode;
    }

    public int getMetricCode() {
        return this.mInnerMetricCode;
    }
}
