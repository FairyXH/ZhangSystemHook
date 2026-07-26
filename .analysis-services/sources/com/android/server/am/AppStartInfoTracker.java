package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class AppStartInfoTracker {
    static final java.lang.String APP_START_INFO_FILE = "procstartinfo";
    static final int APP_START_INFO_HISTORY_LIST_SIZE = 16;
    private static final int APP_START_INFO_MONITORING_MODE_LIST_SIZE = 100;
    private static final long APP_START_INFO_PERSIST_INTERVAL = java.util.concurrent.TimeUnit.MINUTES.toMillis(30);
    static final java.lang.String APP_START_STORE_DIR = "procstartstore";
    private static final boolean DEBUG = false;
    private static final int FOREACH_ACTION_NONE = 0;
    private static final int FOREACH_ACTION_REMOVE_AND_STOP_ITERATION = 3;
    private static final int FOREACH_ACTION_REMOVE_ITEM = 1;
    private static final int FOREACH_ACTION_STOP_ITERATION = 2;
    static final int MAX_IN_PROGRESS_RECORDS = 5;
    private static final java.lang.String MONITORING_MODE_EMPTY_TEXT = "No records";
    private static final java.lang.String TAG = "ActivityManager";
    int mAppStartInfoHistoryListSize;
    private android.os.Handler mHandler;
    java.io.File mProcStartInfoFile;
    java.io.File mProcStartStoreDir;
    com.android.server.am.ActivityManagerService mService;
    final java.lang.Object mLock = new java.lang.Object();
    boolean mEnabled = false;
    com.android.internal.os.MonotonicClock mMonotonicClock = null;
    private java.lang.Runnable mAppStartInfoPersistTask = null;
    private long mLastAppStartInfoPersistTimestamp = 0;
    java.util.concurrent.atomic.AtomicBoolean mAppStartInfoLoaded = new java.util.concurrent.atomic.AtomicBoolean();
    final java.util.ArrayList<android.app.ApplicationStartInfo> mTmpStartInfoList = new java.util.ArrayList<>();
    final android.util.ArrayMap<java.lang.Long, android.app.ApplicationStartInfo> mInProgressRecords = new android.util.ArrayMap<>();
    final java.util.ArrayList<java.lang.Integer> mTemporaryInProgressIndexes = new java.util.ArrayList<>();
    private final com.android.server.am.AppStartInfoTracker.AppStartInfoTrackerWrapper mWrapper = new com.android.server.am.AppStartInfoTracker.AppStartInfoTrackerWrapper();
    private final android.util.SparseArray<java.util.ArrayList<com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback>> mCallbacks = new android.util.SparseArray<>();
    private final com.android.internal.app.ProcessMap<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer> mData = new com.android.internal.app.ProcessMap<>();

    AppStartInfoTracker() {
    }

    void init(com.android.server.am.ActivityManagerService service) {
        this.mService = service;
        com.android.server.ServiceThread thread = new com.android.server.ServiceThread("ActivityManager:handler", 10, true);
        thread.start();
        this.mHandler = new android.os.Handler(thread.getLooper());
        this.mProcStartStoreDir = new java.io.File(com.android.server.SystemServiceManager.ensureSystemDir(), APP_START_STORE_DIR);
        if (!android.os.FileUtils.createDir(this.mProcStartStoreDir)) {
            android.util.Slog.e("ActivityManager", "Unable to create " + this.mProcStartStoreDir);
        } else {
            this.mProcStartInfoFile = new java.io.File(this.mProcStartStoreDir, APP_START_INFO_FILE);
            this.mAppStartInfoHistoryListSize = 16;
        }
    }

    void onSystemReady() {
        this.mEnabled = android.app.Flags.appStartInfo();
        if (!this.mEnabled) {
            return;
        }
        registerForUserRemoval();
        registerForPackageRemoval();
        com.android.server.IoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSystemReady$0();
            }
        });
        if (this.mMonotonicClock == null) {
            this.mMonotonicClock = new com.android.internal.os.MonotonicClock(com.android.internal.os.Clock.SYSTEM_CLOCK.elapsedRealtime(), com.android.internal.os.Clock.SYSTEM_CLOCK);
        }
    }

    private void maybeTrimInProgressRecordsLocked() {
        if (this.mInProgressRecords.size() <= 5) {
            return;
        }
        this.mTemporaryInProgressIndexes.clear();
        for (int i = 0; i < this.mInProgressRecords.size(); i++) {
            this.mTemporaryInProgressIndexes.add(i, java.lang.Integer.valueOf(i));
        }
        java.util.Collections.sort(this.mTemporaryInProgressIndexes, new java.util.Comparator() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return this.f$0.lambda$maybeTrimInProgressRecordsLocked$1((java.lang.Integer) obj, (java.lang.Integer) obj2);
            }
        });
        if (this.mTemporaryInProgressIndexes.size() == 6) {
            this.mInProgressRecords.removeAt(this.mTemporaryInProgressIndexes.get(0).intValue());
        } else {
            this.mTemporaryInProgressIndexes.subList(this.mTemporaryInProgressIndexes.size() - 5, this.mTemporaryInProgressIndexes.size()).clear();
            java.util.Collections.sort(this.mTemporaryInProgressIndexes);
            for (int i2 = this.mTemporaryInProgressIndexes.size() - 1; i2 >= 0; i2--) {
                this.mInProgressRecords.removeAt(this.mTemporaryInProgressIndexes.get(i2).intValue());
            }
        }
        this.mTemporaryInProgressIndexes.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$maybeTrimInProgressRecordsLocked$1(java.lang.Integer a, java.lang.Integer b) {
        return java.lang.Long.compare(this.mInProgressRecords.keyAt(a.intValue()).longValue(), this.mInProgressRecords.keyAt(b.intValue()).longValue());
    }

    void onIntentStarted(android.content.Intent intent, long timestampNanos) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                android.app.ApplicationStartInfo start = new android.app.ApplicationStartInfo(getMonotonicTime());
                start.setStartupState(0);
                start.setIntent(intent);
                start.setStartType(0);
                start.addStartupTimestamp(0, timestampNanos);
                if (intent != null && intent.getCategories() != null && intent.getCategories().contains("android.intent.category.LAUNCHER")) {
                    start.setReason(6);
                } else {
                    start.setReason(11);
                }
                this.mInProgressRecords.put(java.lang.Long.valueOf(timestampNanos), start);
                maybeTrimInProgressRecordsLocked();
            }
        }
    }

    void onIntentFailed(long id) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                int index = this.mInProgressRecords.indexOfKey(java.lang.Long.valueOf(id));
                if (index < 0) {
                    return;
                }
                android.app.ApplicationStartInfo info = this.mInProgressRecords.valueAt(index);
                if (info == null) {
                    this.mInProgressRecords.removeAt(index);
                } else {
                    info.setStartupState(1);
                    this.mInProgressRecords.removeAt(index);
                }
            }
        }
    }

    void onActivityLaunched(long id, android.content.ComponentName name, long temperature, com.android.server.am.ProcessRecord app) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                int index = this.mInProgressRecords.indexOfKey(java.lang.Long.valueOf(id));
                if (index < 0) {
                    return;
                }
                android.app.ApplicationStartInfo info = this.mInProgressRecords.valueAt(index);
                if (info != null && app != null) {
                    info.setStartType((int) temperature);
                    addBaseFieldsFromProcessRecord(info, app);
                    android.app.ApplicationStartInfo newInfo = addStartInfoLocked(info);
                    if (newInfo == null) {
                        this.mInProgressRecords.removeAt(index);
                    } else {
                        this.mInProgressRecords.setValueAt(index, newInfo);
                    }
                    return;
                }
                this.mInProgressRecords.removeAt(index);
            }
        }
    }

    void onActivityLaunchCancelled(long id) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                int index = this.mInProgressRecords.indexOfKey(java.lang.Long.valueOf(id));
                if (index < 0) {
                    return;
                }
                android.app.ApplicationStartInfo info = this.mInProgressRecords.valueAt(index);
                if (info == null) {
                    this.mInProgressRecords.removeAt(index);
                } else {
                    info.setStartupState(1);
                    this.mInProgressRecords.removeAt(index);
                }
            }
        }
    }

    void onActivityLaunchFinished(long id, android.content.ComponentName name, long timestampNanos, int launchMode) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                int index = this.mInProgressRecords.indexOfKey(java.lang.Long.valueOf(id));
                if (index < 0) {
                    return;
                }
                android.app.ApplicationStartInfo info = this.mInProgressRecords.valueAt(index);
                if (info == null) {
                    this.mInProgressRecords.removeAt(index);
                    return;
                }
                info.setLaunchMode(launchMode);
                if (!android.app.Flags.appStartInfoTimestamps()) {
                    info.setStartupState(2);
                    checkCompletenessAndCallback(info);
                }
                this.mInProgressRecords.removeAt(index);
            }
        }
    }

    void onReportFullyDrawn(long id, long timestampNanos) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                int index = this.mInProgressRecords.indexOfKey(java.lang.Long.valueOf(id));
                if (index < 0) {
                    return;
                }
                android.app.ApplicationStartInfo info = this.mInProgressRecords.valueAt(index);
                if (info == null) {
                    this.mInProgressRecords.removeAt(index);
                } else {
                    info.addStartupTimestamp(5, timestampNanos);
                    this.mInProgressRecords.removeAt(index);
                }
            }
        }
    }

    public void handleProcessServiceStart(long startTimeNs, com.android.server.am.ProcessRecord app, com.android.server.am.ServiceRecord serviceRecord) {
        int i;
        synchronized (this.mLock) {
            if (this.mEnabled) {
                android.app.ApplicationStartInfo start = new android.app.ApplicationStartInfo(getMonotonicTime());
                addBaseFieldsFromProcessRecord(start, app);
                start.setStartupState(0);
                start.addStartupTimestamp(0, startTimeNs);
                start.setStartType(1);
                if (serviceRecord.permission != null && serviceRecord.permission.contains("android.permission.BIND_JOB_SERVICE")) {
                    i = 5;
                } else {
                    i = 10;
                }
                start.setReason(i);
                if (serviceRecord.intent != null) {
                    start.setIntent(serviceRecord.intent.getIntent());
                }
                addStartInfoLocked(start);
            }
        }
    }

    public void handleProcessBroadcastStart(long startTimeNs, com.android.server.am.ProcessRecord app, android.content.Intent intent, boolean isAlarm) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                android.app.ApplicationStartInfo start = new android.app.ApplicationStartInfo(getMonotonicTime());
                addBaseFieldsFromProcessRecord(start, app);
                start.setStartupState(0);
                start.addStartupTimestamp(0, startTimeNs);
                start.setStartType(1);
                if (isAlarm) {
                    start.setReason(0);
                } else {
                    start.setReason(3);
                }
                start.setIntent(intent);
                addStartInfoLocked(start);
            }
        }
    }

    public void handleProcessContentProviderStart(long startTimeNs, com.android.server.am.ProcessRecord app) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                android.app.ApplicationStartInfo start = new android.app.ApplicationStartInfo(getMonotonicTime());
                addBaseFieldsFromProcessRecord(start, app);
                start.setStartupState(0);
                start.addStartupTimestamp(0, startTimeNs);
                start.setStartType(1);
                start.setReason(4);
                addStartInfoLocked(start);
            }
        }
    }

    public void handleProcessBackupStart(long startTimeNs, com.android.server.am.ProcessRecord app, com.android.server.am.BackupRecord backupRecord, boolean cold) {
        int i;
        synchronized (this.mLock) {
            if (this.mEnabled) {
                android.app.ApplicationStartInfo start = new android.app.ApplicationStartInfo(getMonotonicTime());
                addBaseFieldsFromProcessRecord(start, app);
                start.setStartupState(0);
                start.addStartupTimestamp(0, startTimeNs);
                if (cold) {
                    i = 1;
                } else {
                    i = 2;
                }
                start.setStartType(i);
                start.setReason(1);
                addStartInfoLocked(start);
            }
        }
    }

    private void addBaseFieldsFromProcessRecord(android.app.ApplicationStartInfo start, com.android.server.am.ProcessRecord app) {
        if (app == null) {
            return;
        }
        boolean z = false;
        int definingUid = app.getHostingRecord() != null ? app.getHostingRecord().getDefiningUid() : 0;
        start.setPid(app.getPid());
        start.setRealUid(app.uid);
        start.setPackageUid(app.info.uid);
        start.setDefiningUid(definingUid > 0 ? definingUid : app.info.uid);
        start.setProcessName(app.processName);
        start.setPackageName(app.info.packageName);
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.stayStopped()) {
            com.android.server.wm.WindowProcessController wpc = app.getWindowProcessController();
            if (app.wasForceStopped() || (wpc != null && wpc.wasForceStopped())) {
                z = true;
            }
            start.setForceStopped(z);
        }
    }

    void configureDetailedMonitoring(java.io.PrintWriter pw, java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda6
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                        return com.android.server.am.AppStartInfoTracker.lambda$configureDetailedMonitoring$2((java.lang.String) obj, (android.util.SparseArray) obj2);
                    }
                });
                if (android.text.TextUtils.isEmpty(packageName)) {
                    pw.println("ActivityManager AppStartInfo detailed monitoring disabled");
                } else {
                    android.util.SparseArray<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer> array = (android.util.SparseArray) this.mData.getMap().get(packageName);
                    if (array != null) {
                        for (int i = 0; i < array.size(); i++) {
                            array.valueAt(i).enableAppMonitoringModeForUser(userId);
                        }
                        pw.println("ActivityManager AppStartInfo detailed monitoring enabled for " + packageName);
                    } else {
                        pw.println("Package " + packageName + " not found");
                    }
                }
            }
        }
    }

    static /* synthetic */ java.lang.Integer lambda$configureDetailedMonitoring$2(java.lang.String name, android.util.SparseArray records) {
        for (int i = 0; i < records.size(); i++) {
            ((com.android.server.am.AppStartInfoTracker.AppStartInfoContainer) records.valueAt(i)).disableAppMonitoringMode();
        }
        return 0;
    }

    void addTimestampToStart(com.android.server.am.ProcessRecord app, long timeNs, int key) {
        addTimestampToStart(app.info.packageName, app.uid, timeNs, key);
    }

    void addTimestampToStart(java.lang.String packageName, int uid, long timeNs, int key) {
        if (!this.mEnabled) {
            return;
        }
        synchronized (this.mLock) {
            com.android.server.am.AppStartInfoTracker.AppStartInfoContainer container = (com.android.server.am.AppStartInfoTracker.AppStartInfoContainer) this.mData.get(packageName, uid);
            if (container == null) {
                return;
            }
            container.addTimestampToStartLocked(key, timeNs);
        }
    }

    private android.app.ApplicationStartInfo addStartInfoLocked(android.app.ApplicationStartInfo raw) {
        if (!this.mAppStartInfoLoaded.get()) {
            android.util.Slog.w("ActivityManager", "Skipping saving the start info due to ongoing loading from storage");
            return null;
        }
        android.app.ApplicationStartInfo info = new android.app.ApplicationStartInfo(raw);
        com.android.server.am.AppStartInfoTracker.AppStartInfoContainer container = (com.android.server.am.AppStartInfoTracker.AppStartInfoContainer) this.mData.get(raw.getPackageName(), raw.getRealUid());
        if (container == null) {
            container = new com.android.server.am.AppStartInfoTracker.AppStartInfoContainer(this.mAppStartInfoHistoryListSize);
            container.mUid = info.getRealUid();
            this.mData.put(raw.getPackageName(), raw.getRealUid(), container);
        }
        container.addStartInfoLocked(info);
        schedulePersistProcessStartInfo(false);
        return info;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCompletenessAndCallback(android.app.ApplicationStartInfo startInfo) {
        synchronized (this.mLock) {
            if (startInfo.getStartupState() == 2) {
                java.util.List<com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback> callbacks = this.mCallbacks.get(startInfo.getRealUid());
                if (callbacks == null) {
                    return;
                }
                int size = callbacks.size();
                for (int i = 0; i < size; i++) {
                    if (callbacks.get(i) != null) {
                        callbacks.get(i).onApplicationStartInfoComplete(startInfo);
                    }
                }
                this.mCallbacks.remove(startInfo.getRealUid());
            }
        }
    }

    void getStartInfo(java.lang.String packageName, final int filterUid, int filterPid, int maxNum, java.util.ArrayList<android.app.ApplicationStartInfo> results) {
        if (!this.mEnabled) {
            return;
        }
        if (maxNum == 0) {
            maxNum = 16;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                boolean emptyPackageName = android.text.TextUtils.isEmpty(packageName);
                if (!emptyPackageName) {
                    com.android.server.am.AppStartInfoTracker.AppStartInfoContainer container = (com.android.server.am.AppStartInfoTracker.AppStartInfoContainer) this.mData.get(packageName, filterUid);
                    if (container != null) {
                        container.getStartInfoLocked(filterPid, maxNum, results);
                    }
                } else {
                    final java.util.ArrayList<android.app.ApplicationStartInfo> list = this.mTmpStartInfoList;
                    list.clear();
                    forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda0
                        @Override // java.util.function.BiFunction
                        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                            return com.android.server.am.AppStartInfoTracker.lambda$getStartInfo$3(filterUid, list, (java.lang.String) obj, (android.util.SparseArray) obj2);
                        }
                    });
                    java.util.Collections.sort(list, new java.util.Comparator() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda1
                        @Override // java.util.Comparator
                        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                            return java.lang.Long.compare(((android.app.ApplicationStartInfo) obj2).getMonoticCreationTimeMs(), ((android.app.ApplicationStartInfo) obj).getMonoticCreationTimeMs());
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

    static /* synthetic */ java.lang.Integer lambda$getStartInfo$3(int filterUid, java.util.ArrayList list, java.lang.String name, android.util.SparseArray records) {
        com.android.server.am.AppStartInfoTracker.AppStartInfoContainer container = (com.android.server.am.AppStartInfoTracker.AppStartInfoContainer) records.get(filterUid);
        if (container != null) {
            list.addAll(container.mInfos);
        }
        return 0;
    }

    final class ApplicationStartInfoCompleteCallback implements android.os.IBinder.DeathRecipient {
        private final android.app.IApplicationStartInfoCompleteListener mCallback;
        private final int mUid;

        ApplicationStartInfoCompleteCallback(android.app.IApplicationStartInfoCompleteListener callback, int uid) {
            this.mCallback = callback;
            this.mUid = uid;
            try {
                this.mCallback.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        void onApplicationStartInfoComplete(android.app.ApplicationStartInfo startInfo) {
            try {
                this.mCallback.onApplicationStartInfoComplete(startInfo);
            } catch (android.os.RemoteException e) {
            }
        }

        void unlinkToDeath() {
            this.mCallback.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.am.AppStartInfoTracker.this.removeStartInfoCompleteListener(this.mCallback, this.mUid, false);
        }
    }

    void addStartInfoCompleteListener(android.app.IApplicationStartInfoCompleteListener listener, int uid) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                java.util.ArrayList<com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback> callbacks = this.mCallbacks.get(uid);
                if (callbacks == null) {
                    android.util.SparseArray<java.util.ArrayList<com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback>> sparseArray = this.mCallbacks;
                    java.util.ArrayList<com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback> arrayList = new java.util.ArrayList<>();
                    callbacks = arrayList;
                    sparseArray.set(uid, arrayList);
                }
                callbacks.add(new com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback(listener, uid));
            }
        }
    }

    void removeStartInfoCompleteListener(android.app.IApplicationStartInfoCompleteListener listener, int uid, boolean unlinkDeathRecipient) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                java.util.ArrayList<com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback> callbacks = this.mCallbacks.get(uid);
                if (callbacks == null) {
                    return;
                }
                int size = callbacks.size();
                int index = 0;
                while (true) {
                    if (index >= size) {
                        break;
                    }
                    com.android.server.am.AppStartInfoTracker.ApplicationStartInfoCompleteCallback callback = callbacks.get(index);
                    if (callback.mCallback != listener) {
                        index++;
                    } else if (unlinkDeathRecipient) {
                        callback.unlinkToDeath();
                    }
                }
                if (index < size) {
                    callbacks.remove(index);
                }
                if (callbacks.isEmpty()) {
                    this.mCallbacks.remove(uid);
                }
            }
        }
    }

    private boolean forEachPackageLocked(java.util.function.BiFunction<java.lang.String, android.util.SparseArray<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer>, java.lang.Integer> callback) {
        if (callback != null) {
            android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer>> map = this.mData.getMap();
            for (int i = map.size() - 1; i >= 0; i--) {
                switch (callback.apply(map.keyAt(i), map.valueAt(i)).intValue()) {
                    case 1:
                        map.removeAt(i);
                        break;
                    case 2:
                        return false;
                    case 3:
                        map.removeAt(i);
                        return false;
                }
            }
        }
        return true;
    }

    private void removePackageLocked(java.lang.String packageName, int uid, boolean removeUid, int userId) {
        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer>> map = this.mData.getMap();
        android.util.SparseArray<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer> array = map.get(packageName);
        if (array == null) {
            return;
        }
        if (userId == -1) {
            this.mData.getMap().remove(packageName);
            return;
        }
        int i = array.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            if (android.os.UserHandle.getUserId(array.keyAt(i)) != userId) {
                i--;
            } else {
                array.removeAt(i);
                break;
            }
        }
        int i2 = array.size();
        if (i2 == 0) {
            map.remove(packageName);
        }
    }

    private void removeByUserIdLocked(final int userId) {
        if (userId == -1) {
            this.mData.getMap().clear();
        } else {
            forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda4
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.am.AppStartInfoTracker.lambda$removeByUserIdLocked$5(userId, (java.lang.String) obj, (android.util.SparseArray) obj2);
                }
            });
        }
    }

    static /* synthetic */ java.lang.Integer lambda$removeByUserIdLocked$5(int userId, java.lang.String packageName, android.util.SparseArray records) {
        int i = records.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            if (android.os.UserHandle.getUserId(records.keyAt(i)) != userId) {
                i--;
            } else {
                records.removeAt(i);
                break;
            }
        }
        int i2 = records.size();
        return java.lang.Integer.valueOf(i2 != 0 ? 0 : 1);
    }

    void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                removeByUserIdLocked(userId);
                schedulePersistProcessStartInfo(true);
            }
        }
    }

    void onPackageRemoved(java.lang.String packageName, int uid, boolean allUsers) {
        if (this.mEnabled && packageName != null) {
            boolean removeUid = android.text.TextUtils.isEmpty(this.mService.mPackageManagerInt.getNameForUid(uid));
            synchronized (this.mLock) {
                removePackageLocked(packageName, uid, removeUid, allUsers ? -1 : android.os.UserHandle.getUserId(uid));
                schedulePersistProcessStartInfo(true);
            }
        }
    }

    private void registerForUserRemoval() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_REMOVED");
        this.mService.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.am.AppStartInfoTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (userId < 1) {
                    return;
                }
                com.android.server.am.AppStartInfoTracker.this.onUserRemoved(userId);
            }
        }, filter, null, this.mHandler);
    }

    private void registerForPackageRemoval() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        this.mService.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.am.AppStartInfoTracker.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                boolean replacing = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                if (replacing) {
                    return;
                }
                int uid = intent.getIntExtra("android.intent.extra.UID", -10000);
                boolean allUsers = intent.getBooleanExtra("android.intent.extra.REMOVED_FOR_ALL_USERS", false);
                com.android.server.am.AppStartInfoTracker.this.onPackageRemoved(intent.getData().getSchemeSpecificPart(), uid, allUsers);
            }
        }, filter, null, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: loadExistingProcessStartInfo, reason: merged with bridge method [inline-methods] */
    public void lambda$onSystemReady$0() {
        if (this.mEnabled) {
            if (!this.mProcStartInfoFile.canRead()) {
                this.mAppStartInfoLoaded.set(true);
                return;
            }
            java.io.FileInputStream fin = null;
            try {
                try {
                    try {
                        android.util.AtomicFile af = new android.util.AtomicFile(this.mProcStartInfoFile);
                        fin = af.openRead();
                        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(fin);
                        for (int next = proto.nextField(); next != -1; next = proto.nextField()) {
                            switch (next) {
                                case 1:
                                    synchronized (this.mLock) {
                                        this.mLastAppStartInfoPersistTimestamp = proto.readLong(1112396529665L);
                                        break;
                                    }
                                    break;
                                case 2:
                                    long monotonicTime = next;
                                    loadPackagesFromProto(proto, monotonicTime);
                                    break;
                                case 3:
                                    long monotonicTime2 = proto.readLong(1112396529667L);
                                    this.mMonotonicClock = new com.android.internal.os.MonotonicClock(monotonicTime2, com.android.internal.os.Clock.SYSTEM_CLOCK);
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (fin != null) {
                            fin.close();
                        }
                    } catch (java.io.IOException | java.lang.IllegalArgumentException | android.util.proto.WireTypeMismatchException | java.lang.ClassNotFoundException e) {
                        android.util.Slog.w("ActivityManager", "Error in loading historical app start info from persistent storage: " + e);
                        if (fin != null) {
                            fin.close();
                        }
                    }
                } catch (java.io.IOException e2) {
                }
                this.mAppStartInfoLoaded.set(true);
            } catch (java.lang.Throwable th) {
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (java.io.IOException e3) {
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.util.proto.WireTypeMismatchException */
    private void loadPackagesFromProto(android.util.proto.ProtoInputStream proto, long fieldId) throws android.util.proto.WireTypeMismatchException, java.io.IOException, java.lang.ClassNotFoundException {
        long token = proto.start(fieldId);
        java.lang.String pkgName = "";
        int next = proto.nextField();
        while (next != -1) {
            switch (next) {
                case 1:
                    pkgName = proto.readString(1138166333441L);
                    break;
                case 2:
                    com.android.server.am.AppStartInfoTracker.AppStartInfoContainer container = new com.android.server.am.AppStartInfoTracker.AppStartInfoContainer(this.mAppStartInfoHistoryListSize);
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

    void persistProcessStartInfo() {
        boolean succeeded;
        if (!this.mEnabled) {
            return;
        }
        android.util.AtomicFile af = new android.util.AtomicFile(this.mProcStartInfoFile);
        java.io.FileOutputStream out = null;
        long now = java.lang.System.currentTimeMillis();
        try {
            out = af.startWrite();
            final android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
            proto.write(1112396529665L, now);
            synchronized (this.mLock) {
                succeeded = forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda3
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                        return com.android.server.am.AppStartInfoTracker.lambda$persistProcessStartInfo$6(proto, (java.lang.String) obj, (android.util.SparseArray) obj2);
                    }
                });
                if (succeeded) {
                    this.mLastAppStartInfoPersistTimestamp = now;
                }
            }
            proto.write(1112396529667L, getMonotonicTime());
            if (succeeded) {
                proto.flush();
                af.finishWrite(out);
            } else {
                af.failWrite(out);
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w("ActivityManager", "Unable to write historical app start info into persistent storage: " + e);
            af.failWrite(out);
        }
        synchronized (this.mLock) {
            this.mAppStartInfoPersistTask = null;
        }
    }

    static /* synthetic */ java.lang.Integer lambda$persistProcessStartInfo$6(android.util.proto.ProtoOutputStream proto, java.lang.String packageName, android.util.SparseArray records) {
        long token = proto.start(2246267895810L);
        proto.write(1138166333441L, packageName);
        int uidArraySize = records.size();
        for (int j = 0; j < uidArraySize; j++) {
            try {
                ((com.android.server.am.AppStartInfoTracker.AppStartInfoContainer) records.valueAt(j)).writeToProto(proto, 2246267895810L);
            } catch (java.io.IOException e) {
                android.util.Slog.w("ActivityManager", "Unable to write app start info into persistentstorage: " + e);
                return 3;
            }
        }
        proto.end(token);
        return 0;
    }

    void schedulePersistProcessStartInfo(boolean immediately) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                if (this.mAppStartInfoPersistTask == null || immediately) {
                    if (this.mAppStartInfoPersistTask != null) {
                        com.android.server.IoThread.getHandler().removeCallbacks(this.mAppStartInfoPersistTask);
                    }
                    this.mAppStartInfoPersistTask = new java.lang.Runnable() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.persistProcessStartInfo();
                        }
                    };
                    com.android.server.IoThread.getHandler().postDelayed(this.mAppStartInfoPersistTask, immediately ? 0L : APP_START_INFO_PERSIST_INTERVAL);
                }
            }
        }
    }

    void clearProcessStartInfo(boolean removeFile) {
        synchronized (this.mLock) {
            if (this.mEnabled) {
                if (this.mAppStartInfoPersistTask != null) {
                    com.android.server.IoThread.getHandler().removeCallbacks(this.mAppStartInfoPersistTask);
                    this.mAppStartInfoPersistTask = null;
                }
                if (removeFile && this.mProcStartInfoFile != null) {
                    this.mProcStartInfoFile.delete();
                }
                this.mData.getMap().clear();
                this.mInProgressRecords.clear();
            }
        }
    }

    void clearHistoryProcessStartInfo(java.lang.String packageName, int userId) {
        if (!this.mEnabled) {
            return;
        }
        java.util.Optional.empty();
        if (android.text.TextUtils.isEmpty(packageName)) {
            synchronized (this.mLock) {
                removeByUserIdLocked(userId);
            }
        } else {
            int uid = this.mService.mPackageManagerInt.getPackageUid(packageName, 131072L, userId);
            java.util.Optional.of(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid)));
            synchronized (this.mLock) {
                removePackageLocked(packageName, uid, true, userId);
            }
        }
        schedulePersistProcessStartInfo(true);
    }

    void dumpHistoryProcessStartInfo(final java.io.PrintWriter pw, java.lang.String packageName) {
        if (!this.mEnabled) {
            return;
        }
        pw.println("ACTIVITY MANAGER LRU PROCESSES (dumpsys activity start-info)");
        final android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat();
        synchronized (this.mLock) {
            pw.println("Last Timestamp of Persistence Into Persistent Storage: " + sdf.format(new java.util.Date(this.mLastAppStartInfoPersistTimestamp)));
            if (android.text.TextUtils.isEmpty(packageName)) {
                forEachPackageLocked(new java.util.function.BiFunction() { // from class: com.android.server.am.AppStartInfoTracker$$ExternalSyntheticLambda8
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                        return this.f$0.lambda$dumpHistoryProcessStartInfo$7(pw, sdf, (java.lang.String) obj, (android.util.SparseArray) obj2);
                    }
                });
            } else {
                android.util.SparseArray<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer> array = (android.util.SparseArray) this.mData.getMap().get(packageName);
                if (array != null) {
                    dumpHistoryProcessStartInfoLocked(pw, "  ", packageName, array, sdf);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$dumpHistoryProcessStartInfo$7(java.io.PrintWriter pw, android.icu.text.SimpleDateFormat sdf, java.lang.String name, android.util.SparseArray records) {
        dumpHistoryProcessStartInfoLocked(pw, "  ", name, records, sdf);
        return 0;
    }

    private void dumpHistoryProcessStartInfoLocked(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String packageName, android.util.SparseArray<com.android.server.am.AppStartInfoTracker.AppStartInfoContainer> array, android.icu.text.SimpleDateFormat sdf) {
        pw.println(prefix + "package: " + packageName);
        int size = array.size();
        for (int i = 0; i < size; i++) {
            pw.println(prefix + "  Historical Process Start for userId=" + array.keyAt(i));
            array.valueAt(i).dumpLocked(pw, prefix + "    ", sdf);
        }
    }

    private long getMonotonicTime() {
        if (this.mMonotonicClock == null) {
            return 0L;
        }
        return this.mMonotonicClock.monotonicTime();
    }

    final class AppStartInfoContainer {
        private int mMaxCapacity;
        private int mUid;
        private boolean mMonitoringModeEnabled = false;
        private java.util.ArrayList<android.app.ApplicationStartInfo> mInfos = new java.util.ArrayList<>();

        AppStartInfoContainer(int maxCapacity) {
            this.mMaxCapacity = maxCapacity;
        }

        int getMaxCapacity() {
            if (this.mMonitoringModeEnabled) {
                return 100;
            }
            return this.mMaxCapacity;
        }

        void enableAppMonitoringModeForUser(int userId) {
            if (android.os.UserHandle.getUserId(this.mUid) == userId) {
                this.mMonitoringModeEnabled = true;
            }
        }

        void disableAppMonitoringMode() {
            this.mMonitoringModeEnabled = false;
            if (this.mInfos.size() <= getMaxCapacity()) {
                return;
            }
            java.util.Collections.sort(this.mInfos, new java.util.Comparator() { // from class: com.android.server.am.AppStartInfoTracker$AppStartInfoContainer$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Long.compare(((android.app.ApplicationStartInfo) obj2).getMonoticCreationTimeMs(), ((android.app.ApplicationStartInfo) obj).getMonoticCreationTimeMs());
                }
            });
            this.mInfos.subList(0, this.mInfos.size() - getMaxCapacity()).clear();
            this.mInfos.trimToSize();
        }

        void getStartInfoLocked(int filterPid, int maxNum, java.util.ArrayList<android.app.ApplicationStartInfo> results) {
            results.addAll(this.mInfos.size() <= maxNum ? 0 : this.mInfos.size() - maxNum, this.mInfos);
        }

        void addStartInfoLocked(android.app.ApplicationStartInfo info) {
            int size = this.mInfos.size();
            if (size >= getMaxCapacity()) {
                int oldestIndex = -1;
                long oldestTimeStamp = Long.MAX_VALUE;
                for (int i = 0; i < size; i++) {
                    android.app.ApplicationStartInfo startInfo = this.mInfos.get(i);
                    if (startInfo.getMonoticCreationTimeMs() < oldestTimeStamp) {
                        oldestTimeStamp = startInfo.getMonoticCreationTimeMs();
                        oldestIndex = i;
                    }
                }
                if (oldestIndex >= 0) {
                    this.mInfos.remove(oldestIndex);
                }
            }
            this.mInfos.add(info);
            java.util.Collections.sort(this.mInfos, new java.util.Comparator() { // from class: com.android.server.am.AppStartInfoTracker$AppStartInfoContainer$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Long.compare(((android.app.ApplicationStartInfo) obj2).getMonoticCreationTimeMs(), ((android.app.ApplicationStartInfo) obj).getMonoticCreationTimeMs());
                }
            });
        }

        void addTimestampToStartLocked(int key, long timestampNs) {
            if (this.mInfos.isEmpty()) {
                return;
            }
            android.app.ApplicationStartInfo startInfo = this.mInfos.get(0);
            if (!isAddTimestampAllowed(startInfo, key, timestampNs)) {
                return;
            }
            startInfo.addStartupTimestamp(key, timestampNs);
            if (key == 4 && android.app.Flags.appStartInfoTimestamps()) {
                startInfo.setStartupState(2);
                com.android.server.am.AppStartInfoTracker.this.checkCompletenessAndCallback(startInfo);
            }
        }

        private boolean isAddTimestampAllowed(android.app.ApplicationStartInfo startInfo, int key, long timestampNs) {
            int startupState = startInfo.getStartupState();
            if (startupState == 1) {
                return false;
            }
            java.util.Map<java.lang.Integer, java.lang.Long> timestamps = startInfo.getStartupTimestamps();
            if (startupState == 2) {
                switch (key) {
                    case 5:
                    case 7:
                        break;
                    case 6:
                        java.lang.Long firstFrameTimeNs = timestamps.get(4);
                        if (firstFrameTimeNs == null || timestampNs > firstFrameTimeNs.longValue()) {
                            return false;
                        }
                        break;
                    default:
                        return false;
                }
            }
            return timestamps.get(java.lang.Integer.valueOf(key)) == null;
        }

        void dumpLocked(java.io.PrintWriter pw, java.lang.String prefix, android.icu.text.SimpleDateFormat sdf) {
            if (this.mMonitoringModeEnabled) {
                java.util.List<java.lang.Long> coldStartTimes = new java.util.ArrayList<>();
                java.util.List<java.lang.Long> warmStartTimes = new java.util.ArrayList<>();
                java.util.List<java.lang.Long> hotStartTimes = new java.util.ArrayList<>();
                for (int i = 0; i < this.mInfos.size(); i++) {
                    android.app.ApplicationStartInfo startInfo = this.mInfos.get(i);
                    java.util.Map<java.lang.Integer, java.lang.Long> timestamps = startInfo.getStartupTimestamps();
                    if (timestamps.containsKey(0) && timestamps.containsKey(4)) {
                        long time = timestamps.get(4).longValue() - timestamps.get(0).longValue();
                        switch (startInfo.getStartType()) {
                            case 1:
                                coldStartTimes.add(java.lang.Long.valueOf(time));
                                break;
                            case 2:
                                warmStartTimes.add(java.lang.Long.valueOf(time));
                                break;
                            case 3:
                                hotStartTimes.add(java.lang.Long.valueOf(time));
                                break;
                        }
                    }
                }
                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append(prefix).append("  Average Start Time in ns for Cold Starts: ");
                boolean zIsEmpty = coldStartTimes.isEmpty();
                java.lang.Object objValueOf = com.android.server.am.AppStartInfoTracker.MONITORING_MODE_EMPTY_TEXT;
                pw.println(sbAppend.append(zIsEmpty ? com.android.server.am.AppStartInfoTracker.MONITORING_MODE_EMPTY_TEXT : java.lang.Long.valueOf(calculateAverage(coldStartTimes))).toString());
                pw.println(prefix + "  Average Start Time in ns for Warm Starts: " + (warmStartTimes.isEmpty() ? com.android.server.am.AppStartInfoTracker.MONITORING_MODE_EMPTY_TEXT : java.lang.Long.valueOf(calculateAverage(warmStartTimes))));
                java.lang.StringBuilder sbAppend2 = new java.lang.StringBuilder().append(prefix).append("  Average Start Time in ns for Hot Starts: ");
                if (!hotStartTimes.isEmpty()) {
                    objValueOf = java.lang.Long.valueOf(calculateAverage(hotStartTimes));
                }
                pw.println(sbAppend2.append(objValueOf).toString());
            }
            int size = this.mInfos.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mInfos.get(i2).dump(pw, prefix + "  ", "#" + i2, sdf);
            }
        }

        private long calculateAverage(java.util.List<java.lang.Long> vals) {
            return (long) vals.stream().mapToDouble(new java.util.function.ToDoubleFunction() { // from class: com.android.server.am.AppStartInfoTracker$AppStartInfoContainer$$ExternalSyntheticLambda1
                /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
                    jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v0 double, still in use, count: 1, list:
                      (r0v0 double) from 0x0006: RETURN (r0v0 double)
                    	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
                    	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
                    	at jadx.core.utils.InsnRemover.unbindInsn(InsnRemover.java:91)
                    	at jadx.core.utils.InsnRemover.addAndUnbind(InsnRemover.java:57)
                    	at jadx.core.dex.visitors.ModVisitor.removeStep(ModVisitor.java:468)
                    	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:97)
                    */
                @Override // java.util.function.ToDoubleFunction
                public final double applyAsDouble(java.lang.Object r3) {
                    /*
                        r2 = this;
                        java.lang.Long r3 = (java.lang.Long) r3
                        double r0 = com.android.server.am.AppStartInfoTracker.AppStartInfoContainer.lambda$calculateAverage$2(r3)
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppStartInfoTracker$AppStartInfoContainer$$ExternalSyntheticLambda1.applyAsDouble(java.lang.Object):double");
                }
            }).average().orElse(0.0d);
        }

        void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) throws java.io.IOException {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.mUid);
            int size = this.mInfos.size();
            for (int i = 0; i < size; i++) {
                this.mInfos.get(i).writeToProto(proto, 2246267895810L);
            }
            proto.write(1133871366147L, this.mMonitoringModeEnabled);
            proto.end(token);
        }

        int readFromProto(android.util.proto.ProtoInputStream proto, long fieldId) throws android.util.proto.WireTypeMismatchException, java.io.IOException, java.lang.ClassNotFoundException {
            long token = proto.start(fieldId);
            int next = proto.nextField();
            while (next != -1) {
                switch (next) {
                    case 1:
                        this.mUid = proto.readInt(1120986464257L);
                        break;
                    case 2:
                        android.app.ApplicationStartInfo info = new android.app.ApplicationStartInfo(0L);
                        info.readFromProto(proto, 2246267895810L);
                        this.mInfos.add(info);
                        break;
                    case 3:
                        this.mMonitoringModeEnabled = proto.readBoolean(1133871366147L);
                        break;
                }
                next = proto.nextField();
            }
            proto.end(token);
            return this.mUid;
        }
    }

    public com.android.server.am.AppStartInfoTracker.AppStartInfoTrackerWrapper getWrapper() {
        return this.mWrapper;
    }

    public class AppStartInfoTrackerWrapper implements com.android.server.am.IAppStartInfoTrackerWrapper {
        public AppStartInfoTrackerWrapper() {
        }

        @Override // com.android.server.am.IAppStartInfoTrackerWrapper
        public boolean hasAppStartupInfo(java.lang.String pkgName, int uid) {
            synchronized (com.android.server.am.AppStartInfoTracker.this.mLock) {
                boolean z = true;
                if (!com.android.server.am.AppStartInfoTracker.this.mEnabled) {
                    return true;
                }
                if (!com.android.server.am.AppStartInfoTracker.this.mAppStartInfoLoaded.get()) {
                    return true;
                }
                if (com.android.server.am.AppStartInfoTracker.this.mData.get(pkgName, uid) == null) {
                    z = false;
                }
                return z;
            }
        }
    }
}
