package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
public final class GameManagerService extends android.app.IGameManagerService.Stub {
    static final int CANCEL_GAME_LOADING_MODE = 5;
    private static final boolean DEBUG = false;
    private static final java.lang.String EVENT_ON_USER_STARTING = "ON_USER_STARTING";
    private static final java.lang.String EVENT_ON_USER_STOPPING = "ON_USER_STOPPING";
    private static final java.lang.String EVENT_ON_USER_SWITCHING = "ON_USER_SWITCHING";
    private static final java.lang.String EVENT_RECEIVE_SHUTDOWN_INDENT = "RECEIVE_SHUTDOWN_INDENT";
    private static final java.lang.String EVENT_SET_GAME_MODE = "SET_GAME_MODE";
    private static final java.lang.String EVENT_UPDATE_CUSTOM_GAME_MODE_CONFIG = "UPDATE_CUSTOM_GAME_MODE_CONFIG";
    private static final java.lang.String GAME_MODE_INTERVENTION_LIST_FILE_NAME = "game_mode_intervention.list";
    static final int LOADING_BOOST_MAX_DURATION = 5000;
    private static final java.lang.String PACKAGE_NAME_MSG_KEY = "packageName";
    private static final java.lang.String PKG_NAME_START_WITH_COLOROS = "com.coloros.";
    private static final java.lang.String PKG_NAME_START_WITH_HEYTAP = "com.heytap.";
    private static final java.lang.String PKG_NAME_START_WITH_NEARME = "com.nearme.";
    private static final java.lang.String PKG_NAME_START_WITH_OPLUS = "com.oplus.";
    private static final java.lang.String PKG_NAME_START_WITH_OPPO = "com.oppo.";
    static final int POPULATE_GAME_MODE_SETTINGS = 3;
    static final java.lang.String PROPERTY_DEBUG_GFX_GAME_DEFAULT_FRAME_RATE_DISABLED = "debug.graphics.game_default_frame_rate.disabled";
    static final java.lang.String PROPERTY_RO_SURFACEFLINGER_GAME_DEFAULT_FRAME_RATE = "ro.surface_flinger.game_default_frame_rate_override";
    static final int REMOVE_SETTINGS = 2;
    static final int SET_GAME_STATE = 4;
    public static final java.lang.String TAG = "GameManagerService";
    private static final java.lang.String USER_ID_MSG_KEY = "userId";
    static final int WRITE_DELAY_MILLIS = 10000;
    static final int WRITE_GAME_MODE_INTERVENTION_LIST_FILE = 6;
    static final int WRITE_SETTINGS = 1;
    private final android.util.ArrayMap<java.lang.String, com.android.server.app.GameManagerService.GamePackageConfiguration> mConfigs;
    private final android.content.Context mContext;
    private com.android.server.app.GameManagerService.DeviceConfigListener mDeviceConfigListener;
    private final java.lang.Object mDeviceConfigLock;
    private float mGameDefaultFrameRateValue;
    private final java.util.Set<java.lang.Integer> mGameForegroundUids;
    final android.util.AtomicFile mGameModeInterventionListFile;
    private final java.lang.Object mGameModeListenerLock;
    private final android.util.ArrayMap<android.app.IGameModeListener, java.lang.Integer> mGameModeListeners;
    private final com.android.server.app.GameServiceController mGameServiceController;
    public com.android.server.app.IGameManagerServiceExt mGameServiceExt;
    private final java.lang.Object mGameStateListenerLock;
    private final android.util.ArrayMap<android.app.IGameStateListener, java.lang.Integer> mGameStateListeners;
    final android.os.Handler mHandler;
    private final java.lang.Object mLock;
    private final java.util.Set<java.lang.Integer> mNonGameForegroundUids;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.os.PowerManagerInternal mPowerManagerInternal;
    private final android.util.ArrayMap<java.lang.Integer, com.android.server.app.GameManagerSettings> mSettings;
    private final com.android.server.app.GameManagerServiceSystemPropertiesWrapper mSysProps;
    final com.android.server.app.GameManagerService.MyUidObserver mUidObserver;
    private final java.lang.Object mUidObserverLock;
    private final android.os.UserManager mUserManager;

    private static native void nativeSetGameDefaultFrameRateOverride(int i, float f);

    private static native void nativeSetGameModeFrameRateOverride(int i, float f);

    static class Injector {
        Injector() {
        }

        public com.android.server.app.GameManagerServiceSystemPropertiesWrapper createSystemPropertiesWrapper() {
            return new com.android.server.app.GameManagerServiceSystemPropertiesWrapper() { // from class: com.android.server.app.GameManagerService.Injector.1
                @Override // com.android.server.app.GameManagerServiceSystemPropertiesWrapper
                public java.lang.String get(java.lang.String key, java.lang.String def) {
                    return android.os.SystemProperties.get(key, def);
                }

                @Override // com.android.server.app.GameManagerServiceSystemPropertiesWrapper
                public boolean getBoolean(java.lang.String key, boolean def) {
                    return android.os.SystemProperties.getBoolean(key, def);
                }

                @Override // com.android.server.app.GameManagerServiceSystemPropertiesWrapper
                public int getInt(java.lang.String key, int def) {
                    return android.os.SystemProperties.getInt(key, def);
                }

                @Override // com.android.server.app.GameManagerServiceSystemPropertiesWrapper
                public void set(java.lang.String key, java.lang.String val) {
                    android.os.SystemProperties.set(key, val);
                }
            };
        }
    }

    public GameManagerService(android.content.Context context) {
        this(context, createServiceThread().getLooper());
    }

    GameManagerService(android.content.Context context, android.os.Looper looper) {
        this(context, looper, android.os.Environment.getDataDirectory(), new com.android.server.app.GameManagerService.Injector());
    }

