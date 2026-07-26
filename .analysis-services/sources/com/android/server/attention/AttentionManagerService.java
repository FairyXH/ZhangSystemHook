package com.android.server.attention;

/* JADX INFO: loaded from: classes.dex */
public class AttentionManagerService extends com.android.server.SystemService {
    protected static final int ATTENTION_CACHE_BUFFER_SIZE = 5;
    private static final long CONNECTION_TTL_MILLIS = 60000;
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    static final long DEFAULT_STALE_AFTER_MILLIS = 1000;
    static final java.lang.String KEY_SERVICE_ENABLED = "service_enabled";
    static final java.lang.String KEY_STALE_AFTER_MILLIS = "stale_after_millis";
    private static final java.lang.String LOG_TAG = "AttentionManagerService";
    private static final long SERVICE_BINDING_WAIT_MILLIS = 1000;
    private static java.lang.String sTestAttentionServicePackage;
    private com.android.server.attention.AttentionManagerService.AttentionCheckCacheBuffer mAttentionCheckCacheBuffer;
    private com.android.server.attention.AttentionManagerService.AttentionHandler mAttentionHandler;
    private boolean mBinding;
    android.content.ComponentName mComponentName;
    private final com.android.server.attention.AttentionManagerService.AttentionServiceConnection mConnection;
    private final android.content.Context mContext;
    com.android.server.attention.AttentionManagerService.AttentionCheck mCurrentAttentionCheck;
    com.android.server.attention.AttentionManagerService.ProximityUpdate mCurrentProximityUpdate;
    boolean mIsProximityEnabled;
    boolean mIsServiceEnabled;
    private final java.lang.Object mLock;
    private final android.os.PowerManager mPowerManager;
    private final android.hardware.SensorPrivacyManager mPrivacyManager;
    protected android.service.attention.IAttentionService mService;
    private java.util.concurrent.CountDownLatch mServiceBindingLatch;
    long mStaleAfterMillis;

    public AttentionManagerService(android.content.Context context) {
        this(context, (android.os.PowerManager) context.getSystemService("power"), new java.lang.Object(), null);
        this.mAttentionHandler = new com.android.server.attention.AttentionManagerService.AttentionHandler();
    }

