package com.android.server.appwidget;

/* JADX INFO: loaded from: classes.dex */
class AppWidgetServiceImpl extends com.android.internal.appwidget.IAppWidgetService.Stub implements com.android.server.WidgetBackupProvider, android.app.admin.DevicePolicyManagerInternal.OnCrossProfileWidgetProvidersChangeListener {
    private static final int CURRENT_VERSION = 1;
    private static final boolean DEBUG;
    private static final int DEFAULT_GENERATED_PREVIEW_MAX_CALLS_PER_INTERVAL = 2;
    private static final long DEFAULT_GENERATED_PREVIEW_RESET_INTERVAL_MS;
    private static final int ID_PROVIDER_CHANGED = 1;
    private static final int ID_VIEWS_UPDATE = 0;
    private static final int KEYGUARD_HOST_ID = 1262836039;
    private static final java.lang.String KEY_SIZES = "sizes";
    private static final int MAX_NUMBER_OF_HOSTS_PER_PACKAGE = 20;
    private static final int MAX_NUMBER_OF_WIDGETS_PER_HOST = 200;
    private static final int MIN_UPDATE_PERIOD = 1800000;
    private static final java.lang.String NEW_KEYGUARD_HOST_PACKAGE = "com.android.keyguard";
    private static final java.lang.String OLD_KEYGUARD_HOST_PACKAGE = "android";
    private static final java.lang.String PENDING_DELETED_IDS_ATTR = "pending_deleted_ids";
    private static final java.lang.String STATE_FILENAME = "appwidgets.xml";
    private static final java.lang.String TAG = "AppWidgetServiceImpl";
    private static final int TAG_UNDEFINED = -1;
    private static final int UNKNOWN_UID = -1;
    private static final int UNKNOWN_USER_ID = -10;
    private static final java.util.concurrent.atomic.AtomicLong UPDATE_COUNTER;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private android.app.AlarmManager mAlarmManager;
    private android.app.AppOpsManager mAppOpsManager;
    private android.app.AppOpsManagerInternal mAppOpsManagerInternal;
    private com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController mBackupRestoreController;
    private android.os.Handler mCallbackHandler;
    private final android.content.Context mContext;
    private android.app.admin.DevicePolicyManagerInternal mDevicePolicyManagerInternal;
    private com.android.server.appwidget.AppWidgetServiceImpl.ApiCounter mGeneratedPreviewsApiCounter;
    private android.os.Bundle mInteractiveBroadcast;
    private boolean mIsCombinedBroadcastEnabled;
    private android.app.KeyguardManager mKeyguardManager;
    private int mMaxWidgetBitmapMemory;
    private android.content.pm.IPackageManager mPackageManager;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private boolean mSafeMode;
    private android.os.Handler mSaveStateHandler;
    private com.android.server.appwidget.AppWidgetServiceImpl.SecurityPolicy mSecurityPolicy;
    private android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal;
    private android.os.UserManager mUserManager;
    private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.appwidget.AppWidgetServiceImpl.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:26:0x007a  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r6, android.content.Intent r7) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 248
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.appwidget.AppWidgetServiceImpl.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    private final java.util.HashMap<android.util.Pair<java.lang.Integer, android.content.Intent.FilterComparison>, java.util.HashSet<java.lang.Integer>> mRemoteViewsServicesAppWidgets = new java.util.HashMap<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Widget> mWidgets = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Host> mHosts = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Provider> mProviders = new java.util.ArrayList<>();
    private final android.util.ArraySet<android.util.Pair<java.lang.Integer, java.lang.String>> mPackagesWithBindWidgetPermission = new android.util.ArraySet<>();
    private final android.util.SparseBooleanArray mLoadedUserIds = new android.util.SparseBooleanArray();
    private final java.lang.Object mWidgetPackagesLock = new java.lang.Object();
    private final android.util.SparseArray<android.util.ArraySet<java.lang.String>> mWidgetPackages = new android.util.SparseArray<>();
    private final android.util.SparseIntArray mNextAppWidgetIds = new android.util.SparseIntArray();
    private com.android.server.appwidget.IAppWidgetServiceImplExt mAppWidgetServiceExt = (com.android.server.appwidget.IAppWidgetServiceImplExt) system.ext.loader.core.ExtLoader.type(com.android.server.appwidget.IAppWidgetServiceImplExt.class).base(this).create();

    /* JADX WARN: Removed duplicated region for block: B:6:0x000e  */
    static {
        /*
            boolean r0 = android.os.Build.IS_DEBUGGABLE
            if (r0 != 0) goto Le
            java.lang.String r0 = "persist.sys.assert.panic"
            r1 = 0
            boolean r0 = android.os.SystemProperties.getBoolean(r0, r1)
            if (r0 == 0) goto Lf
        Le:
            r1 = 1
        Lf:
            com.android.server.appwidget.AppWidgetServiceImpl.DEBUG = r1
            java.util.concurrent.atomic.AtomicLong r0 = new java.util.concurrent.atomic.AtomicLong
            r0.<init>()
            com.android.server.appwidget.AppWidgetServiceImpl.UPDATE_COUNTER = r0
            r0 = 1
            java.time.Duration r0 = java.time.Duration.ofHours(r0)
            long r0 = r0.toMillis()
            com.android.server.appwidget.AppWidgetServiceImpl.DEFAULT_GENERATED_PREVIEW_RESET_INTERVAL_MS = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appwidget.AppWidgetServiceImpl.<clinit>():void");
    }

    AppWidgetServiceImpl(android.content.Context context) {
        this.mContext = context;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onStart() {
        this.mPackageManager = android.app.AppGlobals.getPackageManager();
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService("user");
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService("appops");
        this.mKeyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService("keyguard");
        this.mDevicePolicyManagerInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        if (android.appwidget.flags.Flags.removeAppWidgetServiceIoFromCriticalPath()) {
            this.mSaveStateHandler = new android.os.Handler(com.android.internal.os.BackgroundThread.get().getLooper(), new android.os.Handler.Callback() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda0
                @Override // android.os.Handler.Callback
                public final boolean handleMessage(android.os.Message message) {
                    return this.f$0.handleSaveMessage(message);
                }
            });
        } else {
            this.mSaveStateHandler = com.android.internal.os.BackgroundThread.getHandler();
        }
        com.android.server.ServiceThread serviceThread = new com.android.server.ServiceThread(TAG, -2, false);
        serviceThread.start();
        this.mCallbackHandler = new com.android.server.appwidget.AppWidgetServiceImpl.CallbackHandler(serviceThread.getLooper());
        this.mBackupRestoreController = new com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController();
        this.mSecurityPolicy = new com.android.server.appwidget.AppWidgetServiceImpl.SecurityPolicy();
        this.mIsCombinedBroadcastEnabled = android.provider.DeviceConfig.getBoolean("systemui", "combined_broadcast_enabled", true);
        this.mGeneratedPreviewsApiCounter = new com.android.server.appwidget.AppWidgetServiceImpl.ApiCounter(android.provider.DeviceConfig.getLong("systemui", "generated_preview_api_reset_interval_ms", DEFAULT_GENERATED_PREVIEW_RESET_INTERVAL_MS), android.provider.DeviceConfig.getInt("systemui", "generated_preview_api_reset_interval_ms", 2));
        android.provider.DeviceConfig.addOnPropertiesChangedListener("systemui", new android.os.HandlerExecutor(this.mCallbackHandler), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.handleSystemUiDeviceConfigChange(properties);
            }
        });
        android.app.BroadcastOptions broadcastOptionsMakeBasic = android.app.BroadcastOptions.makeBasic();
        broadcastOptionsMakeBasic.setBackgroundActivityStartsAllowed(false);
        broadcastOptionsMakeBasic.setInteractive(true);
        this.mInteractiveBroadcast = broadcastOptionsMakeBasic.toBundle();
        computeMaximumWidgetBitmapMemory();
        registerBroadcastReceiver();
        registerOnCrossProfileProvidersChangedListener();
        com.android.server.LocalServices.addService(android.appwidget.AppWidgetManagerInternal.class, new com.android.server.appwidget.AppWidgetServiceImpl.AppWidgetManagerLocal());
    }

    void systemServicesReady() {
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mAppOpsManagerInternal = (android.app.AppOpsManagerInternal) com.android.server.LocalServices.getService(android.app.AppOpsManagerInternal.class);
        this.mUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
    }

    private void computeMaximumWidgetBitmapMemory() {
        android.view.Display display = this.mContext.getDisplayNoVerify();
        android.graphics.Point size = new android.graphics.Point();
        display.getRealSize(size);
        this.mMaxWidgetBitmapMemory = size.x * 6 * size.y;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleSaveMessage(android.os.Message msg) {
        android.util.SparseArray<byte[]> userIdToBytesMapping;
        if (this.mSafeMode) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "do not handleSaveMessage in safemode ");
            }
            return true;
        }
        int userId = msg.what;
        synchronized (this.mLock) {
            android.os.Trace.traceBegin(64L, "convert_state_to_bytes");
            ensureGroupStateLoadedLocked(userId, false);
            userIdToBytesMapping = saveStateToByteArrayLocked(userId);
            android.os.Trace.traceEnd(64L);
        }
        android.os.Trace.traceBegin(64L, "byte_to_disk_io");
        for (int i = 0; i < userIdToBytesMapping.size(); i++) {
            int currentProfileId = userIdToBytesMapping.keyAt(i);
            byte[] currentStateByteArray = userIdToBytesMapping.valueAt(i);
            android.util.AtomicFile currentFile = getSavedStateFile(currentProfileId);
            try {
                java.io.FileOutputStream fileStream = currentFile.startWrite();
                try {
                    fileStream.write(currentStateByteArray);
                    currentFile.finishWrite(fileStream);
                } catch (java.io.IOException e) {
                    android.util.Log.e(TAG, "Failed to write state byte stream to file", e);
                    currentFile.failWrite(fileStream);
                }
            } catch (java.io.IOException e2) {
                android.util.Log.e(TAG, "Failed to start writing stream", e2);
            }
        }
        android.os.Trace.traceEnd(64L);
        return true;
    }

    private void registerBroadcastReceiver() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "registerBroadcastReceiver");
        }
        android.content.IntentFilter packageFilter = new android.content.IntentFilter();
        packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        packageFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        packageFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        packageFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        packageFilter.addAction("android.intent.action.PACKAGE_UNSTOPPED");
        packageFilter.addDataScheme("package");
        packageFilter.setPriority(1000);
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, packageFilter, null, this.mCallbackHandler);
        android.content.IntentFilter sdFilter = new android.content.IntentFilter();
        sdFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
        sdFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, sdFilter, null, this.mCallbackHandler);
        android.content.IntentFilter offModeFilter = new android.content.IntentFilter();
        offModeFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        offModeFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, offModeFilter, null, this.mCallbackHandler);
        android.content.IntentFilter suspendPackageFilter = new android.content.IntentFilter();
        suspendPackageFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
        suspendPackageFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, suspendPackageFilter, null, this.mCallbackHandler);
    }

    private void registerOnCrossProfileProvidersChangedListener() {
        if (this.mDevicePolicyManagerInternal != null) {
            this.mDevicePolicyManagerInternal.addOnCrossProfileWidgetProvidersChangeListener(this);
        }
    }

    public void setSafeMode(boolean safeMode) {
        this.mSafeMode = safeMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x018e A[Catch: all -> 0x01a8, TryCatch #2 {all -> 0x01a8, blocks: (B:107:0x019f, B:88:0x0165, B:92:0x016f, B:94:0x0173, B:97:0x017f, B:99:0x0185, B:100:0x0188, B:103:0x018e, B:104:0x0199, B:78:0x0114, B:82:0x0143, B:111:0x01a6), top: B:118:0x009f }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x009f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0099 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onPackageBroadcastReceived(android.content.Intent r20, int r21) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 462
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appwidget.AppWidgetServiceImpl.onPackageBroadcastReceived(android.content.Intent, int):void");
    }

    private boolean clearPreviewsForUidLocked(int clearedUid) {
        boolean changed = false;
        int providerCount = this.mProviders.size();
        for (int i = 0; i < providerCount; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            if (provider.id.uid == clearedUid) {
                changed |= provider.clearGeneratedPreviewsLocked();
            }
        }
        return changed;
    }

    void reloadWidgetsMaskedStateForGroup(int userId) {
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            return;
        }
        synchronized (this.mLock) {
            reloadWidgetsMaskedState(userId);
            int[] profileIds = this.mUserManager.getEnabledProfileIds(userId);
            for (int profileId : profileIds) {
                reloadWidgetsMaskedState(profileId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadWidgetsMaskedState(int userId) {
        boolean suspended;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo user = this.mUserManager.getUserInfo(userId);
            boolean lockedProfile = !this.mUserManager.isUserUnlockingOrUnlocked(userId);
            boolean quietProfile = user.isQuietModeEnabled();
            int N = this.mProviders.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
                int providerUserId = provider.getUserId();
                if (providerUserId == userId) {
                    boolean changed = provider.setMaskedByLockedProfileLocked(lockedProfile) | provider.setMaskedByQuietProfileLocked(quietProfile);
                    try {
                        try {
                            suspended = this.mPackageManager.isPackageSuspendedForUser(provider.id.componentName.getPackageName(), provider.getUserId());
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(TAG, "Failed to query application info", e);
                        }
                    } catch (java.lang.IllegalArgumentException e2) {
                        suspended = false;
                    }
                    changed |= provider.setMaskedBySuspendedPackageLocked(suspended);
                    if (changed) {
                        if (provider.isMaskedLocked()) {
                            maskWidgetsViewsLocked(provider, null);
                        } else {
                            unmaskWidgetsViewsLocked(provider);
                        }
                    }
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWidgetPackageSuspensionMaskedState(android.content.Intent intent, boolean suspended, int profileId) {
        java.lang.String[] packagesArray = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
        if (packagesArray == null) {
            return;
        }
        java.util.Set<java.lang.String> packages = new android.util.ArraySet<>(java.util.Arrays.asList(packagesArray));
        synchronized (this.mLock) {
            int N = this.mProviders.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
                int providerUserId = provider.getUserId();
                if (providerUserId == profileId && packages.contains(provider.id.componentName.getPackageName()) && provider.setMaskedBySuspendedPackageLocked(suspended)) {
                    if (provider.isMaskedLocked()) {
                        maskWidgetsViewsLocked(provider, null);
                    } else {
                        unmaskWidgetsViewsLocked(provider);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWidgetPackageStoppedMaskedState(android.content.Intent intent) {
        java.lang.String packageName;
        int providerUid = intent.getIntExtra("android.intent.extra.UID", -1);
        android.net.Uri uri = intent.getData();
        if (providerUid == -1 || uri == null || (packageName = uri.getSchemeSpecificPart()) == null) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "Updating package stopped masked state for uid " + providerUid + " package " + packageName + " isStopped false");
        }
        synchronized (this.mLock) {
            int count = this.mProviders.size();
            for (int i = 0; i < count; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
                if (providerUid == provider.id.uid && packageName.equals(provider.id.componentName.getPackageName()) && provider.setMaskedByStoppedPackageLocked(false)) {
                    if (provider.isMaskedLocked()) {
                        cancelBroadcastsLocked(provider);
                    } else {
                        unmaskWidgetsViewsLocked(provider);
                        int widgetCount = provider.widgets.size();
                        if (widgetCount > 0) {
                            int[] widgetIds = new int[widgetCount];
                            for (int j = 0; j < widgetCount; j++) {
                                widgetIds[j] = provider.widgets.get(j).appWidgetId;
                            }
                            registerForBroadcastsLocked(provider, widgetIds);
                            sendUpdateIntentLocked(provider, widgetIds, false);
                        }
                        int pendingIdsCount = provider.pendingDeletedWidgetIds.size();
                        if (pendingIdsCount > 0) {
                            if (DEBUG) {
                                android.util.Slog.i(TAG, "Sending missed deleted broadcasts for " + provider.id.componentName + " " + provider.pendingDeletedWidgetIds);
                            }
                            for (int j2 = 0; j2 < pendingIdsCount; j2++) {
                                sendDeletedIntentLocked(provider.id.componentName, provider.id.getProfile(), provider.pendingDeletedWidgetIds.get(j2));
                            }
                            provider.pendingDeletedWidgetIds.clear();
                            if (widgetCount == 0) {
                                sendDisabledIntentLocked(provider);
                            }
                            saveGroupStateAsync(provider.id.getProfile().getIdentifier());
                        }
                    }
                }
            }
        }
    }

    private void maskWidgetsViewsLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, com.android.server.appwidget.AppWidgetServiceImpl.Widget targetWidget) throws java.lang.Throwable {
        android.content.Intent onClickIntent;
        android.content.Intent onClickIntent2;
        android.graphics.drawable.Icon icon;
        com.android.server.appwidget.AppWidgetServiceImpl.Provider provider2 = provider;
        com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = targetWidget;
        int widgetCount = provider2.widgets.size();
        if (widgetCount == 0) {
            return;
        }
        android.widget.RemoteViews views = new android.widget.RemoteViews(this.mContext.getPackageName(), android.R.layout.tab_indicator_material);
        android.content.pm.ActivityInfo activityInfo = provider2.info.providerInfo;
        android.content.pm.ApplicationInfo appInfo = activityInfo != null ? activityInfo.applicationInfo : null;
        java.lang.String packageName = appInfo != null ? appInfo.packageName : provider2.id.componentName.getPackageName();
        int appUserId = provider.getUserId();
        boolean showBadge = false;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (provider2.maskedByQuietProfile) {
                showBadge = true;
                onClickIntent = com.android.internal.app.UnlaunchableAppActivity.createInQuietModeDialogIntent(appUserId);
            } else if (provider2.maskedBySuspendedPackage) {
                boolean showBadge2 = this.mUserManager.hasBadge(appUserId);
                try {
                    android.content.pm.UserPackage suspendingPackage = this.mPackageManagerInternal.getSuspendingPackage(packageName, appUserId);
                    if (suspendingPackage != null && "android".equals(suspendingPackage.packageName)) {
                        onClickIntent2 = this.mDevicePolicyManagerInternal.createShowAdminSupportIntent(appUserId, true);
                    } else {
                        android.content.pm.SuspendDialogInfo dialogInfo = this.mPackageManagerInternal.getSuspendedDialogInfo(packageName, suspendingPackage, appUserId);
                        onClickIntent2 = com.android.internal.app.SuspendedAppActivity.createSuspendedAppInterceptIntent(packageName, suspendingPackage, dialogInfo, (android.os.Bundle) null, (android.content.IntentSender) null, appUserId);
                    }
                    onClickIntent = onClickIntent2;
                    showBadge = showBadge2;
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            } else if (provider2.maskedByLockedProfile) {
                showBadge = true;
                onClickIntent = this.mKeyguardManager.createConfirmDeviceCredentialIntent(null, null, appUserId);
                if (onClickIntent != null) {
                    onClickIntent.setFlags(276824064);
                }
            } else if (!provider2.maskedByStoppedPackage) {
                onClickIntent = null;
            } else {
                showBadge = this.mUserManager.hasBadge(appUserId);
                onClickIntent = null;
            }
            if (appInfo != null && appInfo.icon != 0) {
                icon = android.graphics.drawable.Icon.createWithResource(appInfo.packageName, appInfo.icon);
            } else {
                icon = android.graphics.drawable.Icon.createWithResource(this.mContext, android.R.drawable.sym_def_app_icon);
            }
            views.setImageViewIcon(android.R.id.typeTouchInteractionStart, icon);
            if (!showBadge) {
                views.setViewVisibility(android.R.id.typeViewAccessibilityFocusCleared, 4);
            }
            int j = 0;
            while (j < widgetCount) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget2 = provider2.widgets.get(j);
                if (widget == null || widget == widget2) {
                    if (provider2.maskedByStoppedPackage) {
                        android.content.Intent intent = createUpdateIntentLocked(provider2, new int[]{widget2.appWidgetId});
                        views.setOnClickPendingIntent(android.R.id.background, android.app.PendingIntent.getBroadcast(this.mContext, widget2.appWidgetId, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD));
                    } else if (onClickIntent != null) {
                        views.setOnClickPendingIntent(android.R.id.background, android.app.PendingIntent.getActivity(this.mContext, widget2.appWidgetId, onClickIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD));
                    }
                    if (widget2.replaceWithMaskedViewsLocked(views)) {
                        scheduleNotifyUpdateAppWidgetLocked(widget2, widget2.getEffectiveViewsLocked());
                    }
                }
                j++;
                provider2 = provider;
                widget = targetWidget;
            }
            android.os.Binder.restoreCallingIdentity(identity);
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    private void unmaskWidgetsViewsLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider) {
        int widgetCount = provider.widgets.size();
        for (int j = 0; j < widgetCount; j++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = provider.widgets.get(j);
            if (widget.clearMaskedViewsLocked()) {
                scheduleNotifyUpdateAppWidgetLocked(widget, widget.getEffectiveViewsLocked());
            }
        }
    }

    private void resolveHostUidLocked(java.lang.String pkg, int uid) {
        int N = this.mHosts.size();
        for (int i = 0; i < N; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i);
            if (host.id.uid == -1 && pkg.equals(host.id.packageName)) {
                if (DEBUG) {
                    android.util.Slog.i(TAG, "host " + host.id + " resolved to uid " + uid);
                }
                host.id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(uid, host.id.hostId, host.id.packageName);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureGroupStateLoadedLocked(int userId) throws java.lang.Throwable {
        ensureGroupStateLoadedLocked(userId, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureGroupStateLoadedLocked(int userId, boolean enforceUserUnlockingOrUnlocked) throws java.lang.Throwable {
        if (enforceUserUnlockingOrUnlocked && !isUserRunningAndUnlocked(userId)) {
            throw new java.lang.IllegalStateException("User " + userId + " must be unlocked for widgets to be available");
        }
        if (enforceUserUnlockingOrUnlocked && isProfileWithLockedParent(userId)) {
            throw new java.lang.IllegalStateException("Profile " + userId + " must have unlocked parent");
        }
        int[] profileIds = this.mSecurityPolicy.getEnabledGroupProfileIds(userId);
        android.util.IntArray newIds = new android.util.IntArray(1);
        for (int profileId : profileIds) {
            if (!this.mLoadedUserIds.get(profileId)) {
                this.mLoadedUserIds.put(profileId, true);
                newIds.add(profileId);
            }
        }
        if (newIds.size() <= 0) {
            return;
        }
        int[] newProfileIds = newIds.toArray();
        clearProvidersAndHostsTagsLocked();
        loadGroupWidgetProvidersLocked(newProfileIds);
        loadGroupStateLocked(newProfileIds);
    }

    private boolean isUserRunningAndUnlocked(int userId) {
        return this.mUserManager.isUserUnlockingOrUnlocked(userId);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            synchronized (this.mLock) {
                if (args.length > 0 && "--proto".equals(args[0])) {
                    dumpProto(fd);
                } else {
                    dumpInternalLocked(pw);
                }
            }
        }
    }

    private void dumpProto(java.io.FileDescriptor fd) {
        android.util.Slog.i(TAG, "dump proto for " + this.mWidgets.size() + " widgets");
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        int N = this.mWidgets.size();
        for (int i = 0; i < N; i++) {
            dumpProtoWidget(proto, this.mWidgets.get(i));
        }
        proto.flush();
    }

    private void dumpProtoWidget(android.util.proto.ProtoOutputStream proto, com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        if (widget.host == null || widget.provider == null) {
            android.util.Slog.d(TAG, "skip dumping widget because host or provider is null: widget.host=" + widget.host + " widget.provider=" + widget.provider);
            return;
        }
        long token = proto.start(2246267895809L);
        proto.write(1133871366145L, widget.host.getUserId() != widget.provider.getUserId());
        proto.write(1133871366146L, widget.host.callbacks == null);
        proto.write(1138166333443L, widget.host.id.packageName);
        proto.write(1138166333444L, widget.provider.id.componentName.getPackageName());
        proto.write(1138166333445L, widget.provider.id.componentName.getClassName());
        if (widget.options != null) {
            proto.write(1133871366154L, widget.options.getBoolean("appWidgetRestoreCompleted"));
            proto.write(1120986464262L, widget.options.getInt("appWidgetMinWidth", 0));
            proto.write(1120986464263L, widget.options.getInt("appWidgetMinHeight", 0));
            proto.write(1120986464264L, widget.options.getInt("appWidgetMaxWidth", 0));
            proto.write(1120986464265L, widget.options.getInt("appWidgetMaxHeight", 0));
        }
        proto.end(token);
    }

    private void dumpInternalLocked(java.io.PrintWriter pw) {
        int N = this.mProviders.size();
        pw.println("Providers:");
        for (int i = 0; i < N; i++) {
            dumpProviderLocked(this.mProviders.get(i), i, pw);
        }
        int N2 = this.mWidgets.size();
        pw.println(" ");
        pw.println("Widgets:");
        for (int i2 = 0; i2 < N2; i2++) {
            dumpWidget(this.mWidgets.get(i2), i2, pw);
        }
        int N3 = this.mHosts.size();
        pw.println(" ");
        pw.println("Hosts:");
        for (int i3 = 0; i3 < N3; i3++) {
            dumpHost(this.mHosts.get(i3), i3, pw);
        }
        int N4 = this.mPackagesWithBindWidgetPermission.size();
        pw.println(" ");
        pw.println("Grants:");
        for (int i4 = 0; i4 < N4; i4++) {
            android.util.Pair<java.lang.Integer, java.lang.String> grant = this.mPackagesWithBindWidgetPermission.valueAt(i4);
            dumpGrant(grant, i4, pw);
        }
    }

    public android.content.pm.ParceledListSlice<android.appwidget.PendingHostUpdate> startListening(com.android.internal.appwidget.IAppWidgetHost callbacks, java.lang.String callingPackage, int hostId, int[] appWidgetIds) throws java.lang.Throwable {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "startListening() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mSecurityPolicy.isInstantAppLocked(callingPackage, userId)) {
                        android.util.Slog.w(TAG, "Instant package " + callingPackage + " cannot host app widgets");
                        return android.content.pm.ParceledListSlice.emptyList();
                    }
                    ensureGroupStateLoadedLocked(userId);
                    try {
                        com.android.server.appwidget.AppWidgetServiceImpl.HostId id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(android.os.Binder.getCallingUid(), hostId, callingPackage);
                        com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupOrAddHostLocked(id);
                        host.callbacks = callbacks;
                        long updateSequenceNo = UPDATE_COUNTER.incrementAndGet();
                        int N = appWidgetIds.length;
                        java.util.ArrayList<android.appwidget.PendingHostUpdate> outUpdates = new java.util.ArrayList<>(N);
                        android.util.LongSparseArray<android.appwidget.PendingHostUpdate> updatesMap = new android.util.LongSparseArray<>();
                        int i = 0;
                        while (i < N) {
                            updatesMap.clear();
                            com.android.server.appwidget.AppWidgetServiceImpl.HostId id2 = id;
                            host.getPendingUpdatesForIdLocked(this.mContext, appWidgetIds[i], updatesMap);
                            int j = 0;
                            for (int m = updatesMap.size(); j < m; m = m) {
                                outUpdates.add(updatesMap.valueAt(j));
                                j++;
                            }
                            i++;
                            id = id2;
                        }
                        host.lastWidgetUpdateSequenceNo = updateSequenceNo;
                        return new android.content.pm.ParceledListSlice<>(outUpdates);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public void stopListening(java.lang.String callingPackage, int hostId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "stopListening() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId, false);
            com.android.server.appwidget.AppWidgetServiceImpl.HostId id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(android.os.Binder.getCallingUid(), hostId, callingPackage);
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupHostLocked(id);
            if (host != null) {
                host.callbacks = null;
                pruneHostLocked(host);
                this.mAppOpsManagerInternal.updateAppWidgetVisibility(host.getWidgetUidsIfBound(), false);
            }
        }
    }

    public int allocateAppWidgetId(java.lang.String callingPackage, int hostId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "allocateAppWidgetId() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.isInstantAppLocked(callingPackage, userId)) {
                android.util.Slog.w(TAG, "Instant package " + callingPackage + " cannot host app widgets");
                return 0;
            }
            ensureGroupStateLoadedLocked(userId);
            if (this.mNextAppWidgetIds.indexOfKey(userId) < 0) {
                this.mNextAppWidgetIds.put(userId, 1);
            }
            int appWidgetId = incrementAndGetAppWidgetIdLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.HostId id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(android.os.Binder.getCallingUid(), hostId, callingPackage);
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupOrAddHostLocked(id);
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = new com.android.server.appwidget.AppWidgetServiceImpl.Widget();
            widget.appWidgetId = appWidgetId;
            widget.host = host;
            host.widgets.add(widget);
            addWidgetLocked(widget);
            saveGroupStateAsync(userId);
            if (DEBUG) {
                android.util.Slog.i(TAG, "Allocated widget id " + appWidgetId + " for host " + host.id);
            }
            return appWidgetId;
        }
    }

    public void setAppWidgetHidden(java.lang.String callingPackage, int hostId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "setAppWidgetHidden() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId, false);
            com.android.server.appwidget.AppWidgetServiceImpl.HostId id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(android.os.Binder.getCallingUid(), hostId, callingPackage);
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupHostLocked(id);
            if (host != null) {
                this.mAppOpsManagerInternal.updateAppWidgetVisibility(host.getWidgetUidsIfBound(), false);
            }
        }
    }

    public void deleteAppWidgetId(java.lang.String callingPackage, int appWidgetId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "deleteAppWidgetId() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
            if (widget == null) {
                return;
            }
            deleteAppWidgetLocked(widget);
            saveGroupStateAsync(userId);
            if (DEBUG) {
                android.util.Slog.i(TAG, "Deleted widget id " + appWidgetId + " for host " + widget.host.id);
            }
        }
    }

    public boolean hasBindAppWidgetPermission(java.lang.String packageName, int grantId) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "hasBindAppWidgetPermission() " + android.os.UserHandle.getCallingUserId());
        }
        this.mSecurityPolicy.enforceModifyAppWidgetBindPermissions(packageName);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(grantId);
            int packageUid = getUidForPackage(packageName, grantId);
            if (packageUid < 0) {
                return false;
            }
            android.util.Pair<java.lang.Integer, java.lang.String> packageId = android.util.Pair.create(java.lang.Integer.valueOf(grantId), packageName);
            return this.mPackagesWithBindWidgetPermission.contains(packageId);
        }
    }

    public void setBindAppWidgetPermission(java.lang.String packageName, int grantId, boolean grantPermission) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "setBindAppWidgetPermission() " + android.os.UserHandle.getCallingUserId());
        }
        this.mSecurityPolicy.enforceModifyAppWidgetBindPermissions(packageName);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(grantId);
            int packageUid = getUidForPackage(packageName, grantId);
            if (packageUid < 0) {
                return;
            }
            android.util.Pair<java.lang.Integer, java.lang.String> packageId = android.util.Pair.create(java.lang.Integer.valueOf(grantId), packageName);
            if (grantPermission) {
                this.mPackagesWithBindWidgetPermission.add(packageId);
            } else {
                this.mPackagesWithBindWidgetPermission.remove(packageId);
            }
            saveGroupStateAsync(grantId);
        }
    }

    public android.content.IntentSender createAppWidgetConfigIntentSender(java.lang.String callingPackage, int appWidgetId, int intentFlags) throws java.lang.Throwable {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "createAppWidgetConfigIntentSender() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            try {
                try {
                    ensureGroupStateLoadedLocked(userId);
                    com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
                    if (widget == null) {
                        throw new java.lang.IllegalArgumentException("Bad widget id " + appWidgetId);
                    }
                    com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = widget.provider;
                    if (provider == null) {
                        throw new java.lang.IllegalArgumentException("Widget not bound " + appWidgetId);
                    }
                    int secureFlags = intentFlags & (-196);
                    android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_CONFIGURE");
                    intent.putExtra("appWidgetId", appWidgetId);
                    intent.setComponent(provider.getInfoLocked(this.mContext).configure);
                    intent.setFlags(secureFlags);
                    android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(2);
                    long identity = android.os.Binder.clearCallingIdentity();
                    try {
                        return android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, 1409286144, options.toBundle(), new android.os.UserHandle(provider.getUserId())).getIntentSender();
                    } finally {
                        android.os.Binder.restoreCallingIdentity(identity);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public boolean bindAppWidgetId(java.lang.String callingPackage, int appWidgetId, int providerProfileId, android.content.ComponentName providerComponent, android.os.Bundle options) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "bindAppWidgetId() " + userId + ", caller:" + android.os.Debug.getCallers(7));
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        if (!this.mSecurityPolicy.isEnabledGroupProfile(providerProfileId)) {
            android.util.Slog.i(TAG, "bindAppWidgetId() !mSecurityPolicy.isEnabledGroupProfile,providerProfileId: " + providerProfileId);
            return false;
        }
        if (!this.mSecurityPolicy.isProviderInCallerOrInProfileAndWhitelListed(providerComponent.getPackageName(), providerProfileId)) {
            android.util.Slog.i(TAG, "bindAppWidgetId() !mSecurityPolicy.isProviderInCallerOrInProfileAndWhitelListed,providerProfileId: " + providerProfileId + ", providerComponent.getPackageName()" + providerComponent.getPackageName());
            return false;
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            if (!this.mSecurityPolicy.hasCallerBindPermissionOrBindWhiteListedLocked(callingPackage)) {
                android.util.Slog.i(TAG, "bindAppWidgetId() !mSecurityPolicy.hasCallerBindPermissionOrBindWhiteListedLocked,callingPackage: " + callingPackage);
                return false;
            }
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
            if (widget == null) {
                android.util.Slog.e(TAG, "Bad widget id " + appWidgetId);
                return false;
            }
            if (widget.provider == null) {
                int providerUid = getUidForPackage(providerComponent.getPackageName(), providerProfileId);
                if (providerUid < 0) {
                    android.util.Slog.e(TAG, "Package " + providerComponent.getPackageName() + " not installed  for profile " + providerProfileId);
                    return false;
                }
                com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(providerUid, providerComponent);
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
                if (provider == null) {
                    android.util.Slog.e(TAG, "No widget provider " + providerComponent + " for profile " + providerProfileId);
                    return false;
                }
                if (provider.zombie) {
                    android.util.Slog.e(TAG, "Can't bind to a 3rd party provider in safe mode " + provider);
                    return false;
                }
                widget.provider = provider;
                widget.options = options != null ? cloneIfLocalBinder(options) : new android.os.Bundle();
                if (!widget.options.containsKey("appWidgetCategory")) {
                    widget.options.putInt("appWidgetCategory", 1);
                }
                provider.widgets.add(widget);
                onWidgetProviderAddedOrChangedLocked(widget);
                int widgetCount = provider.widgets.size();
                if (widgetCount == 1) {
                    sendEnableAndUpdateIntentLocked(provider, new int[]{appWidgetId});
                } else {
                    sendUpdateIntentLocked(provider, new int[]{appWidgetId}, true);
                }
                registerForBroadcastsLocked(provider, getWidgetIds(provider.widgets));
                saveGroupStateAsync(userId);
                android.util.Slog.i(TAG, "Bound widget " + appWidgetId + " to provider " + provider.id);
                if (provider.info != null && provider.info.provider != null && provider.id != null && widget.provider.info.provider != null && widget.provider.id != null) {
                    this.mAppWidgetServiceExt.hookUpdateWidgetSate(provider.id.uid, provider.info.provider.getPackageName(), true);
                }
                return true;
            }
            android.util.Slog.e(TAG, "Widget id " + appWidgetId + " already bound to: " + widget.provider.id);
            return false;
        }
    }

    public int[] getAppWidgetIds(android.content.ComponentName componentName) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "getAppWidgetIds() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(android.os.Binder.getCallingUid(), componentName);
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
            if (provider != null) {
                return getWidgetIds(provider.widgets);
            }
            return new int[0];
        }
    }

    public int[] getAppWidgetIdsForHost(java.lang.String callingPackage, int hostId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "getAppWidgetIdsForHost() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.HostId id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(android.os.Binder.getCallingUid(), hostId, callingPackage);
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupHostLocked(id);
            if (host != null) {
                return getWidgetIds(host.widgets);
            }
            return new int[0];
        }
    }

    public boolean bindRemoteViewsService(java.lang.String callingPackage, int appWidgetId, android.content.Intent intent, android.app.IApplicationThread caller, android.os.IBinder activtiyToken, android.app.IServiceConnection connection, long flags) throws java.lang.Throwable {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "bindRemoteViewsService() " + userId);
        }
        synchronized (this.mLock) {
            try {
                try {
                    ensureGroupStateLoadedLocked(userId);
                    com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
                    if (widget == null) {
                        throw new java.lang.IllegalArgumentException("Bad widget id");
                    }
                    if (widget.provider == null) {
                        throw new java.lang.IllegalArgumentException("No provider for widget " + appWidgetId);
                    }
                    android.content.ComponentName componentName = intent.getComponent();
                    java.lang.String providerPackage = widget.provider.id.componentName.getPackageName();
                    java.lang.String servicePackage = componentName.getPackageName();
                    if (!servicePackage.equals(providerPackage)) {
                        throw new java.lang.SecurityException("The taget service not in the same package as the widget provider");
                    }
                    this.mSecurityPolicy.enforceServiceExistsAndRequiresBindRemoteViewsPermission(componentName, widget.provider.getUserId());
                    long callingIdentity = android.os.Binder.clearCallingIdentity();
                    try {
                        try {
                            if (android.app.ActivityManager.getService().bindService(caller, activtiyToken, intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), connection, flags & 33554433, this.mContext.getOpPackageName(), widget.provider.getUserId()) != 0) {
                                incrementAppWidgetServiceRefCount(appWidgetId, android.util.Pair.create(java.lang.Integer.valueOf(widget.provider.id.uid), new android.content.Intent.FilterComparison(intent)));
                                android.os.Binder.restoreCallingIdentity(callingIdentity);
                                return true;
                            }
                        } catch (android.os.RemoteException e) {
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(callingIdentity);
                            throw th;
                        }
                    } catch (android.os.RemoteException e2) {
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                    android.os.Binder.restoreCallingIdentity(callingIdentity);
                    return false;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
            throw th;
        }
    }

    public void deleteHost(java.lang.String callingPackage, int hostId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "deleteHost() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.HostId id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(android.os.Binder.getCallingUid(), hostId, callingPackage);
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupHostLocked(id);
            if (host == null) {
                return;
            }
            deleteHostLocked(host);
            saveGroupStateAsync(userId);
            if (DEBUG) {
                android.util.Slog.i(TAG, "Deleted host " + host.id);
            }
        }
    }

    public void deleteAllHosts() {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "deleteAllHosts() " + userId);
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            boolean changed = false;
            int N = this.mHosts.size();
            for (int i = N - 1; i >= 0; i--) {
                com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i);
                if (host.id.uid == android.os.Binder.getCallingUid()) {
                    deleteHostLocked(host);
                    changed = true;
                    if (DEBUG) {
                        android.util.Slog.i(TAG, "Deleted host " + host.id);
                    }
                }
            }
            if (changed) {
                saveGroupStateAsync(userId);
            }
        }
    }

    public android.appwidget.AppWidgetProviderInfo getAppWidgetInfo(java.lang.String callingPackage, int appWidgetId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "getAppWidgetInfo() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
            if (widget != null && widget.provider != null && !widget.provider.zombie) {
                android.appwidget.AppWidgetProviderInfo info = widget.provider.getInfoLocked(this.mContext);
                if (info == null) {
                    android.util.Slog.e(TAG, "getAppWidgetInfo() returns null because widget.provider.getInfoLocked() returned null. appWidgetId=" + appWidgetId + " userId=" + userId + " widget=" + widget);
                    return null;
                }
                android.appwidget.AppWidgetProviderInfo ret = cloneIfLocalBinder(info);
                if (ret == null) {
                    android.util.Slog.e(TAG, "getAppWidgetInfo() returns null because cloneIfLocalBinder() returned null. appWidgetId=" + appWidgetId + " userId=" + userId + " widget=" + widget + " appWidgetProviderInfo=" + info);
                }
                return ret;
            }
            if (widget == null) {
                android.util.Slog.e(TAG, "getAppWidgetInfo() returns null because widget is null. appWidgetId=" + appWidgetId + " userId=" + userId);
            } else if (widget.provider == null) {
                android.util.Slog.e(TAG, "getAppWidgetInfo() returns null because widget.provider is null. appWidgetId=" + appWidgetId + " userId=" + userId + " widget=" + widget);
            } else {
                android.util.Slog.e(TAG, "getAppWidgetInfo() returns null because widget.provider is zombie. appWidgetId=" + appWidgetId + " userId=" + userId + " widget=" + widget);
            }
            return null;
        }
    }

    public android.widget.RemoteViews getAppWidgetViews(java.lang.String callingPackage, int appWidgetId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "getAppWidgetViews() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
            if (widget == null) {
                return null;
            }
            return cloneIfLocalBinder(widget.getEffectiveViewsLocked());
        }
    }

    public void updateAppWidgetOptions(java.lang.String callingPackage, int appWidgetId, android.os.Bundle options) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "updateAppWidgetOptions() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
            if (widget == null) {
                return;
            }
            widget.options.putAll(options);
            sendOptionsChangedIntentLocked(widget);
            this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(android.os.Binder.getCallingUid());
            saveGroupStateAsync(userId);
        }
    }

    public android.os.Bundle getAppWidgetOptions(java.lang.String callingPackage, int appWidgetId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "getAppWidgetOptions() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
            if (widget != null && widget.options != null) {
                return cloneIfLocalBinder(widget.options);
            }
            return android.os.Bundle.EMPTY;
        }
    }

    public void updateAppWidgetIds(java.lang.String callingPackage, int[] appWidgetIds, android.widget.RemoteViews views) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "updateAppWidgetIds() " + android.os.UserHandle.getCallingUserId());
        }
        updateAppWidgetIds(callingPackage, appWidgetIds, views, false);
    }

    public void partiallyUpdateAppWidgetIds(java.lang.String callingPackage, int[] appWidgetIds, android.widget.RemoteViews views) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "partiallyUpdateAppWidgetIds() " + android.os.UserHandle.getCallingUserId());
        }
        updateAppWidgetIds(callingPackage, appWidgetIds, views, true);
    }

    public void notifyProviderInheritance(android.content.ComponentName[] componentNames) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "notifyProviderInheritance() " + userId);
        }
        if (componentNames == null) {
            return;
        }
        for (android.content.ComponentName componentName : componentNames) {
            if (componentName == null) {
                return;
            }
            this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            for (android.content.ComponentName componentName2 : componentNames) {
                com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(android.os.Binder.getCallingUid(), componentName2);
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
                if (provider != null && provider.info != null) {
                    provider.info.isExtendedFromAppWidgetProvider = true;
                }
                return;
            }
            saveGroupStateAsync(userId);
        }
    }

    public void notifyAppWidgetViewDataChanged(java.lang.String callingPackage, int[] appWidgetIds, int viewId) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "notifyAppWidgetViewDataChanged() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        if (appWidgetIds == null || appWidgetIds.length == 0) {
            return;
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            for (int appWidgetId : appWidgetIds) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
                if (widget != null) {
                    scheduleNotifyAppWidgetViewDataChanged(widget, viewId);
                    this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(android.os.Binder.getCallingUid());
                }
            }
        }
    }

    public void updateAppWidgetProvider(android.content.ComponentName componentName, android.widget.RemoteViews views) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "updateAppWidgetProvider() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(android.os.Binder.getCallingUid(), componentName);
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
            if (provider == null) {
                android.util.Slog.w(TAG, "Provider doesn't exist " + providerId);
                return;
            }
            java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Widget> instances = provider.widgets;
            int N = instances.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = instances.get(i);
                updateAppWidgetInstanceLocked(widget, views, false);
            }
            this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(android.os.Binder.getCallingUid());
        }
    }

    public void updateAppWidgetProviderInfo(android.content.ComponentName componentName, java.lang.String metadataKey) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "updateAppWidgetProvider() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(android.os.Binder.getCallingUid(), componentName);
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
            if (provider == null) {
                throw new java.lang.IllegalArgumentException(componentName + " is not a valid AppWidget provider");
            }
            if (java.util.Objects.equals(provider.infoTag, metadataKey)) {
                return;
            }
            java.lang.String keyToUse = metadataKey == null ? "android.appwidget.provider" : metadataKey;
            android.appwidget.AppWidgetProviderInfo info = parseAppWidgetProviderInfo(this.mContext, providerId, provider.getPartialInfoLocked().providerInfo, keyToUse);
            if (info == null) {
                throw new java.lang.IllegalArgumentException("Unable to parse " + keyToUse + " meta-data to a valid AppWidget provider");
            }
            provider.setInfoLocked(info);
            provider.infoTag = metadataKey;
            int N = provider.widgets.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = provider.widgets.get(i);
                scheduleNotifyProviderChangedLocked(widget);
                updateAppWidgetInstanceLocked(widget, widget.views, false);
            }
            saveGroupStateAsync(userId);
            scheduleNotifyGroupHostsForProvidersChangedLocked(userId);
        }
    }

    public boolean isRequestPinAppWidgetSupported() {
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.isCallerInstantAppLocked()) {
                android.util.Slog.w(TAG, "Instant uid " + android.os.Binder.getCallingUid() + " query information about app widgets");
                return false;
            }
            return ((android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class)).isRequestPinItemSupported(android.os.UserHandle.getCallingUserId(), 2);
        }
    }

    public boolean requestPinAppWidget(java.lang.String callingPackage, android.content.ComponentName componentName, android.os.Bundle extras, android.content.IntentSender resultSender) {
        com.android.server.appwidget.AppWidgetServiceImpl.ProviderId id;
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(callingUid);
        if (DEBUG) {
            android.util.Slog.i(TAG, "requestPinAppWidget() " + userId);
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            java.lang.String pkg = componentName.getPackageName();
            if (!this.mPackageManagerInternal.isSameApp(pkg, callingUid, userId)) {
                if (!injectHasAccessWidgetsPermission(android.os.Binder.getCallingPid(), callingUid)) {
                    return false;
                }
                id = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(this.mPackageManagerInternal.getPackageUid(pkg, 0L, userId), componentName);
            } else {
                id = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(callingUid, componentName);
            }
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(id);
            if (provider != null && !provider.zombie) {
                android.appwidget.AppWidgetProviderInfo info = provider.getInfoLocked(this.mContext);
                if ((info.widgetCategory & 1) == 0) {
                    return false;
                }
                return ((android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class)).requestPinAppWidget(callingPackage, info, extras, resultSender, userId);
            }
            return false;
        }
    }

    private boolean injectHasAccessWidgetsPermission(int callingPid, int callingUid) {
        return this.mContext.checkPermission("android.permission.CLEAR_APP_USER_DATA", callingPid, callingUid) == 0;
    }

    public android.content.pm.ParceledListSlice<android.appwidget.AppWidgetProviderInfo> getInstalledProvidersForProfile(int categoryFilter, int profileId, java.lang.String packageName) {
        int providerProfileId;
        int userId = android.os.UserHandle.getCallingUserId();
        int callingUid = android.os.Binder.getCallingUid();
        if (DEBUG) {
            android.util.Slog.i(TAG, "getInstalledProvidersForProfiles() " + userId);
        }
        if (!this.mSecurityPolicy.isEnabledGroupProfile(profileId)) {
            return null;
        }
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.isCallerInstantAppLocked()) {
                android.util.Slog.w(TAG, "Instant uid " + callingUid + " cannot access widget providers");
                return android.content.pm.ParceledListSlice.emptyList();
            }
            ensureGroupStateLoadedLocked(userId);
            java.util.ArrayList<android.appwidget.AppWidgetProviderInfo> result = new java.util.ArrayList<>();
            int providerCount = this.mProviders.size();
            for (int i = 0; i < providerCount; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
                java.lang.String providerPackageName = provider.id.componentName.getPackageName();
                boolean inPackage = packageName == null || providerPackageName.equals(packageName);
                if (!provider.zombie && inPackage) {
                    android.appwidget.AppWidgetProviderInfo info = provider.getInfoLocked(this.mContext);
                    if ((info.widgetCategory & categoryFilter) != 0 && (providerProfileId = info.getProfile().getIdentifier()) == profileId && this.mSecurityPolicy.isProviderInCallerOrInProfileAndWhitelListed(providerPackageName, providerProfileId) && !this.mPackageManagerInternal.filterAppAccess(providerPackageName, callingUid, profileId)) {
                        result.add(cloneIfLocalBinder(info));
                    }
                }
            }
            return new android.content.pm.ParceledListSlice<>(result);
        }
    }

    private void updateAppWidgetIds(java.lang.String callingPackage, int[] appWidgetIds, android.widget.RemoteViews views, boolean partially) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (appWidgetIds == null || appWidgetIds.length == 0) {
            android.util.Slog.d(TAG, "updateAppWidgetIds appWidgetIds is null or appWidgetIds.length == 0");
            return;
        }
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateAppWidgetIds remoteViews = " + views + "\naction size = " + views.getSequenceNumber());
        }
        if (android.os.Trace.isTagEnabled(8L)) {
            android.os.Trace.traceBegin(8L, "updateAppWidgetIds:" + views.getViewId());
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            for (int appWidgetId : appWidgetIds) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, android.os.Binder.getCallingUid(), callingPackage);
                if (widget != null) {
                    updateAppWidgetInstanceLocked(widget, views, partially);
                    this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(android.os.Binder.getCallingUid());
                } else {
                    android.util.Slog.i(TAG, "updateAppWidgetIds, widget == null, appWidgetId:" + appWidgetId);
                }
            }
        }
        android.os.Trace.traceEnd(8L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int incrementAndGetAppWidgetIdLocked(int userId) {
        int appWidgetId = peekNextAppWidgetIdLocked(userId) + 1;
        this.mNextAppWidgetIds.put(userId, appWidgetId);
        return appWidgetId;
    }

    private void setMinAppWidgetIdLocked(int userId, int minWidgetId) {
        int nextAppWidgetId = peekNextAppWidgetIdLocked(userId);
        if (nextAppWidgetId < minWidgetId) {
            this.mNextAppWidgetIds.put(userId, minWidgetId);
        }
    }

    private int peekNextAppWidgetIdLocked(int userId) {
        if (this.mNextAppWidgetIds.indexOfKey(userId) < 0) {
            return 1;
        }
        return this.mNextAppWidgetIds.get(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.appwidget.AppWidgetServiceImpl.Host lookupOrAddHostLocked(com.android.server.appwidget.AppWidgetServiceImpl.HostId id) {
        com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupHostLocked(id);
        if (host != null) {
            return host;
        }
        ensureHostCountBeforeAddLocked(id);
        com.android.server.appwidget.AppWidgetServiceImpl.Host host2 = new com.android.server.appwidget.AppWidgetServiceImpl.Host();
        host2.id = id;
        this.mHosts.add(host2);
        return host2;
    }

    private void ensureHostCountBeforeAddLocked(com.android.server.appwidget.AppWidgetServiceImpl.HostId hostId) {
        java.util.List<com.android.server.appwidget.AppWidgetServiceImpl.Host> hosts = new java.util.ArrayList<>();
        for (com.android.server.appwidget.AppWidgetServiceImpl.Host host : this.mHosts) {
            if (host.id.uid == hostId.uid && host.id.packageName.equals(hostId.packageName)) {
                hosts.add(host);
            }
        }
        while (hosts.size() >= 20) {
            deleteHostLocked(hosts.remove(0));
        }
    }

    private void deleteHostLocked(com.android.server.appwidget.AppWidgetServiceImpl.Host host) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "deleteHostLocked() " + host);
        }
        int N = host.widgets.size();
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = host.widgets.remove(i);
            deleteAppWidgetLocked(widget);
        }
        this.mHosts.remove(host);
        host.callbacks = null;
    }

    private void deleteAppWidgetLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "deleteAppWidgetLocked() " + widget);
        }
        decrementAppWidgetServiceRefCount(widget);
        com.android.server.appwidget.AppWidgetServiceImpl.Host host = widget.host;
        host.widgets.remove(widget);
        pruneHostLocked(host);
        removeWidgetLocked(widget);
        com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = widget.provider;
        if (provider != null) {
            provider.widgets.remove(widget);
            if (!provider.zombie) {
                if (!provider.maskedByStoppedPackage) {
                    sendDeletedIntentLocked(widget);
                } else {
                    provider.pendingDeletedWidgetIds.add(widget.appWidgetId);
                }
                if (provider.widgets.isEmpty()) {
                    cancelBroadcastsLocked(provider);
                    if (!provider.maskedByStoppedPackage) {
                        sendDisabledIntentLocked(provider);
                    }
                }
            }
        }
    }

    private void cancelBroadcastsLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "cancelBroadcastsLocked() for " + provider);
        }
        if (provider.broadcast != null) {
            final android.app.PendingIntent broadcast = provider.broadcast;
            this.mSaveStateHandler.post(new java.lang.Runnable() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$cancelBroadcastsLocked$0(broadcast);
                }
            });
            provider.broadcast = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelBroadcastsLocked$0(android.app.PendingIntent broadcast) {
        this.mAlarmManager.cancel(broadcast);
        broadcast.cancel();
    }

    private void destroyRemoteViewsService(final android.content.Intent intent, com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        android.content.ServiceConnection conn = new android.content.ServiceConnection() { // from class: com.android.server.appwidget.AppWidgetServiceImpl.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                com.android.internal.widget.IRemoteViewsFactory cb = com.android.internal.widget.IRemoteViewsFactory.Stub.asInterface(service);
                try {
                    cb.onDestroy(intent);
                } catch (android.os.RemoteException re) {
                    android.util.Slog.e(com.android.server.appwidget.AppWidgetServiceImpl.TAG, "Error calling remove view factory", re);
                }
                com.android.server.appwidget.AppWidgetServiceImpl.this.mContext.unbindService(this);
            }

            @Override // android.content.ServiceConnection
            public void onNullBinding(android.content.ComponentName name) {
                com.android.server.appwidget.AppWidgetServiceImpl.this.mContext.unbindService(this);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName name) {
            }
        };
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.bindServiceAsUser(intent, conn, 33554433, widget.provider.id.getProfile());
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void incrementAppWidgetServiceRefCount(int appWidgetId, android.util.Pair<java.lang.Integer, android.content.Intent.FilterComparison> serviceId) {
        java.util.HashSet<java.lang.Integer> appWidgetIds;
        if (this.mRemoteViewsServicesAppWidgets.containsKey(serviceId)) {
            appWidgetIds = this.mRemoteViewsServicesAppWidgets.get(serviceId);
        } else {
            appWidgetIds = new java.util.HashSet<>();
            this.mRemoteViewsServicesAppWidgets.put(serviceId, appWidgetIds);
        }
        appWidgetIds.add(java.lang.Integer.valueOf(appWidgetId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decrementAppWidgetServiceRefCount(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        java.util.Iterator<android.util.Pair<java.lang.Integer, android.content.Intent.FilterComparison>> it = this.mRemoteViewsServicesAppWidgets.keySet().iterator();
        while (it.hasNext()) {
            android.util.Pair<java.lang.Integer, android.content.Intent.FilterComparison> key = it.next();
            java.util.HashSet<java.lang.Integer> ids = this.mRemoteViewsServicesAppWidgets.get(key);
            if (ids.remove(java.lang.Integer.valueOf(widget.appWidgetId)) && ids.isEmpty() && !widget.provider.maskedByStoppedPackage) {
                destroyRemoteViewsService(((android.content.Intent.FilterComparison) key.second).getIntent(), widget);
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveGroupStateAsync(int groupId) {
        if (android.appwidget.flags.Flags.removeAppWidgetServiceIoFromCriticalPath()) {
            this.mSaveStateHandler.removeMessages(groupId);
            this.mSaveStateHandler.sendEmptyMessage(groupId);
        } else {
            this.mSaveStateHandler.post(new com.android.server.appwidget.AppWidgetServiceImpl.SaveStateRunnable(groupId));
        }
    }

    private void updateAppWidgetInstanceLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, android.widget.RemoteViews views, boolean isPartialUpdate) {
        int memoryUsage;
        if (widget != null && widget.provider != null && !widget.provider.zombie && !widget.host.zombie) {
            if (isPartialUpdate && widget.views != null) {
                widget.views.mergeRemoteViews(views);
            } else {
                widget.views = views;
            }
            if (android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) != 1000 && widget.views != null && (memoryUsage = widget.views.estimateMemoryUsage()) > this.mMaxWidgetBitmapMemory) {
                widget.views = null;
                throw new java.lang.IllegalArgumentException("RemoteViews for widget update exceeds maximum bitmap memory usage (used: " + memoryUsage + ", max: " + this.mMaxWidgetBitmapMemory + ")");
            }
            scheduleNotifyUpdateAppWidgetLocked(widget, widget.getEffectiveViewsLocked());
            return;
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "updateAppWidgetInstanceLocked skip");
        }
    }

    private void scheduleNotifyAppWidgetViewDataChanged(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, int viewId) {
        if (viewId == 0 || viewId == 1) {
            android.util.Slog.i(TAG, "scheduleNotifyAppWidgetViewDataChanged, viewId: " + viewId);
            return;
        }
        long requestId = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            widget.updateSequenceNos.put(viewId, requestId);
        }
        if (widget == null || widget.host == null || widget.host.zombie || widget.host.callbacks == null || widget.provider == null || widget.provider.zombie) {
            android.util.Slog.i(TAG, "scheduleNotifyAppWidgetViewDataChanged, widget something null");
            return;
        }
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = widget.host;
        args.arg2 = widget.host.callbacks;
        args.arg3 = java.lang.Long.valueOf(requestId);
        args.argi1 = widget.appWidgetId;
        args.argi2 = viewId;
        this.mCallbackHandler.obtainMessage(4, args).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyAppWidgetViewDataChanged(com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.internal.appwidget.IAppWidgetHost callbacks, int appWidgetId, int viewId, long requestId) {
        try {
            android.util.Slog.d(TAG, "Trying to notify widget view data changed");
            callbacks.viewDataChanged(appWidgetId, viewId);
            host.lastWidgetUpdateSequenceNo = requestId;
        } catch (android.os.RemoteException e) {
            callbacks = null;
        }
        synchronized (this.mLock) {
            if (callbacks == null) {
                host.callbacks = null;
                java.util.Set<android.util.Pair<java.lang.Integer, android.content.Intent.FilterComparison>> keys = this.mRemoteViewsServicesAppWidgets.keySet();
                for (android.util.Pair<java.lang.Integer, android.content.Intent.FilterComparison> key : keys) {
                    if (this.mRemoteViewsServicesAppWidgets.get(key).contains(java.lang.Integer.valueOf(appWidgetId))) {
                        android.content.ServiceConnection connection = new android.content.ServiceConnection() { // from class: com.android.server.appwidget.AppWidgetServiceImpl.3
                            @Override // android.content.ServiceConnection
                            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                                com.android.internal.widget.IRemoteViewsFactory cb = com.android.internal.widget.IRemoteViewsFactory.Stub.asInterface(service);
                                try {
                                    cb.onDataSetChangedAsync();
                                } catch (android.os.RemoteException e2) {
                                    android.util.Slog.e(com.android.server.appwidget.AppWidgetServiceImpl.TAG, "Error calling onDataSetChangedAsync()", e2);
                                }
                                com.android.server.appwidget.AppWidgetServiceImpl.this.mContext.unbindService(this);
                            }

                            @Override // android.content.ServiceConnection
                            public void onNullBinding(android.content.ComponentName name) {
                                com.android.server.appwidget.AppWidgetServiceImpl.this.mContext.unbindService(this);
                            }

                            @Override // android.content.ServiceConnection
                            public void onServiceDisconnected(android.content.ComponentName name) {
                            }
                        };
                        int userId = android.os.UserHandle.getUserId(((java.lang.Integer) key.first).intValue());
                        android.content.Intent intent = ((android.content.Intent.FilterComparison) key.second).getIntent();
                        bindService(intent, connection, new android.os.UserHandle(userId));
                    }
                }
            }
        }
    }

    private void scheduleNotifyUpdateAppWidgetLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, android.widget.RemoteViews updateViews) {
        long requestId = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            if (widget.trackingUpdate) {
                widget.trackingUpdate = false;
                android.util.Log.i(TAG, "Widget update received " + widget.toString());
                android.os.Trace.asyncTraceEnd(64L, "appwidget update-intent " + widget.provider.id.toString(), widget.appWidgetId);
            }
            widget.updateSequenceNos.put(0, requestId);
        }
        if (widget == null || widget.provider == null || widget.provider.zombie || widget.host.callbacks == null || widget.host.zombie) {
            android.util.Slog.i(TAG, "scheduleNotifyUpdateAppWidgetLocked, widget info is null");
            return;
        }
        if (updateViews != null) {
            updateViews = new android.widget.RemoteViews(updateViews);
            updateViews.setProviderInstanceId(requestId);
        }
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = widget.host;
        args.arg2 = widget.host.callbacks;
        args.arg3 = updateViews;
        args.arg4 = java.lang.Long.valueOf(requestId);
        args.argi1 = widget.appWidgetId;
        if (updateViews != null && updateViews.isLegacyListRemoteViews()) {
            this.mCallbackHandler.obtainMessage(6, args).sendToTarget();
        } else {
            this.mCallbackHandler.obtainMessage(1, args).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyUpdateAppWidgetDeferred(com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.internal.appwidget.IAppWidgetHost callbacks, int appWidgetId, long requestId) {
        try {
            android.util.Slog.d(TAG, "Trying to notify widget update deferred for id: " + appWidgetId);
            callbacks.updateAppWidgetDeferred(appWidgetId);
            host.lastWidgetUpdateSequenceNo = requestId;
        } catch (android.os.RemoteException re) {
            synchronized (this.mLock) {
                android.util.Slog.e(TAG, "Widget host dead: " + host.id, re);
                host.callbacks = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyUpdateAppWidget(com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.internal.appwidget.IAppWidgetHost callbacks, int appWidgetId, android.widget.RemoteViews views, long requestId) {
        if (views != null && views.mApplication != null) {
            android.util.Slog.i(TAG, "handleNotifyUpdateAppWidget,appWidgetId: " + appWidgetId + ",packageName:" + views.mApplication.packageName);
        } else {
            android.util.Slog.i(TAG, "handleNotifyUpdateAppWidget,appWidgetId: " + appWidgetId);
        }
        try {
            android.util.Slog.d(TAG, "Trying to notify widget update for package " + (views == null ? "null" : views.getPackage()) + " with widget id: " + appWidgetId);
            callbacks.updateAppWidget(appWidgetId, views);
            host.lastWidgetUpdateSequenceNo = requestId;
        } catch (android.os.RemoteException re) {
            synchronized (this.mLock) {
                android.util.Slog.e(TAG, "Widget host dead: " + host.id, re);
                host.callbacks = null;
            }
        }
    }

    private void scheduleNotifyProviderChangedLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        long requestId = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            widget.updateSequenceNos.clear();
            widget.updateSequenceNos.append(1, requestId);
        }
        if (widget == null || widget.provider == null || widget.provider.zombie || widget.host.callbacks == null || widget.host.zombie) {
            return;
        }
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = widget.host;
        args.arg2 = widget.host.callbacks;
        args.arg3 = widget.provider.getInfoLocked(this.mContext);
        args.arg4 = java.lang.Long.valueOf(requestId);
        args.argi1 = widget.appWidgetId;
        this.mCallbackHandler.obtainMessage(2, args).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyProviderChanged(com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.internal.appwidget.IAppWidgetHost callbacks, int appWidgetId, android.appwidget.AppWidgetProviderInfo info, long requestId) {
        try {
            android.util.Slog.d(TAG, "Trying to notify provider update");
            callbacks.providerChanged(appWidgetId, info);
            host.lastWidgetUpdateSequenceNo = requestId;
        } catch (android.os.RemoteException re) {
            synchronized (this.mLock) {
                android.util.Slog.e(TAG, "Widget host dead: " + host.id, re);
                host.callbacks = null;
            }
        }
    }

    private void scheduleNotifyAppWidgetRemovedLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        long requestId = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            if (widget.trackingUpdate) {
                widget.trackingUpdate = false;
                android.util.Log.i(TAG, "Widget removed " + widget.toString());
                android.os.Trace.asyncTraceEnd(64L, "appwidget update-intent " + widget.provider.id.toString(), widget.appWidgetId);
            }
            widget.updateSequenceNos.clear();
        }
        if (widget == null || widget.provider == null || widget.provider.zombie || widget.host.callbacks == null || widget.host.zombie) {
            android.util.Slog.i(TAG, "scheduleNotifyAppWidgetRemovedLocked, widget info is null");
            return;
        }
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = widget.host;
        args.arg2 = widget.host.callbacks;
        args.arg3 = java.lang.Long.valueOf(requestId);
        args.argi1 = widget.appWidgetId;
        this.mCallbackHandler.obtainMessage(5, args).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyAppWidgetRemoved(com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.internal.appwidget.IAppWidgetHost callbacks, int appWidgetId, long requestId) {
        android.util.Slog.i(TAG, "handleNotifyAppWidgetRemoved,appWidgetId: " + appWidgetId);
        try {
            android.util.Slog.d(TAG, "Trying to notify widget removed");
            callbacks.appWidgetRemoved(appWidgetId);
            host.lastWidgetUpdateSequenceNo = requestId;
        } catch (android.os.RemoteException re) {
            synchronized (this.mLock) {
                android.util.Slog.e(TAG, "Widget host dead: " + host.id, re);
                host.callbacks = null;
            }
        }
    }

    private void scheduleNotifyGroupHostsForProvidersChangedLocked(int userId) {
        int[] profileIds = this.mSecurityPolicy.getEnabledGroupProfileIds(userId);
        int N = this.mHosts.size();
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i);
            boolean hostInGroup = false;
            int M = profileIds.length;
            int j = 0;
            while (true) {
                if (j >= M) {
                    break;
                }
                int profileId = profileIds[j];
                if (host.getUserId() != profileId) {
                    j++;
                } else {
                    hostInGroup = true;
                    break;
                }
            }
            if (hostInGroup && host != null && !host.zombie && host.callbacks != null) {
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = host;
                args.arg2 = host.callbacks;
                this.mCallbackHandler.obtainMessage(3, args).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyProvidersChanged(com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.internal.appwidget.IAppWidgetHost callbacks) {
        try {
            android.util.Slog.d(TAG, "Trying to notify widget providers changed");
            callbacks.providersChanged();
        } catch (android.os.RemoteException re) {
            synchronized (this.mLock) {
                android.util.Slog.e(TAG, "Widget host dead: " + host.id, re);
                host.callbacks = null;
            }
        }
    }

    private static boolean isLocalBinder() {
        return android.os.Process.myPid() == android.os.Binder.getCallingPid();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.widget.RemoteViews cloneIfLocalBinder(android.widget.RemoteViews rv) {
        if (isLocalBinder() && rv != null) {
            return rv.clone();
        }
        return rv;
    }

    private static android.appwidget.AppWidgetProviderInfo cloneIfLocalBinder(android.appwidget.AppWidgetProviderInfo info) {
        if (isLocalBinder() && info != null) {
            return info.clone();
        }
        return info;
    }

    private static android.os.Bundle cloneIfLocalBinder(android.os.Bundle bundle) {
        if (isLocalBinder() && bundle != null) {
            return (android.os.Bundle) bundle.clone();
        }
        return bundle;
    }

    private com.android.server.appwidget.AppWidgetServiceImpl.Widget lookupWidgetLocked(int appWidgetId, int uid, java.lang.String packageName) {
        int N = this.mWidgets.size();
        for (int i = 0; i < N; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = this.mWidgets.get(i);
            if (widget.appWidgetId == appWidgetId && this.mSecurityPolicy.canAccessAppWidget(widget, uid, packageName)) {
                return widget;
            }
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "cannot find widget for appWidgetId=" + appWidgetId + " uid=" + uid + " packageName=" + packageName);
            return null;
        }
        return null;
    }

    private com.android.server.appwidget.AppWidgetServiceImpl.Provider lookupProviderLocked(com.android.server.appwidget.AppWidgetServiceImpl.ProviderId id) {
        int N = this.mProviders.size();
        for (int i = 0; i < N; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            if (provider.id.equals(id)) {
                return provider;
            }
        }
        return null;
    }

    private com.android.server.appwidget.AppWidgetServiceImpl.Host lookupHostLocked(com.android.server.appwidget.AppWidgetServiceImpl.HostId hostId) {
        int N = this.mHosts.size();
        for (int i = 0; i < N; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i);
            if (host.id.equals(hostId)) {
                return host;
            }
        }
        return null;
    }

    private void pruneHostLocked(com.android.server.appwidget.AppWidgetServiceImpl.Host host) {
        if (host.widgets.size() == 0 && host.callbacks == null) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "Pruning host " + host.id);
            }
            this.mHosts.remove(host);
        }
    }

    private void loadGroupWidgetProvidersLocked(int[] profileIds) {
        java.util.List<android.content.pm.ResolveInfo> allReceivers = null;
        android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_UPDATE");
        for (int profileId : profileIds) {
            java.util.List<android.content.pm.ResolveInfo> receivers = queryIntentReceivers(intent, profileId);
            if (receivers != null && !receivers.isEmpty()) {
                if (allReceivers == null) {
                    allReceivers = new java.util.ArrayList<>();
                }
                allReceivers.addAll(receivers);
            }
        }
        int N = allReceivers == null ? 0 : allReceivers.size();
        for (int i = 0; i < N; i++) {
            android.content.pm.ResolveInfo receiver = allReceivers.get(i);
            addProviderLocked(receiver);
        }
    }

    private boolean addProviderLocked(android.content.pm.ResolveInfo ri) {
        if ((ri.activityInfo.applicationInfo.flags & 262144) != 0) {
            return false;
        }
        android.content.ComponentName componentName = new android.content.ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
        com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(ri.activityInfo.applicationInfo.uid, componentName);
        com.android.server.appwidget.AppWidgetServiceImpl.Provider existing = lookupProviderLocked(providerId);
        if (existing == null) {
            com.android.server.appwidget.AppWidgetServiceImpl.ProviderId restoredProviderId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(-1, componentName);
            existing = lookupProviderLocked(restoredProviderId);
        }
        android.appwidget.AppWidgetProviderInfo info = createPartialProviderInfo(providerId, ri, existing);
        if ((com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.disablePrivateSpaceItemsOnHome() && android.multiuser.Flags.enablePrivateSpaceFeatures() && info != null && this.mUserManager.getUserProperties(info.getProfile()).areItemsRestrictedOnHomeScreen()) || info == null) {
            return false;
        }
        if (existing != null) {
            if (existing.zombie && !this.mSafeMode) {
                existing.id = providerId;
                existing.zombie = false;
                existing.setPartialInfoLocked(info);
                if (DEBUG) {
                    android.util.Slog.i(TAG, "Provider placeholder now reified: " + existing);
                    return true;
                }
                return true;
            }
            return true;
        }
        com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = new com.android.server.appwidget.AppWidgetServiceImpl.Provider();
        provider.id = providerId;
        provider.setPartialInfoLocked(info);
        this.mProviders.add(provider);
        return true;
    }

    private void deleteWidgetsLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int userId) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "deleteWidgetsLocked() provider=" + provider + " userId=" + userId);
        }
        int N = provider.widgets.size();
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = provider.widgets.get(i);
            if (userId == -1 || userId == widget.host.getUserId()) {
                provider.widgets.remove(i);
                updateAppWidgetInstanceLocked(widget, null, false);
                widget.host.widgets.remove(widget);
                removeWidgetLocked(widget);
                widget.provider = null;
                pruneHostLocked(widget.host);
                widget.host = null;
            }
        }
    }

    private void deleteProviderLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider) {
        deleteWidgetsLocked(provider, -1);
        this.mProviders.remove(provider);
        this.mGeneratedPreviewsApiCounter.remove(provider.id);
        cancelBroadcastsLocked(provider);
    }

    private void sendEnableAndUpdateIntentLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider p, int[] appWidgetIds) {
        boolean canSendCombinedBroadcast = this.mIsCombinedBroadcastEnabled && p.info != null && p.info.isExtendedFromAppWidgetProvider;
        if (!canSendCombinedBroadcast) {
            sendEnableIntentLocked(p);
            sendUpdateIntentLocked(p, appWidgetIds, true);
        } else {
            android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_ENABLE_AND_UPDATE");
            intent.putExtra("appWidgetIds", appWidgetIds);
            intent.setComponent(p.id.componentName);
            sendBroadcastAsUser(intent, p.id.getProfile(), true);
        }
    }

    private void sendEnableIntentLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider p) {
        android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_ENABLED");
        intent.setComponent(p.id.componentName);
        android.util.Slog.d(TAG, "sendEnableIntentLocked_ACTION_APPWIDGET_ENABLED");
        intent.setFlags(268435456);
        sendBroadcastAsUser(intent, p.id.getProfile(), true);
    }

    private void sendUpdateIntentLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int[] appWidgetIds, boolean interactive) {
        android.content.Intent intent = createUpdateIntentLocked(provider, appWidgetIds);
        android.util.Slog.d(TAG, "sendUpdateIntentLocked_ACTION_APPWIDGET_UPDATE");
        sendBroadcastAsUser(intent, provider.id.getProfile(), interactive);
    }

    private android.content.Intent createUpdateIntentLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int[] appWidgetIds) {
        android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", appWidgetIds);
        intent.setComponent(provider.id.componentName);
        intent.setFlags(268435456);
        return intent;
    }

    private void sendDeletedIntentLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        sendDeletedIntentLocked(widget.provider.id.componentName, widget.provider.id.getProfile(), widget.appWidgetId);
    }

    private void sendDeletedIntentLocked(android.content.ComponentName provider, android.os.UserHandle profile, int appWidgetId) {
        android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_DELETED");
        intent.setComponent(provider);
        intent.putExtra("appWidgetId", appWidgetId);
        sendBroadcastAsUser(intent, profile, false);
    }

    private void sendDisabledIntentLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider) {
        android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_DISABLED");
        intent.setComponent(provider.id.componentName);
        sendBroadcastAsUser(intent, provider.id.getProfile(), false);
    }

    public void sendOptionsChangedIntentLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS");
        intent.setComponent(widget.provider.id.componentName);
        intent.putExtra("appWidgetId", widget.appWidgetId);
        intent.putExtra("appWidgetOptions", widget.options);
        sendBroadcastAsUser(intent, widget.provider.id.getProfile(), true);
    }

    private void registerForBroadcastsLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int[] appWidgetIds) {
        android.appwidget.AppWidgetProviderInfo info = provider.getInfoLocked(this.mContext);
        if (info.updatePeriodMillis > 0) {
            boolean alreadyRegistered = provider.broadcast != null;
            android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra("appWidgetIds", appWidgetIds);
            intent.setComponent(info.provider);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                provider.broadcast = android.app.PendingIntent.getBroadcastAsUser(this.mContext, 1, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, info.getProfile());
                if (!alreadyRegistered) {
                    final long period = java.lang.Math.max(info.updatePeriodMillis, 1800000);
                    final android.app.PendingIntent broadcast = provider.broadcast;
                    this.mSaveStateHandler.post(new java.lang.Runnable() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$registerForBroadcastsLocked$1(period, broadcast);
                        }
                    });
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerForBroadcastsLocked$1(long period, android.app.PendingIntent broadcast) {
        this.mAlarmManager.setInexactRepeating(this.mAppWidgetServiceExt.hookGetRepeatAlarmType(2), android.os.SystemClock.elapsedRealtime() + period, period, broadcast);
    }

    private static int[] getWidgetIds(java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Widget> widgets) {
        int instancesSize = widgets.size();
        int[] appWidgetIds = new int[instancesSize];
        for (int i = 0; i < instancesSize; i++) {
            appWidgetIds[i] = widgets.get(i).appWidgetId;
        }
        return appWidgetIds;
    }

    private static void dumpProviderLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int index, java.io.PrintWriter pw) {
        android.appwidget.AppWidgetProviderInfo info = provider.getPartialInfoLocked();
        pw.print("  [");
        pw.print(index);
        pw.print("] provider ");
        pw.println(provider.id);
        pw.print("    min=(");
        pw.print(info.minWidth);
        pw.print("x");
        pw.print(info.minHeight);
        pw.print(")   minResize=(");
        pw.print(info.minResizeWidth);
        pw.print("x");
        pw.print(info.minResizeHeight);
        pw.print(") updatePeriodMillis=");
        pw.print(info.updatePeriodMillis);
        pw.print(" resizeMode=");
        pw.print(info.resizeMode);
        pw.print(" widgetCategory=");
        pw.print(info.widgetCategory);
        pw.print(" autoAdvanceViewId=");
        pw.print(info.autoAdvanceViewId);
        pw.print(" initialLayout=#");
        pw.print(java.lang.Integer.toHexString(info.initialLayout));
        pw.print(" initialKeyguardLayout=#");
        pw.print(java.lang.Integer.toHexString(info.initialKeyguardLayout));
        pw.print("   zombie=");
        pw.println(provider.zombie);
    }

    private static void dumpHost(com.android.server.appwidget.AppWidgetServiceImpl.Host host, int index, java.io.PrintWriter pw) {
        pw.print("  [");
        pw.print(index);
        pw.print("] hostId=");
        pw.println(host.id);
        pw.print("    callbacks=");
        pw.println(host.callbacks);
        pw.print("    widgets.size=");
        pw.print(host.widgets.size());
        pw.print(" zombie=");
        pw.println(host.zombie);
    }

    private static void dumpGrant(android.util.Pair<java.lang.Integer, java.lang.String> grant, int index, java.io.PrintWriter pw) {
        pw.print("  [");
        pw.print(index);
        pw.print(']');
        pw.print(" user=");
        pw.print(grant.first);
        pw.print(" package=");
        pw.println((java.lang.String) grant.second);
    }

    private static void dumpWidget(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, int index, java.io.PrintWriter pw) {
        pw.print("  [");
        pw.print(index);
        pw.print("] id=");
        pw.println(widget.appWidgetId);
        pw.print("    host=");
        pw.println(widget.host.id);
        if (widget.provider != null) {
            pw.print("    provider=");
            pw.println(widget.provider.id);
        }
        if (widget.host != null) {
            pw.print("    host.callbacks=");
            pw.println(widget.host.callbacks);
        }
        if (widget.views != null) {
            pw.print("    views=");
            pw.println(widget.views);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeProvider(com.android.modules.utils.TypedXmlSerializer out, com.android.server.appwidget.AppWidgetServiceImpl.Provider p, boolean persistsProviderInfo) throws java.io.IOException {
        java.util.Objects.requireNonNull(out);
        java.util.Objects.requireNonNull(p);
        out.startTag((java.lang.String) null, "p");
        out.attribute((java.lang.String) null, "pkg", p.id.componentName.getPackageName());
        out.attribute((java.lang.String) null, "cl", p.id.componentName.getClassName());
        out.attributeIntHex((java.lang.String) null, "tag", p.tag);
        if (!android.text.TextUtils.isEmpty(p.infoTag)) {
            out.attribute((java.lang.String) null, "info_tag", p.infoTag);
        }
        if (persistsProviderInfo && p.mInfoParsed) {
            com.android.server.appwidget.AppWidgetXmlUtil.writeAppWidgetProviderInfoLocked(out, p.info);
        }
        int pendingIdsCount = p.pendingDeletedWidgetIds.size();
        if (pendingIdsCount > 0) {
            java.util.List<java.lang.String> idStrings = new java.util.ArrayList<>();
            for (int i = 0; i < pendingIdsCount; i++) {
                idStrings.add(java.lang.String.valueOf(p.pendingDeletedWidgetIds.get(i)));
            }
            out.attribute((java.lang.String) null, PENDING_DELETED_IDS_ATTR, java.lang.String.join(",", idStrings));
        }
        out.endTag((java.lang.String) null, "p");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeHost(com.android.modules.utils.TypedXmlSerializer out, com.android.server.appwidget.AppWidgetServiceImpl.Host host) throws java.io.IOException {
        out.startTag((java.lang.String) null, "h");
        out.attribute((java.lang.String) null, "pkg", host.id.packageName);
        out.attributeIntHex((java.lang.String) null, "id", host.id.hostId);
        out.attributeIntHex((java.lang.String) null, "tag", host.tag);
        out.endTag((java.lang.String) null, "h");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeAppWidget(com.android.modules.utils.TypedXmlSerializer out, com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, boolean saveRestoreCompleted) throws java.io.IOException {
        out.startTag((java.lang.String) null, "g");
        out.attributeIntHex((java.lang.String) null, "id", widget.appWidgetId);
        out.attributeIntHex((java.lang.String) null, "rid", widget.restoredId);
        out.attributeIntHex((java.lang.String) null, "h", widget.host.tag);
        if (widget.provider != null) {
            out.attributeIntHex((java.lang.String) null, "p", widget.provider.tag);
        }
        if (widget.options != null) {
            int minWidth = widget.options.getInt("appWidgetMinWidth");
            int minHeight = widget.options.getInt("appWidgetMinHeight");
            int maxWidth = widget.options.getInt("appWidgetMaxWidth");
            int maxHeight = widget.options.getInt("appWidgetMaxHeight");
            out.attributeIntHex((java.lang.String) null, "min_width", minWidth > 0 ? minWidth : 0);
            out.attributeIntHex((java.lang.String) null, "min_height", minHeight > 0 ? minHeight : 0);
            out.attributeIntHex((java.lang.String) null, "max_width", maxWidth > 0 ? maxWidth : 0);
            out.attributeIntHex((java.lang.String) null, "max_height", maxHeight > 0 ? maxHeight : 0);
            out.attributeIntHex((java.lang.String) null, "host_category", widget.options.getInt("appWidgetCategory"));
            java.util.List<android.util.SizeF> sizes = widget.options.getParcelableArrayList("appWidgetSizes", android.util.SizeF.class);
            if (sizes != null) {
                out.attribute((java.lang.String) null, KEY_SIZES, com.android.server.appwidget.AppWidgetXmlUtil.serializeWidgetSizes(sizes));
            }
            if (saveRestoreCompleted) {
                boolean restoreCompleted = widget.options.getBoolean("appWidgetRestoreCompleted");
                out.attributeBoolean((java.lang.String) null, "restore_completed", restoreCompleted);
            }
        }
        out.endTag((java.lang.String) null, "g");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.os.Bundle parseWidgetIdOptions(com.android.modules.utils.TypedXmlPullParser parser) {
        android.os.Bundle options = new android.os.Bundle();
        boolean restoreCompleted = parser.getAttributeBoolean((java.lang.String) null, "restore_completed", false);
        if (restoreCompleted) {
            options.putBoolean("appWidgetRestoreCompleted", true);
        }
        int minWidth = parser.getAttributeIntHex((java.lang.String) null, "min_width", -1);
        if (minWidth != -1) {
            options.putInt("appWidgetMinWidth", minWidth);
        }
        int minHeight = parser.getAttributeIntHex((java.lang.String) null, "min_height", -1);
        if (minHeight != -1) {
            options.putInt("appWidgetMinHeight", minHeight);
        }
        int maxWidth = parser.getAttributeIntHex((java.lang.String) null, "max_width", -1);
        if (maxWidth != -1) {
            options.putInt("appWidgetMaxWidth", maxWidth);
        }
        int maxHeight = parser.getAttributeIntHex((java.lang.String) null, "max_height", -1);
        if (maxHeight != -1) {
            options.putInt("appWidgetMaxHeight", maxHeight);
        }
        java.lang.String sizesStr = parser.getAttributeValue((java.lang.String) null, KEY_SIZES);
        java.util.ArrayList<android.util.SizeF> sizes = com.android.server.appwidget.AppWidgetXmlUtil.deserializeWidgetSizesStr(sizesStr);
        if (sizes != null) {
            options.putParcelableArrayList("appWidgetSizes", sizes);
        }
        int category = parser.getAttributeIntHex((java.lang.String) null, "host_category", -1);
        if (category != -1) {
            options.putInt("appWidgetCategory", category);
        }
        return options;
    }

    public java.util.List<java.lang.String> getWidgetParticipants(int userId) {
        return this.mBackupRestoreController.getWidgetParticipants(userId);
    }

    public byte[] getWidgetState(java.lang.String packageName, int userId) {
        return this.mBackupRestoreController.getWidgetState(packageName, userId);
    }

    public void systemRestoreStarting(int userId) {
        this.mBackupRestoreController.systemRestoreStarting(userId);
    }

    public void restoreWidgetState(java.lang.String packageName, byte[] restoredState, int userId) throws java.lang.Throwable {
        this.mBackupRestoreController.restoreWidgetState(packageName, restoredState, userId);
    }

    public void systemRestoreFinished(int userId) {
        this.mBackupRestoreController.systemRestoreFinished(userId);
    }

    private android.appwidget.AppWidgetProviderInfo createPartialProviderInfo(com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId, android.content.pm.ResolveInfo ri, com.android.server.appwidget.AppWidgetServiceImpl.Provider provider) {
        boolean hasXmlDefinition = false;
        android.os.Bundle metaData = ri.activityInfo.metaData;
        if (metaData == null) {
            return null;
        }
        if (provider != null && !android.text.TextUtils.isEmpty(provider.infoTag)) {
            hasXmlDefinition = metaData.getInt(provider.infoTag) != 0;
        }
        if (!(hasXmlDefinition | (metaData.getInt("android.appwidget.provider") != 0))) {
            return null;
        }
        android.appwidget.AppWidgetProviderInfo info = new android.appwidget.AppWidgetProviderInfo();
        info.provider = providerId.componentName;
        info.providerInfo = ri.activityInfo;
        if (DEBUG) {
            java.util.Objects.requireNonNull(ri.activityInfo);
        }
        return info;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.appwidget.AppWidgetProviderInfo parseAppWidgetProviderInfo(android.content.Context context, com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId, android.content.pm.ActivityInfo activityInfo, java.lang.String metadataKey) {
        int type;
        android.content.pm.PackageManager pm = context.getPackageManager();
        try {
            android.content.res.XmlResourceParser parser = activityInfo.loadXmlMetaData(pm, metadataKey);
            try {
                if (parser == null) {
                    android.util.Slog.w(TAG, "No " + metadataKey + " meta-data for AppWidget provider '" + providerId + '\'');
                    if (parser != null) {
                        parser.close();
                    }
                    return null;
                }
                android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
                do {
                    type = parser.next();
                    if (type == 1) {
                        break;
                    }
                } while (type != 2);
                java.lang.String nodeName = parser.getName();
                if (!"appwidget-provider".equals(nodeName)) {
                    android.util.Slog.w(TAG, "Meta-data does not start with appwidget-provider tag for AppWidget provider " + providerId.componentName + " for user " + providerId.uid);
                    if (parser != null) {
                        parser.close();
                    }
                    return null;
                }
                android.appwidget.AppWidgetProviderInfo info = new android.appwidget.AppWidgetProviderInfo();
                info.provider = providerId.componentName;
                info.providerInfo = activityInfo;
                if (DEBUG) {
                    java.util.Objects.requireNonNull(activityInfo);
                }
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    int userId = android.os.UserHandle.getUserId(providerId.uid);
                    android.content.pm.ApplicationInfo app = pm.getApplicationInfoAsUser(activityInfo.packageName, 0, userId);
                    android.content.res.Resources resources = pm.getResourcesForApplication(app);
                    android.os.Binder.restoreCallingIdentity(identity);
                    android.content.res.TypedArray sa = resources.obtainAttributes(attrs, com.android.internal.R.styleable.AppWidgetProviderInfo);
                    android.util.TypedValue value = sa.peekValue(1);
                    info.minWidth = value != null ? value.data : 0;
                    android.util.TypedValue value2 = sa.peekValue(2);
                    info.minHeight = value2 != null ? value2.data : 0;
                    android.util.TypedValue value3 = sa.peekValue(9);
                    info.minResizeWidth = value3 != null ? value3.data : info.minWidth;
                    android.util.TypedValue value4 = sa.peekValue(10);
                    info.minResizeHeight = value4 != null ? value4.data : info.minHeight;
                    android.util.TypedValue value5 = sa.peekValue(15);
                    info.maxResizeWidth = value5 != null ? value5.data : 0;
                    android.util.TypedValue value6 = sa.peekValue(16);
                    info.maxResizeHeight = value6 != null ? value6.data : 0;
                    info.targetCellWidth = sa.getInt(17, 0);
                    info.targetCellHeight = sa.getInt(18, 0);
                    info.updatePeriodMillis = sa.getInt(3, 0);
                    info.initialLayout = sa.getResourceId(4, 0);
                    info.initialKeyguardLayout = sa.getResourceId(11, 0);
                    java.lang.String className = sa.getString(5);
                    if (className != null) {
                        info.configure = new android.content.ComponentName(providerId.componentName.getPackageName(), className);
                    }
                    info.label = activityInfo.loadLabel(pm).toString();
                    info.icon = activityInfo.getIconResource();
                    info.previewImage = sa.getResourceId(6, 0);
                    info.previewLayout = sa.getResourceId(14, 0);
                    info.autoAdvanceViewId = sa.getResourceId(7, -1);
                    info.resizeMode = sa.getInt(8, 0);
                    info.widgetCategory = sa.getInt(12, 1);
                    info.widgetFeatures = sa.getInt(13, 0);
                    info.descriptionRes = sa.getResourceId(0, 0);
                    sa.recycle();
                    if (parser != null) {
                        parser.close();
                    }
                    return info;
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            } finally {
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException | java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.w(TAG, "XML parsing failed for AppWidget provider " + providerId.componentName + " for user " + providerId.uid, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getUidForPackage(java.lang.String packageName, int userId) {
        android.content.pm.PackageInfo pkgInfo = null;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            pkgInfo = this.mPackageManager.getPackageInfo(packageName, 0L, userId);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(identity);
        if (pkgInfo == null || pkgInfo.applicationInfo == null) {
            return -1;
        }
        return pkgInfo.applicationInfo.uid;
    }

    private android.content.pm.ActivityInfo getProviderInfo(android.content.ComponentName componentName, int userId) {
        android.content.Intent intent = new android.content.Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setComponent(componentName);
        java.util.List<android.content.pm.ResolveInfo> receivers = queryIntentReceivers(intent, userId);
        if (!receivers.isEmpty()) {
            return receivers.get(0).activityInfo;
        }
        return null;
    }

    private java.util.List<android.content.pm.ResolveInfo> queryIntentReceivers(android.content.Intent intent, int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int flags = this.mAppWidgetServiceExt.hookqueryIntent(128) | 268435456;
            if (isProfileWithUnlockedParent(userId)) {
                flags |= com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED;
            }
            return this.mPackageManager.queryIntentReceivers(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), flags | 1024, userId).getList();
        } catch (android.os.RemoteException e) {
            return java.util.Collections.emptyList();
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    void handleUserUnlocked(int userId) {
        android.util.Slog.d(TAG, "handleUserUnlocked");
        if (isProfileWithLockedParent(userId)) {
            android.util.Slog.w(TAG, "isProfileWithLockedParent, User:" + userId);
            return;
        }
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            android.util.Slog.w(TAG, "User " + userId + " is no longer unlocked - exiting");
            return;
        }
        long time = android.os.SystemClock.elapsedRealtime();
        synchronized (this.mLock) {
            android.os.Trace.traceBegin(64L, "appwidget ensure");
            ensureGroupStateLoadedLocked(userId);
            android.os.Trace.traceEnd(64L);
            android.os.Trace.traceBegin(64L, "appwidget reload");
            reloadWidgetsMaskedStateForGroup(this.mSecurityPolicy.getGroupParent(userId));
            android.os.Trace.traceEnd(64L);
            android.util.Slog.d(TAG, "User " + userId + " mProviders size:" + this.mProviders.size());
            int N = this.mProviders.size();
            for (int i = 0; i < N; i++) {
                final com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
                if (provider.getUserId() == userId && provider.widgets.size() > 0 && !provider.maskedByStoppedPackage) {
                    android.os.Trace.traceBegin(64L, "appwidget init " + provider.id.componentName.getPackageName());
                    provider.widgets.forEach(new java.util.function.Consumer() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.appwidget.AppWidgetServiceImpl.lambda$handleUserUnlocked$2(provider, (com.android.server.appwidget.AppWidgetServiceImpl.Widget) obj);
                        }
                    });
                    int[] appWidgetIds = getWidgetIds(provider.widgets);
                    sendEnableAndUpdateIntentLocked(provider, appWidgetIds);
                    registerForBroadcastsLocked(provider, appWidgetIds);
                    android.os.Trace.traceEnd(64L);
                }
            }
        }
        android.util.Slog.i(TAG, "Processing of handleUserUnlocked u" + userId + " took " + (android.os.SystemClock.elapsedRealtime() - time) + " ms");
    }

    static /* synthetic */ void lambda$handleUserUnlocked$2(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        widget.trackingUpdate = true;
        android.os.Trace.asyncTraceBegin(64L, "appwidget update-intent " + provider.id.toString(), widget.appWidgetId);
        android.util.Log.i(TAG, "Widget update scheduled on unlock " + widget.toString());
    }

    private void loadGroupStateLocked(int[] profileIds) throws java.lang.Throwable {
        java.util.List<com.android.server.appwidget.AppWidgetServiceImpl.LoadedWidgetState> loadedWidgets = new java.util.ArrayList<>();
        int version = 0;
        for (int profileId : profileIds) {
            android.util.AtomicFile file = getSavedStateFile(profileId);
            try {
                java.io.FileInputStream stream = file.openRead();
                try {
                    version = readProfileStateFromFileLocked(stream, profileId, loadedWidgets);
                    if (stream != null) {
                        stream.close();
                    }
                } catch (java.lang.Throwable th) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Failed to read state: " + e);
            }
        }
        if (version >= 0) {
            bindLoadedWidgetsLocked(loadedWidgets);
            performUpgradeLocked(version);
            return;
        }
        android.util.Slog.w(TAG, "Failed to read state, clearing widgets and hosts.");
        clearWidgetsLocked();
        this.mHosts.clear();
        int N = this.mProviders.size();
        for (int i = 0; i < N; i++) {
            this.mProviders.get(i).widgets.clear();
        }
    }

    private void bindLoadedWidgetsLocked(java.util.List<com.android.server.appwidget.AppWidgetServiceImpl.LoadedWidgetState> loadedWidgets) throws java.lang.Throwable {
        int loadedWidgetCount = loadedWidgets.size();
        for (int i = loadedWidgetCount - 1; i >= 0; i--) {
            com.android.server.appwidget.AppWidgetServiceImpl.LoadedWidgetState loadedWidget = loadedWidgets.remove(i);
            com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = loadedWidget.widget;
            widget.provider = findProviderByTag(loadedWidget.providerTag);
            if (widget.provider == null) {
                android.util.Slog.i(TAG, "bindLoadedWidgetsLocked, widget.provider = null, appWidgetId:" + widget.appWidgetId);
            } else {
                widget.host = findHostByTag(loadedWidget.hostTag);
                if (widget.host == null) {
                    android.util.Slog.i(TAG, "bindLoadedWidgetsLocked, widget.host = null, appWidgetId:" + widget.appWidgetId);
                } else {
                    widget.provider.widgets.add(widget);
                    widget.host.widgets.add(widget);
                    addWidgetLocked(widget);
                    if (widget != null && widget.provider != null && widget.provider.info != null && widget.provider.info.provider != null && widget.provider.id != null) {
                        this.mAppWidgetServiceExt.hookUpdateWidgetSate(widget.provider.id.uid, widget.provider.info.provider.getPackageName(), true);
                    }
                }
            }
        }
    }

    private com.android.server.appwidget.AppWidgetServiceImpl.Provider findProviderByTag(int tag) {
        if (tag < 0) {
            return null;
        }
        int providerCount = this.mProviders.size();
        for (int i = 0; i < providerCount; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            if (provider.tag == tag) {
                return provider;
            }
        }
        return null;
    }

    private com.android.server.appwidget.AppWidgetServiceImpl.Host findHostByTag(int tag) {
        if (tag < 0) {
            return null;
        }
        int hostCount = this.mHosts.size();
        for (int i = 0; i < hostCount; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i);
            if (host.tag == tag) {
                return host;
            }
        }
        return null;
    }

    void addWidgetLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) throws java.lang.Throwable {
        if (DEBUG) {
            android.util.Slog.i(TAG, "addWidgetLocked() " + widget);
        }
        ensureWidgetCountBeforeAddLocked(widget);
        this.mWidgets.add(widget);
        onWidgetProviderAddedOrChangedLocked(widget);
    }

    private void ensureWidgetCountBeforeAddLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        if (widget.host == null || widget.host.id == null) {
            return;
        }
        java.util.List<com.android.server.appwidget.AppWidgetServiceImpl.Widget> widgetsInSameHost = new java.util.ArrayList<>();
        for (com.android.server.appwidget.AppWidgetServiceImpl.Widget w : this.mWidgets) {
            if (w.host != null && widget.host.id.equals(w.host.id)) {
                widgetsInSameHost.add(w);
            }
        }
        while (widgetsInSameHost.size() >= 200) {
            removeWidgetLocked(widgetsInSameHost.remove(0));
        }
    }

    void onWidgetProviderAddedOrChangedLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) throws java.lang.Throwable {
        if (widget.provider == null) {
            return;
        }
        int userId = widget.provider.getUserId();
        synchronized (this.mWidgetPackagesLock) {
            android.util.ArraySet<java.lang.String> packages = this.mWidgetPackages.get(userId);
            if (packages == null) {
                android.util.SparseArray<android.util.ArraySet<java.lang.String>> sparseArray = this.mWidgetPackages;
                android.util.ArraySet<java.lang.String> arraySet = new android.util.ArraySet<>();
                packages = arraySet;
                sparseArray.put(userId, arraySet);
            }
            packages.add(widget.provider.id.componentName.getPackageName());
        }
        if (widget.provider.isMaskedLocked()) {
            maskWidgetsViewsLocked(widget.provider, widget);
        } else {
            widget.clearMaskedViewsLocked();
        }
        this.mAppWidgetServiceExt.notifyOnWidgetProviderAddedOrChangedLocked(widget.hashCode(), widget.provider.id.uid, widget.provider.info.provider.getPackageName(), true);
    }

    void removeWidgetLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "removeWidgetLocked() " + widget);
        }
        this.mWidgets.remove(widget);
        onWidgetRemovedLocked(widget);
        scheduleNotifyAppWidgetRemovedLocked(widget);
        if (widget != null && widget.provider != null && widget.provider.info != null && widget.provider.info.provider != null && widget.provider.id != null) {
            this.mAppWidgetServiceExt.notifyRemoveAppWidget(widget.hashCode(), widget.provider.info.provider.getPackageName(), widget.provider.id.uid);
        }
        if (widget != null && widget.provider != null && widget.provider.info != null && widget.provider.info.provider != null && widget.provider.id != null) {
            this.mAppWidgetServiceExt.hookUpdateWidgetSate(widget.provider.id.uid, widget.provider.info.provider.getPackageName(), false);
        }
    }

    private void onWidgetRemovedLocked(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget) {
        if (widget.provider == null) {
            return;
        }
        int userId = widget.provider.getUserId();
        java.lang.String packageName = widget.provider.id.componentName.getPackageName();
        synchronized (this.mWidgetPackagesLock) {
            android.util.ArraySet<java.lang.String> packages = this.mWidgetPackages.get(userId);
            if (packages == null) {
                return;
            }
            int N = this.mWidgets.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget w = this.mWidgets.get(i);
                if (w.provider != null && w.provider.getUserId() == userId && packageName.equals(w.provider.id.componentName.getPackageName())) {
                    return;
                }
            }
            packages.remove(packageName);
        }
    }

    void clearWidgetsLocked() {
        if (DEBUG) {
            android.util.Slog.i(TAG, "clearWidgetsLocked()");
        }
        this.mWidgets.clear();
        onWidgetsClearedLocked();
        this.mAppWidgetServiceExt.notifyClearWidgetsLocked();
    }

    private void onWidgetsClearedLocked() {
        synchronized (this.mWidgetPackagesLock) {
            this.mWidgetPackages.clear();
        }
    }

    public boolean isBoundWidgetPackage(java.lang.String packageName, int userId) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only the system process can call this");
        }
        synchronized (this.mWidgetPackagesLock) {
            android.util.ArraySet<java.lang.String> packages = this.mWidgetPackages.get(userId);
            if (packages != null) {
                return packages.contains(packageName);
            }
            return false;
        }
    }

    private android.util.SparseArray<byte[]> saveStateToByteArrayLocked(int userId) {
        tagProvidersAndHosts();
        int[] profileIds = this.mSecurityPolicy.getEnabledGroupProfileIds(userId);
        android.util.SparseArray<byte[]> userIdToBytesMapping = new android.util.SparseArray<>();
        for (int profileId : profileIds) {
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            if (writeProfileStateToStreamLocked(outputStream, profileId)) {
                userIdToBytesMapping.put(profileId, outputStream.toByteArray());
            }
        }
        return userIdToBytesMapping;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveStateLocked(int userId) {
        if (this.mSafeMode) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "do not saveStateLocked in safemode ");
                return;
            }
            return;
        }
        tagProvidersAndHosts();
        int[] profileIds = this.mSecurityPolicy.getEnabledGroupProfileIds(userId);
        for (int profileId : profileIds) {
            android.util.AtomicFile file = getSavedStateFile(profileId);
            try {
                java.io.FileOutputStream stream = file.startWrite();
                if (writeProfileStateToStreamLocked(stream, profileId)) {
                    file.finishWrite(stream);
                } else {
                    file.failWrite(stream);
                    android.util.Slog.w(TAG, "Failed to save state, restoring backup.");
                }
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Failed open state file for write: " + e);
            }
        }
    }

    private void tagProvidersAndHosts() {
        int providerCount = this.mProviders.size();
        for (int i = 0; i < providerCount; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            provider.tag = i;
        }
        int hostCount = this.mHosts.size();
        for (int i2 = 0; i2 < hostCount; i2++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i2);
            host.tag = i2;
        }
    }

    private void clearProvidersAndHostsTagsLocked() {
        int providerCount = this.mProviders.size();
        for (int i = 0; i < providerCount; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            provider.tag = -1;
        }
        int hostCount = this.mHosts.size();
        for (int i2 = 0; i2 < hostCount; i2++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i2);
            host.tag = -1;
        }
    }

    private boolean writeProfileStateToStreamLocked(java.io.OutputStream stream, int userId) {
        try {
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, "gs");
            out.attributeInt((java.lang.String) null, "version", 1);
            int N = this.mProviders.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
                if (provider.getUserId() == userId) {
                    serializeProvider(out, provider, true);
                }
            }
            int N2 = this.mHosts.size();
            for (int i2 = 0; i2 < N2; i2++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i2);
                if (host.getUserId() == userId) {
                    serializeHost(out, host);
                }
            }
            int N3 = this.mWidgets.size();
            for (int i3 = 0; i3 < N3; i3++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = this.mWidgets.get(i3);
                if (widget.host.getUserId() == userId) {
                    serializeAppWidget(out, widget, true);
                }
            }
            for (android.util.Pair<java.lang.Integer, java.lang.String> binding : this.mPackagesWithBindWidgetPermission) {
                if (((java.lang.Integer) binding.first).intValue() == userId) {
                    out.startTag((java.lang.String) null, "b");
                    out.attribute((java.lang.String) null, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, (java.lang.String) binding.second);
                    out.endTag((java.lang.String) null, "b");
                }
            }
            out.endTag((java.lang.String) null, "gs");
            out.endDocument();
            return true;
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Failed to write state: " + e);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x024b A[LOOP:0: B:117:0x0013->B:101:0x024b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x024a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0153  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int readProfileStateFromFileLocked(java.io.FileInputStream r27, int r28, java.util.List<com.android.server.appwidget.AppWidgetServiceImpl.LoadedWidgetState> r29) {
        /*
            Method dump skipped, instruction units count: 625
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appwidget.AppWidgetServiceImpl.readProfileStateFromFileLocked(java.io.FileInputStream, int, java.util.List):int");
    }

    private void performUpgradeLocked(int fromVersion) {
        int uid;
        if (fromVersion < 1) {
            android.util.Slog.v(TAG, "Upgrading widget database from " + fromVersion + " to 1");
        }
        int version = fromVersion;
        if (version == 0) {
            com.android.server.appwidget.AppWidgetServiceImpl.HostId oldHostId = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(android.os.Process.myUid(), KEYGUARD_HOST_ID, "android");
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = lookupHostLocked(oldHostId);
            if (host != null && (uid = getUidForPackage(NEW_KEYGUARD_HOST_PACKAGE, 0)) >= 0) {
                host.id = new com.android.server.appwidget.AppWidgetServiceImpl.HostId(uid, KEYGUARD_HOST_ID, NEW_KEYGUARD_HOST_PACKAGE);
            }
            version = 1;
        }
        if (version != 1) {
            throw new java.lang.IllegalStateException("Failed to upgrade widget database");
        }
    }

    private static java.io.File getStateFile(int userId) {
        return new java.io.File(android.os.Environment.getUserSystemDirectory(userId), STATE_FILENAME);
    }

    private static android.util.AtomicFile getSavedStateFile(int userId) {
        java.io.File dir = android.os.Environment.getUserSystemDirectory(userId);
        java.io.File settingsFile = getStateFile(userId);
        if (!settingsFile.exists() && userId == 0) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            java.io.File oldFile = new java.io.File("/data/system/appwidgets.xml");
            oldFile.renameTo(settingsFile);
        }
        return new android.util.AtomicFile(settingsFile);
    }

    void onUserStopped(int userId) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "onUserStopped() " + userId);
        }
        synchronized (this.mLock) {
            boolean crossProfileWidgetsChanged = false;
            int widgetCount = this.mWidgets.size();
            int i = widgetCount - 1;
            while (true) {
                boolean providerInUser = false;
                if (i < 0) {
                    break;
                }
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = this.mWidgets.get(i);
                boolean hostInUser = widget.host.getUserId() == userId;
                boolean hasProvider = widget.provider != null;
                if (hasProvider && widget.provider.getUserId() == userId) {
                    providerInUser = true;
                }
                if (hostInUser && (!hasProvider || providerInUser)) {
                    removeWidgetLocked(widget);
                    widget.host.widgets.remove(widget);
                    widget.host = null;
                    if (hasProvider) {
                        widget.provider.widgets.remove(widget);
                        widget.provider = null;
                    }
                }
                i--;
            }
            int hostCount = this.mHosts.size();
            for (int i2 = hostCount - 1; i2 >= 0; i2--) {
                com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i2);
                if (host.getUserId() == userId) {
                    crossProfileWidgetsChanged |= !host.widgets.isEmpty();
                    deleteHostLocked(host);
                }
            }
            int grantCount = this.mPackagesWithBindWidgetPermission.size();
            for (int i3 = grantCount - 1; i3 >= 0; i3--) {
                android.util.Pair<java.lang.Integer, java.lang.String> packageId = this.mPackagesWithBindWidgetPermission.valueAt(i3);
                if (((java.lang.Integer) packageId.first).intValue() == userId) {
                    this.mPackagesWithBindWidgetPermission.removeAt(i3);
                }
            }
            int userIndex = this.mLoadedUserIds.indexOfKey(userId);
            if (userIndex >= 0) {
                this.mLoadedUserIds.removeAt(userIndex);
            }
            int nextIdIndex = this.mNextAppWidgetIds.indexOfKey(userId);
            if (nextIdIndex >= 0) {
                this.mNextAppWidgetIds.removeAt(nextIdIndex);
            }
            if (crossProfileWidgetsChanged) {
                saveGroupStateAsync(userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyResourceOverlaysToWidgetsLocked(java.util.Set<java.lang.String> packageNames, int userId, boolean updateFrameworkRes) {
        int N = this.mProviders.size();
        for (int i = 0; i < N; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            if (provider.getUserId() == userId) {
                java.lang.String packageName = provider.id.componentName.getPackageName();
                if (updateFrameworkRes || packageNames.contains(packageName)) {
                    android.content.pm.ApplicationInfo newAppInfo = null;
                    try {
                        newAppInfo = this.mPackageManager.getApplicationInfo(packageName, 1024L, userId);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Failed to retrieve app info for " + packageName + " userId=" + userId, e);
                    }
                    if (newAppInfo != null && provider.info != null && provider.info.providerInfo != null) {
                        if (newAppInfo.overlayPaths == null) {
                            android.util.Slog.w(TAG, "newAppInfo.overlayPaths is null, so return");
                        } else if (newAppInfo.resourceDirs == null) {
                            android.util.Slog.w(TAG, "newAppInfo.resourceDirs is null, so return");
                        } else {
                            android.content.pm.ApplicationInfo oldAppInfo = provider.info.providerInfo.applicationInfo;
                            if (oldAppInfo != null && newAppInfo.sourceDir.equals(oldAppInfo.sourceDir)) {
                                android.content.pm.ApplicationInfo oldAppInfo2 = new android.content.pm.ApplicationInfo(oldAppInfo);
                                oldAppInfo2.overlayPaths = newAppInfo.overlayPaths == null ? null : (java.lang.String[]) newAppInfo.overlayPaths.clone();
                                oldAppInfo2.resourceDirs = newAppInfo.resourceDirs != null ? (java.lang.String[]) newAppInfo.resourceDirs.clone() : null;
                                provider.info.providerInfo.applicationInfo = oldAppInfo2;
                                int M = provider.widgets.size();
                                for (int j = 0; j < M; j++) {
                                    com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = provider.widgets.get(j);
                                    if (widget.views != null) {
                                        widget.views.updateAppInfo(oldAppInfo2);
                                    }
                                    if (widget.maskedViews != null) {
                                        widget.maskedViews.updateAppInfo(oldAppInfo2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean updateProvidersForPackageLocked(java.lang.String packageName, int userId, java.util.Set<com.android.server.appwidget.AppWidgetServiceImpl.ProviderId> removedProviders) {
        boolean providersUpdated;
        android.content.Intent intent;
        java.util.List<android.content.pm.ResolveInfo> broadcastReceivers;
        boolean providersUpdated2;
        boolean providersUpdated3 = false;
        java.util.HashSet<com.android.server.appwidget.AppWidgetServiceImpl.ProviderId> keep = new java.util.HashSet<>();
        android.content.Intent intent2 = new android.content.Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent2.setPackage(packageName);
        java.util.List<android.content.pm.ResolveInfo> broadcastReceivers2 = queryIntentReceivers(intent2, userId);
        int N = broadcastReceivers2 == null ? 0 : broadcastReceivers2.size();
        int i = 0;
        while (i < N) {
            android.content.pm.ResolveInfo ri = broadcastReceivers2.get(i);
            android.content.pm.ActivityInfo ai = ri.activityInfo;
            if ((ai.applicationInfo.flags & 262144) != 0 || !packageName.equals(ai.packageName)) {
                providersUpdated = providersUpdated3;
                intent = intent2;
                broadcastReceivers = broadcastReceivers2;
            } else {
                providersUpdated = providersUpdated3;
                com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(ai.applicationInfo.uid, new android.content.ComponentName(ai.packageName, ai.name));
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
                if (provider == null) {
                    if (!addProviderLocked(ri)) {
                        intent = intent2;
                        broadcastReceivers = broadcastReceivers2;
                    } else {
                        keep.add(providerId);
                        providersUpdated2 = true;
                        intent = intent2;
                        broadcastReceivers = broadcastReceivers2;
                    }
                } else {
                    android.appwidget.AppWidgetProviderInfo info = createPartialProviderInfo(providerId, ri, provider);
                    if (info != null) {
                        keep.add(providerId);
                        provider.setPartialInfoLocked(info);
                        int M = provider.widgets.size();
                        if (M > 0) {
                            int[] appWidgetIds = getWidgetIds(provider.widgets);
                            cancelBroadcastsLocked(provider);
                            registerForBroadcastsLocked(provider, appWidgetIds);
                            int j = 0;
                            while (j < M) {
                                android.content.Intent intent3 = intent2;
                                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = provider.widgets.get(j);
                                widget.views = null;
                                scheduleNotifyProviderChangedLocked(widget);
                                j++;
                                intent2 = intent3;
                                broadcastReceivers2 = broadcastReceivers2;
                            }
                            intent = intent2;
                            broadcastReceivers = broadcastReceivers2;
                            sendUpdateIntentLocked(provider, appWidgetIds, false);
                        } else {
                            intent = intent2;
                            broadcastReceivers = broadcastReceivers2;
                        }
                    } else {
                        intent = intent2;
                        broadcastReceivers = broadcastReceivers2;
                    }
                    providersUpdated2 = true;
                }
                i++;
                providersUpdated3 = providersUpdated2;
                intent2 = intent;
                broadcastReceivers2 = broadcastReceivers;
            }
            providersUpdated2 = providersUpdated;
            i++;
            providersUpdated3 = providersUpdated2;
            intent2 = intent;
            broadcastReceivers2 = broadcastReceivers;
        }
        boolean providersUpdated4 = providersUpdated3;
        int N2 = this.mProviders.size();
        for (int i2 = N2 - 1; i2 >= 0; i2--) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider2 = this.mProviders.get(i2);
            if (packageName.equals(provider2.id.componentName.getPackageName()) && provider2.getUserId() == userId && !keep.contains(provider2.id)) {
                if (removedProviders != null) {
                    removedProviders.add(provider2.id);
                }
                deleteProviderLocked(provider2);
                providersUpdated4 = true;
            }
        }
        return providersUpdated4;
    }

    private void removeWidgetsForPackageLocked(java.lang.String pkgName, int userId, int parentUserId) {
        int N = this.mProviders.size();
        for (int i = 0; i < N; i++) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            if (pkgName.equals(provider.id.componentName.getPackageName()) && provider.getUserId() == userId && provider.widgets.size() > 0) {
                deleteWidgetsLocked(provider, parentUserId);
            }
        }
    }

    private boolean removeProvidersForPackageLocked(java.lang.String pkgName, int userId) {
        boolean removed = false;
        int N = this.mProviders.size();
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
            if (pkgName.equals(provider.id.componentName.getPackageName()) && provider.getUserId() == userId) {
                deleteProviderLocked(provider);
                removed = true;
            }
        }
        return removed;
    }

    private boolean removeHostsAndProvidersForPackageLocked(java.lang.String pkgName, int userId) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "removeHostsAndProvidersForPackageLocked() pkg=" + pkgName + " userId=" + userId);
        }
        boolean removed = removeProvidersForPackageLocked(pkgName, userId);
        int N = this.mHosts.size();
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.appwidget.AppWidgetServiceImpl.Host host = this.mHosts.get(i);
            if (pkgName.equals(host.id.packageName) && host.getUserId() == userId) {
                deleteHostLocked(host);
                removed = true;
            }
        }
        return removed;
    }

    private java.lang.String getCanonicalPackageName(java.lang.String packageName, java.lang.String className, int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.app.AppGlobals.getPackageManager().getReceiverInfo(new android.content.ComponentName(packageName, className), 0L, userId);
            return packageName;
        } catch (android.os.RemoteException e) {
            java.lang.String[] packageNames = this.mContext.getPackageManager().currentToCanonicalPackageNames(new java.lang.String[]{packageName});
            if (packageNames != null && packageNames.length > 0) {
                return packageNames[0];
            }
            android.os.Binder.restoreCallingIdentity(identity);
            return null;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle userHandle, boolean isInteractive) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.sendBroadcastAsUser(intent, userHandle, null, isInteractive ? this.mInteractiveBroadcast : null);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void bindService(android.content.Intent intent, android.content.ServiceConnection connection, android.os.UserHandle userHandle) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.bindServiceAsUser(intent, connection, 33554433, userHandle);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void unbindService(android.content.ServiceConnection connection) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.unbindService(connection);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void onCrossProfileWidgetProvidersChanged(int userId, java.util.List<java.lang.String> packages) {
        int parentId = this.mSecurityPolicy.getProfileParent(userId);
        if (parentId != userId) {
            synchronized (this.mLock) {
                boolean providersChanged = false;
                android.util.ArraySet<java.lang.String> previousPackages = new android.util.ArraySet<>();
                int providerCount = this.mProviders.size();
                for (int i = 0; i < providerCount; i++) {
                    com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.mProviders.get(i);
                    if (provider.getUserId() == userId) {
                        previousPackages.add(provider.id.componentName.getPackageName());
                    }
                }
                int packageCount = packages.size();
                for (int i2 = 0; i2 < packageCount; i2++) {
                    java.lang.String packageName = packages.get(i2);
                    previousPackages.remove(packageName);
                    providersChanged |= updateProvidersForPackageLocked(packageName, userId, null);
                }
                int removedCount = previousPackages.size();
                for (int i3 = 0; i3 < removedCount; i3++) {
                    removeWidgetsForPackageLocked(previousPackages.valueAt(i3), userId, parentId);
                }
                if (providersChanged || removedCount > 0) {
                    saveGroupStateAsync(userId);
                    scheduleNotifyGroupHostsForProvidersChangedLocked(userId);
                }
            }
        }
    }

    private boolean isProfileWithLockedParent(int userId) {
        android.content.pm.UserInfo parentInfo;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
            if (userInfo != null && userInfo.isProfile() && (parentInfo = this.mUserManager.getProfileParent(userId)) != null) {
                if (!isUserRunningAndUnlocked(parentInfo.getUserHandle().getIdentifier())) {
                    android.os.Binder.restoreCallingIdentity(token);
                    return true;
                }
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private boolean isProfileWithUnlockedParent(int userId) {
        android.content.pm.UserInfo parentInfo;
        android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
        if (userInfo != null && userInfo.isProfile() && (parentInfo = this.mUserManager.getProfileParent(userId)) != null && this.mUserManager.isUserUnlockingOrUnlocked(parentInfo.getUserHandle())) {
            return true;
        }
        return false;
    }

    public void noteAppWidgetTapped(java.lang.String callingPackage, int appWidgetId) {
        this.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        int callingUid = android.os.Binder.getCallingUid();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            int procState = this.mActivityManagerInternal.getUidProcessState(callingUid);
            if (procState > 2) {
                return;
            }
            synchronized (this.mLock) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = lookupWidgetLocked(appWidgetId, callingUid, callingPackage);
                if (widget == null) {
                    return;
                }
                com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = widget.provider.id;
                java.lang.String packageName = providerId.componentName.getPackageName();
                if (packageName == null) {
                    return;
                }
                android.util.SparseArray<java.lang.String> uid2PackageName = new android.util.SparseArray<>();
                uid2PackageName.put(providerId.uid, packageName);
                this.mAppOpsManagerInternal.updateAppWidgetVisibility(uid2PackageName, true);
                reportWidgetInteractionEvent(packageName, android.os.UserHandle.getUserId(providerId.uid), "tap");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void reportWidgetInteractionEvent(java.lang.String packageName, int userId, java.lang.String action) {
        if (android.app.usage.Flags.userInteractionTypeApi()) {
            android.os.PersistableBundle extras = new android.os.PersistableBundle();
            extras.putString("android.app.usage.extra.EVENT_CATEGORY", "android.appwidget");
            extras.putString("android.app.usage.extra.EVENT_ACTION", action);
            this.mUsageStatsManagerInternal.reportUserInteractionEvent(packageName, userId, extras);
            return;
        }
        this.mUsageStatsManagerInternal.reportEvent(packageName, userId, 7);
    }

    public android.widget.RemoteViews getWidgetPreview(java.lang.String callingPackage, android.content.ComponentName providerComponent, int profileId, int widgetCategory) throws java.lang.Throwable {
        int providerCount;
        com.android.server.appwidget.AppWidgetServiceImpl appWidgetServiceImpl = this;
        int i = profileId;
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "getWidgetPreview() " + callingUserId);
        }
        appWidgetServiceImpl.mSecurityPolicy.enforceCallFromPackage(callingPackage);
        ensureWidgetCategoryCombinationIsValid(widgetCategory);
        synchronized (appWidgetServiceImpl.mLock) {
            try {
                appWidgetServiceImpl.ensureGroupStateLoadedLocked(i);
                int i2 = 0;
                for (int providerCount2 = appWidgetServiceImpl.mProviders.size(); i2 < providerCount2; providerCount2 = providerCount) {
                    com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = appWidgetServiceImpl.mProviders.get(i2);
                    android.content.ComponentName componentName = provider.id.componentName;
                    if (provider.zombie) {
                        providerCount = providerCount2;
                    } else {
                        try {
                            if (providerComponent.equals(componentName)) {
                                android.appwidget.AppWidgetProviderInfo info = provider.getInfoLocked(appWidgetServiceImpl.mContext);
                                int providerProfileId = info.getProfile().getIdentifier();
                                if (providerProfileId != i) {
                                    providerCount = providerCount2;
                                } else {
                                    int callingUid = android.os.Binder.getCallingUid();
                                    java.lang.String providerPackageName = componentName.getPackageName();
                                    boolean providerIsInCallerProfile = appWidgetServiceImpl.mSecurityPolicy.isProviderInCallerOrInProfileAndWhitelListed(providerPackageName, providerProfileId);
                                    boolean shouldFilterAppAccess = appWidgetServiceImpl.mPackageManagerInternal.filterAppAccess(providerPackageName, callingUid, providerProfileId);
                                    providerCount = providerCount2;
                                    boolean providerIsInCallerPackage = appWidgetServiceImpl.mSecurityPolicy.isProviderInPackageForUid(provider, callingUid, callingPackage);
                                    boolean hasBindAppWidgetPermission = appWidgetServiceImpl.mSecurityPolicy.hasCallerBindPermissionOrBindWhiteListedLocked(callingPackage);
                                    if (providerIsInCallerProfile && !shouldFilterAppAccess) {
                                        if (providerIsInCallerPackage || hasBindAppWidgetPermission) {
                                            return provider.getGeneratedPreviewLocked(widgetCategory);
                                        }
                                    }
                                }
                            } else {
                                providerCount = providerCount2;
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    try {
                        i2++;
                        appWidgetServiceImpl = this;
                        i = profileId;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                return null;
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public boolean setWidgetPreview(android.content.ComponentName providerComponent, int widgetCategories, android.widget.RemoteViews preview) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "setWidgetPreview() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(providerComponent.getPackageName());
        ensureWidgetCategoryCombinationIsValid(widgetCategories);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(android.os.Binder.getCallingUid(), providerComponent);
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
            if (provider == null) {
                throw new java.lang.IllegalArgumentException(providerComponent + " is not a valid AppWidget provider");
            }
            if (!this.mGeneratedPreviewsApiCounter.tryApiCall(providerId)) {
                return false;
            }
            provider.setGeneratedPreviewLocked(widgetCategories, preview);
            scheduleNotifyGroupHostsForProvidersChangedLocked(userId);
            return true;
        }
    }

    public void removeWidgetPreview(android.content.ComponentName providerComponent, int widgetCategories) {
        int userId = android.os.UserHandle.getCallingUserId();
        if (DEBUG) {
            android.util.Slog.i(TAG, "removeWidgetPreview() " + userId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(providerComponent.getPackageName());
        ensureWidgetCategoryCombinationIsValid(widgetCategories);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(android.os.Binder.getCallingUid(), providerComponent);
            com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = lookupProviderLocked(providerId);
            if (provider == null) {
                throw new java.lang.IllegalArgumentException(providerComponent + " is not a valid AppWidget provider");
            }
            boolean changed = provider.removeGeneratedPreviewLocked(widgetCategories);
            if (changed) {
                scheduleNotifyGroupHostsForProvidersChangedLocked(userId);
            }
        }
    }

    private static void ensureWidgetCategoryCombinationIsValid(int widgetCategories) {
        int invalid = ~7;
        if ((widgetCategories & invalid) != 0) {
            throw new java.lang.IllegalArgumentException(widgetCategories + " is not a valid widget category combination");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSystemUiDeviceConfigChange(android.provider.DeviceConfig.Properties properties) {
        java.util.Set<java.lang.String> changed = properties.getKeyset();
        synchronized (this.mLock) {
            if (changed.contains("generated_preview_api_reset_interval_ms")) {
                long resetIntervalMs = properties.getLong("generated_preview_api_reset_interval_ms", this.mGeneratedPreviewsApiCounter.getResetIntervalMs());
                this.mGeneratedPreviewsApiCounter.setResetIntervalMs(resetIntervalMs);
            }
            if (changed.contains("generated_preview_api_max_calls_per_interval")) {
                int maxCallsPerInterval = properties.getInt("generated_preview_api_max_calls_per_interval", this.mGeneratedPreviewsApiCounter.getMaxCallsPerInterval());
                this.mGeneratedPreviewsApiCounter.setMaxCallsPerInterval(maxCallsPerInterval);
            }
        }
    }

    private final class CallbackHandler extends android.os.Handler {
        public static final int MSG_NOTIFY_APP_WIDGET_REMOVED = 5;
        public static final int MSG_NOTIFY_PROVIDERS_CHANGED = 3;
        public static final int MSG_NOTIFY_PROVIDER_CHANGED = 2;
        public static final int MSG_NOTIFY_UPDATE_APP_WIDGET = 1;
        public static final int MSG_NOTIFY_UPDATE_APP_WIDGET_DEFERRED = 6;
        public static final int MSG_NOTIFY_VIEW_DATA_CHANGED = 4;

        public CallbackHandler(android.os.Looper looper) {
            super(looper, null, false);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 1:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) message.obj;
                    com.android.server.appwidget.AppWidgetServiceImpl.Host host = (com.android.server.appwidget.AppWidgetServiceImpl.Host) args.arg1;
                    com.android.internal.appwidget.IAppWidgetHost callbacks = (com.android.internal.appwidget.IAppWidgetHost) args.arg2;
                    android.widget.RemoteViews views = (android.widget.RemoteViews) args.arg3;
                    long requestId = ((java.lang.Long) args.arg4).longValue();
                    int appWidgetId = args.argi1;
                    args.recycle();
                    com.android.server.appwidget.AppWidgetServiceImpl.this.handleNotifyUpdateAppWidget(host, callbacks, appWidgetId, views, requestId);
                    break;
                case 2:
                    com.android.internal.os.SomeArgs args2 = (com.android.internal.os.SomeArgs) message.obj;
                    com.android.server.appwidget.AppWidgetServiceImpl.Host host2 = (com.android.server.appwidget.AppWidgetServiceImpl.Host) args2.arg1;
                    com.android.internal.appwidget.IAppWidgetHost callbacks2 = (com.android.internal.appwidget.IAppWidgetHost) args2.arg2;
                    android.appwidget.AppWidgetProviderInfo info = (android.appwidget.AppWidgetProviderInfo) args2.arg3;
                    long requestId2 = ((java.lang.Long) args2.arg4).longValue();
                    int appWidgetId2 = args2.argi1;
                    args2.recycle();
                    com.android.server.appwidget.AppWidgetServiceImpl.this.handleNotifyProviderChanged(host2, callbacks2, appWidgetId2, info, requestId2);
                    break;
                case 3:
                    com.android.internal.os.SomeArgs args3 = (com.android.internal.os.SomeArgs) message.obj;
                    com.android.server.appwidget.AppWidgetServiceImpl.Host host3 = (com.android.server.appwidget.AppWidgetServiceImpl.Host) args3.arg1;
                    com.android.internal.appwidget.IAppWidgetHost callbacks3 = (com.android.internal.appwidget.IAppWidgetHost) args3.arg2;
                    args3.recycle();
                    com.android.server.appwidget.AppWidgetServiceImpl.this.handleNotifyProvidersChanged(host3, callbacks3);
                    break;
                case 4:
                    com.android.internal.os.SomeArgs args4 = (com.android.internal.os.SomeArgs) message.obj;
                    com.android.server.appwidget.AppWidgetServiceImpl.Host host4 = (com.android.server.appwidget.AppWidgetServiceImpl.Host) args4.arg1;
                    com.android.internal.appwidget.IAppWidgetHost callbacks4 = (com.android.internal.appwidget.IAppWidgetHost) args4.arg2;
                    long requestId3 = ((java.lang.Long) args4.arg3).longValue();
                    int appWidgetId3 = args4.argi1;
                    int viewId = args4.argi2;
                    args4.recycle();
                    com.android.server.appwidget.AppWidgetServiceImpl.this.handleNotifyAppWidgetViewDataChanged(host4, callbacks4, appWidgetId3, viewId, requestId3);
                    break;
                case 5:
                    com.android.internal.os.SomeArgs args5 = (com.android.internal.os.SomeArgs) message.obj;
                    com.android.server.appwidget.AppWidgetServiceImpl.Host host5 = (com.android.server.appwidget.AppWidgetServiceImpl.Host) args5.arg1;
                    com.android.internal.appwidget.IAppWidgetHost callbacks5 = (com.android.internal.appwidget.IAppWidgetHost) args5.arg2;
                    long requestId4 = ((java.lang.Long) args5.arg3).longValue();
                    int appWidgetId4 = args5.argi1;
                    args5.recycle();
                    com.android.server.appwidget.AppWidgetServiceImpl.this.handleNotifyAppWidgetRemoved(host5, callbacks5, appWidgetId4, requestId4);
                    break;
                case 6:
                    com.android.internal.os.SomeArgs args6 = (com.android.internal.os.SomeArgs) message.obj;
                    com.android.server.appwidget.AppWidgetServiceImpl.Host host6 = (com.android.server.appwidget.AppWidgetServiceImpl.Host) args6.arg1;
                    com.android.internal.appwidget.IAppWidgetHost callbacks6 = (com.android.internal.appwidget.IAppWidgetHost) args6.arg2;
                    long requestId5 = ((java.lang.Long) args6.arg4).longValue();
                    int appWidgetId5 = args6.argi1;
                    args6.recycle();
                    com.android.server.appwidget.AppWidgetServiceImpl.this.handleNotifyUpdateAppWidgetDeferred(host6, callbacks6, appWidgetId5, requestId5);
                    break;
            }
        }
    }

    private final class SecurityPolicy {
        private SecurityPolicy() {
        }

        public boolean isEnabledGroupProfile(int profileId) {
            int parentId = android.os.UserHandle.getCallingUserId();
            return isParentOrProfile(parentId, profileId) && isProfileEnabled(profileId);
        }

        public int[] getEnabledGroupProfileIds(int userId) {
            int parentId = getGroupParent(userId);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.appwidget.AppWidgetServiceImpl.this.mUserManager.getEnabledProfileIds(parentId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void enforceServiceExistsAndRequiresBindRemoteViewsPermission(android.content.ComponentName componentName, int userId) {
            android.content.pm.ServiceInfo serviceInfo;
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                serviceInfo = com.android.server.appwidget.AppWidgetServiceImpl.this.mPackageManager.getServiceInfo(componentName, 4096L, userId);
            } catch (android.os.RemoteException e) {
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
            if (serviceInfo != null) {
                if (!"android.permission.BIND_REMOTEVIEWS".equals(serviceInfo.permission)) {
                    throw new java.lang.SecurityException("Service " + componentName + " in user " + userId + "does not require android.permission.BIND_REMOTEVIEWS");
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return;
            }
            throw new java.lang.SecurityException("Service " + componentName + " not installed for user " + userId);
        }

        public void enforceModifyAppWidgetBindPermissions(java.lang.String packageName) {
            com.android.server.appwidget.AppWidgetServiceImpl.this.mContext.enforceCallingPermission("android.permission.MODIFY_APPWIDGET_BIND_PERMISSIONS", "hasBindAppWidgetPermission packageName=" + packageName);
        }

        public boolean isCallerInstantAppLocked() {
            int callingUid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                java.lang.String[] uidPackages = com.android.server.appwidget.AppWidgetServiceImpl.this.mPackageManager.getPackagesForUid(callingUid);
                if (!com.android.internal.util.ArrayUtils.isEmpty(uidPackages)) {
                    boolean zIsInstantApp = com.android.server.appwidget.AppWidgetServiceImpl.this.mPackageManager.isInstantApp(uidPackages[0], android.os.UserHandle.getUserId(callingUid));
                    android.os.Binder.restoreCallingIdentity(identity);
                    return zIsInstantApp;
                }
            } catch (android.os.RemoteException e) {
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
            android.os.Binder.restoreCallingIdentity(identity);
            return false;
        }

        public boolean isInstantAppLocked(java.lang.String packageName, int userId) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                boolean zIsInstantApp = com.android.server.appwidget.AppWidgetServiceImpl.this.mPackageManager.isInstantApp(packageName, userId);
                android.os.Binder.restoreCallingIdentity(identity);
                return zIsInstantApp;
            } catch (android.os.RemoteException e) {
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        }

        public void enforceCallFromPackage(java.lang.String packageName) {
            com.android.server.appwidget.AppWidgetServiceImpl.this.mAppOpsManager.checkPackage(android.os.Binder.getCallingUid(), packageName);
        }

        public boolean hasCallerBindPermissionOrBindWhiteListedLocked(java.lang.String packageName) {
            try {
                com.android.server.appwidget.AppWidgetServiceImpl.this.mContext.enforceCallingOrSelfPermission("android.permission.BIND_APPWIDGET", null);
                return true;
            } catch (java.lang.SecurityException e) {
                if (!isCallerBindAppWidgetAllowListedLocked(packageName)) {
                    return false;
                }
                return true;
            }
        }

        private boolean isCallerBindAppWidgetAllowListedLocked(java.lang.String packageName) {
            int userId = android.os.UserHandle.getCallingUserId();
            int packageUid = com.android.server.appwidget.AppWidgetServiceImpl.this.getUidForPackage(packageName, userId);
            if (packageUid < 0) {
                throw new java.lang.IllegalArgumentException("No package " + packageName + " for user " + userId);
            }
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                com.android.server.appwidget.AppWidgetServiceImpl.this.ensureGroupStateLoadedLocked(userId);
                android.util.Pair<java.lang.Integer, java.lang.String> packageId = android.util.Pair.create(java.lang.Integer.valueOf(userId), packageName);
                return com.android.server.appwidget.AppWidgetServiceImpl.this.mPackagesWithBindWidgetPermission.contains(packageId);
            }
        }

        public boolean canAccessAppWidget(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, int uid, java.lang.String packageName) {
            if (isHostInPackageForUid(widget.host, uid, packageName) || isProviderInPackageForUid(widget.provider, uid, packageName) || isHostAccessingProvider(widget.host, widget.provider, uid, packageName)) {
                return true;
            }
            int userId = android.os.UserHandle.getUserId(uid);
            if ((widget.host.getUserId() == userId || (widget.provider != null && widget.provider.getUserId() == userId)) && com.android.server.appwidget.AppWidgetServiceImpl.this.mContext.checkCallingPermission("android.permission.BIND_APPWIDGET") == 0) {
                return true;
            }
            if (com.android.server.appwidget.AppWidgetServiceImpl.DEBUG) {
                android.util.Slog.i(com.android.server.appwidget.AppWidgetServiceImpl.TAG, "canAccessAppWidget() failed. packageName=" + packageName + " uid=" + uid + " userId=" + userId + " widget=" + widget);
                return false;
            }
            return false;
        }

        private boolean isParentOrProfile(int parentId, int profileId) {
            return parentId == profileId || getProfileParent(profileId) == parentId;
        }

        public boolean isProviderInCallerOrInProfileAndWhitelListed(java.lang.String packageName, int profileId) {
            int callerId = android.os.UserHandle.getCallingUserId();
            if (profileId == callerId) {
                return true;
            }
            int parentId = getProfileParent(profileId);
            if (parentId != callerId) {
                return false;
            }
            return isProviderWhiteListed(packageName, profileId);
        }

        public boolean isProviderWhiteListed(java.lang.String packageName, int profileId) {
            if (com.android.server.appwidget.AppWidgetServiceImpl.this.mDevicePolicyManagerInternal == null) {
                return false;
            }
            java.util.List<java.lang.String> crossProfilePackages = com.android.server.appwidget.AppWidgetServiceImpl.this.mDevicePolicyManagerInternal.getCrossProfileWidgetProviders(profileId);
            return crossProfilePackages.contains(packageName);
        }

        public int getProfileParent(int profileId) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.UserInfo parent = com.android.server.appwidget.AppWidgetServiceImpl.this.mUserManager.getProfileParent(profileId);
                if (parent != null) {
                    return parent.getUserHandle().getIdentifier();
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return -10;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public int getGroupParent(int profileId) {
            int parentId = com.android.server.appwidget.AppWidgetServiceImpl.this.mSecurityPolicy.getProfileParent(profileId);
            return parentId != -10 ? parentId : profileId;
        }

        public boolean isHostInPackageForUid(com.android.server.appwidget.AppWidgetServiceImpl.Host host, int uid, java.lang.String packageName) {
            return host.id.uid == uid && host.id.packageName.equals(packageName);
        }

        public boolean isProviderInPackageForUid(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int uid, java.lang.String packageName) {
            return provider != null && provider.id.uid == uid && provider.id.componentName.getPackageName().equals(packageName);
        }

        public boolean isHostAccessingProvider(com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int uid, java.lang.String packageName) {
            return host.id.uid == uid && provider != null && provider.id.componentName.getPackageName().equals(packageName);
        }

        private boolean isProfileEnabled(int profileId) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.UserInfo userInfo = com.android.server.appwidget.AppWidgetServiceImpl.this.mUserManager.getUserInfo(profileId);
                if (userInfo != null) {
                    if (userInfo.isEnabled()) {
                        android.os.Binder.restoreCallingIdentity(identity);
                        return true;
                    }
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class Provider {
        private static final int[] WIDGET_CATEGORY_FLAGS = {1, 2, 4};
        android.app.PendingIntent broadcast;
        android.util.SparseArray<android.widget.RemoteViews> generatedPreviews;
        com.android.server.appwidget.AppWidgetServiceImpl.ProviderId id;
        android.appwidget.AppWidgetProviderInfo info;
        java.lang.String infoTag;
        boolean mInfoParsed;
        boolean maskedByLockedProfile;
        boolean maskedByQuietProfile;
        boolean maskedByStoppedPackage;
        boolean maskedBySuspendedPackage;
        android.util.IntArray pendingDeletedWidgetIds;
        int tag;
        java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Widget> widgets;
        boolean zombie;

        private Provider() {
            this.widgets = new java.util.ArrayList<>();
            this.generatedPreviews = new android.util.SparseArray<>(3);
            this.pendingDeletedWidgetIds = new android.util.IntArray();
            this.mInfoParsed = false;
            this.tag = -1;
        }

        public int getUserId() {
            return android.os.UserHandle.getUserId(this.id.uid);
        }

        public boolean isInPackageForUser(java.lang.String packageName, int userId) {
            return getUserId() == userId && this.id.componentName.getPackageName().equals(packageName);
        }

        public boolean hostedByPackageForUser(java.lang.String packageName, int userId) {
            int N = this.widgets.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = this.widgets.get(i);
                if (packageName.equals(widget.host.id.packageName) && widget.host.getUserId() == userId) {
                    return true;
                }
            }
            return false;
        }

        public android.appwidget.AppWidgetProviderInfo getInfoLocked(android.content.Context context) {
            if (!this.mInfoParsed) {
                if (!this.zombie) {
                    android.appwidget.AppWidgetProviderInfo newInfo = null;
                    if (!android.text.TextUtils.isEmpty(this.infoTag)) {
                        newInfo = com.android.server.appwidget.AppWidgetServiceImpl.parseAppWidgetProviderInfo(context, this.id, this.info.providerInfo, this.infoTag);
                    }
                    if (newInfo == null) {
                        newInfo = com.android.server.appwidget.AppWidgetServiceImpl.parseAppWidgetProviderInfo(context, this.id, this.info.providerInfo, "android.appwidget.provider");
                    }
                    if (newInfo != null) {
                        this.info = newInfo;
                        if (com.android.server.appwidget.AppWidgetServiceImpl.DEBUG) {
                            java.util.Objects.requireNonNull(this.info);
                        }
                        updateGeneratedPreviewCategoriesLocked();
                    }
                }
                this.mInfoParsed = true;
            }
            return this.info;
        }

        public android.appwidget.AppWidgetProviderInfo getPartialInfoLocked() {
            return this.info;
        }

        public void setPartialInfoLocked(android.appwidget.AppWidgetProviderInfo info) {
            this.info = info;
            if (com.android.server.appwidget.AppWidgetServiceImpl.DEBUG) {
                java.util.Objects.requireNonNull(this.info);
            }
            this.mInfoParsed = false;
        }

        public void setInfoLocked(android.appwidget.AppWidgetProviderInfo info) {
            this.info = info;
            if (com.android.server.appwidget.AppWidgetServiceImpl.DEBUG) {
                java.util.Objects.requireNonNull(this.info);
            }
            this.mInfoParsed = true;
        }

        public android.widget.RemoteViews getGeneratedPreviewLocked(int widgetCategories) {
            for (int i = 0; i < this.generatedPreviews.size(); i++) {
                if ((this.generatedPreviews.keyAt(i) & widgetCategories) != 0) {
                    return this.generatedPreviews.valueAt(i);
                }
            }
            return null;
        }

        public void setGeneratedPreviewLocked(int widgetCategories, android.widget.RemoteViews preview) {
            for (int flag : WIDGET_CATEGORY_FLAGS) {
                if ((widgetCategories & flag) != 0) {
                    this.generatedPreviews.put(flag, preview);
                }
            }
            updateGeneratedPreviewCategoriesLocked();
        }

        public boolean removeGeneratedPreviewLocked(int widgetCategories) {
            boolean changed = false;
            for (int flag : WIDGET_CATEGORY_FLAGS) {
                if ((widgetCategories & flag) != 0) {
                    changed |= this.generatedPreviews.removeReturnOld(flag) != null;
                }
            }
            if (changed) {
                updateGeneratedPreviewCategoriesLocked();
            }
            return changed;
        }

        public boolean clearGeneratedPreviewsLocked() {
            if (this.generatedPreviews.size() > 0) {
                this.generatedPreviews.clear();
                updateGeneratedPreviewCategoriesLocked();
                return true;
            }
            return false;
        }

        private void updateGeneratedPreviewCategoriesLocked() {
            this.info.generatedPreviewCategories = 0;
            for (int i = 0; i < this.generatedPreviews.size(); i++) {
                this.info.generatedPreviewCategories |= this.generatedPreviews.keyAt(i);
            }
        }

        public java.lang.String toString() {
            return "Provider{" + this.id + (this.zombie ? " Z" : "") + '}';
        }

        public boolean setMaskedByQuietProfileLocked(boolean masked) {
            boolean oldState = this.maskedByQuietProfile;
            this.maskedByQuietProfile = masked;
            return masked != oldState;
        }

        public boolean setMaskedByLockedProfileLocked(boolean masked) {
            boolean oldState = this.maskedByLockedProfile;
            this.maskedByLockedProfile = masked;
            return masked != oldState;
        }

        public boolean setMaskedBySuspendedPackageLocked(boolean masked) {
            boolean oldState = this.maskedBySuspendedPackage;
            this.maskedBySuspendedPackage = masked;
            return masked != oldState;
        }

        public boolean setMaskedByStoppedPackageLocked(boolean masked) {
            boolean oldState = this.maskedByStoppedPackage;
            this.maskedByStoppedPackage = masked;
            return masked != oldState;
        }

        public boolean isMaskedLocked() {
            return this.maskedByQuietProfile || this.maskedByLockedProfile || this.maskedBySuspendedPackage || this.maskedByStoppedPackage;
        }

        public boolean shouldBePersisted() {
            return (this.widgets.isEmpty() && android.text.TextUtils.isEmpty(this.infoTag)) ? false : true;
        }
    }

    static final class ProviderId {
        final android.content.ComponentName componentName;
        final int uid;

        ProviderId(int uid, android.content.ComponentName componentName) {
            this.uid = uid;
            this.componentName = componentName;
        }

        public android.os.UserHandle getProfile() {
            return android.os.UserHandle.getUserHandleForUid(this.uid);
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            com.android.server.appwidget.AppWidgetServiceImpl.ProviderId other = (com.android.server.appwidget.AppWidgetServiceImpl.ProviderId) obj;
            if (this.uid != other.uid) {
                return false;
            }
            if (this.componentName == null) {
                if (other.componentName != null) {
                    return false;
                }
            } else if (!this.componentName.equals(other.componentName)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result = this.uid;
            return (result * 31) + (this.componentName != null ? this.componentName.hashCode() : 0);
        }

        public java.lang.String toString() {
            return "ProviderId{user:" + android.os.UserHandle.getUserId(this.uid) + ", app:" + android.os.UserHandle.getAppId(this.uid) + ", cmp:" + this.componentName + '}';
        }
    }

    private static final class Host {
        private static final boolean DEBUG = true;
        private static final java.lang.String TAG = "AppWidgetServiceHost";
        com.android.internal.appwidget.IAppWidgetHost callbacks;
        com.android.server.appwidget.AppWidgetServiceImpl.HostId id;
        long lastWidgetUpdateSequenceNo;
        int tag;
        java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Widget> widgets;
        boolean zombie;

        private Host() {
            this.widgets = new java.util.ArrayList<>();
            this.tag = -1;
        }

        public int getUserId() {
            return android.os.UserHandle.getUserId(this.id.uid);
        }

        public boolean isInPackageForUser(java.lang.String packageName, int userId) {
            return getUserId() == userId && this.id.packageName.equals(packageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hostsPackageForUser(java.lang.String pkg, int userId) {
            int N = this.widgets.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = this.widgets.get(i).provider;
                if (provider != null && provider.getUserId() == userId && pkg.equals(provider.id.componentName.getPackageName())) {
                    return true;
                }
            }
            return false;
        }

        public void getPendingUpdatesForIdLocked(android.content.Context context, int appWidgetId, android.util.LongSparseArray<android.appwidget.PendingHostUpdate> outUpdates) {
            android.appwidget.PendingHostUpdate update;
            long updateSequenceNo = this.lastWidgetUpdateSequenceNo;
            int N = this.widgets.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = this.widgets.get(i);
                if (widget.appWidgetId == appWidgetId) {
                    for (int j = widget.updateSequenceNos.size() - 1; j >= 0; j--) {
                        long requestId = widget.updateSequenceNos.valueAt(j);
                        if (requestId > updateSequenceNo) {
                            int id = widget.updateSequenceNos.keyAt(j);
                            switch (id) {
                                case 0:
                                    update = android.appwidget.PendingHostUpdate.updateAppWidget(appWidgetId, com.android.server.appwidget.AppWidgetServiceImpl.cloneIfLocalBinder(widget.getEffectiveViewsLocked()));
                                    break;
                                case 1:
                                    update = android.appwidget.PendingHostUpdate.providerChanged(appWidgetId, widget.provider.getInfoLocked(context));
                                    break;
                                default:
                                    update = android.appwidget.PendingHostUpdate.viewDataChanged(appWidgetId, id);
                                    break;
                            }
                            outUpdates.put(requestId, update);
                        }
                    }
                    return;
                }
            }
            outUpdates.put(this.lastWidgetUpdateSequenceNo, android.appwidget.PendingHostUpdate.appWidgetRemoved(appWidgetId));
        }

        public android.util.SparseArray<java.lang.String> getWidgetUidsIfBound() {
            android.util.SparseArray<java.lang.String> uids = new android.util.SparseArray<>();
            for (int i = this.widgets.size() - 1; i >= 0; i--) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = this.widgets.get(i);
                if (widget.provider == null) {
                    android.util.Slog.i(TAG, "getWidgetUids,widget.provider is null,widget:" + widget);
                } else {
                    com.android.server.appwidget.AppWidgetServiceImpl.ProviderId providerId = widget.provider.id;
                    uids.put(providerId.uid, providerId.componentName.getPackageName());
                }
            }
            return uids;
        }

        public java.lang.String toString() {
            return "Host{" + this.id + (this.zombie ? " Z" : "") + '}';
        }
    }

    private static final class HostId {
        final int hostId;
        final java.lang.String packageName;
        final int uid;

        public HostId(int uid, int hostId, java.lang.String packageName) {
            this.uid = uid;
            this.hostId = hostId;
            this.packageName = packageName;
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            com.android.server.appwidget.AppWidgetServiceImpl.HostId other = (com.android.server.appwidget.AppWidgetServiceImpl.HostId) obj;
            if (this.uid != other.uid || this.hostId != other.hostId) {
                return false;
            }
            if (this.packageName == null) {
                if (other.packageName != null) {
                    return false;
                }
            } else if (!this.packageName.equals(other.packageName)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result = this.uid;
            return (((result * 31) + this.hostId) * 31) + (this.packageName != null ? this.packageName.hashCode() : 0);
        }

        public java.lang.String toString() {
            return "HostId{user:" + android.os.UserHandle.getUserId(this.uid) + ", app:" + android.os.UserHandle.getAppId(this.uid) + ", hostId:" + this.hostId + ", pkg:" + this.packageName + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class Widget {
        int appWidgetId;
        com.android.server.appwidget.AppWidgetServiceImpl.Host host;
        android.widget.RemoteViews maskedViews;
        android.os.Bundle options;
        com.android.server.appwidget.AppWidgetServiceImpl.Provider provider;
        int restoredId;
        boolean trackingUpdate;
        android.util.SparseLongArray updateSequenceNos;
        android.widget.RemoteViews views;

        private Widget() {
            this.updateSequenceNos = new android.util.SparseLongArray(2);
            this.trackingUpdate = false;
        }

        public java.lang.String toString() {
            return "AppWidgetId{" + this.appWidgetId + ':' + this.host + ':' + this.provider + '}';
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean replaceWithMaskedViewsLocked(android.widget.RemoteViews views) {
            this.maskedViews = views;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean clearMaskedViewsLocked() {
            if (this.maskedViews != null) {
                this.maskedViews = null;
                return true;
            }
            return false;
        }

        public android.widget.RemoteViews getEffectiveViewsLocked() {
            return this.maskedViews != null ? this.maskedViews : this.views;
        }
    }

    static final class ApiCounter {
        private final java.util.Map<com.android.server.appwidget.AppWidgetServiceImpl.ProviderId, com.android.server.appwidget.AppWidgetServiceImpl.ApiCounter.ApiCallRecord> mCallCount;
        private int mMaxCallsPerInterval;
        private java.util.function.LongSupplier mMonotonicClock;
        private long mResetIntervalMs;

        private static final class ApiCallRecord {
            public int apiCallCount;
            public long lastResetTimeMs;

            private ApiCallRecord() {
                this.apiCallCount = 0;
                this.lastResetTimeMs = 0L;
            }

            void reset(long nowMs) {
                this.apiCallCount = 0;
                this.lastResetTimeMs = nowMs;
            }
        }

        ApiCounter(long resetIntervalMs, int maxCallsPerInterval) {
            this(resetIntervalMs, maxCallsPerInterval, new java.util.function.LongSupplier() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$ApiCounter$$ExternalSyntheticLambda0
                @Override // java.util.function.LongSupplier
                public final long getAsLong() {
                    return android.os.SystemClock.elapsedRealtime();
                }
            });
        }

        ApiCounter(long resetIntervalMs, int maxCallsPerInterval, java.util.function.LongSupplier monotonicClock) {
            this.mCallCount = new android.util.ArrayMap();
            this.mResetIntervalMs = resetIntervalMs;
            this.mMaxCallsPerInterval = maxCallsPerInterval;
            this.mMonotonicClock = monotonicClock;
        }

        public void setResetIntervalMs(long resetIntervalMs) {
            this.mResetIntervalMs = resetIntervalMs;
        }

        public long getResetIntervalMs() {
            return this.mResetIntervalMs;
        }

        public void setMaxCallsPerInterval(int maxCallsPerInterval) {
            this.mMaxCallsPerInterval = maxCallsPerInterval;
        }

        public int getMaxCallsPerInterval() {
            return this.mMaxCallsPerInterval;
        }

        public boolean tryApiCall(com.android.server.appwidget.AppWidgetServiceImpl.ProviderId provider) {
            com.android.server.appwidget.AppWidgetServiceImpl.ApiCounter.ApiCallRecord record = getOrCreateRecord(provider);
            long now = this.mMonotonicClock.getAsLong();
            long timeSinceLastResetMs = now - record.lastResetTimeMs;
            if (timeSinceLastResetMs > this.mResetIntervalMs) {
                record.reset(now);
            }
            if (record.apiCallCount < this.mMaxCallsPerInterval) {
                record.apiCallCount++;
                return true;
            }
            return false;
        }

        public void remove(com.android.server.appwidget.AppWidgetServiceImpl.ProviderId id) {
            this.mCallCount.remove(id);
        }

        private com.android.server.appwidget.AppWidgetServiceImpl.ApiCounter.ApiCallRecord getOrCreateRecord(com.android.server.appwidget.AppWidgetServiceImpl.ProviderId provider) {
            if (!this.mCallCount.containsKey(provider)) {
                this.mCallCount.put(provider, new com.android.server.appwidget.AppWidgetServiceImpl.ApiCounter.ApiCallRecord());
            }
            return this.mCallCount.get(provider);
        }
    }

    private class LoadedWidgetState {
        final int hostTag;
        final int providerTag;
        final com.android.server.appwidget.AppWidgetServiceImpl.Widget widget;

        public LoadedWidgetState(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, int hostTag, int providerTag) {
            this.widget = widget;
            this.hostTag = hostTag;
            this.providerTag = providerTag;
        }
    }

    private final class SaveStateRunnable implements java.lang.Runnable {
        final int mUserId;

        public SaveStateRunnable(int userId) {
            this.mUserId = userId;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                android.os.Trace.traceBegin(64L, "convert_state_and_io");
                com.android.server.appwidget.AppWidgetServiceImpl.this.ensureGroupStateLoadedLocked(this.mUserId, false);
                com.android.server.appwidget.AppWidgetServiceImpl.this.saveStateLocked(this.mUserId);
                android.os.Trace.traceEnd(64L);
            }
        }
    }

    private final class BackupRestoreController {
        private static final boolean DEBUG = true;
        private static final java.lang.String TAG = "BackupRestoreController";
        private static final int WIDGET_STATE_VERSION = 2;
        private boolean mHasSystemRestoreFinished;
        private final android.util.SparseArray<java.util.Set<java.lang.String>> mPrunedAppsPerUser;
        private final java.util.HashMap<com.android.server.appwidget.AppWidgetServiceImpl.Host, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>> mUpdatesByHost;
        private final java.util.HashMap<com.android.server.appwidget.AppWidgetServiceImpl.Provider, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>> mUpdatesByProvider;

        private BackupRestoreController() {
            this.mPrunedAppsPerUser = new android.util.SparseArray<>();
            this.mUpdatesByProvider = new java.util.HashMap<>();
            this.mUpdatesByHost = new java.util.HashMap<>();
        }

        public java.util.List<java.lang.String> getWidgetParticipants(int userId) {
            android.util.Slog.i(TAG, "Getting widget participants for user: " + userId);
            java.util.HashSet<java.lang.String> packages = new java.util.HashSet<>();
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                int N = com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.size();
                for (int i = 0; i < N; i++) {
                    com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = (com.android.server.appwidget.AppWidgetServiceImpl.Widget) com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.get(i);
                    if (isProviderAndHostInUser(widget, userId)) {
                        packages.add(widget.host.id.packageName);
                        com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = widget.provider;
                        if (provider != null) {
                            packages.add(provider.id.componentName.getPackageName());
                        }
                    }
                }
            }
            return new java.util.ArrayList(packages);
        }

        public byte[] getWidgetState(java.lang.String backedupPackage, int userId) {
            android.util.Slog.i(TAG, "Getting widget state for user: " + userId);
            java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                if (!packageNeedsWidgetBackupLocked(backedupPackage, userId)) {
                    return null;
                }
                try {
                    com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.newFastSerializer();
                    out.setOutput(stream, java.nio.charset.StandardCharsets.UTF_8.name());
                    out.startDocument((java.lang.String) null, true);
                    out.startTag((java.lang.String) null, "ws");
                    out.attributeInt((java.lang.String) null, "version", 2);
                    out.attribute((java.lang.String) null, "pkg", backedupPackage);
                    int index = 0;
                    int N = com.android.server.appwidget.AppWidgetServiceImpl.this.mProviders.size();
                    for (int i = 0; i < N; i++) {
                        com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = (com.android.server.appwidget.AppWidgetServiceImpl.Provider) com.android.server.appwidget.AppWidgetServiceImpl.this.mProviders.get(i);
                        if (provider.shouldBePersisted() && (provider.isInPackageForUser(backedupPackage, userId) || provider.hostedByPackageForUser(backedupPackage, userId))) {
                            provider.tag = index;
                            com.android.server.appwidget.AppWidgetServiceImpl.serializeProvider(out, provider, false);
                            index++;
                        }
                    }
                    int N2 = com.android.server.appwidget.AppWidgetServiceImpl.this.mHosts.size();
                    int index2 = 0;
                    for (int i2 = 0; i2 < N2; i2++) {
                        com.android.server.appwidget.AppWidgetServiceImpl.Host host = (com.android.server.appwidget.AppWidgetServiceImpl.Host) com.android.server.appwidget.AppWidgetServiceImpl.this.mHosts.get(i2);
                        if (!host.widgets.isEmpty() && (host.isInPackageForUser(backedupPackage, userId) || host.hostsPackageForUser(backedupPackage, userId))) {
                            host.tag = index2;
                            com.android.server.appwidget.AppWidgetServiceImpl.serializeHost(out, host);
                            index2++;
                        }
                    }
                    int N3 = com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.size();
                    for (int i3 = 0; i3 < N3; i3++) {
                        com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = (com.android.server.appwidget.AppWidgetServiceImpl.Widget) com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.get(i3);
                        com.android.server.appwidget.AppWidgetServiceImpl.Provider provider2 = widget.provider;
                        if (widget.host.isInPackageForUser(backedupPackage, userId) || (provider2 != null && provider2.isInPackageForUser(backedupPackage, userId))) {
                            com.android.server.appwidget.AppWidgetServiceImpl.serializeAppWidget(out, widget, false);
                        }
                    }
                    out.endTag((java.lang.String) null, "ws");
                    out.endDocument();
                    return stream.toByteArray();
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, "Unable to save widget state for " + backedupPackage);
                    return null;
                }
            }
        }

        public void systemRestoreStarting(int userId) {
            android.util.Slog.i(TAG, "System restore starting for user: " + userId);
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                this.mHasSystemRestoreFinished = false;
                getPrunedAppsLocked(userId).clear();
                this.mUpdatesByProvider.clear();
                this.mUpdatesByHost.clear();
            }
        }

        public void restoreWidgetState(java.lang.String packageName, byte[] restoredState, int userId) throws java.lang.Throwable {
            java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Provider> restoredProviders;
            java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Host> restoredHosts;
            com.android.modules.utils.TypedXmlPullParser parser;
            java.io.ByteArrayInputStream stream;
            java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Provider> restoredProviders2;
            java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.Host> restoredHosts2;
            com.android.server.appwidget.AppWidgetServiceImpl.Provider p;
            android.util.Slog.i(TAG, "Restoring widget state for user:" + userId + " package: " + packageName);
            java.io.ByteArrayInputStream stream2 = new java.io.ByteArrayInputStream(restoredState);
            try {
                try {
                    restoredProviders = new java.util.ArrayList<>();
                    restoredHosts = new java.util.ArrayList<>();
                    parser = android.util.Xml.newFastPullParser();
                    parser.setInput(stream2, java.nio.charset.StandardCharsets.UTF_8.name());
                } catch (java.lang.Throwable th) {
                    th = th;
                    com.android.server.appwidget.AppWidgetServiceImpl.this.saveGroupStateAsync(userId);
                    throw th;
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            } catch (java.lang.Throwable th2) {
                th = th2;
                com.android.server.appwidget.AppWidgetServiceImpl.this.saveGroupStateAsync(userId);
                throw th;
            }
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                while (true) {
                    try {
                        int type = parser.next();
                        if (type == 2) {
                            java.lang.String tag = parser.getName();
                            if ("ws".equals(tag)) {
                                try {
                                    int versionNumber = parser.getAttributeInt((java.lang.String) null, "version");
                                    if (versionNumber > 2) {
                                        android.util.Slog.w(TAG, "Unable to process state version " + versionNumber);
                                        com.android.server.appwidget.AppWidgetServiceImpl.this.saveGroupStateAsync(userId);
                                        return;
                                    }
                                    java.lang.String pkg = parser.getAttributeValue((java.lang.String) null, "pkg");
                                    if (!packageName.equals(pkg)) {
                                        android.util.Slog.w(TAG, "Package mismatch in ws");
                                        com.android.server.appwidget.AppWidgetServiceImpl.this.saveGroupStateAsync(userId);
                                        return;
                                    } else {
                                        stream = stream2;
                                        restoredProviders2 = restoredProviders;
                                        restoredHosts2 = restoredHosts;
                                    }
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                    try {
                                        throw th;
                                    } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e2) {
                                        android.util.Slog.w(TAG, "Unable to restore widget state for " + packageName);
                                        com.android.server.appwidget.AppWidgetServiceImpl.this.saveGroupStateAsync(userId);
                                    }
                                }
                            } else if ("p".equals(tag)) {
                                try {
                                    java.lang.String pkg2 = parser.getAttributeValue((java.lang.String) null, "pkg");
                                    java.lang.String cl = parser.getAttributeValue((java.lang.String) null, "cl");
                                    android.content.ComponentName componentName = new android.content.ComponentName(pkg2, cl);
                                    com.android.server.appwidget.AppWidgetServiceImpl.Provider p2 = findProviderLocked(componentName, userId);
                                    if (p2 == null) {
                                        android.appwidget.AppWidgetProviderInfo info = new android.appwidget.AppWidgetProviderInfo();
                                        info.provider = componentName;
                                        p = new com.android.server.appwidget.AppWidgetServiceImpl.Provider();
                                        stream = stream2;
                                        try {
                                            p.id = new com.android.server.appwidget.AppWidgetServiceImpl.ProviderId(-1, componentName);
                                            p.setPartialInfoLocked(info);
                                            p.zombie = true;
                                            com.android.server.appwidget.AppWidgetServiceImpl.this.mProviders.add(p);
                                        } catch (java.lang.Throwable th4) {
                                            th = th4;
                                            throw th;
                                        }
                                    } else {
                                        stream = stream2;
                                        p = p2;
                                    }
                                    android.util.Slog.i(TAG, "   provider " + p.id);
                                    restoredProviders.add(p);
                                    restoredProviders2 = restoredProviders;
                                    restoredHosts2 = restoredHosts;
                                } catch (java.lang.Throwable th5) {
                                    th = th5;
                                }
                            } else {
                                stream = stream2;
                                try {
                                    if ("h".equals(tag)) {
                                        java.lang.String pkg3 = parser.getAttributeValue((java.lang.String) null, "pkg");
                                        int uid = com.android.server.appwidget.AppWidgetServiceImpl.this.getUidForPackage(pkg3, userId);
                                        int hostId = parser.getAttributeIntHex((java.lang.String) null, "id");
                                        com.android.server.appwidget.AppWidgetServiceImpl.Host h = com.android.server.appwidget.AppWidgetServiceImpl.this.lookupOrAddHostLocked(new com.android.server.appwidget.AppWidgetServiceImpl.HostId(uid, hostId, pkg3));
                                        restoredHosts.add(h);
                                        android.util.Slog.i(TAG, "   host[" + restoredHosts.size() + "]: {" + h.id + "}");
                                        restoredProviders2 = restoredProviders;
                                        restoredHosts2 = restoredHosts;
                                    } else if ("g".equals(tag)) {
                                        int restoredId = parser.getAttributeIntHex((java.lang.String) null, "id");
                                        int hostIndex = parser.getAttributeIntHex((java.lang.String) null, "h");
                                        com.android.server.appwidget.AppWidgetServiceImpl.Host host = restoredHosts.get(hostIndex);
                                        int which = parser.getAttributeIntHex((java.lang.String) null, "p", -1);
                                        com.android.server.appwidget.AppWidgetServiceImpl.Provider p3 = which != -1 ? restoredProviders.get(which) : null;
                                        pruneWidgetStateLocked(host.id.packageName, userId);
                                        if (p3 != null) {
                                            pruneWidgetStateLocked(p3.id.componentName.getPackageName(), userId);
                                        }
                                        com.android.server.appwidget.AppWidgetServiceImpl.Widget id = findRestoredWidgetLocked(restoredId, host, p3);
                                        if (id == null) {
                                            try {
                                                id = new com.android.server.appwidget.AppWidgetServiceImpl.Widget();
                                                id.appWidgetId = com.android.server.appwidget.AppWidgetServiceImpl.this.incrementAndGetAppWidgetIdLocked(userId);
                                                id.restoredId = restoredId;
                                                id.options = com.android.server.appwidget.AppWidgetServiceImpl.parseWidgetIdOptions(parser);
                                                id.host = host;
                                                id.host.widgets.add(id);
                                                id.provider = p3;
                                                if (id.provider != null) {
                                                    id.provider.widgets.add(id);
                                                }
                                                restoredProviders2 = restoredProviders;
                                                try {
                                                    android.util.Slog.i(TAG, "New restored id " + restoredId + " now " + id);
                                                    com.android.server.appwidget.AppWidgetServiceImpl.this.addWidgetLocked(id);
                                                } catch (java.lang.Throwable th6) {
                                                    th = th6;
                                                    throw th;
                                                }
                                            } catch (java.lang.Throwable th7) {
                                                th = th7;
                                            }
                                        } else {
                                            restoredProviders2 = restoredProviders;
                                        }
                                        try {
                                            if (id.provider == null || id.provider.getPartialInfoLocked() == null) {
                                                android.util.Slog.w(TAG, "Missing provider for restored widget " + id);
                                            } else {
                                                stashProviderRestoreUpdateLocked(id.provider, restoredId, id.appWidgetId);
                                            }
                                            stashHostRestoreUpdateLocked(id.host, restoredId, id.appWidgetId);
                                            if (id.provider == null || id.provider.info == null || id.provider.info.provider == null || id.provider.id == null) {
                                                restoredHosts2 = restoredHosts;
                                            } else {
                                                restoredHosts2 = restoredHosts;
                                                try {
                                                    com.android.server.appwidget.AppWidgetServiceImpl.this.mAppWidgetServiceExt.hookUpdateWidgetSate(id.provider.id.uid, id.provider.info.provider.getPackageName(), true);
                                                } catch (java.lang.Throwable th8) {
                                                    th = th8;
                                                    throw th;
                                                }
                                            }
                                            android.util.Slog.i(TAG, "   instance: " + restoredId + " -> " + id.appWidgetId + " :: p=" + id.provider);
                                        } catch (java.lang.Throwable th9) {
                                            th = th9;
                                        }
                                    } else {
                                        restoredProviders2 = restoredProviders;
                                        restoredHosts2 = restoredHosts;
                                    }
                                } catch (java.lang.Throwable th10) {
                                    th = th10;
                                }
                            }
                        } else {
                            stream = stream2;
                            restoredProviders2 = restoredProviders;
                            restoredHosts2 = restoredHosts;
                        }
                        if (type == 1) {
                            break;
                        }
                        restoredProviders = restoredProviders2;
                        restoredHosts = restoredHosts2;
                        stream2 = stream;
                    } catch (java.lang.Throwable th11) {
                        th = th11;
                    }
                }
                com.android.server.appwidget.AppWidgetServiceImpl.this.saveGroupStateAsync(userId);
            }
        }

        public void systemRestoreFinished(int userId) {
            android.util.Slog.i(TAG, "systemRestoreFinished for " + userId);
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                this.mHasSystemRestoreFinished = true;
                maybeSendWidgetRestoreBroadcastsLocked(userId);
            }
        }

        public void widgetComponentsChanged(int userId) {
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                if (this.mHasSystemRestoreFinished) {
                    maybeSendWidgetRestoreBroadcastsLocked(userId);
                }
            }
        }

        private void maybeSendWidgetRestoreBroadcastsLocked(int userId) {
            java.lang.String str;
            java.lang.String str2;
            java.util.Set<java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Host, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>>> hostEntries;
            java.lang.String str3;
            java.lang.String str4;
            java.util.Iterator<java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Host, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>>> it;
            java.lang.String str5;
            java.lang.String str6;
            int[] newIds;
            java.lang.String str7 = TAG;
            android.util.Slog.i(TAG, "maybeSendWidgetRestoreBroadcasts for " + userId);
            android.os.UserHandle userHandle = new android.os.UserHandle(userId);
            java.util.Set<java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Provider, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>>> providerEntries = this.mUpdatesByProvider.entrySet();
            java.util.Iterator<java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Provider, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>>> it2 = providerEntries.iterator();
            while (true) {
                str = "   ";
                str2 = " pending: ";
                if (!it2.hasNext()) {
                    break;
                }
                java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Provider, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>> e = it2.next();
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = e.getKey();
                if (!provider.zombie) {
                    java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord> updates = e.getValue();
                    int pending = countPendingUpdates(updates);
                    android.util.Slog.i(TAG, "Provider " + provider + " pending: " + pending);
                    if (pending > 0) {
                        int[] oldIds = new int[pending];
                        int[] newIds2 = new int[pending];
                        int N = updates.size();
                        int nextPending = 0;
                        int nextPending2 = 0;
                        while (nextPending2 < N) {
                            com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord r = updates.get(nextPending2);
                            int N2 = N;
                            if (r.notified) {
                                newIds = newIds2;
                            } else {
                                r.notified = true;
                                oldIds[nextPending] = r.oldId;
                                newIds2[nextPending] = r.newId;
                                nextPending++;
                                newIds = newIds2;
                                android.util.Slog.i(TAG, "   " + r.oldId + " => " + r.newId);
                            }
                            nextPending2++;
                            N = N2;
                            newIds2 = newIds;
                        }
                        sendWidgetRestoreBroadcastLocked("android.appwidget.action.APPWIDGET_RESTORED", provider, null, oldIds, newIds2, userHandle);
                    }
                }
            }
            java.util.Set<java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Host, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>>> hostEntries2 = this.mUpdatesByHost.entrySet();
            java.util.Iterator<java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Host, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>>> it3 = hostEntries2.iterator();
            while (it3.hasNext()) {
                java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Host, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>> e2 = it3.next();
                com.android.server.appwidget.AppWidgetServiceImpl.Host host = e2.getKey();
                if (host.id.uid == -1) {
                    hostEntries = hostEntries2;
                    str3 = str;
                    str4 = str2;
                    it = it3;
                    str5 = str7;
                } else {
                    java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord> updates2 = e2.getValue();
                    int pending2 = countPendingUpdates(updates2);
                    android.util.Slog.i(str7, "Host " + host + str2 + pending2);
                    if (pending2 <= 0) {
                        hostEntries = hostEntries2;
                        str3 = str;
                        str4 = str2;
                        it = it3;
                        str5 = str7;
                    } else {
                        int[] oldIds2 = new int[pending2];
                        int[] newIds3 = new int[pending2];
                        int N3 = updates2.size();
                        int nextPending3 = 0;
                        hostEntries = hostEntries2;
                        int i = 0;
                        while (i < N3) {
                            java.lang.String str8 = str2;
                            com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord r2 = updates2.get(i);
                            java.util.Iterator<java.util.Map.Entry<com.android.server.appwidget.AppWidgetServiceImpl.Host, java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord>>> it4 = it3;
                            if (r2.notified) {
                                str6 = str;
                            } else {
                                r2.notified = true;
                                oldIds2[nextPending3] = r2.oldId;
                                newIds3[nextPending3] = r2.newId;
                                nextPending3++;
                                str6 = str;
                                android.util.Slog.i(str7, str + r2.oldId + " => " + r2.newId);
                            }
                            i++;
                            it3 = it4;
                            str2 = str8;
                            str = str6;
                        }
                        str3 = str;
                        str4 = str2;
                        it = it3;
                        str5 = str7;
                        sendWidgetRestoreBroadcastLocked("android.appwidget.action.APPWIDGET_HOST_RESTORED", null, host, oldIds2, newIds3, userHandle);
                    }
                }
                hostEntries2 = hostEntries;
                it3 = it;
                str2 = str4;
                str = str3;
                str7 = str5;
            }
        }

        private com.android.server.appwidget.AppWidgetServiceImpl.Provider findProviderLocked(android.content.ComponentName componentName, int userId) {
            int providerCount = com.android.server.appwidget.AppWidgetServiceImpl.this.mProviders.size();
            for (int i = 0; i < providerCount; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = (com.android.server.appwidget.AppWidgetServiceImpl.Provider) com.android.server.appwidget.AppWidgetServiceImpl.this.mProviders.get(i);
                if (provider.getUserId() == userId && provider.id.componentName.equals(componentName)) {
                    return provider;
                }
            }
            return null;
        }

        private com.android.server.appwidget.AppWidgetServiceImpl.Widget findRestoredWidgetLocked(int restoredId, com.android.server.appwidget.AppWidgetServiceImpl.Host host, com.android.server.appwidget.AppWidgetServiceImpl.Provider p) {
            android.util.Slog.i(TAG, "Find restored widget: id=" + restoredId + " host=" + host + " provider=" + p);
            if (p == null || host == null) {
                return null;
            }
            int N = com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = (com.android.server.appwidget.AppWidgetServiceImpl.Widget) com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.get(i);
                if (widget.restoredId == restoredId && widget.host.id.equals(host.id) && widget.provider.id.equals(p.id)) {
                    android.util.Slog.i(TAG, "   Found at " + i + " : " + widget);
                    return widget;
                }
            }
            return null;
        }

        private boolean packageNeedsWidgetBackupLocked(java.lang.String packageName, int userId) {
            int N = com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = (com.android.server.appwidget.AppWidgetServiceImpl.Widget) com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.get(i);
                if (isProviderAndHostInUser(widget, userId)) {
                    if (widget.host.isInPackageForUser(packageName, userId)) {
                        return true;
                    }
                    com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = widget.provider;
                    if (provider != null && provider.isInPackageForUser(packageName, userId)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void stashProviderRestoreUpdateLocked(com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, int oldId, int newId) {
            java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord> r = this.mUpdatesByProvider.get(provider);
            if (r == null) {
                r = new java.util.ArrayList<>();
                this.mUpdatesByProvider.put(provider, r);
            } else if (alreadyStashed(r, oldId, newId)) {
                android.util.Slog.i(TAG, "ID remap " + oldId + " -> " + newId + " already stashed for " + provider);
                return;
            }
            r.add(new com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord(oldId, newId));
        }

        private boolean alreadyStashed(java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord> stash, int oldId, int newId) {
            int N = stash.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord r = stash.get(i);
                if (r.oldId == oldId && r.newId == newId) {
                    return true;
                }
            }
            return false;
        }

        private void stashHostRestoreUpdateLocked(com.android.server.appwidget.AppWidgetServiceImpl.Host host, int oldId, int newId) {
            java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord> r = this.mUpdatesByHost.get(host);
            if (r == null) {
                r = new java.util.ArrayList<>();
                this.mUpdatesByHost.put(host, r);
            } else if (alreadyStashed(r, oldId, newId)) {
                android.util.Slog.i(TAG, "ID remap " + oldId + " -> " + newId + " already stashed for " + host);
                return;
            }
            r.add(new com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord(oldId, newId));
        }

        private void sendWidgetRestoreBroadcastLocked(java.lang.String action, com.android.server.appwidget.AppWidgetServiceImpl.Provider provider, com.android.server.appwidget.AppWidgetServiceImpl.Host host, int[] oldIds, int[] newIds, android.os.UserHandle userHandle) {
            android.content.Intent intent = new android.content.Intent(action);
            intent.putExtra("appWidgetOldIds", oldIds);
            intent.putExtra("appWidgetIds", newIds);
            if (provider != null) {
                intent.setComponent(provider.id.componentName);
                com.android.server.appwidget.AppWidgetServiceImpl.this.sendBroadcastAsUser(intent, userHandle, true);
            }
            if (host != null) {
                intent.setComponent(null);
                intent.setPackage(host.id.packageName);
                intent.putExtra("hostId", host.id.hostId);
                com.android.server.appwidget.AppWidgetServiceImpl.this.sendBroadcastAsUser(intent, userHandle, true);
            }
        }

        private void pruneWidgetStateLocked(java.lang.String pkg, int userId) {
            java.util.Set<java.lang.String> prunedApps = getPrunedAppsLocked(userId);
            if (!prunedApps.contains(pkg)) {
                android.util.Slog.i(TAG, "pruning widget state for restoring package " + pkg);
                for (int i = com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.size() - 1; i >= 0; i--) {
                    com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = (com.android.server.appwidget.AppWidgetServiceImpl.Widget) com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.get(i);
                    com.android.server.appwidget.AppWidgetServiceImpl.Host host = widget.host;
                    com.android.server.appwidget.AppWidgetServiceImpl.Provider provider = widget.provider;
                    if (host.hostsPackageForUser(pkg, userId) || (provider != null && provider.isInPackageForUser(pkg, userId))) {
                        host.widgets.remove(widget);
                        if (provider != null) {
                            provider.widgets.remove(widget);
                        }
                        com.android.server.appwidget.AppWidgetServiceImpl.this.decrementAppWidgetServiceRefCount(widget);
                        com.android.server.appwidget.AppWidgetServiceImpl.this.removeWidgetLocked(widget);
                    }
                }
                prunedApps.add(pkg);
                return;
            }
            android.util.Slog.i(TAG, "already pruned " + pkg + ", continuing normally");
        }

        private java.util.Set<java.lang.String> getPrunedAppsLocked(int userId) {
            if (!this.mPrunedAppsPerUser.contains(userId)) {
                this.mPrunedAppsPerUser.set(userId, new android.util.ArraySet());
            }
            return this.mPrunedAppsPerUser.get(userId);
        }

        private boolean isProviderAndHostInUser(com.android.server.appwidget.AppWidgetServiceImpl.Widget widget, int userId) {
            return widget.host.getUserId() == userId && (widget.provider == null || widget.provider.getUserId() == userId);
        }

        private int countPendingUpdates(java.util.ArrayList<com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord> updates) {
            int pending = 0;
            int N = updates.size();
            for (int i = 0; i < N; i++) {
                com.android.server.appwidget.AppWidgetServiceImpl.BackupRestoreController.RestoreUpdateRecord r = updates.get(i);
                if (!r.notified) {
                    pending++;
                }
            }
            return pending;
        }

        private class RestoreUpdateRecord {
            public int newId;
            public boolean notified = false;
            public int oldId;

            public RestoreUpdateRecord(int theOldId, int theNewId) {
                this.oldId = theOldId;
                this.newId = theNewId;
            }
        }
    }

    private class AppWidgetManagerLocal extends android.appwidget.AppWidgetManagerInternal {
        private AppWidgetManagerLocal() {
        }

        public android.util.ArraySet<java.lang.String> getHostedWidgetPackages(int uid) {
            android.util.ArraySet<java.lang.String> widgetPackages;
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                widgetPackages = null;
                int widgetCount = com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.size();
                for (int i = 0; i < widgetCount; i++) {
                    com.android.server.appwidget.AppWidgetServiceImpl.Widget widget = (com.android.server.appwidget.AppWidgetServiceImpl.Widget) com.android.server.appwidget.AppWidgetServiceImpl.this.mWidgets.get(i);
                    if (widget.host.id.uid == uid && widget.provider != null) {
                        if (widgetPackages == null) {
                            widgetPackages = new android.util.ArraySet<>();
                        }
                        widgetPackages.add(widget.provider.id.componentName.getPackageName());
                    }
                }
            }
            return widgetPackages;
        }

        public void unlockUser(int userId) {
            com.android.server.appwidget.AppWidgetServiceImpl.this.handleUserUnlocked(userId);
        }

        public void applyResourceOverlaysToWidgets(java.util.Set<java.lang.String> packageNames, int userId, boolean updateFrameworkRes) {
            synchronized (com.android.server.appwidget.AppWidgetServiceImpl.this.mLock) {
                com.android.server.appwidget.AppWidgetServiceImpl.this.applyResourceOverlaysToWidgetsLocked(new java.util.HashSet(packageNames), userId, updateFrameworkRes);
            }
        }
    }
}
