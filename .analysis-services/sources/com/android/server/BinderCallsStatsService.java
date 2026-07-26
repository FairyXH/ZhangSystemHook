package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class BinderCallsStatsService extends android.os.Binder {
    private static final java.lang.String PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING = "persist.sys.binder_calls_detailed_tracking";
    private static final java.lang.String SERVICE_NAME = "binder_calls_stats";
    private static final java.lang.String TAG = "BinderCallsStatsService";
    private final com.android.internal.os.BinderCallsStats mBinderCallsStats;
    private com.android.server.BinderCallsStatsService.SettingsObserver mSettingsObserver;
    private final com.android.server.BinderCallsStatsService.AuthorizedWorkSourceProvider mWorkSourceProvider;

    static class AuthorizedWorkSourceProvider implements com.android.internal.os.BinderInternal.WorkSourceProvider {
        private android.util.ArraySet<java.lang.Integer> mAppIdTrustlist = new android.util.ArraySet<>();

        AuthorizedWorkSourceProvider() {
        }

        public int resolveWorkSourceUid(int untrustedWorkSourceUid) {
            int callingUid = getCallingUid();
            int appId = android.os.UserHandle.getAppId(callingUid);
            if (this.mAppIdTrustlist.contains(java.lang.Integer.valueOf(appId))) {
                boolean isWorkSourceSet = untrustedWorkSourceUid != -1;
                return isWorkSourceSet ? untrustedWorkSourceUid : callingUid;
            }
            return callingUid;
        }

        public void systemReady(android.content.Context context) {
            this.mAppIdTrustlist = createAppidTrustlist(context);
        }

        public void dump(java.io.PrintWriter pw, com.android.internal.os.AppIdToPackageMap packageMap) {
            pw.println("AppIds of apps that can set the work source:");
            android.util.ArraySet<java.lang.Integer> trustlist = this.mAppIdTrustlist;
            for (java.lang.Integer appId : trustlist) {
                pw.println("\t- " + packageMap.mapAppId(appId.intValue()));
            }
        }

        protected int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        private android.util.ArraySet<java.lang.Integer> createAppidTrustlist(android.content.Context context) {
            android.util.ArraySet<java.lang.Integer> trustlist = new android.util.ArraySet<>();
            trustlist.add(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(android.os.Process.myUid())));
            android.content.pm.PackageManager pm = context.getPackageManager();
            java.lang.String[] permissions = {"android.permission.UPDATE_DEVICE_STATS"};
            java.util.List<android.content.pm.PackageInfo> packages = pm.getPackagesHoldingPermissions(permissions, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED);
            int packagesSize = packages.size();
            for (int i = 0; i < packagesSize; i++) {
                android.content.pm.PackageInfo pkgInfo = packages.get(i);
                try {
                    int uid = pm.getPackageUid(pkgInfo.packageName, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED);
                    int appId = android.os.UserHandle.getAppId(uid);
                    trustlist.add(java.lang.Integer.valueOf(appId));
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Slog.e(com.android.server.BinderCallsStatsService.TAG, "Cannot find uid for package name " + pkgInfo.packageName, e);
                }
            }
            return trustlist;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class SettingsObserver extends android.database.ContentObserver {
        private final com.android.internal.os.BinderCallsStats mBinderCallsStats;
        private final android.content.Context mContext;
        private boolean mEnabled;
        private final android.util.KeyValueListParser mParser;
        private final android.net.Uri mUri;
        private final com.android.server.BinderCallsStatsService.AuthorizedWorkSourceProvider mWorkSourceProvider;

        SettingsObserver(android.content.Context context, com.android.internal.os.BinderCallsStats binderCallsStats, com.android.server.BinderCallsStatsService.AuthorizedWorkSourceProvider workSourceProvider) {
            super(com.android.internal.os.BackgroundThread.getHandler());
            this.mUri = android.provider.Settings.Global.getUriFor(com.android.server.BinderCallsStatsService.SERVICE_NAME);
            this.mParser = new android.util.KeyValueListParser(',');
            this.mContext = context;
            context.getContentResolver().registerContentObserver(this.mUri, false, this, 0);
            this.mBinderCallsStats = binderCallsStats;
            this.mWorkSourceProvider = workSourceProvider;
            onChange();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (this.mUri.equals(uri)) {
                onChange();
            }
        }

        public void onChange() {
            if (!android.os.SystemProperties.get(com.android.server.BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING).isEmpty()) {
                return;
            }
            try {
                this.mParser.setString(android.provider.Settings.Global.getString(this.mContext.getContentResolver(), com.android.server.BinderCallsStatsService.SERVICE_NAME));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(com.android.server.BinderCallsStatsService.TAG, "Bad binder call stats settings", e);
            }
            this.mBinderCallsStats.setDetailedTracking(this.mParser.getBoolean("detailed_tracking", true));
            this.mBinderCallsStats.setSamplingInterval(this.mParser.getInt("sampling_interval", 1000));
            this.mBinderCallsStats.setMaxBinderCallStats(this.mParser.getInt("max_call_stats_count", android.net.util.NetworkConstants.ETHER_MTU));
            this.mBinderCallsStats.setTrackScreenInteractive(this.mParser.getBoolean("track_screen_state", false));
            this.mBinderCallsStats.setTrackDirectCallerUid(this.mParser.getBoolean("track_calling_uid", true));
            this.mBinderCallsStats.setIgnoreBatteryStatus(this.mParser.getBoolean("ignore_battery_status", false));
            this.mBinderCallsStats.setShardingModulo(this.mParser.getInt("sharding_modulo", 1));
            this.mBinderCallsStats.setCollectLatencyData(this.mParser.getBoolean("collect_latency_data", true));
            com.android.internal.os.BinderCallsStats.SettingsObserver.configureLatencyObserver(this.mParser, this.mBinderCallsStats.getLatencyObserver());
            boolean enabled = this.mParser.getBoolean(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED, true);
            if (this.mEnabled != enabled) {
                if (enabled) {
                    android.os.Binder.setObserver(this.mBinderCallsStats);
                    android.os.Binder.setProxyTransactListener(new android.os.Binder.PropagateWorkSourceTransactListener());
                    android.os.Binder.setWorkSourceProvider(this.mWorkSourceProvider);
                } else {
                    android.os.Binder.setObserver(null);
                    android.os.Binder.setProxyTransactListener(null);
                    android.os.Binder.setWorkSourceProvider(new com.android.internal.os.BinderInternal.WorkSourceProvider() { // from class: com.android.server.BinderCallsStatsService$SettingsObserver$$ExternalSyntheticLambda0
                        public final int resolveWorkSourceUid(int i) {
                            return android.os.Binder.getCallingUid();
                        }
                    });
                }
                this.mEnabled = enabled;
                this.mBinderCallsStats.reset();
                this.mBinderCallsStats.setAddDebugEntries(enabled);
                this.mBinderCallsStats.getLatencyObserver().reset();
            }
        }
    }

    public static class Internal {
        private final com.android.internal.os.BinderCallsStats mBinderCallsStats;

        Internal(com.android.internal.os.BinderCallsStats binderCallsStats) {
            this.mBinderCallsStats = binderCallsStats;
        }

        public void reset() {
            this.mBinderCallsStats.reset();
        }

        public java.util.ArrayList<com.android.internal.os.BinderCallsStats.ExportedCallStat> getExportedCallStats() {
            return this.mBinderCallsStats.getExportedCallStats();
        }

        public android.util.ArrayMap<java.lang.String, java.lang.Integer> getExportedExceptionStats() {
            return this.mBinderCallsStats.getExportedExceptionStats();
        }
    }

    public static class LifeCycle extends com.android.server.SystemService {
        private com.android.internal.os.BinderCallsStats mBinderCallsStats;
        private com.android.server.BinderCallsStatsService mService;
        private com.android.server.BinderCallsStatsService.AuthorizedWorkSourceProvider mWorkSourceProvider;

        public LifeCycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mBinderCallsStats = new com.android.internal.os.BinderCallsStats(new com.android.internal.os.BinderCallsStats.Injector());
            this.mWorkSourceProvider = new com.android.server.BinderCallsStatsService.AuthorizedWorkSourceProvider();
            this.mService = new com.android.server.BinderCallsStatsService(this.mBinderCallsStats, this.mWorkSourceProvider);
            publishLocalService(com.android.server.BinderCallsStatsService.Internal.class, new com.android.server.BinderCallsStatsService.Internal(this.mBinderCallsStats));
            publishBinderService(com.android.server.BinderCallsStatsService.SERVICE_NAME, this.mService);
            boolean detailedTrackingEnabled = android.os.SystemProperties.getBoolean(com.android.server.BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING, false);
            if (detailedTrackingEnabled) {
                android.util.Slog.i(com.android.server.BinderCallsStatsService.TAG, "Enabled CPU usage tracking for binder calls. Controlled by persist.sys.binder_calls_detailed_tracking or via dumpsys binder_calls_stats --enable-detailed-tracking");
                this.mBinderCallsStats.setDetailedTracking(true);
            }
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (500 == phase) {
                com.android.internal.os.CachedDeviceState.Readonly deviceState = (com.android.internal.os.CachedDeviceState.Readonly) getLocalService(com.android.internal.os.CachedDeviceState.Readonly.class);
                this.mBinderCallsStats.setDeviceState(deviceState);
                if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
                    final android.os.BatteryStatsInternal batteryStatsInternal = (android.os.BatteryStatsInternal) getLocalService(android.os.BatteryStatsInternal.class);
                    this.mBinderCallsStats.setCallStatsObserver(new com.android.internal.os.BinderInternal.CallStatsObserver() { // from class: com.android.server.BinderCallsStatsService.LifeCycle.1
                        public void noteCallStats(int workSourceUid, long incrementalCallCount, java.util.Collection<com.android.internal.os.BinderCallsStats.CallStat> callStats) {
                            batteryStatsInternal.noteBinderCallStats(workSourceUid, incrementalCallCount, callStats);
                        }

                        public void noteBinderThreadNativeIds(int[] binderThreadNativeTids) {
                            batteryStatsInternal.noteBinderThreadNativeIds(binderThreadNativeTids);
                        }
                    });
                }
                this.mWorkSourceProvider.systemReady(getContext());
                this.mService.systemReady(getContext());
            }
        }
    }

    BinderCallsStatsService(com.android.internal.os.BinderCallsStats binderCallsStats, com.android.server.BinderCallsStatsService.AuthorizedWorkSourceProvider workSourceProvider) {
        this.mBinderCallsStats = binderCallsStats;
        this.mWorkSourceProvider = workSourceProvider;
    }

    public void systemReady(android.content.Context context) {
        this.mSettingsObserver = new com.android.server.BinderCallsStatsService.SettingsObserver(context, this.mBinderCallsStats, this.mWorkSourceProvider);
    }

    public void reset() {
        android.util.Slog.i(TAG, "Resetting stats");
        this.mBinderCallsStats.reset();
    }

    @Override // android.os.Binder
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(android.app.ActivityThread.currentApplication(), SERVICE_NAME, pw)) {
            return;
        }
        boolean verbose = false;
        int worksourceUid = -1;
        if (args != null) {
            int i = 0;
            while (i < args.length) {
                java.lang.String arg = args[i];
                if ("-a".equals(arg)) {
                    verbose = true;
                } else if ("-h".equals(arg)) {
                    pw.println("dumpsys binder_calls_stats options:");
                    pw.println("  -a: Verbose");
                    pw.println("  --work-source-uid <UID>: Dump binder calls from the UID");
                    return;
                } else if ("--work-source-uid".equals(arg)) {
                    i++;
                    if (i >= args.length) {
                        throw new java.lang.IllegalArgumentException("Argument expected after \"" + arg + "\"");
                    }
                    java.lang.String uidArg = args[i];
                    try {
                        worksourceUid = java.lang.Integer.parseInt(uidArg);
                    } catch (java.lang.NumberFormatException e) {
                        pw.println("Invalid UID: " + uidArg);
                        return;
                    }
                } else {
                    continue;
                }
                i++;
            }
            int i2 = args.length;
            if (i2 > 0 && worksourceUid == -1) {
                com.android.server.BinderCallsStatsService.BinderCallsStatsShellCommand command = new com.android.server.BinderCallsStatsService.BinderCallsStatsShellCommand(pw);
                int status = command.exec(this, null, java.io.FileDescriptor.out, java.io.FileDescriptor.err, args);
                if (status == 0) {
                    return;
                }
            }
        }
        this.mBinderCallsStats.dump(pw, com.android.internal.os.AppIdToPackageMap.getSnapshot(), worksourceUid, verbose);
    }

    public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
        android.os.ShellCommand command = new com.android.server.BinderCallsStatsService.BinderCallsStatsShellCommand(null);
        int status = command.exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
        if (status != 0) {
            command.onHelp();
        }
        return status;
    }

    private class BinderCallsStatsShellCommand extends android.os.ShellCommand {
        private final java.io.PrintWriter mPrintWriter;

        BinderCallsStatsShellCommand(java.io.PrintWriter printWriter) {
            this.mPrintWriter = printWriter;
        }

        public java.io.PrintWriter getOutPrintWriter() {
            if (this.mPrintWriter != null) {
                return this.mPrintWriter;
            }
            return super.getOutPrintWriter();
        }

        public int onCommand(java.lang.String cmd) {
            byte b;
            java.io.PrintWriter pw = getOutPrintWriter();
            if (cmd == null) {
                return -1;
            }
            switch (cmd.hashCode()) {
                case -1615291473:
                    b = !cmd.equals("--reset") ? (byte) -1 : (byte) 0;
                    break;
                case -1289263917:
                    b = !cmd.equals("--no-sampling") ? (byte) -1 : (byte) 3;
                    break;
                case -1237677752:
                    b = !cmd.equals("--disable") ? (byte) -1 : (byte) 2;
                    break;
                case -534486470:
                    b = !cmd.equals("--work-source-uid") ? (byte) -1 : (byte) 7;
                    break;
                case -106516359:
                    b = !cmd.equals("--dump-worksource-provider") ? (byte) -1 : (byte) 6;
                    break;
                case 1101165347:
                    b = !cmd.equals("--enable") ? (byte) -1 : (byte) 1;
                    break;
                case 1448286703:
                    b = !cmd.equals("--disable-detailed-tracking") ? (byte) -1 : (byte) 5;
                    break;
                case 2041864970:
                    b = !cmd.equals("--enable-detailed-tracking") ? (byte) -1 : (byte) 4;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    com.android.server.BinderCallsStatsService.this.reset();
                    pw.println("binder_calls_stats reset.");
                    return 0;
                case 1:
                    android.os.Binder.setObserver(com.android.server.BinderCallsStatsService.this.mBinderCallsStats);
                    return 0;
                case 2:
                    android.os.Binder.setObserver(null);
                    return 0;
                case 3:
                    com.android.server.BinderCallsStatsService.this.mBinderCallsStats.setSamplingInterval(1);
                    return 0;
                case 4:
                    android.os.SystemProperties.set(com.android.server.BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING, "1");
                    com.android.server.BinderCallsStatsService.this.mBinderCallsStats.setDetailedTracking(true);
                    pw.println("Detailed tracking enabled");
                    return 0;
                case 5:
                    android.os.SystemProperties.set(com.android.server.BinderCallsStatsService.PERSIST_SYS_BINDER_CALLS_DETAILED_TRACKING, "");
                    com.android.server.BinderCallsStatsService.this.mBinderCallsStats.setDetailedTracking(false);
                    pw.println("Detailed tracking disabled");
                    return 0;
                case 6:
                    com.android.server.BinderCallsStatsService.this.mBinderCallsStats.setDetailedTracking(true);
                    com.android.server.BinderCallsStatsService.this.mWorkSourceProvider.dump(pw, com.android.internal.os.AppIdToPackageMap.getSnapshot());
                    return 0;
                case 7:
                    java.lang.String uidArg = getNextArgRequired();
                    try {
                        int uid = java.lang.Integer.parseInt(uidArg);
                        com.android.server.BinderCallsStatsService.this.mBinderCallsStats.recordAllCallsForWorkSourceUid(uid);
                        return 0;
                    } catch (java.lang.NumberFormatException e) {
                        pw.println("Invalid UID: " + uidArg);
                        return -1;
                    }
                default:
                    return handleDefaultCommands(cmd);
            }
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("binder_calls_stats commands:");
            pw.println("  --reset: Reset stats");
            pw.println("  --enable: Enable tracking binder calls");
            pw.println("  --disable: Disables tracking binder calls");
            pw.println("  --no-sampling: Tracks all calls");
            pw.println("  --enable-detailed-tracking: Enables detailed tracking");
            pw.println("  --disable-detailed-tracking: Disables detailed tracking");
            pw.println("  --work-source-uid <UID>: Track all binder calls from the UID");
        }
    }
}
