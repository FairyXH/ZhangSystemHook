package com.android.server.sensors;

/* JADX INFO: loaded from: classes3.dex */
public class SensorService extends com.android.server.SystemService {
    private static final java.lang.String START_NATIVE_SENSOR_SERVICE = "StartNativeSensorService";
    private final java.lang.Object mLock;
    private final android.util.ArrayMap<com.android.server.sensors.SensorManagerInternal.ProximityActiveListener, com.android.server.sensors.SensorService.ProximityListenerProxy> mProximityListeners;
    private long mPtr;
    private final java.util.Set<java.lang.Integer> mRuntimeSensorHandles;
    private com.android.server.sensors.ISensorServiceExt mSensorServiceExtImpl;
    private java.util.concurrent.Future<?> mSensorServiceStart;
    private com.android.server.sensors.ISensorServiceWrapper mSensorServiceWrapper;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeCleanUpProxEvents(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long[] nativeGetProximityEvents(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native java.lang.String[] nativeGetProximityOwner(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native java.lang.String[] nativeGetUltrasonicProximityUsage(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeNotifyProxWakeLockAcquired(long j, java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeNotifyProxWakeLockReleased(long j, java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativenotifyApplicationLaunchStage(long j, java.lang.String str, int i, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void registerProximityActiveListenerNative(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int registerRuntimeSensorNative(long j, int i, int i2, java.lang.String str, java.lang.String str2, float f, float f2, float f3, int i3, int i4, int i5, com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback runtimeSensorCallback);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean sendRuntimeSensorEventNative(long j, int i, int i2, long j2, float[] fArr);

    private static native long startSensorServiceNative(com.android.server.sensors.SensorManagerInternal.ProximityActiveListener proximityActiveListener);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void unregisterProximityActiveListenerNative(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void unregisterRuntimeSensorNative(long j, int i);

    public SensorService(android.content.Context ctx) {
        super(ctx);
        this.mLock = new java.lang.Object();
        this.mProximityListeners = new android.util.ArrayMap<>();
        this.mRuntimeSensorHandles = new java.util.HashSet();
        this.mSensorServiceExtImpl = (com.android.server.sensors.ISensorServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.sensors.ISensorServiceExt.class).base(this).create();
        this.mSensorServiceWrapper = new com.android.server.sensors.SensorService.SensorServiceWrapper();
        synchronized (this.mLock) {
            this.mSensorServiceStart = com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.sensors.SensorService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            }, START_NATIVE_SENSOR_SERVICE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        com.android.server.utils.TimingsTraceAndSlog traceLog = com.android.server.utils.TimingsTraceAndSlog.newAsyncLog();
        traceLog.traceBegin(START_NATIVE_SENSOR_SERVICE);
        long ptr = startSensorServiceNative(new com.android.server.sensors.SensorService.ProximityListenerDelegate());
        synchronized (this.mLock) {
            this.mPtr = ptr;
        }
        traceLog.traceEnd();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        com.android.server.LocalServices.addService(com.android.server.sensors.SensorManagerInternal.class, new com.android.server.sensors.SensorService.LocalService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 200) {
            com.android.internal.util.ConcurrentUtils.waitForFutureNoInterrupt(this.mSensorServiceStart, START_NATIVE_SENSOR_SERVICE);
            synchronized (this.mLock) {
                this.mSensorServiceStart = null;
            }
        }
        getWrapper().getExtImpl().onBootPhase(phase);
    }

    class LocalService extends com.android.server.sensors.SensorManagerInternal {
        LocalService() {
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public int createRuntimeSensor(int deviceId, int type, java.lang.String name, java.lang.String vendor2, float maximumRange, float resolution, float power, int minDelay, int maxDelay, int flags, com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback callback) {
            int handle;
            synchronized (com.android.server.sensors.SensorService.this.mLock) {
                handle = com.android.server.sensors.SensorService.registerRuntimeSensorNative(com.android.server.sensors.SensorService.this.mPtr, deviceId, type, name, vendor2, maximumRange, resolution, power, minDelay, maxDelay, flags, callback);
                com.android.server.sensors.SensorService.this.mRuntimeSensorHandles.add(java.lang.Integer.valueOf(handle));
            }
            return handle;
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void removeRuntimeSensor(int handle) {
            synchronized (com.android.server.sensors.SensorService.this.mLock) {
                if (com.android.server.sensors.SensorService.this.mRuntimeSensorHandles.contains(java.lang.Integer.valueOf(handle))) {
                    com.android.server.sensors.SensorService.this.mRuntimeSensorHandles.remove(java.lang.Integer.valueOf(handle));
                    com.android.server.sensors.SensorService.unregisterRuntimeSensorNative(com.android.server.sensors.SensorService.this.mPtr, handle);
                }
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public boolean sendSensorEvent(int handle, int type, long timestampNanos, float[] values) {
            synchronized (com.android.server.sensors.SensorService.this.mLock) {
                if (!com.android.server.sensors.SensorService.this.mRuntimeSensorHandles.contains(java.lang.Integer.valueOf(handle))) {
                    return false;
                }
                return com.android.server.sensors.SensorService.sendRuntimeSensorEventNative(com.android.server.sensors.SensorService.this.mPtr, handle, type, timestampNanos, values);
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void addProximityActiveListener(java.util.concurrent.Executor executor, com.android.server.sensors.SensorManagerInternal.ProximityActiveListener listener) {
            java.util.Objects.requireNonNull(executor, "executor must not be null");
            java.util.Objects.requireNonNull(listener, "listener must not be null");
            android.util.Log.d("SensorService", "addProximityActiveListener");
            com.android.server.sensors.SensorService.ProximityListenerProxy proxy = new com.android.server.sensors.SensorService.ProximityListenerProxy(executor, listener);
            synchronized (com.android.server.sensors.SensorService.this.mLock) {
                if (com.android.server.sensors.SensorService.this.mProximityListeners.containsKey(listener)) {
                    throw new java.lang.IllegalArgumentException("listener already registered");
                }
                com.android.server.sensors.SensorService.this.mProximityListeners.put(listener, proxy);
                if (com.android.server.sensors.SensorService.this.mProximityListeners.size() == 1) {
                    com.android.server.sensors.SensorService.registerProximityActiveListenerNative(com.android.server.sensors.SensorService.this.mPtr);
                }
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void removeProximityActiveListener(com.android.server.sensors.SensorManagerInternal.ProximityActiveListener listener) {
            java.util.Objects.requireNonNull(listener, "listener must not be null");
            android.util.Log.d("SensorService", "removeProximityActiveListener");
            synchronized (com.android.server.sensors.SensorService.this.mLock) {
                com.android.server.sensors.SensorService.ProximityListenerProxy proxy = (com.android.server.sensors.SensorService.ProximityListenerProxy) com.android.server.sensors.SensorService.this.mProximityListeners.remove(listener);
                if (proxy == null) {
                    throw new java.lang.IllegalArgumentException("listener was not registered with sensor service");
                }
                if (com.android.server.sensors.SensorService.this.mProximityListeners.isEmpty()) {
                    com.android.server.sensors.SensorService.unregisterProximityActiveListenerNative(com.android.server.sensors.SensorService.this.mPtr);
                }
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void notifyProxWakeLockAcquired(final java.lang.String packageName) {
            if (!android.text.TextUtils.isEmpty(packageName)) {
                com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.sensors.SensorService$LocalService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$notifyProxWakeLockAcquired$0(packageName);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyProxWakeLockAcquired$0(java.lang.String packageName) {
            com.android.server.sensors.SensorService.nativeNotifyProxWakeLockAcquired(com.android.server.sensors.SensorService.this.mPtr, packageName);
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void notifyProxWakeLockReleased(final java.lang.String packageName) {
            if (!android.text.TextUtils.isEmpty(packageName)) {
                com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.sensors.SensorService$LocalService$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$notifyProxWakeLockReleased$1(packageName);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyProxWakeLockReleased$1(java.lang.String packageName) {
            com.android.server.sensors.SensorService.nativeNotifyProxWakeLockReleased(com.android.server.sensors.SensorService.this.mPtr, packageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyApplicationLaunchStage$2(java.lang.String processName, int pid, int uid, int stage) {
            com.android.server.sensors.SensorService.nativenotifyApplicationLaunchStage(com.android.server.sensors.SensorService.this.mPtr, processName, pid, uid, stage);
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void notifyApplicationLaunchStage(final java.lang.String processName, final int pid, final int uid, final int stage) {
            com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.sensors.SensorService$LocalService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyApplicationLaunchStage$2(processName, pid, uid, stage);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ProximityListenerProxy implements com.android.server.sensors.SensorManagerInternal.ProximityActiveListener {
        private final java.util.concurrent.Executor mExecutor;
        private final com.android.server.sensors.SensorManagerInternal.ProximityActiveListener mListener;

        ProximityListenerProxy(java.util.concurrent.Executor executor, com.android.server.sensors.SensorManagerInternal.ProximityActiveListener listener) {
            this.mExecutor = executor;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProximityActive$0(boolean isActive) {
            this.mListener.onProximityActive(isActive);
        }

        @Override // com.android.server.sensors.SensorManagerInternal.ProximityActiveListener
        public void onProximityActive(final boolean isActive) {
            this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.sensors.SensorService$ProximityListenerProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onProximityActive$0(isActive);
                }
            });
        }
    }

    private class ProximityListenerDelegate implements com.android.server.sensors.SensorManagerInternal.ProximityActiveListener {
        private ProximityListenerDelegate() {
        }

        @Override // com.android.server.sensors.SensorManagerInternal.ProximityActiveListener
        public void onProximityActive(boolean isActive) {
            int i;
            com.android.server.sensors.SensorService.ProximityListenerProxy[] listeners;
            synchronized (com.android.server.sensors.SensorService.this.mLock) {
                listeners = (com.android.server.sensors.SensorService.ProximityListenerProxy[]) com.android.server.sensors.SensorService.this.mProximityListeners.values().toArray(new com.android.server.sensors.SensorService.ProximityListenerProxy[0]);
            }
            for (com.android.server.sensors.SensorService.ProximityListenerProxy listener : listeners) {
                listener.onProximityActive(isActive);
            }
        }
    }

    public com.android.server.sensors.ISensorServiceWrapper getWrapper() {
        return this.mSensorServiceWrapper;
    }

    private class SensorServiceWrapper implements com.android.server.sensors.ISensorServiceWrapper {
        private SensorServiceWrapper() {
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public com.android.server.sensors.ISensorServiceExt getExtImpl() {
            return com.android.server.sensors.SensorService.this.mSensorServiceExtImpl;
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public java.lang.String[] getProximityOwnerInternal() {
            return com.android.server.sensors.SensorService.nativeGetProximityOwner(com.android.server.sensors.SensorService.this.mPtr);
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public long[] getProximityEventsInternal() {
            return com.android.server.sensors.SensorService.nativeGetProximityEvents(com.android.server.sensors.SensorService.this.mPtr);
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public void cleanUpProxEventsInternal() {
            com.android.server.sensors.SensorService.nativeCleanUpProxEvents(com.android.server.sensors.SensorService.this.mPtr);
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public java.lang.String[] getUltrasonicProximityUsage() {
            return com.android.server.sensors.SensorService.nativeGetUltrasonicProximityUsage(com.android.server.sensors.SensorService.this.mPtr);
        }
    }
}
