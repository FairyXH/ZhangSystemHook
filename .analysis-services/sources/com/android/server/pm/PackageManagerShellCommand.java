package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PackageManagerShellCommand extends android.os.ShellCommand {
    private static final java.lang.String ART_PROFILE_SNAPSHOT_DEBUG_LOCATION = "/data/misc/profman/";
    private static final java.util.Set<java.lang.String> ART_SERVICE_COMMANDS;
    private static final int DEFAULT_STAGED_READY_TIMEOUT_MS = 60000;
    private static final java.security.SecureRandom RANDOM;
    private static final java.lang.String STDIN_PATH = "-";
    private static final java.lang.String TAG = "PackageManagerShellCommand";
    boolean mBrief;
    boolean mComponents;
    final android.content.Context mContext;
    final com.android.server.pm.verify.domain.DomainVerificationShell mDomainVerificationShell;
    final android.content.pm.IPackageManager mInterface;
    final android.permission.PermissionManager mPermissionManager;
    int mQueryFlags;
    int mTargetUser;
    private static final java.util.Set<java.lang.String> UNSUPPORTED_INSTALL_CMD_OPTS = java.util.Set.of("--multi-package");
    private static final java.util.Set<java.lang.String> UNSUPPORTED_SESSION_CREATE_OPTS = java.util.Collections.emptySet();
    private static final java.util.Map<java.lang.String, java.lang.Integer> SUPPORTED_PERMISSION_FLAGS = new android.util.ArrayMap();
    private static final java.util.List<java.lang.String> SUPPORTED_PERMISSION_FLAGS_LIST = java.util.List.of("review-required", "revoked-compat", "revoke-when-requested", "user-fixed", "user-set");
    private final java.util.WeakHashMap<java.lang.String, android.content.res.Resources> mResourceCache = new java.util.WeakHashMap<>();
    com.android.server.pm.IPackageManagerShellCommandExt mPackageManagerScExt = (com.android.server.pm.IPackageManagerShellCommandExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerShellCommandExt.class).base(this).create();
    private final android.content.pm.PackageManagerInternal mPm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
    final com.android.server.pm.permission.LegacyPermissionManagerInternal mLegacyPermissionManager = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);

    static {
        SUPPORTED_PERMISSION_FLAGS.put("user-set", 1);
        SUPPORTED_PERMISSION_FLAGS.put("user-fixed", 2);
        SUPPORTED_PERMISSION_FLAGS.put("revoked-compat", 8);
        SUPPORTED_PERMISSION_FLAGS.put("review-required", 64);
        SUPPORTED_PERMISSION_FLAGS.put("revoke-when-requested", 128);
        ART_SERVICE_COMMANDS = java.util.Set.of("compile", "reconcile-secondary-dex-files", "force-dex-opt", "bg-dexopt-job", "cancel-bg-dexopt-job", "delete-dexopt", "dump-profiles", "snapshot-profile", "art");
        RANDOM = new java.security.SecureRandom();
    }

    PackageManagerShellCommand(android.content.pm.IPackageManager packageManager, android.content.Context context, com.android.server.pm.verify.domain.DomainVerificationShell domainVerificationShell) {
        this.mInterface = packageManager;
        this.mPermissionManager = (android.permission.PermissionManager) context.getSystemService(android.permission.PermissionManager.class);
        this.mContext = context;
        this.mDomainVerificationShell = domainVerificationShell;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0018  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r9) {
        /*
            Method dump skipped, instruction units count: 2218
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.onCommand(java.lang.String):int");
    }

    private int runGetModuleInfo() {
        java.io.PrintWriter pw = getOutPrintWriter();
        int flags = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 42995713:
                        b = opt.equals("--all") ? (byte) 0 : (byte) -1;
                        break;
                    case 517440986:
                        if (!opt.equals("--installed")) {
                            b = -1;
                        }
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        flags |= 131072;
                        break;
                    case 1:
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return -1;
                }
            } else {
                java.lang.String moduleName = getNextArg();
                try {
                    if (moduleName != null) {
                        android.content.pm.ModuleInfo m = this.mInterface.getModuleInfo(moduleName, flags);
                        pw.println(m.toString() + " packageName: " + m.getPackageName());
                    } else {
                        java.util.List<android.content.pm.ModuleInfo> modules = this.mInterface.getInstalledModules(flags);
                        for (android.content.pm.ModuleInfo m2 : modules) {
                            pw.println(m2.toString() + " packageName: " + m2.getPackageName());
                        }
                    }
                    return 1;
                } catch (android.os.RemoteException e) {
                    pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
                    return -1;
                }
            }
        }
    }

    private int runLogVisibility() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean enable = true;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -1237677752:
                        b = !opt.equals("--disable") ? (byte) -1 : (byte) 0;
                        break;
                    case 1101165347:
                        if (!opt.equals("--enable")) {
                            b = -1;
                        }
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        enable = false;
                        break;
                    case 1:
                        enable = true;
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return -1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                if (packageName != null) {
                    ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).setVisibilityLogging(packageName, enable);
                    return 1;
                }
                getErrPrintWriter().println("Error: no package specified");
                return -1;
            }
        }
    }

    private int runBypassStagedInstallerCheck() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            this.mInterface.getPackageInstaller().bypassNextStagedInstallerCheck(java.lang.Boolean.parseBoolean(getNextArg()));
            return 0;
        } catch (android.os.RemoteException e) {
            pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
            return -1;
        }
    }

    private int runBypassAllowedApexUpdateCheck() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            this.mInterface.getPackageInstaller().bypassNextAllowedApexUpdateCheck(java.lang.Boolean.parseBoolean(getNextArg()));
            return 0;
        } catch (android.os.RemoteException e) {
            pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
            return -1;
        }
    }

    private int runDisableVerificationForUid() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            int uid = java.lang.Integer.parseInt(getNextArgRequired());
            android.app.ActivityManagerInternal amInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            boolean isInstrumented = amInternal.getInstrumentationSourceUid(uid) != -1;
            if (isInstrumented) {
                this.mInterface.getPackageInstaller().disableVerificationForUid(uid);
                return 0;
            }
            pw.println("Error: must specify an instrumented uid");
            return -1;
        } catch (android.os.RemoteException e) {
            pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
            return -1;
        }
    }

    private int uninstallSystemUpdates(java.lang.String packageName) {
        java.util.List<android.content.pm.ApplicationInfo> list;
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean failedUninstalls = false;
        try {
            android.content.pm.IPackageInstaller installer = this.mInterface.getPackageInstaller();
            if (packageName == null) {
                android.content.pm.ParceledListSlice<android.content.pm.ApplicationInfo> packages = this.mInterface.getInstalledApplications(1056768L, 0);
                list = packages.getList();
            } else {
                java.util.List<android.content.pm.ApplicationInfo> list2 = new java.util.ArrayList<>(1);
                list2.add(this.mInterface.getApplicationInfo(packageName, 1056768L, 0));
                list = list2;
            }
            for (android.content.pm.ApplicationInfo info : list) {
                if (info.isUpdatedSystemApp()) {
                    pw.println("Uninstalling updates to " + info.packageName + "...");
                    com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver receiver = new com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver();
                    installer.uninstall(new android.content.pm.VersionedPackage(info.packageName, info.versionCode), (java.lang.String) null, 0, receiver.getIntentSender(), 0);
                    android.content.Intent result = receiver.getResult();
                    int status = result.getIntExtra("android.content.pm.extra.STATUS", 1);
                    if (status != 0) {
                        failedUninstalls = true;
                        pw.println("Couldn't uninstall package: " + info.packageName);
                    }
                }
            }
            if (failedUninstalls) {
                return 0;
            }
            pw.println("Success");
            return 1;
        } catch (android.os.RemoteException e) {
            pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
            return 0;
        }
    }

    private int runRollbackApp() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        long stagedReadyTimeoutMs = 60000;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -158482320:
                        if (!opt.equals("--staged-ready-timeout")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        stagedReadyTimeoutMs = java.lang.Long.parseLong(getNextArgRequired());
                        break;
                    default:
                        throw new java.lang.IllegalArgumentException("Unknown option: " + opt);
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                if (packageName == null) {
                    pw.println("Error: package name not specified");
                    return 1;
                }
                try {
                    android.content.Context shellPackageContext = this.mContext.createPackageContextAsUser("com.android.shell", 0, android.os.Binder.getCallingUserHandle());
                    com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver receiver = new com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver();
                    android.content.rollback.RollbackManager rm = (android.content.rollback.RollbackManager) shellPackageContext.getSystemService(android.content.rollback.RollbackManager.class);
                    android.content.rollback.RollbackInfo rollback = null;
                    for (android.content.rollback.RollbackInfo r : rm.getAvailableRollbacks()) {
                        java.util.Iterator it = r.getPackages().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                android.content.rollback.PackageRollbackInfo info = (android.content.rollback.PackageRollbackInfo) it.next();
                                if (packageName.equals(info.getPackageName())) {
                                    rollback = r;
                                }
                            }
                        }
                    }
                    if (rollback == null) {
                        pw.println("No available rollbacks for: " + packageName);
                        return 1;
                    }
                    rm.commitRollback(rollback.getRollbackId(), java.util.Collections.emptyList(), receiver.getIntentSender());
                    android.content.Intent result = receiver.getResult();
                    int status = result.getIntExtra("android.content.rollback.extra.STATUS", 1);
                    if (status != 0) {
                        pw.println("Failure [" + result.getStringExtra("android.content.rollback.extra.STATUS_MESSAGE") + "]");
                        return 1;
                    }
                    if (rollback.isStaged() && stagedReadyTimeoutMs > 0) {
                        int committedSessionId = rollback.getCommittedSessionId();
                        return doWaitForStagedSessionReady(committedSessionId, stagedReadyTimeoutMs, pw);
                    }
                    pw.println("Success");
                    return 0;
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    throw new java.lang.RuntimeException(e);
                }
            }
        }
    }

    private void setParamsSize(com.android.server.pm.PackageManagerShellCommand.InstallParams params, java.util.List<java.lang.String> inPaths) {
        if (params.sessionParams.sizeBytes == -1 && !STDIN_PATH.equals(inPaths.get(0))) {
            long sessionSize = 0;
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            for (java.lang.String inPath : inPaths) {
                android.os.ParcelFileDescriptor fd = openFileForSystem(inPath, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
                if (fd == null) {
                    getErrPrintWriter().println("Error: Can't open file: " + inPath);
                    throw new java.lang.IllegalArgumentException("Error: Can't open file: " + inPath);
                }
                try {
                    try {
                        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ApkLite> apkLiteResult = android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input.reset(), fd.getFileDescriptor(), inPath, 0);
                        if (apkLiteResult.isError()) {
                            throw new java.lang.IllegalArgumentException("Error: Failed to parse APK file: " + inPath + ": " + apkLiteResult.getErrorMessage(), apkLiteResult.getException());
                        }
                        android.content.pm.parsing.ApkLite apkLite = (android.content.pm.parsing.ApkLite) apkLiteResult.getResult();
                        android.content.pm.parsing.PackageLite pkgLite = new android.content.pm.parsing.PackageLite((java.lang.String) null, apkLite.getPath(), apkLite, (java.lang.String[]) null, (boolean[]) null, (java.lang.String[]) null, (java.lang.String[]) null, (java.lang.String[]) null, (int[]) null, apkLite.getTargetSdkVersion(), (java.util.Set[]) null, (java.util.Set[]) null);
                        sessionSize += com.android.internal.content.InstallLocationUtils.calculateInstalledSize(pkgLite, params.sessionParams.abiOverride, fd.getFileDescriptor());
                        try {
                            fd.close();
                        } catch (java.io.IOException e) {
                        }
                    } catch (java.io.IOException e2) {
                        getErrPrintWriter().println("Error: Failed to parse APK file: " + inPath);
                        throw new java.lang.IllegalArgumentException("Error: Failed to parse APK file: " + inPath, e2);
                    }
                } finally {
                }
            }
            params.sessionParams.setSize(sessionSize);
        }
    }

    private int displayPackageFilePath(java.lang.String pckg, int userId) throws android.os.RemoteException {
        android.content.pm.PackageInfo info = this.mInterface.getPackageInfo(pckg, 1073741824L, userId);
        if (info != null && info.applicationInfo != null) {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.print("package:");
            pw.println(info.applicationInfo.sourceDir);
            if (!com.android.internal.util.ArrayUtils.isEmpty(info.applicationInfo.splitSourceDirs)) {
                for (java.lang.String splitSourceDir : info.applicationInfo.splitSourceDirs) {
                    pw.print("package:");
                    pw.println(splitSourceDir);
                }
            }
            return 0;
        }
        return 1;
    }

    private int runPath() throws android.os.RemoteException {
        int userId = 0;
        java.lang.String option = getNextOption();
        if (option != null && option.equals("--user")) {
            userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
        }
        java.lang.String pkg = getNextArgRequired();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package specified");
            return 1;
        }
        int translatedUserId = translateUserId(userId, -10000, "runPath");
        return displayPackageFilePath(pkg, translatedUserId);
    }

    private int runList() throws android.os.RemoteException {
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String type = getNextArg();
        if (type != null) {
            switch (type.hashCode()) {
                case -1126096540:
                    b = !type.equals("staged-sessions") ? (byte) -1 : (byte) 7;
                    break;
                case -997447790:
                    b = !type.equals("permission-groups") ? (byte) -1 : (byte) 5;
                    break;
                case -807062458:
                    b = !type.equals("package") ? (byte) -1 : (byte) 3;
                    break;
                case -290659267:
                    b = !type.equals("features") ? (byte) -1 : (byte) 0;
                    break;
                case 3525497:
                    b = !type.equals("sdks") ? (byte) -1 : (byte) 8;
                    break;
                case 111578632:
                    b = !type.equals(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS) ? (byte) -1 : (byte) 9;
                    break;
                case 544550766:
                    b = !type.equals("instrumentation") ? (byte) -1 : (byte) 1;
                    break;
                case 750867693:
                    b = !type.equals("packages") ? (byte) -1 : (byte) 4;
                    break;
                case 812757657:
                    b = !type.equals("libraries") ? (byte) -1 : (byte) 2;
                    break;
                case 1133704324:
                    b = !type.equals("permissions") ? (byte) -1 : (byte) 6;
                    break;
                case 1786251458:
                    b = !type.equals("initial-non-stopped-system-packages") ? (byte) -1 : (byte) 10;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return runListFeatures();
                case 1:
                    return runListInstrumentation();
                case 2:
                    return runListLibraries();
                case 3:
                case 4:
                    return runListPackages(false);
                case 5:
                    return runListPermissionGroups();
                case 6:
                    return runListPermissions();
                case 7:
                    return runListStagedSessions();
                case 8:
                    return runListSdks();
                case 9:
                    android.os.ServiceManager.getService("user").shellCommand(getInFileDescriptor(), getOutFileDescriptor(), getErrFileDescriptor(), new java.lang.String[]{"list"}, getShellCallback(), adoptResultReceiver());
                    return 0;
                case 10:
                    return runListInitialNonStoppedSystemPackages();
                default:
                    int result = this.mPackageManagerScExt.customLogicOnRunList(this, type);
                    if (result >= 0) {
                        return result;
                    }
                    pw.println("Error: unknown list type '" + type + "'");
                    return -1;
            }
        }
        pw.println("Error: didn't specify type of data to list");
        return -1;
    }

    private int runGc() throws android.os.RemoteException {
        java.lang.Runtime.getRuntime().gc();
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Ok");
        return 0;
    }

    private int runListInitialNonStoppedSystemPackages() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.util.List<java.lang.String> list = this.mInterface.getInitialNonStoppedSystemPackages();
        java.util.Collections.sort(list);
        for (java.lang.String pkgName : list) {
            pw.print("package:");
            pw.print(pkgName);
            pw.println();
        }
        return 0;
    }

    private int runListFeatures() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.util.List<android.content.pm.FeatureInfo> list = this.mInterface.getSystemAvailableFeatures().getList();
        java.util.Collections.sort(list, new java.util.Comparator<android.content.pm.FeatureInfo>() { // from class: com.android.server.pm.PackageManagerShellCommand.1
            @Override // java.util.Comparator
            public int compare(android.content.pm.FeatureInfo o1, android.content.pm.FeatureInfo o2) {
                if (o1.name == o2.name) {
                    return 0;
                }
                if (o1.name == null) {
                    return -1;
                }
                if (o2.name == null) {
                    return 1;
                }
                return o1.name.compareTo(o2.name);
            }
        });
        int count = list != null ? list.size() : 0;
        for (int p = 0; p < count; p++) {
            android.content.pm.FeatureInfo fi = list.get(p);
            pw.print("feature:");
            if (fi.name != null) {
                pw.print(fi.name);
                if (fi.version > 0) {
                    pw.print("=");
                    pw.print(fi.version);
                }
                pw.println();
            } else {
                pw.println("reqGlEsVersion=0x" + java.lang.Integer.toHexString(fi.reqGlEsVersion));
            }
        }
        return 0;
    }

    private int runListInstrumentation() throws android.os.RemoteException {
        java.lang.String opt;
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean showSourceDir = false;
        java.lang.String targetPackage = null;
        while (true) {
            try {
                opt = getNextArg();
            } catch (java.lang.RuntimeException ex) {
                pw.println("Error: " + ex.toString());
                return -1;
            }
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1497:
                        if (opt.equals("-f")) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        showSourceDir = true;
                        continue;
                    default:
                        if (opt.charAt(0) != '-') {
                            targetPackage = opt;
                            continue;
                        } else {
                            pw.println("Error: Unknown option: " + opt);
                            return -1;
                        }
                        break;
                }
                pw.println("Error: " + ex.toString());
                return -1;
            }
            java.util.List<android.content.pm.InstrumentationInfo> list = this.mInterface.queryInstrumentationAsUser(targetPackage, 4202496, 0).getList();
            java.util.Collections.sort(list, new java.util.Comparator<android.content.pm.InstrumentationInfo>() { // from class: com.android.server.pm.PackageManagerShellCommand.2
                @Override // java.util.Comparator
                public int compare(android.content.pm.InstrumentationInfo o1, android.content.pm.InstrumentationInfo o2) {
                    return o1.targetPackage.compareTo(o2.targetPackage);
                }
            });
            int count = list != null ? list.size() : 0;
            for (int p = 0; p < count; p++) {
                android.content.pm.InstrumentationInfo ii = list.get(p);
                pw.print("instrumentation:");
                if (showSourceDir) {
                    pw.print(ii.sourceDir);
                    pw.print("=");
                }
                android.content.ComponentName cn = new android.content.ComponentName(ii.packageName, ii.name);
                pw.print(cn.flattenToShortString());
                pw.print(" (target=");
                pw.print(ii.targetPackage);
                pw.println(")");
            }
            return 0;
        }
    }

    private int runListLibraries() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean verbose = false;
        while (true) {
            java.lang.String opt = getNextArg();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1513:
                        if (!opt.equals("-v")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        verbose = true;
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return -1;
                }
            } else {
                java.util.Map<java.lang.String, java.lang.String> namesAndPaths = this.mInterface.getSystemSharedLibraryNamesAndPaths();
                if (namesAndPaths.isEmpty()) {
                    return 0;
                }
                java.util.List<java.lang.String> libs = new java.util.ArrayList<>(namesAndPaths.keySet());
                java.util.Collections.sort(libs, new java.util.Comparator() { // from class: com.android.server.pm.PackageManagerShellCommand$$ExternalSyntheticLambda2
                    @Override // java.util.Comparator
                    public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                        return com.android.server.pm.PackageManagerShellCommand.lambda$runListLibraries$0((java.lang.String) obj, (java.lang.String) obj2);
                    }
                });
                for (int i = 0; i < libs.size(); i++) {
                    java.lang.String lib = libs.get(i);
                    pw.print("library:");
                    pw.print(lib);
                    if (verbose) {
                        pw.print(" path:");
                        pw.print(namesAndPaths.get(lib));
                    }
                    pw.println();
                }
                return 0;
            }
        }
    }

    static /* synthetic */ int lambda$runListLibraries$0(java.lang.String o1, java.lang.String o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return o1.compareTo(o2);
    }

    private int runListPackages(boolean showSourceDir) throws android.os.RemoteException {
        return runListPackages(showSourceDir, false);
    }

    private int runListSdks() throws android.os.RemoteException {
        return runListPackages(false, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:157:0x0318 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0329  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x032b  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0346  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x03c2  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x03e7  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0401  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x041a  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0155 A[PHI: r0
  0x0155: PHI (r0v84 'opt' java.lang.String) = 
  (r0v33 'opt' java.lang.String)
  (r0v34 'opt' java.lang.String)
  (r0v35 'opt' java.lang.String)
  (r0v36 'opt' java.lang.String)
  (r0v37 'opt' java.lang.String)
  (r0v74 'opt' java.lang.String)
  (r0v85 'opt' java.lang.String)
 binds: [B:68:0x013a, B:65:0x012d, B:62:0x0120, B:59:0x0114, B:56:0x0108, B:54:0x00fe, B:13:0x004a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 11 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runListPackages(boolean r47, boolean r48) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 1482
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.runListPackages(boolean, boolean):int");
    }

    static /* synthetic */ java.util.List lambda$runListPackages$1(java.lang.String k) {
        return new java.util.ArrayList();
    }

    private int runListPermissionGroups() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.util.List<android.content.pm.PermissionGroupInfo> pgs = this.mPermissionManager.getAllPermissionGroups(0);
        int count = pgs.size();
        for (int p = 0; p < count; p++) {
            android.content.pm.PermissionGroupInfo pgi = pgs.get(p);
            pw.print("permission group:");
            pw.println(pgi.name);
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runListPermissions() throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 300
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.runListPermissions():int");
    }

    private static class SessionDump {
        boolean onlyParent;
        boolean onlyReady;
        boolean onlySessionId;

        private SessionDump() {
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean setSessionFlag(java.lang.String r4, com.android.server.pm.PackageManagerShellCommand.SessionDump r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -2056597429: goto L1e;
                case -1847964944: goto L14;
                case 1321081314: goto La;
                default: goto L9;
            }
        L9:
            goto L28
        La:
            java.lang.String r0 = "--only-ready"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L9
            r0 = r2
            goto L29
        L14:
            java.lang.String r0 = "--only-sessionid"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L9
            r0 = 2
            goto L29
        L1e:
            java.lang.String r0 = "--only-parent"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L9
            r0 = r1
            goto L29
        L28:
            r0 = -1
        L29:
            switch(r0) {
                case 0: goto L33;
                case 1: goto L30;
                case 2: goto L2d;
                default: goto L2c;
            }
        L2c:
            return r1
        L2d:
            r5.onlySessionId = r2
            goto L36
        L30:
            r5.onlyReady = r2
            goto L36
        L33:
            r5.onlyParent = r2
        L36:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.setSessionFlag(java.lang.String, com.android.server.pm.PackageManagerShellCommand$SessionDump):boolean");
    }

    private int runListStagedSessions() {
        java.lang.String opt;
        com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(getOutPrintWriter(), "  ", 120);
        try {
            com.android.server.pm.PackageManagerShellCommand.SessionDump sessionDump = new com.android.server.pm.PackageManagerShellCommand.SessionDump();
            do {
                opt = getNextOption();
                if (opt == null) {
                    try {
                        java.util.List<android.content.pm.PackageInstaller.SessionInfo> stagedSessions = this.mInterface.getPackageInstaller().getStagedSessions().getList();
                        printSessionList(pw, stagedSessions, sessionDump);
                        pw.close();
                        return 1;
                    } catch (android.os.RemoteException e) {
                        pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
                        pw.close();
                        return -1;
                    }
                }
            } while (setSessionFlag(opt, sessionDump));
            pw.println("Error: Unknown option: " + opt);
            pw.close();
            return -1;
        } catch (java.lang.Throwable th) {
            try {
                pw.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void printSessionList(com.android.internal.util.IndentingPrintWriter pw, java.util.List<android.content.pm.PackageInstaller.SessionInfo> stagedSessions, com.android.server.pm.PackageManagerShellCommand.SessionDump sessionDump) {
        android.util.SparseArray<android.content.pm.PackageInstaller.SessionInfo> sessionById = new android.util.SparseArray<>(stagedSessions.size());
        for (android.content.pm.PackageInstaller.SessionInfo session : stagedSessions) {
            sessionById.put(session.getSessionId(), session);
        }
        for (android.content.pm.PackageInstaller.SessionInfo session2 : stagedSessions) {
            if (!sessionDump.onlyReady || session2.isStagedSessionReady()) {
                if (session2.getParentSessionId() == -1) {
                    printSession(pw, session2, sessionDump);
                    if (session2.isMultiPackage() && !sessionDump.onlyParent) {
                        pw.increaseIndent();
                        int[] childIds = session2.getChildSessionIds();
                        for (int i = 0; i < childIds.length; i++) {
                            android.content.pm.PackageInstaller.SessionInfo childSession = sessionById.get(childIds[i]);
                            if (childSession == null) {
                                if (sessionDump.onlySessionId) {
                                    pw.println(childIds[i]);
                                } else {
                                    pw.println("sessionId = " + childIds[i] + "; not found");
                                }
                            } else {
                                printSession(pw, childSession, sessionDump);
                            }
                        }
                        pw.decreaseIndent();
                    }
                }
            }
        }
    }

    private static void printSession(java.io.PrintWriter pw, android.content.pm.PackageInstaller.SessionInfo session, com.android.server.pm.PackageManagerShellCommand.SessionDump sessionDump) {
        if (sessionDump.onlySessionId) {
            pw.println(session.getSessionId());
        } else {
            pw.println("sessionId = " + session.getSessionId() + "; appPackageName = " + session.getAppPackageName() + "; isStaged = " + session.isStaged() + "; isReady = " + session.isStagedSessionReady() + "; isApplied = " + session.isStagedSessionApplied() + "; isFailed = " + session.isStagedSessionFailed() + "; errorMsg = " + session.getStagedSessionErrorMessage() + ";");
        }
    }

    private android.content.Intent parseIntentAndUser() throws java.net.URISyntaxException {
        this.mTargetUser = -2;
        this.mBrief = false;
        this.mComponents = false;
        android.content.Intent intent = android.content.Intent.parseCommandArgs(this, new android.content.Intent.CommandOptionHandler() { // from class: com.android.server.pm.PackageManagerShellCommand.3
            public boolean handleOption(java.lang.String opt, android.os.ShellCommand cmd) {
                if ("--user".equals(opt)) {
                    com.android.server.pm.PackageManagerShellCommand.this.mTargetUser = android.os.UserHandle.parseUserArg(cmd.getNextArgRequired());
                    return true;
                }
                if ("--brief".equals(opt)) {
                    com.android.server.pm.PackageManagerShellCommand.this.mBrief = true;
                    return true;
                }
                if ("--components".equals(opt)) {
                    com.android.server.pm.PackageManagerShellCommand.this.mComponents = true;
                    return true;
                }
                if ("--query-flags".equals(opt)) {
                    com.android.server.pm.PackageManagerShellCommand.this.mQueryFlags = java.lang.Integer.decode(cmd.getNextArgRequired()).intValue();
                    return true;
                }
                return false;
            }
        });
        this.mTargetUser = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), this.mTargetUser, false, false, null, null);
        return intent;
    }

    private void printResolveInfo(android.util.PrintWriterPrinter pr, java.lang.String prefix, android.content.pm.ResolveInfo ri, boolean brief, boolean components) {
        android.content.ComponentName comp;
        if (brief || components) {
            if (ri.activityInfo != null) {
                comp = new android.content.ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
            } else if (ri.serviceInfo != null) {
                comp = new android.content.ComponentName(ri.serviceInfo.packageName, ri.serviceInfo.name);
            } else if (ri.providerInfo != null) {
                comp = new android.content.ComponentName(ri.providerInfo.packageName, ri.providerInfo.name);
            } else {
                comp = null;
            }
            if (comp != null) {
                if (!components) {
                    pr.println(prefix + "priority=" + ri.priority + " preferredOrder=" + ri.preferredOrder + " match=0x" + java.lang.Integer.toHexString(ri.match) + " specificIndex=" + ri.specificIndex + " isDefault=" + ri.isDefault);
                }
                pr.println(prefix + comp.flattenToShortString());
                return;
            }
        }
        ri.dump(pr, prefix);
    }

    private int runResolveActivity() {
        try {
            android.content.Intent intent = parseIntentAndUser();
            try {
                android.content.pm.ResolveInfo ri = this.mInterface.resolveIntent(intent, intent.getType(), this.mQueryFlags, this.mTargetUser);
                java.io.PrintWriter pw = getOutPrintWriter();
                if (ri == null) {
                    pw.println("No activity found");
                    return 0;
                }
                android.util.PrintWriterPrinter pr = new android.util.PrintWriterPrinter(pw);
                printResolveInfo(pr, "", ri, this.mBrief, this.mComponents);
                return 0;
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("Failed calling service", e);
            }
        } catch (java.net.URISyntaxException e2) {
            throw new java.lang.RuntimeException(e2.getMessage(), e2);
        }
    }

    private int runQueryIntentActivities() {
        try {
            android.content.Intent intent = parseIntentAndUser();
            try {
                java.util.List<android.content.pm.ResolveInfo> result = this.mInterface.queryIntentActivities(intent, intent.getType(), this.mQueryFlags, this.mTargetUser).getList();
                java.io.PrintWriter pw = getOutPrintWriter();
                if (result != null && result.size() > 0) {
                    if (!this.mComponents) {
                        pw.print(result.size());
                        pw.println(" activities found:");
                        android.util.PrintWriterPrinter pr = new android.util.PrintWriterPrinter(pw);
                        for (int i = 0; i < result.size(); i++) {
                            pw.print("  Activity #");
                            pw.print(i);
                            pw.println(":");
                            printResolveInfo(pr, "    ", result.get(i), this.mBrief, this.mComponents);
                        }
                        return 0;
                    }
                    android.util.PrintWriterPrinter pr2 = new android.util.PrintWriterPrinter(pw);
                    for (int i2 = 0; i2 < result.size(); i2++) {
                        printResolveInfo(pr2, "", result.get(i2), this.mBrief, this.mComponents);
                    }
                    return 0;
                }
                pw.println("No activities found");
                return 0;
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("Failed calling service", e);
            }
        } catch (java.net.URISyntaxException e2) {
            throw new java.lang.RuntimeException(e2.getMessage(), e2);
        }
    }

    private int runQueryIntentServices() {
        try {
            android.content.Intent intent = parseIntentAndUser();
            try {
                java.util.List<android.content.pm.ResolveInfo> result = this.mInterface.queryIntentServices(intent, intent.getType(), this.mQueryFlags, this.mTargetUser).getList();
                java.io.PrintWriter pw = getOutPrintWriter();
                if (result != null && result.size() > 0) {
                    if (!this.mComponents) {
                        pw.print(result.size());
                        pw.println(" services found:");
                        android.util.PrintWriterPrinter pr = new android.util.PrintWriterPrinter(pw);
                        for (int i = 0; i < result.size(); i++) {
                            pw.print("  Service #");
                            pw.print(i);
                            pw.println(":");
                            printResolveInfo(pr, "    ", result.get(i), this.mBrief, this.mComponents);
                        }
                        return 0;
                    }
                    android.util.PrintWriterPrinter pr2 = new android.util.PrintWriterPrinter(pw);
                    for (int i2 = 0; i2 < result.size(); i2++) {
                        printResolveInfo(pr2, "", result.get(i2), this.mBrief, this.mComponents);
                    }
                    return 0;
                }
                pw.println("No services found");
                return 0;
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("Failed calling service", e);
            }
        } catch (java.net.URISyntaxException e2) {
            throw new java.lang.RuntimeException(e2.getMessage(), e2);
        }
    }

    private int runQueryIntentReceivers() {
        try {
            android.content.Intent intent = parseIntentAndUser();
            try {
                java.util.List<android.content.pm.ResolveInfo> result = this.mInterface.queryIntentReceivers(intent, intent.getType(), this.mQueryFlags, this.mTargetUser).getList();
                java.io.PrintWriter pw = getOutPrintWriter();
                if (result != null && result.size() > 0) {
                    if (!this.mComponents) {
                        pw.print(result.size());
                        pw.println(" receivers found:");
                        android.util.PrintWriterPrinter pr = new android.util.PrintWriterPrinter(pw);
                        for (int i = 0; i < result.size(); i++) {
                            pw.print("  Receiver #");
                            pw.print(i);
                            pw.println(":");
                            printResolveInfo(pr, "    ", result.get(i), this.mBrief, this.mComponents);
                        }
                        return 0;
                    }
                    android.util.PrintWriterPrinter pr2 = new android.util.PrintWriterPrinter(pw);
                    for (int i2 = 0; i2 < result.size(); i2++) {
                        printResolveInfo(pr2, "", result.get(i2), this.mBrief, this.mComponents);
                    }
                    return 0;
                }
                pw.println("No receivers found");
                return 0;
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("Failed calling service", e);
            }
        } catch (java.net.URISyntaxException e2) {
            throw new java.lang.RuntimeException(e2.getMessage(), e2);
        }
    }

    private int runStreamingInstall() throws android.os.RemoteException {
        com.android.server.pm.PackageManagerShellCommand.InstallParams params = makeInstallParams(UNSUPPORTED_INSTALL_CMD_OPTS);
        if (params.sessionParams.dataLoaderParams == null) {
            params.sessionParams.setDataLoaderParams(com.android.server.pm.PackageManagerShellCommandDataLoader.getStreamingDataLoaderParams(this));
        }
        return doRunInstall(params);
    }

    private int runArchivedInstall() throws android.os.RemoteException {
        com.android.server.pm.PackageManagerShellCommand.InstallParams params = makeInstallParams(UNSUPPORTED_INSTALL_CMD_OPTS);
        params.sessionParams.installFlags |= 134217728;
        if (params.sessionParams.dataLoaderParams == null) {
            params.sessionParams.setDataLoaderParams(com.android.server.pm.PackageManagerShellCommandDataLoader.getStreamingDataLoaderParams(this));
        }
        return doRunInstall(params);
    }

    private int runIncrementalInstall() throws android.os.RemoteException {
        com.android.server.pm.PackageManagerShellCommand.InstallParams params = makeInstallParams(UNSUPPORTED_INSTALL_CMD_OPTS);
        if (params.sessionParams.dataLoaderParams == null) {
            params.sessionParams.setDataLoaderParams(com.android.server.pm.PackageManagerShellCommandDataLoader.getIncrementalDataLoaderParams(this));
        }
        return doRunInstall(params);
    }

    private int runInstall() throws android.os.RemoteException {
        return doRunInstall(makeInstallParams(UNSUPPORTED_INSTALL_CMD_OPTS));
    }

    private int doRunInstall(com.android.server.pm.PackageManagerShellCommand.InstallParams params) throws java.lang.Throwable {
        java.lang.Throwable th;
        int sessionId;
        java.io.PrintWriter pw = getOutPrintWriter();
        int requestUserId = params.userId;
        int sessionId2 = 1;
        if (requestUserId != -1 && requestUserId != -2) {
            com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            android.content.pm.UserInfo userInfo = umi.getUserInfo(requestUserId);
            if (userInfo == null) {
                pw.println("Failure [user " + requestUserId + " doesn't exist]");
                return 1;
            }
        }
        boolean isStreaming = params.sessionParams.dataLoaderParams != null;
        boolean isApex = (params.sessionParams.installFlags & 131072) != 0;
        boolean installArchived = (params.sessionParams.installFlags & 134217728) != 0;
        java.util.ArrayList<java.lang.String> args = getRemainingArgs();
        boolean fromStdIn = args.isEmpty() || STDIN_PATH.equals(args.get(0));
        boolean hasSplits = args.size() > 1;
        if (fromStdIn && params.sessionParams.sizeBytes == -1) {
            pw.println("Error: must either specify a package size or an APK file");
            return 1;
        }
        if (this.mPackageManagerScExt.interceptInDoRunInstall(this)) {
            return 1;
        }
        if (isApex && hasSplits) {
            pw.println("Error: can't specify SPLIT(s) for APEX");
            return 1;
        }
        if (installArchived && hasSplits) {
            pw.println("Error: can't have SPLIT(s) for Archival install");
            return 1;
        }
        if (!isStreaming) {
            if (fromStdIn && hasSplits) {
                pw.println("Error: can't specify SPLIT(s) along with STDIN");
                return 1;
            }
            if (args.isEmpty()) {
                args.add(STDIN_PATH);
            } else {
                setParamsSize(params, args);
            }
        }
        int sessionId3 = doCreateSession(params.sessionParams, params.installerPackageName, params.userId);
        try {
            if (isStreaming) {
                try {
                    sessionId = sessionId3;
                    if (doAddFiles(sessionId3, args, params.sessionParams.sizeBytes, isApex, installArchived) != 0) {
                        if (1 == 0) {
                            return 1;
                        }
                        try {
                            doAbandonSession(sessionId, false);
                            return 1;
                        } catch (java.lang.Exception e) {
                            return 1;
                        }
                    }
                } catch (java.lang.Throwable th2) {
                    sessionId2 = sessionId3;
                    th = th2;
                    if (1 == 0) {
                        throw th;
                    }
                    try {
                        doAbandonSession(sessionId2, false);
                        throw th;
                    } catch (java.lang.Exception e2) {
                        throw th;
                    }
                }
            } else {
                sessionId = sessionId3;
                if (doWriteSplits(sessionId, args, params.sessionParams.sizeBytes, isApex) != 0) {
                    if (1 == 0) {
                        return 1;
                    }
                    try {
                        doAbandonSession(sessionId, false);
                        return 1;
                    } catch (java.lang.Exception e3) {
                        return 1;
                    }
                }
            }
            if (doCommitSession(sessionId, false) != 0) {
                if (1 == 0) {
                    return 1;
                }
                try {
                    doAbandonSession(sessionId, false);
                    return 1;
                } catch (java.lang.Exception e4) {
                    return 1;
                }
            }
            if (!params.sessionParams.isStaged || params.stagedReadyTimeoutMs <= 0) {
                pw.println("Success");
                if (0 != 0) {
                    try {
                        doAbandonSession(sessionId, false);
                    } catch (java.lang.Exception e5) {
                    }
                }
                return 0;
            }
            int iDoWaitForStagedSessionReady = doWaitForStagedSessionReady(sessionId, params.stagedReadyTimeoutMs, pw);
            if (0 != 0) {
                try {
                    doAbandonSession(sessionId, false);
                } catch (java.lang.Exception e6) {
                }
            }
            return iDoWaitForStagedSessionReady;
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
    }

    private int doWaitForStagedSessionReady(int sessionId, long timeoutMs, java.io.PrintWriter pw) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkArgument(timeoutMs > 0);
        android.content.pm.PackageInstaller.SessionInfo si = this.mInterface.getPackageInstaller().getSessionInfo(sessionId);
        if (si == null) {
            pw.println("Failure [Unknown session " + sessionId + "]");
            return 1;
        }
        if (!si.isStaged()) {
            pw.println("Failure [Session " + sessionId + " is not a staged session]");
            return 1;
        }
        long currentTime = java.lang.System.currentTimeMillis();
        long endTime = currentTime + timeoutMs;
        while (si != null && currentTime < endTime && !si.isStagedSessionReady() && !si.isStagedSessionFailed()) {
            android.os.SystemClock.sleep(java.lang.Math.min(endTime - currentTime, 100L));
            currentTime = java.lang.System.currentTimeMillis();
            si = this.mInterface.getPackageInstaller().getSessionInfo(sessionId);
        }
        if (si == null) {
            pw.println("Failure [failed to retrieve SessionInfo]");
            return 1;
        }
        if (!si.isStagedSessionReady() && !si.isStagedSessionFailed()) {
            pw.println("Failure [timed out after " + timeoutMs + " ms]");
            return 1;
        }
        if (!si.isStagedSessionReady()) {
            pw.println("Error [" + si.getStagedSessionErrorCode() + "] [" + si.getStagedSessionErrorMessage() + "]");
            return 1;
        }
        pw.println("Success. Reboot device to apply staged session");
        return 0;
    }

    private int runInstallAbandon() throws android.os.RemoteException {
        int sessionId = java.lang.Integer.parseInt(getNextArg());
        return doAbandonSession(sessionId, true);
    }

    private int runInstallCommit() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        long stagedReadyTimeoutMs = 60000;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -158482320:
                        if (!opt.equals("--staged-ready-timeout")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        stagedReadyTimeoutMs = java.lang.Long.parseLong(getNextArgRequired());
                        break;
                    default:
                        throw new java.lang.IllegalArgumentException("Unknown option: " + opt);
                }
            } else {
                int sessionId = java.lang.Integer.parseInt(getNextArg());
                if (doCommitSession(sessionId, false) != 0) {
                    return 1;
                }
                android.content.pm.PackageInstaller.SessionInfo si = this.mInterface.getPackageInstaller().getSessionInfo(sessionId);
                if (si != null && si.isStaged() && stagedReadyTimeoutMs > 0) {
                    return doWaitForStagedSessionReady(sessionId, stagedReadyTimeoutMs, pw);
                }
                pw.println("Success");
                return 0;
            }
        }
    }

    private int runInstallCreate() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        com.android.server.pm.PackageManagerShellCommand.InstallParams installParams = makeInstallParams(UNSUPPORTED_SESSION_CREATE_OPTS);
        int sessionId = doCreateSession(installParams.sessionParams, installParams.installerPackageName, installParams.userId);
        pw.println("Success: created install session [" + sessionId + "]");
        return 0;
    }

    private int runInstallWrite() throws android.os.RemoteException {
        long sizeBytes = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("-S")) {
                    sizeBytes = java.lang.Long.parseLong(getNextArg());
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + opt);
                }
            } else {
                int sessionId = java.lang.Integer.parseInt(getNextArg());
                java.lang.String splitName = getNextArg();
                java.lang.String path = getNextArg();
                return doWriteSplit(sessionId, path, sizeBytes, splitName, true);
            }
        }
    }

    private int runInstallAddSession() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        int parentSessionId = java.lang.Integer.parseInt(getNextArg());
        android.util.IntArray otherSessionIds = new android.util.IntArray();
        while (true) {
            java.lang.String opt = getNextArg();
            if (opt == null) {
                break;
            }
            otherSessionIds.add(java.lang.Integer.parseInt(opt));
        }
        if (otherSessionIds.size() == 0) {
            pw.println("Error: At least two sessions are required.");
            return 1;
        }
        return doInstallAddSession(parentSessionId, otherSessionIds.toArray(), true);
    }

    private int runInstallSetPreVerifiedDomains() throws android.os.RemoteException {
        getOutPrintWriter();
        int sessionId = java.lang.Integer.parseInt(getNextArg());
        java.lang.String preVerifiedDomainsStr = getNextArg();
        java.lang.String[] preVerifiedDomains = preVerifiedDomainsStr.split(",");
        android.content.pm.PackageInstaller.Session session = null;
        try {
            session = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(sessionId));
            session.setPreVerifiedDomains(new android.util.ArraySet(preVerifiedDomains));
            libcore.io.IoUtils.closeQuietly(session);
            return 0;
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly(session);
            throw th;
        }
    }

    private int runInstallGetPreVerifiedDomains() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        int sessionId = java.lang.Integer.parseInt(getNextArg());
        try {
            android.content.pm.PackageInstaller.Session session = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(sessionId));
            java.util.Set<java.lang.String> preVerifiedDomains = session.getPreVerifiedDomains();
            if (preVerifiedDomains.isEmpty()) {
                pw.println("The session doesn't have any pre-verified domains specified.");
            } else {
                pw.println(java.lang.String.join(",", preVerifiedDomains));
            }
            libcore.io.IoUtils.closeQuietly(session);
            return 0;
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly((java.lang.AutoCloseable) null);
            throw th;
        }
    }

    private int runInstallRemove() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        int sessionId = java.lang.Integer.parseInt(getNextArg());
        java.util.ArrayList<java.lang.String> splitNames = getRemainingArgs();
        if (splitNames.isEmpty()) {
            pw.println("Error: split name not specified");
            return 1;
        }
        return doRemoveSplits(sessionId, splitNames, true);
    }

    private int runGetArchivedPackageMetadata() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1333469547:
                        if (!opt.equals("--user")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                if (packageName == null) {
                    pw.println("Error: package name not specified");
                    return 1;
                }
                int translatedUserId = translateUserId(userId, -10000, "runGetArchivedPackageMetadata");
                try {
                    android.os.Parcelable archivedPackage = this.mInterface.getArchivedPackage(packageName, translatedUserId);
                    if (archivedPackage == null) {
                        pw.write("Package not found " + packageName);
                        return -1;
                    }
                    android.os.Parcel parcel = android.os.Parcel.obtain();
                    try {
                        parcel.writeParcelable(archivedPackage, 0);
                        byte[] bytes = parcel.marshall();
                        parcel.recycle();
                        java.lang.String encoded = libcore.util.HexEncoding.encodeToString(bytes);
                        pw.write(encoded);
                        return 0;
                    } catch (java.lang.Throwable th) {
                        parcel.recycle();
                        throw th;
                    }
                } catch (java.lang.Exception e) {
                    getErrPrintWriter().println("Failed to get archived package, reason: " + e);
                    pw.println("Failure [failed to get archived package], reason: " + e);
                    return -1;
                }
            }
        }
    }

    protected static java.lang.String getFormattedBytes(long size) {
        double k = size / 1024.0d;
        double m = size / 1048576.0d;
        double g = size / 1.073741824E9d;
        java.text.DecimalFormat dec = new java.text.DecimalFormat("0.00");
        if (g > 1.0d) {
            return dec.format(g).concat(" Gb");
        }
        if (m > 1.0d) {
            return dec.format(m).concat(" Mb");
        }
        if (k > 1.0d) {
            return dec.format(k).concat(" Kb");
        }
        return "";
    }

    private java.lang.String getDataSizeDisplay(long size) {
        java.lang.String formattedOutput = getFormattedBytes(size);
        if (!formattedOutput.isEmpty()) {
            formattedOutput = " (" + formattedOutput + ")";
        }
        return java.lang.Long.toString(size) + " bytes" + formattedOutput;
    }

    private int runGetPackageStorageStats() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        if (!com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.getPackageStorageStats()) {
            pw.println("Error: get_package_storage_stats flag is not enabled");
            return 1;
        }
        if (!android.app.usage.Flags.getAppBytesByDataTypeApi()) {
            pw.println("Error: get_app_bytes_by_data_type_api flag is not enabled");
            return 1;
        }
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = -1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1333469547:
                        if (opt.equals("--user")) {
                            b = 0;
                        }
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                if (packageName == null) {
                    pw.println("Error: package name not specified");
                    return 1;
                }
                try {
                    android.app.usage.StorageStatsManager storageStatsManager = (android.app.usage.StorageStatsManager) this.mContext.getSystemService(android.app.usage.StorageStatsManager.class);
                    int translatedUserId = translateUserId(userId, -10000, "runGetPackageStorageStats");
                    android.app.usage.StorageStats stats = storageStatsManager.queryStatsForPackage(android.os.storage.StorageManager.UUID_DEFAULT, packageName, android.os.UserHandle.of(translatedUserId));
                    pw.println("code: " + getDataSizeDisplay(stats.getAppBytes()));
                    pw.println("data: " + getDataSizeDisplay(stats.getDataBytes()));
                    pw.println("cache: " + getDataSizeDisplay(stats.getCacheBytes()));
                    pw.println("apk: " + getDataSizeDisplay(stats.getAppBytesByDataType(3)));
                    pw.println("lib: " + getDataSizeDisplay(stats.getAppBytesByDataType(5)));
                    pw.println("dm: " + getDataSizeDisplay(stats.getAppBytesByDataType(4)));
                    pw.println("dexopt artifacts: " + getDataSizeDisplay(stats.getAppBytesByDataType(0)));
                    pw.println("current profile : " + getDataSizeDisplay(stats.getAppBytesByDataType(2)));
                    pw.println("reference profile: " + getDataSizeDisplay(stats.getAppBytesByDataType(1)));
                    pw.println("external cache: " + getDataSizeDisplay(stats.getExternalCacheBytes()));
                    return 0;
                } catch (java.lang.Exception e) {
                    getErrPrintWriter().println("Failed to get storage stats, reason: " + e);
                    pw.println("Failure [failed to get storage stats], reason: " + e);
                    return -1;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runInstallExisting() throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 428
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.runInstallExisting():int");
    }

    private int runSetInstallLocation() throws android.os.RemoteException {
        java.lang.String arg = getNextArg();
        if (arg == null) {
            getErrPrintWriter().println("Error: no install location specified.");
            return 1;
        }
        try {
            int loc = java.lang.Integer.parseInt(arg);
            if (!this.mInterface.setInstallLocation(loc)) {
                getErrPrintWriter().println("Error: install location has to be a number.");
                return 1;
            }
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: install location has to be a number.");
            return 1;
        }
    }

    private int runGetInstallLocation() throws android.os.RemoteException {
        int loc = this.mInterface.getInstallLocation();
        java.lang.String locStr = "invalid";
        if (loc == 0) {
            locStr = "auto";
        } else if (loc == 1) {
            locStr = "internal";
        } else if (loc == 2) {
            locStr = "external";
        }
        getOutPrintWriter().println(loc + "[" + locStr + "]");
        return 0;
    }

    public int runMovePackage() throws android.os.RemoteException {
        java.lang.String packageName = getNextArg();
        if (packageName == null) {
            getErrPrintWriter().println("Error: package name not specified");
            return 1;
        }
        java.lang.String volumeUuid = getNextArg();
        if ("internal".equals(volumeUuid)) {
            volumeUuid = null;
        }
        int moveId = this.mInterface.movePackage(packageName, volumeUuid);
        int status = this.mInterface.getMoveStatus(moveId);
        while (!android.content.pm.PackageManager.isMoveStatusFinished(status)) {
            android.os.SystemClock.sleep(1000L);
            status = this.mInterface.getMoveStatus(moveId);
        }
        if (status == -100) {
            getOutPrintWriter().println("Success");
            return 0;
        }
        getErrPrintWriter().println("Failure [" + status + "]");
        return 1;
    }

    public int runMovePrimaryStorage() throws android.os.RemoteException {
        java.lang.String volumeUuid = getNextArg();
        if ("internal".equals(volumeUuid)) {
            volumeUuid = null;
        }
        int moveId = this.mInterface.movePrimaryStorage(volumeUuid);
        int status = this.mInterface.getMoveStatus(moveId);
        while (!android.content.pm.PackageManager.isMoveStatusFinished(status)) {
            android.os.SystemClock.sleep(1000L);
            status = this.mInterface.getMoveStatus(moveId);
        }
        if (status == -100) {
            getOutPrintWriter().println("Success");
            return 0;
        }
        getErrPrintWriter().println("Failure [" + status + "]");
        return 1;
    }

    private java.util.ArrayList<java.lang.String> getRemainingArgs() {
        java.util.ArrayList<java.lang.String> args = new java.util.ArrayList<>();
        while (true) {
            java.lang.String arg = getNextArg();
            if (arg != null) {
                args.add(arg);
            } else {
                return args;
            }
        }
    }

    private static class SnapshotRuntimeProfileCallback extends android.content.pm.dex.ISnapshotRuntimeProfileCallback.Stub {
        private boolean mSuccess = false;
        private int mErrCode = -1;
        private android.os.ParcelFileDescriptor mProfileReadFd = null;
        private final java.util.concurrent.CountDownLatch mDoneSignal = new java.util.concurrent.CountDownLatch(1);

        private SnapshotRuntimeProfileCallback() {
        }

        public void onSuccess(android.os.ParcelFileDescriptor profileReadFd) {
            this.mSuccess = true;
            try {
                this.mProfileReadFd = profileReadFd.dup();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            this.mDoneSignal.countDown();
        }

        public void onError(int errCode) {
            this.mSuccess = false;
            this.mErrCode = errCode;
            this.mDoneSignal.countDown();
        }

        boolean waitTillDone() throws java.lang.InterruptedException {
            boolean done = false;
            try {
                done = this.mDoneSignal.await(10000000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException e) {
            }
            return done && this.mSuccess;
        }
    }

    private int runUninstall() throws android.os.RemoteException {
        java.lang.String str;
        int translatedUserId;
        java.io.PrintWriter pw = getOutPrintWriter();
        int flags = 0;
        int userId = -1;
        long versionCode = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1502:
                        if (!opt.equals("-k")) {
                            b = -1;
                        }
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 1;
                        break;
                    case 1884113221:
                        b = !opt.equals("--versionCode") ? (byte) -1 : (byte) 2;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        flags |= 1;
                        break;
                    case 1:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        if (userId != -1 && userId != -2) {
                            com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
                            android.content.pm.UserInfo userInfo = umi.getUserInfo(userId);
                            if (userInfo == null) {
                                pw.println("Failure [user " + userId + " doesn't exist]");
                                return 1;
                            }
                        }
                        break;
                    case 2:
                        long versionCode2 = java.lang.Long.parseLong(getNextArgRequired());
                        versionCode = versionCode2;
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                if (packageName != null) {
                    if (this.mPackageManagerScExt.interceptInRunUninstall(this)) {
                        return 1;
                    }
                    java.util.ArrayList<java.lang.String> splitNames = getRemainingArgs();
                    if (!splitNames.isEmpty()) {
                        return runRemoveSplits(packageName, splitNames);
                    }
                    if (userId == -1) {
                        flags |= 2;
                    }
                    int translatedUserId2 = translateUserId(userId, 0, "runUninstall");
                    com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver receiver = new com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver();
                    android.content.pm.PackageManagerInternal internal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                    if (internal.isApexPackage(packageName)) {
                        str = "]";
                        internal.uninstallApex(packageName, versionCode, translatedUserId2, receiver.getIntentSender(), flags);
                    } else {
                        str = "]";
                        if ((flags & 2) != 0) {
                            translatedUserId = translatedUserId2;
                        } else {
                            translatedUserId = translatedUserId2;
                            android.content.pm.PackageInfo info = this.mInterface.getPackageInfo(packageName, 67108864L, translatedUserId);
                            if (info == null) {
                                pw.println("Failure [not installed for " + translatedUserId + str);
                                return 1;
                            }
                            boolean isSystem = (info.applicationInfo.flags & 1) != 0;
                            if (isSystem) {
                                flags |= 4;
                            }
                        }
                        this.mInterface.getPackageInstaller().uninstall(new android.content.pm.VersionedPackage(packageName, versionCode), (java.lang.String) null, flags, receiver.getIntentSender(), translatedUserId);
                    }
                    android.content.Intent result = receiver.getResult();
                    int status = result.getIntExtra("android.content.pm.extra.STATUS", 1);
                    if (status == 0) {
                        pw.println("Success");
                        return 0;
                    }
                    pw.println("Failure [" + result.getStringExtra("android.content.pm.extra.STATUS_MESSAGE") + str);
                    return 1;
                }
                pw.println("Error: package name not specified");
                return 1;
            }
        }
    }

    private int runRemoveSplits(java.lang.String packageName, java.util.Collection<java.lang.String> splitNames) throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        android.content.pm.PackageInstaller.SessionParams sessionParams = new android.content.pm.PackageInstaller.SessionParams(2);
        sessionParams.installFlags = 2 | sessionParams.installFlags;
        sessionParams.appPackageName = packageName;
        int sessionId = doCreateSession(sessionParams, null, -1);
        boolean abandonSession = true;
        try {
            if (doRemoveSplits(sessionId, splitNames, false) != 0) {
                return 1;
            }
            if (doCommitSession(sessionId, false) != 0) {
                if (1 != 0) {
                    try {
                        doAbandonSession(sessionId, false);
                    } catch (java.lang.RuntimeException e) {
                    }
                }
                return 1;
            }
            abandonSession = false;
            pw.println("Success");
            if (0 != 0) {
                try {
                    doAbandonSession(sessionId, false);
                } catch (java.lang.RuntimeException e2) {
                }
            }
            return 0;
        } finally {
            if (abandonSession) {
                try {
                    doAbandonSession(sessionId, false);
                } catch (java.lang.RuntimeException e3) {
                }
            }
        }
    }

    static class ClearDataObserver extends android.content.pm.IPackageDataObserver.Stub {
        boolean finished;
        boolean result;

        ClearDataObserver() {
        }

        public void onRemoveCompleted(java.lang.String packageName, boolean succeeded) throws android.os.RemoteException {
            synchronized (this) {
                this.finished = true;
                this.result = succeeded;
                notifyAll();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runClear() throws android.os.RemoteException {
        /*
            r10 = this;
            java.io.PrintWriter r0 = r10.getOutPrintWriter()
            r1 = 0
            r2 = 0
        L6:
            java.lang.String r3 = r10.getNextOption()
            r4 = r3
            r5 = 0
            r6 = 1
            if (r3 == 0) goto L51
            int r3 = r4.hashCode()
            switch(r3) {
                case -2056884041: goto L20;
                case 1333469547: goto L17;
                default: goto L16;
            }
        L16:
            goto L2a
        L17:
            java.lang.String r3 = "--user"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L16
            goto L2b
        L20:
            java.lang.String r3 = "--cache-only"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L16
            r5 = r6
            goto L2b
        L2a:
            r5 = -1
        L2b:
            switch(r5) {
                case 0: goto L47;
                case 1: goto L45;
                default: goto L2e;
            }
        L2e:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "Error: Unknown option: "
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r0.println(r3)
            return r6
        L45:
            r2 = 1
            goto L50
        L47:
            java.lang.String r3 = r10.getNextArgRequired()
            int r1 = android.os.UserHandle.parseUserArg(r3)
        L50:
            goto L6
        L51:
            java.lang.String r3 = r10.getNextArg()
            if (r3 != 0) goto L61
            java.io.PrintWriter r5 = r10.getErrPrintWriter()
            java.lang.String r7 = "Error: no package specified"
            r5.println(r7)
            return r6
        L61:
            java.lang.String r7 = "runClear"
            r8 = -10000(0xffffffffffffd8f0, float:NaN)
            int r7 = r10.translateUserId(r1, r8, r7)
            com.android.server.pm.PackageManagerShellCommand$ClearDataObserver r8 = new com.android.server.pm.PackageManagerShellCommand$ClearDataObserver
            r8.<init>()
            if (r2 != 0) goto L79
            android.app.IActivityManager r9 = android.app.ActivityManager.getService()
            r9.clearApplicationUserData(r3, r5, r8, r7)
            goto L7e
        L79:
            android.content.pm.IPackageManager r9 = r10.mInterface
            r9.deleteApplicationCacheFilesAsUser(r3, r7, r8)
        L7e:
            monitor-enter(r8)
        L7f:
            boolean r9 = r8.finished     // Catch: java.lang.Throwable -> La2
            if (r9 != 0) goto L89
            r8.wait()     // Catch: java.lang.InterruptedException -> L87 java.lang.Throwable -> La2
        L86:
            goto L7f
        L87:
            r9 = move-exception
            goto L86
        L89:
            monitor-exit(r8)     // Catch: java.lang.Throwable -> La2
            boolean r9 = r8.result
            if (r9 == 0) goto L98
            java.io.PrintWriter r6 = r10.getOutPrintWriter()
            java.lang.String r9 = "Success"
            r6.println(r9)
            return r5
        L98:
            java.io.PrintWriter r5 = r10.getErrPrintWriter()
            java.lang.String r9 = "Failed"
            r5.println(r9)
            return r6
        La2:
            r5 = move-exception
            monitor-exit(r8)     // Catch: java.lang.Throwable -> La2
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.runClear():int");
    }

    private static java.lang.String enabledSettingToString(int state) {
        switch (state) {
            case 0:
                return "default";
            case 1:
                return com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED;
            case 2:
                return com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
            case 3:
                return "disabled-user";
            case 4:
                return "disabled-until-used";
            default:
                return "unknown";
        }
    }

    private int runSetEnabledSetting(int state) throws android.os.RemoteException {
        int userId = 0;
        java.lang.String option = getNextOption();
        if (option != null && option.equals("--user")) {
            userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
        }
        java.lang.String pkg = getNextArg();
        if (pkg != null) {
            int translatedUserId = translateUserId(userId, -10000, "runSetEnabledSetting");
            android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(pkg);
            if (cn == null) {
                this.mInterface.setApplicationEnabledSetting(pkg, state, 0, translatedUserId, "shell:" + android.os.Process.myUid());
                getOutPrintWriter().println("Package " + pkg + " new state: " + enabledSettingToString(this.mInterface.getApplicationEnabledSetting(pkg, translatedUserId)));
                return 0;
            }
            this.mInterface.setComponentEnabledSetting(cn, state, 0, translatedUserId, "shell");
            getOutPrintWriter().println("Component " + cn.toShortString() + " new state: " + enabledSettingToString(this.mInterface.getComponentEnabledSetting(cn, translatedUserId)));
            return 0;
        }
        getErrPrintWriter().println("Error: no package or component specified");
        return 1;
    }

    private int runSetHiddenSetting(boolean state) throws android.os.RemoteException {
        int userId = 0;
        java.lang.String option = getNextOption();
        if (option != null && option.equals("--user")) {
            userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
        }
        java.lang.String pkg = getNextArg();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package or component specified");
            return 1;
        }
        int translatedUserId = translateUserId(userId, -10000, "runSetHiddenSetting");
        this.mInterface.setApplicationHiddenSettingAsUser(pkg, state, translatedUserId);
        getOutPrintWriter().println("Package " + pkg + " new hidden state: " + this.mInterface.getApplicationHiddenSettingAsUser(pkg, translatedUserId));
        return 0;
    }

    private int runSetStoppedState(boolean state) throws android.os.RemoteException {
        int userId = 0;
        java.lang.String option = getNextOption();
        if (option != null && option.equals("--user")) {
            userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
        }
        java.lang.String pkg = getNextArg();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package specified");
            return 1;
        }
        int translatedUserId = translateUserId(userId, -10000, "runSetStoppedState");
        this.mInterface.setPackageStoppedState(pkg, state, translatedUserId);
        getOutPrintWriter().println("Package " + pkg + " new stopped state: " + this.mInterface.isPackageStoppedForUser(pkg, translatedUserId));
        return 0;
    }

    private int runSetDistractingRestriction() {
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        int userId = 0;
        int flags = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b2 = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1333015820:
                        b = !opt.equals("--flag") ? (byte) -1 : (byte) 1;
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 0;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    case 1:
                        java.lang.String flag = getNextArgRequired();
                        switch (flag.hashCode()) {
                            case -2125559907:
                                if (!flag.equals("hide-notifications")) {
                                    b2 = -1;
                                }
                                break;
                            case -1852537225:
                                b2 = !flag.equals("hide-from-suggestions") ? (byte) -1 : (byte) 1;
                                break;
                            default:
                                b2 = -1;
                                break;
                        }
                        switch (b2) {
                            case 0:
                                flags |= 2;
                                break;
                            case 1:
                                flags |= 1;
                                break;
                            default:
                                pw.println("Unrecognized flag: " + flag);
                                return 1;
                        }
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                java.util.List<java.lang.String> packageNames = getRemainingArgs();
                if (packageNames.isEmpty()) {
                    pw.println("Error: package name not specified");
                    return 1;
                }
                try {
                    int translatedUserId = translateUserId(userId, -10000, "set-distracting");
                    java.lang.String[] errored = this.mInterface.setDistractingPackageRestrictionsAsUser((java.lang.String[]) packageNames.toArray(new java.lang.String[0]), flags, translatedUserId);
                    if (errored.length <= 0) {
                        return 0;
                    }
                    pw.println("Could not set restriction for: " + java.util.Arrays.toString(errored));
                    return 1;
                } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
                    pw.println(e.toString());
                    return 1;
                }
            }
        }
    }

    private int runGetDistractingRestriction() {
        java.io.PrintWriter pw = getOutPrintWriter();
        int userId = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = -1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1333469547:
                        if (opt.equals("--user")) {
                            b = 0;
                        }
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                java.util.List<java.lang.String> packageNames = getRemainingArgs();
                if (packageNames.isEmpty()) {
                    pw.println("Error: package name not specified");
                    return 1;
                }
                pw.println("Distracting restrictions state for user " + userId);
                int translatedUserId = translateUserId(userId, -10000, "get-distracting");
                java.lang.String[] packages = (java.lang.String[]) packageNames.toArray(new java.lang.String[0]);
                int[] res = this.mPm.getDistractingPackageRestrictionsAsUser(packages, translatedUserId);
                for (int i = 0; i < res.length; i++) {
                    int state = res[i];
                    if (state == -1) {
                        pw.println(packages[i] + " not found ...");
                    } else {
                        pw.println(packages[i] + "  state: " + stateToString(state));
                    }
                }
                return 0;
            }
        }
    }

    private static java.lang.String stateToString(int flag) {
        switch (flag) {
            case 0:
                return "NONE";
            case 1:
                return "HIDE_FROM_SUGGESTIONS";
            case 2:
                return "HIDE_NOTIFICATIONS";
            default:
                return "UNKNOWN";
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSuspend(boolean r23, int r24) {
        /*
            Method dump skipped, instruction units count: 464
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.runSuspend(boolean, int):int");
    }

    private int runGrantRevokePermission(boolean grant) throws java.lang.Exception {
        java.lang.String opt;
        java.util.List<android.content.pm.PackageInfo> packageInfos;
        java.util.List<java.lang.String> permissions;
        java.lang.String opt2;
        com.android.server.pm.PackageManagerShellCommand packageManagerShellCommand = this;
        boolean allPermissions = false;
        int userId = 0;
        while (true) {
            java.lang.String nextOption = getNextOption();
            opt = nextOption;
            if (nextOption == null) {
                break;
            }
            if (opt.equals("--user")) {
                userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
            }
            if (opt.equals("--all-permissions")) {
                allPermissions = true;
            }
        }
        java.lang.String pkg = getNextArg();
        if (!allPermissions && pkg == null) {
            getErrPrintWriter().println("Error: no package specified");
            return 1;
        }
        java.lang.String perm = getNextArg();
        if (!allPermissions && perm == null) {
            getErrPrintWriter().println("Error: no permission specified");
            return 1;
        }
        if (allPermissions && perm != null) {
            getErrPrintWriter().println("Error: permission specified but not expected");
            return 1;
        }
        android.os.UserHandle translatedUser = android.os.UserHandle.of(packageManagerShellCommand.translateUserId(userId, -10000, "runGrantRevokePermission"));
        android.content.pm.PackageManager pm = packageManagerShellCommand.mContext.createContextAsUser(translatedUser, 0).getPackageManager();
        if (pkg == null) {
            packageInfos = pm.getInstalledPackages(4096);
        } else {
            try {
                java.util.List<android.content.pm.PackageInfo> packageInfos2 = java.util.Collections.singletonList(pm.getPackageInfo(pkg, 4096));
                packageInfos = packageInfos2;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                getErrPrintWriter().println("Error: package not found");
                getOutPrintWriter().println("Failure [package not found]");
                return 1;
            }
        }
        for (android.content.pm.PackageInfo packageInfo : packageInfos) {
            java.util.List<java.lang.String> permissions2 = java.util.Collections.singletonList(perm);
            if (!allPermissions) {
                permissions = permissions2;
            } else {
                java.util.List<java.lang.String> permissions3 = packageManagerShellCommand.getRequestedRuntimePermissions(packageInfo);
                permissions = permissions3;
            }
            for (java.lang.String permission : permissions) {
                int userId2 = userId;
                if (grant) {
                    try {
                        opt2 = opt;
                    } catch (java.lang.Exception e2) {
                        e = e2;
                        opt2 = opt;
                    }
                    try {
                        packageManagerShellCommand.mPermissionManager.grantRuntimePermission(packageInfo.packageName, permission, translatedUser);
                    } catch (java.lang.Exception e3) {
                        e = e3;
                        if (allPermissions) {
                            android.util.Slog.w(TAG, "Could not grant permission " + permission, e);
                        } else {
                            throw e;
                        }
                    }
                } else {
                    opt2 = opt;
                    try {
                        packageManagerShellCommand.mPermissionManager.revokeRuntimePermission(packageInfo.packageName, permission, translatedUser, (java.lang.String) null);
                    } catch (java.lang.Exception e4) {
                        if (allPermissions) {
                            android.util.Slog.w(TAG, "Could not grant permission " + permission, e4);
                        } else {
                            throw e4;
                        }
                    }
                }
                packageManagerShellCommand = this;
                userId = userId2;
                opt = opt2;
            }
            packageManagerShellCommand = this;
        }
        return 0;
    }

    private java.util.List<java.lang.String> getRequestedRuntimePermissions(android.content.pm.PackageInfo info) throws android.content.pm.PackageManager.NameNotFoundException {
        if (info.requestedPermissions == null) {
            return new java.util.ArrayList();
        }
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        for (java.lang.String permission : info.requestedPermissions) {
            android.content.pm.PermissionInfo pi = null;
            try {
                pi = pm.getPermissionInfo(permission, 0);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
            if (pi != null && pi.getProtection() == 1) {
                result.add(permission);
            }
        }
        return result;
    }

    private int runResetPermissions() throws android.os.RemoteException {
        this.mLegacyPermissionManager.resetRuntimePermissions();
        return 0;
    }

    private int setOrClearPermissionFlags(boolean setFlags) {
        int userId = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt == null) {
                break;
            }
            if (opt.equals("--user")) {
                userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
            }
        }
        java.lang.String pkg = getNextArg();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package specified");
            return 1;
        }
        java.lang.String perm = getNextArg();
        if (perm == null) {
            getErrPrintWriter().println("Error: no permission specified");
            return 1;
        }
        java.lang.String flagName = getNextArg();
        if (flagName != null) {
            int flagMask = 0;
            java.lang.String flagName2 = flagName;
            while (flagName2 != null) {
                if (!SUPPORTED_PERMISSION_FLAGS.containsKey(flagName2)) {
                    getErrPrintWriter().println("Error: specified flag " + flagName2 + " is not one of " + SUPPORTED_PERMISSION_FLAGS_LIST);
                    return 1;
                }
                flagMask |= SUPPORTED_PERMISSION_FLAGS.get(flagName2).intValue();
                flagName2 = getNextArg();
            }
            android.os.UserHandle translatedUser = android.os.UserHandle.of(translateUserId(userId, -10000, "runGrantRevokePermission"));
            int flagSet = setFlags ? flagMask : 0;
            this.mPermissionManager.updatePermissionFlags(pkg, perm, flagMask, flagSet, translatedUser);
            return 0;
        }
        getErrPrintWriter().println("Error: no permission flags specified");
        return 1;
    }

    private int runSetPermissionEnforced() throws android.os.RemoteException {
        java.lang.String permission = getNextArg();
        if (permission == null) {
            getErrPrintWriter().println("Error: no permission specified");
            return 1;
        }
        java.lang.String enforcedRaw = getNextArg();
        if (enforcedRaw == null) {
            getErrPrintWriter().println("Error: no enforcement specified");
            return 1;
        }
        return 0;
    }

    private boolean isVendorApp(java.lang.String pkg) {
        try {
            android.content.pm.PackageInfo info = this.mInterface.getPackageInfo(pkg, 4194304L, 0);
            if (info != null) {
                return info.applicationInfo.isVendor();
            }
            return false;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    private boolean isProductApp(java.lang.String pkg) {
        try {
            android.content.pm.PackageInfo info = this.mInterface.getPackageInfo(pkg, 4194304L, 0);
            if (info != null) {
                return info.applicationInfo.isProduct();
            }
            return false;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    private boolean isSystemExtApp(java.lang.String pkg) {
        try {
            android.content.pm.PackageInfo info = this.mInterface.getPackageInfo(pkg, 4194304L, 0);
            if (info != null) {
                return info.applicationInfo.isSystemExt();
            }
            return false;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    private java.lang.String getApexPackageNameContainingPackage(java.lang.String pkg) {
        com.android.server.pm.ApexManager apexManager = com.android.server.pm.ApexManager.getInstance();
        return apexManager.getActiveApexPackageNameContainingPackage(pkg);
    }

    private boolean isApexApp(java.lang.String pkg) {
        return getApexPackageNameContainingPackage(pkg) != null;
    }

    private int runGetPrivappPermissions() {
        java.lang.String pkg = getNextArg();
        if (pkg != null) {
            getOutPrintWriter().println(getPrivAppPermissionsString(pkg, true));
            return 0;
        }
        getErrPrintWriter().println("Error: no package specified.");
        return 1;
    }

    private int runGetPrivappDenyPermissions() {
        java.lang.String pkg = getNextArg();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package specified.");
            return 1;
        }
        getOutPrintWriter().println(getPrivAppPermissionsString(pkg, false));
        return 0;
    }

    private java.lang.String getPrivAppPermissionsString(java.lang.String packageName, boolean allowed) {
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> privAppPermissions;
        com.android.server.pm.permission.PermissionAllowlist permissionAllowlist = com.android.server.SystemConfig.getInstance().getPermissionAllowlist();
        if (isVendorApp(packageName)) {
            privAppPermissions = permissionAllowlist.getVendorPrivilegedAppAllowlist();
        } else if (isProductApp(packageName)) {
            privAppPermissions = permissionAllowlist.getProductPrivilegedAppAllowlist();
        } else if (isSystemExtApp(packageName)) {
            privAppPermissions = permissionAllowlist.getSystemExtPrivilegedAppAllowlist();
        } else if (isApexApp(packageName)) {
            java.lang.String moduleName = com.android.server.pm.ApexManager.getInstance().getApexModuleNameForPackageName(getApexPackageNameContainingPackage(packageName));
            privAppPermissions = permissionAllowlist.getApexPrivilegedAppAllowlists().get(moduleName);
        } else {
            privAppPermissions = permissionAllowlist.getPrivilegedAppAllowlist();
        }
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = privAppPermissions != null ? privAppPermissions.get(packageName) : null;
        if (permissions == null) {
            return "{}";
        }
        java.lang.StringBuilder result = new java.lang.StringBuilder("{");
        boolean isFirstPermission = true;
        int permissionsSize = permissions.size();
        for (int i = 0; i < permissionsSize; i++) {
            boolean permissionAllowed = permissions.valueAt(i).booleanValue();
            if (permissionAllowed == allowed) {
                if (isFirstPermission) {
                    isFirstPermission = false;
                } else {
                    result.append(", ");
                }
                java.lang.String permissionName = permissions.keyAt(i);
                result.append(permissionName);
            }
        }
        result.append("}");
        return result.toString();
    }

    private int runGetOemPermissions() {
        java.lang.String pkg = getNextArg();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package specified.");
            return 1;
        }
        java.util.Map<java.lang.String, java.lang.Boolean> oemPermissions = com.android.server.SystemConfig.getInstance().getPermissionAllowlist().getOemAppAllowlist().get(pkg);
        if (oemPermissions == null || oemPermissions.isEmpty()) {
            getOutPrintWriter().println("{}");
            return 0;
        }
        oemPermissions.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.pm.PackageManagerShellCommand$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$runGetOemPermissions$2((java.lang.String) obj, (java.lang.Boolean) obj2);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runGetOemPermissions$2(java.lang.String permission, java.lang.Boolean granted) {
        getOutPrintWriter().println(permission + " granted:" + granted);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runGetSignaturePermissionAllowlist() {
        /*
            Method dump skipped, instruction units count: 264
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.runGetSignaturePermissionAllowlist():int");
    }

    private int runGetSharedUidAllowlist() {
        android.util.ArrayMap<java.lang.String, java.lang.String> allowlist = com.android.server.SystemConfig.getInstance().getPackageToSharedUidAllowList();
        java.io.PrintWriter pw = getOutPrintWriter();
        int allowlistSize = allowlist.size();
        for (int allowlistIndex = 0; allowlistIndex < allowlistSize; allowlistIndex++) {
            java.lang.String packageName = allowlist.keyAt(allowlistIndex);
            java.lang.String sharedUserName = allowlist.valueAt(allowlistIndex);
            pw.print(packageName);
            pw.print(" ");
            pw.println(sharedUserName);
        }
        return 0;
    }

    private int runTrimCaches() throws android.os.RemoteException {
        long multiplier;
        long multiplier2;
        java.lang.String size;
        java.lang.String volumeUuid;
        java.lang.String size2 = getNextArg();
        if (size2 == null) {
            getErrPrintWriter().println("Error: no size specified");
            return 1;
        }
        int len = size2.length();
        char c = size2.charAt(len - 1);
        if (c >= '0' && c <= '9') {
            multiplier2 = 1;
            size = size2;
        } else {
            if (c == 'K' || c == 'k') {
                multiplier = 1024;
            } else if (c == 'M' || c == 'm') {
                multiplier = 1048576;
            } else if (c == 'G' || c == 'g') {
                multiplier = 1073741824;
            } else {
                getErrPrintWriter().println("Invalid suffix: " + c);
                return 1;
            }
            multiplier2 = multiplier;
            size = size2.substring(0, len - 1);
        }
        try {
            long sizeVal = java.lang.Long.parseLong(size) * multiplier2;
            java.lang.String volumeUuid2 = getNextArg();
            if (!"internal".equals(volumeUuid2)) {
                volumeUuid = volumeUuid2;
            } else {
                volumeUuid = null;
            }
            com.android.server.pm.PackageManagerShellCommand.ClearDataObserver obs = new com.android.server.pm.PackageManagerShellCommand.ClearDataObserver();
            this.mInterface.freeStorageAndNotify(volumeUuid, sizeVal, 2, obs);
            synchronized (obs) {
                while (!obs.finished) {
                    try {
                        obs.wait();
                    } catch (java.lang.InterruptedException e) {
                    }
                }
            }
            return 0;
        } catch (java.lang.NumberFormatException e2) {
            getErrPrintWriter().println("Error: expected number at: " + size);
            return 1;
        }
    }

    private static boolean isNumber(java.lang.String s) {
        try {
            java.lang.Integer.parseInt(s);
            return true;
        } catch (java.lang.NumberFormatException e) {
            return false;
        }
    }

    public int runCreateUser() throws java.lang.Throwable {
        java.lang.String userType;
        long j;
        boolean preCreateOnly = false;
        int userId = -1;
        java.lang.String userType2 = null;
        int flags = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt == null) {
                java.lang.String arg = getNextArg();
                if (arg == null && !preCreateOnly) {
                    getErrPrintWriter().println("Error: no user name specified.");
                    return 1;
                }
                if (arg != null && preCreateOnly) {
                    getErrPrintWriter().println("Warning: name is ignored for pre-created users");
                }
                android.content.pm.UserInfo info = null;
                android.os.IUserManager um = android.os.IUserManager.Stub.asInterface(android.os.ServiceManager.getService("user"));
                android.accounts.IAccountManager accm = android.accounts.IAccountManager.Stub.asInterface(android.os.ServiceManager.getService("account"));
                if (userType2 == null) {
                    java.lang.String userType3 = android.content.pm.UserInfo.getDefaultUserType(flags);
                    userType = userType3;
                } else {
                    userType = userType2;
                }
                android.os.Trace.traceBegin(262144L, "shell_runCreateUser");
                try {
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                } catch (android.os.ServiceSpecificException e) {
                    e = e;
                    j = 262144;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    j = 262144;
                }
                try {
                } catch (android.os.ServiceSpecificException e2) {
                    e = e2;
                    j = 262144;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    j = 262144;
                    android.os.Trace.traceEnd(j);
                    throw th;
                }
                if (android.os.UserManager.isUserTypeRestricted(userType)) {
                    int parentUserId = userId >= 0 ? userId : 0;
                    info = um.createRestrictedProfileWithThrow(arg, parentUserId);
                    accm.addSharedAccountsFromParentUser(parentUserId, userId, android.os.Process.myUid() == 0 ? "root" : "com.android.shell");
                    j = 262144;
                } else if (userId < 0) {
                    info = preCreateOnly ? um.preCreateUserWithThrow(userType) : um.createUserWithThrow(arg, userType, flags);
                    j = 262144;
                } else {
                    j = 262144;
                    try {
                        info = um.createProfileForUserWithThrow(arg, userType, flags, userId, (java.lang.String[]) null);
                    } catch (android.os.ServiceSpecificException e3) {
                        e = e3;
                        getErrPrintWriter().println("Error: " + e);
                    }
                }
                android.os.Trace.traceEnd(j);
                if (info != null) {
                    getOutPrintWriter().println("Success: created user id " + info.id);
                    return 0;
                }
                getErrPrintWriter().println("Error: couldn't create User.");
                return 1;
            }
            java.lang.String newUserType = null;
            if ("--profileOf".equals(opt)) {
                userId = translateUserId(android.os.UserHandle.parseUserArg(getNextArgRequired()), -1, "runCreateUser");
            } else if ("--managed".equals(opt)) {
                newUserType = "android.os.usertype.profile.MANAGED";
            } else if ("--restricted".equals(opt)) {
                newUserType = "android.os.usertype.full.RESTRICTED";
            } else if ("--guest".equals(opt)) {
                newUserType = "android.os.usertype.full.GUEST";
            } else if ("--demo".equals(opt)) {
                newUserType = "android.os.usertype.full.DEMO";
            } else if ("--ephemeral".equals(opt)) {
                flags |= 256;
            } else if ("--for-testing".equals(opt)) {
                flags |= 32768;
            } else if ("--pre-create-only".equals(opt)) {
                preCreateOnly = true;
            } else {
                if (!"--user-type".equals(opt)) {
                    getErrPrintWriter().println("Error: unknown option " + opt);
                    return 1;
                }
                newUserType = getNextArgRequired();
            }
            if (newUserType != null) {
                if (userType2 != null && !userType2.equals(newUserType)) {
                    getErrPrintWriter().println("Error: more than one user type was specified (" + userType2 + " and " + newUserType + ")");
                    return 1;
                }
                userType2 = newUserType;
            }
        }
    }

    public int runRemoveUser() throws android.os.RemoteException {
        boolean setEphemeralIfInUse = false;
        boolean wait = false;
        while (true) {
            java.lang.String arg = getNextOption();
            byte b = 0;
            if (arg != null) {
                switch (arg.hashCode()) {
                    case -1095309356:
                        if (!arg.equals("--set-ephemeral-if-in-use")) {
                            b = -1;
                        }
                        break;
                    case 1514:
                        b = !arg.equals("-w") ? (byte) -1 : (byte) 2;
                        break;
                    case 1333511957:
                        b = !arg.equals("--wait") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        setEphemeralIfInUse = true;
                        break;
                    case 1:
                    case 2:
                        wait = true;
                        break;
                    default:
                        getErrPrintWriter().println("Error: unknown option: " + arg);
                        return -1;
                }
            } else {
                java.lang.String arg2 = getNextArg();
                if (arg2 == null) {
                    getErrPrintWriter().println("Error: no user id specified.");
                    return 1;
                }
                int userId = android.os.UserHandle.parseUserArg(arg2);
                android.os.IUserManager um = android.os.IUserManager.Stub.asInterface(android.os.ServiceManager.getService("user"));
                if (setEphemeralIfInUse) {
                    return removeUserWhenPossible(um, userId);
                }
                boolean success = wait ? removeUserAndWait(um, userId) : removeUser(um, userId);
                if (!success) {
                    return 1;
                }
                getOutPrintWriter().println("Success: removed user");
                return 0;
            }
        }
    }

    private boolean removeUser(android.os.IUserManager um, int userId) throws android.os.RemoteException {
        android.util.Slog.i(TAG, "Removing user " + userId);
        if (um.removeUser(userId)) {
            return true;
        }
        getErrPrintWriter().println("Error: couldn't remove user id " + userId);
        return false;
    }

    private boolean removeUserAndWait(android.os.IUserManager um, final int userId) throws android.os.RemoteException {
        android.util.Slog.i(TAG, "Removing (and waiting for completion) user " + userId);
        final java.util.concurrent.CountDownLatch waitLatch = new java.util.concurrent.CountDownLatch(1);
        com.android.server.pm.UserManagerInternal.UserLifecycleListener listener = new com.android.server.pm.UserManagerInternal.UserLifecycleListener() { // from class: com.android.server.pm.PackageManagerShellCommand.4
            @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
            public void onUserRemoved(android.content.pm.UserInfo user) {
                if (userId == user.id) {
                    waitLatch.countDown();
                }
            }
        };
        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        umi.addUserLifecycleListener(listener);
        try {
            if (!um.removeUser(userId)) {
                getErrPrintWriter().println("Error: couldn't remove user id " + userId);
                return false;
            }
            boolean awaitSuccess = waitLatch.await(10L, java.util.concurrent.TimeUnit.MINUTES);
            if (awaitSuccess) {
                return true;
            }
            getErrPrintWriter().printf("Error: Remove user %d timed out\n", java.lang.Integer.valueOf(userId));
            return false;
        } catch (java.lang.InterruptedException e) {
            getErrPrintWriter().printf("Error: Remove user %d wait interrupted: %s\n", java.lang.Integer.valueOf(userId), e);
            java.lang.Thread.currentThread().interrupt();
            return false;
        } finally {
            umi.removeUserLifecycleListener(listener);
        }
    }

    private int removeUserWhenPossible(android.os.IUserManager um, int userId) throws android.os.RemoteException {
        android.util.Slog.i(TAG, "Removing " + userId + " or set as ephemeral if in use.");
        int result = um.removeUserWhenPossible(userId, false);
        switch (result) {
            case -5:
                getErrPrintWriter().printf("Error: user %d is a permanent admin main user\n", java.lang.Integer.valueOf(userId));
                return 1;
            case 0:
                getOutPrintWriter().printf("Success: user %d removed\n", java.lang.Integer.valueOf(userId));
                return 0;
            case 1:
                getOutPrintWriter().printf("Success: user %d set as ephemeral\n", java.lang.Integer.valueOf(userId));
                return 0;
            case 2:
                getOutPrintWriter().printf("Success: user %d is already being removed\n", java.lang.Integer.valueOf(userId));
                return 0;
            default:
                getErrPrintWriter().printf("Error: couldn't remove or mark ephemeral user id %d\n", java.lang.Integer.valueOf(userId));
                return 1;
        }
    }

    private int runMarkGuestForDeletion() throws android.os.RemoteException {
        java.lang.String arg = getNextArg();
        if (arg == null) {
            getErrPrintWriter().println("Error: no user id specified.");
            return 1;
        }
        int userId = resolveUserId(android.os.UserHandle.parseUserArg(arg));
        android.os.IUserManager um = android.os.IUserManager.Stub.asInterface(android.os.ServiceManager.getService("user"));
        if (!um.markGuestForDeletion(userId)) {
            getErrPrintWriter().println("Error: could not mark guest for deletion");
            return 1;
        }
        return 0;
    }

    private int runRenameUser() throws android.os.RemoteException {
        java.lang.String arg = getNextArg();
        if (arg == null) {
            getErrPrintWriter().println("Error: no user id specified.");
            return 1;
        }
        int userId = resolveUserId(android.os.UserHandle.parseUserArg(arg));
        java.lang.String name = getNextArg();
        if (name == null) {
            android.util.Slog.i(TAG, "Resetting name of user " + userId);
        } else {
            android.util.Slog.i(TAG, "Renaming user " + userId + " to '" + name + "'");
        }
        android.os.IUserManager um = android.os.IUserManager.Stub.asInterface(android.os.ServiceManager.getService("user"));
        um.setUserName(userId, name);
        return 0;
    }

    public int runSetUserRestriction() throws android.os.RemoteException {
        boolean value;
        int userId = 0;
        java.lang.String opt = getNextOption();
        if (opt != null && "--user".equals(opt)) {
            userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
        }
        java.lang.String restriction = getNextArg();
        java.lang.String arg = getNextArg();
        if ("1".equals(arg)) {
            value = true;
        } else if ("0".equals(arg)) {
            value = false;
        } else {
            getErrPrintWriter().println("Error: valid value not specified");
            return 1;
        }
        int translatedUserId = translateUserId(userId, -10000, "runSetUserRestriction");
        android.os.IUserManager um = android.os.IUserManager.Stub.asInterface(android.os.ServiceManager.getService("user"));
        um.setUserRestriction(restriction, value, translatedUserId);
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runGetUserRestriction() throws android.os.RemoteException {
        /*
            r10 = this;
            java.io.PrintWriter r0 = r10.getOutPrintWriter()
            r1 = 0
            r2 = 0
        L6:
            java.lang.String r3 = r10.getNextOption()
            r4 = r3
            r5 = 0
            if (r3 == 0) goto L60
            int r3 = r4.hashCode()
            switch(r3) {
                case 42995713: goto L1f;
                case 1333469547: goto L16;
                default: goto L15;
            }
        L15:
            goto L29
        L16:
            java.lang.String r3 = "--user"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L15
            goto L2a
        L1f:
            java.lang.String r3 = "--all"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L15
            r5 = 1
            goto L2a
        L29:
            r5 = -1
        L2a:
            switch(r5) {
                case 0: goto L56;
                case 1: goto L46;
                default: goto L2d;
            }
        L2d:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Unknown option "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r4)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            throw r3
        L46:
            r2 = 1
            java.lang.String r3 = r10.getNextArg()
            if (r3 != 0) goto L4e
            goto L5f
        L4e:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Argument unexpected after \"--all\""
            r3.<init>(r5)
            throw r3
        L56:
            java.lang.String r3 = r10.getNextArgRequired()
            int r1 = android.os.UserHandle.parseUserArg(r3)
        L5f:
            goto L6
        L60:
            r3 = -10000(0xffffffffffffd8f0, float:NaN)
            java.lang.String r6 = "runGetUserRestriction"
            int r3 = r10.translateUserId(r1, r3, r6)
            java.lang.String r6 = "user"
            android.os.IBinder r6 = android.os.ServiceManager.getService(r6)
            android.os.IUserManager r6 = android.os.IUserManager.Stub.asInterface(r6)
            if (r2 == 0) goto L89
            android.os.Bundle r7 = r6.getUserRestrictions(r3)
            java.lang.String r8 = "All restrictions:"
            r0.println(r8)
            java.lang.String r8 = r7.toString()
            r0.println(r8)
            goto L9c
        L89:
            java.lang.String r7 = r10.getNextArg()
            if (r7 == 0) goto La5
            java.lang.String r8 = r10.getNextArg()
            if (r8 != 0) goto L9d
            boolean r9 = r6.hasUserRestriction(r7, r3)
            r0.println(r9)
        L9c:
            return r5
        L9d:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.String r9 = "Argument unexpected after restriction key"
            r5.<init>(r9)
            throw r5
        La5:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.String r8 = "No restriction key specified"
            r5.<init>(r8)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.runGetUserRestriction():int");
    }

    public int runSupportsMultipleUsers() {
        getOutPrintWriter().println("Is multiuser supported: " + android.os.UserManager.supportsMultipleUsers());
        return 0;
    }

    public int runGetMaxUsers() {
        getOutPrintWriter().println("Maximum supported users: " + android.os.UserManager.getMaxSupportedUsers());
        return 0;
    }

    public int runGetMaxRunningUsers() {
        android.app.ActivityManagerInternal activityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        getOutPrintWriter().println("Maximum supported running users: " + activityManagerInternal.getMaxRunningUsers());
        return 0;
    }

    private static class InstallParams {
        java.lang.String installerPackageName;
        android.content.pm.PackageInstaller.SessionParams sessionParams;
        long stagedReadyTimeoutMs;
        int userId;

        private InstallParams() {
            this.userId = -1;
            this.stagedReadyTimeoutMs = 60000L;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x02b5, code lost:
    
        throw new java.lang.IllegalArgumentException(r0 + " is not a valid rollback impact level.");
     */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0217  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.pm.PackageManagerShellCommand.InstallParams makeInstallParams(java.util.Set<java.lang.String> r17) {
        /*
            Method dump skipped, instruction units count: 1416
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.makeInstallParams(java.util.Set):com.android.server.pm.PackageManagerShellCommand$InstallParams");
    }

    private int runSetHomeActivity() {
        java.lang.String pkgName;
        android.app.role.RoleManager roleManager;
        android.os.UserHandle userHandleOf;
        java.util.concurrent.Executor executor;
        java.io.PrintWriter pw = getOutPrintWriter();
        int userId = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1333469547:
                        if (!opt.equals("--user")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                java.lang.String component = getNextArg();
                if (component.indexOf(47) < 0) {
                    pkgName = component;
                } else {
                    android.content.ComponentName componentName = component != null ? android.content.ComponentName.unflattenFromString(component) : null;
                    if (componentName == null) {
                        pw.println("Error: invalid component name");
                        return 1;
                    }
                    pkgName = componentName.getPackageName();
                }
                int translatedUserId = translateUserId(userId, -10000, "runSetHomeActivity");
                final java.util.concurrent.CompletableFuture<java.lang.Boolean> future = new java.util.concurrent.CompletableFuture<>();
                try {
                    roleManager = (android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class);
                    userHandleOf = android.os.UserHandle.of(translatedUserId);
                    executor = com.android.server.FgThread.getExecutor();
                    java.util.Objects.requireNonNull(future);
                } catch (java.lang.Exception e) {
                    e = e;
                }
                try {
                    roleManager.addRoleHolderAsUser("android.app.role.HOME", pkgName, 0, userHandleOf, executor, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerShellCommand$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            future.complete((java.lang.Boolean) obj);
                        }
                    });
                    boolean success = future.get().booleanValue();
                    if (success) {
                        pw.println("Success");
                        return 0;
                    }
                    pw.println("Error: Failed to set default home.");
                    return 1;
                } catch (java.lang.Exception e2) {
                    e = e2;
                    pw.println(e.toString());
                    return 1;
                }
            }
        }
    }

    private int runSetInstaller() throws android.os.RemoteException {
        java.lang.String targetPackage = getNextArg();
        java.lang.String installerPackageName = getNextArg();
        if (targetPackage == null || installerPackageName == null) {
            getErrPrintWriter().println("Must provide both target and installer package names");
            return 1;
        }
        this.mInterface.setInstallerPackageName(targetPackage, installerPackageName);
        getOutPrintWriter().println("Success");
        return 0;
    }

    private int runGetInstantAppResolver() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            android.content.ComponentName instantAppsResolver = this.mInterface.getInstantAppResolverComponent();
            if (instantAppsResolver == null) {
                return 1;
            }
            pw.println(instantAppsResolver.flattenToString());
            return 0;
        } catch (java.lang.Exception e) {
            pw.println(e.toString());
            return 1;
        }
    }

    private int runHasFeature() {
        int version;
        java.io.PrintWriter err = getErrPrintWriter();
        java.lang.String featureName = getNextArg();
        if (featureName == null) {
            err.println("Error: expected FEATURE name");
            return 1;
        }
        java.lang.String versionString = getNextArg();
        if (versionString == null) {
            version = 0;
        } else {
            try {
                version = java.lang.Integer.parseInt(versionString);
            } catch (android.os.RemoteException e) {
                err.println(e.toString());
                return 1;
            } catch (java.lang.NumberFormatException e2) {
                err.println("Error: illegal version number " + versionString);
                return 1;
            }
        }
        boolean hasFeature = this.mInterface.hasSystemFeature(featureName, version);
        getOutPrintWriter().println(hasFeature);
        if (!hasFeature) {
            return 1;
        }
        return 0;
    }

    private int runDump() {
        java.lang.String pkg = getNextArg();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package specified");
            return 1;
        }
        android.app.ActivityManager.dumpPackageStateStatic(getOutFileDescriptor(), pkg);
        return 0;
    }

    private int runDumpPackage() {
        java.lang.String pkg = getNextArg();
        if (pkg == null) {
            getErrPrintWriter().println("Error: no package specified");
            return 1;
        }
        try {
            this.mInterface.dump(getOutFileDescriptor(), new java.lang.String[]{pkg});
            return 0;
        } catch (java.lang.Throwable e) {
            java.io.PrintWriter pw = getErrPrintWriter();
            pw.println("Failure dumping service:");
            e.printStackTrace(pw);
            pw.flush();
            return 0;
        }
    }

    private int runSetHarmfulAppWarning() throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                int translatedUserId = translateUserId(userId, -10000, "runSetHarmfulAppWarning");
                java.lang.String packageName = getNextArgRequired();
                java.lang.String warning = getNextArg();
                this.mInterface.setHarmfulAppWarning(packageName, warning, translatedUserId);
                return 0;
            }
        }
    }

    private int runGetHarmfulAppWarning() throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                int translatedUserId = translateUserId(userId, -10000, "runGetHarmfulAppWarning");
                java.lang.String packageName = getNextArgRequired();
                java.lang.CharSequence warning = this.mInterface.getHarmfulAppWarning(packageName, translatedUserId);
                if (!android.text.TextUtils.isEmpty(warning)) {
                    getOutPrintWriter().println(warning);
                    return 0;
                }
                return 1;
            }
        }
    }

    private int runSetSilentUpdatesPolicy() {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String installerPackageName = null;
        java.lang.Long throttleTimeInSeconds = null;
        boolean reset = false;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -1615291473:
                        b = !opt.equals("--reset") ? (byte) -1 : (byte) 2;
                        break;
                    case 771584496:
                        if (!opt.equals("--throttle-time")) {
                            b = -1;
                        }
                        break;
                    case 1002172770:
                        b = !opt.equals("--allow-unlimited-silent-updates") ? (byte) -1 : (byte) 0;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        installerPackageName = getNextArgRequired();
                        break;
                    case 1:
                        throttleTimeInSeconds = java.lang.Long.valueOf(java.lang.Long.parseLong(getNextArgRequired()));
                        break;
                    case 2:
                        reset = true;
                        break;
                    default:
                        pw.println("Error: Unknown option: " + opt);
                        return -1;
                }
            } else {
                if (throttleTimeInSeconds != null && throttleTimeInSeconds.longValue() < 0) {
                    pw.println("Error: Invalid value for \"--throttle-time\":" + throttleTimeInSeconds);
                    return -1;
                }
                try {
                    android.content.pm.IPackageInstaller installer = this.mInterface.getPackageInstaller();
                    if (reset) {
                        installer.setAllowUnlimitedSilentUpdates((java.lang.String) null);
                        installer.setSilentUpdatesThrottleTime(-1L);
                    } else {
                        if (installerPackageName != null) {
                            installer.setAllowUnlimitedSilentUpdates(installerPackageName);
                        }
                        if (throttleTimeInSeconds != null) {
                            installer.setSilentUpdatesThrottleTime(throttleTimeInSeconds.longValue());
                        }
                    }
                    return 1;
                } catch (android.os.RemoteException e) {
                    pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
                    return -1;
                }
            }
        }
    }

    private int runGetAppMetadata() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.GET_APP_METADATA", "getAppMetadataFd");
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String pkgName = getNextArgRequired();
        try {
            android.os.ParcelFileDescriptor pfd = this.mInterface.getAppMetadataFd(pkgName, this.mContext.getUserId());
            if (pfd != null) {
                try {
                    java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd)));
                    while (br.ready()) {
                        try {
                            pw.println(br.readLine());
                        } finally {
                        }
                    }
                    br.close();
                    return 1;
                } catch (java.io.IOException e) {
                    pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
                    return -1;
                }
            }
            return 1;
        } catch (android.os.RemoteException e2) {
            pw.println("Failure [" + e2.getClass().getName() + " - " + e2.getMessage() + "]");
            return -1;
        }
    }

    private int runWaitForHandler(boolean forBackgroundHandler) {
        java.io.PrintWriter pw = getOutPrintWriter();
        long timeoutMillis = 60000;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt == null) {
                if (timeoutMillis <= 0) {
                    pw.println("Error: --timeout value must be positive: " + timeoutMillis);
                    return -1;
                }
                try {
                    boolean success = this.mInterface.waitForHandler(timeoutMillis, forBackgroundHandler);
                    if (success) {
                        pw.println("Success");
                        return 0;
                    }
                    pw.println("Timeout. PackageManager handlers are still busy.");
                    return -1;
                } catch (android.os.RemoteException e) {
                    pw.println("Failure [" + e.getClass().getName() + " - " + e.getMessage() + "]");
                    return -1;
                }
            }
            switch (opt.hashCode()) {
                case 72070081:
                    if (!opt.equals("--timeout")) {
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    timeoutMillis = java.lang.Long.parseLong(getNextArgRequired());
                    break;
                default:
                    pw.println("Error: Unknown option: " + opt);
                    return -1;
            }
        }
    }

    private int runArtServiceCommand() {
        try {
            android.os.ParcelFileDescriptor in = android.os.ParcelFileDescriptor.dup(getInFileDescriptor());
            try {
                android.os.ParcelFileDescriptor out = android.os.ParcelFileDescriptor.dup(getOutFileDescriptor());
                try {
                    android.os.ParcelFileDescriptor err = android.os.ParcelFileDescriptor.dup(getErrFileDescriptor());
                    try {
                        int iHandleShellCommand = ((com.android.server.art.ArtManagerLocal) com.android.server.LocalManagerRegistry.getManagerOrThrow(com.android.server.art.ArtManagerLocal.class)).handleShellCommand(getTarget(), in, out, err, getAllArgs());
                        if (err != null) {
                            err.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        return iHandleShellCommand;
                    } finally {
                    }
                } finally {
                }
            } catch (java.lang.Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (com.android.server.LocalManagerRegistry.ManagerNotFoundException e) {
            java.io.PrintWriter epw = getErrPrintWriter();
            epw.println("ART Service is not ready. Please try again later");
            return -1;
        } catch (java.io.IOException e2) {
            throw new java.lang.IllegalStateException(e2);
        } catch (java.lang.IllegalStateException e3) {
            if (android.os.Build.OPLUS_64BIT_ONLY_CHIP && e3.getMessage().contains("Unsupported isa 'arm'")) {
                android.util.Slog.w(TAG, "Dexopt with art service is conflict with hbt_translator");
                return 0;
            }
            throw e3;
        }
    }

    private static java.lang.String checkAbiArgument(java.lang.String abi) {
        if (android.text.TextUtils.isEmpty(abi)) {
            throw new java.lang.IllegalArgumentException("Missing ABI argument");
        }
        if (STDIN_PATH.equals(abi)) {
            return abi;
        }
        java.lang.String[] supportedAbis = (android.os.Build.MTK_HBT_ON_64BIT_ONLY_CHIP || android.os.Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP) ? android.os.Build.MTK_HBT_SUPPORTED_ABIS : android.os.Build.SUPPORTED_ABIS;
        for (java.lang.String supportedAbi : supportedAbis) {
            if (supportedAbi.equals(abi)) {
                return abi;
            }
        }
        throw new java.lang.IllegalArgumentException("ABI " + abi + " not supported on this device");
    }

    private int translateUserId(int userId, int allUserId, java.lang.String logContext) {
        boolean allowAll = allUserId != -10000;
        int translatedUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, allowAll, true, logContext, "pm command");
        return translatedUserId == -1 ? allUserId : translatedUserId;
    }

    private int doCreateSession(android.content.pm.PackageInstaller.SessionParams params, java.lang.String installerPackageName, int userId) throws android.os.RemoteException {
        if (userId == -1) {
            params.installFlags |= 64;
        }
        int translatedUserId = translateUserId(userId, 0, "doCreateSession");
        int sessionId = this.mInterface.getPackageInstaller().createSession(params, installerPackageName, (java.lang.String) null, translatedUserId);
        return sessionId;
    }

    private int doAddFiles(int sessionId, java.util.ArrayList<java.lang.String> args, long sessionSizeBytes, boolean isApex, boolean installArchived) throws java.lang.Exception {
        com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata metadata;
        long size;
        android.content.pm.PackageInstaller.Session session = null;
        try {
            try {
            } catch (java.lang.Throwable th) {
                e = th;
                libcore.io.IoUtils.closeQuietly(session);
                throw e;
            }
            try {
                session = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(sessionId));
                if (!args.isEmpty()) {
                    try {
                        if (!STDIN_PATH.equals(args.get(0))) {
                            for (java.lang.String arg : args) {
                                int delimLocation = arg.indexOf(58);
                                if (delimLocation == -1) {
                                    processArgForLocalFile(arg, session, installArchived);
                                } else {
                                    if (installArchived) {
                                        getOutPrintWriter().println("Error: can't install with size from STDIN for Archival install");
                                        libcore.io.IoUtils.closeQuietly(session);
                                        return 1;
                                    }
                                    if (processArgForStdin(arg, session) != 0) {
                                        libcore.io.IoUtils.closeQuietly(session);
                                        return 1;
                                    }
                                }
                            }
                            libcore.io.IoUtils.closeQuietly(session);
                            return 0;
                        }
                    } catch (java.io.IOException | java.lang.IllegalArgumentException e) {
                        e = e;
                        getErrPrintWriter().println("Failed to add file(s), reason: " + e);
                        getOutPrintWriter().println("Failure [failed to add file(s)]");
                        libcore.io.IoUtils.closeQuietly(session);
                        return 1;
                    }
                }
                java.lang.String name = "base" + RANDOM.nextInt() + "." + (isApex ? "apex" : "apk");
                if (installArchived) {
                    metadata = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forArchived(getArchivedPackage(STDIN_PATH, sessionSizeBytes));
                    size = -1;
                } else {
                    metadata = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forStdIn(name);
                    size = sessionSizeBytes;
                }
                session.addFile(0, name, size, metadata.toByteArray(), null);
                libcore.io.IoUtils.closeQuietly(session);
                return 0;
            } catch (java.io.IOException | java.lang.IllegalArgumentException e2) {
                e = e2;
                getErrPrintWriter().println("Failed to add file(s), reason: " + e);
                getOutPrintWriter().println("Failure [failed to add file(s)]");
                libcore.io.IoUtils.closeQuietly(session);
                return 1;
            } catch (java.lang.Throwable th2) {
                e = th2;
                libcore.io.IoUtils.closeQuietly(session);
                throw e;
            }
        } catch (java.io.IOException | java.lang.IllegalArgumentException e3) {
            e = e3;
        } catch (java.lang.Throwable th3) {
            e = th3;
        }
    }

    private int processArgForStdin(java.lang.String arg, android.content.pm.PackageInstaller.Session session) {
        java.lang.String fileId;
        com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata metadata;
        java.lang.String[] fileDesc = arg.split(":");
        byte[] signature = null;
        int streamingVersion = 0;
        try {
            if (fileDesc.length < 2) {
                getErrPrintWriter().println("Must specify file name and size");
                return 1;
            }
            java.lang.String name = fileDesc[0];
            long sizeBytes = java.lang.Long.parseUnsignedLong(fileDesc[1]);
            if (fileDesc.length > 2 && !android.text.TextUtils.isEmpty(fileDesc[2])) {
                fileId = fileDesc[2];
            } else {
                fileId = name;
            }
            if (fileDesc.length > 3) {
                signature = java.util.Base64.getDecoder().decode(fileDesc[3]);
            }
            if (fileDesc.length > 4 && ((streamingVersion = java.lang.Integer.parseUnsignedInt(fileDesc[4])) < 0 || streamingVersion > 1)) {
                getErrPrintWriter().println("Unsupported streaming version: " + streamingVersion);
                return 1;
            }
            if (android.text.TextUtils.isEmpty(name)) {
                getErrPrintWriter().println("Empty file name in: " + arg);
                return 1;
            }
            if (signature != null) {
                com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata metadata2 = streamingVersion == 0 ? com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forDataOnlyStreaming(fileId) : com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forStreaming(fileId);
                try {
                    if (signature.length > 0 && android.os.incremental.V4Signature.readFrom(signature) == null) {
                        getErrPrintWriter().println("V4 signature is invalid in: " + arg);
                        return 1;
                    }
                    metadata = metadata2;
                } catch (java.lang.Exception e) {
                    getErrPrintWriter().println("V4 signature is invalid: " + e + " in " + arg);
                    return 1;
                }
            } else {
                com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata metadata3 = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forStdIn(fileId);
                metadata = metadata3;
            }
            session.addFile(0, name, sizeBytes, metadata.toByteArray(), signature);
            return 0;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Unable to parse file parameters: " + arg + ", reason: " + e2);
            return 1;
        }
    }

    private long getFileStatSize(java.io.File file) {
        android.os.ParcelFileDescriptor pfd = openFileForSystem(file.getPath(), com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
        if (pfd == null) {
            throw new java.lang.IllegalArgumentException("Error: Can't open file: " + file.getPath());
        }
        try {
            return pfd.getStatSize();
        } finally {
            libcore.io.IoUtils.closeQuietly(pfd);
        }
    }

    private android.content.pm.ArchivedPackageParcel getArchivedPackage(java.lang.String inPath, long sizeBytes) throws java.io.IOException, android.os.RemoteException {
        android.util.Pair<android.os.ParcelFileDescriptor, java.lang.Long> fdWithSize = openInFile(inPath, sizeBytes);
        if (fdWithSize.first == null) {
            throw new java.lang.IllegalArgumentException("Error: Can't open file: " + inPath);
        }
        android.os.ParcelFileDescriptor fd = (android.os.ParcelFileDescriptor) fdWithSize.first;
        int size = (int) ((java.lang.Long) fdWithSize.second).longValue();
        try {
            java.io.InputStream inStream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(fd);
            try {
                byte[] bytes = new byte[size];
                libcore.io.Streams.readFully(inStream, bytes);
                java.lang.String encoded = new java.lang.String(bytes);
                inStream.close();
                android.content.pm.ArchivedPackageParcel result = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.readArchivedPackageParcel(libcore.util.HexEncoding.decode(encoded));
                if (result == null) {
                    throw new java.lang.IllegalArgumentException("Error: Can't parse archived package from: " + inPath);
                }
                return result;
            } finally {
            }
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalArgumentException("Error: Can't load archived package from: " + inPath, e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0068  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void processArgForLocalFile(java.lang.String r16, android.content.pm.PackageInstaller.Session r17, boolean r18) throws java.io.IOException, android.os.RemoteException {
        /*
            r15 = this;
            r1 = r15
            r2 = r16
            java.io.File r0 = new java.io.File
            r0.<init>(r2)
            r3 = r0
            java.lang.String r11 = r3.getName()
            if (r18 == 0) goto L1e
            r4 = -1
            android.content.pm.ArchivedPackageParcel r0 = r15.getArchivedPackage(r2, r4)
            com.android.server.pm.PackageManagerShellCommandDataLoader$Metadata r0 = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forArchived(r0)
            r4 = 0
            r12 = r0
            r13 = r4
            goto L28
        L1e:
            com.android.server.pm.PackageManagerShellCommandDataLoader$Metadata r0 = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.forLocalFile(r2)
            long r4 = r15.getFileStatSize(r3)
            r12 = r0
            r13 = r4
        L28:
            r4 = 0
            if (r18 != 0) goto L68
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r5 = ".idsig"
            java.lang.StringBuilder r0 = r0.append(r5)
            java.lang.String r5 = r0.toString()
            java.lang.String r0 = "r"
            android.os.ParcelFileDescriptor r6 = r15.openFileForSystem(r5, r0)
            if (r6 == 0) goto L68
            android.os.incremental.V4Signature r0 = android.os.incremental.V4Signature.readFrom(r6)     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            byte[] r7 = r0.toByteArray()     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            r4 = r7
            libcore.io.IoUtils.closeQuietly(r6)
            r0 = r4
            goto L69
        L55:
            r0 = move-exception
            goto L64
        L57:
            r0 = move-exception
            java.lang.String r7 = "PackageManagerShellCommand"
            java.lang.String r8 = "V4 signature file exists but failed to be parsed."
            android.util.Slog.e(r7, r8, r0)     // Catch: java.lang.Throwable -> L55
            libcore.io.IoUtils.closeQuietly(r6)
            goto L68
        L64:
            libcore.io.IoUtils.closeQuietly(r6)
            throw r0
        L68:
            r0 = r4
        L69:
            r5 = 0
            byte[] r9 = r12.toByteArray()
            r4 = r17
            r6 = r11
            r7 = r13
            r10 = r0
            r4.addFile(r5, r6, r7, r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerShellCommand.processArgForLocalFile(java.lang.String, android.content.pm.PackageInstaller$Session, boolean):void");
    }

    private int doWriteSplits(int sessionId, java.util.ArrayList<java.lang.String> splitPaths, long sessionSizeBytes, boolean isApex) throws android.os.RemoteException {
        boolean multipleSplits = splitPaths.size() > 1;
        for (java.lang.String splitPath : splitPaths) {
            java.lang.String splitName = multipleSplits ? new java.io.File(splitPath).getName() : "base." + (isApex ? "apex" : "apk");
            if (doWriteSplit(sessionId, splitPath, sessionSizeBytes, splitName, false) != 0) {
                return 1;
            }
        }
        return 0;
    }

    private android.util.Pair<android.os.ParcelFileDescriptor, java.lang.Long> openInFile(java.lang.String inPath, long sizeBytes) throws java.io.IOException {
        android.os.ParcelFileDescriptor fd;
        if (!STDIN_PATH.equals(inPath) && inPath != null) {
            fd = openFileForSystem(inPath, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
            if (fd == null) {
                return android.util.Pair.create(null, -1L);
            }
            sizeBytes = fd.getStatSize();
            if (sizeBytes < 0) {
                fd.close();
                getErrPrintWriter().println("Unable to get size of: " + inPath);
                return android.util.Pair.create(null, -1L);
            }
        } else {
            fd = android.os.ParcelFileDescriptor.dup(getInFileDescriptor());
        }
        if (sizeBytes <= 0) {
            getErrPrintWriter().println("Error: must specify an APK size");
            return android.util.Pair.create(null, 1L);
        }
        return android.util.Pair.create(fd, java.lang.Long.valueOf(sizeBytes));
    }

    private int doWriteSplit(int sessionId, java.lang.String inPath, long sizeBytes, java.lang.String splitName, boolean logSuccess) throws java.io.IOException, android.os.RemoteException {
        android.content.pm.PackageInstaller.Session session = null;
        try {
            try {
                android.content.pm.PackageInstaller.Session session2 = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(sessionId));
                try {
                    java.io.PrintWriter pw = getOutPrintWriter();
                    try {
                        android.util.Pair<android.os.ParcelFileDescriptor, java.lang.Long> fdWithSize = openInFile(inPath, sizeBytes);
                        if (fdWithSize.first == null) {
                            long resultCode = ((java.lang.Long) fdWithSize.second).longValue();
                            int i = (int) resultCode;
                            libcore.io.IoUtils.closeQuietly(session2);
                            return i;
                        }
                        android.os.ParcelFileDescriptor fd = (android.os.ParcelFileDescriptor) fdWithSize.first;
                        long sizeBytes2 = ((java.lang.Long) fdWithSize.second).longValue();
                        try {
                            session2.write(splitName, 0L, sizeBytes2, fd);
                            if (logSuccess) {
                                pw.println("Success: streamed " + sizeBytes2 + " bytes");
                            }
                            libcore.io.IoUtils.closeQuietly(session2);
                            return 0;
                        } catch (java.io.IOException e) {
                            e = e;
                            session = session2;
                            try {
                                getErrPrintWriter().println("Error: failed to write; " + e.getMessage());
                                libcore.io.IoUtils.closeQuietly(session);
                                return 1;
                            } catch (java.lang.Throwable th) {
                                e = th;
                                libcore.io.IoUtils.closeQuietly(session);
                                throw e;
                            }
                        } catch (java.lang.Throwable th2) {
                            e = th2;
                            session = session2;
                            libcore.io.IoUtils.closeQuietly(session);
                            throw e;
                        }
                    } catch (java.io.IOException e2) {
                        e = e2;
                        session = session2;
                        getErrPrintWriter().println("Error: failed to write; " + e.getMessage());
                        libcore.io.IoUtils.closeQuietly(session);
                        return 1;
                    } catch (java.lang.Throwable th3) {
                        e = th3;
                        session = session2;
                        libcore.io.IoUtils.closeQuietly(session);
                        throw e;
                    }
                } catch (java.io.IOException e3) {
                    e = e3;
                } catch (java.lang.Throwable th4) {
                    e = th4;
                }
            } catch (java.io.IOException e4) {
                e = e4;
                getErrPrintWriter().println("Error: failed to write; " + e.getMessage());
                libcore.io.IoUtils.closeQuietly(session);
                return 1;
            } catch (java.lang.Throwable th5) {
                e = th5;
                libcore.io.IoUtils.closeQuietly(session);
                throw e;
            }
        } catch (java.io.IOException e5) {
            e = e5;
        } catch (java.lang.Throwable th6) {
            e = th6;
        }
    }

    private int doInstallAddSession(int parentId, int[] sessionIds, boolean logSuccess) throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            android.content.pm.PackageInstaller.Session session = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(parentId));
            if (!session.isMultiPackage()) {
                getErrPrintWriter().println("Error: parent session ID is not a multi-package session");
                libcore.io.IoUtils.closeQuietly(session);
                return 1;
            }
            for (int i : sessionIds) {
                session.addChildSessionId(i);
            }
            if (logSuccess) {
                pw.println("Success");
            }
            libcore.io.IoUtils.closeQuietly(session);
            return 0;
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly((java.lang.AutoCloseable) null);
            throw th;
        }
    }

    private int doRemoveSplits(int sessionId, java.util.Collection<java.lang.String> splitNames, boolean logSuccess) throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        android.content.pm.PackageInstaller.Session session = null;
        try {
            try {
                session = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(sessionId));
                for (java.lang.String splitName : splitNames) {
                    session.removeSplit(splitName);
                }
                if (logSuccess) {
                    pw.println("Success");
                }
                libcore.io.IoUtils.closeQuietly(session);
                return 0;
            } catch (java.io.IOException e) {
                pw.println("Error: failed to remove split; " + e.getMessage());
                libcore.io.IoUtils.closeQuietly(session);
                return 1;
            }
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly(session);
            throw th;
        }
    }

    private int doCommitSession(int sessionId, boolean logSuccess) throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        android.content.pm.PackageInstaller.Session session = null;
        try {
            session = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(sessionId));
            if (!session.isMultiPackage() && !session.isStaged()) {
                try {
                    android.content.pm.dex.DexMetadataHelper.validateDexPaths(session.getNames());
                } catch (java.io.IOException | java.lang.IllegalStateException e) {
                    pw.println("Warning [Could not validate the dex paths: " + e.getMessage() + "]");
                }
            }
            com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver receiver = new com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver();
            session.commit(receiver.getIntentSender());
            if (!session.isStaged()) {
                android.content.Intent result = receiver.getResult();
                int status = result.getIntExtra("android.content.pm.extra.STATUS", 1);
                java.util.List<java.lang.String> warnings = result.getStringArrayListExtra("android.content.pm.extra.WARNINGS");
                if (status == 0) {
                    if (!com.android.internal.util.ArrayUtils.isEmpty(warnings)) {
                        for (java.lang.String warning : warnings) {
                            pw.println("Warning: " + warning);
                        }
                        status = 1;
                        pw.println("Completed with warning(s)");
                    } else if (logSuccess) {
                        pw.println("Success");
                    }
                } else {
                    pw.println("Failure [" + result.getStringExtra("android.content.pm.extra.STATUS_MESSAGE") + "]");
                }
                return status;
            }
            if (logSuccess) {
                pw.println("Success");
            }
            libcore.io.IoUtils.closeQuietly(session);
            return 0;
        } finally {
            libcore.io.IoUtils.closeQuietly(session);
        }
    }

    private int doAbandonSession(int sessionId, boolean logSuccess) throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        android.content.pm.PackageInstaller.Session session = null;
        try {
            session = new android.content.pm.PackageInstaller.Session(this.mInterface.getPackageInstaller().openSession(sessionId));
            session.abandon();
            if (logSuccess) {
                pw.println("Success");
            }
            libcore.io.IoUtils.closeQuietly(session);
            return 0;
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly(session);
            throw th;
        }
    }

    private void doListPermissions(java.util.ArrayList<java.lang.String> groupList, boolean groups, boolean labels, boolean summary, int startProtectionLevel, int endProtectionLevel) throws android.os.RemoteException {
        int groupCount;
        java.util.List<android.content.pm.PermissionInfo> ps;
        java.lang.String groupName;
        java.util.ArrayList<java.lang.String> arrayList = groupList;
        java.io.PrintWriter pw = getOutPrintWriter();
        int groupCount2 = groupList.size();
        int i = 0;
        while (i < groupCount2) {
            java.lang.String groupName2 = arrayList.get(i);
            java.lang.String prefix = "";
            if (!groups) {
                groupCount = groupCount2;
            } else {
                if (i > 0) {
                    pw.println("");
                }
                if (groupName2 != null) {
                    android.content.pm.PermissionGroupInfo pgi = this.mInterface.getPermissionGroupInfo(groupName2, 0);
                    if (summary) {
                        android.content.res.Resources res = getResources(pgi);
                        if (res != null) {
                            java.lang.StringBuilder sb = new java.lang.StringBuilder();
                            groupCount = groupCount2;
                            int groupCount3 = pgi.labelRes;
                            pw.print(sb.append(loadText(pgi, groupCount3, pgi.nonLocalizedLabel)).append(": ").toString());
                        } else {
                            groupCount = groupCount2;
                            pw.print(pgi.name + ": ");
                        }
                    } else {
                        groupCount = groupCount2;
                        pw.println((labels ? "+ " : "") + "group:" + pgi.name);
                        if (labels) {
                            pw.println("  package:" + pgi.packageName);
                            android.content.res.Resources res2 = getResources(pgi);
                            if (res2 != null) {
                                pw.println("  label:" + loadText(pgi, pgi.labelRes, pgi.nonLocalizedLabel));
                                pw.println("  description:" + loadText(pgi, pgi.descriptionRes, pgi.nonLocalizedDescription));
                            }
                        }
                    }
                } else {
                    groupCount = groupCount2;
                    pw.println(((!labels || summary) ? "" : "+ ") + "ungrouped:");
                }
                prefix = "  ";
            }
            java.util.List<android.content.pm.PermissionInfo> ps2 = this.mPermissionManager.queryPermissionsByGroup(arrayList.get(i), 0);
            int count = ps2 == null ? 0 : ps2.size();
            boolean first = true;
            int p = 0;
            while (p < count) {
                android.content.pm.PermissionInfo pi = ps2.get(p);
                if (groups && groupName2 == null && pi.group != null) {
                    ps = ps2;
                    groupName = groupName2;
                } else {
                    int base = pi.protectionLevel & 15;
                    ps = ps2;
                    if (base < startProtectionLevel) {
                        groupName = groupName2;
                    } else if (base > endProtectionLevel) {
                        groupName = groupName2;
                    } else if (summary) {
                        if (first) {
                            first = false;
                        } else {
                            pw.print(", ");
                        }
                        android.content.res.Resources res3 = getResources(pi);
                        if (res3 != null) {
                            pw.print(loadText(pi, pi.labelRes, pi.nonLocalizedLabel));
                        } else {
                            pw.print(pi.name);
                        }
                        groupName = groupName2;
                    } else {
                        pw.println(prefix + (labels ? "+ " : "") + "permission:" + pi.name);
                        if (!labels) {
                            groupName = groupName2;
                        } else {
                            pw.println(prefix + "  package:" + pi.packageName);
                            android.content.res.Resources res4 = getResources(pi);
                            if (res4 != null) {
                                groupName = groupName2;
                                pw.println(prefix + "  label:" + loadText(pi, pi.labelRes, pi.nonLocalizedLabel));
                                pw.println(prefix + "  description:" + loadText(pi, pi.descriptionRes, pi.nonLocalizedDescription));
                            } else {
                                groupName = groupName2;
                            }
                            pw.println(prefix + "  protectionLevel:" + android.content.pm.PermissionInfo.protectionToString(pi.protectionLevel));
                        }
                    }
                }
                p++;
                ps2 = ps;
                groupName2 = groupName;
            }
            if (summary) {
                pw.println("");
            }
            i++;
            arrayList = groupList;
            groupCount2 = groupCount;
        }
    }

    private java.lang.String loadText(android.content.pm.PackageItemInfo pii, int res, java.lang.CharSequence nonLocalized) throws android.os.RemoteException {
        android.content.res.Resources r;
        if (nonLocalized != null) {
            return nonLocalized.toString();
        }
        if (res != 0 && (r = getResources(pii)) != null) {
            try {
                return r.getString(res);
            } catch (android.content.res.Resources.NotFoundException e) {
                return null;
            }
        }
        return null;
    }

    private android.content.res.Resources getResources(android.content.pm.PackageItemInfo pii) throws android.os.RemoteException {
        android.content.res.Resources res = this.mResourceCache.get(pii.packageName);
        if (res != null) {
            return res;
        }
        android.content.pm.ApplicationInfo ai = this.mInterface.getApplicationInfo(pii.packageName, 536904192L, 0);
        if (ai == null) {
            android.util.Slog.e(TAG, "Failed to get ApplicationInfo for package name(" + pii.packageName + ").");
            return null;
        }
        android.content.res.AssetManager am = new android.content.res.AssetManager();
        am.addAssetPath(ai.publicSourceDir);
        android.content.res.Resources res2 = new android.content.res.Resources(am, null, null);
        this.mResourceCache.put(pii.packageName, res2);
        return res2;
    }

    private int resolveUserId(int userId) {
        return userId == -2 ? android.app.ActivityManager.getCurrentUser() : userId;
    }

    private int runClearPackagePreferredActivities() {
        java.io.PrintWriter pw = getErrPrintWriter();
        java.lang.String packageName = getNextArg();
        if (packageName == null) {
            pw.println("Error: package name not specified");
            return 1;
        }
        try {
            this.mContext.getPackageManager().clearPackagePreferredActivities(packageName);
            return 0;
        } catch (java.lang.Exception e) {
            pw.println(e.toString());
            return 1;
        }
    }

    private int runArchive() throws android.os.RemoteException {
        int flags;
        java.io.PrintWriter pw = getOutPrintWriter();
        int userId = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                    if (userId != -1 && userId != -2) {
                        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
                        android.content.pm.UserInfo userInfo = umi.getUserInfo(userId);
                        if (userInfo == null) {
                            pw.println("Failure [user " + userId + " doesn't exist]");
                            return 1;
                        }
                    }
                } else {
                    pw.println("Error: Unknown option: " + opt);
                    return 1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                if (packageName == null) {
                    pw.println("Error: package name not specified");
                    return 1;
                }
                if (userId != -1) {
                    flags = 0;
                } else {
                    int flags2 = 0 | 2;
                    flags = flags2;
                }
                int translatedUserId = translateUserId(userId, 0, "runArchive");
                com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver receiver = new com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver();
                try {
                    try {
                        this.mInterface.getPackageInstaller().requestArchive(packageName, "", flags, receiver.getIntentSender(), new android.os.UserHandle(translatedUserId));
                        android.content.Intent result = receiver.getResult();
                        int status = result.getIntExtra("android.content.pm.extra.STATUS", 1);
                        if (status == 0) {
                            pw.println("Success");
                            return 0;
                        }
                        pw.println("Failure [" + result.getStringExtra("android.content.pm.extra.STATUS_MESSAGE") + "]");
                        return 1;
                    } catch (java.lang.Exception e) {
                        e = e;
                        pw.println("Failure [" + e.getMessage() + "]");
                        return 1;
                    }
                } catch (java.lang.Exception e2) {
                    e = e2;
                }
            }
        }
    }

    private int runUnarchive() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        int userId = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                    if (userId != -1 && userId != -2) {
                        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
                        android.content.pm.UserInfo userInfo = umi.getUserInfo(userId);
                        if (userInfo == null) {
                            pw.println("Failure [user " + userId + " doesn't exist]");
                            return 1;
                        }
                    }
                } else {
                    pw.println("Error: Unknown option: " + opt);
                    return 1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                if (packageName == null) {
                    pw.println("Error: package name not specified");
                    return 1;
                }
                int translatedUserId = translateUserId(userId, 0, "runArchive");
                com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver receiver = new com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver();
                try {
                    this.mInterface.getPackageInstaller().requestUnarchive(packageName, this.mContext.getPackageName(), receiver.getIntentSender(), new android.os.UserHandle(translatedUserId));
                    pw.println("Success");
                    return 0;
                } catch (java.lang.Exception e) {
                    pw.println("Failure [" + e.getMessage() + "]");
                    return 1;
                }
            }
        }
    }

    private int runGetDomainVerificationAgent() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        int userId = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                    if (userId != -1 && userId != -2) {
                        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
                        android.content.pm.UserInfo userInfo = umi.getUserInfo(userId);
                        if (userInfo == null) {
                            pw.println("Failure [user " + userId + " doesn't exist]");
                            return 1;
                        }
                    }
                } else {
                    pw.println("Error: Unknown option: " + opt);
                    return 1;
                }
            } else {
                int translatedUserId = translateUserId(userId, 0, "runGetDomainVerificationAgent");
                try {
                    android.content.ComponentName domainVerificationAgent = this.mInterface.getDomainVerificationAgent(translatedUserId);
                    pw.println(domainVerificationAgent == null ? "No Domain Verifier available!" : domainVerificationAgent.flattenToString());
                    return 0;
                } catch (java.lang.Exception e) {
                    pw.println("Failure [" + e.getMessage() + "]");
                    return 1;
                }
            }
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Package manager (package) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  path [--user USER_ID] PACKAGE");
        pw.println("    Print the path to the .apk of the given PACKAGE.");
        pw.println("");
        pw.println("  dump PACKAGE");
        pw.println("    Print various system state associated with the given PACKAGE.");
        pw.println("");
        pw.println("  dump-package PACKAGE");
        pw.println("    Print package manager state associated with the given PACKAGE.");
        pw.println("");
        pw.println("  has-feature FEATURE_NAME [version]");
        pw.println("    Prints true and returns exit status 0 when system has a FEATURE_NAME,");
        pw.println("    otherwise prints false and returns exit status 1");
        pw.println("");
        pw.println("  list features");
        pw.println("    Prints all features of the system.");
        pw.println("");
        pw.println("  list instrumentation [-f] [TARGET-PACKAGE]");
        pw.println("    Prints all test packages; optionally only those targeting TARGET-PACKAGE");
        pw.println("    Options:");
        pw.println("      -f: dump the name of the .apk file containing the test package");
        pw.println("");
        pw.println("  list libraries [-v]");
        pw.println("    Prints all system libraries.");
        pw.println("    Options:");
        pw.println("      -v: shows the location of the library in the device's filesystem");
        pw.println("");
        pw.println("  list packages [-f] [-d] [-e] [-s] [-q] [-3] [-i] [-l] [-u] [-U] ");
        pw.println("      [--show-versioncode] [--apex-only] [--factory-only]");
        pw.println("      [--uid UID] [--user USER_ID] [FILTER]");
        pw.println("    Prints all packages; optionally only those whose name contains");
        pw.println("    the text in FILTER.  Options are:");
        pw.println("      -f: see their associated file");
        pw.println("      -a: all known packages (but excluding APEXes)");
        pw.println("      -d: filter to only show disabled packages");
        pw.println("      -e: filter to only show enabled packages");
        pw.println("      -s: filter to only show system packages");
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.quarantinedEnabled()) {
            pw.println("      -q: filter to only show quarantined packages");
        }
        pw.println("      -3: filter to only show third party packages");
        pw.println("      -i: see the installer for the packages");
        pw.println("      -l: ignored (used for compatibility with older releases)");
        pw.println("      -U: also show the package UID");
        pw.println("      -u: also include uninstalled packages");
        pw.println("      --show-versioncode: also show the version code");
        pw.println("      --apex-only: only show APEX packages");
        pw.println("      --factory-only: only show system packages excluding updates");
        pw.println("      --uid UID: filter to only show packages with the given UID");
        pw.println("      --user USER_ID: only list packages belonging to the given user");
        pw.println("      --match-libraries: include packages that declare static shared and SDK libraries");
        pw.println("");
        pw.println("  list permission-groups");
        pw.println("    Prints all known permission groups.");
        pw.println("");
        pw.println("  list permissions [-g] [-f] [-d] [-u] [GROUP]");
        pw.println("    Prints all known permissions; optionally only those in GROUP.  Options are:");
        pw.println("      -g: organize by group");
        pw.println("      -f: print all information");
        pw.println("      -s: short summary");
        pw.println("      -d: only list dangerous permissions");
        pw.println("      -u: list only the permissions users will see");
        pw.println("");
        pw.println("  list staged-sessions [--only-ready] [--only-sessionid] [--only-parent]");
        pw.println("    Prints all staged sessions.");
        pw.println("      --only-ready: show only staged sessions that are ready");
        pw.println("      --only-sessionid: show only sessionId of each session");
        pw.println("      --only-parent: hide all children sessions");
        pw.println("");
        pw.println("  list users");
        pw.println("    Prints all users.");
        pw.println("");
        pw.println("  resolve-activity [--brief] [--components] [--query-flags FLAGS]");
        pw.println("       [--user USER_ID] INTENT");
        pw.println("    Prints the activity that resolves to the given INTENT.");
        pw.println("");
        pw.println("  query-activities [--brief] [--components] [--query-flags FLAGS]");
        pw.println("       [--user USER_ID] INTENT");
        pw.println("    Prints all activities that can handle the given INTENT.");
        pw.println("");
        pw.println("  query-services [--brief] [--components] [--query-flags FLAGS]");
        pw.println("       [--user USER_ID] INTENT");
        pw.println("    Prints all services that can handle the given INTENT.");
        pw.println("");
        pw.println("  query-receivers [--brief] [--components] [--query-flags FLAGS]");
        pw.println("       [--user USER_ID] INTENT");
        pw.println("    Prints all broadcast receivers that can handle the given INTENT.");
        pw.println("");
        pw.println("  install [-rtfdg] [-i PACKAGE] [--user USER_ID|all|current]");
        pw.println("       [-p INHERIT_PACKAGE] [--install-location 0/1/2]");
        pw.println("       [--install-reason 0/1/2/3/4] [--originating-uri URI]");
        pw.println("       [--referrer URI] [--abi ABI_NAME] [--force-sdk]");
        pw.println("       [--preload] [--instant] [--full] [--dont-kill]");
        pw.println("       [--enable-rollback [0/1/2]]");
        pw.println("       [--force-uuid internal|UUID] [--pkg PACKAGE] [-S BYTES]");
        pw.println("       [--apex] [--non-staged] [--force-non-staged]");
        pw.println("       [--staged-ready-timeout TIMEOUT] [--ignore-dexopt-profile]");
        pw.println("       [--dexopt-compiler-filter FILTER]");
        pw.println("       [PATH [SPLIT...]|-]");
        pw.println("    Install an application.  Must provide the apk data to install, either as");
        pw.println("    file path(s) or '-' to read from stdin.  Options are:");
        pw.println("      -R: disallow replacement of existing application");
        pw.println("      -t: allow test packages");
        pw.println("      -i: specify package name of installer owning the app");
        pw.println("      -f: install application on internal flash");
        pw.println("      -d: allow version code downgrade (debuggable packages only)");
        pw.println("      -p: partial application install (new split on top of existing pkg)");
        pw.println("      -g: grant all runtime permissions");
        pw.println("      -S: size in bytes of package, required for stdin");
        pw.println("      --user: install under the given user.");
        pw.println("      --dont-kill: installing a new feature split, don't kill running app");
        pw.println("      --restrict-permissions: don't whitelist restricted permissions at install");
        pw.println("      --originating-uri: set URI where app was downloaded from");
        pw.println("      --referrer: set URI that instigated the install of the app");
        pw.println("      --pkg: specify expected package name of app being installed");
        pw.println("      --abi: override the default ABI of the platform");
        pw.println("      --instant: cause the app to be installed as an ephemeral install app");
        pw.println("      --full: cause the app to be installed as a non-ephemeral full app");
        pw.println("      --enable-rollback: enable rollbacks for the upgrade.");
        pw.println("          0=restore (default), 1=wipe, 2=retain");
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection()) {
            pw.println("      --rollback-impact-level: set device impact required for rollback.");
            pw.println("          0=low (default), 1=high, 2=manual only");
        }
        pw.println("      --install-location: force the install location:");
        pw.println("          0=auto, 1=internal only, 2=prefer external");
        pw.println("      --install-reason: indicates why the app is being installed:");
        pw.println("          0=unknown, 1=admin policy, 2=device restore,");
        pw.println("          3=device setup, 4=user request");
        pw.println("      --update-ownership: request the update ownership enforcement");
        pw.println("      --force-uuid: force install on to disk volume with given UUID");
        pw.println("      --apex: install an .apex file, not an .apk");
        pw.println("      --non-staged: explicitly set this installation to be non-staged.");
        pw.println("          This flag is only useful for APEX installs that are implicitly");
        pw.println("          assumed to be staged.");
        pw.println("      --force-non-staged: force the installation to run under a non-staged");
        pw.println("          session, which may complete without requiring a reboot. This will");
        pw.println("          force a rebootless update even for APEXes that don't support it");
        pw.println("      --staged-ready-timeout: By default, staged sessions wait 60000");
        pw.println("          milliseconds for pre-reboot verification to complete when");
        pw.println("          performing staged install. This flag is used to alter the waiting");
        pw.println("          time. You can skip the waiting time by specifying a TIMEOUT of '0'");
        pw.println("      --ignore-dexopt-profile: if set, all profiles are ignored by dexopt");
        pw.println("          during the installation, including the profile in the DM file and");
        pw.println("          the profile embedded in the APK file. If an invalid profile is");
        pw.println("          provided during installation, no warning will be reported by `adb");
        pw.println("          install`.");
        pw.println("          This option does not affect later dexopt operations (e.g.,");
        pw.println("          background dexopt and manual `pm compile` invocations).");
        pw.println("      --dexopt-compiler-filter: the target compiler filter for dexopt during");
        pw.println("          the installation. The filter actually used may be different.");
        pw.println("          Valid values: one of the values documented in");
        pw.println("          https://source.android.com/docs/core/runtime/configure#compiler_filters");
        pw.println("          or 'skip'");
        pw.println("");
        pw.println("  install-existing [--user USER_ID|all|current]");
        pw.println("       [--instant] [--full] [--wait] [--restrict-permissions] PACKAGE");
        pw.println("    Installs an existing application for a new user.  Options are:");
        pw.println("      --user: install for the given user.");
        pw.println("      --instant: install as an instant app");
        pw.println("      --full: install as a full app");
        pw.println("      --wait: wait until the package is installed");
        pw.println("      --restrict-permissions: don't whitelist restricted permissions");
        pw.println("");
        pw.println("  install-create [-lrtsfdg] [-i PACKAGE] [--user USER_ID|all|current]");
        pw.println("       [-p INHERIT_PACKAGE] [--install-location 0/1/2]");
        pw.println("       [--install-reason 0/1/2/3/4] [--originating-uri URI]");
        pw.println("       [--referrer URI] [--abi ABI_NAME] [--force-sdk]");
        pw.println("       [--preload] [--instant] [--full] [--dont-kill]");
        pw.println("       [--force-uuid internal|UUID] [--pkg PACKAGE] [--apex] [-S BYTES]");
        pw.println("       [--multi-package] [--staged] [--update-ownership]");
        pw.println("    Like \"install\", but starts an install session.  Use \"install-write\"");
        pw.println("    to push data into the session, and \"install-commit\" to finish.");
        pw.println("");
        pw.println("  install-write [-S BYTES] SESSION_ID SPLIT_NAME [PATH|-]");
        pw.println("    Write an apk into the given install session.  If the path is '-', data");
        pw.println("    will be read from stdin.  Options are:");
        pw.println("      -S: size in bytes of package, required for stdin");
        pw.println("");
        pw.println("  install-remove SESSION_ID SPLIT...");
        pw.println("    Mark SPLIT(s) as removed in the given install session.");
        pw.println("");
        pw.println("  install-add-session MULTI_PACKAGE_SESSION_ID CHILD_SESSION_IDs");
        pw.println("    Add one or more session IDs to a multi-package session.");
        pw.println("");
        pw.println("  install-set-pre-verified-domains SESSION_ID PRE_VERIFIED_DOMAIN... ");
        pw.println("    Specify a comma separated list of pre-verified domains for a session.");
        pw.println("");
        pw.println("  install-get-pre-verified-domains SESSION_ID");
        pw.println("    List all the pre-verified domains that are specified in a session.");
        pw.println("    The result list is comma separated.");
        pw.println("");
        pw.println("  install-commit SESSION_ID");
        pw.println("    Commit the given active install session, installing the app.");
        pw.println("");
        pw.println("  install-abandon SESSION_ID");
        pw.println("    Delete the given active install session.");
        pw.println("");
        pw.println("  set-install-location LOCATION");
        pw.println("    Changes the default install location.  NOTE this is only intended for debugging;");
        pw.println("    using this can cause applications to break and other undersireable behavior.");
        pw.println("    LOCATION is one of:");
        pw.println("    0 [auto]: Let system decide the best location");
        pw.println("    1 [internal]: Install on internal device storage");
        pw.println("    2 [external]: Install on external media");
        pw.println("");
        pw.println("  get-install-location");
        pw.println("    Returns the current install location: 0, 1 or 2 as per set-install-location.");
        pw.println("");
        pw.println("  move-package PACKAGE [internal|UUID]");
        pw.println("");
        pw.println("  move-primary-storage [internal|UUID]");
        pw.println("");
        pw.println("  uninstall [-k] [--user USER_ID] [--versionCode VERSION_CODE]");
        pw.println("       PACKAGE [SPLIT...]");
        pw.println("    Remove the given package name from the system.  May remove an entire app");
        pw.println("    if no SPLIT names specified, otherwise will remove only the splits of the");
        pw.println("    given app.  Options are:");
        pw.println("      -k: keep the data and cache directories around after package removal.");
        pw.println("      --user: remove the app from the given user.");
        pw.println("      --versionCode: only uninstall if the app has the given version code.");
        pw.println("");
        pw.println("  clear [--user USER_ID] [--cache-only] PACKAGE");
        pw.println("    Deletes data associated with a package. Options are:");
        pw.println("    --user: specifies the user for which we need to clear data");
        pw.println("    --cache-only: a flag which tells if we only need to clear cache data");
        pw.println("");
        pw.println("  enable [--user USER_ID] PACKAGE_OR_COMPONENT");
        pw.println("  disable [--user USER_ID] PACKAGE_OR_COMPONENT");
        pw.println("  disable-user [--user USER_ID] PACKAGE_OR_COMPONENT");
        pw.println("  disable-until-used [--user USER_ID] PACKAGE_OR_COMPONENT");
        pw.println("  default-state [--user USER_ID] PACKAGE_OR_COMPONENT");
        pw.println("    These commands change the enabled state of a given package or");
        pw.println("    component (written as \"package/class\").");
        pw.println("");
        pw.println("  hide [--user USER_ID] PACKAGE_OR_COMPONENT");
        pw.println("  unhide [--user USER_ID] PACKAGE_OR_COMPONENT");
        pw.println("");
        pw.println("  unstop [--user USER_ID] PACKAGE");
        pw.println("");
        pw.println("  suspend [--user USER_ID] PACKAGE [PACKAGE...]");
        pw.println("    Suspends the specified package(s) (as user).");
        pw.println("");
        pw.println("  unsuspend [--user USER_ID] PACKAGE [PACKAGE...]");
        pw.println("    Unsuspends the specified package(s) (as user).");
        pw.println("");
        pw.println("  set-distracting-restriction [--user USER_ID] [--flag FLAG ...]");
        pw.println("      PACKAGE [PACKAGE...]");
        pw.println("    Sets the specified restriction flags to given package(s) (for user).");
        pw.println("    Flags are:");
        pw.println("      hide-notifications: Hides notifications from this package");
        pw.println("      hide-from-suggestions: Hides this package from suggestions");
        pw.println("        (by the launcher, etc.)");
        pw.println("    Any existing flags are overwritten, which also means that if no flags are");
        pw.println("    specified then all existing flags will be cleared.");
        pw.println("");
        pw.println("  get-distracting-restriction [--user USER_ID] PACKAGE [PACKAGE...]");
        pw.println("    Gets the specified restriction flags of given package(s) (of the user).");
        pw.println("");
        pw.println("  grant [--user USER_ID] [--all-permissions] PACKAGE PERMISSION");
        pw.println("  revoke [--user USER_ID] [--all-permissions] PACKAGE PERMISSION");
        pw.println("    These commands either grant or revoke permissions to apps.  The permissions");
        pw.println("    must be declared as used in the app's manifest, be runtime permissions");
        pw.println("    (protection level dangerous), and the app targeting SDK greater than Lollipop MR1.");
        pw.println("    Flags are:");
        pw.println("    --user: Specifies the user for which the operation needs to be performed");
        pw.println("    --all-permissions: If specified all the missing runtime permissions will");
        pw.println("       be granted to the PACKAGE or to all the packages if none is specified.");
        pw.println("");
        pw.println("  set-permission-flags [--user USER_ID] PACKAGE PERMISSION [FLAGS..]");
        pw.println("  clear-permission-flags [--user USER_ID] PACKAGE PERMISSION [FLAGS..]");
        pw.println("    These commands either set or clear permission flags on apps.  The permissions");
        pw.println("    must be declared as used in the app's manifest, be runtime permissions");
        pw.println("    (protection level dangerous), and the app targeting SDK greater than Lollipop MR1.");
        pw.println("    The flags must be one or more of " + SUPPORTED_PERMISSION_FLAGS_LIST);
        pw.println("");
        pw.println("  reset-permissions");
        pw.println("    Revert all runtime permissions to their default state.");
        pw.println("");
        pw.println("  set-permission-enforced PERMISSION [true|false]");
        pw.println("");
        pw.println("  get-privapp-permissions TARGET-PACKAGE");
        pw.println("    Prints all privileged permissions for a package.");
        pw.println("");
        pw.println("  get-privapp-deny-permissions TARGET-PACKAGE");
        pw.println("    Prints all privileged permissions that are denied for a package.");
        pw.println("");
        pw.println("  get-oem-permissions TARGET-PACKAGE");
        pw.println("    Prints all OEM permissions for a package.");
        pw.println("");
        pw.println("  get-signature-permission-allowlist PARTITION");
        pw.println("    Prints the signature permission allowlist for a partition.");
        pw.println("    PARTITION is one of system, vendor, product, system-ext and apex");
        pw.println("");
        pw.println("  get-shared-uid-allowlist");
        pw.println("    Prints the shared UID allowlist.");
        pw.println("");
        pw.println("  trim-caches DESIRED_FREE_SPACE [internal|UUID]");
        pw.println("    Trim cache files to reach the given free space.");
        pw.println("");
        pw.println("  list users");
        pw.println("    Lists the current users.");
        pw.println("");
        pw.println("  create-user [--profileOf USER_ID] [--managed] [--restricted] [--guest]");
        pw.println("       [--user-type USER_TYPE] [--ephemeral] [--for-testing] [--pre-create-only]   USER_NAME");
        pw.println("    Create a new user with the given USER_NAME, printing the new user identifier");
        pw.println("    of the user.");
        pw.println("    USER_TYPE is the name of a user type, e.g. android.os.usertype.profile.MANAGED.");
        pw.println("      If not specified, the default user type is android.os.usertype.full.SECONDARY.");
        pw.println("      --managed is shorthand for '--user-type android.os.usertype.profile.MANAGED'.");
        pw.println("      --restricted is shorthand for '--user-type android.os.usertype.full.RESTRICTED'.");
        pw.println("      --guest is shorthand for '--user-type android.os.usertype.full.GUEST'.");
        pw.println("");
        pw.println("  remove-user [--set-ephemeral-if-in-use | --wait] USER_ID");
        pw.println("    Remove the user with the given USER_IDENTIFIER, deleting all data");
        pw.println("    associated with that user.");
        pw.println("      --set-ephemeral-if-in-use: If the user is currently running and");
        pw.println("        therefore cannot be removed immediately, mark the user as ephemeral");
        pw.println("        so that it will be automatically removed when possible (after user");
        pw.println("        switch or reboot)");
        pw.println("      --wait: Wait until user is removed. Ignored if set-ephemeral-if-in-use");
        pw.println("");
        pw.println("  mark-guest-for-deletion USER_ID");
        pw.println("    Mark the guest user for deletion. After this, it is possible to create a");
        pw.println("    new guest user and switch to it. This allows resetting the guest user");
        pw.println("    without switching to another user.");
        pw.println("");
        pw.println("  rename-user USER_ID [USER_NAME]");
        pw.println("    Rename USER_ID with USER_NAME (or null when [USER_NAME] is not set)");
        pw.println("");
        pw.println("  set-user-restriction [--user USER_ID] RESTRICTION VALUE");
        pw.println("");
        pw.println("  get-user-restriction [--user USER_ID] [--all] RESTRICTION_KEY");
        pw.println("    Display the value of restriction for the given restriction key if the");
        pw.println("    given user is valid.");
        pw.println("      --all: display all restrictions for the given user");
        pw.println("          This option is used without restriction key");
        pw.println("");
        pw.println("  get-max-users");
        pw.println("");
        pw.println("  get-max-running-users");
        pw.println("");
        pw.println("  set-home-activity [--user USER_ID] TARGET-COMPONENT");
        pw.println("    Set the default home activity (aka launcher).");
        pw.println("    TARGET-COMPONENT can be a package name (com.package.my) or a full");
        pw.println("    component (com.package.my/component.name). However, only the package name");
        pw.println("    matters: the actual component used will be determined automatically from");
        pw.println("    the package.");
        pw.println("");
        pw.println("  set-installer PACKAGE INSTALLER");
        pw.println("    Set installer package name");
        pw.println("");
        pw.println("  get-instantapp-resolver");
        pw.println("    Return the name of the component that is the current instant app installer.");
        pw.println("");
        pw.println("  set-harmful-app-warning [--user <USER_ID>] <PACKAGE> [<WARNING>]");
        pw.println("    Mark the app as harmful with the given warning message.");
        pw.println("");
        pw.println("  get-harmful-app-warning [--user <USER_ID>] <PACKAGE>");
        pw.println("    Return the harmful app warning message for the given app, if present");
        pw.println();
        pw.println("  uninstall-system-updates [<PACKAGE>]");
        pw.println("    Removes updates to the given system application and falls back to its");
        pw.println("    /system version. Does nothing if the given package is not a system app.");
        pw.println("    If no package is specified, removes updates to all system applications.");
        pw.println("");
        pw.println("  get-moduleinfo [--all | --installed] [module-name]");
        pw.println("    Displays module info. If module-name is specified only that info is shown");
        pw.println("    By default, without any argument only installed modules are shown.");
        pw.println("      --all: show all module info");
        pw.println("      --installed: show only installed modules");
        pw.println("");
        pw.println("  log-visibility [--enable|--disable] <PACKAGE>");
        pw.println("    Turns on debug logging when visibility is blocked for the given package.");
        pw.println("      --enable: turn on debug logging (default)");
        pw.println("      --disable: turn off debug logging");
        pw.println("");
        pw.println("  set-silent-updates-policy [--allow-unlimited-silent-updates <INSTALLER>]");
        pw.println("                            [--throttle-time <SECONDS>] [--reset]");
        pw.println("    Sets the policies of the silent updates.");
        pw.println("      --allow-unlimited-silent-updates: allows unlimited silent updated");
        pw.println("        installation requests from the installer without the throttle time.");
        pw.println("      --throttle-time: update the silent updates throttle time in seconds.");
        pw.println("      --reset: restore the installer and throttle time to the default, and");
        pw.println("        clear tracks of silent updates in the system.");
        pw.println("");
        pw.println("  clear-package-preferred-activities <PACKAGE>");
        pw.println("    Remove the preferred activity mappings for the given package.");
        pw.println("  wait-for-handler --timeout <MILLIS>");
        pw.println("    Wait for a given amount of time till the package manager handler finishes");
        pw.println("    handling all pending messages.");
        pw.println("      --timeout: wait for a given number of milliseconds. If the handler(s)");
        pw.println("        fail to finish before the timeout, the command returns error.");
        pw.println("");
        pw.println("  wait-for-background-handler --timeout <MILLIS>");
        pw.println("    Wait for a given amount of time till the package manager's background");
        pw.println("    handler finishes handling all pending messages.");
        pw.println("      --timeout: wait for a given number of milliseconds. If the handler(s)");
        pw.println("        fail to finish before the timeout, the command returns error.");
        pw.println("");
        pw.println("  archive [--user USER_ID] PACKAGE ");
        pw.println("    During the archival process, the apps APKs and cache are removed from the");
        pw.println("    device while the user data is kept. Options are:");
        pw.println("      --user: archive the app from the given user.");
        pw.println("");
        pw.println("  request-unarchive [--user USER_ID] PACKAGE ");
        pw.println("    Requests to unarchive a currently archived package by sending a request");
        pw.println("    to unarchive an app to the responsible installer. Options are:");
        pw.println("      --user: request unarchival of the app from the given user.");
        pw.println("");
        pw.println("  get-domain-verification-agent [--user USER_ID]");
        pw.println("    Displays the component name of the domain verification agent on device.");
        pw.println("    If the component isn't enabled, an error message will be displayed.");
        pw.println("      --user: return the agent of the given user (SYSTEM_USER if unspecified)");
        pw.println("  get-package-storage-stats [--user <USER_ID>] <PACKAGE>");
        pw.println("    Return the storage stats for the given app, if present");
        pw.println("");
        printArtServiceHelp();
        pw.println("");
        this.mDomainVerificationShell.printHelp(pw);
        pw.println("");
        android.content.Intent.printIntentArgsHelp(pw, "");
    }

    private void printArtServiceHelp() {
        com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(getOutPrintWriter(), "  ");
        ipw.increaseIndent();
        try {
            ((com.android.server.art.ArtManagerLocal) com.android.server.LocalManagerRegistry.getManagerOrThrow(com.android.server.art.ArtManagerLocal.class)).printShellCommandHelp(ipw);
        } catch (com.android.server.LocalManagerRegistry.ManagerNotFoundException e) {
            ipw.println("ART Service is not ready. Please try again later");
        }
        ipw.decreaseIndent();
    }

    private static class LocalIntentReceiver {
        private final android.content.IIntentSender.Stub mLocalSender;
        private final java.util.concurrent.LinkedBlockingQueue<android.content.Intent> mResult;

        private LocalIntentReceiver() {
            this.mResult = new java.util.concurrent.LinkedBlockingQueue<>();
            this.mLocalSender = new android.content.IIntentSender.Stub() { // from class: com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver.1
                public void send(int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder whitelistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) {
                    try {
                        com.android.server.pm.PackageManagerShellCommand.LocalIntentReceiver.this.mResult.offer(intent, 5L, java.util.concurrent.TimeUnit.SECONDS);
                    } catch (java.lang.InterruptedException e) {
                        throw new java.lang.RuntimeException(e);
                    }
                }
            };
        }

        public android.content.IntentSender getIntentSender() {
            return new android.content.IntentSender(this.mLocalSender);
        }

        public android.content.Intent getResult() {
            try {
                return this.mResult.take();
            } catch (java.lang.InterruptedException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
    }
}
