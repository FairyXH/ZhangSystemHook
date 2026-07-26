package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class Watchdog implements android.util.Dumpable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int COMPLETED = 0;
    private static final boolean DB = false;
    public static final boolean DEBUG = false;
    private static final long DEFAULT_TIMEOUT = 60000;
    static final int OVERDUE = 3;
    private static final int PRE_WATCHDOG_TIMEOUT_RATIO = 4;
    private static final java.lang.String PROP_FATAL_LOOP_COUNT = "framework_watchdog.fatal_count";
    private static final java.lang.String PROP_FATAL_LOOP_WINDOWS_SECS = "framework_watchdog.fatal_window.second";
    static final java.lang.String TAG = "Watchdog";
    private static final java.lang.String TIMEOUT_HISTORY_FILE = "/data/system/watchdog-timeout-history.txt";
    static final int WAITED_UNTIL_PRE_WATCHDOG = 2;
    static final int WAITING = 1;
    public static com.android.server.IWatchdogExt mWdtExt;
    private static com.android.server.Watchdog sWatchdog;
    private com.android.server.am.ActivityManagerService mActivity;
    private android.app.IActivityController mController;
    private final com.android.server.Watchdog.HandlerChecker mMonitorChecker;
    private final com.android.server.am.TraceErrorLogger mTraceErrorLogger;
    public static final java.lang.String[] NATIVE_STACKS_OF_INTEREST = {"/system/bin/audioserver", "/system/bin/cameraserver", "/system/bin/drmserver", "/system/bin/keystore2", "/system/bin/mediadrmserver", "/system/bin/mediaserver", "/system/bin/mediaserver64", "/system/bin/netd", "/system/bin/sdcard", "/system/bin/servicemanager", "/system/bin/surfaceflinger", "/system/bin/vold", "media.extractor", "media.metrics", "media.codec", "media.swcodec", "media.transcoding", "com.android.bluetooth", "/apex/com.android.art/bin/artd", "/apex/com.android.os.statsd/bin/statsd"};
    public static final java.util.List<java.lang.String> HAL_INTERFACES_OF_INTEREST = java.util.Arrays.asList("android.hardware.audio@4.0::IDevicesFactory", "android.hardware.audio@5.0::IDevicesFactory", "android.hardware.audio@6.0::IDevicesFactory", "android.hardware.audio@7.0::IDevicesFactory", android.hardware.biometrics.face.V1_0.IBiometricsFace.kInterfaceName, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName, "android.hardware.bluetooth@1.0::IBluetoothHci", "android.hardware.camera.provider@2.4::ICameraProvider", "android.hardware.gnss@1.0::IGnss", "android.hardware.graphics.allocator@2.0::IAllocator", "android.hardware.graphics.allocator@4.0::IAllocator", "android.hardware.graphics.composer@2.1::IComposer", android.hardware.health.V2_0.IHealth.kInterfaceName, "android.hardware.light@2.0::ILight", "android.hardware.media.c2@1.0::IComponentStore", "android.hardware.media.omx@1.0::IOmx", "android.hardware.media.omx@1.0::IOmxStore", "android.hardware.neuralnetworks@1.0::IDevice", "android.hardware.power@1.0::IPower", "android.hardware.power.stats@1.0::IPowerStats", "android.hardware.sensors@1.0::ISensors", "android.hardware.sensors@2.0::ISensors", "android.hardware.sensors@2.1::ISensors", "android.hardware.vibrator@1.0::IVibrator", "android.hardware.vr@1.0::IVr", "android.system.suspend@1.0::ISystemSuspend");
    public static final java.lang.String[] AIDL_INTERFACE_PREFIXES_OF_INTEREST = {"android.hardware.audio.core.IModule/", "android.hardware.audio.core.IConfig/", "android.hardware.audio.effect.IFactory/", "android.hardware.biometrics.face.IFace/", "android.hardware.biometrics.fingerprint.IFingerprint/", "android.hardware.bluetooth.IBluetoothHci/", "android.hardware.camera.provider.ICameraProvider/", "android.hardware.drm.IDrmFactory/", "android.hardware.gnss.IGnss/", "android.hardware.graphics.allocator.IAllocator/", "android.hardware.graphics.composer3.IComposer/", "android.hardware.health.IHealth/", "android.hardware.input.processor.IInputProcessor/", "android.hardware.light.ILights/", "android.hardware.neuralnetworks.IDevice/", "android.hardware.power.IPower/", "android.hardware.power.stats.IPowerStats/", "android.hardware.sensors.ISensors/", "android.hardware.vibrator.IVibrator/", "android.hardware.vibrator.IVibratorManager/", "android.system.suspend.ISystemSuspend/"};
    private final int TIME_SF_WAIT = 20000;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.ArrayList<com.android.server.Watchdog.HandlerCheckerAndTimeout> mHandlerCheckers = new java.util.ArrayList<>();
    private boolean mAllowRestart = true;
    private boolean mSfHang = false;
    private volatile long mWatchdogTimeoutMillis = 60000;
    java.text.SimpleDateFormat mTraceDateFormat = new java.text.SimpleDateFormat("dd_MM_HH_mm_ss.SSS");
    private final java.util.List<java.lang.Integer> mInterestingJavaPids = new java.util.ArrayList();
    public com.android.server.IWatchdogSocExt mWdtSocExt = (com.android.server.IWatchdogSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.IWatchdogSocExt.class).base(this).create();
    private final java.lang.Thread mThread = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.Watchdog$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() throws java.lang.Throwable {
            this.f$0.run();
        }
    }, "watchdog");

    public interface Monitor {
        void monitor();
    }

    static final class HandlerCheckerAndTimeout {
        private final java.util.Optional<java.lang.Long> mCustomTimeoutMillis;
        private final com.android.server.Watchdog.HandlerChecker mHandler;

        private HandlerCheckerAndTimeout(com.android.server.Watchdog.HandlerChecker checker, java.util.Optional<java.lang.Long> timeoutMillis) {
            this.mHandler = checker;
            this.mCustomTimeoutMillis = timeoutMillis;
        }

        com.android.server.Watchdog.HandlerChecker checker() {
            return this.mHandler;
        }

        java.util.Optional<java.lang.Long> customTimeoutMillis() {
            return this.mCustomTimeoutMillis;
        }

        static com.android.server.Watchdog.HandlerCheckerAndTimeout withDefaultTimeout(com.android.server.Watchdog.HandlerChecker checker) {
            return new com.android.server.Watchdog.HandlerCheckerAndTimeout(checker, java.util.Optional.empty());
        }

        static com.android.server.Watchdog.HandlerCheckerAndTimeout withCustomTimeout(com.android.server.Watchdog.HandlerChecker checker, long timeoutMillis) {
            return new com.android.server.Watchdog.HandlerCheckerAndTimeout(checker, java.util.Optional.of(java.lang.Long.valueOf(timeoutMillis)));
        }
    }

    public static class HandlerChecker implements java.lang.Runnable {
        private java.time.Clock mClock;
        private boolean mCompleted;
        private com.android.server.Watchdog.Monitor mCurrentMonitor;
        private final android.os.Handler mHandler;
        private java.lang.Object mLock;
        private final java.util.ArrayList<com.android.server.Watchdog.Monitor> mMonitorQueue;
        private final java.util.ArrayList<com.android.server.Watchdog.Monitor> mMonitors;
        private final java.lang.String mName;
        private int mPauseCount;
        private long mPauseEndTimeMillis;
        private long mStartTimeMillis;
        private long mWaitMaxMillis;

        HandlerChecker(android.os.Handler handler, java.lang.String name, java.lang.Object lock, java.time.Clock clock) {
            this.mMonitors = new java.util.ArrayList<>();
            this.mMonitorQueue = new java.util.ArrayList<>();
            this.mHandler = handler;
            this.mName = name;
            this.mLock = lock;
            this.mCompleted = true;
            this.mClock = clock;
        }

        HandlerChecker(android.os.Handler handler, java.lang.String name, java.lang.Object lock) {
            this(handler, name, lock, android.os.SystemClock.uptimeClock());
        }

        void addMonitorLocked(com.android.server.Watchdog.Monitor monitor) {
            this.mMonitorQueue.add(monitor);
        }

        public void scheduleCheckLocked(long handlerCheckerTimeoutMillis) {
            this.mWaitMaxMillis = handlerCheckerTimeoutMillis;
            if (this.mCompleted) {
                this.mMonitors.addAll(this.mMonitorQueue);
                this.mMonitorQueue.clear();
            }
            long nowMillis = this.mClock.millis();
            boolean isPaused = this.mPauseCount > 0 || this.mPauseEndTimeMillis > nowMillis;
            if ((this.mMonitors.size() == 0 && isHandlerPolling()) || isPaused) {
                this.mCompleted = true;
                return;
            }
            if (!this.mCompleted) {
                return;
            }
            this.mCompleted = false;
            this.mCurrentMonitor = null;
            this.mStartTimeMillis = nowMillis;
            this.mPauseEndTimeMillis = 0L;
            this.mHandler.postAtFrontOfQueue(this);
        }

        boolean isHandlerPolling() {
            return this.mHandler.getLooper().getQueue().isPolling();
        }

        public int getCompletionStateLocked() {
            if (this.mCompleted) {
                return 0;
            }
            long latency = this.mClock.millis() - this.mStartTimeMillis;
            if (latency < this.mWaitMaxMillis / 4) {
                return 1;
            }
            if (latency < this.mWaitMaxMillis) {
                return 2;
            }
            return 3;
        }

        public java.lang.Thread getThread() {
            return this.mHandler.getLooper().getThread();
        }

        public java.lang.String getName() {
            return this.mName;
        }

        java.lang.String describeBlockedStateLocked() {
            java.lang.String prefix;
            if (this.mCurrentMonitor == null) {
                prefix = "Blocked in handler";
            } else {
                prefix = "Blocked in monitor " + this.mCurrentMonitor.getClass().getName();
            }
            long latencySeconds = (this.mClock.millis() - this.mStartTimeMillis) / 1000;
            return prefix + " on " + this.mName + " (" + getThread().getName() + ") for " + latencySeconds + "s";
        }

        @Override // java.lang.Runnable
        public void run() {
            int size = this.mMonitors.size();
            for (int i = 0; i < size; i++) {
                synchronized (this.mLock) {
                    this.mCurrentMonitor = this.mMonitors.get(i);
                }
                this.mCurrentMonitor.monitor();
            }
            synchronized (this.mLock) {
                this.mCompleted = true;
                this.mCurrentMonitor = null;
            }
        }

        public void pauseForLocked(int pauseMillis, java.lang.String reason) {
            this.mPauseEndTimeMillis = this.mClock.millis() + ((long) pauseMillis);
            this.mCompleted = true;
            android.util.Slog.i(com.android.server.Watchdog.TAG, "Pausing of HandlerChecker: " + this.mName + " for reason: " + reason + ". Pause end time: " + this.mPauseEndTimeMillis);
        }

        public void pauseLocked(java.lang.String reason) {
            this.mPauseCount++;
            this.mCompleted = true;
            android.util.Slog.i(com.android.server.Watchdog.TAG, "Pausing HandlerChecker: " + this.mName + " for reason: " + reason + ". Pause count: " + this.mPauseCount);
        }

        public void resumeLocked(java.lang.String reason) {
            if (this.mPauseCount > 0) {
                this.mPauseCount--;
                android.util.Slog.i(com.android.server.Watchdog.TAG, "Resuming HandlerChecker: " + this.mName + " for reason: " + reason + ". Pause count: " + this.mPauseCount);
            } else {
                android.util.Slog.wtf(com.android.server.Watchdog.TAG, "Already resumed HandlerChecker: " + this.mName);
            }
        }

        public java.lang.String toString() {
            return "CheckerHandler for " + this.mName;
        }
    }

    final class RebootRequestReceiver extends android.content.BroadcastReceiver {
        RebootRequestReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context c, android.content.Intent intent) {
            if (intent.getIntExtra("nowait", 0) != 0) {
                com.android.server.Watchdog.this.rebootSystem("Received ACTION_REBOOT broadcast");
            } else {
                android.util.Slog.w(com.android.server.Watchdog.TAG, "Unsupported ACTION_REBOOT broadcast: " + intent);
            }
        }
    }

    private static final class BinderThreadMonitor implements com.android.server.Watchdog.Monitor {
        private BinderThreadMonitor() {
        }

        @Override // com.android.server.Watchdog.Monitor
        public void monitor() {
            com.android.server.Watchdog.mWdtExt.getBinderBlockTimeMS();
            android.os.Binder.blockUntilThreadAvailable();
            com.android.server.Watchdog.mWdtExt.onProcessBinderCnt();
        }
    }

    public static com.android.server.Watchdog getInstance() {
        if (sWatchdog == null) {
            sWatchdog = new com.android.server.Watchdog();
        }
        return sWatchdog;
    }

    private Watchdog() {
        com.android.server.ServiceThread t = new com.android.server.ServiceThread("watchdog.monitor", 0, true);
        t.start();
        this.mMonitorChecker = new com.android.server.Watchdog.HandlerChecker(new android.os.Handler(t.getLooper()), "monitor thread", this.mLock);
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(this.mMonitorChecker));
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(com.android.server.FgThread.getHandler(), "foreground thread", this.mLock)));
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(new android.os.Handler(android.os.Looper.getMainLooper()), "main thread", this.mLock)));
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(com.android.server.UiThread.getHandler(), "ui thread", this.mLock)));
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(com.android.server.IoThread.getHandler(), "i/o thread", this.mLock)));
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(com.android.server.DisplayThread.getHandler(), "display thread", this.mLock)));
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(com.android.server.AnimationThread.getHandler(), "animation thread", this.mLock)));
        this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(com.android.server.wm.SurfaceAnimationThread.getHandler(), "surface animation thread", this.mLock)));
        addMonitor(new com.android.server.Watchdog.BinderThreadMonitor());
        this.mInterestingJavaPids.add(java.lang.Integer.valueOf(android.os.Process.myPid()));
        mWdtExt = (com.android.server.IWatchdogExt) system.ext.loader.core.ExtLoader.type(com.android.server.IWatchdogExt.class).base(this).create();
        this.mWdtSocExt.getExceptionLog();
        this.mTraceErrorLogger = new com.android.server.am.TraceErrorLogger();
    }

    public void start() {
        if (mWdtExt.checkIfNeedCloseWdt()) {
            return;
        }
        this.mThread.start();
    }

    public void init(android.content.Context context, com.android.server.am.ActivityManagerService activity) {
        this.mActivity = activity;
        context.registerReceiver(new com.android.server.Watchdog.RebootRequestReceiver(), new android.content.IntentFilter("android.intent.action.REBOOT"), "android.permission.REBOOT", null);
        this.mWdtSocExt.WDTMatterJava(0L);
        mWdtExt.init(context, activity);
    }

    private static class SettingsObserver extends android.database.ContentObserver {
        private final android.content.Context mContext;
        private final android.net.Uri mUri;
        private final com.android.server.Watchdog mWatchdog;

        SettingsObserver(android.content.Context context, com.android.server.Watchdog watchdog) {
            super(com.android.internal.os.BackgroundThread.getHandler());
            this.mUri = android.provider.Settings.Global.getUriFor("system_server_watchdog_timeout_ms");
            this.mContext = context;
            this.mWatchdog = watchdog;
            onChange();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (this.mUri.equals(uri)) {
                onChange();
            }
        }

        public void onChange() {
            try {
                this.mWatchdog.updateWatchdogTimeout(android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), "system_server_watchdog_timeout_ms", 60000L));
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.e(com.android.server.Watchdog.TAG, "Exception while reading settings " + e.getMessage(), e);
            }
        }
    }

    public void registerSettingsObserver(android.content.Context context) {
        context.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("system_server_watchdog_timeout_ms"), false, new com.android.server.Watchdog.SettingsObserver(context, this), 0);
    }

    void updateWatchdogTimeout(long timeoutMillis) {
        if (!android.os.Build.IS_USERDEBUG && timeoutMillis <= 20000) {
            timeoutMillis = 20001;
        }
        this.mWatchdogTimeoutMillis = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", "")) ? 240000L : timeoutMillis;
        android.util.Slog.i(TAG, "Watchdog timeout updated to " + this.mWatchdogTimeoutMillis + " millis");
    }

    private static boolean isInterestingJavaProcess(java.lang.String processName) {
        return processName.equals(com.android.server.StorageManagerService.sMediaStoreAuthorityProcessName) || processName.equals("com.android.phone");
    }

    public void processStarted(java.lang.String processName, int pid) {
        mWdtExt.processStarted(processName, pid);
        if (isInterestingJavaProcess(processName)) {
            android.util.Slog.i(TAG, "Interesting Java process " + processName + " started. Pid " + pid);
            synchronized (this.mLock) {
                this.mInterestingJavaPids.add(java.lang.Integer.valueOf(pid));
            }
        }
    }

    public void processDied(java.lang.String processName, int pid) {
        if (isInterestingJavaProcess(processName)) {
            android.util.Slog.i(TAG, "Interesting Java process " + processName + " died. Pid " + pid);
            synchronized (this.mLock) {
                this.mInterestingJavaPids.remove(java.lang.Integer.valueOf(pid));
            }
        }
    }

    public void setActivityController(android.app.IActivityController controller) {
        synchronized (this.mLock) {
            this.mController = controller;
        }
    }

    public void setAllowRestart(boolean allowRestart) {
        synchronized (this.mLock) {
            this.mAllowRestart = allowRestart;
        }
    }

    public void addMonitor(com.android.server.Watchdog.Monitor monitor) {
        synchronized (this.mLock) {
            this.mMonitorChecker.addMonitorLocked(monitor);
        }
    }

    public void addThread(android.os.Handler thread) {
        synchronized (this.mLock) {
            java.lang.String name = thread.getLooper().getThread().getName();
            this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withDefaultTimeout(new com.android.server.Watchdog.HandlerChecker(thread, name, this.mLock)));
        }
    }

    public void addThread(android.os.Handler thread, long timeoutMillis) {
        synchronized (this.mLock) {
            java.lang.String name = thread.getLooper().getThread().getName();
            this.mHandlerCheckers.add(com.android.server.Watchdog.HandlerCheckerAndTimeout.withCustomTimeout(new com.android.server.Watchdog.HandlerChecker(thread, name, this.mLock), timeoutMillis));
        }
    }

    public void pauseWatchingCurrentThreadFor(int pauseMillis, java.lang.String reason) {
        synchronized (this.mLock) {
            for (com.android.server.Watchdog.HandlerCheckerAndTimeout hc : this.mHandlerCheckers) {
                com.android.server.Watchdog.HandlerChecker checker = hc.checker();
                if (java.lang.Thread.currentThread().equals(checker.getThread())) {
                    checker.pauseForLocked(pauseMillis, reason);
                }
            }
        }
    }

    public void pauseWatchingMonitorsFor(int pauseMillis, java.lang.String reason) {
        this.mMonitorChecker.pauseForLocked(pauseMillis, reason);
    }

    public void pauseWatchingCurrentThread(java.lang.String reason) {
        synchronized (this.mLock) {
            for (com.android.server.Watchdog.HandlerCheckerAndTimeout hc : this.mHandlerCheckers) {
                com.android.server.Watchdog.HandlerChecker checker = hc.checker();
                if (java.lang.Thread.currentThread().equals(checker.getThread())) {
                    checker.pauseLocked(reason);
                }
            }
        }
    }

    public void resumeWatchingCurrentThread(java.lang.String reason) {
        synchronized (this.mLock) {
            for (com.android.server.Watchdog.HandlerCheckerAndTimeout hc : this.mHandlerCheckers) {
                com.android.server.Watchdog.HandlerChecker checker = hc.checker();
                if (java.lang.Thread.currentThread().equals(checker.getThread())) {
                    checker.resumeLocked(reason);
                }
            }
        }
    }

    void rebootSystem(java.lang.String reason) {
        android.util.Slog.i(TAG, "Rebooting system because: " + reason);
        android.os.IPowerManager pms = android.os.ServiceManager.getService("power");
        try {
            pms.reboot(false, reason, false);
        } catch (android.os.RemoteException e) {
        }
    }

    private int evaluateCheckerCompletionLocked() {
        int state = 0;
        for (int i = 0; i < this.mHandlerCheckers.size(); i++) {
            com.android.server.Watchdog.HandlerChecker hc = this.mHandlerCheckers.get(i).checker();
            state = java.lang.Math.max(state, hc.getCompletionStateLocked());
        }
        return state;
    }

    private java.util.ArrayList<com.android.server.Watchdog.HandlerChecker> getCheckersWithStateLocked(int completionState) {
        java.util.ArrayList<com.android.server.Watchdog.HandlerChecker> checkers = new java.util.ArrayList<>();
        for (int i = 0; i < this.mHandlerCheckers.size(); i++) {
            com.android.server.Watchdog.HandlerChecker hc = this.mHandlerCheckers.get(i).checker();
            if (hc.getCompletionStateLocked() == completionState) {
                checkers.add(hc);
            }
        }
        return checkers;
    }

    private java.lang.String describeCheckersLocked(java.util.List<com.android.server.Watchdog.HandlerChecker> checkers) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(128);
        for (int i = 0; i < checkers.size(); i++) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(checkers.get(i).describeBlockedStateLocked());
        }
        return builder.toString();
    }

    private static void addInterestingHidlPids(java.util.HashSet<java.lang.Integer> pids) {
        try {
            android.hidl.manager.V1_0.IServiceManager serviceManager = android.hidl.manager.V1_0.IServiceManager.getService();
            java.util.ArrayList<android.hidl.manager.V1_0.IServiceManager.InstanceDebugInfo> dump = serviceManager.debugDump();
            for (android.hidl.manager.V1_0.IServiceManager.InstanceDebugInfo info : dump) {
                if (info.pid != -1 && HAL_INTERFACES_OF_INTEREST.contains(info.interfaceName)) {
                    pids.add(java.lang.Integer.valueOf(info.pid));
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.w(TAG, e);
        }
    }

    private static void addInterestingAidlPids(java.util.HashSet<java.lang.Integer> pids) {
        android.os.ServiceDebugInfo[] infos = android.os.ServiceManager.getServiceDebugInfo();
        if (infos == null) {
            return;
        }
        for (android.os.ServiceDebugInfo info : infos) {
            for (java.lang.String prefix : AIDL_INTERFACE_PREFIXES_OF_INTEREST) {
                if (info.name.startsWith(prefix)) {
                    pids.add(java.lang.Integer.valueOf(info.debugPid));
                }
            }
        }
    }

    public static java.util.ArrayList<java.lang.Integer> getInterestingNativePids() {
        java.util.HashSet<java.lang.Integer> pids = new java.util.HashSet<>();
        addInterestingAidlPids(pids);
        addInterestingHidlPids(pids);
        mWdtExt.addWatchdogExtNativePids(pids);
        int[] nativePids = android.os.Process.getPidsForCommands(NATIVE_STACKS_OF_INTEREST);
        if (nativePids != null) {
            for (int i : nativePids) {
                pids.add(java.lang.Integer.valueOf(i));
            }
        }
        return new java.util.ArrayList<>(pids);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void run() throws java.lang.Throwable {
        java.util.List<com.android.server.Watchdog.HandlerChecker> blockedCheckers;
        java.lang.String subject;
        boolean allowRestart;
        int debuggerWasConnected;
        boolean pids;
        java.util.List<com.android.server.Watchdog.HandlerChecker> pids2;
        java.lang.String pids3;
        boolean allowRestart2;
        java.util.List<com.android.server.Watchdog.HandlerChecker> pids4;
        long dueTime;
        android.app.IActivityController controller;
        java.lang.String subject2;
        boolean waitedHalf = false;
        while (true) {
            mWdtExt.triggerDetect();
            java.util.List<com.android.server.Watchdog.HandlerChecker> blockedCheckers2 = java.util.Collections.emptyList();
            java.lang.String subject3 = "";
            boolean allowRestart3 = true;
            boolean doWaitedPreDump = false;
            long watchdogTimeoutMillis = this.mWatchdogTimeoutMillis;
            long checkIntervalMillis = watchdogTimeoutMillis / 4;
            this.mSfHang = false;
            this.mWdtSocExt.WDTMatterJava(300L);
            synchronized (this.mLock) {
                long timeout = checkIntervalMillis;
                int i = 0;
                while (i < this.mHandlerCheckers.size()) {
                    try {
                        try {
                            com.android.server.Watchdog.HandlerCheckerAndTimeout hc = this.mHandlerCheckers.get(i);
                            blockedCheckers = blockedCheckers2;
                            try {
                                com.android.server.Watchdog.HandlerChecker handlerCheckerChecker = hc.checker();
                                subject = subject3;
                                try {
                                    java.util.Optional<java.lang.Long> optionalCustomTimeoutMillis = hc.customTimeoutMillis();
                                    allowRestart = allowRestart3;
                                    try {
                                        long timeout2 = timeout;
                                        long timeout3 = android.os.Build.HW_TIMEOUT_MULTIPLIER;
                                        handlerCheckerChecker.scheduleCheckLocked(optionalCustomTimeoutMillis.orElse(java.lang.Long.valueOf(timeout3 * watchdogTimeoutMillis)).longValue());
                                        i++;
                                        blockedCheckers2 = blockedCheckers;
                                        subject3 = subject;
                                        allowRestart3 = allowRestart;
                                        timeout = timeout2;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        while (true) {
                                            try {
                                                throw th;
                                            } catch (java.lang.Throwable th2) {
                                                th = th2;
                                            }
                                        }
                                    }
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                    while (true) {
                                        throw th;
                                    }
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                    }
                }
                blockedCheckers = blockedCheckers2;
                subject = subject3;
                allowRestart = allowRestart3;
                long timeout4 = timeout;
                debuggerWasConnected = 0 > 0 ? 0 - 1 : 0;
                try {
                    long start = android.os.SystemClock.uptimeMillis();
                    long timeout5 = timeout4;
                    while (timeout5 > 0) {
                        if (android.os.Debug.isDebuggerConnected()) {
                            debuggerWasConnected = 2;
                        }
                        try {
                            this.mLock.wait(timeout5);
                        } catch (java.lang.InterruptedException e) {
                            android.util.Log.wtf(TAG, e);
                        }
                        if (android.os.Debug.isDebuggerConnected()) {
                            debuggerWasConnected = 2;
                        }
                        timeout5 = checkIntervalMillis - (android.os.SystemClock.uptimeMillis() - start);
                        mWdtExt.eventDailyPush();
                    }
                    mWdtExt.checkSystemHeapMem();
                    long sfHangTime = this.mWdtSocExt.getSfHangTime();
                    if (sfHangTime > 40000) {
                        android.util.Slog.v(TAG, "**SF hang Time **" + sfHangTime);
                        this.mSfHang = true;
                        java.util.List<com.android.server.Watchdog.HandlerChecker> blockedCheckers3 = getCheckersWithStateLocked(2);
                        try {
                            java.util.ArrayList<java.lang.Integer> pids5 = new java.util.ArrayList<>(this.mInterestingJavaPids);
                            pids = waitedHalf;
                            pids2 = blockedCheckers3;
                            pids3 = "";
                            allowRestart2 = allowRestart;
                            pids4 = pids5;
                            try {
                            } catch (java.lang.Throwable th7) {
                                th = th7;
                                while (true) {
                                    throw th;
                                }
                            }
                        } catch (java.lang.Throwable th8) {
                            th = th8;
                            while (true) {
                                throw th;
                            }
                        }
                    } else {
                        int waitState = evaluateCheckerCompletionLocked();
                        if (waitState == 0) {
                            mWdtExt.setWatchdogHappenValue(false);
                            if (waitedHalf) {
                                this.mWdtSocExt.switchFtrace(4);
                            }
                            waitedHalf = false;
                        } else if (waitState != 1) {
                            if (waitState != 2) {
                                mWdtExt.removeTheiaMsg();
                                java.util.List<com.android.server.Watchdog.HandlerChecker> blockedCheckers4 = getCheckersWithStateLocked(3);
                                java.lang.String subject4 = describeCheckersLocked(blockedCheckers4);
                                boolean allowRestart4 = this.mAllowRestart;
                                try {
                                    pids = waitedHalf;
                                    try {
                                        pids4 = new java.util.ArrayList<>(this.mInterestingJavaPids);
                                        pids2 = blockedCheckers4;
                                        pids3 = subject4;
                                        allowRestart2 = allowRestart4;
                                    } catch (java.lang.Throwable th9) {
                                        th = th9;
                                        while (true) {
                                            throw th;
                                        }
                                    }
                                } catch (java.lang.Throwable th10) {
                                    th = th10;
                                }
                            } else if (!waitedHalf) {
                                mWdtExt.setWatchdogHappenValue(true);
                                android.util.Slog.i(TAG, "WAITED_UNTIL_PRE_WATCHDOG");
                                mWdtExt.killMultimediaProcess();
                                java.util.List<com.android.server.Watchdog.HandlerChecker> blockedCheckers5 = getCheckersWithStateLocked(2);
                                try {
                                    subject2 = describeCheckersLocked(blockedCheckers5);
                                    try {
                                        pids = true;
                                    } catch (java.lang.Throwable th11) {
                                        th = th11;
                                    }
                                } catch (java.lang.Throwable th12) {
                                    th = th12;
                                }
                                try {
                                    java.util.ArrayList<java.lang.Integer> pids6 = new java.util.ArrayList<>(this.mInterestingJavaPids);
                                    doWaitedPreDump = true;
                                    pids2 = blockedCheckers5;
                                    pids3 = subject2;
                                    allowRestart2 = allowRestart;
                                    pids4 = pids6;
                                } catch (java.lang.Throwable th13) {
                                    th = th13;
                                    while (true) {
                                        throw th;
                                    }
                                }
                            }
                        }
                    }
                } catch (java.lang.Throwable th14) {
                    th = th14;
                }
            }
            if (com.android.server.am.trace.SmartTraceUtils.isPerfettoDumpEnabled()) {
                com.android.server.am.trace.SmartTraceUtils.traceStart();
                long dueTime2 = android.os.SystemClock.uptimeMillis() + 30000;
                dueTime = dueTime2;
            } else {
                dueTime = 0;
            }
            logWatchog(doWaitedPreDump, pids4, pids4);
            if (doWaitedPreDump) {
                waitedHalf = pids4;
            } else {
                synchronized (this.mLock) {
                    try {
                        controller = this.mController;
                    } finally {
                        th = th;
                        java.util.List<com.android.server.Watchdog.HandlerChecker> list = pids4;
                        java.util.List<com.android.server.Watchdog.HandlerChecker> list2 = pids4;
                        boolean z = pids4;
                        while (true) {
                            try {
                            } catch (java.lang.Throwable th15) {
                                th = th15;
                            }
                        }
                    }
                }
                if (!this.mSfHang && controller != null) {
                    android.util.Slog.i(TAG, "Reporting stuck state to activity controller");
                    try {
                        android.os.Binder.setDumpDisabled("Service dumps disabled due to hung system process.");
                        int res = controller.systemNotResponding(pids4);
                        if (res >= 0) {
                            android.util.Slog.i(TAG, "Activity controller requested to coninue to wait");
                            waitedHalf = false;
                        }
                    } catch (android.os.RemoteException e2) {
                    }
                }
                if (android.os.Debug.isDebuggerConnected()) {
                    debuggerWasConnected = 2;
                }
                if (debuggerWasConnected >= 2) {
                    android.util.Slog.w(TAG, "Debugger connected: Watchdog is *not* killing the system process");
                } else if (debuggerWasConnected > 0) {
                    android.util.Slog.w(TAG, "Debugger was connected: Watchdog is *not* killing the system process");
                } else if (allowRestart2) {
                    android.util.Slog.w(TAG, "*** WATCHDOG KILLING SYSTEM PROCESS: " + pids4);
                    mWdtExt.unfreezeForWatchdog();
                    com.android.server.WatchdogDiagnostics.diagnoseCheckers(pids4);
                    mWdtExt.writeEvent(pids4);
                    android.util.Slog.w(TAG, "*** GOODBYE!");
                    if ("vsoc_arm64".equals(android.os.SystemProperties.get("ro.soc.model"))) {
                        android.util.Slog.w(TAG, "reboot for cuttlefish virtual devices ... ");
                        doSysRq('b');
                    }
                    if (com.android.server.am.trace.SmartTraceUtils.isPerfettoDumpEnabled() && dueTime > android.os.SystemClock.uptimeMillis()) {
                        long timeDelta = dueTime - android.os.SystemClock.uptimeMillis();
                        android.util.Slog.i(TAG, "Sleep " + timeDelta + " ms to make sure perfetto log to be dumped completely");
                        android.os.SystemClock.sleep(timeDelta);
                    }
                    if (!android.os.Build.IS_USER && isCrashLoopFound() && !android.sysprop.WatchdogProperties.should_ignore_fatal_count().orElse(false).booleanValue()) {
                        breakCrashLoop();
                    }
                    this.mWdtSocExt.WDTMatterJava(330L);
                    if (this.mSfHang) {
                        android.util.Slog.w(TAG, "SF hang!");
                        if (this.mWdtSocExt.getSfRebootTime() > 3) {
                            android.util.Slog.w(TAG, "SF hang reboot time larger than 3 time, reboot device!");
                            rebootSystem("Maybe SF driver hang, reboot device.");
                        } else {
                            this.mWdtSocExt.setSfRebootTime();
                        }
                        android.util.Slog.v(TAG, "killing surfaceflinger for surfaceflinger hang");
                        java.lang.String[] sf = {"/system/bin/surfaceflinger"};
                        int[] pid_sf = android.os.Process.getPidsForCommands(sf);
                        if (pid_sf != null && pid_sf[0] > 0) {
                            android.os.Process.killProcess(pid_sf[0]);
                        }
                        android.util.Slog.v(TAG, "kill surfaceflinger end");
                    } else {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                    java.lang.System.exit(10);
                } else {
                    android.util.Slog.w(TAG, "Restart not allowed: Watchdog is *not* killing the system process");
                }
                waitedHalf = false;
            }
        }
    }

    private void logWatchog(boolean preWatchdog, java.lang.String subject, java.util.ArrayList<java.lang.Integer> pids) {
        java.lang.String dropboxTag;
        java.util.ArrayList<java.lang.Integer> nativePids = getInterestingNativePids();
        java.lang.String criticalEvents = com.android.server.criticalevents.CriticalEventLog.getInstance().logLinesForSystemServerTraceFile();
        final java.util.UUID errorId = this.mTraceErrorLogger.generateErrorId();
        if (this.mTraceErrorLogger.isAddErrorIdEnabled()) {
            this.mTraceErrorLogger.addProcessInfoAndErrorIdToTrace("system_server", android.os.Process.myPid(), errorId);
            this.mTraceErrorLogger.addSubjectToTrace(subject, errorId);
        }
        if (preWatchdog) {
            mWdtExt.sendTheiaMsg(subject);
            this.mWdtSocExt.WDTMatterJava(360L);
            this.mWdtSocExt.switchFtrace(3);
            android.util.Slog.e(TAG, "**pre_watchdog happen **" + subject);
            com.android.server.criticalevents.CriticalEventLog.getInstance().logHalfWatchdog(subject);
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SYSTEM_SERVER_PRE_WATCHDOG_OCCURRED);
            dropboxTag = "pre_watchdog";
        } else {
            com.android.server.criticalevents.CriticalEventLog.getInstance().logWatchdog(subject, errorId);
            android.util.Slog.e(TAG, "**SWT happen **" + subject);
            this.mWdtSocExt.switchFtrace(2);
            java.lang.String sfLog = (this.mSfHang && subject.isEmpty()) ? "surfaceflinger hang." : "";
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.WATCHDOG, sfLog.isEmpty() ? subject : sfLog);
            this.mWdtSocExt.WDTMatterJava(420L);
            com.android.internal.util.FrameworkStatsLog.write(185, subject);
            dropboxTag = "watchdog";
        }
        long anrTime = android.os.SystemClock.uptimeMillis();
        final java.lang.StringBuilder report = new java.lang.StringBuilder();
        report.append(com.android.server.ResourcePressureUtil.currentPsiState());
        com.android.internal.os.ProcessCpuTracker processCpuTracker = new com.android.internal.os.ProcessCpuTracker(false);
        java.io.StringWriter tracesFileException = new java.io.StringWriter();
        mWdtExt.addBinderPid(pids, nativePids, android.os.Process.myPid());
        final java.io.File finalStack = com.android.server.am.StackTracesDumpHelper.dumpStackTraces(pids, processCpuTracker, new android.util.SparseBooleanArray(), java.util.concurrent.CompletableFuture.completedFuture(getInterestingNativePids()), tracesFileException, subject, criticalEvents, new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), null);
        if (finalStack != null) {
            com.android.server.am.trace.SmartTraceUtils.dumpStackTraces(android.os.Process.myPid(), pids, nativePids, finalStack);
        }
        android.os.SystemClock.sleep(5000L);
        processCpuTracker.update();
        report.append(processCpuTracker.printCurrentState(anrTime, 10));
        report.append(tracesFileException.getBuffer());
        if (!preWatchdog) {
            doSysRq('w');
            doSysRq('l');
        }
        mWdtExt.addStabilityDebugInAll(preWatchdog, finalStack, subject);
        final java.lang.String str = dropboxTag;
        java.lang.Thread dropboxThread = new java.lang.Thread("watchdogWriteToDropbox") { // from class: com.android.server.Watchdog.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (com.android.server.Watchdog.this.mActivity != null) {
                    com.android.server.Watchdog.this.mActivity.addErrorToDropBox(str, null, "system_server", null, null, null, null, report.toString(), finalStack, null, null, null, errorId, null);
                }
            }
        };
        dropboxThread.start();
        try {
            dropboxThread.join(2000L);
            if (!preWatchdog && android.os.Build.isMtkPlatform()) {
                java.lang.Thread.sleep(8000L);
            }
        } catch (java.lang.InterruptedException e) {
        }
    }

    private void doSysRq(char c) {
        try {
            java.io.FileWriter sysrq_trigger = new java.io.FileWriter("/proc/sysrq-trigger");
            sysrq_trigger.write(c);
            sysrq_trigger.close();
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Failed to write to /proc/sysrq-trigger", e);
        }
    }

    private void resetTimeoutHistory() {
        writeTimeoutHistory(new java.util.ArrayList());
    }

    private void writeTimeoutHistory(java.lang.Iterable<java.lang.String> crashHistory) {
        java.lang.String data = java.lang.String.join(",", crashHistory);
        try {
            java.io.FileWriter writer = new java.io.FileWriter(TIMEOUT_HISTORY_FILE);
            try {
                writer.write(android.os.SystemProperties.get("ro.boottime.zygote"));
                writer.write(":");
                writer.write(data);
                writer.close();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write file /data/system/watchdog-timeout-history.txt", e);
        }
    }

    private java.lang.String[] readTimeoutHistory() {
        java.lang.String[] emptyStringArray = new java.lang.String[0];
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(TIMEOUT_HISTORY_FILE));
            try {
                java.lang.String line = reader.readLine();
                if (line == null) {
                    reader.close();
                    return emptyStringArray;
                }
                java.lang.String[] data = line.trim().split(":");
                java.lang.String boottime = data.length >= 1 ? data[0] : "";
                java.lang.String history = data.length >= 2 ? data[1] : "";
                if (!android.os.SystemProperties.get("ro.boottime.zygote").equals(boottime) || history.isEmpty()) {
                    reader.close();
                    return emptyStringArray;
                }
                java.lang.String[] strArrSplit = history.split(",");
                reader.close();
                return strArrSplit;
            } finally {
            }
        } catch (java.io.FileNotFoundException e) {
            return emptyStringArray;
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Failed to read file /data/system/watchdog-timeout-history.txt", e2);
            return emptyStringArray;
        }
        return emptyStringArray;
    }

    private boolean hasActiveUsbConnection() {
        try {
            java.lang.String state = android.os.FileUtils.readTextFile(new java.io.File("/sys/class/android_usb/android0/state"), 128, null).trim();
            if ("CONFIGURED".equals(state)) {
                return true;
            }
            return false;
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Failed to determine if device was on USB", e);
            return false;
        }
    }

    private boolean isCrashLoopFound() {
        int fatalCount = android.sysprop.WatchdogProperties.fatal_count().orElse(0).intValue();
        long fatalWindowMs = java.util.concurrent.TimeUnit.SECONDS.toMillis(android.sysprop.WatchdogProperties.fatal_window_seconds().orElse(0).intValue());
        if (fatalCount == 0 || fatalWindowMs == 0) {
            if (fatalCount != fatalWindowMs) {
                android.util.Slog.w(TAG, java.lang.String.format("sysprops '%s' and '%s' should be set or unset together", PROP_FATAL_LOOP_COUNT, PROP_FATAL_LOOP_WINDOWS_SECS));
            }
            return false;
        }
        long nowMs = android.os.SystemClock.elapsedRealtime();
        java.lang.String[] rawCrashHistory = readTimeoutHistory();
        java.util.ArrayList<java.lang.String> crashHistory = new java.util.ArrayList<>(java.util.Arrays.asList((java.lang.String[]) java.util.Arrays.copyOfRange(rawCrashHistory, java.lang.Math.max(0, (rawCrashHistory.length - fatalCount) - 1), rawCrashHistory.length)));
        crashHistory.add(java.lang.String.valueOf(nowMs));
        writeTimeoutHistory(crashHistory);
        if (hasActiveUsbConnection()) {
            return false;
        }
        try {
            long firstCrashMs = java.lang.Long.parseLong(crashHistory.get(0));
            return crashHistory.size() >= fatalCount && nowMs - firstCrashMs < fatalWindowMs;
        } catch (java.lang.NumberFormatException t) {
            android.util.Slog.w(TAG, "Failed to parseLong " + crashHistory.get(0), t);
            resetTimeoutHistory();
            return false;
        }
    }

    private void breakCrashLoop() {
        try {
            java.io.FileWriter kmsg = new java.io.FileWriter("/dev/kmsg_debug", true);
            try {
                kmsg.append((java.lang.CharSequence) "Fatal reset to escape the system_server crashing loop\n");
                kmsg.close();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Failed to append to kmsg", e);
        }
        doSysRq('c');
    }

    @Override // android.util.Dumpable
    public void dump(java.io.PrintWriter pw, java.lang.String[] args) {
        pw.print("WatchdogTimeoutMillis=");
        pw.println(this.mWatchdogTimeoutMillis);
    }

    private void appendFile(java.io.File writeTo, java.io.File copyFrom) {
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(copyFrom));
            java.io.FileWriter out = new java.io.FileWriter(writeTo, true);
            while (true) {
                java.lang.String line = in.readLine();
                if (line != null) {
                    out.write(line);
                    out.write(10);
                } else {
                    in.close();
                    out.close();
                    return;
                }
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Exception while writing watchdog traces to new file!");
            e.printStackTrace();
        }
    }
}
