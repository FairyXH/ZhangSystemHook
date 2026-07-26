package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class PackageWatchdog {
    private static final java.lang.String ATTR_DURATION = "duration";
    private static final java.lang.String ATTR_EXPLICIT_HEALTH_CHECK_DURATION = "health-check-duration";
    private static final java.lang.String ATTR_MITIGATION_CALLS = "mitigation-calls";
    private static final java.lang.String ATTR_MITIGATION_COUNT = "mitigation-count";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_PASSED_HEALTH_CHECK = "passed-health-check";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final int DB_VERSION = 1;
    static final int DEFAULT_BOOT_LOOP_TRIGGER_COUNT = 5;
    private static final boolean DEFAULT_EXPLICIT_HEALTH_CHECK_ENABLED = true;
    private static final int DEFAULT_MAJOR_USER_IMPACT_LEVEL_THRESHOLD = 71;
    static final int DEFAULT_TRIGGER_FAILURE_COUNT = 5;
    public static final int FAILURE_REASON_APP_CRASH = 3;
    public static final int FAILURE_REASON_APP_NOT_RESPONDING = 4;
    public static final int FAILURE_REASON_BOOT_LOOP = 5;
    public static final int FAILURE_REASON_EXPLICIT_HEALTH_CHECK = 2;
    public static final int FAILURE_REASON_NATIVE_CRASH = 1;
    public static final int FAILURE_REASON_UNKNOWN = 0;
    private static final java.lang.String MAJOR_USER_IMPACT_LEVEL_THRESHOLD = "persist.device_config.configuration.major_user_impact_level_threshold";
    private static final java.lang.String METADATA_FILE = "/metadata/watchdog/mitigation_count.txt";
    private static final java.lang.String MITIGATION_WINDOW_MS = "persist.device_config.configuration.mitigation_window_ms";
    static final java.lang.String PROPERTY_WATCHDOG_EXPLICIT_HEALTH_CHECK_ENABLED = "watchdog_explicit_health_check_enabled";
    static final java.lang.String PROPERTY_WATCHDOG_TRIGGER_DURATION_MILLIS = "watchdog_trigger_failure_duration_millis";
    static final java.lang.String PROPERTY_WATCHDOG_TRIGGER_FAILURE_COUNT = "watchdog_trigger_failure_count";
    private static final java.lang.String TAG = "PackageWatchdog";
    private static final java.lang.String TAG_OBSERVER = "observer";
    private static final java.lang.String TAG_PACKAGE = "package";
    private static final java.lang.String TAG_PACKAGE_WATCHDOG = "package-watchdog";
    private static com.android.server.PackageWatchdog sPackageWatchdog;
    private final android.util.ArrayMap<java.lang.String, com.android.server.PackageWatchdog.ObserverInternal> mAllObservers;
    private final com.android.server.PackageWatchdog.BootThreshold mBootThreshold;
    private final android.net.ConnectivityModuleConnector mConnectivityModuleConnector;
    private final android.content.Context mContext;
    private final com.android.server.ExplicitHealthCheckController mHealthCheckController;
    private boolean mIsHealthCheckEnabled;
    private boolean mIsPackagesReady;
    private long mLastMitigation;
    private final java.lang.Object mLock;
    private final android.os.Handler mLongTaskHandler;
    private long mNumberOfNativeCrashPollsRemaining;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnPropertyChangedListener;
    private final android.util.AtomicFile mPolicyFile;
    private java.util.Set<java.lang.String> mRequestedHealthCheckPackages;
    private final java.lang.Runnable mSaveToFile;
    private final android.os.Handler mShortTaskHandler;
    private final java.lang.Runnable mSyncRequests;
    private boolean mSyncRequired;
    private final java.lang.Runnable mSyncStateWithScheduledReason;
    private final com.android.server.PackageWatchdog.SystemClock mSystemClock;
    private int mTriggerFailureCount;
    private int mTriggerFailureDurationMs;
    private long mUptimeAtLastStateSync;
    private static final long NATIVE_CRASH_POLLING_INTERVAL_MILLIS = java.util.concurrent.TimeUnit.SECONDS.toMillis(30);
    static final int DEFAULT_TRIGGER_FAILURE_DURATION_MS = (int) java.util.concurrent.TimeUnit.MINUTES.toMillis(1);
    static final long DEFAULT_OBSERVING_DURATION_MS = java.util.concurrent.TimeUnit.DAYS.toMillis(2);
    static final long DEFAULT_DEESCALATION_WINDOW_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(1);
    private static final long NUMBER_OF_NATIVE_CRASH_POLLS = 10;
    static final long DEFAULT_BOOT_LOOP_TRIGGER_WINDOW_MS = java.util.concurrent.TimeUnit.MINUTES.toMillis(NUMBER_OF_NATIVE_CRASH_POLLS);
    static final long DEFAULT_MITIGATION_WINDOW_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FailureReasons {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HealthCheckState {
        public static final int ACTIVE = 0;
        public static final int FAILED = 3;
        public static final int INACTIVE = 1;
        public static final int PASSED = 2;
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PackageHealthObserverImpact {
        public static final int USER_IMPACT_LEVEL_0 = 0;
        public static final int USER_IMPACT_LEVEL_10 = 10;
        public static final int USER_IMPACT_LEVEL_100 = 100;
        public static final int USER_IMPACT_LEVEL_20 = 20;
        public static final int USER_IMPACT_LEVEL_30 = 30;
        public static final int USER_IMPACT_LEVEL_40 = 40;
        public static final int USER_IMPACT_LEVEL_50 = 50;
        public static final int USER_IMPACT_LEVEL_70 = 70;
        public static final int USER_IMPACT_LEVEL_71 = 71;
        public static final int USER_IMPACT_LEVEL_75 = 75;
        public static final int USER_IMPACT_LEVEL_80 = 80;
        public static final int USER_IMPACT_LEVEL_90 = 90;
    }

    @java.lang.FunctionalInterface
    interface SystemClock {
        long uptimeMillis();
    }

    private PackageWatchdog(android.content.Context context) {
        this(context, new android.util.AtomicFile(new java.io.File(new java.io.File(android.os.Environment.getDataDirectory(), "system"), "package-watchdog.xml")), new android.os.Handler(android.os.Looper.myLooper()), com.android.internal.os.BackgroundThread.getHandler(), new com.android.server.ExplicitHealthCheckController(context), android.net.ConnectivityModuleConnector.getInstance(), new com.android.server.PackageWatchdog.SystemClock() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda1
            @Override // com.android.server.PackageWatchdog.SystemClock
            public final long uptimeMillis() {
                return android.os.SystemClock.uptimeMillis();
            }
        });
    }

    PackageWatchdog(android.content.Context context, android.util.AtomicFile policyFile, android.os.Handler shortTaskHandler, android.os.Handler longTaskHandler, com.android.server.ExplicitHealthCheckController controller, android.net.ConnectivityModuleConnector connectivityModuleConnector, com.android.server.PackageWatchdog.SystemClock clock) {
        this.mLock = new java.lang.Object();
        this.mAllObservers = new android.util.ArrayMap<>();
        this.mSyncRequests = new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.syncRequests();
            }
        };
        this.mSyncStateWithScheduledReason = new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.syncStateWithScheduledReason();
            }
        };
        this.mSaveToFile = new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.saveToFile();
            }
        };
        this.mOnPropertyChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda13
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.onPropertyChanged(properties);
            }
        };
        this.mRequestedHealthCheckPackages = new android.util.ArraySet();
        this.mIsHealthCheckEnabled = true;
        this.mTriggerFailureDurationMs = DEFAULT_TRIGGER_FAILURE_DURATION_MS;
        this.mTriggerFailureCount = 5;
        this.mSyncRequired = false;
        this.mLastMitigation = -1000000L;
        this.mContext = context;
        this.mPolicyFile = policyFile;
        this.mShortTaskHandler = shortTaskHandler;
        this.mLongTaskHandler = longTaskHandler;
        this.mHealthCheckController = controller;
        this.mConnectivityModuleConnector = connectivityModuleConnector;
        this.mSystemClock = clock;
        this.mNumberOfNativeCrashPollsRemaining = NUMBER_OF_NATIVE_CRASH_POLLS;
        this.mBootThreshold = new com.android.server.PackageWatchdog.BootThreshold(5, DEFAULT_BOOT_LOOP_TRIGGER_WINDOW_MS);
        loadFromFile();
        sPackageWatchdog = this;
    }

    public static com.android.server.PackageWatchdog getInstance(android.content.Context context) {
        com.android.server.PackageWatchdog packageWatchdog;
        synchronized (com.android.server.PackageWatchdog.class) {
            if (sPackageWatchdog == null) {
                new com.android.server.PackageWatchdog(context);
            }
            packageWatchdog = sPackageWatchdog;
        }
        return packageWatchdog;
    }

    public void onPackagesReady() {
        synchronized (this.mLock) {
            this.mIsPackagesReady = true;
            this.mHealthCheckController.setCallbacks(new java.util.function.Consumer() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onPackagesReady$0((java.lang.String) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onPackagesReady$1((java.util.List) obj);
                }
            }, new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.onSyncRequestNotified();
                }
            });
            setPropertyChangedListenerLocked();
            updateConfigs();
            registerConnectivityModuleHealthListener();
        }
    }

    public void registerHealthObserver(com.android.server.PackageWatchdog.PackageHealthObserver observer) {
        synchronized (this.mLock) {
            com.android.server.PackageWatchdog.ObserverInternal internalObserver = this.mAllObservers.get(observer.getName());
            if (internalObserver != null) {
                internalObserver.registeredObserver = observer;
            } else {
                com.android.server.PackageWatchdog.ObserverInternal internalObserver2 = new com.android.server.PackageWatchdog.ObserverInternal(observer.getName(), new java.util.ArrayList());
                internalObserver2.registeredObserver = observer;
                this.mAllObservers.put(observer.getName(), internalObserver2);
                syncState("added new observer");
            }
        }
    }

    public void startObservingHealth(final com.android.server.PackageWatchdog.PackageHealthObserver observer, final java.util.List<java.lang.String> packageNames, long durationMs) {
        if (packageNames.isEmpty()) {
            android.util.Slog.wtf(TAG, "No packages to observe, " + observer.getName());
            return;
        }
        if (durationMs < 1) {
            android.util.Slog.wtf(TAG, "Invalid duration " + durationMs + "ms for observer " + observer.getName() + ". Not observing packages " + packageNames);
            durationMs = DEFAULT_OBSERVING_DURATION_MS;
        }
        final java.util.List<com.android.server.PackageWatchdog.MonitoredPackage> packages = new java.util.ArrayList<>();
        for (int i = 0; i < packageNames.size(); i++) {
            com.android.server.PackageWatchdog.MonitoredPackage pkg = newMonitoredPackage(packageNames.get(i), durationMs, false);
            if (pkg != null) {
                packages.add(pkg);
            } else {
                android.util.Slog.w(TAG, "Failed to create MonitoredPackage for pkg=" + packageNames.get(i));
            }
        }
        if (packages.isEmpty()) {
            return;
        }
        this.mLongTaskHandler.post(new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startObservingHealth$2(observer, packageNames, packages);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startObservingHealth$2(com.android.server.PackageWatchdog.PackageHealthObserver observer, java.util.List packageNames, java.util.List packages) {
        syncState("observing new packages");
        synchronized (this.mLock) {
            com.android.server.PackageWatchdog.ObserverInternal oldObserver = this.mAllObservers.get(observer.getName());
            if (oldObserver == null) {
                android.util.Slog.d(TAG, observer.getName() + " started monitoring health of packages " + packageNames);
                this.mAllObservers.put(observer.getName(), new com.android.server.PackageWatchdog.ObserverInternal(observer.getName(), packages));
            } else {
                android.util.Slog.d(TAG, observer.getName() + " added the following packages to monitor " + packageNames);
                oldObserver.updatePackagesLocked(packages);
            }
        }
        registerHealthObserver(observer);
        syncState("updated observers");
    }

    public void unregisterHealthObserver(final com.android.server.PackageWatchdog.PackageHealthObserver observer) {
        this.mLongTaskHandler.post(new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$unregisterHealthObserver$3(observer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterHealthObserver$3(com.android.server.PackageWatchdog.PackageHealthObserver observer) {
        synchronized (this.mLock) {
            this.mAllObservers.remove(observer.getName());
        }
        syncState("unregistering observer: " + observer.getName());
    }

    public void onPackageFailure(final java.util.List<android.content.pm.VersionedPackage> packages, final int failureReason) {
        if (packages == null) {
            android.util.Slog.w(TAG, "Could not resolve a list of failing packages");
            return;
        }
        synchronized (this.mLock) {
            long now = this.mSystemClock.uptimeMillis();
            if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection() && now >= this.mLastMitigation && now - this.mLastMitigation < getMitigationWindowMs()) {
                android.util.Slog.i(TAG, "Skipping onPackageFailure mitigation");
            } else {
                this.mLongTaskHandler.post(new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onPackageFailure$4(failureReason, packages);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPackageFailure$4(int failureReason, java.util.List packages) {
        int mitigationCount;
        synchronized (this.mLock) {
            if (this.mAllObservers.isEmpty()) {
                return;
            }
            boolean requiresImmediateAction = failureReason == 1 || failureReason == 2;
            if (requiresImmediateAction) {
                handleFailureImmediately(packages, failureReason);
            } else {
                for (int pIndex = 0; pIndex < packages.size(); pIndex++) {
                    android.content.pm.VersionedPackage versionedPackage = (android.content.pm.VersionedPackage) packages.get(pIndex);
                    com.android.server.PackageWatchdog.PackageHealthObserver currentObserverToNotify = null;
                    int currentObserverImpact = Integer.MAX_VALUE;
                    com.android.server.PackageWatchdog.MonitoredPackage currentMonitoredPackage = null;
                    for (int oIndex = 0; oIndex < this.mAllObservers.size(); oIndex++) {
                        com.android.server.PackageWatchdog.ObserverInternal observer = this.mAllObservers.valueAt(oIndex);
                        com.android.server.PackageWatchdog.PackageHealthObserver registeredObserver = observer.registeredObserver;
                        if (registeredObserver != null && observer.onPackageFailureLocked(versionedPackage.getPackageName())) {
                            com.android.server.PackageWatchdog.MonitoredPackage p = observer.getMonitoredPackage(versionedPackage.getPackageName());
                            int mitigationCount2 = 1;
                            if (p != null) {
                                mitigationCount2 = p.getMitigationCountLocked() + 1;
                            }
                            int impact = registeredObserver.onHealthCheckFailed(versionedPackage, failureReason, mitigationCount2);
                            if (impact != 0 && impact < currentObserverImpact) {
                                currentObserverToNotify = registeredObserver;
                                currentObserverImpact = impact;
                                currentMonitoredPackage = p;
                            }
                        }
                    }
                    if (currentObserverToNotify != null) {
                        if (currentMonitoredPackage == null) {
                            mitigationCount = 1;
                        } else {
                            currentMonitoredPackage.noteMitigationCallLocked();
                            int mitigationCount3 = currentMonitoredPackage.getMitigationCountLocked();
                            mitigationCount = mitigationCount3;
                        }
                        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                            int mitigationCount4 = mitigationCount;
                            int mitigationCount5 = currentObserverImpact;
                            maybeExecute(currentObserverToNotify, versionedPackage, failureReason, mitigationCount5, mitigationCount4);
                        } else {
                            currentObserverToNotify.execute(versionedPackage, failureReason, mitigationCount);
                        }
                    }
                }
            }
        }
    }

    private void handleFailureImmediately(java.util.List<android.content.pm.VersionedPackage> packages, int failureReason) {
        int impact;
        android.content.pm.VersionedPackage failingPackage = packages.size() > 0 ? packages.get(0) : null;
        com.android.server.PackageWatchdog.PackageHealthObserver currentObserverToNotify = null;
        int currentObserverImpact = Integer.MAX_VALUE;
        for (com.android.server.PackageWatchdog.ObserverInternal observer : this.mAllObservers.values()) {
            com.android.server.PackageWatchdog.PackageHealthObserver registeredObserver = observer.registeredObserver;
            if (registeredObserver != null && (impact = registeredObserver.onHealthCheckFailed(failingPackage, failureReason, 1)) != 0 && impact < currentObserverImpact) {
                currentObserverToNotify = registeredObserver;
                currentObserverImpact = impact;
            }
        }
        if (currentObserverToNotify != null) {
            if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                maybeExecute(currentObserverToNotify, failingPackage, failureReason, currentObserverImpact, 1);
            } else {
                currentObserverToNotify.execute(failingPackage, failureReason, 1);
            }
        }
    }

    private void maybeExecute(com.android.server.PackageWatchdog.PackageHealthObserver currentObserverToNotify, android.content.pm.VersionedPackage versionedPackage, int failureReason, int currentObserverImpact, int mitigationCount) {
        if (currentObserverImpact < getUserImpactLevelLimit()) {
            synchronized (this.mLock) {
                this.mLastMitigation = this.mSystemClock.uptimeMillis();
            }
            currentObserverToNotify.execute(versionedPackage, failureReason, mitigationCount);
        }
    }

    private long getMitigationWindowMs() {
        return android.os.SystemProperties.getLong(MITIGATION_WINDOW_MS, DEFAULT_MITIGATION_WINDOW_MS);
    }

    public void noteBoot() {
        int impact;
        synchronized (this.mLock) {
            boolean mitigate = this.mBootThreshold.incrementAndTest();
            if (mitigate) {
                if (!com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                    this.mBootThreshold.reset();
                }
                int mitigationCount = this.mBootThreshold.getMitigationCount() + 1;
                com.android.server.PackageWatchdog.PackageHealthObserver currentObserverToNotify = null;
                com.android.server.PackageWatchdog.ObserverInternal currentObserverInternal = null;
                int currentObserverImpact = Integer.MAX_VALUE;
                for (int i = 0; i < this.mAllObservers.size(); i++) {
                    com.android.server.PackageWatchdog.ObserverInternal observer = this.mAllObservers.valueAt(i);
                    com.android.server.PackageWatchdog.PackageHealthObserver registeredObserver = observer.registeredObserver;
                    if (registeredObserver != null) {
                        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                            impact = registeredObserver.onBootLoop(observer.getBootMitigationCount() + 1);
                        } else {
                            impact = registeredObserver.onBootLoop(mitigationCount);
                        }
                        if (impact != 0 && impact < currentObserverImpact) {
                            currentObserverToNotify = registeredObserver;
                            currentObserverInternal = observer;
                            currentObserverImpact = impact;
                        }
                    }
                }
                if (currentObserverToNotify != null) {
                    if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                        int currentObserverMitigationCount = currentObserverInternal.getBootMitigationCount() + 1;
                        currentObserverInternal.setBootMitigationCount(currentObserverMitigationCount);
                        saveAllObserversBootMitigationCountToMetadata(METADATA_FILE);
                        currentObserverToNotify.executeBootLoopMitigation(currentObserverMitigationCount);
                    } else {
                        this.mBootThreshold.setMitigationCount(mitigationCount);
                        this.mBootThreshold.saveMitigationCountToMetadata();
                        currentObserverToNotify.executeBootLoopMitigation(mitigationCount);
                    }
                }
            }
        }
    }

    public void writeNow() {
        synchronized (this.mLock) {
            if (!this.mAllObservers.isEmpty()) {
                this.mLongTaskHandler.removeCallbacks(this.mSaveToFile);
                pruneObserversLocked();
                saveToFile();
                android.util.Slog.i(TAG, "Last write to update package durations");
            }
        }
    }

    private void setExplicitHealthCheckEnabled(boolean enabled) {
        synchronized (this.mLock) {
            this.mIsHealthCheckEnabled = enabled;
            this.mHealthCheckController.setEnabled(enabled);
            this.mSyncRequired = true;
            syncState("health check state " + (enabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: checkAndMitigateNativeCrashes, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$scheduleCheckAndMitigateNativeCrashes$6() {
        this.mNumberOfNativeCrashPollsRemaining--;
        if ("1".equals(android.os.SystemProperties.get("sys.init.updatable_crashing"))) {
            onPackageFailure(java.util.Collections.EMPTY_LIST, 1);
        } else if (this.mNumberOfNativeCrashPollsRemaining > 0) {
            this.mShortTaskHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$checkAndMitigateNativeCrashes$5();
                }
            }, NATIVE_CRASH_POLLING_INTERVAL_MILLIS);
        }
    }

    public void scheduleCheckAndMitigateNativeCrashes() {
        android.util.Slog.i(TAG, "Scheduling " + this.mNumberOfNativeCrashPollsRemaining + " polls to check and mitigate native crashes");
        this.mShortTaskHandler.post(new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleCheckAndMitigateNativeCrashes$6();
            }
        });
    }

    private int getUserImpactLevelLimit() {
        return android.os.SystemProperties.getInt(MAJOR_USER_IMPACT_LEVEL_THRESHOLD, 71);
    }

    public interface PackageHealthObserver {
        boolean execute(android.content.pm.VersionedPackage versionedPackage, int i, int i2);

        java.lang.String getName();

        int onHealthCheckFailed(android.content.pm.VersionedPackage versionedPackage, int i, int i2);

        default int onBootLoop(int mitigationCount) {
            return 0;
        }

        default boolean executeBootLoopMitigation(int mitigationCount) {
            return false;
        }

        default boolean isPersistent() {
            return false;
        }

        default boolean mayObservePackage(java.lang.String packageName) {
            return false;
        }
    }

    long getTriggerFailureCount() {
        long j;
        synchronized (this.mLock) {
            j = this.mTriggerFailureCount;
        }
        return j;
    }

    long getTriggerFailureDurationMs() {
        long j;
        synchronized (this.mLock) {
            j = this.mTriggerFailureDurationMs;
        }
        return j;
    }

    private void syncRequestsAsync() {
        this.mShortTaskHandler.removeCallbacks(this.mSyncRequests);
        this.mShortTaskHandler.post(this.mSyncRequests);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncRequests() {
        boolean syncRequired = false;
        synchronized (this.mLock) {
            if (this.mIsPackagesReady) {
                java.util.Set<java.lang.String> packages = getPackagesPendingHealthChecksLocked();
                if (this.mSyncRequired || !packages.equals(this.mRequestedHealthCheckPackages) || packages.isEmpty()) {
                    syncRequired = true;
                    this.mRequestedHealthCheckPackages = packages;
                }
            }
        }
        if (syncRequired) {
            android.util.Slog.i(TAG, "Syncing health check requests for packages: " + this.mRequestedHealthCheckPackages);
            this.mHealthCheckController.syncRequests(this.mRequestedHealthCheckPackages);
            this.mSyncRequired = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onHealthCheckPassed, reason: merged with bridge method [inline-methods] */
    public void lambda$onPackagesReady$0(java.lang.String packageName) {
        android.util.Slog.i(TAG, "Health check passed for package: " + packageName);
        boolean isStateChanged = false;
        synchronized (this.mLock) {
            for (int observerIdx = 0; observerIdx < this.mAllObservers.size(); observerIdx++) {
                com.android.server.PackageWatchdog.ObserverInternal observer = this.mAllObservers.valueAt(observerIdx);
                com.android.server.PackageWatchdog.MonitoredPackage monitoredPackage = observer.getMonitoredPackage(packageName);
                if (monitoredPackage != null) {
                    int oldState = monitoredPackage.getHealthCheckStateLocked();
                    int newState = monitoredPackage.tryPassHealthCheckLocked();
                    isStateChanged |= oldState != newState;
                }
            }
        }
        if (isStateChanged) {
            syncState("health check passed for " + packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onSupportedPackages, reason: merged with bridge method [inline-methods] */
    public void lambda$onPackagesReady$1(java.util.List<android.service.watchdog.ExplicitHealthCheckService.PackageConfig> supportedPackages) {
        int newState;
        boolean isStateChanged = false;
        java.util.Map<java.lang.String, java.lang.Long> supportedPackageTimeouts = new android.util.ArrayMap<>();
        for (android.service.watchdog.ExplicitHealthCheckService.PackageConfig info : supportedPackages) {
            supportedPackageTimeouts.put(info.getPackageName(), java.lang.Long.valueOf(info.getHealthCheckTimeoutMillis()));
        }
        synchronized (this.mLock) {
            android.util.Slog.d(TAG, "Received supported packages " + supportedPackages);
            java.util.Iterator<com.android.server.PackageWatchdog.ObserverInternal> oit = this.mAllObservers.values().iterator();
            while (oit.hasNext()) {
                for (com.android.server.PackageWatchdog.MonitoredPackage monitoredPackage : oit.next().getMonitoredPackages().values()) {
                    java.lang.String packageName = monitoredPackage.getName();
                    int oldState = monitoredPackage.getHealthCheckStateLocked();
                    if (supportedPackageTimeouts.containsKey(packageName)) {
                        newState = monitoredPackage.setHealthCheckActiveLocked(supportedPackageTimeouts.get(packageName).longValue());
                    } else {
                        newState = monitoredPackage.tryPassHealthCheckLocked();
                    }
                    isStateChanged |= oldState != newState;
                }
            }
        }
        if (isStateChanged) {
            syncState("updated health check supported packages " + supportedPackages);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSyncRequestNotified() {
        synchronized (this.mLock) {
            this.mSyncRequired = true;
            syncRequestsAsync();
        }
    }

    private java.util.Set<java.lang.String> getPackagesPendingHealthChecksLocked() {
        java.util.Set<java.lang.String> packages = new android.util.ArraySet<>();
        for (com.android.server.PackageWatchdog.ObserverInternal observer : this.mAllObservers.values()) {
            for (com.android.server.PackageWatchdog.MonitoredPackage monitoredPackage : observer.getMonitoredPackages().values()) {
                java.lang.String packageName = monitoredPackage.getName();
                if (monitoredPackage.isPendingHealthChecksLocked()) {
                    packages.add(packageName);
                }
            }
        }
        return packages;
    }

    private void syncState(java.lang.String reason) {
        synchronized (this.mLock) {
            android.util.Slog.i(TAG, "Syncing state, reason: " + reason);
            pruneObserversLocked();
            saveToFileAsync();
            syncRequestsAsync();
            scheduleNextSyncStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncStateWithScheduledReason() {
        syncState("scheduled");
    }

    private void scheduleNextSyncStateLocked() {
        long durationMs = getNextStateSyncMillisLocked();
        this.mShortTaskHandler.removeCallbacks(this.mSyncStateWithScheduledReason);
        if (durationMs == Long.MAX_VALUE) {
            android.util.Slog.i(TAG, "Cancelling state sync, nothing to sync");
            this.mUptimeAtLastStateSync = 0L;
        } else {
            this.mUptimeAtLastStateSync = this.mSystemClock.uptimeMillis();
            this.mShortTaskHandler.postDelayed(this.mSyncStateWithScheduledReason, durationMs);
        }
    }

    private long getNextStateSyncMillisLocked() {
        long shortestDurationMs = Long.MAX_VALUE;
        for (int oIndex = 0; oIndex < this.mAllObservers.size(); oIndex++) {
            android.util.ArrayMap<java.lang.String, com.android.server.PackageWatchdog.MonitoredPackage> packages = this.mAllObservers.valueAt(oIndex).getMonitoredPackages();
            for (int pIndex = 0; pIndex < packages.size(); pIndex++) {
                com.android.server.PackageWatchdog.MonitoredPackage mp = packages.valueAt(pIndex);
                long duration = mp.getShortestScheduleDurationMsLocked();
                if (duration < shortestDurationMs) {
                    shortestDurationMs = duration;
                }
            }
        }
        return shortestDurationMs;
    }

    private void pruneObserversLocked() {
        long elapsedMs = this.mUptimeAtLastStateSync == 0 ? 0L : this.mSystemClock.uptimeMillis() - this.mUptimeAtLastStateSync;
        if (elapsedMs <= 0) {
            android.util.Slog.i(TAG, "Not pruning observers, elapsed time: " + elapsedMs + "ms");
            return;
        }
        java.util.Iterator<com.android.server.PackageWatchdog.ObserverInternal> it = this.mAllObservers.values().iterator();
        while (it.hasNext()) {
            com.android.server.PackageWatchdog.ObserverInternal observer = it.next();
            java.util.Set<com.android.server.PackageWatchdog.MonitoredPackage> failedPackages = observer.prunePackagesLocked(elapsedMs);
            if (!failedPackages.isEmpty()) {
                onHealthCheckFailed(observer, failedPackages);
            }
            if (observer.getMonitoredPackages().isEmpty() && (observer.registeredObserver == null || !observer.registeredObserver.isPersistent())) {
                android.util.Slog.i(TAG, "Discarding observer " + observer.name + ". All packages expired");
                it.remove();
            }
        }
    }

    private void onHealthCheckFailed(final com.android.server.PackageWatchdog.ObserverInternal observer, final java.util.Set<com.android.server.PackageWatchdog.MonitoredPackage> failedPackages) {
        this.mLongTaskHandler.post(new java.lang.Runnable() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onHealthCheckFailed$7(observer, failedPackages);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHealthCheckFailed$7(com.android.server.PackageWatchdog.ObserverInternal observer, java.util.Set failedPackages) {
        synchronized (this.mLock) {
            com.android.server.PackageWatchdog.PackageHealthObserver registeredObserver = observer.registeredObserver;
            if (registeredObserver != null) {
                java.util.Iterator<com.android.server.PackageWatchdog.MonitoredPackage> it = failedPackages.iterator();
                while (it.hasNext()) {
                    android.content.pm.VersionedPackage versionedPkg = getVersionedPackage(it.next().getName());
                    if (versionedPkg != null) {
                        android.util.Slog.i(TAG, "Explicit health check failed for package " + versionedPkg);
                        registeredObserver.execute(versionedPkg, 2, 1);
                    }
                }
            }
        }
    }

    private android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, 4194304);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return pm.getPackageInfo(packageName, 1073741824);
        }
    }

    private android.content.pm.VersionedPackage getVersionedPackage(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm == null || android.text.TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            long versionCode = getPackageInfo(packageName).getLongVersionCode();
            return new android.content.pm.VersionedPackage(packageName, versionCode);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private void loadFromFile() {
        java.io.InputStream infile = null;
        this.mAllObservers.clear();
        try {
            try {
                infile = this.mPolicyFile.openRead();
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(infile);
                com.android.internal.util.XmlUtils.beginDocument(parser, TAG_PACKAGE_WATCHDOG);
                int outerDepth = parser.getDepth();
                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                    com.android.server.PackageWatchdog.ObserverInternal observer = com.android.server.PackageWatchdog.ObserverInternal.read(parser, this);
                    if (observer != null) {
                        this.mAllObservers.put(observer.name, observer);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
            } catch (java.io.IOException | java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e2) {
                android.util.Slog.wtf(TAG, "Unable to read monitored packages, deleting file", e2);
                this.mPolicyFile.delete();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(infile);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPropertyChanged(android.provider.DeviceConfig.Properties properties) {
        try {
            updateConfigs();
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Failed to reload device config changes");
        }
    }

    private void setPropertyChangedListenerLocked() {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("rollback", this.mContext.getMainExecutor(), this.mOnPropertyChangedListener);
    }

    void removePropertyChangedListener() {
        android.provider.DeviceConfig.removeOnPropertiesChangedListener(this.mOnPropertyChangedListener);
    }

    void updateConfigs() {
        synchronized (this.mLock) {
            this.mTriggerFailureCount = android.provider.DeviceConfig.getInt("rollback", PROPERTY_WATCHDOG_TRIGGER_FAILURE_COUNT, 5);
            if (this.mTriggerFailureCount <= 0) {
                this.mTriggerFailureCount = 5;
            }
            this.mTriggerFailureDurationMs = android.provider.DeviceConfig.getInt("rollback", PROPERTY_WATCHDOG_TRIGGER_DURATION_MILLIS, DEFAULT_TRIGGER_FAILURE_DURATION_MS);
            if (this.mTriggerFailureDurationMs <= 0) {
                this.mTriggerFailureDurationMs = DEFAULT_TRIGGER_FAILURE_DURATION_MS;
            }
            setExplicitHealthCheckEnabled(android.provider.DeviceConfig.getBoolean("rollback", PROPERTY_WATCHDOG_EXPLICIT_HEALTH_CHECK_ENABLED, true));
        }
    }

    private void registerConnectivityModuleHealthListener() {
        this.mConnectivityModuleConnector.registerHealthListener(new android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener() { // from class: com.android.server.PackageWatchdog$$ExternalSyntheticLambda6
            @Override // android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener
            public final void onNetworkStackFailure(java.lang.String str) {
                this.f$0.lambda$registerConnectivityModuleHealthListener$8(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerConnectivityModuleHealthListener$8(java.lang.String packageName) {
        android.content.pm.VersionedPackage pkg = getVersionedPackage(packageName);
        if (pkg == null) {
            android.util.Slog.wtf(TAG, "NetworkStack failed but could not find its package");
        } else {
            java.util.List<android.content.pm.VersionedPackage> pkgList = java.util.Collections.singletonList(pkg);
            onPackageFailure(pkgList, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean saveToFile() {
        android.util.Slog.i(TAG, "Saving observer state to file");
        synchronized (this.mLock) {
            try {
                try {
                    java.io.FileOutputStream stream = this.mPolicyFile.startWrite();
                    try {
                        try {
                            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
                            out.startDocument((java.lang.String) null, true);
                            out.startTag((java.lang.String) null, TAG_PACKAGE_WATCHDOG);
                            out.attributeInt((java.lang.String) null, ATTR_VERSION, 1);
                            for (int oIndex = 0; oIndex < this.mAllObservers.size(); oIndex++) {
                                this.mAllObservers.valueAt(oIndex).writeLocked(out);
                            }
                            out.endTag((java.lang.String) null, TAG_PACKAGE_WATCHDOG);
                            out.endDocument();
                            this.mPolicyFile.finishWrite(stream);
                        } catch (java.io.IOException e) {
                            android.util.Slog.w(TAG, "Failed to save monitored packages, restoring backup", e);
                            this.mPolicyFile.failWrite(stream);
                            return false;
                        }
                    } finally {
                        libcore.io.IoUtils.closeQuietly(stream);
                    }
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(TAG, "Cannot update monitored packages", e2);
                    return false;
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return true;
    }

    private void saveToFileAsync() {
        if (!this.mLongTaskHandler.hasCallbacks(this.mSaveToFile)) {
            this.mLongTaskHandler.post(this.mSaveToFile);
        }
    }

    public static java.lang.String longArrayQueueToString(android.util.LongArrayQueue queue) {
        if (queue.size() > 0) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(queue.get(0));
            for (int i = 1; i < queue.size(); i++) {
                sb.append(",");
                sb.append(queue.get(i));
            }
            return sb.toString();
        }
        return "";
    }

    public static android.util.LongArrayQueue parseLongArrayQueue(java.lang.String commaSeparatedValues) {
        android.util.LongArrayQueue result = new android.util.LongArrayQueue();
        if (!android.text.TextUtils.isEmpty(commaSeparatedValues)) {
            java.lang.String[] values = commaSeparatedValues.split(",");
            for (java.lang.String value : values) {
                result.addLast(java.lang.Long.parseLong(value));
            }
        }
        return result;
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("Package Watchdog status");
        pw.increaseIndent();
        synchronized (this.mLock) {
            for (java.lang.String observerName : this.mAllObservers.keySet()) {
                pw.println("Observer name: " + observerName);
                pw.increaseIndent();
                com.android.server.PackageWatchdog.ObserverInternal observerInternal = this.mAllObservers.get(observerName);
                observerInternal.dump(pw);
                pw.decreaseIndent();
            }
        }
    }

    void registerObserverInternal(com.android.server.PackageWatchdog.ObserverInternal observerInternal) {
        this.mAllObservers.put(observerInternal.name, observerInternal);
    }

    static class ObserverInternal {
        private int mMitigationCount;
        private final android.util.ArrayMap<java.lang.String, com.android.server.PackageWatchdog.MonitoredPackage> mPackages;
        public final java.lang.String name;
        public com.android.server.PackageWatchdog.PackageHealthObserver registeredObserver;

        ObserverInternal(java.lang.String name, java.util.List<com.android.server.PackageWatchdog.MonitoredPackage> packages) {
            this(name, packages, 0);
        }

        ObserverInternal(java.lang.String name, java.util.List<com.android.server.PackageWatchdog.MonitoredPackage> packages, int mitigationCount) {
            this.mPackages = new android.util.ArrayMap<>();
            this.name = name;
            updatePackagesLocked(packages);
            this.mMitigationCount = mitigationCount;
        }

        public boolean writeLocked(com.android.modules.utils.TypedXmlSerializer out) {
            try {
                out.startTag((java.lang.String) null, com.android.server.PackageWatchdog.TAG_OBSERVER);
                out.attribute((java.lang.String) null, "name", this.name);
                if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                    out.attributeInt((java.lang.String) null, com.android.server.PackageWatchdog.ATTR_MITIGATION_COUNT, this.mMitigationCount);
                }
                for (int i = 0; i < this.mPackages.size(); i++) {
                    com.android.server.PackageWatchdog.MonitoredPackage p = this.mPackages.valueAt(i);
                    p.writeLocked(out);
                }
                out.endTag((java.lang.String) null, com.android.server.PackageWatchdog.TAG_OBSERVER);
                return true;
            } catch (java.io.IOException e) {
                android.util.Slog.w(com.android.server.PackageWatchdog.TAG, "Cannot save observer", e);
                return false;
            }
        }

        public int getBootMitigationCount() {
            return this.mMitigationCount;
        }

        public void setBootMitigationCount(int mitigationCount) {
            this.mMitigationCount = mitigationCount;
        }

        public void updatePackagesLocked(java.util.List<com.android.server.PackageWatchdog.MonitoredPackage> packages) {
            for (int pIndex = 0; pIndex < packages.size(); pIndex++) {
                com.android.server.PackageWatchdog.MonitoredPackage p = packages.get(pIndex);
                com.android.server.PackageWatchdog.MonitoredPackage existingPackage = getMonitoredPackage(p.getName());
                if (existingPackage != null) {
                    existingPackage.updateHealthCheckDuration(p.mDurationMs);
                } else {
                    putMonitoredPackage(p);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.util.Set<com.android.server.PackageWatchdog.MonitoredPackage> prunePackagesLocked(long elapsedMs) {
            java.util.Set<com.android.server.PackageWatchdog.MonitoredPackage> failedPackages = new android.util.ArraySet<>();
            java.util.Iterator<com.android.server.PackageWatchdog.MonitoredPackage> it = this.mPackages.values().iterator();
            while (it.hasNext()) {
                com.android.server.PackageWatchdog.MonitoredPackage p = it.next();
                int oldState = p.getHealthCheckStateLocked();
                int newState = p.handleElapsedTimeLocked(elapsedMs);
                if (oldState != 3 && newState == 3) {
                    android.util.Slog.i(com.android.server.PackageWatchdog.TAG, "Package " + p.getName() + " failed health check");
                    failedPackages.add(p);
                }
                if (p.isExpiredLocked()) {
                    it.remove();
                }
            }
            return failedPackages;
        }

        public boolean onPackageFailureLocked(java.lang.String packageName) {
            if (getMonitoredPackage(packageName) == null && this.registeredObserver.isPersistent() && this.registeredObserver.mayObservePackage(packageName)) {
                putMonitoredPackage(com.android.server.PackageWatchdog.sPackageWatchdog.newMonitoredPackage(packageName, com.android.server.PackageWatchdog.DEFAULT_OBSERVING_DURATION_MS, false));
            }
            com.android.server.PackageWatchdog.MonitoredPackage p = getMonitoredPackage(packageName);
            if (p != null) {
                return p.onFailureLocked();
            }
            return false;
        }

        public android.util.ArrayMap<java.lang.String, com.android.server.PackageWatchdog.MonitoredPackage> getMonitoredPackages() {
            return this.mPackages;
        }

        public com.android.server.PackageWatchdog.MonitoredPackage getMonitoredPackage(java.lang.String packageName) {
            return this.mPackages.get(packageName);
        }

        public void putMonitoredPackage(com.android.server.PackageWatchdog.MonitoredPackage p) {
            this.mPackages.put(p.getName(), p);
        }

        public static com.android.server.PackageWatchdog.ObserverInternal read(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.PackageWatchdog watchdog) {
            java.lang.String observerName = null;
            int observerMitigationCount = 0;
            if (com.android.server.PackageWatchdog.TAG_OBSERVER.equals(parser.getName())) {
                observerName = parser.getAttributeValue((java.lang.String) null, "name");
                if (android.text.TextUtils.isEmpty(observerName)) {
                    android.util.Slog.wtf(com.android.server.PackageWatchdog.TAG, "Unable to read observer name");
                    return null;
                }
            }
            java.util.List<com.android.server.PackageWatchdog.MonitoredPackage> packages = new java.util.ArrayList<>();
            int innerDepth = parser.getDepth();
            try {
                if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                    try {
                        observerMitigationCount = parser.getAttributeInt((java.lang.String) null, com.android.server.PackageWatchdog.ATTR_MITIGATION_COUNT);
                    } catch (org.xmlpull.v1.XmlPullParserException e) {
                        android.util.Slog.i(com.android.server.PackageWatchdog.TAG, "ObserverInternal mitigation count was not present.");
                    }
                }
                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, innerDepth)) {
                    if ("package".equals(parser.getName())) {
                        try {
                            com.android.server.PackageWatchdog.MonitoredPackage pkg = watchdog.parseMonitoredPackage(parser);
                            if (pkg != null) {
                                packages.add(pkg);
                            }
                        } catch (java.lang.NumberFormatException e2) {
                            android.util.Slog.wtf(com.android.server.PackageWatchdog.TAG, "Skipping package for observer " + observerName, e2);
                        }
                    }
                }
                if (packages.isEmpty()) {
                    return null;
                }
                return new com.android.server.PackageWatchdog.ObserverInternal(observerName, packages, observerMitigationCount);
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e3) {
                android.util.Slog.wtf(com.android.server.PackageWatchdog.TAG, "Unable to read observer " + observerName, e3);
                return null;
            }
        }

        public void dump(com.android.internal.util.IndentingPrintWriter pw) {
            boolean isPersistent = this.registeredObserver != null && this.registeredObserver.isPersistent();
            pw.println("Persistent: " + isPersistent);
            for (java.lang.String packageName : this.mPackages.keySet()) {
                com.android.server.PackageWatchdog.MonitoredPackage p = getMonitoredPackage(packageName);
                pw.println(packageName + ": ");
                pw.increaseIndent();
                pw.println("# Failures: " + p.mFailureHistory.size());
                pw.println("Monitoring duration remaining: " + p.mDurationMs + "ms");
                pw.println("Explicit health check duration: " + p.mHealthCheckDurationMs + "ms");
                pw.println("Health check state: " + p.toString(p.mHealthCheckState));
                pw.decreaseIndent();
            }
        }
    }

    com.android.server.PackageWatchdog.MonitoredPackage newMonitoredPackage(java.lang.String name, long durationMs, boolean hasPassedHealthCheck) {
        return newMonitoredPackage(name, durationMs, Long.MAX_VALUE, hasPassedHealthCheck, new android.util.LongArrayQueue());
    }

    com.android.server.PackageWatchdog.MonitoredPackage newMonitoredPackage(java.lang.String name, long durationMs, long healthCheckDurationMs, boolean hasPassedHealthCheck, android.util.LongArrayQueue mitigationCalls) {
        return new com.android.server.PackageWatchdog.MonitoredPackage(name, durationMs, healthCheckDurationMs, hasPassedHealthCheck, mitigationCalls);
    }

    com.android.server.PackageWatchdog.MonitoredPackage parseMonitoredPackage(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
        java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, "name");
        long duration = parser.getAttributeLong((java.lang.String) null, ATTR_DURATION);
        long healthCheckDuration = parser.getAttributeLong((java.lang.String) null, ATTR_EXPLICIT_HEALTH_CHECK_DURATION);
        boolean hasPassedHealthCheck = parser.getAttributeBoolean((java.lang.String) null, ATTR_PASSED_HEALTH_CHECK);
        android.util.LongArrayQueue mitigationCalls = parseLongArrayQueue(parser.getAttributeValue((java.lang.String) null, ATTR_MITIGATION_CALLS));
        return newMonitoredPackage(packageName, duration, healthCheckDuration, hasPassedHealthCheck, mitigationCalls);
    }

    class MonitoredPackage {
        private long mDurationMs;
        private boolean mHasPassedHealthCheck;
        private long mHealthCheckDurationMs;
        private final android.util.LongArrayQueue mMitigationCalls;
        private final java.lang.String mPackageName;
        private final android.util.LongArrayQueue mFailureHistory = new android.util.LongArrayQueue();
        private int mHealthCheckState = 1;

        MonitoredPackage(java.lang.String packageName, long durationMs, long healthCheckDurationMs, boolean hasPassedHealthCheck, android.util.LongArrayQueue mitigationCalls) {
            this.mHealthCheckDurationMs = Long.MAX_VALUE;
            this.mPackageName = packageName;
            this.mDurationMs = durationMs;
            this.mHealthCheckDurationMs = healthCheckDurationMs;
            this.mHasPassedHealthCheck = hasPassedHealthCheck;
            this.mMitigationCalls = mitigationCalls;
            updateHealthCheckStateLocked();
        }

        public void writeLocked(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            out.startTag((java.lang.String) null, "package");
            out.attribute((java.lang.String) null, "name", getName());
            out.attributeLong((java.lang.String) null, com.android.server.PackageWatchdog.ATTR_DURATION, this.mDurationMs);
            out.attributeLong((java.lang.String) null, com.android.server.PackageWatchdog.ATTR_EXPLICIT_HEALTH_CHECK_DURATION, this.mHealthCheckDurationMs);
            out.attributeBoolean((java.lang.String) null, com.android.server.PackageWatchdog.ATTR_PASSED_HEALTH_CHECK, this.mHasPassedHealthCheck);
            android.util.LongArrayQueue normalizedCalls = normalizeMitigationCalls();
            out.attribute((java.lang.String) null, com.android.server.PackageWatchdog.ATTR_MITIGATION_CALLS, com.android.server.PackageWatchdog.longArrayQueueToString(normalizedCalls));
            out.endTag((java.lang.String) null, "package");
        }

        public boolean onFailureLocked() {
            long now = com.android.server.PackageWatchdog.this.mSystemClock.uptimeMillis();
            this.mFailureHistory.addLast(now);
            while (now - this.mFailureHistory.peekFirst() > com.android.server.PackageWatchdog.this.mTriggerFailureDurationMs) {
                this.mFailureHistory.removeFirst();
            }
            boolean failed = this.mFailureHistory.size() >= com.android.server.PackageWatchdog.this.mTriggerFailureCount;
            if (failed) {
                this.mFailureHistory.clear();
            }
            return failed;
        }

        public void noteMitigationCallLocked() {
            this.mMitigationCalls.addLast(com.android.server.PackageWatchdog.this.mSystemClock.uptimeMillis());
        }

        public int getMitigationCountLocked() {
            try {
                long now = com.android.server.PackageWatchdog.this.mSystemClock.uptimeMillis();
                while (now - this.mMitigationCalls.peekFirst() > com.android.server.PackageWatchdog.DEFAULT_DEESCALATION_WINDOW_MS) {
                    this.mMitigationCalls.removeFirst();
                }
            } catch (java.util.NoSuchElementException e) {
            }
            return this.mMitigationCalls.size();
        }

        public android.util.LongArrayQueue normalizeMitigationCalls() {
            android.util.LongArrayQueue normalized = new android.util.LongArrayQueue();
            long now = com.android.server.PackageWatchdog.this.mSystemClock.uptimeMillis();
            for (int i = 0; i < this.mMitigationCalls.size(); i++) {
                normalized.addLast(this.mMitigationCalls.get(i) - now);
            }
            return normalized;
        }

        public int setHealthCheckActiveLocked(long initialHealthCheckDurationMs) {
            if (initialHealthCheckDurationMs <= 0) {
                android.util.Slog.wtf(com.android.server.PackageWatchdog.TAG, "Cannot set non-positive health check duration " + initialHealthCheckDurationMs + "ms for package " + getName() + ". Using total duration " + this.mDurationMs + "ms instead");
                initialHealthCheckDurationMs = this.mDurationMs;
            }
            if (this.mHealthCheckState == 1) {
                this.mHealthCheckDurationMs = initialHealthCheckDurationMs;
            }
            return updateHealthCheckStateLocked();
        }

        public int handleElapsedTimeLocked(long elapsedMs) {
            if (elapsedMs <= 0) {
                android.util.Slog.w(com.android.server.PackageWatchdog.TAG, "Cannot handle non-positive elapsed time for package " + getName());
                return this.mHealthCheckState;
            }
            this.mDurationMs -= elapsedMs;
            if (this.mHealthCheckState == 0) {
                this.mHealthCheckDurationMs -= elapsedMs;
            }
            return updateHealthCheckStateLocked();
        }

        public void updateHealthCheckDuration(long newDurationMs) {
            this.mDurationMs = newDurationMs;
        }

        public int tryPassHealthCheckLocked() {
            if (this.mHealthCheckState != 3) {
                this.mHasPassedHealthCheck = true;
            }
            return updateHealthCheckStateLocked();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getName() {
            return this.mPackageName;
        }

        public int getHealthCheckStateLocked() {
            return this.mHealthCheckState;
        }

        public long getShortestScheduleDurationMsLocked() {
            return java.lang.Math.min(toPositive(this.mDurationMs), isPendingHealthChecksLocked() ? toPositive(this.mHealthCheckDurationMs) : Long.MAX_VALUE);
        }

        public boolean isExpiredLocked() {
            return this.mDurationMs <= 0;
        }

        public boolean isPendingHealthChecksLocked() {
            return this.mHealthCheckState == 0 || this.mHealthCheckState == 1;
        }

        private int updateHealthCheckStateLocked() {
            int oldState = this.mHealthCheckState;
            if (this.mHasPassedHealthCheck) {
                this.mHealthCheckState = 2;
            } else if (this.mHealthCheckDurationMs <= 0 || this.mDurationMs <= 0) {
                this.mHealthCheckState = 3;
            } else if (this.mHealthCheckDurationMs == Long.MAX_VALUE) {
                this.mHealthCheckState = 1;
            } else {
                this.mHealthCheckState = 0;
            }
            if (oldState != this.mHealthCheckState) {
                android.util.Slog.i(com.android.server.PackageWatchdog.TAG, "Updated health check state for package " + getName() + ": " + toString(oldState) + " -> " + toString(this.mHealthCheckState));
            }
            return this.mHealthCheckState;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String toString(int state) {
            switch (state) {
                case 0:
                    return "ACTIVE";
                case 1:
                    return "INACTIVE";
                case 2:
                    return "PASSED";
                case 3:
                    return "FAILED";
                default:
                    return "UNKNOWN";
            }
        }

        private long toPositive(long value) {
            if (value > 0) {
                return value;
            }
            return Long.MAX_VALUE;
        }

        boolean isEqualTo(com.android.server.PackageWatchdog.MonitoredPackage pkg) {
            return getName().equals(pkg.getName()) && this.mDurationMs == pkg.mDurationMs && this.mHasPassedHealthCheck == pkg.mHasPassedHealthCheck && this.mHealthCheckDurationMs == pkg.mHealthCheckDurationMs && this.mMitigationCalls.toString().equals(pkg.mMitigationCalls.toString());
        }
    }

    void saveAllObserversBootMitigationCountToMetadata(java.lang.String filePath) {
        java.util.HashMap<java.lang.String, java.lang.Integer> bootMitigationCounts = new java.util.HashMap<>();
        for (int i = 0; i < this.mAllObservers.size(); i++) {
            com.android.server.PackageWatchdog.ObserverInternal observer = this.mAllObservers.valueAt(i);
            bootMitigationCounts.put(observer.name, java.lang.Integer.valueOf(observer.getBootMitigationCount()));
        }
        try {
            java.io.FileOutputStream fileStream = new java.io.FileOutputStream(new java.io.File(filePath));
            java.io.ObjectOutputStream objectStream = new java.io.ObjectOutputStream(fileStream);
            objectStream.writeObject(bootMitigationCounts);
            objectStream.flush();
            objectStream.close();
            fileStream.close();
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Could not save observers metadata to file: " + e);
        }
    }

    class BootThreshold {
        private final int mBootTriggerCount;
        private final long mTriggerWindow;

        BootThreshold(int bootTriggerCount, long triggerWindow) {
            this.mBootTriggerCount = bootTriggerCount;
            this.mTriggerWindow = triggerWindow;
        }

        public void reset() {
            setStart(0L);
            setCount(0);
        }

        protected int getCount() {
            return ((java.lang.Integer) android.sysprop.CrashRecoveryProperties.rescueBootCount().orElse(0)).intValue();
        }

        protected void setCount(int count) {
            android.sysprop.CrashRecoveryProperties.rescueBootCount(java.lang.Integer.valueOf(count));
        }

        public long getStart() {
            return ((java.lang.Long) android.sysprop.CrashRecoveryProperties.rescueBootStart().orElse(0L)).longValue();
        }

        public int getMitigationCount() {
            return ((java.lang.Integer) android.sysprop.CrashRecoveryProperties.bootMitigationCount().orElse(0)).intValue();
        }

        public void setStart(long start) {
            android.sysprop.CrashRecoveryProperties.rescueBootStart(java.lang.Long.valueOf(getStartTime(start)));
        }

        public void setMitigationStart(long start) {
            android.sysprop.CrashRecoveryProperties.bootMitigationStart(java.lang.Long.valueOf(getStartTime(start)));
        }

        public long getMitigationStart() {
            return ((java.lang.Long) android.sysprop.CrashRecoveryProperties.bootMitigationStart().orElse(0L)).longValue();
        }

        public void setMitigationCount(int count) {
            android.sysprop.CrashRecoveryProperties.bootMitigationCount(java.lang.Integer.valueOf(count));
        }

        private static long constrain(long amount, long low, long high) {
            return amount < low ? low : amount > high ? high : amount;
        }

        public long getStartTime(long start) {
            long now = com.android.server.PackageWatchdog.this.mSystemClock.uptimeMillis();
            return constrain(start, 0L, now);
        }

        public void saveMitigationCountToMetadata() {
            try {
                java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(com.android.server.PackageWatchdog.METADATA_FILE));
                try {
                    writer.write(java.lang.String.valueOf(getMitigationCount()));
                    writer.close();
                } finally {
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.PackageWatchdog.TAG, "Could not save metadata to file: " + e);
            }
        }

        public void readMitigationCountFromMetadataIfNecessary() {
            java.io.File bootPropsFile = new java.io.File(com.android.server.PackageWatchdog.METADATA_FILE);
            if (bootPropsFile.exists()) {
                try {
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(com.android.server.PackageWatchdog.METADATA_FILE));
                    try {
                        java.lang.String mitigationCount = reader.readLine();
                        setMitigationCount(java.lang.Integer.parseInt(mitigationCount));
                        bootPropsFile.delete();
                        reader.close();
                    } finally {
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.i(com.android.server.PackageWatchdog.TAG, "Could not read metadata file: " + e);
                }
            }
        }

        public boolean incrementAndTest() {
            if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                readAllObserversBootMitigationCountIfNecessary(com.android.server.PackageWatchdog.METADATA_FILE);
            } else {
                readMitigationCountFromMetadataIfNecessary();
            }
            long now = com.android.server.PackageWatchdog.this.mSystemClock.uptimeMillis();
            if (now - getStart() < 0) {
                android.util.Slog.e(com.android.server.PackageWatchdog.TAG, "Window was less than zero. Resetting start to current time.");
                setStart(now);
                setMitigationStart(now);
            }
            if (now - getMitigationStart() > com.android.server.PackageWatchdog.DEFAULT_DEESCALATION_WINDOW_MS) {
                setMitigationStart(now);
                if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
                    resetAllObserversBootMitigationCount();
                } else {
                    setMitigationCount(0);
                }
            }
            long window = now - getStart();
            if (window >= this.mTriggerWindow) {
                setCount(1);
                setStart(now);
                return false;
            }
            int count = getCount() + 1;
            setCount(count);
            com.android.server.EventLogTags.writeRescueNote(0, count, window);
            return com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection() ? count >= this.mBootTriggerCount || (performedMitigationsDuringWindow() && count > 1) : count >= this.mBootTriggerCount;
        }

        private boolean performedMitigationsDuringWindow() {
            for (com.android.server.PackageWatchdog.ObserverInternal observerInternal : com.android.server.PackageWatchdog.this.mAllObservers.values()) {
                if (observerInternal.getBootMitigationCount() > 0) {
                    return true;
                }
            }
            return false;
        }

        private void resetAllObserversBootMitigationCount() {
            for (int i = 0; i < com.android.server.PackageWatchdog.this.mAllObservers.size(); i++) {
                com.android.server.PackageWatchdog.ObserverInternal observer = (com.android.server.PackageWatchdog.ObserverInternal) com.android.server.PackageWatchdog.this.mAllObservers.valueAt(i);
                observer.setBootMitigationCount(0);
            }
            com.android.server.PackageWatchdog.this.saveAllObserversBootMitigationCountToMetadata(com.android.server.PackageWatchdog.METADATA_FILE);
        }

        void readAllObserversBootMitigationCountIfNecessary(java.lang.String filePath) {
            java.io.File metadataFile = new java.io.File(filePath);
            if (metadataFile.exists()) {
                try {
                    java.io.FileInputStream fileStream = new java.io.FileInputStream(metadataFile);
                    java.io.ObjectInputStream objectStream = new java.io.ObjectInputStream(fileStream);
                    java.util.HashMap<java.lang.String, java.lang.Integer> bootMitigationCounts = (java.util.HashMap) objectStream.readObject();
                    objectStream.close();
                    fileStream.close();
                    for (int i = 0; i < com.android.server.PackageWatchdog.this.mAllObservers.size(); i++) {
                        com.android.server.PackageWatchdog.ObserverInternal observer = (com.android.server.PackageWatchdog.ObserverInternal) com.android.server.PackageWatchdog.this.mAllObservers.valueAt(i);
                        if (bootMitigationCounts.containsKey(observer.name)) {
                            observer.setBootMitigationCount(bootMitigationCounts.get(observer.name).intValue());
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.i(com.android.server.PackageWatchdog.TAG, "Could not read observer metadata file: " + e);
                }
            }
        }
    }
}
