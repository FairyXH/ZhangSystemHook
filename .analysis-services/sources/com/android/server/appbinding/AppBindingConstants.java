package com.android.server.appbinding;

/* JADX INFO: loaded from: classes.dex */
public class AppBindingConstants {
    private static final java.lang.String SERVICE_RECONNECT_BACKOFF_INCREASE_KEY = "service_reconnect_backoff_increase";
    private static final java.lang.String SERVICE_RECONNECT_BACKOFF_SEC_KEY = "service_reconnect_backoff_sec";
    private static final java.lang.String SERVICE_RECONNECT_MAX_BACKOFF_SEC_KEY = "service_reconnect_max_backoff_sec";
    private static final java.lang.String SERVICE_STABLE_CONNECTION_THRESHOLD_SEC_KEY = "service_stable_connection_threshold_sec";
    private static final java.lang.String SMS_APP_BIND_FLAGS_KEY = "sms_app_bind_flags";
    private static final java.lang.String SMS_SERVICE_ENABLED_KEY = "sms_service_enabled";
    private static final java.lang.String TAG = "AppBindingService";
    public final double SERVICE_RECONNECT_BACKOFF_INCREASE;
    public final long SERVICE_RECONNECT_BACKOFF_SEC;
    public final long SERVICE_RECONNECT_MAX_BACKOFF_SEC;
    public final long SERVICE_STABLE_CONNECTION_THRESHOLD_SEC;
    public final int SMS_APP_BIND_FLAGS;
    public final boolean SMS_SERVICE_ENABLED;
    public final java.lang.String sourceSettings;

    private AppBindingConstants(java.lang.String settings) {
        this.sourceSettings = settings;
        android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
        try {
            parser.setString(settings);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e("AppBindingService", "Bad setting: " + settings);
        }
        long serviceReconnectBackoffSec = parser.getLong(SERVICE_RECONNECT_BACKOFF_SEC_KEY, 10L);
        double serviceReconnectBackoffIncrease = parser.getFloat(SERVICE_RECONNECT_BACKOFF_INCREASE_KEY, 2.0f);
        long serviceReconnectMaxBackoffSec = parser.getLong(SERVICE_RECONNECT_MAX_BACKOFF_SEC_KEY, java.util.concurrent.TimeUnit.HOURS.toSeconds(1L));
        boolean smsServiceEnabled = parser.getBoolean(SMS_SERVICE_ENABLED_KEY, true);
        int smsAppBindFlags = parser.getInt(SMS_APP_BIND_FLAGS_KEY, 1140850688);
        long serviceStableConnectionThresholdSec = parser.getLong(SERVICE_STABLE_CONNECTION_THRESHOLD_SEC_KEY, java.util.concurrent.TimeUnit.MINUTES.toSeconds(2L));
        long serviceReconnectBackoffSec2 = java.lang.Math.max(5L, serviceReconnectBackoffSec);
        double serviceReconnectBackoffIncrease2 = java.lang.Math.max(1.0d, serviceReconnectBackoffIncrease);
        long serviceReconnectMaxBackoffSec2 = java.lang.Math.max(serviceReconnectBackoffSec2, serviceReconnectMaxBackoffSec);
        this.SERVICE_RECONNECT_BACKOFF_SEC = serviceReconnectBackoffSec2;
        this.SERVICE_RECONNECT_BACKOFF_INCREASE = serviceReconnectBackoffIncrease2;
        this.SERVICE_RECONNECT_MAX_BACKOFF_SEC = serviceReconnectMaxBackoffSec2;
        this.SERVICE_STABLE_CONNECTION_THRESHOLD_SEC = serviceStableConnectionThresholdSec;
        this.SMS_SERVICE_ENABLED = smsServiceEnabled;
        this.SMS_APP_BIND_FLAGS = smsAppBindFlags;
    }

    public static com.android.server.appbinding.AppBindingConstants initializeFromString(java.lang.String settings) {
        return new com.android.server.appbinding.AppBindingConstants(settings);
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("Constants: ");
        pw.println(this.sourceSettings);
        pw.print(prefix);
        pw.print("  SERVICE_RECONNECT_BACKOFF_SEC: ");
        pw.println(this.SERVICE_RECONNECT_BACKOFF_SEC);
        pw.print(prefix);
        pw.print("  SERVICE_RECONNECT_BACKOFF_INCREASE: ");
        pw.println(this.SERVICE_RECONNECT_BACKOFF_INCREASE);
        pw.print(prefix);
        pw.print("  SERVICE_RECONNECT_MAX_BACKOFF_SEC: ");
        pw.println(this.SERVICE_RECONNECT_MAX_BACKOFF_SEC);
        pw.print(prefix);
        pw.print("  SERVICE_STABLE_CONNECTION_THRESHOLD_SEC: ");
        pw.println(this.SERVICE_STABLE_CONNECTION_THRESHOLD_SEC);
        pw.print(prefix);
        pw.print("  SMS_SERVICE_ENABLED: ");
        pw.println(this.SMS_SERVICE_ENABLED);
        pw.print(prefix);
        pw.print("  SMS_APP_BIND_FLAGS: 0x");
        pw.println(java.lang.Integer.toHexString(this.SMS_APP_BIND_FLAGS));
    }
}
