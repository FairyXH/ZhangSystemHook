package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class OtaDexoptService extends android.content.pm.IOtaDexopt.Stub {
    private static final long BULK_DELETE_THRESHOLD = 1073741824;
    private static final boolean DEBUG_DEXOPT = true;
    private static final java.lang.String TAG = "OTADexopt";
    private long availableSpaceAfterBulkDelete;
    private long availableSpaceAfterDexopt;
    private long availableSpaceBefore;
    private int completeSize;
    private int dexoptCommandCountExecuted;
    private int dexoptCommandCountTotal;
    private int importantPackageCount;
    private final android.content.Context mContext;
    private java.util.List<java.lang.String> mDexoptCommands;
    private final com.android.server.pm.PackageManagerService mPackageManagerService;
    private final com.android.internal.logging.MetricsLogger metricsLogger = new com.android.internal.logging.MetricsLogger();
    private long otaDexoptTimeStart;
    private int otherPackageCount;

    public OtaDexoptService(android.content.Context context, com.android.server.pm.PackageManagerService packageManagerService) {
        this.mContext = context;
        this.mPackageManagerService = packageManagerService;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [android.os.IBinder, com.android.server.pm.OtaDexoptService] */
    public static com.android.server.pm.OtaDexoptService main(android.content.Context context, com.android.server.pm.PackageManagerService packageManagerService) {
        ?? otaDexoptService = new com.android.server.pm.OtaDexoptService(context, packageManagerService);
        android.os.ServiceManager.addService("otadexopt", (android.os.IBinder) otaDexoptService);
        otaDexoptService.moveAbArtifacts(packageManagerService.mInstaller);
        return otaDexoptService;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.pm.OtaDexoptShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    public synchronized void prepare() throws android.os.RemoteException {
        if (this.mDexoptCommands != null) {
            throw new java.lang.IllegalStateException("already called prepare()");
        }
        java.util.function.Predicate<? super com.android.server.pm.pkg.PackageStateInternal> isPlatformPackage = new java.util.function.Predicate() { // from class: com.android.server.pm.OtaDexoptService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(((com.android.server.pm.pkg.PackageStateInternal) obj).getPkg().getPackageName());
            }
        };
        com.android.server.pm.Computer snapshot = this.mPackageManagerService.snapshotComputer();
        java.util.Collection<? extends com.android.server.pm.pkg.PackageStateInternal> allPackageStates = snapshot.getPackageStates().values();
        java.util.List<com.android.server.pm.pkg.PackageStateInternal> important = com.android.server.pm.DexOptHelper.getPackagesForDexopt(allPackageStates, this.mPackageManagerService, true);
        important.removeIf(isPlatformPackage);
        java.util.List<com.android.server.pm.pkg.PackageStateInternal> others = new java.util.ArrayList<>(allPackageStates);
        others.removeAll(important);
        others.removeIf(com.android.server.pm.PackageManagerServiceUtils.REMOVE_IF_NULL_PKG);
        others.removeIf(com.android.server.pm.PackageManagerServiceUtils.REMOVE_IF_APEX_PKG);
        others.removeIf(isPlatformPackage);
        this.mDexoptCommands = new java.util.ArrayList((allPackageStates.size() * 3) / 2);
        for (com.android.server.pm.pkg.PackageStateInternal pkgSetting : important) {
            this.mDexoptCommands.addAll(generatePackageDexopts(pkgSetting.getPkg(), pkgSetting, 10));
        }
        for (com.android.server.pm.pkg.PackageStateInternal pkgSetting2 : others) {
            if (pkgSetting2.getPkg().isCoreApp()) {
                throw new java.lang.IllegalStateException("Found a core app that's not important");
            }
            this.mDexoptCommands.addAll(generatePackageDexopts(pkgSetting2.getPkg(), pkgSetting2, 0));
        }
        this.completeSize = this.mDexoptCommands.size();
        long spaceAvailable = getAvailableSpace();
        if (spaceAvailable < BULK_DELETE_THRESHOLD) {
            android.util.Log.i(TAG, "Low on space, deleting oat files in an attempt to free up space: " + com.android.server.pm.DexOptHelper.packagesToString(others));
            for (com.android.server.pm.pkg.PackageStateInternal pkg : others) {
                this.mPackageManagerService.deleteOatArtifactsOfPackage(snapshot, pkg.getPackageName());
            }
        }
        long spaceAvailableNow = getAvailableSpace();
        prepareMetricsLogging(important.size(), others.size(), spaceAvailable, spaceAvailableNow);
        try {
            com.android.server.pm.pkg.PackageStateInternal lastUsed = (com.android.server.pm.pkg.PackageStateInternal) java.util.Collections.max(important, java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.pm.OtaDexoptService$$ExternalSyntheticLambda1
                @Override // java.util.function.ToLongFunction
                public final long applyAsLong(java.lang.Object obj) {
                    return ((com.android.server.pm.pkg.PackageStateInternal) obj).getTransientState().getLatestForegroundPackageUseTimeInMills();
                }
            }));
            android.util.Log.d(TAG, "A/B OTA: lastUsed time = " + lastUsed.getTransientState().getLatestForegroundPackageUseTimeInMills());
            android.util.Log.d(TAG, "A/B OTA: deprioritized packages:");
            java.util.Iterator<com.android.server.pm.pkg.PackageStateInternal> it = others.iterator();
            while (it.hasNext()) {
                com.android.server.pm.pkg.PackageStateInternal pkgSetting3 = it.next();
                com.android.server.pm.pkg.PackageStateInternal lastUsed2 = lastUsed;
                java.util.Iterator<com.android.server.pm.pkg.PackageStateInternal> it2 = it;
                android.util.Log.d(TAG, "  " + pkgSetting3.getPackageName() + " - " + pkgSetting3.getTransientState().getLatestForegroundPackageUseTimeInMills());
                lastUsed = lastUsed2;
                it = it2;
            }
        } catch (java.lang.RuntimeException e) {
        }
    }

    public synchronized void cleanup() throws android.os.RemoteException {
        android.util.Log.i(TAG, "Cleaning up OTA Dexopt state.");
        this.mDexoptCommands = null;
        this.availableSpaceAfterDexopt = getAvailableSpace();
        performMetricsLogging();
    }

    public synchronized boolean isDone() throws android.os.RemoteException {
        if (this.mDexoptCommands == null) {
            throw new java.lang.IllegalStateException("done() called before prepare()");
        }
        return this.mDexoptCommands.isEmpty();
    }

    public synchronized float getProgress() throws android.os.RemoteException {
        if (this.completeSize == 0) {
            return 1.0f;
        }
        int commandsLeft = this.mDexoptCommands.size();
        return (this.completeSize - commandsLeft) / this.completeSize;
    }

    public synchronized java.lang.String nextDexoptCommand() throws android.os.RemoteException {
        if (this.mDexoptCommands == null) {
            throw new java.lang.IllegalStateException("dexoptNextPackage() called before prepare()");
        }
        if (this.mDexoptCommands.isEmpty()) {
            return "(all done)";
        }
        java.lang.String next = this.mDexoptCommands.remove(0);
        if (getAvailableSpace() > 0) {
            this.dexoptCommandCountExecuted++;
            android.util.Log.d(TAG, "Next command: " + next);
            return next;
        }
        android.util.Log.w(TAG, "Not enough space for OTA dexopt, stopping with " + (this.mDexoptCommands.size() + 1) + " commands left.");
        this.mDexoptCommands.clear();
        return "(no free space)";
    }

    private long getMainLowSpaceThreshold() {
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        long lowThreshold = android.os.storage.StorageManager.from(this.mContext).getStorageLowBytes(dataDir);
        if (lowThreshold == 0) {
            throw new java.lang.IllegalStateException("Invalid low memory threshold");
        }
        return lowThreshold;
    }

    private long getAvailableSpace() {
        long lowThreshold = getMainLowSpaceThreshold();
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        long usableSpace = dataDir.getUsableSpace();
        return usableSpace - lowThreshold;
    }

    private synchronized java.util.List<java.lang.String> generatePackageDexopts(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int compilationReason) {
        final java.util.List<java.lang.String> commands;
        commands = new java.util.ArrayList<>();
        com.android.server.pm.Installer collectingInstaller = new com.android.server.pm.Installer(this.mContext, true) { // from class: com.android.server.pm.OtaDexoptService.1
            @Override // com.android.server.pm.Installer
            public boolean dexopt(java.lang.String apkPath, int uid, java.lang.String pkgName, java.lang.String instructionSet, int dexoptNeeded, java.lang.String outputPath, int dexFlags, java.lang.String compilerFilter, java.lang.String volumeUuid, java.lang.String sharedLibraries, java.lang.String seInfo, boolean downgrade, int targetSdkVersion, java.lang.String profileName, java.lang.String dexMetadataPath, java.lang.String dexoptCompilationReason) throws com.android.server.pm.Installer.InstallerException {
                java.lang.StringBuilder builder = new java.lang.StringBuilder();
                if ((dexFlags & 32) != 0) {
                    throw new java.lang.IllegalArgumentException("Invalid OTA dexopt call for secondary dex");
                }
                builder.append("10 ");
                builder.append("dexopt");
                encodeParameter(builder, apkPath);
                encodeParameter(builder, java.lang.Integer.valueOf(uid));
                encodeParameter(builder, pkgName);
                encodeParameter(builder, instructionSet);
                encodeParameter(builder, java.lang.Integer.valueOf(dexoptNeeded));
                encodeParameter(builder, outputPath);
                encodeParameter(builder, java.lang.Integer.valueOf(dexFlags));
                encodeParameter(builder, compilerFilter);
                encodeParameter(builder, volumeUuid);
                encodeParameter(builder, sharedLibraries);
                encodeParameter(builder, seInfo);
                encodeParameter(builder, java.lang.Boolean.valueOf(downgrade));
                encodeParameter(builder, java.lang.Integer.valueOf(targetSdkVersion));
                encodeParameter(builder, profileName);
                encodeParameter(builder, dexMetadataPath);
                encodeParameter(builder, dexoptCompilationReason);
                commands.add(builder.toString());
                return true;
            }

            private void encodeParameter(java.lang.StringBuilder builder, java.lang.Object arg) {
                builder.append(' ');
                if (arg == null) {
                    builder.append('!');
                    return;
                }
                java.lang.String txt = java.lang.String.valueOf(arg);
                if (txt.indexOf(0) != -1 || txt.indexOf(32) != -1 || "!".equals(txt)) {
                    throw new java.lang.IllegalArgumentException("Invalid argument while executing " + arg);
                }
                builder.append(txt);
            }
        };
        com.android.server.pm.PackageDexOptimizer optimizer = new com.android.server.pm.OtaDexoptService.OTADexoptPackageDexOptimizer(collectingInstaller, this.mPackageManagerService.mInstallLock, this.mContext);
        try {
            optimizer.performDexOpt(pkg, pkgSetting, null, null, this.mPackageManagerService.getDexManager().getPackageUseInfoOrDefault(pkg.getPackageName()), new com.android.server.pm.dex.DexoptOptions(pkg.getPackageName(), compilationReason, 4));
        } catch (com.android.server.pm.Installer.LegacyDexoptDisabledException e) {
            android.util.Slog.wtf(TAG, e);
        }
        return commands;
    }

    public synchronized void dexoptNextPackage() throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException();
    }

    private void moveAbArtifacts(com.android.server.pm.Installer installer) {
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates;
        com.android.server.pm.OtaDexoptService otaDexoptService = this;
        if (otaDexoptService.mDexoptCommands != null) {
            throw new java.lang.IllegalStateException("Should not be ota-dexopting when trying to move.");
        }
        if (!otaDexoptService.mPackageManagerService.isDeviceUpgrading()) {
            android.util.Slog.d(TAG, "No upgrade, skipping A/B artifacts check.");
            return;
        }
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates2 = ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getPackageStates();
        int packagePaths = 0;
        int pathsSuccessful = 0;
        int index = 0;
        while (index < packageStates2.size()) {
            com.android.server.pm.pkg.PackageStateInternal packageState = packageStates2.valueAt(index);
            com.android.server.pm.pkg.AndroidPackage pkg = packageState.getPkg();
            if (pkg == null) {
                packageStates = packageStates2;
            } else if (!otaDexoptService.mPackageManagerService.mPackageDexOptimizer.canOptimizePackage(pkg)) {
                packageStates = packageStates2;
            } else if (pkg.getPath() == null) {
                android.util.Slog.w(TAG, "Package " + pkg + " can be optimized but has null codePath");
                packageStates = packageStates2;
            } else if (pkg.getPath().startsWith("/system")) {
                packageStates = packageStates2;
            } else if (pkg.getPath().startsWith("/vendor")) {
                packageStates = packageStates2;
            } else if (pkg.getPath().startsWith("/product")) {
                packageStates = packageStates2;
            } else if (pkg.getPath().startsWith("/system_ext")) {
                packageStates = packageStates2;
            } else {
                java.lang.String[] instructionSets = com.android.server.pm.InstructionSets.getAppDexInstructionSets(packageState.getPrimaryCpuAbi(), packageState.getSecondaryCpuAbi());
                java.util.List<java.lang.String> paths = com.android.server.pm.parsing.pkg.AndroidPackageUtils.getAllCodePathsExcludingResourceOnly(pkg);
                java.lang.String[] dexCodeInstructionSets = com.android.server.pm.InstructionSets.getDexCodeInstructionSets(instructionSets);
                java.lang.String packageName = pkg.getPackageName();
                int length = dexCodeInstructionSets.length;
                int i = 0;
                while (i < length) {
                    java.lang.String dexCodeInstructionSet = dexCodeInstructionSets[i];
                    for (java.lang.String path : paths) {
                        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates3 = packageStates2;
                        com.android.server.pm.pkg.PackageStateInternal packageState2 = packageState;
                        java.lang.String oatDir = com.android.server.pm.PackageDexOptimizer.getOatDir(new java.io.File(pkg.getPath())).getAbsolutePath();
                        int packagePaths2 = packagePaths + 1;
                        try {
                            installer.moveAb(packageName, path, dexCodeInstructionSet, oatDir);
                            pathsSuccessful++;
                        } catch (com.android.server.pm.Installer.InstallerException e) {
                        }
                        packageStates2 = packageStates3;
                        packageState = packageState2;
                        packagePaths = packagePaths2;
                    }
                    i++;
                    packageState = packageState;
                }
                packageStates = packageStates2;
            }
            index++;
            otaDexoptService = this;
            packageStates2 = packageStates;
        }
        android.util.Slog.i(TAG, "Moved " + pathsSuccessful + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packagePaths);
    }

    private void prepareMetricsLogging(int important, int others, long spaceBegin, long spaceBulk) {
        this.availableSpaceBefore = spaceBegin;
        this.availableSpaceAfterBulkDelete = spaceBulk;
        this.availableSpaceAfterDexopt = 0L;
        this.importantPackageCount = important;
        this.otherPackageCount = others;
        this.dexoptCommandCountTotal = this.mDexoptCommands.size();
        this.dexoptCommandCountExecuted = 0;
        this.otaDexoptTimeStart = java.lang.System.nanoTime();
    }

    private static int inMegabytes(long value) {
        long in_mega_bytes = value / 1048576;
        if (in_mega_bytes > 2147483647L) {
            android.util.Log.w(TAG, "Recording " + in_mega_bytes + "MB of free space, overflowing range");
            return Integer.MAX_VALUE;
        }
        return (int) in_mega_bytes;
    }

    private void performMetricsLogging() {
        long finalTime = java.lang.System.nanoTime();
        this.metricsLogger.histogram("ota_dexopt_available_space_before_mb", inMegabytes(this.availableSpaceBefore));
        this.metricsLogger.histogram("ota_dexopt_available_space_after_bulk_delete_mb", inMegabytes(this.availableSpaceAfterBulkDelete));
        this.metricsLogger.histogram("ota_dexopt_available_space_after_dexopt_mb", inMegabytes(this.availableSpaceAfterDexopt));
        this.metricsLogger.histogram("ota_dexopt_num_important_packages", this.importantPackageCount);
        this.metricsLogger.histogram("ota_dexopt_num_other_packages", this.otherPackageCount);
        this.metricsLogger.histogram("ota_dexopt_num_commands", this.dexoptCommandCountTotal);
        this.metricsLogger.histogram("ota_dexopt_num_commands_executed", this.dexoptCommandCountExecuted);
        int elapsedTimeSeconds = (int) java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(finalTime - this.otaDexoptTimeStart);
        this.metricsLogger.histogram("ota_dexopt_time_s", elapsedTimeSeconds);
    }

    private static class OTADexoptPackageDexOptimizer extends com.android.server.pm.PackageDexOptimizer.ForcedUpdatePackageDexOptimizer {
        OTADexoptPackageDexOptimizer(com.android.server.pm.Installer installer, com.android.server.pm.PackageManagerTracedLock installLock, android.content.Context context) {
            super(installer, installLock, context, "*otadexopt*");
        }
    }
}
