package com.android.server.locales;

/* JADX INFO: loaded from: classes2.dex */
public class LocaleManagerService extends com.android.server.SystemService {
    private static final java.lang.String ATTR_NAME = "name";
    public static final boolean DEBUG = false;
    private static final java.lang.String LOCALE_CONFIGS = "locale_configs";
    private static final java.lang.String PROP_ALLOW_IME_QUERY_APP_LOCALE = "i18n.feature.allow_ime_query_app_locale";
    private static final java.lang.String PROP_DYNAMIC_LOCALES_CHANGE = "i18n.feature.dynamic_locales_change";
    private static final java.lang.String SUFFIX_FILE_NAME = ".xml";
    private static final java.lang.String TAG = "LocaleManagerService";
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private com.android.server.locales.LocaleManagerBackupHelper mBackupHelper;
    private final com.android.server.locales.LocaleManagerService.LocaleManagerBinderService mBinderService;
    final android.content.Context mContext;
    private android.content.pm.PackageManager mPackageManager;
    private final com.android.internal.content.PackageMonitor mPackageMonitor;
    private final java.lang.Object mWriteLock;

    public LocaleManagerService(android.content.Context context) {
        super(context);
        this.mWriteLock = new java.lang.Object();
        this.mContext = context;
        this.mBinderService = new com.android.server.locales.LocaleManagerService.LocaleManagerBinderService();
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mPackageManager = this.mContext.getPackageManager();
        android.os.HandlerThread broadcastHandlerThread = new android.os.HandlerThread(TAG, 10);
        broadcastHandlerThread.start();
        final com.android.server.locales.SystemAppUpdateTracker systemAppUpdateTracker = new com.android.server.locales.SystemAppUpdateTracker(this);
        broadcastHandlerThread.getThreadHandler().postAtFrontOfQueue(new java.lang.Runnable() { // from class: com.android.server.locales.LocaleManagerService.1
            @Override // java.lang.Runnable
            public void run() {
                systemAppUpdateTracker.init();
            }
        });
        this.mBackupHelper = new com.android.server.locales.LocaleManagerBackupHelper(this, this.mPackageManager, broadcastHandlerThread);
        this.mPackageMonitor = new com.android.server.locales.LocaleManagerServicePackageMonitor(this.mBackupHelper, systemAppUpdateTracker, this);
        this.mPackageMonitor.register(context, broadcastHandlerThread.getLooper(), android.os.UserHandle.ALL, true);
    }

