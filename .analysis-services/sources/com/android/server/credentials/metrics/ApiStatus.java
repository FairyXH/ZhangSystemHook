package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public enum ApiStatus {
    SUCCESS(1),
    FAILURE(2),
    CLIENT_CANCELED(4),
    USER_CANCELED(3);

    private final int mInnerMetricCode;

    ApiStatus(int innerMetricCode) {
        this.mInnerMetricCode = innerMetricCode;
    }

    public int getMetricCode() {
        return this.mInnerMetricCode;
    }
}
