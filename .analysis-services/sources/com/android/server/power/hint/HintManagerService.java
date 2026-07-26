package com.android.server.power.hint;

/* JADX INFO: loaded from: classes3.dex */
public final class HintManagerService extends com.android.server.SystemService {
    static final int CLEAN_UP_UID_DELAY_MILLIS = 1000;
    private static final boolean DEBUG = false;
    private static final int EVENT_CLEAN_UP_UID = 3;
    private static final java.lang.String PROPERTY_HWUI_ENABLE_HINT_MANAGER = "debug.hwui.use_hint_manager";
    private static final java.lang.String PROPERTY_SF_ENABLE_CPU_HINT = "debug.sf.enable_adpf_cpu_hint";
    private static final java.lang.String TAG = "HintManagerService";
    private final android.util.ArrayMap<java.lang.Integer, android.util.ArrayMap<android.os.IBinder, android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession>>> mActiveSessions;
    private final android.app.ActivityManagerInternal mAmInternal;
    private android.util.ArrayMap<java.lang.Integer, java.util.TreeMap<java.lang.Integer, com.android.server.power.hint.HintManagerService.ChannelItem>> mChannelMap;
    private final java.lang.Object mChannelMapLock;
    private final com.android.server.power.hint.HintManagerService.CleanUpHandler mCleanUpHandler;
    private java.util.concurrent.atomic.AtomicBoolean mConfigCreationSupport;
    private final android.content.Context mContext;
    final long mHintSessionPreferredRate;
    private final java.lang.Object mLock;
    private final com.android.server.power.hint.HintManagerService.NativeWrapper mNativeWrapper;
    private final java.util.Map<java.lang.Integer, java.util.Set<java.lang.Long>> mNonIsolatedTids;
    private final java.lang.Object mNonIsolatedTidsLock;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.hardware.power.IPower mPowerHal;
    private int mPowerHalVersion;
    final android.os.IHintManager.Stub mService;
    final com.android.server.power.hint.HintManagerService.MyUidObserver mUidObserver;

    public HintManagerService(android.content.Context context) {
        this(context, new com.android.server.power.hint.HintManagerService.Injector());
    }

