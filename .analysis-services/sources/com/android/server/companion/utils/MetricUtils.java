package com.android.server.companion.utils;

/* JADX INFO: loaded from: classes.dex */
public final class MetricUtils {
    private static final java.util.Map<java.lang.String, java.lang.Integer> METRIC_DEVICE_PROFILE;

    static {
        java.util.Map<java.lang.String, java.lang.Integer> map = new android.util.ArrayMap<>();
        map.put(null, 0);
        map.put("android.app.role.COMPANION_DEVICE_WATCH", 1);
        map.put("android.app.role.COMPANION_DEVICE_APP_STREAMING", 2);
        map.put("android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION", 3);
        map.put("android.app.role.COMPANION_DEVICE_COMPUTER", 4);
        map.put("android.app.role.COMPANION_DEVICE_GLASSES", 5);
        map.put("android.app.role.COMPANION_DEVICE_NEARBY_DEVICE_STREAMING", 6);
        METRIC_DEVICE_PROFILE = java.util.Collections.unmodifiableMap(map);
    }

    public static void logCreateAssociation(java.lang.String profile) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CDM_ASSOCIATION_ACTION, 1, METRIC_DEVICE_PROFILE.get(profile).intValue());
    }

    public static void logRemoveAssociation(java.lang.String profile) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CDM_ASSOCIATION_ACTION, 2, METRIC_DEVICE_PROFILE.get(profile).intValue());
    }
}
