package android.net;

/* JADX INFO: loaded from: classes.dex */
public class ConnectivityModuleConnector {
    private static final java.lang.String CONFIG_ALWAYS_RATELIMIT_NETWORKSTACK_CRASH = "always_ratelimit_networkstack_crash";
    private static final java.lang.String CONFIG_MIN_CRASH_INTERVAL_MS = "min_crash_interval";
    private static final java.lang.String CONFIG_MIN_UPTIME_BEFORE_CRASH_MS = "min_uptime_before_crash";
    private static final long DEFAULT_MIN_CRASH_INTERVAL_MS = 21600000;
    private static final long DEFAULT_MIN_UPTIME_BEFORE_CRASH_MS = 1800000;
    private static final java.lang.String IN_PROCESS_SUFFIX = ".InProcess";
    private static final java.lang.String PREFS_FILE = "ConnectivityModuleConnector.xml";
    private static final java.lang.String PREF_KEY_LAST_CRASH_TIME = "lastcrash_time";
    private static final java.lang.String TAG = android.net.ConnectivityModuleConnector.class.getSimpleName();
    private static android.net.ConnectivityModuleConnector sInstance;
    private android.content.Context mContext;
    private final android.net.ConnectivityModuleConnector.Dependencies mDeps;
    private final android.util.ArraySet<android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener> mHealthListeners;

    public interface ConnectivityModuleHealthListener {
        void onNetworkStackFailure(java.lang.String str);
    }

    protected interface Dependencies {
        android.content.Intent getModuleServiceIntent(android.content.pm.PackageManager packageManager, java.lang.String str, java.lang.String str2, boolean z);
    }

    public interface ModuleServiceCallback {
        void onModuleServiceConnected(android.os.IBinder iBinder);
    }

    private ConnectivityModuleConnector() {
        this(new android.net.ConnectivityModuleConnector.DependenciesImpl());
    }

    ConnectivityModuleConnector(android.net.ConnectivityModuleConnector.Dependencies deps) {
        this.mHealthListeners = new android.util.ArraySet<>();
        this.mDeps = deps;
    }

    public static synchronized android.net.ConnectivityModuleConnector getInstance() {
        if (sInstance == null) {
            sInstance = new android.net.ConnectivityModuleConnector();
        }
        return sInstance;
    }

    public void init(android.content.Context context) {
        log("Network stack init");
        this.mContext = context;
    }

    private static class DependenciesImpl implements android.net.ConnectivityModuleConnector.Dependencies {
        private DependenciesImpl() {
        }

        @Override // android.net.ConnectivityModuleConnector.Dependencies
        public android.content.Intent getModuleServiceIntent(android.content.pm.PackageManager pm, java.lang.String serviceIntentBaseAction, java.lang.String servicePermissionName, boolean inSystemProcess) {
            java.lang.String str;
            if (inSystemProcess) {
                str = serviceIntentBaseAction + android.net.ConnectivityModuleConnector.IN_PROCESS_SUFFIX;
            } else {
                str = serviceIntentBaseAction;
            }
            android.content.Intent intent = new android.content.Intent(str);
            android.content.ComponentName comp = intent.resolveSystemService(pm, 0);
            if (comp == null) {
                return null;
            }
            intent.setComponent(comp);
            try {
                int uid = pm.getPackageUidAsUser(comp.getPackageName(), 0);
                int expectedUid = inSystemProcess ? 1000 : 1073;
                if (uid != expectedUid) {
                    throw new java.lang.SecurityException("Invalid network stack UID: " + uid);
                }
                if (!inSystemProcess) {
                    android.net.ConnectivityModuleConnector.checkModuleServicePermission(pm, comp, servicePermissionName);
                }
                return intent;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new java.lang.SecurityException("Could not check network stack UID; package not found.", e);
            }
        }
    }

    public void registerHealthListener(android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener listener) {
        synchronized (this.mHealthListeners) {
            this.mHealthListeners.add(listener);
        }
    }

    public void startModuleService(java.lang.String serviceIntentBaseAction, java.lang.String servicePermissionName, android.net.ConnectivityModuleConnector.ModuleServiceCallback callback) {
        log("Starting networking module " + serviceIntentBaseAction);
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.content.Intent intent = this.mDeps.getModuleServiceIntent(pm, serviceIntentBaseAction, servicePermissionName, true);
        if (intent == null) {
            intent = this.mDeps.getModuleServiceIntent(pm, serviceIntentBaseAction, servicePermissionName, false);
            log("Starting networking module in network_stack process");
        } else {
            log("Starting networking module in system_server process");
        }
        if (intent == null) {
            maybeCrashWithTerribleFailure("Could not resolve the networking module", null);
            return;
        }
        java.lang.String packageName = intent.getComponent().getPackageName();
        if (!this.mContext.bindServiceAsUser(intent, new android.net.ConnectivityModuleConnector.ModuleServiceConnection(packageName, callback), 65, android.os.UserHandle.SYSTEM)) {
            maybeCrashWithTerribleFailure("Could not bind to networking module in-process, or in app with " + intent, packageName);
        } else {
            log("Networking module service start requested");
        }
    }

