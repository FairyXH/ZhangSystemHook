package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class SystemServer implements android.util.Dumpable {
    private static final java.lang.String AD_SERVICES_MANAGER_SERVICE_CLASS = "com.android.server.adservices.AdServicesManagerService$Lifecycle";
    private static final java.lang.String APPSEARCH_MODULE_LIFECYCLE_CLASS = "com.android.server.appsearch.AppSearchModule$Lifecycle";
    private static final java.lang.String ARC_NETWORK_SERVICE_CLASS = "com.android.server.arc.net.ArcNetworkService";
    private static final java.lang.String ARC_PERSISTENT_DATA_BLOCK_SERVICE_CLASS = "com.android.server.arc.persistent_data_block.ArcPersistentDataBlockService";
    private static final java.lang.String ARC_SYSTEM_HEALTH_SERVICE = "com.android.server.arc.health.ArcSystemHealthService";
    private static final java.lang.String BLOCK_MAP_FILE = "/cache/recovery/block.map";
    private static final java.lang.String BLUETOOTH_APEX_SERVICE_JAR_PATH = "/apex/com.android.btservices/javalib/service-bluetooth.jar";
    private static final java.lang.String BLUETOOTH_SERVICE_CLASS = "com.android.server.bluetooth.BluetoothService";
    private static final java.lang.String CAR_SERVICE_HELPER_SERVICE_CLASS = "com.android.internal.car.CarServiceHelperService";
    private static final java.lang.String CONNECTIVITY_SERVICE_APEX_PATH = "/apex/com.android.tethering/javalib/service-connectivity.jar";
    private static final java.lang.String CONNECTIVITY_SERVICE_INITIALIZER_CLASS = "com.android.server.ConnectivityServiceInitializer";
    private static final java.lang.String DEVICE_LOCK_APEX_PATH = "/apex/com.android.devicelock/javalib/service-devicelock.jar";
    private static final java.lang.String DEVICE_LOCK_SERVICE_CLASS = "com.android.server.devicelock.DeviceLockService";
    private static final java.lang.String ENHANCED_CONFIRMATION_SERVICE_CLASS = "com.android.ecm.EnhancedConfirmationService";
    private static final java.lang.String HEALTHCONNECT_MANAGER_SERVICE_CLASS = "com.android.server.healthconnect.HealthConnectManagerService";
    private static final java.lang.String HEALTH_SERVICE_CLASS = "com.android.clockwork.healthservices.HealthService";
    private static final java.lang.String IOT_SERVICE_CLASS = "com.android.things.server.IoTSystemService";
    private static final java.lang.String ISOLATED_COMPILATION_SERVICE_CLASS = "com.android.server.compos.IsolatedCompilationService";
    private static final java.lang.String LOWPAN_SERVICE_CLASS = "com.android.server.lowpan.LowpanService";
    private static final int MAX_HEAP_DUMPS = 2;
    private static final java.lang.String MEDIA_COMMUNICATION_SERVICE_CLASS = "com.android.server.media.MediaCommunicationService";
    private static final java.lang.String NETWORK_STATS_SERVICE_INITIALIZER_CLASS = "com.android.server.NetworkStatsServiceInitializer";
    private static final java.lang.String ON_DEVICE_PERSONALIZATION_SYSTEM_SERVICE_CLASS = "com.android.server.ondevicepersonalization.OnDevicePersonalizationSystemService$Lifecycle";
    private static final java.lang.String PERSISTENT_DATA_BLOCK_PROP = "ro.frp.pst";
    private static final java.lang.String PROFILING_SERVICE_JAR_PATH = "/apex/com.android.profiling/javalib/service-profiling.jar";
    private static final java.lang.String PROFILING_SERVICE_LIFECYCLE_CLASS = "android.os.profiling.ProfilingService$Lifecycle";
    private static final java.lang.String REBOOT_READINESS_LIFECYCLE_CLASS = "com.android.server.scheduling.RebootReadinessManagerService$Lifecycle";
    private static final java.lang.String ROLE_SERVICE_CLASS = "com.android.role.RoleService";
    private static final java.lang.String SAFETY_CENTER_SERVICE_CLASS = "com.android.safetycenter.SafetyCenterService";
    private static final java.lang.String SCHEDULING_APEX_PATH = "/apex/com.android.scheduling/javalib/service-scheduling.jar";
    private static final java.lang.String SDK_SANDBOX_MANAGER_SERVICE_CLASS = "com.android.server.sdksandbox.SdkSandboxManagerService$Lifecycle";
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 200;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 100;
    private static final java.lang.String START_BLOB_STORE_SERVICE = "startBlobStoreManagerService";
    private static final java.lang.String START_HIDL_SERVICES = "StartHidlServices";
    private static final java.lang.String START_SENSOR_MANAGER_SERVICE = "StartISensorManagerService";
    private static final java.lang.String STATS_COMPANION_APEX_PATH = "/apex/com.android.os.statsd/javalib/service-statsd.jar";
    private static final java.lang.String STATS_COMPANION_LIFECYCLE_CLASS = "com.android.server.stats.StatsCompanion$Lifecycle";
    private static final java.lang.String SYSPROP_FDTRACK_ABORT_THRESHOLD = "persist.sys.debug.fdtrack_abort_threshold";
    private static final java.lang.String SYSPROP_FDTRACK_ENABLE_THRESHOLD = "persist.sys.debug.fdtrack_enable_threshold";
    private static final java.lang.String SYSPROP_FDTRACK_INTERVAL = "persist.sys.debug.fdtrack_interval";
    private static final java.lang.String SYSPROP_START_COUNT = "sys.system_server.start_count";
    private static final java.lang.String SYSPROP_START_ELAPSED = "sys.system_server.start_elapsed";
    private static final java.lang.String SYSPROP_START_UPTIME = "sys.system_server.start_uptime";
    private static final java.lang.String SYSTEM_STATE_DISPLAY_SERVICE_CLASS = "com.android.clockwork.systemstatedisplay.SystemStateDisplayService";
    private static final java.lang.String TAG = "SystemServer";
    private static final java.lang.String TETHERING_CONNECTOR_CLASS = "android.net.ITetheringConnector";
    private static final java.lang.String THERMAL_OBSERVER_CLASS = "com.android.clockwork.ThermalObserver";
    private static final java.lang.String UNCRYPT_PACKAGE_FILE = "/cache/recovery/uncrypt_file";
    private static final java.lang.String UPDATABLE_DEVICE_CONFIG_SERVICE_CLASS = "com.android.server.deviceconfig.DeviceConfigInit$Lifecycle";
    private static final java.lang.String UWB_APEX_SERVICE_JAR_PATH = "/apex/com.android.uwb/javalib/service-uwb.jar";
    private static final java.lang.String UWB_SERVICE_CLASS = "com.android.server.uwb.UwbService";
    private static final java.lang.String WEAR_CONNECTIVITY_SERVICE_CLASS = "com.android.clockwork.connectivity.WearConnectivityService";
    private static final java.lang.String WEAR_DEBUG_SERVICE_CLASS = "com.android.clockwork.debug.WearDebugService";
    private static final java.lang.String WEAR_DISPLAYOFFLOAD_SERVICE_CLASS = "com.android.clockwork.displayoffload.DisplayOffloadService";
    private static final java.lang.String WEAR_DISPLAY_SERVICE_CLASS = "com.android.clockwork.display.WearDisplayService";
    private static final java.lang.String WEAR_MODE_SERVICE_CLASS = "com.android.clockwork.modes.ModeManagerService";
    private static final java.lang.String WEAR_POWER_SERVICE_CLASS = "com.android.clockwork.power.WearPowerService";
    private static final java.lang.String WEAR_SETTINGS_SERVICE_CLASS = "com.android.clockwork.settings.WearSettingsService";
    private static final java.lang.String WEAR_TIME_SERVICE_CLASS = "com.android.clockwork.time.WearTimeService";
    private static final java.lang.String WIFI_APEX_SERVICE_JAR_PATH = "/apex/com.android.wifi/javalib/service-wifi.jar";
    private static final java.lang.String WIFI_AWARE_SERVICE_CLASS = "com.android.server.wifi.aware.WifiAwareService";
    private static final java.lang.String WIFI_P2P_SERVICE_CLASS = "com.android.server.wifi.p2p.WifiP2pService";
    private static final java.lang.String WIFI_RTT_SERVICE_CLASS = "com.android.server.wifi.rtt.RttService";
    private static final java.lang.String WIFI_SCANNING_SERVICE_CLASS = "com.android.server.wifi.scanner.WifiScanningService";
    private static final java.lang.String WIFI_SERVICE_CLASS = "com.android.server.wifi.WifiService";
    private static final java.lang.String WRIST_ORIENTATION_SERVICE_CLASS = "com.android.clockwork.wristorientation.WristOrientationService";
    private static final int sMaxBinderThreads = 31;
    private static java.util.LinkedList<android.util.Pair<java.lang.String, android.app.ApplicationErrorReport.CrashInfo>> sPendingWtfs;
    private com.android.server.am.ActivityManagerService mActivityManagerService;
    private android.content.ContentResolver mContentResolver;
    private com.android.server.pm.DataLoaderManagerService mDataLoaderManagerService;
    private com.android.server.display.DisplayManagerService mDisplayManagerService;
    private com.android.server.EntropyMixer mEntropyMixer;
    private boolean mFirstBoot;
    private android.content.pm.PackageManager mPackageManager;
    private com.android.server.pm.PackageManagerService mPackageManagerService;
    private com.android.server.power.PowerManagerService mPowerManagerService;
    private java.util.Timer mProfilerSnapshotTimer;
    private final boolean mRuntimeRestart;
    public com.android.server.ISystemServerSocExt mSocExt;
    private android.content.Context mSystemContext;
    private com.android.server.ISystemServerExt mSystemServerExt;
    private com.android.server.SystemServiceManager mSystemServiceManager;
    private com.android.server.webkit.WebViewUpdateService mWebViewUpdateService;
    private com.android.server.wm.WindowManagerGlobalLock mWindowManagerGlobalLock;
    private java.util.concurrent.Future<?> mZygotePreload;
    private static int DEFAULT_SYSTEM_THEME = android.R.style.Theme.DeviceDefault.ResolverCommon;
    private static final java.io.File HEAP_DUMP_PATH = new java.io.File("/data/system/heapdump/");
    private long mIncrementalServiceHandle = 0;
    private final com.android.server.SystemServer.SystemServerDumper mDumper = new com.android.server.SystemServer.SystemServerDumper();
    private system.ext.preload.IServicesPreloadExt mServicesPreloadExt = (system.ext.preload.IServicesPreloadExt) system.ext.loader.core.ExtLoader.type(system.ext.preload.IServicesPreloadExt.class).base(this).create();
    private final int mFactoryTestMode = android.os.FactoryTest.getMode();
    private final int mStartCount = android.os.SystemProperties.getInt(SYSPROP_START_COUNT, 0) + 1;
    private final long mRuntimeStartElapsedTime = android.os.SystemClock.elapsedRealtime();
    private final long mRuntimeStartUptime = android.os.SystemClock.uptimeMillis();

    private static native void fdtrackAbort();

    private static native void initZygoteChildHeapProfiling();

    private static native void setIncrementalServiceSystemReady(long j);

    private static native void startHidlServices();

    private static native void startISensorManagerService();

    private static native void startIStatsService();

    private static native long startIncrementalService();

    private static native void startMemtrackProxyService();

    private static int getMaxFd() throws android.system.ErrnoException {
        java.io.FileDescriptor fd = null;
        try {
            try {
                fd = android.system.Os.open("/dev/null", android.system.OsConstants.O_RDONLY | android.system.OsConstants.O_CLOEXEC, 0);
                int int$ = fd.getInt$();
                if (fd != null) {
                    try {
                        android.system.Os.close(fd);
                    } catch (android.system.ErrnoException ex) {
                        throw new java.lang.RuntimeException(ex);
                    }
                }
                return int$;
            } catch (java.lang.Throwable ex2) {
                if (fd != null) {
                    try {
                        android.system.Os.close(fd);
                    } catch (android.system.ErrnoException ex3) {
                        throw new java.lang.RuntimeException(ex3);
                    }
                }
                throw ex2;
            }
        } catch (android.system.ErrnoException ex4) {
            android.util.Slog.e("System", "Failed to get maximum fd: " + ex4);
            if (fd == null) {
                return Integer.MAX_VALUE;
            }
            try {
                android.system.Os.close(fd);
                return Integer.MAX_VALUE;
            } catch (android.system.ErrnoException ex5) {
                throw new java.lang.RuntimeException(ex5);
            }
        }
    }

    private static void dumpHprof() {
        java.util.TreeSet<java.io.File> existingTombstones = new java.util.TreeSet<>();
        for (java.io.File file : HEAP_DUMP_PATH.listFiles()) {
            if (file.isFile() && file.getName().startsWith("fdtrack-")) {
                existingTombstones.add(file);
            }
        }
        if (existingTombstones.size() >= 2) {
            for (int i = 0; i < 1; i++) {
                existingTombstones.pollLast();
            }
            for (java.io.File file2 : existingTombstones) {
                if (!file2.delete()) {
                    android.util.Slog.w("System", "Failed to clean up hprof " + file2);
                }
            }
        }
        try {
            java.lang.String date = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date());
            java.lang.String filename = "/data/system/heapdump/fdtrack-" + date + ".hprof";
            android.os.Debug.dumpHprofData(filename);
        } catch (java.io.IOException ex) {
            android.util.Slog.e("System", "Failed to dump fdtrack hprof", ex);
        }
    }

    private static void spawnFdLeakCheckThread() {
        final int enableThreshold = android.os.SystemProperties.getInt(SYSPROP_FDTRACK_ENABLE_THRESHOLD, 1600);
        final int abortThreshold = android.os.SystemProperties.getInt(SYSPROP_FDTRACK_ABORT_THRESHOLD, 3000);
        final int checkInterval = android.os.SystemProperties.getInt(SYSPROP_FDTRACK_INTERVAL, 120);
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws android.system.ErrnoException {
                com.android.server.SystemServer.lambda$spawnFdLeakCheckThread$0(enableThreshold, abortThreshold, checkInterval);
            }
        }).start();
    }

    static /* synthetic */ void lambda$spawnFdLeakCheckThread$0(int enableThreshold, int abortThreshold, int checkInterval) throws android.system.ErrnoException {
        boolean enabled = false;
        long nextWrite = 0;
        while (true) {
            int maxFd = getMaxFd();
            if (maxFd > enableThreshold) {
                java.lang.System.gc();
                java.lang.System.runFinalization();
                maxFd = getMaxFd();
            }
            int i = 2;
            if (maxFd > enableThreshold && !enabled) {
                android.util.Slog.i("System", "fdtrack enable threshold reached, enabling");
                com.android.internal.util.FrameworkStatsLog.write(364, 2, maxFd);
                java.lang.System.loadLibrary("fdtrack");
                enabled = true;
            } else if (maxFd > abortThreshold) {
                android.util.Slog.i("System", "fdtrack abort threshold reached, dumping and aborting");
                com.android.internal.util.FrameworkStatsLog.write(364, 3, maxFd);
                dumpHprof();
                fdtrackAbort();
            } else {
                long now = android.os.SystemClock.elapsedRealtime();
                if (now > nextWrite) {
                    long nextWrite2 = 3600000 + now;
                    if (!enabled) {
                        i = 1;
                    }
                    com.android.internal.util.FrameworkStatsLog.write(364, i, maxFd);
                    nextWrite = nextWrite2;
                }
            }
            try {
                java.lang.Thread.sleep(checkInterval * 1000);
            } catch (java.lang.InterruptedException e) {
            }
        }
    }

    public static void main(java.lang.String[] args) {
        new com.android.server.SystemServer().run();
    }

    public SystemServer() {
        android.os.Process.setStartTimes(this.mRuntimeStartElapsedTime, this.mRuntimeStartUptime, this.mRuntimeStartElapsedTime, this.mRuntimeStartUptime);
        this.mRuntimeRestart = this.mStartCount > 1;
        this.mServicesPreloadExt.preload(getClass().getClassLoader());
        this.mSystemServerExt = (com.android.server.ISystemServerExt) system.ext.loader.core.ExtLoader.type(com.android.server.ISystemServerExt.class).create();
        int themeStyle = this.mSystemServerExt.getSystemThemeStyle();
        if (themeStyle != -1) {
            DEFAULT_SYSTEM_THEME = themeStyle;
        }
        this.mSocExt = (com.android.server.ISystemServerSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.ISystemServerSocExt.class).base(this).create();
    }

    @Override // android.util.Dumpable
    public java.lang.String getDumpableName() {
        return com.android.server.SystemServer.class.getSimpleName();
    }

    @Override // android.util.Dumpable
    public void dump(java.io.PrintWriter pw, java.lang.String[] args) {
        pw.printf("Runtime restart: %b\n", java.lang.Boolean.valueOf(this.mRuntimeRestart));
        pw.printf("Start count: %d\n", java.lang.Integer.valueOf(this.mStartCount));
        pw.print("Runtime start-up time: ");
        android.util.TimeUtils.formatDuration(this.mRuntimeStartUptime, pw);
        pw.println();
        pw.print("Runtime start-elapsed time: ");
        android.util.TimeUtils.formatDuration(this.mRuntimeStartElapsedTime, pw);
        pw.println();
    }

    private final class SystemServerDumper extends android.os.Binder {
        private final android.util.ArrayMap<java.lang.String, android.util.Dumpable> mDumpables;

        private SystemServerDumper() {
            this.mDumpables = new android.util.ArrayMap<>(4);
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            android.util.IndentingPrintWriter ipw;
            boolean hasArgs = args != null && args.length > 0;
            synchronized (this.mDumpables) {
                if (hasArgs) {
                    try {
                        if ("--list".equals(args[0])) {
                            int dumpablesSize = this.mDumpables.size();
                            for (int i = 0; i < dumpablesSize; i++) {
                                pw.println(this.mDumpables.keyAt(i));
                            }
                            return;
                        }
                    } catch (java.lang.Throwable th) {
                        throw th;
                    }
                }
                if (hasArgs && "--name".equals(args[0])) {
                    if (args.length < 2) {
                        pw.println("Must pass at least one argument to --name");
                        return;
                    }
                    java.lang.String name = args[1];
                    android.util.Dumpable dumpable = this.mDumpables.get(name);
                    if (dumpable == null) {
                        pw.printf("No dumpable named %s\n", name);
                        return;
                    }
                    ipw = new android.util.IndentingPrintWriter(pw, "  ");
                    try {
                        java.lang.String[] actualArgs = (java.lang.String[]) java.util.Arrays.copyOfRange(args, 2, args.length);
                        dumpable.dump(ipw, actualArgs);
                        ipw.close();
                        return;
                    } finally {
                    }
                }
                if (com.android.server.SystemServer.this.mSystemServerExt.stabilityDynamicLogConfig(pw, args)) {
                    return;
                }
                int dumpablesSize2 = this.mDumpables.size();
                ipw = new android.util.IndentingPrintWriter(pw, "  ");
                for (int i2 = 0; i2 < dumpablesSize2; i2++) {
                    try {
                        android.util.Dumpable dumpable2 = this.mDumpables.valueAt(i2);
                        ipw.printf("%s:\n", new java.lang.Object[]{dumpable2.getDumpableName()});
                        ipw.increaseIndent();
                        dumpable2.dump(ipw, args);
                        ipw.decreaseIndent();
                        ipw.println();
                    } finally {
                    }
                }
                ipw.close();
                return;
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addDumpable(android.util.Dumpable dumpable) {
            synchronized (this.mDumpables) {
                this.mDumpables.put(dumpable.getDumpableName(), dumpable);
            }
        }
    }

    private void run() {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        try {
            t.traceBegin("InitBeforeStartServices");
            this.mSystemServerExt.runBootProtector(this.mStartCount);
            android.os.SystemProperties.set(SYSPROP_START_COUNT, java.lang.String.valueOf(this.mStartCount));
            android.os.SystemProperties.set(SYSPROP_START_ELAPSED, java.lang.String.valueOf(this.mRuntimeStartElapsedTime));
            android.os.SystemProperties.set(SYSPROP_START_UPTIME, java.lang.String.valueOf(this.mRuntimeStartUptime));
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.SYSTEM_SERVER_START, java.lang.Integer.valueOf(this.mStartCount), java.lang.Long.valueOf(this.mRuntimeStartUptime), java.lang.Long.valueOf(this.mRuntimeStartElapsedTime));
            com.android.server.SystemTimeZone.initializeTimeZoneSettingsIfRequired();
            if (!android.os.SystemProperties.get("persist.sys.language").isEmpty()) {
                java.lang.String languageTag = java.util.Locale.getDefault().toLanguageTag();
                android.os.SystemProperties.set("persist.sys.locale", languageTag);
                android.os.SystemProperties.set("persist.sys.language", "");
                android.os.SystemProperties.set("persist.sys.country", "");
                android.os.SystemProperties.set("persist.sys.localevar", "");
            }
            android.os.Binder.setWarnOnBlocking(true);
            android.content.pm.PackageItemInfo.forceSafeLabels();
            android.database.sqlite.SQLiteGlobal.sDefaultSyncMode = "FULL";
            android.database.sqlite.SQLiteCompatibilityWalFlags.init((java.lang.String) null);
            android.util.Slog.i(TAG, "Entered the Android system server!");
            long uptimeMillis = android.os.SystemClock.elapsedRealtime();
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BOOT_PROGRESS_SYSTEM_RUN, uptimeMillis);
            if (!this.mRuntimeRestart) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 19, uptimeMillis);
            }
            ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:SysServerInit_START");
            this.mSystemServerExt.setBootstage(true);
            android.os.SystemProperties.set("persist.sys.dalvik.vm.lib.2", dalvik.system.VMRuntime.getRuntime().vmLibrary());
            dalvik.system.VMRuntime.getRuntime().clearGrowthLimit();
            android.os.Build.ensureFingerprintProperty();
            android.os.Environment.setUserRequired(true);
            android.os.BaseBundle.setShouldDefuse(true);
            android.os.Parcel.setStackTraceParceling(true);
            com.android.internal.os.BinderInternal.disableBackgroundScheduling(true);
            com.android.internal.os.BinderInternal.setMaxThreads(31);
            android.os.Process.setCanSelfBackground(false);
            android.os.Looper.prepareMainLooper();
            android.os.Looper.getMainLooper().setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            android.app.SystemServiceRegistry.sEnableServiceNotFoundWtf = true;
            java.lang.System.loadLibrary("android_servers");
            initZygoteChildHeapProfiling();
            performPendingShutdown();
            createSystemContext();
            android.app.ActivityThread.initializeMainlineModules();
            android.os.ServiceManager.addService("system_server_dumper", this.mDumper);
            this.mDumper.addDumpable(this);
            this.mSystemServiceManager = new com.android.server.SystemServiceManager(this.mSystemContext);
            this.mSystemServiceManager.setStartInfo(this.mRuntimeRestart, this.mRuntimeStartElapsedTime, this.mRuntimeStartUptime);
            this.mDumper.addDumpable(this.mSystemServiceManager);
            com.android.server.LocalServices.addService(com.android.server.SystemServiceManager.class, this.mSystemServiceManager);
            com.android.server.SystemServerInitThreadPool tp = com.android.server.SystemServerInitThreadPool.start();
            this.mDumper.addDumpable(tp);
            if (!com.android.text.flags.Flags.useOptimizedBoottimeFontLoading()) {
                android.util.Slog.i(TAG, "Loading pre-installed system font map.");
                this.mSystemServerExt.initFontsForserializeFontMap();
                android.graphics.Typeface.loadPreinstalledSystemFontMap();
            }
            if (android.os.Build.IS_DEBUGGABLE) {
                java.lang.String jvmtiAgent = android.os.SystemProperties.get("persist.sys.dalvik.jvmtiagent");
                if (!jvmtiAgent.isEmpty()) {
                    int equalIndex = jvmtiAgent.indexOf(61);
                    java.lang.String libraryPath = jvmtiAgent.substring(0, equalIndex);
                    java.lang.String parameterList = jvmtiAgent.substring(equalIndex + 1, jvmtiAgent.length());
                    try {
                        android.os.Debug.attachJvmtiAgent(libraryPath, parameterList, null);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e("System", "*************************************************");
                        android.util.Slog.e("System", "********** Failed to load jvmti plugin: " + jvmtiAgent);
                    }
                }
            }
            t.traceEnd();
            this.mSystemServerExt.initSystemServer(this.mSystemContext);
            this.mSocExt.setPrameters(this.mSystemServiceManager, this.mSystemContext);
            com.android.internal.os.RuntimeInit.setDefaultApplicationWtfHandler(new com.android.internal.os.RuntimeInit.ApplicationWtfHandler() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda2
                public final boolean handleApplicationWtf(android.os.IBinder iBinder, java.lang.String str, boolean z, android.app.ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo, int i) {
                    return com.android.server.SystemServer.handleEarlySystemWtf(iBinder, str, z, parcelableCrashInfo, i);
                }
            });
            try {
                t.traceBegin("StartServices");
                startBootstrapServices(t);
                startCoreServices(t);
                startOtherServices(t);
                startApexServices(t);
                updateWatchdogTimeout(t);
                com.android.server.criticalevents.CriticalEventLog.getInstance().logSystemServerStarted();
                t.traceEnd();
                android.os.StrictMode.initVmDefaults(null);
                if (!this.mRuntimeRestart && !isFirstBootOrUpgrade()) {
                    long uptimeMillis2 = android.os.SystemClock.elapsedRealtime();
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 20, uptimeMillis2);
                    if (uptimeMillis2 > 60000) {
                        android.util.Slog.wtf(com.android.server.utils.TimingsTraceAndSlog.SYSTEM_SERVER_TIMING_TAG, "SystemServer init took too long. uptimeMillis=" + uptimeMillis2);
                    }
                }
                android.os.Binder.setTransactionCallback(new android.os.IBinderCallback() { // from class: com.android.server.SystemServer.1
                    public void onTransactionError(int pid, int code, int flags, int err) {
                        com.android.server.SystemServer.this.mActivityManagerService.frozenBinderTransactionDetected(pid, code, flags, err);
                    }
                });
                this.mSystemServerExt.setBootstage(false);
                android.os.Process.setThreadPriority(-2);
                ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:SysServerInit_END");
                android.os.Looper.loop();
                throw new java.lang.RuntimeException("Main thread loop unexpectedly exited");
            } finally {
            }
        } finally {
        }
    }

    private static boolean isValidTimeZoneId(java.lang.String timezoneProperty) {
        return (timezoneProperty == null || timezoneProperty.isEmpty() || !com.android.i18n.timezone.ZoneInfoDb.getInstance().hasTimeZone(timezoneProperty)) ? false : true;
    }

    private boolean isFirstBootOrUpgrade() {
        return this.mPackageManagerService.isFirstBoot() || this.mPackageManagerService.isDeviceUpgrading();
    }

    private void reportWtf(java.lang.String msg, java.lang.Throwable e) {
        android.util.Slog.w(TAG, "***********************************************");
        android.util.Slog.wtf(TAG, "BOOT FAILURE " + msg, e);
    }

    private void performPendingShutdown() {
        final java.lang.String reason;
        java.lang.String shutdownAction = android.os.SystemProperties.get(com.android.server.power.ShutdownThread.SHUTDOWN_ACTION_PROPERTY, "");
        if (shutdownAction != null && shutdownAction.length() > 0) {
            final boolean reboot = shutdownAction.charAt(0) == '1';
            if (shutdownAction.length() > 1) {
                reason = shutdownAction.substring(1, shutdownAction.length());
            } else {
                reason = null;
            }
            if (reason != null && reason.startsWith("recovery-update")) {
                java.io.File packageFile = new java.io.File(UNCRYPT_PACKAGE_FILE);
                if (packageFile.exists()) {
                    java.lang.String filename = null;
                    try {
                        filename = android.os.FileUtils.readTextFile(packageFile, 0, null);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Error reading uncrypt package file", e);
                    }
                    if (filename != null && filename.startsWith("/data") && !new java.io.File(BLOCK_MAP_FILE).exists()) {
                        android.util.Slog.e(TAG, "Can't find block map file, uncrypt failed or unexpected runtime restart?");
                        return;
                    }
                }
            }
            java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.SystemServer.2
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.power.ShutdownThread.rebootOrShutdown(null, reboot, reason);
                }
            };
            android.os.Message msg = android.os.Message.obtain(com.android.server.UiThread.getHandler(), runnable);
            msg.setAsynchronous(true);
            com.android.server.UiThread.getHandler().sendMessage(msg);
        }
    }

    private void createSystemContext() {
        android.app.ActivityThread activityThread = android.app.ActivityThread.systemMain();
        this.mSystemContext = activityThread.getSystemContext();
        this.mSystemContext.setTheme(DEFAULT_SYSTEM_THEME);
        activityThread.getSystemUiContext().setTheme(DEFAULT_SYSTEM_THEME);
        android.os.Trace.registerWithPerfetto();
    }

    /* JADX WARN: Type inference failed for: r5v2, types: [android.os.IBinder, com.android.server.compat.PlatformCompat] */
    private void startBootstrapServices(com.android.server.utils.TimingsTraceAndSlog t) {
        t.traceBegin("startBootstrapServices");
        this.mSystemServerExt.setDataNormalizationManager();
        t.traceBegin("ArtModuleServiceInitializer");
        com.android.server.art.ArtModuleServiceInitializer.setArtModuleServiceManager(new android.os.ArtModuleServiceManager());
        t.traceEnd();
        t.traceBegin("StartWatchdog");
        com.android.server.Watchdog watchdog = com.android.server.Watchdog.getInstance();
        watchdog.start();
        this.mDumper.addDumpable(watchdog);
        t.traceEnd();
        android.util.Slog.i(TAG, "Reading configuration...");
        t.traceBegin("ReadingSystemConfig");
        com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.SystemConfig.getInstance();
            }
        }, "ReadingSystemConfig");
        t.traceEnd();
        t.traceBegin("PlatformCompat");
        ?? platformCompat = new com.android.server.compat.PlatformCompat(this.mSystemContext);
        android.os.ServiceManager.addService("platform_compat", (android.os.IBinder) platformCompat);
        android.os.ServiceManager.addService("platform_compat_native", new com.android.server.compat.PlatformCompatNative(platformCompat));
        android.app.AppCompatCallbacks.install(new long[0], new long[0]);
        t.traceEnd();
        t.traceBegin("StartFileIntegrityService");
        this.mSystemServiceManager.startService(com.android.server.security.FileIntegrityService.class);
        t.traceEnd();
        t.traceBegin("StartInstaller");
        com.android.server.pm.Installer installer = (com.android.server.pm.Installer) this.mSystemServiceManager.startService(com.android.server.pm.Installer.class);
        t.traceEnd();
        t.traceBegin("DeviceIdentifiersPolicyService");
        this.mSystemServiceManager.startService(com.android.server.os.DeviceIdentifiersPolicyService.class);
        t.traceEnd();
        t.traceBegin("StartFeatureFlagsService");
        this.mSystemServiceManager.startService(com.android.server.flags.FeatureFlagsService.class);
        t.traceEnd();
        t.traceBegin("UriGrantsManagerService");
        this.mSystemServiceManager.startService(com.android.server.uri.UriGrantsManagerService.Lifecycle.class);
        t.traceEnd();
        t.traceBegin("StartPowerStatsService");
        this.mSystemServiceManager.startService(com.android.server.powerstats.PowerStatsService.class);
        t.traceEnd();
        this.mSystemServerExt.addOplusDevicePolicyService();
        t.traceBegin("StartIStatsService");
        startIStatsService();
        t.traceEnd();
        t.traceBegin("MemtrackProxyService");
        startMemtrackProxyService();
        t.traceEnd();
        t.traceBegin("StartAccessCheckingService");
        com.android.server.LocalServices.addService(com.android.server.pm.permission.PermissionMigrationHelper.class, new com.android.server.pm.permission.PermissionMigrationHelperImpl());
        com.android.server.LocalServices.addService(com.android.server.appop.AppOpMigrationHelper.class, new com.android.server.appop.AppOpMigrationHelperImpl());
        this.mSystemServiceManager.startService(com.android.server.permission.access.AccessCheckingService.class);
        t.traceEnd();
        t.traceBegin("StartActivityManager");
        com.android.server.wm.ActivityTaskManagerService atm = ((com.android.server.wm.ActivityTaskManagerService.Lifecycle) this.mSystemServiceManager.startService(com.android.server.wm.ActivityTaskManagerService.Lifecycle.class)).getService();
        android.util.Slog.i(TAG, "Ams Service");
        this.mActivityManagerService = com.android.server.am.ActivityManagerService.Lifecycle.startService(this.mSystemServiceManager, atm);
        this.mActivityManagerService.setSystemServiceManager(this.mSystemServiceManager);
        this.mActivityManagerService.setInstaller(installer);
        this.mWindowManagerGlobalLock = atm.getGlobalLock();
        t.traceEnd();
        t.traceBegin("StartDataLoaderManagerService");
        this.mDataLoaderManagerService = (com.android.server.pm.DataLoaderManagerService) this.mSystemServiceManager.startService(com.android.server.pm.DataLoaderManagerService.class);
        t.traceEnd();
        t.traceBegin("StartIncrementalService");
        this.mIncrementalServiceHandle = startIncrementalService();
        t.traceEnd();
        t.traceBegin("StartPowerManager");
        android.util.Slog.i(TAG, "Power Service");
        this.mPowerManagerService = (com.android.server.power.PowerManagerService) this.mSystemServiceManager.startService(com.android.server.power.PowerManagerService.class);
        t.traceEnd();
        t.traceBegin("StartThermalManager");
        this.mSystemServiceManager.startService(com.android.server.power.ThermalManagerService.class);
        t.traceEnd();
        t.traceBegin("InitPowerManagement");
        this.mActivityManagerService.initPowerManagement();
        t.traceEnd();
        t.traceBegin("StartRecoverySystemService");
        this.mSystemServiceManager.startService(com.android.server.recoverysystem.RecoverySystemService.Lifecycle.class);
        t.traceEnd();
        com.android.server.RescueParty.registerHealthObserver(this.mSystemContext);
        if (!com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            com.android.server.PackageWatchdog.getInstance(this.mSystemContext).noteBoot();
        }
        t.traceBegin("StartLightsService");
        this.mSystemServiceManager.startService(com.android.server.lights.LightsService.class);
        t.traceEnd();
        t.traceBegin("StartDisplayOffloadService");
        if (android.os.SystemProperties.getBoolean("config.enable_display_offload", false)) {
            this.mSystemServiceManager.startService(WEAR_DISPLAYOFFLOAD_SERVICE_CLASS);
        }
        t.traceEnd();
        android.util.Slog.i(TAG, "DisplayManager Service");
        t.traceBegin("StartDisplayManager");
        this.mDisplayManagerService = (com.android.server.display.DisplayManagerService) this.mSystemServiceManager.startService(com.android.server.display.DisplayManagerService.class);
        t.traceEnd();
        t.traceBegin("WaitForDisplay");
        this.mSystemServiceManager.startBootPhase(t, 100);
        t.traceEnd();
        if (!this.mRuntimeRestart) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 14, android.os.SystemClock.elapsedRealtime());
        }
        this.mSystemServerExt.waitForFutureNoInterrupt();
        t.traceBegin("StartDomainVerificationService");
        com.android.server.pm.verify.domain.DomainVerificationService domainVerificationService = new com.android.server.pm.verify.domain.DomainVerificationService(this.mSystemContext, com.android.server.SystemConfig.getInstance(), platformCompat);
        this.mSystemServiceManager.startService(domainVerificationService);
        t.traceEnd();
        t.traceBegin("StartPackageManagerService");
        try {
            com.android.server.Watchdog.getInstance().pauseWatchingCurrentThread("packagemanagermain");
            this.mPackageManagerService = com.android.server.pm.PackageManagerService.main(this.mSystemContext, installer, domainVerificationService, this.mFactoryTestMode != 0);
            com.android.server.Watchdog.getInstance().resumeWatchingCurrentThread("packagemanagermain");
            this.mFirstBoot = this.mPackageManagerService.isFirstBoot();
            this.mPackageManager = this.mSystemContext.getPackageManager();
            t.traceEnd();
            t.traceBegin("DexUseManagerLocal");
            com.android.server.LocalManagerRegistry.addManager(com.android.server.art.DexUseManagerLocal.class, com.android.server.art.DexUseManagerLocal.createInstance(this.mSystemContext));
            t.traceEnd();
            if (!this.mRuntimeRestart && !isFirstBootOrUpgrade()) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 15, android.os.SystemClock.elapsedRealtime());
            }
            boolean disableOtaDexopt = android.os.SystemProperties.getBoolean("config.disable_otadexopt", false);
            if (!disableOtaDexopt) {
                t.traceBegin("StartOtaDexOptService");
                try {
                    com.android.server.Watchdog.getInstance().pauseWatchingCurrentThread("moveab");
                    this.mSystemServerExt.addOtaDexoptService(this.mSystemContext, this.mPackageManagerService);
                } finally {
                    try {
                    } finally {
                    }
                }
            }
            if (android.os.Build.IS_ARC) {
                t.traceBegin("StartArcSystemHealthService");
                this.mSystemServiceManager.startService(ARC_SYSTEM_HEALTH_SERVICE);
                t.traceEnd();
            }
            t.traceBegin("StartUserManagerService");
            this.mSystemServiceManager.startService(com.android.server.pm.UserManagerService.LifeCycle.class);
            t.traceEnd();
            t.traceBegin("InitAttributerCache");
            com.android.internal.policy.AttributeCache.init(this.mSystemContext);
            t.traceEnd();
            t.traceBegin("SetSystemProcess");
            this.mActivityManagerService.setSystemProcess();
            t.traceEnd();
            platformCompat.registerPackageReceiver(this.mSystemContext);
            t.traceBegin("InitWatchdog");
            watchdog.init(this.mSystemContext, this.mActivityManagerService);
            t.traceEnd();
            this.mDisplayManagerService.setupSchedulerPolicies();
            t.traceBegin("StartOverlayManagerService");
            this.mSystemServiceManager.startService(new com.android.server.om.OverlayManagerService(this.mSystemContext));
            t.traceEnd();
            android.util.Slog.i(TAG, "Sensor Service");
            t.traceBegin("StartResourcesManagerService");
            com.android.server.resources.ResourcesManagerService resourcesService = new com.android.server.resources.ResourcesManagerService(this.mSystemContext);
            resourcesService.setActivityManagerService(this.mActivityManagerService);
            this.mSystemServiceManager.startService(resourcesService);
            t.traceEnd();
            t.traceBegin("StartSensorPrivacyService");
            this.mSystemServiceManager.startService(new com.android.server.sensorprivacy.SensorPrivacyService(this.mSystemContext));
            t.traceEnd();
            if (android.os.SystemProperties.getInt("persist.sys.displayinset.top", 0) > 0) {
                this.mActivityManagerService.updateSystemUiContext();
                ((android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class)).onOverlayChanged();
            }
            t.traceBegin("StartSensorService");
            this.mSystemServiceManager.startService(com.android.server.sensors.SensorService.class);
            t.traceEnd();
            t.traceEnd();
            this.mSystemServerExt.startBootstrapServices();
        } catch (java.lang.Throwable th) {
            com.android.server.Watchdog.getInstance().resumeWatchingCurrentThread("packagemanagermain");
            throw th;
        }
    }

    private void startCoreServices(com.android.server.utils.TimingsTraceAndSlog t) {
        t.traceBegin("startCoreServices");
        t.traceBegin("StartSystemConfigService");
        this.mSystemServiceManager.startService(com.android.server.SystemConfigService.class);
        t.traceEnd();
        android.util.Slog.i(TAG, "Battery Service");
        t.traceBegin("StartBatteryService");
        this.mSystemServiceManager.startService(com.android.server.BatteryService.class);
        t.traceEnd();
        android.util.Slog.i(TAG, "UsageStats Service");
        t.traceBegin("StartUsageService");
        this.mSystemServerExt.startUsageStatsService(this.mSystemServiceManager);
        this.mActivityManagerService.setUsageStatsManager((android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class));
        t.traceEnd();
        if (this.mPackageManager.hasSystemFeature("android.software.webview")) {
            t.traceBegin("StartWebViewUpdateService");
            this.mWebViewUpdateService = (com.android.server.webkit.WebViewUpdateService) this.mSystemServiceManager.startService(com.android.server.webkit.WebViewUpdateService.class);
            t.traceEnd();
        }
        t.traceBegin("StartCachedDeviceStateService");
        this.mSystemServiceManager.startService(com.android.server.CachedDeviceStateService.class);
        t.traceEnd();
        t.traceBegin("StartBinderCallsStatsService");
        this.mSystemServiceManager.startService(com.android.server.BinderCallsStatsService.LifeCycle.class);
        t.traceEnd();
        t.traceBegin("StartLooperStatsService");
        this.mSystemServiceManager.startService(com.android.server.LooperStatsService.Lifecycle.class);
        t.traceEnd();
        t.traceBegin("StartRollbackManagerService");
        this.mSystemServiceManager.startService(com.android.server.rollback.RollbackManagerService.class);
        t.traceEnd();
        t.traceBegin("StartNativeTombstoneManagerService");
        this.mSystemServiceManager.startService(com.android.server.os.NativeTombstoneManagerService.class);
        t.traceEnd();
        t.traceBegin("StartBugreportManagerService");
        this.mSystemServiceManager.startService(com.android.server.os.BugreportManagerService.class);
        t.traceEnd();
        t.traceBegin(com.android.server.gpu.GpuService.TAG);
        this.mSystemServiceManager.startService(com.android.server.gpu.GpuService.class);
        t.traceEnd();
        t.traceBegin("StartRemoteProvisioningService");
        this.mSystemServiceManager.startService(com.android.server.security.rkp.RemoteProvisioningService.class);
        t.traceEnd();
        if (android.os.Build.IS_DEBUGGABLE || android.os.Build.IS_ENG) {
            t.traceBegin("CpuMonitorService");
            this.mSystemServiceManager.startService(com.android.server.cpu.CpuMonitorService.class);
            t.traceEnd();
        }
        t.traceEnd();
        this.mSystemServerExt.startCoreServices();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:110:0x049d  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x05b8  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x05d5  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x010e A[Catch: all -> 0x1702, TRY_ENTER, TryCatch #60 {all -> 0x1702, blocks: (B:11:0x008e, B:21:0x011d, B:20:0x010e), top: B:798:0x008e }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01ff A[Catch: all -> 0x16e6, TRY_ENTER, TRY_LEAVE, TryCatch #34 {all -> 0x16e6, blocks: (B:31:0x01d0, B:37:0x01ff), top: B:746:0x01d0 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:530:0x10e9  */
    /* JADX WARN: Removed duplicated region for block: B:538:0x1196  */
    /* JADX WARN: Removed duplicated region for block: B:541:0x11b1  */
    /* JADX WARN: Removed duplicated region for block: B:546:0x123d  */
    /* JADX WARN: Removed duplicated region for block: B:549:0x1252  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0299 A[Catch: all -> 0x16aa, TRY_ENTER, TryCatch #5 {all -> 0x16aa, blocks: (B:48:0x0265, B:54:0x0299, B:58:0x02ac), top: B:688:0x0265 }] */
    /* JADX WARN: Removed duplicated region for block: B:551:0x1265  */
    /* JADX WARN: Removed duplicated region for block: B:554:0x1274  */
    /* JADX WARN: Removed duplicated region for block: B:555:0x128a  */
    /* JADX WARN: Removed duplicated region for block: B:558:0x1296  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x12af  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:571:0x12ea  */
    /* JADX WARN: Removed duplicated region for block: B:572:0x12fa  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x02ab  */
    /* JADX WARN: Removed duplicated region for block: B:581:0x137b  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x138d  */
    /* JADX WARN: Removed duplicated region for block: B:585:0x13a3  */
    /* JADX WARN: Removed duplicated region for block: B:700:0x021e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0343 A[Catch: all -> 0x030e, TRY_ENTER, TRY_LEAVE, TryCatch #65 {all -> 0x030e, blocks: (B:63:0x02fe, B:70:0x0343, B:79:0x03a6, B:81:0x03ac, B:74:0x0357), top: B:808:0x02fe }] */
    /* JADX WARN: Removed duplicated region for block: B:716:0x1345 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x034b A[Catch: all -> 0x168e, TRY_ENTER, TRY_LEAVE, TryCatch #66 {all -> 0x168e, blocks: (B:60:0x02bb, B:67:0x031f, B:77:0x036e, B:82:0x03bb, B:72:0x034b, B:76:0x035f), top: B:810:0x02bb }] */
    /* JADX WARN: Removed duplicated region for block: B:744:0x13f1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:768:0x01e4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:804:0x0279 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x03ed  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x03fa  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0422  */
    /* JADX WARN: Type inference failed for: r0v34, types: [android.os.IBinder, com.android.server.TelephonyRegistry] */
    /* JADX WARN: Type inference failed for: r0v529, types: [android.os.IBinder, com.android.server.statusbar.StatusBarManagerService] */
    /* JADX WARN: Type inference failed for: r0v79, types: [android.os.IBinder, com.android.server.input.InputManagerService] */
    /* JADX WARN: Type inference failed for: r0v91, types: [android.os.IBinder, com.android.server.wm.WindowManagerService] */
    /* JADX WARN: Type inference failed for: r0v96, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r10v5, types: [com.android.server.net.NetworkPolicyManagerService] */
    /* JADX WARN: Type inference failed for: r11v10, types: [com.android.server.VpnManagerService] */
    /* JADX WARN: Type inference failed for: r12v22, types: [com.android.server.VcnManagementService] */
    /* JADX WARN: Type inference failed for: r14v10, types: [com.android.server.CountryDetectorService] */
    /* JADX WARN: Type inference failed for: r18v7, types: [com.android.server.media.MediaRouterService] */
    /* JADX WARN: Type inference failed for: r2v170 */
    /* JADX WARN: Type inference failed for: r2v172 */
    /* JADX WARN: Type inference failed for: r2v173 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v280 */
    /* JADX WARN: Type inference failed for: r35v1 */
    /* JADX WARN: Type inference failed for: r37v14 */
    /* JADX WARN: Type inference failed for: r37v15 */
    /* JADX WARN: Type inference failed for: r37v16 */
    /* JADX WARN: Type inference failed for: r37v17 */
    /* JADX WARN: Type inference failed for: r37v18 */
    /* JADX WARN: Type inference failed for: r37v19 */
    /* JADX WARN: Type inference failed for: r37v26 */
    /* JADX WARN: Type inference failed for: r37v27 */
    /* JADX WARN: Type inference failed for: r41v0 */
    /* JADX WARN: Type inference failed for: r41v1 */
    /* JADX WARN: Type inference failed for: r41v2 */
    /* JADX WARN: Type inference failed for: r42v0 */
    /* JADX WARN: Type inference failed for: r42v1 */
    /* JADX WARN: Type inference failed for: r42v2 */
    /* JADX WARN: Type inference failed for: r43v0 */
    /* JADX WARN: Type inference failed for: r43v1 */
    /* JADX WARN: Type inference failed for: r43v2 */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v12 */
    /* JADX WARN: Type inference failed for: r4v17, types: [android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r4v29 */
    /* JADX WARN: Type inference failed for: r4v30 */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v3, types: [android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r5v6 */
    /* JADX WARN: Type inference failed for: r5v7 */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v10 */
    /* JADX WARN: Type inference failed for: r8v2, types: [com.android.server.net.NetworkPolicyManagerService] */
    /* JADX WARN: Type inference failed for: r8v3, types: [android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r8v9 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void startOtherServices(final com.android.server.utils.TimingsTraceAndSlog r61) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 5915
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemServer.startOtherServices(com.android.server.utils.TimingsTraceAndSlog):void");
    }

    static /* synthetic */ void lambda$startOtherServices$1() {
        try {
            android.util.Slog.i(TAG, "SecondaryZygotePreload");
            com.android.server.utils.TimingsTraceAndSlog traceLog = com.android.server.utils.TimingsTraceAndSlog.newAsyncLog();
            traceLog.traceBegin("SecondaryZygotePreload");
            java.lang.String[] abis32 = android.os.Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP ? android.os.Build.QCOM_TANGO_SUPPORTED_32_BIT_ABIS : android.os.Build.SUPPORTED_32_BIT_ABIS;
            if (abis32.length > 0 && !android.os.Build.MTK_HBT_ON_64BIT_ONLY_CHIP && !android.os.Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP && !android.os.Build.OPLUS_OTRANSLATOR_64BIT_ONLY_CHIP && !android.os.Process.ZYGOTE_PROCESS.preloadDefault(abis32[0])) {
                android.util.Slog.e(TAG, "Unable to preload default resources for secondary");
            }
            if (android.os.Process.ZYGOTE_PROCESS.getZygoteProcessExt().isZygoteOcompEnable() && !android.os.Process.ZYGOTE_PROCESS.preloadDefaultOcomp()) {
                android.util.Slog.e(TAG, "Unable to preload default resources for zygote_ocomp");
            }
            traceLog.traceEnd();
        } catch (java.lang.Exception ex) {
            android.util.Slog.e(TAG, "Exception preloading default resources", ex);
        }
    }

    static /* synthetic */ void lambda$startOtherServices$2() {
        com.android.server.utils.TimingsTraceAndSlog traceLog = com.android.server.utils.TimingsTraceAndSlog.newAsyncLog();
        traceLog.traceBegin(START_SENSOR_MANAGER_SERVICE);
        startISensorManagerService();
        traceLog.traceEnd();
    }

    static /* synthetic */ void lambda$startOtherServices$3() {
        com.android.server.utils.TimingsTraceAndSlog traceLog = com.android.server.utils.TimingsTraceAndSlog.newAsyncLog();
        traceLog.traceBegin(START_HIDL_SERVICES);
        startHidlServices();
        traceLog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOtherServices$6(com.android.server.utils.TimingsTraceAndSlog t, com.android.server.devicepolicy.DevicePolicyManagerService.Lifecycle dpms, boolean isWatch, android.content.Context context, boolean safeMode, android.net.ConnectivityManager connectivityF, com.android.server.net.NetworkManagementService networkManagementF, com.android.server.net.NetworkPolicyManagerService networkPolicyF, com.android.server.VpnManagerService vpnManagerF, com.android.server.VcnManagementService vcnManagementF, com.android.server.HsumBootUserInitializer hsumBootUserInitializer, com.android.server.CountryDetectorService countryDetectorF, com.android.server.timedetector.NetworkTimeUpdateService networkTimeUpdaterF, com.android.server.input.InputManagerService inputManagerF, com.android.server.TelephonyRegistry telephonyRegistryF, com.android.server.media.MediaRouterService mediaRouterF, com.android.server.MmsServiceBroker mmsServiceF) {
        java.util.concurrent.Future<?> webviewPrep;
        java.util.concurrent.CountDownLatch networkPolicyInitReadySignal;
        android.util.Slog.i(TAG, "Making services ready");
        t.traceBegin("StartActivityManagerReadyPhase");
        this.mSystemServiceManager.startBootPhase(t, 550);
        t.traceEnd();
        this.mSystemServerExt.systemRunning();
        t.traceBegin("StartObservingNativeCrashes");
        try {
            this.mActivityManagerService.startObservingNativeCrashes();
        } catch (java.lang.Throwable e) {
            reportWtf("observing native crashes", e);
        }
        t.traceEnd();
        t.traceBegin("RegisterAppOpsPolicy");
        try {
            this.mActivityManagerService.setAppOpsPolicy(new com.android.server.policy.AppOpsPolicy(this.mSystemContext));
        } catch (java.lang.Throwable e2) {
            reportWtf("registering app ops policy", e2);
        }
        t.traceEnd();
        if (this.mWebViewUpdateService != null) {
            java.util.concurrent.Future<?> webviewPrep2 = com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startOtherServices$4();
                }
            }, "WebViewFactoryPreparation");
            webviewPrep = webviewPrep2;
        } else {
            webviewPrep = null;
        }
        boolean isAutomotive = this.mPackageManager.hasSystemFeature("android.hardware.type.automotive");
        if (isAutomotive) {
            t.traceBegin("StartCarServiceHelperService");
            android.app.admin.DevicePolicySafetyChecker devicePolicySafetyCheckerStartService = this.mSystemServiceManager.startService(CAR_SERVICE_HELPER_SERVICE_CLASS);
            if (devicePolicySafetyCheckerStartService instanceof android.util.Dumpable) {
                this.mDumper.addDumpable((android.util.Dumpable) devicePolicySafetyCheckerStartService);
            }
            if (devicePolicySafetyCheckerStartService instanceof android.app.admin.DevicePolicySafetyChecker) {
                dpms.setDevicePolicySafetyChecker(devicePolicySafetyCheckerStartService);
            }
            t.traceEnd();
        }
        if (isWatch) {
            t.traceBegin("StartWearService");
            java.lang.String wearServiceComponentNameString = context.getString(android.R.string.config_wearableAmbientContextPackageNameExtraKey);
            if (!android.text.TextUtils.isEmpty(wearServiceComponentNameString)) {
                android.content.ComponentName wearServiceComponentName = android.content.ComponentName.unflattenFromString(wearServiceComponentNameString);
                if (wearServiceComponentName != null) {
                    android.content.Intent intent = new android.content.Intent();
                    intent.setComponent(wearServiceComponentName);
                    intent.addFlags(256);
                    context.startServiceAsUser(intent, android.os.UserHandle.SYSTEM);
                } else {
                    android.util.Slog.d(TAG, "Null wear service component name.");
                }
            }
            t.traceEnd();
        }
        if (safeMode) {
            t.traceBegin("EnableAirplaneModeInSafeMode");
            try {
                connectivityF.setAirplaneMode(true);
            } catch (java.lang.Throwable e3) {
                reportWtf("enabling Airplane Mode during Safe Mode bootup", e3);
            }
            t.traceEnd();
        }
        t.traceBegin("MakeNetworkManagementServiceReady");
        if (networkManagementF != null) {
            try {
                networkManagementF.systemReady();
            } catch (java.lang.Throwable e4) {
                reportWtf("making Network Managment Service ready", e4);
            }
        }
        if (networkPolicyF == null) {
            networkPolicyInitReadySignal = null;
        } else {
            java.util.concurrent.CountDownLatch networkPolicyInitReadySignal2 = networkPolicyF.networkScoreAndNetworkManagementServiceReady();
            networkPolicyInitReadySignal = networkPolicyInitReadySignal2;
        }
        t.traceEnd();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:NetworkStatsService systemReady");
        t.traceBegin("MakeConnectivityServiceReady");
        if (connectivityF != null) {
            try {
                connectivityF.systemReady();
            } catch (java.lang.Throwable e5) {
                reportWtf("making Connectivity Service ready", e5);
            }
        }
        t.traceEnd();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:ConnectivityService systemReady");
        t.traceBegin("MakeVpnManagerServiceReady");
        if (vpnManagerF != null) {
            try {
                vpnManagerF.systemReady();
            } catch (java.lang.Throwable e6) {
                reportWtf("making VpnManagerService ready", e6);
            }
        }
        t.traceEnd();
        t.traceBegin("MakeVcnManagementServiceReady");
        if (vcnManagementF != null) {
            try {
                vcnManagementF.systemReady();
            } catch (java.lang.Throwable e7) {
                reportWtf("making VcnManagementService ready", e7);
            }
        }
        t.traceEnd();
        t.traceBegin("MakeNetworkPolicyServiceReady");
        if (networkPolicyF != null) {
            try {
                networkPolicyF.systemReady(networkPolicyInitReadySignal);
            } catch (java.lang.Throwable e8) {
                reportWtf("making Network Policy Service ready", e8);
            }
        }
        t.traceEnd();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:NetworkPolicyManagerServ systemReady");
        this.mPackageManagerService.waitForAppDataPrepared();
        t.traceBegin("PhaseThirdPartyAppsCanStart");
        if (webviewPrep != null) {
            com.android.internal.util.ConcurrentUtils.waitForFutureNoInterrupt(webviewPrep, "WebViewFactoryPreparation");
        }
        this.mSystemServiceManager.startBootPhase(t, 600);
        t.traceEnd();
        if (hsumBootUserInitializer != null) {
            t.traceBegin("HsumBootUserInitializer.systemRunning");
            hsumBootUserInitializer.systemRunning(t);
            t.traceEnd();
        }
        t.traceBegin("StartNetworkStack");
        try {
            android.net.NetworkStackClient.getInstance().start();
        } catch (java.lang.Throwable e9) {
            reportWtf("starting Network Stack", e9);
        }
        t.traceEnd();
        t.traceBegin("StartTethering");
        try {
            android.net.ConnectivityModuleConnector.getInstance().startModuleService(TETHERING_CONNECTOR_CLASS, "android.permission.MAINLINE_NETWORK_STACK", new android.net.ConnectivityModuleConnector.ModuleServiceCallback() { // from class: com.android.server.SystemServer$$ExternalSyntheticLambda8
                @Override // android.net.ConnectivityModuleConnector.ModuleServiceCallback
                public final void onModuleServiceConnected(android.os.IBinder iBinder) {
                    android.os.ServiceManager.addService("tethering", iBinder, false, 6);
                }
            });
        } catch (java.lang.Throwable e10) {
            reportWtf("starting Tethering", e10);
        }
        t.traceEnd();
        t.traceBegin("MakeCountryDetectionServiceReady");
        if (countryDetectorF != null) {
            try {
                countryDetectorF.systemRunning();
            } catch (java.lang.Throwable e11) {
                reportWtf("Notifying CountryDetectorService running", e11);
            }
        }
        t.traceEnd();
        t.traceBegin("MakeNetworkTimeUpdateReady");
        if (networkTimeUpdaterF != null) {
            try {
                networkTimeUpdaterF.systemRunning();
            } catch (java.lang.Throwable e12) {
                reportWtf("Notifying NetworkTimeService running", e12);
            }
        }
        t.traceEnd();
        t.traceBegin("MakeInputManagerServiceReady");
        if (inputManagerF != null) {
            try {
                inputManagerF.systemRunning();
            } catch (java.lang.Throwable e13) {
                reportWtf("Notifying InputManagerService running", e13);
            }
        }
        t.traceEnd();
        t.traceBegin("MakeTelephonyRegistryReady");
        if (telephonyRegistryF != null) {
            try {
                telephonyRegistryF.systemRunning();
            } catch (java.lang.Throwable e14) {
                reportWtf("Notifying TelephonyRegistry running", e14);
            }
        }
        t.traceEnd();
        t.traceBegin("MakeMediaRouterServiceReady");
        if (mediaRouterF != null) {
            try {
                mediaRouterF.systemRunning();
            } catch (java.lang.Throwable e15) {
                reportWtf("Notifying MediaRouterService running", e15);
            }
        }
        t.traceEnd();
        if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
            t.traceBegin("MakeMmsServiceReady");
            if (mmsServiceF != null) {
                try {
                    mmsServiceF.systemRunning();
                } catch (java.lang.Throwable e16) {
                    reportWtf("Notifying MmsService running", e16);
                }
            }
            t.traceEnd();
        }
        t.traceBegin("IncidentDaemonReady");
        try {
            android.os.IIncidentManager incident = android.os.IIncidentManager.Stub.asInterface(android.os.ServiceManager.getService("incident"));
            if (incident != null) {
                incident.systemRunning();
            }
        } catch (java.lang.Throwable e17) {
            reportWtf("Notifying incident daemon running", e17);
        }
        t.traceEnd();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("SystemServer:PhaseThirdPartyAppsCanStart");
        if (this.mIncrementalServiceHandle != 0) {
            t.traceBegin("MakeIncrementalServiceReady");
            setIncrementalServiceSystemReady(this.mIncrementalServiceHandle);
            t.traceEnd();
        }
        t.traceBegin("OdsignStatsLogger");
        try {
            com.android.server.pm.dex.OdsignStatsLogger.triggerStatsWrite();
        } catch (java.lang.Throwable e18) {
            reportWtf("Triggering OdsignStatsLogger", e18);
        }
        t.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOtherServices$4() {
        android.util.Slog.i(TAG, "WebViewFactoryPreparation");
        com.android.server.utils.TimingsTraceAndSlog traceLog = com.android.server.utils.TimingsTraceAndSlog.newAsyncLog();
        traceLog.traceBegin("WebViewFactoryPreparation");
        com.android.internal.util.ConcurrentUtils.waitForFutureNoInterrupt(this.mZygotePreload, "Zygote preload");
        this.mZygotePreload = null;
        this.mWebViewUpdateService.prepareWebViewInSystemServer();
        traceLog.traceEnd();
    }

    private void startOnDeviceIntelligenceService(com.android.server.utils.TimingsTraceAndSlog t) {
        t.traceBegin("startOnDeviceIntelligenceManagerService");
        this.mSystemServiceManager.startService(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerService.class);
        t.traceEnd();
    }

    private void startApexServices(com.android.server.utils.TimingsTraceAndSlog t) {
        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection() && android.os.Build.IS_DEBUGGABLE && android.os.SystemProperties.getBoolean("debug.crash_system", false)) {
            throw new java.lang.RuntimeException();
        }
        t.traceBegin("startApexServices");
        java.util.List<com.android.server.pm.ApexSystemServiceInfo> services = com.android.server.pm.ApexManager.getInstance().getApexSystemServices();
        for (com.android.server.pm.ApexSystemServiceInfo info : services) {
            java.lang.String name = info.getName();
            java.lang.String jarPath = info.getJarPath();
            t.traceBegin("starting " + name);
            if (android.text.TextUtils.isEmpty(jarPath)) {
                this.mSystemServiceManager.startService(name);
            } else {
                this.mSystemServiceManager.startServiceFromJar(name, jarPath);
            }
            t.traceEnd();
        }
        this.mSystemServiceManager.sealStartedServices();
        t.traceEnd();
    }

    private void updateWatchdogTimeout(com.android.server.utils.TimingsTraceAndSlog t) {
        t.traceBegin("UpdateWatchdogTimeout");
        com.android.server.Watchdog.getInstance().registerSettingsObserver(this.mSystemContext);
        t.traceEnd();
    }

    private boolean deviceHasConfigString(android.content.Context context, int resId) {
        java.lang.String serviceName = context.getString(resId);
        return !android.text.TextUtils.isEmpty(serviceName);
    }

    private void startSystemCaptionsManagerService(android.content.Context context, com.android.server.utils.TimingsTraceAndSlog t) {
        if (!deviceHasConfigString(context, android.R.string.config_deviceSpecificAudioService)) {
            android.util.Slog.d(TAG, "SystemCaptionsManagerService disabled because resource is not overlaid");
            return;
        }
        t.traceBegin("StartSystemCaptionsManagerService");
        this.mSystemServiceManager.startService(com.android.server.systemcaptions.SystemCaptionsManagerService.class);
        t.traceEnd();
    }

    private void startTextToSpeechManagerService(android.content.Context context, com.android.server.utils.TimingsTraceAndSlog t) {
        t.traceBegin("StartTextToSpeechManagerService");
        this.mSystemServiceManager.startService(com.android.server.texttospeech.TextToSpeechManagerService.class);
        t.traceEnd();
    }

    private void startContentCaptureService(android.content.Context context, com.android.server.utils.TimingsTraceAndSlog t) {
        boolean explicitlyEnabled = false;
        java.lang.String settings = android.provider.DeviceConfig.getProperty("content_capture", "service_explicitly_enabled");
        if (settings != null && !settings.equalsIgnoreCase("default")) {
            explicitlyEnabled = java.lang.Boolean.parseBoolean(settings);
            if (explicitlyEnabled) {
                android.util.Slog.d(TAG, "ContentCaptureService explicitly enabled by DeviceConfig");
            } else {
                android.util.Slog.d(TAG, "ContentCaptureService explicitly disabled by DeviceConfig");
                return;
            }
        }
        if (!explicitlyEnabled) {
            if (!deviceHasConfigString(context, android.R.string.config_defaultDndAccessPackages)) {
                android.util.Slog.d(TAG, "ContentCaptureService disabled because resource is not overlaid");
                return;
            } else if (!deviceHasConfigString(context, android.R.string.config_defaultDndDeniedPackages)) {
                android.util.Slog.d(TAG, "ContentProtectionService disabled because resource is not overlaid, ContentCaptureService still enabled");
            }
        }
        t.traceBegin("StartContentCaptureService");
        this.mSystemServiceManager.startService(com.android.server.contentcapture.ContentCaptureManagerService.class);
        com.android.server.contentcapture.ContentCaptureManagerInternal ccmi = (com.android.server.contentcapture.ContentCaptureManagerInternal) com.android.server.LocalServices.getService(com.android.server.contentcapture.ContentCaptureManagerInternal.class);
        if (ccmi != null && this.mActivityManagerService != null) {
            this.mActivityManagerService.setContentCaptureManager(ccmi);
        }
        t.traceEnd();
    }

    private void startAttentionService(android.content.Context context, com.android.server.utils.TimingsTraceAndSlog t) {
        if (!com.android.server.attention.AttentionManagerService.isServiceConfigured(context)) {
            android.util.Slog.d(TAG, "AttentionService is not configured on this device");
            return;
        }
        t.traceBegin("StartAttentionManagerService");
        this.mSystemServiceManager.startService(com.android.server.attention.AttentionManagerService.class);
        t.traceEnd();
    }

    private void startRotationResolverService(android.content.Context context, com.android.server.utils.TimingsTraceAndSlog t) {
        if (!com.android.server.rotationresolver.RotationResolverManagerService.isServiceConfigured(context)) {
            android.util.Slog.d(TAG, "RotationResolverService is not configured on this device");
            return;
        }
        t.traceBegin("StartRotationResolverService");
        this.mSystemServiceManager.startService(com.android.server.rotationresolver.RotationResolverManagerService.class);
        t.traceEnd();
    }

    private void startWearableSensingService(com.android.server.utils.TimingsTraceAndSlog t) {
        t.traceBegin("startWearableSensingService");
        this.mSystemServiceManager.startService(com.android.server.wearable.WearableSensingManagerService.class);
        t.traceEnd();
    }

    private static void startSystemUi(android.content.Context context, com.android.server.wm.WindowManagerService windowManager) {
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(pm.getSystemUiServiceComponent());
        intent.addFlags(256);
        context.startServiceAsUser(intent, android.os.UserHandle.SYSTEM);
        windowManager.onSystemUiStarted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean handleEarlySystemWtf(android.os.IBinder app, java.lang.String tag, boolean system2, android.app.ApplicationErrorReport.ParcelableCrashInfo crashInfo, int immediateCallerPid) {
        int myPid = android.os.Process.myPid();
        com.android.server.am.EventLogTags.writeAmWtf(android.os.UserHandle.getUserId(1000), myPid, "system_server", -1, tag, crashInfo.exceptionMessage);
        com.android.internal.util.FrameworkStatsLog.write(80, 1000, tag, "system_server", myPid, 3);
        synchronized (com.android.server.SystemServer.class) {
            if (sPendingWtfs == null) {
                sPendingWtfs = new java.util.LinkedList<>();
            }
            sPendingWtfs.add(new android.util.Pair<>(tag, crashInfo));
        }
        return false;
    }
}
