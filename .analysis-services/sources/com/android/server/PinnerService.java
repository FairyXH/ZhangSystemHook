package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class PinnerService extends com.android.server.SystemService {
    public static final java.lang.String ANON_REGION_STAT_NAME = "[anon]";
    private static final boolean DEBUG = false;
    private static final java.lang.String DEVICE_CONFIG_KEY_ANON_SIZE = "pin_shared_anon_size";
    private static final java.lang.String DEVICE_CONFIG_NAMESPACE_ANON_SIZE = "runtime_native";
    private static final int KEY_ASSISTANT = 2;
    private static final int KEY_CAMERA = 0;
    private static final int KEY_HOME = 1;
    private static final int MATCH_FLAGS = 851968;
    private static final long MAX_ANON_SIZE = 2147483648L;
    private static final int MAX_ASSISTANT_PIN_SIZE = 62914560;
    private static final int MAX_CAMERA_PIN_SIZE = 83886080;
    private static final java.lang.String PIN_META_FILENAME = "pinlist.meta";
    private static final java.lang.String TAG = "PinnerService";
    private final android.app.IActivityManager mAm;
    private final android.app.ActivityManagerInternal mAmInternal;
    private final com.android.server.wm.ActivityTaskManagerInternal mAtmInternal;
    private com.android.server.PinnerService.BinderService mBinderService;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final int mConfiguredHomePinBytes;
    private final boolean mConfiguredToPinAssistant;
    private final boolean mConfiguredToPinCamera;
    private final int mConfiguredWebviewPinBytes;
    private final android.content.Context mContext;
    private long mCurrentlyPinnedAnonSize;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mDeviceConfigAnonSizeListener;
    private final android.provider.DeviceConfigInterface mDeviceConfigInterface;
    private final com.android.server.PinnerService.Injector mInjector;
    private final android.util.ArrayMap<java.lang.Integer, java.lang.Integer> mPendingRepin;
    private long mPinAnonAddress;
    private long mPinAnonSize;
    private android.util.ArraySet<java.lang.Integer> mPinKeys;
    private final android.util.ArrayMap<java.lang.Integer, com.android.server.PinnerService.PinnedApp> mPinnedApps;
    private final android.util.ArrayMap<java.lang.String, com.android.server.PinnerService.PinnedFile> mPinnedFiles;
    private com.android.server.PinnerService.PinnerHandler mPinnerHandler;
    public com.android.server.IPinnerServiceExt mPinnerServiceExt;
    private final android.os.UserManager mUserManager;
    private static final int PAGE_SIZE = (int) android.system.Os.sysconf(android.system.OsConstants._SC_PAGESIZE);
    private static boolean PROP_PIN_PINLIST = android.os.SystemProperties.getBoolean("pinner.use_pinlist", true);
    private static final long DEFAULT_ANON_SIZE = android.os.SystemProperties.getLong("pinner.pin_shared_anon_size", 0);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AppKey {
    }

    static class Injector {
        Injector() {
        }

        protected android.provider.DeviceConfigInterface getDeviceConfigInterface() {
            return android.provider.DeviceConfigInterface.REAL;
        }

        protected void publishBinderService(com.android.server.PinnerService service, android.os.Binder binderService) {
            service.publishBinderService("pinner", binderService);
        }

        protected com.android.server.PinnerService.PinnedFile pinFileInternal(java.lang.String fileToPin, int maxBytesToPin, boolean attemptPinIntrospection) {
            return com.android.server.PinnerService.pinFileInternal(fileToPin, maxBytesToPin, attemptPinIntrospection);
        }
    }

    public PinnerService(android.content.Context context) {
        this(context, new com.android.server.PinnerService.Injector());
    }

    PinnerService(android.content.Context context, com.android.server.PinnerService.Injector injector) {
        super(context);
        this.mPinnedFiles = new android.util.ArrayMap<>();
        this.mPinnedApps = new android.util.ArrayMap<>();
        this.mPendingRepin = new android.util.ArrayMap<>();
        this.mPinnerHandler = null;
        this.mPinnerServiceExt = (com.android.server.IPinnerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.IPinnerServiceExt.class).create();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.PinnerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
                    android.net.Uri packageUri = intent.getData();
                    java.lang.String packageName = packageUri.getSchemeSpecificPart();
                    android.util.ArraySet<java.lang.String> updatedPackages = new android.util.ArraySet<>();
                    updatedPackages.add(packageName);
                    com.android.server.PinnerService.this.update(updatedPackages, true);
                }
            }
        };
        this.mDeviceConfigAnonSizeListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.PinnerService.2
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) throws java.lang.Exception {
                if (com.android.server.PinnerService.DEVICE_CONFIG_NAMESPACE_ANON_SIZE.equals(properties.getNamespace()) && properties.getKeyset().contains(com.android.server.PinnerService.DEVICE_CONFIG_KEY_ANON_SIZE)) {
                    com.android.server.PinnerService.this.refreshPinAnonConfig();
                }
            }
        };
        this.mContext = context;
        this.mInjector = injector;
        this.mDeviceConfigInterface = this.mInjector.getDeviceConfigInterface();
        this.mConfiguredToPinCamera = context.getResources().getBoolean(android.R.bool.config_navBarCanMove);
        this.mConfiguredHomePinBytes = context.getResources().getInteger(android.R.integer.config_notificationsBatteryLowARGB);
        this.mConfiguredToPinAssistant = context.getResources().getBoolean(android.R.bool.config_navBarAlwaysShowOnSideEdgeGesture);
        this.mConfiguredWebviewPinBytes = context.getResources().getInteger(android.R.integer.config_notificationsBatteryLowBehavior);
        this.mPinKeys = createPinKeys();
        this.mPinnerHandler = new com.android.server.PinnerService.PinnerHandler(com.android.internal.os.BackgroundThread.get().getLooper());
        this.mAtmInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mAm = android.app.ActivityManager.getService();
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        registerUidListener();
        registerUserSetupCompleteListener();
        this.mDeviceConfigInterface.addOnPropertiesChangedListener(DEVICE_CONFIG_NAMESPACE_ANON_SIZE, new android.os.HandlerExecutor(this.mPinnerHandler), this.mDeviceConfigAnonSizeListener);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.SystemService
    public void onStart() {
        this.mBinderService = new com.android.server.PinnerService.BinderService();
        this.mInjector.publishBinderService(this, this.mBinderService);
        publishLocalService(com.android.server.PinnerService.class, this);
        this.mPinnerHandler.obtainMessage(4001).sendToTarget();
        sendPinAppsMessage(0);
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        int userId = to.getUserIdentifier();
        if (!this.mUserManager.isManagedProfile(userId)) {
            sendPinAppsMessage(userId);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        int userId = user.getUserIdentifier();
        if (userId != 0 && !this.mUserManager.isManagedProfile(userId)) {
            sendPinAppsMessage(userId);
        }
    }

    public void update(android.util.ArraySet<java.lang.String> updatedPackages, boolean force) {
        android.util.ArraySet<java.lang.Integer> pinKeys = getPinKeys();
        int currentUser = android.app.ActivityManager.getCurrentUser();
        for (int i = pinKeys.size() - 1; i >= 0; i--) {
            int key = pinKeys.valueAt(i).intValue();
            android.content.pm.ApplicationInfo info = getInfoForKey(key, currentUser);
            if (info != null && updatedPackages.contains(info.packageName)) {
                android.util.Slog.i(TAG, "Updating pinned files for " + info.packageName + " force=" + force);
                sendPinAppMessage(key, currentUser, force);
            }
        }
        this.mPinnerServiceExt.updateExt(updatedPackages, force);
    }

    public java.util.List<com.android.server.PinnerService.PinnedFileStats> dumpDataForStatsd() {
        java.util.List<com.android.server.PinnerService.PinnedFileStats> pinnedFileStats = new java.util.ArrayList<>();
        synchronized (this) {
            for (com.android.server.PinnerService.PinnedFile pinnedFile : this.mPinnedFiles.values()) {
                pinnedFileStats.add(new com.android.server.PinnerService.PinnedFileStats(1000, pinnedFile));
            }
            java.util.Iterator<java.lang.Integer> it = this.mPinnedApps.keySet().iterator();
            while (it.hasNext()) {
                int key = it.next().intValue();
                com.android.server.PinnerService.PinnedApp app = this.mPinnedApps.get(java.lang.Integer.valueOf(key));
                for (com.android.server.PinnerService.PinnedFile pinnedFile2 : this.mPinnedApps.get(java.lang.Integer.valueOf(key)).mFiles) {
                    pinnedFileStats.add(new com.android.server.PinnerService.PinnedFileStats(app.uid, pinnedFile2));
                }
            }
        }
        return pinnedFileStats;
    }

    public static class PinnedFileStats {
        public final java.lang.String filename;
        public final int sizeKb;
        public final int uid;

        protected PinnedFileStats(int uid, com.android.server.PinnerService.PinnedFile file) {
            this.uid = uid;
            this.filename = file.fileName.substring(file.fileName.lastIndexOf(47) + 1);
            this.sizeKb = file.bytesPinned / 1024;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePinOnStart() throws java.lang.Exception {
        java.lang.String[] filesToPin = this.mPinnerServiceExt.replaceDefaultFiles(this.mContext.getResources().getStringArray(android.R.array.config_defaultImperceptibleKillingExemptionPkgs));
        for (java.lang.String fileToPin : filesToPin) {
            com.android.server.PinnerService.PinnedFile pf = this.mInjector.pinFileInternal(fileToPin, Integer.MAX_VALUE, false);
            if (pf == null) {
                android.util.Slog.e(TAG, "Failed to pin file = " + fileToPin);
            } else {
                synchronized (this) {
                    this.mPinnedFiles.put(pf.fileName, pf);
                }
                pf.groupName = "system";
                pinOptimizedDexDependencies(pf, Integer.MAX_VALUE, null);
            }
        }
        refreshPinAnonConfig();
    }

    private void registerUserSetupCompleteListener() {
        final android.net.Uri userSetupCompleteUri = android.provider.Settings.Secure.getUriFor("user_setup_complete");
        this.mContext.getContentResolver().registerContentObserver(userSetupCompleteUri, false, new android.database.ContentObserver(null) { // from class: com.android.server.PinnerService.3
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, android.net.Uri uri) {
                if (userSetupCompleteUri.equals(uri) && com.android.server.PinnerService.this.mConfiguredHomePinBytes > 0) {
                    com.android.server.PinnerService.this.sendPinAppMessage(1, android.app.ActivityManager.getCurrentUser(), true);
                }
            }
        }, -1);
    }

    /* JADX INFO: renamed from: com.android.server.PinnerService$4, reason: invalid class name */
    class AnonymousClass4 extends android.app.UidObserver {
        AnonymousClass4() {
        }

        public void onUidGone(int uid, boolean disabled) {
            com.android.server.PinnerService.this.mPinnerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.PinnerService$4$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.PinnerService) obj).handleUidGone(((java.lang.Integer) obj2).intValue());
                }
            }, com.android.server.PinnerService.this, java.lang.Integer.valueOf(uid)));
        }

        public void onUidActive(int uid) {
            com.android.server.PinnerService.this.mPinnerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.PinnerService$4$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.PinnerService) obj).handleUidActive(((java.lang.Integer) obj2).intValue());
                }
            }, com.android.server.PinnerService.this, java.lang.Integer.valueOf(uid)));
        }
    }

    private void registerUidListener() {
        try {
            this.mAm.registerUidObserver(new com.android.server.PinnerService.AnonymousClass4(), 10, 0, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to register uid observer", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUidGone(int uid) {
        updateActiveState(uid, false);
        synchronized (this) {
            int key = this.mPendingRepin.getOrDefault(java.lang.Integer.valueOf(uid), -1).intValue();
            if (key == -1) {
                return;
            }
            this.mPendingRepin.remove(java.lang.Integer.valueOf(uid));
            pinApp(key, android.app.ActivityManager.getCurrentUser(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUidActive(int uid) {
        updateActiveState(uid, true);
    }

    private void updateActiveState(int uid, boolean active) {
        synchronized (this) {
            for (int i = this.mPinnedApps.size() - 1; i >= 0; i--) {
                com.android.server.PinnerService.PinnedApp app = this.mPinnedApps.valueAt(i);
                if (app.uid == uid) {
                    app.active = active;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unpinApps() {
        android.util.ArraySet<java.lang.Integer> pinKeys = getPinKeys();
        for (int i = pinKeys.size() - 1; i >= 0; i--) {
            int key = pinKeys.valueAt(i).intValue();
            unpinApp(key);
        }
    }

    private void unpinApp(int key) {
        synchronized (this) {
            com.android.server.PinnerService.PinnedApp app = this.mPinnedApps.get(java.lang.Integer.valueOf(key));
            if (app == null) {
                return;
            }
            this.mPinnedApps.remove(java.lang.Integer.valueOf(key));
            java.util.ArrayList<com.android.server.PinnerService.PinnedFile> pinnedAppFiles = new java.util.ArrayList<>(app.mFiles);
            for (com.android.server.PinnerService.PinnedFile pinnedFile : pinnedAppFiles) {
                unpinFile(pinnedFile.fileName);
            }
        }
    }

    private boolean isResolverActivity(android.content.pm.ActivityInfo info) {
        return com.android.internal.app.ResolverActivity.class.getName().equals(info.name);
    }

    public int getWebviewPinQuota() {
        if (!com.android.server.flags.Flags.pinWebview()) {
            return 0;
        }
        int quota = this.mConfiguredWebviewPinBytes;
        int overrideQuota = android.os.SystemProperties.getInt("pinner.pin_webview_size", -1);
        if (overrideQuota != -1) {
            return overrideQuota;
        }
        return quota;
    }

    private android.content.pm.ApplicationInfo getCameraInfo(int userHandle) {
        android.content.Intent cameraIntent = new android.content.Intent("android.media.action.STILL_IMAGE_CAMERA");
        android.content.pm.ApplicationInfo info = getApplicationInfoForIntent(cameraIntent, userHandle, false);
        if (info == null) {
            android.content.Intent cameraIntent2 = new android.content.Intent("android.media.action.STILL_IMAGE_CAMERA_SECURE");
            info = getApplicationInfoForIntent(cameraIntent2, userHandle, false);
        }
        if (info == null) {
            android.content.Intent cameraIntent3 = new android.content.Intent("android.media.action.STILL_IMAGE_CAMERA");
            return getApplicationInfoForIntent(cameraIntent3, userHandle, true);
        }
        return info;
    }

    private android.content.pm.ApplicationInfo getHomeInfo(int userHandle) {
        android.content.Intent intent = this.mAtmInternal.getHomeIntent();
        return getApplicationInfoForIntent(intent, userHandle, false);
    }

    private android.content.pm.ApplicationInfo getAssistantInfo(int userHandle) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.ASSIST");
        return getApplicationInfoForIntent(intent, userHandle, true);
    }

    private android.content.pm.ApplicationInfo getApplicationInfoForIntent(android.content.Intent intent, int userHandle, boolean defaultToSystemApp) {
        android.content.pm.ResolveInfo resolveInfo;
        if (intent == null || (resolveInfo = this.mContext.getPackageManager().resolveActivityAsUser(intent, MATCH_FLAGS, userHandle)) == null) {
            return null;
        }
        if (!isResolverActivity(resolveInfo.activityInfo)) {
            return resolveInfo.activityInfo.applicationInfo;
        }
        if (!defaultToSystemApp) {
            return null;
        }
        java.util.List<android.content.pm.ResolveInfo> infoList = this.mContext.getPackageManager().queryIntentActivitiesAsUser(intent, MATCH_FLAGS, userHandle);
        android.content.pm.ApplicationInfo systemAppInfo = null;
        for (android.content.pm.ResolveInfo info : infoList) {
            if ((info.activityInfo.applicationInfo.flags & 1) != 0) {
                if (systemAppInfo != null) {
                    return null;
                }
                systemAppInfo = info.activityInfo.applicationInfo;
            }
        }
        return systemAppInfo;
    }

    private void sendPinAppsMessage(int userHandle) {
        this.mPinnerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.PinnerService) obj).pinApps(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(userHandle)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPinAppsWithUpdatedKeysMessage(int userHandle) {
        this.mPinnerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.PinnerService) obj).pinAppsWithUpdatedKeys(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(userHandle)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUnpinAppsMessage() {
        this.mPinnerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.PinnerService) obj).unpinApps();
            }
        }, this));
    }

    private android.util.ArraySet<java.lang.Integer> createPinKeys() {
        android.util.ArraySet<java.lang.Integer> pinKeys = new android.util.ArraySet<>();
        boolean shouldPinCamera = this.mConfiguredToPinCamera && this.mDeviceConfigInterface.getBoolean("runtime_native_boot", "pin_camera", android.os.SystemProperties.getBoolean("pinner.pin_camera", true));
        if (shouldPinCamera) {
            pinKeys.add(0);
        }
        if (this.mConfiguredHomePinBytes > 0) {
            pinKeys.add(1);
        }
        if (this.mConfiguredToPinAssistant) {
            pinKeys.add(2);
        }
        return pinKeys;
    }

    private synchronized android.util.ArraySet<java.lang.Integer> getPinKeys() {
        return this.mPinKeys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pinApps(int userHandle) {
        pinAppsInternal(userHandle, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pinAppsWithUpdatedKeys(int userHandle) {
        pinAppsInternal(userHandle, true);
    }

    private void pinAppsInternal(int userHandle, boolean updateKeys) {
        if (updateKeys) {
            android.util.ArraySet<java.lang.Integer> newKeys = createPinKeys();
            synchronized (this) {
                if (!this.mPinnedApps.isEmpty()) {
                    android.util.Slog.e(TAG, "Attempted to update a list of apps, but apps were already pinned. Skipping.");
                    return;
                }
                this.mPinKeys = newKeys;
            }
        }
        android.util.ArraySet<java.lang.Integer> currentPinKeys = getPinKeys();
        for (int i = currentPinKeys.size() - 1; i >= 0; i--) {
            int key = currentPinKeys.valueAt(i).intValue();
            pinApp(key, userHandle, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPinAppMessage(int key, int userHandle, boolean force) {
        this.mPinnerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda4
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                ((com.android.server.PinnerService) obj).pinApp(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Boolean) obj4).booleanValue());
            }
        }, this, java.lang.Integer.valueOf(key), java.lang.Integer.valueOf(userHandle), java.lang.Boolean.valueOf(force)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pinApp(int key, int userHandle, boolean force) {
        int uid = getUidForKey(key);
        if (!force && uid != -1) {
            synchronized (this) {
                this.mPendingRepin.put(java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(key));
            }
            return;
        }
        unpinApp(key);
        android.content.pm.ApplicationInfo info = getInfoForKey(key, userHandle);
        if (info != null) {
            pinApp(key, info);
        }
    }

    private int getUidForKey(int key) {
        int i;
        synchronized (this) {
            com.android.server.PinnerService.PinnedApp existing = this.mPinnedApps.get(java.lang.Integer.valueOf(key));
            if (existing != null && existing.active) {
                i = existing.uid;
            } else {
                i = -1;
            }
        }
        return i;
    }

    private android.content.pm.ApplicationInfo getInfoForKey(int key, int userHandle) {
        switch (key) {
            case 0:
                return getCameraInfo(userHandle);
            case 1:
                return getHomeInfo(userHandle);
            case 2:
                return getAssistantInfo(userHandle);
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getNameForKey(int key) {
        switch (key) {
            case 0:
                return "Camera";
            case 1:
                return "Home";
            case 2:
                return "Assistant";
            default:
                return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshPinAnonConfig() throws java.lang.Exception {
        long newPinAnonSize = java.lang.Math.max(0L, java.lang.Math.min(this.mDeviceConfigInterface.getLong(DEVICE_CONFIG_NAMESPACE_ANON_SIZE, DEVICE_CONFIG_KEY_ANON_SIZE, DEFAULT_ANON_SIZE), MAX_ANON_SIZE));
        if (newPinAnonSize != this.mPinAnonSize) {
            this.mPinAnonSize = newPinAnonSize;
            pinAnonRegion();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x014b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void pinAnonRegion() throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 335
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.PinnerService.pinAnonRegion():void");
    }

    private void unpinAnonRegion() {
        if (this.mPinAnonAddress != 0) {
            safeMunmap(this.mPinAnonAddress, this.mCurrentlyPinnedAnonSize);
        }
        this.mPinAnonAddress = 0L;
        this.mCurrentlyPinnedAnonSize = 0L;
    }

    private int getSizeLimitForKey(int key) {
        switch (key) {
            case 0:
                return 83886080;
            case 1:
                return this.mConfiguredHomePinBytes;
            case 2:
                return MAX_ASSISTANT_PIN_SIZE;
            default:
                return 0;
        }
    }

    private void pinApp(int key, android.content.pm.ApplicationInfo appInfo) {
        if (appInfo == null) {
            return;
        }
        com.android.server.PinnerService.PinnedApp pinnedApp = new com.android.server.PinnerService.PinnedApp(appInfo);
        synchronized (this) {
            this.mPinnedApps.put(java.lang.Integer.valueOf(key), pinnedApp);
        }
        int pinSizeLimit = getSizeLimitForKey(key);
        java.util.List<java.lang.String> apks = new java.util.ArrayList<>();
        apks.add(appInfo.sourceDir);
        boolean shouldSkipArtPins = false;
        if (appInfo.splitSourceDirs != null) {
            for (java.lang.String splitApk : appInfo.splitSourceDirs) {
                apks.add(splitApk);
            }
        }
        int apkPinSizeLimit = pinSizeLimit;
        if (key == 1 && com.android.server.flags.Flags.skipHomeArtPins()) {
            shouldSkipArtPins = true;
        }
        for (java.lang.String apk : apks) {
            if (apkPinSizeLimit > 0) {
                com.android.server.PinnerService.PinnedFile pf = this.mInjector.pinFileInternal(apk, apkPinSizeLimit, true);
                if (pf == null) {
                    android.util.Slog.e(TAG, "Failed to pin " + apk);
                } else {
                    pf.groupName = getNameForKey(key);
                    synchronized (this) {
                        pinnedApp.mFiles.add(pf);
                        this.mPinnedFiles.put(pf.fileName, pf);
                    }
                    apkPinSizeLimit -= pf.bytesPinned;
                    if (apk.equals(appInfo.sourceDir) && !shouldSkipArtPins) {
                        pinOptimizedDexDependencies(pf, Integer.MAX_VALUE, appInfo);
                    }
                }
            } else {
                android.util.Slog.w(TAG, "Reached to the pin size limit. Skipping: " + apk);
            }
        }
    }

    public int pinFile(java.lang.String fileToPin, int maxBytesToPin, android.content.pm.ApplicationInfo appInfo, java.lang.String groupName) {
        com.android.server.PinnerService.PinnedFile existingPin;
        synchronized (this) {
            existingPin = this.mPinnedFiles.get(fileToPin);
        }
        if (existingPin != null) {
            if (existingPin.bytesPinned == maxBytesToPin) {
                return 0;
            }
            unpinFile(fileToPin);
        }
        boolean isApk = fileToPin.endsWith(".apk");
        com.android.server.PinnerService.PinnedFile pf = this.mInjector.pinFileInternal(fileToPin, maxBytesToPin, isApk);
        if (pf == null) {
            android.util.Slog.e(TAG, "Failed to pin file = " + fileToPin);
            return 0;
        }
        pf.groupName = groupName != null ? groupName : "";
        int bytesPinned = 0 + pf.bytesPinned;
        int maxBytesToPin2 = maxBytesToPin - bytesPinned;
        synchronized (this) {
            this.mPinnedFiles.put(pf.fileName, pf);
        }
        if (maxBytesToPin2 > 0) {
            pinOptimizedDexDependencies(pf, maxBytesToPin2, appInfo);
        }
        return bytesPinned;
    }

    private int pinOptimizedDexDependencies(com.android.server.PinnerService.PinnedFile pinnedFile, int maxBytesToPin, android.content.pm.ApplicationInfo appInfo) {
        if (pinnedFile == null) {
            return 0;
        }
        int bytesPinned = 0;
        if (pinnedFile.fileName.endsWith(".jar") | pinnedFile.fileName.endsWith(".apk")) {
            java.lang.String abi = null;
            if (appInfo != null) {
                abi = appInfo.primaryCpuAbi;
            }
            if (abi == null) {
                abi = android.os.Build.SUPPORTED_ABIS[0];
            }
            java.lang.String arch = dalvik.system.VMRuntime.getInstructionSet(abi);
            java.lang.String[] files = null;
            try {
                files = dalvik.system.DexFile.getDexFileOutputPaths(pinnedFile.fileName, arch);
            } catch (java.io.IOException e) {
            }
            if (files == null) {
                return 0;
            }
            int length = files.length;
            int i = 0;
            while (i < length) {
                java.lang.String file = files[i];
                unpinFile(file);
                com.android.server.PinnerService.PinnedFile df = this.mInjector.pinFileInternal(file, maxBytesToPin, false);
                if (df == null) {
                    android.util.Slog.i(TAG, "Failed to pin ART file = " + file);
                    return bytesPinned;
                }
                df.groupName = pinnedFile.groupName;
                pinnedFile.pinnedDeps.add(df);
                int maxBytesToPin2 = maxBytesToPin - df.bytesPinned;
                int maxBytesToPin3 = df.bytesPinned;
                int bytesPinned2 = bytesPinned + maxBytesToPin3;
                synchronized (this) {
                    this.mPinnedFiles.put(df.fileName, df);
                }
                i++;
                maxBytesToPin = maxBytesToPin2;
                bytesPinned = bytesPinned2;
            }
        }
        return bytesPinned;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.PinnerService.PinnedFile pinFileInternal(java.lang.String fileToPin, int maxBytesToPin, boolean attemptPinIntrospection) {
        com.android.server.PinnerService.PinRangeSource pinRangeSource;
        java.util.zip.ZipFile fileAsZip = null;
        java.io.InputStream pinRangeStream = null;
        if (attemptPinIntrospection) {
            try {
                fileAsZip = maybeOpenZip(fileToPin);
            } catch (java.lang.Throwable th) {
                safeClose(pinRangeStream);
                safeClose(fileAsZip);
                throw th;
            }
        }
        if (fileAsZip != null) {
            pinRangeStream = maybeOpenPinMetaInZip(fileAsZip, fileToPin);
        }
        boolean use_pinlist = pinRangeStream != null;
        if (use_pinlist) {
            pinRangeSource = new com.android.server.PinnerService.PinRangeSourceStream(pinRangeStream);
        } else {
            pinRangeSource = new com.android.server.PinnerService.PinRangeSourceStatic(0, Integer.MAX_VALUE);
        }
        com.android.server.PinnerService.PinnedFile pinnedFile = pinFileRanges(fileToPin, maxBytesToPin, pinRangeSource);
        if (pinnedFile != null) {
            pinnedFile.used_pinlist = use_pinlist;
        }
        safeClose(pinRangeStream);
        safeClose(fileAsZip);
        return pinnedFile;
    }

    private static java.util.zip.ZipFile maybeOpenZip(java.lang.String fileName) {
        try {
            java.util.zip.ZipFile zip = new java.util.zip.ZipFile(fileName);
            return zip;
        } catch (java.io.IOException ex) {
            android.util.Slog.w(TAG, java.lang.String.format("could not open \"%s\" as zip: pinning as blob", fileName), ex);
            return null;
        }
    }

    private static java.io.InputStream maybeOpenPinMetaInZip(java.util.zip.ZipFile zipFile, java.lang.String fileName) {
        if (!PROP_PIN_PINLIST) {
            return null;
        }
        java.util.zip.ZipEntry pinMetaEntry = zipFile.getEntry(PIN_META_FILENAME);
        if (pinMetaEntry == null) {
            pinMetaEntry = zipFile.getEntry("assets/pinlist.meta");
        }
        if (pinMetaEntry != null) {
            try {
                java.io.InputStream pinMetaStream = zipFile.getInputStream(pinMetaEntry);
                return pinMetaStream;
            } catch (java.io.IOException ex) {
                android.util.Slog.w(TAG, java.lang.String.format("error reading pin metadata \"%s\": pinning as blob", fileName), ex);
                return null;
            }
        }
        android.util.Slog.w(TAG, java.lang.String.format("Could not find pinlist.meta for \"%s\": pinning as blob", fileName));
        return null;
    }

    private static abstract class PinRangeSource {
        abstract boolean read(com.android.server.PinnerService.PinRange pinRange);

        private PinRangeSource() {
        }
    }

    private static final class PinRangeSourceStatic extends com.android.server.PinnerService.PinRangeSource {
        private boolean mDone;
        private final int mPinLength;
        private final int mPinStart;

        PinRangeSourceStatic(int pinStart, int pinLength) {
            super();
            this.mDone = false;
            this.mPinStart = pinStart;
            this.mPinLength = pinLength;
        }

        @Override // com.android.server.PinnerService.PinRangeSource
        boolean read(com.android.server.PinnerService.PinRange outPinRange) {
            outPinRange.start = this.mPinStart;
            outPinRange.length = this.mPinLength;
            boolean done = this.mDone;
            this.mDone = true;
            return !done;
        }
    }

    private static final class PinRangeSourceStream extends com.android.server.PinnerService.PinRangeSource {
        private boolean mDone;
        private final java.io.DataInputStream mStream;

        PinRangeSourceStream(java.io.InputStream stream) {
            super();
            this.mDone = false;
            this.mStream = new java.io.DataInputStream(stream);
        }

        @Override // com.android.server.PinnerService.PinRangeSource
        boolean read(com.android.server.PinnerService.PinRange outPinRange) {
            if (!this.mDone) {
                try {
                    outPinRange.start = this.mStream.readInt();
                    outPinRange.length = this.mStream.readInt();
                } catch (java.io.IOException e) {
                    this.mDone = true;
                }
            }
            return !this.mDone;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x016d  */
    /* JADX WARN: Removed duplicated region for block: B:96:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.android.server.PinnerService.PinnedFile pinFileRanges(java.lang.String r19, int r20, com.android.server.PinnerService.PinRangeSource r21) throws android.system.ErrnoException {
        /*
            Method dump skipped, instruction units count: 370
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.PinnerService.pinFileRanges(java.lang.String, int, com.android.server.PinnerService$PinRangeSource):com.android.server.PinnerService$PinnedFile");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<com.android.server.PinnerService.PinnedFile> getAllPinsForGroup(final java.lang.String group) {
        java.util.List<com.android.server.PinnerService.PinnedFile> filesInGroup;
        synchronized (this) {
            filesInGroup = this.mPinnedFiles.values().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.PinnerService.PinnedFile) obj).groupName.equals(group);
                }
            }).toList();
        }
        return filesInGroup;
    }

    public void unpinGroup(java.lang.String group) {
        java.util.List<com.android.server.PinnerService.PinnedFile> pinnedFiles = getAllPinsForGroup(group);
        for (com.android.server.PinnerService.PinnedFile pf : pinnedFiles) {
            unpinFile(pf.fileName);
        }
    }

    public void unpinFile(java.lang.String filename) {
        com.android.server.PinnerService.PinnedFile pinnedFile;
        synchronized (this) {
            pinnedFile = this.mPinnedFiles.get(filename);
        }
        if (pinnedFile == null) {
            return;
        }
        pinnedFile.close();
        synchronized (this) {
            this.mPinnedFiles.remove(pinnedFile.fileName);
            for (com.android.server.PinnerService.PinnedFile dep : pinnedFile.pinnedDeps) {
                if (dep != null) {
                    this.mPinnedFiles.remove(dep.fileName);
                }
            }
        }
    }

    private static int clamp(int min, int value, int max) {
        return java.lang.Math.max(min, java.lang.Math.min(value, max));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void safeMunmap(long address, long mapSize) {
        try {
            android.system.Os.munmap(address, mapSize);
        } catch (android.system.ErrnoException ex) {
            android.util.Slog.w(TAG, "ignoring error in unmap", ex);
        }
    }

    private static void safeClose(java.io.FileDescriptor fd) {
        if (fd != null && fd.valid()) {
            try {
                android.system.Os.close(fd);
            } catch (android.system.ErrnoException ex) {
                if (ex.errno == android.system.OsConstants.EBADF) {
                    throw new java.lang.AssertionError(ex);
                }
            }
        }
    }

    private static void safeClose(java.io.Closeable thing) {
        if (thing != null) {
            try {
                thing.close();
            } catch (java.io.IOException ex) {
                android.util.Slog.w(TAG, "ignoring error closing resource: " + thing, ex);
            }
        }
    }

    public java.util.List<android.app.pinner.PinnedFileStat> getPinnerStats() {
        java.util.Collection<com.android.server.PinnerService.PinnedFile> pinnedFiles;
        java.util.ArrayList<android.app.pinner.PinnedFileStat> stats = new java.util.ArrayList<>();
        synchronized (this) {
            pinnedFiles = this.mPinnedFiles.values();
        }
        for (com.android.server.PinnerService.PinnedFile pf : pinnedFiles) {
            android.app.pinner.PinnedFileStat stat = new android.app.pinner.PinnedFileStat(pf.fileName, pf.bytesPinned, pf.groupName);
            stats.add(stat);
        }
        if (this.mCurrentlyPinnedAnonSize > 0) {
            stats.add(new android.app.pinner.PinnedFileStat(ANON_REGION_STAT_NAME, this.mCurrentlyPinnedAnonSize, ANON_REGION_STAT_NAME));
        }
        return stats;
    }

    public final class BinderService extends android.app.pinner.IPinnerService.Stub {
        public BinderService() {
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) throws java.lang.Throwable {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.PinnerService.this.mContext, com.android.server.PinnerService.TAG, pw)) {
                return;
            }
            java.util.HashSet<com.android.server.PinnerService.PinnedFile> shownPins = new java.util.HashSet<>();
            java.util.HashSet<java.lang.String> groups = new java.util.HashSet<>();
            int bytesPerMB = 1048576;
            synchronized (com.android.server.PinnerService.this) {
                long totalSize = 0;
                try {
                    try {
                        java.util.Iterator it = com.android.server.PinnerService.this.mPinnedApps.keySet().iterator();
                        while (it.hasNext()) {
                            try {
                                int key = ((java.lang.Integer) it.next()).intValue();
                                com.android.server.PinnerService.PinnedApp app = (com.android.server.PinnerService.PinnedApp) com.android.server.PinnerService.this.mPinnedApps.get(java.lang.Integer.valueOf(key));
                                pw.print(com.android.server.PinnerService.this.getNameForKey(key));
                                pw.print(" uid=");
                                pw.print(app.uid);
                                pw.print(" active=");
                                pw.print(app.active);
                                pw.println();
                                for (com.android.server.PinnerService.PinnedFile pf : ((com.android.server.PinnerService.PinnedApp) com.android.server.PinnerService.this.mPinnedApps.get(java.lang.Integer.valueOf(key))).mFiles) {
                                    pw.print("  ");
                                    java.util.Iterator it2 = it;
                                    int bytesPerMB2 = bytesPerMB;
                                    try {
                                        pw.format("%s pinned:%d bytes (%d MB) pinlist:%b\n", pf.fileName, java.lang.Integer.valueOf(pf.bytesPinned), java.lang.Integer.valueOf(pf.bytesPinned / 1048576), java.lang.Boolean.valueOf(pf.used_pinlist));
                                        totalSize += (long) pf.bytesPinned;
                                        shownPins.add(pf);
                                        java.util.Iterator<com.android.server.PinnerService.PinnedFile> it3 = pf.pinnedDeps.iterator();
                                        while (it3.hasNext()) {
                                            com.android.server.PinnerService.PinnedFile dep = it3.next();
                                            pw.print("  ");
                                            java.util.Iterator<com.android.server.PinnerService.PinnedFile> it4 = it3;
                                            int key2 = key;
                                            pw.format("%s pinned:%d bytes (%d MB) pinlist:%b (Dependency)\n", dep.fileName, java.lang.Integer.valueOf(dep.bytesPinned), java.lang.Integer.valueOf(dep.bytesPinned / 1048576), java.lang.Boolean.valueOf(dep.used_pinlist));
                                            totalSize += (long) dep.bytesPinned;
                                            shownPins.add(dep);
                                            it3 = it4;
                                            key = key2;
                                        }
                                        it = it2;
                                        bytesPerMB = bytesPerMB2;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        throw th;
                                    }
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                    try {
                        pw.println();
                        for (com.android.server.PinnerService.PinnedFile pinnedFile : com.android.server.PinnerService.this.mPinnedFiles.values()) {
                            if (!groups.contains(pinnedFile.groupName)) {
                                groups.add(pinnedFile.groupName);
                            }
                        }
                        boolean firstPinInGroup = true;
                        for (java.lang.String group : groups) {
                            java.util.List<com.android.server.PinnerService.PinnedFile> groupPins = com.android.server.PinnerService.this.getAllPinsForGroup(group);
                            for (com.android.server.PinnerService.PinnedFile pinnedFile2 : groupPins) {
                                if (!shownPins.contains(pinnedFile2)) {
                                    if (firstPinInGroup) {
                                        firstPinInGroup = false;
                                        pw.print("Group:" + group);
                                        pw.println();
                                    }
                                    boolean firstPinInGroup2 = firstPinInGroup;
                                    java.util.HashSet<com.android.server.PinnerService.PinnedFile> shownPins2 = shownPins;
                                    pw.format("  %s pinned:%d bytes (%d MB) pinlist:%b\n", pinnedFile2.fileName, java.lang.Integer.valueOf(pinnedFile2.bytesPinned), java.lang.Integer.valueOf(pinnedFile2.bytesPinned / 1048576), java.lang.Boolean.valueOf(pinnedFile2.used_pinlist));
                                    totalSize += (long) pinnedFile2.bytesPinned;
                                    firstPinInGroup = firstPinInGroup2;
                                    shownPins = shownPins2;
                                }
                            }
                        }
                        pw.println();
                        if (com.android.server.PinnerService.this.mPinAnonAddress != 0) {
                            pw.format("Pinned anon region: %d (%d MB)\n", java.lang.Long.valueOf(com.android.server.PinnerService.this.mCurrentlyPinnedAnonSize), java.lang.Long.valueOf(com.android.server.PinnerService.this.mCurrentlyPinnedAnonSize / 1048576));
                            totalSize += com.android.server.PinnerService.this.mCurrentlyPinnedAnonSize;
                        }
                        pw.format("Total pinned: %s bytes (%s MB)\n", java.lang.Long.valueOf(totalSize), java.lang.Long.valueOf(totalSize / 1048576));
                        pw.println();
                        if (!com.android.server.PinnerService.this.mPendingRepin.isEmpty()) {
                            pw.print("Pending repin: ");
                            java.util.Iterator it5 = com.android.server.PinnerService.this.mPendingRepin.values().iterator();
                            while (it5.hasNext()) {
                                pw.print(com.android.server.PinnerService.this.getNameForKey(((java.lang.Integer) it5.next()).intValue()));
                                pw.print(' ');
                            }
                            pw.println();
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                        throw th;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            }
        }

        private void repin() {
            com.android.server.PinnerService.this.sendUnpinAppsMessage();
            com.android.server.PinnerService.this.sendPinAppsWithUpdatedKeysMessage(0);
        }

        private void printError(java.io.FileDescriptor out, java.lang.String message) {
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileOutputStream(out));
            writer.println(message);
            writer.flush();
        }

        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            byte b;
            if (args.length < 1) {
                printError(out, "Command is not given.");
                resultReceiver.send(-1, null);
            }
            java.lang.String command = args[0];
            switch (command.hashCode()) {
                case 108401282:
                    if (command.equals("repin")) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    repin();
                    resultReceiver.send(0, null);
                    break;
                default:
                    printError(out, java.lang.String.format("Unknown pinner command: %s. Supported commands: repin", command));
                    resultReceiver.send(-1, null);
                    break;
            }
        }

        public java.util.List<android.app.pinner.PinnedFileStat> getPinnerStats() {
            getPinnerStats_enforcePermission();
            return com.android.server.PinnerService.this.getPinnerStats();
        }
    }

    public static final class PinnedFile implements java.lang.AutoCloseable {
        final int bytesPinned;
        final java.lang.String fileName;
        private long mAddress;
        final int mapSize;
        boolean used_pinlist;
        java.lang.String groupName = "";
        java.util.ArrayList<com.android.server.PinnerService.PinnedFile> pinnedDeps = new java.util.ArrayList<>();

        PinnedFile(long address, int mapSize, java.lang.String fileName, int bytesPinned) {
            this.mAddress = address;
            this.mapSize = mapSize;
            this.fileName = fileName;
            this.bytesPinned = bytesPinned;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (this.mAddress >= 0) {
                com.android.server.PinnerService.safeMunmap(this.mAddress, this.mapSize);
                this.mAddress = -1L;
            }
            for (com.android.server.PinnerService.PinnedFile dep : this.pinnedDeps) {
                if (dep != null) {
                    dep.close();
                }
            }
        }

        public void finalize() {
            close();
        }
    }

    static final class PinRange {
        int length;
        int start;

        PinRange() {
        }
    }

    private final class PinnedApp {
        boolean active;
        final java.util.ArrayList<com.android.server.PinnerService.PinnedFile> mFiles;
        final int uid;

        private PinnedApp(android.content.pm.ApplicationInfo appInfo) {
            this.mFiles = new java.util.ArrayList<>();
            this.uid = appInfo.uid;
            this.active = com.android.server.PinnerService.this.mAmInternal.isUidActive(this.uid);
        }
    }

    final class PinnerHandler extends android.os.Handler {
        static final int PIN_ONSTART_MSG = 4001;

        public PinnerHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Exception {
            switch (msg.what) {
                case PIN_ONSTART_MSG /* 4001 */:
                    com.android.server.PinnerService.this.handlePinOnStart();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
