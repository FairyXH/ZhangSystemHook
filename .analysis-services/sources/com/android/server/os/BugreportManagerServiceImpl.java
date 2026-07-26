package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
class BugreportManagerServiceImpl extends android.os.IDumpstate.Stub {
    private static final java.lang.String ATTR_BUGREPORT_FILE = "bugreport-file";
    private static final java.lang.String ATTR_CALLING_PACKAGE = "calling-package";
    private static final java.lang.String ATTR_CALLING_UID = "calling-uid";
    private static final java.lang.String BUGREPORT_SERVICE = "bugreportd";
    private static final boolean DEBUG = false;
    private static final long DEFAULT_BUGREPORT_SERVICE_TIMEOUT_MILLIS = 30000;
    private static final int LOCAL_LOG_SIZE = 20;
    private static final java.lang.String ROLE_SYSTEM_AUTOMOTIVE_PROJECTION = "android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION";
    private static final java.lang.String TAG = "BugreportManagerService";
    private static final java.lang.String TAG_BUGREPORT_DATA = "bugreport-data";
    private static final java.lang.String TAG_BUGREPORT_MAP = "bugreport-map";
    private static final java.lang.String TAG_PERSISTENT_BUGREPORT = "persistent-bugreport";
    private final android.app.AppOpsManager mAppOps;
    private final android.util.ArraySet<java.lang.String> mBugreportAllowlistedPackages;
    private final com.android.server.os.BugreportManagerServiceImpl.BugreportFileManager mBugreportFileManager;
    private final android.content.Context mContext;
    private com.android.server.os.BugreportManagerServiceImpl.DumpstateListener mCurrentDumpstateListener;
    private final android.util.LocalLog mFinishedBugreports;
    private final com.android.server.os.BugreportManagerServiceImpl.Injector mInjector;
    private final java.lang.Object mLock;
    private int mNumberFinishedBugreports;
    private java.util.OptionalInt mPreDumpedDataUid;
    private final android.telephony.TelephonyManager mTelephonyManager;
    private static final long DEFAULT_BUGREPORT_CONSENTLESS_GRACE_PERIOD_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(2);
    private static final com.android.server.os.FeatureFlags sFeatureFlags = new com.android.server.os.FeatureFlagsImpl();

    static class BugreportFileManager {
        private final android.util.AtomicFile mMappingFile;
        private final java.lang.Object mLock = new java.lang.Object();
        private boolean mReadBugreportMapping = false;
        private android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.ArraySet<java.lang.String>> mBugreportFiles = new android.util.ArrayMap<>();
        private java.util.Map<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Boolean>> mConsentGranted = new java.util.HashMap();
        final java.util.Set<java.lang.String> mBugreportFilesToPersist = new java.util.HashSet();

        BugreportFileManager(android.util.AtomicFile mappingFile) {
            this.mMappingFile = mappingFile;
        }

