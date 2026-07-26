package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class DevicePolicyConstants {
    private static final java.lang.String BATTERY_THRESHOLD_CHARGING_KEY = "battery_threshold_charging";
    private static final java.lang.String BATTERY_THRESHOLD_NOT_CHARGING_KEY = "battery_threshold_not_charging";
    private static final java.lang.String DAS_DIED_SERVICE_RECONNECT_BACKOFF_INCREASE_KEY = "das_died_service_reconnect_backoff_increase";
    private static final java.lang.String DAS_DIED_SERVICE_RECONNECT_BACKOFF_SEC_KEY = "das_died_service_reconnect_backoff_sec";
    private static final java.lang.String DAS_DIED_SERVICE_RECONNECT_MAX_BACKOFF_SEC_KEY = "das_died_service_reconnect_max_backoff_sec";
    private static final java.lang.String DAS_DIED_SERVICE_STABLE_CONNECTION_THRESHOLD_SEC_KEY = "das_died_service_stable_connection_threshold_sec";
    private static final java.lang.String TAG = "DevicePolicyManager";
    private static final java.lang.String USE_TEST_ADMIN_AS_SUPERVISION_COMPONENT_KEY = "use_test_admin_as_supervision_component";
    public final int BATTERY_THRESHOLD_CHARGING;
    public final int BATTERY_THRESHOLD_NOT_CHARGING;
    public final double DAS_DIED_SERVICE_RECONNECT_BACKOFF_INCREASE;
    public final long DAS_DIED_SERVICE_RECONNECT_BACKOFF_SEC;
    public final long DAS_DIED_SERVICE_RECONNECT_MAX_BACKOFF_SEC;
    public final long DAS_DIED_SERVICE_STABLE_CONNECTION_THRESHOLD_SEC;
    public final boolean USE_TEST_ADMIN_AS_SUPERVISION_COMPONENT;

    private DevicePolicyConstants(java.lang.String settings) {
        android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
        try {
            parser.setString(settings);
        } catch (java.lang.IllegalArgumentException e) {
            com.android.server.utils.Slogf.e(TAG, "Bad device policy settings: %s", settings);
        }
        long dasDiedServiceReconnectBackoffSec = parser.getLong(DAS_DIED_SERVICE_RECONNECT_BACKOFF_SEC_KEY, java.util.concurrent.TimeUnit.HOURS.toSeconds(1L));
        double dasDiedServiceReconnectBackoffIncrease = parser.getFloat(DAS_DIED_SERVICE_RECONNECT_BACKOFF_INCREASE_KEY, 2.0f);
        long dasDiedServiceReconnectMaxBackoffSec = parser.getLong(DAS_DIED_SERVICE_RECONNECT_MAX_BACKOFF_SEC_KEY, java.util.concurrent.TimeUnit.DAYS.toSeconds(1L));
        long dasDiedServiceStableConnectionThresholdSec = parser.getLong(DAS_DIED_SERVICE_STABLE_CONNECTION_THRESHOLD_SEC_KEY, java.util.concurrent.TimeUnit.MINUTES.toSeconds(2L));
        int batteryThresholdNotCharging = parser.getInt(BATTERY_THRESHOLD_NOT_CHARGING_KEY, 40);
        int batteryThresholdCharging = parser.getInt(BATTERY_THRESHOLD_CHARGING_KEY, 20);
        boolean useTestAdminAsSupervisionComponent = parser.getBoolean(USE_TEST_ADMIN_AS_SUPERVISION_COMPONENT_KEY, false);
        long dasDiedServiceReconnectBackoffSec2 = java.lang.Math.max(5L, dasDiedServiceReconnectBackoffSec);
        double dasDiedServiceReconnectBackoffIncrease2 = java.lang.Math.max(1.0d, dasDiedServiceReconnectBackoffIncrease);
        long dasDiedServiceReconnectMaxBackoffSec2 = java.lang.Math.max(dasDiedServiceReconnectBackoffSec2, dasDiedServiceReconnectMaxBackoffSec);
        this.DAS_DIED_SERVICE_RECONNECT_BACKOFF_SEC = dasDiedServiceReconnectBackoffSec2;
        this.DAS_DIED_SERVICE_RECONNECT_BACKOFF_INCREASE = dasDiedServiceReconnectBackoffIncrease2;
        this.DAS_DIED_SERVICE_RECONNECT_MAX_BACKOFF_SEC = dasDiedServiceReconnectMaxBackoffSec2;
        this.DAS_DIED_SERVICE_STABLE_CONNECTION_THRESHOLD_SEC = dasDiedServiceStableConnectionThresholdSec;
        this.BATTERY_THRESHOLD_NOT_CHARGING = batteryThresholdNotCharging;
        this.BATTERY_THRESHOLD_CHARGING = batteryThresholdCharging;
        this.USE_TEST_ADMIN_AS_SUPERVISION_COMPONENT = useTestAdminAsSupervisionComponent;
    }

    public static com.android.server.devicepolicy.DevicePolicyConstants loadFromString(java.lang.String settings) {
        return new com.android.server.devicepolicy.DevicePolicyConstants(settings);
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        pw.println("Constants:");
        pw.increaseIndent();
        pw.print("DAS_DIED_SERVICE_RECONNECT_BACKOFF_SEC: ");
        pw.println(this.DAS_DIED_SERVICE_RECONNECT_BACKOFF_SEC);
        pw.print("DAS_DIED_SERVICE_RECONNECT_BACKOFF_INCREASE: ");
        pw.println(this.DAS_DIED_SERVICE_RECONNECT_BACKOFF_INCREASE);
        pw.print("DAS_DIED_SERVICE_RECONNECT_MAX_BACKOFF_SEC: ");
        pw.println(this.DAS_DIED_SERVICE_RECONNECT_MAX_BACKOFF_SEC);
        pw.print("DAS_DIED_SERVICE_STABLE_CONNECTION_THRESHOLD_SEC: ");
        pw.println(this.DAS_DIED_SERVICE_STABLE_CONNECTION_THRESHOLD_SEC);
        pw.decreaseIndent();
    }
}
