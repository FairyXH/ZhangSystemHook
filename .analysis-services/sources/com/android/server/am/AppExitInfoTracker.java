package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class AppExitInfoTracker {
    static final java.lang.String APP_EXIT_INFO_FILE = "procexitinfo";
    private static final long APP_EXIT_INFO_PERSIST_INTERVAL = java.util.concurrent.TimeUnit.MINUTES.toMillis(30);
    private static final long APP_EXIT_INFO_STATSD_LOG_DEBOUNCE = java.util.concurrent.TimeUnit.SECONDS.toMillis(15);
    private static final int APP_EXIT_RAW_INFO_POOL_SIZE = 8;
    static final java.lang.String APP_EXIT_STORE_DIR = "procexitstore";
    private static final java.lang.String APP_TRACE_FILE_SUFFIX = ".gz";
    private static final int FOREACH_ACTION_NONE = 0;
    private static final int FOREACH_ACTION_REMOVE_ITEM = 1;
    private static final int FOREACH_ACTION_STOP_ITERATION = 2;
    private static final java.lang.String TAG = "ActivityManager";
    private int mAppExitInfoHistoryListSize;
    private com.android.server.am.AppExitInfoTracker.KillHandler mKillHandler;
    java.io.File mProcExitInfoFile;
    java.io.File mProcExitStoreDir;
    private com.android.server.am.ActivityManagerService mService;
    private final java.lang.Object mLock = new java.lang.Object();
    private java.lang.Runnable mAppExitInfoPersistTask = null;
    private long mLastAppExitInfoPersistTimestamp = 0;
    java.util.concurrent.atomic.AtomicBoolean mAppExitInfoLoaded = new java.util.concurrent.atomic.AtomicBoolean();
    final java.util.ArrayList<android.app.ApplicationExitInfo> mTmpInfoList = new java.util.ArrayList<>();
    final java.util.ArrayList<android.app.ApplicationExitInfo> mTmpInfoList2 = new java.util.ArrayList<>();
    final com.android.server.am.AppExitInfoTracker.IsolatedUidRecords mIsolatedUidRecords = new com.android.server.am.AppExitInfoTracker.IsolatedUidRecords();
    final com.android.server.am.AppExitInfoTracker.AppExitInfoExternalSource mAppExitInfoSourceZygote = new com.android.server.am.AppExitInfoTracker.AppExitInfoExternalSource("zygote", null);
    final com.android.server.am.AppExitInfoTracker.AppExitInfoExternalSource mAppExitInfoSourceLmkd = new com.android.server.am.AppExitInfoTracker.AppExitInfoExternalSource("lmkd", 3);
    final android.util.SparseArray<android.util.SparseArray<byte[]>> mActiveAppStateSummary = new android.util.SparseArray<>();
    final android.util.SparseArray<android.util.SparseArray<java.io.File>> mActiveAppTraces = new android.util.SparseArray<>();
    final com.android.server.am.AppExitInfoTracker.AppTraceRetriever mAppTraceRetriever = new com.android.server.am.AppExitInfoTracker.AppTraceRetriever();
    public com.android.server.am.IAppExitInfoTrackerExt mAppExitInfoTrackerExt = (com.android.server.am.IAppExitInfoTrackerExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IAppExitInfoTrackerExt.class).create();
    private final com.android.internal.app.ProcessMap<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer> mData = new com.android.internal.app.ProcessMap<>();
    private final android.util.Pools.SynchronizedPool<android.app.ApplicationExitInfo> mRawRecordsPool = new android.util.Pools.SynchronizedPool<>(8);

    interface LmkdKillListener {
        void onLmkdKillOccurred(int i, int i2);
    }

    AppExitInfoTracker() {
    }

    void init(com.android.server.am.ActivityManagerService service) {
        this.mService = service;
        com.android.server.ServiceThread thread = new com.android.server.ServiceThread("ActivityManager:killHandler", 10, true);
        thread.start();
        this.mKillHandler = new com.android.server.am.AppExitInfoTracker.KillHandler(thread.getLooper());
        this.mAppExitInfoTrackerExt.setThreadSchedPolicy(thread.getThreadId(), "ActivityManager:killHandler", 14);
        this.mProcExitStoreDir = new java.io.File(com.android.server.SystemServiceManager.ensureSystemDir(), APP_EXIT_STORE_DIR);
        if (!android.os.FileUtils.createDir(this.mProcExitStoreDir)) {
            android.util.Slog.e("ActivityManager", "Unable to create " + this.mProcExitStoreDir);
        } else {
            this.mProcExitInfoFile = new java.io.File(this.mProcExitStoreDir, APP_EXIT_INFO_FILE);
            this.mAppExitInfoHistoryListSize = service.mContext.getResources().getInteger(android.R.integer.config_activityDefaultDur);
        }
    }

    void onSystemReady() {
        registerForUserRemoval();
        registerForPackageRemoval();
        com.android.server.IoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSystemReady$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0() {
        android.os.SystemProperties.set("persist.sys.lmk.reportkills", java.lang.Boolean.toString(android.os.SystemProperties.getBoolean("sys.lmk.reportkills", false)));
        loadExistingProcessExitInfo();
    }

    void scheduleNoteProcessDied(com.android.server.am.ProcessRecord app) {
        if (app == null || app.info == null || !this.mAppExitInfoLoaded.get()) {
            return;
        }
        android.app.ApplicationExitInfo raw = obtainRawRecord(app, java.lang.System.currentTimeMillis());
        this.mAppExitInfoTrackerExt.notifyOplusExitInfo(raw, app);
        raw.setDescription(this.mAppExitInfoTrackerExt.updateExitInfoMsg(raw.getDescription(), app));
        this.mAppExitInfoTrackerExt.removeProcessInfo(app);
        this.mKillHandler.obtainMessage(4103, raw).sendToTarget();
    }

    void scheduleNoteAppKill(final com.android.server.am.ProcessRecord app, int reason, int subReason, java.lang.String msg) {
        if (!this.mAppExitInfoLoaded.get() || app == null || app.info == null) {
            return;
        }
        final android.app.ApplicationExitInfo raw = obtainRawRecord(app, java.lang.System.currentTimeMillis());
        raw.setReason(reason);
        raw.setSubReason(subReason);
        raw.setDescription(this.mAppExitInfoTrackerExt.updateExitInfoMsg(msg, app));
        this.mService.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleNoteAppKill$1(raw, app);
            }
        });
        this.mKillHandler.obtainMessage(4104, raw).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleNoteAppKill$1(android.app.ApplicationExitInfo raw, com.android.server.am.ProcessRecord app) {
        this.mAppExitInfoTrackerExt.notifyOplusExitInfo(raw, app);
    }

    void scheduleNoteAppRecoverableCrash(com.android.server.am.ProcessRecord app) {
        if (!this.mAppExitInfoLoaded.get() || app == null || app.info == null) {
            return;
        }
        android.app.ApplicationExitInfo raw = obtainRawRecord(app, java.lang.System.currentTimeMillis());
        raw.setReason(5);
        raw.setSubReason(0);
        raw.setDescription("recoverable_crash");
        this.mKillHandler.obtainMessage(4106, raw).sendToTarget();
    }

    void scheduleNoteAppKill(int pid, int uid, int reason, int subReason, java.lang.String msg) {
        com.android.server.am.ProcessRecord app;
        if (!this.mAppExitInfoLoaded.get()) {
            return;
        }
        synchronized (this.mService.mPidsSelfLocked) {
            app = this.mService.mPidsSelfLocked.get(pid);
        }
        if (app == null) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.w("ActivityManager", "Skipping saving the kill reason for pid " + pid + "(uid=" + uid + ") since its process record is not found");
                return;
            }
            return;
        }
        scheduleNoteAppKill(app, reason, subReason, msg);
    }

    void setLmkdKillListener(final com.android.server.am.AppExitInfoTracker.LmkdKillListener listener) {
        synchronized (this.mLock) {
            this.mAppExitInfoSourceLmkd.setOnProcDiedListener(new java.util.function.BiConsumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    listener.onLmkdKillOccurred(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue());
                }
            });
        }
    }

    void scheduleNoteLmkdProcKilled(int pid, int uid) {
        this.mKillHandler.obtainMessage(4101, pid, uid).sendToTarget();
    }

    private void scheduleChildProcDied(int pid, int uid, int status) {
        this.mKillHandler.obtainMessage(4102, pid, uid, java.lang.Integer.valueOf(status)).sendToTarget();
    }

    void handleZygoteSigChld(int pid, int uid, int status) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.i("ActivityManager", "Got SIGCHLD from zygote: pid=" + pid + ", uid=" + uid + ", status=" + java.lang.Integer.toHexString(status));
        }
        scheduleChildProcDied(pid, uid, status);
    }

    void handleNoteProcessDiedLocked(android.app.ApplicationExitInfo raw) {
        if (raw != null) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.i("ActivityManager", "Update process exit info for " + raw.getPackageName() + "(" + raw.getPid() + "/u" + raw.getRealUid() + ")");
            }
            android.app.ApplicationExitInfo info = getExitInfoLocked(raw.getPackageName(), raw.getPackageUid(), raw.getPid());
            android.util.Pair<java.lang.Long, java.lang.Object> zygote = this.mAppExitInfoSourceZygote.remove(raw.getPid(), raw.getRealUid());
            android.util.Pair<java.lang.Long, java.lang.Object> lmkd = this.mAppExitInfoSourceLmkd.remove(raw.getPid(), raw.getRealUid());
            android.util.Pair<java.lang.String, android.util.Pair<java.lang.Integer, java.lang.Integer>> athena = this.mAppExitInfoTrackerExt.removeAthenaKillRecord(raw.getPid(), raw.getRealUid());
            this.mIsolatedUidRecords.removeIsolatedUidLocked(raw.getRealUid());
            if (info == null) {
                info = addExitInfoLocked(raw);
            }
            if (lmkd != null) {
                updateExistingExitInfoRecordLocked(info, null, 3);
            } else if (athena != null) {
                this.mAppExitInfoTrackerExt.updateApplicationExitInfo(info, ((java.lang.Integer) ((android.util.Pair) athena.second).first).intValue(), ((java.lang.Integer) ((android.util.Pair) athena.second).second).intValue(), (java.lang.String) athena.first);
                scheduleLogToStatsdLocked(info, true);
            } else {
                if (this.mAppExitInfoTrackerExt.updateKillReasonInfo(info, zygote != null ? (java.lang.Integer) zygote.second : null)) {
                    scheduleLogToStatsdLocked(info, true);
                } else if (zygote != null) {
                    updateExistingExitInfoRecordLocked(info, (java.lang.Integer) zygote.second, null);
                } else {
                    scheduleLogToStatsdLocked(info, false);
                }
            }
            this.mAppExitInfoTrackerExt.updateOplusExitInfo(info);
            this.mAppExitInfoTrackerExt.notifyAppExitInfo(info);
        }
    }

    void handleNoteAppKillLocked(android.app.ApplicationExitInfo raw) {
        android.app.ApplicationExitInfo info = getExitInfoLocked(raw.getPackageName(), raw.getPackageUid(), raw.getPid());
        if (info == null) {
            info = addExitInfoLocked(raw);
        } else {
            info.setReason(raw.getReason());
            info.setSubReason(raw.getSubReason());
            info.setStatus(0);
            info.setTimestamp(java.lang.System.currentTimeMillis());
            info.setDescription(raw.getDescription());
        }
        scheduleLogToStatsdLocked(info, true);
    }

    void handleNoteAppRecoverableCrashLocked(android.app.ApplicationExitInfo raw) {
        addExitInfoLocked(raw, true);
    }

    private android.app.ApplicationExitInfo addExitInfoLocked(android.app.ApplicationExitInfo raw) {
        return addExitInfoLocked(raw, false);
    }

    private android.app.ApplicationExitInfo addExitInfoLocked(android.app.ApplicationExitInfo raw, boolean recoverable) {
        java.lang.Integer k;
        if (!this.mAppExitInfoLoaded.get()) {
            android.util.Slog.w("ActivityManager", "Skipping saving the exit info due to ongoing loading from storage");
            return null;
        }
        android.app.ApplicationExitInfo info = new android.app.ApplicationExitInfo(raw);
        java.lang.String[] packages = raw.getPackageList();
        int uid = raw.getRealUid();
        if (packages != null) {
            if (android.os.UserHandle.isIsolated(uid) && (k = this.mIsolatedUidRecords.getUidByIsolatedUid(uid)) != null) {
                uid = k.intValue();
            }
            for (java.lang.String str : packages) {
                addExitInfoInnerLocked(str, uid, info, recoverable);
            }
            if (android.os.Process.isSdkSandboxUid(uid)) {
                for (java.lang.String str2 : packages) {
                    addExitInfoInnerLocked(str2, raw.getPackageUid(), info, recoverable);
                }
            }
        }
        schedulePersistProcessExitInfo(false);
        return info;
    }

    private void updateExistingExitInfoRecordLocked(android.app.ApplicationExitInfo info, java.lang.Integer status, java.lang.Integer reason) {
        if (info == null || !isFresh(info.getTimestamp())) {
            return;
        }
        boolean immediateLog = false;
        if (status != null) {
            if (android.system.OsConstants.WIFEXITED(status.intValue())) {
                info.setReason(1);
                info.setStatus(android.system.OsConstants.WEXITSTATUS(status.intValue()));
                immediateLog = true;
            } else if (android.system.OsConstants.WIFSIGNALED(status.intValue())) {
                if (info.getReason() == 0) {
                    info.setReason(2);
                    info.setStatus(android.system.OsConstants.WTERMSIG(status.intValue()));
                } else if (info.getReason() == 5) {
                    info.setStatus(android.system.OsConstants.WTERMSIG(status.intValue()));
                    immediateLog = true;
                }
            }
        }
        if (reason != null) {
            info.setReason(reason.intValue());
            if (reason.intValue() == 3) {
                immediateLog = true;
            }
        }
        scheduleLogToStatsdLocked(info, immediateLog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateExitInfoIfNecessaryLocked(final int pid, int uid, final java.lang.Integer status, final java.lang.Integer reason) {
        java.lang.Integer k = this.mIsolatedUidRecords.getUidByIsolatedUid(uid);
        if (k != null) {
            uid = k.intValue();
        }
        final java.util.ArrayList<android.app.ApplicationExitInfo> tlist = this.mTmpInfoList;
        tlist.clear();
        final int targetUid = uid;
        forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda19
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return this.f$0.lambda$updateExitInfoIfNecessaryLocked$3(targetUid, tlist, pid, status, reason, (java.lang.String) obj, (android.util.SparseArray) obj2);
            }
        });
        return tlist.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$updateExitInfoIfNecessaryLocked$3(int targetUid, java.util.ArrayList tlist, int pid, java.lang.Integer status, java.lang.Integer reason, java.lang.String packageName, android.util.SparseArray records) {
        com.android.server.am.AppExitInfoTracker.AppExitInfoContainer container = (com.android.server.am.AppExitInfoTracker.AppExitInfoContainer) records.get(targetUid);
        if (container == null) {
            return 0;
        }
        tlist.clear();
        container.getExitInfoLocked(pid, 1, tlist);
        if (tlist.size() == 0) {
            return 0;
        }
        android.app.ApplicationExitInfo info = (android.app.ApplicationExitInfo) tlist.get(0);
        if (info.getRealUid() != targetUid) {
            tlist.clear();
            return 0;
        }
        updateExistingExitInfoRecordLocked(info, status, reason);
        this.mAppExitInfoTrackerExt.updateOplusExitInfo(info);
        return 2;
    }

    void getExitInfo(java.lang.String packageName, final int filterUid, final int filterPid, int maxNum, java.util.ArrayList<android.app.ApplicationExitInfo> results) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                boolean emptyPackageName = android.text.TextUtils.isEmpty(packageName);
                if (!emptyPackageName) {
                    com.android.server.am.AppExitInfoTracker.AppExitInfoContainer container = (com.android.server.am.AppExitInfoTracker.AppExitInfoContainer) this.mData.get(packageName, filterUid);
                    if (container != null) {
                        container.getExitInfoLocked(filterPid, maxNum, results);
                    }
                } else {
                    final java.util.ArrayList<android.app.ApplicationExitInfo> list = this.mTmpInfoList2;
                    list.clear();
                    forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda2
                        @Override // java.util.function.BiFunction
                        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                            return this.f$0.lambda$getExitInfo$4(filterUid, list, filterPid, (java.lang.String) obj, (android.util.SparseArray) obj2);
                        }
                    });
                    java.util.Collections.sort(list, new java.util.Comparator() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda3
                        @Override // java.util.Comparator
                        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                            return java.lang.Long.compare(((android.app.ApplicationExitInfo) obj2).getTimestamp(), ((android.app.ApplicationExitInfo) obj).getTimestamp());
                        }
                    });
                    int size = list.size();
                    if (maxNum > 0) {
                        size = java.lang.Math.min(size, maxNum);
                    }
                    for (int i = 0; i < size; i++) {
                        results.add(list.get(i));
                    }
                    list.clear();
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getExitInfo$4(int filterUid, java.util.ArrayList list, int filterPid, java.lang.String name, android.util.SparseArray records) {
        com.android.server.am.AppExitInfoTracker.AppExitInfoContainer container = (com.android.server.am.AppExitInfoTracker.AppExitInfoContainer) records.get(filterUid);
        if (container != null) {
            this.mTmpInfoList.clear();
            list.addAll(container.toListLocked(this.mTmpInfoList, filterPid));
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.ApplicationExitInfo getExitInfoLocked(java.lang.String packageName, int filterUid, int filterPid) {
        java.util.ArrayList<android.app.ApplicationExitInfo> list = this.mTmpInfoList;
        list.clear();
        getExitInfo(packageName, filterUid, filterPid, 1, list);
        android.app.ApplicationExitInfo info = list.size() > 0 ? list.get(0) : null;
        list.clear();
        return info;
    }

    void onUserRemoved(int userId) {
        this.mAppExitInfoSourceZygote.removeByUserId(userId);
        this.mAppExitInfoSourceLmkd.removeByUserId(userId);
        this.mIsolatedUidRecords.removeByUserId(userId);
        synchronized (this.mLock) {
            this.mAppExitInfoTrackerExt.removeByUserId(userId);
            removeByUserIdLocked(userId);
            schedulePersistProcessExitInfo(true);
        }
    }

    void onPackageRemoved(java.lang.String packageName, int uid, boolean allUsers) {
        if (packageName != null) {
            boolean removeUid = android.text.TextUtils.isEmpty(this.mService.mPackageManagerInt.getNameForUid(uid));
            synchronized (this.mLock) {
                if (removeUid) {
                    try {
                        this.mAppExitInfoSourceZygote.removeByUidLocked(uid, allUsers);
                        this.mAppExitInfoSourceLmkd.removeByUidLocked(uid, allUsers);
                        this.mAppExitInfoTrackerExt.removeByUid(uid, android.os.UserHandle.isIsolated(uid) ? this.mIsolatedUidRecords.getUidByIsolatedUid(uid) : null, allUsers);
                        this.mIsolatedUidRecords.removeAppUid(uid, allUsers);
                    } finally {
                    }
                }
                removePackageLocked(packageName, uid, removeUid, allUsers ? -1 : android.os.UserHandle.getUserId(uid));
                schedulePersistProcessExitInfo(true);
            }
        }
    }

    private void registerForUserRemoval() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_REMOVED");
        this.mService.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.am.AppExitInfoTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (userId < 1) {
                    return;
                }
                com.android.server.am.AppExitInfoTracker.this.onUserRemoved(userId);
            }
        }, filter, null, this.mKillHandler);
    }

    private void registerForPackageRemoval() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        this.mService.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.am.AppExitInfoTracker.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                boolean replacing = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                if (replacing) {
                    return;
                }
                int uid = intent.getIntExtra("android.intent.extra.UID", -10000);
                boolean allUsers = intent.getBooleanExtra("android.intent.extra.REMOVED_FOR_ALL_USERS", false);
                com.android.server.am.AppExitInfoTracker.this.onPackageRemoved(intent.getData().getSchemeSpecificPart(), uid, allUsers);
            }
        }, filter, null, this.mKillHandler);
    }

    void loadExistingProcessExitInfo() {
        if (!this.mProcExitInfoFile.canRead()) {
            this.mAppExitInfoLoaded.set(true);
            return;
        }
        java.io.FileInputStream fin = null;
        try {
            try {
                android.util.AtomicFile af = new android.util.AtomicFile(this.mProcExitInfoFile);
                fin = af.openRead();
                android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(fin);
                for (int next = proto.nextField(); next != -1; next = proto.nextField()) {
                    switch (next) {
                        case 1:
                            synchronized (this.mLock) {
                                this.mLastAppExitInfoPersistTimestamp = proto.readLong(1112396529665L);
                                break;
                            }
                            break;
                        case 2:
                            loadPackagesFromProto(proto, next);
                            break;
                        default:
                            break;
                    }
                }
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (java.io.IOException e) {
                    }
                }
            } catch (java.lang.Throwable th) {
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (java.io.IOException e2) {
                    }
                }
                throw th;
            }
        } catch (java.lang.Exception e3) {
            android.util.Slog.w("ActivityManager", "Error in loading historical app exit info from persistent storage: " + e3);
            if (fin != null) {
                try {
                    fin.close();
                } catch (java.io.IOException e4) {
                }
            }
        }
        synchronized (this.mLock) {
            pruneAnrTracesIfNecessaryLocked();
            this.mAppExitInfoLoaded.set(true);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.util.proto.WireTypeMismatchException */
    private void loadPackagesFromProto(android.util.proto.ProtoInputStream proto, long fieldId) throws android.util.proto.WireTypeMismatchException, java.io.IOException {
        long token = proto.start(fieldId);
        java.lang.String pkgName = "";
        int next = proto.nextField();
        while (next != -1) {
            switch (next) {
                case 1:
                    pkgName = proto.readString(1138166333441L);
                    break;
                case 2:
                    com.android.server.am.AppExitInfoTracker.AppExitInfoContainer container = new com.android.server.am.AppExitInfoTracker.AppExitInfoContainer(this.mAppExitInfoHistoryListSize);
                    int uid = container.readFromProto(proto, 2246267895810L);
                    synchronized (this.mLock) {
                        this.mData.put(pkgName, uid, container);
                        break;
                    }
                    break;
            }
            next = proto.nextField();
        }
        proto.end(token);
    }

    void persistProcessExitInfo() {
        android.util.AtomicFile af = new android.util.AtomicFile(this.mProcExitInfoFile);
        java.io.FileOutputStream out = null;
        long now = java.lang.System.currentTimeMillis();
        try {
            out = af.startWrite();
            final android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
            proto.write(1112396529665L, now);
            synchronized (this.mLock) {
                forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda13
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                        return com.android.server.am.AppExitInfoTracker.lambda$persistProcessExitInfo$6(proto, (java.lang.String) obj, (android.util.SparseArray) obj2);
                    }
                });
                this.mLastAppExitInfoPersistTimestamp = now;
            }
            proto.flush();
            af.finishWrite(out);
        } catch (java.io.IOException e) {
            android.util.Slog.w("ActivityManager", "Unable to write historical app exit info into persistent storage: " + e);
            af.failWrite(out);
        }
        synchronized (this.mLock) {
            this.mAppExitInfoPersistTask = null;
        }
    }

    static /* synthetic */ java.lang.Integer lambda$persistProcessExitInfo$6(android.util.proto.ProtoOutputStream proto, java.lang.String packageName, android.util.SparseArray records) {
        long token = proto.start(2246267895810L);
        proto.write(1138166333441L, packageName);
        int uidArraySize = records.size();
        for (int j = 0; j < uidArraySize; j++) {
            ((com.android.server.am.AppExitInfoTracker.AppExitInfoContainer) records.valueAt(j)).writeToProto(proto, 2246267895810L);
        }
        proto.end(token);
        return 0;
    }

    void schedulePersistProcessExitInfo(boolean immediately) {
        synchronized (this.mLock) {
            if (this.mAppExitInfoPersistTask == null || immediately) {
                if (this.mAppExitInfoPersistTask != null) {
                    com.android.server.IoThread.getHandler().removeCallbacks(this.mAppExitInfoPersistTask);
                }
                this.mAppExitInfoPersistTask = new java.lang.Runnable() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.persistProcessExitInfo();
                    }
                };
                com.android.server.IoThread.getHandler().postDelayed(this.mAppExitInfoPersistTask, immediately ? 0L : APP_EXIT_INFO_PERSIST_INTERVAL);
            }
        }
    }

    void clearProcessExitInfo(boolean removeFile) {
        synchronized (this.mLock) {
            if (this.mAppExitInfoPersistTask != null) {
                com.android.server.IoThread.getHandler().removeCallbacks(this.mAppExitInfoPersistTask);
                this.mAppExitInfoPersistTask = null;
            }
            if (removeFile && this.mProcExitInfoFile != null) {
                this.mProcExitInfoFile.delete();
            }
            this.mData.getMap().clear();
            this.mActiveAppStateSummary.clear();
            this.mActiveAppTraces.clear();
            pruneAnrTracesIfNecessaryLocked();
        }
    }

    void clearHistoryProcessExitInfo(java.lang.String packageName, int userId) {
        com.android.server.os.NativeTombstoneManager tombstoneService = (com.android.server.os.NativeTombstoneManager) com.android.server.LocalServices.getService(com.android.server.os.NativeTombstoneManager.class);
        java.util.Optional<java.lang.Integer> appId = java.util.Optional.empty();
        if (android.text.TextUtils.isEmpty(packageName)) {
            synchronized (this.mLock) {
                removeByUserIdLocked(userId);
            }
        } else {
            int uid = this.mService.mPackageManagerInt.getPackageUid(packageName, 131072L, userId);
            appId = java.util.Optional.of(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid)));
            synchronized (this.mLock) {
                removePackageLocked(packageName, uid, true, userId);
            }
        }
        tombstoneService.purge(java.util.Optional.of(java.lang.Integer.valueOf(userId)), appId);
        schedulePersistProcessExitInfo(true);
    }

    void dumpHistoryProcessExitInfo(final java.io.PrintWriter pw, java.lang.String packageName) {
        pw.println("ACTIVITY MANAGER PROCESS EXIT INFO (dumpsys activity exit-info)");
        final android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        synchronized (this.mLock) {
            pw.println("Last Timestamp of Persistence Into Persistent Storage: " + sdf.format(new java.util.Date(this.mLastAppExitInfoPersistTimestamp)));
            if (android.text.TextUtils.isEmpty(packageName)) {
                forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda17
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                        return this.f$0.lambda$dumpHistoryProcessExitInfo$7(pw, sdf, (java.lang.String) obj, (android.util.SparseArray) obj2);
                    }
                });
            } else {
                android.util.SparseArray<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer> array = (android.util.SparseArray) this.mData.getMap().get(packageName);
                if (array != null) {
                    dumpHistoryProcessExitInfoLocked(pw, "  ", packageName, array, sdf);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$dumpHistoryProcessExitInfo$7(java.io.PrintWriter pw, android.icu.text.SimpleDateFormat sdf, java.lang.String name, android.util.SparseArray records) {
        dumpHistoryProcessExitInfoLocked(pw, "  ", name, records, sdf);
        return 0;
    }

    private void dumpHistoryProcessExitInfoLocked(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String packageName, android.util.SparseArray<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer> array, android.icu.text.SimpleDateFormat sdf) {
        pw.println(prefix + "package: " + packageName);
        int size = array.size();
        for (int i = 0; i < size; i++) {
            pw.println(prefix + "  Historical Process Exit for uid=" + array.keyAt(i));
            array.valueAt(i).dumpLocked(pw, prefix + "    ", sdf);
        }
    }

    private void addExitInfoInnerLocked(java.lang.String packageName, int uid, android.app.ApplicationExitInfo info, boolean recoverable) {
        com.android.server.am.AppExitInfoTracker.AppExitInfoContainer container = (com.android.server.am.AppExitInfoTracker.AppExitInfoContainer) this.mData.get(packageName, uid);
        if (container == null) {
            container = new com.android.server.am.AppExitInfoTracker.AppExitInfoContainer(this.mAppExitInfoHistoryListSize);
            if (android.os.UserHandle.isIsolated(info.getRealUid())) {
                java.lang.Integer k = this.mIsolatedUidRecords.getUidByIsolatedUid(info.getRealUid());
                if (k != null) {
                    container.mUid = k.intValue();
                }
            } else {
                container.mUid = info.getRealUid();
            }
            this.mData.put(packageName, uid, container);
        }
        if (recoverable) {
            container.addRecoverableCrashLocked(info);
        } else {
            container.addExitInfoLocked(info);
        }
    }

    private void scheduleLogToStatsdLocked(android.app.ApplicationExitInfo info, boolean immediate) {
        if (info.isLoggedInStatsd()) {
            return;
        }
        if (immediate) {
            this.mKillHandler.removeMessages(4105, info);
            performLogToStatsdLocked(info);
        } else if (!this.mKillHandler.hasMessages(4105, info)) {
            this.mKillHandler.sendMessageDelayed(this.mKillHandler.obtainMessage(4105, info), APP_EXIT_INFO_STATSD_LOG_DEBOUNCE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performLogToStatsdLocked(android.app.ApplicationExitInfo info) {
        if (info.isLoggedInStatsd()) {
            return;
        }
        info.setLoggedInStatsd(true);
        java.lang.String pkgName = info.getPackageName();
        java.lang.String processName = info.getProcessName();
        if (android.text.TextUtils.equals(pkgName, processName)) {
            processName = null;
        } else if (processName != null && pkgName != null && processName.startsWith(pkgName)) {
            processName = processName.substring(pkgName.length());
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_PROCESS_DIED, info.getPackageUid(), processName, info.getReason(), info.getSubReason(), info.getImportance(), (int) info.getPss(), (int) info.getRss(), info.hasForegroundServices());
    }

    private void forEachPackageLocked(java.util.function.BiFunction<java.lang.String, android.util.SparseArray<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer>, java.lang.Integer> callback) {
        if (callback != null) {
            android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer>> map = this.mData.getMap();
            int i = map.size() - 1;
            while (i >= 0) {
                switch (callback.apply(map.keyAt(i), map.valueAt(i)).intValue()) {
                    case 1:
                        android.util.SparseArray<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer> records = map.valueAt(i);
                        for (int j = records.size() - 1; j >= 0; j--) {
                            records.valueAt(j).destroyLocked();
                        }
                        map.removeAt(i);
                        break;
                    case 2:
                        i = 0;
                        break;
                }
                i--;
            }
        }
    }

    private void removePackageLocked(java.lang.String packageName, int uid, boolean removeUid, int userId) {
        if (removeUid) {
            this.mActiveAppStateSummary.remove(uid);
            int idx = this.mActiveAppTraces.indexOfKey(uid);
            if (idx >= 0) {
                android.util.SparseArray<java.io.File> array = this.mActiveAppTraces.valueAt(idx);
                for (int i = array.size() - 1; i >= 0; i--) {
                    array.valueAt(i).delete();
                }
                this.mActiveAppTraces.removeAt(idx);
            }
        }
        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer>> map = this.mData.getMap();
        android.util.SparseArray<com.android.server.am.AppExitInfoTracker.AppExitInfoContainer> array2 = map.get(packageName);
        if (array2 == null) {
            return;
        }
        if (userId == -1) {
            for (int i2 = array2.size() - 1; i2 >= 0; i2--) {
                array2.valueAt(i2).destroyLocked();
            }
            this.mData.getMap().remove(packageName);
            return;
        }
        int i3 = array2.size() - 1;
        while (true) {
            if (i3 < 0) {
                break;
            }
            if (android.os.UserHandle.getUserId(array2.keyAt(i3)) == userId) {
                array2.valueAt(i3).destroyLocked();
                array2.removeAt(i3);
                break;
            }
            i3--;
        }
        int i4 = array2.size();
        if (i4 == 0) {
            map.remove(packageName);
        }
    }

    private void removeByUserIdLocked(final int userId) {
        if (userId == -1) {
            this.mData.getMap().clear();
            this.mActiveAppStateSummary.clear();
            this.mActiveAppTraces.clear();
            pruneAnrTracesIfNecessaryLocked();
            return;
        }
        removeFromSparse2dArray(this.mActiveAppStateSummary, new java.util.function.Predicate() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.am.AppExitInfoTracker.lambda$removeByUserIdLocked$8(userId, (java.lang.Integer) obj);
            }
        }, null, null);
        removeFromSparse2dArray(this.mActiveAppTraces, new java.util.function.Predicate() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.am.AppExitInfoTracker.lambda$removeByUserIdLocked$9(userId, (java.lang.Integer) obj);
            }
        }, null, new java.util.function.Consumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((java.io.File) obj).delete();
            }
        });
        forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda8
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.am.AppExitInfoTracker.lambda$removeByUserIdLocked$11(userId, (java.lang.String) obj, (android.util.SparseArray) obj2);
            }
        });
    }

    static /* synthetic */ boolean lambda$removeByUserIdLocked$8(int userId, java.lang.Integer v) {
        return android.os.UserHandle.getUserId(v.intValue()) == userId;
    }

    static /* synthetic */ boolean lambda$removeByUserIdLocked$9(int userId, java.lang.Integer v) {
        return android.os.UserHandle.getUserId(v.intValue()) == userId;
    }

    static /* synthetic */ java.lang.Integer lambda$removeByUserIdLocked$11(int userId, java.lang.String packageName, android.util.SparseArray records) {
        int i = records.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            if (android.os.UserHandle.getUserId(records.keyAt(i)) != userId) {
                i--;
            } else {
                ((com.android.server.am.AppExitInfoTracker.AppExitInfoContainer) records.valueAt(i)).destroyLocked();
                records.removeAt(i);
                break;
            }
        }
        int i2 = records.size();
        return java.lang.Integer.valueOf(i2 != 0 ? 0 : 1);
    }

    android.app.ApplicationExitInfo obtainRawRecord(com.android.server.am.ProcessRecord app, long timestamp) {
        android.app.ApplicationExitInfo info = (android.app.ApplicationExitInfo) this.mRawRecordsPool.acquire();
        if (info == null) {
            info = new android.app.ApplicationExitInfo();
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                int definingUid = app.getHostingRecord() != null ? app.getHostingRecord().getDefiningUid() : 0;
                info.setPid(app.getPid());
                info.setRealUid(app.uid);
                info.setPackageUid(app.info.uid);
                info.setDefiningUid(definingUid > 0 ? definingUid : app.info.uid);
                info.setProcessName(app.processName);
                info.setConnectionGroup(app.mServices.getConnectionGroup());
                info.setPackageName(app.info.packageName);
                info.setPackageList(app.getPackageList());
                info.setReason(0);
                info.setSubReason(0);
                info.setStatus(0);
                info.setImportance(android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(app.mState.getReportedProcState()));
                info.setPss(app.mProfile.getLastPss());
                info.setRss(app.mProfile.getLastRss());
                info.setTimestamp(timestamp);
                info.setHasForegroundServices(app.mServices.hasReportedForegroundServices());
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        return info;
    }

    void recycleRawRecord(android.app.ApplicationExitInfo info) {
        info.setProcessName(null);
        info.setDescription(null);
        info.setPackageList(null);
        this.mRawRecordsPool.release(info);
    }

    void setProcessStateSummary(int uid, int pid, byte[] data) {
        synchronized (this.mLock) {
            java.lang.Integer k = this.mIsolatedUidRecords.getUidByIsolatedUid(uid);
            if (k != null) {
                uid = k.intValue();
            }
            putToSparse2dArray(this.mActiveAppStateSummary, uid, pid, data, new com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda11(), null);
        }
    }

    byte[] getProcessStateSummary(int uid, int pid) {
        synchronized (this.mLock) {
            java.lang.Integer k = this.mIsolatedUidRecords.getUidByIsolatedUid(uid);
            if (k != null) {
                uid = k.intValue();
            }
            int index = this.mActiveAppStateSummary.indexOfKey(uid);
            if (index < 0) {
                return null;
            }
            return this.mActiveAppStateSummary.valueAt(index).get(pid);
        }
    }

    public void scheduleLogAnrTrace(int pid, int uid, java.lang.String[] packageList, java.io.File traceFile, long startOff, long endOff) {
        this.mKillHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HexConsumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda12
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) throws java.lang.Throwable {
                this.f$0.handleLogAnrTrace(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), (java.lang.String[]) obj3, (java.io.File) obj4, ((java.lang.Long) obj5).longValue(), ((java.lang.Long) obj6).longValue());
            }
        }, java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid), packageList, traceFile, java.lang.Long.valueOf(startOff), java.lang.Long.valueOf(endOff)));
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00a5 A[Catch: all -> 0x00ed, TRY_ENTER, TryCatch #4 {all -> 0x00ed, blocks: (B:27:0x0072, B:31:0x00a5, B:33:0x00b1, B:53:0x00eb), top: B:71:0x0072 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00bf A[Catch: all -> 0x00e1, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x00e1, blocks: (B:25:0x006e, B:29:0x00a2, B:38:0x00bf), top: B:63:0x006e }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d7  */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:55:0x00ed
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void handleLogAnrTrace(int r20, int r21, java.lang.String[] r22, java.io.File r23, long r24, long r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 248
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppExitInfoTracker.handleLogAnrTrace(int, int, java.lang.String[], java.io.File, long, long):void");
    }

    private static boolean copyToGzFile(java.io.File inFile, java.io.File outFile, long start, long length) {
        long remaining = length;
        try {
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(new java.io.FileInputStream(inFile));
            try {
                java.util.zip.GZIPOutputStream out = new java.util.zip.GZIPOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile)));
                try {
                    byte[] buffer = new byte[8192];
                    in.skip(start);
                    while (remaining > 0) {
                        int t = in.read(buffer, 0, (int) java.lang.Math.min(buffer.length, remaining));
                        if (t < 0) {
                            break;
                        }
                        out.write(buffer, 0, t);
                        remaining -= (long) t;
                    }
                    out.close();
                    in.close();
                    return remaining == 0 && outFile.exists();
                } finally {
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.e("ActivityManager", "Error in copying ANR trace from " + inFile + " to " + outFile, e);
            }
            return false;
        }
    }

    private void pruneAnrTracesIfNecessaryLocked() {
        final android.util.ArraySet<java.lang.String> allFiles = new android.util.ArraySet<>();
        java.io.File[] files = this.mProcExitStoreDir.listFiles(new java.io.FileFilter() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda14
            @Override // java.io.FileFilter
            public final boolean accept(java.io.File file) {
                return com.android.server.am.AppExitInfoTracker.lambda$pruneAnrTracesIfNecessaryLocked$13(allFiles, file);
            }
        });
        if (com.android.internal.util.ArrayUtils.isEmpty(files)) {
            return;
        }
        forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda15
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.am.AppExitInfoTracker.lambda$pruneAnrTracesIfNecessaryLocked$15(allFiles, (java.lang.String) obj, (android.util.SparseArray) obj2);
            }
        });
        forEachSparse2dArray(this.mActiveAppTraces, new java.util.function.Consumer() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                allFiles.remove(((java.io.File) obj).getName());
            }
        });
        for (int i = allFiles.size() - 1; i >= 0; i--) {
            new java.io.File(this.mProcExitStoreDir, allFiles.valueAt(i)).delete();
        }
    }

    static /* synthetic */ boolean lambda$pruneAnrTracesIfNecessaryLocked$13(android.util.ArraySet allFiles, java.io.File f) {
        java.lang.String name = f.getName();
        boolean trace = name.startsWith("anr_") && name.endsWith(".gz");
        if (trace) {
            allFiles.add(name);
        }
        return trace;
    }

    static /* synthetic */ java.lang.Integer lambda$pruneAnrTracesIfNecessaryLocked$15(final android.util.ArraySet allFiles, java.lang.String name, android.util.SparseArray records) {
        for (int i = records.size() - 1; i >= 0; i--) {
            com.android.server.am.AppExitInfoTracker.AppExitInfoContainer container = (com.android.server.am.AppExitInfoTracker.AppExitInfoContainer) records.valueAt(i);
            container.forEachRecordLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppExitInfoTracker$$ExternalSyntheticLambda9
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.am.AppExitInfoTracker.lambda$pruneAnrTracesIfNecessaryLocked$14(allFiles, (java.lang.Integer) obj, (android.app.ApplicationExitInfo) obj2);
                }
            });
        }
        return 0;
    }

    static /* synthetic */ java.lang.Integer lambda$pruneAnrTracesIfNecessaryLocked$14(android.util.ArraySet allFiles, java.lang.Integer pid, android.app.ApplicationExitInfo info) {
        java.io.File traceFile = info.getTraceFile();
        if (traceFile != null) {
            allFiles.remove(traceFile.getName());
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T extends android.util.SparseArray<U>, U> void putToSparse2dArray(android.util.SparseArray<T> array, int outerKey, int innerKey, U value, java.util.function.Supplier<T> newInstance, java.util.function.Consumer<U> consumer) {
        T innerArray;
        int idx = array.indexOfKey(outerKey);
        if (idx < 0) {
            T innerArray2 = newInstance.get();
            innerArray = innerArray2;
            array.put(outerKey, innerArray);
        } else {
            T innerArray3 = array.valueAt(idx);
            innerArray = innerArray3;
        }
        int idx2 = innerArray.indexOfKey(innerKey);
        if (idx2 >= 0) {
            if (consumer != 0) {
                consumer.accept(innerArray.valueAt(idx2));
            }
            innerArray.setValueAt(idx2, value);
            return;
        }
        innerArray.put(innerKey, value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T extends android.util.SparseArray<U>, U> void forEachSparse2dArray(android.util.SparseArray<T> array, java.util.function.Consumer<U> consumer) {
        if (consumer != 0) {
            for (int i = array.size() - 1; i >= 0; i--) {
                T innerArray = array.valueAt(i);
                if (innerArray != null) {
                    for (int j = innerArray.size() - 1; j >= 0; j--) {
                        consumer.accept(innerArray.valueAt(j));
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T extends android.util.SparseArray<U>, U> void removeFromSparse2dArray(android.util.SparseArray<T> array, java.util.function.Predicate<java.lang.Integer> outerPredicate, java.util.function.Predicate<java.lang.Integer> innerPredicate, java.util.function.Consumer<U> consumer) {
        T innerArray;
        for (int i = array.size() - 1; i >= 0; i--) {
            if ((outerPredicate == null || outerPredicate.test(java.lang.Integer.valueOf(array.keyAt(i)))) && (innerArray = array.valueAt(i)) != null) {
                for (int j = innerArray.size() - 1; j >= 0; j--) {
                    if (innerPredicate == null || innerPredicate.test(java.lang.Integer.valueOf(innerArray.keyAt(j)))) {
                        if (consumer != 0) {
                            consumer.accept(innerArray.valueAt(j));
                        }
                        innerArray.removeAt(j);
                    }
                }
                int j2 = innerArray.size();
                if (j2 == 0) {
                    array.removeAt(i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends android.util.SparseArray<U>, U> U findAndRemoveFromSparse2dArray(android.util.SparseArray<T> sparseArray, int i, int i2) {
        T tValueAt;
        int iIndexOfKey;
        int iIndexOfKey2 = sparseArray.indexOfKey(i);
        if (iIndexOfKey2 < 0 || (tValueAt = sparseArray.valueAt(iIndexOfKey2)) == null || (iIndexOfKey = tValueAt.indexOfKey(i2)) < 0) {
            return null;
        }
        U u = (U) tValueAt.valueAt(iIndexOfKey);
        tValueAt.removeAt(iIndexOfKey);
        if (tValueAt.size() == 0) {
            sparseArray.removeAt(iIndexOfKey2);
        }
        return u;
    }

    final class AppExitInfoContainer {
        private int mMaxCapacity;
        private int mUid;
        private android.util.SparseArray<android.app.ApplicationExitInfo> mInfos = new android.util.SparseArray<>();
        private android.util.SparseArray<android.app.ApplicationExitInfo> mRecoverableCrashes = new android.util.SparseArray<>();

        AppExitInfoContainer(int maxCapacity) {
            this.mMaxCapacity = maxCapacity;
        }

        void getInfosLocked(android.util.SparseArray<android.app.ApplicationExitInfo> map, int filterPid, int maxNum, java.util.ArrayList<android.app.ApplicationExitInfo> results) {
            if (filterPid > 0) {
                android.app.ApplicationExitInfo r = map.get(filterPid);
                if (r != null) {
                    results.add(r);
                    return;
                }
                return;
            }
            int numRep = map.size();
            if (maxNum <= 0 || numRep <= maxNum) {
                for (int i = 0; i < numRep; i++) {
                    results.add(map.valueAt(i));
                }
                java.util.Collections.sort(results, new java.util.Comparator() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoContainer$$ExternalSyntheticLambda1
                    @Override // java.util.Comparator
                    public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                        return java.lang.Long.compare(((android.app.ApplicationExitInfo) obj2).getTimestamp(), ((android.app.ApplicationExitInfo) obj).getTimestamp());
                    }
                });
                return;
            }
            if (maxNum == 1) {
                android.app.ApplicationExitInfo r2 = map.valueAt(0);
                for (int i2 = 1; i2 < numRep; i2++) {
                    android.app.ApplicationExitInfo t = map.valueAt(i2);
                    if (r2.getTimestamp() < t.getTimestamp()) {
                        r2 = t;
                    }
                }
                results.add(r2);
                return;
            }
            java.util.ArrayList<android.app.ApplicationExitInfo> list = com.android.server.am.AppExitInfoTracker.this.mTmpInfoList2;
            list.clear();
            for (int i3 = 0; i3 < numRep; i3++) {
                list.add(map.valueAt(i3));
            }
            java.util.Collections.sort(list, new java.util.Comparator() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoContainer$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Long.compare(((android.app.ApplicationExitInfo) obj2).getTimestamp(), ((android.app.ApplicationExitInfo) obj).getTimestamp());
                }
            });
            for (int i4 = 0; i4 < maxNum; i4++) {
                results.add(list.get(i4));
            }
            list.clear();
        }

        void getExitInfoLocked(int filterPid, int maxNum, java.util.ArrayList<android.app.ApplicationExitInfo> results) {
            getInfosLocked(this.mInfos, filterPid, maxNum, results);
        }

        void addInfoLocked(android.util.SparseArray<android.app.ApplicationExitInfo> map, android.app.ApplicationExitInfo info) {
            int size = map.size();
            if (size >= this.mMaxCapacity) {
                int oldestIndex = -1;
                long oldestTimeStamp = Long.MAX_VALUE;
                for (int i = 0; i < size; i++) {
                    android.app.ApplicationExitInfo r = map.valueAt(i);
                    if (r.getTimestamp() < oldestTimeStamp) {
                        oldestTimeStamp = r.getTimestamp();
                        oldestIndex = i;
                    }
                }
                if (oldestIndex >= 0) {
                    java.io.File traceFile = map.valueAt(oldestIndex).getTraceFile();
                    if (traceFile != null) {
                        traceFile.delete();
                    }
                    map.removeAt(oldestIndex);
                }
            }
            int uid = info.getPackageUid();
            if (android.os.Process.isSdkSandboxUid(info.getRealUid())) {
                uid = info.getRealUid();
            }
            int pid = info.getPid();
            if (info.getProcessStateSummary() == null) {
                info.setProcessStateSummary((byte[]) com.android.server.am.AppExitInfoTracker.findAndRemoveFromSparse2dArray(com.android.server.am.AppExitInfoTracker.this.mActiveAppStateSummary, uid, pid));
            }
            if (info.getTraceFile() == null) {
                info.setTraceFile((java.io.File) com.android.server.am.AppExitInfoTracker.findAndRemoveFromSparse2dArray(com.android.server.am.AppExitInfoTracker.this.mActiveAppTraces, uid, pid));
            }
            info.setAppTraceRetriever(com.android.server.am.AppExitInfoTracker.this.mAppTraceRetriever);
            map.append(pid, info);
        }

        void addExitInfoLocked(android.app.ApplicationExitInfo info) {
            addInfoLocked(this.mInfos, info);
        }

        void addRecoverableCrashLocked(android.app.ApplicationExitInfo info) {
            addInfoLocked(this.mRecoverableCrashes, info);
        }

        boolean appendTraceIfNecessaryLocked(int pid, java.io.File traceFile) {
            android.app.ApplicationExitInfo r = this.mInfos.get(pid);
            if (r != null) {
                r.setTraceFile(traceFile);
                r.setAppTraceRetriever(com.android.server.am.AppExitInfoTracker.this.mAppTraceRetriever);
                return true;
            }
            return false;
        }

        void destroyLocked(android.util.SparseArray<android.app.ApplicationExitInfo> map) {
            for (int i = map.size() - 1; i >= 0; i--) {
                android.app.ApplicationExitInfo ai = map.valueAt(i);
                java.io.File traceFile = ai.getTraceFile();
                if (traceFile != null) {
                    traceFile.delete();
                }
                ai.setTraceFile(null);
                ai.setAppTraceRetriever(null);
            }
        }

        void destroyLocked() {
            destroyLocked(this.mInfos);
            destroyLocked(this.mRecoverableCrashes);
        }

        void forEachRecordLocked(java.util.function.BiFunction<java.lang.Integer, android.app.ApplicationExitInfo, java.lang.Integer> callback) {
            if (callback == null) {
                return;
            }
            for (int i = this.mInfos.size() - 1; i >= 0; i--) {
                switch (callback.apply(java.lang.Integer.valueOf(this.mInfos.keyAt(i)), this.mInfos.valueAt(i)).intValue()) {
                    case 1:
                        java.io.File traceFile = this.mInfos.valueAt(i).getTraceFile();
                        if (traceFile != null) {
                            traceFile.delete();
                        }
                        this.mInfos.removeAt(i);
                        break;
                    case 2:
                        return;
                }
            }
            for (int i2 = this.mRecoverableCrashes.size() - 1; i2 >= 0; i2--) {
                switch (callback.apply(java.lang.Integer.valueOf(this.mRecoverableCrashes.keyAt(i2)), this.mRecoverableCrashes.valueAt(i2)).intValue()) {
                    case 1:
                        java.io.File traceFile2 = this.mRecoverableCrashes.valueAt(i2).getTraceFile();
                        if (traceFile2 != null) {
                            traceFile2.delete();
                        }
                        this.mRecoverableCrashes.removeAt(i2);
                        break;
                    case 2:
                        return;
                }
            }
        }

        void dumpLocked(java.io.PrintWriter pw, java.lang.String prefix, android.icu.text.SimpleDateFormat sdf) {
            java.util.ArrayList<android.app.ApplicationExitInfo> list = new java.util.ArrayList<>();
            for (int i = this.mInfos.size() - 1; i >= 0; i--) {
                list.add(this.mInfos.valueAt(i));
            }
            for (int i2 = this.mRecoverableCrashes.size() - 1; i2 >= 0; i2--) {
                list.add(this.mRecoverableCrashes.valueAt(i2));
            }
            java.util.Collections.sort(list, new java.util.Comparator() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoContainer$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Long.compare(((android.app.ApplicationExitInfo) obj2).getTimestamp(), ((android.app.ApplicationExitInfo) obj).getTimestamp());
                }
            });
            int size = list.size();
            for (int i3 = 0; i3 < size; i3++) {
                list.get(i3).dump(pw, prefix + "  ", "#" + i3, sdf);
            }
        }

        void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.mUid);
            for (int i = 0; i < this.mInfos.size(); i++) {
                this.mInfos.valueAt(i).writeToProto(proto, 2246267895810L);
            }
            for (int i2 = 0; i2 < this.mRecoverableCrashes.size(); i2++) {
                this.mRecoverableCrashes.valueAt(i2).writeToProto(proto, 2246267895811L);
            }
            proto.end(token);
        }

        int readFromProto(android.util.proto.ProtoInputStream proto, long fieldId) throws android.util.proto.WireTypeMismatchException, java.io.IOException {
            long token = proto.start(fieldId);
            int next = proto.nextField();
            while (next != -1) {
                switch (next) {
                    case 1:
                        this.mUid = proto.readInt(1120986464257L);
                        break;
                    case 2:
                        android.app.ApplicationExitInfo info = new android.app.ApplicationExitInfo();
                        info.readFromProto(proto, 2246267895810L);
                        this.mInfos.put(info.getPid(), info);
                        break;
                    case 3:
                        android.app.ApplicationExitInfo info2 = new android.app.ApplicationExitInfo();
                        info2.readFromProto(proto, 2246267895811L);
                        this.mRecoverableCrashes.put(info2.getPid(), info2);
                        break;
                }
                next = proto.nextField();
            }
            proto.end(token);
            return this.mUid;
        }

        java.util.List<android.app.ApplicationExitInfo> toListLocked(java.util.List<android.app.ApplicationExitInfo> list, int filterPid) {
            if (list == null) {
                list = new java.util.ArrayList();
            }
            for (int i = this.mInfos.size() - 1; i >= 0; i--) {
                if (filterPid == 0 || filterPid == this.mInfos.keyAt(i)) {
                    list.add(this.mInfos.valueAt(i));
                }
            }
            for (int i2 = this.mRecoverableCrashes.size() - 1; i2 >= 0; i2--) {
                if (filterPid == 0 || filterPid == this.mRecoverableCrashes.keyAt(i2)) {
                    list.add(this.mRecoverableCrashes.valueAt(i2));
                }
            }
            return list;
        }
    }

    final class IsolatedUidRecords {
        private final android.util.SparseArray<android.util.ArraySet<java.lang.Integer>> mUidToIsolatedUidMap = new android.util.SparseArray<>();
        private final android.util.SparseArray<java.lang.Integer> mIsolatedUidToUidMap = new android.util.SparseArray<>();

        IsolatedUidRecords() {
        }

        void addIsolatedUid(int isolatedUid, int uid) {
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                android.util.ArraySet<java.lang.Integer> set = this.mUidToIsolatedUidMap.get(uid);
                if (set == null) {
                    set = new android.util.ArraySet<>();
                    this.mUidToIsolatedUidMap.put(uid, set);
                }
                set.add(java.lang.Integer.valueOf(isolatedUid));
                this.mIsolatedUidToUidMap.put(isolatedUid, java.lang.Integer.valueOf(uid));
            }
        }

        void removeIsolatedUid(int isolatedUid, int uid) {
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                int index = this.mUidToIsolatedUidMap.indexOfKey(uid);
                if (index >= 0) {
                    android.util.ArraySet<java.lang.Integer> set = this.mUidToIsolatedUidMap.valueAt(index);
                    set.remove(java.lang.Integer.valueOf(isolatedUid));
                    if (set.isEmpty()) {
                        this.mUidToIsolatedUidMap.removeAt(index);
                    }
                }
                this.mIsolatedUidToUidMap.remove(isolatedUid);
            }
        }

        java.lang.Integer getUidByIsolatedUid(int isolatedUid) {
            java.lang.Integer num;
            if (android.os.UserHandle.isIsolated(isolatedUid)) {
                synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                    num = this.mIsolatedUidToUidMap.get(isolatedUid);
                }
                return num;
            }
            return java.lang.Integer.valueOf(isolatedUid);
        }

        private void removeAppUidLocked(int uid) {
            android.util.ArraySet<java.lang.Integer> set = this.mUidToIsolatedUidMap.get(uid);
            if (set != null) {
                for (int i = set.size() - 1; i >= 0; i--) {
                    int isolatedUid = set.removeAt(i).intValue();
                    this.mIsolatedUidToUidMap.remove(isolatedUid);
                }
            }
        }

        void removeAppUid(int uid, boolean allUsers) {
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                if (allUsers) {
                    int uid2 = android.os.UserHandle.getAppId(uid);
                    for (int i = this.mUidToIsolatedUidMap.size() - 1; i >= 0; i--) {
                        int u = this.mUidToIsolatedUidMap.keyAt(i);
                        if (uid2 == android.os.UserHandle.getAppId(u)) {
                            removeAppUidLocked(u);
                        }
                        this.mUidToIsolatedUidMap.removeAt(i);
                    }
                } else {
                    removeAppUidLocked(uid);
                    this.mUidToIsolatedUidMap.remove(uid);
                }
            }
        }

        int removeIsolatedUidLocked(int isolatedUid) {
            int uid;
            if (!android.os.UserHandle.isIsolated(isolatedUid) || (uid = this.mIsolatedUidToUidMap.get(isolatedUid, -1).intValue()) == -1) {
                return isolatedUid;
            }
            this.mIsolatedUidToUidMap.remove(isolatedUid);
            android.util.ArraySet<java.lang.Integer> set = this.mUidToIsolatedUidMap.get(uid);
            if (set != null) {
                set.remove(java.lang.Integer.valueOf(isolatedUid));
            }
            return uid;
        }

        void removeByUserId(int userId) {
            if (userId == -2) {
                userId = com.android.server.am.AppExitInfoTracker.this.mService.mUserController.getCurrentUserId();
            }
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                if (userId == -1) {
                    this.mIsolatedUidToUidMap.clear();
                    this.mUidToIsolatedUidMap.clear();
                    return;
                }
                for (int i = this.mIsolatedUidToUidMap.size() - 1; i >= 0; i--) {
                    this.mIsolatedUidToUidMap.keyAt(i);
                    int uid = this.mIsolatedUidToUidMap.valueAt(i).intValue();
                    if (android.os.UserHandle.getUserId(uid) == userId) {
                        this.mIsolatedUidToUidMap.removeAt(i);
                        this.mUidToIsolatedUidMap.remove(uid);
                    }
                }
            }
        }
    }

    final class KillHandler extends android.os.Handler {
        static final int MSG_APP_KILL = 4104;
        static final int MSG_APP_RECOVERABLE_CRASH = 4106;
        static final int MSG_CHILD_PROC_DIED = 4102;
        static final int MSG_LMKD_PROC_KILLED = 4101;
        static final int MSG_PROC_DIED = 4103;
        static final int MSG_STATSD_LOG = 4105;

        KillHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 4101:
                    com.android.server.am.AppExitInfoTracker.this.mAppExitInfoSourceLmkd.onProcDied(msg.arg1, msg.arg2, null);
                    return;
                case MSG_CHILD_PROC_DIED /* 4102 */:
                    com.android.server.am.AppExitInfoTracker.this.mAppExitInfoSourceZygote.onProcDied(msg.arg1, msg.arg2, (java.lang.Integer) msg.obj);
                    return;
                case 4103:
                    android.app.ApplicationExitInfo raw = (android.app.ApplicationExitInfo) msg.obj;
                    synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                        com.android.server.am.AppExitInfoTracker.this.handleNoteProcessDiedLocked(raw);
                        break;
                    }
                    com.android.server.am.AppExitInfoTracker.this.recycleRawRecord(raw);
                    return;
                case MSG_APP_KILL /* 4104 */:
                    android.app.ApplicationExitInfo raw2 = (android.app.ApplicationExitInfo) msg.obj;
                    synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                        com.android.server.am.AppExitInfoTracker.this.handleNoteAppKillLocked(raw2);
                        break;
                    }
                    com.android.server.am.AppExitInfoTracker.this.recycleRawRecord(raw2);
                    return;
                case 4105:
                    synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                        com.android.server.am.AppExitInfoTracker.this.performLogToStatsdLocked((android.app.ApplicationExitInfo) msg.obj);
                        break;
                    }
                    return;
                case 4106:
                    android.app.ApplicationExitInfo raw3 = (android.app.ApplicationExitInfo) msg.obj;
                    synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                        com.android.server.am.AppExitInfoTracker.this.handleNoteAppRecoverableCrashLocked(raw3);
                        break;
                    }
                    com.android.server.am.AppExitInfoTracker.this.recycleRawRecord(raw3);
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    }

    boolean isFresh(long timestamp) {
        long now = java.lang.System.currentTimeMillis();
        return 300000 + timestamp >= now;
    }

    final class AppExitInfoExternalSource {
        private static final long APP_EXIT_INFO_FRESHNESS_MS = 300000;
        private final android.util.SparseArray<android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Object>>> mData = new android.util.SparseArray<>();
        private final java.lang.Integer mPresetReason;
        private java.util.function.BiConsumer<java.lang.Integer, java.lang.Integer> mProcDiedListener;
        private final java.lang.String mTag;

        AppExitInfoExternalSource(java.lang.String tag, java.lang.Integer reason) {
            this.mTag = tag;
            this.mPresetReason = reason;
        }

        private void addLocked(int pid, int uid, java.lang.Object extra) {
            java.lang.Integer k = com.android.server.am.AppExitInfoTracker.this.mIsolatedUidRecords.getUidByIsolatedUid(uid);
            if (k != null) {
                uid = k.intValue();
            }
            android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Object>> array = this.mData.get(uid);
            if (array == null) {
                array = new android.util.SparseArray<>();
                this.mData.put(uid, array);
            }
            array.put(pid, new android.util.Pair<>(java.lang.Long.valueOf(java.lang.System.currentTimeMillis()), extra));
        }

        android.util.Pair<java.lang.Long, java.lang.Object> remove(int pid, int uid) {
            android.util.Pair<java.lang.Long, java.lang.Object> p;
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                java.lang.Integer k = com.android.server.am.AppExitInfoTracker.this.mIsolatedUidRecords.getUidByIsolatedUid(uid);
                if (k != null) {
                    uid = k.intValue();
                }
                android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Object>> array = this.mData.get(uid);
                if (array == null || (p = array.get(pid)) == null) {
                    return null;
                }
                array.remove(pid);
                return com.android.server.am.AppExitInfoTracker.this.isFresh(((java.lang.Long) p.first).longValue()) ? p : null;
            }
        }

        void removeByUserId(int userId) {
            if (userId == -2) {
                userId = com.android.server.am.AppExitInfoTracker.this.mService.mUserController.getCurrentUserId();
            }
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                if (userId == -1) {
                    this.mData.clear();
                    return;
                }
                for (int i = this.mData.size() - 1; i >= 0; i--) {
                    int uid = this.mData.keyAt(i);
                    if (android.os.UserHandle.getUserId(uid) == userId) {
                        this.mData.removeAt(i);
                    }
                }
            }
        }

        void removeByUidLocked(int uid, boolean allUsers) {
            java.lang.Integer k;
            if (android.os.UserHandle.isIsolated(uid) && (k = com.android.server.am.AppExitInfoTracker.this.mIsolatedUidRecords.getUidByIsolatedUid(uid)) != null) {
                uid = k.intValue();
            }
            if (allUsers) {
                int uid2 = android.os.UserHandle.getAppId(uid);
                for (int i = this.mData.size() - 1; i >= 0; i--) {
                    if (android.os.UserHandle.getAppId(this.mData.keyAt(i)) == uid2) {
                        this.mData.removeAt(i);
                    }
                }
                return;
            }
            this.mData.remove(uid);
        }

        void setOnProcDiedListener(java.util.function.BiConsumer<java.lang.Integer, java.lang.Integer> listener) {
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                this.mProcDiedListener = listener;
            }
        }

        void onProcDied(final int pid, final int uid, java.lang.Integer status) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.i("ActivityManager", this.mTag + ": proc died: pid=" + pid + " uid=" + uid + ", status=" + status);
            }
            if (com.android.server.am.AppExitInfoTracker.this.mService == null) {
                return;
            }
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                if (!com.android.server.am.AppExitInfoTracker.this.updateExitInfoIfNecessaryLocked(pid, uid, status, this.mPresetReason)) {
                    addLocked(pid, uid, status);
                }
                final java.util.function.BiConsumer<java.lang.Integer, java.lang.Integer> listener = this.mProcDiedListener;
                if (listener != null) {
                    com.android.server.am.AppExitInfoTracker.this.mService.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.AppExitInfoTracker$AppExitInfoExternalSource$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            listener.accept(java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid));
                        }
                    });
                }
            }
        }
    }

    class AppTraceRetriever extends android.app.IAppTraceRetriever.Stub {
        AppTraceRetriever() {
        }

        public android.os.ParcelFileDescriptor getTraceFileDescriptor(java.lang.String packageName, int uid, int pid) {
            com.android.server.am.AppExitInfoTracker.this.mService.enforceNotIsolatedCaller("getTraceFileDescriptor");
            if (android.text.TextUtils.isEmpty(packageName)) {
                throw new java.lang.IllegalArgumentException("Invalid package name");
            }
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(uid);
            com.android.server.am.AppExitInfoTracker.this.mService.mUserController.handleIncomingUser(callingPid, callingUid, userId, true, 0, "getTraceFileDescriptor", null);
            int filterUid = com.android.server.am.AppExitInfoTracker.this.mService.enforceDumpPermissionForPackage(packageName, userId, callingUid, "getTraceFileDescriptor");
            if (filterUid == -1) {
                return null;
            }
            synchronized (com.android.server.am.AppExitInfoTracker.this.mLock) {
                android.app.ApplicationExitInfo info = com.android.server.am.AppExitInfoTracker.this.getExitInfoLocked(packageName, filterUid, pid);
                if (info == null) {
                    return null;
                }
                java.io.File traceFile = info.getTraceFile();
                if (traceFile == null) {
                    return null;
                }
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return android.os.ParcelFileDescriptor.open(traceFile, 268435456);
                } catch (java.io.FileNotFoundException e) {
                    return null;
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }
    }
}
