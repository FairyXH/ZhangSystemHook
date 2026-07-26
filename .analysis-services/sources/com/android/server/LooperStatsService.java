package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class LooperStatsService extends android.os.Binder {
    private static final java.lang.String DEBUG_SYS_LOOPER_STATS_ENABLED = "debug.sys.looper_stats_enabled";
    private static final boolean DEFAULT_ENABLED = true;
    private static final int DEFAULT_ENTRIES_SIZE_CAP = 1500;
    private static final int DEFAULT_SAMPLING_INTERVAL = 1000;
    private static final boolean DEFAULT_TRACK_SCREEN_INTERACTIVE = false;
    private static final java.lang.String LOOPER_STATS_SERVICE_NAME = "looper_stats";
    private static final java.lang.String SETTINGS_ENABLED_KEY = "enabled";
    private static final java.lang.String SETTINGS_IGNORE_BATTERY_STATUS_KEY = "ignore_battery_status";
    private static final java.lang.String SETTINGS_SAMPLING_INTERVAL_KEY = "sampling_interval";
    private static final java.lang.String SETTINGS_TRACK_SCREEN_INTERACTIVE_KEY = "track_screen_state";
    private static final java.lang.String TAG = "LooperStatsService";
    private final android.content.Context mContext;
    private boolean mEnabled;
    private boolean mIgnoreBatteryStatus;
    private final com.android.internal.os.LooperStats mStats;
    private boolean mTrackScreenInteractive;

    private LooperStatsService(android.content.Context context, com.android.internal.os.LooperStats stats) {
        this.mEnabled = false;
        this.mTrackScreenInteractive = false;
        this.mIgnoreBatteryStatus = false;
        this.mContext = context;
        this.mStats = stats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFromSettings() {
        android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
        try {
            parser.setString(android.provider.Settings.Global.getString(this.mContext.getContentResolver(), LOOPER_STATS_SERVICE_NAME));
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Bad looper_stats settings", e);
        }
        setSamplingInterval(parser.getInt(SETTINGS_SAMPLING_INTERVAL_KEY, 1000));
        setTrackScreenInteractive(parser.getBoolean(SETTINGS_TRACK_SCREEN_INTERACTIVE_KEY, false));
        setIgnoreBatteryStatus(parser.getBoolean(SETTINGS_IGNORE_BATTERY_STATUS_KEY, false));
        setEnabled(android.os.SystemProperties.getBoolean(DEBUG_SYS_LOOPER_STATS_ENABLED, parser.getBoolean("enabled", true)));
    }

    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.LooperStatsService.LooperShellCommand().exec(this, in, out, err, args, callback, resultReceiver);
    }

    @Override // android.os.Binder
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            com.android.internal.os.AppIdToPackageMap packageMap = com.android.internal.os.AppIdToPackageMap.getSnapshot();
            pw.print("Start time: ");
            pw.println(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", this.mStats.getStartTimeMillis()));
            pw.print("On battery time (ms): ");
            pw.println(this.mStats.getBatteryTimeMillis());
            java.util.List<com.android.internal.os.LooperStats.ExportedEntry> entries = this.mStats.getEntries();
            entries.sort(java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Integer.valueOf(((com.android.internal.os.LooperStats.ExportedEntry) obj).workSourceUid);
                }
            }).thenComparing(new java.util.function.Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((com.android.internal.os.LooperStats.ExportedEntry) obj).threadName;
                }
            }).thenComparing(new java.util.function.Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((com.android.internal.os.LooperStats.ExportedEntry) obj).handlerClassName;
                }
            }).thenComparing(new java.util.function.Function() { // from class: com.android.server.LooperStatsService$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((com.android.internal.os.LooperStats.ExportedEntry) obj).messageName;
                }
            }));
            java.lang.String header = java.lang.String.join(",", java.util.Arrays.asList("work_source_uid", "thread_name", "handler_class", "message_name", "is_interactive", "message_count", "recorded_message_count", "total_latency_micros", "max_latency_micros", "total_cpu_micros", "max_cpu_micros", "recorded_delay_message_count", "total_delay_millis", "max_delay_millis", "exception_count"));
            pw.println(header);
            for (com.android.internal.os.LooperStats.ExportedEntry entry : entries) {
                if (!entry.messageName.startsWith("__DEBUG_")) {
                    pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", packageMap.mapUid(entry.workSourceUid), entry.threadName, entry.handlerClassName, entry.messageName, java.lang.Boolean.valueOf(entry.isInteractive), java.lang.Long.valueOf(entry.messageCount), java.lang.Long.valueOf(entry.recordedMessageCount), java.lang.Long.valueOf(entry.totalLatencyMicros), java.lang.Long.valueOf(entry.maxLatencyMicros), java.lang.Long.valueOf(entry.cpuUsageMicros), java.lang.Long.valueOf(entry.maxCpuUsageMicros), java.lang.Long.valueOf(entry.recordedDelayMessageCount), java.lang.Long.valueOf(entry.delayMillis), java.lang.Long.valueOf(entry.maxDelayMillis), java.lang.Long.valueOf(entry.exceptionCount));
                    packageMap = packageMap;
                    entries = entries;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnabled(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            this.mStats.reset();
            this.mStats.setAddDebugEntries(enabled);
            android.os.Looper.setObserver(enabled ? this.mStats : null);
        }
    }

    private void setTrackScreenInteractive(boolean enabled) {
        if (this.mTrackScreenInteractive != enabled) {
            this.mTrackScreenInteractive = enabled;
            this.mStats.reset();
        }
    }

    private void setIgnoreBatteryStatus(boolean ignore) {
        if (this.mIgnoreBatteryStatus != ignore) {
            this.mStats.setIgnoreBatteryStatus(ignore);
            this.mIgnoreBatteryStatus = ignore;
            this.mStats.reset();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSamplingInterval(int samplingInterval) {
        if (samplingInterval > 0) {
            this.mStats.setSamplingInterval(samplingInterval);
        } else {
            android.util.Slog.w(TAG, "Ignored invalid sampling interval (value must be positive): " + samplingInterval);
        }
    }

    public static class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.LooperStatsService mService;
        private final com.android.server.LooperStatsService.SettingsObserver mSettingsObserver;
        private final com.android.internal.os.LooperStats mStats;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mStats = new com.android.internal.os.LooperStats(1000, 1500);
            this.mService = new com.android.server.LooperStatsService(getContext(), this.mStats);
            this.mSettingsObserver = new com.android.server.LooperStatsService.SettingsObserver(this.mService);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishLocalService(com.android.internal.os.LooperStats.class, this.mStats);
            publishBinderService(com.android.server.LooperStatsService.LOOPER_STATS_SERVICE_NAME, this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (500 == phase) {
                this.mService.initFromSettings();
                android.net.Uri settingsUri = android.provider.Settings.Global.getUriFor(com.android.server.LooperStatsService.LOOPER_STATS_SERVICE_NAME);
                getContext().getContentResolver().registerContentObserver(settingsUri, false, this.mSettingsObserver, 0);
                this.mStats.setDeviceState((com.android.internal.os.CachedDeviceState.Readonly) getLocalService(com.android.internal.os.CachedDeviceState.Readonly.class));
            }
        }
    }

    private static class SettingsObserver extends android.database.ContentObserver {
        private final com.android.server.LooperStatsService mService;

        SettingsObserver(com.android.server.LooperStatsService service) {
            super(com.android.internal.os.BackgroundThread.getHandler());
            this.mService = service;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            this.mService.initFromSettings();
        }
    }

    private class LooperShellCommand extends android.os.ShellCommand {
        private LooperShellCommand() {
        }

        public int onCommand(java.lang.String cmd) {
            if (com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE.equals(cmd)) {
                com.android.server.LooperStatsService.this.setEnabled(true);
                return 0;
            }
            if ("disable".equals(cmd)) {
                com.android.server.LooperStatsService.this.setEnabled(false);
                return 0;
            }
            if ("reset".equals(cmd)) {
                com.android.server.LooperStatsService.this.mStats.reset();
                return 0;
            }
            if (com.android.server.LooperStatsService.SETTINGS_SAMPLING_INTERVAL_KEY.equals(cmd)) {
                int sampling = java.lang.Integer.parseUnsignedInt(getNextArgRequired());
                com.android.server.LooperStatsService.this.setSamplingInterval(sampling);
                return 0;
            }
            int sampling2 = handleDefaultCommands(cmd);
            return sampling2;
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("looper_stats commands:");
            pw.println("  enable: Enable collecting stats.");
            pw.println("  disable: Disable collecting stats.");
            pw.println("  sampling_interval: Change the sampling interval.");
            pw.println("  reset: Reset stats.");
        }
    }
}
