package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothManagerService {
    private static final int ACTIVE_LOG_MAX_SIZE = 20;
    static final int BLUETOOTH_OFF = 0;
    static final int BLUETOOTH_ON_AIRPLANE = 2;
    static final int BLUETOOTH_ON_BLUETOOTH = 1;
    private static final int BOOT_COMPLETE_AUTO_ENABLE_DELAY = 0;
    private static final int CRASH_LOG_MAX_SIZE = 100;
    private static final int MAX_ERROR_RESTART_RETRIES = 6;
    private static final int MAX_WAIT_FOR_ENABLE_DISABLE_RETRIES = 10;
    static final int MESSAGE_BLUETOOTH_SERVICE_CONNECTED = 40;
    static final int MESSAGE_BLUETOOTH_SERVICE_DISCONNECTED = 41;
    static final int MESSAGE_BLUETOOTH_STATE_CHANGE = 60;
    static final int MESSAGE_DISABLE = 2;
    static final int MESSAGE_ENABLE = 1;
    static final int MESSAGE_GET_NAME_AND_ADDRESS = 200;
    static final int MESSAGE_HANDLE_DISABLE_DELAYED = 4;
    static final int MESSAGE_HANDLE_ENABLE_DELAYED = 3;
    static final int MESSAGE_OPLUS_BACKUP_RESTORE = 501;
    static final int MESSAGE_RESTART_BLUETOOTH_SERVICE = 42;
    static final int MESSAGE_RESTORE_USER_SETTING = 500;
    static final int MESSAGE_TIMEOUT_BIND = 100;
    static final int MESSAGE_USER_SWITCHED = 300;
    static final int MESSAGE_USER_UNLOCKED = 301;
    private static final java.lang.String OPLUS_BACKUP_RESTORE_END_ACTION = "oplus.intent.action.change.over.restore.end";
    private static final java.lang.String PACKAGE_NAME_OSHARE = "com.coloros.oshare";
    private static final int RESTORE_SETTING_TO_OFF = 0;
    private static final int RESTORE_SETTING_TO_ON = 1;
    private boolean DBG;
    private final java.util.LinkedList<com.android.server.bluetooth.BluetoothManagerService.ActiveLog> mActiveLogs;
    private com.android.server.bluetooth.AdapterBinder mAdapter;
    private final java.util.concurrent.locks.ReentrantReadWriteLock mAdapterLock;
    private java.lang.String mAddress;
    private final com.android.server.bluetooth.BluetoothServiceBinder mBinder;
    private java.util.Map<android.os.IBinder, com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient> mBleApps;
    private final android.bluetooth.IBluetoothCallback mBluetoothCallback;
    private com.android.server.bluetooth.BluetoothManagerService.BluetoothManagerServiceWrapper mBmsWrapper;
    private final android.os.RemoteCallbackList<android.bluetooth.IBluetoothManagerCallback> mCallbacks;
    private com.android.server.bluetooth.BluetoothManagerService.BluetoothServiceConnection mConnection;
    private final android.content.ContentResolver mContentResolver;
    private final android.content.Context mContext;
    private final java.util.LinkedList<java.lang.Long> mCrashTimestamps;
    private int mCrashes;
    private android.content.Context mCurrentUserContext;
    private final boolean mDeviceConfigAllowAutoOn;
    private boolean mEnable;
    private boolean mEnableExternal;
    private int mErrorRecoveryRetryCounter;
    private final com.android.server.bluetooth.BluetoothManagerService.BluetoothHandler mHandler;
    private final boolean mIsHearingAidProfileSupported;
    private long mLastEnabledTime;
    private final android.os.Looper mLooper;
    private java.lang.String mName;
    com.android.server.bluetooth.IOplusBluetoothManagerServiceExt mOplusBms;
    private boolean mQuietEnable;
    private boolean mQuietEnableExternal;
    private final android.content.BroadcastReceiver mReceiver;
    private boolean mShutdownInProgress;
    private final com.android.server.bluetooth.BluetoothAdapterState mState;
    private final android.os.UserManager mUserManager;
    private int mWaitForDisableRetry;
    private int mWaitForEnableRetry;
    private static final java.lang.String TAG = com.android.server.bluetooth.BluetoothManagerService.class.getSimpleName();
    private static final int HW_MULTIPLIER = android.os.SystemProperties.getInt("ro.hw_timeout_multiplier", 1);
    private static final int TIMEOUT_BIND_MS = HW_MULTIPLIER * 4000;
    private static final java.time.Duration STATE_TIMEOUT = java.time.Duration.ofSeconds(HW_MULTIPLIER * 4);
    private static final int SERVICE_RESTART_TIME_MS = HW_MULTIPLIER * 400;
    private static final int ERROR_RESTART_TIME_MS = HW_MULTIPLIER * 3000;
    private static final int USER_SWITCHED_TIME_MS = HW_MULTIPLIER * 200;
    private static final int ADD_PROXY_DELAY_MS = HW_MULTIPLIER * 100;
    private static final int ENABLE_DISABLE_DELAY_MS = HW_MULTIPLIER * 300;
    private static final java.lang.Object ON_AIRPLANE_MODE_CHANGED_TOKEN = new java.lang.Object();
    private static final java.lang.Object ON_SATELLITE_MODE_CHANGED_TOKEN = new java.lang.Object();
    private static final java.lang.Object ON_SWITCH_USER_TOKEN = new java.lang.Object();

    static java.lang.String timeToLog(long timestamp) {
        return java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS").withZone(java.time.ZoneId.systemDefault()).format(java.time.Instant.ofEpochMilli(timestamp));
    }

    private static class ActiveLog {
        private boolean mEnable;
        private boolean mIsBle;
        private java.lang.String mPackageName;
        private int mReason;
        private long mTimestamp;

        ActiveLog(int reason, java.lang.String packageName, boolean enable, boolean isBle, long timestamp) {
            this.mReason = reason;
            this.mPackageName = packageName;
            this.mEnable = enable;
            this.mIsBle = isBle;
            this.mTimestamp = timestamp;
            com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, toString());
        }

        public java.lang.String toString() {
            return com.android.server.bluetooth.BluetoothManagerService.timeToLog(this.mTimestamp) + " \tPackage [" + this.mPackageName + "] requested to [" + (this.mEnable ? "Enable" : "Disable") + (this.mIsBle ? "Ble" : "") + "]. \tReason is " + com.android.server.bluetooth.BluetoothManagerService.getEnableDisableReasonString(this.mReason);
        }

        long getTimestamp() {
            return this.mTimestamp;
        }

        boolean getEnable() {
            return this.mEnable;
        }

        void dump(android.util.proto.ProtoOutputStream proto) {
            proto.write(1112396529665L, this.mTimestamp);
            proto.write(1133871366146L, this.mEnable);
            proto.write(1138166333443L, this.mPackageName);
            proto.write(1159641169924L, this.mReason);
        }
    }

    public void onUserRestrictionsChanged(android.os.UserHandle userHandle) {
        boolean newBluetoothDisallowed = this.mUserManager.hasUserRestrictionForUser("no_bluetooth", userHandle);
        boolean newBluetoothSharingDisallowed = this.mUserManager.hasUserRestrictionForUser("no_bluetooth_sharing", userHandle) || newBluetoothDisallowed;
        updateOppLauncherComponentState(userHandle, newBluetoothSharingDisallowed);
        if (android.os.UserHandle.SYSTEM.equals(userHandle) && newBluetoothDisallowed) {
            sendDisableMsg(3);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x005a A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean onFactoryReset() {
        /*
            Method dump skipped, instruction units count: 318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.bluetooth.BluetoothManagerService.onFactoryReset():boolean");
    }

    private int estimateBusyTime(int state) {
        if (state == 15 && isBluetoothPersistedStateOn()) {
            return SERVICE_RESTART_TIME_MS;
        }
        if (state != 12 && state != 10 && state != 15) {
            return ADD_PROXY_DELAY_MS;
        }
        if (this.mHandler.hasMessages(1) || this.mHandler.hasMessages(2) || this.mHandler.hasMessages(3) || this.mHandler.hasMessages(4) || this.mHandler.hasMessages(42) || this.mHandler.hasMessages(100)) {
            com.android.server.bluetooth.Log.d(TAG, "Busy reason: ENABLE=" + this.mHandler.hasMessages(1) + " DISABLE=" + this.mHandler.hasMessages(2) + " HANDLE_ENABLE_DELAYED=" + this.mHandler.hasMessages(3) + " HANDLE_DISABLE_DELAYED=" + this.mHandler.hasMessages(4) + " RESTART_BLUETOOTH_SERVICE=" + this.mHandler.hasMessages(42) + " TIMEOUT_BIND=" + this.mHandler.hasMessages(100));
            return SERVICE_RESTART_TIME_MS;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: delayModeChangedIfNeeded, reason: merged with bridge method [inline-methods] */
    public void lambda$delayModeChangedIfNeeded$0(final java.lang.Object token, final java.lang.Runnable r, final java.lang.String modechanged) {
        int state = getState();
        int delayMs = estimateBusyTime(state);
        com.android.server.bluetooth.Log.d(TAG, "delayModeChangedIfNeeded(" + modechanged + "): state=" + android.bluetooth.BluetoothAdapter.nameForState(state) + " Airplane.isOnOverrode=" + com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode() + " Airplane.isOn=" + com.android.server.bluetooth.airplane.AirplaneModeListener.isOn() + " isSatelliteModeOn()=" + isSatelliteModeOn() + " delayed=" + delayMs + "ms");
        this.mHandler.removeCallbacksAndMessages(token);
        if (delayMs > 0) {
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$delayModeChangedIfNeeded$0(token, r, modechanged);
                }
            }, token, delayMs);
        } else {
            r.run();
        }
    }

    kotlin.Unit sendToggleNotification(java.lang.String notificationReason) {
        android.content.Intent intent = new android.content.Intent("android.bluetooth.notification.action.SEND_TOGGLE_NOTIFICATION");
        intent.setComponent(resolveSystemService(intent));
        intent.putExtra("android.bluetooth.notification.extra.NOTIFICATION_REASON", notificationReason);
        this.mCurrentUserContext.startService(intent);
        return kotlin.Unit.INSTANCE;
    }

    kotlin.Unit onAirplaneModeChanged(final boolean isAirplaneModeOn) {
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAirplaneModeChanged$2(isAirplaneModeOn);
            }
        }, ON_AIRPLANE_MODE_CHANGED_TOKEN, 0L);
        return kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAirplaneModeChanged$2(final boolean isAirplaneModeOn) {
        lambda$delayModeChangedIfNeeded$0(ON_AIRPLANE_MODE_CHANGED_TOKEN, new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAirplaneModeChanged$1(isAirplaneModeOn);
            }
        }, "onAirplaneModeChanged");
    }

    kotlin.Unit onSatelliteModeChanged(final boolean isSatelliteModeOn) {
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSatelliteModeChanged$4(isSatelliteModeOn);
            }
        }, ON_SATELLITE_MODE_CHANGED_TOKEN, 0L);
        return kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSatelliteModeChanged$4(final boolean isSatelliteModeOn) {
        lambda$delayModeChangedIfNeeded$0(ON_SATELLITE_MODE_CHANGED_TOKEN, new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSatelliteModeChanged$3(isSatelliteModeOn);
            }
        }, "onSatelliteModeChanged");
    }

    void onSwitchUser(final android.os.UserHandle userHandle) {
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSwitchUser$6(userHandle);
            }
        }, ON_SWITCH_USER_TOKEN, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSwitchUser$6(final android.os.UserHandle userHandle) {
        lambda$delayModeChangedIfNeeded$0(ON_SWITCH_USER_TOKEN, new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSwitchUser$5(userHandle);
            }
        }, "onSwitchUser");
    }

    private void forceToOffFromModeChange(int currentState, int reason) {
        clearBleApps();
        if (reason == 12 || !com.android.server.bluetooth.airplane.AirplaneModeListener.hasUserToggledApm(this.mCurrentUserContext)) {
            com.android.server.bluetooth.AutoOnFeature.pause();
        }
        if (currentState == 12) {
            sendDisableMsg(reason);
            return;
        }
        if (currentState == 15) {
            this.mAdapterLock.readLock().lock();
            try {
                try {
                    if (this.mAdapter != null) {
                        addActiveLog(reason, false);
                        this.mAdapter.stopBle(this.mContext.getAttributionSource());
                        this.mEnable = false;
                        this.mEnableExternal = false;
                    }
                } catch (android.os.RemoteException e) {
                    com.android.server.bluetooth.Log.e(TAG, "Unable to call stopBle", e);
                }
            } finally {
                this.mAdapterLock.readLock().unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleAirplaneModeChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onAirplaneModeChanged$1(boolean isAirplaneModeOn) {
        synchronized (this) {
            if (isBluetoothPersistedStateOn()) {
                if (isAirplaneModeOn) {
                    setBluetoothPersistedState(2);
                } else {
                    setBluetoothPersistedState(1);
                }
            }
            int currentState = this.mState.get();
            com.android.server.bluetooth.Log.d(TAG, "handleAirplaneModeChanged(" + isAirplaneModeOn + "): currentState=" + android.bluetooth.BluetoothAdapter.nameForState(currentState));
            if (isAirplaneModeOn) {
                forceToOffFromModeChange(currentState, 2);
            } else if (this.mEnableExternal) {
                sendEnableMsg(this.mQuietEnableExternal, 2);
            } else if (currentState != 12) {
                autoOnSetupTimer();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleSatelliteModeChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onSatelliteModeChanged$3(boolean isSatelliteModeOn) {
        int currentState = this.mState.get();
        if (shouldBluetoothBeOn(isSatelliteModeOn) && currentState != 12) {
            sendEnableMsg(this.mQuietEnableExternal, 12);
            return;
        }
        if (!shouldBluetoothBeOn(isSatelliteModeOn) && currentState != 10) {
            forceToOffFromModeChange(currentState, 12);
        } else if (!isSatelliteModeOn && !shouldBluetoothBeOn(isSatelliteModeOn) && currentState != 12) {
            autoOnSetupTimer();
        }
    }

    private boolean shouldBluetoothBeOn(boolean isSatelliteModeOn) {
        if (!isBluetoothPersistedStateOn()) {
            com.android.server.bluetooth.Log.d(TAG, "shouldBluetoothBeOn: User want BT off.");
            return false;
        }
        if (isSatelliteModeOn) {
            com.android.server.bluetooth.Log.d(TAG, "shouldBluetoothBeOn: BT should be off as satellite mode is on.");
            return false;
        }
        if (com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode() && isBluetoothPersistedStateOnAirplane()) {
            com.android.server.bluetooth.Log.d(TAG, "shouldBluetoothBeOn: BT should be off as airplaneMode is on.");
            return false;
        }
        com.android.server.bluetooth.Log.d(TAG, "shouldBluetoothBeOn: BT should be on.");
        return true;
    }

    BluetoothManagerService(android.content.Context context, android.os.Looper looper) {
        boolean z = false;
        this.DBG = !android.os.SystemProperties.getBoolean("ro.build.release_type", false) || android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
        this.mOplusBms = null;
        this.mAddress = null;
        this.mName = null;
        this.mCallbacks = new android.os.RemoteCallbackList<>();
        this.mAdapterLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
        this.mAdapter = null;
        this.mQuietEnable = false;
        this.mEnable = false;
        this.mShutdownInProgress = false;
        this.mCurrentUserContext = null;
        this.mActiveLogs = new java.util.LinkedList<>();
        this.mCrashTimestamps = new java.util.LinkedList<>();
        this.mCrashes = 0;
        this.mQuietEnableExternal = false;
        this.mEnableExternal = false;
        this.mBleApps = new java.util.concurrent.ConcurrentHashMap();
        this.mState = new com.android.server.bluetooth.BluetoothAdapterState();
        this.mErrorRecoveryRetryCounter = 0;
        this.mBluetoothCallback = new android.bluetooth.IBluetoothCallback.Stub() { // from class: com.android.server.bluetooth.BluetoothManagerService.1
            public void onBluetoothStateChange(int prevState, int newState) throws android.os.RemoteException {
                com.android.server.bluetooth.BluetoothManagerService.this.mHandler.obtainMessage(60, prevState, newState).sendToTarget();
            }
        };
        this.mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.bluetooth.BluetoothManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if ("android.bluetooth.adapter.action.LOCAL_NAME_CHANGED".equals(action)) {
                    java.lang.String newName = intent.getStringExtra("android.bluetooth.adapter.extra.LOCAL_NAME");
                    if (newName != null) {
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "Bluetooth Adapter name changed to " + newName);
                        com.android.server.bluetooth.BluetoothManagerService.this.storeNameAndAddress(newName, null);
                        return;
                    }
                    return;
                }
                if ("android.bluetooth.adapter.action.BLUETOOTH_ADDRESS_CHANGED".equals(action)) {
                    java.lang.String newAddress = intent.getStringExtra("android.bluetooth.adapter.extra.BLUETOOTH_ADDRESS");
                    if (newAddress != null) {
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "Local address changed to …" + com.android.server.bluetooth.BluetoothManagerService.this.logAddress(newAddress));
                        com.android.server.bluetooth.BluetoothManagerService.this.storeNameAndAddress(null, newAddress);
                        return;
                    } else {
                        com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "No Bluetooth Adapter address parameter found");
                        return;
                    }
                }
                if ("android.os.action.SETTING_RESTORED".equals(action)) {
                    java.lang.String name = intent.getStringExtra("setting_name");
                    if ("bluetooth_on".equals(name)) {
                        java.lang.String prevValue = intent.getStringExtra("previous_value");
                        java.lang.String newValue = intent.getStringExtra("new_value");
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "ACTION_SETTING_RESTORED with BLUETOOTH_ON prevValue=" + prevValue + " newValue=" + newValue);
                        if (newValue != null && prevValue != null && !prevValue.equals(newValue)) {
                            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.obtainMessage(500, newValue.equals("0") ? 0 : 1, 0).sendToTarget();
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
                    com.android.server.bluetooth.Log.i(com.android.server.bluetooth.BluetoothManagerService.TAG, "Device is shutting down.");
                    com.android.server.bluetooth.BluetoothManagerService.this.mShutdownInProgress = true;
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.readLock().lock();
                    try {
                        try {
                            com.android.server.bluetooth.BluetoothManagerService.this.mEnable = false;
                            com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal = false;
                            if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null && com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(15)) {
                                com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.stopBle(com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource());
                            } else if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null && com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(12)) {
                                com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.disable(com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource());
                            }
                        } catch (android.os.RemoteException e) {
                            com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to shutdown Bluetooth", e);
                        }
                        return;
                    } finally {
                        com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.readLock().unlock();
                    }
                }
                if (action.equals(com.android.server.bluetooth.BluetoothManagerService.OPLUS_BACKUP_RESTORE_END_ACTION)) {
                    if (com.android.server.bluetooth.BluetoothManagerService.this.isBluetoothPersistedStateOn()) {
                        if (!com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(12)) {
                            com.android.server.bluetooth.Log.i(com.android.server.bluetooth.BluetoothManagerService.TAG, " backup restore end, set bluetooh on");
                            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.obtainMessage(501, 1, 0).sendToTarget();
                            return;
                        }
                        return;
                    }
                    if (com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(12)) {
                        com.android.server.bluetooth.Log.i(com.android.server.bluetooth.BluetoothManagerService.TAG, " backup restore end, set bluetooh off");
                        com.android.server.bluetooth.BluetoothManagerService.this.mHandler.obtainMessage(501, 0, 0).sendToTarget();
                    }
                }
            }
        };
        this.mConnection = new com.android.server.bluetooth.BluetoothManagerService.BluetoothServiceConnection();
        this.mBmsWrapper = new com.android.server.bluetooth.BluetoothManagerService.BluetoothManagerServiceWrapper();
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context, "Context cannot be null");
        this.mContentResolver = (android.content.ContentResolver) java.util.Objects.requireNonNull(this.mContext.getContentResolver(), "Resolver cannot be null");
        this.mLooper = (android.os.Looper) java.util.Objects.requireNonNull(looper, "Looper cannot be null");
        this.mUserManager = (android.os.UserManager) java.util.Objects.requireNonNull((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class), "UserManager system service cannot be null");
        this.mBinder = new com.android.server.bluetooth.BluetoothServiceBinder(this, this.mLooper, this.mContext, this.mUserManager);
        this.mHandler = new com.android.server.bluetooth.BluetoothManagerService.BluetoothHandler(this.mLooper);
        if (com.android.bluetooth.flags.Flags.respectBleScanSetting()) {
            com.android.server.bluetooth.BleScanSettingListener.initialize(this.mLooper, this.mContentResolver, new kotlin.jvm.functions.Function0() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function0
                public final java.lang.Object invoke() {
                    return this.f$0.onBleScanDisabled();
                }
            });
        } else {
            registerForBleScanModeChange();
        }
        if (!isBleSupported(this.mContext)) {
            this.mIsHearingAidProfileSupported = false;
        } else {
            boolean isAshaEnabledByDefault = (isAutomotive(this.mContext) || isWatch(this.mContext) || isTv(this.mContext)) ? false : true;
            this.mIsHearingAidProfileSupported = ((java.lang.Boolean) android.sysprop.BluetoothProperties.isProfileAshaCentralEnabled().orElse(java.lang.Boolean.valueOf(isAshaEnabledByDefault))).booleanValue();
        }
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.LOCAL_NAME_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.BLUETOOTH_ADDRESS_CHANGED");
        filter.addAction("android.os.action.SETTING_RESTORED");
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction(OPLUS_BACKUP_RESTORE_END_ACTION);
        filter.setPriority(1000);
        this.mContext.registerReceiver(this.mReceiver, filter, null, this.mHandler);
        android.content.IntentFilter filterUser = new android.content.IntentFilter();
        filterUser.addAction("android.os.action.USER_RESTRICTIONS_CHANGED");
        filterUser.addAction("android.intent.action.USER_SWITCHED");
        filterUser.setPriority(1000);
        this.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.bluetooth.BluetoothManagerService.3
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:11:0x0021  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r4, android.content.Intent r5) {
                /*
                    r3 = this;
                    java.lang.String r0 = r5.getAction()
                    int r1 = r0.hashCode()
                    r2 = 0
                    switch(r1) {
                        case 959232034: goto L17;
                        case 1527998851: goto Ld;
                        default: goto Lc;
                    }
                Lc:
                    goto L21
                Ld:
                    java.lang.String r1 = "android.os.action.USER_RESTRICTIONS_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Lc
                    r0 = 1
                    goto L22
                L17:
                    java.lang.String r1 = "android.intent.action.USER_SWITCHED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Lc
                    r0 = r2
                    goto L22
                L21:
                    r0 = -1
                L22:
                    switch(r0) {
                        case 0: goto L39;
                        case 1: goto L2f;
                        default: goto L25;
                    }
                L25:
                    java.lang.String r0 = com.android.server.bluetooth.BluetoothManagerService.m2647$$Nest$sfgetTAG()
                    java.lang.String r1 = "Unknown broadcast received in BluetoothManagerService receiver registered across all users"
                    com.android.server.bluetooth.Log.e(r0, r1)
                    goto L57
                L2f:
                    com.android.server.bluetooth.BluetoothManagerService r0 = com.android.server.bluetooth.BluetoothManagerService.this
                    android.os.UserHandle r1 = r3.getSendingUser()
                    r0.onUserRestrictionsChanged(r1)
                    goto L57
                L39:
                    java.lang.String r0 = "android.intent.extra.user_handle"
                    int r0 = r5.getIntExtra(r0, r2)
                    com.android.server.bluetooth.BluetoothManagerService r1 = com.android.server.bluetooth.BluetoothManagerService.this
                    com.android.server.bluetooth.IOplusBluetoothManagerServiceExt r1 = r1.mOplusBms
                    if (r1 == 0) goto L51
                    com.android.server.bluetooth.BluetoothManagerService r1 = com.android.server.bluetooth.BluetoothManagerService.this
                    com.android.server.bluetooth.IOplusBluetoothManagerServiceExt r1 = r1.mOplusBms
                    boolean r1 = r1.oplusPropagateForegroundUserId(r0)
                    if (r1 == 0) goto L51
                    goto L57
                L51:
                    com.android.server.bluetooth.BluetoothManagerService r1 = com.android.server.bluetooth.BluetoothManagerService.this
                    com.android.server.bluetooth.BluetoothManagerService.m2636$$Nest$mpropagateForegroundUserId(r1, r0)
                L57:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.bluetooth.BluetoothManagerService.AnonymousClass3.onReceive(android.content.Context, android.content.Intent):void");
            }
        }, filterUser, null, this.mHandler);
        loadStoredNameAndAddress();
        if (isBluetoothPersistedStateOn()) {
            com.android.server.bluetooth.Log.i(TAG, "Startup: Bluetooth persisted state is ON.");
            this.mEnableExternal = true;
        }
        boolean autoOnFlag = com.android.bluetooth.flags.Flags.autoOnFeature();
        boolean autoOnProperty = android.os.SystemProperties.getBoolean("bluetooth.server.automatic_turn_on", false);
        com.android.server.bluetooth.Log.d(TAG, "AutoOnFeature status: flag=" + autoOnFlag + ", property=" + autoOnProperty);
        if (autoOnFlag && autoOnProperty) {
            z = true;
        }
        this.mDeviceConfigAllowAutoOn = z;
        if (this.mDeviceConfigAllowAutoOn) {
            com.android.modules.expresslog.Counter.logIncrement("bluetooth.value_auto_on_supported");
        }
        this.mOplusBms = (com.android.server.bluetooth.IOplusBluetoothManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.class).base(this).create();
        this.mOplusBms.setContext(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public kotlin.Unit onBleScanDisabled() {
        if (this.mState.oneOf(10, 16)) {
            com.android.server.bluetooth.Log.i(TAG, "onBleScanDisabled: Nothing to do, Bluetooth is already turning off");
            return kotlin.Unit.INSTANCE;
        }
        clearBleApps();
        try {
            this.mAdapter.unregAllGattClient(this.mContext.getAttributionSource());
        } catch (android.os.RemoteException e) {
            com.android.server.bluetooth.Log.e(TAG, "onBleScanDisabled: unregAllGattClient failed", e);
        }
        if (this.mState.oneOf(15)) {
            com.android.server.bluetooth.Log.i(TAG, "onBleScanDisabled: Shutting down BLE_ON mode");
            try {
                this.mAdapter.stopBle(this.mContext.getAttributionSource());
            } catch (android.os.RemoteException e2) {
                com.android.server.bluetooth.Log.e(TAG, "onBleScanDisabled: stopBle failed", e2);
            }
        } else {
            com.android.server.bluetooth.Log.i(TAG, "onBleScanDisabled: Bluetooth is not in BLE_ON, staying on");
        }
        return kotlin.Unit.INSTANCE;
    }

    android.bluetooth.IBluetoothManager.Stub getBinder() {
        return this.mBinder;
    }

    private boolean isSatelliteModeOn() {
        return com.android.server.bluetooth.satellite.SatelliteModeListener.isOn();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBluetoothPersistedStateOn() {
        int state = com.android.server.bluetooth.BluetoothServerProxy.getInstance().getBluetoothPersistedState(this.mContentResolver, 1);
        com.android.server.bluetooth.Log.d(TAG, "isBluetoothPersistedStateOn: " + state);
        return state != 0;
    }

    private boolean isBluetoothPersistedStateOnAirplane() {
        int state = com.android.server.bluetooth.BluetoothServerProxy.getInstance().getBluetoothPersistedState(this.mContentResolver, 1);
        com.android.server.bluetooth.Log.d(TAG, "isBluetoothPersistedStateOnAirplane: " + state);
        return state == 2;
    }

    private boolean isBluetoothPersistedStateOnBluetooth() {
        int state = com.android.server.bluetooth.BluetoothServerProxy.getInstance().getBluetoothPersistedState(this.mContentResolver, 1);
        com.android.server.bluetooth.Log.d(TAG, "isBluetoothPersistedStateOnBluetooth: " + state);
        return state == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBluetoothPersistedState(int state) {
        com.android.server.bluetooth.BluetoothServerProxy.getInstance().setBluetoothPersistedState(this.mContentResolver, state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNameAndAddressSet() {
        return this.mName != null && this.mAddress != null && this.mName.length() > 0 && this.mAddress.length() > 0;
    }

    private void loadStoredNameAndAddress() {
        if (((java.lang.Boolean) android.sysprop.BluetoothProperties.isAdapterAddressValidationEnabled().orElse(false)).booleanValue() && android.provider.Settings.Secure.getInt(this.mContentResolver, "bluetooth_addr_valid", 0) == 0) {
            com.android.server.bluetooth.Log.w(TAG, "There is no valid bluetooth name and address stored");
            return;
        }
        this.mName = com.android.server.bluetooth.BluetoothServerProxy.getInstance().settingsSecureGetString(this.mContentResolver, "bluetooth_name");
        this.mAddress = com.android.server.bluetooth.BluetoothServerProxy.getInstance().settingsSecureGetString(this.mContentResolver, "bluetooth_address");
        com.android.server.bluetooth.Log.d(TAG, "loadStoredNameAndAddress: Name=" + this.mName + ", Address=" + logAddress(this.mAddress));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String logAddress(java.lang.String address) {
        if (address == null) {
            return "[address is null]";
        }
        if (address.length() != 17) {
            return "[address invalid]";
        }
        return "XX:XX:XX:XX:" + address.substring(address.length() - 5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeNameAndAddress(java.lang.String name, java.lang.String address) {
        java.lang.String logHeader = "storeNameAndAddress(" + name + ", " + logAddress(address) + "): ";
        if (name != null) {
            if (android.provider.Settings.Secure.putString(this.mContentResolver, "bluetooth_name", name)) {
                this.mName = name;
            } else {
                com.android.server.bluetooth.Log.e(TAG, logHeader + "Failed. Name is still " + this.mName);
            }
        }
        if (address != null) {
            if (android.provider.Settings.Secure.putString(this.mContentResolver, "bluetooth_address", address)) {
                this.mAddress = address;
            } else {
                com.android.server.bluetooth.Log.e(TAG, logHeader + "Failed. Address is still " + logAddress(this.mAddress));
            }
        }
        if (this.mName != null && this.mAddress != null) {
            android.provider.Settings.Secure.putInt(this.mContentResolver, "bluetooth_addr_valid", 1);
        }
        com.android.server.bluetooth.Log.d(TAG, logHeader + "Completed successfully");
    }

    android.bluetooth.IBluetooth registerAdapter(android.bluetooth.IBluetoothManagerCallback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.register(callback);
        }
        if (this.mAdapter != null) {
            return this.mAdapter.getAdapterBinder();
        }
        return null;
    }

    void unregisterAdapter(android.bluetooth.IBluetoothManagerCallback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.unregister(callback);
        }
    }

    boolean isEnabled() {
        return getState() == 12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void propagateForegroundUserId(int userId) {
        this.mAdapterLock.readLock().lock();
        try {
            try {
                if (this.mAdapter != null) {
                    this.mAdapter.setForegroundUserId(userId, this.mContext.getAttributionSource());
                }
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "Unable to set foreground user id", e);
            }
        } finally {
            this.mAdapterLock.readLock().unlock();
        }
    }

    int getState() {
        return this.mState.get();
    }

    class ClientDeathRecipient implements android.os.IBinder.DeathRecipient {
        private java.lang.String mPackageName;

        ClientDeathRecipient(java.lang.String packageName) {
            this.mPackageName = packageName;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.bluetooth.Log.w(com.android.server.bluetooth.BluetoothManagerService.TAG, "Binder is dead - unregister " + this.mPackageName);
            for (java.util.Map.Entry<android.os.IBinder, com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient> entry : com.android.server.bluetooth.BluetoothManagerService.this.mBleApps.entrySet()) {
                android.os.IBinder token = entry.getKey();
                com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient deathRec = entry.getValue();
                if (deathRec.equals(this)) {
                    com.android.server.bluetooth.BluetoothManagerService.this.updateBleAppCount(token, false, this.mPackageName);
                    return;
                }
            }
        }

        public java.lang.String getPackageName() {
            return this.mPackageName;
        }
    }

    boolean isBleScanAvailable() {
        if (com.android.bluetooth.flags.Flags.airplaneModeXBleOn()) {
            if (com.android.server.bluetooth.airplane.AirplaneModeListener.isOn() && !this.mEnable) {
                return false;
            }
        } else if (com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode() && !this.mEnable) {
            return false;
        }
        if (!com.android.bluetooth.flags.Flags.respectBleScanSetting()) {
            try {
                return android.provider.Settings.Global.getInt(this.mContentResolver, "ble_scan_always_enabled") != 0;
            } catch (android.provider.Settings.SettingNotFoundException e) {
                return false;
            }
        }
        if (com.android.server.bluetooth.satellite.SatelliteModeListener.isOn()) {
            return false;
        }
        return com.android.server.bluetooth.BleScanSettingListener.isScanAllowed();
    }

    boolean isHearingAidProfileSupported() {
        return this.mIsHearingAidProfileSupported;
    }

    android.content.Context getCurrentUserContext() {
        return this.mCurrentUserContext;
    }

    boolean isMediaProfileConnected() {
        if (this.mAdapter == null || !this.mState.oneOf(12)) {
            return false;
        }
        return this.mAdapter.isMediaProfileConnected(this.mContext.getAttributionSource());
    }

    private void registerForBleScanModeChange() {
        android.database.ContentObserver contentObserver = new android.database.ContentObserver(new android.os.Handler(this.mLooper)) { // from class: com.android.server.bluetooth.BluetoothManagerService.4
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                if (com.android.server.bluetooth.BluetoothManagerService.this.isBleScanAvailable()) {
                    return;
                }
                com.android.server.bluetooth.BluetoothManagerService.this.disableBleScanMode();
                com.android.server.bluetooth.BluetoothManagerService.this.clearBleApps();
                com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.readLock().lock();
                try {
                    try {
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null) {
                            com.android.server.bluetooth.BluetoothManagerService.this.addActiveLog(1, false);
                            com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.stopBle(com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource());
                        }
                    } catch (android.os.RemoteException e) {
                        com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "error when disabling bluetooth", e);
                    }
                } finally {
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.readLock().unlock();
                }
            }
        };
        this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("ble_scan_always_enabled"), false, contentObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableBleScanMode() {
        this.mAdapterLock.writeLock().lock();
        try {
            if (this.mAdapter != null && this.mState.oneOf(12)) {
                com.android.server.bluetooth.Log.d(TAG, "disableBleScanMode: Resetting the mEnable flag for clean disable");
                this.mEnable = false;
            }
        } finally {
            this.mAdapterLock.writeLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int updateBleAppCount(android.os.IBinder token, boolean enable, java.lang.String packageName) {
        java.lang.String header = "updateBleAppCount(" + token + ", " + enable + ", " + packageName + ")";
        com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient r = this.mBleApps.get(token);
        if (r == null && enable) {
            com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient deathRec = new com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient(packageName);
            try {
                token.linkToDeath(deathRec, 0);
                this.mBleApps.put(token, deathRec);
                com.android.server.bluetooth.Log.d(TAG, header + " linkToDeath");
            } catch (android.os.RemoteException e) {
                throw new java.lang.IllegalArgumentException("BLE app (" + packageName + ") already dead!");
            }
        } else if (!enable && r != null) {
            try {
                token.unlinkToDeath(r, 0);
            } catch (java.util.NoSuchElementException nsee) {
                com.android.server.bluetooth.Log.e(TAG, "updateBleAppCount(), Unable to unlinkToDeath", nsee);
            }
            this.mBleApps.remove(token);
            com.android.server.bluetooth.Log.d(TAG, header + " unlinkToDeath");
        }
        int appCount = this.mBleApps.size();
        this.mOplusBms.oplusDcsEventReport(4, appCount, 0, null, null);
        com.android.server.bluetooth.Log.d(TAG, header + " Number of BLE app registered: appCount=" + appCount);
        return appCount;
    }

    boolean enableBle(java.lang.String packageName, android.os.IBinder token) {
        com.android.server.bluetooth.Log.i(TAG, "enableBle(" + packageName + ", " + token + "): mAdapter=" + this.mAdapter + " isBinding=" + isBinding() + " mState=" + this.mState);
        if (com.android.bluetooth.flags.Flags.airplaneModeXBleOn()) {
            if (com.android.server.bluetooth.airplane.AirplaneModeListener.isOn() && !this.mEnable) {
                com.android.server.bluetooth.Log.d(TAG, "enableBle: not enabling - Airplane mode is ON on system");
                return false;
            }
        } else if (com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode()) {
            com.android.server.bluetooth.Log.d(TAG, "enableBle: not enabling - Airplane mode is ON");
            return false;
        }
        if (isSatelliteModeOn()) {
            com.android.server.bluetooth.Log.d(TAG, "enableBle: not enabling - Satellite mode is on.");
            return false;
        }
        if (com.android.bluetooth.flags.Flags.respectBleScanSetting() && !com.android.server.bluetooth.BleScanSettingListener.isScanAllowed()) {
            com.android.server.bluetooth.Log.d(TAG, "enableBle: not enabling - Scan mode is not allowed.");
            return false;
        }
        updateBleAppCount(token, true, packageName);
        if (this.mState.oneOf(12, 15, 11, 13, 14)) {
            com.android.server.bluetooth.Log.i(TAG, "enableBle: Bluetooth is already in state" + this.mState);
            return true;
        }
        synchronized (this.mReceiver) {
            sendEnableMsg(false, 1, packageName, true);
        }
        return true;
    }

    boolean disableBle(java.lang.String packageName, android.os.IBinder token) {
        com.android.server.bluetooth.Log.i(TAG, "disableBle(" + packageName + ", " + token + "): mAdapter=" + this.mAdapter + " isBinding=" + isBinding() + " mState=" + this.mState);
        if (!com.android.bluetooth.flags.Flags.respectBleScanSetting() && isSatelliteModeOn()) {
            com.android.server.bluetooth.Log.d(TAG, "disableBle: not disabling - satellite mode is on.");
            return false;
        }
        if (this.mState.oneOf(10)) {
            com.android.server.bluetooth.Log.i(TAG, "disableBle: Already disabled");
            return false;
        }
        updateBleAppCount(token, false, packageName);
        if (this.mState.oneOf(15) && !isBleAppPresent()) {
            if (this.mEnable) {
                disableBleScanMode();
            }
            if (!this.mEnableExternal) {
                addActiveLog(1, packageName, false, true);
                sendBrEdrDownCallback();
                this.mOplusBms.oplusDcsEventReport(1, 21, 1, packageName, null);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearBleApps() {
        this.mBleApps.clear();
        this.mOplusBms.oplusDcsEventReport(4, 0, 0, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBleAppPresent() {
        com.android.server.bluetooth.Log.d(TAG, "isBleAppPresent(): Number of BLE app registered: " + this.mBleApps.size());
        return this.mBleApps.size() > 0;
    }

    private void continueFromBleOnState() {
        this.mAdapterLock.readLock().lock();
        try {
            try {
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "Unable to call onServiceUp", e);
            }
            if (this.mAdapter == null) {
                com.android.server.bluetooth.Log.e(TAG, "continueFromBleOnState: Adapter is null");
                return;
            }
            if (!this.mEnableExternal && !isBleAppPresent()) {
                com.android.server.bluetooth.Log.i(TAG, "continueFromBleOnState: Disabled while enabling BLE, disable BLE now");
                this.mEnable = false;
                this.mAdapter.stopBle(this.mContext.getAttributionSource());
            } else {
                if (isBluetoothPersistedStateOnBluetooth() || !isBleAppPresent()) {
                    com.android.server.bluetooth.Log.i(TAG, "continueFromBleOnState: Starting br edr");
                    this.mAdapter.startBrEdr(this.mContext.getAttributionSource());
                    setBluetoothPersistedState(1);
                } else {
                    com.android.server.bluetooth.Log.i(TAG, "continueFromBleOnState: Staying in BLE_ON");
                }
            }
        } finally {
            this.mAdapterLock.readLock().unlock();
        }
    }

    private void sendBrEdrDownCallback() {
        this.mAdapterLock.readLock().lock();
        try {
            try {
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "sendBrEdrDownCallback: Call to mAdapter failed.", e);
            }
            if (this.mAdapter == null) {
                com.android.server.bluetooth.Log.w(TAG, "sendBrEdrDownCallback: mAdapter is null");
                return;
            }
            boolean scanIsAllowed = true;
            boolean airplaneDoesNotAllowBleOn = com.android.bluetooth.flags.Flags.airplaneModeXBleOn() && com.android.server.bluetooth.airplane.AirplaneModeListener.isOn();
            if (com.android.bluetooth.flags.Flags.respectBleScanSetting() && !com.android.server.bluetooth.BleScanSettingListener.isScanAllowed()) {
                scanIsAllowed = false;
            }
            if (!airplaneDoesNotAllowBleOn && isBleAppPresent() && scanIsAllowed) {
                com.android.server.bluetooth.Log.i(TAG, "sendBrEdrDownCallback: Staying in BLE_ON");
                this.mAdapter.unregAllGattClient(this.mContext.getAttributionSource());
            } else {
                com.android.server.bluetooth.Log.i(TAG, "sendBrEdrDownCallback: Stopping ble");
                this.mAdapter.stopBle(this.mContext.getAttributionSource());
            }
        } finally {
            this.mAdapterLock.readLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public kotlin.Unit enableFromAutoOn() {
        if (isBluetoothDisallowed()) {
            com.android.server.bluetooth.Log.d(TAG, "Bluetooth is not allowed, preventing AutoOn");
            return kotlin.Unit.INSTANCE;
        }
        com.android.modules.expresslog.Counter.logIncrement("bluetooth.value_auto_on_triggered");
        sendToggleNotification("auto_on_bt_enabled_notification");
        enable("BluetoothSystemServer/AutoOn");
        return kotlin.Unit.INSTANCE;
    }

    boolean enableNoAutoConnect(java.lang.String packageName) {
        if (isSatelliteModeOn()) {
            com.android.server.bluetooth.Log.d(TAG, "enableNoAutoConnect(" + packageName + "): Blocked by satellite mode");
            return false;
        }
        synchronized (this.mReceiver) {
            this.mQuietEnableExternal = true;
            this.mEnableExternal = true;
            sendEnableMsg(true, 1, packageName);
        }
        return true;
    }

    boolean enable(java.lang.String packageName) {
        com.android.server.bluetooth.Log.d(TAG, "enable(" + packageName + "): mAdapter=" + this.mAdapter + " isBinding=" + isBinding() + " mState=" + this.mState);
        if (isSatelliteModeOn()) {
            com.android.server.bluetooth.Log.d(TAG, "enable: not enabling - satellite mode is on.");
            return false;
        }
        synchronized (this.mReceiver) {
            this.mQuietEnableExternal = false;
            this.mEnableExternal = true;
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.bluetooth.airplane.AirplaneModeListener.notifyUserToggledBluetooth(this.mContentResolver, this.mCurrentUserContext, true);
                android.os.Binder.restoreCallingIdentity(callingIdentity);
                sendEnableMsg(false, 1, packageName);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
                throw th;
            }
        }
        return true;
    }

    boolean disable(java.lang.String packageName, boolean persist) {
        com.android.server.bluetooth.Log.d(TAG, "disable(" + packageName + ", " + persist + "): mAdapter=" + this.mAdapter + " isBinding=" + isBinding() + " mState=" + this.mState);
        synchronized (this.mReceiver) {
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.bluetooth.airplane.AirplaneModeListener.notifyUserToggledBluetooth(this.mContentResolver, this.mCurrentUserContext, false);
                if (persist) {
                    setBluetoothPersistedState(0);
                    this.mOplusBms.oplusClearBleApp(packageName);
                }
                this.mEnableExternal = false;
                sendDisableMsg(1, packageName);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }
        return true;
    }

    void unbindAndFinish() {
        com.android.server.bluetooth.Log.d(TAG, "unbindAndFinish(): mAdapter=" + this.mAdapter + " isBinding=" + isBinding());
        this.mOplusBms.oplusHandleUnbind();
        this.mAdapterLock.writeLock().lock();
        try {
            this.mHandler.removeMessages(60);
        } finally {
        }
        if (this.mAdapter == null) {
            return;
        }
        try {
            this.mAdapter.unregisterCallback(this.mBluetoothCallback, this.mContext.getAttributionSource());
        } catch (android.os.RemoteException e) {
            com.android.server.bluetooth.Log.e(TAG, "Unable to unregister BluetoothCallback", e);
        }
        if (!com.android.bluetooth.flags.Flags.explicitKillFromSystemServer()) {
            this.mAdapter = null;
            try {
                this.mContext.unbindService(this.mConnection);
            } catch (java.lang.IllegalArgumentException e2) {
                com.android.server.bluetooth.Log.e(TAG, "unbindService fail:" + e2);
            }
            this.mHandler.removeMessages(100);
            return;
        }
        final java.util.concurrent.CompletableFuture<java.lang.Void> binderDead = new java.util.concurrent.CompletableFuture<>();
        try {
            this.mAdapter.getAdapterBinder().asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda13
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    com.android.server.bluetooth.BluetoothManagerService.lambda$unbindAndFinish$7(binderDead);
                }
            }, 0);
        } catch (android.os.RemoteException e3) {
            com.android.server.bluetooth.Log.e(TAG, "Failed to linkToDeath", e3);
            binderDead.complete(null);
        }
        try {
            this.mContext.unbindService(this.mConnection);
        } catch (java.lang.IllegalArgumentException e4) {
            com.android.server.bluetooth.Log.e(TAG, "unbindService fail:" + e4);
        }
        try {
            try {
                this.mAdapter.killBluetoothProcess();
                binderDead.get(1L, java.util.concurrent.TimeUnit.SECONDS);
            } catch (android.os.RemoteException e5) {
                com.android.server.bluetooth.Log.e(TAG, "Unexpected RemoteException when calling killBluetoothProcess", e5);
            }
        } catch (android.os.DeadObjectException e6) {
            com.android.server.bluetooth.Log.i(TAG, "Bluetooth already dead 💀");
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e7) {
            com.android.server.bluetooth.Log.e(TAG, "Bluetooth death not received correctly", e7);
        }
        this.mAdapter = null;
        this.mHandler.removeMessages(100);
        return;
        this.mAdapterLock.writeLock().unlock();
    }

    static /* synthetic */ void lambda$unbindAndFinish$7(java.util.concurrent.CompletableFuture binderDead) {
        com.android.server.bluetooth.Log.i(TAG, "Successfully received Bluetooth death");
        binderDead.complete(null);
    }

    void handleOnBootPhase(final android.os.UserHandle userHandle) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleOnBootPhase$8(userHandle);
            }
        });
    }

    void initialize(android.os.UserHandle userHandle) {
        this.mCurrentUserContext = (android.content.Context) java.util.Objects.requireNonNull(this.mContext.createContextAsUser(userHandle, 0), "Current User Context cannot be null");
        com.android.server.bluetooth.airplane.AirplaneModeListener.initialize(this.mLooper, this.mContentResolver, this.mState, new kotlin.jvm.functions.Function1() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Object invoke(java.lang.Object obj) {
                return this.f$0.onAirplaneModeChanged(((java.lang.Boolean) obj).booleanValue());
            }
        }, new kotlin.jvm.functions.Function1() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Object invoke(java.lang.Object obj) {
                return this.f$0.sendToggleNotification((java.lang.String) obj);
            }
        }, new kotlin.jvm.functions.Function0() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function0
            public final java.lang.Object invoke() {
                return java.lang.Boolean.valueOf(this.f$0.isMediaProfileConnected());
            }
        }, new kotlin.jvm.functions.Function0() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function0
            public final java.lang.Object invoke() {
                return this.f$0.getCurrentUserContext();
            }
        }, kotlin.time.TimeSource.Monotonic.INSTANCE);
        com.android.server.bluetooth.satellite.SatelliteModeListener.initialize(this.mLooper, this.mContentResolver, new kotlin.jvm.functions.Function1() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda21
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Object invoke(java.lang.Object obj) {
                return this.f$0.onSatelliteModeChanged(((java.lang.Boolean) obj).booleanValue());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: internalHandleOnBootPhase, reason: merged with bridge method [inline-methods] */
    public void lambda$handleOnBootPhase$8(android.os.UserHandle userHandle) {
        com.android.server.bluetooth.Log.d(TAG, "internalHandleOnBootPhase(" + userHandle + "): Bluetooth boot completed");
        initialize(userHandle);
        this.mOplusBms.oplusHandleOnBootPhase();
        boolean isBluetoothDisallowed = isBluetoothDisallowed();
        if (isBluetoothDisallowed) {
            return;
        }
        boolean isSafeMode = this.mContext.getPackageManager().isSafeMode();
        if (this.mEnableExternal && isBluetoothPersistedStateOnBluetooth() && !isSafeMode) {
            com.android.server.bluetooth.Log.i(TAG, "internalHandleOnBootPhase: Auto-enabling Bluetooth.");
            sendEnableMsg(this.mQuietEnableExternal, 6);
        } else if (!isNameAndAddressSet()) {
            com.android.server.bluetooth.Log.i(TAG, "internalHandleOnBootPhase: Getting adapter name and address");
            this.mHandler.sendEmptyMessage(200);
        } else {
            autoOnSetupTimer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleSwitchUser, reason: merged with bridge method [inline-methods] */
    public void lambda$onSwitchUser$5(android.os.UserHandle userHandle) {
        com.android.server.bluetooth.Log.d(TAG, "handleSwitchUser(" + userHandle + ")");
        this.mHandler.obtainMessage(300, userHandle).sendToTarget();
    }

    void handleOnUnlockUser(android.os.UserHandle userHandle) {
        com.android.server.bluetooth.Log.d(TAG, "handleOnUnlockUser(" + userHandle + ")");
        this.mHandler.obtainMessage(301, userHandle).sendToTarget();
    }

    private void sendBluetoothOnCallback() {
        synchronized (this.mCallbacks) {
            try {
                int n = this.mCallbacks.beginBroadcast();
                com.android.server.bluetooth.Log.d(TAG, "Broadcasting onBluetoothOn() to " + n + " receivers.");
                for (int i = 0; i < n; i++) {
                    try {
                        this.mCallbacks.getBroadcastItem(i).onBluetoothOn();
                    } catch (android.os.RemoteException e) {
                        com.android.server.bluetooth.Log.e(TAG, "Unable to call onBluetoothOn() on callback #" + i, e);
                    }
                }
            } finally {
                this.mCallbacks.finishBroadcast();
            }
        }
    }

    private void sendBluetoothOffCallback() {
        synchronized (this.mCallbacks) {
            try {
                int n = this.mCallbacks.beginBroadcast();
                com.android.server.bluetooth.Log.d(TAG, "Broadcasting onBluetoothOff() to " + n + " receivers.");
                for (int i = 0; i < n; i++) {
                    try {
                        this.mCallbacks.getBroadcastItem(i).onBluetoothOff();
                    } catch (android.os.RemoteException e) {
                        com.android.server.bluetooth.Log.e(TAG, "Unable to call onBluetoothOff() on callback #" + i, e);
                    } catch (java.lang.SecurityException se) {
                        com.android.server.bluetooth.Log.e(TAG, "Unable to call onBluetoothStateChange() on callback #" + i, se);
                    }
                }
            } finally {
                this.mCallbacks.finishBroadcast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBluetoothServiceUpCallback() {
        synchronized (this.mCallbacks) {
            this.mAdapterLock.readLock().lock();
            try {
                int n = this.mCallbacks.beginBroadcast();
                com.android.server.bluetooth.Log.d(TAG, "sendBluetoothServiceUpCallback(): to " + n + " receivers");
                for (int i = 0; i < n; i++) {
                    try {
                        try {
                            this.mCallbacks.getBroadcastItem(i).onBluetoothServiceUp(this.mAdapter.getAdapterBinder().asBinder());
                        } catch (java.lang.SecurityException se) {
                            com.android.server.bluetooth.Log.e(TAG, "Unable to call onBluetoothServiceUp() on callback #" + i, se);
                        }
                    } catch (android.os.RemoteException e) {
                        com.android.server.bluetooth.Log.e(TAG, "Unable to call onBluetoothServiceUp() on callback #" + i, e);
                    }
                }
            } finally {
                this.mCallbacks.finishBroadcast();
                this.mAdapterLock.readLock().unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBluetoothServiceDownCallback() {
        synchronized (this.mCallbacks) {
            try {
                int n = this.mCallbacks.beginBroadcast();
                com.android.server.bluetooth.Log.d(TAG, "sendBluetoothServiceDownCallback(): to " + n + " receivers");
                for (int i = 0; i < n; i++) {
                    try {
                        this.mCallbacks.getBroadcastItem(i).onBluetoothServiceDown();
                    } catch (android.os.RemoteException e) {
                        com.android.server.bluetooth.Log.e(TAG, "Unable to call onBluetoothServiceDown() on callback #" + i, e);
                    } catch (java.lang.SecurityException se) {
                        com.android.server.bluetooth.Log.e(TAG, "Unable to call onBluetoothServiceDown() on callback #" + i, se);
                    }
                }
            } finally {
                this.mCallbacks.finishBroadcast();
            }
        }
    }

    java.lang.String getAddress() {
        this.mAdapterLock.readLock().lock();
        try {
            try {
                if (this.mAdapter != null && !this.mState.oneOf(14) && !this.mState.oneOf(16)) {
                    return this.mAdapter.getAddress(this.mContext.getAttributionSource());
                }
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "getAddress(): Unable to retrieve address remotely. Returning cached address", e);
            }
            return this.mAddress;
        } finally {
            this.mAdapterLock.readLock().unlock();
        }
    }

    java.lang.String getName() {
        this.mAdapterLock.readLock().lock();
        try {
            try {
                if (this.mAdapter != null) {
                    return this.mAdapter.getName(this.mContext.getAttributionSource());
                }
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "getName(): Unable to retrieve name remotely. Returning cached name", e);
            }
            return this.mName;
        } finally {
            this.mAdapterLock.readLock().unlock();
        }
    }

    public boolean factoryReset() {
        int callingUid = android.os.Binder.getCallingUid();
        if (android.os.UserHandle.getAppId(callingUid) == 1000) {
        }
        int state = getState();
        if ((state == 14 || state == 11 || state == 13) && !waitForState(15, 12)) {
            return false;
        }
        clearBleApps();
        boolean doReset = true;
        try {
            try {
                this.mAdapterLock.writeLock().lock();
                if (this.mAdapter == null) {
                    this.mEnable = true;
                    handleEnable(this.mQuietEnable);
                    doReset = false;
                } else if (state == 10) {
                    this.mEnable = true;
                    this.mAdapter.factoryReset(this.mContext.getAttributionSource());
                    handleEnable(this.mQuietEnable);
                } else if (state == 15) {
                    addActiveLog(10, false);
                    this.mAdapter.stopBle(this.mContext.getAttributionSource());
                    this.mAdapter.factoryReset(this.mContext.getAttributionSource());
                } else if (state == 12) {
                    addActiveLog(10, false);
                    handleDisable();
                    this.mAdapter.factoryReset(this.mContext.getAttributionSource());
                }
                this.mAdapterLock.writeLock().unlock();
                com.android.server.bluetooth.Log.d(TAG, "run oplus factoryReset()");
                if (doReset && (waitForState(10) || this.mAdapter == null)) {
                    this.mOplusBms.oplusFactoryReset();
                }
                return true;
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "factoryReset(): Unable to do factoryReset.", e);
                this.mAdapterLock.writeLock().unlock();
                com.android.server.bluetooth.Log.d(TAG, "run oplus factoryReset()");
                if (0 != 0 && (waitForState(10) || this.mAdapter == null)) {
                    this.mOplusBms.oplusFactoryReset();
                }
                return false;
            }
        } catch (java.lang.Throwable th) {
            this.mAdapterLock.writeLock().unlock();
            com.android.server.bluetooth.Log.d(TAG, "run oplus factoryReset()");
            if (1 != 0 && (waitForState(10) || this.mAdapter == null)) {
                this.mOplusBms.oplusFactoryReset();
            }
            throw th;
        }
    }

    class BluetoothServiceConnection implements android.content.ServiceConnection {
        BluetoothServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName componentName, android.os.IBinder service) {
            java.lang.String name = componentName.getClassName();
            com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "ServiceConnection.onServiceConnected(" + name + ", " + service + ")");
            if (!name.equals("com.android.bluetooth.btservice.AdapterService")) {
                com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unknown service connected: " + name);
            } else {
                com.android.server.bluetooth.BluetoothManagerService.this.mOplusBms.oplusHandleOnbind();
                com.android.server.bluetooth.BluetoothManagerService.this.mHandler.obtainMessage(40, service).sendToTarget();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName componentName) {
            java.lang.String name = componentName.getClassName();
            com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "ServiceConnection.onServiceDisconnected(" + name + ")");
            if (!name.equals("com.android.bluetooth.btservice.AdapterService")) {
                com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unknown service disconnected: " + name);
            } else {
                com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessage(41);
            }
        }
    }

    class BluetoothHandler extends android.os.Handler {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean mGetNameAddressOnly;

        BluetoothHandler(android.os.Looper looper) {
            super(looper);
            this.mGetNameAddressOnly = false;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int quietEnable = msg.arg1;
                    int isBle = msg.arg2;
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_ENABLE(quietEnable=" + quietEnable + ", isBle=" + isBle + "): mAdapter=" + com.android.server.bluetooth.BluetoothManagerService.this.mAdapter);
                    com.android.server.bluetooth.BluetoothManagerService.this.handleEnableMessage(quietEnable, isBle);
                    return;
                case 2:
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_DISABLE: mAdapter=" + com.android.server.bluetooth.BluetoothManagerService.this.mAdapter);
                    com.android.server.bluetooth.BluetoothManagerService.this.handleDisableMessage();
                    return;
                case 3:
                    if (!com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(10)) {
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mWaitForEnableRetry < 10) {
                            com.android.server.bluetooth.BluetoothManagerService.this.mWaitForEnableRetry++;
                            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(3, com.android.server.bluetooth.BluetoothManagerService.ENABLE_DISABLE_DELAY_MS);
                            return;
                        }
                        com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Wait for STATE_OFF timeout");
                    }
                    com.android.server.bluetooth.BluetoothManagerService.this.mWaitForEnableRetry = 0;
                    com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(42, com.android.server.bluetooth.BluetoothManagerService.this.getServiceRestartMs());
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "Handle enable is finished");
                    return;
                case 4:
                    boolean disabling = msg.arg1 == 1;
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_HANDLE_DISABLE_DELAYED: disabling:" + disabling);
                    if (disabling) {
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(12)) {
                            if (com.android.server.bluetooth.BluetoothManagerService.this.mWaitForDisableRetry < 10) {
                                com.android.server.bluetooth.BluetoothManagerService.this.mWaitForDisableRetry++;
                                android.os.Message disableDelayedMsg = com.android.server.bluetooth.BluetoothManagerService.this.mHandler.obtainMessage(4, 1, 0);
                                com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendMessageDelayed(disableDelayedMsg, com.android.server.bluetooth.BluetoothManagerService.ENABLE_DISABLE_DELAY_MS);
                                return;
                            }
                            com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Wait for exiting STATE_ON timeout");
                        }
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "Handle disable is finished");
                        return;
                    }
                    if (!com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(12)) {
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mWaitForDisableRetry < 10) {
                            com.android.server.bluetooth.BluetoothManagerService.this.mWaitForDisableRetry++;
                            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(4, com.android.server.bluetooth.BluetoothManagerService.ENABLE_DISABLE_DELAY_MS);
                            return;
                        }
                        com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Wait for STATE_ON timeout");
                    }
                    com.android.server.bluetooth.BluetoothManagerService.this.mWaitForDisableRetry = 0;
                    com.android.server.bluetooth.BluetoothManagerService.this.mEnable = false;
                    com.android.server.bluetooth.BluetoothManagerService.this.handleDisable();
                    android.os.Message disableDelayedMsg2 = com.android.server.bluetooth.BluetoothManagerService.this.mHandler.obtainMessage(4, 1, 0);
                    com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendMessageDelayed(disableDelayedMsg2, com.android.server.bluetooth.BluetoothManagerService.ENABLE_DISABLE_DELAY_MS);
                    return;
                case 40:
                    android.os.IBinder service = (android.os.IBinder) msg.obj;
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_BLUETOOTH_SERVICE_CONNECTED: service=" + service);
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().lock();
                    try {
                        com.android.server.bluetooth.BluetoothManagerService.this.mHandler.removeMessages(100);
                        com.android.server.bluetooth.BluetoothManagerService.this.mAdapter = com.android.server.bluetooth.BluetoothServerProxy.getInstance().createAdapterBinder(service);
                        int foregroundUserId = android.app.ActivityManager.getCurrentUser();
                        com.android.server.bluetooth.BluetoothManagerService.this.propagateForegroundUserId(foregroundUserId);
                        if (!com.android.server.bluetooth.BluetoothManagerService.this.isNameAndAddressSet()) {
                            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessage(200);
                            if (this.mGetNameAddressOnly && !com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                                return;
                            }
                        }
                        try {
                            com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.registerCallback(com.android.server.bluetooth.BluetoothManagerService.this.mBluetoothCallback, com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource());
                            break;
                        } catch (android.os.RemoteException e) {
                            com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to register BluetoothCallback", e);
                        }
                        if (!com.android.bluetooth.flags.Flags.fastBindToApp()) {
                            com.android.server.bluetooth.BluetoothManagerService.this.sendBluetoothServiceUpCallback();
                        }
                        try {
                            com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.enable(com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnable, com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource());
                            break;
                        } catch (android.os.RemoteException e2) {
                            com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to call enable()", e2);
                        }
                        if (com.android.bluetooth.flags.Flags.fastBindToApp()) {
                            com.android.server.bluetooth.BluetoothManagerService.this.sendBluetoothServiceUpCallback();
                            break;
                        }
                        com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().unlock();
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                            return;
                        }
                        com.android.server.bluetooth.BluetoothManagerService.this.waitForState(12);
                        com.android.server.bluetooth.BluetoothManagerService.this.handleDisable();
                        com.android.server.bluetooth.BluetoothManagerService.this.waitForState(10, 11, 13, 14, 15, 16);
                        return;
                    } finally {
                    }
                case 41:
                    com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_BLUETOOTH_SERVICE_DISCONNECTED");
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().lock();
                    try {
                        try {
                            if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter == null) {
                                return;
                            }
                            com.android.server.bluetooth.BluetoothManagerService.this.mContext.unbindService(com.android.server.bluetooth.BluetoothManagerService.this.mConnection);
                            com.android.server.bluetooth.BluetoothManagerService.this.mAdapter = null;
                            break;
                        } catch (java.lang.IllegalArgumentException e3) {
                            com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "unbindService fail:" + e3);
                            break;
                        }
                        com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().unlock();
                        com.android.server.bluetooth.BluetoothManagerService.this.addCrashLog();
                        com.android.server.bluetooth.BluetoothManagerService.this.addActiveLog(7, false);
                        com.android.server.bluetooth.BluetoothManagerService.this.mOplusBms.oplusDcsEventReport(3, 17, com.android.server.bluetooth.BluetoothManagerService.this.mErrorRecoveryRetryCounter < 7 ? 0 : 1, null, null);
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                            com.android.server.bluetooth.BluetoothManagerService.this.mEnable = false;
                            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(42, com.android.server.bluetooth.BluetoothManagerService.this.getServiceRestartMs());
                        }
                        com.android.server.bluetooth.BluetoothManagerService.this.sendBluetoothServiceDownCallback();
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(11, 12)) {
                            com.android.server.bluetooth.BluetoothManagerService.this.bluetoothStateChangeHandler(12, 13);
                            com.android.server.bluetooth.BluetoothManagerService.this.mState.set(13);
                        }
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(13)) {
                            com.android.server.bluetooth.BluetoothManagerService.this.bluetoothStateChangeHandler(13, 10);
                        }
                        com.android.server.bluetooth.BluetoothManagerService.this.mHandler.removeMessages(60);
                        com.android.server.bluetooth.BluetoothManagerService.this.mState.set(10);
                        return;
                    } finally {
                    }
                case 42:
                    com.android.server.bluetooth.BluetoothManagerService.this.mErrorRecoveryRetryCounter++;
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_RESTART_BLUETOOTH_SERVICE: retry count=" + com.android.server.bluetooth.BluetoothManagerService.this.mErrorRecoveryRetryCounter);
                    if (com.android.server.bluetooth.BluetoothManagerService.this.mErrorRecoveryRetryCounter < 6) {
                        com.android.server.bluetooth.BluetoothManagerService.this.mEnable = true;
                        com.android.server.bluetooth.BluetoothManagerService.this.addActiveLog(4, true);
                        com.android.server.bluetooth.BluetoothManagerService.this.mOplusBms.oplusDcsEventReport(1, 1, 4, com.android.server.bluetooth.BluetoothManagerService.this.mContext.getPackageName(), null);
                        com.android.server.bluetooth.BluetoothManagerService.this.handleEnable(com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnable);
                        return;
                    }
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().lock();
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapter = null;
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().unlock();
                    com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Reach maximum retry to restart Bluetooth!");
                    return;
                case 60:
                    int prevState = msg.arg1;
                    int newState = msg.arg2;
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_BLUETOOTH_STATE_CHANGE: prevState=" + android.bluetooth.BluetoothAdapter.nameForState(prevState) + " newState=" + android.bluetooth.BluetoothAdapter.nameForState(newState));
                    com.android.server.bluetooth.BluetoothManagerService.this.mState.set(newState);
                    com.android.server.bluetooth.BluetoothManagerService.this.bluetoothStateChangeHandler(prevState, newState);
                    if (prevState == 14 && newState == 10 && com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null && com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                        com.android.server.bluetooth.BluetoothManagerService.this.recoverBluetoothServiceFromError(false);
                    }
                    if (prevState == 11 && newState == 15 && com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null && com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                        com.android.server.bluetooth.BluetoothManagerService.this.recoverBluetoothServiceFromError(true);
                    }
                    if (prevState == 16 && newState == 10 && com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "Entering STATE_OFF but mEnabled is true; restarting.");
                        com.android.server.bluetooth.BluetoothManagerService.this.waitForState(10);
                        com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(42, com.android.server.bluetooth.BluetoothManagerService.this.getServiceRestartMs());
                    }
                    if ((newState == 12 || (!com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal && newState == 15)) && com.android.server.bluetooth.BluetoothManagerService.this.mErrorRecoveryRetryCounter != 0) {
                        com.android.server.bluetooth.Log.w(com.android.server.bluetooth.BluetoothManagerService.TAG, "bluetooth is recovered from error");
                        com.android.server.bluetooth.BluetoothManagerService.this.mErrorRecoveryRetryCounter = 0;
                        return;
                    }
                    return;
                case 100:
                    com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_TIMEOUT_BIND");
                    return;
                case 200:
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_GET_NAME_AND_ADDRESS");
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().lock();
                    try {
                        if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null || com.android.server.bluetooth.BluetoothManagerService.this.isBinding()) {
                            if (!com.android.server.bluetooth.BluetoothManagerService.this.mOplusBms.oplusSaveRemoteNameAndAddress()) {
                                if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null) {
                                    try {
                                        com.android.server.bluetooth.BluetoothManagerService.this.storeNameAndAddress(com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.getName(com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource()), com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.getAddress(com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource()));
                                    } catch (android.os.RemoteException e4) {
                                        com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to grab names", e4);
                                    }
                                    if (this.mGetNameAddressOnly && !com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                                        com.android.server.bluetooth.BluetoothManagerService.this.unbindAndFinish();
                                    }
                                    this.mGetNameAddressOnly = false;
                                }
                                break;
                            }
                            return;
                        }
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "Binding to service to get name and address");
                        this.mGetNameAddressOnly = true;
                        com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(100, com.android.server.bluetooth.BluetoothManagerService.TIMEOUT_BIND_MS);
                        android.content.Intent i = new android.content.Intent(android.bluetooth.IBluetooth.class.getName());
                        if (!com.android.server.bluetooth.BluetoothManagerService.this.doBind(i, com.android.server.bluetooth.BluetoothManagerService.this.mConnection, 65, android.os.UserHandle.CURRENT)) {
                            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.removeMessages(100);
                            com.android.server.bluetooth.BluetoothManagerService.this.mOplusBms.oplusDcsEventReport(3, 16, 0, null, null);
                        }
                        return;
                    } finally {
                    }
                case 300:
                    android.os.UserHandle userTo = (android.os.UserHandle) msg.obj;
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_USER_SWITCHED: userTo=" + userTo);
                    com.android.server.bluetooth.BluetoothManagerService.this.mHandler.removeMessages(300);
                    com.android.server.bluetooth.AutoOnFeature.pause();
                    com.android.server.bluetooth.BluetoothManagerService.this.mCurrentUserContext = com.android.server.bluetooth.BluetoothManagerService.this.mContext.createContextAsUser(userTo, 0);
                    if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null && com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(12)) {
                        restartForNewUser(userTo);
                        return;
                    }
                    if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter == null || !com.android.server.bluetooth.BluetoothManagerService.this.mState.oneOf(15) || !com.android.server.bluetooth.BluetoothManagerService.this.isBleAppPresent()) {
                        if (!com.android.server.bluetooth.BluetoothManagerService.this.isBinding() && com.android.server.bluetooth.BluetoothManagerService.this.mAdapter == null) {
                            com.android.server.bluetooth.BluetoothManagerService.this.autoOnSetupTimer();
                            return;
                        }
                        android.os.Message userMsg = android.os.Message.obtain(msg);
                        userMsg.arg1++;
                        com.android.server.bluetooth.BluetoothManagerService.this.mHandler.sendMessageDelayed(userMsg, com.android.server.bluetooth.BluetoothManagerService.USER_SWITCHED_TIME_MS);
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_USER_SWITCHED: userTo=" + userTo + " number of retry attempt=" + userMsg.arg1 + " isBinding=" + com.android.server.bluetooth.BluetoothManagerService.this.isBinding() + " mAdapter=" + com.android.server.bluetooth.BluetoothManagerService.this.mAdapter);
                        return;
                    }
                    com.android.server.bluetooth.Log.i(com.android.server.bluetooth.BluetoothManagerService.TAG, "Turn off from BLE state");
                    com.android.server.bluetooth.BluetoothManagerService.this.clearBleApps();
                    com.android.server.bluetooth.BluetoothManagerService.this.addActiveLog(8, com.android.server.bluetooth.BluetoothManagerService.this.mContext.getPackageName(), false, true);
                    com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.writeLock().lock();
                    try {
                        try {
                            com.android.server.bluetooth.BluetoothManagerService.this.mEnable = false;
                            com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.stopBle(com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource());
                        } finally {
                        }
                        break;
                    } catch (android.os.RemoteException e5) {
                        com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to shutdown Bluetooth", e5);
                    }
                    return;
                case 301:
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_USER_UNLOCKED");
                    com.android.server.bluetooth.BluetoothManagerService.this.mHandler.removeMessages(300);
                    if (com.android.server.bluetooth.BluetoothManagerService.this.mEnable && !com.android.server.bluetooth.BluetoothManagerService.this.isBinding() && com.android.server.bluetooth.BluetoothManagerService.this.mAdapter == null) {
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "Enabled but not bound; retrying after unlock");
                        com.android.server.bluetooth.BluetoothManagerService.this.handleEnable(com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnable);
                        return;
                    }
                    return;
                case 500:
                    if (msg.arg1 == 0 && com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_RESTORE_USER_SETTING: set Bluetooth state to disabled");
                        com.android.server.bluetooth.BluetoothManagerService.this.setBluetoothPersistedState(0);
                        com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal = false;
                        com.android.server.bluetooth.BluetoothManagerService.this.sendDisableMsg(9);
                        return;
                    }
                    if (msg.arg1 != 1 || com.android.server.bluetooth.BluetoothManagerService.this.mEnable) {
                        com.android.server.bluetooth.Log.w(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_RESTORE_USER_SETTING: Unhandled. mEnable=" + com.android.server.bluetooth.BluetoothManagerService.this.mEnable + " msg.arg1=" + msg.arg1);
                        return;
                    }
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_RESTORE_USER_SETTING: set Bluetooth state to enabled");
                    com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnableExternal = false;
                    com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal = true;
                    com.android.server.bluetooth.BluetoothManagerService.this.sendEnableMsg(false, 9);
                    return;
                case 501:
                    if (msg.arg1 == 0) {
                        com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_OPLUS_BACKUP_RESTORE: set Bluetooth state to disabled");
                        com.android.server.bluetooth.BluetoothManagerService.this.setBluetoothPersistedState(0);
                        com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal = false;
                        com.android.server.bluetooth.BluetoothManagerService.this.sendDisableMsg(9);
                        return;
                    }
                    com.android.server.bluetooth.Log.d(com.android.server.bluetooth.BluetoothManagerService.TAG, "MESSAGE_OPLUS_BACKUP_RESTORE: set Bluetooth state to enabled");
                    com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnableExternal = false;
                    com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal = true;
                    com.android.server.bluetooth.BluetoothManagerService.this.sendEnableMsg(false, 9);
                    return;
                default:
                    return;
            }
        }

        private void restartForNewUser(android.os.UserHandle unusedNewUser) {
            com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.readLock().lock();
            try {
                try {
                    if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null) {
                        com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.unregisterCallback(com.android.server.bluetooth.BluetoothManagerService.this.mBluetoothCallback, com.android.server.bluetooth.BluetoothManagerService.this.mContext.getAttributionSource());
                    }
                } catch (android.os.RemoteException e) {
                    com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to unregister", e);
                }
                com.android.server.bluetooth.BluetoothManagerService.this.addActiveLog(8, false);
                com.android.server.bluetooth.BluetoothManagerService.this.mOplusBms.oplusDcsEventReport(1, 2, 8, com.android.server.bluetooth.BluetoothManagerService.this.mContext.getPackageName(), null);
                com.android.server.bluetooth.BluetoothManagerService.this.handleDisable();
                com.android.server.bluetooth.BluetoothManagerService.this.bluetoothStateChangeHandler(12, 13);
                boolean didDisableTimeout = !com.android.server.bluetooth.BluetoothManagerService.this.waitForState(10);
                com.android.server.bluetooth.BluetoothManagerService.this.bluetoothStateChangeHandler(13, 10);
                if (didDisableTimeout) {
                    android.os.SystemClock.sleep(3000L);
                } else {
                    android.os.SystemClock.sleep(100L);
                }
                com.android.server.bluetooth.BluetoothManagerService.this.mHandler.removeMessages(60);
                com.android.server.bluetooth.BluetoothManagerService.this.mState.set(10);
                com.android.server.bluetooth.BluetoothManagerService.this.addActiveLog(8, true);
                com.android.server.bluetooth.BluetoothManagerService.this.mOplusBms.oplusDcsEventReport(1, 1, 8, com.android.server.bluetooth.BluetoothManagerService.this.mContext.getPackageName(), null);
                com.android.server.bluetooth.BluetoothManagerService.this.mEnable = true;
                com.android.server.bluetooth.BluetoothManagerService.this.handleEnable(com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnable);
            } finally {
                com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock.readLock().unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBinding() {
        return this.mHandler.hasMessages(100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnableMessage(int quietEnable, int isBle) {
        if (this.mShutdownInProgress) {
            com.android.server.bluetooth.Log.d(TAG, "Skip Bluetooth Enable in device shutdown process");
            return;
        }
        if (this.mHandler.hasMessages(4) || this.mHandler.hasMessages(3)) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, quietEnable, isBle), ENABLE_DISABLE_DELAY_MS);
            return;
        }
        this.mHandler.removeMessages(42);
        this.mEnable = true;
        if (isBle == 0) {
            setBluetoothPersistedState(1);
        }
        this.mAdapterLock.readLock().lock();
        try {
            try {
                if (this.mAdapter != null) {
                    boolean isHandled = true;
                    switch (this.mState.get()) {
                        case 11:
                        case 12:
                        case 14:
                            com.android.server.bluetooth.Log.i(TAG, "MESSAGE_ENABLE: already enabled");
                            break;
                        case 13:
                        default:
                            isHandled = false;
                            break;
                        case 15:
                            if (isBle != 1) {
                                com.android.server.bluetooth.Log.w(TAG, "BT Enable in BLE_ON State, going to ON");
                                this.mAdapter.startBrEdr(this.mContext.getAttributionSource());
                            } else {
                                com.android.server.bluetooth.Log.i(TAG, "Already at BLE_ON State");
                            }
                            break;
                    }
                    if (isHandled) {
                        return;
                    }
                }
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "", e);
            }
            this.mQuietEnable = quietEnable == 1;
            if (this.mAdapter == null) {
                handleEnable(this.mQuietEnable);
            } else {
                this.mWaitForEnableRetry = 0;
                this.mHandler.sendEmptyMessageDelayed(3, ENABLE_DISABLE_DELAY_MS);
            }
        } finally {
            this.mAdapterLock.readLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisableMessage() {
        if (this.mHandler.hasMessages(4) || isBinding() || this.mHandler.hasMessages(3)) {
            this.mHandler.sendEmptyMessageDelayed(2, ENABLE_DISABLE_DELAY_MS);
            return;
        }
        this.mHandler.removeMessages(42);
        if (this.mEnable && this.mAdapter != null) {
            this.mWaitForDisableRetry = 0;
            this.mHandler.sendEmptyMessageDelayed(4, ENABLE_DISABLE_DELAY_MS);
        } else {
            this.mEnable = false;
            handleDisable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnable(boolean quietMode) {
        this.mQuietEnable = quietMode;
        this.mAdapterLock.writeLock().lock();
        try {
            if (this.mAdapter == null && !isBinding()) {
                this.mOplusBms.waitForBluetoothProcesseExit();
                com.android.server.bluetooth.Log.d(TAG, "binding Bluetooth service");
                this.mHandler.sendEmptyMessageDelayed(100, TIMEOUT_BIND_MS);
                android.content.Intent i = new android.content.Intent(android.bluetooth.IBluetooth.class.getName());
                if (!doBind(i, this.mConnection, 65, android.os.UserHandle.CURRENT)) {
                    this.mHandler.removeMessages(100);
                }
            } else if (!com.android.bluetooth.flags.Flags.fastBindToApp() && this.mAdapter != null) {
                try {
                    this.mAdapter.enable(this.mQuietEnable, this.mContext.getAttributionSource());
                } catch (android.os.RemoteException e) {
                    com.android.server.bluetooth.Log.e(TAG, "Unable to call enable()", e);
                }
            }
        } finally {
            this.mAdapterLock.writeLock().unlock();
        }
    }

    boolean doBind(android.content.Intent intent, android.content.ServiceConnection conn, int flags, android.os.UserHandle user) {
        android.content.ComponentName comp = resolveSystemService(intent);
        if (this.DBG) {
            com.android.server.bluetooth.Log.d(TAG, "doBind(), " + comp);
        }
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, conn, flags, user)) {
            com.android.server.bluetooth.Log.e(TAG, "Fail to bind to: " + intent);
            this.mOplusBms.oplusDcsEventReport(3, 16, 0, null, null);
            return false;
        }
        com.android.server.bluetooth.Log.d(TAG, "doBind(), done");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisable() {
        if (this.mQuietEnable) {
            this.mQuietEnable = false;
        }
        this.mAdapterLock.readLock().lock();
        try {
            try {
                if (this.mAdapter != null) {
                    com.android.server.bluetooth.Log.d(TAG, "handleDisable: Sending off request.");
                    this.mAdapter.disable(this.mContext.getAttributionSource());
                }
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(TAG, "Unable to call disable()", e);
            }
        } finally {
            this.mAdapterLock.readLock().unlock();
        }
    }

    private void broadcastIntentStateChange(java.lang.String action, int prevState, int newState) {
        com.android.server.bluetooth.Log.d(TAG, "broadcastIntentStateChange: action=" + action.substring(action.lastIndexOf(46) + 1) + " prevState=" + android.bluetooth.BluetoothAdapter.nameForState(prevState) + " newState=" + android.bluetooth.BluetoothAdapter.nameForState(newState));
        android.content.Intent intent = new android.content.Intent(action);
        intent.putExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", prevState);
        intent.putExtra("android.bluetooth.adapter.extra.STATE", newState);
        intent.addFlags(67108864);
        intent.addFlags(16777216);
        intent.addFlags(268435456);
        if (this.DBG) {
            com.android.server.bluetooth.Log.d(TAG, "bluetoothStateChangeHandler() - Broadcast Adapter State: " + prevState + " > " + newState);
        }
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, null, getTempAllowlistBroadcastOptions());
    }

    private boolean isBleState(int state) {
        switch (state) {
            case 14:
            case 15:
            case 16:
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bluetoothStateChangeHandler(int prevState, int newState) {
        if (prevState == newState) {
            return;
        }
        this.mOplusBms.oplusDcsEventReport(2, prevState, newState, null, null);
        if (prevState == 12) {
            autoOnSetupTimer();
        }
        if (newState == 12) {
            if (com.android.modules.utils.build.SdkLevel.isAtLeastV() && this.mDeviceConfigAllowAutoOn) {
                com.android.server.bluetooth.AutoOnFeature.notifyBluetoothOn(this.mCurrentUserContext);
            }
            sendBluetoothOnCallback();
        } else if (newState == 10) {
            com.android.server.bluetooth.Log.d(TAG, "bluetoothStateChangeHandler: Bluetooth is OFF send Service Down");
            sendBluetoothServiceDownCallback();
            unbindAndFinish();
        } else if (newState == 15 && prevState == 14) {
            continueFromBleOnState();
        }
        broadcastIntentStateChange("android.bluetooth.adapter.action.BLE_STATE_CHANGED", prevState, newState);
        int prevBrEdrState = isBleState(prevState) ? 10 : prevState;
        int newBrEdrState = isBleState(newState) ? 10 : newState;
        if (prevBrEdrState != newBrEdrState) {
            if (newBrEdrState == 10) {
                sendBluetoothOffCallback();
                sendBrEdrDownCallback();
            }
            broadcastIntentStateChange("android.bluetooth.adapter.action.STATE_CHANGED", prevBrEdrState, newBrEdrState);
        }
    }

    boolean waitForManagerState(int state) {
        return this.mState.waitForState(STATE_TIMEOUT, state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean waitForState(int... states) {
        return this.mState.waitForState(STATE_TIMEOUT, states);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDisableMsg(int reason) {
        sendDisableMsg(reason, this.mContext.getPackageName());
    }

    private void sendDisableMsg(int reason, java.lang.String packageName) {
        this.mHandler.sendEmptyMessage(2);
        addActiveLog(reason, packageName, false, false);
        this.mOplusBms.oplusDcsEventReport(1, 2, reason, packageName, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnableMsg(boolean quietMode, int reason) {
        sendEnableMsg(quietMode, reason, this.mContext.getPackageName());
    }

    private void sendEnableMsg(boolean quietMode, int reason, java.lang.String packageName) {
        sendEnableMsg(quietMode, reason, packageName, false);
    }

    private void sendEnableMsg(boolean z, int i, java.lang.String str, boolean z2) {
        int i2 = 0;
        if (i == 6) {
            i2 = android.os.SystemProperties.getInt("persist.bluetooth.auto.enable.delay", 0);
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, z ? 1 : 0, z2 ? 1 : 0), i2);
        addActiveLog(i, str, true, z2);
        this.mLastEnabledTime = android.os.SystemClock.elapsedRealtime();
        this.mOplusBms.oplusDcsEventReport(1, 1, i, str, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveLog(int reason, boolean enable) {
        addActiveLog(reason, this.mContext.getPackageName(), enable, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveLog(int reason, java.lang.String packageName, boolean enable, boolean isBle) {
        int state;
        long timeSinceLastChanged;
        com.android.server.bluetooth.BluetoothManagerService.ActiveLog lastActiveLog = this.mActiveLogs.peekLast();
        synchronized (this.mActiveLogs) {
            if (this.mActiveLogs.size() > 20) {
                this.mActiveLogs.remove();
            }
            this.mActiveLogs.add(new com.android.server.bluetooth.BluetoothManagerService.ActiveLog(reason, packageName, enable, isBle, java.lang.System.currentTimeMillis()));
            int lastState = 1;
            if (enable) {
                state = 1;
            } else {
                state = 2;
            }
            if (lastActiveLog == null) {
                lastState = 0;
                timeSinceLastChanged = 0;
            } else {
                if (!lastActiveLog.getEnable()) {
                    lastState = 2;
                }
                timeSinceLastChanged = java.lang.System.currentTimeMillis() - lastActiveLog.getTimestamp();
            }
            com.android.bluetooth.BluetoothStatsLog.write_non_chained(67, android.os.Binder.getCallingUid(), null, state, reason, packageName, lastState, timeSinceLastChanged);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addCrashLog() {
        synchronized (this.mCrashTimestamps) {
            if (this.mCrashTimestamps.size() == 100) {
                this.mCrashTimestamps.removeFirst();
            }
            this.mCrashTimestamps.add(java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
            this.mCrashes++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recoverBluetoothServiceFromError(boolean clearBle) {
        com.android.server.bluetooth.Log.e(TAG, "recoverBluetoothServiceFromError");
        boolean repeatAirplaneRunnable = false;
        if (this.mHandler.hasMessages(0, ON_AIRPLANE_MODE_CHANGED_TOKEN)) {
            this.mHandler.removeCallbacksAndMessages(ON_AIRPLANE_MODE_CHANGED_TOKEN);
            repeatAirplaneRunnable = true;
        }
        this.mAdapterLock.readLock().lock();
        try {
            try {
                if (this.mAdapter != null) {
                    this.mAdapter.unregisterCallback(this.mBluetoothCallback, this.mContext.getAttributionSource());
                }
            } finally {
                this.mAdapterLock.readLock().unlock();
            }
        } catch (android.os.RemoteException e) {
            com.android.server.bluetooth.Log.e(TAG, "Unable to unregister", e);
        }
        android.os.SystemClock.sleep(500L);
        addActiveLog(5, false);
        handleDisable();
        waitForState(10);
        sendBluetoothServiceDownCallback();
        this.mAdapterLock.writeLock().lock();
        try {
            try {
                if (this.mAdapter != null) {
                    this.mAdapter = null;
                    this.mContext.unbindService(this.mConnection);
                }
            } catch (java.lang.IllegalArgumentException e2) {
                com.android.server.bluetooth.Log.e(TAG, "unbindService fail:" + e2);
            }
            this.mHandler.removeMessages(60);
            this.mState.set(10);
            if (clearBle) {
                clearBleApps();
            }
            this.mEnable = false;
            this.mHandler.sendEmptyMessageDelayed(42, ERROR_RESTART_TIME_MS);
            if (repeatAirplaneRunnable) {
                onAirplaneModeChanged(com.android.server.bluetooth.airplane.AirplaneModeListener.isOnOverrode());
            }
        } finally {
            this.mAdapterLock.writeLock().unlock();
        }
    }

    private boolean isBluetoothDisallowed() {
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            return ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).hasUserRestrictionForUser("no_bluetooth", android.os.UserHandle.SYSTEM);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    private void updateOppLauncherComponentState(android.os.UserHandle userHandle, boolean bluetoothSharingDisallowed) {
        int newState;
        int i = 0;
        if (bluetoothSharingDisallowed) {
            newState = 2;
        } else {
            try {
                if (((java.lang.Boolean) android.sysprop.BluetoothProperties.isProfileOppEnabled().orElse(false)).booleanValue()) {
                    newState = 1;
                } else {
                    newState = 0;
                }
            } catch (java.lang.Exception e) {
                e = e;
                com.android.server.bluetooth.Log.e(TAG, "updateOppLauncherComponentState failed: " + e);
            }
        }
        java.util.List<java.lang.String> baseBluetoothOppActivities = java.util.List.of("com.android.bluetooth.opp.BluetoothOppLauncherActivity", "com.android.bluetooth.opp.BluetoothOppBtEnableActivity", "com.android.bluetooth.opp.BluetoothOppBtEnablingActivity", "com.android.bluetooth.opp.BluetoothOppBtErrorActivity");
        android.content.pm.PackageManager systemPackageManager = this.mContext.getPackageManager();
        try {
            android.content.pm.PackageManager userPackageManager = this.mContext.createContextAsUser(userHandle, 0).getPackageManager();
            java.lang.String[] allPackages = systemPackageManager.getPackagesForUid(1002);
            int length = allPackages.length;
            int i2 = 0;
            while (i2 < length) {
                java.lang.String candidatePackage = allPackages[i2];
                com.android.server.bluetooth.Log.v(TAG, "Searching package " + candidatePackage);
                try {
                    android.content.pm.PackageInfo packageInfo = systemPackageManager.getPackageInfo(candidatePackage, android.content.pm.PackageManager.PackageInfoFlags.of(4203009L));
                    if (packageInfo.activities != null) {
                        android.content.pm.ActivityInfo[] activityInfoArr = packageInfo.activities;
                        int length2 = activityInfoArr.length;
                        int i3 = i;
                        while (i3 < length2) {
                            android.content.pm.ActivityInfo activity = activityInfoArr[i3];
                            android.content.pm.PackageInfo packageInfo2 = packageInfo;
                            com.android.server.bluetooth.Log.v(TAG, "Checking activity " + activity.name);
                            if (!baseBluetoothOppActivities.contains(activity.name)) {
                                i3++;
                                packageInfo = packageInfo2;
                            } else {
                                for (java.lang.String activityName : baseBluetoothOppActivities) {
                                    userPackageManager.setComponentEnabledSetting(new android.content.ComponentName(candidatePackage, activityName), newState, 1);
                                }
                                return;
                            }
                        }
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                    com.android.server.bluetooth.Log.e(TAG, "Could not find package " + candidatePackage);
                } catch (java.lang.Exception e3) {
                    com.android.server.bluetooth.Log.e(TAG, "Error while loading package" + e3);
                }
                i2++;
                i = 0;
            }
            com.android.server.bluetooth.Log.e(TAG, "Cannot toggle Bluetooth OPP activities, could not find them in any package");
        } catch (java.lang.Exception e4) {
            e = e4;
            com.android.server.bluetooth.Log.e(TAG, "updateOppLauncherComponentState failed: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getServiceRestartMs() {
        return (this.mErrorRecoveryRetryCounter + 1) * SERVICE_RESTART_TIME_MS;
    }

    void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        java.lang.String[] args2;
        if (args.length > 0 && args[0].startsWith("--proto")) {
            dumpProto(fd);
            return;
        }
        java.lang.String errorMsg = null;
        writer.println("Bluetooth Status");
        writer.println("  enabled: " + isEnabled());
        writer.println("  state: " + this.mState);
        writer.println("  address: " + logAddress(this.mAddress));
        writer.println("  name: " + this.mName);
        if (this.mEnable) {
            long onDuration = android.os.SystemClock.elapsedRealtime() - this.mLastEnabledTime;
            java.lang.String onDurationString = java.lang.String.format(java.util.Locale.US, "%02d:%02d:%02d.%03d", java.lang.Integer.valueOf((int) (onDuration / 3600000)), java.lang.Integer.valueOf((int) ((onDuration / 60000) % 60)), java.lang.Integer.valueOf((int) ((onDuration / 1000) % 60)), java.lang.Integer.valueOf((int) (onDuration % 1000)));
            writer.println("  time since enabled: " + onDurationString);
        }
        if (this.mActiveLogs.size() == 0) {
            writer.println("\nBluetooth never enabled!");
        } else {
            writer.println("\nEnable log:");
            for (com.android.server.bluetooth.BluetoothManagerService.ActiveLog log : this.mActiveLogs) {
                writer.println("  " + log);
            }
        }
        writer.println("\nBluetooth crashed " + this.mCrashes + " time" + (this.mCrashes == 1 ? "" : "s"));
        if (this.mCrashes == 100) {
            writer.println("(last 100)");
        }
        for (java.lang.Long time : this.mCrashTimestamps) {
            writer.println("  " + timeToLog(time.longValue()));
        }
        writer.println("\n" + this.mBleApps.size() + " BLE app" + (this.mBleApps.size() == 1 ? "" : "s") + " registered");
        for (com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient app : this.mBleApps.values()) {
            writer.println("  " + app.getPackageName());
        }
        writer.println("\nBluetoothManagerService:");
        writer.println("  mEnable:" + this.mEnable);
        writer.println("  mQuietEnable:" + this.mQuietEnable);
        writer.println("  mEnableExternal:" + this.mEnableExternal);
        writer.println("  mQuietEnableExternal:" + this.mQuietEnableExternal);
        writer.println("");
        writer.flush();
        if (args.length != 0) {
            args2 = args;
        } else {
            args2 = new java.lang.String[]{"--print"};
        }
        try {
            dumpBluetoothFlags(writer);
        } catch (java.lang.Exception e) {
            writer.println("Exception while dumping Bluetooth Flags");
        }
        if (this.mAdapter == null) {
            errorMsg = "Bluetooth Service not connected";
        } else {
            try {
                this.mAdapter.getAdapterBinder().asBinder().dumpAsync(fd, args2);
            } catch (android.os.RemoteException e2) {
                errorMsg = "RemoteException while dumping Bluetooth Service";
            }
        }
        if (errorMsg != null) {
            writer.println(errorMsg);
        }
    }

    private void dumpBluetoothFlags(java.io.PrintWriter writer) throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
        writer.println("🚩Flag dump:");
        int maxLen = ((java.lang.Integer) java.util.Arrays.stream(com.android.bluetooth.flags.Flags.class.getDeclaredMethods()).map(new java.util.function.Function() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((java.lang.reflect.Method) obj).getName();
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((java.lang.String) obj).length());
            }
        }).max(new java.util.Comparator() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda11
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return java.lang.Integer.compare(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue());
            }
        }).get()).intValue();
        java.lang.String fmt = "\t%s: %-" + maxLen + "s %s";
        for (java.lang.reflect.Method m : com.android.bluetooth.flags.Flags.class.getDeclaredMethods()) {
            java.lang.String flagStatus = ((java.lang.Boolean) m.invoke(null, new java.lang.Object[0])).booleanValue() ? "[■]" : "[ ]";
            java.lang.String name = m.getName();
            java.lang.String snakeCaseName = name.replaceAll("([A-Z])", "_$1").toLowerCase(java.util.Locale.US);
            writer.println(java.lang.String.format(fmt, flagStatus, name, snakeCaseName));
        }
        writer.println("");
    }

    private void dumpProto(java.io.FileDescriptor fd) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(new java.io.FileOutputStream(fd));
        proto.write(1133871366145L, isEnabled());
        proto.write(1120986464258L, this.mState.get());
        proto.write(1138166333443L, android.bluetooth.BluetoothAdapter.nameForState(this.mState.get()));
        proto.write(1138166333444L, logAddress(this.mAddress));
        proto.write(1138166333445L, this.mName);
        if (this.mEnable) {
            proto.write(1112396529670L, this.mLastEnabledTime);
        }
        proto.write(1112396529671L, android.os.SystemClock.elapsedRealtime());
        for (com.android.server.bluetooth.BluetoothManagerService.ActiveLog log : this.mActiveLogs) {
            long token = proto.start(2246267895816L);
            log.dump(proto);
            proto.end(token);
        }
        proto.write(1120986464265L, this.mCrashes);
        proto.write(1133871366154L, this.mCrashes == 100);
        for (java.lang.Long time : this.mCrashTimestamps) {
            proto.write(2211908157451L, time.longValue());
        }
        proto.write(1120986464268L, this.mBleApps.size());
        for (com.android.server.bluetooth.BluetoothManagerService.ClientDeathRecipient app : this.mBleApps.values()) {
            proto.write(2237677961229L, app.getPackageName());
        }
        proto.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getEnableDisableReasonString(int reason) {
        switch (reason) {
            case 1:
                return "APPLICATION_REQUEST";
            case 2:
                return "AIRPLANE_MODE";
            case 3:
                return "DISALLOWED";
            case 4:
                return "RESTARTED";
            case 5:
                return "START_ERROR";
            case 6:
                return "SYSTEM_BOOT";
            case 7:
                return "CRASH";
            case 8:
                return "USER_SWITCH";
            case 9:
                return "RESTORE_USER_SETTING";
            case 10:
                return "FACTORY_RESET";
            case 11:
            default:
                return "UNKNOWN[" + reason + "]";
            case 12:
                return "SATELLITE MODE";
        }
    }

    static android.os.Bundle getTempAllowlistBroadcastOptions() {
        android.app.BroadcastOptions bOptions = android.app.BroadcastOptions.makeBasic();
        bOptions.setTemporaryAppAllowlist(10000L, 0, 203, "");
        return bOptions.toBundle();
    }

    private android.content.ComponentName resolveSystemService(android.content.Intent intent) {
        java.util.List<android.content.pm.ResolveInfo> results = this.mContext.getPackageManager().queryIntentServices(intent, 0);
        if (results == null) {
            return null;
        }
        android.content.ComponentName comp = null;
        for (int i = 0; i < results.size(); i++) {
            android.content.pm.ResolveInfo ri = results.get(i);
            if ((ri.serviceInfo.applicationInfo.flags & 1) != 0) {
                android.content.ComponentName foundComp = new android.content.ComponentName(ri.serviceInfo.applicationInfo.packageName, ri.serviceInfo.name);
                if (comp != null) {
                    throw new java.lang.IllegalStateException("Multiple system services handle " + intent + ": " + comp + ", " + foundComp);
                }
                comp = foundComp;
            }
        }
        return comp;
    }

    int setBtHciSnoopLogMode(int mode) {
        android.sysprop.BluetoothProperties.snoop_log_mode_values snoopMode;
        switch (mode) {
            case 0:
                snoopMode = android.sysprop.BluetoothProperties.snoop_log_mode_values.DISABLED;
                break;
            case 1:
                snoopMode = android.sysprop.BluetoothProperties.snoop_log_mode_values.FILTERED;
                break;
            case 2:
                snoopMode = android.sysprop.BluetoothProperties.snoop_log_mode_values.FULL;
                break;
            default:
                com.android.server.bluetooth.Log.e(TAG, "setBtHciSnoopLogMode: Not a valid mode:" + mode);
                return 21;
        }
        try {
            android.sysprop.BluetoothProperties.snoop_log_mode(snoopMode);
            return 0;
        } catch (java.lang.RuntimeException e) {
            com.android.server.bluetooth.Log.e(TAG, "setBtHciSnoopLogMode: Failed to set mode to " + mode + ": " + e);
            return Integer.MAX_VALUE;
        }
    }

    int getBtHciSnoopLogMode() {
        android.sysprop.BluetoothProperties.snoop_log_mode_values mode = (android.sysprop.BluetoothProperties.snoop_log_mode_values) android.sysprop.BluetoothProperties.snoop_log_mode().orElse(android.sysprop.BluetoothProperties.snoop_log_mode_values.DISABLED);
        if (mode == android.sysprop.BluetoothProperties.snoop_log_mode_values.FILTERED) {
            return 1;
        }
        if (mode == android.sysprop.BluetoothProperties.snoop_log_mode_values.FULL) {
            return 2;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void autoOnSetupTimer() {
        if (!this.mDeviceConfigAllowAutoOn) {
            com.android.server.bluetooth.Log.d(TAG, "No support for AutoOn feature: Not creating a timer");
        } else {
            com.android.server.bluetooth.AutoOnFeature.resetAutoOnTimerForUser(this.mLooper, this.mCurrentUserContext, this.mState, new com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda8(this));
        }
    }

    private <T> T postAndWait(java.util.concurrent.Callable<T> callable) {
        java.util.concurrent.FutureTask<T> task = new java.util.concurrent.FutureTask<>(callable);
        this.mHandler.post(task);
        try {
            return task.get(1L, java.util.concurrent.TimeUnit.SECONDS);
        } catch (java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
            libcore.util.SneakyThrow.sneakyThrow(e);
            return null;
        } catch (java.util.concurrent.ExecutionException e2) {
            libcore.util.SneakyThrow.sneakyThrow(e2.getCause());
            return null;
        }
    }

    boolean isAutoOnSupported() {
        return this.mDeviceConfigAllowAutoOn && ((java.lang.Boolean) postAndWait(new java.util.concurrent.Callable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda15
            @Override // java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.lambda$isAutoOnSupported$9();
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isAutoOnSupported$9() throws java.lang.Exception {
        return java.lang.Boolean.valueOf(com.android.server.bluetooth.AutoOnFeature.isUserSupported(this.mCurrentUserContext.getContentResolver()));
    }

    boolean isAutoOnEnabled() {
        if (!this.mDeviceConfigAllowAutoOn) {
            throw new java.lang.IllegalStateException("AutoOnFeature is not supported in current config");
        }
        return ((java.lang.Boolean) postAndWait(new java.util.concurrent.Callable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda4
            @Override // java.util.concurrent.Callable
            public final java.lang.Object call() {
                return this.f$0.lambda$isAutoOnEnabled$10();
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isAutoOnEnabled$10() throws java.lang.Exception {
        return java.lang.Boolean.valueOf(com.android.server.bluetooth.AutoOnFeature.isUserEnabled(this.mCurrentUserContext));
    }

    void setAutoOnEnabled(final boolean status) {
        if (!this.mDeviceConfigAllowAutoOn) {
            throw new java.lang.IllegalStateException("AutoOnFeature is not supported in current config");
        }
        postAndWait(java.util.concurrent.Executors.callable(new java.lang.Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setAutoOnEnabled$11(status);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAutoOnEnabled$11(boolean status) {
        com.android.server.bluetooth.AutoOnFeature.setUserEnabled(this.mLooper, this.mCurrentUserContext, this.mState, status, new com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda8(this));
    }

    private static boolean isBleSupported(android.content.Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le");
    }

    private static boolean isAutomotive(android.content.Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    private static boolean isWatch(android.content.Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }

    private static boolean isTv(android.content.Context context) {
        android.content.pm.PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature("android.hardware.type.television") || pm.hasSystemFeature("android.software.leanback");
    }

    public com.android.server.bluetooth.IBluetoothManagerServiceWrapper getWrapper() {
        return this.mBmsWrapper;
    }

    private class BluetoothManagerServiceWrapper implements com.android.server.bluetooth.IBluetoothManagerServiceWrapper {
        private BluetoothManagerServiceWrapper() {
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void setNameAddressOnly(boolean nameAddressOnly) {
            com.android.server.bluetooth.BluetoothManagerService.this.mHandler.mGetNameAddressOnly = nameAddressOnly;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean getNameAddressOnly() {
            return com.android.server.bluetooth.BluetoothManagerService.this.mHandler.mGetNameAddressOnly;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean getQuietEnable() {
            return com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnable;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean getEnable() {
            return com.android.server.bluetooth.BluetoothManagerService.this.mEnable;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public java.util.concurrent.locks.ReentrantReadWriteLock getBluetoothLock() {
            return com.android.server.bluetooth.BluetoothManagerService.this.mAdapterLock;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public android.bluetooth.IBluetooth getBluetooth() {
            if (com.android.server.bluetooth.BluetoothManagerService.this.mAdapter != null) {
                return com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.getAdapterBinder();
            }
            return null;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public java.lang.Object getHandler() {
            return com.android.server.bluetooth.BluetoothManagerService.this.mHandler;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void storeNameAndAddress(java.lang.String name, java.lang.String address) {
            com.android.server.bluetooth.BluetoothManagerService.this.storeNameAndAddress(name, address);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void setEnableExternal(boolean mEnableExternal) {
            com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal = mEnableExternal;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void clearBleApps() {
            com.android.server.bluetooth.BluetoothManagerService.this.clearBleApps();
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void handleDisable() {
            com.android.server.bluetooth.BluetoothManagerService.this.handleDisable();
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void handleEnable(boolean quietMode) {
            com.android.server.bluetooth.BluetoothManagerService.this.handleEnable(quietMode);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean waitForState(java.util.Set<java.lang.Integer> states) {
            int[] statesIntArray = new int[states.size()];
            int i = 0;
            for (java.lang.Integer state : states) {
                statesIntArray[i] = state.intValue();
                i++;
            }
            return com.android.server.bluetooth.BluetoothManagerService.this.waitForState(statesIntArray);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void unbindAndFinish() {
            com.android.server.bluetooth.BluetoothManagerService.this.unbindAndFinish();
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void persistBluetoothSetting(int value) {
            com.android.server.bluetooth.BluetoothManagerService.this.setBluetoothPersistedState(value);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void propagateForegroundUserId(int foregroundUserId) {
            com.android.server.bluetooth.BluetoothManagerService.this.propagateForegroundUserId(foregroundUserId);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void OnBrEdrDown(android.content.AttributionSource attributionSource) {
            try {
                com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.stopBle(attributionSource);
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to call disable()", e);
            }
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void enableBluetooth(boolean quietMode, android.content.AttributionSource attributionSource) {
            try {
                com.android.server.bluetooth.BluetoothManagerService.this.mAdapter.enable(quietMode, attributionSource);
            } catch (android.os.RemoteException e) {
                com.android.server.bluetooth.Log.e(com.android.server.bluetooth.BluetoothManagerService.TAG, "Unable to call disable()", e);
            }
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public android.os.Bundle syncEnableDisableFlag() {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putBoolean(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE, com.android.server.bluetooth.BluetoothManagerService.this.mEnable);
            bundle.putBoolean(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_QUITE_ENABLE, com.android.server.bluetooth.BluetoothManagerService.this.mQuietEnable);
            bundle.putBoolean(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE_EXTERNAL, com.android.server.bluetooth.BluetoothManagerService.this.mEnableExternal);
            return bundle;
        }
    }
}
