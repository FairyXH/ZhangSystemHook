package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public final class NativeTombstoneManager {
    private static final java.lang.String TAG = com.android.server.os.NativeTombstoneManager.class.getSimpleName();
    private static final java.io.File TOMBSTONE_DIR = new java.io.File("/data/tombstones");
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final com.android.server.os.NativeTombstoneManager.TombstoneWatcher mWatcher;
    private final java.util.concurrent.locks.ReentrantLock mTmpFileLock = new java.util.concurrent.locks.ReentrantLock();
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.os.INativeTombstoneManagerExt mNativeTombstoneManagerExt = (com.android.server.os.INativeTombstoneManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.os.INativeTombstoneManagerExt.class).base(this).create();
    private final android.util.SparseArray<com.android.server.os.NativeTombstoneManager.TombstoneFile> mTombstones = new android.util.SparseArray<>();

    NativeTombstoneManager(android.content.Context context) {
        this.mContext = context;
        com.android.server.ServiceThread thread = new com.android.server.ServiceThread(TAG + ":tombstoneWatcher", 10, true);
        thread.start();
        this.mHandler = thread.getThreadHandler();
        this.mWatcher = new com.android.server.os.NativeTombstoneManager.TombstoneWatcher();
        this.mWatcher.startWatching();
    }

    void onSystemReady() {
        registerForUserRemoval();
        registerForPackageRemoval();
        com.android.server.BootReceiver.initDropboxRateLimiter();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.os.NativeTombstoneManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSystemReady$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0() {
        java.io.File[] tombstoneFiles = TOMBSTONE_DIR.listFiles();
        for (int i = 0; tombstoneFiles != null && i < tombstoneFiles.length; i++) {
            if (tombstoneFiles[i].isFile()) {
                handleTombstone(tombstoneFiles[i]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTombstone(java.io.File path) {
        java.lang.String filename = path.getName();
        if (filename.endsWith(".tmp")) {
            this.mTmpFileLock.lock();
            try {
                path.delete();
                return;
            } finally {
                this.mTmpFileLock.unlock();
            }
        }
        if (!filename.startsWith("tombstone_") || this.mNativeTombstoneManagerExt.isOverLimitSize(path)) {
            return;
        }
        java.lang.String processName = "UNKNOWN";
        boolean isProtoFile = filename.endsWith(".pb");
        java.io.File protoPath = isProtoFile ? path : new java.io.File(path.getAbsolutePath() + ".pb");
        java.util.Optional<com.android.server.os.NativeTombstoneManager.TombstoneFile> parsedTombstone = handleProtoTombstone(protoPath, isProtoFile);
        if (parsedTombstone.isPresent()) {
            processName = parsedTombstone.get().getProcessName();
        }
        com.android.server.BootReceiver.addTombstoneToDropBox(this.mContext, path, isProtoFile, processName, this.mTmpFileLock);
        java.lang.ref.Reference.reachabilityFence(this.mWatcher);
    }

    private java.util.Optional<com.android.server.os.NativeTombstoneManager.TombstoneFile> handleProtoTombstone(java.io.File path, boolean addToList) {
        java.lang.String filename = path.getName();
        if (!filename.endsWith(".pb")) {
            android.util.Slog.w(TAG, "unexpected tombstone name: " + path);
            return java.util.Optional.empty();
        }
        java.lang.String suffix = filename.substring("tombstone_".length());
        java.lang.String numberStr = suffix.substring(0, suffix.length() - 3);
        try {
            int number = java.lang.Integer.parseInt(numberStr);
            if (number < 0 || number > 99) {
                android.util.Slog.w(TAG, "unexpected tombstone name: " + path);
                return java.util.Optional.empty();
            }
            try {
                android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(path, 805306368);
                java.util.Optional<com.android.server.os.NativeTombstoneManager.TombstoneFile> parsedTombstone = com.android.server.os.NativeTombstoneManager.TombstoneFile.parse(pfd);
                if (!parsedTombstone.isPresent()) {
                    libcore.io.IoUtils.closeQuietly(pfd);
                    return java.util.Optional.empty();
                }
                if (addToList) {
                    synchronized (this.mLock) {
                        com.android.server.os.NativeTombstoneManager.TombstoneFile previous = this.mTombstones.get(number);
                        if (previous != null) {
                            previous.dispose();
                        }
                        this.mTombstones.put(number, parsedTombstone.get());
                    }
                }
                return parsedTombstone;
            } catch (java.io.FileNotFoundException ex) {
                android.util.Slog.w(TAG, "failed to open " + path, ex);
                return java.util.Optional.empty();
            }
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.w(TAG, "unexpected tombstone name: " + path);
            return java.util.Optional.empty();
        }
    }

    public void purge(final java.util.Optional<java.lang.Integer> userId, final java.util.Optional<java.lang.Integer> appId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.os.NativeTombstoneManager$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$purge$1(userId, appId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$purge$1(java.util.Optional userId, java.util.Optional appId) {
        synchronized (this.mLock) {
            for (int i = this.mTombstones.size() - 1; i >= 0; i--) {
                com.android.server.os.NativeTombstoneManager.TombstoneFile tombstone = this.mTombstones.valueAt(i);
                if (tombstone.matches(userId, appId)) {
                    tombstone.purge();
                    this.mTombstones.removeAt(i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgePackage(int uid, boolean allUsers) {
        java.util.Optional<java.lang.Integer> userId;
        int appId = android.os.UserHandle.getAppId(uid);
        if (allUsers) {
            userId = java.util.Optional.empty();
        } else {
            userId = java.util.Optional.of(java.lang.Integer.valueOf(android.os.UserHandle.getUserId(uid)));
        }
        purge(userId, java.util.Optional.of(java.lang.Integer.valueOf(appId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgeUser(int uid) {
        purge(java.util.Optional.of(java.lang.Integer.valueOf(uid)), java.util.Optional.empty());
    }

    private void registerForPackageRemoval() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        filter.addDataScheme("package");
        this.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.os.NativeTombstoneManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int uid = intent.getIntExtra("android.intent.extra.UID", -10000);
                if (uid == -10000) {
                    return;
                }
                boolean allUsers = intent.getBooleanExtra("android.intent.extra.REMOVED_FOR_ALL_USERS", false);
                com.android.server.os.NativeTombstoneManager.this.purgePackage(uid, allUsers);
            }
        }, filter, null, this.mHandler);
    }

    private void registerForUserRemoval() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.os.NativeTombstoneManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                if (userId < 1) {
                    return;
                }
                com.android.server.os.NativeTombstoneManager.this.purgeUser(userId);
            }
        }, filter, null, this.mHandler);
    }

    public void collectTombstones(final java.util.ArrayList<android.app.ApplicationExitInfo> output, int callingUid, final int pid, final int maxNum) {
        final java.util.concurrent.CompletableFuture<java.lang.Object> future = new java.util.concurrent.CompletableFuture<>();
        if (!android.os.UserHandle.isApp(callingUid)) {
            return;
        }
        final int userId = android.os.UserHandle.getUserId(callingUid);
        final int appId = android.os.UserHandle.getAppId(callingUid);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.os.NativeTombstoneManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$collectTombstones$3(userId, appId, pid, output, maxNum, future);
            }
        });
        try {
            future.get();
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collectTombstones$3(int userId, int appId, int pid, java.util.ArrayList output, int maxNum, java.util.concurrent.CompletableFuture future) {
        boolean appendedTombstones = false;
        synchronized (this.mLock) {
            int tombstonesSize = this.mTombstones.size();
            for (int i = 0; i < tombstonesSize; i++) {
                com.android.server.os.NativeTombstoneManager.TombstoneFile tombstone = this.mTombstones.valueAt(i);
                if (tombstone.matches(java.util.Optional.of(java.lang.Integer.valueOf(userId)), java.util.Optional.of(java.lang.Integer.valueOf(appId))) && (pid == 0 || tombstone.mPid == pid)) {
                    int outputSize = output.size();
                    int j = 0;
                    while (true) {
                        if (j < outputSize) {
                            android.app.ApplicationExitInfo exitInfo = (android.app.ApplicationExitInfo) output.get(j);
                            if (!tombstone.matches(exitInfo)) {
                                j++;
                            } else {
                                exitInfo.setNativeTombstoneRetriever(tombstone.getPfdRetriever());
                                break;
                            }
                        } else {
                            int j2 = output.size();
                            if (j2 < maxNum) {
                                appendedTombstones = true;
                                output.add(tombstone.toAppExitInfo());
                            }
                        }
                    }
                }
            }
        }
        if (appendedTombstones) {
            java.util.Collections.sort(output, new java.util.Comparator() { // from class: com.android.server.os.NativeTombstoneManager$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.os.NativeTombstoneManager.lambda$collectTombstones$2((android.app.ApplicationExitInfo) obj, (android.app.ApplicationExitInfo) obj2);
                }
            });
        }
        future.complete(null);
    }

    static /* synthetic */ int lambda$collectTombstones$2(android.app.ApplicationExitInfo lhs, android.app.ApplicationExitInfo rhs) {
        long diff = rhs.getTimestamp() - lhs.getTimestamp();
        if (diff < 0) {
            return -1;
        }
        if (diff == 0) {
            return 0;
        }
        return 1;
    }

    static class TombstoneFile {
        int mAppId;
        java.lang.String mCrashReason;
        final android.os.ParcelFileDescriptor mPfd;
        int mPid;
        java.lang.String mProcessName;
        boolean mPurged = false;
        final android.app.IParcelFileDescriptorRetriever mRetriever = new com.android.server.os.NativeTombstoneManager.TombstoneFile.ParcelFileDescriptorRetriever();
        long mTimestampMs;
        int mUid;
        int mUserId;

        TombstoneFile(android.os.ParcelFileDescriptor pfd) {
            this.mPfd = pfd;
        }

        public boolean matches(java.util.Optional<java.lang.Integer> userId, java.util.Optional<java.lang.Integer> appId) {
            if (this.mPurged) {
                return false;
            }
            if (!userId.isPresent() || userId.get().intValue() == this.mUserId) {
                return !appId.isPresent() || appId.get().intValue() == this.mAppId;
            }
            return false;
        }

        public boolean matches(android.app.ApplicationExitInfo exitInfo) {
            return exitInfo.getReason() == 5 && exitInfo.getPid() == this.mPid && exitInfo.getRealUid() == this.mUid && java.lang.Math.abs(exitInfo.getTimestamp() - this.mTimestampMs) <= 10000;
        }

        public java.lang.String getProcessName() {
            return this.mProcessName;
        }

        public void dispose() {
            libcore.io.IoUtils.closeQuietly(this.mPfd);
        }

        public void purge() {
            if (!this.mPurged) {
                try {
                    android.system.Os.ftruncate(this.mPfd.getFileDescriptor(), 0L);
                } catch (android.system.ErrnoException ex) {
                    android.util.Slog.e(com.android.server.os.NativeTombstoneManager.TAG, "Failed to truncate tombstone", ex);
                }
                this.mPurged = true;
            }
        }

        static java.util.Optional<com.android.server.os.NativeTombstoneManager.TombstoneFile> parse(android.os.ParcelFileDescriptor pfd) {
            java.io.FileInputStream is = new java.io.FileInputStream(pfd.getFileDescriptor());
            android.util.proto.ProtoInputStream stream = new android.util.proto.ProtoInputStream(is);
            java.lang.String selinuxLabel = "";
            java.lang.String crashReason = "";
            java.lang.String crashReason2 = null;
            int uid = 0;
            int uid2 = 0;
            while (true) {
                try {
                    int pid = stream.nextField();
                    if (pid != -1) {
                        switch (stream.getFieldNumber()) {
                            case 5:
                                uid2 = stream.readInt(1155346202629L);
                                break;
                            case 7:
                                uid = stream.readInt(1155346202631L);
                                break;
                            case 8:
                                selinuxLabel = stream.readString(1138166333448L);
                                break;
                            case 9:
                                if (crashReason2 == null) {
                                    crashReason2 = stream.readString(2237677961225L);
                                }
                                break;
                            case 15:
                                if (crashReason.equals("")) {
                                    long token = stream.start(2246267895823L);
                                    while (true) {
                                        if (stream.nextField() != -1) {
                                            switch (stream.getFieldNumber()) {
                                                case 1:
                                                    java.lang.String crashReason3 = stream.readString(1138166333441L);
                                                    crashReason = crashReason3;
                                                    break;
                                            }
                                        }
                                    }
                                    stream.end(token);
                                }
                                break;
                        }
                    } else {
                        if (!android.os.UserHandle.isApp(uid)) {
                            android.util.Slog.e(com.android.server.os.NativeTombstoneManager.TAG, "Tombstone's UID (" + uid + ") not an app, ignoring");
                            return java.util.Optional.empty();
                        }
                        long timestampMs = 0;
                        try {
                            android.system.StructStat stat = android.system.Os.fstat(pfd.getFileDescriptor());
                            timestampMs = (stat.st_atim.tv_sec * 1000) + (stat.st_atim.tv_nsec / 1000000);
                        } catch (android.system.ErrnoException ex) {
                            android.util.Slog.e(com.android.server.os.NativeTombstoneManager.TAG, "Failed to get timestamp of tombstone", ex);
                        }
                        int userId = android.os.UserHandle.getUserId(uid);
                        int appId = android.os.UserHandle.getAppId(uid);
                        if (!selinuxLabel.startsWith("u:r:untrusted_app")) {
                            android.util.Slog.e(com.android.server.os.NativeTombstoneManager.TAG, "Tombstone has invalid selinux label (" + selinuxLabel + "), ignoring");
                            return java.util.Optional.empty();
                        }
                        com.android.server.os.NativeTombstoneManager.TombstoneFile result = new com.android.server.os.NativeTombstoneManager.TombstoneFile(pfd);
                        result.mUserId = userId;
                        result.mAppId = appId;
                        result.mPid = uid2;
                        result.mUid = uid;
                        result.mProcessName = crashReason2 != null ? crashReason2 : "";
                        result.mTimestampMs = timestampMs;
                        result.mCrashReason = crashReason;
                        return java.util.Optional.of(result);
                    }
                } catch (java.io.IOException | android.util.proto.ProtoParseException ex2) {
                    android.util.Slog.e(com.android.server.os.NativeTombstoneManager.TAG, "Failed to parse tombstone", ex2);
                    return java.util.Optional.empty();
                }
            }
        }

        public android.app.IParcelFileDescriptorRetriever getPfdRetriever() {
            return this.mRetriever;
        }

        public android.app.ApplicationExitInfo toAppExitInfo() {
            android.app.ApplicationExitInfo info = new android.app.ApplicationExitInfo();
            info.setPid(this.mPid);
            info.setRealUid(this.mUid);
            info.setPackageUid(this.mUid);
            info.setDefiningUid(this.mUid);
            info.setProcessName(this.mProcessName);
            info.setReason(5);
            info.setStatus(0);
            info.setImportance(1000);
            info.setPackageName("");
            info.setProcessStateSummary(null);
            info.setPss(0L);
            info.setRss(0L);
            info.setTimestamp(this.mTimestampMs);
            info.setDescription(this.mCrashReason);
            info.setSubReason(0);
            info.setNativeTombstoneRetriever(this.mRetriever);
            return info;
        }

        class ParcelFileDescriptorRetriever extends android.app.IParcelFileDescriptorRetriever.Stub {
            ParcelFileDescriptorRetriever() {
            }

            public android.os.ParcelFileDescriptor getPfd() {
                if (com.android.server.os.NativeTombstoneManager.TombstoneFile.this.mPurged) {
                    return null;
                }
                try {
                    java.lang.String path = "/proc/self/fd/" + com.android.server.os.NativeTombstoneManager.TombstoneFile.this.mPfd.getFd();
                    android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(new java.io.File(path), 268435456);
                    return pfd;
                } catch (java.io.FileNotFoundException ex) {
                    android.util.Slog.e(com.android.server.os.NativeTombstoneManager.TAG, "failed to reopen file descriptor as read-only", ex);
                    return null;
                }
            }
        }
    }

    class TombstoneWatcher extends android.os.FileObserver {
        TombstoneWatcher() {
            super(com.android.server.os.NativeTombstoneManager.TOMBSTONE_DIR, com.android.internal.util.FrameworkStatsLog.NON_A11Y_TOOL_SERVICE_WARNING_REPORT);
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, final java.lang.String path) {
            if (path == null) {
                android.util.Slog.w(com.android.server.os.NativeTombstoneManager.TAG, "path is null at TombstoneWatcher.onEvent()");
            } else {
                com.android.server.os.NativeTombstoneManager.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.os.NativeTombstoneManager$TombstoneWatcher$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onEvent$0(path);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEvent$0(java.lang.String path) {
            if (path.endsWith(".tmp")) {
                return;
            }
            com.android.server.os.NativeTombstoneManager.this.handleTombstone(new java.io.File(com.android.server.os.NativeTombstoneManager.TOMBSTONE_DIR, path));
        }
    }
}
