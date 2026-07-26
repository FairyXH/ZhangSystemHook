package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class ActiveServices {
    static final int DEFAULT_SERVICE_CACHED_BIND_POLICY_FLAG = 3;
    static final int DEFAULT_SERVICE_NO_BUMP_BIND_POLICY_FLAG = 7;
    static final long FGS_BG_START_RESTRICTION_CHANGE_ID = 170668199;
    public static final long FGS_BOOT_COMPLETED_RESTRICTIONS = 296558535;
    static final int FGS_IMMEDIATE_DISPLAY_MASK = 54;
    static final long FGS_INTRODUCE_TIME_LIMITS = 317799821;
    public static final long FGS_SAW_RESTRICTIONS = 319471980;
    static final long FGS_START_EXCEPTION_CHANGE_ID = 174041399;
    static final int FGS_STOP_REASON_STOP_FOREGROUND = 1;
    static final int FGS_STOP_REASON_STOP_SERVICE = 2;
    static final int FGS_STOP_REASON_UNKNOWN = 0;
    static final long FGS_TYPE_CHECK_FOR_INSTANT_APPS = 261055255;
    static final int LAST_ANR_LIFETIME_DURATION_MSECS = 7200000;
    static final int SERVICE_BIND_OOMADJ_POLICY_FREEZE_CALLER = 8;
    static final int SERVICE_BIND_OOMADJ_POLICY_LEGACY = 0;
    static final int SERVICE_BIND_OOMADJ_POLICY_SKIP_OOM_UPDATE_ON_BIND = 2;
    static final int SERVICE_BIND_OOMADJ_POLICY_SKIP_OOM_UPDATE_ON_CONNECT = 4;
    static final int SERVICE_BIND_OOMADJ_POLICY_SKIP_OOM_UPDATE_ON_CREATE = 1;
    private static final boolean SHOW_DUNGEON_NOTIFICATION = false;
    private static final java.lang.String TAG = "ActivityManager";
    private static final java.lang.String TAG_MU = "ActivityManager_MU";
    private final com.android.server.am.ActiveServices.ProcessAnrTimer mActiveServiceAnrTimer;
    final com.android.server.am.ActivityManagerService mAm;
    com.android.server.AppStateTracker mAppStateTracker;
    android.appwidget.AppWidgetManagerInternal mAppWidgetManagerInternal;
    java.lang.String mCachedDeviceProvisioningPackage;
    private final com.android.server.am.ForegroundServiceTypeLoggerModule mFGSLogger;
    java.lang.String mLastAnrDump;
    final int mMaxStartingBackground;
    private final com.android.server.am.ActiveServices.ServiceAnrTimer mServiceFGAnrTimer;
    private final com.android.server.am.ActiveServices.ServiceAnrTimer mShortFGSAnrTimer;
    static final java.lang.String TAG_SERVICE = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_SERVICE;
    private static final java.lang.String TAG_SERVICE_EXECUTING = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_SERVICE_EXECUTING;
    private static boolean DEBUG_DELAYED_SERVICE = com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE;
    private static boolean DEBUG_DELAYED_STARTS = DEBUG_DELAYED_SERVICE;
    private static boolean LOG_SERVICE_START_STOP = com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE;
    private static final boolean DEBUG_SHORT_SERVICE = com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE;
    private static boolean DEBUG_PANIC_FLAG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final java.util.concurrent.atomic.AtomicReference<android.util.Pair<java.lang.Integer, java.lang.Integer>> sNumForegroundServices = new java.util.concurrent.atomic.AtomicReference<>(new android.util.Pair(0, 0));
    private static final java.text.SimpleDateFormat DATE_FORMATTER = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final android.util.SparseArray<com.android.server.am.ActiveServices.ServiceMap> mServiceMap = new android.util.SparseArray<>();
    final android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> mServiceConnections = new android.util.ArrayMap<>();
    final java.util.ArrayList<com.android.server.am.ServiceRecord> mPendingServices = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.am.ServiceRecord> mRestartingServices = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.am.ServiceRecord> mDestroyingServices = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.am.ServiceRecord> mPendingFgsNotifications = new java.util.ArrayList<>();
    final android.util.ArrayMap<com.android.server.am.ForegroundServiceDelegation, com.android.server.am.ServiceRecord> mFgsDelegations = new android.util.ArrayMap<>();
    private long mBindServiceSeqCounter = 0;
    private boolean mFgsDeferralRateLimited = true;
    final android.util.SparseLongArray mFgsDeferralEligible = new android.util.SparseLongArray();
    final android.os.RemoteCallbackList<android.app.IForegroundServiceObserver> mFgsObservers = new android.os.RemoteCallbackList<>();
    private android.util.ArrayMap<com.android.server.am.ServiceRecord, java.util.ArrayList<java.lang.Runnable>> mPendingBringups = new android.util.ArrayMap<>();
    private java.util.ArrayList<com.android.server.am.ServiceRecord> mTmpCollectionResults = null;
    private final android.util.SparseArray<com.android.server.am.ActiveServices.AppOpCallback> mFgsAppOpCallbacks = new android.util.SparseArray<>();
    private final android.util.ArraySet<java.lang.String> mRestartBackoffDisabledPackages = new android.util.ArraySet<>();
    boolean mScreenOn = true;
    final android.util.SparseArray<android.util.SparseArray<com.android.server.am.ServiceRecord.TimeLimitedFgsInfo>> mTimeLimitedFgsInfo = new android.util.SparseArray<>();
    android.util.ArraySet<java.lang.String> mAllowListWhileInUsePermissionInFgs = new android.util.ArraySet<>();
    final java.lang.Runnable mLastAnrDumpClearer = new java.lang.Runnable() { // from class: com.android.server.am.ActiveServices.1
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActiveServices.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActiveServices.this.mLastAnrDump = null;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    };
    private final java.lang.Runnable mPostDeferredFGSNotifications = new java.lang.Runnable() { // from class: com.android.server.am.ActiveServices.5
        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.d(com.android.server.am.ActiveServices.TAG_SERVICE, "+++ evaluating deferred FGS notifications +++");
            }
            long now = android.os.SystemClock.uptimeMillis();
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActiveServices.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    for (int i = com.android.server.am.ActiveServices.this.mPendingFgsNotifications.size() - 1; i >= 0; i--) {
                        com.android.server.am.ServiceRecord r = com.android.server.am.ActiveServices.this.mPendingFgsNotifications.get(i);
                        if (r.fgDisplayTime <= now) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                                android.util.Slog.d(com.android.server.am.ActiveServices.TAG_SERVICE, "FGS " + r + " handling deferred notification now");
                            }
                            com.android.server.am.ActiveServices.this.mPendingFgsNotifications.remove(i);
                            if (r.isForeground && r.app != null) {
                                r.postNotification(true);
                                r.mFgsNotificationShown = true;
                            } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                                android.util.Slog.d(com.android.server.am.ActiveServices.TAG_SERVICE, "  - service no longer running/fg, ignoring");
                            }
                        }
                    }
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        android.util.Slog.d(com.android.server.am.ActiveServices.TAG_SERVICE, "Done evaluating deferred FGS notifications; " + com.android.server.am.ActiveServices.this.mPendingFgsNotifications.size() + " remaining");
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    };
    private com.android.server.am.ActiveServices.ActiveServicesWrapper mActiveServicesWrapper = new com.android.server.am.ActiveServices.ActiveServicesWrapper();
    private com.android.server.am.IActiveServicesExt mActiveServicesExt = (com.android.server.am.IActiveServicesExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IActiveServicesExt.class).base(this).create();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface FgsStopReason {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface ServiceBindingOomAdjPolicy {
    }

    class BackgroundRestrictedListener implements com.android.server.AppStateTracker.BackgroundRestrictedAppListener {
        BackgroundRestrictedListener() {
        }

        public void updateBackgroundRestrictedForUidPackage(int uid, java.lang.String packageName, boolean restricted) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActiveServices.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActiveServices.this.mAm.mProcessList.updateBackgroundRestrictedForUidPackageLocked(uid, packageName, restricted);
                    if (!com.android.server.am.ActiveServices.this.isForegroundServiceAllowedInBackgroundRestricted(uid, packageName) && !com.android.server.am.ActiveServices.this.isTempAllowedByAlarmClock(uid)) {
                        com.android.server.am.ActiveServices.this.stopAllForegroundServicesLocked(uid, packageName);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    void stopAllForegroundServicesLocked(int uid, java.lang.String packageName) throws java.lang.Throwable {
        com.android.server.am.ActiveServices.ServiceMap smap = getServiceMapLocked(android.os.UserHandle.getUserId(uid));
        int N = smap.mServicesByInstanceName.size();
        java.util.ArrayList<com.android.server.am.ServiceRecord> toStop = new java.util.ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            com.android.server.am.ServiceRecord r = smap.mServicesByInstanceName.valueAt(i);
            if ((uid == r.serviceInfo.applicationInfo.uid || packageName.equals(r.serviceInfo.packageName)) && r.isForeground && r.mAllowStartForegroundAtEntering != 301 && !isDeviceProvisioningPackage(r.packageName)) {
                toStop.add(r);
            }
        }
        int numToStop = toStop.size();
        if (numToStop > 0 && com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            android.util.Slog.i("ActivityManager", "Package " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + " in FAS with foreground services");
        }
        for (int i2 = 0; i2 < numToStop; i2++) {
            com.android.server.am.ServiceRecord r2 = toStop.get(i2);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.i("ActivityManager", "  Stopping fg for service " + r2);
            }
            setServiceForegroundInnerLocked(r2, 0, null, 0, 0, 0);
        }
    }

    static final class ActiveForegroundApp {
        boolean mAppOnTop;
        long mEndTime;
        long mHideTime;
        java.lang.CharSequence mLabel;
        int mNumActive;
        java.lang.String mPackageName;
        boolean mShownWhileScreenOn;
        boolean mShownWhileTop;
        long mStartTime;
        long mStartVisibleTime;
        int mUid;

        ActiveForegroundApp() {
        }
    }

    final class ServiceMap extends android.os.Handler {
        static final int MSG_BG_START_TIMEOUT = 1;
        static final int MSG_ENSURE_NOT_START_BG = 3;
        static final int MSG_UPDATE_FOREGROUND_APPS = 2;
        final android.util.ArrayMap<java.lang.String, com.android.server.am.ActiveServices.ActiveForegroundApp> mActiveForegroundApps;
        boolean mActiveForegroundAppsChanged;
        final java.util.ArrayList<com.android.server.am.ServiceRecord> mDelayedStartList;
        final java.util.ArrayList<java.lang.String> mPendingRemoveForegroundApps;
        final android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> mServicesByInstanceName;
        final android.util.ArrayMap<android.content.Intent.FilterComparison, com.android.server.am.ServiceRecord> mServicesByIntent;
        final java.util.ArrayList<com.android.server.am.ServiceRecord> mStartingBackground;
        final int mUserId;

        ServiceMap(android.os.Looper looper, int userId) {
            super(looper);
            this.mServicesByInstanceName = new android.util.ArrayMap<>();
            this.mServicesByIntent = new android.util.ArrayMap<>();
            this.mDelayedStartList = new java.util.ArrayList<>();
            this.mStartingBackground = new java.util.ArrayList<>();
            this.mActiveForegroundApps = new android.util.ArrayMap<>();
            this.mPendingRemoveForegroundApps = new java.util.ArrayList<>();
            this.mUserId = userId;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActiveServices.this.mAm;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            rescheduleDelayedStartsLocked();
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                case 2:
                    com.android.server.am.ActiveServices.this.updateForegroundApps(this);
                    return;
                case 3:
                    com.android.server.am.ActivityManagerService activityManagerService2 = com.android.server.am.ActiveServices.this.mAm;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService2) {
                        try {
                            rescheduleDelayedStartsLocked();
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                default:
                    return;
            }
        }

        void ensureNotStartingBackgroundLocked(com.android.server.am.ServiceRecord r) {
            if (this.mStartingBackground.remove(r)) {
                if (com.android.server.am.ActiveServices.DEBUG_DELAYED_STARTS) {
                    android.util.Slog.v(com.android.server.am.ActiveServices.TAG_SERVICE, "No longer background starting: " + r);
                }
                removeMessages(3);
                android.os.Message msg = obtainMessage(3);
                sendMessage(msg);
            }
            if (!this.mDelayedStartList.remove(r) || !com.android.server.am.ActiveServices.DEBUG_DELAYED_STARTS) {
                return;
            }
            android.util.Slog.v(com.android.server.am.ActiveServices.TAG_SERVICE, "No longer delaying start: " + r);
        }

        void rescheduleDelayedStartsLocked() throws java.lang.Throwable {
            java.lang.String str;
            java.lang.String str2;
            removeMessages(1);
            long now = android.os.SystemClock.uptimeMillis();
            int i = 0;
            int N = this.mStartingBackground.size();
            while (true) {
                str = "ActivityManager";
                if (i >= N) {
                    break;
                }
                com.android.server.am.ServiceRecord r = this.mStartingBackground.get(i);
                if (r.startingBgTimeout <= now) {
                    android.util.Slog.i("ActivityManager", "Waited long enough for: " + r);
                    this.mStartingBackground.remove(i);
                    N--;
                    i--;
                }
                i++;
            }
            while (this.mDelayedStartList.size() > 0 && this.mStartingBackground.size() < com.android.server.am.ActiveServices.this.mMaxStartingBackground) {
                com.android.server.am.ServiceRecord r2 = this.mDelayedStartList.remove(0);
                if (com.android.server.am.ActiveServices.DEBUG_DELAYED_STARTS) {
                    android.util.Slog.v(com.android.server.am.ActiveServices.TAG_SERVICE, "REM FR DELAY LIST (exec next): " + r2);
                }
                if (com.android.server.am.ActiveServices.DEBUG_DELAYED_SERVICE && this.mDelayedStartList.size() > 0) {
                    android.util.Slog.v(com.android.server.am.ActiveServices.TAG_SERVICE, "Remaining delayed list:");
                    for (int i2 = 0; i2 < this.mDelayedStartList.size(); i2++) {
                        android.util.Slog.v(com.android.server.am.ActiveServices.TAG_SERVICE, "  #" + i2 + ": " + this.mDelayedStartList.get(i2));
                    }
                }
                r2.delayed = false;
                if (r2.pendingStarts.size() <= 0) {
                    android.util.Slog.wtf(str, "**** NO PENDING STARTS! " + r2 + " startReq=" + r2.startRequested + " delayedStop=" + r2.delayedStop);
                    str2 = str;
                } else {
                    try {
                        com.android.server.am.ServiceRecord.StartItem si = r2.pendingStarts.get(0);
                        str2 = str;
                        try {
                            com.android.server.am.ActiveServices.this.startServiceInnerLocked(this, si.intent, r2, false, true, si.callingId, si.mCallingProcessName, si.mCallingProcessState, r2.startRequested, si.mCallingPackageName);
                        } catch (android.os.TransactionTooLargeException e) {
                        }
                    } catch (android.os.TransactionTooLargeException e2) {
                        str2 = str;
                    }
                }
                str = str2;
            }
            if (this.mStartingBackground.size() > 0) {
                com.android.server.am.ServiceRecord next = this.mStartingBackground.get(0);
                long when = next.startingBgTimeout > now ? next.startingBgTimeout : now;
                if (com.android.server.am.ActiveServices.DEBUG_DELAYED_SERVICE) {
                    android.util.Slog.v(com.android.server.am.ActiveServices.TAG_SERVICE, "Top bg start is " + next + ", can delay others up to " + when);
                }
                android.os.Message msg = obtainMessage(1);
                sendMessageAtTime(msg, when);
            }
            if (this.mStartingBackground.size() < com.android.server.am.ActiveServices.this.mMaxStartingBackground) {
                com.android.server.am.ActiveServices.this.mAm.backgroundServicesFinishedLocked(this.mUserId);
            }
        }
    }

    public ActiveServices(com.android.server.am.ActivityManagerService service) {
        int i;
        this.mAm = service;
        int maxBg = 0;
        try {
            maxBg = java.lang.Integer.parseInt(android.os.SystemProperties.get("ro.config.max_starting_bg", "0"));
        } catch (java.lang.RuntimeException e) {
        }
        if (maxBg <= 0) {
            i = android.app.ActivityManager.isLowRamDeviceStatic() ? 1 : 8;
        } else {
            i = maxBg;
        }
        this.mMaxStartingBackground = i;
        android.os.ServiceManager.getService("platform_compat");
        this.mFGSLogger = new com.android.server.am.ForegroundServiceTypeLoggerModule();
        this.mActiveServiceAnrTimer = new com.android.server.am.ActiveServices.ProcessAnrTimer(service, 12, "SERVICE_TIMEOUT", new com.android.server.utils.AnrTimer.Args().freeze(true));
        this.mShortFGSAnrTimer = new com.android.server.am.ActiveServices.ServiceAnrTimer(service, 78, "SHORT_FGS_TIMEOUT");
        this.mServiceFGAnrTimer = new com.android.server.am.ActiveServices.ServiceAnrTimer(service, 66, "SERVICE_FOREGROUND_TIMEOUT");
    }

    void systemServicesReady() {
        getAppStateTracker().addBackgroundRestrictedAppListener(new com.android.server.am.ActiveServices.BackgroundRestrictedListener());
        this.mAppWidgetManagerInternal = (android.appwidget.AppWidgetManagerInternal) com.android.server.LocalServices.getService(android.appwidget.AppWidgetManagerInternal.class);
        setAllowListWhileInUsePermissionInFgs();
        initSystemExemptedFgsTypePermission();
        initMediaProjectFgsTypeCustomPermission();
    }

    private com.android.server.AppStateTracker getAppStateTracker() {
        if (this.mAppStateTracker == null) {
            this.mAppStateTracker = (com.android.server.AppStateTracker) com.android.server.LocalServices.getService(com.android.server.AppStateTracker.class);
        }
        return this.mAppStateTracker;
    }

    private void setAllowListWhileInUsePermissionInFgs() {
        java.lang.String attentionServicePackageName = this.mAm.mContext.getPackageManager().getAttentionServicePackageName();
        if (!android.text.TextUtils.isEmpty(attentionServicePackageName)) {
            this.mAllowListWhileInUsePermissionInFgs.add(attentionServicePackageName);
        }
        java.lang.String systemCaptionsServicePackageName = this.mAm.mContext.getPackageManager().getSystemCaptionsServicePackageName();
        if (!android.text.TextUtils.isEmpty(systemCaptionsServicePackageName)) {
            this.mAllowListWhileInUsePermissionInFgs.add(systemCaptionsServicePackageName);
        }
    }

    com.android.server.am.ServiceRecord getServiceByNameLocked(android.content.ComponentName name, int callingUser) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            android.util.Slog.v(TAG_MU, "getServiceByNameLocked(" + name + "), callingUser = " + callingUser);
        }
        return getServiceMapLocked(callingUser).mServicesByInstanceName.get(name);
    }

    boolean hasBackgroundServicesLocked(int callingUser) {
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(callingUser);
        return smap != null && smap.mStartingBackground.size() >= this.mMaxStartingBackground;
    }

    boolean hasForegroundServiceNotificationLocked(java.lang.String pkg, int userId, java.lang.String channelId) {
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(userId);
        if (smap != null) {
            for (int i = 0; i < smap.mServicesByInstanceName.size(); i++) {
                com.android.server.am.ServiceRecord sr = smap.mServicesByInstanceName.valueAt(i);
                if (sr.appInfo.packageName.equals(pkg) && sr.isForeground && sr.foregroundNoti != null && java.util.Objects.equals(sr.foregroundNoti.getChannelId(), channelId)) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        android.util.Slog.d(TAG_SERVICE, "Channel u" + userId + "/pkg=" + pkg + "/channelId=" + channelId + " has fg service notification");
                        return true;
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.am.ActiveServices.ServiceMap getServiceMapLocked(int callingUser) {
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(callingUser);
        if (smap == null) {
            com.android.server.am.ActiveServices.ServiceMap smap2 = new com.android.server.am.ActiveServices.ServiceMap(this.mAm.mHandler.getLooper(), callingUser);
            this.mServiceMap.put(callingUser, smap2);
            return smap2;
        }
        return smap;
    }

    android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> getServicesLocked(int callingUser) {
        return getServiceMapLocked(callingUser).mServicesByInstanceName;
    }

    private boolean appRestrictedAnyInBackground(int uid, java.lang.String packageName) {
        com.android.server.AppStateTracker appStateTracker = getAppStateTracker();
        if (appStateTracker != null) {
            return appStateTracker.isAppBackgroundRestricted(uid, packageName);
        }
        return false;
    }

    void updateAppRestrictedAnyInBackgroundLocked(int uid, java.lang.String packageName) {
        com.android.server.am.ProcessRecord app;
        boolean restricted = appRestrictedAnyInBackground(uid, packageName);
        com.android.server.am.UidRecord uidRec = this.mAm.mProcessList.getUidRecordLOSP(uid);
        if (uidRec != null && (app = uidRec.getProcessInPackage(packageName)) != null) {
            app.mState.setBackgroundRestricted(restricted);
        }
    }

    static java.lang.String getProcessNameForService(android.content.pm.ServiceInfo sInfo, android.content.ComponentName name, java.lang.String callingPackage, java.lang.String instanceName, boolean isSdkSandbox, boolean inSharedIsolatedProcess, boolean inPrivateSharedIsolatedProcess) {
        if (isSdkSandbox) {
            return instanceName;
        }
        if ((sInfo.flags & 2) == 0 || (inPrivateSharedIsolatedProcess && !isDefaultProcessService(sInfo))) {
            return sInfo.processName;
        }
        if (inSharedIsolatedProcess) {
            return callingPackage + ":ishared:" + instanceName;
        }
        return sInfo.processName + ":" + name.getClassName();
    }

    private static boolean isDefaultProcessService(android.content.pm.ServiceInfo serviceInfo) {
        return serviceInfo.applicationInfo.processName.equals(serviceInfo.processName);
    }

    private static void traceInstant(java.lang.String message, com.android.server.am.ServiceRecord service) {
        if (!android.os.Trace.isTagEnabled(64L)) {
            return;
        }
        java.lang.String serviceName = service.getComponentName() != null ? service.getComponentName().toShortString() : "(?)";
        android.os.Trace.instant(64L, message + serviceName);
    }

    android.content.ComponentName startServiceLocked(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, int callingPid, int callingUid, boolean fgRequired, java.lang.String callingPackage, java.lang.String callingFeatureId, int userId, boolean isSdkSandboxService, int sdkSandboxClientAppUid, java.lang.String sdkSandboxClientAppPackage, java.lang.String instanceName) throws android.os.TransactionTooLargeException {
        return startServiceLocked(caller, service, resolvedType, callingPid, callingUid, fgRequired, callingPackage, callingFeatureId, userId, android.app.BackgroundStartPrivileges.NONE, isSdkSandboxService, sdkSandboxClientAppUid, sdkSandboxClientAppPackage, instanceName);
    }

    android.content.ComponentName startServiceLocked(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, int callingPid, int callingUid, boolean fgRequired, java.lang.String callingPackage, java.lang.String callingFeatureId, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges) throws android.os.TransactionTooLargeException {
        return startServiceLocked(caller, service, resolvedType, callingPid, callingUid, fgRequired, callingPackage, callingFeatureId, userId, backgroundStartPrivileges, false, -1, null, null);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02d4  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0392 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0393  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0240  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x02c7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    android.content.ComponentName startServiceLocked(android.app.IApplicationThread r35, android.content.Intent r36, java.lang.String r37, int r38, int r39, boolean r40, java.lang.String r41, java.lang.String r42, int r43, android.app.BackgroundStartPrivileges r44, boolean r45, int r46, java.lang.String r47, java.lang.String r48) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1244
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.startServiceLocked(android.app.IApplicationThread, android.content.Intent, java.lang.String, int, int, boolean, java.lang.String, java.lang.String, int, android.app.BackgroundStartPrivileges, boolean, int, java.lang.String, java.lang.String):android.content.ComponentName");
    }

    private boolean shouldAllowBootCompletedStart(com.android.server.am.ServiceRecord r, int foregroundServiceType) {
        int fgsStartReasonCode = r.getFgsAllowStart();
        return (com.android.server.am.Flags.fgsBootCompleted() && android.app.compat.CompatChanges.isChangeEnabled(FGS_BOOT_COMPLETED_RESTRICTIONS, r.appInfo.uid) && fgsStartReasonCode == 200 && (this.mAm.mConstants.FGS_BOOT_COMPLETED_ALLOWLIST & foregroundServiceType) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.ComponentName startServiceInnerLocked(com.android.server.am.ServiceRecord r, android.content.Intent service, int callingUid, int callingPid, java.lang.String callingProcessName, int callingProcessState, boolean fgRequired, boolean callerFg, android.app.BackgroundStartPrivileges backgroundStartPrivileges, java.lang.String callingPackage) throws java.lang.Throwable {
        boolean z;
        boolean addToStarting;
        com.android.server.uri.NeededUriGrants neededGrants = this.mAm.mUgmInternal.checkGrantUriPermissionFromIntent(service, callingUid, r.packageName, r.userId);
        if (unscheduleServiceRestartLocked(r, callingUid, false) && com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, "START SERVICE WHILE RESTART PENDING: " + r);
        }
        boolean wasStartRequested = r.startRequested;
        r.lastActivity = android.os.SystemClock.uptimeMillis();
        r.startRequested = true;
        r.delayedStop = false;
        r.fgRequired = fgRequired;
        r.pendingStarts.add(new com.android.server.am.ServiceRecord.StartItem(r, false, r.makeNextStartId(), service, neededGrants, callingUid, callingProcessName, callingPackage, callingProcessState));
        boolean isFgs = r.isForeground || r.fgRequired;
        if (isFgs) {
            boolean whileInUseAllowsUiJobScheduling = com.android.server.am.ActivityManagerService.doesReasonCodeAllowSchedulingUserInitiatedJobs(r.getFgsAllowWiu_forStart(), callingUid);
            r.updateAllowUiJobScheduling(whileInUseAllowsUiJobScheduling || this.mAm.canScheduleUserInitiatedJobs(callingUid, callingPid, callingPackage));
        } else {
            r.updateAllowUiJobScheduling(false);
        }
        if (!fgRequired) {
            z = true;
        } else {
            synchronized (this.mAm.mProcessStats.mLock) {
                com.android.internal.app.procstats.ServiceState stracker = r.getTracker();
                if (stracker == null) {
                    z = true;
                } else {
                    z = true;
                    stracker.setForeground(true, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                }
            }
            this.mAm.mAppOpsService.startOperation(android.app.AppOpsManager.getToken(this.mAm.mAppOpsService), 76, r.appInfo.uid, r.packageName, null, true, false, null, false, 0, -1);
        }
        com.android.server.am.ActiveServices.ServiceMap smap = getServiceMapLocked(r.userId);
        boolean addToStarting2 = false;
        if (!callerFg && !fgRequired && r.app == null && this.mAm.mUserController.hasStartedUserState(r.userId)) {
            com.android.server.am.ProcessRecord proc = this.mAm.getProcessRecordLocked(r.processName, r.appInfo.uid);
            if (proc == null || proc.mState.getCurProcState() > 11) {
                if (DEBUG_DELAYED_SERVICE) {
                    android.util.Slog.v(TAG_SERVICE, "Potential start delay of " + r + " in " + proc);
                }
                if (!r.delayed) {
                    if (smap.mStartingBackground.size() >= this.mMaxStartingBackground) {
                        android.util.Slog.i(TAG_SERVICE, "Delaying start of: " + r);
                        smap.mDelayedStartList.add(r);
                        r.delayed = z;
                        return r.name;
                    }
                    if (DEBUG_DELAYED_STARTS) {
                        android.util.Slog.v(TAG_SERVICE, "Not delaying: " + r);
                    }
                    addToStarting2 = true;
                } else {
                    if (DEBUG_DELAYED_STARTS) {
                        android.util.Slog.v(TAG_SERVICE, "Continuing to delay: " + r);
                    }
                    return r.name;
                }
            } else if (proc.mState.getCurProcState() >= 10) {
                addToStarting2 = true;
                if (DEBUG_DELAYED_STARTS) {
                    android.util.Slog.v(TAG_SERVICE, "Not delaying, but counting as bg: " + r);
                }
            } else if (DEBUG_DELAYED_STARTS) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
                sb.append("Not potential delay (state=").append(proc.mState.getCurProcState()).append(' ').append(proc.mState.getAdjType());
                java.lang.String reason = proc.mState.makeAdjReason();
                if (reason != null) {
                    sb.append(' ');
                    sb.append(reason);
                }
                sb.append("): ");
                sb.append(r.toString());
                android.util.Slog.v(TAG_SERVICE, sb.toString());
            }
            addToStarting = addToStarting2;
        } else {
            if (DEBUG_DELAYED_STARTS) {
                if (callerFg || fgRequired) {
                    android.util.Slog.v(TAG_SERVICE, "Not potential delay (callerFg=" + callerFg + " uid=" + callingUid + " pid=" + callingPid + " fgRequired=" + fgRequired + "): " + r);
                } else if (r.app != null) {
                    android.util.Slog.v(TAG_SERVICE, "Not potential delay (cur app=" + r.app + "): " + r);
                } else {
                    android.util.Slog.v(TAG_SERVICE, "Not potential delay (user " + r.userId + " not started): " + r);
                }
            }
            addToStarting = false;
        }
        if (backgroundStartPrivileges.allowsAny()) {
            r.allowBgActivityStartsOnServiceStart(backgroundStartPrivileges);
        }
        android.content.ComponentName cmp = startServiceInnerLocked(smap, service, r, callerFg, addToStarting, callingUid, callingProcessName, callingProcessState, wasStartRequested, callingPackage);
        return cmp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requestStartTargetPermissionsReviewIfNeededLocked(final com.android.server.am.ServiceRecord r, java.lang.String callingPackage, java.lang.String callingFeatureId, int callingUid, final android.content.Intent service, final boolean callerFg, final int userId, boolean isBinding, final android.app.IServiceConnection connection) {
        if (!this.mAm.getPackageManagerInternal().isPermissionsReviewRequired(r.packageName, r.userId)) {
            return true;
        }
        if (!callerFg) {
            android.util.Slog.w("ActivityManager", "u" + r.userId + (isBinding ? " Binding" : " Starting") + " a service in package" + r.packageName + " requires a permissions review");
            return false;
        }
        final android.content.Intent intent = new android.content.Intent("android.intent.action.REVIEW_PERMISSIONS");
        intent.addFlags(411041792);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", r.packageName);
        if (isBinding) {
            android.os.RemoteCallback callback = new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.am.ActiveServices.2
                public void onResult(android.os.Bundle result) {
                    com.android.server.am.ActivityManagerService activityManagerService;
                    com.android.server.am.ActivityManagerService activityManagerService2 = com.android.server.am.ActiveServices.this.mAm;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService2) {
                        try {
                            long identity = com.android.server.am.ActiveServices.this.mAm.mInjector.clearCallingIdentity();
                            try {
                                if (!com.android.server.am.ActiveServices.this.mPendingServices.contains(r)) {
                                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                    return;
                                }
                                if (!com.android.server.am.ActiveServices.this.mAm.getPackageManagerInternal().isPermissionsReviewRequired(r.packageName, r.userId)) {
                                    try {
                                        try {
                                            com.android.server.am.ActiveServices.this.bringUpServiceLocked(r, service.getFlags(), callerFg, false, false, false, true, 0);
                                            activityManagerService = com.android.server.am.ActiveServices.this.mAm;
                                        } catch (java.lang.Throwable th) {
                                            com.android.server.am.ActiveServices.this.mAm.updateOomAdjPendingTargetsLocked(6);
                                            throw th;
                                        }
                                    } catch (android.os.RemoteException e) {
                                        activityManagerService = com.android.server.am.ActiveServices.this.mAm;
                                    }
                                    activityManagerService.updateOomAdjPendingTargetsLocked(6);
                                } else {
                                    com.android.server.am.ActiveServices.this.unbindServiceLocked(connection);
                                }
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            } finally {
                                com.android.server.am.ActiveServices.this.mAm.mInjector.restoreCallingIdentity(identity);
                            }
                        } catch (java.lang.Throwable th2) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th2;
                        }
                    }
                }
            });
            intent.putExtra("android.intent.extra.REMOTE_CALLBACK", (android.os.Parcelable) callback);
        } else {
            intent.putExtra("android.intent.extra.INTENT", new android.content.IntentSender(this.mAm.mPendingIntentController.getIntentSender(4, callingPackage, callingFeatureId, callingUid, userId, null, null, 0, new android.content.Intent[]{service}, new java.lang.String[]{service.resolveType(this.mAm.mContext.getContentResolver())}, 1409286144, null)));
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PERMISSIONS_REVIEW) {
            android.util.Slog.i("ActivityManager", "u" + r.userId + " Launching permission review for package " + r.packageName);
        }
        this.mAm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActiveServices.3
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.am.ActiveServices.this.mAm.mContext.startActivityAsUser(intent, new android.os.UserHandle(userId));
            }
        });
        return false;
    }

    private boolean deferServiceBringupIfFrozenLocked(final com.android.server.am.ServiceRecord s, final android.content.Intent serviceIntent, final java.lang.String callingPackage, final java.lang.String callingFeatureId, final int callingUid, final int callingPid, final java.lang.String callingProcessName, final int callingProcessState, final boolean fgRequired, final boolean callerFg, final int userId, final android.app.BackgroundStartPrivileges backgroundStartPrivileges, final boolean isBinding, final android.app.IServiceConnection connection) {
        java.util.ArrayList<java.lang.Runnable> curPendingBringups;
        android.content.pm.PackageManagerInternal pm = this.mAm.getPackageManagerInternal();
        boolean frozen = pm.isPackageFrozen(s.packageName, callingUid, s.userId);
        if (!frozen) {
            return false;
        }
        java.util.ArrayList<java.lang.Runnable> curPendingBringups2 = this.mPendingBringups.get(s);
        if (curPendingBringups2 != null) {
            curPendingBringups = curPendingBringups2;
        } else {
            java.util.ArrayList<java.lang.Runnable> curPendingBringups3 = new java.util.ArrayList<>();
            this.mPendingBringups.put(s, curPendingBringups3);
            curPendingBringups = curPendingBringups3;
        }
        curPendingBringups.add(new java.lang.Runnable() { // from class: com.android.server.am.ActiveServices.4
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.am.ActivityManagerService activityManagerService;
                com.android.server.am.ActivityManagerService activityManagerService2 = com.android.server.am.ActiveServices.this.mAm;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService2) {
                    try {
                        if (!com.android.server.am.ActiveServices.this.mPendingBringups.containsKey(s)) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        if (!com.android.server.am.ActiveServices.this.requestStartTargetPermissionsReviewIfNeededLocked(s, callingPackage, callingFeatureId, callingUid, serviceIntent, callerFg, userId, isBinding, connection)) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        if (isBinding) {
                            try {
                                com.android.server.am.ActiveServices.this.bringUpServiceLocked(s, serviceIntent.getFlags(), callerFg, false, false, false, true, 0);
                                activityManagerService = com.android.server.am.ActiveServices.this.mAm;
                            } catch (android.os.TransactionTooLargeException e) {
                                activityManagerService = com.android.server.am.ActiveServices.this.mAm;
                            } catch (java.lang.Throwable th) {
                                com.android.server.am.ActiveServices.this.mAm.updateOomAdjPendingTargetsLocked(6);
                                throw th;
                            }
                            activityManagerService.updateOomAdjPendingTargetsLocked(6);
                        } else {
                            try {
                                com.android.server.am.ActiveServices.this.startServiceInnerLocked(s, serviceIntent, callingUid, callingPid, callingProcessName, callingProcessState, fgRequired, callerFg, backgroundStartPrivileges, callingPackage);
                            } catch (android.os.TransactionTooLargeException e2) {
                            }
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    } catch (java.lang.Throwable th2) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th2;
                    }
                }
            }
        });
        return true;
    }

    void schedulePendingServiceStartLocked(java.lang.String packageName, int userId) {
        int totalPendings = this.mPendingBringups.size();
        int i = totalPendings - 1;
        while (i >= 0 && totalPendings > 0) {
            com.android.server.am.ServiceRecord r = this.mPendingBringups.keyAt(i);
            if (r.userId == userId && android.text.TextUtils.equals(r.packageName, packageName)) {
                java.util.ArrayList<java.lang.Runnable> curPendingBringups = this.mPendingBringups.valueAt(i);
                if (curPendingBringups != null) {
                    for (int j = curPendingBringups.size() - 1; j >= 0; j--) {
                        curPendingBringups.get(j).run();
                    }
                    curPendingBringups.clear();
                }
                int curTotalPendings = this.mPendingBringups.size();
                this.mPendingBringups.remove(r);
                if (totalPendings != curTotalPendings) {
                    totalPendings = this.mPendingBringups.size();
                    i = totalPendings - 1;
                } else {
                    totalPendings = this.mPendingBringups.size();
                    i--;
                }
            } else {
                i--;
            }
        }
    }

    android.content.ComponentName startServiceInnerLocked(com.android.server.am.ActiveServices.ServiceMap smap, android.content.Intent service, com.android.server.am.ServiceRecord r, boolean callerFg, boolean addToStarting, int callingUid, java.lang.String callingProcessName, int callingProcessState, boolean wasStartRequested, java.lang.String callingPackage) throws java.lang.Throwable {
        boolean zWasStopped;
        boolean z;
        int packageState;
        int i;
        synchronized (this.mAm.mProcessStats.mLock) {
            try {
                com.android.internal.app.procstats.ServiceState stracker = r.getTracker();
                if (stracker != null) {
                    stracker.setStarted(true, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                }
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        r.callStart = false;
        int uid = r.appInfo.uid;
        java.lang.String packageName = r.name.getPackageName();
        java.lang.String serviceName = r.name.getClassName();
        com.android.internal.util.FrameworkStatsLog.write(99, uid, packageName, serviceName, 1);
        this.mAm.mBatteryStatsService.noteServiceStartRunning(uid, packageName, serviceName);
        com.android.server.am.ProcessRecord hostApp = r.app;
        if (hostApp == null) {
            zWasStopped = wasStopped(r);
        } else {
            zWasStopped = false;
        }
        boolean wasStopped = zWasStopped;
        if (hostApp == null && !this.mAm.wasPackageEverLaunched(r.packageName, r.userId)) {
            z = true;
        } else {
            z = false;
        }
        boolean firstLaunch = z;
        r.mServiceRecordExt.setCallingPackageName(callingPackage);
        java.lang.String error = bringUpServiceLocked(r, service.getFlags(), callerFg, false, false, false, true, 0);
        r.mServiceRecordExt.setCallingPackageName(null);
        this.mAm.updateOomAdjPendingTargetsLocked(6);
        if (error != null) {
            return new android.content.ComponentName("!!", error);
        }
        if (wasStopped) {
            packageState = 2;
        } else {
            packageState = 1;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.d("ActivityManager", "Logging startService for " + packageName + ", stopped=" + wasStopped + ", firstLaunch=" + firstLaunch + ", intent=" + service + ", r.app=" + r.app);
        }
        java.lang.String action = service.getAction();
        if (r.app == null || r.app.getThread() == null) {
            i = 3;
        } else if (wasStartRequested || !r.getConnections().isEmpty()) {
            i = 2;
        } else {
            i = 1;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, uid, callingUid, action, 1, false, i, getShortProcessNameForStats(callingUid, callingProcessName), getShortServiceNameForStats(r), packageState, packageName, callingPackage, callingProcessState, r.mProcessStateOnRequest, firstLaunch, 0L);
        if (r.startRequested && addToStarting) {
            boolean first = smap.mStartingBackground.size() == 0;
            smap.mStartingBackground.add(r);
            r.startingBgTimeout = android.os.SystemClock.uptimeMillis() + this.mAm.mConstants.BG_START_TIMEOUT;
            if (DEBUG_DELAYED_SERVICE) {
                java.lang.RuntimeException here = new java.lang.RuntimeException("here");
                here.fillInStackTrace();
                android.util.Slog.v(TAG_SERVICE, "Starting background (first=" + first + "): " + r, here);
            } else if (DEBUG_DELAYED_STARTS) {
                android.util.Slog.v(TAG_SERVICE, "Starting background (first=" + first + "): " + r);
            }
            if (first) {
                smap.rescheduleDelayedStartsLocked();
            }
        } else if (callerFg || r.fgRequired) {
            smap.ensureNotStartingBackgroundLocked(r);
        }
        return r.name;
    }

    private java.lang.String getShortProcessNameForStats(int uid, java.lang.String processName) {
        java.lang.String[] packages = this.mAm.mContext.getPackageManager().getPackagesForUid(uid);
        if (packages != null && packages.length == 1) {
            if (android.text.TextUtils.equals(packages[0], processName)) {
                return null;
            }
            if (processName != null && processName.startsWith(packages[0])) {
                return processName.substring(packages[0].length());
            }
        }
        return processName;
    }

    private java.lang.String getShortServiceNameForStats(com.android.server.am.ServiceRecord r) {
        android.content.ComponentName cn = r.getComponentName();
        if (cn != null) {
            return cn.getShortClassName();
        }
        return null;
    }

    private void stopServiceLocked(com.android.server.am.ServiceRecord service, boolean enqueueOomAdj) {
        traceInstant("stopService(): ", service);
        try {
            android.os.Trace.traceBegin(64L, "stopServiceLocked()");
            if (service.delayed) {
                if (DEBUG_DELAYED_STARTS) {
                    android.util.Slog.v(TAG_SERVICE, "Delaying stop of pending: " + service);
                }
                service.delayedStop = true;
                return;
            }
            maybeStopShortFgsTimeoutLocked(service);
            maybeStopFgsTimeoutLocked(service);
            int uid = service.appInfo.uid;
            java.lang.String packageName = service.name.getPackageName();
            java.lang.String serviceName = service.name.getClassName();
            com.android.internal.util.FrameworkStatsLog.write(99, uid, packageName, serviceName, 2);
            this.mAm.mBatteryStatsService.noteServiceStopRunning(uid, packageName, serviceName);
            service.startRequested = false;
            if (service.tracker != null) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    service.tracker.setStarted(false, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                }
            }
            service.callStart = false;
            bringDownServiceIfNeededLocked(service, false, false, enqueueOomAdj, "stopService");
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    int stopServiceLocked(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, int userId, boolean isSdkSandboxService, int sdkSandboxClientAppUid, java.lang.String sdkSandboxClientAppPackage, java.lang.String instanceName) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, "stopService: " + service + " type=" + resolvedType);
        }
        com.android.server.am.ProcessRecord callerApp = this.mAm.getRecordForAppLOSP(caller);
        if (caller != null && callerApp == null) {
            throw new java.lang.SecurityException("Unable to find app for caller " + caller + " (pid=" + this.mAm.mInjector.getCallingPid() + ") when stopping service " + service);
        }
        com.android.server.am.ActiveServices.ServiceLookupResult r = retrieveServiceLocked(service, instanceName, isSdkSandboxService, sdkSandboxClientAppUid, sdkSandboxClientAppPackage, resolvedType, null, this.mAm.mInjector.getCallingPid(), this.mAm.mInjector.getCallingUid(), userId, false, false, false, false, null, false, false);
        if (r == null) {
            return 0;
        }
        if (r.record == null) {
            return -1;
        }
        long origId = this.mAm.mInjector.clearCallingIdentity();
        try {
            stopServiceLocked(r.record, false);
            this.mAm.mInjector.restoreCallingIdentity(origId);
            return 1;
        } catch (java.lang.Throwable th) {
            this.mAm.mInjector.restoreCallingIdentity(origId);
            throw th;
        }
    }

    void stopInBackgroundLocked(int uid) {
        com.android.server.am.ActiveServices.ServiceMap services = this.mServiceMap.get(android.os.UserHandle.getUserId(uid));
        java.util.ArrayList<com.android.server.am.ServiceRecord> stopping = null;
        if (services != null) {
            for (int i = services.mServicesByInstanceName.size() - 1; i >= 0; i--) {
                com.android.server.am.ServiceRecord service = services.mServicesByInstanceName.valueAt(i);
                if (service.packageName != null && service.packageName.equals("com.oplus.autotest.qetest")) {
                    android.util.Slog.d("ActivityManager", "stopInBackgroundLocked ignore package:" + service.packageName);
                } else if (!this.mActiveServicesExt.skipStopInBackgroundBegin(service, uid) && service.appInfo.uid == uid && service.startRequested && this.mAm.getAppStartModeLOSP(service.appInfo.uid, service.packageName, service.appInfo.targetSdkVersion, -1, false, false, false) != 0) {
                    if (stopping == null) {
                        stopping = new java.util.ArrayList<>();
                    }
                    java.lang.String compName = service.shortInstanceName;
                    com.android.server.am.EventLogTags.writeAmStopIdleService(service.appInfo.uid, compName);
                    java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
                    sb.append("Stopping service due to app idle: ");
                    android.os.UserHandle.formatUid(sb, service.appInfo.uid);
                    sb.append(" ");
                    android.util.TimeUtils.formatDuration(service.createRealTime - android.os.SystemClock.elapsedRealtime(), sb);
                    sb.append(" ");
                    sb.append(compName);
                    android.util.Slog.w("ActivityManager", sb.toString());
                    stopping.add(service);
                    if (appRestrictedAnyInBackground(service.appInfo.uid, service.packageName)) {
                        cancelForegroundNotificationLocked(service);
                    }
                }
            }
            if (stopping != null) {
                int size = stopping.size();
                for (int i2 = size - 1; i2 >= 0; i2--) {
                    com.android.server.am.ServiceRecord service2 = stopping.get(i2);
                    service2.delayed = false;
                    services.ensureNotStartingBackgroundLocked(service2);
                    stopServiceLocked(service2, true);
                }
                if (size > 0) {
                    this.mAm.updateOomAdjPendingTargetsLocked(18);
                }
            }
        }
    }

    void killMisbehavingService(com.android.server.am.ServiceRecord r, int appUid, int appPid, java.lang.String localPackageName, int exceptionTypeId) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (!r.destroying) {
                    stopServiceLocked(r, false);
                } else {
                    com.android.server.am.ActiveServices.ServiceMap smap = getServiceMapLocked(r.userId);
                    com.android.server.am.ServiceRecord found = smap.mServicesByInstanceName.remove(r.instanceName);
                    if (found != null) {
                        stopServiceLocked(found, false);
                    }
                }
                this.mAm.crashApplicationWithType(appUid, appPid, localPackageName, -1, "Bad notification for startForeground", true, exceptionTypeId);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    android.os.IBinder peekServiceLocked(android.content.Intent service, java.lang.String resolvedType, java.lang.String callingPackage) {
        com.android.server.am.ActiveServices.ServiceLookupResult r = retrieveServiceLocked(service, null, resolvedType, callingPackage, this.mAm.mInjector.getCallingPid(), this.mAm.mInjector.getCallingUid(), android.os.UserHandle.getCallingUserId(), false, false, false, false, false, false);
        if (r == null) {
            return null;
        }
        if (r.record == null) {
            throw new java.lang.SecurityException("Permission Denial: Accessing service from pid=" + this.mAm.mInjector.getCallingPid() + ", uid=" + this.mAm.mInjector.getCallingUid() + " requires " + r.permission);
        }
        com.android.server.am.IntentBindRecord ib = r.record.bindings.get(r.record.intent);
        if (ib == null) {
            return null;
        }
        android.os.IBinder ret = ib.binder;
        return ret;
    }

    boolean stopServiceTokenLocked(android.content.ComponentName className, android.os.IBinder token, int startId) throws java.lang.Throwable {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, "stopServiceToken: " + className + " " + token + " startId=" + startId);
        }
        com.android.server.am.ServiceRecord r = findServiceLocked(className, token, android.os.UserHandle.getCallingUserId());
        if (r == null) {
            return false;
        }
        if (startId >= 0) {
            com.android.server.am.ServiceRecord.StartItem si = r.findDeliveredStart(startId, false, false);
            if (si != null) {
                while (r.deliveredStarts.size() > 0) {
                    com.android.server.am.ServiceRecord.StartItem cur = r.deliveredStarts.remove(0);
                    cur.removeUriPermissionsLocked();
                    if (cur == si) {
                        break;
                    }
                }
            }
            if (r.getLastStartId() != startId) {
                return false;
            }
            if (r.deliveredStarts.size() > 0) {
                android.util.Slog.w("ActivityManager", "stopServiceToken startId " + startId + " is last, but have " + r.deliveredStarts.size() + " remaining args");
            }
        }
        maybeStopShortFgsTimeoutLocked(r);
        maybeStopFgsTimeoutLocked(r);
        int uid = r.appInfo.uid;
        java.lang.String packageName = r.name.getPackageName();
        java.lang.String serviceName = r.name.getClassName();
        com.android.internal.util.FrameworkStatsLog.write(99, uid, packageName, serviceName, 2);
        this.mAm.mBatteryStatsService.noteServiceStopRunning(uid, packageName, serviceName);
        r.startRequested = false;
        if (r.tracker != null) {
            synchronized (this.mAm.mProcessStats.mLock) {
                r.tracker.setStarted(false, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
            }
        }
        r.callStart = false;
        long origId = this.mAm.mInjector.clearCallingIdentity();
        bringDownServiceIfNeededLocked(r, false, false, false, "stopServiceToken");
        this.mAm.mInjector.restoreCallingIdentity(origId);
        return true;
    }

    public void setServiceForegroundLocked(android.content.ComponentName className, android.os.IBinder token, int id, android.app.Notification notification, int flags, int foregroundServiceType) {
        int userId = android.os.UserHandle.getCallingUserId();
        int callingUid = this.mAm.mInjector.getCallingUid();
        long origId = this.mAm.mInjector.clearCallingIdentity();
        try {
            com.android.server.am.ServiceRecord r = findServiceLocked(className, token, userId);
            if (r != null) {
                setServiceForegroundInnerLocked(r, id, notification, flags, foregroundServiceType, callingUid);
            }
        } finally {
            this.mAm.mInjector.restoreCallingIdentity(origId);
        }
    }

    public int getForegroundServiceTypeLocked(android.content.ComponentName className, android.os.IBinder token) {
        int userId = android.os.UserHandle.getCallingUserId();
        long origId = this.mAm.mInjector.clearCallingIdentity();
        int ret = 0;
        try {
            com.android.server.am.ServiceRecord r = findServiceLocked(className, token, userId);
            if (r != null) {
                ret = r.foregroundServiceType;
            }
            return ret;
        } finally {
            this.mAm.mInjector.restoreCallingIdentity(origId);
        }
    }

    boolean foregroundAppShownEnoughLocked(com.android.server.am.ActiveServices.ActiveForegroundApp aa, long nowElapsed) {
        long j;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            android.util.Slog.d("ActivityManager", "Shown enough: pkg=" + aa.mPackageName + ", uid=" + aa.mUid);
        }
        aa.mHideTime = Long.MAX_VALUE;
        if (aa.mShownWhileTop) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.d("ActivityManager", "YES - shown while on top");
                return true;
            }
            return true;
        }
        if (this.mScreenOn || aa.mShownWhileScreenOn) {
            long minTime = aa.mStartVisibleTime;
            if (aa.mStartTime != aa.mStartVisibleTime) {
                j = this.mAm.mConstants.FGSERVICE_SCREEN_ON_AFTER_TIME;
            } else {
                j = this.mAm.mConstants.FGSERVICE_MIN_SHOWN_TIME;
            }
            long minTime2 = minTime + j;
            if (nowElapsed >= minTime2) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d("ActivityManager", "YES - shown long enough with screen on");
                }
                return true;
            }
            long reportTime = this.mAm.mConstants.FGSERVICE_MIN_REPORT_TIME + nowElapsed;
            aa.mHideTime = reportTime > minTime2 ? reportTime : minTime2;
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.d("ActivityManager", "NO -- wait " + (aa.mHideTime - nowElapsed) + " with screen on");
                return false;
            }
            return false;
        }
        long minTime3 = aa.mEndTime + this.mAm.mConstants.FGSERVICE_SCREEN_ON_BEFORE_TIME;
        if (nowElapsed >= minTime3) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.d("ActivityManager", "YES - gone long enough with screen off");
            }
            return true;
        }
        aa.mHideTime = minTime3;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            android.util.Slog.d("ActivityManager", "NO -- wait " + (aa.mHideTime - nowElapsed) + " with screen off");
            return false;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0072 A[Catch: all -> 0x013e, TryCatch #0 {all -> 0x013e, blocks: (B:4:0x0006, B:6:0x0011, B:8:0x0015, B:9:0x002f, B:11:0x003e, B:13:0x004e, B:15:0x0054, B:30:0x00de, B:16:0x0065, B:18:0x006b, B:19:0x006e, B:21:0x0072, B:23:0x007c, B:25:0x0080, B:26:0x00a7, B:28:0x00ab, B:29:0x00d7, B:31:0x00e2, B:33:0x00eb, B:34:0x00f9, B:36:0x0106, B:38:0x010a, B:39:0x0124, B:40:0x0136, B:41:0x0139), top: B:48:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00de A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void updateForegroundApps(com.android.server.am.ActiveServices.ServiceMap r13) {
        /*
            Method dump skipped, instruction units count: 324
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.updateForegroundApps(com.android.server.am.ActiveServices$ServiceMap):void");
    }

    private void requestUpdateActiveForegroundAppsLocked(com.android.server.am.ActiveServices.ServiceMap smap, long timeElapsed) {
        android.os.Message msg = smap.obtainMessage(2);
        if (timeElapsed != 0) {
            smap.sendMessageAtTime(msg, (android.os.SystemClock.uptimeMillis() + timeElapsed) - android.os.SystemClock.elapsedRealtime());
        } else {
            smap.mActiveForegroundAppsChanged = true;
            smap.sendMessage(msg);
        }
    }

    private void decActiveForegroundAppLocked(com.android.server.am.ActiveServices.ServiceMap smap, com.android.server.am.ServiceRecord r) {
        com.android.server.am.ActiveServices.ActiveForegroundApp active = smap.mActiveForegroundApps.get(r.packageName);
        if (active != null) {
            active.mNumActive--;
            if (active.mNumActive <= 0) {
                active.mEndTime = android.os.SystemClock.elapsedRealtime();
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d("ActivityManager", "Ended running of service");
                }
                if (foregroundAppShownEnoughLocked(active, active.mEndTime)) {
                    smap.mActiveForegroundApps.remove(r.packageName);
                    smap.mActiveForegroundAppsChanged = true;
                    requestUpdateActiveForegroundAppsLocked(smap, 0L);
                } else if (active.mHideTime < Long.MAX_VALUE) {
                    requestUpdateActiveForegroundAppsLocked(smap, active.mHideTime);
                }
            }
        }
    }

    void updateScreenStateLocked(boolean screenOn) {
        if (this.mScreenOn != screenOn) {
            this.mScreenOn = screenOn;
            if (screenOn) {
                long nowElapsed = android.os.SystemClock.elapsedRealtime();
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d("ActivityManager", "Screen turned on");
                }
                for (int i = this.mServiceMap.size() - 1; i >= 0; i--) {
                    com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.valueAt(i);
                    long nextUpdateTime = Long.MAX_VALUE;
                    boolean changed = false;
                    for (int j = smap.mActiveForegroundApps.size() - 1; j >= 0; j--) {
                        com.android.server.am.ActiveServices.ActiveForegroundApp active = smap.mActiveForegroundApps.valueAt(j);
                        if (active.mEndTime == 0) {
                            if (!active.mShownWhileScreenOn) {
                                active.mShownWhileScreenOn = true;
                                active.mStartVisibleTime = nowElapsed;
                            }
                        } else {
                            if (!active.mShownWhileScreenOn && active.mStartVisibleTime == active.mStartTime) {
                                active.mStartVisibleTime = nowElapsed;
                                active.mEndTime = nowElapsed;
                            }
                            if (foregroundAppShownEnoughLocked(active, nowElapsed)) {
                                smap.mActiveForegroundApps.remove(active.mPackageName);
                                smap.mActiveForegroundAppsChanged = true;
                                changed = true;
                            } else if (active.mHideTime < nextUpdateTime) {
                                nextUpdateTime = active.mHideTime;
                            }
                        }
                    }
                    if (changed) {
                        requestUpdateActiveForegroundAppsLocked(smap, 0L);
                    } else if (nextUpdateTime < Long.MAX_VALUE) {
                        requestUpdateActiveForegroundAppsLocked(smap, nextUpdateTime);
                    }
                }
            }
        }
    }

    void foregroundServiceProcStateChangedLocked(com.android.server.am.UidRecord uidRec) {
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(android.os.UserHandle.getUserId(uidRec.getUid()));
        if (smap != null) {
            boolean changed = false;
            for (int j = smap.mActiveForegroundApps.size() - 1; j >= 0; j--) {
                com.android.server.am.ActiveServices.ActiveForegroundApp active = smap.mActiveForegroundApps.valueAt(j);
                if (active.mUid == uidRec.getUid()) {
                    if (uidRec.getCurProcState() <= 2) {
                        if (!active.mAppOnTop) {
                            active.mAppOnTop = true;
                            changed = true;
                        }
                        active.mShownWhileTop = true;
                    } else if (active.mAppOnTop) {
                        active.mAppOnTop = false;
                        changed = true;
                    }
                }
            }
            if (changed) {
                requestUpdateActiveForegroundAppsLocked(smap, 0L);
            }
        }
    }

    private boolean isForegroundServiceAllowedInBackgroundRestricted(com.android.server.am.ProcessRecord app) {
        com.android.server.am.ProcessStateRecord state = app.mState;
        if (!isDeviceProvisioningPackage(app.info.packageName) && state.isBackgroundRestricted() && state.getSetProcState() > 3) {
            return state.getSetProcState() == 4 && state.isSetBoundByNonBgRestrictedApp();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isForegroundServiceAllowedInBackgroundRestricted(int uid, java.lang.String packageName) {
        com.android.server.am.ProcessRecord app;
        com.android.server.am.UidRecord uidRec = this.mAm.mProcessList.getUidRecordLOSP(uid);
        return (uidRec == null || (app = uidRec.getProcessInPackage(packageName)) == null || !isForegroundServiceAllowedInBackgroundRestricted(app)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTempAllowedByAlarmClock(int uid) {
        com.android.server.am.ActivityManagerService.FgsTempAllowListItem item = this.mAm.isAllowlistedForFgsStartLOSP(uid);
        return item != null && item.mReasonCode == 301;
    }

    void logFgsApiBeginLocked(int uid, int pid, int apiType) {
        synchronized (this.mFGSLogger) {
            this.mFGSLogger.logForegroundServiceApiEventBegin(uid, pid, apiType, "");
        }
    }

    void logFgsApiEndLocked(int uid, int pid, int apiType) {
        synchronized (this.mFGSLogger) {
            this.mFGSLogger.logForegroundServiceApiEventEnd(uid, pid, apiType);
        }
    }

    void logFgsApiStateChangedLocked(int uid, int pid, int apiType, int state) {
        synchronized (this.mFGSLogger) {
            this.mFGSLogger.logForegroundServiceApiStateChanged(uid, pid, apiType, state);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x031a  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0581  */
    /* JADX WARN: Removed duplicated region for block: B:459:0x054c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void setServiceForegroundInnerLocked(com.android.server.am.ServiceRecord r54, int r55, android.app.Notification r56, int r57, int r58, int r59) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2600
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.setServiceForegroundInnerLocked(com.android.server.am.ServiceRecord, int, android.app.Notification, int, int, int):void");
    }

    private boolean withinFgsDeferRateLimit(com.android.server.am.ServiceRecord sr, long now) {
        if (now < sr.fgDisplayTime) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.d(TAG_SERVICE, "FGS transition for " + sr + " within deferral period, no rate limit applied");
            }
            return false;
        }
        int uid = sr.appInfo.uid;
        long eligible = this.mFgsDeferralEligible.get(uid, 0L);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE && now < eligible) {
            android.util.Slog.d(TAG_SERVICE, "FGS transition for uid " + uid + " within rate limit, showing immediately");
        }
        return now < eligible;
    }

    private android.util.Pair<java.lang.Integer, java.lang.RuntimeException> validateForegroundServiceType(com.android.server.am.ServiceRecord r, int type, int defaultToType, int startType) {
        android.app.ForegroundServiceTypePolicy policy = android.app.ForegroundServiceTypePolicy.getDefaultPolicy();
        android.app.ForegroundServiceTypePolicy.ForegroundServiceTypePolicyInfo policyInfo = policy.getForegroundServiceTypePolicyInfo(type, defaultToType);
        int code = policy.checkForegroundServiceTypePolicy(this.mAm.mContext, r.packageName, r.app.uid, r.app.getPid(), r.isFgsAllowedWiu_forStart(), policyInfo);
        java.lang.RuntimeException exception = null;
        java.lang.String str = " and the app must be in the eligible state/exemptions to access the foreground only permission";
        switch (code) {
            case 2:
                java.lang.String msg = "Starting FGS with type " + android.content.pm.ServiceInfo.foregroundServiceTypeToLabel(type) + " code=" + code + " callerApp=" + r.app + " targetSDK=" + r.app.info.targetSdkVersion;
                android.util.Slog.wtfQuiet("ActivityManager", msg);
                android.util.Slog.w("ActivityManager", msg);
                break;
            case 3:
                exception = (startType == -1 && type == 0) ? new android.app.MissingForegroundServiceTypeException("Starting FGS without a type  callerApp=" + r.app + " targetSDK=" + r.app.info.targetSdkVersion) : new android.app.InvalidForegroundServiceTypeException("Starting FGS with type " + android.content.pm.ServiceInfo.foregroundServiceTypeToLabel(type) + " callerApp=" + r.app + " targetSDK=" + r.app.info.targetSdkVersion + " has been prohibited");
                break;
            case 4:
                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Starting FGS with type ").append(android.content.pm.ServiceInfo.foregroundServiceTypeToLabel(type)).append(" code=").append(code).append(" callerApp=").append(r.app).append(" targetSDK=").append(r.app.info.targetSdkVersion).append(" requiredPermissions=").append(policyInfo.toPermissionString());
                if (!policyInfo.hasForegroundOnlyPermission()) {
                    str = "";
                }
                java.lang.String msg2 = sbAppend.append(str).toString();
                android.util.Slog.wtfQuiet("ActivityManager", msg2);
                android.util.Slog.w("ActivityManager", msg2);
                break;
            case 5:
                java.lang.StringBuilder sbAppend2 = new java.lang.StringBuilder().append("Starting FGS with type ").append(android.content.pm.ServiceInfo.foregroundServiceTypeToLabel(type)).append(" callerApp=").append(r.app).append(" targetSDK=").append(r.app.info.targetSdkVersion).append(" requires permissions: ").append(policyInfo.toPermissionString());
                if (!policyInfo.hasForegroundOnlyPermission()) {
                    str = "";
                }
                exception = new java.lang.SecurityException(sbAppend2.append(str).toString());
                break;
        }
        return android.util.Pair.create(java.lang.Integer.valueOf(code), exception);
    }

    private class SystemExemptedFgsTypePermission extends android.app.ForegroundServiceTypePolicy.ForegroundServiceTypePermission {
        SystemExemptedFgsTypePermission() {
            super("System exempted");
        }

        public int checkPermission(android.content.Context context, int callerUid, int callerPid, java.lang.String packageName, boolean allowWhileInUse) {
            com.android.server.am.AppRestrictionController appRestrictionController = com.android.server.am.ActiveServices.this.mAm.mAppRestrictionController;
            int reason = appRestrictionController.getPotentialSystemExemptionReason(callerUid);
            if (reason == -1 && (reason = appRestrictionController.getPotentialSystemExemptionReason(callerUid, packageName)) == -1) {
                reason = appRestrictionController.getPotentialUserAllowedExemptionReason(callerUid, packageName);
            }
            if (reason == -1 && com.android.internal.util.ArrayUtils.contains(com.android.server.am.ActiveServices.this.mAm.getPackageManagerInternal().getKnownPackageNames(2, 0), packageName)) {
                reason = com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_PACKAGE_INSTALLER;
            }
            switch (reason) {
                case 10:
                case 11:
                case 51:
                case 55:
                case 56:
                case 63:
                case 65:
                case 300:
                case 319:
                case 320:
                case 321:
                case 322:
                case 323:
                case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ACTIVE_DEVICE_ADMIN /* 324 */:
                case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_PACKAGE_INSTALLER /* 326 */:
                case 327:
                    return 0;
                default:
                    return -1;
            }
        }
    }

    private void initSystemExemptedFgsTypePermission() {
        android.app.ForegroundServiceTypePolicy policy = android.app.ForegroundServiceTypePolicy.getDefaultPolicy();
        android.app.ForegroundServiceTypePolicy.ForegroundServiceTypePolicyInfo policyInfo = policy.getForegroundServiceTypePolicyInfo(1024, 0);
        if (policyInfo != null) {
            policyInfo.setCustomPermission(new com.android.server.am.ActiveServices.SystemExemptedFgsTypePermission());
        }
    }

    private class MediaProjectionFgsTypeCustomPermission extends android.app.ForegroundServiceTypePolicy.ForegroundServiceTypePermission {
        MediaProjectionFgsTypeCustomPermission() {
            super("Media projection screen capture permission");
        }

        public int checkPermission(android.content.Context context, int callerUid, int callerPid, java.lang.String packageName, boolean allowWhileInUse) {
            return com.android.server.am.ActiveServices.this.mAm.isAllowedMediaProjectionNoOpCheck(callerUid) ? 0 : -1;
        }
    }

    private void initMediaProjectFgsTypeCustomPermission() {
        android.app.ForegroundServiceTypePolicy policy = android.app.ForegroundServiceTypePolicy.getDefaultPolicy();
        android.app.ForegroundServiceTypePolicy.ForegroundServiceTypePolicyInfo policyInfo = policy.getForegroundServiceTypePolicyInfo(32, 0);
        if (policyInfo != null) {
            policyInfo.setCustomPermission(new com.android.server.am.ActiveServices.MediaProjectionFgsTypeCustomPermission());
        }
    }

    android.app.ActivityManagerInternal.ServiceNotificationPolicy applyForegroundServiceNotificationLocked(android.app.Notification notification, java.lang.String tag, int id, java.lang.String pkg, int userId) {
        if (tag != null) {
            return android.app.ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            android.util.Slog.d(TAG_SERVICE, "Evaluating FGS policy for id=" + id + " pkg=" + pkg + " not=" + notification);
        }
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(userId);
        if (smap == null) {
            return android.app.ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE;
        }
        for (int i = 0; i < smap.mServicesByInstanceName.size(); i++) {
            com.android.server.am.ServiceRecord sr = smap.mServicesByInstanceName.valueAt(i);
            if (sr.isForeground && id == sr.foregroundId && pkg.equals(sr.appInfo.packageName)) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d(TAG_SERVICE, "   FOUND: notification is for " + sr);
                }
                notification.flags |= 64;
                sr.foregroundNoti = notification;
                boolean showNow = shouldShowFgsNotificationLocked(sr);
                if (showNow) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        android.util.Slog.d(TAG_SERVICE, "   Showing immediately due to policy");
                    }
                    sr.mFgsNotificationDeferred = false;
                    return android.app.ActivityManagerInternal.ServiceNotificationPolicy.SHOW_IMMEDIATELY;
                }
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d(TAG_SERVICE, "   Deferring / update-only");
                }
                startFgsDeferralTimerLocked(sr);
                return android.app.ActivityManagerInternal.ServiceNotificationPolicy.UPDATE_ONLY;
            }
        }
        return android.app.ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE;
    }

    private boolean shouldShowFgsNotificationLocked(com.android.server.am.ServiceRecord r) {
        long now = android.os.SystemClock.uptimeMillis();
        if (!this.mAm.mConstants.mFlagFgsNotificationDeferralEnabled) {
            return true;
        }
        if (r.mFgsNotificationDeferred && now >= r.fgDisplayTime) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                android.util.Slog.d("ActivityManager", "FGS reached end of deferral period: " + r);
            }
            return true;
        }
        if (withinFgsDeferRateLimit(r, now)) {
            return true;
        }
        if (this.mAm.mConstants.mFlagFgsNotificationDeferralApiGated) {
            boolean isLegacyApp = r.appInfo.targetSdkVersion < 31;
            if (isLegacyApp) {
                return true;
            }
        }
        boolean isLegacyApp2 = r.mFgsNotificationShown;
        if (isLegacyApp2) {
            return true;
        }
        if (!r.foregroundNoti.isForegroundDisplayForceDeferred()) {
            if (r.foregroundNoti.shouldShowForegroundImmediately()) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d(TAG_SERVICE, "FGS " + r + " notification policy says show immediately");
                }
                return true;
            }
            if ((r.foregroundServiceType & 54) != 0) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d(TAG_SERVICE, "FGS " + r + " type gets immediate display");
                }
                return true;
            }
        } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            android.util.Slog.d(TAG_SERVICE, "FGS " + r + " notification is app deferred");
        }
        return false;
    }

    private void startFgsDeferralTimerLocked(com.android.server.am.ServiceRecord r) {
        long now = android.os.SystemClock.uptimeMillis();
        int uid = r.appInfo.uid;
        long when = (r.isShortFgs() ? this.mAm.mConstants.mFgsNotificationDeferralIntervalForShort : this.mAm.mConstants.mFgsNotificationDeferralInterval) + now;
        for (int i = 0; i < this.mPendingFgsNotifications.size(); i++) {
            com.android.server.am.ServiceRecord pending = this.mPendingFgsNotifications.get(i);
            if (pending == r) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d(TAG_SERVICE, "FGS " + r + " already pending notification display");
                    return;
                }
                return;
            }
            if (uid == pending.appInfo.uid) {
                when = java.lang.Math.min(when, pending.fgDisplayTime);
            }
        }
        if (this.mFgsDeferralRateLimited) {
            long nextEligible = (r.isShortFgs() ? this.mAm.mConstants.mFgsNotificationDeferralExclusionTimeForShort : this.mAm.mConstants.mFgsNotificationDeferralExclusionTime) + when;
            this.mFgsDeferralEligible.put(uid, nextEligible);
        }
        r.fgDisplayTime = when;
        boolean isLegacyApp = true;
        r.mFgsNotificationDeferred = true;
        r.mFgsNotificationWasDeferred = true;
        r.mFgsNotificationShown = false;
        this.mPendingFgsNotifications.add(r);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            android.util.Slog.d(TAG_SERVICE, "FGS " + r + " notification in " + (when - now) + " ms");
        }
        if (r.appInfo.targetSdkVersion >= 31) {
            isLegacyApp = false;
        }
        if (isLegacyApp) {
            android.util.Slog.i(TAG_SERVICE, "Deferring FGS notification in legacy app " + r.appInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.UserHandle.formatUid(r.appInfo.uid) + " : " + r.foregroundNoti);
        }
        this.mAm.mHandler.postAtTime(this.mPostDeferredFGSNotifications, when);
    }

    boolean enableFgsNotificationRateLimitLocked(boolean enable) {
        if (enable != this.mFgsDeferralRateLimited) {
            this.mFgsDeferralRateLimited = enable;
            if (!enable) {
                this.mFgsDeferralEligible.clear();
            }
        }
        return enable;
    }

    private void removeServiceNotificationDeferralsLocked(java.lang.String packageName, int userId) {
        for (int i = this.mPendingFgsNotifications.size() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord r = this.mPendingFgsNotifications.get(i);
            if (userId == r.userId && r.appInfo.packageName.equals(packageName)) {
                this.mPendingFgsNotifications.remove(i);
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d(TAG_SERVICE, "Removing notification deferral for " + r);
                }
            }
        }
    }

    public void onForegroundServiceNotificationUpdateLocked(boolean shown, android.app.Notification notification, int id, java.lang.String pkg, int userId) {
        for (int i = this.mPendingFgsNotifications.size() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord sr = this.mPendingFgsNotifications.get(i);
            if (userId == sr.userId && id == sr.foregroundId && sr.appInfo.packageName.equals(pkg)) {
                if (shown) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        android.util.Slog.d(TAG_SERVICE, "Notification shown; canceling deferral of " + sr);
                    }
                    sr.mFgsNotificationShown = true;
                    sr.mFgsNotificationDeferred = false;
                    this.mPendingFgsNotifications.remove(i);
                } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    android.util.Slog.d(TAG_SERVICE, "FGS notification deferred for " + sr);
                }
            }
        }
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(userId);
        if (smap != null) {
            for (int i2 = 0; i2 < smap.mServicesByInstanceName.size(); i2++) {
                com.android.server.am.ServiceRecord sr2 = smap.mServicesByInstanceName.valueAt(i2);
                if (sr2.isForeground && id == sr2.foregroundId && sr2.appInfo.packageName.equals(pkg)) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        android.util.Slog.d(TAG_SERVICE, "Recording shown notification for " + sr2);
                    }
                    sr2.foregroundNoti = notification;
                }
            }
        }
    }

    private void registerAppOpCallbackLocked(com.android.server.am.ServiceRecord r) {
        if (r.app == null) {
            return;
        }
        int uid = r.appInfo.uid;
        com.android.server.am.ActiveServices.AppOpCallback callback = this.mFgsAppOpCallbacks.get(uid);
        if (callback == null) {
            callback = new com.android.server.am.ActiveServices.AppOpCallback(r.app, this.mAm.getAppOpsManager());
            this.mFgsAppOpCallbacks.put(uid, callback);
        }
        callback.registerLocked();
    }

    private void unregisterAppOpCallbackLocked(com.android.server.am.ServiceRecord r) {
        int uid = r.appInfo.uid;
        com.android.server.am.ActiveServices.AppOpCallback callback = this.mFgsAppOpCallbacks.get(uid);
        if (callback != null) {
            callback.unregisterLocked();
            if (callback.isObsoleteLocked()) {
                this.mFgsAppOpCallbacks.remove(uid);
            }
        }
    }

    private static final class AppOpCallback {
        private static final int[] LOGGED_AP_OPS = {0, 1, 27, 26};
        private final android.app.AppOpsManager mAppOpsManager;
        private final com.android.server.am.ProcessRecord mProcessRecord;
        private final android.util.SparseIntArray mAcceptedOps = new android.util.SparseIntArray();
        private final android.util.SparseIntArray mRejectedOps = new android.util.SparseIntArray();
        private final java.lang.Object mCounterLock = new java.lang.Object();
        private final android.util.SparseIntArray mAppOpModes = new android.util.SparseIntArray();
        private int mNumFgs = 0;
        private boolean mDestroyed = false;
        private final android.app.AppOpsManager.OnOpNotedInternalListener mOpNotedCallback = new android.app.AppOpsManager.OnOpNotedInternalListener() { // from class: com.android.server.am.ActiveServices.AppOpCallback.1
            public void onOpNoted(int op, int uid, java.lang.String pkgName, java.lang.String attributionTag, int flags, int result) {
                com.android.server.am.ActiveServices.AppOpCallback.this.incrementOpCountIfNeeded(op, uid, result);
            }
        };
        private final android.app.AppOpsManager.OnOpStartedListener mOpStartedCallback = new android.app.AppOpsManager.OnOpStartedListener() { // from class: com.android.server.am.ActiveServices.AppOpCallback.2
            public void onOpStarted(int op, int uid, java.lang.String pkgName, java.lang.String attributionTag, int flags, int result) {
                com.android.server.am.ActiveServices.AppOpCallback.this.incrementOpCountIfNeeded(op, uid, result);
            }
        };

        AppOpCallback(com.android.server.am.ProcessRecord r, android.app.AppOpsManager appOpsManager) {
            this.mProcessRecord = r;
            this.mAppOpsManager = appOpsManager;
            for (int op : LOGGED_AP_OPS) {
                int mode = appOpsManager.unsafeCheckOpRawNoThrow(op, r.uid, r.info.packageName);
                this.mAppOpModes.put(op, mode);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void incrementOpCountIfNeeded(int op, int uid, int result) {
            if (uid == this.mProcessRecord.uid && isNotTop()) {
                incrementOpCount(op, result == 0);
            }
        }

        private boolean isNotTop() {
            return this.mProcessRecord.mState.getCurProcState() != 2;
        }

        private void incrementOpCount(int op, boolean allowed) {
            synchronized (this.mCounterLock) {
                android.util.SparseIntArray counter = allowed ? this.mAcceptedOps : this.mRejectedOps;
                int index = counter.indexOfKey(op);
                if (index >= 0) {
                    counter.setValueAt(index, counter.valueAt(index) + 1);
                } else {
                    counter.put(op, 1);
                }
            }
        }

        void registerLocked() {
            if (isObsoleteLocked()) {
                android.util.Slog.wtf("ActivityManager", "Trying to register on a stale AppOpCallback.");
                return;
            }
            this.mNumFgs++;
            if (this.mNumFgs == 1) {
                this.mAppOpsManager.startWatchingNoted(LOGGED_AP_OPS, this.mOpNotedCallback);
                this.mAppOpsManager.startWatchingStarted(LOGGED_AP_OPS, this.mOpStartedCallback);
            }
        }

        void unregisterLocked() {
            this.mNumFgs--;
            if (this.mNumFgs <= 0) {
                this.mDestroyed = true;
                logFinalValues();
                this.mAppOpsManager.stopWatchingNoted(this.mOpNotedCallback);
                this.mAppOpsManager.stopWatchingStarted(this.mOpStartedCallback);
            }
        }

        boolean isObsoleteLocked() {
            return this.mDestroyed;
        }

        private void logFinalValues() {
            synchronized (this.mCounterLock) {
                for (int op : LOGGED_AP_OPS) {
                    int acceptances = this.mAcceptedOps.get(op);
                    int rejections = this.mRejectedOps.get(op);
                    if (acceptances > 0 || rejections > 0) {
                        com.android.internal.util.FrameworkStatsLog.write(256, this.mProcessRecord.uid, op, modeToEnum(this.mAppOpModes.get(op)), acceptances, rejections);
                    }
                }
            }
        }

        private static int modeToEnum(int mode) {
            switch (mode) {
                case 0:
                    return 1;
                case 1:
                    return 2;
                case 2:
                case 3:
                default:
                    return 0;
                case 4:
                    return 3;
            }
        }
    }

    private void cancelForegroundNotificationLocked(com.android.server.am.ServiceRecord r) {
        if (r.foregroundNoti != null) {
            com.android.server.am.ActiveServices.ServiceMap sm = getServiceMapLocked(r.userId);
            if (sm != null) {
                for (int i = sm.mServicesByInstanceName.size() - 1; i >= 0; i--) {
                    com.android.server.am.ServiceRecord other = sm.mServicesByInstanceName.valueAt(i);
                    if (other != r && other.isForeground && other.foregroundId == r.foregroundId && other.packageName.equals(r.packageName)) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                            android.util.Slog.i(TAG_SERVICE, "FGS notification for " + r + " shared by " + other + " (isForeground=" + other.isForeground + ") - NOT cancelling");
                            return;
                        }
                        return;
                    }
                }
            }
            r.cancelNotification();
        }
    }

    private void updateServiceForegroundLocked(com.android.server.am.ProcessServiceRecord psr, boolean oomAdj) {
        boolean anyForeground = false;
        int fgServiceTypes = 0;
        boolean hasTypeNone = false;
        for (int i = psr.numberOfRunningServices() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord sr = psr.getRunningServiceAt(i);
            if (sr.isForeground || sr.fgRequired) {
                anyForeground = true;
                fgServiceTypes |= sr.foregroundServiceType;
                if (sr.foregroundServiceType == 0) {
                    hasTypeNone = true;
                }
            }
        }
        this.mAm.updateProcessForegroundLocked(psr.mApp, anyForeground, fgServiceTypes, hasTypeNone, oomAdj);
        psr.setHasReportedForegroundServices(anyForeground);
    }

    void unscheduleShortFgsTimeoutLocked(com.android.server.am.ServiceRecord sr) {
        this.mShortFGSAnrTimer.cancel(sr);
        this.mAm.mHandler.removeMessages(77, sr);
        this.mAm.mHandler.removeMessages(76, sr);
    }

    private void maybeUpdateShortFgsTrackingLocked(com.android.server.am.ServiceRecord sr, boolean extendTimeout) {
        if (!sr.isShortFgs()) {
            sr.clearShortFgsInfo();
            unscheduleShortFgsTimeoutLocked(sr);
            return;
        }
        boolean isAlreadyShortFgs = sr.hasShortFgsInfo();
        if (extendTimeout || !isAlreadyShortFgs) {
            if (DEBUG_SHORT_SERVICE) {
                if (isAlreadyShortFgs) {
                    android.util.Slog.i(TAG_SERVICE, "Extending SHORT_SERVICE time out: " + sr);
                } else {
                    android.util.Slog.i(TAG_SERVICE, "Short FGS started: " + sr);
                }
            }
            traceInstant("short FGS start/extend: ", sr);
            sr.setShortFgsInfo(android.os.SystemClock.uptimeMillis());
            unscheduleShortFgsTimeoutLocked(sr);
            android.os.Message msg = this.mAm.mHandler.obtainMessage(76, sr);
            this.mAm.mHandler.sendMessageAtTime(msg, sr.getShortFgsInfo().getTimeoutTime());
            return;
        }
        if (DEBUG_SHORT_SERVICE) {
            android.util.Slog.w(TAG_SERVICE, "NOT extending SHORT_SERVICE time out: " + sr);
        }
        sr.getShortFgsInfo().update();
    }

    private void maybeStopShortFgsTimeoutLocked(com.android.server.am.ServiceRecord sr) {
        sr.clearShortFgsInfo();
        if (!sr.isShortFgs()) {
            return;
        }
        if (DEBUG_SHORT_SERVICE) {
            android.util.Slog.i(TAG_SERVICE, "Stop short FGS timeout: " + sr);
        }
        unscheduleShortFgsTimeoutLocked(sr);
    }

    void onShortFgsTimeout(com.android.server.am.ServiceRecord sr) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                long nowUptime = android.os.SystemClock.uptimeMillis();
                if (!sr.shouldTriggerShortFgsTimeout(nowUptime)) {
                    if (DEBUG_SHORT_SERVICE) {
                        android.util.Slog.d(TAG_SERVICE, "[STALE] Short FGS timed out: " + sr + " " + sr.getShortFgsTimedEventDescription(nowUptime));
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                android.util.Slog.e(TAG_SERVICE, "Short FGS timed out: " + sr);
                traceInstant("short FGS timeout: ", sr);
                logFGSStateChangeLocked(sr, 5, nowUptime > sr.mFgsEnterTime ? (int) (nowUptime - sr.mFgsEnterTime) : 0, 0, 0, 0, false);
                try {
                    sr.app.getThread().scheduleTimeoutService(sr, sr.getShortFgsInfo().getStartId());
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG_SERVICE, "Exception from scheduleTimeoutService: " + e.toString());
                }
                android.os.Message msg = this.mAm.mHandler.obtainMessage(77, sr);
                this.mAm.mHandler.sendMessageAtTime(msg, sr.getShortFgsInfo().getProcStateDemoteTime());
                this.mShortFGSAnrTimer.start(sr, sr.getShortFgsInfo().getAnrTime() - android.os.SystemClock.uptimeMillis());
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    boolean shouldServiceTimeOutLocked(android.content.ComponentName className, android.os.IBinder token) {
        int userId = android.os.UserHandle.getCallingUserId();
        long ident = this.mAm.mInjector.clearCallingIdentity();
        try {
            com.android.server.am.ServiceRecord sr = findServiceLocked(className, token, userId);
            if (sr != null) {
                long nowUptime = android.os.SystemClock.uptimeMillis();
                return sr.shouldTriggerShortFgsTimeout(nowUptime);
            }
            this.mAm.mInjector.restoreCallingIdentity(ident);
            return false;
        } finally {
            this.mAm.mInjector.restoreCallingIdentity(ident);
        }
    }

    void onShortFgsProcstateTimeout(com.android.server.am.ServiceRecord sr) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                long nowUptime = android.os.SystemClock.uptimeMillis();
                if (!sr.shouldDemoteShortFgsProcState(nowUptime)) {
                    if (DEBUG_SHORT_SERVICE) {
                        android.util.Slog.d(TAG_SERVICE, "[STALE] Short FGS procstate demotion: " + sr + " " + sr.getShortFgsTimedEventDescription(nowUptime));
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                } else {
                    android.util.Slog.e(TAG_SERVICE, "Short FGS procstate demoted: " + sr);
                    traceInstant("short FGS demote: ", sr);
                    this.mAm.updateOomAdjLocked(sr.app, 13);
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void onShortFgsAnrTimeout(com.android.server.am.ServiceRecord sr) {
        java.lang.String reason = "A foreground service of FOREGROUND_SERVICE_TYPE_SHORT_SERVICE did not stop within a timeout: " + sr.getComponentName();
        com.android.internal.os.TimeoutRecord tr = com.android.internal.os.TimeoutRecord.forShortFgsTimeout(reason);
        tr.mLatencyTracker.waitingOnAMSLockStarted();
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                tr.mLatencyTracker.waitingOnAMSLockEnded();
                long nowUptime = android.os.SystemClock.uptimeMillis();
                if (!sr.shouldTriggerShortFgsAnr(nowUptime)) {
                    if (DEBUG_SHORT_SERVICE) {
                        android.util.Slog.d(TAG_SERVICE, "[STALE] Short FGS ANR'ed: " + sr + " " + sr.getShortFgsTimedEventDescription(nowUptime));
                    }
                    this.mShortFGSAnrTimer.discard(sr);
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mShortFGSAnrTimer.accept(sr);
                java.lang.String message = "Short FGS ANR'ed: " + sr;
                if (DEBUG_SHORT_SERVICE) {
                    android.util.Slog.wtf(TAG_SERVICE, message);
                } else {
                    android.util.Slog.e(TAG_SERVICE, message);
                }
                traceInstant("short FGS ANR: ", sr);
                this.mAm.appNotResponding(sr.app, tr);
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    int getTimeLimitedFgsType(int foregroundServiceType) {
        int fgsType = 0;
        long timeout = 0;
        if ((foregroundServiceType & 8192) == 8192) {
            fgsType = 8192;
            timeout = this.mAm.mConstants.mMediaProcessingFgsTimeoutDuration;
        }
        if ((foregroundServiceType & 1) != 1) {
            return fgsType;
        }
        if (timeout == 0 || this.mAm.mConstants.mDataSyncFgsTimeoutDuration > timeout) {
            long timeout2 = this.mAm.mConstants.mDataSyncFgsTimeoutDuration;
            return 1;
        }
        return fgsType;
    }

    private long getTimeLimitForFgsType(int foregroundServiceType) {
        switch (foregroundServiceType) {
            case 1:
                return this.mAm.mConstants.mDataSyncFgsTimeoutDuration;
            case 8192:
                return this.mAm.mConstants.mMediaProcessingFgsTimeoutDuration;
            default:
                return Long.MAX_VALUE;
        }
    }

    private long getNextFgsStopTime(int fgsType, com.android.server.am.ServiceRecord.TimeLimitedFgsInfo fgsInfo) {
        long timeLimit = getTimeLimitForFgsType(fgsType);
        if (timeLimit == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        return fgsInfo.getLastFgsStartTime() + java.lang.Math.max(0L, timeLimit - fgsInfo.getTotalRuntime());
    }

    private com.android.server.am.ServiceRecord.TimeLimitedFgsInfo getFgsTimeLimitedInfo(int uid, int fgsType) {
        android.util.SparseArray<com.android.server.am.ServiceRecord.TimeLimitedFgsInfo> fgsInfo = this.mTimeLimitedFgsInfo.get(uid);
        if (fgsInfo != null) {
            return fgsInfo.get(fgsType);
        }
        return null;
    }

    private void maybeUpdateFgsTrackingLocked(com.android.server.am.ServiceRecord sr, int previousFgsType) {
        int previouslyTimeLimitedType = getTimeLimitedFgsType(previousFgsType);
        if (previouslyTimeLimitedType == 0 && !sr.isFgsTimeLimited()) {
            return;
        }
        if (previouslyTimeLimitedType != 0) {
            com.android.server.am.ServiceRecord.TimeLimitedFgsInfo fgsTypeInfo = getFgsTimeLimitedInfo(sr.appInfo.uid, previouslyTimeLimitedType);
            if (fgsTypeInfo != null) {
                fgsTypeInfo.updateTotalRuntime(android.os.SystemClock.uptimeMillis());
                fgsTypeInfo.decNumParallelServices();
            }
            if (!sr.isFgsTimeLimited()) {
                this.mAm.mHandler.removeMessages(84, sr);
                this.mAm.mHandler.removeMessages(85, sr);
                return;
            }
        }
        traceInstant("FGS start: ", sr);
        long nowUptime = android.os.SystemClock.uptimeMillis();
        android.util.SparseArray<com.android.server.am.ServiceRecord.TimeLimitedFgsInfo> fgsInfo = this.mTimeLimitedFgsInfo.get(sr.appInfo.uid);
        if (fgsInfo == null) {
            fgsInfo = new android.util.SparseArray<>();
            this.mTimeLimitedFgsInfo.put(sr.appInfo.uid, fgsInfo);
        }
        int timeLimitedFgsType = getTimeLimitedFgsType(sr.foregroundServiceType);
        com.android.server.am.ServiceRecord.TimeLimitedFgsInfo fgsTypeInfo2 = fgsInfo.get(timeLimitedFgsType);
        if (fgsTypeInfo2 == null) {
            fgsTypeInfo2 = sr.createTimeLimitedFgsInfo();
            fgsInfo.put(timeLimitedFgsType, fgsTypeInfo2);
        }
        fgsTypeInfo2.noteFgsFgsStart(nowUptime);
        this.mAm.mHandler.removeMessages(84, sr);
        this.mAm.mHandler.removeMessages(85, sr);
        android.os.Message msg = this.mAm.mHandler.obtainMessage(84, sr);
        long timeoutCallbackTime = getNextFgsStopTime(timeLimitedFgsType, fgsTypeInfo2);
        if (timeoutCallbackTime == Long.MAX_VALUE) {
            android.util.Slog.wtf("ActivityManager", "Couldn't calculate timeout for time-limited fgs: " + sr);
        } else {
            this.mAm.mHandler.sendMessageAtTime(msg, timeoutCallbackTime);
        }
    }

    private void maybeStopFgsTimeoutLocked(com.android.server.am.ServiceRecord sr) {
        int timeLimitedType = getTimeLimitedFgsType(sr.foregroundServiceType);
        if (timeLimitedType == 0) {
            return;
        }
        com.android.server.am.ServiceRecord.TimeLimitedFgsInfo fgsTypeInfo = getFgsTimeLimitedInfo(sr.appInfo.uid, timeLimitedType);
        if (fgsTypeInfo != null) {
            fgsTypeInfo.updateTotalRuntime(android.os.SystemClock.uptimeMillis());
            fgsTypeInfo.decNumParallelServices();
        }
        android.util.Slog.d(TAG_SERVICE, "Stop FGS timeout: " + sr);
        this.mAm.mHandler.removeMessages(84, sr);
        this.mAm.mHandler.removeMessages(85, sr);
    }

    void onUidRemovedLocked(int uid) {
        this.mTimeLimitedFgsInfo.delete(uid);
    }

    boolean hasServiceTimedOutLocked(android.content.ComponentName className, android.os.IBinder token) {
        int userId = android.os.UserHandle.getCallingUserId();
        long ident = this.mAm.mInjector.clearCallingIdentity();
        try {
            com.android.server.am.ServiceRecord sr = findServiceLocked(className, token, userId);
            if (sr == null) {
                return false;
            }
            return getTimeLimitedFgsType(sr.foregroundServiceType) != 0;
        } finally {
            this.mAm.mInjector.restoreCallingIdentity(ident);
        }
    }

    void onFgsTimeout(com.android.server.am.ServiceRecord sr) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                int fgsType = getTimeLimitedFgsType(sr.foregroundServiceType);
                if (fgsType != 0 && sr.app != null) {
                    boolean currentlyTop = sr.app.mState.getCurProcState() <= 2;
                    long nowUptime = android.os.SystemClock.uptimeMillis();
                    long lastTopTime = currentlyTop ? nowUptime : sr.app.mState.getLastTopTime();
                    long constantTimeLimit = getTimeLimitForFgsType(fgsType);
                    if (lastTopTime != Long.MIN_VALUE && constantTimeLimit > nowUptime - lastTopTime) {
                        this.mAm.mHandler.removeMessages(84, sr);
                        this.mAm.mHandler.removeMessages(85, sr);
                        android.os.Message msg = this.mAm.mHandler.obtainMessage(84, sr);
                        this.mAm.mHandler.sendMessageAtTime(msg, lastTopTime + constantTimeLimit);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    android.util.Slog.e(TAG_SERVICE, "FGS (" + android.content.pm.ServiceInfo.foregroundServiceTypeToLabel(fgsType) + ") timed out: " + sr);
                    traceInstant("FGS timed out: ", sr);
                    com.android.server.am.ServiceRecord.TimeLimitedFgsInfo fgsTypeInfo = getFgsTimeLimitedInfo(sr.appInfo.uid, fgsType);
                    if (fgsTypeInfo != null) {
                        fgsTypeInfo.updateTotalRuntime(nowUptime);
                        fgsTypeInfo.setTimeLimitExceededAt(nowUptime);
                        logFGSStateChangeLocked(sr, 5, nowUptime > fgsTypeInfo.getFirstFgsStartUptime() ? (int) (nowUptime - fgsTypeInfo.getFirstFgsStartUptime()) : 0, 0, 0, 0, false);
                    }
                    try {
                        sr.app.getThread().scheduleTimeoutServiceForType(sr, sr.getLastStartId(), fgsType);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG_SERVICE, "Exception from scheduleTimeoutServiceForType: " + e);
                    }
                    android.os.Message msg2 = this.mAm.mHandler.obtainMessage(85, sr);
                    this.mAm.mHandler.sendMessageDelayed(msg2, this.mAm.mConstants.mFgsCrashExtraWaitDuration);
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mAm.mHandler.removeMessages(85, sr);
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void onFgsCrashTimeout(com.android.server.am.ServiceRecord sr) {
        int fgsType = getTimeLimitedFgsType(sr.foregroundServiceType);
        if (fgsType == 0) {
            return;
        }
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ServiceRecord.TimeLimitedFgsInfo fgsTypeInfo = getFgsTimeLimitedInfo(sr.appInfo.uid, fgsType);
                if (fgsTypeInfo != null) {
                    fgsTypeInfo.decNumParallelServices();
                }
                java.lang.String reason = "A foreground service of type " + android.content.pm.ServiceInfo.foregroundServiceTypeToLabel(fgsType) + " did not stop within its timeout: " + sr.getComponentName();
                if (android.app.Flags.enableFgsTimeoutCrashBehavior()) {
                    android.util.Slog.e(TAG_SERVICE, "FGS Crashed: " + sr);
                    traceInstant("FGS Crash: ", sr);
                    if (sr.app != null) {
                        this.mAm.crashApplicationWithTypeWithExtras(sr.app.uid, sr.app.getPid(), sr.app.info.packageName, sr.app.userId, reason, false, 7, android.app.RemoteServiceException.ForegroundServiceDidNotStopInTimeException.createExtrasForService(sr.getComponentName()));
                    }
                } else {
                    android.util.Slog.wtf("ActivityManager", reason);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void updateAllowlistManagerLocked(com.android.server.am.ProcessServiceRecord psr) {
        psr.mAllowlistManager = false;
        for (int i = psr.numberOfRunningServices() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord sr = psr.getRunningServiceAt(i);
            if (sr.allowlistManager) {
                psr.mAllowlistManager = true;
                return;
            }
        }
    }

    private void stopServiceAndUpdateAllowlistManagerLocked(com.android.server.am.ServiceRecord service) {
        maybeStopShortFgsTimeoutLocked(service);
        com.android.server.am.ProcessServiceRecord psr = service.app.mServices;
        psr.stopService(service);
        psr.updateBoundClientUids();
        if (service.allowlistManager) {
            updateAllowlistManagerLocked(psr);
        }
    }

    void updateServiceConnectionActivitiesLocked(com.android.server.am.ProcessServiceRecord clientPsr) {
        android.util.ArraySet<com.android.server.am.ProcessRecord> updatedProcesses = null;
        for (int i = 0; i < clientPsr.numberOfConnections(); i++) {
            com.android.server.am.ConnectionRecord conn = clientPsr.getConnectionAt(i);
            com.android.server.am.ProcessRecord proc = conn.binding.service.app;
            if (proc != null && proc != clientPsr.mApp) {
                if (updatedProcesses == null) {
                    updatedProcesses = new android.util.ArraySet<>();
                } else if (updatedProcesses.contains(proc)) {
                }
                updatedProcesses.add(proc);
                updateServiceClientActivitiesLocked(proc.mServices, null, false);
            }
        }
    }

    private boolean updateServiceClientActivitiesLocked(com.android.server.am.ProcessServiceRecord psr, com.android.server.am.ConnectionRecord modCr, boolean updateLru) {
        if (modCr != null && modCr.binding.client != null && !modCr.binding.client.hasActivities()) {
            return false;
        }
        boolean anyClientActivities = false;
        for (int i = psr.numberOfRunningServices() - 1; i >= 0 && !anyClientActivities; i--) {
            com.android.server.am.ServiceRecord sr = psr.getRunningServiceAt(i);
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections = sr.getConnections();
            for (int conni = connections.size() - 1; conni >= 0 && !anyClientActivities; conni--) {
                java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = connections.valueAt(conni);
                int cri = clist.size() - 1;
                while (true) {
                    if (cri >= 0) {
                        com.android.server.am.ConnectionRecord cr = clist.get(cri);
                        if (cr.binding.client != null && cr.binding.client != psr.mApp && cr.binding.client.hasActivities()) {
                            anyClientActivities = true;
                            break;
                        }
                        cri--;
                    }
                }
            }
        }
        if (anyClientActivities == psr.hasClientActivities()) {
            return false;
        }
        psr.setHasClientActivities(anyClientActivities);
        if (updateLru) {
            this.mAm.updateLruProcessLocked(psr.mApp, anyClientActivities, null);
        }
        return true;
    }

    int bindServiceLocked(android.app.IApplicationThread caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, long flags, java.lang.String instanceName, boolean isSdkSandboxService, int sdkSandboxClientAppUid, java.lang.String sdkSandboxClientAppPackage, android.app.IApplicationThread sdkSandboxClientApplicationThread, java.lang.String callingPackage, int userId) throws android.os.TransactionTooLargeException {
        return bindServiceLocked(caller, token, service, resolvedType, connection, flags, instanceName, isSdkSandboxService, sdkSandboxClientAppUid, sdkSandboxClientAppPackage, sdkSandboxClientApplicationThread, callingPackage, userId, -1L);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(57:489|116|117|(1:119)(1:120)|(1:127)(1:126)|128|(56:130|(1:132)|135|527|136|(3:540|138|(1:140))|143|(2:145|(3:147|405|154)(1:158))(1:159)|160|(1:162)|163|(1:165)(1:166)|167|168|501|169|170|(6:519|172|173|495|174|(2:176|177)(1:178))(1:183)|(9:503|185|186|547|187|188|(6:485|190|194|521|195|196)(4:194|521|195|196)|467|468)(1:203)|204|481|205|206|543|207|(3:209|511|210)|214|215|(2:217|218)|219|220|(2:222|223)|224|225|(2:227|228)|229|230|(1:238)|239|(1:241)|(1:248)(3:(1:244)(1:245)|246|247)|249|(1:251)(1:252)|253|(1:255)(1:256)|257|(1:259)(1:260)|261|(1:269)(1:268)|270|(1:272)|(1:274)(1:275)|276|(8:278|(1:280)(1:281)|282|283|(9:285|513|286|(1:288)(1:289)|290|291|493|292|(6:294|295|533|296|297|298)(3:301|507|302))(1:308)|531|309|(17:311|(1:313)(1:316)|317|318|319|509|320|(7:487|322|(3:324|535|325)(1:329)|330|(1:332)|333|(5:335|(14:339|523|340|(1:345)|346|(0)(1:350)|(1:356)|360|(1:362)(1:363)|499|365|(1:367)(1:369)|370|(7:372|(1:380)(1:(1:379)(1:378))|381|382|483|383|(8:515|385|389|390|541|391|538|(3:497|393|(10:395|(2:505|397)(1:401)|402|551|403|404|549|405|416|(7:424|438|439|525|440|441|442)(10:420|421|422|537|438|439|525|440|441|442))(10:430|529|431|(2:433|537)|438|439|525|440|441|442))(0))(6:389|390|541|391|538|(0)(0)))(0))(1:338)|344|346|(1:348))(0))(1:353)|354|(0)|360|(0)(0)|499|365|(0)(0)|370|(0)(0))(0))(0)|467|468)(1:133)|134|135|527|136|(0)|143|(0)(0)|160|(0)|163|(0)(0)|167|168|501|169|170|(0)(0)|(0)(0)|204|481|205|206|543|207|(0)|214|215|(0)|219|220|(0)|224|225|(0)|229|230|(4:232|234|236|238)|239|(0)|(0)(0)|249|(0)(0)|253|(0)(0)|257|(0)(0)|261|(6:269|270|(0)|(0)(0)|276|(0)(0))(0)|467|468) */
    /* JADX WARN: Can't wrap try/catch for region: R(60:517|114|115|489|116|117|(1:119)(1:120)|(1:127)(1:126)|128|(56:130|(1:132)|135|527|136|(3:540|138|(1:140))|143|(2:145|(3:147|405|154)(1:158))(1:159)|160|(1:162)|163|(1:165)(1:166)|167|168|501|169|170|(6:519|172|173|495|174|(2:176|177)(1:178))(1:183)|(9:503|185|186|547|187|188|(6:485|190|194|521|195|196)(4:194|521|195|196)|467|468)(1:203)|204|481|205|206|543|207|(3:209|511|210)|214|215|(2:217|218)|219|220|(2:222|223)|224|225|(2:227|228)|229|230|(1:238)|239|(1:241)|(1:248)(3:(1:244)(1:245)|246|247)|249|(1:251)(1:252)|253|(1:255)(1:256)|257|(1:259)(1:260)|261|(1:269)(1:268)|270|(1:272)|(1:274)(1:275)|276|(8:278|(1:280)(1:281)|282|283|(9:285|513|286|(1:288)(1:289)|290|291|493|292|(6:294|295|533|296|297|298)(3:301|507|302))(1:308)|531|309|(17:311|(1:313)(1:316)|317|318|319|509|320|(7:487|322|(3:324|535|325)(1:329)|330|(1:332)|333|(5:335|(14:339|523|340|(1:345)|346|(0)(1:350)|(1:356)|360|(1:362)(1:363)|499|365|(1:367)(1:369)|370|(7:372|(1:380)(1:(1:379)(1:378))|381|382|483|383|(8:515|385|389|390|541|391|538|(3:497|393|(10:395|(2:505|397)(1:401)|402|551|403|404|549|405|416|(7:424|438|439|525|440|441|442)(10:420|421|422|537|438|439|525|440|441|442))(10:430|529|431|(2:433|537)|438|439|525|440|441|442))(0))(6:389|390|541|391|538|(0)(0)))(0))(1:338)|344|346|(1:348))(0))(1:353)|354|(0)|360|(0)(0)|499|365|(0)(0)|370|(0)(0))(0))(0)|467|468)(1:133)|134|135|527|136|(0)|143|(0)(0)|160|(0)|163|(0)(0)|167|168|501|169|170|(0)(0)|(0)(0)|204|481|205|206|543|207|(0)|214|215|(0)|219|220|(0)|224|225|(0)|229|230|(4:232|234|236|238)|239|(0)|(0)(0)|249|(0)(0)|253|(0)(0)|257|(0)(0)|261|(6:269|270|(0)|(0)(0)|276|(0)(0))(0)|467|468) */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0b1c, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0b21, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0b26, code lost:
    
        r4 = r59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b35, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0b36, code lost:
    
        r4 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0b4b, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x0b4c, code lost:
    
        r4 = r9;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:145:0x03f3 A[Catch: all -> 0x03d4, TryCatch #32 {all -> 0x03d4, blocks: (B:138:0x03b7, B:140:0x03bb, B:143:0x03eb, B:145:0x03f3, B:147:0x03ff, B:148:0x0405, B:160:0x0426, B:162:0x042f, B:157:0x0422, B:149:0x0406, B:151:0x040c, B:153:0x041e), top: B:540:0x03b7, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0425  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x042f A[Catch: all -> 0x03d4, TRY_LEAVE, TryCatch #32 {all -> 0x03d4, blocks: (B:138:0x03b7, B:140:0x03bb, B:143:0x03eb, B:145:0x03f3, B:147:0x03ff, B:148:0x0405, B:160:0x0426, B:162:0x042f, B:157:0x0422, B:149:0x0406, B:151:0x040c, B:153:0x041e), top: B:540:0x03b7, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0446  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0448  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x05d5  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x05f8  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0619  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0624  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0631  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x065b A[Catch: all -> 0x05fd, TRY_ENTER, TryCatch #18 {all -> 0x05fd, blocks: (B:210:0x05f9, B:218:0x061a, B:223:0x0625, B:228:0x0632, B:232:0x0639, B:234:0x063f, B:236:0x0649, B:238:0x0651, B:241:0x065b, B:244:0x0667, B:246:0x0674, B:259:0x06a3, B:264:0x06b1, B:266:0x06b5, B:272:0x06c3, B:274:0x06cd, B:278:0x06d6), top: B:511:0x05f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0665  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x067a  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x068b  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x068d  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0692  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0694 A[Catch: all -> 0x0b1c, TRY_LEAVE, TryCatch #35 {all -> 0x0b1c, blocks: (B:207:0x05e8, B:215:0x0613, B:220:0x061e, B:225:0x062b, B:229:0x0634, B:239:0x0657, B:249:0x067c, B:283:0x06e8, B:256:0x0694), top: B:543:0x05e8 }] */
    /* JADX WARN: Removed duplicated region for block: B:259:0x06a3 A[Catch: all -> 0x05fd, TRY_ENTER, TryCatch #18 {all -> 0x05fd, blocks: (B:210:0x05f9, B:218:0x061a, B:223:0x0625, B:228:0x0632, B:232:0x0639, B:234:0x063f, B:236:0x0649, B:238:0x0651, B:241:0x065b, B:244:0x0667, B:246:0x0674, B:259:0x06a3, B:264:0x06b1, B:266:0x06b5, B:272:0x06c3, B:274:0x06cd, B:278:0x06d6), top: B:511:0x05f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:260:0x06aa  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x06bd  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x06c3 A[Catch: all -> 0x05fd, TryCatch #18 {all -> 0x05fd, blocks: (B:210:0x05f9, B:218:0x061a, B:223:0x0625, B:228:0x0632, B:232:0x0639, B:234:0x063f, B:236:0x0649, B:238:0x0651, B:241:0x065b, B:244:0x0667, B:246:0x0674, B:259:0x06a3, B:264:0x06b1, B:266:0x06b5, B:272:0x06c3, B:274:0x06cd, B:278:0x06d6), top: B:511:0x05f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:274:0x06cd A[Catch: all -> 0x05fd, TryCatch #18 {all -> 0x05fd, blocks: (B:210:0x05f9, B:218:0x061a, B:223:0x0625, B:228:0x0632, B:232:0x0639, B:234:0x063f, B:236:0x0649, B:238:0x0651, B:241:0x065b, B:244:0x0667, B:246:0x0674, B:259:0x06a3, B:264:0x06b1, B:266:0x06b5, B:272:0x06c3, B:274:0x06cd, B:278:0x06d6), top: B:511:0x05f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x06d2  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x06d6 A[Catch: all -> 0x05fd, TRY_LEAVE, TryCatch #18 {all -> 0x05fd, blocks: (B:210:0x05f9, B:218:0x061a, B:223:0x0625, B:228:0x0632, B:232:0x0639, B:234:0x063f, B:236:0x0649, B:238:0x0651, B:241:0x065b, B:244:0x0667, B:246:0x0674, B:259:0x06a3, B:264:0x06b1, B:266:0x06b5, B:272:0x06c3, B:274:0x06cd, B:278:0x06d6), top: B:511:0x05f9 }] */
    /* JADX WARN: Removed duplicated region for block: B:281:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x07cd  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x0839  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x0881 A[Catch: all -> 0x0888, TRY_LEAVE, TryCatch #24 {all -> 0x0888, blocks: (B:346:0x084d, B:348:0x0858, B:350:0x085c, B:356:0x0881, B:367:0x08a3, B:372:0x08e6, B:340:0x083b, B:342:0x0844), top: B:523:0x083b }] */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0899  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x089c  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x08a3 A[Catch: all -> 0x0888, TRY_ENTER, TRY_LEAVE, TryCatch #24 {all -> 0x0888, blocks: (B:346:0x084d, B:348:0x0858, B:350:0x085c, B:356:0x0881, B:367:0x08a3, B:372:0x08e6, B:340:0x083b, B:342:0x0844), top: B:523:0x083b }] */
    /* JADX WARN: Removed duplicated region for block: B:369:0x08d4  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x08e6 A[Catch: all -> 0x0888, TRY_ENTER, TRY_LEAVE, TryCatch #24 {all -> 0x0888, blocks: (B:346:0x084d, B:348:0x0858, B:350:0x085c, B:356:0x0881, B:367:0x08a3, B:372:0x08e6, B:340:0x083b, B:342:0x0844), top: B:523:0x083b }] */
    /* JADX WARN: Removed duplicated region for block: B:380:0x08fa  */
    /* JADX WARN: Removed duplicated region for block: B:430:0x0a7a  */
    /* JADX WARN: Removed duplicated region for block: B:497:0x09ad A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:503:0x0535 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:519:0x04a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:540:0x03b7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v123, types: [com.android.server.wm.ActivityTaskManagerInternal] */
    /* JADX WARN: Type inference failed for: r35v1 */
    /* JADX WARN: Type inference failed for: r35v2, types: [int] */
    /* JADX WARN: Type inference failed for: r35v3 */
    /* JADX WARN: Type inference failed for: r35v4 */
    /* JADX WARN: Type inference failed for: r38v0 */
    /* JADX WARN: Type inference failed for: r38v1, types: [int] */
    /* JADX WARN: Type inference failed for: r38v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int bindServiceLocked(android.app.IApplicationThread r70, android.os.IBinder r71, android.content.Intent r72, java.lang.String r73, android.app.IServiceConnection r74, long r75, java.lang.String r77, boolean r78, int r79, java.lang.String r80, android.app.IApplicationThread r81, java.lang.String r82, int r83, long r84) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 3026
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.bindServiceLocked(android.app.IApplicationThread, android.os.IBinder, android.content.Intent, java.lang.String, android.app.IServiceConnection, long, java.lang.String, boolean, int, java.lang.String, android.app.IApplicationThread, java.lang.String, int, long):int");
    }

    private void notifyBindingServiceEventLocked(com.android.server.am.ProcessRecord callerApp, java.lang.String callingPackage) {
        android.content.pm.ApplicationInfo ai = callerApp.info;
        java.lang.String callerPackage = ai != null ? ai.packageName : callingPackage;
        if (callerPackage != null) {
            this.mAm.mHandler.obtainMessage(75, callerApp.uid, 0, callerPackage).sendToTarget();
        }
    }

    private void maybeLogBindCrossProfileService(int userId, java.lang.String callingPackage, int callingUid) {
        int callingUserId;
        if (android.os.UserHandle.isCore(callingUid) || (callingUserId = android.os.UserHandle.getUserId(callingUid)) == userId || !this.mAm.mUserController.isSameProfileGroup(callingUserId, userId)) {
            return;
        }
        android.app.admin.DevicePolicyEventLogger.createEvent(151).setStrings(new java.lang.String[]{callingPackage}).write();
    }

    void publishServiceLocked(com.android.server.am.ServiceRecord r, android.content.Intent intent, android.os.IBinder service) throws java.lang.Throwable {
        publishServiceLocked(r, intent, service, -1L);
    }

    void publishServiceLocked(com.android.server.am.ServiceRecord r, android.content.Intent intent, android.os.IBinder service, long beginTime) throws java.lang.Throwable {
        long origId;
        long origId2;
        com.android.server.am.IntentBindRecord b;
        int i;
        boolean tooManyConn;
        boolean tooManyClist;
        android.content.Intent.FilterComparison filter;
        boolean tooManyClist2;
        com.android.server.am.IntentBindRecord b2;
        java.lang.String str;
        boolean tooManyConn2;
        com.android.server.am.IntentBindRecord b3;
        long origId3 = this.mAm.mInjector.clearCallingIdentity();
        try {
            java.lang.String str2 = ": ";
            java.lang.String str3 = " ";
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                try {
                    android.util.Slog.v(TAG_SERVICE, "PUBLISHING " + r + " " + intent + ": " + service);
                } catch (java.lang.Throwable th) {
                    th = th;
                    origId = origId3;
                    this.mAm.mInjector.restoreCallingIdentity(origId);
                    throw th;
                }
            }
            if (r != null) {
                try {
                    android.content.Intent.FilterComparison filter2 = new android.content.Intent.FilterComparison(intent);
                    com.android.server.am.IntentBindRecord b4 = r.bindings.get(filter2);
                    if (b4 == null || b4.received) {
                        origId2 = origId3;
                        b = b4;
                        i = 20;
                    } else {
                        b4.binder = service;
                        b4.requested = true;
                        b4.received = true;
                        android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections = r.getConnections();
                        if (connections.size() > 1000) {
                            android.util.Slog.v("ActivityManager", "too many connections: PUBLISHING " + r + " " + intent + ": " + service);
                            tooManyConn = true;
                        } else {
                            tooManyConn = false;
                        }
                        int conni = connections.size() - 1;
                        while (conni >= 0) {
                            java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = connections.valueAt(conni);
                            origId2 = origId3;
                            if (clist.size() > 1000) {
                                try {
                                    android.util.Slog.v("ActivityManager", "too many clist: PUBLISHING " + r + str3 + intent + str2 + service);
                                    tooManyClist = true;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    origId = origId2;
                                    this.mAm.mInjector.restoreCallingIdentity(origId);
                                    throw th;
                                }
                            } else {
                                tooManyClist = false;
                            }
                            int i2 = 0;
                            while (i2 < clist.size()) {
                                java.util.ArrayList<com.android.server.am.ConnectionRecord> clist2 = clist;
                                com.android.server.am.ConnectionRecord c = clist2.get(i2);
                                java.lang.String str4 = str2;
                                if (filter2.equals(c.binding.intent.intent)) {
                                    filter = filter2;
                                    com.android.server.am.IntentBindRecord b5 = b4;
                                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                        android.util.Slog.v(TAG_SERVICE, "Publishing to: " + c);
                                    }
                                    android.content.ComponentName clientSideComponentName = c.aliasComponent != null ? c.aliasComponent : r.name;
                                    if (tooManyConn && conni > connections.size() - 20 && conni < connections.size() - 1) {
                                        android.util.Slog.v("ActivityManager", "Publishing to: " + conni + str3 + c);
                                    }
                                    if (tooManyClist && i2 > clist2.size() - 20) {
                                        if (i2 < clist2.size() - 1) {
                                            android.util.Slog.v("ActivityManager", "Publishing to: " + clist2 + str3 + c);
                                        }
                                    }
                                    try {
                                        c.conn.connected(clientSideComponentName, service, false);
                                        tooManyClist2 = tooManyClist;
                                        b2 = b5;
                                        try {
                                            try {
                                                this.mActiveServicesExt.hookPublishServiceLockedAfterConnected(b2, beginTime);
                                                str = str3;
                                                tooManyConn2 = tooManyConn;
                                            } catch (java.lang.Exception e) {
                                                e = e;
                                                str = str3;
                                                tooManyConn2 = tooManyConn;
                                                android.util.Slog.w("ActivityManager", "Failure sending service " + r.shortInstanceName + " to connection " + c.conn.asBinder() + " (in " + c.binding.client.processName + ")", e);
                                            }
                                        } catch (java.lang.Throwable th3) {
                                            th = th3;
                                            origId = origId2;
                                            this.mAm.mInjector.restoreCallingIdentity(origId);
                                            throw th;
                                        }
                                    } catch (java.lang.Exception e2) {
                                        e = e2;
                                        tooManyClist2 = tooManyClist;
                                        b2 = b5;
                                    }
                                } else {
                                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                        filter = filter2;
                                        b3 = b4;
                                        android.util.Slog.v(TAG_SERVICE, "Not publishing to: " + c);
                                    } else {
                                        filter = filter2;
                                        b3 = b4;
                                    }
                                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                        android.util.Slog.v(TAG_SERVICE, "Bound intent: " + c.binding.intent.intent);
                                    }
                                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                        android.util.Slog.v(TAG_SERVICE, "Published intent: " + intent);
                                    }
                                    str = str3;
                                    tooManyConn2 = tooManyConn;
                                    com.android.server.am.IntentBindRecord intentBindRecord = b3;
                                    tooManyClist2 = tooManyClist;
                                    b2 = intentBindRecord;
                                }
                                i2++;
                                b4 = b2;
                                filter2 = filter;
                                str2 = str4;
                                tooManyClist = tooManyClist2;
                                str3 = str;
                                tooManyConn = tooManyConn2;
                                clist = clist2;
                            }
                            conni--;
                            b4 = b4;
                            filter2 = filter2;
                            origId3 = origId2;
                        }
                        origId2 = origId3;
                        b = b4;
                        i = 20;
                    }
                    serviceDoneExecutingLocked(r, this.mDestroyingServices.contains(r), false, false, (!com.android.server.am.Flags.serviceBindingOomAdjPolicy() || r.wasOomAdjUpdated()) ? i : 0);
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    origId = origId3;
                }
            } else {
                origId2 = origId3;
            }
            this.mAm.mInjector.restoreCallingIdentity(origId2);
        } catch (java.lang.Throwable th5) {
            th = th5;
            origId = origId3;
        }
    }

    void updateServiceGroupLocked(android.app.IServiceConnection connection, int group, int importance) {
        android.os.IBinder binder = connection.asBinder();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, "updateServiceGroup: conn=" + binder);
        }
        java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = this.mServiceConnections.get(binder);
        if (clist == null) {
            throw new java.lang.IllegalArgumentException("Could not find connection for " + connection.asBinder());
        }
        for (int i = clist.size() - 1; i >= 0; i--) {
            com.android.server.am.ConnectionRecord crec = clist.get(i);
            com.android.server.am.ServiceRecord srec = crec.binding.service;
            if (srec != null && (srec.serviceInfo.flags & 2) != 0) {
                if (srec.app != null) {
                    com.android.server.am.ProcessServiceRecord psr = srec.app.mServices;
                    if (group > 0) {
                        psr.setConnectionService(srec);
                        psr.setConnectionGroup(group);
                        psr.setConnectionImportance(importance);
                    } else {
                        psr.setConnectionService(null);
                        psr.setConnectionGroup(0);
                        psr.setConnectionImportance(0);
                    }
                } else if (group > 0) {
                    srec.pendingConnectionGroup = group;
                    srec.pendingConnectionImportance = importance;
                } else {
                    srec.pendingConnectionGroup = 0;
                    srec.pendingConnectionImportance = 0;
                }
            }
        }
    }

    boolean unbindServiceLocked(android.app.IServiceConnection connection) {
        java.lang.String info;
        android.os.IBinder binder = connection.asBinder();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, "unbindService: conn=" + binder);
        }
        java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = this.mServiceConnections.get(binder);
        if (clist == null) {
            android.util.Slog.w("ActivityManager", "Unbind failed: could not find connection for " + connection.asBinder());
            return false;
        }
        int callingPid = this.mAm.mInjector.getCallingPid();
        long origId = this.mAm.mInjector.clearCallingIdentity();
        try {
            if (android.os.Trace.isTagEnabled(64L)) {
                if (clist.size() > 0) {
                    com.android.server.am.ConnectionRecord r = clist.get(0);
                    info = r.binding.service.shortInstanceName + " from " + r.clientProcessName;
                } else {
                    info = java.lang.Integer.toString(callingPid);
                }
                android.os.Trace.traceBegin(64L, "unbindServiceLocked: " + info);
            }
            boolean needOomAdj = false;
            while (clist.size() > 0) {
                com.android.server.am.ConnectionRecord r2 = clist.get(0);
                int serviceBindingOomAdjPolicy = removeConnectionLocked(r2, null, null, true);
                if (clist.size() > 0 && clist.get(0) == r2) {
                    android.util.Slog.wtf("ActivityManager", "Connection " + r2 + " not removed for binder " + binder);
                    clist.remove(0);
                }
                com.android.server.am.ProcessRecord app = r2.binding.service.app;
                if (app != null) {
                    com.android.server.am.ProcessServiceRecord psr = app.mServices;
                    if (psr.mAllowlistManager) {
                        updateAllowlistManagerLocked(psr);
                    }
                    if (r2.hasFlag(134217728)) {
                        psr.setTreatLikeActivity(true);
                        this.mAm.updateLruProcessLocked(app, true, null);
                    }
                    if (serviceBindingOomAdjPolicy == 0) {
                        this.mAm.lambda$appDiedLocked$2(app);
                        needOomAdj = true;
                    }
                }
            }
            if (needOomAdj) {
                this.mAm.updateOomAdjPendingTargetsLocked(5);
            }
            return true;
        } finally {
            android.os.Trace.traceEnd(64L);
            this.mAm.mInjector.restoreCallingIdentity(origId);
        }
    }

    void unbindFinishedLocked(com.android.server.am.ServiceRecord r, android.content.Intent intent, boolean doRebind) throws java.lang.Throwable {
        boolean inFg;
        long origId = this.mAm.mInjector.clearCallingIdentity();
        if (r != null) {
            try {
                try {
                    android.content.Intent.FilterComparison filter = new android.content.Intent.FilterComparison(intent);
                    com.android.server.am.IntentBindRecord b = r.bindings.get(filter);
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                        android.util.Slog.v(TAG_SERVICE, "unbindFinished in " + r + " at " + b + ": apps=" + (b != null ? b.apps.size() : 0));
                    }
                    boolean inDestroying = this.mDestroyingServices.contains(r);
                    if (b != null) {
                        if (b.apps.size() > 0 && !inDestroying) {
                            int i = b.apps.size() - 1;
                            while (true) {
                                if (i < 0) {
                                    inFg = false;
                                    break;
                                }
                                com.android.server.am.ProcessRecord client = b.apps.valueAt(i).client;
                                if (client == null || client.mState.getSetSchedGroup() == 0) {
                                    i--;
                                } else {
                                    inFg = true;
                                    break;
                                }
                            }
                            try {
                                requestServiceBindingLocked(r, b, inFg, true, 0);
                            } catch (android.os.TransactionTooLargeException e) {
                            }
                        } else {
                            b.doRebind = true;
                        }
                    }
                    serviceDoneExecutingLocked(r, inDestroying, false, false, (!com.android.server.am.Flags.serviceBindingOomAdjPolicy() || r.wasOomAdjUpdated()) ? 5 : 0);
                } catch (java.lang.Throwable th) {
                    th = th;
                    this.mAm.mInjector.restoreCallingIdentity(origId);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
        this.mAm.mInjector.restoreCallingIdentity(origId);
    }

    private final com.android.server.am.ServiceRecord findServiceLocked(android.content.ComponentName name, android.os.IBinder token, int userId) {
        com.android.server.am.ServiceRecord r = getServiceByNameLocked(name, userId);
        if (r == token) {
            return r;
        }
        return null;
    }

    private final class ServiceLookupResult {
        final android.content.ComponentName aliasComponent;
        final java.lang.String permission;
        final com.android.server.am.ServiceRecord record;

        ServiceLookupResult(com.android.server.am.ServiceRecord _record, android.content.ComponentName _aliasComponent) {
            this.record = _record;
            this.permission = null;
            this.aliasComponent = _aliasComponent;
        }

        ServiceLookupResult(java.lang.String _permission) {
            this.record = null;
            this.permission = _permission;
            this.aliasComponent = null;
        }
    }

    private class ServiceRestarter implements java.lang.Runnable {
        private com.android.server.am.ServiceRecord mService;

        private ServiceRestarter() {
        }

        void setService(com.android.server.am.ServiceRecord service) {
            this.mService = service;
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActiveServices.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActiveServices.this.performServiceRestartLocked(this.mService);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    private com.android.server.am.ActiveServices.ServiceLookupResult retrieveServiceLocked(android.content.Intent service, java.lang.String instanceName, java.lang.String resolvedType, java.lang.String callingPackage, int callingPid, int callingUid, int userId, boolean createIfNeeded, boolean callingFromFg, boolean isBindExternal, boolean allowInstant, boolean inSharedIsolatedProcess, boolean inPrivateSharedIsolatedProcess) {
        return retrieveServiceLocked(service, instanceName, false, -1, null, resolvedType, callingPackage, callingPid, callingUid, userId, createIfNeeded, callingFromFg, isBindExternal, allowInstant, null, inSharedIsolatedProcess, inPrivateSharedIsolatedProcess);
    }

    private java.lang.String generateAdditionalSeInfoFromService(android.content.Intent service) {
        if (service != null && service.getAction() != null) {
            if (service.getAction().equals("android.service.voice.HotwordDetectionService") || service.getAction().equals("android.service.voice.VisualQueryDetectionService") || service.getAction().equals("android.service.wearable.WearableSensingService") || service.getAction().equals("android.service.ondeviceintelligence.OnDeviceSandboxedInferenceService")) {
                return ":isolatedComputeApp";
            }
            return "";
        }
        return "";
    }

    private com.android.server.am.ActiveServices.ServiceLookupResult retrieveServiceLocked(android.content.Intent service, java.lang.String instanceName, boolean isSdkSandboxService, int sdkSandboxClientAppUid, java.lang.String sdkSandboxClientAppPackage, java.lang.String resolvedType, java.lang.String callingPackage, int callingPid, int callingUid, int userId, boolean createIfNeeded, boolean callingFromFg, boolean isBindExternal, boolean allowInstant, android.app.ForegroundServiceDelegationOptions fgsDelegateOptions, boolean inSharedIsolatedProcess, boolean inPrivateSharedIsolatedProcess) {
        return retrieveServiceLocked(service, instanceName, isSdkSandboxService, sdkSandboxClientAppUid, sdkSandboxClientAppPackage, resolvedType, callingPackage, callingPid, callingUid, userId, createIfNeeded, callingFromFg, isBindExternal, allowInstant, fgsDelegateOptions, inSharedIsolatedProcess, inPrivateSharedIsolatedProcess, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 29, insn: 0x0774: MOVE (r8 I:??[OBJECT, ARRAY]) = (r29 I:??[OBJECT, ARRAY]), block:B:250:0x076f */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0667  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x07e2  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0825  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0827 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:332:0x095b  */
    /* JADX WARN: Removed duplicated region for block: B:462:0x0698 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r12v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v16 */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v18 */
    /* JADX WARN: Type inference failed for: r8v22 */
    /* JADX WARN: Type inference failed for: r8v24 */
    /* JADX WARN: Type inference failed for: r8v25 */
    /* JADX WARN: Type inference failed for: r8v26 */
    /* JADX WARN: Type inference failed for: r8v28 */
    /* JADX WARN: Type inference failed for: r8v29 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v31 */
    /* JADX WARN: Type inference failed for: r8v33 */
    /* JADX WARN: Type inference failed for: r8v36 */
    /* JADX WARN: Type inference failed for: r8v37 */
    /* JADX WARN: Type inference failed for: r8v38 */
    /* JADX WARN: Type inference failed for: r8v39 */
    /* JADX WARN: Type inference failed for: r8v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v42 */
    /* JADX WARN: Type inference failed for: r8v45 */
    /* JADX WARN: Type inference failed for: r8v46 */
    /* JADX WARN: Type inference failed for: r8v49, types: [boolean] */
    /* JADX WARN: Type inference failed for: r8v50 */
    /* JADX WARN: Type inference failed for: r8v51 */
    /* JADX WARN: Type inference failed for: r8v53 */
    /* JADX WARN: Type inference failed for: r8v54 */
    /* JADX WARN: Type inference failed for: r8v55 */
    /* JADX WARN: Type inference failed for: r8v56 */
    /* JADX WARN: Type inference failed for: r8v57 */
    /* JADX WARN: Type inference failed for: r8v58 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v60 */
    /* JADX WARN: Type inference failed for: r8v61 */
    /* JADX WARN: Type inference failed for: r8v62 */
    /* JADX WARN: Type inference failed for: r8v63 */
    /* JADX WARN: Type inference failed for: r8v64 */
    /* JADX WARN: Type inference failed for: r8v65 */
    /* JADX WARN: Type inference failed for: r8v8 */
    /* JADX WARN: Type inference failed for: r8v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.am.ActiveServices.ServiceLookupResult retrieveServiceLocked(android.content.Intent r55, java.lang.String r56, boolean r57, int r58, java.lang.String r59, java.lang.String r60, java.lang.String r61, int r62, int r63, int r64, boolean r65, boolean r66, boolean r67, boolean r68, android.app.ForegroundServiceDelegationOptions r69, boolean r70, boolean r71, boolean r72) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 3181
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.retrieveServiceLocked(android.content.Intent, java.lang.String, boolean, int, java.lang.String, java.lang.String, java.lang.String, int, int, int, boolean, boolean, boolean, boolean, android.app.ForegroundServiceDelegationOptions, boolean, boolean, boolean):com.android.server.am.ActiveServices$ServiceLookupResult");
    }

    private int getAllowMode(android.content.Intent service, java.lang.String callingPackage) {
        if (callingPackage != null && service.getComponent() != null && callingPackage.equals(service.getComponent().getPackageName())) {
            return 3;
        }
        return 1;
    }

    private void bumpServiceExecutingLocked(com.android.server.am.ServiceRecord r, boolean fg, java.lang.String why, int oomAdjReason, boolean skipTimeoutIfPossible) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, ">>> EXECUTING " + why + " of " + r + " in app " + r.app);
        } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE_EXECUTING) {
            android.util.Slog.v(TAG_SERVICE_EXECUTING, ">>> EXECUTING " + why + " of " + r.shortInstanceName);
        }
        boolean timeoutNeeded = true;
        if (this.mAm.mBootPhase < 600 && r.app != null && r.app.getPid() == com.android.server.am.ActivityManagerService.MY_PID) {
            android.util.Slog.w("ActivityManager", "Too early to start/bind service in system_server: Phase=" + this.mAm.mBootPhase + " " + r.getComponentName());
            timeoutNeeded = false;
        }
        if (timeoutNeeded && this.mActiveServicesExt.setTimeoutNeededToFalseIfNeed(r, fg, why)) {
            timeoutNeeded = false;
        }
        boolean shouldSkipTimeout = skipTimeoutIfPossible && r.app != null && (r.app.mOptRecord.isPendingFreeze() || r.app.mOptRecord.isFrozen());
        if (r.executeNesting == 0) {
            r.executeFg = fg;
            synchronized (this.mAm.mProcessStats.mLock) {
                com.android.internal.app.procstats.ServiceState stracker = r.getTracker();
                if (stracker != null) {
                    stracker.setExecuting(true, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                }
            }
            if (r.app != null) {
                com.android.server.am.ProcessServiceRecord psr = r.app.mServices;
                psr.startExecutingService(r);
                psr.setExecServicesFg(psr.shouldExecServicesFg() || fg);
                if (timeoutNeeded && psr.numberOfExecutingServices() == 1) {
                    if (!shouldSkipTimeout) {
                        scheduleServiceTimeoutLocked(r.app);
                    } else {
                        r.app.mServices.noteScheduleServiceTimeoutPending(true);
                    }
                }
            }
        } else if (r.app != null && fg) {
            com.android.server.am.ProcessServiceRecord psr2 = r.app.mServices;
            if (!psr2.shouldExecServicesFg()) {
                psr2.setExecServicesFg(true);
                if (timeoutNeeded) {
                    if (!shouldSkipTimeout) {
                        scheduleServiceTimeoutLocked(r.app);
                    } else {
                        r.app.mServices.noteScheduleServiceTimeoutPending(true);
                    }
                }
            }
        }
        if (r.app != null && r.app.mState.getCurProcState() > 10) {
            this.mAm.lambda$appDiedLocked$2(r.app);
            r.updateOomAdjSeq();
            if (oomAdjReason != 0) {
                this.mAm.updateOomAdjPendingTargetsLocked(oomAdjReason);
            }
        }
        r.executeFg |= fg;
        r.executeNesting++;
        r.executingStart = android.os.SystemClock.uptimeMillis();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0170  */
    /* JADX WARN: Type inference failed for: r17v0 */
    /* JADX WARN: Type inference failed for: r17v1 */
    /* JADX WARN: Type inference failed for: r17v2 */
    /* JADX WARN: Type inference failed for: r17v3 */
    /* JADX WARN: Type inference failed for: r18v0, types: [com.android.server.am.ActiveServices] */
    /* JADX WARN: Type inference failed for: r6v3 */
    /* JADX WARN: Type inference failed for: r6v4 */
    /* JADX WARN: Type inference failed for: r6v5, types: [int] */
    /* JADX WARN: Type inference failed for: r6v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final boolean requestServiceBindingLocked(com.android.server.am.ServiceRecord r19, com.android.server.am.IntentBindRecord r20, boolean r21, boolean r22, int r23) throws android.os.TransactionTooLargeException {
        /*
            Method dump skipped, instruction units count: 385
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.requestServiceBindingLocked(com.android.server.am.ServiceRecord, com.android.server.am.IntentBindRecord, boolean, boolean, int):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x02a9, code lost:
    
        r2 = 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final boolean scheduleServiceRestartLocked(com.android.server.am.ServiceRecord r28, boolean r29) {
        /*
            Method dump skipped, instruction units count: 912
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.scheduleServiceRestartLocked(com.android.server.am.ServiceRecord, boolean):boolean");
    }

    void performScheduleRestartLocked(com.android.server.am.ServiceRecord r, java.lang.String scheduling, java.lang.String reason, long now) {
        if (r.fgRequired && r.fgWaiting) {
            this.mServiceFGAnrTimer.cancel(r);
            r.fgWaiting = false;
            this.mActiveServicesExt.updateExecutingComponent(r.appInfo.uid, "fg-service", 2);
        }
        this.mAm.mHandler.removeCallbacks(r.restarter);
        this.mAm.mHandler.postAtTime(r.restarter, r.nextRestartTime);
        r.nextRestartTime = r.restartDelay + now;
        if (DEBUG_DELAYED_SERVICE || DEBUG_PANIC_FLAG) {
            android.util.Slog.w("ActivityManager", scheduling + " restart of crashed service " + r.shortInstanceName + " in " + r.restartDelay + "ms for " + reason);
        }
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_SCHEDULE_SERVICE_RESTART, java.lang.Integer.valueOf(r.userId), r.shortInstanceName, java.lang.Long.valueOf(r.restartDelay));
        if (DEBUG_DELAYED_SERVICE) {
            android.util.Slog.v("ActivityManager", "scheduleServiceRestartLocked r " + r + " call by " + android.os.Debug.getCallers(8));
        }
    }

    void rescheduleServiceRestartOnMemoryPressureIfNeededLocked(int prevMemFactor, int curMemFactor, java.lang.String reason, long now) {
        boolean enabled = this.mAm.mConstants.mEnableExtraServiceRestartDelayOnMemPressure;
        if (!enabled) {
            return;
        }
        if (DEBUG_PANIC_FLAG) {
            android.util.Slog.d("ActivityManager", "rescheduleServiceRestartOnMemoryPressureIfNeededLocked for " + reason + " , preMem: " + prevMemFactor + ", curMem: " + curMemFactor);
        }
        performRescheduleServiceRestartOnMemoryPressureLocked(this.mAm.mConstants.mExtraServiceRestartDelayOnMemPressure[prevMemFactor], this.mAm.mConstants.mExtraServiceRestartDelayOnMemPressure[curMemFactor], reason, now);
    }

    void rescheduleServiceRestartOnMemoryPressureIfNeededLocked(boolean prevEnabled, boolean curEnabled, long now) {
        if (prevEnabled == curEnabled) {
            return;
        }
        int memFactor = this.mAm.mAppProfiler.getLastMemoryLevelLocked();
        long delay = this.mAm.mConstants.mExtraServiceRestartDelayOnMemPressure[memFactor];
        performRescheduleServiceRestartOnMemoryPressureLocked(prevEnabled ? delay : 0L, curEnabled ? delay : 0L, "config", now);
    }

    void rescheduleServiceRestartIfPossibleLocked(long extraRestartTimeBetween, long minRestartTimeBetween, java.lang.String reason, long now) {
        int i;
        int size;
        com.android.server.am.ServiceRecord r;
        com.android.server.am.ServiceRecord r2;
        long j;
        com.android.server.am.ActiveServices activeServices = this;
        long j2 = now;
        long restartTimeBetween = extraRestartTimeBetween + minRestartTimeBetween;
        long spanForInsertOne = restartTimeBetween * 2;
        long lastRestartTime = now;
        int lastRestartTimePos = -1;
        int i2 = 0;
        int size2 = activeServices.mRestartingServices.size();
        while (i2 < size2) {
            com.android.server.am.ServiceRecord r3 = activeServices.mRestartingServices.get(i2);
            if ((r3.serviceInfo.applicationInfo.flags & 8) != 0 || !activeServices.isServiceRestartBackoffEnabledLocked(r3.packageName)) {
                i = i2;
                size = size2;
                com.android.server.am.ServiceRecord r4 = r3;
                lastRestartTime = r4.nextRestartTime;
                lastRestartTimePos = i;
                i2 = i + 1;
                j2 = now;
                size2 = size;
            } else {
                long oldVal = r3.nextRestartTime;
                if (lastRestartTime + restartTimeBetween <= r3.mEarliestRestartTime) {
                    long j3 = r3.mEarliestRestartTime;
                    if (i2 > 0) {
                        j = activeServices.mRestartingServices.get(i2 - 1).nextRestartTime + restartTimeBetween;
                    } else {
                        j = 0;
                    }
                    r3.nextRestartTime = java.lang.Math.max(j2, java.lang.Math.max(j3, j));
                    size = size2;
                    r2 = r3;
                } else {
                    if (lastRestartTime <= j2) {
                        r3.nextRestartTime = java.lang.Math.max(j2, java.lang.Math.max(r3.mEarliestRestartTime, r3.mRestartSchedulingTime + extraRestartTimeBetween));
                        com.android.server.am.IActiveServicesExt iActiveServicesExt = activeServices.mActiveServicesExt;
                        size = size2;
                        r = r3;
                        iActiveServicesExt.adjustRescheduleServiceRestartDelayIfNeed(r3, oldVal, now, restartTimeBetween);
                    } else {
                        size = size2;
                        r = r3;
                        r.nextRestartTime = java.lang.Math.max(j2, lastRestartTime + restartTimeBetween);
                    }
                    if (i2 <= lastRestartTimePos + 1) {
                        r2 = r;
                        activeServices = this;
                    } else {
                        r2 = r;
                        activeServices = this;
                        activeServices.mRestartingServices.remove(i2);
                        activeServices.mRestartingServices.add(lastRestartTimePos + 1, r2);
                    }
                }
                int lastRestartTimePos2 = lastRestartTimePos;
                long j4 = lastRestartTime;
                int j5 = lastRestartTimePos + 1;
                long lastRestartTime2 = j4;
                while (j5 <= i2) {
                    com.android.server.am.ServiceRecord r22 = activeServices.mRestartingServices.get(j5);
                    long timeInBetween = r22.nextRestartTime - (j5 == 0 ? lastRestartTime2 : activeServices.mRestartingServices.get(j5 - 1).nextRestartTime);
                    if (timeInBetween >= spanForInsertOne) {
                        break;
                    }
                    lastRestartTime2 = r22.nextRestartTime;
                    lastRestartTimePos2 = j5;
                    j5++;
                }
                r2.restartDelay = r2.nextRestartTime - j2;
                i = i2;
                performScheduleRestartLocked(r2, "Rescheduling", reason, now);
                lastRestartTime = lastRestartTime2;
                lastRestartTimePos = lastRestartTimePos2;
                i2 = i + 1;
                j2 = now;
                size2 = size;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void performRescheduleServiceRestartOnMemoryPressureLocked(long r24, long r26, java.lang.String r28, long r29) {
        /*
            Method dump skipped, instruction units count: 226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.performRescheduleServiceRestartOnMemoryPressureLocked(long, long, java.lang.String, long):void");
    }

    long getExtraRestartTimeInBetweenLocked() {
        if (!this.mAm.mConstants.mEnableExtraServiceRestartDelayOnMemPressure) {
            return 0L;
        }
        int memFactor = this.mAm.mAppProfiler.getLastMemoryLevelLocked();
        return this.mAm.mConstants.mExtraServiceRestartDelayOnMemPressure[memFactor];
    }

    final void performServiceRestartLocked(com.android.server.am.ServiceRecord r) {
        if (!this.mRestartingServices.contains(r)) {
            return;
        }
        r.mServiceRecordExt.setExceptionWhenBringUp(false);
        if (!isServiceNeededLocked(r, false, false)) {
            android.util.Slog.wtf("ActivityManager", "Restarting service that is not needed: " + r);
            return;
        }
        if (this.mActiveServicesExt.rescheduleServiceIfNeeded(r, this.mAm.mHandler)) {
            return;
        }
        try {
            this.mActiveServicesExt.hookPerformRestartServiceBegin(r);
            bringUpServiceLocked(r, r.intent.getIntent().getFlags(), r.createdFromFg, true, false, false, true, 0);
        } catch (android.os.TransactionTooLargeException e) {
        } catch (java.lang.Throwable th) {
            this.mAm.updateOomAdjPendingTargetsLocked(6);
            throw th;
        }
        this.mAm.updateOomAdjPendingTargetsLocked(6);
    }

    private final boolean unscheduleServiceRestartLocked(com.android.server.am.ServiceRecord r, int callingUid, boolean force) {
        if (!force && r.restartDelay == 0) {
            return false;
        }
        boolean removed = this.mRestartingServices.remove(r);
        if (removed || callingUid != r.appInfo.uid) {
            r.resetRestartCounter();
        }
        if (removed) {
            clearRestartingIfNeededLocked(r);
        }
        r.mServiceRecordExt.setExceptionWhenBringUp(false);
        this.mAm.mHandler.removeCallbacks(r.restarter);
        return true;
    }

    private void clearRestartingIfNeededLocked(com.android.server.am.ServiceRecord r) {
        if (r.restartTracker != null) {
            boolean stillTracking = false;
            int i = this.mRestartingServices.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                if (this.mRestartingServices.get(i).restartTracker != r.restartTracker) {
                    i--;
                } else {
                    stillTracking = true;
                    break;
                }
            }
            if (!stillTracking) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    r.restartTracker.setRestarting(false, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                }
                r.restartTracker = null;
            }
        }
    }

    void setServiceRestartBackoffEnabledLocked(java.lang.String packageName, boolean enable, java.lang.String reason) {
        if (!enable) {
            if (this.mRestartBackoffDisabledPackages.contains(packageName)) {
                return;
            }
            this.mRestartBackoffDisabledPackages.add(packageName);
            long now = android.os.SystemClock.uptimeMillis();
            int size = this.mRestartingServices.size();
            for (int i = 0; i < size; i++) {
                com.android.server.am.ServiceRecord r = this.mRestartingServices.get(i);
                if (android.text.TextUtils.equals(r.packageName, packageName)) {
                    long remaining = r.nextRestartTime - now;
                    if (remaining > this.mAm.mConstants.SERVICE_RESTART_DURATION) {
                        r.restartDelay = this.mAm.mConstants.SERVICE_RESTART_DURATION;
                        r.nextRestartTime = r.restartDelay + now;
                        performScheduleRestartLocked(r, "Rescheduling", reason, now);
                    }
                }
                java.util.Collections.sort(this.mRestartingServices, new java.util.Comparator() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda0
                    @Override // java.util.Comparator
                    public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                        return com.android.server.am.ActiveServices.lambda$setServiceRestartBackoffEnabledLocked$0((com.android.server.am.ServiceRecord) obj, (com.android.server.am.ServiceRecord) obj2);
                    }
                });
            }
            return;
        }
        removeServiceRestartBackoffEnabledLocked(packageName);
    }

    static /* synthetic */ int lambda$setServiceRestartBackoffEnabledLocked$0(com.android.server.am.ServiceRecord a, com.android.server.am.ServiceRecord b) {
        return (int) (a.nextRestartTime - b.nextRestartTime);
    }

    private void removeServiceRestartBackoffEnabledLocked(java.lang.String packageName) {
        this.mRestartBackoffDisabledPackages.remove(packageName);
    }

    boolean isServiceRestartBackoffEnabledLocked(java.lang.String packageName) {
        return !this.mRestartBackoffDisabledPackages.contains(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String bringUpServiceLocked(com.android.server.am.ServiceRecord r, int intentFlags, boolean execInFg, boolean whileRestarting, boolean permissionsReviewRequired, boolean packageFrozen, boolean enqueueOomAdj, int serviceBindingOomAdjPolicy) throws android.os.TransactionTooLargeException {
        try {
            if (android.os.Trace.isTagEnabled(64L)) {
                android.os.Trace.traceBegin(64L, "bringUpServiceLocked: " + r.shortInstanceName);
            }
            return bringUpServiceInnerLocked(r, intentFlags, execInFg, whileRestarting, permissionsReviewRequired, packageFrozen, enqueueOomAdj, serviceBindingOomAdjPolicy);
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:181:0x047c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x054e  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0557  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x05a7  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x05b0  */
    /* JADX WARN: Removed duplicated region for block: B:243:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r17v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r17v13 */
    /* JADX WARN: Type inference failed for: r17v18 */
    /* JADX WARN: Type inference failed for: r17v21 */
    /* JADX WARN: Type inference failed for: r17v24 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String bringUpServiceInnerLocked(com.android.server.am.ServiceRecord r33, int r34, boolean r35, boolean r36, boolean r37, boolean r38, boolean r39, int r40) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1496
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.bringUpServiceInnerLocked(com.android.server.am.ServiceRecord, int, boolean, boolean, boolean, boolean, boolean, int):java.lang.String");
    }

    private java.lang.String getHostingRecordTriggerType(com.android.server.am.ServiceRecord r) {
        if ("android.permission.BIND_JOB_SERVICE".equals(r.permission) && r.mRecentCallingUid == 1000) {
            return com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB;
        }
        return "unknown";
    }

    private void requestServiceBindingsLocked(com.android.server.am.ServiceRecord r, boolean execInFg, int serviceBindingOomAdjPolicy) throws android.os.TransactionTooLargeException {
        for (int i = r.bindings.size() - 1; i >= 0; i--) {
            com.android.server.am.IntentBindRecord ibr = r.bindings.valueAt(i);
            if (!requestServiceBindingLocked(r, ibr, execInFg, false, serviceBindingOomAdjPolicy)) {
                return;
            }
        }
    }

    private int getServiceBindingOomAdjPolicyForAddLocked(com.android.server.am.ProcessRecord clientApp, com.android.server.am.ProcessRecord hostApp, com.android.server.am.ConnectionRecord cr) {
        int policy = 0;
        if (!com.android.server.am.Flags.serviceBindingOomAdjPolicy() || clientApp == null || hostApp == null) {
            return 0;
        }
        if (clientApp == hostApp) {
            policy = 7;
        } else if (clientApp.isCached()) {
            policy = 7;
            if (clientApp.isFreezable()) {
                policy = 7 | 8;
            }
        }
        if ((policy & 4) == 0 && !this.mAm.mOomAdjuster.evaluateServiceConnectionAdd(clientApp, hostApp, cr)) {
            return 7;
        }
        return policy;
    }

    private int getServiceBindingOomAdjPolicyForRemovalLocked(com.android.server.am.ProcessRecord clientApp, com.android.server.am.ProcessRecord hostApp, com.android.server.am.ConnectionRecord cr) {
        if (!com.android.server.am.Flags.serviceBindingOomAdjPolicy() || clientApp == null || hostApp == null || cr == null) {
            return 0;
        }
        if (clientApp != hostApp && this.mAm.mOomAdjuster.evaluateServiceConnectionRemoval(clientApp, hostApp, cr)) {
            return 0;
        }
        return 7;
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0294  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void realStartServiceLocked(com.android.server.am.ServiceRecord r25, com.android.server.am.ProcessRecord r26, android.app.IApplicationThread r27, int r28, com.android.server.am.UidRecord r29, boolean r30, boolean r31, int r32) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 673
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.realStartServiceLocked(com.android.server.am.ServiceRecord, com.android.server.am.ProcessRecord, android.app.IApplicationThread, int, com.android.server.am.UidRecord, boolean, boolean, int):void");
    }

    private final void sendServiceArgsLocked(com.android.server.am.ServiceRecord r, boolean execInFg, boolean oomAdjusted) throws android.os.TransactionTooLargeException {
        int N = r.pendingStarts.size();
        if (N == 0) {
            return;
        }
        java.util.ArrayList<android.app.ServiceStartArgs> args = new java.util.ArrayList<>();
        while (r.pendingStarts.size() > 0) {
            com.android.server.am.ServiceRecord.StartItem si = r.pendingStarts.remove(0);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                android.util.Slog.v(TAG_SERVICE, "Sending arguments to: " + r + " " + r.intent + " args=" + si.intent);
            }
            if (si.intent != null || N <= 1) {
                si.deliveredTime = android.os.SystemClock.uptimeMillis();
                r.deliveredStarts.add(si);
                si.deliveryCount++;
                if (si.neededGrants != null) {
                    this.mAm.mUgmInternal.grantUriPermissionUncheckedFromIntent(si.neededGrants, si.getUriPermissionsLocked());
                }
                this.mAm.grantImplicitAccess(r.userId, si.intent, si.callingId, android.os.UserHandle.getAppId(r.appInfo.uid));
                bumpServiceExecutingLocked(r, execInFg, "start", 0, false);
                if (r.fgRequired && !r.fgWaiting) {
                    if (!r.isForeground) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                            android.util.Slog.i("ActivityManager", "Launched service must call startForeground() within timeout: " + r);
                        }
                        scheduleServiceForegroundTransitionTimeoutLocked(r);
                    } else {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                            android.util.Slog.i("ActivityManager", "Service already foreground; no new timeout: " + r);
                        }
                        r.fgRequired = false;
                    }
                }
                int flags = 0;
                if (si.deliveryCount > 1) {
                    flags = 0 | 2;
                }
                if (si.doneExecutingCount > 0) {
                    flags |= 1;
                }
                args.add(new android.app.ServiceStartArgs(si.taskRemoved, si.id, flags, si.intent));
            }
        }
        if (!oomAdjusted) {
            this.mAm.lambda$appDiedLocked$2(r.app);
            this.mAm.updateOomAdjPendingTargetsLocked(6);
        }
        android.content.pm.ParceledListSlice<android.app.ServiceStartArgs> slice = new android.content.pm.ParceledListSlice<>(args);
        slice.setInlineCountLimit(4);
        try {
            r.app.getThread().scheduleServiceArgs(r, slice);
            caughtException = null;
        } catch (android.os.TransactionTooLargeException e) {
            caughtException = e;
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                android.util.Slog.v(TAG_SERVICE, "Transaction too large for " + args.size() + " args, first: " + args.get(0).args);
            }
            android.util.Slog.w("ActivityManager", "Failed delivering service starts", caughtException);
        } catch (android.os.RemoteException e2) {
            caughtException = e2;
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                android.util.Slog.v(TAG_SERVICE, "Crashed while sending args: " + r);
            }
            android.util.Slog.w("ActivityManager", "Failed delivering service starts", caughtException);
        } catch (java.lang.Exception e3) {
            caughtException = e3;
            android.util.Slog.w("ActivityManager", "Unexpected exception", caughtException);
        }
        if (caughtException != null) {
            boolean inDestroying = this.mDestroyingServices.contains(r);
            int size = args.size();
            for (int i = 0; i < size; i++) {
                serviceDoneExecutingLocked(r, inDestroying, inDestroying, true, 19);
            }
            this.mAm.updateOomAdjPendingTargetsLocked(19);
            if (caughtException instanceof android.os.TransactionTooLargeException) {
                throw ((android.os.TransactionTooLargeException) caughtException);
            }
        }
    }

    private final boolean isServiceNeededLocked(com.android.server.am.ServiceRecord r, boolean knowConn, boolean hasConn) {
        if (r.startRequested) {
            return true;
        }
        if (!knowConn) {
            hasConn = r.hasAutoCreateConnections();
        }
        return hasConn;
    }

    private void bringDownServiceIfNeededLocked(com.android.server.am.ServiceRecord r, boolean knowConn, boolean hasConn, boolean enqueueOomAdj, java.lang.String debugReason) throws java.lang.Throwable {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.i("ActivityManager", "Bring down service for " + debugReason + " :" + r.toString());
        }
        if (isServiceNeededLocked(r, knowConn, hasConn) || this.mPendingServices.contains(r)) {
            return;
        }
        com.android.server.am.ActiveServices.ServiceMap smap = getServiceMapLocked(r.userId);
        com.android.server.am.ServiceRecord found = smap.mServicesByInstanceName.remove(r.instanceName);
        if (found != null && found != r) {
            android.util.Slog.i("ActivityManager", "trying to bring down a service record that has been brought down once " + r);
        } else {
            if (this.mActiveServicesExt.interceptBringDownServiceIfNeeded(getServiceMapLocked(r.userId), r)) {
                return;
            }
            bringDownServiceLocked(r, enqueueOomAdj);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x0379  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x03d0  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0475  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x048c  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x04a2  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x056a  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0588  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x05a0  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x05ab  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x05c4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x02ce  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0304  */
    /* JADX WARN: Type inference failed for: r13v19 */
    /* JADX WARN: Type inference failed for: r13v20 */
    /* JADX WARN: Type inference failed for: r13v3, types: [boolean, int] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void bringDownServiceLocked(final com.android.server.am.ServiceRecord r26, boolean r27) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1525
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.bringDownServiceLocked(com.android.server.am.ServiceRecord, boolean):void");
    }

    private void dropFgsNotificationStateLocked(com.android.server.am.ServiceRecord r) {
        if (r.foregroundNoti == null) {
            return;
        }
        boolean shared = false;
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(r.userId);
        if (smap != null) {
            int numServices = smap.mServicesByInstanceName.size();
            int i = 0;
            while (true) {
                if (i >= numServices) {
                    break;
                }
                com.android.server.am.ServiceRecord sr = smap.mServicesByInstanceName.valueAt(i);
                if (sr == r || !sr.isForeground || r.foregroundId != sr.foregroundId || !r.appInfo.packageName.equals(sr.appInfo.packageName)) {
                    i++;
                } else {
                    shared = true;
                    break;
                }
            }
        } else {
            android.util.Slog.wtf("ActivityManager", "FGS " + r + " not found!");
        }
        if (!shared) {
            r.stripForegroundServiceFlagFromNotification();
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    int removeConnectionLocked(com.android.server.am.ConnectionRecord c, com.android.server.am.ProcessRecord skipApp, com.android.server.wm.ActivityServiceConnectionsHolder skipAct, boolean enqueueOomAdj) {
        boolean z;
        int serviceBindingOomAdjPolicy;
        android.os.IBinder binder = c.conn.asBinder();
        com.android.server.am.AppBindRecord b = c.binding;
        com.android.server.am.ServiceRecord s = b.service;
        java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = s.getConnections().get(binder);
        if (clist != null) {
            clist.remove(c);
            if (clist.size() == 0) {
                s.removeConnection(binder);
            }
        }
        b.connections.remove(c);
        c.stopAssociation();
        if (c.activity != null && c.activity != skipAct) {
            c.activity.removeConnection(c);
        }
        if (b.client != skipApp) {
            com.android.server.am.ProcessServiceRecord psr = b.client.mServices;
            psr.removeConnection(c);
            if (c.hasFlag(8)) {
                psr.updateHasAboveClientLocked();
            }
            if (c.hasFlag(16777216)) {
                s.updateAllowlistManager();
                if (!s.allowlistManager && s.app != null) {
                    updateAllowlistManagerLocked(s.app.mServices);
                }
            }
            if (c.hasFlag(1048576)) {
                s.updateIsAllowedBgActivityStartsByBinding();
            }
            if (c.hasFlag(65536)) {
                psr.updateHasTopStartedAlmostPerceptibleServices();
            }
            if (s.app != null) {
                updateServiceClientActivitiesLocked(s.app.mServices, c, true);
            }
        }
        java.util.ArrayList<com.android.server.am.ConnectionRecord> clist2 = this.mServiceConnections.get(binder);
        if (clist2 != null) {
            clist2.remove(c);
            if (clist2.size() == 0) {
                this.mServiceConnections.remove(binder);
            }
        }
        this.mActiveServicesExt.hookUpdateServiceBindStatus(s, b.intent.intent.getIntent().getAction(), false);
        this.mAm.stopAssociationLocked(b.client.uid, b.client.processName, s.appInfo.uid, s.appInfo.longVersionCode, s.instanceName, s.processName);
        this.mActiveServicesExt.noteAssociation(b.client.uid, s.appInfo.uid, false);
        if (b.connections.size() == 0) {
            b.intent.apps.remove(b.client);
        }
        if (c.serviceDead) {
            return 0;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, "Disconnecting binding " + b.intent + ": shouldUnbind=" + b.intent.hasBound);
        }
        if (s.app != null && s.app.isThreadReady() && b.intent.apps.size() == 0 && b.intent.hasBound) {
            serviceBindingOomAdjPolicy = getServiceBindingOomAdjPolicyForRemovalLocked(b.client, s.app, c);
            boolean skipOomAdj = (serviceBindingOomAdjPolicy & 4) != 0;
            z = false;
            try {
                bumpServiceExecutingLocked(s, false, "unbind", skipOomAdj ? 0 : 5, skipOomAdj);
                if (b.client != s.app && c.notHasFlag(32) && s.app.mState.getSetProcState() <= 13) {
                    this.mAm.updateLruProcessLocked(s.app, false, null);
                }
                b.intent.hasBound = false;
                b.intent.doRebind = false;
                s.app.getThread().scheduleUnbindService(s, b.intent.intent.getIntent());
            } catch (java.lang.Exception e) {
                android.util.Slog.w("ActivityManager", "Exception when unbinding service " + s.shortInstanceName, e);
                serviceProcessGoneLocked(s, enqueueOomAdj);
            }
        } else {
            z = false;
            serviceBindingOomAdjPolicy = 0;
        }
        if (s.getConnections().isEmpty()) {
            this.mPendingServices.remove(s);
            this.mPendingBringups.remove(s);
        }
        if (c.hasFlag(1)) {
            boolean hasAutoCreate = s.hasAutoCreateConnections();
            if (!hasAutoCreate && s.tracker != null) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    s.tracker.setBound(z, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                }
            }
            bringDownServiceIfNeededLocked(s, true, hasAutoCreate, enqueueOomAdj, "removeConnection");
        }
        return serviceBindingOomAdjPolicy;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00db  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void serviceDoneExecutingLocked(com.android.server.am.ServiceRecord r17, int r18, int r19, int r20, boolean r21, android.content.Intent r22) {
        /*
            Method dump skipped, instruction units count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.serviceDoneExecutingLocked(com.android.server.am.ServiceRecord, int, int, int, boolean, android.content.Intent):void");
    }

    private void serviceProcessGoneLocked(com.android.server.am.ServiceRecord r, boolean enqueueOomAdj) {
        if (r.tracker != null) {
            synchronized (this.mAm.mProcessStats.mLock) {
                int memFactor = this.mAm.mProcessStats.getMemFactorLocked();
                long now = android.os.SystemClock.uptimeMillis();
                r.tracker.setExecuting(false, memFactor, now);
                r.tracker.setForeground(false, memFactor, now);
                r.tracker.setBound(false, memFactor, now);
                r.tracker.setStarted(false, memFactor, now);
            }
        }
        serviceDoneExecutingLocked(r, true, true, enqueueOomAdj, 12);
    }

    private void serviceDoneExecutingLocked(com.android.server.am.ServiceRecord r, boolean inDestroying, boolean finishing, boolean enqueueOomAdj, int oomAdjReason) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
            android.util.Slog.v(TAG_SERVICE, "<<< DONE EXECUTING " + r + ": nesting=" + r.executeNesting + ", inDestroying=" + inDestroying + ", app=" + r.app);
        } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE_EXECUTING) {
            android.util.Slog.v(TAG_SERVICE_EXECUTING, "<<< DONE EXECUTING " + r.shortInstanceName);
        }
        r.executeNesting--;
        if (r.executeNesting <= 0) {
            if (r.app != null) {
                com.android.server.am.ProcessServiceRecord psr = r.app.mServices;
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    android.util.Slog.v(TAG_SERVICE, "Nesting at 0 of " + r.shortInstanceName);
                }
                psr.setExecServicesFg(false);
                psr.stopExecutingService(r);
                if (psr.numberOfExecutingServices() == 0) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE || com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE_EXECUTING) {
                        android.util.Slog.v(TAG_SERVICE_EXECUTING, "No more executingServices of " + r.shortInstanceName);
                    }
                    if (r.app.mPid != 0) {
                        this.mActiveServiceAnrTimer.cancel(r.app);
                    }
                } else if (r.executeFg) {
                    int i = psr.numberOfExecutingServices() - 1;
                    while (true) {
                        if (i < 0) {
                            break;
                        }
                        if (!psr.getExecutingServiceAt(i).executeFg) {
                            i--;
                        } else {
                            psr.setExecServicesFg(true);
                            break;
                        }
                    }
                }
                if (inDestroying) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                        android.util.Slog.v(TAG_SERVICE, "doneExecuting remove destroying " + r);
                    }
                    this.mDestroyingServices.remove(r);
                    r.bindings.clear();
                }
                if (oomAdjReason != 0) {
                    if (enqueueOomAdj) {
                        this.mAm.lambda$appDiedLocked$2(r.app);
                    } else {
                        this.mAm.updateOomAdjLocked(r.app, oomAdjReason);
                    }
                }
                r.updateOomAdjSeq();
            }
            r.executeFg = false;
            if (r.tracker != null) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    int memFactor = this.mAm.mProcessStats.getMemFactorLocked();
                    long now = android.os.SystemClock.uptimeMillis();
                    r.tracker.setExecuting(false, memFactor, now);
                    r.tracker.setForeground(false, memFactor, now);
                    if (finishing) {
                        r.tracker.clearCurrentOwner(r, false);
                        r.tracker = null;
                    }
                }
            }
            if (finishing) {
                if (r.app != null && !r.app.isPersistent()) {
                    stopServiceAndUpdateAllowlistManagerLocked(r);
                }
                r.setProcess(null, null, 0, null);
            }
        }
    }

    boolean attachApplicationLocked(com.android.server.am.ProcessRecord proc, java.lang.String processName) throws java.lang.Throwable {
        long j;
        boolean didSomething = false;
        proc.mState.setBackgroundRestricted(appRestrictedAnyInBackground(proc.uid, proc.info.packageName));
        if (this.mPendingServices.size() > 0) {
            com.android.server.am.ServiceRecord sr = null;
            int i = 0;
            boolean didSomething2 = false;
            while (i < this.mPendingServices.size()) {
                try {
                    com.android.server.am.ServiceRecord sr2 = this.mPendingServices.get(i);
                    try {
                        if (proc == sr2.isolationHostProc || (proc.uid == sr2.appInfo.uid && processName.equals(sr2.processName))) {
                            android.app.IApplicationThread thread = proc.getThread();
                            int pid = proc.getPid();
                            com.android.server.am.UidRecord uidRecord = proc.getUidRecord();
                            this.mPendingServices.remove(i);
                            int i2 = i - 1;
                            proc.addPackage(sr2.appInfo.packageName, sr2.appInfo.longVersionCode, this.mAm.mProcessStats);
                            try {
                                if (android.os.Trace.isTagEnabled(64L)) {
                                    android.os.Trace.traceBegin(64L, "realStartServiceLocked: " + sr2.shortInstanceName);
                                }
                                j = 64;
                                try {
                                    realStartServiceLocked(sr2, proc, thread, pid, uidRecord, sr2.createdFromFg, true, 0);
                                    android.os.Trace.traceEnd(64L);
                                    didSomething2 = true;
                                    if (!isServiceNeededLocked(sr2, false, false)) {
                                        bringDownServiceLocked(sr2, true);
                                    }
                                    this.mAm.updateOomAdjPendingTargetsLocked(6);
                                    i = i2;
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    android.os.Trace.traceEnd(j);
                                    throw th;
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                j = 64;
                            }
                        }
                        i++;
                        sr = sr2;
                    } catch (android.os.RemoteException e) {
                        e = e;
                        sr = sr2;
                        android.util.Slog.w("ActivityManager", "Exception in new application when starting service " + sr.shortInstanceName, e);
                        throw e;
                    }
                } catch (android.os.RemoteException e2) {
                    e = e2;
                }
            }
            didSomething = didSomething2;
        }
        if (this.mRestartingServices.size() > 0) {
            boolean didImmediateRestart = false;
            for (int i3 = 0; i3 < this.mRestartingServices.size(); i3++) {
                com.android.server.am.ServiceRecord sr3 = this.mRestartingServices.get(i3);
                if (proc == sr3.isolationHostProc || (proc.uid == sr3.appInfo.uid && processName.equals(sr3.processName))) {
                    this.mAm.mHandler.removeCallbacks(sr3.restarter);
                    this.mAm.mHandler.post(sr3.restarter);
                    didImmediateRestart = true;
                }
            }
            if (didImmediateRestart) {
                this.mAm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$attachApplicationLocked$2();
                    }
                });
            }
        }
        return didSomething;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$attachApplicationLocked$2() {
        long now = android.os.SystemClock.uptimeMillis();
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                rescheduleServiceRestartIfPossibleLocked(getExtraRestartTimeInBetweenLocked(), this.mAm.mConstants.SERVICE_MIN_RESTART_TIME_BETWEEN, "other", now);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    void processStartTimedOutLocked(com.android.server.am.ProcessRecord proc) throws java.lang.Throwable {
        boolean needOomAdj = false;
        int i = 0;
        int size = this.mPendingServices.size();
        while (i < size) {
            com.android.server.am.ServiceRecord sr = this.mPendingServices.get(i);
            if ((proc.uid == sr.appInfo.uid && proc.processName.equals(sr.processName)) || sr.isolationHostProc == proc) {
                android.util.Slog.w("ActivityManager", "Forcing bringing down service: " + sr);
                sr.isolationHostProc = null;
                this.mPendingServices.remove(i);
                size = this.mPendingServices.size();
                i--;
                needOomAdj = true;
                com.android.server.am.ActiveServices.ServiceMap smap = getServiceMapLocked(sr.userId);
                com.android.server.am.ServiceRecord found = smap.mServicesByInstanceName.remove(sr.instanceName);
                if (found != null && found != sr) {
                    android.util.Slog.i("ActivityManager", "trying to bring down a service record that has been brought down once " + sr);
                } else if (!this.mActiveServicesExt.interceptProcessStartTimedOutBeforeBringDown(getServiceMapLocked(sr.userId), sr)) {
                    bringDownServiceLocked(sr, true);
                }
            }
            i++;
        }
        if (needOomAdj) {
            this.mAm.updateOomAdjPendingTargetsLocked(12);
        }
    }

    private boolean collectPackageServicesLocked(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, boolean evenPersistent, boolean doit, android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> services) {
        boolean didSomething = false;
        for (int i = services.size() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord service = services.valueAt(i);
            boolean sameComponent = packageName == null || (service.packageName.equals(packageName) && (filterByClasses == null || filterByClasses.contains(service.name.getClassName())));
            if (sameComponent && (service.app == null || evenPersistent || !service.app.isPersistent())) {
                if (!doit) {
                    return true;
                }
                didSomething = true;
                android.util.Slog.i("ActivityManager", "  Force stopping service " + service);
                if (service.app != null && !service.app.isPersistent()) {
                    stopServiceAndUpdateAllowlistManagerLocked(service);
                }
                service.setProcess(null, null, 0, null);
                service.isolationHostProc = null;
                if (this.mTmpCollectionResults == null) {
                    this.mTmpCollectionResults = new java.util.ArrayList<>();
                }
                this.mTmpCollectionResults.add(service);
            }
        }
        return didSomething;
    }

    boolean bringDownDisabledPackageServicesLocked(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, int userId, boolean evenPersistent, boolean fullStop, boolean doit) {
        boolean didSomething;
        boolean didSomething2 = false;
        if (this.mTmpCollectionResults != null) {
            this.mTmpCollectionResults.clear();
        }
        if (userId == -1) {
            didSomething = false;
            for (int i = this.mServiceMap.size() - 1; i >= 0; i--) {
                didSomething |= collectPackageServicesLocked(packageName, filterByClasses, evenPersistent, doit, this.mServiceMap.valueAt(i).mServicesByInstanceName);
                if (!doit && didSomething) {
                    return true;
                }
                if (doit && filterByClasses == null) {
                    forceStopPackageLocked(packageName, this.mServiceMap.valueAt(i).mUserId);
                }
            }
        } else {
            com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(userId);
            if (smap != null) {
                android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> items = smap.mServicesByInstanceName;
                didSomething2 = collectPackageServicesLocked(packageName, filterByClasses, evenPersistent, doit, items);
            }
            if (doit && filterByClasses == null) {
                forceStopPackageLocked(packageName, userId);
            }
            didSomething = didSomething2;
        }
        if (this.mTmpCollectionResults != null) {
            int size = this.mTmpCollectionResults.size();
            for (int i2 = size - 1; i2 >= 0; i2--) {
                com.android.server.am.ServiceRecord r = this.mTmpCollectionResults.get(i2);
                com.android.server.am.ServiceRecord found = getServiceMapLocked(r.userId).mServicesByInstanceName.remove(r.instanceName);
                if (found == null || found == r) {
                    if (!this.mActiveServicesExt.interceptBringDownDisabledPackageServicesBeforeBringDown(getServiceMapLocked(this.mTmpCollectionResults.get(i2).userId), this.mTmpCollectionResults.get(i2))) {
                        bringDownServiceLocked(this.mTmpCollectionResults.get(i2), true);
                    }
                } else {
                    android.util.Slog.i("ActivityManager", "trying to bring down a service record that has been brought down once " + r);
                }
            }
            if (size > 0) {
                this.mAm.updateOomAdjPendingTargetsLocked(22);
            }
            if (fullStop && !this.mTmpCollectionResults.isEmpty()) {
                final java.util.ArrayList<com.android.server.am.ServiceRecord> allServices = (java.util.ArrayList) this.mTmpCollectionResults.clone();
                this.mAm.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.am.ActiveServices.lambda$bringDownDisabledPackageServicesLocked$3(allServices);
                    }
                }, 250L);
            }
            java.util.ArrayList<com.android.server.am.ServiceRecord> allServices2 = this.mTmpCollectionResults;
            allServices2.clear();
        }
        return didSomething;
    }

    static /* synthetic */ void lambda$bringDownDisabledPackageServicesLocked$3(java.util.ArrayList allServices) {
        for (int i = 0; i < allServices.size(); i++) {
            ((com.android.server.am.ServiceRecord) allServices.get(i)).cancelNotification();
        }
    }

    private void signalForegroundServiceObserversLocked(com.android.server.am.ServiceRecord r) {
        int num = this.mFgsObservers.beginBroadcast();
        for (int i = 0; i < num; i++) {
            try {
                this.mFgsObservers.getBroadcastItem(i).onForegroundStateChanged(r, r.appInfo.packageName, r.userId, r.isForeground);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mFgsObservers.finishBroadcast();
    }

    boolean registerForegroundServiceObserverLocked(int callingUid, android.app.IForegroundServiceObserver callback) {
        try {
            int mapSize = this.mServiceMap.size();
            for (int mapIndex = 0; mapIndex < mapSize; mapIndex++) {
                com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.valueAt(mapIndex);
                if (smap != null) {
                    int numServices = smap.mServicesByInstanceName.size();
                    for (int i = 0; i < numServices; i++) {
                        com.android.server.am.ServiceRecord sr = smap.mServicesByInstanceName.valueAt(i);
                        if (sr.isForeground && callingUid == sr.appInfo.uid) {
                            callback.onForegroundStateChanged(sr, sr.appInfo.packageName, sr.userId, true);
                        }
                    }
                }
            }
            this.mFgsObservers.register(callback);
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG_SERVICE, "Bad FGS observer from uid " + callingUid);
            return false;
        }
    }

    void forceStopPackageLocked(java.lang.String packageName, int userId) {
        com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(userId);
        if (smap != null && smap.mActiveForegroundApps.size() > 0) {
            for (int i = smap.mActiveForegroundApps.size() - 1; i >= 0; i--) {
                com.android.server.am.ActiveServices.ActiveForegroundApp aa = smap.mActiveForegroundApps.valueAt(i);
                if (aa.mPackageName.equals(packageName)) {
                    smap.mActiveForegroundApps.removeAt(i);
                    smap.mActiveForegroundAppsChanged = true;
                }
            }
            if (smap.mActiveForegroundAppsChanged) {
                requestUpdateActiveForegroundAppsLocked(smap, 0L);
            }
        }
        for (int i2 = this.mPendingBringups.size() - 1; i2 >= 0; i2--) {
            com.android.server.am.ServiceRecord r = this.mPendingBringups.keyAt(i2);
            if (android.text.TextUtils.equals(r.packageName, packageName) && r.userId == userId) {
                this.mPendingBringups.removeAt(i2);
            }
        }
        removeServiceRestartBackoffEnabledLocked(packageName);
        removeServiceNotificationDeferralsLocked(packageName, userId);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void cleanUpServices(int i, android.content.ComponentName componentName, android.content.Intent intent) {
        boolean z;
        java.util.ArrayList arrayList = new java.util.ArrayList();
        android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> servicesLocked = getServicesLocked(i);
        boolean z2 = 1;
        for (int size = servicesLocked.size() - 1; size >= 0; size--) {
            com.android.server.am.ServiceRecord serviceRecordValueAt = servicesLocked.valueAt(size);
            if (serviceRecordValueAt.packageName.equals(componentName.getPackageName())) {
                arrayList.add(serviceRecordValueAt);
            }
        }
        int size2 = arrayList.size() - 1;
        boolean z3 = false;
        while (size2 >= 0) {
            com.android.server.am.ServiceRecord serviceRecord = (com.android.server.am.ServiceRecord) arrayList.get(size2);
            if (!serviceRecord.startRequested) {
                z = z2;
            } else if ((serviceRecord.serviceInfo.flags & z2) != 0) {
                android.util.Slog.i("ActivityManager", "Stopping service " + serviceRecord.shortInstanceName + ": remove task");
                stopServiceLocked(serviceRecord, z2);
                z3 = true;
                z = z2;
            } else {
                serviceRecord.pendingStarts.add(new com.android.server.am.ServiceRecord.StartItem(serviceRecord, true, serviceRecord.getLastStartId(), intent, null, 0, null, null, -1));
                if (serviceRecord.app != null && serviceRecord.app.isThreadReady()) {
                    z = true;
                    z = true;
                    try {
                        sendServiceArgsLocked(serviceRecord, true, false);
                    } catch (android.os.TransactionTooLargeException e) {
                    }
                } else {
                    z = true;
                }
            }
            size2--;
            z2 = z;
            z3 = z3;
        }
        if (z3) {
            this.mAm.updateOomAdjPendingTargetsLocked(17);
        }
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4, types: [android.app.IApplicationThread, com.android.server.am.ProcessRecord, com.android.server.am.UidRecord] */
    /* JADX WARN: Type inference failed for: r0v65, types: [android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r0v66 */
    /* JADX WARN: Type inference failed for: r0v67 */
    /* JADX WARN: Type inference failed for: r0v75 */
    /* JADX WARN: Type inference failed for: r10v25, types: [com.android.server.am.ServiceRecord, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r11v25, types: [com.android.server.am.ProcessServiceRecord] */
    /* JADX WARN: Type inference failed for: r12v16, types: [java.lang.StringBuilder] */
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
    final void killServicesLocked(com.android.server.am.ProcessRecord processRecord, boolean z) {
        ?? r0;
        int i;
        com.android.server.am.ProcessServiceRecord processServiceRecord = processRecord.mServices;
        int iNumberOfConnections = processServiceRecord.numberOfConnections();
        if (iNumberOfConnections > 1000) {
            android.util.Slog.d("ActivityManager", "killServicesLocked app:" + processRecord + ", connection size:" + iNumberOfConnections);
        }
        long jUptimeMillis = android.os.SystemClock.uptimeMillis();
        int iNumberOfConnections2 = processServiceRecord.numberOfConnections() - 1;
        while (true) {
            r0 = 0;
            if (iNumberOfConnections2 < 0) {
                break;
            }
            try {
                removeConnectionLocked(processServiceRecord.getConnectionAt(iNumberOfConnections2), processRecord, null, true);
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                android.util.Slog.e("ActivityManager", "Failed to get connection record!", e);
            }
            iNumberOfConnections2--;
        }
        this.mActiveServicesExt.hookKillServicesWhenRemoveServiceConnection(processRecord, android.os.SystemClock.uptimeMillis() - jUptimeMillis);
        updateServiceConnectionActivitiesLocked(processServiceRecord);
        processServiceRecord.removeAllConnections();
        processServiceRecord.removeAllSdkSandboxConnections();
        boolean z2 = false;
        processServiceRecord.mAllowlistManager = false;
        int iNumberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1;
        while (iNumberOfRunningServices >= 0) {
            ?? runningServiceAt = processServiceRecord.getRunningServiceAt(iNumberOfRunningServices);
            this.mAm.mBatteryStatsService.noteServiceStopLaunch(runningServiceAt.appInfo.uid, runningServiceAt.name.getPackageName(), runningServiceAt.name.getClassName());
            if (runningServiceAt.app != processRecord && runningServiceAt.app != null && !runningServiceAt.app.isPersistent()) {
                runningServiceAt.app.mServices.stopService(runningServiceAt);
                runningServiceAt.app.mServices.updateBoundClientUids();
            }
            runningServiceAt.setProcess(r0, r0, z2 ? 1 : 0, r0);
            runningServiceAt.isolationHostProc = r0;
            runningServiceAt.executeNesting = z2 ? 1 : 0;
            synchronized (this.mAm.mProcessStats.mLock) {
                try {
                    runningServiceAt.forceClearTracker();
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
            if (this.mDestroyingServices.remove((java.lang.Object) runningServiceAt) && com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                android.util.Slog.v(TAG_SERVICE, "killServices remove destroying " + runningServiceAt);
            }
            int size = runningServiceAt.bindings.size() - 1;
            ?? r02 = r0;
            while (size >= 0) {
                com.android.server.am.IntentBindRecord intentBindRecordValueAt = runningServiceAt.bindings.valueAt(size);
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    android.util.Slog.v(TAG_SERVICE, "Killing binding " + intentBindRecordValueAt + ": shouldUnbind=" + intentBindRecordValueAt.hasBound);
                }
                intentBindRecordValueAt.binder = r02;
                intentBindRecordValueAt.hasBound = z2;
                intentBindRecordValueAt.received = z2;
                intentBindRecordValueAt.requested = z2;
                int size2 = intentBindRecordValueAt.apps.size() - 1;
                while (size2 >= 0) {
                    com.android.server.am.ProcessRecord processRecordKeyAt = intentBindRecordValueAt.apps.keyAt(size2);
                    if (processRecordKeyAt.isKilledByAm()) {
                        i = iNumberOfConnections;
                    } else if (processRecordKeyAt.getThread() == null) {
                        i = iNumberOfConnections;
                    } else {
                        com.android.server.am.AppBindRecord appBindRecordValueAt = intentBindRecordValueAt.apps.valueAt(size2);
                        int size3 = appBindRecordValueAt.connections.size() - 1;
                        while (true) {
                            if (size3 < 0) {
                                i = iNumberOfConnections;
                                break;
                            }
                            com.android.server.am.ConnectionRecord connectionRecordValueAt = appBindRecordValueAt.connections.valueAt(size3);
                            i = iNumberOfConnections;
                            if (!connectionRecordValueAt.hasFlag(1) || !connectionRecordValueAt.notHasFlag(48)) {
                                size3--;
                                iNumberOfConnections = i;
                            }
                        }
                    }
                    size2--;
                    iNumberOfConnections = i;
                }
                size--;
                r02 = 0;
                z2 = false;
            }
            iNumberOfRunningServices--;
            r0 = 0;
            z2 = false;
        }
        com.android.server.am.ActiveServices.ServiceMap serviceMapLocked = getServiceMapLocked(processRecord.userId);
        for (int iNumberOfRunningServices2 = processServiceRecord.numberOfRunningServices() - 1; iNumberOfRunningServices2 >= 0; iNumberOfRunningServices2--) {
            com.android.server.am.ServiceRecord runningServiceAt2 = processServiceRecord.getRunningServiceAt(iNumberOfRunningServices2);
            if (!processRecord.isPersistent()) {
                processServiceRecord.stopService(runningServiceAt2);
                processServiceRecord.updateBoundClientUids();
            }
            com.android.server.am.ServiceRecord serviceRecord = serviceMapLocked.mServicesByInstanceName.get(runningServiceAt2.instanceName);
            if (serviceRecord != runningServiceAt2) {
                if (serviceRecord != null) {
                    android.util.Slog.e("ActivityManager", "Service " + runningServiceAt2 + " in process " + processRecord + " not same as in map: " + serviceRecord);
                }
            } else if (z && runningServiceAt2.crashCount >= this.mAm.mConstants.BOUND_SERVICE_MAX_CRASH_RETRY && (runningServiceAt2.serviceInfo.applicationInfo.flags & 8) == 0) {
                android.util.Slog.w("ActivityManager", "Service crashed " + runningServiceAt2.crashCount + " times, stopping: " + runningServiceAt2);
                android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_SERVICE_CRASHED_TOO_MUCH, java.lang.Integer.valueOf(runningServiceAt2.userId), java.lang.Integer.valueOf(runningServiceAt2.crashCount), runningServiceAt2.shortInstanceName, java.lang.Integer.valueOf(runningServiceAt2.app != null ? runningServiceAt2.app.getPid() : -1));
                bringDownServiceLocked(runningServiceAt2, true);
            } else if (!z || !this.mAm.mUserController.isUserRunning(runningServiceAt2.userId, 0)) {
                bringDownServiceLocked(runningServiceAt2, true);
            } else if (!scheduleServiceRestartLocked(runningServiceAt2, true)) {
                bringDownServiceLocked(runningServiceAt2, true);
            } else if (runningServiceAt2.canStopIfKilled(false)) {
                runningServiceAt2.startRequested = false;
                if (runningServiceAt2.tracker != null) {
                    synchronized (this.mAm.mProcessStats.mLock) {
                        runningServiceAt2.tracker.setStarted(false, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                    }
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        this.mAm.updateOomAdjPendingTargetsLocked(19);
        if (!z) {
            processServiceRecord.stopAllServices();
            processServiceRecord.clearBoundClientUids();
            for (int size4 = this.mRestartingServices.size() - 1; size4 >= 0; size4--) {
                com.android.server.am.ServiceRecord serviceRecord2 = this.mRestartingServices.get(size4);
                if (serviceRecord2.processName.equals(processRecord.processName) && serviceRecord2.serviceInfo.applicationInfo.uid == processRecord.info.uid) {
                    this.mRestartingServices.remove(size4);
                    clearRestartingIfNeededLocked(serviceRecord2);
                }
            }
            for (int size5 = this.mPendingServices.size() - 1; size5 >= 0; size5--) {
                com.android.server.am.ServiceRecord serviceRecord3 = this.mPendingServices.get(size5);
                if (serviceRecord3.processName.equals(processRecord.processName) && serviceRecord3.serviceInfo.applicationInfo.uid == processRecord.info.uid) {
                    this.mPendingServices.remove(size5);
                }
            }
            for (int size6 = this.mPendingBringups.size() - 1; size6 >= 0; size6--) {
                com.android.server.am.ServiceRecord serviceRecordKeyAt = this.mPendingBringups.keyAt(size6);
                if (serviceRecordKeyAt.processName.equals(processRecord.processName) && serviceRecordKeyAt.serviceInfo.applicationInfo.uid == processRecord.info.uid) {
                    this.mPendingBringups.removeAt(size6);
                }
            }
        }
        int size7 = this.mDestroyingServices.size();
        while (size7 > 0) {
            int i2 = size7 - 1;
            com.android.server.am.ServiceRecord serviceRecord4 = this.mDestroyingServices.get(i2);
            if (serviceRecord4.app == processRecord) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    serviceRecord4.forceClearTracker();
                }
                this.mDestroyingServices.remove(i2);
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    android.util.Slog.v(TAG_SERVICE, "killServices remove destroying " + serviceRecord4);
                }
            }
            size7 = i2;
        }
        processServiceRecord.stopAllExecutingServices();
        processServiceRecord.noteScheduleServiceTimeoutPending(false);
    }

    android.app.ActivityManager.RunningServiceInfo makeRunningServiceInfoLocked(com.android.server.am.ServiceRecord r) {
        android.app.ActivityManager.RunningServiceInfo info = new android.app.ActivityManager.RunningServiceInfo();
        info.service = r.name;
        if (r.app != null) {
            info.pid = r.app.getPid();
        }
        info.uid = r.appInfo.uid;
        info.process = r.processName;
        info.foreground = r.isForeground;
        info.activeSince = r.createRealTime;
        info.started = r.startRequested;
        info.clientCount = r.getConnections().size();
        info.crashCount = r.crashCount;
        info.lastActivityTime = r.lastActivity;
        if (r.isForeground) {
            info.flags |= 2;
        }
        if (r.startRequested) {
            info.flags |= 1;
        }
        if (r.app != null && r.app.getPid() == com.android.server.am.ActivityManagerService.MY_PID) {
            info.flags |= 4;
        }
        if (r.app != null && r.app.isPersistent()) {
            info.flags |= 8;
        }
        android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections = r.getConnections();
        for (int conni = connections.size() - 1; conni >= 0; conni--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> connl = connections.valueAt(conni);
            for (int i = 0; i < connl.size(); i++) {
                com.android.server.am.ConnectionRecord conn = connl.get(i);
                if (conn.clientLabel != 0) {
                    info.clientPackage = conn.binding.client.info.packageName;
                    info.clientLabel = conn.clientLabel;
                    return info;
                }
            }
        }
        return info;
    }

    java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServiceInfoLocked(int maxNum, int flags, int callingUid, boolean allowed, boolean canInteractAcrossUsers) {
        java.util.ArrayList<android.app.ActivityManager.RunningServiceInfo> res = new java.util.ArrayList<>();
        long ident = this.mAm.mInjector.clearCallingIdentity();
        try {
            if (canInteractAcrossUsers) {
                int[] users = this.mAm.mUserController.getUsers();
                for (int ui = 0; ui < users.length && res.size() < maxNum; ui++) {
                    android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> alls = getServicesLocked(users[ui]);
                    for (int i = 0; i < alls.size() && res.size() < maxNum; i++) {
                        res.add(makeRunningServiceInfoLocked(alls.valueAt(i)));
                    }
                }
                for (int i2 = 0; i2 < this.mRestartingServices.size() && res.size() < maxNum; i2++) {
                    com.android.server.am.ServiceRecord r = this.mRestartingServices.get(i2);
                    android.app.ActivityManager.RunningServiceInfo info = makeRunningServiceInfoLocked(r);
                    info.restarting = r.nextRestartTime;
                    res.add(info);
                }
            } else {
                int userId = android.os.UserHandle.getUserId(callingUid);
                android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> alls2 = getServicesLocked(userId);
                for (int i3 = 0; i3 < alls2.size() && res.size() < maxNum; i3++) {
                    com.android.server.am.ServiceRecord sr = alls2.valueAt(i3);
                    if (allowed || (sr.app != null && sr.app.uid == callingUid)) {
                        res.add(makeRunningServiceInfoLocked(sr));
                    }
                }
                for (int i4 = 0; i4 < this.mRestartingServices.size() && res.size() < maxNum; i4++) {
                    com.android.server.am.ServiceRecord r2 = this.mRestartingServices.get(i4);
                    if (r2.userId == userId && (allowed || (r2.app != null && r2.app.uid == callingUid))) {
                        android.app.ActivityManager.RunningServiceInfo info2 = makeRunningServiceInfoLocked(r2);
                        info2.restarting = r2.nextRestartTime;
                        res.add(info2);
                    }
                }
            }
            return res;
        } finally {
            this.mAm.mInjector.restoreCallingIdentity(ident);
        }
    }

    public android.app.PendingIntent getRunningServiceControlPanelLocked(android.content.ComponentName name) {
        int userId = android.os.UserHandle.getUserId(this.mAm.mInjector.getCallingUid());
        com.android.server.am.ServiceRecord r = getServiceByNameLocked(name, userId);
        if (r != null) {
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections = r.getConnections();
            for (int conni = connections.size() - 1; conni >= 0; conni--) {
                java.util.ArrayList<com.android.server.am.ConnectionRecord> conn = connections.valueAt(conni);
                for (int i = 0; i < conn.size(); i++) {
                    if (conn.get(i).clientIntent != null) {
                        return conn.get(i).clientIntent;
                    }
                }
            }
            return null;
        }
        return null;
    }

    /* JADX WARN: Not initialized variable reg: 16, insn: 0x0141: MOVE (r5 I:??[OBJECT, ARRAY]) = (r16 I:??[OBJECT, ARRAY] A[D('timeoutRecord' com.android.internal.os.TimeoutRecord)]), block:B:58:0x0141 */
    void serviceTimeout(com.android.server.am.ProcessRecord proc) {
        com.android.internal.os.TimeoutRecord timeoutRecord;
        try {
            android.os.Trace.traceBegin(64L, "serviceTimeout()");
            com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            try {
            } catch (java.lang.Throwable th) {
                th = th;
            }
            synchronized (activityManagerService) {
                try {
                    if (proc.isDebugging()) {
                        this.mActiveServiceAnrTimer.discard(proc);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        android.os.Trace.traceEnd(64L);
                        return;
                    }
                    com.android.server.am.ProcessServiceRecord psr = proc.mServices;
                    try {
                        if (psr.numberOfExecutingServices() == 0 || proc.getThread() == null || proc.isKilled()) {
                            this.mActiveServiceAnrTimer.discard(proc);
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        long now = android.os.SystemClock.uptimeMillis();
                        long maxTime = now - (psr.shouldExecServicesFg() ? this.mAm.mConstants.SERVICE_TIMEOUT : this.mAm.mConstants.SERVICE_BACKGROUND_TIMEOUT);
                        com.android.server.am.ServiceRecord timeout = null;
                        long nextTime = 0;
                        int i = psr.numberOfExecutingServices() - 1;
                        while (true) {
                            if (i < 0) {
                                break;
                            }
                            com.android.server.am.ServiceRecord sr = psr.getExecutingServiceAt(i);
                            if (sr.executingStart < maxTime) {
                                timeout = sr;
                                break;
                            } else {
                                if (sr.executingStart > nextTime) {
                                    nextTime = sr.executingStart;
                                }
                                i--;
                            }
                        }
                        if (timeout == null || !this.mAm.mProcessList.isInLruListLOSP(proc)) {
                            this.mActiveServiceAnrTimer.discard(proc);
                            long delay = psr.shouldExecServicesFg() ? this.mAm.mConstants.SERVICE_TIMEOUT + nextTime : (this.mAm.mConstants.SERVICE_BACKGROUND_TIMEOUT + nextTime) - android.os.SystemClock.uptimeMillis();
                            this.mActiveServiceAnrTimer.start(proc, delay);
                            timeoutRecord = null;
                        } else {
                            java.lang.AutoCloseable timer = this.mActiveServiceAnrTimer.accept(proc);
                            android.util.Slog.w("ActivityManager", "Timeout executing service: " + timeout);
                            java.io.StringWriter sw = new java.io.StringWriter();
                            com.android.internal.util.FastPrintWriter fastPrintWriter = new com.android.internal.util.FastPrintWriter(sw, false, 1024);
                            fastPrintWriter.println(timeout);
                            timeout.dump((java.io.PrintWriter) fastPrintWriter, "    ");
                            fastPrintWriter.close();
                            this.mLastAnrDump = sw.toString();
                            this.mAm.mHandler.removeCallbacks(this.mLastAnrDumpClearer);
                            this.mAm.mHandler.postDelayed(this.mLastAnrDumpClearer, com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT);
                            long waitedMillis = now - timeout.executingStart;
                            com.android.internal.os.TimeoutRecord timeoutRecord2 = com.android.internal.os.TimeoutRecord.forServiceExec(timeout.shortInstanceName, waitedMillis).setExpiredTimer(timer);
                            timeoutRecord = timeoutRecord2;
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        if (timeoutRecord != null) {
                            this.mAm.mAnrHelper.appNotResponding(proc, timeoutRecord);
                        }
                        return;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    void serviceForegroundTimeout(com.android.server.am.ServiceRecord r) {
        try {
            android.os.Trace.traceBegin(64L, "serviceForegroundTimeout()");
            java.lang.String annotation = "Context.startForegroundService() did not then call Service.startForeground(): " + r;
            com.android.internal.os.TimeoutRecord timeoutRecord = com.android.internal.os.TimeoutRecord.forServiceStartWithEndTime(annotation, android.os.SystemClock.uptimeMillis());
            timeoutRecord.mLatencyTracker.waitingOnAMSLockStarted();
            com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    timeoutRecord.mLatencyTracker.waitingOnAMSLockEnded();
                    this.mActiveServicesExt.updateExecutingComponent(r.appInfo.uid, "fg-service", 2);
                    if (r.fgRequired && r.fgWaiting && !r.destroying) {
                        com.android.server.am.ProcessRecord app = r.app;
                        if (app != null && app.isDebugging()) {
                            this.mServiceFGAnrTimer.discard(r);
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        this.mServiceFGAnrTimer.accept(r);
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                            android.util.Slog.i("ActivityManager", "Service foreground-required timeout for " + r);
                        }
                        r.fgWaiting = false;
                        stopServiceLocked(r, false);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        if (app != null) {
                            android.os.Message msg = this.mAm.mHandler.obtainMessage(67);
                            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                            args.arg1 = app;
                            args.arg2 = timeoutRecord;
                            msg.obj = args;
                            this.mAm.mHandler.sendMessageDelayed(msg, this.mAm.mConstants.mServiceStartForegroundAnrDelayMs);
                        }
                        return;
                    }
                    this.mServiceFGAnrTimer.discard(r);
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    void serviceForegroundTimeoutANR(com.android.server.am.ProcessRecord app, com.android.internal.os.TimeoutRecord timeoutRecord) {
        this.mAm.mAnrHelper.appNotResponding(app, timeoutRecord);
    }

    public void updateServiceApplicationInfoLocked(android.content.pm.ApplicationInfo applicationInfo) {
        int userId = android.os.UserHandle.getUserId(applicationInfo.uid);
        com.android.server.am.ActiveServices.ServiceMap serviceMap = this.mServiceMap.get(userId);
        if (serviceMap != null) {
            android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> servicesByName = serviceMap.mServicesByInstanceName;
            for (int j = servicesByName.size() - 1; j >= 0; j--) {
                com.android.server.am.ServiceRecord serviceRecord = servicesByName.valueAt(j);
                if (applicationInfo.packageName.equals(serviceRecord.appInfo.packageName)) {
                    serviceRecord.appInfo = applicationInfo;
                    serviceRecord.serviceInfo.applicationInfo = applicationInfo;
                }
            }
        }
    }

    void serviceForegroundCrash(com.android.server.am.ProcessRecord app, java.lang.String serviceRecord, android.content.ComponentName service) {
        this.mAm.crashApplicationWithTypeWithExtras(app.uid, app.getPid(), app.info.packageName, app.userId, "Context.startForegroundService() did not then call Service.startForeground(): " + serviceRecord, false, 1, android.app.RemoteServiceException.ForegroundServiceDidNotStartInTimeException.createExtrasForService(service));
    }

    private static class ProcessAnrTimer extends com.android.server.utils.AnrTimer<com.android.server.am.ProcessRecord> {
        ProcessAnrTimer(com.android.server.am.ActivityManagerService am, int msg, java.lang.String label) {
            super(((com.android.server.am.ActivityManagerService) java.util.Objects.requireNonNull(am)).mHandler, msg, label);
        }

        ProcessAnrTimer(com.android.server.am.ActivityManagerService am, int msg, java.lang.String label, com.android.server.utils.AnrTimer.Args args) {
            super(((com.android.server.am.ActivityManagerService) java.util.Objects.requireNonNull(am)).mHandler, msg, label, args);
        }

        @Override // com.android.server.utils.AnrTimer
        public int getPid(com.android.server.am.ProcessRecord proc) {
            return proc.getPid();
        }

        @Override // com.android.server.utils.AnrTimer
        public int getUid(com.android.server.am.ProcessRecord proc) {
            return proc.uid;
        }
    }

    private static class ServiceAnrTimer extends com.android.server.utils.AnrTimer<com.android.server.am.ServiceRecord> {
        ServiceAnrTimer(com.android.server.am.ActivityManagerService am, int msg, java.lang.String label) {
            super(((com.android.server.am.ActivityManagerService) java.util.Objects.requireNonNull(am)).mHandler, msg, label);
        }

        @Override // com.android.server.utils.AnrTimer
        public int getPid(com.android.server.am.ServiceRecord service) {
            if (service.app != null) {
                return service.app.getPid();
            }
            return 0;
        }

        @Override // com.android.server.utils.AnrTimer
        public int getUid(com.android.server.am.ServiceRecord service) {
            if (service.appInfo != null) {
                return service.appInfo.uid;
            }
            return 0;
        }
    }

    void scheduleServiceTimeoutLocked(com.android.server.am.ProcessRecord proc) {
        if (proc.mServices.numberOfExecutingServices() == 0 || proc.getThread() == null) {
            return;
        }
        long delay = proc.mServices.shouldExecServicesFg() ? this.mAm.mConstants.SERVICE_TIMEOUT : this.mAm.mConstants.SERVICE_BACKGROUND_TIMEOUT;
        this.mActiveServiceAnrTimer.start(proc, delay);
        proc.mServices.noteScheduleServiceTimeoutPending(false);
    }

    void scheduleServiceForegroundTransitionTimeoutLocked(com.android.server.am.ServiceRecord r) {
        if (r.app.mServices.numberOfExecutingServices() == 0 || r.app.getThread() == null) {
            return;
        }
        r.fgWaiting = true;
        this.mServiceFGAnrTimer.start(r, this.mAm.mConstants.mServiceStartForegroundTimeoutMs);
        this.mActiveServicesExt.updateExecutingComponent(r.appInfo.uid, "fg-service", 1);
    }

    final class ServiceDumper {
        private final java.lang.String[] args;
        private final boolean dumpAll;
        private final java.lang.String dumpPackage;
        private final java.io.FileDescriptor fd;
        private final java.io.PrintWriter pw;
        final /* synthetic */ com.android.server.am.ActiveServices this$0;
        private final java.util.ArrayList<com.android.server.am.ServiceRecord> services = new java.util.ArrayList<>();
        private final long nowReal = android.os.SystemClock.elapsedRealtime();
        private boolean needSep = false;
        private boolean printedAnything = false;
        private boolean printed = false;
        private final com.android.server.am.ActivityManagerService.ItemMatcher matcher = new com.android.server.am.ActivityManagerService.ItemMatcher();

        ServiceDumper(com.android.server.am.ActiveServices this$0, java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage) {
            com.android.server.am.ActiveServices activeServices = this$0;
            this.this$0 = activeServices;
            int i = 0;
            this.fd = fd;
            this.pw = pw;
            this.args = args;
            this.dumpAll = dumpAll;
            this.dumpPackage = dumpPackage;
            this.matcher.build(args, opti);
            int[] users = activeServices.mAm.mUserController.getUsers();
            int length = users.length;
            while (i < length) {
                int user = users[i];
                com.android.server.am.ActiveServices.ServiceMap smap = activeServices.getServiceMapLocked(user);
                if (smap.mServicesByInstanceName.size() > 0) {
                    for (int si = 0; si < smap.mServicesByInstanceName.size(); si++) {
                        com.android.server.am.ServiceRecord r = smap.mServicesByInstanceName.valueAt(si);
                        if (this.matcher.match(r, r.name) && (dumpPackage == null || dumpPackage.equals(r.appInfo.packageName))) {
                            this.services.add(r);
                        }
                    }
                }
                i++;
                activeServices = this$0;
            }
        }

        private void dumpHeaderLocked() {
            this.pw.println("ACTIVITY MANAGER SERVICES (dumpsys activity services)");
            if (this.this$0.mLastAnrDump != null) {
                this.pw.println("  Last ANR service:");
                this.pw.print(this.this$0.mLastAnrDump);
                this.pw.println();
            }
        }

        void dumpLocked() {
            dumpHeaderLocked();
            try {
                int[] users = this.this$0.mAm.mUserController.getUsers();
                for (int user : users) {
                    int serviceIdx = 0;
                    while (serviceIdx < this.services.size() && this.services.get(serviceIdx).userId != user) {
                        serviceIdx++;
                    }
                    this.printed = false;
                    if (serviceIdx < this.services.size()) {
                        this.needSep = false;
                        while (serviceIdx < this.services.size()) {
                            com.android.server.am.ServiceRecord r = this.services.get(serviceIdx);
                            serviceIdx++;
                            if (r.userId != user) {
                                break;
                            } else {
                                dumpServiceLocalLocked(r);
                            }
                        }
                        this.needSep |= this.printed;
                    }
                    dumpUserRemainsLocked(user);
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.w("ActivityManager", "Exception in dumpServicesLocked", e);
            }
            dumpRemainsLocked();
        }

        /* JADX WARN: Removed duplicated region for block: B:63:0x00a4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void dumpWithClient() {
            /*
                r8 = this;
                com.android.server.am.ActiveServices r0 = r8.this$0
                com.android.server.am.ActivityManagerService r0 = r0.mAm
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection()
                monitor-enter(r0)
                r8.dumpHeaderLocked()     // Catch: java.lang.Throwable -> Lb2
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb2
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()
                com.android.server.am.ActiveServices r0 = r8.this$0     // Catch: java.lang.Exception -> L94
                com.android.server.am.ActivityManagerService r0 = r0.mAm     // Catch: java.lang.Exception -> L94
                com.android.server.am.UserController r0 = r0.mUserController     // Catch: java.lang.Exception -> L94
                int[] r0 = r0.getUsers()     // Catch: java.lang.Exception -> L94
                int r1 = r0.length     // Catch: java.lang.Exception -> L94
                r2 = 0
                r3 = r2
            L1c:
                if (r3 >= r1) goto L93
                r4 = r0[r3]     // Catch: java.lang.Exception -> L94
                r5 = 0
            L21:
                java.util.ArrayList<com.android.server.am.ServiceRecord> r6 = r8.services     // Catch: java.lang.Exception -> L94
                int r6 = r6.size()     // Catch: java.lang.Exception -> L94
                if (r5 >= r6) goto L38
                java.util.ArrayList<com.android.server.am.ServiceRecord> r6 = r8.services     // Catch: java.lang.Exception -> L94
                java.lang.Object r6 = r6.get(r5)     // Catch: java.lang.Exception -> L94
                com.android.server.am.ServiceRecord r6 = (com.android.server.am.ServiceRecord) r6     // Catch: java.lang.Exception -> L94
                int r6 = r6.userId     // Catch: java.lang.Exception -> L94
                if (r6 == r4) goto L38
                int r5 = r5 + 1
                goto L21
            L38:
                r8.printed = r2     // Catch: java.lang.Exception -> L94
                java.util.ArrayList<com.android.server.am.ServiceRecord> r6 = r8.services     // Catch: java.lang.Exception -> L94
                int r6 = r6.size()     // Catch: java.lang.Exception -> L94
                if (r5 >= r6) goto L7b
                r8.needSep = r2     // Catch: java.lang.Exception -> L94
            L44:
                java.util.ArrayList<com.android.server.am.ServiceRecord> r6 = r8.services     // Catch: java.lang.Exception -> L94
                int r6 = r6.size()     // Catch: java.lang.Exception -> L94
                if (r5 >= r6) goto L74
                java.util.ArrayList<com.android.server.am.ServiceRecord> r6 = r8.services     // Catch: java.lang.Exception -> L94
                java.lang.Object r6 = r6.get(r5)     // Catch: java.lang.Exception -> L94
                com.android.server.am.ServiceRecord r6 = (com.android.server.am.ServiceRecord) r6     // Catch: java.lang.Exception -> L94
                int r5 = r5 + 1
                int r7 = r6.userId     // Catch: java.lang.Exception -> L94
                if (r7 == r4) goto L5b
                goto L74
            L5b:
                com.android.server.am.ActiveServices r7 = r8.this$0     // Catch: java.lang.Exception -> L94
                com.android.server.am.ActivityManagerService r7 = r7.mAm     // Catch: java.lang.Exception -> L94
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection()     // Catch: java.lang.Exception -> L94
                monitor-enter(r7)     // Catch: java.lang.Exception -> L94
                r8.dumpServiceLocalLocked(r6)     // Catch: java.lang.Throwable -> L6e
                monitor-exit(r7)     // Catch: java.lang.Throwable -> L6e
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()     // Catch: java.lang.Exception -> L94
                r8.dumpServiceClient(r6)     // Catch: java.lang.Exception -> L94
                goto L44
            L6e:
                r1 = move-exception
                monitor-exit(r7)     // Catch: java.lang.Throwable -> L6e
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()     // Catch: java.lang.Exception -> L94
                throw r1     // Catch: java.lang.Exception -> L94
            L74:
                boolean r6 = r8.needSep     // Catch: java.lang.Exception -> L94
                boolean r7 = r8.printed     // Catch: java.lang.Exception -> L94
                r6 = r6 | r7
                r8.needSep = r6     // Catch: java.lang.Exception -> L94
            L7b:
                com.android.server.am.ActiveServices r6 = r8.this$0     // Catch: java.lang.Exception -> L94
                com.android.server.am.ActivityManagerService r6 = r6.mAm     // Catch: java.lang.Exception -> L94
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection()     // Catch: java.lang.Exception -> L94
                monitor-enter(r6)     // Catch: java.lang.Exception -> L94
                r8.dumpUserRemainsLocked(r4)     // Catch: java.lang.Throwable -> L8d
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L8d
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()     // Catch: java.lang.Exception -> L94
                int r3 = r3 + 1
                goto L1c
            L8d:
                r1 = move-exception
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L8d
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()     // Catch: java.lang.Exception -> L94
                throw r1     // Catch: java.lang.Exception -> L94
            L93:
                goto L9c
            L94:
                r0 = move-exception
                java.lang.String r1 = "ActivityManager"
                java.lang.String r2 = "Exception in dumpServicesLocked"
                android.util.Slog.w(r1, r2, r0)
            L9c:
                com.android.server.am.ActiveServices r0 = r8.this$0
                com.android.server.am.ActivityManagerService r1 = r0.mAm
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection()
                monitor-enter(r1)
                r8.dumpRemainsLocked()     // Catch: java.lang.Throwable -> Lac
                monitor-exit(r1)     // Catch: java.lang.Throwable -> Lac
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()
                return
            Lac:
                r0 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> Lac
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()
                throw r0
            Lb2:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb2
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection()
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActiveServices.ServiceDumper.dumpWithClient():void");
        }

        private void dumpUserHeaderLocked(int user) {
            if (!this.printed) {
                if (this.printedAnything) {
                    this.pw.println();
                }
                this.pw.println("  User " + user + " active services:");
                this.printed = true;
            }
            this.printedAnything = true;
            if (this.needSep) {
                this.pw.println();
            }
        }

        private void dumpServiceLocalLocked(com.android.server.am.ServiceRecord r) {
            dumpUserHeaderLocked(r.userId);
            this.pw.print("  * ");
            this.pw.println(r);
            if (this.dumpAll) {
                r.dump(this.pw, "    ");
                this.needSep = true;
                return;
            }
            this.pw.print("    app=");
            this.pw.println(r.app);
            this.pw.print("    created=");
            android.util.TimeUtils.formatDuration(r.createRealTime, this.nowReal, this.pw);
            this.pw.print(" started=");
            this.pw.print(r.startRequested);
            this.pw.print(" connections=");
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections = r.getConnections();
            this.pw.println(connections.size());
            if (connections.size() > 0) {
                this.pw.println("    Connections:");
                for (int conni = 0; conni < connections.size(); conni++) {
                    java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = connections.valueAt(conni);
                    for (int i = 0; i < clist.size(); i++) {
                        com.android.server.am.ConnectionRecord conn = clist.get(i);
                        this.pw.print("      ");
                        this.pw.print(conn.binding.intent.intent.getIntent().toShortString(false, false, false, false));
                        this.pw.print(" -> ");
                        com.android.server.am.ProcessRecord proc = conn.binding.client;
                        this.pw.println(proc != null ? proc.toShortString() : "null");
                    }
                }
            }
        }

        private void dumpServiceClient(com.android.server.am.ServiceRecord r) {
            android.app.IApplicationThread thread;
            com.android.server.am.ProcessRecord proc = r.app;
            if (proc == null || (thread = proc.getThread()) == null) {
                return;
            }
            this.pw.println("    Client:");
            this.pw.flush();
            try {
                com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                try {
                    thread.dumpService(tp.getWriteFd(), r, this.args);
                    tp.setBufferPrefix("      ");
                    tp.go(this.fd, 2000L);
                    tp.kill();
                } catch (java.lang.Throwable th) {
                    tp.kill();
                    throw th;
                }
            } catch (android.os.RemoteException e) {
                this.pw.println("      Got a RemoteException while dumping the service");
            } catch (java.io.IOException e2) {
                this.pw.println("      Failure while dumping the service: " + e2);
            }
            this.needSep = true;
        }

        private void dumpUserRemainsLocked(int user) {
            com.android.server.am.ActiveServices.ServiceMap smap = this.this$0.getServiceMapLocked(user);
            this.printed = false;
            int SN = smap.mDelayedStartList.size();
            for (int si = 0; si < SN; si++) {
                com.android.server.am.ServiceRecord r = smap.mDelayedStartList.get(si);
                if (this.matcher.match(r, r.name) && (this.dumpPackage == null || this.dumpPackage.equals(r.appInfo.packageName))) {
                    if (!this.printed) {
                        if (this.printedAnything) {
                            this.pw.println();
                        }
                        this.pw.println("  User " + user + " delayed start services:");
                        this.printed = true;
                    }
                    this.printedAnything = true;
                    this.pw.print("  * Delayed start ");
                    this.pw.println(r);
                }
            }
            this.printed = false;
            int SN2 = smap.mStartingBackground.size();
            for (int si2 = 0; si2 < SN2; si2++) {
                com.android.server.am.ServiceRecord r2 = smap.mStartingBackground.get(si2);
                if (this.matcher.match(r2, r2.name) && (this.dumpPackage == null || this.dumpPackage.equals(r2.appInfo.packageName))) {
                    if (!this.printed) {
                        if (this.printedAnything) {
                            this.pw.println();
                        }
                        this.pw.println("  User " + user + " starting in background:");
                        this.printed = true;
                    }
                    this.printedAnything = true;
                    this.pw.print("  * Starting bg ");
                    this.pw.println(r2);
                }
            }
        }

        private void dumpRemainsLocked() {
            if (this.this$0.mPendingServices.size() > 0) {
                this.printed = false;
                for (int i = 0; i < this.this$0.mPendingServices.size(); i++) {
                    com.android.server.am.ServiceRecord r = this.this$0.mPendingServices.get(i);
                    if (this.matcher.match(r, r.name) && (this.dumpPackage == null || this.dumpPackage.equals(r.appInfo.packageName))) {
                        this.printedAnything = true;
                        if (!this.printed) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.needSep = true;
                            this.pw.println("  Pending services:");
                            this.printed = true;
                        }
                        this.pw.print("  * Pending ");
                        this.pw.println(r);
                        r.dump(this.pw, "    ");
                    }
                }
                this.needSep = true;
            }
            if (this.this$0.mRestartingServices.size() > 0) {
                this.printed = false;
                for (int i2 = 0; i2 < this.this$0.mRestartingServices.size(); i2++) {
                    com.android.server.am.ServiceRecord r2 = this.this$0.mRestartingServices.get(i2);
                    if (this.matcher.match(r2, r2.name) && (this.dumpPackage == null || this.dumpPackage.equals(r2.appInfo.packageName))) {
                        this.printedAnything = true;
                        if (!this.printed) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.needSep = true;
                            this.pw.println("  Restarting services:");
                            this.printed = true;
                        }
                        this.pw.print("  * Restarting ");
                        this.pw.println(r2);
                        r2.dump(this.pw, "    ");
                    }
                }
                this.needSep = true;
            }
            if (this.this$0.mDestroyingServices.size() > 0) {
                this.printed = false;
                for (int i3 = 0; i3 < this.this$0.mDestroyingServices.size(); i3++) {
                    com.android.server.am.ServiceRecord r3 = this.this$0.mDestroyingServices.get(i3);
                    if (this.matcher.match(r3, r3.name) && (this.dumpPackage == null || this.dumpPackage.equals(r3.appInfo.packageName))) {
                        this.printedAnything = true;
                        if (!this.printed) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.needSep = true;
                            this.pw.println("  Destroying services:");
                            this.printed = true;
                        }
                        this.pw.print("  * Destroy ");
                        this.pw.println(r3);
                        r3.dump(this.pw, "    ");
                    }
                }
                this.needSep = true;
            }
            if (this.dumpAll) {
                this.printed = false;
                for (int ic = 0; ic < this.this$0.mServiceConnections.size(); ic++) {
                    java.util.ArrayList<com.android.server.am.ConnectionRecord> r4 = this.this$0.mServiceConnections.valueAt(ic);
                    for (int i4 = 0; i4 < r4.size(); i4++) {
                        com.android.server.am.ConnectionRecord cr = r4.get(i4);
                        if (this.matcher.match(cr.binding.service, cr.binding.service.name) && (this.dumpPackage == null || (cr.binding.client != null && this.dumpPackage.equals(cr.binding.client.info.packageName)))) {
                            this.printedAnything = true;
                            if (!this.printed) {
                                if (this.needSep) {
                                    this.pw.println();
                                }
                                this.needSep = true;
                                this.pw.println("  Connection bindings to services:");
                                this.printed = true;
                            }
                            this.pw.print("  * ");
                            this.pw.println(cr);
                            cr.dump(this.pw, "    ");
                        }
                    }
                }
            }
            if (this.matcher.all) {
                long nowElapsed = android.os.SystemClock.elapsedRealtime();
                int[] users = this.this$0.mAm.mUserController.getUsers();
                for (int user : users) {
                    boolean printedUser = false;
                    com.android.server.am.ActiveServices.ServiceMap smap = this.this$0.mServiceMap.get(user);
                    if (smap != null) {
                        for (int i5 = smap.mActiveForegroundApps.size() - 1; i5 >= 0; i5--) {
                            com.android.server.am.ActiveServices.ActiveForegroundApp aa = smap.mActiveForegroundApps.valueAt(i5);
                            if (this.dumpPackage == null || this.dumpPackage.equals(aa.mPackageName)) {
                                if (!printedUser) {
                                    printedUser = true;
                                    this.printedAnything = true;
                                    if (this.needSep) {
                                        this.pw.println();
                                    }
                                    this.needSep = true;
                                    this.pw.print("Active foreground apps - user ");
                                    this.pw.print(user);
                                    this.pw.println(":");
                                }
                                this.pw.print("  #");
                                this.pw.print(i5);
                                this.pw.print(": ");
                                this.pw.println(aa.mPackageName);
                                if (aa.mLabel != null) {
                                    this.pw.print("    mLabel=");
                                    this.pw.println(aa.mLabel);
                                }
                                this.pw.print("    mNumActive=");
                                this.pw.print(aa.mNumActive);
                                this.pw.print(" mAppOnTop=");
                                this.pw.print(aa.mAppOnTop);
                                this.pw.print(" mShownWhileTop=");
                                this.pw.print(aa.mShownWhileTop);
                                this.pw.print(" mShownWhileScreenOn=");
                                this.pw.println(aa.mShownWhileScreenOn);
                                this.pw.print("    mStartTime=");
                                android.util.TimeUtils.formatDuration(aa.mStartTime - nowElapsed, this.pw);
                                this.pw.print(" mStartVisibleTime=");
                                android.util.TimeUtils.formatDuration(aa.mStartVisibleTime - nowElapsed, this.pw);
                                this.pw.println();
                                if (aa.mEndTime != 0) {
                                    this.pw.print("    mEndTime=");
                                    android.util.TimeUtils.formatDuration(aa.mEndTime - nowElapsed, this.pw);
                                    this.pw.println();
                                }
                            }
                        }
                        if (smap.hasMessagesOrCallbacks()) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.printedAnything = true;
                            this.needSep = true;
                            this.pw.print("  Handler - user ");
                            this.pw.print(user);
                            this.pw.println(":");
                            smap.dumpMine(new android.util.PrintWriterPrinter(this.pw), "    ");
                        }
                    }
                }
            }
            if (!this.printedAnything) {
                this.pw.println("  (nothing)");
            }
        }
    }

    com.android.server.am.ActiveServices.ServiceDumper newServiceDumperLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage) {
        return new com.android.server.am.ActiveServices.ServiceDumper(this, fd, pw, args, opti, dumpAll, dumpPackage);
    }

    protected void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        int[] users;
        com.android.server.am.ActiveServices activeServices = this;
        com.android.server.am.ActivityManagerService activityManagerService = activeServices.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                long outterToken = proto.start(fieldId);
                int[] users2 = activeServices.mAm.mUserController.getUsers();
                int length = users2.length;
                int i = 0;
                while (i < length) {
                    int user = users2[i];
                    com.android.server.am.ActiveServices.ServiceMap smap = activeServices.mServiceMap.get(user);
                    if (smap == null) {
                        users = users2;
                    } else {
                        long token = proto.start(2246267895809L);
                        proto.write(1120986464257L, user);
                        android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> alls = smap.mServicesByInstanceName;
                        int i2 = 0;
                        while (i2 < alls.size()) {
                            alls.valueAt(i2).dumpDebug(proto, 2246267895810L);
                            i2++;
                            users2 = users2;
                        }
                        users = users2;
                        proto.end(token);
                    }
                    i++;
                    activeServices = this;
                    users2 = users;
                }
                proto.end(outterToken);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    protected boolean dumpService(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String name, int[] users, java.lang.String[] args, int opti, boolean dumpAll) throws java.lang.Throwable {
        int[] users2;
        try {
            this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
            java.util.ArrayList<com.android.server.am.ServiceRecord> services = new java.util.ArrayList<>();
            java.util.function.Predicate<com.android.server.am.ServiceRecord> filter = com.android.internal.util.DumpUtils.filterRecord(name);
            com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    try {
                        if (users != null) {
                            users2 = users;
                        } else {
                            try {
                                users2 = this.mAm.mUserController.getUsers();
                            } catch (java.lang.Throwable th) {
                                th = th;
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                throw th;
                            }
                        }
                        for (int user : users2) {
                            com.android.server.am.ActiveServices.ServiceMap smap = this.mServiceMap.get(user);
                            if (smap != null) {
                                android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> alls = smap.mServicesByInstanceName;
                                for (int i = 0; i < alls.size(); i++) {
                                    com.android.server.am.ServiceRecord r1 = alls.valueAt(i);
                                    if (filter.test(r1)) {
                                        services.add(r1);
                                    }
                                }
                            }
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        if (services.size() > 0) {
                            services.sort(java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda1
                                @Override // java.util.function.Function
                                public final java.lang.Object apply(java.lang.Object obj) {
                                    return ((com.android.server.am.ServiceRecord) obj).getComponentName();
                                }
                            }));
                            boolean needSep = false;
                            for (int i2 = 0; i2 < services.size(); i2++) {
                                if (needSep) {
                                    pw.println();
                                }
                                needSep = true;
                                dumpService("", fd, pw, services.get(i2), args, dumpAll);
                            }
                            this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
                            return true;
                        }
                        this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
                        return false;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
                    throw th;
                }
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    private void dumpService(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter pw, com.android.server.am.ServiceRecord r, java.lang.String[] args, boolean dumpAll) {
        android.app.IApplicationThread thread;
        java.lang.String innerPrefix = prefix + "  ";
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                pw.print(prefix);
                pw.print("SERVICE ");
                pw.print(r.shortInstanceName);
                pw.print(" ");
                pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(r)));
                pw.print(" pid=");
                if (r.app != null) {
                    pw.print(r.app.getPid());
                    pw.print(" user=");
                    pw.println(r.userId);
                } else {
                    pw.println("(not running)");
                }
                if (dumpAll) {
                    r.dump(pw, innerPrefix);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        if (r.app != null && (thread = r.app.getThread()) != null) {
            pw.print(prefix);
            pw.println("  Client:");
            pw.flush();
            try {
                com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                try {
                    thread.dumpService(tp.getWriteFd(), r, args);
                    tp.setBufferPrefix(prefix + "    ");
                    tp.go(fd);
                    tp.kill();
                } catch (java.lang.Throwable th2) {
                    tp.kill();
                    throw th2;
                }
            } catch (android.os.RemoteException e) {
                pw.println(prefix + "    Got a RemoteException while dumping the service");
            } catch (java.io.IOException e2) {
                pw.println(prefix + "    Failure while dumping the service: " + e2);
            }
        }
    }

    private void setFgsRestrictionLocked(java.lang.String callingPackage, int callingPid, int callingUid, android.content.Intent intent, com.android.server.am.ServiceRecord r, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges, boolean isBindService) throws android.content.pm.PackageManager.NameNotFoundException {
        setFgsRestrictionLocked(callingPackage, callingPid, callingUid, intent, r, userId, backgroundStartPrivileges, isBindService, false);
    }

    private void setFgsRestrictionLocked(java.lang.String callingPackage, int callingPid, int callingUid, android.content.Intent intent, com.android.server.am.ServiceRecord r, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges, boolean inBindService, boolean forBoundFgs) throws android.content.pm.PackageManager.NameNotFoundException {
        int allowWiu;
        int allowStart;
        int allowWiu2;
        if (inBindService) {
            allowWiu = r.mAllowWiu_inBindService;
            allowStart = r.mAllowStart_inBindService;
        } else {
            allowWiu = r.mAllowWiu_noBinding;
            allowStart = r.mAllowStart_noBinding;
        }
        if (allowWiu == -1 || allowStart == -1) {
            int allowWhileInUse = shouldAllowFgsWhileInUsePermissionLocked(callingPackage, callingPid, callingUid, r.app, backgroundStartPrivileges);
            if (allowWiu != -1) {
                allowWiu2 = allowWiu;
            } else {
                allowWiu2 = allowWhileInUse;
            }
            if (allowStart != -1) {
                allowWiu = allowWiu2;
            } else {
                allowStart = shouldAllowFgsStartForegroundWithBindingCheckLocked(allowWhileInUse, callingPackage, callingPid, callingUid, intent, r, backgroundStartPrivileges, inBindService);
                allowWiu = allowWiu2;
            }
        }
        if (inBindService) {
            r.mAllowWiu_inBindService = allowWiu;
            r.mAllowStart_inBindService = allowStart;
            return;
        }
        if (forBoundFgs) {
            if (r.mAllowWiu_byBindings == -1) {
                r.mAllowWiu_byBindings = allowWiu;
            }
            if (r.mAllowStart_byBindings == -1) {
                r.mAllowStart_byBindings = allowStart;
            }
        } else {
            r.mAllowWiu_noBinding = allowWiu;
            r.mAllowStart_noBinding = allowStart;
        }
        if (r.mAllowWiu_byBindings == -1) {
            r.mAllowWiu_byBindings = shouldAllowFgsWhileInUsePermissionByBindingsLocked(callingUid);
        }
        if (r.mAllowStart_byBindings == -1) {
            r.mAllowStart_byBindings = r.mAllowWiu_byBindings;
        }
    }

    void resetFgsRestrictionLocked(com.android.server.am.ServiceRecord r) {
        r.clearFgsAllowWiu();
        r.clearFgsAllowStart();
        r.mInfoAllowStartForeground = null;
        r.mInfoTempFgsAllowListReason = null;
        r.mLoggedInfoAllowStartForeground = false;
        r.updateAllowUiJobScheduling(r.isFgsAllowedWiu_forStart());
    }

    boolean canStartForegroundServiceLocked(int callingPid, int callingUid, java.lang.String callingPackage) {
        if (!this.mAm.mConstants.mFlagBackgroundFgsStartRestrictionEnabled) {
            return true;
        }
        int allowWhileInUse = shouldAllowFgsWhileInUsePermissionLocked(callingPackage, callingPid, callingUid, null, android.app.BackgroundStartPrivileges.NONE);
        int allowStartFgs = shouldAllowFgsStartForegroundNoBindingCheckLocked(allowWhileInUse, callingPid, callingUid, callingPackage, null, android.app.BackgroundStartPrivileges.NONE);
        if (allowStartFgs == -1 && canBindingClientStartFgsLocked(callingUid) != null) {
            allowStartFgs = 54;
        }
        return allowStartFgs != -1;
    }

    int shouldAllowFgsWhileInUsePermissionLocked(java.lang.String callingPackage, int callingPid, final int callingUid, com.android.server.am.ProcessRecord targetProcess, android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        com.android.server.am.ActiveInstrumentation instr;
        java.lang.Integer allowedType;
        boolean isCallerSystem;
        int ret = -1;
        int uidState = this.mAm.getUidStateLocked(callingUid);
        if (-1 == -1 && uidState <= 2) {
            ret = android.os.PowerExemptionManager.getReasonCodeFromProcState(uidState);
        }
        if (ret == -1) {
            boolean isCallingUidVisible = this.mAm.mAtmInternal.isUidForeground(callingUid);
            if (isCallingUidVisible) {
                ret = 50;
            }
        }
        if (ret == -1 && backgroundStartPrivileges.allowsBackgroundActivityStarts()) {
            ret = 53;
        }
        if (ret == -1) {
            int callingAppId = android.os.UserHandle.getAppId(callingUid);
            switch (callingAppId) {
                case 0:
                case 1000:
                case 1027:
                case 2000:
                    isCallerSystem = true;
                    break;
                default:
                    isCallerSystem = false;
                    break;
            }
            if (isCallerSystem) {
                ret = 51;
            }
        }
        if (ret == -1 && (allowedType = (java.lang.Integer) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new java.util.function.Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.am.ActiveServices.lambda$shouldAllowFgsWhileInUsePermissionLocked$4(callingUid, (com.android.server.am.ProcessRecord) obj);
            }
        })) != null) {
            ret = allowedType.intValue();
        }
        if (ret == -1 && this.mAm.mInternal.isTempAllowlistedForFgsWhileInUse(callingUid)) {
            return 70;
        }
        if (ret == -1 && targetProcess != null && (instr = targetProcess.getActiveInstrumentation()) != null && instr.mHasBackgroundActivityStartsPermission) {
            ret = 60;
        }
        if (ret == -1 && this.mAm.checkPermission("android.permission.START_ACTIVITIES_FROM_BACKGROUND", callingPid, callingUid) == 0) {
            ret = 58;
        }
        if (ret == -1) {
            if (verifyPackage(callingPackage, callingUid)) {
                boolean isAllowedPackage = this.mAllowListWhileInUsePermissionInFgs.contains(callingPackage);
                if (isAllowedPackage) {
                    ret = 65;
                }
            } else {
                android.util.EventLog.writeEvent(1397638484, "215003903", java.lang.Integer.valueOf(callingUid), "callingPackage:" + callingPackage + " does not belong to callingUid:" + callingUid);
            }
        }
        if (ret == -1) {
            boolean isDeviceOwner = this.mAm.mInternal.isDeviceOwner(callingUid);
            if (isDeviceOwner) {
                return 55;
            }
            return ret;
        }
        return ret;
    }

    static /* synthetic */ java.lang.Integer lambda$shouldAllowFgsWhileInUsePermissionLocked$4(int callingUid, com.android.server.am.ProcessRecord pr) {
        if (pr.uid == callingUid && pr.getWindowProcessController().areBackgroundFgsStartsAllowed()) {
            return 52;
        }
        return null;
    }

    private int shouldAllowFgsWhileInUsePermissionByBindingsLocked(final int callingUid) {
        final android.util.ArraySet<java.lang.Integer> checkedClientUids = new android.util.ArraySet<>();
        java.lang.Integer result = (java.lang.Integer) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new java.util.function.Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$shouldAllowFgsWhileInUsePermissionByBindingsLocked$5(callingUid, checkedClientUids, (com.android.server.am.ProcessRecord) obj);
            }
        });
        if (result == null) {
            return -1;
        }
        return result.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$shouldAllowFgsWhileInUsePermissionByBindingsLocked$5(int callingUid, android.util.ArraySet checkedClientUids, com.android.server.am.ProcessRecord pr) {
        int i = callingUid;
        if (pr.uid != i) {
            return null;
        }
        com.android.server.am.ProcessServiceRecord psr = pr.mServices;
        int serviceCount = psr.mServices.size();
        int svc = 0;
        while (svc < serviceCount) {
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> conns = psr.mServices.valueAt(svc).getConnections();
            int size = conns.size();
            int conni = 0;
            while (conni < size) {
                java.util.ArrayList<com.android.server.am.ConnectionRecord> crs = conns.valueAt(conni);
                int con = 0;
                while (con < crs.size()) {
                    com.android.server.am.ConnectionRecord cr = crs.get(con);
                    com.android.server.am.ProcessRecord clientPr = cr.binding.client;
                    int clientUid = clientPr.uid;
                    if (clientUid != i && !checkedClientUids.contains(java.lang.Integer.valueOf(clientUid))) {
                        int clientUidState = this.mAm.getUidStateLocked(i);
                        boolean z = false;
                        boolean boundByTop = clientUidState == 2;
                        if (clientUidState < 2 && cr.hasFlag(1048576)) {
                            z = true;
                        }
                        boolean boundByPersistentWithBal = z;
                        if (!boundByTop && !boundByPersistentWithBal) {
                            checkedClientUids.add(java.lang.Integer.valueOf(clientUid));
                        }
                        return java.lang.Integer.valueOf(android.os.PowerExemptionManager.getReasonCodeFromProcState(clientUidState));
                    }
                    con++;
                    i = callingUid;
                }
                conni++;
                i = callingUid;
            }
            svc++;
            i = callingUid;
        }
        return null;
    }

    private java.lang.String canBindingClientStartFgsLocked(final int uid) {
        final android.util.ArraySet<java.lang.Integer> checkedClientUids = new android.util.ArraySet<>();
        android.util.Pair<java.lang.Integer, java.lang.String> isAllowed = (android.util.Pair) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new java.util.function.Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$canBindingClientStartFgsLocked$6(uid, checkedClientUids, (com.android.server.am.ProcessRecord) obj);
            }
        });
        if (isAllowed == null) {
            return null;
        }
        java.lang.String bindFromPackage = (java.lang.String) isAllowed.second;
        return bindFromPackage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.util.Pair lambda$canBindingClientStartFgsLocked$6(int uid, android.util.ArraySet checkedClientUids, com.android.server.am.ProcessRecord pr) {
        int i = uid;
        if (pr.uid == i) {
            com.android.server.am.ProcessServiceRecord psr = pr.mServices;
            int serviceCount = psr.mServices.size();
            int svc = 0;
            while (svc < serviceCount) {
                android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> conns = psr.mServices.valueAt(svc).getConnections();
                int size = conns.size();
                int conni = 0;
                while (conni < size) {
                    java.util.ArrayList<com.android.server.am.ConnectionRecord> crs = conns.valueAt(conni);
                    int con = 0;
                    while (con < crs.size()) {
                        com.android.server.am.ConnectionRecord cr = crs.get(con);
                        com.android.server.am.ProcessRecord clientPr = cr.binding.client;
                        if (!clientPr.isPersistent()) {
                            int clientPid = clientPr.mPid;
                            int clientUid = clientPr.uid;
                            if (clientUid != i && !checkedClientUids.contains(java.lang.Integer.valueOf(clientUid))) {
                                java.lang.String clientPackageName = cr.clientPackageName;
                                int allowWhileInUse2 = shouldAllowFgsWhileInUsePermissionLocked(clientPackageName, clientPid, clientUid, null, android.app.BackgroundStartPrivileges.NONE);
                                int allowStartFgs = shouldAllowFgsStartForegroundNoBindingCheckLocked(allowWhileInUse2, clientPid, clientUid, clientPackageName, null, android.app.BackgroundStartPrivileges.NONE);
                                if (allowStartFgs != -1) {
                                    return new android.util.Pair(java.lang.Integer.valueOf(allowStartFgs), clientPackageName);
                                }
                                checkedClientUids.add(java.lang.Integer.valueOf(clientUid));
                            }
                        }
                        con++;
                        i = uid;
                    }
                    conni++;
                    i = uid;
                }
                svc++;
                i = uid;
            }
            return null;
        }
        return null;
    }

    private int shouldAllowFgsStartForegroundWithBindingCheckLocked(int allowWhileInUse, java.lang.String callingPackage, int callingPid, int callingUid, android.content.Intent intent, com.android.server.am.ServiceRecord r, android.app.BackgroundStartPrivileges backgroundStartPrivileges, boolean isBindService) throws android.content.pm.PackageManager.NameNotFoundException {
        java.lang.String bindFromPackage;
        int ret;
        com.android.server.am.ActivityManagerService.FgsTempAllowListItem tempAllowListReason = this.mAm.isAllowlistedForFgsStartLOSP(callingUid);
        r.mInfoTempFgsAllowListReason = tempAllowListReason;
        int ret2 = shouldAllowFgsStartForegroundNoBindingCheckLocked(allowWhileInUse, callingPid, callingUid, callingPackage, r, backgroundStartPrivileges);
        if (ret2 != -1) {
            bindFromPackage = null;
            ret = ret2;
        } else {
            java.lang.String bindFromPackage2 = canBindingClientStartFgsLocked(callingUid);
            if (bindFromPackage2 == null) {
                bindFromPackage = bindFromPackage2;
                ret = ret2;
            } else {
                bindFromPackage = bindFromPackage2;
                ret = 54;
            }
        }
        int uidState = this.mAm.getUidStateLocked(callingUid);
        int callerTargetSdkVersion = -1;
        try {
            callerTargetSdkVersion = this.mAm.mContext.getPackageManager().getTargetSdkVersion(callingPackage);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        boolean uidBfsl = (this.mAm.getUidProcessCapabilityLocked(callingUid) & 16) != 0;
        java.lang.String debugInfo = "[callingPackage: " + callingPackage + "; callingUid: " + callingUid + "; uidState: " + com.android.server.am.ProcessList.makeProcStateString(uidState) + "; uidBFSL: " + (uidBfsl ? "[BFSL]" : "n/a") + "; intent: " + intent + "; code:" + android.os.PowerExemptionManager.reasonCodeToString(ret) + "; tempAllowListReason:<" + (tempAllowListReason == null ? null : tempAllowListReason.mReason + ",reasonCode:" + android.os.PowerExemptionManager.reasonCodeToString(tempAllowListReason.mReasonCode) + ",duration:" + tempAllowListReason.mDuration + ",callingUid:" + tempAllowListReason.mCallingUid) + ">; targetSdkVersion:" + r.appInfo.targetSdkVersion + "; callerTargetSdkVersion:" + callerTargetSdkVersion + "; startForegroundCount:" + r.mStartForegroundCount + "; bindFromPackage:" + bindFromPackage + ": isBindService:" + isBindService + "]";
        if (!debugInfo.equals(r.mInfoAllowStartForeground)) {
            r.mLoggedInfoAllowStartForeground = false;
            r.mInfoAllowStartForeground = debugInfo;
        }
        return ret;
    }

    private int shouldAllowFgsStartForegroundNoBindingCheckLocked(int allowWhileInUse, int callingPid, final int callingUid, java.lang.String callingPackage, com.android.server.am.ServiceRecord targetService, android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        java.lang.String inputMethod;
        android.content.ComponentName cn;
        com.android.server.am.ActivityManagerService.FgsTempAllowListItem item;
        int uidState;
        int ret = allowWhileInUse;
        if (ret == -1 && (uidState = this.mAm.getUidStateLocked(callingUid)) <= 2) {
            ret = android.os.PowerExemptionManager.getReasonCodeFromProcState(uidState);
        }
        if (ret == -1) {
            final boolean uidBfsl = (this.mAm.getUidProcessCapabilityLocked(callingUid) & 16) != 0;
            java.lang.Integer allowedType = (java.lang.Integer) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new java.util.function.Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda6
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$shouldAllowFgsStartForegroundNoBindingCheckLocked$7(callingUid, uidBfsl, (com.android.server.am.ProcessRecord) obj);
                }
            });
            if (allowedType != null) {
                ret = allowedType.intValue();
            }
        }
        if (ret == -1 && this.mAm.checkPermission("android.permission.START_FOREGROUND_SERVICES_FROM_BACKGROUND", callingPid, callingUid) == 0) {
            ret = 59;
        }
        if (ret == -1 && backgroundStartPrivileges.allowsBackgroundFgsStarts()) {
            ret = 53;
        }
        if (ret == -1 && this.mAm.mAtmInternal.hasSystemAlertWindowPermission(callingUid, callingPid, callingPackage)) {
            if (com.android.server.am.Flags.fgsDisableSaw() && android.app.compat.CompatChanges.isChangeEnabled(FGS_SAW_RESTRICTIONS, callingUid)) {
                com.android.server.am.UidRecord uidRecord = this.mAm.mProcessList.getUidRecordLOSP(callingUid);
                if (uidRecord != null) {
                    int i = uidRecord.getNumOfProcs() - 1;
                    while (true) {
                        if (i >= 0) {
                            com.android.server.am.ProcessRecord pr = uidRecord.getProcessRecordByIndex(i);
                            if (pr == null || !pr.mState.hasOverlayUi()) {
                                i--;
                            } else {
                                ret = 62;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            } else {
                ret = 62;
            }
        }
        if (ret == -1) {
            boolean isCompanionApp = this.mAm.mInternal.isAssociatedCompanionApp(android.os.UserHandle.getUserId(callingUid), callingUid);
            if (isCompanionApp && (isPermissionGranted("android.permission.REQUEST_COMPANION_START_FOREGROUND_SERVICES_FROM_BACKGROUND", callingPid, callingUid) || isPermissionGranted("android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND", callingPid, callingUid))) {
                ret = 57;
            }
        }
        if (ret == -1 && (item = this.mAm.isAllowlistedForFgsStartLOSP(callingUid)) != null) {
            if (item == com.android.server.am.ActivityManagerService.FAKE_TEMP_ALLOW_LIST_ITEM) {
                ret = 300;
            } else {
                ret = item.mReasonCode;
            }
        }
        if (ret == -1 && android.os.UserManager.isDeviceInDemoMode(this.mAm.mContext)) {
            ret = 63;
        }
        if (ret == -1) {
            boolean isProfileOwner = this.mAm.mInternal.isProfileOwner(callingUid);
            if (isProfileOwner) {
                ret = 56;
            }
        }
        if (ret == -1) {
            android.app.AppOpsManager appOpsManager = this.mAm.getAppOpsManager();
            if (this.mAm.mConstants.mFlagSystemExemptPowerRestrictionsEnabled && appOpsManager.checkOpNoThrow(128, callingUid, callingPackage) == 0) {
                ret = 327;
            }
        }
        if (ret == -1) {
            android.app.AppOpsManager appOpsManager2 = this.mAm.getAppOpsManager();
            if (appOpsManager2.checkOpNoThrow(47, callingUid, callingPackage) == 0) {
                ret = 68;
            } else if (appOpsManager2.checkOpNoThrow(94, callingUid, callingPackage) == 0) {
                ret = 69;
            }
        }
        if (ret == -1 && (inputMethod = android.provider.Settings.Secure.getStringForUser(this.mAm.mContext.getContentResolver(), "default_input_method", android.os.UserHandle.getUserId(callingUid))) != null && (cn = android.content.ComponentName.unflattenFromString(inputMethod)) != null && cn.getPackageName().equals(callingPackage)) {
            ret = 71;
        }
        if (ret == -1 && this.mAm.mConstants.mFgsAllowOptOut && targetService != null && targetService.appInfo.hasRequestForegroundServiceExemption()) {
            return 1000;
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$shouldAllowFgsStartForegroundNoBindingCheckLocked$7(int callingUid, boolean uidBfsl, com.android.server.am.ProcessRecord app) {
        if (app.uid == callingUid) {
            com.android.server.am.ProcessStateRecord state = app.mState;
            int procstate = state.getCurProcState();
            if (procstate <= 3 || (uidBfsl && procstate <= 5)) {
                return java.lang.Integer.valueOf(android.os.PowerExemptionManager.getReasonCodeFromProcState(procstate));
            }
            com.android.server.am.ActiveInstrumentation instr = app.getActiveInstrumentation();
            if (instr != null && instr.mHasBackgroundForegroundServiceStartsPermission) {
                return 61;
            }
            long lastInvisibleTime = app.mState.getLastInvisibleTime();
            if (lastInvisibleTime > 0 && lastInvisibleTime < Long.MAX_VALUE) {
                long sinceLastInvisible = android.os.SystemClock.elapsedRealtime() - lastInvisibleTime;
                if (sinceLastInvisible < this.mAm.mConstants.mFgToBgFgsGraceDuration) {
                    return 67;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private boolean isPermissionGranted(java.lang.String permission, int callingPid, int callingUid) {
        return this.mAm.checkPermission(permission, callingPid, callingUid) == 0;
    }

    private static boolean isFgsBgStart(int code) {
        return (code == 10 || code == 11 || code == 12 || code == 50) ? false : true;
    }

    private void showFgsBgRestrictedNotificationLocked(com.android.server.am.ServiceRecord r) {
        if (!this.mAm.mConstants.mFgsStartRestrictionNotificationEnabled) {
            return;
        }
        android.content.Context context = this.mAm.mContext;
        java.lang.String content = "App restricted: " + r.mRecentCallingPackage;
        long now = java.lang.System.currentTimeMillis();
        java.lang.String bigText = DATE_FORMATTER.format(java.lang.Long.valueOf(now)) + " " + r.mInfoAllowStartForeground;
        android.app.Notification.Builder n = new android.app.Notification.Builder(context, com.android.internal.notification.SystemNotificationChannels.ALERTS).setGroup("com.android.fgs-bg-restricted").setSmallIcon(android.R.drawable.spinner_pressed_holo_dark).setWhen(0L).setColor(context.getColor(android.R.color.system_notification_accent_color)).setTicker("Foreground Service BG-Launch Restricted").setContentTitle("Foreground Service BG-Launch Restricted").setContentText(content).setStyle(new android.app.Notification.BigTextStyle().bigText(bigText));
        ((android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class)).notifyAsUser(java.lang.Long.toString(now), 61, n.build(), android.os.UserHandle.ALL);
    }

    private boolean isBgFgsRestrictionEnabled(com.android.server.am.ServiceRecord r, int actualCallingUid) {
        int callingUid;
        if (!this.mAm.mConstants.mFlagFgsStartRestrictionEnabled || !android.app.compat.CompatChanges.isChangeEnabled(FGS_BG_START_RESTRICTION_CHANGE_ID, r.appInfo.uid)) {
            return false;
        }
        if (!this.mAm.mConstants.mFgsStartRestrictionCheckCallerTargetSdk) {
            return true;
        }
        if (com.android.server.am.Flags.newFgsRestrictionLogic()) {
            if (actualCallingUid == 1000) {
                return true;
            }
            callingUid = actualCallingUid;
        } else {
            callingUid = r.mRecentCallingUid;
        }
        return android.app.compat.CompatChanges.isChangeEnabled(FGS_BG_START_RESTRICTION_CHANGE_ID, callingUid);
    }

    private void logFgsBackgroundStart(com.android.server.am.ServiceRecord r) {
        if (!r.mLoggedInfoAllowStartForeground) {
            java.lang.String msg = "Background started FGS: " + (r.isFgsAllowedStart() ? "Allowed " : "Disallowed ") + r.mInfoAllowStartForeground + (r.isShortFgs() ? " (Called on SHORT_SERVICE)" : "");
            if (r.isFgsAllowedStart()) {
                if (this.mActiveServicesExt.logFgsBackgroundStart() && com.android.server.am.ActivityManagerUtils.shouldSamplePackageForAtom(r.packageName, this.mAm.mConstants.mFgsStartAllowedLogSampleRate)) {
                    android.util.Slog.wtfQuiet("ActivityManager", msg);
                }
                android.util.Slog.i("ActivityManager", msg);
            } else {
                if (this.mActiveServicesExt.logFgsBackgroundStart()) {
                    android.util.Slog.wtfQuiet("ActivityManager", msg);
                }
                android.util.Slog.w("ActivityManager", msg);
            }
            r.mLoggedInfoAllowStartForeground = true;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r42v0 */
    /* JADX WARN: Type inference failed for: r42v1, types: [int] */
    /* JADX WARN: Type inference failed for: r42v2 */
    /* JADX WARN: Type inference failed for: r6v7, types: [boolean] */
    private void logFGSStateChangeLocked(com.android.server.am.ServiceRecord serviceRecord, int i, int i2, int i3, int i4, int i5, boolean z) {
        ?? IsFgsAllowedWiu_forCapabilities;
        int fgsAllowStart;
        int i6;
        if (!com.android.server.am.ActivityManagerUtils.shouldSamplePackageForAtom(serviceRecord.packageName, this.mAm.mConstants.mFgsAtomSampleRate)) {
            return;
        }
        if (i == 1 || i == 2 || i == 5) {
            IsFgsAllowedWiu_forCapabilities = serviceRecord.mAllowWhileInUsePermissionInFgsAtEntering;
            fgsAllowStart = serviceRecord.mAllowStartForegroundAtEntering;
        } else {
            IsFgsAllowedWiu_forCapabilities = serviceRecord.isFgsAllowedWiu_forCapabilities();
            fgsAllowStart = serviceRecord.getFgsAllowStart();
        }
        int i7 = serviceRecord.mRecentCallerApplicationInfo != null ? serviceRecord.mRecentCallerApplicationInfo.targetSdkVersion : 0;
        com.android.internal.util.FrameworkStatsLog.write(60, serviceRecord.appInfo.uid, serviceRecord.shortInstanceName, i, IsFgsAllowedWiu_forCapabilities, fgsAllowStart, serviceRecord.appInfo.targetSdkVersion, serviceRecord.mRecentCallingUid, i7, serviceRecord.mInfoTempFgsAllowListReason != null ? serviceRecord.mInfoTempFgsAllowListReason.mCallingUid : -1, serviceRecord.mFgsNotificationWasDeferred, serviceRecord.mFgsNotificationShown, i2, serviceRecord.mStartForegroundCount, 0, serviceRecord.mFgsHasNotificationPermission, serviceRecord.foregroundServiceType, i4, serviceRecord.mIsFgsDelegate, serviceRecord.mFgsDelegation != null ? serviceRecord.mFgsDelegation.mOptions.mClientUid : -1, serviceRecord.mFgsDelegation != null ? serviceRecord.mFgsDelegation.mOptions.mDelegationService : 0, 0, null, null, this.mAm.getUidStateLocked(serviceRecord.appInfo.uid), this.mAm.getUidProcessCapabilityLocked(serviceRecord.appInfo.uid), this.mAm.getUidStateLocked(serviceRecord.mRecentCallingUid), this.mAm.getUidProcessCapabilityLocked(serviceRecord.mRecentCallingUid), 0L, 0L, serviceRecord.mAllowWiu_noBinding, serviceRecord.mAllowWiu_inBindService, serviceRecord.mAllowWiu_byBindings, serviceRecord.mAllowStart_noBinding, serviceRecord.mAllowStart_inBindService, serviceRecord.mAllowStart_byBindings, i5, z);
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 5) {
                        i6 = com.android.server.am.EventLogTags.AM_FOREGROUND_SERVICE_TIMED_OUT;
                    } else {
                        return;
                    }
                } else {
                    i6 = com.android.server.am.EventLogTags.AM_FOREGROUND_SERVICE_DENIED;
                }
            } else {
                i6 = com.android.server.am.EventLogTags.AM_FOREGROUND_SERVICE_STOP;
            }
        } else {
            i6 = com.android.server.am.EventLogTags.AM_FOREGROUND_SERVICE_START;
        }
        android.util.EventLog.writeEvent(i6, java.lang.Integer.valueOf(serviceRecord.userId), serviceRecord.shortInstanceName, java.lang.Integer.valueOf((int) IsFgsAllowedWiu_forCapabilities), android.os.PowerExemptionManager.reasonCodeToString(fgsAllowStart), java.lang.Integer.valueOf(serviceRecord.appInfo.targetSdkVersion), java.lang.Integer.valueOf(i7), java.lang.Integer.valueOf(serviceRecord.mFgsNotificationWasDeferred ? 1 : 0), java.lang.Integer.valueOf(serviceRecord.mFgsNotificationShown ? 1 : 0), java.lang.Integer.valueOf(i2), java.lang.Integer.valueOf(serviceRecord.mStartForegroundCount), fgsStopReasonToString(i3), java.lang.Integer.valueOf(serviceRecord.foregroundServiceType));
    }

    private void updateNumForegroundServicesLocked() {
        sNumForegroundServices.set(this.mAm.mProcessList.getNumForegroundServices());
    }

    boolean canAllowWhileInUsePermissionInFgsLocked(int callingPid, int callingUid, java.lang.String callingPackage) {
        return shouldAllowFgsWhileInUsePermissionLocked(callingPackage, callingPid, callingUid, null, android.app.BackgroundStartPrivileges.NONE) != -1;
    }

    boolean canAllowWhileInUsePermissionInFgsLocked(int callingPid, int callingUid, java.lang.String callingPackage, com.android.server.am.ProcessRecord targetProcess, android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        return shouldAllowFgsWhileInUsePermissionLocked(callingPackage, callingPid, callingUid, targetProcess, backgroundStartPrivileges) != -1;
    }

    private boolean verifyPackage(java.lang.String packageName, int uid) {
        if (uid == 0 || uid == 1000) {
            return true;
        }
        return this.mAm.getPackageManagerInternal().isSameApp(packageName, uid, android.os.UserHandle.getUserId(uid));
    }

    private static java.lang.String fgsStopReasonToString(int stopReason) {
        switch (stopReason) {
            case 1:
                return "STOP_FOREGROUND";
            case 2:
                return "STOP_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    boolean startForegroundServiceDelegateLocked(android.app.ForegroundServiceDelegationOptions options, final android.content.ServiceConnection connection) throws java.lang.Throwable {
        com.android.server.am.ProcessRecord callerApp;
        android.app.IApplicationThread caller;
        com.android.server.am.ProcessRecord callerApp2;
        android.app.IApplicationThread caller2;
        android.util.Slog.v("ActivityManager", "startForegroundServiceDelegateLocked " + options.getDescription());
        final android.content.ComponentName cn = options.getComponentName();
        for (int i = this.mFgsDelegations.size() - 1; i >= 0; i--) {
            if (this.mFgsDelegations.keyAt(i).mOptions.isSameDelegate(options)) {
                android.util.Slog.e("ActivityManager", "startForegroundServiceDelegate " + options.getDescription() + " already exists, multiple connections are not allowed");
                return false;
            }
        }
        int callingPid = options.mClientPid;
        int callingUid = options.mClientUid;
        int userId = android.os.UserHandle.getUserId(callingUid);
        java.lang.String callingPackage = options.mClientPackageName;
        if (!canStartForegroundServiceLocked(callingPid, callingUid, callingPackage)) {
            android.util.Slog.d("ActivityManager", "startForegroundServiceDelegateLocked aborted, app is in the background");
            return false;
        }
        android.app.IApplicationThread caller3 = options.mClientAppThread;
        if (caller3 != null) {
            callerApp2 = this.mAm.getRecordForAppLOSP(caller3);
            caller2 = caller3;
        } else {
            synchronized (this.mAm.mPidsSelfLocked) {
                try {
                    callerApp = this.mAm.mPidsSelfLocked.get(callingPid);
                    caller = callerApp.getThread();
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
            callerApp2 = callerApp;
            caller2 = caller;
        }
        if (callerApp2 == null) {
            throw new java.lang.SecurityException("Unable to find app for caller " + caller2 + " (pid=" + callingPid + ") when startForegroundServiceDelegateLocked " + cn);
        }
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(cn);
        android.app.IApplicationThread caller4 = caller2;
        com.android.server.am.ProcessRecord callerApp3 = callerApp2;
        com.android.server.am.ActiveServices.ServiceLookupResult res = retrieveServiceLocked(intent, null, false, -1, null, null, callingPackage, callingPid, callingUid, userId, true, false, false, false, options, false, false);
        if (res == null || res.record == null) {
            boolean z = false;
            android.util.Slog.d("ActivityManager", "startForegroundServiceDelegateLocked retrieveServiceLocked returns null");
            return z;
        }
        com.android.server.am.ServiceRecord r = res.record;
        r.setProcess(callerApp3, caller4, callingPid, null);
        r.mIsFgsDelegate = true;
        final com.android.server.am.ForegroundServiceDelegation delegation = new com.android.server.am.ForegroundServiceDelegation(options, connection);
        r.mFgsDelegation = delegation;
        this.mFgsDelegations.put(delegation, r);
        r.isForeground = true;
        r.mFgsEnterTime = android.os.SystemClock.uptimeMillis();
        r.foregroundServiceType = options.mForegroundServiceTypes;
        r.updateOomAdjSeq();
        setFgsRestrictionLocked(callingPackage, callingPid, callingUid, intent, r, userId, android.app.BackgroundStartPrivileges.NONE, false);
        com.android.server.am.ProcessServiceRecord psr = callerApp3.mServices;
        psr.startService(r);
        updateServiceForegroundLocked(psr, true);
        synchronized (this.mAm.mProcessStats.mLock) {
            try {
                com.android.internal.app.procstats.ServiceState stracker = r.getTracker();
                if (stracker != null) {
                    try {
                        stracker.setForeground(true, this.mAm.mProcessStats.getMemFactorLocked(), android.os.SystemClock.uptimeMillis());
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        while (true) {
                            try {
                                throw th;
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                        }
                    }
                }
                this.mAm.mBatteryStatsService.noteServiceStartRunning(callingUid, callingPackage, cn.getClassName());
                this.mAm.mAppOpsService.startOperation(android.app.AppOpsManager.getToken(this.mAm.mAppOpsService), 76, r.appInfo.uid, r.packageName, null, true, false, null, false, 0, -1);
                registerAppOpCallbackLocked(r);
                synchronized (this.mFGSLogger) {
                    try {
                        this.mFGSLogger.logForegroundServiceStart(r.appInfo.uid, 0, r);
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                        while (true) {
                            try {
                                throw th;
                            } catch (java.lang.Throwable th6) {
                                th = th6;
                            }
                        }
                    }
                }
                logFGSStateChangeLocked(r, 1, 0, 0, 0, 4, false);
                if (connection != null) {
                    this.mAm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            connection.onServiceConnected(cn, delegation.mBinder);
                        }
                    });
                }
                signalForegroundServiceObserversLocked(r);
                if (r.foregroundId != 0 && r.foregroundNoti != null) {
                    r.foregroundNoti.flags |= 64;
                    r.postNotification(true);
                }
                return true;
            } catch (java.lang.Throwable th7) {
                th = th7;
            }
        }
    }

    void stopForegroundServiceDelegateLocked(android.app.ForegroundServiceDelegationOptions options) throws java.lang.Throwable {
        com.android.server.am.ServiceRecord r = null;
        int i = this.mFgsDelegations.size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            if (this.mFgsDelegations.keyAt(i).mOptions.isSameDelegate(options)) {
                android.util.Slog.d("ActivityManager", "stopForegroundServiceDelegateLocked " + options.getDescription());
                com.android.server.am.ServiceRecord r2 = this.mFgsDelegations.valueAt(i);
                r = r2;
                break;
            }
        }
        if (r != null) {
            r.updateOomAdjSeq();
            bringDownServiceLocked(r, false);
        } else {
            android.util.Slog.e("ActivityManager", "stopForegroundServiceDelegateLocked delegate does not exist " + options.getDescription());
        }
    }

    void stopForegroundServiceDelegateLocked(android.content.ServiceConnection connection) throws java.lang.Throwable {
        com.android.server.am.ServiceRecord r = null;
        int i = this.mFgsDelegations.size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            com.android.server.am.ForegroundServiceDelegation d = this.mFgsDelegations.keyAt(i);
            if (d.mConnection == connection) {
                android.util.Slog.d("ActivityManager", "stopForegroundServiceDelegateLocked " + d.mOptions.getDescription());
                com.android.server.am.ServiceRecord r2 = this.mFgsDelegations.valueAt(i);
                r = r2;
                break;
            }
        }
        if (r != null) {
            r.updateOomAdjSeq();
            bringDownServiceLocked(r, false);
        } else {
            android.util.Slog.e("ActivityManager", "stopForegroundServiceDelegateLocked delegate does not exist");
        }
    }

    private static void getClientPackages(com.android.server.am.ServiceRecord sr, android.util.ArraySet<java.lang.String> output) {
        android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections = sr.getConnections();
        for (int conni = connections.size() - 1; conni >= 0; conni--) {
            java.util.ArrayList<com.android.server.am.ConnectionRecord> connl = connections.valueAt(conni);
            int size = connl.size();
            for (int i = 0; i < size; i++) {
                com.android.server.am.ConnectionRecord conn = connl.get(i);
                if (conn.binding.client != null) {
                    output.add(conn.binding.client.info.packageName);
                }
            }
        }
    }

    android.util.ArraySet<java.lang.String> getClientPackagesLocked(java.lang.String servicePackageName) {
        android.util.ArraySet<java.lang.String> results = new android.util.ArraySet<>();
        int[] users = this.mAm.mUserController.getUsers();
        for (int i : users) {
            android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> alls = getServicesLocked(i);
            int size = alls.size();
            for (int i2 = 0; i2 < size; i2++) {
                com.android.server.am.ServiceRecord sr = alls.valueAt(i2);
                if (sr.name.getPackageName().equals(servicePackageName)) {
                    getClientPackages(sr, results);
                }
            }
        }
        return results;
    }

    private boolean isDeviceProvisioningPackage(java.lang.String packageName) {
        if (this.mCachedDeviceProvisioningPackage == null) {
            this.mCachedDeviceProvisioningPackage = this.mAm.mContext.getResources().getString(android.R.string.config_dozeDoubleTapSensorType);
        }
        return this.mCachedDeviceProvisioningPackage != null && this.mCachedDeviceProvisioningPackage.equals(packageName);
    }

    private boolean wasStopped(com.android.server.am.ServiceRecord serviceRecord) {
        return (serviceRecord.appInfo.flags & 2097152) != 0;
    }

    public com.android.server.am.IActiveServicesWrapper getWrapper() {
        return this.mActiveServicesWrapper;
    }

    private class ActiveServicesWrapper implements com.android.server.am.IActiveServicesWrapper {
        private ActiveServicesWrapper() {
        }

        @Override // com.android.server.am.IActiveServicesWrapper
        public com.android.server.am.IActiveServicesExt getExtImpl() {
            return com.android.server.am.ActiveServices.this.mActiveServicesExt;
        }

        @Override // com.android.server.am.IActiveServicesWrapper
        public void setDynamicalLogEnable(boolean on) {
            com.android.server.am.ActiveServices.DEBUG_DELAYED_SERVICE = on;
            com.android.server.am.ActiveServices.DEBUG_DELAYED_STARTS = on;
            com.android.server.am.ActiveServices.LOG_SERVICE_START_STOP = on;
        }
    }
}
