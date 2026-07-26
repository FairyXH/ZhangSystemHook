package com.android.server.grammaticalinflection;

/* JADX INFO: loaded from: classes2.dex */
public class GrammaticalInflectionService extends com.android.server.SystemService {
    private static final java.lang.String ATTR_NAME = "grammatical_gender";
    private static final java.lang.String GRAMMATICAL_GENDER_PROPERTY = "persist.sys.grammatical_gender";
    private static final java.lang.String GRAMMATICAL_INFLECTION_ENABLED = "i18n.grammatical_Inflection.enabled";
    private static final java.lang.String TAG = "GrammaticalInflection";
    private static final java.lang.String TAG_GRAMMATICAL_INFLECTION = "grammatical_inflection";
    private static final java.lang.String USER_SETTINGS_FILE_NAME = "user_settings.xml";
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private final com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper mBackupHelper;
    private com.android.server.grammaticalinflection.GrammaticalInflectionService.GrammaticalInflectionBinderService mBinderService;
    private android.content.Context mContext;
    private final android.util.SparseIntArray mGrammaticalGenderCache;
    private final java.lang.Object mLock;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private android.permission.PermissionManager mPermissionManager;

    public GrammaticalInflectionService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mGrammaticalGenderCache = new android.util.SparseIntArray();
        this.mContext = context;
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mBackupHelper = new com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper(this.mContext.getAttributionSource(), this, context.getPackageManager());
        this.mBinderService = new com.android.server.grammaticalinflection.GrammaticalInflectionService.GrammaticalInflectionBinderService();
        this.mPermissionManager = (android.permission.PermissionManager) context.getSystemService(android.permission.PermissionManager.class);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(TAG_GRAMMATICAL_INFLECTION, this.mBinderService);
        com.android.server.LocalServices.addService(com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal.class, new com.android.server.grammaticalinflection.GrammaticalInflectionService.GrammaticalInflectionManagerInternalImpl());
    }

    private final class GrammaticalInflectionBinderService extends android.app.IGrammaticalInflectionManager.Stub {
        private GrammaticalInflectionBinderService() {
        }

        public void setRequestedApplicationGrammaticalGender(java.lang.String appPackageName, int userId, int gender) {
            com.android.server.grammaticalinflection.GrammaticalInflectionService.this.setRequestedApplicationGrammaticalGender(appPackageName, userId, gender);
        }

        public void setSystemWideGrammaticalGender(int grammaticalGender, int userId) {
            com.android.server.grammaticalinflection.GrammaticalInflectionService.this.enforceCallerPermissions();
            com.android.server.grammaticalinflection.GrammaticalInflectionService.this.setSystemWideGrammaticalGender(grammaticalGender, userId);
        }

        public int getSystemGrammaticalGender(android.content.AttributionSource attributionSource, int userId) {
            if (!com.android.server.grammaticalinflection.GrammaticalInflectionUtils.checkSystemGrammaticalGenderPermission(com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mPermissionManager, attributionSource)) {
                throw new java.lang.SecurityException("AttributionSource: " + attributionSource + " does not have READ_SYSTEM_GRAMMATICAL_GENDER permission.");
            }
            if (com.android.server.grammaticalinflection.GrammaticalInflectionService.checkSystemTermsOfAddressIsEnabled()) {
                return com.android.server.grammaticalinflection.GrammaticalInflectionService.this.getSystemGrammaticalGender(userId);
            }
            return 0;
        }

        public int peekSystemGrammaticalGenderByUserId(android.content.AttributionSource attributionSource, int userId) {
            if (com.android.server.grammaticalinflection.GrammaticalInflectionService.this.canGetSystemGrammaticalGender(attributionSource)) {
                return com.android.server.grammaticalinflection.GrammaticalInflectionService.this.getSystemGrammaticalGender(userId);
            }
            return 0;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.grammaticalinflection.GrammaticalInflectionShellCommand(com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mBinderService, com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mContext.getAttributionSource()).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    private final class GrammaticalInflectionManagerInternalImpl extends com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal {
        private GrammaticalInflectionManagerInternalImpl() {
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public byte[] getBackupPayload(int userId) {
            com.android.server.grammaticalinflection.GrammaticalInflectionService.this.enforceCallerPermissions();
            return com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mBackupHelper.getBackupPayload(userId);
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public void stageAndApplyRestoredPayload(byte[] payload, int userId) {
            com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mBackupHelper.stageAndApplyRestoredPayload(payload, userId);
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public byte[] getSystemBackupPayload(int userId) {
            com.android.server.grammaticalinflection.GrammaticalInflectionService.this.enforceCallerPermissions();
            return com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mBackupHelper.getSystemBackupPayload(userId);
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public void applyRestoredSystemPayload(byte[] payload, int userId) {
            com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mBackupHelper.applyRestoredSystemPayload(payload, userId);
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public int getSystemGrammaticalGender(int userId) {
            if (com.android.server.grammaticalinflection.GrammaticalInflectionService.checkSystemTermsOfAddressIsEnabled()) {
                return com.android.server.grammaticalinflection.GrammaticalInflectionService.this.getSystemGrammaticalGender(userId);
            }
            return 0;
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public int mergedFinalSystemGrammaticalGender() {
            int systemGrammaticalGender = getSystemGrammaticalGender(com.android.server.grammaticalinflection.GrammaticalInflectionService.this.mContext.getUserId());
            if (systemGrammaticalGender == 0) {
                systemGrammaticalGender = getGrammaticalGenderFromDeveloperSettings();
            }
            if (systemGrammaticalGender == -1) {
                return 0;
            }
            return systemGrammaticalGender;
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public int getGrammaticalGenderFromDeveloperSettings() {
            return android.os.SystemProperties.getInt(com.android.server.grammaticalinflection.GrammaticalInflectionService.GRAMMATICAL_GENDER_PROPERTY, 0);
        }

        @Override // com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal
        public boolean canGetSystemGrammaticalGender(int uid) {
            if (uid == 1000) {
                return true;
            }
            android.content.AttributionSource attributionSource = new android.content.AttributionSource.Builder(uid).build();
            return com.android.server.grammaticalinflection.GrammaticalInflectionService.this.canGetSystemGrammaticalGender(attributionSource);
        }
    }

    protected int getApplicationGrammaticalGender(java.lang.String appPackageName, int userId) {
        com.android.server.wm.ActivityTaskManagerInternal.PackageConfig appConfig = this.mActivityTaskManagerInternal.getApplicationConfig(appPackageName, userId);
        if (appConfig == null || appConfig.mGrammaticalGender == null) {
            return 0;
        }
        return appConfig.mGrammaticalGender.intValue();
    }

    protected void setRequestedApplicationGrammaticalGender(java.lang.String appPackageName, int userId, int gender) {
        int preValue = getApplicationGrammaticalGender(appPackageName, userId);
        com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater updater = this.mActivityTaskManagerInternal.createPackageConfigurationUpdater(appPackageName, userId);
        if (!android.os.SystemProperties.getBoolean(GRAMMATICAL_INFLECTION_ENABLED, true)) {
            if (preValue != 0) {
                android.util.Log.d(TAG, "Clearing the user's grammatical gender setting");
                updater.setGrammaticalGender(0).commit();
                return;
            }
            return;
        }
        int uid = this.mPackageManagerInternal.getPackageUid(appPackageName, 0L, userId);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APPLICATION_GRAMMATICAL_INFLECTION_CHANGED, 2, uid, gender != 0, preValue != 0);
        updater.setGrammaticalGender(gender).commit();
    }

    protected void setSystemWideGrammaticalGender(int grammaticalGender, int userId) {
        try {
            if (!checkSystemTermsOfAddressIsEnabled()) {
                return;
            }
            android.os.Trace.beginSection("GrammaticalInflectionService.setSystemWideGrammaticalGender");
            if (!android.app.GrammaticalInflectionManager.VALID_GRAMMATICAL_GENDER_VALUES.contains(java.lang.Integer.valueOf(grammaticalGender))) {
                throw new java.lang.IllegalArgumentException("Unknown grammatical gender");
            }
            java.io.File file = getGrammaticalGenderFile(userId);
            synchronized (this.mLock) {
                android.util.AtomicFile atomicFile = new android.util.AtomicFile(file);
                java.io.FileOutputStream stream = null;
                try {
                    stream = atomicFile.startWrite();
                    stream.write(toXmlByteArray(grammaticalGender, stream));
                    atomicFile.finishWrite(stream);
                    this.mGrammaticalGenderCache.put(userId, grammaticalGender);
                } catch (java.io.IOException e) {
                    android.util.Log.e(TAG, "Failed to write file " + atomicFile, e);
                    if (stream != null) {
                        atomicFile.failWrite(stream);
                    }
                    throw new java.lang.RuntimeException(e);
                }
            }
            updateConfiguration(grammaticalGender, userId);
        } finally {
            android.os.Trace.endSection();
        }
    }

    private static void updateConfiguration(int grammaticalGender, int userId) {
        try {
            android.content.res.Configuration config = new android.content.res.Configuration();
            int preValue = config.getGrammaticalGender();
            config.setGrammaticalGender(grammaticalGender);
            android.app.ActivityTaskManager.getService().updateConfiguration(config);
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SYSTEM_GRAMMATICAL_INFLECTION_CHANGED, 1, userId, grammaticalGender != 0, preValue != 0);
            com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper.notifyBackupManager();
        } catch (android.os.RemoteException e) {
            android.util.Log.w(TAG, "Can not update configuration", e);
        }
    }

    public int getSystemGrammaticalGender(int userId) {
        int i;
        synchronized (this.mLock) {
            int grammaticalGender = this.mGrammaticalGenderCache.get(userId);
            i = grammaticalGender < 0 ? 0 : grammaticalGender;
        }
        return i;
    }

    private static java.io.File getGrammaticalGenderFile(int userId) {
        java.io.File dir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), TAG_GRAMMATICAL_INFLECTION);
        return new java.io.File(dir, USER_SETTINGS_FILE_NAME);
    }

    private static byte[] toXmlByteArray(int grammaticalGender, java.io.FileOutputStream fileStream) throws java.io.IOException {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fileStream);
        out.setOutput(outputStream, java.nio.charset.StandardCharsets.UTF_8.name());
        out.startDocument((java.lang.String) null, true);
        out.startTag((java.lang.String) null, TAG_GRAMMATICAL_INFLECTION);
        out.attributeInt((java.lang.String) null, ATTR_NAME, grammaticalGender);
        out.endTag((java.lang.String) null, TAG_GRAMMATICAL_INFLECTION);
        out.endDocument();
        return outputStream.toByteArray();
    }

    private static int getGrammaticalGenderFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.internal.util.XmlUtils.nextElement(parser);
        while (parser.getEventType() != 1) {
            java.lang.String tagName = parser.getName();
            if (TAG_GRAMMATICAL_INFLECTION.equals(tagName)) {
                return parser.getAttributeInt((java.lang.String) null, ATTR_NAME);
            }
            com.android.internal.util.XmlUtils.nextElement(parser);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCallerPermissions() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000 && callingUid != 2000 && callingUid != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.CHANGE_CONFIGURATION", "Caller must be system, shell, root or hold CHANGE_CONFIGURATION permission.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean checkSystemTermsOfAddressIsEnabled() {
        if (!android.app.Flags.systemTermsOfAddressEnabled()) {
            android.util.Log.d(TAG, "The flag must be enabled to allow calling the API.");
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canGetSystemGrammaticalGender(android.content.AttributionSource attributionSource) {
        return checkSystemTermsOfAddressIsEnabled() && com.android.server.grammaticalinflection.GrammaticalInflectionUtils.checkSystemGrammaticalGenderPermission(this.mPermissionManager, attributionSource);
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocked(final com.android.server.SystemService.TargetUser user) {
        if (!checkSystemTermsOfAddressIsEnabled()) {
            return;
        }
        com.android.server.IoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.grammaticalinflection.GrammaticalInflectionService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserUnlocked$0(user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserUnlocked$0(com.android.server.SystemService.TargetUser user) {
        int userId = user.getUserIdentifier();
        java.io.File file = getGrammaticalGenderFile(userId);
        synchronized (this.mLock) {
            if (!file.exists()) {
                android.util.Log.d(TAG, "User " + userId + " doesn't have the grammatical gender file.");
                return;
            }
            if (this.mGrammaticalGenderCache.indexOfKey(userId) >= 0) {
                return;
            }
            try {
                java.io.FileInputStream in = new java.io.FileInputStream(file);
                try {
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                    int grammaticalGender = getGrammaticalGenderFromXml(parser);
                    this.mGrammaticalGenderCache.put(userId, grammaticalGender);
                    in.close();
                    updateConfiguration(grammaticalGender, userId);
                } catch (java.lang.Throwable th) {
                    try {
                        in.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Log.e(TAG, "Failed to parse XML configuration from " + file, e);
            }
        }
    }
}