    private class ModuleServiceConnection implements android.content.ServiceConnection {
        private final android.net.ConnectivityModuleConnector.ModuleServiceCallback mModuleServiceCallback;
        private final java.lang.String mPackageName;

        private ModuleServiceConnection(java.lang.String packageName, android.net.ConnectivityModuleConnector.ModuleServiceCallback moduleCallback) {
            this.mPackageName = packageName;
            this.mModuleServiceCallback = moduleCallback;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            android.net.ConnectivityModuleConnector.this.logi("Networking module service connected");
            this.mModuleServiceCallback.onModuleServiceConnected(service);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.net.ConnectivityModuleConnector.this.log("ModuleServiceConnection mPackageName:" + this.mPackageName + ",getPackageName:" + name.getPackageName() + ", getClassName" + name.getClassName());
            android.net.ConnectivityModuleConnector.this.maybeCrashWithTerribleFailure("Lost network stack. This is not the root cause of any issue, it is a side effect of a crash that happened earlier. Earlier logs should point to the actual issue.", this.mPackageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkModuleServicePermission(android.content.pm.PackageManager pm, android.content.ComponentName comp, java.lang.String servicePermissionName) {
        int hasPermission = pm.checkPermission(servicePermissionName, comp.getPackageName());
        if (hasPermission != 0) {
            throw new java.lang.SecurityException("Networking module does not have permission " + servicePermissionName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void maybeCrashWithTerribleFailure(java.lang.String message, java.lang.String packageName) {
        android.util.ArraySet<android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener> listeners;
        logWtf(message, null);
        long uptime = android.os.SystemClock.elapsedRealtime();
        long now = java.lang.System.currentTimeMillis();
        long minCrashIntervalMs = android.provider.DeviceConfig.getLong("connectivity", CONFIG_MIN_CRASH_INTERVAL_MS, DEFAULT_MIN_CRASH_INTERVAL_MS);
        long minUptimeBeforeCrash = android.provider.DeviceConfig.getLong("connectivity", CONFIG_MIN_UPTIME_BEFORE_CRASH_MS, 1800000L);
        boolean haveKnownRecentCrash = false;
        boolean alwaysRatelimit = android.provider.DeviceConfig.getBoolean("connectivity", CONFIG_ALWAYS_RATELIMIT_NETWORKSTACK_CRASH, false);
        android.content.SharedPreferences prefs = getSharedPreferences();
        long lastCrashTime = tryGetLastCrashTime(prefs);
        boolean alwaysCrash = android.os.Build.IS_DEBUGGABLE && !alwaysRatelimit;
        boolean justBooted = uptime < minUptimeBeforeCrash;
        boolean haveLastCrashTime = lastCrashTime != 0 && lastCrashTime < now;
        if (haveLastCrashTime && now < lastCrashTime + minCrashIntervalMs) {
            haveKnownRecentCrash = true;
        }
        if (alwaysCrash || (!justBooted && !haveKnownRecentCrash)) {
            tryWriteLastCrashTime(prefs, now);
            throw new java.lang.IllegalStateException(message);
        }
        if (packageName != null) {
            synchronized (this.mHealthListeners) {
                listeners = new android.util.ArraySet<>(this.mHealthListeners);
            }
            for (android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener listener : listeners) {
                listener.onNetworkStackFailure(packageName);
            }
        }
    }

    private android.content.SharedPreferences getSharedPreferences() {
        try {
            java.io.File prefsFile = new java.io.File(android.os.Environment.getDataSystemDeDirectory(0), PREFS_FILE);
            return this.mContext.createDeviceProtectedStorageContext().getSharedPreferences(prefsFile, 0);
        } catch (java.lang.Throwable e) {
            logWtf("Error loading shared preferences", e);
            return null;
        }
    }

    private long tryGetLastCrashTime(android.content.SharedPreferences prefs) {
        if (prefs == null) {
            return 0L;
        }
        try {
            return prefs.getLong(PREF_KEY_LAST_CRASH_TIME, 0L);
        } catch (java.lang.Throwable e) {
            logWtf("Error getting last crash time", e);
            return 0L;
        }
    }

    private void tryWriteLastCrashTime(android.content.SharedPreferences prefs, long value) {
        if (prefs == null) {
            return;
        }
        try {
            prefs.edit().putLong(PREF_KEY_LAST_CRASH_TIME, value).commit();
        } catch (java.lang.Throwable e) {
            logWtf("Error writing last crash time", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(java.lang.String message) {
        android.util.Log.d(TAG, message);
    }

    private void logWtf(java.lang.String message, java.lang.Throwable e) {
        android.util.Slog.wtf(TAG, message, e);
        android.util.Log.e(TAG, message, e);
    }

    private void loge(java.lang.String message, java.lang.Throwable e) {
        android.util.Log.e(TAG, message, e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(java.lang.String message) {
        android.util.Log.i(TAG, message);
    }
}
