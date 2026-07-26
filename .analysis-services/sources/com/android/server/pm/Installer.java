package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class Installer extends com.android.server.SystemService {
    private static final long CONNECT_RETRY_DELAY_MS = 1000;
    private static final long CONNECT_WAIT_MS = 10000;
    public static final int DEXOPT_BOOTCOMPLETE = 8;
    public static final int DEXOPT_DEBUGGABLE = 4;
    public static final int DEXOPT_ENABLE_HIDDEN_API_CHECKS = 1024;
    public static final int DEXOPT_FORCE = 64;
    public static final int DEXOPT_FOR_RESTORE = 8192;
    public static final int DEXOPT_GENERATE_APP_IMAGE = 4096;
    public static final int DEXOPT_GENERATE_COMPACT_DEX = 2048;
    public static final int DEXOPT_IDLE_BACKGROUND_JOB = 512;
    public static final int DEXOPT_PROFILE_GUIDED = 16;
    public static final int DEXOPT_PUBLIC = 2;
    public static final int DEXOPT_SECONDARY_DEX = 32;
    public static final int DEXOPT_STORAGE_CE = 128;
    public static final int DEXOPT_STORAGE_DE = 256;
    public static final int FLAG_CLEAR_APP_DATA_KEEP_ART_PROFILES = 131072;
    public static final int FLAG_CLEAR_CACHE_ONLY = 16;
    public static final int FLAG_CLEAR_CODE_CACHE_ONLY = 32;
    public static final int FLAG_FORCE = 8192;
    public static final int FLAG_FREE_CACHE_DEFY_TARGET_FREE_BYTES = 2048;
    public static final int FLAG_FREE_CACHE_NOOP = 1024;
    public static final int FLAG_FREE_CACHE_V2 = 256;
    public static final int FLAG_FREE_CACHE_V2_DEFY_QUOTA = 512;
    public static final int FLAG_STORAGE_CE = 2;
    public static final int FLAG_STORAGE_DE = 1;
    public static final int FLAG_STORAGE_EXTERNAL = 4;
    public static final int FLAG_STORAGE_SDK = 8;
    public static final int FLAG_USE_QUOTA = 4096;
    public static final int PROFILE_ANALYSIS_DONT_OPTIMIZE_EMPTY_PROFILES = 3;
    public static final int PROFILE_ANALYSIS_DONT_OPTIMIZE_SMALL_DELTA = 2;
    public static final int PROFILE_ANALYSIS_OPTIMIZE = 1;
    private static final java.lang.String TAG = "Installer";
    private volatile boolean mDeferSetFirstBoot;
    private volatile android.os.IInstalld mInstalld;
    private volatile java.util.concurrent.CountDownLatch mInstalldLatch;
    public com.android.server.pm.IInstallerExt mInstallerExt;
    private final boolean mIsolated;
    private volatile java.lang.Object mWarnIfHeld;

    public Installer(android.content.Context context) {
        this(context, false);
    }

    public Installer(android.content.Context context, boolean isolated) {
        super(context);
        this.mInstalld = null;
        this.mInstalldLatch = new java.util.concurrent.CountDownLatch(1);
        this.mInstallerExt = (com.android.server.pm.IInstallerExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IInstallerExt.class).base(this).create();
        this.mIsolated = isolated;
    }

    public void setWarnIfHeld(java.lang.Object warnIfHeld) {
        this.mWarnIfHeld = warnIfHeld;
    }

    public boolean isIsolated() {
        return this.mIsolated;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        if (this.mIsolated) {
            this.mInstalld = null;
            this.mInstalldLatch.countDown();
        } else {
            connect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connect() {
        android.os.IBinder binder = android.os.ServiceManager.getService("installd");
        if (binder != null) {
            try {
                binder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.pm.Installer$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        this.f$0.lambda$connect$0();
                    }
                }, 0);
            } catch (android.os.RemoteException e) {
                binder = null;
            }
        }
        if (binder != null) {
            android.os.IInstalld installd = android.os.IInstalld.Stub.asInterface(binder);
            this.mInstalld = installd;
            this.mInstalldLatch.countDown();
            try {
                invalidateMounts();
                executeDeferredActions();
                this.mInstallerExt.afterInstalldConnected(this, super.getContext());
                return;
            } catch (com.android.server.pm.Installer.InstallerException e2) {
                return;
            }
        }
        android.util.Slog.w(TAG, "installd not found; trying again");
        com.android.internal.os.BackgroundThread.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.Installer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.connect();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$connect$0() {
        android.util.Slog.w(TAG, "installd died; reconnecting");
        this.mInstalldLatch = new java.util.concurrent.CountDownLatch(1);
        connect();
    }

    private void executeDeferredActions() throws com.android.server.pm.Installer.InstallerException {
        if (this.mDeferSetFirstBoot) {
            setFirstBoot();
        }
    }

    private boolean checkBeforeRemote() throws com.android.server.pm.Installer.InstallerException {
        if (this.mWarnIfHeld != null && java.lang.Thread.holdsLock(this.mWarnIfHeld)) {
            android.util.Slog.wtf(TAG, "Calling thread " + java.lang.Thread.currentThread().getName() + " is holding 0x" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mWarnIfHeld)), new java.lang.Throwable());
        }
        if (this.mIsolated) {
            android.util.Slog.i(TAG, "Ignoring request because this installer is isolated");
            return false;
        }
        try {
            if (!this.mInstalldLatch.await(10000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                throw new com.android.server.pm.Installer.InstallerException("time out waiting for the installer to be ready");
            }
            return true;
        } catch (java.lang.InterruptedException e) {
            return true;
        }
    }

    static android.os.CreateAppDataArgs buildCreateAppDataArgs(java.lang.String uuid, java.lang.String packageName, int userId, int flags, int appId, java.lang.String seInfo, int targetSdkVersion, boolean usesSdk) {
        android.os.CreateAppDataArgs args = new android.os.CreateAppDataArgs();
        args.uuid = uuid;
        args.packageName = packageName;
        args.userId = userId;
        args.flags = flags;
        if (usesSdk) {
            args.flags |= 8;
        }
        args.appId = appId;
        args.seInfo = seInfo;
        args.targetSdkVersion = targetSdkVersion;
        return args;
    }

    private static android.os.CreateAppDataResult buildPlaceholderCreateAppDataResult() {
        android.os.CreateAppDataResult result = new android.os.CreateAppDataResult();
        result.ceDataInode = -1L;
        result.deDataInode = -1L;
        result.exceptionCode = 0;
        result.exceptionMessage = null;
        return result;
    }

    static android.os.ReconcileSdkDataArgs buildReconcileSdkDataArgs(java.lang.String uuid, java.lang.String packageName, java.util.List<java.lang.String> subDirNames, int userId, int appId, java.lang.String seInfo, int flags) {
        android.os.ReconcileSdkDataArgs args = new android.os.ReconcileSdkDataArgs();
        args.uuid = uuid;
        args.packageName = packageName;
        args.subDirNames = subDirNames;
        args.userId = userId;
        args.appId = appId;
        args.previousAppId = 0;
        args.seInfo = seInfo;
        args.flags = flags;
        return args;
    }

    public android.os.CreateAppDataResult createAppData(android.os.CreateAppDataArgs args) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return buildPlaceholderCreateAppDataResult();
        }
        args.previousAppId = 0;
        try {
            return this.mInstalld.createAppData(args);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public android.os.CreateAppDataResult[] createAppDataBatched(android.os.CreateAppDataArgs[] args) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            android.os.CreateAppDataResult[] results = new android.os.CreateAppDataResult[args.length];
            java.util.Arrays.fill(results, buildPlaceholderCreateAppDataResult());
            return results;
        }
        for (android.os.CreateAppDataArgs arg : args) {
            arg.previousAppId = 0;
        }
        try {
            return this.mInstalld.createAppDataBatched(args);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    void reconcileSdkData(android.os.ReconcileSdkDataArgs args) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return;
        }
        try {
            this.mInstalld.reconcileSdkData(args);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public void setFirstBoot() throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return;
        }
        try {
            if (this.mInstalld != null) {
                this.mInstalld.setFirstBoot();
            } else {
                this.mDeferSetFirstBoot = true;
            }
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public static class Batch {
        private static final int CREATE_APP_DATA_BATCH_SIZE = 256;
        private boolean mExecuted;
        private final java.util.List<android.os.CreateAppDataArgs> mArgs = new java.util.ArrayList();
        private final java.util.List<java.util.concurrent.CompletableFuture<android.os.CreateAppDataResult>> mFutures = new java.util.ArrayList();
        public com.android.server.pm.IBatchExt mBatchExt = (com.android.server.pm.IBatchExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IBatchExt.class).base(this).create();

        public synchronized java.util.concurrent.CompletableFuture<android.os.CreateAppDataResult> createAppData(android.os.CreateAppDataArgs args) {
            java.util.concurrent.CompletableFuture<android.os.CreateAppDataResult> future;
            if (this.mExecuted) {
                throw new java.lang.IllegalStateException();
            }
            future = new java.util.concurrent.CompletableFuture<>();
            this.mArgs.add(args);
            this.mFutures.add(future);
            return future;
        }

        public synchronized void execute(com.android.server.pm.Installer installer) throws com.android.server.pm.Installer.InstallerException {
            if (this.mExecuted) {
                throw new java.lang.IllegalStateException();
            }
            this.mExecuted = true;
            int size = this.mArgs.size();
            for (int i = 0; i < size; i += 256) {
                if (!this.mBatchExt.isAsyncJob() || this.mBatchExt.isUserRunningAndNotStopping()) {
                    android.os.CreateAppDataArgs[] args = new android.os.CreateAppDataArgs[java.lang.Math.min(size - i, 256)];
                    for (int j = 0; j < args.length; j++) {
                        args[j] = this.mArgs.get(i + j);
                    }
                    android.os.CreateAppDataResult[] results = installer.createAppDataBatched(args);
                    for (int j2 = 0; j2 < results.length; j2++) {
                        android.os.CreateAppDataResult result = results[j2];
                        java.util.concurrent.CompletableFuture<android.os.CreateAppDataResult> future = this.mFutures.get(i + j2);
                        if (result.exceptionCode == 0) {
                            future.complete(result);
                        } else {
                            future.completeExceptionally(new com.android.server.pm.Installer.InstallerException(result.exceptionMessage));
                        }
                    }
                }
            }
        }
    }

    public void migrateAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.migrateAppData(uuid, packageName, userId, flags);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void clearAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, long ceDataInode) throws com.android.server.pm.Installer.InstallerException {
        java.lang.StackTraceElement[] elements;
        int pid;
        int uid;
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.clearAppData(uuid, packageName, userId, flags, ceDataInode);
                elements = java.lang.Thread.currentThread().getStackTrace();
                pid = android.os.Binder.getCallingPid();
                uid = android.os.Binder.getCallingUid();
            } catch (java.lang.Exception e) {
                e = e;
            }
            try {
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.INSTALLER_CLEAR_APP_DATA_CALLER, java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid), packageName, java.lang.Integer.valueOf(flags));
                for (int i = 2; i < elements.length; i++) {
                    java.lang.String className = elements[i].getClassName();
                    java.lang.String methodName = elements[i].getMethodName();
                    java.lang.String fileName = elements[i].getFileName();
                    int lineNumber = elements[i].getLineNumber();
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.INSTALLER_CLEAR_APP_DATA_CALL_STACK, methodName, className, fileName, java.lang.Integer.valueOf(lineNumber));
                }
            } catch (java.lang.Exception e2) {
                e = e2;
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void destroyAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, long ceDataInode) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.destroyAppData(uuid, packageName, userId, flags, ceDataInode);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void fixupAppData(java.lang.String uuid, int flags) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.fixupAppData(uuid, flags);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void cleanupInvalidPackageDirs(java.lang.String uuid, int userId, int flags) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.cleanupInvalidPackageDirs(uuid, userId, flags);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void moveCompleteApp(java.lang.String fromUuid, java.lang.String toUuid, java.lang.String packageName, int appId, java.lang.String seInfo, int targetSdkVersion, java.lang.String fromCodePath) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.moveCompleteApp(fromUuid, toUuid, packageName, appId, seInfo, targetSdkVersion, fromCodePath);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void getAppSize(java.lang.String uuid, java.lang.String[] packageNames, int userId, int flags, int appId, long[] ceDataInodes, java.lang.String[] codePaths, android.content.pm.PackageStats stats) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            if (codePaths != null) {
                for (java.lang.String codePath : codePaths) {
                    dalvik.system.BlockGuard.getVmPolicy().onPathAccess(codePath);
                }
            }
            try {
                long[] res = this.mInstalld.getAppSize(uuid, packageNames, userId, flags, appId, ceDataInodes, codePaths);
                stats.codeSize += res[0];
                stats.dataSize += res[1];
                stats.cacheSize += res[2];
                stats.externalCodeSize += res[3];
                stats.externalDataSize += res[4];
                stats.externalCacheSize += res[5];
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void getUserSize(java.lang.String uuid, int userId, int flags, int[] appIds, android.content.pm.PackageStats stats) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                long[] res = this.mInstalld.getUserSize(uuid, userId, flags, appIds);
                stats.codeSize += res[0];
                stats.dataSize += res[1];
                stats.cacheSize += res[2];
                stats.externalCodeSize += res[3];
                stats.externalDataSize += res[4];
                stats.externalCacheSize += res[5];
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public long[] getExternalSize(java.lang.String uuid, int userId, int flags, int[] appIds) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return new long[6];
        }
        try {
            return this.mInstalld.getExternalSize(uuid, userId, flags, appIds);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public android.os.storage.CrateMetadata[] getAppCrates(java.lang.String uuid, java.lang.String[] packageNames, int userId) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return null;
        }
        try {
            return this.mInstalld.getAppCrates(uuid, packageNames, userId);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public android.os.storage.CrateMetadata[] getUserCrates(java.lang.String uuid, int userId) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return null;
        }
        try {
            return this.mInstalld.getUserCrates(uuid, userId);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public void setAppQuota(java.lang.String uuid, int userId, int appId, long cacheQuota) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.setAppQuota(uuid, userId, appId, cacheQuota);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public boolean dexopt(java.lang.String apkPath, int uid, java.lang.String pkgName, java.lang.String instructionSet, int dexoptNeeded, java.lang.String outputPath, int dexFlags, java.lang.String compilerFilter, java.lang.String volumeUuid, java.lang.String classLoaderContext, java.lang.String seInfo, boolean downgrade, int targetSdkVersion, java.lang.String profileName, java.lang.String dexMetadataPath, java.lang.String compilationReason) throws com.android.server.pm.Installer.LegacyDexoptDisabledException, com.android.server.pm.Installer.InstallerException {
        throw new com.android.server.pm.Installer.LegacyDexoptDisabledException();
    }

    public void rmPackageDir(java.lang.String packageName, java.lang.String packageDir) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            dalvik.system.BlockGuard.getVmPolicy().onPathAccess(packageDir);
            try {
                this.mInstallerExt.beforermPackageDir("rmPackageDir");
                this.mInstalld.rmPackageDir(packageName, packageDir);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void createUserData(java.lang.String uuid, int userId, int userSerial, int flags) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.createUserData(uuid, userId, userSerial, flags);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void destroyUserData(java.lang.String uuid, int userId, int flags) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.destroyUserData(uuid, userId, flags);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void freeCache(java.lang.String uuid, long targetFreeBytes, int flags) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.freeCache(uuid, targetFreeBytes, flags);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void linkNativeLibraryDirectory(java.lang.String uuid, java.lang.String packageName, java.lang.String nativeLibPath32, int userId) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            dalvik.system.BlockGuard.getVmPolicy().onPathAccess(nativeLibPath32);
            try {
                this.mInstalld.linkNativeLibraryDirectory(uuid, packageName, nativeLibPath32, userId);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void createOatDir(java.lang.String packageName, java.lang.String oatDir, java.lang.String dexInstructionSet) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.createOatDir(packageName, oatDir, dexInstructionSet);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void linkFile(java.lang.String packageName, java.lang.String relativePath, java.lang.String fromBase, java.lang.String toBase) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            dalvik.system.BlockGuard.getVmPolicy().onPathAccess(fromBase);
            dalvik.system.BlockGuard.getVmPolicy().onPathAccess(toBase);
            try {
                this.mInstalld.linkFile(packageName, relativePath, fromBase, toBase);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void moveAb(java.lang.String packageName, java.lang.String apkPath, java.lang.String instructionSet, java.lang.String outputPath) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            dalvik.system.BlockGuard.getVmPolicy().onPathAccess(apkPath);
            dalvik.system.BlockGuard.getVmPolicy().onPathAccess(outputPath);
            try {
                this.mInstalld.moveAb(packageName, apkPath, instructionSet, outputPath);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public byte[] hashSecondaryDexFile(java.lang.String dexPath, java.lang.String packageName, int uid, java.lang.String volumeUuid, int flags) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return new byte[0];
        }
        dalvik.system.BlockGuard.getVmPolicy().onPathAccess(dexPath);
        try {
            return this.mInstalld.hashSecondaryDexFile(dexPath, packageName, uid, volumeUuid, flags);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public void invalidateMounts() throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.invalidateMounts();
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public boolean isQuotaSupported(java.lang.String volumeUuid) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            return this.mInstalld.isQuotaSupported(volumeUuid);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public void tryMountDataMirror(java.lang.String volumeUuid) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.tryMountDataMirror(volumeUuid);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public void onPrivateVolumeRemoved(java.lang.String volumeUuid) throws com.android.server.pm.Installer.InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.onPrivateVolumeRemoved(volumeUuid);
            } catch (java.lang.Exception e) {
                throw com.android.server.pm.Installer.InstallerException.from(e);
            }
        }
    }

    public boolean snapshotAppData(java.lang.String pkg, int userId, int snapshotId, int storageFlags) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.snapshotAppData(null, pkg, userId, snapshotId, storageFlags);
            return true;
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public boolean restoreAppDataSnapshot(java.lang.String pkg, int appId, java.lang.String seInfo, int userId, int snapshotId, int storageFlags) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.restoreAppDataSnapshot(null, pkg, appId, seInfo, userId, snapshotId, storageFlags);
            return true;
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public boolean destroyAppDataSnapshot(java.lang.String pkg, int userId, int snapshotId, int storageFlags) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.destroyAppDataSnapshot(null, pkg, userId, 0L, snapshotId, storageFlags);
            return true;
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public boolean destroyCeSnapshotsNotSpecified(int userId, int[] retainSnapshotIds) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.destroyCeSnapshotsNotSpecified(null, userId, retainSnapshotIds);
            return true;
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public boolean migrateLegacyObbData() throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.migrateLegacyObbData();
            return true;
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    private static void assertValidInstructionSet(java.lang.String instructionSet) throws com.android.server.pm.Installer.InstallerException {
        int i = 0;
        if (android.os.Build.OPLUS_64BIT_ONLY_CHIP) {
            java.lang.String[] strArr = android.os.Build.MTK_HBT_SUPPORTED_ABIS;
            int length = strArr.length;
            while (i < length) {
                java.lang.String abi = strArr[i];
                if (!dalvik.system.VMRuntime.getInstructionSet(abi).equals(instructionSet)) {
                    i++;
                } else {
                    return;
                }
            }
        } else {
            java.lang.String[] strArr2 = android.os.Build.SUPPORTED_ABIS;
            int length2 = strArr2.length;
            while (i < length2) {
                java.lang.String abi2 = strArr2[i];
                if (!dalvik.system.VMRuntime.getInstructionSet(abi2).equals(instructionSet)) {
                    i++;
                } else {
                    return;
                }
            }
        }
        throw new com.android.server.pm.Installer.InstallerException("Invalid instruction set: " + instructionSet);
    }

    public android.os.IInstalld.IFsveritySetupAuthToken createFsveritySetupAuthToken(android.os.ParcelFileDescriptor authFd, int uid) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            return null;
        }
        try {
            return this.mInstalld.createFsveritySetupAuthToken(authFd, uid);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public int enableFsverity(android.os.IInstalld.IFsveritySetupAuthToken authToken, java.lang.String filePath, java.lang.String packageName) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            throw new com.android.server.pm.Installer.InstallerException("fs-verity wasn't enabled with an isolated installer");
        }
        dalvik.system.BlockGuard.getVmPolicy().onPathAccess(filePath);
        try {
            return this.mInstalld.enableFsverity(authToken, filePath, packageName);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public static class InstallerException extends java.lang.Exception {
        public InstallerException(java.lang.String detailMessage) {
            super(detailMessage);
        }

        public static com.android.server.pm.Installer.InstallerException from(java.lang.Exception e) throws com.android.server.pm.Installer.InstallerException {
            throw new com.android.server.pm.Installer.InstallerException(e.toString());
        }
    }

    public android.os.PersistableBundle oplusCommonInterface(java.lang.String func, android.os.PersistableBundle args) throws com.android.server.pm.Installer.InstallerException {
        if (!checkBeforeRemote()) {
            android.util.Slog.e(TAG, "oplusCommonInterface: no remote.");
            return null;
        }
        try {
            return this.mInstalld.oplusCommonInterface(func, args);
        } catch (java.lang.Exception e) {
            throw com.android.server.pm.Installer.InstallerException.from(e);
        }
    }

    public static class LegacyDexoptDisabledException extends java.lang.Exception {
        public LegacyDexoptDisabledException() {
            super("Invalid call to legacy dexopt method while ART Service is in use.");
        }
    }
}
