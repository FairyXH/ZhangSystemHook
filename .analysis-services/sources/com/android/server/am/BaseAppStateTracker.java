package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseAppStateTracker<T extends com.android.server.am.BaseAppStatePolicy> {
    static final long ONE_DAY = 86400000;
    static final long ONE_HOUR = 3600000;
    static final long ONE_MINUTE = 60000;
    static final int STATE_TYPE_FGS_LOCATION = 4;
    static final int STATE_TYPE_FGS_MEDIA_PLAYBACK = 2;
    static final int STATE_TYPE_FGS_WITH_NOTIFICATION = 8;
    static final int STATE_TYPE_INDEX_FGS_LOCATION = 2;
    static final int STATE_TYPE_INDEX_FGS_MEDIA_PLAYBACK = 1;
    static final int STATE_TYPE_INDEX_FGS_WITH_NOTIFICATION = 3;
    static final int STATE_TYPE_INDEX_MEDIA_SESSION = 0;
    static final int STATE_TYPE_INDEX_PERMISSION = 4;
    static final int STATE_TYPE_MEDIA_SESSION = 1;
    static final int STATE_TYPE_NUM = 5;
    static final int STATE_TYPE_PERMISSION = 16;
    protected static final java.lang.String TAG = "ActivityManager";
    protected final com.android.server.am.AppRestrictionController mAppRestrictionController;
    protected final android.os.Handler mBgHandler;
    protected final android.content.Context mContext;
    final com.android.server.am.BaseAppStateTracker.Injector<T> mInjector;
    protected final java.lang.Object mLock;
    protected final java.util.ArrayList<com.android.server.am.BaseAppStateTracker.StateListener> mStateListeners = new java.util.ArrayList<>();

    interface StateListener {
        void onStateChange(int i, java.lang.String str, boolean z, long j, int i2);
    }

    BaseAppStateTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<T>> injector, java.lang.Object outerContext) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.reflect.InvocationTargetException {
        this.mContext = context;
        this.mAppRestrictionController = controller;
        this.mBgHandler = controller.getBackgroundHandler();
        this.mLock = controller.getLock();
        if (injector == null) {
            this.mInjector = new com.android.server.am.BaseAppStateTracker.Injector<>();
            return;
        }
        com.android.server.am.BaseAppStateTracker.Injector<T> localInjector = null;
        try {
            localInjector = injector.newInstance(outerContext);
        } catch (java.lang.Exception e) {
            android.util.Slog.w("ActivityManager", "Unable to instantiate " + injector, e);
        }
        this.mInjector = localInjector == null ? new com.android.server.am.BaseAppStateTracker.Injector<>() : localInjector;
    }

    static int stateTypeToIndex(int stateType) {
        return java.lang.Integer.numberOfTrailingZeros(stateType);
    }

    static int stateIndexToType(int stateTypeIndex) {
        return 1 << stateTypeIndex;
    }

    static java.lang.String stateTypesToString(int stateTypes) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[");
        boolean needDelimiter = false;
        int stateType = java.lang.Integer.highestOneBit(stateTypes);
        while (stateType != 0) {
            if (needDelimiter) {
                sb.append('|');
            }
            needDelimiter = true;
            switch (stateType) {
                case 1:
                    sb.append("MEDIA_SESSION");
                    break;
                case 2:
                    sb.append("FGS_MEDIA_PLAYBACK");
                    break;
                case 4:
                    sb.append("FGS_LOCATION");
                    break;
                case 8:
                    sb.append("FGS_NOTIFICATION");
                    break;
                case 16:
                    sb.append("PERMISSION");
                    break;
                default:
                    return "[UNKNOWN(" + java.lang.Integer.toHexString(stateTypes) + ")]";
            }
            stateTypes &= ~stateType;
            stateType = java.lang.Integer.highestOneBit(stateTypes);
        }
        sb.append("]");
        return sb.toString();
    }

    void registerStateListener(com.android.server.am.BaseAppStateTracker.StateListener listener) {
        synchronized (this.mLock) {
            this.mStateListeners.add(listener);
        }
    }

    void notifyListenersOnStateChange(int uid, java.lang.String packageName, boolean start, long now, int stateType) {
        synchronized (this.mLock) {
            int size = this.mStateListeners.size();
            for (int i = 0; i < size; i++) {
                this.mStateListeners.get(i).onStateChange(uid, packageName, start, now, stateType);
            }
        }
    }

    int getType() {
        return 0;
    }

    byte[] getTrackerInfoForStatsd(int uid) {
        return null;
    }

    T getPolicy() {
        return (T) this.mInjector.getPolicy();
    }

    void onSystemReady() {
        this.mInjector.onSystemReady();
    }

    void onUidAdded(int uid) {
    }

    void onUidRemoved(int uid) {
    }

    void onUserAdded(int userId) {
    }

    void onUserStarted(int userId) {
    }

    void onUserStopped(int userId) {
    }

    void onUserRemoved(int userId) {
    }

    void onLockedBootCompleted() {
    }

    void onPropertiesChanged(java.lang.String name) {
        getPolicy().onPropertiesChanged(name);
    }

    void onUserInteractionStarted(java.lang.String packageName, int uid) {
    }

    void onBackgroundRestrictionChanged(int uid, java.lang.String pkgName, boolean restricted) {
    }

    void onUidProcStateChanged(int uid, int procState) {
    }

    void onUidGone(int uid) {
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        this.mInjector.getPolicy().dump(pw, "  " + prefix);
    }

    void dumpAsProto(android.util.proto.ProtoOutputStream proto, int uid) {
    }

    static class Injector<T extends com.android.server.am.BaseAppStatePolicy> {
        android.app.ActivityManagerInternal mActivityManagerInternal;
        android.app.AppOpsManager mAppOpsManager;
        T mAppStatePolicy;
        android.os.BatteryManagerInternal mBatteryManagerInternal;
        android.os.BatteryStatsInternal mBatteryStatsInternal;
        android.content.Context mContext;
        com.android.server.DeviceIdleInternal mDeviceIdleInternal;
        com.android.internal.app.IAppOpsService mIAppOpsService;
        android.media.session.MediaSessionManager mMediaSessionManager;
        com.android.server.notification.NotificationManagerInternal mNotificationManagerInternal;
        android.content.pm.PackageManager mPackageManager;
        android.content.pm.PackageManagerInternal mPackageManagerInternal;
        android.permission.PermissionManager mPermissionManager;
        com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManagerServiceInternal;
        android.app.role.RoleManager mRoleManager;
        com.android.server.pm.UserManagerInternal mUserManagerInternal;

        Injector() {
        }

        void setPolicy(T policy) {
            this.mAppStatePolicy = policy;
        }

        void onSystemReady() {
            this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            this.mBatteryManagerInternal = (android.os.BatteryManagerInternal) com.android.server.LocalServices.getService(android.os.BatteryManagerInternal.class);
            this.mBatteryStatsInternal = (android.os.BatteryStatsInternal) com.android.server.LocalServices.getService(android.os.BatteryStatsInternal.class);
            this.mDeviceIdleInternal = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
            this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            this.mPermissionManagerServiceInternal = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
            android.content.Context context = this.mAppStatePolicy.mTracker.mContext;
            this.mPackageManager = context.getPackageManager();
            this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
            this.mMediaSessionManager = (android.media.session.MediaSessionManager) context.getSystemService(android.media.session.MediaSessionManager.class);
            this.mPermissionManager = (android.permission.PermissionManager) context.getSystemService(android.permission.PermissionManager.class);
            this.mRoleManager = (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
            this.mNotificationManagerInternal = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
            this.mIAppOpsService = com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
            this.mContext = context;
            getPolicy().onSystemReady();
        }

        android.app.ActivityManagerInternal getActivityManagerInternal() {
            return this.mActivityManagerInternal;
        }

        android.os.BatteryManagerInternal getBatteryManagerInternal() {
            return this.mBatteryManagerInternal;
        }

        android.os.BatteryStatsInternal getBatteryStatsInternal() {
            return this.mBatteryStatsInternal;
        }

        T getPolicy() {
            return this.mAppStatePolicy;
        }

        com.android.server.DeviceIdleInternal getDeviceIdleInternal() {
            return this.mDeviceIdleInternal;
        }

        com.android.server.pm.UserManagerInternal getUserManagerInternal() {
            return this.mUserManagerInternal;
        }

        long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        android.content.pm.PackageManager getPackageManager() {
            return this.mPackageManager;
        }

        android.content.pm.PackageManagerInternal getPackageManagerInternal() {
            return this.mPackageManagerInternal;
        }

        android.permission.PermissionManager getPermissionManager() {
            return this.mPermissionManager;
        }

        com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManagerServiceInternal() {
            return this.mPermissionManagerServiceInternal;
        }

        android.app.AppOpsManager getAppOpsManager() {
            return this.mAppOpsManager;
        }

        android.media.session.MediaSessionManager getMediaSessionManager() {
            return this.mMediaSessionManager;
        }

        long getServiceStartForegroundTimeout() {
            return this.mActivityManagerInternal.getServiceStartForegroundTimeout();
        }

        android.app.role.RoleManager getRoleManager() {
            return this.mRoleManager;
        }

        com.android.server.notification.NotificationManagerInternal getNotificationManagerInternal() {
            return this.mNotificationManagerInternal;
        }

        com.android.internal.app.IAppOpsService getIAppOpsService() {
            return this.mIAppOpsService;
        }

        int checkPermission(java.lang.String perm, int pid, int uid) {
            return this.mContext.checkPermission(perm, pid, uid);
        }
    }
}
