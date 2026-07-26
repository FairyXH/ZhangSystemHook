package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbDeviceManager implements com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver {
    private static final int ACCESSORY_HANDSHAKE_TIMEOUT = 10000;
    private static final int ACCESSORY_REQUEST_TIMEOUT = 10000;
    private static final java.lang.String ACCESSORY_START_MATCH = "DEVPATH=/devices/virtual/misc/usb_accessory";
    private static final java.lang.String ADB_NOTIFICATION_CHANNEL_ID_TV = "usbdevicemanager.adb.tv";
    private static final java.lang.String BOOT_MODE_PROPERTY = "ro.bootmode";
    private static final boolean DEBUG = true;
    private static final int DEVICE_STATE_UPDATE_DELAY = 1000;
    private static final int DEVICE_STATE_UPDATE_DELAY_EXT = 1500;
    private static final int DUMPSYS_LOG_BUFFER = 200;
    private static final java.lang.String FUNCTIONS_PATH = "/sys/class/android_usb/android0/functions";
    private static final int HOST_STATE_UPDATE_DELAY = 1000;
    private static final java.lang.String MIDI_ALSA_PATH = "/sys/class/android_usb/android0/f_midi/alsa";
    private static final int MSG_ACCESSORY_HANDSHAKE_TIMEOUT = 20;
    private static final int MSG_ACCESSORY_MODE_ENTER_TIMEOUT = 8;
    private static final int MSG_BOOT_COMPLETED = 4;
    private static final int MSG_ENABLE_ADB = 1;
    private static final int MSG_FUNCTION_SWITCH_TIMEOUT = 17;
    private static final int MSG_GADGET_HAL_REGISTERED = 18;
    private static final int MSG_GET_CURRENT_USB_FUNCTIONS = 16;
    private static final int MSG_INCREASE_SENDSTRING_COUNT = 21;
    private static final int MSG_LOCALE_CHANGED = 11;
    private static final int MSG_RESET_USB_GADGET = 19;
    private static final int MSG_SET_CHARGING_FUNCTIONS = 14;
    private static final int MSG_SET_CURRENT_FUNCTIONS = 2;
    private static final int MSG_SET_FUNCTIONS_TIMEOUT = 15;
    private static final int MSG_SET_SCREEN_UNLOCKED_FUNCTIONS = 12;
    private static final int MSG_SYSTEM_READY = 3;
    private static final int MSG_UPDATE_CHARGING_STATE = 9;
    private static final int MSG_UPDATE_HAL_VERSION = 23;
    private static final int MSG_UPDATE_HOST_STATE = 10;
    private static final int MSG_UPDATE_PORT_STATE = 7;
    private static final int MSG_UPDATE_SCREEN_LOCK = 13;
    private static final int MSG_UPDATE_STATE = 0;
    private static final int MSG_UPDATE_USB_SPEED = 22;
    private static final int MSG_UPDATE_USER_RESTRICTIONS = 6;
    private static final int MSG_USER_SWITCHED = 5;
    private static final java.lang.String NORMAL_BOOT = "normal";
    private static final java.lang.String RNDIS_ETH_ADDR_PATH = "/sys/class/android_usb/android0/f_rndis/ethaddr";
    private static final java.lang.String STATE_PATH = "/sys/class/android_usb/android0/state";
    static final java.lang.String UNLOCKED_CONFIG_PREF = "usb-screen-unlocked-config-%d";
    private static final java.lang.String USB_CONTROLLER_NAME_PROPERTY = "sys.usb.controller";
    private static final java.lang.String USB_PREFS_XML = "UsbDeviceManagerPrefs.xml";
    private static final java.lang.String USB_STATE_MATCH = "DEVPATH=/devices/virtual/android_usb/android0";
    private static final java.lang.String USB_STATE_MATCH_SEC = "DEVPATH=/devices/virtual/android_usb/android1";
    private static com.android.server.usb.IOplusUsbDeviceFeature mIOplusUsbDeviceFeature;
    private static com.android.server.usb.hal.gadget.UsbGadgetHal mUsbGadgetHal;
    private static com.android.server.utils.EventLogger sEventLogger;
    private java.lang.String[] mAccessoryStrings;
    private final android.content.ContentResolver mContentResolver;
    private final android.content.Context mContext;
    private java.util.HashMap<java.lang.Long, java.io.FileDescriptor> mControlFds;
    private com.android.server.usb.UsbProfileGroupSettingsManager mCurrentSettings;
    private com.android.server.usb.UsbDeviceManager.UsbHandler mHandler;
    private final boolean mHasUsbAccessory;
    private final android.os.UEventObserver mUEventObserver;
    private static final java.lang.String TAG = com.android.server.usb.UsbDeviceManager.class.getSimpleName();
    private static final com.android.server.usb.UsbDeviceManager.UsbDeviceManagerWrapper USB_DEVICE_MANAGER_WRAPPER = new com.android.server.usb.UsbDeviceManager.UsbDeviceManagerWrapper();
    private static final java.util.concurrent.atomic.AtomicInteger sUsbOperationCount = new java.util.concurrent.atomic.AtomicInteger();
    private static java.util.Set<java.lang.Integer> sDenyInterfaces = new java.util.HashSet();
    private final java.lang.Object mLock = new java.lang.Object();
    private com.android.server.usb.IOplusUsbDeviceManagerCallback mUsbDeviceManagerCallback = new com.android.server.usb.IOplusUsbDeviceManagerCallback() { // from class: com.android.server.usb.UsbDeviceManager.5
        @Override // com.android.server.usb.IOplusUsbDeviceManagerCallback
        public android.content.Context getUsbDeviceManagerContext() {
            return com.android.server.usb.UsbDeviceManager.this.mContext;
        }
    };

    private native java.lang.String[] nativeGetAccessoryStrings();

    private native boolean nativeIsStartRequested();

    private native android.os.ParcelFileDescriptor nativeOpenAccessory();

    private native java.io.FileDescriptor nativeOpenControl(java.lang.String str);

    static {
        sDenyInterfaces.add(1);
        sDenyInterfaces.add(2);
        sDenyInterfaces.add(3);
        sDenyInterfaces.add(7);
        sDenyInterfaces.add(8);
        sDenyInterfaces.add(9);
        sDenyInterfaces.add(10);
        sDenyInterfaces.add(11);
        sDenyInterfaces.add(13);
        sDenyInterfaces.add(14);
        sDenyInterfaces.add(java.lang.Integer.valueOf(com.android.server.usb.descriptors.UsbDescriptor.CLASSID_WIRELESS));
        mIOplusUsbDeviceFeature = null;
    }

    private final class UsbUEventObserver extends android.os.UEventObserver {
        private UsbUEventObserver() {
        }

        public void onUEvent(android.os.UEventObserver.UEvent event) {
            android.util.Slog.v(com.android.server.usb.UsbDeviceManager.TAG, "USB UEVENT: " + event.toString());
            if (com.android.server.usb.UsbDeviceManager.sEventLogger != null) {
                com.android.server.usb.UsbDeviceManager.sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("USB UEVENT: " + event.toString()));
            } else {
                android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "sEventLogger == null");
            }
            java.lang.String state = event.get("USB_STATE");
            java.lang.String accessory = event.get("ACCESSORY");
            if (state != null) {
                com.android.server.usb.UsbDeviceManager.this.mHandler.updateState(state);
                return;
            }
            if ("GETPROTOCOL".equals(accessory)) {
                android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "got accessory get protocol");
                com.android.server.usb.UsbDeviceManager.this.mHandler.setAccessoryUEventTime(android.os.SystemClock.elapsedRealtime());
                com.android.server.usb.UsbDeviceManager.this.resetAccessoryHandshakeTimeoutHandler();
            } else if ("SENDSTRING".equals(accessory)) {
                android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "got accessory send string");
                com.android.server.usb.UsbDeviceManager.this.mHandler.sendEmptyMessage(21);
                com.android.server.usb.UsbDeviceManager.this.resetAccessoryHandshakeTimeoutHandler();
            } else if ("START".equals(accessory)) {
                android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "got accessory start");
                com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().setUsbAccessoryStartFlag();
                com.android.server.usb.UsbDeviceManager.this.mHandler.removeMessages(20);
                com.android.server.usb.UsbDeviceManager.this.mHandler.setStartAccessoryTrue();
                com.android.server.usb.UsbDeviceManager.this.startAccessoryMode();
            }
        }
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
    public void onKeyguardStateChanged(boolean isShowing) {
        int userHandle = android.app.ActivityManager.getCurrentUser();
        boolean secure = ((android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class)).isDeviceSecure(userHandle);
        android.util.Slog.v(TAG, "onKeyguardStateChanged: isShowing:" + isShowing + " secure:" + secure + " user:" + userHandle);
        this.mHandler.sendMessage(13, isShowing && secure);
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
    public void onAwakeStateChanged(boolean isAwake) {
    }

    public void onUnlockUser(int userHandle) {
        onKeyguardStateChanged(false);
    }

    public UsbDeviceManager(android.content.Context context, com.android.server.usb.UsbAlsaManager alsaManager, com.android.server.usb.UsbSettingsManager settingsManager, com.android.server.usb.UsbPermissionManager permissionManager) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        this.mHasUsbAccessory = pm.hasSystemFeature("android.hardware.usb.accessory");
        initRndisAddress();
        int operationId = sUsbOperationCount.incrementAndGet();
        mUsbGadgetHal = com.android.server.usb.hal.gadget.UsbGadgetHalInstance.getInstance(this, null);
        android.util.Slog.d(TAG, "getInstance done");
        this.mControlFds = new java.util.HashMap<>();
        java.io.FileDescriptor mtpFd = nativeOpenControl("mtp");
        if (mtpFd == null) {
            android.util.Slog.e(TAG, "Failed to open control for mtp");
        }
        this.mControlFds.put(4L, mtpFd);
        java.io.FileDescriptor ptpFd = nativeOpenControl("ptp");
        if (ptpFd == null) {
            android.util.Slog.e(TAG, "Failed to open control for ptp");
        }
        this.mControlFds.put(16L, ptpFd);
        if (mUsbGadgetHal == null) {
            this.mHandler = new com.android.server.usb.UsbDeviceManager.UsbHandlerLegacy(com.android.server.FgThread.get().getLooper(), this.mContext, this, alsaManager, permissionManager);
        } else {
            this.mHandler = new com.android.server.usb.UsbDeviceManager.UsbHandlerHal(com.android.server.FgThread.get().getLooper(), this.mContext, this, alsaManager, permissionManager);
        }
        this.mHandler.handlerInitDone(operationId);
        getOplusUsbDeviceFeature().init(context, this.mHandler);
        if (nativeIsStartRequested()) {
            android.util.Slog.d(TAG, "accessory attached at boot");
            startAccessoryMode();
        }
        android.content.BroadcastReceiver portReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                android.hardware.usb.ParcelableUsbPort port = (android.hardware.usb.ParcelableUsbPort) intent.getParcelableExtra("port", android.hardware.usb.ParcelableUsbPort.class);
                android.hardware.usb.UsbPortStatus status = (android.hardware.usb.UsbPortStatus) intent.getParcelableExtra("portStatus", android.hardware.usb.UsbPortStatus.class);
                com.android.server.usb.UsbDeviceManager.this.mHandler.updateHostState(port.getUsbPort((android.hardware.usb.UsbManager) context2.getSystemService(android.hardware.usb.UsbManager.class)), status);
            }
        };
        android.content.BroadcastReceiver chargingReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                int chargePlug = intent.getIntExtra("plugged", -1);
                boolean usbCharging = chargePlug == 2;
                com.android.server.usb.UsbDeviceManager.this.mHandler.sendMessage(9, usbCharging);
            }
        };
        android.content.BroadcastReceiver hostReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.util.Iterator<java.util.Map.Entry<java.lang.String, android.hardware.usb.UsbDevice>> it = ((android.hardware.usb.UsbManager) context2.getSystemService("usb")).getDeviceList().entrySet().iterator();
                if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                    com.android.server.usb.UsbDeviceManager.this.mHandler.sendMessage(10, (java.lang.Object) it, true);
                } else {
                    com.android.server.usb.UsbDeviceManager.this.mHandler.sendMessage(10, (java.lang.Object) it, false);
                }
            }
        };
        android.content.BroadcastReceiver languageChangedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.usb.UsbDeviceManager.this.mHandler.sendEmptyMessage(11);
            }
        };
        this.mContext.registerReceiver(portReceiver, new android.content.IntentFilter("android.hardware.usb.action.USB_PORT_CHANGED"));
        android.content.IntentFilter batteryFilter = new android.content.IntentFilter("android.intent.action.BATTERY_CHANGED");
        batteryFilter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
        this.mContext.registerReceiver(chargingReceiver, batteryFilter);
        android.content.IntentFilter filter = new android.content.IntentFilter("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        this.mContext.registerReceiver(hostReceiver, filter);
        this.mContext.registerReceiver(languageChangedReceiver, new android.content.IntentFilter("android.intent.action.LOCALE_CHANGED"));
        this.mUEventObserver = new com.android.server.usb.UsbDeviceManager.UsbUEventObserver();
        this.mUEventObserver.startObserving(USB_STATE_MATCH);
        this.mUEventObserver.startObserving(USB_STATE_MATCH_SEC);
        this.mUEventObserver.startObserving(ACCESSORY_START_MATCH);
        sEventLogger = new com.android.server.utils.EventLogger(200, "UsbDeviceManager activity");
    }

    com.android.server.usb.UsbProfileGroupSettingsManager getCurrentSettings() {
        com.android.server.usb.UsbProfileGroupSettingsManager usbProfileGroupSettingsManager;
        synchronized (this.mLock) {
            usbProfileGroupSettingsManager = this.mCurrentSettings;
        }
        return usbProfileGroupSettingsManager;
    }

    java.lang.String[] getAccessoryStrings() {
        java.lang.String[] strArr;
        synchronized (this.mLock) {
            strArr = this.mAccessoryStrings;
        }
        return strArr;
    }

    public void systemReady() {
        android.util.Slog.d(TAG, "systemReady");
        ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).registerScreenObserver(this);
        this.mHandler.sendEmptyMessage(3);
    }

    public void bootCompleted() {
        android.util.Slog.d(TAG, "boot completed");
        this.mHandler.sendEmptyMessage(4);
    }

    public void setCurrentUser(int newCurrentUserId, com.android.server.usb.UsbProfileGroupSettingsManager settings) {
        synchronized (this.mLock) {
            this.mCurrentSettings = settings;
            this.mHandler.obtainMessage(5, newCurrentUserId, 0).sendToTarget();
        }
    }

    public void updateUserRestrictions() {
        this.mHandler.sendEmptyMessage(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetAccessoryHandshakeTimeoutHandler() {
        long functions = getCurrentFunctions();
        if ((2 & functions) == 0) {
            this.mHandler.removeMessages(20);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(20), 10000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAccessoryMode() {
        if (this.mHasUsbAccessory) {
            int operationId = sUsbOperationCount.incrementAndGet();
            this.mAccessoryStrings = nativeGetAccessoryStrings();
            boolean z = false;
            if (this.mAccessoryStrings != null && this.mAccessoryStrings[0] != null && this.mAccessoryStrings[1] != null) {
                z = true;
            }
            boolean enableAccessory = z;
            long functions = enableAccessory ? 0 | 2 : 0L;
            if (functions != 0) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(8), 10000L);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(20), 10000L);
                setCurrentFunctions(functions, operationId);
            }
        }
    }

    private static void initRndisAddress() {
        int[] address = new int[6];
        address[0] = 2;
        java.lang.String serial = android.os.SystemProperties.get("ro.serialno", "1234567890ABCDEF");
        int serialLength = serial.length();
        for (int i = 0; i < serialLength; i++) {
            int i2 = (i % 5) + 1;
            address[i2] = address[i2] ^ serial.charAt(i);
        }
        java.lang.String addrString = java.lang.String.format(java.util.Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", java.lang.Integer.valueOf(address[0]), java.lang.Integer.valueOf(address[1]), java.lang.Integer.valueOf(address[2]), java.lang.Integer.valueOf(address[3]), java.lang.Integer.valueOf(address[4]), java.lang.Integer.valueOf(address[5]));
        try {
            android.os.FileUtils.stringToFile(RNDIS_ETH_ADDR_PATH, addrString);
        } catch (java.io.IOException e) {
            android.util.Slog.i(TAG, "failed to write to /sys/class/android_usb/android0/f_rndis/ethaddr");
        }
    }

    public static void logAndPrint(int priority, com.android.internal.util.IndentingPrintWriter pw, java.lang.String msg) {
        android.util.Slog.println(priority, TAG, msg);
        if (pw != null) {
            pw.println(msg);
        }
    }

    public static void logAndPrintException(com.android.internal.util.IndentingPrintWriter pw, java.lang.String msg, java.lang.Exception e) {
        android.util.Slog.e(TAG, msg, e);
        if (pw != null) {
            pw.println(msg + e);
        }
    }

    static abstract class UsbHandler extends android.os.Handler {
        protected static final java.lang.String MTP_PACKAGE_NAME = "com.android.mtp";
        protected static final java.lang.String MTP_SERVICE_CLASS_NAME = "com.android.mtp.MtpService";
        protected static final java.lang.String USB_PERSISTENT_CONFIG_PROPERTY = "persist.sys.usb.config";
        private long mAccessoryConnectionStartTime;
        private boolean mAdbNotificationShown;
        private boolean mAudioAccessoryConnected;
        private boolean mAudioAccessorySupported;
        private boolean mAudioSourceEnabled;
        protected boolean mBootCompleted;
        private android.content.Intent mBroadcastedIntent;
        protected boolean mConfigured;
        protected boolean mConnected;
        private boolean mConnectedToDataDisabledPort;
        protected final android.content.ContentResolver mContentResolver;
        private final android.content.Context mContext;
        private android.hardware.usb.UsbAccessory mCurrentAccessory;
        protected long mCurrentFunctions;
        protected boolean mCurrentFunctionsApplied;
        protected int mCurrentGadgetHalVersion;
        protected boolean mCurrentUsbFunctionsReceived;
        protected int mCurrentUser;
        private boolean mHideUsbNotification;
        private boolean mHostConnected;
        private boolean mInHostModeWithNoAccessoryConnected;
        private boolean mIsMtpServiceBound;
        private int mMidiCard;
        private int mMidiDevice;
        private boolean mMidiEnabled;
        private android.content.ServiceConnection mMtpServiceConnection;
        private android.app.NotificationManager mNotificationManager;
        protected boolean mPendingBootAccessoryHandshakeBroadcast;
        private boolean mPendingBootBroadcast;
        private final com.android.server.usb.UsbPermissionManager mPermissionManager;
        private int mPowerBrickConnectionStatus;
        protected boolean mResetUsbGadgetDisableDebounce;
        private boolean mScreenLocked;
        protected long mScreenUnlockedFunctions;
        private int mSendStringCount;
        protected android.content.SharedPreferences mSettings;
        private boolean mSinkPower;
        private boolean mSourcePower;
        private boolean mStartAccessory;
        private boolean mSupportsAllCombinations;
        private boolean mSystemReady;
        private boolean mUsbAccessoryConnected;
        private final com.android.server.usb.UsbAlsaManager mUsbAlsaManager;
        private boolean mUsbCharging;
        protected final com.android.server.usb.UsbDeviceManager mUsbDeviceManager;
        private int mUsbNotificationId;
        protected int mUsbSpeed;
        protected boolean mUseUsbNotification;

        public abstract void getUsbSpeedCb(int i);

        public abstract void handlerInitDone(int i);

        public abstract void resetCb(int i);

        public abstract void setCurrentUsbFunctionsCb(long j, int i, int i2, long j2, boolean z);

        protected abstract void setEnabledFunctions(long j, boolean z, int i);

        UsbHandler(android.os.Looper looper, android.content.Context context, com.android.server.usb.UsbDeviceManager deviceManager, com.android.server.usb.UsbAlsaManager alsaManager, com.android.server.usb.UsbPermissionManager permissionManager) {
            boolean massStorageSupported;
            super(looper);
            this.mAccessoryConnectionStartTime = 0L;
            boolean z = false;
            this.mSendStringCount = 0;
            this.mStartAccessory = false;
            this.mIsMtpServiceBound = false;
            this.mMtpServiceConnection = new android.content.ServiceConnection() { // from class: com.android.server.usb.UsbDeviceManager.UsbHandler.1
                @Override // android.content.ServiceConnection
                public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(android.content.ComponentName arg0) {
                }
            };
            this.mContext = context;
            this.mUsbDeviceManager = deviceManager;
            this.mUsbAlsaManager = alsaManager;
            this.mPermissionManager = permissionManager;
            this.mContentResolver = context.getContentResolver();
            this.mCurrentUser = android.app.ActivityManager.getCurrentUser();
            this.mScreenLocked = true;
            this.mSettings = getPinnedSharedPrefs(this.mContext);
            if (this.mSettings == null) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Couldn't load shared preferences");
            } else {
                this.mScreenUnlockedFunctions = android.hardware.usb.UsbManager.usbFunctionsFromString(this.mSettings.getString(java.lang.String.format(java.util.Locale.ENGLISH, com.android.server.usb.UsbDeviceManager.UNLOCKED_CONFIG_PREF, java.lang.Integer.valueOf(this.mCurrentUser)), ""));
            }
            android.os.storage.StorageManager storageManager = android.os.storage.StorageManager.from(this.mContext);
            android.os.storage.StorageVolume primary = storageManager != null ? storageManager.getPrimaryVolume() : null;
            if (primary == null || !primary.allowMassStorage()) {
                massStorageSupported = false;
            } else {
                massStorageSupported = true;
            }
            if (!massStorageSupported && this.mContext.getResources().getBoolean(android.R.bool.config_support_disable_satellite_while_enable_in_progress)) {
                z = true;
            }
            this.mUseUsbNotification = z;
        }

        public void sendMessage(int i, boolean z) {
            removeMessages(i);
            android.os.Message messageObtain = android.os.Message.obtain(this, i);
            messageObtain.arg1 = z ? 1 : 0;
            sendMessage(messageObtain);
        }

        public boolean sendMessage(int what) {
            removeMessages(what);
            android.os.Message m = android.os.Message.obtain(this, what);
            return sendMessageDelayed(m, 0L);
        }

        public void sendMessage(int what, int operationId) {
            removeMessages(what);
            android.os.Message m = android.os.Message.obtain(this, what);
            m.arg1 = operationId;
            sendMessage(m);
        }

        public void sendMessage(int what, java.lang.Object arg) {
            removeMessages(what);
            android.os.Message m = android.os.Message.obtain(this, what);
            m.obj = arg;
            sendMessage(m);
        }

        public void sendMessage(int what, java.lang.Object arg, int operationId) {
            removeMessages(what);
            android.os.Message m = android.os.Message.obtain(this, what);
            m.obj = arg;
            m.arg1 = operationId;
            sendMessage(m);
        }

        public void sendMessage(int i, boolean z, int i2) {
            removeMessages(i);
            android.os.Message messageObtain = android.os.Message.obtain(this, i);
            messageObtain.arg1 = z ? 1 : 0;
            messageObtain.arg2 = i2;
            sendMessage(messageObtain);
        }

        public void sendMessage(int i, java.lang.Object obj, boolean z) {
            removeMessages(i);
            android.os.Message messageObtain = android.os.Message.obtain(this, i);
            messageObtain.obj = obj;
            messageObtain.arg1 = z ? 1 : 0;
            sendMessage(messageObtain);
        }

        public void sendMessage(int i, long j, boolean z, int i2) {
            removeMessages(i);
            android.os.Message messageObtain = android.os.Message.obtain(this, i);
            messageObtain.obj = java.lang.Long.valueOf(j);
            messageObtain.arg1 = z ? 1 : 0;
            messageObtain.arg2 = i2;
            sendMessage(messageObtain);
        }

        public void sendMessage(int i, boolean z, boolean z2) {
            removeMessages(i);
            android.os.Message messageObtain = android.os.Message.obtain(this, i);
            messageObtain.arg1 = z ? 1 : 0;
            messageObtain.arg2 = z2 ? 1 : 0;
            sendMessage(messageObtain);
        }

        public void sendMessageDelayed(int i, boolean z, long j) {
            removeMessages(i);
            android.os.Message messageObtain = android.os.Message.obtain(this, i);
            messageObtain.arg1 = z ? 1 : 0;
            sendMessageDelayed(messageObtain, j);
        }

        public void updateState(java.lang.String state) {
            int connected;
            int configured;
            long j;
            if ("DISCONNECTED".equals(state)) {
                connected = 0;
                configured = 0;
            } else if ("CONNECTED".equals(state)) {
                connected = 1;
                configured = 0;
            } else if ("CONFIGURED".equals(state)) {
                connected = 1;
                configured = 1;
            } else {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "unknown state " + state);
                return;
            }
            if (connected == 1) {
                removeMessages(17);
            }
            android.os.Message msg = android.os.Message.obtain(this, 0);
            msg.arg1 = connected;
            msg.arg2 = configured;
            android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "mResetUsbGadgetDisableDebounce:" + this.mResetUsbGadgetDisableDebounce + " connected:" + connected + "configured:" + configured);
            if (this.mResetUsbGadgetDisableDebounce) {
                sendMessage(msg);
                if (connected == 1) {
                    this.mResetUsbGadgetDisableDebounce = false;
                    return;
                }
                return;
            }
            if (configured == 0) {
                removeMessages(0);
                android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "removeMessages MSG_UPDATE_STATE");
            }
            if (connected == 1) {
                removeMessages(17);
            }
            if (connected == 0) {
                j = this.mScreenLocked ? 1000 : 1500;
            } else {
                j = 0;
            }
            sendMessageDelayed(msg, j);
        }

        public void updateHostState(android.hardware.usb.UsbPort port, android.hardware.usb.UsbPortStatus status) {
            android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "updateHostState " + port + " status=" + status);
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = port;
            args.arg2 = status;
            removeMessages(7);
            android.os.Message msg = obtainMessage(7, args);
            sendMessageDelayed(msg, 1000L);
        }

        private void setAdbEnabled(boolean enable, int operationId) {
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "setAdbEnabled: " + enable);
            if (enable) {
                setSystemProperty(USB_PERSISTENT_CONFIG_PROPERTY, com.android.server.integrity.AppIntegrityManagerServiceImpl.ADB_INSTALLER);
            } else {
                setSystemProperty(USB_PERSISTENT_CONFIG_PROPERTY, "");
            }
            setEnabledFunctions(this.mCurrentFunctions, true, operationId);
            updateAdbNotification(false);
        }

        protected boolean isUsbTransferAllowed() {
            android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService("user");
            return !userManager.hasUserRestriction("no_usb_file_transfer");
        }

        private void updateCurrentAccessory() {
            int operationId = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
            boolean enteringAccessoryMode = hasMessages(8);
            if (this.mConfigured && enteringAccessoryMode) {
                java.lang.String[] accessoryStrings = this.mUsbDeviceManager.getAccessoryStrings();
                if (accessoryStrings != null && accessoryStrings[0] != null) {
                    com.android.server.usb.UsbSerialReader serialReader = new com.android.server.usb.UsbSerialReader(this.mContext, this.mPermissionManager, accessoryStrings[5]);
                    this.mCurrentAccessory = new android.hardware.usb.UsbAccessory(accessoryStrings[0], accessoryStrings[1], accessoryStrings[2], accessoryStrings[3], accessoryStrings[4], serialReader);
                    serialReader.setDevice(this.mCurrentAccessory);
                    android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "entering USB accessory mode: " + this.mCurrentAccessory);
                    if (this.mBootCompleted) {
                        this.mUsbDeviceManager.getCurrentSettings().accessoryAttached(this.mCurrentAccessory);
                        removeMessages(20);
                        broadcastUsbAccessoryHandshake();
                        return;
                    }
                    return;
                }
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "nativeGetAccessoryStrings failed");
                return;
            }
            if (!enteringAccessoryMode) {
                notifyAccessoryModeExit(operationId);
            } else {
                android.util.Slog.v(com.android.server.usb.UsbDeviceManager.TAG, "Debouncing accessory mode exit");
            }
        }

        protected void notifyAccessoryModeExit(int operationId) {
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "exited USB accessory mode");
            setEnabledFunctions(8L, false, operationId);
            com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().initUsbAccessoryStartFlag();
            if (this.mCurrentAccessory != null) {
                if (this.mBootCompleted) {
                    this.mPermissionManager.usbAccessoryRemoved(this.mCurrentAccessory);
                }
                this.mCurrentAccessory = null;
            }
        }

        protected android.content.SharedPreferences getPinnedSharedPrefs(android.content.Context context) {
            java.io.File prefsFile = new java.io.File(android.os.Environment.getDataSystemDeDirectory(0), com.android.server.usb.UsbDeviceManager.USB_PREFS_XML);
            return context.createDeviceProtectedStorageContext().getSharedPreferences(prefsFile, 0);
        }

        private boolean isUsbStateChanged(android.content.Intent intent) {
            java.util.Set<java.lang.String> keySet = intent.getExtras().keySet();
            if (this.mBroadcastedIntent == null) {
                java.util.Iterator<java.lang.String> it = keySet.iterator();
                while (it.hasNext()) {
                    if (intent.getBooleanExtra(it.next(), false)) {
                        return true;
                    }
                }
            } else {
                if (!keySet.equals(this.mBroadcastedIntent.getExtras().keySet())) {
                    return true;
                }
                for (java.lang.String key : keySet) {
                    if (intent.getBooleanExtra(key, false) != this.mBroadcastedIntent.getBooleanExtra(key, false)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void broadcastUsbAccessoryHandshake() {
            android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_ACCESSORY_HANDSHAKE").addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB).putExtra("android.hardware.usb.extra.ACCESSORY_UEVENT_TIME", this.mAccessoryConnectionStartTime).putExtra("android.hardware.usb.extra.ACCESSORY_STRING_COUNT", this.mSendStringCount).putExtra("android.hardware.usb.extra.ACCESSORY_START", this.mStartAccessory).putExtra("android.hardware.usb.extra.ACCESSORY_HANDSHAKE_END", android.os.SystemClock.elapsedRealtime());
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "broadcasting " + intent + " extras: " + intent.getExtras());
            sendStickyBroadcast(intent);
            resetUsbAccessoryHandshakeDebuggingInfo();
        }

        protected void updateUsbStateBroadcastIfNeeded(long functions) {
            android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_STATE");
            intent.addFlags(822083584);
            intent.addFlags(1048576);
            intent.putExtra("connected", this.mConnected);
            intent.putExtra("host_connected", this.mHostConnected);
            intent.putExtra("configured", this.mConfigured);
            intent.putExtra("unlocked", isUsbTransferAllowed() && isUsbDataTransferActive(this.mCurrentFunctions));
            for (long remainingFunctions = functions; remainingFunctions != 0; remainingFunctions -= java.lang.Long.highestOneBit(remainingFunctions)) {
                intent.putExtra(android.hardware.usb.UsbManager.usbFunctionsToString(java.lang.Long.highestOneBit(remainingFunctions)), true);
            }
            if (!isUsbStateChanged(intent)) {
                android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "skip broadcasting " + intent + " extras: " + intent.getExtras());
                return;
            }
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "broadcasting " + intent + " extras: " + intent.getExtras());
            sendStickyBroadcast(intent);
            this.mBroadcastedIntent = intent;
        }

        protected void sendStickyBroadcast(android.content.Intent intent) {
            this.mContext.sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
            com.android.server.usb.UsbDeviceManager.sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("USB intent: " + intent));
        }

        private void getMidiCardDevice() throws java.io.FileNotFoundException {
            java.lang.String controllerName = getSystemProperty(com.android.server.usb.UsbDeviceManager.USB_CONTROLLER_NAME_PROPERTY, "");
            if (android.text.TextUtils.isEmpty(controllerName)) {
                throw new java.io.FileNotFoundException("controller name not found");
            }
            java.io.File soundDir = new java.io.File("/sys/class/udc/" + controllerName + "/gadget/sound");
            if (!soundDir.exists()) {
                throw new java.io.FileNotFoundException("sound device not found");
            }
            java.io.File[] cardDirs = android.os.FileUtils.listFilesOrEmpty(soundDir, new java.io.FilenameFilter() { // from class: com.android.server.usb.UsbDeviceManager$UsbHandler$$ExternalSyntheticLambda0
                @Override // java.io.FilenameFilter
                public final boolean accept(java.io.File file, java.lang.String str) {
                    return str.startsWith("card");
                }
            });
            if (cardDirs.length != 1) {
                throw new java.io.FileNotFoundException("sound card not match");
            }
            java.io.File[] midis = android.os.FileUtils.listFilesOrEmpty(cardDirs[0], new java.io.FilenameFilter() { // from class: com.android.server.usb.UsbDeviceManager$UsbHandler$$ExternalSyntheticLambda1
                @Override // java.io.FilenameFilter
                public final boolean accept(java.io.File file, java.lang.String str) {
                    return str.startsWith("midi");
                }
            });
            if (midis.length != 1) {
                throw new java.io.FileNotFoundException("MIDI device not match");
            }
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("midiC(\\d+)D(\\d+)");
            java.util.regex.Matcher matcher = pattern.matcher(midis[0].getName());
            if (matcher.matches()) {
                this.mMidiCard = java.lang.Integer.parseInt(matcher.group(1));
                this.mMidiDevice = java.lang.Integer.parseInt(matcher.group(2));
                android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "Found MIDI card " + this.mMidiCard + " device " + this.mMidiDevice);
                return;
            }
            throw new java.io.FileNotFoundException("MIDI name not match");
        }

        private void updateUsbFunctions() {
            updateMidiFunction();
            updateMtpFunction();
        }

        /* JADX WARN: Removed duplicated region for block: B:35:0x006d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void updateMidiFunction() {
            /*
                r7 = this;
                long r0 = r7.mCurrentFunctions
                r2 = 8
                long r0 = r0 & r2
                r2 = 0
                int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                r1 = 1
                r2 = 0
                if (r0 == 0) goto Lf
                r0 = r1
                goto L10
            Lf:
                r0 = r2
            L10:
                boolean r3 = r7.mMidiEnabled
                if (r0 == r3) goto L62
                if (r0 == 0) goto L60
                boolean r3 = com.android.internal.hidden_from_bootclasspath.android.hardware.usb.flags.Flags.enableUsbSysfsMidiIdentification()
                if (r3 == 0) goto L2c
                r7.getMidiCardDevice()     // Catch: java.io.FileNotFoundException -> L20
                goto L2b
            L20:
                r3 = move-exception
                java.lang.String r4 = com.android.server.usb.UsbDeviceManager.m9871$$Nest$sfgetTAG()
                java.lang.String r5 = "could not identify MIDI device"
                android.util.Slog.e(r4, r5, r3)
                r0 = 0
            L2b:
                goto L60
            L2c:
                r3 = 0
                java.util.Scanner r4 = new java.util.Scanner     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                java.io.File r5 = new java.io.File     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                java.lang.String r6 = "/sys/class/android_usb/android0/f_midi/alsa"
                r5.<init>(r6)     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                r4.<init>(r5)     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                r3 = r4
                int r4 = r3.nextInt()     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                r7.mMidiCard = r4     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                int r4 = r3.nextInt()     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                r7.mMidiDevice = r4     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L49
                goto L56
            L47:
                r1 = move-exception
                goto L5a
            L49:
                r4 = move-exception
                java.lang.String r5 = com.android.server.usb.UsbDeviceManager.m9871$$Nest$sfgetTAG()     // Catch: java.lang.Throwable -> L47
                java.lang.String r6 = "could not open MIDI file"
                android.util.Slog.e(r5, r6, r4)     // Catch: java.lang.Throwable -> L47
                r0 = 0
                if (r3 == 0) goto L60
            L56:
                r3.close()
                goto L60
            L5a:
                if (r3 == 0) goto L5f
                r3.close()
            L5f:
                throw r1
            L60:
                r7.mMidiEnabled = r0
            L62:
                com.android.server.usb.UsbAlsaManager r3 = r7.mUsbAlsaManager
                boolean r4 = r7.mMidiEnabled
                if (r4 == 0) goto L6d
                boolean r4 = r7.mConfigured
                if (r4 == 0) goto L6d
                goto L6e
            L6d:
                r1 = r2
            L6e:
                int r2 = r7.mMidiCard
                int r4 = r7.mMidiDevice
                r3.setPeripheralMidiState(r1, r2, r4)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.usb.UsbDeviceManager.UsbHandler.updateMidiFunction():void");
        }

        private void updateMtpFunction() {
            boolean mtpEnabled = (this.mCurrentFunctions & 4) != 0;
            boolean ptpEnabled = (this.mCurrentFunctions & 16) != 0;
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "updateMtpFunction , mtpEnabled: " + mtpEnabled + ", ptpEnabled: " + ptpEnabled + ", mIsMtpServiceBound: " + this.mIsMtpServiceBound);
            if (this.mConfigured && (mtpEnabled || ptpEnabled)) {
                bindToMtpService();
            } else if (this.mIsMtpServiceBound) {
                unbindMtpService();
            }
        }

        private void bindToMtpService() {
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(new android.content.ComponentName(MTP_PACKAGE_NAME, MTP_SERVICE_CLASS_NAME));
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "Binding to MtpService");
            try {
                this.mIsMtpServiceBound = this.mContext.bindServiceAsUser(intent, this.mMtpServiceConnection, 1, android.os.UserHandle.CURRENT);
            } catch (java.lang.SecurityException exception) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Unable to bind to MtpService due to SecurityException", exception);
            }
            if (!this.mIsMtpServiceBound) {
                unbindMtpService();
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Binding to MtpService failed");
            }
            if (this.mIsMtpServiceBound) {
                android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "Successfully bound to MtpService");
            }
        }

        private void unbindMtpService() {
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "Unbinding from MtpService");
            this.mContext.unbindService(this.mMtpServiceConnection);
            this.mIsMtpServiceBound = false;
        }

        private void setScreenUnlockedFunctions(int operationId) {
            setEnabledFunctions(this.mScreenUnlockedFunctions, false, operationId);
        }

        private static class AdbTransport extends android.debug.IAdbTransport.Stub {
            private final com.android.server.usb.UsbDeviceManager.UsbHandler mHandler;

            AdbTransport(com.android.server.usb.UsbDeviceManager.UsbHandler handler) {
                this.mHandler = handler;
            }

            public void onAdbEnabled(boolean enabled, byte transportType) {
                if (transportType == 0) {
                    int operationId = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    this.mHandler.sendMessage(1, enabled, operationId);
                }
                com.android.server.usb.UsbDeviceManager.USB_DEVICE_MANAGER_WRAPPER.getExtImpl().onAdbEnabled(enabled);
            }
        }

        long getAppliedFunctions(long functions) {
            if (functions == 0) {
                return getChargingFunctions();
            }
            if (isAdbEnabled()) {
                return 1 | functions;
            }
            return functions;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    int operationId = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    this.mConnected = msg.arg1 == 1;
                    this.mConfigured = msg.arg2 == 1;
                    com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().setUsbPlugFlag(this.mConnected);
                    android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "handleMessage MSG_UPDATE_STATE mConnected:" + this.mConnected + " mConfigured:" + this.mConfigured);
                    updateUsbNotification(false);
                    updateAdbNotification(false);
                    if (this.mBootCompleted) {
                        updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
                    }
                    if ((this.mCurrentFunctions & 2) != 0) {
                        updateCurrentAccessory();
                    }
                    if (this.mBootCompleted) {
                        if (!this.mConnected && !hasMessages(8) && !hasMessages(17)) {
                            android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "usb plug out");
                            com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().initUsbAccessoryStartFlag();
                            if (!this.mScreenLocked && this.mScreenUnlockedFunctions != 0) {
                                setScreenUnlockedFunctions(operationId);
                            } else {
                                boolean shouldUseDefUsbFun = com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbFunctionsShuoldUseDefault(android.hardware.usb.UsbManager.usbFunctionsToString(getEnabledFunctions()));
                                setEnabledFunctions(shouldUseDefUsbFun ? getChargingFunctions() : 0L, false, operationId);
                            }
                        }
                        updateUsbFunctions();
                    } else {
                        this.mPendingBootBroadcast = true;
                    }
                    updateUsbSpeed();
                    break;
                case 1:
                    setAdbEnabled(msg.arg1 == 1, msg.arg2);
                    break;
                case 2:
                    long functions = ((java.lang.Long) msg.obj).longValue();
                    int operationId2 = msg.arg1;
                    if (com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().getUsbAccessoryStartFlag(functions) || (hasMessages(8) && !android.hardware.usb.UsbManager.usbFunctionsToString(functions).toLowerCase().contains("accessory"))) {
                        android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "still entering accessory mode");
                    } else {
                        setEnabledFunctions(functions, false, operationId2);
                    }
                    break;
                case 3:
                    int operationId3 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    this.mNotificationManager = (android.app.NotificationManager) this.mContext.getSystemService("notification");
                    ((android.debug.AdbManagerInternal) com.android.server.LocalServices.getService(android.debug.AdbManagerInternal.class)).registerTransport(new com.android.server.usb.UsbDeviceManager.UsbHandler.AdbTransport(this));
                    if (isTv()) {
                        this.mNotificationManager.createNotificationChannel(new android.app.NotificationChannel(com.android.server.usb.UsbDeviceManager.ADB_NOTIFICATION_CHANNEL_ID_TV, this.mContext.getString(android.R.string.accessibility_uncheck_legacy_item_warning), 4));
                    }
                    this.mSystemReady = true;
                    finishBoot(operationId3);
                    break;
                case 4:
                    int operationId4 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    this.mBootCompleted = true;
                    finishBoot(operationId4);
                    break;
                case 5:
                    int operationId5 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    if (this.mCurrentUser != msg.arg1) {
                        android.util.Slog.v(com.android.server.usb.UsbDeviceManager.TAG, "Current user switched to " + msg.arg1);
                        this.mCurrentUser = msg.arg1;
                        this.mScreenLocked = true;
                        this.mScreenUnlockedFunctions = 0L;
                        if (this.mSettings != null) {
                            this.mScreenUnlockedFunctions = android.hardware.usb.UsbManager.usbFunctionsFromString(this.mSettings.getString(java.lang.String.format(java.util.Locale.ENGLISH, com.android.server.usb.UsbDeviceManager.UNLOCKED_CONFIG_PREF, java.lang.Integer.valueOf(this.mCurrentUser)), ""));
                        }
                        setEnabledFunctions(0L, false, operationId5);
                        com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().sendUserSwitchBroadcast(this.mConnected, this.mConfigured, this.mCurrentUser);
                    }
                    break;
                case 6:
                    int operationId6 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    if (isUsbDataTransferActive(this.mCurrentFunctions) && !isUsbTransferAllowed()) {
                        setEnabledFunctions(0L, true, operationId6);
                        break;
                    }
                    break;
                case 7:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    boolean prevHostConnected = this.mHostConnected;
                    android.hardware.usb.UsbPort port = (android.hardware.usb.UsbPort) args.arg1;
                    android.hardware.usb.UsbPortStatus status = (android.hardware.usb.UsbPortStatus) args.arg2;
                    if (status != null) {
                        this.mHostConnected = status.getCurrentDataRole() == 1;
                        this.mSourcePower = status.getCurrentPowerRole() == 1;
                        this.mSinkPower = status.getCurrentPowerRole() == 2;
                        this.mAudioAccessoryConnected = status.getCurrentMode() == 4;
                        this.mSupportsAllCombinations = status.isRoleCombinationSupported(1, 1) && status.isRoleCombinationSupported(2, 1) && status.isRoleCombinationSupported(1, 2) && status.isRoleCombinationSupported(2, 2);
                        boolean usbDataDisabled = status.getUsbDataStatus() != 1;
                        this.mConnectedToDataDisabledPort = status.isConnected() && usbDataDisabled;
                        this.mPowerBrickConnectionStatus = status.getPowerBrickConnectionStatus();
                    } else {
                        this.mHostConnected = false;
                        this.mSourcePower = false;
                        this.mSinkPower = false;
                        this.mAudioAccessoryConnected = false;
                        this.mSupportsAllCombinations = false;
                        this.mConnectedToDataDisabledPort = false;
                        this.mPowerBrickConnectionStatus = 0;
                    }
                    if (this.mHostConnected && !this.mUsbAccessoryConnected) {
                        this.mInHostModeWithNoAccessoryConnected = true;
                    } else {
                        this.mInHostModeWithNoAccessoryConnected = false;
                    }
                    this.mAudioAccessorySupported = port.isModeSupported(4);
                    args.recycle();
                    updateUsbNotification(false);
                    if (this.mBootCompleted) {
                        if (this.mHostConnected || prevHostConnected) {
                            updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
                        }
                    } else {
                        this.mPendingBootBroadcast = true;
                    }
                    break;
                case 8:
                    int operationId7 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    android.util.Slog.v(com.android.server.usb.UsbDeviceManager.TAG, "Accessory mode enter timeout: " + this.mConnected + " ,operationId: " + operationId7);
                    if (!this.mConnected || (this.mCurrentFunctions & 2) == 0) {
                        notifyAccessoryModeExit(operationId7);
                        com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbEnterAccessoryTimeoutRecord("Accessory mode enter timeout, mConnected: " + this.mConnected, "AOA ERROR");
                    }
                    break;
                case 9:
                    this.mUsbCharging = msg.arg1 == 1;
                    updateUsbNotification(false);
                    break;
                case 10:
                    java.util.Iterator devices = (java.util.Iterator) msg.obj;
                    this.mUsbAccessoryConnected = msg.arg1 == 1;
                    android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "HOST_STATE connected:" + this.mUsbAccessoryConnected);
                    if (!devices.hasNext()) {
                        this.mInHostModeWithNoAccessoryConnected = true;
                    } else {
                        this.mInHostModeWithNoAccessoryConnected = false;
                    }
                    this.mHideUsbNotification = false;
                    while (devices.hasNext()) {
                        java.util.Map.Entry pair = (java.util.Map.Entry) devices.next();
                        android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, pair.getKey() + " = " + pair.getValue());
                        android.hardware.usb.UsbDevice device = (android.hardware.usb.UsbDevice) pair.getValue();
                        int configurationCount = device.getConfigurationCount() - 1;
                        while (configurationCount >= 0) {
                            android.hardware.usb.UsbConfiguration config = device.getConfiguration(configurationCount);
                            configurationCount--;
                            int interfaceCount = config.getInterfaceCount() - 1;
                            while (true) {
                                if (interfaceCount >= 0) {
                                    android.hardware.usb.UsbInterface intrface = config.getInterface(interfaceCount);
                                    interfaceCount--;
                                    if (com.android.server.usb.UsbDeviceManager.sDenyInterfaces.contains(java.lang.Integer.valueOf(intrface.getInterfaceClass()))) {
                                        this.mHideUsbNotification = true;
                                    }
                                }
                            }
                        }
                    }
                    updateUsbNotification(false);
                    break;
                case 11:
                    updateAdbNotification(true);
                    updateUsbNotification(true);
                    break;
                case 12:
                    int operationId8 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    this.mScreenUnlockedFunctions = ((java.lang.Long) msg.obj).longValue();
                    if (this.mSettings != null) {
                        android.content.SharedPreferences.Editor editor = this.mSettings.edit();
                        editor.putString(java.lang.String.format(java.util.Locale.ENGLISH, com.android.server.usb.UsbDeviceManager.UNLOCKED_CONFIG_PREF, java.lang.Integer.valueOf(this.mCurrentUser)), android.hardware.usb.UsbManager.usbFunctionsToString(this.mScreenUnlockedFunctions));
                        editor.commit();
                    }
                    if (!this.mScreenLocked && this.mScreenUnlockedFunctions != 0) {
                        setScreenUnlockedFunctions(operationId8);
                    } else {
                        setEnabledFunctions(0L, false, operationId8);
                    }
                    break;
                case 13:
                    int operationId9 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    if ((msg.arg1 == 1) != this.mScreenLocked) {
                        this.mScreenLocked = msg.arg1 == 1;
                        if (this.mBootCompleted) {
                            if (this.mScreenLocked) {
                                if (!this.mConnected) {
                                    setEnabledFunctions(0L, false, operationId9);
                                }
                                break;
                            } else if (this.mScreenUnlockedFunctions != 0 && this.mCurrentFunctions == 0) {
                                setScreenUnlockedFunctions(operationId9);
                                break;
                            }
                        }
                    }
                    break;
                case 20:
                    android.util.Slog.v(com.android.server.usb.UsbDeviceManager.TAG, "Accessory handshake timeout");
                    if (this.mBootCompleted) {
                        broadcastUsbAccessoryHandshake();
                    } else {
                        android.util.Slog.v(com.android.server.usb.UsbDeviceManager.TAG, "Pending broadcasting intent as not boot completed yet.");
                        this.mPendingBootAccessoryHandshakeBroadcast = true;
                    }
                    com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbAoaHandshakeTimeoutRecord("Accessory handshake timeout", "AOA ERROR");
                    break;
                case 21:
                    this.mSendStringCount++;
                    break;
            }
        }

        protected void finishBoot(int operationId) {
            com.android.server.usb.OplusUsbDeviceFinishBootInfo bootInfo = new com.android.server.usb.OplusUsbDeviceFinishBootInfo(this.mConnected, this.mBootCompleted, this.mCurrentUsbFunctionsReceived, this.mSystemReady, this.mPendingBootBroadcast, this.mScreenLocked, android.hardware.usb.UsbManager.usbFunctionsToString(this.mScreenUnlockedFunctions), isAdbEnabled());
            com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().printFinishBootInfo(bootInfo);
            if (this.mBootCompleted && this.mCurrentUsbFunctionsReceived && this.mSystemReady) {
                if (this.mPendingBootBroadcast) {
                    updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
                    this.mPendingBootBroadcast = false;
                }
                if (!this.mScreenLocked && this.mScreenUnlockedFunctions != 0) {
                    setScreenUnlockedFunctions(operationId);
                } else {
                    com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().processUserTestHarnessIfNeed(this.mContext);
                    setEnabledFunctions(0L, false, operationId);
                }
                if (this.mCurrentAccessory != null) {
                    this.mUsbDeviceManager.getCurrentSettings().accessoryAttached(this.mCurrentAccessory);
                    broadcastUsbAccessoryHandshake();
                } else if (this.mPendingBootAccessoryHandshakeBroadcast) {
                    broadcastUsbAccessoryHandshake();
                }
                this.mPendingBootAccessoryHandshakeBroadcast = false;
                updateUsbNotification(false);
                updateAdbNotification(false);
                updateUsbFunctions();
            }
        }

        protected boolean isUsbDataTransferActive(long functions) {
            return ((4 & functions) == 0 && (16 & functions) == 0) ? false : true;
        }

        public android.hardware.usb.UsbAccessory getCurrentAccessory() {
            return this.mCurrentAccessory;
        }

        protected void updateUsbGadgetHalVersion() {
            sendMessage(23, (java.lang.Object) null);
        }

        protected void updateUsbSpeed() {
            if (this.mCurrentGadgetHalVersion < 10) {
                this.mUsbSpeed = -1;
            } else if (this.mConnected && this.mConfigured) {
                sendMessage(22, (java.lang.Object) null);
            } else {
                this.mUsbSpeed = -1;
            }
        }

        protected void updateUsbNotification(boolean force) {
        }

        protected boolean isAdbEnabled() {
            return ((android.debug.AdbManagerInternal) com.android.server.LocalServices.getService(android.debug.AdbManagerInternal.class)).isAdbEnabled((byte) 0);
        }

        protected void updateAdbNotification(boolean force) {
        }

        private boolean isTv() {
            return this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        }

        protected long getChargingFunctions() {
            if (isAdbEnabled()) {
                return 1L;
            }
            return com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().getChargingFunctions();
        }

        protected void setSystemProperty(java.lang.String prop, java.lang.String val) {
            try {
                android.os.SystemProperties.set(prop, val);
            } catch (java.lang.Exception e) {
                android.util.Slog.w(com.android.server.usb.UsbDeviceManager.TAG, "Failed to set property.");
            }
        }

        protected java.lang.String getSystemProperty(java.lang.String prop, java.lang.String def) {
            return android.os.SystemProperties.get(prop, def);
        }

        protected void putGlobalSettings(android.content.ContentResolver contentResolver, java.lang.String setting, int val) {
            android.provider.Settings.Global.putInt(contentResolver, setting, val);
        }

        public long getEnabledFunctions() {
            return this.mCurrentFunctions;
        }

        public long getScreenUnlockedFunctions() {
            return this.mScreenUnlockedFunctions;
        }

        public int getUsbSpeed() {
            return this.mUsbSpeed;
        }

        public int getGadgetHalVersion() {
            return this.mCurrentGadgetHalVersion;
        }

        private void dumpFunctions(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id, long functions) {
            for (int i = 0; i < 63; i++) {
                if (((1 << i) & functions) != 0) {
                    if (dump.isProto()) {
                        dump.write(idName, id, 1 << i);
                    } else {
                        dump.write(idName, id, android.hardware.usb.gadget.V1_0.GadgetFunction.toString(1 << i));
                    }
                }
            }
        }

        public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
            long token = dump.start(idName, id);
            dumpFunctions(dump, "current_functions", 2259152797697L, this.mCurrentFunctions);
            dump.write("current_functions_applied", 1133871366146L, this.mCurrentFunctionsApplied);
            dumpFunctions(dump, "screen_unlocked_functions", 2259152797699L, this.mScreenUnlockedFunctions);
            dump.write("screen_locked", 1133871366148L, this.mScreenLocked);
            dump.write("connected", 1133871366149L, this.mConnected);
            dump.write("configured", 1133871366150L, this.mConfigured);
            if (this.mCurrentAccessory != null) {
                com.android.internal.usb.DumpUtils.writeAccessory(dump, "current_accessory", 1146756268039L, this.mCurrentAccessory);
            }
            dump.write("host_connected", 1133871366152L, this.mHostConnected);
            dump.write("source_power", 1133871366153L, this.mSourcePower);
            dump.write("sink_power", 1133871366154L, this.mSinkPower);
            dump.write("usb_charging", 1133871366155L, this.mUsbCharging);
            dump.write("hide_usb_notification", 1133871366156L, this.mHideUsbNotification);
            dump.write("audio_accessory_connected", 1133871366157L, this.mAudioAccessoryConnected);
            try {
                com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dump, "kernel_state", 1138166333455L, android.os.FileUtils.readTextFile(new java.io.File(com.android.server.usb.UsbDeviceManager.STATE_PATH), 0, null).trim());
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.w(com.android.server.usb.UsbDeviceManager.TAG, "Ignore missing legacy kernel path in bugreport dump: kernel state:/sys/class/android_usb/android0/state");
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Could not read kernel state", e2);
            }
            try {
                com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dump, "kernel_function_list", 1138166333456L, android.os.FileUtils.readTextFile(new java.io.File(com.android.server.usb.UsbDeviceManager.FUNCTIONS_PATH), 0, null).trim());
            } catch (java.io.FileNotFoundException e3) {
                android.util.Slog.w(com.android.server.usb.UsbDeviceManager.TAG, "Ignore missing legacy kernel path in bugreport dump: kernel function list:/sys/class/android_usb/android0/functions");
            } catch (java.lang.Exception e4) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Could not read kernel function list", e4);
            }
            dump.end(token);
        }

        public void setAccessoryUEventTime(long accessoryConnectionStartTime) {
            this.mAccessoryConnectionStartTime = accessoryConnectionStartTime;
        }

        public void setStartAccessoryTrue() {
            this.mStartAccessory = true;
        }

        public void resetUsbAccessoryHandshakeDebuggingInfo() {
            this.mAccessoryConnectionStartTime = 0L;
            this.mSendStringCount = 0;
            this.mStartAccessory = false;
        }
    }

    private static final class UsbHandlerLegacy extends com.android.server.usb.UsbDeviceManager.UsbHandler {
        private static final java.lang.String USB_CONFIG_PROPERTY = "sys.usb.config";
        private static final java.lang.String USB_STATE_PROPERTY = "sys.usb.state";
        private java.lang.String mCurrentFunctionsStr;
        private java.lang.String mCurrentOemFunctions;
        private int mCurrentRequest;
        private java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, android.util.Pair<java.lang.String, java.lang.String>>> mOemModeMap;
        private boolean mUsbDataUnlocked;

        UsbHandlerLegacy(android.os.Looper looper, android.content.Context context, com.android.server.usb.UsbDeviceManager deviceManager, com.android.server.usb.UsbAlsaManager alsaManager, com.android.server.usb.UsbPermissionManager permissionManager) {
            super(looper, context, deviceManager, alsaManager, permissionManager);
            this.mCurrentRequest = 0;
            try {
                readOemUsbOverrideConfig(context);
                this.mCurrentOemFunctions = getSystemProperty(getPersistProp(false), "none");
                if (!isNormalBoot()) {
                    this.mCurrentFunctionsStr = getSystemProperty(getPersistProp(true), "none");
                    this.mCurrentFunctionsApplied = getSystemProperty(USB_CONFIG_PROPERTY, "none").equals(getSystemProperty(USB_STATE_PROPERTY, "none"));
                } else {
                    this.mCurrentFunctionsStr = getSystemProperty(USB_CONFIG_PROPERTY, "none");
                    this.mCurrentFunctionsApplied = this.mCurrentFunctionsStr.equals(getSystemProperty(USB_STATE_PROPERTY, "none"));
                }
                this.mCurrentFunctions = 0L;
                this.mCurrentUsbFunctionsReceived = true;
                this.mUsbSpeed = -1;
                this.mCurrentGadgetHalVersion = -1;
                java.lang.String state = android.os.FileUtils.readTextFile(new java.io.File(com.android.server.usb.UsbDeviceManager.STATE_PATH), 0, null).trim();
                updateState(state);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Error initializing UsbHandler", e);
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void handlerInitDone(int operationId) {
        }

        private void readOemUsbOverrideConfig(android.content.Context context) {
            java.lang.String[] configList = context.getResources().getStringArray(android.R.array.config_notificationFallbackVibeWaveform);
            if (configList != null) {
                for (java.lang.String config : configList) {
                    java.lang.String[] items = config.split(":");
                    if (items.length == 3 || items.length == 4) {
                        if (this.mOemModeMap == null) {
                            this.mOemModeMap = new java.util.HashMap<>();
                        }
                        java.util.HashMap<java.lang.String, android.util.Pair<java.lang.String, java.lang.String>> overrideMap = this.mOemModeMap.get(items[0]);
                        if (overrideMap == null) {
                            overrideMap = new java.util.HashMap<>();
                            this.mOemModeMap.put(items[0], overrideMap);
                        }
                        if (!overrideMap.containsKey(items[1])) {
                            if (items.length == 3) {
                                overrideMap.put(items[1], new android.util.Pair<>(items[2], ""));
                            } else {
                                overrideMap.put(items[1], new android.util.Pair<>(items[2], items[3]));
                            }
                        }
                    }
                }
            }
        }

        private java.lang.String applyOemOverrideFunction(java.lang.String usbFunctions) {
            java.lang.String newFunction;
            if (usbFunctions == null || this.mOemModeMap == null) {
                return usbFunctions;
            }
            java.lang.String bootMode = getSystemProperty(com.android.server.usb.UsbDeviceManager.BOOT_MODE_PROPERTY, "unknown");
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "applyOemOverride usbfunctions=" + usbFunctions + " bootmode=" + bootMode);
            java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.String>> overridesMap = this.mOemModeMap.get(bootMode);
            if (overridesMap != null && !bootMode.equals(com.android.server.usb.UsbDeviceManager.NORMAL_BOOT) && !bootMode.equals("unknown")) {
                android.util.Pair<java.lang.String, java.lang.String> overrideFunctions = overridesMap.get(usbFunctions);
                if (overrideFunctions != null) {
                    android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "OEM USB override: " + usbFunctions + " ==> " + ((java.lang.String) overrideFunctions.first) + " persist across reboot " + ((java.lang.String) overrideFunctions.second));
                    if (!((java.lang.String) overrideFunctions.second).equals("")) {
                        if (isAdbEnabled()) {
                            newFunction = addFunction((java.lang.String) overrideFunctions.second, com.android.server.integrity.AppIntegrityManagerServiceImpl.ADB_INSTALLER);
                        } else {
                            newFunction = (java.lang.String) overrideFunctions.second;
                        }
                        android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "OEM USB override persisting: " + newFunction + "in prop: " + getPersistProp(false));
                        setSystemProperty(getPersistProp(false), newFunction);
                    }
                    return (java.lang.String) overrideFunctions.first;
                }
                if (isAdbEnabled()) {
                    java.lang.String newFunction2 = addFunction("none", com.android.server.integrity.AppIntegrityManagerServiceImpl.ADB_INSTALLER);
                    setSystemProperty(getPersistProp(false), newFunction2);
                } else {
                    setSystemProperty(getPersistProp(false), "none");
                }
            }
            return usbFunctions;
        }

        private boolean waitForState(java.lang.String state) {
            java.lang.String value = null;
            for (int i = 0; i < 20; i++) {
                value = getSystemProperty(USB_STATE_PROPERTY, "");
                if (state.equals(value)) {
                    return true;
                }
                android.os.SystemClock.sleep(120L);
            }
            com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbSetFunctionFailedRecord(state, value, "qcom function error");
            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "waitForState(" + state + ") FAILED: got " + value);
            return false;
        }

        private void setUsbConfig(java.lang.String config) {
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "setUsbConfig(" + config + ")");
            setSystemProperty(USB_CONFIG_PROPERTY, config);
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        protected void setEnabledFunctions(long usbFunctions, boolean forceRestart, int operationId) {
            boolean usbDataUnlocked = isUsbDataTransferActive(usbFunctions);
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "setEnabledFunctions functions=" + usbFunctions + " ,forceRestart=" + forceRestart + " ,usbDataUnlocked=" + usbDataUnlocked + " ,mUsbDataUnlocked=" + this.mUsbDataUnlocked + " ,operationId=" + operationId);
            if (usbDataUnlocked != this.mUsbDataUnlocked) {
                this.mUsbDataUnlocked = usbDataUnlocked;
                updateUsbNotification(false);
                forceRestart = true;
            }
            long oldFunctions = this.mCurrentFunctions;
            boolean oldFunctionsApplied = this.mCurrentFunctionsApplied;
            if (trySetEnabledFunctions(usbFunctions, forceRestart)) {
                return;
            }
            if (oldFunctionsApplied && oldFunctions != usbFunctions) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Failsafe 1: Restoring previous USB functions.");
                if (trySetEnabledFunctions(oldFunctions, false)) {
                    return;
                }
            }
            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Failsafe 2: Restoring default USB functions.");
            if (trySetEnabledFunctions(0L, false)) {
                return;
            }
            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Failsafe 3: Restoring empty function list (with ADB if enabled).");
            if (trySetEnabledFunctions(0L, false)) {
                return;
            }
            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Unable to set any USB functions!");
        }

        private boolean isNormalBoot() {
            getSystemProperty(com.android.server.usb.UsbDeviceManager.BOOT_MODE_PROPERTY, "unknown");
            return com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().isNormalBoot();
        }

        protected java.lang.String applyAdbFunction(java.lang.String functions) {
            if (functions == null) {
                functions = "";
            }
            if (isAdbEnabled()) {
                return addFunction(functions, com.android.server.integrity.AppIntegrityManagerServiceImpl.ADB_INSTALLER);
            }
            return removeFunction(functions, com.android.server.integrity.AppIntegrityManagerServiceImpl.ADB_INSTALLER);
        }

        private boolean trySetEnabledFunctions(long usbFunctions, boolean forceRestart) {
            boolean forceRestart2;
            long usbFunctions2 = com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbTetheringSwitchOffFunctions(usbFunctions, this.mCurrentFunctionsStr);
            java.lang.String functions = usbFunctions2 != 0 ? android.hardware.usb.UsbManager.usbFunctionsToString(usbFunctions2) : null;
            this.mCurrentFunctions = usbFunctions2;
            if (functions == null || applyAdbFunction(functions).equals("none")) {
                java.lang.String newFunction = functions;
                functions = getSystemProperty(getPersistProp(true), "none");
                if (newFunction == null) {
                    if (containsFunction(functions, "mtp")) {
                        functions = removeFunction(functions, "mtp");
                    }
                    if (containsFunction(functions, "ptp")) {
                        functions = removeFunction(functions, "ptp");
                    }
                }
                if (functions.equals("none")) {
                    functions = android.hardware.usb.UsbManager.usbFunctionsToString(getChargingFunctions());
                }
            }
            java.lang.String functions2 = applyAdbFunction(functions);
            boolean isTelecomRequire = com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().isTelecomRequirement(functions2);
            if (!isTelecomRequire) {
                forceRestart2 = forceRestart;
            } else {
                functions2 = "rndis,serial_cdev,diag,adb";
                forceRestart2 = true;
            }
            java.lang.String oemFunctions = applyOemOverrideFunction(functions2);
            if (!isNormalBoot() && !this.mCurrentFunctionsStr.equals(functions2)) {
                setSystemProperty(getPersistProp(true), functions2);
            } else if (isNormalBoot() && functions2.equals("midi")) {
                forceRestart2 = true;
            }
            com.android.server.usb.OplusUsbDeviceFunctionInfo info = new com.android.server.usb.OplusUsbDeviceFunctionInfo(functions2, oemFunctions, this.mCurrentFunctionsStr, this.mCurrentFunctionsApplied, forceRestart2, this.mCurrentOemFunctions);
            com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().printFunctionsForDebug(info);
            if ((functions2.equals(oemFunctions) || this.mCurrentOemFunctions.equals(oemFunctions)) && this.mCurrentFunctionsStr.equals(functions2) && this.mCurrentFunctionsApplied && !forceRestart2) {
                return true;
            }
            this.mCurrentFunctionsStr = functions2;
            this.mCurrentOemFunctions = oemFunctions;
            this.mCurrentFunctionsApplied = false;
            setUsbConfig("none");
            if (!waitForState("none")) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Failed to kick USB config");
                return false;
            }
            setUsbConfig(oemFunctions);
            if (this.mBootCompleted && (containsFunction(functions2, "mtp") || containsFunction(functions2, "ptp"))) {
                updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
            }
            if (waitForState(oemFunctions)) {
                this.mCurrentFunctionsApplied = true;
                return true;
            }
            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Failed to switch USB config to " + functions2);
            return false;
        }

        private java.lang.String getPersistProp(boolean functions) {
            java.lang.String bootMode = getSystemProperty(com.android.server.usb.UsbDeviceManager.BOOT_MODE_PROPERTY, "unknown");
            if ((bootMode.equals("reboot") && android.os.SystemProperties.getBoolean("persist.sys.allcommode", false)) || bootMode.equals(com.android.server.usb.UsbDeviceManager.NORMAL_BOOT) || bootMode.equals("unknown")) {
                return "persist.sys.usb.config";
            }
            if (functions) {
                java.lang.String persistProp = "persist.sys.usb." + bootMode + ".func";
                return persistProp;
            }
            java.lang.String persistProp2 = "persist.sys.usb." + bootMode + ".config";
            return persistProp2;
        }

        private static java.lang.String addFunction(java.lang.String functions, java.lang.String function) {
            if ("none".equals(functions)) {
                return function;
            }
            if (!containsFunction(functions, function)) {
                if (functions.length() > 0) {
                    functions = functions + ",";
                }
                return functions + function;
            }
            return functions;
        }

        private static java.lang.String removeFunction(java.lang.String functions, java.lang.String function) {
            java.lang.String[] split = functions.split(",");
            for (int i = 0; i < split.length; i++) {
                if (function.equals(split[i])) {
                    split[i] = null;
                }
            }
            int i2 = split.length;
            if (i2 == 1 && split[0] == null) {
                return "none";
            }
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            for (java.lang.String s : split) {
                if (s != null) {
                    if (builder.length() > 0) {
                        builder.append(",");
                    }
                    builder.append(s);
                }
            }
            return builder.toString();
        }

        static boolean containsFunction(java.lang.String functions, java.lang.String function) {
            int index = functions.indexOf(function);
            if (index < 0) {
                return false;
            }
            if (index > 0 && functions.charAt(index - 1) != ',') {
                return false;
            }
            int charAfter = function.length() + index;
            if (charAfter < functions.length() && functions.charAt(charAfter) != ',') {
                return false;
            }
            return true;
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void setCurrentUsbFunctionsCb(long functions, int status, int mRequest, long mFunctions, boolean mChargingFunctions) {
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void getUsbSpeedCb(int speed) {
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void resetCb(int status) {
        }
    }

    private static final class UsbHandlerHal extends com.android.server.usb.UsbDeviceManager.UsbHandler {
        private static final int ENUMERATION_TIME_OUT_MS = 2000;
        protected static final java.lang.String GADGET_HAL_FQ_NAME = "android.hardware.usb.gadget@1.0::IUsbGadget";
        private static final int SET_FUNCTIONS_LEEWAY_MS = 500;
        private static final int SET_FUNCTIONS_TIMEOUT_MS = 3000;
        private static final int USB_GADGET_HAL_DEATH_COOKIE = 2000;
        private int mCurrentRequest;
        protected boolean mCurrentUsbFunctionsRequested;
        private final java.lang.Object mGadgetProxyLock;

        UsbHandlerHal(android.os.Looper looper, android.content.Context context, com.android.server.usb.UsbDeviceManager deviceManager, com.android.server.usb.UsbAlsaManager alsaManager, com.android.server.usb.UsbPermissionManager permissionManager) {
            super(looper, context, deviceManager, alsaManager, permissionManager);
            this.mGadgetProxyLock = new java.lang.Object();
            this.mCurrentRequest = 0;
            com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
            try {
                synchronized (this.mGadgetProxyLock) {
                    this.mCurrentFunctions = 0L;
                    this.mCurrentUsbFunctionsRequested = true;
                    this.mUsbSpeed = -1;
                    this.mCurrentGadgetHalVersion = 10;
                    updateUsbGadgetHalVersion();
                }
                java.lang.String state = android.os.FileUtils.readTextFile(new java.io.File(com.android.server.usb.UsbDeviceManager.STATE_PATH), 0, null).trim();
                updateState(state);
            } catch (java.util.NoSuchElementException e) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Usb gadget hal not found", e);
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Error initializing UsbHandler", e2);
            }
        }

        final class UsbGadgetDeathRecipient implements android.os.IHwBinder.DeathRecipient {
            UsbGadgetDeathRecipient() {
            }

            public void serviceDied(long cookie) {
                if (cookie == 2000) {
                    android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Usb Gadget hal service died cookie: " + cookie);
                    synchronized (com.android.server.usb.UsbDeviceManager.UsbHandlerHal.this.mGadgetProxyLock) {
                        com.android.server.usb.UsbDeviceManager.mUsbGadgetHal = null;
                    }
                }
            }
        }

        final class ServiceNotification extends android.hidl.manager.V1_0.IServiceNotification.Stub {
            ServiceNotification() {
            }

            @Override // android.hidl.manager.V1_0.IServiceNotification
            public void onRegistration(java.lang.String fqName, java.lang.String name, boolean preexisting) {
                android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "Usb gadget hal service started " + fqName + " " + name);
                if (!fqName.equals(com.android.server.usb.UsbDeviceManager.UsbHandlerHal.GADGET_HAL_FQ_NAME)) {
                    android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "fqName does not match");
                } else {
                    com.android.server.usb.UsbDeviceManager.UsbHandlerHal.this.sendMessage(18, preexisting);
                }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler, android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 14:
                    setEnabledFunctions(0L, false, com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet());
                    return;
                case 15:
                    int operationId = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Set functions timed out! no reply from usb hal ,operationId:" + operationId);
                    if (msg.arg1 != 1) {
                        setEnabledFunctions(this.mScreenUnlockedFunctions, false, operationId);
                    }
                    com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbMtkSetFunctionTimeoutRecord("Set functions timed out! no reply from usb hal", "mtk timeout");
                    return;
                case 16:
                    android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "processing MSG_GET_CURRENT_USB_FUNCTIONS");
                    this.mCurrentUsbFunctionsReceived = true;
                    int operationId2 = msg.arg2;
                    if (this.mCurrentUsbFunctionsRequested) {
                        android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "updating mCurrentFunctions");
                        this.mCurrentFunctions = ((java.lang.Long) msg.obj).longValue() & (-2);
                        android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "mCurrentFunctions:" + this.mCurrentFunctions + "applied:" + msg.arg1);
                        this.mCurrentFunctionsApplied = msg.arg1 == 1;
                    }
                    finishBoot(operationId2);
                    return;
                case 17:
                    int operationId3 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    if (msg.arg1 != 1) {
                        if (this.mCurrentFunctions == 2) {
                            notifyAccessoryModeExit(operationId3);
                        } else {
                            setEnabledFunctions(this.mScreenUnlockedFunctions, false, operationId3);
                        }
                    }
                    com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbMtkSwitchFunctionTimeoutRecord("Switch functions timed out", "mtk timeout");
                    return;
                case 18:
                    boolean preexisting = msg.arg1 == 1;
                    int operationId4 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    synchronized (this.mGadgetProxyLock) {
                        try {
                            com.android.server.usb.UsbDeviceManager.mUsbGadgetHal = com.android.server.usb.hal.gadget.UsbGadgetHalInstance.getInstance(this.mUsbDeviceManager, null);
                        } catch (java.util.NoSuchElementException e) {
                            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Usb gadget hal not found", e);
                        }
                        if (!this.mCurrentFunctionsApplied && !preexisting) {
                            setEnabledFunctions(this.mCurrentFunctions, false, operationId4);
                            break;
                        } else {
                            break;
                        }
                    }
                    return;
                case 19:
                    int operationId5 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    synchronized (this.mGadgetProxyLock) {
                        if (com.android.server.usb.UsbDeviceManager.mUsbGadgetHal == null) {
                            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "reset Usb Gadget mUsbGadgetHal is null");
                            return;
                        }
                        try {
                            removeMessages(8);
                            if (this.mConfigured) {
                                this.mResetUsbGadgetDisableDebounce = true;
                            }
                            com.android.server.usb.UsbDeviceManager.mUsbGadgetHal.reset(operationId5);
                            break;
                        } catch (java.lang.Exception e2) {
                            android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "reset Usb Gadget failed", e2);
                            this.mResetUsbGadgetDisableDebounce = false;
                        }
                        return;
                    }
                case 20:
                case 21:
                default:
                    super.handleMessage(msg);
                    return;
                case 22:
                    int operationId6 = com.android.server.usb.UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    if (com.android.server.usb.UsbDeviceManager.mUsbGadgetHal == null) {
                        android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "mGadgetHal is null, operationId:" + operationId6);
                        return;
                    }
                    try {
                        com.android.server.usb.UsbDeviceManager.mUsbGadgetHal.getUsbSpeed(operationId6);
                        return;
                    } catch (java.lang.Exception e3) {
                        android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "get UsbSpeed failed", e3);
                        return;
                    }
                case 23:
                    if (com.android.server.usb.UsbDeviceManager.mUsbGadgetHal == null) {
                        android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "mUsbGadgetHal is null");
                        return;
                    }
                    try {
                        this.mCurrentGadgetHalVersion = com.android.server.usb.UsbDeviceManager.mUsbGadgetHal.getGadgetHalVersion();
                        return;
                    } catch (android.os.RemoteException e4) {
                        android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "update Usb gadget version failed", e4);
                        return;
                    }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void setCurrentUsbFunctionsCb(long functions, int status, int mRequest, long mFunctions, boolean mChargingFunctions) {
            if (this.mCurrentRequest != mRequest || !hasMessages(15) || mFunctions != functions) {
                return;
            }
            removeMessages(15);
            android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "notifyCurrentFunction request:" + mRequest + " status:" + status);
            if (status == 0) {
                this.mCurrentFunctionsApplied = true;
            } else if (!mChargingFunctions) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Setting default fuctions");
                sendEmptyMessage(14);
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void getUsbSpeedCb(int speed) {
            this.mUsbSpeed = speed;
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void resetCb(int status) {
            if (status != 0) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "resetCb fail");
            }
        }

        private void setUsbConfig(long config, boolean chargingFunctions, int operationId) throws java.lang.Throwable {
            java.lang.String str = com.android.server.usb.UsbDeviceManager.TAG;
            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("setUsbConfig(").append(config).append(") request:");
            int i = this.mCurrentRequest + 1;
            this.mCurrentRequest = i;
            android.util.Slog.d(str, sbAppend.append(i).toString());
            removeMessages(17);
            removeMessages(15);
            removeMessages(14);
            synchronized (this.mGadgetProxyLock) {
                try {
                    try {
                        if (com.android.server.usb.UsbDeviceManager.mUsbGadgetHal != null) {
                            try {
                                if ((1 & config) != 0) {
                                    ((android.debug.AdbManagerInternal) com.android.server.LocalServices.getService(android.debug.AdbManagerInternal.class)).startAdbdForTransport((byte) 0);
                                } else {
                                    ((android.debug.AdbManagerInternal) com.android.server.LocalServices.getService(android.debug.AdbManagerInternal.class)).stopAdbdForTransport((byte) 0);
                                }
                            } catch (java.lang.Exception e) {
                                e = e;
                            }
                            try {
                                com.android.server.usb.UsbDeviceManager.mUsbGadgetHal.setCurrentUsbFunctions(this.mCurrentRequest, config, chargingFunctions, 2500, operationId);
                                sendMessageDelayed(15, chargingFunctions, 3000L);
                                if (this.mConnected) {
                                    sendMessageDelayed(17, chargingFunctions, 5000L);
                                }
                                android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "timeout message queued");
                            } catch (java.lang.Exception e2) {
                                e = e2;
                                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Remoteexception while calling setCurrentUsbFunctions", e);
                            }
                            return;
                        }
                        android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "setUsbConfig mUsbGadgetHal is null");
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        protected void setEnabledFunctions(long functions, boolean forceRestart, int operationId) throws java.lang.Throwable {
            android.util.Slog.d(com.android.server.usb.UsbDeviceManager.TAG, "setEnabledFunctionsi functions=" + functions + ", forceRestart=" + forceRestart + ", operationId=" + operationId);
            if (this.mCurrentGadgetHalVersion < 12 && (1024 & functions) != 0) {
                android.util.Slog.e(com.android.server.usb.UsbDeviceManager.TAG, "Could not set unsupported function for the GadgetHal");
                return;
            }
            if (this.mCurrentFunctions != functions || !this.mCurrentFunctionsApplied || forceRestart) {
                android.util.Slog.i(com.android.server.usb.UsbDeviceManager.TAG, "Setting USB config to " + android.hardware.usb.UsbManager.usbFunctionsToString(functions));
                this.mCurrentFunctions = functions;
                this.mCurrentFunctionsApplied = false;
                this.mCurrentUsbFunctionsRequested = false;
                boolean chargingFunctions = functions == 0;
                long functions2 = getAppliedFunctions(functions);
                setUsbConfig(0L, chargingFunctions, operationId);
                setUsbConfig(functions2, chargingFunctions, operationId);
                if (this.mBootCompleted && isUsbDataTransferActive(functions2)) {
                    updateUsbStateBroadcastIfNeeded(functions2);
                }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void handlerInitDone(int operationId) {
            com.android.server.usb.UsbDeviceManager.mUsbGadgetHal.getCurrentUsbFunctions(operationId);
        }
    }

    public android.hardware.usb.UsbAccessory getCurrentAccessory() {
        return this.mHandler.getCurrentAccessory();
    }

    public android.os.ParcelFileDescriptor openAccessory(android.hardware.usb.UsbAccessory accessory, com.android.server.usb.UsbUserPermissionManager permissions, int pid, int uid) {
        android.hardware.usb.UsbAccessory currentAccessory = this.mHandler.getCurrentAccessory();
        if (currentAccessory == null) {
            throw new java.lang.IllegalArgumentException("no accessory attached");
        }
        if (!currentAccessory.equals(accessory)) {
            java.lang.String error = accessory.toString() + " does not match current accessory " + currentAccessory;
            throw new java.lang.IllegalArgumentException(error);
        }
        permissions.checkPermission(accessory, pid, uid);
        return nativeOpenAccessory();
    }

    public long getCurrentFunctions() {
        return this.mHandler.getEnabledFunctions();
    }

    public int getCurrentUsbSpeed() {
        return this.mHandler.getUsbSpeed();
    }

    public int getGadgetHalVersion() {
        return this.mHandler.getGadgetHalVersion();
    }

    public void setCurrentUsbFunctionsCb(long functions, int status, int mRequest, long mFunctions, boolean mChargingFunctions) {
        this.mHandler.setCurrentUsbFunctionsCb(functions, status, mRequest, mFunctions, mChargingFunctions);
    }

    public void getCurrentUsbFunctionsCb(long functions, int status) {
        this.mHandler.sendMessage(16, java.lang.Long.valueOf(functions), status == 2);
    }

    public void getUsbSpeedCb(int speed) {
        this.mHandler.getUsbSpeedCb(speed);
    }

    public void resetCb(int status) {
        this.mHandler.resetCb(status);
    }

    public android.os.ParcelFileDescriptor getControlFd(long usbFunction) {
        java.io.FileDescriptor fd = this.mControlFds.get(java.lang.Long.valueOf(usbFunction));
        if (fd == null) {
            return null;
        }
        try {
            return android.os.ParcelFileDescriptor.dup(fd);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Could not dup fd for " + usbFunction);
            return null;
        }
    }

    public long getScreenUnlockedFunctions() {
        return this.mHandler.getScreenUnlockedFunctions();
    }

    public void setCurrentFunctions(long functions, int operationId) {
        android.util.Slog.d(TAG, "setCurrentFunctions(" + android.hardware.usb.UsbManager.usbFunctionsToString(functions) + ")");
        if (functions == 0) {
            com.android.internal.logging.MetricsLogger.action(this.mContext, 1275);
        } else if (functions == 4) {
            com.android.internal.logging.MetricsLogger.action(this.mContext, 1276);
        } else if (functions == 16) {
            com.android.internal.logging.MetricsLogger.action(this.mContext, 1277);
        } else if (functions == 8) {
            com.android.internal.logging.MetricsLogger.action(this.mContext, 1279);
        } else if (functions == 32) {
            if (((com.android.server.usb.IOplusUsbDeviceFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.usb.IOplusUsbDeviceFeature.DEFAULT, new java.lang.Object[0])).isUsbTetheringDisabled(this.mContext)) {
                return;
            } else {
                com.android.internal.logging.MetricsLogger.action(this.mContext, 1278);
            }
        } else if (functions == 2) {
            com.android.internal.logging.MetricsLogger.action(this.mContext, 1280);
        }
        this.mHandler.sendMessage(2, java.lang.Long.valueOf(functions), operationId);
        int callingPid = android.os.Binder.getCallingPid();
        android.util.Slog.d(TAG, "printCallerPkg pid: " + callingPid);
        getOplusUsbDeviceFeature().printCallerPkg(callingPid);
    }

    public void setScreenUnlockedFunctions(long functions) {
        android.util.Slog.d(TAG, "setScreenUnlockedFunctions(" + android.hardware.usb.UsbManager.usbFunctionsToString(functions) + ")");
        this.mHandler.sendMessage(12, java.lang.Long.valueOf(functions));
    }

    public void resetUsbGadget() {
        android.util.Slog.d(TAG, "reset Usb Gadget");
        this.mHandler.sendMessage(19, (java.lang.Object) null);
    }

    private void onAdbEnabled(boolean enabled) {
        int operationId = sUsbOperationCount.incrementAndGet();
        this.mHandler.sendMessage(1, enabled, operationId);
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        if (this.mHandler != null) {
            this.mHandler.dump(dump, "handler", 1146756268033L);
            sEventLogger.dump(new com.android.server.usb.DualOutputStreamDumpSink(dump, 1138166333457L));
        }
        dump.end(token);
    }

    public static com.android.server.usb.IOplusUsbDeviceFeature getOplusUsbDeviceFeature() {
        if (mIOplusUsbDeviceFeature == null) {
            mIOplusUsbDeviceFeature = (com.android.server.usb.IOplusUsbDeviceFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.usb.IOplusUsbDeviceFeature.DEFAULT, new java.lang.Object[0]);
        }
        return mIOplusUsbDeviceFeature;
    }

    private static class UsbDeviceManagerWrapper {
        private final com.android.server.engineer.IOplusEngineerServiceExt mEngineerServiceExt;

        private UsbDeviceManagerWrapper() {
            this.mEngineerServiceExt = (com.android.server.engineer.IOplusEngineerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.engineer.IOplusEngineerServiceExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.engineer.IOplusEngineerServiceExt getExtImpl() {
            return this.mEngineerServiceExt;
        }
    }
}