    AttentionManagerService(android.content.Context context, android.os.PowerManager powerManager, java.lang.Object lock, com.android.server.attention.AttentionManagerService.AttentionHandler handler) {
        super(context);
        this.mConnection = new com.android.server.attention.AttentionManagerService.AttentionServiceConnection();
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mPowerManager = powerManager;
        this.mLock = lock;
        this.mAttentionHandler = handler;
        this.mPrivacyManager = android.hardware.SensorPrivacyManager.getInstance(context);
        this.mServiceBindingLatch = new java.util.concurrent.CountDownLatch(1);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            this.mContext.registerReceiver(new com.android.server.attention.AttentionManagerService.ScreenStateReceiver(), new android.content.IntentFilter("android.intent.action.SCREEN_OFF"));
            readValuesFromDeviceConfig();
            android.provider.DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", android.app.ActivityThread.currentApplication().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.attention.AttentionManagerService$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$onBootPhase$0(properties);
                }
            });
            this.mIsProximityEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_enableMultiUserUI);
            android.util.Slog.i(LOG_TAG, "mIsProximityEnabled is: " + this.mIsProximityEnabled);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("attention", new com.android.server.attention.AttentionManagerService.BinderService());
        publishLocalService(android.attention.AttentionManagerInternal.class, new com.android.server.attention.AttentionManagerService.LocalService());
    }

    public static boolean isServiceConfigured(android.content.Context context) {
        return !android.text.TextUtils.isEmpty(getServiceConfigPackage(context));
    }

    protected boolean isServiceAvailable() {
        if (this.mComponentName == null) {
            this.mComponentName = resolveAttentionService(this.mContext);
        }
        return this.mComponentName != null;
    }

    private boolean getIsServiceEnabled() {
        return android.provider.DeviceConfig.getBoolean("attention_manager_service", KEY_SERVICE_ENABLED, true);
    }

    protected long getStaleAfterMillis() {
        long millis = android.provider.DeviceConfig.getLong("attention_manager_service", KEY_STALE_AFTER_MILLIS, 1000L);
        if (millis < 0 || millis > 10000) {
            android.util.Slog.w(LOG_TAG, "Bad flag value supplied for: stale_after_millis");
            return 1000L;
        }
        return millis;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onDeviceConfigChange(java.util.Set<java.lang.String> r5) {
        /*
            r4 = this;
            java.util.Iterator r0 = r5.iterator()
        L4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L4f
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            int r2 = r1.hashCode()
            switch(r2) {
                case -337803025: goto L23;
                case 1914663863: goto L18;
                default: goto L17;
            }
        L17:
            goto L2e
        L18:
            java.lang.String r2 = "service_enabled"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 0
            goto L2f
        L23:
            java.lang.String r2 = "stale_after_millis"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 1
            goto L2f
        L2e:
            r2 = -1
        L2f:
            switch(r2) {
                case 0: goto L4b;
                case 1: goto L4b;
                default: goto L32;
            }
        L32:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Ignoring change on "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "AttentionManagerService"
            android.util.Slog.i(r3, r2)
            goto L4
        L4b:
            r4.readValuesFromDeviceConfig()
            return
        L4f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.attention.AttentionManagerService.onDeviceConfigChange(java.util.Set):void");
    }

    private void readValuesFromDeviceConfig() {
        this.mIsServiceEnabled = getIsServiceEnabled();
        this.mStaleAfterMillis = getStaleAfterMillis();
        android.util.Slog.i(LOG_TAG, "readValuesFromDeviceConfig():\nmIsServiceEnabled=" + this.mIsServiceEnabled + "\nmStaleAfterMillis=" + this.mStaleAfterMillis);
    }

    boolean checkAttention(long timeout, android.attention.AttentionManagerInternal.AttentionCallbackInternal callbackInternal) {
        java.util.Objects.requireNonNull(callbackInternal);
        if (!this.mIsServiceEnabled) {
            android.util.Slog.w(LOG_TAG, "Trying to call checkAttention() on an unsupported device.");
            return false;
        }
        if (!isServiceAvailable()) {
            android.util.Slog.w(LOG_TAG, "Service is not available at this moment.");
            return false;
        }
        if (this.mPrivacyManager.isSensorPrivacyEnabled(2)) {
            android.util.Slog.w(LOG_TAG, "Camera is locked by a toggle.");
            return false;
        }
        if (!this.mPowerManager.isInteractive() || this.mPowerManager.isPowerSaveMode()) {
            return false;
        }
        synchronized (this.mLock) {
            freeIfInactiveLocked();
            bindLocked();
        }
        long now = android.os.SystemClock.uptimeMillis();
        awaitServiceBinding(java.lang.Math.min(1000L, timeout));
        synchronized (this.mLock) {
            com.android.server.attention.AttentionManagerService.AttentionCheckCache cache = this.mAttentionCheckCacheBuffer == null ? null : this.mAttentionCheckCacheBuffer.getLast();
            if (cache != null && now < cache.mLastComputed + this.mStaleAfterMillis) {
                callbackInternal.onSuccess(cache.mResult, cache.mTimestamp);
                return true;
            }
            if (this.mCurrentAttentionCheck != null && (!this.mCurrentAttentionCheck.mIsDispatched || !this.mCurrentAttentionCheck.mIsFulfilled)) {
                return false;
            }
            this.mCurrentAttentionCheck = new com.android.server.attention.AttentionManagerService.AttentionCheck(callbackInternal, this);
            if (this.mService != null) {
                try {
                    cancelAfterTimeoutLocked(timeout);
                    this.mService.checkAttention(this.mCurrentAttentionCheck.mIAttentionCallback);
                    this.mCurrentAttentionCheck.mIsDispatched = true;
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(LOG_TAG, "Cannot call into the AttentionService");
                    return false;
                }
            }
            return true;
        }
    }

    void cancelAttentionCheck(android.attention.AttentionManagerInternal.AttentionCallbackInternal callbackInternal) {
        synchronized (this.mLock) {
            if (!this.mCurrentAttentionCheck.mCallbackInternal.equals(callbackInternal)) {
                android.util.Slog.w(LOG_TAG, "Cannot cancel a non-current request");
            } else {
                cancel();
            }
        }
    }

    boolean onStartProximityUpdates(android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal callbackInternal) {
        java.util.Objects.requireNonNull(callbackInternal);
        if (!this.mIsProximityEnabled) {
            android.util.Slog.w(LOG_TAG, "Trying to call onProximityUpdate() on an unsupported device.");
            return false;
        }
        if (!isServiceAvailable()) {
            android.util.Slog.w(LOG_TAG, "Service is not available at this moment.");
            return false;
        }
        if (!this.mPowerManager.isInteractive()) {
            android.util.Slog.w(LOG_TAG, "Proximity Service is unavailable during screen off at this moment.");
            return false;
        }
        synchronized (this.mLock) {
            freeIfInactiveLocked();
            bindLocked();
        }
        awaitServiceBinding(1000L);
        synchronized (this.mLock) {
            if (this.mCurrentProximityUpdate != null && this.mCurrentProximityUpdate.mStartedUpdates) {
                if (this.mCurrentProximityUpdate.mCallbackInternal == callbackInternal) {
                    android.util.Slog.w(LOG_TAG, "Provided callback is already registered. Skipping.");
                    return true;
                }
                android.util.Slog.w(LOG_TAG, "New proximity update cannot be processed because there is already an ongoing update");
                return false;
            }
            this.mCurrentProximityUpdate = new com.android.server.attention.AttentionManagerService.ProximityUpdate(callbackInternal);
            return this.mCurrentProximityUpdate.startUpdates();
        }
    }

    void onStopProximityUpdates(android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal callbackInternal) {
        synchronized (this.mLock) {
            if (this.mCurrentProximityUpdate != null && this.mCurrentProximityUpdate.mCallbackInternal.equals(callbackInternal) && this.mCurrentProximityUpdate.mStartedUpdates) {
                this.mCurrentProximityUpdate.cancelUpdates();
                this.mCurrentProximityUpdate = null;
                return;
            }
            android.util.Slog.w(LOG_TAG, "Cannot stop a non-current callback");
        }
    }

    protected void freeIfInactiveLocked() {
        this.mAttentionHandler.removeMessages(1);
        this.mAttentionHandler.sendEmptyMessageDelayed(1, 60000L);
    }

    private void cancelAfterTimeoutLocked(long timeout) {
        this.mAttentionHandler.sendEmptyMessageDelayed(2, timeout);
    }

    private static java.lang.String getServiceConfigPackage(android.content.Context context) {
        return context.getPackageManager().getAttentionServicePackageName();
    }

    private void awaitServiceBinding(long millis) {
        try {
            this.mServiceBindingLatch.await(millis, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.e(LOG_TAG, "Interrupted while waiting to bind Attention Service.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.content.ComponentName resolveAttentionService(android.content.Context context) {
        java.lang.String resolvedPackage;
        java.lang.String serviceConfigPackage = getServiceConfigPackage(context);
        int flags = 1048576;
        if (!android.text.TextUtils.isEmpty(sTestAttentionServicePackage)) {
            resolvedPackage = sTestAttentionServicePackage;
            flags = 128;
        } else {
            if (android.text.TextUtils.isEmpty(serviceConfigPackage)) {
                return null;
            }
            resolvedPackage = serviceConfigPackage;
        }
        android.content.Intent intent = new android.content.Intent("android.service.attention.AttentionService").setPackage(resolvedPackage);
        android.content.pm.ResolveInfo resolveInfo = context.getPackageManager().resolveService(intent, flags);
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            android.util.Slog.wtf(LOG_TAG, java.lang.String.format("Service %s not found in package %s", "android.service.attention.AttentionService", serviceConfigPackage));
            return null;
        }
        android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        java.lang.String permission = serviceInfo.permission;
        if (!"android.permission.BIND_ATTENTION_SERVICE".equals(permission)) {
            android.util.Slog.e(LOG_TAG, java.lang.String.format("Service %s should require %s permission. Found %s permission", serviceInfo.getComponentName(), "android.permission.BIND_ATTENTION_SERVICE", serviceInfo.permission));
            return null;
        }
        return serviceInfo.getComponentName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(com.android.internal.util.IndentingPrintWriter ipw) {
        ipw.println("Attention Manager Service (dumpsys attention) state:\n");
        ipw.println("isServiceEnabled=" + this.mIsServiceEnabled);
        ipw.println("mIsProximityEnabled=" + this.mIsProximityEnabled);
        ipw.println("mStaleAfterMillis=" + this.mStaleAfterMillis);
        ipw.println("AttentionServicePackageName=" + getServiceConfigPackage(this.mContext));
        ipw.println("Resolved component:");
        if (this.mComponentName != null) {
            ipw.increaseIndent();
            ipw.println("Component=" + this.mComponentName.getPackageName());
            ipw.println("Class=" + this.mComponentName.getClassName());
            ipw.decreaseIndent();
        }
        synchronized (this.mLock) {
            ipw.println("binding=" + this.mBinding);
            ipw.println("current attention check:");
            if (this.mCurrentAttentionCheck != null) {
                this.mCurrentAttentionCheck.dump(ipw);
            }
            if (this.mAttentionCheckCacheBuffer != null) {
                this.mAttentionCheckCacheBuffer.dump(ipw);
            }
            if (this.mCurrentProximityUpdate != null) {
                this.mCurrentProximityUpdate.dump(ipw);
            }
        }
    }

    private final class LocalService extends android.attention.AttentionManagerInternal {
        private LocalService() {
        }

        public boolean isAttentionServiceSupported() {
            return com.android.server.attention.AttentionManagerService.this.mIsServiceEnabled;
        }

        public boolean isProximitySupported() {
            return com.android.server.attention.AttentionManagerService.this.mIsProximityEnabled;
        }

        public boolean checkAttention(long timeout, android.attention.AttentionManagerInternal.AttentionCallbackInternal callbackInternal) {
            return com.android.server.attention.AttentionManagerService.this.checkAttention(timeout, callbackInternal);
        }

        public void cancelAttentionCheck(android.attention.AttentionManagerInternal.AttentionCallbackInternal callbackInternal) {
            com.android.server.attention.AttentionManagerService.this.cancelAttentionCheck(callbackInternal);
        }

        public boolean onStartProximityUpdates(android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal callback) {
            return com.android.server.attention.AttentionManagerService.this.onStartProximityUpdates(callback);
        }

        public void onStopProximityUpdates(android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal callback) {
            com.android.server.attention.AttentionManagerService.this.onStopProximityUpdates(callback);
        }
    }

    protected static final class AttentionCheckCacheBuffer {
        private final com.android.server.attention.AttentionManagerService.AttentionCheckCache[] mQueue = new com.android.server.attention.AttentionManagerService.AttentionCheckCache[5];
        private int mStartIndex = 0;
        private int mSize = 0;

        AttentionCheckCacheBuffer() {
        }

        public com.android.server.attention.AttentionManagerService.AttentionCheckCache getLast() {
            int lastIdx = ((this.mStartIndex + this.mSize) - 1) % 5;
            if (this.mSize == 0) {
                return null;
            }
            return this.mQueue[lastIdx];
        }

        public void add(com.android.server.attention.AttentionManagerService.AttentionCheckCache cache) {
            int nextIndex = (this.mStartIndex + this.mSize) % 5;
            this.mQueue[nextIndex] = cache;
            if (this.mSize == 5) {
                this.mStartIndex++;
            } else {
                this.mSize++;
            }
        }

        public com.android.server.attention.AttentionManagerService.AttentionCheckCache get(int offset) {
            if (offset >= this.mSize) {
                return null;
            }
            return this.mQueue[(this.mStartIndex + offset) % 5];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(com.android.internal.util.IndentingPrintWriter ipw) {
            ipw.println("attention check cache:");
            for (int i = 0; i < this.mSize; i++) {
                com.android.server.attention.AttentionManagerService.AttentionCheckCache cache = get(i);
                if (cache != null) {
                    ipw.increaseIndent();
                    ipw.println("timestamp=" + cache.mTimestamp);
                    ipw.println("result=" + cache.mResult);
                    ipw.decreaseIndent();
                }
            }
        }
    }

    protected static final class AttentionCheckCache {
        private final long mLastComputed;
        private final int mResult;
        private final long mTimestamp;

        AttentionCheckCache(long lastComputed, int result, long timestamp) {
            this.mLastComputed = lastComputed;
            this.mResult = result;
            this.mTimestamp = timestamp;
        }
    }

    static final class AttentionCheck {
        private final android.attention.AttentionManagerInternal.AttentionCallbackInternal mCallbackInternal;
        private final android.service.attention.IAttentionCallback mIAttentionCallback;
        private boolean mIsDispatched;
        private boolean mIsFulfilled;

        AttentionCheck(final android.attention.AttentionManagerInternal.AttentionCallbackInternal callbackInternal, final com.android.server.attention.AttentionManagerService service) {
            this.mCallbackInternal = callbackInternal;
            this.mIAttentionCallback = new android.service.attention.IAttentionCallback.Stub() { // from class: com.android.server.attention.AttentionManagerService.AttentionCheck.1
                public void onSuccess(int result, long timestamp) {
                    if (com.android.server.attention.AttentionManagerService.AttentionCheck.this.mIsFulfilled) {
                        return;
                    }
                    com.android.server.attention.AttentionManagerService.AttentionCheck.this.mIsFulfilled = true;
                    callbackInternal.onSuccess(result, timestamp);
                    logStats(result);
                    service.appendResultToAttentionCacheBuffer(new com.android.server.attention.AttentionManagerService.AttentionCheckCache(android.os.SystemClock.uptimeMillis(), result, timestamp));
                }

                public void onFailure(int error) {
                    if (com.android.server.attention.AttentionManagerService.AttentionCheck.this.mIsFulfilled) {
                        return;
                    }
                    com.android.server.attention.AttentionManagerService.AttentionCheck.this.mIsFulfilled = true;
                    callbackInternal.onFailure(error);
                    logStats(error);
                }

                private void logStats(int result) {
                    com.android.internal.util.FrameworkStatsLog.write(143, result);
                }
            };
        }

        void cancelInternal() {
            this.mIsFulfilled = true;
            this.mCallbackInternal.onFailure(3);
        }

        void dump(com.android.internal.util.IndentingPrintWriter ipw) {
            ipw.increaseIndent();
            ipw.println("is dispatched=" + this.mIsDispatched);
            ipw.println("is fulfilled:=" + this.mIsFulfilled);
            ipw.decreaseIndent();
        }
    }

    final class ProximityUpdate {
        private final android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal mCallbackInternal;
        private final android.service.attention.IProximityUpdateCallback mIProximityUpdateCallback;
        private boolean mStartedUpdates;

        ProximityUpdate(android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal callbackInternal) {
            this.mCallbackInternal = callbackInternal;
            this.mIProximityUpdateCallback = new android.service.attention.IProximityUpdateCallback.Stub() { // from class: com.android.server.attention.AttentionManagerService.ProximityUpdate.1
                public void onProximityUpdate(double distance) {
                    com.android.server.attention.AttentionManagerService.ProximityUpdate.this.mCallbackInternal.onProximityUpdate(distance);
                    synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                        com.android.server.attention.AttentionManagerService.this.freeIfInactiveLocked();
                    }
                }
            };
        }

        boolean startUpdates() {
            synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                if (this.mStartedUpdates) {
                    android.util.Slog.w(com.android.server.attention.AttentionManagerService.LOG_TAG, "Already registered to a proximity service.");
                    return false;
                }
                if (com.android.server.attention.AttentionManagerService.this.mService == null) {
                    android.util.Slog.w(com.android.server.attention.AttentionManagerService.LOG_TAG, "There is no service bound. Proximity update request rejected.");
                    return false;
                }
                try {
                    com.android.server.attention.AttentionManagerService.this.mService.onStartProximityUpdates(this.mIProximityUpdateCallback);
                    this.mStartedUpdates = true;
                    return true;
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.attention.AttentionManagerService.LOG_TAG, "Cannot call into the AttentionService", e);
                    return false;
                }
            }
        }

        void cancelUpdates() {
            synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                if (this.mStartedUpdates) {
                    if (com.android.server.attention.AttentionManagerService.this.mService == null) {
                        this.mStartedUpdates = false;
                        return;
                    }
                    try {
                        com.android.server.attention.AttentionManagerService.this.mService.onStopProximityUpdates();
                        this.mStartedUpdates = false;
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.attention.AttentionManagerService.LOG_TAG, "Cannot call into the AttentionService", e);
                    }
                }
            }
        }

        void dump(com.android.internal.util.IndentingPrintWriter ipw) {
            ipw.increaseIndent();
            ipw.println("is StartedUpdates=" + this.mStartedUpdates);
            ipw.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appendResultToAttentionCacheBuffer(com.android.server.attention.AttentionManagerService.AttentionCheckCache cache) {
        synchronized (this.mLock) {
            if (this.mAttentionCheckCacheBuffer == null) {
                this.mAttentionCheckCacheBuffer = new com.android.server.attention.AttentionManagerService.AttentionCheckCacheBuffer();
            }
            this.mAttentionCheckCacheBuffer.add(cache);
        }
    }

    private class AttentionServiceConnection implements android.content.ServiceConnection {
        private AttentionServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            init(android.service.attention.IAttentionService.Stub.asInterface(service));
            com.android.server.attention.AttentionManagerService.this.mServiceBindingLatch.countDown();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            cleanupService();
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(android.content.ComponentName name) {
            cleanupService();
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(android.content.ComponentName name) {
            cleanupService();
        }

        void cleanupService() {
            init(null);
            com.android.server.attention.AttentionManagerService.this.mServiceBindingLatch = new java.util.concurrent.CountDownLatch(1);
        }

        private void init(android.service.attention.IAttentionService service) {
            synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                com.android.server.attention.AttentionManagerService.this.mService = service;
                com.android.server.attention.AttentionManagerService.this.mBinding = false;
                com.android.server.attention.AttentionManagerService.this.handlePendingCallbackLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePendingCallbackLocked() {
        if (this.mCurrentAttentionCheck != null && !this.mCurrentAttentionCheck.mIsDispatched) {
            if (this.mService != null) {
                try {
                    this.mService.checkAttention(this.mCurrentAttentionCheck.mIAttentionCallback);
                    this.mCurrentAttentionCheck.mIsDispatched = true;
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(LOG_TAG, "Cannot call into the AttentionService");
                }
            } else {
                this.mCurrentAttentionCheck.mCallbackInternal.onFailure(2);
            }
        }
        if (this.mCurrentProximityUpdate != null && this.mCurrentProximityUpdate.mStartedUpdates) {
            if (this.mService != null) {
                try {
                    this.mService.onStartProximityUpdates(this.mCurrentProximityUpdate.mIProximityUpdateCallback);
                    return;
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(LOG_TAG, "Cannot call into the AttentionService", e2);
                    return;
                }
            }
            this.mCurrentProximityUpdate.cancelUpdates();
            this.mCurrentProximityUpdate = null;
        }
    }

    protected class AttentionHandler extends android.os.Handler {
        private static final int ATTENTION_CHECK_TIMEOUT = 2;
        private static final int CHECK_CONNECTION_EXPIRATION = 1;

        AttentionHandler() {
            super(android.os.Looper.myLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                        com.android.server.attention.AttentionManagerService.this.cancelAndUnbindLocked();
                        break;
                    }
                    return;
                case 2:
                    synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                        com.android.server.attention.AttentionManagerService.this.cancel();
                        break;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    void cancel() {
        if (this.mCurrentAttentionCheck.mIsFulfilled) {
            return;
        }
        if (this.mService == null) {
            this.mCurrentAttentionCheck.cancelInternal();
            return;
        }
        try {
            this.mService.cancelAttentionCheck(this.mCurrentAttentionCheck.mIAttentionCallback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(LOG_TAG, "Unable to cancel attention check");
            this.mCurrentAttentionCheck.cancelInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAndUnbindLocked() {
        synchronized (this.mLock) {
            if (this.mCurrentAttentionCheck != null) {
                cancel();
            }
            if (this.mCurrentProximityUpdate != null) {
                this.mCurrentProximityUpdate.cancelUpdates();
            }
            if (this.mService == null) {
                return;
            }
            this.mAttentionHandler.post(new java.lang.Runnable() { // from class: com.android.server.attention.AttentionManagerService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$cancelAndUnbindLocked$1();
                }
            });
            this.mConnection.cleanupService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAndUnbindLocked$1() {
        try {
            this.mContext.unbindService(this.mConnection);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(LOG_TAG, "Cannot set mBinding to false", e);
        } catch (java.lang.IllegalStateException e2) {
            android.util.Slog.e(LOG_TAG, "Cannot set mBinding to false", e2);
        }
    }

    private void bindLocked() {
        if (this.mBinding || this.mService != null) {
            return;
        }
        this.mBinding = true;
        this.mAttentionHandler.post(new java.lang.Runnable() { // from class: com.android.server.attention.AttentionManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$bindLocked$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindLocked$2() {
        android.content.Intent serviceIntent = new android.content.Intent("android.service.attention.AttentionService").setComponent(this.mComponentName);
        this.mContext.bindServiceAsUser(serviceIntent, this.mConnection, 67112961, android.os.UserHandle.CURRENT);
    }

    private final class ScreenStateReceiver extends android.content.BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                    com.android.server.attention.AttentionManagerService.this.cancelAndUnbindLocked();
                }
            }
        }
    }

    private final class AttentionManagerServiceShellCommand extends android.os.ShellCommand {
        final com.android.server.attention.AttentionManagerService.AttentionManagerServiceShellCommand.TestableAttentionCallbackInternal mTestableAttentionCallback;
        final com.android.server.attention.AttentionManagerService.AttentionManagerServiceShellCommand.TestableProximityUpdateCallbackInternal mTestableProximityUpdateCallback;

        class TestableAttentionCallbackInternal extends android.attention.AttentionManagerInternal.AttentionCallbackInternal {
            private int mLastCallbackCode = -1;

            TestableAttentionCallbackInternal() {
            }

            public void onSuccess(int result, long timestamp) {
                this.mLastCallbackCode = result;
            }

            public void onFailure(int error) {
                this.mLastCallbackCode = error;
            }

            public void reset() {
                this.mLastCallbackCode = -1;
            }

            public int getLastCallbackCode() {
                return this.mLastCallbackCode;
            }
        }

        private AttentionManagerServiceShellCommand() {
            this.mTestableAttentionCallback = new com.android.server.attention.AttentionManagerService.AttentionManagerServiceShellCommand.TestableAttentionCallbackInternal();
            this.mTestableProximityUpdateCallback = new com.android.server.attention.AttentionManagerService.AttentionManagerServiceShellCommand.TestableProximityUpdateCallbackInternal();
        }

        class TestableProximityUpdateCallbackInternal implements android.attention.AttentionManagerInternal.ProximityUpdateCallbackInternal {
            private double mLastCallbackCode = -1.0d;

            TestableProximityUpdateCallbackInternal() {
            }

            public void onProximityUpdate(double distance) {
                this.mLastCallbackCode = distance;
            }

            public void reset() {
                this.mLastCallbackCode = -1.0d;
            }

            public double getLastCallbackCode() {
                return this.mLastCallbackCode;
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0085  */
        /* JADX WARN: Removed duplicated region for block: B:8:0x0017  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int onCommand(java.lang.String r9) {
            /*
                Method dump skipped, instruction units count: 314
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.attention.AttentionManagerService.AttentionManagerServiceShellCommand.onCommand(java.lang.String):int");
        }

        private int cmdSetTestableAttentionService(java.lang.String testingServicePackage) {
            java.io.PrintWriter out = getOutPrintWriter();
            if (android.text.TextUtils.isEmpty(testingServicePackage)) {
                out.println("false");
                return 0;
            }
            com.android.server.attention.AttentionManagerService.sTestAttentionServicePackage = testingServicePackage;
            resetStates();
            out.println(com.android.server.attention.AttentionManagerService.this.mComponentName != null ? "true" : "false");
            return 0;
        }

        private int cmdClearTestableAttentionService() {
            com.android.server.attention.AttentionManagerService.sTestAttentionServicePackage = "";
            this.mTestableAttentionCallback.reset();
            this.mTestableProximityUpdateCallback.reset();
            resetStates();
            return 0;
        }

        private int cmdCallCheckAttention() {
            java.io.PrintWriter out = getOutPrintWriter();
            boolean calledSuccessfully = com.android.server.attention.AttentionManagerService.this.checkAttention(2000L, this.mTestableAttentionCallback);
            out.println(calledSuccessfully ? "true" : "false");
            return 0;
        }

        private int cmdCallCancelAttention() {
            java.io.PrintWriter out = getOutPrintWriter();
            com.android.server.attention.AttentionManagerService.this.cancelAttentionCheck(this.mTestableAttentionCallback);
            out.println("true");
            return 0;
        }

        private int cmdCallOnStartProximityUpdates() {
            java.io.PrintWriter out = getOutPrintWriter();
            boolean calledSuccessfully = com.android.server.attention.AttentionManagerService.this.onStartProximityUpdates(this.mTestableProximityUpdateCallback);
            out.println(calledSuccessfully ? "true" : "false");
            return 0;
        }

        private int cmdCallOnStopProximityUpdates() {
            java.io.PrintWriter out = getOutPrintWriter();
            com.android.server.attention.AttentionManagerService.this.onStopProximityUpdates(this.mTestableProximityUpdateCallback);
            out.println("true");
            return 0;
        }

        private int cmdResolveAttentionServiceComponent() {
            java.io.PrintWriter out = getOutPrintWriter();
            android.content.ComponentName resolvedComponent = com.android.server.attention.AttentionManagerService.resolveAttentionService(com.android.server.attention.AttentionManagerService.this.mContext);
            out.println(resolvedComponent != null ? resolvedComponent.flattenToShortString() : "");
            return 0;
        }

        private int cmdGetLastTestCallbackCode() {
            java.io.PrintWriter out = getOutPrintWriter();
            out.println(this.mTestableAttentionCallback.getLastCallbackCode());
            return 0;
        }

        private int cmdGetLastTestProximityUpdateCallbackCode() {
            java.io.PrintWriter out = getOutPrintWriter();
            out.println(this.mTestableProximityUpdateCallback.getLastCallbackCode());
            return 0;
        }

        private void resetStates() {
            synchronized (com.android.server.attention.AttentionManagerService.this.mLock) {
                com.android.server.attention.AttentionManagerService.this.mCurrentProximityUpdate = null;
                com.android.server.attention.AttentionManagerService.this.cancelAndUnbindLocked();
            }
            com.android.server.attention.AttentionManagerService.this.mComponentName = com.android.server.attention.AttentionManagerService.resolveAttentionService(com.android.server.attention.AttentionManagerService.this.mContext);
        }

        public void onHelp() {
            java.io.PrintWriter out = getOutPrintWriter();
            out.println("Attention commands: ");
            out.println("  setTestableAttentionService <service_package>: Bind to a custom implementation of attention service");
            out.println("  ---<service_package>:");
            out.println("       := Package containing the Attention Service implementation to bind to");
            out.println("  ---returns:");
            out.println("       := true, if was bound successfully");
            out.println("       := false, if was not bound successfully");
            out.println("  clearTestableAttentionService: Undo custom bindings. Revert to previous behavior");
            out.println("  getAttentionServiceComponent: Get the current service component string");
            out.println("  ---returns:");
            out.println("       := If valid, the component string (in shorten form) for the currently bound service.");
            out.println("       := else, empty string");
            out.println("  call checkAttention: Calls check attention");
            out.println("  ---returns:");
            out.println("       := true, if the call was successfully dispatched to the service implementation. (to see the result, call getLastTestCallbackCode)");
            out.println("       := false, otherwise");
            out.println("  call cancelCheckAttention: Cancels check attention");
            out.println("  call onStartProximityUpdates: Calls onStartProximityUpdates");
            out.println("  ---returns:");
            out.println("       := true, if the request was successfully dispatched to the service implementation. (to see the result, call getLastTestProximityUpdateCallbackCode)");
            out.println("       := false, otherwise");
            out.println("  call onStopProximityUpdates: Cancels proximity updates");
            out.println("  getLastTestCallbackCode");
            out.println("  ---returns:");
            out.println("       := An integer, representing the last callback code received from the bounded implementation. If none, it will return -1");
            out.println("  getLastTestProximityUpdateCallbackCode");
            out.println("  ---returns:");
            out.println("       := A double, representing the last proximity value received from the bounded implementation. If none, it will return -1.0");
        }
    }

    private final class BinderService extends android.os.Binder {
        com.android.server.attention.AttentionManagerService.AttentionManagerServiceShellCommand mAttentionManagerServiceShellCommand;

        private BinderService() {
            this.mAttentionManagerServiceShellCommand = new com.android.server.attention.AttentionManagerService.AttentionManagerServiceShellCommand();
        }

        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            this.mAttentionManagerServiceShellCommand.exec(this, in, out, err, args, callback, resultReceiver);
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.attention.AttentionManagerService.this.mContext, com.android.server.attention.AttentionManagerService.LOG_TAG, pw)) {
                return;
            }
            com.android.server.attention.AttentionManagerService.this.dumpInternal(new com.android.internal.util.IndentingPrintWriter(pw, "  "));
        }
    }
}
