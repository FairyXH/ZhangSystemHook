package com.android.server.vr;

/* JADX INFO: loaded from: classes3.dex */
public class VrManagerService extends com.android.server.SystemService implements com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener, com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver {
    static final boolean DBG = false;
    private static final int EVENT_LOG_SIZE = 64;
    private static final int FLAG_ALL = 7;
    private static final int FLAG_AWAKE = 1;
    private static final int FLAG_KEYGUARD_UNLOCKED = 4;
    private static final int FLAG_NONE = 0;
    private static final int FLAG_SCREEN_ON = 2;
    private static final int INVALID_APPOPS_MODE = -1;
    private static final int MSG_PENDING_VR_STATE_CHANGE = 1;
    private static final int MSG_PERSISTENT_VR_MODE_STATE_CHANGE = 2;
    private static final int MSG_VR_STATE_CHANGE = 0;
    private static final int PENDING_STATE_DELAY_MS = 300;
    public static final java.lang.String TAG = "VrManagerService";
    private static final com.android.server.utils.ManagedApplicationService.BinderChecker sBinderChecker = new com.android.server.utils.ManagedApplicationService.BinderChecker() { // from class: com.android.server.vr.VrManagerService.3
        @Override // com.android.server.utils.ManagedApplicationService.BinderChecker
        public android.os.IInterface asInterface(android.os.IBinder binder) {
            return android.service.vr.IVrListener.Stub.asInterface(binder);
        }

        @Override // com.android.server.utils.ManagedApplicationService.BinderChecker
        public boolean checkType(android.os.IInterface service) {
            return service instanceof android.service.vr.IVrListener;
        }
    };
    private boolean mBootsToVr;
    private com.android.server.vr.EnabledComponentsObserver mComponentObserver;
    private android.content.Context mContext;
    private com.android.server.utils.ManagedApplicationService mCurrentVrCompositorService;
    private android.content.ComponentName mCurrentVrModeComponent;
    private int mCurrentVrModeUser;
    private com.android.server.utils.ManagedApplicationService mCurrentVrService;
    private android.content.ComponentName mDefaultVrService;
    private final com.android.server.utils.ManagedApplicationService.EventCallback mEventCallback;
    private boolean mGuard;
    private final android.os.Handler mHandler;
    private final java.lang.Object mLock;
    private boolean mLogLimitHit;
    private final java.util.ArrayDeque<com.android.server.utils.ManagedApplicationService.LogFormattable> mLoggingDeque;
    private final com.android.server.vr.VrManagerService.NotificationAccessManager mNotifAccessManager;
    private android.app.INotificationManager mNotificationManager;
    private final android.os.IBinder mOverlayToken;
    private com.android.server.vr.VrManagerService.VrState mPendingState;
    private boolean mPersistentVrModeEnabled;
    private final android.os.RemoteCallbackList<android.service.vr.IPersistentVrStateCallbacks> mPersistentVrStateRemoteCallbacks;
    private int mPreviousCoarseLocationMode;
    private int mPreviousManageOverlayMode;
    private boolean mRunning2dInVr;
    private boolean mStandby;
    private int mSystemSleepFlags;
    private boolean mUseStandbyToExitVrMode;
    private boolean mUserUnlocked;
    private com.android.server.vr.Vr2dDisplay mVr2dDisplay;
    private int mVrAppProcessId;
    private final android.service.vr.IVrManager mVrManager;
    private boolean mVrModeAllowed;
    private boolean mVrModeEnabled;
    private final android.os.RemoteCallbackList<android.service.vr.IVrStateCallbacks> mVrStateRemoteCallbacks;
    private boolean mWasDefaultGranted;

    private static native void initializeNative();

    private static native void setVrModeNative(boolean z);

