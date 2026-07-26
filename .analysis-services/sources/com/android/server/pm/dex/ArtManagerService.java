package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public class ArtManagerService extends android.content.pm.dex.IArtManager.Stub {
    private static final java.lang.String BOOT_IMAGE_ANDROID_PACKAGE = "android";
    private static final java.lang.String BOOT_IMAGE_PROFILE_NAME = "android.prof";
    public static final java.lang.String DEXOPT_REASON_WITH_DEX_METADATA_ANNOTATION = "-dm";
    private static final int TRON_COMPILATION_FILTER_ASSUMED_VERIFIED = 2;
    private static final int TRON_COMPILATION_FILTER_ASSUMED_VERIFIED_IORAP = 15;
    private static final int TRON_COMPILATION_FILTER_ERROR = 0;
    private static final int TRON_COMPILATION_FILTER_EVERYTHING = 11;
    private static final int TRON_COMPILATION_FILTER_EVERYTHING_IORAP = 24;
    private static final int TRON_COMPILATION_FILTER_EVERYTHING_PROFILE = 10;
    private static final int TRON_COMPILATION_FILTER_EVERYTHING_PROFILE_IORAP = 23;
    private static final int TRON_COMPILATION_FILTER_EXTRACT = 3;
    private static final int TRON_COMPILATION_FILTER_EXTRACT_IORAP = 16;
    private static final int TRON_COMPILATION_FILTER_FAKE_RUN_FROM_APK = 12;
    private static final int TRON_COMPILATION_FILTER_FAKE_RUN_FROM_APK_FALLBACK = 13;
    private static final int TRON_COMPILATION_FILTER_FAKE_RUN_FROM_APK_FALLBACK_IORAP = 26;
    private static final int TRON_COMPILATION_FILTER_FAKE_RUN_FROM_APK_IORAP = 25;
    private static final int TRON_COMPILATION_FILTER_FAKE_RUN_FROM_VDEX_FALLBACK = 14;
    private static final int TRON_COMPILATION_FILTER_FAKE_RUN_FROM_VDEX_FALLBACK_IORAP = 27;
    private static final int TRON_COMPILATION_FILTER_QUICKEN = 5;
    private static final int TRON_COMPILATION_FILTER_QUICKEN_IORAP = 18;
    private static final int TRON_COMPILATION_FILTER_SPACE = 7;
    private static final int TRON_COMPILATION_FILTER_SPACE_IORAP = 20;
    private static final int TRON_COMPILATION_FILTER_SPACE_PROFILE = 6;
    private static final int TRON_COMPILATION_FILTER_SPACE_PROFILE_IORAP = 19;
    private static final int TRON_COMPILATION_FILTER_SPEED = 9;
    private static final int TRON_COMPILATION_FILTER_SPEED_IORAP = 22;
    private static final int TRON_COMPILATION_FILTER_SPEED_PROFILE = 8;
    private static final int TRON_COMPILATION_FILTER_SPEED_PROFILE_IORAP = 21;
    private static final int TRON_COMPILATION_FILTER_UNKNOWN = 1;
    private static final int TRON_COMPILATION_FILTER_VERIFY = 4;
    private static final int TRON_COMPILATION_FILTER_VERIFY_IORAP = 17;
    private static final int TRON_COMPILATION_REASON_AB_OTA = 6;
    private static final int TRON_COMPILATION_REASON_BG_DEXOPT = 5;
    private static final int TRON_COMPILATION_REASON_BOOT_AFTER_MAINLINE_UPDATE = 25;
    private static final int TRON_COMPILATION_REASON_BOOT_AFTER_OTA = 20;
    private static final int TRON_COMPILATION_REASON_BOOT_DEPRECATED_SINCE_S = 3;
    private static final int TRON_COMPILATION_REASON_CMDLINE = 22;
    private static final int TRON_COMPILATION_REASON_ERROR = 0;
    private static final int TRON_COMPILATION_REASON_FIRST_BOOT = 2;
    private static final int TRON_COMPILATION_REASON_INACTIVE = 7;
    private static final int TRON_COMPILATION_REASON_INSTALL = 4;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK = 11;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK_DOWNGRADED = 13;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK_DOWNGRADED_WITH_DM = 18;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK_SECONDARY = 12;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK_SECONDARY_DOWNGRADED = 14;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK_SECONDARY_DOWNGRADED_WITH_DM = 19;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK_SECONDARY_WITH_DM = 17;
    private static final int TRON_COMPILATION_REASON_INSTALL_BULK_WITH_DM = 16;
    private static final int TRON_COMPILATION_REASON_INSTALL_FAST = 10;
    private static final int TRON_COMPILATION_REASON_INSTALL_FAST_WITH_DM = 15;
    private static final int TRON_COMPILATION_REASON_INSTALL_WITH_DM = 9;
    private static final int TRON_COMPILATION_REASON_POST_BOOT = 21;
    private static final int TRON_COMPILATION_REASON_PREBUILT = 23;
    private static final int TRON_COMPILATION_REASON_SHARED = 8;
    private static final int TRON_COMPILATION_REASON_UNKNOWN = 1;
    private static final int TRON_COMPILATION_REASON_VDEX = 24;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler = new android.os.Handler(com.android.internal.os.BackgroundThread.getHandler().getLooper());
    private final com.android.server.pm.Installer mInstaller;
    private android.content.pm.IPackageManager mPackageManager;
    private static final java.lang.String TAG = "ArtManagerService";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static com.android.server.pm.dex.IArtManagerServiceExt mArtExt = (com.android.server.pm.dex.IArtManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.dex.IArtManagerServiceExt.class).create();

    static {
        verifyTronLoggingConstants();
    }

    public ArtManagerService(android.content.Context context, com.android.server.pm.Installer installer, java.lang.Object ignored) {
        this.mContext = context;
        this.mInstaller = installer;
        com.android.server.LocalServices.addService(android.content.pm.dex.ArtManagerInternal.class, new com.android.server.pm.dex.ArtManagerService.ArtManagerInternalImpl());
    }

    private android.content.pm.IPackageManager getPackageManager() {
        if (this.mPackageManager == null) {
            this.mPackageManager = android.content.pm.IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
        }
        return this.mPackageManager;
    }

    private boolean checkAndroidPermissions(int callingUid, java.lang.String callingPackage) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_RUNTIME_PROFILES", TAG);
        switch (((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).noteOp(43, callingUid, callingPackage)) {
            case 0:
                break;
            case 3:
                this.mContext.enforceCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS", TAG);
                break;
        }
        return true;
    }

    private boolean checkShellPermissions(int profileType, java.lang.String packageName, int callingUid) {
        if (callingUid != 2000) {
            return false;
        }
        if (com.android.internal.os.RoSystemProperties.DEBUGGABLE) {
            return true;
        }
        if (profileType == 1) {
            return false;
        }
        android.content.pm.PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(packageName, 0L, 0);
        } catch (android.os.RemoteException e) {
        }
        return info != null && (info.applicationInfo.flags & 2) == 2;
    }

    public void snapshotRuntimeProfile(int profileType, java.lang.String packageName, java.lang.String codePath, android.content.pm.dex.ISnapshotRuntimeProfileCallback callback, java.lang.String callingPackage) {
        int callingUid = android.os.Binder.getCallingUid();
        if (!checkShellPermissions(profileType, packageName, callingUid) && !checkAndroidPermissions(callingUid, callingPackage)) {
            try {
                callback.onError(2);
                return;
            } catch (android.os.RemoteException e) {
                return;
            }
        }
        java.util.Objects.requireNonNull(callback);
        boolean bootImageProfile = profileType == 1;
        if (!bootImageProfile) {
            com.android.internal.util.Preconditions.checkStringNotEmpty(codePath);
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName);
        }
        if (!isRuntimeProfilingEnabled(profileType, callingPackage)) {
            throw new java.lang.IllegalStateException("Runtime profiling is not enabled for " + profileType);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Requested snapshot for " + packageName + ":" + codePath);
        }
        if (bootImageProfile) {
            snapshotBootImageProfile(callback);
        } else {
            snapshotAppProfile(packageName, codePath, callback);
        }
    }

    private void snapshotAppProfile(java.lang.String packageName, java.lang.String codePath, android.content.pm.dex.ISnapshotRuntimeProfileCallback callback) {
        android.content.pm.PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(packageName, 0L, 0);
        } catch (android.os.RemoteException e) {
        }
        if (info == null) {
            postError(callback, packageName, 0);
            return;
        }
        boolean pathFound = info.applicationInfo.getBaseCodePath().equals(codePath);
        java.lang.String splitName = null;
        java.lang.String[] splitCodePaths = info.applicationInfo.getSplitCodePaths();
        if (!pathFound && splitCodePaths != null) {
            int i = splitCodePaths.length - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                if (!splitCodePaths[i].equals(codePath)) {
                    i--;
                } else {
                    pathFound = true;
                    splitName = info.applicationInfo.splitNames[i];
                    break;
                }
            }
        }
        if (!pathFound) {
            postError(callback, packageName, 1);
            return;
        }
        try {
            try {
                com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
                try {
                    android.os.ParcelFileDescriptor fd = com.android.server.pm.DexOptHelper.getArtManagerLocal().snapshotAppProfile(snapshot, packageName, splitName);
                    if (snapshot != null) {
                        snapshot.close();
                    }
                    postSuccess(packageName, fd, callback);
                } catch (java.lang.Throwable th) {
                    if (snapshot != null) {
                        try {
                            snapshot.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.lang.IllegalArgumentException e2) {
                postError(callback, packageName, 0);
            }
        } catch (java.lang.IllegalStateException | com.android.server.art.ArtManagerLocal.SnapshotProfileException e3) {
            postError(callback, packageName, 2);
        }
    }

    public boolean isRuntimeProfilingEnabled(int profileType, java.lang.String callingPackage) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 2000 && !checkAndroidPermissions(callingUid, callingPackage)) {
            return false;
        }
        switch (profileType) {
            case 0:
                return true;
            case 1:
                boolean profileBootClassPath = android.os.SystemProperties.getBoolean("persist.device_config.runtime_native_boot.profilebootclasspath", android.os.SystemProperties.getBoolean("dalvik.vm.profilebootclasspath", false));
                return (android.os.Build.IS_USERDEBUG || android.os.Build.IS_ENG) && profileBootClassPath;
            default:
                throw new java.lang.IllegalArgumentException("Invalid profile type:" + profileType);
        }
    }

    private void snapshotBootImageProfile(android.content.pm.dex.ISnapshotRuntimeProfileCallback callback) {
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
            try {
                android.os.ParcelFileDescriptor fd = com.android.server.pm.DexOptHelper.getArtManagerLocal().snapshotBootImageProfile(snapshot);
                if (snapshot != null) {
                    snapshot.close();
                }
                postSuccess("android", fd, callback);
            } catch (java.lang.Throwable th) {
                if (snapshot != null) {
                    try {
                        snapshot.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.lang.IllegalStateException | com.android.server.art.ArtManagerLocal.SnapshotProfileException e) {
            postError(callback, "android", 2);
        }
    }

    private void postError(final android.content.pm.dex.ISnapshotRuntimeProfileCallback callback, final java.lang.String packageName, final int errCode) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Failed to snapshot profile for " + packageName + " with error: " + errCode);
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.dex.ArtManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.pm.dex.ArtManagerService.lambda$postError$0(callback, errCode, packageName);
            }
        });
    }

    static /* synthetic */ void lambda$postError$0(android.content.pm.dex.ISnapshotRuntimeProfileCallback callback, int errCode, java.lang.String packageName) {
        try {
            callback.onError(errCode);
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            android.util.Slog.w(TAG, "Failed to callback after profile snapshot for " + packageName, e);
        }
    }

    private void postSuccess(final java.lang.String packageName, final android.os.ParcelFileDescriptor fd, final android.content.pm.dex.ISnapshotRuntimeProfileCallback callback) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Successfully snapshot profile for " + packageName);
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.dex.ArtManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.pm.dex.ArtManagerService.lambda$postSuccess$1(fd, callback, packageName);
            }
        });
    }

    static /* synthetic */ void lambda$postSuccess$1(android.os.ParcelFileDescriptor fd, android.content.pm.dex.ISnapshotRuntimeProfileCallback callback, java.lang.String packageName) {
        try {
            try {
                if (fd.getFileDescriptor().valid()) {
                    callback.onSuccess(fd);
                } else {
                    android.util.Slog.wtf(TAG, "The snapshot FD became invalid before posting the result for " + packageName);
                    callback.onError(2);
                }
            } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                android.util.Slog.w(TAG, "Failed to call onSuccess after profile snapshot for " + packageName, e);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(fd);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:77:0x014e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getCompilationReasonTronValue(java.lang.String r24) {
        /*
            Method dump skipped, instruction units count: 528
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.dex.ArtManagerService.getCompilationReasonTronValue(java.lang.String):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0183  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getCompilationFilterTronValue(java.lang.String r24) {
        /*
            Method dump skipped, instruction units count: 606
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.dex.ArtManagerService.getCompilationFilterTronValue(java.lang.String):int");
    }

    private static void verifyTronLoggingConstants() {
        for (int i = 0; i < com.android.server.pm.PackageManagerServiceCompilerMapping.REASON_STRINGS.length; i++) {
            java.lang.String reason = com.android.server.pm.PackageManagerServiceCompilerMapping.REASON_STRINGS[i];
            int value = getCompilationReasonTronValue(reason);
            if (value == 0 || value == 1) {
                throw new java.lang.IllegalArgumentException("Compilation reason not configured for TRON logging: " + reason);
            }
        }
    }

    private class ArtManagerInternalImpl extends android.content.pm.dex.ArtManagerInternal {
        private static final java.lang.String IORAP_DIR = "/data/misc/iorapd";
        private static final java.lang.String TAG = "ArtManagerInternalImpl";

        private ArtManagerInternalImpl() {
        }

        public android.content.pm.dex.PackageOptimizationInfo getPackageOptimizationInfo(android.content.pm.ApplicationInfo info, java.lang.String abi, java.lang.String activityName) {
            java.lang.String compilationFilter;
            java.lang.String compilationReason;
            if (info.packageName.equals("android")) {
                return android.content.pm.dex.PackageOptimizationInfo.createWithNoInfo();
            }
            try {
                java.lang.String isa = dalvik.system.VMRuntime.getInstructionSet(abi);
                dalvik.system.DexFile.OptimizationInfo optInfo = dalvik.system.DexFile.getDexFileOptimizationInfo(info.getBaseCodePath(), isa);
                compilationFilter = optInfo.getStatus();
                compilationReason = optInfo.getReason();
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.e(TAG, "Could not get optimizations status for " + info.getBaseCodePath(), e);
                compilationFilter = "error";
                compilationReason = "error";
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.wtf(TAG, "Requested optimization status for " + info.getBaseCodePath() + " due to an invalid abi " + abi, e2);
                compilationFilter = "error";
                compilationReason = "error";
            }
            if (checkIorapCompiledTrace(info.packageName, activityName, info.longVersionCode)) {
                compilationFilter = compilationFilter + "-iorap";
            }
            int compilationFilterTronValue = com.android.server.pm.dex.ArtManagerService.getCompilationFilterTronValue(compilationFilter);
            int compilationReasonTronValue = com.android.server.pm.dex.ArtManagerService.getCompilationReasonTronValue(compilationReason);
            return new android.content.pm.dex.PackageOptimizationInfo(compilationFilterTronValue, compilationReasonTronValue);
        }

        private boolean checkIorapCompiledTrace(java.lang.String packageName, java.lang.String activityName, long version) {
            java.nio.file.Path tracePath = java.nio.file.Paths.get(IORAP_DIR, packageName, java.lang.Long.toString(version), activityName, "compiled_traces", "compiled_trace.pb");
            try {
                boolean exists = java.nio.file.Files.exists(tracePath, new java.nio.file.LinkOption[0]);
                if (com.android.server.pm.dex.ArtManagerService.DEBUG) {
                    android.util.Log.d(TAG, tracePath.toString() + (exists ? " exists" : " doesn't exist"));
                }
                if (exists) {
                    long bytes = java.nio.file.Files.size(tracePath);
                    if (com.android.server.pm.dex.ArtManagerService.DEBUG) {
                        android.util.Log.d(TAG, tracePath.toString() + " size is " + java.lang.Long.toString(bytes));
                    }
                    return bytes > 0;
                }
                return exists;
            } catch (java.io.IOException e) {
                android.util.Log.d(TAG, e.getMessage());
                return false;
            }
        }
    }
}