        void ensureCallerPreviouslyGeneratedFile(android.content.Context context, final android.content.pm.PackageManager packageManager, final android.util.Pair<java.lang.Integer, java.lang.String> callingInfo, final int userId, final java.lang.String bugreportFile, boolean forceUpdateMapping) {
            synchronized (this.mLock) {
                if (android.app.admin.flags.Flags.onboardingBugreportV2Enabled()) {
                    int uidForUser = ((java.lang.Integer) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.os.BugreportManagerServiceImpl$BugreportFileManager$$ExternalSyntheticLambda0
                        public final java.lang.Object getOrThrow() {
                            return com.android.server.os.BugreportManagerServiceImpl.BugreportFileManager.lambda$ensureCallerPreviouslyGeneratedFile$0(packageManager, callingInfo, userId, bugreportFile);
                        }
                    })).intValue();
                    if (uidForUser != ((java.lang.Integer) callingInfo.first).intValue() && context.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS") != 0) {
                        throw new java.lang.SecurityException(((java.lang.String) callingInfo.second) + " does not hold the INTERACT_ACROSS_USERS permission to access cross-user bugreports.");
                    }
                    if (!this.mReadBugreportMapping || forceUpdateMapping) {
                        readBugreportMappingLocked();
                    }
                    android.util.ArraySet<java.lang.String> bugreportFilesForUid = this.mBugreportFiles.get(new android.util.Pair(java.lang.Integer.valueOf(uidForUser), (java.lang.String) callingInfo.second));
                    if (bugreportFilesForUid == null || !bugreportFilesForUid.contains(bugreportFile)) {
                        throwInvalidBugreportFileForCallerException(bugreportFile, (java.lang.String) callingInfo.second);
                    }
                    boolean keepBugreportOnRetrieval = false;
                    if (android.app.admin.flags.Flags.onboardingBugreportV2Enabled()) {
                        keepBugreportOnRetrieval = this.mBugreportFilesToPersist.contains(bugreportFile);
                    }
                    if (!keepBugreportOnRetrieval) {
                        bugreportFilesForUid.remove(bugreportFile);
                    }
                } else {
                    android.util.ArraySet<java.lang.String> bugreportFilesForCaller = this.mBugreportFiles.get(callingInfo);
                    if (bugreportFilesForCaller != null && bugreportFilesForCaller.contains(bugreportFile)) {
                        bugreportFilesForCaller.remove(bugreportFile);
                        if (bugreportFilesForCaller.isEmpty()) {
                            this.mBugreportFiles.remove(callingInfo);
                        }
                    } else {
                        throwInvalidBugreportFileForCallerException(bugreportFile, (java.lang.String) callingInfo.second);
                    }
                }
            }
        }

        static /* synthetic */ java.lang.Integer lambda$ensureCallerPreviouslyGeneratedFile$0(android.content.pm.PackageManager packageManager, android.util.Pair callingInfo, int userId, java.lang.String bugreportFile) throws java.lang.Exception {
            try {
                return java.lang.Integer.valueOf(packageManager.getPackageUidAsUser((java.lang.String) callingInfo.second, userId));
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throwInvalidBugreportFileForCallerException(bugreportFile, (java.lang.String) callingInfo.second);
                return -1;
            }
        }

        private static void throwInvalidBugreportFileForCallerException(java.lang.String bugreportFile, java.lang.String packageName) {
            throw new java.lang.IllegalArgumentException("File " + bugreportFile + " was not generated on behalf of calling package " + packageName);
        }

        void addBugreportFileForCaller(android.util.Pair<java.lang.Integer, java.lang.String> caller, java.lang.String bugreportFile, boolean keepOnRetrieval) {
            addBugreportMapping(caller, bugreportFile);
            synchronized (this.mLock) {
                if (android.app.admin.flags.Flags.onboardingBugreportV2Enabled()) {
                    if (keepOnRetrieval) {
                        this.mBugreportFilesToPersist.add(bugreportFile);
                    }
                    writeBugreportDataLocked();
                }
            }
        }

        void logConsentGrantedForCaller(java.lang.String packageName, boolean consentGranted, boolean isDeferredReport) {
            if (!android.app.admin.flags.Flags.onboardingConsentlessBugreports() || !android.os.Build.IS_DEBUGGABLE) {
                return;
            }
            synchronized (this.mLock) {
                try {
                    if (consentGranted) {
                        this.mConsentGranted.put(packageName, new android.util.Pair<>(java.lang.Long.valueOf(java.lang.System.currentTimeMillis()), java.lang.Boolean.valueOf(isDeferredReport)));
                    } else if (!isDeferredReport) {
                        if (!this.mConsentGranted.containsKey(packageName)) {
                            android.util.Slog.e(com.android.server.os.BugreportManagerServiceImpl.TAG, "Previous consent from package: " + packageName + " shouldhave been logged.");
                            return;
                        }
                        this.mConsentGranted.put(packageName, new android.util.Pair<>((java.lang.Long) this.mConsentGranted.get(packageName).first, false));
                    }
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
        }

        boolean canSkipConsentScreen(java.lang.String packageName, boolean isFullReport) {
            if (!android.app.admin.flags.Flags.onboardingConsentlessBugreports() || !android.os.Build.IS_DEBUGGABLE) {
                return false;
            }
            synchronized (this.mLock) {
                if (!this.mConsentGranted.containsKey(packageName)) {
                    return false;
                }
                long currentTime = java.lang.System.currentTimeMillis();
                long consentGrantedTime = ((java.lang.Long) this.mConsentGranted.get(packageName).first).longValue();
                if (com.android.server.os.BugreportManagerServiceImpl.DEFAULT_BUGREPORT_CONSENTLESS_GRACE_PERIOD_MILLIS + consentGrantedTime < currentTime) {
                    this.mConsentGranted.remove(packageName);
                    return false;
                }
                boolean skipConsentForFullReport = ((java.lang.Boolean) this.mConsentGranted.get(packageName).second).booleanValue();
                return !isFullReport || skipConsentForFullReport;
            }
        }

        private void addBugreportMapping(android.util.Pair<java.lang.Integer, java.lang.String> caller, java.lang.String bugreportFile) {
            synchronized (this.mLock) {
                if (!this.mBugreportFiles.containsKey(caller)) {
                    this.mBugreportFiles.put(caller, new android.util.ArraySet<>());
                }
                android.util.ArraySet<java.lang.String> bugreportFilesForCaller = this.mBugreportFiles.get(caller);
                bugreportFilesForCaller.add(bugreportFile);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:9:0x002e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void readBugreportMappingLocked() {
            /*
                r7 = this;
                java.lang.String r0 = "BugreportManagerService"
                android.util.ArrayMap r1 = new android.util.ArrayMap
                r1.<init>()
                r7.mBugreportFiles = r1
                android.util.AtomicFile r1 = r7.mMappingFile     // Catch: java.lang.Throwable -> L7b java.io.FileNotFoundException -> L82
                java.io.FileInputStream r1 = r1.openRead()     // Catch: java.lang.Throwable -> L7b java.io.FileNotFoundException -> L82
                com.android.modules.utils.TypedXmlPullParser r2 = android.util.Xml.resolvePullParser(r1)     // Catch: java.lang.Throwable -> L6f
                java.lang.String r3 = "bugreport-data"
                com.android.internal.util.XmlUtils.beginDocument(r2, r3)     // Catch: java.lang.Throwable -> L6f
                int r3 = r2.getDepth()     // Catch: java.lang.Throwable -> L6f
            L1c:
                boolean r4 = com.android.internal.util.XmlUtils.nextElementWithin(r2, r3)     // Catch: java.lang.Throwable -> L6f
                r5 = 1
                if (r4 == 0) goto L67
                java.lang.String r4 = r2.getName()     // Catch: java.lang.Throwable -> L6f
                int r6 = r4.hashCode()     // Catch: java.lang.Throwable -> L6f
                switch(r6) {
                    case -1731556110: goto L39;
                    case 761901751: goto L2f;
                    default: goto L2e;
                }     // Catch: java.lang.Throwable -> L6f
            L2e:
                goto L43
            L2f:
                java.lang.String r5 = "bugreport-map"
                boolean r5 = r4.equals(r5)     // Catch: java.lang.Throwable -> L6f
                if (r5 == 0) goto L2e
                r5 = 0
                goto L44
            L39:
                java.lang.String r6 = "persistent-bugreport"
                boolean r6 = r4.equals(r6)     // Catch: java.lang.Throwable -> L6f
                if (r6 == 0) goto L2e
                goto L44
            L43:
                r5 = -1
            L44:
                switch(r5) {
                    case 0: goto L4c;
                    case 1: goto L48;
                    default: goto L47;
                }     // Catch: java.lang.Throwable -> L6f
            L47:
                goto L50
            L48:
                r7.readPersistentBugreportEntry(r2)     // Catch: java.lang.Throwable -> L6f
                goto L66
            L4c:
                r7.readBugreportMapEntry(r2)     // Catch: java.lang.Throwable -> L6f
                goto L66
            L50:
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6f
                r5.<init>()     // Catch: java.lang.Throwable -> L6f
                java.lang.String r6 = "Unknown tag while reading bugreport mapping file: "
                java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L6f
                java.lang.StringBuilder r5 = r5.append(r4)     // Catch: java.lang.Throwable -> L6f
                java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L6f
                android.util.Slog.e(r0, r5)     // Catch: java.lang.Throwable -> L6f
            L66:
                goto L1c
            L67:
                r7.mReadBugreportMapping = r5     // Catch: java.lang.Throwable -> L6f
                if (r1 == 0) goto L88
                r1.close()     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L7b java.io.FileNotFoundException -> L82
                goto L88
            L6f:
                r2 = move-exception
                if (r1 == 0) goto L7a
                r1.close()     // Catch: java.lang.Throwable -> L76
                goto L7a
            L76:
                r3 = move-exception
                r2.addSuppressed(r3)     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L7b java.io.FileNotFoundException -> L82
            L7a:
                throw r2     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L7b java.io.FileNotFoundException -> L82
            L7b:
                r0 = move-exception
                android.util.AtomicFile r1 = r7.mMappingFile
                r1.delete()
                goto L89
            L82:
                r1 = move-exception
                java.lang.String r2 = "Bugreport mapping file does not exist"
                android.util.Slog.i(r0, r2)
            L88:
            L89:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.os.BugreportManagerServiceImpl.BugreportFileManager.readBugreportMappingLocked():void");
        }

        private void writeBugreportDataLocked() {
            if (this.mBugreportFiles.isEmpty() && this.mBugreportFilesToPersist.isEmpty()) {
                return;
            }
            try {
                java.io.FileOutputStream stream = this.mMappingFile.startWrite();
                try {
                    com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
                    out.startDocument((java.lang.String) null, true);
                    out.startTag((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.TAG_BUGREPORT_DATA);
                    for (java.util.Map.Entry<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.ArraySet<java.lang.String>> entry : this.mBugreportFiles.entrySet()) {
                        android.util.Pair<java.lang.Integer, java.lang.String> callingInfo = entry.getKey();
                        android.util.ArraySet<java.lang.String> callersBugreports = entry.getValue();
                        for (java.lang.String bugreportFile : callersBugreports) {
                            writeBugreportMapEntry(callingInfo, bugreportFile, out);
                        }
                    }
                    for (java.lang.String file : this.mBugreportFilesToPersist) {
                        writePersistentBugreportEntry(file, out);
                    }
                    out.endTag((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.TAG_BUGREPORT_DATA);
                    out.endDocument();
                    this.mMappingFile.finishWrite(stream);
                    if (stream != null) {
                        stream.close();
                    }
                } finally {
                }
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.os.BugreportManagerServiceImpl.TAG, "Failed to write bugreport mapping file", e);
            }
        }

        private void readBugreportMapEntry(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
            int callingUid = parser.getAttributeInt((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_CALLING_UID);
            java.lang.String callingPackage = parser.getAttributeValue((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_CALLING_PACKAGE);
            java.lang.String bugreportFile = parser.getAttributeValue((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_BUGREPORT_FILE);
            addBugreportMapping(new android.util.Pair<>(java.lang.Integer.valueOf(callingUid), callingPackage), bugreportFile);
        }

        private void readPersistentBugreportEntry(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
            java.lang.String bugreportFile = parser.getAttributeValue((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_BUGREPORT_FILE);
            synchronized (this.mLock) {
                this.mBugreportFilesToPersist.add(bugreportFile);
            }
        }

        private void writeBugreportMapEntry(android.util.Pair<java.lang.Integer, java.lang.String> callingInfo, java.lang.String bugreportFile, com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            out.startTag((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.TAG_BUGREPORT_MAP);
            out.attributeInt((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_CALLING_UID, ((java.lang.Integer) callingInfo.first).intValue());
            out.attribute((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_CALLING_PACKAGE, (java.lang.String) callingInfo.second);
            out.attribute((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_BUGREPORT_FILE, bugreportFile);
            out.endTag((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.TAG_BUGREPORT_MAP);
        }

        private void writePersistentBugreportEntry(java.lang.String bugreportFile, com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            out.startTag((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.TAG_PERSISTENT_BUGREPORT);
            out.attribute((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.ATTR_BUGREPORT_FILE, bugreportFile);
            out.endTag((java.lang.String) null, com.android.server.os.BugreportManagerServiceImpl.TAG_PERSISTENT_BUGREPORT);
        }
    }

    static class Injector {
        android.util.ArraySet<java.lang.String> mAllowlistedPackages;
        android.content.Context mContext;
        android.util.AtomicFile mMappingFile;
        com.android.server.os.BugreportManagerServiceImpl.Injector.RoleManagerWrapper mRoleManagerWrapper = new com.android.server.os.BugreportManagerServiceImpl.Injector.RoleManagerWrapper();

        class RoleManagerWrapper {
            RoleManagerWrapper() {
            }

            java.util.List<java.lang.String> getRoleHolders(java.lang.String roleName) {
                return ((android.app.role.RoleManager) com.android.server.os.BugreportManagerServiceImpl.Injector.this.mContext.getSystemService(android.app.role.RoleManager.class)).getRoleHolders(roleName);
            }
        }

        Injector(android.content.Context context, android.util.ArraySet<java.lang.String> allowlistedPackages, android.util.AtomicFile mappingFile) {
            this.mContext = context;
            this.mAllowlistedPackages = allowlistedPackages;
            this.mMappingFile = mappingFile;
        }

        android.content.Context getContext() {
            return this.mContext;
        }

        android.util.ArraySet<java.lang.String> getAllowlistedPackages() {
            return this.mAllowlistedPackages;
        }

        android.util.AtomicFile getMappingFile() {
            return this.mMappingFile;
        }

        android.os.UserManager getUserManager() {
            return (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        }

        android.app.admin.DevicePolicyManager getDevicePolicyManager() {
            return (android.app.admin.DevicePolicyManager) this.mContext.getSystemService(android.app.admin.DevicePolicyManager.class);
        }

        void setSystemProperty(java.lang.String key, java.lang.String value) {
            android.os.SystemProperties.set(key, value);
        }

        com.android.server.os.BugreportManagerServiceImpl.Injector.RoleManagerWrapper getRoleManagerWrapper() {
            return this.mRoleManagerWrapper;
        }
    }

    BugreportManagerServiceImpl(android.content.Context context) {
        this(new com.android.server.os.BugreportManagerServiceImpl.Injector(context, com.android.server.SystemConfig.getInstance().getBugreportWhitelistedPackages(), new android.util.AtomicFile(new java.io.File(new java.io.File(android.os.Environment.getDataDirectory(), "system"), "bugreport-mapping.xml"))));
    }

    BugreportManagerServiceImpl(com.android.server.os.BugreportManagerServiceImpl.Injector injector) {
        this.mLock = new java.lang.Object();
        this.mPreDumpedDataUid = java.util.OptionalInt.empty();
        this.mFinishedBugreports = new android.util.LocalLog(20);
        this.mInjector = injector;
        this.mContext = injector.getContext();
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mTelephonyManager = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        this.mBugreportFileManager = new com.android.server.os.BugreportManagerServiceImpl.BugreportFileManager(injector.getMappingFile());
        this.mBugreportAllowlistedPackages = injector.getAllowlistedPackages();
    }

    @Override // android.os.IDumpstate
    public void preDumpUiData(java.lang.String callingPackage) {
        enforcePermission(callingPackage, android.os.Binder.getCallingUid(), true);
        synchronized (this.mLock) {
            preDumpUiDataLocked(callingPackage);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v0, types: [int] */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r14v5, types: [android.util.MutableBoolean, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r17v0, types: [com.android.server.os.BugreportManagerServiceImpl] */
    @Override // android.os.IDumpstate
    public void startBugreport(int callingUidUnused, final java.lang.String callingPackage, final java.io.FileDescriptor bugreportFd, final java.io.FileDescriptor screenshotFd, final int bugreportMode, final int bugreportFlags, final android.os.IDumpstateListener listener, final boolean isScreenshotRequested, boolean skipUserConsentUnused) throws java.lang.Throwable {
        ?? r14 = bugreportMode;
        java.util.Objects.requireNonNull(callingPackage);
        java.util.Objects.requireNonNull(bugreportFd);
        java.util.Objects.requireNonNull(listener);
        validateBugreportMode(r14);
        validateBugreportFlags(bugreportFlags);
        final int callingUid = android.os.Binder.getCallingUid();
        enforcePermission(callingPackage, callingUid, r14 == 4);
        ensureUserCanTakeBugReport(r14);
        com.android.server.utils.Slogf.i(TAG, "Starting bugreport for %s / %d", callingPackage, java.lang.Integer.valueOf(callingUid));
        final android.util.MutableBoolean handoffLock = new android.util.MutableBoolean(false);
        if (!sFeatureFlags.asyncStartBugreport()) {
            synchronized (this.mLock) {
                startBugreportLocked(callingUid, callingPackage, bugreportFd, screenshotFd, bugreportMode, bugreportFlags, listener, isScreenshotRequested);
            }
            return;
        }
        synchronized (handoffLock) {
            try {
                try {
                    r14 = handoffLock;
                    new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.os.BugreportManagerServiceImpl$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$startBugreport$0(handoffLock, callingUid, callingPackage, bugreportFd, screenshotFd, bugreportMode, bugreportFlags, listener, isScreenshotRequested);
                        }
                    }, "BugreportManagerServiceThread").start();
                    while (!((android.util.MutableBoolean) r14).value) {
                        try {
                            r14.wait(30000L);
                        } catch (java.lang.InterruptedException e) {
                            android.util.Slog.e(TAG, "Unexpectedly interrupted waiting for startBugreportLocked", e);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    r14 = handoffLock;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startBugreport$0(android.util.MutableBoolean handoffLock, int callingUid, java.lang.String callingPackage, java.io.FileDescriptor bugreportFd, java.io.FileDescriptor screenshotFd, int bugreportMode, int bugreportFlags, android.os.IDumpstateListener listener, boolean isScreenshotRequested) {
        try {
            synchronized (this.mLock) {
                synchronized (handoffLock) {
                    handoffLock.value = true;
                    handoffLock.notifyAll();
                }
                startBugreportLocked(callingUid, callingPackage, bugreportFd, screenshotFd, bugreportMode, bugreportFlags, listener, isScreenshotRequested);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Cannot start a new bugreport due to an unknown error", e);
            reportError(listener, 2);
        }
    }

    @Override // android.os.IDumpstate
    public void cancelBugreport(int callingUidUnused, java.lang.String callingPackage) {
        int callingUid = android.os.Binder.getCallingUid();
        enforcePermission(callingPackage, callingUid, true);
        com.android.server.utils.Slogf.i(TAG, "Cancelling bugreport for %s / %d", callingPackage, java.lang.Integer.valueOf(callingUid));
        synchronized (this.mLock) {
            android.os.IDumpstate ds = getDumpstateBinderServiceLocked();
            if (ds == null) {
                android.util.Slog.w(TAG, "cancelBugreport: Could not find native dumpstate service");
                return;
            }
            try {
                ds.cancelBugreport(callingUid, callingPackage);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException in cancelBugreport", e);
            }
            stopDumpstateBinderServiceLocked();
        }
    }

    @Override // android.os.IDumpstate
    public void retrieveBugreport(int callingUidUnused, java.lang.String callingPackage, int userId, java.io.FileDescriptor bugreportFd, java.lang.String bugreportFile, boolean keepBugreportOnRetrievalUnused, boolean skipUserConsentUnused, android.os.IDumpstateListener listener) throws java.lang.Throwable {
        boolean keepBugreportOnRetrieval;
        int callingUid = android.os.Binder.getCallingUid();
        enforcePermission(callingPackage, callingUid, false);
        com.android.server.utils.Slogf.i(TAG, "Retrieving bugreport for %s / %d", callingPackage, java.lang.Integer.valueOf(callingUid));
        try {
            com.android.server.os.BugreportManagerServiceImpl.BugreportFileManager bugreportFileManager = this.mBugreportFileManager;
            android.content.Context context = this.mContext;
            android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
            java.lang.Object objValueOf = java.lang.Integer.valueOf(callingUid);
            bugreportFileManager.ensureCallerPreviouslyGeneratedFile(context, packageManager, new android.util.Pair<>(objValueOf, callingPackage), userId, bugreportFile, false);
            java.lang.Object obj = this.mLock;
            synchronized (obj) {
                try {
                    try {
                        try {
                            if (isDumpstateBinderServiceRunningLocked()) {
                                android.util.Slog.w(TAG, "'dumpstate' is already running. Cannot retrieve a bugreport while another one is currently in progress.");
                                reportError(listener, 5);
                                return;
                            }
                            android.os.IDumpstate ds = startAndGetDumpstateBinderServiceLocked();
                            if (ds == null) {
                                android.util.Slog.w(TAG, "Unable to get bugreport service");
                                reportError(listener, 2);
                                return;
                            }
                            boolean skipUserConsent = this.mBugreportFileManager.canSkipConsentScreen(callingPackage, false);
                            com.android.server.os.BugreportManagerServiceImpl.DumpstateListener myListener = new com.android.server.os.BugreportManagerServiceImpl.DumpstateListener(this, listener, ds, new android.util.Pair(java.lang.Integer.valueOf(callingUid), callingPackage), true, skipUserConsent ? false : true, true);
                            if (android.app.admin.flags.Flags.onboardingBugreportV2Enabled()) {
                                try {
                                    keepBugreportOnRetrieval = this.mBugreportFileManager.mBugreportFilesToPersist.contains(bugreportFile);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    objValueOf = obj;
                                    throw th;
                                }
                            } else {
                                keepBugreportOnRetrieval = false;
                            }
                            try {
                                setCurrentDumpstateListenerLocked(myListener);
                                objValueOf = obj;
                                try {
                                    ds.retrieveBugreport(callingUid, callingPackage, userId, bugreportFd, bugreportFile, keepBugreportOnRetrieval, skipUserConsent, myListener);
                                } catch (android.os.RemoteException e) {
                                    android.util.Slog.e(TAG, "RemoteException in retrieveBugreport", e);
                                }
                                return;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                objValueOf = obj;
                                throw th;
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
        } catch (java.lang.IllegalArgumentException e2) {
            android.util.Slog.e(TAG, e2.getMessage());
            reportError(listener, 6);
        }
    }

    private void setCurrentDumpstateListenerLocked(com.android.server.os.BugreportManagerServiceImpl.DumpstateListener listener) {
        if (this.mCurrentDumpstateListener != null) {
            com.android.server.utils.Slogf.w(TAG, "setCurrentDumpstateListenerLocked(%s): called when mCurrentDumpstateListener is already set (%s)", listener, this.mCurrentDumpstateListener);
        }
        this.mCurrentDumpstateListener = listener;
    }

    private void validateBugreportMode(int mode) {
        if (mode != 0 && mode != 1 && mode != 2 && mode != 3 && mode != 4 && mode != 5 && mode != 7) {
            android.util.Slog.w(TAG, "Unknown bugreport mode: " + mode);
            throw new java.lang.IllegalArgumentException("Unknown bugreport mode: " + mode);
        }
    }

    private void validateBugreportFlags(int flags) {
        int flags2 = clearBugreportFlag(flags, 7);
        if (flags2 != 0) {
            android.util.Slog.w(TAG, "Unknown bugreport flags: " + flags2);
            throw new java.lang.IllegalArgumentException("Unknown bugreport flags: " + flags2);
        }
    }

    private void enforcePermission(java.lang.String callingPackage, int callingUid, boolean checkCarrierPrivileges) {
        long token;
        this.mAppOps.checkPackage(callingUid, callingPackage);
        boolean allowlisted = this.mBugreportAllowlistedPackages.contains(callingPackage);
        if (!allowlisted) {
            token = android.os.Binder.clearCallingIdentity();
            try {
                allowlisted = this.mInjector.getRoleManagerWrapper().getRoleHolders(ROLE_SYSTEM_AUTOMOTIVE_PROJECTION).contains(callingPackage);
            } finally {
            }
        }
        if (allowlisted && this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") == 0) {
            return;
        }
        token = android.os.Binder.clearCallingIdentity();
        if (checkCarrierPrivileges) {
            try {
                if (this.mTelephonyManager.checkCarrierPrivilegesForPackageAnyPhone(callingPackage) == 1) {
                    return;
                }
            } finally {
            }
        }
        android.os.Binder.restoreCallingIdentity(token);
        java.lang.String message = callingPackage + " does not hold the DUMP permission or is not bugreport-whitelisted or does not have an allowed role " + (checkCarrierPrivileges ? "and does not have carrier privileges " : "") + "to request a bugreport";
        android.util.Slog.w(TAG, message);
        throw new java.lang.SecurityException(message);
    }

    private void ensureUserCanTakeBugReport(int bugreportMode) {
        boolean isAdminUser;
        int effectiveCallingUserId = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo profileParent = this.mInjector.getUserManager().getProfileParent(effectiveCallingUserId);
            if (profileParent == null) {
                isAdminUser = this.mInjector.getUserManager().isUserAdmin(effectiveCallingUserId);
            } else {
                effectiveCallingUserId = profileParent.id;
                isAdminUser = profileParent.isAdmin();
            }
            if (!isAdminUser) {
                if (bugreportMode == 2 && isUserAffiliated(effectiveCallingUserId)) {
                    return;
                }
                logAndThrow(android.text.TextUtils.formatSimple("Calling user %s is not an admin user. Only admin users and their profiles are allowed to take bugreport.", new java.lang.Object[]{java.lang.Integer.valueOf(effectiveCallingUserId)}));
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private boolean isUserAffiliated(int userId) {
        android.app.admin.DevicePolicyManager dpm = this.mInjector.getDevicePolicyManager();
        int deviceOwnerUid = dpm.getDeviceOwnerUserId();
        if (deviceOwnerUid == -10000) {
            return false;
        }
        if (userId != deviceOwnerUid && !dpm.isAffiliatedUser(userId)) {
            logAndThrow("User " + userId + " is not affiliated to the device owner.");
            return true;
        }
        return true;
    }

    private void preDumpUiDataLocked(java.lang.String callingPackage) {
        this.mPreDumpedDataUid = java.util.OptionalInt.empty();
        if (isDumpstateBinderServiceRunningLocked()) {
            android.util.Slog.e(TAG, "'dumpstate' is already running. Cannot pre-dump data while another operation is currently in progress.");
            return;
        }
        android.os.IDumpstate ds = startAndGetDumpstateBinderServiceLocked();
        if (ds == null) {
            android.util.Slog.e(TAG, "Unable to get bugreport service");
            return;
        }
        try {
            ds.preDumpUiData(callingPackage);
            stopDumpstateBinderServiceLocked();
            this.mPreDumpedDataUid = java.util.OptionalInt.of(android.os.Binder.getCallingUid());
        } catch (android.os.RemoteException e) {
            stopDumpstateBinderServiceLocked();
        } catch (java.lang.Throwable th) {
            stopDumpstateBinderServiceLocked();
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void startBugreportLocked(int r26, java.lang.String r27, java.io.FileDescriptor r28, java.io.FileDescriptor r29, int r30, int r31, android.os.IDumpstateListener r32, boolean r33) {
        /*
            r25 = this;
            r10 = r25
            r15 = r27
            r0 = r31
            r14 = r32
            boolean r1 = r25.isDumpstateBinderServiceRunningLocked()
            java.lang.String r2 = "BugreportManagerService"
            if (r1 == 0) goto L1a
            java.lang.String r1 = "'dumpstate' is already running. Cannot start a new bugreport while another operation is currently in progress."
            android.util.Slog.w(r2, r1)
            r1 = 5
            r10.reportError(r14, r1)
            return
        L1a:
            r1 = r0 & 1
            r3 = 1
            if (r1 == 0) goto L4b
            java.util.OptionalInt r1 = r10.mPreDumpedDataUid
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L35
            int r0 = r10.clearBugreportFlag(r0, r3)
            java.lang.String r1 = "Ignoring BUGREPORT_FLAG_USE_PREDUMPED_UI_DATA. No pre-dumped data is available."
            android.util.Slog.w(r2, r1)
            r13 = r26
            r21 = r0
            goto L4f
        L35:
            java.util.OptionalInt r1 = r10.mPreDumpedDataUid
            int r1 = r1.getAsInt()
            r13 = r26
            if (r1 == r13) goto L4d
            int r0 = r10.clearBugreportFlag(r0, r3)
            java.lang.String r1 = "Ignoring BUGREPORT_FLAG_USE_PREDUMPED_UI_DATA. Data was pre-dumped by a different UID."
            android.util.Slog.w(r2, r1)
            r21 = r0
            goto L4f
        L4b:
            r13 = r26
        L4d:
            r21 = r0
        L4f:
            r0 = r21 & 2
            r1 = 0
            if (r0 == 0) goto L56
            r0 = r3
            goto L57
        L56:
            r0 = r1
        L57:
            r22 = r0
            r0 = r21 & 4
            if (r0 == 0) goto L5f
            r7 = r3
            goto L60
        L5f:
            r7 = r1
        L60:
            android.os.IDumpstate r23 = r25.startAndGetDumpstateBinderServiceLocked()
            if (r23 != 0) goto L70
            java.lang.String r0 = "Unable to get bugreport service"
            android.util.Slog.w(r2, r0)
            r0 = 2
            r10.reportError(r14, r0)
            return
        L70:
            com.android.server.os.BugreportManagerServiceImpl$BugreportFileManager r0 = r10.mBugreportFileManager
            if (r22 != 0) goto L76
            r2 = r3
            goto L77
        L76:
            r2 = r1
        L77:
            boolean r24 = r0.canSkipConsentScreen(r15, r2)
            com.android.server.os.BugreportManagerServiceImpl$DumpstateListener r0 = new com.android.server.os.BugreportManagerServiceImpl$DumpstateListener
            android.util.Pair r5 = new android.util.Pair
            java.lang.Integer r2 = java.lang.Integer.valueOf(r26)
            r5.<init>(r2, r15)
            if (r22 != 0) goto L8c
            if (r24 != 0) goto L8c
            r8 = r3
            goto L8d
        L8c:
            r8 = r1
        L8d:
            r1 = r0
            r2 = r25
            r3 = r32
            r4 = r23
            r6 = r22
            r9 = r22
            r1.<init>(r3, r4, r5, r6, r7, r8, r9)
            r10.setCurrentDumpstateListenerLocked(r1)
            r11 = r23
            r12 = r26
            r13 = r27
            r14 = r28
            r15 = r29
            r16 = r30
            r17 = r21
            r18 = r1
            r19 = r33
            r20 = r24
            r11.startBugreport(r12, r13, r14, r15, r16, r17, r18, r19, r20)     // Catch: android.os.RemoteException -> Lb6
            goto Lbc
        Lb6:
            r0 = move-exception
            r2 = r0
            r0 = r2
            r25.cancelBugreport(r26, r27)
        Lbc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.os.BugreportManagerServiceImpl.startBugreportLocked(int, java.lang.String, java.io.FileDescriptor, java.io.FileDescriptor, int, int, android.os.IDumpstateListener, boolean):void");
    }

    private boolean isDumpstateBinderServiceRunningLocked() {
        return getDumpstateBinderServiceLocked() != null;
    }

    private android.os.IDumpstate getDumpstateBinderServiceLocked() {
        return android.os.IDumpstate.Stub.asInterface(android.os.ServiceManager.getService("dumpstate"));
    }

    private android.os.IDumpstate startAndGetDumpstateBinderServiceLocked() {
        this.mInjector.setSystemProperty("ctl.start", BUGREPORT_SERVICE);
        android.os.IDumpstate ds = null;
        boolean timedOut = false;
        int totalTimeWaitedMillis = 0;
        int seedWaitTimeMillis = 500;
        while (true) {
            if (timedOut) {
                break;
            }
            ds = getDumpstateBinderServiceLocked();
            if (ds != null) {
                android.util.Slog.i(TAG, "Got bugreport service handle.");
                break;
            }
            android.os.SystemClock.sleep(seedWaitTimeMillis);
            android.util.Slog.i(TAG, "Waiting to get dumpstate service handle (" + totalTimeWaitedMillis + "ms)");
            totalTimeWaitedMillis += seedWaitTimeMillis;
            seedWaitTimeMillis *= 2;
            timedOut = ((long) totalTimeWaitedMillis) > 30000;
        }
        if (timedOut) {
            android.util.Slog.w(TAG, "Timed out waiting to get dumpstate service handle (" + totalTimeWaitedMillis + "ms)");
        }
        return ds;
    }

    private void stopDumpstateBinderServiceLocked() {
        this.mInjector.setSystemProperty("ctl.stop", BUGREPORT_SERVICE);
    }

    @Override // android.os.Binder
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            pw.printf("Allow-listed packages: %s\n", this.mBugreportAllowlistedPackages);
            synchronized (this.mLock) {
                pw.print("Pre-dumped data UID: ");
                if (this.mPreDumpedDataUid.isEmpty()) {
                    pw.println("none");
                } else {
                    pw.println(this.mPreDumpedDataUid.getAsInt());
                }
                if (this.mCurrentDumpstateListener == null) {
                    pw.println("Not taking a bug report");
                } else {
                    this.mCurrentDumpstateListener.dump(pw);
                }
                if (this.mNumberFinishedBugreports == 0) {
                    pw.println("No finished bugreports");
                } else {
                    pw.printf("%d finished bugreport%s. Last %d:\n", java.lang.Integer.valueOf(this.mNumberFinishedBugreports), this.mNumberFinishedBugreports > 1 ? "s" : "", java.lang.Integer.valueOf(java.lang.Math.min(this.mNumberFinishedBugreports, 20)));
                    this.mFinishedBugreports.dump("  ", pw);
                }
            }
            synchronized (this.mBugreportFileManager.mLock) {
                if (!this.mBugreportFileManager.mReadBugreportMapping) {
                    pw.println("Has not read bugreport mapping");
                }
                int numberFiles = this.mBugreportFileManager.mBugreportFiles.size();
                pw.printf("%d pending file%s", java.lang.Integer.valueOf(numberFiles), numberFiles > 1 ? "s" : "");
                if (numberFiles > 0) {
                    for (int i = 0; i < numberFiles; i++) {
                        android.util.Pair<java.lang.Integer, java.lang.String> caller = (android.util.Pair) this.mBugreportFileManager.mBugreportFiles.keyAt(i);
                        android.util.ArraySet<java.lang.String> files = (android.util.ArraySet) this.mBugreportFileManager.mBugreportFiles.valueAt(i);
                        pw.printf("  %s: %s\n", callerToString(caller), files);
                    }
                } else {
                    pw.println();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String callerToString(android.util.Pair<java.lang.Integer, java.lang.String> caller) {
        return caller == null ? "N/A" : ((java.lang.String) caller.second) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + caller.first;
    }

    private int clearBugreportFlag(int flags, int flag) {
        return flags & (~flag);
    }

    private void reportError(android.os.IDumpstateListener listener, int errorCode) {
        try {
            listener.onError(errorCode);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "onError() transaction threw RemoteException: " + e.getMessage());
        }
    }

    private void logAndThrow(java.lang.String message) {
        android.util.Slog.w(TAG, message);
        throw new java.lang.IllegalArgumentException(message);
    }

    private final class DumpstateListener extends android.os.IDumpstateListener.Stub implements android.os.IBinder.DeathRecipient {
        private static int sNextId;
        private final android.util.Pair<java.lang.Integer, java.lang.String> mCaller;
        private boolean mConsentGranted;
        private boolean mDone;
        private final android.os.IDumpstate mDs;
        private final int mId;
        private boolean mIsDeferredReport;
        private boolean mKeepBugreportOnRetrieval;
        private final android.os.IDumpstateListener mListener;
        private int mProgress;
        private final boolean mReportFinishedFile;

        DumpstateListener(com.android.server.os.BugreportManagerServiceImpl bugreportManagerServiceImpl, android.os.IDumpstateListener listener, android.os.IDumpstate ds, android.util.Pair<java.lang.Integer, java.lang.String> caller, boolean reportFinishedFile, boolean consentGranted, boolean isDeferredReport) {
            this(listener, ds, caller, reportFinishedFile, false, consentGranted, isDeferredReport);
        }

        DumpstateListener(android.os.IDumpstateListener listener, android.os.IDumpstate ds, android.util.Pair<java.lang.Integer, java.lang.String> caller, boolean reportFinishedFile, boolean keepBugreportOnRetrieval, boolean consentGranted, boolean isDeferredReport) {
            int i = sNextId + 1;
            sNextId = i;
            this.mId = i;
            this.mListener = listener;
            this.mDs = ds;
            this.mCaller = caller;
            this.mReportFinishedFile = reportFinishedFile;
            this.mKeepBugreportOnRetrieval = keepBugreportOnRetrieval;
            this.mConsentGranted = consentGranted;
            this.mIsDeferredReport = isDeferredReport;
            try {
                this.mDs.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.os.BugreportManagerServiceImpl.TAG, "Unable to register Death Recipient for IDumpstate", e);
            }
        }

        @Override // android.os.IDumpstateListener
        public void onProgress(int progress) throws android.os.RemoteException {
            this.mProgress = progress;
            this.mListener.onProgress(progress);
        }

        @Override // android.os.IDumpstateListener
        public void onError(int errorCode) throws android.os.RemoteException {
            com.android.server.utils.Slogf.e(com.android.server.os.BugreportManagerServiceImpl.TAG, "onError(): %d", java.lang.Integer.valueOf(errorCode));
            synchronized (com.android.server.os.BugreportManagerServiceImpl.this.mLock) {
                releaseItselfLocked();
                reportFinishedLocked("ErroCode: " + errorCode);
            }
            this.mListener.onError(errorCode);
        }

        @Override // android.os.IDumpstateListener
        public void onFinished(java.lang.String bugreportFile) throws android.os.RemoteException {
            com.android.server.utils.Slogf.i(com.android.server.os.BugreportManagerServiceImpl.TAG, "onFinished(): %s", bugreportFile);
            synchronized (com.android.server.os.BugreportManagerServiceImpl.this.mLock) {
                releaseItselfLocked();
                reportFinishedLocked("File: " + bugreportFile);
            }
            if (this.mReportFinishedFile) {
                com.android.server.os.BugreportManagerServiceImpl.this.mBugreportFileManager.addBugreportFileForCaller(this.mCaller, bugreportFile, this.mKeepBugreportOnRetrieval);
            }
            com.android.server.os.BugreportManagerServiceImpl.this.mBugreportFileManager.logConsentGrantedForCaller((java.lang.String) this.mCaller.second, this.mConsentGranted, this.mIsDeferredReport);
            this.mListener.onFinished(bugreportFile);
        }

        @Override // android.os.IDumpstateListener
        public void onScreenshotTaken(boolean success) throws android.os.RemoteException {
            this.mListener.onScreenshotTaken(success);
        }

        @Override // android.os.IDumpstateListener
        public void onUiIntensiveBugreportDumpsFinished() throws android.os.RemoteException {
            this.mListener.onUiIntensiveBugreportDumpsFinished();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                java.lang.Thread.sleep(1000L);
            } catch (java.lang.InterruptedException e) {
            }
            synchronized (com.android.server.os.BugreportManagerServiceImpl.this.mLock) {
                if (!this.mDone) {
                    android.util.Slog.e(com.android.server.os.BugreportManagerServiceImpl.TAG, "IDumpstate likely crashed. Notifying listener");
                    try {
                        this.mListener.onError(2);
                    } catch (android.os.RemoteException e2) {
                    }
                }
            }
            this.mDs.asBinder().unlinkToDeath(this, 0);
        }

        public java.lang.String toString() {
            return "DumpstateListener[id=" + this.mId + ", progress=" + this.mProgress + "]";
        }

        private void reportFinishedLocked(java.lang.String message) {
            com.android.server.os.BugreportManagerServiceImpl.this.mNumberFinishedBugreports++;
            com.android.server.os.BugreportManagerServiceImpl.this.mFinishedBugreports.log("Caller: " + com.android.server.os.BugreportManagerServiceImpl.callerToString(this.mCaller) + " " + message);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw) {
            pw.println("DumpstateListener:");
            pw.printf("  id: %d\n", java.lang.Integer.valueOf(this.mId));
            pw.printf("  caller: %s\n", com.android.server.os.BugreportManagerServiceImpl.callerToString(this.mCaller));
            pw.printf("  reports finished file: %b\n", java.lang.Boolean.valueOf(this.mReportFinishedFile));
            pw.printf("  progress: %d\n", java.lang.Integer.valueOf(this.mProgress));
            pw.printf("  done: %b\n", java.lang.Boolean.valueOf(this.mDone));
        }

        private void releaseItselfLocked() {
            this.mDone = true;
            if (com.android.server.os.BugreportManagerServiceImpl.this.mCurrentDumpstateListener == this) {
                com.android.server.os.BugreportManagerServiceImpl.this.mCurrentDumpstateListener = null;
            } else {
                com.android.server.utils.Slogf.w(com.android.server.os.BugreportManagerServiceImpl.TAG, "releaseItselfLocked(): " + this + " is finished, but current listener is " + com.android.server.os.BugreportManagerServiceImpl.this.mCurrentDumpstateListener);
            }
        }
    }
}
