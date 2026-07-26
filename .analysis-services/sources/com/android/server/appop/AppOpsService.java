package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AppOpsService extends com.android.internal.app.IAppOpsService.Stub {
    private static final int CURRENT_VERSION = 1;
    static final boolean DEBUG = false;
    private static final int MAX_UNFORWARDED_OPS = 10;
    private static final int MAX_UNUSED_POOLED_OBJECTS = 3;
    private static final int RARELY_USED_PACKAGES_INITIALIZATION_DELAY_MILLIS = 300000;
    static final java.lang.String TAG = "AppOps";
    private static final int UID_ANY = -2;
    static final long WRITE_DELAY = 1800000;
    com.android.server.appop.AppOpsCheckingServiceInterface mAppOpsCheckingService;
    com.android.server.appop.AppOpsRestrictions mAppOpsRestrictions;
    public com.android.server.appop.IAppOpsServiceExt mAppOpsServiceExt;
    private android.app.RuntimeAppOpAccessMessage mCollectedRuntimePermissionMessage;
    final com.android.server.appop.AppOpsService.Constants mConstants;
    final android.content.Context mContext;
    boolean mFastWriteScheduled;
    final android.os.Handler mHandler;
    private float mMessagesCollectedCount;
    private final java.io.File mNoteOpCallerStacktracesFile;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private com.android.server.pm.PackageManagerLocal mPackageManagerLocal;
    android.util.SparseIntArray mProfileOwners;
    private final com.android.server.appop.AppOpsRecentAccessPersistence mRecentAccessPersistence;
    final android.util.AtomicFile mRecentAccessesFile;
    private int mSamplingStrategy;
    private android.hardware.SensorPrivacyManager mSensorPrivacyManager;
    final android.util.AtomicFile mStorageFile;
    private com.android.server.appop.AppOpsUidStateTracker mUidStateTracker;
    private boolean mUidStatesInitialized;
    private com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private com.android.server.companion.virtual.VirtualDeviceManagerInternal mVirtualDeviceManagerInternal;
    boolean mWriteNoteOpsScheduled;
    boolean mWriteScheduled;
    private static final int[] OPS_RESTRICTED_ON_SUSPEND = {28, 27, 26, 3};
    private static final boolean IS_AGING_VERSION = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
    private static final int[] NON_PACKAGE_UIDS = {0, 1001, 1002, 1041, 1027, 1073, 2000};
    private final android.util.ArraySet<com.android.server.appop.AppOpsService.NoteOpTrace> mNoteOpCallerStacktraces = new android.util.ArraySet<>();
    final com.android.server.appop.AttributedOp.OpEventProxyInfoPool mOpEventProxyInfoPool = new com.android.server.appop.AttributedOp.OpEventProxyInfoPool(3);
    final com.android.server.appop.AttributedOp.InProgressStartOpEventPool mInProgressStartOpEventPool = new com.android.server.appop.AttributedOp.InProgressStartOpEventPool(this.mOpEventProxyInfoPool, 3);
    private final com.android.server.appop.AppOpsService.AppOpsManagerInternalImpl mAppOpsManagerInternal = new com.android.server.appop.AppOpsService.AppOpsManagerInternalImpl();
    private final android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
    private final android.util.SparseArray<java.lang.String> mKnownDeviceIds = new android.util.SparseArray<>();
    private final com.android.internal.compat.IPlatformCompat mPlatformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));
    private final android.util.ArrayMap<android.util.Pair<java.lang.String, java.lang.Integer>, android.os.RemoteCallbackList<com.android.internal.app.IAppOpsAsyncNotedCallback>> mAsyncOpWatchers = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.util.Pair<java.lang.String, java.lang.Integer>, java.util.ArrayList<android.app.AsyncNotedAppOp>> mUnforwardedAsyncNotedOps = new android.util.ArrayMap<>();
    private final android.util.SparseArray<android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener>> mOpModeWatchers = new android.util.SparseArray<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener>> mPackageModeWatchers = new android.util.ArrayMap<>();
    final java.lang.Runnable mWriteRunner = new java.lang.Runnable() { // from class: com.android.server.appop.AppOpsService.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.appop.AppOpsService.this) {
                com.android.server.appop.AppOpsService.this.mWriteScheduled = false;
                com.android.server.appop.AppOpsService.this.mFastWriteScheduled = false;
                android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void> task = new android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void>() { // from class: com.android.server.appop.AppOpsService.1.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // android.os.AsyncTask
                    public java.lang.Void doInBackground(java.lang.Void... params) throws java.io.IOException {
                        com.android.server.appop.AppOpsService.this.writeRecentAccesses();
                        return null;
                    }
                };
                task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR, null);
            }
        }
    };
    final android.util.SparseArray<com.android.server.appop.AppOpsService.UidState> mUidStates = new android.util.SparseArray<>();
    volatile com.android.server.appop.HistoricalRegistry mHistoricalRegistry = new com.android.server.appop.HistoricalRegistry(this);
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AppOpsService.ClientUserRestrictionState> mOpUserRestrictions = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AppOpsService.ClientGlobalRestrictionState> mOpGlobalRestrictions = new android.util.ArrayMap<>();
    private volatile com.android.server.appop.AppOpsService.CheckOpsDelegateDispatcher mCheckOpsDelegateDispatcher = new com.android.server.appop.AppOpsService.CheckOpsDelegateDispatcher(null, null);
    private final android.util.SparseArray<int[]> mSwitchedOps = new android.util.SparseArray<>();
    private java.lang.String mSampledPackage = null;
    private int mSampledAppOpCode = -1;
    private int mAcceptableLeftDistance = 0;
    private android.util.ArraySet<java.lang.String> mRarelyUsedPackages = new android.util.ArraySet<>();
    private com.android.internal.app.IAppOpsCallback mIgnoredCallback = null;
    final android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AppOpsService.ModeCallback> mModeWatchers = new android.util.ArrayMap<>();
    final android.util.ArrayMap<android.os.IBinder, android.util.SparseArray<com.android.server.appop.AppOpsService.ActiveCallback>> mActiveWatchers = new android.util.ArrayMap<>();
    final android.util.ArrayMap<android.os.IBinder, android.util.SparseArray<com.android.server.appop.AppOpsService.StartedCallback>> mStartedWatchers = new android.util.ArrayMap<>();
    final android.util.ArrayMap<android.os.IBinder, android.util.SparseArray<com.android.server.appop.AppOpsService.NotedCallback>> mNotedWatchers = new android.util.ArrayMap<>();
    final com.android.server.appop.AudioRestrictionManager mAudioRestrictionManager = new com.android.server.appop.AudioRestrictionManager();
    private android.content.BroadcastReceiver mOnPackageUpdatedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.appop.AppOpsService.3
        /* JADX WARN: Removed duplicated region for block: B:12:0x005c A[Catch: all -> 0x006e, TryCatch #2 {, blocks: (B:9:0x0044, B:10:0x004d, B:12:0x005c, B:13:0x0066, B:14:0x006c), top: B:52:0x0044 }] */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r10, android.content.Intent r11) {
            /*
                r9 = this;
                java.lang.String r0 = r11.getAction()
                android.net.Uri r1 = r11.getData()
                java.lang.String r1 = r1.getEncodedSchemeSpecificPart()
                java.lang.String r2 = "android.intent.extra.UID"
                r3 = -1
                int r8 = r11.getIntExtra(r2, r3)
                java.lang.String r2 = "android.intent.action.PACKAGE_ADDED"
                boolean r2 = r0.equals(r2)
                r3 = 0
                if (r2 == 0) goto L71
                java.lang.String r2 = "android.intent.extra.REPLACING"
                boolean r2 = r11.getBooleanExtra(r2, r3)
                if (r2 != 0) goto L71
                com.android.server.appop.AppOpsService r2 = com.android.server.appop.AppOpsService.this
                android.content.pm.PackageManagerInternal r2 = com.android.server.appop.AppOpsService.m1662$$Nest$mgetPackageManagerInternal(r2)
                int r6 = android.os.Process.myUid()
                int r7 = android.os.UserHandle.getUserId(r8)
                r4 = 4096(0x1000, double:2.0237E-320)
                r3 = r1
                android.content.pm.PackageInfo r2 = r2.getPackageInfo(r3, r4, r6, r7)
                com.android.server.appop.AppOpsService r3 = com.android.server.appop.AppOpsService.this
                boolean r4 = com.android.server.appop.AppOpsService.m1666$$Nest$misSamplingTarget(r3, r2)
                com.android.server.appop.AppOpsService r5 = com.android.server.appop.AppOpsService.this
                monitor-enter(r5)
                if (r4 == 0) goto L4d
                com.android.server.appop.AppOpsService r3 = com.android.server.appop.AppOpsService.this     // Catch: java.lang.Throwable -> L6e
                android.util.ArraySet r3 = com.android.server.appop.AppOpsService.m1652$$Nest$fgetmRarelyUsedPackages(r3)     // Catch: java.lang.Throwable -> L6e
                r3.add(r1)     // Catch: java.lang.Throwable -> L6e
            L4d:
                com.android.server.appop.AppOpsService r3 = com.android.server.appop.AppOpsService.this     // Catch: java.lang.Throwable -> L6e
                r6 = 1
                com.android.server.appop.AppOpsService$UidState r3 = com.android.server.appop.AppOpsService.m1663$$Nest$mgetUidStateLocked(r3, r8, r6)     // Catch: java.lang.Throwable -> L6e
                android.util.ArrayMap<java.lang.String, com.android.server.appop.AppOpsService$Ops> r6 = r3.pkgOps     // Catch: java.lang.Throwable -> L6e
                boolean r6 = r6.containsKey(r1)     // Catch: java.lang.Throwable -> L6e
                if (r6 != 0) goto L66
                android.util.ArrayMap<java.lang.String, com.android.server.appop.AppOpsService$Ops> r6 = r3.pkgOps     // Catch: java.lang.Throwable -> L6e
                com.android.server.appop.AppOpsService$Ops r7 = new com.android.server.appop.AppOpsService$Ops     // Catch: java.lang.Throwable -> L6e
                r7.<init>(r1, r3)     // Catch: java.lang.Throwable -> L6e
                r6.put(r1, r7)     // Catch: java.lang.Throwable -> L6e
            L66:
                com.android.server.appop.AppOpsService r6 = com.android.server.appop.AppOpsService.this     // Catch: java.lang.Throwable -> L6e
                r7 = 0
                com.android.server.appop.AppOpsService.m1658$$Nest$mcreateSandboxUidStateIfNotExistsForAppLocked(r6, r8, r7)     // Catch: java.lang.Throwable -> L6e
                monitor-exit(r5)     // Catch: java.lang.Throwable -> L6e
                goto Lb9
            L6e:
                r3 = move-exception
                monitor-exit(r5)     // Catch: java.lang.Throwable -> L6e
                throw r3
            L71:
                java.lang.String r2 = "android.intent.action.PACKAGE_REMOVED"
                boolean r2 = r0.equals(r2)
                if (r2 == 0) goto L97
                java.lang.String r2 = "android.intent.extra.REPLACING"
                boolean r2 = r11.hasExtra(r2)
                if (r2 != 0) goto L97
                java.lang.String r2 = "android.intent.extra.OPLUS_HIDE"
                boolean r2 = r11.getBooleanExtra(r2, r3)
                if (r2 == 0) goto L8a
                return
            L8a:
                com.android.server.appop.AppOpsService r2 = com.android.server.appop.AppOpsService.this
                monitor-enter(r2)
                com.android.server.appop.AppOpsService r3 = com.android.server.appop.AppOpsService.this     // Catch: java.lang.Throwable -> L94
                com.android.server.appop.AppOpsService.m1673$$Nest$mpackageRemovedLocked(r3, r8, r1)     // Catch: java.lang.Throwable -> L94
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L94
                goto Lb9
            L94:
                r3 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L94
                throw r3
            L97:
                java.lang.String r2 = "android.intent.action.PACKAGE_REPLACED"
                boolean r2 = r0.equals(r2)
                if (r2 == 0) goto Lb9
                com.android.server.appop.AppOpsService r2 = com.android.server.appop.AppOpsService.this
                android.content.pm.PackageManagerInternal r2 = com.android.server.appop.AppOpsService.m1662$$Nest$mgetPackageManagerInternal(r2)
                com.android.server.pm.pkg.AndroidPackage r2 = r2.getPackage(r1)
                if (r2 != 0) goto Lac
                return
            Lac:
                com.android.server.appop.AppOpsService r3 = com.android.server.appop.AppOpsService.this
                monitor-enter(r3)
                com.android.server.appop.AppOpsService r4 = com.android.server.appop.AppOpsService.this     // Catch: java.lang.Throwable -> Lb6
                com.android.server.appop.AppOpsService.m1674$$Nest$mrefreshAttributionsLocked(r4, r2, r8)     // Catch: java.lang.Throwable -> Lb6
                monitor-exit(r3)     // Catch: java.lang.Throwable -> Lb6
                goto Lb9
            Lb6:
                r4 = move-exception
                monitor-exit(r3)     // Catch: java.lang.Throwable -> Lb6
                throw r4
            Lb9:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.AppOpsService.AnonymousClass3.onReceive(android.content.Context, android.content.Intent):void");
        }
    };

    public com.android.server.appop.AppOpsUidStateTracker getUidStateTracker() {
        if (this.mUidStateTracker == null) {
            this.mUidStateTracker = new com.android.server.appop.AppOpsUidStateTrackerImpl((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class), this.mHandler, new java.util.concurrent.Executor() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda8
                @Override // java.util.concurrent.Executor
                public final void execute(java.lang.Runnable runnable) {
                    this.f$0.lambda$getUidStateTracker$0(runnable);
                }
            }, com.android.internal.os.Clock.SYSTEM_CLOCK, this.mConstants);
            this.mUidStateTracker.addUidStateChangedCallback(new android.os.HandlerExecutor(this.mHandler), new com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda9
                @Override // com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback
                public final void onUidStateChanged(int i, int i2, boolean z) {
                    this.f$0.onUidStateChanged(i, i2, z);
                }
            });
        }
        return this.mUidStateTracker;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getUidStateTracker$0(java.lang.Runnable r) {
        synchronized (this) {
            r.run();
        }
    }

    final class Constants extends android.database.ContentObserver {
        public long BG_STATE_SETTLE_TIME;
        public long FG_SERVICE_STATE_SETTLE_TIME;
        public long TOP_STATE_SETTLE_TIME;
        private final android.util.KeyValueListParser mParser;
        private android.content.ContentResolver mResolver;

        public Constants(android.os.Handler handler) {
            super(handler);
            this.mParser = new android.util.KeyValueListParser(',');
            updateConstants();
        }

        public void startMonitoring(android.content.ContentResolver resolver) {
            this.mResolver = resolver;
            this.mResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("app_ops_constants"), false, this);
            updateConstants();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            updateConstants();
        }

        private void updateConstants() {
            java.lang.String value = this.mResolver != null ? android.provider.Settings.Global.getString(this.mResolver, "app_ops_constants") : "";
            synchronized (com.android.server.appop.AppOpsService.this) {
                try {
                    this.mParser.setString(value);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(com.android.server.appop.AppOpsService.TAG, "Bad app ops settings", e);
                }
                this.TOP_STATE_SETTLE_TIME = this.mParser.getDurationMillis("top_state_settle_time", 5000L);
                this.FG_SERVICE_STATE_SETTLE_TIME = this.mParser.getDurationMillis("fg_service_state_settle_time", 5000L);
                this.BG_STATE_SETTLE_TIME = this.mParser.getDurationMillis("bg_state_settle_time", 1000L);
            }
        }

        void dump(java.io.PrintWriter pw) {
            pw.println("  Settings:");
            pw.print("    ");
            pw.print("top_state_settle_time");
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.TOP_STATE_SETTLE_TIME, pw);
            pw.println();
            pw.print("    ");
            pw.print("fg_service_state_settle_time");
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.FG_SERVICE_STATE_SETTLE_TIME, pw);
            pw.println();
            pw.print("    ");
            pw.print("bg_state_settle_time");
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.BG_STATE_SETTLE_TIME, pw);
            pw.println();
        }
    }

    final class UidState {
        public final android.util.ArrayMap<java.lang.String, com.android.server.appop.AppOpsService.Ops> pkgOps = new android.util.ArrayMap<>();
        public final int uid;

        public UidState(int uid) {
            this.uid = uid;
        }

        public void clear() {
            com.android.server.appop.AppOpsService.this.mAppOpsCheckingService.removeUid(this.uid);
            for (int i = 0; i < this.pkgOps.size(); i++) {
                java.lang.String packageName = this.pkgOps.keyAt(i);
                com.android.server.appop.AppOpsService.this.mAppOpsCheckingService.removePackage(packageName, android.os.UserHandle.getUserId(this.uid));
            }
        }

        int evalMode(int op, int mode) {
            return com.android.server.appop.AppOpsService.this.getUidStateTracker().evalMode(this.uid, op, mode);
        }

        public int getState() {
            return com.android.server.appop.AppOpsService.this.getUidStateTracker().getUidState(this.uid);
        }

        public void dump(java.io.PrintWriter pw, long nowElapsed) {
            com.android.server.appop.AppOpsService.this.getUidStateTracker().dumpUidState(pw, this.uid, nowElapsed);
        }
    }

    static final class Ops extends android.util.SparseArray<com.android.server.appop.AppOpsService.Op> {
        android.app.AppOpsManager.RestrictionBypass bypass;
        final java.lang.String packageName;
        final com.android.server.appop.AppOpsService.UidState uidState;
        final android.util.ArraySet<java.lang.String> knownAttributionTags = new android.util.ArraySet<>();
        final android.util.ArraySet<java.lang.String> validAttributionTags = new android.util.ArraySet<>();

        Ops(java.lang.String _packageName, com.android.server.appop.AppOpsService.UidState _uidState) {
            this.packageName = _packageName;
            this.uidState = _uidState;
        }
    }

    private static final class PackageVerificationResult {
        final android.app.AppOpsManager.RestrictionBypass bypass;
        final boolean isAttributionTagValid;

        PackageVerificationResult(android.app.AppOpsManager.RestrictionBypass bypass, boolean isAttributionTagValid) {
            this.bypass = bypass;
            this.isAttributionTagValid = isAttributionTagValid;
        }
    }

    final class Op {
        final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp>> mDeviceAttributedOps = new android.util.ArrayMap<>(1);
        int op;
        final java.lang.String packageName;
        int uid;
        final com.android.server.appop.AppOpsService.UidState uidState;

        Op(com.android.server.appop.AppOpsService.UidState uidState, java.lang.String packageName, int op, int uid) {
            this.op = op;
            this.uid = uid;
            this.uidState = uidState;
            this.packageName = packageName;
            this.mDeviceAttributedOps.put("default:0", new android.util.ArrayMap<>());
        }

        void removeAttributionsWithNoTime() {
            for (int deviceIndex = this.mDeviceAttributedOps.size() - 1; deviceIndex >= 0; deviceIndex--) {
                android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = this.mDeviceAttributedOps.valueAt(deviceIndex);
                for (int tagIndex = attributedOps.size() - 1; tagIndex >= 0; tagIndex--) {
                    if (!attributedOps.valueAt(tagIndex).hasAnyTime()) {
                        attributedOps.removeAt(tagIndex);
                    }
                }
                if (!java.util.Objects.equals("default:0", this.mDeviceAttributedOps.keyAt(deviceIndex)) && attributedOps.isEmpty()) {
                    this.mDeviceAttributedOps.removeAt(deviceIndex);
                }
            }
        }

        com.android.server.appop.AttributedOp getOrCreateAttribution(com.android.server.appop.AppOpsService.Op parent, java.lang.String attributionTag, java.lang.String persistentDeviceId) {
            android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = this.mDeviceAttributedOps.get(persistentDeviceId);
            if (attributedOps == null) {
                attributedOps = new android.util.ArrayMap<>();
                this.mDeviceAttributedOps.put(persistentDeviceId, attributedOps);
            }
            com.android.server.appop.AttributedOp attributedOp = attributedOps.get(attributionTag);
            if (attributedOp == null) {
                com.android.server.appop.AttributedOp attributedOp2 = new com.android.server.appop.AttributedOp(com.android.server.appop.AppOpsService.this, attributionTag, persistentDeviceId, parent);
                attributedOps.put(attributionTag, attributedOp2);
                return attributedOp2;
            }
            return attributedOp;
        }

        android.app.AppOpsManager.OpEntry createEntryLocked(java.lang.String persistentDeviceId) {
            android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = this.mDeviceAttributedOps.get(persistentDeviceId);
            if (attributedOps == null) {
                attributedOps = new android.util.ArrayMap<>();
            }
            android.util.ArrayMap<java.lang.String, android.app.AppOpsManager.AttributedOpEntry> attributionEntries = new android.util.ArrayMap<>(attributedOps.size());
            for (int i = 0; i < attributedOps.size(); i++) {
                attributionEntries.put(attributedOps.keyAt(i), attributedOps.valueAt(i).createAttributedOpEntryLocked());
            }
            return new android.app.AppOpsManager.OpEntry(this.op, com.android.server.appop.AppOpsService.this.mAppOpsCheckingService.getPackageMode(this.packageName, this.op, android.os.UserHandle.getUserId(this.uid)), attributionEntries);
        }

        android.app.AppOpsManager.OpEntry createSingleAttributionEntryLocked(java.lang.String attributionTag) {
            android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = this.mDeviceAttributedOps.get("default:0");
            if (attributedOps == null) {
                attributedOps = new android.util.ArrayMap<>();
            }
            android.util.ArrayMap<java.lang.String, android.app.AppOpsManager.AttributedOpEntry> attributionEntries = new android.util.ArrayMap<>(1);
            if (attributedOps.get(attributionTag) != null) {
                attributionEntries.put(attributionTag, attributedOps.get(attributionTag).createAttributedOpEntryLocked());
            }
            return new android.app.AppOpsManager.OpEntry(this.op, com.android.server.appop.AppOpsService.this.mAppOpsCheckingService.getPackageMode(this.packageName, this.op, android.os.UserHandle.getUserId(this.uid)), attributionEntries);
        }

        boolean isRunning() {
            for (int deviceIndex = 0; deviceIndex < this.mDeviceAttributedOps.size(); deviceIndex++) {
                android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = this.mDeviceAttributedOps.valueAt(deviceIndex);
                for (int tagIndex = 0; tagIndex < attributedOps.size(); tagIndex++) {
                    if (attributedOps.valueAt(tagIndex).isRunning()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    final class ModeCallback extends com.android.server.appop.OnOpModeChangedListener implements android.os.IBinder.DeathRecipient {
        public static final int ALL_OPS = -2;
        private final com.android.internal.app.IAppOpsCallback mCallback;

        ModeCallback(com.android.internal.app.IAppOpsCallback callback, int watchingUid, int flags, int watchedOpCode, int callingUid, int callingPid) {
            super(watchingUid, flags, watchedOpCode, callingUid, callingPid);
            this.mCallback = callback;
            try {
                this.mCallback.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        @Override // com.android.server.appop.OnOpModeChangedListener
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("ModeCallback{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(" watchinguid=");
            android.os.UserHandle.formatUid(sb, getWatchingUid());
            sb.append(" flags=0x");
            sb.append(java.lang.Integer.toHexString(getFlags()));
            switch (getWatchedOpCode()) {
                case -2:
                    sb.append(" op=(all)");
                    break;
                case -1:
                    break;
                default:
                    sb.append(" op=");
                    sb.append(android.app.AppOpsManager.opToName(getWatchedOpCode()));
                    break;
            }
            sb.append(" from uid=");
            android.os.UserHandle.formatUid(sb, getCallingUid());
            sb.append(" pid=");
            sb.append(getCallingPid());
            sb.append('}');
            return sb.toString();
        }

        void unlinkToDeath() {
            this.mCallback.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.appop.AppOpsService.this.stopWatchingMode(this.mCallback);
        }

        @Override // com.android.server.appop.OnOpModeChangedListener
        public void onOpModeChanged(int op, int uid, java.lang.String packageName) throws android.os.RemoteException {
            throw new java.lang.IllegalStateException("unimplemented onOpModeChanged method called for op: " + op + " uid: " + uid + " packageName: " + packageName);
        }

        @Override // com.android.server.appop.OnOpModeChangedListener
        public void onOpModeChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) throws android.os.RemoteException {
            this.mCallback.opChanged(op, uid, packageName, persistentDeviceId);
        }
    }

    final class ActiveCallback implements android.os.IBinder.DeathRecipient {
        final com.android.internal.app.IAppOpsActiveCallback mCallback;
        final int mCallingPid;
        final int mCallingUid;
        final int mWatchingUid;

        ActiveCallback(com.android.internal.app.IAppOpsActiveCallback callback, int watchingUid, int callingUid, int callingPid) {
            this.mCallback = callback;
            this.mWatchingUid = watchingUid;
            this.mCallingUid = callingUid;
            this.mCallingPid = callingPid;
            try {
                this.mCallback.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("ActiveCallback{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(" watchinguid=");
            android.os.UserHandle.formatUid(sb, this.mWatchingUid);
            sb.append(" from uid=");
            android.os.UserHandle.formatUid(sb, this.mCallingUid);
            sb.append(" pid=");
            sb.append(this.mCallingPid);
            sb.append('}');
            return sb.toString();
        }

        void destroy() {
            this.mCallback.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.appop.AppOpsService.this.stopWatchingActive(this.mCallback);
        }
    }

    final class StartedCallback implements android.os.IBinder.DeathRecipient {
        final com.android.internal.app.IAppOpsStartedCallback mCallback;
        final int mCallingPid;
        final int mCallingUid;
        final int mWatchingUid;

        StartedCallback(com.android.internal.app.IAppOpsStartedCallback callback, int watchingUid, int callingUid, int callingPid) {
            this.mCallback = callback;
            this.mWatchingUid = watchingUid;
            this.mCallingUid = callingUid;
            this.mCallingPid = callingPid;
            try {
                this.mCallback.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("StartedCallback{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(" watchinguid=");
            android.os.UserHandle.formatUid(sb, this.mWatchingUid);
            sb.append(" from uid=");
            android.os.UserHandle.formatUid(sb, this.mCallingUid);
            sb.append(" pid=");
            sb.append(this.mCallingPid);
            sb.append('}');
            return sb.toString();
        }

        void destroy() {
            this.mCallback.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.appop.AppOpsService.this.stopWatchingStarted(this.mCallback);
        }
    }

    final class NotedCallback implements android.os.IBinder.DeathRecipient {
        final com.android.internal.app.IAppOpsNotedCallback mCallback;
        final int mCallingPid;
        final int mCallingUid;
        final int mWatchingUid;

        NotedCallback(com.android.internal.app.IAppOpsNotedCallback callback, int watchingUid, int callingUid, int callingPid) {
            this.mCallback = callback;
            this.mWatchingUid = watchingUid;
            this.mCallingUid = callingUid;
            this.mCallingPid = callingPid;
            try {
                this.mCallback.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("NotedCallback{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(" watchinguid=");
            android.os.UserHandle.formatUid(sb, this.mWatchingUid);
            sb.append(" from uid=");
            android.os.UserHandle.formatUid(sb, this.mCallingUid);
            sb.append(" pid=");
            sb.append(this.mCallingPid);
            sb.append('}');
            return sb.toString();
        }

        void destroy() {
            this.mCallback.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.appop.AppOpsService.this.stopWatchingNoted(this.mCallback);
        }
    }

    static void onClientDeath(com.android.server.appop.AttributedOp attributedOp, android.os.IBinder clientId) {
        attributedOp.onClientDeath(clientId);
    }

    private void readNoteOpCallerStackTraces() {
        try {
            if (!this.mNoteOpCallerStacktracesFile.exists()) {
                this.mNoteOpCallerStacktracesFile.createNewFile();
                return;
            }
            java.util.Scanner read = new java.util.Scanner(this.mNoteOpCallerStacktracesFile);
            try {
                read.useDelimiter("\\},");
                while (read.hasNext()) {
                    java.lang.String jsonOps = read.next();
                    this.mNoteOpCallerStacktraces.add(com.android.server.appop.AppOpsService.NoteOpTrace.fromJson(jsonOps));
                }
                read.close();
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Cannot parse traces noteOps", e);
        }
    }

    public AppOpsService(java.io.File recentAccessesFile, java.io.File storageFile, android.os.Handler handler, android.content.Context context) {
        this.mContext = context;
        this.mKnownDeviceIds.put(0, "default:0");
        for (int switchedCode = 0; switchedCode < 149; switchedCode++) {
            int switchCode = android.app.AppOpsManager.opToSwitch(switchedCode);
            this.mSwitchedOps.put(switchCode, com.android.internal.util.ArrayUtils.appendInt(this.mSwitchedOps.get(switchCode), switchedCode));
        }
        this.mAppOpsServiceExt = (com.android.server.appop.IAppOpsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.appop.IAppOpsServiceExt.class).create();
        this.mAppOpsServiceExt.addCustomSwitchedOps(this.mSwitchedOps);
        if (android.permission.PermissionManager.USE_ACCESS_CHECKING_SERVICE) {
            this.mAppOpsCheckingService = new com.android.server.appop.AppOpsCheckingServiceTracingDecorator((com.android.server.appop.AppOpsCheckingServiceInterface) com.android.server.LocalServices.getService(com.android.server.appop.AppOpsCheckingServiceInterface.class));
        } else {
            this.mAppOpsCheckingService = new com.android.server.appop.AppOpsCheckingServiceTracingDecorator(new com.android.server.appop.AppOpsCheckingServiceImpl(storageFile, this, handler, context, this.mSwitchedOps));
        }
        this.mAppOpsCheckingService.addAppOpsModeChangedListener(new com.android.server.appop.AppOpsService.AnonymousClass2());
        this.mAppOpsRestrictions = new com.android.server.appop.AppOpsRestrictionsImpl(context, handler, new com.android.server.appop.AppOpsRestrictions.AppOpsRestrictionRemovedListener() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda16
            @Override // com.android.server.appop.AppOpsRestrictions.AppOpsRestrictionRemovedListener
            public final void onAppOpsRestrictionRemoved(int i) {
                this.f$0.lambda$new$1(i);
            }
        });
        com.android.server.LockGuard.installLock(this, 0);
        this.mStorageFile = new android.util.AtomicFile(storageFile, "appops_legacy");
        this.mRecentAccessesFile = new android.util.AtomicFile(recentAccessesFile, "appops_accesses");
        this.mRecentAccessPersistence = new com.android.server.appop.AppOpsRecentAccessPersistence(this.mRecentAccessesFile, this);
        this.mNoteOpCallerStacktracesFile = null;
        this.mHandler = handler;
        this.mConstants = new com.android.server.appop.AppOpsService.Constants(this.mHandler);
        readRecentAccesses();
        this.mAppOpsCheckingService.readState();
        this.mAppOpsServiceExt.shouldBackupAppOpsXml();
        this.mAppOpsServiceExt.hookServiceStart(this, this.mContext);
    }

    /* JADX INFO: renamed from: com.android.server.appop.AppOpsService$2, reason: invalid class name */
    class AnonymousClass2 implements com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener {
        AnonymousClass2() {
        }

        @Override // com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener
        public void onUidModeChanged(int uid, int code, int mode, java.lang.String persistentDeviceId) {
            com.android.server.appop.AppOpsService.this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.appop.AppOpsService$2$$ExternalSyntheticLambda1
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) throws java.lang.Throwable {
                    ((com.android.server.appop.AppOpsService) obj).notifyOpChangedForAllPkgsInUid(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Boolean) obj4).booleanValue(), (java.lang.String) obj5);
                }
            }, com.android.server.appop.AppOpsService.this, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), false, persistentDeviceId));
        }

        @Override // com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener
        public void onPackageModeChanged(java.lang.String packageName, int userId, int code, int mode) {
            com.android.server.appop.AppOpsService.this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.appop.AppOpsService$2$$ExternalSyntheticLambda0
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) throws java.lang.Throwable {
                    ((com.android.server.appop.AppOpsService) obj).notifyOpChangedForPkg((java.lang.String) obj2, ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Integer) obj5).intValue());
                }
            }, com.android.server.appop.AppOpsService.this, packageName, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(mode), java.lang.Integer.valueOf(userId)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int code) {
        notifyWatchersOnDefaultDevice(code, -2);
    }

    public void publish() {
        android.os.ServiceManager.addService("appops", asBinder());
        com.android.server.LocalServices.addService(android.app.AppOpsManagerInternal.class, this.mAppOpsManagerInternal);
        com.android.server.LocalManagerRegistry.addManager(com.android.server.appop.AppOpsManagerLocal.class, new com.android.server.appop.AppOpsService.AppOpsManagerLocalImpl());
    }

    public void systemReady() {
        this.mVirtualDeviceManagerInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        this.mAppOpsCheckingService.systemReady();
        initializeUidStates();
        this.mConstants.startMonitoring(this.mContext.getContentResolver());
        this.mHistoricalRegistry.systemReady(this.mContext.getContentResolver());
        android.content.IntentFilter packageUpdateFilter = new android.content.IntentFilter();
        packageUpdateFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageUpdateFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        packageUpdateFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        packageUpdateFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mOnPackageUpdatedReceiver, android.os.UserHandle.ALL, packageUpdateFilter, null, null);
        prepareInternalCallbacks();
        android.content.IntentFilter packageSuspendFilter = new android.content.IntentFilter();
        packageSuspendFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
        packageSuspendFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.appop.AppOpsService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int[] changedUids = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                java.lang.String[] changedPkgs = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                for (int code : com.android.server.appop.AppOpsService.OPS_RESTRICTED_ON_SUSPEND) {
                    synchronized (com.android.server.appop.AppOpsService.this) {
                        android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> onModeChangedListeners = (android.util.ArraySet) com.android.server.appop.AppOpsService.this.mOpModeWatchers.get(code);
                        if (onModeChangedListeners != null) {
                            android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> onModeChangedListeners2 = new android.util.ArraySet<>(onModeChangedListeners);
                            for (int i = 0; i < changedUids.length; i++) {
                                int changedUid = changedUids[i];
                                java.lang.String changedPkg = changedPkgs[i];
                                java.util.Set<java.lang.String> devices = new android.util.ArraySet<>();
                                devices.add("default:0");
                                if (com.android.server.appop.AppOpsService.this.mVirtualDeviceManagerInternal != null) {
                                    devices.addAll(com.android.server.appop.AppOpsService.this.mVirtualDeviceManagerInternal.getAllPersistentDeviceIds());
                                }
                                for (java.lang.String device : devices) {
                                    com.android.server.appop.AppOpsService.this.notifyOpChanged(onModeChangedListeners2, code, changedUid, changedPkg, device);
                                    devices = devices;
                                }
                            }
                        }
                    }
                }
            }
        }, android.os.UserHandle.ALL, packageSuspendFilter, null, null);
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.appop.AppOpsService.5
            @Override // java.lang.Runnable
            public void run() {
                java.util.List<java.lang.String> packageNames = com.android.server.appop.AppOpsService.this.getPackageListAndResample();
                com.android.server.appop.AppOpsService.this.initializeRarelyUsedPackagesList(new android.util.ArraySet(packageNames));
            }
        }, 300000L);
        getPackageManagerInternal().setExternalSourcesPolicy(new android.content.pm.PackageManagerInternal.ExternalSourcesPolicy() { // from class: com.android.server.appop.AppOpsService.6
            @Override // android.content.pm.PackageManagerInternal.ExternalSourcesPolicy
            public int getPackageTrustedToInstallApps(java.lang.String packageName, int uid) {
                int appOpMode = com.android.server.appop.AppOpsService.this.checkOperation(66, uid, packageName);
                switch (appOpMode) {
                    case 0:
                        return 0;
                    case 1:
                    default:
                        return 2;
                    case 2:
                        return 1;
                }
            }
        });
        this.mSensorPrivacyManager = android.hardware.SensorPrivacyManager.getInstance(this.mContext);
    }

    void prepareInternalCallbacks() {
        getUserManagerInternal().addUserLifecycleListener(new com.android.server.pm.UserManagerInternal.UserLifecycleListener() { // from class: com.android.server.appop.AppOpsService.7
            @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
            public void onUserCreated(android.content.pm.UserInfo user, java.lang.Object token) {
                com.android.server.appop.AppOpsService.this.initializeUserUidStates(user.id);
            }
        });
    }

    void initializeUidStates() {
        com.android.server.pm.UserManagerInternal umi = getUserManagerInternal();
        synchronized (this) {
            android.util.SparseBooleanArray knownUids = new android.util.SparseBooleanArray();
            for (int uid : NON_PACKAGE_UIDS) {
                if (!this.mUidStates.contains(uid)) {
                    this.mUidStates.put(uid, new com.android.server.appop.AppOpsService.UidState(uid));
                }
                knownUids.put(uid, true);
            }
            int[] userIds = umi.getUserIds();
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = getPackageManagerLocal().withUnfilteredSnapshot();
            try {
                java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> packageStates = snapshot.getPackageStates();
                for (int userId : userIds) {
                    initializeUserUidStatesLocked(userId, packageStates, knownUids);
                }
                trimUidStatesLocked(knownUids, packageStates);
                this.mUidStatesInitialized = true;
                if (snapshot != null) {
                    snapshot.close();
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeUserUidStates(int userId) {
        synchronized (this) {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = getPackageManagerLocal().withUnfilteredSnapshot();
            try {
                initializeUserUidStatesLocked(userId, snapshot.getPackageStates(), null);
                if (snapshot != null) {
                    snapshot.close();
                }
            } finally {
            }
        }
    }

    private void initializeUserUidStatesLocked(int userId, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> packageStates, android.util.SparseBooleanArray knownUids) {
        for (java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState> entry : packageStates.entrySet()) {
            com.android.server.pm.pkg.PackageState packageState = entry.getValue();
            if (!packageState.isApex()) {
                int appId = packageState.getAppId();
                java.lang.String packageName = entry.getKey();
                initializePackageUidStateLocked(userId, appId, packageName, knownUids);
            }
        }
    }

    private void initializePackageUidStateLocked(int userId, int appId, java.lang.String packageName, android.util.SparseBooleanArray knownUids) {
        com.android.server.appop.AppOpsService.Ops ops;
        int uid = android.os.UserHandle.getUid(userId, appId);
        if (knownUids != null) {
            knownUids.put(uid, true);
        }
        com.android.server.appop.AppOpsService.UidState uidState = getUidStateLocked(uid, true);
        com.android.server.appop.AppOpsService.Ops ops2 = uidState.pkgOps.get(packageName);
        if (ops2 != null) {
            ops = ops2;
        } else {
            com.android.server.appop.AppOpsService.Ops ops3 = new com.android.server.appop.AppOpsService.Ops(packageName, uidState);
            uidState.pkgOps.put(packageName, ops3);
            ops = ops3;
        }
        android.util.SparseIntArray packageModes = this.mAppOpsCheckingService.getNonDefaultPackageModes(packageName, userId);
        for (int k = 0; k < packageModes.size(); k++) {
            int code = packageModes.keyAt(k);
            if (ops.indexOfKey(code) < 0) {
                ops.put(code, new com.android.server.appop.AppOpsService.Op(uidState, packageName, code, uid));
            }
        }
        createSandboxUidStateIfNotExistsForAppLocked(uid, knownUids);
    }

    private void trimUidStatesLocked(android.util.SparseBooleanArray knownUids, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> packageStates) {
        synchronized (this) {
            for (int uidIdx = this.mUidStates.size() - 1; uidIdx >= 0; uidIdx--) {
                int uid = this.mUidStates.keyAt(uidIdx);
                if (knownUids.get(uid, false)) {
                    int appId = android.os.UserHandle.getAppId(uid);
                    if (appId >= 10000 && appId <= 19999) {
                        android.util.ArrayMap<java.lang.String, com.android.server.appop.AppOpsService.Ops> pkgOps = this.mUidStates.valueAt(uidIdx).pkgOps;
                        for (int pkgIdx = pkgOps.size() - 1; pkgIdx >= 0; pkgIdx--) {
                            java.lang.String pkgName = pkgOps.keyAt(pkgIdx);
                            if (!packageStates.containsKey(pkgName)) {
                                pkgOps.removeAt(pkgIdx);
                            } else {
                                com.android.server.pm.pkg.AndroidPackage pkg = packageStates.get(pkgName).getAndroidPackage();
                                if (pkg != null) {
                                    refreshAttributionsLocked(pkg, uid);
                                }
                            }
                        }
                        if (pkgOps.isEmpty()) {
                            this.mUidStates.removeAt(uidIdx);
                        }
                    }
                } else {
                    this.mUidStates.removeAt(uidIdx);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshAttributionsLocked(com.android.server.pm.pkg.AndroidPackage pkg, int uid) {
        com.android.server.appop.AppOpsService.Ops ops;
        java.lang.String pkgName;
        java.lang.String pkgName2 = pkg.getPackageName();
        android.util.ArrayMap<java.lang.String, java.lang.String> dstAttributionTags = new android.util.ArrayMap<>();
        android.util.ArraySet<java.lang.String> attributionTags = new android.util.ArraySet<>();
        attributionTags.add(null);
        if (pkg.getAttributions() != null) {
            int numAttributions = pkg.getAttributions().size();
            for (int attributionNum = 0; attributionNum < numAttributions; attributionNum++) {
                com.android.internal.pm.pkg.component.ParsedAttribution attribution = (com.android.internal.pm.pkg.component.ParsedAttribution) pkg.getAttributions().get(attributionNum);
                attributionTags.add(attribution.getTag());
                int numInheritFrom = attribution.getInheritFrom().size();
                for (int inheritFromNum = 0; inheritFromNum < numInheritFrom; inheritFromNum++) {
                    dstAttributionTags.put((java.lang.String) attribution.getInheritFrom().get(inheritFromNum), attribution.getTag());
                }
            }
        }
        com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.get(uid);
        if (uidState == null || (ops = uidState.pkgOps.get(pkgName2)) == null) {
            return;
        }
        ops.bypass = null;
        ops.knownAttributionTags.clear();
        int numOps = ops.size();
        for (int opNum = 0; opNum < numOps; opNum++) {
            com.android.server.appop.AppOpsService.Op op = ops.valueAt(opNum);
            for (int deviceIndex = op.mDeviceAttributedOps.size() - 1; deviceIndex >= 0; deviceIndex--) {
                android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = op.mDeviceAttributedOps.valueAt(deviceIndex);
                int tagIndex = attributedOps.size() - 1;
                while (tagIndex >= 0) {
                    java.lang.String tag = attributedOps.keyAt(tagIndex);
                    if (attributionTags.contains(tag)) {
                        pkgName = pkgName2;
                    } else {
                        java.lang.String newAttributionTag = dstAttributionTags.get(tag);
                        com.android.server.appop.AttributedOp newAttributedOp = op.getOrCreateAttribution(op, newAttributionTag, op.mDeviceAttributedOps.keyAt(deviceIndex));
                        pkgName = pkgName2;
                        newAttributedOp.add(attributedOps.get(tag));
                        attributedOps.remove(tag);
                        scheduleFastWriteLocked();
                    }
                    tagIndex--;
                    pkgName2 = pkgName;
                }
            }
        }
    }

    public void setAppOpsPolicy(android.app.AppOpsManagerInternal.CheckOpsDelegate policy) {
        com.android.server.appop.AppOpsService.CheckOpsDelegateDispatcher oldDispatcher = this.mCheckOpsDelegateDispatcher;
        android.app.AppOpsManagerInternal.CheckOpsDelegate delegate = oldDispatcher != null ? oldDispatcher.mCheckOpsDelegate : null;
        this.mCheckOpsDelegateDispatcher = new com.android.server.appop.AppOpsService.CheckOpsDelegateDispatcher(policy, delegate);
    }

    void packageRemoved(int uid, java.lang.String packageName) {
        synchronized (this) {
            packageRemovedLocked(uid, packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void packageRemovedLocked(int uid, java.lang.String packageName) {
        this.mHandler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda19
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.appop.HistoricalRegistry) obj).clearHistory(((java.lang.Integer) obj2).intValue(), (java.lang.String) obj3);
            }
        }, this.mHistoricalRegistry, java.lang.Integer.valueOf(uid), packageName));
        com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.get(uid);
        if (uidState == null) {
            return;
        }
        com.android.server.appop.AppOpsService.Ops removedOps = uidState.pkgOps.remove(packageName);
        this.mAppOpsCheckingService.removePackage(packageName, android.os.UserHandle.getUserId(uid));
        if (removedOps != null) {
            scheduleFastWriteLocked();
            int numOps = removedOps.size();
            for (int opNum = 0; opNum < numOps; opNum++) {
                com.android.server.appop.AppOpsService.Op op = removedOps.valueAt(opNum);
                for (int deviceIndex = 0; deviceIndex < op.mDeviceAttributedOps.size(); deviceIndex++) {
                    android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = op.mDeviceAttributedOps.valueAt(deviceIndex);
                    for (int tagIndex = 0; tagIndex < attributedOps.size(); tagIndex++) {
                        com.android.server.appop.AttributedOp attributedOp = attributedOps.valueAt(tagIndex);
                        while (attributedOp.isRunning()) {
                            attributedOp.finished(attributedOp.mInProgressEvents.keyAt(0));
                        }
                        while (attributedOp.isPaused()) {
                            attributedOp.finished(attributedOp.mPausedInProgressEvents.keyAt(0));
                        }
                    }
                }
            }
        }
    }

    public void uidRemoved(int uid) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String uidName = getPackageManagerInternal().getNameForUid(uid);
            if (uidName != null) {
                android.util.Slog.e(TAG, "Tried to remove existing UID. uid: " + uid + " name: " + uidName);
                return;
            }
            synchronized (this) {
                if (this.mUidStates.indexOfKey(uid) >= 0) {
                    this.mUidStates.get(uid).clear();
                    this.mUidStates.remove(uid);
                    scheduleFastWriteLocked();
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUidStateChanged(int uid, int state, boolean foregroundModeMayChange) {
        boolean z;
        boolean hasForegroundWatchers;
        int userId;
        java.lang.String[] uidPackageNames;
        boolean z2;
        char c;
        int cbi;
        java.lang.String[] uidPackageNames2;
        int code;
        java.lang.String[] uidPackageNames3;
        int pkgi;
        char c2;
        int code2;
        com.android.server.appop.OnOpModeChangedListener listener;
        int cbi2;
        synchronized (this) {
            if (state == Integer.MAX_VALUE) {
                onUidProcessDeathLocked(uid);
            }
            com.android.server.appop.AppOpsService.UidState uidState = getUidStateLocked(uid, false);
            int i = 0;
            while (true) {
                z = true;
                if (i >= this.mModeWatchers.size()) {
                    hasForegroundWatchers = false;
                    break;
                }
                com.android.server.appop.AppOpsService.ModeCallback cb = this.mModeWatchers.valueAt(i);
                if (!cb.isWatchingUid(uid) || (cb.getFlags() & 1) == 0) {
                    i++;
                } else {
                    hasForegroundWatchers = true;
                    break;
                }
            }
            if (uidState != null && foregroundModeMayChange && hasForegroundWatchers) {
                android.util.SparseBooleanArray foregroundOps = new android.util.SparseBooleanArray();
                android.util.SparseBooleanArray uidForegroundOps = this.mAppOpsCheckingService.getForegroundOps(uid, "default:0");
                for (int i2 = 0; i2 < uidForegroundOps.size(); i2++) {
                    foregroundOps.put(uidForegroundOps.keyAt(i2), true);
                }
                java.lang.String[] uidPackageNames4 = getPackagesForUid(uid);
                int userId2 = android.os.UserHandle.getUserId(uid);
                for (java.lang.String packageName : uidPackageNames4) {
                    android.util.SparseBooleanArray packageForegroundOps = this.mAppOpsCheckingService.getForegroundOps(packageName, userId2);
                    for (int i3 = 0; i3 < packageForegroundOps.size(); i3++) {
                        foregroundOps.put(packageForegroundOps.keyAt(i3), true);
                    }
                }
                int fgi = foregroundOps.size() - 1;
                while (fgi >= 0) {
                    if (!foregroundOps.valueAt(fgi)) {
                        userId = userId2;
                        uidPackageNames = uidPackageNames4;
                        z2 = z;
                    } else {
                        int code3 = foregroundOps.keyAt(fgi);
                        char c3 = 4;
                        if (this.mAppOpsCheckingService.getUidMode(uidState.uid, "default:0", code3) == android.app.AppOpsManager.opToDefaultMode(code3) || this.mAppOpsCheckingService.getUidMode(uidState.uid, "default:0", code3) != 4) {
                            int code4 = code3;
                            userId = userId2;
                            if (uidState.pkgOps.isEmpty()) {
                                uidPackageNames = uidPackageNames4;
                                z2 = true;
                            } else {
                                android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> listenerSet = this.mOpModeWatchers.get(code4);
                                if (listenerSet == null) {
                                    uidPackageNames = uidPackageNames4;
                                    z2 = true;
                                } else {
                                    int cbi3 = listenerSet.size() - 1;
                                    while (cbi3 >= 0) {
                                        com.android.server.appop.OnOpModeChangedListener listener2 = listenerSet.valueAt(cbi3);
                                        if ((listener2.getFlags() & 1) == 0) {
                                            c = c3;
                                            cbi = cbi3;
                                            uidPackageNames2 = uidPackageNames4;
                                            code = code4;
                                        } else if (listener2.isWatchingUid(uidState.uid)) {
                                            int pkgi2 = uidState.pkgOps.size() - 1;
                                            while (pkgi2 >= 0) {
                                                com.android.server.appop.AppOpsService.Op op = uidState.pkgOps.valueAt(pkgi2).get(code4);
                                                if (op == null) {
                                                    pkgi = pkgi2;
                                                    c2 = c3;
                                                    listener = listener2;
                                                    cbi2 = cbi3;
                                                    uidPackageNames3 = uidPackageNames4;
                                                    code2 = code4;
                                                } else {
                                                    com.android.server.appop.OnOpModeChangedListener listener3 = listener2;
                                                    uidPackageNames3 = uidPackageNames4;
                                                    if (this.mAppOpsCheckingService.getPackageMode(op.packageName, op.op, android.os.UserHandle.getUserId(op.uid)) == 4) {
                                                        pkgi = pkgi2;
                                                        c2 = 4;
                                                        listener = listener3;
                                                        cbi2 = cbi3;
                                                        code2 = code4;
                                                        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.appop.AppOpsService$$ExternalSyntheticLambda7(), this, listenerSet.valueAt(cbi3), java.lang.Integer.valueOf(code4), java.lang.Integer.valueOf(uidState.uid), uidState.pkgOps.keyAt(pkgi2), "default:0"));
                                                    } else {
                                                        pkgi = pkgi2;
                                                        c2 = 4;
                                                        code2 = code4;
                                                        listener = listener3;
                                                        cbi2 = cbi3;
                                                    }
                                                }
                                                pkgi2 = pkgi - 1;
                                                listener2 = listener;
                                                cbi3 = cbi2;
                                                uidPackageNames4 = uidPackageNames3;
                                                c3 = c2;
                                                code4 = code2;
                                            }
                                            c = c3;
                                            cbi = cbi3;
                                            uidPackageNames2 = uidPackageNames4;
                                            code = code4;
                                        } else {
                                            c = c3;
                                            cbi = cbi3;
                                            uidPackageNames2 = uidPackageNames4;
                                            code = code4;
                                        }
                                        cbi3 = cbi - 1;
                                        uidPackageNames4 = uidPackageNames2;
                                        c3 = c;
                                        code4 = code;
                                    }
                                    uidPackageNames = uidPackageNames4;
                                    z2 = true;
                                }
                            }
                        } else {
                            userId = userId2;
                            this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda6
                                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) throws java.lang.Throwable {
                                    ((com.android.server.appop.AppOpsService) obj).notifyOpChangedForAllPkgsInUid(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Boolean) obj4).booleanValue(), (java.lang.String) obj5);
                                }
                            }, this, java.lang.Integer.valueOf(code3), java.lang.Integer.valueOf(uidState.uid), java.lang.Boolean.valueOf(z), "default:0"));
                            uidPackageNames = uidPackageNames4;
                            z2 = true;
                        }
                    }
                    fgi--;
                    userId2 = userId;
                    z = z2;
                    uidPackageNames4 = uidPackageNames;
                }
            }
            if (state == Integer.MAX_VALUE) {
                return;
            }
            if (uidState != null) {
                int numPkgs = uidState.pkgOps.size();
                for (int pkgNum = 0; pkgNum < numPkgs; pkgNum++) {
                    com.android.server.appop.AppOpsService.Ops ops = uidState.pkgOps.valueAt(pkgNum);
                    int numOps = ops.size();
                    for (int opNum = 0; opNum < numOps; opNum++) {
                        com.android.server.appop.AppOpsService.Op op2 = ops.valueAt(opNum);
                        for (int deviceIndex = 0; deviceIndex < op2.mDeviceAttributedOps.size(); deviceIndex++) {
                            android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attributedOps = op2.mDeviceAttributedOps.valueAt(deviceIndex);
                            for (int tagIndex = 0; tagIndex < attributedOps.size(); tagIndex++) {
                                com.android.server.appop.AttributedOp attributedOp = attributedOps.valueAt(tagIndex);
                                attributedOp.onUidStateChanged(state);
                            }
                        }
                    }
                }
            }
        }
    }

    private void onUidProcessDeathLocked(int uid) {
        if (!this.mUidStates.contains(uid) || !com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.finishRunningOpsForKilledPackages()) {
            return;
        }
        final android.util.SparseLongArray chainsToFinish = new android.util.SparseLongArray();
        doForAllAttributedOpsInUidLocked(uid, new java.util.function.Consumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.appop.AttributedOp attributedOp = (com.android.server.appop.AttributedOp) obj;
                attributedOp.doForAllInProgressStartOpEvents(new java.util.function.Consumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj2) {
                        com.android.server.appop.AppOpsService.lambda$onUidProcessDeathLocked$2(sparseLongArray, attributedOp, (com.android.server.appop.AttributedOp.InProgressStartOpEvent) obj2);
                    }
                });
            }
        });
        finishChainsLocked(chainsToFinish);
    }

    static /* synthetic */ void lambda$onUidProcessDeathLocked$2(android.util.SparseLongArray chainsToFinish, com.android.server.appop.AttributedOp attributedOp, com.android.server.appop.AttributedOp.InProgressStartOpEvent event) {
        if (event == null) {
            return;
        }
        int chainId = event.getAttributionChainId();
        if (chainId != -1) {
            long currentEarliestStartTime = chainsToFinish.get(chainId, Long.MAX_VALUE);
            if (event.getStartTime() < currentEarliestStartTime) {
                chainsToFinish.put(chainId, event.getStartTime());
            }
        }
        attributedOp.finished(event.getClientId());
    }

    private void finishChainsLocked(final android.util.SparseLongArray chainsToFinish) {
        doForAllAttributedOpsLocked(new java.util.function.Consumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.appop.AttributedOp attributedOp = (com.android.server.appop.AttributedOp) obj;
                attributedOp.doForAllInProgressStartOpEvents(new java.util.function.Consumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda15
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj2) {
                        com.android.server.appop.AppOpsService.lambda$finishChainsLocked$4(sparseLongArray, attributedOp, (com.android.server.appop.AttributedOp.InProgressStartOpEvent) obj2);
                    }
                });
            }
        });
    }

    static /* synthetic */ void lambda$finishChainsLocked$4(android.util.SparseLongArray chainsToFinish, com.android.server.appop.AttributedOp attributedOp, com.android.server.appop.AttributedOp.InProgressStartOpEvent event) {
        int chainId = event.getAttributionChainId();
        long earliestEventStart = chainsToFinish.get(chainId, Long.MAX_VALUE);
        if (chainId != -1 && event.getStartTime() >= earliestEventStart) {
            attributedOp.finished(event.getClientId());
        }
    }

    private void doForAllAttributedOpsLocked(java.util.function.Consumer<com.android.server.appop.AttributedOp> action) {
        int numUids = this.mUidStates.size();
        for (int uidNum = 0; uidNum < numUids; uidNum++) {
            int uid = this.mUidStates.keyAt(uidNum);
            doForAllAttributedOpsInUidLocked(uid, action);
        }
    }

    private void doForAllAttributedOpsInUidLocked(int uid, java.util.function.Consumer<com.android.server.appop.AttributedOp> action) {
        com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.get(uid);
        if (uidState == null) {
            return;
        }
        int numPkgs = uidState.pkgOps.size();
        for (int pkgNum = 0; pkgNum < numPkgs; pkgNum++) {
            com.android.server.appop.AppOpsService.Ops ops = uidState.pkgOps.valueAt(pkgNum);
            int numOps = ops.size();
            for (int opNum = 0; opNum < numOps; opNum++) {
                com.android.server.appop.AppOpsService.Op op = ops.valueAt(opNum);
                int numDevices = op.mDeviceAttributedOps.size();
                for (int deviceNum = 0; deviceNum < numDevices; deviceNum++) {
                    android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> attrOps = op.mDeviceAttributedOps.valueAt(deviceNum);
                    int numAttributions = attrOps.size();
                    for (int attrNum = 0; attrNum < numAttributions; attrNum++) {
                        action.accept(attrOps.valueAt(attrNum));
                    }
                }
            }
        }
    }

    public void updateUidProcState(int uid, int procState, int capability) {
        synchronized (this) {
            getUidStateTracker().updateUidProcState(uid, procState, capability);
        }
    }

    public void shutdown() throws java.io.IOException {
        android.util.Slog.w(TAG, "Writing app ops before shutdown...");
        boolean doWrite = false;
        synchronized (this) {
            if (this.mWriteScheduled) {
                this.mWriteScheduled = false;
                this.mFastWriteScheduled = false;
                this.mHandler.removeCallbacks(this.mWriteRunner);
                doWrite = true;
            }
        }
        if (doWrite) {
            writeRecentAccesses();
        }
        this.mAppOpsCheckingService.shutdown();
        this.mHistoricalRegistry.shutdown();
    }

    private java.util.ArrayList<android.app.AppOpsManager.OpEntry> collectOps(com.android.server.appop.AppOpsService.Ops pkgOps, int[] ops, java.lang.String persistentDeviceId) {
        java.util.ArrayList<android.app.AppOpsManager.OpEntry> resOps = null;
        boolean shouldReturnRestrictedAppOps = this.mContext.checkPermission("android.permission.GET_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) == 0;
        if (ops == null) {
            resOps = new java.util.ArrayList<>();
            for (int j = 0; j < pkgOps.size(); j++) {
                com.android.server.appop.AppOpsService.Op curOp = pkgOps.valueAt(j);
                if (curOp.op < 0 || curOp.op >= 10004) {
                    android.util.Slog.i(TAG, "collectOps skip invalid op:" + curOp.op);
                } else if (!android.app.AppOpsManager.opRestrictsRead(curOp.op) || shouldReturnRestrictedAppOps) {
                    resOps.add(getOpEntryForResult(curOp, persistentDeviceId));
                }
            }
        } else {
            for (int i : ops) {
                com.android.server.appop.AppOpsService.Op curOp2 = pkgOps.get(i);
                if (curOp2 != null) {
                    if (resOps == null) {
                        resOps = new java.util.ArrayList<>();
                    }
                    if (curOp2.op < 0 || curOp2.op >= 10004) {
                        android.util.Slog.i(TAG, "collectOps skip invalid op:" + curOp2.op);
                    } else if (!android.app.AppOpsManager.opRestrictsRead(curOp2.op) || shouldReturnRestrictedAppOps) {
                        resOps.add(getOpEntryForResult(curOp2, persistentDeviceId));
                    }
                }
            }
        }
        return resOps;
    }

    private java.util.ArrayList<android.app.AppOpsManager.OpEntry> collectUidOps(com.android.server.appop.AppOpsService.UidState uidState, int[] ops) {
        int opModeCount;
        android.util.SparseIntArray opModes = this.mAppOpsCheckingService.getNonDefaultUidModes(uidState.uid, "default:0");
        if (opModes == null || (opModeCount = opModes.size()) == 0) {
            return null;
        }
        java.util.ArrayList<android.app.AppOpsManager.OpEntry> resOps = null;
        if (ops == null) {
            resOps = new java.util.ArrayList<>();
            for (int i = 0; i < opModeCount; i++) {
                int code = opModes.keyAt(i);
                resOps.add(new android.app.AppOpsManager.OpEntry(code, opModes.get(code), java.util.Collections.emptyMap()));
            }
        } else {
            for (int code2 : ops) {
                if (opModes.indexOfKey(code2) >= 0) {
                    if (resOps == null) {
                        resOps = new java.util.ArrayList<>();
                    }
                    resOps.add(new android.app.AppOpsManager.OpEntry(code2, opModes.get(code2), java.util.Collections.emptyMap()));
                }
            }
        }
        return resOps;
    }

    private static android.app.AppOpsManager.OpEntry getOpEntryForResult(com.android.server.appop.AppOpsService.Op op, java.lang.String persistentDeviceId) {
        return op.createEntryLocked(persistentDeviceId);
    }

    public java.util.List<android.app.AppOpsManager.PackageOps> getPackagesForOps(int[] ops) {
        return getPackagesForOpsForDevice(ops, "default:0");
    }

    public java.util.List<android.app.AppOpsManager.PackageOps> getPackagesForOpsForDevice(int[] ops, java.lang.String persistentDeviceId) throws java.lang.Throwable {
        int uidStateCount;
        int uidStateCount2;
        int callingUid = android.os.Binder.getCallingUid();
        boolean hasAllPackageAccess = this.mContext.checkPermission("android.permission.GET_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null) == 0;
        java.util.ArrayList<android.app.AppOpsManager.PackageOps> res = null;
        synchronized (this) {
            try {
                int uidStateCount3 = this.mUidStates.size();
                int i = 0;
                while (i < uidStateCount3) {
                    com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.valueAt(i);
                    if (!uidState.pkgOps.isEmpty() && (hasAllPackageAccess || callingUid == uidState.uid)) {
                        android.util.ArrayMap<java.lang.String, com.android.server.appop.AppOpsService.Ops> packages = uidState.pkgOps;
                        int packageCount = packages.size();
                        int j = 0;
                        while (j < packageCount) {
                            com.android.server.appop.AppOpsService.Ops pkgOps = packages.valueAt(j);
                            java.util.ArrayList<android.app.AppOpsManager.OpEntry> resOps = collectOps(pkgOps, ops, persistentDeviceId);
                            if (resOps == null) {
                                uidStateCount2 = uidStateCount3;
                            } else {
                                if (res == null) {
                                    res = new java.util.ArrayList<>();
                                }
                                uidStateCount2 = uidStateCount3;
                                android.app.AppOpsManager.PackageOps resPackage = new android.app.AppOpsManager.PackageOps(pkgOps.packageName, pkgOps.uidState.uid, resOps);
                                res.add(resPackage);
                            }
                            j++;
                            uidStateCount3 = uidStateCount2;
                        }
                        uidStateCount = uidStateCount3;
                    } else {
                        uidStateCount = uidStateCount3;
                    }
                    try {
                        i++;
                        uidStateCount3 = uidStateCount;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                return res;
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    public java.util.List<android.app.AppOpsManager.PackageOps> getOpsForPackage(int uid, java.lang.String packageName, int[] ops) {
        enforceGetAppOpsStatsPermissionIfNeeded(uid, packageName);
        java.lang.String resolvedPackageName = android.app.AppOpsManager.resolvePackageName(uid, packageName);
        if (resolvedPackageName == null) {
            return java.util.Collections.emptyList();
        }
        synchronized (this) {
            com.android.server.appop.AppOpsService.Ops pkgOps = getOpsLocked(uid, resolvedPackageName, null, false, null, false);
            if (pkgOps == null) {
                return null;
            }
            java.util.ArrayList<android.app.AppOpsManager.OpEntry> resOps = collectOps(pkgOps, ops, "default:0");
            if (resOps != null && resOps.size() != 0) {
                java.util.ArrayList<android.app.AppOpsManager.PackageOps> res = new java.util.ArrayList<>();
                android.app.AppOpsManager.PackageOps resPackage = new android.app.AppOpsManager.PackageOps(pkgOps.packageName, pkgOps.uidState.uid, resOps);
                res.add(resPackage);
                return res;
            }
            return null;
        }
    }

    private void enforceGetAppOpsStatsPermissionIfNeeded(int uid, java.lang.String packageName) {
        int callingPid = android.os.Binder.getCallingPid();
        if (callingPid == android.os.Process.myPid()) {
            return;
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (uid == callingUid && packageName != null && checkPackage(uid, packageName) == 0) {
            return;
        }
        this.mContext.enforcePermission("android.permission.GET_APP_OPS_STATS", callingPid, callingUid, null);
    }

    private void ensureHistoricalOpRequestIsValid(int uid, java.lang.String packageName, java.lang.String attributionTag, java.util.List<java.lang.String> opNames, int filter, long beginTimeMillis, long endTimeMillis, int flags) {
        if ((filter & 1) != 0) {
            com.android.internal.util.Preconditions.checkArgument(uid != -1);
        } else {
            com.android.internal.util.Preconditions.checkArgument(uid == -1);
        }
        if ((filter & 2) != 0) {
            java.util.Objects.requireNonNull(packageName);
        } else {
            com.android.internal.util.Preconditions.checkArgument(packageName == null);
        }
        if ((filter & 4) == 0) {
            com.android.internal.util.Preconditions.checkArgument(attributionTag == null);
        }
        if ((filter & 8) != 0) {
            java.util.Objects.requireNonNull(opNames);
        } else {
            com.android.internal.util.Preconditions.checkArgument(opNames == null);
        }
        com.android.internal.util.Preconditions.checkFlagsArgument(filter, 15);
        com.android.internal.util.Preconditions.checkArgumentNonnegative(beginTimeMillis);
        com.android.internal.util.Preconditions.checkArgument(endTimeMillis > beginTimeMillis);
        com.android.internal.util.Preconditions.checkFlagsArgument(flags, 31);
    }

    public void getHistoricalOps(int uid, java.lang.String packageName, java.lang.String attributionTag, java.util.List<java.lang.String> opNames, int dataType, int filter, final long beginTimeMillis, final long endTimeMillis, int flags, final android.os.RemoteCallback callback) {
        java.lang.String[] opNamesArray;
        java.util.Set<java.lang.String> attributionChainExemptPackages;
        java.lang.String[] chainExemptPkgArray;
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        ensureHistoricalOpRequestIsValid(uid, packageName, attributionTag, opNames, filter, beginTimeMillis, endTimeMillis, flags);
        java.util.Objects.requireNonNull(callback, "callback cannot be null");
        android.app.ActivityManagerInternal ami = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        boolean isSelfRequest = (filter & 1) != 0 && uid == android.os.Binder.getCallingUid();
        if (!isSelfRequest) {
            boolean isCallerInstrumented = ami.getInstrumentationSourceUid(android.os.Binder.getCallingUid()) != -1;
            boolean isCallerSystem = android.os.Binder.getCallingPid() == android.os.Process.myPid();
            try {
                boolean isCallerPermissionController = pm.getPackageUidAsUser(this.mContext.getPackageManager().getPermissionControllerPackageName(), 0, android.os.UserHandle.getUserId(android.os.Binder.getCallingUid())) == android.os.Binder.getCallingUid();
                boolean doesCallerHavePermission = this.mContext.checkPermission("android.permission.GET_HISTORICAL_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) == 0;
                if (!isCallerSystem && !isCallerInstrumented && !isCallerPermissionController && !doesCallerHavePermission) {
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            callback.sendResult(new android.os.Bundle());
                        }
                    });
                    return;
                }
                this.mContext.enforcePermission("android.permission.GET_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), "getHistoricalOps");
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return;
            }
        }
        if (opNames == null) {
            opNamesArray = null;
        } else {
            opNamesArray = (java.lang.String[]) opNames.toArray(new java.lang.String[opNames.size()]);
        }
        if ((dataType & 4) == 0) {
            attributionChainExemptPackages = null;
        } else {
            java.util.Set<java.lang.String> attributionChainExemptPackages2 = android.permission.PermissionManager.getIndicatorExemptedPackages(this.mContext);
            attributionChainExemptPackages = attributionChainExemptPackages2;
        }
        if (attributionChainExemptPackages != null) {
            chainExemptPkgArray = (java.lang.String[]) attributionChainExemptPackages.toArray(new java.lang.String[attributionChainExemptPackages.size()]);
        } else {
            chainExemptPkgArray = null;
        }
        this.mHandler.post(com.android.internal.util.FunctionalUtils.handleExceptions(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.DodecConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda21
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9, java.lang.Object obj10, java.lang.Object obj11, java.lang.Object obj12) throws java.lang.Throwable {
                ((com.android.server.appop.HistoricalRegistry) obj).getHistoricalOps(((java.lang.Integer) obj2).intValue(), (java.lang.String) obj3, (java.lang.String) obj4, (java.lang.String[]) obj5, ((java.lang.Integer) obj6).intValue(), ((java.lang.Integer) obj7).intValue(), ((java.lang.Long) obj8).longValue(), ((java.lang.Long) obj9).longValue(), ((java.lang.Integer) obj10).intValue(), (java.lang.String[]) obj11, (android.os.RemoteCallback) obj12);
            }
        }, this.mHistoricalRegistry, java.lang.Integer.valueOf(uid), packageName, attributionTag, opNamesArray, java.lang.Integer.valueOf(dataType), java.lang.Integer.valueOf(filter), java.lang.Long.valueOf(beginTimeMillis), java.lang.Long.valueOf(endTimeMillis), java.lang.Integer.valueOf(flags), chainExemptPkgArray, callback).recycleOnUse(), new java.util.function.Consumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getHistoricalOps$7(beginTimeMillis, endTimeMillis, callback, (java.lang.Throwable) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getHistoricalOps$7(long beginTimeMillis, long endTimeMillis, android.os.RemoteCallback callback, java.lang.Throwable e) {
        this.mAppOpsServiceExt.handleOpsException(this.mContext, android.os.Binder.getCallingUid(), beginTimeMillis, endTimeMillis, this.mHandler, callback);
    }

    public void getHistoricalOpsFromDiskRaw(int uid, java.lang.String packageName, java.lang.String attributionTag, java.util.List<java.lang.String> opNames, int dataType, int filter, long beginTimeMillis, long endTimeMillis, int flags, android.os.RemoteCallback callback) {
        java.lang.String[] opNamesArray;
        java.lang.String[] chainExemptPkgArray;
        ensureHistoricalOpRequestIsValid(uid, packageName, attributionTag, opNames, filter, beginTimeMillis, endTimeMillis, flags);
        java.util.Objects.requireNonNull(callback, "callback cannot be null");
        this.mContext.enforcePermission("android.permission.MANAGE_APPOPS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), "getHistoricalOps");
        if (opNames == null) {
            opNamesArray = null;
        } else {
            opNamesArray = (java.lang.String[]) opNames.toArray(new java.lang.String[opNames.size()]);
        }
        java.util.Set<java.lang.String> attributionChainExemptPackages = null;
        if ((dataType & 4) != 0) {
            attributionChainExemptPackages = android.permission.PermissionManager.getIndicatorExemptedPackages(this.mContext);
        }
        if (attributionChainExemptPackages != null) {
            chainExemptPkgArray = (java.lang.String[]) attributionChainExemptPackages.toArray(new java.lang.String[attributionChainExemptPackages.size()]);
        } else {
            chainExemptPkgArray = null;
        }
        this.mHandler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.DodecConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda0
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9, java.lang.Object obj10, java.lang.Object obj11, java.lang.Object obj12) throws java.lang.Throwable {
                ((com.android.server.appop.HistoricalRegistry) obj).getHistoricalOpsFromDiskRaw(((java.lang.Integer) obj2).intValue(), (java.lang.String) obj3, (java.lang.String) obj4, (java.lang.String[]) obj5, ((java.lang.Integer) obj6).intValue(), ((java.lang.Integer) obj7).intValue(), ((java.lang.Long) obj8).longValue(), ((java.lang.Long) obj9).longValue(), ((java.lang.Integer) obj10).intValue(), (java.lang.String[]) obj11, (android.os.RemoteCallback) obj12);
            }
        }, this.mHistoricalRegistry, java.lang.Integer.valueOf(uid), packageName, attributionTag, opNamesArray, java.lang.Integer.valueOf(dataType), java.lang.Integer.valueOf(filter), java.lang.Long.valueOf(beginTimeMillis), java.lang.Long.valueOf(endTimeMillis), java.lang.Integer.valueOf(flags), chainExemptPkgArray, callback).recycleOnUse());
    }

    public void reloadNonHistoricalState() {
        this.mContext.enforcePermission("android.permission.MANAGE_APPOPS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), "reloadNonHistoricalState");
        this.mAppOpsCheckingService.writeState();
        this.mAppOpsCheckingService.readState();
    }

    void readState() {
        this.mAppOpsCheckingService.readState();
    }

    public java.util.List<android.app.AppOpsManager.PackageOps> getUidOps(int uid, int[] ops) {
        this.mContext.enforcePermission("android.permission.GET_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null);
        synchronized (this) {
            com.android.server.appop.AppOpsService.UidState uidState = getUidStateLocked(uid, false);
            if (uidState == null) {
                return null;
            }
            java.util.ArrayList<android.app.AppOpsManager.OpEntry> resOps = collectUidOps(uidState, ops);
            if (resOps == null) {
                return null;
            }
            java.util.ArrayList<android.app.AppOpsManager.PackageOps> res = new java.util.ArrayList<>();
            android.app.AppOpsManager.PackageOps resPackage = new android.app.AppOpsManager.PackageOps((java.lang.String) null, uidState.uid, resOps);
            res.add(resPackage);
            return res;
        }
    }

    private void pruneOpLocked(com.android.server.appop.AppOpsService.Op op, int uid, java.lang.String packageName) {
        com.android.server.appop.AppOpsService.Ops ops;
        com.android.server.appop.AppOpsService.UidState uidState;
        android.util.ArrayMap<java.lang.String, com.android.server.appop.AppOpsService.Ops> pkgOps;
        op.removeAttributionsWithNoTime();
        if (op.mDeviceAttributedOps.isEmpty() && (ops = getOpsLocked(uid, packageName, null, false, null, false)) != null) {
            ops.remove(op.op);
            this.mAppOpsCheckingService.setPackageMode(packageName, op.op, android.app.AppOpsManager.opToDefaultMode(op.op), android.os.UserHandle.getUserId(op.uid));
            if (ops.size() <= 0 && (pkgOps = (uidState = ops.uidState).pkgOps) != null) {
                pkgOps.remove(ops.packageName);
                this.mAppOpsCheckingService.removePackage(ops.packageName, android.os.UserHandle.getUserId(uidState.uid));
            }
        }
    }

    private void enforceManageAppOpsModes(int callingPid, int callingUid, int targetUid) {
        if (callingPid == android.os.Process.myPid()) {
            return;
        }
        int callingUser = android.os.UserHandle.getUserId(callingUid);
        synchronized (this) {
            if (this.mProfileOwners == null || this.mProfileOwners.get(callingUser, -1) != callingUid || targetUid < 0 || callingUser != android.os.UserHandle.getUserId(targetUid)) {
                this.mContext.enforcePermission("android.permission.MANAGE_APP_OPS_MODES", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null);
            }
        }
    }

    public void setUidMode(int code, int uid, int mode) throws java.lang.Throwable {
        setUidMode(code, uid, mode, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUidMode(int code, int uid, int mode, com.android.internal.app.IAppOpsCallback permissionPolicyCallback) throws java.lang.Throwable {
        int previousMode;
        enforceManageAppOpsModes(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), uid);
        verifyIncomingOp(code);
        int code2 = android.app.AppOpsManager.opToSwitch(code);
        if (permissionPolicyCallback == null) {
            updatePermissionRevokedCompat(uid, code2, mode);
        }
        synchronized (this) {
            int defaultMode = android.app.AppOpsManager.opToDefaultMode(code2);
            com.android.server.appop.AppOpsService.UidState uidState = getUidStateLocked(uid, false);
            if (uidState == null) {
                if (mode == defaultMode) {
                    return;
                }
                if (uid >= 10000) {
                    android.util.Slog.e(TAG, "Trying to set mode for unknown uid " + uid + ".");
                }
                uidState = new com.android.server.appop.AppOpsService.UidState(uid);
                this.mUidStates.put(uid, uidState);
            }
            if (this.mAppOpsCheckingService.getUidMode(uidState.uid, "default:0", code2) != android.app.AppOpsManager.opToDefaultMode(code2)) {
                previousMode = this.mAppOpsCheckingService.getUidMode(uidState.uid, "default:0", code2);
            } else {
                previousMode = 3;
            }
            this.mIgnoredCallback = permissionPolicyCallback;
            if (this.mAppOpsCheckingService.setUidMode(uidState.uid, "default:0", code2, mode)) {
                if (mode != 2 && mode != previousMode) {
                    boolean z = true;
                    if (mode != 1) {
                        z = false;
                    }
                    updateStartedOpModeForUidForDefaultDeviceLocked(code2, z, uid);
                }
                if (this.mAppOpsServiceExt.shouldLog(null, code2, 0)) {
                    android.util.Slog.i(TAG, "setUidMode for uid = " + uid + "; OP_" + android.app.AppOpsManager.opToName(code2) + " = " + android.app.AppOpsManager.modeToName(mode) + "; calling uid = " + android.os.Binder.getCallingUid() + "; calling pid = " + android.os.Binder.getCallingPid());
                }
                notifyStorageManagerOpModeChangedSync(code2, uid, null, mode, previousMode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOpChangedForAllPkgsInUid(int code, int uid, boolean onlyForeground, java.lang.String persistentDeviceId) throws java.lang.Throwable {
        android.util.ArrayMap<com.android.server.appop.OnOpModeChangedListener, android.util.ArraySet<java.lang.String>> callbackSpecs;
        java.lang.String[] uidPackageNames = getPackagesForUid(uid);
        android.util.ArrayMap<com.android.server.appop.OnOpModeChangedListener, android.util.ArraySet<java.lang.String>> callbackSpecs2 = null;
        synchronized (this) {
            try {
                try {
                    android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> callbacks = this.mOpModeWatchers.get(code);
                    if (callbacks != null) {
                        try {
                            int callbackCount = callbacks.size();
                            for (int i = 0; i < callbackCount; i++) {
                                com.android.server.appop.OnOpModeChangedListener callback = callbacks.valueAt(i);
                                try {
                                    if (callback.isWatchingUid(uid) && (!onlyForeground || (callback.getFlags() & 1) != 0)) {
                                        android.util.ArraySet<java.lang.String> changedPackages = new android.util.ArraySet<>();
                                        java.util.Collections.addAll(changedPackages, uidPackageNames);
                                        if (callbackSpecs2 == null) {
                                            callbackSpecs2 = new android.util.ArrayMap<>();
                                        }
                                        callbackSpecs2.put(callback, changedPackages);
                                    }
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
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    }
                    try {
                        callbackSpecs = callbackSpecs2;
                        for (java.lang.String uidPackageName : uidPackageNames) {
                            try {
                                android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> callbacks2 = this.mPackageModeWatchers.get(uidPackageName);
                                if (callbacks2 != null) {
                                    if (callbackSpecs == null) {
                                        callbackSpecs = new android.util.ArrayMap<>();
                                    }
                                    int callbackCount2 = callbacks2.size();
                                    for (int i2 = 0; i2 < callbackCount2; i2++) {
                                        com.android.server.appop.OnOpModeChangedListener callback2 = callbacks2.valueAt(i2);
                                        if (!onlyForeground || (callback2.getFlags() & 1) != 0) {
                                            android.util.ArraySet<java.lang.String> changedPackages2 = callbackSpecs.get(callback2);
                                            if (changedPackages2 == null) {
                                                changedPackages2 = new android.util.ArraySet<>();
                                                callbackSpecs.put(callback2, changedPackages2);
                                            }
                                            changedPackages2.add(uidPackageName);
                                        }
                                    }
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                while (true) {
                                    throw th;
                                }
                            }
                        }
                        if (callbackSpecs != null && this.mIgnoredCallback != null) {
                            callbackSpecs.remove(this.mModeWatchers.get(this.mIgnoredCallback.asBinder()));
                        }
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                    }
                } catch (java.lang.Throwable th6) {
                    th = th6;
                }
            } catch (java.lang.Throwable th7) {
                th = th7;
            }
            try {
                if (callbackSpecs == null) {
                    return;
                }
                int i3 = 0;
                while (i3 < callbackSpecs.size()) {
                    com.android.server.appop.OnOpModeChangedListener callback3 = callbackSpecs.keyAt(i3);
                    android.util.ArraySet<java.lang.String> reportedPackageNames = callbackSpecs.valueAt(i3);
                    if (reportedPackageNames == null) {
                        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.appop.AppOpsService$$ExternalSyntheticLambda7(), this, callback3, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), (java.lang.Object) null, persistentDeviceId));
                    } else {
                        int reportedPackageCount = reportedPackageNames.size();
                        int j = 0;
                        while (j < reportedPackageCount) {
                            java.lang.String reportedPackageName = reportedPackageNames.valueAt(j);
                            this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.appop.AppOpsService$$ExternalSyntheticLambda7(), this, callback3, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), reportedPackageName, persistentDeviceId));
                            j++;
                            uidPackageNames = uidPackageNames;
                        }
                    }
                    i3++;
                    uidPackageNames = uidPackageNames;
                }
            } catch (java.lang.Throwable th8) {
                th = th8;
                while (true) {
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOpChangedForPkg(java.lang.String packageName, int code, int mode, int userId) throws java.lang.Throwable {
        android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> repCbs;
        synchronized (this) {
            try {
                android.util.ArraySet<? extends com.android.server.appop.OnOpModeChangedListener> arraySet = this.mOpModeWatchers.get(code);
                if (arraySet != null) {
                    repCbs = 0 == 0 ? new android.util.ArraySet<>() : null;
                    repCbs.addAll(arraySet);
                }
                android.util.ArraySet<? extends com.android.server.appop.OnOpModeChangedListener> arraySet2 = this.mPackageModeWatchers.get(packageName);
                if (arraySet2 == null) {
                    repCbs = repCbs;
                } else {
                    if (repCbs == null) {
                        repCbs = new android.util.ArraySet<>();
                    }
                    repCbs.addAll(arraySet2);
                    repCbs = repCbs;
                }
                if (repCbs != null) {
                    try {
                        if (this.mIgnoredCallback != null) {
                            repCbs.remove(this.mModeWatchers.get(this.mIgnoredCallback.asBinder()));
                        }
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
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
            try {
                int uid = getPackageManagerInternal().getPackageUid(packageName, 4202496L, userId);
                try {
                    com.android.server.appop.AppOpsService.Op op = getOpLocked(code, uid, packageName, null, false, null, false);
                    if (op != null && mode == android.app.AppOpsManager.opToDefaultMode(op.op)) {
                        pruneOpLocked(op, uid, packageName);
                    }
                    scheduleFastWriteLocked();
                    if (mode != 2) {
                        boolean z = true;
                        if (mode != 1) {
                            z = false;
                        }
                        updateStartedOpModeForUidForDefaultDeviceLocked(code, z, uid);
                    }
                    if (repCbs != null && uid != -1) {
                        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HexConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda18
                            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                                ((com.android.server.appop.AppOpsService) obj).notifyOpChanged((android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener>) obj2, ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), (java.lang.String) obj5, (java.lang.String) obj6);
                            }
                        }, this, repCbs, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), packageName, "default:0"));
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    while (true) {
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
                while (true) {
                    throw th;
                }
            }
        }
    }

    private void updatePermissionRevokedCompat(int uid, int switchCode, int mode) throws java.lang.Throwable {
        int i;
        int i2;
        java.lang.String[] packageNames;
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        java.lang.String permissionName;
        boolean isRevokedCompat;
        long identity;
        java.lang.String str4;
        java.lang.String str5;
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager == null) {
            return;
        }
        java.lang.String[] packageNames2 = packageManager.getPackagesForUid(uid);
        if (com.android.internal.util.ArrayUtils.isEmpty(packageNames2)) {
            return;
        }
        int i3 = 0;
        java.lang.String packageName = packageNames2[0];
        int[] ops = this.mSwitchedOps.get(switchCode);
        int length = ops.length;
        int i4 = 0;
        while (i4 < length) {
            int code = ops[i4];
            java.lang.String permissionName2 = android.app.AppOpsManager.opToPermission(code);
            if (permissionName2 == null) {
                i = i4;
                i2 = length;
                packageNames = packageNames2;
            } else if (packageManager.checkPermission(permissionName2, packageName) != 0) {
                i = i4;
                i2 = length;
                packageNames = packageNames2;
            } else {
                try {
                    android.content.pm.PermissionInfo permissionInfo = packageManager.getPermissionInfo(permissionName2, i3);
                    if (!permissionInfo.isRuntime()) {
                        i = i4;
                        i2 = length;
                        packageNames = packageNames2;
                    } else {
                        int i5 = getPackageManagerInternal().getUidTargetSdkVersion(uid) >= 23 ? 1 : i3;
                        android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(uid);
                        if (permissionInfo.backgroundPermission != null) {
                            if (packageManager.checkPermission(permissionInfo.backgroundPermission, packageName) != 0) {
                                str = ", mode=";
                                str2 = ", permission=";
                                str3 = "setUidMode() called with a mode inconsistent with runtime permission state, this is discouraged and you should revoke the runtime permission instead: uid=";
                                i2 = length;
                                packageNames = packageNames2;
                                permissionName = permissionName2;
                                i = i4;
                            } else {
                                boolean isBackgroundRevokedCompat = mode != 0;
                                if (!isBackgroundRevokedCompat || i5 == 0) {
                                    str4 = ", mode=";
                                } else {
                                    str4 = ", mode=";
                                    android.util.Slog.w(TAG, "setUidMode() called with a mode inconsistent with runtime permission state, this is discouraged and you should revoke the runtime permission instead: uid=" + uid + ", switchCode=" + switchCode + ", mode=" + mode + ", permission=" + permissionInfo.backgroundPermission);
                                }
                                identity = android.os.Binder.clearCallingIdentity();
                                try {
                                    str5 = permissionInfo.backgroundPermission;
                                    str = str4;
                                    str2 = ", permission=";
                                    str3 = "setUidMode() called with a mode inconsistent with runtime permission state, this is discouraged and you should revoke the runtime permission instead: uid=";
                                    packageNames = packageNames2;
                                    permissionName = permissionName2;
                                    i = i4;
                                    i2 = length;
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                }
                                try {
                                    packageManager.updatePermissionFlags(str5, packageName, 8, isBackgroundRevokedCompat ? 8 : 0, user);
                                    android.os.Binder.restoreCallingIdentity(identity);
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            }
                            isRevokedCompat = (mode == 0 || mode == 4) ? false : true;
                        } else {
                            str = ", mode=";
                            str2 = ", permission=";
                            str3 = "setUidMode() called with a mode inconsistent with runtime permission state, this is discouraged and you should revoke the runtime permission instead: uid=";
                            i2 = length;
                            packageNames = packageNames2;
                            permissionName = permissionName2;
                            i = i4;
                            isRevokedCompat = mode != 0;
                        }
                        if (isRevokedCompat && i5 != 0) {
                            android.util.Slog.w(TAG, str3 + uid + ", switchCode=" + switchCode + str + mode + str2 + permissionName);
                        }
                        identity = android.os.Binder.clearCallingIdentity();
                        try {
                            packageManager.updatePermissionFlags(permissionName, packageName, 8, isRevokedCompat ? 8 : 0, user);
                        } finally {
                            android.os.Binder.restoreCallingIdentity(identity);
                        }
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    i = i4;
                    i2 = length;
                    packageNames = packageNames2;
                    e.printStackTrace();
                }
            }
            i4 = i + 1;
            i3 = 0;
            length = i2;
            packageNames2 = packageNames;
        }
    }

    private void notifyStorageManagerOpModeChangedSync(int code, int uid, java.lang.String packageName, int mode, int previousMode) {
        android.os.storage.StorageManagerInternal storageManagerInternal = (android.os.storage.StorageManagerInternal) com.android.server.LocalServices.getService(android.os.storage.StorageManagerInternal.class);
        if (storageManagerInternal != null) {
            storageManagerInternal.onAppOpsChanged(code, uid, packageName, mode, previousMode);
        }
    }

    public void setMode(int code, int uid, java.lang.String packageName, int mode) {
        setMode(code, uid, packageName, mode, null);
    }

    void setMode(int code, int uid, java.lang.String packageName, int mode, com.android.internal.app.IAppOpsCallback permissionPolicyCallback) {
        if (uid == -1 && packageName == null) {
            android.util.Slog.w(TAG, "params error. uid is -1 and pkg is null");
            return;
        }
        enforceManageAppOpsModes(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), uid);
        verifyIncomingOp(code);
        if (!isIncomingPackageValid(packageName, android.os.UserHandle.getUserId(uid))) {
            return;
        }
        int code2 = android.app.AppOpsManager.opToSwitch(code);
        try {
            com.android.server.appop.AppOpsService.PackageVerificationResult pvr = verifyAndGetBypass(uid, packageName, null);
            int previousMode = 3;
            synchronized (this) {
                com.android.server.appop.AppOpsService.Op op = getOpLocked(code2, uid, packageName, null, false, pvr.bypass, true);
                if (op != null && this.mAppOpsCheckingService.getPackageMode(op.packageName, op.op, android.os.UserHandle.getUserId(op.uid)) != mode) {
                    if (this.mAppOpsServiceExt.shouldLog(packageName, code2, 0)) {
                        android.util.Slog.i(TAG, "setMode for pkg = " + packageName + "; uid = " + uid + "; OP_" + android.app.AppOpsManager.opToName(code2) + " = " + android.app.AppOpsManager.modeToName(mode) + "; calling uid = " + android.os.Binder.getCallingUid() + "; calling pid = " + android.os.Binder.getCallingPid());
                    }
                    previousMode = this.mAppOpsCheckingService.getPackageMode(op.packageName, op.op, android.os.UserHandle.getUserId(op.uid));
                    this.mIgnoredCallback = permissionPolicyCallback;
                    this.mAppOpsCheckingService.setPackageMode(op.packageName, op.op, mode, android.os.UserHandle.getUserId(op.uid));
                }
            }
            notifyStorageManagerOpModeChangedSync(code2, uid, packageName, mode, previousMode);
        } catch (java.lang.SecurityException e) {
            logVerifyAndGetBypassFailure(uid, e, "setMode");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOpChanged(android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> callbacks, int code, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) {
        for (int i = 0; i < callbacks.size(); i++) {
            com.android.server.appop.OnOpModeChangedListener callback = callbacks.valueAt(i);
            notifyOpChanged(callback, code, uid, packageName, persistentDeviceId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOpChanged(com.android.server.appop.OnOpModeChangedListener onModeChangedListener, int code, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) {
        int[] switchedCodes;
        java.util.Objects.requireNonNull(onModeChangedListener);
        if (uid == -2 || onModeChangedListener.getWatchingUid() < 0 || onModeChangedListener.getWatchingUid() == uid) {
            if (onModeChangedListener.getWatchedOpCode() == -2) {
                switchedCodes = this.mSwitchedOps.get(code);
            } else if (onModeChangedListener.getWatchedOpCode() == -1) {
                switchedCodes = new int[]{code};
            } else {
                switchedCodes = new int[]{onModeChangedListener.getWatchedOpCode()};
            }
            for (int switchedCode : switchedCodes) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                } catch (android.os.RemoteException e) {
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
                if (!shouldIgnoreCallback(switchedCode, onModeChangedListener.getCallingPid(), onModeChangedListener.getCallingUid())) {
                    onModeChangedListener.onOpModeChanged(switchedCode, uid, packageName, persistentDeviceId);
                    android.os.Binder.restoreCallingIdentity(identity);
                } else {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }
    }

    private static java.util.ArrayList<com.android.server.appop.AppOpsService.ChangeRec> addChange(java.util.ArrayList<com.android.server.appop.AppOpsService.ChangeRec> reports, int op, int uid, java.lang.String packageName, int previousMode) {
        boolean duplicate = false;
        if (reports == null) {
            reports = new java.util.ArrayList<>();
        } else {
            int reportCount = reports.size();
            int j = 0;
            while (true) {
                if (j >= reportCount) {
                    break;
                }
                com.android.server.appop.AppOpsService.ChangeRec report = reports.get(j);
                if (report.op != op || !report.pkg.equals(packageName)) {
                    j++;
                } else {
                    duplicate = true;
                    break;
                }
            }
        }
        if (!duplicate) {
            reports.add(new com.android.server.appop.AppOpsService.ChangeRec(op, uid, packageName, previousMode));
        }
        return reports;
    }

    private static java.util.HashMap<com.android.server.appop.OnOpModeChangedListener, java.util.ArrayList<com.android.server.appop.AppOpsService.ChangeRec>> addCallbacks(java.util.HashMap<com.android.server.appop.OnOpModeChangedListener, java.util.ArrayList<com.android.server.appop.AppOpsService.ChangeRec>> callbacks, int op, int uid, java.lang.String packageName, int previousMode, android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> cbs) {
        if (cbs == null) {
            return callbacks;
        }
        if (callbacks == null) {
            callbacks = new java.util.HashMap<>();
        }
        int N = cbs.size();
        for (int i = 0; i < N; i++) {
            com.android.server.appop.OnOpModeChangedListener cb = cbs.valueAt(i);
            java.util.ArrayList<com.android.server.appop.AppOpsService.ChangeRec> reports = callbacks.get(cb);
            java.util.ArrayList<com.android.server.appop.AppOpsService.ChangeRec> changed = addChange(reports, op, uid, packageName, previousMode);
            if (changed != reports) {
                callbacks.put(cb, changed);
            }
        }
        return callbacks;
    }

    static final class ChangeRec {
        final int op;
        final java.lang.String pkg;
        final int previous_mode;
        final int uid;

        ChangeRec(int _op, int _uid, java.lang.String _pkg, int _previous_mode) {
            this.op = _op;
            this.uid = _uid;
            this.pkg = _pkg;
            this.previous_mode = _previous_mode;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0168  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void resetAllModes(int r33, java.lang.String r34) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1003
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.AppOpsService.resetAllModes(int, java.lang.String):void");
    }

    private boolean isUidOpGrantedByRole(int uid, int code) {
        if (!android.app.AppOpsManager.opIsUidAppOpPermission(code)) {
            return false;
        }
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String packageName = (java.lang.String) com.android.internal.util.ArrayUtils.firstOrNull(com.android.internal.util.ArrayUtils.defeatNullable(packageManager.getPackagesForUid(uid)));
            if (packageName == null) {
                return false;
            }
            int permissionFlags = packageManager.getPermissionFlags(android.app.AppOpsManager.opToPermission(code), packageName, android.os.UserHandle.getUserHandleForUid(uid));
            return (32768 & permissionFlags) != 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean isPackageOpGrantedByRole(java.lang.String packageName, int uid, int code) {
        if (!android.app.AppOpsManager.opIsPackageAppOpPermission(code)) {
            return false;
        }
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int permissionFlags = packageManager.getPermissionFlags(android.app.AppOpsManager.opToPermission(code), packageName, android.os.UserHandle.getUserHandleForUid(uid));
            return (32768 & permissionFlags) != 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean shouldDeferResetOpToDpm(int op) {
        return this.dpmi != null && this.dpmi.supportsResetOp(op);
    }

    private void deferResetOpToDpm(int op, java.lang.String packageName, int userId) {
        this.dpmi.resetOp(op, packageName, userId);
    }

    public void startWatchingMode(int op, java.lang.String packageName, com.android.internal.app.IAppOpsCallback callback) {
        startWatchingModeWithFlags(op, packageName, 0, callback);
    }

    public void startWatchingModeWithFlags(int op, java.lang.String packageName, int flags, com.android.internal.app.IAppOpsCallback callback) {
        int iOpToSwitch;
        int notifiedOps;
        int switchOp;
        com.android.server.appop.AppOpsService.ModeCallback cb;
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (op <= 10000) {
            com.android.internal.util.Preconditions.checkArgumentInRange(op, -1, 148, "Invalid op code: " + op);
        } else {
            com.android.internal.util.Preconditions.checkArgumentInRange(op, 10000, 10003, "Invalid op code: " + op);
        }
        if (callback == null) {
            return;
        }
        boolean mayWatchPackageName = (packageName == null || filterAppAccessUnlocked(packageName, android.os.UserHandle.getUserId(callingUid))) ? false : true;
        synchronized (this) {
            if (op == -1) {
                iOpToSwitch = op;
            } else {
                try {
                    iOpToSwitch = android.app.AppOpsManager.opToSwitch(op);
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            int switchOp2 = iOpToSwitch;
            if ((flags & 2) == 0) {
                if (op == -1) {
                    notifiedOps = -2;
                } else {
                    notifiedOps = op;
                }
            } else {
                notifiedOps = switchOp2;
            }
            com.android.server.appop.AppOpsService.ModeCallback cb2 = this.mModeWatchers.get(callback.asBinder());
            if (cb2 != null) {
                switchOp = switchOp2;
                cb = cb2;
            } else {
                switchOp = switchOp2;
                cb = new com.android.server.appop.AppOpsService.ModeCallback(callback, -1, flags, notifiedOps, callingUid, callingPid);
                this.mModeWatchers.put(callback.asBinder(), cb);
            }
            if (switchOp != -1) {
                android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> cbs = this.mOpModeWatchers.get(switchOp);
                if (cbs == null) {
                    cbs = new android.util.ArraySet<>();
                    this.mOpModeWatchers.put(switchOp, cbs);
                }
                cbs.add(cb);
            }
            if (mayWatchPackageName) {
                android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> cbs2 = this.mPackageModeWatchers.get(packageName);
                if (cbs2 == null) {
                    cbs2 = new android.util.ArraySet<>();
                    this.mPackageModeWatchers.put(packageName, cbs2);
                }
                cbs2.add(cb);
            }
        }
    }

    public void stopWatchingMode(com.android.internal.app.IAppOpsCallback callback) {
        if (callback == null) {
            return;
        }
        synchronized (this) {
            com.android.server.appop.AppOpsService.ModeCallback cb = this.mModeWatchers.remove(callback.asBinder());
            if (cb != null) {
                cb.unlinkToDeath();
                for (int i = this.mOpModeWatchers.size() - 1; i >= 0; i--) {
                    android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> cbs = this.mOpModeWatchers.valueAt(i);
                    cbs.remove(cb);
                    if (cbs.size() <= 0) {
                        this.mOpModeWatchers.removeAt(i);
                    }
                }
                for (int i2 = this.mPackageModeWatchers.size() - 1; i2 >= 0; i2--) {
                    android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> cbs2 = this.mPackageModeWatchers.valueAt(i2);
                    cbs2.remove(cb);
                    if (cbs2.size() <= 0) {
                        this.mPackageModeWatchers.removeAt(i2);
                    }
                }
            }
        }
    }

    public void setCheckOpsDelegate(android.app.AppOpsManagerInternal.CheckOpsDelegate delegate) {
        synchronized (this) {
            com.android.server.appop.AppOpsService.CheckOpsDelegateDispatcher oldDispatcher = this.mCheckOpsDelegateDispatcher;
            android.app.AppOpsManagerInternal.CheckOpsDelegate policy = oldDispatcher != null ? oldDispatcher.mPolicy : null;
            this.mCheckOpsDelegateDispatcher = new com.android.server.appop.AppOpsService.CheckOpsDelegateDispatcher(policy, delegate);
        }
    }

    private static boolean isOpAllowedForUid(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        return com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled() && (appId == 0 || appId == 1000);
    }

    public int checkOperationRaw(int code, int uid, java.lang.String packageName, java.lang.String attributionTag) {
        return this.mCheckOpsDelegateDispatcher.checkOperation(code, uid, packageName, attributionTag, 0, true);
    }

    public int checkOperationRawForDevice(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId) {
        return this.mCheckOpsDelegateDispatcher.checkOperation(code, uid, packageName, attributionTag, virtualDeviceId, true);
    }

    public int checkOperation(int code, int uid, java.lang.String packageName) {
        return this.mCheckOpsDelegateDispatcher.checkOperation(code, uid, packageName, null, 0, false);
    }

    public int checkOperationForDevice(int code, int uid, java.lang.String packageName, int virtualDeviceId) {
        return this.mCheckOpsDelegateDispatcher.checkOperation(code, uid, packageName, null, virtualDeviceId, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkOperationImpl(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean raw) {
        verifyIncomingOp(code);
        if (!isValidVirtualDeviceId(virtualDeviceId)) {
            android.util.Slog.w(TAG, "checkOperationImpl returned MODE_IGNORED as virtualDeviceId " + virtualDeviceId + " is invalid");
            return 1;
        }
        if (!isIncomingPackageValid(packageName, android.os.UserHandle.getUserId(uid))) {
            return android.app.AppOpsManager.opToDefaultMode(code);
        }
        java.lang.String resolvedPackageName = android.app.AppOpsManager.resolvePackageName(uid, packageName);
        if (resolvedPackageName == null) {
            return 1;
        }
        return checkOperationUnchecked(code, uid, resolvedPackageName, attributionTag, virtualDeviceId, raw);
    }

    private int checkOperationUnchecked(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean raw) throws java.lang.Throwable {
        int iEvalMode;
        try {
            com.android.server.appop.AppOpsService.PackageVerificationResult pvr = verifyAndGetBypass(uid, packageName, null);
            if (isOpRestrictedDueToSuspend(code, packageName, uid)) {
                return 1;
            }
            synchronized (this) {
                try {
                    try {
                        if (isOpRestrictedLocked(uid, code, packageName, attributionTag, virtualDeviceId, pvr.bypass, true)) {
                            return 1;
                        }
                        if (isOpAllowedForUid(uid)) {
                            return 0;
                        }
                        int code2 = android.app.AppOpsManager.opToSwitch(code);
                        try {
                            com.android.server.appop.AppOpsService.UidState uidState = getUidStateLocked(uid, false);
                            if (uidState != null) {
                                int rawUidMode = this.mAppOpsCheckingService.getUidMode(uidState.uid, getPersistentId(virtualDeviceId), code2);
                                if (rawUidMode != android.app.AppOpsManager.opToDefaultMode(code2)) {
                                    return raw ? rawUidMode : uidState.evalMode(code2, rawUidMode);
                                }
                            }
                            com.android.server.appop.AppOpsService.Op op = getOpLocked(code2, uid, packageName, null, false, pvr.bypass, false);
                            if (op == null) {
                                return android.app.AppOpsManager.opToDefaultMode(code2);
                            }
                            if (raw) {
                                iEvalMode = this.mAppOpsCheckingService.getPackageMode(op.packageName, op.op, android.os.UserHandle.getUserId(op.uid));
                            } else {
                                iEvalMode = op.uidState.evalMode(op.op, this.mAppOpsCheckingService.getPackageMode(op.packageName, op.op, android.os.UserHandle.getUserId(op.uid)));
                            }
                            return iEvalMode;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        } catch (java.lang.SecurityException e) {
            logVerifyAndGetBypassFailure(uid, e, "checkOperation");
            return android.app.AppOpsManager.opToDefaultMode(code);
        }
    }

    public int checkAudioOperation(int code, int usage, int uid, java.lang.String packageName) {
        return this.mCheckOpsDelegateDispatcher.checkAudioOperation(code, usage, uid, packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkAudioOperationImpl(int code, int usage, int uid, java.lang.String packageName) {
        int mode = this.mAudioRestrictionManager.checkAudioOperation(code, usage, uid, packageName);
        if (mode != 0) {
            return mode;
        }
        return checkOperation(code, uid, packageName);
    }

    public void setAudioRestriction(int code, int usage, int uid, int mode, java.lang.String[] exceptionPackages) {
        enforceManageAppOpsModes(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), uid);
        verifyIncomingUid(uid);
        verifyIncomingOp(code);
        this.mAudioRestrictionManager.setZenModeAudioRestriction(code, usage, uid, mode, exceptionPackages);
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.appop.AppOpsService$$ExternalSyntheticLambda4(), this, java.lang.Integer.valueOf(code), -2));
        if (code == 28 && usage == 5 && mode != 0) {
            this.mAppOpsServiceExt.hookSetAudioRestriction(this.mContext, code, usage, uid, mode, this.mHandler);
        }
    }

    public void setCameraAudioRestriction(int mode) {
        enforceManageAppOpsModes(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), -1);
        this.mAudioRestrictionManager.setCameraAudioRestriction(mode);
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.appop.AppOpsService$$ExternalSyntheticLambda4(), this, 28, -2));
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.appop.AppOpsService$$ExternalSyntheticLambda4(), this, 3, -2));
    }

    public int checkPackage(int uid, java.lang.String packageName) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(packageName);
        try {
            verifyAndGetBypass(uid, packageName, null, -1, null, true);
            if (resolveNonAppUid(packageName) != uid) {
                if (isPackageExisted(packageName)) {
                    if (!filterAppAccessUnlocked(packageName, android.os.UserHandle.getUserId(uid))) {
                        return 0;
                    }
                }
                return 2;
            }
            return 0;
        } catch (java.lang.SecurityException e) {
            return 2;
        }
    }

    private boolean isPackageExisted(java.lang.String packageName) {
        return getPackageManagerInternal().getPackageStateInternal(packageName) != null;
    }

    private boolean filterAppAccessUnlocked(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        return ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).filterAppAccess(packageName, callingUid, userId);
    }

    public android.app.SyncNotedAppOp noteProxyOperation(int code, android.content.AttributionSource attributionSource, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation) {
        return this.mCheckOpsDelegateDispatcher.noteProxyOperation(code, attributionSource, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation);
    }

    public android.app.SyncNotedAppOp noteProxyOperationWithState(int code, android.content.AttributionSourceState attributionSourceState, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation) {
        android.content.AttributionSource attributionSource = new android.content.AttributionSource(attributionSourceState);
        return this.mCheckOpsDelegateDispatcher.noteProxyOperation(code, attributionSource, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.SyncNotedAppOp noteProxyOperationImpl(int code, android.content.AttributionSource attributionSource, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation) throws java.lang.Throwable {
        java.lang.String proxiedPackageName;
        int proxiedUid;
        int proxyVirtualDeviceId;
        int proxyUid;
        java.lang.String proxiedAttributionTag;
        int proxyUid2 = attributionSource.getUid();
        java.lang.String proxyPackageName = attributionSource.getPackageName();
        java.lang.String proxyAttributionTag = attributionSource.getAttributionTag();
        int proxyVirtualDeviceId2 = attributionSource.getDeviceId();
        int proxiedUid2 = attributionSource.getNextUid();
        java.lang.String proxiedPackageName2 = attributionSource.getNextPackageName();
        java.lang.String proxiedAttributionTag2 = attributionSource.getNextAttributionTag();
        int proxiedVirtualDeviceId = attributionSource.getNextDeviceId();
        verifyIncomingProxyUid(attributionSource);
        verifyIncomingOp(code);
        if (isValidVirtualDeviceId(proxyVirtualDeviceId2)) {
            if (!isIncomingPackageValid(proxiedPackageName2, android.os.UserHandle.getUserId(proxiedUid2)) || !isIncomingPackageValid(proxyPackageName, android.os.UserHandle.getUserId(proxyUid2))) {
                java.lang.String proxiedAttributionTag3 = proxiedAttributionTag2;
                java.lang.String proxiedPackageName3 = proxiedPackageName2;
                return new android.app.SyncNotedAppOp(2, code, proxiedAttributionTag3, proxiedPackageName3);
            }
            boolean skipProxyOperation2 = skipProxyOperation && isCallerAndAttributionTrusted(attributionSource);
            java.lang.String resolveProxyPackageName = android.app.AppOpsManager.resolvePackageName(proxyUid2, proxyPackageName);
            if (resolveProxyPackageName == null) {
                return new android.app.SyncNotedAppOp(1, code, proxiedAttributionTag2, proxiedPackageName2);
            }
            boolean isSelfBlame = android.os.Binder.getCallingUid() == proxiedUid2;
            boolean isProxyTrusted = this.mContext.checkPermission("android.permission.UPDATE_APP_OPS_STATS", -1, proxyUid2) == 0 || isSelfBlame;
            if (skipProxyOperation2) {
                proxiedPackageName = proxiedPackageName2;
                proxiedUid = proxiedUid2;
                proxyVirtualDeviceId = proxyVirtualDeviceId2;
                proxyUid = proxyUid2;
                proxiedAttributionTag = proxiedAttributionTag2;
            } else {
                int proxyFlags = isProxyTrusted ? 2 : 4;
                proxiedUid = proxiedUid2;
                proxyVirtualDeviceId = proxyVirtualDeviceId2;
                proxyUid = proxyUid2;
                android.app.SyncNotedAppOp proxyReturn = noteOperationUnchecked(code, proxyUid2, resolveProxyPackageName, proxyAttributionTag, proxyVirtualDeviceId2, -1, null, null, 0, proxyFlags, !isProxyTrusted, "proxy " + message, shouldCollectMessage);
                if (proxyReturn.getOpMode() == 0) {
                    proxiedAttributionTag = proxiedAttributionTag2;
                    proxiedPackageName = proxiedPackageName2;
                } else {
                    return new android.app.SyncNotedAppOp(proxyReturn.getOpMode(), code, proxiedAttributionTag2, proxiedPackageName2);
                }
            }
            int proxiedUid3 = proxiedUid;
            java.lang.String resolveProxiedPackageName = android.app.AppOpsManager.resolvePackageName(proxiedUid3, proxiedPackageName);
            if (resolveProxiedPackageName == null) {
                return new android.app.SyncNotedAppOp(1, code, proxiedAttributionTag, proxiedPackageName);
            }
            int proxiedFlags = isProxyTrusted ? 8 : 16;
            return noteOperationUnchecked(code, proxiedUid3, resolveProxiedPackageName, proxiedAttributionTag, proxiedVirtualDeviceId, proxyUid, resolveProxyPackageName, proxyAttributionTag, proxyVirtualDeviceId, proxiedFlags, shouldCollectAsyncNotedOp, message, shouldCollectMessage);
        }
        android.util.Slog.w(TAG, "noteProxyOperationImpl returned MODE_IGNORED as virtualDeviceId " + proxyVirtualDeviceId2 + " is invalid");
        return new android.app.SyncNotedAppOp(1, code, proxiedAttributionTag2, proxiedPackageName2);
    }

    public android.app.SyncNotedAppOp noteOperation(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage) {
        return this.mCheckOpsDelegateDispatcher.noteOperation(code, uid, packageName, attributionTag, 0, shouldCollectAsyncNotedOp, message, shouldCollectMessage);
    }

    public android.app.SyncNotedAppOp noteOperationForDevice(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage) {
        return this.mCheckOpsDelegateDispatcher.noteOperation(code, uid, packageName, attributionTag, virtualDeviceId, shouldCollectAsyncNotedOp, message, shouldCollectMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.SyncNotedAppOp noteOperationImpl(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage) {
        verifyIncomingUid(uid);
        verifyIncomingOp(code);
        if (isValidVirtualDeviceId(virtualDeviceId)) {
            if (!isIncomingPackageValid(packageName, android.os.UserHandle.getUserId(uid))) {
                if (code == 111) {
                    android.util.Slog.e(TAG, "noting OP_BLUETOOTH_CONNECT returned MODE_ERRORED as incoming package: " + packageName + " and uid: " + uid + " is invalid");
                }
                return new android.app.SyncNotedAppOp(2, code, attributionTag, packageName);
            }
            java.lang.String resolvedPackageName = android.app.AppOpsManager.resolvePackageName(uid, packageName);
            if (resolvedPackageName == null) {
                return new android.app.SyncNotedAppOp(1, code, attributionTag, packageName);
            }
            return noteOperationUnchecked(code, uid, resolvedPackageName, attributionTag, virtualDeviceId, -1, null, null, 0, 1, shouldCollectAsyncNotedOp, message, shouldCollectMessage);
        }
        android.util.Slog.w(TAG, "checkOperationImpl returned MODE_IGNORED as virtualDeviceId " + virtualDeviceId + " is invalid");
        return new android.app.SyncNotedAppOp(1, code, attributionTag, packageName);
    }

    private android.app.SyncNotedAppOp noteOperationUnchecked(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, int proxyVirtualDeviceId, int flags, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage) throws java.lang.Throwable {
        com.android.server.appop.AttributedOp attributedOp;
        com.android.server.appop.AppOpsService.UidState uidState;
        java.lang.String attributionTag2;
        int uidMode;
        int mode;
        java.lang.String attributionTag3;
        java.lang.String attributionTag4 = attributionTag;
        try {
            com.android.server.appop.AppOpsService.PackageVerificationResult pvr = verifyAndGetBypass(uid, packageName, attributionTag, proxyUid, proxyPackageName);
            if (!pvr.isAttributionTagValid) {
                attributionTag4 = null;
            }
            java.lang.String proxyAttributionTag2 = (proxyAttributionTag == null || isAttributionTagDefined(packageName, proxyPackageName, proxyAttributionTag)) ? proxyAttributionTag : null;
            synchronized (this) {
                try {
                    try {
                        java.lang.String attributionTag5 = attributionTag4;
                        try {
                            com.android.server.appop.AppOpsService.Ops ops = getOpsLocked(uid, packageName, attributionTag4, pvr.isAttributionTagValid, pvr.bypass, true);
                            try {
                                if (ops == null) {
                                    try {
                                        scheduleOpNotedIfNeededLocked(code, uid, packageName, attributionTag5, virtualDeviceId, flags, 1);
                                        if (code == 111) {
                                            attributionTag3 = attributionTag5;
                                            try {
                                                android.util.Slog.e(TAG, "noting OP_BLUETOOTH_CONNECT returned MODE_ERRORED as #getOpsLocked returned null for uid: " + uid + " packageName: " + packageName + " attributionTag: " + attributionTag3 + " pvr.isAttributionTagValid: " + pvr.isAttributionTagValid + " pvr.bypass: " + pvr.bypass);
                                                android.util.Slog.e(TAG, "mUidStates.get(" + uid + "): " + this.mUidStates.get(uid));
                                            } catch (java.lang.Throwable th) {
                                                th = th;
                                            }
                                        } else {
                                            attributionTag3 = attributionTag5;
                                        }
                                        return new android.app.SyncNotedAppOp(2, code, attributionTag3, packageName);
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                } else {
                                    try {
                                        com.android.server.appop.AppOpsService.Op op = getOpLocked(ops, code, uid, true);
                                        com.android.server.appop.AttributedOp attributedOp2 = op.getOrCreateAttribution(op, attributionTag5, getPersistentId(virtualDeviceId));
                                        if (attributedOp2.isRunning()) {
                                            attributedOp = attributedOp2;
                                            android.util.Slog.w(TAG, "Noting op not finished: uid " + uid + " pkg " + packageName + " code " + code + " startTime of in progress event=" + attributedOp2.mInProgressEvents.valueAt(0).getStartTime());
                                        } else {
                                            attributedOp = attributedOp2;
                                        }
                                        int switchCode = android.app.AppOpsManager.opToSwitch(code);
                                        com.android.server.appop.AppOpsService.UidState uidState2 = ops.uidState;
                                        com.android.server.appop.AttributedOp attributedOp3 = attributedOp;
                                        try {
                                            if (isOpRestrictedLocked(uid, code, packageName, attributionTag5, virtualDeviceId, pvr.bypass, false)) {
                                                try {
                                                    attributedOp3.rejected(uidState2.getState(), flags);
                                                    scheduleOpNotedIfNeededLocked(code, uid, packageName, attributionTag5, virtualDeviceId, flags, 1);
                                                    try {
                                                        return new android.app.SyncNotedAppOp(1, code, attributionTag5, packageName);
                                                    } catch (java.lang.Throwable th3) {
                                                        th = th3;
                                                    }
                                                } catch (java.lang.Throwable th4) {
                                                    th = th4;
                                                }
                                            } else {
                                                try {
                                                    if (isOpAllowedForUid(uid)) {
                                                        uidState = uidState2;
                                                        attributionTag2 = attributionTag5;
                                                    } else {
                                                        try {
                                                            if (this.mAppOpsCheckingService.getUidMode(uidState2.uid, getPersistentId(virtualDeviceId), switchCode) != android.app.AppOpsManager.opToDefaultMode(switchCode)) {
                                                                try {
                                                                    int uidMode2 = uidState2.evalMode(code, this.mAppOpsCheckingService.getUidMode(uidState2.uid, getPersistentId(virtualDeviceId), switchCode));
                                                                    if (uidMode2 != 0) {
                                                                        attributedOp3.rejected(uidState2.getState(), flags);
                                                                        scheduleOpNotedIfNeededLocked(code, uid, packageName, attributionTag5, virtualDeviceId, flags, uidMode2);
                                                                        if (code == 111) {
                                                                            uidMode = uidMode2;
                                                                            if (uidMode == 2) {
                                                                                android.util.Slog.e(TAG, "noting OP_BLUETOOTH_CONNECT returned MODE_ERRORED as uid mode is MODE_ERRORED");
                                                                            }
                                                                        } else {
                                                                            uidMode = uidMode2;
                                                                        }
                                                                        return new android.app.SyncNotedAppOp(uidMode, code, attributionTag5, packageName);
                                                                    }
                                                                    uidState = uidState2;
                                                                    attributionTag2 = attributionTag5;
                                                                } catch (java.lang.Throwable th5) {
                                                                    th = th5;
                                                                }
                                                            } else {
                                                                uidState = uidState2;
                                                                com.android.server.appop.AppOpsService.Op switchOp = switchCode != code ? getOpLocked(ops, switchCode, uid, true) : op;
                                                                try {
                                                                    int mode2 = switchOp.uidState.evalMode(switchOp.op, this.mAppOpsCheckingService.getPackageMode(switchOp.packageName, switchOp.op, android.os.UserHandle.getUserId(switchOp.uid)));
                                                                    if (mode2 != 0) {
                                                                        try {
                                                                            attributedOp3.rejected(uidState.getState(), flags);
                                                                            scheduleOpNotedIfNeededLocked(code, uid, packageName, attributionTag5, virtualDeviceId, flags, mode2);
                                                                            if (code == 111) {
                                                                                mode = mode2;
                                                                                if (mode == 2) {
                                                                                    android.util.Slog.e(TAG, "noting OP_BLUETOOTH_CONNECT returned MODE_ERRORED as package mode is MODE_ERRORED");
                                                                                }
                                                                            } else {
                                                                                mode = mode2;
                                                                            }
                                                                            return new android.app.SyncNotedAppOp(mode, code, attributionTag5, packageName);
                                                                        } catch (java.lang.Throwable th6) {
                                                                            th = th6;
                                                                        }
                                                                    } else {
                                                                        attributionTag2 = attributionTag5;
                                                                    }
                                                                } catch (java.lang.Throwable th7) {
                                                                    th = th7;
                                                                }
                                                            }
                                                        } catch (java.lang.Throwable th8) {
                                                            th = th8;
                                                        }
                                                    }
                                                    scheduleOpNotedIfNeededLocked(code, uid, packageName, attributionTag2, virtualDeviceId, flags, 0);
                                                    attributedOp3.accessed(proxyUid, proxyPackageName, proxyAttributionTag2, getPersistentId(proxyVirtualDeviceId), uidState.getState(), flags);
                                                    if (shouldCollectAsyncNotedOp) {
                                                        collectAsyncNotedOp(uid, packageName, code, attributionTag2, flags, message, shouldCollectMessage);
                                                    }
                                                    return new android.app.SyncNotedAppOp(0, code, attributionTag2, packageName);
                                                } catch (java.lang.Throwable th9) {
                                                    th = th9;
                                                }
                                            }
                                        } catch (java.lang.Throwable th10) {
                                            th = th10;
                                        }
                                    } catch (java.lang.Throwable th11) {
                                        th = th11;
                                    }
                                }
                            } catch (java.lang.Throwable th12) {
                                th = th12;
                            }
                        } catch (java.lang.Throwable th13) {
                            th = th13;
                        }
                    } catch (java.lang.Throwable th14) {
                        th = th14;
                    }
                } catch (java.lang.Throwable th15) {
                    th = th15;
                }
                throw th;
            }
        } catch (java.lang.SecurityException e) {
            logVerifyAndGetBypassFailure(uid, e, "noteOperation");
            if (code == 111) {
                android.util.Slog.e(TAG, "noting OP_BLUETOOTH_CONNECT returned MODE_ERRORED as verifyAndGetBypass returned a SecurityException for package: " + packageName + " and uid: " + uid + " and attributionTag: " + attributionTag4, e);
            }
            return new android.app.SyncNotedAppOp(2, code, attributionTag4, packageName);
        }
    }

    public void startWatchingActive(int[] ops, com.android.internal.app.IAppOpsActiveCallback callback) {
        android.util.SparseArray<com.android.server.appop.AppOpsService.ActiveCallback> callbacks;
        int watchedUid = -1;
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WATCH_APPOPS") != 0) {
            watchedUid = callingUid;
        }
        if (ops != null) {
            com.android.internal.util.Preconditions.checkArrayElementsInRange(ops, 0, 10003, "Invalid op code in: " + java.util.Arrays.toString(ops));
        }
        if (callback == null) {
            return;
        }
        synchronized (this) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.ActiveCallback> callbacks2 = this.mActiveWatchers.get(callback.asBinder());
            if (callbacks2 != null) {
                callbacks = callbacks2;
            } else {
                android.util.SparseArray<com.android.server.appop.AppOpsService.ActiveCallback> callbacks3 = new android.util.SparseArray<>();
                this.mActiveWatchers.put(callback.asBinder(), callbacks3);
                callbacks = callbacks3;
            }
            com.android.server.appop.AppOpsService.ActiveCallback activeCallback = new com.android.server.appop.AppOpsService.ActiveCallback(callback, watchedUid, callingUid, callingPid);
            for (int op : ops) {
                callbacks.put(op, activeCallback);
            }
        }
    }

    public void stopWatchingActive(com.android.internal.app.IAppOpsActiveCallback callback) {
        if (callback == null) {
            return;
        }
        synchronized (this) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.ActiveCallback> activeCallbacks = this.mActiveWatchers.remove(callback.asBinder());
            if (activeCallbacks == null) {
                return;
            }
            int callbackCount = activeCallbacks.size();
            for (int i = 0; i < callbackCount; i++) {
                activeCallbacks.valueAt(i).destroy();
            }
        }
    }

    public void startWatchingStarted(int[] ops, com.android.internal.app.IAppOpsStartedCallback callback) {
        android.util.SparseArray<com.android.server.appop.AppOpsService.StartedCallback> callbacks;
        int watchedUid = -1;
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WATCH_APPOPS") != 0) {
            watchedUid = callingUid;
        }
        com.android.internal.util.Preconditions.checkArgument(!com.android.internal.util.ArrayUtils.isEmpty(ops), "Ops cannot be null or empty");
        com.android.internal.util.Preconditions.checkArrayElementsInRange(ops, 0, 148, "Invalid op code in: " + java.util.Arrays.toString(ops));
        java.util.Objects.requireNonNull(callback, "Callback cannot be null");
        synchronized (this) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.StartedCallback> callbacks2 = this.mStartedWatchers.get(callback.asBinder());
            if (callbacks2 != null) {
                callbacks = callbacks2;
            } else {
                android.util.SparseArray<com.android.server.appop.AppOpsService.StartedCallback> callbacks3 = new android.util.SparseArray<>();
                this.mStartedWatchers.put(callback.asBinder(), callbacks3);
                callbacks = callbacks3;
            }
            com.android.server.appop.AppOpsService.StartedCallback startedCallback = new com.android.server.appop.AppOpsService.StartedCallback(callback, watchedUid, callingUid, callingPid);
            for (int op : ops) {
                callbacks.put(op, startedCallback);
            }
        }
    }

    public void stopWatchingStarted(com.android.internal.app.IAppOpsStartedCallback callback) {
        java.util.Objects.requireNonNull(callback, "Callback cannot be null");
        synchronized (this) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.StartedCallback> startedCallbacks = this.mStartedWatchers.remove(callback.asBinder());
            if (startedCallbacks == null) {
                return;
            }
            int callbackCount = startedCallbacks.size();
            for (int i = 0; i < callbackCount; i++) {
                startedCallbacks.valueAt(i).destroy();
            }
        }
    }

    public void startWatchingNoted(int[] ops, com.android.internal.app.IAppOpsNotedCallback callback) {
        android.util.SparseArray<com.android.server.appop.AppOpsService.NotedCallback> callbacks;
        int watchedUid = -1;
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WATCH_APPOPS") != 0) {
            watchedUid = callingUid;
        }
        com.android.internal.util.Preconditions.checkArgument(!com.android.internal.util.ArrayUtils.isEmpty(ops), "Ops cannot be null or empty");
        com.android.internal.util.Preconditions.checkArrayElementsInRange(ops, 0, 10003, "Invalid op code in: " + java.util.Arrays.toString(ops));
        java.util.Objects.requireNonNull(callback, "Callback cannot be null");
        synchronized (this) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.NotedCallback> callbacks2 = this.mNotedWatchers.get(callback.asBinder());
            if (callbacks2 != null) {
                callbacks = callbacks2;
            } else {
                android.util.SparseArray<com.android.server.appop.AppOpsService.NotedCallback> callbacks3 = new android.util.SparseArray<>();
                this.mNotedWatchers.put(callback.asBinder(), callbacks3);
                callbacks = callbacks3;
            }
            com.android.server.appop.AppOpsService.NotedCallback notedCallback = new com.android.server.appop.AppOpsService.NotedCallback(callback, watchedUid, callingUid, callingPid);
            for (int op : ops) {
                callbacks.put(op, notedCallback);
            }
        }
    }

    public void stopWatchingNoted(com.android.internal.app.IAppOpsNotedCallback callback) {
        java.util.Objects.requireNonNull(callback, "Callback cannot be null");
        synchronized (this) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.NotedCallback> notedCallbacks = this.mNotedWatchers.remove(callback.asBinder());
            if (notedCallbacks == null) {
                return;
            }
            int callbackCount = notedCallbacks.size();
            for (int i = 0; i < callbackCount; i++) {
                notedCallbacks.valueAt(i).destroy();
            }
        }
    }

    private void collectAsyncNotedOp(final int uid, final java.lang.String packageName, final int opCode, final java.lang.String attributionTag, int flags, java.lang.String message, boolean shouldCollectMessage) {
        android.os.RemoteCallbackList<com.android.internal.app.IAppOpsAsyncNotedCallback> callbacks;
        int i;
        int i2;
        android.app.AsyncNotedAppOp asyncNotedOp;
        java.util.Objects.requireNonNull(message);
        int callingUid = android.os.Binder.getCallingUid();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this) {
                android.util.Pair<java.lang.String, java.lang.Integer> key = getAsyncNotedOpsKey(packageName, uid);
                android.os.RemoteCallbackList<com.android.internal.app.IAppOpsAsyncNotedCallback> callbacks2 = this.mAsyncOpWatchers.get(key);
                final android.app.AsyncNotedAppOp asyncNotedOp2 = new android.app.AsyncNotedAppOp(opCode, callingUid, attributionTag, message, java.lang.System.currentTimeMillis());
                final boolean[] wasNoteForwarded = {false};
                if ((flags & 9) == 0 || !shouldCollectMessage) {
                    callbacks = callbacks2;
                } else {
                    callbacks = callbacks2;
                    reportRuntimeAppOpAccessMessageAsyncLocked(uid, packageName, opCode, attributionTag, message);
                }
                if (callbacks != null) {
                    i = 0;
                    i2 = 1;
                    asyncNotedOp = asyncNotedOp2;
                    callbacks.broadcast(new java.util.function.Consumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda17
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.appop.AppOpsService.lambda$collectAsyncNotedOp$8(asyncNotedOp2, wasNoteForwarded, opCode, packageName, uid, attributionTag, (com.android.internal.app.IAppOpsAsyncNotedCallback) obj);
                        }
                    });
                } else {
                    i = 0;
                    i2 = 1;
                    asyncNotedOp = asyncNotedOp2;
                }
                if (!wasNoteForwarded[i]) {
                    java.util.ArrayList<android.app.AsyncNotedAppOp> unforwardedOps = this.mUnforwardedAsyncNotedOps.get(key);
                    if (unforwardedOps == null) {
                        unforwardedOps = new java.util.ArrayList<>(i2);
                        this.mUnforwardedAsyncNotedOps.put(key, unforwardedOps);
                    }
                    unforwardedOps.add(asyncNotedOp);
                    if (unforwardedOps.size() > 10) {
                        unforwardedOps.remove(i);
                    }
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    static /* synthetic */ void lambda$collectAsyncNotedOp$8(android.app.AsyncNotedAppOp asyncNotedOp, boolean[] wasNoteForwarded, int opCode, java.lang.String packageName, int uid, java.lang.String attributionTag, com.android.internal.app.IAppOpsAsyncNotedCallback cb) {
        try {
            cb.opNoted(asyncNotedOp);
            wasNoteForwarded[0] = true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not forward noteOp of " + opCode + " to " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + "(" + attributionTag + ")", e);
        }
    }

    private android.util.Pair<java.lang.String, java.lang.Integer> getAsyncNotedOpsKey(java.lang.String packageName, int uid) {
        return new android.util.Pair<>(packageName, java.lang.Integer.valueOf(uid));
    }

    public void startWatchingAsyncNoted(java.lang.String packageName, com.android.internal.app.IAppOpsAsyncNotedCallback callback) {
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(callback);
        int uid = android.os.Binder.getCallingUid();
        final android.util.Pair<java.lang.String, java.lang.Integer> key = getAsyncNotedOpsKey(packageName, uid);
        verifyAndGetBypass(uid, packageName, null);
        synchronized (this) {
            android.os.RemoteCallbackList<com.android.internal.app.IAppOpsAsyncNotedCallback> callbacks = this.mAsyncOpWatchers.get(key);
            if (callbacks == null) {
                callbacks = new android.os.RemoteCallbackList<com.android.internal.app.IAppOpsAsyncNotedCallback>() { // from class: com.android.server.appop.AppOpsService.8
                    @Override // android.os.RemoteCallbackList
                    public void onCallbackDied(com.android.internal.app.IAppOpsAsyncNotedCallback callback2) {
                        synchronized (com.android.server.appop.AppOpsService.this) {
                            if (getRegisteredCallbackCount() == 0) {
                                com.android.server.appop.AppOpsService.this.mAsyncOpWatchers.remove(key);
                            }
                        }
                    }
                };
                this.mAsyncOpWatchers.put(key, callbacks);
            }
            callbacks.register(callback);
        }
    }

    public void stopWatchingAsyncNoted(java.lang.String packageName, com.android.internal.app.IAppOpsAsyncNotedCallback callback) {
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(callback);
        int uid = android.os.Binder.getCallingUid();
        android.util.Pair<java.lang.String, java.lang.Integer> key = getAsyncNotedOpsKey(packageName, uid);
        verifyAndGetBypass(uid, packageName, null);
        synchronized (this) {
            android.os.RemoteCallbackList<com.android.internal.app.IAppOpsAsyncNotedCallback> callbacks = this.mAsyncOpWatchers.get(key);
            if (callbacks != null) {
                callbacks.unregister(callback);
                if (callbacks.getRegisteredCallbackCount() == 0) {
                    this.mAsyncOpWatchers.remove(key);
                }
            }
        }
    }

    public java.util.List<android.app.AsyncNotedAppOp> extractAsyncOps(java.lang.String packageName) {
        java.util.ArrayList<android.app.AsyncNotedAppOp> arrayListRemove;
        java.util.Objects.requireNonNull(packageName);
        int uid = android.os.Binder.getCallingUid();
        verifyAndGetBypass(uid, packageName, null);
        synchronized (this) {
            arrayListRemove = this.mUnforwardedAsyncNotedOps.remove(getAsyncNotedOpsKey(packageName, uid));
        }
        return arrayListRemove;
    }

    public android.app.SyncNotedAppOp startOperation(android.os.IBinder token, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId) {
        return this.mCheckOpsDelegateDispatcher.startOperation(token, code, uid, packageName, attributionTag, 0, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId);
    }

    public android.app.SyncNotedAppOp startOperationForDevice(android.os.IBinder token, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId) {
        return this.mCheckOpsDelegateDispatcher.startOperation(token, code, uid, packageName, attributionTag, virtualDeviceId, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.SyncNotedAppOp startOperationImpl(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId) {
        int result;
        verifyIncomingUid(uid);
        verifyIncomingOp(code);
        if (isValidVirtualDeviceId(virtualDeviceId)) {
            if (!isIncomingPackageValid(packageName, android.os.UserHandle.getUserId(uid))) {
                return new android.app.SyncNotedAppOp(2, code, attributionTag, packageName);
            }
            java.lang.String resolvedPackageName = android.app.AppOpsManager.resolvePackageName(uid, packageName);
            if (resolvedPackageName == null) {
                return new android.app.SyncNotedAppOp(1, code, attributionTag, packageName);
            }
            int result2 = 3;
            if ((code == 102 || code == 120 || code == 135) && (result2 = checkOperation(27, uid, packageName)) != 0) {
                return new android.app.SyncNotedAppOp(result2, code, attributionTag, packageName);
            }
            if (code == 134 && (result = checkOperation(26, uid, packageName)) != 0) {
                return new android.app.SyncNotedAppOp(result, code, attributionTag, packageName);
            }
            return startOperationUnchecked(clientId, code, uid, packageName, attributionTag, virtualDeviceId, -1, null, null, 0, 1, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId);
        }
        android.util.Slog.w(TAG, "startOperationImpl returned MODE_IGNORED as virtualDeviceId " + virtualDeviceId + " is invalid");
        return new android.app.SyncNotedAppOp(1, code, attributionTag, packageName);
    }

    public android.app.SyncNotedAppOp startProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlags, int attributionChainId) {
        return this.mCheckOpsDelegateDispatcher.startProxyOperation(clientId, code, attributionSource, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId);
    }

    public android.app.SyncNotedAppOp startProxyOperationWithState(android.os.IBinder clientId, int code, android.content.AttributionSourceState attributionSourceState, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlags, int attributionChainId) {
        android.content.AttributionSource attributionSource = new android.content.AttributionSource(attributionSourceState);
        return this.mCheckOpsDelegateDispatcher.startProxyOperation(clientId, code, attributionSource, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.SyncNotedAppOp startProxyOperationImpl(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlags, int attributionChainId) throws java.lang.Throwable {
        int proxiedVirtualDeviceId;
        java.lang.String proxiedAttributionTag;
        int proxiedUid;
        int proxyVirtualDeviceId;
        int proxyUid;
        int proxyUid2 = attributionSource.getUid();
        java.lang.String proxyPackageName = attributionSource.getPackageName();
        java.lang.String proxyAttributionTag = attributionSource.getAttributionTag();
        int proxyVirtualDeviceId2 = attributionSource.getDeviceId();
        int proxiedUid2 = attributionSource.getNextUid();
        java.lang.String proxiedPackageName = attributionSource.getNextPackageName();
        java.lang.String proxiedAttributionTag2 = attributionSource.getNextAttributionTag();
        int proxiedVirtualDeviceId2 = attributionSource.getNextDeviceId();
        verifyIncomingProxyUid(attributionSource);
        verifyIncomingOp(code);
        if (!isValidVirtualDeviceId(proxyVirtualDeviceId2)) {
            android.util.Slog.w(TAG, "startProxyOperationImpl returned MODE_IGNORED as proxyVirtualDeviceId " + proxyVirtualDeviceId2 + " is invalid");
            return new android.app.SyncNotedAppOp(1, code, proxiedAttributionTag2, proxiedPackageName);
        }
        if (isValidVirtualDeviceId(proxiedVirtualDeviceId2)) {
            if (!isIncomingPackageValid(proxyPackageName, android.os.UserHandle.getUserId(proxyUid2)) || !isIncomingPackageValid(proxiedPackageName, android.os.UserHandle.getUserId(proxiedUid2))) {
                java.lang.String proxiedAttributionTag3 = proxiedAttributionTag2;
                java.lang.String proxiedPackageName2 = proxiedPackageName;
                int i = code;
                int i2 = 2;
                return new android.app.SyncNotedAppOp(i2, i, proxiedAttributionTag3, proxiedPackageName2);
            }
            boolean isCallerTrusted = isCallerAndAttributionTrusted(attributionSource);
            boolean skipProxyOperation2 = isCallerTrusted && skipProxyOperation;
            java.lang.String resolvedProxyPackageName = android.app.AppOpsManager.resolvePackageName(proxyUid2, proxyPackageName);
            if (resolvedProxyPackageName == null) {
                return new android.app.SyncNotedAppOp(1, code, proxiedAttributionTag2, proxiedPackageName);
            }
            boolean isChainTrusted = (!isCallerTrusted || attributionChainId == -1 || ((proxyAttributionFlags & 8) == 0 && (proxiedAttributionFlags & 8) == 0)) ? false : true;
            boolean isSelfBlame = android.os.Binder.getCallingUid() == proxiedUid2;
            boolean isProxyTrusted = this.mContext.checkPermission("android.permission.UPDATE_APP_OPS_STATS", -1, proxyUid2) == 0 || isSelfBlame || isChainTrusted;
            java.lang.String resolvedProxiedPackageName = android.app.AppOpsManager.resolvePackageName(proxiedUid2, proxiedPackageName);
            if (resolvedProxiedPackageName == null) {
                return new android.app.SyncNotedAppOp(1, code, proxiedAttributionTag2, proxiedPackageName);
            }
            int proxiedFlags = isProxyTrusted ? 8 : 16;
            if (skipProxyOperation2) {
                proxiedVirtualDeviceId = proxiedVirtualDeviceId2;
                proxiedAttributionTag = proxiedAttributionTag2;
                proxiedUid = proxiedUid2;
                proxyVirtualDeviceId = proxyVirtualDeviceId2;
                proxyUid = proxyUid2;
            } else {
                proxiedVirtualDeviceId = proxiedVirtualDeviceId2;
                proxiedAttributionTag = proxiedAttributionTag2;
                proxiedUid = proxiedUid2;
                proxyVirtualDeviceId = proxyVirtualDeviceId2;
                android.app.SyncNotedAppOp testProxiedOp = startOperationDryRun(code, proxiedUid2, resolvedProxiedPackageName, proxiedAttributionTag2, proxiedVirtualDeviceId, proxyUid2, resolvedProxyPackageName, proxiedFlags, startIfModeDefault);
                if (!shouldStartForMode(testProxiedOp.getOpMode(), startIfModeDefault)) {
                    return testProxiedOp;
                }
                int proxyFlags = isProxyTrusted ? 2 : 4;
                proxyUid = proxyUid2;
                android.app.SyncNotedAppOp proxyAppOp = startOperationUnchecked(clientId, code, proxyUid, resolvedProxyPackageName, proxyAttributionTag, proxyVirtualDeviceId, -1, null, null, 0, proxyFlags, startIfModeDefault, !isProxyTrusted, "proxy " + message, shouldCollectMessage, proxyAttributionFlags, attributionChainId);
                if (!shouldStartForMode(proxyAppOp.getOpMode(), startIfModeDefault)) {
                    return proxyAppOp;
                }
            }
            return startOperationUnchecked(clientId, code, proxiedUid, resolvedProxiedPackageName, proxiedAttributionTag, proxiedVirtualDeviceId, proxyUid, resolvedProxyPackageName, proxyAttributionTag, proxyVirtualDeviceId, proxiedFlags, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, proxiedAttributionFlags, attributionChainId);
        }
        android.util.Slog.w(TAG, "startProxyOperationImpl returned MODE_IGNORED as proxiedVirtualDeviceId " + proxiedVirtualDeviceId2 + " is invalid");
        return new android.app.SyncNotedAppOp(1, code, proxiedAttributionTag2, proxiedPackageName);
    }

    private boolean shouldStartForMode(int mode, boolean startIfModeDefault) {
        return mode == 0 || (mode == 3 && startIfModeDefault);
    }

    private android.app.SyncNotedAppOp startOperationUnchecked(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, int proxyVirtualDeviceId, int flags, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId) throws android.os.RemoteException {
        com.android.server.appop.AppOpsService.UidState uidState;
        boolean isRestricted;
        java.lang.String attributionTag2;
        com.android.server.appop.AttributedOp attributedOp;
        java.lang.String str;
        int i;
        com.android.server.appop.AttributedOp attributedOp2;
        com.android.server.appop.AppOpsService.Ops ops;
        com.android.server.appop.AppOpsService.Op opLocked;
        java.lang.String str2;
        boolean z;
        boolean isRestricted2;
        boolean isRestricted3;
        try {
            com.android.server.appop.AppOpsService.PackageVerificationResult pvr = verifyAndGetBypass(uid, packageName, attributionTag, proxyUid, proxyPackageName);
            java.lang.String attributionTag3 = !pvr.isAttributionTagValid ? null : attributionTag;
            java.lang.String proxyAttributionTag2 = (proxyAttributionTag == null || isAttributionTagDefined(packageName, proxyPackageName, proxyAttributionTag)) ? proxyAttributionTag : null;
            int startType = 0;
            synchronized (this) {
                try {
                    try {
                        java.lang.String attributionTag4 = attributionTag3;
                        try {
                            com.android.server.appop.AppOpsService.Ops ops2 = getOpsLocked(uid, packageName, attributionTag3, pvr.isAttributionTagValid, pvr.bypass, true);
                            if (ops2 == null) {
                                try {
                                    scheduleOpStartedIfNeededLocked(code, uid, packageName, attributionTag4, virtualDeviceId, flags, 1, 0, attributionFlags, attributionChainId);
                                    try {
                                        return new android.app.SyncNotedAppOp(2, code, attributionTag4, packageName);
                                    } catch (java.lang.Throwable th) {
                                        e = th;
                                    }
                                } catch (java.lang.Throwable th2) {
                                    e = th2;
                                }
                            } else {
                                java.lang.String attributionTag5 = attributionTag4;
                                try {
                                    com.android.server.appop.AppOpsService.Op op = getOpLocked(ops2, code, uid, true);
                                    com.android.server.appop.AttributedOp attributedOp3 = op.getOrCreateAttribution(op, attributionTag5, getPersistentId(virtualDeviceId));
                                    com.android.server.appop.AppOpsService.UidState uidState2 = ops2.uidState;
                                    try {
                                        try {
                                            boolean isRestricted4 = isOpRestrictedLocked(uid, code, packageName, attributionTag5, virtualDeviceId, pvr.bypass, false);
                                            try {
                                                int switchCode = android.app.AppOpsManager.opToSwitch(code);
                                                if (isOpAllowedForUid(uid)) {
                                                    str = packageName;
                                                    isRestricted = isRestricted4;
                                                    uidState = uidState2;
                                                    attributedOp = attributedOp3;
                                                    attributionTag2 = attributionTag5;
                                                } else {
                                                    int rawUidMode = this.mAppOpsCheckingService.getUidMode(uidState2.uid, getPersistentId(virtualDeviceId), switchCode);
                                                    try {
                                                        if (rawUidMode != android.app.AppOpsManager.opToDefaultMode(switchCode)) {
                                                            try {
                                                                int uidMode = uidState2.evalMode(code, rawUidMode);
                                                                if (!shouldStartForMode(uidMode, startIfModeDefault)) {
                                                                    attributedOp3.rejected(uidState2.getState(), flags);
                                                                    try {
                                                                        scheduleOpStartedIfNeededLocked(code, uid, packageName, attributionTag5, virtualDeviceId, flags, uidMode, 0, attributionFlags, attributionChainId);
                                                                        return new android.app.SyncNotedAppOp(uidMode, code, attributionTag5, packageName);
                                                                    } catch (java.lang.Throwable th3) {
                                                                        e = th3;
                                                                        attributionTag5 = packageName;
                                                                        throw e;
                                                                    }
                                                                }
                                                                uidState = uidState2;
                                                                isRestricted = isRestricted4;
                                                                attributionTag2 = attributionTag5;
                                                                attributedOp = attributedOp3;
                                                                str = packageName;
                                                            } catch (java.lang.Throwable th4) {
                                                                e = th4;
                                                            }
                                                        } else {
                                                            uidState = uidState2;
                                                            isRestricted = isRestricted4;
                                                            attributionTag2 = attributionTag5;
                                                            if (switchCode != code) {
                                                                i = uid;
                                                                attributedOp2 = attributedOp3;
                                                                ops = ops2;
                                                                opLocked = getOpLocked(ops, switchCode, i, true);
                                                            } else {
                                                                i = uid;
                                                                attributedOp2 = attributedOp3;
                                                                ops = ops2;
                                                                opLocked = op;
                                                            }
                                                            com.android.server.appop.AppOpsService.Op switchOp = opLocked;
                                                            try {
                                                                int mode = switchOp.uidState.evalMode(switchOp.op, this.mAppOpsCheckingService.getPackageMode(switchOp.packageName, switchOp.op, android.os.UserHandle.getUserId(switchOp.uid)));
                                                                if (mode == 0) {
                                                                    attributedOp = attributedOp2;
                                                                    str = packageName;
                                                                } else if (startIfModeDefault && mode == 3) {
                                                                    attributedOp = attributedOp2;
                                                                    str = packageName;
                                                                } else {
                                                                    try {
                                                                        attributedOp2.rejected(uidState.getState(), flags);
                                                                        try {
                                                                            scheduleOpStartedIfNeededLocked(code, uid, packageName, attributionTag2, virtualDeviceId, flags, mode, 0, attributionFlags, attributionChainId);
                                                                            return new android.app.SyncNotedAppOp(mode, code, attributionTag2, packageName);
                                                                        } catch (java.lang.Throwable th5) {
                                                                            e = th5;
                                                                        }
                                                                    } catch (java.lang.Throwable th6) {
                                                                        e = th6;
                                                                    }
                                                                }
                                                            } catch (java.lang.Throwable th7) {
                                                                e = th7;
                                                                throw e;
                                                            }
                                                        }
                                                    } catch (java.lang.Throwable th8) {
                                                        e = th8;
                                                    }
                                                }
                                                try {
                                                    if (this.mAppOpsServiceExt.shouldLog(str, code, 1)) {
                                                        try {
                                                            str2 = str;
                                                            try {
                                                                isRestricted2 = isRestricted;
                                                                try {
                                                                    android.util.Slog.d(TAG, "startOperation: allowing code " + code + " uid " + uid + " package " + str2 + " restricted: " + isRestricted2 + " flags: " + android.app.AppOpsManager.flagsToString(flags) + " callingUid: " + android.os.Binder.getCallingUid() + " callingPid: " + android.os.Binder.getCallingPid());
                                                                } catch (java.lang.Throwable th9) {
                                                                    e = th9;
                                                                }
                                                            } catch (java.lang.Throwable th10) {
                                                                e = th10;
                                                                z = isRestricted;
                                                            }
                                                        } catch (java.lang.Throwable th11) {
                                                            e = th11;
                                                            str2 = str;
                                                            z = isRestricted;
                                                        }
                                                    } else {
                                                        isRestricted2 = isRestricted;
                                                    }
                                                    try {
                                                        try {
                                                            if (isRestricted2) {
                                                                try {
                                                                    isRestricted3 = isRestricted2;
                                                                    attributedOp.createPaused(clientId, virtualDeviceId, proxyUid, proxyPackageName, proxyAttributionTag2, getPersistentId(proxyVirtualDeviceId), uidState.getState(), flags, attributionFlags, attributionChainId);
                                                                } catch (android.os.RemoteException e) {
                                                                    e = e;
                                                                    throw new java.lang.RuntimeException(e);
                                                                } catch (java.lang.Throwable th12) {
                                                                    e = th12;
                                                                }
                                                            } else {
                                                                isRestricted3 = isRestricted2;
                                                                attributedOp.started(clientId, virtualDeviceId, proxyUid, proxyPackageName, proxyAttributionTag2, getPersistentId(proxyVirtualDeviceId), uidState.getState(), flags, attributionFlags, attributionChainId);
                                                                startType = 1;
                                                            }
                                                            scheduleOpStartedIfNeededLocked(code, uid, packageName, attributionTag2, virtualDeviceId, flags, isRestricted3 ? 1 : 0, startType, attributionFlags, attributionChainId);
                                                            if (shouldCollectAsyncNotedOp && !isRestricted3) {
                                                                collectAsyncNotedOp(uid, packageName, code, attributionTag2, 1, message, shouldCollectMessage);
                                                            }
                                                            return new android.app.SyncNotedAppOp(isRestricted3 ? 1 : 0, code, attributionTag2, packageName);
                                                        } catch (java.lang.Throwable th13) {
                                                            e = th13;
                                                        }
                                                    } catch (android.os.RemoteException e2) {
                                                        e = e2;
                                                    }
                                                } catch (java.lang.Throwable th14) {
                                                    e = th14;
                                                }
                                            } catch (java.lang.Throwable th15) {
                                                e = th15;
                                            }
                                        } catch (java.lang.Throwable th16) {
                                            e = th16;
                                        }
                                    } catch (java.lang.Throwable th17) {
                                        e = th17;
                                    }
                                } catch (java.lang.Throwable th18) {
                                    e = th18;
                                }
                            }
                        } catch (java.lang.Throwable th19) {
                            e = th19;
                        }
                    } catch (java.lang.Throwable th20) {
                        e = th20;
                    }
                } catch (java.lang.Throwable th21) {
                    e = th21;
                }
                throw e;
            }
        } catch (java.lang.SecurityException e3) {
            logVerifyAndGetBypassFailure(uid, e3, "startOperation");
            return new android.app.SyncNotedAppOp(2, code, attributionTag, packageName);
        }
    }

    private android.app.SyncNotedAppOp startOperationDryRun(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, int proxyUid, java.lang.String proxyPackageName, int flags, boolean startIfModeDefault) throws java.lang.Throwable {
        java.lang.String attributionTag2;
        java.lang.String attributionTag3;
        com.android.server.appop.AppOpsService.Op switchOp;
        try {
            com.android.server.appop.AppOpsService.PackageVerificationResult pvr = verifyAndGetBypass(uid, packageName, attributionTag, proxyUid, proxyPackageName);
            if (pvr.isAttributionTagValid) {
                attributionTag2 = attributionTag;
            } else {
                attributionTag2 = null;
            }
            synchronized (this) {
                try {
                    try {
                        java.lang.String attributionTag4 = attributionTag2;
                        try {
                            com.android.server.appop.AppOpsService.Ops ops = getOpsLocked(uid, packageName, attributionTag2, pvr.isAttributionTagValid, pvr.bypass, true);
                            if (ops != null) {
                                com.android.server.appop.AppOpsService.Op op = getOpLocked(ops, code, uid, true);
                                com.android.server.appop.AppOpsService.UidState uidState = ops.uidState;
                                try {
                                    boolean isRestricted = isOpRestrictedLocked(uid, code, packageName, attributionTag4, virtualDeviceId, pvr.bypass, false);
                                    int switchCode = android.app.AppOpsManager.opToSwitch(code);
                                    if (this.mAppOpsCheckingService.getUidMode(uidState.uid, getPersistentId(virtualDeviceId), switchCode) != android.app.AppOpsManager.opToDefaultMode(switchCode)) {
                                        int uidMode = uidState.evalMode(code, this.mAppOpsCheckingService.getUidMode(uidState.uid, getPersistentId(virtualDeviceId), switchCode));
                                        attributionTag3 = attributionTag4;
                                        if (!shouldStartForMode(uidMode, startIfModeDefault)) {
                                            return new android.app.SyncNotedAppOp(uidMode, code, attributionTag3, packageName);
                                        }
                                    } else {
                                        attributionTag3 = attributionTag4;
                                        if (switchCode != code) {
                                            switchOp = getOpLocked(ops, switchCode, uid, true);
                                        } else {
                                            switchOp = op;
                                        }
                                        int mode = switchOp.uidState.evalMode(switchOp.op, this.mAppOpsCheckingService.getPackageMode(switchOp.packageName, switchOp.op, android.os.UserHandle.getUserId(switchOp.uid)));
                                        if (mode != 0 && (!startIfModeDefault || mode != 3)) {
                                            return new android.app.SyncNotedAppOp(mode, code, attributionTag3, packageName);
                                        }
                                    }
                                    return new android.app.SyncNotedAppOp(isRestricted ? 1 : 0, code, attributionTag3, packageName);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                }
                            } else {
                                try {
                                    return new android.app.SyncNotedAppOp(2, code, attributionTag4, packageName);
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                }
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
                throw th;
            }
        } catch (java.lang.SecurityException e) {
            if (android.os.Process.isIsolated(uid)) {
                android.util.Slog.e(TAG, "Cannot startOperation: isolated process");
            } else {
                android.util.Slog.e(TAG, "Cannot startOperation", e);
            }
            return new android.app.SyncNotedAppOp(2, code, attributionTag, packageName);
        }
    }

    public void finishOperation(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag) throws java.lang.Throwable {
        this.mCheckOpsDelegateDispatcher.finishOperation(clientId, code, uid, packageName, attributionTag, 0);
    }

    public void finishOperationForDevice(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId) throws java.lang.Throwable {
        this.mCheckOpsDelegateDispatcher.finishOperation(clientId, code, uid, packageName, attributionTag, virtualDeviceId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishOperationImpl(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId) throws java.lang.Throwable {
        java.lang.String resolvedPackageName;
        verifyIncomingUid(uid);
        verifyIncomingOp(code);
        if (!isValidVirtualDeviceId(virtualDeviceId)) {
            android.util.Slog.w(TAG, "finishOperationImpl was a no-op as virtualDeviceId " + virtualDeviceId + " is invalid");
        } else {
            if (!isIncomingPackageValid(packageName, android.os.UserHandle.getUserId(uid)) || (resolvedPackageName = android.app.AppOpsManager.resolvePackageName(uid, packageName)) == null) {
                return;
            }
            finishOperationUnchecked(clientId, code, uid, resolvedPackageName, attributionTag, virtualDeviceId);
        }
    }

    public void finishProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean skipProxyOperation) throws java.lang.Throwable {
        this.mCheckOpsDelegateDispatcher.finishProxyOperation(clientId, code, attributionSource, skipProxyOperation);
    }

    public void finishProxyOperationWithState(android.os.IBinder clientId, int code, android.content.AttributionSourceState attributionSourceState, boolean skipProxyOperation) throws java.lang.Throwable {
        android.content.AttributionSource attributionSource = new android.content.AttributionSource(attributionSourceState);
        this.mCheckOpsDelegateDispatcher.finishProxyOperation(clientId, code, attributionSource, skipProxyOperation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.Void finishProxyOperationImpl(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean skipProxyOperation) throws java.lang.Throwable {
        java.lang.String resolvedProxyPackageName;
        int proxyUid = attributionSource.getUid();
        java.lang.String proxyPackageName = attributionSource.getPackageName();
        java.lang.String proxyAttributionTag = attributionSource.getAttributionTag();
        int proxiedUid = attributionSource.getNextUid();
        int proxyVirtualDeviceId = attributionSource.getDeviceId();
        java.lang.String proxiedPackageName = attributionSource.getNextPackageName();
        java.lang.String proxiedAttributionTag = attributionSource.getNextAttributionTag();
        boolean skipProxyOperation2 = skipProxyOperation && isCallerAndAttributionTrusted(attributionSource);
        verifyIncomingProxyUid(attributionSource);
        verifyIncomingOp(code);
        if (isValidVirtualDeviceId(proxyVirtualDeviceId)) {
            if (!isIncomingPackageValid(proxyPackageName, android.os.UserHandle.getUserId(proxyUid)) || !isIncomingPackageValid(proxiedPackageName, android.os.UserHandle.getUserId(proxiedUid)) || (resolvedProxyPackageName = android.app.AppOpsManager.resolvePackageName(proxyUid, proxyPackageName)) == null) {
                return null;
            }
            if (!skipProxyOperation2) {
                finishOperationUnchecked(clientId, code, proxyUid, resolvedProxyPackageName, proxyAttributionTag, proxyVirtualDeviceId);
            }
            java.lang.String resolvedProxiedPackageName = android.app.AppOpsManager.resolvePackageName(proxiedUid, proxiedPackageName);
            if (resolvedProxiedPackageName == null) {
                return null;
            }
            finishOperationUnchecked(clientId, code, proxiedUid, resolvedProxiedPackageName, proxiedAttributionTag, proxyVirtualDeviceId);
            return null;
        }
        android.util.Slog.w(TAG, "finishProxyOperationImpl was a no-op as virtualDeviceId " + proxyVirtualDeviceId + " is invalid");
        return null;
    }

    private void finishOperationUnchecked(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId) throws java.lang.Throwable {
        java.lang.String attributionTag2;
        try {
            com.android.server.appop.AppOpsService.PackageVerificationResult pvr = verifyAndGetBypass(uid, packageName, attributionTag);
            if (pvr.isAttributionTagValid) {
                attributionTag2 = attributionTag;
            } else {
                attributionTag2 = null;
            }
            synchronized (this) {
                try {
                    try {
                        com.android.server.appop.AppOpsService.Op op = getOpLocked(code, uid, packageName, attributionTag2, pvr.isAttributionTagValid, pvr.bypass, true);
                        if (op == null) {
                            android.util.Slog.e(TAG, "Operation not found: uid=" + uid + " pkg=" + packageName + "(" + attributionTag2 + ") op=" + android.app.AppOpsManager.opToName(code));
                            return;
                        }
                        try {
                            com.android.server.appop.AttributedOp attributedOp = op.mDeviceAttributedOps.getOrDefault(getPersistentId(virtualDeviceId), new android.util.ArrayMap<>()).get(attributionTag2);
                            if (attributedOp == null) {
                                android.util.Slog.e(TAG, "Attribution not found: uid=" + uid + " pkg=" + packageName + "(" + attributionTag2 + ") op=" + android.app.AppOpsManager.opToName(code));
                                return;
                            }
                            if (attributedOp.isRunning() || attributedOp.isPaused()) {
                                if (this.mAppOpsServiceExt.shouldLog(packageName, code, 1)) {
                                    android.util.Slog.d(TAG, "finishOperation: package: " + packageName + " op: " + code + " callingUid: " + android.os.Binder.getCallingUid() + " callingPid: " + android.os.Binder.getCallingPid());
                                }
                                attributedOp.finished(clientId);
                            } else {
                                android.util.Slog.e(TAG, "Operation not started: uid=" + uid + " pkg=" + packageName + "(" + attributionTag2 + ") op=" + android.app.AppOpsManager.opToName(code));
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        } catch (java.lang.SecurityException e) {
            logVerifyAndGetBypassFailure(uid, e, "finishOperation");
        }
    }

    void scheduleOpActiveChangedIfNeededLocked(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean active, int attributionFlags, int attributionChainId) {
        int callbackListCount = this.mActiveWatchers.size();
        android.util.ArraySet<com.android.server.appop.AppOpsService.ActiveCallback> dispatchedCallbacks = null;
        for (int i = 0; i < callbackListCount; i++) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.ActiveCallback> callbacks = this.mActiveWatchers.valueAt(i);
            com.android.server.appop.AppOpsService.ActiveCallback callback = callbacks.get(code);
            if (callback != null && (callback.mWatchingUid < 0 || callback.mWatchingUid == uid)) {
                if (dispatchedCallbacks == null) {
                    dispatchedCallbacks = new android.util.ArraySet<>();
                }
                dispatchedCallbacks.add(callback);
            }
        }
        if (dispatchedCallbacks == null) {
            return;
        }
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.DecConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda10
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9, java.lang.Object obj10) {
                ((com.android.server.appop.AppOpsService) obj).notifyOpActiveChanged((android.util.ArraySet) obj2, ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), (java.lang.String) obj5, (java.lang.String) obj6, ((java.lang.Integer) obj7).intValue(), ((java.lang.Boolean) obj8).booleanValue(), ((java.lang.Integer) obj9).intValue(), ((java.lang.Integer) obj10).intValue());
            }
        }, this, dispatchedCallbacks, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(active), java.lang.Integer.valueOf(attributionFlags), java.lang.Integer.valueOf(attributionChainId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOpActiveChanged(android.util.ArraySet<com.android.server.appop.AppOpsService.ActiveCallback> callbacks, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean active, int attributionFlags, int attributionChainId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int callbackCount = callbacks.size();
            for (int i = 0; i < callbackCount; i++) {
                com.android.server.appop.AppOpsService.ActiveCallback callback = callbacks.valueAt(i);
                try {
                    if (!shouldIgnoreCallback(code, callback.mCallingPid, callback.mCallingUid)) {
                        if (this.mAppOpsServiceExt.isActivityPreloadPkg(packageName, callback.mCallingUid)) {
                            try {
                                android.util.Slog.i(TAG, "skip opActiveChanage, as " + packageName + " is activity preloading.");
                            } catch (android.os.RemoteException e) {
                            }
                        } else {
                            try {
                                callback.mCallback.opActiveChanged(code, uid, packageName, attributionTag, virtualDeviceId, active, attributionFlags, attributionChainId);
                            } catch (android.os.RemoteException e2) {
                            }
                        }
                    }
                } catch (android.os.RemoteException e3) {
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    void scheduleOpStartedIfNeededLocked(int code, int uid, java.lang.String pkgName, java.lang.String attributionTag, int virtualDeviceId, int flags, int result, int startedType, int attributionFlags, int attributionChainId) {
        int callbackListCount = this.mStartedWatchers.size();
        android.util.ArraySet<com.android.server.appop.AppOpsService.StartedCallback> dispatchedCallbacks = null;
        for (int i = 0; i < callbackListCount; i++) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.StartedCallback> callbacks = this.mStartedWatchers.valueAt(i);
            com.android.server.appop.AppOpsService.StartedCallback callback = callbacks.get(code);
            if (callback != null && (callback.mWatchingUid < 0 || callback.mWatchingUid == uid)) {
                if (dispatchedCallbacks == null) {
                    dispatchedCallbacks = new android.util.ArraySet<>();
                }
                dispatchedCallbacks.add(callback);
            }
        }
        if (dispatchedCallbacks == null) {
            return;
        }
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.DodecConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda13
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9, java.lang.Object obj10, java.lang.Object obj11, java.lang.Object obj12) {
                ((com.android.server.appop.AppOpsService) obj).notifyOpStarted((android.util.ArraySet) obj2, ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), (java.lang.String) obj5, (java.lang.String) obj6, ((java.lang.Integer) obj7).intValue(), ((java.lang.Integer) obj8).intValue(), ((java.lang.Integer) obj9).intValue(), ((java.lang.Integer) obj10).intValue(), ((java.lang.Integer) obj11).intValue(), ((java.lang.Integer) obj12).intValue());
            }
        }, this, dispatchedCallbacks, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), pkgName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Integer.valueOf(flags), java.lang.Integer.valueOf(result), java.lang.Integer.valueOf(startedType), java.lang.Integer.valueOf(attributionFlags), java.lang.Integer.valueOf(attributionChainId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOpStarted(android.util.ArraySet<com.android.server.appop.AppOpsService.StartedCallback> callbacks, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, int flags, int result, int startedType, int attributionFlags, int attributionChainId) {
        int i;
        int callbackCount;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int callbackCount2 = callbacks.size();
            int i2 = 0;
            while (i2 < callbackCount2) {
                com.android.server.appop.AppOpsService.StartedCallback callback = callbacks.valueAt(i2);
                try {
                    if (shouldIgnoreCallback(code, callback.mCallingPid, callback.mCallingUid)) {
                        i = i2;
                        callbackCount = callbackCount2;
                    } else if (this.mAppOpsServiceExt.isActivityPreloadPkg(packageName, callback.mCallingUid)) {
                        try {
                            android.util.Slog.i(TAG, "skip opActiveChanage, as " + packageName + " is activity preloading.");
                            i = i2;
                            callbackCount = callbackCount2;
                        } catch (android.os.RemoteException e) {
                            i = i2;
                            callbackCount = callbackCount2;
                        }
                    } else {
                        i = i2;
                        callbackCount = callbackCount2;
                        try {
                            callback.mCallback.opStarted(code, uid, packageName, attributionTag, virtualDeviceId, flags, result, startedType, attributionFlags, attributionChainId);
                        } catch (android.os.RemoteException e2) {
                        }
                    }
                } catch (android.os.RemoteException e3) {
                    i = i2;
                    callbackCount = callbackCount2;
                }
                i2 = i + 1;
                callbackCount2 = callbackCount;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void scheduleOpNotedIfNeededLocked(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, int flags, int result) {
        int callbackListCount = this.mNotedWatchers.size();
        android.util.ArraySet<com.android.server.appop.AppOpsService.NotedCallback> dispatchedCallbacks = null;
        for (int i = 0; i < callbackListCount; i++) {
            android.util.SparseArray<com.android.server.appop.AppOpsService.NotedCallback> callbacks = this.mNotedWatchers.valueAt(i);
            com.android.server.appop.AppOpsService.NotedCallback callback = callbacks.get(code);
            if (callback != null && (callback.mWatchingUid < 0 || callback.mWatchingUid == uid)) {
                if (dispatchedCallbacks == null) {
                    dispatchedCallbacks = new android.util.ArraySet<>();
                }
                dispatchedCallbacks.add(callback);
            }
        }
        if (dispatchedCallbacks == null) {
            return;
        }
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.NonaConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda5
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9) throws java.lang.Throwable {
                ((com.android.server.appop.AppOpsService) obj).notifyOpChecked((android.util.ArraySet) obj2, ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), (java.lang.String) obj5, (java.lang.String) obj6, ((java.lang.Integer) obj7).intValue(), ((java.lang.Integer) obj8).intValue(), ((java.lang.Integer) obj9).intValue());
            }
        }, this, dispatchedCallbacks, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Integer.valueOf(flags), java.lang.Integer.valueOf(result)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOpChecked(android.util.ArraySet<com.android.server.appop.AppOpsService.NotedCallback> callbacks, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, int flags, int result) throws java.lang.Throwable {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int callbackCount = callbacks.size();
            for (int i = 0; i < callbackCount; i++) {
                try {
                    com.android.server.appop.AppOpsService.NotedCallback callback = callbacks.valueAt(i);
                    try {
                        if (!shouldIgnoreCallback(code, callback.mCallingPid, callback.mCallingUid)) {
                            if (this.mAppOpsServiceExt.isActivityPreloadPkg(packageName, callback.mCallingUid)) {
                                try {
                                    android.util.Slog.i(TAG, "skip opActiveChanage, as " + packageName + " is activity preloading.");
                                } catch (android.os.RemoteException e) {
                                }
                            } else {
                                try {
                                    callback.mCallback.opNoted(code, uid, packageName, attributionTag, virtualDeviceId, flags, result);
                                } catch (android.os.RemoteException e2) {
                                }
                            }
                        }
                    } catch (android.os.RemoteException e3) {
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            }
            android.os.Binder.restoreCallingIdentity(identity);
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    public int permissionToOpCode(java.lang.String permission) {
        if (permission == null) {
            return -1;
        }
        return android.app.AppOpsManager.permissionToOpCode(permission);
    }

    public boolean shouldCollectNotes(int opCode) {
        com.android.internal.util.Preconditions.checkArgumentInRange(opCode, 0, 148, "opCode");
        if (android.app.AppOpsManager.shouldForceCollectNoteForOp(opCode)) {
            return true;
        }
        java.lang.String perm = android.app.AppOpsManager.opToPermission(opCode);
        if (perm == null) {
            return false;
        }
        try {
            android.content.pm.PermissionInfo permInfo = this.mContext.getPackageManager().getPermissionInfo(perm, 0);
            return permInfo.getProtection() == 1 || (permInfo.getProtectionFlags() & 64) != 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void verifyIncomingProxyUid(android.content.AttributionSource attributionSource) {
        if (attributionSource.getUid() == android.os.Binder.getCallingUid() || android.os.Binder.getCallingPid() == android.os.Process.myPid() || attributionSource.isTrusted(this.mContext)) {
            return;
        }
        this.mContext.enforcePermission("android.permission.UPDATE_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null);
    }

    private void verifyIncomingUid(int uid) {
        if (uid == android.os.Binder.getCallingUid() || android.os.Binder.getCallingPid() == android.os.Process.myPid()) {
            return;
        }
        this.mContext.enforcePermission("android.permission.UPDATE_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null);
    }

    private boolean shouldIgnoreCallback(int op, int watcherPid, int watcherUid) {
        return android.app.AppOpsManager.opRestrictsRead(op) && this.mContext.checkPermission("android.permission.MANAGE_APPOPS", watcherPid, watcherUid) != 0;
    }

    private boolean isValidVirtualDeviceId(int virtualDeviceId) {
        if (virtualDeviceId == 0 || this.mVirtualDeviceManagerInternal == null) {
            return true;
        }
        if (this.mVirtualDeviceManagerInternal.isValidVirtualDeviceId(virtualDeviceId)) {
            this.mKnownDeviceIds.put(virtualDeviceId, this.mVirtualDeviceManagerInternal.getPersistentIdForDevice(virtualDeviceId));
            return true;
        }
        return false;
    }

    private void verifyIncomingOp(int op) {
        if (op >= 0 && op < 149) {
            if (android.app.AppOpsManager.opRestrictsRead(op) && this.mContext.checkPermission("android.permission.MANAGE_APPOPS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) != 0 && this.mContext.checkPermission("android.permission.GET_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) != 0 && this.mContext.checkPermission("android.permission.MANAGE_APP_OPS_MODES", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) != 0) {
                throw new java.lang.SecurityException("verifyIncomingOp: uid " + android.os.Binder.getCallingUid() + " does not have any of {MANAGE_APPOPS, GET_APP_OPS_STATS, MANAGE_APP_OPS_MODES}");
            }
            return;
        }
        if (op > 10000 && op < 10004) {
        } else {
            throw new java.lang.IllegalArgumentException("Bad operation #" + op);
        }
    }

    private boolean isIncomingPackageValid(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (packageName == null || isSpecialPackage(callingUid, packageName)) {
            return true;
        }
        if (!isPackageExisted(packageName)) {
            return false;
        }
        if (!getPackageManagerInternal().filterAppAccess(packageName, callingUid, userId)) {
            return true;
        }
        android.util.Slog.w(TAG, packageName + " not found from " + callingUid);
        return false;
    }

    private boolean isSpecialPackage(int callingUid, java.lang.String packageName) {
        java.lang.String resolvedPackage = android.app.AppOpsManager.resolvePackageName(callingUid, packageName);
        return callingUid == 1000 || resolveNonAppUid(resolvedPackage) != -1;
    }

    private boolean isCallerAndAttributionTrusted(android.content.AttributionSource attributionSource) {
        return (attributionSource.getUid() != android.os.Binder.getCallingUid() && attributionSource.isTrusted(this.mContext) && (attributionSource.getNext() == null || attributionSource.getNext().isTrusted(this.mContext))) || this.mContext.checkPermission("android.permission.UPDATE_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.appop.AppOpsService.UidState getUidStateLocked(int uid, boolean edit) {
        com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.get(uid);
        if (uidState == null) {
            if (!edit) {
                return null;
            }
            com.android.server.appop.AppOpsService.UidState uidState2 = new com.android.server.appop.AppOpsService.UidState(uid);
            this.mUidStates.put(uid, uidState2);
            return uidState2;
        }
        return uidState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createSandboxUidStateIfNotExistsForAppLocked(int uid, android.util.SparseBooleanArray knownUids) {
        if (android.os.UserHandle.getAppId(uid) < 10000) {
            return;
        }
        int sandboxUid = android.os.Process.toSdkSandboxUid(uid);
        if (knownUids != null) {
            knownUids.put(sandboxUid, true);
        }
        getUidStateLocked(sandboxUid, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAppWidgetVisibility(android.util.SparseArray<java.lang.String> uidPackageNames, boolean visible) {
        synchronized (this) {
            getUidStateTracker().updateAppWidgetVisibility(uidPackageNames, visible);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.pm.PackageManagerInternal getPackageManagerInternal() {
        if (this.mPackageManagerInternal == null) {
            this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }
        if (this.mPackageManagerInternal == null) {
            throw new java.lang.IllegalStateException("PackageManagerInternal not loaded");
        }
        return this.mPackageManagerInternal;
    }

    private com.android.server.pm.PackageManagerLocal getPackageManagerLocal() {
        if (this.mPackageManagerLocal == null) {
            this.mPackageManagerLocal = (com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.pm.PackageManagerLocal.class);
        }
        if (this.mPackageManagerLocal == null) {
            throw new java.lang.IllegalStateException("PackageManagerLocal not loaded");
        }
        return this.mPackageManagerLocal;
    }

    private com.android.server.pm.UserManagerInternal getUserManagerInternal() {
        if (this.mUserManagerInternal == null) {
            this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        }
        if (this.mUserManagerInternal == null) {
            throw new java.lang.IllegalStateException("UserManagerInternal not loaded");
        }
        return this.mUserManagerInternal;
    }

    private android.app.AppOpsManager.RestrictionBypass getBypassforPackage(com.android.server.pm.pkg.PackageState packageState) {
        return new android.app.AppOpsManager.RestrictionBypass(packageState.getAppId() == 1000, packageState.isPrivileged(), this.mContext.checkPermission("android.permission.EXEMPT_FROM_AUDIO_RECORD_RESTRICTIONS", -1, packageState.getAppId()) == 0);
    }

    private com.android.server.appop.AppOpsService.PackageVerificationResult verifyAndGetBypass(int uid, java.lang.String packageName, java.lang.String attributionTag) {
        return verifyAndGetBypass(uid, packageName, attributionTag, -1, null);
    }

    private com.android.server.appop.AppOpsService.PackageVerificationResult verifyAndGetBypass(int uid, java.lang.String packageName, java.lang.String attributionTag, int proxyUid, java.lang.String proxyPackageName) {
        return verifyAndGetBypass(uid, packageName, attributionTag, proxyUid, proxyPackageName, false);
    }

    private com.android.server.appop.AppOpsService.PackageVerificationResult verifyAndGetBypass(int uid, java.lang.String packageName, java.lang.String attributionTag, int proxyUid, java.lang.String proxyPackageName, boolean suppressErrorLogs) throws java.lang.Throwable {
        int uid2;
        int nonAppUid;
        java.lang.String msg;
        com.android.server.appop.AppOpsService.Ops ops;
        if (uid == 0) {
            return new com.android.server.appop.AppOpsService.PackageVerificationResult(null, true);
        }
        if (android.os.Process.isSdkSandboxUid(uid)) {
            try {
                android.content.pm.PackageManager pm = this.mContext.getPackageManager();
                java.lang.String supplementalPackageName = pm.getSdkSandboxPackageName();
                if (!java.util.Objects.equals(packageName, supplementalPackageName)) {
                    uid2 = uid;
                } else {
                    uid2 = pm.getPackageUidAsUser(supplementalPackageName, android.content.pm.PackageManager.PackageInfoFlags.of(0L), android.os.UserHandle.getUserId(uid));
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                uid2 = uid;
            }
        } else {
            uid2 = uid;
        }
        synchronized (this) {
            com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.get(uid2);
            if (uidState != null && !uidState.pkgOps.isEmpty() && (ops = uidState.pkgOps.get(packageName)) != null && ((attributionTag == null || ops.knownAttributionTags.contains(attributionTag)) && ops.bypass != null)) {
                return new com.android.server.appop.AppOpsService.PackageVerificationResult(ops.bypass, ops.validAttributionTags.contains(attributionTag));
            }
            int callingUid = android.os.Binder.getCallingUid();
            if (java.util.Objects.equals(packageName, "com.android.shell")) {
                nonAppUid = 2000;
            } else {
                int nonAppUid2 = resolveNonAppUid(packageName);
                nonAppUid = nonAppUid2;
            }
            if (nonAppUid != -1) {
                if (nonAppUid != android.os.UserHandle.getAppId(uid2)) {
                    if (!suppressErrorLogs) {
                        android.util.Slog.e(TAG, "Bad call made by uid " + callingUid + ". Package \"" + packageName + "\" does not belong to uid " + uid2 + ".");
                    }
                    throw new java.lang.SecurityException("Specified package \"" + packageName + "\" under uid " + android.os.UserHandle.getAppId(uid2) + " but it is not");
                }
                boolean proxyIsSystemAppOrNull = true;
                if (proxyPackageName != null) {
                    int proxyAppId = android.os.UserHandle.getAppId(proxyUid);
                    if (proxyAppId >= 10000) {
                        proxyIsSystemAppOrNull = this.mPackageManagerInternal.isSystemPackage(proxyPackageName);
                    }
                }
                return new com.android.server.appop.AppOpsService.PackageVerificationResult(android.app.AppOpsManager.RestrictionBypass.UNRESTRICTED, proxyIsSystemAppOrNull);
            }
            int userId = android.os.UserHandle.getUserId(uid2);
            android.app.AppOpsManager.RestrictionBypass bypass = null;
            boolean isAttributionTagValid = false;
            int pkgUid = nonAppUid;
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.PackageManagerInternal pmInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                com.android.server.pm.pkg.PackageStateInternal pkgState = pmInt.getPackageStateInternal(packageName);
                com.android.server.pm.pkg.AndroidPackage pkg = pkgState == null ? null : pkgState.getAndroidPackage();
                if (pkg != null) {
                    try {
                        isAttributionTagValid = isAttributionInPackage(pkg, attributionTag);
                        pkgUid = android.os.UserHandle.getUid(userId, pkgState.getAppId());
                        bypass = getBypassforPackage(pkgState);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                }
                if (!isAttributionTagValid) {
                    com.android.server.pm.pkg.AndroidPackage proxyPkg = proxyPackageName != null ? pmInt.getPackage(proxyPackageName) : null;
                    isAttributionTagValid = isAttributionInPackage(proxyPkg, attributionTag);
                    if (pkg != null && isAttributionTagValid) {
                        msg = "attributionTag " + attributionTag + " declared in manifest of the proxy package " + proxyPackageName + ", this is not advised";
                    } else if (pkg != null) {
                        msg = "attributionTag " + attributionTag + " not declared in manifest of " + packageName;
                    } else {
                        msg = "package " + packageName + " not found, can't check for attributionTag " + attributionTag;
                    }
                    try {
                        if (!this.mPlatformCompat.isChangeEnabledByPackageName(151105954L, packageName, userId) || !this.mPlatformCompat.isChangeEnabledByUid(151105954L, callingUid)) {
                            isAttributionTagValid = true;
                        }
                        android.util.Slog.e(TAG, msg);
                    } catch (android.os.RemoteException e2) {
                    }
                }
                android.os.Binder.restoreCallingIdentity(ident);
                if (pkgUid != uid2) {
                    if (!suppressErrorLogs) {
                        android.util.Slog.e(TAG, "Bad call made by uid " + callingUid + ". Package \"" + packageName + "\" does not belong to uid " + uid2 + ".");
                    }
                    throw new java.lang.SecurityException("Specified package \"" + packageName + "\" under uid " + uid2 + " but it is not");
                }
                return new com.android.server.appop.AppOpsService.PackageVerificationResult(bypass, isAttributionTagValid);
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    private boolean isAttributionInPackage(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String attributionTag) {
        if (pkg == null) {
            return false;
        }
        if (attributionTag == null) {
            return true;
        }
        if (pkg.getAttributions() != null) {
            int numAttributions = pkg.getAttributions().size();
            for (int i = 0; i < numAttributions; i++) {
                if (((com.android.internal.pm.pkg.component.ParsedAttribution) pkg.getAttributions().get(i)).getTag().equals(attributionTag)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAttributionTagDefined(java.lang.String packageName, java.lang.String proxyPackageName, java.lang.String attributionTag) {
        com.android.server.pm.pkg.AndroidPackage proxyPkg;
        if (packageName == null) {
            return false;
        }
        if (attributionTag == null) {
            return true;
        }
        android.content.pm.PackageManagerInternal pmInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        if (proxyPackageName != null && (proxyPkg = pmInt.getPackage(proxyPackageName)) != null && isAttributionInPackage(proxyPkg, attributionTag)) {
            return true;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = pmInt.getPackage(packageName);
        return isAttributionInPackage(pkg, attributionTag);
    }

    private void logVerifyAndGetBypassFailure(int uid, java.lang.SecurityException e, java.lang.String methodName) {
        if (android.os.Process.isIsolated(uid)) {
            android.util.Slog.e(TAG, "Cannot " + methodName + ": isolated UID");
        } else if (android.os.UserHandle.getAppId(uid) < 10000) {
            android.util.Slog.e(TAG, "Cannot " + methodName + ": non-application UID " + uid);
        } else {
            android.util.Slog.e(TAG, "Cannot " + methodName, e);
        }
    }

    private com.android.server.appop.AppOpsService.Ops getOpsLocked(int uid, java.lang.String packageName, java.lang.String attributionTag, boolean isAttributionTagValid, android.app.AppOpsManager.RestrictionBypass bypass, boolean edit) {
        com.android.server.appop.AppOpsService.UidState uidState = getUidStateLocked(uid, false);
        if (uidState == null) {
            return null;
        }
        com.android.server.appop.AppOpsService.Ops ops = uidState.pkgOps.get(packageName);
        if (ops == null) {
            if (!edit) {
                return null;
            }
            ops = new com.android.server.appop.AppOpsService.Ops(packageName, uidState);
            uidState.pkgOps.put(packageName, ops);
        }
        if (edit) {
            if (bypass != null) {
                ops.bypass = bypass;
            }
            if (attributionTag != null) {
                ops.knownAttributionTags.add(attributionTag);
                if (isAttributionTagValid) {
                    ops.validAttributionTags.add(attributionTag);
                } else {
                    ops.validAttributionTags.remove(attributionTag);
                }
            }
        }
        return ops;
    }

    private void scheduleWriteLocked() {
        if (!this.mWriteScheduled) {
            this.mWriteScheduled = true;
            this.mHandler.postDelayed(this.mWriteRunner, 1800000L);
        }
    }

    private void scheduleFastWriteLocked() {
        if (!this.mFastWriteScheduled) {
            this.mWriteScheduled = true;
            this.mFastWriteScheduled = true;
            this.mHandler.removeCallbacks(this.mWriteRunner);
            this.mHandler.postDelayed(this.mWriteRunner, 10000L);
        }
    }

    private com.android.server.appop.AppOpsService.Op getOpLocked(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, boolean isAttributionTagValid, android.app.AppOpsManager.RestrictionBypass bypass, boolean edit) {
        com.android.server.appop.AppOpsService.Ops ops = getOpsLocked(uid, packageName, attributionTag, isAttributionTagValid, bypass, edit);
        if (ops == null) {
            return null;
        }
        return getOpLocked(ops, code, uid, edit);
    }

    private com.android.server.appop.AppOpsService.Op getOpLocked(com.android.server.appop.AppOpsService.Ops ops, int code, int uid, boolean edit) {
        com.android.server.appop.AppOpsService.Op op = ops.get(code);
        if (op == null) {
            if (!edit) {
                return null;
            }
            op = new com.android.server.appop.AppOpsService.Op(ops.uidState, ops.packageName, code, uid);
            ops.put(code, op);
        }
        if (edit) {
            scheduleWriteLocked();
        }
        return op;
    }

    private boolean isOpRestrictedDueToSuspend(int code, java.lang.String packageName, int uid) {
        if (!com.android.internal.util.ArrayUtils.contains(OPS_RESTRICTED_ON_SUSPEND, code)) {
            return false;
        }
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        return pmi.isPackageSuspended(packageName, android.os.UserHandle.getUserId(uid));
    }

    private boolean isAutomotive() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    private boolean isOpRestrictedLocked(int uid, int code, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, android.app.AppOpsManager.RestrictionBypass appBypass, boolean isCheckOp) throws java.lang.Throwable {
        if (virtualDeviceId != 0) {
            return false;
        }
        int restrictionSetCount = this.mOpGlobalRestrictions.size();
        for (int i = 0; i < restrictionSetCount; i++) {
            com.android.server.appop.AppOpsService.ClientGlobalRestrictionState restrictionState = this.mOpGlobalRestrictions.valueAt(i);
            if (restrictionState.hasRestriction(code)) {
                return true;
            }
        }
        if (code == 26 && isAutomotive()) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.internal.camera.flags.Flags.cameraPrivacyAllowlist()) {
                    try {
                        if (this.mSensorPrivacyManager.isCameraPrivacyEnabled(packageName)) {
                            android.os.Binder.restoreCallingIdentity(identity);
                            return true;
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(identity);
                        throw th;
                    }
                }
                android.os.Binder.restoreCallingIdentity(identity);
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
        int userHandle = android.os.UserHandle.getUserId(uid);
        int restrictionSetCount2 = this.mOpUserRestrictions.size();
        for (int i2 = 0; i2 < restrictionSetCount2; i2++) {
            com.android.server.appop.AppOpsService.ClientUserRestrictionState restrictionState2 = this.mOpUserRestrictions.valueAt(i2);
            if (restrictionState2.hasRestriction(code, packageName, attributionTag, userHandle, isCheckOp)) {
                android.app.AppOpsManager.RestrictionBypass opBypass = android.app.AppOpsManager.opAllowSystemBypassRestriction(code);
                if (opBypass != null) {
                    synchronized (this) {
                        if (opBypass.isSystemUid && appBypass != null && appBypass.isSystemUid) {
                            return false;
                        }
                        if (opBypass.isPrivileged && appBypass != null && appBypass.isPrivileged) {
                            return false;
                        }
                        if (opBypass.isRecordAudioRestrictionExcept && appBypass != null && appBypass.isRecordAudioRestrictionExcept) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void readRecentAccesses() {
        if (!this.mRecentAccessesFile.exists()) {
            readRecentAccesses(this.mStorageFile);
        } else if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.deviceAwareAppOpNewSchemaEnabled()) {
            synchronized (this) {
                this.mRecentAccessPersistence.readRecentAccesses(this.mUidStates);
            }
        } else {
            readRecentAccesses(this.mRecentAccessesFile);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:37:0x008e
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    private void readRecentAccesses(android.util.AtomicFile r11) {
        /*
            Method dump skipped, instruction units count: 474
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.AppOpsService.readRecentAccesses(android.util.AtomicFile):void");
    }

    private void readPackage(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        java.lang.String pkgName = parser.getAttributeValue((java.lang.String) null, "n");
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("uid")) {
                            readUid(parser, pkgName);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <pkg>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readUid(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String pkgName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        int uid = parser.getAttributeInt((java.lang.String) null, "n");
        com.android.server.appop.AppOpsService.UidState uidState = getUidStateLocked(uid, true);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("op")) {
                            readOp(parser, uidState, pkgName);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <pkg>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readAttributionOp(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.appop.AppOpsService.Op parent, java.lang.String attribution) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        long rejectTime;
        long j;
        int opFlags;
        int uidState;
        com.android.server.appop.AttributedOp attributedOp = parent.getOrCreateAttribution(parent, attribution, "default:0");
        long key = parser.getAttributeLong((java.lang.String) null, "n");
        int uidState2 = android.app.AppOpsManager.extractUidStateFromKey(key);
        int opFlags2 = android.app.AppOpsManager.extractFlagsFromKey(key);
        long accessTime = parser.getAttributeLong((java.lang.String) null, "t", 0L);
        long rejectTime2 = parser.getAttributeLong((java.lang.String) null, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD, 0L);
        long accessDuration = parser.getAttributeLong((java.lang.String) null, "d", -1L);
        java.lang.String proxyPkg = com.android.internal.util.XmlUtils.readStringAttribute(parser, "pp");
        int proxyUid = parser.getAttributeInt((java.lang.String) null, "pu", -1);
        java.lang.String proxyAttributionTag = com.android.internal.util.XmlUtils.readStringAttribute(parser, "pc");
        if (accessTime <= 0) {
            rejectTime = rejectTime2;
            j = 0;
            opFlags = opFlags2;
            uidState = uidState2;
        } else {
            rejectTime = rejectTime2;
            j = 0;
            opFlags = opFlags2;
            uidState = uidState2;
            attributedOp.accessed(accessTime, accessDuration, proxyUid, proxyPkg, proxyAttributionTag, "default:0", uidState2, opFlags);
        }
        if (rejectTime > j) {
            attributedOp.rejected(rejectTime, uidState, opFlags);
        }
    }

    private void readOp(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.appop.AppOpsService.UidState uidState, java.lang.String pkgName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        int opCode = parser.getAttributeInt((java.lang.String) null, "n");
        com.android.server.appop.AppOpsService.Op op = new com.android.server.appop.AppOpsService.Op(uidState, pkgName, opCode, uidState.uid);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals("st")) {
                    readAttributionOp(parser, op, com.android.internal.util.XmlUtils.readStringAttribute(parser, "id"));
                } else {
                    android.util.Slog.w(TAG, "Unknown element under <op>: " + parser.getName());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        com.android.server.appop.AppOpsService.Ops ops = uidState.pkgOps.get(pkgName);
        if (ops == null) {
            ops = new com.android.server.appop.AppOpsService.Ops(pkgName, uidState);
            uidState.pkgOps.put(pkgName, ops);
        }
        ops.put(op.op, op);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:131|14|15|16|(2:148|17)|(5:19|(7:22|(4:32|(7:35|(1:37)|38|(5:41|42|(2:43|(22:143|45|46|(2:48|(3:50|(0)(2:53|156)|84)(1:54))(1:55)|56|(3:137|58|59)(1:60)|61|141|62|(2:129|64)|68|(1:70)|71|(1:73)|74|(1:76)|(1:78)|(1:80)|(1:82)|83|155|84))|87|39)|153|88|33)|152|89)(7:(2:133|25)|26|(1:28)|32|(1:33)|152|89)|118|119|120|20|139)|151|90|(1:92))(1:99)|150|100|101|135|102|103|104|118|119|120) */
    /* JADX WARN: Can't wrap try/catch for region: R(17:131|14|15|16|148|17|(5:19|(7:22|(4:32|(7:35|(1:37)|38|(5:41|42|(2:43|(22:143|45|46|(2:48|(3:50|(0)(2:53|156)|84)(1:54))(1:55)|56|(3:137|58|59)(1:60)|61|141|62|(2:129|64)|68|(1:70)|71|(1:73)|74|(1:76)|(1:78)|(1:80)|(1:82)|83|155|84))|87|39)|153|88|33)|152|89)(7:(2:133|25)|26|(1:28)|32|(1:33)|152|89)|118|119|120|20|139)|151|90|(1:92))(1:99)|150|100|101|135|102|103|104|118|119|120) */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x02d5, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x02db, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x02dc, code lost:
    
        r1 = r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x02de, code lost:
    
        r3 = r29;
     */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0097 A[Catch: IOException -> 0x02b4, all -> 0x02d7, TRY_LEAVE, TryCatch #5 {IOException -> 0x02b4, blocks: (B:20:0x0045, B:22:0x004b, B:32:0x007c, B:33:0x0091, B:35:0x0097, B:38:0x00c5, B:39:0x00d1, B:41:0x00d7), top: B:139:0x0045 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void writeRecentAccesses() throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 790
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.AppOpsService.writeRecentAccesses():void");
    }

    static class Shell extends android.os.ShellCommand {
        static final android.os.Binder sBinder = new android.os.Binder();
        java.lang.String attributionTag;
        final com.android.internal.app.IAppOpsService mInterface;
        final com.android.server.appop.AppOpsService mInternal;
        int mode;
        java.lang.String modeStr;
        int nonpackageUid;
        int op;
        java.lang.String opStr;
        java.lang.String packageName;
        int packageUid;
        boolean targetsUid;
        int userId = 0;
        android.os.IBinder mToken = android.app.AppOpsManager.getClientId();

        Shell(com.android.internal.app.IAppOpsService iface, com.android.server.appop.AppOpsService internal) {
            this.mInterface = iface;
            this.mInternal = internal;
        }

        public int onCommand(java.lang.String cmd) {
            return com.android.server.appop.AppOpsService.onShellCommand(this, cmd);
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            com.android.server.appop.AppOpsService.dumpCommandHelp(pw);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static int strOpToOp(java.lang.String op, java.io.PrintWriter err) {
            try {
                return android.app.AppOpsManager.strOpToOp(op);
            } catch (java.lang.IllegalArgumentException e) {
                try {
                    return java.lang.Integer.parseInt(op);
                } catch (java.lang.NumberFormatException e2) {
                    try {
                        return android.app.AppOpsManager.strDebugOpToOp(op);
                    } catch (java.lang.IllegalArgumentException e3) {
                        err.println("Error: " + e3.getMessage());
                        return -1;
                    }
                }
            }
        }

        static int strModeToMode(java.lang.String modeStr, java.io.PrintWriter err) {
            for (int i = android.app.AppOpsManager.MODE_NAMES.length - 1; i >= 0; i--) {
                if (android.app.AppOpsManager.MODE_NAMES[i].equals(modeStr)) {
                    return i;
                }
            }
            try {
                int i2 = java.lang.Integer.parseInt(modeStr);
                return i2;
            } catch (java.lang.NumberFormatException e) {
                err.println("Error: Mode " + modeStr + " is not valid");
                return -1;
            }
        }

        int parseUserOpMode(int defMode, java.io.PrintWriter err) throws android.os.RemoteException {
            this.userId = -2;
            this.opStr = null;
            this.modeStr = null;
            while (true) {
                java.lang.String argument = getNextArg();
                if (argument == null) {
                    break;
                }
                if ("--user".equals(argument)) {
                    this.userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else if (this.opStr == null) {
                    this.opStr = argument;
                } else if (this.modeStr == null) {
                    this.modeStr = argument;
                    break;
                }
            }
            if (this.opStr == null) {
                err.println("Error: Operation not specified.");
                return -1;
            }
            this.op = strOpToOp(this.opStr, err);
            if (this.op < 0) {
                return -1;
            }
            if (this.modeStr != null) {
                int iStrModeToMode = strModeToMode(this.modeStr, err);
                this.mode = iStrModeToMode;
                return iStrModeToMode < 0 ? -1 : 0;
            }
            this.mode = defMode;
            return 0;
        }

        int parseUserPackageOp(boolean reqOp, java.io.PrintWriter err) throws android.os.RemoteException {
            this.userId = -2;
            this.packageName = null;
            this.opStr = null;
            while (true) {
                java.lang.String argument = getNextArg();
                if (argument == null) {
                    break;
                }
                if ("--user".equals(argument)) {
                    this.userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else if ("--uid".equals(argument)) {
                    this.targetsUid = true;
                } else if ("--attribution".equals(argument)) {
                    this.attributionTag = getNextArgRequired();
                } else if (this.packageName == null) {
                    this.packageName = argument;
                } else if (this.opStr == null) {
                    this.opStr = argument;
                    break;
                }
            }
            if (this.packageName == null) {
                err.println("Error: Package name not specified.");
                return -1;
            }
            if (this.opStr == null && reqOp) {
                err.println("Error: Operation not specified.");
                return -1;
            }
            if (this.opStr != null) {
                this.op = strOpToOp(this.opStr, err);
                if (this.op < 0) {
                    return -1;
                }
            } else {
                this.op = -1;
            }
            if (this.userId == -2) {
                this.userId = android.app.ActivityManager.getCurrentUser();
            }
            this.nonpackageUid = -1;
            try {
                this.nonpackageUid = java.lang.Integer.parseInt(this.packageName);
            } catch (java.lang.NumberFormatException e) {
            }
            if (this.nonpackageUid == -1 && this.packageName.length() > 1 && this.packageName.charAt(0) == 'u' && this.packageName.indexOf(46) < 0) {
                int i = 1;
                while (i < this.packageName.length() && this.packageName.charAt(i) >= '0' && this.packageName.charAt(i) <= '9') {
                    i++;
                }
                if (i > 1 && i < this.packageName.length()) {
                    java.lang.String userStr = this.packageName.substring(1, i);
                    try {
                        int user = java.lang.Integer.parseInt(userStr);
                        char type = this.packageName.charAt(i);
                        int i2 = i + 1;
                        while (i2 < this.packageName.length() && this.packageName.charAt(i2) >= '0' && this.packageName.charAt(i2) <= '9') {
                            i2++;
                        }
                        if (i2 > i2) {
                            java.lang.String typeValStr = this.packageName.substring(i2, i2);
                            try {
                                int typeVal = java.lang.Integer.parseInt(typeValStr);
                                if (type == 'a') {
                                    this.nonpackageUid = android.os.UserHandle.getUid(user, typeVal + 10000);
                                } else if (type == 's') {
                                    this.nonpackageUid = android.os.UserHandle.getUid(user, typeVal);
                                }
                            } catch (java.lang.NumberFormatException e2) {
                            }
                        }
                    } catch (java.lang.NumberFormatException e3) {
                    }
                }
            }
            int i3 = this.nonpackageUid;
            if (i3 != -1) {
                this.packageName = null;
            } else {
                this.packageUid = com.android.server.appop.AppOpsService.resolveNonAppUid(this.packageName);
                if (this.packageUid < 0) {
                    this.packageUid = android.app.AppGlobals.getPackageManager().getPackageUid(this.packageName, 8192L, this.userId);
                }
                if (this.packageUid < 0) {
                    err.println("Error: No UID for " + this.packageName + " in user " + this.userId);
                    return -1;
                }
            }
            return 0;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.appop.AppOpsService.Shell(this, this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    static void dumpCommandHelp(java.io.PrintWriter pw) {
        pw.println("AppOps service (appops) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  start [--user <USER_ID>] [--attribution <ATTRIBUTION_TAG>] <PACKAGE | UID> <OP> ");
        pw.println("    Starts a given operation for a particular application.");
        pw.println("  stop [--user <USER_ID>] [--attribution <ATTRIBUTION_TAG>] <PACKAGE | UID> <OP> ");
        pw.println("    Stops a given operation for a particular application.");
        pw.println("  set [--user <USER_ID>] <[--uid] PACKAGE | UID> <OP> <MODE>");
        pw.println("    Set the mode for a particular application and operation.");
        pw.println("  get [--user <USER_ID>] [--attribution <ATTRIBUTION_TAG>] <PACKAGE | UID> [<OP>]");
        pw.println("    Return the mode for a particular application and optional operation.");
        pw.println("  query-op [--user <USER_ID>] <OP> [<MODE>]");
        pw.println("    Print all packages that currently have the given op in the given mode.");
        pw.println("  reset [--user <USER_ID>] [<PACKAGE>]");
        pw.println("    Reset the given application or all applications to default modes.");
        pw.println("  write-settings");
        pw.println("    Immediately write pending changes to storage.");
        pw.println("  read-settings");
        pw.println("    Read the last written settings, replacing current state in RAM.");
        pw.println("  options:");
        pw.println("    <PACKAGE> an Android package name or its UID if prefixed by --uid");
        pw.println("    <OP>      an AppOps operation.");
        pw.println("    <MODE>    one of allow, ignore, deny, or default");
        pw.println("    <USER_ID> the user id under which the package is installed. If --user is");
        pw.println("              not specified, the current user is assumed.");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:9:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static int onShellCommand(com.android.server.appop.AppOpsService.Shell r22, java.lang.String r23) {
        /*
            Method dump skipped, instruction units count: 1236
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.AppOpsService.onShellCommand(com.android.server.appop.AppOpsService$Shell, java.lang.String):int");
    }

    private void dumpHelp(java.io.PrintWriter pw) {
        pw.println("AppOps service (appops) dump options:");
        pw.println("  -h");
        pw.println("    Print this help text.");
        pw.println("  --op [OP]");
        pw.println("    Limit output to data associated with the given app op code.");
        pw.println("  --mode [MODE]");
        pw.println("    Limit output to data associated with the given app op mode.");
        pw.println("  --package [PACKAGE]");
        pw.println("    Limit output to data associated with the given package name.");
        pw.println("  --attributionTag [attributionTag]");
        pw.println("    Limit output to data associated with the given attribution tag.");
        pw.println("  --include-discrete [n]");
        pw.println("    Include discrete ops limited to n per dimension. Use zero for no limit.");
        pw.println("  --watchers");
        pw.println("    Only output the watcher sections.");
        pw.println("  --history");
        pw.println("    Only output history.");
        pw.println("  --uid-state-changes");
        pw.println("    Include logs about uid state changes.");
    }

    private void dumpStatesLocked(java.io.PrintWriter pw, java.lang.String filterAttributionTag, int filter, long nowElapsed, com.android.server.appop.AppOpsService.Op op, long now, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix) {
        int i;
        android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> defaultDeviceAttributedOps = op.mDeviceAttributedOps.get("default:0");
        int numAttributions = defaultDeviceAttributedOps.size();
        int i2 = 0;
        while (i2 < numAttributions) {
            if ((filter & 4) == 0 || java.util.Objects.equals(defaultDeviceAttributedOps.keyAt(i2), filterAttributionTag)) {
                pw.print(prefix + defaultDeviceAttributedOps.keyAt(i2) + "=[\n");
                i = i2;
                dumpStatesLocked(pw, nowElapsed, op, defaultDeviceAttributedOps.keyAt(i2), now, sdf, date, prefix + "  ");
                pw.print(prefix + "]\n");
            } else {
                i = i2;
            }
            i2 = i + 1;
        }
    }

    private void dumpStatesLocked(java.io.PrintWriter pw, long nowElapsed, com.android.server.appop.AppOpsService.Op op, java.lang.String attributionTag, long now, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix) {
        android.app.AppOpsManager.AttributedOpEntry entry;
        java.lang.String proxyPkg;
        java.lang.String proxyAttributionTag;
        int flags;
        java.lang.String proxyAttributionTag2;
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        java.util.Date date2 = date;
        java.lang.String str4 = prefix;
        android.app.AppOpsManager.AttributedOpEntry entry2 = (android.app.AppOpsManager.AttributedOpEntry) op.createSingleAttributionEntryLocked(attributionTag).getAttributedOpEntries().get(attributionTag);
        android.util.ArraySet<java.lang.Long> keys = entry2.collectKeys();
        int keyCount = keys.size();
        int k = 0;
        while (k < keyCount) {
            long key = keys.valueAt(k).longValue();
            int uidState = android.app.AppOpsManager.extractUidStateFromKey(key);
            int flags2 = android.app.AppOpsManager.extractFlagsFromKey(key);
            long accessTime = entry2.getLastAccessTime(uidState, uidState, flags2);
            long rejectTime = entry2.getLastRejectTime(uidState, uidState, flags2);
            android.util.ArraySet<java.lang.Long> keys2 = keys;
            int keyCount2 = keyCount;
            long accessDuration = entry2.getLastDuration(uidState, uidState, flags2);
            android.app.AppOpsManager.OpEventProxyInfo proxy = entry2.getLastProxyInfo(uidState, uidState, flags2);
            if (proxy == null) {
                entry = entry2;
                proxyPkg = null;
                proxyAttributionTag = null;
                flags = -1;
            } else {
                java.lang.String proxyPkg2 = proxy.getPackageName();
                java.lang.String proxyAttributionTag3 = proxy.getAttributionTag();
                int proxyUid = proxy.getUid();
                entry = entry2;
                proxyPkg = proxyPkg2;
                proxyAttributionTag = proxyAttributionTag3;
                flags = proxyUid;
            }
            int k2 = k;
            java.lang.String proxyAttributionTag4 = proxyAttributionTag;
            if (accessTime <= 0) {
                proxyAttributionTag2 = proxyAttributionTag4;
                str = ", attributionTag=";
                str2 = "]";
            } else {
                pw.print(str4);
                pw.print("Access: ");
                pw.print(android.app.AppOpsManager.keyToString(key));
                pw.print(" ");
                date2.setTime(accessTime);
                pw.print(sdf.format(date));
                pw.print(" (");
                android.util.TimeUtils.formatDuration(accessTime - now, pw);
                pw.print(")");
                if (accessDuration > 0) {
                    pw.print(" duration=");
                    android.util.TimeUtils.formatDuration(accessDuration, pw);
                }
                if (flags < 0) {
                    proxyAttributionTag2 = proxyAttributionTag4;
                    str = ", attributionTag=";
                    str2 = "]";
                } else {
                    pw.print(" proxy[");
                    pw.print("uid=");
                    pw.print(flags);
                    pw.print(", pkg=");
                    pw.print(proxyPkg);
                    str = ", attributionTag=";
                    pw.print(str);
                    proxyAttributionTag2 = proxyAttributionTag4;
                    pw.print(proxyAttributionTag2);
                    str2 = "]";
                    pw.print(str2);
                }
                pw.println();
            }
            if (rejectTime <= 0) {
                str3 = prefix;
            } else {
                str3 = prefix;
                pw.print(str3);
                pw.print("Reject: ");
                pw.print(android.app.AppOpsManager.keyToString(key));
                date.setTime(rejectTime);
                pw.print(sdf.format(date));
                pw.print(" (");
                android.util.TimeUtils.formatDuration(rejectTime - now, pw);
                pw.print(")");
                if (flags >= 0) {
                    pw.print(" proxy[");
                    pw.print("uid=");
                    pw.print(flags);
                    pw.print(", pkg=");
                    pw.print(proxyPkg);
                    pw.print(str);
                    pw.print(proxyAttributionTag2);
                    pw.print(str2);
                }
                pw.println();
            }
            k = k2 + 1;
            date2 = date;
            str4 = str3;
            keys = keys2;
            keyCount = keyCount2;
            entry2 = entry;
        }
        java.lang.String str5 = str4;
        com.android.server.appop.AttributedOp attributedOp = op.mDeviceAttributedOps.getOrDefault("default:0", new android.util.ArrayMap<>()).get(attributionTag);
        if (attributedOp.isRunning()) {
            long earliestElapsedTime = Long.MAX_VALUE;
            long maxNumStarts = 0;
            int numInProgressEvents = attributedOp.mInProgressEvents.size();
            for (int i = 0; i < numInProgressEvents; i++) {
                com.android.server.appop.AttributedOp.InProgressStartOpEvent event = attributedOp.mInProgressEvents.valueAt(i);
                earliestElapsedTime = java.lang.Math.min(earliestElapsedTime, event.getStartElapsedTime());
                maxNumStarts = java.lang.Math.max(maxNumStarts, event.mNumUnfinishedStarts);
            }
            pw.print(str5 + "Running start at: ");
            android.util.TimeUtils.formatDuration(nowElapsed - earliestElapsedTime, pw);
            pw.println();
            if (maxNumStarts > 1) {
                pw.print(str5 + "startNesting=");
                pw.println(maxNumStarts);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:322:0x06a2 A[Catch: all -> 0x0220, TRY_LEAVE, TryCatch #1 {all -> 0x0220, blocks: (B:99:0x021a, B:107:0x023f, B:111:0x0247, B:112:0x024d, B:114:0x0255, B:115:0x0277, B:121:0x0288, B:124:0x0292, B:127:0x02a0, B:128:0x02af, B:130:0x02b7, B:132:0x02bf, B:137:0x02cf, B:140:0x02dc, B:141:0x02f5, B:153:0x032f, B:156:0x0339, B:161:0x0349, B:162:0x034f, B:163:0x036d, B:165:0x0373, B:173:0x039b, B:175:0x03a3, B:177:0x03ad, B:182:0x03bb, B:183:0x03c1, B:189:0x03ef, B:191:0x03f7, B:194:0x0406, B:196:0x0411, B:200:0x041a, B:204:0x0428, B:205:0x042e, B:208:0x045c, B:210:0x0466, B:212:0x0475, B:213:0x047a, B:214:0x047f, B:222:0x04a4, B:224:0x04ad, B:227:0x04be, B:229:0x04c9, B:233:0x04d4, B:238:0x04e6, B:239:0x04ec, B:242:0x051a, B:244:0x0524, B:246:0x0533, B:247:0x0538, B:248:0x053d, B:256:0x0567, B:258:0x056f, B:261:0x057e, B:263:0x0587, B:267:0x0590, B:271:0x05a3, B:272:0x05a9, B:275:0x05d7, B:277:0x05e3, B:279:0x05f2, B:281:0x05fa, B:282:0x05ff, B:292:0x062c, B:300:0x063e, B:316:0x0695, B:322:0x06a2), top: B:537:0x021a }] */
    /* JADX WARN: Removed duplicated region for block: B:326:0x06af  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x06b5  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x06b8  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x06bc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:347:0x06ee  */
    /* JADX WARN: Removed duplicated region for block: B:395:0x07e3  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x07f3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:480:0x0aab  */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void dump(java.io.FileDescriptor r49, java.io.PrintWriter r50, java.lang.String[] r51) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 3010
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.AppOpsService.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    public void setUserRestrictions(android.os.Bundle restrictions, android.os.IBinder token, int userHandle) {
        checkSystemUid("setUserRestrictions");
        java.util.Objects.requireNonNull(restrictions);
        java.util.Objects.requireNonNull(token);
        for (int i = 0; i < 149; i++) {
            java.lang.String restriction = android.app.AppOpsManager.opToRestriction(i);
            if (restriction != null) {
                setUserRestrictionNoCheck(i, restrictions.getBoolean(restriction, false), token, userHandle, null);
            }
        }
        for (int i2 = 10001; i2 < 10004; i2++) {
            java.lang.String restriction2 = android.app.AppOpsManager.opToRestriction(i2);
            if (restriction2 != null) {
                setUserRestrictionNoCheck(i2, restrictions.getBoolean(restriction2, false), token, userHandle, null);
            }
        }
    }

    public void setUserRestriction(int code, boolean restricted, android.os.IBinder token, int userHandle, android.os.PackageTagsList excludedPackageTags) {
        if (android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
            this.mContext.enforcePermission("android.permission.MANAGE_APP_OPS_RESTRICTIONS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null);
        }
        if (userHandle != android.os.UserHandle.getCallingUserId() && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS") != 0) {
            throw new java.lang.SecurityException("Need INTERACT_ACROSS_USERS_FULL or INTERACT_ACROSS_USERS to interact cross user ");
        }
        verifyIncomingOp(code);
        java.util.Objects.requireNonNull(token);
        setUserRestrictionNoCheck(code, restricted, token, userHandle, excludedPackageTags);
    }

    private void setUserRestrictionNoCheck(int code, boolean restricted, android.os.IBinder token, int userHandle, android.os.PackageTagsList excludedPackageTags) {
        synchronized (this) {
            com.android.server.appop.AppOpsService.ClientUserRestrictionState restrictionState = this.mOpUserRestrictions.get(token);
            if (restrictionState == null) {
                try {
                    restrictionState = new com.android.server.appop.AppOpsService.ClientUserRestrictionState(token);
                    this.mOpUserRestrictions.put(token, restrictionState);
                } catch (android.os.RemoteException e) {
                    return;
                }
            }
            if (restrictionState.setRestriction(code, restricted, excludedPackageTags, userHandle)) {
                this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.appop.AppOpsService$$ExternalSyntheticLambda4(), this, java.lang.Integer.valueOf(code), -2));
                this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda14
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                        ((com.android.server.appop.AppOpsService) obj).updateStartedOpModeForUserForDefaultDevice(((java.lang.Integer) obj2).intValue(), ((java.lang.Boolean) obj3).booleanValue(), ((java.lang.Integer) obj4).intValue());
                    }
                }, this, java.lang.Integer.valueOf(code), java.lang.Boolean.valueOf(restricted), java.lang.Integer.valueOf(userHandle)));
            }
            if (restrictionState.isDefault()) {
                this.mOpUserRestrictions.remove(token);
                restrictionState.destroy();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStartedOpModeForUserForDefaultDevice(int code, boolean restricted, int userId) {
        synchronized (this) {
            int numUids = this.mUidStates.size();
            for (int uidNum = 0; uidNum < numUids; uidNum++) {
                int uid = this.mUidStates.keyAt(uidNum);
                if (userId == -1 || android.os.UserHandle.getUserId(uid) == userId) {
                    updateStartedOpModeForUidForDefaultDeviceLocked(code, restricted, uid);
                }
            }
        }
    }

    private void updateStartedOpModeForUidForDefaultDeviceLocked(int code, boolean restricted, int uid) {
        int mode;
        int tagIndex;
        android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> defaultDeviceAttributedOps;
        int mode2;
        com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.get(uid);
        if (uidState == null) {
            return;
        }
        int numPkgOps = uidState.pkgOps.size();
        for (int pkgNum = 0; pkgNum < numPkgOps; pkgNum++) {
            com.android.server.appop.AppOpsService.Ops ops = uidState.pkgOps.valueAt(pkgNum);
            com.android.server.appop.AppOpsService.Op op = ops != null ? ops.get(code) : null;
            if (op != null && ((mode = this.mAppOpsCheckingService.getPackageMode(op.packageName, op.op, android.os.UserHandle.getUserId(op.uid))) == 0 || mode == 4)) {
                android.util.ArrayMap<java.lang.String, com.android.server.appop.AttributedOp> defaultDeviceAttributedOps2 = op.mDeviceAttributedOps.get("default:0");
                int tagIndex2 = 0;
                while (tagIndex2 < defaultDeviceAttributedOps2.size()) {
                    com.android.server.appop.AttributedOp attrOp = defaultDeviceAttributedOps2.valueAt(tagIndex2);
                    if (restricted && attrOp.isRunning()) {
                        attrOp.pause();
                        tagIndex = tagIndex2;
                        defaultDeviceAttributedOps = defaultDeviceAttributedOps2;
                        mode2 = mode;
                    } else if (attrOp.isPaused()) {
                        android.app.AppOpsManager.RestrictionBypass bypass = verifyAndGetBypass(uid, ops.packageName, attrOp.tag).bypass;
                        tagIndex = tagIndex2;
                        defaultDeviceAttributedOps = defaultDeviceAttributedOps2;
                        mode2 = mode;
                        if (!isOpRestrictedLocked(uid, code, ops.packageName, attrOp.tag, 0, bypass, false)) {
                            attrOp.resume();
                        }
                    } else {
                        tagIndex = tagIndex2;
                        defaultDeviceAttributedOps = defaultDeviceAttributedOps2;
                        mode2 = mode;
                    }
                    tagIndex2 = tagIndex + 1;
                    defaultDeviceAttributedOps2 = defaultDeviceAttributedOps;
                    mode = mode2;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWatchersOnDefaultDevice(int code, int uid) {
        synchronized (this) {
            android.util.ArraySet<com.android.server.appop.OnOpModeChangedListener> modeChangedListenerSet = this.mOpModeWatchers.get(code);
            if (modeChangedListenerSet == null) {
                return;
            }
            notifyOpChanged(new android.util.ArraySet<>((android.util.ArraySet) modeChangedListenerSet), code, uid, (java.lang.String) null, "default:0");
        }
    }

    public void removeUser(int userHandle) throws android.os.RemoteException {
        checkSystemUid("removeUser");
        synchronized (this) {
            int tokenCount = this.mOpUserRestrictions.size();
            for (int i = tokenCount - 1; i >= 0; i--) {
                com.android.server.appop.AppOpsService.ClientUserRestrictionState opRestrictions = this.mOpUserRestrictions.valueAt(i);
                opRestrictions.removeUser(userHandle);
            }
            removeUidsForUserLocked(userHandle);
        }
    }

    public boolean isOperationActive(int code, int uid, java.lang.String packageName) {
        java.lang.String resolvedPackageName;
        if (android.os.Binder.getCallingUid() != uid && this.mContext.checkCallingOrSelfPermission("android.permission.WATCH_APPOPS") != 0) {
            return false;
        }
        verifyIncomingOp(code);
        if (!isIncomingPackageValid(packageName, android.os.UserHandle.getUserId(uid)) || (resolvedPackageName = android.app.AppOpsManager.resolvePackageName(uid, packageName)) == null) {
            return false;
        }
        synchronized (this) {
            com.android.server.appop.AppOpsService.Ops pkgOps = getOpsLocked(uid, resolvedPackageName, null, false, null, false);
            if (pkgOps == null) {
                return false;
            }
            com.android.server.appop.AppOpsService.Op op = pkgOps.get(code);
            if (op == null) {
                return false;
            }
            return op.isRunning();
        }
    }

    public boolean isProxying(int op, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, int proxiedUid, java.lang.String proxiedPackageName) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(proxyPackageName);
        java.util.Objects.requireNonNull(proxiedPackageName);
        long callingUid = android.os.Binder.getCallingUid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                java.util.List<android.app.AppOpsManager.PackageOps> packageOps = getOpsForPackage(proxiedUid, proxiedPackageName, new int[]{op});
                boolean z = false;
                if (packageOps == null || packageOps.isEmpty()) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    return false;
                }
                java.util.List<android.app.AppOpsManager.OpEntry> opEntries = packageOps.get(0).getOps();
                if (opEntries.isEmpty()) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    return false;
                }
                android.app.AppOpsManager.OpEntry opEntry = opEntries.get(0);
                if (!opEntry.isRunning()) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    return false;
                }
                android.app.AppOpsManager.OpEventProxyInfo proxyInfo = opEntry.getLastProxyInfo(24);
                if (proxyInfo != null && callingUid == proxyInfo.getUid()) {
                    try {
                        if (proxyPackageName.equals(proxyInfo.getPackageName())) {
                            try {
                                if (java.util.Objects.equals(proxyAttributionTag, proxyInfo.getAttributionTag())) {
                                    z = true;
                                }
                            } catch (java.lang.Throwable th) {
                                th = th;
                            }
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return z;
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
        android.os.Binder.restoreCallingIdentity(identity);
        throw th;
    }

    public void resetPackageOpsNoHistory(java.lang.String packageName) {
        resetPackageOpsNoHistory_enforcePermission();
        synchronized (this) {
            int uid = this.mPackageManagerInternal.getPackageUid(packageName, 0L, android.os.UserHandle.getCallingUserId());
            if (uid == -1) {
                return;
            }
            com.android.server.appop.AppOpsService.UidState uidState = this.mUidStates.get(uid);
            if (uidState == null) {
                return;
            }
            com.android.server.appop.AppOpsService.Ops removedOps = uidState.pkgOps.remove(packageName);
            this.mAppOpsCheckingService.removePackage(packageName, android.os.UserHandle.getUserId(uid));
            if (removedOps != null) {
                scheduleFastWriteLocked();
            }
        }
    }

    public void setHistoryParameters(int mode, long baseSnapshotInterval, int compressionStep) {
        setHistoryParameters_enforcePermission();
        this.mHistoricalRegistry.setHistoryParameters(mode, baseSnapshotInterval, compressionStep);
    }

    public void offsetHistory(long offsetMillis) {
        offsetHistory_enforcePermission();
        this.mHistoricalRegistry.offsetHistory(offsetMillis);
        this.mHistoricalRegistry.offsetDiscreteHistory(offsetMillis);
    }

    public void addHistoricalOps(android.app.AppOpsManager.HistoricalOps ops) {
        addHistoricalOps_enforcePermission();
        this.mHistoricalRegistry.addHistoricalOps(ops);
    }

    public void resetHistoryParameters() {
        resetHistoryParameters_enforcePermission();
        this.mHistoricalRegistry.resetHistoryParameters();
    }

    public void clearHistory() {
        clearHistory_enforcePermission();
        this.mHistoricalRegistry.clearAllHistory();
    }

    public void rebootHistory(long offlineDurationMillis) {
        rebootHistory_enforcePermission();
        com.android.internal.util.Preconditions.checkArgument(offlineDurationMillis >= 0);
        this.mHistoricalRegistry.shutdown();
        if (offlineDurationMillis > 0) {
            android.os.SystemClock.sleep(offlineDurationMillis);
        }
        this.mHistoricalRegistry = new com.android.server.appop.HistoricalRegistry(this.mHistoricalRegistry);
        this.mHistoricalRegistry.systemReady(this.mContext.getContentResolver());
        this.mHistoricalRegistry.persistPendingHistory();
    }

    public com.android.internal.app.MessageSamplingConfig reportRuntimeAppOpAccessMessageAndGetConfig(java.lang.String packageName, android.app.SyncNotedAppOp notedAppOp, java.lang.String message) {
        int uid = android.os.Binder.getCallingUid();
        java.util.Objects.requireNonNull(packageName);
        synchronized (this) {
            switchPackageIfBootTimeOrRarelyUsedLocked(packageName);
            if (!packageName.equals(this.mSampledPackage)) {
                return new com.android.internal.app.MessageSamplingConfig(-1, 0, java.time.Instant.now().plus(1L, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.HOURS).toEpochMilli());
            }
            java.util.Objects.requireNonNull(notedAppOp);
            java.util.Objects.requireNonNull(message);
            reportRuntimeAppOpAccessMessageInternalLocked(uid, packageName, android.app.AppOpsManager.strOpToOp(notedAppOp.getOp()), notedAppOp.getAttributionTag(), message);
            return new com.android.internal.app.MessageSamplingConfig(this.mSampledAppOpCode, this.mAcceptableLeftDistance, java.time.Instant.now().plus(1L, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.HOURS).toEpochMilli());
        }
    }

    private void reportRuntimeAppOpAccessMessageAsyncLocked(int uid, java.lang.String packageName, int opCode, java.lang.String attributionTag, java.lang.String message) {
        switchPackageIfBootTimeOrRarelyUsedLocked(packageName);
        if (!java.util.Objects.equals(this.mSampledPackage, packageName)) {
            return;
        }
        reportRuntimeAppOpAccessMessageInternalLocked(uid, packageName, opCode, attributionTag, message);
    }

    private void reportRuntimeAppOpAccessMessageInternalLocked(int uid, java.lang.String packageName, int opCode, java.lang.String attributionTag, java.lang.String message) {
        int newLeftDistance = android.app.AppOpsManager.leftCircularDistance(opCode, this.mSampledAppOpCode, 149);
        if (this.mAcceptableLeftDistance < newLeftDistance && this.mSamplingStrategy != 4) {
            return;
        }
        if (this.mAcceptableLeftDistance > newLeftDistance && this.mSamplingStrategy != 4) {
            this.mAcceptableLeftDistance = newLeftDistance;
            this.mMessagesCollectedCount = 0.0f;
        }
        this.mMessagesCollectedCount += 1.0f;
        if (java.util.concurrent.ThreadLocalRandom.current().nextFloat() <= 1.0f / this.mMessagesCollectedCount) {
            this.mCollectedRuntimePermissionMessage = new android.app.RuntimeAppOpAccessMessage(uid, opCode, packageName, attributionTag, message, this.mSamplingStrategy);
        }
    }

    public android.app.RuntimeAppOpAccessMessage collectRuntimeAppOpAccessMessage() {
        android.app.RuntimeAppOpAccessMessage result;
        android.app.ActivityManagerInternal ami = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        boolean isCallerInstrumented = ami.getInstrumentationSourceUid(android.os.Binder.getCallingUid()) != -1;
        boolean isCallerSystem = android.os.Binder.getCallingPid() == android.os.Process.myPid();
        if (!isCallerSystem && !isCallerInstrumented) {
            return null;
        }
        this.mContext.enforcePermission("android.permission.GET_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null);
        synchronized (this) {
            result = this.mCollectedRuntimePermissionMessage;
            this.mCollectedRuntimePermissionMessage = null;
        }
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.appop.AppOpsService$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.appop.AppOpsService) obj).getPackageListAndResample();
            }
        }, this));
        return result;
    }

    private void switchPackageIfBootTimeOrRarelyUsedLocked(java.lang.String packageName) {
        if (this.mSampledPackage == null) {
            if (java.util.concurrent.ThreadLocalRandom.current().nextFloat() < 0.5f) {
                this.mSamplingStrategy = 3;
                resampleAppOpForPackageLocked(packageName, true);
                return;
            }
            return;
        }
        if (this.mRarelyUsedPackages.contains(packageName)) {
            this.mRarelyUsedPackages.remove(packageName);
            if (java.util.concurrent.ThreadLocalRandom.current().nextFloat() < 0.5f) {
                this.mSamplingStrategy = 2;
                resampleAppOpForPackageLocked(packageName, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<java.lang.String> getPackageListAndResample() {
        java.util.List<java.lang.String> packageNames = getPackageNamesForSampling();
        synchronized (this) {
            resamplePackageAndAppOpLocked(packageNames);
        }
        return packageNames;
    }

    private void resamplePackageAndAppOpLocked(java.util.List<java.lang.String> packageNames) {
        if (packageNames.isEmpty()) {
            return;
        }
        if (java.util.concurrent.ThreadLocalRandom.current().nextFloat() < 0.5f) {
            this.mSamplingStrategy = 1;
            resampleAppOpForPackageLocked(packageNames.get(java.util.concurrent.ThreadLocalRandom.current().nextInt(packageNames.size())), true);
        } else {
            this.mSamplingStrategy = 4;
            resampleAppOpForPackageLocked(packageNames.get(java.util.concurrent.ThreadLocalRandom.current().nextInt(packageNames.size())), false);
        }
    }

    private void resampleAppOpForPackageLocked(java.lang.String packageName, boolean pickOp) {
        this.mMessagesCollectedCount = 0.0f;
        this.mSampledAppOpCode = pickOp ? java.util.concurrent.ThreadLocalRandom.current().nextInt(149) : -1;
        this.mAcceptableLeftDistance = 148;
        this.mSampledPackage = packageName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeRarelyUsedPackagesList(final android.util.ArraySet<java.lang.String> candidates) {
        android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        java.util.List<java.lang.String> runtimeAppOpsList = getRuntimeAppOpsList();
        android.app.AppOpsManager.HistoricalOpsRequest histOpsRequest = new android.app.AppOpsManager.HistoricalOpsRequest.Builder(java.lang.Math.max(java.time.Instant.now().minus(7L, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.DAYS).toEpochMilli(), 0L), Long.MAX_VALUE).setOpNames(runtimeAppOpsList).setFlags(9).build();
        appOps.getHistoricalOps(histOpsRequest, android.os.AsyncTask.THREAD_POOL_EXECUTOR, new java.util.function.Consumer<android.app.AppOpsManager.HistoricalOps>() { // from class: com.android.server.appop.AppOpsService.9
            @Override // java.util.function.Consumer
            public void accept(android.app.AppOpsManager.HistoricalOps histOps) {
                int uidCount = histOps.getUidCount();
                for (int uidIdx = 0; uidIdx < uidCount; uidIdx++) {
                    android.app.AppOpsManager.HistoricalUidOps uidOps = histOps.getUidOpsAt(uidIdx);
                    int pkgCount = uidOps.getPackageCount();
                    for (int pkgIdx = 0; pkgIdx < pkgCount; pkgIdx++) {
                        java.lang.String packageName = uidOps.getPackageOpsAt(pkgIdx).getPackageName();
                        if (candidates.contains(packageName)) {
                            android.app.AppOpsManager.HistoricalPackageOps packageOps = uidOps.getPackageOpsAt(pkgIdx);
                            if (packageOps.getOpCount() != 0) {
                                candidates.remove(packageName);
                            }
                        }
                    }
                }
                synchronized (this) {
                    int numPkgs = com.android.server.appop.AppOpsService.this.mRarelyUsedPackages.size();
                    for (int i = 0; i < numPkgs; i++) {
                        candidates.add((java.lang.String) com.android.server.appop.AppOpsService.this.mRarelyUsedPackages.valueAt(i));
                    }
                    com.android.server.appop.AppOpsService.this.mRarelyUsedPackages = candidates;
                }
            }
        });
    }

    private java.util.List<java.lang.String> getRuntimeAppOpsList() {
        java.util.ArrayList<java.lang.String> result = new java.util.ArrayList<>();
        for (int i = 0; i < 149; i++) {
            if (shouldCollectNotes(i)) {
                result.add(android.app.AppOpsManager.opToPublicName(i));
            }
        }
        return result;
    }

    private java.util.List<java.lang.String> getPackageNamesForSampling() {
        java.util.List<java.lang.String> packageNames = new java.util.ArrayList<>();
        android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        com.android.server.pm.PackageList packages = packageManagerInternal.getPackageList();
        for (java.lang.String packageName : packages.getPackageNames()) {
            android.content.pm.PackageInfo pkg = packageManagerInternal.getPackageInfo(packageName, 4096L, android.os.Process.myUid(), this.mContext.getUserId());
            if (isSamplingTarget(pkg)) {
                packageNames.add(pkg.packageName);
            }
        }
        return packageNames;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSamplingTarget(android.content.pm.PackageInfo pkg) {
        java.lang.String[] requestedPermissions;
        android.content.pm.PermissionInfo permissionInfo;
        if (pkg == null || (requestedPermissions = pkg.requestedPermissions) == null) {
            return false;
        }
        for (java.lang.String permission : requestedPermissions) {
            try {
                permissionInfo = this.mContext.getPackageManager().getPermissionInfo(permission, 0);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
            if (permissionInfo.getProtection() == 1) {
                return true;
            }
        }
        return false;
    }

    private void removeUidsForUserLocked(int userHandle) {
        for (int i = this.mUidStates.size() - 1; i >= 0; i--) {
            int uid = this.mUidStates.keyAt(i);
            if (android.os.UserHandle.getUserId(uid) == userHandle) {
                this.mUidStates.valueAt(i).clear();
                this.mUidStates.removeAt(i);
            }
        }
    }

    private void checkSystemUid(java.lang.String function) {
        int uid = android.os.Binder.getCallingUid();
        if (uid != 1000) {
            throw new java.lang.SecurityException(function + " must by called by the system");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int resolveNonAppUid(java.lang.String packageName) {
        byte b;
        if (packageName == null) {
            return -1;
        }
        switch (packageName.hashCode()) {
            case -1336564963:
                b = !packageName.equals("dumpstate") ? (byte) -1 : (byte) 2;
                break;
            case -31178072:
                b = !packageName.equals("cameraserver") ? (byte) -1 : (byte) 5;
                break;
            case 3506402:
                b = !packageName.equals("root") ? (byte) -1 : (byte) 0;
                break;
            case 103772132:
                b = !packageName.equals("media") ? (byte) -1 : (byte) 3;
                break;
            case 109403696:
                b = !packageName.equals("shell") ? (byte) -1 : (byte) 1;
                break;
            case 1344606873:
                b = !packageName.equals("audioserver") ? (byte) -1 : (byte) 4;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
        }
        return -1;
    }

    private static java.lang.String[] getPackagesForUid(int uid) {
        java.lang.String[] packageNames = null;
        if (android.app.AppGlobals.getPackageManager() != null) {
            try {
                packageNames = android.app.AppGlobals.getPackageManager().getPackagesForUid(uid);
            } catch (android.os.RemoteException e) {
            }
        }
        if (packageNames == null) {
            return libcore.util.EmptyArray.STRING;
        }
        return packageNames;
    }

    private java.lang.String getPersistentId(int virtualDeviceId) {
        if (virtualDeviceId == 0 || this.mVirtualDeviceManagerInternal == null) {
            return "default:0";
        }
        java.lang.String persistentId = this.mVirtualDeviceManagerInternal.getPersistentIdForDevice(virtualDeviceId);
        if (persistentId == null) {
            persistentId = this.mKnownDeviceIds.get(virtualDeviceId);
        }
        if (persistentId != null) {
            return persistentId;
        }
        throw new java.lang.IllegalStateException("Requested persistentId for invalid virtualDeviceId: " + virtualDeviceId);
    }

    private final class ClientUserRestrictionState implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder token;

        ClientUserRestrictionState(android.os.IBinder token) throws android.os.RemoteException {
            token.linkToDeath(this, 0);
            this.token = token;
        }

        public boolean setRestriction(int code, boolean restricted, android.os.PackageTagsList excludedPackageTags, int userId) {
            return com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.setUserRestriction(this.token, userId, code, restricted, excludedPackageTags);
        }

        public boolean hasRestriction(int code, java.lang.String packageName, java.lang.String attributionTag, int userId, boolean isCheckOp) {
            return com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.getUserRestriction(this.token, userId, code, packageName, attributionTag, isCheckOp);
        }

        public void removeUser(int userId) {
            com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.clearUserRestrictions(this.token, java.lang.Integer.valueOf(userId));
        }

        public boolean isDefault() {
            return !com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.hasUserRestrictions(this.token);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.appop.AppOpsService.this) {
                com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.clearUserRestrictions(this.token);
                com.android.server.appop.AppOpsService.this.mOpUserRestrictions.remove(this.token);
                destroy();
            }
        }

        public void destroy() {
            this.token.unlinkToDeath(this, 0);
        }
    }

    private final class ClientGlobalRestrictionState implements android.os.IBinder.DeathRecipient {
        final android.os.IBinder mToken;

        ClientGlobalRestrictionState(android.os.IBinder token) throws android.os.RemoteException {
            token.linkToDeath(this, 0);
            this.mToken = token;
        }

        boolean setRestriction(int code, boolean restricted) {
            return com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.setGlobalRestriction(this.mToken, code, restricted);
        }

        boolean hasRestriction(int code) {
            return com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.getGlobalRestriction(this.mToken, code);
        }

        boolean isDefault() {
            return !com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.hasGlobalRestrictions(this.mToken);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.appop.AppOpsService.this.mAppOpsRestrictions.clearGlobalRestrictions(this.mToken);
            com.android.server.appop.AppOpsService.this.mOpGlobalRestrictions.remove(this.mToken);
            destroy();
        }

        void destroy() {
            this.mToken.unlinkToDeath(this, 0);
        }
    }

    private final class AppOpsManagerLocalImpl implements com.android.server.appop.AppOpsManagerLocal {
        private AppOpsManagerLocalImpl() {
        }

        @Override // com.android.server.appop.AppOpsManagerLocal
        public boolean isUidInForeground(int uid) {
            boolean zIsUidInForeground;
            synchronized (com.android.server.appop.AppOpsService.this) {
                zIsUidInForeground = com.android.server.appop.AppOpsService.this.mUidStateTracker.isUidInForeground(uid);
            }
            return zIsUidInForeground;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class AppOpsManagerInternalImpl extends android.app.AppOpsManagerInternal {
        private AppOpsManagerInternalImpl() {
        }

        public void setDeviceAndProfileOwners(android.util.SparseIntArray owners) {
            synchronized (com.android.server.appop.AppOpsService.this) {
                com.android.server.appop.AppOpsService.this.mProfileOwners = owners;
            }
        }

        public void updateAppWidgetVisibility(android.util.SparseArray<java.lang.String> uidPackageNames, boolean visible) {
            com.android.server.appop.AppOpsService.this.updateAppWidgetVisibility(uidPackageNames, visible);
        }

        public void setUidModeFromPermissionPolicy(int code, int uid, int mode, com.android.internal.app.IAppOpsCallback callback) throws java.lang.Throwable {
            com.android.server.appop.AppOpsService.this.setUidMode(code, uid, mode, callback);
        }

        public void setModeFromPermissionPolicy(int code, int uid, java.lang.String packageName, int mode, com.android.internal.app.IAppOpsCallback callback) {
            com.android.server.appop.AppOpsService.this.setMode(code, uid, packageName, mode, callback);
        }

        public void setGlobalRestriction(int code, boolean restricted, android.os.IBinder token) {
            if (android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
                throw new java.lang.SecurityException("Only the system can set global restrictions");
            }
            synchronized (com.android.server.appop.AppOpsService.this) {
                com.android.server.appop.AppOpsService.ClientGlobalRestrictionState restrictionState = (com.android.server.appop.AppOpsService.ClientGlobalRestrictionState) com.android.server.appop.AppOpsService.this.mOpGlobalRestrictions.get(token);
                if (restrictionState == null) {
                    try {
                        restrictionState = com.android.server.appop.AppOpsService.this.new ClientGlobalRestrictionState(token);
                        com.android.server.appop.AppOpsService.this.mOpGlobalRestrictions.put(token, restrictionState);
                    } catch (android.os.RemoteException e) {
                        return;
                    }
                }
                if (restrictionState.setRestriction(code, restricted)) {
                    com.android.server.appop.AppOpsService.this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.appop.AppOpsService$AppOpsManagerInternalImpl$$ExternalSyntheticLambda0
                        public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                            ((com.android.server.appop.AppOpsService) obj).notifyWatchersOnDefaultDevice(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue());
                        }
                    }, com.android.server.appop.AppOpsService.this, java.lang.Integer.valueOf(code), -2));
                    com.android.server.appop.AppOpsService.this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.appop.AppOpsService$AppOpsManagerInternalImpl$$ExternalSyntheticLambda1
                        public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                            ((com.android.server.appop.AppOpsService) obj).updateStartedOpModeForUserForDefaultDevice(((java.lang.Integer) obj2).intValue(), ((java.lang.Boolean) obj3).booleanValue(), ((java.lang.Integer) obj4).intValue());
                        }
                    }, com.android.server.appop.AppOpsService.this, java.lang.Integer.valueOf(code), java.lang.Boolean.valueOf(restricted), -1));
                }
                if (restrictionState.isDefault()) {
                    com.android.server.appop.AppOpsService.this.mOpGlobalRestrictions.remove(token);
                    restrictionState.destroy();
                }
            }
        }

        public int getOpRestrictionCount(int code, android.os.UserHandle user, java.lang.String pkg, java.lang.String attributionTag) {
            int number = 0;
            synchronized (com.android.server.appop.AppOpsService.this) {
                int numRestrictions = com.android.server.appop.AppOpsService.this.mOpUserRestrictions.size();
                for (int i = 0; i < numRestrictions; i++) {
                    if (((com.android.server.appop.AppOpsService.ClientUserRestrictionState) com.android.server.appop.AppOpsService.this.mOpUserRestrictions.valueAt(i)).hasRestriction(code, pkg, attributionTag, user.getIdentifier(), false)) {
                        number++;
                    }
                }
                int numRestrictions2 = com.android.server.appop.AppOpsService.this.mOpGlobalRestrictions.size();
                for (int i2 = 0; i2 < numRestrictions2; i2++) {
                    if (((com.android.server.appop.AppOpsService.ClientGlobalRestrictionState) com.android.server.appop.AppOpsService.this.mOpGlobalRestrictions.valueAt(i2)).hasRestriction(code)) {
                        number++;
                    }
                }
            }
            return number;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeNoteOps() {
        synchronized (this) {
            this.mWriteNoteOpsScheduled = false;
        }
        synchronized (this.mNoteOpCallerStacktracesFile) {
            try {
                java.io.FileWriter writer = new java.io.FileWriter(this.mNoteOpCallerStacktracesFile);
                try {
                    int numTraces = this.mNoteOpCallerStacktraces.size();
                    for (int i = 0; i < numTraces; i++) {
                        writer.write(this.mNoteOpCallerStacktraces.valueAt(i).asJson());
                        writer.write(",");
                    }
                    writer.close();
                } catch (java.lang.Throwable th) {
                    try {
                        writer.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Failed to load opsValidation file for FileWriter", e);
            }
        }
    }

    static class NoteOpTrace {
        static final java.lang.String OP = "op";
        static final java.lang.String PACKAGENAME = "packageName";
        static final java.lang.String STACKTRACE = "stackTrace";
        static final java.lang.String VERSION = "version";
        private final int mOp;
        private final java.lang.String mPackageName;
        private final java.lang.String mStackTrace;
        private final long mVersion;

        static com.android.server.appop.AppOpsService.NoteOpTrace fromJson(java.lang.String jsonTrace) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(jsonTrace.concat("}"));
                return new com.android.server.appop.AppOpsService.NoteOpTrace(obj.getString(STACKTRACE), obj.getInt(OP), obj.getString("packageName"), obj.getLong(VERSION));
            } catch (org.json.JSONException e) {
                android.util.Slog.e(com.android.server.appop.AppOpsService.TAG, "Error constructing NoteOpTrace object JSON trace format incorrect", e);
                return null;
            }
        }

        NoteOpTrace(java.lang.String stackTrace, int op, java.lang.String packageName, long version) {
            this.mStackTrace = stackTrace;
            this.mOp = op;
            this.mPackageName = packageName;
            this.mVersion = version;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.appop.AppOpsService.NoteOpTrace that = (com.android.server.appop.AppOpsService.NoteOpTrace) o;
            if (this.mOp == that.mOp && this.mVersion == that.mVersion && this.mStackTrace.equals(that.mStackTrace) && java.util.Objects.equals(this.mPackageName, that.mPackageName)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.mStackTrace, java.lang.Integer.valueOf(this.mOp), this.mPackageName, java.lang.Long.valueOf(this.mVersion));
        }

        public java.lang.String asJson() {
            return "{\"stackTrace\":\"" + this.mStackTrace.replace("\n", "\\n") + "\",\"" + OP + "\":" + this.mOp + ",\"packageName\":\"" + this.mPackageName + "\",\"" + VERSION + "\":" + this.mVersion + '}';
        }
    }

    public void collectNoteOpCallsForValidation(java.lang.String stackTrace, int op, java.lang.String packageName, long version) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class CheckOpsDelegateDispatcher {
        private final android.app.AppOpsManagerInternal.CheckOpsDelegate mCheckOpsDelegate;
        private final android.app.AppOpsManagerInternal.CheckOpsDelegate mPolicy;

        CheckOpsDelegateDispatcher(android.app.AppOpsManagerInternal.CheckOpsDelegate policy, android.app.AppOpsManagerInternal.CheckOpsDelegate checkOpsDelegate) {
            this.mPolicy = policy;
            this.mCheckOpsDelegate = checkOpsDelegate;
        }

        public int checkOperation(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean raw) {
            if (this.mPolicy != null) {
                if (this.mCheckOpsDelegate != null) {
                    return this.mPolicy.checkOperation(code, uid, packageName, attributionTag, virtualDeviceId, raw, new com.android.internal.util.function.HexFunction() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda11
                        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                            return java.lang.Integer.valueOf(this.f$0.checkDelegateOperationImpl(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), (java.lang.String) obj3, (java.lang.String) obj4, ((java.lang.Integer) obj5).intValue(), ((java.lang.Boolean) obj6).booleanValue()));
                        }
                    });
                }
                return this.mPolicy.checkOperation(code, uid, packageName, attributionTag, virtualDeviceId, raw, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda12(com.android.server.appop.AppOpsService.this));
            }
            if (this.mCheckOpsDelegate != null) {
                return checkDelegateOperationImpl(code, uid, packageName, attributionTag, virtualDeviceId, raw);
            }
            return com.android.server.appop.AppOpsService.this.checkOperationImpl(code, uid, packageName, attributionTag, virtualDeviceId, raw);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int checkDelegateOperationImpl(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean raw) {
            return this.mCheckOpsDelegate.checkOperation(code, uid, packageName, attributionTag, virtualDeviceId, raw, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda12(com.android.server.appop.AppOpsService.this));
        }

        public int checkAudioOperation(int code, int usage, int uid, java.lang.String packageName) {
            if (this.mPolicy != null) {
                if (this.mCheckOpsDelegate != null) {
                    return this.mPolicy.checkAudioOperation(code, usage, uid, packageName, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda8
                        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                            return java.lang.Integer.valueOf(this.f$0.checkDelegateAudioOperationImpl(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), (java.lang.String) obj4));
                        }
                    });
                }
                return this.mPolicy.checkAudioOperation(code, usage, uid, packageName, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda5(com.android.server.appop.AppOpsService.this));
            }
            if (this.mCheckOpsDelegate != null) {
                return checkDelegateAudioOperationImpl(code, usage, uid, packageName);
            }
            return com.android.server.appop.AppOpsService.this.checkAudioOperationImpl(code, usage, uid, packageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int checkDelegateAudioOperationImpl(int code, int usage, int uid, java.lang.String packageName) {
            return this.mCheckOpsDelegate.checkAudioOperation(code, usage, uid, packageName, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda5(com.android.server.appop.AppOpsService.this));
        }

        public android.app.SyncNotedAppOp noteOperation(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage) {
            if (this.mPolicy != null) {
                if (this.mCheckOpsDelegate == null) {
                    return this.mPolicy.noteOperation(code, uid, packageName, attributionTag, virtualDeviceId, shouldCollectAsyncNotedOp, message, shouldCollectMessage, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda0(com.android.server.appop.AppOpsService.this));
                }
                return this.mPolicy.noteOperation(code, uid, packageName, attributionTag, virtualDeviceId, shouldCollectAsyncNotedOp, message, shouldCollectMessage, new com.android.internal.util.function.OctFunction() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda2
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8) {
                        return this.f$0.noteDelegateOperationImpl(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), (java.lang.String) obj3, (java.lang.String) obj4, ((java.lang.Integer) obj5).intValue(), ((java.lang.Boolean) obj6).booleanValue(), (java.lang.String) obj7, ((java.lang.Boolean) obj8).booleanValue());
                    }
                });
            }
            if (this.mCheckOpsDelegate != null) {
                return noteDelegateOperationImpl(code, uid, packageName, attributionTag, virtualDeviceId, shouldCollectAsyncNotedOp, message, shouldCollectMessage);
            }
            return com.android.server.appop.AppOpsService.this.noteOperationImpl(code, uid, packageName, attributionTag, virtualDeviceId, shouldCollectAsyncNotedOp, message, shouldCollectMessage);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.app.SyncNotedAppOp noteDelegateOperationImpl(int code, int uid, java.lang.String packageName, java.lang.String featureId, int virtualDeviceId, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage) {
            return this.mCheckOpsDelegate.noteOperation(code, uid, packageName, featureId, virtualDeviceId, shouldCollectAsyncNotedOp, message, shouldCollectMessage, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda0(com.android.server.appop.AppOpsService.this));
        }

        public android.app.SyncNotedAppOp noteProxyOperation(int code, android.content.AttributionSource attributionSource, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation) {
            if (this.mPolicy != null) {
                if (this.mCheckOpsDelegate != null) {
                    return this.mPolicy.noteProxyOperation(code, attributionSource, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, new com.android.internal.util.function.HexFunction() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda14
                        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                            return this.f$0.noteDelegateProxyOperationImpl(((java.lang.Integer) obj).intValue(), (android.content.AttributionSource) obj2, ((java.lang.Boolean) obj3).booleanValue(), (java.lang.String) obj4, ((java.lang.Boolean) obj5).booleanValue(), ((java.lang.Boolean) obj6).booleanValue());
                        }
                    });
                }
                return this.mPolicy.noteProxyOperation(code, attributionSource, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda1(com.android.server.appop.AppOpsService.this));
            }
            if (this.mCheckOpsDelegate != null) {
                return noteDelegateProxyOperationImpl(code, attributionSource, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation);
            }
            return com.android.server.appop.AppOpsService.this.noteProxyOperationImpl(code, attributionSource, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.app.SyncNotedAppOp noteDelegateProxyOperationImpl(int code, android.content.AttributionSource attributionSource, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation) {
            return this.mCheckOpsDelegate.noteProxyOperation(code, attributionSource, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda1(com.android.server.appop.AppOpsService.this));
        }

        public android.app.SyncNotedAppOp startOperation(android.os.IBinder token, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId) {
            if (this.mPolicy != null) {
                return this.mCheckOpsDelegate != null ? this.mPolicy.startOperation(token, code, uid, packageName, attributionTag, virtualDeviceId, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId, new com.android.internal.util.function.DodecFunction() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda9
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9, java.lang.Object obj10, java.lang.Object obj11, java.lang.Object obj12) {
                        return this.f$0.startDelegateOperationImpl((android.os.IBinder) obj, ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), (java.lang.String) obj4, (java.lang.String) obj5, ((java.lang.Integer) obj6).intValue(), ((java.lang.Boolean) obj7).booleanValue(), ((java.lang.Boolean) obj8).booleanValue(), (java.lang.String) obj9, ((java.lang.Boolean) obj10).booleanValue(), ((java.lang.Integer) obj11).intValue(), ((java.lang.Integer) obj12).intValue());
                    }
                }) : this.mPolicy.startOperation(token, code, uid, packageName, attributionTag, virtualDeviceId, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda10(com.android.server.appop.AppOpsService.this));
            }
            if (this.mCheckOpsDelegate != null) {
                return startDelegateOperationImpl(token, code, uid, packageName, attributionTag, virtualDeviceId, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId);
            }
            return com.android.server.appop.AppOpsService.this.startOperationImpl(token, code, uid, packageName, attributionTag, virtualDeviceId, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.app.SyncNotedAppOp startDelegateOperationImpl(android.os.IBinder token, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId) {
            return this.mCheckOpsDelegate.startOperation(token, code, uid, packageName, attributionTag, virtualDeviceId, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, attributionFlags, attributionChainId, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda10(com.android.server.appop.AppOpsService.this));
        }

        public android.app.SyncNotedAppOp startProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlags, int attributionChainId) {
            if (this.mPolicy != null) {
                if (this.mCheckOpsDelegate == null) {
                    return this.mPolicy.startProxyOperation(clientId, code, attributionSource, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda7(com.android.server.appop.AppOpsService.this));
                }
                return this.mPolicy.startProxyOperation(clientId, code, attributionSource, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId, new com.android.internal.util.function.UndecFunction() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda6
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9, java.lang.Object obj10, java.lang.Object obj11) {
                        return this.f$0.startDelegateProxyOperationImpl((android.os.IBinder) obj, ((java.lang.Integer) obj2).intValue(), (android.content.AttributionSource) obj3, ((java.lang.Boolean) obj4).booleanValue(), ((java.lang.Boolean) obj5).booleanValue(), (java.lang.String) obj6, ((java.lang.Boolean) obj7).booleanValue(), ((java.lang.Boolean) obj8).booleanValue(), ((java.lang.Integer) obj9).intValue(), ((java.lang.Integer) obj10).intValue(), ((java.lang.Integer) obj11).intValue());
                    }
                });
            }
            if (this.mCheckOpsDelegate != null) {
                return startDelegateProxyOperationImpl(clientId, code, attributionSource, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId);
            }
            return com.android.server.appop.AppOpsService.this.startProxyOperationImpl(clientId, code, attributionSource, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.app.SyncNotedAppOp startDelegateProxyOperationImpl(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlsgs, int attributionChainId) {
            return this.mCheckOpsDelegate.startProxyOperation(clientId, code, attributionSource, startIfModeDefault, shouldCollectAsyncNotedOp, message, shouldCollectMessage, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlsgs, attributionChainId, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda7(com.android.server.appop.AppOpsService.this));
        }

        public void finishOperation(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId) throws java.lang.Throwable {
            if (this.mPolicy != null) {
                if (this.mCheckOpsDelegate != null) {
                    this.mPolicy.finishOperation(clientId, code, uid, packageName, attributionTag, virtualDeviceId, new com.android.internal.util.function.HexConsumer() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda13
                        public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                            this.f$0.finishDelegateOperationImpl((android.os.IBinder) obj, ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), (java.lang.String) obj4, (java.lang.String) obj5, ((java.lang.Integer) obj6).intValue());
                        }
                    });
                    return;
                } else {
                    this.mPolicy.finishOperation(clientId, code, uid, packageName, attributionTag, virtualDeviceId, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda4(com.android.server.appop.AppOpsService.this));
                    return;
                }
            }
            if (this.mCheckOpsDelegate != null) {
                finishDelegateOperationImpl(clientId, code, uid, packageName, attributionTag, virtualDeviceId);
            } else {
                com.android.server.appop.AppOpsService.this.finishOperationImpl(clientId, code, uid, packageName, attributionTag, virtualDeviceId);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finishDelegateOperationImpl(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId) {
            this.mCheckOpsDelegate.finishOperation(clientId, code, uid, packageName, attributionTag, virtualDeviceId, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda4(com.android.server.appop.AppOpsService.this));
        }

        public void finishProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean skipProxyOperation) throws java.lang.Throwable {
            if (this.mPolicy != null) {
                if (this.mCheckOpsDelegate == null) {
                    this.mPolicy.finishProxyOperation(clientId, code, attributionSource, skipProxyOperation, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda3(com.android.server.appop.AppOpsService.this));
                    return;
                } else {
                    this.mPolicy.finishProxyOperation(clientId, code, attributionSource, skipProxyOperation, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda15
                        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                            return this.f$0.finishDelegateProxyOperationImpl((android.os.IBinder) obj, ((java.lang.Integer) obj2).intValue(), (android.content.AttributionSource) obj3, ((java.lang.Boolean) obj4).booleanValue());
                        }
                    });
                    return;
                }
            }
            if (this.mCheckOpsDelegate != null) {
                finishDelegateProxyOperationImpl(clientId, code, attributionSource, skipProxyOperation);
            } else {
                com.android.server.appop.AppOpsService.this.finishProxyOperationImpl(clientId, code, attributionSource, skipProxyOperation);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.Void finishDelegateProxyOperationImpl(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean skipProxyOperation) {
            this.mCheckOpsDelegate.finishProxyOperation(clientId, code, attributionSource, skipProxyOperation, new com.android.server.appop.AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda3(com.android.server.appop.AppOpsService.this));
            return null;
        }
    }
}