    HintManagerService(android.content.Context context, com.android.server.power.hint.HintManagerService.Injector injector) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mChannelMapLock = new java.lang.Object();
        this.mNonIsolatedTidsLock = new java.lang.Object();
        this.mConfigCreationSupport = new java.util.concurrent.atomic.AtomicBoolean(true);
        this.mService = new com.android.server.power.hint.HintManagerService.BinderService();
        this.mContext = context;
        if (com.android.server.power.hint.Flags.powerhintThreadCleanup()) {
            this.mCleanUpHandler = new com.android.server.power.hint.HintManagerService.CleanUpHandler(createCleanUpThread().getLooper());
            this.mNonIsolatedTids = new java.util.HashMap();
        } else {
            this.mCleanUpHandler = null;
            this.mNonIsolatedTids = null;
        }
        if (com.android.server.power.hint.Flags.adpfSessionTag()) {
            this.mPackageManager = this.mContext.getPackageManager();
        } else {
            this.mPackageManager = null;
        }
        this.mActiveSessions = new android.util.ArrayMap<>();
        this.mChannelMap = new android.util.ArrayMap<>();
        this.mNativeWrapper = injector.createNativeWrapper();
        this.mNativeWrapper.halInit();
        this.mHintSessionPreferredRate = this.mNativeWrapper.halGetHintSessionPreferredRate();
        this.mUidObserver = new com.android.server.power.hint.HintManagerService.MyUidObserver();
        this.mAmInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
        this.mPowerHal = injector.createIPower();
        this.mPowerHalVersion = 0;
        if (this.mPowerHal != null) {
            try {
                this.mPowerHalVersion = this.mPowerHal.getInterfaceVersion();
            } catch (android.os.RemoteException e) {
                throw new java.lang.IllegalStateException("Could not contact PowerHAL!", e);
            }
        }
    }

    private com.android.server.ServiceThread createCleanUpThread() {
        com.android.server.ServiceThread handlerThread = new com.android.server.ServiceThread(TAG, 19, true);
        handlerThread.start();
        return handlerThread;
    }

    static class Injector {
        Injector() {
        }

        com.android.server.power.hint.HintManagerService.NativeWrapper createNativeWrapper() {
            return new com.android.server.power.hint.HintManagerService.NativeWrapper();
        }

        android.hardware.power.IPower createIPower() {
            return android.hardware.power.IPower.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(android.hardware.power.IPower.DESCRIPTOR + "/default"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHalSupported() {
        return this.mHintSessionPreferredRate != -1;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("performance_hint", this.mService);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            systemReady();
        }
        if (phase == 1000) {
            registerStatsCallbacks();
        }
    }

    private void systemReady() {
        com.android.server.utils.Slogf.v(TAG, "Initializing HintManager service...");
        try {
            android.app.ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
        }
    }

    private void registerStatsCallbacks() {
        android.app.StatsManager statsManager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.ADPF_SYSTEM_COMPONENT_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.power.hint.HintManagerService$$ExternalSyntheticLambda0
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.onPullAtom(i, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
        if (atomTag == 10173) {
            boolean isSurfaceFlingerUsingCpuHint = android.os.SystemProperties.getBoolean(PROPERTY_SF_ENABLE_CPU_HINT, false);
            boolean isHwuiHintManagerEnabled = android.os.SystemProperties.getBoolean(PROPERTY_HWUI_ENABLE_HINT_MANAGER, false);
            data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.ADPF_SYSTEM_COMPONENT_INFO, isSurfaceFlingerUsingCpuHint, isHwuiHintManagerEnabled));
        }
        return 0;
    }

    public static class NativeWrapper {
        private static native void nativeCloseHintSession(long j);

        private static native long nativeCreateHintSession(int i, int i2, int[] iArr, long j);

        private static native long nativeCreateHintSessionWithConfig(int i, int i2, int[] iArr, long j, int i3, android.hardware.power.SessionConfig sessionConfig);

        private static native long nativeGetHintSessionPreferredRate();

        private native void nativeInit();

        private static native void nativePauseHintSession(long j);

        private static native void nativeReportActualWorkDuration(long j, long[] jArr, long[] jArr2);

        private static native void nativeReportActualWorkDuration(long j, android.hardware.power.WorkDuration[] workDurationArr);

        private static native void nativeResumeHintSession(long j);

        private static native void nativeSendHint(long j, int i);

        private static native void nativeSetMode(long j, int i, boolean z);

        private static native void nativeSetThreads(long j, int[] iArr);

        private static native void nativeUpdateTargetWorkDuration(long j, long j2);

        public void halInit() {
            nativeInit();
        }

        public long halGetHintSessionPreferredRate() {
            return nativeGetHintSessionPreferredRate();
        }

        public long halCreateHintSession(int tgid, int uid, int[] tids, long durationNanos) {
            return nativeCreateHintSession(tgid, uid, tids, durationNanos);
        }

        public long halCreateHintSessionWithConfig(int tgid, int uid, int[] tids, long durationNanos, int tag, android.hardware.power.SessionConfig config) {
            return nativeCreateHintSessionWithConfig(tgid, uid, tids, durationNanos, tag, config);
        }

        public void halPauseHintSession(long halPtr) {
            nativePauseHintSession(halPtr);
        }

        public void halResumeHintSession(long halPtr) {
            nativeResumeHintSession(halPtr);
        }

        public void halCloseHintSession(long halPtr) {
            nativeCloseHintSession(halPtr);
        }

        public void halUpdateTargetWorkDuration(long halPtr, long targetDurationNanos) {
            nativeUpdateTargetWorkDuration(halPtr, targetDurationNanos);
        }

        public void halReportActualWorkDuration(long halPtr, long[] actualDurationNanos, long[] timeStampNanos) {
            nativeReportActualWorkDuration(halPtr, actualDurationNanos, timeStampNanos);
        }

        public void halSendHint(long halPtr, int hint) {
            nativeSendHint(halPtr, hint);
        }

        public void halSetThreads(long halPtr, int[] tids) {
            nativeSetThreads(halPtr, tids);
        }

        public void halSetMode(long halPtr, int mode, boolean enabled) {
            nativeSetMode(halPtr, mode, enabled);
        }

        public void halReportActualWorkDuration(long halPtr, android.hardware.power.WorkDuration[] workDurations) {
            nativeReportActualWorkDuration(halPtr, workDurations);
        }
    }

    final class MyUidObserver extends android.app.UidObserver {
        private final android.util.SparseIntArray mProcStatesCache = new android.util.SparseIntArray();

        MyUidObserver() {
        }

        public boolean isUidForeground(int uid) {
            boolean z;
            synchronized (com.android.server.power.hint.HintManagerService.this.mLock) {
                z = this.mProcStatesCache.get(uid, 6) <= 6;
            }
            return z;
        }

        public void onUidGone(final int uid, boolean disabled) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.power.hint.HintManagerService$MyUidObserver$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUidGone$0(uid);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUidGone$0(int uid) {
            synchronized (com.android.server.power.hint.HintManagerService.this.mLock) {
                this.mProcStatesCache.delete(uid);
                android.util.ArrayMap<android.os.IBinder, android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession>> tokenMap = (android.util.ArrayMap) com.android.server.power.hint.HintManagerService.this.mActiveSessions.get(java.lang.Integer.valueOf(uid));
                if (tokenMap == null) {
                    return;
                }
                android.util.Slog.d(com.android.server.power.hint.HintManagerService.TAG, "Uid gone for " + uid);
                for (int i = tokenMap.size() - 1; i >= 0; i--) {
                    android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession> sessionSet = tokenMap.valueAt(i);
                    for (int j = sessionSet.size() - 1; j >= 0; j--) {
                        sessionSet.valueAt(j).close();
                    }
                }
                synchronized (com.android.server.power.hint.HintManagerService.this.mChannelMapLock) {
                    java.util.TreeMap<java.lang.Integer, com.android.server.power.hint.HintManagerService.ChannelItem> uidMap = (java.util.TreeMap) com.android.server.power.hint.HintManagerService.this.mChannelMap.get(java.lang.Integer.valueOf(uid));
                    if (uidMap != null) {
                        for (java.util.Map.Entry<java.lang.Integer, com.android.server.power.hint.HintManagerService.ChannelItem> entry : uidMap.entrySet()) {
                            entry.getValue().closeChannel();
                        }
                        com.android.server.power.hint.HintManagerService.this.mChannelMap.remove(java.lang.Integer.valueOf(uid));
                    }
                }
            }
        }

        public void onUidStateChanged(final int uid, final int procState, long procStateSeq, int capability) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.power.hint.HintManagerService$MyUidObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUidStateChanged$1(uid, procState);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUidStateChanged$1(int uid, int procState) {
            synchronized (com.android.server.power.hint.HintManagerService.this.mLock) {
                boolean shouldCleanup = false;
                if (com.android.server.power.hint.HintManagerService.this.mPowerHalVersion >= 4 && com.android.server.power.hint.Flags.powerhintThreadCleanup()) {
                    int prevProcState = this.mProcStatesCache.get(uid, Integer.MAX_VALUE);
                    shouldCleanup = prevProcState <= 6 && procState > 6;
                }
                this.mProcStatesCache.put(uid, procState);
                android.util.ArrayMap<android.os.IBinder, android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession>> tokenMap = (android.util.ArrayMap) com.android.server.power.hint.HintManagerService.this.mActiveSessions.get(java.lang.Integer.valueOf(uid));
                if (tokenMap == null) {
                    return;
                }
                if (shouldCleanup && com.android.server.power.hint.Flags.powerhintThreadCleanup()) {
                    android.os.Message msg = com.android.server.power.hint.HintManagerService.this.mCleanUpHandler.obtainMessage(3, java.lang.Integer.valueOf(uid));
                    com.android.server.power.hint.HintManagerService.this.mCleanUpHandler.sendMessageDelayed(msg, 1000L);
                    android.util.Slog.d(com.android.server.power.hint.HintManagerService.TAG, "Sent cleanup message for uid " + uid);
                }
                boolean shouldAllowUpdate = isUidForeground(uid);
                for (int i = tokenMap.size() - 1; i >= 0; i--) {
                    android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession> sessionSet = tokenMap.valueAt(i);
                    for (int j = sessionSet.size() - 1; j >= 0; j--) {
                        sessionSet.valueAt(j).updateHintAllowedByProcState(shouldAllowUpdate);
                    }
                }
            }
        }
    }

    public com.android.server.power.hint.HintManagerService.ChannelItem getOrCreateMappedChannelItem(int tgid, int uid, android.os.IBinder token) {
        com.android.server.power.hint.HintManagerService.ChannelItem channelItem;
        synchronized (this.mChannelMapLock) {
            if (!this.mChannelMap.containsKey(java.lang.Integer.valueOf(uid))) {
                this.mChannelMap.put(java.lang.Integer.valueOf(uid), new java.util.TreeMap<>());
            }
            java.util.TreeMap<java.lang.Integer, com.android.server.power.hint.HintManagerService.ChannelItem> map = this.mChannelMap.get(java.lang.Integer.valueOf(uid));
            if (!map.containsKey(java.lang.Integer.valueOf(tgid))) {
                com.android.server.power.hint.HintManagerService.ChannelItem item = new com.android.server.power.hint.HintManagerService.ChannelItem(tgid, uid, token);
                item.openChannel();
                map.put(java.lang.Integer.valueOf(tgid), item);
            }
            channelItem = map.get(java.lang.Integer.valueOf(tgid));
        }
        return channelItem;
    }

    public void removeChannelItem(java.lang.Integer tgid, java.lang.Integer uid) {
        synchronized (this.mChannelMapLock) {
            java.util.TreeMap<java.lang.Integer, com.android.server.power.hint.HintManagerService.ChannelItem> map = this.mChannelMap.get(uid);
            if (map != null) {
                com.android.server.power.hint.HintManagerService.ChannelItem item = map.get(tgid);
                if (item != null) {
                    item.closeChannel();
                    map.remove(tgid);
                }
                if (map.isEmpty()) {
                    this.mChannelMap.remove(uid);
                }
            }
        }
    }

    private class ChannelItem implements android.os.IBinder.DeathRecipient {
        final int mTgid;
        final android.os.IBinder mToken;
        final int mUid;
        boolean mLinked = false;
        android.hardware.power.ChannelConfig mConfig = null;

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.power.hint.HintManagerService.this.removeChannelItem(java.lang.Integer.valueOf(this.mTgid), java.lang.Integer.valueOf(this.mUid));
        }

        ChannelItem(int tgid, int uid, android.os.IBinder token) {
            this.mTgid = tgid;
            this.mUid = uid;
            this.mToken = token;
        }

        public void closeChannel() {
            if (this.mLinked) {
                this.mToken.unlinkToDeath(this, 0);
                this.mLinked = false;
            }
            if (this.mConfig != null) {
                try {
                    com.android.server.power.hint.HintManagerService.this.mPowerHal.closeSessionChannel(this.mTgid, this.mUid);
                    this.mConfig = null;
                } catch (android.os.RemoteException e) {
                    throw new java.lang.IllegalStateException("Failed to close session channel!", e);
                }
            }
        }

        public void openChannel() {
            if (!this.mLinked) {
                try {
                    this.mToken.linkToDeath(this, 0);
                    this.mLinked = true;
                } catch (android.os.RemoteException e) {
                    throw new java.lang.IllegalStateException("Client already dead", e);
                }
            }
            if (this.mConfig == null) {
                try {
                    this.mConfig = com.android.server.power.hint.HintManagerService.this.mPowerHal.getSessionChannel(this.mTgid, this.mUid);
                } catch (android.os.RemoteException e2) {
                    com.android.server.power.hint.HintManagerService.this.removeChannelItem(java.lang.Integer.valueOf(this.mTgid), java.lang.Integer.valueOf(this.mUid));
                    throw new java.lang.IllegalStateException("Failed to create session channel!", e2);
                }
            }
        }

        android.hardware.power.ChannelConfig getConfig() {
            return this.mConfig;
        }
    }

    final class CleanUpHandler extends android.os.Handler {
        private static final int TID_EXITED = 2;
        private static final int TID_NOT_CHECKED = 0;
        private static final int TID_PASSED_CHECK = 1;

        CleanUpHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 3) {
                if (hasEqualMessages(msg.what, msg.obj)) {
                    removeEqualMessages(msg.what, msg.obj);
                    android.os.Message newMsg = obtainMessage(msg.what, msg.obj);
                    sendMessageDelayed(newMsg, 1000L);
                    android.util.Slog.d(com.android.server.power.hint.HintManagerService.TAG, "Duplicate messages for " + msg.obj);
                    return;
                }
                android.util.Slog.d(com.android.server.power.hint.HintManagerService.TAG, "Starts cleaning for " + msg.obj);
                int uid = ((java.lang.Integer) msg.obj).intValue();
                boolean isForeground = com.android.server.power.hint.HintManagerService.this.mUidObserver.isUidForeground(uid);
                synchronized (com.android.server.power.hint.HintManagerService.this.mLock) {
                    android.util.ArrayMap<android.os.IBinder, android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession>> tokenMap = (android.util.ArrayMap) com.android.server.power.hint.HintManagerService.this.mActiveSessions.get(java.lang.Integer.valueOf(uid));
                    if (tokenMap != null && !tokenMap.isEmpty()) {
                        java.util.List<com.android.server.power.hint.HintManagerService.AppHintSession> sessions = new java.util.ArrayList<>(tokenMap.size());
                        for (int i = tokenMap.size() - 1; i >= 0; i--) {
                            android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession> set = tokenMap.valueAt(i);
                            for (int j = set.size() - 1; j >= 0; j--) {
                                sessions.add(set.valueAt(j));
                            }
                        }
                        long[] durationList = new long[sessions.size()];
                        int[] invalidTidCntList = new int[sessions.size()];
                        android.util.SparseIntArray checkedTids = new android.util.SparseIntArray();
                        int[] totalTidCnt = new int[1];
                        for (int i2 = sessions.size() - 1; i2 >= 0; i2--) {
                            com.android.server.power.hint.HintManagerService.AppHintSession session = sessions.get(i2);
                            long start = java.lang.System.nanoTime();
                            try {
                                int invalidCnt = cleanUpSession(session, checkedTids, totalTidCnt);
                                long elapsed = java.lang.System.nanoTime() - start;
                                invalidTidCntList[i2] = invalidCnt;
                                durationList[i2] = elapsed;
                            } catch (java.lang.Exception e) {
                                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Failed to clean up session ");
                                long start2 = session.mHalSessionPtr;
                                android.util.Slog.e(com.android.server.power.hint.HintManagerService.TAG, sbAppend.append(start2).append(" for UID ").append(session.mUid).toString());
                            }
                        }
                        logCleanUpMetrics(uid, invalidTidCntList, durationList, sessions.size(), totalTidCnt[0], isForeground);
                    }
                }
            }
        }

        private void logCleanUpMetrics(int uid, int[] count, long[] durationNsList, int sessionCnt, int totalTidCnt, boolean isForeground) {
            int maxInvalidTidCnt = Integer.MIN_VALUE;
            int totalInvalidTidCnt = 0;
            for (int i = 0; i < count.length; i++) {
                totalInvalidTidCnt += count[i];
                maxInvalidTidCnt = java.lang.Math.max(maxInvalidTidCnt, count[i]);
            }
            if (totalInvalidTidCnt > 0) {
                java.util.Arrays.sort(durationNsList);
                long totalDurationNs = 0;
                for (long j : durationNsList) {
                    totalDurationNs += j;
                }
                int totalDurationUs = (int) java.util.concurrent.TimeUnit.NANOSECONDS.toMicros(totalDurationNs);
                int maxDurationUs = (int) java.util.concurrent.TimeUnit.NANOSECONDS.toMicros(durationNsList[durationNsList.length - 1]);
                int minDurationUs = (int) java.util.concurrent.TimeUnit.NANOSECONDS.toMicros(durationNsList[0]);
                int avgDurationUs = (int) java.util.concurrent.TimeUnit.NANOSECONDS.toMicros(totalDurationNs / ((long) durationNsList.length));
                int th90DurationUs = (int) java.util.concurrent.TimeUnit.NANOSECONDS.toMicros(durationNsList[(int) (((double) durationNsList.length) * 0.9d)]);
                int th90DurationUs2 = totalInvalidTidCnt;
                int avgDurationUs2 = maxInvalidTidCnt;
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.ADPF_HINT_SESSION_TID_CLEANUP, uid, totalDurationUs, maxDurationUs, totalTidCnt, th90DurationUs2, avgDurationUs2, sessionCnt, isForeground);
                android.util.Slog.w(com.android.server.power.hint.HintManagerService.TAG, "Invalid tid found for UID" + uid + " in " + totalDurationUs + "us:\n\tcount( session: " + sessionCnt + " totalTid: " + totalTidCnt + " maxInvalidTid: " + maxInvalidTidCnt + " totalInvalidTid: " + totalInvalidTidCnt + ")\n\ttime per session( min: " + minDurationUs + "us max: " + maxDurationUs + "us avg: " + avgDurationUs + "us 90%: " + th90DurationUs + "us)\n\tisForeground: " + isForeground);
            }
        }

        public int cleanUpSession(com.android.server.power.hint.HintManagerService.AppHintSession session, android.util.SparseIntArray checkedTids, int[] total) {
            boolean isNotIsolated;
            if (session.isClosed() || session.isForcePaused()) {
                return 0;
            }
            int pid = session.mPid;
            int[] tids = session.getTidsInternal();
            if (total != null && total.length == 1) {
                total[0] = total[0] + tids.length;
            }
            android.util.IntArray filtered = new android.util.IntArray(tids.length);
            for (int tid : tids) {
                if (checkedTids.get(tid, 0) != 0) {
                    if (checkedTids.get(tid) == 1) {
                        filtered.add(tid);
                    }
                } else {
                    synchronized (com.android.server.power.hint.HintManagerService.this.mNonIsolatedTidsLock) {
                        isNotIsolated = com.android.server.power.hint.HintManagerService.this.mNonIsolatedTids.containsKey(java.lang.Integer.valueOf(tid));
                    }
                    if (isNotIsolated) {
                        try {
                            android.os.Process.checkTid(pid, tid);
                        } catch (java.util.NoSuchElementException e) {
                            checkedTids.put(tid, 2);
                        } catch (java.lang.Exception e2) {
                            android.util.Slog.w(com.android.server.power.hint.HintManagerService.TAG, "Unexpected exception when checking TID " + tid + " under PID " + pid + "(isolated: " + (!isNotIsolated) + ")", e2);
                            filtered.add(tid);
                        }
                    } else {
                        android.os.Process.checkPid(tid);
                    }
                    checkedTids.put(tid, 1);
                    filtered.add(tid);
                }
            }
            int i = tids.length;
            int diff = i - filtered.size();
            if (diff > 0) {
                synchronized (session) {
                    int[] newTids = session.getTidsInternal();
                    if (newTids.length != tids.length) {
                        android.util.Slog.d(com.android.server.power.hint.HintManagerService.TAG, "Skipped cleaning up the session as new tids are added");
                        return diff;
                    }
                    java.util.Arrays.sort(newTids);
                    java.util.Arrays.sort(tids);
                    if (!java.util.Arrays.equals(newTids, tids)) {
                        android.util.Slog.d(com.android.server.power.hint.HintManagerService.TAG, "Skipped cleaning up the session as new tids are updated");
                        return diff;
                    }
                    android.util.Slog.d(com.android.server.power.hint.HintManagerService.TAG, "Cleaned up " + diff + " invalid tids for session " + session.mHalSessionPtr + " with UID " + session.mUid + "\n\tbefore: " + java.util.Arrays.toString(tids) + "\n\tafter: " + filtered);
                    int[] filteredTids = filtered.toArray();
                    if (filteredTids.length == 0) {
                        session.mShouldForcePause = true;
                        if (session.mUpdateAllowedByProcState) {
                            session.pause();
                        }
                    } else {
                        session.setThreadsInternal(filteredTids, false);
                    }
                }
            }
            return diff;
        }
    }

    android.os.IHintManager.Stub getBinderServiceInstance() {
        return this.mService;
    }

    java.lang.Boolean hasChannel(int tgid, int uid) {
        synchronized (this.mChannelMapLock) {
            java.util.TreeMap<java.lang.Integer, com.android.server.power.hint.HintManagerService.ChannelItem> uidMap = this.mChannelMap.get(java.lang.Integer.valueOf(uid));
            if (uidMap != null) {
                com.android.server.power.hint.HintManagerService.ChannelItem item = uidMap.get(java.lang.Integer.valueOf(tgid));
                return java.lang.Boolean.valueOf(item != null);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.Integer checkTidValid(int uid, int tgid, int[] tids, android.util.IntArray nonIsolated) {
        java.util.List<java.lang.Integer> isolatedPids = null;
        for (int tid : tids) {
            java.lang.String[] procStatusKeys = {"Uid:", "Tgid:"};
            long[] output = new long[procStatusKeys.length];
            android.os.Process.readProcLines("/proc/" + tid + "/status", procStatusKeys, output);
            int uidOfThreadId = (int) output[0];
            int pidOfThreadId = (int) output[1];
            if (nonIsolated != null && pidOfThreadId == tgid) {
                nonIsolated.add(tid);
            } else if (uidOfThreadId != uid) {
                if (isolatedPids == null) {
                    if (uid == 1000) {
                        return java.lang.Integer.valueOf(tid);
                    }
                    isolatedPids = this.mAmInternal.getIsolatedProcesses(uid);
                    if (isolatedPids == null) {
                        return java.lang.Integer.valueOf(tid);
                    }
                }
                if (!isolatedPids.contains(java.lang.Integer.valueOf(pidOfThreadId))) {
                    return java.lang.Integer.valueOf(tid);
                }
            } else {
                continue;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String formatTidCheckErrMsg(int callingUid, int[] tids, java.lang.Integer invalidTid) {
        return "Tid" + invalidTid + " from list " + java.util.Arrays.toString(tids) + " doesn't belong to the calling application " + callingUid;
    }

    final class BinderService extends android.os.IHintManager.Stub {
        BinderService() {
        }

        /* JADX WARN: Removed duplicated region for block: B:127:0x01e7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:150:0x0103 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:152:0x0090 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:54:0x00fb  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x0171  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0179 A[Catch: all -> 0x011d, TRY_ENTER, TryCatch #15 {all -> 0x011d, blocks: (B:37:0x00a6, B:56:0x0103, B:69:0x0179, B:70:0x017f, B:82:0x01c3, B:87:0x01ca, B:61:0x0124, B:62:0x0149, B:64:0x014b, B:65:0x0170, B:48:0x00c4, B:49:0x00e9, B:52:0x00f1), top: B:145:0x008e, inners: #17 }] */
        /* JADX WARN: Removed duplicated region for block: B:85:0x01c6  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x01ca A[Catch: all -> 0x011d, TRY_LEAVE, TryCatch #15 {all -> 0x011d, blocks: (B:37:0x00a6, B:56:0x0103, B:69:0x0179, B:70:0x017f, B:82:0x01c3, B:87:0x01ca, B:61:0x0124, B:62:0x0149, B:64:0x014b, B:65:0x0170, B:48:0x00c4, B:49:0x00e9, B:52:0x00f1), top: B:145:0x008e, inners: #17 }] */
        /* JADX WARN: Removed duplicated region for block: B:89:0x01cd A[Catch: all -> 0x024b, TRY_ENTER, TryCatch #6 {all -> 0x024b, blocks: (B:67:0x0173, B:90:0x01d1, B:91:0x01e6, B:89:0x01cd), top: B:135:0x0173 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public android.os.IHintSession createHintSessionWithConfig(android.os.IBinder r27, int[] r28, long r29, int r31, android.hardware.power.SessionConfig r32) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 668
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.hint.HintManagerService.BinderService.createHintSessionWithConfig(android.os.IBinder, int[], long, int, android.hardware.power.SessionConfig):android.os.IHintSession");
        }

        public android.hardware.power.ChannelConfig getSessionChannel(android.os.IBinder token) {
            if (com.android.server.power.hint.HintManagerService.this.mPowerHalVersion < 5 || !com.android.internal.hidden_from_bootclasspath.android.os.Flags.adpfUseFmqChannel()) {
                return null;
            }
            java.util.Objects.requireNonNull(token);
            int callingTgid = android.os.Process.getThreadGroupLeader(android.os.Binder.getCallingPid());
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.power.hint.HintManagerService.ChannelItem item = com.android.server.power.hint.HintManagerService.this.getOrCreateMappedChannelItem(callingTgid, callingUid, token);
            return item.getConfig();
        }

        public void closeSessionChannel() {
            if (com.android.server.power.hint.HintManagerService.this.mPowerHalVersion < 5 || !com.android.internal.hidden_from_bootclasspath.android.os.Flags.adpfUseFmqChannel()) {
                return;
            }
            int callingTgid = android.os.Process.getThreadGroupLeader(android.os.Binder.getCallingPid());
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.power.hint.HintManagerService.this.removeChannelItem(java.lang.Integer.valueOf(callingTgid), java.lang.Integer.valueOf(callingUid));
        }

        public long getHintSessionPreferredRate() {
            return com.android.server.power.hint.HintManagerService.this.mHintSessionPreferredRate;
        }

        public void setHintSessionThreads(android.os.IHintSession hintSession, int[] tids) {
            com.android.server.power.hint.HintManagerService.AppHintSession appHintSession = (com.android.server.power.hint.HintManagerService.AppHintSession) hintSession;
            appHintSession.setThreads(tids);
        }

        public int[] getHintSessionThreadIds(android.os.IHintSession hintSession) {
            com.android.server.power.hint.HintManagerService.AppHintSession appHintSession = (com.android.server.power.hint.HintManagerService.AppHintSession) hintSession;
            return appHintSession.getThreadIds();
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.power.hint.HintManagerService.this.getContext(), com.android.server.power.hint.HintManagerService.TAG, pw)) {
                return;
            }
            pw.println("HintSessionPreferredRate: " + com.android.server.power.hint.HintManagerService.this.mHintSessionPreferredRate);
            pw.println("HAL Support: " + com.android.server.power.hint.HintManagerService.this.isHalSupported());
            pw.println("Active Sessions:");
            synchronized (com.android.server.power.hint.HintManagerService.this.mLock) {
                for (int i = 0; i < com.android.server.power.hint.HintManagerService.this.mActiveSessions.size(); i++) {
                    pw.println("Uid " + ((java.lang.Integer) com.android.server.power.hint.HintManagerService.this.mActiveSessions.keyAt(i)).toString() + ":");
                    android.util.ArrayMap<android.os.IBinder, android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession>> tokenMap = (android.util.ArrayMap) com.android.server.power.hint.HintManagerService.this.mActiveSessions.valueAt(i);
                    for (int j = 0; j < tokenMap.size(); j++) {
                        android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession> sessionSet = tokenMap.valueAt(j);
                        for (int k = 0; k < sessionSet.size(); k++) {
                            pw.println("  Session:");
                            sessionSet.valueAt(k).dump(pw, "    ");
                        }
                    }
                }
            }
        }

        private void logPerformanceHintSessionAtom(int uid, long sessionId, long targetDuration, int[] tids, int sessionTag) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PERFORMANCE_HINT_SESSION_REPORTED, uid, sessionId, targetDuration, tids.length, sessionTag);
        }

        private int getUidApplicationCategory(int uid) {
            try {
                java.lang.String packageName = com.android.server.power.hint.HintManagerService.this.mPackageManager.getNameForUid(uid);
                android.content.pm.ApplicationInfo applicationInfo = com.android.server.power.hint.HintManagerService.this.mPackageManager.getApplicationInfo(packageName, 131072);
                return applicationInfo.category;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return -1;
            }
        }
    }

    final class AppHintSession extends android.os.IHintSession.Stub implements android.os.IBinder.DeathRecipient {
        protected long mHalSessionPtr;
        protected int[] mNewThreadIds;
        protected final int mPid;
        protected long mTargetDurationNanos;
        protected int[] mThreadIds;
        protected final android.os.IBinder mToken;
        protected final int mUid;
        protected boolean mUpdateAllowedByProcState = true;
        protected boolean mPowerEfficient = false;
        protected boolean mShouldForcePause = false;

        private enum SessionModes {
            POWER_EFFICIENCY
        }

        protected AppHintSession(int uid, int pid, int[] threadIds, android.os.IBinder token, long halSessionPtr, long durationNanos) {
            this.mUid = uid;
            this.mPid = pid;
            this.mToken = token;
            this.mThreadIds = threadIds;
            this.mHalSessionPtr = halSessionPtr;
            this.mTargetDurationNanos = durationNanos;
            boolean allowed = com.android.server.power.hint.HintManagerService.this.mUidObserver.isUidForeground(this.mUid);
            updateHintAllowedByProcState(allowed);
            try {
                token.linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halCloseHintSession(this.mHalSessionPtr);
                throw new java.lang.IllegalStateException("Client already dead", e);
            }
        }

        boolean updateHintAllowedByProcState(boolean allowed) {
            boolean z;
            synchronized (this) {
                if (allowed) {
                    try {
                        if (!this.mUpdateAllowedByProcState && !this.mShouldForcePause) {
                            resume();
                        }
                    } catch (java.lang.Throwable th) {
                        throw th;
                    }
                }
                if (!allowed && this.mUpdateAllowedByProcState) {
                    pause();
                }
                this.mUpdateAllowedByProcState = allowed;
                z = this.mUpdateAllowedByProcState;
            }
            return z;
        }

        boolean isHintAllowed() {
            return (this.mHalSessionPtr == 0 || !this.mUpdateAllowedByProcState || this.mShouldForcePause) ? false : true;
        }

        public void updateTargetWorkDuration(long targetDurationNanos) {
            synchronized (this) {
                if (isHintAllowed()) {
                    com.android.internal.util.Preconditions.checkArgument(targetDurationNanos > 0, "Expected the target duration to be greater than 0.");
                    com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halUpdateTargetWorkDuration(this.mHalSessionPtr, targetDurationNanos);
                    this.mTargetDurationNanos = targetDurationNanos;
                }
            }
        }

        public void reportActualWorkDuration(long[] actualDurationNanos, long[] timeStampNanos) {
            synchronized (this) {
                if (isHintAllowed()) {
                    com.android.internal.util.Preconditions.checkArgument(actualDurationNanos.length != 0, "the count of hint durations shouldn't be 0.");
                    com.android.internal.util.Preconditions.checkArgument(actualDurationNanos.length == timeStampNanos.length, "The length of durations and timestamps should be the same.");
                    for (int i = 0; i < actualDurationNanos.length; i++) {
                        if (actualDurationNanos[i] <= 0) {
                            throw new java.lang.IllegalArgumentException(java.lang.String.format("durations[%d]=%d should be greater than 0", java.lang.Integer.valueOf(i), java.lang.Long.valueOf(actualDurationNanos[i])));
                        }
                    }
                    com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halReportActualWorkDuration(this.mHalSessionPtr, actualDurationNanos, timeStampNanos);
                }
            }
        }

        public void close() {
            synchronized (this) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halCloseHintSession(this.mHalSessionPtr);
                this.mHalSessionPtr = 0L;
                try {
                    this.mToken.unlinkToDeath(this, 0);
                } catch (java.util.NoSuchElementException e) {
                    com.android.server.utils.Slogf.d(com.android.server.power.hint.HintManagerService.TAG, "Death link does not exist for session with UID " + this.mUid);
                }
                synchronized (com.android.server.power.hint.HintManagerService.this.mLock) {
                    android.util.ArrayMap<android.os.IBinder, android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession>> tokenMap = (android.util.ArrayMap) com.android.server.power.hint.HintManagerService.this.mActiveSessions.get(java.lang.Integer.valueOf(this.mUid));
                    if (tokenMap == null) {
                        com.android.server.utils.Slogf.w(com.android.server.power.hint.HintManagerService.TAG, "UID %d is not present in active session map", java.lang.Integer.valueOf(this.mUid));
                        return;
                    }
                    android.util.ArraySet<com.android.server.power.hint.HintManagerService.AppHintSession> sessionSet = tokenMap.get(this.mToken);
                    if (sessionSet == null) {
                        com.android.server.utils.Slogf.w(com.android.server.power.hint.HintManagerService.TAG, "Token %s is not present in token map", this.mToken.toString());
                        return;
                    }
                    sessionSet.remove(this);
                    if (sessionSet.isEmpty()) {
                        tokenMap.remove(this.mToken);
                    }
                    if (tokenMap.isEmpty()) {
                        com.android.server.power.hint.HintManagerService.this.mActiveSessions.remove(java.lang.Integer.valueOf(this.mUid));
                    }
                    if (com.android.server.power.hint.Flags.powerhintThreadCleanup()) {
                        synchronized (com.android.server.power.hint.HintManagerService.this.mNonIsolatedTidsLock) {
                            int[] tids = getTidsInternal();
                            for (int tid : tids) {
                                if (com.android.server.power.hint.HintManagerService.this.mNonIsolatedTids.containsKey(java.lang.Integer.valueOf(tid))) {
                                    ((java.util.Set) com.android.server.power.hint.HintManagerService.this.mNonIsolatedTids.get(java.lang.Integer.valueOf(tid))).remove(java.lang.Long.valueOf(this.mHalSessionPtr));
                                    if (((java.util.Set) com.android.server.power.hint.HintManagerService.this.mNonIsolatedTids.get(java.lang.Integer.valueOf(tid))).isEmpty()) {
                                        com.android.server.power.hint.HintManagerService.this.mNonIsolatedTids.remove(java.lang.Integer.valueOf(tid));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public void sendHint(int hint) {
            synchronized (this) {
                if (isHintAllowed()) {
                    com.android.internal.util.Preconditions.checkArgument(hint >= 0, "the hint ID value should be greater than zero.");
                    com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halSendHint(this.mHalSessionPtr, hint);
                }
            }
        }

        public void setThreads(int[] tids) {
            setThreadsInternal(tids, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setThreadsInternal(int[] tids, boolean checkTid) {
            if (tids.length == 0) {
                throw new java.lang.IllegalArgumentException("Thread id list can't be empty.");
            }
            synchronized (this) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                if (!this.mUpdateAllowedByProcState) {
                    com.android.server.utils.Slogf.v(com.android.server.power.hint.HintManagerService.TAG, "update hint not allowed, storing tids.");
                    this.mNewThreadIds = tids;
                    this.mShouldForcePause = false;
                    return;
                }
                if (checkTid) {
                    int callingUid = android.os.Binder.getCallingUid();
                    int callingTgid = android.os.Process.getThreadGroupLeader(android.os.Binder.getCallingPid());
                    android.util.IntArray nonIsolated = com.android.server.power.hint.Flags.powerhintThreadCleanup() ? new android.util.IntArray() : null;
                    long identity = android.os.Binder.clearCallingIdentity();
                    try {
                        java.lang.Integer invalidTid = com.android.server.power.hint.HintManagerService.this.checkTidValid(callingUid, callingTgid, tids, nonIsolated);
                        if (invalidTid != null) {
                            java.lang.String errMsg = com.android.server.power.hint.HintManagerService.this.formatTidCheckErrMsg(callingUid, tids, invalidTid);
                            com.android.server.utils.Slogf.w(com.android.server.power.hint.HintManagerService.TAG, errMsg);
                            throw new java.lang.SecurityException(errMsg);
                        }
                        if (com.android.server.power.hint.Flags.powerhintThreadCleanup()) {
                            synchronized (com.android.server.power.hint.HintManagerService.this.mNonIsolatedTidsLock) {
                                for (int i = nonIsolated.size() - 1; i >= 0; i--) {
                                    com.android.server.power.hint.HintManagerService.this.mNonIsolatedTids.putIfAbsent(java.lang.Integer.valueOf(nonIsolated.get(i)), new android.util.ArraySet());
                                    ((java.util.Set) com.android.server.power.hint.HintManagerService.this.mNonIsolatedTids.get(java.lang.Integer.valueOf(nonIsolated.get(i)))).add(java.lang.Long.valueOf(this.mHalSessionPtr));
                                }
                            }
                        }
                    } finally {
                        android.os.Binder.restoreCallingIdentity(identity);
                    }
                }
                com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halSetThreads(this.mHalSessionPtr, tids);
                this.mThreadIds = tids;
                this.mNewThreadIds = null;
                if (this.mShouldForcePause) {
                    resume();
                    this.mShouldForcePause = false;
                }
            }
        }

        public int[] getThreadIds() {
            int[] iArrCopyOf;
            synchronized (this) {
                iArrCopyOf = java.util.Arrays.copyOf(this.mThreadIds, this.mThreadIds.length);
            }
            return iArrCopyOf;
        }

        int[] getTidsInternal() {
            int[] iArrCopyOf;
            synchronized (this) {
                iArrCopyOf = this.mNewThreadIds != null ? java.util.Arrays.copyOf(this.mNewThreadIds, this.mNewThreadIds.length) : java.util.Arrays.copyOf(this.mThreadIds, this.mThreadIds.length);
            }
            return iArrCopyOf;
        }

        boolean isClosed() {
            boolean z;
            synchronized (this) {
                z = this.mHalSessionPtr == 0;
            }
            return z;
        }

        boolean isForcePaused() {
            boolean z;
            synchronized (this) {
                z = this.mShouldForcePause;
            }
            return z;
        }

        public void setMode(int mode, boolean enabled) {
            synchronized (this) {
                if (isHintAllowed()) {
                    com.android.internal.util.Preconditions.checkArgument(mode >= 0, "the mode Id value should be greater than zero.");
                    if (mode == com.android.server.power.hint.HintManagerService.AppHintSession.SessionModes.POWER_EFFICIENCY.ordinal()) {
                        this.mPowerEfficient = enabled;
                    }
                    com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halSetMode(this.mHalSessionPtr, mode, enabled);
                }
            }
        }

        public void reportActualWorkDuration2(android.hardware.power.WorkDuration[] workDurations) {
            synchronized (this) {
                if (isHintAllowed()) {
                    com.android.internal.util.Preconditions.checkArgument(workDurations.length != 0, "the count of work durations shouldn't be 0.");
                    for (android.hardware.power.WorkDuration workDuration : workDurations) {
                        validateWorkDuration(workDuration);
                    }
                    com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halReportActualWorkDuration(this.mHalSessionPtr, workDurations);
                }
            }
        }

        public boolean isPowerEfficient() {
            boolean z;
            synchronized (this) {
                z = this.mPowerEfficient;
            }
            return z;
        }

        void validateWorkDuration(android.hardware.power.WorkDuration workDuration) {
            if (workDuration.durationNanos <= 0) {
                throw new java.lang.IllegalArgumentException(android.text.TextUtils.formatSimple("Actual total duration (%d) should be greater than 0", new java.lang.Object[]{java.lang.Long.valueOf(workDuration.durationNanos)}));
            }
            if (workDuration.workPeriodStartTimestampNanos < 0) {
                throw new java.lang.IllegalArgumentException(android.text.TextUtils.formatSimple("Work period start timestamp (%d) should be greater than 0", new java.lang.Object[]{java.lang.Long.valueOf(workDuration.workPeriodStartTimestampNanos)}));
            }
            if (workDuration.cpuDurationNanos < 0) {
                throw new java.lang.IllegalArgumentException(android.text.TextUtils.formatSimple("Actual CPU duration (%d) should be greater than or equal to 0", new java.lang.Object[]{java.lang.Long.valueOf(workDuration.cpuDurationNanos)}));
            }
            if (workDuration.gpuDurationNanos < 0) {
                throw new java.lang.IllegalArgumentException(android.text.TextUtils.formatSimple("Actual GPU duration (%d) should greater than or equal to 0", new java.lang.Object[]{java.lang.Long.valueOf(workDuration.gpuDurationNanos)}));
            }
            if (workDuration.cpuDurationNanos + workDuration.gpuDurationNanos <= 0) {
                throw new java.lang.IllegalArgumentException(android.text.TextUtils.formatSimple("The actual CPU duration (%d) and the actual GPU duration (%d) should not both be 0", new java.lang.Object[]{java.lang.Long.valueOf(workDuration.cpuDurationNanos), java.lang.Long.valueOf(workDuration.gpuDurationNanos)}));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pause() {
            synchronized (this) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halPauseHintSession(this.mHalSessionPtr);
            }
        }

        private void resume() {
            synchronized (this) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halResumeHintSession(this.mHalSessionPtr);
                if (this.mNewThreadIds != null) {
                    com.android.server.power.hint.HintManagerService.this.mNativeWrapper.halSetThreads(this.mHalSessionPtr, this.mNewThreadIds);
                    this.mThreadIds = this.mNewThreadIds;
                    this.mNewThreadIds = null;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            synchronized (this) {
                pw.println(prefix + "SessionPID: " + this.mPid);
                pw.println(prefix + "SessionUID: " + this.mUid);
                pw.println(prefix + "SessionTIDs: " + java.util.Arrays.toString(this.mThreadIds));
                pw.println(prefix + "SessionTargetDurationNanos: " + this.mTargetDurationNanos);
                pw.println(prefix + "SessionAllowedByProcState: " + this.mUpdateAllowedByProcState);
                pw.println(prefix + "SessionForcePaused: " + this.mShouldForcePause);
                pw.println(prefix + "PowerEfficient: " + (this.mPowerEfficient ? "true" : "false"));
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            close();
        }
    }
}
