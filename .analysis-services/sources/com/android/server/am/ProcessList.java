package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class ProcessList {
    static final java.lang.String ANDROID_APP_DATA_ISOLATION_ENABLED_PROPERTY = "persist.zygote.app_data_isolation";
    static final java.lang.String ANDROID_VOLD_APP_DATA_ISOLATION_ENABLED_PROPERTY = "persist.sys.vold_app_data_isolation_enabled";
    private static final java.lang.String APPLY_SDK_SANDBOX_AUDIT_RESTRICTIONS = ":isSdkSandboxAudit";
    private static final java.lang.String APPLY_SDK_SANDBOX_NEXT_RESTRICTIONS = ":isSdkSandboxNext";
    private static final long APP_DATA_DIRECTORY_ISOLATION = 143937733;
    public static final int BACKUP_APP_ADJ = 300;
    public static final int CACHED_APP_IMPORTANCE_LEVELS = 5;
    public static final int CACHED_APP_LMK_FIRST_ADJ = 950;
    public static final int CACHED_APP_MAX_ADJ = 999;
    public static final int CACHED_APP_MIN_ADJ = 900;
    private static final boolean DEFAULT_APPLY_SDK_SANDBOX_AUDIT_RESTRICTIONS = false;
    private static final boolean DEFAULT_APPLY_SDK_SANDBOX_NEXT_RESTRICTIONS = false;
    public static final int FOREGROUND_APP_ADJ = 0;
    static final int FREEZER_CUTOFF_ADJ = 900;
    public static final int HEAVY_WEIGHT_APP_ADJ = 400;
    public static final int HOME_APP_ADJ = 600;
    public static final int INVALID_ADJ = -10000;
    private static final long LMKD_RECONNECT_DELAY_MS = 1000;
    static final int LMK_ASYNC_EVENT_KILL = 0;
    static final int LMK_ASYNC_EVENT_STAT = 1;
    static final byte LMK_BOOT_COMPLETED = 10;
    static final byte LMK_GETKILLCNT = 4;
    static final byte LMK_KILL_OCCURRED = 8;
    static final byte LMK_PROCKILL = 6;
    static final byte LMK_PROCPRIO = 1;
    static final byte LMK_PROCPURGE = 3;
    static final byte LMK_PROCREMOVE = 2;
    static final byte LMK_PROCS_PRIO = 11;
    static final byte LMK_START_MONITORING = 9;
    static final byte LMK_SUBSCRIBE = 5;
    static final byte LMK_TARGET = 0;
    static final byte LMK_UPDATE_PROPS = 7;
    private static final int MAX_OOM_ADJ_BATCH_LENGTH = 52;
    private static final int MAX_PROCS_PRIO_PACKET_SIZE = 3;
    private static final int MAX_ZYGOTE_UNSOLICITED_MESSAGE_SIZE = 16;
    static final int MIN_CACHED_APPS = 2;
    public static final int NATIVE_ADJ = -1000;
    static final int NETWORK_STATE_BLOCK = 1;
    static final int NETWORK_STATE_NO_CHANGE = 0;
    static final int NETWORK_STATE_UNBLOCK = 2;
    public static final int PERCEPTIBLE_APP_ADJ = 200;
    public static final int PERCEPTIBLE_LOW_APP_ADJ = 250;
    public static final int PERCEPTIBLE_MEDIUM_APP_ADJ = 225;
    public static final int PERCEPTIBLE_RECENT_FOREGROUND_APP_ADJ = 50;
    public static final int PERSISTENT_PROC_ADJ = -800;
    public static final int PERSISTENT_SERVICE_ADJ = -700;
    public static final int PREVIOUS_APP_ADJ = 700;
    public static final int PROC_MEM_CACHED = 4;
    public static final int PROC_MEM_IMPORTANT = 2;
    public static final int PROC_MEM_NUM = 5;
    public static final int PROC_MEM_PERSISTENT = 0;
    public static final int PROC_MEM_SERVICE = 3;
    public static final int PROC_MEM_TOP = 1;
    private static final java.lang.String PROPERTY_APPLY_SDK_SANDBOX_AUDIT_RESTRICTIONS = "apply_sdk_sandbox_audit_restrictions";
    private static final java.lang.String PROPERTY_APPLY_SDK_SANDBOX_NEXT_RESTRICTIONS = "apply_sdk_sandbox_next_restrictions";
    private static final java.lang.String PROPERTY_USE_APP_IMAGE_STARTUP_CACHE = "persist.device_config.runtime_native.use_app_image_startup_cache";
    public static final int PSS_ALL_INTERVAL = 1200000;
    private static final int PSS_FIRST_ASLEEP_BACKGROUND_INTERVAL = 30000;
    private static final int PSS_FIRST_ASLEEP_CACHED_INTERVAL = 60000;
    private static final int PSS_FIRST_ASLEEP_PERSISTENT_INTERVAL = 60000;
    private static final int PSS_FIRST_ASLEEP_TOP_INTERVAL = 20000;
    private static final int PSS_FIRST_BACKGROUND_INTERVAL = 20000;
    private static final int PSS_FIRST_CACHED_INTERVAL = 20000;
    private static final int PSS_FIRST_PERSISTENT_INTERVAL = 30000;
    private static final int PSS_FIRST_TOP_INTERVAL = 10000;
    public static final int PSS_MAX_INTERVAL = 3600000;
    public static final int PSS_MIN_TIME_FROM_STATE_CHANGE = 15000;
    public static final int PSS_SAFE_TIME_FROM_STATE_CHANGE = 1000;
    private static final int PSS_SAME_CACHED_INTERVAL = 600000;
    private static final int PSS_SAME_IMPORTANT_INTERVAL = 600000;
    private static final int PSS_SAME_PERSISTENT_INTERVAL = 600000;
    private static final int PSS_SAME_SERVICE_INTERVAL = 300000;
    private static final int PSS_SAME_TOP_INTERVAL = 60000;
    private static final int PSS_TEST_FIRST_BACKGROUND_INTERVAL = 5000;
    private static final int PSS_TEST_FIRST_TOP_INTERVAL = 3000;
    public static final int PSS_TEST_MIN_TIME_FROM_STATE_CHANGE = 10000;
    private static final int PSS_TEST_SAME_BACKGROUND_INTERVAL = 15000;
    private static final int PSS_TEST_SAME_IMPORTANT_INTERVAL = 10000;
    static final int SCHED_GROUP_BACKGROUND = 0;
    static final int SCHED_GROUP_DEFAULT = 2;
    static final int SCHED_GROUP_RESTRICTED = 1;
    public static final int SCHED_GROUP_TOP_APP = 3;
    static final int SCHED_GROUP_TOP_APP_BOUND = 4;
    static final int SCHED_GROUP_UNDEFINED = Integer.MIN_VALUE;
    public static final int SERVICE_ADJ = 500;
    public static final int SERVICE_B_ADJ = 800;
    public static final int SYSTEM_ADJ = -900;
    static final java.lang.String TAG = "ActivityManager";
    static final int TRIM_CRITICAL_THRESHOLD = 3;
    static final int TRIM_LOW_THRESHOLD = 5;
    public static final int UNKNOWN_ADJ = 1001;
    private static final java.lang.String UNSOL_ZYGOTE_MSG_SOCKET_PATH = "/data/system/unsolzygotesocket";
    public static final int VISIBLE_APP_ADJ = 100;
    static final int VISIBLE_APP_LAYER_MAX = 99;
    private static final int mMaxReserve = 65536;
    com.android.server.am.ActiveUids mActiveUids;
    private java.util.ArrayList<java.lang.String> mAppDataIsolationAllowlistedApps;
    private long mCachedRestoreLevel;
    private boolean mHaveDisplaySize;
    com.android.server.am.ProcessList.ImperceptibleKillRunner mImperceptibleKillRunner;
    com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private com.android.server.am.ProcessList.ProcessListSettingsListener mProcessListSettingsListener;
    private android.net.LocalSocket mSystemServerSocketForZygote;
    private final long mTotalMemMb;
    static final java.lang.String TAG_PROCESS_OBSERVERS = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_PROCESS_OBSERVERS;
    static final int PAGE_SIZE = (int) android.system.Os.sysconf(android.system.OsConstants._SC_PAGESIZE);
    private static boolean LTW_DISABLE = android.os.SystemProperties.getBoolean("persist.sys.ltw.disable", false);
    static com.android.server.am.ProcessList.KillHandler sKillHandler = null;
    static com.android.server.ServiceThread sKillThread = null;
    private static com.android.server.am.LmkdConnection sLmkdConnection = null;
    private static com.android.server.am.OomConnection sOomConnection = null;
    private static final int[] sProcStateToProcMem = {0, 0, 1, 2, 1, 2, 2, 2, 2, 2, 3, 4, 1, 2, 4, 4, 4, 4, 4, 4};
    private static final long[] sFirstAwakePssTimes = {30000, 10000, 20000, 20000, 20000};
    private static final long[] sSameAwakePssTimes = {600000, 60000, 600000, 300000, 600000};
    private static final long[] sFirstAsleepPssTimes = {60000, 20000, 30000, 30000, 60000};
    private static final long[] sSameAsleepPssTimes = {600000, 60000, 600000, 300000, 600000};
    private static final long[] sTestFirstPssTimes = {3000, 3000, 5000, 5000, 5000};
    private static final long[] sTestSamePssTimes = {15000, 10000, 10000, 15000, 15000};
    private static com.android.server.am.IProcessListExt.IStaticExt mStaticExt = (com.android.server.am.IProcessListExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessListExt.IStaticExt.class).create();
    com.android.server.am.ActivityManagerService mService = null;
    private final int[] mOomAdj = {0, 100, 200, 250, 900, CACHED_APP_LMK_FIRST_ADJ};
    private final int[] mOomMinFreeLow = {12288, 18432, 24576, 36864, 43008, 49152};
    private final int[] mOomMinFreeHigh = {73728, 92160, 110592, 129024, 147456, 184320};
    private final int[] mOomMinFree = new int[this.mOomAdj.length];
    private boolean mOomLevelsSet = false;
    private boolean mAppDataIsolationEnabled = false;
    private boolean mVoldAppDataIsolationEnabled = false;
    final java.lang.StringBuilder mStringBuilder = new java.lang.StringBuilder(256);
    volatile long mProcStateSeqCounter = 0;
    private long mProcStartSeqCounter = 0;
    final android.util.LongSparseArray<com.android.server.am.ProcessRecord> mPendingStarts = new android.util.LongSparseArray<>();
    public final java.util.ArrayList<com.android.server.am.ProcessRecord> mLruProcesses = new java.util.ArrayList<>();
    private int mLruProcessActivityStart = 0;
    private int mLruProcessServiceStart = 0;
    private int mLruSeq = 0;
    final android.util.SparseArray<com.android.server.am.ProcessRecord> mIsolatedProcesses = new android.util.SparseArray<>();
    final com.android.internal.app.ProcessMap<android.os.AppZygote> mAppZygotes = new com.android.internal.app.ProcessMap<>();
    private final com.android.server.am.AppStartInfoTracker mAppStartInfoTracker = new com.android.server.am.AppStartInfoTracker();
    final android.util.SparseArray<java.util.ArrayList<com.android.server.am.ProcessRecord>> mSdkSandboxes = new android.util.SparseArray<>();
    public final com.android.server.am.AppExitInfoTracker mAppExitInfoTracker = new com.android.server.am.AppExitInfoTracker();
    final android.util.ArrayMap<android.os.AppZygote, java.util.ArrayList<com.android.server.am.ProcessRecord>> mAppZygoteProcesses = new android.util.ArrayMap<>();
    final android.util.ArraySet<com.android.server.am.ProcessRecord> mAppsInBackgroundRestricted = new android.util.ArraySet<>();
    private com.android.server.compat.PlatformCompat mPlatformCompat = null;
    private final byte[] mZygoteUnsolicitedMessage = new byte[16];
    private final int[] mZygoteSigChldMessage = new int[3];
    com.android.server.am.ProcessList.IsolatedUidRange mGlobalIsolatedUids = new com.android.server.am.ProcessList.IsolatedUidRange(99000, 99999);
    com.android.server.am.ProcessList.IsolatedUidRangeAllocator mAppIsolatedUidRangeAllocator = new com.android.server.am.ProcessList.IsolatedUidRangeAllocator(90000, 98999, 100);
    final java.util.ArrayList<com.android.server.am.ProcessRecord> mRemovedProcesses = new java.util.ArrayList<>();
    final com.android.internal.app.ProcessMap<com.android.server.am.ProcessRecord> mDyingProcesses = new com.android.internal.app.ProcessMap<>();
    private final android.os.RemoteCallbackList<android.app.IProcessObserver> mProcessObservers = new android.os.RemoteCallbackList<>();
    private com.android.server.am.ActivityManagerService.ProcessChangeItem[] mActiveProcessChanges = new com.android.server.am.ActivityManagerService.ProcessChangeItem[5];
    private final java.util.ArrayList<com.android.server.am.ActivityManagerService.ProcessChangeItem> mPendingProcessChanges = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.am.ActivityManagerService.ProcessChangeItem> mAvailProcessChanges = new java.util.ArrayList<>();
    private final java.lang.Object mProcessChangeLock = new java.lang.Object();
    private final com.android.server.am.ProcessList.MyProcessMap mProcessNames = new com.android.server.am.ProcessList.MyProcessMap();
    private com.android.server.am.ProcessList.ProcessListWrapper mProcListWrapper = new com.android.server.am.ProcessList.ProcessListWrapper();
    private com.android.server.am.IProcessListExt mProcessListExt = (com.android.server.am.IProcessListExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessListExt.class).base(this).create();
    private com.android.server.am.IProcessListSocExt mSocExt = (com.android.server.am.IProcessListSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessListSocExt.class).base(this).create();

    com.android.server.am.ProcessList.ProcessListSettingsListener getProcessListSettingsListener() {
        com.android.server.am.ProcessList.ProcessListSettingsListener processListSettingsListener;
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (this.mProcessListSettingsListener == null) {
                    this.mProcessListSettingsListener = new com.android.server.am.ProcessList.ProcessListSettingsListener(this.mService.mContext);
                    this.mProcessListSettingsListener.registerObserver();
                }
                processListSettingsListener = this.mProcessListSettingsListener;
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        return processListSettingsListener;
    }

    static class ProcessListSettingsListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private final android.content.Context mContext;
        private final java.lang.Object mLock = new java.lang.Object();
        private boolean mSdkSandboxApplyRestrictionsAudit = android.provider.DeviceConfig.getBoolean("adservices", com.android.server.am.ProcessList.PROPERTY_APPLY_SDK_SANDBOX_AUDIT_RESTRICTIONS, false);
        private boolean mSdkSandboxApplyRestrictionsNext = android.provider.DeviceConfig.getBoolean("adservices", com.android.server.am.ProcessList.PROPERTY_APPLY_SDK_SANDBOX_NEXT_RESTRICTIONS, false);

        ProcessListSettingsListener(android.content.Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerObserver() {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("adservices", this.mContext.getMainExecutor(), this);
        }

        void unregisterObserver() {
            android.provider.DeviceConfig.removeOnPropertiesChangedListener(this);
        }

        boolean applySdkSandboxRestrictionsAudit() {
            boolean z;
            synchronized (this.mLock) {
                z = this.mSdkSandboxApplyRestrictionsAudit;
            }
            return z;
        }

        boolean applySdkSandboxRestrictionsNext() {
            boolean z;
            synchronized (this.mLock) {
                z = this.mSdkSandboxApplyRestrictionsNext;
            }
            return z;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0022  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties r6) {
            /*
                r5 = this;
                java.lang.Object r0 = r5.mLock
                monitor-enter(r0)
                java.util.Set r1 = r6.getKeyset()     // Catch: java.lang.Throwable -> L51
                java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> L51
            Lb:
                boolean r2 = r1.hasNext()     // Catch: java.lang.Throwable -> L51
                if (r2 == 0) goto L4f
                java.lang.Object r2 = r1.next()     // Catch: java.lang.Throwable -> L51
                java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Throwable -> L51
                if (r2 != 0) goto L1a
                goto Lb
            L1a:
                int r3 = r2.hashCode()     // Catch: java.lang.Throwable -> L51
                r4 = 0
                switch(r3) {
                    case -460166235: goto L2d;
                    case 1346273945: goto L23;
                    default: goto L22;
                }     // Catch: java.lang.Throwable -> L51
            L22:
                goto L37
            L23:
                java.lang.String r3 = "apply_sdk_sandbox_audit_restrictions"
                boolean r3 = r2.equals(r3)     // Catch: java.lang.Throwable -> L51
                if (r3 == 0) goto L22
                r3 = r4
                goto L38
            L2d:
                java.lang.String r3 = "apply_sdk_sandbox_next_restrictions"
                boolean r3 = r2.equals(r3)     // Catch: java.lang.Throwable -> L51
                if (r3 == 0) goto L22
                r3 = 1
                goto L38
            L37:
                r3 = -1
            L38:
                switch(r3) {
                    case 0: goto L45;
                    case 1: goto L3c;
                    default: goto L3b;
                }     // Catch: java.lang.Throwable -> L51
            L3b:
                goto L4e
            L3c:
                java.lang.String r3 = "apply_sdk_sandbox_next_restrictions"
                boolean r3 = r6.getBoolean(r3, r4)     // Catch: java.lang.Throwable -> L51
                r5.mSdkSandboxApplyRestrictionsNext = r3     // Catch: java.lang.Throwable -> L51
                goto L4e
            L45:
                java.lang.String r3 = "apply_sdk_sandbox_audit_restrictions"
                boolean r3 = r6.getBoolean(r3, r4)     // Catch: java.lang.Throwable -> L51
                r5.mSdkSandboxApplyRestrictionsAudit = r3     // Catch: java.lang.Throwable -> L51
            L4e:
                goto Lb
            L4f:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L51
                return
            L51:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L51
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ProcessList.ProcessListSettingsListener.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
        }
    }

    final class IsolatedUidRange {
        public final int mFirstUid;
        public final int mLastUid;
        private int mNextUid;
        private final android.util.SparseBooleanArray mUidUsed = new android.util.SparseBooleanArray();

        IsolatedUidRange(int firstUid, int lastUid) {
            this.mFirstUid = firstUid;
            this.mLastUid = lastUid;
            this.mNextUid = firstUid;
        }

        int allocateIsolatedUidLocked(int userId) {
            int stepsLeft = (this.mLastUid - this.mFirstUid) + 1;
            for (int i = 0; i < stepsLeft; i++) {
                if (this.mNextUid < this.mFirstUid || this.mNextUid > this.mLastUid) {
                    this.mNextUid = this.mFirstUid;
                }
                int uid = android.os.UserHandle.getUid(userId, this.mNextUid);
                this.mNextUid++;
                if (!this.mUidUsed.get(uid, false)) {
                    this.mUidUsed.put(uid, true);
                    return uid;
                }
            }
            return -1;
        }

        void freeIsolatedUidLocked(int uid) {
            this.mUidUsed.delete(uid);
        }
    }

    final class IsolatedUidRangeAllocator {
        private final com.android.internal.app.ProcessMap<com.android.server.am.ProcessList.IsolatedUidRange> mAppRanges = new com.android.internal.app.ProcessMap<>();
        private final java.util.BitSet mAvailableUidRanges;
        private final int mFirstUid;
        private final int mNumUidRanges;
        private final int mNumUidsPerRange;

        IsolatedUidRangeAllocator(int firstUid, int lastUid, int numUidsPerRange) {
            this.mFirstUid = firstUid;
            this.mNumUidsPerRange = numUidsPerRange;
            this.mNumUidRanges = ((lastUid - firstUid) + 1) / numUidsPerRange;
            this.mAvailableUidRanges = new java.util.BitSet(this.mNumUidRanges);
            this.mAvailableUidRanges.set(0, this.mNumUidRanges);
        }

        com.android.server.am.ProcessList.IsolatedUidRange getIsolatedUidRangeLocked(java.lang.String processName, int uid) {
            return (com.android.server.am.ProcessList.IsolatedUidRange) this.mAppRanges.get(processName, uid);
        }

        com.android.server.am.ProcessList.IsolatedUidRange getOrCreateIsolatedUidRangeLocked(java.lang.String processName, int uid) {
            com.android.server.am.ProcessList.IsolatedUidRange range = getIsolatedUidRangeLocked(processName, uid);
            if (range == null) {
                int uidRangeIndex = this.mAvailableUidRanges.nextSetBit(0);
                if (uidRangeIndex < 0) {
                    return null;
                }
                this.mAvailableUidRanges.clear(uidRangeIndex);
                int actualUid = this.mFirstUid + (this.mNumUidsPerRange * uidRangeIndex);
                com.android.server.am.ProcessList.IsolatedUidRange range2 = com.android.server.am.ProcessList.this.new IsolatedUidRange(actualUid, (this.mNumUidsPerRange + actualUid) - 1);
                this.mAppRanges.put(processName, uid, range2);
                return range2;
            }
            return range;
        }

        void freeUidRangeLocked(android.content.pm.ApplicationInfo info) {
            com.android.server.am.ProcessList.IsolatedUidRange range = (com.android.server.am.ProcessList.IsolatedUidRange) this.mAppRanges.get(info.processName, info.uid);
            if (range != null) {
                int uidRangeIndex = (range.mFirstUid - this.mFirstUid) / this.mNumUidsPerRange;
                this.mAvailableUidRanges.set(uidRangeIndex);
                this.mAppRanges.remove(info.processName, info.uid);
            }
        }
    }

    final class MyProcessMap extends com.android.internal.app.ProcessMap<com.android.server.am.ProcessRecord> {
        MyProcessMap() {
        }

        public com.android.server.am.ProcessRecord put(java.lang.String name, int uid, com.android.server.am.ProcessRecord value) {
            com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) super.put(name, uid, value);
            com.android.server.am.ProcessList.this.mService.mAtmInternal.onProcessAdded(r.getWindowProcessController());
            return r;
        }

        /* JADX INFO: renamed from: remove, reason: merged with bridge method [inline-methods] */
        public com.android.server.am.ProcessRecord m1524remove(java.lang.String name, int uid) {
            com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) super.remove(name, uid);
            com.android.server.am.ProcessList.this.mService.mAtmInternal.onProcessRemoved(name, uid);
            return r;
        }
    }

    final class KillHandler extends android.os.Handler {
        static final int KILL_PROCESS_GROUP_MSG = 4000;
        static final int LMKD_RECONNECT_MSG = 4001;

        public KillHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case KILL_PROCESS_GROUP_MSG /* 4000 */:
                    android.os.Trace.traceBegin(64L, "killProcessGroup");
                    android.os.Process.killProcessGroup(msg.arg1, msg.arg2);
                    android.os.Trace.traceEnd(64L);
                    break;
                case LMKD_RECONNECT_MSG /* 4001 */:
                    if (!com.android.server.am.ProcessList.sLmkdConnection.connect()) {
                        android.util.Slog.i("ActivityManager", "Failed to connect to lmkd, retry after 1000 ms");
                        com.android.server.am.ProcessList.sKillHandler.sendMessageDelayed(com.android.server.am.ProcessList.sKillHandler.obtainMessage(LMKD_RECONNECT_MSG), 1000L);
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    ProcessList() {
        com.android.internal.util.MemInfoReader minfo = new com.android.internal.util.MemInfoReader();
        minfo.readMemInfo();
        this.mTotalMemMb = minfo.getTotalSize() / 1048576;
        updateOomLevels(0, 0, false);
    }

    void init(com.android.server.am.ActivityManagerService service, com.android.server.am.ActiveUids activeUids, com.android.server.compat.PlatformCompat platformCompat) {
        this.mService = service;
        this.mActiveUids = activeUids;
        this.mPlatformCompat = platformCompat;
        this.mProcLock = service.mProcLock;
        this.mAppDataIsolationEnabled = android.os.SystemProperties.getBoolean(ANDROID_APP_DATA_ISOLATION_ENABLED_PROPERTY, true);
        this.mVoldAppDataIsolationEnabled = android.os.SystemProperties.getBoolean(ANDROID_VOLD_APP_DATA_ISOLATION_ENABLED_PROPERTY, false);
        if (sKillHandler == null) {
            sKillThread = new com.android.server.ServiceThread("ActivityManager:kill", 10, true);
            sKillThread.start();
            sKillHandler = new com.android.server.am.ProcessList.KillHandler(sKillThread.getLooper());
            this.mProcessListExt.setThreadSchedPolicy(sKillThread.getThreadId(), "ActivityManager:kill", 14);
            sOomConnection = new com.android.server.am.OomConnection(new com.android.server.am.OomConnection.OomConnectionListener() { // from class: com.android.server.am.ProcessList.1
                @Override // com.android.server.am.OomConnection.OomConnectionListener
                public void handleOomEvent(android.os.OomKillRecord[] oomKills) {
                    for (android.os.OomKillRecord oomKill : oomKills) {
                        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ProcessList.this.mProcLock;
                        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                        synchronized (activityManagerGlobalLock) {
                            try {
                                com.android.server.am.ProcessList.this.noteAppKill(oomKill.getPid(), oomKill.getUid(), 3, 30, "oom");
                                oomKill.logKillOccurred();
                            } catch (java.lang.Throwable th) {
                                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                throw th;
                            }
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    }
                }
            });
            sLmkdConnection = new com.android.server.am.LmkdConnection(sKillThread.getLooper().getQueue(), new com.android.server.am.LmkdConnection.LmkdConnectionListener() { // from class: com.android.server.am.ProcessList.2
                @Override // com.android.server.am.LmkdConnection.LmkdConnectionListener
                public boolean onConnect(java.io.OutputStream ostream) {
                    android.util.Slog.i("ActivityManager", "Connection with lmkd established");
                    return com.android.server.am.ProcessList.this.onLmkdConnect(ostream);
                }

                @Override // com.android.server.am.LmkdConnection.LmkdConnectionListener
                public void onDisconnect() {
                    android.util.Slog.w("ActivityManager", "Lost connection to lmkd");
                    com.android.server.am.ProcessList.sKillHandler.sendMessageDelayed(com.android.server.am.ProcessList.sKillHandler.obtainMessage(4001), 1000L);
                }

                @Override // com.android.server.am.LmkdConnection.LmkdConnectionListener
                public boolean isReplyExpected(java.nio.ByteBuffer replyBuf, java.nio.ByteBuffer dataReceived, int receivedLen) {
                    return receivedLen == replyBuf.array().length && dataReceived.getInt(0) == replyBuf.getInt(0);
                }

                @Override // com.android.server.am.LmkdConnection.LmkdConnectionListener
                public boolean handleUnsolicitedMessage(java.io.DataInputStream inputData, int receivedLen) {
                    if (receivedLen < 4) {
                        return false;
                    }
                    try {
                        switch (inputData.readInt()) {
                            case 6:
                                if (receivedLen == 12) {
                                    int pid = inputData.readInt();
                                    int uid = inputData.readInt();
                                    com.android.server.am.ProcessList.this.mAppExitInfoTracker.scheduleNoteLmkdProcKilled(pid, uid);
                                    break;
                                }
                                break;
                            case 8:
                                if (receivedLen >= 80) {
                                    android.util.Pair<java.lang.Integer, java.lang.Integer> foregroundServices = com.android.server.am.ActiveServices.sNumForegroundServices.get();
                                    com.android.server.am.LmkdStatsReporter.logKillOccurred(inputData, ((java.lang.Integer) foregroundServices.first).intValue(), ((java.lang.Integer) foregroundServices.second).intValue());
                                    break;
                                }
                                break;
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e("ActivityManager", "Invalid buffer data. Failed to log LMK_KILL_OCCURRED");
                        return false;
                    }
                    return false;
                }
            });
            this.mSystemServerSocketForZygote = createSystemServerSocketForZygote();
            if (this.mSystemServerSocketForZygote != null) {
                sKillHandler.getLooper().getQueue().addOnFileDescriptorEventListener(this.mSystemServerSocketForZygote.getFileDescriptor(), 1, new android.os.MessageQueue.OnFileDescriptorEventListener() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda7
                    @Override // android.os.MessageQueue.OnFileDescriptorEventListener
                    public final int onFileDescriptorEvents(java.io.FileDescriptor fileDescriptor, int i) {
                        return this.f$0.handleZygoteMessages(fileDescriptor, i);
                    }
                });
            }
            this.mAppStartInfoTracker.init(this.mService);
            this.mAppExitInfoTracker.init(this.mService);
            this.mImperceptibleKillRunner = new com.android.server.am.ProcessList.ImperceptibleKillRunner(sKillThread.getLooper());
        }
    }

    void onSystemReady() {
        this.mAppStartInfoTracker.onSystemReady();
        this.mAppExitInfoTracker.onSystemReady();
        this.mProcListWrapper.getExtImpl().hookOnSystemReady(this.mService);
    }

    void applyDisplaySize(com.android.server.wm.WindowManagerService wm) {
        if (!this.mHaveDisplaySize) {
            android.graphics.Point p = new android.graphics.Point();
            wm.getBaseDisplaySize(0, p);
            if (p.x != 0 && p.y != 0) {
                updateOomLevels(p.x, p.y, true);
                this.mHaveDisplaySize = true;
            }
        }
    }

    java.util.Map<java.lang.Integer, java.lang.String> getProcessesWithPendingBindMounts(int userId) {
        java.util.Map<java.lang.Integer, java.lang.String> pidPackageMap = new java.util.HashMap<>();
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
                    com.android.server.am.ProcessRecord record = this.mLruProcesses.get(i);
                    if (record.userId == userId && record.isBindMountPending()) {
                        int pid = record.getPid();
                        if (pid == 0) {
                            throw new java.lang.IllegalStateException("Pending process is not started yet,retry later");
                        }
                        pidPackageMap.put(java.lang.Integer.valueOf(pid), record.info.packageName);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        return pidPackageMap;
    }

    private void updateOomLevels(int displayWidth, int displayHeight, boolean write) {
        float scaleMem = (this.mTotalMemMb - 350) / 350.0f;
        float scaleDisp = ((displayWidth * displayHeight) - 384000) / (1024000 - 384000);
        float scale = scaleMem > scaleDisp ? scaleMem : scaleDisp;
        if (scale < 0.0f) {
            scale = 0.0f;
        } else if (scale > 1.0f) {
            scale = 1.0f;
        }
        int minfree_adj = android.content.res.Resources.getSystem().getInteger(android.R.integer.config_letterboxDefaultPositionForTabletopModeReachability);
        int minfree_abs = android.content.res.Resources.getSystem().getInteger(android.R.integer.config_letterboxDefaultPositionForHorizontalReachability);
        boolean is64bit = android.os.Build.SUPPORTED_64_BIT_ABIS.length > 0;
        for (int i = 0; i < this.mOomAdj.length; i++) {
            int low = this.mOomMinFreeLow[i];
            int high = this.mOomMinFreeHigh[i];
            if (is64bit) {
                if (i == 4) {
                    high = (high * 3) / 2;
                } else if (i == 5) {
                    high = (high * 7) / 4;
                }
            }
            this.mOomMinFree[i] = (int) (low + ((high - low) * scale));
        }
        if (minfree_abs >= 0) {
            for (int i2 = 0; i2 < this.mOomAdj.length; i2++) {
                this.mOomMinFree[i2] = (int) ((minfree_abs * this.mOomMinFree[i2]) / this.mOomMinFree[this.mOomAdj.length - 1]);
            }
        }
        if (minfree_adj != 0) {
            for (int i3 = 0; i3 < this.mOomAdj.length; i3++) {
                int[] iArr = this.mOomMinFree;
                iArr[i3] = iArr[i3] + ((int) ((minfree_adj * this.mOomMinFree[i3]) / this.mOomMinFree[this.mOomAdj.length - 1]));
                if (this.mOomMinFree[i3] < 0) {
                    this.mOomMinFree[i3] = 0;
                }
            }
        }
        this.mCachedRestoreLevel = (getMemLevel(999) / 1024) / 3;
        int reserve = (((displayWidth * displayHeight) * 4) * 3) / 1024;
        int reserve_adj = android.content.res.Resources.getSystem().getInteger(android.R.integer.config_dreamsBatteryLevelDrainCutoff);
        int reserve_abs = android.content.res.Resources.getSystem().getInteger(android.R.integer.config_dreamOpenAnimationDuration);
        if (reserve_abs >= 0) {
            reserve = reserve_abs;
        }
        if (reserve_adj != 0 && (reserve = reserve + reserve_adj) < 0) {
            reserve = 0;
        }
        if (this.mTotalMemMb <= 8192) {
            this.mOomMinFree[this.mOomAdj.length - 2] = this.mOomMinFree[this.mOomAdj.length - 2] * 2;
            this.mOomMinFree[this.mOomAdj.length - 1] = this.mOomMinFree[this.mOomAdj.length - 1] * 2;
        } else {
            this.mOomMinFree[this.mOomAdj.length - 2] = this.mOomMinFree[this.mOomAdj.length - 2] * 3;
            this.mOomMinFree[this.mOomAdj.length - 1] = this.mOomMinFree[this.mOomAdj.length - 1] * 3;
        }
        this.mProcListWrapper.getExtImpl().customizeMinfreeLevels(this.mOomAdj, this.mOomMinFree, this.mTotalMemMb, (this.mService == null || this.mService.mContext == null) ? null : this.mService.mContext);
        if (write) {
            java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(((this.mOomAdj.length * 2) + 1) * 4);
            buf.putInt(0);
            for (int i4 = 0; i4 < this.mOomAdj.length; i4++) {
                buf.putInt((this.mOomMinFree[i4] * 1024) / PAGE_SIZE);
                buf.putInt(this.mOomAdj[i4]);
            }
            writeLmkd(buf, null);
            int reserve2 = (reserve * 2) + (reserve / 10);
            if (reserve2 > 65536) {
                reserve2 = 65536;
            }
            android.os.SystemProperties.set("sys.sysctl.extra_free_kbytes", java.lang.Integer.toString(this.mProcListWrapper.getExtImpl().customizeExtraFreeKbytes(reserve2, this.mTotalMemMb)));
            this.mOomLevelsSet = true;
        }
    }

    public static int computeEmptyProcessLimit(int totalProcessLimit) {
        return totalProcessLimit / 2;
    }

    private static java.lang.String buildOomTag(java.lang.String prefix, java.lang.String compactPrefix, java.lang.String space, int val, int base, boolean compact) {
        int diff = val - base;
        if (diff == 0) {
            if (compact) {
                return compactPrefix;
            }
            return space == null ? prefix : prefix + space;
        }
        if (diff < 10) {
            return prefix + (compact ? "+" : "+ ") + java.lang.Integer.toString(diff);
        }
        return prefix + "+" + java.lang.Integer.toString(diff);
    }

    public static java.lang.String makeOomAdjString(int setAdj, boolean compact) {
        if (setAdj >= 900) {
            return buildOomTag("cch", "cch", "   ", setAdj, 900, compact);
        }
        if (setAdj >= 800) {
            return buildOomTag("svcb  ", "svcb", null, setAdj, 800, compact);
        }
        if (setAdj >= 700) {
            return buildOomTag("prev  ", "prev", null, setAdj, PREVIOUS_APP_ADJ, compact);
        }
        if (setAdj >= 600) {
            return buildOomTag("home  ", "home", null, setAdj, 600, compact);
        }
        if (setAdj >= 500) {
            return buildOomTag("svc   ", "svc", null, setAdj, 500, compact);
        }
        if (setAdj >= 400) {
            return buildOomTag("hvy   ", "hvy", null, setAdj, 400, compact);
        }
        if (setAdj >= 300) {
            return buildOomTag("bkup  ", "bkup", null, setAdj, 300, compact);
        }
        if (setAdj >= 250) {
            return buildOomTag("prcl  ", "prcl", null, setAdj, 250, compact);
        }
        if (setAdj >= 225) {
            return buildOomTag("prcm  ", "prcm", null, setAdj, PERCEPTIBLE_MEDIUM_APP_ADJ, compact);
        }
        if (setAdj >= 200) {
            return buildOomTag("prcp  ", "prcp", null, setAdj, 200, compact);
        }
        if (setAdj >= 100) {
            return buildOomTag("vis", "vis", "   ", setAdj, 100, compact);
        }
        if (setAdj >= 0) {
            return buildOomTag("fg ", "fg ", "   ", setAdj, 0, compact);
        }
        if (setAdj >= -700) {
            return buildOomTag("psvc  ", "psvc", null, setAdj, PERSISTENT_SERVICE_ADJ, compact);
        }
        if (setAdj >= -800) {
            return buildOomTag("pers  ", "pers", null, setAdj, PERSISTENT_PROC_ADJ, compact);
        }
        if (setAdj >= -900) {
            return buildOomTag("sys   ", "sys", null, setAdj, SYSTEM_ADJ, compact);
        }
        if (setAdj >= -1000) {
            return buildOomTag("ntv  ", "ntv", null, setAdj, -1000, compact);
        }
        return java.lang.Integer.toString(setAdj);
    }

    public static java.lang.String makeProcStateString(int curProcState) {
        return android.app.ActivityManager.procStateToString(curProcState);
    }

    public static int makeProcStateProtoEnum(int curProcState) {
        switch (curProcState) {
            case -1:
                return 999;
            case 0:
                return 1000;
            case 1:
                return 1001;
            case 2:
                return 1002;
            case 3:
                return 1020;
            case 4:
                return 1003;
            case 5:
                return 1004;
            case 6:
                return 1005;
            case 7:
                return 1006;
            case 8:
                return 1007;
            case 9:
                return 1008;
            case 10:
                return 1009;
            case 11:
                return 1010;
            case 12:
                return 1011;
            case 13:
                return 1012;
            case 14:
                return 1013;
            case 15:
                return 1014;
            case 16:
                return 1015;
            case 17:
                return 1016;
            case 18:
                return 1017;
            case 19:
                return 1018;
            case 20:
                return 1019;
            default:
                return 998;
        }
    }

    public static void appendRamKb(java.lang.StringBuilder sb, long ramKb) {
        int j = 0;
        int fact = 10;
        while (j < 6) {
            if (ramKb < fact) {
                sb.append(' ');
            }
            j++;
            fact *= 10;
        }
        sb.append(ramKb);
    }

    public static final class ProcStateMemTracker {
        int mPendingHighestMemState;
        int mPendingMemState;
        float mPendingScalingFactor;
        final int[] mHighestMem = new int[5];
        final float[] mScalingFactor = new float[5];
        int mTotalHighestMem = 4;

        public ProcStateMemTracker() {
            for (int i = 0; i < 5; i++) {
                this.mHighestMem[i] = 5;
                this.mScalingFactor[i] = 1.0f;
            }
            this.mPendingMemState = -1;
        }

        public void dumpLine(java.io.PrintWriter pw) {
            pw.print("best=");
            pw.print(this.mTotalHighestMem);
            pw.print(" (");
            boolean needSep = false;
            for (int i = 0; i < 5; i++) {
                if (this.mHighestMem[i] < 5) {
                    if (needSep) {
                        pw.print(", ");
                    }
                    pw.print(i);
                    pw.print("=");
                    pw.print(this.mHighestMem[i]);
                    pw.print(" ");
                    pw.print(this.mScalingFactor[i]);
                    pw.print("x");
                    needSep = true;
                }
            }
            pw.print(")");
            if (this.mPendingMemState >= 0) {
                pw.print(" / pending state=");
                pw.print(this.mPendingMemState);
                pw.print(" highest=");
                pw.print(this.mPendingHighestMemState);
                pw.print(" ");
                pw.print(this.mPendingScalingFactor);
                pw.print("x");
            }
            pw.println();
        }
    }

    public static boolean procStatesDifferForMem(int procState1, int procState2) {
        return sProcStateToProcMem[procState1] != sProcStateToProcMem[procState2];
    }

    public static long minTimeFromStateChange(boolean test) {
        return test ? 10000L : 15000L;
    }

    public static long computeNextPssTime(int procState, com.android.server.am.ProcessList.ProcStateMemTracker tracker, boolean test, boolean sleeping, long now, long earliest) {
        boolean first;
        float scalingFactor;
        long[] table;
        int highestMemState;
        int memState = sProcStateToProcMem[procState];
        if (tracker != null) {
            if (memState >= tracker.mTotalHighestMem) {
                highestMemState = tracker.mTotalHighestMem;
            } else {
                highestMemState = memState;
            }
            first = highestMemState < tracker.mHighestMem[memState];
            tracker.mPendingMemState = memState;
            tracker.mPendingHighestMemState = highestMemState;
            if (first) {
                scalingFactor = 1.0f;
                tracker.mPendingScalingFactor = 1.0f;
            } else {
                scalingFactor = tracker.mScalingFactor[memState];
                tracker.mPendingScalingFactor = 1.5f * scalingFactor;
            }
        } else {
            first = true;
            scalingFactor = 1.0f;
        }
        if (test) {
            if (first) {
                table = sTestFirstPssTimes;
            } else {
                table = sTestSamePssTimes;
            }
        } else if (first) {
            table = sleeping ? sFirstAsleepPssTimes : sFirstAwakePssTimes;
        } else {
            table = sleeping ? sSameAsleepPssTimes : sSameAwakePssTimes;
        }
        long delay = (long) (table[memState] * scalingFactor);
        if (delay > 3600000) {
            delay = 3600000;
        }
        return java.lang.Math.max(now + delay, earliest);
    }

    long getMemLevel(int adjustment) {
        for (int i = 0; i < this.mOomAdj.length; i++) {
            if (adjustment <= this.mOomAdj[i]) {
                return this.mOomMinFree[i] * 1024;
            }
        }
        return this.mOomMinFree[this.mOomAdj.length - 1] * 1024;
    }

    long getCachedRestoreThresholdKb() {
        return this.mCachedRestoreLevel;
    }

    com.android.server.am.AppStartInfoTracker getAppStartInfoTracker() {
        return this.mAppStartInfoTracker;
    }

    public static void setOomAdj(int pid, int uid, int amt) {
        if (pid <= 0 || amt == 1001) {
            return;
        }
        long start = android.os.SystemClock.elapsedRealtime();
        java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(16);
        buf.putInt(1);
        buf.putInt(pid);
        buf.putInt(uid);
        buf.putInt(amt);
        writeLmkd(buf, null);
        long now = android.os.SystemClock.elapsedRealtime();
        if (now - start > 250) {
            android.util.Slog.w("ActivityManager", "SLOW OOM ADJ: " + (now - start) + "ms for pid " + pid + " = " + amt);
        }
    }

    public static void batchSetOomAdj(java.util.ArrayList<com.android.server.am.ProcessRecord> apps) {
        int totalApps = apps.size();
        if (totalApps == 0) {
            return;
        }
        java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(52);
        int total_procs_in_buf = 0;
        buf.putInt(11);
        for (int i = 0; i < totalApps; i++) {
            int pid = apps.get(i).getPid();
            int amt = apps.get(i).mState.getCurAdj();
            int uid = apps.get(i).uid;
            if (pid > 0 && amt != 1001) {
                if (total_procs_in_buf >= 3) {
                    writeLmkd(buf, null);
                    buf.clear();
                    total_procs_in_buf = 0;
                    java.nio.ByteBuffer.allocate(52);
                    buf.putInt(11);
                }
                buf.putInt(pid);
                buf.putInt(uid);
                buf.putInt(amt);
                buf.putInt(0);
                total_procs_in_buf++;
            }
        }
        writeLmkd(buf, null);
    }

    public static final void remove(int pid) {
        if (pid <= 0) {
            return;
        }
        java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(8);
        buf.putInt(2);
        buf.putInt(pid);
        writeLmkd(buf, null);
    }

    public static final java.lang.Integer getLmkdKillCount(int min_oom_adj, int max_oom_adj) {
        java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(12);
        java.nio.ByteBuffer repl = java.nio.ByteBuffer.allocate(8);
        buf.putInt(4);
        buf.putInt(min_oom_adj);
        buf.putInt(max_oom_adj);
        repl.putInt(4);
        repl.rewind();
        if (writeLmkd(buf, repl) && repl.getInt() == 4) {
            return new java.lang.Integer(repl.getInt());
        }
        return null;
    }

    public boolean onLmkdConnect(java.io.OutputStream ostream) {
        try {
            java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(4);
            buf.putInt(3);
            ostream.write(buf.array(), 0, buf.position());
            if (this.mOomLevelsSet) {
                java.nio.ByteBuffer buf2 = java.nio.ByteBuffer.allocate(((this.mOomAdj.length * 2) + 1) * 4);
                buf2.putInt(0);
                for (int i = 0; i < this.mOomAdj.length; i++) {
                    buf2.putInt((this.mOomMinFree[i] * 1024) / PAGE_SIZE);
                    buf2.putInt(this.mOomAdj[i]);
                }
                ostream.write(buf2.array(), 0, buf2.position());
            }
            java.nio.ByteBuffer buf3 = java.nio.ByteBuffer.allocate(8);
            buf3.putInt(5);
            buf3.putInt(0);
            ostream.write(buf3.array(), 0, buf3.position());
            java.nio.ByteBuffer buf4 = java.nio.ByteBuffer.allocate(8);
            buf4.putInt(5);
            buf4.putInt(1);
            ostream.write(buf4.array(), 0, buf4.position());
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    public static void startPsiMonitoringAfterBoot() {
        java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(4);
        buf.putInt(9);
        writeLmkd(buf, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean writeLmkd(java.nio.ByteBuffer buf, java.nio.ByteBuffer repl) {
        if (!sLmkdConnection.isConnected()) {
            sKillHandler.sendMessage(sKillHandler.obtainMessage(4001));
            if (!sLmkdConnection.waitForConnection(3000L)) {
                return false;
            }
        }
        return sLmkdConnection.exchange(buf, repl);
    }

    static void killProcessGroup(int uid, int pid) {
        if (pid <= 0) {
            android.util.Slog.e("ActivityManager", "pid " + pid + " to killProcessGroup <= 0 !");
        } else if (sKillHandler == null) {
            android.util.Slog.w("ActivityManager", "Asked to kill process group before system bringup!");
            android.os.Process.killProcessGroup(uid, pid);
        } else {
            sKillHandler.sendMessage(sKillHandler.obtainMessage(4000, uid, pid));
        }
    }

    com.android.server.am.ProcessRecord getProcessRecordLocked(java.lang.String processName, int uid) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.v("ActivityManager", "processName: " + processName + " uid " + uid);
        }
        if (uid == 1000) {
            android.util.SparseArray<com.android.server.am.ProcessRecord> procs = (android.util.SparseArray) this.mProcessNames.getMap().get(processName);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.v("ActivityManager", "procs: " + procs);
            }
            if (procs == null) {
                return null;
            }
            int procCount = procs.size();
            for (int i = 0; i < procCount; i++) {
                int procUid = procs.keyAt(i);
                if (!android.os.UserHandle.isCore(procUid) || !android.os.UserHandle.isSameUser(procUid, uid)) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                        android.util.Slog.v("ActivityManager", "procUid: " + procUid);
                    }
                } else {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                        android.util.Slog.v("ActivityManager", "i: " + i + " proc " + procs.valueAt(i));
                    }
                    return procs.valueAt(i);
                }
            }
        }
        return (com.android.server.am.ProcessRecord) this.mProcessNames.get(processName, uid);
    }

    void getMemoryInfo(android.app.ActivityManager.MemoryInfo outInfo) {
        long homeAppMem = getMemLevel(600);
        long cachedAppMem = getMemLevel(900);
        outInfo.advertisedMem = android.os.Process.getAdvertisedMem();
        outInfo.availMem = android.os.Process.getFreeMemory();
        outInfo.totalMem = android.os.Process.getTotalMemory();
        outInfo.threshold = homeAppMem;
        outInfo.lowMemory = outInfo.availMem < ((cachedAppMem - homeAppMem) / 2) + homeAppMem;
        outInfo.hiddenAppThreshold = cachedAppMem;
        outInfo.secondaryServerThreshold = getMemLevel(500);
        outInfo.visibleAppThreshold = getMemLevel(100);
        outInfo.foregroundAppThreshold = getMemLevel(0);
    }

    com.android.server.am.ProcessRecord findAppProcessLOSP(android.os.IBinder app, java.lang.String reason) {
        int NP = this.mProcessNames.getMap().size();
        for (int ip = 0; ip < NP; ip++) {
            android.util.SparseArray<com.android.server.am.ProcessRecord> apps = (android.util.SparseArray) this.mProcessNames.getMap().valueAt(ip);
            int NA = apps.size();
            for (int ia = 0; ia < NA; ia++) {
                com.android.server.am.ProcessRecord p = apps.valueAt(ia);
                android.app.IApplicationThread thread = p.getThread();
                if (thread != null && thread.asBinder() == app) {
                    return p;
                }
            }
        }
        android.util.Slog.w("ActivityManager", "Can't find mystery application for " + reason + " from pid=" + android.os.Binder.getCallingPid() + " uid=" + android.os.Binder.getCallingUid() + ": " + app);
        return null;
    }

    private void checkSlow(long startTime, java.lang.String where) {
        long now = android.os.SystemClock.uptimeMillis();
        if (now - startTime > 50) {
            android.util.Slog.w("ActivityManager", "Slow operation: " + (now - startTime) + "ms so far, now at " + where);
        }
    }

    private int[] computeGidsForProcess(int mountExternal, int uid, int[] permGids, boolean externalStorageAccess) {
        java.util.ArrayList<java.lang.Integer> gidList = new java.util.ArrayList<>(permGids.length + 5);
        int sharedAppGid = android.os.UserHandle.getSharedAppGid(android.os.UserHandle.getAppId(uid));
        int cacheAppGid = android.os.UserHandle.getCacheAppGid(android.os.UserHandle.getAppId(uid));
        int userGid = android.os.UserHandle.getUserGid(android.os.UserHandle.getUserId(uid));
        for (int permGid : permGids) {
            gidList.add(java.lang.Integer.valueOf(permGid));
        }
        if (sharedAppGid != -1) {
            gidList.add(java.lang.Integer.valueOf(sharedAppGid));
        }
        if (cacheAppGid != -1) {
            gidList.add(java.lang.Integer.valueOf(cacheAppGid));
        }
        if (userGid != -1) {
            gidList.add(java.lang.Integer.valueOf(userGid));
        }
        if (mountExternal == 4 || mountExternal == 3) {
            gidList.add(java.lang.Integer.valueOf(android.os.UserHandle.getUid(android.os.UserHandle.getUserId(uid), 1015)));
            gidList.add(1078);
            gidList.add(1079);
            this.mProcListWrapper.getExtImpl().addGidsForMultiApp(uid, gidList);
        }
        if (mountExternal == 2) {
            gidList.add(1079);
        }
        if (mountExternal == 3) {
            gidList.add(1023);
        }
        if (externalStorageAccess) {
            gidList.add(1077);
        }
        this.mProcListWrapper.getExtImpl().addComputeGids(mountExternal, uid, gidList);
        int[] gidArray = new int[gidList.size()];
        for (int i = 0; i < gidArray.length; i++) {
            gidArray[i] = gidList.get(i).intValue();
        }
        return gidArray;
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x02af  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x02d2 A[Catch: RuntimeException -> 0x04c3, TryCatch #8 {RuntimeException -> 0x04c3, blocks: (B:63:0x01cc, B:67:0x01e3, B:71:0x01ef, B:87:0x0263, B:97:0x028b, B:99:0x0297, B:100:0x0299, B:106:0x02b1, B:111:0x02c6, B:113:0x02d2, B:114:0x02d4, B:116:0x02e0, B:117:0x02e2, B:119:0x02ee, B:120:0x02f1, B:125:0x030e, B:143:0x036f, B:169:0x03e6), top: B:229:0x01cc }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02e0 A[Catch: RuntimeException -> 0x04c3, TryCatch #8 {RuntimeException -> 0x04c3, blocks: (B:63:0x01cc, B:67:0x01e3, B:71:0x01ef, B:87:0x0263, B:97:0x028b, B:99:0x0297, B:100:0x0299, B:106:0x02b1, B:111:0x02c6, B:113:0x02d2, B:114:0x02d4, B:116:0x02e0, B:117:0x02e2, B:119:0x02ee, B:120:0x02f1, B:125:0x030e, B:143:0x036f, B:169:0x03e6), top: B:229:0x01cc }] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02ee A[Catch: RuntimeException -> 0x04c3, TryCatch #8 {RuntimeException -> 0x04c3, blocks: (B:63:0x01cc, B:67:0x01e3, B:71:0x01ef, B:87:0x0263, B:97:0x028b, B:99:0x0297, B:100:0x0299, B:106:0x02b1, B:111:0x02c6, B:113:0x02d2, B:114:0x02d4, B:116:0x02e0, B:117:0x02e2, B:119:0x02ee, B:120:0x02f1, B:125:0x030e, B:143:0x036f, B:169:0x03e6), top: B:229:0x01cc }] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0320 A[Catch: RuntimeException -> 0x01af, TryCatch #5 {RuntimeException -> 0x01af, blocks: (B:25:0x00df, B:27:0x00f6, B:29:0x010c, B:31:0x011d, B:35:0x0133, B:37:0x0154, B:39:0x0158, B:41:0x015e, B:43:0x016a, B:45:0x0180, B:47:0x0184, B:50:0x019c, B:51:0x019f, B:73:0x01ff, B:75:0x0205, B:79:0x0210, B:84:0x022f, B:86:0x0249, B:90:0x0278, B:103:0x02a9, B:108:0x02be, B:122:0x02f7, B:124:0x0303, B:127:0x0316, B:129:0x031a, B:133:0x0324, B:135:0x032e, B:137:0x0349, B:139:0x034c, B:145:0x037f, B:147:0x0387, B:150:0x038d, B:161:0x03d1, B:164:0x03da, B:165:0x03de, B:140:0x0350, B:141:0x036a, B:131:0x0320, B:33:0x0129, B:60:0x01bd, B:61:0x01c1), top: B:224:0x00df }] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x036b  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x038d A[Catch: RuntimeException -> 0x01af, TRY_LEAVE, TryCatch #5 {RuntimeException -> 0x01af, blocks: (B:25:0x00df, B:27:0x00f6, B:29:0x010c, B:31:0x011d, B:35:0x0133, B:37:0x0154, B:39:0x0158, B:41:0x015e, B:43:0x016a, B:45:0x0180, B:47:0x0184, B:50:0x019c, B:51:0x019f, B:73:0x01ff, B:75:0x0205, B:79:0x0210, B:84:0x022f, B:86:0x0249, B:90:0x0278, B:103:0x02a9, B:108:0x02be, B:122:0x02f7, B:124:0x0303, B:127:0x0316, B:129:0x031a, B:133:0x0324, B:135:0x032e, B:137:0x0349, B:139:0x034c, B:145:0x037f, B:147:0x0387, B:150:0x038d, B:161:0x03d1, B:164:0x03da, B:165:0x03de, B:140:0x0350, B:141:0x036a, B:131:0x0320, B:33:0x0129, B:60:0x01bd, B:61:0x01c1), top: B:224:0x00df }] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x03df  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x03e3  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x03e6 A[Catch: RuntimeException -> 0x04c3, TRY_ENTER, TRY_LEAVE, TryCatch #8 {RuntimeException -> 0x04c3, blocks: (B:63:0x01cc, B:67:0x01e3, B:71:0x01ef, B:87:0x0263, B:97:0x028b, B:99:0x0297, B:100:0x0299, B:106:0x02b1, B:111:0x02c6, B:113:0x02d2, B:114:0x02d4, B:116:0x02e0, B:117:0x02e2, B:119:0x02ee, B:120:0x02f1, B:125:0x030e, B:143:0x036f, B:169:0x03e6), top: B:229:0x01cc }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0401  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x040a A[Catch: RuntimeException -> 0x03f4, TRY_ENTER, TRY_LEAVE, TryCatch #1 {RuntimeException -> 0x03f4, blocks: (B:173:0x03f0, B:184:0x040a, B:188:0x041e, B:194:0x044b), top: B:216:0x03f0 }] */
    /* JADX WARN: Removed duplicated region for block: B:188:0x041e A[Catch: RuntimeException -> 0x03f4, TRY_ENTER, TRY_LEAVE, TryCatch #1 {RuntimeException -> 0x03f4, blocks: (B:173:0x03f0, B:184:0x040a, B:188:0x041e, B:194:0x044b), top: B:216:0x03f0 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0431  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x044b A[Catch: RuntimeException -> 0x03f4, TRY_ENTER, TRY_LEAVE, TryCatch #1 {RuntimeException -> 0x03f4, blocks: (B:173:0x03f0, B:184:0x040a, B:188:0x041e, B:194:0x044b), top: B:216:0x03f0 }] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0481  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x03ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x022f A[Catch: RuntimeException -> 0x01af, TryCatch #5 {RuntimeException -> 0x01af, blocks: (B:25:0x00df, B:27:0x00f6, B:29:0x010c, B:31:0x011d, B:35:0x0133, B:37:0x0154, B:39:0x0158, B:41:0x015e, B:43:0x016a, B:45:0x0180, B:47:0x0184, B:50:0x019c, B:51:0x019f, B:73:0x01ff, B:75:0x0205, B:79:0x0210, B:84:0x022f, B:86:0x0249, B:90:0x0278, B:103:0x02a9, B:108:0x02be, B:122:0x02f7, B:124:0x0303, B:127:0x0316, B:129:0x031a, B:133:0x0324, B:135:0x032e, B:137:0x0349, B:139:0x034c, B:145:0x037f, B:147:0x0387, B:150:0x038d, B:161:0x03d1, B:164:0x03da, B:165:0x03de, B:140:0x0350, B:141:0x036a, B:131:0x0320, B:33:0x0129, B:60:0x01bd, B:61:0x01c1), top: B:224:0x00df }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x027e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0282  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0288  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0297 A[Catch: RuntimeException -> 0x04c3, TryCatch #8 {RuntimeException -> 0x04c3, blocks: (B:63:0x01cc, B:67:0x01e3, B:71:0x01ef, B:87:0x0263, B:97:0x028b, B:99:0x0297, B:100:0x0299, B:106:0x02b1, B:111:0x02c6, B:113:0x02d2, B:114:0x02d4, B:116:0x02e0, B:117:0x02e2, B:119:0x02ee, B:120:0x02f1, B:125:0x030e, B:143:0x036f, B:169:0x03e6), top: B:229:0x01cc }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean startProcessLocked(com.android.server.am.ProcessRecord r41, com.android.server.am.HostingRecord r42, int r43, boolean r44, boolean r45, java.lang.String r46) {
        /*
            Method dump skipped, instruction units count: 1307
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ProcessList.startProcessLocked(com.android.server.am.ProcessRecord, com.android.server.am.HostingRecord, int, boolean, boolean, java.lang.String):boolean");
    }

    java.lang.String updateSeInfo(com.android.server.am.ProcessRecord app) {
        java.lang.String extraInfo = "";
        if (app.isSdkSandbox) {
            if (getProcessListSettingsListener().applySdkSandboxRestrictionsNext()) {
                extraInfo = APPLY_SDK_SANDBOX_NEXT_RESTRICTIONS;
            } else if (com.android.sdksandbox.flags.Flags.selinuxSdkSandboxAudit() && getProcessListSettingsListener().applySdkSandboxRestrictionsAudit()) {
                extraInfo = APPLY_SDK_SANDBOX_AUDIT_RESTRICTIONS;
            }
        }
        if (com.android.sdksandbox.flags.Flags.selinuxInputSelector()) {
            return app.info.seInfo + extraInfo + android.text.TextUtils.emptyIfNull(app.info.seInfoUser);
        }
        return app.info.seInfo + (android.text.TextUtils.isEmpty(app.info.seInfoUser) ? "" : app.info.seInfoUser) + extraInfo;
    }

    boolean startProcessLocked(com.android.server.am.HostingRecord hostingRecord, final java.lang.String entryPoint, final com.android.server.am.ProcessRecord app, int uid, final int[] gids, final int runtimeFlags, final int zygotePolicyFlags, final int mountExternal, java.lang.String seInfo, final java.lang.String requiredAbi, final java.lang.String instructionSet, final java.lang.String invokeWith, long startUptime, long startElapsedTime) throws java.lang.Throwable {
        if (this.mProcListWrapper.getExtImpl().interceptStartProcessBeforeHandle(this.mService, app, hostingRecord)) {
            return false;
        }
        app.setPendingStart(true);
        app.setRemoved(false);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                app.setKilledByAm(false);
                app.setKilled(false);
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        if (app.getStartSeq() != 0) {
            android.util.Slog.wtf("ActivityManager", "startProcessLocked processName:" + app.processName + " with non-zero startSeq:" + app.getStartSeq());
        }
        if (app.getPid() != 0) {
            android.util.Slog.wtf("ActivityManager", "startProcessLocked processName:" + app.processName + " with non-zero pid:" + app.getPid());
        }
        app.setDisabledCompatChanges(null);
        app.setLoggableCompatChanges(null);
        if (this.mPlatformCompat != null) {
            app.setDisabledCompatChanges(this.mPlatformCompat.getDisabledChanges(app.info));
            app.setLoggableCompatChanges(this.mPlatformCompat.getLoggableChanges(app.info));
        }
        final long startSeq = this.mProcStartSeqCounter + 1;
        this.mProcStartSeqCounter = startSeq;
        app.setStartSeq(startSeq);
        app.setStartParams(uid, hostingRecord, seInfo, startUptime, startElapsedTime);
        app.setUsingWrapper((invokeWith == null && com.android.internal.os.Zygote.getWrapProperty(app.processName) == null) ? false : true);
        this.mPendingStarts.put(startSeq, app);
        if (this.mService.mConstants.FLAG_PROCESS_START_ASYNC) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "Posting procStart msg for " + app.toShortString());
            }
            this.mService.mProcStartHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startProcessLocked$0(app, entryPoint, gids, runtimeFlags, zygotePolicyFlags, mountExternal, requiredAbi, instructionSet, invokeWith, startSeq);
                }
            });
            this.mProcListWrapper.getExtImpl().hookStartProcessAfterHandleProcessStartAsync(this.mActiveUids, app);
            return true;
        }
        try {
            android.os.Process.ProcessStartResult startResult = startProcess(hostingRecord, entryPoint, app, uid, gids, runtimeFlags, zygotePolicyFlags, mountExternal, seInfo, requiredAbi, instructionSet, invokeWith, startUptime);
            handleProcessStartedLocked(app, startResult.pid, startResult.usingWrapper, startSeq, false);
            this.mProcListWrapper.getExtImpl().hookStartProcessAfterHandleProcessStart(this.mActiveUids, app);
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.e("ActivityManager", "Failure starting process " + app.processName, e);
            app.setPendingStart(false);
            this.mService.forceStopPackageLocked(app.info.packageName, android.os.UserHandle.getAppId(app.uid), false, false, true, false, false, false, app.userId, "start failure");
        }
        return app.getPid() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleProcessStart, reason: merged with bridge method [inline-methods] */
    public void lambda$startProcessLocked$0(final com.android.server.am.ProcessRecord app, final java.lang.String entryPoint, final int[] gids, final int runtimeFlags, final int zygotePolicyFlags, final int mountExternal, final java.lang.String requiredAbi, final java.lang.String instructionSet, final java.lang.String invokeWith, final long startSeq) {
        java.lang.Runnable startRunnable = new java.lang.Runnable() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$handleProcessStart$1(app, entryPoint, gids, runtimeFlags, zygotePolicyFlags, mountExternal, requiredAbi, instructionSet, invokeWith, startSeq);
            }
        };
        com.android.server.am.ProcessRecord predecessor = app.mPredecessor;
        if (predecessor == null || predecessor.getDyingPid() <= 0) {
            startRunnable.run();
        } else {
            handleProcessStartWithPredecessor(predecessor, startRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$handleProcessStart$1(com.android.server.am.ProcessRecord r19, java.lang.String r20, int[] r21, int r22, int r23, int r24, java.lang.String r25, java.lang.String r26, java.lang.String r27, long r28) throws java.lang.Throwable {
        /*
            r18 = this;
            r14 = r18
            r15 = r19
            r12 = r28
            com.android.server.am.HostingRecord r2 = r19.getHostingRecord()     // Catch: java.lang.RuntimeException -> L57
            int r5 = r19.getStartUid()     // Catch: java.lang.RuntimeException -> L57
            java.lang.String r10 = r19.getSeInfo()     // Catch: java.lang.RuntimeException -> L57
            long r16 = r19.getStartTime()     // Catch: java.lang.RuntimeException -> L57
            r1 = r18
            r3 = r20
            r4 = r19
            r6 = r21
            r7 = r22
            r8 = r23
            r9 = r24
            r11 = r25
            r12 = r26
            r13 = r27
            r14 = r16
            android.os.Process$ProcessStartResult r0 = r1.startProcess(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)     // Catch: java.lang.RuntimeException -> L4f
            r1 = r0
            r2 = r18
            com.android.server.am.ActivityManagerService r3 = r2.mService     // Catch: java.lang.RuntimeException -> L4d
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection()     // Catch: java.lang.RuntimeException -> L4d
            monitor-enter(r3)     // Catch: java.lang.RuntimeException -> L4d
            r4 = r19
            r5 = r28
            r2.handleProcessStartedLocked(r4, r1, r5)     // Catch: java.lang.Throwable -> L47
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L47
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()     // Catch: java.lang.RuntimeException -> L45
            goto La8
        L45:
            r0 = move-exception
            goto L5b
        L47:
            r0 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L47
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()     // Catch: java.lang.RuntimeException -> L45
            throw r0     // Catch: java.lang.RuntimeException -> L45
        L4d:
            r0 = move-exception
            goto L52
        L4f:
            r0 = move-exception
            r2 = r18
        L52:
            r4 = r19
            r5 = r28
            goto L5b
        L57:
            r0 = move-exception
            r5 = r12
            r2 = r14
            r4 = r15
        L5b:
            r1 = r0
            com.android.server.am.ActivityManagerService r3 = r2.mService
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection()
            monitor-enter(r3)
            java.lang.String r0 = "ActivityManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La9
            r7.<init>()     // Catch: java.lang.Throwable -> La9
            java.lang.String r8 = "Failure starting process "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch: java.lang.Throwable -> La9
            java.lang.String r8 = r4.processName     // Catch: java.lang.Throwable -> La9
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch: java.lang.Throwable -> La9
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> La9
            android.util.Slog.e(r0, r7, r1)     // Catch: java.lang.Throwable -> La9
            android.util.LongSparseArray<com.android.server.am.ProcessRecord> r0 = r2.mPendingStarts     // Catch: java.lang.Throwable -> La9
            r0.remove(r5)     // Catch: java.lang.Throwable -> La9
            r0 = 0
            r4.setPendingStart(r0)     // Catch: java.lang.Throwable -> La9
            com.android.server.am.ActivityManagerService r7 = r2.mService     // Catch: java.lang.Throwable -> La9
            android.content.pm.ApplicationInfo r0 = r4.info     // Catch: java.lang.Throwable -> La9
            java.lang.String r8 = r0.packageName     // Catch: java.lang.Throwable -> La9
            int r0 = r4.uid     // Catch: java.lang.Throwable -> La9
            int r9 = android.os.UserHandle.getAppId(r0)     // Catch: java.lang.Throwable -> La9
            int r0 = r4.userId     // Catch: java.lang.Throwable -> La9
            java.lang.String r17 = "start failure"
            r10 = 0
            r11 = 0
            r12 = 1
            r13 = 0
            r14 = 0
            r15 = 0
            r16 = r0
            r7.forceStopPackageLocked(r8, r9, r10, r11, r12, r13, r14, r15, r16, r17)     // Catch: java.lang.Throwable -> La9
            r19.doEarlyCleanupIfNecessaryLocked()     // Catch: java.lang.Throwable -> La9
            monitor-exit(r3)     // Catch: java.lang.Throwable -> La9
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()
        La8:
            return
        La9:
            r0 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> La9
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ProcessList.lambda$handleProcessStart$1(com.android.server.am.ProcessRecord, java.lang.String, int[], int, int, int, java.lang.String, java.lang.String, java.lang.String, long):void");
    }

    private void handleProcessStartWithPredecessor(com.android.server.am.ProcessRecord predecessor, java.lang.Runnable successorStartRunnable) {
        if (predecessor.mSuccessorStartRunnable != null) {
            android.util.Slog.wtf("ActivityManager", "We've been watching for the death of " + predecessor);
        } else {
            predecessor.mSuccessorStartRunnable = successorStartRunnable;
            this.mService.mProcStartHandler.sendMessageDelayed(this.mService.mProcStartHandler.obtainMessage(2, predecessor), this.mService.mConstants.mProcessKillTimeoutMs);
        }
    }

    static final class ProcStartHandler extends android.os.Handler {
        static final int MSG_PROCESS_DIED = 1;
        static final int MSG_PROCESS_KILL_TIMEOUT = 2;
        private final com.android.server.am.ActivityManagerService mService;

        ProcStartHandler(com.android.server.am.ActivityManagerService service, android.os.Looper looper) {
            super(looper);
            this.mService = service;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    this.mService.mProcessList.handlePredecessorProcDied((com.android.server.am.ProcessRecord) msg.obj);
                    return;
                case 2:
                    com.android.server.am.ActivityManagerService activityManagerService = this.mService;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            this.mService.handleProcessStartOrKillTimeoutLocked((com.android.server.am.ProcessRecord) msg.obj, true);
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePredecessorProcDied(com.android.server.am.ProcessRecord app) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.i("ActivityManager", app.toString() + " is really gone now");
        }
        java.lang.Runnable start = app.mSuccessorStartRunnable;
        if (start != null) {
            app.mSuccessorStartRunnable = null;
            start.run();
        }
    }

    public void killAppZygoteIfNeededLocked(android.os.AppZygote appZygote, boolean force) {
        android.content.pm.ApplicationInfo appInfo = appZygote.getAppInfo();
        java.util.ArrayList<com.android.server.am.ProcessRecord> zygoteProcesses = this.mAppZygoteProcesses.get(appZygote);
        if (zygoteProcesses != null) {
            if (force || zygoteProcesses.size() == 0) {
                this.mAppZygotes.remove(appInfo.processName, appInfo.uid);
                this.mAppZygoteProcesses.remove(appZygote);
                this.mAppIsolatedUidRangeAllocator.freeUidRangeLocked(appInfo);
                appZygote.stopZygote();
            }
        }
    }

    private void removeProcessFromAppZygoteLocked(com.android.server.am.ProcessRecord app) {
        com.android.server.am.ProcessList.IsolatedUidRange appUidRange = this.mAppIsolatedUidRangeAllocator.getIsolatedUidRangeLocked(app.info.processName, app.getHostingRecord().getDefiningUid());
        if (appUidRange != null) {
            appUidRange.freeIsolatedUidLocked(app.uid);
        }
        android.os.AppZygote appZygote = (android.os.AppZygote) this.mAppZygotes.get(app.info.processName, app.getHostingRecord().getDefiningUid());
        if (appZygote != null) {
            java.util.ArrayList<com.android.server.am.ProcessRecord> zygoteProcesses = this.mAppZygoteProcesses.get(appZygote);
            zygoteProcesses.remove(app);
            if (zygoteProcesses.size() == 0) {
                this.mService.mHandler.removeMessages(71);
                if (app.isRemoved()) {
                    killAppZygoteIfNeededLocked(appZygote, false);
                    return;
                }
                android.os.Message msg = this.mService.mHandler.obtainMessage(71);
                msg.obj = appZygote;
                this.mService.mHandler.sendMessageDelayed(msg, 5000L);
            }
        }
    }

    private android.os.AppZygote createAppZygoteForProcessIfNeeded(com.android.server.am.ProcessRecord app) {
        android.os.AppZygote appZygote;
        java.util.ArrayList<com.android.server.am.ProcessRecord> zygoteProcessList;
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                int uid = app.getHostingRecord().getDefiningUid();
                appZygote = (android.os.AppZygote) this.mAppZygotes.get(app.info.processName, uid);
                if (appZygote == null) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                        android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "Creating new app zygote.");
                    }
                    com.android.server.am.ProcessList.IsolatedUidRange uidRange = this.mAppIsolatedUidRangeAllocator.getIsolatedUidRangeLocked(app.info.processName, app.getHostingRecord().getDefiningUid());
                    int userId = android.os.UserHandle.getUserId(uid);
                    int firstUid = android.os.UserHandle.getUid(userId, uidRange.mFirstUid);
                    int lastUid = android.os.UserHandle.getUid(userId, uidRange.mLastUid);
                    android.content.pm.ApplicationInfo appInfo = new android.content.pm.ApplicationInfo(app.info);
                    appInfo.packageName = app.getHostingRecord().getDefiningPackageName();
                    appInfo.uid = uid;
                    appZygote = new android.os.AppZygote(appInfo, app.processInfo, uid, firstUid, lastUid);
                    this.mAppZygotes.put(app.info.processName, uid, appZygote);
                    zygoteProcessList = new java.util.ArrayList<>();
                    this.mAppZygoteProcesses.put(appZygote, zygoteProcessList);
                } else {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                        android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "Reusing existing app zygote.");
                    }
                    this.mService.mHandler.removeMessages(71, appZygote);
                    zygoteProcessList = this.mAppZygoteProcesses.get(appZygote);
                }
                zygoteProcessList.add(app);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        return appZygote;
    }

    private java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.Long>> getPackageAppDataInfoMap(android.content.pm.PackageManagerInternal pmInt, java.lang.String[] packages, int uid) {
        java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.Long>> result = new android.util.ArrayMap<>(packages.length);
        int userId = android.os.UserHandle.getUserId(uid);
        for (java.lang.String packageName : packages) {
            com.android.server.pm.pkg.PackageStateInternal packageState = pmInt.getPackageStateInternal(packageName);
            if (packageState == null) {
                android.util.Slog.w("ActivityManager", "Unknown package:" + packageName);
            } else {
                java.lang.String volumeUuid = packageState.getVolumeUuid();
                long inode = packageState.getUserStateOrDefault(userId).getCeDataInode();
                if (inode == 0) {
                    android.util.Slog.w("ActivityManager", packageName + " inode == 0 (b/152760674)");
                    return null;
                }
                result.put(packageName, android.util.Pair.create(volumeUuid, java.lang.Long.valueOf(inode)));
            }
        }
        return result;
    }

    private boolean needsStorageDataIsolation(android.os.storage.StorageManagerInternal storageManagerInternal, com.android.server.am.ProcessRecord app) {
        int mountMode = app.getMountMode();
        return (!this.mVoldAppDataIsolationEnabled || !android.os.UserHandle.isApp(app.uid) || storageManagerInternal.isExternalStorageService(app.uid) || mountMode == 4 || mountMode == 3 || mountMode == 2 || mountMode == 0) ? false : true;
    }

    private android.os.Process.ProcessStartResult startProcess(com.android.server.am.HostingRecord hostingRecord, java.lang.String entryPoint, com.android.server.am.ProcessRecord app, int uid, int[] gids, int runtimeFlags, int zygotePolicyFlags, int mountExternal, java.lang.String seInfo, java.lang.String requiredAbi, java.lang.String instructionSet, java.lang.String invokeWith, long startTime) throws java.lang.Throwable {
        long j;
        java.lang.String[] targetPackagesList;
        boolean bindMountAppsData;
        boolean bindMountAppStorageDirs;
        java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.Long>> allowlistedAppDataInfoMap;
        java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.Long>> pkgDataInfoMap;
        boolean bindOverrideSysprops;
        java.lang.String[] pkgs;
        boolean regularZygote;
        android.os.storage.StorageManagerInternal storageManagerInternal;
        int userId;
        int zygotePolicyFlags2;
        android.os.Process.ProcessStartResult startResult;
        boolean isTopApp;
        try {
            android.os.Trace.traceBegin(64L, "Start proc: " + app.processName);
            checkSlow(startTime, "startProcess: asking zygote to start proc");
            boolean isTopApp2 = hostingRecord.isTopApp();
            if (isTopApp2) {
                app.mState.setHasForegroundActivities(true);
            }
            boolean bindMountAppsData2 = this.mAppDataIsolationEnabled && (android.os.UserHandle.isApp(app.uid) || android.os.UserHandle.isIsolated(app.uid) || app.isSdkSandbox) && this.mPlatformCompat.isChangeEnabled(APP_DATA_DIRECTORY_ISOLATION, app.info);
            android.content.pm.PackageManagerInternal pmInt = this.mService.getPackageManagerInternal();
            if (app.isSdkSandbox) {
                targetPackagesList = new java.lang.String[]{app.sdkSandboxClientAppPackage};
            } else {
                java.lang.String[] sharedPackages = pmInt.getSharedUserPackagesForPackage(app.info.packageName, app.userId);
                targetPackagesList = sharedPackages.length == 0 ? new java.lang.String[]{app.info.packageName} : sharedPackages;
            }
            boolean hasAppStorage = hasAppStorage(pmInt, app.info.packageName);
            java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.Long>> pkgDataInfoMap2 = getPackageAppDataInfoMap(pmInt, targetPackagesList, uid);
            if (pkgDataInfoMap2 == null) {
                bindMountAppsData2 = false;
            }
            if (this.mAppDataIsolationAllowlistedApps == null) {
                try {
                    this.mAppDataIsolationAllowlistedApps = new java.util.ArrayList<>(com.android.server.SystemConfig.getInstance().getAppDataIsolationWhitelistedApps());
                } catch (java.lang.Throwable th) {
                    th = th;
                    j = 64;
                    android.os.Trace.traceEnd(j);
                    throw th;
                }
            }
            java.util.Set<java.lang.String> allowlistedApps = new android.util.ArraySet<>(this.mAppDataIsolationAllowlistedApps);
            for (java.lang.String pkg : targetPackagesList) {
                allowlistedApps.remove(pkg);
            }
            java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.Long>> allowlistedAppDataInfoMap2 = getPackageAppDataInfoMap(pmInt, (java.lang.String[]) allowlistedApps.toArray(new java.lang.String[0]), uid);
            if (allowlistedAppDataInfoMap2 == null) {
                bindMountAppsData2 = false;
            }
            if (hasAppStorage || app.isSdkSandbox) {
                bindMountAppsData = bindMountAppsData2;
            } else {
                pkgDataInfoMap2 = null;
                allowlistedAppDataInfoMap2 = null;
                bindMountAppsData = false;
            }
            int userId2 = android.os.UserHandle.getUserId(uid);
            android.os.storage.StorageManagerInternal storageManagerInternal2 = (android.os.storage.StorageManagerInternal) com.android.server.LocalServices.getService(android.os.storage.StorageManagerInternal.class);
            if (!needsStorageDataIsolation(storageManagerInternal2, app)) {
                bindMountAppStorageDirs = false;
            } else if (pkgDataInfoMap2 == null || !storageManagerInternal2.isFuseMounted(userId2)) {
                app.setBindMountPending(true);
                bindMountAppStorageDirs = false;
            } else {
                bindMountAppStorageDirs = true;
            }
            boolean bindMountAppStorageDirs2 = app.isolated;
            if (bindMountAppStorageDirs2) {
                allowlistedAppDataInfoMap = null;
                pkgDataInfoMap = null;
            } else {
                allowlistedAppDataInfoMap = allowlistedAppDataInfoMap2;
                pkgDataInfoMap = pkgDataInfoMap2;
            }
            java.lang.String[] syspropOverridePkgNames = android.provider.DeviceConfig.getString("app_compat", "appcompat_sysprop_override_pkgs", "").split(",");
            java.lang.String[] pkgs2 = app.getPackageList();
            int i = 0;
            while (true) {
                if (i >= pkgs2.length) {
                    bindOverrideSysprops = false;
                    break;
                }
                if (com.android.internal.util.ArrayUtils.contains(syspropOverridePkgNames, pkgs2[i])) {
                    bindOverrideSysprops = true;
                    break;
                }
                i++;
            }
            com.android.server.AppStateTracker ast = this.mService.mServices.mAppStateTracker;
            if (ast != null) {
                boolean inBgRestricted = ast.isAppBackgroundRestricted(app.info.uid, app.info.packageName);
                if (inBgRestricted) {
                    pkgs = pkgs2;
                    com.android.server.am.ActivityManagerService activityManagerService = this.mService;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            this.mAppsInBackgroundRestricted.add(app);
                        } catch (java.lang.Throwable th2) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th2;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                } else {
                    pkgs = pkgs2;
                }
                app.mState.setBackgroundRestricted(inBgRestricted);
            } else {
                pkgs = pkgs2;
            }
            app.mProcessGroupCreated = false;
            app.mSkipProcessGroupCreation = false;
            long forkTimeNs = android.os.SystemClock.uptimeNanos();
            try {
                if (hostingRecord.usesWebviewZygote()) {
                    try {
                        regularZygote = false;
                        storageManagerInternal = storageManagerInternal2;
                        userId = userId2;
                        j = 64;
                        zygotePolicyFlags2 = zygotePolicyFlags;
                        startResult = android.os.Process.startWebView(entryPoint, app.processName, uid, uid, gids, runtimeFlags, mountExternal, app.info.targetSdkVersion, seInfo, requiredAbi, instructionSet, app.info.dataDir, null, app.info.packageName, app.getDisabledCompatChanges(), new java.lang.String[]{"seq=" + app.getStartSeq()});
                        isTopApp = isTopApp2;
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        j = 64;
                        android.os.Trace.traceEnd(j);
                        throw th;
                    }
                } else {
                    regularZygote = false;
                    storageManagerInternal = storageManagerInternal2;
                    userId = userId2;
                    j = 64;
                    boolean regularZygote2 = hostingRecord.usesAppZygote();
                    if (regularZygote2) {
                        this.mProcListWrapper.getExtImpl().handleAppZygoteStart(app.info);
                        android.os.AppZygote appZygote = createAppZygoteForProcessIfNeeded(app);
                        startResult = appZygote.getProcess().start(entryPoint, app.processName, uid, uid, gids, runtimeFlags, mountExternal, app.info.targetSdkVersion, seInfo, requiredAbi, instructionSet, app.info.dataDir, (java.lang.String) null, app.info.packageName, 0, isTopApp2, app.getDisabledCompatChanges(), pkgDataInfoMap, allowlistedAppDataInfoMap, false, false, false, new java.lang.String[]{"seq=" + app.getStartSeq()});
                        zygotePolicyFlags2 = zygotePolicyFlags;
                        isTopApp = isTopApp2;
                    } else {
                        try {
                            zygotePolicyFlags2 = ((com.android.internal.os.IZygoteConfigSocExt) system.ext.loader.core.ExtLoader.type(com.android.internal.os.IZygoteConfigSocExt.class).create()).updateZygotePolicyFlags(this.mService.mContext.getContentResolver(), zygotePolicyFlags);
                            try {
                                startResult = android.os.Process.start(entryPoint, app.processName, uid, uid, gids, runtimeFlags, mountExternal, app.info.targetSdkVersion, seInfo, requiredAbi, instructionSet, app.info.dataDir, invokeWith, app.info.packageName, zygotePolicyFlags2, isTopApp2, app.getDisabledCompatChanges(), pkgDataInfoMap, allowlistedAppDataInfoMap, bindMountAppsData, bindMountAppStorageDirs, bindOverrideSysprops, new java.lang.String[]{"seq=" + app.getStartSeq()});
                                app.mProcessGroupCreated = true;
                                isTopApp = isTopApp2;
                                this.mProcListWrapper.getExtImpl().setUxForStartProcess(startResult, app, isTopApp);
                                regularZygote = true;
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                android.os.Trace.traceEnd(j);
                                throw th;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                            android.os.Trace.traceEnd(j);
                            throw th;
                        }
                    }
                }
                boolean regularZygote3 = android.app.Flags.appStartInfoTimestamps();
                if (regularZygote3) {
                    this.mAppStartInfoTracker.addTimestampToStart(app, forkTimeNs, 1);
                }
                try {
                    if (!regularZygote) {
                        try {
                            try {
                                synchronized (app) {
                                    try {
                                        if (!app.mSkipProcessGroupCreation) {
                                            int res = android.os.Process.createProcessGroup(uid, startResult.pid);
                                            if (res >= 0) {
                                                app.mProcessGroupCreated = true;
                                            } else if (res == (-android.system.OsConstants.ESRCH)) {
                                                android.util.Slog.e("ActivityManager", "Unable to create process group for " + app.processName + " (" + startResult.pid + ")");
                                            } else {
                                                if (res != (-android.system.OsConstants.ENOENT)) {
                                                    throw new java.lang.AssertionError("Unable to create process group for " + app.processName + " (" + startResult.pid + ")");
                                                }
                                                android.util.Slog.e("ActivityManager", "Unable to create process group for " + app.processName + " (" + startResult.pid + "), Reason: No such file or directory");
                                            }
                                        }
                                    } catch (java.lang.Throwable th6) {
                                        th = th6;
                                        throw th;
                                    }
                                }
                            } catch (java.lang.Throwable th7) {
                                th = th7;
                            }
                        } catch (java.lang.Throwable th8) {
                            th = th8;
                            android.os.Trace.traceEnd(j);
                            throw th;
                        }
                    }
                    if (isTopApp) {
                        android.os.PerformanceManager.addTaskTrackPid(1, startResult.pid, false);
                    }
                    if (bindMountAppStorageDirs) {
                        storageManagerInternal.prepareStorageDirs(userId, pkgDataInfoMap.keySet(), app.processName);
                    }
                    try {
                        this.mProcListWrapper.getSocExtImpl().startProcess(hostingRecord, startResult, app);
                        try {
                            checkSlow(startTime, "startProcess: returned from zygote!");
                            android.os.Trace.traceEnd(j);
                            return startResult;
                        } catch (java.lang.Throwable th9) {
                            th = th9;
                            android.os.Trace.traceEnd(j);
                            throw th;
                        }
                    } catch (java.lang.Throwable th10) {
                        th = th10;
                        android.os.Trace.traceEnd(j);
                        throw th;
                    }
                } catch (java.lang.Throwable th11) {
                    th = th11;
                }
            } catch (java.lang.Throwable th12) {
                th = th12;
            }
        } catch (java.lang.Throwable th13) {
            th = th13;
            j = 64;
        }
    }

    private boolean hasAppStorage(android.content.pm.PackageManagerInternal pmInt, java.lang.String packageName) {
        com.android.server.pm.pkg.AndroidPackage pkg = pmInt.getPackage(packageName);
        if (pkg == null) {
            android.util.Slog.w("ActivityManager", "Unknown package " + packageName);
            return false;
        }
        android.content.pm.PackageManager.Property noAppStorageProp = (android.content.pm.PackageManager.Property) pkg.getProperties().get("android.internal.PROPERTY_NO_APP_DATA_STORAGE");
        return noAppStorageProp == null || !noAppStorageProp.getBoolean();
    }

    void startProcessLocked(com.android.server.am.ProcessRecord app, com.android.server.am.HostingRecord hostingRecord, int zygotePolicyFlags) {
        startProcessLocked(app, hostingRecord, zygotePolicyFlags, null);
    }

    boolean startProcessLocked(com.android.server.am.ProcessRecord app, com.android.server.am.HostingRecord hostingRecord, int zygotePolicyFlags, java.lang.String abiOverride) {
        return startProcessLocked(app, hostingRecord, zygotePolicyFlags, false, false, abiOverride);
    }

    com.android.server.am.ProcessRecord startProcessLocked(java.lang.String processName, android.content.pm.ApplicationInfo info, boolean knownToBeDead, int intentFlags, com.android.server.am.HostingRecord hostingRecord, int zygotePolicyFlags, boolean allowWhileBooting, boolean isolated, int isolatedUid, boolean isSdkSandbox, int sdkSandboxUid, java.lang.String sdkSandboxClientAppPackage, java.lang.String abiOverride, java.lang.String entryPoint, java.lang.String[] entryPointArgs, java.lang.Runnable crashHandler) {
        com.android.server.am.ProcessRecord app;
        com.android.server.am.ProcessRecord app2;
        com.android.server.am.ProcessRecord predecessor;
        long startTime;
        com.android.server.am.ProcessList processList;
        long startTime2 = android.os.SystemClock.uptimeMillis();
        android.os.SystemClock.elapsedRealtimeNanos();
        if (this.mProcListWrapper.getExtImpl().returnIsRunningDisallowed(info.packageName)) {
            return null;
        }
        if (!isolated) {
            com.android.server.am.ProcessRecord app3 = getProcessRecordLocked(processName, info.uid);
            checkSlow(startTime2, "startProcess: after getProcessRecord");
            if ((intentFlags & 4) != 0) {
                if (this.mService.mAppErrors.isBadProcess(processName, info.uid)) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                        android.util.Slog.v("ActivityManager", "Bad process: " + info.uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + processName);
                    }
                    return null;
                }
            } else {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    android.util.Slog.v("ActivityManager", "Clearing bad process: " + info.uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + processName);
                }
                this.mService.mAppErrors.resetProcessCrashTime(processName, info.uid);
                if (this.mService.mAppErrors.isBadProcess(processName, info.uid)) {
                    android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_PROC_GOOD, java.lang.Integer.valueOf(android.os.UserHandle.getUserId(info.uid)), java.lang.Integer.valueOf(info.uid), info.processName);
                    this.mService.mAppErrors.clearBadProcess(processName, info.uid);
                    if (app3 != null) {
                        app3.mErrorState.setBad(false);
                    }
                }
            }
            app = app3;
        } else {
            app = null;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.v(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "startProcess: name=" + processName + " app=" + app + " knownToBeDead=" + knownToBeDead + " thread=" + (app != null ? app.getThread() : null) + " pid=" + (app != null ? app.getPid() : -1));
        }
        com.android.server.am.ProcessRecord predecessor2 = null;
        if (app != null && app.getPid() > 0) {
            if ((!knownToBeDead && !app.isKilled()) || app.getThread() == null) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    android.util.Slog.v(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "App already running: " + app);
                }
                app.addPackage(info.packageName, info.longVersionCode, this.mService.mProcessStats);
                this.mProcListWrapper.getExtImpl().decideHandleActivityStart(hostingRecord, app);
                checkSlow(startTime2, "startProcess: done, added package to proc");
                return app;
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.v(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "App died: " + app);
            }
            checkSlow(startTime2, "startProcess: bad proc running, killing");
            killProcessGroup(app.uid, app.getPid());
            checkSlow(startTime2, "startProcess: done killing old proc");
            if (!app.isKilled()) {
                android.util.Slog.wtf(com.android.server.am.ActivityManagerService.TAG_PROCESSES, app.toString() + " is attached to a previous process");
            } else {
                android.util.Slog.w(com.android.server.am.ActivityManagerService.TAG_PROCESSES, app.toString() + " is attached to a previous process");
            }
            app2 = null;
            predecessor = app;
        } else if (isolated) {
            app2 = app;
            predecessor = null;
        } else {
            try {
                predecessor2 = (com.android.server.am.ProcessRecord) this.mDyingProcesses.get(processName, info.uid);
            } catch (java.util.ConcurrentModificationException e) {
                if (this.mService == null || java.lang.Thread.holdsLock(this.mService)) {
                    android.util.Slog.e("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException");
                } else {
                    android.util.Slog.wtf("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException, didn't hold mService lock");
                }
            }
            if (predecessor2 == null) {
                app2 = app;
                predecessor = predecessor2;
            } else {
                if (app != null && app != predecessor2) {
                    app.mPredecessor = predecessor2;
                    predecessor2.mSuccessor = app;
                } else {
                    app = null;
                }
                android.util.Slog.w(com.android.server.am.ActivityManagerService.TAG_PROCESSES, predecessor2.toString() + " is attached to a previous process " + predecessor2.getDyingPid());
                app2 = app;
                predecessor = predecessor2;
            }
        }
        if (app2 == null) {
            checkSlow(startTime2, "startProcess: creating new process record");
            com.android.server.am.ProcessRecord predecessor3 = predecessor;
            app2 = newProcessRecordLocked(info, processName, isolated, isolatedUid, isSdkSandbox, sdkSandboxUid, sdkSandboxClientAppPackage, hostingRecord);
            if (app2 == null) {
                android.util.Slog.w("ActivityManager", "Failed making new process record for " + processName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + info.uid + " isolated=" + isolated);
                return null;
            }
            app2.mErrorState.setCrashHandler(crashHandler);
            app2.setIsolatedEntryPoint(entryPoint);
            app2.setIsolatedEntryPointArgs(entryPointArgs);
            if (predecessor3 != null) {
                app2.mPredecessor = predecessor3;
                predecessor3.mSuccessor = app2;
            }
            processList = this;
            startTime = startTime2;
            processList.checkSlow(startTime, "startProcess: done creating new process record");
        } else {
            startTime = startTime2;
            processList = this;
            app2.addPackage(info.packageName, info.longVersionCode, processList.mService.mProcessStats);
            processList.checkSlow(startTime, "startProcess: added package to existing proc");
        }
        if (!processList.mService.mProcessesReady && !processList.mService.isAllowedWhileBooting(info) && !allowWhileBooting) {
            if (!processList.mService.mProcessesOnHold.contains(app2)) {
                processList.mService.mProcessesOnHold.add(app2);
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.v(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "System not ready, putting on hold: " + app2);
            }
            processList.checkSlow(startTime, "startProcess: returning with proc on hold");
            return app2;
        }
        processList.checkSlow(startTime, "startProcess: stepping in to startProcess");
        boolean success = processList.startProcessLocked(app2, hostingRecord, zygotePolicyFlags, abiOverride);
        processList.checkSlow(startTime, "startProcess: done starting proc!");
        if (success) {
            return app2;
        }
        return null;
    }

    java.lang.String isProcStartValidLocked(com.android.server.am.ProcessRecord app, long expectedStartSeq) {
        if (app.isKilledByAm()) {
            sb = 0 == 0 ? new java.lang.StringBuilder() : null;
            sb.append("killedByAm=true;");
        }
        if (this.mProcessNames.get(app.processName, app.uid) != app) {
            if (sb == null) {
                sb = new java.lang.StringBuilder();
            }
            sb.append("No entry in mProcessNames;");
        }
        if (!app.isPendingStart()) {
            if (sb == null) {
                sb = new java.lang.StringBuilder();
            }
            sb.append("pendingStart=false;");
        }
        if (app.getStartSeq() > expectedStartSeq) {
            if (sb == null) {
                sb = new java.lang.StringBuilder();
            }
            sb.append("seq=" + app.getStartSeq() + ",expected=" + expectedStartSeq + ";");
        }
        try {
            android.app.AppGlobals.getPackageManager().checkPackageStartable(app.info.packageName, app.userId);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.SecurityException e2) {
            if (this.mService.mConstants.FLAG_PROCESS_START_ASYNC) {
                if (sb == null) {
                    sb = new java.lang.StringBuilder();
                }
                sb.append("Package is frozen;");
            } else {
                throw e2;
            }
        }
        if (sb == null) {
            return null;
        }
        return sb.toString();
    }

    private boolean handleProcessStartedLocked(com.android.server.am.ProcessRecord pending, android.os.Process.ProcessStartResult startResult, long expectedStartSeq) {
        if (this.mPendingStarts.get(expectedStartSeq) == null) {
            if (pending.getPid() == startResult.pid) {
                pending.setUsingWrapper(startResult.usingWrapper);
                return false;
            }
            return false;
        }
        return handleProcessStartedLocked(pending, startResult.pid, startResult.usingWrapper, expectedStartSeq, false);
    }

    boolean handleProcessStartedLocked(final com.android.server.am.ProcessRecord app, final int pid, boolean usingWrapper, long expectedStartSeq, boolean procAttached) throws java.lang.Throwable {
        com.android.server.am.ProcessRecord oldApp;
        this.mPendingStarts.remove(expectedStartSeq);
        java.lang.String reason = isProcStartValidLocked(app, expectedStartSeq);
        if (reason != null) {
            android.util.Slog.w(com.android.server.am.ActivityManagerService.TAG_PROCESSES, app + " start not valid, killing pid=" + pid + ", " + reason);
            app.setPendingStart(false);
            android.os.Process.killProcessQuiet(pid);
            int appPid = app.getPid();
            if (appPid != 0) {
                android.os.Process.killProcessGroup(app.uid, appPid);
            }
            noteAppKill(app, 13, 13, reason);
            app.doEarlyCleanupIfNecessaryLocked();
            this.mProcListWrapper.getExtImpl().decideCleanupAppInLaunchingProvidersLocked(this.mService, app);
            return false;
        }
        this.mService.mBatteryStatsService.noteProcessStart(app.processName, app.info.uid);
        checkSlow(app.getStartTime(), "startProcess: done updating battery stats");
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_PROC_START, java.lang.Integer.valueOf(android.os.UserHandle.getUserId(app.getStartUid())), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(app.getStartUid()), app.processName, app.getHostingRecord().getType(), app.getHostingRecord().getName() != null ? app.getHostingRecord().getName() : "", this.mProcessListExt.callerInfoPrint(app, app.getHostingRecord().getType()));
        try {
            android.app.AppGlobals.getPackageManager().logAppProcessStartIfNeeded(app.info.packageName, app.processName, app.uid, app.getSeInfo(), app.info.sourceDir, pid);
        } catch (android.os.RemoteException e) {
        }
        com.android.server.Watchdog.getInstance().processStarted(app.processName, pid);
        this.mProcListWrapper.getExtImpl().sendApplicationStartAndDump(app, pid, this.mService);
        checkSlow(app.getStartTime(), "startProcess: building log message");
        java.lang.StringBuilder buf = this.mStringBuilder;
        buf.setLength(0);
        buf.append("Start proc ");
        buf.append(pid);
        buf.append(':');
        buf.append(app.processName);
        buf.append('/');
        android.os.UserHandle.formatUid(buf, app.getStartUid());
        if (app.getIsolatedEntryPoint() != null) {
            buf.append(" [");
            buf.append(app.getIsolatedEntryPoint());
            buf.append("]");
        }
        buf.append(" for ");
        buf.append(app.getHostingRecord().getType());
        if (app.getHostingRecord().getName() != null) {
            buf.append(" ");
            buf.append(app.getHostingRecord().getName());
        }
        this.mService.reportUidInfoMessageLocked("ActivityManager", buf.toString(), app.getStartUid());
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                app.setPid(pid);
                app.setUsingWrapper(usingWrapper);
                app.setPendingStart(false);
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        checkSlow(app.getStartTime(), "startProcess: starting to update pids map");
        synchronized (this.mService.mPidsSelfLocked) {
            try {
                oldApp = this.mService.mPidsSelfLocked.get(pid);
            } catch (java.lang.Throwable th3) {
                th = th3;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                }
            }
        }
        if (oldApp != null && !app.isolated) {
            android.util.Slog.wtf("ActivityManager", "handleProcessStartedLocked process:" + app.processName + " startSeq:" + app.getStartSeq() + " pid:" + pid + " belongs to another existing app:" + oldApp.processName + " startSeq:" + oldApp.getStartSeq());
            this.mService.cleanUpApplicationRecordLocked(oldApp, pid, false, false, -1, true, false);
        }
        this.mService.addPidLocked(app);
        synchronized (this.mService.mPidsSelfLocked) {
            if (!procAttached) {
                android.os.Message msg = this.mService.mHandler.obtainMessage(20);
                msg.obj = app;
                this.mService.mHandler.sendMessageDelayed(msg, usingWrapper ? 1200000L : com.android.server.am.ActivityManagerService.PROC_START_TIMEOUT);
            }
        }
        this.mService.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleProcessStartedLocked$2(app, pid);
            }
        });
        checkSlow(app.getStartTime(), "startProcess: done updating pids map");
        this.mProcListWrapper.getExtImpl().hookHandleProcessStart(app);
        return true;
    }

    void removeLruProcessLocked(com.android.server.am.ProcessRecord app) {
        int lrui = this.mLruProcesses.lastIndexOf(app);
        if (lrui >= 0) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    if (!app.isKilled()) {
                        if (app.isPersistent()) {
                            android.util.Slog.w("ActivityManager", "Removing persistent process that hasn't been killed: " + app);
                        } else {
                            android.util.Slog.wtfStack("ActivityManager", "Removing process that hasn't been killed: " + app);
                            if (app.getPid() > 0) {
                                android.os.Process.killProcessQuiet(app.getPid());
                                killProcessGroup(app.uid, app.getPid());
                                noteAppKill(app, 13, 16, "hasn't been killed");
                            } else {
                                app.setPendingStart(false);
                            }
                        }
                    }
                    if (lrui < this.mLruProcessActivityStart) {
                        this.mLruProcessActivityStart--;
                    }
                    if (lrui < this.mLruProcessServiceStart) {
                        this.mLruProcessServiceStart--;
                    }
                    this.mLruProcesses.remove(lrui);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
        this.mService.removeOomAdjTargetLocked(app, true);
    }

    boolean killPackageProcessesLSP(java.lang.String packageName, int appId, int userId, int minOomAdj, int reasonCode, int subReason, java.lang.String reason) {
        return killPackageProcessesLSP(packageName, appId, userId, minOomAdj, false, true, true, false, false, false, reasonCode, subReason, reason);
    }

    void killAppZygotesLocked(java.lang.String packageName, int appId, int userId, boolean force) {
        java.util.ArrayList<android.os.AppZygote> zygotesToKill = new java.util.ArrayList<>();
        for (android.util.SparseArray<android.os.AppZygote> appZygotes : this.mAppZygotes.getMap().values()) {
            for (int i = 0; i < appZygotes.size(); i++) {
                int appZygoteUid = appZygotes.keyAt(i);
                if ((userId == -1 || android.os.UserHandle.getUserId(appZygoteUid) == userId) && (appId < 0 || android.os.UserHandle.getAppId(appZygoteUid) == appId)) {
                    android.os.AppZygote appZygote = appZygotes.valueAt(i);
                    if (packageName == null || packageName.equals(appZygote.getAppInfo().packageName)) {
                        zygotesToKill.add(appZygote);
                    }
                }
            }
        }
        java.util.Iterator<android.os.AppZygote> it = zygotesToKill.iterator();
        while (it.hasNext()) {
            killAppZygoteIfNeededLocked(it.next(), force);
        }
    }

    private static boolean freezePackageCgroup(int packageUID, boolean freeze) {
        try {
            android.os.Process.freezeCgroupUid(packageUID, freeze);
            return true;
        } catch (java.lang.RuntimeException e) {
            java.lang.String logtxt = freeze ? "freeze" : "unfreeze";
            android.util.Slog.e("ActivityManager", "Unable to " + logtxt + " cgroup uid: " + packageUID + ": " + e);
            return false;
        }
    }

    private static boolean unfreezePackageCgroup(int packageUID) {
        return freezePackageCgroup(packageUID, false);
    }

    private static void freezeBinderAndPackageCgroup(java.util.List<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Boolean>> procs, int packageUID) {
        int rc;
        int N = procs.size();
        for (int i = 0; i < N; i++) {
            int pid = ((com.android.server.am.ProcessRecord) procs.get(i).first).getPid();
            int nRetries = 0;
            if (pid > 0) {
                while (true) {
                    try {
                        rc = com.android.server.am.CachedAppOptimizer.freezeBinder(pid, true, 10);
                        if (rc != (-android.system.OsConstants.EAGAIN)) {
                            break;
                        }
                        int nRetries2 = nRetries + 1;
                        if (nRetries >= 1) {
                            break;
                        } else {
                            nRetries = nRetries2;
                        }
                    } catch (java.lang.RuntimeException e) {
                        android.util.Slog.e("ActivityManager", "Unable to freeze binder for " + pid + ": " + e);
                    }
                }
                if (rc != 0) {
                    android.util.Slog.e("ActivityManager", "Unable to freeze binder for " + pid + ": " + rc);
                }
            }
        }
        freezePackageCgroup(packageUID, true);
    }

    private static java.util.List<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Boolean>> getUIDSublist(java.util.List<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Boolean>> procs, int startIdx) {
        int uid = ((com.android.server.am.ProcessRecord) procs.get(startIdx).first).uid;
        int endIdx = startIdx + 1;
        while (endIdx < procs.size() && ((com.android.server.am.ProcessRecord) procs.get(endIdx).first).uid == uid) {
            endIdx++;
        }
        return procs.subList(startIdx, endIdx);
    }

    boolean killPackageProcessesLSP(java.lang.String packageName, int appId, int userId, int minOomAdj, boolean callerWillRestart, boolean allowRestart, boolean doit, boolean evenPersistent, boolean setRemoved, boolean uninstalling, int reasonCode, int subReason, java.lang.String reason) {
        android.util.SparseArray<com.android.server.am.ProcessRecord> apps;
        int NA;
        boolean isInPkgList;
        boolean shouldAllowRestart;
        boolean shouldAllowRestart2;
        android.content.pm.PackageManagerInternal pm = this.mService.getPackageManagerInternal();
        java.util.ArrayList<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Boolean>> procs = new java.util.ArrayList<>();
        int reasonCode2 = this.mProcListWrapper.getExtImpl().updateReasonCodeIfNeeded(reasonCode);
        int subReason2 = this.mProcListWrapper.getExtImpl().updateSubReasonIfNeeded(subReason);
        int NP = this.mProcessNames.getMap().size();
        for (int ip = 0; ip < NP; ip++) {
            android.util.SparseArray<com.android.server.am.ProcessRecord> apps2 = (android.util.SparseArray) this.mProcessNames.getMap().valueAt(ip);
            int NA2 = apps2.size();
            int ia = 0;
            while (ia < NA2) {
                com.android.server.am.ProcessRecord app = apps2.valueAt(ia);
                if (this.mService.mKillBackgroundProcessesCallingUid == 2000 && this.mProcessListExt.isForbidKill(app.info.packageName)) {
                    android.util.Slog.i("ActivityManager", "forbid kill packageName:" + app.info.packageName);
                    apps = apps2;
                    NA = NA2;
                } else if (app.isPersistent() && !evenPersistent) {
                    apps = apps2;
                    NA = NA2;
                } else if (app.isRemoved()) {
                    if (!doit) {
                        apps = apps2;
                        NA = NA2;
                    } else {
                        boolean shouldAllowRestart3 = false;
                        if (uninstalling || packageName == null) {
                            apps = apps2;
                            NA = NA2;
                        } else {
                            if (app.getPkgList().containsKey(packageName) || app.getPkgDeps() == null || !app.getPkgDeps().contains(packageName) || app.info == null) {
                                apps = apps2;
                                NA = NA2;
                            } else {
                                java.lang.String str = app.info.packageName;
                                apps = apps2;
                                int i = app.uid;
                                NA = NA2;
                                int NA3 = app.userId;
                                boolean z = pm.isPackageFrozen(str, i, NA3) ? false : true;
                                shouldAllowRestart3 = z;
                            }
                            shouldAllowRestart3 = z;
                        }
                        procs.add(new android.util.Pair<>(app, java.lang.Boolean.valueOf(shouldAllowRestart3)));
                    }
                } else {
                    apps = apps2;
                    NA = NA2;
                    if (app.mState.getSetAdj() >= minOomAdj) {
                        if (packageName == null) {
                            if ((userId == -1 || app.userId == userId) && (appId < 0 || android.os.UserHandle.getAppId(app.uid) == appId)) {
                                shouldAllowRestart = false;
                                shouldAllowRestart2 = shouldAllowRestart;
                            }
                        } else {
                            boolean isDep = app.getPkgDeps() != null && app.getPkgDeps().contains(packageName);
                            if ((isDep || android.os.UserHandle.getAppId(app.uid) == appId) && ((userId == -1 || app.userId == userId) && ((isInPkgList = app.getPkgList().containsKey(packageName)) || isDep))) {
                                if (isInPkgList || !isDep || uninstalling) {
                                    shouldAllowRestart = false;
                                } else {
                                    shouldAllowRestart = false;
                                    if (app.info != null && !pm.isPackageFrozen(app.info.packageName, app.uid, app.userId)) {
                                        shouldAllowRestart2 = true;
                                    }
                                }
                                shouldAllowRestart2 = shouldAllowRestart;
                            }
                        }
                        if (!doit) {
                            return true;
                        }
                        if (setRemoved) {
                            app.setRemoved(true);
                        }
                        procs.add(new android.util.Pair<>(app, java.lang.Boolean.valueOf(shouldAllowRestart2)));
                    } else {
                        continue;
                    }
                }
                ia++;
                apps2 = apps;
                NA2 = NA;
            }
        }
        boolean killingUserApp = appId >= 10000 && appId <= 19999;
        if (killingUserApp) {
            procs.sort(new java.util.Comparator() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda3
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Integer.compare(((com.android.server.am.ProcessRecord) ((android.util.Pair) obj).first).uid, ((com.android.server.am.ProcessRecord) ((android.util.Pair) obj2).first).uid);
                }
            });
        }
        int idx = 0;
        while (idx < procs.size()) {
            java.util.List<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Boolean>> uidProcs = getUIDSublist(procs, idx);
            boolean z2 = false;
            int packageUID = ((com.android.server.am.ProcessRecord) uidProcs.get(0).first).uid;
            boolean doFreeze = killingUserApp && android.os.UserHandle.getAppId(packageUID) == appId;
            if (doFreeze) {
                freezeBinderAndPackageCgroup(uidProcs, packageUID);
            }
            for (android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Boolean> proc : uidProcs) {
                removeProcessLocked((com.android.server.am.ProcessRecord) proc.first, callerWillRestart, (allowRestart || ((java.lang.Boolean) proc.second).booleanValue()) ? true : z2, reasonCode2, subReason2, reason, !doFreeze ? true : z2);
                z2 = z2;
                packageUID = packageUID;
                uidProcs = uidProcs;
                NP = NP;
                idx = idx;
                pm = pm;
            }
            int packageUID2 = packageUID;
            java.util.List<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Boolean>> uidProcs2 = uidProcs;
            int idx2 = idx;
            int NP2 = NP;
            android.content.pm.PackageManagerInternal pm2 = pm;
            killAppZygotesLocked(packageName, appId, userId, z2);
            if (doFreeze) {
                unfreezePackageCgroup(packageUID2);
            }
            idx = idx2 + uidProcs2.size();
            NP = NP2;
            pm = pm2;
        }
        this.mService.mOomAdjuster.getWrapper().setFullOomAdjUpdateInfo(appId, packageName, userId > 0 ? "userid " + userId : null);
        this.mService.updateOomAdjLocked(12);
        return procs.size() > 0;
    }

    boolean removeProcessLocked(com.android.server.am.ProcessRecord app, boolean callerWillRestart, boolean allowRestart, int reasonCode, java.lang.String reason) {
        return removeProcessLocked(app, callerWillRestart, allowRestart, reasonCode, 0, reason, true);
    }

    boolean removeProcessLocked(com.android.server.am.ProcessRecord app, boolean callerWillRestart, boolean allowRestart, int reasonCode, int subReason, java.lang.String reason) {
        return removeProcessLocked(app, callerWillRestart, allowRestart, reasonCode, subReason, reason, true);
    }

    boolean removeProcessLocked(com.android.server.am.ProcessRecord app, boolean callerWillRestart, boolean allowRestart, int reasonCode, int subReason, java.lang.String reason, boolean async) {
        boolean needRestart;
        boolean willRestart;
        java.lang.String name = app.processName;
        int uid = app.uid;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_PROCESSES, "Force removing proc " + app.toShortString() + " (" + name + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + ")");
        }
        com.android.server.am.ProcessRecord old = (com.android.server.am.ProcessRecord) this.mProcessNames.get(name, uid);
        if (old != app) {
            android.util.Slog.w("ActivityManager", "Ignoring remove of inactive process: " + app);
            return false;
        }
        removeProcessNameLocked(name, uid);
        this.mService.mAtmInternal.clearHeavyWeightProcessIfEquals(app.getWindowProcessController());
        int processId = this.mProcListWrapper.getExtImpl().returnRrocessRecordPid(app.getPid());
        int pid = app.getPid();
        if ((pid > 0 && pid != com.android.server.am.ActivityManagerService.MY_PID) || (pid == 0 && app.isPendingStart())) {
            if (pid > 0) {
                if (!LTW_DISABLE) {
                    this.mService.mActivityTaskManager.getWrapper().getExtImpl().getRemoteTaskManager().handleProcessDied(app.getWindowProcessController());
                }
                this.mService.removePidLocked(pid, app);
                app.setBindMountPending(false);
                this.mService.mHandler.removeMessages(20, app);
                this.mService.mBatteryStatsService.noteProcessFinish(app.processName, app.info.uid);
                if (app.isolated) {
                    this.mService.mBatteryStatsService.removeIsolatedUid(app.uid, app.info.uid);
                    this.mService.getPackageManagerInternal().removeIsolatedUid(app.uid);
                    this.mProcListWrapper.getExtImpl().hookRemoveIsolatedUid(app);
                }
            }
            if (app.isPersistent() && !app.isolated) {
                if (callerWillRestart) {
                    willRestart = false;
                    needRestart = true;
                } else {
                    willRestart = true;
                    needRestart = false;
                }
            } else {
                willRestart = false;
                needRestart = false;
            }
            app.killLocked(reason, reasonCode, subReason, true, async);
            this.mService.handleAppDiedLocked(app, pid, willRestart, allowRestart, false);
            if (willRestart) {
                removeLruProcessLocked(app);
                this.mService.addAppLocked(app.info, null, false, null, 0);
            }
        } else {
            this.mRemovedProcesses.add(app);
            needRestart = false;
        }
        this.mProcListWrapper.getExtImpl().hookHandleProcessKilled(needRestart, callerWillRestart, app, processId, reason);
        return needRestart;
    }

    void addProcessNameLocked(com.android.server.am.ProcessRecord proc) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.am.ProcessRecord old = removeProcessNameLocked(proc.processName, proc.uid);
                if (old == proc && proc.isPersistent()) {
                    android.util.Slog.w("ActivityManager", "Re-adding persistent process " + proc);
                    proc.resetCrashingOnRestart();
                } else if (old != null) {
                    if (old.isKilled()) {
                        android.util.Slog.w("ActivityManager", "Existing proc " + old + " was killed " + (android.os.SystemClock.uptimeMillis() - old.getKillTime()) + "ms ago when adding " + proc);
                    } else {
                        android.util.Slog.wtf("ActivityManager", "Already have existing proc " + old + " when adding " + proc);
                    }
                }
                com.android.server.am.UidRecord uidRec = this.mActiveUids.get(proc.uid);
                if (uidRec == null) {
                    uidRec = new com.android.server.am.UidRecord(proc.uid, this.mService);
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                        android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "Creating new process uid: " + uidRec);
                    }
                    if (java.util.Arrays.binarySearch(this.mService.mDeviceIdleTempAllowlist, android.os.UserHandle.getAppId(proc.uid)) >= 0 || this.mService.mPendingTempAllowlist.indexOfKey(proc.uid) >= 0) {
                        uidRec.setCurAllowListed(true);
                        uidRec.setSetAllowListed(true);
                    }
                    uidRec.updateHasInternetPermission();
                    this.mActiveUids.put(proc.uid, uidRec);
                    com.android.server.am.EventLogTags.writeAmUidRunning(uidRec.getUid());
                    this.mService.noteUidProcessState(uidRec.getUid(), uidRec.getCurProcState(), uidRec.getCurCapability());
                }
                proc.setUidRecord(uidRec);
                uidRec.addProcess(proc);
                proc.setRenderThreadTid(0);
                this.mProcessNames.put(proc.processName, proc.uid, proc);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        if (proc.isolated) {
            this.mIsolatedProcesses.put(proc.uid, proc);
        }
        if (proc.isSdkSandbox) {
            java.util.ArrayList<com.android.server.am.ProcessRecord> sdkSandboxes = this.mSdkSandboxes.get(proc.uid);
            if (sdkSandboxes == null) {
                sdkSandboxes = new java.util.ArrayList<>();
            }
            sdkSandboxes.add(proc);
            this.mSdkSandboxes.put(android.os.Process.getAppUidForSdkSandboxUid(proc.uid), sdkSandboxes);
        }
    }

    private com.android.server.am.ProcessList.IsolatedUidRange getOrCreateIsolatedUidRangeLocked(android.content.pm.ApplicationInfo info, com.android.server.am.HostingRecord hostingRecord) {
        if (hostingRecord == null || !hostingRecord.usesAppZygote()) {
            return this.mGlobalIsolatedUids;
        }
        return this.mAppIsolatedUidRangeAllocator.getOrCreateIsolatedUidRangeLocked(info.processName, hostingRecord.getDefiningUid());
    }

    com.android.server.am.ProcessRecord getSharedIsolatedProcess(java.lang.String processName, int uid, java.lang.String packageName) {
        int size = this.mIsolatedProcesses.size();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ProcessRecord app = this.mIsolatedProcesses.valueAt(i);
            if (app.info.uid == uid && app.info.packageName.equals(packageName) && app.processName.equals(processName)) {
                return app;
            }
        }
        return null;
    }

    java.util.List<java.lang.Integer> getIsolatedProcessesLocked(int uid) {
        java.util.List<java.lang.Integer> ret = null;
        int size = this.mIsolatedProcesses.size();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ProcessRecord app = this.mIsolatedProcesses.valueAt(i);
            if (app.info.uid == uid) {
                if (ret == null) {
                    ret = new java.util.ArrayList<>();
                }
                ret.add(java.lang.Integer.valueOf(app.getPid()));
            }
        }
        return ret;
    }

    java.util.List<com.android.server.am.ProcessRecord> getSdkSandboxProcessesForAppLocked(int uid) {
        return this.mSdkSandboxes.get(uid);
    }

    com.android.server.am.ProcessRecord newProcessRecordLocked(android.content.pm.ApplicationInfo info, java.lang.String customProcess, boolean isolated, int isolatedUid, boolean isSdkSandbox, int sdkSandboxUid, java.lang.String sdkSandboxClientAppPackage, com.android.server.am.HostingRecord hostingRecord) {
        int uid;
        int stoppedState;
        int uid2;
        int uid3;
        java.lang.String proc = customProcess != null ? customProcess : info.processName;
        int userId = android.os.UserHandle.getUserId(info.uid);
        int uid4 = info.uid;
        com.android.server.am.ProcessRecord record = this.mProcListWrapper.getExtImpl().replaceProcessRecordAtNewProcessRecord(info, this.mActiveUids, uid4, proc, hostingRecord);
        if (record != null) {
            return record;
        }
        if (isSdkSandbox) {
            uid4 = sdkSandboxUid;
        }
        if (android.os.Process.isSdkSandboxUid(uid4) && (!isSdkSandbox || sdkSandboxClientAppPackage == null)) {
            android.util.Slog.e("ActivityManager", "Abort creating new sandbox process as required parameters are missing.");
            return null;
        }
        if (isolated) {
            if (isolatedUid == 0) {
                com.android.server.am.ProcessList.IsolatedUidRange uidRange = getOrCreateIsolatedUidRangeLocked(info, hostingRecord);
                if (uidRange == null || (uid3 = uidRange.allocateIsolatedUidLocked(userId)) == -1) {
                    return null;
                }
                uid2 = uid3;
            } else {
                uid2 = isolatedUid;
            }
            this.mAppExitInfoTracker.mIsolatedUidRecords.addIsolatedUid(uid2, info.uid);
            this.mService.getPackageManagerInternal().addIsolatedUid(uid2, info.uid);
            this.mService.mBatteryStatsService.addIsolatedUid(uid2, info.uid);
            this.mProcListWrapper.getExtImpl().hookAddIsolatedUid(uid2, info.uid, info.packageName);
            uid = uid2;
        } else {
            uid = uid4;
        }
        com.android.server.am.ProcessRecord r = new com.android.server.am.ProcessRecord(this.mService, info, proc, uid, sdkSandboxClientAppPackage, hostingRecord.getDefiningUid(), hostingRecord.getDefiningProcessName());
        com.android.server.am.ProcessStateRecord state = r.mState;
        boolean wasStopped = (info.flags & 2097152) != 0;
        if (wasStopped) {
            if (hostingRecord.isTypeActivity()) {
                boolean wasPackageEverLaunched = this.mService.wasPackageEverLaunched(r.getApplicationInfo().packageName, r.userId);
                if (wasPackageEverLaunched) {
                    stoppedState = 2;
                } else {
                    stoppedState = 1;
                }
                r.getWindowProcessController().setStoppedState(stoppedState);
            } else {
                r.setWasForceStopped(true);
            }
        }
        if (!isolated && !isSdkSandbox && userId == 0 && (info.flags & 9) == 9 && android.text.TextUtils.equals(proc, info.processName)) {
            state.setCurrentSchedulingGroup(2);
            state.setSetSchedGroup(2);
            r.setPersistent(true);
            state.setMaxAdj(PERSISTENT_PROC_ADJ);
        }
        if (isolated && isolatedUid != 0) {
            state.setMaxAdj(PERSISTENT_SERVICE_ADJ);
        }
        addProcessNameLocked(r);
        return r;
    }

    com.android.server.am.ProcessRecord removeProcessNameLocked(java.lang.String name, int uid) {
        return removeProcessNameLocked(name, uid, null);
    }

    com.android.server.am.ProcessRecord removeProcessNameLocked(java.lang.String name, int uid, com.android.server.am.ProcessRecord expecting) {
        int appUid;
        java.util.ArrayList<com.android.server.am.ProcessRecord> sdkSandboxesForUid;
        com.android.server.am.UidRecord uidRecord;
        com.android.server.am.ProcessRecord old = (com.android.server.am.ProcessRecord) this.mProcessNames.get(name, uid);
        com.android.server.am.ProcessRecord record = expecting != null ? expecting : old;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            if (expecting == null || old == expecting) {
                try {
                    this.mProcessNames.m1524remove(name, uid);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            if (record != null && (uidRecord = record.getUidRecord()) != null) {
                uidRecord.removeProcess(record);
                if (uidRecord.getNumOfProcs() == 0) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                        android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "No more processes in " + uidRecord);
                    }
                    this.mService.enqueueUidChangeLocked(uidRecord, -1, -2147483647);
                    com.android.server.am.EventLogTags.writeAmUidStopped(uid);
                    this.mActiveUids.remove(uid);
                    this.mService.mFgsStartTempAllowList.removeUid(record.info.uid);
                    this.mService.noteUidProcessState(uid, 20, 0);
                }
                record.setUidRecord(null);
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        this.mIsolatedProcesses.remove(uid);
        this.mGlobalIsolatedUids.freeIsolatedUidLocked(uid);
        if (record != null && record.appZygote) {
            removeProcessFromAppZygoteLocked(record);
        }
        if (record != null && record.isSdkSandbox && (sdkSandboxesForUid = this.mSdkSandboxes.get((appUid = android.os.Process.getAppUidForSdkSandboxUid(uid)))) != null) {
            sdkSandboxesForUid.remove(record);
            if (sdkSandboxesForUid.size() == 0) {
                this.mSdkSandboxes.remove(appUid);
            }
        }
        this.mAppsInBackgroundRestricted.remove(record);
        return old;
    }

    void updateCoreSettingsLOSP(android.os.Bundle settings) {
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord processRecord = this.mLruProcesses.get(i);
            android.app.IApplicationThread thread = processRecord.getThread();
            if (thread != null) {
                try {
                    thread.setCoreSettings(settings);
                } catch (android.os.RemoteException e) {
                }
            }
        }
    }

    void killAllBackgroundProcessesExceptLSP(int minTargetSdk, int maxProcState) {
        java.util.ArrayList<com.android.server.am.ProcessRecord> procs = new java.util.ArrayList<>();
        int NP = this.mProcessNames.getMap().size();
        for (int ip = 0; ip < NP; ip++) {
            android.util.SparseArray<com.android.server.am.ProcessRecord> apps = (android.util.SparseArray) this.mProcessNames.getMap().valueAt(ip);
            int NA = apps.size();
            for (int ia = 0; ia < NA; ia++) {
                com.android.server.am.ProcessRecord app = apps.valueAt(ia);
                if (app.isRemoved() || ((minTargetSdk < 0 || app.info.targetSdkVersion < minTargetSdk) && (maxProcState < 0 || app.mState.getSetProcState() > maxProcState))) {
                    procs.add(app);
                }
            }
        }
        int N = procs.size();
        for (int i = 0; i < N; i++) {
            removeProcessLocked(procs.get(i), false, true, 13, 10, "kill all background except");
        }
    }

    void updateAllTimePrefsLOSP(int timePref) {
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = this.mLruProcesses.get(i);
            android.app.IApplicationThread thread = r.getThread();
            if (thread != null) {
                try {
                    thread.updateTimePrefs(timePref);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w("ActivityManager", "Failed to update preferences for: " + r.info.processName);
                }
            }
        }
    }

    void setAllHttpProxy() {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
                    com.android.server.am.ProcessRecord r = this.mLruProcesses.get(i);
                    android.app.IApplicationThread thread = r.getThread();
                    if (r.getPid() != com.android.server.am.ActivityManagerService.MY_PID && thread != null && !r.isolated) {
                        try {
                            thread.updateHttpProxy();
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.w("ActivityManager", "Failed to update http proxy for: " + r.info.processName);
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        android.app.ActivityThread.updateHttpProxy(this.mService.mContext);
    }

    void clearAllDnsCacheLOSP() {
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = this.mLruProcesses.get(i);
            android.app.IApplicationThread thread = r.getThread();
            if (thread != null) {
                try {
                    thread.clearDnsCache();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w("ActivityManager", "Failed to clear dns cache for: " + r.info.processName);
                }
            }
        }
    }

    void handleAllTrustStorageUpdateLOSP() {
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = this.mLruProcesses.get(i);
            android.app.IApplicationThread thread = r.getThread();
            if (thread != null) {
                try {
                    thread.handleTrustStorageUpdate();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w("ActivityManager", "Failed to handle trust storage update for: " + r.info.processName);
                }
            }
        }
    }

    private int updateLruProcessInternalLSP(com.android.server.am.ProcessRecord app, long now, int index, int lruSeq, java.lang.String what, java.lang.Object obj, com.android.server.am.ProcessRecord srcApp) {
        app.setLastActivityTime(now);
        if (app.hasActivitiesOrRecentTasks()) {
            return index;
        }
        int lrui = this.mLruProcesses.lastIndexOf(app);
        if (lrui < 0) {
            android.util.Slog.wtf("ActivityManager", "Adding dependent process " + app + " not on LRU list: " + what + " " + obj + " from " + srcApp);
            return index;
        }
        if (lrui >= index) {
            return index;
        }
        if (lrui >= this.mLruProcessActivityStart && index < this.mLruProcessActivityStart) {
            return index;
        }
        this.mLruProcesses.remove(lrui);
        if (index > 0) {
            index--;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
            android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Moving dep from " + lrui + " to " + index + " in LRU list: " + app);
        }
        this.mLruProcesses.add(index, app);
        app.setLruSeq(lruSeq);
        return index;
    }

    /* JADX WARN: Code restructure failed: missing block: B:85:0x0274, code lost:
    
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU == false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0276, code lost:
    
        android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Already found a different group: connGroup=" + r12 + " group=" + r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x029a, code lost:
    
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU == false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x029c, code lost:
    
        android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Already found a different activity: connUid=" + r11 + " uid=" + r3.info.uid);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void updateClientActivitiesOrderingLSP(com.android.server.am.ProcessRecord r22, int r23, int r24, int r25) {
        /*
            Method dump skipped, instruction units count: 980
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ProcessList.updateClientActivitiesOrderingLSP(com.android.server.am.ProcessRecord, int, int, int):void");
    }

    void updateLruProcessLocked(com.android.server.am.ProcessRecord app, boolean activityChange, com.android.server.am.ProcessRecord client) {
        com.android.server.am.ProcessServiceRecord psr = app.mServices;
        boolean hasActivity = app.hasActivitiesOrRecentTasks() || psr.hasClientActivities() || psr.isTreatedLikeActivity();
        if (!activityChange && hasActivity) {
            return;
        }
        if (app.getPid() == 0 && !app.isPendingStart()) {
            return;
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                updateLruProcessLSP(app, client, hasActivity, false);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    private void updateLruProcessLSP(com.android.server.am.ProcessRecord app, com.android.server.am.ProcessRecord client, boolean hasActivity, boolean hasService) {
        int nextIndex;
        com.android.server.am.ProcessProviderRecord ppr;
        int j;
        int lrui;
        this.mLruSeq++;
        long now = android.os.SystemClock.uptimeMillis();
        com.android.server.am.ProcessServiceRecord psr = app.mServices;
        app.setLastActivityTime(now);
        if (hasActivity) {
            int N = this.mLruProcesses.size();
            if (N > 0 && this.mLruProcesses.get(N - 1) == app) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                    android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Not moving, already top activity: " + app);
                    return;
                }
                return;
            }
        } else if (this.mLruProcessServiceStart > 0 && this.mLruProcesses.get(this.mLruProcessServiceStart - 1) == app) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Not moving, already top other: " + app);
                return;
            }
            return;
        }
        int lrui2 = this.mLruProcesses.lastIndexOf(app);
        if (this.mProcListWrapper.getExtImpl().returnSkipForLru(lrui2, app)) {
            return;
        }
        if (app.isPersistent() && lrui2 >= 0) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Not moving, persistent: " + app);
                return;
            }
            return;
        }
        if (lrui2 >= 0) {
            if (lrui2 < this.mLruProcessActivityStart) {
                this.mLruProcessActivityStart--;
            }
            if (lrui2 < this.mLruProcessServiceStart) {
                this.mLruProcessServiceStart--;
            }
            this.mLruProcesses.remove(lrui2);
        }
        int nextActivityIndex = -1;
        if (hasActivity) {
            int N2 = this.mLruProcesses.size();
            nextIndex = this.mLruProcessServiceStart;
            if (!app.hasActivitiesOrRecentTasks() && !psr.isTreatedLikeActivity() && this.mLruProcessActivityStart < N2 - 1) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                    android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Adding to second-top of LRU activity list: " + app + " group=" + psr.getConnectionGroup() + " importance=" + psr.getConnectionImportance());
                }
                int pos = N2 - 1;
                while (pos > this.mLruProcessActivityStart) {
                    com.android.server.am.ProcessRecord posproc = this.mLruProcesses.get(pos);
                    if (posproc.info.uid == app.info.uid) {
                        break;
                    } else {
                        pos--;
                    }
                }
                this.mLruProcesses.add(pos, app);
                int endIndex = pos - 1;
                if (endIndex < this.mLruProcessActivityStart) {
                    endIndex = this.mLruProcessActivityStart;
                }
                nextActivityIndex = endIndex;
                updateClientActivitiesOrderingLSP(app, pos, this.mLruProcessActivityStart, endIndex);
            } else {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                    android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Adding to top of LRU activity list: " + app);
                }
                this.mLruProcesses.add(app);
                nextActivityIndex = this.mLruProcesses.size() - 1;
            }
        } else if (hasService) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Adding to top of LRU service list: " + app);
            }
            this.mLruProcesses.add(this.mLruProcessActivityStart, app);
            nextIndex = this.mLruProcessServiceStart;
            this.mLruProcessActivityStart++;
        } else {
            int index = this.mLruProcessServiceStart;
            if (client != null) {
                int clientIndex = this.mLruProcesses.lastIndexOf(client);
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU && clientIndex < 0) {
                    android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Unknown client " + client + " when updating " + app);
                }
                if (clientIndex <= lrui2) {
                    clientIndex = lrui2;
                }
                if (clientIndex >= 0 && index > clientIndex) {
                    index = clientIndex;
                }
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Adding at " + index + " of LRU list: " + app);
            }
            this.mLruProcesses.add(index, app);
            nextIndex = index - 1;
            this.mLruProcessActivityStart++;
            this.mLruProcessServiceStart++;
            if (index > 1) {
                updateClientActivitiesOrderingLSP(app, this.mLruProcessServiceStart - 1, 0, index - 1);
            }
        }
        app.setLruSeq(this.mLruSeq);
        int nextActivityIndex2 = nextActivityIndex;
        int j2 = psr.numberOfConnections() - 1;
        int nextIndex2 = nextIndex;
        while (j2 >= 0) {
            com.android.server.am.ConnectionRecord cr = psr.getConnectionAt(j2);
            if (cr.binding == null || cr.serviceDead || cr.binding.service == null || cr.binding.service.app == null) {
                j = j2;
                lrui = lrui2;
            } else if (cr.binding.service.app.getLruSeq() == this.mLruSeq) {
                j = j2;
                lrui = lrui2;
            } else if (!cr.notHasFlag(1073742128)) {
                j = j2;
                lrui = lrui2;
            } else if (cr.binding.service.app.isPersistent()) {
                j = j2;
                lrui = lrui2;
            } else if (cr.binding.service.app.mServices.hasClientActivities()) {
                if (nextActivityIndex2 >= 0) {
                    j = j2;
                    lrui = lrui2;
                    nextActivityIndex2 = updateLruProcessInternalLSP(cr.binding.service.app, now, nextActivityIndex2, this.mLruSeq, "service connection", cr, app);
                } else {
                    j = j2;
                    lrui = lrui2;
                }
            } else {
                j = j2;
                lrui = lrui2;
                nextIndex2 = updateLruProcessInternalLSP(cr.binding.service.app, now, nextIndex2, this.mLruSeq, "service connection", cr, app);
            }
            j2 = j - 1;
            lrui2 = lrui;
        }
        com.android.server.am.ProcessProviderRecord ppr2 = app.mProviders;
        int j3 = ppr2.numberOfProviderConnections() - 1;
        while (j3 >= 0) {
            com.android.server.am.ContentProviderRecord cpr = ppr2.getProviderConnectionAt(j3).provider;
            if (cpr.proc == null || cpr.proc.getLruSeq() == this.mLruSeq || cpr.proc.isPersistent()) {
                ppr = ppr2;
            } else {
                ppr = ppr2;
                nextIndex2 = updateLruProcessInternalLSP(cpr.proc, now, nextIndex2, this.mLruSeq, "provider reference", cpr, app);
            }
            j3--;
            ppr2 = ppr;
        }
    }

    com.android.server.am.ProcessRecord getLRURecordForAppLOSP(android.app.IApplicationThread thread) {
        if (thread == null) {
            return null;
        }
        return getLRURecordForAppLOSP(thread.asBinder());
    }

    com.android.server.am.ProcessRecord getLRURecordForAppLOSP(android.os.IBinder threadBinder) {
        if (threadBinder == null) {
            return null;
        }
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord rec = this.mLruProcesses.get(i);
            android.app.IApplicationThread t = rec.getThread();
            if (t != null && t.asBinder() == threadBinder) {
                return rec;
            }
        }
        return null;
    }

    boolean haveBackgroundProcessLOSP() {
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord rec = this.mLruProcesses.get(i);
            if (rec.getThread() != null && rec.mState.getSetProcState() >= 16) {
                return true;
            }
        }
        return false;
    }

    private static int procStateToImportance(int procState, int memAdj, android.app.ActivityManager.RunningAppProcessInfo currApp, int clientTargetSdk) {
        int imp = android.app.ActivityManager.RunningAppProcessInfo.procStateToImportanceForTargetSdk(procState, clientTargetSdk);
        if (imp == 400) {
            currApp.lru = memAdj;
        } else {
            currApp.lru = 0;
        }
        return imp;
    }

    void fillInProcMemInfoLOSP(com.android.server.am.ProcessRecord app, android.app.ActivityManager.RunningAppProcessInfo outInfo, int clientTargetSdk) {
        outInfo.pid = app.getPid();
        outInfo.uid = app.info.uid;
        if (app.getWindowProcessController().isHeavyWeightProcess()) {
            outInfo.flags |= 1;
        }
        if (app.isPersistent()) {
            outInfo.flags |= 2;
        }
        if (app.hasActivities()) {
            outInfo.flags |= 4;
        }
        outInfo.lastTrimLevel = app.mProfile.getTrimMemoryLevel();
        com.android.server.am.ProcessStateRecord state = app.mState;
        int adj = state.getCurAdj();
        int procState = state.getCurProcState();
        outInfo.importance = procStateToImportance(procState, adj, outInfo, clientTargetSdk);
        outInfo.importanceReasonCode = state.getAdjTypeCode();
        outInfo.processState = procState;
        outInfo.isFocused = app == this.mService.getTopApp();
        outInfo.lastActivityTime = app.getLastActivityTime();
    }

    java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcessesLOSP(boolean allUsers, int userId, boolean allUids, int callingUid, int clientTargetSdk) {
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> runList = null;
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord app = this.mLruProcesses.get(i);
            com.android.server.am.ProcessStateRecord state = app.mState;
            com.android.server.am.ProcessErrorStateRecord errState = app.mErrorState;
            if ((allUsers || app.userId == userId) && ((allUids || app.uid == callingUid) && app.getThread() != null && !errState.isCrashing() && !errState.isNotResponding())) {
                android.app.ActivityManager.RunningAppProcessInfo currApp = new android.app.ActivityManager.RunningAppProcessInfo(app.processName, app.getPid(), app.getPackageList());
                if (app.getPkgDeps() != null) {
                    int size = app.getPkgDeps().size();
                    currApp.pkgDeps = (java.lang.String[]) app.getPkgDeps().toArray(new java.lang.String[size]);
                }
                fillInProcMemInfoLOSP(app, currApp, clientTargetSdk);
                if (state.getAdjSource() instanceof com.android.server.am.ProcessRecord) {
                    currApp.importanceReasonPid = ((com.android.server.am.ProcessRecord) state.getAdjSource()).getPid();
                    currApp.importanceReasonImportance = android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(state.getAdjSourceProcState());
                } else if (state.getAdjSource() instanceof com.android.server.wm.ActivityServiceConnectionsHolder) {
                    com.android.server.wm.ActivityServiceConnectionsHolder r = (com.android.server.wm.ActivityServiceConnectionsHolder) state.getAdjSource();
                    int pid = r.getActivityPid();
                    if (pid != -1) {
                        currApp.importanceReasonPid = pid;
                    }
                }
                if (state.getAdjTarget() instanceof android.content.ComponentName) {
                    currApp.importanceReasonComponent = (android.content.ComponentName) state.getAdjTarget();
                }
                if (runList == null) {
                    runList = new java.util.ArrayList<>();
                }
                runList.add(currApp);
            }
        }
        return runList;
    }

    int getLruSizeLOSP() {
        return this.mLruProcesses.size();
    }

    java.util.ArrayList<com.android.server.am.ProcessRecord> getLruProcessesLOSP() {
        return this.mLruProcesses;
    }

    java.util.ArrayList<com.android.server.am.ProcessRecord> getLruProcessesLSP() {
        return this.mLruProcesses;
    }

    void setLruProcessServiceStartLSP(int pos) {
        this.mLruProcessServiceStart = pos;
    }

    int getLruProcessServiceStartLOSP() {
        return this.mLruProcessServiceStart;
    }

    void forEachLruProcessesLOSP(boolean iterateForward, java.util.function.Consumer<com.android.server.am.ProcessRecord> callback) {
        if (iterateForward) {
            int size = this.mLruProcesses.size();
            for (int i = 0; i < size; i++) {
                callback.accept(this.mLruProcesses.get(i));
            }
            return;
        }
        for (int i2 = this.mLruProcesses.size() - 1; i2 >= 0; i2--) {
            callback.accept(this.mLruProcesses.get(i2));
        }
    }

    <R> R searchEachLruProcessesLOSP(boolean iterateForward, java.util.function.Function<com.android.server.am.ProcessRecord, R> callback) {
        if (iterateForward) {
            int size = this.mLruProcesses.size();
            for (int i = 0; i < size; i++) {
                R r = callback.apply(this.mLruProcesses.get(i));
                if (r != null) {
                    return r;
                }
            }
            return null;
        }
        for (int i2 = this.mLruProcesses.size() - 1; i2 >= 0; i2--) {
            R r2 = callback.apply(this.mLruProcesses.get(i2));
            if (r2 != null) {
                return r2;
            }
        }
        return null;
    }

    boolean isInLruListLOSP(com.android.server.am.ProcessRecord app) {
        return this.mLruProcesses.contains(app);
    }

    int getLruSeqLOSP() {
        return this.mLruSeq;
    }

    com.android.server.am.ProcessList.MyProcessMap getProcessNamesLOSP() {
        return this.mProcessNames;
    }

    void dumpLruListHeaderLocked(java.io.PrintWriter pw) {
        pw.print("  Process LRU list (sorted by oom_adj, ");
        pw.print(this.mLruProcesses.size());
        pw.print(" total, non-act at ");
        pw.print(this.mLruProcesses.size() - this.mLruProcessActivityStart);
        pw.print(", non-svc at ");
        pw.print(this.mLruProcesses.size() - this.mLruProcessServiceStart);
        pw.println("):");
    }

    private void dumpLruEntryLocked(java.io.PrintWriter pw, int index, com.android.server.am.ProcessRecord proc, java.lang.String prefix) {
        pw.print(prefix);
        pw.print('#');
        if (index < 10) {
            pw.print(' ');
        }
        pw.print(index);
        pw.print(": ");
        pw.print(makeOomAdjString(proc.mState.getSetAdj(), false));
        pw.print(' ');
        pw.print(makeProcStateString(proc.mState.getCurProcState()));
        pw.print(' ');
        android.app.ActivityManager.printCapabilitiesSummary(pw, proc.mState.getCurCapability());
        pw.print(' ');
        pw.print(proc.toShortString());
        com.android.server.am.ProcessServiceRecord psr = proc.mServices;
        if (proc.hasActivitiesOrRecentTasks() || psr.hasClientActivities() || psr.isTreatedLikeActivity()) {
            pw.print(" act:");
            boolean printed = false;
            if (proc.hasActivities()) {
                pw.print(com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_CMD);
                printed = true;
            }
            if (proc.hasRecentTasks()) {
                if (printed) {
                    pw.print("|");
                }
                pw.print(com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_CMD);
                printed = true;
            }
            if (psr.hasClientActivities()) {
                if (printed) {
                    pw.print("|");
                }
                pw.print("client");
                printed = true;
            }
            if (psr.isTreatedLikeActivity()) {
                if (printed) {
                    pw.print("|");
                }
                pw.print("treated");
            }
        }
        pw.println();
    }

    boolean dumpLruLocked(java.io.PrintWriter pw, java.lang.String dumpPackage, java.lang.String prefix) {
        java.lang.String innerPrefix;
        int lruSize = this.mLruProcesses.size();
        if (prefix == null) {
            pw.println("ACTIVITY MANAGER LRU PROCESSES (dumpsys activity lru)");
            innerPrefix = "  ";
        } else {
            boolean haveAny = false;
            for (int i = lruSize - 1; i >= 0; i--) {
                com.android.server.am.ProcessRecord r = this.mLruProcesses.get(i);
                if (dumpPackage == null || r.getPkgList().containsKey(dumpPackage)) {
                    haveAny = true;
                    break;
                }
            }
            if (!haveAny) {
                return false;
            }
            pw.print(prefix);
            pw.println("Raw LRU list (dumpsys activity lru):");
            innerPrefix = prefix + "  ";
        }
        boolean first = true;
        int i2 = lruSize - 1;
        while (i2 >= this.mLruProcessActivityStart) {
            com.android.server.am.ProcessRecord r2 = this.mLruProcesses.get(i2);
            if (dumpPackage == null || r2.getPkgList().containsKey(dumpPackage)) {
                if (first) {
                    pw.print(innerPrefix);
                    pw.println("Activities:");
                    first = false;
                }
                dumpLruEntryLocked(pw, i2, r2, innerPrefix);
            }
            i2--;
        }
        boolean first2 = true;
        while (i2 >= this.mLruProcessServiceStart) {
            com.android.server.am.ProcessRecord r3 = this.mLruProcesses.get(i2);
            if (dumpPackage == null || r3.getPkgList().containsKey(dumpPackage)) {
                if (first2) {
                    pw.print(innerPrefix);
                    pw.println("Services:");
                    first2 = false;
                }
                dumpLruEntryLocked(pw, i2, r3, innerPrefix);
            }
            i2--;
        }
        boolean first3 = true;
        while (i2 >= 0) {
            com.android.server.am.ProcessRecord r4 = this.mLruProcesses.get(i2);
            if (dumpPackage == null || r4.getPkgList().containsKey(dumpPackage)) {
                if (first3) {
                    pw.print(innerPrefix);
                    pw.println("Other:");
                    first3 = false;
                }
                dumpLruEntryLocked(pw, i2, r4, innerPrefix);
            }
            i2--;
        }
        return true;
    }

    void dumpProcessesLSP(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage, int dumpAppId) throws java.lang.Throwable {
        int numPers;
        boolean needSep = false;
        int numPers2 = 0;
        pw.println("ACTIVITY MANAGER RUNNING PROCESSES (dumpsys activity processes)");
        if (!dumpAll && dumpPackage == null) {
            numPers = 0;
        } else {
            int numOfNames = this.mProcessNames.getMap().size();
            for (int ip = 0; ip < numOfNames; ip++) {
                android.util.SparseArray<com.android.server.am.ProcessRecord> procs = (android.util.SparseArray) this.mProcessNames.getMap().valueAt(ip);
                int size = procs.size();
                for (int ia = 0; ia < size; ia++) {
                    com.android.server.am.ProcessRecord r = procs.valueAt(ia);
                    if (dumpPackage == null || r.getPkgList().containsKey(dumpPackage)) {
                        if (!needSep) {
                            pw.println("  All known processes:");
                            needSep = true;
                        }
                        pw.print(r.isPersistent() ? "  *PERS*" : "  *APP*");
                        pw.print(" UID ");
                        pw.print(procs.keyAt(ia));
                        pw.print(" ");
                        pw.println(r);
                        r.dump(pw, "    ");
                        if (r.isPersistent()) {
                            numPers2++;
                        }
                    }
                }
            }
            numPers = numPers2;
        }
        if (this.mIsolatedProcesses.size() > 0) {
            boolean printed = false;
            int size2 = this.mIsolatedProcesses.size();
            for (int i = 0; i < size2; i++) {
                com.android.server.am.ProcessRecord r2 = this.mIsolatedProcesses.valueAt(i);
                if (dumpPackage == null || r2.getPkgList().containsKey(dumpPackage)) {
                    if (!printed) {
                        if (needSep) {
                            pw.println();
                        }
                        pw.println("  Isolated process list (sorted by uid):");
                        printed = true;
                        needSep = true;
                    }
                    pw.print("    Isolated #");
                    pw.print(i);
                    pw.print(": ");
                    pw.println(r2);
                }
            }
        }
        boolean needSep2 = this.mService.dumpActiveInstruments(pw, dumpPackage, needSep);
        if (dumpOomLocked(fd, pw, needSep2, args, opti, dumpAll, dumpPackage, false)) {
            needSep2 = true;
        }
        if (this.mActiveUids.size() > 0) {
            needSep2 |= this.mActiveUids.dump(pw, dumpPackage, dumpAppId, "UID states:", needSep2);
        }
        if (dumpAll) {
            needSep2 |= this.mService.mUidObserverController.dumpValidateUids(pw, dumpPackage, dumpAppId, "UID validation:", needSep2);
        }
        if (needSep2) {
            pw.println();
        }
        if (dumpLruLocked(pw, dumpPackage, "  ")) {
            needSep2 = true;
        }
        if (getLruSizeLOSP() > 0) {
            if (needSep2) {
                pw.println();
            }
            dumpLruListHeaderLocked(pw);
            dumpProcessOomList(pw, this.mService, this.mLruProcesses, "    ", "Proc", "PERS", false, dumpPackage);
            needSep2 = true;
        }
        this.mService.dumpOtherProcessesInfoLSP(fd, pw, dumpAll, dumpPackage, dumpAppId, numPers, needSep2);
    }

    void writeProcessesToProtoLSP(android.util.proto.ProtoOutputStream proto, java.lang.String dumpPackage) {
        int numOfNames = this.mProcessNames.getMap().size();
        int numPers = 0;
        for (int ip = 0; ip < numOfNames; ip++) {
            android.util.SparseArray<com.android.server.am.ProcessRecord> procs = (android.util.SparseArray) this.mProcessNames.getMap().valueAt(ip);
            int size = procs.size();
            for (int ia = 0; ia < size; ia++) {
                com.android.server.am.ProcessRecord r = procs.valueAt(ia);
                if (dumpPackage == null || r.getPkgList().containsKey(dumpPackage)) {
                    r.dumpDebug(proto, 2246267895809L, this.mLruProcesses.indexOf(r));
                    if (r.isPersistent()) {
                        numPers++;
                    }
                }
            }
        }
        int size2 = this.mIsolatedProcesses.size();
        for (int i = 0; i < size2; i++) {
            com.android.server.am.ProcessRecord r2 = this.mIsolatedProcesses.valueAt(i);
            if (dumpPackage == null || r2.getPkgList().containsKey(dumpPackage)) {
                r2.dumpDebug(proto, 2246267895810L, this.mLruProcesses.indexOf(r2));
            }
        }
        int dumpAppId = this.mService.getAppId(dumpPackage);
        this.mActiveUids.dumpProto(proto, dumpPackage, dumpAppId, 2246267895812L);
        if (getLruSizeLOSP() > 0) {
            long lruToken = proto.start(1146756268038L);
            int total = getLruSizeLOSP();
            proto.write(1120986464257L, total);
            proto.write(1120986464258L, total - this.mLruProcessActivityStart);
            proto.write(1120986464259L, total - this.mLruProcessServiceStart);
            writeProcessOomListToProto(proto, 2246267895812L, this.mService, this.mLruProcesses, true, dumpPackage);
            proto.end(lruToken);
        }
        this.mService.writeOtherProcessesInfoToProtoLSP(proto, dumpPackage, dumpAppId, numPers);
    }

    private static java.util.ArrayList<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>> sortProcessOomList(java.util.List<com.android.server.am.ProcessRecord> origList, java.lang.String dumpPackage) {
        java.util.ArrayList<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>> list = new java.util.ArrayList<>(origList.size());
        int size = origList.size();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ProcessRecord r = origList.get(i);
            if (dumpPackage == null || r.getPkgList().containsKey(dumpPackage)) {
                list.add(new android.util.Pair<>(origList.get(i), java.lang.Integer.valueOf(i)));
            }
        }
        java.util.Comparator<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>> comparator = new java.util.Comparator<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>>() { // from class: com.android.server.am.ProcessList.3
            @Override // java.util.Comparator
            public int compare(android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer> object1, android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer> object2) {
                int adj = ((com.android.server.am.ProcessRecord) object2.first).mState.getSetAdj() - ((com.android.server.am.ProcessRecord) object1.first).mState.getSetAdj();
                if (adj != 0) {
                    return adj;
                }
                int procState = ((com.android.server.am.ProcessRecord) object2.first).mState.getSetProcState() - ((com.android.server.am.ProcessRecord) object1.first).mState.getSetProcState();
                if (procState != 0) {
                    return procState;
                }
                int val = ((java.lang.Integer) object2.second).intValue() - ((java.lang.Integer) object1.second).intValue();
                if (val != 0) {
                    return val;
                }
                return 0;
            }
        };
        java.util.Collections.sort(list, comparator);
        return list;
    }

    private static boolean writeProcessOomListToProto(android.util.proto.ProtoOutputStream proto, long fieldId, com.android.server.am.ActivityManagerService service, java.util.List<com.android.server.am.ProcessRecord> origList, boolean inclDetails, java.lang.String dumpPackage) {
        java.util.ArrayList<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>> list;
        long curUptime;
        int i;
        java.util.ArrayList<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>> list2 = sortProcessOomList(origList, dumpPackage);
        if (list2.isEmpty()) {
            return false;
        }
        long curUptime2 = android.os.SystemClock.uptimeMillis();
        boolean z = true;
        int i2 = list2.size() - 1;
        while (i2 >= 0) {
            com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) list2.get(i2).first;
            com.android.server.am.ProcessStateRecord state = r.mState;
            com.android.server.am.ProcessServiceRecord psr = r.mServices;
            long token = proto.start(fieldId);
            java.lang.String oomAdj = makeOomAdjString(state.getSetAdj(), z);
            proto.write(1133871366145L, r.isPersistent());
            proto.write(1120986464258L, (origList.size() - 1) - ((java.lang.Integer) list2.get(i2).second).intValue());
            proto.write(1138166333443L, oomAdj);
            int schedGroup = -1;
            switch (state.getSetSchedGroup()) {
                case 0:
                    schedGroup = 0;
                    break;
                case 2:
                    schedGroup = 1;
                    break;
                case 3:
                    schedGroup = 2;
                    break;
                case 4:
                    schedGroup = 3;
                    break;
            }
            if (schedGroup != -1) {
                proto.write(1159641169924L, schedGroup);
            }
            if (state.hasForegroundActivities()) {
                proto.write(1133871366149L, true);
            } else if (psr.hasForegroundServices()) {
                proto.write(1133871366150L, true);
            }
            proto.write(1159641169927L, makeProcStateProtoEnum(state.getCurProcState()));
            proto.write(1120986464264L, r.mProfile.getTrimMemoryLevel());
            r.dumpDebug(proto, 1146756268041L);
            proto.write(1138166333450L, state.getAdjType());
            if (state.getAdjSource() != null || state.getAdjTarget() != null) {
                if (state.getAdjTarget() instanceof android.content.ComponentName) {
                    android.content.ComponentName cn = (android.content.ComponentName) state.getAdjTarget();
                    cn.dumpDebug(proto, 1146756268043L);
                } else if (state.getAdjTarget() != null) {
                    proto.write(1138166333452L, state.getAdjTarget().toString());
                }
                if (state.getAdjSource() instanceof com.android.server.am.ProcessRecord) {
                    com.android.server.am.ProcessRecord p = (com.android.server.am.ProcessRecord) state.getAdjSource();
                    p.dumpDebug(proto, 1146756268045L);
                } else if (state.getAdjSource() != null) {
                    proto.write(1138166333454L, state.getAdjSource().toString());
                }
            }
            if (inclDetails) {
                long detailToken = proto.start(1146756268047L);
                list = list2;
                proto.write(1120986464257L, state.getMaxAdj());
                proto.write(1120986464258L, state.getCurRawAdj());
                proto.write(1120986464259L, state.getSetRawAdj());
                proto.write(1120986464260L, state.getCurAdj());
                proto.write(1120986464261L, state.getSetAdj());
                proto.write(1159641169927L, makeProcStateProtoEnum(state.getCurProcState()));
                proto.write(1159641169928L, makeProcStateProtoEnum(state.getSetProcState()));
                proto.write(1138166333449L, android.util.DebugUtils.sizeValueToString(r.mProfile.getLastPss() * 1024, new java.lang.StringBuilder()));
                proto.write(1138166333450L, android.util.DebugUtils.sizeValueToString(r.mProfile.getLastSwapPss() * 1024, new java.lang.StringBuilder()));
                proto.write(1138166333451L, android.util.DebugUtils.sizeValueToString(r.mProfile.getLastCachedPss() * 1024, new java.lang.StringBuilder()));
                proto.write(1133871366156L, state.isCached());
                proto.write(1133871366157L, state.isEmpty());
                proto.write(1133871366158L, psr.hasAboveClient());
                if (state.getSetProcState() < 10) {
                    curUptime = curUptime2;
                    i = i2;
                } else {
                    long lastCpuTime = r.mProfile.mLastCpuTime.get();
                    long uptimeSince = curUptime2 - service.mLastPowerCheckUptime;
                    if (lastCpuTime == 0 || uptimeSince <= 0) {
                        curUptime = curUptime2;
                        i = i2;
                    } else {
                        curUptime = curUptime2;
                        long timeUsed = r.mProfile.mCurCpuTime.get() - lastCpuTime;
                        long cpuTimeToken = proto.start(1146756268047L);
                        proto.write(1112396529665L, uptimeSince);
                        proto.write(1112396529666L, timeUsed);
                        i = i2;
                        proto.write(1108101562371L, (timeUsed * 100.0d) / uptimeSince);
                        proto.end(cpuTimeToken);
                    }
                }
                proto.end(detailToken);
            } else {
                list = list2;
                curUptime = curUptime2;
                i = i2;
            }
            proto.end(token);
            i2 = i - 1;
            curUptime2 = curUptime;
            list2 = list;
            z = true;
        }
        return true;
    }

    private static boolean dumpProcessOomList(java.io.PrintWriter pw, com.android.server.am.ActivityManagerService service, java.util.List<com.android.server.am.ProcessRecord> origList, java.lang.String prefix, java.lang.String normalLabel, java.lang.String persistentLabel, boolean inclDetails, java.lang.String dumpPackage) {
        char schedGroup;
        char foreground;
        char c;
        com.android.server.am.ActivityManagerService activityManagerService = service;
        java.lang.String str = prefix;
        java.util.ArrayList<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>> list = sortProcessOomList(origList, dumpPackage);
        boolean z = false;
        if (list.isEmpty()) {
            return false;
        }
        long curUptime = android.os.SystemClock.uptimeMillis();
        long uptimeSince = curUptime - activityManagerService.mLastPowerCheckUptime;
        int i = list.size() - 1;
        while (i >= 0) {
            com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) list.get(i).first;
            com.android.server.am.ProcessStateRecord state = r.mState;
            com.android.server.am.ProcessServiceRecord psr = r.mServices;
            java.lang.String oomAdj = makeOomAdjString(state.getSetAdj(), z);
            switch (state.getSetSchedGroup()) {
                case 0:
                    schedGroup = 'b';
                    break;
                case 1:
                    schedGroup = 'R';
                    break;
                case 2:
                    schedGroup = 'F';
                    break;
                case 3:
                    schedGroup = 'T';
                    break;
                case 4:
                    schedGroup = 'B';
                    break;
                default:
                    schedGroup = '?';
                    break;
            }
            if (state.hasForegroundActivities()) {
                foreground = 'A';
            } else if (psr.hasForegroundServices()) {
                foreground = 'S';
            } else {
                foreground = ' ';
            }
            java.lang.String procState = makeProcStateString(state.getCurProcState());
            pw.print(str);
            long curUptime2 = curUptime;
            pw.print(r.isPersistent() ? persistentLabel : normalLabel);
            pw.print(" #");
            int num = (origList.size() - 1) - ((java.lang.Integer) list.get(i).second).intValue();
            java.util.ArrayList<android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer>> list2 = list;
            if (num < 10) {
                pw.print(' ');
            }
            pw.print(num);
            pw.print(": ");
            pw.print(oomAdj);
            pw.print(' ');
            pw.print(schedGroup);
            pw.print('/');
            pw.print(foreground);
            pw.print('/');
            pw.print(procState);
            pw.print(' ');
            android.app.ActivityManager.printCapabilitiesSummary(pw, state.getCurCapability());
            pw.print(' ');
            pw.print(" t:");
            if (r.mProfile.getTrimMemoryLevel() < 10) {
                c = ' ';
                pw.print(' ');
            } else {
                c = ' ';
            }
            pw.print(r.mProfile.getTrimMemoryLevel());
            pw.print(c);
            pw.print(r.toShortString());
            pw.print(" (");
            pw.print(state.getAdjType());
            pw.println(')');
            if (state.getAdjSource() != null || state.getAdjTarget() != null) {
                pw.print(str);
                pw.print("    ");
                if (state.getAdjTarget() instanceof android.content.ComponentName) {
                    pw.print(((android.content.ComponentName) state.getAdjTarget()).flattenToShortString());
                } else if (state.getAdjTarget() != null) {
                    pw.print(state.getAdjTarget().toString());
                } else {
                    pw.print("{null}");
                }
                pw.print("<=");
                if (state.getAdjSource() instanceof com.android.server.am.ProcessRecord) {
                    pw.print("Proc{");
                    pw.print(((com.android.server.am.ProcessRecord) state.getAdjSource()).toShortString());
                    pw.println("}");
                } else if (state.getAdjSource() != null) {
                    pw.println(state.getAdjSource().toString());
                } else {
                    pw.println("{null}");
                }
            }
            if (inclDetails) {
                pw.print(str);
                pw.print("    ");
                pw.print("oom: max=");
                pw.print(state.getMaxAdj());
                pw.print(" curRaw=");
                pw.print(state.getCurRawAdj());
                pw.print(" setRaw=");
                pw.print(state.getSetRawAdj());
                pw.print(" cur=");
                pw.print(state.getCurAdj());
                pw.print(" set=");
                pw.println(state.getSetAdj());
                pw.print(str);
                pw.print("    ");
                pw.print("state: cur=");
                pw.print(makeProcStateString(state.getCurProcState()));
                pw.print(" set=");
                pw.print(makeProcStateString(state.getSetProcState()));
                if (activityManagerService.mAppProfiler.isProfilingPss()) {
                    pw.print(" lastPss=");
                    android.util.DebugUtils.printSizeValue(pw, r.mProfile.getLastPss() * 1024);
                    pw.print(" lastSwapPss=");
                    android.util.DebugUtils.printSizeValue(pw, r.mProfile.getLastSwapPss() * 1024);
                    pw.print(" lastCachedPss=");
                    android.util.DebugUtils.printSizeValue(pw, r.mProfile.getLastCachedPss() * 1024);
                } else {
                    pw.print(" lastRss=");
                    android.util.DebugUtils.printSizeValue(pw, r.mProfile.getLastRss() * 1024);
                    pw.print(" lastCachedRss=");
                    android.util.DebugUtils.printSizeValue(pw, r.mProfile.getLastCachedRss() * 1024);
                }
                pw.println();
                pw.print(str);
                pw.print("    ");
                pw.print("cached=");
                pw.print(state.isCached());
                pw.print(" empty=");
                pw.print(state.isEmpty());
                pw.print(" hasAboveClient=");
                pw.println(psr.hasAboveClient());
                if (state.getSetProcState() >= 10) {
                    long lastCpuTime = r.mProfile.mLastCpuTime.get();
                    if (lastCpuTime != 0 && uptimeSince > 0) {
                        long timeUsed = r.mProfile.mCurCpuTime.get() - lastCpuTime;
                        pw.print(str);
                        pw.print("    ");
                        pw.print("run cpu over ");
                        android.util.TimeUtils.formatDuration(uptimeSince, pw);
                        pw.print(" used ");
                        android.util.TimeUtils.formatDuration(timeUsed, pw);
                        pw.print(" (");
                        pw.print((100 * timeUsed) / uptimeSince);
                        pw.println("%)");
                    }
                }
            }
            i--;
            activityManagerService = service;
            str = prefix;
            list = list2;
            curUptime = curUptime2;
            z = false;
        }
        return true;
    }

    private void printOomLevel(java.io.PrintWriter pw, java.lang.String name, int adj) {
        pw.print("    ");
        if (adj >= 0) {
            pw.print(' ');
            if (adj < 10) {
                pw.print(' ');
            }
        } else if (adj > -10) {
            pw.print(' ');
        }
        pw.print(adj);
        pw.print(": ");
        pw.print(name);
        pw.print(" (");
        pw.print(com.android.server.am.ActivityManagerService.stringifySize(getMemLevel(adj), 1024));
        pw.println(")");
    }

    boolean dumpOomLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, boolean needSep, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage, boolean inclGc) throws java.lang.Throwable {
        boolean needSep2;
        if (getLruSizeLOSP() <= 0) {
            needSep2 = needSep;
        } else {
            if (needSep) {
                pw.println();
            }
            pw.println("  OOM levels:");
            printOomLevel(pw, "SYSTEM_ADJ", SYSTEM_ADJ);
            printOomLevel(pw, "PERSISTENT_PROC_ADJ", PERSISTENT_PROC_ADJ);
            printOomLevel(pw, "PERSISTENT_SERVICE_ADJ", PERSISTENT_SERVICE_ADJ);
            printOomLevel(pw, "FOREGROUND_APP_ADJ", 0);
            printOomLevel(pw, "VISIBLE_APP_ADJ", 100);
            printOomLevel(pw, "PERCEPTIBLE_APP_ADJ", 200);
            printOomLevel(pw, "PERCEPTIBLE_MEDIUM_APP_ADJ", PERCEPTIBLE_MEDIUM_APP_ADJ);
            printOomLevel(pw, "PERCEPTIBLE_LOW_APP_ADJ", 250);
            printOomLevel(pw, "BACKUP_APP_ADJ", 300);
            printOomLevel(pw, "HEAVY_WEIGHT_APP_ADJ", 400);
            printOomLevel(pw, "SERVICE_ADJ", 500);
            printOomLevel(pw, "HOME_APP_ADJ", 600);
            printOomLevel(pw, "PREVIOUS_APP_ADJ", PREVIOUS_APP_ADJ);
            printOomLevel(pw, "SERVICE_B_ADJ", 800);
            printOomLevel(pw, "CACHED_APP_MIN_ADJ", 900);
            printOomLevel(pw, "CACHED_APP_MAX_ADJ", 999);
            if (1 != 0) {
                pw.println();
            }
            pw.print("  Process OOM control (");
            pw.print(getLruSizeLOSP());
            pw.print(" total, non-act at ");
            pw.print(getLruSizeLOSP() - this.mLruProcessActivityStart);
            pw.print(", non-svc at ");
            pw.print(getLruSizeLOSP() - this.mLruProcessServiceStart);
            pw.println("):");
            dumpProcessOomList(pw, this.mService, this.mLruProcesses, "    ", "Proc", "PERS", true, dumpPackage);
            needSep2 = true;
        }
        synchronized (this.mService.mAppProfiler.mProfilerLock) {
            try {
                try {
                    this.mService.mAppProfiler.dumpProcessesToGc(pw, needSep2, dumpPackage);
                    pw.println();
                    this.mService.mAtmInternal.dumpForOom(pw);
                    return true;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void registerProcessObserver(android.app.IProcessObserver observer) {
        this.mProcessObservers.register(observer);
    }

    void unregisterProcessObserver(android.app.IProcessObserver observer) {
        this.mProcessObservers.unregister(observer);
    }

    void dispatchProcessesChanged() {
        int numOfChanges;
        synchronized (this.mProcessChangeLock) {
            numOfChanges = this.mPendingProcessChanges.size();
            if (this.mActiveProcessChanges.length < numOfChanges) {
                this.mActiveProcessChanges = new com.android.server.am.ActivityManagerService.ProcessChangeItem[numOfChanges];
            }
            this.mPendingProcessChanges.toArray(this.mActiveProcessChanges);
            this.mPendingProcessChanges.clear();
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                android.util.Slog.i(TAG_PROCESS_OBSERVERS, "*** Delivering " + numOfChanges + " process changes");
            }
        }
        int i = this.mProcessObservers.beginBroadcast();
        while (i > 0) {
            i--;
            android.app.IProcessObserver observer = this.mProcessObservers.getBroadcastItem(i);
            if (observer != null) {
                for (int j = 0; j < numOfChanges; j++) {
                    try {
                        com.android.server.am.ActivityManagerService.ProcessChangeItem item = this.mActiveProcessChanges[j];
                        if ((item.changes & 1) != 0) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                                android.util.Slog.i(TAG_PROCESS_OBSERVERS, "ACTIVITIES CHANGED pid=" + item.pid + " uid=" + item.uid + ": " + item.foregroundActivities);
                            }
                            observer.onForegroundActivitiesChanged(item.pid, item.uid, item.foregroundActivities);
                        }
                        if ((item.changes & 2) != 0) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                                android.util.Slog.i(TAG_PROCESS_OBSERVERS, "FOREGROUND SERVICES CHANGED pid=" + item.pid + " uid=" + item.uid + ": " + item.foregroundServiceTypes);
                            }
                            observer.onForegroundServicesChanged(item.pid, item.uid, item.foregroundServiceTypes);
                        }
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        }
        this.mProcessObservers.finishBroadcast();
        synchronized (this.mProcessChangeLock) {
            for (int j2 = 0; j2 < numOfChanges; j2++) {
                this.mAvailProcessChanges.add(this.mActiveProcessChanges[j2]);
            }
        }
    }

    com.android.server.am.ActivityManagerService.ProcessChangeItem enqueueProcessChangeItemLocked(int pid, int uid) {
        com.android.server.am.ActivityManagerService.ProcessChangeItem item;
        synchronized (this.mProcessChangeLock) {
            int i = this.mPendingProcessChanges.size() - 1;
            item = null;
            while (true) {
                if (i < 0) {
                    break;
                }
                item = this.mPendingProcessChanges.get(i);
                if (item.pid == pid) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                        android.util.Slog.i(TAG_PROCESS_OBSERVERS, "Re-using existing item: " + item);
                    }
                } else {
                    i--;
                }
            }
            if (i < 0) {
                int num = this.mAvailProcessChanges.size();
                if (num > 0) {
                    item = this.mAvailProcessChanges.remove(num - 1);
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                        android.util.Slog.i(TAG_PROCESS_OBSERVERS, "Retrieving available item: " + item);
                    }
                } else {
                    item = new com.android.server.am.ActivityManagerService.ProcessChangeItem();
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                        android.util.Slog.i(TAG_PROCESS_OBSERVERS, "Allocating new item: " + item);
                    }
                }
                item.changes = 0;
                item.pid = pid;
                item.uid = uid;
                if (this.mPendingProcessChanges.size() == 0) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESS_OBSERVERS) {
                        android.util.Slog.i(TAG_PROCESS_OBSERVERS, "*** Enqueueing dispatch processes changed!");
                    }
                    this.mService.mUiHandler.obtainMessage(31).sendToTarget();
                }
                this.mPendingProcessChanges.add(item);
            }
        }
        return item;
    }

    void scheduleDispatchProcessDiedLocked(int pid, int uid) {
        synchronized (this.mProcessChangeLock) {
            for (int i = this.mPendingProcessChanges.size() - 1; i >= 0; i--) {
                com.android.server.am.ActivityManagerService.ProcessChangeItem item = this.mPendingProcessChanges.get(i);
                if (pid > 0 && item.pid == pid) {
                    this.mPendingProcessChanges.remove(i);
                    this.mAvailProcessChanges.add(item);
                }
            }
            this.mService.mUiHandler.obtainMessage(32, pid, uid, null).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: dispatchProcessStarted, reason: merged with bridge method [inline-methods] */
    public void lambda$handleProcessStartedLocked$2(com.android.server.am.ProcessRecord app, int pid) {
    }

    void dispatchProcessDied(int pid, int uid) {
        int i = this.mProcessObservers.beginBroadcast();
        while (i > 0) {
            i--;
            android.app.IProcessObserver observer = this.mProcessObservers.getBroadcastItem(i);
            if (observer != null) {
                try {
                    observer.onProcessDied(pid, uid);
                } catch (android.os.RemoteException e) {
                }
            }
        }
        this.mProcessObservers.finishBroadcast();
    }

    java.util.ArrayList<com.android.server.am.ProcessRecord> collectProcessesLOSP(int start, boolean allPkgs, java.lang.String[] args) {
        if (args != null && args.length > start && args[start].charAt(0) != '-') {
            java.util.ArrayList<com.android.server.am.ProcessRecord> procs = new java.util.ArrayList<>();
            int pid = -1;
            try {
                pid = java.lang.Integer.parseInt(args[start]);
            } catch (java.lang.NumberFormatException e) {
            }
            for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
                com.android.server.am.ProcessRecord proc = this.mLruProcesses.get(i);
                if (proc.getPid() > 0 && proc.getPid() == pid) {
                    procs.add(proc);
                } else if (allPkgs && proc.getPkgList() != null && proc.getPkgList().containsKey(args[start])) {
                    procs.add(proc);
                } else if (proc.processName.equals(args[start])) {
                    procs.add(proc);
                }
            }
            int i2 = procs.size();
            if (i2 <= 0) {
                return null;
            }
            return procs;
        }
        return new java.util.ArrayList<>(this.mLruProcesses);
    }

    void updateApplicationInfoLOSP(final java.util.List<java.lang.String> packagesToUpdate, int userId, final boolean updateFrameworkRes) {
        final android.util.ArrayMap<java.lang.String, android.content.pm.ApplicationInfo> applicationInfoByPackage = new android.util.ArrayMap<>();
        for (int i = packagesToUpdate.size() - 1; i >= 0; i--) {
            java.lang.String packageName = packagesToUpdate.get(i);
            android.content.pm.ApplicationInfo ai = this.mService.getPackageManagerInternal().getApplicationInfo(packageName, 1024L, 1000, userId);
            if (ai != null) {
                applicationInfoByPackage.put(packageName, ai);
            }
        }
        this.mService.mActivityTaskManager.updateActivityApplicationInfo(userId, applicationInfoByPackage);
        boolean fromSwitchUser = this.mProcListWrapper.getExtImpl().returnIsFromSwitchUser(packagesToUpdate);
        final java.util.ArrayList<com.android.server.wm.WindowProcessController> targetProcesses = new java.util.ArrayList<>();
        for (int i2 = this.mLruProcesses.size() - 1; i2 >= 0; i2--) {
            final com.android.server.am.ProcessRecord app = this.mLruProcesses.get(i2);
            if (app.getThread() != null && (userId == -1 || app.userId == userId)) {
                app.getPkgList().forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.am.ProcessList.lambda$updateApplicationInfoLOSP$4(updateFrameworkRes, packagesToUpdate, applicationInfoByPackage, app, targetProcesses, (java.lang.String) obj);
                    }
                });
            }
        }
        this.mService.mActivityTaskManager.updateAssetConfigurationForSwitchUser(targetProcesses, updateFrameworkRes, fromSwitchUser);
    }

    static /* synthetic */ void lambda$updateApplicationInfoLOSP$4(boolean updateFrameworkRes, java.util.List packagesToUpdate, android.util.ArrayMap applicationInfoByPackage, com.android.server.am.ProcessRecord app, java.util.ArrayList targetProcesses, java.lang.String packageName) {
        if (updateFrameworkRes || packagesToUpdate.contains(packageName)) {
            try {
                android.content.pm.ApplicationInfo ai = (android.content.pm.ApplicationInfo) applicationInfoByPackage.get(packageName);
                if (ai != null) {
                    if (ai.packageName.equals(app.info.packageName)) {
                        app.info = ai;
                        com.android.server.am.PlatformCompatCache.getInstance().onApplicationInfoChanged(ai);
                    }
                    app.getThread().scheduleApplicationInfoChanged(ai);
                    targetProcesses.add(app.getWindowProcessController());
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w("ActivityManager", java.lang.String.format("Failed to update %s ApplicationInfo for %s", packageName, app));
            }
        }
    }

    void sendPackageBroadcastLocked(int cmd, java.lang.String[] packages, int userId) {
        boolean foundProcess = false;
        for (int i = this.mLruProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = this.mLruProcesses.get(i);
            android.app.IApplicationThread thread = r.getThread();
            if (thread != null && (userId == -1 || r.userId == userId)) {
                try {
                    for (int index = packages.length - 1; index >= 0 && !foundProcess; index--) {
                        if (packages[index].equals(r.info.packageName)) {
                            foundProcess = true;
                        }
                    }
                    thread.dispatchPackageBroadcast(cmd, packages);
                } catch (android.os.RemoteException e) {
                }
            }
        }
        if (!foundProcess) {
            try {
                android.app.AppGlobals.getPackageManager().notifyPackagesReplacedReceived(packages);
            } catch (android.os.RemoteException e2) {
            }
        }
    }

    int getUidProcStateLOSP(int uid) {
        com.android.server.am.UidRecord uidRec = this.mActiveUids.get(uid);
        if (uidRec == null) {
            return 20;
        }
        return uidRec.getCurProcState();
    }

    int getUidProcessCapabilityLOSP(int uid) {
        com.android.server.am.UidRecord uidRec = this.mActiveUids.get(uid);
        if (uidRec == null) {
            return 0;
        }
        return uidRec.getCurCapability();
    }

    com.android.server.am.UidRecord getUidRecordLOSP(int uid) {
        return this.mActiveUids.get(uid);
    }

    void doStopUidForIdleUidsLocked() {
        int size = this.mActiveUids.size();
        for (int i = 0; i < size; i++) {
            int uid = this.mActiveUids.keyAt(i);
            if (!android.os.UserHandle.isCore(uid)) {
                com.android.server.am.UidRecord uidRec = this.mActiveUids.valueAt(i);
                if (uidRec.isIdle()) {
                    this.mService.doStopUidLocked(uidRec.getUid(), uidRec);
                }
            }
        }
    }

    int getBlockStateForUid(com.android.server.am.UidRecord uidRec) {
        boolean isAllowed = android.net.NetworkPolicyManager.isProcStateAllowedWhileIdleOrPowerSaveMode(uidRec.getCurProcState(), uidRec.getCurCapability()) || android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(uidRec.getCurProcState(), uidRec.getCurCapability());
        boolean wasAllowed = android.net.NetworkPolicyManager.isProcStateAllowedWhileIdleOrPowerSaveMode(uidRec.getSetProcState(), uidRec.getSetCapability()) || android.net.NetworkPolicyManager.isProcStateAllowedWhileOnRestrictBackground(uidRec.getSetProcState(), uidRec.getSetCapability());
        if (wasAllowed || !isAllowed) {
            return (!wasAllowed || isAllowed) ? 0 : 2;
        }
        return 1;
    }

    void incrementProcStateSeqAndNotifyAppsLOSP(com.android.server.am.ActiveUids activeUids) {
        int blockState;
        for (int i = activeUids.size() - 1; i >= 0; i--) {
            activeUids.valueAt(i).curProcStateSeq = getNextProcStateSeq();
        }
        if (this.mService.mConstants.mNetworkAccessTimeoutMs <= 0) {
            return;
        }
        java.util.ArrayList<java.lang.Integer> blockingUids = null;
        for (int i2 = activeUids.size() - 1; i2 >= 0; i2--) {
            com.android.server.am.UidRecord uidRec = activeUids.valueAt(i2);
            if (this.mService.mInjector.isNetworkRestrictedForUid(uidRec.getUid()) && android.os.UserHandle.isApp(uidRec.getUid()) && uidRec.hasInternetPermission && ((uidRec.getSetProcState() != uidRec.getCurProcState() || uidRec.getSetCapability() != uidRec.getCurCapability()) && (blockState = getBlockStateForUid(uidRec)) != 0)) {
                synchronized (uidRec.networkStateLock) {
                    if (blockState == 1) {
                        if (blockingUids == null) {
                            blockingUids = new java.util.ArrayList<>();
                        }
                        blockingUids.add(java.lang.Integer.valueOf(uidRec.getUid()));
                    } else {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                            android.util.Slog.d("ActivityManager_Network", "uid going to background, notifying all blocking threads for uid: " + uidRec);
                        }
                        if (uidRec.procStateSeqWaitingForNetwork != 0) {
                            uidRec.networkStateLock.notifyAll();
                        }
                    }
                }
            }
        }
        if (blockingUids == null) {
            return;
        }
        for (int i3 = this.mLruProcesses.size() - 1; i3 >= 0; i3--) {
            com.android.server.am.ProcessRecord app = this.mLruProcesses.get(i3);
            if (blockingUids.contains(java.lang.Integer.valueOf(app.uid))) {
                android.app.IApplicationThread thread = app.getThread();
                if (!app.isKilledByAm() && thread != null) {
                    com.android.server.am.UidRecord uidRec2 = getUidRecordLOSP(app.uid);
                    try {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                            android.util.Slog.d("ActivityManager_Network", "Informing app thread that it needs to block: " + uidRec2);
                        }
                        if (uidRec2 != null) {
                            thread.setNetworkBlockSeq(uidRec2.curProcStateSeq);
                        }
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        }
    }

    long getNextProcStateSeq() {
        long j = this.mProcStateSeqCounter + 1;
        this.mProcStateSeqCounter = j;
        return j;
    }

    private android.net.LocalSocket createSystemServerSocketForZygote() {
        java.io.File socketFile = new java.io.File(UNSOL_ZYGOTE_MSG_SOCKET_PATH);
        if (socketFile.exists()) {
            socketFile.delete();
        }
        android.net.LocalSocket serverSocket = null;
        try {
            serverSocket = new android.net.LocalSocket(1);
            serverSocket.bind(new android.net.LocalSocketAddress(UNSOL_ZYGOTE_MSG_SOCKET_PATH, android.net.LocalSocketAddress.Namespace.FILESYSTEM));
            android.system.Os.chmod(UNSOL_ZYGOTE_MSG_SOCKET_PATH, 438);
            return serverSocket;
        } catch (java.lang.Exception e) {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (java.io.IOException e2) {
                }
                return null;
            }
            return serverSocket;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleZygoteMessages(java.io.FileDescriptor fd, int events) {
        fd.getInt$();
        if ((events & 1) != 0) {
            try {
                int len = android.system.Os.read(fd, this.mZygoteUnsolicitedMessage, 0, this.mZygoteUnsolicitedMessage.length);
                if (len > 0 && this.mZygoteSigChldMessage.length == com.android.internal.os.Zygote.nativeParseSigChld(this.mZygoteUnsolicitedMessage, len, this.mZygoteSigChldMessage)) {
                    this.mAppExitInfoTracker.handleZygoteSigChld(this.mZygoteSigChldMessage[0], this.mZygoteSigChldMessage[1], this.mZygoteSigChldMessage[2]);
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.w("ActivityManager", "Exception in reading unsolicited zygote message: " + e);
            }
        }
        return 1;
    }

    boolean handleDyingAppDeathLocked(com.android.server.am.ProcessRecord app, int pid) {
        try {
            if (this.mProcessNames.get(app.processName, app.uid) != app && this.mDyingProcesses.get(app.processName, app.uid) == app) {
                android.util.Slog.v("ActivityManager", "Got obituary of " + pid + ":" + app.processName);
                app.unlinkDeathRecipient();
                this.mDyingProcesses.remove(app.processName, app.uid);
                app.setDyingPid(0);
                handlePrecedingAppDiedLocked(app);
                removeLruProcessLocked(app);
                return true;
            }
        } catch (java.util.ConcurrentModificationException e) {
            if (this.mService == null || java.lang.Thread.holdsLock(this.mService)) {
                android.util.Slog.e("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException");
            } else {
                android.util.Slog.wtf("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException, didn't hold mService lock");
            }
        }
        return false;
    }

    boolean handlePrecedingAppDiedLocked(com.android.server.am.ProcessRecord app) {
        if (app.mSuccessor == null) {
            return true;
        }
        if (app.isPersistent() && !app.isRemoved() && this.mService.mPersistentStartingProcesses.indexOf(app.mSuccessor) < 0 && this.mProcessListExt.needAddPersistentStartingProcesses(app.mSuccessor)) {
            this.mService.mPersistentStartingProcesses.add(app.mSuccessor);
        }
        app.mSuccessor.mPredecessor = null;
        app.mSuccessor = null;
        this.mService.mProcStartHandler.removeMessages(2, app);
        this.mService.mProcStartHandler.obtainMessage(1, app).sendToTarget();
        return false;
    }

    void updateBackgroundRestrictedForUidPackageLocked(int uid, final java.lang.String packageName, final boolean restricted) {
        com.android.server.am.UidRecord uidRec = getUidRecordLOSP(uid);
        if (uidRec != null) {
            final long nowElapsed = android.os.SystemClock.elapsedRealtime();
            uidRec.forEachProcess(new java.util.function.Consumer() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$updateBackgroundRestrictedForUidPackageLocked$5(packageName, restricted, nowElapsed, (com.android.server.am.ProcessRecord) obj);
                }
            });
            this.mService.updateOomAdjPendingTargetsLocked(21);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBackgroundRestrictedForUidPackageLocked$5(java.lang.String packageName, boolean restricted, long nowElapsed, com.android.server.am.ProcessRecord app) {
        if (android.text.TextUtils.equals(app.info.packageName, packageName)) {
            app.mState.setBackgroundRestricted(restricted);
            if (restricted) {
                this.mAppsInBackgroundRestricted.add(app);
                long future = lambda$killAppIfBgRestrictedAndCachedIdleLocked$6(app, nowElapsed);
                if (future > 0 && (this.mService.mDeterministicUidIdle || !this.mService.mHandler.hasMessages(58))) {
                    this.mService.mHandler.sendEmptyMessageDelayed(58, future - nowElapsed);
                }
            } else {
                this.mAppsInBackgroundRestricted.remove(app);
            }
            if (!app.isKilledByAm()) {
                this.mService.lambda$appDiedLocked$2(app);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: killAppIfBgRestrictedAndCachedIdleLocked, reason: merged with bridge method [inline-methods] */
    public long lambda$killAppIfBgRestrictedAndCachedIdleLocked$6(com.android.server.am.ProcessRecord app, long nowElapsed) {
        if (app == null) {
            return 0L;
        }
        com.android.server.am.UidRecord uidRec = app.getUidRecord();
        long lastCanKillTime = app.mState.getLastCanKillOnBgRestrictedAndIdleTime();
        if (!this.mService.mConstants.mKillBgRestrictedAndCachedIdle || app.isKilled() || app.getThread() == null || uidRec == null || !uidRec.isIdle() || !app.isCached() || app.mState.shouldNotKillOnBgRestrictedAndIdle() || !app.mState.isBackgroundRestricted() || lastCanKillTime == 0) {
            return 0L;
        }
        long future = this.mService.mConstants.mKillBgRestrictedAndCachedIdleSettleTimeMs + lastCanKillTime;
        if (future <= nowElapsed) {
            app.killLocked("cached idle & background restricted", 13, 18, true);
            return 0L;
        }
        return future;
    }

    void killAppIfBgRestrictedAndCachedIdleLocked(com.android.server.am.UidRecord uidRec) {
        final long nowElapsed = android.os.SystemClock.elapsedRealtime();
        uidRec.forEachProcess(new java.util.function.Consumer() { // from class: com.android.server.am.ProcessList$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$killAppIfBgRestrictedAndCachedIdleLocked$6(nowElapsed, (com.android.server.am.ProcessRecord) obj);
            }
        });
    }

    void noteProcessDiedLocked(com.android.server.am.ProcessRecord app) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.i("ActivityManager", "note: " + app + " died, saving the exit info");
        }
        com.android.server.Watchdog.getInstance().processDied(app.processName, app.getPid());
        if (app.getDeathRecipient() == null) {
            try {
                if (this.mDyingProcesses.get(app.processName, app.uid) == app) {
                    this.mDyingProcesses.remove(app.processName, app.uid);
                    app.setDyingPid(0);
                }
            } catch (java.util.ConcurrentModificationException e) {
                if (this.mService == null || java.lang.Thread.holdsLock(this.mService)) {
                    android.util.Slog.e("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException");
                } else {
                    android.util.Slog.wtf("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException, didn't hold mService lock");
                }
            }
        }
        this.mAppExitInfoTracker.scheduleNoteProcessDied(app);
    }

    void noteAppRecoverableCrash(com.android.server.am.ProcessRecord app) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.i("ActivityManager", "note: " + app + " has a recoverable native crash");
        }
        this.mAppExitInfoTracker.scheduleNoteAppRecoverableCrash(app);
    }

    void noteAppKill(com.android.server.am.ProcessRecord app, int reason, int subReason, java.lang.String msg) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.i("ActivityManager", "note: " + app + " is being killed, reason: " + reason + ", sub-reason: " + subReason + ", message: " + msg);
        }
        this.mProcessListExt.noteAppKill(app);
        if (app.getPid() > 0 && !app.isolated && app.getDeathRecipient() != null) {
            try {
                this.mDyingProcesses.put(app.processName, app.uid, app);
                app.setDyingPid(app.getPid());
            } catch (java.util.ConcurrentModificationException e) {
                if (this.mService == null || java.lang.Thread.holdsLock(this.mService)) {
                    android.util.Slog.e("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException");
                } else {
                    android.util.Slog.wtf("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException, didn't hold mService lock");
                }
            }
        }
        this.mAppExitInfoTracker.scheduleNoteAppKill(app, reason, subReason, msg);
    }

    void noteAppKill(int pid, int uid, int reason, int subReason, java.lang.String msg) {
        com.android.server.am.ProcessRecord app;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.i("ActivityManager", "note: " + pid + " is being killed, reason: " + reason + ", sub-reason: " + subReason + ", message: " + msg);
        }
        synchronized (this.mService.mPidsSelfLocked) {
            app = this.mService.mPidsSelfLocked.get(pid);
        }
        this.mProcessListExt.noteAppKill(app);
        if (app != null && app.uid == uid && !app.isolated && app.getDeathRecipient() != null) {
            try {
                this.mDyingProcesses.put(app.processName, uid, app);
                app.setDyingPid(app.getPid());
            } catch (java.util.ConcurrentModificationException e) {
                if (this.mService == null || java.lang.Thread.holdsLock(this.mService)) {
                    android.util.Slog.e("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException");
                } else {
                    android.util.Slog.wtf("ActivityManager", "mDyingProcesses trigger ConcurrentModificationException, didn't hold mService lock");
                }
            }
        }
        this.mAppExitInfoTracker.scheduleNoteAppKill(pid, uid, reason, subReason, msg);
    }

    void killProcessesWhenImperceptible(int[] pids, java.lang.String reason, int requester) {
        com.android.server.am.ProcessRecord app;
        if (com.android.internal.util.ArrayUtils.isEmpty(pids)) {
            return;
        }
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            for (int i : pids) {
                try {
                    synchronized (this.mService.mPidsSelfLocked) {
                        app = this.mService.mPidsSelfLocked.get(i);
                    }
                    if (app != null) {
                        this.mImperceptibleKillRunner.enqueueLocked(app, reason, requester);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    android.util.Pair<java.lang.Integer, java.lang.Integer> getNumForegroundServices() {
        int numForegroundServices = 0;
        int procs = 0;
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                int size = this.mLruProcesses.size();
                for (int i = 0; i < size; i++) {
                    com.android.server.am.ProcessRecord pr = this.mLruProcesses.get(i);
                    int numFgs = pr.mServices.getNumForegroundServices();
                    if (numFgs > 0) {
                        numForegroundServices += numFgs;
                        procs++;
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        return new android.util.Pair<>(java.lang.Integer.valueOf(numForegroundServices), java.lang.Integer.valueOf(procs));
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ImperceptibleKillRunner extends android.app.UidObserver {
        private static final java.lang.String DROPBOX_TAG_IMPERCEPTIBLE_KILL = "imperceptible_app_kill";
        private static final java.lang.String EXTRA_PID = "pid";
        private static final java.lang.String EXTRA_REASON = "reason";
        private static final java.lang.String EXTRA_REQUESTER = "requester";
        private static final java.lang.String EXTRA_TIMESTAMP = "timestamp";
        private static final java.lang.String EXTRA_UID = "uid";
        private static final boolean LOG_TO_DROPBOX = false;
        private android.os.Handler mHandler;
        private volatile boolean mIdle;
        private com.android.server.am.ProcessList.ImperceptibleKillRunner.IdlenessReceiver mReceiver;
        private boolean mUidObserverEnabled;
        private android.util.SparseArray<java.util.List<android.os.Bundle>> mWorkItems = new android.util.SparseArray<>();
        private com.android.internal.app.ProcessMap<java.lang.Long> mLastProcessKillTimes = new com.android.internal.app.ProcessMap<>();

        private final class H extends android.os.Handler {
            static final int MSG_DEVICE_IDLE = 0;
            static final int MSG_UID_GONE = 1;
            static final int MSG_UID_STATE_CHANGED = 2;

            H(android.os.Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:
                        com.android.server.am.ProcessList.ImperceptibleKillRunner.this.handleDeviceIdle();
                        break;
                    case 1:
                        com.android.server.am.ProcessList.ImperceptibleKillRunner.this.handleUidGone(msg.arg1);
                        break;
                    case 2:
                        com.android.server.am.ProcessList.ImperceptibleKillRunner.this.handleUidStateChanged(msg.arg1, msg.arg2);
                        break;
                }
            }
        }

        private final class IdlenessReceiver extends android.content.BroadcastReceiver {
            private IdlenessReceiver() {
            }

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:11:0x0030  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r4, android.content.Intent r5) {
                /*
                    r3 = this;
                    com.android.server.am.ProcessList$ImperceptibleKillRunner r0 = com.android.server.am.ProcessList.ImperceptibleKillRunner.this
                    com.android.server.am.ProcessList r0 = com.android.server.am.ProcessList.this
                    com.android.server.am.ActivityManagerService r0 = r0.mService
                    android.content.Context r0 = r0.mContext
                    java.lang.Class<android.os.PowerManager> r1 = android.os.PowerManager.class
                    java.lang.Object r0 = r0.getSystemService(r1)
                    android.os.PowerManager r0 = (android.os.PowerManager) r0
                    java.lang.String r1 = r5.getAction()
                    int r2 = r1.hashCode()
                    switch(r2) {
                        case 498807504: goto L26;
                        case 870701415: goto L1c;
                        default: goto L1b;
                    }
                L1b:
                    goto L30
                L1c:
                    java.lang.String r2 = "android.os.action.DEVICE_IDLE_MODE_CHANGED"
                    boolean r1 = r1.equals(r2)
                    if (r1 == 0) goto L1b
                    r1 = 1
                    goto L31
                L26:
                    java.lang.String r2 = "android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED"
                    boolean r1 = r1.equals(r2)
                    if (r1 == 0) goto L1b
                    r1 = 0
                    goto L31
                L30:
                    r1 = -1
                L31:
                    switch(r1) {
                        case 0: goto L3f;
                        case 1: goto L35;
                        default: goto L34;
                    }
                L34:
                    goto L49
                L35:
                    com.android.server.am.ProcessList$ImperceptibleKillRunner r1 = com.android.server.am.ProcessList.ImperceptibleKillRunner.this
                    boolean r2 = r0.isDeviceIdleMode()
                    r1.notifyDeviceIdleness(r2)
                    goto L49
                L3f:
                    com.android.server.am.ProcessList$ImperceptibleKillRunner r1 = com.android.server.am.ProcessList.ImperceptibleKillRunner.this
                    boolean r2 = r0.isLightDeviceIdleMode()
                    r1.notifyDeviceIdleness(r2)
                L49:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ProcessList.ImperceptibleKillRunner.IdlenessReceiver.onReceive(android.content.Context, android.content.Intent):void");
            }
        }

        ImperceptibleKillRunner(android.os.Looper looper) {
            this.mHandler = new com.android.server.am.ProcessList.ImperceptibleKillRunner.H(looper);
        }

        boolean enqueueLocked(com.android.server.am.ProcessRecord app, java.lang.String reason, int requester) {
            java.lang.Long last = app.isolated ? null : (java.lang.Long) this.mLastProcessKillTimes.get(app.processName, app.uid);
            if (last != null && android.os.SystemClock.uptimeMillis() < last.longValue() + ((long) com.android.server.am.ActivityManagerConstants.MIN_CRASH_INTERVAL)) {
                return false;
            }
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putInt("pid", app.getPid());
            bundle.putInt("uid", app.uid);
            bundle.putLong("timestamp", app.getStartTime());
            bundle.putString("reason", reason);
            bundle.putInt(EXTRA_REQUESTER, requester);
            java.util.List<android.os.Bundle> list = this.mWorkItems.get(app.uid);
            if (list == null) {
                list = new java.util.ArrayList();
                this.mWorkItems.put(app.uid, list);
            }
            list.add(bundle);
            if (this.mReceiver == null) {
                this.mReceiver = new com.android.server.am.ProcessList.ImperceptibleKillRunner.IdlenessReceiver();
                android.content.IntentFilter filter = new android.content.IntentFilter("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
                filter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
                com.android.server.am.ProcessList.this.mService.mContext.registerReceiver(this.mReceiver, filter);
                return true;
            }
            return true;
        }

        void notifyDeviceIdleness(boolean idle) {
            boolean diff = this.mIdle != idle;
            this.mIdle = idle;
            if (diff && idle) {
                com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ProcessList.this.mService;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        if (this.mWorkItems.size() > 0) {
                            this.mHandler.sendEmptyMessage(0);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDeviceIdle() {
            android.os.DropBoxManager dbox = (android.os.DropBoxManager) com.android.server.am.ProcessList.this.mService.mContext.getSystemService(android.os.DropBoxManager.class);
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ProcessList.this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    int j = this.mWorkItems.size();
                    int i = j - 1;
                    while (this.mIdle && i >= 0) {
                        java.util.List<android.os.Bundle> list = this.mWorkItems.valueAt(i);
                        int len = list.size();
                        int j2 = len - 1;
                        while (this.mIdle && j2 >= 0) {
                            android.os.Bundle bundle = list.get(j2);
                            int size = j;
                            int size2 = j2;
                            if (killProcessLocked(bundle.getInt("pid"), bundle.getInt("uid"), bundle.getLong("timestamp"), bundle.getString("reason"), bundle.getInt(EXTRA_REQUESTER), dbox, false)) {
                                list.remove(size2);
                            }
                            j2 = size2 - 1;
                            j = size;
                        }
                        int size3 = j;
                        int size4 = list.size();
                        if (size4 == 0) {
                            this.mWorkItems.removeAt(i);
                        }
                        i--;
                        j = size3;
                    }
                    registerUidObserverIfNecessaryLocked();
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        private void registerUidObserverIfNecessaryLocked() {
            if (!this.mUidObserverEnabled && this.mWorkItems.size() > 0) {
                this.mUidObserverEnabled = true;
                com.android.server.am.ProcessList.this.mService.registerUidObserver(this, 3, -1, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            } else if (this.mUidObserverEnabled && this.mWorkItems.size() == 0) {
                this.mUidObserverEnabled = false;
                com.android.server.am.ProcessList.this.mService.unregisterUidObserver(this);
            }
        }

        private boolean killProcessLocked(int pid, int uid, long timestamp, java.lang.String reason, int requester, android.os.DropBoxManager dbox, boolean logToDropbox) throws java.lang.Throwable {
            com.android.server.am.ProcessRecord app;
            synchronized (com.android.server.am.ProcessList.this.mService.mPidsSelfLocked) {
                try {
                    app = com.android.server.am.ProcessList.this.mService.mPidsSelfLocked.get(pid);
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
            }
            if (app != null && app.getPid() == pid) {
                if (app.uid != uid || app.getStartTime() != timestamp || app.getPkgList().searchEachPackage(new java.util.function.Function() { // from class: com.android.server.am.ProcessList$ImperceptibleKillRunner$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return this.f$0.lambda$killProcessLocked$0((java.lang.String) obj);
                    }
                }) != null) {
                    return true;
                }
                if (!com.android.server.am.ProcessList.this.mService.mConstants.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.contains(java.lang.Integer.valueOf(app.mState.getReportedProcState()))) {
                    app.killLocked(reason, 13, 15, true);
                    if (!app.isolated) {
                        this.mLastProcessKillTimes.put(app.processName, app.uid, java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis()));
                    }
                    if (logToDropbox) {
                        android.os.SystemClock.elapsedRealtime();
                        java.lang.StringBuilder sb = new java.lang.StringBuilder();
                        com.android.server.am.ProcessList.this.mService.appendDropBoxProcessHeaders(app, app.processName, null, sb);
                        sb.append("Reason: " + reason).append("\n");
                        sb.append("Requester UID: " + requester).append("\n");
                        dbox.addText(DROPBOX_TAG_IMPERCEPTIBLE_KILL, sb.toString());
                    }
                    return true;
                }
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.lang.Boolean lambda$killProcessLocked$0(java.lang.String pkgName) {
            if (com.android.server.am.ProcessList.this.mService.mConstants.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.contains(pkgName)) {
                return java.lang.Boolean.TRUE;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleUidStateChanged(int uid, int procState) {
            java.util.List<android.os.Bundle> list;
            android.os.DropBoxManager dbox = (android.os.DropBoxManager) com.android.server.am.ProcessList.this.mService.mContext.getSystemService(android.os.DropBoxManager.class);
            boolean logToDropbox = dbox != null && dbox.isTagEnabled(DROPBOX_TAG_IMPERCEPTIBLE_KILL);
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ProcessList.this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (this.mIdle && !com.android.server.am.ProcessList.this.mService.mConstants.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.contains(java.lang.Integer.valueOf(procState)) && (list = this.mWorkItems.get(uid)) != null) {
                        int len = list.size();
                        for (int j = len - 1; this.mIdle && j >= 0; j--) {
                            android.os.Bundle bundle = list.get(j);
                            if (killProcessLocked(bundle.getInt("pid"), bundle.getInt("uid"), bundle.getLong("timestamp"), bundle.getString("reason"), bundle.getInt(EXTRA_REQUESTER), dbox, logToDropbox)) {
                                list.remove(j);
                            }
                        }
                        if (list.size() == 0) {
                            this.mWorkItems.remove(uid);
                        }
                        registerUidObserverIfNecessaryLocked();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleUidGone(int uid) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ProcessList.this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    this.mWorkItems.remove(uid);
                    registerUidObserverIfNecessaryLocked();
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void onUidGone(int uid, boolean disabled) {
            this.mHandler.obtainMessage(1, uid, 0).sendToTarget();
        }

        public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
            this.mHandler.obtainMessage(2, uid, procState).sendToTarget();
        }
    }

    public com.android.server.am.IProcessListWrapper getWrapper() {
        return this.mProcListWrapper;
    }

    private class ProcessListWrapper implements com.android.server.am.IProcessListWrapper {
        private ProcessListWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.am.IProcessListExt getExtImpl() {
            return com.android.server.am.ProcessList.this.mProcessListExt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.am.IProcessListSocExt getSocExtImpl() {
            return com.android.server.am.ProcessList.this.mSocExt;
        }

        @Override // com.android.server.am.IProcessListWrapper
        public boolean writeLmkd(java.nio.ByteBuffer buf, java.nio.ByteBuffer repl) {
            return com.android.server.am.ProcessList.writeLmkd(buf, repl);
        }

        @Override // com.android.server.am.IProcessListWrapper
        public void onBootComplete() {
            com.android.server.am.ProcessList.this.mProcessListExt.onBootComplete();
        }
    }
}
