package com.android.server.performance;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusPerformanceService extends android.common.IOplusCommonFeature {
    public static final com.android.server.performance.IOplusPerformanceService DEFAULT = new com.android.server.performance.IOplusPerformanceService() { // from class: com.android.server.performance.IOplusPerformanceService.1
    };
    public static final java.lang.String NAME = "IOplusPerformanceService";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusPerformanceService;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void disableSensorScreenShot(android.content.Context context) {
    }
}