    private void updateVrModeAllowedLocked() throws java.lang.Throwable {
        com.android.server.vr.VrManagerService.VrState vrState;
        boolean ignoreSleepFlags = this.mBootsToVr && this.mUseStandbyToExitVrMode;
        boolean disallowedByStandby = this.mStandby && this.mUseStandbyToExitVrMode;
        boolean allowed = (this.mSystemSleepFlags == 7 || ignoreSleepFlags) && this.mUserUnlocked && !disallowedByStandby;
        if (this.mVrModeAllowed != allowed) {
            this.mVrModeAllowed = allowed;
            if (this.mVrModeAllowed) {
                if (this.mBootsToVr) {
                    setPersistentVrModeEnabled(true);
                }
                if (this.mBootsToVr && !this.mVrModeEnabled) {
                    setVrMode(true, this.mDefaultVrService, 0, -1, null);
                    return;
                }
                return;
            }
            setPersistentModeAndNotifyListenersLocked(false);
            if (this.mVrModeEnabled && this.mCurrentVrService != null) {
                vrState = new com.android.server.vr.VrManagerService.VrState(this.mVrModeEnabled, this.mRunning2dInVr, this.mCurrentVrService.getComponent(), this.mCurrentVrService.getUserId(), this.mVrAppProcessId, this.mCurrentVrModeComponent);
            } else {
                vrState = null;
            }
            this.mPendingState = vrState;
            updateCurrentVrServiceLocked(false, false, null, 0, -1, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScreenOn(boolean isScreenOn) {
        setSystemState(2, isScreenOn);
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
    public void onAwakeStateChanged(boolean isAwake) {
        setSystemState(1, isAwake);
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
    public void onKeyguardStateChanged(boolean isShowing) {
        setSystemState(4, !isShowing);
    }

    private void setSystemState(int flags, boolean isOn) {
        synchronized (this.mLock) {
            int oldState = this.mSystemSleepFlags;
            if (isOn) {
                this.mSystemSleepFlags |= flags;
            } else {
                this.mSystemSleepFlags &= ~flags;
            }
            if (oldState != this.mSystemSleepFlags) {
                updateVrModeAllowedLocked();
            }
        }
    }

    private java.lang.String getStateAsString() {
        return ((this.mSystemSleepFlags & 1) != 0 ? "awake, " : "") + ((this.mSystemSleepFlags & 2) != 0 ? "screen_on, " : "") + ((this.mSystemSleepFlags & 4) != 0 ? "keyguard_off" : "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserUnlocked() {
        synchronized (this.mLock) {
            this.mUserUnlocked = true;
            updateVrModeAllowedLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStandbyEnabled(boolean standby) {
        synchronized (this.mLock) {
            if (!this.mBootsToVr) {
                android.util.Slog.e(TAG, "Attempting to set standby mode on a non-standalone device");
            } else {
                this.mStandby = standby;
                updateVrModeAllowedLocked();
            }
        }
    }

    private static class SettingEvent implements com.android.server.utils.ManagedApplicationService.LogFormattable {
        public final long timestamp = java.lang.System.currentTimeMillis();
        public final java.lang.String what;

        SettingEvent(java.lang.String what) {
            this.what = what;
        }

        @Override // com.android.server.utils.ManagedApplicationService.LogFormattable
        public java.lang.String toLogString(java.text.SimpleDateFormat dateFormat) {
            return dateFormat.format(new java.util.Date(this.timestamp)) + "   " + this.what;
        }
    }

    private static class VrState implements com.android.server.utils.ManagedApplicationService.LogFormattable {
        final android.content.ComponentName callingPackage;
        final boolean defaultPermissionsGranted;
        final boolean enabled;
        final int processId;
        final boolean running2dInVr;
        final android.content.ComponentName targetPackageName;
        final long timestamp;
        final int userId;

        VrState(boolean enabled, boolean running2dInVr, android.content.ComponentName targetPackageName, int userId, int processId, android.content.ComponentName callingPackage) {
            this.enabled = enabled;
            this.running2dInVr = running2dInVr;
            this.userId = userId;
            this.processId = processId;
            this.targetPackageName = targetPackageName;
            this.callingPackage = callingPackage;
            this.defaultPermissionsGranted = false;
            this.timestamp = java.lang.System.currentTimeMillis();
        }

        VrState(boolean enabled, boolean running2dInVr, android.content.ComponentName targetPackageName, int userId, int processId, android.content.ComponentName callingPackage, boolean defaultPermissionsGranted) {
            this.enabled = enabled;
            this.running2dInVr = running2dInVr;
            this.userId = userId;
            this.processId = processId;
            this.targetPackageName = targetPackageName;
            this.callingPackage = callingPackage;
            this.defaultPermissionsGranted = defaultPermissionsGranted;
            this.timestamp = java.lang.System.currentTimeMillis();
        }

        @Override // com.android.server.utils.ManagedApplicationService.LogFormattable
        public java.lang.String toLogString(java.text.SimpleDateFormat dateFormat) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(dateFormat.format(new java.util.Date(this.timestamp)));
            sb.append("  ");
            sb.append("State changed to:");
            sb.append("  ");
            sb.append(this.enabled ? "ENABLED" : "DISABLED");
            sb.append("\n");
            if (this.enabled) {
                sb.append("  ");
                sb.append("User=");
                sb.append(this.userId);
                sb.append("\n");
                sb.append("  ");
                sb.append("Current VR Activity=");
                android.content.ComponentName componentName = this.callingPackage;
                java.lang.String strFlattenToString = com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG;
                sb.append(componentName == null ? com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG : this.callingPackage.flattenToString());
                sb.append("\n");
                sb.append("  ");
                sb.append("Bound VrListenerService=");
                if (this.targetPackageName != null) {
                    strFlattenToString = this.targetPackageName.flattenToString();
                }
                sb.append(strFlattenToString);
                sb.append("\n");
                if (this.defaultPermissionsGranted) {
                    sb.append("  ");
                    sb.append("Default permissions granted to the bound VrListenerService.");
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
    }

    private final class NotificationAccessManager {
        private final android.util.SparseArray<android.util.ArraySet<java.lang.String>> mAllowedPackages;
        private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mNotificationAccessPackageToUserId;

        private NotificationAccessManager() {
            this.mAllowedPackages = new android.util.SparseArray<>();
            this.mNotificationAccessPackageToUserId = new android.util.ArrayMap<>();
        }

        public void update(java.util.Collection<java.lang.String> packageNames) {
            int currentUserId = android.app.ActivityManager.getCurrentUser();
            android.util.ArraySet<java.lang.String> allowed = this.mAllowedPackages.get(currentUserId);
            if (allowed == null) {
                allowed = new android.util.ArraySet<>();
            }
            int listenerCount = this.mNotificationAccessPackageToUserId.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                int grantUserId = this.mNotificationAccessPackageToUserId.valueAt(i).intValue();
                if (grantUserId != currentUserId) {
                    java.lang.String packageName = this.mNotificationAccessPackageToUserId.keyAt(i);
                    com.android.server.vr.VrManagerService.this.revokeNotificationListenerAccess(packageName, grantUserId);
                    com.android.server.vr.VrManagerService.this.revokeNotificationPolicyAccess(packageName);
                    com.android.server.vr.VrManagerService.this.revokeCoarseLocationPermissionIfNeeded(packageName, grantUserId);
                    this.mNotificationAccessPackageToUserId.removeAt(i);
                }
            }
            for (java.lang.String pkg : allowed) {
                if (!packageNames.contains(pkg)) {
                    com.android.server.vr.VrManagerService.this.revokeNotificationListenerAccess(pkg, currentUserId);
                    com.android.server.vr.VrManagerService.this.revokeNotificationPolicyAccess(pkg);
                    com.android.server.vr.VrManagerService.this.revokeCoarseLocationPermissionIfNeeded(pkg, currentUserId);
                    this.mNotificationAccessPackageToUserId.remove(pkg);
                }
            }
            for (java.lang.String pkg2 : packageNames) {
                if (!allowed.contains(pkg2)) {
                    com.android.server.vr.VrManagerService.this.grantNotificationPolicyAccess(pkg2);
                    com.android.server.vr.VrManagerService.this.grantNotificationListenerAccess(pkg2, currentUserId);
                    com.android.server.vr.VrManagerService.this.grantCoarseLocationPermissionIfNeeded(pkg2, currentUserId);
                    this.mNotificationAccessPackageToUserId.put(pkg2, java.lang.Integer.valueOf(currentUserId));
                }
            }
            allowed.clear();
            allowed.addAll(packageNames);
            this.mAllowedPackages.put(currentUserId, allowed);
        }
    }

    @Override // com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener
    public void onEnabledComponentChanged() {
        synchronized (this.mLock) {
            int currentUser = android.app.ActivityManager.getCurrentUser();
            android.util.ArraySet<android.content.ComponentName> enabledListeners = this.mComponentObserver.getEnabled(currentUser);
            android.util.ArraySet<java.lang.String> enabledPackages = new android.util.ArraySet<>();
            for (android.content.ComponentName n : enabledListeners) {
                java.lang.String pkg = n.getPackageName();
                if (isDefaultAllowed(pkg)) {
                    enabledPackages.add(n.getPackageName());
                }
            }
            this.mNotifAccessManager.update(enabledPackages);
            if (this.mVrModeAllowed) {
                consumeAndApplyPendingStateLocked(false);
                if (this.mCurrentVrService == null) {
                    return;
                }
                updateCurrentVrServiceLocked(this.mVrModeEnabled, this.mRunning2dInVr, this.mCurrentVrService.getComponent(), this.mCurrentVrService.getUserId(), this.mVrAppProcessId, this.mCurrentVrModeComponent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCallerPermissionAnyOf(java.lang.String... permissions) {
        for (java.lang.String permission : permissions) {
            if (this.mContext.checkCallingOrSelfPermission(permission) == 0) {
                return;
            }
        }
        throw new java.lang.SecurityException("Caller does not hold at least one of the permissions: " + java.util.Arrays.toString(permissions));
    }

    private final class LocalService extends com.android.server.vr.VrManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void setVrMode(boolean enabled, android.content.ComponentName packageName, int userId, int processId, android.content.ComponentName callingPackage) {
            com.android.server.vr.VrManagerService.this.setVrMode(enabled, packageName, userId, processId, callingPackage);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void onScreenStateChanged(boolean isScreenOn) {
            com.android.server.vr.VrManagerService.this.setScreenOn(isScreenOn);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public boolean isCurrentVrListener(java.lang.String packageName, int userId) {
            return com.android.server.vr.VrManagerService.this.isCurrentVrListener(packageName, userId);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public int hasVrPackage(android.content.ComponentName packageName, int userId) {
            return com.android.server.vr.VrManagerService.this.hasVrPackage(packageName, userId);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void setPersistentVrModeEnabled(boolean enabled) {
            com.android.server.vr.VrManagerService.this.setPersistentVrModeEnabled(enabled);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void setVr2dDisplayProperties(android.app.Vr2dDisplayProperties compatDisplayProp) {
            com.android.server.vr.VrManagerService.this.setVr2dDisplayProperties(compatDisplayProp);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public int getVr2dDisplayId() {
            return com.android.server.vr.VrManagerService.this.getVr2dDisplayId();
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void addPersistentVrModeStateListener(android.service.vr.IPersistentVrStateCallbacks listener) {
            com.android.server.vr.VrManagerService.this.addPersistentStateCallback(listener);
        }
    }

    public VrManagerService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mOverlayToken = new android.os.Binder();
        this.mVrStateRemoteCallbacks = new android.os.RemoteCallbackList<>();
        this.mPersistentVrStateRemoteCallbacks = new android.os.RemoteCallbackList<>();
        this.mPreviousCoarseLocationMode = -1;
        this.mPreviousManageOverlayMode = -1;
        this.mLoggingDeque = new java.util.ArrayDeque<>(64);
        this.mNotifAccessManager = new com.android.server.vr.VrManagerService.NotificationAccessManager();
        this.mSystemSleepFlags = 5;
        this.mEventCallback = new com.android.server.utils.ManagedApplicationService.EventCallback() { // from class: com.android.server.vr.VrManagerService.1
            @Override // com.android.server.utils.ManagedApplicationService.EventCallback
            public void onServiceEvent(com.android.server.utils.ManagedApplicationService.LogEvent event) {
                android.content.ComponentName component;
                com.android.server.vr.VrManagerService.this.logEvent(event);
                synchronized (com.android.server.vr.VrManagerService.this.mLock) {
                    component = com.android.server.vr.VrManagerService.this.mCurrentVrService == null ? null : com.android.server.vr.VrManagerService.this.mCurrentVrService.getComponent();
                    if (component != null && component.equals(event.component) && (event.event == 2 || event.event == 3)) {
                        com.android.server.vr.VrManagerService.this.callFocusedActivityChangedLocked();
                    }
                }
                if (!com.android.server.vr.VrManagerService.this.mBootsToVr && event.event == 4) {
                    if (component == null || component.equals(event.component)) {
                        android.util.Slog.e(com.android.server.vr.VrManagerService.TAG, "VrListenerSevice has died permanently, leaving system VR mode.");
                        com.android.server.vr.VrManagerService.this.setPersistentVrModeEnabled(false);
                    }
                }
            }
        };
        this.mHandler = new android.os.Handler() { // from class: com.android.server.vr.VrManagerService.2
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:
                        boolean state = msg.arg1 == 1;
                        int i = com.android.server.vr.VrManagerService.this.mVrStateRemoteCallbacks.beginBroadcast();
                        while (i > 0) {
                            i--;
                            try {
                                com.android.server.vr.VrManagerService.this.mVrStateRemoteCallbacks.getBroadcastItem(i).onVrStateChanged(state);
                            } catch (android.os.RemoteException e) {
                            }
                        }
                        com.android.server.vr.VrManagerService.this.mVrStateRemoteCallbacks.finishBroadcast();
                        return;
                    case 1:
                        synchronized (com.android.server.vr.VrManagerService.this.mLock) {
                            if (com.android.server.vr.VrManagerService.this.mVrModeAllowed) {
                                com.android.server.vr.VrManagerService.this.consumeAndApplyPendingStateLocked();
                            }
                            break;
                        }
                        return;
                    case 2:
                        boolean state2 = msg.arg1 == 1;
                        int i2 = com.android.server.vr.VrManagerService.this.mPersistentVrStateRemoteCallbacks.beginBroadcast();
                        while (i2 > 0) {
                            i2--;
                            try {
                                com.android.server.vr.VrManagerService.this.mPersistentVrStateRemoteCallbacks.getBroadcastItem(i2).onPersistentVrStateChanged(state2);
                            } catch (android.os.RemoteException e2) {
                            }
                        }
                        com.android.server.vr.VrManagerService.this.mPersistentVrStateRemoteCallbacks.finishBroadcast();
                        return;
                    default:
                        throw new java.lang.IllegalStateException("Unknown message type: " + msg.what);
                }
            }
        };
        this.mVrManager = new android.service.vr.IVrManager.Stub() { // from class: com.android.server.vr.VrManagerService.4
            public void registerListener(android.service.vr.IVrStateCallbacks cb) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (cb == null) {
                    throw new java.lang.IllegalArgumentException("Callback binder object is null.");
                }
                com.android.server.vr.VrManagerService.this.addStateCallback(cb);
            }

            public void unregisterListener(android.service.vr.IVrStateCallbacks cb) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (cb == null) {
                    throw new java.lang.IllegalArgumentException("Callback binder object is null.");
                }
                com.android.server.vr.VrManagerService.this.removeStateCallback(cb);
            }

            public void registerPersistentVrStateListener(android.service.vr.IPersistentVrStateCallbacks cb) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (cb == null) {
                    throw new java.lang.IllegalArgumentException("Callback binder object is null.");
                }
                com.android.server.vr.VrManagerService.this.addPersistentStateCallback(cb);
            }

            public void unregisterPersistentVrStateListener(android.service.vr.IPersistentVrStateCallbacks cb) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (cb == null) {
                    throw new java.lang.IllegalArgumentException("Callback binder object is null.");
                }
                com.android.server.vr.VrManagerService.this.removePersistentStateCallback(cb);
            }

            public boolean getVrModeState() {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                return com.android.server.vr.VrManagerService.this.getVrMode();
            }

            public boolean getPersistentVrModeEnabled() {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                return com.android.server.vr.VrManagerService.this.getPersistentVrMode();
            }

            public void setPersistentVrModeEnabled(boolean enabled) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.RESTRICTED_VR_ACCESS");
                com.android.server.vr.VrManagerService.this.setPersistentVrModeEnabled(enabled);
            }

            public void setVr2dDisplayProperties(android.app.Vr2dDisplayProperties vr2dDisplayProp) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.RESTRICTED_VR_ACCESS");
                com.android.server.vr.VrManagerService.this.setVr2dDisplayProperties(vr2dDisplayProp);
            }

            public int getVr2dDisplayId() {
                return com.android.server.vr.VrManagerService.this.getVr2dDisplayId();
            }

            public void setAndBindCompositor(java.lang.String componentName) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.RESTRICTED_VR_ACCESS");
                com.android.server.vr.VrManagerService.this.setAndBindCompositor(componentName == null ? null : android.content.ComponentName.unflattenFromString(componentName));
            }

            public void setStandbyEnabled(boolean standby) {
                com.android.server.vr.VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER");
                com.android.server.vr.VrManagerService.this.setStandbyEnabled(standby);
            }

            protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
                if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.vr.VrManagerService.this.mContext, com.android.server.vr.VrManagerService.TAG, pw)) {
                    pw.println("********* Dump of VrManagerService *********");
                    pw.println("VR mode is currently: " + (com.android.server.vr.VrManagerService.this.mVrModeAllowed ? "allowed" : "disallowed"));
                    pw.println("Persistent VR mode is currently: " + (com.android.server.vr.VrManagerService.this.mPersistentVrModeEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
                    pw.println("Currently bound VR listener service: " + (com.android.server.vr.VrManagerService.this.mCurrentVrService == null ? com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG : com.android.server.vr.VrManagerService.this.mCurrentVrService.getComponent().flattenToString()));
                    pw.println("Currently bound VR compositor service: " + (com.android.server.vr.VrManagerService.this.mCurrentVrCompositorService == null ? com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG : com.android.server.vr.VrManagerService.this.mCurrentVrCompositorService.getComponent().flattenToString()));
                    pw.println("Previous state transitions:\n");
                    com.android.server.vr.VrManagerService.this.dumpStateTransitions(pw);
                    pw.println("\n\nRemote Callbacks:");
                    int i = com.android.server.vr.VrManagerService.this.mVrStateRemoteCallbacks.beginBroadcast();
                    while (true) {
                        int i2 = i - 1;
                        if (i <= 0) {
                            break;
                        }
                        pw.print("  ");
                        pw.print(com.android.server.vr.VrManagerService.this.mVrStateRemoteCallbacks.getBroadcastItem(i2));
                        if (i2 > 0) {
                            pw.println(",");
                        }
                        i = i2;
                    }
                    com.android.server.vr.VrManagerService.this.mVrStateRemoteCallbacks.finishBroadcast();
                    pw.println("\n\nPersistent Vr State Remote Callbacks:");
                    int i3 = com.android.server.vr.VrManagerService.this.mPersistentVrStateRemoteCallbacks.beginBroadcast();
                    while (true) {
                        int i4 = i3 - 1;
                        if (i3 <= 0) {
                            break;
                        }
                        pw.print("  ");
                        pw.print(com.android.server.vr.VrManagerService.this.mPersistentVrStateRemoteCallbacks.getBroadcastItem(i4));
                        if (i4 > 0) {
                            pw.println(",");
                        }
                        i3 = i4;
                    }
                    com.android.server.vr.VrManagerService.this.mPersistentVrStateRemoteCallbacks.finishBroadcast();
                    pw.println("\n");
                    pw.println("Installed VrListenerService components:");
                    int userId = com.android.server.vr.VrManagerService.this.mCurrentVrModeUser;
                    android.util.ArraySet<android.content.ComponentName> installed = com.android.server.vr.VrManagerService.this.mComponentObserver.getInstalled(userId);
                    if (installed == null || installed.size() == 0) {
                        pw.println(com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG);
                    } else {
                        for (android.content.ComponentName n : installed) {
                            pw.print("  ");
                            pw.println(n.flattenToString());
                        }
                    }
                    pw.println("Enabled VrListenerService components:");
                    android.util.ArraySet<android.content.ComponentName> enabled = com.android.server.vr.VrManagerService.this.mComponentObserver.getEnabled(userId);
                    if (enabled == null || enabled.size() == 0) {
                        pw.println(com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG);
                    } else {
                        for (android.content.ComponentName n2 : enabled) {
                            pw.print("  ");
                            pw.println(n2.flattenToString());
                        }
                    }
                    pw.println("\n");
                    pw.println("********* End of VrManagerService Dump *********");
                }
            }
        };
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        synchronized (this.mLock) {
            initializeNative();
            this.mContext = getContext();
        }
        boolean z = false;
        this.mBootsToVr = android.os.SystemProperties.getBoolean("ro.boot.vr", false);
        if (this.mBootsToVr && android.os.SystemProperties.getBoolean("persist.vr.use_standby_to_exit_vr_mode", true)) {
            z = true;
        }
        this.mUseStandbyToExitVrMode = z;
        publishLocalService(com.android.server.vr.VrManagerInternal.class, new com.android.server.vr.VrManagerService.LocalService());
        publishBinderService("vrmanager", this.mVrManager.asBinder());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).registerScreenObserver(this);
            this.mNotificationManager = android.app.INotificationManager.Stub.asInterface(android.os.ServiceManager.getService("notification"));
            synchronized (this.mLock) {
                android.os.Looper looper = android.os.Looper.getMainLooper();
                android.os.Handler handler = new android.os.Handler(looper);
                java.util.ArrayList<com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener> listeners = new java.util.ArrayList<>();
                listeners.add(this);
                this.mComponentObserver = com.android.server.vr.EnabledComponentsObserver.build(this.mContext, handler, "enabled_vr_listeners", looper, "android.permission.BIND_VR_LISTENER_SERVICE", "android.service.vr.VrListenerService", this.mLock, listeners);
                this.mComponentObserver.rebuildAll();
            }
            android.util.ArraySet<android.content.ComponentName> defaultVrComponents = com.android.server.SystemConfig.getInstance().getDefaultVrComponents();
            if (defaultVrComponents.size() > 0) {
                this.mDefaultVrService = defaultVrComponents.valueAt(0);
            } else {
                android.util.Slog.i(TAG, "No default vr listener service found.");
            }
            android.hardware.display.DisplayManager dm = (android.hardware.display.DisplayManager) getContext().getSystemService("display");
            this.mVr2dDisplay = new com.android.server.vr.Vr2dDisplay(dm, (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class), (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class), this.mVrManager);
            this.mVr2dDisplay.init(getContext(), this.mBootsToVr);
            android.content.IntentFilter intentFilter = new android.content.IntentFilter();
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            getContext().registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.vr.VrManagerService.5
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction())) {
                        com.android.server.vr.VrManagerService.this.setUserUnlocked();
                    }
                }
            }, intentFilter);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.vr.VrManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserSwitching$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserSwitching$0() {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStopped(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    private void updateOverlayStateLocked(java.lang.String exemptedPackage, int newUserId, int oldUserId) {
        android.os.PackageTagsList exemptions;
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) getContext().getSystemService(android.app.AppOpsManager.class);
        if (oldUserId != newUserId) {
            appOpsManager.setUserRestrictionForUser(24, false, this.mOverlayToken, null, oldUserId);
        }
        if (exemptedPackage == null) {
            exemptions = null;
        } else {
            android.os.PackageTagsList exemptions2 = new android.os.PackageTagsList.Builder(1).add(exemptedPackage).build();
            exemptions = exemptions2;
        }
        appOpsManager.setUserRestrictionForUser(24, this.mVrModeEnabled, this.mOverlayToken, exemptions, newUserId);
    }

    private void updateDependentAppOpsLocked(java.lang.String newVrServicePackage, int newUserId, java.lang.String oldVrServicePackage, int oldUserId) {
        if (java.util.Objects.equals(newVrServicePackage, oldVrServicePackage)) {
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            updateOverlayStateLocked(newVrServicePackage, newUserId, oldUserId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0111 A[Catch: all -> 0x013c, TryCatch #2 {all -> 0x013c, blocks: (B:52:0x010b, B:54:0x0111, B:55:0x0114, B:57:0x0118, B:59:0x0124, B:62:0x012f, B:64:0x0134), top: B:79:0x010b }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0118 A[Catch: all -> 0x013c, TryCatch #2 {all -> 0x013c, blocks: (B:52:0x010b, B:54:0x0111, B:55:0x0114, B:57:0x0118, B:59:0x0124, B:62:0x012f, B:64:0x0134), top: B:79:0x010b }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x012d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0134 A[Catch: all -> 0x013c, TRY_LEAVE, TryCatch #2 {all -> 0x013c, blocks: (B:52:0x010b, B:54:0x0111, B:55:0x0114, B:57:0x0118, B:59:0x0124, B:62:0x012f, B:64:0x0134), top: B:79:0x010b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean updateCurrentVrServiceLocked(boolean r18, boolean r19, android.content.ComponentName r20, int r21, int r22, android.content.ComponentName r23) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 333
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.vr.VrManagerService.updateCurrentVrServiceLocked(boolean, boolean, android.content.ComponentName, int, int, android.content.ComponentName):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callFocusedActivityChangedLocked() {
        final android.content.ComponentName c = this.mCurrentVrModeComponent;
        final boolean b = this.mRunning2dInVr;
        final int pid = this.mVrAppProcessId;
        this.mCurrentVrService.sendEvent(new com.android.server.utils.ManagedApplicationService.PendingEvent() { // from class: com.android.server.vr.VrManagerService.6
            @Override // com.android.server.utils.ManagedApplicationService.PendingEvent
            public void runEvent(android.os.IInterface service) throws android.os.RemoteException {
                android.service.vr.IVrListener l = (android.service.vr.IVrListener) service;
                l.focusedActivityChanged(c, b, pid);
            }
        });
    }

    private boolean isDefaultAllowed(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.content.pm.ApplicationInfo info = null;
        try {
            info = pm.getApplicationInfo(packageName, 128);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        if (info == null) {
            return false;
        }
        if (!info.isSystemApp() && !info.isUpdatedSystemApp()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantNotificationPolicyAccess(java.lang.String pkg) {
        android.app.NotificationManager nm = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        nm.setNotificationPolicyAccessGranted(pkg, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeNotificationPolicyAccess(java.lang.String pkg) {
        android.app.NotificationManager nm = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        nm.removeAutomaticZenRules(pkg);
        nm.setNotificationPolicyAccessGranted(pkg, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantNotificationListenerAccess(java.lang.String pkg, int userId) {
        android.app.NotificationManager nm = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.util.ArraySet<android.content.ComponentName> possibleServices = com.android.server.vr.EnabledComponentsObserver.loadComponentNames(pm, userId, "android.service.notification.NotificationListenerService", "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE");
        for (android.content.ComponentName c : possibleServices) {
            if (java.util.Objects.equals(c.getPackageName(), pkg)) {
                try {
                    nm.setNotificationListenerAccessGrantedForUser(c, userId, true);
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(TAG, "Could not grant NLS access to package " + pkg, e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeNotificationListenerAccess(java.lang.String pkg, int userId) {
        android.app.NotificationManager nm = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        java.util.List<android.content.ComponentName> current = nm.getEnabledNotificationListeners(userId);
        for (android.content.ComponentName component : current) {
            if (component != null && component.getPackageName().equals(pkg)) {
                nm.setNotificationListenerAccessGrantedForUser(component, userId, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantCoarseLocationPermissionIfNeeded(java.lang.String pkg, int userId) {
        if (!isPermissionUserUpdated("android.permission.ACCESS_COARSE_LOCATION", pkg, userId)) {
            try {
                this.mContext.getPackageManager().grantRuntimePermission(pkg, "android.permission.ACCESS_COARSE_LOCATION", new android.os.UserHandle(userId));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.w(TAG, "Could not grant coarse location permission, package " + pkg + " was removed.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeCoarseLocationPermissionIfNeeded(java.lang.String pkg, int userId) {
        if (!isPermissionUserUpdated("android.permission.ACCESS_COARSE_LOCATION", pkg, userId)) {
            try {
                this.mContext.getPackageManager().revokeRuntimePermission(pkg, "android.permission.ACCESS_COARSE_LOCATION", new android.os.UserHandle(userId));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.w(TAG, "Could not revoke coarse location permission, package " + pkg + " was removed.");
            }
        }
    }

    private boolean isPermissionUserUpdated(java.lang.String permission, java.lang.String pkg, int userId) {
        int flags = this.mContext.getPackageManager().getPermissionFlags(permission, pkg, new android.os.UserHandle(userId));
        return (flags & 3) != 0;
    }

    private android.util.ArraySet<java.lang.String> getNotificationListeners(android.content.ContentResolver resolver, int userId) {
        java.lang.String flat = android.provider.Settings.Secure.getStringForUser(resolver, "enabled_notification_listeners", userId);
        android.util.ArraySet<java.lang.String> current = new android.util.ArraySet<>();
        if (flat != null) {
            java.lang.String[] allowed = flat.split(":");
            for (java.lang.String s : allowed) {
                if (!android.text.TextUtils.isEmpty(s)) {
                    current.add(s);
                }
            }
        }
        return current;
    }

    private static java.lang.String formatSettings(java.util.Collection<java.lang.String> c) {
        if (c == null || c.isEmpty()) {
            return "";
        }
        java.lang.StringBuilder b = new java.lang.StringBuilder();
        boolean start = true;
        for (java.lang.String s : c) {
            if (!"".equals(s)) {
                if (!start) {
                    b.append(':');
                }
                b.append(s);
                start = false;
            }
        }
        return b.toString();
    }

    private void createAndConnectService(android.content.ComponentName component, int userId) {
        this.mCurrentVrService = createVrListenerService(component, userId);
        this.mCurrentVrService.connect();
        android.util.Slog.i(TAG, "Connecting " + component + " for user " + userId);
    }

    private void changeVrModeLocked(boolean enabled) {
        if (this.mVrModeEnabled != enabled) {
            this.mVrModeEnabled = enabled;
            android.util.Slog.i(TAG, "VR mode " + (this.mVrModeEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
            setVrModeNative(this.mVrModeEnabled);
            onVrModeChangedLocked();
        }
    }

    private void onVrModeChangedLocked() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(0, this.mVrModeEnabled ? 1 : 0, 0));
    }

    private com.android.server.utils.ManagedApplicationService createVrListenerService(android.content.ComponentName component, int userId) {
        int retryType = this.mBootsToVr ? 1 : 2;
        return com.android.server.utils.ManagedApplicationService.build(this.mContext, component, userId, android.R.string.usb_contaminant_detected_title, "android.settings.VR_LISTENER_SETTINGS", sBinderChecker, true, retryType, this.mHandler, this.mEventCallback);
    }

    private com.android.server.utils.ManagedApplicationService createVrCompositorService(android.content.ComponentName component, int userId) {
        int retryType = this.mBootsToVr ? 1 : 3;
        return com.android.server.utils.ManagedApplicationService.build(this.mContext, component, userId, 0, null, null, true, retryType, this.mHandler, this.mEventCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void consumeAndApplyPendingStateLocked() throws java.lang.Throwable {
        consumeAndApplyPendingStateLocked(true);
    }

    private void consumeAndApplyPendingStateLocked(boolean disconnectIfNoPendingState) throws java.lang.Throwable {
        if (this.mPendingState != null) {
            updateCurrentVrServiceLocked(this.mPendingState.enabled, this.mPendingState.running2dInVr, this.mPendingState.targetPackageName, this.mPendingState.userId, this.mPendingState.processId, this.mPendingState.callingPackage);
            this.mPendingState = null;
        } else if (disconnectIfNoPendingState) {
            updateCurrentVrServiceLocked(false, false, null, 0, -1, null);
        }
    }

    private void logStateLocked() {
        android.content.ComponentName currentBoundService = this.mCurrentVrService == null ? null : this.mCurrentVrService.getComponent();
        logEvent(new com.android.server.vr.VrManagerService.VrState(this.mVrModeEnabled, this.mRunning2dInVr, currentBoundService, this.mCurrentVrModeUser, this.mVrAppProcessId, this.mCurrentVrModeComponent, this.mWasDefaultGranted));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logEvent(com.android.server.utils.ManagedApplicationService.LogFormattable event) {
        synchronized (this.mLoggingDeque) {
            if (this.mLoggingDeque.size() == 64) {
                this.mLoggingDeque.removeFirst();
                this.mLogLimitHit = true;
            }
            this.mLoggingDeque.add(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpStateTransitions(java.io.PrintWriter pw) {
        java.text.SimpleDateFormat d = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        synchronized (this.mLoggingDeque) {
            if (this.mLoggingDeque.size() == 0) {
                pw.print("  ");
                pw.println(com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG);
            }
            if (this.mLogLimitHit) {
                pw.println("...");
            }
            for (com.android.server.utils.ManagedApplicationService.LogFormattable event : this.mLoggingDeque) {
                pw.println(event.toLogString(d));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVrMode(boolean enabled, android.content.ComponentName targetPackageName, int userId, int processId, android.content.ComponentName callingPackage) {
        boolean z;
        android.content.ComponentName targetListener;
        synchronized (this.mLock) {
            boolean running2dInVr = false;
            if (!enabled) {
                try {
                    z = this.mPersistentVrModeEnabled;
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            boolean targetEnabledState = z;
            if (!enabled && this.mPersistentVrModeEnabled) {
                running2dInVr = true;
            }
            if (running2dInVr) {
                targetListener = this.mDefaultVrService;
            } else {
                targetListener = targetPackageName;
            }
            com.android.server.vr.VrManagerService.VrState pending = new com.android.server.vr.VrManagerService.VrState(targetEnabledState, running2dInVr, targetListener, userId, processId, callingPackage);
            if (!this.mVrModeAllowed) {
                this.mPendingState = pending;
                return;
            }
            if (!targetEnabledState && this.mCurrentVrService != null) {
                if (this.mPendingState == null) {
                    this.mHandler.sendEmptyMessageDelayed(1, 300L);
                }
                this.mPendingState = pending;
            } else {
                this.mHandler.removeMessages(1);
                this.mPendingState = null;
                updateCurrentVrServiceLocked(targetEnabledState, running2dInVr, targetListener, userId, processId, callingPackage);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPersistentVrModeEnabled(boolean enabled) {
        synchronized (this.mLock) {
            setPersistentModeAndNotifyListenersLocked(enabled);
            if (!enabled) {
                setVrMode(false, null, 0, -1, null);
            }
        }
    }

    public void setVr2dDisplayProperties(android.app.Vr2dDisplayProperties compatDisplayProp) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mVr2dDisplay != null) {
                this.mVr2dDisplay.setVirtualDisplayProperties(compatDisplayProp);
            } else {
                android.os.Binder.restoreCallingIdentity(token);
                android.util.Slog.w(TAG, "Vr2dDisplay is null!");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVr2dDisplayId() {
        if (this.mVr2dDisplay != null) {
            return this.mVr2dDisplay.getVirtualDisplayId();
        }
        android.util.Slog.w(TAG, "Vr2dDisplay is null!");
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAndBindCompositor(android.content.ComponentName componentName) {
        int userId = android.os.UserHandle.getCallingUserId();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                updateCompositorServiceLocked(userId, componentName);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void updateCompositorServiceLocked(int userId, android.content.ComponentName componentName) {
        if (this.mCurrentVrCompositorService != null && this.mCurrentVrCompositorService.disconnectIfNotMatching(componentName, userId)) {
            android.util.Slog.i(TAG, "Disconnecting compositor service: " + this.mCurrentVrCompositorService.getComponent());
            this.mCurrentVrCompositorService = null;
        }
        if (componentName != null && this.mCurrentVrCompositorService == null) {
            android.util.Slog.i(TAG, "Connecting compositor service: " + componentName);
            this.mCurrentVrCompositorService = createVrCompositorService(componentName, userId);
            this.mCurrentVrCompositorService.connect();
        }
    }

    private void setPersistentModeAndNotifyListenersLocked(boolean z) {
        if (this.mPersistentVrModeEnabled == z) {
            return;
        }
        java.lang.String str = "Persistent VR mode " + (z ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        android.util.Slog.i(TAG, str);
        logEvent(new com.android.server.vr.VrManagerService.SettingEvent(str));
        this.mPersistentVrModeEnabled = z;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2, this.mPersistentVrModeEnabled ? 1 : 0, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int hasVrPackage(android.content.ComponentName targetPackageName, int userId) {
        int iIsValid;
        synchronized (this.mLock) {
            iIsValid = this.mComponentObserver.isValid(targetPackageName, userId);
        }
        return iIsValid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCurrentVrListener(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            boolean z = false;
            if (this.mCurrentVrService == null) {
                return false;
            }
            if (this.mCurrentVrService.getComponent().getPackageName().equals(packageName) && userId == this.mCurrentVrService.getUserId()) {
                z = true;
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addStateCallback(android.service.vr.IVrStateCallbacks cb) {
        this.mVrStateRemoteCallbacks.register(cb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeStateCallback(android.service.vr.IVrStateCallbacks cb) {
        this.mVrStateRemoteCallbacks.unregister(cb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPersistentStateCallback(android.service.vr.IPersistentVrStateCallbacks cb) {
        this.mPersistentVrStateRemoteCallbacks.register(cb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePersistentStateCallback(android.service.vr.IPersistentVrStateCallbacks cb) {
        this.mPersistentVrStateRemoteCallbacks.unregister(cb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getVrMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mVrModeEnabled;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getPersistentVrMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPersistentVrModeEnabled;
        }
        return z;
    }
}