    LocaleManagerService(android.content.Context context, com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal, android.app.ActivityManagerInternal activityManagerInternal, android.content.pm.PackageManager packageManager, com.android.server.locales.LocaleManagerBackupHelper localeManagerBackupHelper, com.android.internal.content.PackageMonitor packageMonitor) {
        super(context);
        this.mWriteLock = new java.lang.Object();
        this.mContext = context;
        this.mBinderService = new com.android.server.locales.LocaleManagerService.LocaleManagerBinderService();
        this.mActivityTaskManagerInternal = activityTaskManagerInternal;
        this.mActivityManagerInternal = activityManagerInternal;
        this.mPackageManager = packageManager;
        this.mBackupHelper = localeManagerBackupHelper;
        this.mPackageMonitor = packageMonitor;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE, this.mBinderService);
        com.android.server.LocalServices.addService(com.android.server.locales.LocaleManagerInternal.class, new com.android.server.locales.LocaleManagerService.LocaleManagerInternalImpl());
    }

    private final class LocaleManagerInternalImpl extends com.android.server.locales.LocaleManagerInternal {
        private LocaleManagerInternalImpl() {
        }

        @Override // com.android.server.locales.LocaleManagerInternal
        public byte[] getBackupPayload(int userId) {
            checkCallerIsSystem();
            return com.android.server.locales.LocaleManagerService.this.mBackupHelper.getBackupPayload(userId);
        }

        @Override // com.android.server.locales.LocaleManagerInternal
        public void stageAndApplyRestoredPayload(byte[] payload, int userId) {
            com.android.server.locales.LocaleManagerService.this.mBackupHelper.stageAndApplyRestoredPayload(payload, userId);
        }

        private void checkCallerIsSystem() {
            if (android.os.Binder.getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Caller is not system.");
            }
        }
    }

    private final class LocaleManagerBinderService extends android.app.ILocaleManager.Stub {
        private LocaleManagerBinderService() {
        }

        public void setApplicationLocales(java.lang.String appPackageName, int userId, android.os.LocaleList locales, boolean fromDelegate) throws android.os.RemoteException {
            int caller;
            if (fromDelegate) {
                caller = 1;
            } else {
                caller = 2;
            }
            com.android.server.locales.LocaleManagerService.this.setApplicationLocales(appPackageName, userId, locales, fromDelegate, caller);
        }

        public android.os.LocaleList getApplicationLocales(java.lang.String appPackageName, int userId) throws android.os.RemoteException {
            return com.android.server.locales.LocaleManagerService.this.getApplicationLocales(appPackageName, userId);
        }

        public android.os.LocaleList getSystemLocales() throws android.os.RemoteException {
            return com.android.server.locales.LocaleManagerService.this.getSystemLocales();
        }

        public void setOverrideLocaleConfig(java.lang.String appPackageName, int userId, android.app.LocaleConfig localeConfig) throws android.os.RemoteException {
            com.android.server.locales.LocaleManagerService.this.setOverrideLocaleConfig(appPackageName, userId, localeConfig);
        }

        public android.app.LocaleConfig getOverrideLocaleConfig(java.lang.String appPackageName, int userId) {
            return com.android.server.locales.LocaleManagerService.this.getOverrideLocaleConfig(appPackageName, userId);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.locales.LocaleManagerShellCommand(com.android.server.locales.LocaleManagerService.this.mBinderService).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    public void setApplicationLocales(java.lang.String appPackageName, int userId, android.os.LocaleList locales, boolean fromDelegate, int caller) throws android.os.RemoteException, java.lang.IllegalArgumentException {
        com.android.server.locales.AppLocaleChangedAtomRecord atomRecordForMetrics = new com.android.server.locales.AppLocaleChangedAtomRecord(android.os.Binder.getCallingUid());
        try {
            java.util.Objects.requireNonNull(appPackageName);
            java.util.Objects.requireNonNull(locales);
            atomRecordForMetrics.setCaller(caller);
            atomRecordForMetrics.setNewLocales(locales.toLanguageTags());
            int userId2 = this.mActivityManagerInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 0, "setApplicationLocales", (java.lang.String) null);
            boolean isCallerOwner = isPackageOwnedByCaller(appPackageName, userId2, atomRecordForMetrics, null);
            if (!isCallerOwner) {
                enforceChangeConfigurationPermission(atomRecordForMetrics);
            }
            this.mBackupHelper.persistLocalesModificationInfo(userId2, appPackageName, fromDelegate, locales.isEmpty());
            long token = android.os.Binder.clearCallingIdentity();
            try {
                setApplicationLocalesUnchecked(appPackageName, userId2, locales, atomRecordForMetrics);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        } finally {
            logAppLocalesMetric(atomRecordForMetrics);
        }
    }

    private void setApplicationLocalesUnchecked(java.lang.String appPackageName, int userId, android.os.LocaleList locales, com.android.server.locales.AppLocaleChangedAtomRecord atomRecordForMetrics) {
        atomRecordForMetrics.setPrevLocales(getApplicationLocalesUnchecked(appPackageName, userId).toLanguageTags());
        com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater updater = this.mActivityTaskManagerInternal.createPackageConfigurationUpdater(appPackageName, userId);
        boolean isConfigChanged = updater.setLocales(locales).commit();
        if (isConfigChanged) {
            notifyAppWhoseLocaleChanged(appPackageName, userId, locales);
            notifyInstallerOfAppWhoseLocaleChanged(appPackageName, userId, locales);
            notifyRegisteredReceivers(appPackageName, userId, locales);
            this.mBackupHelper.notifyBackupManager();
            atomRecordForMetrics.setStatus(1);
            return;
        }
        atomRecordForMetrics.setStatus(2);
    }

    private void notifyRegisteredReceivers(java.lang.String appPackageName, int userId, android.os.LocaleList locales) {
        android.content.Intent intent = createBaseIntent("android.intent.action.APPLICATION_LOCALE_CHANGED", appPackageName, locales);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(userId), "android.permission.READ_APP_SPECIFIC_LOCALES");
    }

    void notifyInstallerOfAppWhoseLocaleChanged(java.lang.String appPackageName, int userId, android.os.LocaleList locales) {
        java.lang.String installingPackageName = getInstallingPackageName(appPackageName, userId);
        if (installingPackageName != null) {
            android.content.Intent intent = createBaseIntent("android.intent.action.APPLICATION_LOCALE_CHANGED", appPackageName, locales);
            intent.setPackage(installingPackageName);
            this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(userId));
        }
    }

    private void notifyAppWhoseLocaleChanged(java.lang.String appPackageName, int userId, android.os.LocaleList locales) {
        android.content.Intent intent = createBaseIntent("android.intent.action.LOCALE_CHANGED", appPackageName, locales);
        intent.setPackage(appPackageName);
        intent.addFlags(2097152);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(userId));
    }

    static android.content.Intent createBaseIntent(java.lang.String intentAction, java.lang.String appPackageName, android.os.LocaleList locales) {
        return new android.content.Intent(intentAction).putExtra("android.intent.extra.PACKAGE_NAME", appPackageName).putExtra("android.intent.extra.LOCALE_LIST", locales).addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB);
    }

    private boolean isPackageOwnedByCaller(java.lang.String appPackageName, int userId, com.android.server.locales.AppLocaleChangedAtomRecord atomRecordForMetrics, com.android.server.locales.AppSupportedLocalesChangedAtomRecord appSupportedLocalesChangedAtomRecord) {
        int uid = getPackageUid(appPackageName, userId);
        if (uid < 0) {
            android.util.Slog.w(TAG, "Unknown package " + appPackageName + " for user " + userId);
            if (atomRecordForMetrics != null) {
                atomRecordForMetrics.setStatus(3);
            } else if (appSupportedLocalesChangedAtomRecord != null) {
                appSupportedLocalesChangedAtomRecord.setStatus(3);
            }
            throw new java.lang.IllegalArgumentException("Unknown package: " + appPackageName + " for user " + userId);
        }
        if (atomRecordForMetrics != null) {
            atomRecordForMetrics.setTargetUid(uid);
        } else if (appSupportedLocalesChangedAtomRecord != null) {
            appSupportedLocalesChangedAtomRecord.setTargetUid(uid);
        }
        return android.os.UserHandle.isSameApp(android.os.Binder.getCallingUid(), uid);
    }

    private void enforceChangeConfigurationPermission(com.android.server.locales.AppLocaleChangedAtomRecord atomRecordForMetrics) {
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.CHANGE_CONFIGURATION", "setApplicationLocales");
        } catch (java.lang.SecurityException e) {
            atomRecordForMetrics.setStatus(4);
            throw e;
        }
    }

    public android.os.LocaleList getApplicationLocales(java.lang.String appPackageName, int userId) throws android.os.RemoteException, java.lang.IllegalArgumentException {
        java.util.Objects.requireNonNull(appPackageName);
        int userId2 = this.mActivityManagerInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 0, "getApplicationLocales", (java.lang.String) null);
        if (!isPackageOwnedByCaller(appPackageName, userId2, null, null) && !isCallerInstaller(appPackageName, userId2) && (!isCallerFromCurrentInputMethod(userId2) || !this.mActivityManagerInternal.isAppForeground(getPackageUid(appPackageName, userId2)))) {
            enforceReadAppSpecificLocalesPermission();
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return getApplicationLocalesUnchecked(appPackageName, userId2);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private android.os.LocaleList getApplicationLocalesUnchecked(java.lang.String appPackageName, int userId) {
        com.android.server.wm.ActivityTaskManagerInternal.PackageConfig appConfig = this.mActivityTaskManagerInternal.getApplicationConfig(appPackageName, userId);
        if (appConfig == null) {
            return android.os.LocaleList.getEmptyLocaleList();
        }
        android.os.LocaleList locales = appConfig.mLocales;
        return locales != null ? locales : android.os.LocaleList.getEmptyLocaleList();
    }

    private boolean isCallerInstaller(java.lang.String appPackageName, int userId) {
        int installerUid;
        java.lang.String installingPackageName = getInstallingPackageName(appPackageName, userId);
        return installingPackageName != null && (installerUid = getPackageUid(installingPackageName, userId)) >= 0 && android.os.UserHandle.isSameApp(android.os.Binder.getCallingUid(), installerUid);
    }

    private boolean isCallerFromCurrentInputMethod(int userId) {
        if (!android.os.SystemProperties.getBoolean(PROP_ALLOW_IME_QUERY_APP_LOCALE, true)) {
            return false;
        }
        java.lang.String currentInputMethod = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "default_input_method", userId);
        if (android.text.TextUtils.isEmpty(currentInputMethod)) {
            return false;
        }
        android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(currentInputMethod);
        if (componentName == null) {
            android.util.Slog.d(TAG, "inValid input method");
            return false;
        }
        java.lang.String inputMethodPkgName = componentName.getPackageName();
        int inputMethodUid = getPackageUid(inputMethodPkgName, userId);
        return inputMethodUid >= 0 && android.os.UserHandle.isSameApp(android.os.Binder.getCallingUid(), inputMethodUid);
    }

    private void enforceReadAppSpecificLocalesPermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_APP_SPECIFIC_LOCALES", "getApplicationLocales");
    }

    private int getPackageUid(java.lang.String appPackageName, int userId) {
        try {
            return this.mPackageManager.getPackageUidAsUser(appPackageName, android.content.pm.PackageManager.PackageInfoFlags.of(0L), userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    java.lang.String getInstallingPackageName(java.lang.String packageName, int userId) {
        try {
            return this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0).getPackageManager().getInstallSourceInfo(packageName).getInstallingPackageName();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, "Package not found " + packageName);
            return null;
        }
    }

    public android.os.LocaleList getSystemLocales() throws android.os.RemoteException {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return getSystemLocalesUnchecked();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private android.os.LocaleList getSystemLocalesUnchecked() throws android.os.RemoteException {
        android.os.LocaleList systemLocales = null;
        android.content.res.Configuration conf = android.app.ActivityManager.getService().getConfiguration();
        if (conf != null) {
            systemLocales = conf.getLocales();
        }
        if (systemLocales == null) {
            android.os.LocaleList systemLocales2 = android.os.LocaleList.getEmptyLocaleList();
            return systemLocales2;
        }
        return systemLocales;
    }

    private void logAppLocalesMetric(com.android.server.locales.AppLocaleChangedAtomRecord atomRecordForMetrics) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APPLICATION_LOCALES_CHANGED, atomRecordForMetrics.mCallingUid, atomRecordForMetrics.mTargetUid, atomRecordForMetrics.mNewLocales, atomRecordForMetrics.mPrevLocales, atomRecordForMetrics.mStatus, atomRecordForMetrics.mCaller);
    }

    public void setOverrideLocaleConfig(java.lang.String appPackageName, int userId, android.app.LocaleConfig localeConfig) throws java.lang.IllegalArgumentException {
        if (!android.os.SystemProperties.getBoolean(PROP_DYNAMIC_LOCALES_CHANGE, true)) {
            return;
        }
        com.android.server.locales.AppSupportedLocalesChangedAtomRecord atomRecord = new com.android.server.locales.AppSupportedLocalesChangedAtomRecord(android.os.Binder.getCallingUid());
        try {
            java.util.Objects.requireNonNull(appPackageName);
            int userId2 = this.mActivityManagerInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 0, "setOverrideLocaleConfig", (java.lang.String) null);
            if (!isPackageOwnedByCaller(appPackageName, userId2, null, atomRecord)) {
                enforceSetAppSpecificLocaleConfigPermission(atomRecord);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                setOverrideLocaleConfigUnchecked(appPackageName, userId2, localeConfig, atomRecord);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        } finally {
            logAppSupportedLocalesChangedMetric(atomRecord);
        }
    }

    private void setOverrideLocaleConfigUnchecked(java.lang.String appPackageName, int userId, android.app.LocaleConfig overrideLocaleConfig, com.android.server.locales.AppSupportedLocalesChangedAtomRecord atomRecord) {
        synchronized (this.mWriteLock) {
            try {
                try {
                    android.app.LocaleConfig resLocaleConfig = android.app.LocaleConfig.fromContextIgnoringOverride(this.mContext.createPackageContext(appPackageName, 0));
                    java.io.File file = getXmlFileNameForUser(appPackageName, userId);
                    if (overrideLocaleConfig == null) {
                        if (file.exists()) {
                            android.util.Slog.d(TAG, "remove the override LocaleConfig");
                            file.delete();
                        }
                        removeUnsupportedAppLocales(appPackageName, userId, resLocaleConfig, 5);
                        atomRecord.setOverrideRemoved(true);
                        atomRecord.setStatus(1);
                        return;
                    }
                    if (overrideLocaleConfig.isSameLocaleConfig(getOverrideLocaleConfig(appPackageName, userId))) {
                        android.util.Slog.d(TAG, "the same override, ignore it");
                        atomRecord.setSameAsPrevConfig(true);
                        return;
                    }
                    android.os.LocaleList localeList = overrideLocaleConfig.getSupportedLocales();
                    if (localeList == null) {
                        localeList = android.os.LocaleList.getEmptyLocaleList();
                    }
                    atomRecord.setNumLocales(localeList.size());
                    android.util.AtomicFile atomicFile = new android.util.AtomicFile(file);
                    java.io.FileOutputStream stream = null;
                    try {
                        stream = atomicFile.startWrite();
                        stream.write(toXmlByteArray(localeList));
                        atomicFile.finishWrite(stream);
                        removeUnsupportedAppLocales(appPackageName, userId, overrideLocaleConfig, 5);
                        if (overrideLocaleConfig.isSameLocaleConfig(resLocaleConfig)) {
                            android.util.Slog.d(TAG, "setOverrideLocaleConfig, same as the app's LocaleConfig");
                            atomRecord.setSameAsResConfig(true);
                        }
                        atomRecord.setStatus(1);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(TAG, "Failed to write file " + atomicFile, e);
                        if (stream != null) {
                            atomicFile.failWrite(stream);
                        }
                        atomRecord.setStatus(2);
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                    android.util.Slog.e(TAG, "Unknown package name " + appPackageName);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    void removeUnsupportedAppLocales(java.lang.String appPackageName, int userId, android.app.LocaleConfig localeConfig, int caller) {
        android.os.LocaleList appLocales = getApplicationLocalesUnchecked(appPackageName, userId);
        boolean resetAppLocales = false;
        java.util.List<java.util.Locale> newAppLocales = new java.util.ArrayList<>();
        if (localeConfig == null) {
            android.util.Slog.i(TAG, "There is no LocaleConfig, reset app locales");
            resetAppLocales = true;
        } else {
            for (int i = 0; i < appLocales.size(); i++) {
                if (!localeConfig.containsLocale(appLocales.get(i))) {
                    android.util.Slog.i(TAG, "Missing from the LocaleConfig, reset app locales");
                    resetAppLocales = true;
                } else {
                    newAppLocales.add(appLocales.get(i));
                }
            }
        }
        if (resetAppLocales) {
            java.util.Locale[] locales = new java.util.Locale[newAppLocales.size()];
            try {
                setApplicationLocales(appPackageName, userId, new android.os.LocaleList((java.util.Locale[]) newAppLocales.toArray(locales)), this.mBackupHelper.areLocalesSetFromDelegate(userId, appPackageName), caller);
            } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "Could not set locales for " + appPackageName, e);
            }
        }
    }

    private void enforceSetAppSpecificLocaleConfigPermission(com.android.server.locales.AppSupportedLocalesChangedAtomRecord atomRecord) {
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.SET_APP_SPECIFIC_LOCALECONFIG", "setOverrideLocaleConfig");
        } catch (java.lang.SecurityException e) {
            atomRecord.setStatus(4);
            throw e;
        }
    }

    public android.app.LocaleConfig getOverrideLocaleConfig(java.lang.String appPackageName, int userId) {
        if (!android.os.SystemProperties.getBoolean(PROP_DYNAMIC_LOCALES_CHANGE, true)) {
            return null;
        }
        java.util.Objects.requireNonNull(appPackageName);
        java.io.File file = getXmlFileNameForUser(appPackageName, this.mActivityManagerInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 0, "getOverrideLocaleConfig", (java.lang.String) null));
        if (!file.exists()) {
            return null;
        }
        try {
            java.io.InputStream in = new java.io.FileInputStream(file);
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                java.util.List<java.lang.String> overrideLocales = loadFromXml(parser);
                android.app.LocaleConfig storedLocaleConfig = new android.app.LocaleConfig(android.os.LocaleList.forLanguageTags(java.lang.String.join(",", overrideLocales)));
                in.close();
                return storedLocaleConfig;
            } catch (java.lang.Throwable th) {
                try {
                    in.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Failed to parse XML configuration from " + file, e);
            return null;
        }
    }

    void deleteOverrideLocaleConfig(java.lang.String appPackageName, int userId) {
        java.io.File file = getXmlFileNameForUser(appPackageName, userId);
        if (file.exists()) {
            android.util.Slog.d(TAG, "Delete the override LocaleConfig.");
            file.delete();
        }
    }

    private byte[] toXmlByteArray(android.os.LocaleList localeList) {
        try {
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            try {
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.newFastSerializer();
                out.setOutput(os, java.nio.charset.StandardCharsets.UTF_8.name());
                out.startDocument((java.lang.String) null, true);
                out.startTag((java.lang.String) null, "locale-config");
                java.util.List<java.lang.String> locales = new java.util.ArrayList<>(java.util.Arrays.asList(localeList.toLanguageTags().split(",")));
                for (java.lang.String locale : locales) {
                    out.startTag((java.lang.String) null, com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE);
                    out.attribute((java.lang.String) null, "name", locale);
                    out.endTag((java.lang.String) null, com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE);
                }
                out.endTag((java.lang.String) null, "locale-config");
                out.endDocument();
                byte[] byteArray = os.toByteArray();
                os.close();
                return byteArray;
            } finally {
            }
        } catch (java.io.IOException e) {
            return null;
        }
    }

    private java.util.List<java.lang.String> loadFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.List<java.lang.String> localeList = new java.util.ArrayList<>();
        com.android.internal.util.XmlUtils.beginDocument(parser, "locale-config");
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            java.lang.String tagName = parser.getName();
            if (com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE.equals(tagName)) {
                java.lang.String locale = parser.getAttributeValue((java.lang.String) null, "name");
                localeList.add(locale);
            } else {
                android.util.Slog.w(TAG, "Unexpected tag name: " + tagName);
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        }
        return localeList;
    }

    private java.io.File getXmlFileNameForUser(java.lang.String appPackageName, int userId) {
        java.io.File dir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), LOCALE_CONFIGS);
        return new java.io.File(dir, appPackageName + SUFFIX_FILE_NAME);
    }

    private void logAppSupportedLocalesChangedMetric(com.android.server.locales.AppSupportedLocalesChangedAtomRecord atomRecord) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_SUPPORTED_LOCALES_CHANGED, atomRecord.mCallingUid, atomRecord.mTargetUid, atomRecord.mNumLocales, atomRecord.mOverrideRemoved, atomRecord.mSameAsResConfig, atomRecord.mSameAsPrevConfig, atomRecord.mStatus);
    }
}