    GameManagerService(android.content.Context context, android.os.Looper looper, java.io.File dataDir, com.android.server.app.GameManagerService.Injector injector) {
        super(android.os.PermissionEnforcer.fromContext(context));
        this.mLock = new java.lang.Object();
        this.mDeviceConfigLock = new java.lang.Object();
        this.mGameModeListenerLock = new java.lang.Object();
        this.mGameStateListenerLock = new java.lang.Object();
        this.mSettings = new android.util.ArrayMap<>();
        this.mConfigs = new android.util.ArrayMap<>();
        this.mGameModeListeners = new android.util.ArrayMap<>();
        this.mGameStateListeners = new android.util.ArrayMap<>();
        this.mGameServiceExt = (com.android.server.app.IGameManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.app.IGameManagerServiceExt.class).create();
        this.mUidObserverLock = new java.lang.Object();
        this.mGameForegroundUids = new java.util.HashSet();
        this.mNonGameForegroundUids = new java.util.HashSet();
        this.mContext = context;
        this.mHandler = new com.android.server.app.GameManagerService.SettingsHandler(looper);
        this.mGameServiceExt.setLooper(looper);
        this.mPackageManager = this.mContext.getPackageManager();
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        java.io.File systemDir = new java.io.File(dataDir, "system");
        systemDir.mkdirs();
        android.os.FileUtils.setPermissions(systemDir.toString(), 509, -1, -1);
        this.mGameModeInterventionListFile = new android.util.AtomicFile(new java.io.File(systemDir, GAME_MODE_INTERVENTION_LIST_FILE_NAME));
        android.os.FileUtils.setPermissions(this.mGameModeInterventionListFile.getBaseFile().getAbsolutePath(), com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTION_SERVICE_RESTARTED, -1, -1);
        if (this.mPackageManager.hasSystemFeature("android.software.game_service")) {
            this.mGameServiceController = new com.android.server.app.GameServiceController(context, com.android.internal.os.BackgroundThread.getExecutor(), new com.android.server.app.GameServiceProviderSelectorImpl(context.getResources(), this.mPackageManager), new com.android.server.app.GameServiceProviderInstanceFactoryImpl(context));
        } else {
            this.mGameServiceController = null;
        }
        this.mUidObserver = new com.android.server.app.GameManagerService.MyUidObserver();
        try {
            android.app.ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Could not register UidObserver");
        }
        this.mSysProps = injector.createSystemPropertiesWrapper();
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        if (this.mGameServiceExt.onTransact(code, data, reply, flags)) {
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver result) {
        new com.android.server.app.GameManagerShellCommand().exec(this, in, out, err, args, callback, result);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            writer.println("Permission Denial: can't dump GameManagerService from from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " without permission android.permission.DUMP");
        } else if (args == null || args.length == 0) {
            writer.println("*Dump GameManagerService*");
            dumpAllGameConfigs(writer);
        }
    }

    private void dumpAllGameConfigs(java.io.PrintWriter pw) {
        int userId = android.app.ActivityManager.getCurrentUser();
        java.lang.String[] packageList = getInstalledGamePackageNames(userId);
        for (java.lang.String packageName : packageList) {
            pw.println(getInterventionList(packageName, userId));
        }
    }

    class SettingsHandler extends android.os.Handler {
        SettingsHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            doHandleMessage(msg);
        }

        void doHandleMessage(android.os.Message msg) {
            int uid;
            java.lang.String packageName;
            switch (msg.what) {
                case 1:
                    int userId = ((java.lang.Integer) msg.obj).intValue();
                    if (userId < 0) {
                        android.util.Slog.wtf(com.android.server.app.GameManagerService.TAG, "Attempt to write settings for invalid user: " + userId);
                        synchronized (com.android.server.app.GameManagerService.this.mLock) {
                            removeEqualMessages(1, msg.obj);
                            break;
                        }
                        return;
                    }
                    android.os.Process.setThreadPriority(0);
                    synchronized (com.android.server.app.GameManagerService.this.mLock) {
                        removeEqualMessages(1, msg.obj);
                        if (com.android.server.app.GameManagerService.this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                            com.android.server.app.GameManagerSettings userSettings = (com.android.server.app.GameManagerSettings) com.android.server.app.GameManagerService.this.mSettings.get(java.lang.Integer.valueOf(userId));
                            userSettings.writePersistentDataLocked();
                        }
                        break;
                    }
                    android.os.Process.setThreadPriority(10);
                    return;
                case 2:
                    int userId2 = ((java.lang.Integer) msg.obj).intValue();
                    if (userId2 < 0) {
                        android.util.Slog.wtf(com.android.server.app.GameManagerService.TAG, "Attempt to write settings for invalid user: " + userId2);
                        synchronized (com.android.server.app.GameManagerService.this.mLock) {
                            removeEqualMessages(1, msg.obj);
                            removeEqualMessages(2, msg.obj);
                            break;
                        }
                        return;
                    }
                    synchronized (com.android.server.app.GameManagerService.this.mLock) {
                        removeEqualMessages(1, msg.obj);
                        removeEqualMessages(2, msg.obj);
                        if (com.android.server.app.GameManagerService.this.mSettings.containsKey(java.lang.Integer.valueOf(userId2))) {
                            com.android.server.app.GameManagerSettings userSettings2 = (com.android.server.app.GameManagerSettings) com.android.server.app.GameManagerService.this.mSettings.get(java.lang.Integer.valueOf(userId2));
                            com.android.server.app.GameManagerService.this.mSettings.remove(java.lang.Integer.valueOf(userId2));
                            userSettings2.writePersistentDataLocked();
                        }
                        break;
                    }
                    return;
                case 3:
                    removeEqualMessages(3, msg.obj);
                    int userId3 = ((java.lang.Integer) msg.obj).intValue();
                    java.lang.String[] packageNames = com.android.server.app.GameManagerService.this.getInstalledGamePackageNames(userId3);
                    com.android.server.app.GameManagerService.this.updateConfigsForUser(userId3, false, packageNames);
                    return;
                case 4:
                    android.app.GameState gameState = (android.app.GameState) msg.obj;
                    boolean isLoading = gameState.isLoading();
                    android.os.Bundle data = msg.getData();
                    java.lang.String packageName2 = data.getString("packageName");
                    int userId4 = data.getInt("userId");
                    boolean boostEnabled = com.android.server.app.GameManagerService.this.getGameMode(packageName2, userId4) == 2;
                    try {
                        int uid2 = com.android.server.app.GameManagerService.this.mPackageManager.getPackageUidAsUser(packageName2, userId4);
                        uid = uid2;
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        android.util.Slog.v(com.android.server.app.GameManagerService.TAG, "Failed to get package metadata");
                        uid = -1;
                    }
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.GAME_STATE_CHANGED, packageName2, uid, boostEnabled, com.android.server.app.GameManagerService.gameStateModeToStatsdGameState(gameState.getMode()), isLoading, gameState.getLabel(), gameState.getQuality());
                    if (!boostEnabled) {
                        packageName = packageName2;
                    } else {
                        if (com.android.server.app.GameManagerService.this.mPowerManagerInternal == null) {
                            android.util.Slog.d(com.android.server.app.GameManagerService.TAG, "Error setting loading mode for package " + packageName2 + " and userId " + userId4);
                            return;
                        }
                        packageName = packageName2;
                        if (com.android.server.app.GameManagerService.this.mHandler.hasMessages(5)) {
                            com.android.server.app.GameManagerService.this.mHandler.removeMessages(5);
                        }
                        android.util.Slog.v(com.android.server.app.GameManagerService.TAG, java.lang.String.format("Game loading power mode %s (game state change isLoading=%b)", isLoading ? "ON" : "OFF", java.lang.Boolean.valueOf(isLoading)));
                        com.android.server.app.GameManagerService.this.mPowerManagerInternal.setPowerMode(16, isLoading);
                        if (isLoading) {
                            int loadingBoostDuration = com.android.server.app.GameManagerService.this.getLoadingBoostDuration(packageName, userId4);
                            com.android.server.app.GameManagerService.this.mHandler.sendMessageDelayed(com.android.server.app.GameManagerService.this.mHandler.obtainMessage(5), loadingBoostDuration > 0 ? loadingBoostDuration : 5000);
                        }
                    }
                    synchronized (com.android.server.app.GameManagerService.this.mGameStateListenerLock) {
                        for (android.app.IGameStateListener listener : com.android.server.app.GameManagerService.this.mGameStateListeners.keySet()) {
                            try {
                                listener.onGameStateChanged(packageName, gameState, userId4);
                            } catch (android.os.RemoteException e2) {
                                android.util.Slog.w(com.android.server.app.GameManagerService.TAG, "Cannot notify game state change for listener added by " + com.android.server.app.GameManagerService.this.mGameStateListeners.get(listener));
                            }
                        }
                        break;
                    }
                    return;
                case 5:
                    android.util.Slog.v(com.android.server.app.GameManagerService.TAG, "Game loading power mode OFF (loading boost ended)");
                    com.android.server.app.GameManagerService.this.mPowerManagerInternal.setPowerMode(16, false);
                    return;
                case 6:
                    int userId5 = ((java.lang.Integer) msg.obj).intValue();
                    if (userId5 < 0) {
                        android.util.Slog.wtf(com.android.server.app.GameManagerService.TAG, "Attempt to write setting for invalid user: " + userId5);
                        synchronized (com.android.server.app.GameManagerService.this.mLock) {
                            removeEqualMessages(6, msg.obj);
                            break;
                        }
                        return;
                    }
                    android.os.Process.setThreadPriority(0);
                    removeEqualMessages(6, msg.obj);
                    com.android.server.app.GameManagerService.this.writeGameModeInterventionsToFile(userId5);
                    android.os.Process.setThreadPriority(10);
                    return;
                default:
                    return;
            }
        }
    }

    private class DeviceConfigListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        DeviceConfigListener() {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("game_overlay", com.android.server.app.GameManagerService.this.mContext.getMainExecutor(), this);
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            java.lang.String[] packageNames = (java.lang.String[]) properties.getKeyset().toArray(new java.lang.String[0]);
            android.util.Slog.v(com.android.server.app.GameManagerService.TAG, "Device config changed for packages: " + java.util.Arrays.toString(packageNames));
            com.android.server.app.GameManagerService.this.updateConfigsForUser(android.app.ActivityManager.getCurrentUser(), true, packageNames);
        }

        public void finalize() {
            android.provider.DeviceConfig.removeOnPropertiesChangedListener(this);
        }
    }

    public void setGameState(java.lang.String packageName, android.app.GameState gameState, int userId) {
        if (!lambda$updateConfigsForUser$0(packageName, userId)) {
            android.util.Slog.d(TAG, "No-op for attempt to set game state for non-game app: " + packageName);
            return;
        }
        android.os.Message msg = this.mHandler.obtainMessage(4);
        android.os.Bundle data = new android.os.Bundle();
        data.putString("packageName", packageName);
        data.putInt("userId", userId);
        msg.setData(data);
        msg.obj = gameState;
        this.mHandler.sendMessage(msg);
    }

    public static class GamePackageConfiguration {
        private static final java.lang.String GAME_MODE_CONFIG_NODE_NAME = "game-mode-config";
        public static final java.lang.String METADATA_ANGLE_ALLOW_ANGLE = "com.android.graphics.intervention.angle.allowAngle";
        public static final java.lang.String METADATA_BATTERY_MODE_ENABLE = "com.android.app.gamemode.battery.enabled";
        public static final java.lang.String METADATA_GAME_MODE_CONFIG = "android.game_mode_config";
        public static final java.lang.String METADATA_PERFORMANCE_MODE_ENABLE = "com.android.app.gamemode.performance.enabled";
        public static final java.lang.String METADATA_WM_ALLOW_DOWNSCALE = "com.android.graphics.intervention.wm.allowDownscale";
        public static final java.lang.String TAG = "GameManagerService_GamePackageConfiguration";
        private boolean mAllowAngle;
        private boolean mAllowDownscale;
        private boolean mAllowFpsOverride;
        private boolean mBatteryModeOverridden;
        private final java.lang.Object mModeConfigLock;
        private final android.util.ArrayMap<java.lang.Integer, com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration> mModeConfigs;
        private final java.lang.String mPackageName;
        private boolean mPerfModeOverridden;

        GamePackageConfiguration(java.lang.String packageName) {
            this.mModeConfigLock = new java.lang.Object();
            this.mModeConfigs = new android.util.ArrayMap<>();
            this.mPerfModeOverridden = false;
            this.mBatteryModeOverridden = false;
            this.mAllowDownscale = true;
            this.mAllowAngle = true;
            this.mAllowFpsOverride = true;
            this.mPackageName = packageName;
        }

        GamePackageConfiguration(android.content.pm.PackageManager packageManager, java.lang.String packageName, int userId) {
            this.mModeConfigLock = new java.lang.Object();
            this.mModeConfigs = new android.util.ArrayMap<>();
            this.mPerfModeOverridden = false;
            this.mBatteryModeOverridden = false;
            this.mAllowDownscale = true;
            this.mAllowAngle = true;
            this.mAllowFpsOverride = true;
            this.mPackageName = packageName;
            try {
                android.content.pm.ApplicationInfo ai = packageManager.getApplicationInfoAsUser(packageName, 128, userId);
                if (!parseInterventionFromXml(packageManager, ai, packageName) && ai.metaData != null) {
                    this.mPerfModeOverridden = ai.metaData.getBoolean(METADATA_PERFORMANCE_MODE_ENABLE);
                    this.mBatteryModeOverridden = ai.metaData.getBoolean(METADATA_BATTERY_MODE_ENABLE);
                    this.mAllowDownscale = ai.metaData.getBoolean(METADATA_WM_ALLOW_DOWNSCALE, true);
                    this.mAllowAngle = ai.metaData.getBoolean(METADATA_ANGLE_ALLOW_ANGLE, true);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.v(TAG, "Failed to get package metadata");
            }
            java.lang.String configString = android.provider.DeviceConfig.getProperty("game_overlay", packageName);
            if (configString != null) {
                java.lang.String[] gameModeConfigStrings = configString.split(":");
                for (java.lang.String gameModeConfigString : gameModeConfigStrings) {
                    try {
                        android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
                        parser.setString(gameModeConfigString);
                        addModeConfig(new com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration(parser));
                    } catch (java.lang.IllegalArgumentException e2) {
                        android.util.Slog.e(TAG, "Invalid config string");
                    }
                }
            }
        }

        private boolean parseInterventionFromXml(android.content.pm.PackageManager packageManager, android.content.pm.ApplicationInfo ai, java.lang.String packageName) {
            android.content.res.XmlResourceParser parser;
            int type;
            boolean xmlFound = false;
            try {
                parser = ai.loadXmlMetaData(packageManager, METADATA_GAME_MODE_CONFIG);
            } catch (android.content.pm.PackageManager.NameNotFoundException | java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                this.mPerfModeOverridden = false;
                this.mBatteryModeOverridden = false;
                this.mAllowDownscale = true;
                this.mAllowAngle = true;
                this.mAllowFpsOverride = true;
                android.util.Slog.e(TAG, "Error while parsing XML meta-data for android.game_mode_config");
            }
            try {
                if (parser == null) {
                    android.util.Slog.v(TAG, "No android.game_mode_config meta-data found for package " + this.mPackageName);
                } else {
                    xmlFound = true;
                    android.content.res.Resources resources = packageManager.getResourcesForApplication(packageName);
                    android.util.AttributeSet attributeSet = android.util.Xml.asAttributeSet(parser);
                    do {
                        type = parser.next();
                        if (type == 1) {
                            break;
                        }
                    } while (type != 2);
                    boolean isStartingTagGameModeConfig = GAME_MODE_CONFIG_NODE_NAME.equals(parser.getName());
                    if (!isStartingTagGameModeConfig) {
                        android.util.Slog.w(TAG, "Meta-data does not start with game-mode-config tag");
                    } else {
                        android.content.res.TypedArray array = resources.obtainAttributes(attributeSet, com.android.internal.R.styleable.GameModeConfig);
                        this.mPerfModeOverridden = array.getBoolean(1, false);
                        this.mBatteryModeOverridden = array.getBoolean(0, false);
                        this.mAllowDownscale = array.getBoolean(3, true);
                        this.mAllowAngle = array.getBoolean(2, true);
                        this.mAllowFpsOverride = array.getBoolean(4, true);
                        array.recycle();
                    }
                }
                if (parser != null) {
                    parser.close();
                }
                return xmlFound;
            } catch (java.lang.Throwable th) {
                if (parser != null) {
                    try {
                        parser.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration getOrAddDefaultGameModeConfiguration(int gameMode) {
            com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration gameModeConfiguration;
            synchronized (this.mModeConfigLock) {
                this.mModeConfigs.putIfAbsent(java.lang.Integer.valueOf(gameMode), new com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration(gameMode));
                gameModeConfiguration = this.mModeConfigs.get(java.lang.Integer.valueOf(gameMode));
            }
            return gameModeConfiguration;
        }

        boolean hasActiveGameModeConfig() {
            boolean z;
            synchronized (this.mModeConfigLock) {
                z = !this.mModeConfigs.isEmpty();
            }
            return z;
        }

        public class GameModeConfiguration {
            public static final java.lang.String ANGLE_KEY = "useAngle";
            public static final java.lang.String DEFAULT_FPS = "";
            public static final int DEFAULT_LOADING_BOOST_DURATION = -1;
            public static final float DEFAULT_SCALING = -1.0f;
            public static final boolean DEFAULT_USE_ANGLE = false;
            public static final java.lang.String FPS_KEY = "fps";
            public static final java.lang.String LOADING_BOOST_KEY = "loadingBoost";
            public static final java.lang.String MODE_KEY = "mode";
            public static final java.lang.String SCALING_KEY = "downscaleFactor";
            public static final java.lang.String TAG = "GameManagerService_GameModeConfiguration";
            private java.lang.String mFps;
            private final int mGameMode;
            private int mLoadingBoostDuration;
            private float mScaling;
            private boolean mUseAngle;

            GameModeConfiguration(int gameMode) {
                this.mScaling = -1.0f;
                this.mFps = "";
                this.mGameMode = gameMode;
                this.mUseAngle = false;
                this.mLoadingBoostDuration = -1;
            }

            GameModeConfiguration(android.util.KeyValueListParser parser) {
                float f = -1.0f;
                this.mScaling = -1.0f;
                java.lang.String string = "";
                this.mFps = "";
                boolean z = false;
                this.mGameMode = parser.getInt(MODE_KEY, 0);
                if (com.android.server.app.GameManagerService.GamePackageConfiguration.this.mAllowDownscale && !com.android.server.app.GameManagerService.GamePackageConfiguration.this.willGamePerformOptimizations(this.mGameMode)) {
                    f = parser.getFloat(SCALING_KEY, -1.0f);
                }
                this.mScaling = f;
                if (com.android.server.app.GameManagerService.GamePackageConfiguration.this.mAllowFpsOverride && !com.android.server.app.GameManagerService.GamePackageConfiguration.this.willGamePerformOptimizations(this.mGameMode)) {
                    string = parser.getString(FPS_KEY, "");
                }
                this.mFps = string;
                if (com.android.server.app.GameManagerService.GamePackageConfiguration.this.mAllowAngle && !com.android.server.app.GameManagerService.GamePackageConfiguration.this.willGamePerformOptimizations(this.mGameMode) && parser.getBoolean(ANGLE_KEY, false)) {
                    z = true;
                }
                this.mUseAngle = z;
                this.mLoadingBoostDuration = com.android.server.app.GameManagerService.GamePackageConfiguration.this.willGamePerformOptimizations(this.mGameMode) ? -1 : parser.getInt(LOADING_BOOST_KEY, -1);
            }

            public int getGameMode() {
                return this.mGameMode;
            }

            public synchronized float getScaling() {
                return this.mScaling;
            }

            public synchronized int getFps() {
                try {
                    int fpsInt = java.lang.Integer.parseInt(this.mFps);
                    return fpsInt;
                } catch (java.lang.NumberFormatException e) {
                    return 0;
                }
            }

            synchronized java.lang.String getFpsStr() {
                return this.mFps;
            }

            public synchronized boolean getUseAngle() {
                return this.mUseAngle;
            }

            public synchronized int getLoadingBoostDuration() {
                return this.mLoadingBoostDuration;
            }

            public synchronized void setScaling(float scaling) {
                this.mScaling = scaling;
            }

            public synchronized void setFpsStr(java.lang.String fpsStr) {
                this.mFps = fpsStr;
            }

            public synchronized void setUseAngle(boolean useAngle) {
                this.mUseAngle = useAngle;
            }

            public synchronized void setLoadingBoostDuration(int loadingBoostDuration) {
                this.mLoadingBoostDuration = loadingBoostDuration;
            }

            public boolean isActive() {
                return (this.mGameMode == 1 || this.mGameMode == 2 || this.mGameMode == 3 || this.mGameMode == 4) && !com.android.server.app.GameManagerService.GamePackageConfiguration.this.willGamePerformOptimizations(this.mGameMode);
            }

            android.app.GameModeConfiguration toPublicGameModeConfig() {
                int fpsOverride;
                try {
                    fpsOverride = java.lang.Integer.parseInt(this.mFps);
                } catch (java.lang.NumberFormatException e) {
                    fpsOverride = 0;
                }
                int fpsOverride2 = fpsOverride > 0 ? fpsOverride : 0;
                float scaling = this.mScaling == -1.0f ? 1.0f : this.mScaling;
                return new android.app.GameModeConfiguration.Builder().setScalingFactor(scaling).setFpsOverride(fpsOverride2).build();
            }

            void updateFromPublicGameModeConfig(android.app.GameModeConfiguration config) {
                this.mScaling = config.getScalingFactor();
                this.mFps = java.lang.String.valueOf(config.getFpsOverride());
            }

            public java.lang.String toString() {
                return "[Game Mode:" + this.mGameMode + ",Scaling:" + this.mScaling + ",Use Angle:" + this.mUseAngle + ",Fps:" + this.mFps + ",Loading Boost Duration:" + this.mLoadingBoostDuration + "]";
            }
        }

        public java.lang.String getPackageName() {
            return this.mPackageName;
        }

        public boolean willGamePerformOptimizations(int gameMode) {
            return (this.mBatteryModeOverridden && gameMode == 3) || (this.mPerfModeOverridden && gameMode == 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getAvailableGameModesBitfield() {
            int field = com.android.server.app.GameManagerService.modeToBitmask(4) | com.android.server.app.GameManagerService.modeToBitmask(1);
            synchronized (this.mModeConfigLock) {
                java.util.Iterator<java.lang.Integer> it = this.mModeConfigs.keySet().iterator();
                while (it.hasNext()) {
                    int mode = it.next().intValue();
                    field |= com.android.server.app.GameManagerService.modeToBitmask(mode);
                }
            }
            if (this.mBatteryModeOverridden) {
                field |= com.android.server.app.GameManagerService.modeToBitmask(3);
            }
            if (this.mPerfModeOverridden) {
                return field | com.android.server.app.GameManagerService.modeToBitmask(2);
            }
            return field;
        }

        public int[] getAvailableGameModes() {
            int modesBitfield = getAvailableGameModesBitfield();
            int[] modes = new int[java.lang.Integer.bitCount(modesBitfield)];
            int i = 0;
            int gameModeInHighestBit = java.lang.Integer.numberOfTrailingZeros(java.lang.Integer.highestOneBit(modesBitfield));
            for (int mode = 0; mode <= gameModeInHighestBit; mode++) {
                if (((modesBitfield >> mode) & 1) != 0) {
                    modes[i] = mode;
                    i++;
                }
            }
            return modes;
        }

        public int[] getOverriddenGameModes() {
            if (this.mBatteryModeOverridden && this.mPerfModeOverridden) {
                return new int[]{3, 2};
            }
            if (this.mBatteryModeOverridden) {
                return new int[]{3};
            }
            if (this.mPerfModeOverridden) {
                return new int[]{2};
            }
            return new int[0];
        }

        public com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration getGameModeConfiguration(int gameMode) {
            com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration gameModeConfiguration;
            synchronized (this.mModeConfigLock) {
                gameModeConfiguration = this.mModeConfigs.get(java.lang.Integer.valueOf(gameMode));
            }
            return gameModeConfiguration;
        }

        public void addModeConfig(com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration config) {
            if (config.isActive()) {
                synchronized (this.mModeConfigLock) {
                    this.mModeConfigs.put(java.lang.Integer.valueOf(config.getGameMode()), config);
                }
                return;
            }
            android.util.Slog.w(TAG, "Attempt to add inactive game mode config for " + this.mPackageName + ":" + config.toString());
        }

        public void removeModeConfig(int mode) {
            synchronized (this.mModeConfigLock) {
                this.mModeConfigs.remove(java.lang.Integer.valueOf(mode));
            }
        }

        public boolean isActive() {
            boolean z;
            synchronized (this.mModeConfigLock) {
                z = this.mModeConfigs.size() > 0 || this.mBatteryModeOverridden || this.mPerfModeOverridden;
            }
            return z;
        }

        com.android.server.app.GameManagerService.GamePackageConfiguration copyAndApplyOverride(com.android.server.app.GameManagerService.GamePackageConfiguration overrideConfig) {
            com.android.server.app.GameManagerService.GamePackageConfiguration copy = new com.android.server.app.GameManagerService.GamePackageConfiguration(this.mPackageName);
            boolean z = true;
            copy.mPerfModeOverridden = this.mPerfModeOverridden && (overrideConfig == null || overrideConfig.getGameModeConfiguration(2) == null);
            copy.mBatteryModeOverridden = this.mBatteryModeOverridden && (overrideConfig == null || overrideConfig.getGameModeConfiguration(3) == null);
            copy.mAllowDownscale = this.mAllowDownscale || overrideConfig != null;
            copy.mAllowAngle = this.mAllowAngle || overrideConfig != null;
            if (!this.mAllowFpsOverride && overrideConfig == null) {
                z = false;
            }
            copy.mAllowFpsOverride = z;
            if (overrideConfig != null) {
                synchronized (copy.mModeConfigLock) {
                    synchronized (this.mModeConfigLock) {
                        for (java.util.Map.Entry<java.lang.Integer, com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration> entry : this.mModeConfigs.entrySet()) {
                            copy.mModeConfigs.put(entry.getKey(), entry.getValue());
                        }
                    }
                    synchronized (overrideConfig.mModeConfigLock) {
                        for (java.util.Map.Entry<java.lang.Integer, com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration> entry2 : overrideConfig.mModeConfigs.entrySet()) {
                            copy.mModeConfigs.put(entry2.getKey(), entry2.getValue());
                        }
                    }
                }
            }
            return copy;
        }

        public java.lang.String toString() {
            java.lang.String str;
            synchronized (this.mModeConfigLock) {
                str = "[Name:" + this.mPackageName + " Modes: " + this.mModeConfigs.toString() + "]";
            }
            return str;
        }
    }

    private final class LocalService extends android.app.GameManagerInternal implements com.android.server.wm.CompatScaleProvider {
        private LocalService() {
        }

        public float getResolutionScalingFactor(java.lang.String packageName, int userId) {
            int gameMode = com.android.server.app.GameManagerService.this.getGameModeFromSettingsUnchecked(packageName, userId);
            return com.android.server.app.GameManagerService.this.getResolutionScalingFactorInternal(packageName, gameMode, userId);
        }

        @Override // com.android.server.wm.CompatScaleProvider
        public android.content.res.CompatibilityInfo.CompatScale getCompatScale(java.lang.String packageName, int uid) {
            android.os.UserHandle userHandle = android.os.UserHandle.getUserHandleForUid(uid);
            int userId = userHandle.getIdentifier();
            float scalingFactor = getResolutionScalingFactor(packageName, userId);
            if (scalingFactor > 0.0f) {
                return new android.content.res.CompatibilityInfo.CompatScale(1.0f / scalingFactor);
            }
            return null;
        }
    }

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.app.GameManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mService = new com.android.server.app.GameManagerService(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishBinderService("game", this.mService);
            this.mService.publishLocalService();
            this.mService.mGameServiceExt.init(getContext(), this.mService);
            this.mService.registerDeviceConfigListener();
            this.mService.registerPackageReceiver();
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 1000) {
                this.mService.onBootCompleted();
                this.mService.registerStatsCallbacks();
            }
            this.mService.mGameServiceExt.onBootPhase(phase);
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) {
            android.util.Slog.d(com.android.server.app.GameManagerService.TAG, "Starting user " + user.getUserIdentifier());
            this.mService.onUserStarting(user, android.os.Environment.getDataSystemDeDirectory(user.getUserIdentifier()));
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mService.onUserUnlocking(user);
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocked(com.android.server.SystemService.TargetUser user) {
            super.onUserUnlocked(user);
            this.mService.mGameServiceExt.onUserUnlocked(user);
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            this.mService.onUserStopping(user);
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
            this.mService.onUserSwitching(from, to);
        }
    }

    private boolean isValidPackageName(java.lang.String packageName, int userId) {
        try {
            return this.mPackageManager.getPackageUidAsUser(packageName, userId) == android.os.Binder.getCallingUid();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void checkPermission(java.lang.String permission) throws java.lang.SecurityException {
        if (this.mContext.checkCallingOrSelfPermission(permission) != 0) {
            throw new java.lang.SecurityException("Access denied to process: " + android.os.Binder.getCallingPid() + ", must have permission " + permission);
        }
    }

    private int[] getAvailableGameModesUnchecked(java.lang.String packageName, int userId) {
        com.android.server.app.GameManagerService.GamePackageConfiguration config = getConfig(packageName, userId);
        if (config == null) {
            return new int[]{1, 4};
        }
        return config.getAvailableGameModes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: isPackageGame, reason: merged with bridge method [inline-methods] */
    public boolean lambda$updateConfigsForUser$0(java.lang.String packageName, int userId) {
        try {
            android.content.pm.ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfoAsUser(packageName, 131072, userId);
            return applicationInfo.category == 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public int[] getAvailableGameModes(java.lang.String packageName, int userId) throws java.lang.SecurityException {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        if (!lambda$updateConfigsForUser$0(packageName, userId)) {
            return new int[0];
        }
        return getAvailableGameModesUnchecked(packageName, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getGameModeFromSettingsUnchecked(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            if (!this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                android.util.Slog.d(TAG, "User ID '" + userId + "' does not have a Game Mode selected for package: '" + packageName + "'");
                return 1;
            }
            return this.mSettings.get(java.lang.Integer.valueOf(userId)).getGameModeLocked(packageName);
        }
    }

    public int getGameMode(java.lang.String packageName, int userId) throws java.lang.SecurityException {
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getGameMode", "com.android.server.app.GameManagerService");
        if (!lambda$updateConfigsForUser$0(packageName, userId2)) {
            return 0;
        }
        if (isValidPackageName(packageName, userId2)) {
            return getGameModeFromSettingsUnchecked(packageName, userId2);
        }
        checkPermission("android.permission.MANAGE_GAME_MODE");
        return getGameModeFromSettingsUnchecked(packageName, userId2);
    }

    public android.app.GameModeInfo getGameModeInfo(java.lang.String packageName, int userId) {
        com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration gameModeConfig;
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getGameModeInfo", "com.android.server.app.GameManagerService");
        checkPermission("android.permission.MANAGE_GAME_MODE");
        if (!lambda$updateConfigsForUser$0(packageName, userId2)) {
            return null;
        }
        int activeGameMode = getGameModeFromSettingsUnchecked(packageName, userId2);
        com.android.server.app.GameManagerService.GamePackageConfiguration config = getConfig(packageName, userId2);
        if (config != null) {
            int[] overriddenGameModes = config.getOverriddenGameModes();
            int[] availableGameModes = config.getAvailableGameModes();
            android.app.GameModeInfo.Builder gameModeInfoBuilder = new android.app.GameModeInfo.Builder().setActiveGameMode(activeGameMode).setAvailableGameModes(availableGameModes).setOverriddenGameModes(overriddenGameModes).setDownscalingAllowed(config.mAllowDownscale).setFpsOverrideAllowed(config.mAllowFpsOverride);
            for (int gameMode : availableGameModes) {
                if (!config.willGamePerformOptimizations(gameMode) && (gameModeConfig = config.getGameModeConfiguration(gameMode)) != null) {
                    gameModeInfoBuilder.setGameModeConfiguration(gameMode, gameModeConfig.toPublicGameModeConfig());
                }
            }
            return gameModeInfoBuilder.build();
        }
        return new android.app.GameModeInfo.Builder().setActiveGameMode(activeGameMode).setAvailableGameModes(getAvailableGameModesUnchecked(packageName, userId2)).build();
    }

    public void setGameMode(java.lang.String packageName, int gameMode, int userId) throws java.lang.SecurityException {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        if (gameMode == 0) {
            android.util.Slog.d(TAG, "No-op for attempt to set UNSUPPORTED mode for app: " + packageName);
            return;
        }
        if (!lambda$updateConfigsForUser$0(packageName, userId)) {
            android.util.Slog.d(TAG, "No-op for attempt to set game mode for non-game app: " + packageName);
            return;
        }
        synchronized (this.mLock) {
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "setGameMode", "com.android.server.app.GameManagerService");
            if (!this.mSettings.containsKey(java.lang.Integer.valueOf(userId2))) {
                android.util.Slog.d(TAG, "Failed to set game mode for package " + packageName + " as user " + userId2 + " is not started");
                return;
            }
            com.android.server.app.GameManagerSettings userSettings = this.mSettings.get(java.lang.Integer.valueOf(userId2));
            int fromGameMode = userSettings.getGameModeLocked(packageName);
            userSettings.setGameModeLocked(packageName, gameMode);
            updateInterventions(packageName, gameMode, userId2);
            synchronized (this.mGameModeListenerLock) {
                for (android.app.IGameModeListener listener : this.mGameModeListeners.keySet()) {
                    android.os.Binder.allowBlocking(listener.asBinder());
                    try {
                        listener.onGameModeChanged(packageName, fromGameMode, gameMode, userId2);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Cannot notify game mode change for listener added by " + this.mGameModeListeners.get(listener));
                    }
                }
            }
            sendUserMessage(userId2, 1, EVENT_SET_GAME_MODE, 10000);
            sendUserMessage(userId2, 6, EVENT_SET_GAME_MODE, 0);
            int gameUid = -1;
            try {
                gameUid = this.mPackageManager.getPackageUidAsUser(packageName, userId2);
            } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                android.util.Slog.d(TAG, "Cannot find the UID for package " + packageName + " under user " + userId2);
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.GAME_MODE_CHANGED, gameUid, android.os.Binder.getCallingUid(), gameModeToStatsdGameMode(fromGameMode), gameModeToStatsdGameMode(gameMode));
        }
    }

    public boolean isAngleEnabled(java.lang.String packageName, int userId) throws java.lang.SecurityException {
        int gameMode = getGameMode(packageName, userId);
        if (gameMode == 0) {
            return false;
        }
        synchronized (this.mDeviceConfigLock) {
            com.android.server.app.GameManagerService.GamePackageConfiguration config = this.mConfigs.get(packageName);
            if (config == null) {
                return false;
            }
            com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration gameModeConfiguration = config.getGameModeConfiguration(gameMode);
            if (gameModeConfiguration == null) {
                return false;
            }
            return gameModeConfiguration.getUseAngle();
        }
    }

    public int getLoadingBoostDuration(java.lang.String packageName, int userId) throws java.lang.SecurityException {
        com.android.server.app.GameManagerService.GamePackageConfiguration config;
        com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration gameModeConfiguration;
        int gameMode = getGameMode(packageName, userId);
        if (gameMode == 0) {
            return -1;
        }
        synchronized (this.mDeviceConfigLock) {
            config = this.mConfigs.get(packageName);
        }
        if (config == null || (gameModeConfiguration = config.getGameModeConfiguration(gameMode)) == null) {
            return -1;
        }
        return gameModeConfiguration.getLoadingBoostDuration();
    }

    public void notifyGraphicsEnvironmentSetup(java.lang.String packageName, int userId) throws java.lang.SecurityException {
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "notifyGraphicsEnvironmentSetup", "com.android.server.app.GameManagerService");
        if (!isValidPackageName(packageName, userId2)) {
            android.util.Slog.d(TAG, "No-op for attempt to notify graphics env setup for different packagethan caller with uid: " + android.os.Binder.getCallingUid());
            return;
        }
        int gameMode = getGameMode(packageName, userId2);
        if (gameMode == 0) {
            android.util.Slog.d(TAG, "No-op for attempt to notify graphics env setup for non-game app: " + packageName);
            return;
        }
        int loadingBoostDuration = getLoadingBoostDuration(packageName, userId2);
        if (loadingBoostDuration != -1) {
            if (loadingBoostDuration == 0 || loadingBoostDuration > 5000) {
                loadingBoostDuration = 5000;
            }
            if (this.mHandler.hasMessages(5)) {
                this.mHandler.removeMessages(5);
            } else {
                android.util.Slog.v(TAG, "Game loading power mode ON (loading boost on game start)");
                this.mPowerManagerInternal.setPowerMode(16, true);
            }
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(5), loadingBoostDuration);
        }
    }

    public void setGameServiceProvider(java.lang.String packageName) throws java.lang.SecurityException {
        checkPermission("android.permission.SET_GAME_SERVICE");
        if (this.mGameServiceController == null) {
            return;
        }
        this.mGameServiceController.setGameServiceProvider(packageName);
    }

    public void updateResolutionScalingFactor(java.lang.String packageName, int gameMode, float scalingFactor, int userId) throws java.lang.SecurityException, java.lang.IllegalArgumentException {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        synchronized (this.mLock) {
            if (!this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                throw new java.lang.IllegalArgumentException("User " + userId + " wasn't started");
            }
        }
        setGameModeConfigOverride(packageName, userId, gameMode, null, java.lang.Float.toString(scalingFactor));
    }

    public float getResolutionScalingFactor(java.lang.String packageName, int gameMode, int userId) throws java.lang.SecurityException, java.lang.IllegalArgumentException {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        synchronized (this.mLock) {
            if (!this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                throw new java.lang.IllegalArgumentException("User " + userId + " wasn't started");
            }
        }
        return getResolutionScalingFactorInternal(packageName, gameMode, userId);
    }

    float getResolutionScalingFactorInternal(java.lang.String packageName, int gameMode, int userId) {
        com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration modeConfig;
        com.android.server.app.GameManagerService.GamePackageConfiguration packageConfig = getConfig(packageName, userId);
        if (packageConfig == null || (modeConfig = packageConfig.getGameModeConfiguration(gameMode)) == null) {
            return -1.0f;
        }
        return modeConfig.getScaling();
    }

    public void updateCustomGameModeConfiguration(java.lang.String packageName, android.app.GameModeConfiguration gameModeConfig, int userId) throws java.lang.SecurityException, java.lang.IllegalArgumentException {
        int gameUid;
        checkPermission("android.permission.MANAGE_GAME_MODE");
        if (!lambda$updateConfigsForUser$0(packageName, userId)) {
            android.util.Slog.d(TAG, "No-op for attempt to update custom game mode for non-game app: " + packageName);
            return;
        }
        synchronized (this.mLock) {
            if (!this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                throw new java.lang.IllegalArgumentException("User " + userId + " wasn't started");
            }
        }
        synchronized (this.mLock) {
            if (this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                com.android.server.app.GameManagerSettings settings = this.mSettings.get(java.lang.Integer.valueOf(userId));
                com.android.server.app.GameManagerService.GamePackageConfiguration configOverride = settings.getConfigOverride(packageName);
                if (configOverride == null) {
                    configOverride = new com.android.server.app.GameManagerService.GamePackageConfiguration(packageName);
                    settings.setConfigOverride(packageName, configOverride);
                }
                com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration internalConfig = configOverride.getOrAddDefaultGameModeConfiguration(4);
                float scalingValueFrom = internalConfig.getScaling();
                int fpsValueFrom = internalConfig.getFps();
                internalConfig.updateFromPublicGameModeConfig(gameModeConfig);
                sendUserMessage(userId, 1, EVENT_UPDATE_CUSTOM_GAME_MODE_CONFIG, 10000);
                sendUserMessage(userId, 6, EVENT_UPDATE_CUSTOM_GAME_MODE_CONFIG, 10000);
                int gameMode = getGameMode(packageName, userId);
                if (gameMode == 4) {
                    updateInterventions(packageName, gameMode, userId);
                }
                android.util.Slog.i(TAG, "Updated custom game mode config for package: " + packageName + " with FPS=" + internalConfig.getFps() + ";Scaling=" + internalConfig.getScaling() + " under user " + userId);
                try {
                    gameUid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Slog.d(TAG, "Cannot find the UID for package " + packageName + " under user " + userId);
                    gameUid = -1;
                }
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.GAME_MODE_CONFIGURATION_CHANGED, gameUid, android.os.Binder.getCallingUid(), gameModeToStatsdGameMode(4), scalingValueFrom, gameModeConfig.getScalingFactor(), fpsValueFrom, gameModeConfig.getFpsOverride());
            }
        }
    }

    public void addGameModeListener(final android.app.IGameModeListener listener) {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        try {
            final android.os.IBinder listenerBinder = listener.asBinder();
            listenerBinder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.app.GameManagerService.1
                @Override // android.os.IBinder.DeathRecipient
                public void binderDied() {
                    com.android.server.app.GameManagerService.this.removeGameModeListenerUnchecked(listener);
                    listenerBinder.unlinkToDeath(this, 0);
                }
            }, 0);
            synchronized (this.mGameModeListenerLock) {
                this.mGameModeListeners.put(listener, java.lang.Integer.valueOf(android.os.Binder.getCallingUid()));
            }
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(TAG, "Failed to link death recipient for IGameModeListener from caller " + android.os.Binder.getCallingUid() + ", abandoned its listener registration", ex);
        }
    }

    public void removeGameModeListener(android.app.IGameModeListener listener) {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        removeGameModeListenerUnchecked(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeGameModeListenerUnchecked(android.app.IGameModeListener listener) {
        synchronized (this.mGameModeListenerLock) {
            this.mGameModeListeners.remove(listener);
        }
    }

    public void addGameStateListener(final android.app.IGameStateListener listener) {
        try {
            final android.os.IBinder listenerBinder = listener.asBinder();
            listenerBinder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.app.GameManagerService.2
                @Override // android.os.IBinder.DeathRecipient
                public void binderDied() {
                    com.android.server.app.GameManagerService.this.removeGameStateListenerUnchecked(listener);
                    listenerBinder.unlinkToDeath(this, 0);
                }
            }, 0);
            synchronized (this.mGameStateListenerLock) {
                this.mGameStateListeners.put(listener, java.lang.Integer.valueOf(android.os.Binder.getCallingUid()));
            }
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(TAG, "Failed to link death recipient for IGameStateListener from caller " + android.os.Binder.getCallingUid() + ", abandoned its listener registration", ex);
        }
    }

    public void removeGameStateListener(android.app.IGameStateListener listener) {
        removeGameStateListenerUnchecked(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeGameStateListenerUnchecked(android.app.IGameStateListener listener) {
        synchronized (this.mGameStateListenerLock) {
            this.mGameStateListeners.remove(listener);
        }
    }

    void onBootCompleted() {
        android.util.Slog.d(TAG, "onBootCompleted");
        if (this.mGameServiceController != null) {
            this.mGameServiceController.onBootComplete();
        }
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.app.GameManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                    synchronized (com.android.server.app.GameManagerService.this.mLock) {
                        for (java.util.Map.Entry<java.lang.Integer, com.android.server.app.GameManagerSettings> entry : com.android.server.app.GameManagerService.this.mSettings.entrySet()) {
                            int userId = entry.getKey().intValue();
                            com.android.server.app.GameManagerService.this.sendUserMessage(userId, 1, com.android.server.app.GameManagerService.EVENT_RECEIVE_SHUTDOWN_INDENT, 0);
                            com.android.server.app.GameManagerService.this.sendUserMessage(userId, 6, com.android.server.app.GameManagerService.EVENT_RECEIVE_SHUTDOWN_INDENT, 0);
                        }
                    }
                }
            }
        }, new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN"));
        android.util.Slog.v(TAG, "Game loading power mode OFF (game manager service start/restart)");
        this.mPowerManagerInternal.setPowerMode(16, false);
        android.util.Slog.v(TAG, "Game power mode OFF (game manager service start/restart)");
        this.mPowerManagerInternal.setPowerMode(15, false);
        this.mGameDefaultFrameRateValue = this.mSysProps.getInt(PROPERTY_RO_SURFACEFLINGER_GAME_DEFAULT_FRAME_RATE, 60);
        android.util.Slog.v(TAG, "Game Default Frame Rate : " + this.mGameDefaultFrameRateValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUserMessage(int userId, int what, java.lang.String eventForLog, int delayMillis) {
        android.os.Message msg = this.mHandler.obtainMessage(what, java.lang.Integer.valueOf(userId));
        if (!this.mHandler.sendMessageDelayed(msg, delayMillis)) {
            android.util.Slog.e(TAG, "Failed to send user message " + what + " on " + eventForLog);
        }
    }

    void onUserStarting(com.android.server.SystemService.TargetUser user, java.io.File settingDataDir) {
        int userId = user.getUserIdentifier();
        synchronized (this.mLock) {
            if (!this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                com.android.server.app.GameManagerSettings userSettings = new com.android.server.app.GameManagerSettings(settingDataDir);
                this.mSettings.put(java.lang.Integer.valueOf(userId), userSettings);
                userSettings.readPersistentDataLocked();
            }
        }
        sendUserMessage(userId, 3, EVENT_ON_USER_STARTING, 0);
        if (this.mGameServiceController != null) {
            this.mGameServiceController.notifyUserStarted(user);
        }
    }

    void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        if (this.mGameServiceController != null) {
            this.mGameServiceController.notifyUserUnlocking(user);
        }
    }

    void onUserStopping(com.android.server.SystemService.TargetUser user) {
        int userId = user.getUserIdentifier();
        synchronized (this.mLock) {
            if (this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                sendUserMessage(userId, 2, EVENT_ON_USER_STOPPING, 0);
                if (this.mGameServiceController != null) {
                    this.mGameServiceController.notifyUserStopped(user);
                }
            }
        }
    }

    void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        int toUserId = to.getUserIdentifier();
        sendUserMessage(toUserId, 3, EVENT_ON_USER_SWITCHING, 0);
        if (this.mGameServiceController != null) {
            this.mGameServiceController.notifyNewForegroundUser(to);
        }
    }

    private void resetFps(java.lang.String packageName, int userId) {
        try {
            int uid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
            setGameModeFrameRateOverride(uid, 0.0f);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int modeToBitmask(int gameMode) {
        return 1 << gameMode;
    }

    private boolean bitFieldContainsModeBitmask(int bitField, int gameMode) {
        return (modeToBitmask(gameMode) & bitField) != 0;
    }

    private void updateUseAngle(java.lang.String packageName, int gameMode) {
    }

    private void updateFps(com.android.server.app.GameManagerService.GamePackageConfiguration packageConfig, java.lang.String packageName, int gameMode, int userId) {
        com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration modeConfig = packageConfig.getGameModeConfiguration(gameMode);
        if (modeConfig == null) {
            android.util.Slog.d(TAG, "Game mode " + gameMode + " not found for " + packageName);
            return;
        }
        try {
            float fps = modeConfig.getFps();
            int uid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
            setGameModeFrameRateOverride(uid, fps);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
    }

    private void updateInterventions(java.lang.String packageName, int gameMode, int userId) {
        com.android.server.app.GameManagerService.GamePackageConfiguration packageConfig = getConfig(packageName, userId);
        if (gameMode == 1 || gameMode == 0 || packageConfig == null || packageConfig.willGamePerformOptimizations(gameMode) || packageConfig.getGameModeConfiguration(gameMode) == null) {
            resetFps(packageName, userId);
            if (packageConfig == null) {
                android.util.Slog.v(TAG, "Package configuration not found for " + packageName);
                return;
            }
        } else {
            updateFps(packageConfig, packageName, gameMode, userId);
        }
        updateUseAngle(packageName, gameMode);
    }

    public void setGameModeConfigOverride(java.lang.String packageName, int userId, int gameMode, java.lang.String fpsStr, java.lang.String scaling) throws java.lang.SecurityException {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        int gameUid = -1;
        try {
            gameUid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.d(TAG, "Cannot find the UID for package " + packageName + " under user " + userId);
        }
        com.android.server.app.GameManagerService.GamePackageConfiguration pkgConfig = getConfig(packageName, userId);
        if (pkgConfig != null && pkgConfig.getGameModeConfiguration(gameMode) != null) {
            com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration currentModeConfig = pkgConfig.getGameModeConfiguration(gameMode);
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.GAME_MODE_CONFIGURATION_CHANGED, gameUid, android.os.Binder.getCallingUid(), gameModeToStatsdGameMode(gameMode), currentModeConfig.getScaling(), scaling == null ? currentModeConfig.getScaling() : java.lang.Float.parseFloat(scaling), currentModeConfig.getFps(), fpsStr == null ? currentModeConfig.getFps() : java.lang.Integer.parseInt(fpsStr));
        } else {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.GAME_MODE_CONFIGURATION_CHANGED, gameUid, android.os.Binder.getCallingUid(), gameModeToStatsdGameMode(gameMode), -1.0f, scaling == null ? -1.0f : java.lang.Float.parseFloat(scaling), 0, fpsStr == null ? 0 : java.lang.Integer.parseInt(fpsStr));
        }
        synchronized (this.mLock) {
            if (this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                com.android.server.app.GameManagerSettings settings = this.mSettings.get(java.lang.Integer.valueOf(userId));
                com.android.server.app.GameManagerService.GamePackageConfiguration configOverride = settings.getConfigOverride(packageName);
                if (configOverride == null) {
                    configOverride = new com.android.server.app.GameManagerService.GamePackageConfiguration(packageName);
                    settings.setConfigOverride(packageName, configOverride);
                }
                com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration modeConfigOverride = configOverride.getOrAddDefaultGameModeConfiguration(gameMode);
                if (fpsStr != null) {
                    modeConfigOverride.setFpsStr(fpsStr);
                } else {
                    modeConfigOverride.setFpsStr("");
                }
                if (scaling != null) {
                    modeConfigOverride.setScaling(java.lang.Float.parseFloat(scaling));
                }
                android.util.Slog.i(TAG, "Package Name: " + packageName + " FPS: " + java.lang.String.valueOf(modeConfigOverride.getFps()) + " Scaling: " + modeConfigOverride.getScaling());
                setGameMode(packageName, gameMode, userId);
            }
        }
    }

    public void resetGameModeConfigOverride(java.lang.String packageName, int userId, int gameModeToReset) throws java.lang.SecurityException {
        checkPermission("android.permission.MANAGE_GAME_MODE");
        synchronized (this.mLock) {
            if (this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                com.android.server.app.GameManagerSettings settings = this.mSettings.get(java.lang.Integer.valueOf(userId));
                if (gameModeToReset != -1) {
                    com.android.server.app.GameManagerService.GamePackageConfiguration configOverride = settings.getConfigOverride(packageName);
                    if (configOverride == null) {
                        return;
                    }
                    int modesBitfield = configOverride.getAvailableGameModesBitfield();
                    if (!bitFieldContainsModeBitmask(modesBitfield, gameModeToReset)) {
                        return;
                    }
                    configOverride.removeModeConfig(gameModeToReset);
                    if (!configOverride.hasActiveGameModeConfig()) {
                        settings.removeConfigOverride(packageName);
                    }
                } else {
                    settings.removeConfigOverride(packageName);
                }
                int gameMode = getGameMode(packageName, userId);
                com.android.server.app.GameManagerService.GamePackageConfiguration config = getConfig(packageName, userId);
                int newGameMode = getNewGameMode(gameMode, config);
                if (gameMode != newGameMode) {
                    setGameMode(packageName, 1, userId);
                } else {
                    setGameMode(packageName, gameMode, userId);
                }
            }
        }
    }

    private int getNewGameMode(int gameMode, com.android.server.app.GameManagerService.GamePackageConfiguration config) {
        if (config != null) {
            int modesBitfield = config.getAvailableGameModesBitfield();
            if (bitFieldContainsModeBitmask(modesBitfield & (~modeToBitmask(0)), gameMode)) {
                return gameMode;
            }
            return 1;
        }
        return 1;
    }

    public java.lang.String getInterventionList(java.lang.String packageName, int userId) {
        checkPermission("android.permission.QUERY_ALL_PACKAGES");
        com.android.server.app.GameManagerService.GamePackageConfiguration packageConfig = getConfig(packageName, userId);
        java.lang.StringBuilder listStrSb = new java.lang.StringBuilder();
        if (packageConfig == null) {
            listStrSb.append("\n No intervention found for package ").append(packageName);
            return listStrSb.toString();
        }
        listStrSb.append("\n").append(packageConfig.toString());
        return listStrSb.toString();
    }

    void updateConfigsForUser(final int userId, boolean checkGamePackage, java.lang.String... packageNames) {
        com.android.server.app.GameManagerService.GamePackageConfiguration config;
        if (checkGamePackage) {
            packageNames = (java.lang.String[]) java.util.Arrays.stream(packageNames).filter(new java.util.function.Predicate() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$updateConfigsForUser$0(userId, (java.lang.String) obj);
                }
            }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda4
                @Override // java.util.function.IntFunction
                public final java.lang.Object apply(int i) {
                    return com.android.server.app.GameManagerService.lambda$updateConfigsForUser$1(i);
                }
            });
        }
        try {
            synchronized (this.mDeviceConfigLock) {
                for (java.lang.String packageName : packageNames) {
                    com.android.server.app.GameManagerService.GamePackageConfiguration config2 = new com.android.server.app.GameManagerService.GamePackageConfiguration(this.mPackageManager, packageName, userId);
                    if (config2.isActive()) {
                        android.util.Slog.v(TAG, "Adding config: " + config2.toString());
                        this.mConfigs.put(packageName, config2);
                    } else {
                        android.util.Slog.v(TAG, "Inactive package config for " + config2.getPackageName() + ":" + config2.toString());
                        this.mConfigs.remove(packageName);
                    }
                }
            }
            synchronized (this.mLock) {
                if (this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                    for (java.lang.String packageName2 : packageNames) {
                        int gameMode = getGameMode(packageName2, userId);
                        synchronized (this.mDeviceConfigLock) {
                            config = this.mConfigs.get(packageName2);
                        }
                        int newGameMode = getNewGameMode(gameMode, config);
                        if (newGameMode != gameMode) {
                            setGameMode(packageName2, newGameMode, userId);
                        } else {
                            updateInterventions(packageName2, gameMode, userId);
                        }
                    }
                    sendUserMessage(userId, 6, "UPDATE_CONFIGS_FOR_USERS", 0);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to update configs for user " + userId + ": " + e);
        }
    }

    static /* synthetic */ java.lang.String[] lambda$updateConfigsForUser$1(int x$0) {
        return new java.lang.String[x$0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeGameModeInterventionsToFile(int userId) {
        java.lang.String str;
        int i = userId;
        java.lang.String str2 = ",";
        java.io.FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = this.mGameModeInterventionListFile.startWrite();
            java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(fileOutputStream, java.nio.charset.Charset.defaultCharset()));
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.util.List<java.lang.String> installedGamesList = getInstalledGamePackageNamesByAllUsers(userId);
            for (java.lang.String packageName : installedGamesList) {
                com.android.server.app.GameManagerService.GamePackageConfiguration packageConfig = getConfig(packageName, i);
                if (packageConfig != null) {
                    sb.append(packageName);
                    sb.append("\t");
                    sb.append(this.mPackageManager.getPackageUidAsUser(packageName, i));
                    sb.append("\t");
                    sb.append(getGameMode(packageName, i));
                    sb.append("\t");
                    int[] modes = packageConfig.getAvailableGameModes();
                    int length = modes.length;
                    int i2 = 0;
                    while (i2 < length) {
                        int mode = modes[i2];
                        com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration gameModeConfiguration = packageConfig.getGameModeConfiguration(mode);
                        if (gameModeConfiguration == null) {
                            str = str2;
                        } else {
                            sb.append(mode);
                            sb.append("\t");
                            int useAngle = gameModeConfiguration.getUseAngle() ? 1 : 0;
                            sb.append(android.text.TextUtils.formatSimple("angle=%d", new java.lang.Object[]{java.lang.Integer.valueOf(useAngle)}));
                            sb.append(str2);
                            float scaling = gameModeConfiguration.getScaling();
                            sb.append("scaling=");
                            sb.append(scaling);
                            sb.append(str2);
                            int fps = gameModeConfiguration.getFps();
                            str = str2;
                            sb.append(android.text.TextUtils.formatSimple("fps=%d", new java.lang.Object[]{java.lang.Integer.valueOf(fps)}));
                            sb.append("\t");
                        }
                        i2++;
                        str2 = str;
                    }
                    sb.append("\n");
                    i = userId;
                    str2 = str2;
                }
            }
            bufferedWriter.append((java.lang.CharSequence) sb);
            bufferedWriter.flush();
            android.os.FileUtils.sync(fileOutputStream);
            this.mGameModeInterventionListFile.finishWrite(fileOutputStream);
        } catch (java.lang.Exception e) {
            this.mGameModeInterventionListFile.failWrite(fileOutputStream);
            android.util.Slog.wtf(TAG, "Failed to write game_mode_intervention.list, exception " + e);
        }
    }

    private int[] getAllUserIds(int currentUserId) {
        java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
        int[] userIds = new int[users.size()];
        for (int i = 0; i < userIds.length; i++) {
            userIds[i] = users.get(i).id;
        }
        if (currentUserId != -1) {
            return com.android.internal.util.ArrayUtils.appendInt(userIds, currentUserId);
        }
        return userIds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getInstalledGamePackageNames(int userId) {
        java.util.List<android.content.pm.PackageInfo> packages = this.mPackageManager.getInstalledPackagesAsUser(0, userId);
        return (java.lang.String[]) packages.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.app.GameManagerService.lambda$getInstalledGamePackageNames$2((android.content.pm.PackageInfo) obj);
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.content.pm.PackageInfo) obj).packageName;
            }
        }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.app.GameManagerService.lambda$getInstalledGamePackageNames$4(i);
            }
        });
    }

    static /* synthetic */ boolean lambda$getInstalledGamePackageNames$2(android.content.pm.PackageInfo e) {
        return e.applicationInfo != null && e.applicationInfo.category == 0;
    }

    static /* synthetic */ java.lang.String[] lambda$getInstalledGamePackageNames$4(int x$0) {
        return new java.lang.String[x$0];
    }

    private java.util.List<java.lang.String> getInstalledGamePackageNamesByAllUsers(int currentUserId) {
        java.util.HashSet<java.lang.String> packageSet = new java.util.HashSet<>();
        int[] userIds = getAllUserIds(currentUserId);
        for (int userId : userIds) {
            packageSet.addAll(java.util.Arrays.asList(getInstalledGamePackageNames(userId)));
        }
        return new java.util.ArrayList(packageSet);
    }

    public com.android.server.app.GameManagerService.GamePackageConfiguration getConfig(java.lang.String packageName, int userId) {
        com.android.server.app.GameManagerService.GamePackageConfiguration config;
        com.android.server.app.GameManagerService.GamePackageConfiguration overrideConfig = null;
        synchronized (this.mDeviceConfigLock) {
            config = this.mConfigs.get(packageName);
        }
        synchronized (this.mLock) {
            if (this.mSettings.containsKey(java.lang.Integer.valueOf(userId))) {
                overrideConfig = this.mSettings.get(java.lang.Integer.valueOf(userId)).getConfigOverride(packageName);
            }
        }
        if (overrideConfig == null || config == null) {
            return overrideConfig == null ? config : overrideConfig;
        }
        return config.copyAndApplyOverride(overrideConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerPackageReceiver() {
        android.content.IntentFilter packageFilter = new android.content.IntentFilter();
        packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        packageFilter.addDataScheme("package");
        android.content.BroadcastReceiver packageReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.app.GameManagerService.4
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:14:0x003e  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r9, android.content.Intent r10) {
                /*
                    Method dump skipped, instruction units count: 218
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.app.GameManagerService.AnonymousClass4.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mContext.registerReceiverForAllUsers(packageReceiver, packageFilter, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDeviceConfigListener() {
        this.mDeviceConfigListener = new com.android.server.app.GameManagerService.DeviceConfigListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishLocalService() {
        com.android.server.app.GameManagerService.LocalService localService = new com.android.server.app.GameManagerService.LocalService();
        com.android.server.wm.ActivityTaskManagerInternal atmi = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        atmi.registerCompatScaleProvider(1, localService);
        com.android.server.LocalServices.addService(android.app.GameManagerInternal.class, localService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerStatsCallbacks() {
        android.app.StatsManager statsManager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.GAME_MODE_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda5
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.onPullAtom(i, list);
            }
        });
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.GAME_MODE_CONFIGURATION, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda5
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.onPullAtom(i, list);
            }
        });
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.GAME_MODE_LISTENER, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.app.GameManagerService$$ExternalSyntheticLambda5
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.onPullAtom(i, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
        java.util.Set<java.lang.String> packages;
        int[] iArr;
        int i = com.android.internal.util.FrameworkStatsLog.GAME_MODE_INFO;
        if (atomTag == 10165 || atomTag == 10166) {
            int userId = android.app.ActivityManager.getCurrentUser();
            synchronized (this.mDeviceConfigLock) {
                packages = this.mConfigs.keySet();
            }
            for (java.lang.String p : packages) {
                com.android.server.app.GameManagerService.GamePackageConfiguration config = getConfig(p, userId);
                if (config != null) {
                    int uid = -1;
                    try {
                        uid = this.mPackageManager.getPackageUidAsUser(p, userId);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        android.util.Slog.d(TAG, "Cannot find UID for package " + p + " under user handle id " + userId);
                    }
                    if (atomTag == i) {
                        data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(i, uid, gameModesToStatsdGameModes(config.getOverriddenGameModes()), gameModesToStatsdGameModes(config.getAvailableGameModes())));
                    } else if (atomTag == 10166) {
                        int[] availableGameModes = config.getAvailableGameModes();
                        int length = availableGameModes.length;
                        int i2 = 0;
                        while (i2 < length) {
                            int gameMode = availableGameModes[i2];
                            com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration modeConfig = config.getGameModeConfiguration(gameMode);
                            if (modeConfig == null) {
                                iArr = availableGameModes;
                            } else {
                                iArr = availableGameModes;
                                data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.GAME_MODE_CONFIGURATION, uid, gameModeToStatsdGameMode(gameMode), modeConfig.getFps(), modeConfig.getScaling()));
                            }
                            i2++;
                            availableGameModes = iArr;
                        }
                    }
                    i = com.android.internal.util.FrameworkStatsLog.GAME_MODE_INFO;
                }
            }
            return 0;
        }
        if (atomTag == 10167) {
            synchronized (this.mGameModeListenerLock) {
                data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.GAME_MODE_LISTENER, this.mGameModeListeners.size()));
            }
            return 0;
        }
        return 0;
    }

    private static int[] gameModesToStatsdGameModes(int[] modes) {
        if (modes == null) {
            return null;
        }
        int[] statsdModes = new int[modes.length];
        int i = 0;
        int length = modes.length;
        int i2 = 0;
        while (i2 < length) {
            int mode = modes[i2];
            statsdModes[i] = gameModeToStatsdGameMode(mode);
            i2++;
            i++;
        }
        return statsdModes;
    }

    private static int gameModeToStatsdGameMode(int mode) {
        switch (mode) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int gameStateModeToStatsdGameState(int mode) {
        switch (mode) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return 0;
        }
    }

    private static com.android.server.ServiceThread createServiceThread() {
        com.android.server.ServiceThread handlerThread = new com.android.server.ServiceThread(TAG, 10, true);
        handlerThread.start();
        return handlerThread;
    }

    void setGameModeFrameRateOverride(int uid, float frameRate) {
        nativeSetGameModeFrameRateOverride(uid, frameRate);
    }

    void setGameDefaultFrameRateOverride(int uid, float frameRate) {
        android.util.Slog.v(TAG, "setDefaultFrameRateOverride : " + uid + " , " + frameRate);
        nativeSetGameDefaultFrameRateOverride(uid, frameRate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getGameDefaultFrameRate(boolean isEnabled) {
        if (!android.server.app.Flags.gameDefaultFrameRate()) {
            return 0.0f;
        }
        float gameDefaultFrameRate = isEnabled ? this.mGameDefaultFrameRateValue : 0.0f;
        return gameDefaultFrameRate;
    }

    public void toggleGameDefaultFrameRate(boolean isEnabled) {
        toggleGameDefaultFrameRate_enforcePermission();
        if (android.server.app.Flags.gameDefaultFrameRate()) {
            android.util.Slog.v(TAG, "toggleGameDefaultFrameRate : " + isEnabled);
            toggleGameDefaultFrameRateUnchecked(isEnabled);
        }
    }

    private void toggleGameDefaultFrameRateUnchecked(boolean isEnabled) {
        synchronized (this.mLock) {
            if (isEnabled) {
                this.mSysProps.set(PROPERTY_DEBUG_GFX_GAME_DEFAULT_FRAME_RATE_DISABLED, "false");
            } else {
                this.mSysProps.set(PROPERTY_DEBUG_GFX_GAME_DEFAULT_FRAME_RATE_DISABLED, "true");
            }
        }
        synchronized (this.mUidObserverLock) {
            java.util.Iterator<java.lang.Integer> it = this.mGameForegroundUids.iterator();
            while (it.hasNext()) {
                int uid = it.next().intValue();
                setGameDefaultFrameRateOverride(uid, getGameDefaultFrameRate(isEnabled));
            }
        }
    }

    final class MyUidObserver extends android.app.UidObserver {
        MyUidObserver() {
        }

        public void onUidGone(int uid, boolean disabled) {
            synchronized (com.android.server.app.GameManagerService.this.mUidObserverLock) {
                handleUidMovedOffTop(uid);
            }
        }

        public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
            switch (procState) {
                case 2:
                    handleUidMovedToTop(uid);
                    break;
                default:
                    handleUidMovedOffTop(uid);
                    break;
            }
        }

        private void handleUidMovedToTop(int uid) {
            java.lang.String[] packages = com.android.server.app.GameManagerService.this.mPackageManager.getPackagesForUid(uid);
            if (packages == null || packages.length == 0) {
                return;
            }
            for (java.lang.String pkgName : packages) {
                if (pkgName.startsWith(com.android.server.app.GameManagerService.PKG_NAME_START_WITH_OPLUS) || pkgName.startsWith(com.android.server.app.GameManagerService.PKG_NAME_START_WITH_OPPO) || pkgName.startsWith(com.android.server.app.GameManagerService.PKG_NAME_START_WITH_COLOROS) || pkgName.startsWith(com.android.server.app.GameManagerService.PKG_NAME_START_WITH_NEARME) || pkgName.startsWith(com.android.server.app.GameManagerService.PKG_NAME_START_WITH_HEYTAP)) {
                    return;
                }
            }
            final int userId = com.android.server.app.GameManagerService.this.mContext.getUserId();
            boolean isNotGame = java.util.Arrays.stream(packages).noneMatch(new java.util.function.Predicate() { // from class: com.android.server.app.GameManagerService$MyUidObserver$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$handleUidMovedToTop$0(userId, (java.lang.String) obj);
                }
            });
            synchronized (com.android.server.app.GameManagerService.this.mUidObserverLock) {
                if (isNotGame) {
                    if (android.server.app.Flags.disableGameModeWhenAppTop()) {
                        if (!com.android.server.app.GameManagerService.this.mGameForegroundUids.isEmpty() && com.android.server.app.GameManagerService.this.mNonGameForegroundUids.isEmpty()) {
                            android.util.Slog.v(com.android.server.app.GameManagerService.TAG, "Game power mode OFF (first non-game in foreground)");
                            com.android.server.app.GameManagerService.this.mPowerManagerInternal.setPowerMode(15, false);
                        }
                        com.android.server.app.GameManagerService.this.mNonGameForegroundUids.add(java.lang.Integer.valueOf(uid));
                    }
                    return;
                }
                if (com.android.server.app.GameManagerService.this.mGameForegroundUids.isEmpty() && (!android.server.app.Flags.disableGameModeWhenAppTop() || com.android.server.app.GameManagerService.this.mNonGameForegroundUids.isEmpty())) {
                    android.util.Slog.v(com.android.server.app.GameManagerService.TAG, "Game power mode ON (first game in foreground)");
                    com.android.server.app.GameManagerService.this.mPowerManagerInternal.setPowerMode(15, true);
                }
                boolean isGameDefaultFrameRateDisabled = com.android.server.app.GameManagerService.this.mSysProps.getBoolean(com.android.server.app.GameManagerService.PROPERTY_DEBUG_GFX_GAME_DEFAULT_FRAME_RATE_DISABLED, false);
                com.android.server.app.GameManagerService.this.setGameDefaultFrameRateOverride(uid, com.android.server.app.GameManagerService.this.getGameDefaultFrameRate(isGameDefaultFrameRateDisabled ? false : true));
                com.android.server.app.GameManagerService.this.mGameForegroundUids.add(java.lang.Integer.valueOf(uid));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$handleUidMovedToTop$0(int userId, java.lang.String p) {
            return com.android.server.app.GameManagerService.this.lambda$updateConfigsForUser$0(p, userId);
        }

        private void handleUidMovedOffTop(int uid) {
            synchronized (com.android.server.app.GameManagerService.this.mUidObserverLock) {
                if (com.android.server.app.GameManagerService.this.mGameForegroundUids.contains(java.lang.Integer.valueOf(uid))) {
                    com.android.server.app.GameManagerService.this.mGameForegroundUids.remove(java.lang.Integer.valueOf(uid));
                    if (com.android.server.app.GameManagerService.this.mGameForegroundUids.isEmpty() && (!android.server.app.Flags.disableGameModeWhenAppTop() || com.android.server.app.GameManagerService.this.mNonGameForegroundUids.isEmpty())) {
                        android.util.Slog.v(com.android.server.app.GameManagerService.TAG, "Game power mode OFF (no games in foreground)");
                        com.android.server.app.GameManagerService.this.mPowerManagerInternal.setPowerMode(15, false);
                    }
                } else if (android.server.app.Flags.disableGameModeWhenAppTop() && com.android.server.app.GameManagerService.this.mNonGameForegroundUids.contains(java.lang.Integer.valueOf(uid))) {
                    com.android.server.app.GameManagerService.this.mNonGameForegroundUids.remove(java.lang.Integer.valueOf(uid));
                    if (com.android.server.app.GameManagerService.this.mNonGameForegroundUids.isEmpty() && !com.android.server.app.GameManagerService.this.mGameForegroundUids.isEmpty()) {
                        android.util.Slog.v(com.android.server.app.GameManagerService.TAG, "Game power mode ON (only games in foreground)");
                        com.android.server.app.GameManagerService.this.mPowerManagerInternal.setPowerMode(15, true);
                    }
                }
            }
        }
    }
}
