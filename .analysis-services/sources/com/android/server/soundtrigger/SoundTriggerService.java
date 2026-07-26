package com.android.server.soundtrigger;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerService extends com.android.server.SystemService {
    private static final boolean DEBUG = true;
    private static final int SESSION_MAX_EVENT_SIZE = 128;
    private static final java.lang.String TAG = "SoundTriggerService";
    private android.app.AppOpsManager mAppOpsManager;
    private final android.content.Context mContext;
    private com.android.server.soundtrigger.SoundTriggerDbHelper mDbHelper;
    private final java.util.Deque<com.android.server.utils.EventLogger> mDetachedSessionEventLoggers;
    private final com.android.server.utils.EventLogger mDeviceEventLogger;
    private final com.android.server.soundtrigger.DeviceStateHandler mDeviceStateHandler;
    private final java.util.concurrent.Executor mDeviceStateHandlerExecutor;
    private final com.android.server.soundtrigger.SoundTriggerService.LocalSoundTriggerService mLocalSoundTriggerService;
    private final java.lang.Object mLock;
    private android.media.soundtrigger_middleware.ISoundTriggerMiddlewareService mMiddlewareService;
    private final android.util.ArrayMap<java.lang.String, com.android.server.soundtrigger.SoundTriggerService.NumOps> mNumOpsPerPackage;
    private android.content.pm.PackageManager mPackageManager;
    private com.android.server.soundtrigger.PhoneCallStateHandler mPhoneCallStateHandler;
    private final com.android.server.utils.EventLogger mServiceEventLogger;
    private final com.android.server.soundtrigger.SoundTriggerService.SoundTriggerServiceStub mServiceStub;
    private final java.util.Set<com.android.server.utils.EventLogger> mSessionEventLoggers;
    private java.util.concurrent.atomic.AtomicInteger mSessionIdCounter;
    private final com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker mSoundModelStatTracker;

    class SoundModelStatTracker {
        private final java.util.TreeMap<java.util.UUID, com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker.SoundModelStat> mModelStats = new java.util.TreeMap<>();

        private class SoundModelStat {
            long mStartCount = 0;
            long mTotalTimeMsec = 0;
            long mLastStartTimestampMsec = 0;
            long mLastStopTimestampMsec = 0;
            boolean mIsStarted = false;

            SoundModelStat() {
            }
        }

        SoundModelStatTracker() {
        }

        public synchronized void onStart(java.util.UUID id) {
            com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker.SoundModelStat stat = this.mModelStats.get(id);
            if (stat == null) {
                stat = new com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker.SoundModelStat();
                this.mModelStats.put(id, stat);
            }
            if (stat.mIsStarted) {
                android.util.Slog.w(com.android.server.soundtrigger.SoundTriggerService.TAG, "error onStart(): Model " + id + " already started");
                return;
            }
            stat.mStartCount++;
            stat.mLastStartTimestampMsec = android.os.SystemClock.elapsedRealtime();
            stat.mIsStarted = true;
        }

        public synchronized void onStop(java.util.UUID id) {
            com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker.SoundModelStat stat = this.mModelStats.get(id);
            if (stat == null) {
                android.util.Slog.i(com.android.server.soundtrigger.SoundTriggerService.TAG, "error onStop(): Model " + id + " has no stats available");
            } else {
                if (!stat.mIsStarted) {
                    android.util.Slog.w(com.android.server.soundtrigger.SoundTriggerService.TAG, "error onStop(): Model " + id + " already stopped");
                    return;
                }
                stat.mLastStopTimestampMsec = android.os.SystemClock.elapsedRealtime();
                stat.mTotalTimeMsec += stat.mLastStopTimestampMsec - stat.mLastStartTimestampMsec;
                stat.mIsStarted = false;
            }
        }

        public synchronized void dump(java.io.PrintWriter pw) {
            long curTime = android.os.SystemClock.elapsedRealtime();
            pw.println("Model Stats:");
            for (java.util.Map.Entry<java.util.UUID, com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker.SoundModelStat> entry : this.mModelStats.entrySet()) {
                java.util.UUID uuid = entry.getKey();
                com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker.SoundModelStat stat = entry.getValue();
                long totalTimeMsec = stat.mTotalTimeMsec;
                if (stat.mIsStarted) {
                    totalTimeMsec += curTime - stat.mLastStartTimestampMsec;
                }
                pw.println(uuid + ", total_time(msec)=" + totalTimeMsec + ", total_count=" + stat.mStartCount + ", last_start=" + stat.mLastStartTimestampMsec + ", last_stop=" + stat.mLastStopTimestampMsec);
            }
        }
    }

    public SoundTriggerService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mServiceEventLogger = new com.android.server.utils.EventLogger(256, "Service");
        this.mDeviceEventLogger = new com.android.server.utils.EventLogger(256, "Device Event");
        this.mSessionEventLoggers = java.util.concurrent.ConcurrentHashMap.newKeySet(4);
        this.mDetachedSessionEventLoggers = new java.util.concurrent.LinkedBlockingDeque(4);
        this.mSessionIdCounter = new java.util.concurrent.atomic.AtomicInteger(0);
        this.mNumOpsPerPackage = new android.util.ArrayMap<>();
        this.mDeviceStateHandlerExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
        this.mContext = context;
        this.mServiceStub = new com.android.server.soundtrigger.SoundTriggerService.SoundTriggerServiceStub();
        this.mLocalSoundTriggerService = new com.android.server.soundtrigger.SoundTriggerService.LocalSoundTriggerService(context);
        this.mSoundModelStatTracker = new com.android.server.soundtrigger.SoundTriggerService.SoundModelStatTracker();
        this.mDeviceStateHandler = new com.android.server.soundtrigger.DeviceStateHandler(this.mDeviceStateHandlerExecutor, this.mDeviceEventLogger);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("soundtrigger", this.mServiceStub);
        publishLocalService(com.android.server.SoundTriggerInternal.class, this.mLocalSoundTriggerService);
    }

    private boolean hasCalling() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.calling");
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        android.util.Slog.d(TAG, "onBootPhase: " + phase + " : " + isSafeMode());
        if (600 == phase) {
            this.mDbHelper = new com.android.server.soundtrigger.SoundTriggerDbHelper(this.mContext);
            this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
            this.mPackageManager = this.mContext.getPackageManager();
            final android.os.PowerManager powerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
            this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.soundtrigger.SoundTriggerService.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    if (!"android.os.action.POWER_SAVE_MODE_CHANGED".equals(intent.getAction())) {
                        return;
                    }
                    com.android.server.soundtrigger.SoundTriggerService.this.mDeviceStateHandler.onPowerModeChanged(powerManager.getSoundTriggerPowerSaveMode());
                }
            }, new android.content.IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
            this.mDeviceStateHandler.onPowerModeChanged(powerManager.getSoundTriggerPowerSaveMode());
            if (hasCalling()) {
                this.mPhoneCallStateHandler = new com.android.server.soundtrigger.PhoneCallStateHandler((android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class), (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class), this.mDeviceStateHandler);
            }
        }
        this.mMiddlewareService = android.media.soundtrigger_middleware.ISoundTriggerMiddlewareService.Stub.asInterface(android.os.ServiceManager.waitForService("soundtrigger_middleware"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    /* JADX INFO: renamed from: listUnderlyingModuleProperties, reason: merged with bridge method [inline-methods] */
    public java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> lambda$newSoundTriggerHelper$2(android.media.permission.Identity originatorIdentity) throws android.os.ServiceSpecificException {
        android.media.permission.Identity middlemanIdentity = new android.media.permission.Identity();
        middlemanIdentity.packageName = android.app.ActivityThread.currentOpPackageName();
        try {
            return (java.util.List) java.util.Arrays.stream(this.mMiddlewareService.listModulesAsMiddleman(middlemanIdentity, originatorIdentity)).map(new java.util.function.Function() { // from class: com.android.server.soundtrigger.SoundTriggerService$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.hardware.soundtrigger.ConversionUtil.aidl2apiModuleDescriptor((android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor) obj);
                }
            }).collect(java.util.stream.Collectors.toList());
        } catch (android.os.RemoteException e) {
            throw new android.os.ServiceSpecificException(android.hardware.soundtrigger.SoundTrigger.STATUS_DEAD_OBJECT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.soundtrigger.SoundTriggerHelper newSoundTriggerHelper(android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties, com.android.server.utils.EventLogger eventLogger) {
        return newSoundTriggerHelper(moduleProperties, eventLogger, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public com.android.server.soundtrigger.SoundTriggerHelper newSoundTriggerHelper(android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties, com.android.server.utils.EventLogger eventLogger, final boolean isTrusted) throws android.os.ServiceSpecificException {
        int id;
        final android.media.permission.Identity middlemanIdentity = new android.media.permission.Identity();
        middlemanIdentity.packageName = android.app.ActivityThread.currentOpPackageName();
        final android.media.permission.Identity originatorIdentity = android.media.permission.IdentityContext.getNonNull();
        java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> moduleList = lambda$newSoundTriggerHelper$2(originatorIdentity);
        if (moduleProperties == null) {
            id = -1;
        } else {
            id = moduleProperties.getId();
        }
        final int moduleId = id;
        if (moduleId != -1 && !moduleList.contains(moduleProperties)) {
            throw new java.lang.IllegalArgumentException("Invalid module properties");
        }
        return new com.android.server.soundtrigger.SoundTriggerHelper(this.mContext, eventLogger, new java.util.function.Function() { // from class: com.android.server.soundtrigger.SoundTriggerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$newSoundTriggerHelper$1(moduleId, middlemanIdentity, originatorIdentity, isTrusted, (android.hardware.soundtrigger.SoundTrigger.StatusListener) obj);
            }
        }, moduleId, new java.util.function.Supplier() { // from class: com.android.server.soundtrigger.SoundTriggerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$newSoundTriggerHelper$2(originatorIdentity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.hardware.soundtrigger.SoundTriggerModule lambda$newSoundTriggerHelper$1(int moduleId, android.media.permission.Identity middlemanIdentity, android.media.permission.Identity originatorIdentity, boolean isTrusted, android.hardware.soundtrigger.SoundTrigger.StatusListener statusListener) {
        return new android.hardware.soundtrigger.SoundTriggerModule(this.mMiddlewareService, moduleId, statusListener, android.os.Looper.getMainLooper(), middlemanIdentity, originatorIdentity, isTrusted);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void detachSessionLogger(com.android.server.utils.EventLogger logger) {
        if (!this.mSessionEventLoggers.remove(logger)) {
            return;
        }
        while (!this.mDetachedSessionEventLoggers.offerFirst(logger)) {
            this.mDetachedSessionEventLoggers.pollLast();
        }
    }

    class MyAppOpsListener implements android.app.AppOpsManager.OnOpChangedListener {
        private final java.util.function.Consumer<java.lang.Boolean> mOnOpModeChanged;
        private final android.media.permission.Identity mOriginatorIdentity;

        MyAppOpsListener(android.media.permission.Identity originatorIdentity, java.util.function.Consumer<java.lang.Boolean> onOpModeChanged) {
            this.mOriginatorIdentity = (android.media.permission.Identity) java.util.Objects.requireNonNull(originatorIdentity);
            this.mOnOpModeChanged = (java.util.function.Consumer) java.util.Objects.requireNonNull(onOpModeChanged);
            try {
                int uid = com.android.server.soundtrigger.SoundTriggerService.this.mPackageManager.getPackageUid(this.mOriginatorIdentity.packageName, android.content.pm.PackageManager.PackageInfoFlags.of(4194304L));
                if (!android.os.UserHandle.isSameApp(uid, this.mOriginatorIdentity.uid)) {
                    throw new java.lang.SecurityException("Uid " + this.mOriginatorIdentity.uid + " attempted to spoof package name " + this.mOriginatorIdentity.packageName + " with uid: " + uid);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new java.lang.SecurityException("Package name not found: " + this.mOriginatorIdentity.packageName);
            }
        }

        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(java.lang.String op, java.lang.String packageName) {
            if (java.util.Objects.equals(op, "android:record_audio")) {
                int mode = com.android.server.soundtrigger.SoundTriggerService.this.mAppOpsManager.checkOpNoThrow("android:record_audio", this.mOriginatorIdentity.uid, this.mOriginatorIdentity.packageName);
                this.mOnOpModeChanged.accept(java.lang.Boolean.valueOf(mode == 0));
            }
        }

        void forceOpChangeRefresh() {
            onOpChanged("android:record_audio", this.mOriginatorIdentity.packageName);
        }
    }

    class SoundTriggerServiceStub extends com.android.internal.app.ISoundTriggerService.Stub {
        SoundTriggerServiceStub() {
        }

        public com.android.internal.app.ISoundTriggerSession attachAsOriginator(android.media.permission.Identity originatorIdentity, android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties, android.os.IBinder client) {
            int sessionId = com.android.server.soundtrigger.SoundTriggerService.this.mSessionIdCounter.getAndIncrement();
            com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type.ATTACH, originatorIdentity.packageName + "#" + sessionId));
            android.media.permission.SafeCloseable ignored = android.media.permission.PermissionUtil.establishIdentityDirect(originatorIdentity);
            try {
                com.android.server.utils.EventLogger eventLogger = new com.android.server.utils.EventLogger(128, "SoundTriggerSessionLogs for package: " + ((java.lang.String) java.util.Objects.requireNonNull(originatorIdentity.packageName)) + "#" + sessionId + " - " + originatorIdentity.uid + "|" + originatorIdentity.pid);
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub soundTriggerSessionStub = com.android.server.soundtrigger.SoundTriggerService.this.new SoundTriggerSessionStub(client, com.android.server.soundtrigger.SoundTriggerService.this.newSoundTriggerHelper(moduleProperties, eventLogger), eventLogger);
                if (ignored != null) {
                    ignored.close();
                }
                return soundTriggerSessionStub;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public com.android.internal.app.ISoundTriggerSession attachAsMiddleman(android.media.permission.Identity originatorIdentity, android.media.permission.Identity middlemanIdentity, android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties, android.os.IBinder client) {
            int sessionId = com.android.server.soundtrigger.SoundTriggerService.this.mSessionIdCounter.getAndIncrement();
            com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type.ATTACH, originatorIdentity.packageName + "#" + sessionId));
            android.media.permission.SafeCloseable ignored = android.media.permission.PermissionUtil.establishIdentityIndirect(com.android.server.soundtrigger.SoundTriggerService.this.mContext, "android.permission.SOUNDTRIGGER_DELEGATE_IDENTITY", middlemanIdentity, originatorIdentity);
            try {
                com.android.server.utils.EventLogger eventLogger = new com.android.server.utils.EventLogger(128, "SoundTriggerSessionLogs for package: " + ((java.lang.String) java.util.Objects.requireNonNull(originatorIdentity.packageName)) + "#" + sessionId + " - " + originatorIdentity.uid + "|" + originatorIdentity.pid);
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub soundTriggerSessionStub = com.android.server.soundtrigger.SoundTriggerService.this.new SoundTriggerSessionStub(client, com.android.server.soundtrigger.SoundTriggerService.this.newSoundTriggerHelper(moduleProperties, eventLogger), eventLogger);
                if (ignored != null) {
                    ignored.close();
                }
                return soundTriggerSessionStub;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> listModuleProperties(android.media.permission.Identity originatorIdentity) {
            com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type.LIST_MODULE, originatorIdentity.packageName));
            android.media.permission.SafeCloseable ignored = android.media.permission.PermissionUtil.establishIdentityDirect(originatorIdentity);
            try {
                java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> listLambda$newSoundTriggerHelper$2 = com.android.server.soundtrigger.SoundTriggerService.this.lambda$newSoundTriggerHelper$2(originatorIdentity);
                if (ignored != null) {
                    ignored.close();
                }
                return listLambda$newSoundTriggerHelper$2;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void attachInjection(android.media.soundtrigger_middleware.ISoundTriggerInjection injection) {
            if (android.content.PermissionChecker.checkCallingPermissionForPreflight(com.android.server.soundtrigger.SoundTriggerService.this.mContext, "android.permission.MANAGE_SOUND_TRIGGER", (java.lang.String) null) != 0) {
                throw new java.lang.SecurityException();
            }
            try {
                android.media.soundtrigger_middleware.ISoundTriggerMiddlewareService.Stub.asInterface(android.os.ServiceManager.waitForService("soundtrigger_middleware")).attachFakeHalInjection(injection);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void setInPhoneCallState(boolean isInPhoneCall) {
            android.util.Slog.i(com.android.server.soundtrigger.SoundTriggerService.TAG, "Overriding phone call state: " + isInPhoneCall);
            com.android.server.soundtrigger.SoundTriggerService.this.mDeviceStateHandler.onPhoneCallStateChanged(isInPhoneCall);
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.soundtrigger.SoundTriggerService.this.mContext, com.android.server.soundtrigger.SoundTriggerService.TAG, pw)) {
                pw.println("##Service-Wide logs:");
                com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.dump(pw, "  ");
                pw.println("\n##Device state logs:");
                com.android.server.soundtrigger.SoundTriggerService.this.mDeviceStateHandler.dump(pw);
                com.android.server.soundtrigger.SoundTriggerService.this.mDeviceEventLogger.dump(pw, "  ");
                pw.println("\n##Active Session dumps:\n");
                for (com.android.server.utils.EventLogger sessionLogger : com.android.server.soundtrigger.SoundTriggerService.this.mSessionEventLoggers) {
                    sessionLogger.dump(pw, "  ");
                    pw.println("");
                }
                pw.println("##Detached Session dumps:\n");
                for (com.android.server.utils.EventLogger sessionLogger2 : com.android.server.soundtrigger.SoundTriggerService.this.mDetachedSessionEventLoggers) {
                    sessionLogger2.dump(pw, "  ");
                    pw.println("");
                }
                pw.println("##Enrolled db dump:\n");
                com.android.server.soundtrigger.SoundTriggerService.this.mDbHelper.dump(pw);
                pw.println("\n##Sound Model Stats dump:\n");
                com.android.server.soundtrigger.SoundTriggerService.this.mSoundModelStatTracker.dump(pw);
            }
        }
    }

    class SoundTriggerSessionStub extends com.android.internal.app.ISoundTriggerSession.Stub {
        private final com.android.server.soundtrigger.SoundTriggerService.MyAppOpsListener mAppOpsListener;
        private final android.os.IBinder mClient;
        private final com.android.server.utils.EventLogger mEventLogger;
        private final com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener mListener;
        private final com.android.server.soundtrigger.SoundTriggerHelper mSoundTriggerHelper;
        private final java.util.TreeMap<java.util.UUID, android.hardware.soundtrigger.SoundTrigger.SoundModel> mLoadedModels = new java.util.TreeMap<>();
        private final java.lang.Object mCallbacksLock = new java.lang.Object();
        private final java.util.TreeMap<java.util.UUID, android.hardware.soundtrigger.IRecognitionStatusCallback> mCallbacks = new java.util.TreeMap<>();
        private final android.media.permission.Identity mOriginatorIdentity = android.media.permission.IdentityContext.getNonNull();

        SoundTriggerSessionStub(android.os.IBinder client, com.android.server.soundtrigger.SoundTriggerHelper soundTriggerHelper, com.android.server.utils.EventLogger eventLogger) {
            this.mSoundTriggerHelper = soundTriggerHelper;
            this.mClient = client;
            this.mEventLogger = eventLogger;
            com.android.server.soundtrigger.SoundTriggerService.this.mSessionEventLoggers.add(this.mEventLogger);
            try {
                this.mClient.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        this.f$0.lambda$new$0();
                    }
                }, 0);
            } catch (android.os.RemoteException e) {
                lambda$new$0();
            }
            this.mListener = new com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$$ExternalSyntheticLambda1
                @Override // com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener
                public final void onSoundTriggerDeviceStateUpdate(com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState) {
                    this.f$0.lambda$new$1(soundTriggerDeviceState);
                }
            };
            android.media.permission.Identity identity = this.mOriginatorIdentity;
            com.android.server.soundtrigger.SoundTriggerHelper soundTriggerHelper2 = this.mSoundTriggerHelper;
            java.util.Objects.requireNonNull(soundTriggerHelper2);
            this.mAppOpsListener = com.android.server.soundtrigger.SoundTriggerService.this.new MyAppOpsListener(identity, new com.android.server.soundtrigger.SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda2(soundTriggerHelper2));
            this.mAppOpsListener.forceOpChangeRefresh();
            com.android.server.soundtrigger.SoundTriggerService.this.mAppOpsManager.startWatchingMode("android:record_audio", this.mOriginatorIdentity.packageName, 1, this.mAppOpsListener);
            com.android.server.soundtrigger.SoundTriggerService.this.mDeviceStateHandler.registerListener(this.mListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState state) {
            this.mSoundTriggerHelper.onDeviceStateChanged(state);
        }

        public int startRecognition(android.hardware.soundtrigger.SoundTrigger.GenericSoundModel soundModel, android.hardware.soundtrigger.IRecognitionStatusCallback callback, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig config, boolean runInBatterySaverMode) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                if (soundModel == null) {
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel), "Invalid sound model").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                    if (ignored != null) {
                        ignored.close();
                        return Integer.MIN_VALUE;
                    }
                    return Integer.MIN_VALUE;
                }
                if (runInBatterySaverMode) {
                    enforceCallingPermission("android.permission.SOUND_TRIGGER_RUN_IN_BATTERY_SAVER");
                }
                int ret = this.mSoundTriggerHelper.startGenericRecognition(soundModel.getUuid(), soundModel, callback, config, runInBatterySaverMode);
                if (ret == 0) {
                    com.android.server.soundtrigger.SoundTriggerService.this.mSoundModelStatTracker.onStart(soundModel.getUuid());
                }
                if (ignored != null) {
                    ignored.close();
                }
                return ret;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int stopRecognition(android.os.ParcelUuid parcelUuid, android.hardware.soundtrigger.IRecognitionStatusCallback callback) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION, getUuid(parcelUuid)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                int ret = this.mSoundTriggerHelper.stopGenericRecognition(parcelUuid.getUuid(), callback);
                if (ret == 0) {
                    com.android.server.soundtrigger.SoundTriggerService.this.mSoundModelStatTracker.onStop(parcelUuid.getUuid());
                }
                if (ignored != null) {
                    ignored.close();
                }
                return ret;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public android.hardware.soundtrigger.SoundTrigger.GenericSoundModel getSoundModel(android.os.ParcelUuid soundModelId) {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                android.hardware.soundtrigger.SoundTrigger.GenericSoundModel model = com.android.server.soundtrigger.SoundTriggerService.this.mDbHelper.getGenericSoundModel(soundModelId.getUuid());
                if (ignored != null) {
                    ignored.close();
                }
                return model;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void updateSoundModel(android.hardware.soundtrigger.SoundTrigger.GenericSoundModel soundModel) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.UPDATE_MODEL, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                com.android.server.soundtrigger.SoundTriggerService.this.mDbHelper.updateGenericSoundModel(soundModel);
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void deleteSoundModel(android.os.ParcelUuid soundModelId) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.DELETE_MODEL, getUuid(soundModelId)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                this.mSoundTriggerHelper.unloadGenericSoundModel(soundModelId.getUuid());
                com.android.server.soundtrigger.SoundTriggerService.this.mSoundModelStatTracker.onStop(soundModelId.getUuid());
                com.android.server.soundtrigger.SoundTriggerService.this.mDbHelper.deleteGenericSoundModel(soundModelId.getUuid());
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int loadGenericSoundModel(android.hardware.soundtrigger.SoundTrigger.GenericSoundModel soundModel) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.LOAD_MODEL, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                if (soundModel != null && soundModel.getUuid() != null) {
                    synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                        android.hardware.soundtrigger.SoundTrigger.SoundModel oldModel = this.mLoadedModels.get(soundModel.getUuid());
                        if (oldModel != null && !oldModel.equals(soundModel)) {
                            this.mSoundTriggerHelper.unloadGenericSoundModel(soundModel.getUuid());
                            synchronized (this.mCallbacksLock) {
                                this.mCallbacks.remove(soundModel.getUuid());
                            }
                        }
                        this.mLoadedModels.put(soundModel.getUuid(), soundModel);
                    }
                    if (ignored != null) {
                        ignored.close();
                        return 0;
                    }
                    return 0;
                }
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.LOAD_MODEL, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel), "Invalid sound model").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                if (ignored != null) {
                    ignored.close();
                    return Integer.MIN_VALUE;
                }
                return Integer.MIN_VALUE;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int loadKeyphraseSoundModel(android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel soundModel) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.LOAD_MODEL, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                if (soundModel != null && soundModel.getUuid() != null) {
                    if (soundModel.getKeyphrases() != null && soundModel.getKeyphrases().length == 1) {
                        synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                            android.hardware.soundtrigger.SoundTrigger.SoundModel oldModel = this.mLoadedModels.get(soundModel.getUuid());
                            if (oldModel != null && !oldModel.equals(soundModel)) {
                                this.mSoundTriggerHelper.unloadKeyphraseSoundModel(soundModel.getKeyphrases()[0].getId());
                                synchronized (this.mCallbacksLock) {
                                    this.mCallbacks.remove(soundModel.getUuid());
                                }
                            }
                            this.mLoadedModels.put(soundModel.getUuid(), soundModel);
                        }
                        if (ignored != null) {
                            ignored.close();
                        }
                        return 0;
                    }
                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.LOAD_MODEL, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel), "Only one keyphrase supported").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                    if (ignored != null) {
                        ignored.close();
                    }
                    return Integer.MIN_VALUE;
                }
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.LOAD_MODEL, getUuid((android.hardware.soundtrigger.SoundTrigger.SoundModel) soundModel), "Invalid sound model").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                if (ignored != null) {
                    ignored.close();
                }
                return Integer.MIN_VALUE;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int startRecognitionForService(android.os.ParcelUuid soundModelId, android.os.Bundle params, android.content.ComponentName detectionService, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig config) throws java.lang.Throwable {
            android.hardware.soundtrigger.IRecognitionStatusCallback existingCallback;
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION_SERVICE, getUuid(soundModelId)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                java.util.Objects.requireNonNull(soundModelId);
                java.util.Objects.requireNonNull(detectionService);
                java.util.Objects.requireNonNull(config);
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                try {
                    enforceDetectionPermissions(detectionService);
                    android.hardware.soundtrigger.IRecognitionStatusCallback callback = new com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService(soundModelId.getUuid(), params, detectionService, android.os.Binder.getCallingUserHandle(), config);
                    synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                        android.hardware.soundtrigger.SoundTrigger.GenericSoundModel genericSoundModel = (android.hardware.soundtrigger.SoundTrigger.SoundModel) this.mLoadedModels.get(soundModelId.getUuid());
                        if (genericSoundModel == null) {
                            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION_SERVICE, getUuid(soundModelId), "Model not loaded").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                            if (ignored != null) {
                                ignored.close();
                            }
                            return Integer.MIN_VALUE;
                        }
                        synchronized (this.mCallbacksLock) {
                            existingCallback = this.mCallbacks.get(soundModelId.getUuid());
                        }
                        if (existingCallback != null) {
                            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION_SERVICE, getUuid(soundModelId), "Model already running").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                            if (ignored != null) {
                                ignored.close();
                            }
                            return Integer.MIN_VALUE;
                        }
                        switch (genericSoundModel.getType()) {
                            case 1:
                                int ret = this.mSoundTriggerHelper.startGenericRecognition(genericSoundModel.getUuid(), genericSoundModel, callback, config, false);
                                if (ret != 0) {
                                    this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION_SERVICE, getUuid(soundModelId), "Model start fail").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                                    if (ignored != null) {
                                        ignored.close();
                                    }
                                    return ret;
                                }
                                synchronized (this.mCallbacksLock) {
                                    this.mCallbacks.put(soundModelId.getUuid(), callback);
                                    break;
                                }
                                com.android.server.soundtrigger.SoundTriggerService.this.mSoundModelStatTracker.onStart(soundModelId.getUuid());
                                if (ignored == null) {
                                    return 0;
                                }
                                ignored.close();
                                return 0;
                            default:
                                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION_SERVICE, getUuid(soundModelId), "Unsupported model type").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                                if (ignored != null) {
                                    ignored.close();
                                }
                                return Integer.MIN_VALUE;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    java.lang.Throwable th2 = th;
                    if (ignored == null) {
                        throw th2;
                    }
                    try {
                        ignored.close();
                        throw th2;
                    } catch (java.lang.Throwable th3) {
                        th2.addSuppressed(th3);
                        throw th2;
                    }
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }

        public int stopRecognitionForService(android.os.ParcelUuid soundModelId) {
            android.hardware.soundtrigger.IRecognitionStatusCallback callback;
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION_SERVICE, getUuid(soundModelId)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    android.hardware.soundtrigger.SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(soundModelId.getUuid());
                    if (soundModel == null) {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION_SERVICE, getUuid(soundModelId), "Model not loaded").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                        if (ignored != null) {
                            ignored.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    synchronized (this.mCallbacksLock) {
                        callback = this.mCallbacks.get(soundModelId.getUuid());
                    }
                    if (callback == null) {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION_SERVICE, getUuid(soundModelId), "Model not running").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                        if (ignored != null) {
                            ignored.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    switch (soundModel.getType()) {
                        case 1:
                            int ret = this.mSoundTriggerHelper.stopGenericRecognition(soundModel.getUuid(), callback);
                            if (ret != 0) {
                                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION_SERVICE, getUuid(soundModelId), "Failed to stop model").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                                if (ignored != null) {
                                    ignored.close();
                                }
                                return ret;
                            }
                            synchronized (this.mCallbacksLock) {
                                this.mCallbacks.remove(soundModelId.getUuid());
                                break;
                            }
                            com.android.server.soundtrigger.SoundTriggerService.this.mSoundModelStatTracker.onStop(soundModelId.getUuid());
                            if (ignored == null) {
                                return 0;
                            }
                            ignored.close();
                            return 0;
                        default:
                            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION_SERVICE, getUuid(soundModelId), "Unknown model type").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                            if (ignored != null) {
                                ignored.close();
                            }
                            return Integer.MIN_VALUE;
                    }
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int unloadSoundModel(android.os.ParcelUuid soundModelId) {
            int ret;
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.UNLOAD_MODEL, getUuid(soundModelId)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel keyphraseSoundModel = (android.hardware.soundtrigger.SoundTrigger.SoundModel) this.mLoadedModels.get(soundModelId.getUuid());
                    if (keyphraseSoundModel == null) {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.UNLOAD_MODEL, getUuid(soundModelId), "Model not loaded").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                        if (ignored != null) {
                            ignored.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    switch (keyphraseSoundModel.getType()) {
                        case 0:
                            ret = this.mSoundTriggerHelper.unloadKeyphraseSoundModel(keyphraseSoundModel.getKeyphrases()[0].getId());
                            break;
                        case 1:
                            ret = this.mSoundTriggerHelper.unloadGenericSoundModel(keyphraseSoundModel.getUuid());
                            break;
                        default:
                            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.UNLOAD_MODEL, getUuid(soundModelId), "Unknown model type").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                            if (ignored != null) {
                                ignored.close();
                            }
                            return Integer.MIN_VALUE;
                    }
                    if (ret != 0) {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.UNLOAD_MODEL, getUuid(soundModelId), "Failed to unload model").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                        if (ignored != null) {
                            ignored.close();
                        }
                        return ret;
                    }
                    this.mLoadedModels.remove(soundModelId.getUuid());
                    if (ignored != null) {
                        ignored.close();
                    }
                    return 0;
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public boolean isRecognitionActive(android.os.ParcelUuid parcelUuid) {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (this.mCallbacksLock) {
                    android.hardware.soundtrigger.IRecognitionStatusCallback callback = this.mCallbacks.get(parcelUuid.getUuid());
                    if (callback != null) {
                        boolean zIsRecognitionRequested = this.mSoundTriggerHelper.isRecognitionRequested(parcelUuid.getUuid());
                        if (ignored != null) {
                            ignored.close();
                        }
                        return zIsRecognitionRequested;
                    }
                    if (ignored != null) {
                        ignored.close();
                        return false;
                    }
                    return false;
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int getModelState(android.os.ParcelUuid soundModelId) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.GET_MODEL_STATE, getUuid(soundModelId)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                int ret = Integer.MIN_VALUE;
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    android.hardware.soundtrigger.SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(soundModelId.getUuid());
                    if (soundModel == null) {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.GET_MODEL_STATE, getUuid(soundModelId), "Model is not loaded").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                        if (ignored != null) {
                            ignored.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    switch (soundModel.getType()) {
                        case 1:
                            ret = this.mSoundTriggerHelper.getGenericModelState(soundModel.getUuid());
                            break;
                        default:
                            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.GET_MODEL_STATE, getUuid(soundModelId), "Unsupported model type").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                            break;
                    }
                    if (ignored != null) {
                        ignored.close();
                    }
                    return ret;
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public android.hardware.soundtrigger.SoundTrigger.ModuleProperties getModuleProperties() {
            android.hardware.soundtrigger.SoundTrigger.ModuleProperties properties;
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.GET_MODULE_PROPERTIES, null));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    properties = this.mSoundTriggerHelper.getModuleProperties();
                }
                if (ignored != null) {
                    ignored.close();
                }
                return properties;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int setParameter(android.os.ParcelUuid soundModelId, int modelParam, int value) {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.SET_PARAMETER, getUuid(soundModelId)));
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    android.hardware.soundtrigger.SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(soundModelId.getUuid());
                    if (soundModel == null) {
                        this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.SET_PARAMETER, getUuid(soundModelId), "Model not loaded").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                        int i = android.hardware.soundtrigger.SoundTrigger.STATUS_BAD_VALUE;
                        if (ignored != null) {
                            ignored.close();
                        }
                        return i;
                    }
                    int parameter = this.mSoundTriggerHelper.setParameter(soundModel.getUuid(), modelParam, value);
                    if (ignored != null) {
                        ignored.close();
                    }
                    return parameter;
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int getParameter(android.os.ParcelUuid soundModelId, int modelParam) throws java.lang.UnsupportedOperationException, java.lang.IllegalArgumentException {
            int parameter;
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    android.hardware.soundtrigger.SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(soundModelId.getUuid());
                    if (soundModel == null) {
                        throw new java.lang.IllegalArgumentException("sound model is not loaded");
                    }
                    parameter = this.mSoundTriggerHelper.getParameter(soundModel.getUuid(), modelParam);
                }
                if (ignored != null) {
                    ignored.close();
                }
                return parameter;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public android.hardware.soundtrigger.SoundTrigger.ModelParamRange queryParameter(android.os.ParcelUuid soundModelId, int modelParam) {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    android.hardware.soundtrigger.SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(soundModelId.getUuid());
                    if (soundModel != null) {
                        android.hardware.soundtrigger.SoundTrigger.ModelParamRange modelParamRangeQueryParameter = this.mSoundTriggerHelper.queryParameter(soundModel.getUuid(), modelParam);
                        if (ignored != null) {
                            ignored.close();
                        }
                        return modelParamRangeQueryParameter;
                    }
                    if (ignored != null) {
                        ignored.close();
                        return null;
                    }
                    return null;
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: clientDied, reason: merged with bridge method [inline-methods] */
        public void lambda$new$0() {
            this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.DETACH, null));
            com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type.DETACH, this.mOriginatorIdentity.packageName, "Client died").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
            detach();
        }

        private void detach() {
            if (this.mAppOpsListener != null) {
                com.android.server.soundtrigger.SoundTriggerService.this.mAppOpsManager.stopWatchingMode(this.mAppOpsListener);
            }
            com.android.server.soundtrigger.SoundTriggerService.this.mDeviceStateHandler.unregisterListener(this.mListener);
            this.mSoundTriggerHelper.detach();
            com.android.server.soundtrigger.SoundTriggerService.this.detachSessionLogger(this.mEventLogger);
        }

        private void enforceCallingPermission(java.lang.String permission) {
            if (android.media.permission.PermissionUtil.checkPermissionForPreflight(com.android.server.soundtrigger.SoundTriggerService.this.mContext, this.mOriginatorIdentity, permission) != 0) {
                throw new java.lang.SecurityException("Identity " + this.mOriginatorIdentity + " does not have permission " + permission);
            }
        }

        private void enforceDetectionPermissions(android.content.ComponentName detectionService) {
            java.lang.String packageName = detectionService.getPackageName();
            if (com.android.server.soundtrigger.SoundTriggerService.this.mPackageManager.checkPermission("android.permission.CAPTURE_AUDIO_HOTWORD", packageName) != 0) {
                throw new java.lang.SecurityException(detectionService.getPackageName() + " does not have permission android.permission.CAPTURE_AUDIO_HOTWORD");
            }
        }

        private java.util.UUID getUuid(android.os.ParcelUuid uuid) {
            if (uuid != null) {
                return uuid.getUuid();
            }
            return null;
        }

        private java.util.UUID getUuid(android.hardware.soundtrigger.SoundTrigger.SoundModel model) {
            if (model != null) {
                return model.getUuid();
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        class RemoteSoundTriggerDetectionService extends android.hardware.soundtrigger.IRecognitionStatusCallback.Stub implements android.content.ServiceConnection {
            private static final int MSG_STOP_ALL_PENDING_OPERATIONS = 1;
            private final android.media.soundtrigger.ISoundTriggerDetectionServiceClient mClient;
            private boolean mDestroyOnceRunningOpsDone;
            private boolean mIsBound;
            private boolean mIsDestroyed;
            private final com.android.server.soundtrigger.SoundTriggerService.NumOps mNumOps;
            private int mNumTotalOpsPerformed;
            private final android.os.Bundle mParams;
            private final android.os.ParcelUuid mPuuid;
            private final android.hardware.soundtrigger.SoundTrigger.RecognitionConfig mRecognitionConfig;
            private final android.os.PowerManager.WakeLock mRemoteServiceWakeLock;
            private android.media.soundtrigger.ISoundTriggerDetectionService mService;
            private final android.content.ComponentName mServiceName;
            private final android.os.UserHandle mUser;
            private final java.lang.Object mRemoteServiceLock = new java.lang.Object();
            private final java.util.ArrayList<com.android.server.soundtrigger.SoundTriggerService.Operation> mPendingOps = new java.util.ArrayList<>();
            private final android.util.ArraySet<java.lang.Integer> mRunningOpIds = new android.util.ArraySet<>();
            private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());

            public RemoteSoundTriggerDetectionService(java.util.UUID modelUuid, android.os.Bundle params, android.content.ComponentName serviceName, android.os.UserHandle user, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig config) {
                this.mPuuid = new android.os.ParcelUuid(modelUuid);
                this.mParams = params;
                this.mServiceName = serviceName;
                this.mUser = user;
                this.mRecognitionConfig = config;
                android.os.PowerManager pm = (android.os.PowerManager) com.android.server.soundtrigger.SoundTriggerService.this.mContext.getSystemService("power");
                this.mRemoteServiceWakeLock = pm.newWakeLock(1, "RemoteSoundTriggerDetectionService " + this.mServiceName.getPackageName() + ":" + this.mServiceName.getClassName());
                synchronized (com.android.server.soundtrigger.SoundTriggerService.this.mLock) {
                    com.android.server.soundtrigger.SoundTriggerService.NumOps numOps = (com.android.server.soundtrigger.SoundTriggerService.NumOps) com.android.server.soundtrigger.SoundTriggerService.this.mNumOpsPerPackage.get(this.mServiceName.getPackageName());
                    if (numOps == null) {
                        numOps = new com.android.server.soundtrigger.SoundTriggerService.NumOps();
                        com.android.server.soundtrigger.SoundTriggerService.this.mNumOpsPerPackage.put(this.mServiceName.getPackageName(), numOps);
                    }
                    this.mNumOps = numOps;
                }
                this.mClient = new android.media.soundtrigger.ISoundTriggerDetectionServiceClient.Stub() { // from class: com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.1
                    public void onOpFinished(int opId) {
                        long token = android.os.Binder.clearCallingIdentity();
                        try {
                            synchronized (com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.mRemoteServiceLock) {
                                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.mRunningOpIds.remove(java.lang.Integer.valueOf(opId));
                                if (com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.mRunningOpIds.isEmpty() && com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.mPendingOps.isEmpty()) {
                                    if (com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.mDestroyOnceRunningOpsDone) {
                                        com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.destroy();
                                    } else {
                                        com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.disconnectLocked();
                                    }
                                }
                            }
                        } finally {
                            android.os.Binder.restoreCallingIdentity(token);
                        }
                    }
                };
            }

            public boolean pingBinder() {
                return (this.mIsDestroyed || this.mDestroyOnceRunningOpsDone) ? false : true;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void disconnectLocked() {
                if (this.mService != null) {
                    try {
                        this.mService.removeClient(this.mPuuid);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": Cannot remove client", e);
                        com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": Cannot remove client"));
                    }
                    this.mService = null;
                }
                if (this.mIsBound) {
                    com.android.server.soundtrigger.SoundTriggerService.this.mContext.unbindService(this);
                    this.mIsBound = false;
                    synchronized (com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mCallbacksLock) {
                        this.mRemoteServiceWakeLock.release();
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void destroy() {
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": destroy"));
                synchronized (this.mRemoteServiceLock) {
                    disconnectLocked();
                    this.mIsDestroyed = true;
                }
                if (!this.mDestroyOnceRunningOpsDone) {
                    synchronized (com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mCallbacksLock) {
                        com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mCallbacks.remove(this.mPuuid.getUuid());
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void stopAllPendingOperations() {
                synchronized (this.mRemoteServiceLock) {
                    if (this.mIsDestroyed) {
                        return;
                    }
                    if (this.mService != null) {
                        int numOps = this.mRunningOpIds.size();
                        for (int i = 0; i < numOps; i++) {
                            try {
                                this.mService.onStopOperation(this.mPuuid, this.mRunningOpIds.valueAt(i).intValue());
                            } catch (java.lang.Exception e) {
                                android.util.Slog.e(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": Could not stop operation " + this.mRunningOpIds.valueAt(i), e);
                                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": Could not stop operation " + this.mRunningOpIds.valueAt(i)));
                            }
                        }
                        this.mRunningOpIds.clear();
                    }
                    disconnectLocked();
                }
            }

            private void bind() {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    android.content.Intent i = new android.content.Intent();
                    i.setComponent(this.mServiceName);
                    android.content.pm.ResolveInfo ri = com.android.server.soundtrigger.SoundTriggerService.this.mContext.getPackageManager().resolveServiceAsUser(i, 268435588, this.mUser.getIdentifier());
                    if (ri == null) {
                        android.util.Slog.w(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": " + this.mServiceName + " not found");
                        com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": " + this.mServiceName + " not found"));
                    } else {
                        if (!"android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE".equals(ri.serviceInfo.permission)) {
                            android.util.Slog.w(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": " + this.mServiceName + " does not require android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE");
                            com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": " + this.mServiceName + " does not require android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE"));
                            return;
                        }
                        this.mIsBound = com.android.server.soundtrigger.SoundTriggerService.this.mContext.bindServiceAsUser(i, this, 67112961, this.mUser);
                        if (this.mIsBound) {
                            this.mRemoteServiceWakeLock.acquire();
                        } else {
                            android.util.Slog.w(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": Could not bind to " + this.mServiceName);
                            com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": Could not bind to " + this.mServiceName));
                        }
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }

            /* JADX WARN: Code restructure failed: missing block: B:19:0x00b1, code lost:
            
                r6 = move-exception;
             */
            /* JADX WARN: Code restructure failed: missing block: B:20:0x00b2, code lost:
            
                android.util.Slog.e(com.android.server.soundtrigger.SoundTriggerService.TAG, r12.mPuuid + ": Could not run operation " + r5, r6);
                r12.this$1.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(r12.mPuuid + ": Could not run operation " + r5));
             */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            private void runOrAddOperation(com.android.server.soundtrigger.SoundTriggerService.Operation r13) {
                /*
                    Method dump skipped, instruction units count: 393
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.runOrAddOperation(com.android.server.soundtrigger.SoundTriggerService$Operation):void");
            }

            public void onKeyphraseDetected(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event) {
            }

            private android.media.AudioRecord createAudioRecordForEvent(android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent event) throws java.lang.UnsupportedOperationException, java.lang.IllegalArgumentException {
                android.media.AudioAttributes.Builder attributesBuilder = new android.media.AudioAttributes.Builder();
                attributesBuilder.setInternalCapturePreset(1999);
                android.media.AudioAttributes attributes = attributesBuilder.build();
                android.media.AudioFormat originalFormat = event.getCaptureFormat();
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("createAudioRecordForEvent"));
                return new android.media.AudioRecord.Builder().setAudioAttributes(attributes).setAudioFormat(new android.media.AudioFormat.Builder().setChannelMask(originalFormat.getChannelMask()).setEncoding(originalFormat.getEncoding()).setSampleRate(originalFormat.getSampleRate()).build()).setSessionId(event.getCaptureSession()).build();
            }

            public void onGenericSoundTriggerDetected(final android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent event) {
                runOrAddOperation(new com.android.server.soundtrigger.SoundTriggerService.Operation(new java.lang.Runnable() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onGenericSoundTriggerDetected$0();
                    }
                }, new com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda2
                    @Override // com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp
                    public final void run(int i, android.media.soundtrigger.ISoundTriggerDetectionService iSoundTriggerDetectionService) throws android.os.RemoteException {
                        this.f$0.lambda$onGenericSoundTriggerDetected$1(event, i, iSoundTriggerDetectionService);
                    }
                }, new java.lang.Runnable() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onGenericSoundTriggerDetected$2(event);
                    }
                }));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGenericSoundTriggerDetected$0() {
                if (!this.mRecognitionConfig.allowMultipleTriggers) {
                    synchronized (com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mCallbacksLock) {
                        com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mCallbacks.remove(this.mPuuid.getUuid());
                    }
                    this.mDestroyOnceRunningOpsDone = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGenericSoundTriggerDetected$1(android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent event, int opId, android.media.soundtrigger.ISoundTriggerDetectionService service) throws android.os.RemoteException {
                service.onGenericRecognitionEvent(this.mPuuid, opId, event);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGenericSoundTriggerDetected$2(android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent event) {
                if (event.isCaptureAvailable()) {
                    try {
                        android.media.AudioRecord capturedData = createAudioRecordForEvent(event);
                        capturedData.startRecording();
                        capturedData.release();
                    } catch (java.lang.IllegalArgumentException | java.lang.UnsupportedOperationException e) {
                        android.util.Slog.w(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": createAudioRecordForEvent(" + event + "), failed to create AudioRecord");
                    }
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            private void onError(final int i) {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onError: " + i);
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": onError: " + i));
                runOrAddOperation(new com.android.server.soundtrigger.SoundTriggerService.Operation(new java.lang.Runnable() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onError$3();
                    }
                }, new com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda5
                    @Override // com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp
                    public final void run(int i2, android.media.soundtrigger.ISoundTriggerDetectionService iSoundTriggerDetectionService) throws android.os.RemoteException {
                        this.f$0.lambda$onError$4(i, i2, iSoundTriggerDetectionService);
                    }
                }, null));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onError$3() {
                synchronized (com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mCallbacksLock) {
                    com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mCallbacks.remove(this.mPuuid.getUuid());
                }
                this.mDestroyOnceRunningOpsDone = true;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onError$4(int status, int opId, android.media.soundtrigger.ISoundTriggerDetectionService service) throws android.os.RemoteException {
                service.onError(this.mPuuid, opId, status);
            }

            public void onPreempted() {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onPreempted");
                onError(Integer.MIN_VALUE);
            }

            public void onModuleDied() {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onModuleDied");
                onError(android.hardware.soundtrigger.SoundTrigger.STATUS_DEAD_OBJECT);
            }

            public void onResumeFailed(int status) {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onResumeFailed: " + status);
                onError(status);
            }

            public void onPauseFailed(int status) {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onPauseFailed: " + status);
                onError(status);
            }

            public void onRecognitionPaused() {
            }

            public void onRecognitionResumed() {
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onServiceConnected(" + service + ")");
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": onServiceConnected(" + service + ")"));
                synchronized (this.mRemoteServiceLock) {
                    this.mService = android.media.soundtrigger.ISoundTriggerDetectionService.Stub.asInterface(service);
                    try {
                        this.mService.setClient(this.mPuuid, this.mParams, this.mClient);
                        while (!this.mPendingOps.isEmpty()) {
                            runOrAddOperation(this.mPendingOps.remove(0));
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": Could not init " + this.mServiceName, e);
                    }
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName name) {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onServiceDisconnected");
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": onServiceDisconnected"));
                synchronized (this.mRemoteServiceLock) {
                    this.mService = null;
                }
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(android.content.ComponentName name) {
                android.util.Slog.v(com.android.server.soundtrigger.SoundTriggerService.TAG, this.mPuuid + ": onBindingDied");
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(this.mPuuid + ": onBindingDied"));
                synchronized (this.mRemoteServiceLock) {
                    destroy();
                }
            }

            @Override // android.content.ServiceConnection
            public void onNullBinding(android.content.ComponentName name) {
                android.util.Slog.w(com.android.server.soundtrigger.SoundTriggerService.TAG, name + " for model " + this.mPuuid + " returned a null binding");
                com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.this.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(name + " for model " + this.mPuuid + " returned a null binding"));
                synchronized (this.mRemoteServiceLock) {
                    disconnectLocked();
                }
            }
        }
    }

    private static class NumOps {
        private long mLastOpsHourSinceBoot;
        private final java.lang.Object mLock;
        private int[] mNumOps;

        private NumOps() {
            this.mLock = new java.lang.Object();
            this.mNumOps = new int[24];
        }

        void clearOldOps(long currentTime) {
            synchronized (this.mLock) {
                long numHoursSinceBoot = java.util.concurrent.TimeUnit.HOURS.convert(currentTime, java.util.concurrent.TimeUnit.NANOSECONDS);
                if (this.mLastOpsHourSinceBoot != 0) {
                    for (long hour = this.mLastOpsHourSinceBoot + 1; hour <= numHoursSinceBoot; hour++) {
                        this.mNumOps[(int) (hour % 24)] = 0;
                    }
                }
            }
        }

        void addOp(long currentTime) {
            synchronized (this.mLock) {
                long numHoursSinceBoot = java.util.concurrent.TimeUnit.HOURS.convert(currentTime, java.util.concurrent.TimeUnit.NANOSECONDS);
                int[] iArr = this.mNumOps;
                int i = (int) (numHoursSinceBoot % 24);
                iArr[i] = iArr[i] + 1;
                this.mLastOpsHourSinceBoot = numHoursSinceBoot;
            }
        }

        int getOpsAdded() {
            int totalOperationsInLastDay;
            synchronized (this.mLock) {
                totalOperationsInLastDay = 0;
                for (int i = 0; i < 24; i++) {
                    totalOperationsInLastDay += this.mNumOps[i];
                }
            }
            return totalOperationsInLastDay;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Operation {
        private final java.lang.Runnable mDropOp;
        private final com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp mExecuteOp;
        private final java.lang.Runnable mSetupOp;

        /* JADX INFO: Access modifiers changed from: private */
        interface ExecuteOp {
            void run(int i, android.media.soundtrigger.ISoundTriggerDetectionService iSoundTriggerDetectionService) throws android.os.RemoteException;
        }

        private Operation(java.lang.Runnable setupOp, com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp executeOp, java.lang.Runnable cancelOp) {
            this.mSetupOp = setupOp;
            this.mExecuteOp = executeOp;
            this.mDropOp = cancelOp;
        }

        private void setup() {
            if (this.mSetupOp != null) {
                this.mSetupOp.run();
            }
        }

        void run(int opId, android.media.soundtrigger.ISoundTriggerDetectionService service) throws android.os.RemoteException {
            setup();
            this.mExecuteOp.run(opId, service);
        }

        void drop() {
            setup();
            if (this.mDropOp != null) {
                this.mDropOp.run();
            }
        }
    }

    public final class LocalSoundTriggerService implements com.android.server.SoundTriggerInternal {
        private final android.content.Context mContext;

        LocalSoundTriggerService(android.content.Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        class SessionImpl implements com.android.server.SoundTriggerInternal.Session {
            private final com.android.server.soundtrigger.SoundTriggerService.MyAppOpsListener mAppOpsListener;
            private final android.os.IBinder mClient;
            private final com.android.server.utils.EventLogger mEventLogger;
            private final com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener mListener;
            private final android.util.SparseArray<java.util.UUID> mModelUuid;
            private final android.media.permission.Identity mOriginatorIdentity;
            private final com.android.server.soundtrigger.SoundTriggerHelper mSoundTriggerHelper;

            private SessionImpl(com.android.server.soundtrigger.SoundTriggerHelper soundTriggerHelper, android.os.IBinder client, com.android.server.utils.EventLogger eventLogger, android.media.permission.Identity originatorIdentity) {
                this.mModelUuid = new android.util.SparseArray<>(1);
                this.mSoundTriggerHelper = soundTriggerHelper;
                this.mClient = client;
                this.mOriginatorIdentity = originatorIdentity;
                this.mEventLogger = eventLogger;
                com.android.server.soundtrigger.SoundTriggerService.this.mSessionEventLoggers.add(this.mEventLogger);
                try {
                    this.mClient.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.soundtrigger.SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda0
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            this.f$0.lambda$new$0();
                        }
                    }, 0);
                } catch (android.os.RemoteException e) {
                    lambda$new$0();
                }
                this.mListener = new com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener() { // from class: com.android.server.soundtrigger.SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda1
                    @Override // com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener
                    public final void onSoundTriggerDeviceStateUpdate(com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState) {
                        this.f$0.lambda$new$1(soundTriggerDeviceState);
                    }
                };
                com.android.server.soundtrigger.SoundTriggerService soundTriggerService = com.android.server.soundtrigger.SoundTriggerService.this;
                android.media.permission.Identity identity = this.mOriginatorIdentity;
                com.android.server.soundtrigger.SoundTriggerHelper soundTriggerHelper2 = this.mSoundTriggerHelper;
                java.util.Objects.requireNonNull(soundTriggerHelper2);
                this.mAppOpsListener = soundTriggerService.new MyAppOpsListener(identity, new com.android.server.soundtrigger.SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda2(soundTriggerHelper2));
                this.mAppOpsListener.forceOpChangeRefresh();
                com.android.server.soundtrigger.SoundTriggerService.this.mAppOpsManager.startWatchingMode("android:record_audio", this.mOriginatorIdentity.packageName, 1, this.mAppOpsListener);
                com.android.server.soundtrigger.SoundTriggerService.this.mDeviceStateHandler.registerListener(this.mListener);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$new$1(com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState state) {
                this.mSoundTriggerHelper.onDeviceStateChanged(state);
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public int startRecognition(int keyphraseId, android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel soundModel, android.hardware.soundtrigger.IRecognitionStatusCallback listener, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig recognitionConfig, boolean runInBatterySaverMode) {
                this.mModelUuid.put(keyphraseId, soundModel.getUuid());
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION, soundModel.getUuid()));
                return this.mSoundTriggerHelper.startKeyphraseRecognition(keyphraseId, soundModel, listener, recognitionConfig, runInBatterySaverMode);
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public synchronized int stopRecognition(int keyphraseId, android.hardware.soundtrigger.IRecognitionStatusCallback listener) {
                java.util.UUID uuid = this.mModelUuid.get(keyphraseId);
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION, uuid));
                return this.mSoundTriggerHelper.stopKeyphraseRecognition(keyphraseId, listener);
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public android.hardware.soundtrigger.SoundTrigger.ModuleProperties getModuleProperties() {
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.GET_MODULE_PROPERTIES, null));
                return this.mSoundTriggerHelper.getModuleProperties();
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public int setParameter(int keyphraseId, int modelParam, int value) {
                java.util.UUID uuid = this.mModelUuid.get(keyphraseId);
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.SET_PARAMETER, uuid));
                return this.mSoundTriggerHelper.setKeyphraseParameter(keyphraseId, modelParam, value);
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public int getParameter(int keyphraseId, int modelParam) {
                return this.mSoundTriggerHelper.getKeyphraseParameter(keyphraseId, modelParam);
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public android.hardware.soundtrigger.SoundTrigger.ModelParamRange queryParameter(int keyphraseId, int modelParam) {
                return this.mSoundTriggerHelper.queryKeyphraseParameter(keyphraseId, modelParam);
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public void detach() {
                detachInternal();
            }

            @Override // com.android.server.SoundTriggerInternal.Session
            public int unloadKeyphraseModel(int keyphraseId) {
                java.util.UUID uuid = this.mModelUuid.get(keyphraseId);
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.UNLOAD_MODEL, uuid));
                return this.mSoundTriggerHelper.unloadKeyphraseSoundModel(keyphraseId);
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* JADX INFO: renamed from: clientDied, reason: merged with bridge method [inline-methods] */
            public void lambda$new$0() {
                com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type.DETACH, this.mOriginatorIdentity.packageName, "Client died").printLog(2, com.android.server.soundtrigger.SoundTriggerService.TAG));
                detachInternal();
            }

            private void detachInternal() {
                if (this.mAppOpsListener != null) {
                    com.android.server.soundtrigger.SoundTriggerService.this.mAppOpsManager.stopWatchingMode(this.mAppOpsListener);
                }
                this.mEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type.DETACH, null));
                com.android.server.soundtrigger.SoundTriggerService.this.detachSessionLogger(this.mEventLogger);
                com.android.server.soundtrigger.SoundTriggerService.this.mDeviceStateHandler.unregisterListener(this.mListener);
                this.mSoundTriggerHelper.detach();
            }
        }

        @Override // com.android.server.SoundTriggerInternal
        public com.android.server.SoundTriggerInternal.Session attach(android.os.IBinder client, android.hardware.soundtrigger.SoundTrigger.ModuleProperties underlyingModule, boolean isTrusted) {
            android.media.permission.Identity identity = android.media.permission.IdentityContext.getNonNull();
            int sessionId = com.android.server.soundtrigger.SoundTriggerService.this.mSessionIdCounter.getAndIncrement();
            com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type.ATTACH, identity.packageName + "#" + sessionId));
            com.android.server.utils.EventLogger eventLogger = new com.android.server.utils.EventLogger(128, "LocalSoundTriggerEventLogger for package: " + identity.packageName + "#" + sessionId + " - " + identity.uid + "|" + identity.pid);
            return new com.android.server.soundtrigger.SoundTriggerService.LocalSoundTriggerService.SessionImpl(com.android.server.soundtrigger.SoundTriggerService.this.newSoundTriggerHelper(underlyingModule, eventLogger, isTrusted), client, eventLogger, identity);
        }

        @Override // com.android.server.SoundTriggerInternal
        public java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> listModuleProperties(android.media.permission.Identity originatorIdentity) {
            com.android.server.soundtrigger.SoundTriggerService.this.mServiceEventLogger.enqueue(new com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type.LIST_MODULE, originatorIdentity.packageName));
            android.media.permission.SafeCloseable ignored = android.media.permission.PermissionUtil.establishIdentityDirect(originatorIdentity);
            try {
                java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> listLambda$newSoundTriggerHelper$2 = com.android.server.soundtrigger.SoundTriggerService.this.lambda$newSoundTriggerHelper$2(originatorIdentity);
                if (ignored != null) {
                    ignored.close();
                }
                return listLambda$newSoundTriggerHelper$2;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }
}
