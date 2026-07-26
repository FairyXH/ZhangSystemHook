package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastConstants {
    private static final boolean DEFAULT_CORE_DEFER_UNTIL_ACTIVE = true;
    private static final long DEFAULT_DELAY_CACHED_MILLIS = 120000;
    private static final long DEFAULT_DELAY_FOREGROUND_PROC_MILLIS = -120000;
    private static final long DEFAULT_DELAY_NORMAL_MILLIS = 500;
    private static final long DEFAULT_DELAY_PERSISTENT_PROC_MILLIS = -120000;
    private static final long DEFAULT_DELAY_URGENT_MILLIS = -120000;
    private static final int DEFAULT_EXTRA_RUNNING_URGENT_PROCESS_QUEUES = 1;
    private static final int DEFAULT_MAX_CONSECUTIVE_NORMAL_DISPATCHES = 10;
    private static final int DEFAULT_MAX_CONSECUTIVE_URGENT_DISPATCHES = 3;
    private static final int DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS;
    private static final int DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS;
    private static final int DEFAULT_MAX_FROZEN_OUTGOING_BROADCASTS = 32;
    private static final int DEFAULT_MAX_HISTORY_COMPLETE_SIZE;
    private static final int DEFAULT_MAX_HISTORY_SUMMARY_SIZE;
    private static final int DEFAULT_MAX_PENDING_BROADCASTS;
    private static final int DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS;
    private static final int DEFAULT_MAX_RUNNING_PROCESS_QUEUES;
    private static final long DEFAULT_PENDING_COLD_START_CHECK_INTERVAL_MILLIS = 30000;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_ALL = 1;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_BACKGROUND_RESTRICTED_ONLY = 2;
    static final long DEFER_BOOT_COMPLETED_BROADCAST_CHANGE_ID = 203704822;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_NONE = 0;
    public static final int DEFER_BOOT_COMPLETED_BROADCAST_TARGET_T_ONLY = 4;
    static final java.lang.String KEY_ALLOW_BG_ACTIVITY_START_TIMEOUT = "bcast_allow_bg_activity_start_timeout";
    private static final java.lang.String KEY_CORE_DEFER_UNTIL_ACTIVE = "bcast_core_defer_until_active";
    private static final java.lang.String KEY_CORE_MAX_RUNNING_BLOCKING_BROADCASTS = "bcast_max_core_running_blocking_broadcasts";
    private static final java.lang.String KEY_CORE_MAX_RUNNING_NON_BLOCKING_BROADCASTS = "bcast_max_core_running_non_blocking_broadcasts";
    private static final java.lang.String KEY_DELAY_CACHED_MILLIS = "bcast_delay_cached_millis";
    private static final java.lang.String KEY_DELAY_FOREGROUND_PROC_MILLIS = "bcast_delay_foreground_proc_millis";
    private static final java.lang.String KEY_DELAY_NORMAL_MILLIS = "bcast_delay_normal_millis";
    private static final java.lang.String KEY_DELAY_PERSISTENT_PROC_MILLIS = "bcast_delay_persistent_proc_millis";
    private static final java.lang.String KEY_DELAY_URGENT_MILLIS = "bcast_delay_urgent_millis";
    private static final java.lang.String KEY_EXTRA_RUNNING_URGENT_PROCESS_QUEUES = "bcast_extra_running_urgent_process_queues";
    private static final java.lang.String KEY_MAX_CONSECUTIVE_NORMAL_DISPATCHES = "bcast_max_consecutive_normal_dispatches";
    private static final java.lang.String KEY_MAX_CONSECUTIVE_URGENT_DISPATCHES = "bcast_max_consecutive_urgent_dispatches";
    private static final java.lang.String KEY_MAX_FROZEN_OUTGOING_BROADCASTS = "max_frozen_outgoing_broadcasts";
    private static final java.lang.String KEY_MAX_HISTORY_COMPLETE_SIZE = "bcast_max_history_complete_size";
    private static final java.lang.String KEY_MAX_HISTORY_SUMMARY_SIZE = "bcast_max_history_summary_size";
    private static final java.lang.String KEY_MAX_PENDING_BROADCASTS = "bcast_max_pending_broadcasts";
    private static final java.lang.String KEY_MAX_RUNNING_ACTIVE_BROADCASTS = "bcast_max_running_active_broadcasts";
    private static final java.lang.String KEY_MAX_RUNNING_PROCESS_QUEUES = "bcast_max_running_process_queues";
    private static final java.lang.String KEY_PENDING_COLD_START_CHECK_INTERVAL_MILLIS = "pending_cold_start_check_interval_millis";
    static final java.lang.String KEY_TIMEOUT = "bcast_timeout";
    private static final java.lang.String TAG = "BroadcastConstants";
    private android.content.ContentResolver mResolver;
    private java.lang.String mSettingsKey;
    private com.android.server.am.BroadcastConstants.SettingsObserver mSettingsObserver;
    private static final long DEFAULT_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 10000;
    private static final long DEFAULT_ALLOW_BG_ACTIVITY_START_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 10000;
    public long TIMEOUT = DEFAULT_TIMEOUT;
    public long ALLOW_BG_ACTIVITY_START_TIMEOUT = DEFAULT_ALLOW_BG_ACTIVITY_START_TIMEOUT;
    public int MAX_RUNNING_PROCESS_QUEUES = DEFAULT_MAX_RUNNING_PROCESS_QUEUES;
    public int EXTRA_RUNNING_URGENT_PROCESS_QUEUES = 1;
    public int MAX_CONSECUTIVE_URGENT_DISPATCHES = 3;
    public int MAX_CONSECUTIVE_NORMAL_DISPATCHES = 10;
    public int MAX_RUNNING_ACTIVE_BROADCASTS = DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS;
    public int MAX_CORE_RUNNING_BLOCKING_BROADCASTS = DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS;
    public int MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS = DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS;
    public int MAX_PENDING_BROADCASTS = DEFAULT_MAX_PENDING_BROADCASTS;
    public long DELAY_NORMAL_MILLIS = 500;
    public long DELAY_CACHED_MILLIS = 120000;
    public long DELAY_URGENT_MILLIS = -120000;
    public long DELAY_FOREGROUND_PROC_MILLIS = -120000;
    public long DELAY_PERSISTENT_PROC_MILLIS = -120000;
    public int MAX_HISTORY_COMPLETE_SIZE = DEFAULT_MAX_HISTORY_COMPLETE_SIZE;
    public int MAX_HISTORY_SUMMARY_SIZE = DEFAULT_MAX_HISTORY_SUMMARY_SIZE;
    public boolean CORE_DEFER_UNTIL_ACTIVE = true;
    public long PENDING_COLD_START_CHECK_INTERVAL_MILLIS = 30000;
    public int MAX_FROZEN_OUTGOING_BROADCASTS = 32;
    private final android.util.KeyValueListParser mParser = new android.util.KeyValueListParser(',');

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DeferBootCompletedBroadcastType {
    }

    static {
        DEFAULT_MAX_RUNNING_PROCESS_QUEUES = android.app.ActivityManager.isLowRamDeviceStatic() ? 2 : 4;
        DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS = android.app.ActivityManager.isLowRamDeviceStatic() ? 8 : 16;
        DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS = android.app.ActivityManager.isLowRamDeviceStatic() ? 8 : 16;
        DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS = android.app.ActivityManager.isLowRamDeviceStatic() ? 32 : 64;
        DEFAULT_MAX_PENDING_BROADCASTS = android.app.ActivityManager.isLowRamDeviceStatic() ? 128 : 256;
        DEFAULT_MAX_HISTORY_COMPLETE_SIZE = android.app.ActivityManager.isLowRamDeviceStatic() ? 64 : 256;
        DEFAULT_MAX_HISTORY_SUMMARY_SIZE = android.app.ActivityManager.isLowRamDeviceStatic() ? 256 : 1024;
    }

    class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            com.android.server.am.BroadcastConstants.this.updateSettingsConstants();
        }
    }

    public BroadcastConstants(java.lang.String settingsKey) {
        this.mSettingsKey = settingsKey;
        updateDeviceConfigConstants();
    }

    public void startObserving(android.os.Handler handler, android.content.ContentResolver resolver) {
        this.mResolver = resolver;
        this.mSettingsObserver = new com.android.server.am.BroadcastConstants.SettingsObserver(handler);
        this.mResolver.registerContentObserver(android.provider.Settings.Global.getUriFor(this.mSettingsKey), false, this.mSettingsObserver);
        updateSettingsConstants();
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager_native_boot", new android.os.HandlerExecutor(handler), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.BroadcastConstants$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.updateDeviceConfigConstants(properties);
            }
        });
        updateDeviceConfigConstants();
    }

    public int getMaxRunningQueues() {
        return this.MAX_RUNNING_PROCESS_QUEUES + this.EXTRA_RUNNING_URGENT_PROCESS_QUEUES;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettingsConstants() {
        synchronized (this) {
            try {
                try {
                    this.mParser.setString(android.provider.Settings.Global.getString(this.mResolver, this.mSettingsKey));
                    this.TIMEOUT = this.mParser.getLong(KEY_TIMEOUT, this.TIMEOUT);
                    this.ALLOW_BG_ACTIVITY_START_TIMEOUT = this.mParser.getLong(KEY_ALLOW_BG_ACTIVITY_START_TIMEOUT, this.ALLOW_BG_ACTIVITY_START_TIMEOUT);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(TAG, "Bad broadcast settings in key '" + this.mSettingsKey + "'", e);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    private static java.lang.String propertyFor(java.lang.String key) {
        return "persist.device_config.activity_manager_native_boot." + key;
    }

    private static java.lang.String propertyOverrideFor(java.lang.String key) {
        return "persist.sys.activity_manager_native_boot." + key;
    }

    static boolean getDeviceConfigBoolean(java.lang.String key, boolean def) {
        return android.os.SystemProperties.getBoolean(propertyOverrideFor(key), android.os.SystemProperties.getBoolean(propertyFor(key), def));
    }

    private int getDeviceConfigInt(java.lang.String key, int def) {
        return android.os.SystemProperties.getInt(propertyOverrideFor(key), android.os.SystemProperties.getInt(propertyFor(key), def));
    }

    private long getDeviceConfigLong(java.lang.String key, long def) {
        return android.os.SystemProperties.getLong(propertyOverrideFor(key), android.os.SystemProperties.getLong(propertyFor(key), def));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceConfigConstants(android.provider.DeviceConfig.Properties properties) {
        updateDeviceConfigConstants();
    }

    private void updateDeviceConfigConstants() {
        synchronized (this) {
            this.MAX_RUNNING_PROCESS_QUEUES = getDeviceConfigInt(KEY_MAX_RUNNING_PROCESS_QUEUES, DEFAULT_MAX_RUNNING_PROCESS_QUEUES);
            this.EXTRA_RUNNING_URGENT_PROCESS_QUEUES = getDeviceConfigInt(KEY_EXTRA_RUNNING_URGENT_PROCESS_QUEUES, 1);
            this.MAX_CONSECUTIVE_URGENT_DISPATCHES = getDeviceConfigInt(KEY_MAX_CONSECUTIVE_URGENT_DISPATCHES, 3);
            this.MAX_CONSECUTIVE_NORMAL_DISPATCHES = getDeviceConfigInt(KEY_MAX_CONSECUTIVE_NORMAL_DISPATCHES, 10);
            this.MAX_RUNNING_ACTIVE_BROADCASTS = getDeviceConfigInt(KEY_MAX_RUNNING_ACTIVE_BROADCASTS, DEFAULT_MAX_RUNNING_ACTIVE_BROADCASTS);
            this.MAX_CORE_RUNNING_BLOCKING_BROADCASTS = getDeviceConfigInt(KEY_CORE_MAX_RUNNING_BLOCKING_BROADCASTS, DEFAULT_MAX_CORE_RUNNING_BLOCKING_BROADCASTS);
            this.MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS = getDeviceConfigInt(KEY_CORE_MAX_RUNNING_NON_BLOCKING_BROADCASTS, DEFAULT_MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS);
            this.MAX_PENDING_BROADCASTS = getDeviceConfigInt(KEY_MAX_PENDING_BROADCASTS, DEFAULT_MAX_PENDING_BROADCASTS);
            this.DELAY_NORMAL_MILLIS = getDeviceConfigLong(KEY_DELAY_NORMAL_MILLIS, 500L);
            this.DELAY_CACHED_MILLIS = getDeviceConfigLong(KEY_DELAY_CACHED_MILLIS, 120000L);
            this.DELAY_URGENT_MILLIS = getDeviceConfigLong(KEY_DELAY_URGENT_MILLIS, -120000L);
            this.DELAY_FOREGROUND_PROC_MILLIS = getDeviceConfigLong(KEY_DELAY_FOREGROUND_PROC_MILLIS, -120000L);
            this.DELAY_PERSISTENT_PROC_MILLIS = getDeviceConfigLong(KEY_DELAY_PERSISTENT_PROC_MILLIS, -120000L);
            this.MAX_HISTORY_COMPLETE_SIZE = getDeviceConfigInt(KEY_MAX_HISTORY_COMPLETE_SIZE, DEFAULT_MAX_HISTORY_COMPLETE_SIZE);
            this.MAX_HISTORY_SUMMARY_SIZE = getDeviceConfigInt(KEY_MAX_HISTORY_SUMMARY_SIZE, DEFAULT_MAX_HISTORY_SUMMARY_SIZE);
            this.CORE_DEFER_UNTIL_ACTIVE = getDeviceConfigBoolean(KEY_CORE_DEFER_UNTIL_ACTIVE, true);
            this.PENDING_COLD_START_CHECK_INTERVAL_MILLIS = getDeviceConfigLong(KEY_PENDING_COLD_START_CHECK_INTERVAL_MILLIS, 30000L);
            this.MAX_FROZEN_OUTGOING_BROADCASTS = getDeviceConfigInt(KEY_MAX_FROZEN_OUTGOING_BROADCASTS, 32);
        }
        com.android.server.am.BroadcastRecord.CORE_DEFER_UNTIL_ACTIVE = this.CORE_DEFER_UNTIL_ACTIVE;
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this) {
            pw.print("Broadcast parameters (key=");
            pw.print(this.mSettingsKey);
            pw.print(", observing=");
            pw.print(this.mSettingsObserver != null);
            pw.println("):");
            pw.increaseIndent();
            pw.print(KEY_TIMEOUT, android.util.TimeUtils.formatDuration(this.TIMEOUT)).println();
            pw.print(KEY_ALLOW_BG_ACTIVITY_START_TIMEOUT, android.util.TimeUtils.formatDuration(this.ALLOW_BG_ACTIVITY_START_TIMEOUT)).println();
            pw.decreaseIndent();
            pw.println();
            pw.print("Broadcast parameters (namespace=");
            pw.print("activity_manager_native_boot");
            pw.println("):");
            pw.increaseIndent();
            pw.print(KEY_MAX_RUNNING_PROCESS_QUEUES, java.lang.Integer.valueOf(this.MAX_RUNNING_PROCESS_QUEUES)).println();
            pw.print(KEY_MAX_RUNNING_ACTIVE_BROADCASTS, java.lang.Integer.valueOf(this.MAX_RUNNING_ACTIVE_BROADCASTS)).println();
            pw.print(KEY_CORE_MAX_RUNNING_BLOCKING_BROADCASTS, java.lang.Integer.valueOf(this.MAX_CORE_RUNNING_BLOCKING_BROADCASTS)).println();
            pw.print(KEY_CORE_MAX_RUNNING_NON_BLOCKING_BROADCASTS, java.lang.Integer.valueOf(this.MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS)).println();
            pw.print(KEY_MAX_PENDING_BROADCASTS, java.lang.Integer.valueOf(this.MAX_PENDING_BROADCASTS)).println();
            pw.print(KEY_DELAY_NORMAL_MILLIS, android.util.TimeUtils.formatDuration(this.DELAY_NORMAL_MILLIS)).println();
            pw.print(KEY_DELAY_CACHED_MILLIS, android.util.TimeUtils.formatDuration(this.DELAY_CACHED_MILLIS)).println();
            pw.print(KEY_DELAY_URGENT_MILLIS, android.util.TimeUtils.formatDuration(this.DELAY_URGENT_MILLIS)).println();
            pw.print(KEY_DELAY_FOREGROUND_PROC_MILLIS, android.util.TimeUtils.formatDuration(this.DELAY_FOREGROUND_PROC_MILLIS)).println();
            pw.print(KEY_DELAY_PERSISTENT_PROC_MILLIS, android.util.TimeUtils.formatDuration(this.DELAY_PERSISTENT_PROC_MILLIS)).println();
            pw.print(KEY_MAX_HISTORY_COMPLETE_SIZE, java.lang.Integer.valueOf(this.MAX_HISTORY_COMPLETE_SIZE)).println();
            pw.print(KEY_MAX_HISTORY_SUMMARY_SIZE, java.lang.Integer.valueOf(this.MAX_HISTORY_SUMMARY_SIZE)).println();
            pw.print(KEY_MAX_CONSECUTIVE_URGENT_DISPATCHES, java.lang.Integer.valueOf(this.MAX_CONSECUTIVE_URGENT_DISPATCHES)).println();
            pw.print(KEY_MAX_CONSECUTIVE_NORMAL_DISPATCHES, java.lang.Integer.valueOf(this.MAX_CONSECUTIVE_NORMAL_DISPATCHES)).println();
            pw.print(KEY_CORE_DEFER_UNTIL_ACTIVE, java.lang.Boolean.valueOf(this.CORE_DEFER_UNTIL_ACTIVE)).println();
            pw.print(KEY_PENDING_COLD_START_CHECK_INTERVAL_MILLIS, java.lang.Long.valueOf(this.PENDING_COLD_START_CHECK_INTERVAL_MILLIS)).println();
            pw.print(KEY_MAX_FROZEN_OUTGOING_BROADCASTS, java.lang.Integer.valueOf(this.MAX_FROZEN_OUTGOING_BROADCASTS)).println();
            pw.decreaseIndent();
            pw.println();
        }
    }
}
