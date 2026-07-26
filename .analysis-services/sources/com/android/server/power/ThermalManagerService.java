package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class ThermalManagerService extends com.android.server.SystemService {
    private static final boolean DEBUG = false;
    public static final int MAX_FORECAST_SEC = 60;
    public static final int MIN_FORECAST_SEC = 0;
    private static final java.lang.String TAG = com.android.server.power.ThermalManagerService.class.getSimpleName();
    private final android.content.Context mContext;
    private final java.util.concurrent.atomic.AtomicBoolean mHalReady;
    private com.android.server.power.ThermalManagerService.ThermalHalWrapper mHalWrapper;
    private boolean mIsStatusOverride;
    private final java.lang.Object mLock;
    final android.os.IThermalService.Stub mService;
    private int mStatus;
    private android.util.ArrayMap<java.lang.String, android.os.Temperature> mTemperatureMap;
    final com.android.server.power.ThermalManagerService.TemperatureWatcher mTemperatureWatcher;
    private final android.os.RemoteCallbackList<android.os.IThermalEventListener> mThermalEventListeners;
    private final android.os.RemoteCallbackList<android.os.IThermalStatusListener> mThermalStatusListeners;
    private com.android.server.power.ThermalManagerService.ThermalManagerServiceWrapper mTmsWrapper;

    public ThermalManagerService(android.content.Context context) {
        this(context, null);
    }

    ThermalManagerService(android.content.Context context, com.android.server.power.ThermalManagerService.ThermalHalWrapper halWrapper) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mThermalEventListeners = new android.os.RemoteCallbackList<>();
        this.mThermalStatusListeners = new android.os.RemoteCallbackList<>();
        this.mTemperatureMap = new android.util.ArrayMap<>();
        this.mHalReady = new java.util.concurrent.atomic.AtomicBoolean();
        this.mTemperatureWatcher = new com.android.server.power.ThermalManagerService.TemperatureWatcher();
        this.mService = new android.os.IThermalService.Stub() { // from class: com.android.server.power.ThermalManagerService.1
            public boolean registerThermalEventListener(android.os.IThermalEventListener listener) {
                com.android.server.power.ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        if (!com.android.server.power.ThermalManagerService.this.mThermalEventListeners.register(listener, null)) {
                            return false;
                        }
                        com.android.server.power.ThermalManagerService.this.postEventListenerCurrentTemperatures(listener, null);
                        return true;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
            }

            public boolean registerThermalEventListenerWithType(android.os.IThermalEventListener listener, int type) {
                com.android.server.power.ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        if (!com.android.server.power.ThermalManagerService.this.mThermalEventListeners.register(listener, new java.lang.Integer(type))) {
                            return false;
                        }
                        com.android.server.power.ThermalManagerService.this.postEventListenerCurrentTemperatures(listener, new java.lang.Integer(type));
                        return true;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
            }

            public boolean unregisterThermalEventListener(android.os.IThermalEventListener listener) {
                boolean zUnregister;
                com.android.server.power.ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        zUnregister = com.android.server.power.ThermalManagerService.this.mThermalEventListeners.unregister(listener);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
                return zUnregister;
            }

            public android.os.Temperature[] getCurrentTemperatures() {
                com.android.server.power.ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (!com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                        return new android.os.Temperature[0];
                    }
                    java.util.List<android.os.Temperature> curr = com.android.server.power.ThermalManagerService.this.mHalWrapper.getCurrentTemperatures(false, 0);
                    return (android.os.Temperature[]) curr.toArray(new android.os.Temperature[curr.size()]);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public android.os.Temperature[] getCurrentTemperaturesWithType(int type) {
                com.android.server.power.ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (!com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                        return new android.os.Temperature[0];
                    }
                    java.util.List<android.os.Temperature> curr = com.android.server.power.ThermalManagerService.this.mHalWrapper.getCurrentTemperatures(true, type);
                    return (android.os.Temperature[]) curr.toArray(new android.os.Temperature[curr.size()]);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public boolean registerThermalStatusListener(android.os.IThermalStatusListener listener) {
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        if (!com.android.server.power.ThermalManagerService.this.mThermalStatusListeners.register(listener)) {
                            return false;
                        }
                        com.android.server.power.ThermalManagerService.this.postStatusListener(listener);
                        return true;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
            }

            public boolean unregisterThermalStatusListener(android.os.IThermalStatusListener listener) {
                boolean zUnregister;
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        zUnregister = com.android.server.power.ThermalManagerService.this.mThermalStatusListeners.unregister(listener);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
                return zUnregister;
            }

            public int getCurrentThermalStatus() {
                int i;
                int i2;
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        int callingUid = android.os.Binder.getCallingUid();
                        if (com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                            i = 1;
                        } else {
                            i = 2;
                        }
                        com.android.internal.util.FrameworkStatsLog.write(772, callingUid, i, com.android.server.power.ThermalManagerService.thermalSeverityToStatsdStatus(com.android.server.power.ThermalManagerService.this.mStatus));
                        i2 = com.android.server.power.ThermalManagerService.this.mStatus;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(token);
                    }
                }
                return i2;
            }

            public android.os.CoolingDevice[] getCurrentCoolingDevices() {
                com.android.server.power.ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (!com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                        return new android.os.CoolingDevice[0];
                    }
                    java.util.List<android.os.CoolingDevice> devList = com.android.server.power.ThermalManagerService.this.mHalWrapper.getCurrentCoolingDevices(false, 0);
                    return (android.os.CoolingDevice[]) devList.toArray(new android.os.CoolingDevice[devList.size()]);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public android.os.CoolingDevice[] getCurrentCoolingDevicesWithType(int type) {
                com.android.server.power.ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (!com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                        return new android.os.CoolingDevice[0];
                    }
                    java.util.List<android.os.CoolingDevice> devList = com.android.server.power.ThermalManagerService.this.mHalWrapper.getCurrentCoolingDevices(true, type);
                    return (android.os.CoolingDevice[]) devList.toArray(new android.os.CoolingDevice[devList.size()]);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public float getThermalHeadroom(int forecastSeconds) {
                if (!com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                    com.android.internal.util.FrameworkStatsLog.write(773, getCallingUid(), 2, Float.NaN, forecastSeconds);
                    return Float.NaN;
                }
                if (forecastSeconds < 0 || forecastSeconds > 60) {
                    com.android.internal.util.FrameworkStatsLog.write(773, getCallingUid(), 4, Float.NaN, forecastSeconds);
                    return Float.NaN;
                }
                return com.android.server.power.ThermalManagerService.this.mTemperatureWatcher.getForecast(forecastSeconds);
            }

            public float[] getThermalHeadroomThresholds() {
                float[] fArrCopyOf;
                if (!com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                    com.android.internal.util.FrameworkStatsLog.write(774, android.os.Binder.getCallingUid(), 2);
                    throw new java.lang.IllegalStateException("Thermal HAL connection is not initialized");
                }
                if (!com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowThermalHeadroomThresholds()) {
                    com.android.internal.util.FrameworkStatsLog.write(774, android.os.Binder.getCallingUid(), 3);
                    throw new java.lang.UnsupportedOperationException("Thermal headroom thresholds not enabled");
                }
                synchronized (com.android.server.power.ThermalManagerService.this.mTemperatureWatcher.mSamples) {
                    com.android.internal.util.FrameworkStatsLog.write(774, android.os.Binder.getCallingUid(), 1);
                    fArrCopyOf = java.util.Arrays.copyOf(com.android.server.power.ThermalManagerService.this.mTemperatureWatcher.mHeadroomThresholds, com.android.server.power.ThermalManagerService.this.mTemperatureWatcher.mHeadroomThresholds.length);
                }
                return fArrCopyOf;
            }

            protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
                com.android.server.power.ThermalManagerService.this.dumpInternal(fd, pw, args);
            }

            private boolean isCallerShell() {
                int callingUid = android.os.Binder.getCallingUid();
                return callingUid == 2000 || callingUid == 0;
            }

            /* JADX WARN: Multi-variable type inference failed */
            public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
                if (!isCallerShell()) {
                    android.util.Slog.w(com.android.server.power.ThermalManagerService.TAG, "Only shell is allowed to call thermalservice shell commands");
                } else {
                    com.android.server.power.ThermalManagerService.this.new ThermalShellCommand().exec(this, in, out, err, args, callback, resultReceiver);
                }
            }
        };
        this.mTmsWrapper = new com.android.server.power.ThermalManagerService.ThermalManagerServiceWrapper();
        this.mContext = context;
        this.mHalWrapper = halWrapper;
        if (halWrapper != null) {
            halWrapper.setCallback(new com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda0(this));
        }
        this.mStatus = 0;
        ((com.android.server.power.IThermalManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.IThermalManagerServiceExt.class).base(this).create()).init();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("thermalservice", this.mService);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 550) {
            onActivityManagerReady();
        }
        if (phase == 1000) {
            registerStatsCallbacks();
        }
    }

    private void onActivityManagerReady() {
        synchronized (this.mLock) {
            boolean halConnected = this.mHalWrapper != null;
            if (!halConnected) {
                this.mHalWrapper = new com.android.server.power.ThermalManagerService.ThermalHalAidlWrapper(new com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda0(this));
                halConnected = this.mHalWrapper.connectToHal();
            }
            if (!halConnected) {
                this.mHalWrapper = new com.android.server.power.ThermalManagerService.ThermalHal20Wrapper(new com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda0(this));
                halConnected = this.mHalWrapper.connectToHal();
            }
            if (!halConnected) {
                this.mHalWrapper = new com.android.server.power.ThermalManagerService.ThermalHal11Wrapper(new com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda0(this));
                halConnected = this.mHalWrapper.connectToHal();
            }
            if (!halConnected) {
                this.mHalWrapper = new com.android.server.power.ThermalManagerService.ThermalHal10Wrapper(new com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda0(this));
                halConnected = this.mHalWrapper.connectToHal();
            }
            if (!halConnected) {
                android.util.Slog.w(TAG, "No Thermal HAL service on this device");
                return;
            }
            java.util.List<android.os.Temperature> temperatures = this.mHalWrapper.getCurrentTemperatures(false, 0);
            int count = temperatures.size();
            if (count == 0) {
                android.util.Slog.w(TAG, "Thermal HAL reported invalid data, abort connection");
            }
            for (int i = 0; i < count; i++) {
                onTemperatureChanged(temperatures.get(i), false);
            }
            onTemperatureMapChangedLocked();
            this.mTemperatureWatcher.updateThresholds();
            this.mHalReady.set(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postStatusListener(final android.os.IThermalStatusListener listener) {
        boolean thermalCallbackQueued = com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$postStatusListener$0(listener);
            }
        });
        if (!thermalCallbackQueued) {
            android.util.Slog.e(TAG, "Thermal callback failed to queue");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postStatusListener$0(android.os.IThermalStatusListener listener) {
        try {
            listener.onStatusChange(this.mStatus);
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            android.util.Slog.e(TAG, "Thermal callback failed to call", e);
        }
    }

    private void notifyStatusListenersLocked() {
        int length = this.mThermalStatusListeners.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                android.os.IThermalStatusListener listener = (android.os.IThermalStatusListener) this.mThermalStatusListeners.getBroadcastItem(i);
                postStatusListener(listener);
            } finally {
                this.mThermalStatusListeners.finishBroadcast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTemperatureMapChangedLocked() {
        int newStatus = 0;
        int count = this.mTemperatureMap.size();
        for (int i = 0; i < count; i++) {
            android.os.Temperature t = this.mTemperatureMap.valueAt(i);
            if (t.getType() == 3 && t.getStatus() >= newStatus) {
                newStatus = t.getStatus();
            }
        }
        if (!this.mIsStatusOverride) {
            setStatusLocked(newStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStatusLocked(int newStatus) {
        if (newStatus != this.mStatus) {
            this.mStatus = newStatus;
            notifyStatusListenersLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postEventListenerCurrentTemperatures(android.os.IThermalEventListener listener, java.lang.Integer type) {
        synchronized (this.mLock) {
            int count = this.mTemperatureMap.size();
            for (int i = 0; i < count; i++) {
                postEventListener(this.mTemperatureMap.valueAt(i), listener, type);
            }
        }
    }

    private void postEventListener(final android.os.Temperature temperature, final android.os.IThermalEventListener listener, java.lang.Integer type) {
        if (type != null && type.intValue() != temperature.getType()) {
            return;
        }
        boolean thermalCallbackQueued = com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.power.ThermalManagerService.lambda$postEventListener$1(listener, temperature);
            }
        });
        if (!thermalCallbackQueued) {
            android.util.Slog.e(TAG, "Thermal callback failed to queue");
        }
    }

    static /* synthetic */ void lambda$postEventListener$1(android.os.IThermalEventListener listener, android.os.Temperature temperature) {
        try {
            listener.notifyThrottling(temperature);
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            android.util.Slog.e(TAG, "Thermal callback failed to call", e);
        }
    }

    private void notifyEventListenersLocked(android.os.Temperature temperature) {
        int length = this.mThermalEventListeners.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                android.os.IThermalEventListener listener = (android.os.IThermalEventListener) this.mThermalEventListeners.getBroadcastItem(i);
                java.lang.Integer type = (java.lang.Integer) this.mThermalEventListeners.getBroadcastCookie(i);
                postEventListener(temperature, listener, type);
            } catch (java.lang.Throwable th) {
                this.mThermalEventListeners.finishBroadcast();
                throw th;
            }
        }
        this.mThermalEventListeners.finishBroadcast();
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.THERMAL_CHANGED, temperature.getName(), java.lang.Integer.valueOf(temperature.getType()), java.lang.Float.valueOf(temperature.getValue()), java.lang.Integer.valueOf(temperature.getStatus()), java.lang.Integer.valueOf(this.mStatus));
    }

    private void shutdownIfNeeded(android.os.Temperature temperature) {
        if (temperature.getStatus() != 6) {
        }
        android.os.PowerManager powerManager = (android.os.PowerManager) getContext().getSystemService(android.os.PowerManager.class);
        switch (temperature.getType()) {
            case 0:
            case 1:
            case 3:
            case 9:
                powerManager.shutdown(false, "thermal", false);
                break;
            case 2:
                powerManager.shutdown(false, "thermal,battery", false);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTemperatureChanged(android.os.Temperature temperature, boolean sendStatus) {
        if (11 != android.os.ProjectManager.getEngVersion()) {
            shutdownIfNeeded(temperature);
        }
        synchronized (this.mLock) {
            android.os.Temperature old = this.mTemperatureMap.put(temperature.getName(), temperature);
            if (old == null || old.getStatus() != temperature.getStatus()) {
                notifyEventListenersLocked(temperature);
            }
            if (sendStatus) {
                onTemperatureMapChangedLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTemperatureChangedCallback(android.os.Temperature temperature) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            onTemperatureChanged(temperature, true);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void registerStatsCallbacks() {
        android.app.StatsManager statsManager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        if (statsManager != null) {
            statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.THERMAL_HEADROOM_THRESHOLDS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda1
                public final int onPullAtom(int i, java.util.List list) {
                    return this.f$0.onPullAtom(i, list);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
        float[] thresholds;
        if (atomTag == 10201) {
            synchronized (this.mTemperatureWatcher.mSamples) {
                thresholds = java.util.Arrays.copyOf(this.mTemperatureWatcher.mHeadroomThresholds, this.mTemperatureWatcher.mHeadroomThresholds.length);
            }
            data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.THERMAL_HEADROOM_THRESHOLDS, thresholds));
            return 0;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int thermalSeverityToStatsdStatus(int severity) {
        switch (severity) {
        }
        return 0;
    }

    private static void dumpItemsLocked(java.io.PrintWriter pw, java.lang.String prefix, java.util.Collection<?> items) {
        java.util.Iterator<?> it = items.iterator();
        while (it.hasNext()) {
            pw.println(prefix + it.next().toString());
        }
    }

    private static void dumpTemperatureThresholds(java.io.PrintWriter pw, java.lang.String prefix, java.util.List<android.hardware.thermal.TemperatureThreshold> thresholds) {
        for (android.hardware.thermal.TemperatureThreshold threshold : thresholds) {
            pw.println(prefix + "TemperatureThreshold{mType=" + threshold.type + ", mName=" + threshold.name + ", mHotThrottlingThresholds=" + java.util.Arrays.toString(threshold.hotThrottlingThresholds) + ", mColdThrottlingThresholds=" + java.util.Arrays.toString(threshold.coldThrottlingThresholds) + "}");
        }
    }

    void dumpInternal(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpPermission(getContext(), TAG, pw)) {
            return;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                pw.println("IsStatusOverride: " + this.mIsStatusOverride);
                pw.println("ThermalEventListeners:");
                this.mThermalEventListeners.dump(pw, "\t");
                pw.println("ThermalStatusListeners:");
                this.mThermalStatusListeners.dump(pw, "\t");
                pw.println("Thermal Status: " + this.mStatus);
                pw.println("Cached temperatures:");
                dumpItemsLocked(pw, "\t", this.mTemperatureMap.values());
                pw.println("HAL Ready: " + this.mHalReady.get());
                if (this.mHalReady.get()) {
                    pw.println("HAL connection:");
                    this.mHalWrapper.dump(pw, "\t");
                    pw.println("Current temperatures from HAL:");
                    dumpItemsLocked(pw, "\t", this.mHalWrapper.getCurrentTemperatures(false, 0));
                    pw.println("Current cooling devices from HAL:");
                    dumpItemsLocked(pw, "\t", this.mHalWrapper.getCurrentCoolingDevices(false, 0));
                    pw.println("Temperature static thresholds from HAL:");
                    dumpTemperatureThresholds(pw, "\t", this.mHalWrapper.getTemperatureThresholds(false, 0));
                }
            }
            if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowThermalHeadroomThresholds()) {
                synchronized (this.mTemperatureWatcher.mSamples) {
                    pw.println("Temperature headroom thresholds:");
                    pw.println(java.util.Arrays.toString(this.mTemperatureWatcher.mHeadroomThresholds));
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    class ThermalShellCommand extends android.os.ShellCommand {
        ThermalShellCommand() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0038  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int onCommand(java.lang.String r3) {
            /*
                r2 = this;
                if (r3 == 0) goto L4
                r0 = r3
                goto L6
            L4:
                java.lang.String r0 = ""
            L6:
                int r1 = r0.hashCode()
                switch(r1) {
                    case -1114874181: goto L2e;
                    case -61558984: goto L24;
                    case 108404047: goto L19;
                    case 385515795: goto Le;
                    default: goto Ld;
                }
            Ld:
                goto L38
            Le:
                java.lang.String r1 = "override-status"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 1
                goto L39
            L19:
                java.lang.String r1 = "reset"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 2
                goto L39
            L24:
                java.lang.String r1 = "inject-temperature"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 0
                goto L39
            L2e:
                java.lang.String r1 = "headroom"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 3
                goto L39
            L38:
                r0 = -1
            L39:
                switch(r0) {
                    case 0: goto L50;
                    case 1: goto L4b;
                    case 2: goto L46;
                    case 3: goto L41;
                    default: goto L3c;
                }
            L3c:
                int r0 = r2.handleDefaultCommands(r3)
                return r0
            L41:
                int r0 = r2.runHeadroom()
                return r0
            L46:
                int r0 = r2.runReset()
                return r0
            L4b:
                int r0 = r2.runOverrideStatus()
                return r0
            L50:
                int r0 = r2.runInjectTemperature()
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.ThermalManagerService.ThermalShellCommand.onCommand(java.lang.String):int");
        }

        private int runReset() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    com.android.server.power.ThermalManagerService.this.mIsStatusOverride = false;
                    com.android.server.power.ThermalManagerService.this.onTemperatureMapChangedLocked();
                }
                return 0;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:5:0x0021  */
        /* JADX WARN: Removed duplicated region for block: B:97:0x0175  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private int runInjectTemperature() throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 782
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.ThermalManagerService.ThermalShellCommand.runInjectTemperature():int");
        }

        private int runOverrideStatus() {
            java.io.PrintWriter pw;
            long token = android.os.Binder.clearCallingIdentity();
            try {
                pw = getOutPrintWriter();
                int status = java.lang.Integer.parseInt(getNextArgRequired());
                if (!android.os.Temperature.isValidStatus(status)) {
                    pw.println("Invalid status: " + status);
                    return -1;
                }
                synchronized (com.android.server.power.ThermalManagerService.this.mLock) {
                    com.android.server.power.ThermalManagerService.this.mIsStatusOverride = true;
                    com.android.server.power.ThermalManagerService.this.setStatusLocked(status);
                }
                android.os.Binder.restoreCallingIdentity(token);
                return 0;
            } catch (java.lang.RuntimeException ex) {
                pw.println("Error: " + ex.toString());
                return -1;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        private int runHeadroom() {
            java.io.PrintWriter pw;
            long token = android.os.Binder.clearCallingIdentity();
            try {
                pw = getOutPrintWriter();
                int forecastSecs = java.lang.Integer.parseInt(getNextArgRequired());
                if (!com.android.server.power.ThermalManagerService.this.mHalReady.get()) {
                    pw.println("Error: thermal HAL is not ready");
                    return -1;
                }
                if (forecastSecs < 0 || forecastSecs > 60) {
                    pw.println("Error: forecast second input should be in range [0,60]");
                    return -1;
                }
                float headroom = com.android.server.power.ThermalManagerService.this.mTemperatureWatcher.getForecast(forecastSecs);
                pw.println("Headroom in " + forecastSecs + " seconds: " + headroom);
                android.os.Binder.restoreCallingIdentity(token);
                return 0;
            } catch (java.lang.RuntimeException ex) {
                pw.println("Error: " + ex);
                return -1;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("Thermal service (thermalservice) commands:");
            pw.println("  help");
            pw.println("    Print this help text.");
            pw.println("");
            pw.println("  inject-temperature TYPE STATUS NAME [VALUE]");
            pw.println("    injects a new temperature sample for the specified device.");
            pw.println("    type and status strings follow the names in android.os.Temperature.");
            pw.println("  override-status STATUS");
            pw.println("    sets and locks the thermal status of the device to STATUS.");
            pw.println("    status code is defined in android.os.Temperature.");
            pw.println("  reset");
            pw.println("    unlocks the thermal status of the device.");
            pw.println("  headroom FORECAST_SECONDS");
            pw.println("    gets the thermal headroom forecast in specified seconds, from [0,60].");
            pw.println();
        }
    }

    static abstract class ThermalHalWrapper {
        protected static final java.lang.String TAG = com.android.server.power.ThermalManagerService.ThermalHalWrapper.class.getSimpleName();
        protected static final int THERMAL_HAL_DEATH_COOKIE = 5612;
        protected com.android.server.power.ThermalManagerService.ThermalHalWrapper.TemperatureChangedCallback mCallback;
        protected final java.lang.Object mHalLock = new java.lang.Object();

        @java.lang.FunctionalInterface
        interface TemperatureChangedCallback {
            void onValues(android.os.Temperature temperature);
        }

        protected abstract boolean connectToHal();

        protected abstract void dump(java.io.PrintWriter printWriter, java.lang.String str);

        protected abstract java.util.List<android.os.CoolingDevice> getCurrentCoolingDevices(boolean z, int i);

        protected abstract java.util.List<android.os.Temperature> getCurrentTemperatures(boolean z, int i);

        protected abstract java.util.List<android.hardware.thermal.TemperatureThreshold> getTemperatureThresholds(boolean z, int i);

        ThermalHalWrapper() {
        }

        protected void setCallback(com.android.server.power.ThermalManagerService.ThermalHalWrapper.TemperatureChangedCallback cb) {
            this.mCallback = cb;
        }

        protected void resendCurrentTemperatures() {
            synchronized (this.mHalLock) {
                java.util.List<android.os.Temperature> temperatures = getCurrentTemperatures(false, 0);
                int count = temperatures.size();
                for (int i = 0; i < count; i++) {
                    this.mCallback.onValues(temperatures.get(i));
                }
            }
        }

        final class DeathRecipient implements android.os.IHwBinder.DeathRecipient {
            DeathRecipient() {
            }

            public void serviceDied(long cookie) {
                if (cookie == 5612) {
                    android.util.Slog.e(com.android.server.power.ThermalManagerService.ThermalHalWrapper.TAG, "Thermal HAL service died cookie: " + cookie);
                    synchronized (com.android.server.power.ThermalManagerService.ThermalHalWrapper.this.mHalLock) {
                        com.android.server.power.ThermalManagerService.ThermalHalWrapper.this.connectToHal();
                        com.android.server.power.ThermalManagerService.ThermalHalWrapper.this.resendCurrentTemperatures();
                    }
                }
            }
        }
    }

    static class ThermalHalAidlWrapper extends com.android.server.power.ThermalManagerService.ThermalHalWrapper implements android.os.IBinder.DeathRecipient {
        private android.hardware.thermal.IThermal mInstance = null;
        private final android.hardware.thermal.IThermalChangedCallback mThermalChangedCallback = new android.hardware.thermal.IThermalChangedCallback.Stub() { // from class: com.android.server.power.ThermalManagerService.ThermalHalAidlWrapper.1
            public void notifyThrottling(android.hardware.thermal.Temperature temperature) throws android.os.RemoteException {
                android.os.Temperature svcTemperature = new android.os.Temperature(temperature.value, temperature.type, temperature.name, temperature.throttlingStatus);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.power.ThermalManagerService.ThermalHalAidlWrapper.this.mCallback.onValues(svcTemperature);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            public int getInterfaceVersion() throws android.os.RemoteException {
                return 2;
            }

            public java.lang.String getInterfaceHash() throws android.os.RemoteException {
                return "2f49c78011338b42b43d5d0e250d9b520850cc1f";
            }
        };

        ThermalHalAidlWrapper(com.android.server.power.ThermalManagerService.ThermalHalWrapper.TemperatureChangedCallback callback) {
            this.mCallback = callback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.Temperature> getCurrentTemperatures(boolean shouldFilter, int type) {
            synchronized (this.mHalLock) {
                java.util.List<android.os.Temperature> ret = new java.util.ArrayList<>();
                if (this.mInstance == null) {
                    return ret;
                }
                try {
                    try {
                        android.hardware.thermal.Temperature[] halRet = shouldFilter ? this.mInstance.getTemperaturesWithType(type) : this.mInstance.getTemperatures();
                        if (halRet == null) {
                            return ret;
                        }
                        for (android.hardware.thermal.Temperature t : halRet) {
                            if (!android.os.Temperature.isValidStatus(t.throttlingStatus)) {
                                android.util.Slog.e(TAG, "Invalid temperature status " + t.throttlingStatus + " received from AIDL HAL");
                                t.throttlingStatus = 0;
                            }
                            if (!shouldFilter || t.type == type) {
                                ret.add(new android.os.Temperature(t.value, t.type, t.name, t.throttlingStatus));
                            }
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "Couldn't getCurrentTemperatures, reconnecting", e);
                        connectToHal();
                    }
                } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e2) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentCoolingDevices due to invalid status", e2);
                }
                return ret;
            }
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.CoolingDevice> getCurrentCoolingDevices(boolean shouldFilter, int type) {
            android.hardware.thermal.CoolingDevice[] halRet;
            synchronized (this.mHalLock) {
                java.util.List<android.os.CoolingDevice> ret = new java.util.ArrayList<>();
                if (this.mInstance == null) {
                    return ret;
                }
                try {
                    try {
                        if (shouldFilter) {
                            halRet = this.mInstance.getCoolingDevicesWithType(type);
                        } else {
                            halRet = this.mInstance.getCoolingDevices();
                        }
                        if (halRet == null) {
                            return ret;
                        }
                        for (android.hardware.thermal.CoolingDevice t : halRet) {
                            if (!android.os.CoolingDevice.isValidType(t.type)) {
                                android.util.Slog.e(TAG, "Invalid cooling device type " + t.type + " from AIDL HAL");
                            } else if (!shouldFilter || t.type == type) {
                                ret.add(new android.os.CoolingDevice(t.value, t.type, t.name));
                            }
                        }
                    } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e) {
                        android.util.Slog.e(TAG, "Couldn't getCurrentCoolingDevices due to invalid status", e);
                    }
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentCoolingDevices, reconnecting", e2);
                    connectToHal();
                }
                return ret;
            }
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.hardware.thermal.TemperatureThreshold> getTemperatureThresholds(boolean shouldFilter, final int type) {
            synchronized (this.mHalLock) {
                java.util.List<android.hardware.thermal.TemperatureThreshold> ret = new java.util.ArrayList<>();
                if (this.mInstance == null) {
                    return ret;
                }
                try {
                    try {
                        android.hardware.thermal.TemperatureThreshold[] halRet = shouldFilter ? this.mInstance.getTemperatureThresholdsWithType(type) : this.mInstance.getTemperatureThresholds();
                        if (halRet == null) {
                            return ret;
                        }
                        if (shouldFilter) {
                            return (java.util.List) java.util.Arrays.stream(halRet).filter(new java.util.function.Predicate() { // from class: com.android.server.power.ThermalManagerService$ThermalHalAidlWrapper$$ExternalSyntheticLambda0
                                @Override // java.util.function.Predicate
                                public final boolean test(java.lang.Object obj) {
                                    return com.android.server.power.ThermalManagerService.ThermalHalAidlWrapper.lambda$getTemperatureThresholds$0(type, (android.hardware.thermal.TemperatureThreshold) obj);
                                }
                            }).collect(java.util.stream.Collectors.toList());
                        }
                        return java.util.Arrays.asList(halRet);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "Couldn't getTemperatureThresholds, reconnecting...", e);
                        connectToHal();
                        return ret;
                    }
                } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e2) {
                    android.util.Slog.e(TAG, "Couldn't getTemperatureThresholds due to invalid status", e2);
                    return ret;
                }
            }
        }

        static /* synthetic */ boolean lambda$getTemperatureThresholds$0(int type, android.hardware.thermal.TemperatureThreshold t) {
            return t.type == type;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            synchronized (this.mHalLock) {
                android.os.IBinder binder = android.os.Binder.allowBlocking(android.os.ServiceManager.waitForDeclaredService(android.hardware.thermal.IThermal.DESCRIPTOR + "/default"));
                initProxyAndRegisterCallback(binder);
            }
            return this.mInstance != null;
        }

        void initProxyAndRegisterCallback(android.os.IBinder binder) {
            synchronized (this.mHalLock) {
                if (binder != null) {
                    this.mInstance = android.hardware.thermal.IThermal.Stub.asInterface(binder);
                    try {
                        binder.linkToDeath(this, 0);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "Unable to connect IThermal AIDL instance", e);
                        connectToHal();
                    }
                    if (this.mInstance != null) {
                        try {
                            android.util.Slog.i(TAG, "Thermal HAL AIDL service connected with version " + this.mInstance.getInterfaceVersion());
                            registerThermalChangedCallback();
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.e(TAG, "Unable to read interface version from Thermal HAL", e2);
                            connectToHal();
                        }
                    }
                }
            }
        }

        void registerThermalChangedCallback() {
            try {
                this.mInstance.registerThermalChangedCallback(this.mThermalChangedCallback);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unable to connect IThermal AIDL instance", e);
                connectToHal();
            } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e2) {
                android.util.Slog.e(TAG, "Couldn't registerThermalChangedCallback due to invalid status", e2);
            }
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            synchronized (this.mHalLock) {
                pw.print(prefix);
                pw.println("ThermalHAL AIDL 2  connected: " + (this.mInstance != null ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO));
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public synchronized void binderDied() {
            android.util.Slog.w(TAG, "Thermal AIDL HAL died, reconnecting...");
            connectToHal();
        }
    }

    static class ThermalHal10Wrapper extends com.android.server.power.ThermalManagerService.ThermalHalWrapper {
        private android.hardware.thermal.V1_0.IThermal mThermalHal10 = null;

        ThermalHal10Wrapper(com.android.server.power.ThermalManagerService.ThermalHalWrapper.TemperatureChangedCallback callback) {
            this.mCallback = callback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.Temperature> getCurrentTemperatures(final boolean shouldFilter, final int type) {
            synchronized (this.mHalLock) {
                final java.util.List<android.os.Temperature> ret = new java.util.ArrayList<>();
                if (this.mThermalHal10 == null) {
                    return ret;
                }
                try {
                    this.mThermalHal10.getTemperatures(new android.hardware.thermal.V1_0.IThermal.getTemperaturesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal10Wrapper$$ExternalSyntheticLambda1
                        public final void onValues(android.hardware.thermal.V1_0.ThermalStatus thermalStatus, java.util.ArrayList arrayList) {
                            com.android.server.power.ThermalManagerService.ThermalHal10Wrapper.lambda$getCurrentTemperatures$0(shouldFilter, type, ret, thermalStatus, arrayList);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentTemperatures, reconnecting...", e);
                    connectToHal();
                }
                return ret;
            }
        }

        static /* synthetic */ void lambda$getCurrentTemperatures$0(boolean shouldFilter, int type, java.util.List ret, android.hardware.thermal.V1_0.ThermalStatus status, java.util.ArrayList temperatures) {
            if (status.code == 0) {
                java.util.Iterator it = temperatures.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.Temperature temperature = (android.hardware.thermal.V1_0.Temperature) it.next();
                    if (!shouldFilter || type == temperature.type) {
                        ret.add(new android.os.Temperature(temperature.currentValue, temperature.type, temperature.name, 0));
                    }
                }
                return;
            }
            android.util.Slog.e(TAG, "Couldn't get temperatures because of HAL error: " + status.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.CoolingDevice> getCurrentCoolingDevices(final boolean shouldFilter, final int type) {
            synchronized (this.mHalLock) {
                final java.util.List<android.os.CoolingDevice> ret = new java.util.ArrayList<>();
                if (this.mThermalHal10 == null) {
                    return ret;
                }
                try {
                    this.mThermalHal10.getCoolingDevices(new android.hardware.thermal.V1_0.IThermal.getCoolingDevicesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal10Wrapper$$ExternalSyntheticLambda0
                        public final void onValues(android.hardware.thermal.V1_0.ThermalStatus thermalStatus, java.util.ArrayList arrayList) {
                            com.android.server.power.ThermalManagerService.ThermalHal10Wrapper.lambda$getCurrentCoolingDevices$1(shouldFilter, type, ret, thermalStatus, arrayList);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentCoolingDevices, reconnecting...", e);
                    connectToHal();
                }
                return ret;
            }
        }

        static /* synthetic */ void lambda$getCurrentCoolingDevices$1(boolean shouldFilter, int type, java.util.List ret, android.hardware.thermal.V1_0.ThermalStatus status, java.util.ArrayList coolingDevices) {
            if (status.code == 0) {
                java.util.Iterator it = coolingDevices.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.CoolingDevice coolingDevice = (android.hardware.thermal.V1_0.CoolingDevice) it.next();
                    if (!shouldFilter || type == coolingDevice.type) {
                        ret.add(new android.os.CoolingDevice((long) coolingDevice.currentValue, coolingDevice.type, coolingDevice.name));
                    }
                }
                return;
            }
            android.util.Slog.e(TAG, "Couldn't get cooling device because of HAL error: " + status.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.hardware.thermal.TemperatureThreshold> getTemperatureThresholds(boolean shouldFilter, int type) {
            return new java.util.ArrayList();
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            boolean z;
            synchronized (this.mHalLock) {
                z = true;
                try {
                    this.mThermalHal10 = android.hardware.thermal.V1_0.IThermal.getService(true);
                    this.mThermalHal10.linkToDeath(new com.android.server.power.ThermalManagerService.ThermalHalWrapper.DeathRecipient(), 5612L);
                    android.util.Slog.i(TAG, "Thermal HAL 1.0 service connected, no thermal call back will be called due to legacy API.");
                } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
                    android.util.Slog.e(TAG, "Thermal HAL 1.0 service not connected.");
                    this.mThermalHal10 = null;
                }
                if (this.mThermalHal10 == null) {
                    z = false;
                }
            }
            return z;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            synchronized (this.mHalLock) {
                pw.print(prefix);
                pw.println("ThermalHAL 1.0 connected: " + (this.mThermalHal10 != null ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO));
            }
        }
    }

    static class ThermalHal11Wrapper extends com.android.server.power.ThermalManagerService.ThermalHalWrapper {
        private android.hardware.thermal.V1_1.IThermal mThermalHal11 = null;
        private final android.hardware.thermal.V1_1.IThermalCallback.Stub mThermalCallback11 = new android.hardware.thermal.V1_1.IThermalCallback.Stub() { // from class: com.android.server.power.ThermalManagerService.ThermalHal11Wrapper.1
            public void notifyThrottling(boolean isThrottling, android.hardware.thermal.V1_0.Temperature temperature) {
                android.os.Temperature thermalSvcTemp = new android.os.Temperature(temperature.currentValue, temperature.type, temperature.name, isThrottling ? 3 : 0);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.power.ThermalManagerService.ThermalHal11Wrapper.this.mCallback.onValues(thermalSvcTemp);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        };

        ThermalHal11Wrapper(com.android.server.power.ThermalManagerService.ThermalHalWrapper.TemperatureChangedCallback callback) {
            this.mCallback = callback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.Temperature> getCurrentTemperatures(final boolean shouldFilter, final int type) {
            synchronized (this.mHalLock) {
                final java.util.List<android.os.Temperature> ret = new java.util.ArrayList<>();
                if (this.mThermalHal11 == null) {
                    return ret;
                }
                try {
                    this.mThermalHal11.getTemperatures(new android.hardware.thermal.V1_0.IThermal.getTemperaturesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal11Wrapper$$ExternalSyntheticLambda1
                        public final void onValues(android.hardware.thermal.V1_0.ThermalStatus thermalStatus, java.util.ArrayList arrayList) {
                            com.android.server.power.ThermalManagerService.ThermalHal11Wrapper.lambda$getCurrentTemperatures$0(shouldFilter, type, ret, thermalStatus, arrayList);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentTemperatures, reconnecting...", e);
                    connectToHal();
                }
                return ret;
            }
        }

        static /* synthetic */ void lambda$getCurrentTemperatures$0(boolean shouldFilter, int type, java.util.List ret, android.hardware.thermal.V1_0.ThermalStatus status, java.util.ArrayList temperatures) {
            if (status.code == 0) {
                java.util.Iterator it = temperatures.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.Temperature temperature = (android.hardware.thermal.V1_0.Temperature) it.next();
                    if (!shouldFilter || type == temperature.type) {
                        ret.add(new android.os.Temperature(temperature.currentValue, temperature.type, temperature.name, 0));
                    }
                }
                return;
            }
            android.util.Slog.e(TAG, "Couldn't get temperatures because of HAL error: " + status.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.CoolingDevice> getCurrentCoolingDevices(final boolean shouldFilter, final int type) {
            synchronized (this.mHalLock) {
                final java.util.List<android.os.CoolingDevice> ret = new java.util.ArrayList<>();
                if (this.mThermalHal11 == null) {
                    return ret;
                }
                try {
                    this.mThermalHal11.getCoolingDevices(new android.hardware.thermal.V1_0.IThermal.getCoolingDevicesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal11Wrapper$$ExternalSyntheticLambda0
                        public final void onValues(android.hardware.thermal.V1_0.ThermalStatus thermalStatus, java.util.ArrayList arrayList) {
                            com.android.server.power.ThermalManagerService.ThermalHal11Wrapper.lambda$getCurrentCoolingDevices$1(shouldFilter, type, ret, thermalStatus, arrayList);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentCoolingDevices, reconnecting...", e);
                    connectToHal();
                }
                return ret;
            }
        }

        static /* synthetic */ void lambda$getCurrentCoolingDevices$1(boolean shouldFilter, int type, java.util.List ret, android.hardware.thermal.V1_0.ThermalStatus status, java.util.ArrayList coolingDevices) {
            if (status.code == 0) {
                java.util.Iterator it = coolingDevices.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.CoolingDevice coolingDevice = (android.hardware.thermal.V1_0.CoolingDevice) it.next();
                    if (!shouldFilter || type == coolingDevice.type) {
                        ret.add(new android.os.CoolingDevice((long) coolingDevice.currentValue, coolingDevice.type, coolingDevice.name));
                    }
                }
                return;
            }
            android.util.Slog.e(TAG, "Couldn't get cooling device because of HAL error: " + status.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.hardware.thermal.TemperatureThreshold> getTemperatureThresholds(boolean shouldFilter, int type) {
            return new java.util.ArrayList();
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            boolean z;
            synchronized (this.mHalLock) {
                z = true;
                try {
                    this.mThermalHal11 = android.hardware.thermal.V1_1.IThermal.getService(true);
                    this.mThermalHal11.linkToDeath(new com.android.server.power.ThermalManagerService.ThermalHalWrapper.DeathRecipient(), 5612L);
                    this.mThermalHal11.registerThermalCallback(this.mThermalCallback11);
                    android.util.Slog.i(TAG, "Thermal HAL 1.1 service connected, limited thermal functions due to legacy API.");
                } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
                    android.util.Slog.e(TAG, "Thermal HAL 1.1 service not connected.");
                    this.mThermalHal11 = null;
                }
                if (this.mThermalHal11 == null) {
                    z = false;
                }
            }
            return z;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            synchronized (this.mHalLock) {
                pw.print(prefix);
                pw.println("ThermalHAL 1.1 connected: " + (this.mThermalHal11 != null ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO));
            }
        }
    }

    static class ThermalHal20Wrapper extends com.android.server.power.ThermalManagerService.ThermalHalWrapper {
        private android.hardware.thermal.V2_0.IThermal mThermalHal20 = null;
        private final android.hardware.thermal.V2_0.IThermalChangedCallback.Stub mThermalCallback20 = new android.hardware.thermal.V2_0.IThermalChangedCallback.Stub() { // from class: com.android.server.power.ThermalManagerService.ThermalHal20Wrapper.1
            public void notifyThrottling(android.hardware.thermal.V2_0.Temperature temperature) {
                android.os.Temperature thermalSvcTemp = new android.os.Temperature(temperature.value, temperature.type, temperature.name, temperature.throttlingStatus);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.power.ThermalManagerService.ThermalHal20Wrapper.this.mCallback.onValues(thermalSvcTemp);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        };

        ThermalHal20Wrapper(com.android.server.power.ThermalManagerService.ThermalHalWrapper.TemperatureChangedCallback callback) {
            this.mCallback = callback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.Temperature> getCurrentTemperatures(boolean shouldFilter, int type) {
            synchronized (this.mHalLock) {
                final java.util.List<android.os.Temperature> ret = new java.util.ArrayList<>();
                if (this.mThermalHal20 == null) {
                    return ret;
                }
                try {
                    this.mThermalHal20.getCurrentTemperatures(shouldFilter, type, new android.hardware.thermal.V2_0.IThermal.getCurrentTemperaturesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda3
                        public final void onValues(android.hardware.thermal.V1_0.ThermalStatus thermalStatus, java.util.ArrayList arrayList) {
                            com.android.server.power.ThermalManagerService.ThermalHal20Wrapper.lambda$getCurrentTemperatures$0(ret, thermalStatus, arrayList);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentTemperatures, reconnecting...", e);
                    connectToHal();
                }
                return ret;
            }
        }

        static /* synthetic */ void lambda$getCurrentTemperatures$0(java.util.List ret, android.hardware.thermal.V1_0.ThermalStatus status, java.util.ArrayList temperatures) {
            if (status.code == 0) {
                java.util.Iterator it = temperatures.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V2_0.Temperature temperature = (android.hardware.thermal.V2_0.Temperature) it.next();
                    if (!android.os.Temperature.isValidStatus(temperature.throttlingStatus)) {
                        android.util.Slog.e(TAG, "Invalid status data from HAL");
                        temperature.throttlingStatus = 0;
                    }
                    ret.add(new android.os.Temperature(temperature.value, temperature.type, temperature.name, temperature.throttlingStatus));
                }
                return;
            }
            android.util.Slog.e(TAG, "Couldn't get temperatures because of HAL error: " + status.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.os.CoolingDevice> getCurrentCoolingDevices(boolean shouldFilter, int type) {
            synchronized (this.mHalLock) {
                final java.util.List<android.os.CoolingDevice> ret = new java.util.ArrayList<>();
                if (this.mThermalHal20 == null) {
                    return ret;
                }
                try {
                    this.mThermalHal20.getCurrentCoolingDevices(shouldFilter, type, new android.hardware.thermal.V2_0.IThermal.getCurrentCoolingDevicesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda1
                        public final void onValues(android.hardware.thermal.V1_0.ThermalStatus thermalStatus, java.util.ArrayList arrayList) {
                            com.android.server.power.ThermalManagerService.ThermalHal20Wrapper.lambda$getCurrentCoolingDevices$1(ret, thermalStatus, arrayList);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Couldn't getCurrentCoolingDevices, reconnecting...", e);
                    connectToHal();
                }
                return ret;
            }
        }

        static /* synthetic */ void lambda$getCurrentCoolingDevices$1(java.util.List ret, android.hardware.thermal.V1_0.ThermalStatus status, java.util.ArrayList coolingDevices) {
            if (status.code == 0) {
                java.util.Iterator it = coolingDevices.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V2_0.CoolingDevice coolingDevice = (android.hardware.thermal.V2_0.CoolingDevice) it.next();
                    ret.add(new android.os.CoolingDevice(coolingDevice.value, coolingDevice.type, coolingDevice.name));
                }
                return;
            }
            android.util.Slog.e(TAG, "Couldn't get cooling device because of HAL error: " + status.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected java.util.List<android.hardware.thermal.TemperatureThreshold> getTemperatureThresholds(boolean shouldFilter, int type) {
            synchronized (this.mHalLock) {
                final java.util.List<android.hardware.thermal.TemperatureThreshold> ret = new java.util.ArrayList<>();
                if (this.mThermalHal20 == null) {
                    return ret;
                }
                try {
                    this.mThermalHal20.getTemperatureThresholds(shouldFilter, type, new android.hardware.thermal.V2_0.IThermal.getTemperatureThresholdsCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda2
                        public final void onValues(android.hardware.thermal.V1_0.ThermalStatus thermalStatus, java.util.ArrayList arrayList) {
                            this.f$0.lambda$getTemperatureThresholds$2(ret, thermalStatus, arrayList);
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Couldn't getTemperatureThresholds, reconnecting...", e);
                }
                return ret;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getTemperatureThresholds$2(java.util.List ret, android.hardware.thermal.V1_0.ThermalStatus status, java.util.ArrayList thresholds) {
            if (status.code == 0) {
                ret.addAll((java.util.Collection) thresholds.stream().map(new java.util.function.Function() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return this.f$0.convertToAidlTemperatureThreshold((android.hardware.thermal.V2_0.TemperatureThreshold) obj);
                    }
                }).collect(java.util.stream.Collectors.toList()));
            } else {
                android.util.Slog.e(TAG, "Couldn't get temperature thresholds because of HAL error: " + status.debugMessage);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.hardware.thermal.TemperatureThreshold convertToAidlTemperatureThreshold(android.hardware.thermal.V2_0.TemperatureThreshold threshold) {
            android.hardware.thermal.TemperatureThreshold ret = new android.hardware.thermal.TemperatureThreshold();
            ret.name = threshold.name;
            ret.type = threshold.type;
            ret.coldThrottlingThresholds = threshold.coldThrottlingThresholds;
            ret.hotThrottlingThresholds = threshold.hotThrottlingThresholds;
            return ret;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            boolean z;
            synchronized (this.mHalLock) {
                z = true;
                try {
                    this.mThermalHal20 = android.hardware.thermal.V2_0.IThermal.getService(true);
                    this.mThermalHal20.linkToDeath(new com.android.server.power.ThermalManagerService.ThermalHalWrapper.DeathRecipient(), 5612L);
                    this.mThermalHal20.registerThermalChangedCallback(this.mThermalCallback20, false, 0);
                    android.util.Slog.i(TAG, "Thermal HAL 2.0 service connected.");
                } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
                    android.util.Slog.e(TAG, "Thermal HAL 2.0 service not connected.");
                    this.mThermalHal20 = null;
                }
                if (this.mThermalHal20 == null) {
                    z = false;
                }
            }
            return z;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            synchronized (this.mHalLock) {
                pw.print(prefix);
                pw.println("ThermalHAL 2.0 connected: " + (this.mThermalHal20 != null ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO));
            }
        }
    }

    class TemperatureWatcher {
        private static final float DEGREES_BETWEEN_ZERO_AND_ONE = 30.0f;
        private static final int INACTIVITY_THRESHOLD_MILLIS = 10000;
        private static final int MINIMUM_SAMPLE_COUNT = 3;
        private static final int RING_BUFFER_SIZE = 30;
        private final android.os.Handler mHandler = com.android.internal.os.BackgroundThread.getHandler();
        final android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample>> mSamples = new android.util.ArrayMap<>();
        android.util.ArrayMap<java.lang.String, java.lang.Float> mSevereThresholds = new android.util.ArrayMap<>();
        float[] mHeadroomThresholds = new float[7];
        private long mLastForecastCallTimeMillis = 0;
        long mInactivityThresholdMillis = 10000;

        TemperatureWatcher() {
        }

        void updateThresholds() {
            synchronized (this.mSamples) {
                java.util.List<android.hardware.thermal.TemperatureThreshold> thresholds = com.android.server.power.ThermalManagerService.this.mHalWrapper.getTemperatureThresholds(true, 3);
                if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowThermalHeadroomThresholds()) {
                    java.util.Arrays.fill(this.mHeadroomThresholds, Float.NaN);
                }
                for (int t = 0; t < thresholds.size(); t++) {
                    android.hardware.thermal.TemperatureThreshold threshold = thresholds.get(t);
                    if (threshold.hotThrottlingThresholds.length > 3) {
                        float severeThreshold = threshold.hotThrottlingThresholds[3];
                        if (!java.lang.Float.isNaN(severeThreshold)) {
                            this.mSevereThresholds.put(threshold.name, java.lang.Float.valueOf(severeThreshold));
                            if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowThermalHeadroomThresholds()) {
                                for (int severity = 1; severity <= 6; severity++) {
                                    if (severity != 3 && threshold.hotThrottlingThresholds.length > severity) {
                                        updateHeadroomThreshold(severity, threshold.hotThrottlingThresholds[severity], severeThreshold);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        void updateHeadroomThreshold(int severity, float threshold, float severeThreshold) {
            if (!java.lang.Float.isNaN(threshold)) {
                synchronized (this.mSamples) {
                    if (severity == 3) {
                        this.mHeadroomThresholds[severity] = 1.0f;
                        return;
                    }
                    float headroom = normalizeTemperature(threshold, severeThreshold);
                    if (java.lang.Float.isNaN(this.mHeadroomThresholds[severity])) {
                        this.mHeadroomThresholds[severity] = headroom;
                    } else {
                        float lastHeadroom = this.mHeadroomThresholds[severity];
                        this.mHeadroomThresholds[severity] = java.lang.Math.min(lastHeadroom, headroom);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateTemperature() {
            synchronized (this.mSamples) {
                if (android.os.SystemClock.elapsedRealtime() - this.mLastForecastCallTimeMillis < this.mInactivityThresholdMillis) {
                    this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.power.ThermalManagerService$TemperatureWatcher$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.updateTemperature();
                        }
                    }, 1000L);
                    long now = android.os.SystemClock.elapsedRealtime();
                    java.util.List<android.os.Temperature> temperatures = com.android.server.power.ThermalManagerService.this.mHalWrapper.getCurrentTemperatures(true, 3);
                    for (int t = 0; t < temperatures.size(); t++) {
                        android.os.Temperature temperature = temperatures.get(t);
                        if (!java.lang.Float.isNaN(temperature.getValue())) {
                            java.util.ArrayList<com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample> samples = this.mSamples.computeIfAbsent(temperature.getName(), new java.util.function.Function() { // from class: com.android.server.power.ThermalManagerService$TemperatureWatcher$$ExternalSyntheticLambda1
                                @Override // java.util.function.Function
                                public final java.lang.Object apply(java.lang.Object obj) {
                                    return com.android.server.power.ThermalManagerService.TemperatureWatcher.lambda$updateTemperature$0((java.lang.String) obj);
                                }
                            });
                            if (samples.size() == 30) {
                                samples.removeFirst();
                            }
                            samples.add(new com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample(now, temperature.getValue()));
                        }
                    }
                    return;
                }
                this.mSamples.clear();
            }
        }

        static /* synthetic */ java.util.ArrayList lambda$updateTemperature$0(java.lang.String k) {
            return new java.util.ArrayList(30);
        }

        float getSlopeOf(java.util.List<com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample> samples) {
            long sumTimes = 0;
            float sumTemperatures = 0.0f;
            for (int s = 0; s < samples.size(); s++) {
                com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample sample = samples.get(s);
                sumTimes += sample.time;
                sumTemperatures += sample.temperature;
            }
            int s2 = samples.size();
            long meanTime = sumTimes / ((long) s2);
            float meanTemperature = sumTemperatures / samples.size();
            long sampleVariance = 0;
            float sampleCovariance = 0.0f;
            for (int s3 = 0; s3 < samples.size(); s3++) {
                com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample sample2 = samples.get(s3);
                long timeDelta = sample2.time - meanTime;
                float temperatureDelta = sample2.temperature - meanTemperature;
                sampleVariance += timeDelta * timeDelta;
                sampleCovariance += timeDelta * temperatureDelta;
            }
            return sampleCovariance / sampleVariance;
        }

        static float normalizeTemperature(float temperature, float severeThreshold) {
            float zeroNormalized = severeThreshold - DEGREES_BETWEEN_ZERO_AND_ONE;
            if (temperature <= zeroNormalized) {
                return 0.0f;
            }
            float delta = temperature - zeroNormalized;
            return delta / DEGREES_BETWEEN_ZERO_AND_ONE;
        }

        float getForecast(int forecastSeconds) {
            synchronized (this.mSamples) {
                this.mLastForecastCallTimeMillis = android.os.SystemClock.elapsedRealtime();
                if (this.mSamples.isEmpty()) {
                    updateTemperature();
                }
                if (this.mSamples.isEmpty()) {
                    android.util.Slog.e(com.android.server.power.ThermalManagerService.TAG, "No temperature samples found");
                    com.android.internal.util.FrameworkStatsLog.write(773, android.os.Binder.getCallingUid(), 5, Float.NaN, forecastSeconds);
                    return Float.NaN;
                }
                if (this.mSevereThresholds.isEmpty()) {
                    android.util.Slog.e(com.android.server.power.ThermalManagerService.TAG, "No temperature thresholds found");
                    com.android.internal.util.FrameworkStatsLog.write(773, android.os.Binder.getCallingUid(), 6, Float.NaN, forecastSeconds);
                    return Float.NaN;
                }
                float maxNormalized = Float.NaN;
                int noThresholdSampleCount = 0;
                for (java.util.Map.Entry<java.lang.String, java.util.ArrayList<com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample>> entry : this.mSamples.entrySet()) {
                    java.lang.String name = entry.getKey();
                    java.util.ArrayList<com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample> samples = entry.getValue();
                    java.lang.Float threshold = this.mSevereThresholds.get(name);
                    if (threshold == null) {
                        noThresholdSampleCount++;
                        android.util.Slog.e(com.android.server.power.ThermalManagerService.TAG, "No threshold found for " + name);
                    } else {
                        float currentTemperature = ((com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample) samples.getLast()).temperature;
                        if (samples.size() < 3) {
                            float normalized = normalizeTemperature(currentTemperature, threshold.floatValue());
                            if (java.lang.Float.isNaN(maxNormalized) || normalized > maxNormalized) {
                                maxNormalized = normalized;
                            }
                        } else {
                            float slope = getSlopeOf(samples);
                            float normalized2 = normalizeTemperature((forecastSeconds * slope * 1000.0f) + currentTemperature, threshold.floatValue());
                            if (java.lang.Float.isNaN(maxNormalized) || normalized2 > maxNormalized) {
                                maxNormalized = normalized2;
                            }
                        }
                    }
                }
                if (noThresholdSampleCount == this.mSamples.size()) {
                    com.android.internal.util.FrameworkStatsLog.write(773, android.os.Binder.getCallingUid(), 6, Float.NaN, forecastSeconds);
                } else {
                    com.android.internal.util.FrameworkStatsLog.write(773, android.os.Binder.getCallingUid(), 1, maxNormalized, forecastSeconds);
                }
                return maxNormalized;
            }
        }

        com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample createSampleForTesting(long time, float temperature) {
            return new com.android.server.power.ThermalManagerService.TemperatureWatcher.Sample(time, temperature);
        }

        class Sample {
            public float temperature;
            public long time;

            Sample(long time, float temperature) {
                this.time = time;
                this.temperature = temperature;
            }
        }
    }

    public com.android.server.power.IThermalManagerServiceWrapper getWrapper() {
        return this.mTmsWrapper;
    }

    private class ThermalManagerServiceWrapper implements com.android.server.power.IThermalManagerServiceWrapper {
        private ThermalManagerServiceWrapper() {
        }

        @Override // com.android.server.power.IThermalManagerServiceWrapper
        public void updateSkinThermalStatus(int thermalTemp, int status) {
            android.os.Temperature temperature = new android.os.Temperature(thermalTemp, 3, "shell_skin", status);
            com.android.server.power.ThermalManagerService.this.onTemperatureChanged(temperature, true);
        }
    }
}
