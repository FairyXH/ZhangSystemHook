package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class AppStateTrackerImpl implements com.android.server.AppStateTracker {
    private static final java.lang.String APP_RESTRICTION_COUNTER_METRIC_ID = "battery.value_app_background_restricted";
    private static final boolean DEBUG = false;
    static final int TARGET_OP = 70;
    android.app.ActivityManagerInternal mActivityManagerInternal;
    android.app.AppOpsManager mAppOpsManager;
    com.android.internal.app.IAppOpsService mAppOpsService;
    com.android.server.usage.AppStandbyInternal mAppStandbyInternal;
    boolean mBatterySaverEnabled;
    private final android.content.Context mContext;
    com.android.server.AppStateTrackerImpl.FeatureFlagsObserver mFlagsObserver;
    boolean mForceAllAppStandbyForSmallBattery;
    boolean mForceAllAppsStandby;
    private final com.android.server.AppStateTrackerImpl.MyHandler mHandler;
    android.app.IActivityManager mIActivityManager;
    boolean mIsPluggedIn;
    android.os.PowerManagerInternal mPowerManagerInternal;
    com.android.server.AppStateTrackerImpl.StandbyTracker mStandbyTracker;
    boolean mStarted;
    private final java.lang.Object mLock = new java.lang.Object();
    final android.util.ArraySet<android.util.Pair<java.lang.Integer, java.lang.String>> mRunAnyRestrictedPackages = new android.util.ArraySet<>();
    final android.util.SparseBooleanArray mActiveUids = new android.util.SparseBooleanArray();
    private int[] mPowerExemptAllAppIds = new int[0];
    private int[] mPowerExemptUserAppIds = new int[0];
    private int[] mTempExemptAppIds = this.mPowerExemptAllAppIds;
    final android.util.SparseSetArray<java.lang.String> mExemptedBucketPackages = new android.util.SparseSetArray<>();
    final android.util.ArraySet<com.android.server.AppStateTrackerImpl.Listener> mListeners = new android.util.ArraySet<>();
    volatile java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> mBackgroundRestrictedUidPackages = java.util.Collections.emptySet();
    private final com.android.internal.util.StatLogger mStatLogger = new com.android.internal.util.StatLogger(new java.lang.String[]{"UID_FG_STATE_CHANGED", "UID_ACTIVE_STATE_CHANGED", "RUN_ANY_CHANGED", "ALL_UNEXEMPTED", "ALL_EXEMPTION_LIST_CHANGED", "TEMP_EXEMPTION_LIST_CHANGED", "EXEMPTED_BUCKET_CHANGED", "FORCE_ALL_CHANGED", "IS_UID_ACTIVE_CACHED", "IS_UID_ACTIVE_RAW"});
    private final android.app.ActivityManagerInternal.AppBackgroundRestrictionListener mAppBackgroundRestrictionListener = new android.app.ActivityManagerInternal.AppBackgroundRestrictionListener() { // from class: com.android.server.AppStateTrackerImpl.2
        public void onAutoRestrictedBucketFeatureFlagChanged(boolean autoRestrictedBucket) {
            com.android.server.AppStateTrackerImpl.this.mHandler.notifyAutoRestrictedBucketFeatureFlagChanged(autoRestrictedBucket);
        }
    };
    private final android.content.BroadcastReceiver mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.AppStateTrackerImpl.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            byte b;
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
            java.lang.String action = intent.getAction();
            boolean z = true;
            switch (action.hashCode()) {
                case -2061058799:
                    b = !action.equals("android.intent.action.USER_REMOVED") ? (byte) -1 : (byte) 0;
                    break;
                case -1538406691:
                    b = !action.equals("android.intent.action.BATTERY_CHANGED") ? (byte) -1 : (byte) 1;
                    break;
                case 525384130:
                    b = !action.equals("android.intent.action.PACKAGE_REMOVED") ? (byte) -1 : (byte) 2;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    if (userId > 0) {
                        com.android.server.AppStateTrackerImpl.this.mHandler.doUserRemoved(userId);
                        return;
                    }
                    return;
                case 1:
                    synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                        com.android.server.AppStateTrackerImpl appStateTrackerImpl = com.android.server.AppStateTrackerImpl.this;
                        if (intent.getIntExtra("plugged", 0) == 0) {
                            z = false;
                        }
                        appStateTrackerImpl.mIsPluggedIn = z;
                        break;
                    }
                    com.android.server.AppStateTrackerImpl.this.updateForceAllAppStandbyState();
                    return;
                case 2:
                    if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        java.lang.String pkgName = intent.getData().getSchemeSpecificPart();
                        int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                        synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                            com.android.server.AppStateTrackerImpl.this.mExemptedBucketPackages.remove(userId, pkgName);
                            com.android.server.AppStateTrackerImpl.this.mRunAnyRestrictedPackages.remove(android.util.Pair.create(java.lang.Integer.valueOf(uid), pkgName));
                            com.android.server.AppStateTrackerImpl.this.updateBackgroundRestrictedUidPackagesLocked();
                            com.android.server.AppStateTrackerImpl.this.mActiveUids.delete(uid);
                            break;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    interface Stats {
        public static final int ALL_EXEMPTION_LIST_CHANGED = 4;
        public static final int ALL_UNEXEMPTED = 3;
        public static final int EXEMPTED_BUCKET_CHANGED = 6;
        public static final int FORCE_ALL_CHANGED = 7;
        public static final int IS_UID_ACTIVE_CACHED = 8;
        public static final int IS_UID_ACTIVE_RAW = 9;
        public static final int RUN_ANY_CHANGED = 2;
        public static final int TEMP_EXEMPTION_LIST_CHANGED = 5;
        public static final int UID_ACTIVE_STATE_CHANGED = 1;
        public static final int UID_FG_STATE_CHANGED = 0;
    }

    public void addBackgroundRestrictedAppListener(final com.android.server.AppStateTracker.BackgroundRestrictedAppListener listener) {
        addListener(new com.android.server.AppStateTrackerImpl.Listener() { // from class: com.android.server.AppStateTrackerImpl.1
            @Override // com.android.server.AppStateTrackerImpl.Listener
            public void updateBackgroundRestrictedForUidPackage(int uid, java.lang.String packageName, boolean restricted) {
                listener.updateBackgroundRestrictedForUidPackage(uid, packageName, restricted);
            }
        });
    }

    public boolean isAppBackgroundRestricted(int uid, java.lang.String packageName) {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> bgRestrictedUidPkgs = this.mBackgroundRestrictedUidPackages;
        return bgRestrictedUidPkgs.contains(android.util.Pair.create(java.lang.Integer.valueOf(uid), packageName));
    }

    class FeatureFlagsObserver extends android.database.ContentObserver {
        FeatureFlagsObserver() {
            super(null);
        }

        void register() {
            com.android.server.AppStateTrackerImpl.this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("forced_app_standby_for_small_battery_enabled"), false, this);
        }

        boolean isForcedAppStandbyForSmallBatteryEnabled() {
            return com.android.server.AppStateTrackerImpl.this.injectGetGlobalSettingInt("forced_app_standby_for_small_battery_enabled", 0) == 1;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (android.provider.Settings.Global.getUriFor("forced_app_standby_for_small_battery_enabled").equals(uri)) {
                boolean enabled = isForcedAppStandbyForSmallBatteryEnabled();
                synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                    if (com.android.server.AppStateTrackerImpl.this.mForceAllAppStandbyForSmallBattery == enabled) {
                        return;
                    }
                    com.android.server.AppStateTrackerImpl.this.mForceAllAppStandbyForSmallBattery = enabled;
                    com.android.server.AppStateTrackerImpl.this.updateForceAllAppStandbyState();
                    return;
                }
            }
            android.util.Slog.w("AppStateTracker", "Unexpected feature flag uri encountered: " + uri);
        }
    }

    public static abstract class Listener {
        /* JADX INFO: Access modifiers changed from: private */
        public void onRunAnyAppOpsChanged(com.android.server.AppStateTrackerImpl sender, int uid, java.lang.String packageName) {
            updateJobsForUidPackage(uid, packageName, sender.isUidActive(uid));
            if (!sender.areAlarmsRestricted(uid, packageName)) {
                unblockAlarmsForUidPackage(uid, packageName);
            }
            if (!sender.isRunAnyInBackgroundAppOpsAllowed(uid, packageName)) {
                android.util.Slog.v("AppStateTracker", "Package " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + " toggled into fg service restriction");
                updateBackgroundRestrictedForUidPackage(uid, packageName, true);
            } else {
                android.util.Slog.v("AppStateTracker", "Package " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + " toggled out of fg service restriction");
                updateBackgroundRestrictedForUidPackage(uid, packageName, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onUidActiveStateChanged(com.android.server.AppStateTrackerImpl sender, int uid) {
            boolean isActive = sender.isUidActive(uid);
            updateJobsForUid(uid, isActive);
            updateAlarmsForUid(uid);
            if (isActive) {
                unblockAlarmsForUid(uid);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onPowerSaveUnexempted(com.android.server.AppStateTrackerImpl sender) {
            updateAllJobs();
            updateAllAlarms();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onPowerSaveExemptionListChanged(com.android.server.AppStateTrackerImpl sender) {
            updateAllJobs();
            updateAllAlarms();
            unblockAllUnrestrictedAlarms();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onTempPowerSaveExemptionListChanged(com.android.server.AppStateTrackerImpl sender) {
            updateAllJobs();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onExemptedBucketChanged(com.android.server.AppStateTrackerImpl sender) {
            updateAllJobs();
            updateAllAlarms();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onForceAllAppsStandbyChanged(com.android.server.AppStateTrackerImpl sender) {
            updateAllJobs();
            updateAllAlarms();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAutoRestrictedBucketFeatureFlagChanged(com.android.server.AppStateTrackerImpl sender, boolean autoRestrictedBucket) {
            updateAllJobs();
            if (autoRestrictedBucket) {
                unblockAllUnrestrictedAlarms();
            }
        }

        public void updateAllJobs() {
        }

        public void updateJobsForUid(int uid, boolean isNowActive) {
        }

        public void updateJobsForUidPackage(int uid, java.lang.String packageName, boolean isNowActive) {
        }

        public void updateBackgroundRestrictedForUidPackage(int uid, java.lang.String packageName, boolean restricted) {
        }

        public void updateAllAlarms() {
        }

        public void updateAlarmsForUid(int uid) {
        }

        public void unblockAllUnrestrictedAlarms() {
        }

        public void unblockAlarmsForUid(int uid) {
        }

        public void unblockAlarmsForUidPackage(int uid, java.lang.String packageName) {
        }

        public void removeAlarmsForUid(int uid) {
        }

        public void handleUidCachedChanged(int uid, boolean cached) {
        }
    }

    public AppStateTrackerImpl(android.content.Context context, android.os.Looper looper) {
        this.mContext = context;
        this.mHandler = new com.android.server.AppStateTrackerImpl.MyHandler(looper);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onSystemServicesReady() {
        synchronized (this.mLock) {
            if (this.mStarted) {
                return;
            }
            this.mStarted = true;
            this.mIActivityManager = (android.app.IActivityManager) java.util.Objects.requireNonNull(injectIActivityManager());
            this.mActivityManagerInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull(injectActivityManagerInternal());
            this.mAppOpsManager = (android.app.AppOpsManager) java.util.Objects.requireNonNull(injectAppOpsManager());
            this.mAppOpsService = (com.android.internal.app.IAppOpsService) java.util.Objects.requireNonNull(injectIAppOpsService());
            this.mPowerManagerInternal = (android.os.PowerManagerInternal) java.util.Objects.requireNonNull(injectPowerManagerInternal());
            this.mAppStandbyInternal = (com.android.server.usage.AppStandbyInternal) java.util.Objects.requireNonNull(injectAppStandbyInternal());
            this.mFlagsObserver = new com.android.server.AppStateTrackerImpl.FeatureFlagsObserver();
            this.mFlagsObserver.register();
            this.mForceAllAppStandbyForSmallBattery = this.mFlagsObserver.isForcedAppStandbyForSmallBatteryEnabled();
            this.mStandbyTracker = new com.android.server.AppStateTrackerImpl.StandbyTracker();
            this.mAppStandbyInternal.addListener(this.mStandbyTracker);
            this.mActivityManagerInternal.addAppBackgroundRestrictionListener(this.mAppBackgroundRestrictionListener);
            try {
                this.mIActivityManager.registerUidObserver(new com.android.server.AppStateTrackerImpl.UidObserver(), 30, -1, (java.lang.String) null);
                this.mAppOpsService.startWatchingMode(70, (java.lang.String) null, new com.android.server.AppStateTrackerImpl.AppOpsWatcher());
            } catch (android.os.RemoteException e) {
            }
            android.content.IntentFilter intentFilter = new android.content.IntentFilter();
            intentFilter.addAction("android.intent.action.USER_REMOVED");
            intentFilter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            this.mContext.registerReceiver(this.mReceiver, intentFilter);
            android.content.IntentFilter intentFilter2 = new android.content.IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter2.addDataScheme("package");
            intentFilter2.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
            this.mContext.registerReceiver(this.mReceiver, intentFilter2);
            refreshForcedAppStandbyUidPackagesLocked();
            this.mPowerManagerInternal.registerLowPowerModeObserver(11, new java.util.function.Consumer() { // from class: com.android.server.AppStateTrackerImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onSystemServicesReady$0((android.os.PowerSaveState) obj);
                }
            });
            this.mBatterySaverEnabled = this.mPowerManagerInternal.getLowPowerState(11).batterySaverEnabled;
            updateForceAllAppStandbyState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemServicesReady$0(android.os.PowerSaveState state) {
        synchronized (this.mLock) {
            this.mBatterySaverEnabled = state.batterySaverEnabled;
            updateForceAllAppStandbyState();
        }
    }

    android.app.AppOpsManager injectAppOpsManager() {
        return (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
    }

    com.android.internal.app.IAppOpsService injectIAppOpsService() {
        return com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
    }

    android.app.IActivityManager injectIActivityManager() {
        return android.app.ActivityManager.getService();
    }

    android.app.ActivityManagerInternal injectActivityManagerInternal() {
        return (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
    }

    android.os.PowerManagerInternal injectPowerManagerInternal() {
        return (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
    }

    com.android.server.usage.AppStandbyInternal injectAppStandbyInternal() {
        return (com.android.server.usage.AppStandbyInternal) com.android.server.LocalServices.getService(com.android.server.usage.AppStandbyInternal.class);
    }

    boolean isSmallBatteryDevice() {
        return android.app.ActivityManager.isSmallBatteryDevice();
    }

    int injectGetGlobalSettingInt(java.lang.String key, int def) {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), key, def);
    }

    private void refreshForcedAppStandbyUidPackagesLocked() {
        this.mRunAnyRestrictedPackages.clear();
        java.util.List<android.app.AppOpsManager.PackageOps> ops = this.mAppOpsManager.getPackagesForOps(new int[]{70});
        if (ops == null) {
            return;
        }
        int size = ops.size();
        for (int i = 0; i < size; i++) {
            android.app.AppOpsManager.PackageOps pkg = ops.get(i);
            java.util.List<android.app.AppOpsManager.OpEntry> entries = ops.get(i).getOps();
            for (int j = 0; j < entries.size(); j++) {
                android.app.AppOpsManager.OpEntry ent = entries.get(j);
                if (ent.getOp() == 70 && ent.getMode() != 0) {
                    this.mRunAnyRestrictedPackages.add(android.util.Pair.create(java.lang.Integer.valueOf(pkg.getUid()), pkg.getPackageName()));
                }
            }
        }
        updateBackgroundRestrictedUidPackagesLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBackgroundRestrictedUidPackagesLocked() {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> fasUidPkgs = new android.util.ArraySet<>();
        int size = this.mRunAnyRestrictedPackages.size();
        for (int i = 0; i < size; i++) {
            fasUidPkgs.add(this.mRunAnyRestrictedPackages.valueAt(i));
        }
        this.mBackgroundRestrictedUidPackages = java.util.Collections.unmodifiableSet(fasUidPkgs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForceAllAppStandbyState() {
        synchronized (this.mLock) {
            if (this.mForceAllAppStandbyForSmallBattery && isSmallBatteryDevice()) {
                toggleForceAllAppsStandbyLocked(!this.mIsPluggedIn);
            } else {
                toggleForceAllAppsStandbyLocked(this.mBatterySaverEnabled);
            }
        }
    }

    private void toggleForceAllAppsStandbyLocked(boolean enable) {
        if (enable == this.mForceAllAppsStandby) {
            return;
        }
        this.mForceAllAppsStandby = enable;
        this.mHandler.notifyForceAllAppsStandbyChanged();
    }

    private int findForcedAppStandbyUidPackageIndexLocked(int uid, java.lang.String packageName) {
        int size = this.mRunAnyRestrictedPackages.size();
        if (size > 8) {
            return this.mRunAnyRestrictedPackages.indexOf(android.util.Pair.create(java.lang.Integer.valueOf(uid), packageName));
        }
        for (int i = 0; i < size; i++) {
            android.util.Pair<java.lang.Integer, java.lang.String> pair = this.mRunAnyRestrictedPackages.valueAt(i);
            if (((java.lang.Integer) pair.first).intValue() == uid && packageName.equals(pair.second)) {
                return i;
            }
        }
        return -1;
    }

    boolean isRunAnyRestrictedLocked(int uid, java.lang.String packageName) {
        return findForcedAppStandbyUidPackageIndexLocked(uid, packageName) >= 0;
    }

    boolean updateForcedAppStandbyUidPackageLocked(int uid, java.lang.String packageName, boolean restricted) {
        int index = findForcedAppStandbyUidPackageIndexLocked(uid, packageName);
        boolean wasRestricted = index >= 0;
        if (wasRestricted == restricted) {
            return false;
        }
        if (restricted) {
            this.mRunAnyRestrictedPackages.add(android.util.Pair.create(java.lang.Integer.valueOf(uid), packageName));
        } else {
            this.mRunAnyRestrictedPackages.removeAt(index);
        }
        updateBackgroundRestrictedUidPackagesLocked();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean addUidToArray(android.util.SparseBooleanArray array, int uid) {
        if (android.os.UserHandle.isCore(uid) || array.get(uid)) {
            return false;
        }
        array.put(uid, true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean removeUidFromArray(android.util.SparseBooleanArray array, int uid, boolean remove) {
        if (android.os.UserHandle.isCore(uid) || !array.get(uid)) {
            return false;
        }
        if (remove) {
            array.delete(uid);
            return true;
        }
        array.put(uid, false);
        return true;
    }

    private final class UidObserver extends android.app.UidObserver {
        private UidObserver() {
        }

        public void onUidActive(int uid) {
            com.android.server.AppStateTrackerImpl.this.mHandler.onUidActive(uid);
        }

        public void onUidGone(int uid, boolean disabled) {
            com.android.server.AppStateTrackerImpl.this.mHandler.onUidGone(uid, disabled);
        }

        public void onUidIdle(int uid, boolean disabled) {
            com.android.server.AppStateTrackerImpl.this.mHandler.onUidIdle(uid, disabled);
        }

        public void onUidCachedChanged(int uid, boolean cached) {
            com.android.server.AppStateTrackerImpl.this.mHandler.onUidCachedChanged(uid, cached);
        }
    }

    private final class AppOpsWatcher extends com.android.internal.app.IAppOpsCallback.Stub {
        private AppOpsWatcher() {
        }

        public void opChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) throws android.os.RemoteException {
            boolean restricted = false;
            try {
                restricted = com.android.server.AppStateTrackerImpl.this.mAppOpsService.checkOperation(70, uid, packageName) != 0;
            } catch (android.os.RemoteException e) {
            }
            if (restricted) {
                com.android.modules.expresslog.Counter.logIncrementWithUid(com.android.server.AppStateTrackerImpl.APP_RESTRICTION_COUNTER_METRIC_ID, uid);
            }
            synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                if (com.android.server.AppStateTrackerImpl.this.updateForcedAppStandbyUidPackageLocked(uid, packageName, restricted)) {
                    com.android.server.AppStateTrackerImpl.this.mHandler.notifyRunAnyAppOpsChanged(uid, packageName);
                }
            }
        }
    }

    final class StandbyTracker extends com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener {
        StandbyTracker() {
        }

        public void onAppIdleStateChanged(java.lang.String packageName, int userId, boolean idle, int bucket, int reason) {
            boolean changed;
            synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                if (bucket == 5) {
                    changed = com.android.server.AppStateTrackerImpl.this.mExemptedBucketPackages.add(userId, packageName);
                } else {
                    changed = com.android.server.AppStateTrackerImpl.this.mExemptedBucketPackages.remove(userId, packageName);
                }
                if (changed) {
                    com.android.server.AppStateTrackerImpl.this.mHandler.notifyExemptedBucketChanged();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.AppStateTrackerImpl.Listener[] cloneListeners() {
        com.android.server.AppStateTrackerImpl.Listener[] listenerArr;
        synchronized (this.mLock) {
            listenerArr = (com.android.server.AppStateTrackerImpl.Listener[]) this.mListeners.toArray(new com.android.server.AppStateTrackerImpl.Listener[this.mListeners.size()]);
        }
        return listenerArr;
    }

    private class MyHandler extends android.os.Handler {
        private static final int MSG_ALL_EXEMPTION_LIST_CHANGED = 5;
        private static final int MSG_ALL_UNEXEMPTED = 4;
        private static final int MSG_AUTO_RESTRICTED_BUCKET_FEATURE_FLAG_CHANGED = 11;
        private static final int MSG_EXEMPTED_BUCKET_CHANGED = 10;
        private static final int MSG_FORCE_ALL_CHANGED = 7;
        private static final int MSG_ON_UID_ACTIVE = 12;
        private static final int MSG_ON_UID_CACHED = 15;
        private static final int MSG_ON_UID_GONE = 13;
        private static final int MSG_ON_UID_IDLE = 14;
        private static final int MSG_RUN_ANY_CHANGED = 3;
        private static final int MSG_TEMP_EXEMPTION_LIST_CHANGED = 6;
        private static final int MSG_UID_ACTIVE_STATE_CHANGED = 0;
        private static final int MSG_USER_REMOVED = 8;

        MyHandler(android.os.Looper looper) {
            super(looper);
        }

        public void notifyUidActiveStateChanged(int uid) {
            obtainMessage(0, uid, 0).sendToTarget();
        }

        public void notifyRunAnyAppOpsChanged(int uid, java.lang.String packageName) {
            obtainMessage(3, uid, 0, packageName).sendToTarget();
        }

        public void notifyAllUnexempted() {
            removeMessages(4);
            obtainMessage(4).sendToTarget();
        }

        public void notifyAllExemptionListChanged() {
            removeMessages(5);
            obtainMessage(5).sendToTarget();
        }

        public void notifyTempExemptionListChanged() {
            removeMessages(6);
            obtainMessage(6).sendToTarget();
        }

        public void notifyForceAllAppsStandbyChanged() {
            removeMessages(7);
            obtainMessage(7).sendToTarget();
        }

        public void notifyExemptedBucketChanged() {
            removeMessages(10);
            obtainMessage(10).sendToTarget();
        }

        public void notifyAutoRestrictedBucketFeatureFlagChanged(boolean z) {
            removeMessages(11);
            obtainMessage(11, z ? 1 : 0, 0).sendToTarget();
        }

        public void doUserRemoved(int userId) {
            obtainMessage(8, userId, 0).sendToTarget();
        }

        public void onUidActive(int uid) {
            obtainMessage(12, uid, 0).sendToTarget();
        }

        public void onUidGone(int i, boolean z) {
            obtainMessage(13, i, z ? 1 : 0).sendToTarget();
        }

        public void onUidIdle(int i, boolean z) {
            obtainMessage(14, i, z ? 1 : 0).sendToTarget();
        }

        public void onUidCachedChanged(int i, boolean z) {
            obtainMessage(15, i, z ? 1 : 0).sendToTarget();
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 8:
                    com.android.server.AppStateTrackerImpl.this.handleUserRemoved(message.arg1);
                    return;
                default:
                    synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                        if (com.android.server.AppStateTrackerImpl.this.mStarted) {
                            com.android.server.AppStateTrackerImpl appStateTrackerImpl = com.android.server.AppStateTrackerImpl.this;
                            long time = com.android.server.AppStateTrackerImpl.this.mStatLogger.getTime();
                            switch (message.what) {
                                case 0:
                                    for (com.android.server.AppStateTrackerImpl.Listener listener : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener.onUidActiveStateChanged(appStateTrackerImpl, message.arg1);
                                    }
                                    com.android.server.AppStateTrackerImpl.this.mStatLogger.logDurationStat(1, time);
                                    return;
                                case 1:
                                case 2:
                                case 9:
                                default:
                                    return;
                                case 3:
                                    for (com.android.server.AppStateTrackerImpl.Listener listener2 : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener2.onRunAnyAppOpsChanged(appStateTrackerImpl, message.arg1, (java.lang.String) message.obj);
                                    }
                                    com.android.server.AppStateTrackerImpl.this.mStatLogger.logDurationStat(2, time);
                                    return;
                                case 4:
                                    for (com.android.server.AppStateTrackerImpl.Listener listener3 : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener3.onPowerSaveUnexempted(appStateTrackerImpl);
                                    }
                                    com.android.server.AppStateTrackerImpl.this.mStatLogger.logDurationStat(3, time);
                                    return;
                                case 5:
                                    for (com.android.server.AppStateTrackerImpl.Listener listener4 : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener4.onPowerSaveExemptionListChanged(appStateTrackerImpl);
                                    }
                                    com.android.server.AppStateTrackerImpl.this.mStatLogger.logDurationStat(4, time);
                                    return;
                                case 6:
                                    for (com.android.server.AppStateTrackerImpl.Listener listener5 : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener5.onTempPowerSaveExemptionListChanged(appStateTrackerImpl);
                                    }
                                    com.android.server.AppStateTrackerImpl.this.mStatLogger.logDurationStat(5, time);
                                    return;
                                case 7:
                                    for (com.android.server.AppStateTrackerImpl.Listener listener6 : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener6.onForceAllAppsStandbyChanged(appStateTrackerImpl);
                                    }
                                    com.android.server.AppStateTrackerImpl.this.mStatLogger.logDurationStat(7, time);
                                    return;
                                case 8:
                                    com.android.server.AppStateTrackerImpl.this.handleUserRemoved(message.arg1);
                                    return;
                                case 10:
                                    for (com.android.server.AppStateTrackerImpl.Listener listener7 : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener7.onExemptedBucketChanged(appStateTrackerImpl);
                                    }
                                    com.android.server.AppStateTrackerImpl.this.mStatLogger.logDurationStat(6, time);
                                    return;
                                case 11:
                                    boolean z = message.arg1 == 1;
                                    for (com.android.server.AppStateTrackerImpl.Listener listener8 : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                                        listener8.onAutoRestrictedBucketFeatureFlagChanged(appStateTrackerImpl, z);
                                    }
                                    return;
                                case 12:
                                    handleUidActive(message.arg1);
                                    return;
                                case 13:
                                    handleUidGone(message.arg1);
                                    if (message.arg2 != 0) {
                                        handleUidDisabled(message.arg1);
                                        return;
                                    }
                                    return;
                                case 14:
                                    handleUidIdle(message.arg1);
                                    if (message.arg2 != 0) {
                                        handleUidDisabled(message.arg1);
                                        return;
                                    }
                                    return;
                                case 15:
                                    handleUidCached(message.arg1, message.arg2 != 0);
                                    return;
                            }
                        }
                        return;
                    }
            }
        }

        private void handleUidCached(int uid, boolean cached) {
            for (com.android.server.AppStateTrackerImpl.Listener l : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                l.handleUidCachedChanged(uid, cached);
            }
        }

        private void handleUidDisabled(int uid) {
            for (com.android.server.AppStateTrackerImpl.Listener l : com.android.server.AppStateTrackerImpl.this.cloneListeners()) {
                l.removeAlarmsForUid(uid);
            }
        }

        public void handleUidActive(int uid) {
            synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                if (com.android.server.AppStateTrackerImpl.addUidToArray(com.android.server.AppStateTrackerImpl.this.mActiveUids, uid)) {
                    com.android.server.AppStateTrackerImpl.this.mHandler.notifyUidActiveStateChanged(uid);
                }
            }
        }

        public void handleUidGone(int uid) {
            removeUid(uid, true);
        }

        public void handleUidIdle(int uid) {
            removeUid(uid, false);
        }

        private void removeUid(int uid, boolean remove) {
            synchronized (com.android.server.AppStateTrackerImpl.this.mLock) {
                if (com.android.server.AppStateTrackerImpl.removeUidFromArray(com.android.server.AppStateTrackerImpl.this.mActiveUids, uid, remove)) {
                    com.android.server.AppStateTrackerImpl.this.mHandler.notifyUidActiveStateChanged(uid);
                }
            }
        }
    }

    void handleUserRemoved(int removedUserId) {
        synchronized (this.mLock) {
            for (int i = this.mRunAnyRestrictedPackages.size() - 1; i >= 0; i--) {
                android.util.Pair<java.lang.Integer, java.lang.String> pair = this.mRunAnyRestrictedPackages.valueAt(i);
                int uid = ((java.lang.Integer) pair.first).intValue();
                int userId = android.os.UserHandle.getUserId(uid);
                if (userId == removedUserId) {
                    this.mRunAnyRestrictedPackages.removeAt(i);
                }
            }
            updateBackgroundRestrictedUidPackagesLocked();
            cleanUpArrayForUser(this.mActiveUids, removedUserId);
            this.mExemptedBucketPackages.remove(removedUserId);
        }
    }

    private void cleanUpArrayForUser(android.util.SparseBooleanArray array, int removedUserId) {
        for (int i = array.size() - 1; i >= 0; i--) {
            int uid = array.keyAt(i);
            int userId = android.os.UserHandle.getUserId(uid);
            if (userId == removedUserId) {
                array.removeAt(i);
            }
        }
    }

    public void setPowerSaveExemptionListAppIds(int[] powerSaveExemptionListExceptIdleAppIdArray, int[] powerSaveExemptionListUserAppIdArray, int[] tempExemptionListAppIdArray) {
        synchronized (this.mLock) {
            int[] previousExemptionList = this.mPowerExemptAllAppIds;
            int[] previousTempExemptionList = this.mTempExemptAppIds;
            this.mPowerExemptAllAppIds = powerSaveExemptionListExceptIdleAppIdArray;
            this.mTempExemptAppIds = tempExemptionListAppIdArray;
            this.mPowerExemptUserAppIds = powerSaveExemptionListUserAppIdArray;
            if (isAnyAppIdUnexempt(previousExemptionList, this.mPowerExemptAllAppIds)) {
                this.mHandler.notifyAllUnexempted();
            } else if (!java.util.Arrays.equals(previousExemptionList, this.mPowerExemptAllAppIds)) {
                this.mHandler.notifyAllExemptionListChanged();
            }
            if (!java.util.Arrays.equals(previousTempExemptionList, this.mTempExemptAppIds)) {
                this.mHandler.notifyTempExemptionListChanged();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0027, code lost:
    
        if (r2 == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0029, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x002a, code lost:
    
        return r5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean isAnyAppIdUnexempt(int[] r7, int[] r8) {
        /*
            r0 = 0
            r1 = 0
        L2:
            int r2 = r7.length
            r3 = 0
            r4 = 1
            if (r0 < r2) goto L9
            r2 = r4
            goto La
        L9:
            r2 = r3
        La:
            int r5 = r8.length
            if (r1 < r5) goto Lf
            r5 = r4
            goto L10
        Lf:
            r5 = r3
        L10:
            if (r2 != 0) goto L27
            if (r5 == 0) goto L15
            goto L27
        L15:
            r3 = r7[r0]
            r6 = r8[r1]
            if (r3 != r6) goto L20
            int r0 = r0 + 1
            int r1 = r1 + 1
            goto L2
        L20:
            if (r3 >= r6) goto L23
            return r4
        L23:
            int r1 = r1 + 1
            goto L2
        L27:
            if (r2 == 0) goto L2a
            return r3
        L2a:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.AppStateTrackerImpl.isAnyAppIdUnexempt(int[], int[]):boolean");
    }

    public void addListener(com.android.server.AppStateTrackerImpl.Listener listener) {
        synchronized (this.mLock) {
            this.mListeners.add(listener);
        }
    }

    public boolean areAlarmsRestricted(int uid, java.lang.String packageName) {
        boolean z = false;
        if (isUidActive(uid)) {
            return false;
        }
        synchronized (this.mLock) {
            int appId = android.os.UserHandle.getAppId(uid);
            if (com.android.internal.util.ArrayUtils.contains(this.mPowerExemptAllAppIds, appId)) {
                return false;
            }
            if (!this.mActivityManagerInternal.isBgAutoRestrictedBucketFeatureFlagEnabled() && isRunAnyRestrictedLocked(uid, packageName)) {
                z = true;
            }
            return z;
        }
    }

    public boolean areAlarmsRestrictedByBatterySaver(int uid, java.lang.String packageName) {
        if (isUidActive(uid)) {
            return false;
        }
        synchronized (this.mLock) {
            int appId = android.os.UserHandle.getAppId(uid);
            if (com.android.internal.util.ArrayUtils.contains(this.mPowerExemptAllAppIds, appId)) {
                return false;
            }
            int userId = android.os.UserHandle.getUserId(uid);
            if (this.mAppStandbyInternal.isAppIdleEnabled() && !this.mAppStandbyInternal.isInParole() && this.mExemptedBucketPackages.contains(userId, packageName)) {
                return false;
            }
            return this.mForceAllAppsStandby;
        }
    }

    public boolean areJobsRestricted(int uid, java.lang.String packageName, boolean hasForegroundExemption) {
        if (isUidActive(uid)) {
            return false;
        }
        synchronized (this.mLock) {
            int appId = android.os.UserHandle.getAppId(uid);
            if (!com.android.internal.util.ArrayUtils.contains(this.mPowerExemptAllAppIds, appId) && !com.android.internal.util.ArrayUtils.contains(this.mTempExemptAppIds, appId)) {
                if (!this.mActivityManagerInternal.isBgAutoRestrictedBucketFeatureFlagEnabled() && isRunAnyRestrictedLocked(uid, packageName)) {
                    return true;
                }
                if (hasForegroundExemption) {
                    return false;
                }
                int userId = android.os.UserHandle.getUserId(uid);
                if (this.mAppStandbyInternal.isAppIdleEnabled() && !this.mAppStandbyInternal.isInParole() && this.mExemptedBucketPackages.contains(userId, packageName)) {
                    return false;
                }
                return this.mForceAllAppsStandby;
            }
            return false;
        }
    }

    public boolean isUidActive(int uid) {
        boolean z;
        if (android.os.UserHandle.isCore(uid)) {
            return true;
        }
        synchronized (this.mLock) {
            z = this.mActiveUids.get(uid);
        }
        return z;
    }

    public boolean isUidActiveSynced(int uid) {
        if (isUidActive(uid)) {
            return true;
        }
        long start = this.mStatLogger.getTime();
        boolean ret = this.mActivityManagerInternal.isUidActive(uid);
        this.mStatLogger.logDurationStat(9, start);
        return ret;
    }

    public boolean isForceAllAppsStandbyEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mForceAllAppsStandby;
        }
        return z;
    }

    public boolean isRunAnyInBackgroundAppOpsAllowed(int uid, java.lang.String packageName) {
        boolean z;
        synchronized (this.mLock) {
            z = !isRunAnyRestrictedLocked(uid, packageName);
        }
        return z;
    }

    public boolean isUidPowerSaveExempt(int uid) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = com.android.internal.util.ArrayUtils.contains(this.mPowerExemptAllAppIds, android.os.UserHandle.getAppId(uid));
        }
        return zContains;
    }

    public boolean isUidPowerSaveUserExempt(int uid) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = com.android.internal.util.ArrayUtils.contains(this.mPowerExemptUserAppIds, android.os.UserHandle.getAppId(uid));
        }
        return zContains;
    }

    public boolean isUidTempPowerSaveExempt(int uid) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = com.android.internal.util.ArrayUtils.contains(this.mTempExemptAppIds, android.os.UserHandle.getAppId(uid));
        }
        return zContains;
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("Current AppStateTracker State:");
            pw.increaseIndent();
            pw.print("Force all apps standby: ");
            pw.println(isForceAllAppsStandbyEnabled());
            pw.print("Small Battery Device: ");
            pw.println(isSmallBatteryDevice());
            pw.print("Force all apps standby for small battery device: ");
            pw.println(this.mForceAllAppStandbyForSmallBattery);
            pw.print("Plugged In: ");
            pw.println(this.mIsPluggedIn);
            pw.print("Active uids: ");
            dumpUids(pw, this.mActiveUids);
            pw.print("Except-idle + user exemption list appids: ");
            pw.println(java.util.Arrays.toString(this.mPowerExemptAllAppIds));
            pw.print("User exemption list appids: ");
            pw.println(java.util.Arrays.toString(this.mPowerExemptUserAppIds));
            pw.print("Temp exemption list appids: ");
            pw.println(java.util.Arrays.toString(this.mTempExemptAppIds));
            pw.println("Exempted bucket packages:");
            pw.increaseIndent();
            for (int i = 0; i < this.mExemptedBucketPackages.size(); i++) {
                pw.print("User ");
                pw.print(this.mExemptedBucketPackages.keyAt(i));
                pw.println();
                pw.increaseIndent();
                for (int j = 0; j < this.mExemptedBucketPackages.sizeAt(i); j++) {
                    pw.print((java.lang.String) this.mExemptedBucketPackages.valueAt(i, j));
                    pw.println();
                }
                pw.decreaseIndent();
            }
            pw.decreaseIndent();
            pw.println();
            pw.println("Restricted packages:");
            pw.increaseIndent();
            for (android.util.Pair<java.lang.Integer, java.lang.String> uidAndPackage : this.mRunAnyRestrictedPackages) {
                pw.print(android.os.UserHandle.formatUid(((java.lang.Integer) uidAndPackage.first).intValue()));
                pw.print(" ");
                pw.print((java.lang.String) uidAndPackage.second);
                pw.println();
            }
            pw.decreaseIndent();
            this.mStatLogger.dump(pw);
            pw.decreaseIndent();
        }
    }

    private void dumpUids(java.io.PrintWriter pw, android.util.SparseBooleanArray array) {
        pw.print("[");
        java.lang.String sep = "";
        for (int i = 0; i < array.size(); i++) {
            if (array.valueAt(i)) {
                pw.print(sep);
                pw.print(android.os.UserHandle.formatUid(array.keyAt(i)));
                sep = " ";
            }
        }
        pw.println("]");
    }

    public void dumpProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        synchronized (this.mLock) {
            long token = proto.start(fieldId);
            proto.write(1133871366145L, isForceAllAppsStandbyEnabled());
            proto.write(1133871366150L, isSmallBatteryDevice());
            proto.write(1133871366151L, this.mForceAllAppStandbyForSmallBattery);
            proto.write(1133871366152L, this.mIsPluggedIn);
            for (int i = 0; i < this.mActiveUids.size(); i++) {
                if (this.mActiveUids.valueAt(i)) {
                    proto.write(2220498092034L, this.mActiveUids.keyAt(i));
                }
            }
            for (int appId : this.mPowerExemptAllAppIds) {
                proto.write(2220498092035L, appId);
            }
            for (int appId2 : this.mPowerExemptUserAppIds) {
                proto.write(2220498092044L, appId2);
            }
            for (int appId3 : this.mTempExemptAppIds) {
                proto.write(2220498092036L, appId3);
            }
            for (int i2 = 0; i2 < this.mExemptedBucketPackages.size(); i2++) {
                for (int j = 0; j < this.mExemptedBucketPackages.sizeAt(i2); j++) {
                    long token2 = proto.start(2246267895818L);
                    proto.write(1120986464257L, this.mExemptedBucketPackages.keyAt(i2));
                    proto.write(1138166333442L, (java.lang.String) this.mExemptedBucketPackages.valueAt(i2, j));
                    proto.end(token2);
                }
            }
            for (android.util.Pair<java.lang.Integer, java.lang.String> uidAndPackage : this.mRunAnyRestrictedPackages) {
                long token22 = proto.start(2246267895813L);
                proto.write(1120986464257L, ((java.lang.Integer) uidAndPackage.first).intValue());
                proto.write(1138166333442L, (java.lang.String) uidAndPackage.second);
                proto.end(token22);
            }
            this.mStatLogger.dumpProto(proto, 1146756268041L);
            proto.end(token);
        }
    }
}
