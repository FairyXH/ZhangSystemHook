package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public enum ApiName {
    UNKNOWN(0),
    GET_CREDENTIAL(1),
    GET_CREDENTIAL_VIA_REGISTRY(9),
    CREATE_CREDENTIAL(2),
    CLEAR_CREDENTIAL(3),
    IS_ENABLED_CREDENTIAL_PROVIDER_SERVICE(4),
    SET_ENABLED_PROVIDERS(5),
    GET_CREDENTIAL_PROVIDER_SERVICES(6),
    REGISTER_CREDENTIAL_DESCRIPTION(7),
    UNREGISTER_CREDENTIAL_DESCRIPTION(8);

    private static final java.lang.String TAG = "ApiName";
    private final int mInnerMetricCode;
    private static final java.util.Map<java.lang.String, java.lang.Integer> sRequestInfoToMetric = java.util.Map.ofEntries(new java.util.AbstractMap.SimpleEntry("android.credentials.selection.TYPE_CREATE", java.lang.Integer.valueOf(CREATE_CREDENTIAL.mInnerMetricCode)), new java.util.AbstractMap.SimpleEntry("android.credentials.selection.TYPE_GET", java.lang.Integer.valueOf(GET_CREDENTIAL.mInnerMetricCode)), new java.util.AbstractMap.SimpleEntry("android.credentials.selection.TYPE_GET_VIA_REGISTRY", java.lang.Integer.valueOf(GET_CREDENTIAL_VIA_REGISTRY.mInnerMetricCode)), new java.util.AbstractMap.SimpleEntry("android.credentials.selection.TYPE_UNDEFINED", java.lang.Integer.valueOf(CLEAR_CREDENTIAL.mInnerMetricCode)));

    ApiName(int innerMetricCode) {
        this.mInnerMetricCode = innerMetricCode;
    }

    public int getMetricCode() {
        return this.mInnerMetricCode;
    }

    public static int getMetricCodeFromRequestInfo(java.lang.String stringKey) {
        if (!sRequestInfoToMetric.containsKey(stringKey)) {
            android.util.Slog.i(TAG, "Attempted to use an unsupported string key request info");
            return UNKNOWN.mInnerMetricCode;
        }
        return sRequestInfoToMetric.get(stringKey).intValue();
    }
}
