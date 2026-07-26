package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbService extends android.hardware.usb.IUsbManager.Stub {
    static final int PACKAGE_MONITOR_OPERATION_ID = 1;
    static final int STRONG_AUTH_OPERATION_ID = 2;
    private static final java.lang.String TAG = "UsbService";
    private final com.android.server.usb.UsbAlsaManager mAlsaManager;
    private final android.content.Context mContext;
    private int mCurrentUserId;
    private com.android.server.usb.UsbDeviceManager mDeviceManager;
    private com.android.server.usb.UsbHostManager mHostManager;
    private final com.android.server.usb.UsbPermissionManager mPermissionManager;
    private com.android.server.usb.UsbPortManager mPortManager;
    private final com.android.server.usb.UsbSettingsManager mSettingsManager;
    private final android.os.UserManager mUserManager;
    private com.android.server.usb.IOplusUsbServiceExt mOplusUsbServiceExt = (com.android.server.usb.IOplusUsbServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.usb.IOplusUsbServiceExt.class).base(this).create();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.Integer>> mUsbDisableRequesters = new android.util.ArrayMap<>();

    public static class Lifecycle extends com.android.server.SystemService {
        private final java.util.concurrent.CompletableFuture<java.lang.Void> mOnActivityManagerPhaseFinished;
        private final java.util.concurrent.CompletableFuture<java.lang.Void> mOnStartFinished;
        private com.android.server.usb.UsbService mUsbService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mOnStartFinished = new java.util.concurrent.CompletableFuture<>();
            this.mOnActivityManagerPhaseFinished = new java.util.concurrent.CompletableFuture<>();
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.usb.UsbService$Lifecycle$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStart$0();
                }
            }, "UsbService$Lifecycle#onStart");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStart$0() {
            this.mUsbService = new com.android.server.usb.UsbService(getContext());
            publishBinderService("usb", this.mUsbService);
            this.mOnStartFinished.complete(null);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.usb.UsbService$Lifecycle$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onBootPhase$1();
                    }
                }, "UsbService$Lifecycle#onBootPhase");
            } else if (phase == 1000) {
                this.mOnActivityManagerPhaseFinished.join();
                this.mUsbService.bootCompleted();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBootPhase$1() {
            this.mOnStartFinished.join();
            this.mUsbService.systemReady();
            this.mOnActivityManagerPhaseFinished.complete(null);
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, final com.android.server.SystemService.TargetUser to) {
            com.android.server.FgThread.getHandler().postAtFrontOfQueue(new java.lang.Runnable() { // from class: com.android.server.usb.UsbService$Lifecycle$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUserSwitching$2(to);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserSwitching$2(com.android.server.SystemService.TargetUser to) {
            this.mUsbService.onSwitchUser(to.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser userInfo) {
            this.mUsbService.onStopUser(userInfo.getUserHandle());
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser userInfo) {
            this.mUsbService.onUnlockUser(userInfo.getUserIdentifier());
        }
    }

    com.android.server.usb.UsbUserSettingsManager getSettingsForUser(int userId) {
        return this.mSettingsManager.getSettingsForUser(userId);
    }

    com.android.server.usb.UsbUserPermissionManager getPermissionsForUser(int userId) {
        return this.mPermissionManager.getPermissionsForUser(userId);
    }

    public UsbService(android.content.Context context) {
        this.mContext = context;
        this.mUserManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        this.mSettingsManager = new com.android.server.usb.UsbSettingsManager(context, this);
        this.mPermissionManager = new com.android.server.usb.UsbPermissionManager(context, this);
        this.mAlsaManager = new com.android.server.usb.UsbAlsaManager(context);
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm.hasSystemFeature("android.hardware.usb.host")) {
            this.mHostManager = new com.android.server.usb.UsbHostManager(context, this.mAlsaManager, this.mPermissionManager);
        }
        if (new java.io.File("/sys/class/android_usb").exists()) {
            this.mDeviceManager = new com.android.server.usb.UsbDeviceManager(context, this.mAlsaManager, this.mSettingsManager, this.mPermissionManager);
        }
        if (this.mHostManager != null || this.mDeviceManager != null) {
            this.mPortManager = new com.android.server.usb.UsbPortManager(context);
        }
        onSwitchUser(0);
        android.content.BroadcastReceiver receiver = new android.content.BroadcastReceiver() { // from class: com.android.server.usb.UsbService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action) && com.android.server.usb.UsbService.this.mDeviceManager != null) {
                    com.android.server.usb.UsbService.this.mDeviceManager.updateUserRestrictions();
                }
            }
        };
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.setPriority(1000);
        filter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        this.mContext.registerReceiverAsUser(receiver, android.os.UserHandle.ALL, filter, null, null);
        this.mOplusUsbServiceExt.hookServiceStart(this.mContext);
    }

    UsbService(android.content.Context context, com.android.server.usb.UsbPortManager usbPortManager, com.android.server.usb.UsbAlsaManager usbAlsaManager, android.os.UserManager userManager, com.android.server.usb.UsbSettingsManager usbSettingsManager) {
        this.mContext = context;
        this.mPortManager = usbPortManager;
        this.mAlsaManager = usbAlsaManager;
        this.mUserManager = userManager;
        this.mSettingsManager = usbSettingsManager;
        this.mPermissionManager = new com.android.server.usb.UsbPermissionManager(context, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSwitchUser(int newUserId) {
        synchronized (this.mLock) {
            this.mCurrentUserId = newUserId;
            com.android.server.usb.UsbProfileGroupSettingsManager settings = this.mSettingsManager.getSettingsForProfileGroup(android.os.UserHandle.of(newUserId));
            if (this.mHostManager != null) {
                this.mHostManager.setCurrentUserSettings(settings);
            }
            if (this.mDeviceManager != null) {
                this.mDeviceManager.setCurrentUser(newUserId, settings);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStopUser(android.os.UserHandle stoppedUser) {
        this.mSettingsManager.remove(stoppedUser);
    }

    public void systemReady() {
        this.mAlsaManager.systemReady();
        if (this.mDeviceManager != null) {
            this.mDeviceManager.systemReady();
        }
        if (this.mHostManager != null) {
            this.mHostManager.systemReady();
        }
        if (this.mPortManager != null) {
            this.mPortManager.systemReady();
        }
    }

    public void bootCompleted() {
        if (this.mDeviceManager != null) {
            this.mDeviceManager.bootCompleted();
        }
        if (com.android.internal.hidden_from_bootclasspath.android.hardware.usb.flags.Flags.enableUsbDataSignalStaking()) {
            new com.android.server.usb.UsbService.PackageUninstallMonitor().register(this.mContext, android.os.UserHandle.ALL, com.android.internal.os.BackgroundThread.getHandler());
            new com.android.internal.widget.LockPatternUtils(this.mContext).registerStrongAuthTracker(new com.android.server.usb.UsbService.StrongAuthTracker(this.mContext, com.android.internal.os.BackgroundThread.getHandler().getLooper()));
        }
    }

    public void onUnlockUser(int user) {
        if (this.mDeviceManager != null) {
            this.mDeviceManager.onUnlockUser(user);
        }
    }

    public void getDeviceList(android.os.Bundle devices) {
        if (this.mHostManager != null) {
            this.mHostManager.getDeviceList(devices);
        }
    }

    public android.os.ParcelFileDescriptor openDevice(java.lang.String deviceName, java.lang.String packageName) {
        android.os.ParcelFileDescriptor fd = null;
        if (this.mHostManager != null && deviceName != null) {
            int uid = android.os.Binder.getCallingUid();
            int pid = android.os.Binder.getCallingPid();
            int user = android.os.UserHandle.getUserId(uid);
            long ident = clearCallingIdentity();
            try {
                synchronized (this.mLock) {
                    if (this.mUserManager.isSameProfileGroup(user, this.mCurrentUserId)) {
                        fd = this.mHostManager.openDevice(deviceName, getPermissionsForUser(user), packageName, pid, uid);
                    } else {
                        android.util.Slog.w(TAG, "Cannot open " + deviceName + " for user " + user + " as user is not active.");
                    }
                }
            } finally {
                restoreCallingIdentity(ident);
            }
        }
        return fd;
    }

    public android.hardware.usb.UsbAccessory getCurrentAccessory() {
        if (this.mDeviceManager != null) {
            return this.mDeviceManager.getCurrentAccessory();
        }
        return null;
    }

    public android.os.ParcelFileDescriptor openAccessory(android.hardware.usb.UsbAccessory accessory) {
        if (this.mDeviceManager != null) {
            int uid = android.os.Binder.getCallingUid();
            int pid = android.os.Binder.getCallingPid();
            int user = android.os.UserHandle.getUserId(uid);
            long ident = clearCallingIdentity();
            try {
                synchronized (this.mLock) {
                    if (this.mUserManager.isSameProfileGroup(user, this.mCurrentUserId)) {
                        return this.mDeviceManager.openAccessory(accessory, getPermissionsForUser(user), pid, uid);
                    }
                    android.util.Slog.w(TAG, "Cannot open " + accessory + " for user " + user + " as user is not active.");
                    return null;
                }
            } finally {
                restoreCallingIdentity(ident);
            }
        }
        return null;
    }

    public android.os.ParcelFileDescriptor getControlFd(long function) {
        getControlFd_enforcePermission();
        return this.mDeviceManager.getControlFd(function);
    }

    public void setDevicePackage(android.hardware.usb.UsbDevice device, java.lang.String packageName, int userId) {
        java.util.Objects.requireNonNull(device);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettingsManager.getSettingsForProfileGroup(user).setDevicePackage(device, packageName, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setAccessoryPackage(android.hardware.usb.UsbAccessory accessory, java.lang.String packageName, int userId) {
        java.util.Objects.requireNonNull(accessory);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettingsManager.getSettingsForProfileGroup(user).setAccessoryPackage(accessory, packageName, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void addDevicePackagesToPreferenceDenied(android.hardware.usb.UsbDevice device, java.lang.String[] packageNames, android.os.UserHandle user) {
        java.util.Objects.requireNonNull(device);
        java.lang.String[] packageNames2 = (java.lang.String[]) com.android.internal.util.Preconditions.checkArrayElementsNotNull(packageNames, com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY);
        java.util.Objects.requireNonNull(user);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettingsManager.getSettingsForProfileGroup(user).addDevicePackagesToDenied(device, packageNames2, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void addAccessoryPackagesToPreferenceDenied(android.hardware.usb.UsbAccessory accessory, java.lang.String[] packageNames, android.os.UserHandle user) {
        java.util.Objects.requireNonNull(accessory);
        java.lang.String[] packageNames2 = (java.lang.String[]) com.android.internal.util.Preconditions.checkArrayElementsNotNull(packageNames, com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY);
        java.util.Objects.requireNonNull(user);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettingsManager.getSettingsForProfileGroup(user).addAccessoryPackagesToDenied(accessory, packageNames2, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void removeDevicePackagesFromPreferenceDenied(android.hardware.usb.UsbDevice device, java.lang.String[] packageNames, android.os.UserHandle user) {
        java.util.Objects.requireNonNull(device);
        java.lang.String[] packageNames2 = (java.lang.String[]) com.android.internal.util.Preconditions.checkArrayElementsNotNull(packageNames, com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY);
        java.util.Objects.requireNonNull(user);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettingsManager.getSettingsForProfileGroup(user).removeDevicePackagesFromDenied(device, packageNames2, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void removeAccessoryPackagesFromPreferenceDenied(android.hardware.usb.UsbAccessory accessory, java.lang.String[] packageNames, android.os.UserHandle user) {
        java.util.Objects.requireNonNull(accessory);
        java.lang.String[] packageNames2 = (java.lang.String[]) com.android.internal.util.Preconditions.checkArrayElementsNotNull(packageNames, com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY);
        java.util.Objects.requireNonNull(user);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettingsManager.getSettingsForProfileGroup(user).removeAccessoryPackagesFromDenied(accessory, packageNames2, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setDevicePersistentPermission(android.hardware.usb.UsbDevice device, int uid, android.os.UserHandle user, boolean shouldBeGranted) {
        java.util.Objects.requireNonNull(device);
        java.util.Objects.requireNonNull(user);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mPermissionManager.getPermissionsForUser(user).setDevicePersistentPermission(device, uid, shouldBeGranted);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setAccessoryPersistentPermission(android.hardware.usb.UsbAccessory accessory, int uid, android.os.UserHandle user, boolean shouldBeGranted) {
        java.util.Objects.requireNonNull(accessory);
        java.util.Objects.requireNonNull(user);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mPermissionManager.getPermissionsForUser(user).setAccessoryPersistentPermission(accessory, uid, shouldBeGranted);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean hasDevicePermission(android.hardware.usb.UsbDevice device, java.lang.String packageName) {
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userId = android.os.UserHandle.getUserId(uid);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return getPermissionsForUser(userId).hasPermission(device, packageName, pid, uid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean hasDevicePermissionWithIdentity(android.hardware.usb.UsbDevice device, java.lang.String packageName, int pid, int uid) {
        hasDevicePermissionWithIdentity_enforcePermission();
        int userId = android.os.UserHandle.getUserId(uid);
        return getPermissionsForUser(userId).hasPermission(device, packageName, pid, uid);
    }

    public boolean hasAccessoryPermission(android.hardware.usb.UsbAccessory accessory) {
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userId = android.os.UserHandle.getUserId(uid);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return getPermissionsForUser(userId).hasPermission(accessory, pid, uid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean hasAccessoryPermissionWithIdentity(android.hardware.usb.UsbAccessory accessory, int pid, int uid) {
        hasAccessoryPermissionWithIdentity_enforcePermission();
        int userId = android.os.UserHandle.getUserId(uid);
        return getPermissionsForUser(userId).hasPermission(accessory, pid, uid);
    }

    public void requestDevicePermission(android.hardware.usb.UsbDevice device, java.lang.String packageName, android.app.PendingIntent pi) {
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userId = android.os.UserHandle.getUserId(uid);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            getPermissionsForUser(userId).requestPermission(device, packageName, pi, pid, uid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void requestAccessoryPermission(android.hardware.usb.UsbAccessory accessory, java.lang.String packageName, android.app.PendingIntent pi) {
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userId = android.os.UserHandle.getUserId(uid);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            getPermissionsForUser(userId).requestPermission(accessory, packageName, pi, pid, uid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void grantDevicePermission(android.hardware.usb.UsbDevice device, int uid) {
        grantDevicePermission_enforcePermission();
        int userId = android.os.UserHandle.getUserId(uid);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            getPermissionsForUser(userId).grantDevicePermission(device, uid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void grantAccessoryPermission(android.hardware.usb.UsbAccessory accessory, int uid) {
        grantAccessoryPermission_enforcePermission();
        int userId = android.os.UserHandle.getUserId(uid);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            getPermissionsForUser(userId).grantAccessoryPermission(accessory, uid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean hasDefaults(java.lang.String packageName, int userId) {
        java.lang.String packageName2 = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty(packageName);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return this.mSettingsManager.getSettingsForProfileGroup(user).hasDefaults(packageName2, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void clearDefaults(java.lang.String packageName, int userId) {
        java.lang.String packageName2 = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty(packageName);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSettingsManager.getSettingsForProfileGroup(user).clearDefaults(packageName2, user);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setCurrentFunctions(long functions, int operationId) {
        setCurrentFunctions_enforcePermission();
        com.android.internal.util.Preconditions.checkArgument(android.hardware.usb.UsbManager.areSettableFunctions(functions));
        com.android.internal.util.Preconditions.checkState(this.mDeviceManager != null);
        this.mDeviceManager.setCurrentFunctions(functions, operationId);
    }

    public void setCurrentFunction(java.lang.String functions, boolean usbDataUnlocked, int operationId) {
        setCurrentFunctions(android.hardware.usb.UsbManager.usbFunctionsFromString(functions), operationId);
    }

    public boolean isFunctionEnabled(java.lang.String function) {
        return (getCurrentFunctions() & android.hardware.usb.UsbManager.usbFunctionsFromString(function)) != 0;
    }

    public long getCurrentFunctions() {
        getCurrentFunctions_enforcePermission();
        com.android.internal.util.Preconditions.checkState(this.mDeviceManager != null);
        return this.mDeviceManager.getCurrentFunctions();
    }

    public void setScreenUnlockedFunctions(long functions) {
        setScreenUnlockedFunctions_enforcePermission();
        com.android.internal.util.Preconditions.checkArgument(android.hardware.usb.UsbManager.areSettableFunctions(functions));
        com.android.internal.util.Preconditions.checkState(this.mDeviceManager != null);
        this.mDeviceManager.setScreenUnlockedFunctions(functions);
    }

    public long getScreenUnlockedFunctions() {
        getScreenUnlockedFunctions_enforcePermission();
        com.android.internal.util.Preconditions.checkState(this.mDeviceManager != null);
        return this.mDeviceManager.getScreenUnlockedFunctions();
    }

    public int getCurrentUsbSpeed() {
        getCurrentUsbSpeed_enforcePermission();
        com.android.internal.util.Preconditions.checkNotNull(this.mDeviceManager, "DeviceManager must not be null");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mDeviceManager.getCurrentUsbSpeed();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public int getGadgetHalVersion() {
        getGadgetHalVersion_enforcePermission();
        com.android.internal.util.Preconditions.checkNotNull(this.mDeviceManager, "DeviceManager must not be null");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mDeviceManager.getGadgetHalVersion();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void resetUsbGadget() {
        resetUsbGadget_enforcePermission();
        com.android.internal.util.Preconditions.checkNotNull(this.mDeviceManager, "DeviceManager must not be null");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDeviceManager.resetUsbGadget();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void resetUsbPort(java.lang.String portId, int operationId, android.hardware.usb.IUsbOperationInternal callback) {
        java.util.Objects.requireNonNull(portId, "resetUsbPort: portId must not be null. opId:" + operationId);
        java.util.Objects.requireNonNull(callback, "resetUsbPort: callback must not be null. opId:" + operationId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                this.mPortManager.resetUsbPort(portId, operationId, callback, null);
            } else {
                try {
                    callback.onOperationComplete(1);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "resetUsbPort: Failed to call onOperationComplete", e);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public java.util.List<android.hardware.usb.ParcelableUsbPort> getPorts() {
        getPorts_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                android.hardware.usb.UsbPort[] ports = this.mPortManager.getPorts();
                java.util.ArrayList<android.hardware.usb.ParcelableUsbPort> parcelablePorts = new java.util.ArrayList<>();
                for (android.hardware.usb.UsbPort usbPort : ports) {
                    parcelablePorts.add(android.hardware.usb.ParcelableUsbPort.of(usbPort));
                }
                return parcelablePorts;
            }
            android.os.Binder.restoreCallingIdentity(ident);
            return null;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public android.hardware.usb.UsbPortStatus getPortStatus(java.lang.String portId) {
        java.util.Objects.requireNonNull(portId, "portId must not be null");
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mPortManager != null ? this.mPortManager.getPortStatus(portId) : null;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean isModeChangeSupported(java.lang.String portId) {
        isModeChangeSupported_enforcePermission();
        java.util.Objects.requireNonNull(portId, "portId must not be null");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mPortManager != null ? this.mPortManager.isModeChangeSupported(portId) : false;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setPortRoles(java.lang.String portId, int powerRole, int dataRole) {
        java.util.Objects.requireNonNull(portId, "portId must not be null");
        android.hardware.usb.UsbPort.checkRoles(powerRole, dataRole);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                this.mPortManager.setPortRoles(portId, powerRole, dataRole, null);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void enableLimitPowerTransfer(java.lang.String portId, boolean limit, int operationId, android.hardware.usb.IUsbOperationInternal callback) {
        java.util.Objects.requireNonNull(portId, "portId must not be null. opID:" + operationId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                this.mPortManager.enableLimitPowerTransfer(portId, limit, operationId, callback, null);
            } else {
                try {
                    callback.onOperationComplete(1);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "enableLimitPowerTransfer: Failed to call onOperationComplete", e);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void enableContaminantDetection(java.lang.String portId, boolean enable) {
        java.util.Objects.requireNonNull(portId, "portId must not be null");
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                this.mPortManager.enableContaminantDetection(portId, enable, null);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public int getUsbHalVersion() {
        getUsbHalVersion_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                return this.mPortManager.getUsbHalVersion();
            }
            android.os.Binder.restoreCallingIdentity(ident);
            return -1;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean enableUsbData(java.lang.String portId, boolean enable, int operationId, android.hardware.usb.IUsbOperationInternal callback) {
        return enableUsbDataInternal(portId, enable, operationId, callback, android.os.Binder.getCallingUid());
    }

    boolean enableUsbDataInternal(java.lang.String portId, boolean enable, int operationId, android.hardware.usb.IUsbOperationInternal callback, int callerUid) {
        boolean wait;
        java.util.Objects.requireNonNull(portId, "enableUsbData: portId must not be null. opId:" + operationId);
        java.util.Objects.requireNonNull(callback, "enableUsbData: callback must not be null. opId:" + operationId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        if (com.android.internal.hidden_from_bootclasspath.android.hardware.usb.flags.Flags.enableUsbDataSignalStaking() && !shouldUpdateUsbSignaling(portId, enable, callerUid)) {
            try {
                callback.onOperationComplete(1);
                return false;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "enableUsbData: Failed to call onOperationComplete", e);
                return false;
            }
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                wait = this.mPortManager.enableUsbData(portId, enable, operationId, callback, null);
            } else {
                try {
                    callback.onOperationComplete(1);
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(TAG, "enableUsbData: Failed to call onOperationComplete", e2);
                }
                wait = false;
            }
            return wait;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private boolean shouldUpdateUsbSignaling(java.lang.String portId, boolean enable, int uid) {
        synchronized (this.mUsbDisableRequesters) {
            if (!this.mUsbDisableRequesters.containsKey(portId)) {
                this.mUsbDisableRequesters.put(portId, new android.util.ArraySet<>());
            }
            android.util.ArraySet<java.lang.Integer> uidsOfDisableRequesters = this.mUsbDisableRequesters.get(portId);
            if (enable) {
                uidsOfDisableRequesters.remove(java.lang.Integer.valueOf(uid));
                return uidsOfDisableRequesters.isEmpty();
            }
            uidsOfDisableRequesters.add(java.lang.Integer.valueOf(uid));
            return true;
        }
    }

    public void enableUsbDataWhileDocked(java.lang.String portId, int operationId, android.hardware.usb.IUsbOperationInternal callback) {
        enableUsbDataWhileDockedInternal(portId, operationId, callback, android.os.Binder.getCallingUid());
    }

    void enableUsbDataWhileDockedInternal(java.lang.String portId, int operationId, android.hardware.usb.IUsbOperationInternal callback, int callerUid) {
        java.util.Objects.requireNonNull(portId, "enableUsbDataWhileDocked: portId must not be null. opId:" + operationId);
        java.util.Objects.requireNonNull(callback, "enableUsbDataWhileDocked: callback must not be null. opId:" + operationId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        if (com.android.internal.hidden_from_bootclasspath.android.hardware.usb.flags.Flags.enableUsbDataSignalStaking() && !shouldUpdateUsbSignaling(portId, true, callerUid)) {
            try {
                callback.onOperationComplete(1);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "enableUsbDataWhileDocked: Failed to call onOperationComplete", e);
                return;
            }
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                this.mPortManager.enableUsbDataWhileDocked(portId, operationId, callback, null);
            } else {
                try {
                    callback.onOperationComplete(1);
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(TAG, "enableUsbData: Failed to call onOperationComplete", e2);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setUsbDeviceConnectionHandler(android.content.ComponentName usbDeviceConnectionHandler) {
        setUsbDeviceConnectionHandler_enforcePermission();
        synchronized (this.mLock) {
            if (this.mCurrentUserId == android.os.UserHandle.getCallingUserId()) {
                if (this.mHostManager != null) {
                    this.mHostManager.setUsbDeviceConnectionHandler(usbDeviceConnectionHandler);
                }
            } else {
                throw new java.lang.IllegalArgumentException("Only the current user can register a usb connection handler");
            }
        }
    }

    public boolean registerForDisplayPortEvents(android.hardware.usb.IDisplayPortAltModeInfoListener listener) {
        java.util.Objects.requireNonNull(listener, "registerForDisplayPortEvents: listener must not be null.");
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                return this.mPortManager.registerForDisplayPortEvents(listener);
            }
            android.os.Binder.restoreCallingIdentity(ident);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void unregisterForDisplayPortEvents(android.hardware.usb.IDisplayPortAltModeInfoListener listener) {
        java.util.Objects.requireNonNull(listener, "unregisterForDisplayPortEvents: listener must not be null.");
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USB", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mPortManager != null) {
                this.mPortManager.unregisterForDisplayPortEvents(listener);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0293  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x015e  */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void dump(java.io.FileDescriptor r30, java.io.PrintWriter r31, java.lang.String[] r32) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2210
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usb.UsbService.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    private static java.lang.String removeLastChar(java.lang.String value) {
        return value.substring(0, value.length() - 1);
    }

    private class PackageUninstallMonitor extends com.android.internal.content.PackageMonitor {
        private PackageUninstallMonitor() {
        }

        public void onUidRemoved(int uid) {
            synchronized (com.android.server.usb.UsbService.this.mUsbDisableRequesters) {
                for (java.lang.String portId : com.android.server.usb.UsbService.this.mUsbDisableRequesters.keySet()) {
                    android.util.ArraySet<java.lang.Integer> disabledUid = (android.util.ArraySet) com.android.server.usb.UsbService.this.mUsbDisableRequesters.get(portId);
                    if (disabledUid != null) {
                        disabledUid.remove(java.lang.Integer.valueOf(uid));
                        if (disabledUid.isEmpty()) {
                            com.android.server.usb.UsbService.this.enableUsbData(portId, true, 1, new android.hardware.usb.IUsbOperationInternal.Default());
                        }
                    }
                }
            }
        }
    }

    private class StrongAuthTracker extends com.android.internal.widget.LockPatternUtils.StrongAuthTracker {
        private boolean mLockdownModeStatus;

        StrongAuthTracker(android.content.Context context, android.os.Looper looper) {
            super(context, looper);
        }

        public synchronized void onStrongAuthRequiredChanged(int userId) {
            boolean lockDownTriggeredByUser = (getStrongAuthForUser(userId) & 32) != 0;
            if (this.mLockdownModeStatus == lockDownTriggeredByUser) {
                return;
            }
            this.mLockdownModeStatus = lockDownTriggeredByUser;
            for (android.hardware.usb.UsbPort port : com.android.server.usb.UsbService.this.mPortManager.getPorts()) {
                com.android.server.usb.UsbService.this.enableUsbData(port.getId(), !lockDownTriggeredByUser, 2, new android.hardware.usb.IUsbOperationInternal.Default());
            }
        }
    }
}
